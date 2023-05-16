package curam.ca.gc.bdm.batch.bdmgeneratepaymentstagingdatabatch.impl;

import curam.ca.gc.bdm.batch.bdmbatchutil.impl.BDMBatchUtil;
import curam.ca.gc.bdm.batch.bdmgeneratepaymentstagingdatabatch.fact.BDMGeneratePaymentStagingDataBatchStreamFactory;
import curam.ca.gc.bdm.batch.bdmgeneratepaymentstagingdatabatch.struct.GeneratePaymentsFileKey;
import curam.ca.gc.bdm.codetable.BDMPMTRECONCILIATIONSTATUS;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.fact.BDMPaymentStagingDataFactory;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.intf.BDMPaymentStagingData;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMPaymentStagingDataDtlsList;
import curam.ca.gc.bdm.entity.fact.BDMILIStageDataFactory;
import curam.ca.gc.bdm.entity.financial.fact.BDMPaymentInstrumentFactory;
import curam.ca.gc.bdm.entity.financial.intf.BDMPaymentInstrument;
import curam.ca.gc.bdm.entity.financial.struct.BDMParticipantRoleID;
import curam.ca.gc.bdm.entity.financial.struct.BDMSearchParticipantKey;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMGENERATEPAYMENTSTAGINGDATA;
import curam.ca.gc.bdm.util.payment.impl.BDMPaymentUtil;
import curam.codetable.BATCHPROCESSNAME;
import curam.codetable.PMTRECONCILIATIONSTATUS;
import curam.core.fact.UniqueIDFactory;
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
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.ProgramLocale;
import curam.util.resources.StringUtil;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.StringList;
import org.apache.commons.lang.StringUtils;

/**
 * Batch class that implements streaming and chunks to process payment
 * instrument and populate staging table.
 */
