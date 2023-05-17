package curam.ca.gc.bdm.batch.bdmvsgtaskallocation.impl;

import curam.ca.gc.bdm.batch.bdmvsgtaskallocation.fact.BDMVSGTaskAllocationBatchStreamFactory;
import curam.ca.gc.bdm.batch.bdmvsgtaskallocation.intf.BDMVSGTaskAllocationBatchStream;
import curam.ca.gc.bdm.batch.bdmvsgtaskallocation.struct.BDMVSGTaskAllocationBatchKey;
import curam.ca.gc.bdm.entity.fact.BDMGenericBatchProcessInputFactory;
import curam.ca.gc.bdm.entity.struct.BDMTaskSearchKey;
import curam.ca.gc.bdm.entity.struct.BDMTaskSearchResult;
import curam.ca.gc.bdm.entity.struct.BDMTaskSearchResultList;
import curam.ca.gc.bdm.message.BDMVSGTASKALLOCATIONBATCH;
import curam.codetable.BATCHPROCESSNAME;
import curam.core.impl.BatchStreamHelper;
import curam.core.impl.CuramBatch;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.struct.BatchProcessChunkDtls;
import curam.core.struct.BatchProcessChunkDtlsList;
import curam.core.struct.BatchProcessDtls;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingIDList;
import curam.core.struct.ChunkMainParameters;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.ProgramLocale;
import curam.util.resources.StringUtil;
import curam.util.type.StringList;
import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;

