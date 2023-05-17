/**
 *
 */
package curam.ca.gc.bdm.batch.bdmgcnotify.impl;

import curam.ca.gc.bdm.codetable.BDMGCNOTIFYALERTTYPE;
import curam.ca.gc.bdm.entity.bdmgcnotify.fact.BDMGcNotifyRequestDataFactory;
import curam.ca.gc.bdm.entity.bdmgcnotify.intf.BDMGcNotifyRequestData;
import curam.ca.gc.bdm.entity.bdmgcnotify.struct.BDMGcNotifyMetadataKey;
import curam.ca.gc.bdm.entity.bdmgcnotify.struct.BDMGcNotifyRequestDataDetails;
import curam.ca.gc.bdm.entity.bdmgcnotify.struct.BDMGcNotifyRequestDataDetailsList;
import curam.ca.gc.bdm.sl.interfaces.gcnotify.impl.BDMGCNotifyInterfaceImpl;
import curam.codetable.BATCHPROCESSNAME;
import curam.core.impl.BatchStreamHelper;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Trace;
import org.apache.commons.lang3.StringUtils;

/**
 * @author azar.khan
 *
 */
public class BDMGCNotifyDataBatchStream
  extends curam.ca.gc.bdm.batch.bdmgcnotify.base.BDMGCNotifyDataBatchStream {

  private BDMGCNotifyDataBatchStreamWrapper streamWrapper;

  @Override
  public void process(final BatchProcessStreamKey batchProcessStreamKey)
    throws AppException, InformationalException {

    streamWrapper = new BDMGCNotifyDataBatchStreamWrapper(this);

    final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();

    if (StringUtils.isEmpty(batchProcessStreamKey.instanceID)) {
      batchProcessStreamKey.instanceID = BATCHPROCESSNAME.BDM_GC_NOTIFY_DATA;
    }
    streamWrapper.setInstanceID(batchProcessStreamKey.instanceID);

    batchStreamHelper.runStream(batchProcessStreamKey, streamWrapper);

  }

  @Override
  public String getChunkResult(final int skippedCasesCount)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void processSkippedCases(
    final BatchProcessingSkippedRecordList batchProcessingSkippedRecordList)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub

  }

  /***
   * Call GCNotify email/phone bulk APIs
   * - Gets the relevant recordIDs by passing metadataID (batchProcessingID)
   * GCNotify
   * - Calls the GC Notify interface impl with eligible list *
   */
  @Override
  public BatchProcessingSkippedRecord
    processRecord(final BatchProcessingID batchProcessingID)
      throws AppException, InformationalException {

    final BDMGCNotifyInterfaceImpl bulkNotifyImplObj =
      new BDMGCNotifyInterfaceImpl();

    BatchProcessingSkippedRecord skippedRecord = null;
    try {
      final BDMGcNotifyRequestData gcNotifyRequestDataObj =
        BDMGcNotifyRequestDataFactory.newInstance();
      final BDMGcNotifyMetadataKey gcNotifyMetadataKeyObj =
        new BDMGcNotifyMetadataKey();
      gcNotifyMetadataKeyObj.metaDataID = batchProcessingID.recordID;
      Trace.kTopLevelLogger
        .info("BDMGCNotifyDataBatchStream processing Batch processing ID :"
          + batchProcessingID.recordID);
      final BDMGcNotifyRequestDataDetailsList gcNotifyList =
        gcNotifyRequestDataObj
          .readGCNotifyDataByMetadataID(gcNotifyMetadataKeyObj);

      if (gcNotifyList != null && gcNotifyList.dtls.size() > 0) {
        Trace.kTopLevelLogger.info(
          "BDMGCNotifyDataBatchStream list of records to be processed for GC bulk Notify :"
            + gcNotifyList.dtls.size());

        final BDMGcNotifyRequestDataDetails gcNotifyDetails =
          gcNotifyList.dtls.get(0);

        // call for bulk English emails API
        if (gcNotifyDetails.alertType
          .equals(BDMGCNOTIFYALERTTYPE.BDM_GCNOTIFY_EMAIL)) {
          final String gcNotifyEmailResponseJson =
            bulkNotifyImplObj.sendGCNotifyBulkEmailRequest(gcNotifyList);
          bulkNotifyImplObj.updateGCNotifyRecords(gcNotifyDetails.metaDataID,
            gcNotifyEmailResponseJson);
          // call for bulk Phone API
        } else if (gcNotifyDetails.alertType
          .equals(BDMGCNOTIFYALERTTYPE.BDM_GCNOTIFY_PHONE)) {
          final String gcNotifySMSResponseJson =
            bulkNotifyImplObj.sendGCNotifyBulkSMSRequest(gcNotifyList);
          bulkNotifyImplObj.updateGCNotifyRecords(gcNotifyDetails.metaDataID,
            gcNotifySMSResponseJson);

        }
      }
    } catch (final Exception e) {
      skippedRecord = new BatchProcessingSkippedRecord();
      skippedRecord.recordID = batchProcessingID.recordID;
      skippedRecord.errorMessage = e.getMessage();
      Trace.kTopLevelLogger.debug(
        "Error GCNotify Stream for record : " + batchProcessingID.recordID
          + " Error message : " + skippedRecord.errorMessage);
      e.printStackTrace();
    }

    return skippedRecord;

  }

  public void setWrapper(
    final BDMGCNotifyDataBatchStreamWrapper bdmgcNotifyDataBatchStreamWrapper) {

    // TODO Auto-generated method stub

  }

}
