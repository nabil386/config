package curam.ca.gc.bdm.test.facade.pdcprospectperson.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMCORRESDELIVERY;
import curam.ca.gc.bdm.codetable.BDMEDUCATIONLEVEL;
import curam.ca.gc.bdm.codetable.BDMIEGYESNOOPTIONAL;
import curam.ca.gc.bdm.codetable.BDMPHONECOUNTRY;
import curam.ca.gc.bdm.codetable.BDMRECEIVEDFROM;
import curam.ca.gc.bdm.entity.fact.BDMProspectPersonFactory;
import curam.ca.gc.bdm.entity.intf.BDMProspectPerson;
import curam.ca.gc.bdm.entity.struct.BDMProspectPersonDtls;
import curam.ca.gc.bdm.entity.struct.BDMProspectPersonKey;
import curam.ca.gc.bdm.facade.participant.struct.BDMPersonRegistrationDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMProspectPersonRegistrationDetails;
import curam.ca.gc.bdm.facade.pdcperson.struct.BDMPersonDemographicPageDetails;
import curam.ca.gc.bdm.facade.pdcperson.struct.BDMReadPersonDemographicDetails;
import curam.ca.gc.bdm.facade.pdcprospectperson.fact.BDMPDCProspectPersonFactory;
import curam.ca.gc.bdm.facade.pdcprospectperson.intf.BDMPDCProspectPerson;
import curam.ca.gc.bdm.facade.pdcprospectperson.struct.BDMContactPreferenceDetails;
import curam.ca.gc.bdm.facade.representative.struct.BDMRepresentativeSummaryDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMPERSON;
import curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.ADDRESSELEMENTTYPE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.COUNTRYCODE;
import curam.codetable.EMAILTYPE;
import curam.codetable.LANGUAGE;
import curam.codetable.METHODOFDELIVERY;
import curam.codetable.PHONETYPE;
import curam.codetable.PROVINCETYPE;
import curam.codetable.STATECODES;
import curam.core.facade.fact.PersonFactory;
import curam.core.facade.fact.ProspectPersonFactory;
import curam.core.facade.struct.ConcernRoleIDKey;
import curam.core.facade.struct.PersonRegistrationDetails;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.facade.struct.ProspectPersonRegistrationDetails;
import curam.core.fact.AddressDataFactory;
import curam.core.intf.AddressData;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.AddressMap;
import curam.core.struct.AddressMapList;
import curam.core.struct.ElementDetails;
import curam.core.struct.OtherAddressData;
import curam.core.struct.ProspectPersonKey;
import curam.pdc.facade.struct.ProspectPersonRegistrationResult;
import curam.pdc.facade.struct.ReadProspectPersonKey;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.administration.fact.CodeTableAdminFactory;
import curam.util.administration.intf.CodeTableAdmin;
import curam.util.administration.struct.CodeTableItemUniqueKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.exception.LocalisableString;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Task-17494 - Junit class to manage Prospect person demographics details
 *
 * @author rajat.soni
 *
 */
@RunWith(JMockit.class)
public class BDMPDCProspectPersonTest extends CuramServerTestJUnit4 {

  private long concernRoleID = 0;

  private final BDMUtil bdmUtil = new BDMUtil();

  @Before
  public void setUp() throws AppException, InformationalException {

    ProspectPersonRegistrationDetails details =
      new ProspectPersonRegistrationDetails();

    details = getProspectPersonRegistrationDetails();

    final curam.core.facade.struct.ProspectPersonRegistrationResult registrationResult =
      ProspectPersonFactory.newInstance().registerProspectPerson(details);

    this.concernRoleID =
      registrationResult.registrationIDDetails.concernRoleID;

    final BDMProspectPerson bdmProspectPersonObj =
      BDMProspectPersonFactory.newInstance();
    final BDMProspectPersonKey bdmProspectPersonKey =
      new BDMProspectPersonKey();
    bdmProspectPersonKey.concernRoleID = this.concernRoleID;
    BDMProspectPersonDtls bdmProspectPersonDtls = new BDMProspectPersonDtls();

    bdmProspectPersonDtls = new BDMProspectPersonDtls();
    bdmProspectPersonDtls.concernRoleID = bdmProspectPersonKey.concernRoleID;
    bdmProspectPersonDtls.educationLevel = BDMEDUCATIONLEVEL.COLLEGE;
    bdmProspectPersonDtls.indigenousPersonType = BDMIEGYESNOOPTIONAL.YES;
    bdmProspectPersonDtls.memberMinorityGrpType = BDMIEGYESNOOPTIONAL.NO;

    bdmProspectPersonObj.insert(bdmProspectPersonDtls);

  }

