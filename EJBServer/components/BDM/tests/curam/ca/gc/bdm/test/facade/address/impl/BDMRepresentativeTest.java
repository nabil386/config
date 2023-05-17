package curam.ca.gc.bdm.test.facade.address.impl;

import curam.ca.gc.bdm.codetable.BDMPHONECOUNTRY;
import curam.ca.gc.bdm.facade.participant.struct.BDMPersonRegistrationDetails;
import curam.ca.gc.bdm.facade.representative.fact.BDMRepresentativeFactory;
import curam.ca.gc.bdm.facade.representative.intf.BDMRepresentative;
import curam.ca.gc.bdm.facade.representative.struct.BDMRepresentativeHomePageDetails;
import curam.ca.gc.bdm.facade.representative.struct.BDMRepresentativeSummaryDetails;
import curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.LANGUAGE;
import curam.codetable.METHODOFDELIVERY;
import curam.codetable.PROVINCETYPE;
import curam.core.facade.fact.PersonFactory;
import curam.core.facade.struct.InformationalMsgDetailsList;
import curam.core.facade.struct.PersonRegistrationDetails;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.facade.struct.RepresentativeKey;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.MaintainAddressKey;
import curam.core.struct.OtherAddressData;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import jdk.nashorn.internal.ir.annotations.Ignore;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)
@Ignore
public class BDMRepresentativeTest extends CuramServerTestJUnit4 {

  public BDMRepresentativeTest() {

    super();
  }

  @Before
  public void setUp() throws AppException, InformationalException {

  }

  /**
   * Search address details extension Junit
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testmodifyRepresentative()
    throws AppException, InformationalException {

    final BDMRepresentativeSummaryDetails details =
      new BDMRepresentativeSummaryDetails();
    final BDMEvidenceUtilsTest bdmEvidenceUtilsTest =
      new BDMEvidenceUtilsTest();
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();
    // final PersonRegistrationResult originalPerson1 =
    // registerPersonwithSIN("Original Person with SIN", "081975187");
    // final String dupName1 = "Duplicate Person1";

    final long concernRoleID = pdcPersonDetails.concernRoleID;
    final MaintainAddressKey addressKey1 = new MaintainAddressKey();
    addressKey1.concernRoleID = concernRoleID;
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = pdcPersonDetails.concernRoleID;
    details.dtls.representativeSummaryDetails.concernRoleID =
      pdcPersonDetails.concernRoleID;
    details.dtls.representativeSummaryDetails.representativeDtls.alternateID =
      "081975187";
    details.preferredLanguage = LANGUAGE.ENGLISH;
    details.dtls.representativeSummaryDetails.concernRoleID =
      pdcPersonDetails.concernRoleID;
    details.dtls.representativeSummaryDetails.representativeDtls.comments =
      "Merge Unit Test";
    details.dtls.representativeSummaryDetails.representativeDtls.concernRoleID =
      pdcPersonDetails.concernRoleID;
    details.dtls.representativeSummaryDetails.representativeDtls.currencyType =
      curam.codetable.CURRENCY.DEFAULTCODE;
    details.dtls.representativeSummaryDetails.representativeDtls.dateOfBirth =
      Date.getDate("19800101");
    details.dtls.representativeSummaryDetails.representativeDtls.methodOfPmtCode =
      METHODOFDELIVERY.CHEQUE;
    details.dtls.representativeSummaryDetails.representativeDtls.nextPaymentDate =
      Date.getDate("20200101");
    details.dtls.representativeSummaryDetails.representativeDtls.paymentFrequency =
      "100100100";
    details.dtls.representativeSummaryDetails.representativeDtls.representativeName =
      "john";
    details.dtls.representativeSummaryDetails.representativeDtls.upperRepresentativeName =
      "JOHN";
    details.dtls.representativeSummaryDetails.representativeDtls.representativeType =
      curam.codetable.REPRESENTATIVETYPE.CONTACT;
    details.dtls.representativeSummaryDetails.representativeSummaryDetails.registrationDate =
      curam.util.type.Date.getCurrentDate();
    details.dtls.representativeSummaryDetails.representativeSummaryDetails.concernRoleName =
      "CRA";
    details.dtls.representativeSummaryDetails.representativeSummaryDetails.prefCommMethod =
      "Digital copy only";

    details.preferredLanguage = LANGUAGE.ENGLISH;

    final BDMRepresentative bdmRepresentativeDataObj =
      BDMRepresentativeFactory.newInstance();
    InformationalMsgDetailsList informationalMsgDetailsList =
      new InformationalMsgDetailsList();
    informationalMsgDetailsList =
      bdmRepresentativeDataObj.modifyRepresentative(details);

    assertTrue(
      informationalMsgDetailsList.informationalMsgDtlsList.dtls.size() > 0);

  }

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
      curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;

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

  /**
   * Retrieves address details Ext Junit
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testviewRepresentativeHomePage()
    throws AppException, InformationalException {

    final RepresentativeKey key = new RepresentativeKey();
    final BDMEvidenceUtilsTest bdmEvidenceUtilsTest =
      new BDMEvidenceUtilsTest();
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();

    // final PersonRegistrationResult originalPerson1 =
    // registerPersonwithSIN("Original Person with SIN", "081975187");
    // final String dupName1 = "Duplicate Person1";

    final long concernRoleID = pdcPersonDetails.concernRoleID;
    final MaintainAddressKey addressKey1 = new MaintainAddressKey();
    addressKey1.concernRoleID = concernRoleID;
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = pdcPersonDetails.concernRoleID;

    key.representativeKey.representativeID = pdcPersonDetails.concernRoleID;
    concernRoleKey.concernRoleID = key.representativeKey.representativeID;
    final BDMRepresentative bdmRepresentativeDataObj =
      BDMRepresentativeFactory.newInstance();
    BDMRepresentativeHomePageDetails bdmHomePageDetails =
      new BDMRepresentativeHomePageDetails();
    // representativeKey.representativeKey = concernRoleKey.concernRoleID;

    bdmHomePageDetails.preferredLanguage = LANGUAGE.ENGLISH;

    bdmHomePageDetails.dtls.representativeDetails.phoneAreaCode = "123";
    bdmHomePageDetails.dtls.representativeDetails.phoneCountryCode =
      BDMPHONECOUNTRY.CANADA;
    bdmHomePageDetails.dtls.representativeDetails.phoneExtension = "";
    bdmHomePageDetails.dtls.representativeDetails.phoneNumber = "5551234";

    // assertEquals("1-31 CORONATION ST,ST. JOHN'S,NL,A1C 5B9",
    // bdmHomePageDetails.dtls.representativeDetails.formattedAddressData);
    bdmHomePageDetails =
      bdmRepresentativeDataObj.viewRepresentativeHomePage(key);
    assertTrue(
      bdmHomePageDetails.dtls.representativeContextDescriptionDetails.description
        .length() > 0);

  }

}
