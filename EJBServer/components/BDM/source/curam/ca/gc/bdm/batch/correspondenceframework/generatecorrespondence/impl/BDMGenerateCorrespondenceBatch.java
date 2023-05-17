package curam.ca.gc.bdm.batch.correspondenceframework.generatecorrespondence.impl;

import curam.ca.gc.bdm.batch.correspondenceframework.generatecorrespondence.struct.BDMGenerateCorrespondenceBatchKey;
import curam.ca.gc.bdm.entity.communication.struct.BDMCorrespondenceStagingDtls;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.codetable.BATCHPROCESSNAME;
import curam.codetable.CORRESPONDENCEJOBSTATUS;
import curam.core.impl.BatchStreamHelper;
import curam.core.impl.CuramBatch;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.struct.BatchProcessChunkDtls;
import curam.core.struct.BatchProcessChunkDtlsList;
import curam.core.struct.BatchProcessDtls;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingIDList;
import curam.core.struct.ChunkMainParameters;
import curam.util.dataaccess.CuramValueList;
import curam.util.dataaccess.DynamicDataAccess;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.type.Date;
import curam.util.type.StringList;
import org.apache.commons.lang3.StringUtils;

public class BDMGenerateCorrespondenceBatch extends
  curam.ca.gc.bdm.batch.correspondenceframework.generatecorrespondence.base.BDMGenerateCorrespondenceBatch {

  private static final String kBulkPrintOutputFileID =
    "BDM_Generate_Correspondence_Output";

  final CuramBatch curamBatchObj = new CuramBatch();

  ChunkMainParameters chunkMainParameters = new ChunkMainParameters();

  /** Process bulk print batch */
  @Override
  public void process(final BDMGenerateCorrespondenceBatchKey key)
    throws AppException, InformationalException {

    getChunkMainParameters(chunkMainParameters);

    final BDMGenerateCorrespondenceBatchStreamWrapper streamWrapper =
      new BDMGenerateCorrespondenceBatchStreamWrapper(
        new BDMGenerateCorrespondenceBatchStream());

    final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();

    final BDMGenerateCorrespondenceBatchWrapper chunkerWrapper =
      new BDMGenerateCorrespondenceBatchWrapper(this);

    if (key.processingDate.isZero()) {
      key.processingDate = Date.getCurrentDate();
    }
    if (key.status.isEmpty()) {
      key.status = CORRESPONDENCEJOBSTATUS.PENDING;
    }

    // Retrieve the records that need to be processed
    final BatchProcessingIDList batchProcessingIDList =
      getBatchProcessingIDList(key);

    final String instanceID = !StringUtils.isEmpty(key.instanceID)
      ? key.instanceID : BATCHPROCESSNAME.BDM_GENERATE_CORRESPONDENCE_BATCH;

    batchStreamHelper.runChunkMain(instanceID, key, chunkerWrapper,
      batchProcessingIDList, chunkMainParameters, streamWrapper);

  }

  /**
   * retrieves the staged records for processing
   *
   * @return BatchProcessingIDList
   * @throws AppException
   * @throws InformationalException
   */
  protected BatchProcessingIDList
    getBatchProcessingIDList(final BDMGenerateCorrespondenceBatchKey key)
      throws AppException, InformationalException {

    String sql = "SELECT " + "correspondenceStagingID " + " INTO "
      + " :correspondenceStagingID " + " FROM " + "bdmcorrespondencestaging "
      + " WHERE "
      + " dateforsending =  :processingDate and status = :status ";

    if (!key.templateName.isEmpty()) {
      sql += " and templatename = :templateName   ";
    }

    if (!key.frequency.isEmpty()) {
      sql += " and frequency =  :frequency  ";
    }

    final CuramValueList<BDMCorrespondenceStagingDtls> curamValueList =
      DynamicDataAccess.executeNsMulti(BDMCorrespondenceStagingDtls.class,
        key, false, true, sql);

    final BatchProcessingIDList processingIDList =
      new BatchProcessingIDList();

    BatchProcessingID processingID;
    for (final BDMCorrespondenceStagingDtls correspondenceStagingDtls : curamValueList
      .items()) {
      processingID = new BatchProcessingID();
      processingID.recordID =
        correspondenceStagingDtls.correspondenceStagingID;
      processingIDList.dtls.addRef(processingID);
    }

    Trace.kTopLevelLogger.info(
      BDMConstants.kNoOfRecordsToProcess + processingIDList.dtls.size());
    return processingIDList;
  }

  /**
   * Batch method to send report of batches that have been processed.
   */
  @Override
  public void sendBatchReport(final String instanceID,
    final BatchProcessDtls batchProcessDtls,
    final BatchProcessChunkDtlsList processedBatchProcessChunkDtlsList,
    final BatchProcessChunkDtlsList unprocessedBatchProcessChunkDtlsList)
    throws AppException, InformationalException {

    long totalNumberOfRecordProcessed = 0;
    long totalNumberOfRecordSkipped = 0;
    final long totalNumberOfUnprocessedChunks =
      unprocessedBatchProcessChunkDtlsList.dtls.size();

    final StringBuilder emailMessage = new StringBuilder();
    StringList resultsList = null;

    for (final BatchProcessChunkDtls processedBatchProcessChunkDtls : processedBatchProcessChunkDtlsList.dtls) {
      resultsList = StringUtil
        .tabText2StringList(processedBatchProcessChunkDtls.resultSummary);
      if (!resultsList.isEmpty()) {
        totalNumberOfRecordProcessed += Integer.parseInt(resultsList.item(0));
        totalNumberOfRecordSkipped += Integer.parseInt(resultsList.item(1));
      }
    }

    if (totalNumberOfUnprocessedChunks > 0) {
      emailMessage.append(CuramConst.gkNewLine)
        .append(BDMConstants.kUnprocessedRecords
          + totalNumberOfUnprocessedChunks * chunkMainParameters.chunkSize)
        .append(CuramConst.gkNewLine);
    }

    emailMessage.append(CuramConst.gkNewLine)
      .append(BDMConstants.kProcessedRecords + totalNumberOfRecordProcessed)
      .append(CuramConst.gkNewLine);

    if (totalNumberOfRecordSkipped > 0) {

      emailMessage.append(CuramConst.gkNewLine)
        .append(BDMConstants.kSkippedRecords + totalNumberOfRecordSkipped)
        .append(CuramConst.gkNewLine);
    }

    curamBatchObj.emailMessage = emailMessage.toString();

    curamBatchObj.outputFileID = kBulkPrintOutputFileID;

    curamBatchObj.setStartTime(batchProcessDtls.startDateTime);
    curamBatchObj.setEndTime();

  }

  /**
   * chunker main parameters for the batch
   *
   * @param chunkMainParameters
   * @return
   */
  protected ChunkMainParameters
    getChunkMainParameters(final ChunkMainParameters chunkMainParameters) {

    chunkMainParameters.chunkSize = Configuration
      .getIntProperty(EnvVars.BDM_GENERATE_CORRESPONDENCE_BATCH_CHUNK_SIZE);
    chunkMainParameters.dontRunStream = Configuration.getBooleanProperty(
      EnvVars.BDM_GENERATE_CORRESPONDENCE_BATCH_DONT_RUN_STREAM);
    chunkMainParameters.startChunkKey =
      Long.parseLong(Configuration.getProperty(
        EnvVars.BDM_GENERATE_CORRESPONDENCE_BATCH_START_CHUNK_KEY));
    chunkMainParameters.unProcessedChunkReadWait =
      Configuration.getIntProperty(
        EnvVars.BDM_GENERATE_CORRESPONDENCE_BATCH_UNPROCESSED_CHUNK_READ_WAIT);
    chunkMainParameters.processUnProcessedChunks =
      Configuration.getBooleanProperty(
        EnvVars.BDM_GENERATE_CORRESPONDENCE_BATCH_PROCESS_UNPROCESSED_CHUNKS);

    return chunkMainParameters;
  }

}
