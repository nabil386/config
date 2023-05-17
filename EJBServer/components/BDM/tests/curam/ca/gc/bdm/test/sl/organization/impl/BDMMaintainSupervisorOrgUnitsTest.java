/**
 *
 */
package curam.ca.gc.bdm.test.sl.organization.impl;

import curam.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.codetable.BDMESCALATIONLEVELS;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.codetable.BDMURGENTFLAGTYPE;
import curam.ca.gc.bdm.entity.fact.BDMEscalationLevelFactory;
import curam.ca.gc.bdm.entity.intf.BDMEscalationLevel;
import curam.ca.gc.bdm.entity.struct.BDMEscalationLevelDtls;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.fact.BDMCaseUrgentFlagFactory;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.struct.BDMCaseUrgentFlagDetails;
import curam.ca.gc.bdm.facade.fec.fact.BDMForeignEngagementCaseFactory;
import curam.ca.gc.bdm.facade.fec.struct.BDMFECaseDetails;
import curam.ca.gc.bdm.sl.communication.fact.BDMCommunicationFactory;
import curam.ca.gc.bdm.sl.organization.fact.BDMMaintainSupervisorOrgUnitsFactory;
import curam.ca.gc.bdm.sl.organization.intf.BDMMaintainSupervisorOrgUnits;
import curam.ca.gc.bdm.sl.organization.struct.BDMListDeferredTasksReservedByOrgUnitUserDetails;
import curam.ca.gc.bdm.sl.organization.struct.BDMListOpenTasksReservedByOrgUnitUserDetails;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.codetable.CASEPRIORITY;
import curam.codetable.RECORDSTATUS;
import curam.codetable.TARGETITEMTYPE;
import curam.codetable.TASKPRIORITY;
import curam.codetable.TASKSTATUS;
import curam.codetable.impl.METHODOFDELIVERYEntry;
import curam.core.facade.struct.CreateIntegratedCaseResultAndMessages;
import curam.core.sl.entity.fact.TaskAssignmentFactory;
import curam.core.sl.entity.struct.TaskAssignmentDtls;
import curam.core.sl.struct.RecordedCommDetails1;
import curam.core.struct.ConcernRoleCommKeyOut;
import curam.core.struct.PersonDtls;
import curam.supervisor.sl.struct.UserNameOrgUnitIDKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.UniqueID;
import curam.util.workflow.fact.BizObjAssociationFactory;
import curam.util.workflow.fact.TaskFactory;
import curam.util.workflow.fact.WorkflowDeadlineFactory;
import curam.util.workflow.impl.LocalizableStringResolver;
import curam.util.workflow.impl.LocalizableStringResolver.TaskStringResolver;
import curam.util.workflow.struct.BizObjAssociationDtls;
import curam.util.workflow.struct.TaskDtls;
import curam.util.workflow.struct.TaskWDOSnapshotDetails;
import curam.util.workflow.struct.WorkflowDeadlineDtls;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author jalpa.patel
 *
 */
@RunWith(JMockit.class)
public class BDMMaintainSupervisorOrgUnitsTest extends CuramServerTestJUnit4 {

  BDMMaintainSupervisorOrgUnits bdmMaintainSupervisorOrgUnits;

  @Mocked
  LocalizableStringResolver localizableStringResolver;

  @Mocked
  TaskStringResolver taskResolver;

  private static String taskSubject = "Test Task Subject";

  private static String ESCALATIONLEVEL_1 = "Escalation Level 1";

  private static String SALLY = "Sally";

  @Override
  public void setUpCuramServerTest() {

    super.setUpCuramServerTest();
    bdmMaintainSupervisorOrgUnits =
      BDMMaintainSupervisorOrgUnitsFactory.newInstance();

    taskSubjectResolverExpectation();
  }

  /**
   *
   */
  private void taskSubjectResolverExpectation() {

    try {
      new Expectations() {

        {
          taskResolver.getSubjectForTask((TaskWDOSnapshotDetails) any);
          result = taskSubject;
        }
      };
    } catch (final AppException e) {

      e.printStackTrace();
    } catch (final InformationalException e) {

      e.printStackTrace();
    }
  }

  // ___________________________________________________________________________
  /**
   * This method gets a list of the tasks that have been reserved, and then
   * deferred by members of the organization unit . This list single user
   * deferred single task
   */

