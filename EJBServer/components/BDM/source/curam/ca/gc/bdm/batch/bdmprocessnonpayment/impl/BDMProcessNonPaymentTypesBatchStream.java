package curam.ca.gc.bdm.batch.bdmprocessnonpayment.impl;

import curam.ca.gc.bdm.batch.bdmprocessnonpayment.struct.BDMProcessNonPaymentTypesBatchKey;
import curam.ca.gc.bdm.codetable.BDMCDOCODE;
import curam.ca.gc.bdm.codetable.BDMESDCCODE;
import curam.ca.gc.bdm.codetable.BDMEXTERNALPROCSTATUSTYPE;
import curam.ca.gc.bdm.codetable.BDMREGIONCODE;
import curam.ca.gc.bdm.entity.fact.BDMILIStageDataFactory;
import curam.ca.gc.bdm.entity.financial.fact.BDMInstructionLineItemFactory;
import curam.ca.gc.bdm.entity.financial.struct.BDMInstructionLineItemDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMInstructionLineItemKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMSearchNonPaymentILIDetails;
import curam.ca.gc.bdm.entity.financial.struct.BDMSearchNonPaymentILIDetailsList;
import curam.ca.gc.bdm.entity.financial.struct.BDMSearchNonPaymentILIDetailsRev;
import curam.ca.gc.bdm.entity.financial.struct.BDMSearchNonPaymentILIDetailsRevList;
import curam.ca.gc.bdm.entity.financial.struct.BDMSearchNonPaymentILIKey1;
import curam.ca.gc.bdm.entity.financial.struct.BDMSearchNonPaymentILIKey2;
import curam.ca.gc.bdm.entity.financial.struct.BDMSearchNonPaymentILIKey3;
import curam.ca.gc.bdm.entity.financial.struct.BDMSearchPaymentReversalsKey;
import curam.ca.gc.bdm.entity.intf.BDMILIStageData;
import curam.ca.gc.bdm.entity.struct.BDMILICodingDetails;
import curam.ca.gc.bdm.entity.struct.BDMILIStageDataDtls;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.sl.financial.impl.BDMFinancial;
import curam.ca.gc.bdm.sl.financial.struct.BDMILIFinancialCodingDetailsList;
import curam.ca.gc.bdm.sl.financial.struct.BDMILIFinancialCodingKey;
import curam.ca.gc.bdm.util.payment.impl.BDMPaymentUtil;
import curam.codetable.BATCHPROCESSNAME;
import curam.codetable.BATCHPROCESSRESULTSTATUS;
import curam.codetable.CREDITDEBIT;
import curam.codetable.ILICATEGORY;
import curam.codetable.ILIRELATIONSHIP;
import curam.codetable.ILITYPE;
import curam.core.facade.fact.UniqueIDFactory;
import curam.core.fact.InstructionLineItemFactory;
import curam.core.impl.BatchStreamHelper;
import curam.core.impl.CuramConst;
import curam.core.intf.InstructionLineItem;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.core.struct.InstructionLineItemDtls;
import curam.core.struct.InstructionLineItemKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.RecordNotFoundException;
import curam.util.resources.Trace;
import curam.util.type.CodeTable;
import curam.util.type.Date;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;

