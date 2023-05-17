package curam.ca.gc.bdm.sl.manageprivacyrequest;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.facade.externalparty.fact.BDMExternalPartyFactory;
import curam.ca.gc.bdm.facade.externalparty.intf.BDMExternalParty;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMExternalPartyRegistrationDetails;
import curam.ca.gc.bdm.facade.fec.fact.BDMForeignEngagementCaseFactory;
import curam.ca.gc.bdm.facade.fec.struct.BDMFECaseDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMPersonRegistrationDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.sl.communication.fact.BDMCommunicationFactory;
import curam.ca.gc.bdm.sl.communication.intf.BDMCommunication;
import curam.ca.gc.bdm.test.data.impl.BDMExternalPartyTestDataDetails;
import curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.codetable.CASEPRIORITY;
import curam.codetable.COMMUNICATIONDIRECTION;
import curam.codetable.COMMUNICATIONMETHOD;
import curam.codetable.COMMUNICATIONTYPE;
import curam.codetable.PROVINCETYPE;
import curam.codetable.TARGETITEMTYPE;
import curam.codetable.TASKPRIORITY;
import curam.codetable.TASKSTATUS;
import curam.core.facade.fact.PersonFactory;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.facade.struct.CreateIntegratedCaseResultAndMessages;
import curam.core.facade.struct.ExternalPartyRegistrationDetails;
import curam.core.facade.struct.ParticipantRegistrationWizardResult;
import curam.core.facade.struct.PersonRegistrationDetails;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.impl.CuramConst;
import curam.core.sl.entity.fact.TaskAssignmentFactory;
import curam.core.sl.entity.struct.TaskAssignmentDtls;
import curam.core.sl.struct.RecordedCommDetails1;
import curam.core.sl.struct.RecordedCommKey;
import curam.core.sl.struct.WizardStateID;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.ConcernRoleCommKeyOut;
import curam.core.struct.OtherAddressData;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.UniqueID;
import curam.util.workflow.fact.BizObjAssociationFactory;
import curam.util.workflow.fact.TaskFactory;
import curam.util.workflow.fact.WorkflowDeadlineFactory;
import curam.util.workflow.struct.BizObjAssociationDtls;
import curam.util.workflow.struct.BizObjectTypeKey;
import curam.util.workflow.struct.TaskDtls;
import curam.util.workflow.struct.WorkflowDeadlineDtls;
import data.impl.BDMExternalPartyTestData;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)
public class BDMManagePersonPrivacyRequestTest extends CuramServerTestJUnit4 {

  final BDMEvidenceUtilsTest bdmEvidenceUtils = new BDMEvidenceUtilsTest();

  @Mocked
  BDMUtil BDMUTIL;

  /**
  *
  */
  @SuppressWarnings("unused")
  private void getSINNumberSticExpectation() {

    try {
      new Expectations() {

        {

          BDMUTIL.getSINNumberForPerson((long) any);
          result = "337002570";
        }
      };
    } catch (final AppException e) {

      e.printStackTrace();
    } catch (final InformationalException e) {

      e.printStackTrace();
    }
  }

