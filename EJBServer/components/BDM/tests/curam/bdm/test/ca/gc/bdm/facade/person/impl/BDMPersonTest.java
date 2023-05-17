package curam.bdm.test.ca.gc.bdm.facade.person.impl;

import curam.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.entity.communication.fact.BDMConcernRoleCommunicationFactory;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationDtls;
import curam.ca.gc.bdm.entity.communication.struct.BDMSearchByTrackingNumberKey;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchResultByTrackingNum;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchResultByTrackingNumList;
import curam.ca.gc.bdm.facade.fec.fact.BDMForeignEngagementCaseFactory;
import curam.ca.gc.bdm.facade.fec.struct.BDMFECaseDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMPersonRegistrationDetails;
import curam.ca.gc.bdm.facade.person.fact.BDMPersonFactory;
import curam.ca.gc.bdm.facade.person.intf.BDMPerson;
import curam.ca.gc.bdm.facade.person.struct.BDMPersonSearchDetailsResult;
import curam.ca.gc.bdm.facade.person.struct.BDMPersonSearchKey1;
import curam.ca.gc.bdm.sl.communication.fact.BDMCommunicationFactory;
import curam.codetable.CASEPRIORITY;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.CORRESPONDENT;
import curam.codetable.PROVINCETYPE;
import curam.core.facade.fact.PersonFactory;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.facade.struct.CreateIntegratedCaseResultAndMessages;
import curam.core.facade.struct.PersonRegistrationDetails;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.facade.struct.PersonSearchDetailsResult;
import curam.core.facade.struct.PersonSearchKey1;
import curam.core.sl.struct.RecordedCommDetails1;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.ConcernRoleCommKeyOut;
import curam.core.struct.OtherAddressData;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)
public class BDMPersonTest extends CuramServerTestJUnit4 {

  @Mocked
  curam.ca.gc.bdm.entity.person.fact.BDMPersonFactory personFactory;

  private static String ERROR_ADDITIONAL_SERACH_CRITERIIA =
    "Search cannot be completed - Additional search criteria required.";

  BDMPersonSearchResultByTrackingNumList bdmPersonSearchResultByTrackingNumList =
    null;

  @Before
  public void setUp() throws AppException, InformationalException {

    bdmPersonSearchResultByTrackingNumList =
      new BDMPersonSearchResultByTrackingNumList();

  }

  /*
   * Dummy call to BDMPersonFactory - SearchByTrackingNumber()
   *
   * @throws AppException
   *
   * @throws InformationalException
   */

  private void expectationSearchByTrackingNumber(final long concernRoleID,
    final String alternateID) throws AppException, InformationalException {

    new Expectations() {

      {
        curam.ca.gc.bdm.entity.person.fact.BDMPersonFactory.newInstance()
          .searchByTrackingNumber((BDMSearchByTrackingNumberKey) any);
        final BDMPersonSearchResultByTrackingNum resultNum =
          new BDMPersonSearchResultByTrackingNum();
        resultNum.concernRoleID = concernRoleID;
        resultNum.primaryAlternateID = alternateID;

        bdmPersonSearchResultByTrackingNumList.dtls.add(resultNum);

        result = bdmPersonSearchResultByTrackingNumList;
      }
    };
  }

