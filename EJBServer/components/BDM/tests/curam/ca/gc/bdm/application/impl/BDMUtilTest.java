/**
 *
 */
package curam.ca.gc.bdm.application.impl;

import curam.ca.gc.bdm.codetable.BDMESCALATIONLEVELS;
import curam.ca.gc.bdm.codetable.BDMPHONECOUNTRY;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.entity.fact.BDMEscalationLevelFactory;
import curam.ca.gc.bdm.entity.struct.BDMEscalationLevelDtls;
import curam.ca.gc.bdm.facade.participant.struct.BDMPersonRegistrationDetails;
import curam.ca.gc.bdm.sl.communication.fact.BDMCommunicationFactory;
import curam.ca.gc.bdm.sl.struct.BDMThirdPartyParticipantSearchKey;
import curam.ca.gc.bdm.test.BDMBaseTest;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.CORRESPONDENT;
import curam.codetable.EMAILTYPE;
import curam.codetable.METHODOFDELIVERY;
import curam.codetable.PHONETYPE;
import curam.codetable.PROVINCETYPE;
import curam.core.facade.fact.PersonFactory;
import curam.core.facade.fact.ProspectPersonFactory;
import curam.core.facade.struct.PersonRegistrationDetails;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.facade.struct.ProspectPersonRegistrationDetails;
import curam.core.sl.struct.AllParticipantSearchDetails;
import curam.core.sl.struct.AllParticipantSearchResult;
import curam.core.sl.struct.RecordedCommDetails1;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.ConcernRoleCommKeyOut;
import curam.core.struct.ConcernRolePhoneNumberKey;
import curam.core.struct.OtherAddressData;
import curam.pdc.struct.ParticipantPhoneDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import curam.util.workflow.fact.BizObjAssociationFactory;
import curam.util.workflow.struct.BizObjAssociationDtls;
import curam.util.workflow.struct.BizObjAssociationDtlsList;
import curam.util.workflow.struct.TaskKey;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * JUnit test class to test
 * <code>curam.ca.gc.bdm.application.impl.BDMUtil</code>.
 *
 * @author donghua.jin
 *
 */

@RunWith(JMockit.class)
public class BDMUtilTest extends BDMBaseTest {

  @Mocked
  BizObjAssociationFactory bizObjAssociationFactory;

  BizObjAssociationDtlsList bizObjAssociationDtlsList;

  BDMUtil bdmUtil;

  @Before
  public void setUp() throws AppException, InformationalException {

    bdmUtil = new BDMUtil();

    bizObjAssociationDtlsList = new BizObjAssociationDtlsList();

  }

  private void searchByTaskIDExpectation(final long taskID,
    final long communicationID) throws AppException, InformationalException {

    new Expectations() {

      {
        BizObjAssociationFactory.newInstance().searchByTaskID((TaskKey) any);

        final BizObjAssociationDtls bizObjAssociationDtls =
          new BizObjAssociationDtls();

        bizObjAssociationDtls.bizObjectType =
          BUSINESSOBJECTTYPE.BDMCOMMUNICATION;
        bizObjAssociationDtls.bizObjectID = communicationID;
        bizObjAssociationDtls.bizObjAssocID = 256;
        bizObjAssociationDtlsList.dtls.add(bizObjAssociationDtls);
        result = bizObjAssociationDtlsList;

      }

    };

  }