public class BDMVSGTaskAllocationBatch extends
  curam.ca.gc.bdm.batch.bdmvsgtaskallocation.base.BDMVSGTaskAllocationBatch {

  @Override
  public void process(final BDMVSGTaskAllocationBatchKey key)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub

    final ChunkMainParameters chunkMainParameters = getChunkMainParameters();

    final BDMVSGTaskAllocationBatchWrapper chunkerWrapper =
      new BDMVSGTaskAllocationBatchWrapper(this);

    final BatchProcessingIDList batchProcessingIDList =
      getBatchProcessingIDs(key);

    final String instanceID;
    if (!StringUtils.isEmpty(key.instanceID)) {
      instanceID = key.instanceID;
    } else {
      instanceID = BATCHPROCESSNAME.BDM_VSG_TASK_ALLOCATION_BATCH;
    }
    final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();

    final BDMVSGTaskAllocationBatchStream streamObj =
      BDMVSGTaskAllocationBatchStreamFactory.newInstance();

    final BDMVSGTaskAllocationBatchStreamWrapper streamWrapper =
      new BDMVSGTaskAllocationBatchStreamWrapper(streamObj, instanceID);

    batchStreamHelper.runChunkMain(instanceID, key, chunkerWrapper,
      batchProcessingIDList, chunkMainParameters, streamWrapper);

  }

  @Override
  public void sendBatchReport(final String instanceID,
    final BatchProcessDtls batchProcessDtls,
    final BatchProcessChunkDtlsList processedBatchProcessChunkDtlsList,
    final BatchProcessChunkDtlsList unprocessedBatchProcessChunkDtlsList)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub
    long totalNumberOfTasksProcessed = 0;
    long totalNumberOfTasksSkipped = 0;
    final int kChunkSize = Configuration
      .getIntProperty(EnvVars.BDM_SVG_TASK_ALLOCATION_BATCH_CHUNK_SIZE);

    final long totalNumberOfUnprocessedChunks =
      unprocessedBatchProcessChunkDtlsList.dtls.size();

    // curamBatch manipulation variable
    final CuramBatch curamBatchObj = new CuramBatch();

    // the StringBuffer size is based on the data being written to the buffer
    final StringBuffer emailMessage = new StringBuffer(512);
    StringList resultsList = null;

    // For each processed chunk, gather the stats
    for (final BatchProcessChunkDtls processedBatchProcessChunkDtls : processedBatchProcessChunkDtlsList.dtls) {
      resultsList = StringUtil
        .tabText2StringList(processedBatchProcessChunkDtls.resultSummary);
      if (resultsList.size() > 0) {
        totalNumberOfTasksProcessed += Integer.parseInt(resultsList.item(0));
        totalNumberOfTasksSkipped += Integer.parseInt(resultsList.item(1));
      }
    }

    if (totalNumberOfUnprocessedChunks > 0) {

      final AppException errChunksSkippedText =
        new AppException(BDMVSGTASKALLOCATIONBATCH.ERR_CHUNKS_SKIPPED);

      errChunksSkippedText.arg(totalNumberOfUnprocessedChunks);
      errChunksSkippedText.arg(totalNumberOfUnprocessedChunks * kChunkSize);

      // append to StringBuffer
      emailMessage.append(CuramConst.gkNewLine)
        .append(errChunksSkippedText
          .getMessage(ProgramLocale.getDefaultServerLocale()))
        .append(CuramConst.gkNewLine);
    }

    // Create the text strings to send to the email report
    final AppException infTotalTasksText =
      new AppException(BDMVSGTASKALLOCATIONBATCH.INF_TASK_RECORDS_PROCESSED);

    infTotalTasksText.arg(totalNumberOfTasksProcessed);

    // append to StringBuffer
    emailMessage.append(CuramConst.gkNewLine)
      .append(
        infTotalTasksText.getMessage(ProgramLocale.getDefaultServerLocale()))
      .append(CuramConst.gkNewLine);

    if (totalNumberOfTasksSkipped > 0) {

      final AppException infTotalSkippedCasesText =
        new AppException(BDMVSGTASKALLOCATIONBATCH.INF_TASK_RECORDS_SKIPPED);

      infTotalSkippedCasesText.arg(totalNumberOfTasksSkipped);

      // append to StringBuffer
      emailMessage.append(CuramConst.gkNewLine)
        .append(infTotalSkippedCasesText
          .getMessage(ProgramLocale.getDefaultServerLocale()))
        .append(CuramConst.gkNewLine);
    }

    curamBatchObj.emailMessage = emailMessage.toString();

    // constructing the Email Subject
    curamBatchObj.setEmailSubject(
      BDMVSGTASKALLOCATIONBATCH.INF_BDM_VSG_TASK_ALLOCATION_SUBJECT);

    // set output file id
    curamBatchObj.outputFileID =
      BDMVSGTASKALLOCATIONBATCH.INF_BDM_VSG_TASK_ALLOCATION
        .getMessageText(ProgramLocale.getDefaultServerLocale());

    // set the elapsed time
    curamBatchObj.setStartTime(batchProcessDtls.startDateTime);
    curamBatchObj.setEndTime();

    // send email
    curamBatchObj.sendEmail();

  }

  private ChunkMainParameters getChunkMainParameters() {

    final ChunkMainParameters chunkMainParameters = new ChunkMainParameters();

    chunkMainParameters.chunkSize = Configuration
      .getIntProperty(EnvVars.BDM_SVG_TASK_ALLOCATION_BATCH_CHUNK_SIZE);
    chunkMainParameters.dontRunStream = Configuration.getBooleanProperty(
      EnvVars.BDM_SVG_TASK_ALLOCATION_BATCH_DONT_RUN_STREAM);
    chunkMainParameters.startChunkKey = Long.parseLong(Configuration
      .getProperty(EnvVars.BDM_SVG_TASK_ALLOCATION_BATCH_START_CHUNK_KEY));
    chunkMainParameters.unProcessedChunkReadWait =
      Configuration.getIntProperty(
        EnvVars.BDM_SVG_TASK_ALLOCATION_BATCH_UNPROCESSED_CHUNK_READ_WAIT);
    chunkMainParameters.processUnProcessedChunks =
      Configuration.getBooleanProperty(
        EnvVars.BDM_SVG_TASK_ALLOCATION_BATCH_PROCESS_UNPROCESSED_CHUNKS);

    return chunkMainParameters;
  }

  /**
   * retrieves all tasks from work queue for processing
   *
   * @return BatchProcessingIDList
   * @throws AppException
   * @throws InformationalException
   */
  private BatchProcessingIDList
    getBatchProcessingIDs(final BDMVSGTaskAllocationBatchKey key)
      throws AppException, InformationalException {

    final BatchProcessingIDList allTasks = new BatchProcessingIDList();
    BatchProcessingID batchProcessingID = null;

    // TODO: Retrieve tasks assigned to work queue
    final BDMTaskSearchKey bdmTaskSearchKey = new BDMTaskSearchKey();
    bdmTaskSearchKey.workQueueID = key.workQueueID;
    BDMTaskSearchResultList bdmTaskSearchResultList =
      new BDMTaskSearchResultList();
    bdmTaskSearchResultList = BDMGenericBatchProcessInputFactory.newInstance()
      .searchTasksByAssignedQueue(bdmTaskSearchKey);
    BDMTaskSearchResult bdmTaskSearchResult = null;
    final Iterator<BDMTaskSearchResult> bdmTaskResultListIT =
      bdmTaskSearchResultList.dtls.iterator();
    while (bdmTaskResultListIT.hasNext()) {
      bdmTaskSearchResult = new BDMTaskSearchResult();
      bdmTaskSearchResult = bdmTaskResultListIT.next();
      batchProcessingID = new BatchProcessingID();
      batchProcessingID.recordID = bdmTaskSearchResult.taskID;
      allTasks.dtls.add(batchProcessingID);
    }

    return allTasks;
  }

}
