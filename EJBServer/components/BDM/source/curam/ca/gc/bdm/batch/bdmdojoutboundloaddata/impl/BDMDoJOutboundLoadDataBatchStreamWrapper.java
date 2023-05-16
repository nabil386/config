package curam.ca.gc.bdm.batch.bdmdojoutboundloaddata.impl;

import curam.ca.gc.bdm.batch.bdmdojoutboundloaddata.struct.BDMDoJOutboundLoadDataBatchKey;
import curam.core.impl.BatchStream;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMDoJOutboundLoadDataBatchStreamWrapper implements BatchStream {

  private final curam.ca.gc.bdm.batch.bdmdojoutboundloaddata.intf.BDMDoJOutboundLoadDataBatchStream streamObj;

  private String instanceID;
  
  BDMDoJOutboundLoadDataBatchKey key;

  public BDMDoJOutboundLoadDataBatchStreamWrapper(
    final curam.ca.gc.bdm.batch.bdmdojoutboundloaddata.intf.BDMDoJOutboundLoadDataBatchStream stream, BDMDoJOutboundLoadDataBatchKey key) {

    streamObj = stream;
    ((BDMDoJOutboundLoadDataBatchStream) stream).setWrapper(this);
    
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

}
