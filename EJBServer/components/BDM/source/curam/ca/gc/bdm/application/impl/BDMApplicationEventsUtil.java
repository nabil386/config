package curam.ca.gc.bdm.application.impl;

import curam.ca.gc.bdm.impl.BDMConstants;
import curam.core.impl.CuramConst;
import curam.core.sl.entity.fact.ExternalUserFactory;
import curam.core.sl.entity.intf.ExternalUser;
import curam.core.sl.entity.struct.ExternalUserDtls;
import curam.core.sl.entity.struct.ExternalUserKey;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.type.NotFoundIndicator;
import curam.workspaceservices.intake.impl.IntakeApplication;
import curam.workspaceservices.util.impl.DatastoreHelper;

public class BDMApplicationEventsUtil {

  public BDMApplicationEventsUtil() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Get the entities of a given root entity ID and entity name.
   *
   * @param rootEntityID
   * root entity ID.
   * @param entityName
   * the name of the entity as defined in the schema
   * @return
   * an array of entities, which may be empty.
   */
  public static Entity[] getEntities(final long rootEntityID,
    final String entityName) {

    final Entity[] entities;

    final Datastore datastore = getDatastoreInstance();
    final Entity rootEntity = datastore.readEntity(rootEntityID);
    entities =
      rootEntity.getChildEntities(datastore.getEntityType(entityName));

    return entities;
  }

  /**
   * Get the entity of a given root entity ID.
   *
   * @param rootEntityID
   * root entity ID.
   * @param entityName
   * the name of the entity as defined in the schema
   * @return
   * an array of entities, which may be empty.
   */
  public static Entity getEntity(final long entityID) {

    Entity entity;
    final Datastore datastore = getDatastoreInstance();
    entity = datastore.readEntity(entityID);

    return entity;
  }

  /**
   * Get the datastore instance.
   *
   * @return The datastore instance.
   *
   * @throws NoSuchSchemaException
   */
  public static Datastore getDatastoreInstance() {

    try {
      final String schemaName = BDMDatastoreConstants.BDM_STIMULUS_SCHEMA;

      final Datastore datastore =
        DatastoreFactory.newInstance().openDatastore(schemaName);

      return datastore;

    } catch (final NoSuchSchemaException e) {
      Trace.kTopLevelLogger.info(
        BDMConstants.BDM_LOGS_PREFIX + "Could not find the related Schema ");
      e.printStackTrace();
    }

    return null;

  }

  /**
   * Get the root Entity for any given entity.
   *
   * @return The root entity instance.
   *
   * @throws NoSuchSchemaException
   */
  public static Entity getRootEntity(Entity searchEntity) {

    // compare the name of the current entity to the name of the root entity
    // until the root entity is found
    while (!searchEntity.getEntityType().getName()
      .equals(BDMDatastoreConstants.ROOT_ENTITY)) {
      searchEntity = searchEntity.getParentEntity();
    }

    return searchEntity;
  }

  /**
   * this method returns the root entity of an intakeapplication
   *
   * @param IntakeApplication
   * @return Entity
   */
  public static Entity getRootEntity(
    final IntakeApplication intakeApplication) throws InformationalException {

    final Datastore datastore =
      DatastoreHelper.openDatastore(intakeApplication.getSchemaName());
    return datastore.readEntity(intakeApplication.getRootEntityID());
  }

  /**
   * This method assumes there is only child for the parent entity and returns
   * the first child found, otherwise it returns a null object
   *
   * @param parentEntity
   * @param entityType
   * @return
   */
  public static Entity retrieveChildEntity(final Entity parentEntity,
    final String entityType) {

    Datastore datastore;
    datastore = getDatastoreInstance();
    final Entity[] childEntities =
      parentEntity.getChildEntities(datastore.getEntityType(entityType));
    if (childEntities.length >= 1) {
      return childEntities[0];
    }
    return null;
  }

