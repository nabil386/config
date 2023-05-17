package curam.ca.gc.bdm.util.integrity.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRRestResponse;
import curam.commonintake.impl.ApplicationCase;
import curam.commonintake.impl.ApplicationCaseDAO;
import curam.core.impl.EnvVars;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.participant.impl.ConcernRole;
import curam.participant.impl.ConcernRoleDAO;
import curam.util.exception.AppException;
import curam.util.exception.DateConversionException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.type.Date;
import java.util.Calendar;
import java.util.List;

/**
 * This class contains all the required methods for the SIN Date Verification
 * check process.
 *
 * @author alim.maredia
 *
 */
public class BDMSINIntegrityDateCheck {

  @Inject
  private ApplicationCaseDAO applicationCaseDAO;

  @Inject
  private ConcernRoleDAO concernRoleDAO;

  public BDMSINIntegrityDateCheck() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * This method takes in a BDMSINIntegrityCheckResult Object and determines
   * whether to raise any date verifications or not based on various checks
   *
   * @param checkResult
   * @throws AppException
   * @throws InformationalException
   */
  public void
    getSINDateVerificationCheck(final BDMSINIntegrityCheckResult checkResult)
      throws AppException, InformationalException {

    // Check if checkResult is null
    if (checkResult.getSIRResponse() == null) {
      return;
    }
    // Get the response object
    final BDMSINSIRRestResponse response = checkResult.getSIRResponse();
    // Check if response validation is not successful
    if (!response.isSINSIRValidatonSuccess()) {
      return;
    }
    final BDMSIRResponseDateCheckFlags dateValidationflags =
      calculateSINDateValidationFlags(checkResult);
    // isValiadtionFlag true means validation error, call trigger verification
    if (dateValidationflags.isValiadtionFlag()) {
      final BDMSINIntegrityCheckUtil bdmSINIntegrityCheckUtil =
        new BDMSINIntegrityCheckUtil();
      final EvidenceDescriptorKey evidenceDescriptorKey =
        new EvidenceDescriptorKey();
      // Raise a verification
      evidenceDescriptorKey.evidenceDescriptorID =
        checkResult.getDetails().getSinEvidenceDescriptorID();
      bdmSINIntegrityCheckUtil.triggerVerifiction(evidenceDescriptorKey);
    }
  }

