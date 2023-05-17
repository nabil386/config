package curam.ca.gc.bdm.batch.bdmpaymentreturn.impl;

import curam.ca.gc.bdm.codetable.BDMPMTRECONCILIATIONSTATUS;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.fact.BDMPRNStagingDataFactory;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.fact.BDMPaymentStagingDataFactory;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.intf.BDMPRNStagingData;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.intf.BDMPaymentStagingData;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMPRNStagingDataDtls;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMPRNStagingDataDtlsStruct2;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMPRNStagingDataKey;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMPRNStagingDataKeyStruct1;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMPaymentStagingDataDtlsStruct1;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMPaymentStagingDataKeyStruct1;
import curam.ca.gc.bdm.entity.financial.fact.BDMPaymentInstrumentFactory;
import curam.ca.gc.bdm.entity.financial.intf.BDMPaymentInstrument;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentInstrumentDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentInstrumentDtlsStruct2;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentInstrumentDtlsStruct2List;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentInstrumentKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentInstrumentKeyStruct2;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.codetable.BATCHPROCESSNAME;
import curam.codetable.BATCHPROCESSRESULTSTATUS;
import curam.codetable.METHODOFDELIVERY;
import curam.codetable.PMTRECONCILIATIONSTATUS;
import curam.core.fact.PaymentInstrumentFactory;
import curam.core.impl.BatchStreamHelper;
import curam.core.impl.CuramConst;
import curam.core.intf.PaymentInstrument;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.core.struct.PIReconcilStatusCode;
import curam.core.struct.PaymentInstrumentDtls;
import curam.core.struct.PaymentInstrumentKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Trace;
import curam.util.type.CodeTable;
import curam.util.type.DateTime;
import curam.util.type.NotFoundIndicator;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;

