
package curam.ca.gc.bdm.test.concern.person;

import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.EMAILTYPE;
import curam.codetable.GENDER;
import curam.codetable.METHODOFDELIVERY;
import curam.codetable.PROVINCETYPE;
import curam.codetable.impl.METHODOFDELIVERYEntry;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.OtherAddressData;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonKey;
import curam.core.struct.PersonRegistrationDetails;
import curam.core.struct.RegistrationIDDetails;
import curam.pdc.fact.PDCPersonFactory;
import curam.pdc.intf.PDCPerson;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;

/**
 * Tests for Process Flow: Register Person
 * This process allows the user to register a person on the system. The user
 * can check if the person is already registered as a person or in another
 * concern role. Once the user is satisfied that the person does not already
 * exist, registration details are recorded for the person. Registration
 * details include personal details, contact details and demographic
 * information.
 */

public class RegisterPerson extends curam.ca.gc.bdm.test.framework.CuramServerTest {

  // Common process objects
  curam.core.intf.PersonRegistration personRegistrationObj =
    curam.core.fact.PersonRegistrationFactory.newInstance();

  // Common Data Structs
  PersonRegistrationDetails personRegistrationDetails;

  RegistrationIDDetails registrationIDDetails = new RegistrationIDDetails();

  PDCPerson pdcPersonObj = PDCPersonFactory.newInstance();

  /*
   * This field is a representation of the equals sign, its value is {@value}
   */
  protected static final char kEquals = '=';

  /*
   * This field is a constant number, its value is {@value}
   */
  protected static final String kOne = "1";

  /*
   * This field is a representation of the new line character,
   * its value is {@value}
   */
  protected static final char kNewLine = '\n';

  /*
   * This field is a representation of the empty string, its value is {@value}
   */
  protected static final String kEmptyString = "";

  /*
   * This field is a sample social security number, its value is {@value}
   */
  protected static final String kSSN = "103882466";

  // ___________________________________________________________________________
  /**
   * Constructor from CuramServerTest
   *
   * @param arg0 The name of the class calling this test
   */
  public RegisterPerson(final String arg0) {

    super(arg0);
  }

  public void testDummy() {

  }

  // ___________________________________________________________________________
  /**
   * Tests the simplest successful flow through the business logic.
   * This test registers a person by first getting the details
   * for the person to be registered then calling PersonRegistration::
   * registerPerson to register a person.
   * It then reads the person details back and asserts that the
   * proper data was inserted.
   *
   * @return The registered person details
   */
  public PersonDtls registerDefault(final String name,
    final METHODOFDELIVERYEntry methodofDelivery)
    throws AppException, InformationalException {

    // Get the details of the person to be registered
    personRegistrationDetails = getPersonRegistrationDetails();
    if (name != null) {
      personRegistrationDetails.surname = name;
    }
    personRegistrationDetails.methodOfPmtCode = methodofDelivery.getCode();
    personRegistrationDetails.sex = GENDER.MALE;

    final PersonDtls personDtls = register(personRegistrationDetails);

    return personDtls;
  }

