package curam.ca.gc.bdm.batch.bdmprocessnonpayment.impl;

import curam.ca.gc.bdm.batch.bdmpaymentdestinationbatch.struct.BDMPaymentDestinationChunkResult;
import curam.ca.gc.bdm.batch.bdmprocessnonpayment.fact.BDMProcessNonPaymentTypesBatchStreamFactory;
import curam.ca.gc.bdm.batch.bdmprocessnonpayment.intf.BDMProcessNonPaymentTypesBatchStream;
import curam.ca.gc.bdm.batch.bdmprocessnonpayment.struct.BDMProcessNonPaymentTypesBatchKey;
import curam.ca.gc.bdm.codetable.BDMEXTERNALPROCSTATUSTYPE;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMPaymentStagingDataDtlsStruct2;
import curam.ca.gc.bdm.entity.financial.fact.BDMInstructionLineItemFactory;
import curam.ca.gc.bdm.entity.financial.intf.BDMInstructionLineItem;
import curam.ca.gc.bdm.entity.financial.struct.BDMNonPaymentFinIDsToProcessKey;
import curam.ca.gc.bdm.message.BDMPAYMENTDESTINATION;
import curam.codetable.BATCHPROCESSNAME;
import curam.codetable.ILICATEGORY;
import curam.core.impl.BatchStreamHelper;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.struct.BatchProcessChunkDtls;
import curam.core.struct.BatchProcessChunkDtlsList;
import curam.core.struct.BatchProcessDtls;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingIDList;
import curam.core.struct.ChunkMainParameters;
import curam.core.struct.FinInstructionIDList;
import curam.util.dataaccess.CuramValueList;
import curam.util.dataaccess.DynamicDataAccess;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.ProgramLocale;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;

public class BDMProcessNonPaymentTypesBatch extends
  curam.ca.gc.bdm.batch.bdmprocessnonpayment.base.BDMProcessNonPaymentTypesBatch {

  @Override
  public void process(final BDMProcessNonPaymentTypesBatchKey key)
    throws AppException, InformationalException {

    final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();

    final BDMProcessNonPaymentTypesBatchStream batchStream =
      BDMProcessNonPaymentTypesBatchStreamFactory.newInstance();

    if (StringUtil.isNullOrEmpty(key.instanceID)) {
      key.instanceID = BATCHPROCESSNAME.BDM_PROCESS_NON_PAYMENT_TYPES_BATCH;
    }

    final BatchProcessingIDList batchProcessingIDList =
      getBatchProcessingIDs();

    final ChunkMainParameters chunkMainParameters = getChunkMainParameters();

    final BDMProcessNonPaymentTypesBatchWrapper chunkWrapper =
      new BDMProcessNonPaymentTypesBatchWrapper(this);
    key.regionSequenceNumber = getRegionSeqNum();

    final BDMProcessNonPaymentTypesBatchStreamWrapper streamWrapper =
      new BDMProcessNonPaymentTypesBatchStreamWrapper(batchStream, key);

    batchStreamHelper.runChunkMain(key.instanceID, null, chunkWrapper,
      batchProcessingIDList, chunkMainParameters, streamWrapper);

  }

  private int getRegionSeqNum() {

    int reqNumber = 0;

    final String sql =
      "SELECT DISTINCT REGIONSEQUENCENUMBER INTO :regionSequenceNumber FROM BDMPAYMENTSTAGINGDATA";

    try {

      final CuramValueList<BDMPaymentStagingDataDtlsStruct2> regionSeqStruct =
        DynamicDataAccess.executeNsMulti(
          BDMPaymentStagingDataDtlsStruct2.class, null, false, sql);

      if (regionSeqStruct.size() > 0) {
        reqNumber = regionSeqStruct.get(0).regionSequenceNumber;
      } else {
        Trace.kTopLevelLogger
          .error("No Sequence Number found in BDMPAYMENTSTAGINGDATA table");
      }

    } catch (final Exception e) {

      e.printStackTrace();

    }
    return reqNumber;

  }

  private BatchProcessingIDList getBatchProcessingIDs()
    throws AppException, InformationalException {

    final BatchProcessingIDList idList = new BatchProcessingIDList();
    final BDMInstructionLineItem bdmIliObj =
      BDMInstructionLineItemFactory.newInstance();
    final BDMNonPaymentFinIDsToProcessKey finIDsToProcessKey =
      new BDMNonPaymentFinIDsToProcessKey();
    finIDsToProcessKey.instructLineItemCategory =
      ILICATEGORY.PAYMENTINSTRUCTION;
    finIDsToProcessKey.extProcStatusTypeCode =
      BDMEXTERNALPROCSTATUSTYPE.UNPROCESSED;
    // Search all financial instructions for processing

    FinInstructionIDList finInstIDList = new FinInstructionIDList();
    finInstIDList =
      bdmIliObj.searchNonPaymentFinIDsToProcess(finIDsToProcessKey);

    finInstIDList.dtls.forEach(processChunk -> {
      final BatchProcessingID batchProcessingID = new BatchProcessingID();
      batchProcessingID.recordID = processChunk.finInstructionID;
      idList.dtls.add(batchProcessingID);

    });

    return idList;

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
    /*
     * curamBatchObj.setEmailSubject(
     * BDMPAYMENTDESTINATION.INF_PAYMENT_DESTINATION_BATCH_SUB);
     */

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

  private BDMPaymentDestinationChunkResult
    decodeProcessChunkResult(final String resultSummary) {

    // TODO Auto-generated method stub
    return null;
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

}
