package curam.ca.gc.bdm.test.data;

import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.ca.gc.bdm.ws.sinvalidation.impl.SINValidationMod10;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.EMAILTYPE;
import curam.codetable.GENDER;
import curam.codetable.MARITALSTATUS;
import curam.codetable.METHODOFDELIVERY;
import curam.codetable.PROVINCETYPE;
import curam.core.fact.PersonRegistrationFactory;
import curam.core.intf.PersonRegistration;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.OtherAddressData;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonRegistrationDetails;
import curam.core.struct.RegistrationIDDetails;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import curam.util.type.DateTime;

public class BDMPersonTestData {

  public static final String kFirstName_Father = "Father";

  public static final String kFirstName_Mother = "Mother";

  public static final String kFirstName_ChildB = "ChildB";

  public static final String kFirstName_ChildG = "ChildG";

  public static final String kLastName = "Test";

  public static final String kPersonName = "Person";

  public static final Date kDOB_Father =
    Date.getCurrentDate().addDays(-13000);

  public static final Date kDOB_Mother =
    Date.getCurrentDate().addDays(-12300);

  public static final Date kDOB_ChildB = Date.getCurrentDate().addDays(-3650);

  public static final Date kDOB_ChildG = Date.getCurrentDate().addDays(-2800);

  public static final Date kDOB_Person =
    Date.getCurrentDate().addDays(-13000);

  // Common process objects
  curam.core.intf.PersonRegistration personRegistrationObj =
    curam.core.fact.PersonRegistrationFactory.newInstance();

  /**
   * Get current date time string to append in to the names, so that person
   * registration will always have unique name.
   *
   * @return The registered person details
   */
  public String getCurrentDateTimeString()
    throws AppException, InformationalException {

    final String dateTimeStr =
      DateTime.getCurrentDateTime().toString().replaceAll(":", "")
        .replaceAll("-", "").replaceAll("/", "").replaceAll(" ", "");

    return dateTimeStr;
  }

  /**
   * Method to register single person with default details.
   *
   * @return The registered person details
   */
  public BDMPersonTestDataDetails registerPerson_Default()
    throws AppException, InformationalException {

    final BDMPersonTestDataDetails bdmPersonTestReturnDetails =
      new BDMPersonTestDataDetails();
    final String dateTimeStr = this.getCurrentDateTimeString();

    final String firstName = kPersonName + dateTimeStr;
    final String lastName = kLastName + dateTimeStr;
    final String sex = GENDER.MALE;
    final String maritalStatus = MARITALSTATUS.MARRIED;

    // Get the details of the person to be registered
    final PersonRegistrationDetails personRegistrationDetails =
      getPersonRegistrationDetails(firstName, lastName, kDOB_Person, sex,
        maritalStatus);

    final RegistrationIDDetails registrationIDDetails =
      personRegistrationObj.registerPerson(personRegistrationDetails);

    bdmPersonTestReturnDetails.personCRoleID =
      registrationIDDetails.concernRoleID;

    bdmPersonTestReturnDetails.linkedUserName =
      (firstName + lastName).toLowerCase();

    return bdmPersonTestReturnDetails;
  }

  /**
   * Method to register a family of two persons..
   *
   * @return The registered person details
   */
  public BDMPersonTestDataDetails registerFamily_of_two()
    throws AppException, InformationalException {

    final String dateTimeStr = this.getCurrentDateTimeString();

    // Register Peroson1
    final BDMPersonTestDataDetails bdmPersonTestReturnDetails =
      this.registerPerson_Father(dateTimeStr);

    // Register Person two
    final RegistrationIDDetails motherIDDetails =
      this.registerPerson_Mother(dateTimeStr);

    bdmPersonTestReturnDetails.motherCRoleID = motherIDDetails.concernRoleID;

    return bdmPersonTestReturnDetails;
  }

  /**
   * Method to register a family of four persons.
   *
   * @return The registered person details
   */
  public BDMPersonTestDataDetails registerFamily_of_four()
    throws AppException, InformationalException {

    new BDMPersonTestDataDetails();

    final String dateTimeStr = this.getCurrentDateTimeString();

    // Register Peroson1
    final BDMPersonTestDataDetails bdmPersonTestReturnDetails =
      this.registerPerson_Father(dateTimeStr);

    // Register Person two
    final RegistrationIDDetails motherIDDetails =
      this.registerPerson_Mother(dateTimeStr);

    // Register child one
    final RegistrationIDDetails child1Details =
      this.registerPerson_ChildBoy(dateTimeStr);

    // Register child two
    final RegistrationIDDetails child2IDDetails =
      this.registerPerson_ChildGirl(dateTimeStr);

    bdmPersonTestReturnDetails.motherCRoleID = motherIDDetails.concernRoleID;

    bdmPersonTestReturnDetails.child1CRoleID = child1Details.concernRoleID;

    bdmPersonTestReturnDetails.child2CRoleID = child2IDDetails.concernRoleID;

    return bdmPersonTestReturnDetails;
  }

