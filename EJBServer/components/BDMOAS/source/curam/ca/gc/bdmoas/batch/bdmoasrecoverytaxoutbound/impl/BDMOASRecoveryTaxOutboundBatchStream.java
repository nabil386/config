package curam.ca.gc.bdmoas.batch.bdmoasrecoverytaxoutbound.impl;

import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;


public class BDMOASRecoveryTaxOutboundBatchStream implements
  curam.ca.gc.bdmoas.batch.bdmoasrecoverytaxoutbound.intf.BDMOASRecoveryTaxOutboundBatchStream {

  @Override
  public String getChunkResult(int skippedCasesCount)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void process(BatchProcessStreamKey batchProcessStreamKey)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub

  }

  @Override
  public BatchProcessingSkippedRecord
    processRecord(BatchProcessingID batchProcessingID)
      throws AppException, InformationalException {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void processSkippedCases(
    BatchProcessingSkippedRecordList batchProcessingSkippedRecordList)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub

  }

}
