package curam.ca.gc.bdm.batch.bdmdojoutboundloaddata.impl;

import curam.ca.gc.bdm.batch.bdmdojoutboundloaddata.struct.BDMDoJOutboundLoadDataBatchKey;
import curam.ca.gc.bdm.codetable.BDMDEDUCTIONTYPE;
import curam.ca.gc.bdm.codetable.BDMDOJRECORDSTATUS;
import curam.ca.gc.bdm.codetable.BDMEXTERNALLIABILITYTYPE;
import curam.ca.gc.bdm.entity.bdmexternalliability.fact.BDMExternalLiabilityFactory;
import curam.ca.gc.bdm.entity.bdmexternalliability.intf.BDMExternalLiability;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityDtls;
import curam.ca.gc.bdm.entity.fact.BDMDoJOutboundStageFactory;
import curam.ca.gc.bdm.entity.financial.fact.BDMExternalLiabilityDOJDataFactory;
import curam.ca.gc.bdm.entity.financial.fact.BDMPaymentInstrumentFactory;
import curam.ca.gc.bdm.entity.financial.intf.BDMExternalLiabilityDOJData;
import curam.ca.gc.bdm.entity.financial.intf.BDMPaymentInstrument;
import curam.ca.gc.bdm.entity.financial.struct.BDMExternalLiabilityDOJDataDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMExternalLiabilityDOJDataKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMExternalLiabilityKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMExternalLiabilityKeyList;
import curam.ca.gc.bdm.entity.financial.struct.DOJOutboundCrossDebtDetails;
import curam.ca.gc.bdm.entity.financial.struct.DOJOutboundCrossDebtKey;
import curam.ca.gc.bdm.entity.financial.struct.DOJOutboundDebtILIsDetails;
import curam.ca.gc.bdm.entity.financial.struct.DOJOutboundDebtILIsDetailsList;
import curam.ca.gc.bdm.entity.financial.struct.DOJOutboundDebtILIsKey;
import curam.ca.gc.bdm.entity.financial.struct.DOJOutboundDebtsByConcernRoleKey;
import curam.ca.gc.bdm.entity.intf.BDMDoJOutboundStage;
import curam.ca.gc.bdm.entity.struct.BDMDoJInboundStagingKeyStruct1;
import curam.ca.gc.bdm.entity.struct.BDMDoJOutboundStageDtls;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.sl.financial.struct.BDMCheckDOJLiabilityExistDetails;
import curam.ca.gc.bdm.util.impl.BDMDateUtil;
import curam.ca.gc.bdm.util.payment.impl.BDMPaymentUtil;
import curam.codetable.BATCHPROCESSNAME;
import curam.codetable.ILICATEGORY;
import curam.codetable.ILITYPE;
import curam.core.facade.fact.ConcernRoleFactory;
import curam.core.facade.intf.ConcernRole;
import curam.core.fact.AddressElementFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.impl.BatchStreamHelper;
import curam.core.impl.CuramConst;
import curam.core.intf.AddressElement;
import curam.core.struct.AddressElementDtls;
import curam.core.struct.AddressElementDtlsList;
import curam.core.struct.AddressKey;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.RecordNotFoundException;
import curam.util.resources.Trace;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.Money;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.apache.commons.lang.StringUtils;

