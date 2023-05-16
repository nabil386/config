package curam.ca.gc.bdmoas.batch.bdmoasmaintainnrtdeductionsusingnrtrateupdate.impl;

import curam.ca.gc.bdm.batch.bdmbatchutil.impl.BDMBatchUtil;
import curam.ca.gc.bdmoas.batch.bdmoasmaintainnrtdeductionsusingnrtrateupdate.fact.BDMOASMaintainNRTDeductionsUsingNRTRateUpdateBatchStreamFactory;
import curam.ca.gc.bdmoas.entity.oasdeduction.fact.BDMOASDeductionFactory;
import curam.codetable.BATCHPROCESSNAME;
import curam.core.fact.UniqueIDFactory;
import curam.core.impl.BatchStreamHelper;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.struct.BatchProcessChunkDtlsList;
import curam.core.struct.BatchProcessChunkKey;
import curam.core.struct.BatchProcessDtls;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingIDList;
import curam.core.struct.CaseDeductionItemKey;
import curam.core.struct.CaseDeductionItemKeyList;
import curam.core.struct.ChunkMainParameters;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.type.DateTime;
import org.apache.commons.lang3.StringUtils;

/**
 * This batch will be used to end date and create new NRT Withhold deductions
 * in event of the change in NRT rate due to change in Tax treaty between Canada
 * and other countries.
 *
 * This is a CHUNKER class which will be used get the list of Case Deduction
 * Items which contains NRT Withhold deductions with old NRT rate and there is
 * change in NRT rate due to change in Tax treaty between Canada and other
 * countries.
 *
 * @author pranav.agarwal
 */
public class BDMOASMaintainNRTDeductionsUsingNRTRateUpdateBatch extends
  curam.ca.gc.bdmoas.batch.bdmoasmaintainnrtdeductionsusingnrtrateupdate.base.BDMOASMaintainNRTDeductionsUsingNRTRateUpdateBatch {

  /**
   * The number of records per chunk.
   */
  private static final int kChunkSize = getIntProperty(
    EnvVars.BDMOAS_MAINTAINNRTDEDUCTIONSUSINGNRTRATEUPDATE_CHUNK_SIZE,
    EnvVars.BDMOAS_MAINTAINNRTDEDUCTIONSUSINGNRTRATEUPDATE_CHUNK_SIZE_DEFAULT);

  /**
   * Controls whether the CHUNKER should start a stream.
   */
  private static final boolean kDontRunStream =
    Configuration.getBooleanProperty(
      EnvVars.BDMOAS_MAINTAINNRTDEDUCTIONSUSINGNRTRATEUPDATE_DONTRUNSTREAM,
      EnvVars.BDMOAS_MAINTAINNRTDEDUCTIONSUSINGNRTRATEUPDATE_DONTRUNSTREAM_DEFAULT);

  /**
   * Wait time for unprocessed chunks.
   */
  private static final int kUnProcessedChunkReadWait = getIntProperty(
    EnvVars.BDMOAS_MAINTAINNRTDEDUCTIONSUSINGNRTRATEUPDATE_UNPROCESSED_CHUNK_WAIT_INTERVAL,
    EnvVars.BDMOAS_MAINTAINNRTDEDUCTIONSUSINGNRTRATEUPDATE_UNPROCESSED_CHUNK_WAIT_INTERVAL_DEFAULT);

  /**
   * Indicates whether to process unprocessed chunks at the end.
   */
  private static final boolean kProcessUnProcessedChunks =
    Configuration.getBooleanProperty(
      EnvVars.BDMOAS_MAINTAINNRTDEDUCTIONSUSINGNRTRATEUPDATE_PROCESS_UNPROCESSED_CHUNKS,
      false);

  /**
   * This method is used to get the which will be used get the list of Case
   * Deduction Items which contains NRT Withhold deductions with old NRT rate
   * and there is change in NRT rate due to change in Tax treaty between Canada
   * and other countries.
   * These will be used further by Streamers to process.
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
    final BDMOASMaintainNRTDeductionsUsingNRTRateUpdateBatchStreamWrapper streamWrapper =
      new BDMOASMaintainNRTDeductionsUsingNRTRateUpdateBatchStreamWrapper(
        BDMOASMaintainNRTDeductionsUsingNRTRateUpdateBatchStreamFactory
          .newInstance());
    // new instance of CHUNKER wrapper
    final BDMOASMaintainNRTDeductionsUsingNRTRateUpdateBatchWrapper chunkerWrapper =
      new BDMOASMaintainNRTDeductionsUsingNRTRateUpdateBatchWrapper(this);

    // calling the method to retrieve the records that need to be processed
    final BatchProcessingIDList batchProcessingIDList = getRecordsToProcess();

    // check if instance ID is empty
    if (StringUtils.isEmpty(key.instanceID)) {
      // assign the instance ID here
      key.instanceID =
        BATCHPROCESSNAME.BDMOAS_MAINTAIN_NRT_WITHHOLD_DEDUCTION_USING_NRT_RATE_BATCH;
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
   * This method will be used to get the list of Case Deduction
   * Items which contains NRT Withhold deductions with old NRT rate and there is
   * change in NRT rate due to change in Tax treaty between Canada and other
   * countries.
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

    // calling the method to search case deduction item ID for the NRT
    // deductions if there is a change in the Country NRT rate
    final CaseDeductionItemKeyList caseDeductionItemKeyList =
      BDMOASDeductionFactory.newInstance()
        .searchNRTDeductionWithNRTRateChange();

    // new reference
    BatchProcessingID batchProcessingID;
    // iterate the results
    for (final CaseDeductionItemKey caseDeductionItemKey : caseDeductionItemKeyList.dtls) {
      // new instance
      batchProcessingID = new BatchProcessingID();
      // assign the value
      batchProcessingID.recordID = caseDeductionItemKey.caseDeductionItemID;
      // add to the return list object
      batchProcessingIDList.dtls.add(batchProcessingID);
    }

    // return the list object
    return batchProcessingIDList;
  }

}
