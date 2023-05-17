package curam.ca.gc.bdm.test.person;

import curam.ca.gc.bdm.codetable.BDMPHONECOUNTRY;
import curam.ca.gc.bdm.facade.participant.fact.BDMParticipantRegistrationDetailsFactory;
import curam.ca.gc.bdm.facade.participant.intf.BDMParticipantRegistrationDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMPersonRegistrationDetails;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.EMAILTYPE;
import curam.codetable.METHODOFDELIVERY;
import curam.codetable.PHONETYPE;
import curam.codetable.PROVINCETYPE;
import curam.codetable.STATECODES;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.facade.struct.PersonSearchWizardKey;
import curam.core.facade.struct.RegisterPersonWizardSearchDetails;
import curam.core.fact.PersonRegistrationFactory;
import curam.core.impl.CuramConst;
import curam.core.intf.PersonRegistration;
import curam.core.sl.struct.WizardStateID;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.OtherAddressData;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonRegistrationDetails;
import curam.core.struct.RegistrationIDDetails;
import curam.creole.execution.session.RecalculationsProhibited;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.execution.session.StronglyTypedRuleObjectFactory;
import curam.creole.storage.inmemory.InMemoryDataStorage;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author teja.konda
 *
 */
public class BDMPersonRegistrationTest extends CuramServerTestJUnit4 {

  public BDMPersonRegistrationTest() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  Session session;

