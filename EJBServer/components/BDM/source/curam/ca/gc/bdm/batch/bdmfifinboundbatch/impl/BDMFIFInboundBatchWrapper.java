package curam.ca.gc.bdm.batch.bdmfifinboundbatch.impl;

import curam.core.impl.BatchMain;
import curam.core.struct.BatchProcessChunkDtlsList;
import curam.core.struct.BatchProcessDtls;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingResult;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Blob;

public class BDMFIFInboundBatchWrapper implements BatchMain {

  BDMFIFInboundBatch batch;

  public BDMFIFInboundBatchWrapper(BDMFIFInboundBatch inboundBatch) {

    inboundBatch = batch;

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

    batch.sendBatchReport(instanceID, batchProcessDtls,
      processedBatchProcessChunkDtlsList,
      unprocessedBatchProcessChunkDtlsList);
  }

}
