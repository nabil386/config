package curam.ca.gc.bdm.batch.bdmfifinboundbatch.impl;

import curam.ca.gc.bdm.batch.bdmfifinboundbatch.fact.BDMFIFInboundBatchStreamFactory;
import curam.ca.gc.bdm.batch.bdmfifinboundbatch.struct.BDMFIFBankName;
import curam.ca.gc.bdm.batch.bdmfifinboundbatch.struct.BDMFIFBatchParameters;
import curam.ca.gc.bdm.batch.bdmfifinboundbatch.struct.BDMFIFReport;
import curam.ca.gc.bdm.entity.fact.BDMFIFStageInboundFactory;
import curam.ca.gc.bdm.entity.intf.BDMFIFStageInbound;
import curam.ca.gc.bdm.entity.struct.BDMFIFStageInboundDtls;
import curam.ca.gc.bdm.entity.struct.BDMFIFStageInboundDtlsList;
import curam.codetable.BATCHPROCESSNAME;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.BankFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.impl.BatchStreamHelper;
import curam.core.impl.SecurityImplementationFactory;
import curam.core.intf.Bank;
import curam.core.struct.BankDtls;
import curam.core.struct.BankNameStructRef;
import curam.core.struct.BatchProcessChunkDtlsList;
import curam.core.struct.BatchProcessDtls;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingIDList;
import curam.core.struct.ChunkMainParameters;
import curam.util.dataaccess.CuramValueList;
import curam.util.dataaccess.DynamicDataAccess;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Trace;
import curam.util.type.Date;
import curam.util.type.NotFoundIndicator;
import curam.validationmanager.sl.entity.fact.ValidationConfigurationFactory;
import curam.validationmanager.sl.entity.intf.ValidationConfiguration;
import curam.validationmanager.sl.entity.struct.ValidationConfigurationDetails;
import curam.validationmanager.sl.entity.struct.ValidationConfigurationDtls;
import curam.validationmanager.sl.entity.struct.ValidationConfigurationExceptionKey;
import curam.validationmanager.sl.entity.struct.ValidationConfigurationKey;
import org.apache.commons.lang3.StringUtils;

