package curam.ca.gc.bdm.batch.bdmpurgeinprogressapplication.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.batch.bdmpurgeinprogressapplication.struct.BDMPurgeInProgressApplicationChunkResult;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.commonintake.facade.fact.ApplicationFormFactory;
import curam.commonintake.facade.intf.ApplicationForm;
import curam.commonintake.facade.struct.ApplicationFormVersionedKey;
import curam.commonintake.impl.ApplicationFormDAO;
import curam.core.impl.BatchStreamHelper;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Trace;
import java.util.Arrays;

/**
 *
 * @since ADO-9297
 *
 * @author teja.konda
 *
 */
public class BDMPurgeInProgressApplicationDataProcessStream extends
  curam.ca.gc.bdm.batch.bdmpurgeinprogressapplication.base.BDMPurgeInProgressApplicationDataProcessStream {

  protected static BDMPurgeInProgressApplicationChunkResult chunkResult =
    new BDMPurgeInProgressApplicationChunkResult();

  @Inject
  private ApplicationFormDAO applicationFormDAO;

  public BDMPurgeInProgressApplicationDataProcessStream() {

    GuiceWrapper.getInjector().injectMembers(this);

  }

  /**
   * This method runs the process of the streamer.
   *
   * @param batchProcessStreamKey
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void process(final BatchProcessStreamKey batchProcessStreamKey)
    throws AppException, InformationalException {

    final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();

    final BDMPurgeInProgressApplicationDataProcessStreamWrapper streamWrapper =
      new BDMPurgeInProgressApplicationDataProcessStreamWrapper(this);

    batchStreamHelper.runStream(batchProcessStreamKey, streamWrapper);
  }

  /**
   * This method gets the Chunk result.
   *
   * @param skippedCasesCount the number of skipped cases
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public String getChunkResult(final int skippedCasesCount)
    throws AppException, InformationalException {

    return null;
  }

  /**
   * Processes a record with a batch processing ID. If the last updated date of
   * the draft application is before 30 days of the current date
   * the state of the application is IN_PROGRESS, moving the status to cancelled
   *
   * @param batchProcessingID
   * @return BatchProcessingSkippedRecord
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BatchProcessingSkippedRecord
    processRecord(final BatchProcessingID batchProcessingID)
      throws AppException, InformationalException {

    final BatchProcessingSkippedRecord batchProcessingSkippedRecord =
      new BatchProcessingSkippedRecord();
    final ApplicationFormVersionedKey applicationFormVersionedKey =
      new ApplicationFormVersionedKey();
    applicationFormVersionedKey.applicationFormID =
      batchProcessingID.recordID;
    applicationFormVersionedKey.versionNo =
      applicationFormDAO.get(batchProcessingID.recordID).getVersionNo();

    final ApplicationForm applicationForm =
      ApplicationFormFactory.newInstance();

    try {

      applicationForm.cancelApplicationForm(applicationFormVersionedKey);

      chunkResult.totalCasesProcessedCount += 1;

    } catch (final Exception e) {

      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + " inprogress App Purge Batch : Error occured during processing of "
        + batchProcessingID.recordID + " and Error is"
        + Arrays.toString(e.getStackTrace()));

      batchProcessingSkippedRecord.errorMessage = e.getMessage();
      batchProcessingSkippedRecord.recordID = batchProcessingID.recordID;
      batchProcessingSkippedRecord.stackTrace =
        Arrays.toString(e.getStackTrace());
    }

    return null;
  }

  /**
   * This method processes the skipped cases.
   *
   * @param batchProcessingSkippedRecordList
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void processSkippedCases(
    final BatchProcessingSkippedRecordList batchProcessingSkippedRecordList)
    throws AppException, InformationalException {

  }

}
