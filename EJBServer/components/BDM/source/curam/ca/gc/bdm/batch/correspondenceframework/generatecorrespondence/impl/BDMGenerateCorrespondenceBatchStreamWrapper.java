package curam.ca.gc.bdm.batch.correspondenceframework.generatecorrespondence.impl;

import curam.core.impl.BatchStream;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMGenerateCorrespondenceBatchStreamWrapper
  implements BatchStream {

  BDMGenerateCorrespondenceBatchStream BDMGenerateCorrespondenceBatchStream;

  /**
   * Bulk print batch stream wrapper method
   *
   * @param _BDMGenerateCorrespondenceBatchStream
   */
  BDMGenerateCorrespondenceBatchStreamWrapper(
    final BDMGenerateCorrespondenceBatchStream _BDMGenerateCorrespondenceBatchStream) {

    this.BDMGenerateCorrespondenceBatchStream =
      _BDMGenerateCorrespondenceBatchStream;
  }

  /**
   * process record method for bulk print
   *
   */
  @Override
  public BatchProcessingSkippedRecord processRecord(
    final BatchProcessingID batchProcessingID, final Object parameters)
    throws AppException, InformationalException {

    return BDMGenerateCorrespondenceBatchStream
      .processRecord(batchProcessingID);
  }

  /**
   * wrapper for processed skipped cases
   */
  @Override
  public void processSkippedCases(
    final BatchProcessingSkippedRecordList batchProcessingSkippedRecordList)
    throws AppException, InformationalException {

    BDMGenerateCorrespondenceBatchStream
      .processSkippedCases(batchProcessingSkippedRecordList);
  }

  /*
   * Method to get chunk result
   */
  @Override
  public String getChunkResult(final int skippedCasesCount)
    throws AppException, InformationalException {

    return BDMGenerateCorrespondenceBatchStream
      .getChunkResult(skippedCasesCount);
  }

}
