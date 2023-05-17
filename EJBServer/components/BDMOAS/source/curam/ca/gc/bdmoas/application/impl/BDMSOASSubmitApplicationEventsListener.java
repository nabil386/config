package curam.ca.gc.bdmoas.application.impl;

import curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdmoas.impl.BDMOASConstants;
import curam.codetable.IEGYESNO;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.workspaceservices.applicationprocessing.impl.SubmitApplicationEvents;
import curam.workspaceservices.codetable.impl.ApplicationChannelEntry;
import curam.workspaceservices.codetable.impl.IntakeApplicationMethodEntry;
import curam.workspaceservices.intake.impl.IntakeApplication;
import curam.workspaceservices.util.impl.DatastoreHelper;

/**
 * BDMOAS FEATURE 92921: Class Added
 * Submit Application Events Listener class.
 *
 * @author SMisal
 *
 */
public class BDMSOASSubmitApplicationEventsListener
  implements SubmitApplicationEvents {

  private static final String PRIMARY_PARTICIPANT_BOOLEAN_EXP =
    "isPrimaryParticipant==true";

  @Override
  public void postSubmitApplication(final IntakeApplication intakeApplication)
    throws InformationalException, AppException {

	  //BEGIN BUG 110413
    final Entity rootEntity =
      BDMApplicationEventsUtil.getRootEntity(intakeApplication);

    final Datastore dataStore =
      DatastoreHelper.openDatastore(intakeApplication.getSchemaName());

    final Entity personEntity = rootEntity.getChildEntities(
      dataStore.getEntityType(BDMDatastoreConstants.PERSON),
      PRIMARY_PARTICIPANT_BOOLEAN_EXP)[0];

    if (StringUtil.isNullOrEmpty(personEntity
      .getAttribute(BDMDatastoreConstants.IS_MAILING_ADDRESS_DIFFERENT))) {
      personEntity.setTypedAttribute(
        BDMDatastoreConstants.IS_MAILING_ADDRESS_DIFFERENT, IEGYESNO.YES);
      personEntity.update();
    }   
    //END BUG 110413

    // Map application method to 'Paper' for OAS GIS Application.
    this.mapApplicationMethod(intakeApplication);
  }

  @Override
  public void preSubmitApplication(final Long rootEntityID,
    final String schemaName) throws InformationalException, AppException {

  }

  /**
   * Method to map application method.
   *
   * @param intakeApplication
   */
  public void mapApplicationMethod(final IntakeApplication intakeApplication)
    throws InformationalException, AppException {

    try {

      if (IntakeApplicationMethodEntry.ONLINE.getCode()
        .equals(intakeApplication.getApplicationMethod().getCode())) {
        return;
      }

      // BR-06 When OAS-GIS application is submitted then the Application Method
      // is mapped as ‘Paper’.
      final String intakeScriptID =
        intakeApplication.getIntakeApplicationType().getIntakeScriptID();

      if (intakeApplication.getIntakeApplicationType().getApplicationChannel()
        .equals(ApplicationChannelEntry.INTERNAL)
        && (intakeScriptID.equals(
          BDMOASDatastoreConstants.BDMOASGIS_COMBINED_APPLICATION_AP_SCRIPT)
          || intakeScriptID.equals(
            BDMOASDatastoreConstants.BDM_GIS_APPLICATION_AP_SCRIPT))) {
        intakeApplication
          .setApplicationMethod(IntakeApplicationMethodEntry.PAPER);
        intakeApplication.modify(intakeApplication.getVersionNo());
      }
    } catch (final Exception e) {
      Trace.kTopLevelLogger.info(BDMOASConstants.ERROR_POST_SUBMIT_INTAKE_APP,
        e.getMessage());
    }
  }

}
