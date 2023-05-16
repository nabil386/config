package curam.ca.gc.bdm.batch.bdmcancelpaymentbatch.impl;

import curam.ca.gc.bdm.batch.bdmcancelpaymentbatch.fact.BDMCancelPaymentBatchStreamFactory;
import curam.ca.gc.bdm.batch.bdmgeneratepaymentstagingdatabatch.struct.GeneratePaymentsFileKey;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.fact.BDMCancelPaymentStageFactory;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.intf.BDMCancelPaymentStage;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMCancelPaymentStageDtls;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMCancelPaymentStageDtlsList;
import curam.codetable.BATCHPROCESSNAME;
import curam.core.impl.BatchStreamHelper;
import curam.core.impl.EnvVars;
import curam.core.struct.BatchProcessChunkDtlsList;
import curam.core.struct.BatchProcessDtls;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingIDList;
import curam.core.struct.ChunkMainParameters;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.type.NotFoundIndicator;
import org.apache.commons.lang.StringUtils;

public class BDMCancelPaymentBatch extends
  curam.ca.gc.bdm.batch.bdmcancelpaymentbatch.base.BDMCancelPaymentBatch {

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

  @Override
  public void process(final GeneratePaymentsFileKey key)
    throws AppException, InformationalException {

    final ChunkMainParameters chunkMainParameters = new ChunkMainParameters();

    final curam.ca.gc.bdm.batch.bdmcancelpaymentbatch.intf.BDMCancelPaymentBatchStream streamObj =
      BDMCancelPaymentBatchStreamFactory.newInstance();

    final BDMCancelPaymentBatchStreamWrapper streamWrapper =
      new BDMCancelPaymentBatchStreamWrapper(streamObj);

    final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();

    final BDMCancelPaymentBatchWrapper chunkerWrapper =
      new BDMCancelPaymentBatchWrapper(this);

    // Retrieve the records that need to be processed
    final BatchProcessingIDList batchProcessingIDList = getRecordsToProcess();

    final NotFoundIndicator nfIndicator = new NotFoundIndicator();

    chunkMainParameters.chunkSize = kChunkSize;
    chunkMainParameters.dontRunStream = kDontRunStream;
    chunkMainParameters.processUnProcessedChunks = kProcessUnProcessedChunks;
    chunkMainParameters.startChunkKey = 1;
    chunkMainParameters.unProcessedChunkReadWait = kUnProcessedChunkReadWait;

    final String instanceID;
    if (!StringUtils.isEmpty(key.instanceID)) {
      instanceID = key.instanceID;
    } else {
      instanceID = BATCHPROCESSNAME.BDM_CANCEL_PAYMENT_BATCH;
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

    // TODO Auto-generated method stub

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

  /**
   * Searches all CancelPaymentStage records that have a
   * reconcilStatusCode of PRO
   *
   * @return the list of payment instruments added to a batch list
   * @throws AppException
   * @throws InformationalException
   */
  private BatchProcessingIDList getRecordsToProcess()
    throws AppException, InformationalException {

    final BatchProcessingIDList idList = new BatchProcessingIDList();

    final BDMCancelPaymentStage cancelStagingDataObj =
      BDMCancelPaymentStageFactory.newInstance();
    BDMCancelPaymentStageDtlsList cancelPaymentDtlsList =
      new BDMCancelPaymentStageDtlsList();

    cancelPaymentDtlsList = cancelStagingDataObj.nkreadmulti();
    BatchProcessingID batchProcessingID;

    for (final BDMCancelPaymentStageDtls processChunkDtls : cancelPaymentDtlsList.dtls) {
      batchProcessingID = new BatchProcessingID();
      batchProcessingID.recordID = processChunkDtls.recordID;
      idList.dtls.add(batchProcessingID);
    }

    return idList;
  }

}
