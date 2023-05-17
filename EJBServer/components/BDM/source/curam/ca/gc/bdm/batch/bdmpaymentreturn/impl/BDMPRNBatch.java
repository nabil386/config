package curam.ca.gc.bdm.batch.bdmpaymentreturn.impl;

import curam.ca.gc.bdm.batch.bdmgeneratepaymentstagingdatabatch.struct.GeneratePaymentsFileKey;
import curam.ca.gc.bdm.batch.bdmpaymentreturn.fact.BDMPRNBatchStreamFactory;
import curam.ca.gc.bdm.batch.bdmpaymentreturn.intf.BDMPRNBatchStream;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.fact.BDMPRNStagingDataFactory;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.intf.BDMPRNStagingData;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMPRNStagingDataDtlsStruct3;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMPRNStagingDataDtlsStruct3List;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMPRNStagingDataKeyStruct3;
import curam.ca.gc.bdm.message.BDMBPOPROCESSPAYMENTRESPONSEBATCH;
import curam.codetable.BATCHPROCESSNAME;
import curam.codetable.BATCHPROCESSRESULTSTATUS;
import curam.core.impl.BatchStreamHelper;
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
import curam.util.type.StringList;
import org.apache.commons.lang3.StringUtils;

public class BDMPRNBatch
  extends curam.ca.gc.bdm.batch.bdmpaymentreturn.base.BDMPRNBatch {

  /*
   * @author Chandan
   * Task-13471
   * Process the Payment return data which is coming from ADF Pipeline and
   * update the payment record of Curam
   */

  /**
   * The number of records per chunk.
   */
  private static final int kChunkSize =
    getIntProperty(EnvVars.BDM_ENV_BDMPROCESSPAYMENTRESPONSEBATCH_CHUNK_SIZE,
      EnvVars.BDM_ENV_BDMPROCESSPAYMENTRESPONSEBATCH_CHUNK_SIZE_DEFAULT);

  /**
   * Controls whether the chunker should start a stream.
   */
  private static final boolean kDontRunStream =
    Configuration.getBooleanProperty(
      EnvVars.BDM_ENV_BDMPROCESSPAYMENTRESPONSEBATCH_DONTRUNSTREAM,
      EnvVars.BDM_ENV_BDMPROCESSPAYMENTRESPONSEBATCH_DONTRUNSTREAM_DEFAULT);

  /**
   * Wait time for unprocessed chunks.
   */
  private static final int kUnProcessedChunkReadWait = getIntProperty(
    EnvVars.BDM_ENV_BDMPROCESSPAYMENTRESPONSEBATCH_UNPROCESSED_CHUNK_WAIT_INTERVAL,
    EnvVars.BDM_ENV_BDMPROCESSPAYMENTRESPONSEBATCH_UNPROCESSED_CHUNK_WAIT_INTERVAL_DEFAULT);

  private static final boolean kProcessUnProcessedChunks =
    Configuration.getBooleanProperty(
      EnvVars.BDM_ENV_BDMPROCESSPAYMENTRESPONSEBATCH_PROCESS_UNPROCESSED_CHUNKS,
      false);

  @Override
  public void process(final GeneratePaymentsFileKey key)
    throws AppException, InformationalException {

    final ChunkMainParameters chunkMainParameters = new ChunkMainParameters();

    final BDMPRNBatchStream streamObj =
      BDMPRNBatchStreamFactory.newInstance();

    final BDMPRNBatchStreamWrapper streamWrapper =
      new BDMPRNBatchStreamWrapper(streamObj);

    final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();

    final BDMPRNBatchWrapper chunkerWrapper = new BDMPRNBatchWrapper(this);

    // Retrieve the records that need to be processed
    final BatchProcessingIDList batchProcessingIDList = getRecordsToProcess();

    chunkMainParameters.chunkSize = kChunkSize;
    chunkMainParameters.dontRunStream = kDontRunStream;
    chunkMainParameters.processUnProcessedChunks = kProcessUnProcessedChunks;
    chunkMainParameters.startChunkKey = 1;
    chunkMainParameters.unProcessedChunkReadWait = kUnProcessedChunkReadWait;

    final String instanceID;
    if (!StringUtils.isEmpty(key.instanceID)) {
      instanceID = key.instanceID;
    } else {
      instanceID = BATCHPROCESSNAME.BDM_PAYMENT_RETURN_BATCH;
    }
    streamWrapper.setInstanceID(instanceID);

    batchStreamHelper.runChunkMain(instanceID, null, chunkerWrapper,
      batchProcessingIDList, chunkMainParameters, streamWrapper);

  }

  @Override
  public void sendBatchReport(final String instanceID,
    final BatchProcessDtls batchProcessDtls,
    final BatchProcessChunkDtlsList processedBatchProcessChunkDtlsList,
    final BatchProcessChunkDtlsList unprocessedBatchProcessChunkDtlsList)
    throws AppException, InformationalException {

    long totalNumberOfPmtInstrumentsProcessed = 0;
    long totalNumberOfPmtInstrumentsSkipped = 0;
    final long totalNumberOfUnprocessedChunks =
      unprocessedBatchProcessChunkDtlsList.dtls.size();

    // curamBatch manipulation variable
    final curam.core.impl.CuramBatch curamBatchObj =
      new curam.core.impl.CuramBatch();

    // the StringBuffer size is based on the data being written to
    // to the buffer
    final StringBuffer emailMessage = new StringBuffer(512);
    StringList resultsList = null;

    // For each processed chunk, gather the stats
    for (final BatchProcessChunkDtls processedBatchProcessChunkDtls : processedBatchProcessChunkDtlsList.dtls) {
      resultsList = StringUtil
        .tabText2StringList(processedBatchProcessChunkDtls.resultSummary);
      totalNumberOfPmtInstrumentsProcessed +=
        Integer.parseInt(resultsList.item(0));
      totalNumberOfPmtInstrumentsSkipped +=
        Integer.parseInt(resultsList.item(1));
    }

    if (totalNumberOfUnprocessedChunks > 0) {

      final AppException errChunksSkippedText = new AppException(
        BDMBPOPROCESSPAYMENTRESPONSEBATCH.ERR_CHUNKS_SKIPPED);

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
      BDMBPOPROCESSPAYMENTRESPONSEBATCH.INF_PAYMENT_INSTRUMENTS_PROCESSED);

    infTotalCasesText.arg(totalNumberOfPmtInstrumentsProcessed);

    // append to StringBuffer
    emailMessage.append(CuramConst.gkNewLine)
      .append(
        infTotalCasesText.getMessage(ProgramLocale.getDefaultServerLocale()))
      .append(CuramConst.gkNewLine);

    if (totalNumberOfPmtInstrumentsSkipped > 0) {

      final AppException infTotalSkippedCasesText = new AppException(
        BDMBPOPROCESSPAYMENTRESPONSEBATCH.INF_PAYMENT_INSTRUMENTS_SKIPPED);

      infTotalSkippedCasesText.arg(totalNumberOfPmtInstrumentsSkipped);

      // append to StringBuffer
      emailMessage.append(CuramConst.gkNewLine)
        .append(infTotalSkippedCasesText
          .getMessage(ProgramLocale.getDefaultServerLocale()))
        .append(CuramConst.gkNewLine);
    }

    curamBatchObj.emailMessage = emailMessage.toString();

    // constructing the Email Subject
    curamBatchObj.setEmailSubject(
      BDMBPOPROCESSPAYMENTRESPONSEBATCH.INF_PROCESS_PAYMENT_RESPONSE_SUB);

    // set output file id
    curamBatchObj.outputFileID =
      BDMBPOPROCESSPAYMENTRESPONSEBATCH.INF_PROCESS_PAYMENT_RESPONSE
        .getMessageText(ProgramLocale.getDefaultServerLocale());

    // set the elapsed time
    curamBatchObj.setStartTime(batchProcessDtls.startDateTime);
    curamBatchObj.setEndTime();

    // send email
    curamBatchObj.sendEmail();

  }

  private BatchProcessingIDList getRecordsToProcess()
    throws AppException, InformationalException {

    final BatchProcessingIDList idList = new BatchProcessingIDList();

    final BDMPRNStagingData bdmprnStagingData =
      BDMPRNStagingDataFactory.newInstance();

    final BDMPRNStagingDataKeyStruct3 key = new BDMPRNStagingDataKeyStruct3();

    key.processingStatus = BATCHPROCESSRESULTSTATUS.NOT_PROCESSED;
    final BDMPRNStagingDataDtlsStruct3List list =
      bdmprnStagingData.readByProcessingStatus(key);

    BatchProcessingID batchProcessingID;

    for (final BDMPRNStagingDataDtlsStruct3 processChunkDtls : list.dtls) {
      batchProcessingID = new BatchProcessingID();
      batchProcessingID.recordID = processChunkDtls.spsPmtGroupID;
      idList.dtls.add(batchProcessingID);
    }

    return idList;
  }

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