  /**
   * Test the create, modify and read privacy request tier 1 details for the
   * person.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testPersonManagePrivacyRequestTier1()
    throws AppException, InformationalException {

    final PersonRegistrationResult registrationResult =
      registerAPersonForTest();

    bdmEvidenceUtils.createContactPreferenceEvidence(
      registrationResult.registrationIDDetails.concernRoleID,
      BDMLANGUAGE.ENGLISHL, BDMLANGUAGE.ENGLISHL, BDMConstants.EMPTY_STRING);

    final BDMCommunication bdmCommunicationObj =
      BDMCommunicationFactory.newInstance();

    final RecordedCommDetails1 recordedCommDetails1 =
      new RecordedCommDetails1();
    recordedCommDetails1.clientParticipantRoleID =
      registrationResult.registrationIDDetails.concernRoleID;
    recordedCommDetails1.correspondentParticipantRoleID =
      registrationResult.registrationIDDetails.concernRoleID;
    recordedCommDetails1.correspondentConcernRoleID =
      registrationResult.registrationIDDetails.concernRoleID;
    recordedCommDetails1.communicationDate = Date.getCurrentDate();
    recordedCommDetails1.communicationText = "Person Privacy Request";
    recordedCommDetails1.communicationDirection =
      COMMUNICATIONDIRECTION.INCOMING;
    recordedCommDetails1.communicationTypeCode =
      COMMUNICATIONTYPE.PRIVACY_REQUEST_TIER_1;
    recordedCommDetails1.correspondentType =
      curam.codetable.CORRESPONDENT.CLIENT;
    recordedCommDetails1.methodTypeCode = COMMUNICATIONMETHOD.HARDCOPY;
    recordedCommDetails1.subject = "Person Privacy Request";
    recordedCommDetails1.correspondentName = "Person Test";

    final ConcernRoleCommKeyOut concernRoleCommKeyOut = bdmCommunicationObj
      .createRecordedCommWithReturningID(recordedCommDetails1);

    assertTrue(concernRoleCommKeyOut.communicationID != 0);

    final BizObjectTypeKey bizObjectTypeKey = new BizObjectTypeKey();
    bizObjectTypeKey.bizObjectID =
      registrationResult.registrationIDDetails.concernRoleID;
    bizObjectTypeKey.bizObjectType = BUSINESSOBJECTTYPE.PERSON;

    final RecordedCommKey recordedCommKey = new RecordedCommKey();
    recordedCommKey.communicationID = concernRoleCommKeyOut.communicationID;

    final RecordedCommDetails1 recordedCommDetailsForModify =
      bdmCommunicationObj.readRecordedComm(recordedCommKey);

    recordedCommDetailsForModify.communicationDate =
      Date.getCurrentDate().addDays(-10);

    bdmCommunicationObj.modifyRecordedComm(recordedCommKey,
      recordedCommDetailsForModify);

    final RecordedCommDetails1 readRecordedCommDetails =
      bdmCommunicationObj.readRecordedComm(recordedCommKey);

    assertEquals(Date.getCurrentDate().addDays(-10),
      readRecordedCommDetails.communicationDate);

    assertEquals(COMMUNICATIONTYPE.PRIVACY_REQUEST_TIER_1,
      readRecordedCommDetails.communicationTypeCode);

  }

  /**
   * Test the create, modify and read privacy request tier 1 details for the
   * integrated case.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testICManagePrivacyRequestTier1()
    throws AppException, InformationalException {

    final PersonRegistrationResult registrationResult =
      registerAPersonForTest();

    bdmEvidenceUtils.createContactPreferenceEvidence(
      registrationResult.registrationIDDetails.concernRoleID,
      BDMLANGUAGE.FRENCHL, BDMLANGUAGE.FRENCHL, BDMConstants.EMPTY_STRING);

    final CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      createFEC(registrationResult.registrationIDDetails.concernRoleID);

    final BDMCommunication bdmCommunicationObj =
      BDMCommunicationFactory.newInstance();

    final RecordedCommDetails1 recordedCommDetails1 =
      new RecordedCommDetails1();
    recordedCommDetails1.correspondentParticipantRoleID =
      registrationResult.registrationIDDetails.concernRoleID;
    recordedCommDetails1.correspondentName = "Person Test";
    recordedCommDetails1.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;
    recordedCommDetails1.communicationDate = Date.getCurrentDate();
    recordedCommDetails1.communicationText = "Person Privacy Request";
    recordedCommDetails1.communicationDirection =
      COMMUNICATIONDIRECTION.INCOMING;
    recordedCommDetails1.communicationTypeCode =
      COMMUNICATIONTYPE.PRIVACY_REQUEST_TIER_1;
    recordedCommDetails1.correspondentType =
      curam.codetable.CORRESPONDENT.CLIENT;
    recordedCommDetails1.methodTypeCode = COMMUNICATIONMETHOD.HARDCOPY;
    recordedCommDetails1.subject = "Person Privacy Request";
    recordedCommDetails1.correspondentName = "Person Test";

    final ConcernRoleCommKeyOut concernRoleCommKeyOut = bdmCommunicationObj
      .createRecordedCommWithReturningID(recordedCommDetails1);

    assertTrue(concernRoleCommKeyOut.communicationID != 0);

    final RecordedCommKey recordedCommKey = new RecordedCommKey();
    recordedCommKey.communicationID = concernRoleCommKeyOut.communicationID;

    final RecordedCommDetails1 recordedCommDetailsForModify =
      bdmCommunicationObj.readRecordedComm(recordedCommKey);

    recordedCommDetailsForModify.communicationDate =
      Date.getCurrentDate().addDays(-10);

    bdmCommunicationObj.modifyRecordedComm(recordedCommKey,
      recordedCommDetailsForModify);

    final RecordedCommDetails1 readRecordedCommDetails =
      bdmCommunicationObj.readRecordedComm(recordedCommKey);

    assertEquals(Date.getCurrentDate().addDays(-10),
      readRecordedCommDetails.communicationDate);

    assertEquals(COMMUNICATIONTYPE.PRIVACY_REQUEST_TIER_1,
      readRecordedCommDetails.communicationTypeCode);

  }

  /**
   * Test the create, modify and read privacy request tier 2 details for the
   * person.
   * ORAL and written Language are different
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testPersonManagePrivacyRequestTier2()
    throws AppException, InformationalException {

    final PersonRegistrationResult registrationResult =
      registerAPersonForTest();

    bdmEvidenceUtils.createContactPreferenceEvidence(
      registrationResult.registrationIDDetails.concernRoleID,
      BDMLANGUAGE.FRENCHL, BDMLANGUAGE.ENGLISHL, BDMConstants.EMPTY_STRING);

    final ParticipantRegistrationWizardResult ssaCountry = createSSACountry();

    final BDMCommunication bdmCommunicationObj =
      BDMCommunicationFactory.newInstance();

    final RecordedCommDetails1 recordedCommDetails1 =
      new RecordedCommDetails1();
    recordedCommDetails1.clientParticipantRoleID =
      registrationResult.registrationIDDetails.concernRoleID;
    recordedCommDetails1.correspondentParticipantRoleID =
      ssaCountry.registrationResult.concernRoleID;
    recordedCommDetails1.communicationDate = Date.getCurrentDate();
    recordedCommDetails1.communicationText = "Person Privacy Request";
    recordedCommDetails1.communicationDirection =
      COMMUNICATIONDIRECTION.INCOMING;
    recordedCommDetails1.communicationTypeCode =
      COMMUNICATIONTYPE.PRIVACY_REQUEST_TIER_2;
    recordedCommDetails1.correspondentType =
      curam.codetable.CORRESPONDENT.ATIPGROUP;
    recordedCommDetails1.methodTypeCode = COMMUNICATIONMETHOD.HARDCOPY;
    recordedCommDetails1.subject = "Person Privacy Request";
    recordedCommDetails1.correspondentName = "Person Test";

    final ConcernRoleCommKeyOut concernRoleCommKeyOut = bdmCommunicationObj
      .createRecordedCommWithReturningID(recordedCommDetails1);

    assertTrue(concernRoleCommKeyOut.communicationID != 0);

    final RecordedCommKey recordedCommKey = new RecordedCommKey();
    recordedCommKey.communicationID = concernRoleCommKeyOut.communicationID;

    final RecordedCommDetails1 recordedCommDetailsForModify =
      bdmCommunicationObj.readRecordedComm(recordedCommKey);

    recordedCommDetailsForModify.communicationDate =
      Date.getCurrentDate().addDays(-10);

    bdmCommunicationObj.modifyRecordedComm(recordedCommKey,
      recordedCommDetailsForModify);

    final RecordedCommDetails1 readRecordedCommDetails =
      bdmCommunicationObj.readRecordedComm(recordedCommKey);

    assertEquals(Date.getCurrentDate().addDays(-10),
      readRecordedCommDetails.communicationDate);

    assertEquals(COMMUNICATIONTYPE.PRIVACY_REQUEST_TIER_2,
      readRecordedCommDetails.communicationTypeCode);

  }

  /**
   * Test the create, modify and read privacy request tier 2 details for the
   * IC.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testICManagePrivacyRequestTier2()
    throws AppException, InformationalException {

    final PersonRegistrationResult registrationResult =
      registerAPersonForTest();

    bdmEvidenceUtils.createContactPreferenceEvidence(
      registrationResult.registrationIDDetails.concernRoleID,
      BDMLANGUAGE.ENGLISHL, BDMLANGUAGE.ENGLISHL, BDMConstants.EMPTY_STRING);

    final CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      createFEC(registrationResult.registrationIDDetails.concernRoleID);

    final ParticipantRegistrationWizardResult ssaCountry = createSSACountry();

    final BDMCommunication bdmCommunicationObj =
      BDMCommunicationFactory.newInstance();

    final RecordedCommDetails1 recordedCommDetails1 =
      new RecordedCommDetails1();
    recordedCommDetails1.correspondentName = "Person Test";
    recordedCommDetails1.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;
    recordedCommDetails1.correspondentParticipantRoleID =
      ssaCountry.registrationResult.concernRoleID;
    recordedCommDetails1.correspondentName = "Person Test";
    recordedCommDetails1.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;
    recordedCommDetails1.communicationDate = Date.getCurrentDate();
    recordedCommDetails1.communicationText = "Person Privacy Request";
    recordedCommDetails1.communicationDirection =
      COMMUNICATIONDIRECTION.INCOMING;
    recordedCommDetails1.communicationTypeCode =
      COMMUNICATIONTYPE.PRIVACY_REQUEST_TIER_2;
    recordedCommDetails1.correspondentType =
      curam.codetable.CORRESPONDENT.ATIPGROUP;
    recordedCommDetails1.methodTypeCode = COMMUNICATIONMETHOD.HARDCOPY;
    recordedCommDetails1.subject = "Person Privacy Request";
    recordedCommDetails1.correspondentName = "Person Test";

    final ConcernRoleCommKeyOut concernRoleCommKeyOut = bdmCommunicationObj
      .createRecordedCommWithReturningID(recordedCommDetails1);

    assertTrue(concernRoleCommKeyOut.communicationID != 0);

    final RecordedCommKey recordedCommKey = new RecordedCommKey();
    recordedCommKey.communicationID = concernRoleCommKeyOut.communicationID;

    final RecordedCommDetails1 recordedCommDetailsForModify =
      bdmCommunicationObj.readRecordedComm(recordedCommKey);

    recordedCommDetailsForModify.communicationDate =
      Date.getCurrentDate().addDays(-10);

    bdmCommunicationObj.modifyRecordedComm(recordedCommKey,
      recordedCommDetailsForModify);

    final RecordedCommDetails1 readRecordedCommDetails =
      bdmCommunicationObj.readRecordedComm(recordedCommKey);

    assertEquals(Date.getCurrentDate().addDays(-10),
      readRecordedCommDetails.communicationDate);

    assertEquals(COMMUNICATIONTYPE.PRIVACY_REQUEST_TIER_2,
      readRecordedCommDetails.communicationTypeCode);

  }

  /**
   * Test the create, modify and read privacy request tier 1 details for the
   * person.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testModifyPersonManagePrivacyRequestTask()
    throws AppException, InformationalException {

    final PersonRegistrationResult registrationResult =
      registerAPersonForTest();

    bdmEvidenceUtils.createContactPreferenceEvidence(
      registrationResult.registrationIDDetails.concernRoleID,
      BDMLANGUAGE.ENGLISHL, BDMLANGUAGE.ENGLISHL, BDMConstants.EMPTY_STRING);

    final BDMCommunication bdmCommunicationObj =
      BDMCommunicationFactory.newInstance();

    final RecordedCommDetails1 recordedCommDetails1 =
      new RecordedCommDetails1();
    recordedCommDetails1.clientParticipantRoleID =
      registrationResult.registrationIDDetails.concernRoleID;
    recordedCommDetails1.correspondentParticipantRoleID =
      registrationResult.registrationIDDetails.concernRoleID;
    recordedCommDetails1.correspondentConcernRoleID =
      registrationResult.registrationIDDetails.concernRoleID;
    recordedCommDetails1.communicationDate = Date.getCurrentDate();
    recordedCommDetails1.communicationText = "Person Privacy Request";
    recordedCommDetails1.communicationDirection =
      COMMUNICATIONDIRECTION.INCOMING;
    recordedCommDetails1.communicationTypeCode =
      COMMUNICATIONTYPE.PRIVACY_REQUEST_TIER_1;
    recordedCommDetails1.correspondentType =
      curam.codetable.CORRESPONDENT.CLIENT;
    recordedCommDetails1.methodTypeCode = COMMUNICATIONMETHOD.HARDCOPY;
    recordedCommDetails1.subject = "Person Privacy Request";
    recordedCommDetails1.correspondentName = "Person Test";

    final ConcernRoleCommKeyOut concernRoleCommKeyOut = bdmCommunicationObj
      .createRecordedCommWithReturningID(recordedCommDetails1);

    assertTrue(concernRoleCommKeyOut.communicationID != 0);

    createTaskDataForPerson(concernRoleCommKeyOut.communicationID,
      registrationResult.registrationIDDetails.concernRoleID, 0, 0, 0);

    final RecordedCommKey recordedCommKey = new RecordedCommKey();
    recordedCommKey.communicationID = concernRoleCommKeyOut.communicationID;

    final RecordedCommDetails1 recordedCommDetailsForModify =
      bdmCommunicationObj.readRecordedComm(recordedCommKey);

    recordedCommDetailsForModify.communicationDate =
      Date.getCurrentDate().addDays(-10);

    bdmCommunicationObj.modifyRecordedComm(recordedCommKey,
      recordedCommDetailsForModify);

    final RecordedCommDetails1 readRecordedCommDetails =
      bdmCommunicationObj.readRecordedComm(recordedCommKey);

    assertEquals(Date.getCurrentDate().addDays(-10),
      readRecordedCommDetails.communicationDate);

    assertEquals(COMMUNICATIONTYPE.PRIVACY_REQUEST_TIER_1,
      readRecordedCommDetails.communicationTypeCode);

  }

  /**
   * Test the create, modify and read privacy request tier 1 details for the
   * person.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testPersonInterimApp()
    throws AppException, InformationalException {

    // getSINNumberSticExpectation();
    final PDCPersonDetails person = bdmEvidenceUtils.createPDCPerson();

    bdmEvidenceUtils.createContactPreferenceEvidence(person.concernRoleID,
      BDMLANGUAGE.ENGLISHL, BDMLANGUAGE.ENGLISHL, BDMConstants.EMPTY_STRING);

    final BDMCommunication bdmCommunicationObj =
      BDMCommunicationFactory.newInstance();

    final RecordedCommDetails1 recordedCommDetails1 =
      new RecordedCommDetails1();
    recordedCommDetails1.clientParticipantRoleID = person.concernRoleID;
    recordedCommDetails1.correspondentParticipantRoleID =
      person.concernRoleID;
    recordedCommDetails1.correspondentConcernRoleID = person.concernRoleID;
    recordedCommDetails1.communicationDate = Date.getCurrentDate();
    recordedCommDetails1.communicationText = "Person Privacy Request";
    recordedCommDetails1.communicationDirection =
      COMMUNICATIONDIRECTION.INCOMING;
    recordedCommDetails1.communicationTypeCode =
      COMMUNICATIONTYPE.INTERIM_APP_REQUEST;
    recordedCommDetails1.correspondentType =
      curam.codetable.CORRESPONDENT.CLIENT;
    recordedCommDetails1.methodTypeCode = COMMUNICATIONMETHOD.HARDCOPY;
    recordedCommDetails1.subject = "Person Privacy Request";
    recordedCommDetails1.correspondentName = "Person Test";

    final ConcernRoleCommKeyOut concernRoleCommKeyOut = bdmCommunicationObj
      .createRecordedCommWithReturningID(recordedCommDetails1);

    assertTrue(concernRoleCommKeyOut.communicationID != 0);

    final BizObjectTypeKey bizObjectTypeKey = new BizObjectTypeKey();
    bizObjectTypeKey.bizObjectID = person.concernRoleID;
    bizObjectTypeKey.bizObjectType = BUSINESSOBJECTTYPE.PERSON;

    final RecordedCommKey recordedCommKey = new RecordedCommKey();
    recordedCommKey.communicationID = concernRoleCommKeyOut.communicationID;

    final RecordedCommDetails1 recordedCommDetailsForModify =
      bdmCommunicationObj.readRecordedComm(recordedCommKey);

    recordedCommDetailsForModify.communicationDate =
      Date.getCurrentDate().addDays(-10);

    bdmCommunicationObj.modifyRecordedComm(recordedCommKey,
      recordedCommDetailsForModify);

    final RecordedCommDetails1 readRecordedCommDetails =
      bdmCommunicationObj.readRecordedComm(recordedCommKey);

    assertEquals(Date.getCurrentDate().addDays(-10),
      readRecordedCommDetails.communicationDate);

    assertEquals(COMMUNICATIONTYPE.INTERIM_APP_REQUEST,
      readRecordedCommDetails.communicationTypeCode);

  }

  /**
   * Test the create, modify and read privacy request tier 1 details for the
   * person.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testPersonOutgoingInterimApp()
    throws AppException, InformationalException {

    // getSINNumberSticExpectation();
    final PDCPersonDetails person = bdmEvidenceUtils.createPDCPerson();

    bdmEvidenceUtils.createContactPreferenceEvidence(person.concernRoleID,
      BDMLANGUAGE.ENGLISHL, BDMLANGUAGE.ENGLISHL, BDMConstants.EMPTY_STRING);

    final BDMCommunication bdmCommunicationObj =
      BDMCommunicationFactory.newInstance();

    final RecordedCommDetails1 recordedCommDetails1 =
      new RecordedCommDetails1();
    recordedCommDetails1.clientParticipantRoleID = person.concernRoleID;
    recordedCommDetails1.correspondentParticipantRoleID =
      person.concernRoleID;
    recordedCommDetails1.correspondentConcernRoleID = person.concernRoleID;
    recordedCommDetails1.communicationDate = Date.getCurrentDate();
    recordedCommDetails1.communicationText = "Person Privacy Request";
    recordedCommDetails1.communicationDirection =
      COMMUNICATIONDIRECTION.OUTGOING;
    recordedCommDetails1.communicationTypeCode =
      COMMUNICATIONTYPE.INTERIM_APP_OUTGOING;
    recordedCommDetails1.correspondentType =
      curam.codetable.CORRESPONDENT.CLIENT;
    recordedCommDetails1.methodTypeCode = COMMUNICATIONMETHOD.HARDCOPY;
    recordedCommDetails1.subject = "Person Privacy Request";
    recordedCommDetails1.correspondentName = "Person Test";

    final ConcernRoleCommKeyOut concernRoleCommKeyOut = bdmCommunicationObj
      .createRecordedCommWithReturningID(recordedCommDetails1);

    assertTrue(concernRoleCommKeyOut.communicationID != 0);

    final BizObjectTypeKey bizObjectTypeKey = new BizObjectTypeKey();
    bizObjectTypeKey.bizObjectID = person.concernRoleID;
    bizObjectTypeKey.bizObjectType = BUSINESSOBJECTTYPE.PERSON;

    final RecordedCommKey recordedCommKey = new RecordedCommKey();
    recordedCommKey.communicationID = concernRoleCommKeyOut.communicationID;

    final RecordedCommDetails1 recordedCommDetailsForModify =
      bdmCommunicationObj.readRecordedComm(recordedCommKey);

    recordedCommDetailsForModify.communicationDate =
      Date.getCurrentDate().addDays(-10);

    bdmCommunicationObj.modifyRecordedComm(recordedCommKey,
      recordedCommDetailsForModify);

    final RecordedCommDetails1 readRecordedCommDetails =
      bdmCommunicationObj.readRecordedComm(recordedCommKey);

    assertEquals(Date.getCurrentDate().addDays(-10),
      readRecordedCommDetails.communicationDate);

    assertEquals(COMMUNICATIONTYPE.INTERIM_APP_OUTGOING,
      readRecordedCommDetails.communicationTypeCode);

  }

  /**
   * Method to create task for the person
   *
   * @param communicationID
   * @param concernRoleID
   * @param hours
   * @param minutes
   * @param seconds
   * @throws AppException
   * @throws InformationalException
   */
  private void createTaskDataForPerson(final long communicationID,
    final long concernRoleID, final int hours, final int minutes,
    final int seconds) throws AppException, InformationalException {

    final TaskDtls taskCreateDetail = new TaskDtls();

    taskCreateDetail.taskID = UniqueID.nextUniqueID();
    taskCreateDetail.creationTime = DateTime.getCurrentDateTime();
    taskCreateDetail.status = TASKSTATUS.NOTSTARTED;
    taskCreateDetail.wdoSnapshot = "";
    taskCreateDetail.priority = TASKPRIORITY.NORMAL;
    taskCreateDetail.versionNo = 1;
    taskCreateDetail.reservedBy = "caseworker";
    TaskFactory.newInstance().insert(taskCreateDetail);

    final TaskAssignmentDtls taskAssignmentDtls = new TaskAssignmentDtls();
    taskAssignmentDtls.taskID = taskCreateDetail.taskID;
    taskAssignmentDtls.relatedName = "caseworker";
    taskAssignmentDtls.assigneeType = TARGETITEMTYPE.WORKQUEUE;

    TaskAssignmentFactory.newInstance().insert(taskAssignmentDtls);

    final WorkflowDeadlineDtls workflowDeadlineDtls =
      new WorkflowDeadlineDtls();
    workflowDeadlineDtls.deadlineID = UniqueID.nextUniqueID();
    workflowDeadlineDtls.taskID = taskCreateDetail.taskID;
    workflowDeadlineDtls.suspended = false;
    workflowDeadlineDtls.deadlineTime =
      DateTime.getCurrentDateTime().addTime(hours, minutes, seconds);
    workflowDeadlineDtls.deadlineType = "WDT1";
    workflowDeadlineDtls.versionNo = 1;

    WorkflowDeadlineFactory.newInstance().insert(workflowDeadlineDtls);

    BizObjAssociationDtls bizObjAssociationDtls = new BizObjAssociationDtls();
    bizObjAssociationDtls.bizObjAssocID = UniqueID.nextUniqueID();
    bizObjAssociationDtls.taskID = taskCreateDetail.taskID;
    bizObjAssociationDtls.bizObjectID = communicationID;
    bizObjAssociationDtls.bizObjectType = BUSINESSOBJECTTYPE.BDMCOMMUNICATION;

    BizObjAssociationFactory.newInstance().insert(bizObjAssociationDtls);

    bizObjAssociationDtls = new BizObjAssociationDtls();
    bizObjAssociationDtls.bizObjAssocID = taskCreateDetail.taskID;
    bizObjAssociationDtls.taskID = taskCreateDetail.taskID;
    bizObjAssociationDtls.bizObjectID = concernRoleID;
    bizObjAssociationDtls.bizObjectType = BUSINESSOBJECTTYPE.CASE;

    BizObjAssociationFactory.newInstance().insert(bizObjAssociationDtls);

  }