  /**
   * 8914 person search extension Junit
   * Display erroe when additional search criteria not provided
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSearchPerson_additionCriteria()
    throws AppException, InformationalException {

    final PersonSearchKey1 personSearchKey1 = new PersonSearchKey1();
    personSearchKey1.personSearchKey.surname = "Cruise";

    personSearchKey1.personSearchKey.forename = "Tom";
    // bdmPersonSearchKey1.postalCode = "K5";

    final PersonRegistrationResult person = registerPersonwithSIN();

    final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();

    try {
      // bdmPersonSearchDetailsResult =
      bdmPersonObj.searchPerson(personSearchKey1);
    } catch (final Exception e) {
      assertEquals(
        "There are no matching items based on the Search Criteria entered.",
        e.getLocalizedMessage());
    }

  }

  /**
   * person search OOTB Junit
   * Successfully return person record when firstname , lastname DOB provided
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSearchPerson_additionCriteria2()
    throws AppException, InformationalException {

    final PersonSearchKey1 personSearchKey1 = new PersonSearchKey1();
    personSearchKey1.personSearchKey.surname = "Test";
    personSearchKey1.personSearchKey.forename = "Test";
    personSearchKey1.personSearchKey.addressDtls.addressLine1 = "Cruise Ave";

    final PersonRegistrationResult person = registerPersonwithSIN();

    final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();

    PersonSearchDetailsResult result = new PersonSearchDetailsResult();

    result = bdmPersonObj.searchPerson(personSearchKey1);

    assertEquals(person.registrationIDDetails.concernRoleID,
      result.personSearchResult.dtlsList.get(0).concernRoleID);

  }

  /**
   *
   * Display serach result when additional criteria is provided
   ** Successfully return person record when firstname , lastname DOB provided
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSearchPersonExt()
    throws AppException, InformationalException {

    // Register Person
    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "Tom";
    bdmPersonRegistrationDetails.dtls.surname = "Maverick";
    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19770101");
    bdmPersonRegistrationDetails.dtls.socialSecurityNumber = "259932895";
    bdmPersonRegistrationDetails.dtls.alternateIDTypeCodeOpt =
      CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER;

    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;

    addressFieldDetails.suiteNum = "5789";
    addressFieldDetails.addressLine1 = "Cruise Ave";
    addressFieldDetails.addressLine2 = "Train street";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Ontario";
    addressFieldDetails.postalCode = "K5M 1G7";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;

    OtherAddressData otherAddressData = null;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    bdmPersonRegistrationDetails.dtls.addressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.mailingAddressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.registrationDate =
      Date.getCurrentDate();

    final PersonRegistrationDetails details = new PersonRegistrationDetails();
    details.personRegistrationDetails = bdmPersonRegistrationDetails.dtls;

    final PersonRegistrationResult registrationResult =
      PersonFactory.newInstance().register(details);

    // Define search with first name , last name and DOB
    final BDMPersonSearchKey1 bdmPersonSearchKey1 = new BDMPersonSearchKey1();
    bdmPersonSearchKey1.dtls.personSearchKey.surname = "Maverick";
    bdmPersonSearchKey1.dtls.personSearchKey.forename = "Tom";
    bdmPersonSearchKey1.dtls.personSearchKey.dateOfBirth =
      Date.getDate("19770101");

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();

    final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();

    // Call facade method
    bdmPersonSearchDetailsResult =
      bdmPersonObj.searchPersonExt(bdmPersonSearchKey1);
    assertTrue(
      bdmPersonSearchDetailsResult.personSearchResult.dtlsList.size() > 0);

    assertEquals("Tom",
      bdmPersonSearchDetailsResult.personSearchResult.dtlsList
        .get(0).firstForename);
    assertEquals("Maverick",
      bdmPersonSearchDetailsResult.personSearchResult.dtlsList
        .get(0).lastName);
    assertEquals("Tom",
      bdmPersonSearchDetailsResult.personSearchResult.dtlsList
        .get(0).firstForename);
    assertEquals("K5M 1G7",
      bdmPersonSearchDetailsResult.personSearchResult.dtlsList
        .get(0).postalOrZipCode);

  }

  /**
   * 8914 person search extension Junit
   * Display search result based on corresponding tracking number
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSearchPersonByTrackingNumber()
    throws AppException, InformationalException {

    // Register Person
    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "Tom";
    bdmPersonRegistrationDetails.dtls.surname = "Maverick";
    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19770101");
    bdmPersonRegistrationDetails.dtls.socialSecurityNumber = "615569589";
    bdmPersonRegistrationDetails.dtls.alternateIDTypeCodeOpt =
      CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER;

    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;

    addressFieldDetails.suiteNum = "5789";
    addressFieldDetails.addressLine1 = "Cruise Ave";
    addressFieldDetails.addressLine2 = "Train street";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Ontario";
    addressFieldDetails.postalCode = "K5M 1G7";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;

    OtherAddressData otherAddressData = null;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    bdmPersonRegistrationDetails.dtls.addressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.mailingAddressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.registrationDate =
      Date.getCurrentDate();

    final PersonRegistrationDetails details = new PersonRegistrationDetails();
    details.personRegistrationDetails = bdmPersonRegistrationDetails.dtls;

    // Register a person
    final PersonRegistrationResult registrationResult =
      PersonFactory.newInstance().register(details);

    // Create FEC case
    final CreateIntegratedCaseResultAndMessages createCase =
      createFEC(registrationResult.registrationIDDetails.concernRoleID);

    // Created recorded CXommunication with escaltion level
    final ConcernRoleCommKeyOut commID =
      createRecordedCommunicationwithEscalation(
        createCase.createCaseResult.integratedCaseID, "Tom Maverick",
        registrationResult.registrationIDDetails.concernRoleID);

    createBDMConcernRoleCommunication(commID.communicationID);

    // Define search with first name , last name and DOB
    final BDMPersonSearchKey1 bdmPersonSearchKey1 = new BDMPersonSearchKey1();
    bdmPersonSearchKey1.corrTrackingNumber = "1234";

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();

    final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();

    expectationSearchByTrackingNumber(
      registrationResult.registrationIDDetails.concernRoleID,
      registrationResult.registrationIDDetails.alternateID);

    // Call facade method
    bdmPersonSearchDetailsResult =
      bdmPersonObj.searchPersonExt(bdmPersonSearchKey1);
    assertTrue(
      !bdmPersonSearchDetailsResult.personSearchResult.dtlsList.isEmpty());

    assertEquals("Tom",
      bdmPersonSearchDetailsResult.personSearchResult.dtlsList
        .get(0).firstForename);
    assertEquals("Maverick",
      bdmPersonSearchDetailsResult.personSearchResult.dtlsList
        .get(0).lastName);
    assertEquals("Tom",
      bdmPersonSearchDetailsResult.personSearchResult.dtlsList
        .get(0).firstForename);
    assertEquals("K5M 1G7",
      bdmPersonSearchDetailsResult.personSearchResult.dtlsList
        .get(0).postalOrZipCode);

  }

  /**
   * Search perosn by Correct tracking number and correct reference should
   * display person result
   */

