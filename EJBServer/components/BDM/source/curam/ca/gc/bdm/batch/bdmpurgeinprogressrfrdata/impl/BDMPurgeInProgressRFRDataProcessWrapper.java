package curam.ca.gc.bdm.batch.bdmpurgeinprogressrfrdata.impl;

import curam.core.impl.BatchMain;
import curam.core.struct.BatchProcessChunkDtlsList;
import curam.core.struct.BatchProcessDtls;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingResult;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Blob;

/**
 * This class is the wrapper for the chunker class for the BDM Purge In Progress
 * RFR Data Process Batch.
 *
 * @author Mahesh.Hadimani
 *
 */
public class BDMPurgeInProgressRFRDataProcessWrapper implements BatchMain {

  private final BDMPurgeInProgressRFRDataProcess bdmPurgeInProgressRFRDataProcess;

  public BDMPurgeInProgressRFRDataProcessWrapper(
    final BDMPurgeInProgressRFRDataProcess bdmPurgeInProgressRFRDataProcess) {

    this.bdmPurgeInProgressRFRDataProcess = bdmPurgeInProgressRFRDataProcess;
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

    bdmPurgeInProgressRFRDataProcess.sendBatchReport(instanceID,
      batchProcessDtls, processedBatchProcessChunkDtlsList,
      unprocessedBatchProcessChunkDtlsList);

  }

}
