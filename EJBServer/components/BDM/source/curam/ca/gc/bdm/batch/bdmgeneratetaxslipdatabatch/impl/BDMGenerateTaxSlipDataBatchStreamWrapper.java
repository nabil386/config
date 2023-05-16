package curam.ca.gc.bdm.batch.bdmgeneratetaxslipdatabatch.impl;

import curam.ca.gc.bdm.batch.bdmgeneratetaxslipdatabatch.struct.BDMGenerateTaxSlipDataBatchKey;
import curam.core.impl.BatchStream;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMGenerateTaxSlipDataBatchStreamWrapper implements BatchStream {

  private final BDMGenerateTaxSlipDataBatchStream bdmGenerateTaxSlipDataBatchStream;

  BDMGenerateTaxSlipDataBatchKey key;

  public BDMGenerateTaxSlipDataBatchStreamWrapper(
    final BDMGenerateTaxSlipDataBatchStream bdmGenerateTaxSlipDataBatchStream,
    final BDMGenerateTaxSlipDataBatchKey key) {

    this.bdmGenerateTaxSlipDataBatchStream = bdmGenerateTaxSlipDataBatchStream;
    this.key = key;
  }

  @Override
  public BatchProcessingSkippedRecord processRecord(
    final BatchProcessingID batchProcessingID, final Object parameters)
    throws AppException, InformationalException {

    return bdmGenerateTaxSlipDataBatchStream.processRecord(batchProcessingID,
      key);
  }

  @Override
  public void processSkippedCases(
    final BatchProcessingSkippedRecordList batchProcessingSkippedRecordList)
    throws AppException, InformationalException {

    bdmGenerateTaxSlipDataBatchStream
      .processSkippedCases(batchProcessingSkippedRecordList);

  }

  @Override
  public String getChunkResult(final int skippedCasesCount)
    throws AppException, InformationalException {

    return bdmGenerateTaxSlipDataBatchStream.getChunkResult(skippedCasesCount);
  }

}
