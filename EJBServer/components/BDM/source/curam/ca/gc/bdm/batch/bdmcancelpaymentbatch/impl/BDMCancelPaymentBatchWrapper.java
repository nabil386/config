package curam.ca.gc.bdm.batch.bdmcancelpaymentbatch.impl;

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
public class BDMCancelPaymentBatchWrapper implements BatchMain {

  private final curam.ca.gc.bdm.batch.bdmcancelpaymentbatch.impl.BDMCancelPaymentBatch cancelPaymentFileObj;

  public BDMCancelPaymentBatchWrapper(
    final curam.ca.gc.bdm.batch.bdmcancelpaymentbatch.impl.BDMCancelPaymentBatch cancelPaymentFile) {

    cancelPaymentFileObj = cancelPaymentFile;
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

    cancelPaymentFileObj.sendBatchReport(instanceID, batchProcessDtls,
      processedBatchProcessChunkDtlsList,
      unprocessedBatchProcessChunkDtlsList);

  }

}