public class BDMPRNBatchStream
  extends curam.ca.gc.bdm.batch.bdmpaymentreturn.base.BDMPRNBatchStream {

  private BDMPRNBatchStreamWrapper streamWrapper;

  protected int processedInstrumentsCount = 0;

  protected int skippedInstrumentsCount = 0;

  private final String recordIdentifier = "recordID";

  private final String entityIdentifier = "BDMPRNStagingData";

  HashMap<String, String> logErrorMap = new HashMap<String, String>();

  @Override
  public void process(final BatchProcessStreamKey batchProcessStreamKey)
    throws AppException, InformationalException {

    streamWrapper = new BDMPRNBatchStreamWrapper(this);

    final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();

    if (StringUtils.isEmpty(batchProcessStreamKey.instanceID)) {
      batchProcessStreamKey.instanceID =
        BATCHPROCESSNAME.BDM_PAYMENT_RETURN_BATCH;
    }
    streamWrapper.setInstanceID(batchProcessStreamKey.instanceID);

    batchStreamHelper.runStream(batchProcessStreamKey, streamWrapper);

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

      // task-63419 - As per code review : Update the Status to Fail

      final BDMPRNStagingData pmtReturnData =
        BDMPRNStagingDataFactory.newInstance();
      final BDMPRNStagingDataKey dataKey = new BDMPRNStagingDataKey();
      final BDMPRNStagingDataKeyStruct1 prnStageKey =
        new BDMPRNStagingDataKeyStruct1();
      BDMPRNStagingDataDtls dataDtls = new BDMPRNStagingDataDtls();
      BDMPRNStagingDataDtlsStruct2 prnStageDtls =
        new BDMPRNStagingDataDtlsStruct2();

      prnStageKey.spsPmtGroupID = batchProcessingSkippedRecord.recordID;
      prnStageDtls = pmtReturnData.readBySpsPmtGroupID(prnStageKey);

      dataKey.recordID = prnStageDtls.recordID;
      dataDtls = pmtReturnData.read(dataKey);
      dataDtls.processingDateTime = DateTime.getCurrentDateTime();
      dataDtls.processingStatus = BATCHPROCESSRESULTSTATUS.FAILED;
      pmtReturnData.modify(dataKey, dataDtls);

    }

    // Iterating log through forEach
    logErrorMap.forEach(
      (key, value) -> Trace.kTopLevelLogger.error(key + " = " + value));

    /*
     * Output example
     *
     * EntityIdentifier = BDMPRNStagingData
     * RecordIdentifier = participantRoleID
     * RecordID = 1090434059777081344
     * InstanceID = BDM Generate Payment Staging Data Batch
     * ProcessingStatus = Failed
     * ErrorMessage = Record Not found
     */

  }

  /*
   * Modify the payment status 'Transferred' to 'Issued' and update the payment
   * reference number , Data returning from SPS system
   */
  @Override
  public BatchProcessingSkippedRecord
    processRecord(final BatchProcessingID batchProcessingID)
      throws AppException, InformationalException {

    final PaymentInstrument instrObj = PaymentInstrumentFactory.newInstance();

    final BDMPaymentInstrument bdmInstrObj =
      BDMPaymentInstrumentFactory.newInstance();

    final BDMPRNStagingDataKey dataKey = new BDMPRNStagingDataKey();

    BatchProcessingSkippedRecord skippedRecord = null;

    final BDMPRNStagingData pmtReturnData =
      BDMPRNStagingDataFactory.newInstance();

    // BEGIN: 63224: PRN RequisitionNumber validation
    final BDMPaymentStagingData bdmPaymentStagingData =
      BDMPaymentStagingDataFactory.newInstance();
    final BDMPaymentStagingDataKeyStruct1 bdmPaymentStagingDataKey =
      new BDMPaymentStagingDataKeyStruct1();
    BDMPaymentStagingDataDtlsStruct1 paymentStagingDataDtls =
      new BDMPaymentStagingDataDtlsStruct1();
    // END: 63224: PRN RequisitionValidation Validation

    final PaymentInstrumentKey instrumentKey = new PaymentInstrumentKey();

    PaymentInstrumentDtls instrDtls = new PaymentInstrumentDtls();

    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    final BDMPaymentInstrumentKey bdmPaymentInstrumentKey =
      new BDMPaymentInstrumentKey();
    BDMPaymentInstrumentDtls bdmPmtInstrDtls = new BDMPaymentInstrumentDtls();

    final BDMPaymentInstrumentKeyStruct2 bdmInstrKey =
      new BDMPaymentInstrumentKeyStruct2();
    bdmInstrKey.spsPmtGroupID = batchProcessingID.recordID;

    BDMPaymentInstrumentDtlsStruct2List bdmPmtInstrList =
      new BDMPaymentInstrumentDtlsStruct2List();
    BDMPRNStagingDataDtls dataDtls = new BDMPRNStagingDataDtls();
    // Search BDMPaymentInstrument record by spsPmtGroupID
    bdmPmtInstrList = bdmInstrObj.searchBySpsPmtGroupID(bdmInstrKey);

    if (bdmPmtInstrList.dtls.size() == 0) {
      Trace.kTopLevelLogger
        .error("SPS Payment Group ID - '" + bdmInstrKey.spsPmtGroupID
          + "' is not found in BDMPaymentInstrument table.");
    }

    final BDMPRNStagingDataKeyStruct1 prnStageKey =
      new BDMPRNStagingDataKeyStruct1();
    BDMPRNStagingDataDtlsStruct2 prnStageDtls =
      new BDMPRNStagingDataDtlsStruct2();
    prnStageKey.spsPmtGroupID = batchProcessingID.recordID;
    prnStageDtls = pmtReturnData.readBySpsPmtGroupID(prnStageKey);
    Trace.kTopLevelLogger.error("SPS Payment Group ID - '"
      + bdmInstrKey.spsPmtGroupID + "' is being used.");
    Trace.kTopLevelLogger.error("Processing BDMPRNStagingData record id '"
      + prnStageDtls.recordID + "' now.");

    for (final BDMPaymentInstrumentDtlsStruct2 bdmPmt : bdmPmtInstrList.dtls) {

      instrumentKey.pmtInstrumentID = bdmPmt.pmtInstrumentID;
      try {
        // BEGIN, 63224 : Validation of REQUISITIONNUMBER
        // Validate that 'REQUISITIONNUMBER' from 'BDMPAYMENTSTAGINGDATA'
        // for the 'pmtInstrumentID' matches with the value from
        // 'BDMPRNStagingData'
        bdmPaymentStagingDataKey.pmtInstrumentID = bdmPmt.pmtInstrumentID;
        paymentStagingDataDtls = bdmPaymentStagingData
          .readByPmtInstrumentID(nfIndicator, bdmPaymentStagingDataKey);

        if (!nfIndicator.isNotFound()) {
          // validate if the RequisitionNumber matches and proceed for further
          // processing
          if (paymentStagingDataDtls != null
            && paymentStagingDataDtls.requisitionNumber
              .equals(prnStageDtls.requisitionNumber)) {
            // END, 63224 : Validation of REQUISITIONNUMBER

            instrDtls = instrObj.read(nfIndicator, instrumentKey);

            if (!nfIndicator.isNotFound()) {

              if (bdmPmt.reconcilStatusCode
                .equals(BDMPMTRECONCILIATIONSTATUS.TRANSFERRED)
                && !instrDtls.reconcilStatusCode
                  .equals(PMTRECONCILIATIONSTATUS.ISSUED)) {

                // modify the ootb Payment status to ISSUED
                instrObj.modifyReconcilStatusCode(instrumentKey,
                  getReconcilStatus(PMTRECONCILIATIONSTATUS.ISSUED,
                    instrDtls.versionNo));

                bdmPaymentInstrumentKey.pmtInstrumentID =
                  bdmPmt.pmtInstrumentID;

                Trace.kTopLevelLogger.info("getting BDMPaymentInstrument id '"
                  + bdmPaymentInstrumentKey.pmtInstrumentID + "' now.");
                bdmPmtInstrDtls = bdmInstrObj.read(bdmPaymentInstrumentKey);
                bdmPmtInstrDtls.spsPmtRefNo = prnStageDtls.spsPmtRefNo;

                // task-67775: update payment due
                bdmPmtInstrDtls.paymentDueDate = prnStageDtls.paymentDueDate;

                if (prnStageDtls.paymentInstrumentType == BDMConstants.PAYMENT_TYPE_CHEQUE) {
                  bdmPmtInstrDtls.spsDeliveryMethodType =
                    METHODOFDELIVERY.CHEQUE;
                } else {
                  bdmPmtInstrDtls.spsDeliveryMethodType =
                    METHODOFDELIVERY.EFT;
                }

                // Update SPS Ref number to BDMPaymentInstrument
                bdmInstrObj.modify(bdmPaymentInstrumentKey, bdmPmtInstrDtls);

                // Update the batch processing status to process to
                // BDMPRNStagingData
                dataKey.recordID = prnStageDtls.recordID;
                dataDtls = pmtReturnData.read(dataKey);
                dataDtls.processingDateTime = DateTime.getCurrentDateTime();
                dataDtls.processingStatus =
                  BATCHPROCESSRESULTSTATUS.PROCESSED;
                pmtReturnData.modify(dataKey, dataDtls);

              } else {
                Trace.kTopLevelLogger
                  .error("Payment with ID - '" + bdmPmt.pmtInstrumentID
                    + "' is not in transferred status.");
              }

            } else {
              // If a payment is not found
              Trace.kTopLevelLogger
                .error("Payment with ID - '" + instrDtls.pmtInstrumentID
                  + "' is not found in the database.");
            }
          }
          // BEGIN, 63224 - Validate RequisitionNumber
          // Will enter here if the requisition ID match check failed.
          else {
            Trace.kTopLevelLogger.error("Payment with ID - '"
              + bdmPmt.pmtInstrumentID
              + "' Requistition number is not matching BDMPRNStagingData and BDMPaymentStagingData");
          }
        } else {
          // If a payment is not found in BDMPaymentStagingData
          Trace.kTopLevelLogger
            .error("Payment with ID - '" + instrDtls.pmtInstrumentID
              + "' is not found in the BDMPaymentStagingData.");
        }
        // END, 63224 - Validate RequisitionNumber
        processedInstrumentsCount++;

      } catch (final Exception e) {
        Trace.kTopLevelLogger.error(
          "An error has occurred and record " + batchProcessingID.recordID
            + " is being skipped with error: " + e.getMessage());
        skippedRecord = new BatchProcessingSkippedRecord();
        skippedRecord.recordID = batchProcessingID.recordID;
        skippedRecord.errorMessage = e.getMessage();
        skippedInstrumentsCount++;
      }

    }

    return skippedRecord;
  }

  // Update the Reconcile status of payment
  private PIReconcilStatusCode getReconcilStatus(final String status,
    final int versionNo) {

    final PIReconcilStatusCode statusCode = new PIReconcilStatusCode();
    statusCode.reconcilStatusCode = status;
    statusCode.versionNo = versionNo;

    return statusCode;
  }

  public void setWrapper(final BDMPRNBatchStreamWrapper wrapper) {

    this.streamWrapper = wrapper;
  }

}
