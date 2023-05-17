package curam.ca.gc.bdm.batch.bdmpurgeinprogressrfrdata.impl;

import curam.ca.gc.bdm.batch.bdmcitizenworkspacepurgeinprogressdataprocess.struct.BDMCitizenWorkspacePurgeInProgressDataProcessChunkResult;
import curam.ca.gc.bdm.batch.bdmcitizenworkspacepurgeinprogressdataprocess.struct.BDMCitizenWorkspacePurgeInProgressDataProcessKey;
import curam.citizenworkspace.codetable.impl.CitizenScriptStatusEntry;
import curam.citizenworkspace.codetable.impl.OnlineScriptTypeEntry;
import curam.citizenworkspace.entity.fact.CitizenScriptInfoFactory;
import curam.citizenworkspace.entity.struct.CitizenScriptDateScriptTypeAndStatusKey;
import curam.codetable.BATCHPROCESSNAME;
import curam.core.impl.BatchStreamHelper;
import curam.core.impl.EnvVars;
import curam.core.struct.BatchProcessChunkDtlsList;
import curam.core.struct.BatchProcessDtls;
import curam.core.struct.BatchProcessingIDList;
import curam.core.struct.ChunkMainParameters;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.type.Date;

/**
 * This class is the chunker of for the BDM RFR Purge In Progress
 * Data Process Batch. This chunker gets all of the in-progress draft RFR
 * appeal applications which has a last updated date before the configured
 * number of days from current date
 *
 *
 * @author Mahesh.Hadimani
 *
 */
public class BDMPurgeInProgressRFRDataProcess extends
  curam.ca.gc.bdm.batch.bdmpurgeinprogressrfrdata.base.BDMPurgeInProgressRFRDataProcess {

  private BatchStreamHelper batchStreamHelper;

  /**
   * This method runs the process of the chunker.
   *
   * @param bdmCitizenWorkspacePurgeInProgressDataProcessKey
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void process(
    final BDMCitizenWorkspacePurgeInProgressDataProcessKey bdmCitizenWorkspacePurgeInProgressDataProcessKey)
    throws AppException, InformationalException {

    // Retrieve the batch process name from the code table
    if (bdmCitizenWorkspacePurgeInProgressDataProcessKey.instanceID
      .isEmpty()) {
      bdmCitizenWorkspacePurgeInProgressDataProcessKey.instanceID =
        BATCHPROCESSNAME.BDM_PURGE_RFR_IN_PROGRESS_DATA_PROCESS_BATCH;
    }

    batchStreamHelper = new BatchStreamHelper();
    // Instantiate the chunkWrapper
    final BDMPurgeInProgressRFRDataProcessWrapper chunkWrapper =
      new BDMPurgeInProgressRFRDataProcessWrapper(this);
    // Instantiate the streamWrapper
    final BDMPurgeInProgressRFRDataProcessStreamWrapper streamWrapper =
      new BDMPurgeInProgressRFRDataProcessStreamWrapper(
        new curam.ca.gc.bdm.batch.bdmpurgeinprogressrfrdata.impl.BDMPurgeInProgressRFRDataProcessStream());
    // Get the parameters for the chunker
    final ChunkMainParameters chunkMainParameters = getChunkMainParameters();
    // Retrieve the ID list of the draft applications to remove
    final BatchProcessingIDList batchProcessingIDList =
      getBatchProcessingIDs();

    batchStreamHelper.runChunkMain(
      bdmCitizenWorkspacePurgeInProgressDataProcessKey.instanceID,
      bdmCitizenWorkspacePurgeInProgressDataProcessKey, chunkWrapper,
      batchProcessingIDList, chunkMainParameters, streamWrapper);
  }

  /**
   * Retrieves a list of id's to run for the streamer. All the id's are within 7
   * days of the current date
   *
   * @return The specified batch processing id list
   * @throws AppException
   * @throws InformationalException
   */
  public BatchProcessingIDList getBatchProcessingIDs()
    throws AppException, InformationalException {

    // Create a new list to store the id's
    final BatchProcessingIDList idList = new BatchProcessingIDList();
    // Create a key to narrow the search
    final CitizenScriptDateScriptTypeAndStatusKey statusKey =
      new CitizenScriptDateScriptTypeAndStatusKey();
    // Use the key created to search for multiple id's
    final BDMRFRCitizenScriptInfoReadMultiOperation submittedIntakeReadMultiOp =
      new BDMRFRCitizenScriptInfoReadMultiOperation(idList);
    // Specify that the id's should only be within X configured days in the past
    // from the
    // current date. Subtracting 1 so that applications on or before the cut-off
    // date are purged.
    final int numberOfDays = Configuration.getIntProperty(
      EnvVars.BDM_PURGE_IN_PROGRESS_RFR_DATA_PROCESS_NO_OF_DAYS) - 1;
    statusKey.status = CitizenScriptStatusEntry.OPEN.getCode();
    statusKey.date = Date.getCurrentDate().addDays(-numberOfDays);
    statusKey.scriptType = OnlineScriptTypeEntry.APPEAL.getCode();
    // Retrieve all the id's are return that list
    CitizenScriptInfoFactory.newInstance()
      .searchByDateScriptTypeAndStatus(statusKey, submittedIntakeReadMultiOp);
    return idList;
  }

  /**
   * Gets the chunk parameters for the the streamers to run
   *
   * @return The specified chunk parameter
   */
  private ChunkMainParameters getChunkMainParameters() {

    final ChunkMainParameters chunkMainParameters = new ChunkMainParameters();

    // Get the chunck size for the streamer
    chunkMainParameters.chunkSize = Configuration.getIntProperty(
      EnvVars.BDM_PURGE_IN_PROGRESS_RFR_DATA_PROCESS_CHUNK_SIZE);
    // Get the boolean of whether or not a stream is run in the chunker process
    // while waiting for the other streams to complete
    chunkMainParameters.dontRunStream = Configuration.getBooleanProperty(
      EnvVars.BDM_PURGE_IN_PROGRESS_RFR_DATA_PROCESS_DONT_RUN_STREAM);
    // Get the key value for the first chunk to be picked up by the streams
    chunkMainParameters.startChunkKey =
      Long.parseLong(Configuration.getProperty(
        EnvVars.BDM_PURGE_IN_PROGRESS_RFR_DATA_PROCESS_START_CHUNK_KEY));
    // Get the wait time for unprocessed chunks
    chunkMainParameters.unProcessedChunkReadWait =
      Configuration.getIntProperty(
        EnvVars.BDM_PURGE_IN_PROGRESS_RFR_DATA_PROCESS_UNPROCESSED_CHUNK_READ_WAIT);
    // Get the boolean that specifies if the batch should attempt to reprocess
    // unprocessed chunks
    chunkMainParameters.processUnProcessedChunks =
      Configuration.getBooleanProperty(
        EnvVars.BDM_PURGE_IN_PROGRESS_RFR_DATA_PROCESS_UNPROCESSED_CHUNKS);

    return chunkMainParameters;
  }

  @Override
  public void sendBatchReport(final String instanceID,
    final BatchProcessDtls batchProcessDtls,
    final BatchProcessChunkDtlsList processedBatchProcessChunkDtlsList,
    final BatchProcessChunkDtlsList unprocessedBatchProcessChunkDtlsList)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub

  }

  @Override
  public BDMCitizenWorkspacePurgeInProgressDataProcessChunkResult
    decodeProcessChunkResult(final String resultString)
      throws AppException, InformationalException {

    // TODO Auto-generated method stub
    return null;
  }

}