  /**
   * Junit test method for readBDMNonPDCDemographicData()
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testReadBDMNonPDCDemographicData()
    throws AppException, InformationalException {

    final BDMPDCProspectPerson bdmPDCProspectPersonObj =
      BDMPDCProspectPersonFactory.newInstance();
    final ProspectPersonKey prospectPersonKey = new ProspectPersonKey();
    prospectPersonKey.concernRoleID = this.concernRoleID;
    BDMReadPersonDemographicDetails bdmReadPersonDemographicDetails =
      new BDMReadPersonDemographicDetails();
    bdmReadPersonDemographicDetails =
      bdmPDCProspectPersonObj.readBDMNonPDCDemographicData(prospectPersonKey);

    assertEquals(bdmReadPersonDemographicDetails.educationLevel,
      BDMEDUCATIONLEVEL.COLLEGE);
    assertEquals(bdmReadPersonDemographicDetails.indigenousPersonType,
      BDMIEGYESNOOPTIONAL.YES);
    assertEquals(bdmReadPersonDemographicDetails.memberMinorityGrpType,
      BDMIEGYESNOOPTIONAL.NO);
  }

  /**
   * Junit test method for readBDMNonPDCDemographicData()
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testreadContactPreferencesEvidence()
    throws AppException, InformationalException {

    final BDMPDCProspectPerson bdmPDCProspectPersonObj =
      BDMPDCProspectPersonFactory.newInstance();
    final ConcernRoleIDKey concernRoleIDKey = new ConcernRoleIDKey();
    concernRoleIDKey.concernRoleID = this.concernRoleID;
    BDMContactPreferenceDetails bdmReadPersonDemographicDetails =
      new BDMContactPreferenceDetails();
    bdmReadPersonDemographicDetails = bdmPDCProspectPersonObj
      .readContactPreferencesEvidence(concernRoleIDKey);

    assertEquals(bdmReadPersonDemographicDetails.preferredOralLang,
      LANGUAGE.ENGLISH);
    assertEquals(bdmReadPersonDemographicDetails.preferredWrittenLang,
      LANGUAGE.ENGLISH);

  }

  /**
   * Junit test method for modifyBDMNonPDCDemographicData()
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testModifyBDMNonPDCDemographicData()
    throws AppException, InformationalException {

    final BDMPDCProspectPerson bdmPDCProspectPersonObj =
      BDMPDCProspectPersonFactory.newInstance();

    final ProspectPersonKey prospectPersonKey = new ProspectPersonKey();
    prospectPersonKey.concernRoleID = this.concernRoleID;
    BDMReadPersonDemographicDetails bdmReadPersonDemographicDetails =
      new BDMReadPersonDemographicDetails();
    bdmReadPersonDemographicDetails =
      bdmPDCProspectPersonObj.readBDMNonPDCDemographicData(prospectPersonKey);

    final BDMPersonDemographicPageDetails bdmPersonDemographicPageDetails =
      new BDMPersonDemographicPageDetails();

    bdmPersonDemographicPageDetails.assign(bdmReadPersonDemographicDetails);

    bdmPersonDemographicPageDetails.comments = "Performing update";
    bdmPersonDemographicPageDetails.educationLevel =
      BDMEDUCATIONLEVEL.UNIVERSITY;

    bdmPDCProspectPersonObj
      .modifyBDMNonPDCDemographicData(bdmPersonDemographicPageDetails);

    final BDMProspectPerson bdmProspectPersonObj =
      BDMProspectPersonFactory.newInstance();
    final BDMProspectPersonKey bdmProspectPersonKey =
      new BDMProspectPersonKey();
    bdmProspectPersonKey.concernRoleID = this.concernRoleID;
    final BDMProspectPersonDtls bdmProspectPersonDtls =
      bdmProspectPersonObj.read(bdmProspectPersonKey);

    assertEquals(bdmProspectPersonDtls.educationLevel,
      BDMEDUCATIONLEVEL.UNIVERSITY);
    assertEquals(bdmProspectPersonDtls.indigenousPersonType,
      BDMIEGYESNOOPTIONAL.YES);
    assertEquals(bdmProspectPersonDtls.memberMinorityGrpType,
      BDMIEGYESNOOPTIONAL.NO);

  }

  /**
   * Helper method to setup data for prospect person
   *
   * @return Returns prospect person registration details
   */
  private ProspectPersonRegistrationDetails
    getProspectPersonRegistrationDetails()
      throws AppException, InformationalException {

    final ProspectPersonRegistrationDetails details =
      new ProspectPersonRegistrationDetails();

    details.prospectPersonRegistrationDtls.firstForename = "Test";
    details.prospectPersonRegistrationDtls.surname = "Prospect Person";
    details.prospectPersonRegistrationDtls.gender = "SX1";

    final java.util.Calendar cal = new java.util.GregorianCalendar(1970,
      java.util.Calendar.APRIL, 1, 0, 0, 0);
    final curam.util.type.Date birthday = new curam.util.type.Date(cal);

    details.prospectPersonRegistrationDtls.dateOfBirth = birthday;
    details.prospectPersonRegistrationDtls.registrationDate =
      curam.util.type.Date.getCurrentDate();
    details.prospectPersonRegistrationDtls.maritalStatusCode =
      curam.codetable.MARITALSTATUS.MARRIED;
    details.prospectPersonRegistrationDtls.nationalityCode =
      curam.codetable.NATIONALITY.DEFAULTCODE;
    details.prospectPersonRegistrationDtls.countryOfBirth =
      curam.codetable.COUNTRY.CA;

    // parseFieldsToData always make US address type - it is irrelevant what
    // is defined here.
    details.prospectPersonRegistrationDtls.addressType =
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
    addressFieldDetails.countryCode = curam.codetable.COUNTRYCODE.CACODE;
    // END, CR00272990
    OtherAddressData otherAddressData = null;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    details.prospectPersonRegistrationDtls.addressData =
      otherAddressData.addressData;

    // BEGIN, CR00141773, CW
    details.prospectPersonRegistrationDtls.paymentFrequency = "100100100";
    details.prospectPersonRegistrationDtls.methodOfPmtCode =
      METHODOFDELIVERY.CHEQUE;
    details.prospectPersonRegistrationDtls.currencyType =
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
    mailingAddressFieldDetails.countryCode =
      curam.codetable.COUNTRYCODE.CACODE;

    final OtherAddressData mailingAddressData =
      addressDataObj.parseFieldsToData(mailingAddressFieldDetails);

    details.prospectPersonRegistrationDtls.mailingAddressData =
      mailingAddressData.addressData;

    // Add email address details
    details.prospectPersonRegistrationDtls.contactEmailAddress =
      "test@gmail.com";
    details.prospectPersonRegistrationDtls.contactEmailType =
      EMAILTYPE.PERSONAL;

    return details;

  }

