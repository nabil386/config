package curam.ca.gc.bdm.batch.bdmtaxslipmrq.impl;

import curam.ca.gc.bdm.batch.bdmtaxslipmrq.intf.BDMTaxSlipMRQBatchStream;
import curam.core.impl.BatchStream;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMTaxSlipMRQBatchStreamWrapper implements BatchStream {

  private final BDMTaxSlipMRQBatchStream streamObj;

  private String instanceID;

  private long currentBatchProcessChunkID;

  public BDMTaxSlipMRQBatchStreamWrapper(
    final BDMTaxSlipMRQBatchStream stream) {

    streamObj = stream;
    ((curam.ca.gc.bdm.batch.bdmtaxslipmrq.impl.BDMTaxSlipMRQBatchStream) stream)
      .setWrapper(this);

  }

  @Override
  public BatchProcessingSkippedRecord processRecord(
    final BatchProcessingID batchProcessingID, final Object parameters)
    throws AppException, InformationalException {

    return streamObj.processRecord(batchProcessingID);
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
