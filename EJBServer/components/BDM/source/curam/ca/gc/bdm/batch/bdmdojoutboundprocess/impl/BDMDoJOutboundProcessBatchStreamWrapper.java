package curam.ca.gc.bdm.batch.bdmdojoutboundprocess.impl;

import curam.core.impl.BatchStream;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMDoJOutboundProcessBatchStreamWrapper implements BatchStream {

  curam.ca.gc.bdm.batch.bdmdojoutboundprocess.intf.BDMDoJOutboundProcessBatchStream streamObj;

  private String instanceID;

  public BDMDoJOutboundProcessBatchStreamWrapper(
    final curam.ca.gc.bdm.batch.bdmdojoutboundprocess.intf.BDMDoJOutboundProcessBatchStream stream) {

    streamObj = stream;
    ((BDMDoJOutboundProcessBatchStream) stream).setWrapper(this);
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

}