  @Test
  public void testRegisterIOProspectPersonAsPerson()
    throws AppException, InformationalException {

    final ReadProspectPersonKey key = new ReadProspectPersonKey();
    final BDMProspectPersonRegistrationDetails details =
      new BDMProspectPersonRegistrationDetails();

    ProspectPersonRegistrationResult result =
      new ProspectPersonRegistrationResult();
    final PersonRegistrationResult originalPerson1 =
      registerPersonwithSIN("Original Person with SIN", "081975187");

    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    // Person Address Details
    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;

    addressFieldDetails.suiteNum = "5789";
    addressFieldDetails.addressLine1 = "Cruise Ave";
    addressFieldDetails.addressLine2 = "Train street";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Ontario";
    addressFieldDetails.postalCode = "T6K 3M1";
    addressFieldDetails.countryCode = curam.codetable.COUNTRYCODE.CACODE;

    OtherAddressData otherAddressData = null;
    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    key.dtls.concernRoleID =
      originalPerson1.registrationIDDetails.concernRoleID;
    details.bdmReceivedFrom = "Start";
    details.countryCode = curam.codetable.COUNTRYCODE.CACODE;
    details.dtls.prospectPersonRegistrationDtls.addressData =
      otherAddressData.addressData;
    details.dtls.prospectPersonRegistrationDtls.countryOfBirth =
      curam.codetable.COUNTRY.CA;

    details.dtls.prospectPersonRegistrationDtls.addressIndicator =
      Boolean.TRUE;
    details.dtls.prospectPersonRegistrationDtls.foreignResidencyCountryCode =
      curam.codetable.COUNTRYCODE.CACODE;

    details.dtls.prospectPersonRegistrationDtls.registrationDate =
      curam.util.type.Date.getCurrentDate();
    details.dtls.prospectPersonRegistrationDtls.maritalStatusCode =
      curam.codetable.MARITALSTATUS.MARRIED;
    details.dtls.prospectPersonRegistrationDtls.addressType =
      CONCERNROLEADDRESSTYPE.PRIVATE.toString();
    details.dtls.prospectPersonRegistrationDtls.alternateIDTypeCodeOpt =
      "3223232";
    details.dtls.prospectPersonRegistrationDtls.citizenshipCountryCode =
      curam.codetable.COUNTRYCODE.CACODE;
    details.dtls.prospectPersonRegistrationDtls.citizenshipFromDate =
      curam.util.type.Date.getCurrentDate();
    details.dtls.prospectPersonRegistrationDtls.citizenshipID = 323232332;
    details.dtls.prospectPersonRegistrationDtls.socialSecurityNumber =
      originalPerson1.registrationIDDetails.alternateID;
    details.dtls.prospectPersonRegistrationDtls.citizenshipReasonCode = "123";
    details.dtls.prospectPersonRegistrationDtls.citizenshipToDate =
      curam.util.type.Date.getCurrentDate();
    details.dtls.prospectPersonRegistrationDtls.commentsOpt =
      "Please Enter Comments";
    details.dtls.prospectPersonRegistrationDtls.concernID =
      this.concernRoleID;
    details.dtls.prospectPersonRegistrationDtls.gender = "SX1";
    details.dtls.prospectPersonRegistrationDtls.nameSuffix = "JR";
    details.dtls.prospectPersonRegistrationDtls.phoneType =
      PHONETYPE.PERSONAL_MOBILE;
    details.dtls.prospectPersonRegistrationDtls.countryOfBirth =
      curam.codetable.COUNTRY.CA;
    details.dtls.prospectPersonRegistrationDtls.estimatedFromAgeOpt = "10";
    details.dtls.prospectPersonRegistrationDtls.firstForename = "Test";
    details.dtls.prospectPersonRegistrationDtls.foreignResidencyID = 455445;
    details.dtls.prospectPersonRegistrationDtls.foreignResidencyReasonCode =
      "0001";

    details.dtls.prospectPersonRegistrationDtls.concernRolePhoneNumberID =
      767656566;
    details.dtls.prospectPersonRegistrationDtls.contactEmailAddress =
      "abc.abc@gmail.com";
    details.dtls.prospectPersonRegistrationDtls.contactEmailType = "Mailing";
    details.dtls.prospectPersonRegistrationDtls.estimatedFromAgeOpt =
      "01012001";
    details.dtls.prospectPersonRegistrationDtls.personBirthName =
      "Test Prospect";
    details.dtls.prospectPersonRegistrationDtls.phoneAreaCode = "001";
    details.dtls.prospectPersonRegistrationDtls.phoneCountryCode =
      BDMPHONECOUNTRY.CANADA;
    details.dtls.prospectPersonRegistrationDtls.phoneNumber = "5646565";
    details.dtls.prospectPersonRegistrationDtls.paymentFrequency = "Monthly";
    details.dtls.prospectPersonRegistrationDtls.preferredLanguage =
      LANGUAGE.ENGLISH;
    details.dtls.prospectPersonRegistrationDtls.preferredName =
      "Test Prospect";
    details.dtls.prospectPersonRegistrationDtls.primaryAddressID =
      8322652111389073409L;
    details.dtls.prospectPersonRegistrationDtls.primaryAlternateNameID =
      656565;
    details.dtls.prospectPersonRegistrationDtls.primaryPhoneNumberID =
      45454545;
    details.isMailingAddSame = Boolean.TRUE;
    details.phoneCountryCode = BDMPHONECOUNTRY.CANADA;
    details.preferredOralLang = LANGUAGE.ENGLISH;
    details.preferredWrittenLang = "English";
    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    final BDMPDCProspectPerson bdmPDCProspectPersonObj =
      BDMPDCProspectPersonFactory.newInstance();
    final ProspectPersonRegistrationResult prospectPersonRegistrationResult =
      bdmPDCProspectPersonObj.registerProspectPersonAsPerson(key, details);

    // // call SIN-SIR Check
    // if (!StringUtil
    // .isNullOrEmpty(originalPerson1.registrationIDDetails.alternateID)) {
    // bdmUtil.callSINSIR(originalPerson1.registrationIDDetails.alternateID,
    // originalPerson1.registrationIDDetails.concernRoleID);
    // }
    //
    // // modify address evidence
    // if (!StringUtil.isNullOrEmpty(details.countryCode)) {
    // bdmUtil.updateEvidenceAttributes(details.bdmReceivedFrom,
    // details.countryCode,
    // originalPerson1.registrationIDDetails.concernRoleID);
    // }

    // final BDMPDCProspectPerson bdmProspectPerson =
    // BDMPDCProspectPersonFactory.newInstance();

    result =
      bdmPDCProspectPersonObj.registerIOProspectPersonAsPerson(key, details);

    assertTrue(result.dtls.registrationIDDetails.concernRoleID != 0);
  }

