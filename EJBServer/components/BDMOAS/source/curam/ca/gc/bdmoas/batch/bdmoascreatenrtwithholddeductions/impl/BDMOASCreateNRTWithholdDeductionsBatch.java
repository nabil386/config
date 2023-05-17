package curam.ca.gc.bdmoas.batch.bdmoascreatenrtwithholddeductions.impl;

import curam.ca.gc.bdm.batch.bdmbatchutil.impl.BDMBatchUtil;
import curam.ca.gc.bdm.entity.fact.BDMBatchHistoryFactory;
import curam.ca.gc.bdm.entity.struct.BDMBatchHistoryDtls;
import curam.ca.gc.bdmoas.batch.bdmoascreatenrtwithholddeductions.fact.BDMOASCreateNRTWithholdDeductionsBatchStreamFactory;
import curam.ca.gc.bdmoas.batch.bdmoascreatenrtwithholddeductions.struct.BDMOASCreateNRTWithholdDeductionsSearchKey;
import curam.ca.gc.bdmoas.entity.oasdeduction.fact.BDMOASDeductionFactory;
import curam.codetable.BATCHPROCESSNAME;
import curam.core.fact.UniqueIDFactory;
import curam.core.impl.BatchStreamHelper;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.struct.BatchProcessChunkDtlsList;
import curam.core.struct.BatchProcessChunkKey;
import curam.core.struct.BatchProcessDtls;
import curam.core.struct.BatchProcessKey;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingIDList;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseHeaderKeyList;
import curam.core.struct.ChunkMainParameters;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.RecordNotFoundException;
import curam.util.resources.Configuration;
import curam.util.type.DateTime;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

/**
 * This batch will create a new NRT Withhold deduction when NRT Withhold
 * deduction is end-dated because of
 * NRT Correction evidence or any unexpected conditions.
 *
 * This is a CHUNKER class which will be used get the list of PDC Case IDs for
 * which NRT Correction evidence is end dating within next three months and PDC
 * does not have NRT withhold deduction after NRT correction end date
 *
 * @author pranav.agarwal
 */
