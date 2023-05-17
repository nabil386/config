package curam.ca.gc.bdm.test.address.impl;

import curam.ca.gc.bdm.message.BDMBPOADDRESS;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.ADDRESSELEMENTTYPE;
import curam.codetable.PROVINCETYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.AddressElementFactory;
import curam.core.fact.AddressFactory;
import curam.core.fact.ConcernRoleAddressFactory;
import curam.core.intf.Address;
import curam.core.intf.AddressElement;
import curam.core.intf.ConcernRoleAddress;
import curam.core.struct.AddressDtls;
import curam.core.struct.AddressElementDtls;
import curam.core.struct.AddressElementDtlsList;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.AddressKey;
import curam.core.struct.AddressReadMultiDtlsList;
import curam.core.struct.ConcernRoleIDStatusCodeKey;
import curam.core.struct.OtherAddressData;
import curam.core.struct.PersonRegistrationDetails;
import curam.core.struct.RegistrationIDDetails;
import curam.core.struct.ValidateAddressResult;
import curam.message.BPOADDRESS;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import java.util.Map;
import org.apache.commons.collections4.map.HashedMap;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AddressFormatTest extends CuramServerTestJUnit4 {

  PersonRegistrationDetails personRegistrationDetails;

  curam.core.intf.PersonRegistration personRegistrationObj;

  ConcernRoleAddress crAddressObj;

  AddressElement addressElementObj;

  @Override
  protected void setUpCuramServerTest() {

    personRegistrationObj =
      curam.core.fact.PersonRegistrationFactory.newInstance();

    crAddressObj = ConcernRoleAddressFactory.newInstance();

    addressElementObj = AddressElementFactory.newInstance();

    final RegisterPerson registerPersonObj = new RegisterPerson(getName());
    try {
      // use existing code to populate person registration details, we only need
      // to modify evidence
      personRegistrationDetails =
        registerPersonObj.getPersonRegistrationDetails();
    } catch (AppException | InformationalException e) {
      fail("There was an error retrieving person registration details");
      e.printStackTrace();
    }

    super.setUpCuramServerTest();
  }

  /**
   * Tests that a Canadian address is correctly added and each address element
   * has the appropriate value
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testCanadianAddress()
    throws AppException, InformationalException {

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    // modify person address details
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;
    addressFieldDetails.suiteNum = "Unit 123";
    addressFieldDetails.addressLine1 = "180";
    addressFieldDetails.addressLine2 = "Richmond Road";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Ontario";
    addressFieldDetails.postalCode = "L5A 1V9";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;

    OtherAddressData otherAddressData = null;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    personRegistrationDetails.addressData = otherAddressData.addressData;
    personRegistrationDetails.mailingAddressData =
      otherAddressData.addressData;

    // register the person
    final RegistrationIDDetails registrationIDDetails =
      personRegistrationObj.registerPerson(personRegistrationDetails);

    // find the concern role address record corresponding to the newly
    // registered person
    final ConcernRoleIDStatusCodeKey crAddressKey =
      new ConcernRoleIDStatusCodeKey();
    crAddressKey.concernRoleID = registrationIDDetails.concernRoleID;
    crAddressKey.statusCode = RECORDSTATUS.NORMAL;
    final AddressReadMultiDtlsList searchAddressesByConcernRoleIDAndStatus =
      crAddressObj.searchAddressesByConcernRoleIDAndStatus(crAddressKey);

    // assert that only 2 concern role addresses exist
    assertEquals(2, searchAddressesByConcernRoleIDAndStatus.dtls.size());

    // both private and mailing addresses are the same so just get the first one
    final AddressKey addressKey = new AddressKey();
    addressKey.addressID =
      searchAddressesByConcernRoleIDAndStatus.dtls.get(0).addressID;
    final AddressElementDtlsList addressElementDtlsList =
      addressElementObj.readAddressElementDetails(addressKey);

    // expected element values
    final Map<String, String> expectedElements = new HashedMap<>();
    expectedElements.put(ADDRESSELEMENTTYPE.APT,
      addressFieldDetails.suiteNum);
    expectedElements.put(ADDRESSELEMENTTYPE.LINE1,
      addressFieldDetails.addressLine1);
    expectedElements.put(ADDRESSELEMENTTYPE.LINE2,
      addressFieldDetails.addressLine2);
    expectedElements.put(ADDRESSELEMENTTYPE.CITY, addressFieldDetails.city);
    expectedElements.put(ADDRESSELEMENTTYPE.COUNTRY,
      addressFieldDetails.countryCode);
    expectedElements.put(ADDRESSELEMENTTYPE.PROVINCE,
      addressFieldDetails.province);
    expectedElements.put(ADDRESSELEMENTTYPE.POSTCODE,
      addressFieldDetails.postalCode);
    expectedElements.put(ADDRESSELEMENTTYPE.POBOXNO, "");
    expectedElements.put(ADDRESSELEMENTTYPE.STATEPROV, "");
    expectedElements.put(ADDRESSELEMENTTYPE.ZIP, "");
    expectedElements.put(ADDRESSELEMENTTYPE.BDMSTPROV_X, "");
    expectedElements.put(ADDRESSELEMENTTYPE.BDMZIP_X, "");

    // iterate through elements and assert that the element belongs in the
    // address and its value is correctly mapped
    for (final AddressElementDtls addressElementDtls : addressElementDtlsList.dtls) {
      if (!expectedElements.containsKey(addressElementDtls.elementType)) {
        fail("Address contains unexpected element: "
          + addressElementDtls.elementType);
      }
      final String expectedValue =
        expectedElements.get(addressElementDtls.elementType);
      assertEquals(expectedValue, addressElementDtls.elementValue);
    }

  }

  /**
   * Tests that an American address is correctly added and each address element
   * has the appropriate value
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testAmericanAddress()
    throws AppException, InformationalException {

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    // modify person address details
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;
    addressFieldDetails.suiteNum = "Unit 234";
    addressFieldDetails.addressLine1 = "200";
    addressFieldDetails.addressLine2 = "Hollywood Road";
    addressFieldDetails.stateProvince = "LA";
    addressFieldDetails.city = "Hollywood";
    addressFieldDetails.zipCode = "90210";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.US;

    OtherAddressData otherAddressData = null;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    personRegistrationDetails.addressData = otherAddressData.addressData;
    personRegistrationDetails.mailingAddressData =
      otherAddressData.addressData;

    // register the person
    final RegistrationIDDetails registrationIDDetails =
      personRegistrationObj.registerPerson(personRegistrationDetails);

    // find the concern role address record corresponding to the newly
    // registered person
    final ConcernRoleIDStatusCodeKey crAddressKey =
      new ConcernRoleIDStatusCodeKey();
    crAddressKey.concernRoleID = registrationIDDetails.concernRoleID;
    crAddressKey.statusCode = RECORDSTATUS.NORMAL;
    final AddressReadMultiDtlsList searchAddressesByConcernRoleIDAndStatus =
      crAddressObj.searchAddressesByConcernRoleIDAndStatus(crAddressKey);

    // assert that only 2 concern role addresses exist
    assertEquals(2, searchAddressesByConcernRoleIDAndStatus.dtls.size());

    // both private and mailing addresses are the same so just get the first one
    final AddressKey addressKey = new AddressKey();
    addressKey.addressID =
      searchAddressesByConcernRoleIDAndStatus.dtls.get(0).addressID;
    final AddressElementDtlsList addressElementDtlsList =
      addressElementObj.readAddressElementDetails(addressKey);

    // expected element values
    final Map<String, String> expectedElements = new HashedMap<>();
    expectedElements.put(ADDRESSELEMENTTYPE.APT,
      addressFieldDetails.suiteNum);
    expectedElements.put(ADDRESSELEMENTTYPE.LINE1,
      addressFieldDetails.addressLine1);
    expectedElements.put(ADDRESSELEMENTTYPE.LINE2,
      addressFieldDetails.addressLine2);
    expectedElements.put(ADDRESSELEMENTTYPE.CITY, addressFieldDetails.city);
    expectedElements.put(ADDRESSELEMENTTYPE.COUNTRY,
      addressFieldDetails.countryCode);
    expectedElements.put(ADDRESSELEMENTTYPE.PROVINCE, "");
    expectedElements.put(ADDRESSELEMENTTYPE.POSTCODE, "");
    expectedElements.put(ADDRESSELEMENTTYPE.POBOXNO, "");
    expectedElements.put(ADDRESSELEMENTTYPE.STATEPROV,
      addressFieldDetails.stateProvince);
    expectedElements.put(ADDRESSELEMENTTYPE.ZIP, addressFieldDetails.zipCode);
    expectedElements.put(ADDRESSELEMENTTYPE.BDMSTPROV_X, "");
    expectedElements.put(ADDRESSELEMENTTYPE.BDMZIP_X, "");

    // iterate through elements and assert that the element belongs in the
    // address and its value is correctly mapped
    for (final AddressElementDtls addressElementDtls : addressElementDtlsList.dtls) {
      if (!expectedElements.containsKey(addressElementDtls.elementType)) {
        fail("Address contains unexpected element: "
          + addressElementDtls.elementType);
      }
      final String expectedValue =
        expectedElements.get(addressElementDtls.elementType);
      assertEquals(expectedValue, addressElementDtls.elementValue);
    }

  }

  /**
   * Tests that an American address is correctly added and each address element
   * has the appropriate value
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testInternationalAddress()
    throws AppException, InformationalException {

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    // modify person address details
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;
    addressFieldDetails.suiteNum = "Unit 345";
    addressFieldDetails.addressLine1 = "560";
    addressFieldDetails.addressLine2 = "Smith Road";
    addressFieldDetails.stateProvince = "Angus";
    addressFieldDetails.city = "Ark Hill";
    addressFieldDetails.zipCode = "DD10";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.GB;

    OtherAddressData otherAddressData = null;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    personRegistrationDetails.addressData = otherAddressData.addressData;
    personRegistrationDetails.mailingAddressData =
      otherAddressData.addressData;

    // register the person
    final RegistrationIDDetails registrationIDDetails =
      personRegistrationObj.registerPerson(personRegistrationDetails);

    // find the concern role address record corresponding to the newly
    // registered person
    final ConcernRoleIDStatusCodeKey crAddressKey =
      new ConcernRoleIDStatusCodeKey();
    crAddressKey.concernRoleID = registrationIDDetails.concernRoleID;
    crAddressKey.statusCode = RECORDSTATUS.NORMAL;
    final AddressReadMultiDtlsList searchAddressesByConcernRoleIDAndStatus =
      crAddressObj.searchAddressesByConcernRoleIDAndStatus(crAddressKey);

    // assert that only 2 concern role addresses exist
    assertEquals(2, searchAddressesByConcernRoleIDAndStatus.dtls.size());

    // both private and mailing addresses are the same so just get the first one
    final AddressKey addressKey = new AddressKey();
    addressKey.addressID =
      searchAddressesByConcernRoleIDAndStatus.dtls.get(0).addressID;
    final AddressElementDtlsList addressElementDtlsList =
      addressElementObj.readAddressElementDetails(addressKey);

    // expected element values
    final Map<String, String> expectedElements = new HashedMap<>();
    expectedElements.put(ADDRESSELEMENTTYPE.APT,
      addressFieldDetails.suiteNum);
    expectedElements.put(ADDRESSELEMENTTYPE.LINE1,
      addressFieldDetails.addressLine1);
    expectedElements.put(ADDRESSELEMENTTYPE.LINE2,
      addressFieldDetails.addressLine2);
    expectedElements.put(ADDRESSELEMENTTYPE.CITY, addressFieldDetails.city);
    expectedElements.put(ADDRESSELEMENTTYPE.COUNTRY,
      addressFieldDetails.countryCode);
    expectedElements.put(ADDRESSELEMENTTYPE.PROVINCE, "");
    expectedElements.put(ADDRESSELEMENTTYPE.POSTCODE, "");
    expectedElements.put(ADDRESSELEMENTTYPE.POBOXNO, "");
    expectedElements.put(ADDRESSELEMENTTYPE.BDMSTPROV_X,
      addressFieldDetails.stateProvince);
    expectedElements.put(ADDRESSELEMENTTYPE.BDMZIP_X,
      addressFieldDetails.zipCode);
    expectedElements.put(ADDRESSELEMENTTYPE.STATEPROV, "");
    expectedElements.put(ADDRESSELEMENTTYPE.ZIP, "");

    // iterate through elements and assert that the element belongs in the
    // address and its value is correctly mapped
    for (final AddressElementDtls addressElementDtls : addressElementDtlsList.dtls) {
      if (!expectedElements.containsKey(addressElementDtls.elementType)) {
        fail("Address contains unexpected element: "
          + addressElementDtls.elementType);
      }
      final String expectedValue =
        expectedElements.get(addressElementDtls.elementType);
      assertEquals(expectedValue, addressElementDtls.elementValue);
    }

  }

  /**
   * Tests that a Canadian address is correctly added and test city must be
   * entered
   * has the appropriate value
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testCanadianAddress_CityMustBeEntered()
    throws AppException, InformationalException {

    final curam.util.message.CatEntry expectedException =
      BPOADDRESS.ERR_CITY_UNAVAILABLE;

    ValidateAddressResult validateAddressResult;

    final AddressDtls addressDtls = new AddressDtls();
    final Address addressObj = AddressFactory.newInstance();
    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    // modify person address details
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;
    addressFieldDetails.suiteNum = "Unit 123";
    addressFieldDetails.addressLine1 = "180";
    addressFieldDetails.addressLine2 = "Richmond Road";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "";
    addressFieldDetails.postalCode = "L5A 1V9";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;
    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;

    // validate fails because city is not entered
    try {
      validateAddressResult = addressObj.validate(addressDtls);
    } catch (final AppException ae) {
      assertEquals(expectedException, ae.getCatEntry());
    }

    // Updating city to the address.
    addressFieldDetails.city = "Winnipeg";

    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;
    validateAddressResult = addressObj.validate(addressDtls);

    assertEquals(addressFieldDetails.city,
      validateAddressResult.addressMapList.dtls.item(3).value);

  }

  /**
   * Tests POSTALCODE must be added for CanADIAN ADDRESS
   * entered
   * has the appropriate value
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testCanadianAddress_PostalcodeMustBeEntered()
    throws AppException, InformationalException {

    final curam.util.message.CatEntry expectedException =
      BDMBPOADDRESS.ERR_POSTAL_CODE_MUST_BE_ENTERED_CANADIANADDRESS;
    final curam.util.message.CatEntry postalCodeFormatException =
      BDMBPOADDRESS.ERR_POSTAL_CODE_FORMAT_FOR_CANADIANADDRESS;

    ValidateAddressResult validateAddressResult;

    final AddressDtls addressDtls = new AddressDtls();
    final Address addressObj = AddressFactory.newInstance();
    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    // modify person address details
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;
    addressFieldDetails.suiteNum = "007";
    addressFieldDetails.addressLine1 = "180";
    addressFieldDetails.addressLine2 = "Richmond Road";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Barry";
    addressFieldDetails.postalCode = "";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;
    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;

    // validate fails because postalCode is not entered
    try {
      validateAddressResult = addressObj.validate(addressDtls);
    } catch (final AppException ae) {
      assertEquals(expectedException, ae.getCatEntry());
    }

    // Updating postal to invalid format to the address.
    addressFieldDetails.postalCode = "e4e 6tt";

    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;

    try {
      validateAddressResult = addressObj.validate(addressDtls);
    } catch (final AppException ae) {
      assertEquals(postalCodeFormatException, ae.getCatEntry());
    }

  }

  /**
   * Tests that a Canadian address is correctly added and test province must be
   * entered
   * has the appropriate value
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testCanadianAddress_ProvinceMustBeEntered()
    throws AppException, InformationalException {

    final curam.util.message.CatEntry expectedException =
      BDMBPOADDRESS.ERR_ADDRESS_PROVINCE_NOT_SUPPLIED;

    ValidateAddressResult validateAddressResult;

    final AddressDtls addressDtls = new AddressDtls();
    final Address addressObj = AddressFactory.newInstance();
    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    // modify person address details
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;
    addressFieldDetails.suiteNum = "Unit 123";
    addressFieldDetails.addressLine1 = "180";
    addressFieldDetails.addressLine2 = "Richmond Road";
    addressFieldDetails.province = "";
    addressFieldDetails.city = "Regina";
    addressFieldDetails.postalCode = "L5A 1V9";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;
    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;

    // validate fails because province is not entered
    try {
      validateAddressResult = addressObj.validate(addressDtls);
    } catch (final AppException ae) {
      assertEquals(expectedException, ae.getCatEntry());
    }

    // Updating province to the address.
    addressFieldDetails.province = PROVINCETYPE.ALBERTA;

    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;
    validateAddressResult = addressObj.validate(addressDtls);

    assertEquals(addressFieldDetails.province,
      validateAddressResult.addressMapList.dtls.item(4).value);

  }

  /**
   * Tests that a Canadian address is correctly added and test province must be
   * entered
   * has the appropriate value
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testCanadianAddress_STRNAMEandNUMMustBeEntered()
    throws AppException, InformationalException {

    final curam.util.message.CatEntry expectedException =
      BPOADDRESS.ERR_ADDRESS_FV_ADDRESS_LINE_LENGTH;

    ValidateAddressResult validateAddressResult;

    final AddressDtls addressDtls = new AddressDtls();
    final Address addressObj = AddressFactory.newInstance();
    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    // modify person address details
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;

    addressFieldDetails.suiteNum = "Unit 123";
    addressFieldDetails.addressLine1 = "";
    addressFieldDetails.addressLine2 = "";
    addressFieldDetails.province = PROVINCETYPE.BRITISHCOLUMBIA;
    addressFieldDetails.city = "Regina";
    addressFieldDetails.postalCode = "L5A 1V9";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;
    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;

    // validate fails because street number and street name is not entered
    try {
      validateAddressResult = addressObj.validate(addressDtls);
    } catch (final AppException ae) {
      assertEquals(expectedException, ae.getCatEntry());
    }

    // Updating street number only
    addressFieldDetails.addressLine1 = "Unit-25";

    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;
    try {
      validateAddressResult = addressObj.validate(addressDtls);
    } catch (final AppException ae) {
      assertEquals(expectedException, ae.getCatEntry());
    }
    // Updating street name but blank street number
    addressFieldDetails.addressLine1 = "";
    addressFieldDetails.addressLine2 = "RAe Street";

    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;
    try {
      validateAddressResult = addressObj.validate(addressDtls);
    } catch (final AppException ae) {
      assertEquals(expectedException, ae.getCatEntry());
    }

    // Updating street name but blank street number
    addressFieldDetails.addressLine1 = "unit-45";
    addressFieldDetails.addressLine2 = "RAe Street";

    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;

    validateAddressResult = addressObj.validate(addressDtls);

    // TODO; to check wher is addressline1 being stored

    // assertEquals(addressFieldDetails.addressLine1,
    // validateAddressResult.addressMapList.dtls.item(2).value);
    assertEquals(addressFieldDetails.addressLine2,
      validateAddressResult.addressMapList.dtls.item(2).value);

  }

  /**
   * Tests that a US address is correctly added and test SATTE must be
   * entered
   * has the appropriate value
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testUSAddress_STATEMustBeEntered()
    throws AppException, InformationalException {

    final curam.util.message.CatEntry expectedException =
      BDMBPOADDRESS.ERR_ADDRESS_STATE_MUST_BE_ENTERED_FOR_FOREIGNADDRESS;

    ValidateAddressResult validateAddressResult;

    final AddressDtls addressDtls = new AddressDtls();
    final Address addressObj = AddressFactory.newInstance();
    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    // modify person address details
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;

    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;
    addressFieldDetails.suiteNum = "Unit 234";
    addressFieldDetails.addressLine1 = "200";
    addressFieldDetails.addressLine2 = "Hollywood Road";
    addressFieldDetails.stateProvince = "";
    addressFieldDetails.city = "Hollywood";
    addressFieldDetails.zipCode = "90210";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.US;

    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;

    // validate fails STATE is not entered
    try {
      validateAddressResult = addressObj.validate(addressDtls);
    } catch (final AppException ae) {
      assertEquals(expectedException, ae.getCatEntry());
    }

    // Updating STATE only for US Address
    addressFieldDetails.stateProvince = "LA";

    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;

    validateAddressResult = addressObj.validate(addressDtls);

    assertEquals(addressFieldDetails.stateProvince,
      validateAddressResult.addressMapList.dtls.item(10).value);

  }

  @Test
  public void testCountryOtherThanUS_STATEMustBeEntered()
    throws AppException, InformationalException {

    final curam.util.message.CatEntry expectedException =
      BDMBPOADDRESS.ERR_ADDRESS_STATE_MUST_BE_ENTERED_FOR_FOREIGNADDRESS;

    ValidateAddressResult validateAddressResult;

    final AddressDtls addressDtls = new AddressDtls();
    final Address addressObj = AddressFactory.newInstance();
    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    // modify person address details
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;

    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;
    addressFieldDetails.suiteNum = "Apt 12";
    addressFieldDetails.addressLine1 = "Parliamnet place 234";
    addressFieldDetails.addressLine2 = "Little Crosby";
    addressFieldDetails.stateProvince = "";

    addressFieldDetails.city = "Liverpool";
    addressFieldDetails.zipCode = "L23";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.GB;

    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;

    // validate fails STATE is not entered
    try {
      validateAddressResult = addressObj.validate(addressDtls);
    } catch (final AppException ae) {
      assertEquals(expectedException, ae.getCatEntry());
    }

    // Updating STATE only for GB Address
    addressFieldDetails.stateProvince = "LA";

    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;

    validateAddressResult = addressObj.validate(addressDtls);

    assertEquals(addressFieldDetails.stateProvince,
      validateAddressResult.addressMapList.dtls.item(11).value);

  }

  @Test
  public void testUSAddress_ZIPMustBeEntered()
    throws AppException, InformationalException {

    final curam.util.message.CatEntry expectedException =
      BDMBPOADDRESS.ERR_ZIP_CODE_MUST_BE_ENTERED_USADDRESS;

    ValidateAddressResult validateAddressResult;

    final AddressDtls addressDtls = new AddressDtls();
    final Address addressObj = AddressFactory.newInstance();
    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    // modify person address details
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;

    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;
    addressFieldDetails.suiteNum = "Unit 234";
    addressFieldDetails.addressLine1 = "200";
    addressFieldDetails.addressLine2 = "Hollywood Road";
    addressFieldDetails.stateProvince = "Oregon";
    addressFieldDetails.city = "Hollywood";
    addressFieldDetails.zipCode = "";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.US;

    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;

    // validate fails STATE is not entered
    try {
      validateAddressResult = addressObj.validate(addressDtls);
    } catch (final AppException ae) {
      assertEquals(expectedException, ae.getCatEntry());
    }

    // Updating STATE only
    addressFieldDetails.zipCode = "12345";

    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;

    validateAddressResult = addressObj.validate(addressDtls);

    assertEquals(addressFieldDetails.zipCode,
      validateAddressResult.addressMapList.dtls.item(2).value);

  }

}
