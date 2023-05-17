package curam.ca.gc.bdm.batch.bdmprocessnonpayment.impl;

import curam.core.impl.BatchMain;
import curam.core.struct.BatchProcessChunkDtlsList;
import curam.core.struct.BatchProcessDtls;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingResult;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Blob;

public class BDMProcessNonPaymentTypesBatchWrapper implements BatchMain {

  private final BDMProcessNonPaymentTypesBatch bdmProcessNonPaymentTypesBatchObj;

  /**
   * Constructor, takes an instance of the class implementing the
   * ProviderDeductionUpgrade batch program
   */
  public BDMProcessNonPaymentTypesBatchWrapper(
    final BDMProcessNonPaymentTypesBatch bdmProcessNonPaymentTypesBatch) {

    bdmProcessNonPaymentTypesBatchObj = bdmProcessNonPaymentTypesBatch;
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

    bdmProcessNonPaymentTypesBatchObj.sendBatchReport(instanceID,
      batchProcessDtls, processedBatchProcessChunkDtlsList,
      unprocessedBatchProcessChunkDtlsList);

  }

}
