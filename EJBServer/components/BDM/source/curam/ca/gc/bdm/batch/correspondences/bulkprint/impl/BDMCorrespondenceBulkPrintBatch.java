package curam.ca.gc.bdm.batch.correspondences.bulkprint.impl;

import curam.ca.gc.bdm.batch.correspondences.bulkprint.struct.BDMCorrespondenceBulkPrintBatchKey;
import curam.ca.gc.bdm.batch.correspondences.bulkprint.struct.BDMCorrespondenceBulkPrintChunkResult;
import curam.ca.gc.bdm.entity.communication.fact.BDMConcernRoleCommunicationSubFactory;
import curam.ca.gc.bdm.entity.communication.intf.BDMConcernRoleCommunicationSub;
import curam.ca.gc.bdm.entity.communication.struct.BDMSearchByCSCFRSTStruct;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.codetable.BATCHPROCESSNAME;
import curam.codetable.COMMUNICATIONFORMAT;
import curam.codetable.COMMUNICATIONSTATUS;
import curam.codetable.RECORDSTATUS;
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
import curam.core.struct.ConcernRoleCommunicationDtls;
import curam.core.struct.ConcernRoleCommunicationDtlsList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.type.StringList;
import org.apache.commons.lang.StringUtils;

public class BDMCorrespondenceBulkPrintBatch extends
  curam.ca.gc.bdm.batch.correspondences.bulkprint.base.BDMCorrespondenceBulkPrintBatch {

  private static final String kBulkPrintOutputFileID =
    "BDM_Correspondence_Bulk_Print_Output";

  final CuramBatch curamBatchObj = new CuramBatch();

  ChunkMainParameters chunkMainParameters = new ChunkMainParameters();

  /** Process bulk print batch */
  @Override
  public void
    process(final BDMCorrespondenceBulkPrintBatchKey bdmBulkPrintBatchKey)
      throws AppException, InformationalException {

    getChunkMainParameters(chunkMainParameters);

    final BDMCorrespondenceBulkPrintBatchStreamWrapper streamWrapper =
      new BDMCorrespondenceBulkPrintBatchStreamWrapper(
        new BDMCorrespondenceBulkPrintBatchStream());

    final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();

    final BDMCorrespondenceBulkPrintBatchWrapper chunkerWrapper =
      new BDMCorrespondenceBulkPrintBatchWrapper(this);

    // Retrieve the records that need to be processed
    final BatchProcessingIDList batchProcessingIDList =
      getBatchProcessingIDList();

    final String instanceID =
      !StringUtils.isEmpty(bdmBulkPrintBatchKey.instanceID)
        ? bdmBulkPrintBatchKey.instanceID
        : BATCHPROCESSNAME.BDM_CORRESPONDENCE_BULK_PRINT_BATCH;

    batchStreamHelper.runChunkMain(instanceID, bdmBulkPrintBatchKey,
      chunkerWrapper, batchProcessingIDList, chunkMainParameters,
      streamWrapper);

  }

  /**
   * retrieves the Submitted and Resubmitted Communications
   *
   * @return BatchProcessingIDList
   * @throws AppException
   * @throws InformationalException
   */
  protected BatchProcessingIDList getBatchProcessingIDList()
    throws AppException, InformationalException {

    final BDMConcernRoleCommunicationSub crcObj =
      BDMConcernRoleCommunicationSubFactory.newInstance();
    final BDMSearchByCSCFRSTStruct searchStruct =
      new BDMSearchByCSCFRSTStruct();
    searchStruct.communicationStatus1 = COMMUNICATIONSTATUS.SUBMITTED;
    searchStruct.communicationStatus2 = COMMUNICATIONSTATUS.RESUBMITTED;
    searchStruct.communicationFormat1 = COMMUNICATIONFORMAT.PROFORMA;
    searchStruct.communicationFormat2 = COMMUNICATIONFORMAT.MSWORD;
    searchStruct.statusCode = RECORDSTATUS.NORMAL;
    final ConcernRoleCommunicationDtlsList crcDtlsList =
      crcObj.searchByCSCFRST(searchStruct);

    final BatchProcessingIDList processingIDList =
      new BatchProcessingIDList();

    BatchProcessingID processingID;

    for (final ConcernRoleCommunicationDtls commDtls : crcDtlsList.dtls
      .items()) {
      processingID = new BatchProcessingID();
      processingID.recordID = commDtls.communicationID;
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

    final StringBuffer emailMessage = new StringBuffer();
    StringList resultsList = null;

    for (final BatchProcessChunkDtls processedBatchProcessChunkDtls : processedBatchProcessChunkDtlsList.dtls) {
      resultsList = StringUtil
        .tabText2StringList(processedBatchProcessChunkDtls.resultSummary);
      if (resultsList.size() > 0) {
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

    // TODO: set email subject cat entry

    curamBatchObj.outputFileID = kBulkPrintOutputFileID;

    curamBatchObj.setStartTime(batchProcessDtls.startDateTime);
    curamBatchObj.setEndTime();

    // send email
    // curamBatchObj.sendEmail();

  }

  @Override
  public BDMCorrespondenceBulkPrintChunkResult decodeProcessChunkResult(
    final String resultString) throws AppException, InformationalException {

    // TODO Auto-generated method stub
    return null;
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
      .getIntProperty(EnvVars.BDM_CORRESPONDENCE_BULK_PRINT_BATCH_CHUNK_SIZE);
    chunkMainParameters.dontRunStream = Configuration.getBooleanProperty(
      EnvVars.BDM_CORRESPONDENCE__BULK_PRINT_BATCH_DONT_RUN_STREAM);
    chunkMainParameters.startChunkKey =
      Long.parseLong(Configuration.getProperty(
        EnvVars.BDM_CORRESPONDENCE_BULK_PRINT_BATCH_START_CHUNK_KEY));
    chunkMainParameters.unProcessedChunkReadWait =
      Configuration.getIntProperty(
        EnvVars.BDM_CURAM_CORRESPONDENCE_BULK_PRINT_BATCH_UNPROCESSED_CHUNK_READ_WAIT);
    chunkMainParameters.processUnProcessedChunks =
      Configuration.getBooleanProperty(
        EnvVars.BDM_CURAM_CORRESPONDENCE_BULK_PRINT_BATCH_PROCESS_UNPROCESSED_CHUNKS);

    return chunkMainParameters;
  }

}
