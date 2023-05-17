package curam.ca.gc.bdm.batch.bdmpurgeinprogressapplication.impl;

import curam.core.impl.BatchMain;
import curam.core.struct.BatchProcessChunkDtlsList;
import curam.core.struct.BatchProcessDtls;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingResult;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Blob;

/**
 * This class is the wrapper for the chunker class for the BDM
 * Purge In Progress Application Data Process Batch.
 *
 * @author teja.konda
 *
 */
public class BDMPurgeInProgressApplicationDataProcessWrapper
  implements BatchMain {

  private final curam.ca.gc.bdm.batch.bdmpurgeinprogressapplication.intf.BDMPurgeInProgressApplicationDataProcess bdmPurgeInProgressApplicationDataProcess;

  public BDMPurgeInProgressApplicationDataProcessWrapper(
    final curam.ca.gc.bdm.batch.bdmpurgeinprogressapplication.intf.BDMPurgeInProgressApplicationDataProcess bdmPurgeInProgressApplicationDataProcess) {

    this.bdmPurgeInProgressApplicationDataProcess =
      bdmPurgeInProgressApplicationDataProcess;
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

    bdmPurgeInProgressApplicationDataProcess.sendBatchReport(instanceID,
      batchProcessDtls, processedBatchProcessChunkDtlsList,
      unprocessedBatchProcessChunkDtlsList);

  }

}
