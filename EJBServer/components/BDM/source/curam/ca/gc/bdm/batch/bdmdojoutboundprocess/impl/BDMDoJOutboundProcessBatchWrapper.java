package curam.ca.gc.bdm.batch.bdmdojoutboundprocess.impl;

import curam.core.impl.BatchMain;
import curam.core.struct.BatchProcessChunkDtlsList;
import curam.core.struct.BatchProcessDtls;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingResult;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Blob;

public class BDMDoJOutboundProcessBatchWrapper implements BatchMain {

  BDMDoJOutboundProcessBatch outboundProcessBatch;

  public BDMDoJOutboundProcessBatchWrapper(
    final BDMDoJOutboundProcessBatch bdmDoJOutboundProcessBatch) {

    outboundProcessBatch = bdmDoJOutboundProcessBatch;
  }

  @Override
  public void sendBatchReport(final String instanceID,
    final BatchProcessDtls batchProcessDtls,
    final BatchProcessChunkDtlsList processedBatchProcessChunkDtlsList,
    final BatchProcessChunkDtlsList unprocessedBatchProcessChunkDtlsList)
    throws AppException, InformationalException {

    outboundProcessBatch.sendBatchReport(instanceID, batchProcessDtls,
      processedBatchProcessChunkDtlsList,
      unprocessedBatchProcessChunkDtlsList);

  }

  @Override
  public BatchProcessingResult doExtraProcessing(
    final BatchProcessStreamKey batchProcessStreamKey,
    final Blob batchProcessParameters)
    throws AppException, InformationalException {

    return outboundProcessBatch.doExtraProcessing(batchProcessStreamKey);
  }

}