  @Test
  public void testSearchPersonExt_correctTrackingNumberAndReferenceNumber()
    throws AppException, InformationalException {

    // Person Details
    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "Jane";
    bdmPersonRegistrationDetails.dtls.surname = "Doe";
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

    final PersonRegistrationResult registrationResult =
      PersonFactory.newInstance().register(details);

    // Create FEC case
    final CreateIntegratedCaseResultAndMessages createCase =
      createFEC(registrationResult.registrationIDDetails.concernRoleID);

    // Created recorded CXommunication
    final ConcernRoleCommKeyOut commID =
      createRecordedCommunicationwithEscalation(
        createCase.createCaseResult.integratedCaseID, "Jane Doe",
        registrationResult.registrationIDDetails.concernRoleID);

    // Create BDM concernRole Communication with Tracking number
    createBDMConcernRoleCommunication(commID.communicationID);

    expectationSearchByTrackingNumber(
      registrationResult.registrationIDDetails.concernRoleID,
      registrationResult.registrationIDDetails.alternateID);

    // create Search criteris with tracking number only
    final BDMPersonSearchKey1 bdmPersonSearchKey1 = new BDMPersonSearchKey1();
    bdmPersonSearchKey1.corrTrackingNumber = "1234";
    bdmPersonSearchKey1.dtls.personSearchKey.referenceNumber =
      registrationResult.registrationIDDetails.alternateID;

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();

    // Assert no records are found with given criteria
    final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();
    bdmPersonSearchDetailsResult =
      bdmPersonObj.searchPersonExt(bdmPersonSearchKey1);

    assertTrue(
      !bdmPersonSearchDetailsResult.personSearchResult.dtlsList.isEmpty());
    assertEquals(bdmPersonSearchDetailsResult.personSearchResult.dtlsList
      .get(0).firstForename, "Jane");
    assertEquals(bdmPersonSearchDetailsResult.personSearchResult.dtlsList
      .get(0).lastName, "Doe");

    assertEquals(bdmPersonSearchDetailsResult.personSearchResult.dtlsList
      .get(0).postalOrZipCode, "T6K 3M1");

  }

  /**
   * Search perosn by Correct tracking number and Incorrect reference should not
   * display person result
   */

  @Test
  public void testSearchPersonExt_trackingNumberAndReferenceNumber()
    throws AppException, InformationalException {

    // Person Details
    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "Jane";
    bdmPersonRegistrationDetails.dtls.surname = "Doe";
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

    final PersonRegistrationResult registrationResult =
      PersonFactory.newInstance().register(details);

    // Register perosn 2 with SIN number
    final PersonRegistrationResult registrationResult2 =
      registerPersonwithSIN();

    // Create FEC case
    final CreateIntegratedCaseResultAndMessages createCase =
      createFEC(registrationResult.registrationIDDetails.concernRoleID);

    // Created recorded CXommunication
    final ConcernRoleCommKeyOut commID =
      createRecordedCommunicationwithEscalation(
        createCase.createCaseResult.integratedCaseID, "Jane Doe",
        registrationResult.registrationIDDetails.concernRoleID);

    // Create BDM concernRole Communication with Tracking number
    createBDMConcernRoleCommunication(commID.communicationID);

    expectationSearchByTrackingNumber(
      registrationResult.registrationIDDetails.concernRoleID,
      registrationResult.registrationIDDetails.alternateID);

    // create Search criteris with tracking number only
    final BDMPersonSearchKey1 bdmPersonSearchKey1 = new BDMPersonSearchKey1();
    bdmPersonSearchKey1.corrTrackingNumber = "1234";
    bdmPersonSearchKey1.dtls.personSearchKey.referenceNumber =
      registrationResult2.registrationIDDetails.alternateID;

    final BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();

    final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();

    try {
      // bdmPersonSearchDetailsResult =
      bdmPersonObj.searchPersonExt(bdmPersonSearchKey1);
    } catch (final Exception ex) {
      assertTrue(
        bdmPersonSearchDetailsResult.personSearchResult.dtlsList.isEmpty());
    }

  }

  /**
   * Invalid serach search criteria provided
   */