  @Test
  public void testlistDeferredTasksReservedByOrgUnitUser()
    throws AppException, InformationalException {

    // Register person
    final String name = "Joe Test";
    final long concernRoleID = registerPerson(name);

    // Create FEC case
    final CreateIntegratedCaseResultAndMessages createCase =
      createFEC(concernRoleID);

    // Created recorded CXommunication with escaltion level
    final ConcernRoleCommKeyOut commID =
      createRecordedCommunicationwithEscalation(
        createCase.createCaseResult.integratedCaseID, name, concernRoleID);

    createTaskEscalationLevel(commID.communicationID);
    // Set urgent flag
    setUpUrgentFlagData(createCase.createCaseResult.integratedCaseID,
      BDMURGENTFLAGTYPE.MPENQUIRY, Date.getCurrentDate());

    // Create task
    createDeferredTaskDataDeadline(SALLY, commID.communicationID,
      createCase.createCaseResult.integratedCaseID, 24, 0, 0);

    final UserNameOrgUnitIDKey key = new UserNameOrgUnitIDKey();
    key.orgUnitID = 80166;
    key.userName = SALLY;

    final BDMListDeferredTasksReservedByOrgUnitUserDetails list =
      bdmMaintainSupervisorOrgUnits
        .listDeferredTasksReservedByOrgUnitUser(key);
    assertTrue(!list.details.taskDetailsList.isEmpty());
    assertEquals(
      CodeTable.getOneItem(BDMURGENTFLAGTYPE.TABLENAME,
        BDMURGENTFLAGTYPE.MPENQUIRY, TransactionInfo.getProgramLocale()),
      list.details.taskDetailsList.get(0).caseUrgentFlagStr);
    assertEquals(ESCALATIONLEVEL_1,
      list.details.taskDetailsList.get(0).escalationLevelDesc);

  }

  // ___________________________________________________________________________
  /**
   * This method gets a list of the tasks that have been reserved, and then
   * deferred by members of the organization unit . This list single user
   * deferred 2 task
   */
  @Test
  public void testlistDeferredTasksReservedByOrgUnitUser_multiple()
    throws AppException, InformationalException {

    // Register person
    final String name = "Jane Test";
    final long concernRoleID = registerPerson(name);

    // Create FEC case
    final CreateIntegratedCaseResultAndMessages createCase =
      createFEC(concernRoleID);

    // Created recorded CXommunication with escaltion level
    final ConcernRoleCommKeyOut commID =
      createRecordedCommunicationwithEscalation(
        createCase.createCaseResult.integratedCaseID, name, concernRoleID);

    createTaskEscalationLevel(commID.communicationID);
    // Set urgent flag
    setUpUrgentFlagData(createCase.createCaseResult.integratedCaseID,
      BDMURGENTFLAGTYPE.MPENQUIRY, Date.getCurrentDate());

    // Create task 1
    createDeferredTaskDataDeadline(SALLY, commID.communicationID,
      createCase.createCaseResult.integratedCaseID, 24, 0, 0);
    // Create task 2
    createDeferredTaskDataDeadline(SALLY, commID.communicationID,
      createCase.createCaseResult.integratedCaseID, 24, 0, 0);

    final UserNameOrgUnitIDKey key = new UserNameOrgUnitIDKey();
    key.orgUnitID = 80166;
    key.userName = SALLY;

    final BDMListDeferredTasksReservedByOrgUnitUserDetails list =
      bdmMaintainSupervisorOrgUnits
        .listDeferredTasksReservedByOrgUnitUser(key);

    assertTrue(list.details.taskDetailsList.size() == 2);
    assertEquals(
      CodeTable.getOneItem(BDMURGENTFLAGTYPE.TABLENAME,
        BDMURGENTFLAGTYPE.MPENQUIRY, TransactionInfo.getProgramLocale()),
      list.details.taskDetailsList.get(0).caseUrgentFlagStr);
    assertEquals(ESCALATIONLEVEL_1,
      list.details.taskDetailsList.get(0).escalationLevelDesc);
    assertEquals(
      CodeTable.getOneItem(BDMURGENTFLAGTYPE.TABLENAME,
        BDMURGENTFLAGTYPE.MPENQUIRY, TransactionInfo.getProgramLocale()),
      list.details.taskDetailsList.get(1).caseUrgentFlagStr);
    assertEquals(ESCALATIONLEVEL_1,
      list.details.taskDetailsList.get(1).escalationLevelDesc);

  }

  /*
   * //
   * ___________________________________________________________________________
   * /**
   * This method tests a list of the tasks reserved by members of the
   * organization unit, that are open.
   *
   */