  /**
   * Test filtering search result with BDM International Address layout.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testFilterAddressForAllParticipant()
    throws AppException, InformationalException {

    final PersonRegistrationResult regPersonDetails =
      this.registerPersonWithBDMIntlAddress();

    final AllParticipantSearchResult participantSearchResult =
      new AllParticipantSearchResult();

    final AllParticipantSearchDetails allPartSearchDetails =
      new AllParticipantSearchDetails();

    allPartSearchDetails.concernRoleID =
      regPersonDetails.registrationIDDetails.concernRoleID;

    participantSearchResult.dtlsList.addRef(allPartSearchDetails);

    final BDMThirdPartyParticipantSearchKey key =
      new BDMThirdPartyParticipantSearchKey();

    AllParticipantSearchResult filteredSearchResult =
      BDMUtil.filterAddressForAllParticipant(participantSearchResult, key);

    assertEquals(1, filteredSearchResult.dtlsList.size());

    key.stateProvince = "British columbia";
    key.postalCode = "V7C 3V9";

    filteredSearchResult =
      BDMUtil.filterAddressForAllParticipant(participantSearchResult, key);

    key.stateProvince = "Alberta"; // wrong city
    key.postalCode = "V7C 3V9";

    filteredSearchResult =
      BDMUtil.filterAddressForAllParticipant(participantSearchResult, key);

    assertEquals(0, filteredSearchResult.dtlsList.size());

    key.stateProvince = "BRITISH COLUMBIA";
    key.postalCode = "V7C 3V8"; // wrong postal code

    filteredSearchResult =
      BDMUtil.filterAddressForAllParticipant(participantSearchResult, key);

    assertEquals(0, filteredSearchResult.dtlsList.size());

    key.unitNumber = "207";
    key.stateProvince = "BRITISH COLUMBIA";
    key.postalCode = "V7C 3V9";
    key.countryCode = curam.codetable.COUNTRY.CA;

    filteredSearchResult =
      BDMUtil.filterAddressForAllParticipant(participantSearchResult, key);

    assertEquals(1, filteredSearchResult.dtlsList.size());
  }

  /**
   * Test BDM status is not matched with an invalid CCT status.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testGetBDMStatusForCCTStatus_invalidCCTStatus()
    throws AppException, InformationalException {

    final BDMUtil testSubject = new BDMUtil();
    final String result =
      testSubject.getBDMStatusForCCTStatus("ACTIF_COMPLET");

    assertEquals("1", result);
  }

  /**
   * util method to create Phone Number Evidence
   *
   * @param details
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testCreatePhoneNumberEvidence()
    throws AppException, InformationalException {

    final ParticipantPhoneDetails participantPhoneDetails =
      new ParticipantPhoneDetails();
    final String countryCode = BDMPHONECOUNTRY.CANADA;

    final ProspectPersonRegistrationDetails prospectPersonRegistrationDetails =
      getProspectPersonRegistrationDetails();

    final curam.core.facade.struct.ProspectPersonRegistrationResult registrationResult =
      ProspectPersonFactory.newInstance()
        .registerProspectPerson(prospectPersonRegistrationDetails);

    participantPhoneDetails.concernRoleID =
      registrationResult.registrationIDDetails.concernRoleID;

    participantPhoneDetails.phoneAreaCode = "306";

    participantPhoneDetails.phoneCountryCode = BDMPHONECOUNTRY.CANADA;

    participantPhoneDetails.phoneNumber = "1234567";

    participantPhoneDetails.startDate = Date.getCurrentDate();
    participantPhoneDetails.typeCode = PHONETYPE.MOBILE;

    final ConcernRolePhoneNumberKey concernRolePhoneNumberKey =
      bdmUtil.createPhoneNumberEvidence(participantPhoneDetails, countryCode);

    assertTrue(null != concernRolePhoneNumberKey);

  }

  @Test
  public void testGetNextEscalationLevel()
    throws AppException, InformationalException {

    String nextEscalation = "";

    nextEscalation =
      BDMUtil.getNextEscalationLevel(BDMESCALATIONLEVELS.ESCALATION_LEVEL_1);
    assertEquals(BDMESCALATIONLEVELS.ESCALATION_LEVEL_2, nextEscalation);

    nextEscalation =
      BDMUtil.getNextEscalationLevel(BDMESCALATIONLEVELS.ESCALATION_LEVEL_2);
    assertEquals(BDMESCALATIONLEVELS.ESCALATION_LEVEL_3, nextEscalation);

    nextEscalation =
      BDMUtil.getNextEscalationLevel(BDMESCALATIONLEVELS.ESCALATION_LEVEL_3);
    assertEquals(BDMESCALATIONLEVELS.ESCALATION_LEVEL_4, nextEscalation);

    nextEscalation =
      BDMUtil.getNextEscalationLevel(BDMESCALATIONLEVELS.ESCALATION_LEVEL_4);
    assertEquals(BDMESCALATIONLEVELS.ESCALATION_LEVEL_5, nextEscalation);

    nextEscalation =
      BDMUtil.getNextEscalationLevel(BDMESCALATIONLEVELS.ESCALATION_LEVEL_5);
    assertEquals(BDMESCALATIONLEVELS.ESCALATION_LEVEL_6, nextEscalation);

    nextEscalation =
      BDMUtil.getNextEscalationLevel(BDMESCALATIONLEVELS.ESCALATION_LEVEL_6);
    assertEquals(BDMESCALATIONLEVELS.ESCALATION_LEVEL_7, nextEscalation);

    nextEscalation =
      BDMUtil.getNextEscalationLevel(BDMESCALATIONLEVELS.ESCALATION_LEVEL_7);
    assertEquals(BDMESCALATIONLEVELS.ESCALATION_LEVEL_8, nextEscalation);

    nextEscalation =
      BDMUtil.getNextEscalationLevel(BDMESCALATIONLEVELS.ESCALATION_LEVEL_8);
    assertEquals(BDMESCALATIONLEVELS.ESCALATION_LEVEL_8, nextEscalation);

  }

  @Test
  public void testGetCurrentEscalationLevel()
    throws AppException, InformationalException {

    final PersonRegistrationResult registrationResult =
      createPersonRecord("Jane", "Doe");

    final ConcernRoleCommKeyOut concernRoleCommKeyOut =
      setUpcreateRecordedCommunicationwithEscalation("John",
        registrationResult.registrationIDDetails.concernRoleID);

    setUpTaskEscalationLevel(concernRoleCommKeyOut.communicationID);

    searchByTaskIDExpectation(256, concernRoleCommKeyOut.communicationID);
    final BDMEscalationLevelDtls str = bdmUtil.getCurrentEscalationLevel(256);

    assertEquals(BDMESCALATIONLEVELS.ESCALATION_LEVEL_1, str.escalationLevel);

  }

  @Test
  public void isCountryRestricted()
    throws AppException, InformationalException {

    final boolean flag = BDMUtil.isCountryRestricted(BDMSOURCECOUNTRY.US);

    assertTrue(flag);

  }

  /** Set up task escalation */
  private void setUpTaskEscalationLevel(final long communicationID)
    throws AppException, InformationalException {

    final BDMEscalationLevelDtls escalationLevelDtls =
      new BDMEscalationLevelDtls();
    escalationLevelDtls.bdmEscalationLevelID = 256;

    escalationLevelDtls.communicationID = communicationID;
    escalationLevelDtls.escalationLevel =
      BDMESCALATIONLEVELS.ESCALATION_LEVEL_1;

    BDMEscalationLevelFactory.newInstance().insert(escalationLevelDtls);

  }