  @Test
  public void testSearchPersonExt_invalidSerarchCriteria()
    throws AppException, InformationalException {

    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = "Save";

    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "Tom";
    bdmPersonRegistrationDetails.dtls.surname = "Cruise";
    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19780101");

    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;

    addressFieldDetails.suiteNum = "123";
    addressFieldDetails.addressLine1 = "Cruise Ave";
    addressFieldDetails.addressLine2 = "Train street";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Ontario";
    addressFieldDetails.postalCode = "K5M 1G7";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;

    OtherAddressData otherAddressData = null;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    bdmPersonRegistrationDetails.dtls.addressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.mailingAddressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.registrationDate =
      Date.getCurrentDate();

    final PersonRegistrationDetails details = new PersonRegistrationDetails();
    details.personRegistrationDetails = bdmPersonRegistrationDetails.dtls;

    final PersonRegistrationResult registrationResult =
      PersonFactory.newInstance().register(details);

    assertTrue(
      registrationResult.registrationIDDetails.alternateID.length() > 0);

    final BDMPersonSearchKey1 bdmPersonSearchKey1 = new BDMPersonSearchKey1();
    bdmPersonSearchKey1.postalCode = "K5M 1G7";

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();

    boolean caught = false;
    final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();
    try {
      bdmPersonSearchDetailsResult =
        bdmPersonObj.searchPersonExt(bdmPersonSearchKey1);
    } catch (final Exception e) {
      caught = true;

    }

    assertTrue(caught);
  }

  /**
   * Display no results when firstname , lastname and partial postalcode is
   * provided
   * Invalid serach search criteria provided
   */

  @Test
  public void testSearchPersonExt_invalidSerarchCriteria2()
    throws AppException, InformationalException {

    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = "Save";

    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "Tom";
    bdmPersonRegistrationDetails.dtls.surname = "Cruise";
    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19780101");

    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;

    addressFieldDetails.suiteNum = "123";
    addressFieldDetails.addressLine1 = "Cruise Ave";
    addressFieldDetails.addressLine2 = "Train street";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Ontario";
    addressFieldDetails.postalCode = "K5M 1G7";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;

    OtherAddressData otherAddressData = null;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    bdmPersonRegistrationDetails.dtls.addressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.mailingAddressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.registrationDate =
      Date.getCurrentDate();

    final PersonRegistrationDetails details = new PersonRegistrationDetails();
    details.personRegistrationDetails = bdmPersonRegistrationDetails.dtls;

    final PersonRegistrationResult registrationResult =
      PersonFactory.newInstance().register(details);

    assertTrue(
      registrationResult.registrationIDDetails.alternateID.length() > 0);

    final BDMPersonSearchKey1 bdmPersonSearchKey1 = new BDMPersonSearchKey1();
    bdmPersonSearchKey1.postalCode = "K5";
    bdmPersonSearchKey1.dtls.personSearchKey.forename = "Tom";
    bdmPersonSearchKey1.dtls.personSearchKey.surname = "Cruise";

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();

    boolean caught = false;
    final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();
    try {
      bdmPersonSearchDetailsResult =
        bdmPersonObj.searchPersonExt(bdmPersonSearchKey1);
    } catch (final Exception e) {
      caught = true;

    }

    assertTrue(caught);
  }

  /** City code */
  @Test
  public void testSearchPerson_invalidSerarchCriteria_City()
    throws AppException, InformationalException {

    final BDMPersonSearchKey1 bdmPersonSearchKey1 = new BDMPersonSearchKey1();
    bdmPersonSearchKey1.dtls.personSearchKey.addressDtls.city = "O";
    bdmPersonSearchKey1.dtls.personSearchKey.forename = "Tom";
    bdmPersonSearchKey1.dtls.personSearchKey.surname = "Cruise";

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();

    boolean caught = false;
    final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();
    try {
      bdmPersonSearchDetailsResult =
        bdmPersonObj.searchPersonExt(bdmPersonSearchKey1);
    } catch (final Exception e) {
      caught = true;

    }

    assertTrue(caught);
  }

  /**
   * Search perosn by Correct tracking number and correct reference should
   * display person result
   */

  @Test
  public void
    testSearchPersonForPopupExt_correctTrackingNumberAndReferenceNumber()
      throws AppException, InformationalException {

    // Person Details
    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "Jane";
    bdmPersonRegistrationDetails.dtls.surname = "Doe";
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

    final PersonRegistrationResult registrationResult =
      PersonFactory.newInstance().register(details);

    // Create FEC case
    final CreateIntegratedCaseResultAndMessages createCase =
      createFEC(registrationResult.registrationIDDetails.concernRoleID);

    // Created recorded CXommunication
    final ConcernRoleCommKeyOut commID =
      createRecordedCommunicationwithEscalation(
        createCase.createCaseResult.integratedCaseID, "Jane Doe",
        registrationResult.registrationIDDetails.concernRoleID);

    // Create BDM concernRole Communication with Tracking number
    createBDMConcernRoleCommunication(commID.communicationID);

    expectationSearchByTrackingNumber(
      registrationResult.registrationIDDetails.concernRoleID,
      registrationResult.registrationIDDetails.alternateID);

    // create Search criteris with tracking number only
    final BDMPersonSearchKey1 bdmPersonSearchKey1 = new BDMPersonSearchKey1();
    bdmPersonSearchKey1.corrTrackingNumber = "1234";
    bdmPersonSearchKey1.dtls.personSearchKey.referenceNumber =
      registrationResult.registrationIDDetails.alternateID;

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();

    // Assert no records are found with given criteria
    final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();
    bdmPersonSearchDetailsResult =
      bdmPersonObj.searchPersonForPopupExt(bdmPersonSearchKey1);

    assertTrue(
      !bdmPersonSearchDetailsResult.personSearchResult.dtlsList.isEmpty());
    assertEquals(bdmPersonSearchDetailsResult.personSearchResult.dtlsList
      .get(0).firstForename, "Jane");
    assertEquals(bdmPersonSearchDetailsResult.personSearchResult.dtlsList
      .get(0).lastName, "Doe");

    assertEquals(bdmPersonSearchDetailsResult.personSearchResult.dtlsList
      .get(0).postalOrZipCode, "T6K 3M1");

  }

