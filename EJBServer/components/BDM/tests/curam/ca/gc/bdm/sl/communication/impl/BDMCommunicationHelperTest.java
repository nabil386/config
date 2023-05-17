package curam.ca.gc.bdm.sl.communication.impl;

import curam.bdm.test.junit4.MockLogin;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.communication.impl.BDMCommunicationHelper;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationDtls;
import curam.ca.gc.bdm.entity.fec.fact.BDMFECaseFactory;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseDtls;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseKey;
import curam.ca.gc.bdm.facade.communication.struct.BDMViewCorrespondenceDetails;
import curam.ca.gc.bdm.facade.externalparty.impl.BDMExternalParty;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMExternalPartyRegistrationDetails;
import curam.ca.gc.bdm.facade.organization.fact.BDMOrganizationFactory;
import curam.ca.gc.bdm.facade.organization.intf.BDMOrganization;
import curam.ca.gc.bdm.facade.organization.struct.BDMCreateAndAssignUser;
import curam.ca.gc.bdm.facade.participant.struct.BDMCommunicationAndListRowActionDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMBPOCCT;
import curam.ca.gc.bdm.test.BDMBaseTest;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.ca.gc.bdm.test.data.impl.BDMExternalPartyTestDataDetails;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.codetable.COMMUNICATIONFORMAT;
import curam.codetable.COMMUNICATIONSTATUS;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.EMAILTYPE;
import curam.codetable.EXTERNALPARTYTYPE;
import curam.codetable.LANGUAGE;
import curam.codetable.METHODOFDELIVERY;
import curam.codetable.PROVINCETYPE;
import curam.codetable.TASKSTATUS;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.facade.struct.CreateIntegratedCaseResultAndMessages;
import curam.core.facade.struct.ExternalPartyRegistrationDetails;
import curam.core.facade.struct.ParticipantRegistrationWizardResult;
import curam.core.fact.AddressElementFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.fact.PersonRegistrationFactory;
import curam.core.impl.CuramConst;
import curam.core.intf.ConcernRole;
import curam.core.intf.PersonRegistration;
import curam.core.sl.struct.WizardStateID;
import curam.core.struct.AddressElementDtls;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.ConcernRoleCommunicationDtls;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.OtherAddressData;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonRegistrationDetails;
import curam.core.struct.RegistrationIDDetails;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import curam.util.workflow.fact.BizObjAssociationFactory;
import curam.util.workflow.intf.BizObjAssociation;
import curam.util.workflow.struct.BizObjAssocCountOpenTasksKey;
import curam.util.workflow.struct.TaskCount;
import data.impl.BDMExternalPartyTestData;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for class BDMCommunication
 */
public class BDMCommunicationHelperTest extends BDMBaseTest {

  BDMCommunicationHelper bdmCommunicationHelper =
    new BDMCommunicationHelper();

  BDMExternalParty bdmExternalPartyFacade = new BDMExternalParty();

  public BDMCommunicationHelperTest() {

    super();

  }

  @Override
  public boolean shouldCommit() {

    return false;
  }

  @Test
  public void testGetPreferredLanguage_ThirdParty_English() throws Exception {

    // testCreateAndAssignUserForPosition();

    MockLogin.getInst().login("unauthenticated");

    final long externalPartyID =
      createExternalParty_ThirdParty(LANGUAGE.ENGLISH);

    final String preferredLanguage =
      bdmCommunicationHelper.getPreferredLanguage(externalPartyID);

    assertEquals("en_ca", preferredLanguage);
  }

  @Test
  public void testGetPreferredLanguage_ThirdParty_French() throws Exception {

    // testCreateAndAssignUserForPosition();

    MockLogin.getInst().login("unauthenticated");

    final long externalPartyID =
      createExternalParty_ThirdParty(LANGUAGE.FRENCH);

    final String preferredLanguage =
      bdmCommunicationHelper.getPreferredLanguage(externalPartyID);
    assertEquals("fr_ca", preferredLanguage);
  }

  @Test
  public void testGetPreferredLanguage_ThirdParty_BDMEnglish()
    throws Exception {

    MockLogin.getInst().login("unauthenticated");

    final long externalPartyID =
      createExternalParty_ThirdParty(BDMLANGUAGE.ENGLISHL);

    final String preferredLanguage =
      bdmCommunicationHelper.getPreferredLanguage(externalPartyID);

    assertEquals("en_ca", preferredLanguage);
  }

  @Test
  public void testGetPreferredLanguage_ThirdParty_BDMFrench()
    throws Exception {

    MockLogin.getInst().login("unauthenticated");

    final long externalPartyID =
      createExternalParty_ThirdParty(BDMLANGUAGE.FRENCHL);

    final String preferredLanguage =
      bdmCommunicationHelper.getPreferredLanguage(externalPartyID);
    assertEquals("fr_ca", preferredLanguage);
  }

  @Test
  public void testGetPreferredLanguage_PDCPerson_English() throws Exception {

    // testCreateAndAssignUserForPosition();

    MockLogin.getInst().login("unauthenticated");

    final PDCPersonDetails person = createPDCPerson(BDMLANGUAGE.ENGLISHL);

    final String preferredLanguage =
      bdmCommunicationHelper.getPreferredLanguage(person.concernRoleID);

    assertEquals("en_ca", preferredLanguage);
  }

  @Test
  public void testGetPreferredLanguage_PDCPerson_French() throws Exception {

    // testCreateAndAssignUserForPosition();

    MockLogin.getInst().login("unauthenticated");

    final PDCPersonDetails person = createPDCPerson(BDMLANGUAGE.FRENCHL);

    final String preferredLanguage =
      bdmCommunicationHelper.getPreferredLanguage(person.concernRoleID);

    assertEquals("fr_ca", preferredLanguage);
  }

  @Test
  public void testGetAddressMapper_ERR_UNABLE_TO_CREATE_CORRESPONDENCE()
    throws Exception {

    MockLogin.getInst().login("unauthenticated");
    final PDCPersonDetails person = createPDCPersonLongName();
    final ConcernRoleKey key = new ConcernRoleKey();
    key.concernRoleID = person.concernRoleID;
    final long mailingAddressID =
      bdmCommunicationHelper.getAddressIDforConcern(key.concernRoleID, false);

    final AddressElementDtls dtls1 = new AddressElementDtls();
    dtls1.addressID = mailingAddressID;
    dtls1.addressElementID = 1234;
    dtls1.elementType = BDMConstants.kADDRESSELEMENT_COUNTRY;
    dtls1.elementValue = "";
    dtls1.upperElementValue = "1";
    try {
      AddressElementFactory.newInstance().insert(dtls1);
      bdmCommunicationHelper.getAddressMapper(mailingAddressID);
    } catch (final Exception e) {
      System.out.println(e.getLocalizedMessage());
      assertEquals(BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE.toString(),
        e.getLocalizedMessage());
    }

  }