  /**
   * This is the utility method to register person for the test class.
   *
   * @throws AppException
   * @throws InformationalException
   */
  private PersonRegistrationResult registerAPersonForTest()
    throws AppException, InformationalException {

    // BEGIN: REGISTER PERSON
    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = "Save";

    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "Person1";
    bdmPersonRegistrationDetails.dtls.surname = "Test1";
    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19900201");

    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;

    addressFieldDetails.suiteNum = "207";
    addressFieldDetails.addressLine1 = "6511";
    addressFieldDetails.addressLine2 = "GILBER RD";
    addressFieldDetails.province = PROVINCETYPE.BRITISHCOLUMBIA;
    addressFieldDetails.city = "RICHMOND";
    addressFieldDetails.postalCode = "V7C 3V9";
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

    assertTrue(registrationResult.registrationIDDetails.concernRoleID != 0);

    return registrationResult;

  }

  protected CreateIntegratedCaseResultAndMessages createFEC(
    final long concernRoleID) throws AppException, InformationalException {

    // BEGIN: create FE IC for the person
    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      new CreateIntegratedCaseResultAndMessages();

    final BDMFECaseDetails details = new BDMFECaseDetails();
    details.concernRoleID = concernRoleID;

    details.countryCode = BDMSOURCECOUNTRY.IRELAND;

    details.priorityCode = CASEPRIORITY.HIGH;

    createIntegratedCaseResultAndMessages = BDMForeignEngagementCaseFactory
      .newInstance().createFEIntegratedCase(details);
    // END: create FE IC for the person

    return createIntegratedCaseResultAndMessages;
  }

  protected ParticipantRegistrationWizardResult createSSACountry()
    throws AppException, InformationalException {

    final BDMExternalPartyTestData extPartyTestDataObj =
      new BDMExternalPartyTestData();

    final BDMExternalParty bdmExtPartyObj =
      BDMExternalPartyFactory.newInstance();

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

    final ParticipantRegistrationWizardResult wizardResult =
      bdmExtPartyObj.setRegisterExternalPartyDetails(registrationDtls,
        wizardStateID, actionIDProperty, bdmextPartyRegDetails);
    return wizardResult;

  }

}