  /**
   * Search perosn by Correct tracking number and Incorrect reference should not
   * display person result
   */

  @Test
  public void testSearchPersonForPopupExt_trackingNumberAndReferenceNumber()
    throws AppException, InformationalException {

    // Person Details
    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "Jane";
    bdmPersonRegistrationDetails.dtls.surname = "Doe";
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

    final PersonRegistrationResult registrationResult =
      PersonFactory.newInstance().register(details);

    // Register perosn 2 with SIN number
    final PersonRegistrationResult registrationResult2 =
      registerPersonwithSIN();

    // Create FEC case
    final CreateIntegratedCaseResultAndMessages createCase =
      createFEC(registrationResult.registrationIDDetails.concernRoleID);

    // Created recorded CXommunication
    final ConcernRoleCommKeyOut commID =
      createRecordedCommunicationwithEscalation(
        createCase.createCaseResult.integratedCaseID, "Jane Doe",
        registrationResult.registrationIDDetails.concernRoleID);

    // Create BDM concernRole Communication with Tracking number
    createBDMConcernRoleCommunication(commID.communicationID);

    expectationSearchByTrackingNumber(
      registrationResult.registrationIDDetails.concernRoleID,
      registrationResult.registrationIDDetails.alternateID);

    // create Search criteris with tracking number only
    final BDMPersonSearchKey1 bdmPersonSearchKey1 = new BDMPersonSearchKey1();
    bdmPersonSearchKey1.corrTrackingNumber = "1234";
    bdmPersonSearchKey1.dtls.personSearchKey.referenceNumber =
      registrationResult2.registrationIDDetails.alternateID;

    final BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();

    final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();

    try {

      bdmPersonObj.searchPersonForPopupExt(bdmPersonSearchKey1);
    } catch (final Exception ex) {
      assertTrue(
        bdmPersonSearchDetailsResult.personSearchResult.dtlsList.isEmpty());
    }

  }

  private void createBDMConcernRoleCommunication(final long communicationID)
    throws AppException, InformationalException {

    final BDMConcernRoleCommunicationDtls bdmConcernRoleCommunicationDtls =
      new BDMConcernRoleCommunicationDtls();
    bdmConcernRoleCommunicationDtls.submittedInd = true;
    bdmConcernRoleCommunicationDtls.digitalInd = true;
    bdmConcernRoleCommunicationDtls.trackingNumber = 1234;
    bdmConcernRoleCommunicationDtls.communicationID = communicationID;
    BDMConcernRoleCommunicationFactory.newInstance()
      .insert(bdmConcernRoleCommunicationDtls);

  }

