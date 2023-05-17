package curam.ca.gc.bdm.test.address.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.address.impl.BDMAddressFormatINTL;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.ADDRESSLAYOUTTYPE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.COUNTRY;
import curam.codetable.PROVINCETYPE;
import curam.core.fact.AddressDataFactory;
import curam.core.fact.AddressElementFactory;
import curam.core.fact.AddressFactory;
import curam.core.fact.ConcernRoleAddressFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.intf.Address;
import curam.core.intf.AddressData;
import curam.core.intf.AddressElement;
import curam.core.intf.ConcernRoleAddress;
import curam.core.intf.UniqueID;
import curam.core.struct.AddressDtls;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.ConcernRoleAddressDtls;
import curam.core.struct.OtherAddressData;
import curam.core.struct.PersonRegistrationDetails;
import curam.core.struct.ValidateAddressResult;
import curam.dynamicevidence.definition.impl.EvidenceTypeDefDAO;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDefDAO;
import curam.participant.impl.ConcernRoleDAO;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.Date;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

// @Generated(value = "org.junit-tools-1.1.0")
public class BDMAddressFormatINTLTest extends CuramServerTestJUnit4 {

  public BDMAddressFormatINTLTest() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Inject
  protected ConcernRoleDAO concernRoleDAO;

  @Inject
  protected EvidenceTypeVersionDefDAO etVerDefDAO;

  @Inject
  private EvidenceTypeDefDAO etDefDAO;

  final private String ERR_FOREIGN_STATE_MUST_NOT_BE_BLANK =
    "State must be entered for a Foreign address";

  final private String ERR_CANADA_CITY_MUST_NOT_BE_BLANK =
    "Please enter the city information.";

  final private String ERR_CANADA_PROVINCE_MUST_NOT_BE_BLANK =
    "Province or Territory must be selected for Canadian address";

  final private String ERR_CANADA_POSTALCODE_INCORRECT_FORMAT =
    "Postal Code must be entered";

  final private String ERR_CANADA_POSTALCODE_INCORRECT_FORMAT1 =
    "Postal Code must be in the right format for a Canadian address.";

  final private String ERR_RESIDENTIALADDRESS_POBOX_SHALL_NOT_BE_ENTERED = "";

  final private String ERR_STRNUM_STRNAME_MUST_BE_COMPLETED_FOR_CANADIANADDRESS =
    "Street Number and Street name must be completed for Canadian addresses.";

  final private String ERR_ZIP_CODE_MUST_BE_ENTERED_USADDRESS =
    "Zip Code must be entered";

  PersonRegistrationDetails personRegistrationDetails;

  curam.core.intf.PersonRegistration personRegistrationObj;

  ConcernRoleAddress crAddressObj;

  BDMEvidenceUtilsTest bdmEvidenceUtils = new BDMEvidenceUtilsTest();

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

  @Test
  public void testValidateAddress_stateFieldCannotBeBlank() throws Exception {

    ValidateAddressResult validateAddressResult = new ValidateAddressResult();
    final AddressDtls addressDtls = new AddressDtls();
    final Address addressObj = AddressFactory.newInstance();
    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();
    final BDMAddressFormatINTL addFormat = new BDMAddressFormatINTL();

    // modify person address details
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;
    addressFieldDetails.suiteNum = "Unit 345";
    addressFieldDetails.addressLine1 = "560";
    addressFieldDetails.addressLine2 = "Smith Road";
    addressFieldDetails.stateProvince = "";
    addressFieldDetails.city = "Ark Hill";
    addressFieldDetails.zipCode = "DD10";

    addressFieldDetails.countryCode = curam.codetable.COUNTRY.GB;
    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;
    OtherAddressData otherAddressData = null;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    personRegistrationDetails.addressData = otherAddressData.addressData;
    personRegistrationDetails.mailingAddressData =
      otherAddressData.addressData;

    try {
      validateAddressResult = addressObj.validate(addressDtls);
    } catch (final Exception e) {
      assertEquals(ERR_FOREIGN_STATE_MUST_NOT_BE_BLANK, e.getMessage());
    }
    // adding the state
    addressFieldDetails.stateProvince = "Angus";
    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;
    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);
    personRegistrationDetails.addressType = CONCERNROLEADDRESSTYPE.PRIVATE;
    personRegistrationDetails.addressData = otherAddressData.addressData;
    personRegistrationDetails.mailingAddressData =
      otherAddressData.addressData;

