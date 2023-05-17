package curam.ca.gc.bdm.batch.bdmgenerateamendtaxslipdatabatch.impl;

import com.ibm.icu.util.Calendar;
import curam.ca.gc.bdm.batch.bdmgeneratetaxslipdatabatch.struct.BDMGenerateTaxSlipDataBatchKey;
import curam.ca.gc.bdm.batch.bdmgeneratetaxslipdatabatch.struct.BDMGenerateTaxSlipDataChunkResult;
import curam.ca.gc.bdm.codetable.BDMPMTRECONCILIATIONSTATUS;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.fact.BDMPaymentInstrumentDAFactory;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMSearchAmendTaxSlipEligClientsKey;
import curam.ca.gc.bdm.message.BDMGENERATETAXSLIPBATCH;
import curam.codetable.BATCHPROCESSNAME;
import curam.core.impl.BatchStreamHelper;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
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

public class BDMGenerateAmendTaxSlipDataBatch extends
  curam.ca.gc.bdm.batch.bdmgenerateamendtaxslipdatabatch.base.BDMGenerateAmendTaxSlipDataBatch {

  private BatchStreamHelper batchStreamHelper;

  @Override
  public void process(final BDMGenerateTaxSlipDataBatchKey key)
    throws AppException, InformationalException {

    batchStreamHelper = new BatchStreamHelper();

    if (key.taxYear == 0) {
      key.taxYear =
        Date.getCurrentDate().getCalendar().get(Calendar.YEAR) - 1;
    }

    final BDMGenerateAmendTaxSlipDataBatchWrapper chunkWrapper =
      new BDMGenerateAmendTaxSlipDataBatchWrapper(this);

    final BDMGenerateAmendTaxSlipDataBatchStreamWrapper streamWrapper =
      new BDMGenerateAmendTaxSlipDataBatchStreamWrapper(
        new BDMGenerateAmendTaxSlipDataBatchStream(), key);

    final ChunkMainParameters chunkMainParameters = getChunkMainParameters();

    final BatchProcessingIDList batchProcessingIDList =
      getBatchProcessingIDs(key.taxYear);

    batchStreamHelper.runChunkMain(
      BATCHPROCESSNAME.BDM_GENERATE_AMEND_TAX_SLIP_DATA_BATCH, key,
      chunkWrapper, batchProcessingIDList, chunkMainParameters,
      streamWrapper);

  }

  /**
   * Get the concern role IDs of participants with cancelled or reissued
   * payments that are tagged to be added to pay slips
   *
   * @param taxYear
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private BatchProcessingIDList getBatchProcessingIDs(final int taxYear)
    throws AppException, InformationalException {

    final BatchProcessingIDList batchProcessingIDList =
      new BatchProcessingIDList();

    final BDMSearchAmendTaxSlipEligClientsKey key =
      new BDMSearchAmendTaxSlipEligClientsKey();
    key.addToTaxSlipInd = true;
    key.reconcilStatusCancel = BDMPMTRECONCILIATIONSTATUS.CANCELED;
    key.reconcilStatusIssued = BDMPMTRECONCILIATIONSTATUS.ISSUED;
    final ConcernRoleKeyList eligibleClients = BDMPaymentInstrumentDAFactory
      .newInstance().searchAmendTaxSlipEligClients(key);

    for (final ConcernRoleKey client : eligibleClients.dtls) {
      final BatchProcessingID batchProcessingID = new BatchProcessingID();
      batchProcessingID.recordID = client.concernRoleID;
      batchProcessingIDList.dtls.add(batchProcessingID);
    }
    return batchProcessingIDList;
  }

  private ChunkMainParameters getChunkMainParameters() {

    final ChunkMainParameters chunkMainParameters = new ChunkMainParameters();

    chunkMainParameters.chunkSize = Configuration.getIntProperty(
      EnvVars.BDM_CURAM_GENERATE_AMEND_TAX_SLIPS_DATA_BATCH_CHUNK_SIZE);
    chunkMainParameters.dontRunStream = Configuration.getBooleanProperty(
      EnvVars.BDM_CURAM_GENERATE_AMEND_TAX_SLIPS_DATA_BATCH_DONT_RUN_STREAM);
    chunkMainParameters.startChunkKey =
      Long.parseLong(Configuration.getProperty(
        EnvVars.BDM_CURAM_GENERATE_AMEND_TAX_SLIPS_DATA_BATCH_START_CHUNK_KEY));
    chunkMainParameters.unProcessedChunkReadWait =
      Configuration.getIntProperty(
        EnvVars.BDM_CURAM_GENERATE_AMEND_TAX_SLIPS_DATA_BATCH_UNPROCESSED_CHUNK_READ_WAIT);
    chunkMainParameters.processUnProcessedChunks =
      Configuration.getBooleanProperty(
        EnvVars.BDM_CURAM_GENERATE_AMEND_TAX_SLIPS_DATA_BATCH_PROCESS_UNPROCESSED_CHUNKS);

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

      final BDMGenerateTaxSlipDataChunkResult decodedResultList =
        decodeProcessChunkResult(chunkDtls.resultSummary);

      totalProcessedCount += decodedResultList.totalCasesProcessedCount;
      totalSkippedCount += decodedResultList.failureCasesProcessedCount;
    }

    if (totalNumberOfUnprocessedChunks > 0) {

      final AppException errChunksSkippedText =
        new AppException(BDMGENERATETAXSLIPBATCH.ERR_CHUNKS_SKIPPED);

      errChunksSkippedText.arg(totalNumberOfUnprocessedChunks);
      errChunksSkippedText
        .arg(totalNumberOfUnprocessedChunks * Configuration.getIntProperty(
          EnvVars.BDM_CURAM_GENERATE_AMEND_TAX_SLIPS_DATA_BATCH_CHUNK_SIZE));

      // append to StringBuffer
      emailMessage.append(CuramConst.gkNewLine)
        .append(errChunksSkippedText
          .getMessage(ProgramLocale.getDefaultServerLocale()))
        .append(CuramConst.gkNewLine);
    }

    // Create the text strings to send to the email report
    final AppException infTotalCasesText =
      new AppException(BDMGENERATETAXSLIPBATCH.INF_RECORDS_PROCESSED);

    infTotalCasesText.arg(totalProcessedCount);

    // append to StringBuffer
    emailMessage.append(CuramConst.gkNewLine)
      .append(
        infTotalCasesText.getMessage(ProgramLocale.getDefaultServerLocale()))
      .append(CuramConst.gkNewLine);

    if (totalSkippedCount > 0) {

      final AppException infTotalSkippedCasesText =
        new AppException(BDMGENERATETAXSLIPBATCH.INF_RECORDS_SKIPPED);

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
      BDMGENERATETAXSLIPBATCH.INF_GEN_AMENDED_TAX_SLIP_BATCH_SUB);

    // set output file id
    curamBatchObj.outputFileID =
      BDMGENERATETAXSLIPBATCH.INF_GEN_AMENDED_TAX_SLIP
        .getMessageText(ProgramLocale.getDefaultServerLocale());

    // set the elapsed time
    curamBatchObj.setStartTime(batchProcessDtls.startDateTime);
    curamBatchObj.setEndTime();

    // send email
    curamBatchObj.sendEmail();

  }

  @Override
  public BDMGenerateTaxSlipDataChunkResult decodeProcessChunkResult(
    final String resultString) throws AppException, InformationalException {

    final BDMGenerateTaxSlipDataChunkResult chunkResult =
      new BDMGenerateTaxSlipDataChunkResult();
    final StringList resultList = StringUtil.tabText2StringList(resultString);
    chunkResult.totalCasesProcessedCount = Integer.valueOf(resultList.get(0));
    chunkResult.failureCasesProcessedCount =
      Integer.valueOf(resultList.get(1));
    return chunkResult;
  }

}
