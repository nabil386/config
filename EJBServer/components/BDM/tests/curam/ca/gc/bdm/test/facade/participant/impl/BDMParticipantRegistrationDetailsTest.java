package curam.ca.gc.bdm.test.facade.participant.impl;

import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.entity.communication.fact.BDMConcernRoleCommunicationFactory;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationDtls;
import curam.ca.gc.bdm.entity.communication.struct.BDMSearchByTrackingNumberKey;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchResultByTrackingNum;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchResultByTrackingNumList;
import curam.ca.gc.bdm.facade.fec.fact.BDMForeignEngagementCaseFactory;
import curam.ca.gc.bdm.facade.fec.struct.BDMFECaseDetails;
import curam.ca.gc.bdm.facade.participant.impl.BDMParticipantRegistrationDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMPersonRegistrationDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMPersonSearchWizardKey;
import curam.ca.gc.bdm.facade.participant.struct.BDMRegisterPersonWizardSearchDetails;
import curam.ca.gc.bdm.sl.communication.fact.BDMCommunicationFactory;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.CASEPRIORITY;
import curam.codetable.CORRESPONDENT;
import curam.codetable.PROVINCETYPE;
import curam.core.facade.fact.ParticipantRegistrationFactory;
import curam.core.facade.fact.PersonFactory;
import curam.core.facade.intf.ParticipantRegistration;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.facade.struct.ConcernRoleIDKey;
import curam.core.facade.struct.CreateIntegratedCaseResultAndMessages;
import curam.core.facade.struct.ParticipantRegistrationWizardResult;
import curam.core.facade.struct.PersonDetails;
import curam.core.facade.struct.PersonRegistrationDetails;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.impl.EnvVars;
import curam.core.sl.struct.RecordedCommDetails1;
import curam.core.sl.struct.WizardStateID;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.ConcernRoleCommKeyOut;
import curam.core.struct.OtherAddressData;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.type.Date;
import curam.util.type.UniqueID;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)
public class BDMParticipantRegistrationDetailsTest
  extends CuramServerTestJUnit4 {

  RegisterPerson registerPerson = new RegisterPerson("RegisterPerson");

  @Mocked
  curam.ca.gc.bdm.entity.person.fact.BDMPersonFactory personFactory;

  curam.ca.gc.bdm.facade.participant.intf.BDMParticipantRegistrationDetails bdmParticipantRegistrationDetailsObj =
    null;

  public BDMParticipantRegistrationDetailsTest() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  BDMPersonSearchResultByTrackingNumList bdmPersonSearchResultByTrackingNumList =
    null;

  ActionIDProperty actionIDPropertySearch = null;

  @Before
  public void setUp() throws AppException, InformationalException {

    bdmPersonSearchResultByTrackingNumList =
      new BDMPersonSearchResultByTrackingNumList();

    bdmParticipantRegistrationDetailsObj =
      curam.ca.gc.bdm.facade.participant.fact.BDMParticipantRegistrationDetailsFactory
        .newInstance();

    actionIDPropertySearch = new ActionIDProperty();
    actionIDPropertySearch.actionIDProperty = "Search";
  }

  /**
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

  public BDMPersonRegistrationDetails setPersonRegDetails()
    throws AppException, InformationalException {

    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    // CREATING ADDRESS FIELD - BEGIN
    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

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
    // CREATING ADDRESS FIELD - END
    bdmPersonRegistrationDetails.dtls.registrationDate =
      Date.getCurrentDate();
    bdmPersonRegistrationDetails.dtls.dateOfBirth =
      Date.getCurrentDate().addDays(-5);
    bdmPersonRegistrationDetails.dtls.firstForename = "John";
    bdmPersonRegistrationDetails.dtls.surname = "Doe";
    bdmPersonRegistrationDetails.dtls.sex = "Male";
    bdmPersonRegistrationDetails.dtls.addressData =
      otherAddressData.addressData;

    return bdmPersonRegistrationDetails;

  }

  @Test
  public void testCreatePerson_sameMailingID()
    throws AppException, InformationalException {

    final BDMPersonRegistrationDetails personDetails = setPersonRegDetails();
    personDetails.isMailingAddSame = true;

    final ActionIDProperty actionID = new ActionIDProperty();
    actionID.actionIDProperty = "Save";

    ParticipantRegistrationWizardResult participantRegistrationWizardResult =
      new ParticipantRegistrationWizardResult();

    final BDMParticipantRegistrationDetails registration =
      new BDMParticipantRegistrationDetails();
    final WizardStateID wizardStateID = new WizardStateID();

    final ParticipantRegistration partReg =
      ParticipantRegistrationFactory.newInstance();
    partReg.getRegisterPersonSearchCriteria(wizardStateID);
    Configuration.setProperty(EnvVars.ENV_PDC_ENABLED, "YES");
    participantRegistrationWizardResult = registration
      .setRegisterPersonForPDCDetails(personDetails, wizardStateID, actionID);

    final curam.core.facade.intf.Person person = PersonFactory.newInstance();

    final ConcernRoleIDKey concernRoleID = new ConcernRoleIDKey();
    concernRoleID.concernRoleID =
      participantRegistrationWizardResult.registrationResult.concernRoleID;

    final PersonDetails personInfo = person.readPersonDetails(concernRoleID);

    System.out.println(personInfo.dtls.gender);

    assertEquals(personInfo.dtls.gender, "Male");
  }

  /**
   * 8914 person search criteria extension Junit
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSetRegisterPersonSearchCriteriaDetailsExt()
    throws AppException, InformationalException {

    final PersonRegistrationResult personRecord =
      createPersonRecord("Tom", "Denwood");
    final BDMRegisterPersonWizardSearchDetails bdmParticipantRegistrationDetails =
      new BDMRegisterPersonWizardSearchDetails();

    final BDMPersonSearchWizardKey bdmPersonSearchWizardKey =
      new BDMPersonSearchWizardKey();
    bdmPersonSearchWizardKey.dtls.searchKey.forename = "Tom";
    bdmPersonSearchWizardKey.dtls.searchKey.surname = "Denwood";
    bdmPersonSearchWizardKey.dtls.searchKey.gender = "SX1";
    bdmPersonSearchWizardKey.dtls.searchKey.dateOfBirth =
      Date.getDate("19770101");

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = UniqueID.nextUniqueID();

    assertTrue(
      bdmParticipantRegistrationDetails.searchResult.personSearchResult.dtlsList
        .isEmpty());

    // boolean caught = false;
    // try {
    // bdmParticipantRegistrationDetails = bdmParticipantRegistrationDetailsObj
    // .setRegisterPersonSearchCriteriaDetailsExt(bdmPersonSearchWizardKey,
    // wizardStateID, actionIDProperty);
    //
    // } catch (final Exception e) {
    //
    // caught = true;
    // assertEquals(
    // "There are no matching items based on the Search Criteria entered.",
    // e.getMessage());
    // }
    //
    // assertTrue(caught);

  }

  /**
   * Search Person by Tracking Number only
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void
    testSetRegisterPersonSearchCriteriaDetailsExt_trackingNumberOnly()
      throws AppException, InformationalException {

    // Person Details
    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "John";
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

    // Assert person is registered
    assertTrue(
      registrationResult.registrationIDDetails.alternateID.length() > 0);

    // Create FEC case
    final CreateIntegratedCaseResultAndMessages createCase =
      createFEC(registrationResult.registrationIDDetails.concernRoleID);

    // Created recorded CXommunication
    final ConcernRoleCommKeyOut commID =
      createRecordedCommunicationwithEscalation(
        createCase.createCaseResult.integratedCaseID, "John Doe",
        registrationResult.registrationIDDetails.concernRoleID);

    // Create BDM concernRole Communication with Tracking number
    createBDMConcernRoleCommunication(commID.communicationID);
    expectationSearchByTrackingNumber(
      registrationResult.registrationIDDetails.concernRoleID,
      registrationResult.registrationIDDetails.alternateID);

    final BDMPersonSearchWizardKey bdmPersonSearchWizardKey =
      new BDMPersonSearchWizardKey();
    bdmPersonSearchWizardKey.correspondenceTrackingNumber = "1234";

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = UniqueID.nextUniqueID();

    final BDMRegisterPersonWizardSearchDetails result =
      bdmParticipantRegistrationDetailsObj
        .setRegisterPersonSearchCriteriaDetailsExt(bdmPersonSearchWizardKey,
          wizardStateID, actionIDPropertySearch);

    assertTrue(!result.searchResult.personSearchResult.dtlsList.isEmpty());
    assertEquals(
      result.searchResult.personSearchResult.dtlsList.get(0).concernRoleName,
      "John Doe");

    assertEquals(
      result.searchResult.personSearchResult.dtlsList.get(0).dateOfBirth,
      Date.getDate("19800101"));

  }

  /**
   * Search Person by Tracking Number and reference number is provided
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void
    testSetRegisterPersonSearchCriteriaDetailsExt_trackingNumber_referenceNum()
      throws AppException, InformationalException {

    // Person Details
    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "John";
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

    // Assert person is registered
    assertTrue(
      registrationResult.registrationIDDetails.alternateID.length() > 0);

    // Create FEC case
    final CreateIntegratedCaseResultAndMessages createCase =
      createFEC(registrationResult.registrationIDDetails.concernRoleID);

    // Created recorded CXommunication
    final ConcernRoleCommKeyOut commID =
      createRecordedCommunicationwithEscalation(
        createCase.createCaseResult.integratedCaseID, "John Doe",
        registrationResult.registrationIDDetails.concernRoleID);

    // Create BDM concernRole Communication with Tracking number
    createBDMConcernRoleCommunication(commID.communicationID);

    expectationSearchByTrackingNumber(
      registrationResult.registrationIDDetails.concernRoleID,
      registrationResult.registrationIDDetails.alternateID);

    final BDMPersonSearchWizardKey bdmPersonSearchWizardKey =
      new BDMPersonSearchWizardKey();
    bdmPersonSearchWizardKey.correspondenceTrackingNumber = "1234";
    bdmPersonSearchWizardKey.dtls.searchKey.referenceNumber =
      registrationResult.registrationIDDetails.alternateID;

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = UniqueID.nextUniqueID();

    final boolean assertFlag = false;
    final BDMRegisterPersonWizardSearchDetails result =
      bdmParticipantRegistrationDetailsObj
        .setRegisterPersonSearchCriteriaDetailsExt(bdmPersonSearchWizardKey,
          wizardStateID, actionIDPropertySearch);

    assertTrue(!result.searchResult.personSearchResult.dtlsList.isEmpty());
    assertEquals(
      result.searchResult.personSearchResult.dtlsList.get(0).concernRoleName,
      "John Doe");

    assertEquals(
      result.searchResult.personSearchResult.dtlsList.get(0).dateOfBirth,
      Date.getDate("19800101"));

  }

  /**
   * Search Person by Tracking Number only when alpha numeric tracking number is
   * provided
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void
    testSetRegisterPersonSearchCriteriaDetailsExt_trackingNumber_alpaNum()
      throws AppException, InformationalException {

    // Person Details

    final BDMPersonSearchWizardKey bdmPersonSearchWizardKey =
      new BDMPersonSearchWizardKey();
    bdmPersonSearchWizardKey.correspondenceTrackingNumber = "A1234%*";

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = UniqueID.nextUniqueID();

    BDMRegisterPersonWizardSearchDetails result =
      new BDMRegisterPersonWizardSearchDetails();

    result = bdmParticipantRegistrationDetailsObj
      .setRegisterPersonSearchCriteriaDetailsExt(bdmPersonSearchWizardKey,
        wizardStateID, actionIDPropertySearch);

    assertTrue(result.searchResult.personSearchResult.dtlsList.isEmpty());
    assertTrue(result.isNextButtonEnabled);
  }

  /**
   * Search Person no criteria provided
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSetRegisterPersonSearchCriteriaDetailsExt_noCriteria()
    throws AppException, InformationalException {

    final BDMPersonSearchWizardKey bdmPersonSearchWizardKey =
      new BDMPersonSearchWizardKey();

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = UniqueID.nextUniqueID();

    boolean caught = false;
    try {
      bdmParticipantRegistrationDetailsObj
        .setRegisterPersonSearchCriteriaDetailsExt(bdmPersonSearchWizardKey,
          wizardStateID, actionIDPropertySearch);
    } catch (final Exception e) {
      caught = true;

    }
    assertTrue(caught);
  }

  /**
   * Search Person by first name , last name and partial city
   * provided
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSetRegisterPersonSearchCriteriaDetailsExt_city()
    throws AppException, InformationalException {

    final BDMPersonSearchWizardKey bdmPersonSearchWizardKey =
      new BDMPersonSearchWizardKey();
    bdmPersonSearchWizardKey.dtls.searchKey.forename = "John";
    bdmPersonSearchWizardKey.dtls.searchKey.surname = "Doe";
    bdmPersonSearchWizardKey.dtls.searchKey.addressDtls.city = "O";

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = UniqueID.nextUniqueID();

    boolean caught = false;
    try {
      bdmParticipantRegistrationDetailsObj
        .setRegisterPersonSearchCriteriaDetailsExt(bdmPersonSearchWizardKey,
          wizardStateID, actionIDPropertySearch);
    } catch (final Exception e) {
      caught = true;

      assertTrue(e.getMessage()
        .contains("City, Town, or Village requires at least 2 characters."));

    }
    assertTrue(caught);
  }

  /**
   * Search Person by first name , last name and partial postalcode
   * provided
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSetRegisterPersonSearchCriteriaDetailsExt_postalCode()
    throws AppException, InformationalException {

    final BDMPersonSearchWizardKey bdmPersonSearchWizardKey =
      new BDMPersonSearchWizardKey();
    bdmPersonSearchWizardKey.dtls.searchKey.forename = "John";
    bdmPersonSearchWizardKey.dtls.searchKey.surname = "Doe";
    bdmPersonSearchWizardKey.postalCode = "K5";

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = UniqueID.nextUniqueID();

    boolean caught = false;
    try {
      bdmParticipantRegistrationDetailsObj
        .setRegisterPersonSearchCriteriaDetailsExt(bdmPersonSearchWizardKey,
          wizardStateID, actionIDPropertySearch);
    } catch (final Exception e) {
      caught = true;
      assertTrue(e.getMessage()
        .contains("Postal or Zip Code requires at least 3 characters"));
    }
    assertTrue(caught);
  }

  /**
   * Search Person by Tracking Number only when alpha numeric tracing number is
   * provided
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSetAddProspectPersonSearchCriteriaDetailsExt_city()
    throws AppException, InformationalException {

    final BDMPersonSearchWizardKey bdmPersonSearchWizardKey =
      new BDMPersonSearchWizardKey();
    bdmPersonSearchWizardKey.dtls.searchKey.forename = "John";
    bdmPersonSearchWizardKey.dtls.searchKey.surname = "Doe";
    bdmPersonSearchWizardKey.dtls.searchKey.addressDtls.city = "O";

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = UniqueID.nextUniqueID();

    boolean caught = false;
    try {
      bdmParticipantRegistrationDetailsObj
        .setAddProspectPersonSearchCriteriaDetailsExt(
          bdmPersonSearchWizardKey, wizardStateID, actionIDPropertySearch);
    } catch (final Exception e) {
      caught = true;

      assertTrue(e.getMessage()
        .contains("City, Town, or Village requires at least 2 characters."));

    }
    assertTrue(caught);
  }

  /**
   * Search Person by first name , last name and partial postalcode
   * provided
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testAddProspectPersonSearchCriteriaDetailsExt_postalCode()
    throws AppException, InformationalException {

    // Person Details

    final PersonRegistrationResult registrationResult =
      createPersonRecord("John", "Doe");

    // Assert person is registered
    assertTrue(
      registrationResult.registrationIDDetails.alternateID.length() > 0);

    // Create FEC case
    final CreateIntegratedCaseResultAndMessages createCase =
      createFEC(registrationResult.registrationIDDetails.concernRoleID);

    // Created recorded CXommunication
    final ConcernRoleCommKeyOut commID =
      createRecordedCommunicationwithEscalation(
        createCase.createCaseResult.integratedCaseID, "John Doe",
        registrationResult.registrationIDDetails.concernRoleID);

    // Create BDM concernRole Communication with Tracking number
    createBDMConcernRoleCommunication(commID.communicationID);

    final BDMPersonSearchWizardKey bdmPersonSearchWizardKey =
      new BDMPersonSearchWizardKey();
    bdmPersonSearchWizardKey.dtls.searchKey.forename = "John";
    bdmPersonSearchWizardKey.dtls.searchKey.surname = "Doe";
    bdmPersonSearchWizardKey.postalCode = "K5";

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = UniqueID.nextUniqueID();

    boolean caught = false;
    try {
      bdmParticipantRegistrationDetailsObj
        .setAddProspectPersonSearchCriteriaDetailsExt(
          bdmPersonSearchWizardKey, wizardStateID, actionIDPropertySearch);
    } catch (final Exception e) {
      caught = true;
      assertTrue(e.getMessage()
        .contains("Postal or Zip Code requires at least 3 characters"));
    }
    assertTrue(caught);
  }

  /**
   * Search Person by Tracking Number only
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void
    testsetAddProspectPersonSearchCriteriaDetailsExt_trackingNumberOnly()
      throws AppException, InformationalException {

    // Person Details

    final PersonRegistrationResult registrationResult =
      createPersonRecord("John", "Doe");

    // Assert person is registered
    assertTrue(
      registrationResult.registrationIDDetails.alternateID.length() > 0);

    // Create FEC case
    final CreateIntegratedCaseResultAndMessages createCase =
      createFEC(registrationResult.registrationIDDetails.concernRoleID);

    // Created recorded CXommunication
    final ConcernRoleCommKeyOut commID =
      createRecordedCommunicationwithEscalation(
        createCase.createCaseResult.integratedCaseID, "John Doe",
        registrationResult.registrationIDDetails.concernRoleID);

    // Create BDM concernRole Communication with Tracking number
    createBDMConcernRoleCommunication(commID.communicationID);
    expectationSearchByTrackingNumber(
      registrationResult.registrationIDDetails.concernRoleID,
      registrationResult.registrationIDDetails.alternateID);

    final BDMPersonSearchWizardKey bdmPersonSearchWizardKey =
      new BDMPersonSearchWizardKey();
    bdmPersonSearchWizardKey.correspondenceTrackingNumber = "1234";

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = UniqueID.nextUniqueID();

    final BDMRegisterPersonWizardSearchDetails result =
      bdmParticipantRegistrationDetailsObj
        .setAddProspectPersonSearchCriteriaDetailsExt(
          bdmPersonSearchWizardKey, wizardStateID, actionIDPropertySearch);

    assertTrue(!result.searchResult.personSearchResult.dtlsList.isEmpty());
    assertEquals(
      result.searchResult.personSearchResult.dtlsList.get(0).concernRoleName,
      "John Doe");

    assertEquals(
      result.searchResult.personSearchResult.dtlsList.get(0).dateOfBirth,
      Date.getDate("19800101"));

  }

  /**
   * Search Person no criteria provided
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSetAddProspectPersonSearchCriteriaDetailsExt_noCriteria()
    throws AppException, InformationalException {

    final BDMPersonSearchWizardKey bdmPersonSearchWizardKey =
      new BDMPersonSearchWizardKey();

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = UniqueID.nextUniqueID();

    final BDMRegisterPersonWizardSearchDetails result = null;
    boolean caught = false;
    try {
      bdmParticipantRegistrationDetailsObj
        .setAddProspectPersonSearchCriteriaDetailsExt(
          bdmPersonSearchWizardKey, wizardStateID, actionIDPropertySearch);
    } catch (final Exception e) {
      caught = true;

    }
    assertTrue(caught);
  }

  /**
   * Search Person by Tracking Number only when alpha numeric tracing number is
   * provided
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void
    testSetAddProspectPersonSearchCriteriaDetailsExt_trackingNumber_alpaNum()
      throws AppException, InformationalException {

    final BDMPersonSearchWizardKey bdmPersonSearchWizardKey =
      new BDMPersonSearchWizardKey();
    bdmPersonSearchWizardKey.correspondenceTrackingNumber = "A1234%*";

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = UniqueID.nextUniqueID();

    final BDMRegisterPersonWizardSearchDetails result =
      bdmParticipantRegistrationDetailsObj
        .setAddProspectPersonSearchCriteriaDetailsExt(
          bdmPersonSearchWizardKey, wizardStateID, actionIDPropertySearch);

    assertTrue(result.searchResult.personSearchResult.dtlsList.isEmpty());
    assertTrue(result.isNextButtonEnabled);
  }

  /**
   * Search Person by Tracking Number and reference number is provided
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void
    testSetAddProspectPersonSearchCriteriaDetailsExt_trackingNumber_referenceNum()
      throws AppException, InformationalException {

    // Person Details

    final PersonRegistrationResult registrationResult =
      createPersonRecord("John", "Doe");

    // Assert person is registered
    assertTrue(
      registrationResult.registrationIDDetails.alternateID.length() > 0);

    // Create FEC case
    final CreateIntegratedCaseResultAndMessages createCase =
      createFEC(registrationResult.registrationIDDetails.concernRoleID);

    // Created recorded CXommunication
    final ConcernRoleCommKeyOut commID =
      createRecordedCommunicationwithEscalation(
        createCase.createCaseResult.integratedCaseID, "John Doe",
        registrationResult.registrationIDDetails.concernRoleID);

    // Create BDM concernRole Communication with Tracking number
    createBDMConcernRoleCommunication(commID.communicationID);

    expectationSearchByTrackingNumber(
      registrationResult.registrationIDDetails.concernRoleID,
      registrationResult.registrationIDDetails.alternateID);

    final BDMPersonSearchWizardKey bdmPersonSearchWizardKey =
      new BDMPersonSearchWizardKey();
    bdmPersonSearchWizardKey.correspondenceTrackingNumber = "1234";
    bdmPersonSearchWizardKey.dtls.searchKey.referenceNumber =
      registrationResult.registrationIDDetails.alternateID;

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = UniqueID.nextUniqueID();

    final BDMRegisterPersonWizardSearchDetails result =
      bdmParticipantRegistrationDetailsObj
        .setAddProspectPersonSearchCriteriaDetailsExt(
          bdmPersonSearchWizardKey, wizardStateID, actionIDPropertySearch);

    assertTrue(!result.searchResult.personSearchResult.dtlsList.isEmpty());
    assertEquals(
      result.searchResult.personSearchResult.dtlsList.get(0).concernRoleName,
      "John Doe");

    assertEquals(
      result.searchResult.personSearchResult.dtlsList.get(0).dateOfBirth,
      Date.getDate("19800101"));

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

  /**
   * Create BDM ConcernRole communication Object with Tracking number
   */
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
    return registrationResult;
  }

}
