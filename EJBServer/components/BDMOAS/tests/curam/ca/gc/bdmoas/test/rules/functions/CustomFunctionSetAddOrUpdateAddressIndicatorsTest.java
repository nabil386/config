package curam.ca.gc.bdmoas.test.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdmoas.application.impl.BDMOASDatastoreConstants;
import curam.codetable.COUNTRYCODE;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.ieg.impl.IEG2Context;
import curam.ieg.impl.IEGScriptExecution;
import curam.ieg.impl.IEGScriptExecutionFactory;
import curam.rules.functions.CustomFunctionSetAddOrUpdateAddressIndicators;
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
 * The Class tests {@link CustomFunctionSetAddOrUpdateAddressIndicators} custom
 * function.
 */

@RunWith(JMockit.class)
public class CustomFunctionSetAddOrUpdateAddressIndicatorsTest
  extends CustomFunctionTestJunit4 {

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The rulesParameters. */
  @Mocked
  IEG2ExecutionContext ieg2ExecutionContext;

  /** The script execution. */
  @Mocked
  IEGScriptExecution scriptExecution;

  /** The script execution factory. */
  @Mocked
  IEGScriptExecutionFactory scriptExecutionFactory;

  /** The validate function. */
  @Tested
  CustomFunctionSetAddOrUpdateAddressIndicators customFunction;

  Datastore datastore;

  Entity applicationEntity;

  Entity personEntity;

  public CustomFunctionSetAddOrUpdateAddressIndicatorsTest() {

    super();
  }

  /**
   * Before each test, init the tested class and other objects.
   */
  @Before
  public void before() throws NoSuchSchemaException {

    customFunction = new CustomFunctionSetAddOrUpdateAddressIndicators();
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
   * Test that the appropriate fields are set.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_ResidentialAddressAlreadyExists()
    throws AppException, InformationalException, NoSuchSchemaException {

    setUpEntities();

    final Entity searchCriEntity =
      datastore.newEntity(BDMOASDatastoreConstants.kSearchCriteriaResi);
    personEntity.addChildEntity(searchCriEntity);
    personEntity.update();
    applicationEntity.update();

    new Expectations(datastore) {

      {
        datastore.readEntity(anyLong);
        result = applicationEntity;

      }
    };

    final String country = COUNTRYCODE.CACODE;
    final String addressType = BDMDatastoreConstants.RESIDENTIAL;
    final String postalCode = "";

    expectationsParameters(country, addressType, postalCode);
    ieg2Context.setRootEntityID(applicationEntity.getUniqueID());
    final RulesParameters rulesParameters = ieg2Context;

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction
        .getAdaptorValue(rulesParameters);

    // Verify
    assertEquals(true, adaptorValue.getValue(ieg2Context));

    final Entity verifyPersonEntity = applicationEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.PERSON))[0];
    final Entity verifySearchCriEntity =
      verifyPersonEntity.getChildEntities(datastore
        .getEntityType(BDMOASDatastoreConstants.kSearchCriteriaResi))[0];

    assertEquals(false, verifyPersonEntity
      .getTypedAttribute(BDMOASDatastoreConstants.kAddOrUpdateResidAddress));

    assertEquals(false, verifySearchCriEntity
      .getTypedAttribute(BDMOASDatastoreConstants.kCaResiAddrSearchInd));
  }

  /**
   * Test that the appropriate fields are set.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_MailingAddressAlreadyExists()
    throws AppException, InformationalException, NoSuchSchemaException {

    setUpEntities();

    final Entity searchCriEntity =
      datastore.newEntity(BDMOASDatastoreConstants.kSearchCriteriaMail);
    personEntity.addChildEntity(searchCriEntity);
    personEntity.update();
    applicationEntity.update();

    new Expectations(datastore) {

      {
        datastore.readEntity(anyLong);
        result = applicationEntity;

      }
    };

    final String country = COUNTRYCODE.CACODE;
    final String addressType = BDMDatastoreConstants.MAILING;
    final String postalCode = "";

    expectationsParameters(country, addressType, postalCode);
    ieg2Context.setRootEntityID(applicationEntity.getUniqueID());
    final RulesParameters rulesParameters = ieg2Context;

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction
        .getAdaptorValue(rulesParameters);

    // Verify
    assertEquals(true, adaptorValue.getValue(ieg2Context));

    final Entity verifyPersonEntity = applicationEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.PERSON))[0];
    final Entity verifySearchCriEntity =
      verifyPersonEntity.getChildEntities(datastore
        .getEntityType(BDMOASDatastoreConstants.kSearchCriteriaMail))[0];

    assertEquals(false, verifyPersonEntity
      .getTypedAttribute(BDMOASDatastoreConstants.kAddOrUpdateMailAddress));

    assertEquals(false, verifySearchCriEntity
      .getTypedAttribute(BDMOASDatastoreConstants.kCaMailAddrSearchInd));
  }

  /**
   * Test that the appropriate fields are set.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_ResidentialAddressUpdate()
    throws AppException, InformationalException, NoSuchSchemaException {

    setUpEntities();

    final Entity searchCriEntity =
      datastore.newEntity(BDMOASDatastoreConstants.kSearchCriteriaResi);
    personEntity.addChildEntity(searchCriEntity);
    personEntity.update();
    applicationEntity.update();

    new Expectations(datastore) {

      {
        datastore.readEntity(anyLong);
        result = applicationEntity;

      }
    };

    final String country = COUNTRYCODE.CACODE;
    final String addressType = BDMDatastoreConstants.RESIDENTIAL;
    final String postalCode = "B1B1B1";

    expectationsParameters(country, addressType, postalCode);
    ieg2Context.setRootEntityID(applicationEntity.getUniqueID());
    final RulesParameters rulesParameters = ieg2Context;

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction
        .getAdaptorValue(rulesParameters);

    // Verify
    assertEquals(true, adaptorValue.getValue(ieg2Context));

    final Entity verifyPersonEntity = applicationEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.PERSON))[0];
    final Entity verifySearchCriEntity =
      verifyPersonEntity.getChildEntities(datastore
        .getEntityType(BDMOASDatastoreConstants.kSearchCriteriaResi))[0];

    assertEquals(true, verifyPersonEntity
      .getTypedAttribute(BDMOASDatastoreConstants.kAddOrUpdateResidAddress));
    assertEquals(COUNTRYCODE.CACODE, verifyPersonEntity
      .getTypedAttribute(BDMDatastoreConstants.RESIDENTIAL_COUNTRY));

    assertEquals(true, verifySearchCriEntity
      .getTypedAttribute(BDMOASDatastoreConstants.kCaResiAddrSearchInd));
  }

  /**
   * Test that the appropriate fields are set.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_MailingAddressUpdate()
    throws AppException, InformationalException, NoSuchSchemaException {

    setUpEntities();

    final Entity searchCriEntity =
      datastore.newEntity(BDMOASDatastoreConstants.kSearchCriteriaMail);
    personEntity.addChildEntity(searchCriEntity);
    personEntity.update();
    applicationEntity.update();

    new Expectations(datastore) {

      {
        datastore.readEntity(anyLong);
        result = applicationEntity;

      }
    };

    final String country = COUNTRYCODE.CACODE;
    final String addressType = BDMDatastoreConstants.MAILING;
    final String postalCode = "B1B1B1";

    expectationsParameters(country, addressType, postalCode);
    ieg2Context.setRootEntityID(applicationEntity.getUniqueID());
    final RulesParameters rulesParameters = ieg2Context;

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction
        .getAdaptorValue(rulesParameters);

    // Verify
    assertEquals(true, adaptorValue.getValue(ieg2Context));

    final Entity verifyPersonEntity = applicationEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.PERSON))[0];
    final Entity verifySearchCriEntity =
      verifyPersonEntity.getChildEntities(datastore
        .getEntityType(BDMOASDatastoreConstants.kSearchCriteriaMail))[0];

    assertEquals(true, verifyPersonEntity
      .getTypedAttribute(BDMOASDatastoreConstants.kAddOrUpdateMailAddress));
    assertEquals(COUNTRYCODE.CACODE, verifyPersonEntity
      .getTypedAttribute(BDMDatastoreConstants.MAILING_COUNTRY));

    assertEquals(true, verifySearchCriEntity
      .getTypedAttribute(BDMOASDatastoreConstants.kCaMailAddrSearchInd));
  }

  /**
   * Test that the appropriate fields are set.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_ResidentialCountryDiff()
    throws AppException, InformationalException, NoSuchSchemaException {

    setUpEntities();

    final Entity searchCriEntity =
      datastore.newEntity(BDMOASDatastoreConstants.kSearchCriteriaResi);
    personEntity.addChildEntity(searchCriEntity);
    personEntity.update();
    applicationEntity.update();

    new Expectations(datastore) {

      {
        datastore.readEntity(anyLong);
        result = applicationEntity;

      }
    };

    final String country = COUNTRYCODE.USCODE;
    final String addressType = BDMDatastoreConstants.RESIDENTIAL;
    final String postalCode = "";

    expectationsParameters(country, addressType, postalCode);
    ieg2Context.setRootEntityID(applicationEntity.getUniqueID());
    final RulesParameters rulesParameters = ieg2Context;

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction
        .getAdaptorValue(rulesParameters);

    // Verify
    assertEquals(true, adaptorValue.getValue(ieg2Context));

    final Entity verifyPersonEntity = applicationEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.PERSON))[0];
    final Entity verifySearchCriEntity =
      verifyPersonEntity.getChildEntities(datastore
        .getEntityType(BDMOASDatastoreConstants.kSearchCriteriaResi))[0];

    assertEquals(true, verifyPersonEntity
      .getTypedAttribute(BDMOASDatastoreConstants.kAddOrUpdateResidAddress));
    assertEquals(COUNTRYCODE.USCODE, verifyPersonEntity
      .getTypedAttribute(BDMDatastoreConstants.RESIDENTIAL_COUNTRY));

    assertEquals(false, verifySearchCriEntity
      .getTypedAttribute(BDMOASDatastoreConstants.kCaResiAddrSearchInd));
  }

  /**
   * Test that the appropriate fields are set.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_MailingCountryDiff()
    throws AppException, InformationalException, NoSuchSchemaException {

    setUpEntities();

    final Entity searchCriEntity =
      datastore.newEntity(BDMOASDatastoreConstants.kSearchCriteriaMail);
    personEntity.addChildEntity(searchCriEntity);
    personEntity.setTypedAttribute(BDMDatastoreConstants.RESIDENTIAL_COUNTRY,
      COUNTRYCODE.CACODE);
    personEntity.update();
    applicationEntity.update();

    new Expectations(datastore) {

      {
        datastore.readEntity(anyLong);
        result = applicationEntity;

      }
    };

    final String country = COUNTRYCODE.USCODE;
    final String addressType = BDMDatastoreConstants.MAILING;
    final String postalCode = "";

    expectationsParameters(country, addressType, postalCode);
    ieg2Context.setRootEntityID(applicationEntity.getUniqueID());
    final RulesParameters rulesParameters = ieg2Context;

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction
        .getAdaptorValue(rulesParameters);

    // Verify
    assertEquals(true, adaptorValue.getValue(ieg2Context));

    final Entity verifyPersonEntity = applicationEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.PERSON))[0];
    final Entity verifySearchCriEntity =
      verifyPersonEntity.getChildEntities(datastore
        .getEntityType(BDMOASDatastoreConstants.kSearchCriteriaMail))[0];

    assertEquals(true, verifyPersonEntity
      .getTypedAttribute(BDMOASDatastoreConstants.kAddOrUpdateMailAddress));
    assertEquals(COUNTRYCODE.USCODE, verifyPersonEntity
      .getTypedAttribute(BDMDatastoreConstants.MAILING_COUNTRY));

    assertEquals(false, verifySearchCriEntity
      .getTypedAttribute(BDMOASDatastoreConstants.kCaMailAddrSearchInd));
  }

}