  @Override
  public void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    session =
      Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
        new InMemoryDataStorage(new StronglyTypedRuleObjectFactory()));
  }

  RegisterPerson registerPerson = new RegisterPerson("RegisterPerson");

  private final String ERR_MAILING_ADDRESS_MANDATORY =
    "Please enter your mailing address";

  private final String ERR_PHONE_AREA_CODE =
    "All countries which have a country phone code of + 1, Area Code must be entered for a phone number.";

  private final String ERR_PHONE_AREA_CODE_3DIGIT =
    "Area Code must be numeric and 3 digits for all countries which have a country phone code of + 1.";

  private final String ERR_PHONE_NUMBER_7DIGIT =
    "Phone Number must be numeric and 7 digits for all countries which have a country phone code of + 1.";

  private final String ERR_PHONE_NUMBER_OTHER_COUNTRY =
    "Phone area code and phone number must be numeric for all countries which do not have a country phone code of + 1.";

  private final String ERR_PHONE_INVALID_TYPE =
    "Personal mobile phone number or Business mobile phone number cannot have extensions.";

  private final String ERR_PHONE_EXTENSION_NUMERIC =
    "Extensions can only be numeric.";

  private final String ERR_ADDR_CA_CITY_MISSING =
    "City, Town, or Village must be entered.";

  private final String ERR_ADDR_CA_PROV_MISSING =
    "Province or Territory must be selected for a Canadian address.";

  private final String ERR_ADDR_CA_POSTALCODE_MISSING =
    "Postal Code must be entered.";

  private final String ERR_ADDR_CA_INVALID_POSTAL =
    "Postal Code must be in the right format for a Canadian address.";

  private final String ERR_ADDR_US_ZIPCODE_MISSING =
    "Zip Code must be entered.";

  private final String ERR_ADDR_STATE_MISSING =
    "If country selected is other than Canada the State field cannot be left blank.";

  /**
   * create contact preference evidence
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testPersonRegistration()
    throws AppException, InformationalException {

    // createPDCPerson();
    final PDCPersonDetails pdcPersonDetails = createPDCPerson();

    assertTrue(pdcPersonDetails.concernRoleID != 0);

  }

  @Test
  public void testPersonSearch() throws AppException, InformationalException {

    // createPDCPerson();
    final PDCPersonDetails pdcPersonDetails = createPDCPerson();

    final PersonSearchWizardKey searchKey = new PersonSearchWizardKey();

    final WizardStateID stateID = new WizardStateID();
    final ActionIDProperty actionID = new ActionIDProperty();
    actionID.actionIDProperty = CuramConst.kSearchAction;

    searchKey.searchKey.forename = "Test";
    searchKey.searchKey.surname = "Person";

    final BDMParticipantRegistrationDetails participantRegistrationDetails =
      BDMParticipantRegistrationDetailsFactory.newInstance();

    final RegisterPersonWizardSearchDetails registerPersonWizardSearchDetails =
      participantRegistrationDetails
        .setRegisterPersonSearchCriteriaDetails(searchKey, stateID, actionID);

    assertTrue(
      registerPersonWizardSearchDetails.searchResult.personSearchResult.dtlsList
        .size() > 0);

  }

  /**
   * Test to check mailing address validation
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testPersonRegistrationMailingAddressFailure()
    throws AppException, InformationalException {

    boolean caught = false;

    try {

      final PersonRegistrationDetails personRegistrationDetails =
        this.getPersonRegistrationDetails();

      final WizardStateID wizardStateID = new WizardStateID();
      final ActionIDProperty actionIDProperty = new ActionIDProperty();
      actionIDProperty.actionIDProperty = "Save";

      final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
        new BDMPersonRegistrationDetails();
      bdmPersonRegistrationDetails.dtls = personRegistrationDetails;
      bdmPersonRegistrationDetails.isMailingAddSame = false;

      final BDMParticipantRegistrationDetails participantRegistration =
        BDMParticipantRegistrationDetailsFactory.newInstance();

      participantRegistration.setRegisterPersonForPDCDetails(
        bdmPersonRegistrationDetails, wizardStateID, actionIDProperty);

    } catch (final InformationalException e) {

      caught = true;
      assertEquals(ERR_MAILING_ADDRESS_MANDATORY, e.getMessage());
    }

    assertTrue(caught);

  }

  /**
   * Test to check address state validation
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testPersonRegistrationAddressStateFailure()
    throws AppException, InformationalException {

    boolean caught = false;

    try {

      final java.util.Calendar cal = new java.util.GregorianCalendar(1970,
        java.util.Calendar.APRIL, 1, 0, 0, 0);
      final curam.util.type.Date birthday = new curam.util.type.Date(cal);

      final PersonRegistrationDetails personRegistrationDetails =
        new PersonRegistrationDetails();

      personRegistrationDetails.firstForename = "Test";
      personRegistrationDetails.surname = "Person";
      // BEGIN, CR00446316, YF
      // END, CR00446316, YF
      personRegistrationDetails.sex = curam.codetable.GENDER.getDefaultCode();
      personRegistrationDetails.dateOfBirth = birthday;
      personRegistrationDetails.registrationDate =
        curam.util.type.Date.getCurrentDate();
      personRegistrationDetails.currentMaritalStatus =
        curam.codetable.MARITALSTATUS.MARRIED;
      personRegistrationDetails.nationality =
        curam.codetable.NATIONALITY.DEFAULTCODE;
      personRegistrationDetails.birthCountry = curam.codetable.COUNTRY.GB;

      // parseFieldsToData always make US address type - it is irrelevant what
      // is defined here.
      personRegistrationDetails.addressType =
        CONCERNROLEADDRESSTYPE.PRIVATE.toString();

      final AddressFieldDetails addressFieldDetails =
        new AddressFieldDetails();
      final curam.core.intf.AddressData addressDataObj =
        curam.core.fact.AddressDataFactory.newInstance();

      addressFieldDetails.addressLayoutType =
        curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;

      // BEGIN, CR00272990 , KRK
      addressFieldDetails.suiteNum = "4994";
      addressFieldDetails.addressLine1 = "Heatherleigh";
      addressFieldDetails.addressLine2 = "Cooksville";
      addressFieldDetails.province = PROVINCETYPE.ONTARIO;
      addressFieldDetails.city = "Ontario";
      // BEGIN, CR00380472, MV
      addressFieldDetails.postalCode = "L5A 1V9";
      // END, CR00380472
      addressFieldDetails.countryCode = curam.codetable.COUNTRY.US;
      addressFieldDetails.zipCode = "12345";
      // addressFieldDetails.stateProvince = "AK";
      // END, CR00272990
      OtherAddressData otherAddressData = null;

      otherAddressData =
        addressDataObj.parseFieldsToData(addressFieldDetails);

      personRegistrationDetails.addressData = otherAddressData.addressData;

      // BEGIN, CR00141773, CW
      personRegistrationDetails.paymentFrequency = "100100100";
      personRegistrationDetails.methodOfPmtCode = METHODOFDELIVERY.CHEQUE;
      personRegistrationDetails.currencyType =
        curam.codetable.CURRENCY.DEFAULTCODE;
      // END, CR00141773

      // Add email address details
      personRegistrationDetails.contactEmailAddress = "test@gmail.com";
      personRegistrationDetails.contactEmailType = EMAILTYPE.PERSONAL;

      final WizardStateID wizardStateID = new WizardStateID();
      final ActionIDProperty actionIDProperty = new ActionIDProperty();
      actionIDProperty.actionIDProperty = "Save";

      final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
        new BDMPersonRegistrationDetails();
      bdmPersonRegistrationDetails.dtls = personRegistrationDetails;
      bdmPersonRegistrationDetails.isMailingAddSame = true;

      final BDMParticipantRegistrationDetails participantRegistration =
        BDMParticipantRegistrationDetailsFactory.newInstance();

      participantRegistration.setRegisterPersonForPDCDetails(
        bdmPersonRegistrationDetails, wizardStateID, actionIDProperty);

    } catch (final InformationalException e) {

      caught = true;
      assertEquals(ERR_ADDR_STATE_MISSING, e.getMessage());
    }

    assertTrue(caught);

  }

  /**
   * Test to check US address Zip code validation
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testPersonRegistrationAddressZIPFailure()
    throws AppException, InformationalException {

    boolean caught = false;

    try {

      final java.util.Calendar cal = new java.util.GregorianCalendar(1970,
        java.util.Calendar.APRIL, 1, 0, 0, 0);
      final curam.util.type.Date birthday = new curam.util.type.Date(cal);

      final PersonRegistrationDetails personRegistrationDetails =
        new PersonRegistrationDetails();

      personRegistrationDetails.firstForename = "Test";
      personRegistrationDetails.surname = "Person";
      // BEGIN, CR00446316, YF
      // END, CR00446316, YF
      personRegistrationDetails.sex = curam.codetable.GENDER.getDefaultCode();
      personRegistrationDetails.dateOfBirth = birthday;
      personRegistrationDetails.registrationDate =
        curam.util.type.Date.getCurrentDate();
      personRegistrationDetails.currentMaritalStatus =
        curam.codetable.MARITALSTATUS.MARRIED;
      personRegistrationDetails.nationality =
        curam.codetable.NATIONALITY.DEFAULTCODE;
      personRegistrationDetails.birthCountry = curam.codetable.COUNTRY.GB;

      // parseFieldsToData always make US address type - it is irrelevant what
      // is defined here.
      personRegistrationDetails.addressType =
        CONCERNROLEADDRESSTYPE.PRIVATE.toString();

      final AddressFieldDetails addressFieldDetails =
        new AddressFieldDetails();
      final curam.core.intf.AddressData addressDataObj =
        curam.core.fact.AddressDataFactory.newInstance();

      addressFieldDetails.addressLayoutType =
        curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;

      // BEGIN, CR00272990 , KRK
      addressFieldDetails.suiteNum = "4994";
      addressFieldDetails.addressLine1 = "Heatherleigh";
      addressFieldDetails.addressLine2 = "Cooksville";
      addressFieldDetails.province = PROVINCETYPE.ONTARIO;
      addressFieldDetails.city = "Ontario";
      // BEGIN, CR00380472, MV
      addressFieldDetails.postalCode = "L5A 1V9";
      // END, CR00380472
      addressFieldDetails.countryCode = curam.codetable.COUNTRY.US;
      addressFieldDetails.stateProvince = STATECODES.DEFAULTCODE;
      // END, CR00272990
      OtherAddressData otherAddressData = null;

      otherAddressData =
        addressDataObj.parseFieldsToData(addressFieldDetails);

      personRegistrationDetails.addressData = otherAddressData.addressData;

      // BEGIN, CR00141773, CW
      personRegistrationDetails.paymentFrequency = "100100100";
      personRegistrationDetails.methodOfPmtCode = METHODOFDELIVERY.CHEQUE;
      personRegistrationDetails.currencyType =
        curam.codetable.CURRENCY.DEFAULTCODE;
      // END, CR00141773

      // Add email address details
      personRegistrationDetails.contactEmailAddress = "test@gmail.com";
      personRegistrationDetails.contactEmailType = EMAILTYPE.PERSONAL;

      final WizardStateID wizardStateID = new WizardStateID();
      final ActionIDProperty actionIDProperty = new ActionIDProperty();
      actionIDProperty.actionIDProperty = "Save";

      final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
        new BDMPersonRegistrationDetails();
      bdmPersonRegistrationDetails.dtls = personRegistrationDetails;
      bdmPersonRegistrationDetails.isMailingAddSame = true;

      final BDMParticipantRegistrationDetails participantRegistration =
        BDMParticipantRegistrationDetailsFactory.newInstance();

      participantRegistration.setRegisterPersonForPDCDetails(
        bdmPersonRegistrationDetails, wizardStateID, actionIDProperty);

    } catch (final InformationalException e) {

      caught = true;
      assertEquals(ERR_ADDR_US_ZIPCODE_MISSING, e.getMessage());
    }

    assertTrue(caught);

  }

  /**
   * Test to check CA address invalid Postal validation
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testPersonRegistrationAddressPostalFailure()
    throws AppException, InformationalException {

    boolean caught = false;

    try {

      final java.util.Calendar cal = new java.util.GregorianCalendar(1970,
        java.util.Calendar.APRIL, 1, 0, 0, 0);
      final curam.util.type.Date birthday = new curam.util.type.Date(cal);

      final PersonRegistrationDetails personRegistrationDetails =
        new PersonRegistrationDetails();

      personRegistrationDetails.firstForename = "Test";
      personRegistrationDetails.surname = "Person";
      // BEGIN, CR00446316, YF
      // END, CR00446316, YF
      personRegistrationDetails.sex = curam.codetable.GENDER.getDefaultCode();
      personRegistrationDetails.dateOfBirth = birthday;
      personRegistrationDetails.registrationDate =
        curam.util.type.Date.getCurrentDate();
      personRegistrationDetails.currentMaritalStatus =
        curam.codetable.MARITALSTATUS.MARRIED;
      personRegistrationDetails.nationality =
        curam.codetable.NATIONALITY.DEFAULTCODE;
      personRegistrationDetails.birthCountry = curam.codetable.COUNTRY.GB;

      // parseFieldsToData always make US address type - it is irrelevant what
      // is defined here.
      personRegistrationDetails.addressType =
        CONCERNROLEADDRESSTYPE.PRIVATE.toString();

      final AddressFieldDetails addressFieldDetails =
        new AddressFieldDetails();
      final curam.core.intf.AddressData addressDataObj =
        curam.core.fact.AddressDataFactory.newInstance();

      addressFieldDetails.addressLayoutType =
        curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;

      // BEGIN, CR00272990 , KRK
      addressFieldDetails.suiteNum = "4994";
      addressFieldDetails.addressLine1 = "Heatherleigh";
      addressFieldDetails.addressLine2 = "Cooksville";
      addressFieldDetails.province = PROVINCETYPE.ONTARIO;
      addressFieldDetails.city = "Ontario";
      // BEGIN, CR00380472, MV
      addressFieldDetails.postalCode = "L5A 1VS";
      // END, CR00380472
      addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;
      // END, CR00272990
      OtherAddressData otherAddressData = null;

      otherAddressData =
        addressDataObj.parseFieldsToData(addressFieldDetails);

      personRegistrationDetails.addressData = otherAddressData.addressData;

      // BEGIN, CR00141773, CW
      personRegistrationDetails.paymentFrequency = "100100100";
      personRegistrationDetails.methodOfPmtCode = METHODOFDELIVERY.CHEQUE;
      personRegistrationDetails.currencyType =
        curam.codetable.CURRENCY.DEFAULTCODE;
      // END, CR00141773

      // Add email address details
      personRegistrationDetails.contactEmailAddress = "test@gmail.com";
      personRegistrationDetails.contactEmailType = EMAILTYPE.PERSONAL;

      final WizardStateID wizardStateID = new WizardStateID();
      final ActionIDProperty actionIDProperty = new ActionIDProperty();
      actionIDProperty.actionIDProperty = "Save";

      final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
        new BDMPersonRegistrationDetails();
      bdmPersonRegistrationDetails.dtls = personRegistrationDetails;
      bdmPersonRegistrationDetails.isMailingAddSame = true;

      final BDMParticipantRegistrationDetails participantRegistration =
        BDMParticipantRegistrationDetailsFactory.newInstance();

      participantRegistration.setRegisterPersonForPDCDetails(
        bdmPersonRegistrationDetails, wizardStateID, actionIDProperty);

    } catch (final InformationalException e) {

      caught = true;
      assertEquals(ERR_ADDR_CA_INVALID_POSTAL, e.getMessage());
    }

    assertTrue(caught);

  }

  /**
   * Test to check CA address Postal Code Missing validation
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testPersonRegistrationAddressPostalMisingFailure()
    throws AppException, InformationalException {

    boolean caught = false;

    try {

      final java.util.Calendar cal = new java.util.GregorianCalendar(1970,
        java.util.Calendar.APRIL, 1, 0, 0, 0);
      final curam.util.type.Date birthday = new curam.util.type.Date(cal);

      final PersonRegistrationDetails personRegistrationDetails =
        new PersonRegistrationDetails();

      personRegistrationDetails.firstForename = "Test";
      personRegistrationDetails.surname = "Person";
      // BEGIN, CR00446316, YF
      // END, CR00446316, YF
      personRegistrationDetails.sex = curam.codetable.GENDER.getDefaultCode();
      personRegistrationDetails.dateOfBirth = birthday;
      personRegistrationDetails.registrationDate =
        curam.util.type.Date.getCurrentDate();
      personRegistrationDetails.currentMaritalStatus =
        curam.codetable.MARITALSTATUS.MARRIED;
      personRegistrationDetails.nationality =
        curam.codetable.NATIONALITY.DEFAULTCODE;
      personRegistrationDetails.birthCountry = curam.codetable.COUNTRY.GB;

      // parseFieldsToData always make US address type - it is irrelevant what
      // is defined here.
      personRegistrationDetails.addressType =
        CONCERNROLEADDRESSTYPE.PRIVATE.toString();

      final AddressFieldDetails addressFieldDetails =
        new AddressFieldDetails();
      final curam.core.intf.AddressData addressDataObj =
        curam.core.fact.AddressDataFactory.newInstance();

      addressFieldDetails.addressLayoutType =
        curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;

      // BEGIN, CR00272990 , KRK
      addressFieldDetails.suiteNum = "4994";
      addressFieldDetails.addressLine1 = "Heatherleigh";
      addressFieldDetails.addressLine2 = "Cooksville";
      addressFieldDetails.province = PROVINCETYPE.ONTARIO;
      addressFieldDetails.city = "Ontario";
      // BEGIN, CR00380472, MV
      // END, CR00380472
      addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;
      // END, CR00272990
      OtherAddressData otherAddressData = null;

      otherAddressData =
        addressDataObj.parseFieldsToData(addressFieldDetails);

      personRegistrationDetails.addressData = otherAddressData.addressData;

      // BEGIN, CR00141773, CW
      personRegistrationDetails.paymentFrequency = "100100100";
      personRegistrationDetails.methodOfPmtCode = METHODOFDELIVERY.CHEQUE;
      personRegistrationDetails.currencyType =
        curam.codetable.CURRENCY.DEFAULTCODE;
      // END, CR00141773

      // Add email address details
      personRegistrationDetails.contactEmailAddress = "test@gmail.com";
      personRegistrationDetails.contactEmailType = EMAILTYPE.PERSONAL;

      final WizardStateID wizardStateID = new WizardStateID();
      final ActionIDProperty actionIDProperty = new ActionIDProperty();
      actionIDProperty.actionIDProperty = "Save";

      final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
        new BDMPersonRegistrationDetails();
      bdmPersonRegistrationDetails.dtls = personRegistrationDetails;
      bdmPersonRegistrationDetails.isMailingAddSame = true;

      final BDMParticipantRegistrationDetails participantRegistration =
        BDMParticipantRegistrationDetailsFactory.newInstance();

      participantRegistration.setRegisterPersonForPDCDetails(
        bdmPersonRegistrationDetails, wizardStateID, actionIDProperty);

    } catch (final InformationalException e) {

      caught = true;
      assertEquals(ERR_ADDR_CA_POSTALCODE_MISSING, e.getMessage());
    }

    assertTrue(caught);

  }

  /**
   * Test to check CA address Province Missing validation
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testPersonRegistrationAddressProvMisingFailure()
    throws AppException, InformationalException {

    boolean caught = false;

    try {

      final java.util.Calendar cal = new java.util.GregorianCalendar(1970,
        java.util.Calendar.APRIL, 1, 0, 0, 0);
      final curam.util.type.Date birthday = new curam.util.type.Date(cal);

      final PersonRegistrationDetails personRegistrationDetails =
        new PersonRegistrationDetails();

      personRegistrationDetails.firstForename = "Test";
      personRegistrationDetails.surname = "Person";
      // BEGIN, CR00446316, YF
      // END, CR00446316, YF
      personRegistrationDetails.sex = curam.codetable.GENDER.getDefaultCode();
      personRegistrationDetails.dateOfBirth = birthday;
      personRegistrationDetails.registrationDate =
        curam.util.type.Date.getCurrentDate();
      personRegistrationDetails.currentMaritalStatus =
        curam.codetable.MARITALSTATUS.MARRIED;
      personRegistrationDetails.nationality =
        curam.codetable.NATIONALITY.DEFAULTCODE;
      personRegistrationDetails.birthCountry = curam.codetable.COUNTRY.GB;

      // parseFieldsToData always make US address type - it is irrelevant what
      // is defined here.
      personRegistrationDetails.addressType =
        CONCERNROLEADDRESSTYPE.PRIVATE.toString();

      final AddressFieldDetails addressFieldDetails =
        new AddressFieldDetails();
      final curam.core.intf.AddressData addressDataObj =
        curam.core.fact.AddressDataFactory.newInstance();

      addressFieldDetails.addressLayoutType =
        curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;

      // BEGIN, CR00272990 , KRK
      addressFieldDetails.suiteNum = "4994";
      addressFieldDetails.addressLine1 = "Heatherleigh";
      addressFieldDetails.addressLine2 = "Cooksville";
      addressFieldDetails.province = "";
      addressFieldDetails.postalCode = "A1A 1A1";
      addressFieldDetails.city = "Ontario";
      // BEGIN, CR00380472, MV
      // END, CR00380472
      addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;
      // END, CR00272990
      OtherAddressData otherAddressData = null;

      otherAddressData =
        addressDataObj.parseFieldsToData(addressFieldDetails);

      personRegistrationDetails.addressData = otherAddressData.addressData;

      // BEGIN, CR00141773, CW
      personRegistrationDetails.paymentFrequency = "100100100";
      personRegistrationDetails.methodOfPmtCode = METHODOFDELIVERY.CHEQUE;
      personRegistrationDetails.currencyType =
        curam.codetable.CURRENCY.DEFAULTCODE;
      // END, CR00141773

      // Add email address details
      personRegistrationDetails.contactEmailAddress = "test@gmail.com";
      personRegistrationDetails.contactEmailType = EMAILTYPE.PERSONAL;

      final WizardStateID wizardStateID = new WizardStateID();
      final ActionIDProperty actionIDProperty = new ActionIDProperty();
      actionIDProperty.actionIDProperty = "Save";

      final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
        new BDMPersonRegistrationDetails();
      bdmPersonRegistrationDetails.dtls = personRegistrationDetails;
      bdmPersonRegistrationDetails.isMailingAddSame = true;

      final BDMParticipantRegistrationDetails participantRegistration =
        BDMParticipantRegistrationDetailsFactory.newInstance();

      participantRegistration.setRegisterPersonForPDCDetails(
        bdmPersonRegistrationDetails, wizardStateID, actionIDProperty);

    } catch (final InformationalException e) {

      caught = true;
      assertEquals(ERR_ADDR_CA_PROV_MISSING, e.getMessage());
    }

    assertTrue(caught);

  }

  /**
   * Test to check CA address City Missing validation
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testPersonRegistrationAddressCityMisingFailure()
    throws AppException, InformationalException {

    boolean caught = false;

    try {

      final java.util.Calendar cal = new java.util.GregorianCalendar(1970,
        java.util.Calendar.APRIL, 1, 0, 0, 0);
      final curam.util.type.Date birthday = new curam.util.type.Date(cal);

      final PersonRegistrationDetails personRegistrationDetails =
        new PersonRegistrationDetails();

      personRegistrationDetails.firstForename = "Test";
      personRegistrationDetails.surname = "Person";
      // BEGIN, CR00446316, YF
      // END, CR00446316, YF
      personRegistrationDetails.sex = curam.codetable.GENDER.getDefaultCode();
      personRegistrationDetails.dateOfBirth = birthday;
      personRegistrationDetails.registrationDate =
        curam.util.type.Date.getCurrentDate();
      personRegistrationDetails.currentMaritalStatus =
        curam.codetable.MARITALSTATUS.MARRIED;
      personRegistrationDetails.nationality =
        curam.codetable.NATIONALITY.DEFAULTCODE;
      personRegistrationDetails.birthCountry = curam.codetable.COUNTRY.GB;

      // parseFieldsToData always make US address type - it is irrelevant what
      // is defined here.
      personRegistrationDetails.addressType =
        CONCERNROLEADDRESSTYPE.PRIVATE.toString();

      final AddressFieldDetails addressFieldDetails =
        new AddressFieldDetails();
      final curam.core.intf.AddressData addressDataObj =
        curam.core.fact.AddressDataFactory.newInstance();

      addressFieldDetails.addressLayoutType =
        curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;

      // BEGIN, CR00272990 , KRK
      addressFieldDetails.suiteNum = "4994";
      addressFieldDetails.addressLine1 = "Heatherleigh";
      addressFieldDetails.addressLine2 = "Cooksville";
      addressFieldDetails.province = PROVINCETYPE.ALBERTA;
      addressFieldDetails.postalCode = "A1A 1A1";
      addressFieldDetails.city = "";
      // BEGIN, CR00380472, MV
      // END, CR00380472
      addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;
      // END, CR00272990
      OtherAddressData otherAddressData = null;

      otherAddressData =
        addressDataObj.parseFieldsToData(addressFieldDetails);

      personRegistrationDetails.addressData = otherAddressData.addressData;

      // BEGIN, CR00141773, CW
      personRegistrationDetails.paymentFrequency = "100100100";
      personRegistrationDetails.methodOfPmtCode = METHODOFDELIVERY.CHEQUE;
      personRegistrationDetails.currencyType =
        curam.codetable.CURRENCY.DEFAULTCODE;
      // END, CR00141773

      // Add email address details
      personRegistrationDetails.contactEmailAddress = "test@gmail.com";
      personRegistrationDetails.contactEmailType = EMAILTYPE.PERSONAL;

      final WizardStateID wizardStateID = new WizardStateID();
      final ActionIDProperty actionIDProperty = new ActionIDProperty();
      actionIDProperty.actionIDProperty = "Save";

      final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
        new BDMPersonRegistrationDetails();
      bdmPersonRegistrationDetails.dtls = personRegistrationDetails;
      bdmPersonRegistrationDetails.isMailingAddSame = true;

      final BDMParticipantRegistrationDetails participantRegistration =
        BDMParticipantRegistrationDetailsFactory.newInstance();

      participantRegistration.setRegisterPersonForPDCDetails(
        bdmPersonRegistrationDetails, wizardStateID, actionIDProperty);

    } catch (final InformationalException e) {

      caught = true;
      assertEquals(ERR_ADDR_CA_CITY_MISSING, e.getMessage());
    }

    assertTrue(caught);

  }

  /**
   * Test to check phone Area Code missing validation
   * for countries with +1 country code
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testPersonRegistrationPhoneAreaCodeFailure()
    throws AppException, InformationalException {

    boolean caught = false;

    try {
      final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
        new BDMPersonRegistrationDetails();
      final java.util.Calendar cal = new java.util.GregorianCalendar(1970,
        java.util.Calendar.APRIL, 1, 0, 0, 0);
      final curam.util.type.Date birthday = new curam.util.type.Date(cal);

      final PersonRegistrationDetails personRegistrationDetails =
        new PersonRegistrationDetails();

      personRegistrationDetails.firstForename = "Test";
      personRegistrationDetails.surname = "Person";
      // BEGIN, CR00446316, YF
      // END, CR00446316, YF
      personRegistrationDetails.sex = curam.codetable.GENDER.getDefaultCode();
      personRegistrationDetails.dateOfBirth = birthday;
      personRegistrationDetails.registrationDate =
        curam.util.type.Date.getCurrentDate();
      personRegistrationDetails.currentMaritalStatus =
        curam.codetable.MARITALSTATUS.MARRIED;
      personRegistrationDetails.nationality =
        curam.codetable.NATIONALITY.DEFAULTCODE;
      personRegistrationDetails.birthCountry = curam.codetable.COUNTRY.GB;

      // parseFieldsToData always make US address type - it is irrelevant what
      // is defined here.
      personRegistrationDetails.addressType =
        CONCERNROLEADDRESSTYPE.PRIVATE.toString();

      final AddressFieldDetails addressFieldDetails =
        new AddressFieldDetails();
      final curam.core.intf.AddressData addressDataObj =
        curam.core.fact.AddressDataFactory.newInstance();

      addressFieldDetails.addressLayoutType =
        curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;

      // BEGIN, CR00272990 , KRK
      addressFieldDetails.suiteNum = "4994";
      addressFieldDetails.addressLine1 = "Heatherleigh";
      addressFieldDetails.addressLine2 = "Cooksville";
      addressFieldDetails.province = PROVINCETYPE.ALBERTA;
      addressFieldDetails.postalCode = "A1A 1A1";
      addressFieldDetails.city = "test";
      // BEGIN, CR00380472, MV
      // END, CR00380472
      addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;
      // END, CR00272990
      OtherAddressData otherAddressData = null;

      otherAddressData =
        addressDataObj.parseFieldsToData(addressFieldDetails);

      personRegistrationDetails.addressData = otherAddressData.addressData;

      // BEGIN, CR00141773, CW
      personRegistrationDetails.paymentFrequency = "100100100";
      personRegistrationDetails.methodOfPmtCode = METHODOFDELIVERY.CHEQUE;
      personRegistrationDetails.currencyType =
        curam.codetable.CURRENCY.DEFAULTCODE;
      // END, CR00141773

      // Add email address details
      personRegistrationDetails.contactEmailAddress = "test@gmail.com";
      personRegistrationDetails.contactEmailType = EMAILTYPE.PERSONAL;

      bdmPersonRegistrationDetails.phoneCountryCode = BDMPHONECOUNTRY.CANADA;

      personRegistrationDetails.phoneAreaCode = "";
      personRegistrationDetails.phoneNumber = "4567890";
      personRegistrationDetails.phoneExtension = "";
      personRegistrationDetails.phoneType = PHONETYPE.PERSONAL_MOBILE;

      final WizardStateID wizardStateID = new WizardStateID();
      final ActionIDProperty actionIDProperty = new ActionIDProperty();
      actionIDProperty.actionIDProperty = "Save";

      bdmPersonRegistrationDetails.dtls = personRegistrationDetails;
      bdmPersonRegistrationDetails.isMailingAddSame = true;

      final BDMParticipantRegistrationDetails participantRegistration =
        BDMParticipantRegistrationDetailsFactory.newInstance();

      participantRegistration.setRegisterPersonForPDCDetails(
        bdmPersonRegistrationDetails, wizardStateID, actionIDProperty);

    } catch (final InformationalException e) {

      caught = true;
      assertEquals(ERR_PHONE_AREA_CODE, e.getMessage());
    }

    assertTrue(caught);

  }

  /**
   * Test to check phone Area Code is 3 Digit validation
   * for countries with +1 country code
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testPersonRegistrationPhoneAreaCode3DigitFailure()
    throws AppException, InformationalException {

    boolean caught = false;

    try {
      final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
        new BDMPersonRegistrationDetails();
      final java.util.Calendar cal = new java.util.GregorianCalendar(1970,
        java.util.Calendar.APRIL, 1, 0, 0, 0);
      final curam.util.type.Date birthday = new curam.util.type.Date(cal);

      final PersonRegistrationDetails personRegistrationDetails =
        new PersonRegistrationDetails();

      personRegistrationDetails.firstForename = "Test";
      personRegistrationDetails.surname = "Person";
      // BEGIN, CR00446316, YF
      // END, CR00446316, YF
      personRegistrationDetails.sex = curam.codetable.GENDER.getDefaultCode();
      personRegistrationDetails.dateOfBirth = birthday;
      personRegistrationDetails.registrationDate =
        curam.util.type.Date.getCurrentDate();
      personRegistrationDetails.currentMaritalStatus =
        curam.codetable.MARITALSTATUS.MARRIED;
      personRegistrationDetails.nationality =
        curam.codetable.NATIONALITY.DEFAULTCODE;
      personRegistrationDetails.birthCountry = curam.codetable.COUNTRY.GB;

      // parseFieldsToData always make US address type - it is irrelevant what
      // is defined here.
      personRegistrationDetails.addressType =
        CONCERNROLEADDRESSTYPE.PRIVATE.toString();

      final AddressFieldDetails addressFieldDetails =
        new AddressFieldDetails();
      final curam.core.intf.AddressData addressDataObj =
        curam.core.fact.AddressDataFactory.newInstance();

      addressFieldDetails.addressLayoutType =
        curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;

      // BEGIN, CR00272990 , KRK
      addressFieldDetails.suiteNum = "4994";
      addressFieldDetails.addressLine1 = "Heatherleigh";
      addressFieldDetails.addressLine2 = "Cooksville";
      addressFieldDetails.province = PROVINCETYPE.ALBERTA;
      addressFieldDetails.postalCode = "A1A 1A1";
      addressFieldDetails.city = "test";
      // BEGIN, CR00380472, MV
      // END, CR00380472
      addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;
      // END, CR00272990
      OtherAddressData otherAddressData = null;

      otherAddressData =
        addressDataObj.parseFieldsToData(addressFieldDetails);

      personRegistrationDetails.addressData = otherAddressData.addressData;

      // BEGIN, CR00141773, CW
      personRegistrationDetails.paymentFrequency = "100100100";
      personRegistrationDetails.methodOfPmtCode = METHODOFDELIVERY.CHEQUE;
      personRegistrationDetails.currencyType =
        curam.codetable.CURRENCY.DEFAULTCODE;
      // END, CR00141773

      // Add email address details
      personRegistrationDetails.contactEmailAddress = "test@gmail.com";
      personRegistrationDetails.contactEmailType = EMAILTYPE.PERSONAL;

      bdmPersonRegistrationDetails.phoneCountryCode = BDMPHONECOUNTRY.CANADA;

      personRegistrationDetails.phoneAreaCode = "1234";
      personRegistrationDetails.phoneNumber = "4567890";
      personRegistrationDetails.phoneExtension = "";
      personRegistrationDetails.phoneType = PHONETYPE.PERSONAL_MOBILE;

      final WizardStateID wizardStateID = new WizardStateID();
      final ActionIDProperty actionIDProperty = new ActionIDProperty();
      actionIDProperty.actionIDProperty = "Save";

      bdmPersonRegistrationDetails.dtls = personRegistrationDetails;
      bdmPersonRegistrationDetails.isMailingAddSame = true;

      final BDMParticipantRegistrationDetails participantRegistration =
        BDMParticipantRegistrationDetailsFactory.newInstance();

      participantRegistration.setRegisterPersonForPDCDetails(
        bdmPersonRegistrationDetails, wizardStateID, actionIDProperty);

    } catch (final InformationalException e) {

      caught = true;
      assertEquals(ERR_PHONE_AREA_CODE_3DIGIT, e.getMessage());
    }

    assertTrue(caught);

  }

  /**
   * Test to check phone number is 7 Digit validation
   * for countries with +1 country code
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testPersonRegistrationPhoneNumber7DigitFailure()
    throws AppException, InformationalException {

    boolean caught = false;

    try {
      final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
        new BDMPersonRegistrationDetails();
      final java.util.Calendar cal = new java.util.GregorianCalendar(1970,
        java.util.Calendar.APRIL, 1, 0, 0, 0);
      final curam.util.type.Date birthday = new curam.util.type.Date(cal);

      final PersonRegistrationDetails personRegistrationDetails =
        new PersonRegistrationDetails();

      personRegistrationDetails.firstForename = "Test";
      personRegistrationDetails.surname = "Person";
      // BEGIN, CR00446316, YF
      // END, CR00446316, YF
      personRegistrationDetails.sex = curam.codetable.GENDER.getDefaultCode();
      personRegistrationDetails.dateOfBirth = birthday;
      personRegistrationDetails.registrationDate =
        curam.util.type.Date.getCurrentDate();
      personRegistrationDetails.currentMaritalStatus =
        curam.codetable.MARITALSTATUS.MARRIED;
      personRegistrationDetails.nationality =
        curam.codetable.NATIONALITY.DEFAULTCODE;
      personRegistrationDetails.birthCountry = curam.codetable.COUNTRY.GB;

      // parseFieldsToData always make US address type - it is irrelevant what
      // is defined here.
      personRegistrationDetails.addressType =
        CONCERNROLEADDRESSTYPE.PRIVATE.toString();

      final AddressFieldDetails addressFieldDetails =
        new AddressFieldDetails();
      final curam.core.intf.AddressData addressDataObj =
        curam.core.fact.AddressDataFactory.newInstance();

      addressFieldDetails.addressLayoutType =
        curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;

      // BEGIN, CR00272990 , KRK
      addressFieldDetails.suiteNum = "4994";
      addressFieldDetails.addressLine1 = "Heatherleigh";
      addressFieldDetails.addressLine2 = "Cooksville";
      addressFieldDetails.province = PROVINCETYPE.ALBERTA;
      addressFieldDetails.postalCode = "A1A 1A1";
      addressFieldDetails.city = "test";
      // BEGIN, CR00380472, MV
      // END, CR00380472
      addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;
      // END, CR00272990
      OtherAddressData otherAddressData = null;

      otherAddressData =
        addressDataObj.parseFieldsToData(addressFieldDetails);

      personRegistrationDetails.addressData = otherAddressData.addressData;

      // BEGIN, CR00141773, CW
      personRegistrationDetails.paymentFrequency = "100100100";
      personRegistrationDetails.methodOfPmtCode = METHODOFDELIVERY.CHEQUE;
      personRegistrationDetails.currencyType =
        curam.codetable.CURRENCY.DEFAULTCODE;
      // END, CR00141773

      // Add email address details
      personRegistrationDetails.contactEmailAddress = "test@gmail.com";
      personRegistrationDetails.contactEmailType = EMAILTYPE.PERSONAL;

      bdmPersonRegistrationDetails.phoneCountryCode = BDMPHONECOUNTRY.CANADA;

      personRegistrationDetails.phoneAreaCode = "123";
      personRegistrationDetails.phoneNumber = "456780";
      personRegistrationDetails.phoneExtension = "";
      personRegistrationDetails.phoneType = PHONETYPE.PERSONAL_MOBILE;

      final WizardStateID wizardStateID = new WizardStateID();
      final ActionIDProperty actionIDProperty = new ActionIDProperty();
      actionIDProperty.actionIDProperty = "Save";

      bdmPersonRegistrationDetails.dtls = personRegistrationDetails;
      bdmPersonRegistrationDetails.isMailingAddSame = true;

      final BDMParticipantRegistrationDetails participantRegistration =
        BDMParticipantRegistrationDetailsFactory.newInstance();

      participantRegistration.setRegisterPersonForPDCDetails(
        bdmPersonRegistrationDetails, wizardStateID, actionIDProperty);

    } catch (final InformationalException e) {

      caught = true;
      assertEquals(ERR_PHONE_NUMBER_7DIGIT, e.getMessage());
    }

    assertTrue(caught);

  }

  /**
   * Test to check phone number is is non-Numeric validation
   * for countries with +1 country code
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testPersonRegistrationPhoneNumberNumericFailure()
    throws AppException, InformationalException {

    boolean caught = false;

    try {
      final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
        new BDMPersonRegistrationDetails();
      final java.util.Calendar cal = new java.util.GregorianCalendar(1970,
        java.util.Calendar.APRIL, 1, 0, 0, 0);
      final curam.util.type.Date birthday = new curam.util.type.Date(cal);

      final PersonRegistrationDetails personRegistrationDetails =
        new PersonRegistrationDetails();

      personRegistrationDetails.firstForename = "Test";
      personRegistrationDetails.surname = "Person";
      // BEGIN, CR00446316, YF
      // END, CR00446316, YF
      personRegistrationDetails.sex = curam.codetable.GENDER.getDefaultCode();
      personRegistrationDetails.dateOfBirth = birthday;
      personRegistrationDetails.registrationDate =
        curam.util.type.Date.getCurrentDate();
      personRegistrationDetails.currentMaritalStatus =
        curam.codetable.MARITALSTATUS.MARRIED;
      personRegistrationDetails.nationality =
        curam.codetable.NATIONALITY.DEFAULTCODE;
      personRegistrationDetails.birthCountry = curam.codetable.COUNTRY.GB;

      // parseFieldsToData always make US address type - it is irrelevant what
      // is defined here.
      personRegistrationDetails.addressType =
        CONCERNROLEADDRESSTYPE.PRIVATE.toString();

      final AddressFieldDetails addressFieldDetails =
        new AddressFieldDetails();
      final curam.core.intf.AddressData addressDataObj =
        curam.core.fact.AddressDataFactory.newInstance();

      addressFieldDetails.addressLayoutType =
        curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;

      // BEGIN, CR00272990 , KRK
      addressFieldDetails.suiteNum = "4994";
      addressFieldDetails.addressLine1 = "Heatherleigh";
      addressFieldDetails.addressLine2 = "Cooksville";
      addressFieldDetails.province = PROVINCETYPE.ALBERTA;
      addressFieldDetails.postalCode = "A1A 1A1";
      addressFieldDetails.city = "test";
      // BEGIN, CR00380472, MV
      // END, CR00380472
      addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;
      // END, CR00272990
      OtherAddressData otherAddressData = null;

      otherAddressData =
        addressDataObj.parseFieldsToData(addressFieldDetails);

      personRegistrationDetails.addressData = otherAddressData.addressData;

      // BEGIN, CR00141773, CW
      personRegistrationDetails.paymentFrequency = "100100100";
      personRegistrationDetails.methodOfPmtCode = METHODOFDELIVERY.CHEQUE;
      personRegistrationDetails.currencyType =
        curam.codetable.CURRENCY.DEFAULTCODE;
      // END, CR00141773

      // Add email address details
      personRegistrationDetails.contactEmailAddress = "test@gmail.com";
      personRegistrationDetails.contactEmailType = EMAILTYPE.PERSONAL;

      bdmPersonRegistrationDetails.phoneCountryCode =
        BDMPHONECOUNTRY.AFGHANISTAN;

      personRegistrationDetails.phoneAreaCode = "sd";
      personRegistrationDetails.phoneNumber = "sd";
      personRegistrationDetails.phoneExtension = "";
      personRegistrationDetails.phoneType = PHONETYPE.PERSONAL_MOBILE;

      final WizardStateID wizardStateID = new WizardStateID();
      final ActionIDProperty actionIDProperty = new ActionIDProperty();
      actionIDProperty.actionIDProperty = "Save";

      bdmPersonRegistrationDetails.dtls = personRegistrationDetails;
      bdmPersonRegistrationDetails.isMailingAddSame = true;

      final BDMParticipantRegistrationDetails participantRegistration =
        BDMParticipantRegistrationDetailsFactory.newInstance();

      participantRegistration.setRegisterPersonForPDCDetails(
        bdmPersonRegistrationDetails, wizardStateID, actionIDProperty);

    } catch (final InformationalException e) {

      caught = true;
      assertEquals(ERR_PHONE_NUMBER_OTHER_COUNTRY, e.getMessage());
    }

    assertTrue(caught);

  }

  /**
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testPersonRegistrationInvalidPhoneTypeFailure()
    throws AppException, InformationalException {

    boolean caught = false;

    try {
      final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
        new BDMPersonRegistrationDetails();
      final java.util.Calendar cal = new java.util.GregorianCalendar(1970,
        java.util.Calendar.APRIL, 1, 0, 0, 0);
      final curam.util.type.Date birthday = new curam.util.type.Date(cal);

      final PersonRegistrationDetails personRegistrationDetails =
        new PersonRegistrationDetails();

      personRegistrationDetails.firstForename = "Test";
      personRegistrationDetails.surname = "Person";
      // BEGIN, CR00446316, YF
      // END, CR00446316, YF
      personRegistrationDetails.sex = curam.codetable.GENDER.getDefaultCode();
      personRegistrationDetails.dateOfBirth = birthday;
      personRegistrationDetails.registrationDate =
        curam.util.type.Date.getCurrentDate();
      personRegistrationDetails.currentMaritalStatus =
        curam.codetable.MARITALSTATUS.MARRIED;
      personRegistrationDetails.nationality =
        curam.codetable.NATIONALITY.DEFAULTCODE;
      personRegistrationDetails.birthCountry = curam.codetable.COUNTRY.GB;

      // parseFieldsToData always make US address type - it is irrelevant what
      // is defined here.
      personRegistrationDetails.addressType =
        CONCERNROLEADDRESSTYPE.PRIVATE.toString();

      final AddressFieldDetails addressFieldDetails =
        new AddressFieldDetails();
      final curam.core.intf.AddressData addressDataObj =
        curam.core.fact.AddressDataFactory.newInstance();

      addressFieldDetails.addressLayoutType =
        curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;

      // BEGIN, CR00272990 , KRK
      addressFieldDetails.suiteNum = "4994";
      addressFieldDetails.addressLine1 = "Heatherleigh";
      addressFieldDetails.addressLine2 = "Cooksville";
      addressFieldDetails.province = PROVINCETYPE.ALBERTA;
      addressFieldDetails.postalCode = "A1A 1A1";
      addressFieldDetails.city = "test";
      // BEGIN, CR00380472, MV
      // END, CR00380472
      addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;
      // END, CR00272990
      OtherAddressData otherAddressData = null;

      otherAddressData =
        addressDataObj.parseFieldsToData(addressFieldDetails);

      personRegistrationDetails.addressData = otherAddressData.addressData;

      // BEGIN, CR00141773, CW
      personRegistrationDetails.paymentFrequency = "100100100";
      personRegistrationDetails.methodOfPmtCode = METHODOFDELIVERY.CHEQUE;
      personRegistrationDetails.currencyType =
        curam.codetable.CURRENCY.DEFAULTCODE;
      // END, CR00141773

      // Add email address details
      personRegistrationDetails.contactEmailAddress = "test@gmail.com";
      personRegistrationDetails.contactEmailType = EMAILTYPE.PERSONAL;

      bdmPersonRegistrationDetails.phoneCountryCode =
        BDMPHONECOUNTRY.AFGHANISTAN;

      personRegistrationDetails.phoneAreaCode = "123";
      personRegistrationDetails.phoneNumber = "4567890";
      personRegistrationDetails.phoneExtension = "123";
      personRegistrationDetails.phoneType = PHONETYPE.PERSONAL_MOBILE;

      final WizardStateID wizardStateID = new WizardStateID();
      final ActionIDProperty actionIDProperty = new ActionIDProperty();
      actionIDProperty.actionIDProperty = "Save";

      bdmPersonRegistrationDetails.dtls = personRegistrationDetails;
      bdmPersonRegistrationDetails.isMailingAddSame = true;

      final BDMParticipantRegistrationDetails participantRegistration =
        BDMParticipantRegistrationDetailsFactory.newInstance();

      participantRegistration.setRegisterPersonForPDCDetails(
        bdmPersonRegistrationDetails, wizardStateID, actionIDProperty);

    } catch (final InformationalException e) {

      caught = true;
      assertEquals(ERR_PHONE_INVALID_TYPE, e.getMessage());
    }

    assertTrue(caught);

  }

  /**
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testPersonRegistrationPhoneExtensionFailure()
    throws AppException, InformationalException {

    boolean caught = false;

    try {
      final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
        new BDMPersonRegistrationDetails();
      final java.util.Calendar cal = new java.util.GregorianCalendar(1970,
        java.util.Calendar.APRIL, 1, 0, 0, 0);
      final curam.util.type.Date birthday = new curam.util.type.Date(cal);

      final PersonRegistrationDetails personRegistrationDetails =
        new PersonRegistrationDetails();

      personRegistrationDetails.firstForename = "Test";
      personRegistrationDetails.surname = "Person";
      // BEGIN, CR00446316, YF
      // END, CR00446316, YF
      personRegistrationDetails.sex = curam.codetable.GENDER.getDefaultCode();
      personRegistrationDetails.dateOfBirth = birthday;
      personRegistrationDetails.registrationDate =
        curam.util.type.Date.getCurrentDate();
      personRegistrationDetails.currentMaritalStatus =
        curam.codetable.MARITALSTATUS.MARRIED;
      personRegistrationDetails.nationality =
        curam.codetable.NATIONALITY.DEFAULTCODE;
      personRegistrationDetails.birthCountry = curam.codetable.COUNTRY.GB;

      // parseFieldsToData always make US address type - it is irrelevant what
      // is defined here.
      personRegistrationDetails.addressType =
        CONCERNROLEADDRESSTYPE.PRIVATE.toString();

      final AddressFieldDetails addressFieldDetails =
        new AddressFieldDetails();
      final curam.core.intf.AddressData addressDataObj =
        curam.core.fact.AddressDataFactory.newInstance();

      addressFieldDetails.addressLayoutType =
        curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;

      // BEGIN, CR00272990 , KRK
      addressFieldDetails.suiteNum = "4994";
      addressFieldDetails.addressLine1 = "Heatherleigh";
      addressFieldDetails.addressLine2 = "Cooksville";
      addressFieldDetails.province = PROVINCETYPE.ALBERTA;
      addressFieldDetails.postalCode = "A1A 1A1";
      addressFieldDetails.city = "test";
      // BEGIN, CR00380472, MV
      // END, CR00380472
      addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;
      // END, CR00272990
      OtherAddressData otherAddressData = null;

      otherAddressData =
        addressDataObj.parseFieldsToData(addressFieldDetails);

      personRegistrationDetails.addressData = otherAddressData.addressData;

      // BEGIN, CR00141773, CW
      personRegistrationDetails.paymentFrequency = "100100100";
      personRegistrationDetails.methodOfPmtCode = METHODOFDELIVERY.CHEQUE;
      personRegistrationDetails.currencyType =
        curam.codetable.CURRENCY.DEFAULTCODE;
      // END, CR00141773

      // Add email address details
      personRegistrationDetails.contactEmailAddress = "test@gmail.com";
      personRegistrationDetails.contactEmailType = EMAILTYPE.PERSONAL;

      bdmPersonRegistrationDetails.phoneCountryCode =
        BDMPHONECOUNTRY.AFGHANISTAN;

      personRegistrationDetails.phoneAreaCode = "123";
      personRegistrationDetails.phoneNumber = "4567890";
      personRegistrationDetails.phoneExtension = "1as";
      personRegistrationDetails.phoneType = PHONETYPE.BUSINESS_LANDLINE;

      final WizardStateID wizardStateID = new WizardStateID();
      final ActionIDProperty actionIDProperty = new ActionIDProperty();
      actionIDProperty.actionIDProperty = "Save";

      bdmPersonRegistrationDetails.dtls = personRegistrationDetails;
      bdmPersonRegistrationDetails.isMailingAddSame = true;

      final BDMParticipantRegistrationDetails participantRegistration =
        BDMParticipantRegistrationDetailsFactory.newInstance();

      participantRegistration.setRegisterPersonForPDCDetails(
        bdmPersonRegistrationDetails, wizardStateID, actionIDProperty);

    } catch (final InformationalException e) {

      caught = true;
      assertEquals(ERR_PHONE_EXTENSION_NUMERIC, e.getMessage());
    }

    assertTrue(caught);

  }

  public PDCPersonDetails createPDCPerson()
    throws AppException, InformationalException {

    final PersonRegistrationDetails personRegistrationDetails =
      registerPerson.getPersonRegistrationDetails();

    final PersonRegistration personRegistration =
      PersonRegistrationFactory.newInstance();

    final RegistrationIDDetails registrationIDDetails =
      personRegistration.registerPerson(personRegistrationDetails);

    final PersonDtls personDtls =
      registerPerson.readPerson(registrationIDDetails.concernRoleID);

    final PDCPersonDetails pdcPersonDetails = new PDCPersonDetails();

    pdcPersonDetails.assign(personDtls);
    return pdcPersonDetails;

  }

  /**
   * Returns set of input data sufficient for default flow
   *
   * @return Returns registration details for default registration
   */
  public PersonRegistrationDetails getPersonRegistrationDetails()
    throws AppException, InformationalException {

    final java.util.Calendar cal = new java.util.GregorianCalendar(1970,
      java.util.Calendar.APRIL, 1, 0, 0, 0);
    final curam.util.type.Date birthday = new curam.util.type.Date(cal);

    final PersonRegistrationDetails personRegistrationDetails =
      new PersonRegistrationDetails();

    personRegistrationDetails.firstForename = "Test";
    personRegistrationDetails.surname = "Person";
    // BEGIN, CR00446316, YF
    // END, CR00446316, YF
    personRegistrationDetails.sex = curam.codetable.GENDER.getDefaultCode();
    personRegistrationDetails.dateOfBirth = birthday;
    personRegistrationDetails.registrationDate =
      curam.util.type.Date.getCurrentDate();
    personRegistrationDetails.currentMaritalStatus =
      curam.codetable.MARITALSTATUS.MARRIED;
    personRegistrationDetails.nationality =
      curam.codetable.NATIONALITY.DEFAULTCODE;
    personRegistrationDetails.birthCountry = curam.codetable.COUNTRY.GB;

    // parseFieldsToData always make US address type - it is irrelevant what
    // is defined here.
    personRegistrationDetails.addressType =
      CONCERNROLEADDRESSTYPE.PRIVATE.toString();

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;

    // BEGIN, CR00272990 , KRK
    addressFieldDetails.suiteNum = "4994";
    addressFieldDetails.addressLine1 = "Heatherleigh";
    addressFieldDetails.addressLine2 = "Cooksville";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Ontario";
    // BEGIN, CR00380472, MV
    addressFieldDetails.postalCode = "L5A 1V9";
    // END, CR00380472
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;
    // END, CR00272990
    OtherAddressData otherAddressData = null;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    personRegistrationDetails.addressData = otherAddressData.addressData;

    // BEGIN, CR00141773, CW
    personRegistrationDetails.paymentFrequency = "100100100";
    personRegistrationDetails.methodOfPmtCode = METHODOFDELIVERY.CHEQUE;
    personRegistrationDetails.currencyType =
      curam.codetable.CURRENCY.DEFAULTCODE;
    // END, CR00141773

    // Add email address details
    personRegistrationDetails.contactEmailAddress = "test@gmail.com";
    personRegistrationDetails.contactEmailType = EMAILTYPE.PERSONAL;

    return personRegistrationDetails;
  }

}
