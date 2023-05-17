package curam.ca.gc.bdmoas.test.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.BDMWSAddressInterfaceImpl;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.validatepojos.WsaddrValidationRequest;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.intf.BDMWSAddressInterfaceIntf;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.ieg.impl.IEG2Context;
import curam.ieg.impl.IEGScriptExecution;
import curam.ieg.impl.IEGScriptExecutionFactory;
import curam.rules.functions.CustomFunctionValidatePartnerResidentialAddress;
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
 * The Class tests {@link CustomFunctionValidatePartnerResidentialAddress}
 * custom
 * function.
 */

@RunWith(JMockit.class)
public class CustomFunctionValidatePartnerResidentialAddressTest
  extends CustomFunctionTestJunit4 {

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The rulesParameters. */
  @Mocked
  IEG2ExecutionContext ieg2ExecutionContext;

  /** The script execution. */
  @Mocked
  IEGScriptExecution scriptExecution;

  @Mocked
  BDMWSAddressInterfaceIntf wsAddressImplObj;

  @Mocked
  BDMWSAddressInterfaceImpl BDMWSAddressInterfaceImpl;

  /** The script execution factory. */
  @Mocked
  IEGScriptExecutionFactory scriptExecutionFactory;

  /** The validate function. */
  @Tested
  CustomFunctionValidatePartnerResidentialAddress customFunction;

  public CustomFunctionValidatePartnerResidentialAddressTest() {

    super();
  }

  /**
   * Before each test, init the tested class and other objects.
   */
  @Before
  public void before() throws NoSuchSchemaException {

    customFunction = new CustomFunctionValidatePartnerResidentialAddress();
    ieg2Context = new IEG2Context();
    wsAddressImplObj = new BDMWSAddressInterfaceImpl();

  }

  /**
   * Test that the appropriate fields are set.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_idmatch()
    throws AppException, InformationalException, NoSuchSchemaException {

    // Set up entities
    final Datastore datastore = DatastoreFactory.newInstance()
      .openDatastore("BDMOASGISApplicationSchema");

    final Entity applicationEntity =
      datastore.newEntity(BDMDatastoreConstants.APPLICATION);
    datastore.addRootEntity(applicationEntity);

    final Entity partnerPersonEntity =
      datastore.newEntity(BDMDatastoreConstants.PERSON);
    partnerPersonEntity
      .setAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT, "false");
    partnerPersonEntity.setAttribute("localID", "" + 34324023);
    partnerPersonEntity
      .setTypedAttribute(BDMDatastoreConstants.WS_RES_CONTINUE, "1234");
    applicationEntity.addChildEntity(partnerPersonEntity);

    partnerPersonEntity.update();
    applicationEntity.update();

    new Expectations(datastore) {

      {
        datastore.readEntity(anyLong);
        result = applicationEntity;

      }
    };

    final String id = "1234";
    final String suiteNum = "4324";
    final String streetNumber = "434";
    final String streetName = "NLfante Plaza";
    final String city = "Montreal";
    final String province = "QC";
    final String postalCode = "B1B1B1";
    final String country = "CA";

    expectationsParameters(id, suiteNum, streetNumber, streetName, city,
      province, postalCode, country);
    ieg2Context.setRootEntityID(applicationEntity.getUniqueID());
    final RulesParameters rulesParameters = ieg2Context;

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction
        .getAdaptorValue(rulesParameters);

    // Verify
    assertEquals("EAddress search must be true.", true,
      adaptorValue.getValue(rulesParameters));

  }

  /**
   * Test that the appropriate fields are set.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_address_check()
    throws AppException, InformationalException, NoSuchSchemaException {

    // Set up entities
    final Datastore datastore = DatastoreFactory.newInstance()
      .openDatastore("BDMOASGISApplicationSchema");

    final Entity applicationEntity =
      datastore.newEntity(BDMDatastoreConstants.APPLICATION);
    datastore.addRootEntity(applicationEntity);
    final Entity partnerPersonEntity =
      datastore.newEntity(BDMDatastoreConstants.PERSON);
    partnerPersonEntity
      .setAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT, "false");
    partnerPersonEntity.setAttribute("localID", "" + 34324023);
    applicationEntity.addChildEntity(partnerPersonEntity);

    final Entity residentialEntity =
      datastore.newEntity(BDMDatastoreConstants.RESIDENTIAL_ADDRESS);
    residentialEntity.setAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY,
      "US");
    partnerPersonEntity.addChildEntity(residentialEntity);
    residentialEntity.update();

    partnerPersonEntity.update();
    applicationEntity.update();

    new Expectations(datastore) {

      {
        datastore.readEntity(anyLong);
        result = applicationEntity;

        wsAddressImplObj.validateAddress((WsaddrValidationRequest) any);
        result = true;

      }
    };

    final String id = "1234";
    final String suiteNum = "4324";
    final String streetNumber = "434";
    final String streetName = "NLfante Plaza";
    final String city = "Montreal";
    final String province = "QC";
    final String postalCode = "B1B1B1";
    final String country = "CA";

    expectationsParameters(id, suiteNum, streetNumber, streetName, city,
      province, postalCode, country);
    ieg2Context.setRootEntityID(applicationEntity.getUniqueID());
    final RulesParameters rulesParameters = ieg2Context;

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction
        .getAdaptorValue(rulesParameters);

    // Verify
    assertEquals("Address search must be true.", true,
      adaptorValue.getValue(rulesParameters));

    final Entity verifyPersonEntity = applicationEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.PERSON),
      "isPrimaryParticipant==false")[0];
    final Entity residentalAddress = verifyPersonEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.RESIDENTIAL_ADDRESS))[0];

    assertEquals(residentalAddress.getAttribute("validateStatus"), "1");

  }
}