  /** register a person **/

  private PersonRegistrationResult
    registerPersonwithSIN(final String lastName, final String sinNumber)
      throws AppException, InformationalException {

    // Person Details
    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "Test";
    bdmPersonRegistrationDetails.dtls.surname = lastName;
    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19800101");
    bdmPersonRegistrationDetails.dtls.socialSecurityNumber = sinNumber;
    bdmPersonRegistrationDetails.dtls.alternateIDTypeCodeOpt =
      CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER;

    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    // Person Address Details
    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;

    addressFieldDetails.suiteNum = "5789";
    addressFieldDetails.addressLine1 = "Cruise Ave";
    addressFieldDetails.addressLine2 = "Train street";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Ontario";
    addressFieldDetails.postalCode = "T6K 3M1";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;

    OtherAddressData otherAddressData = null;
    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    bdmPersonRegistrationDetails.dtls.addressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.mailingAddressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.registrationDate =
      Date.getCurrentDate();

    // Register Person
    final PersonRegistrationDetails details = new PersonRegistrationDetails();
    details.personRegistrationDetails = bdmPersonRegistrationDetails.dtls;

    return PersonFactory.newInstance().register(details);

  }

  @Test
  public void testregisterProspectPersonAsPerson()
    throws AppException, InformationalException {

    final BDMPDCProspectPerson bdmPDCProspectPersonObj =
      BDMPDCProspectPersonFactory.newInstance();
    final ReadProspectPersonKey prospectPersonKey =
      new ReadProspectPersonKey();
    prospectPersonKey.dtls.concernRoleID = this.concernRoleID;
    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();

    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19800101");
    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    // Person Address Details
    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;

    addressFieldDetails.suiteNum = "5789";
    addressFieldDetails.addressLine1 = "Cruise Ave";
    addressFieldDetails.addressLine2 = "Train street";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Ontario";
    addressFieldDetails.postalCode = "T6K 3M1";
    addressFieldDetails.stateCode = STATECODES.DEFAULTCODE;
    addressFieldDetails.countryCode = curam.codetable.COUNTRYCODE.CACODE;

    OtherAddressData otherAddressData = null;
    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    bdmPersonRegistrationDetails.dtls.addressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.mailingAddressData =
      otherAddressData.addressData;

    ProspectPersonRegistrationResult bdmReadPersonDemographicDetails =
      new ProspectPersonRegistrationResult();
    final BDMRepresentativeSummaryDetails details =
      new BDMRepresentativeSummaryDetails();
    final BDMEvidenceUtilsTest bdmEvidenceUtilsTest =
      new BDMEvidenceUtilsTest();
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();
    final java.util.Calendar cal = new java.util.GregorianCalendar(1970,
      java.util.Calendar.APRIL, 1, 0, 0, 0);
    final curam.util.type.Date birthday = new curam.util.type.Date(cal);
    final PersonRegistrationResult originalPerson1 =
      registerPersonwithSIN("Original Person with SIN", "081975187");
    final String dupName1 = "Duplicate Person1";

    final long concernRoleID = pdcPersonDetails.concernRoleID;

    final BDMProspectPersonRegistrationDetails details1 =
      new BDMProspectPersonRegistrationDetails();
    details1.bdmReceivedFrom = BDMRECEIVEDFROM.FOREIGN_GOVERNMENT;
    details1.countryCode = curam.codetable.COUNTRY.CA;

    details1.isMailingAddSame = true;
    details1.phoneCountryCode = BDMPHONECOUNTRY.CANADA;
    details1.preferredCorrDeliveryMethod = BDMCORRESDELIVERY.POSTALDIG;
    details1.preferredOralLang = LANGUAGE.ENGLISH;
    details1.preferredWrittenLang = LANGUAGE.ENGLISH;
    // details1.dtls.prospectPersonRegistrationDtls.addressData =
    // "1\n" + "100\n" + "BDMINTL\n" + "CA\n" + "1\n" + "1\n" + "COUNTRY=CA\n"
    // + "POSTCODE=T3T 3T3\n" + "ZIP=\n" + "BDMZIPX=\n" + "APT=\n"
    // + "POBOXNO=\n" + "ADD1=123\n" + "ADD2=Street\n" + "CITY=Edmonton\n"
    // + "PROV=AB\n" + "STATEPROV=\n" + "BDMSTPROVX=";

    details1.dtls.prospectPersonRegistrationDtls.addressData =
      otherAddressData.addressData;
    details1.dtls.prospectPersonRegistrationDtls.mailingAddressData =
      otherAddressData.addressData;
    details1.dtls.prospectPersonRegistrationDtls.addressIndicator =
      Boolean.TRUE;
    details1.dtls.prospectPersonRegistrationDtls.addressType =
      CONCERNROLEADDRESSTYPE.PRIVATE.toString();
    details1.dtls.prospectPersonRegistrationDtls.alternateIDTypeCodeOpt =
      CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER;
    details1.dtls.prospectPersonRegistrationDtls.concernID =
      this.concernRoleID;
    details1.dtls.prospectPersonRegistrationDtls.contactPhoneAreaCode = "555";
    details1.dtls.prospectPersonRegistrationDtls.dateOfBirth = birthday;
    details1.dtls.prospectPersonRegistrationDtls.firstForename = "Johnny";
    details1.dtls.prospectPersonRegistrationDtls.contactEmailAddress =
      "test@gmail.com";
    details1.dtls.prospectPersonRegistrationDtls.phoneAreaCode = "134";
    details1.dtls.prospectPersonRegistrationDtls.phoneNumber = "4567890";
    details1.dtls.prospectPersonRegistrationDtls.phoneExtension = "";
    details1.dtls.prospectPersonRegistrationDtls.phoneType =
      PHONETYPE.PERSONAL_MOBILE;
    details1.dtls.prospectPersonRegistrationDtls.methodOfPmtCode =
      METHODOFDELIVERY.CHEQUE;
    details1.dtls.prospectPersonRegistrationDtls.paymentFrequency =
      "100100100";
    details1.dtls.prospectPersonRegistrationDtls.registrationDate =
      curam.util.type.Date.getCurrentDate();
    details1.dtls.prospectPersonRegistrationDtls.maritalStatusCode =
      curam.codetable.MARITALSTATUS.MARRIED;

    validateRegistrationDetails(details1);
    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails1 =
      new BDMPersonRegistrationDetails();
    bdmReadPersonDemographicDetails = bdmPDCProspectPersonObj
      .registerProspectPersonAsPerson(prospectPersonKey, details1);

    assertEquals(
      bdmReadPersonDemographicDetails.dtls.registrationIDDetails.alternateID,
      "081975187");
    assertEquals(
      bdmReadPersonDemographicDetails.dtls.registrationIDDetails.concernRoleID,
      pdcPersonDetails.concernRoleID);

  }