public class BDMFIFInboundBatch
  extends curam.ca.gc.bdm.batch.bdmfifinboundbatch.base.BDMFIFInboundBatch {

  /**
   * Identifies the configurable validation for a branch having active
   * accounts.
   */
  private static final String kBankBranchException =
    "bpobankbranch.err_bankbranch_xrv_at_least_one_open_account|a|";

  /** The configurable validation for duplicate branch names. */
  private static final String kBankBranchNameException =
    "bpovalidatebankbranch.err_bankbranch_dre_name|a|1";

  private static final String kModifyCancelledBankBranchException =
    "general.err_general_fv_no_modify_record_cancelled|a|36";

  public static String bankBranchCountSQL;;

  public static String blankAdressSQL;

  public static String blankPOCodeSQL;

  public static String uniqueBankNamesSQL;

  public static String bankCountSQL;

  public static String skippedRecordSQL;

  public static String skippedRecordByStatusSQL;

  /**
   * Batch entry point.
   */
  @Override
  public void process(final BDMFIFBatchParameters parameters)
    throws AppException, InformationalException {

    final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();
    final ChunkMainParameters chunkMainParameters = new ChunkMainParameters();

    final curam.ca.gc.bdm.batch.bdmfifinboundbatch.intf.BDMFIFInboundBatchStream streamObj =
      BDMFIFInboundBatchStreamFactory.newInstance();
    insertUniqueBank();

    final BDMFIFInboundBatchStreamWrapper streamWrapper =
      new BDMFIFInboundBatchStreamWrapper(streamObj, parameters);

    final BDMFIFInboundBatchWrapper chunkerWrapper =
      new BDMFIFInboundBatchWrapper(this);

    SecurityImplementationFactory.register();

    // Set the start time of the program
    batchStreamHelper.setStartTime();

    chunkMainParameters.chunkSize = 100;
    chunkMainParameters.dontRunStream = false;
    chunkMainParameters.processUnProcessedChunks = true;
    chunkMainParameters.startChunkKey = 1;
    chunkMainParameters.unProcessedChunkReadWait = 1000;

    final BatchProcessingIDList batchProcessingIDList =
      getBatchProcessingIDList();

    // Configure the validation to allow a branch to be closed even if
    // it has active accounts: configurable=true, enabled=false.
    updateConfigurableValidation(kBankBranchException, true, false);
    // Disable the 'name already exists' exception when closing a branch.
    updateConfigurableValidation(kBankBranchNameException, true, false);

    updateConfigurableValidation(kModifyCancelledBankBranchException, true,
      false);

    final String instanceID;
    if (!StringUtils.isEmpty(parameters.instanceID)) {
      instanceID = parameters.instanceID;
    } else {
      instanceID = BATCHPROCESSNAME.BDM_FIF_INBOUND;
    }
    streamWrapper.setInstanceID(instanceID);

    batchStreamHelper.runChunkMain(instanceID, parameters, chunkerWrapper,
      batchProcessingIDList, chunkMainParameters, streamWrapper);

    // Revert it to its ootb state: configurable=false, enabled=true.
    // Note that this value is cached so this won't take effect on this
    // vm, only on the next vm which starts up.
    updateConfigurableValidation(kBankBranchException, false, true);
    updateConfigurableValidation(kBankBranchNameException, false, true);

    dbBatchReport();

    // streamWrapper.displayExecutionSummary();

  }

  // Insert new bank entries
  private void insertUniqueBank()
    throws AppException, InformationalException {

    final BDMFIFBankName bankNameDtls = new BDMFIFBankName();
    final String sql = "SELECT DISTINCT BRANCHNAMEEN INTO :bankName FROM "
      + "BDMFIFSTAGEINBOUND WHERE banksortcode NOT IN (SELECT banksortcode FROM BANKBRANCH)";

    final CuramValueList<BDMFIFBankName> bankNameList = DynamicDataAccess
      .executeNsMulti(BDMFIFBankName.class, bankNameDtls, false, false, sql);

    final Bank bank = BankFactory.newInstance();

    BankDtls bankDtls;
    final BankNameStructRef ref = new BankNameStructRef();
    final NotFoundIndicator indicator = new NotFoundIndicator();
    final curam.core.intf.UniqueID uniqueIDObj =
      UniqueIDFactory.newInstance();

    int bankCount = 0;

    for (final BDMFIFBankName fifDtls : bankNameList) {
      bankDtls = new BankDtls();
      bankDtls.name = fifDtls.bankName;
      ref.name = fifDtls.bankName;
      ref.statusCode = RECORDSTATUS.DEFAULTCODE;

      bank.readBankByName(indicator, ref);
      if (indicator.isNotFound()) {
        bankCount++;
        bankDtls.startDate = Date.getCurrentDate();
        bankDtls.bankStatus = curam.codetable.BANKSTATUS.DEFAULTCODE;
        bankDtls.statusCode = curam.codetable.RECORDSTATUS.NORMAL;
        bankDtls.bankID = uniqueIDObj.getNextID();
        bank.insert(bankDtls);
      } else {
        Trace.kTopLevelLogger.info("No New bank entry found");
      }

    }

    Trace.kTopLevelLogger.info("Total Inserted Bank: " + bankCount);
  }

  /**
   * Generates a list of mock record numbers for use by the streamer.
   * Each number represents the index of an entry in the list of lines from
   * the file rather than a primary key. So we just return a list sequential
   * numbers from zero up to the requested number of entries.
   *
   * @param parameters The overall batch program parameters.
   * @param numOfEntries The number of entries in the file.
   * @return
   */
  private BatchProcessingIDList getBatchProcessingIDList()
    throws AppException, InformationalException {

    final BatchProcessingIDList idList = new BatchProcessingIDList();

    final BDMFIFStageInbound inboundObj =
      BDMFIFStageInboundFactory.newInstance();
    BDMFIFStageInboundDtlsList dtlsList = new BDMFIFStageInboundDtlsList();
    dtlsList = inboundObj.nkreadmulti();

    BatchProcessingID batchProcessingID;

    for (final BDMFIFStageInboundDtls processChunkDtls : dtlsList.dtls) {

      batchProcessingID = new BatchProcessingID();
      batchProcessingID.recordID = processChunkDtls.recordID;
      idList.dtls.add(batchProcessingID);

    }

    return idList;
  }

  /**
   * Disables the validation against a branch having active accounts.
   *
   * @throws AppException
   * @throws InformationalException
   */
  private void updateConfigurableValidation(final String exceptionID,
    final boolean isConfigurable, final boolean isEnabled)
    throws AppException, InformationalException {

    // Disable the validation which prevents a branch from being closed.
    final ValidationConfiguration validationConfig =
      ValidationConfigurationFactory.newInstance();
    final ValidationConfigurationExceptionKey exceptionIDKey =
      new ValidationConfigurationExceptionKey();
    exceptionIDKey.exceptionID = exceptionID;

    final ValidationConfigurationDetails valDetails =
      validationConfig.readByExceptionID(exceptionIDKey);
    final ValidationConfigurationKey valKey =
      new ValidationConfigurationKey();
    valKey.validationConfigurationID = valDetails.validationConfigurationID;
    final ValidationConfigurationDtls valDtls = validationConfig.read(valKey);
    valDtls.enabled = isEnabled;
    valDtls.configurable = isConfigurable;
    validationConfig.modify(valKey, valDtls);

  }

  public void dbBatchReport() throws AppException, InformationalException {

    getSQL();
    Trace.kTopLevelLogger.info(
      "++++++++++++++++++++++++++++++++++++++\nDB Reports\n++++++++++++++++++++++++++++++++++++++");

    final BDMFIFReport bdmfifReport4 = (BDMFIFReport) DynamicDataAccess
      .executeNs(BDMFIFReport.class, null, false, bankCountSQL);
    Trace.kTopLevelLogger
      .info("Number of Bank count: " + bdmfifReport4.countResult);

    final BDMFIFReport bdmfifReport =
      (BDMFIFReport) DynamicDataAccess.executeNs(BDMFIFReport.class, null,
        false,
        "SELECT 'Bank Branch Count',count(*) FROM BANKBRANCH INTO :recordDetails, :countResult WHERE banksortcode IN (SELECT banksortcode FROM BDMFIFSTAGEINBOUND)");
    Trace.kTopLevelLogger
      .info("Number of Bank branch: " + bdmfifReport.countResult);

    final BDMFIFReport bdmfifReport3 = (BDMFIFReport) DynamicDataAccess
      .executeNs(BDMFIFReport.class, null, false, uniqueBankNamesSQL);
    Trace.kTopLevelLogger
      .info("Number of distinct Bank names: " + bdmfifReport3.countResult);

    final BDMFIFReport bdmfifReport1 = (BDMFIFReport) DynamicDataAccess
      .executeNs(BDMFIFReport.class, null, false, blankAdressSQL);
    Trace.kTopLevelLogger.info(
      "Number of records with blank address: " + bdmfifReport1.countResult);

    final BDMFIFReport bdmfifReport2 = (BDMFIFReport) DynamicDataAccess
      .executeNs(BDMFIFReport.class, null, false, blankPOCodeSQL);
    Trace.kTopLevelLogger.info(
      "Number of records with blank po code: " + bdmfifReport2.countResult);

    final BDMFIFReport bdmfifReport5 = (BDMFIFReport) DynamicDataAccess
      .executeNs(BDMFIFReport.class, null, false, skippedRecordSQL);

    Trace.kTopLevelLogger
      .info("Number of skipped records:" + bdmfifReport5.countResult);

    /*
     * final BDMFIFReport bdmfifReport6 = new BDMFIFReport();
     * DynamicDataAccess.executeNs(BDMFIFReport.class, bdmfifReport6, false,
     * skippedRecordByStatusSQL);
     *
     * Trace.kTopLevelLogger
     * .info("No of skipped records by status: " + bdmfifReport6.countResult
     * + " " + "Status: " + bdmfifReport6.branchStatus);
     */

  }

  public void getSQL() {

    bankBranchCountSQL =
      "SELECT 'Bank Branch Count',count(*) FROM BANKBRANCH INTO :recordDetails, :countResult WHERE "
        + "banksortcode IN (SELECT banksortcode FROM BDMFIFSTAGEINBOUND)";

    blankAdressSQL =
      "SELECT 'Number of records with address is blank',count(*) FROM BDMFIFSTAGEINBOUND INTO :recordDetails, :countResult WHERE branchstatus='BBS2' and BRANCHCIVICADDRESS is NULL AND BRANCHPOCODE is NULL";

    blankPOCodeSQL =
      "SELECT 'Number of records with blank PO Code',count(*) FROM BDMFIFSTAGEINBOUND INTO :recordDetails, :countResult WHERE branchstatus='BBS2' and branchpocode is NULL";

    uniqueBankNamesSQL =
      "SELECT 'Distinct Bank names',count(distinct BRANCHNAMEEN) FROM BDMFIFSTAGEINBOUND INTO :recordDetails, :countResult WHERE banksortcode in (select banksortcode FROM BANKBRANCH)";

    bankCountSQL =
      "SELECT 'Number of bank records - matches with FIF Stage',count(*) FROM BDMFIFSTAGEINBOUND INTO :recordDetails, :countResult WHERE  banksortcode in (SELECT banksortcode FROM BANKBRANCH)";

    skippedRecordSQL =
      "SELECT 'Number of records skipped',count(*) FROM BDMFIFSTAGEINBOUND INTO :recordDetails, :countResult WHERE banksortcode NOT IN (SELECT banksortcode FROM BANKBRANCH)";

    skippedRecordByStatusSQL =
      "SELECT 'Number of records skipped - by status',branchstatus,count(*) FROM BDMFIFSTAGEINBOUND  WHERE banksortcode NOT IN (SELECT banksortcode FROM BANKBRANCH) GROUP BY branchstatus";

  }

  @Override
  public void sendBatchReport(final String instanceID,
    final BatchProcessDtls batchProcessDtls,
    final BatchProcessChunkDtlsList processedBatchProcessChunkDtlsList,
    final BatchProcessChunkDtlsList unprocessedBatchProcessChunkDtlsList)
    throws AppException, InformationalException {

    Trace.kTopLevelLogger.info(
      "--chunks processed: " + processedBatchProcessChunkDtlsList.dtls.size()
        + " chunks unprocessed: "
        + unprocessedBatchProcessChunkDtlsList.dtls.size());

  }

}
