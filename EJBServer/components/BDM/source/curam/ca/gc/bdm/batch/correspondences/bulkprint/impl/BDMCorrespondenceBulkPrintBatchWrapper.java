package curam.ca.gc.bdm.batch.correspondences.bulkprint.impl;

import curam.core.impl.BatchMain;
import curam.core.struct.BatchProcessChunkDtlsList;
import curam.core.struct.BatchProcessDtls;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingResult;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Blob;

public class BDMCorrespondenceBulkPrintBatchWrapper implements BatchMain {

  BDMCorrespondenceBulkPrintBatch bDMCorrespondenceBulkPrintBatch;

  /**
   * Method of correspondence bulk print batch
   *
   * @param _bDMCorrespondenceBulkPrintBatch
   */
  BDMCorrespondenceBulkPrintBatchWrapper(
    final BDMCorrespondenceBulkPrintBatch _bDMCorrespondenceBulkPrintBatch) {

    this.bDMCorrespondenceBulkPrintBatch = _bDMCorrespondenceBulkPrintBatch;
  }

  @Override
  public BatchProcessingResult doExtraProcessing(
    final BatchProcessStreamKey batchProcessStreamKey,
    final Blob batchProcessParameters)
    throws AppException, InformationalException {

    return null;
  }

  /**
   * Method to send batch report for bulk printing
   */
  @Override
  public void sendBatchReport(final String instanceID,
    final BatchProcessDtls batchProcessDtls,
    final BatchProcessChunkDtlsList processedBatchProcessChunkDtlsList,
    final BatchProcessChunkDtlsList unprocessedBatchProcessChunkDtlsList)
    throws AppException, InformationalException {

    bDMCorrespondenceBulkPrintBatch.sendBatchReport(instanceID,
      batchProcessDtls, processedBatchProcessChunkDtlsList,
      unprocessedBatchProcessChunkDtlsList);

  }

}
