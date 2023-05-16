package curam.ca.gc.bdm.batch.bdmpurgeinprogressrfrdata.impl;

import curam.core.impl.BatchStream;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

/**
 * This class is the wrapper for the streamer class for BDM Purge In Progress
 * RFR Data Process Batch.
 *
 * @author Mahesh.Hadimani
 *
 */
public class BDMPurgeInProgressRFRDataProcessStreamWrapper
  implements BatchStream {

  private final BDMPurgeInProgressRFRDataProcessStream bdmPurgeInProgressRFRDataProcessStream;

  public BDMPurgeInProgressRFRDataProcessStreamWrapper(
    final BDMPurgeInProgressRFRDataProcessStream bdmPurgeInProgressRFRDataProcessStream) {

    this.bdmPurgeInProgressRFRDataProcessStream =
      bdmPurgeInProgressRFRDataProcessStream;
  }

  /**
   * Process the record passed into the method to the streamer class.
   *
   * @param The ID of the object that will be processed
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BatchProcessingSkippedRecord processRecord(
    final BatchProcessingID batchProcessingID, final Object parameters)
    throws AppException, InformationalException {

    return bdmPurgeInProgressRFRDataProcessStream
      .processRecord(batchProcessingID);
  }

  /**
   * Process the skipped cases into the method to the streamer class.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void processSkippedCases(
    final BatchProcessingSkippedRecordList batchProcessingSkippedRecordList)
    throws AppException, InformationalException {

    bdmPurgeInProgressRFRDataProcessStream
      .processSkippedCases(batchProcessingSkippedRecordList);
  }

  /**
   * Retrieve the chunk result based on the count of skipped cases.
   *
   * @param The number of skipped cases
   * @throws AppException
   * @throws InformationalException
   *
   */
  @Override
  public String getChunkResult(final int skippedCasesCount)
    throws AppException, InformationalException {

    return bdmPurgeInProgressRFRDataProcessStream
      .getChunkResult(skippedCasesCount);
  }

}
