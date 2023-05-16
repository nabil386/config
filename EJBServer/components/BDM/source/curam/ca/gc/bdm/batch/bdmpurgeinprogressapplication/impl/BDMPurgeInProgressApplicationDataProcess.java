package curam.ca.gc.bdm.batch.bdmpurgeinprogressapplication.impl;

import curam.ca.gc.bdm.batch.bdmpurgeinprogressapplication.fact.BDMPurgeInProgressApplicationDataProcessStreamFactory;
import curam.ca.gc.bdm.batch.bdmpurgeinprogressapplication.intf.BDMPurgeInProgressApplicationDataProcessStream;
import curam.ca.gc.bdm.batch.bdmpurgeinprogressapplication.struct.BDMPurgeInProgressApplicationChunkResult;
import curam.ca.gc.bdm.batch.bdmpurgeinprogressapplication.struct.BDMPurgeInProgressApplicationDataProcessKey;
import curam.ca.gc.bdm.entity.struct.BDMApplicationPurgeKey;
import curam.ca.gc.bdm.entity.struct.BDMPurgeApplicationDtls;
import curam.ca.gc.bdm.entity.struct.BDMPurgeApplicationDtlsList;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.codetable.BATCHPROCESSNAME;
import curam.commonintake.codetable.APPLICATIONFORMSTATUS;
import curam.commonintake.entity.fact.ApplicationFormFactory;
import curam.core.impl.BatchStreamHelper;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.struct.BatchProcessChunkDtlsList;
import curam.core.struct.BatchProcessDtls;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingIDList;
import curam.core.struct.ChunkMainParameters;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;

/**
 * chunker class for BDM Purge in progress Application
 *
 * This Batch picks up internal applications which are in progress status
 * and untouched by worker for the past 'n' Days
 *
 * @author teja.konda
 *
 * @since ADO-9297
 *
 */
public class BDMPurgeInProgressApplicationDataProcess extends
  curam.ca.gc.bdm.batch.bdmpurgeinprogressapplication.base.BDMPurgeInProgressApplicationDataProcess {

  private static BatchStreamHelper batchHelper = new BatchStreamHelper();

  // Number of records for each chunk
  private static int chunkSize;

  // whether to run chunker/streamer in multiple parallel threads
  private static boolean dontRunStream;

  // should unprocessed chunks receive additional processing
  private static boolean processUnprocessedChunks;

  // should hte stream wait until it encounters unprocessed chunk.
  private static int unprocessedChunkReadWait;

  private static int purgeDays;

  static {

    batchHelper.setStartTime();

    chunkSize = Integer.parseInt(
      Configuration.getProperty(EnvVars.BDM_IN_PROGRESS_PURGE_CHUNK_SIZE));

    processUnprocessedChunks =
      Configuration.getBooleanProperty(EnvVars.BDM_IN_PROGRESS_PURGE_CHUNKS);
    dontRunStream = Configuration
      .getBooleanProperty(EnvVars.BDM_IN_PROGRESS_PURGE_DONT_RUN_STREAM);
    unprocessedChunkReadWait = Integer.parseInt(Configuration
      .getProperty(EnvVars.BDM_IN_PROGRESS_PURGE_CHUNK_READ_WAIT));
    purgeDays = Integer.parseInt(
      Configuration.getProperty(EnvVars.BDM_IN_PROGRESS_PURGE_NO_OF_DAYS));

  }

  /**
   *
   */
  @Override
  public void process(
    final BDMPurgeInProgressApplicationDataProcessKey bdmApplyDeductionsBatchKey)
    throws AppException, InformationalException {

    Trace.kTopLevelLogger.info(
      BDMConstants.BDM_LOGS_PREFIX + " Inprogress App Purge Batch Started");

    if (bdmApplyDeductionsBatchKey.instanceID.isEmpty()) {

      bdmApplyDeductionsBatchKey.instanceID =
        BATCHPROCESSNAME.BDM_INPROGRESS_APP_PURGE;

    }

    if (bdmApplyDeductionsBatchKey.processingDate.isZero()) {

      bdmApplyDeductionsBatchKey.processingDate =
        TransactionInfo.getSystemDate();

    }

    final ChunkMainParameters chunkMainParameters = new ChunkMainParameters();

    final BDMPurgeInProgressApplicationDataProcessStream bdmInprogessPurgeStreamObj =
      BDMPurgeInProgressApplicationDataProcessStreamFactory.newInstance();

    final BDMPurgeInProgressApplicationDataProcessStreamWrapper streamWrapper =
      new BDMPurgeInProgressApplicationDataProcessStreamWrapper(
        bdmInprogessPurgeStreamObj);

    final BDMPurgeInProgressApplicationDataProcessWrapper chunkerWrapper =
      new BDMPurgeInProgressApplicationDataProcessWrapper(this);

    final BatchProcessingIDList batchProcessingIDList =
      getInprogressAppsforPurge(bdmApplyDeductionsBatchKey.processingDate);

    chunkMainParameters.chunkSize = chunkSize;
    chunkMainParameters.dontRunStream = dontRunStream;
    chunkMainParameters.processUnProcessedChunks = processUnprocessedChunks;
    chunkMainParameters.unProcessedChunkReadWait = unprocessedChunkReadWait;
    chunkMainParameters.startChunkKey = CuramConst.gkOne;

    batchHelper.runChunkMain(bdmApplyDeductionsBatchKey.instanceID,
      chunkMainParameters, chunkerWrapper, batchProcessingIDList,
      chunkMainParameters, streamWrapper);

  }

  /**
   * util method to fetch in progress application which are last touched mor
   * than 30 days ago
   *
   * @param processingDate
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private BatchProcessingIDList getInprogressAppsforPurge(
    final Date processingDate) throws AppException, InformationalException {

    final Date purgeDate = processingDate.addDays(-purgeDays);
    final BatchProcessingIDList batchProcessingIDList =
      new BatchProcessingIDList();
    BatchProcessingID batchProcessingID;

    final BDMApplicationPurgeKey purgeKey = new BDMApplicationPurgeKey();
    purgeKey.applicationFormStatus = APPLICATIONFORMSTATUS.INPROGRESS;
    purgeKey.bdmlastUpdatedDate = purgeDate;

    final BDMPurgeApplicationDtlsList applicationDtlsList =
      ApplicationFormFactory.newInstance()
        .readInprogressApplicationFormID(purgeKey);

    for (final BDMPurgeApplicationDtls applicationDtls : applicationDtlsList.dtls) {
      batchProcessingID = new BatchProcessingID();
      batchProcessingID.recordID = applicationDtls.applicationFormID;
      batchProcessingIDList.dtls.addRef(batchProcessingID);

    }

    Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
      + " No of Records Selected : " + batchProcessingIDList.dtls.size());

    return batchProcessingIDList;
  }

  @Override
  public void sendBatchReport(final String instanceID,
    final BatchProcessDtls batchProcessDtls,
    final BatchProcessChunkDtlsList processedBatchProcessChunkDtlsList,
    final BatchProcessChunkDtlsList unprocessedBatchProcessChunkDtlsList)
    throws AppException, InformationalException {

  }

  @Override
  public BDMPurgeInProgressApplicationChunkResult decodeProcessChunkResult(
    final String resultString) throws AppException, InformationalException {

    return null;
  }

}