  /**
   * Method to register single person with default details.
   *
   * @return The registered person details
   */
  public BDMPersonTestDataDetails
    registerPerson_Father(final String currentDateTimeStr)
      throws AppException, InformationalException {

    final BDMPersonTestDataDetails bdmPersonTestReturnDetails =
      new BDMPersonTestDataDetails();

    final String firstName = kFirstName_Father + currentDateTimeStr;
    final String lastName = kLastName + currentDateTimeStr;
    final String sex = GENDER.MALE;
    final String maritalStatus = MARITALSTATUS.MARRIED;

    // Get the details of the person to be registered
    final PersonRegistrationDetails personRegistrationDetails =
      getPersonRegistrationDetails(firstName, lastName, kDOB_Father, sex,
        maritalStatus);

    final RegistrationIDDetails registrationIDDetails =
      personRegistrationObj.registerPerson(personRegistrationDetails);

    bdmPersonTestReturnDetails.fatherCRoleID =
      registrationIDDetails.concernRoleID;
    bdmPersonTestReturnDetails.linkedUserName =
      (firstName + lastName).toLowerCase();

    return bdmPersonTestReturnDetails;
  }

  /**
   * Method to register single person with default details.
   *
   * @return The registered person details
   */
  public RegistrationIDDetails
    registerPerson_Mother(final String currentDateTimeStr)
      throws AppException, InformationalException {

    final String firstName = kFirstName_Mother + currentDateTimeStr;
    final String lastName = kLastName + currentDateTimeStr;
    final String sex = GENDER.FEMALE;
    final String maritalStatus = MARITALSTATUS.MARRIED;

    // Get the details of the person to be registered
    final PersonRegistrationDetails personRegistrationDetails =
      getPersonRegistrationDetails(firstName, lastName, kDOB_Mother, sex,
        maritalStatus);

    final RegistrationIDDetails registrationIDDetails =
      personRegistrationObj.registerPerson(personRegistrationDetails);

    return registrationIDDetails;
  }

  /**
   * Method to register single person with default details.
   *
   * @return The registered person details
   */
  public RegistrationIDDetails
    registerPerson_ChildBoy(final String currentDateTimeStr)
      throws AppException, InformationalException {

    final String firstName = kFirstName_ChildB + currentDateTimeStr;
    final String lastName = kLastName + currentDateTimeStr;
    final String sex = GENDER.MALE;
    final String maritalStatus = MARITALSTATUS.SINGLE;

    // Get the details of the person to be registered
    final PersonRegistrationDetails personRegistrationDetails =
      getPersonRegistrationDetails(firstName, lastName, kDOB_ChildB, sex,
        maritalStatus);

    final RegistrationIDDetails registrationIDDetails =
      personRegistrationObj.registerPerson(personRegistrationDetails);

    return registrationIDDetails;
  }

  /**
   * Method to register single person with default details.
   *
   * @return The registered person details
   */
  public RegistrationIDDetails
    registerPerson_ChildGirl(final String currentDateTimeStr)
      throws AppException, InformationalException {

    final String firstName = kFirstName_ChildG + currentDateTimeStr;
    final String lastName = kLastName + currentDateTimeStr;
    final String sex = GENDER.FEMALE;
    final String maritalStatus = MARITALSTATUS.SINGLE;

    // Get the details of the person to be registered
    final PersonRegistrationDetails personRegistrationDetails =
      getPersonRegistrationDetails(firstName, lastName, kDOB_ChildG, sex,
        maritalStatus);

    final RegistrationIDDetails registrationIDDetails =
      personRegistrationObj.registerPerson(personRegistrationDetails);

    return registrationIDDetails;
  }

  /**
   * Sets the Register person object with the input values and values assigned
   * within the method.
   *
   * @return Returns registration details for default registration
   */
  public PersonRegistrationDetails getPersonRegistrationDetails(
    final String fName, final String lName, final Date dob, final String sex,
    final String maritalStatus) throws AppException, InformationalException {

    final PersonRegistrationDetails personRegistrationDetails =
      new PersonRegistrationDetails();

    personRegistrationDetails.firstForename = fName;
    personRegistrationDetails.surname = lName;
    personRegistrationDetails.sex = sex;
    personRegistrationDetails.dateOfBirth = dob;
    personRegistrationDetails.currentMaritalStatus = maritalStatus;
    personRegistrationDetails.registrationDate =
      curam.util.type.Date.getCurrentDate();
    personRegistrationDetails.nationality =
      curam.codetable.NATIONALITY.DEFAULTCODE;
    personRegistrationDetails.birthCountry = curam.codetable.COUNTRY.GB;
    personRegistrationDetails.paymentFrequency = "100100100";
    personRegistrationDetails.methodOfPmtCode = METHODOFDELIVERY.CHEQUE;
    personRegistrationDetails.currencyType =
      curam.codetable.CURRENCY.DEFAULTCODE;
    personRegistrationDetails.contactEmailAddress = "testmail@gmail.com";
    personRegistrationDetails.contactEmailType = EMAILTYPE.PERSONAL;
    // parseFieldsToData always make US address type - it is irrelevant what
    // is defined here.
    personRegistrationDetails.addressType =
      CONCERNROLEADDRESSTYPE.PRIVATE.toString();

    final OtherAddressData otherAddressData = this.getHomeAddress();

    personRegistrationDetails.addressData = otherAddressData.addressData;

    final OtherAddressData mailingAddressData = this.getMailingAddress();

    personRegistrationDetails.mailingAddressData =
      mailingAddressData.addressData;

    return personRegistrationDetails;
  }

