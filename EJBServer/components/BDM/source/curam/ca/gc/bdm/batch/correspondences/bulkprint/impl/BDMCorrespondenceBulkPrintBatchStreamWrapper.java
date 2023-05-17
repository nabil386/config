package curam.ca.gc.bdm.batch.correspondences.bulkprint.impl;

import curam.core.impl.BatchStream;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMCorrespondenceBulkPrintBatchStreamWrapper
  implements BatchStream {

  BDMCorrespondenceBulkPrintBatchStream bDMCorrespondenceBulkPrintBatchStream;

  /**
   * Bulk print batch stream wrapper method
   *
   * @param _bDMCorrespondenceBulkPrintBatchStream
   */
  BDMCorrespondenceBulkPrintBatchStreamWrapper(
    final BDMCorrespondenceBulkPrintBatchStream _bDMCorrespondenceBulkPrintBatchStream) {

    this.bDMCorrespondenceBulkPrintBatchStream =
      _bDMCorrespondenceBulkPrintBatchStream;
  }

  /**
   * process record method for bulk print
   *
   */
  @Override
  public BatchProcessingSkippedRecord processRecord(
    final BatchProcessingID batchProcessingID, final Object parameters)
    throws AppException, InformationalException {

    return bDMCorrespondenceBulkPrintBatchStream
      .processRecord(batchProcessingID);
  }

  /**
   * wrapper for processed skipped cases
   */
  @Override
  public void processSkippedCases(
    final BatchProcessingSkippedRecordList batchProcessingSkippedRecordList)
    throws AppException, InformationalException {

    bDMCorrespondenceBulkPrintBatchStream
      .processSkippedCases(batchProcessingSkippedRecordList);
  }

  /*
   * Method to get chunk result
   */
  @Override
  public String getChunkResult(final int skippedCasesCount)
    throws AppException, InformationalException {

    return bDMCorrespondenceBulkPrintBatchStream
      .getChunkResult(skippedCasesCount);
  }

}
