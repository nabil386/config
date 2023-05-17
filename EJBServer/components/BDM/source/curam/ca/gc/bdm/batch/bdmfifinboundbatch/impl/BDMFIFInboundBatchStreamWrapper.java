package curam.ca.gc.bdm.batch.bdmfifinboundbatch.impl;

import curam.ca.gc.bdm.batch.bdmfifinboundbatch.struct.BDMFIFBatchParameters;
import curam.core.impl.BatchStream;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;

public class BDMFIFInboundBatchStreamWrapper implements BatchStream {

  /** The parameters which launched the program. */
  final BDMFIFBatchParameters batchParameters;

  /**
   * Constructor which keeps a reference to the stream which created us.
   *
   * @param streamObj The stream who created us.
   * @param params The parameters which were passed into the batch.
   */

  curam.ca.gc.bdm.batch.bdmfifinboundbatch.intf.BDMFIFInboundBatchStream streamObj;

  private String instanceID;

  public BDMFIFInboundBatchStreamWrapper(
    final curam.ca.gc.bdm.batch.bdmfifinboundbatch.intf.BDMFIFInboundBatchStream streamObj2,
    final BDMFIFBatchParameters params) {

    this.batchParameters = params;
    streamObj = streamObj2;

    ((BDMFIFInboundBatchStream) streamObj2).setWrapper(this);

    if (!params.startDate.isZero()) {
    } else {
      Date.getCurrentDate();
    }

  }

  /**
   * Does the business processing for one record.
   *
   * @param batchProcessingID Identifies the record to process.
   * @param parameters The program parameters.
   *
   * @return Information about this record if we have to skip it, otherwise
   * null to denote successful completion.
   */
  @Override
  public BatchProcessingSkippedRecord processRecord(
    final BatchProcessingID batchProcessingID, final Object parameters)
    throws AppException, InformationalException {

    return streamObj.processRecord(batchProcessingID);

  }

  /*
   * public void displayExecutionSummary() {
   *
   * final BDMFIFInboundBatchStream streamOBJ = new BDMFIFInboundBatchStream();
   * streamOBJ.displayExecutionSummary();
   * }
   */
  /**
   *
   */
  @Override
  public void processSkippedCases(
    final BatchProcessingSkippedRecordList skippedRecordList)
    throws AppException, InformationalException {

    streamObj.processSkippedCases(skippedRecordList);

  }

  @Override
  public String getChunkResult(final int skippedCasesCount)
    throws AppException, InformationalException {

    return streamObj.getChunkResult(skippedCasesCount);
  }

  public void setInstanceID(final String instanceID) {

    this.instanceID = instanceID;
  }

}
