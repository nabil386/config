package curam.ca.gc.bdm.batch.bdmpaymentdestinationbatch.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.sl.financial.maintainpaymentdestination.intf.BDMMaintainPaymentDestination;
import curam.codetable.BATCHPROCESSNAME;
import curam.core.impl.BatchStreamHelper;
import curam.core.impl.CuramConst;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.core.struct.ConcernRoleKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.type.Date;

public class BDMPaymentDestinationBatchStream extends
  curam.ca.gc.bdm.batch.bdmpaymentdestinationbatch.base.BDMPaymentDestinationBatchStream {

  int totalProcessed = 0;

  private final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();

  @Inject
  BDMMaintainPaymentDestination maintainPaymentDestinationObj;

  public BDMPaymentDestinationBatchStream() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public void process(final BatchProcessStreamKey batchProcessStreamKey)
    throws AppException, InformationalException {

    if (StringUtil.isNullOrEmpty(batchProcessStreamKey.instanceID)) {
      batchProcessStreamKey.instanceID =
        BATCHPROCESSNAME.BDM_PAYMENT_DESTINATION_BATCH;
    }
    if (batchProcessStreamKey.processingDate.isZero()) {
      batchProcessStreamKey.processingDate = Date.getCurrentDate();
    }

    final BDMPaymentDestinationBatchStreamWrapper streamWrapper =
      new BDMPaymentDestinationBatchStreamWrapper(this);

    batchStreamHelper.runStream(batchProcessStreamKey, streamWrapper);

  }

  @Override
  public String getChunkResult(final int skippedCasesCount)
    throws AppException, InformationalException {

    final String chunkResult =
      totalProcessed + CuramConst.gkTabDelimiter + skippedCasesCount;

    // reset count
    totalProcessed = 0;

    return chunkResult;
  }

  @Override
  public BatchProcessingSkippedRecord
    processRecord(final BatchProcessingID batchProcessingID)
      throws AppException, InformationalException {

    BatchProcessingSkippedRecord skippedRecord = null;
    try {
      final ConcernRoleKey crKey = new ConcernRoleKey();
      crKey.concernRoleID = batchProcessingID.recordID;
      maintainPaymentDestinationObj.syncPaymentDestination(crKey);
      totalProcessed++;

    } catch (final Exception e) {
      skippedRecord = new BatchProcessingSkippedRecord();
      skippedRecord.recordID = batchProcessingID.recordID;
      skippedRecord.errorMessage = e.getMessage();

      e.printStackTrace();
    }

    return skippedRecord;
  }

  @Override
  public void processSkippedCases(
    final BatchProcessingSkippedRecordList batchProcessingSkippedRecordList)
    throws AppException, InformationalException {

  }

}
