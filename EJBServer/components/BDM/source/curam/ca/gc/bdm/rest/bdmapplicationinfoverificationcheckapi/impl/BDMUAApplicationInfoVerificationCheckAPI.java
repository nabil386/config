package curam.ca.gc.bdm.rest.bdmapplicationinfoverificationcheckapi.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.rest.bdmapplicationinfoverificationcheckapi.struct.BDMUAApplicationInfoVerificationCheckDetails;
import curam.ca.gc.bdm.rest.bdmapplicationinfoverificationcheckapi.struct.BDMUAApplicationInfoVerificationCheckKey;
import curam.datastore.impl.Entity;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Trace;
import curam.util.type.NotFoundIndicator;
import curam.workspaceservices.intake.fact.IntakeApplicationFactory;
import curam.workspaceservices.intake.intf.IntakeApplication;
import curam.workspaceservices.intake.struct.IntakeApplicationDtls;
import curam.workspaceservices.intake.struct.IntakeApplicationKey;
import java.util.Objects;

public class BDMUAApplicationInfoVerificationCheckAPI implements
  curam.ca.gc.bdm.rest.bdmapplicationinfoverificationcheckapi.intf.BDMUAApplicationInfoVerificationCheckAPI {

  @Inject
  BDMApplicationEventsUtil bdmApplicationEventsUtil;

  // The income threshold below which verifications will be required
  private static int kIncomeLimit = 30000;

  /*
   * Check if the application will create an income verification for the
   * application
   */
  @Override
  public BDMUAApplicationInfoVerificationCheckDetails checkVerifications(
    final BDMUAApplicationInfoVerificationCheckKey appInfoVerificationCheckKey)
    throws AppException, InformationalException {

    final BDMUAApplicationInfoVerificationCheckDetails appInfoVerificationCheckDetails =
      new BDMUAApplicationInfoVerificationCheckDetails();
    appInfoVerificationCheckDetails.application_id =
      appInfoVerificationCheckKey.application_id;
    appInfoVerificationCheckDetails.verifications_will_exist = false;
    try {
      // Search for the intake application
      final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
      final IntakeApplication intakeApplicationObj =
        IntakeApplicationFactory.newInstance();
      final IntakeApplicationKey intakeApplicationkey =
        new IntakeApplicationKey();
      intakeApplicationkey.intakeApplicationID =
        appInfoVerificationCheckKey.application_id;
      final IntakeApplicationDtls intakeApplicationDtls =
        intakeApplicationObj.read(notFoundIndicator, intakeApplicationkey);
      if (notFoundIndicator.isNotFound()) {
        return appInfoVerificationCheckDetails;
      }
      final Entity rootEntity = BDMApplicationEventsUtil
        .getEntity(intakeApplicationDtls.rootEntityID);
      appInfoVerificationCheckDetails.verifications_will_exist =
        incomeWillCreateVerification(rootEntity);
      appInfoVerificationCheckDetails.added_by =
        getFullNameForApplicant(rootEntity);
    } catch (final Exception e) {
      Trace.kTopLevelLogger.trace(BDMConstants.BDM_LOGS_PREFIX
        + "Could not parse income from applcation datastore for verification check");
      e.printStackTrace();
      appInfoVerificationCheckDetails.verifications_will_exist = false;
      return appInfoVerificationCheckDetails;
    }
    return appInfoVerificationCheckDetails;
  }

  /*
   * Check if the application will create an income verification for the
   * application
   */
  private Boolean incomeWillCreateVerification(final Entity rootEntity) {

    if (Objects.isNull(rootEntity)) {
      return false;
    }
    try {
      final Entity personEntity = BDMApplicationEventsUtil
        .retrieveChildEntity(rootEntity, BDMDatastoreConstants.PERSON);
      final Entity incomeEntity =
        BDMApplicationEventsUtil.retrieveChildEntity(personEntity,
          BDMDatastoreConstants.INCOME_ENTITY);
      String incomeString =
        incomeEntity.getAttribute(BDMDatastoreConstants.INCOME_AMOUNT);
      incomeString = incomeString.replaceAll(",", "");
      final int incomeAmount = (int) Double.parseDouble(incomeString);
      if (incomeAmount < 0 || incomeAmount > kIncomeLimit) {
        return false;
      } else
        return true;
    } catch (final Exception e) {
      Trace.kTopLevelLogger.trace(BDMConstants.BDM_LOGS_PREFIX
        + "Could not parse income from applcation datastore for verification check");
      e.printStackTrace();
    }
    return false;
  }

  /*
   * Get the full name for the primary applicant
   */
  private String getFullNameForApplicant(final Entity rootEntity) {

    String fullName = "";
    if (Objects.isNull(rootEntity)) {
      return fullName;
    }
    try {
      final Entity personEntity = BDMApplicationEventsUtil
        .retrieveChildEntity(rootEntity, BDMDatastoreConstants.PERSON);
      final String firstName =
        personEntity.getAttribute(BDMDatastoreConstants.FIRST_NAME);
      final String lastName =
        personEntity.getAttribute(BDMDatastoreConstants.LAST_NAME);
      fullName = firstName + " " + lastName;
    } catch (final Exception e) {
      Trace.kTopLevelLogger.trace(BDMConstants.BDM_LOGS_PREFIX
        + "Could not parse fullname from applcation datastore for verification check");
    }
    return fullName;
  }

}
