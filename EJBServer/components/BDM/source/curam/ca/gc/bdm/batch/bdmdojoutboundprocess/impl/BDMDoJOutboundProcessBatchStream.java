package curam.ca.gc.bdm.batch.bdmdojoutboundprocess.impl;

import curam.ca.gc.bdm.entity.fact.BDMDoJOutboundStageFactory;
import curam.ca.gc.bdm.entity.intf.BDMDoJOutboundStage;
import curam.ca.gc.bdm.entity.struct.BDMDoJOutboundStageDtls;
import curam.ca.gc.bdm.entity.struct.BDMDoJOutboundStageKey;
import curam.ca.gc.bdm.sl.interfaces.bdmdojoutbound.impl.BDMDoJOutboundInterfaceImpl;
import curam.codetable.BATCHPROCESSNAME;
import curam.core.impl.BatchStreamHelper;
import curam.core.impl.CuramConst;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Trace;
import org.apache.commons.lang3.StringUtils;

public class BDMDoJOutboundProcessBatchStream extends
  curam.ca.gc.bdm.batch.bdmdojoutboundprocess.base.BDMDoJOutboundProcessBatchStream {

  protected int processedInstrumentsCount = 0;

  protected int skippedInstrumentsCount = 0;

  BDMDoJOutboundProcessBatchStreamWrapper streamWrapper;

  @Override
  public void process(final BatchProcessStreamKey streamKey)
    throws AppException, InformationalException {

    streamWrapper = new BDMDoJOutboundProcessBatchStreamWrapper(this);

    final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();

    if (StringUtils.isEmpty(streamKey.instanceID)) {
      streamKey.instanceID = BATCHPROCESSNAME.BDM_DOJ_OUTBOUND_DATA_PROCESS;
    }
    streamWrapper.setInstanceID(streamKey.instanceID);

    batchStreamHelper.runStream(streamKey, streamWrapper);

  }

  @Override
  public String getChunkResult(final int skippedCasesCount)
    throws AppException, InformationalException {

    final String chunkResult =
      processedInstrumentsCount + CuramConst.gkTabDelimiter
        + (skippedCasesCount + skippedInstrumentsCount);
    processedInstrumentsCount = 0;
    skippedInstrumentsCount = 0;
    return chunkResult;
  }

  @Override
  public void processSkippedCases(
    final BatchProcessingSkippedRecordList batchProcessingSkippedRecordList)
    throws AppException, InformationalException {

    for (final BatchProcessingSkippedRecord batchProcessingSkippedRecord : batchProcessingSkippedRecordList.dtls) {

      Trace.kTopLevelLogger.error(batchProcessingSkippedRecord.errorMessage
        + " " + batchProcessingSkippedRecord.recordID);
    }

  }

  @Override
  public BatchProcessingSkippedRecord
    processRecord(final BatchProcessingID batchProcessingID)
      throws AppException, InformationalException {

    final BatchProcessingSkippedRecord skippedRecord =
      new BatchProcessingSkippedRecord();

    final BDMDoJOutboundStage outStageObj =
      BDMDoJOutboundStageFactory.newInstance();
    final BDMDoJOutboundStageKey stageKey = new BDMDoJOutboundStageKey();
    stageKey.recordID = batchProcessingID.recordID;
    BDMDoJOutboundStageDtls stageDtls = new BDMDoJOutboundStageDtls();

    final BDMDoJOutboundInterfaceImpl dojInterObj =
      new BDMDoJOutboundInterfaceImpl();

    try {
      stageDtls = outStageObj.read(stageKey);

      processedInstrumentsCount++;
      // Call Interop Rest api
      dojInterObj.createObligationRequest(stageDtls,
        processedInstrumentsCount);

    } catch (final Exception e) {
      skippedRecord.errorMessage = e.getMessage();
      skippedRecord.recordID = batchProcessingID.recordID;
    }

    return null;
  }

  public void
    setWrapper(final BDMDoJOutboundProcessBatchStreamWrapper wrapper) {

    this.streamWrapper = wrapper;

  }

}