  // ___________________________________________________________________________
  /**
   * Method to run the actual process of registration and
   * verify that the data has been inserted correctly
   *
   * @param personRegistrationDetails The details of the person
   *
   * @return The registered person details
   */
  private PersonDtls
    register(final PersonRegistrationDetails personRegistrationDetails)
      throws AppException, InformationalException {

    registrationIDDetails =
      personRegistrationObj.registerPerson(personRegistrationDetails);

    final PersonDtls personDtls =
      readPerson(registrationIDDetails.concernRoleID);

    assertEquals(personRegistrationDetails.dateOfBirth,
      personDtls.dateOfBirth);
    assertEquals(personRegistrationDetails.currentMaritalStatus,
      personDtls.maritalStatusCode);
    assertEquals(personRegistrationDetails.sex, personDtls.gender);
    assertEquals(personRegistrationDetails.nationality,
      personDtls.nationalityCode);

    return personDtls;
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
    personRegistrationDetails.birthName = "BirthName";
    personRegistrationDetails.motherBirthSurname = "ParentName";
    personRegistrationDetails.otherForename = "otherName";
    // BEGIN, CR00446316, YF
    // END, CR00446316, YF
    personRegistrationDetails.sex = curam.codetable.GENDER.MALE;
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
      curam.codetable.ADDRESSLAYOUTTYPE.CA;

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

    // Adding the mailing address details
    final AddressFieldDetails mailingAddressFieldDetails =
      new AddressFieldDetails();

    mailingAddressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;
    mailingAddressFieldDetails.suiteNum = "496";
    mailingAddressFieldDetails.addressLine1 = "Young St";
    mailingAddressFieldDetails.addressLine2 = "Belleville";
    mailingAddressFieldDetails.province = PROVINCETYPE.ONTARIO;
    mailingAddressFieldDetails.city = "Ontario";
    mailingAddressFieldDetails.postalCode = "K8R 7T0";
    mailingAddressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;

    final OtherAddressData mailingAddressData =
      addressDataObj.parseFieldsToData(mailingAddressFieldDetails);

    personRegistrationDetails.mailingAddressData =
      mailingAddressData.addressData;

    // Add email address details
    personRegistrationDetails.contactEmailAddress = "test@gmail.com";
    personRegistrationDetails.contactEmailType = EMAILTYPE.PERSONAL;

    return personRegistrationDetails;
  }

  // ___________________________________________________________________________
  /**
   * Method to read the person details
   *
   * @param concernRoleID The concern role id
   *
   * @return The person details
   */
  public PersonDtls readPerson(final long concernRoleID)
    throws AppException, InformationalException {

    final curam.core.intf.Person personObj =
      curam.core.fact.PersonFactory.newInstance();
    PersonDtls personDtls = null;
    final PersonKey personKey = new PersonKey();

    personKey.concernRoleID = concernRoleID;

    personDtls = personObj.read(personKey);

    return personDtls;
  }

  /**
   * Returns set of input data sufficient for default flow
   *
   * @return Returns registration details for default registration
   */
  public PersonRegistrationDetails getPersonRegistrationDetails(
    final String name) throws AppException, InformationalException {

    final java.util.Calendar cal = new java.util.GregorianCalendar(1970,
      java.util.Calendar.APRIL, 1, 0, 0, 0);
    final curam.util.type.Date birthday = new curam.util.type.Date(cal);

    final PersonRegistrationDetails personRegistrationDetails =
      new PersonRegistrationDetails();

    personRegistrationDetails.firstForename = name;
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
      curam.codetable.ADDRESSLAYOUTTYPE.CA;

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

    // Adding the mailing address details
    final AddressFieldDetails mailingAddressFieldDetails =
      new AddressFieldDetails();

    mailingAddressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;
    mailingAddressFieldDetails.suiteNum = "496";
    mailingAddressFieldDetails.addressLine1 = "Young St";
    mailingAddressFieldDetails.addressLine2 = "Belleville";
    mailingAddressFieldDetails.province = PROVINCETYPE.ONTARIO;
    mailingAddressFieldDetails.city = "Ontario";
    mailingAddressFieldDetails.postalCode = "K8R 7T0";
    mailingAddressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;

    final OtherAddressData mailingAddressData =
      addressDataObj.parseFieldsToData(mailingAddressFieldDetails);

    personRegistrationDetails.mailingAddressData =
      mailingAddressData.addressData;

    // Add email address details
    personRegistrationDetails.contactEmailAddress = "test@gmail.com";
    personRegistrationDetails.contactEmailType = EMAILTYPE.PERSONAL;

    return personRegistrationDetails;
  }

