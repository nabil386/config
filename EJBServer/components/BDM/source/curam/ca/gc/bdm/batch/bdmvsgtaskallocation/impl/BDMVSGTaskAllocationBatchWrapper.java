package curam.ca.gc.bdm.batch.bdmvsgtaskallocation.impl;

import curam.core.impl.BatchMain;
import curam.core.struct.BatchProcessChunkDtlsList;
import curam.core.struct.BatchProcessDtls;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingResult;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Blob;

public class BDMVSGTaskAllocationBatchWrapper implements BatchMain {

  private final curam.ca.gc.bdm.batch.bdmvsgtaskallocation.intf.BDMVSGTaskAllocationBatch bdmVSGTaskAllocationBatchObj;

  public BDMVSGTaskAllocationBatchWrapper(
    final curam.ca.gc.bdm.batch.bdmvsgtaskallocation.intf.BDMVSGTaskAllocationBatch bdmVSGTaskAllocationBatch) {

    bdmVSGTaskAllocationBatchObj = bdmVSGTaskAllocationBatch;

  }

  @Override
  public BatchProcessingResult doExtraProcessing(
    final BatchProcessStreamKey batchProcessStreamKey,
    final Blob batchProcessParameters)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void sendBatchReport(final String instanceID,
    final BatchProcessDtls batchProcessDtls,
    final BatchProcessChunkDtlsList processedBatchProcessChunkDtlsList,
    final BatchProcessChunkDtlsList unprocessedBatchProcessChunkDtlsList)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub
    bdmVSGTaskAllocationBatchObj.sendBatchReport(instanceID, batchProcessDtls,
      processedBatchProcessChunkDtlsList,
      unprocessedBatchProcessChunkDtlsList);

  }

}