  /**
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private PersonRegistrationResult createPersonRecord(final String firstName,
    final String lastName) throws AppException, InformationalException {

    // Person Details
    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = firstName;
    bdmPersonRegistrationDetails.dtls.surname = lastName;
    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19800101");

    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    // Person Address Details
    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;

    addressFieldDetails.suiteNum = "5789";
    addressFieldDetails.addressLine1 = "Cruise Ave";
    addressFieldDetails.addressLine2 = "Train street";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Ontario";
    addressFieldDetails.postalCode = "T6K 3M1";
    addressFieldDetails.countryCode = curam.codetable.COUNTRYCODE.CACODE;

    OtherAddressData otherAddressData = null;
    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    bdmPersonRegistrationDetails.dtls.addressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.mailingAddressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.registrationDate =
      Date.getCurrentDate();

    // Register Person
    final PersonRegistrationDetails details = new PersonRegistrationDetails();
    details.personRegistrationDetails = bdmPersonRegistrationDetails.dtls;

    final PersonRegistrationResult registrationResult =
      PersonFactory.newInstance().register(details);
    return registrationResult;
  }

  // @Test
  // public void testregisterProspectPersonAsPerson()
  // throws AppException, InformationalException {
  //
  // final ReadProspectPersonKey key = new ReadProspectPersonKey();
  // final BDMProspectPersonRegistrationDetails details =
  // new BDMProspectPersonRegistrationDetails();
  //
  // ProspectPersonRegistrationResult result =
  // new ProspectPersonRegistrationResult();
  //
  // key.dtls.concernRoleID = 4545445;
  // details.bdmReceivedFrom = "Start";
  // details.countryCode = "123";
  // details.dtls.prospectPersonRegistrationDtls.addressData = "OverCup";
  // details.dtls.prospectPersonRegistrationDtls.addressIndicator =
  // Boolean.TRUE;
  // details.dtls.prospectPersonRegistrationDtls.addressType = "Street1";
  // details.dtls.prospectPersonRegistrationDtls.alternateIDTypeCodeOpt =
  // "3223232";
  // details.dtls.prospectPersonRegistrationDtls.citizenshipCountryCode =
  // "001";
  // details.dtls.prospectPersonRegistrationDtls.citizenshipFromDate =
  // curam.util.type.Date.getCurrentDate();
  // details.dtls.prospectPersonRegistrationDtls.citizenshipID = 323232332;
  // details.dtls.prospectPersonRegistrationDtls.citizenshipReasonCode = "123";
  // details.dtls.prospectPersonRegistrationDtls.citizenshipToDate =
  // curam.util.type.Date.getCurrentDate();
  // details.dtls.prospectPersonRegistrationDtls.commentsOpt =
  // "Please Enter Comments";
  // details.dtls.prospectPersonRegistrationDtls.concernID = 67766767;
  // details.dtls.prospectPersonRegistrationDtls.gender = "SX1";
  // details.dtls.prospectPersonRegistrationDtls.nameSuffix = "JR";
  // details.dtls.prospectPersonRegistrationDtls.phoneType = "CELL";
  // details.dtls.prospectPersonRegistrationDtls.countryOfBirth = "USA";
  // details.dtls.prospectPersonRegistrationDtls.estimatedFromAgeOpt = "10";
  // details.dtls.prospectPersonRegistrationDtls.firstForename = "Test";
  // details.dtls.prospectPersonRegistrationDtls.foreignResidencyID = 455445;
  // details.dtls.prospectPersonRegistrationDtls.foreignResidencyReasonCode =
  // "0001";
  //
  // details.dtls.prospectPersonRegistrationDtls.concernRolePhoneNumberID =
  // 767656566;
  // details.dtls.prospectPersonRegistrationDtls.contactEmailAddress =
  // "abc.abc@gmail.com";
  // details.dtls.prospectPersonRegistrationDtls.contactEmailType = "Mailing";
  // details.dtls.prospectPersonRegistrationDtls.estimatedFromAgeOpt =
  // "01012001";
  // details.dtls.prospectPersonRegistrationDtls.personBirthName =
  // "Test Prospect";
  // details.dtls.prospectPersonRegistrationDtls.phoneAreaCode = "001";
  // details.dtls.prospectPersonRegistrationDtls.phoneCountryCode = "456";
  // details.dtls.prospectPersonRegistrationDtls.phoneNumber = "5646";
  // details.dtls.prospectPersonRegistrationDtls.paymentFrequency = "Monthly";
  // details.dtls.prospectPersonRegistrationDtls.preferredLanguage = "English";
  // details.dtls.prospectPersonRegistrationDtls.preferredName =
  // "Test Prospect";
  // details.dtls.prospectPersonRegistrationDtls.primaryAddressID =
  // 8322652111389073409L;
  // details.dtls.prospectPersonRegistrationDtls.primaryAlternateNameID =
  // 656565;
  // details.dtls.prospectPersonRegistrationDtls.primaryPhoneNumberID =
  // 45454545;
  // details.isMailingAddSame = Boolean.TRUE;
  // details.phoneCountryCode = "756";
  // details.preferredOralLang = "English";
  // details.preferredWrittenLang = "English";
  //
  // final BDMPDCProspectPerson bdmProspectPerson =
  // BDMPDCProspectPersonFactory.newInstance();
  //
  // result = bdmProspectPerson.registerIOProspectPersonAsPerson(key, details);
  //
  // assertTrue(result.dtls.registrationIDDetails.concernRoleID != 0);
  // }

  private void validateRegistrationDetails(
    final BDMProspectPersonRegistrationDetails details)
    throws InformationalException, AppException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    // Address validations
    validateAddress(details.dtls.prospectPersonRegistrationDtls.addressData,
      informationalManager);

    // Validation-1
    if (!details.isMailingAddSame && isAddressEmpty(
      details.dtls.prospectPersonRegistrationDtls.mailingAddressData)) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_MAILING_ADDRESS_MANDATORY);

      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);
    } else if (!details.isMailingAddSame && !isAddressEmpty(
      details.dtls.prospectPersonRegistrationDtls.mailingAddressData)) {

      validateAddress(
        details.dtls.prospectPersonRegistrationDtls.mailingAddressData,
        informationalManager);
    }

    final CodeTableAdmin codeTableAdminObj =
      CodeTableAdminFactory.newInstance();
    final CodeTableItemUniqueKey codeTableItemUniqueKey =
      new CodeTableItemUniqueKey();

    codeTableItemUniqueKey.code = details.phoneCountryCode;
    codeTableItemUniqueKey.locale = TransactionInfo.getProgramLocale();
    codeTableItemUniqueKey.tableName = BDMPHONECOUNTRY.TABLENAME;

    final Boolean isPlusOneCountry = details.phoneCountryCode.isEmpty()
      ? false
      : codeTableAdminObj
        .readCTIDetailsForLocaleOrLanguage(codeTableItemUniqueKey).annotation
          .trim().equals(BDMConstants.kphonePrefix);

    // validation-2 if country code is +1, then area code is mandatory
    if (isPlusOneCountry
      && details.dtls.prospectPersonRegistrationDtls.phoneAreaCode
        .isEmpty()) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PHONE_AREA_CODE);

      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);
    } else if (isPlusOneCountry && (!isNumeric(
      details.dtls.prospectPersonRegistrationDtls.phoneAreaCode)
      || details.dtls.prospectPersonRegistrationDtls.phoneAreaCode
        .length() != 3)) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PHONE_AREA_CODE_3DIGIT);

      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);

    }

    // validation-4 if country code is +1 then phone Number must be 7 digits and
    // numeric
    if (isPlusOneCountry
      && details.dtls.prospectPersonRegistrationDtls.phoneNumber.isEmpty()) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PHONE_NUMBER_MISSING);

      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);

    } else if (isPlusOneCountry
      && (!isNumeric(details.dtls.prospectPersonRegistrationDtls.phoneNumber)
        || details.dtls.prospectPersonRegistrationDtls.phoneNumber
          .length() != 7)) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PHONE_NUMBER_7DIGIT);

      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);

    }
    // validation-5 if country code is not +1 then phone Number and Area code
    // must be numeric
    if (!isPlusOneCountry && (!isNumeric(
      details.dtls.prospectPersonRegistrationDtls.phoneAreaCode)
      || !isNumeric(
        details.dtls.prospectPersonRegistrationDtls.phoneNumber))) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PHONE_NUMBER_OTHER_COUNTRY);

      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);

    }
    // validation-6 if Phone Type is Personal or Business Mobile then extension
    // cannot be added
    if ((details.dtls.prospectPersonRegistrationDtls.phoneType
      .equals(PHONETYPE.PERSONAL_MOBILE)
      || details.dtls.prospectPersonRegistrationDtls.phoneType
        .equals(PHONETYPE.BUSINESS_MOBILE))
      && !details.dtls.prospectPersonRegistrationDtls.phoneExtension
        .isEmpty()) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PHONE_INVALID_TYPE);

      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);

    }

    // validation-7 Extension can only have numeric values
    if (!isNumeric(
      details.dtls.prospectPersonRegistrationDtls.phoneExtension)) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PHONE_EXTENSION_NUMERIC);

      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);

    }

    informationalManager.failOperation();

  }

  /**
   * Util method to validate address fields
   *
   * @since ADO-7089
   * @param addressData
   * @param informationalManager
   * @throws InformationalException
   */
  private InformationalManager validateAddress(final String addressData,
    final InformationalManager informationalManager)
    throws InformationalException, AppException {

    final OtherAddressData otherAddressData = new OtherAddressData();
    otherAddressData.addressData = addressData;

    // get Country
    final String country =
      this.getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.COUNTRY);

