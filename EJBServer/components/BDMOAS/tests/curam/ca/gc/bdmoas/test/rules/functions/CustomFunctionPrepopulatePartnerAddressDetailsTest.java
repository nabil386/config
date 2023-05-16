package curam.ca.gc.bdmoas.test.rules.functions;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.codetable.ADDRESSELEMENTTYPE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.core.fact.AddressElementFactory;
import curam.core.fact.ConcernRoleAddressFactory;
import curam.core.fact.PersonFactory;
import curam.core.intf.AddressElement;
import curam.core.intf.ConcernRoleAddress;
import curam.core.intf.Person;
import curam.core.struct.AddressElementDtls;
import curam.core.struct.AddressElementDtlsList;
import curam.core.struct.AddressKey;
import curam.core.struct.AddressReadMultiDtls;
import curam.core.struct.AddressReadMultiDtlsList;
import curam.core.struct.ConcernRoleIDStatusCodeKey;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.ieg.impl.IEG2Context;
import curam.ieg.impl.IEGScriptExecution;
import curam.ieg.impl.IEGScriptExecutionFactory;
import curam.piwrapper.impl.Address;
import curam.piwrapper.impl.AddressDAO;
import curam.rules.functions.CustomFunctionPrepopulatePartnerAddressDetails;
import curam.rules.functions.IEG2ExecutionContext;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import mockit.Capturing;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/**
 * The Class tests {@link CustomFunctionPrepopulatePartnerAddressDetails} custom
 * function.
 */

