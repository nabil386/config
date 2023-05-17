package curam.ca.gc.bdm.batch.bdmcancelpaymentbatch.impl;

import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.fact.BDMCancelPaymentStageFactory;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.intf.BDMCancelPaymentStage;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMCancelPaymentStageDtls;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMCancelPaymentStageKey;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.sl.financial.maintainpaymentinstrument.impl.MaintainPaymentInstrument;
import curam.codetable.BATCHPROCESSNAME;
import curam.core.impl.BatchStreamHelper;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import org.apache.commons.lang3.StringUtils;

public class BDMCancelPaymentBatchStream extends
  curam.ca.gc.bdm.batch.bdmcancelpaymentbatch.base.BDMCancelPaymentBatchStream {

  private BDMCancelPaymentBatchStreamWrapper streamWrapper;

  public void setWrapper(
    final BDMCancelPaymentBatchStreamWrapper bdmCancelPaymentBatchStreamWrapper) {

    this.streamWrapper = bdmCancelPaymentBatchStreamWrapper;

  }

  @Override
  public void process(final BatchProcessStreamKey batchProcessStreamKey)
    throws AppException, InformationalException {

    streamWrapper = new BDMCancelPaymentBatchStreamWrapper(this);

    final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();

    if (StringUtils.isEmpty(batchProcessStreamKey.instanceID)) {
      batchProcessStreamKey.instanceID =
        BATCHPROCESSNAME.BDM_CANCEL_PAYMENT_BATCH;
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

  @Override
  public BatchProcessingSkippedRecord
    processRecord(final BatchProcessingID batchProcessingID)
      throws AppException, InformationalException {

    final BatchProcessingSkippedRecord skippedRecord = null;

    final BDMCancelPaymentStage stage =
      BDMCancelPaymentStageFactory.newInstance();
    final BDMCancelPaymentStageKey stageKey = new BDMCancelPaymentStageKey();
    stageKey.recordID = batchProcessingID.recordID;

    final BDMCancelPaymentStageDtls dtls = stage.read(stageKey);

    final MaintainPaymentInstrument mpi = new MaintainPaymentInstrument();

    if (dtls.requisitionTypeCode.equals(BDMConstants.kREQUISITION_TYPE_2))
      mpi.cancelPaymentByPRN(dtls.payeeAccountNumber,
        dtls.paymentReferenceNumber);

    return skippedRecord;
  }

}
