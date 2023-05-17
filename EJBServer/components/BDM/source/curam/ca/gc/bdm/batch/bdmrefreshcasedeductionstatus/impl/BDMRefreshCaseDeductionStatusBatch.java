package curam.ca.gc.bdm.batch.bdmrefreshcasedeductionstatus.impl;

import curam.ca.gc.bdm.batch.bdmrefreshcasedeductionstatus.fact.BDMRefreshCaseDeductionStatusStreamFactory;
import curam.ca.gc.bdm.entity.bdmcasedeductionitem.fact.BDMCaseDeductionItemFactory;
import curam.ca.gc.bdm.entity.bdmcasedeductionitem.intf.BDMCaseDeductionItem;
import curam.ca.gc.bdm.entity.bdmcasedeductionitem.struct.ProvTaxDeductionsForRecheckKey;
import curam.codetable.BATCHPROCESSNAME;
import curam.core.impl.BatchStreamHelper;
import curam.core.impl.EnvVars;
import curam.core.struct.BatchProcessChunkDtlsList;
import curam.core.struct.BatchProcessDtls;
import curam.core.struct.BatchProcessingDate;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingIDList;
import curam.core.struct.CaseDeductionItemKey;
import curam.core.struct.CaseDeductionItemKeyList;
import curam.core.struct.ChunkMainParameters;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.transaction.TransactionInfo;

public abstract class BDMRefreshCaseDeductionStatusBatch extends
  curam.ca.gc.bdm.batch.bdmrefreshcasedeductionstatus.base.BDMRefreshCaseDeductionStatusBatch {

  private static BatchStreamHelper batchHelper = new BatchStreamHelper();

  /**
   * The number of records per chunk.
   */
  private static final int kChunkSize =
    getIntProperty(EnvVars.BDM_ENV_BDMREFRESHCASEDEDUCTIONSTATUS_CHUNK_SIZE,
      EnvVars.BDM_ENV_BDMREFRESHCASEDEDUCTIONSTATUS_CHUNK_SIZE_DEFAULT);

  /**
   * Controls whether the chunker should start a stream.
   */
  private static final boolean kDontRunStream =
    Configuration.getBooleanProperty(
      EnvVars.BDM_ENV_BDMREFRESHCASEDEDUCTIONSTATUS_DONTRUNSTREAM,
      EnvVars.BDM_ENV_BDMREFRESHCASEDEDUCTIONSTATUS_DONTRUNSTREAM_DEFAULT);

  /**
   * Wait time for unprocessed chunks.
   */
  private static final int kUnProcessedChunkReadWait = getIntProperty(
    EnvVars.BDM_ENV_BDMREFRESHCASEDEDUCTIONSTATUS_UNPROCESSED_CHUNK_WAIT_INTERVAL,
    EnvVars.BDM_ENV_BDMREFRESHCASEDEDUCTIONSTATUS_UNPROCESSED_CHUNK_WAIT_INTERVAL_DEFAULT);

  /**
   * Indicates whether to process unprocessed chunks at the end.
   */
  private static final boolean kProcessUnProcessedChunks =
    Configuration.getBooleanProperty(
      EnvVars.BDM_ENV_BDMREFRESHCASEDEDUCTIONSTATUS_PROCESS_UNPROCESSED_CHUNKS,
      false);

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
  public void process(final BatchProcessingDate key)
    throws AppException, InformationalException {

    final ChunkMainParameters chunkMainParameters = new ChunkMainParameters();

    chunkMainParameters.chunkSize = kChunkSize;
    chunkMainParameters.dontRunStream = kDontRunStream;
    chunkMainParameters.processUnProcessedChunks = kProcessUnProcessedChunks;
    chunkMainParameters.startChunkKey = 1;
    chunkMainParameters.unProcessedChunkReadWait = kUnProcessedChunkReadWait;

    if (key.processingDate.isZero()) {
      key.processingDate = TransactionInfo.getSystemDate();
    }

    final curam.ca.gc.bdm.batch.bdmrefreshcasedeductionstatus.intf.BDMRefreshCaseDeductionStatusStream bdmRefreshCaseDeductionStatusStreamerObj =
      BDMRefreshCaseDeductionStatusStreamFactory.newInstance();

    final BDMRefreshCaseDeductionStatusStreamWrapper streamWrapper =
      new BDMRefreshCaseDeductionStatusStreamWrapper(
        bdmRefreshCaseDeductionStatusStreamerObj);

    final BDMRefreshCaseDeductionStatusBatchWrapper chunkerWrapper =
      new BDMRefreshCaseDeductionStatusBatchWrapper(this);

    // Retrieve the records that need to be processed
    final BatchProcessingIDList batchProcessingIDList =
      getRecordsToProcess(key);

    batchHelper.runChunkMain(BATCHPROCESSNAME.BDM_REFRESH_DEDUCTION_STATUS,
      chunkMainParameters, chunkerWrapper, batchProcessingIDList,
      chunkMainParameters, streamWrapper);
  }

  /**
   * Parses the file and loads the staging table with the status of unprocessed
   *
   * @return the list of records to be processed
   * @throws AppException
   * @throws InformationalException
   */
  protected BatchProcessingIDList
    getRecordsToProcess(final BatchProcessingDate processingDate)
      throws AppException, InformationalException {

    final BatchProcessingIDList idList = new BatchProcessingIDList();

    // Fetch the record for processing.
    final BDMCaseDeductionItem bdmCaseDeductionItem =
      BDMCaseDeductionItemFactory.newInstance();
    final ProvTaxDeductionsForRecheckKey provTaxDeductionsForRecheckKey =
      new ProvTaxDeductionsForRecheckKey();
    provTaxDeductionsForRecheckKey.processingDate =
      processingDate.processingDate;
    final CaseDeductionItemKeyList caseDeductionItemKeyList =
      bdmCaseDeductionItem
        .searchProvTaxDeductionsDueForRecheck(provTaxDeductionsForRecheckKey);

    BatchProcessingID batchProcessingID = null;

    for (final CaseDeductionItemKey caseDeductionItemKey : caseDeductionItemKeyList.dtls) {
      batchProcessingID = new BatchProcessingID();
      batchProcessingID.recordID = caseDeductionItemKey.caseDeductionItemID;
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

  }

}
