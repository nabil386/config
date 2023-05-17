package curam.ca.gc.bdm.batch.bdmvsgtaskallocation.impl;

import curam.ca.gc.bdm.batch.bdmvsgtaskallocation.intf.BDMVSGTaskAllocationBatchStream;
import curam.ca.gc.bdm.batch.bdmvsgtaskallocation.struct.BDMVSGTaskAllocationBatchKey;
import curam.core.impl.BatchStream;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMVSGTaskAllocationBatchStreamWrapper implements BatchStream {

  private final BDMVSGTaskAllocationBatchStream streamObj;

  public BDMVSGTaskAllocationBatchStreamWrapper(
    final BDMVSGTaskAllocationBatchStream stream, final String instanceID) {

    streamObj = stream;
    ((curam.ca.gc.bdm.batch.bdmvsgtaskallocation.impl.BDMVSGTaskAllocationBatchStream) stream)
      .setWrapper(this);
  }

  @Override
  public BatchProcessingSkippedRecord processRecord(
    final BatchProcessingID batchProcessingID, final Object parameters)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub
    return streamObj.processRecord(batchProcessingID,
      (BDMVSGTaskAllocationBatchKey) parameters);
  }

  @Override
  public void processSkippedCases(
    final BatchProcessingSkippedRecordList batchProcessingSkippedRecordList)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub
    streamObj.processSkippedCases(batchProcessingSkippedRecordList);

  }

  @Override
  public String getChunkResult(final int skippedCasesCount)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub
    return streamObj.getChunkResult(skippedCasesCount);
  }

}
