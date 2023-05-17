package curam.ca.gc.bdm.batch.bdmdojoutboundloaddata.impl;

import curam.core.impl.BatchMain;
import curam.core.struct.BatchProcessChunkDtlsList;
import curam.core.struct.BatchProcessDtls;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingResult;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Blob;

public class BDMDoJOutboundLoadDataBatchWrapper implements BatchMain {

  private final BDMDoJOutboundLoadDataBatch bdmDoJOutboundBatchBatchObj;

  /**
   * Constructor, takes an instance of the class implementing the
   * ProviderDeductionUpgrade batch program
   */
  public BDMDoJOutboundLoadDataBatchWrapper(
    final BDMDoJOutboundLoadDataBatch bdmDoJOutboundBatch) {

    bdmDoJOutboundBatchBatchObj = bdmDoJOutboundBatch;
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

    bdmDoJOutboundBatchBatchObj.sendBatchReport(instanceID, batchProcessDtls,
      processedBatchProcessChunkDtlsList,
      unprocessedBatchProcessChunkDtlsList);

  }

}
