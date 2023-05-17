/**
 *
 */
package curam.ca.gc.bdm.batch.bdmgcnotify.impl;

import curam.ca.gc.bdm.batch.bdmgcnotify.fact.BDMGCNotifyDataBatchStreamFactory;
import curam.ca.gc.bdm.batch.bdmgcnotify.struct.BDMGCNotifyDataBatchKey;
import curam.ca.gc.bdm.codetable.BDMGCNOTIFYINTERFACETYPE;
import curam.ca.gc.bdm.codetable.BDMGCNOTIFYNOTIFICATIONSTATUS;
import curam.codetable.BATCHPROCESSNAME;
import curam.core.facade.fact.UniqueIDFactory;
import curam.core.impl.BatchStreamHelper;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.sl.struct.RecordCount;
import curam.core.struct.BatchProcessChunkDtlsList;
import curam.core.struct.BatchProcessDtls;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingIDList;
import curam.core.struct.ChunkMainParameters;
import curam.util.dataaccess.CuramValueList;
import curam.util.dataaccess.DynamicDataAccess;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import org.apache.commons.lang3.StringUtils;

/**
 * @author azar.khan
 *
 */
public class BDMGCNotifyDataBatch
  extends curam.ca.gc.bdm.batch.bdmgcnotify.base.BDMGCNotifyDataBatch {

  /**
   * This is the size of records that can be processed in one GCNotify Bulk
   * request
   */
  private static final long kGCNotifyBulkSize = Configuration
    .getIntProperty(EnvVars.BDM_GCNotify_Bulk_Request_Size).longValue();

  private static curam.util.type.Date processingDate;

  private ChunkMainParameters getChunkMainParameters() {

    final ChunkMainParameters chunkMainParameters = new ChunkMainParameters();

    chunkMainParameters.chunkSize = Configuration
      .getIntProperty(EnvVars.BDM_CURAM_GCNOTIFY_BATCH_CHUNK_SIZE);
    chunkMainParameters.dontRunStream = Configuration
      .getBooleanProperty(EnvVars.BDM_GGCNOTIFYDATA_DONTRUNSTREAM);
    chunkMainParameters.startChunkKey = Long.parseLong(
      Configuration.getProperty(EnvVars.BDM_GCNOTIFYDATA_START_CHUNK_KEY));
    chunkMainParameters.unProcessedChunkReadWait =
      Configuration.getIntProperty(
        EnvVars.BDM_GGCNOTIFYDATA_UNPROCESSED_CHUNK_WAIT_INTERVAL);
    chunkMainParameters.processUnProcessedChunks =
      Configuration.getBooleanProperty(
        EnvVars.BDM_GGCNOTIFYDATA_PROCESS_UNPROCESSED_CHUNKS);

    return chunkMainParameters;
  }

  /**
   * Prepares records to be processed in Stream
   * - Populates the List of metadatIDs and updates the column "metadataID" of
   * the BDMGCNOTIFYREQUESTDATA
   * - BulkSize Property
   * -curam.ca.gc.bdm.batch.bdmgcnotifybatch.bulkrequest.size is set to number
   * of records to be processed in the Bulk GCNotify
   * - processingDate can be set to process the records which are on or before ,
   * or else current datatime is considered
   *
   *
   * * @param key
   * * @return
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void process(final BDMGCNotifyDataBatchKey key)
    throws AppException, InformationalException {

    final curam.ca.gc.bdm.batch.bdmgcnotify.intf.BDMGCNotifyDataBatchStream gcNotifyStreamObj =
      BDMGCNotifyDataBatchStreamFactory.newInstance();

    final BDMGCNotifyDataBatchStreamWrapper gcNotifyStreamWrapper =
      new BDMGCNotifyDataBatchStreamWrapper(gcNotifyStreamObj);

    final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();

    // List for eligible database records for notifications.
    final BatchProcessingIDList batchProcessingIDList =
      new BatchProcessingIDList();

    BatchProcessingIDList tempBatchProcessingIDList =
      new BatchProcessingIDList();

    final BDMGCNotifyDataBatchWrapper gcNotifyChunkerWrapper =
      new BDMGCNotifyDataBatchWrapper(this);

    if (key.processingDate != null && !key.processingDate.isZero()) {
      processingDate = key.processingDate;
    } else {
      processingDate = TransactionInfo.getSystemDate();
    }

    // Set the start time of the program
    batchStreamHelper.setStartTime();
    Trace.kTopLevelLogger.info("BDMGCNotifyDataBatch  start time : "
      + TransactionInfo.getSystemDateTime());

    final CuramValueList<BDMGCNotifyTemplateCount> gcNotifyTemplateCountList =
      getEligibleRecordCountForGCBulk();
    BatchProcessingID batchProcessingIDObj = new BatchProcessingID();
    for (final BDMGCNotifyTemplateCount gcNotifyTemplateCountObj : gcNotifyTemplateCountList) {
      if (gcNotifyTemplateCountObj.count > kGCNotifyBulkSize) {
        // If number of records exceeds the GCNotify limit, it must be broken
        // down to GC notify chunks for processing in Streamer
        // each chunk will have unique metadataid which is the batchprocessingID
        tempBatchProcessingIDList =
          processAboveBulkThreshholdRecords(gcNotifyTemplateCountObj);
        for (final BatchProcessingID tempBatchProcessingIDObj : tempBatchProcessingIDList.dtls) {
          batchProcessingIDList.dtls.add(tempBatchProcessingIDObj);
        }

      } else {
        // generate metadataid for records belonging to this particular
        // templateid , update metadataid to those templates and populate
        // batachprocessing list
        final long metaDataID =
          UniqueIDFactory.newInstance().getNextID().uniqueID;
        if (updateMetaDataID(gcNotifyTemplateCountObj.getTemplateID(),
          metaDataID, true)) {
          batchProcessingIDObj = new BatchProcessingID();
          batchProcessingIDObj.recordID = metaDataID;
          batchProcessingIDList.dtls.add(batchProcessingIDObj);
        }

      }
    }

    final String instanceID = !StringUtils.isEmpty(key.instanceID)
      ? key.instanceID : BATCHPROCESSNAME.BDM_GC_NOTIFY_DATA;

    final ChunkMainParameters chunkMainParameters = getChunkMainParameters();
    batchStreamHelper.runChunkMain(instanceID, key, gcNotifyChunkerWrapper,
      batchProcessingIDList, chunkMainParameters, gcNotifyStreamWrapper);

  }

  /**
   * Logic to breakdown the total eligible records into BulkSize for GCNotify
   * e.g if there exists 12k records, and bulk size property is set to 5k ,
   * there
   * will be 3
   * metadataIDs created for (5k,5k and 2k) . Each metadataID is a
   * batchProcessingID which
   * are processed in parallel by the BDMGCNotifyDataStream
   *
   * @param key
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  BatchProcessingIDList processAboveBulkThreshholdRecords(
    final BDMGCNotifyTemplateCount gcNotifyTemplateCountObj)
    throws AppException, InformationalException {

    final BatchProcessingIDList batchProcessingIDList =
      new BatchProcessingIDList();
    final long totalRecords = gcNotifyTemplateCountObj.count;

    if (totalRecords > 0) {
      final long bulk_processing_chunk = totalRecords / kGCNotifyBulkSize;
      Trace.kTopLevelLogger
        .info("BDMGCNotifyDataBatch bulk_processing_chunk :"
          + bulk_processing_chunk);
      int chunkCounter = 0;
      BatchProcessingID batchProcessingID;
      boolean isResidualRecords = false;
      while (chunkCounter <= bulk_processing_chunk) {
        Trace.kTopLevelLogger
          .info("BDMGCNotifyDataBatch chunkCounter :" + chunkCounter);
        final long metaDataID =
          UniqueIDFactory.newInstance().getNextID().uniqueID;
        // update the metadataid column for top most records decided by the
        // BulkSize, for the last iteration it will have fewer(residual) records
        // or No records
        if (chunkCounter == bulk_processing_chunk) {
          isResidualRecords = true;
        }
        // increment the chunkCounter
        chunkCounter++;
        if (updateMetaDataID(gcNotifyTemplateCountObj.getTemplateID(),
          metaDataID, isResidualRecords)) {
          // populate the MetaDataID as batchprocessingList for Stream
          batchProcessingID = new BatchProcessingID();
          batchProcessingID.recordID = metaDataID;
          batchProcessingIDList.dtls.add(batchProcessingID);
        }
      }
    }

    return batchProcessingIDList;

  }

  /**
   * Get eligible templateIDs and count from database based on search criteria
   * (status,
   * creation date , interfacetype and metadataID.
   *
   * @param
   * @return List<BDMGCNotifyTemplateCount>
   * @throws AppException
   * @throws InformationalException
   */
  public CuramValueList<BDMGCNotifyTemplateCount>
    getEligibleRecordCountForGCBulk()
      throws AppException, InformationalException {

    final BDMGCNotifyWhereClauseKey gcNotifyWhereClauseKey =
      new BDMGCNotifyWhereClauseKey();
    gcNotifyWhereClauseKey.processingDate = processingDate;

    final StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append(
      " SELECT   GC.TEMPLATEID , count(*) INTO :templateID , :count  FROM BDMGCNOTIFYREQUESTDATA GC  ");
    sqlBuilder.append(
      "  WHERE GC.STATUS = '" + BDMGCNOTIFYNOTIFICATIONSTATUS.UN_PROC + "' ");
    sqlBuilder.append("  AND  GC.INTERFACETYPE = '"
      + BDMGCNOTIFYINTERFACETYPE.BDM_GCNOTIFY_BATCH + "' ");
    sqlBuilder.append("  AND GC.METADATAID IS " + CuramConst.gkNull);
    sqlBuilder.append(" AND TRUNC(GC.CREATIONDATETIME) <=  :processingDate");
    sqlBuilder.append("  GROUP BY GC.TEMPLATEID");

    Trace.kTopLevelLogger
      .info("BDMGCNotifyDataBatch  SQL to get   TEMPLATE IDs and Count : "
        + sqlBuilder.toString());
    final CuramValueList<BDMGCNotifyTemplateCount> notifyTemplateIDCountList =
      DynamicDataAccess.executeNsMulti(BDMGCNotifyTemplateCount.class,
        gcNotifyWhereClauseKey, false, sqlBuilder.toString());

    return notifyTemplateIDCountList;

  }

  /**
   * Update metadataID for eligible records for notifications.
   *
   * @param metadataID
   * @throws AppException
   * @throws InformationalException
   */
  public boolean updateMetaDataID(final String templateID,
    final long metadataID, final boolean isResidualRecords)
    throws AppException, InformationalException {

    final BDMGCNotifyWhereClauseKey gcNotifyWhereClauseKey =
      new BDMGCNotifyWhereClauseKey();
    gcNotifyWhereClauseKey.processingDate = processingDate;

    boolean isUpdated = false;

    final StringBuilder sqlSelectBuilder = new StringBuilder();

    sqlSelectBuilder.append(
      "SELECT count(*) INTO :count FROM BDMGcNotifyRequestData gcn WHERE ");
    sqlSelectBuilder.append(
      " gcn.STATUS = '" + BDMGCNOTIFYNOTIFICATIONSTATUS.UN_PROC + "'");
    sqlSelectBuilder.append("  AND  gcn.INTERFACETYPE = '");
    sqlSelectBuilder
      .append(BDMGCNOTIFYINTERFACETYPE.BDM_GCNOTIFY_BATCH + "' ");
    sqlSelectBuilder.append("  AND gcn.TEMPLATEID =  '" + templateID + "'");
    sqlSelectBuilder.append("  AND gcn.METADATAID IS " + CuramConst.gkNull);
    sqlSelectBuilder
      .append(" AND TRUNC(gcn.CREATIONDATETIME) <= :processingDate");

    final RecordCount countForTemplateID =
      (RecordCount) DynamicDataAccess.executeNs(RecordCount.class,
        gcNotifyWhereClauseKey, false, sqlSelectBuilder.toString());

    Trace.kTopLevelLogger.info(
      "BDMGCNotifyDataBatch Number of records exists for a TemplateID to be updated :"
        + countForTemplateID.count);

    if (countForTemplateID.count > 0) {
      isUpdated = true;

      final StringBuilder sqlUpdateBuilder = new StringBuilder();
      sqlUpdateBuilder
        .append(" UPDATE BDMGcNotifyRequestData gc SET gc.METADATAID = ");
      sqlUpdateBuilder.append(metadataID + " WHERE gc.RECORDID  IN ( ");

      sqlUpdateBuilder.append(
        "SELECT gcn.RECORDID   FROM BDMGcNotifyRequestData gcn WHERE ");

      sqlUpdateBuilder.append(
        " gcn.STATUS = '" + BDMGCNOTIFYNOTIFICATIONSTATUS.UN_PROC + "'");
      sqlUpdateBuilder.append("  AND  gcn.INTERFACETYPE = '");
      sqlUpdateBuilder
        .append(BDMGCNOTIFYINTERFACETYPE.BDM_GCNOTIFY_BATCH + "' ");
      sqlUpdateBuilder.append("  AND gcn.TEMPLATEID =  '" + templateID + "'");
      sqlUpdateBuilder.append("  AND gcn.METADATAID IS " + CuramConst.gkNull);
      sqlUpdateBuilder
        .append("  AND TRUNC(gcn.CREATIONDATETIME) <=  :processingDate");
      if (!isResidualRecords) { // Doesn't apply for residual records
        sqlUpdateBuilder
          .append("  FETCH FIRST " + kGCNotifyBulkSize + " ROWS ONLY  ");
      }
      sqlUpdateBuilder.append(")");

      try {
        Trace.kTopLevelLogger
          .info("BDMGCNotifyDataBatch update metadata sql :"
            + sqlUpdateBuilder.toString());
        DynamicDataAccess.executeNs(null, gcNotifyWhereClauseKey, false,
          sqlUpdateBuilder.toString());
      } catch (final Exception e) {
        isUpdated = false;
      }

    }
    return isUpdated;

  }

  @Override
  public void sendBatchReport(final String instanceID,
    final BatchProcessDtls batchProcessDtls,
    final BatchProcessChunkDtlsList processedBatchProcessChunkDtlsList,
    final BatchProcessChunkDtlsList unprocessedBatchProcessChunkDtlsList)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub

  }

}
