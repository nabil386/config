package curam.ca.gc.bdm.batch.bdmgenerateamendtaxslipdatabatch.impl;

import curam.core.impl.BatchMain;
import curam.core.struct.BatchProcessChunkDtlsList;
import curam.core.struct.BatchProcessDtls;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingResult;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Blob;

public class BDMGenerateAmendTaxSlipDataBatchWrapper implements BatchMain {

  private final BDMGenerateAmendTaxSlipDataBatch bdmGenerateAmendTaxSlipDataBatch;

  public BDMGenerateAmendTaxSlipDataBatchWrapper(
    final BDMGenerateAmendTaxSlipDataBatch bdmGenerateAmendTaxSlipDataBatch) {

    this.bdmGenerateAmendTaxSlipDataBatch = bdmGenerateAmendTaxSlipDataBatch;
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

    bdmGenerateAmendTaxSlipDataBatch.sendBatchReport(instanceID,
      batchProcessDtls, processedBatchProcessChunkDtlsList,
      unprocessedBatchProcessChunkDtlsList);

  }

}
