package curam.ca.gc.bdm.batch.bdms34stagingdatabatch.impl;

import curam.ca.gc.bdm.batch.bdmbatchutil.impl.BDMBatchUtil;
import curam.ca.gc.bdm.batch.bdmgeneratepaymentstagingdatabatch.struct.GeneratePaymentsFileKey;
import curam.ca.gc.bdm.batch.bdms34stagingdatabatch.fact.BDMS34StagingDataBatchStreamFactory;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.fact.BDMPaymentStagingDataFactory;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.intf.BDMPaymentStagingData;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMPaymentStagingDataDtls;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMPaymentStagingDataDtlsList;
import curam.ca.gc.bdm.entity.fact.BDMS34StagingDataFactory;
import curam.ca.gc.bdm.entity.intf.BDMS34StagingData;
import curam.codetable.BATCHPROCESSNAME;
import curam.core.fact.UniqueIDFactory;
import curam.core.impl.BatchStreamHelper;
import curam.core.impl.EnvVars;
import curam.core.struct.BatchProcessChunkDtlsList;
import curam.core.struct.BatchProcessDtls;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingIDList;
import curam.core.struct.ChunkMainParameters;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.RecordNotFoundException;
import curam.util.resources.Configuration;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.NotFoundIndicator;
import org.apache.commons.lang.StringUtils;

public class BDMS34StagingDataBatch extends
  curam.ca.gc.bdm.batch.bdms34stagingdatabatch.base.BDMS34StagingDataBatch {

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

    final curam.ca.gc.bdm.batch.bdms34stagingdatabatch.intf.BDMS34StagingDataBatchStream streamObj =
      BDMS34StagingDataBatchStreamFactory.newInstance();

    final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();

    final BDMS34StagingDataBatchWrapper chunkerWrapper =
      new BDMS34StagingDataBatchWrapper(this);

    // Retrieve the records that need to be processed
    final BatchProcessingIDList batchProcessingIDList = getRecordsToProcess();

    final NotFoundIndicator nfIndicator = new NotFoundIndicator();

    chunkMainParameters.chunkSize = kChunkSize;
    chunkMainParameters.dontRunStream = kDontRunStream;
    chunkMainParameters.processUnProcessedChunks = kProcessUnProcessedChunks;
    chunkMainParameters.startChunkKey = 1;
    chunkMainParameters.unProcessedChunkReadWait = kUnProcessedChunkReadWait;

    if (StringUtils.isEmpty(key.instanceID)) {
      key.instanceID = BATCHPROCESSNAME.BDM_GENERATE_S34_REPORT_DATA_BATCH;
    }
    if (key.processingDate.isZero()) {
      key.processingDate = Date.getCurrentDate();
    }
    
   // Task 42332: REPORT.<Program>P.xxx.<CDO Abbreviation>.<PayRun Type>.X1402.Y<yyyy>.M<mm>.D<dd>.txt
    BDMBatchUtil util = new BDMBatchUtil();
    String currentSchema = "TST";
    if (util.getSchema().equalsIgnoreCase(Configuration.getProperty(EnvVars.BDM_PROD_SCHEMA_NAME))) {
      currentSchema = "PRD";
    }
    key.runID = UniqueIDFactory.newInstance().getNextID();
    DateTime datetime = DateTime.getCurrentDateTime();
    String currentDateTime = util.getFormattedDateTime();
    key.filename = "REPORT.BDMP." + currentSchema + ".FR.ARR.X1402.Y" + currentDateTime.substring(0, 4) + ".M" + currentDateTime.substring(4, 6) + ".D" + currentDateTime.substring(6, 8);
    util.insertBatchHistory(datetime, key.instanceID, key.runID);
    
    final BDMS34StagingDataBatchStreamWrapper streamWrapper =
      new BDMS34StagingDataBatchStreamWrapper(streamObj, key);

    final BDMS34StagingData stagingTable =
      BDMS34StagingDataFactory.newInstance();
    try {
      stagingTable.nkremove();
    } catch (final RecordNotFoundException e) {

    }

    batchStreamHelper.runChunkMain(key.instanceID, null, chunkerWrapper,
      batchProcessingIDList, chunkMainParameters, streamWrapper);

  }

  /**
   * Searches all PaymentInstruments that have a
   * reconcilStatusCode of PRO
   *
   * @return the list of payment instruments added to a batch list
   * @throws AppException
   * @throws InformationalException
   */
  private BatchProcessingIDList getRecordsToProcess()
    throws AppException, InformationalException {

    final BatchProcessingIDList idList = new BatchProcessingIDList();

    final BDMPaymentStagingData pmtStagingDataObj =
      BDMPaymentStagingDataFactory.newInstance();
    BDMPaymentStagingDataDtlsList pmtStagingDataDtlsList =
      new BDMPaymentStagingDataDtlsList();

    pmtStagingDataDtlsList = pmtStagingDataObj.nkreadmulti();
    BatchProcessingID batchProcessingID;

    for (final BDMPaymentStagingDataDtls processChunkDtls : pmtStagingDataDtlsList.dtls) {
      batchProcessingID = new BatchProcessingID();
      batchProcessingID.recordID = processChunkDtls.recordID;
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

  @Override
  public void sendBatchReport(final String instanceID,
    final BatchProcessDtls batchProcessDtls,
    final BatchProcessChunkDtlsList processedBatchProcessChunkDtlsList,
    final BatchProcessChunkDtlsList unprocessedBatchProcessChunkDtlsList)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub

  }

}
