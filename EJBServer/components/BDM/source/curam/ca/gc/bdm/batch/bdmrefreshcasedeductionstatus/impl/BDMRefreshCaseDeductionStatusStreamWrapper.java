/**
 *
 */
package curam.ca.gc.bdm.batch.bdmrefreshcasedeductionstatus.impl;

import curam.core.impl.BatchStream;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

/**
 * @author jigar.shah
 *
 */
public class BDMRefreshCaseDeductionStatusStreamWrapper
  implements BatchStream {

  private final curam.ca.gc.bdm.batch.bdmrefreshcasedeductionstatus.intf.BDMRefreshCaseDeductionStatusStream bdmRefreshCaseDeductionStatusStream;

  public BDMRefreshCaseDeductionStatusStreamWrapper(
    final curam.ca.gc.bdm.batch.bdmrefreshcasedeductionstatus.intf.BDMRefreshCaseDeductionStatusStream bdmRefreshCaseDeductionStatusStream) {

    this.bdmRefreshCaseDeductionStatusStream =
      bdmRefreshCaseDeductionStatusStream;
  }

  @Override
  public BatchProcessingSkippedRecord processRecord(
    final BatchProcessingID batchProcessingID, final Object parameters)
    throws AppException, InformationalException {

    return this.bdmRefreshCaseDeductionStatusStream
      .processRecord(batchProcessingID);
  }

  @Override
  public void processSkippedCases(
    final BatchProcessingSkippedRecordList batchProcessingSkippedRecordList)
    throws AppException, InformationalException {

    this.bdmRefreshCaseDeductionStatusStream
      .processSkippedCases(batchProcessingSkippedRecordList);

  }

  @Override
  public String getChunkResult(final int skippedCasesCount)
    throws AppException, InformationalException {

    return this.bdmRefreshCaseDeductionStatusStream
      .getChunkResult(skippedCasesCount);
  }

}
