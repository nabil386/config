package curam.ca.gc.bdm.test.rest.bdmcitizenaccountapi.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.codetable.impl.BDMIEGYESNOOPTIONALEntry;
import curam.ca.gc.bdm.entity.person.fact.BDMPersonFactory;
import curam.ca.gc.bdm.entity.person.intf.BDMPerson;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonDtls;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonKey;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.lifeevent.impl.BDMContactPreferenceEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMEmailEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMNamesEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMPhoneEvidence;
import curam.ca.gc.bdm.rest.bdmcitizenaccountapi.impl.BDMCitizenAccountAPI;
import curam.ca.gc.bdm.rest.bdmcitizenaccountapi.struct.BDMUAAddress;
import curam.ca.gc.bdm.rest.bdmcitizenaccountapi.struct.BDMUAAddressDetails;
import curam.ca.gc.bdm.rest.bdmcitizenaccountapi.struct.BDMUAAlert;
import curam.ca.gc.bdm.rest.bdmcitizenaccountapi.struct.BDMUAEmailAddress;
import curam.ca.gc.bdm.rest.bdmcitizenaccountapi.struct.BDMUAIdentification;
import curam.ca.gc.bdm.rest.bdmcitizenaccountapi.struct.BDMUAPhoneNumber;
import curam.ca.gc.bdm.rest.bdmcitizenaccountapi.struct.BDMUAProfile;
import curam.ca.gc.bdm.rest.bdmcitizenaccountapi.struct.BDMUAProfileContactDetails;
import curam.ca.gc.bdm.rest.bdmcitizenaccountapi.struct.BDMUAProfilePayment;
import curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest;
import curam.citizenaccount.configuration.impl.ContactInformationConfig;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.EMAILTYPE;
import curam.codetable.METHODOFDELIVERY;
import curam.codetable.PROVINCETYPE;
import curam.codetable.impl.GENDEREntry;
import curam.codetable.impl.INTAKEMARITALSTATUSEntry;
import curam.codetable.impl.METHODOFDELIVERYEntry;
import curam.codetable.impl.PHONETIMEEntry;
import curam.core.facade.fact.PersonFactory;
import curam.core.facade.struct.ReadPersonDetails;
import curam.core.facade.struct.ReadPersonKey;
import curam.core.fact.ConcernRoleImageFactory;
import curam.core.impl.EnvVars;
import curam.core.intf.ConcernRoleImage;
import curam.core.intf.Person;
import curam.core.struct.AddressDtls;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.AlternateIDReadmultiDtls;
import curam.core.struct.AlternateIDReadmultiDtlsList;
import curam.core.struct.ConcernRoleAlternateIDRMKey;
import curam.core.struct.ConcernRoleImageDtlsList;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.EmailAddressDtls;
import curam.core.struct.OtherAddressData;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonKey;
import curam.core.struct.PhoneNumberDtls;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.pdc.impl.PDCConst;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.Trace;
import curam.util.type.NotFoundIndicator;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BDMCitizenAccountAPIMockTest extends BDMEvidenceUtilsTest {

  public static final String PHOTO_URL = "/ua/profile_image/";

  @Inject
  private ContactInformationConfig contactInformationConfig;

  @Inject
  BDMContactPreferenceEvidence bdmContactPreferenceEvidence;

  @Inject
  BDMEmailEvidence bdmEmailEvidence;

  @Inject
  BDMPhoneEvidence bdmPhoneEvidence;

  @Inject
  BDMNamesEvidence bdmNamesEvidence;

  /** The BDMCitizenAccount API. */
  BDMCitizenAccountAPI bdmCitizenAccountAPI = new BDMCitizenAccountAPI();

  PDCPersonDetails pdcPersonDetails;

  /**
   * Before.
   */
  @Before
  public void before() {

    super.setUpTransaction();

    pdcPersonDetails = new PDCPersonDetails();

  }

  @Override
  public void setUpCuramServerTest() {

    super.setUpCuramServerTest();
  }

  @Test
  public void testReadProfile() throws InformationalException, AppException,
    InstantiationException, IllegalAccessException {

    final BDMUAProfile bdmUAProfile;

    // createPDCPerson();
    pdcPersonDetails = this.createPDCPerson();

    // updatePDCPerson()
    updatePDCPerson();

    bdmUAProfile = readProfile();

    assertEquals("Test", bdmUAProfile.fullName);
    assertEquals("Prefer not to answer", bdmUAProfile.indigenous_person_ind);
    assertEquals("BDMINTL", bdmUAProfile.registeredPersonDetails.addresses
      .get(0).addressDetails.addressLayoutType);
    assertEquals("Yes",
      bdmUAProfile.registeredPersonDetails.alerts.get(0).receive_alert);
    assertEquals("mail@gmail.com",
      bdmUAProfile.registeredPersonDetails.emailAddresses.get(0).email);
    assertEquals("555",
      bdmUAProfile.registeredPersonDetails.phoneNumbers.get(0).areaCode);
    assertEquals(METHODOFDELIVERYEntry.CHEQUE.toUserLocaleString(),
      bdmUAProfile.registeredPersonDetails.paymentDetails
        .get(0).payment_method);

  }

  private void updatePDCPerson() throws InformationalException, AppException,
    InstantiationException, IllegalAccessException {

    final java.util.Calendar cal = new java.util.GregorianCalendar(1970,
      java.util.Calendar.APRIL, 1, 0, 0, 0);
    final curam.util.type.Date birthday = new curam.util.type.Date(cal);

    pdcPersonDetails.dateOfBirth = birthday;
    pdcPersonDetails.registrationDate = curam.util.type.Date.getCurrentDate();
    pdcPersonDetails.birthName = "Test";
    pdcPersonDetails.motherBirthSurname = "Smith";
    pdcPersonDetails.preferredName = "Test";
    pdcPersonDetails.gender = "SX1";
    pdcPersonDetails.maritalStatusCode =
      curam.codetable.MARITALSTATUS.MARRIED;
    pdcPersonDetails.nationalityCode =
      curam.codetable.NATIONALITY.DEFAULTCODE;
    pdcPersonDetails.countryOfBirth = curam.codetable.COUNTRY.GB;

    pdcPersonDetails.addressType = CONCERNROLEADDRESSTYPE.PRIVATE.toString();

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

    pdcPersonDetails.addressData = otherAddressData.addressData;

    pdcPersonDetails.paymentFrequency = "100100100";
    pdcPersonDetails.methodOfPmtCode = METHODOFDELIVERY.CHEQUE;
    pdcPersonDetails.currencyType = curam.codetable.CURRENCY.DEFAULTCODE;

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

    pdcPersonDetails.mailingAddressData = mailingAddressData.addressData;
    pdcPersonDetails.prefCommMethod = "Digital copy only";
    pdcPersonDetails.preferredLanguage = "English";

  }

  public BDMUAProfile readProfile()
    throws AppException, InformationalException {

    final BDMUAProfile profile = new BDMUAProfile();

    final long concernRoleID = pdcPersonDetails.concernRoleID;
    final ReadPersonKey readPersonKey = new ReadPersonKey();
    readPersonKey.maintainConcernRoleKey.concernRoleID = concernRoleID;
    final ReadPersonDetails personDetails =
      PersonFactory.newInstance().readPerson(readPersonKey);
    profile.fullName = pdcPersonDetails.firstForename;
    profile.person_id = concernRoleID;
    profile.dateOfBirth = pdcPersonDetails.dateOfBirth;
    profile.last_name_at_birth = pdcPersonDetails.birthName;
    profile.parent_last_name = pdcPersonDetails.motherBirthSurname;
    profile.pref_name = pdcPersonDetails.preferredName;

    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();

    // Get gender and marital status from personDtls
    final Person personObj = curam.core.fact.PersonFactory.newInstance();
    final PersonKey personKey = new PersonKey();
    personKey.concernRoleID = pdcPersonDetails.concernRoleID; // concernRole.getID().longValue();
    final PersonDtls personDtls =
      personObj.read(notFoundIndicator, personKey);
    if (notFoundIndicator.isNotFound()) {
      Trace.kTopLevelLogger.warn(BDMConstants.BDM_LOGS_PREFIX
        + "Person could not be found in citizenAccountAPI");
    } else {
      profile.gender =
        GENDEREntry.get(personDtls.gender).toUserLocaleString();
      profile.marital_status_code = INTAKEMARITALSTATUSEntry
        .get(personDtls.maritalStatusCode).toUserLocaleString();
    }

    // Get Education, Minority, and Indigenous indicators from bdmPersonDtls
    final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();
    final BDMPersonKey bdmPersonKey = new BDMPersonKey();
    bdmPersonKey.concernRoleID = concernRoleID;
    final BDMPersonDtls bdmPersonDtls = new BDMPersonDtls();
    bdmPersonDtls.educationLevel = "University";
    // Task 26891 TDD 19885 2022-05-26
    // bdmPersonDtls.memberMinorityGrpType = "BDMIEGO0801";
    bdmPersonDtls.memberMinorityGrpType = "BDMIEG8001";
    bdmPersonDtls.indigenousPersonType = "BDMIEG8002";
    // bdmPersonObj.read(notFoundIndicator, bdmPersonKey);
    /*
     * if (notFoundIndicator.isNotFound()) {
     * Trace.kTopLevelLogger.warn(BDMConstants.BDM_LOGS_PREFIX
     * + "BDMPerson could not be found in citizenAccountAPI");
     * } else {
     */
    profile.education_level = bdmPersonDtls.educationLevel;
    profile.minority_person_ind = BDMIEGYESNOOPTIONALEntry
      .get(bdmPersonDtls.memberMinorityGrpType).toUserLocaleString();
    profile.indigenous_person_ind = BDMIEGYESNOOPTIONALEntry
      .get(bdmPersonDtls.indigenousPersonType).toUserLocaleString();
    // }

    // Get photo
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final ConcernRoleImage concernRoleImage =
      ConcernRoleImageFactory.newInstance();
    concernRoleKey.concernRoleID = pdcPersonDetails.concernRoleID;// concernRole.getID().longValue();
    final ConcernRoleImageDtlsList concernRoleImageDtlsList =
      concernRoleImage.searchLatestImageByConcernRoleID(concernRoleKey);
    if (!concernRoleImageDtlsList.dtls.isEmpty())
      profile.photo = "/ua/profile_image/"
        + concernRoleImageDtlsList.dtls.get(0).concernRoleImageID;

    // Add Identifications
    for (final BDMUAIdentification identification : getIdentification(
      concernRoleID)) {
      profile.registeredPersonDetails.identifications.addRef(identification);
    }

    // Add profilePayment to registeredPersonDetails
    if (personDetails.personFurtherDetails.methodOfPmtCode
      .equals(METHODOFDELIVERYEntry.CHEQUE.getCode())) {
      final BDMUAProfilePayment profilePayment = new BDMUAProfilePayment();
      profilePayment.payment_method =
        METHODOFDELIVERYEntry.CHEQUE.toUserLocaleString();
      profile.registeredPersonDetails.paymentDetails.addRef(profilePayment);
    } else if (personDetails.personFurtherDetails.methodOfPmtCode
      .equals(METHODOFDELIVERYEntry.EFT.getCode())) {
      profile.registeredPersonDetails.paymentDetails
        .addRef(getProfilePaymentDetails(concernRoleID));
    }

    // Add Alert
    // final List<BDMContactPreferenceEvidenceVO>
    // bdmContactPreferenceEvidenceVOList =
    // bdmContactPreferenceEvidence.getEvidenceValueObject(concernRoleID);
    // if (!bdmContactPreferenceEvidenceVOList.isEmpty()) {
    profile.registeredPersonDetails.alerts.addRef(getAlertDetails());
    // }

    // Add addresses, phones, and emails
    if (this.contactInformationConfig.showClientDetails().booleanValue()) {
      final BDMUAProfileContactDetails profileContactDetails =
        getProfileContactDetails(concernRoleID);
      profile.registeredPersonDetails.addresses
        .addAll(profileContactDetails.addresses);
      profile.registeredPersonDetails.phoneNumbers
        .addAll(profileContactDetails.phoneNumbers);
      profile.registeredPersonDetails.emailAddresses
        .addAll(profileContactDetails.emailAddresses);
    }
    return profile;
  }

  private ArrayList<BDMUAIdentification> getIdentification(
    final long concernRoleID) throws AppException, InformationalException {

    final ArrayList<BDMUAIdentification> identificationList =
      new ArrayList<>();
    // final ConcernRoleAlternateID concerntRoleAlternateIDObj =
    // ConcernRoleAlternateIDFactory.newInstance();

    final ConcernRoleAlternateIDRMKey key = new ConcernRoleAlternateIDRMKey();
    key.concernRoleID = concernRoleID;

    final AlternateIDReadmultiDtlsList alternateIDList =
      createIdentification();

    // concerntRoleAlternateIDObj.searchByConcernRole(key);
    if (null != alternateIDList.dtls)
      for (final AlternateIDReadmultiDtls alternateID : alternateIDList.dtls) {
        final BDMUAIdentification identification = new BDMUAIdentification();
        identification.identificationType = alternateID.typeCode;
        identification.identificationNumber =
          "*****" + alternateID.alternateID.substring(5);
        identificationList.add(identification);
      }
    return identificationList;
  }

  private AlternateIDReadmultiDtlsList createIdentification() {

    final AlternateIDReadmultiDtlsList alternateIDList =
      new AlternateIDReadmultiDtlsList();

    final AlternateIDReadmultiDtls alternateIDReadmultiDtls1 =
      new AlternateIDReadmultiDtls();
    alternateIDReadmultiDtls1.alternateID = "30000001";
    alternateIDReadmultiDtls1.statusCode = "RST1";
    alternateIDReadmultiDtls1.typeCode = "CA7";

    final AlternateIDReadmultiDtls alternateIDReadmultiDtls2 =
      new AlternateIDReadmultiDtls();
    alternateIDReadmultiDtls2.alternateID = "329217079";
    alternateIDReadmultiDtls2.statusCode = "RST1";
    alternateIDReadmultiDtls2.typeCode = "BDMCA80002";

    alternateIDList.dtls.add(alternateIDReadmultiDtls1);
    alternateIDList.dtls.add(alternateIDReadmultiDtls2);

    return alternateIDList;
  }

  private BDMUAProfilePayment getProfilePaymentDetails(
    final long concernRoleID) throws AppException, InformationalException {

    final List<DynamicEvidenceDataDetails> bankEvidenceDataDetailsList =
      new BDMEvidenceUtil().getEvdDtlsByConcernroleIDandEvidenceType(
        concernRoleID, PDCConst.PDCBANKACCOUNT);
    final BDMUAProfilePayment profilePayment = new BDMUAProfilePayment();
    final DynamicEvidenceDataDetails bankDetails =
      bankEvidenceDataDetailsList.get(0);
    profilePayment.account_nick_name =
      bankDetails.getAttribute(BDMDatastoreConstants.ACCOUNT_NAME).getValue();
    profilePayment.account_number = bankDetails
      .getAttribute(BDMDatastoreConstants.BANK_ACCT_NUM).getValue();
    profilePayment.account_transit_number = bankDetails
      .getAttribute(BDMDatastoreConstants.BANK_TRANSIT_NUM).getValue();
    profilePayment.payment_method =
      METHODOFDELIVERYEntry.EFT.toUserLocaleString();
    profilePayment.sort_code = bankDetails
      .getAttribute(BDMDatastoreConstants.BANK_SORT_CODE).getValue();
    return profilePayment;

  }

  private BDMUAAlert getAlertDetails()
    throws AppException, InformationalException {

    final BDMUAAlert alert = new BDMUAAlert();
    alert.alert_frequency =
      "One alert per day when new information is available";
    alert.pref_comm_method = pdcPersonDetails.prefCommMethod;
    alert.pref_oral_language = pdcPersonDetails.preferredLanguage;
    alert.pref_written_language = pdcPersonDetails.preferredLanguage;
    alert.receive_alert = "Yes";
    return alert;
  }

  private BDMUAAddress getProfileAddressDetails(final long concernRoleID,
    final AddressDtls address) throws AppException, InformationalException {

    final BDMUAAddress profileAddress = new BDMUAAddress();
    profileAddress.address_id = address.addressID;
    // profileAddress.address = address.getOneLineAddressString();
    profileAddress.type = "Mailing";
    final StringBuilder addressJSON = new StringBuilder();
    final String addressData = address.addressData;

    if (null != addressData) {
      addressJSON.append("{ ");
      final String[] addressDataLines = addressData.split("\n");
      for (int i = 0; i < addressDataLines.length; i++) {
        final String currentLine = addressDataLines[i];
        if (currentLine.contains("=")) {
          final String[] pairValue = currentLine.split("=");
          addressJSON.append("\"" + pairValue[0] + "\"");
          addressJSON.append(" : ");
          if (pairValue.length > 1) {
            addressJSON.append("\"" + pairValue[1] + "\"");
          } else {
            addressJSON.append("\"\"");
          }
          if (i < addressDataLines.length - 1)
            addressJSON.append(", ");
        }
      }
      addressJSON.append(" }");
    }
    final BDMUAAddressDetails addressDetails = new BDMUAAddressDetails();
    addressDetails.addressLayoutType =
      Configuration.getProperty(EnvVars.ENV_ADDRESS_LAYOUT);
    addressDetails.addressJSON = addressJSON.toString();
    profileAddress.addressDetails = addressDetails;
    return profileAddress;
  }

  private BDMUAProfileContactDetails getProfileContactDetails(
    final long concernRoleID) throws AppException, InformationalException {

    final BDMUAProfileContactDetails profileContactDetails =
      new BDMUAProfileContactDetails();

    // Add Addresses
    final List<AddressDtls> addressList = createAddressList();

    // final List<Address> addressList = ;
    // this.addressDAO.listActiveAddressesByConcernRole(concernRole);
    for (final AddressDtls address : addressList)
      profileContactDetails.addresses
        .addRef(getProfileAddressDetails(concernRoleID, address));

    // Add Emails
    final List<EmailAddressDtls> emailAddressList = createEmailAddressList();

    // final List<BDMEmailEvidenceVO> bdmEmailEvidenceVOList =
    // bdmEmailEvidence.getEvidenceValueObject(concernRole.getID());
    if (!emailAddressList.isEmpty()) {
      for (final EmailAddressDtls emailAddressDtls : emailAddressList) {
        profileContactDetails.emailAddresses
          .addRef(getProfileEmailAddressDetails(emailAddressDtls, true, true,
            EMAILTYPE.PERSONAL));
      }
    }

    // Add Phones
    final List<PhoneNumberDtls> phoneNumbersList =
      createPhoneNumberDtlsList();

    // final List<BDMPhoneEvidenceVO> bdmPhoneEvidenceVOList =
    // bdmPhoneEvidence.getEvidenceValueObject(concernRole.getID());
    if (!phoneNumbersList.isEmpty()) {
      for (final PhoneNumberDtls phNumberDtls : phoneNumbersList) {
        profileContactDetails.phoneNumbers
          .addRef(getProfilePhoneNumberDetails(phNumberDtls));
      }
    }
    return profileContactDetails;

  }

  private List<AddressDtls> createAddressList() {

    final AddressDtls addressDtls1 = new AddressDtls();
    addressDtls1.addressID = 8322652111389073408L;
    addressDtls1.addressData = pdcPersonDetails.addressData;

    final AddressDtls addressDtls2 = new AddressDtls();
    addressDtls2.addressID = 8322652111389073409L;
    addressDtls2.addressData = pdcPersonDetails.mailingAddressData;

    final List<AddressDtls> addressList = new ArrayList<AddressDtls>();
    addressList.add(addressDtls1);
    addressList.add(addressDtls2);

    return addressList;

  }

  private List<EmailAddressDtls> createEmailAddressList() {

    final EmailAddressDtls emailAddressDtls1 = new EmailAddressDtls();
    emailAddressDtls1.emailAddressID = 2345678;
    emailAddressDtls1.emailAddress = "mail@gmail.com";

    final EmailAddressDtls emailAddressDtls2 = new EmailAddressDtls();
    emailAddressDtls2.emailAddressID = 1345678;
    emailAddressDtls2.emailAddress = "email@gmail.com";

    final List<EmailAddressDtls> emailAddressList =
      new ArrayList<EmailAddressDtls>();
    emailAddressList.add(emailAddressDtls1);
    emailAddressList.add(emailAddressDtls2);

    return emailAddressList;
  }

  private List<PhoneNumberDtls> createPhoneNumberDtlsList() {

    // Add Phones
    final PhoneNumberDtls phoneNumberDtls1 = new PhoneNumberDtls();
    phoneNumberDtls1.phoneNumberID = 987654;
    phoneNumberDtls1.phoneAreaCode = "555";
    phoneNumberDtls1.phoneCountryCode = "1";
    phoneNumberDtls1.phoneNumber = "5551234";
    phoneNumberDtls1.phoneExtension = "123";

    final PhoneNumberDtls phoneNumberDtls2 = new PhoneNumberDtls();
    phoneNumberDtls2.phoneNumberID = 987653;
    phoneNumberDtls2.phoneAreaCode = "555";
    phoneNumberDtls2.phoneCountryCode = "1";
    phoneNumberDtls2.phoneNumber = "5552222";
    phoneNumberDtls2.phoneExtension = "";

    final List<PhoneNumberDtls> phoneNumbersList =
      new ArrayList<PhoneNumberDtls>();

    phoneNumbersList.add(phoneNumberDtls1);
    phoneNumbersList.add(phoneNumberDtls2);

    return phoneNumbersList;
  }

  private BDMUAEmailAddress getProfileEmailAddressDetails(
    final EmailAddressDtls emailAddressDtls, final Boolean preferred_ind,
    final Boolean use_for_alerts_ind, final String type)
    throws AppException, InformationalException {

    final BDMUAEmailAddress emailAddress = new BDMUAEmailAddress();
    emailAddress.email_address_id = emailAddressDtls.emailAddressID;
    emailAddress.email = emailAddressDtls.emailAddress;
    emailAddress.preferred_ind = preferred_ind;
    emailAddress.type = type;
    emailAddress.use_for_alerts_ind = use_for_alerts_ind;

    return emailAddress;
  }

  private BDMUAPhoneNumber
    getProfilePhoneNumberDetails(final PhoneNumberDtls phoneNumberDtls)
      throws AppException, InformationalException {

    final BDMUAPhoneNumber phoneNumber = new BDMUAPhoneNumber();
    phoneNumber.phone_number_id = phoneNumberDtls.phoneNumberID;
    phoneNumber.areaCode = phoneNumberDtls.phoneAreaCode;
    phoneNumber.countryCode = phoneNumberDtls.phoneCountryCode;
    phoneNumber.number = phoneNumberDtls.phoneNumber;
    phoneNumber.type = PHONETIMEEntry.NOT_SPECIFIED.toUserLocaleString();
    phoneNumber.extension = phoneNumberDtls.phoneExtension;
    phoneNumber.afternoon_ind =
      PHONETIMEEntry.AFTERNOONS.toUserLocaleString();
    phoneNumber.evening_ind = PHONETIMEEntry.EVENINGS.toUserLocaleString();
    phoneNumber.morning_ind = PHONETIMEEntry.MORNINGS.toUserLocaleString();
    phoneNumber.preferred_ind = true;
    phoneNumber.use_for_alerts_ind = true;
    return phoneNumber;
  }

}
