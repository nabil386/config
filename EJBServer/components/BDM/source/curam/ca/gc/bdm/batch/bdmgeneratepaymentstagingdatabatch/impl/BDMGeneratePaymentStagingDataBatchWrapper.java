package curam.ca.gc.bdm.batch.bdmgeneratepaymentstagingdatabatch.impl;

import curam.core.impl.BatchMain;
import curam.core.struct.BatchProcessChunkDtlsList;
import curam.core.struct.BatchProcessDtls;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingResult;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Blob;

/**
 * This wrapper is used to allow the batch streaming infrastructure to invoke
 * the reporting for the BDMGeneratePaymentStagingDataBatch batch program.
 */
public class BDMGeneratePaymentStagingDataBatchWrapper implements BatchMain {

  private final curam.ca.gc.bdm.batch.bdmgeneratepaymentstagingdatabatch.impl.BDMGeneratePaymentStagingDataBatch generatePaymentsFileObj;

  public BDMGeneratePaymentStagingDataBatchWrapper(
    final curam.ca.gc.bdm.batch.bdmgeneratepaymentstagingdatabatch.impl.BDMGeneratePaymentStagingDataBatch generatePaymentsFile) {

    generatePaymentsFileObj = generatePaymentsFile;
  }

  @Override
  public BatchProcessingResult doExtraProcessing(
    final BatchProcessStreamKey batchProcessStreamKey,
    final Blob batchProcessParameters)
    throws AppException, InformationalException {

    return null;
  }

  @Override
  public void sendBatchReport(final String instanceID,
    final BatchProcessDtls batchProcessDtls,
    final BatchProcessChunkDtlsList processedBatchProcessChunkDtlsList,
    final BatchProcessChunkDtlsList unprocessedBatchProcessChunkDtlsList)
    throws AppException, InformationalException {

    generatePaymentsFileObj.sendBatchReport(instanceID, batchProcessDtls,
      processedBatchProcessChunkDtlsList,
      unprocessedBatchProcessChunkDtlsList);

  }

}
