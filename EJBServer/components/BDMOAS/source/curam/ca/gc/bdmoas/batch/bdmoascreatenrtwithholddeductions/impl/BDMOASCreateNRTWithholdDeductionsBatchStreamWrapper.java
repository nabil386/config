package curam.ca.gc.bdmoas.batch.bdmoascreatenrtwithholddeductions.impl;

import curam.core.impl.BatchStream;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

/**
 * This interface is used to allow the batch streaming infrastructure to invoke
 * the implementation of the
 * BDMOASCreateNRTWithholdDeductionsBatchStream.
 */
public class BDMOASCreateNRTWithholdDeductionsBatchStreamWrapper
  implements BatchStream {

  private final curam.ca.gc.bdmoas.batch.bdmoascreatenrtwithholddeductions.intf.BDMOASCreateNRTWithholdDeductionsBatchStream streamObj;

  /**
   * Constructor takes an instance of the class implementing the
   * BDMOASCreateNRTWithholdDeductionsBatchStream
   *
   * @param stream - Instance of the class
   * BDMOASCreateNRTWithholdDeductionsBatchStream
   */
  public BDMOASCreateNRTWithholdDeductionsBatchStreamWrapper(
    final curam.ca.gc.bdmoas.batch.bdmoascreatenrtwithholddeductions.intf.BDMOASCreateNRTWithholdDeductionsBatchStream stream) {

    // assign the stream object here
    streamObj = stream;

  }

  /**
   * Call the processRecord method of
   * BDMOASCreateNRTWithholdDeductionsBatchStream
   *
   * @param batchProcessingID - This holds the batch processing ID
   * @param parameters - This holds the batch input parameters
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BatchProcessingSkippedRecord processRecord(
    final BatchProcessingID batchProcessingID, final Object parameters)
    throws AppException, InformationalException {

    return streamObj.processRecord(batchProcessingID);
  }

  /**
   * Call the processSkippedCases method of
   * BDMOASCreateNRTWithholdDeductionsBatchStream
   *
   * @param batchProcessingSkippedRecordList - This holds the skipped records
   * list
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void processSkippedCases(
    final BatchProcessingSkippedRecordList batchProcessingSkippedRecordList)
    throws AppException, InformationalException {

    streamObj.processSkippedCases(batchProcessingSkippedRecordList);

  }

  /**
   * Call the getChunkResult method of
   * BDMOASCreateNRTWithholdDeductionsBatchStream
   *
   * @param skippedCasesCount - This holds the skipped cases count
   *
   * @return the chunk result.
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public String getChunkResult(final int skippedCasesCount)
    throws AppException, InformationalException {

    // calling the method to get the chunk results
    return streamObj.getChunkResult(skippedCasesCount);
  }
}