  @Test
  public void testlistOpenTasksReservedByOrgUnitUser()
    throws AppException, InformationalException {

    // Register person
    final String name = "John Doe";
    final long concernRoleID = registerPerson(name);

    // Create FEC case
    final CreateIntegratedCaseResultAndMessages createCase =
      createFEC(concernRoleID);

    // Created recorded CXommunication with escaltion level
    final ConcernRoleCommKeyOut commID =
      createRecordedCommunicationwithEscalation(
        createCase.createCaseResult.integratedCaseID, name, concernRoleID);

    createTaskEscalationLevel(commID.communicationID);

    // Set urgent flag
    setUpUrgentFlagData(createCase.createCaseResult.integratedCaseID,
      BDMURGENTFLAGTYPE.MPENQUIRY, Date.getCurrentDate());

    // Create task 1
    createTaskData(SALLY, commID.communicationID,
      createCase.createCaseResult.integratedCaseID);

    final UserNameOrgUnitIDKey key = new UserNameOrgUnitIDKey();
    key.orgUnitID = 80166;
    key.userName = SALLY;

    final BDMListOpenTasksReservedByOrgUnitUserDetails list =
      bdmMaintainSupervisorOrgUnits.listOpenTasksReservedByOrgUnitUser(key);

    assertTrue(!list.taskDetailsList.taskDetailsList.isEmpty());
    assertEquals(
      CodeTable.getOneItem(BDMURGENTFLAGTYPE.TABLENAME,
        BDMURGENTFLAGTYPE.MPENQUIRY, TransactionInfo.getProgramLocale()),
      list.taskDetailsList.taskDetailsList.get(0).caseUrgentFlagStr);
    assertEquals(ESCALATIONLEVEL_1,
      list.taskDetailsList.taskDetailsList.get(0).escalationLevelDesc);

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
    recordedCommDetails.correspondentType = "CO2";
    recordedCommDetails.methodTypeCode = "CM1";
    recordedCommDetails.subject = "Test comm 2";
    recordedCommDetails.communicationFormat = "CF4"; // recorded Communication

    return BDMCommunicationFactory.newInstance()
      .createRecordedCommWithReturningID(recordedCommDetails);
  }

  /**
   * Creates test data for task with deadline date set
   *
   * @throws AppException
   * @throws InformationalException
   */
  private void createDeferredTaskDataDeadline(final String username,
    final long communicationID, final long caseID, final int hours,
    final int minutes, final int seconds)
    throws AppException, InformationalException {

    final TaskDtls taskCreateDetail = new TaskDtls();

    taskCreateDetail.taskID = UniqueID.nextUniqueID();
    taskCreateDetail.creationTime = DateTime.getCurrentDateTime();
    taskCreateDetail.status = TASKSTATUS.DEFERRED;
    taskCreateDetail.wdoSnapshot = "";
    taskCreateDetail.priority = TASKPRIORITY.NORMAL;
    taskCreateDetail.versionNo = 1;
    taskCreateDetail.reservedBy = username;
    TaskFactory.newInstance().insert(taskCreateDetail);

    // Create task assignment to user
    final TaskAssignmentDtls taskAssignmentDtls = new TaskAssignmentDtls();
    taskAssignmentDtls.taskID = taskCreateDetail.taskID;
    taskAssignmentDtls.relatedName = username;
    taskAssignmentDtls.assigneeType = TARGETITEMTYPE.USER;

    TaskAssignmentFactory.newInstance().insert(taskAssignmentDtls);

    // Create task deadline
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

    // Create Biz Object association
    BizObjAssociationDtls bizObjAssociationDtls = new BizObjAssociationDtls();
    bizObjAssociationDtls.bizObjAssocID = UniqueID.nextUniqueID();
    bizObjAssociationDtls.taskID = taskCreateDetail.taskID;
    bizObjAssociationDtls.bizObjectID = communicationID;
    bizObjAssociationDtls.bizObjectType = BUSINESSOBJECTTYPE.BDMCOMMUNICATION;

    BizObjAssociationFactory.newInstance().insert(bizObjAssociationDtls);

    bizObjAssociationDtls = new BizObjAssociationDtls();
    bizObjAssociationDtls.bizObjAssocID = taskCreateDetail.taskID;
    bizObjAssociationDtls.taskID = taskCreateDetail.taskID;
    bizObjAssociationDtls.bizObjectID = caseID;
    bizObjAssociationDtls.bizObjectType = BUSINESSOBJECTTYPE.CASE;

    BizObjAssociationFactory.newInstance().insert(bizObjAssociationDtls);

  }

