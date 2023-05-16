package curam.ca.gc.bdm.batch.bdmpaymentdestinationbatch.impl;

import curam.ca.gc.bdm.batch.bdmpaymentdestinationbatch.struct.BDMPaymentDestinationChunkResult;
import curam.ca.gc.bdm.entity.financial.fact.BDMPaymentDestinationFactory;
import curam.ca.gc.bdm.entity.financial.struct.BDMConcernRolePendingSyncKey;
import curam.ca.gc.bdm.message.BDMPAYMENTDESTINATION;
import curam.codetable.BATCHPROCESSNAME;
import curam.codetable.RECORDSTATUS;
import curam.core.impl.BatchStreamHelper;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.struct.BatchProcessByInstanceIDKey;
import curam.core.struct.BatchProcessChunkDtls;
import curam.core.struct.BatchProcessChunkDtlsList;
import curam.core.struct.BatchProcessDtls;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingIDList;
import curam.core.struct.ChunkMainParameters;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.ConcernRoleKeyList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.ProgramLocale;
import curam.util.resources.StringUtil;
import curam.util.type.Date;
import curam.util.type.StringList;

public class BDMPaymentDestinationBatch extends
  curam.ca.gc.bdm.batch.bdmpaymentdestinationbatch.base.BDMPaymentDestinationBatch {

  private BatchStreamHelper batchStreamHelper;

  @Override
  public void process(final BatchProcessByInstanceIDKey batchProcessKey)
    throws AppException, InformationalException {

    batchStreamHelper = new BatchStreamHelper();

    if (StringUtil.isNullOrEmpty(batchProcessKey.instanceID)) {
      batchProcessKey.instanceID =
        BATCHPROCESSNAME.BDM_PAYMENT_DESTINATION_BATCH;
    }

    final BDMPaymentDestinationBatchWrapper chunkWrapper =
      new BDMPaymentDestinationBatchWrapper(this);

    final BDMPaymentDestinationBatchStreamWrapper streamWrapper =
      new BDMPaymentDestinationBatchStreamWrapper(
        new BDMPaymentDestinationBatchStream());

    final ChunkMainParameters chunkMainParameters = getChunkMainParameters();

    final BatchProcessingIDList batchProcessingIDList =
      getBatchProcessingIDs();

    batchStreamHelper.runChunkMain(batchProcessKey.instanceID,
      batchProcessKey, chunkWrapper, batchProcessingIDList,
      chunkMainParameters, streamWrapper);

  }

  /**
   * Get the concern role IDs to be processed
   *
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private BatchProcessingIDList getBatchProcessingIDs()
    throws AppException, InformationalException {

    final BatchProcessingIDList batchProcessingIDList =
      new BatchProcessingIDList();

    final BDMConcernRolePendingSyncKey key =
      new BDMConcernRolePendingSyncKey();
    key.recordStatusCode = RECORDSTATUS.NORMAL;
    key.syncDestinationInd = true;
    key.currentDate = Date.getCurrentDate();
    // find concern roles
    final ConcernRoleKeyList concernRoleList = BDMPaymentDestinationFactory
      .newInstance().searchConcernRolePendingSync(key);

    // add case nominees to be processed
    for (final ConcernRoleKey concernRole : concernRoleList.dtls) {

      final BatchProcessingID batchProcessingID = new BatchProcessingID();
      batchProcessingID.recordID = concernRole.concernRoleID;

      batchProcessingIDList.dtls.add(batchProcessingID);

    }

    return batchProcessingIDList;
  }

  private ChunkMainParameters getChunkMainParameters() {

    final ChunkMainParameters chunkMainParameters = new ChunkMainParameters();

    chunkMainParameters.chunkSize = Configuration
      .getIntProperty(EnvVars.BDM_CURAM_PAYMENT_DESTINATION_BATCH_CHUNK_SIZE);
    chunkMainParameters.dontRunStream = Configuration.getBooleanProperty(
      EnvVars.BDM_CURAM_PAYMENT_DESTINATION_BATCH_DONT_RUN_STREAM);
    chunkMainParameters.startChunkKey =
      Long.parseLong(Configuration.getProperty(
        EnvVars.BDM_CURAM_PAYMENT_DESTINATION_BATCH_START_CHUNK_KEY));
    chunkMainParameters.unProcessedChunkReadWait =
      Configuration.getIntProperty(
        EnvVars.BDM_CURAM_PAYMENT_DESTINATION_BATCH_UNPROCESSED_CHUNK_READ_WAIT);
    chunkMainParameters.processUnProcessedChunks =
      Configuration.getBooleanProperty(
        EnvVars.BDM_CURAM_PAYMENT_DESTINATION_BATCH_PROCESS_UNPROCESSED_CHUNKS);

    return chunkMainParameters;
  }

  @Override
  public void sendBatchReport(final String instanceID,
    final BatchProcessDtls batchProcessDtls,
    final BatchProcessChunkDtlsList processedBatchProcessChunkDtlsList,
    final BatchProcessChunkDtlsList unprocessedBatchProcessChunkDtlsList)
    throws AppException, InformationalException {

    int totalProcessedCount = 0;
    int totalSkippedCount = 0;
    final long totalNumberOfUnprocessedChunks =
      unprocessedBatchProcessChunkDtlsList.dtls.size();

    // curamBatch manipulation variable
    final curam.core.impl.CuramBatch curamBatchObj =
      new curam.core.impl.CuramBatch();

    // the StringBuffer size is based on the data being written to
    // to the buffer
    final StringBuffer emailMessage = new StringBuffer(512);
    for (final BatchProcessChunkDtls chunkDtls : processedBatchProcessChunkDtlsList.dtls) {

      final BDMPaymentDestinationChunkResult decodedResultList =
        decodeProcessChunkResult(chunkDtls.resultSummary);

      totalProcessedCount += decodedResultList.totalProcessedCount;
      totalSkippedCount += decodedResultList.totalSkippedCount;
    }

    if (totalNumberOfUnprocessedChunks > 0) {

      final AppException errChunksSkippedText =
        new AppException(BDMPAYMENTDESTINATION.ERR_CHUNKS_SKIPPED);

      errChunksSkippedText.arg(totalNumberOfUnprocessedChunks);
      errChunksSkippedText
        .arg(totalNumberOfUnprocessedChunks * Configuration.getIntProperty(
          EnvVars.BDM_CURAM_PAYMENT_DESTINATION_BATCH_CHUNK_SIZE));

      // append to StringBuffer
      emailMessage.append(CuramConst.gkNewLine)
        .append(errChunksSkippedText
          .getMessage(ProgramLocale.getDefaultServerLocale()))
        .append(CuramConst.gkNewLine);
    }

    // Create the text strings to send to the email report
    final AppException infTotalCasesText =
      new AppException(BDMPAYMENTDESTINATION.INF_RECORDS_PROCESSED);

    infTotalCasesText.arg(totalProcessedCount);

    // append to StringBuffer
    emailMessage.append(CuramConst.gkNewLine)
      .append(
        infTotalCasesText.getMessage(ProgramLocale.getDefaultServerLocale()))
      .append(CuramConst.gkNewLine);

    if (totalSkippedCount > 0) {

      final AppException infTotalSkippedCasesText =
        new AppException(BDMPAYMENTDESTINATION.INF_RECORDS_SKIPPED);

      infTotalSkippedCasesText.arg(totalSkippedCount);

      // append to StringBuffer
      emailMessage.append(CuramConst.gkNewLine)
        .append(infTotalSkippedCasesText
          .getMessage(ProgramLocale.getDefaultServerLocale()))
        .append(CuramConst.gkNewLine);
    }

    curamBatchObj.emailMessage = emailMessage.toString();

    // constructing the Email Subject
    curamBatchObj.setEmailSubject(
      BDMPAYMENTDESTINATION.INF_PAYMENT_DESTINATION_BATCH_SUB);

    // set output file id
    curamBatchObj.outputFileID =
      BDMPAYMENTDESTINATION.INF_PAYMENT_DESTINATION_BATCH
        .getMessageText(ProgramLocale.getDefaultServerLocale());

    // set the elapsed time
    curamBatchObj.setStartTime(batchProcessDtls.startDateTime);
    curamBatchObj.setEndTime();

    // send email
    curamBatchObj.sendEmail();

  }

  @Override
  public BDMPaymentDestinationChunkResult decodeProcessChunkResult(
    final String resultString) throws AppException, InformationalException {

    final BDMPaymentDestinationChunkResult chunkResult =
      new BDMPaymentDestinationChunkResult();
    final StringList resultList = StringUtil.tabText2StringList(resultString);
    chunkResult.totalProcessedCount = Integer.valueOf(resultList.get(0));
    chunkResult.totalSkippedCount = Integer.valueOf(resultList.get(1));
    return chunkResult;
  }

}
