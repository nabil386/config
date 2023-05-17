package curam.ca.gc.bdmoas.batch.bdmoasvac.impl;

import curam.ca.gc.bdm.codetable.BDMINTERFACESTATUSCODE;
import curam.ca.gc.bdm.codetable.BDMInterfaceNameCode;
import curam.ca.gc.bdm.entity.bdmfiletofile.fact.BDMInboundFileToFileStagingFactory;
import curam.ca.gc.bdm.entity.bdmfiletofile.intf.BDMInboundFileToFileStaging;
import curam.ca.gc.bdm.entity.bdmfiletofile.struct.BDMInboundFileToFileStagingDtls;
import curam.ca.gc.bdm.entity.bdmfiletofile.struct.BDMInboundFileToFileStagingDtlsList;
import curam.ca.gc.bdm.entity.bdmfiletofile.struct.BDMInboundFileToFileStagingSearchKey;
import curam.ca.gc.bdmoas.batch.bdmoasvac.fact.BDMOASVACProcessBatchStreamFactory;
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

public class BDMOASVACProcessBatch
  implements curam.ca.gc.bdmoas.batch.bdmoasvac.intf.BDMOASVACProcessBatch {

  /**
   * The number of records per chunk.
   */
  private static final int kChunkSize =
    getIntProperty(EnvVars.BDMOAS_VAC_BATCH_CHUNK_SIZE,
      EnvVars.BDMOAS_VAC_BATCH_CHUNK_SIZE_DEFAULT);

  /**
   * Controls whether the chunker should start a stream.
   */
  private static final boolean kDontRunStream =
    Configuration.getBooleanProperty(EnvVars.BDMOAS_VAC_BATCH_DONTRUNSTREAM);

  /**
   * Wait time for unprocessed chunks.
   */
  private static final int kUnProcessedChunkReadWait =
    getIntProperty(EnvVars.BDMOAS_VAC_BATCH_UNPROCESSED_CHUNK_WAIT_INTERVAL,
      EnvVars.BDMOAS_VAC_BATCH_UNPROCESSED_CHUNK_WAIT_INTERVAL_DEFAULT);

  /**
   * Indicates whether to process unprocessed chunks at the end.
   */
  private static final boolean kProcessUnProcessedChunks =
    Configuration.getBooleanProperty(
      EnvVars.BDMOAS_VAC_BATCH_PROCESS_UNPROCESSED_CHUNKS, false);

  @Override
  public void process() throws AppException, InformationalException {

    final ChunkMainParameters chunkMainParameters = new ChunkMainParameters();

    final curam.ca.gc.bdmoas.batch.bdmoasvac.intf.BDMOASVACProcessBatchStream streamObj =
      BDMOASVACProcessBatchStreamFactory.newInstance();

    final BDMOASVACProcessBatchStreamWrapper streamWrapper =
      new BDMOASVACProcessBatchStreamWrapper(streamObj);

    final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();

    final BDMOASVACProcessBatchWrapper chunkerWrapper =
      new BDMOASVACProcessBatchWrapper(this);

    // retrieve the records that need to be processed
    final BatchProcessingIDList batchProcessingIDList = getRecordsToProcess();

    chunkMainParameters.chunkSize = kChunkSize;
    chunkMainParameters.dontRunStream = kDontRunStream;
    chunkMainParameters.processUnProcessedChunks = kProcessUnProcessedChunks;
    chunkMainParameters.startChunkKey = 1;
    chunkMainParameters.unProcessedChunkReadWait = kUnProcessedChunkReadWait;

    final String instanceID = BATCHPROCESSNAME.BDMOAS_VAC_BATCH;

    batchStreamHelper.runChunkMain(instanceID, null, chunkerWrapper,
      batchProcessingIDList, chunkMainParameters, streamWrapper);

  }

  @Override
  public void sendBatchReport(final String instanceID,
    final BatchProcessDtls batchProcessDtls,
    final BatchProcessChunkDtlsList processedBatchProcessChunkDtlsList,
    final BatchProcessChunkDtlsList unprocessedBatchProcessChunkDtlsList)
    throws AppException, InformationalException {

    return;

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
   * Searches all the VAC records from the Interface Inbound Staging table
   * with the record status as 'New'.
   *
   * @return the list of new VAC records added to a batch list
   * @throws AppException
   * @throws InformationalException
   */
  private BatchProcessingIDList getRecordsToProcess()
    throws AppException, InformationalException {

    final BatchProcessingIDList idList = new BatchProcessingIDList();

    final BDMInboundFileToFileStaging inboundStagingObj =
      BDMInboundFileToFileStagingFactory.newInstance();

    final BDMInboundFileToFileStagingSearchKey searchKey =
      new BDMInboundFileToFileStagingSearchKey();

    // set the status to 'New'
    searchKey.statusCode = BDMINTERFACESTATUSCODE.NEW;
    searchKey.interfaceType = BDMInterfaceNameCode.VAC;

    final BDMInboundFileToFileStagingDtlsList recordList =
      inboundStagingObj.searchByStatusAndType(searchKey);
    BatchProcessingID batchProcessingID;

    for (final BDMInboundFileToFileStagingDtls processChunkDtls : recordList.dtls) {
      batchProcessingID = new BatchProcessingID();
      batchProcessingID.recordID = processChunkDtls.recordID;
      idList.dtls.add(batchProcessingID);
    }

    return idList;
  }

}