    // Postal Code
    final String postalCode =
      this.getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.POSTCODE);

    // state
    final String state =
      this.getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.STATE);

    // city
    final String city =
      this.getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.CITY);

    // province
    final String province =
      this.getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.PROVINCE);

    final String zipCode =
      this.getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.ZIP);

    // validations for CA Address
    if (country.equalsIgnoreCase(COUNTRYCODE.CACODE)) {

      // City is Mandatory
      if (city.isEmpty()) {

        final LocalisableString localisableString =
          new LocalisableString(BDMPERSON.ERR_ADDR_CA_CITY_MISSING);

        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kError);

      }
      // Province is Mandatory
      if (province.isEmpty()) {

        final LocalisableString localisableString =
          new LocalisableString(BDMPERSON.ERR_ADDR_CA_PROV_MISSING);

        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kError);
      }
      // Postal Code is Mandatory
      if (postalCode.isEmpty()) {

        final LocalisableString localisableString =
          new LocalisableString(BDMPERSON.ERR_ADDR_CA_POSTALCODE_MISSING);

        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kError);

      } else if (!bdmUtil.isValidPostalCode(postalCode)) {// Postal Code should
                                                          // be in valid
                                                          // Canadian Format

        final LocalisableString localisableString =
          new LocalisableString(BDMPERSON.ERR_ADDR_CA_INVALID_POSTAL);

        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kError);

      }

    } else if (country.equalsIgnoreCase(COUNTRYCODE.USCODE)) {// validations for
                                                              // US Address

      if (zipCode.isEmpty()) {// Zip code is mandatory

        final LocalisableString localisableString =
          new LocalisableString(BDMPERSON.ERR_ADDR_US_ZIPCODE_MISSING);

        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kError);

      }

    }
    if (!country.equalsIgnoreCase(COUNTRYCODE.CACODE)) {// Validations for all
                                                        // other countries

      if (state.isEmpty()) {

        final LocalisableString localisableString =
          new LocalisableString(BDMPERSON.ERR_ADDR_STATE_MISSING);

        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kError);

      }
    }

    return informationalManager;

  }

  /**
   *
   * @param mailingAddressData
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private boolean isAddressEmpty(final String mailingAddressData)
    throws AppException, InformationalException {

    final OtherAddressData otherAddressData = new OtherAddressData();
    otherAddressData.addressData = mailingAddressData;

    Boolean isEmptyAddress = false;

    // get Country
    final String country =
      this.getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.COUNTRY);

    // city
    final String city =
      this.getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.CITY);

    if (city.isEmpty() || country.isEmpty()) {
      isEmptyAddress = true;
    }

    return isEmptyAddress;
  }

  /**
   *
   * Util Method to convert Address Data
   *
   * @param otherAddressData
   * @param addressElementType
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private String getAddressDetails(final OtherAddressData otherAddressData,
    final String addressElementType)
    throws AppException, InformationalException {

    final AddressData addressDataObj = AddressDataFactory.newInstance();

    final AddressMap addressMap = new AddressMap();

    addressMap.name = addressElementType;

    final AddressMapList addressMapList =
      addressDataObj.parseDataToMap(otherAddressData);

    final ElementDetails elementDetails =
      addressDataObj.findElement(addressMapList, addressMap);

    if (elementDetails.elementFound) {

      return elementDetails.elementValue;
    }

    return "";

  }

  /**
   * Util Method to validate if given string is a number
   *
   * @since TASK-9375
   * @param phoneNumber
   * @return
   */
  private boolean isNumeric(final String phoneNumber) {

    if (phoneNumber.isEmpty()) {

      return true;

    }
    // regex to check for Numeric Values
    final Pattern pattern = Pattern.compile("^[0-9]*$");
    final Matcher matcher = pattern.matcher(phoneNumber);
    return matcher.matches();
  }
}
