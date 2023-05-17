package curam.ca.gc.bdm.batch.bdmgeneratepaymentstagingdatabatch.impl;

import curam.ca.gc.bdm.batch.bdmbatchutil.impl.BDMBatchUtil;
import curam.ca.gc.bdm.batch.bdmgeneratepaymentstagingdatabatch.struct.BDMPaymentDetailRecord;
import curam.ca.gc.bdm.batch.bdmgeneratepaymentstagingdatabatch.struct.BDMPaymentDetailRecordList;
import curam.ca.gc.bdm.batch.bdmgeneratepaymentstagingdatabatch.struct.GeneratePaymentsFileKey;
import curam.ca.gc.bdm.codetable.BDMCDOCODE;
import curam.ca.gc.bdm.codetable.BDMDEDUCTIONTYPE;
import curam.ca.gc.bdm.codetable.BDMESDCCODE;
import curam.ca.gc.bdm.codetable.BDMPMTRECONCILIATIONSTATUS;
import curam.ca.gc.bdm.codetable.BDMPMTSTAGINGDATATYPE;
import curam.ca.gc.bdm.codetable.BDMREGIONCODE;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.fact.BDMPaymentInstrumentDAFactory;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.fact.BDMPaymentStagingDataFactory;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.intf.BDMPaymentInstrumentDA;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMCountILIsKey;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMPaymentStagingDataDtls;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMReadPaymentDueDateKey;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.CaseNomineeConcernRoleDtls;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.CaseNomineeKey;
import curam.ca.gc.bdm.entity.fact.BDMBatchFilenameFactory;
import curam.ca.gc.bdm.entity.fact.BDMILIStageDataFactory;
import curam.ca.gc.bdm.entity.fact.BDMProductFactory;
import curam.ca.gc.bdm.entity.financial.fact.BDMPaymentInstrumentFactory;
import curam.ca.gc.bdm.entity.financial.intf.BDMPaymentInstrument;
import curam.ca.gc.bdm.entity.financial.struct.BDMParticipantRoleID;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentInstrumentDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentInstrumentKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMPmtInstrumentDetails;
import curam.ca.gc.bdm.entity.financial.struct.BDMPmtInstrumentDetailsList;
import curam.ca.gc.bdm.entity.financial.struct.BDMPmtInstrumentKey;
import curam.ca.gc.bdm.entity.financial.struct.DOJILIsByInstrumentKey;
import curam.ca.gc.bdm.entity.financial.struct.DOJProcessInd;
import curam.ca.gc.bdm.entity.intf.BDMBatchFilename;
import curam.ca.gc.bdm.entity.intf.BDMILIStageData;
import curam.ca.gc.bdm.entity.intf.BDMProduct;
import curam.ca.gc.bdm.entity.struct.BDMBatchFilenameKeyStruct1;
import curam.ca.gc.bdm.entity.struct.BDMILIStageDataDtls;
import curam.ca.gc.bdm.entity.struct.BDMProductKey;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.sl.financial.impl.BDMFinancial;
import curam.ca.gc.bdm.sl.financial.struct.BDMILIFinancialCodingDetailsList;
import curam.ca.gc.bdm.sl.financial.struct.BDMILIFinancialCodingKey;
import curam.ca.gc.bdm.sl.financial.struct.ILIFinancialCodingDetails;
import curam.ca.gc.bdm.util.impl.BDMDateUtil;
import curam.ca.gc.bdm.util.payment.impl.BDMPaymentUtil;
import curam.codetable.ADDRESSELEMENTTYPE;
import curam.codetable.BATCHPROCESSNAME;
import curam.codetable.BATCHPROCESSRESULTSTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.COUNTRY;
import curam.codetable.CREDITDEBIT;
import curam.codetable.CURRENCY;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.codetable.ILICATEGORY;
import curam.codetable.LANGUAGE;
import curam.codetable.LOCALE;
import curam.codetable.METHODOFDELIVERY;
import curam.codetable.PERSONTITLE;
import curam.codetable.PMTRECONCILIATIONSTATUS;
import curam.codetable.PRODUCTNAME;
import curam.core.facade.fact.UniqueIDFactory;
import curam.core.fact.AddressFactory;
import curam.core.fact.AlternateNameFactory;
import curam.core.fact.BankAccountFactory;
import curam.core.fact.BankBranchFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.InstructionLineItemFactory;
import curam.core.fact.PaymentInstructionFactory;
import curam.core.fact.PaymentInstrumentFactory;
import curam.core.fact.PersonFactory;
import curam.core.fact.ProductDeliveryFactory;
import curam.core.impl.BatchStreamHelper;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.intf.Address;
import curam.core.intf.AlternateName;
import curam.core.intf.BankAccount;
import curam.core.intf.BankBranch;
import curam.core.intf.InstructionLineItem;
import curam.core.intf.PaymentInstruction;
import curam.core.intf.PaymentInstrument;
import curam.core.intf.Person;
import curam.core.intf.ProductDelivery;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor;
import curam.core.sl.infrastructure.entity.struct.CaseIDEvidenceTypeStatusesKey;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKeyList;
import curam.core.sl.infrastructure.paymentcorrection.entity.fact.PaymentCorrectionEvidenceFactory;
import curam.core.sl.infrastructure.paymentcorrection.entity.struct.PaymentCorrectionEvidenceDtls;
import curam.core.sl.infrastructure.paymentcorrection.entity.struct.PaymentCorrectionEvidenceDtlsList;
import curam.core.sl.struct.RecordCount;
import curam.core.struct.ActiveCasesConcernRoleIDAndTypeKey;
import curam.core.struct.AddressDtls;
import curam.core.struct.AddressKey;
import curam.core.struct.AddressMap;
import curam.core.struct.AddressMapList;
import curam.core.struct.AlternateNameDtls;
import curam.core.struct.AlternateNameKey;
import curam.core.struct.BankAccountDtls;
import curam.core.struct.BankAccountKey;
import curam.core.struct.BankBranchDtls;
import curam.core.struct.BankBranchKey;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.core.struct.CaseHeaderDtlsList;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.Count;
import curam.core.struct.DateStruct;
import curam.core.struct.ElementDetails;
import curam.core.struct.FinInstructionID;
import curam.core.struct.ILIFinInstructID;
import curam.core.struct.InstructionLineItemDtls;
import curam.core.struct.InstructionLineItemDtlsList;
import curam.core.struct.OtherAddressData;
import curam.core.struct.PIReconcilStatusCode;
import curam.core.struct.PaymentInstrumentDtls;
import curam.core.struct.PaymentInstrumentKey;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonKey;
import curam.core.struct.PmtInstrumentID;
import curam.core.struct.ProductDeliveryKey;
import curam.dynamicevidence.sl.entity.fact.DynamicEvidenceDataAttributeFactory;
import curam.dynamicevidence.sl.entity.intf.DynamicEvidenceDataAttribute;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataAttributeDtlsList;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataIDAndAttrKey;
import curam.pdc.impl.PDCConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.Trace;
import curam.util.type.CodeTable;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.Money;
import curam.util.type.NotFoundIndicator;
import curam.util.type.UniqueID;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;

/**
 * Batch to process payment instrument and populate staging table.
 */
