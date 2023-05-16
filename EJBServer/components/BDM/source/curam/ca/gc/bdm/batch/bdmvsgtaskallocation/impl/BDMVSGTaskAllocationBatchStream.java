package curam.ca.gc.bdm.batch.bdmvsgtaskallocation.impl;

import curam.ca.gc.bdm.batch.bdmvsgtaskallocation.struct.BDMVSGTaskAllocationBatchKey;
import curam.ca.gc.bdm.entity.bdmbatchio.fact.BDMBatchIOFactory;
import curam.ca.gc.bdm.entity.bdmbatchio.intf.BDMBatchIO;
import curam.ca.gc.bdm.entity.bdmbatchio.struct.BDMBatchIODtls;
import curam.ca.gc.bdm.entity.bdmbatchio.struct.BDMBatchIOKey;
import curam.ca.gc.bdm.sl.fact.BDMMaintainTaskAllocationFactory;
import curam.ca.gc.bdm.sl.intf.BDMMaintainTaskAllocation;
import curam.ca.gc.bdm.sl.struct.BDMAllocateTaskKey;
import curam.codetable.BATCHPROCESSNAME;
import curam.codetable.BATCHPROCESSRESULTSTATUS;
import curam.core.impl.BatchStreamHelper;
import curam.core.impl.CuramConst;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Trace;
import org.apache.commons.lang.StringUtils;

public class BDMVSGTaskAllocationBatchStream extends
  curam.ca.gc.bdm.batch.bdmvsgtaskallocation.base.BDMVSGTaskAllocationBatchStream {

  protected int processedTaskAllocationCount = 0;

  protected int skippedTaskAllocationCount = 0;

  private BDMVSGTaskAllocationBatchStreamWrapper streamWrapper;

  @Override
  public void process(final BatchProcessStreamKey batchProcessStreamKey)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub
    if (StringUtils.isEmpty(batchProcessStreamKey.instanceID)) {
      batchProcessStreamKey.instanceID =
        BATCHPROCESSNAME.BDM_VSG_TASK_ALLOCATION_BATCH;
    }

    streamWrapper = new BDMVSGTaskAllocationBatchStreamWrapper(this,
      batchProcessStreamKey.instanceID);
    final BatchStreamHelper batchStreamHelper =
      new BDMVSGTaskAllocationBatchStreamHelper(streamWrapper);
    batchStreamHelper.runStream(batchProcessStreamKey, streamWrapper);

  }

  @Override
  public String getChunkResult(final int skippedTaskCount)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub
    final String chunkResult =
      processedTaskAllocationCount + CuramConst.gkTabDelimiter
        + (skippedTaskCount + skippedTaskAllocationCount);
    // reset counts
    processedTaskAllocationCount = 0;
    skippedTaskAllocationCount = 0;
    return chunkResult;

  }

  @Override
  public void processSkippedCases(
    final BatchProcessingSkippedRecordList batchProcessingSkippedRecordList)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub
    final BDMBatchIO bdmBatchIOObj = BDMBatchIOFactory.newInstance();
    BDMBatchIODtls bdmBatchIODtls;
    BDMBatchIOKey key;
    for (final BatchProcessingSkippedRecord batchProcessingSkippedRecord : batchProcessingSkippedRecordList.dtls) {

      Trace.kTopLevelLogger.error(batchProcessingSkippedRecord.errorMessage
        + " " + batchProcessingSkippedRecord.recordID);

      key = new BDMBatchIOKey();
      key.batchIOID = batchProcessingSkippedRecord.recordID;
      bdmBatchIODtls = bdmBatchIOObj.read(key);
      bdmBatchIODtls.processingStatus = BATCHPROCESSRESULTSTATUS.FAILED;
      bdmBatchIOObj.modify(key, bdmBatchIODtls);
    }

  }

  @Override
  public BatchProcessingSkippedRecord processRecord(
    final BatchProcessingID batchProcessingID,
    final BDMVSGTaskAllocationBatchKey key)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub
    BatchProcessingSkippedRecord skippedRecord = null;

    final BDMMaintainTaskAllocation bdmMaintainTaskAllocationObj =
      BDMMaintainTaskAllocationFactory.newInstance();

    final BDMAllocateTaskKey bdmAllocateTaskKey = new BDMAllocateTaskKey();
    try {

      // Call common SL BPO to allocate task
      bdmAllocateTaskKey.taskID = batchProcessingID.recordID;
      bdmMaintainTaskAllocationObj.allocateTask(bdmAllocateTaskKey);

    } catch (final Exception e) {

      skippedRecord = new BatchProcessingSkippedRecord();
      skippedRecord.recordID = batchProcessingID.recordID;
      skippedRecord.errorMessage = e.getMessage();
    }

    return skippedRecord;

  }

  public void
    setWrapper(final BDMVSGTaskAllocationBatchStreamWrapper wrapper) {

    this.streamWrapper = wrapper;
  }

}
