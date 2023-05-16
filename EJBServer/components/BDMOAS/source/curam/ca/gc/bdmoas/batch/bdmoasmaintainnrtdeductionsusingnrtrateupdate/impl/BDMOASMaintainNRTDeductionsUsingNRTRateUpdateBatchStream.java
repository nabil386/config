package curam.ca.gc.bdmoas.batch.bdmoasmaintainnrtdeductionsusingnrtrateupdate.impl;

import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.sl.financial.maintainliabilitycase.struct.ConcernRoleIDKey;
import curam.ca.gc.bdmoas.batch.bdmoasmaintainnrtdeductionsusingnrtrateupdate.struct.BDMOASMaintainNRTDeductionsUsingNRTRateUpdateDetails;
import curam.ca.gc.bdmoas.deductions.impl.BDMOASMaintainDeduction;
import curam.ca.gc.bdmoas.entity.oasdeduction.fact.BDMOASDeductionFactory;
import curam.codetable.BATCHPROCESSNAME;
import curam.core.fact.CaseDeductionItemFactory;
import curam.core.impl.BatchStreamHelper;
import curam.core.impl.CuramConst;
import curam.core.intf.CaseDeductionItem;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.core.struct.CaseDeductionItemDtls;
import curam.core.struct.CaseDeductionItemKey;
import curam.core.struct.CaseHeaderKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.RecordNotFoundException;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.type.Date;
import org.apache.commons.lang3.StringUtils;

/**
 * This batch will be used to end date and create new NRT Withhold deductions
 * in event of the change in NRT rate due to change in Tax treaty between Canada
 * and other countries.
 *
 * This is a Streamer class which will be used to end date existing NRT Withhold
 * deduction with old rate and will create new NRT deduction with new rate if
 * foreign address exists on or after the effective date of NRT Rates
 * correction.
 *
 * This is a Streamer class which will be used create/update NRT Withhold
 * deduction
 * for the PDC Cases if there is change in NRT rate.
 *
 * @author pranav.agarwal
 */