public class BDMDoJOutboundLoadDataBatchStream extends
  curam.ca.gc.bdm.batch.bdmdojoutboundloaddata.base.BDMDoJOutboundLoadDataBatchStream {

  protected int processedInstrumentsCount = 0;

  protected int skippedInstrumentsCount = 0;

  private BDMDoJOutboundLoadDataBatchStreamWrapper streamWrapper;

  @Override
  public void process(final BatchProcessStreamKey streamKey)
    throws AppException, InformationalException {

    if (StringUtils.isEmpty(streamKey.instanceID)) {
      streamKey.instanceID = BATCHPROCESSNAME.BDM_DOJ_OUTBOUND_DATA_LOAD;
    }
    streamWrapper.setInstanceID(streamKey.instanceID);

    final BDMDoJOutboundLoadDataBatchKey key =
      new BDMDoJOutboundLoadDataBatchKey();
    key.instanceID = streamKey.instanceID;

    streamWrapper = new BDMDoJOutboundLoadDataBatchStreamWrapper(this, key);

    final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();

    batchStreamHelper.runStream(streamKey, streamWrapper);

  }

  @Override
  public BatchProcessingSkippedRecord processRecord(
    final BatchProcessingID batchProcessingID,
    final BDMDoJOutboundLoadDataBatchKey key)
    throws AppException, InformationalException {

    final BDMExternalLiabilityDOJData dojData =
      BDMExternalLiabilityDOJDataFactory.newInstance();
    final BDMExternalLiability bdmExtLbyObj =
      BDMExternalLiabilityFactory.newInstance();

    BDMDoJOutboundStageDtls perDebtData = null;

    DOJOutboundDebtILIsDetailsList iliList =
      new DOJOutboundDebtILIsDetailsList();

    BDMExternalLiabilityDtls lbyFeeDtls;

    final ArrayList<String> suffixList = new ArrayList<String>();
    final BDMExternalLiabilityDOJDataKey dojDataKey =
      new BDMExternalLiabilityDOJDataKey();
    BDMExternalLiabilityDOJDataDtls lbyDOJDtls =
      new BDMExternalLiabilityDOJDataDtls();

    final curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityKey bdmExtLbyKey =
      new curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityKey();

    BDMExternalLiabilityDtls lbyDtls = new BDMExternalLiabilityDtls();

    curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityKey feeLbyID =
      new curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityKey();

    final ArrayList<BDMDoJOutboundStageDtls> debtsPerPerson =
      new ArrayList<BDMDoJOutboundStageDtls>();
    perDebtData = new BDMDoJOutboundStageDtls();

    // Reading DOJOutbound Cross Debt Data
    DOJOutboundCrossDebtDetails crossDebtDetails =
      new DOJOutboundCrossDebtDetails();
    crossDebtDetails = readCrossDebtDetails(batchProcessingID.recordID);

    final String provCode = getProvincecode(batchProcessingID.recordID);

    final Money obligationFundsAvailable =
      new Money(crossDebtDetails.totalPaymentAmount.getValue()
        - crossDebtDetails.totalPreDOJDedAmount.getValue());

    final Money obligationDivertAmount =
      crossDebtDetails.totalDOJArrearsAmount;

    final Money obligationPaidAmount =
      new Money(crossDebtDetails.totalPaymentAmount.getValue()
        - crossDebtDetails.totalPreDOJDedAmount.getValue()
        - crossDebtDetails.totalDOJDedAmount.getValue());

    // Search DoJ debts by concernRoleID
    BDMExternalLiabilityKeyList bdmExtlLbyKeyList =
      new BDMExternalLiabilityKeyList();
    bdmExtlLbyKeyList =
      searchDoJDebtByConcernRole(batchProcessingID.recordID);

    // Adding Person's suffix id to the arraylist
    for (final BDMExternalLiabilityKey bdmExtLby : bdmExtlLbyKeyList.dtls) {
      dojDataKey.externalLiabilityID = bdmExtLby.externalLiabilityID;
      lbyDOJDtls = dojData.read(dojDataKey);
      suffixList.add(lbyDOJDtls.obligationIDSuffix);
    }

    /*
     * Sort the Person's suffix list in order to assign the sequence number
     * based on alphabetic order
     */
    Collections.sort(suffixList);
    final Object[] suffixObj = suffixList.toArray();
    final String[] sortedSuffix =
      Arrays.copyOf(suffixObj, suffixObj.length, String[].class);

    boolean nonFeeLbyInd;

    for (final BDMExternalLiabilityKey bdmExtLby : bdmExtlLbyKeyList.dtls) {
      bdmExtLbyKey.externalLiabilityID = bdmExtLby.externalLiabilityID;
      lbyDtls = bdmExtLbyObj.read(bdmExtLbyKey);
      nonFeeLbyInd = true;
      // Lets first process non-fee liabilities.
      if (lbyDtls.liabilityTypeCode
        .equals(BDMEXTERNALLIABILITYTYPE.DOJ_FEES)) {
        continue;
      }

      final long concernRoleID = lbyDtls.concernRoleID;

      // calling getLiabilityByExtRefNumber from MaintainDOJLiability class
      feeLbyID = readLbyByExtRefNumber(lbyDtls, lbyDOJDtls);

      bdmExtLbyKey.externalLiabilityID = feeLbyID.externalLiabilityID;

      perDebtData = new BDMDoJOutboundStageDtls();

      try {

        lbyFeeDtls = bdmExtLbyObj.read(bdmExtLbyKey);
        perDebtData.obligationOutstandingFeeBal =
          lbyFeeDtls.outstandingAmount;
      } catch (final RecordNotFoundException e) {

      }

      perDebtData.obligationArrearsBalance = lbyDtls.outstandingAmount;
      perDebtData.obligationFixedAmount = lbyDOJDtls.deductionMinPayAmount;
      perDebtData.obligationDebtPercentage = (byte) lbyDOJDtls.deductionRate;
      perDebtData.obligationFixedAmountIND = lbyDOJDtls.deductionFirstPayInd;
      perDebtData.obligationPaymentAmount = lbyDOJDtls.deductionAmount;

      for (int i = 0; i < sortedSuffix.length; i++) {
        if (sortedSuffix[i].equals(lbyDOJDtls.obligationIDSuffix)) {
          perDebtData.obligationDebtSeqNumber = i;
          break;
        }
      }

      perDebtData.obligationPaymentDate = Date.getCurrentDate();
      perDebtData.personSINIdentification =
        Integer.valueOf(lbyDOJDtls.sinIdentification);
      perDebtData.personObligationID = lbyDOJDtls.obligationID;
      perDebtData.personObligationIDSuffix = lbyDOJDtls.obligationIDSuffix;
      perDebtData.obligationCanadianProvinceCode = provCode;

      // nonFeeLbyInd is true : searchDOJOutboundDebtILIs
      iliList = searchDOJDebtILIs(bdmExtLby.externalLiabilityID,
        concernRoleID, nonFeeLbyInd);

      // case nominee list
      for (final DOJOutboundDebtILIsDetails caseNomineeDtls : iliList.dtls) {

        perDebtData.obligationDebtPercentage =
          (byte) caseNomineeDtls.dojDebtRatio; // All ILI for a debt will have

        if (lbyDtls.liabilityTypeCode
          .equals(BDMEXTERNALLIABILITYTYPE.DOJ_ARREARS)) {
          perDebtData.obligationAmountPerDebt =
            new Money(perDebtData.obligationAmountPerDebt.getValue()
              + caseNomineeDtls.amount.getValue());
        } else {
          perDebtData.obligationAmtOfFeeIncluded =
            new Money(perDebtData.obligationAmtOfFeeIncluded.getValue()
              + caseNomineeDtls.amount.getValue());
        }
      }

      /*
       * This is out of scope as business mentioned the batch jobs will be kept
       * on hold until a single failed person record is resolved. Which means we
       * are not accumulating deuctions from multiple pay runs.
       * IF caseNomineeIDList.CONTAINS(caseNomineeDtls.caseNomineeID)
       * perDebtData.obligationException = "01"
       * else{
       * caseNomineeIDList.add(caseNomineeDtls.caseNomineeID)
       * }
       */

      debtsPerPerson.add(perDebtData);
    }
    nonFeeLbyInd = false;

    // Now process fee liabilities
    for (final BDMExternalLiabilityKey bdmExtLby : bdmExtlLbyKeyList.dtls) {
      bdmExtLbyKey.externalLiabilityID = bdmExtLby.externalLiabilityID;
      lbyDtls = bdmExtLbyObj.read(bdmExtLbyKey);

      dojDataKey.externalLiabilityID = bdmExtLby.externalLiabilityID;
      lbyDOJDtls = dojData.read(dojDataKey);

      if (lbyDtls.liabilityTypeCode
        .equals(BDMEXTERNALLIABILITYTYPE.DOJ_ARREARS)) {
        continue;
      }
      // nonFeeLbyInd is false : searchDOJOutboundDebtILIs
      iliList = searchDOJDebtILIs(bdmExtLby.externalLiabilityID,
        batchProcessingID.recordID, nonFeeLbyInd);

      boolean debtRecordFound = Boolean.FALSE;

      for (final BDMDoJOutboundStageDtls debtDtls : debtsPerPerson) {

        if (debtDtls.personSINIdentification == Integer
          .valueOf(lbyDOJDtls.sinIdentification)
          && debtDtls.personObligationIDSuffix
            .equals(lbyDOJDtls.obligationIDSuffix)) {

          debtRecordFound = Boolean.TRUE;

          for (final DOJOutboundDebtILIsDetails ili : iliList.dtls) {
            debtDtls.obligationAmtOfFeeIncluded =
              new Money(perDebtData.obligationAmtOfFeeIncluded.getValue()
                + ili.amount.getValue());
          }
          break;
        }
      }
      if (!debtRecordFound) {

        perDebtData.obligationOutstandingFeeBal = lbyDtls.outstandingAmount;

        for (int i = 0; i < suffixList.size(); i++) {
          if (suffixList.get(i).equals(lbyDOJDtls.obligationIDSuffix)) {
            perDebtData.obligationDebtSeqNumber = i;
            break;
          }
        }

        perDebtData.obligationPaymentDate = Date.getCurrentDate();
        perDebtData.personSINIdentification =
          Integer.valueOf(lbyDOJDtls.sinIdentification);
        perDebtData.personObligationID = lbyDOJDtls.obligationID;
        perDebtData.personObligationIDSuffix = lbyDOJDtls.obligationIDSuffix;
        perDebtData.obligationCanadianProvinceCode = provCode;
        for (final DOJOutboundDebtILIsDetails ili : iliList.dtls) {

          perDebtData.obligationAmtOfFeeIncluded =
            new Money(perDebtData.obligationAmtOfFeeIncluded.getValue()
              + ili.amount.getValue());
        }

        perDebtData.obligationFundsAvailable = obligationFundsAvailable;
        perDebtData.obligationDivertAmount = obligationDivertAmount;
        perDebtData.obligationPaidAmount = obligationPaidAmount;
        debtsPerPerson.add(perDebtData);

      }

    }

    // Inserting data into Staging table
    BDMDoJOutboundStageDtls outOblDtls;
    for (final BDMDoJOutboundStageDtls record : debtsPerPerson) {
      outOblDtls = new BDMDoJOutboundStageDtls();
      outOblDtls = record;
      outOblDtls.runID = key.runID;
      writeRecordToStagingTable(outOblDtls, key);
    }

    return null;
  }

  private String getProvincecode(final long recordID) {

    String provCode = "";

    final AddressElement addressElement = AddressElementFactory.newInstance();
    final curam.core.struct.AddressKey addressKey = new AddressKey();
    final ConcernRole concernRole = ConcernRoleFactory.newInstance();
    final curam.core.struct.ConcernRoleKey concernRoleKey =
      new ConcernRoleKey();
    concernRoleKey.concernRoleID = recordID;
    ConcernRoleDtls concernRoleDtls = new ConcernRoleDtls();
    try {
      concernRoleDtls = concernRole.readConcernRole(concernRoleKey);
    } catch (AppException | InformationalException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    addressKey.addressID = concernRoleDtls.primaryAddressID;

    AddressElementDtlsList addressElementDtlsList =
      new AddressElementDtlsList();
    try {
      addressElementDtlsList =
        addressElement.readAddressElementDetails(addressKey);
    } catch (AppException | InformationalException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    for (final AddressElementDtls addrData : addressElementDtlsList.dtls) {
      if (addrData.elementType.equals("PROV")) {
        provCode = addrData.elementValue;
        return provCode;
      }
    }
    return provCode;
  }

  private DOJOutboundDebtILIsDetailsList searchDOJDebtILIs(
    final long externalLiabilityID, final long concernRoleID,
    final boolean nonFeeLbyInd) throws AppException, InformationalException {

    final BDMPaymentInstrument bmdPmtInstrObj =
      BDMPaymentInstrumentFactory.newInstance();
    final DOJOutboundDebtILIsKey debtILIsKey = new DOJOutboundDebtILIsKey();
    debtILIsKey.externalLiabilityID = externalLiabilityID;
    debtILIsKey.concernRoleID = concernRoleID;
    debtILIsKey.dojProcessPendingInd = true;
    debtILIsKey.iliCategory = ILICATEGORY.PAYMENTINSTRUCTION;

    if (nonFeeLbyInd) {

      debtILIsKey.deductionTypeFees =
        BDMDEDUCTIONTYPE.DOJ_FOE_WITHHOLD_ARREARS;

      debtILIsKey.deductionTypeArrears =
        BDMDEDUCTIONTYPE.DOJ_FOE_WITHHOLD_ARREARS;

    } else {
      debtILIsKey.deductionTypeFees = BDMDEDUCTIONTYPE.DOJ_FOE_WITHHOLD_FEES;

      debtILIsKey.deductionTypeArrears =
        BDMDEDUCTIONTYPE.DOJ_FOE_WITHHOLD_FEES;

    }

    return bmdPmtInstrObj.searchDOJOutboundDebtILIs(debtILIsKey);

  }

  private BDMExternalLiabilityKeyList searchDoJDebtByConcernRole(
    final long recordID) throws AppException, InformationalException {

    final DOJOutboundDebtsByConcernRoleKey byConcernRoleKey =
      new DOJOutboundDebtsByConcernRoleKey();

    byConcernRoleKey.concernRoleID = recordID;
    byConcernRoleKey.deductionTypeArrears =
      BDMDEDUCTIONTYPE.DOJ_FOE_WITHHOLD_ARREARS;
    byConcernRoleKey.deductionTypeFees =
      BDMDEDUCTIONTYPE.DOJ_FOE_WITHHOLD_FEES;
    byConcernRoleKey.iliCategory = ILICATEGORY.PAYMENTINSTRUCTION;
    byConcernRoleKey.dojProcessPendingInd = true;
    final BDMPaymentInstrument bmdPmtInstrObj =
      BDMPaymentInstrumentFactory.newInstance();

    return bmdPmtInstrObj
      .searchDOJOutboundDebtsByConcernRole(byConcernRoleKey);
  }

  // Writing the data to outboundstaging table

  private DOJOutboundCrossDebtDetails readCrossDebtDetails(
    final long recordID) throws AppException, InformationalException {

    final DOJOutboundCrossDebtKey crossDebtKey =
      new DOJOutboundCrossDebtKey();

    crossDebtKey.concernRoleID = recordID;
    crossDebtKey.dojProcessPendingInd = true;
    crossDebtKey.iliCategory = ILICATEGORY.PAYMENTINSTRUCTION;
    crossDebtKey.iliTypePmtDed = ILITYPE.DEDUCTIONITEM;
    crossDebtKey.iliTypePmtTax = ILITYPE.TAXPAYMENT;
    crossDebtKey.preDOJDeductionInd = true;
    crossDebtKey.deductionTypeArrears =
      BDMDEDUCTIONTYPE.DOJ_FOE_WITHHOLD_ARREARS;
    crossDebtKey.deductionTypeFees = BDMDEDUCTIONTYPE.DOJ_FOE_WITHHOLD_FEES;

    final BDMPaymentInstrument bmdPmtInstrObj =
      BDMPaymentInstrumentFactory.newInstance();

    return bmdPmtInstrObj.readDOJOutboundCrossDebtData(crossDebtKey);

  }

  private
    curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityKey
    readLbyByExtRefNumber(final BDMExternalLiabilityDtls lbyDtls,
      final BDMExternalLiabilityDOJDataDtls lbyDOJDtls)
      throws AppException, InformationalException {

    final BDMCheckDOJLiabilityExistDetails dojLblKey =
      new BDMCheckDOJLiabilityExistDetails();

    dojLblKey.concernRoleID = lbyDtls.concernRoleID;
    dojLblKey.obligationIDSuffix = lbyDOJDtls.obligationIDSuffix;
    dojLblKey.sinIdentification = lbyDOJDtls.sinIdentification;
    dojLblKey.isFeeTypeInd = true;

    final curam.ca.gc.bdm.sl.maintaindojliability.impl.MaintainDOJLiability dojLblObj =
      new curam.ca.gc.bdm.sl.maintaindojliability.impl.MaintainDOJLiability();

    return dojLblObj.getLiabilityByExtRefNumber(dojLblKey);
  }

  private void writeRecordToStagingTable(
    final BDMDoJOutboundStageDtls outOblDtls,
    final BDMDoJOutboundLoadDataBatchKey key)
    throws AppException, InformationalException {

    final BDMDoJInboundStagingKeyStruct1 dojInboundKey =
      new BDMDoJInboundStagingKeyStruct1();

    dojInboundKey.personObligationIDSuffix =
      outOblDtls.personObligationIDSuffix;
    dojInboundKey.personSINIdentification =
      Integer.valueOf(outOblDtls.personSINIdentification);

    outOblDtls.recordID = UniqueIDFactory.newInstance().getNextID();

    outOblDtls.documentFileDateTime = Date.getCurrentDate().getDateTime();

    // Calling dojInbound staging entity
    // dojInboundDtls =doJInboundStagingObj.readBySuffixIDAndSin(dojInboundKey);
    final BDMPaymentUtil pmtUtilObj = new BDMPaymentUtil();
    // Assigning some data from dojinbound to dojoutbound
    outOblDtls.metadataIdentificationID = key.metadataIdentificationID;

    outOblDtls.transactionControlIdentificationID =
      pmtUtilObj.getGuidNumber();
    outOblDtls.documentFileControlID = BDMConstants.kfileControID;
    outOblDtls.documentFileDayCode =
      Short.valueOf(BDMConstants.kdocumentFileDayCode);
    outOblDtls.documentFileEnvironmentType =
      BDMConstants.kdocumentFileEnvironmentType;
    String hhmm = BDMConstants.EMPTY_STRING;
    final String Dddate =
      BDMDateUtil.dateFormatter(Date.getCurrentDate().toString());
    try {
      hhmm = timeFormatter(DateTime.getCurrentDateTime().toString());
    } catch (final ParseException e) {
      Trace.kTopLevelLogger.error(e.getMessage());
      e.printStackTrace();
    }

    outOblDtls.documentFileName = "FOE.DEBTORS.D" + Dddate + ".T" + hhmm + "";
    outOblDtls.documentFileWeekCode = BDMConstants.kdocumentFileWeekCode;
    outOblDtls.documentSource = BDMConstants.kdocumentSource;

    // Reading from bdmConstant file
    outOblDtls.obligationITCCode = BDMConstants.kObligationITCCode;
    outOblDtls.obligationExceptionCode =
      BDMConstants.kObligationExceptionCode;
    outOblDtls.obligationVendorCode = BDMConstants.kObligationVendorCode;
    outOblDtls.obligationOCONRegionalCode =
      BDMConstants.kObligationOCONRegionalCode;

    outOblDtls.obligationCanadianProvinceCode =
      outOblDtls.obligationCanadianProvinceCode;

    outOblDtls.processingDateTime = Date.getCurrentDate().getDateTime();

    // Setting DOJ Batch processing status
    outOblDtls.processingStatus = BDMDOJRECORDSTATUS.COMPLETED;

    final BDMDoJOutboundStage stageObj =
      BDMDoJOutboundStageFactory.newInstance();
    stageObj.insert(outOblDtls);
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

    // TODO Auto-generated method stub

  }

  public void
    setWrapper(final BDMDoJOutboundLoadDataBatchStreamWrapper wrapper) {

    this.streamWrapper = wrapper;

  }

  private static String timeFormatter(final String inputDate)
    throws ParseException {

    final SimpleDateFormat inSDF = new SimpleDateFormat(
      BDMConstants.MM_DD_YYYY_HH_MM_SS_DATE_FORMAT_SLASH_DELIMITER1);
    final SimpleDateFormat outSDF =
      new SimpleDateFormat(BDMConstants.HH_MM_FORMAT);

    String outDateTime = "";
    if (inputDate != null) {
      try {
        final java.util.Date date = inSDF.parse(inputDate);
        outDateTime = outSDF.format(date);
      } catch (final ParseException ex) {
      }
    }

    return outDateTime;
  }

}
