package curam.ca.gc.bdm.batch.bdmintegrity.impl;

import curam.ca.gc.bdm.batch.bdmintegrity.struct.BDMBankAccountIntegrityBatchKey;
import curam.ca.gc.bdm.batch.bdmintegrity.struct.BDMBankAccountIntegrityChunkResult;
import curam.ca.gc.bdm.util.integrity.impl.BDMIntegrityRulesUtil;
import curam.codetable.BATCHPROCESSNAME;
import curam.core.impl.BatchStreamHelper;
import curam.core.impl.EnvVars;
import curam.core.struct.BatchProcessChunkDtlsList;
import curam.core.struct.BatchProcessDtls;
import curam.core.struct.BatchProcessingIDList;
import curam.core.struct.ChunkMainParameters;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;

public class BDMBankAccountIntegrityBatch extends
  curam.ca.gc.bdm.batch.bdmintegrity.base.BDMBankAccountIntegrityBatch {

  private BatchStreamHelper batchStreamHelper;

  @Override
  public void process(
    final BDMBankAccountIntegrityBatchKey bdmBankAccountIntegrityBatchKey)
    throws AppException, InformationalException {

    if (bdmBankAccountIntegrityBatchKey.instanceID.isEmpty()) {
      bdmBankAccountIntegrityBatchKey.instanceID =
        BATCHPROCESSNAME.BDM_BANK_ACCOUNT_INEGRITY_BATCH;
    }

    batchStreamHelper = new BatchStreamHelper();

    final BDMBankAccountIntegrityBatchWrapper chunkWrapper =
      new BDMBankAccountIntegrityBatchWrapper(this);

    final BDMBankAccountIntegrityBatchStreamWrapper streamWrapper =
      new BDMBankAccountIntegrityBatchStreamWrapper(
        new BDMBankAccountIntegrityBatchStream());

    final ChunkMainParameters chunkMainParameters = getChunkMainParameters();

    // Execute Chunker SQL
    final BatchProcessingIDList batchProcessingIDList =
      BDMIntegrityRulesUtil.getBankAccountIntegrityBatchProcessingIDs();

    // Call Batch
    batchStreamHelper.runChunkMain(bdmBankAccountIntegrityBatchKey.instanceID,
      bdmBankAccountIntegrityBatchKey, chunkWrapper, batchProcessingIDList,
      chunkMainParameters, streamWrapper);
  }

  @Override
  public void sendBatchReport(final String instanceID,
    final BatchProcessDtls batchProcessDtls,
    final BatchProcessChunkDtlsList processedBatchProcessChunkDtlsList,
    final BatchProcessChunkDtlsList unprocessedBatchProcessChunkDtlsList)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub

  }

  @Override
  public BDMBankAccountIntegrityChunkResult decodeProcessChunkResult(
    final String resultString) throws AppException, InformationalException {

    // TODO Auto-generated method stub
    return null;
  }

  /**
   * parameter for the batch job
   *
   * @return
   */
  private ChunkMainParameters getChunkMainParameters() {

    final ChunkMainParameters chunkMainParameters = new ChunkMainParameters();

    chunkMainParameters.chunkSize = Configuration.getIntProperty(
      EnvVars.BDM_CURAM_BANK_ACCOUNT_INEGRITY_BATCH_CHUNK_SIZE);
    chunkMainParameters.dontRunStream = Configuration.getBooleanProperty(
      EnvVars.BDM_CURAM_BANK_ACCOUNT_INEGRITY_BATCH_DONT_RUN_STREAM);
    chunkMainParameters.startChunkKey =
      Long.parseLong(Configuration.getProperty(
        EnvVars.BDM_CURAM_BANK_ACCOUNT_INEGRITY_BATCH_START_CHUNK_KEY));
    chunkMainParameters.unProcessedChunkReadWait =
      Configuration.getIntProperty(
        EnvVars.BDM_CURAM_BANK_ACCOUNT_INEGRITY_BATCH_UNPROCESSED_CHUNK_READ_WAIT);
    chunkMainParameters.processUnProcessedChunks =
      Configuration.getBooleanProperty(
        EnvVars.BDM_CURAM_BANK_ACCOUNT_INEGRITY_BATCH_PROCESS_UNPROCESSED_CHUNKS);

    return chunkMainParameters;
  }
}