  /**
   * Util method to generate valid SIN Number for testing.
   *
   * @return
   */
  public String generateValidSIN() {

    String sinNumber = "";

    do {
      final long nanoTime = System.nanoTime();

      final double randMathNum = Math.random() * 1000;

      final long randomNumer = (long) (nanoTime * randMathNum);

      final String s = randomNumer + "";
      sinNumber = s.substring(0, 9);

    } while (!SINValidationMod10.isValidMOD10(sinNumber));

    return sinNumber;
  }

  /**
   * Sets the Register person object with the input values and values assigned
   * within the method.
   *
   * @return Returns registration details for default registration
   */
  public OtherAddressData getHomeAddress()
    throws AppException, InformationalException {

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;
    addressFieldDetails.suiteNum = "116";
    addressFieldDetails.addressLine1 = "55 Ellerslie Ave";
    addressFieldDetails.addressLine2 = "North West";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Toronto";
    addressFieldDetails.postalCode = "M2N 1X9";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;
    final OtherAddressData otherAddressData =
      addressDataObj.parseFieldsToData(addressFieldDetails);
    return otherAddressData;
  }

  /**
   * Sets the Register person object with the input values and values assigned
   * within the method.
   *
   * @return Returns registration details for default registration
   */
  public OtherAddressData getMailingAddress()
    throws AppException, InformationalException {

    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();
    // Adding the mailing address details
    final AddressFieldDetails mailingAddressFieldDetails =
      new AddressFieldDetails();

    mailingAddressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;
    mailingAddressFieldDetails.suiteNum = "116";
    mailingAddressFieldDetails.addressLine1 = "55 Ellesrlie Ave";
    mailingAddressFieldDetails.addressLine2 = "North West";
    mailingAddressFieldDetails.province = PROVINCETYPE.ONTARIO;
    mailingAddressFieldDetails.city = "Toronto";
    mailingAddressFieldDetails.postalCode = "M2N 1X9";
    mailingAddressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;

    final OtherAddressData mailingAddressData =
      addressDataObj.parseFieldsToData(mailingAddressFieldDetails);

    return mailingAddressData;
  }

  /**
   * This method registers the Person.
   *
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public PDCPersonDetails createPDCPerson(final String fName,
    final String lName, final Date dob)
    throws AppException, InformationalException {

    final RegisterPerson registerPerson =
      new RegisterPerson("RegisterPerson");

    final PersonRegistrationDetails personRegistrationDetails =
      registerPerson.getPersonRegistrationDetails(fName, lName, dob);

    final PersonRegistration personRegistration =
      PersonRegistrationFactory.newInstance();

    final RegistrationIDDetails registrationIDDetails =
      personRegistration.registerPerson(personRegistrationDetails);

    final PersonDtls personDtls =
      registerPerson.readPerson(registrationIDDetails.concernRoleID);

    final PDCPersonDetails pdcPersonDetails = new PDCPersonDetails();

    pdcPersonDetails.assign(personDtls);
    createPDCPerson(pdcPersonDetails);

    return pdcPersonDetails;

  }

  public void createPDCPerson(final PDCPersonDetails pdcPersonDetails)
    throws AppException, InformationalException {

  }

  /**
   * Util method to generate valid SIN Number for testing.
   *
   * @return
   */
  public String generateValidSINStartsWith9() {

    String sinNumber = "";
    boolean isSinStartsWith9 = false;

    do {
      final long nanoTime = System.nanoTime();

      final double randMathNum = Math.random() * 1000;

      final long randomNumer = (long) (nanoTime * randMathNum);

      final String s = randomNumer + "";
      sinNumber = s.substring(0, 9);

      final boolean isValidSin = SINValidationMod10.isValidMOD10(sinNumber);
      if (isValidSin) {
        // Check it starts with 9;
        if (sinNumber.startsWith("9")) {
          isSinStartsWith9 = true;
        }
      }

    } while (!isSinStartsWith9);

    return sinNumber;
  }
}