@RunWith(JMockit.class)
public class CustomFunctionPrepopulatePartnerAddressDetailsTest
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

  @Mocked
  ConcernRoleAddress concernroleAddressObj;

  @Mocked
  AddressElement addressElementObj;

  @Mocked
  ConcernRoleAddressFactory ConcernRoleAddressFactory;

  @Mocked
  AddressElementFactory AddressElementFactory;

  @Capturing
  AddressDAO addressDAO;

  @Mocked
  Address addressObj;

  @Mocked
  Person personObj;

  @Mocked
  BDMUtil BDMUtil;

  @Mocked
  PersonFactory PersonFactory;

  /** The validate function. */
  @Tested
  CustomFunctionPrepopulatePartnerAddressDetails customFunction;

  public CustomFunctionPrepopulatePartnerAddressDetailsTest() {

    super();
  }

  /**
   * Before each test, init the tested class and other objects.
   */
  @Before
  public void before() throws NoSuchSchemaException {

    customFunction = new CustomFunctionPrepopulatePartnerAddressDetails();
    ieg2Context = new IEG2Context();
    concernroleAddressObj =
      curam.core.fact.ConcernRoleAddressFactory.newInstance();
    addressElementObj = curam.core.fact.AddressElementFactory.newInstance();
    final Injector injector = Guice.createInjector(new AbstractModule() {

      @Override
      protected void configure() {

        bind(AddressDAO.class).toInstance(addressDAO);
      }
    });
    injector.injectMembers(customFunction);

  }

  /**
   * Test that the fields are being set.
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

    final Entity primaryPersonEntity =
      datastore.newEntity(BDMDatastoreConstants.PERSON);
    primaryPersonEntity
      .setAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT, "true");
    primaryPersonEntity.setAttribute("localID", "" + 3942034);
    applicationEntity.addChildEntity(primaryPersonEntity);

    final Entity partnerPersonEntity =
      datastore.newEntity(BDMDatastoreConstants.PERSON);
    partnerPersonEntity
      .setAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT, "false");
    partnerPersonEntity.setAttribute("localID", "" + 329104);
    applicationEntity.addChildEntity(partnerPersonEntity);

    partnerPersonEntity.update();
    primaryPersonEntity.update();
    applicationEntity.update();

    final AddressReadMultiDtlsList concernRoleAddressList =
      new AddressReadMultiDtlsList();

    final AddressReadMultiDtls concernRoleAddressDtls =
      new AddressReadMultiDtls();
    final String addressData = "1\r\n" + "0\r\n" + "BDMINTL\r\n" + "CA\r\n"
      + "1\r\n" + "2\r\n" + "POBOXNO=\r\n" + "APT=4324\r\n" + "ADD1=434\r\n"
      + "ADD2=NLfante Plaza\r\n" + "CITY=Montreal\r\n" + "PROV=QC\r\n"
      + "STATEPROV=\r\n" + "BDMSTPROVX=\r\n" + "COUNTRY=CA\r\n"
      + "POSTCODE=B1B1B1\r\n" + "ZIP=\r\n" + "BDMZIPX=\r\n" + "";
    concernRoleAddressDtls.addressData = addressData;
    concernRoleAddressDtls.typeCode = CONCERNROLEADDRESSTYPE.PRIVATE;
    concernRoleAddressDtls.addressID = 34932;
    concernRoleAddressList.dtls.add(concernRoleAddressDtls);

    final AddressReadMultiDtls concernRoleAddressDtls1 =
      new AddressReadMultiDtls();
    concernRoleAddressDtls1.addressData = addressData;
    concernRoleAddressDtls1.typeCode = CONCERNROLEADDRESSTYPE.MAILING;
    concernRoleAddressDtls1.addressID = 34932;
    concernRoleAddressList.dtls.add(concernRoleAddressDtls1);

    final AddressElementDtlsList addressElementDtlsList =
      new AddressElementDtlsList();

    final AddressElementDtls addressElementApt = new AddressElementDtls();
    addressElementApt.addressID = 34932;
    addressElementApt.elementType = ADDRESSELEMENTTYPE.APT;
    addressElementApt.elementValue = "4324";
    addressElementDtlsList.dtls.add(addressElementApt);

    final AddressElementDtls addressElementCity = new AddressElementDtls();
    addressElementCity.addressID = 34932;
    addressElementCity.elementType = ADDRESSELEMENTTYPE.CITY;
    addressElementCity.elementValue = "Montreal";
    addressElementDtlsList.dtls.add(addressElementCity);

    final AddressElementDtls addressElementProv = new AddressElementDtls();
    addressElementProv.addressID = 34932;
    addressElementProv.elementType = ADDRESSELEMENTTYPE.PROVINCE;
    addressElementProv.elementValue = "QC";
    addressElementDtlsList.dtls.add(addressElementProv);

    final AddressElementDtls addressElementCoun = new AddressElementDtls();
    addressElementCoun.addressID = 34932;
    addressElementCoun.elementType = ADDRESSELEMENTTYPE.COUNTRY;
    addressElementCoun.elementValue = "CA";
    addressElementDtlsList.dtls.add(addressElementCoun);

    final AddressElementDtls addressElementLin1 = new AddressElementDtls();
    addressElementLin1.addressID = 34932;
    addressElementLin1.elementType = ADDRESSELEMENTTYPE.LINE1;
    addressElementLin1.elementValue = "434";
    addressElementDtlsList.dtls.add(addressElementLin1);

    final AddressElementDtls addressElementLin2 = new AddressElementDtls();
    addressElementLin2.addressID = 34932;
    addressElementLin2.elementType = ADDRESSELEMENTTYPE.LINE2;
    addressElementLin2.elementValue = "NLfante Plaza";
    addressElementDtlsList.dtls.add(addressElementLin2);

    final AddressElementDtls addressElementPost = new AddressElementDtls();
    addressElementPost.addressID = 34932;
    addressElementPost.elementType = ADDRESSELEMENTTYPE.POSTCODE;
    addressElementPost.elementValue = "B1B1B1";
    addressElementDtlsList.dtls.add(addressElementPost);

    /*
     * personObj = curam.core.fact.PersonFactory.newInstance();
     * final PersonDtls personDtls = new PersonDtls();
     * personDtls.personBirthName = "Jane";
     * personDtls.motherBirthSurname = "Doe";
     * personDtls.maritalStatusCode =
     * BDMOASMARITALRELATION.OAS_MARITAL_RELATION_MARRIED;
     * personDtls.dateOfDeath = Date.getCurrentDate();
     * personDtls.countryOfBirth = "CA";
     */

    new Expectations(datastore) {

      {
        datastore.readEntity(anyLong);
        result = applicationEntity;

        concernroleAddressObj.searchAddressesByConcernRoleIDAndStatus(
          (ConcernRoleIDStatusCodeKey) any);
        result = concernRoleAddressList;

        addressElementObj.readAddressElementDetails((AddressKey) any);
        result = addressElementDtlsList;

        addressDAO.get(anyLong);
        result = addressObj;

        addressObj.getOneLineAddressString();
        result = "test string here";

      }
    };

    ieg2Context.setRootEntityID(applicationEntity.getUniqueID());

    final RulesParameters rulesParameters = ieg2Context;

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction
        .getAdaptorValue(rulesParameters);

    // Verify
    final Entity personEntity_Applicant = applicationEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.PERSON),
      "isPrimaryParticipant==false")[0];
    final Entity residentalEntity = personEntity_Applicant.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.RESIDENTIAL_ADDRESS))[0];

    assertEquals(
      residentalEntity.getAttribute(BDMDatastoreConstants.SUITE_NUM),
      addressElementApt.elementValue);
    assertEquals(residentalEntity.getAttribute(BDMDatastoreConstants.CITY),
      addressElementCity.elementValue);
    assertEquals(
      residentalEntity.getAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY),
      addressElementCoun.elementValue);
    assertEquals(
      residentalEntity.getAttribute(BDMDatastoreConstants.PROVINCE),
      addressElementProv.elementValue);
    assertEquals(
      residentalEntity.getAttribute(BDMDatastoreConstants.POSTAL_CODE),
      addressElementPost.elementValue);
    assertEquals(
      residentalEntity.getAttribute(BDMDatastoreConstants.ADDRESS_LINE1),
      addressElementLin1.elementValue);
    assertEquals(
      residentalEntity.getAttribute(BDMDatastoreConstants.ADDRESS_LINE2),
      addressElementLin2.elementValue);
    assertEquals(
      residentalEntity.getAttribute(BDMDatastoreConstants.ONE_LINE_ADDRESS),
      "test string here");

  }
}