public class BDMProcessNonPaymentTypesBatchStream extends
  curam.ca.gc.bdm.batch.bdmprocessnonpayment.base.BDMProcessNonPaymentTypesBatchStream {

  int totalProcessed = 0;

  private BDMProcessNonPaymentTypesBatchStreamWrapper streamWrapper;

  HashMap<String, String> logErrorMap = new HashMap<String, String>();

  private final String recordIdentifier = "finInstructionID";

  private final String entityIdentifier = "InstructionLineItem";

  curam.ca.gc.bdm.entity.financial.intf.BDMInstructionLineItem bdmiliObj =
    BDMInstructionLineItemFactory.newInstance();

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
    Trace.kTopLevelLogger.info("Batch Load Info:");
    // Iterating log through forEach
    logErrorMap.forEach(
      (key, value) -> Trace.kTopLevelLogger.error(key + " = " + value));

    /*
     * Output example
     *
     * EntityIdentifier = InstructionLineItem
     * RecordIdentifier = finInstructionID
     * RecordID = 1090434059777081344
     * InstanceID = BDM Generate Payment Staging Data Batch
     * ProcessingStatus = Failed
     * ErrorMessage = Record Not found
     */

  }

  @Override
  public String getChunkResult(final int skippedCasesCount)
    throws AppException, InformationalException {

    final String chunkResult =
      totalProcessed + CuramConst.gkTabDelimiter + skippedCasesCount;

    // reset count
    totalProcessed = 0;

    return chunkResult;
  }

  @Override
  public void process(final BatchProcessStreamKey streamKey)
    throws AppException, InformationalException {

    if (StringUtils.isEmpty(streamKey.instanceID)) {
      streamKey.instanceID = BATCHPROCESSNAME.BDM_DOJ_OUTBOUND_DATA_LOAD;
    }
    streamWrapper.setInstanceID(streamKey.instanceID);

    final BDMProcessNonPaymentTypesBatchKey key =
      new BDMProcessNonPaymentTypesBatchKey();
    key.instanceID = streamKey.instanceID;

    streamWrapper =
      new BDMProcessNonPaymentTypesBatchStreamWrapper(this, key);

    final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();

    batchStreamHelper.runStream(streamKey, streamWrapper);

  }

  /* Map the JV code for the non financial instructional type */
  @Override
  public BatchProcessingSkippedRecord processRecord(
    final BatchProcessingID batchProcessingID,
    final BDMProcessNonPaymentTypesBatchKey key)
    throws AppException, InformationalException {

    final BDMFinancial bdmFinancial = new BDMFinancial();

    final ArrayList<BDMILICodingDetails> finCodeList =
      new ArrayList<BDMILICodingDetails>();

    final BDMInstructionLineItemKey iliKey = new BDMInstructionLineItemKey();

    // Process Original Liabilities
    final BDMSearchNonPaymentILIKey1 iliCodeKey1 =
      new BDMSearchNonPaymentILIKey1();
    iliCodeKey1.creditDebitType = CREDITDEBIT.DEBIT;
    iliCodeKey1.extProcStatusTypeCode = BDMEXTERNALPROCSTATUSTYPE.UNPROCESSED;
    iliCodeKey1.instructLineItemCategory = ILICATEGORY.LIABILITYINSTRUCTION;
    iliCodeKey1.finInstructionID = batchProcessingID.recordID;

    final BDMSearchNonPaymentILIDetailsList nonPaymentType1List =
      bdmiliObj.searchNonPaymentTypes1(iliCodeKey1);

    for (final BDMSearchNonPaymentILIDetails lbyList : nonPaymentType1List.dtls) {
      try {
        final BDMILIFinancialCodingKey iliFinCodeKey =
          new BDMILIFinancialCodingKey();
        iliFinCodeKey.addCashTypeInd = true;
        iliFinCodeKey.instructLineItemID = lbyList.instructLineItemID;
        iliFinCodeKey.processingType = BDMConstants.kFinCodeLiability;
        final BDMILIFinancialCodingDetailsList finCodeList1 =
          bdmFinancial.getCodesByILI(iliFinCodeKey);

        // finCodeList.add(finCodeList1);

        finCodeList1.dtls.forEach(iliFinCode -> {
          final BDMILICodingDetails bdmiliCodingDetails =
            new BDMILICodingDetails();
          bdmiliCodingDetails.instructLineItemID =
            iliFinCodeKey.instructLineItemID;
          bdmiliCodingDetails.processingType = iliFinCodeKey.processingType;
          bdmiliCodingDetails.creditDebitType = iliFinCode.creditDebitType;
          bdmiliCodingDetails.assign(iliFinCode);

          finCodeList.add(bdmiliCodingDetails);
        });
        BDMInstructionLineItemDtls bdmILIDtls =
          new BDMInstructionLineItemDtls();

        iliKey.instructLineItemID = lbyList.instructLineItemID;
        bdmILIDtls = bdmiliObj.read(iliKey);
        bdmILIDtls.instructLineItemID = bdmILIDtls.instructLineItemID;
        bdmILIDtls.extProcStatusTypeCode =
          BDMEXTERNALPROCSTATUSTYPE.PROCESSED;
        bdmILIDtls.versionNo = lbyList.versionNo;
        bdmiliObj.modify(iliKey, bdmILIDtls);

      } catch (final RecordNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    // Process Liability Deductions
    final BDMSearchNonPaymentILIKey2 iliCodeKey2 =
      new BDMSearchNonPaymentILIKey2();
    iliCodeKey2.creditDebitType = CREDITDEBIT.CREDIT;
    iliCodeKey2.finInstructionID = batchProcessingID.recordID;
    iliCodeKey2.instructLineItemCategory =
      ILICATEGORY.PAYMENTINSTRUCTIONFORDEDUCTION;
    iliCodeKey2.instructionLineItemType = ILITYPE.DEDUCTIONPAYMENT;
    iliCodeKey2.extProcStatusTypeCode = BDMEXTERNALPROCSTATUSTYPE.UNPROCESSED;

    final BDMSearchNonPaymentILIDetailsList nonPaymentType2List =
      bdmiliObj.searchNonPaymentTypes2(iliCodeKey2);

    for (final BDMSearchNonPaymentILIDetails lbyDedList : nonPaymentType2List.dtls) {
      try {
        final BDMILIFinancialCodingKey iliFinCodeKey =
          new BDMILIFinancialCodingKey();
        iliFinCodeKey.addCashTypeInd = true;
        iliFinCodeKey.instructLineItemID = lbyDedList.instructLineItemID;
        iliFinCodeKey.processingType =
          BDMConstants.kFinCodeLiabilityDeduction;

        final BDMILIFinancialCodingDetailsList finCodeList2 =
          bdmFinancial.getCodesByILI(iliFinCodeKey);

        finCodeList2.dtls.forEach(iliFinCode -> {
          final BDMILICodingDetails bdmiliCodingDetails =
            new BDMILICodingDetails();
          bdmiliCodingDetails.instructLineItemID =
            iliFinCodeKey.instructLineItemID;
          bdmiliCodingDetails.processingType = iliFinCodeKey.processingType;
          bdmiliCodingDetails.creditDebitType = iliFinCode.creditDebitType;
          bdmiliCodingDetails.assign(iliFinCode);
          finCodeList.add(bdmiliCodingDetails);
        });

        BDMInstructionLineItemDtls bdmILIDtls =
          new BDMInstructionLineItemDtls();

        iliKey.instructLineItemID = lbyDedList.instructLineItemID;
        bdmILIDtls = bdmiliObj.read(iliKey);
        bdmILIDtls.instructLineItemID = bdmILIDtls.instructLineItemID;
        bdmILIDtls.extProcStatusTypeCode =
          BDMEXTERNALPROCSTATUSTYPE.PROCESSED;
        bdmILIDtls.versionNo = lbyDedList.versionNo;
        bdmiliObj.modify(iliKey, bdmILIDtls);

      } catch (final RecordNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    }

    // Payment Reversals
    final BDMSearchPaymentReversalsKey iliReversalsKey =
      new BDMSearchPaymentReversalsKey();
    iliReversalsKey.finInstructionID = batchProcessingID.recordID;
    iliReversalsKey.extProcStatusTypeCode =
      BDMEXTERNALPROCSTATUSTYPE.UNPROCESSED;
    iliReversalsKey.instructionLineItemType = ILITYPE.ALLOCATEDREVERSAL;
    iliReversalsKey.instructLineItemCategory =
      ILICATEGORY.REVERSALINSTRUCTION;
    iliReversalsKey.relationTypeCode = ILIRELATIONSHIP.REVERSALS;
    iliReversalsKey.relatedInstructLineItemCategory =
      ILICATEGORY.PAYMENTINSTRUCTION;

    final BDMSearchNonPaymentILIDetailsRevList pmtILIDtlsRevList =
      bdmiliObj.searchPaymentReversals(iliReversalsKey);

    for (final BDMSearchNonPaymentILIDetailsRev pmtRevList : pmtILIDtlsRevList.dtls) {
      try {
        final BDMILIFinancialCodingKey iliFinCodeKey =
          new BDMILIFinancialCodingKey();
        iliFinCodeKey.addCashTypeInd = true;
        iliFinCodeKey.instructLineItemID = pmtRevList.instructLineItemID;
        iliFinCodeKey.processingType = BDMConstants.kFinCodePaymentReversal;

        final BDMILIFinancialCodingDetailsList finCodeRevList =
          bdmFinancial.getCodesByILI(iliFinCodeKey);

        finCodeRevList.dtls.forEach(iliFinCode -> {
          final BDMILICodingDetails bdmiliCodingDetails =
            new BDMILICodingDetails();
          bdmiliCodingDetails.instructLineItemID =
            iliFinCodeKey.instructLineItemID;
          bdmiliCodingDetails.processingType = iliFinCodeKey.processingType;
          bdmiliCodingDetails.creditDebitType = iliFinCode.creditDebitType;
          bdmiliCodingDetails.assign(iliFinCode);
          finCodeList.add(bdmiliCodingDetails);
        });

        BDMInstructionLineItemDtls bdmILIDtls =
          new BDMInstructionLineItemDtls();

        iliKey.instructLineItemID = pmtRevList.instructLineItemID;
        bdmILIDtls = bdmiliObj.read(iliKey);
        bdmILIDtls.instructLineItemID = bdmILIDtls.instructLineItemID;
        bdmILIDtls.extProcStatusTypeCode =
          BDMEXTERNALPROCSTATUSTYPE.PROCESSED;
        bdmILIDtls.versionNo = pmtRevList.versionNo;
        bdmiliObj.modify(iliKey, bdmILIDtls);

      } catch (final RecordNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    // Liability Reversal Debits

    final BDMSearchNonPaymentILIKey3 iliCodeKey3 =
      new BDMSearchNonPaymentILIKey3();
    iliCodeKey3.extProcStatusTypeCode = BDMEXTERNALPROCSTATUSTYPE.UNPROCESSED;
    iliCodeKey3.instructLineItemCategory = ILICATEGORY.REVERSALINSTRUCTION;
    iliCodeKey3.creditDebitType = CREDITDEBIT.DEBIT;
    iliCodeKey3.relationTypeCode = ILIRELATIONSHIP.REVERSALS;
    iliCodeKey3.relatedInstructLineItemCategory =
      ILICATEGORY.LIABILITYINSTRUCTION;
    iliCodeKey3.instructionLineItemType = ILITYPE.DEDUCTIONPAYMENT;
    iliCodeKey3.relatedCreditDebitType = CREDITDEBIT.CREDIT;

    final BDMSearchNonPaymentILIDetailsList nonPaymentType3List =
      bdmiliObj.searchNonPaymentTypes3(iliCodeKey3);

    for (final BDMSearchNonPaymentILIDetails lbyReversalList : nonPaymentType3List.dtls) {
      try {
        final BDMILIFinancialCodingKey iliFinCodeKey =
          new BDMILIFinancialCodingKey();
        iliFinCodeKey.addCashTypeInd = true;
        iliFinCodeKey.instructLineItemID = lbyReversalList.instructLineItemID;
        iliFinCodeKey.processingType = BDMConstants.kFinCodeLiabilityReversal;

        final BDMILIFinancialCodingDetailsList finCodeList3 =
          bdmFinancial.getCodesByILI(iliFinCodeKey);

        finCodeList3.dtls.forEach(iliFinCode -> {
          final BDMILICodingDetails bdmiliCodingDetails =
            new BDMILICodingDetails();
          bdmiliCodingDetails.instructLineItemID =
            iliFinCodeKey.instructLineItemID;
          bdmiliCodingDetails.processingType = iliFinCodeKey.processingType;
          bdmiliCodingDetails.creditDebitType = iliFinCode.creditDebitType;
          bdmiliCodingDetails.assign(iliFinCode);
          finCodeList.add(bdmiliCodingDetails);
        });

        BDMInstructionLineItemDtls bdmILIDtls =
          new BDMInstructionLineItemDtls();

        iliKey.instructLineItemID = lbyReversalList.instructLineItemID;
        bdmILIDtls = bdmiliObj.read(iliKey);
        bdmILIDtls.instructLineItemID = bdmILIDtls.instructLineItemID;
        bdmILIDtls.extProcStatusTypeCode =
          BDMEXTERNALPROCSTATUSTYPE.PROCESSED;
        bdmILIDtls.versionNo = lbyReversalList.versionNo;
        bdmiliObj.modify(iliKey, bdmILIDtls);

      } catch (final RecordNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    // Deposits
    iliCodeKey2.creditDebitType = CREDITDEBIT.CREDIT;
    iliCodeKey2.finInstructionID = batchProcessingID.recordID;
    iliCodeKey2.instructLineItemCategory =
      ILICATEGORY.PAYMENTRECEIVEDINSTRUCTION;
    iliCodeKey2.instructionLineItemType = ILITYPE.UNALLOCATEPMTRECVD;
    iliCodeKey2.extProcStatusTypeCode = BDMEXTERNALPROCSTATUSTYPE.UNPROCESSED;

    final BDMSearchNonPaymentILIDetailsList nonPmtType2DepositList =
      bdmiliObj.searchNonPaymentTypes2(iliCodeKey2);

    for (final BDMSearchNonPaymentILIDetails depositList : nonPmtType2DepositList.dtls) {
      try {
        final BDMILIFinancialCodingKey iliFinCodeKey =
          new BDMILIFinancialCodingKey();
        iliFinCodeKey.addCashTypeInd = true;
        iliFinCodeKey.instructLineItemID = depositList.instructLineItemID;
        iliFinCodeKey.processingType = BDMConstants.kFinCodePaymentReceived;

        final BDMILIFinancialCodingDetailsList finCodeDepList2 =
          bdmFinancial.getCodesByILI(iliFinCodeKey);

        finCodeDepList2.dtls.forEach(iliFinCode -> {
          final BDMILICodingDetails bdmiliCodingDetails =
            new BDMILICodingDetails();
          bdmiliCodingDetails.instructLineItemID =
            iliFinCodeKey.instructLineItemID;
          bdmiliCodingDetails.processingType = iliFinCodeKey.processingType;
          bdmiliCodingDetails.creditDebitType = iliFinCode.creditDebitType;

          bdmiliCodingDetails.assign(iliFinCode);

          finCodeList.add(bdmiliCodingDetails);
        });

        BDMInstructionLineItemDtls bdmILIDtls =
          new BDMInstructionLineItemDtls();
        iliKey.instructLineItemID = depositList.instructLineItemID;
        bdmILIDtls = bdmiliObj.read(iliKey);
        bdmILIDtls.instructLineItemID = bdmILIDtls.instructLineItemID;
        bdmILIDtls.extProcStatusTypeCode =
          BDMEXTERNALPROCSTATUSTYPE.PROCESSED;
        bdmILIDtls.versionNo = depositList.versionNo;
        bdmiliObj.modify(iliKey, bdmILIDtls);

      } catch (AppException | InformationalException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    // Deposit Allocations
    iliCodeKey2.creditDebitType = CREDITDEBIT.CREDIT;
    iliCodeKey2.finInstructionID = batchProcessingID.recordID;
    iliCodeKey2.instructLineItemCategory =
      ILICATEGORY.PAYMENTRECEIVEDINSTRUCTION;
    iliCodeKey2.instructionLineItemType = ILITYPE.ALLOCATEDPMTRECVD;
    iliCodeKey2.extProcStatusTypeCode = BDMEXTERNALPROCSTATUSTYPE.UNPROCESSED;

    final BDMSearchNonPaymentILIDetailsList nonPmtType2DepositAllocList =
      bdmiliObj.searchNonPaymentTypes2(iliCodeKey2);

    for (final BDMSearchNonPaymentILIDetails depositAllocList : nonPmtType2DepositAllocList.dtls) {
      try {
        final BDMILIFinancialCodingKey iliFinCodeKey =
          new BDMILIFinancialCodingKey();
        iliFinCodeKey.addCashTypeInd = true;
        iliFinCodeKey.instructLineItemID =
          depositAllocList.instructLineItemID;
        iliFinCodeKey.processingType = BDMConstants.kFinCodePaymentReceived;

        final BDMILIFinancialCodingDetailsList finCodeDepAllocList2 =
          bdmFinancial.getCodesByILI(iliFinCodeKey);

        finCodeDepAllocList2.dtls.forEach(iliFinCode -> {
          final BDMILICodingDetails bdmiliCodingDetails =
            new BDMILICodingDetails();
          bdmiliCodingDetails.instructLineItemID =
            iliFinCodeKey.instructLineItemID;
          bdmiliCodingDetails.processingType = iliFinCodeKey.processingType;
          bdmiliCodingDetails.creditDebitType = iliFinCode.creditDebitType;

          bdmiliCodingDetails.assign(iliFinCode);
          finCodeList.add(bdmiliCodingDetails);
        });

        BDMInstructionLineItemDtls bdmILIDtls =
          new BDMInstructionLineItemDtls();

        iliKey.instructLineItemID = depositAllocList.instructLineItemID;
        bdmILIDtls = bdmiliObj.read(iliKey);
        bdmILIDtls.instructLineItemID = bdmILIDtls.instructLineItemID;
        bdmILIDtls.extProcStatusTypeCode =
          BDMEXTERNALPROCSTATUSTYPE.PROCESSED;
        bdmILIDtls.versionNo = depositAllocList.versionNo;
        bdmiliObj.modify(iliKey, bdmILIDtls);

      } catch (final RecordNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    for (final BDMILICodingDetails finCode : finCodeList) {

      writeDataToILI(finCode, key, false);
    }

    return null;

  }

  // Writing JV codes into BDMILIStageData table
  private void writeDataToILI(final BDMILICodingDetails finCode,
    final BDMProcessNonPaymentTypesBatchKey key,
    final boolean underPaymentIND)
    throws AppException, InformationalException {

    final BDMILIStageData stageDataObj = BDMILIStageDataFactory.newInstance();
    final BDMILIStageDataDtls bdmiliStageDataDtls = new BDMILIStageDataDtls();
    bdmiliStageDataDtls.creationDate = Date.getCurrentDate();
    bdmiliStageDataDtls.paymentAmount = finCode.jvAmount;
    bdmiliStageDataDtls.instructLineItemID = finCode.instructLineItemID;

    final InstructionLineItemDtls iliDtls =
      readILI(finCode.instructLineItemID);

    bdmiliStageDataDtls.instructionLineItemType =
      iliDtls.instructionLineItemType;

    bdmiliStageDataDtls.requisitionNumber = getReqNumber(
      iliDtls.effectiveDate.toString(), key.regionSequenceNumber);

    bdmiliStageDataDtls.paymentDueDate = iliDtls.effectiveDate;
    bdmiliStageDataDtls.regionSequenceNumber = key.regionSequenceNumber;

    bdmiliStageDataDtls.companyCode = finCode.jvCompanyCode;
    bdmiliStageDataDtls.costCentreCode = finCode.jvCostCentreCode;
    bdmiliStageDataDtls.functionalAreaCode = finCode.jvFunctionalAreaCode;
    bdmiliStageDataDtls.fundCode = finCode.jvFundCode;
    bdmiliStageDataDtls.gLCode = finCode.jvGLCode;

    bdmiliStageDataDtls.regionTypeCd = finCode.regionTypeCd;
    bdmiliStageDataDtls.financialItemType = finCode.financialItemType;
    bdmiliStageDataDtls.financialCategoryType = finCode.financialCategoryType;
    bdmiliStageDataDtls.programCode = finCode.programType;
    bdmiliStageDataDtls.underPaymentIND = underPaymentIND;
    bdmiliStageDataDtls.processingType = finCode.processingType;
    bdmiliStageDataDtls.creditDebitType = finCode.creditDebitType;
    bdmiliStageDataDtls.financialIND = false;
    bdmiliStageDataDtls.recordID =
      UniqueIDFactory.newInstance().getNextID().uniqueID;
    stageDataObj.insert(bdmiliStageDataDtls);
  }

  private InstructionLineItemDtls readILI(final long instructLineItemID)
    throws AppException, InformationalException {

    final InstructionLineItem iliObj =
      InstructionLineItemFactory.newInstance();

    final InstructionLineItemKey iliKey = new InstructionLineItemKey();
    iliKey.instructLineItemID = instructLineItemID;
    final InstructionLineItemDtls iliDtls = iliObj.read(iliKey);
    return iliDtls;
  }

  private String getReqNumber(final String dueDate, final int seqNo)
    throws AppException, InformationalException {

    final BDMPaymentUtil pmtUtilObj = new BDMPaymentUtil();

    final String reqNum =
      pmtUtilObj.getRequisitionNumber(BDMESDCCODE.BDMESDCCODE,
        BDMCDOCODE.BDMCDOCODE, dueDate, BDMREGIONCODE.REGIONCODE, seqNo);
    return reqNum;
  }

  public void

    setWrapper(final BDMProcessNonPaymentTypesBatchStreamWrapper wrapper) {

    this.streamWrapper = wrapper;
  }

}
