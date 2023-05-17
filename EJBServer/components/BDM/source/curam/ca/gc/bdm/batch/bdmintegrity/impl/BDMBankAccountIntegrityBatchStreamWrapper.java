package curam.ca.gc.bdm.batch.bdmintegrity.impl;

import curam.core.impl.BatchStream;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMBankAccountIntegrityBatchStreamWrapper
  implements BatchStream {

  private final BDMBankAccountIntegrityBatchStream bdmBankAccountIntegrityBatchStream;

  public BDMBankAccountIntegrityBatchStreamWrapper(
    final BDMBankAccountIntegrityBatchStream bdmBankAccountIntegrityBatchStream) {

    this.bdmBankAccountIntegrityBatchStream =
      bdmBankAccountIntegrityBatchStream;
  }

  @Override
  public BatchProcessingSkippedRecord processRecord(
    final BatchProcessingID batchProcessingID, final Object parameters)
    throws AppException, InformationalException {

    return bdmBankAccountIntegrityBatchStream
      .processRecord(batchProcessingID);
  }

  @Override
  public void processSkippedCases(
    final BatchProcessingSkippedRecordList batchProcessingSkippedRecordList)
    throws AppException, InformationalException {

    bdmBankAccountIntegrityBatchStream
      .processSkippedCases(batchProcessingSkippedRecordList);

  }

  @Override
  public String getChunkResult(final int skippedCasesCount)
    throws AppException, InformationalException {

    return bdmBankAccountIntegrityBatchStream
      .getChunkResult(skippedCasesCount);
  }

}