  @Test
  public void testGetAddressMapper_UnParsed() throws Exception {

    MockLogin.getInst().login("unauthenticated");
    final PDCPersonDetails person = createPDCPersonLongName();
    final ConcernRoleKey key = new ConcernRoleKey();
    key.concernRoleID = person.concernRoleID;
    final long mailingAddressID =
      bdmCommunicationHelper.getAddressIDforConcern(key.concernRoleID, false);

    final AddressElementDtls dtls1 = new AddressElementDtls();
    dtls1.addressID = mailingAddressID;
    dtls1.addressElementID = 1234;
    dtls1.elementType = BDMConstants.kBDMUNPARSE;
    dtls1.elementValue = "1";
    dtls1.upperElementValue = "1";
    AddressElementFactory.newInstance().insert(dtls1);

    final AddressElementDtls dtls2 = new AddressElementDtls();
    dtls2.addressID = mailingAddressID;
    dtls2.addressElementID = 1235;
    dtls2.elementType = BDMConstants.kADDRESSELEMENT_APTUNITNUM;
    dtls2.elementValue = "1";
    dtls2.upperElementValue = "1";
    AddressElementFactory.newInstance().insert(dtls2);

    final String[] result =
      bdmCommunicationHelper.getAddressMapper(mailingAddressID);
    assertTrue(result.length != 0);

  }

  // failing... fix me
  @Test
  public void testGetAddressMapper_CA() throws Exception {

    MockLogin.getInst().login("unauthenticated");

    final PDCPersonDetails person = createPDCPerson(BDMLANGUAGE.FRENCHL);
    final ConcernRole concernRole = ConcernRoleFactory.newInstance();
    final ConcernRoleKey key = new ConcernRoleKey();
    key.concernRoleID = person.concernRoleID;
    final ConcernRoleDtls concernRoleDtls = concernRole.read(key);

    final AddressElementDtls dtls1 = new AddressElementDtls();
    dtls1.addressID = concernRoleDtls.primaryAddressID;
    dtls1.addressElementID = 1234;
    dtls1.elementType = BDMConstants.kADDRESSELEMENT_POBOX;
    dtls1.elementValue = "pobox 1";
    dtls1.upperElementValue = "1";
    AddressElementFactory.newInstance().insert(dtls1);

    final AddressElementDtls dtls2 = new AddressElementDtls();
    dtls2.addressID = concernRoleDtls.primaryAddressID;
    dtls2.addressElementID = 1235;
    dtls2.elementType = BDMConstants.kADDRESSELEMENT_APTUNITNUM;
    dtls2.elementValue = "apt 1";
    dtls2.upperElementValue = "1";
    AddressElementFactory.newInstance().insert(dtls2);

    final String[] result = bdmCommunicationHelper
      .getAddressMapper(concernRoleDtls.primaryAddressID);

    assertEquals(result.length, 5);
    assertEquals("apt 1", dtls2.elementValue);
  }

  // failing... fix me
  @Test
  public void testGetAddressMapper_US() throws Exception {

    // testCreateAndAssignUserForPosition();

    MockLogin.getInst().login("unauthenticated");

    final PDCPersonDetails person = createPDCPersonCustomAddress("US");
    final ConcernRole concernRole = ConcernRoleFactory.newInstance();
    final ConcernRoleKey key = new ConcernRoleKey();
    key.concernRoleID = person.concernRoleID;
    final ConcernRoleDtls concernRoleDtls = concernRole.read(key);

    final AddressElementDtls dtls1 = new AddressElementDtls();
    dtls1.addressID = concernRoleDtls.primaryAddressID;
    dtls1.addressElementID = 1234;
    dtls1.elementType = BDMConstants.kADDRESSELEMENT_POBOX;
    dtls1.elementValue = "1";
    dtls1.upperElementValue = "1";
    AddressElementFactory.newInstance().insert(dtls1);

    final AddressElementDtls dtls2 = new AddressElementDtls();
    dtls2.addressID = concernRoleDtls.primaryAddressID;
    dtls2.addressElementID = 1235;
    dtls2.elementType = BDMConstants.kADDRESSELEMENT_APTUNITNUM;
    dtls2.elementValue = "1";
    dtls2.upperElementValue = "1";
    AddressElementFactory.newInstance().insert(dtls2);

    final AddressElementDtls dtls3 = new AddressElementDtls();
    dtls3.addressID = concernRoleDtls.primaryAddressID;
    dtls3.addressElementID = 1236;
    dtls3.elementType = BDMConstants.kADDRESSELEMENT_STATEPROV;
    dtls3.elementValue = "1";
    dtls3.upperElementValue = "1";
    AddressElementFactory.newInstance().insert(dtls3);

    final String[] result = bdmCommunicationHelper
      .getAddressMapper(concernRoleDtls.primaryAddressID);

    assertEquals(5, result.length);
    assertEquals("San Jose 1 95125", result[2]);
  }

  @Test
  public void testGetAddressMapper_INTL() throws Exception {

    // testCreateAndAssignUserForPosition();

    MockLogin.getInst().login("unauthenticated");

    final PDCPersonDetails person = createPDCPersonCustomAddress("INTL");
    final ConcernRole concernRole = ConcernRoleFactory.newInstance();
    final ConcernRoleKey key = new ConcernRoleKey();
    key.concernRoleID = person.concernRoleID;
    final ConcernRoleDtls concernRoleDtls = concernRole.read(key);

    final String[] result = bdmCommunicationHelper
      .getAddressMapper(concernRoleDtls.primaryAddressID);
    assertEquals("95/2-Ve Struhach", result[0]);
  }

  @Test
  public void testGetAddressIDforConcern() throws Exception {

    MockLogin.getInst().login("unauthenticated");

    final PDCPersonDetails person = createPDCPerson(BDMLANGUAGE.FRENCHL);
    final long addressID = bdmCommunicationHelper
      .getAddressIDforConcern(person.concernRoleID, false);
    assertTrue(addressID != 0);
  }

