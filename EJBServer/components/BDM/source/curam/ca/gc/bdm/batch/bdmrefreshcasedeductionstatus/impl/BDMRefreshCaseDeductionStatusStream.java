package curam.ca.gc.bdm.batch.bdmrefreshcasedeductionstatus.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMDEDUCTIONTYPE;
import curam.ca.gc.bdm.entity.deduction.fact.BDMDeductionFactory;
import curam.ca.gc.bdm.entity.deduction.intf.BDMDeduction;
import curam.ca.gc.bdm.entity.deduction.struct.BDMDeductionDetails;
import curam.ca.gc.bdm.entity.deduction.struct.BDMDeductionKey;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.sl.maintaincasedeductions.intf.MaintainCaseDeductions;
import curam.core.fact.CaseDeductionItemFactory;
import curam.core.impl.BatchStreamHelper;
import curam.core.intf.CaseDeductionItem;
import curam.core.sl.struct.CaseIDKey;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.core.struct.CaseDeductionItemDtls;
import curam.core.struct.CaseDeductionItemKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Trace;
import java.util.Arrays;

public abstract class BDMRefreshCaseDeductionStatusStream extends
  curam.ca.gc.bdm.batch.bdmrefreshcasedeductionstatus.base.BDMRefreshCaseDeductionStatusStream {

  @Inject
  MaintainCaseDeductions maintainCaseDeductionsObj;

  public BDMRefreshCaseDeductionStatusStream() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public void process(final BatchProcessStreamKey batchProcessStreamKey)
    throws AppException, InformationalException {

    final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();

    final BDMRefreshCaseDeductionStatusStreamWrapper streamWrapper =
      new BDMRefreshCaseDeductionStatusStreamWrapper(this);

    batchStreamHelper.runStream(batchProcessStreamKey, streamWrapper);

  }

  @Override
  public String getChunkResult(final int skippedCasesCount)
    throws AppException, InformationalException {

    // No implementation needed.
    return null;
  }

  @Override
  public void processSkippedCases(
    final BatchProcessingSkippedRecordList batchProcessingSkippedRecordList)
    throws AppException, InformationalException {

    // No implementation needed.

  }

  @Override
  public BatchProcessingSkippedRecord
    processRecord(final BatchProcessingID batchProcessingID)
      throws AppException, InformationalException {

    BatchProcessingSkippedRecord batchProcessingSkippedRecord = null;

    try {

      final CaseDeductionItem caseDeductionItemObj =
        CaseDeductionItemFactory.newInstance();
      final BDMDeduction bdmDeductionObj = BDMDeductionFactory.newInstance();
      final CaseDeductionItemKey caseDeductionItemKey =
        new CaseDeductionItemKey();
      final BDMDeductionKey bdmDeductionKey = new BDMDeductionKey();
      caseDeductionItemKey.caseDeductionItemID = batchProcessingID.recordID;

      final CaseDeductionItemDtls caseDeductionItemDtls =
        caseDeductionItemObj.read(caseDeductionItemKey);
      bdmDeductionKey.deductionID = caseDeductionItemDtls.deductionID;

      final BDMDeductionDetails bdmDeductionDetails =
        bdmDeductionObj.readByDeductionID(bdmDeductionKey);

      if (BDMDEDUCTIONTYPE.BASIC_PROVINCIAL_TAX
        .equals(bdmDeductionDetails.deductionType)) {
        final CaseIDKey caseIDKey = new CaseIDKey();
        caseIDKey.caseID = caseDeductionItemDtls.caseID;
        this.maintainCaseDeductionsObj.updateTaxDeductionStatus(caseIDKey);
        this.maintainCaseDeductionsObj.regenerateCaseFinancials(caseIDKey);
      } else if (BDMDEDUCTIONTYPE.VOLUNTARY_TAX_WITHHOLD_FED
        .equals(bdmDeductionDetails.deductionType)
        || BDMDEDUCTIONTYPE.VOLUNTARY_TAX_WITHHOLD_Prov
          .equals(bdmDeductionDetails.deductionType)) {
        final CaseIDKey caseIDKey = new CaseIDKey();
        caseIDKey.caseID = caseDeductionItemDtls.caseID;
        this.maintainCaseDeductionsObj
          .processStatusDueVTWDeduction(caseDeductionItemKey);
        this.maintainCaseDeductionsObj.regenerateCaseFinancials(caseIDKey);
      }
    } catch (final Exception e) {

      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + " Refresh Case Deduction Status Batch : Error occured during processing of "
        + batchProcessingID.recordID + " and Error is"
        + Arrays.toString(e.getStackTrace()));

      batchProcessingSkippedRecord = new BatchProcessingSkippedRecord();
      batchProcessingSkippedRecord.errorMessage = e.getMessage();
      batchProcessingSkippedRecord.recordID = batchProcessingID.recordID;
      batchProcessingSkippedRecord.stackTrace =
        Arrays.toString(e.getStackTrace());

    }

    return batchProcessingSkippedRecord;
  }

}
