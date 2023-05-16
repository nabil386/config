package curam.ca.gc.bdmoas.test.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.sl.bdmaddress.fact.BDMAddressSearchFactory;
import curam.ca.gc.bdm.sl.bdmaddress.intf.BDMAddressSearch;
import curam.ca.gc.bdm.sl.bdmaddress.struct.BDMAddressDetailsStruct;
import curam.ca.gc.bdm.sl.bdmaddress.struct.BDMAddressSearchDetails;
import curam.ca.gc.bdm.sl.bdmaddress.struct.BDMAddressSearchDetailsList;
import curam.ca.gc.bdmoas.application.impl.BDMOASDatastoreConstants;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.ieg.impl.IEG2Context;
import curam.ieg.impl.IEGScriptExecution;
import curam.ieg.impl.IEGScriptExecutionFactory;
import curam.rules.functions.CustomFunctionSearchWitnessAddress;
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
import static org.junit.Assert.assertTrue;

/**
 * The Class tests {@link CustomFunctionSearchWitnessAddress} custom
 * function.
 */

@RunWith(JMockit.class)
public class CustomFunctionSearchWitnessAddressTest
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

  /** Address functions **/
  @Mocked
  BDMAddressSearch bdmAddressSearch;

  @Mocked
  BDMAddressSearchFactory BDMAddressSearchFactory;

  /** The validate function. */
  @Tested
  CustomFunctionSearchWitnessAddress customFunction;

  public CustomFunctionSearchWitnessAddressTest() {

    super();
  }

  /**
   * Before each test, init the tested class and other objects.
   */
  @Before
  public void before() throws NoSuchSchemaException {

    customFunction = new CustomFunctionSearchWitnessAddress();
    ieg2Context = new IEG2Context();
    bdmAddressSearch =
      curam.ca.gc.bdm.sl.bdmaddress.fact.BDMAddressSearchFactory
        .newInstance();

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

    final Entity witnessEntity =
      datastore.newEntity(BDMOASDatastoreConstants.DECLARATION_ENTITY);
    // witnessEntity.setAttribute(BDMDatastoreConstants.MAILING_COUNTRY, "CA");
    applicationEntity.addChildEntity(witnessEntity);

    final String addressData = "1\r\n" + "0\r\n" + "BDMINTL\r\n" + "CA\r\n"
      + "1\r\n" + "2\r\n" + "POBOXNO=\r\n" + "APT=4324\r\n" + "ADD1=434\r\n"
      + "ADD2=NLfante Plaza\r\n" + "CITY=Montreal\r\n" + "PROV=QC\r\n"
      + "STATEPROV=\r\n" + "BDMSTPROVX=\r\n" + "COUNTRY=CA\r\n"
      + "POSTCODE=B1B1B1\r\n" + "ZIP=\r\n" + "BDMZIPX=\r\n" + "";

    witnessEntity.update();
    applicationEntity.update();

    final BDMAddressSearchDetailsList bdmAddressSearchDetailsList =
      new BDMAddressSearchDetailsList();

    final BDMAddressSearchDetails bdmAddressSearchDetails =
      new BDMAddressSearchDetails();

    bdmAddressSearchDetails.formattedAddressData = addressData;
    bdmAddressSearchDetails.addressData = addressData;

    bdmAddressSearchDetailsList.detailsList.add(bdmAddressSearchDetails);

    new Expectations(datastore) {

      {
        datastore.readEntity(anyLong);
        result = applicationEntity;

        bdmAddressSearch.searchAddressData((BDMAddressDetailsStruct) any);
        result = bdmAddressSearchDetailsList;

      }
    };

    final String country = "CA";
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

    final Entity verifyWitnessEntity =
      applicationEntity.getChildEntities(datastore
        .getEntityType(BDMOASDatastoreConstants.DECLARATION_ENTITY))[0];
    final Entity[] searchAddress = verifyWitnessEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.SEARCH_ADDRESS));

    assertTrue("A search address entity should have been added.",
      searchAddress.length > 0);
    assertEquals("Address data should be set.", addressData,
      searchAddress[0].getAttribute(BDMDatastoreConstants.ADDRESS_DATA1));

  }
}
