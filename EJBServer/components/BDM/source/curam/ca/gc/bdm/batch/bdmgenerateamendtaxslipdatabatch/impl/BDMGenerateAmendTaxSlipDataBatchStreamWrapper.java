package curam.ca.gc.bdm.batch.bdmgenerateamendtaxslipdatabatch.impl;

import curam.ca.gc.bdm.batch.bdmgeneratetaxslipdatabatch.struct.BDMGenerateTaxSlipDataBatchKey;
import curam.core.impl.BatchStream;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMGenerateAmendTaxSlipDataBatchStreamWrapper
  implements BatchStream {

  private final BDMGenerateAmendTaxSlipDataBatchStream bdmGenerateAmendTaxSlipDataBatchStream;

  BDMGenerateTaxSlipDataBatchKey key;

  public BDMGenerateAmendTaxSlipDataBatchStreamWrapper(
    final BDMGenerateAmendTaxSlipDataBatchStream bdmGenerateAmendTaxSlipDataBatchStream,
    final BDMGenerateTaxSlipDataBatchKey key) {

    this.bdmGenerateAmendTaxSlipDataBatchStream =
      bdmGenerateAmendTaxSlipDataBatchStream;
    this.key = key;
  }

  @Override
  public BatchProcessingSkippedRecord processRecord(
    final BatchProcessingID batchProcessingID, final Object parameters)
    throws AppException, InformationalException {

    return bdmGenerateAmendTaxSlipDataBatchStream
      .processRecord(batchProcessingID, key);
  }

  @Override
  public void processSkippedCases(
    final BatchProcessingSkippedRecordList batchProcessingSkippedRecordList)
    throws AppException, InformationalException {

    bdmGenerateAmendTaxSlipDataBatchStream
      .processSkippedCases(batchProcessingSkippedRecordList);

  }

  @Override
  public String getChunkResult(final int skippedCasesCount)
    throws AppException, InformationalException {

    return bdmGenerateAmendTaxSlipDataBatchStream
      .getChunkResult(skippedCasesCount);
  }

}