    // register the person
    personRegistrationObj.registerPerson(personRegistrationDetails);

  }

  @Test
  public void testValidateAddress_ResidentialAddressNotAllowedPOBoxes()
    throws AppException, InformationalException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();
    ValidateAddressResult validateAddressResult;
    final BDMAddressFormatINTL addressFormat = new BDMAddressFormatINTL();

    // create an address
    final AddressDtls addressDtls = new AddressDtls();
    addressDtls.addressID = UniqueIDFactory.newInstance().getNextID();
    final Address addressObj = AddressFactory.newInstance();
    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    final AddressData addressDataObj = AddressDataFactory.newInstance();

    // Create address with no city entered.
    addressFieldDetails.addressLayoutType = ADDRESSLAYOUTTYPE.BDMINTL;
    addressFieldDetails.suiteNum = "15";
    addressFieldDetails.addressLine1 = "PO ";
    addressFieldDetails.addressLine2 = "Cliffs avenue";
    addressFieldDetails.addressLine3 = "PO 123";
    addressFieldDetails.city = "City";
    addressFieldDetails.province = PROVINCETYPE.ALBERTA;
    addressFieldDetails.postalCode = "T6T 8U8";
    addressFieldDetails.countryCode = COUNTRY.CA;
    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;

    addressObj.insert(addressDtls);

    final OtherAddressData otherAddressData =
      addressDataObj.parseFieldsToData(addressFieldDetails);
    final UniqueID uniqueIDObj = UniqueIDFactory.newInstance();
    // based on domain CONCERN_ROLE_ADDRESS_ID
    long concernRoleAddressID = 0;
    // generate unique key for new concernRoleAddress record
    concernRoleAddressID = uniqueIDObj.getNextID();
    final ConcernRoleAddress concernRoleAddressObj =
      ConcernRoleAddressFactory.newInstance();
    final ConcernRoleAddressDtls concernRoleAddressDtls =
      new ConcernRoleAddressDtls();
    concernRoleAddressDtls.assign(addressDtls);
    concernRoleAddressDtls.statusCode = curam.codetable.RECORDSTATUS.NORMAL;
    concernRoleAddressDtls.concernRoleAddressID = concernRoleAddressID;
    concernRoleAddressDtls.addressID = addressDtls.addressID;
    concernRoleAddressDtls.typeCode = CONCERNROLEADDRESSTYPE.PRIVATE;
    concernRoleAddressDtls.concernRoleAddressID = concernRoleAddressID;
    concernRoleAddressDtls.concernRoleID = pdcPersonDetails.concernRoleID;

    validateAddressResult = addressObj.validate(addressDtls);
    try {
      concernRoleAddressObj.pdcInsert(concernRoleAddressDtls);

    } catch (final Exception ae) {
      assertEquals(ERR_RESIDENTIALADDRESS_POBOX_SHALL_NOT_BE_ENTERED,
        ae.getMessage());
    }
    // register the person

  }

  @Test
  public void testValidateAddress_canadaCityCannotBeBlank()
    throws AppException, InformationalException {

    ValidateAddressResult validateAddressResult;
    final BDMAddressFormatINTL addressFormat = new BDMAddressFormatINTL();

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
    // empty city
    addressFieldDetails.city = "";
    addressFieldDetails.postalCode = "J8T 2N4";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;
    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;

    OtherAddressData otherAddressData =
      addressDataObj.parseFieldsToData(addressFieldDetails);

    // validate fails because city is not entered
    try {
      validateAddressResult = addressObj.validate(addressDtls);
    } catch (final Exception e) {
      assertEquals(ERR_CANADA_CITY_MUST_NOT_BE_BLANK, e.getMessage());
    }
    // updating city
    addressFieldDetails.city = "Barry";
    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    try {
      validateAddressResult = addressObj.validate(addressDtls);

    } catch (final Exception e) {
      assertEquals(ERR_CANADA_CITY_MUST_NOT_BE_BLANK, e.getMessage());
    }
  }

  @Test
  public void testValidateAddress_canadaProvinceCannotBeBlank()
    throws AppException, InformationalException {

    ValidateAddressResult validateAddressResult;
    final BDMAddressFormatINTL addressFormat = new BDMAddressFormatINTL();

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
    // empty province
    addressFieldDetails.province = "";
    addressFieldDetails.city = "Barry";
    addressFieldDetails.postalCode = "J8T 2N4";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;
    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;

    OtherAddressData otherAddressData =
      addressDataObj.parseFieldsToData(addressFieldDetails);

    // validate fails because province is not entered
    try {
      validateAddressResult = addressObj.validate(addressDtls);

    } catch (final Exception e) {
      assertEquals(ERR_CANADA_PROVINCE_MUST_NOT_BE_BLANK, e.getMessage());
    }
    // updating province
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    try {
      validateAddressResult = addressObj.validate(addressDtls);

    } catch (final Exception e) {
      assertEquals(ERR_CANADA_PROVINCE_MUST_NOT_BE_BLANK, e.getMessage());
    }
  }

  @Test
  public void testValidateAddress_canadaPostalCodeCannotBeBlank()
    throws AppException, InformationalException {

    ValidateAddressResult validateAddressResult;
    final BDMAddressFormatINTL addressFormat = new BDMAddressFormatINTL();

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
    // empty postal code
    addressFieldDetails.postalCode = "";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;
    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;

    OtherAddressData otherAddressData =
      addressDataObj.parseFieldsToData(addressFieldDetails);

    // validate fails because postal code is not entered
    try {
      validateAddressResult = addressObj.validate(addressDtls);

    } catch (final Exception e) {
      assertEquals(ERR_CANADA_POSTALCODE_INCORRECT_FORMAT, e.getMessage());
    }
    // updating postal code with a badly formatted one
    addressFieldDetails.postalCode = "e4e444";
    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    // validate fails because the format is incorrect
    try {
      validateAddressResult = addressObj.validate(addressDtls);

    } catch (final Exception e) {
      assertEquals(ERR_CANADA_POSTALCODE_INCORRECT_FORMAT1, e.getMessage());
    }
  }

  @Test
  public void testValidateAddress_canadaStreetCannotBeBlank()
    throws AppException, InformationalException {

    ValidateAddressResult validateAddressResult;
    final BDMAddressFormatINTL addressFormat = new BDMAddressFormatINTL();
    final AddressDtls addressDtls = new AddressDtls();
    final Address addressObj = AddressFactory.newInstance();
    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    // modify person address details
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;
    addressFieldDetails.suiteNum = "007";
    // empty Address
    addressFieldDetails.addressLine1 = "";
    addressFieldDetails.addressLine2 = "";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Barry";
    addressFieldDetails.postalCode = "K8T 2J3";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;
    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;

    OtherAddressData otherAddressData =
      addressDataObj.parseFieldsToData(addressFieldDetails);

    // validate fails because address is not entered
    try {
      validateAddressResult = addressObj.validate(addressDtls);

    } catch (final Exception e) {
      assertEquals(ERR_STRNUM_STRNAME_MUST_BE_COMPLETED_FOR_CANADIANADDRESS,
        e.getMessage());
    }
    // updating address line 1 but 2 is still empty
    addressFieldDetails.addressLine1 = "190";
    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    // validate fails because address line 2 isn't entered
    try {
      validateAddressResult = addressObj.validate(addressDtls);

    } catch (final Exception e) {
      assertEquals(ERR_STRNUM_STRNAME_MUST_BE_COMPLETED_FOR_CANADIANADDRESS,
        e.getMessage());
    }

    // updating address line 2 and setting 1 to empty
    addressFieldDetails.addressLine1 = "";
    addressFieldDetails.addressLine2 = "Eddy St";
    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    // validate fails because address line 1 isn't entered
    try {
      validateAddressResult = addressObj.validate(addressDtls);

    } catch (final Exception e) {
      assertEquals(ERR_STRNUM_STRNAME_MUST_BE_COMPLETED_FOR_CANADIANADDRESS,
        e.getMessage());
    }

    // updating address line 2 and 1
    addressFieldDetails.addressLine1 = "33";
    addressFieldDetails.addressLine2 = "Eddy St";
    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    // passes
    try {
      validateAddressResult = addressObj.validate(addressDtls);

    } catch (final Exception e) {
      assertEquals(ERR_STRNUM_STRNAME_MUST_BE_COMPLETED_FOR_CANADIANADDRESS,
        e.getMessage());
    }
  }

  @Test
  public void testValidateAddress_usaZIPCodeCannotBeBlank()
    throws AppException, InformationalException {

    ValidateAddressResult validateAddressResult;
    final BDMAddressFormatINTL addressFormat = new BDMAddressFormatINTL();

    final AddressDtls addressDtls = new AddressDtls();
    final Address addressObj = AddressFactory.newInstance();
    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    // modify person address details
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;
    addressFieldDetails.suiteNum = "Unit 234";
    addressFieldDetails.addressLine1 = "200";
    addressFieldDetails.addressLine2 = "Hollywood Road";
    addressFieldDetails.stateProvince = "Oregon";
    addressFieldDetails.city = "Hollywood";
    // empty zip
    addressFieldDetails.zipCode = "";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.US;
    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;

    OtherAddressData otherAddressData =
      addressDataObj.parseFieldsToData(addressFieldDetails);

    // validate fails because zip is not entered
    try {
      validateAddressResult = addressObj.validate(addressDtls);

    } catch (final Exception e) {
      assertEquals(ERR_ZIP_CODE_MUST_BE_ENTERED_USADDRESS, e.getMessage());
    }
    // updating zip with a badly formatted one
    addressFieldDetails.zipCode = "12345";
    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    try {
      validateAddressResult = addressObj.validate(addressDtls);
    } catch (final Exception e) {
      assertEquals(ERR_ZIP_CODE_MUST_BE_ENTERED_USADDRESS, e.getMessage());
    }
  }

  @Test
  public void testAddressOverLap_samedates()
    throws AppException, InformationalException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final String ERR_ADDRESSTYPE_WITH_OVERLAPPING_TIME_NOT_ALLOWED =
      "An address of the same type and overlapping time period is not allowed.";

    final Date fromDate = Date.fromISO8601("20010101");
    final Date toDate = Date.fromISO8601("20010101");

    // create new address evidence with same data toDate // fromDate , should
    // throw overlapping error message
    try {
      bdmEvidenceUtils.createAddressEvidence1(pdcPersonDetails,
        CONCERNROLEADDRESSTYPE.PRIVATE, fromDate, toDate);
    } catch (final Exception e) {
      assertEquals(ERR_ADDRESSTYPE_WITH_OVERLAPPING_TIME_NOT_ALLOWED,
        e.getMessage());
    }
  }

}