  /**
   * register and person search Ext Junit
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSearchPersonExtRegister()
    throws AppException, InformationalException {

    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = "Save";

    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "Tom";
    bdmPersonRegistrationDetails.dtls.surname = "Maverick";
    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19770101");
    bdmPersonRegistrationDetails.dtls.socialSecurityNumber = "245577226";
    bdmPersonRegistrationDetails.dtls.alternateIDTypeCodeOpt =
      CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER;

    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;

    addressFieldDetails.suiteNum = "5789";
    addressFieldDetails.addressLine1 = "Cruise Ave";
    addressFieldDetails.addressLine2 = "Train street";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Ontario";
    addressFieldDetails.postalCode = "K5M 1G7";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;

    OtherAddressData otherAddressData = null;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    bdmPersonRegistrationDetails.dtls.addressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.mailingAddressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.registrationDate =
      Date.getCurrentDate();

    final PersonRegistrationDetails details = new PersonRegistrationDetails();
    details.personRegistrationDetails = bdmPersonRegistrationDetails.dtls;

    final PersonRegistrationResult registrationResult =
      PersonFactory.newInstance().register(details);

    assertTrue(
      registrationResult.registrationIDDetails.alternateID.length() > 0);

    final BDMPersonSearchKey1 bdmPersonSearchKey1 = new BDMPersonSearchKey1();
    bdmPersonSearchKey1.dtls.personSearchKey.surname = "Maverick";
    bdmPersonSearchKey1.dtls.personSearchKey.forename = "Tom";
    bdmPersonSearchKey1.postalCode = "K5M 1G7";
    bdmPersonSearchKey1.dtls.personSearchKey.dateOfBirth =
      Date.getDate("19770101");

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();

    final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();

    bdmPersonSearchDetailsResult =
      bdmPersonObj.searchPersonExt(bdmPersonSearchKey1);

    assertTrue(
      bdmPersonSearchDetailsResult.personSearchResult.dtlsList.size() == 1);
    assertEquals(bdmPersonSearchDetailsResult.personSearchResult.dtlsList
      .get(0).firstForename, "Tom");
    assertEquals(bdmPersonSearchDetailsResult.personSearchResult.dtlsList
      .get(0).lastName, "Maverick");
    assertTrue(bdmPersonSearchDetailsResult.personSearchResult.dtlsList
      .get(0).sinNumber.contains("*"));

  }

  /**
   * 8914 register person and search Junit
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSearchPersonForPopupExt()
    throws AppException, InformationalException {

    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = "Save";

    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "Tom";
    bdmPersonRegistrationDetails.dtls.surname = "Cruise";
    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19780101");
    // bdmPersonRegistrationDetails.dtls.socialSecurityNumber = "295744858";
    // bdmPersonRegistrationDetails.dtls.alternateIDTypeCodeOpt =
    // CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER;

    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;

    addressFieldDetails.suiteNum = "123";
    addressFieldDetails.addressLine1 = "Cruise Ave";
    addressFieldDetails.addressLine2 = "Train street";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Ontario";
    addressFieldDetails.postalCode = "K5M 1G7";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;

    OtherAddressData otherAddressData = null;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    bdmPersonRegistrationDetails.dtls.addressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.mailingAddressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.registrationDate =
      Date.getCurrentDate();

    final PersonRegistrationDetails details = new PersonRegistrationDetails();
    details.personRegistrationDetails = bdmPersonRegistrationDetails.dtls;

    final PersonRegistrationResult registrationResult =
      PersonFactory.newInstance().register(details);

    assertTrue(
      registrationResult.registrationIDDetails.alternateID.length() > 0);

    final BDMPersonSearchKey1 bdmPersonSearchKey1 = new BDMPersonSearchKey1();
    bdmPersonSearchKey1.dtls.personSearchKey.surname = "Cruise";
    bdmPersonSearchKey1.dtls.personSearchKey.forename = "Tom";
    bdmPersonSearchKey1.postalCode = "K5M 1G7";

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();

    final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();

    bdmPersonSearchDetailsResult =
      bdmPersonObj.searchPersonForPopupExt(bdmPersonSearchKey1);

    assertTrue(
      bdmPersonSearchDetailsResult.personSearchResult.dtlsList.size() == 1);
    assertEquals(bdmPersonSearchDetailsResult.personSearchResult.dtlsList
      .get(0).firstForename, "Tom");
    assertEquals(bdmPersonSearchDetailsResult.personSearchResult.dtlsList
      .get(0).lastName, "Cruise");

  }

  /**
   * 8914 register person and search Junit
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSearchPersonForPopupExt_ByTrackingNumberonly()
    throws AppException, InformationalException {

    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = "Save";

    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "Tom";
    bdmPersonRegistrationDetails.dtls.surname = "Cruise";
    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19780101");

    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;

    addressFieldDetails.suiteNum = "123";
    addressFieldDetails.addressLine1 = "Cruise Ave";
    addressFieldDetails.addressLine2 = "Train street";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Ontario";
    addressFieldDetails.postalCode = "K5M 1G7";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;

    OtherAddressData otherAddressData = null;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    bdmPersonRegistrationDetails.dtls.addressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.mailingAddressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.registrationDate =
      Date.getCurrentDate();

    final PersonRegistrationDetails details = new PersonRegistrationDetails();
    details.personRegistrationDetails = bdmPersonRegistrationDetails.dtls;

    // Register a person
    final PersonRegistrationResult registrationResult =
      PersonFactory.newInstance().register(details);

    // Create FEC case
    final CreateIntegratedCaseResultAndMessages createCase =
      createFEC(registrationResult.registrationIDDetails.concernRoleID);

    // Created recorded CXommunication with escaltion level
    final ConcernRoleCommKeyOut commID =
      createRecordedCommunicationwithEscalation(
        createCase.createCaseResult.integratedCaseID, "Tom Cruise",
        registrationResult.registrationIDDetails.concernRoleID);

    createBDMConcernRoleCommunication(commID.communicationID);

    // Define search with first name , last name and DOB
    final BDMPersonSearchKey1 bdmPersonSearchKey1 = new BDMPersonSearchKey1();
    bdmPersonSearchKey1.corrTrackingNumber = "1234";

    expectationSearchByTrackingNumber(
      registrationResult.registrationIDDetails.concernRoleID,
      registrationResult.registrationIDDetails.alternateID);

    assertTrue(
      registrationResult.registrationIDDetails.alternateID.length() > 0);

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();

    final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();

    bdmPersonSearchDetailsResult =
      bdmPersonObj.searchPersonForPopupExt(bdmPersonSearchKey1);

    assertTrue(
      bdmPersonSearchDetailsResult.personSearchResult.dtlsList.size() == 1);
    assertEquals(bdmPersonSearchDetailsResult.personSearchResult.dtlsList
      .get(0).firstForename, "Tom");
    assertEquals(bdmPersonSearchDetailsResult.personSearchResult.dtlsList
      .get(0).lastName, "Cruise");

  }

  @Test
  public void testSearchPersonForPopupExt_invalidSerarchCriteria()
    throws AppException, InformationalException {

    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = "Save";

    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "Tom";
    bdmPersonRegistrationDetails.dtls.surname = "Cruise";
    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19780101");

    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;

    addressFieldDetails.suiteNum = "123";
    addressFieldDetails.addressLine1 = "Cruise Ave";
    addressFieldDetails.addressLine2 = "Train street";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Ontario";
    addressFieldDetails.postalCode = "K5M 1G7";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;

    OtherAddressData otherAddressData = null;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    bdmPersonRegistrationDetails.dtls.addressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.mailingAddressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.registrationDate =
      Date.getCurrentDate();

    final PersonRegistrationDetails details = new PersonRegistrationDetails();
    details.personRegistrationDetails = bdmPersonRegistrationDetails.dtls;

    final PersonRegistrationResult registrationResult =
      PersonFactory.newInstance().register(details);

    assertTrue(
      registrationResult.registrationIDDetails.alternateID.length() > 0);

    final BDMPersonSearchKey1 bdmPersonSearchKey1 = new BDMPersonSearchKey1();
    // bdmPersonSearchKey1.postalCode = "K5M 1G7";

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();

    boolean caught = false;
    final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();
    try {
      bdmPersonSearchDetailsResult =
        bdmPersonObj.searchPersonForPopupExt(bdmPersonSearchKey1);
    } catch (final Exception e) {
      caught = true;

    }

    assertTrue(caught);
  }

  /** POstal code */
  @Test
  public void testSearchPersonForPopupExt_invalidSerarchCriteria2()
    throws AppException, InformationalException {

    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = "Save";

    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "Tom";
    bdmPersonRegistrationDetails.dtls.surname = "Cruise";
    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19780101");

    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;

    addressFieldDetails.suiteNum = "123";
    addressFieldDetails.addressLine1 = "Cruise Ave";
    addressFieldDetails.addressLine2 = "Train street";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Ontario";
    addressFieldDetails.postalCode = "K5M 1G7";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;

    OtherAddressData otherAddressData = null;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    bdmPersonRegistrationDetails.dtls.addressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.mailingAddressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.registrationDate =
      Date.getCurrentDate();

    final PersonRegistrationDetails details = new PersonRegistrationDetails();
    details.personRegistrationDetails = bdmPersonRegistrationDetails.dtls;

    final PersonRegistrationResult registrationResult =
      PersonFactory.newInstance().register(details);

    assertTrue(
      registrationResult.registrationIDDetails.alternateID.length() > 0);

    final BDMPersonSearchKey1 bdmPersonSearchKey1 = new BDMPersonSearchKey1();
    bdmPersonSearchKey1.postalCode = "K5";
    bdmPersonSearchKey1.dtls.personSearchKey.forename = "Tom";
    bdmPersonSearchKey1.dtls.personSearchKey.surname = "Cruise";

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();

    boolean caught = false;
    final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();
    try {
      bdmPersonSearchDetailsResult =
        bdmPersonObj.searchPersonForPopupExt(bdmPersonSearchKey1);
    } catch (final Exception e) {
      caught = true;

    }

    assertTrue(caught);
  }