public class BDMGeneratePaymentStagingDataBatch extends
  curam.ca.gc.bdm.batch.bdmgeneratepaymentstagingdatabatch.base.BDMGeneratePaymentStagingDataBatch {

  /**
   * The number of records per chunk.
   */
  private static final int kChunkSize =
    getIntProperty(EnvVars.BDM_GENERATEPAYMENTSTAGINGDATA_CHUNK_SIZE,
      EnvVars.BDM_GENERATEPAYMENTSTAGINGDATA_CHUNK_SIZE_DEFAULT);

  /**
   * Controls whether the chunker should start a stream.
   */
  private static final boolean kDontRunStream = Configuration
    .getBooleanProperty(EnvVars.BDM_GENERATEPAYMENTSTAGINGDATA_DONTRUNSTREAM,
      EnvVars.BDM_GENERATEPAYMENTSTAGINGDATA_DONTRUNSTREAM_DEFAULT);

  /**
   * Wait time for unprocessed chunks.
   */
  private static final int kUnProcessedChunkReadWait = getIntProperty(
    EnvVars.BDM_GENERATEPAYMENTSTAGINGDATA_UNPROCESSED_CHUNK_WAIT_INTERVAL,
    EnvVars.BDM_GENERATEPAYMENTSTAGINGDATA_UNPROCESSED_CHUNK_WAIT_INTERVAL_DEFAULT);

  /**
   * Indicates whether to process unprocessed chunks at the end.
   */
  private static final boolean kProcessUnProcessedChunks =
    Configuration.getBooleanProperty(
      EnvVars.BDM_GENERATEPAYMENTSTAGINGDATA_PROCESS_UNPROCESSED_CHUNKS,
      false);

  /* Loads the payment instrument data */

  @Override
  public void process(final GeneratePaymentsFileKey key)
    throws AppException, InformationalException {

    final ChunkMainParameters chunkMainParameters = new ChunkMainParameters();

    final curam.ca.gc.bdm.batch.bdmgeneratepaymentstagingdatabatch.intf.BDMGeneratePaymentStagingDataBatchStream streamObj =
      BDMGeneratePaymentStagingDataBatchStreamFactory.newInstance();

    final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();

    key.regionSequenceNumber = getSeqNumber();

    final BDMGeneratePaymentStagingDataBatchWrapper chunkerWrapper =
      new BDMGeneratePaymentStagingDataBatchWrapper(this);

    // Retrieve the records that need to be processed
    final BatchProcessingIDList batchProcessingIDList = getRecordsToProcess();

    chunkMainParameters.chunkSize = kChunkSize;
    chunkMainParameters.dontRunStream = kDontRunStream;
    chunkMainParameters.processUnProcessedChunks = kProcessUnProcessedChunks;
    chunkMainParameters.startChunkKey = 1;
    chunkMainParameters.unProcessedChunkReadWait = kUnProcessedChunkReadWait;

    if (StringUtils.isEmpty(key.instanceID)) {
      key.instanceID =
        BATCHPROCESSNAME.BDM_GENERATE_PAYMENT_STAGING_DATA_BATCH;
    }
    if (key.processingDate.isZero()) {
      key.processingDate = Date.getCurrentDate();
    }
    // Task 42332: create filename SPS1.xxxx.<CDO Abbreviation>.<PayRun
    // Type>.X1402.Y<yyyy>.M<mm>.D<dd>
    final BDMBatchUtil util = new BDMBatchUtil();
    String currentSchema = "OTST";
    if (util.getSchema().equalsIgnoreCase(
      Configuration.getProperty(EnvVars.BDM_PROD_SCHEMA_NAME))) {
      currentSchema = "PROD";
    }
    key.runID = UniqueIDFactory.newInstance().getNextID();
    final DateTime datetime = DateTime.getCurrentDateTime();
    final String currentDateTime = util.getFormattedDateTime();
    key.filename = "SPS1." + currentSchema + ".FR.ARR.X1402.Y"
      + currentDateTime.substring(0, 4) + ".M"
      + currentDateTime.substring(4, 6) + ".D"
      + currentDateTime.substring(6, 8);
    util.insertBatchHistory(datetime, key.instanceID, key.runID);

    final BDMGeneratePaymentStagingDataBatchStreamWrapper streamWrapper =
      new BDMGeneratePaymentStagingDataBatchStreamWrapper(streamObj, key);

    // purge table before run if table is not empty
    final BDMPaymentStagingData stagingTable =
      BDMPaymentStagingDataFactory.newInstance();

    final BDMPaymentStagingDataDtlsList tableData =
      stagingTable.nkreadmulti();

    if (!tableData.dtls.isEmpty()) {
      stagingTable.nkremove();
    }

    if (BDMILIStageDataFactory.newInstance().nkreadmulti().dtls.size() > 0) {
      BDMILIStageDataFactory.newInstance().nkremove();
    }

    batchStreamHelper.runChunkMain(key.instanceID, null, chunkerWrapper,
      batchProcessingIDList, chunkMainParameters, streamWrapper);
  }

  /**
   * Searches all Participant that have a
   * reconcilStatusCode of PAYMENT_DUE in BDMPaymentInstrument
   *
   * @return the list of payment instruments added to a batch list
   * @throws AppException
   * @throws InformationalException
   */

  protected BatchProcessingIDList getRecordsToProcess()
    throws AppException, InformationalException {

    final BatchProcessingIDList idList = new BatchProcessingIDList();

    final BDMPaymentInstrument bdmPmtInstrObj =
      BDMPaymentInstrumentFactory.newInstance();

    curam.ca.gc.bdm.entity.financial.struct.BDMParticipantRoleIDList participantList =
      new curam.ca.gc.bdm.entity.financial.struct.BDMParticipantRoleIDList();
    final BDMSearchParticipantKey searchKey = new BDMSearchParticipantKey();
    searchKey.reconcilStatusCode = PMTRECONCILIATIONSTATUS.PROCESSED;
    searchKey.bdmReconcilStatusCode = BDMPMTRECONCILIATIONSTATUS.TRANSFERRED;
    participantList = bdmPmtInstrObj.searchParticipantIDByStatus(searchKey);

    BatchProcessingID batchProcessingID;

    for (final BDMParticipantRoleID processChunkDtls : participantList.dtls) {

      batchProcessingID = new BatchProcessingID();
      batchProcessingID.recordID = processChunkDtls.participantRoleID;
      idList.dtls.add(batchProcessingID);
    }

    return idList;
  }

  private int getSeqNumber() throws AppException, InformationalException {

    final BDMPaymentUtil pmtUtilObj = new BDMPaymentUtil();

    final int seqNo = pmtUtilObj.getSeqNumber(BDMConstants.kPAYMENT);
    return seqNo;
  }

  @Override
  public void sendBatchReport(final String instanceID,
    final BatchProcessDtls batchProcessDtls,
    final BatchProcessChunkDtlsList processedBatchProcessChunkDtlsList,
    final BatchProcessChunkDtlsList unprocessedBatchProcessChunkDtlsList)
    throws AppException, InformationalException {

    long totalNumberOfCasesProcessed = 0;
    long totalNumberOfCasesSkipped = 0;
    final long totalNumberOfUnprocessedChunks =
      unprocessedBatchProcessChunkDtlsList.dtls.size();

    // curamBatch manipulation variable
    final CuramBatch curamBatchObj = new CuramBatch();

    // the StringBuffer size is based on the data being written to the buffer
    final StringBuffer emailMessage = new StringBuffer(512);
    StringList resultsList = null;

    // For each processed chunk, gather the stats
    for (final BatchProcessChunkDtls processedBatchProcessChunkDtls : processedBatchProcessChunkDtlsList.dtls) {
      resultsList = StringUtil
        .tabText2StringList(processedBatchProcessChunkDtls.resultSummary);
      if (resultsList.size() > 0) {
        totalNumberOfCasesProcessed += Integer.parseInt(resultsList.item(0));
        totalNumberOfCasesSkipped += Integer.parseInt(resultsList.item(1));
      }
    }

    if (totalNumberOfUnprocessedChunks > 0) {

      final AppException errChunksSkippedText =
        new AppException(BDMGENERATEPAYMENTSTAGINGDATA.ERR_CHUNKS_SKIPPED);

      errChunksSkippedText.arg(totalNumberOfUnprocessedChunks);
      errChunksSkippedText.arg(totalNumberOfUnprocessedChunks * kChunkSize);

      // append to StringBuffer
      emailMessage.append(CuramConst.gkNewLine)
        .append(errChunksSkippedText
          .getMessage(ProgramLocale.getDefaultServerLocale()))
        .append(CuramConst.gkNewLine);
    }

    // Create the text strings to send to the email report
    final AppException infTotalCasesText = new AppException(
      BDMGENERATEPAYMENTSTAGINGDATA.INF_CASE_RECORDS_PROCESSED);

    infTotalCasesText.arg(totalNumberOfCasesProcessed);

    // append to StringBuffer
    emailMessage.append(CuramConst.gkNewLine)
      .append(
        infTotalCasesText.getMessage(ProgramLocale.getDefaultServerLocale()))
      .append(CuramConst.gkNewLine);

    if (totalNumberOfCasesSkipped > 0) {

      final AppException infTotalSkippedCasesText = new AppException(
        BDMGENERATEPAYMENTSTAGINGDATA.INF_CASE_RECORDS_SKIPPED);

      infTotalSkippedCasesText.arg(totalNumberOfCasesSkipped);

      // append to StringBuffer
      emailMessage.append(CuramConst.gkNewLine)
        .append(infTotalSkippedCasesText
          .getMessage(ProgramLocale.getDefaultServerLocale()))
        .append(CuramConst.gkNewLine);
    }

    curamBatchObj.emailMessage = emailMessage.toString();

    // constructing the Email Subject
    curamBatchObj.setEmailSubject(
      BDMGENERATEPAYMENTSTAGINGDATA.INF_BDM_GENERATE_PAYMENT_SUBJECT);

    // set output file id
    curamBatchObj.outputFileID =
      BDMGENERATEPAYMENTSTAGINGDATA.INF_BDM_GENERATE_PAYMENT
        .getMessageText(ProgramLocale.getDefaultServerLocale());

    // set the elapsed time
    curamBatchObj.setStartTime(batchProcessDtls.startDateTime);
    curamBatchObj.setEndTime();

    // send email
    curamBatchObj.sendEmail();
  }

  /**
   * Gets an integer property setting, with a nominated default.
   *
   * @param propName The name of the property.
   * @param defaultValue The default value of the property.
   *
   * @return The specified or default value for the property.
   */
  private static int getIntProperty(final String propName,
    final int defaultValue) {

    final int result;
    final Integer val1 = Configuration.getIntProperty(propName);

    if (val1 != null && val1.intValue() != 0) {
      result = val1;
    } else {
      result = defaultValue;
    }
    return result;
  }

}