  /** Create recorded Communication with escaltion level */
  ConcernRoleCommKeyOut setUpcreateRecordedCommunicationwithEscalation(
    final String concernRoleName, final long concernRoleID)
    throws AppException, InformationalException {

    final RecordedCommDetails1 recordedCommDetails =
      new RecordedCommDetails1();

    recordedCommDetails.addressData =
      "1\n0\nBDMINTL\nUS\n1\n0\nZIP=\nBDMSTPROVX=\nCITY=\nCOUNTRY=\nPOBOXNO=\nAPT=\nPOSTCODE=\nBDMZIPX=\nSTATEPROV=\nPROV=\nADD1=\nADD2=\n";
    recordedCommDetails.caseID = 0;
    recordedCommDetails.communicationDirection = "CD2";
    recordedCommDetails.communicationText = "Test communication";
    recordedCommDetails.communicationTypeCode = "CT8007"; // requires attendtion
    recordedCommDetails.correspondentName = "Test";
    recordedCommDetails.correspondentParticipantRoleID = concernRoleID;
    recordedCommDetails.correspondentType = CORRESPONDENT.CLIENT;
    recordedCommDetails.methodTypeCode = "CM1";
    recordedCommDetails.subject = "Test comm 2";
    recordedCommDetails.communicationFormat = "CF4"; // recorded Communication

    return BDMCommunicationFactory.newInstance()
      .createRecordedCommWithReturningID(recordedCommDetails);
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