  /** City code */
  @Test
  public void testSearchPersonForPopupExt_invalidSerarchCriteria_City()
    throws AppException, InformationalException {

    final BDMPersonSearchKey1 bdmPersonSearchKey1 = new BDMPersonSearchKey1();
    bdmPersonSearchKey1.dtls.personSearchKey.addressDtls.city = "O";
    bdmPersonSearchKey1.dtls.personSearchKey.forename = "Tom";
    bdmPersonSearchKey1.dtls.personSearchKey.surname = "Cruise";

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();

    boolean caught = false;
    final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();
    try {
      bdmPersonSearchDetailsResult =
        bdmPersonObj.searchPersonForPopupExt(bdmPersonSearchKey1);
    } catch (final Exception e) {
      caught = true;

    }

    assertTrue(caught);
  }

  /** State code */
  @Test
  public void testSearchPersonForPopupExt_invalidSerarchCriteria_State()
    throws AppException, InformationalException {

    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = "Save";

    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "Tom";
    bdmPersonRegistrationDetails.dtls.surname = "Cruise";
    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19780101");

    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;

    addressFieldDetails.suiteNum = "123";
    addressFieldDetails.addressLine1 = "Cruise Ave";
    addressFieldDetails.addressLine2 = "Train street";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Ontario";
    addressFieldDetails.postalCode = "K5M 1G7";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;

    OtherAddressData otherAddressData = null;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    bdmPersonRegistrationDetails.dtls.addressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.mailingAddressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.registrationDate =
      Date.getCurrentDate();

    final PersonRegistrationDetails details = new PersonRegistrationDetails();
    details.personRegistrationDetails = bdmPersonRegistrationDetails.dtls;

    final PersonRegistrationResult registrationResult =
      PersonFactory.newInstance().register(details);

    assertTrue(
      registrationResult.registrationIDDetails.alternateID.length() > 0);

    final BDMPersonSearchKey1 bdmPersonSearchKey1 = new BDMPersonSearchKey1();
    bdmPersonSearchKey1.stateProvince = "On";
    bdmPersonSearchKey1.dtls.personSearchKey.forename = "Tom";
    bdmPersonSearchKey1.dtls.personSearchKey.surname = "Cruise";

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();

    boolean caught = false;
    final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();
    try {
      bdmPersonSearchDetailsResult =
        bdmPersonObj.searchPersonForPopupExt(bdmPersonSearchKey1);
    } catch (final Exception e) {
      caught = true;

    }
    // First Name and Last Name require at least 2 characters.Search cannot be
    // completed - Additional search criteria required.

    assertTrue(caught);
  }

