package curam.ca.gc.bdmoas.test.application.impl;

import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdmoas.application.impl.BDMOASDataStoreEntityUtil;
import curam.ca.gc.bdmoas.application.impl.BDMOASDatastoreConstants;
import curam.ca.gc.bdmoas.test.evidence.BDMEvidenceUtilsTest;
import curam.ca.gc.bdmoas.test.rules.functions.CustomFunctionTestJunit4;
import curam.codetable.COUNTRY;
import curam.codetable.IEGYESNO;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
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
 * The Class tests {@link BDMOASDataStoreEntityUtil} custom
 * function.
 */

@RunWith(JMockit.class)
public class BDMOASDataStoreEntityUtilTest extends CustomFunctionTestJunit4 {

  private static final String SCHEMA_NAME = "BDMOASGISApplicationSchema";

  /** The entity functions. */
  @Mocked
  BDMEvidenceUtil bdmEvidenceUtil;

  Datastore datastore;

  Entity applicationEntity;

  Entity personEntity;

  /** The testing functions. */

  BDMEvidenceUtilsTest bdmEvidenceUtils;

  /** The validated function. */
  @Tested
  BDMOASDataStoreEntityUtil BDMOASDataStoreEntityUtil;

  public BDMOASDataStoreEntityUtilTest() {

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
  public void setUpEntities(final long concernRoleID, final String country)
    throws NoSuchSchemaException {

    applicationEntity =
      datastore.newEntity(BDMDatastoreConstants.APPLICATION);
    datastore.addRootEntity(applicationEntity);

    personEntity = datastore.newEntity(BDMDatastoreConstants.PERSON);
    personEntity.setAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT,
      "true");
    personEntity.setAttribute("localID", "" + concernRoleID);
    personEntity.setAttribute(BDMOASDatastoreConstants.COUNTRY_OF_BIRTH,
      country);
    applicationEntity.addChildEntity(personEntity);

    final Entity legalStatusEntity =
      datastore.newEntity(BDMOASDatastoreConstants.LEGAL_STATUS_ENTITY);
    personEntity.addChildEntity(legalStatusEntity);

    legalStatusEntity.update();
    personEntity.update();
    applicationEntity.update();

  }

  /**
   * Test that the getAndSetLegalStatusEntity when born in Canada.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_getAndSetLegalStatusEntity_BornInCanada()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    setUpEntities(pdcPersonDetails.concernRoleID, COUNTRY.CA);

    final Entity resultPersonEntity =
      BDMOASDataStoreEntityUtil.getAndSetLegalStatusEntity(personEntity,
        datastore, pdcPersonDetails.concernRoleID);
    final String result = resultPersonEntity.getChildEntities(datastore
      .getEntityType(BDMOASDatastoreConstants.LEGAL_STATUS_ENTITY))[0]
        .getAttribute(BDMOASDatastoreConstants.OUT_OF_CA_BORNED_ATTRIBUTE);

    assertEquals("Person should be born in Canada.", result, IEGYESNO.NO);
  }

  /**
   * Test that the getAndSetLegalStatusEntity when born outside of Canada.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_getAndSetLegalStatusEntity_BornOutsideCanada()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    setUpEntities(pdcPersonDetails.concernRoleID, COUNTRY.US);

    final Entity resultPersonEntity =
      BDMOASDataStoreEntityUtil.getAndSetLegalStatusEntity(personEntity,
        datastore, pdcPersonDetails.concernRoleID);
    final String result = resultPersonEntity.getChildEntities(datastore
      .getEntityType(BDMOASDatastoreConstants.LEGAL_STATUS_ENTITY))[0]
        .getAttribute(BDMOASDatastoreConstants.OUT_OF_CA_BORNED_ATTRIBUTE);

    assertEquals("Person should be born outside of Canada.", result,
      IEGYESNO.YES);
  }

  /**
   * Test that the getAndSetLegalStatusEntity when not given the legal entity
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_getAndSetLegalStatusEntity_CreateEntity()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    applicationEntity =
      datastore.newEntity(BDMDatastoreConstants.APPLICATION);
    datastore.addRootEntity(applicationEntity);

    personEntity = datastore.newEntity(BDMDatastoreConstants.PERSON);
    personEntity.setAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT,
      "true");
    personEntity.setAttribute("localID", "" + pdcPersonDetails.concernRoleID);
    personEntity.setAttribute(BDMOASDatastoreConstants.COUNTRY_OF_BIRTH,
      COUNTRY.CA);
    applicationEntity.addChildEntity(personEntity);
    personEntity.update();
    applicationEntity.update();

    final Entity resultPersonEntity =
      BDMOASDataStoreEntityUtil.getAndSetLegalStatusEntity(personEntity,
        datastore, pdcPersonDetails.concernRoleID);

    final String result = resultPersonEntity.getChildEntities(datastore
      .getEntityType(BDMOASDatastoreConstants.LEGAL_STATUS_ENTITY))[0]
        .getAttribute(BDMOASDatastoreConstants.OUT_OF_CA_BORNED_ATTRIBUTE);

    assertEquals("Person should be born in Canada.", result, IEGYESNO.NO);
  }
}
