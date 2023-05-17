package curam.ca.gc.bdm.batch.bdmcitizenworkspacepurgeinprogressdataprocess.impl;

import curam.core.impl.BatchMain;
import curam.core.struct.BatchProcessChunkDtlsList;
import curam.core.struct.BatchProcessDtls;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingResult;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Blob;

/**
 * This class is the wrapper for the chunker class for the BDM Citizen Workspace
 * Purge In Progress Data Process Batch.
 *
 * @author alim.maredia
 *
 */
public class BDMCitizenWorkspacePurgeInProgressDataProcessWrapper
  implements BatchMain {

  private final BDMCitizenWorkspacePurgeInProgressDataProcess bdmCitizenWorkspacePurgeInProgressDataProcess;

  public BDMCitizenWorkspacePurgeInProgressDataProcessWrapper(
    final BDMCitizenWorkspacePurgeInProgressDataProcess bdmCitizenWorkspacePurgeInProgressDataProcess) {

    this.bdmCitizenWorkspacePurgeInProgressDataProcess =
      bdmCitizenWorkspacePurgeInProgressDataProcess;
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

    bdmCitizenWorkspacePurgeInProgressDataProcess.sendBatchReport(instanceID,
      batchProcessDtls, processedBatchProcessChunkDtlsList,
      unprocessedBatchProcessChunkDtlsList);

  }

}