  /** State code */
  @Test
  public void testSearchPersonExt_invalidSerarchCriteria_State()
    throws AppException, InformationalException {

    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = "Save";

    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "Tom";
    bdmPersonRegistrationDetails.dtls.surname = "Cruise";
    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19780101");

    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;

    addressFieldDetails.suiteNum = "123";
    addressFieldDetails.addressLine1 = "Cruise Ave";
    addressFieldDetails.addressLine2 = "Train street";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Ontario";
    addressFieldDetails.postalCode = "K5M 1G7";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;

    OtherAddressData otherAddressData = null;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    bdmPersonRegistrationDetails.dtls.addressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.mailingAddressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.registrationDate =
      Date.getCurrentDate();

    final PersonRegistrationDetails details = new PersonRegistrationDetails();
    details.personRegistrationDetails = bdmPersonRegistrationDetails.dtls;

    final PersonRegistrationResult registrationResult =
      PersonFactory.newInstance().register(details);

    assertTrue(
      registrationResult.registrationIDDetails.alternateID.length() > 0);

    final BDMPersonSearchKey1 bdmPersonSearchKey1 = new BDMPersonSearchKey1();
    bdmPersonSearchKey1.stateProvince = "On";
    bdmPersonSearchKey1.dtls.personSearchKey.forename = "Tom";
    bdmPersonSearchKey1.dtls.personSearchKey.surname = "Cruise";

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();

    boolean caught = false;
    final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();
    try {
      bdmPersonSearchDetailsResult =
        bdmPersonObj.searchPersonExt(bdmPersonSearchKey1);
    } catch (final Exception e) {
      caught = true;

    }

    assertTrue(caught);
  }

  /*** Create FEc case for person */
  protected CreateIntegratedCaseResultAndMessages createFEC(
    final long concernRoleID) throws AppException, InformationalException {

    final BDMFECaseDetails details = new BDMFECaseDetails();
    details.concernRoleID = concernRoleID;

    details.countryCode = BDMSOURCECOUNTRY.IRELAND;

    details.priorityCode = CASEPRIORITY.HIGH;

    return BDMForeignEngagementCaseFactory.newInstance()
      .createFEIntegratedCase(details);

  }

  /** Create recorded Communication with escaltion level */
  ConcernRoleCommKeyOut createRecordedCommunicationwithEscalation(
    final long caseID, final String concernRoleName, final long concernRoleID)
    throws AppException, InformationalException {

    final RecordedCommDetails1 recordedCommDetails =
      new RecordedCommDetails1();

    recordedCommDetails.addressData =
      "1\n0\nBDMINTL\nUS\n1\n0\nZIP=\nBDMSTPROVX=\nCITY=\nCOUNTRY=\nPOBOXNO=\nAPT=\nPOSTCODE=\nBDMZIPX=\nSTATEPROV=\nPROV=\nADD1=\nADD2=\n";
    recordedCommDetails.caseID = caseID;
    recordedCommDetails.communicationDirection = "CD2";
    recordedCommDetails.communicationText = "Test communication";
    recordedCommDetails.communicationTypeCode = "CT8007"; // requires attendtion
    recordedCommDetails.correspondentName = concernRoleName;
    recordedCommDetails.correspondentParticipantRoleID = concernRoleID;
    recordedCommDetails.correspondentType = CORRESPONDENT.CLIENT;
    recordedCommDetails.methodTypeCode = "CM1";
    recordedCommDetails.subject = "Test comm 2";
    recordedCommDetails.communicationFormat = "CF4"; // recorded Communication

    return BDMCommunicationFactory.newInstance()
      .createRecordedCommWithReturningID(recordedCommDetails);
  }

  /** register a person **/

  private PersonRegistrationResult registerPersonwithSIN()
    throws AppException, InformationalException {

    // Person Details
    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "Test";
    bdmPersonRegistrationDetails.dtls.surname = "Test";
    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19800101");
    bdmPersonRegistrationDetails.dtls.socialSecurityNumber = "236041661";
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

}