  /**
   * This method returns the intake application date.
   *
   * @param datastore
   * datastore
   * @param applicationEntity
   * application entity
   * @return {@link String} returns intake application date else an empty string
   */
  public static String getIntakeApplicationStartDate(
    final Datastore datastore, final Entity applicationEntity) {

    // variable to store application start date
    String startDate = CuramConst.gkEmpty;

    // Get the intake applications entities
    final Entity[] intakeApplications = applicationEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.INTAKE_APPLICATION));

    if (intakeApplications != null && intakeApplications.length > 0) {
      final Entity intakeApplication = intakeApplications[0];
      // get the application date
      final String intakeStartDate = intakeApplication
        .getAttribute(BDMDatastoreConstants.DATE_OF_APPLICATION);
      if (!StringUtil.isNullOrEmpty(intakeStartDate))
        startDate = intakeStartDate;
    }
    return startDate;
  }

  /**
   * This method takes the person ID attribute from datastore and returns the
   * person name (concatenation of first name
   * and last name). This method is specifically for intake IEG application.
   *
   * @param datastore
   * : contains data related to entities
   * @param rootEntity
   * : root entity of application
   * @param personID
   * : Person ID attribute
   * @return returns the person name
   */
  public static String getPersonNameFromID(final Datastore datastore,
    final Entity rootEntity, final String personID) {

    // get the person entities
    final Entity[] people = rootEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.PERSON));

    for (final Entity person : people) {

      // if the person ID in parameter and the person ID of loop entity match,
      // get the name of person
      if (person.getUniqueID() == Long.parseLong(personID)) {

        final String firstname =
          person.getAttribute(BDMDatastoreConstants.FIRST_NAME).trim();
        final String lastname =
          person.getAttribute(BDMDatastoreConstants.LAST_NAME).trim();

        if (!StringUtil.isNullOrEmpty(lastname))
          return firstname + CuramConst.gkSpace + lastname;
        else
          return firstname;
      }
    }
    return CuramConst.gkEmpty;
  }

  /**
   * This method takes the person ID attribute from datastore and returns the
   * person's first name
   * This method is specifically for intake IEG application.
   *
   * @param datastore
   * - The datastore with data from the IEG
   * @param rootEntity
   * - The root entity of the application
   * @param personID
   * - The id of the person who's first name is being returned
   *
   * @return
   * - The first name of the person being retrieved
   */
  public static String getPersonFirstNameFromID(final Datastore datastore,
    final Entity rootEntity, final String personID) {

    // get the person entities
    final Entity[] people = rootEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.PERSON));

    for (final Entity person : people) {

      // if the person ID in parameter and the person ID of loop entity match,
      // get the name of person
      if (person.getUniqueID() == Long.parseLong(personID)) {

        final String firstname =
          person.getAttribute(BDMDatastoreConstants.FIRST_NAME).trim();

        return firstname;
      }
    }
    return CuramConst.gkEmpty;
  }

  /**
   * This method takes the external username and returns the
   * users details
   *
   * @param externalUserName - The username of the person who's role is being
   * returned, Can return NULL
   *
   * @return externalUserDetails
   *
   */

  public static ExternalUserDtls
    getExternalUserDetails(final String externalUserName)
      throws AppException, InformationalException {

    // initialise
    final ExternalUser externalUserObj = ExternalUserFactory.newInstance();
    ExternalUserDtls externalUserDtls = new ExternalUserDtls();
    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();

    // check if user exists, and if they exist get their details
    final ExternalUserKey externalUserKey = new ExternalUserKey();
    externalUserKey.userName = externalUserName;

    externalUserDtls =
      externalUserObj.read(notFoundIndicator, externalUserKey);

    // Handle and log if user is not an external account
    if (notFoundIndicator.isNotFound()) {
      Trace.kTopLevelLogger.warn(BDMConstants.BDM_LOGS_PREFIX
        + "User is not an external account: " + externalUserName);
    }

    return externalUserDtls;
  }

  /**
   * this method returns all occurances of child entities
   *
   * @param parentEntity
   * @param entityType
   * @return
   */
  public static Entity[] retrieveChildEntities(final Entity parentEntity,
    final String entityType) {

    Datastore datastore;
    datastore = getDatastoreInstance();
    final Entity[] childEntities =
      parentEntity.getChildEntities(datastore.getEntityType(entityType));
    if (childEntities.length >= 1) {
      return childEntities;
    }
    return null;
  }

  /**
   * 
   * @param parentEntity
   * @param entityType
   * @return
   */
  public static Boolean hasChildEntities(final Entity parentEntity,
    final String entityType) {

    Datastore datastore;
    datastore = getDatastoreInstance();
    final Entity[] childEntities =
      parentEntity.getChildEntities(datastore.getEntityType(entityType));
    if (childEntities.length >= 1) {
      return true;
    }
    return false;
  }

}
