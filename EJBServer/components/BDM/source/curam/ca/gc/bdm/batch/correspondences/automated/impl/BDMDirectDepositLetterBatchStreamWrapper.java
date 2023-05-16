package curam.ca.gc.bdm.batch.correspondences.automated.impl;

import curam.core.impl.BatchStream;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMDirectDepositLetterBatchStreamWrapper implements BatchStream {

  BDMDirectDepositLetterBatchStream BDMCreateDirectDepositLetterBatchStream;

  /**
   * Wrapper method main
   *
   * @param _BDMCreateDirectDepositLetterBatchStream batch stream wrapper param
   */
  BDMDirectDepositLetterBatchStreamWrapper(
    final BDMDirectDepositLetterBatchStream _BDMCreateDirectDepositLetterBatchStream) {

    this.BDMCreateDirectDepositLetterBatchStream =
      _BDMCreateDirectDepositLetterBatchStream;
  }

  /**
   * Wrapper method to call process record for the batch stream
   */
  @Override
  public BatchProcessingSkippedRecord processRecord(
    final BatchProcessingID batchProcessingID, final Object parameters)
    throws AppException, InformationalException {

    return BDMCreateDirectDepositLetterBatchStream
      .processRecord(batchProcessingID);
  }

  /**
   * wrapper method to run process skipped method
   */
  @Override
  public void processSkippedCases(
    final BatchProcessingSkippedRecordList batchProcessingSkippedRecordList)
    throws AppException, InformationalException {

    BDMCreateDirectDepositLetterBatchStream
      .processSkippedCases(batchProcessingSkippedRecordList);
  }

  /**
   * Wrapper to get chuck results
   */
  @Override
  public String getChunkResult(final int skippedCasesCount)
    throws AppException, InformationalException {

    return BDMCreateDirectDepositLetterBatchStream
      .getChunkResult(skippedCasesCount);
  }

}
