package curam.ca.gc.bdm.batch.bdmrefreshcasedeductionstatus.impl;

import curam.core.impl.BatchMain;
import curam.core.struct.BatchProcessChunkDtlsList;
import curam.core.struct.BatchProcessDtls;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingResult;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Blob;

public class BDMRefreshCaseDeductionStatusBatchWrapper implements BatchMain {

  private final curam.ca.gc.bdm.batch.bdmrefreshcasedeductionstatus.intf.BDMRefreshCaseDeductionStatusBatch bdmRefreshCaseDeductionStatusBatchObj;

  public BDMRefreshCaseDeductionStatusBatchWrapper(
    final curam.ca.gc.bdm.batch.bdmrefreshcasedeductionstatus.intf.BDMRefreshCaseDeductionStatusBatch bdmRefreshCaseDeductionStatusBatchObj) {

    this.bdmRefreshCaseDeductionStatusBatchObj =
      bdmRefreshCaseDeductionStatusBatchObj;
  }

  @Override
  public BatchProcessingResult doExtraProcessing(
    final BatchProcessStreamKey batchProcessStreamKey,
    final Blob batchProcessParameters)
    throws AppException, InformationalException {

    // No implementation needed for this.
    return null;
  }

  @Override
  public void sendBatchReport(final String instanceID,
    final BatchProcessDtls batchProcessDtls,
    final BatchProcessChunkDtlsList processedBatchProcessChunkDtlsList,
    final BatchProcessChunkDtlsList unprocessedBatchProcessChunkDtlsList)
    throws AppException, InformationalException {

    this.bdmRefreshCaseDeductionStatusBatchObj.sendBatchReport(instanceID,
      batchProcessDtls, processedBatchProcessChunkDtlsList,
      unprocessedBatchProcessChunkDtlsList);

  }

}
