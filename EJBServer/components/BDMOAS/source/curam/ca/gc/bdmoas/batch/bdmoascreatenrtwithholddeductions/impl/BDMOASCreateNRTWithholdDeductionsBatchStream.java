package curam.ca.gc.bdmoas.batch.bdmoascreatenrtwithholddeductions.impl;

import curam.ca.gc.bdm.entity.fact.BDMBatchHistoryFactory;
import curam.ca.gc.bdm.entity.struct.BDMBatchHistoryDtls;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.sl.financial.maintainliabilitycase.struct.ConcernRoleIDKey;
import curam.ca.gc.bdmoas.batch.bdmoascreatenrtwithholddeductions.struct.BDMOASCreateNRTWithholdDeductionsDetails;
import curam.ca.gc.bdmoas.batch.bdmoascreatenrtwithholddeductions.struct.BDMOASCreateNRTWithholdDeductionsSearchKey;
import curam.ca.gc.bdmoas.deductions.impl.BDMOASMaintainDeduction;
import curam.ca.gc.bdmoas.entity.oasdeduction.fact.BDMOASDeductionFactory;
import curam.codetable.BATCHPROCESSNAME;
import curam.core.impl.BatchStreamHelper;
import curam.core.impl.CuramConst;
import curam.core.struct.BatchProcessKey;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.core.struct.CaseHeaderKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.RecordNotFoundException;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.type.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.commons.lang3.StringUtils;

/**
 * This batch will create a new NRT Withhold deduction when NRT Withhold
 * deduction is end-dated because of
 * NRT Correction evidence or any unexpected conditions.
 *
 * This is a Streamer class which will be used to create NRT Withhold deduction
 * for the PDC Cases if the foreign address exists after the NRT correction end
 * date
 *
 * @author pranav.agarwal
 */
public class BDMOASCreateNRTWithholdDeductionsBatchStream extends
  curam.ca.gc.bdmoas.batch.bdmoascreatenrtwithholddeductions.base.BDMOASCreateNRTWithholdDeductionsBatchStream {

  // instance of stream wrapper class
  private BDMOASCreateNRTWithholdDeductionsBatchStreamWrapper streamWrapper;

  /**
   * This method is used to initiate the stream wrapper class.
   *
   * @param BDMOASCreateNRTWithholdDeductionsBatchStreamWrapper
   */
  public void setWrapper(
    final BDMOASCreateNRTWithholdDeductionsBatchStreamWrapper BDMOASCreateNRTWithholdDeductionsBatchStreamWrapper) {

    // assign the stream wrapper object here
    this.streamWrapper = BDMOASCreateNRTWithholdDeductionsBatchStreamWrapper;

  }

  /**
   * This method will be used to create NRT Withhold deduction for the PDC Cases
   * if the foreign address exists after the NRT correction end date
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
      new BDMOASCreateNRTWithholdDeductionsBatchStreamWrapper(this);

    // empty check for the batch instance ID
    if (StringUtils.isEmpty(batchProcessStreamKey.instanceID)) {
      // assign the batch name here
      batchProcessStreamKey.instanceID =
        BATCHPROCESSNAME.BDMOAS_CREATE_NRT_WITHHOLD_DEDUCTION_BATCH;
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
   * This method will be used to create NRT Withhold deduction for the PDC Cases
   * if the foreign address exists after the NRT correction end date
   *
   * @param batchProcessingSkippedRecordList - This holds the skipped records
   * list
   *
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
      // new instance of search key
      final BDMOASCreateNRTWithholdDeductionsSearchKey searchKey =
        new BDMOASCreateNRTWithholdDeductionsSearchKey();
      // populate the key
      searchKey.caseID = batchProcessingID.recordID;
      // new instance of simple date format
      final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
      // populate key object
      try {
        // start date will be the last batch run date
        final BatchProcessKey batchProcessKey = new BatchProcessKey();
        // populate the key object
        batchProcessKey.instanceID =
          BATCHPROCESSNAME.BDMOAS_CREATE_NRT_WITHHOLD_DEDUCTION_BATCH;
        // calling the method to read last batch run date by instance ID
        final BDMBatchHistoryDtls bdmBatchHistoryDtls = BDMBatchHistoryFactory
          .newInstance().readLastBatchRunDateByInstanceID(batchProcessKey);
        // assign the start date here
        searchKey.startDate =
          sdf.format(bdmBatchHistoryDtls.createdOn.getCalendar().getTime());

      } catch (final RecordNotFoundException recordNotFoundException) {
        // that means we are running the batch for the first time
        // assign the current date
        searchKey.startDate = sdf.format(new Date());
      }

      // end date will be after 3 months from the current date
      final Calendar cal = Calendar.getInstance();
      // add three months
      cal.add(Calendar.MONTH, 3);
      // assign the end date here
      searchKey.endDate = sdf.format(cal.getTime());

      // calling the method to check if the foreign address exists after the
      // NRT correction end date
      final BDMOASCreateNRTWithholdDeductionsDetails deductionDetails =
        BDMOASDeductionFactory.newInstance()
          .getDetailsToCreateNRTDeductionByPDCCaseID(searchKey);

      // input start date
      Date startDate = Date.kZeroDate;
      // check Whichever is later will be used as start date to create the NRT
      // deduction
      // Foreign Residential Address Start date
      // A day after NRT Correction Evidence end date.
      if (Date.getDate(deductionDetails.foreignAddressStartDate).after(Date
        .getDate(deductionDetails.nrtEndDate).addDays(CuramConst.gkOne))) {
        // set the residential address start date
        startDate = Date.getDate(deductionDetails.foreignAddressStartDate);
      } else {
        // set NRT deduction end date + 1 date
        startDate =
          Date.getDate(deductionDetails.nrtEndDate).addDays(CuramConst.gkOne);
      }

      // input end date
      Date endDate = Date.kZeroDate;
      // set the end date here
      // Set to - Foreign Residential Address end date.
      // If Foreign Residential Address End date is blank, then Deduction’s end
      // date will also be blank
      if (!StringUtil.isNullOrEmpty(deductionDetails.foreignAddressEndDate)) {
        // set the Foreign Residential Address End date here
        endDate = Date.getDate(deductionDetails.foreignAddressEndDate);
      }

      // new instance of case header
      final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
      // assign the PDC case ID
      caseHeaderKey.caseID = batchProcessingID.recordID;
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

      // log the message
      Trace.kTopLevelLogger
        .info(BDMConstants.kProcessed + batchProcessingID.recordID);
      // set skipped record back to null so will not be processed
      skippedRecord = null;

    } catch (final RecordNotFoundException rne) {
      // log the skipped record
      Trace.kTopLevelLogger.info(BDMConstants.kSkipped
        + "Foreign Residential Address does not exists after the NRT Correction evidence end date for the record - "
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
}