  @Test
  public void testGetAddressIDforConcern_other() throws Exception {

    MockLogin.getInst().login("unauthenticated");

    final PDCPersonDetails person = createPDCPerson(BDMLANGUAGE.FRENCHL);
    final long addressID = bdmCommunicationHelper
      .getAddressIDforConcern(person.concernRoleID, false);
    assertTrue(addressID != 0);
  }

  @Test
  public void testCreateStatusChangeTask() throws Exception {

    MockLogin.getInst().login("unauthenticated");

    final BDMCommunicationTest bdmCommunicationTest =
      new BDMCommunicationTest();

    // input parameters
    final ConcernRoleCommunicationDtls commDtls =
      new ConcernRoleCommunicationDtls();
    final BDMConcernRoleCommunicationDtls bdmCrDtls =
      new BDMConcernRoleCommunicationDtls();

    final PDCPersonDetails person = createPDCPerson(BDMLANGUAGE.FRENCHL);

    commDtls.correspondentConcernRoleID = person.concernRoleID;
    commDtls.communicationID = bdmCommunicationTest.createTestCommunication();

    bdmCommunicationHelper.createStatusChangeTask(commDtls, bdmCrDtls);

    // ADD ASERT STATTEMENT
    final BizObjAssocCountOpenTasksKey openTasksKey =
      new BizObjAssocCountOpenTasksKey();
    openTasksKey.bizObjectID = commDtls.communicationID;
    openTasksKey.bizObjectType = BUSINESSOBJECTTYPE.BDMCOMMUNICATION;
    openTasksKey.closedTaskStatusToIgnore = TASKSTATUS.CLOSED;
    openTasksKey.completedTaskStatusToIgnore = TASKSTATUS.COMPLETED;
    final BizObjAssociation bizObjAssociationObj =
      BizObjAssociationFactory.newInstance();
    final TaskCount taskCount =
      bizObjAssociationObj.countOpenTasksByBizObjectTypeAndID(openTasksKey);
    // If there's a task already for this communication, return

    assertTrue(curam.core.impl.CuramConst.gkZero < taskCount.count);

  }

  @Test
  public void testCreateStatusChangeTaskFECase() throws Exception {

    MockLogin.getInst().login("unauthenticated");

    final BDMCommunicationTest bdmCommunicationTest =
      new BDMCommunicationTest();

    // input parameters
    final ConcernRoleCommunicationDtls commDtls =
      new ConcernRoleCommunicationDtls();
    final BDMConcernRoleCommunicationDtls bdmCrDtls =
      new BDMConcernRoleCommunicationDtls();

    final PDCPersonDetails person = createPDCPerson(BDMLANGUAGE.FRENCHL);

    commDtls.correspondentConcernRoleID = person.concernRoleID;
    commDtls.communicationID = bdmCommunicationTest.createTestCommunication();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.concernRoleID);

    final long caseID = icResult.createCaseResult.integratedCaseID;

    commDtls.caseID = caseID;

    bdmCommunicationHelper.createStatusChangeTask(commDtls, bdmCrDtls);

    // ADD ASERT STATTEMENT
    final BizObjAssocCountOpenTasksKey openTasksKey =
      new BizObjAssocCountOpenTasksKey();
    openTasksKey.bizObjectID = commDtls.communicationID;
    openTasksKey.bizObjectType = BUSINESSOBJECTTYPE.BDMCOMMUNICATION;
    openTasksKey.closedTaskStatusToIgnore = TASKSTATUS.CLOSED;
    openTasksKey.completedTaskStatusToIgnore = TASKSTATUS.COMPLETED;
    final BizObjAssociation bizObjAssociationObj =
      BizObjAssociationFactory.newInstance();
    final TaskCount taskCount =
      bizObjAssociationObj.countOpenTasksByBizObjectTypeAndID(openTasksKey);
    // If there's a task already for this communication, return

