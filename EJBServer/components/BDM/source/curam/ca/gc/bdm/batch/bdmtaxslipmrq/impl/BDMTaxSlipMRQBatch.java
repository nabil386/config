/**
 *
 */
package curam.ca.gc.bdm.batch.bdmtaxslipmrq.impl;

import curam.ca.gc.bdm.batch.bdmtaxslipmrq.fact.BDMTaxSlipMRQBatchStreamFactory;
import curam.ca.gc.bdm.batch.bdmtaxslipmrq.struct.BDMTaxSlipMRQBatchKey;
import curam.ca.gc.bdm.batch.bdmtaxslipmrqbatch.impl.mrqpojos.BDMTaxSlipMRQRecord;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPPROCSTATUS;
import curam.codetable.BATCHPROCESSNAME;
import curam.core.impl.BatchStreamHelper;
import curam.core.impl.EnvVars;
import curam.core.struct.BatchProcessChunkDtlsList;
import curam.core.struct.BatchProcessDtls;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingIDList;
import curam.core.struct.ChunkMainParameters;
import curam.util.dataaccess.DynamicDataAccess;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import java.util.ArrayList;
import org.apache.commons.lang.StringUtils;

/**
 * @author azar.khan
 *
 */
public class BDMTaxSlipMRQBatch
  extends curam.ca.gc.bdm.batch.bdmtaxslipmrq.base.BDMTaxSlipMRQBatch {

  private static curam.util.type.Date processingDate;

  private ChunkMainParameters getChunkMainParameters() {

    final ChunkMainParameters chunkMainParameters = new ChunkMainParameters();

    chunkMainParameters.chunkSize =
      Configuration.getIntProperty(EnvVars.BDM_TAX_SLIP_MRQ_BATCH_CHUNK_SIZE);
    chunkMainParameters.dontRunStream = Configuration
      .getBooleanProperty(EnvVars.BDM_TAX_SLIP_MRQ_BATCH_DONTRUNSTREAM);
    chunkMainParameters.startChunkKey = Long.parseLong(Configuration
      .getProperty(EnvVars.BDM_TAX_SLIP_MRQ_BATCH_START_CHUNK_KEY));
    chunkMainParameters.unProcessedChunkReadWait =
      Configuration.getIntProperty(
        EnvVars.BDM_TAX_SLIP_MRQ_BATCH_UNPROCESSED_CHUNK_WAIT_INTERVAL);
    chunkMainParameters.processUnProcessedChunks =
      Configuration.getBooleanProperty(
        EnvVars.BDM_TAX_SLIP_MRQ_BATCH_PROCESS_UNPROCESSED_CHUNKS);

    return chunkMainParameters;
  }

  @Override
  public void process(final BDMTaxSlipMRQBatchKey key)
    throws AppException, InformationalException {

    final curam.ca.gc.bdm.batch.bdmtaxslipmrq.intf.BDMTaxSlipMRQBatchStream taxSlipMRQStreamObj =
      BDMTaxSlipMRQBatchStreamFactory.newInstance();

    final BDMTaxSlipMRQBatchStreamWrapper taxSlipMRQBatchStreamWrapper =
      new BDMTaxSlipMRQBatchStreamWrapper(taxSlipMRQStreamObj);

    final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();

    final BDMTaxSlipMRQBatchWrapper bdmTaxSlipMRQChunkerWrapper =
      new BDMTaxSlipMRQBatchWrapper(this);

    if (key.processingDate != null && !key.processingDate.isZero()) {
      processingDate = key.processingDate;
    } else {
      processingDate = TransactionInfo.getSystemDate();
    }

    // Set the start time of the program
    batchStreamHelper.setStartTime();
    Trace.kTopLevelLogger.info("BDMTaxSlipMRQBatch  start time : "
      + TransactionInfo.getSystemDateTime());

    // Retrieve the records that need to be processed
    final BatchProcessingIDList batchProcessingIDList =
      getEligibleRecordIDList();

    final String instanceID = !StringUtils.isEmpty(key.instanceID)
      ? key.instanceID : BATCHPROCESSNAME.BDM_TAXSLIP_MRQ_DATA_BATCH;

    final ChunkMainParameters chunkMainParameters = getChunkMainParameters();
    batchStreamHelper.runChunkMain(instanceID, key,
      bdmTaxSlipMRQChunkerWrapper, batchProcessingIDList, chunkMainParameters,
      taxSlipMRQBatchStreamWrapper);

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
   * Get eligible record list from BDMTaxSlipDataRL1 entity
   *
   * @return BatchProcessingIDList/list of eligible records
   * @throws AppException
   * @throws InformationalException
   */
  public BatchProcessingIDList getEligibleRecordIDList()
    throws AppException, InformationalException {

    final StringBuilder sqlSelectBuilder = new StringBuilder();
    sqlSelectBuilder.append(
      "SELECT tsd.taxSlipDataID INTO :taxSlipDataID FROM BDMTaxSlipDataRL1 tsd WHERE ");
    sqlSelectBuilder.append(" tsd.PROCESSINGSTATUS = '"
      + BDMTAXSLIPPROCSTATUS.PENDINGTRANSFER + "'");

    ArrayList<BDMTaxSlipMRQRecord> TaxSlipDataRL1RecordList =
      new ArrayList<BDMTaxSlipMRQRecord>();
    try {

      Trace.kTopLevelLogger.info(
        "BDMTaxSlipMRQ Batch- SQL to get eligible records to be processed :"
          + sqlSelectBuilder.toString());

      TaxSlipDataRL1RecordList = DynamicDataAccess.executeNsMulti(
        BDMTaxSlipMRQRecord.class, null, false, sqlSelectBuilder.toString());
    } catch (final Exception e) {
      e.printStackTrace();
    }

    final BatchProcessingIDList recordList = new BatchProcessingIDList();
    for (final BDMTaxSlipMRQRecord bdmTaxSlipMRQRecord : TaxSlipDataRL1RecordList) {
      final BatchProcessingID batchProcessingIDObj = new BatchProcessingID();
      batchProcessingIDObj.recordID = bdmTaxSlipMRQRecord.getTaxSlipDataID();
      recordList.dtls.add(batchProcessingIDObj);
    }
    return recordList;
  }

}
