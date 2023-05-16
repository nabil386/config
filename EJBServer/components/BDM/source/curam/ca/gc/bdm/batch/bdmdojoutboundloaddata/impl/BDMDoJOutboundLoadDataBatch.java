package curam.ca.gc.bdm.batch.bdmdojoutboundloaddata.impl;

import curam.ca.gc.bdm.batch.bdmbatchutil.impl.BDMBatchUtil;
import curam.ca.gc.bdm.batch.bdmdojoutboundloaddata.fact.BDMDoJOutboundLoadDataBatchStreamFactory;
import curam.ca.gc.bdm.batch.bdmdojoutboundloaddata.intf.BDMDoJOutboundLoadDataBatchStream;
import curam.ca.gc.bdm.entity.fact.BDMDoJOutboundStageFactory;
import curam.ca.gc.bdm.entity.financial.fact.BDMPaymentInstrumentFactory;
import curam.ca.gc.bdm.entity.financial.intf.BDMPaymentInstrument;
import curam.ca.gc.bdm.entity.financial.struct.BDMConcernRole;
import curam.ca.gc.bdm.entity.financial.struct.BDMConcernRoleList;
import curam.ca.gc.bdm.entity.financial.struct.DOJOutboundParticipantsKey;
import curam.ca.gc.bdm.entity.intf.BDMDoJOutboundStage;
import curam.ca.gc.bdm.util.payment.impl.BDMPaymentUtil;
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
import curam.util.resources.Configuration;
import curam.util.type.Date;
import curam.util.type.DateTime;
import org.apache.commons.lang.StringUtils;

/*
 * @author:Chandan K
 * DOJ Outbound batch will process the data of DOJ Inbound and Payment and send
 * the debtor information to Interop system.
 * Task-14074
 */

public class BDMDoJOutboundLoadDataBatch extends
  curam.ca.gc.bdm.batch.bdmdojoutboundloaddata.base.BDMDoJOutboundLoadDataBatch {

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
  public void process(
    final curam.ca.gc.bdm.batch.bdmdojoutboundloaddata.struct.BDMDoJOutboundLoadDataBatchKey key)
    throws AppException, InformationalException {

    final BDMDoJOutboundLoadDataBatchStream batchStream =
      BDMDoJOutboundLoadDataBatchStreamFactory.newInstance();
    final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();

    final BDMDoJOutboundLoadDataBatchWrapper chunkerWrapper =
      new BDMDoJOutboundLoadDataBatchWrapper(this);

    final BatchProcessingIDList batchProcessingIDList = getRecordsToProcess();

    final ChunkMainParameters chunkMainParameters = new ChunkMainParameters();

    // Data wipe off
    final BDMDoJOutboundStage bdmDoJOBStageObj =
      BDMDoJOutboundStageFactory.newInstance();

    if (BDMDoJOutboundStageFactory.newInstance().nkreadmulti().dtls
      .size() > 0) {
      BDMDoJOutboundStageFactory.newInstance().nkremove();
    }

    chunkMainParameters.chunkSize = kChunkSize;
    chunkMainParameters.dontRunStream = kDontRunStream;
    chunkMainParameters.processUnProcessedChunks = kProcessUnProcessedChunks;
    chunkMainParameters.startChunkKey = 1;
    chunkMainParameters.unProcessedChunkReadWait = kUnProcessedChunkReadWait;

    if (StringUtils.isEmpty(key.instanceID)) {
      key.instanceID = BATCHPROCESSNAME.BDM_DOJ_OUTBOUND_DATA_LOAD;
    }
    if (key.processingDate.isZero()) {
      key.processingDate = Date.getCurrentDate();
    }

    // Task 42332: create filename FOE.GARNISHMENT.D<yyyymmdd>.T<hhmm>.xml
    final BDMBatchUtil util = new BDMBatchUtil();
    final DateTime datetime = DateTime.getCurrentDateTime();
    final String currentDateTime = util.getFormattedDateTime();
    final String filename =
      "FOE.GARNISHMENT.D" + currentDateTime.substring(0, 7) + ".T"
        + currentDateTime.substring(8) + ".xml";
    key.runID = UniqueIDFactory.newInstance().getNextID();
    final BDMPaymentUtil pmtUtilObj = new BDMPaymentUtil();
    key.metadataIdentificationID = pmtUtilObj.getGuidNumber();
    util.insertFilename(datetime, filename, key.instanceID, key.runID, null);
    util.insertBatchHistory(datetime, key.instanceID, key.runID);

    final BDMDoJOutboundLoadDataBatchStreamWrapper streamWrapper =
      new BDMDoJOutboundLoadDataBatchStreamWrapper(batchStream, key);

    batchStreamHelper.runChunkMain(key.instanceID, null, chunkerWrapper,
      batchProcessingIDList, chunkMainParameters, streamWrapper);

  }

  /**
   * @return the list of Payment Staging data added to a batch list
   * @throws AppException
   * @throws InformationalException
   */
  protected BatchProcessingIDList getRecordsToProcess()
    throws AppException, InformationalException {

    final BatchProcessingIDList idList = new BatchProcessingIDList();
    final DOJOutboundParticipantsKey dojInputKey =
      new DOJOutboundParticipantsKey();
    BDMConcernRoleList concernRoleList = new BDMConcernRoleList();
    final BDMPaymentInstrument bdmPmtInstrObj =
      BDMPaymentInstrumentFactory.newInstance();
    dojInputKey.dojProcessPendingInd = true;
    concernRoleList =
      bdmPmtInstrObj.searchDOJOutboundParticipants(dojInputKey);
    BatchProcessingID batchProcessingID;

    for (final BDMConcernRole processChunkDtls : concernRoleList.dtls) {
      batchProcessingID = new BatchProcessingID();
      batchProcessingID.recordID = processChunkDtls.concernRoleID;
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

    // TODO Auto-generated method stub

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
