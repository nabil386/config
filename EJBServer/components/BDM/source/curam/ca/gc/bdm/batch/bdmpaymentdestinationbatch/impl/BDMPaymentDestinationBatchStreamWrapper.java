package curam.ca.gc.bdm.batch.bdmpaymentdestinationbatch.impl;

import curam.core.impl.BatchStream;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMPaymentDestinationBatchStreamWrapper implements BatchStream {

  private final BDMPaymentDestinationBatchStream bdmPaymentDestinationBatchStream;

  public BDMPaymentDestinationBatchStreamWrapper(
    final BDMPaymentDestinationBatchStream bdmPaymentDestinationBatchStream) {

    this.bdmPaymentDestinationBatchStream = bdmPaymentDestinationBatchStream;
  }

  @Override
  public BatchProcessingSkippedRecord processRecord(
    final BatchProcessingID batchProcessingID, final Object parameters)
    throws AppException, InformationalException {

    return bdmPaymentDestinationBatchStream.processRecord(batchProcessingID);
  }

  @Override
  public void processSkippedCases(
    final BatchProcessingSkippedRecordList batchProcessingSkippedRecordList)
    throws AppException, InformationalException {

    bdmPaymentDestinationBatchStream
      .processSkippedCases(batchProcessingSkippedRecordList);

  }

  @Override
  public String getChunkResult(final int skippedCasesCount)
    throws AppException, InformationalException {

    return bdmPaymentDestinationBatchStream.getChunkResult(skippedCasesCount);
  }

}
