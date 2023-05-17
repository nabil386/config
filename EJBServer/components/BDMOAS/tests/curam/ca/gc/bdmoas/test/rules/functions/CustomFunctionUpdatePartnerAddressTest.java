package curam.ca.gc.bdmoas.test.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.codetable.IEGYESNO;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.ieg.impl.IEG2Context;
import curam.ieg.impl.IEGScriptExecution;
import curam.ieg.impl.IEGScriptExecutionFactory;
import curam.rules.functions.CustomFunctionUpdatePartnerAddress;
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
 * The Class tests {@link CustomFunctionUpdatePartnerAddress} custom
 * function.
 */

@RunWith(JMockit.class)
public class CustomFunctionUpdatePartnerAddressTest
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
  CustomFunctionUpdatePartnerAddress customFunction;

  public CustomFunctionUpdatePartnerAddressTest() {

    super();
  }

  /**
   * Before each test, init the tested class and other objects.
   */
  @Before
  public void before() throws NoSuchSchemaException {

    customFunction = new CustomFunctionUpdatePartnerAddress();
    ieg2Context = new IEG2Context();

  }

  /**
   * Test that the appropriate fields are set.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid()
    throws AppException, InformationalException, NoSuchSchemaException {

    // Set up entities
    final Datastore datastore = DatastoreFactory.newInstance()
      .openDatastore("BDMOASGISApplicationSchema");

    final Entity applicationEntity =
      datastore.newEntity(BDMDatastoreConstants.APPLICATION);
    datastore.addRootEntity(applicationEntity);

    final Entity personEntity =
      datastore.newEntity(BDMDatastoreConstants.PERSON);
    personEntity.setAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT,
      "false");
    personEntity.setAttribute("localID", "" + 3942034);
    personEntity.setAttribute(BDMDatastoreConstants.RESIDENTIAL_COUNTRY,
      "CA");
    personEntity.setAttribute(BDMDatastoreConstants.MAILING_COUNTRY, "CA");
    applicationEntity.addChildEntity(personEntity);

    final Entity residentialEntity =
      datastore.newEntity(BDMDatastoreConstants.RESIDENTIAL_ADDRESS);
    residentialEntity.setAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY,
      "US");
    personEntity.addChildEntity(residentialEntity);
    residentialEntity.update();

    final Entity intlResidentialEntity =
      datastore.newEntity(BDMDatastoreConstants.INTL_RESIDENTIAL_ADDRESS);
    intlResidentialEntity.setAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY,
      "US");
    personEntity.addChildEntity(intlResidentialEntity);
    intlResidentialEntity.update();

    final String addressData = "1\r\n" + "0\r\n" + "BDMINTL\r\n" + "CA\r\n"
      + "1\r\n" + "2\r\n" + "POBOXNO=\r\n" + "APT=4324\r\n" + "ADD1=434\r\n"
      + "ADD2=NLfante Plaza\r\n" + "CITY=Montreal\r\n" + "PROV=QC\r\n"
      + "STATEPROV=\r\n" + "BDMSTPROVX=\r\n" + "COUNTRY=CA\r\n"
      + "POSTCODE=B1B1B1\r\n" + "ZIP=\r\n" + "BDMZIPX=\r\n" + "";

    final Entity searchEntity =
      datastore.newEntity(BDMDatastoreConstants.SEARCH_ADDRESS);
    searchEntity.setAttribute(BDMDatastoreConstants.IS_ACTIVE, IEGYESNO.YES);
    searchEntity.setAttribute(BDMDatastoreConstants.IS_RESIDENTIAL_ADDRESS,
      IEGYESNO.YES);
    searchEntity.setAttribute(BDMDatastoreConstants.IS_SELECTED, "true");
    searchEntity.setAttribute(BDMDatastoreConstants.ADDRESS_DATA1,
      addressData);
    personEntity.addChildEntity(searchEntity);
    searchEntity.update();

    personEntity.update();
    applicationEntity.update();

    new Expectations(datastore) {

      {
        datastore.readEntity(anyLong);
        result = applicationEntity;

      }
    };

    final String addressType = BDMDatastoreConstants.RESIDENTIAL;
    final String step = BDMDatastoreConstants.STEP_1;
    final String streetNumber = "77";

    expectationsParameters(addressType, step, streetNumber);
    ieg2Context.setRootEntityID(applicationEntity.getUniqueID());
    final RulesParameters rulesParameters = ieg2Context;

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction
        .getAdaptorValue(rulesParameters);

    // Verify
    final Entity verifyPersonEntity = applicationEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.PERSON))[0];
    final Entity residentalAddress = verifyPersonEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.RESIDENTIAL_ADDRESS))[0];

    assertEquals(residentalAddress.getAttribute(BDMDatastoreConstants.CITY),
      "Montreal\r");
    assertEquals(
      residentalAddress.getAttribute(BDMDatastoreConstants.ADDRESS_LINE1),
      "434\r");
    assertEquals(
      residentalAddress.getAttribute(BDMDatastoreConstants.SUITE_NUM),
      "4324\r");
    assertEquals(
      residentalAddress.getAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY),
      "CA\r");
    assertEquals(
      residentalAddress.getAttribute(BDMDatastoreConstants.POSTAL_CODE),
      "B1B1B1\r");

  }
}