    assertTrue(curam.core.impl.CuramConst.gkZero < taskCount.count);

  }

  // NEEDS WORK
  @Test
  public void testCreateStatusChangeTasknfIndicatorNotFound()
    throws Exception {

    MockLogin.getInst().login("unauthenticated");
    final BDMCommunicationTest bdmCommunicationTest =
      new BDMCommunicationTest();
    // input parameters
    final ConcernRoleCommunicationDtls commDtls =
      new ConcernRoleCommunicationDtls();
    final BDMConcernRoleCommunicationDtls bdmCrDtls =
      new BDMConcernRoleCommunicationDtls();
    final PDCPersonDetails person = createPDCPerson(BDMLANGUAGE.FRENCHL);
    commDtls.correspondentConcernRoleID = person.concernRoleID;
    commDtls.communicationID = bdmCommunicationTest.createTestCommunication();
    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.concernRoleID);
    final long caseID = icResult.createCaseResult.integratedCaseID;
    final BDMFECaseKey feCaseKey = new BDMFECaseKey();
    final BDMFECaseDtls details = new BDMFECaseDtls();
    details.countryCode = "ABC123";
    details.caseID = caseID;
    feCaseKey.caseID = caseID;
    BDMFECaseFactory.newInstance().modify(feCaseKey, details);
    commDtls.caseID = caseID;
    bdmCommunicationHelper.createStatusChangeTask(commDtls, bdmCrDtls);
    // ADD ASSERT STATEMENT
    final BizObjAssocCountOpenTasksKey openTasksKey =
      new BizObjAssocCountOpenTasksKey();
    openTasksKey.bizObjectID = commDtls.communicationID;
    openTasksKey.bizObjectType = BUSINESSOBJECTTYPE.BDMCOMMUNICATION;
    openTasksKey.closedTaskStatusToIgnore = TASKSTATUS.CLOSED;
    openTasksKey.completedTaskStatusToIgnore = TASKSTATUS.COMPLETED;
    final BizObjAssociation bizObjAssociationObj =
      BizObjAssociationFactory.newInstance();
    final TaskCount taskCount =
      bizObjAssociationObj.countOpenTasksByBizObjectTypeAndID(openTasksKey);
    // If there's a task already for this communication, return
    assertTrue(curam.core.impl.CuramConst.gkZero < taskCount.count);
  }

  @Test
  public void testGetConcernRoleNameForLetter() throws Exception {

    // testCreateAndAssignUserForPosition();

    MockLogin.getInst().login("unauthenticated");

    final PDCPersonDetails personDtls = createPDCPerson(BDMLANGUAGE.FRENCHL);
    final String concernRoleName = bdmCommunicationHelper
      .getConcernRoleNameForLetter(personDtls.concernRoleID);
    assertTrue(concernRoleName.length() != CuramConst.gkZero);

  }

  @Test
  public void testGetConcernRoleName() throws Exception {

    // testCreateAndAssignUserForPosition();

    MockLogin.getInst().login("unauthenticated");

    final BDMCommunicationHelper bdmCommunicationHelper =
      new BDMCommunicationHelper();

    final BDMViewCorrespondenceDetails viewDetails =
      new BDMViewCorrespondenceDetails();

    final BDMCommunicationTest bdmCommunicationTest =
      new BDMCommunicationTest();

    final PDCPersonDetails personDtls =
      bdmCommunicationTest.createPDCPerson();

    viewDetails.clientName =
      bdmCommunicationHelper.getConcernRoleName(personDtls.concernRoleID);

    assertTrue(viewDetails.clientName.length() != CuramConst.gkZero);

  }

  @Test
  public void testGetConcernRoleNameLongName() throws Exception {

    // testCreateAndAssignUserForPosition();

    MockLogin.getInst().login("unauthenticated");

    final PersonRegistrationDetails personRegistrationDetails =
      getPersonRegistrationDetailsPersonLongName();

    final BDMCommunicationHelper bdmCommunicationHelper =
      new BDMCommunicationHelper();

    final BDMViewCorrespondenceDetails viewDetails =
      new BDMViewCorrespondenceDetails();

    final BDMCommunicationTest bdmCommunicationTest =
      new BDMCommunicationTest();

    final PDCPersonDetails personDtls = createPDCPersonLongName();

    viewDetails.clientName =
      bdmCommunicationHelper.getConcernRoleName(personDtls.concernRoleID);

    System.out.println(viewDetails.clientName);

    assertTrue(viewDetails.clientName.length() != CuramConst.gkZero);
  }

  @Test
  public void testGetConcernRoleNameNotPerson() throws Exception {

    // testCreateAndAssignUserForPosition();

    MockLogin.getInst().login("unauthenticated");

    final BDMCommunicationHelper bdmCommunicationHelper =
      new BDMCommunicationHelper();

    final BDMViewCorrespondenceDetails viewDetails =
      new BDMViewCorrespondenceDetails();

    final BDMCommunicationTest bdmCommunicationTest =
      new BDMCommunicationTest();

    final long externalID = createExternalParty_ThirdParty();

    viewDetails.clientName =
      bdmCommunicationHelper.getConcernRoleName(externalID);

    assertTrue(viewDetails.clientName.length() != CuramConst.gkZero);
  }

  @Test
  public void testGetConcernRoleNameNotPersonLongName() throws Exception {

    // testCreateAndAssignUserForPosition();

    MockLogin.getInst().login("unauthenticated");

    final BDMExternalPartyTestData extPartyTestDataObj =
      new BDMExternalPartyTestData();

    final BDMExternalPartyTestDataDetails extPartyTestDataDetails =
      extPartyTestDataObj.getRegisterExtPartySearchCriteria_Empty();

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = extPartyTestDataDetails.wizardStateID;

    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = CuramConst.kSaveAction;

    final BDMExternalPartyRegistrationDetails bdmextPartyRegDetails =
      extPartyTestDataObj.getExternalPartyRegistrationCustomDetails();

    final ExternalPartyRegistrationDetails registrationDtls =
      getExternalPartyRegistrationDetails_DefaultLongName();

    extPartyTestDataDetails.extPartyName =
      registrationDtls.externalPartyRegistrationDetails.externalPartyDtls.name;

    registrationDtls.externalPartyRegistrationDetails.externalPartyDtls.type =
      EXTERNALPARTYTYPE.THIRDPARTY;

    registrationDtls.externalPartyRegistrationDetails.preferredLanguage =
      LANGUAGE.ENGLISH;

    final ParticipantRegistrationWizardResult wizardResult =
      bdmExternalPartyFacade.setRegisterExternalPartyDetails(registrationDtls,
        wizardStateID, actionIDProperty, bdmextPartyRegDetails);

    final BDMCommunicationHelper bdmCommunicationHelper =
      new BDMCommunicationHelper();

    final BDMViewCorrespondenceDetails viewDetails =
      new BDMViewCorrespondenceDetails();

    final BDMCommunicationTest bdmCommunicationTest =
      new BDMCommunicationTest();

    viewDetails.clientName = bdmCommunicationHelper
      .getConcernRoleName(wizardResult.registrationResult.concernRoleID);

    assertTrue(viewDetails.clientName.length() != CuramConst.gkZero);
  }

  @Test
  public void testDetermineActionMenuIndicator_Draft() throws Exception {

    final BDMCommunicationAndListRowActionDetails bdmCommAndListRowActionDtls =
      new BDMCommunicationAndListRowActionDetails();
    bdmCommAndListRowActionDtls.commDtls.communicationFormat =
      COMMUNICATIONFORMAT.CORRESPONDENCE;
    bdmCommAndListRowActionDtls.commDtls.communicationStatus =
      COMMUNICATIONSTATUS.DRAFT;
    bdmCommunicationHelper
      .determineActionMenuIndicator(bdmCommAndListRowActionDtls);
    testAssertDraftEquivalentStatus(bdmCommAndListRowActionDtls);

  }

  @Test
  public void testDetermineActionMenuIndicator_Submitted() throws Exception {

    final BDMCommunicationAndListRowActionDetails bdmCommAndListRowActionDtls =
      new BDMCommunicationAndListRowActionDetails();
    bdmCommAndListRowActionDtls.commDtls.communicationFormat =
      COMMUNICATIONFORMAT.CORRESPONDENCE;
    bdmCommAndListRowActionDtls.commDtls.communicationStatus =
      COMMUNICATIONSTATUS.SUBMITTED;
    bdmCommunicationHelper
      .determineActionMenuIndicator(bdmCommAndListRowActionDtls);
    testAssertDraftEquivalentStatus(bdmCommAndListRowActionDtls);

  }

  @Test
  public void testDetermineActionMenuIndicator_underReview()
    throws Exception {

    final BDMCommunicationAndListRowActionDetails bdmCommAndListRowActionDtls =
      new BDMCommunicationAndListRowActionDetails();
    bdmCommAndListRowActionDtls.commDtls.communicationFormat =
      COMMUNICATIONFORMAT.CORRESPONDENCE;
    bdmCommAndListRowActionDtls.commDtls.communicationStatus =
      COMMUNICATIONSTATUS.UNDER_REVIEW;
    bdmCommunicationHelper
      .determineActionMenuIndicator(bdmCommAndListRowActionDtls);
    testAssertDraftEquivalentStatus(bdmCommAndListRowActionDtls);

  }

  @Test
  public void testDetermineActionMenuIndicator_QARejected() throws Exception {

    final BDMCommunicationAndListRowActionDetails bdmCommAndListRowActionDtls =
      new BDMCommunicationAndListRowActionDetails();
    bdmCommAndListRowActionDtls.commDtls.communicationFormat =
      COMMUNICATIONFORMAT.CORRESPONDENCE;
    bdmCommAndListRowActionDtls.commDtls.communicationStatus =
      COMMUNICATIONSTATUS.QA_REJECTED;
    bdmCommunicationHelper
      .determineActionMenuIndicator(bdmCommAndListRowActionDtls);
    testAssertDraftEquivalentStatus(bdmCommAndListRowActionDtls);

  }

  @Test
  public void testDetermineActionMenuIndicator_failedDelivery()
    throws Exception {

    final BDMCommunicationAndListRowActionDetails bdmCommAndListRowActionDtls =
      new BDMCommunicationAndListRowActionDetails();
    bdmCommAndListRowActionDtls.commDtls.communicationFormat =
      COMMUNICATIONFORMAT.CORRESPONDENCE;
    bdmCommAndListRowActionDtls.commDtls.communicationStatus =
      COMMUNICATIONSTATUS.DRAFT;
    bdmCommunicationHelper
      .determineActionMenuIndicator(bdmCommAndListRowActionDtls);
    testAssertDraftEquivalentStatus(bdmCommAndListRowActionDtls);

  }

  @Test
  public void testDetermineActionMenuIndicator_sent() throws Exception {

    final BDMCommunicationAndListRowActionDetails bdmCommAndListRowActionDtls =
      new BDMCommunicationAndListRowActionDetails();
    bdmCommAndListRowActionDtls.commDtls.communicationFormat =
      COMMUNICATIONFORMAT.CORRESPONDENCE;
    bdmCommAndListRowActionDtls.commDtls.communicationStatus =
      COMMUNICATIONSTATUS.SENT;
    bdmCommunicationHelper
      .determineActionMenuIndicator(bdmCommAndListRowActionDtls);
    testAssertSentEquivalentStatus(bdmCommAndListRowActionDtls);

  }

  @Test
  public void testDetermineActionMenuIndicator_returned() throws Exception {

    final BDMCommunicationAndListRowActionDetails bdmCommAndListRowActionDtls =
      new BDMCommunicationAndListRowActionDetails();
    bdmCommAndListRowActionDtls.commDtls.communicationFormat =
      COMMUNICATIONFORMAT.CORRESPONDENCE;
    bdmCommAndListRowActionDtls.commDtls.communicationStatus =
      COMMUNICATIONSTATUS.RETURNED;
    bdmCommunicationHelper
      .determineActionMenuIndicator(bdmCommAndListRowActionDtls);
    testAssertSentEquivalentStatus(bdmCommAndListRowActionDtls);

  }

  @Test
  public void testDetermineActionMenuIndicator_void() throws Exception {

    final BDMCommunicationAndListRowActionDetails bdmCommAndListRowActionDtls =
      new BDMCommunicationAndListRowActionDetails();
    bdmCommAndListRowActionDtls.commDtls.communicationFormat =
      COMMUNICATIONFORMAT.CORRESPONDENCE;
    bdmCommAndListRowActionDtls.commDtls.communicationStatus =
      COMMUNICATIONSTATUS.VOID;
    bdmCommunicationHelper
      .determineActionMenuIndicator(bdmCommAndListRowActionDtls);
    testAssertSentEquivalentStatus(bdmCommAndListRowActionDtls);

  }

  @Test
  public void testDetermineActionMenuIndicator_misdirected()
    throws Exception {

    final BDMCommunicationAndListRowActionDetails bdmCommAndListRowActionDtls =
      new BDMCommunicationAndListRowActionDetails();
    bdmCommAndListRowActionDtls.commDtls.communicationFormat =
      COMMUNICATIONFORMAT.CORRESPONDENCE;
    bdmCommAndListRowActionDtls.commDtls.communicationStatus =
      COMMUNICATIONSTATUS.MISDIRECTED;
    bdmCommunicationHelper
      .determineActionMenuIndicator(bdmCommAndListRowActionDtls);
    testAssertSentEquivalentStatus(bdmCommAndListRowActionDtls);

  }

  @Test
  public void testDetermineActionMenuIndicator_suppressed() throws Exception {

    final BDMCommunicationAndListRowActionDetails bdmCommAndListRowActionDtls =
      new BDMCommunicationAndListRowActionDetails();
    bdmCommAndListRowActionDtls.commDtls.communicationFormat =
      COMMUNICATIONFORMAT.CORRESPONDENCE;
    bdmCommAndListRowActionDtls.commDtls.communicationStatus =
      COMMUNICATIONSTATUS.SUPPRESS;
    bdmCommunicationHelper
      .determineActionMenuIndicator(bdmCommAndListRowActionDtls);
    testAssertSentEquivalentStatus(bdmCommAndListRowActionDtls);

  }

  @Test
  public void testDetermineActionMenuIndicator_printFailed()
    throws Exception {

    final BDMCommunicationAndListRowActionDetails bdmCommAndListRowActionDtls =
      new BDMCommunicationAndListRowActionDetails();
    bdmCommAndListRowActionDtls.commDtls.communicationFormat =
      COMMUNICATIONFORMAT.CORRESPONDENCE;
    bdmCommAndListRowActionDtls.commDtls.communicationStatus =
      COMMUNICATIONSTATUS.PRINTFAILED;
    bdmCommunicationHelper
      .determineActionMenuIndicator(bdmCommAndListRowActionDtls);
    testAssertSentEquivalentStatus(bdmCommAndListRowActionDtls);

  }

  @Test
  public void testDetermineActionMenuIndicator_cancelled() throws Exception {

    final BDMCommunicationAndListRowActionDetails bdmCommAndListRowActionDtls =
      new BDMCommunicationAndListRowActionDetails();
    bdmCommAndListRowActionDtls.commDtls.communicationFormat =
      COMMUNICATIONFORMAT.CORRESPONDENCE;
    bdmCommAndListRowActionDtls.commDtls.communicationStatus =
      COMMUNICATIONSTATUS.CANCELLED;
    bdmCommunicationHelper
      .determineActionMenuIndicator(bdmCommAndListRowActionDtls);
    testAssertCancelledEquivalentStatus(bdmCommAndListRowActionDtls);

  }

  private void testAssertDraftEquivalentStatus(
    final BDMCommunicationAndListRowActionDetails bdmCommAndListRowActionDtls) {

    assertTrue(bdmCommAndListRowActionDtls.editActionInd);
    assertTrue(bdmCommAndListRowActionDtls.deleteActionInd);
    assertTrue(bdmCommAndListRowActionDtls.enableTrackingInd);
    assertFalse(bdmCommAndListRowActionDtls.modifyStatusActionInd);
    assertFalse(bdmCommAndListRowActionDtls.misdirectActionInd);
    assertFalse(bdmCommAndListRowActionDtls.previewActionInd);
    assertFalse(bdmCommAndListRowActionDtls.recallActionInd);
    assertFalse(bdmCommAndListRowActionDtls.resubmitActionInd);
    assertFalse(bdmCommAndListRowActionDtls.returnActionInd);
    assertFalse(bdmCommAndListRowActionDtls.returnedActionInd);
    assertFalse(bdmCommAndListRowActionDtls.sendNowActionInd);
    assertFalse(bdmCommAndListRowActionDtls.submitActionInd);
    assertFalse(bdmCommAndListRowActionDtls.viewActionInd);
    assertFalse(bdmCommAndListRowActionDtls.voidActionInd);
  }

  private void testAssertSentEquivalentStatus(
    final BDMCommunicationAndListRowActionDetails bdmCommAndListRowActionDtls) {

    assertFalse(bdmCommAndListRowActionDtls.editActionInd);
    assertFalse(bdmCommAndListRowActionDtls.deleteActionInd);
    assertTrue(bdmCommAndListRowActionDtls.enableTrackingInd);
    assertTrue(bdmCommAndListRowActionDtls.modifyStatusActionInd);
    assertFalse(bdmCommAndListRowActionDtls.misdirectActionInd);
    assertFalse(bdmCommAndListRowActionDtls.previewActionInd);
    assertFalse(bdmCommAndListRowActionDtls.recallActionInd);
    assertFalse(bdmCommAndListRowActionDtls.resubmitActionInd);
    assertFalse(bdmCommAndListRowActionDtls.returnActionInd);
    assertFalse(bdmCommAndListRowActionDtls.returnedActionInd);
    assertFalse(bdmCommAndListRowActionDtls.sendNowActionInd);
    assertFalse(bdmCommAndListRowActionDtls.submitActionInd);
    assertFalse(bdmCommAndListRowActionDtls.viewActionInd);
    assertFalse(bdmCommAndListRowActionDtls.voidActionInd);
  }

  private void testAssertCancelledEquivalentStatus(
    final BDMCommunicationAndListRowActionDetails bdmCommAndListRowActionDtls) {

    assertFalse(bdmCommAndListRowActionDtls.editActionInd);
    assertFalse(bdmCommAndListRowActionDtls.deleteActionInd);
    assertFalse(bdmCommAndListRowActionDtls.enableTrackingInd);
    assertFalse(bdmCommAndListRowActionDtls.modifyStatusActionInd);
    assertFalse(bdmCommAndListRowActionDtls.misdirectActionInd);
    assertFalse(bdmCommAndListRowActionDtls.previewActionInd);
    assertFalse(bdmCommAndListRowActionDtls.recallActionInd);
    assertFalse(bdmCommAndListRowActionDtls.resubmitActionInd);
    assertFalse(bdmCommAndListRowActionDtls.returnActionInd);
    assertFalse(bdmCommAndListRowActionDtls.returnedActionInd);
    assertFalse(bdmCommAndListRowActionDtls.sendNowActionInd);
    assertFalse(bdmCommAndListRowActionDtls.submitActionInd);
    assertFalse(bdmCommAndListRowActionDtls.viewActionInd);
    assertFalse(bdmCommAndListRowActionDtls.voidActionInd);
  }

  private ExternalPartyRegistrationDetails
    getExternalPartyRegistrationDetails_DefaultLongName()
      throws AppException, InformationalException {

    final BDMExternalPartyTestData extPartyTestDataObj =
      new BDMExternalPartyTestData();

    final ExternalPartyRegistrationDetails extPartyRegistrationDetails =
      new ExternalPartyRegistrationDetails();

    extPartyRegistrationDetails.externalPartyRegistrationDetails.externalPartyDtls.name =
      "Thisistomakeafortycharacterlongnameisandismuchlongerthan40";

    extPartyRegistrationDetails.externalPartyRegistrationDetails.externalPartyDtls.type =
      EXTERNALPARTYTYPE.SSACOUNTRY;
    extPartyRegistrationDetails.externalPartyRegistrationDetails.registrationDate =
      Date.getCurrentDate();

    final OtherAddressData otherAddressData =
      extPartyTestDataObj.getInternationAddressForExtParty();

    extPartyRegistrationDetails.externalPartyRegistrationDetails.addressData =
      otherAddressData.addressData;

    return extPartyRegistrationDetails;
  }

  private long createExternalParty_ThirdParty()
    throws AppException, InformationalException {

    final BDMExternalPartyTestData extPartyTestDataObj =
      new BDMExternalPartyTestData();

    final BDMExternalPartyTestDataDetails extPartyTestDataDetails =
      extPartyTestDataObj.getRegisterExtPartySearchCriteria_Empty();

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = extPartyTestDataDetails.wizardStateID;

    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = CuramConst.kSaveAction;

    final BDMExternalPartyRegistrationDetails bdmextPartyRegDetails =
      extPartyTestDataObj.getExternalPartyRegistrationCustomDetails();

    final ExternalPartyRegistrationDetails registrationDtls =
      extPartyTestDataObj.getExternalPartyRegistrationDetails_Default();

    extPartyTestDataDetails.extPartyName =
      registrationDtls.externalPartyRegistrationDetails.externalPartyDtls.name;

    registrationDtls.externalPartyRegistrationDetails.externalPartyDtls.type =
      EXTERNALPARTYTYPE.THIRDPARTY;

    registrationDtls.externalPartyRegistrationDetails.preferredLanguage =
      LANGUAGE.ENGLISH;

    final ParticipantRegistrationWizardResult wizardResult =
      bdmExternalPartyFacade.setRegisterExternalPartyDetails(registrationDtls,
        wizardStateID, actionIDProperty, bdmextPartyRegDetails);

    return wizardResult.registrationResult.concernRoleID;

  }

  long createExternalParty_ThirdParty(final String language)
    throws AppException, InformationalException {

    final BDMExternalPartyTestData extPartyTestDataObj =
      new BDMExternalPartyTestData();

    final BDMExternalPartyTestDataDetails extPartyTestDataDetails =
      extPartyTestDataObj.getRegisterExtPartySearchCriteria_Empty();

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = extPartyTestDataDetails.wizardStateID;

    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = CuramConst.kSaveAction;

    final BDMExternalPartyRegistrationDetails bdmextPartyRegDetails =
      extPartyTestDataObj.getExternalPartyRegistrationCustomDetails();

    final ExternalPartyRegistrationDetails registrationDtls =
      extPartyTestDataObj.getExternalPartyRegistrationDetails_Default();

    extPartyTestDataDetails.extPartyName =
      registrationDtls.externalPartyRegistrationDetails.externalPartyDtls.name;

    registrationDtls.externalPartyRegistrationDetails.externalPartyDtls.type =
      EXTERNALPARTYTYPE.THIRDPARTY;

    registrationDtls.externalPartyRegistrationDetails.preferredLanguage =
      language;

    final ParticipantRegistrationWizardResult wizardResult =
      bdmExternalPartyFacade.setRegisterExternalPartyDetails(registrationDtls,
        wizardStateID, actionIDProperty, bdmextPartyRegDetails);

    return wizardResult.registrationResult.concernRoleID;
  }

  public PDCPersonDetails createPDCPerson(final String language)
    throws AppException, InformationalException {

    final BDMUtil bdmUtil = new BDMUtil();

    final RegisterPerson registerPerson =
      new RegisterPerson("RegisterPerson");

    final PersonRegistrationDetails personRegistrationDetails =
      registerPerson.getPersonRegistrationDetails();

    final PersonRegistration personRegistration =
      PersonRegistrationFactory.newInstance();

    final RegistrationIDDetails registrationIDDetails =
      personRegistration.registerPerson(personRegistrationDetails);

    bdmUtil.createContactPreferenceEvidence(
      registrationIDDetails.concernRoleID, language, language,
      BDMConstants.EMPTY_STRING);

    final PersonDtls personDtls =
      registerPerson.readPerson(registrationIDDetails.concernRoleID);

    final PDCPersonDetails pdcPersonDetails = new PDCPersonDetails();

    pdcPersonDetails.assign(personDtls);
    return pdcPersonDetails;

  }

  public PDCPersonDetails createPDCPersonCustomAddress(final String address)
    throws AppException, InformationalException {

    final BDMUtil bdmUtil = new BDMUtil();

    final RegisterPerson registerPerson =
      new RegisterPerson("RegisterPerson");

    PersonRegistrationDetails personRegistrationDetails =
      new PersonRegistrationDetails();

    if (address.equalsIgnoreCase("US")) {
      personRegistrationDetails = getPersonRegistrationDetailsUS();
    } else if (address.equalsIgnoreCase("INTL")) {
      personRegistrationDetails = getPersonRegistrationDetailsINTL();
    }

    final PersonRegistration personRegistration =
      PersonRegistrationFactory.newInstance();

    final RegistrationIDDetails registrationIDDetails =
      personRegistration.registerPerson(personRegistrationDetails);

    bdmUtil.createContactPreferenceEvidence(
      registrationIDDetails.concernRoleID, BDMLANGUAGE.ENGLISHL,
      BDMLANGUAGE.ENGLISHL, BDMConstants.EMPTY_STRING);

    final PersonDtls personDtls =
      registerPerson.readPerson(registrationIDDetails.concernRoleID);

    final PDCPersonDetails pdcPersonDetails = new PDCPersonDetails();

    pdcPersonDetails.assign(personDtls);
    return pdcPersonDetails;

  }

  public PersonRegistrationDetails getPersonRegistrationDetailsUS()
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
      curam.codetable.ADDRESSLAYOUTTYPE.US;

    addressFieldDetails.suiteNum = "2025";
    addressFieldDetails.addressLine1 = "Hamilton Avenue";
    addressFieldDetails.addressLine2 = "San Jose";
    addressFieldDetails.stateProvince = "CA";
    addressFieldDetails.city = "San Jose";

    addressFieldDetails.zipCode = "95125";

    addressFieldDetails.countryCode = curam.codetable.COUNTRY.US;

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
      curam.codetable.ADDRESSLAYOUTTYPE.US;
    mailingAddressFieldDetails.suiteNum = "2025";
    mailingAddressFieldDetails.addressLine1 = "Hamilton Avenue";
    mailingAddressFieldDetails.addressLine2 = "San Jose";
    mailingAddressFieldDetails.stateProvince = "CA";
    mailingAddressFieldDetails.city = "San Jose";
    mailingAddressFieldDetails.zipCode = "95125";
    mailingAddressFieldDetails.countryCode = curam.codetable.COUNTRY.US;

    final OtherAddressData mailingAddressData =
      addressDataObj.parseFieldsToData(mailingAddressFieldDetails);

    personRegistrationDetails.mailingAddressData =
      mailingAddressData.addressData;

    // Add email address details
    personRegistrationDetails.contactEmailAddress = "test@gmail.com";
    personRegistrationDetails.contactEmailType = EMAILTYPE.PERSONAL;

    return personRegistrationDetails;
  }

  public PersonRegistrationDetails getPersonRegistrationDetailsINTL()
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

    personRegistrationDetails.sex = curam.codetable.GENDER.MALE;
    personRegistrationDetails.dateOfBirth = birthday;
    personRegistrationDetails.registrationDate =
      curam.util.type.Date.getCurrentDate();
    personRegistrationDetails.currentMaritalStatus =
      curam.codetable.MARITALSTATUS.MARRIED;
    personRegistrationDetails.nationality =
      curam.codetable.NATIONALITY.DEFAULTCODE;
    personRegistrationDetails.birthCountry = curam.codetable.COUNTRY.GB;

    personRegistrationDetails.addressType =
      CONCERNROLEADDRESSTYPE.PRIVATE.toString();

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;

    addressFieldDetails.suiteNum = "95/2";
    addressFieldDetails.addressLine1 = "Ve Struhach";
    addressFieldDetails.addressLine2 = "Praha";
    addressFieldDetails.stateProvince = "CZ";
    addressFieldDetails.city = "Praha";
    addressFieldDetails.zipCode = "16000";

    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CZECH_REPUBLIC;

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
      curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;
    mailingAddressFieldDetails.suiteNum = "95/2";
    mailingAddressFieldDetails.addressLine1 = "Ve Struhach";
    mailingAddressFieldDetails.addressLine2 = "Praha";
    mailingAddressFieldDetails.stateProvince = "CA";
    mailingAddressFieldDetails.city = "Praha";
    mailingAddressFieldDetails.zipCode = "160000";
    mailingAddressFieldDetails.countryCode =
      curam.codetable.COUNTRY.CZECH_REPUBLIC;

    final OtherAddressData mailingAddressData =
      addressDataObj.parseFieldsToData(mailingAddressFieldDetails);

    personRegistrationDetails.mailingAddressData =
      mailingAddressData.addressData;

    // Add email address details
    personRegistrationDetails.contactEmailAddress = "test@gmail.com";
    personRegistrationDetails.contactEmailType = EMAILTYPE.PERSONAL;

    return personRegistrationDetails;
  }

  public PersonRegistrationDetails getPersonRegistrationDetailsNoAddress()
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
      curam.codetable.ADDRESSLAYOUTTYPE.US;

    addressFieldDetails.suiteNum = "2025";
    addressFieldDetails.addressLine1 = "Hamilton Avenue";
    addressFieldDetails.addressLine2 = "San Jose";
    addressFieldDetails.stateProvince = "CA";
    addressFieldDetails.city = "San Jose";

    addressFieldDetails.zipCode = "95125";

    addressFieldDetails.countryCode = curam.codetable.COUNTRY.US;

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
      curam.codetable.ADDRESSLAYOUTTYPE.US;
    mailingAddressFieldDetails.suiteNum = "2025";
    mailingAddressFieldDetails.addressLine1 = "Hamilton Avenue";
    mailingAddressFieldDetails.addressLine2 = "San Jose";
    mailingAddressFieldDetails.stateProvince = "CA";
    mailingAddressFieldDetails.city = "San Jose";
    mailingAddressFieldDetails.zipCode = "95125";
    mailingAddressFieldDetails.countryCode = curam.codetable.COUNTRY.US;

    final OtherAddressData mailingAddressData =
      addressDataObj.parseFieldsToData(mailingAddressFieldDetails);

    personRegistrationDetails.mailingAddressData =
      mailingAddressData.addressData;

    // Add email address details
    personRegistrationDetails.contactEmailAddress = "test@gmail.com";
    personRegistrationDetails.contactEmailType = EMAILTYPE.PERSONAL;

    return personRegistrationDetails;
  }

  public void testCreateAndAssignUserForPosition() throws Exception {

    BDMOrganization testSubject;
    final BDMCreateAndAssignUser details = new BDMCreateAndAssignUser();

    details.userLangList = BDMLANGUAGE.ENGLISHL + "\t" + BDMLANGUAGE.FRENCHL;
    details.createUserDtls.createUserDetails.userName = "unauthenticated";
    details.createUserDtls.createUserDetails.accountEnabled = true;
    details.createUserDtls.createUserDetails.roleName = "SUPERROLE";
    details.createUserDtls.createUserDetails.fullName = "TEST USER";
    details.createUserDtls.createUserDetails.firstname = "TEST";
    details.createUserDtls.createUserDetails.surname = "USER";
    details.createUserDtls.createUserDetails.locationID = 1;
    details.createUserDtls.createUserDetails.sensitivity = "1";
    details.createUserDtls.createUserDetails.password = "password";
    details.createUserDtls.createUserDetails.passwordConfirm = "password";

    details.createUserDtls.assignDetails.organisationID = 80000;
    details.createUserDtls.assignDetails.positionHolderLinkDetails.positionHolderLinkDtls.fromDate =
      Date.getCurrentDate();
    details.createUserDtls.assignDetails.positionHolderLinkDetails.positionHolderLinkDtls.organisationStructureID =
      1;
    details.createUserDtls.assignDetails.positionHolderLinkDetails.positionHolderLinkDtls.positionID =
      80000;

    details.createUserDtls.assignDetails.positionHolderLinkDetails.positionHolderLinkDtls.recordStatus =
      "RST1";

    details.createUserDtls.assignDetails.positionHolderLinkDetails.positionHolderLinkDtls.userName =
      "unauthenticated";

    // default test
    testSubject = BDMOrganizationFactory.newInstance();
    testSubject.createAndAssignUserForPosition(details);
  }

  private PersonRegistrationDetails
    getPersonRegistrationDetailsPersonLongName()
      throws AppException, InformationalException {

    final java.util.Calendar cal = new java.util.GregorianCalendar(1970,
      java.util.Calendar.APRIL, 1, 0, 0, 0);
    final curam.util.type.Date birthday = new curam.util.type.Date(cal);
    final PersonRegistrationDetails personRegistrationDetails =
      new PersonRegistrationDetails();
    personRegistrationDetails.firstForename =
      "Thisisareallylongnamesothatwecanreachthe40charactermark";
    personRegistrationDetails.surname = "Person";
    personRegistrationDetails.birthName = "BirthName";
    personRegistrationDetails.motherBirthSurname = "ParentName";
    personRegistrationDetails.otherForename = "otherName";
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
    addressFieldDetails.suiteNum = "1-4994";
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

  private PDCPersonDetails createPDCPersonLongName()
    throws AppException, InformationalException {

    final BDMUtil bdmUtil = new BDMUtil();

    final RegisterPerson registerPerson =
      new RegisterPerson("RegisterPerson");

    final PersonRegistrationDetails personRegistrationDetails =
      getPersonRegistrationDetailsPersonLongName();

    final PersonRegistration personRegistration =
      PersonRegistrationFactory.newInstance();

    final RegistrationIDDetails registrationIDDetails =
      personRegistration.registerPerson(personRegistrationDetails);

    bdmUtil.createContactPreferenceEvidence(
      registrationIDDetails.concernRoleID, BDMLANGUAGE.ENGLISHL,
      BDMLANGUAGE.ENGLISHL, BDMConstants.EMPTY_STRING);

    final PersonDtls personDtls =
      registerPerson.readPerson(registrationIDDetails.concernRoleID);

    final PDCPersonDetails pdcPersonDetails = new PDCPersonDetails();

    pdcPersonDetails.assign(personDtls);
    return pdcPersonDetails;
  }

}