public class BDMOASCreateNRTWithholdDeductionsBatch extends
  curam.ca.gc.bdmoas.batch.bdmoascreatenrtwithholddeductions.base.BDMOASCreateNRTWithholdDeductionsBatch {

  /**
   * The number of records per chunk.
   */
  private static final int kChunkSize =
    getIntProperty(EnvVars.BDMOAS_CREATENRTWITHHOLDDEDUCTIONS_CHUNK_SIZE,
      EnvVars.BDMOAS_CREATENRTWITHHOLDDEDUCTIONS_CHUNK_SIZE_DEFAULT);

  /**
   * Controls whether the CHUNKER should start a stream.
   */
  private static final boolean kDontRunStream =
    Configuration.getBooleanProperty(
      EnvVars.BDMOAS_CREATENRTWITHHOLDDEDUCTIONS_DONTRUNSTREAM,
      EnvVars.BDMOAS_CREATENRTWITHHOLDDEDUCTIONS_DONTRUNSTREAM_DEFAULT);

  /**
   * Wait time for unprocessed chunks.
   */
  private static final int kUnProcessedChunkReadWait = getIntProperty(
    EnvVars.BDMOAS_CREATENRTWITHHOLDDEDUCTIONS_UNPROCESSED_CHUNK_WAIT_INTERVAL,
    EnvVars.BDMOAS_CREATENRTWITHHOLDDEDUCTIONS_UNPROCESSED_CHUNK_WAIT_INTERVAL_DEFAULT);

  /**
   * Indicates whether to process unprocessed chunks at the end.
   */
  private static final boolean kProcessUnProcessedChunks =
    Configuration.getBooleanProperty(
      EnvVars.BDMOAS_CREATENRTWITHHOLDDEDUCTIONS_PROCESS_UNPROCESSED_CHUNKS,
      false);

  /**
   * This method is used to get the PDC case IDs which will be used further by
   * Streamers to process.
   *
   * @param key - This holds the input key
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void process(final BatchProcessChunkKey key)
    throws AppException, InformationalException {

    // new instance of chunk parameters
    final ChunkMainParameters chunkMainParameters = new ChunkMainParameters();
    // populate the chunk parameters here
    chunkMainParameters.chunkSize = kChunkSize;
    chunkMainParameters.dontRunStream = kDontRunStream;
    chunkMainParameters.processUnProcessedChunks = kProcessUnProcessedChunks;
    chunkMainParameters.startChunkKey = CuramConst.gkOne;
    chunkMainParameters.unProcessedChunkReadWait = kUnProcessedChunkReadWait;

    // new instance of streamer wrapper
    final BDMOASCreateNRTWithholdDeductionsBatchStreamWrapper streamWrapper =
      new BDMOASCreateNRTWithholdDeductionsBatchStreamWrapper(
        BDMOASCreateNRTWithholdDeductionsBatchStreamFactory.newInstance());
    // new instance of CHUNKER wrapper
    final BDMOASCreateNRTWithholdDeductionsBatchWrapper chunkerWrapper =
      new BDMOASCreateNRTWithholdDeductionsBatchWrapper(this);

    // calling the method to retrieve the records that need to be processed
    final BatchProcessingIDList batchProcessingIDList = getRecordsToProcess();

    // check if instance ID is empty
    if (StringUtils.isEmpty(key.instanceID)) {
      // assign the instance ID here
      key.instanceID =
        BATCHPROCESSNAME.BDMOAS_CREATE_NRT_WITHHOLD_DEDUCTION_BATCH;
    }

    // insert the record into history table to maintain the runs
    final BDMBatchUtil util = new BDMBatchUtil();
    // calling the utility method to insert the record
    util.insertBatchHistory(DateTime.getCurrentDateTime(), key.instanceID,
      UniqueIDFactory.newInstance().getNextID());

    // new instance of batch stream helper
    final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();
    // calling method to run the chunks
    batchStreamHelper.runChunkMain(key.instanceID, null, chunkerWrapper,
      batchProcessingIDList, chunkMainParameters, streamWrapper);

  }

  /**
   * This method is used to generate batch report.
   *
   * @param instanceID - This holds the batch instance ID.
   * @param batchProcessDtls - This holds the batch process details.
   * @param processedBatchProcessChunkDtlsList - This holds process chunk
   * details.
   * @param unprocessedBatchProcessChunkDtlsList - This holds unprocessed chunk
   * details.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void sendBatchReport(final String instanceID,
    final BatchProcessDtls batchProcessDtls,
    final BatchProcessChunkDtlsList processedBatchProcessChunkDtlsList,
    final BatchProcessChunkDtlsList unprocessedBatchProcessChunkDtlsList)
    throws AppException, InformationalException {

    // nothing as of now
  }

  /**
   * This method is used to get the value from the configured properties
   * for batch.
   *
   * @param propName - This holds the property name.
   * @param defaultValue - This holds the default value.
   * @return the property value.
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

  /**
   * This method is used to get the list of PDC Case IDs for which NRT
   * Correction evidence is end dating within next three months and PDC does not
   * have NRT withhold deduction after NRT correction end date
   *
   * @return the list of payment instruments added to a batch list
   * @throws AppException
   * @throws InformationalException
   */
  private BatchProcessingIDList getRecordsToProcess()
    throws AppException, InformationalException {

    // new instance of return object
    final BatchProcessingIDList batchProcessingIDList =
      new BatchProcessingIDList();

    // new instance of input key
    final BDMOASCreateNRTWithholdDeductionsSearchKey searchKey =
      new BDMOASCreateNRTWithholdDeductionsSearchKey();
    // new instance of simple date format
    final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    // populate key object
    try {
      // start date will be the last batch run date
      final BatchProcessKey batchProcessKey = new BatchProcessKey();
      // populate the key object
      batchProcessKey.instanceID =
        BATCHPROCESSNAME.BDMOAS_CREATE_NRT_WITHHOLD_DEDUCTION_BATCH;
      // calling the method to read last batch run date by instance ID
      final BDMBatchHistoryDtls bdmBatchHistoryDtls = BDMBatchHistoryFactory
        .newInstance().readLastBatchRunDateByInstanceID(batchProcessKey);
      // assign the start date here
      searchKey.startDate =
        sdf.format(bdmBatchHistoryDtls.createdOn.getCalendar().getTime());

    } catch (final RecordNotFoundException recordNotFoundException) {
      // that means we are running the batch for the first time
      // assign the current date
      searchKey.startDate = sdf.format(new Date());
    }

    // end date will be after 3 months from the current date
    final Calendar cal = Calendar.getInstance();
    // add three months
    cal.add(Calendar.MONTH, 3);
    // assign the end date here
    searchKey.endDate = sdf.format(cal.getTime());
    // calling the method to search PDC Case ID for which NRT Correction
    // evidence is end dating within next three months and PDC does not have NRT
    // withhold deduction after NRT correction end date
    final CaseHeaderKeyList caseHeaderKeyList =
      BDMOASDeductionFactory.newInstance()
        .searchPDCCaseIDToCreateNRTDeductionAfterNRTCorrectionEndDate(
          searchKey);

    // new reference
    BatchProcessingID batchProcessingID;
    // iterate the results
    for (final CaseHeaderKey caseHeaderKey : caseHeaderKeyList.dtls) {
      // new instance
      batchProcessingID = new BatchProcessingID();
      // assign the value
      batchProcessingID.recordID = caseHeaderKey.caseID;
      // add to the return list object
      batchProcessingIDList.dtls.add(batchProcessingID);
    }

    // return the list object
    return batchProcessingIDList;
  }

}
