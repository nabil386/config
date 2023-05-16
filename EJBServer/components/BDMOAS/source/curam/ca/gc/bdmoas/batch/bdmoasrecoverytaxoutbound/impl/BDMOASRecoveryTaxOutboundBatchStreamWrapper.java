package curam.ca.gc.bdmoas.batch.bdmoasrecoverytaxoutbound.impl;

import curam.core.impl.BatchStream;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMOASRecoveryTaxOutboundBatchStreamWrapper
  implements BatchStream {

  private final curam.ca.gc.bdmoas.batch.bdmoasrecoverytaxoutbound.intf.BDMOASRecoveryTaxOutboundBatchStream streamObj;

  public BDMOASRecoveryTaxOutboundBatchStreamWrapper(
    final curam.ca.gc.bdmoas.batch.bdmoasrecoverytaxoutbound.intf.BDMOASRecoveryTaxOutboundBatchStream stream) {

    streamObj = stream;
  }

  @Override
  public BatchProcessingSkippedRecord processRecord(
    final BatchProcessingID batchProcessingID, final Object parameters)
    throws AppException, InformationalException {

    return streamObj.processRecord(batchProcessingID);
  }

  @Override
  public void processSkippedCases(
    final BatchProcessingSkippedRecordList batchProcessingSkippedRecordList)
    throws AppException, InformationalException {

    streamObj.processSkippedCases(batchProcessingSkippedRecordList);

  }

  @Override
  public String getChunkResult(final int skippedCasesCount)
    throws AppException, InformationalException {

    return streamObj.getChunkResult(skippedCasesCount);
  }

}
