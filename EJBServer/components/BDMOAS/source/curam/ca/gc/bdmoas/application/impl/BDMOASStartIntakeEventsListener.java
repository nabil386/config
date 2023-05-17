package curam.ca.gc.bdmoas.application.impl;

import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.application.impl.BDMStartIntakeEventsListener;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdmoas.impl.BDMOASConstants;
import curam.core.sl.entity.fact.ExternalUserFactory;
import curam.core.sl.entity.intf.ExternalUser;
import curam.core.sl.entity.struct.ExternalUserDtls;
import curam.core.sl.entity.struct.ExternalUserKey;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import curam.util.type.NotFoundIndicator;
import java.util.Arrays;

/**
 * BDMOAS FEATURE 92921: Class Added
 * Class created to populate some attributes in the
 * datastore before IEG script is launched.
 *
 * @author SMisal
 *
 */
public class BDMOASStartIntakeEventsListener
  extends BDMStartIntakeEventsListener {

  /**
   * Custom implementation of creating a new datastore for the IEG
   *
   * @param rootEntity
   * Datastore entity root of IEG script
   */
  @Override
  public void startIntake(final Entity rootEntity)
    throws AppException, InformationalException {

    // pre-populate ieg fields based on details provided on sign up
    try {
      this.prePopulateDetailsForOASGIS(rootEntity);
    } catch (final Exception e) {
      Trace.kTopLevelLogger.info(
        BDMOASConstants.ERROR_PREPOPULATING_INTAKE_DETAILS, e.getMessage());
    }
  }

  /**
   * This method creates and pre-populates fields in the IEG
   *
   * @param rootEntity Datastore entity root of IEG script
   * @throws AppException
   * @throws InformationalException
   */
  private void prePopulateDetailsForOASGIS(final Entity rootEntity)
    throws AppException, InformationalException, NoSuchSchemaException {
    // TODO: BEGIN BDMOAS Pre-poluate country of birth and date of death.

    // Get user name of the current user
    // Read details from ExternalUser table based on current user
    final String userName = TransactionInfo.getProgramUser();
    final ExternalUser externalUserObj = ExternalUserFactory.newInstance();
    final ExternalUserKey externalUserKey = new ExternalUserKey();
    externalUserKey.userName = userName;
    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();

    // try find the external user dtls
    final ExternalUserDtls externalUserDtls =
      externalUserObj.read(notFoundIndicator, externalUserKey);

    // set the application channel to UA, this is used to generate the overview
    // section in application PDF
    // rootEntity.setTypedAttribute("applicationChannel", "UA");

    // dont proceed if the ExternalUser is not found
    if (notFoundIndicator.isNotFound()) {
      rootEntity.update();
      return;
    }

    // Get BDM datastore
    final Datastore datastore = rootEntity.getDatastore();

    // Get and update Person Datastore from root
    Entity personEntity = null;
    final Entity[] personEntities = rootEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.PERSON));

    // replace the CitizenPortal Schema Person with the BDM_STIMULUS_SCHEMA
    // person
    if (personEntities.length > 0) {
      // get existing Person Datastore, Can assume only 1 exists
      personEntity = Arrays.stream(personEntities).iterator().next();
      personEntity.delete();
    }
    personEntity = datastore
      .newEntity(datastore.getEntityType(BDMDatastoreConstants.PERSON));
    rootEntity.addChildEntity(personEntity);

    final Long concernRoleID =
      super.getConcernRoleDtlsForExternalUser(externalUserDtls);

    // populate information based on linked External User
    if (concernRoleID != 0l && externalUserDtls.roleName
      .equals(BDMDatastoreConstants.LINKED_CITIZEN_ROLE)) {
      super.setPersonEntityAttributes(externalUserDtls, personEntity);
    }

    try {
      rootEntity.update();
    } catch (final Exception e) {
      Trace.kTopLevelLogger.warn(BDMConstants.BDM_LOGS_PREFIX
        + BDMOASConstants.ERROR_PREPOPULATING_ROOT_ENTITY
        + externalUserDtls.userName);
      e.printStackTrace();
    }
  }

}
