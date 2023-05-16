package curam.ca.gc.bdm.batch.bdmcitizenworkspacepurgeinprogressdataprocess.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.batch.bdmcitizenworkspacepurgeinprogressdataprocess.struct.BDMCitizenWorkspacePurgeInProgressDataProcessChunkResult;
import curam.citizenworkspace.scriptinfo.impl.CitizenScriptInfo;
import curam.citizenworkspace.scriptinfo.impl.CitizenScriptInfoDAO;
import curam.codetable.BATCHPROCESSNAME;
import curam.core.impl.BatchStreamHelper;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.ieg.impl.IEGScriptExecutionFactory;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.workspaceservices.intake.impl.IntakeApplication;
import curam.workspaceservices.intake.impl.IntakeApplicationDAO;
import curam.workspaceservices.util.impl.DatastoreHelper;
import java.util.List;

/**
 * This class is the streamer of for the BDM Citizen Workspace Purge In Progress
 * Data Process Batch. This streamer removes all in-progress draft applications
 * which has a last updated date before 7 days than the current date.
 *
 * @author alim.maredia
 *
 */
public class BDMCitizenWorkspacePurgeInProgressDataProcessStream extends
  curam.ca.gc.bdm.batch.bdmcitizenworkspacepurgeinprogressdataprocess.base.BDMCitizenWorkspacePurgeInProgressDataProcessStream {

  protected static final BDMCitizenWorkspacePurgeInProgressDataProcessChunkResult chunkResult =
    new BDMCitizenWorkspacePurgeInProgressDataProcessChunkResult();

  @Inject
  private CitizenScriptInfoDAO citizenScriptInfoDAO;

  @Inject
  private IntakeApplicationDAO intakeApplicationDAO;

  public BDMCitizenWorkspacePurgeInProgressDataProcessStream() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  private final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();

  /**
   * This method runs the process of the streamer.
   *
   * @param batchProcessStreamKey
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void process(final BatchProcessStreamKey batchProcessStreamKey)
    throws AppException, InformationalException {

    if (batchProcessStreamKey.instanceID.isEmpty()) {
      batchProcessStreamKey.instanceID =
        BATCHPROCESSNAME.BDM_CITIZEN_WORKSPACE_PURGE_IN_PROGRESS_DATA_PROCESS_BATCH;
    }

    final BDMCitizenWorkspacePurgeInProgressDataProcessStreamWrapper streamWrapper =
      new BDMCitizenWorkspacePurgeInProgressDataProcessStreamWrapper(this);

    batchStreamHelper.runStream(batchProcessStreamKey, streamWrapper);
  }

  /**
   * This method gets the Chunk result.
   *
   * @param skippedCasesCount the number of skipped cases
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public String getChunkResult(final int skippedCasesCount)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Processes a record with a batch processing ID. If the last updated date of
   * the draft application is before 7 days of the current date, created by an
   * internal user and the state of the application is IN_PROGRESS, remove that
   * draft application.
   *
   * @param batchProcessingID
   * @return BatchProcessingSkippedRecord
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BatchProcessingSkippedRecord
    processRecord(final BatchProcessingID batchProcessingID)
      throws AppException, InformationalException {

    Long datastoreID = 0L;
    String schema = null;
    final IEGScriptExecutionFactory scriptExecutionFactory =
      IEGScriptExecutionFactory.getInstance();
    final CitizenScriptInfo citizenScriptInfo =
      this.citizenScriptInfoDAO.get(batchProcessingID.recordID);

    datastoreID = citizenScriptInfo.getDatastoreID();
    schema = citizenScriptInfo.getDatastoreSchema();

    // Remove the draft application that was specified
    scriptExecutionFactory
      .removeScriptExecutionObject(citizenScriptInfo.getID());
    citizenScriptInfo.remove();

    // If there was no draft application found, then delete the datastoreID
    if (0L != datastoreID) {
      final List<CitizenScriptInfo> citizenScriptInfoList =
        this.citizenScriptInfoDAO.searchByDatastore(datastoreID);
      final IntakeApplication application =
        this.intakeApplicationDAO.readByRootEntityID(datastoreID);
      if (citizenScriptInfoList.isEmpty() && null == application) {
        final Datastore datastore = DatastoreHelper.openDatastore(schema);
        final Entity rootEntity = datastore.readEntity(datastoreID);
        if (rootEntity != null) {
          rootEntity.delete();
        }
      }
    }

    chunkResult.totalCasesProcessedCount += 1;
    return null;
  }

  /**
   * This method processes the skipped cases.
   *
   * @param batchProcessingSkippedRecordList
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void processSkippedCases(
    final BatchProcessingSkippedRecordList batchProcessingSkippedRecordList)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub

  }

}
