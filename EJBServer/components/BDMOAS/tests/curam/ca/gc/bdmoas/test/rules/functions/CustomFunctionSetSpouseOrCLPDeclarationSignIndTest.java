package curam.ca.gc.bdmoas.test.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.codetable.BDMMARITALSTATUS;
import curam.ca.gc.bdmoas.application.impl.BDMOASDatastoreConstants;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionSetSpouseOrCLPDeclarationSignInd;
import curam.rules.functions.IEG2ExecutionContext;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/**
 * The Class tests {@link CustomFunctionSetSpouseOrCLPDeclarationSignIndTest}
 * custom
 * function.
 */

@RunWith(JMockit.class)
public class CustomFunctionSetSpouseOrCLPDeclarationSignIndTest
  extends CustomFunctionTestJunit4 {

  private static final boolean IS_VALID = true;

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The tested class. */
  @Tested
  CustomFunctionSetSpouseOrCLPDeclarationSignInd setSpouseOrCLPDeclarationSignInd;

  @Mocked
  IEG2ExecutionContext IEG2ExecutionContext;

  Datastore datastore;

  Entity applicationEntity;

  Entity personEntity;

  /**
   * Instantiates a new CustomFunctionSetSpouseOrCLPDeclarationSignIndTest.
   */
  public CustomFunctionSetSpouseOrCLPDeclarationSignIndTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    setSpouseOrCLPDeclarationSignInd =
      new CustomFunctionSetSpouseOrCLPDeclarationSignInd();
    ieg2Context = new IEG2Context();
  }

  public void setUpEntities() throws NoSuchSchemaException {

    // Set up entities
    datastore = DatastoreFactory.newInstance()
      .openDatastore("BDMOASGISApplicationSchema");

    applicationEntity =
      datastore.newEntity(BDMDatastoreConstants.APPLICATION);
    datastore.addRootEntity(applicationEntity);

    personEntity = datastore.newEntity(BDMDatastoreConstants.PERSON);
    personEntity.setAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT,
      "true");
    personEntity.setAttribute("localID", "" + 3942034);
    personEntity.setAttribute(BDMDatastoreConstants.RESIDENTIAL_COUNTRY,
      "CA");
    personEntity.setAttribute(BDMDatastoreConstants.MAILING_COUNTRY, "CA");
    applicationEntity.addChildEntity(personEntity);

    personEntity.update();
    applicationEntity.update();

  }

  /**
   * Test true when spouse details filled
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_SetSpouseOrCLPDeclarationSignInd_True()
    throws AppException, InformationalException, NoSuchSchemaException {

    setUpEntities();

    personEntity.setTypedAttribute(BDMOASDatastoreConstants.SPOUSE_OR_CLP_IND,
      true);
    personEntity.update();
    applicationEntity.update();

    new Expectations(datastore) {

      {
        datastore.readEntity(anyLong);
        result = applicationEntity;

      }
    };

    ieg2Context.setRootEntityID(applicationEntity.getUniqueID());
    final RulesParameters rulesParameters = ieg2Context;

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) setSpouseOrCLPDeclarationSignInd
        .getAdaptorValue(rulesParameters);

    // Verify
    assertEquals(true, adaptorValue.getValue(ieg2Context));

    final Entity verifyPersonEntity = applicationEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.PERSON))[0];

    assertEquals(BDMDatastoreConstants.TRUE, verifyPersonEntity
      .getAttribute(BDMOASDatastoreConstants.SPOUSE_OR_CLP_DECL_SIGN_IND));

  }

  /**
   * Test true when marital status is not single
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_SetSpouseOrCLPDeclarationSignInd_True2()
    throws AppException, InformationalException, NoSuchSchemaException {

    setUpEntities();

    final Entity maritalStatusEntity = datastore
      .newEntity(BDMOASDatastoreConstants.MARITAL_STATUS_INFO_ENTITY);
    maritalStatusEntity.setAttribute(
      BDMOASDatastoreConstants.MARITAL_STATUS_INFO_CURNTSTATUS_ATTR,
      BDMMARITALSTATUS.MARRIED);
    personEntity.addChildEntity(maritalStatusEntity);
    personEntity.update();
    applicationEntity.update();

    new Expectations(datastore) {

      {
        datastore.readEntity(anyLong);
        result = applicationEntity;

      }
    };

    ieg2Context.setRootEntityID(applicationEntity.getUniqueID());
    final RulesParameters rulesParameters = ieg2Context;

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) setSpouseOrCLPDeclarationSignInd
        .getAdaptorValue(rulesParameters);

    // Verify
    assertEquals(true, adaptorValue.getValue(ieg2Context));

    final Entity verifyPersonEntity = applicationEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.PERSON))[0];

    assertEquals(BDMDatastoreConstants.TRUE, verifyPersonEntity
      .getAttribute(BDMOASDatastoreConstants.SPOUSE_OR_CLP_DECL_SIGN_IND));
  }

  /**
   * Test false when spouse details not filled.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_SetSpouseOrCLPDeclarationSignInd_False()
    throws AppException, InformationalException, NoSuchSchemaException {

    setUpEntities();

    personEntity.setTypedAttribute(BDMOASDatastoreConstants.SPOUSE_OR_CLP_IND,
      false);
    applicationEntity.update();

    new Expectations(datastore) {

      {
        datastore.readEntity(anyLong);
        result = applicationEntity;

      }
    };

    ieg2Context.setRootEntityID(applicationEntity.getUniqueID());
    final RulesParameters rulesParameters = ieg2Context;

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) setSpouseOrCLPDeclarationSignInd
        .getAdaptorValue(rulesParameters);

    // Verify
    assertEquals(true, adaptorValue.getValue(ieg2Context));

    final Entity verifyPersonEntity = applicationEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.PERSON))[0];

    assertEquals(BDMOASDatastoreConstants.FALSE, verifyPersonEntity
      .getAttribute(BDMOASDatastoreConstants.SPOUSE_OR_CLP_DECL_SIGN_IND));

  }

  /**
   * Test false when marital status is single
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_SetSpouseOrCLPDeclarationSignInd_False2()
    throws AppException, InformationalException, NoSuchSchemaException {

    setUpEntities();

    final Entity maritalStatusEntity = datastore
      .newEntity(BDMOASDatastoreConstants.MARITAL_STATUS_INFO_ENTITY);
    maritalStatusEntity.setAttribute(
      BDMOASDatastoreConstants.MARITAL_STATUS_INFO_CURNTSTATUS_ATTR,
      BDMMARITALSTATUS.SINGLE);
    personEntity.addChildEntity(maritalStatusEntity);
    personEntity.update();
    applicationEntity.update();

    new Expectations(datastore) {

      {
        datastore.readEntity(anyLong);
        result = applicationEntity;

      }
    };

    ieg2Context.setRootEntityID(applicationEntity.getUniqueID());
    final RulesParameters rulesParameters = ieg2Context;

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) setSpouseOrCLPDeclarationSignInd
        .getAdaptorValue(rulesParameters);

    // Verify
    assertEquals(true, adaptorValue.getValue(ieg2Context));

    final Entity verifyPersonEntity = applicationEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.PERSON))[0];

    assertEquals(BDMOASDatastoreConstants.FALSE, verifyPersonEntity
      .getAttribute(BDMOASDatastoreConstants.SPOUSE_OR_CLP_DECL_SIGN_IND));
  }

}
