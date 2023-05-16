package curam.ca.gc.bdm.batch.bdms34stagingdatabatch.impl;

import curam.core.impl.BatchMain;
import curam.core.struct.BatchProcessChunkDtlsList;
import curam.core.struct.BatchProcessDtls;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingResult;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Blob;

/**
 * This wrapper is used to allow the batch streaming infrastructure to invoke
 * the reporting for the BDMGeneratePaymentStagingDataBatch batch program.
 */
public class BDMS34StagingDataBatchWrapper implements BatchMain {

  private final curam.ca.gc.bdm.batch.bdms34stagingdatabatch.impl.BDMS34StagingDataBatch s34FileObj;

  public BDMS34StagingDataBatchWrapper(
    final curam.ca.gc.bdm.batch.bdms34stagingdatabatch.impl.BDMS34StagingDataBatch s34File) {

    s34FileObj = s34File;
  }

  @Override
  public BatchProcessingResult doExtraProcessing(
    final BatchProcessStreamKey batchProcessStreamKey,
    final Blob batchProcessParameters)
    throws AppException, InformationalException {

    return null;
  }

  @Override
  public void sendBatchReport(final String instanceID,
    final BatchProcessDtls batchProcessDtls,
    final BatchProcessChunkDtlsList processedBatchProcessChunkDtlsList,
    final BatchProcessChunkDtlsList unprocessedBatchProcessChunkDtlsList)
    throws AppException, InformationalException {

    s34FileObj.sendBatchReport(instanceID, batchProcessDtls,
      processedBatchProcessChunkDtlsList,
      unprocessedBatchProcessChunkDtlsList);

  }

}