  /**
   * Register person
   *
   *
   * @throws AppException
   * @throws InformationalException
   */
  private long registerPerson(final String name)
    throws AppException, InformationalException {

    final RegisterPerson registerPersonObj = new RegisterPerson("");
    final PersonDtls personDtls =
      registerPersonObj.registerDefault(name, METHODOFDELIVERYEntry.CHEQUE);
    return personDtls.concernRoleID;

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
   * Setup urgent flag data
   *
   * @param caseID
   * @param urgentFlagType
   * @param startDate
   * @throws AppException
   * @throws InformationalException
   */
  private void setUpUrgentFlagData(final long caseID,
    final String urgentFlagType, final Date startDate)
    throws AppException, InformationalException {

    final BDMCaseUrgentFlagDetails flagDtls = new BDMCaseUrgentFlagDetails();
    flagDtls.caseID = caseID;
    TransactionInfo.getInfo();
    flagDtls.createdBy = TransactionInfo.getProgramUser();
    TransactionInfo.getInfo();
    flagDtls.createdByFullName = TransactionInfo.getProgramUser();
    flagDtls.recordStatus = RECORDSTATUS.NORMAL;
    flagDtls.startDate = startDate;
    flagDtls.type = urgentFlagType;
    BDMCaseUrgentFlagFactory.newInstance().createCaseUrgentFlag(flagDtls);

  }

  /**
   * Creates test data for task with deadline date set
   *
   * @throws AppException
   * @throws InformationalException
   */
  private void createTaskData(final String username,
    final long communicationID, final long caseID)
    throws AppException, InformationalException {

    // create task object
    final TaskDtls taskCreateDetail = new TaskDtls();

    taskCreateDetail.taskID = UniqueID.nextUniqueID();
    taskCreateDetail.creationTime = DateTime.getCurrentDateTime();
    taskCreateDetail.status = TASKSTATUS.NOTSTARTED;
    taskCreateDetail.wdoSnapshot = "";
    taskCreateDetail.priority = TASKPRIORITY.NORMAL;
    taskCreateDetail.versionNo = 1;
    taskCreateDetail.reservedBy = username;
    TaskFactory.newInstance().insert(taskCreateDetail);

    // create task assignment
    final TaskAssignmentDtls taskAssignmentDtls = new TaskAssignmentDtls();
    taskAssignmentDtls.taskID = taskCreateDetail.taskID;
    taskAssignmentDtls.relatedName = username;
    taskAssignmentDtls.assigneeType = TARGETITEMTYPE.USER;

    TaskAssignmentFactory.newInstance().insert(taskAssignmentDtls);

    // Create task deadline
    final WorkflowDeadlineDtls workflowDeadlineDtls =
      new WorkflowDeadlineDtls();
    workflowDeadlineDtls.deadlineID = UniqueID.nextUniqueID();
    workflowDeadlineDtls.taskID = taskCreateDetail.taskID;
    workflowDeadlineDtls.suspended = false;
    workflowDeadlineDtls.deadlineTime =
      DateTime.getCurrentDateTime().addTime(72, 0, 0);
    workflowDeadlineDtls.deadlineType = "WDT1";
    workflowDeadlineDtls.versionNo = 1;

    WorkflowDeadlineFactory.newInstance().insert(workflowDeadlineDtls);

    // Create Biz Obje assocuation
    BizObjAssociationDtls bizObjAssociationDtls = new BizObjAssociationDtls();
    bizObjAssociationDtls.bizObjAssocID = UniqueID.nextUniqueID();
    bizObjAssociationDtls.taskID = taskCreateDetail.taskID;
    bizObjAssociationDtls.bizObjectID = communicationID;
    bizObjAssociationDtls.bizObjectType = BUSINESSOBJECTTYPE.BDMCOMMUNICATION;

    BizObjAssociationFactory.newInstance().insert(bizObjAssociationDtls);

    bizObjAssociationDtls = new BizObjAssociationDtls();
    bizObjAssociationDtls.bizObjAssocID = taskCreateDetail.taskID;
    bizObjAssociationDtls.taskID = taskCreateDetail.taskID;
    bizObjAssociationDtls.bizObjectID = caseID;
    bizObjAssociationDtls.bizObjectType = BUSINESSOBJECTTYPE.CASE;

    BizObjAssociationFactory.newInstance().insert(bizObjAssociationDtls);

  }

  /***
   *
   *
   * Create BDMTAskEscalationLevel Object
   */
  private void createTaskEscalationLevel(final long commID)
    throws AppException, InformationalException {

    final BDMEscalationLevel bdmEscalationLevelObj =
      BDMEscalationLevelFactory.newInstance();

    final BDMEscalationLevelDtls escalationLevelDtls =
      new BDMEscalationLevelDtls();
    escalationLevelDtls.bdmEscalationLevelID = UniqueID.nextUniqueID();
    escalationLevelDtls.communicationID = commID;
    escalationLevelDtls.escalationLevel =
      BDMESCALATIONLEVELS.ESCALATION_LEVEL_1;
    escalationLevelDtls.versionNo = 1;
    BDMEscalationLevelFactory.newInstance().insert(escalationLevelDtls);

  }
}