  /**
   * This method takes in a BDMSINIntegrityCheckResult Object and calculates all
   * the dates related validation flags. The response needs to be checked by the
   * calling method.
   *
   * @param checkResult
   * @throws AppException
   * @throws InformationalException
   */
  public BDMSIRResponseDateCheckFlags calculateSINDateValidationFlags(
    final BDMSINIntegrityCheckResult checkResult)
    throws AppException, InformationalException {

    // return flags
    final BDMSIRResponseDateCheckFlags dateCheckFlags =
      new BDMSIRResponseDateCheckFlags();
    // Get the response object
    final BDMSINSIRRestResponse response = checkResult.getSIRResponse();
    // Get the concernRole from the concernRoleID
    final ConcernRole concernRole =
      concernRoleDAO.get(checkResult.getDetails().getConcernRoleID());
    // List the pending applications for that individual
    final List<ApplicationCase> applicationCaseList =
      applicationCaseDAO.listPendingByConcernRole(concernRole);
    // Get the SIN issuance date
    final Date sinIssueDate = getCuramDate(response.getValidatedSINResults()
      .getSsIndividual().getSsSIN().getSsIssueDate());
    // Initially set this variable to false and will change to true if any
    // checks pass
    // If there is an open application case for the individual, do the following
    if (!applicationCaseList.isEmpty()) {
      final ApplicationCase applicationCase = applicationCaseList.get(0);
      // Check if the difference between SIN issuance date and the application
      // date is lower than the Issue_App_Date threshold
      if (sinIssueDate != null && applicationCase.getApplicationDate()
        .subtract(sinIssueDate) < Configuration.getIntProperty(
          EnvVars.BDM_SININTEGRITYCHECK_ISSUE_APP_DATE_THRESHOLD)) {
        // Set the app date flag
        Trace.kTopLevelLogger.info("SIN Issue_App_Date validation failed");
        dateCheckFlags.setAppDateCheckFlag(true);
        dateCheckFlags.setValiadtionFlag(true);
      }

      // Get the dormant flag value
      final String dormantFlag = response.getValidatedSINResults()
        .getSsIndividual().getSsSIN().getSsDormantFlag();
      if (BDMIntegrityRulesUtil.flagCheckInResponseData(
        BDMSINIntegrityCheckConstants.SIR_FLAG_DORMANT_REACTIVATION,
        dormantFlag)) {
        // Get the SIN Reactivation Date
        final Date sinReactivationDate = getSinReactivateDate(dormantFlag,
          response.getValidatedSINResults().getSsIndividual().getSsSIN()
            .getSsDateDormantEffective(),
          BDMSINIntegrityCheckConstants.SIR_FLAG_DORMANT_REACTIVATION);
        // Check if the difference between SIN Reactivation date and the
        // application date is lower than the Reactivation_App_Date threshold
        if (sinReactivationDate != null
          && applicationCase.getApplicationDate()
            .subtract(sinReactivationDate) < Configuration.getIntProperty(
              EnvVars.BDM_SININTEGRITYCHECK_REACTIVATION_APPDATE_THRESHOLD)) {
          // Set SIN Reactivation_App_Date flag
          dateCheckFlags.setReactivationDateCheckFlag(true);
          dateCheckFlags.setValiadtionFlag(true);
          Trace.kTopLevelLogger
            .info("SIN Reactivation_App_Date validation failed");
        }
      }
    }
    // Check if the difference between the client DoB and the SIN issuance date
    // is lower than the Issue_DoB threshold
    if (sinIssueDate != null) {
      final Calendar dobc =
        getCuramDate(checkResult.getSIRRequest().getNcPersonBirthDate())
          .getCalendar();
      dobc.add(Calendar.YEAR, Configuration
        .getIntProperty(EnvVars.BDM_SININTEGRITYCHECK_ISSUE_DOB_THRESHOLD));
      final Date cutoffDate = Date.getFromJavaUtilDate(dobc.getTime());
      if (sinIssueDate.subtract(cutoffDate) > 0) {
        // Set SIN Issue_DoB flag
        dateCheckFlags.setDobDateCheckFlag(true);
        dateCheckFlags.setValiadtionFlag(true);
        Trace.kTopLevelLogger.info("SIN Issue_DoB validation failed");
      }
    }
    return dateCheckFlags;
  }

  /**
   * This method takes in a String value and converts that to a date. If the
   * string is invalid it will return null. Handle exception for the wrong
   * format of the date. Returns null if date format is not correct.
   *
   * @param dateStr
   * @return Date
   */
  private Date getCuramDate(final String dateStr) {

    // Retrieve date from String
    try {
      if (!StringUtil.isNullOrEmpty(dateStr)) {
        return Date.getDate(dateStr);
      }
    } catch (final DateConversionException e) {
      Trace.kTopLevelLogger.info(
        "Date conversion failed due to wrong or incomplete date passed : dateStr = "
          + dateStr);
    }
    return null;
  }

  /**
   * Get the SIN reactivate date from the response field
   *
   * @param dormantFlag
   * @param dormntFlageffectiveDate
   * @param flag
   * @return
   */
  private Date getSinReactivateDate(final String dormantFlag,
    final String dormntFlageffectiveDate, final int flag) {

    // check for non empty string
    if (!StringUtil.isNullOrEmpty(dormntFlageffectiveDate)
      && !StringUtil.isNullOrEmpty(dormantFlag)) {
      final String[] flagArray = dormantFlag.split(",");
      final String[] dateArray = dormntFlageffectiveDate.split(",");
      for (int i = 0; i < flagArray.length; i++) {
        final String strFlag = flagArray[i];
        // flag comparison
        if (strFlag.equals("" + flag)) {
          // get the date at same index from the date array
          if (dateArray.length > i) {
            // Retrieve date from String
            return getCuramDate(dateArray[i]);
          }
        }
      }
    }
    return null;
  }

}