public class BDMGeneratePaymentStagingDataBatchStream extends
  curam.ca.gc.bdm.batch.bdmgeneratepaymentstagingdatabatch.base.BDMGeneratePaymentStagingDataBatchStream {

  private final Address addressObj = AddressFactory.newInstance();

  private final BankAccount bankAccountObj = BankAccountFactory.newInstance();

  private final PaymentInstruction paymentInstructionObj =
    PaymentInstructionFactory.newInstance();

  private final InstructionLineItem iliObj =
    InstructionLineItemFactory.newInstance();

  private BDMGeneratePaymentStagingDataBatchStreamWrapper streamWrapper;

  private final BDMPaymentDetailRecordList paymentRecordListForChunk =
    new BDMPaymentDetailRecordList();

  protected int processedInstrumentsCount = 0;

  protected int skippedInstrumentsCount = 0;

  private final String recordIdentifier = "participantRoleID";

  private final String entityIdentifier = "CaseParticipantRole";

  HashMap<String, String> logErrorMap = new HashMap<String, String>();

  final BDMPaymentInstrument bdmPmtInstrObj =
    BDMPaymentInstrumentFactory.newInstance();

  final BDMParticipantRoleID bdmParticipantRoleID =
    new BDMParticipantRoleID();

  BDMPmtInstrumentDetailsList bdmPmtList = new BDMPmtInstrumentDetailsList();

  final PaymentInstrument pmtInstrObj =
    PaymentInstrumentFactory.newInstance();

  final PaymentInstrumentKey instrumentKey = new PaymentInstrumentKey();

  @Override
  public void process(final GeneratePaymentsFileKey batchProcessStreamKey)
    throws AppException, InformationalException {

    streamWrapper = new BDMGeneratePaymentStagingDataBatchStreamWrapper(this,
      batchProcessStreamKey);

    final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();

    if (StringUtils.isEmpty(batchProcessStreamKey.instanceID)) {
      batchProcessStreamKey.instanceID =
        BATCHPROCESSNAME.BDM_GENERATE_PAYMENT_STAGING_DATA_BATCH;
    }
    if (batchProcessStreamKey.processingDate.isZero()) {
      batchProcessStreamKey.processingDate = Date.getCurrentDate();
    }
    streamWrapper.setInstanceID(batchProcessStreamKey.instanceID);

    final BatchProcessStreamKey key = new BatchProcessStreamKey();
    key.instanceID = batchProcessStreamKey.instanceID;
    batchStreamHelper.runStream(key, streamWrapper);

  }

  /*
   * For each participantID it Search all paymentinstrument records and whose
   * Reconcile status is PROCESSED
   */

  @Override
  public BatchProcessingSkippedRecord processRecord(
    final BatchProcessingID batchProcessingID,
    final GeneratePaymentsFileKey key)
    throws AppException, InformationalException {

    final BatchProcessingSkippedRecord skippedRecord = null;

    final BDMPaymentDetailRecord paymentDetailRecord =
      new BDMPaymentDetailRecord();
    bdmParticipantRoleID.participantRoleID = batchProcessingID.recordID;

    bdmParticipantRoleID.reconcilStatusCode =
      PMTRECONCILIATIONSTATUS.PROCESSED;
    final curam.core.struct.ConcernRoleKey concernRoleKey =
      new ConcernRoleKey();
    concernRoleKey.concernRoleID = batchProcessingID.recordID;
    pmtInstrObj.searchByConcernRole(concernRoleKey);

    bdmParticipantRoleID.bdmReconcilStatusCode =
      BDMPMTRECONCILIATIONSTATUS.TRANSFERRED;

    bdmPmtList =
      bdmPmtInstrObj.searchPmtIDByParticipantID(bdmParticipantRoleID);

    double totalAmt = 0;
    boolean processUnderthreshold = false;

    /*
     * Reading the Payment instrument data for each participant and if amount is
     * UNDERTHRESHOLD status it means amount is less than $2 then it is not
     * qualified for processing. When total amount reaches equal and greater
     * than $2 then the rollup the payment with the regular payment into single
     * transaction with same spsPmtGroupID
     */
    for (final BDMPmtInstrumentDetails bdmPmt : bdmPmtList.dtls) {

      instrumentKey.pmtInstrumentID = bdmPmt.pmtInstrumentID;

      final PaymentInstrumentDtls instrDtls;

      instrDtls = readPaymentInstrument(instrumentKey);

      totalAmt = totalAmt + instrDtls.amount.getValue();
    }

    final String minIssueAmtStr =
      Configuration.getProperty(EnvVars.BDM_ENV_BDM_MINIMUM_ISSUE_AMOUNT);

    final Money minIssueAmt = new Money(minIssueAmtStr);

    final Money totalPmtAmt = new Money(totalAmt);

    processUnderthreshold = false;
    long latestInstrumentIDUTRollUp = 0;
    Date latestPaymentInstrumentDate = Date.kZeroDate;
    if (totalPmtAmt.getValue() >= minIssueAmt.getValue()) {
      processUnderthreshold = true;
      for (final BDMPmtInstrumentDetails bdmPmt : bdmPmtList.dtls) {

        instrumentKey.pmtInstrumentID = bdmPmt.pmtInstrumentID;

        final PaymentInstrumentDtls instrDtls;

        instrDtls = readPaymentInstrument(instrumentKey);

        if (bdmPmt.reconcilStatusCode
          .equals(BDMPMTRECONCILIATIONSTATUS.PAYMENT_DUE)) {
          if (instrDtls.creationDate.after(latestPaymentInstrumentDate)) {
            latestPaymentInstrumentDate = instrDtls.creationDate;
            latestInstrumentIDUTRollUp = instrDtls.pmtInstrumentID;
          }
        }
      }
      if (latestInstrumentIDUTRollUp == 0) {
        for (final BDMPmtInstrumentDetails bdmPmt : bdmPmtList.dtls) {

          instrumentKey.pmtInstrumentID = bdmPmt.pmtInstrumentID;

          final PaymentInstrumentDtls instrDtls;

          instrDtls = readPaymentInstrument(instrumentKey);

          if (bdmPmt.reconcilStatusCode
            .equals(BDMPMTRECONCILIATIONSTATUS.UNDERTHRESHOLD)) {
            if (instrDtls.creationDate.after(latestPaymentInstrumentDate)) {
              latestPaymentInstrumentDate = instrDtls.creationDate;
              latestInstrumentIDUTRollUp = instrDtls.pmtInstrumentID;
            }
          }
        }
      }
    }

    final ArrayList<BDMPmtInstrumentDetails> accWithUTPmts =
      new ArrayList<BDMPmtInstrumentDetails>();

    for (final BDMPmtInstrumentDetails bdmPmt : bdmPmtList.dtls) {

      instrumentKey.pmtInstrumentID = bdmPmt.pmtInstrumentID;
      final PaymentInstrumentDtls instrDtls =
        readPaymentInstrument(instrumentKey);

      paymentDetailRecord.paymentDueDate = instrDtls.effectiveDate;
      paymentDetailRecord.regionSequenceNumber = key.regionSequenceNumber;
      paymentDetailRecord.requisitionNumber =
        getReqNumber(key.regionSequenceNumber,
          paymentDetailRecord.paymentDueDate.toString());

      paymentDetailRecord.assign(instrDtls);
      paymentDetailRecord.pmtInstrumentID = bdmPmt.pmtInstrumentID;

      final BDMBatchFilename filenameObj =
        BDMBatchFilenameFactory.newInstance();
      final BDMBatchFilenameKeyStruct1 reqNumberKey =
        new BDMBatchFilenameKeyStruct1();
      reqNumberKey.requisitionNumber = paymentDetailRecord.requisitionNumber;
      reqNumberKey.instanceID = key.instanceID;
      if (filenameObj.searchByReqNumberAndInstanceId(reqNumberKey).dtls
        .isEmpty()) {
        final String fileName =
          key.filename + "_" + paymentDetailRecord.requisitionNumber;
        final BDMBatchUtil util = new BDMBatchUtil();
        final DateTime datetime = DateTime.getCurrentDateTime();
        util.insertFilename(datetime, fileName, key.instanceID, key.runID,
          paymentDetailRecord.requisitionNumber);
      }

      // paymentDetailRecord.assign(instrDtls);
      /*
       * If payment is zero then no need to write data to staging but need to
       * update reconcile status to ISSUED
       */
      if (bdmPmt.reconcilStatusCode.equals(BDMPMTRECONCILIATIONSTATUS.NIL)) {

        // Populate the FinancialCodes for SAP JV
        readAndCreateILIDataForJV(paymentDetailRecord, false, false, false);

        pmtInstrObj.modifyReconcilStatusCode(instrumentKey, getReconcilStatus(
          PMTRECONCILIATIONSTATUS.ISSUED, instrDtls.versionNo));
      }
      // UNDERTHRESHOLD amount is not qualified for processing
      else if (bdmPmt.reconcilStatusCode
        .equals(BDMPMTRECONCILIATIONSTATUS.UNDERTHRESHOLD)
        && !processUnderthreshold) {

        // Populate the FinancialCodes for SAP JV
        // check already in jv entry
        readAndCreateILIDataForJV(paymentDetailRecord, true, false, true);

      } else if (latestInstrumentIDUTRollUp == bdmPmt.pmtInstrumentID) {
        accWithUTPmts.add(0, bdmPmt);
        readAndCreateILIDataForJV(paymentDetailRecord, false, false, false);
      } else if (bdmPmt.reconcilStatusCode.equals(
        BDMPMTRECONCILIATIONSTATUS.UNDERTHRESHOLD) && processUnderthreshold) {
        // Populate the FinancialCodes for SAP JV

        readAndCreateILIDataForJV(paymentDetailRecord, false, true, false);

        accWithUTPmts.add(bdmPmt);

      } else {
        /*
         * If the Status is PAYMENT_DUE then
         * 1. Write the record into Staging table
         * 2. Update the Payment Instruction status to ISSUED
         */

        // Populate the FinancialCodes for SAP JV
        readAndCreateILIDataForJV(paymentDetailRecord, false, false, false);
        final ArrayList<BDMPmtInstrumentDetails> bdmPmts =
          new ArrayList<BDMPmtInstrumentDetails>();
        bdmPmts.add(bdmPmt);
        final BDMPaymentDetailRecord paymentDetailRecord_2 =
          constructPaymentDetailRecord(key, bdmPmts,
            batchProcessingID.recordID);

        paymentDetailRecord_2.requisitionNumber =
          paymentDetailRecord.requisitionNumber;

        paymentDetailRecord_2.regionSequenceNumber =
          paymentDetailRecord.regionSequenceNumber;

        writeRecordToStagingTable(paymentDetailRecord_2,
          BDMPMTSTAGINGDATATYPE.PAYMENT, batchProcessingID.recordID,
          key.runID);
      }
    }

    final BDMPaymentDetailRecord paymentDetailRecord_withUTs =
      constructPaymentDetailRecord(key, accWithUTPmts,
        batchProcessingID.recordID);

    // write to sps
    if (paymentDetailRecord_withUTs != null) {

      paymentDetailRecord_withUTs.requisitionNumber =
        paymentDetailRecord.requisitionNumber;

      paymentDetailRecord_withUTs.regionSequenceNumber =
        paymentDetailRecord.regionSequenceNumber;

      writeRecordToStagingTable(paymentDetailRecord_withUTs,
        BDMPMTSTAGINGDATATYPE.PAYMENT, batchProcessingID.recordID, key.runID);
    }

    paymentRecordListForChunk.dtls.add(paymentDetailRecord);
    processedInstrumentsCount++;
    return skippedRecord;
  }

  // Question : No need to run for all payment id
  private void updateDoJIndicator(final long pmtInstrumentID)
    throws AppException, InformationalException {

    final DOJILIsByInstrumentKey dojInstrKey = new DOJILIsByInstrumentKey();
    final BDMPmtInstrumentKey bdmPmtInstrKey = new BDMPmtInstrumentKey();
    final DOJProcessInd dojProcessInd = new DOJProcessInd();
    curam.core.struct.Count count = new Count();

    dojInstrKey.pmtInstrumentID = pmtInstrumentID;
    dojInstrKey.deductionTypeArrears =
      BDMDEDUCTIONTYPE.DOJ_FOE_WITHHOLD_ARREARS;
    dojInstrKey.deductionTypeFees = BDMDEDUCTIONTYPE.DOJ_FOE_WITHHOLD_FEES;
    dojInstrKey.iliCategory = ILICATEGORY.PAYMENTINSTRUCTION;

    count = bdmPmtInstrObj.countDOJILIsByInstrument(dojInstrKey);

    if (count.numberOfRecords > 0) {
      final BDMPaymentInstrumentKey bdmPaymentInstrumentKey =
        new BDMPaymentInstrumentKey();
      bdmPaymentInstrumentKey.pmtInstrumentID = pmtInstrumentID;
      final BDMPaymentInstrumentDtls bdmPaymentInstrumentDtls =
        bdmPmtInstrObj.read(bdmPaymentInstrumentKey);

      bdmPmtInstrKey.pmtInstrumentID = pmtInstrumentID;
      dojProcessInd.dojProcessPendingInd = true;
      dojProcessInd.versionNo = bdmPaymentInstrumentDtls.versionNo;

      bdmPmtInstrObj.modifyDoJProcessInd(bdmPmtInstrKey, dojProcessInd);

    }
  }

  // Read the PaymentInstrument table from instrument key
  private PaymentInstrumentDtls
    readPaymentInstrument(final PaymentInstrumentKey instrumentKey)
      throws AppException, InformationalException {

    final NotFoundIndicator nfIndicator = new NotFoundIndicator();

    final PaymentInstrumentDtls instrumentDtls =
      pmtInstrObj.read(nfIndicator, instrumentKey);

    if (!nfIndicator.isNotFound()) {
      return instrumentDtls;
    } else {
      return null;
    }
  }

  // Get the AlternateID of Nominee
  private String getPayeeAlternateID(final long pmtInstrID)
    throws AppException, InformationalException {

    final PaymentInstrumentKey instrumentKey = new PaymentInstrumentKey();
    final PmtInstrumentID pmtInstrumentID = new PmtInstrumentID();
    instrumentKey.pmtInstrumentID = pmtInstrID;
    final PaymentInstrumentDtls instrumentDtls =
      readPaymentInstrument(instrumentKey);

    pmtInstrumentID.pmtInstrumentID = pmtInstrID;
    final FinInstructionID paymentInstruction =
      paymentInstructionObj.readByPmtInstrumentID(pmtInstrumentID);

    final ILIFinInstructID finInstructionID = new ILIFinInstructID();
    finInstructionID.finInstructionID = paymentInstruction.finInstructionID;

    final CaseNomineeKey caseNomineeKey = new CaseNomineeKey();
    caseNomineeKey.caseNomineeID = instrumentDtls.caseNomineeID;

    // Below two lines(as before PWD code merge) have been added as a work
    // around to fix the PWD code merge.
    // Payments have been failing as the getConcernRoleFromCaseNominee BPO
    // fails to fetch a single record.

    String payeeAlternateID = instrumentDtls.nomineeAlternateID;

    //
    try {
      final CaseNomineeConcernRoleDtls caseNomineeConcernRoleDtls =
        BDMPaymentInstrumentDAFactory.newInstance()
          .getConcernRoleFromCaseNominee(caseNomineeKey);
      payeeAlternateID = caseNomineeConcernRoleDtls.primaryAlternateID;
    } catch (final Exception e) {
    }

    return payeeAlternateID;
  }

  public BDMPaymentDetailRecord processPayment(final long pmtInstrID,
    final long concernRoleID, final long spsPmtGroupID)
    throws AppException, InformationalException {

    // Read the PaymentInstrument details

    final PaymentInstrumentKey instrumentKey = new PaymentInstrumentKey();
    final PmtInstrumentID pmtInstrumentID = new PmtInstrumentID();
    instrumentKey.pmtInstrumentID = pmtInstrID;
    final PaymentInstrumentDtls instrumentDtls =
      readPaymentInstrument(instrumentKey);

    // Populate Payment Detail Record for staging data
    final BDMPaymentDetailRecord paymentDetailRecord =
      new BDMPaymentDetailRecord();

    paymentDetailRecord.spsPmtGroupID = spsPmtGroupID;

    pmtInstrumentID.pmtInstrumentID = pmtInstrID;

    final FinInstructionID paymentInstruction =
      paymentInstructionObj.readByPmtInstrumentID(pmtInstrumentID);

    final ILIFinInstructID finInstructionID = new ILIFinInstructID();
    finInstructionID.finInstructionID = paymentInstruction.finInstructionID;

    final InstructionLineItemDtlsList iliList =
      iliObj.searchByFinInstructID(finInstructionID);

    final long payeeConcernRoleID = concernRoleID;

    paymentDetailRecord.alternateID = getPayeeAlternateID(pmtInstrID);

    // clarify (max payment due data from ili)
    // paymentDetailRecord.paymentDueDate = instrumentDtls.effectiveDate;
    paymentDetailRecord.payRunDate = instrumentDtls.creationDate;

    paymentDetailRecord.pmtInstrumentID = instrumentDtls.pmtInstrumentID;
    paymentDetailRecord.reconcileStatusCode =
      instrumentDtls.reconcilStatusCode;
    paymentDetailRecord.creditDebitType = CREDITDEBIT.DEBIT;
    populatePaymentType(paymentDetailRecord, instrumentDtls,
      payeeConcernRoleID);

    if (paymentDetailRecord.paymentInstrumentType == BDMConstants.PAYMENT_TYPE_EFT) {
      populateBankDetails(paymentDetailRecord, instrumentDtls.bankAccountID);
    }

    populatePaymentInfo(paymentDetailRecord, payeeConcernRoleID);

    populateLanguageCode(paymentDetailRecord, payeeConcernRoleID);

    populatePayeeName(paymentDetailRecord, payeeConcernRoleID);

    populateAddress(paymentDetailRecord, instrumentDtls.addressID);

    // instrumentDtls it already have address id so no need extra code..
    // paymentinstrument contain ilis from multiple product

    // Its picking random product

    for (final InstructionLineItemDtls ili : iliList.dtls) {
      if (ili.instructLineItemCategory
        .equals(ILICATEGORY.PAYMENTINSTRUCTION)) {
        populateProductName(paymentDetailRecord, ili);
        break;
      }
    }

    return paymentDetailRecord;
  }

  // Populate the Financial Codes for SAP JV

  private void readAndCreateILIDataForJV(
    final BDMPaymentDetailRecord paymentDetailRecord, final boolean utInd,
    final boolean utToOTInd, final boolean underPaymentIND)

    throws AppException, InformationalException {

    final PaymentInstruction instructionObj =
      PaymentInstructionFactory.newInstance();
    final PmtInstrumentID pmtInstrumentID = new PmtInstrumentID();

    pmtInstrumentID.pmtInstrumentID = paymentDetailRecord.pmtInstrumentID;
    FinInstructionID finInstructionID = new FinInstructionID();

    finInstructionID = instructionObj.readByPmtInstrumentID(pmtInstrumentID);

    final ILIFinInstructID finInstructID = new ILIFinInstructID();
    finInstructID.finInstructionID = finInstructionID.finInstructionID;
    InstructionLineItemDtlsList iliList = new InstructionLineItemDtlsList();

    iliList = iliObj.searchByFinInstructID(finInstructID);

    final BDMFinancial bdmFinancial = new BDMFinancial();
    BDMILIFinancialCodingKey itemKey;
    ILIFinancialCodingDetails codingDetails;

    for (final InstructionLineItemDtls iliData : iliList.dtls) {
      itemKey = new BDMILIFinancialCodingKey();
      itemKey.instructLineItemID = iliData.instructLineItemID;
      itemKey.processingType = BDMConstants.kFinCodePayment;
      itemKey.addCashTypeInd = true;
      itemKey.utInd = utInd;
      itemKey.utToOTInd = utToOTInd;

      final BDMILIFinancialCodingDetailsList codeList =
        bdmFinancial.getCodesByILI(itemKey);

      for (final ILIFinancialCodingDetails codeDetails : codeList.dtls) {
        codingDetails = new ILIFinancialCodingDetails();

        codingDetails = codeDetails;

        writeDataToILI(codingDetails, iliData, paymentDetailRecord,
          underPaymentIND);
      }

    }
  }

  private void writeDataToILI(final ILIFinancialCodingDetails codingDetails,
    final InstructionLineItemDtls iliData,
    final BDMPaymentDetailRecord paymentDetailRecord,
    final boolean underPaymentIND)
    throws AppException, InformationalException {

    final BDMILIStageData stageDataObj = BDMILIStageDataFactory.newInstance();
    final BDMILIStageDataDtls bdmiliStageDataDtls = new BDMILIStageDataDtls();
    bdmiliStageDataDtls.pmtInstrumentID = paymentDetailRecord.pmtInstrumentID;
    bdmiliStageDataDtls.creationDate = Date.getCurrentDate();
    bdmiliStageDataDtls.instructionLineItemType =
      iliData.instructionLineItemType;
    bdmiliStageDataDtls.paymentAmount = iliData.amount;
    bdmiliStageDataDtls.instructLineItemID = iliData.instructLineItemID;
    bdmiliStageDataDtls.companyCode = codingDetails.jvCompanyCode;
    bdmiliStageDataDtls.costCentreCode = codingDetails.jvCostCentreCode;
    bdmiliStageDataDtls.functionalAreaCode =
      codingDetails.jvFunctionalAreaCode;
    bdmiliStageDataDtls.fundCode = codingDetails.jvFundCode;
    bdmiliStageDataDtls.gLCode = codingDetails.jvGLCode;
    bdmiliStageDataDtls.creditDebitType = codingDetails.creditDebitType;
    bdmiliStageDataDtls.regionTypeCd = codingDetails.regionTypeCd;
    bdmiliStageDataDtls.financialItemType = codingDetails.financialItemType;
    bdmiliStageDataDtls.financialCategoryType =
      codingDetails.financialCategoryType;
    bdmiliStageDataDtls.programCode = codingDetails.programType;
    bdmiliStageDataDtls.requisitionNumber =
      paymentDetailRecord.requisitionNumber;
    bdmiliStageDataDtls.regionSequenceNumber =
      paymentDetailRecord.regionSequenceNumber;
    bdmiliStageDataDtls.paymentDueDate = paymentDetailRecord.paymentDueDate;
    bdmiliStageDataDtls.underPaymentIND = underPaymentIND;
    bdmiliStageDataDtls.financialIND = true;
    bdmiliStageDataDtls.processingType = "PMT";
    bdmiliStageDataDtls.recordID =
      UniqueIDFactory.newInstance().getNextID().uniqueID;
    stageDataObj.insert(bdmiliStageDataDtls);
  }

  /**
   * Method to write payment information to staging table.
   *
   * @param BDMPaymentDetailRecord
   *
   * @throws AppException
   * @throws InformationalException
   */
  private void writeRecordToStagingTable(final BDMPaymentDetailRecord record,
    final String recordType, final long concernRoleID, final long runID)
    throws AppException, InformationalException {

    final BDMPaymentStagingDataDtls stagingDataDtls =
      new BDMPaymentStagingDataDtls();

    stagingDataDtls.recordID =
      UniqueIDFactory.newInstance().getNextID().uniqueID;
    stagingDataDtls.recordType = recordType;
    stagingDataDtls.processingDateTime = DateTime.getCurrentDateTime();
    stagingDataDtls.runID = runID;
    stagingDataDtls.concernRoleID = concernRoleID;
    stagingDataDtls.dueDateYearMonth =
      BDMDateUtil.dateFormatToYYMM(record.paymentDueDate.toString());
    stagingDataDtls.paymentAmount = record.paymentAmount;
    stagingDataDtls.paymentInfo = record.paymentInfo;
    stagingDataDtls.spsPmtGroupID = record.spsPmtGroupID;

    stagingDataDtls.regionSequenceNumber = record.regionSequenceNumber;
    stagingDataDtls.requisitionNumber = record.requisitionNumber;
    stagingDataDtls.cdoCode = BDMCDOCODE.BDMCDOCODE;
    stagingDataDtls.esdcCode = BDMESDCCODE.BDMESDCCODE;
    stagingDataDtls.regionCode = BDMREGIONCODE.REGIONCODE;
    stagingDataDtls.currencyCode = CURRENCY.DEFAULTCODE;// Needed for future
    // Always 0 for foundation
    stagingDataDtls.paymentPriorityIND = BDMConstants.PARTY_PAY_IND;
    stagingDataDtls.payRunDate = record.paymentDueDate;
    stagingDataDtls.expectedPayDate = record.paymentDueDate;

    stagingDataDtls.assign(record);
    BDMPaymentStagingDataFactory.newInstance().insert(stagingDataDtls);

  }

  private String getReqNumber(final int seqNo, final String dueDate) {

    final BDMPaymentUtil pmtUtilObj = new BDMPaymentUtil();
    final String reqno =
      pmtUtilObj.getRequisitionNumber(BDMESDCCODE.BDMESDCCODE,
        BDMCDOCODE.BDMCDOCODE, dueDate, BDMREGIONCODE.REGIONCODE, seqNo);
    return reqno;
  }

  /**
   * TODO: Modify this method to handle new address format
   *
   * Method to get the Address details for the input concernrole ID. Based on
   * primaryAddressID of the input concernrole, address details are fetched and
   * populated into deliveryAddress and postalcode values.
   *
   * @param BDMPaymentDetailRecord
   * @param concernRoleID
   *
   * @throws AppException
   * @throws InformationalException
   *
   */
  private void populateAddress(final BDMPaymentDetailRecord detailRecord,
    final long addressID) throws AppException, InformationalException {

    final AddressKey addressKey = new AddressKey();
    addressKey.addressID = addressID;
    // For Cheque payment, read address from case nominee destination
    // if (detailRecord.paymentInstrumentType ==
    // BDMConstants.PAYMENT_TYPE_CHEQUE) {
    final AddressDtls addressDtls = addressObj.read(addressKey);

    final OtherAddressData otherAddressData = new OtherAddressData();
    otherAddressData.addressData = addressDtls.addressData;

    // NL - Refactor deliveryAddress into the constituent address elements
    // as separate columns instead of concatenating each into deliveryAddress

    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    final AddressMapList addressMapList =
      addressDataObj.parseDataToMap(otherAddressData);

    final AddressMap addressMap = new AddressMap();
    ElementDetails elementDetails;
    final StringBuffer addressBuffer = new StringBuffer();

    addressMap.name = ADDRESSELEMENTTYPE.APT;
    elementDetails = addressDataObj.findElement(addressMapList, addressMap);
    detailRecord.deliveryAddressAPT = elementDetails.elementValue;
    if (!detailRecord.deliveryAddressAPT.equals("")) {
      addressBuffer.append(detailRecord.deliveryAddressAPT + " ");
    }

    addressMap.name = ADDRESSELEMENTTYPE.STREET_NUMBER;
    elementDetails = addressDataObj.findElement(addressMapList, addressMap);
    detailRecord.deliveryAddressStreetNumber = elementDetails.elementValue;
    if (!detailRecord.deliveryAddressStreetNumber.equals("")) {
      addressBuffer.append(detailRecord.deliveryAddressStreetNumber + " ");
    }

    addressMap.name = ADDRESSELEMENTTYPE.LINE1;
    elementDetails = addressDataObj.findElement(addressMapList, addressMap);
    detailRecord.deliveryAddressStreetNumber = elementDetails.elementValue;
    if (!detailRecord.deliveryAddressStreetNumber.equals("")) {
      addressBuffer.append(detailRecord.deliveryAddressStreetNumber + " ");
    }

    addressMap.name = ADDRESSELEMENTTYPE.LINE2;
    elementDetails = addressDataObj.findElement(addressMapList, addressMap);
    detailRecord.deliveryAddressStreetName = elementDetails.elementValue;
    if (!detailRecord.deliveryAddressStreetName.equals("")) {
      addressBuffer.append(detailRecord.deliveryAddressStreetName + " ");
    }

    addressMap.name = ADDRESSELEMENTTYPE.CITY;
    elementDetails = addressDataObj.findElement(addressMapList, addressMap);
    detailRecord.deliveryAddressCity = elementDetails.elementValue;
    if (!detailRecord.deliveryAddressCity.equals("")) {
      addressBuffer.append("\n" + detailRecord.deliveryAddressCity + " ");
    }

    addressMap.name = ADDRESSELEMENTTYPE.PROVINCE;
    elementDetails = addressDataObj.findElement(addressMapList, addressMap);
    detailRecord.deliveryAddressProvince = elementDetails.elementValue;
    if (!detailRecord.deliveryAddressProvince.equals("")) {
      addressBuffer.append("\n" + detailRecord.deliveryAddressProvince);
    }

    addressMap.name = ADDRESSELEMENTTYPE.STATEPROV;
    elementDetails = addressDataObj.findElement(addressMapList, addressMap);
    detailRecord.deliveryAddressStateProv = elementDetails.elementValue;
    if (!detailRecord.deliveryAddressStateProv.equals("")) {
      addressBuffer.append("\n" + detailRecord.deliveryAddressStateProv);
    }

    addressMap.name = ADDRESSELEMENTTYPE.POBOXNO;
    elementDetails = addressDataObj.findElement(addressMapList, addressMap);
    if (!detailRecord.deliveryAddressPOBOXNO.equals("")) {
      addressBuffer.append(detailRecord.deliveryAddressPOBOXNO + " ");
    }
    detailRecord.deliveryAddressPOBOXNO = elementDetails.elementValue;

    addressMap.name = ADDRESSELEMENTTYPE.POSTCODE;
    elementDetails = addressDataObj.findElement(addressMapList, addressMap);
    detailRecord.deliveryAddressPostcode = elementDetails.elementValue;
    if (!detailRecord.deliveryAddressPostcode.equals("")) {
      // remove final spaces
      detailRecord.postalCode =
        detailRecord.deliveryAddressPostcode.replaceAll("\\s+", "");
    }

    addressMap.name = ADDRESSELEMENTTYPE.COUNTRY;
    elementDetails = addressDataObj.findElement(addressMapList, addressMap);
    detailRecord.deliveryAddressCountry = elementDetails.elementValue;

    if (!detailRecord.deliveryAddressCountry.equals("")) {
      addressBuffer.append("\n" + detailRecord.deliveryAddressCountry);
    }
    // concat the final address
    detailRecord.deliveryAddress = addressBuffer.toString();

  }

  /**
   * Method to populate the bank country code in detailRecord.
   * Based on the input bank account details, corresponding
   * bank branch's address details are fetched and populated
   * into country code.
   *
   * @param BDMPaymentDetailRecord
   * @param BankAccountKey
   *
   * @throws AppException
   * @throws InformationalException
   *
   */
  private void populateBankCountryCode(
    final BDMPaymentDetailRecord detailRecord,
    final BankAccountKey bankAccountKey)
    throws AppException, InformationalException {

    final BankAccountDtls bankDtls = bankAccountObj.read(bankAccountKey);

    final BankBranch bankBranchObj = BankBranchFactory.newInstance();
    final BankBranchKey branchKey = new BankBranchKey();
    branchKey.bankBranchID = bankDtls.bankBranchID;

    final BankBranchDtls branchDtls = bankBranchObj.read(branchKey);

    final AddressKey addressKey = new AddressKey();
    addressKey.addressID = branchDtls.addressID;

    final AddressDtls addressDtls = addressObj.read(addressKey);
    detailRecord.countryCode = addressDtls.countryCode;
  }

  /**
   * Method to populate the bank details in detailRecord.
   * Based on the input bank account id, corresponding
   * bank branch, payee account number and financial institution number is
   * fetched and populated in detailRecord.
   *
   * @param BDMPaymentDetailRecord
   * @param bankAccountID
   *
   * @throws AppException
   * @throws InformationalException
   */
  private void populateBankDetails(final BDMPaymentDetailRecord detailRecord,
    final long bankAccountID) throws AppException, InformationalException {

    final BankAccountKey bankAccountKey = new BankAccountKey();
    bankAccountKey.bankAccountID = bankAccountID;
    final BankAccountDtls bankDtls = bankAccountObj.read(bankAccountKey);

    detailRecord.financialInstitutionNum =
      StringUtils.substring(bankDtls.bankSortCode, 0, 3);
    detailRecord.bankBranchNum =
      StringUtils.substring(bankDtls.bankSortCode, 3, 8);
    detailRecord.payeeAccountNum = bankDtls.accountNumber;
  }

  /**
   * TODO: check if payee name needs to be formatted with the white space
   *
   * Method to populate the payee name in detail record.
   * Based on the input payee concernrole id, payee name is fetched and
   * populated in detailRecord.
   *
   * @param BDMPaymentDetailRecord
   * @param concernRoleID
   *
   * @throws AppException
   * @throws InformationalException
   */
  private void populatePayeeName(final BDMPaymentDetailRecord detailRecord,
    final long concernRoleID) throws AppException, InformationalException {

    final Person personObj = PersonFactory.newInstance();
    final PersonKey personKey = new PersonKey();
    personKey.concernRoleID = concernRoleID;
    final PersonDtls personDtls = personObj.read(personKey);

    final AlternateName alternateNameObj = AlternateNameFactory.newInstance();

    final AlternateNameKey nameKey = new AlternateNameKey();
    nameKey.alternateNameID = personDtls.primaryAlternateNameID;
    final AlternateNameDtls nameDtls = alternateNameObj.read(nameKey);

    // NL - Refactor payeeName into the constituent names as separate columns
    // instead of concatenating each into payeeName

    final StringBuffer payeeNameBuffer = new StringBuffer();

    String title = "";
    if (detailRecord.languageCode == BDMConstants.LANGUAGE_CODE_FRENCH) {
      title = CodeTable.getOneItem(PERSONTITLE.TABLENAME, nameDtls.title,
        LOCALE.FRENCH);

    } else {
      title = CodeTable.getOneItem(PERSONTITLE.TABLENAME, nameDtls.title,
        LOCALE.ENGLISH);
    }

    payeeNameBuffer.append(title);
    payeeNameBuffer.append(" ");
    payeeNameBuffer.append(nameDtls.firstForename);
    payeeNameBuffer.append(" ");
    payeeNameBuffer.append(nameDtls.otherForename);
    payeeNameBuffer.append(" ");
    payeeNameBuffer.append(nameDtls.surname);

    detailRecord.payeeTitle = title;
    detailRecord.payeeFirstForename = nameDtls.firstForename;
    detailRecord.payeeOtherForename = nameDtls.otherForename;
    detailRecord.payeeSurname = nameDtls.surname;
    detailRecord.payeeName = payeeNameBuffer.toString();

  }

  /**
   * Method to populate the language code in detail record.
   * Based on the input concern role id, preferred language of the concern role
   * is fetched and populated in detailRecord.
   *
   * @param BDMPaymentDetailRecord
   * @param concernRoleID
   *
   * @throws AppException
   * @throws InformationalException
   */
  private void populateLanguageCode(final BDMPaymentDetailRecord detailRecord,
    final long concernRoleID) throws AppException, InformationalException {

    // 1. Get the caseID of the PDC case for this concernRoleID
    final ActiveCasesConcernRoleIDAndTypeKey caseKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseKey.concernRoleID = concernRoleID;
    caseKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;

    final CaseHeaderDtlsList pdcCases =
      CaseHeaderFactory.newInstance().searchByConcernRoleIDType(caseKey);
    if (pdcCases.dtls.isEmpty()) {
      detailRecord.languageCode = BDMConstants.LANGUAGE_CODE_ENGLISH;
      return;
    }
    final long pdcCaseID = pdcCases.dtls.get(0).caseID;

    // 2. Find the relatedID in the EvidenceDescriptor for the caseID and
    // evidence type
    final CaseIDEvidenceTypeStatusesKey evidenceKey =
      new CaseIDEvidenceTypeStatusesKey();
    evidenceKey.caseID = pdcCaseID;
    evidenceKey.evidenceType = PDCConst.PDCCONTACTPREFERENCES;
    evidenceKey.statusCode1 = EVIDENCEDESCRIPTORSTATUS.ACTIVE;
    evidenceKey.statusCode2 = EVIDENCEDESCRIPTORSTATUS.ACTIVE;

    final EvidenceDescriptor edObj = EvidenceDescriptorFactory.newInstance();

    final DynamicEvidenceDataAttribute deObj =
      DynamicEvidenceDataAttributeFactory.newInstance();

    final EvidenceDescriptorKeyList contactPrefEvidences =
      edObj.searchActiveInEditByCaseIDAndType(evidenceKey);
    if (contactPrefEvidences.dtls.isEmpty()) {
      detailRecord.languageCode = BDMConstants.LANGUAGE_CODE_ENGLISH;
      return;
    }

    final EvidenceDescriptorKey edKey = new EvidenceDescriptorKey();
    edKey.evidenceDescriptorID =
      contactPrefEvidences.dtls.get(0).evidenceDescriptorID;
    final EvidenceDescriptorDtls edDtls = edObj.read(edKey);
    final long contactPrefRelatedID = edDtls.relatedID;

    // 3. Read DynamicEvidenceDataAttribute where evidenceID=relatedID and
    // name='preferredWrittenLanguage'
    final DynamicEvidenceDataIDAndAttrKey deKey =
      new DynamicEvidenceDataIDAndAttrKey();
    deKey.evidenceID = contactPrefRelatedID;
    deKey.name = BDMConstants.PREFERRED_WRITTEN_LANGUAGE;
    final DynamicEvidenceDataAttributeDtlsList deAttributes =
      deObj.searchByEvidenceIDAndAttribute(deKey);
    if (deAttributes.dtls.isEmpty()) {
      detailRecord.languageCode = BDMConstants.LANGUAGE_CODE_ENGLISH;
      return;
    }
    final String preferredWrittenLanguage = deAttributes.dtls.get(0).value;

    // Set this to '2' for French if French is specified as preferred,
    // otherwise default to English.
    if (LANGUAGE.FRENCH.equals(preferredWrittenLanguage)) {
      detailRecord.languageCode = BDMConstants.LANGUAGE_CODE_FRENCH;
    } else {
      detailRecord.languageCode = BDMConstants.LANGUAGE_CODE_ENGLISH;
    }
  }

  /**
   * Method to populate the payment type in detail record.
   * Based on the input payment instrument, payment type information is fetched
   * and populated in detailRecord.
   *
   * @param BDMPaymentDetailRecord
   * @param PaymentInstrumentDtls
   *
   * @throws AppException
   * @throws InformationalException
   */
  private void populatePaymentType(final BDMPaymentDetailRecord detailRecord,
    final PaymentInstrumentDtls instrumentDtls, final long concernRoleID)
    throws AppException, InformationalException {

    if (METHODOFDELIVERY.CHEQUE.equals(instrumentDtls.deliveryMethodType)) {
      detailRecord.paymentInstrumentType = BDMConstants.PAYMENT_TYPE_CHEQUE;

      final AddressKey addressKey = new AddressKey();
      addressKey.addressID = instrumentDtls.addressID;
      // populateAddressCountryCode(detailRecord, concernRoleID);

      // This is for foundation
      detailRecord.countryCode = COUNTRY.DEFAULTCODE;

    } else if (METHODOFDELIVERY.EFT
      .equals(instrumentDtls.deliveryMethodType)) {
      detailRecord.paymentInstrumentType = BDMConstants.PAYMENT_TYPE_EFT;

      final BankAccountKey accountKey = new BankAccountKey();
      accountKey.bankAccountID = instrumentDtls.bankAccountID;
      populateBankCountryCode(detailRecord, accountKey);
    }
  }

  /**
   * Append concernRoleID and SPSPaymentGroupID to track Under-threshold
   * amount
   * Method to populate the payment information in detail record.
   * Based on the input payment instrument, payment information is fetched
   * and populated in detailRecord.
   *
   * @param BDMPaymentDetailRecord
   * @param PaymentInstrumentDtls
   *
   * @throws AppException
   * @throws InformationalException
   */
  private void populatePaymentInfo(final BDMPaymentDetailRecord detailRecord,
    final long concernRoleID) throws AppException, InformationalException {

    detailRecord.paymentInfo =
      String.format("%20s|%s", concernRoleID, detailRecord.spsPmtGroupID);

    // System.out.println("paymentInfo2>>"+String.format("%20s|%-20s",
    // concernRoleID, detailRecord.spsPmtGroupID));

    /*
     * detailRecord.paymentInfo = concernRoleID + "|" +
     * detailRecord.spsPmtGroupID;
     */

  }

  /**
   * Method to populate produce name from instruction line item in detail
   * record.
   * If the ILI caseID is a Payment Correction case, read the
   * PaymentCorrectionEvidence to retrieve the caseID of the related benefit
   * case.
   *
   * @param BDMPaymentDetailRecord
   * @param InstructionLineItemDtls
   *
   * @throws AppException
   * @throws InformationalException
   */
  private void populateProductName(final BDMPaymentDetailRecord detailRecord,
    final InstructionLineItemDtls ili)
    throws AppException, InformationalException {

    // ili can come from multiple product
    // rollup from multiple product

    // every product will have 1 paymentgroup
    // we cannot get instruction level
    // may need break dowm all product code..concatenate
    if (ili.caseID != 0) {

      final ProductDeliveryKey productDeliveryKey = new ProductDeliveryKey();
      productDeliveryKey.caseID = ili.caseID;

      final ProductDelivery productDeliveryObj =
        ProductDeliveryFactory.newInstance();

      String productName =
        productDeliveryObj.readProductName(productDeliveryKey).name;

      final long productID =
        productDeliveryObj.readProductID(productDeliveryKey).productID;

      final BDMProduct bdmProduct = BDMProductFactory.newInstance();
      final BDMProductKey bdmProductKey = new BDMProductKey();
      bdmProductKey.productID = productID;
      final String productcode =
        bdmProduct.read(bdmProductKey).spsProductCode;

      if (PRODUCTNAME.PAYMENTCORRECTION.equals(productName)) {

        final CaseHeaderKey correctionCaseKey = new CaseHeaderKey();
        correctionCaseKey.caseID = ili.caseID;
        final PaymentCorrectionEvidenceDtlsList correctionEvidences =
          PaymentCorrectionEvidenceFactory.newInstance()
            .searchByCaseID(correctionCaseKey);

        for (final PaymentCorrectionEvidenceDtls correctionEvidence : correctionEvidences.dtls) {
          productDeliveryKey.caseID = correctionEvidence.relatedCaseID;
          break;
        }

        productName =
          productDeliveryObj.readProductName(productDeliveryKey).name;
      }

      detailRecord.productName = productName;
      detailRecord.productCode = productcode;

    }
  }

  @Override
  public String getChunkResult(final int skippedCasesCount)
    throws AppException, InformationalException {

    final String chunkResult =
      processedInstrumentsCount + CuramConst.gkTabDelimiter
        + (skippedCasesCount + skippedInstrumentsCount);
    processedInstrumentsCount = 0;
    skippedInstrumentsCount = 0;
    return chunkResult;

  }

  @Override
  public void processSkippedCases(
    final BatchProcessingSkippedRecordList batchProcessingSkippedRecordList)
    throws AppException, InformationalException {

    for (final BatchProcessingSkippedRecord batchProcessingSkippedRecord : batchProcessingSkippedRecordList.dtls) {

      logErrorMap.put(BDMConstants.kRecordIdentifier, recordIdentifier);

      logErrorMap.put(BDMConstants.kEntityIdentifier, entityIdentifier);

      logErrorMap.put(BDMConstants.kRecordID,
        String.valueOf(batchProcessingSkippedRecord.recordID));

      logErrorMap.put(BDMConstants.kInstanceID,
        CodeTable.getOneItem(BATCHPROCESSNAME.TABLENAME,
          BATCHPROCESSNAME.BDM_GENERATE_PAYMENT_STAGING_DATA_BATCH));

      logErrorMap.put(BDMConstants.kProcessingStatus, CodeTable.getOneItem(
        BATCHPROCESSNAME.TABLENAME, BATCHPROCESSRESULTSTATUS.FAILED));

      logErrorMap.put(BDMConstants.kErrorMessage,
        batchProcessingSkippedRecord.errorMessage);

    }

    // Iterating log through forEach
    logErrorMap.forEach(
      (key, value) -> Trace.kTopLevelLogger.error(key + " = " + value));

    /*
     * Output example
     *
     * EntityIdentifier = CaseParticipantRole
     * RecordIdentifier = participantRoleID
     * RecordID = 1090434059777081344
     * InstanceID = BDM Generate Payment Staging Data Batch
     * ProcessingStatus = Failed
     * ErrorMessage = Record Not found
     */

  }

  public void setWrapper(
    final BDMGeneratePaymentStagingDataBatchStreamWrapper wrapper) {

    this.streamWrapper = wrapper;
  }

  public BDMPaymentDetailRecordList getPaymentRecordListForChunk() {

    return paymentRecordListForChunk;
  }

  /**
   * Takes status & version no, returns PIReconcilStatusCode object
   *
   * @param status
   * @param versionNo
   * @return
   */
  private PIReconcilStatusCode getReconcilStatus(final String status,
    final int versionNo) {

    final PIReconcilStatusCode statusCode = new PIReconcilStatusCode();
    statusCode.reconcilStatusCode = status;
    statusCode.versionNo = versionNo;
    return statusCode;
  }

  protected BDMPaymentDetailRecord constructPaymentDetailRecord(
    final GeneratePaymentsFileKey key,
    final ArrayList<BDMPmtInstrumentDetails> pmtList,
    final long concernRoleID) throws AppException, InformationalException {

    BDMPaymentDetailRecord paymentDetailRecord = null;
    final BDMReadPaymentDueDateKey bdmPaymentDueDateKey =
      new BDMReadPaymentDueDateKey();
    final BDMPaymentInstrumentKey bdmPaymentKey =
      new BDMPaymentInstrumentKey();
    final BDMPaymentInstrumentDA bdmPaymentInstrumentDAObj =
      BDMPaymentInstrumentDAFactory.newInstance();
    final BDMCountILIsKey countILIKey = new BDMCountILIsKey();
    final BDMPaymentInstrumentKey bdmPIKey = new BDMPaymentInstrumentKey();

    for (final BDMPmtInstrumentDetails bdmPmt : pmtList) {

      updateDoJIndicator(bdmPmt.pmtInstrumentID);
      bdmPIKey.pmtInstrumentID = bdmPmt.pmtInstrumentID;
      final BDMPaymentInstrumentDtls bdmPIDtls =
        bdmPmtInstrObj.read(bdmPIKey);

      // read the payment due date for the payment instrument
      bdmPaymentDueDateKey.pmtInstrumentID = bdmPmt.pmtInstrumentID;
      bdmPaymentDueDateKey.iliCategory = ILICATEGORY.PAYMENTINSTRUCTION;

      final DateStruct readPaymentDueDate =
        bdmPaymentInstrumentDAObj.readPaymentDueDate(bdmPaymentDueDateKey);

      // set the due date on the BDMPaymentInstrument entity
      bdmPaymentKey.pmtInstrumentID = bdmPmt.pmtInstrumentID;
      bdmPIDtls.paymentDueDate = readPaymentDueDate.date;

      // TODO: test this!
      if (bdmPIDtls.paymentDueDate.before(key.processingDate)) {
        bdmPIDtls.paymentDueDate = key.processingDate;
      }

      bdmPIDtls.addToTaxSlipInd = false;

      // check if there are ILIs that are eligible for tax reporting
      if (bdmPIDtls.taxSlipGenInd == false) {
        countILIKey.iliCategory = ILICATEGORY.PAYMENTINSTRUCTION;
        countILIKey.pmtInstrumentID = bdmPmt.pmtInstrumentID;
        countILIKey.taxReportInd = true;
        final RecordCount iliCount =
          bdmPaymentInstrumentDAObj.countILIsForTaxReporting(countILIKey);

        if (iliCount.count > 0) {
          bdmPIDtls.addToTaxSlipInd = true;
        }

      }

      instrumentKey.pmtInstrumentID = bdmPmt.pmtInstrumentID;
      final PaymentInstrumentDtls instrDtls =
        readPaymentInstrument(instrumentKey);

      if (paymentDetailRecord == null) {
        final long spsPmtGroupID = UniqueID.nextUniqueID();
        paymentDetailRecord = processPayment(bdmPmt.pmtInstrumentID,
          concernRoleID, spsPmtGroupID);
        paymentDetailRecord.spsPmtGroupID = spsPmtGroupID;
      }
      if (paymentDetailRecord.paymentDueDate
        .before(bdmPIDtls.paymentDueDate)) {
        paymentDetailRecord.paymentDueDate = bdmPIDtls.paymentDueDate;
      }

      paymentDetailRecord.paymentAmount =
        new Money(paymentDetailRecord.paymentAmount.getValue()
          + instrDtls.amount.getValue());

      bdmPIDtls.spsPmtGroupID = paymentDetailRecord.spsPmtGroupID;
      bdmPIDtls.reconcilStatusCode = BDMPMTRECONCILIATIONSTATUS.TRANSFERRED;
      bdmPmtInstrObj.modify(bdmPIKey, bdmPIDtls);
    }
    return paymentDetailRecord;
  }
}
