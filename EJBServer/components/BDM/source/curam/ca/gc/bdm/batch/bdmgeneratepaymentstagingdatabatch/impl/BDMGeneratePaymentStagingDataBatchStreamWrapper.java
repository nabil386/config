package curam.ca.gc.bdm.batch.bdmgeneratepaymentstagingdatabatch.impl;

import curam.ca.gc.bdm.batch.bdmgeneratepaymentstagingdatabatch.struct.GeneratePaymentsFileKey;
import curam.core.impl.BatchStream;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

/**
 * This interface is used to allow the batch streaming infrastructure to invoke
 * the implementation of the BDMGeneratePaymentStagingDataBatchStream.
 */
public class BDMGeneratePaymentStagingDataBatchStreamWrapper
  implements BatchStream {

  private final curam.ca.gc.bdm.batch.bdmgeneratepaymentstagingdatabatch.intf.BDMGeneratePaymentStagingDataBatchStream streamObj;

  private String instanceID;

  private long currentBatchProcessChunkID;

  public GeneratePaymentsFileKey key;

  public BDMGeneratePaymentStagingDataBatchStreamWrapper(
    final curam.ca.gc.bdm.batch.bdmgeneratepaymentstagingdatabatch.intf.BDMGeneratePaymentStagingDataBatchStream stream,
    final GeneratePaymentsFileKey key) {

    streamObj = stream;
    ((curam.ca.gc.bdm.batch.bdmgeneratepaymentstagingdatabatch.impl.BDMGeneratePaymentStagingDataBatchStream) stream)
      .setWrapper(this);

    this.key = key;

  }

  @Override
  public BatchProcessingSkippedRecord processRecord(
    final BatchProcessingID batchProcessingID, final Object parameters)
    throws AppException, InformationalException {

    return streamObj.processRecord(batchProcessingID, this.key);
  }

  @Override
  public void processSkippedCases(
    final BatchProcessingSkippedRecordList batchProcessingSkippedRecordList)
    throws AppException, InformationalException {

    streamObj.processSkippedCases(batchProcessingSkippedRecordList);

  }

  @Override
  public String getChunkResult(final int skippedCasesCount)
    throws AppException, InformationalException {

    return streamObj.getChunkResult(skippedCasesCount);
  }

  public void setInstanceID(final String instanceID) {

    this.instanceID = instanceID;
  }

  public void
    setCurrentBatchProcessChunkID(final long currentBatchProcessChunkID) {

    this.currentBatchProcessChunkID = currentBatchProcessChunkID;
  }

  public long getCurrentBatchProcessChunkID() {

    return this.currentBatchProcessChunkID;
  }

}
