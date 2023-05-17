package curam.ca.gc.bdm.batch.correspondences.automated.impl;

import curam.ca.gc.bdm.batch.correspondences.automated.struct.BDMDirectDepositLetterBatchKey;
import curam.ca.gc.bdm.batch.correspondences.automated.struct.BDMDirectDepositLetterChunkResult;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.core.impl.BatchStreamHelper;
import curam.core.impl.CuramBatch;
import curam.core.struct.BatchProcessChunkDtlsList;
import curam.core.struct.BatchProcessDtls;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingIDList;
import curam.core.struct.ChunkMainParameters;
import curam.core.struct.ConcernRoleKey;
import curam.util.dataaccess.CuramValueList;
import curam.util.dataaccess.DynamicDataAccess;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Trace;
import org.apache.commons.lang3.StringUtils;

public class BDMDirectDepositLetterBatch extends
  curam.ca.gc.bdm.batch.correspondences.automated.base.BDMDirectDepositLetterBatch {

  final CuramBatch curamBatchObj = new CuramBatch();

  final ChunkMainParameters chunkMainParameters = new ChunkMainParameters();

  /**
   * Main process method for the Direct deposit batch stream.
   *
   * @param bdmCreateDirectDepositLetterBatchKey
   * BDMCreateDirectDepositLetterBatchKey id to run the batch process.
   */
  @Override
  public void process(
    final BDMDirectDepositLetterBatchKey bdmCreateDirectDepositLetterBatchKey)
    throws AppException, InformationalException {

    getChunkMainParameters(chunkMainParameters);

    final BDMDirectDepositLetterBatchStreamWrapper streamWrapper =
      new BDMDirectDepositLetterBatchStreamWrapper(
        new BDMDirectDepositLetterBatchStream());

    final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();

    final BDMDirectDepositLetterBatchWrapper chunkerWrapper =
      new BDMDirectDepositLetterBatchWrapper(this);

    // Retrieve the records that need to be processed
    final BatchProcessingIDList batchProcessingIDList =
      getBatchProcessingIDList();

    final String instanceID =
      !StringUtils.isEmpty(bdmCreateDirectDepositLetterBatchKey.instanceID)
        ? bdmCreateDirectDepositLetterBatchKey.instanceID
        : BDMConstants.kAutoDDLeterBatch;

    Trace.kTopLevelLogger.info(BDMConstants.kBatchInstanceID + instanceID
      + BDMConstants.kIDList + batchProcessingIDList.dtls.size());

    batchStreamHelper.runChunkMain(instanceID, null, chunkerWrapper,
      batchProcessingIDList, chunkMainParameters, streamWrapper);

  }

  /**
   * Method to fetch bathes of processed Bulk Print
   *
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  protected BatchProcessingIDList getBatchProcessingIDList()
    throws AppException, InformationalException {

    final StringBuffer sBuf = new StringBuffer();
    sBuf.append("SELECT p.concernroleid\r\n" + "INTO :concernRoleID\r\n"
      + "FROM person p, concernrole cr\r\n"
      + "WHERE p.concernroleid = cr.concernroleid\r\n"
      + "    and not exists (select 1 from concernroleduplicate crd where cr.concernroleid = crd.duplicateconcernroleid)");

    Trace.kTopLevelLogger.info(sBuf.toString());

    final CuramValueList<ConcernRoleKey> curamValueList = DynamicDataAccess
      .executeNsMulti(ConcernRoleKey.class, null, false, sBuf.toString());

    final BatchProcessingIDList processingIDList =
      new BatchProcessingIDList();

    for (final ConcernRoleKey crDtls : curamValueList.items()) {
      final BatchProcessingID processingID = new BatchProcessingID();
      processingID.recordID = crDtls.concernRoleID;
      processingIDList.dtls.add(processingID);
    }

    Trace.kTopLevelLogger.info(
      BDMConstants.kNoOfRecordsToProcess + processingIDList.dtls.size());

    return processingIDList;
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
  public BDMDirectDepositLetterChunkResult decodeProcessChunkResult(
    final String resultString) throws AppException, InformationalException {

    // TODO Auto-generated method stub
    return null;
  }

  /**
   *
   * @param chunkMainParameters
   * @return
   */
  protected ChunkMainParameters
    getChunkMainParameters(final ChunkMainParameters chunkMainParameters) {

    chunkMainParameters.chunkSize = 1;
    // Configuration
    // .getIntProperty(EnvVars.BDM_CORRESPONDENCE_BULK_PRINT_BATCH_CHUNK_SIZE);
    chunkMainParameters.dontRunStream = false;
    // Configuration.getBooleanProperty(
    // EnvVars.BDM_CORRESPONDENCE__BULK_PRINT_BATCH_DONT_RUN_STREAM);
    chunkMainParameters.startChunkKey = 1;
    // Long.parseLong(Configuration
    // .getProperty(EnvVars.BDM_CORRESPONDENCE_BULK_PRINT_BATCH_START_CHUNK_KEY));
    chunkMainParameters.unProcessedChunkReadWait = 1000;
    // Configuration.getIntProperty(
    // EnvVars.BDM_CURAM_CORRESPONDENCE_BULK_PRINT_BATCH_UNPROCESSED_CHUNK_READ_WAIT);
    chunkMainParameters.processUnProcessedChunks = false;
    // Configuration.getBooleanProperty(
    // EnvVars.BDM_CURAM_CORRESPONDENCE_BULK_PRINT_BATCH_PROCESS_UNPROCESSED_CHUNKS);

    return chunkMainParameters;
  }

}
