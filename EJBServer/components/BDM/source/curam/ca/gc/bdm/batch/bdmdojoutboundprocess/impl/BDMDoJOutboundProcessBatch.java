package curam.ca.gc.bdm.batch.bdmdojoutboundprocess.impl;

import curam.ca.gc.bdm.batch.bdmdojoutboundprocess.fact.BDMDoJOutboundProcessBatchStreamFactory;
import curam.ca.gc.bdm.batch.bdmdojoutboundprocess.intf.BDMDoJOutboundProcessBatchStream;
import curam.ca.gc.bdm.batch.bdmdojoutboundprocess.struct.BDMDoJOutboundProcessBatchKey;
import curam.ca.gc.bdm.codetable.BDMDOJRECORDSTATUS;
import curam.ca.gc.bdm.entity.fact.BDMDoJOutboundStageFactory;
import curam.ca.gc.bdm.entity.intf.BDMDoJOutboundStage;
import curam.ca.gc.bdm.entity.struct.BDMDoJOutboundStageKeyStruct1;
import curam.ca.gc.bdm.entity.struct.BDMRecordID;
import curam.ca.gc.bdm.entity.struct.BDMRecordIDList;
import curam.ca.gc.bdm.sl.interfaces.bdmdojoutbound.impl.BDMDoJOutboundInterfaceImpl;
import curam.codetable.BATCHPROCESSNAME;
import curam.core.impl.BatchStreamHelper;
import curam.core.impl.EnvVars;
import curam.core.struct.BatchProcessChunkDtlsList;
import curam.core.struct.BatchProcessDtls;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingIDList;
import curam.core.struct.BatchProcessingResult;
import curam.core.struct.ChunkMainParameters;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.Trace;
import java.io.IOException;
import java.text.ParseException;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringUtils;

public class BDMDoJOutboundProcessBatch extends
  curam.ca.gc.bdm.batch.bdmdojoutboundprocess.base.BDMDoJOutboundProcessBatch {

  /*
   * populate the obligation data and metadata to the interface.
   * interface pass the response payload and post it interop team through api
   */

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

  public int totalRecord = 0;

  @Override
  public void process(final BDMDoJOutboundProcessBatchKey key)
    throws AppException, InformationalException {

    final BDMDoJOutboundProcessBatchStream batchStream =
      BDMDoJOutboundProcessBatchStreamFactory.newInstance();
    final BDMDoJOutboundProcessBatchStreamWrapper streamWrapper =
      new BDMDoJOutboundProcessBatchStreamWrapper(batchStream);

    final BDMDoJOutboundProcessBatchWrapper chunkerWrapper =
      new BDMDoJOutboundProcessBatchWrapper(this);

    final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();

    // updateObligationWithMetaID();
    final BatchProcessingIDList batchProcessingIDList = getRecordsToProcess();

    final ChunkMainParameters chunkMainParameters = new ChunkMainParameters();

    chunkMainParameters.chunkSize = kChunkSize;
    chunkMainParameters.dontRunStream = kDontRunStream;
    chunkMainParameters.processUnProcessedChunks = kProcessUnProcessedChunks;
    chunkMainParameters.startChunkKey = 1;
    chunkMainParameters.unProcessedChunkReadWait = kUnProcessedChunkReadWait;

    final String instanceID;
    if (!StringUtils.isEmpty(key.instanceID)) {
      instanceID = key.instanceID;
    } else {
      instanceID = BATCHPROCESSNAME.BDM_DOJ_OUTBOUND_DATA_PROCESS;
    }
    streamWrapper.setInstanceID(instanceID);

    batchStreamHelper.runChunkMain(instanceID, null, chunkerWrapper,
      batchProcessingIDList, chunkMainParameters, streamWrapper);

  }

  /**
   * @return Outbound Staging assign metaDataRecordID to batchProcessingID
   * @throws AppException
   * @throws InformationalException
   */
  protected BatchProcessingIDList getRecordsToProcess()
    throws AppException, InformationalException {

    final BatchProcessingIDList idList = new BatchProcessingIDList();

    final BDMDoJOutboundStage obligationObj =
      BDMDoJOutboundStageFactory.newInstance();
    final BDMDoJOutboundStageKeyStruct1 statusKey =
      new BDMDoJOutboundStageKeyStruct1();

    // Select record whose doj status is completed and sort by person SIN number
    BDMRecordIDList bdmRecordIDList = new BDMRecordIDList();
    statusKey.processingStatus = BDMDOJRECORDSTATUS.COMPLETED;
    bdmRecordIDList = obligationObj.searchByDoJProcessingStatus(statusKey);

    BatchProcessingID batchProcessingID;

    for (final BDMRecordID processChunkDtls : bdmRecordIDList.dtls) {
      batchProcessingID = new BatchProcessingID();
      batchProcessingID.recordID = processChunkDtls.recordID;
      idList.dtls.add(batchProcessingID);
    }

    return idList;
  }

  @Override
  public void sendBatchReport(final String instanceID,
    final BatchProcessDtls batchProcessDtls,
    final BatchProcessChunkDtlsList processedBatchProcessChunkDtlsList,
    final BatchProcessChunkDtlsList unprocessedBatchProcessChunkDtlsList)
    throws AppException, InformationalException {

    int totalCount = 0;
    // For each processed chunk, gather the stats
    for (int i = 0; i < processedBatchProcessChunkDtlsList.dtls.size(); i++) {
      final StringTokenizer st = new StringTokenizer(
        processedBatchProcessChunkDtlsList.dtls.get(i).resultSummary);
      int elementNumber = 0;
      while (st.hasMoreTokens()) {
        elementNumber++;
        switch (elementNumber) {
          case 1:
            totalCount = totalCount + Integer.valueOf(st.nextToken());
          break;
          default:
            st.nextToken();
          break;
        }
      }
    }
    final BDMDoJOutboundInterfaceImpl dojSysEvent =
      new BDMDoJOutboundInterfaceImpl();

    try {
      dojSysEvent.createSystemEventRequest(totalCount);
    } catch (AppException | InformationalException | IOException
      | ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    Trace.kTopLevelLogger.info("Total Processed Records:" + totalCount);

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

  @Override
  public BatchProcessingResult
    doExtraProcessing(final BatchProcessStreamKey batchProcessStreamKey)
      throws AppException, InformationalException {

    final BatchProcessingResult result = new BatchProcessingResult();
    return result;
  }

}
