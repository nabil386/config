package curam.ca.gc.bdmoas.test.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdmoas.application.impl.BDMOASDatastoreConstants;
import curam.codetable.IEGYESNO;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.ieg.impl.IEG2Context;
import curam.ieg.impl.IEGScriptExecution;
import curam.ieg.impl.IEGScriptExecutionFactory;
import curam.rules.functions.CustomFunctionUpdateWitnessAddress;
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
 * The Class tests {@link CustomFunctionUpdateWitnessAddress} custom
 * function.
 */

@RunWith(JMockit.class)
public class CustomFunctionUpdateWitnessAddressTest
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
  CustomFunctionUpdateWitnessAddress customFunction;

  public CustomFunctionUpdateWitnessAddressTest() {

    super();
  }

  /**
   * Before each test, init the tested class and other objects.
   */
  @Before
  public void before() throws NoSuchSchemaException {

    customFunction = new CustomFunctionUpdateWitnessAddress();
    ieg2Context = new IEG2Context();

  }

  /**
   * Test that the appropriate fields are set.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_mailing()
    throws AppException, InformationalException, NoSuchSchemaException {

    // Set up entities
    final Datastore datastore = DatastoreFactory.newInstance()
      .openDatastore("BDMOASGISApplicationSchema");

    final Entity applicationEntity =
      datastore.newEntity(BDMDatastoreConstants.APPLICATION);
    datastore.addRootEntity(applicationEntity);

    final Entity witnessEntity =
      datastore.newEntity(BDMOASDatastoreConstants.DECLARATION_ENTITY);
    witnessEntity.setAttribute(BDMDatastoreConstants.MAILING_COUNTRY, "CA");
    applicationEntity.addChildEntity(witnessEntity);

    final Entity mailingEntity =
      datastore.newEntity(BDMDatastoreConstants.MAILING_ADDRESS);
    mailingEntity.setAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY, "US");
    witnessEntity.addChildEntity(mailingEntity);
    mailingEntity.update();

    final Entity intlMailingEntity =
      datastore.newEntity(BDMDatastoreConstants.INTL_MAILING_ADDRESS);
    intlMailingEntity.setAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY,
      "US");
    witnessEntity.addChildEntity(intlMailingEntity);
    intlMailingEntity.update();

    final String addressData = "1\r\n" + "0\r\n" + "BDMINTL\r\n" + "CA\r\n"
      + "1\r\n" + "2\r\n" + "POBOXNO=\r\n" + "APT=4324\r\n" + "ADD1=434\r\n"
      + "ADD2=NLfante Plaza\r\n" + "CITY=Montreal\r\n" + "PROV=QC\r\n"
      + "STATEPROV=\r\n" + "BDMSTPROVX=\r\n" + "COUNTRY=CA\r\n"
      + "POSTCODE=B1B1B1\r\n" + "ZIP=\r\n" + "BDMZIPX=\r\n" + "";

    final Entity searchEntityMailing =
      datastore.newEntity(BDMDatastoreConstants.SEARCH_ADDRESS);
    searchEntityMailing.setAttribute(BDMDatastoreConstants.IS_ACTIVE,
      IEGYESNO.YES);
    searchEntityMailing.setAttribute(BDMDatastoreConstants.IS_MAILING_ADDRESS,
      IEGYESNO.YES);
    searchEntityMailing.setAttribute(BDMDatastoreConstants.IS_SELECTED,
      "true");
    searchEntityMailing.setAttribute(BDMDatastoreConstants.ADDRESS_DATA1,
      addressData);
    witnessEntity.addChildEntity(searchEntityMailing);
    searchEntityMailing.update();

    witnessEntity.update();
    applicationEntity.update();

    new Expectations(datastore) {

      {
        datastore.readEntity(anyLong);
        result = applicationEntity;

      }
    };

    final String addressType = BDMDatastoreConstants.MAILING;
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
    final Entity verifyWitnessEntity =
      applicationEntity.getChildEntities(datastore
        .getEntityType(BDMOASDatastoreConstants.DECLARATION_ENTITY))[0];
    final Entity mailingAddress = verifyWitnessEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.MAILING_ADDRESS))[0];

    assertEquals(mailingAddress.getAttribute(BDMDatastoreConstants.CITY),
      "Montreal\r");
    assertEquals(
      mailingAddress.getAttribute(BDMDatastoreConstants.ADDRESS_LINE1),
      "434\r");
    assertEquals(mailingAddress.getAttribute(BDMDatastoreConstants.SUITE_NUM),
      "4324\r");
    assertEquals(
      mailingAddress.getAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY),
      "CA\r");
    assertEquals(
      mailingAddress.getAttribute(BDMDatastoreConstants.POSTAL_CODE),
      "B1B1B1\r");

  }
}