public class BDMOASMaintainNRTDeductionsUsingNRTRateUpdateBatchStream extends
  curam.ca.gc.bdmoas.batch.bdmoasmaintainnrtdeductionsusingnrtrateupdate.base.BDMOASMaintainNRTDeductionsUsingNRTRateUpdateBatchStream {

  // instance of stream wrapper class
  private BDMOASMaintainNRTDeductionsUsingNRTRateUpdateBatchStreamWrapper streamWrapper;

  /**
   * This method is used to initiate the stream wrapper class.
   *
   * @param BDMOASMaintainNRTDeductionsUsingNRTRateUpdateBatchStreamWrapper
   */
  public void setWrapper(
    final BDMOASMaintainNRTDeductionsUsingNRTRateUpdateBatchStreamWrapper BDMOASMaintainNRTDeductionsUsingNRTRateUpdateBatchStreamWrapper) {

    // assign the stream wrapper object here
    this.streamWrapper =
      BDMOASMaintainNRTDeductionsUsingNRTRateUpdateBatchStreamWrapper;

  }

  /**
   * This method is used to end date existing NRT Withhold deduction with old
   * rate and will create new NRT deduction with new rate if foreign address
   * exists on or after the effective date of NRT Rates correction.
   *
   * @param batchProcessStreamKey - This holds the batch process key
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void process(final BatchProcessStreamKey batchProcessStreamKey)
    throws AppException, InformationalException {

    // new instance of stream wrapper class
    streamWrapper =
      new BDMOASMaintainNRTDeductionsUsingNRTRateUpdateBatchStreamWrapper(
        this);

    // empty check for the batch instance ID
    if (StringUtils.isEmpty(batchProcessStreamKey.instanceID)) {
      // assign the batch name here
      batchProcessStreamKey.instanceID =
        BATCHPROCESSNAME.BDMOAS_MAINTAIN_NRT_WITHHOLD_DEDUCTION_USING_NRT_RATE_BATCH;
    }

    // new instance of batch stream helper class
    final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();
    // invoke run stream here
    batchStreamHelper.runStream(batchProcessStreamKey, streamWrapper);

  }

  /**
   * This method is used to get the chunk results. It will be useful if you need
   * to generate the report for the batch run.
   *
   * @param skippedCasesCount - This holds the skipped case count
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public String getChunkResult(final int skippedCasesCount)
    throws AppException, InformationalException {

    // do nothing here
    return null;
  }

  /**
   * This method is used to process skipped case if any.
   *
   * @param batchProcessingSkippedRecordList - This holds the skipped records
   * list
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void processSkippedCases(
    final BatchProcessingSkippedRecordList batchProcessingSkippedRecordList)
    throws AppException, InformationalException {

    // do nothing here
  }

  /**
   * This method is used to end date existing NRT Withhold deduction with old
   * rate and will create new NRT deduction with new rate if foreign address
   * exists on or after the effective date of NRT Rates correction.
   *
   * @param batchProcessingID - This holds the batch processing ID
   *
   * @return the skipped records.
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BatchProcessingSkippedRecord
    processRecord(final BatchProcessingID batchProcessingID)
      throws AppException, InformationalException {

    // instance of return object
    BatchProcessingSkippedRecord skippedRecord =
      new BatchProcessingSkippedRecord();
    // assign the PDC Case ID
    skippedRecord.recordID = batchProcessingID.recordID;

    try {

      // read the case deduction item
      // new instance of key
      final CaseDeductionItemKey caseDeductionItemKey =
        new CaseDeductionItemKey();
      // populate the key object
      caseDeductionItemKey.caseDeductionItemID = batchProcessingID.recordID;

      // calling the method to read the details which will be used to end date
      // existing deduction with old rate and to create new one with new rate
      // Also it checks if any foreign residential address exists
      // on or after the effective date of NRT rate change
      final BDMOASMaintainNRTDeductionsUsingNRTRateUpdateDetails deductionDetails =
        BDMOASDeductionFactory.newInstance()
          .getDetailsToCreateNRTDeductionByCaseDeductionItemID(
            caseDeductionItemKey);

      // calling the method to end date existing NRT deduction with old rate
      endDateExistingNRTDeduction(caseDeductionItemKey,
        deductionDetails.effectiveDate);

      // calling the method to create new NRT deduction using new NRT rates
      createNewNRTDeductionWithNewNRTRates(deductionDetails);

      // log the message
      Trace.kTopLevelLogger
        .info(BDMConstants.kProcessed + batchProcessingID.recordID);
      // set skipped record back to null so will not be processed
      skippedRecord = null;

    } catch (final RecordNotFoundException rne) {
      // log the skipped record
      Trace.kTopLevelLogger.info(BDMConstants.kSkipped
        + "Foreign Residential Address does not exists on or after the effective date of NRT rate change for the record - "
        + batchProcessingID.recordID);

    } catch (final Exception e) {
      // print the stack trace
      e.printStackTrace();
      // log the skipped record
      Trace.kTopLevelLogger
        .info(BDMConstants.kSkipped + batchProcessingID.recordID);
    }

    // return skipped record instance
    return skippedRecord;
  }

  /**
   * This method is used to end date existing NRT deduction which has old NRT
   * rates.
   *
   * @param caseDeductionItemKey - This holds the case deduction item ID key
   * @param effectiveDate - This holds the effective date of NRT rates change
   * @throws AppException
   * @throws InformationalException
   */
  private void endDateExistingNRTDeduction(
    final CaseDeductionItemKey caseDeductionItemKey, final Date effectiveDate)
    throws AppException, InformationalException {

    // read the existing NRT deduction details
    // entity instance
    final CaseDeductionItem caseDeductionItemObj =
      CaseDeductionItemFactory.newInstance();
    // calling the entity method to read the deduction
    final CaseDeductionItemDtls caseDeductionItemDtls =
      caseDeductionItemObj.read(caseDeductionItemKey);

    // end date the existing NRT deduction on a day before effective date
    caseDeductionItemDtls.endDate =
      effectiveDate.addDays(CuramConst.gkMinusOne);
    // modify the case deduction item
    caseDeductionItemObj.modify(caseDeductionItemKey, caseDeductionItemDtls);
  }

  /**
   * This method will be used to create new NRT deduction with new NRT rates
   *
   * @param deductionDetails - This holds the details which will be used to
   * create new NRT deduction
   * @throws AppException
   * @throws InformationalException
   */
  private void createNewNRTDeductionWithNewNRTRates(
    final BDMOASMaintainNRTDeductionsUsingNRTRateUpdateDetails deductionDetails)
    throws AppException, InformationalException {

    // create new NRT deduction with the new rates
    // NRT rate change Effective date will be used as a start date
    final Date startDate = deductionDetails.effectiveDate;

    // input end date
    Date endDate = Date.kZeroDate;
    // get the end date
    // Set to - Foreign Residential Address end date.
    // If Foreign Residential Address End date is blank, then Deduction’s end
    // date will also be blank.
    if (!StringUtil.isNullOrEmpty(deductionDetails.foreignAddressEndDate)) {
      // set the Foreign Residential Address End date here
      endDate = Date.getDate(deductionDetails.foreignAddressEndDate);
    }

    // new instance of case header
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    // assign the PDC case ID
    caseHeaderKey.caseID = deductionDetails.caseID;
    // new instance of concern role key
    final ConcernRoleIDKey concernRoleIDKey = new ConcernRoleIDKey();
    // assign concern role ID
    concernRoleIDKey.concernRoleID = deductionDetails.concernRoleID;

    // new instance of maintain deduction
    final BDMOASMaintainDeduction caseDeductions =
      new BDMOASMaintainDeduction();
    // calling the method to create OAS NRT deduction
    caseDeductions.recordOASNRTDeductions(startDate, endDate, caseHeaderKey,
      concernRoleIDKey, deductionDetails.country, false,
      CuramConst.gkDoubleZero, CuramConst.kLongZero);

  }

}