  /**
   * Sets the Register person object with the input values and values assigned
   * within the method.
   *
   * @return Returns registration details for default registration
   */
  public PersonRegistrationDetails getPersonRegistrationDetails(
    final String fName, final String lName, final Date dob)
    throws AppException, InformationalException {

    final curam.util.type.Date birthday = dob;

    final PersonRegistrationDetails personRegistrationDetails =
      new PersonRegistrationDetails();

    personRegistrationDetails.firstForename = fName;
    personRegistrationDetails.surname = lName;
    personRegistrationDetails.sex = curam.codetable.GENDER.MALE;
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
      curam.codetable.ADDRESSLAYOUTTYPE.CA;

    addressFieldDetails.suiteNum = "4994";
    addressFieldDetails.addressLine1 = "Heatherleigh";
    addressFieldDetails.addressLine2 = "Cooksville";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Ontario";
    addressFieldDetails.postalCode = "L5A 1V9";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;
    OtherAddressData otherAddressData = null;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    personRegistrationDetails.addressData = otherAddressData.addressData;

    personRegistrationDetails.paymentFrequency = "100100100";
    personRegistrationDetails.methodOfPmtCode = METHODOFDELIVERY.CHEQUE;
    personRegistrationDetails.currencyType =
      curam.codetable.CURRENCY.DEFAULTCODE;

    // Adding the mailing address details
    final AddressFieldDetails mailingAddressFieldDetails =
      new AddressFieldDetails();

    mailingAddressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;
    mailingAddressFieldDetails.suiteNum = "496";
    mailingAddressFieldDetails.addressLine1 = "Young St";
    mailingAddressFieldDetails.addressLine2 = "Belleville";
    mailingAddressFieldDetails.province = PROVINCETYPE.ONTARIO;
    mailingAddressFieldDetails.city = "Ontario";
    mailingAddressFieldDetails.postalCode = "K8R 7T0";
    mailingAddressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;

    final OtherAddressData mailingAddressData =
      addressDataObj.parseFieldsToData(mailingAddressFieldDetails);

    personRegistrationDetails.mailingAddressData =
      mailingAddressData.addressData;

    // Add email address details
    personRegistrationDetails.contactEmailAddress = "test@gmail.com";
    personRegistrationDetails.contactEmailType = EMAILTYPE.PERSONAL;

    return personRegistrationDetails;
  }

  /**
   * Sets the Register person object with the input values and values assigned
   * within the method. Also person province is set to Saskatchewan
   *
   * @return Returns registration details for default registration
   */
  public PersonRegistrationDetails getPersonRegistrationDetailsForSK()
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
    personRegistrationDetails.sex = curam.codetable.GENDER.MALE;
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
    addressFieldDetails.province = PROVINCETYPE.SASKATCHEWAN;
    addressFieldDetails.city = "Saskatoon";
    // BEGIN, CR00380472, MV
    addressFieldDetails.postalCode = "S3S 3S3";
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

    // Adding the mailing address details
    final AddressFieldDetails mailingAddressFieldDetails =
      new AddressFieldDetails();

    mailingAddressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;
    mailingAddressFieldDetails.suiteNum = "496";
    mailingAddressFieldDetails.addressLine1 = "Young St";
    mailingAddressFieldDetails.addressLine2 = "Belleville";
    mailingAddressFieldDetails.province = PROVINCETYPE.SASKATCHEWAN;
    mailingAddressFieldDetails.city = "Saskatoon";
    mailingAddressFieldDetails.postalCode = "S3S 3S3";
    mailingAddressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;

    final OtherAddressData mailingAddressData =
      addressDataObj.parseFieldsToData(mailingAddressFieldDetails);

    personRegistrationDetails.mailingAddressData =
      mailingAddressData.addressData;

    // Add email address details
    personRegistrationDetails.contactEmailAddress = "test@gmail.com";
    personRegistrationDetails.contactEmailType = EMAILTYPE.PERSONAL;

    return personRegistrationDetails;
  }
}
