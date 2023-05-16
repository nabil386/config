/**
 *
 */
package curam.ca.gc.bdm.batch.bdmtaxslipmrq.impl;

import curam.ca.gc.bdm.sl.interfaces.bdmtaxslipmrq.impl.BDMTaxSlipMrqBatchImpl;
import curam.codetable.BATCHPROCESSNAME;
import curam.core.impl.BatchStreamHelper;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Trace;
import org.apache.commons.lang.StringUtils;

/**
 * @author azar.khan
 *
 */
public class BDMTaxSlipMRQBatchStream
  extends curam.ca.gc.bdm.batch.bdmtaxslipmrq.base.BDMTaxSlipMRQBatchStream {

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

  /**
   * Calls BDMTaxSlipMrqBatchImpl.processRecords to process eligible records by
   * passing batchProcessingID
   * Return BatchProcessingSkippedRecord
   */
  @Override
  public BatchProcessingSkippedRecord
    processRecord(final BatchProcessingID batchProcessingID)
      throws AppException, InformationalException {

    final BDMTaxSlipMrqBatchImpl BDMTaxSlipMrqBatchImplObj =
      new BDMTaxSlipMrqBatchImpl();

    BatchProcessingSkippedRecord skippedRecord = null;

    try {
      BDMTaxSlipMrqBatchImplObj.processRecords(batchProcessingID);

    } catch (final Exception e) {
      skippedRecord = new BatchProcessingSkippedRecord();
      skippedRecord.recordID = batchProcessingID.recordID;
      skippedRecord.errorMessage = e.getMessage();
      Trace.kTopLevelLogger.debug(
        "Error MRQTaxSlip Stream for record : " + batchProcessingID.recordID
          + " Error message : " + skippedRecord.errorMessage);
      e.printStackTrace();
    }
    return skippedRecord;
  }

  @Override
  public void process(final BatchProcessStreamKey batchProcessStreamKey)
    throws AppException, InformationalException {

    final BDMTaxSlipMRQBatchStreamWrapper streamWrapper =
      new BDMTaxSlipMRQBatchStreamWrapper(this);
    ;
    final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();
    if (StringUtils.isEmpty(batchProcessStreamKey.instanceID)) {
      batchProcessStreamKey.instanceID =
        BATCHPROCESSNAME.BDM_TAXSLIP_MRQ_DATA_BATCH;
    }
    streamWrapper.setInstanceID(batchProcessStreamKey.instanceID);

    batchStreamHelper.runStream(batchProcessStreamKey, streamWrapper);

  }

  public void setWrapper(
    final BDMTaxSlipMRQBatchStreamWrapper bdmTaxSlipMRQBatchStreamWrapper) {

    // TODO Auto-generated method stub

  }

}
