package curam.ca.gc.bdmoas.test.application.impl;

import curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventDatastoreConstants;
import curam.ca.gc.bdm.codetable.BDMCORRESDELIVERY;
import curam.ca.gc.bdmoas.application.impl.BDMOASDatastoreConstants;
import curam.ca.gc.bdmoas.application.impl.BDMOASIntakeApplicationMappingEvents;
import curam.ca.gc.bdmoas.test.evidence.BDMEvidenceUtilsTest;
import curam.ca.gc.bdmoas.test.rules.functions.CustomFunctionTestJunit4;
import curam.codetable.COUNTRY;
import curam.codetable.GENDER;
import curam.codetable.IEGYESNO;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import curam.workspaceservices.intake.impl.IntakeApplication;
import curam.workspaceservices.util.impl.DatastoreHelper;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/**
 * The Class tests {@link BDMOASIntakeApplicationMappingEvents} custom
 * function.
 */

@RunWith(JMockit.class)
public class BDMOASIntakeApplicationMappingEventsTest
  extends CustomFunctionTestJunit4 {

  private static final String SCHEMA_NAME = "BDMOASGISApplicationSchema";

  /** The entity functions. */

  @Mocked
  BDMApplicationEventsUtil BDMApplicationEventsUtil;

  @Mocked
  IntakeApplication intakeApplication;

  @Mocked
  DatastoreHelper DatastoreHelper;

  Datastore datastore;

  Entity applicationEntity;

  Entity personEntity;

  Entity communicationPrefEntity;

  Entity legalStatusEntity;

  /** The testing functions. */

  BDMEvidenceUtilsTest bdmEvidenceUtils;

  /** The validated function. */
  @Tested
  BDMOASIntakeApplicationMappingEvents BDMOASIntakeApplicationMappingEvents;

  public BDMOASIntakeApplicationMappingEventsTest() {

    super();
  }

  /**
   * Before each test, init the tested class and other objects.
   */
  @Before
  public void before() throws NoSuchSchemaException {

    bdmEvidenceUtils = new BDMEvidenceUtilsTest();
    datastore = DatastoreFactory.newInstance().openDatastore(SCHEMA_NAME);

  }


  // utility function to set up entities
  public void setUpEntities(final long concernRoleID)
    throws NoSuchSchemaException {

    applicationEntity =
      datastore.newEntity(BDMDatastoreConstants.APPLICATION);
    datastore.addRootEntity(applicationEntity);

    personEntity = datastore.newEntity(BDMDatastoreConstants.PERSON);
    personEntity.setAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT,
      "true");
    personEntity.setAttribute("localID", "" + concernRoleID);
    personEntity.setAttribute(BDMDatastoreConstants.LAST_NAME, "Doe");
    applicationEntity.addChildEntity(personEntity);

    legalStatusEntity =
      datastore.newEntity(BDMOASDatastoreConstants.LEGAL_STATUS_ENTITY);
    legalStatusEntity.setAttribute(
      BDMOASDatastoreConstants.OUT_OF_CA_BORNED_ATTRIBUTE, IEGYESNO.NO);
    personEntity.addChildEntity(legalStatusEntity);
    legalStatusEntity.update();

    communicationPrefEntity =
      datastore.newEntity(BDMDatastoreConstants.COMMUNICATION_PREF);
    communicationPrefEntity.setAttribute(
      BDMLifeEventDatastoreConstants.PREFERRED_SPOKEN_LANGUAGE, "EN");
    personEntity.addChildEntity(communicationPrefEntity);
    communicationPrefEntity.update();

    personEntity.update();
    applicationEntity.update();

  }

  /**
   * Test that all attributes set in preCreateCasesInCuram are done correctly.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_preCreateCasesInCuram()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    setUpEntities(pdcPersonDetails.concernRoleID);
    final Entity[] personEntities = {personEntity };
    new Expectations(datastore) {

      {
        curam.workspaceservices.util.impl.DatastoreHelper
          .openDatastore(intakeApplication.getSchemaName());
        result = datastore;

        datastore.readEntity(intakeApplication.getRootEntityID());
        result = applicationEntity;

        curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil
          .retrieveChildEntity(personEntity,
            BDMDatastoreConstants.COMMUNICATION_PREF);
        result = communicationPrefEntity;

        curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil
          .retrieveChildEntity(personEntity,
            BDMOASDatastoreConstants.LEGAL_STATUS_ENTITY);
        result = legalStatusEntity;

      }
    };

    BDMOASIntakeApplicationMappingEvents
      .preCreateCasesInCuram(intakeApplication);

    final Entity resultPersonEntity = applicationEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.PERSON))[0];

    assertEquals("Attribute was not set correctly.",
      communicationPrefEntity.getAttribute(
        BDMLifeEventDatastoreConstants.PREFERRED_WRITTEN_LANGUAGE),
      "EN");
    assertEquals("Attribute was not set correctly.",
      communicationPrefEntity
        .getAttribute(BDMLifeEventDatastoreConstants.RECEIVE_CORRESPONDENCE),
      BDMCORRESDELIVERY.POSTALDIG);
    assertEquals("Attribute was not set correctly.", resultPersonEntity
      .getAttribute(BDMLifeEventDatastoreConstants.LAST_NAME_AT_BIRTH),
      "Doe");
    assertEquals("Attribute was not set correctly.", resultPersonEntity
      .getAttribute(BDMOASDatastoreConstants.COUNTRY_OF_BIRTH), COUNTRY.CA);
    assertEquals("Attribute was not set correctly.",
      resultPersonEntity.getAttribute(BDMDatastoreConstants.GENDER),
      GENDER.UNKNOWN);
  }
}
