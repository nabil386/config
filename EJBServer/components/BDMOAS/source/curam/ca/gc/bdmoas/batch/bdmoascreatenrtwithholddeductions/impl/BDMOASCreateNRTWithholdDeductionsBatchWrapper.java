package curam.ca.gc.bdmoas.batch.bdmoascreatenrtwithholddeductions.impl;

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
 * the reporting for the BDMOASCreateNRTWithholdDeductionsBatch
 * batch program.
 */
public class BDMOASCreateNRTWithholdDeductionsBatchWrapper
  implements BatchMain {

  private final BDMOASCreateNRTWithholdDeductionsBatch chunkerObj;

  /**
   * Public Constructor
   *
   * @param chunker - This holds the instance of
   * BDMOASCreateNRTWithholdDeductionsBatch
   */
  public BDMOASCreateNRTWithholdDeductionsBatchWrapper(
    final BDMOASCreateNRTWithholdDeductionsBatch chunker) {

    // assign the object
    this.chunkerObj = chunker;
  }

  /**
   * No extra processing exists so this method is unimplemented
   *
   * @param batchProcessStreamKey - This holds the batch process key
   * @param batchProcessParameters - This holds the batch parameters
   *
   * @return the batch processing result
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BatchProcessingResult doExtraProcessing(
    final BatchProcessStreamKey batchProcessStreamKey,
    final Blob batchProcessParameters)
    throws AppException, InformationalException {

    // return null here
    return null;
  }

  /**
   * This method is used to send batch report
   */
  @Override
  public void sendBatchReport(final String instanceID,
    final BatchProcessDtls batchProcessDtls,
    final BatchProcessChunkDtlsList processedBatchProcessChunkDtlsList,
    final BatchProcessChunkDtlsList unprocessedBatchProcessChunkDtlsList)
    throws AppException, InformationalException {

    // calling the method to send batch report
    chunkerObj.sendBatchReport(instanceID, batchProcessDtls,
      processedBatchProcessChunkDtlsList,
      unprocessedBatchProcessChunkDtlsList);

  }

}
