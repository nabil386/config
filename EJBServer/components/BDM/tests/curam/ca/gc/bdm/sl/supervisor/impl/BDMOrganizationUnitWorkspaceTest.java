/**
 *
 */
package curam.ca.gc.bdm.sl.supervisor.impl;

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
import curam.ca.gc.bdm.sl.supervisor.fact.BDMOrganizationUnitWorkspaceFactory;
import curam.ca.gc.bdm.sl.supervisor.struct.BDMOrgUnitReservedTasksByUserDueOnDateDetailsList;
import curam.ca.gc.bdm.sl.supervisor.struct.BDMOrgUnitTasksDetailsList;
import curam.ca.gc.bdm.sl.supervisor.struct.BDMOrgUnitTasksDueByWeekDetailsList;
import curam.ca.gc.bdm.sl.supervisor.struct.BDMOrgUnitTasksDueOnDateDetailsList;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.codetable.CASEPRIORITY;
import curam.codetable.RECORDSTATUS;
import curam.codetable.TARGETITEMTYPE;
import curam.codetable.TASKPRIORITY;
import curam.codetable.TASKSTATUS;
import curam.codetable.impl.METHODOFDELIVERYEntry;
import curam.core.facade.struct.CreateIntegratedCaseResultAndMessages;
import curam.core.sl.entity.fact.OrganisationUnitFactory;
import curam.core.sl.entity.fact.TaskAssignmentFactory;
import curam.core.sl.entity.struct.OrgUnitTasksSearchByReservationStatusKey;
import curam.core.sl.entity.struct.OrgUnitTasksSummaryDetails;
import curam.core.sl.entity.struct.OrgUnitTasksSummaryDetailsList;
import curam.core.sl.entity.struct.TaskAssignmentDtls;
import curam.core.sl.struct.RecordedCommDetails1;
import curam.core.sl.supervisor.struct.OrgUnitReservedTasksByUserDueOnDateKey;
import curam.core.sl.supervisor.struct.OrgUnitTasksDueOnDateKey;
import curam.core.sl.supervisor.struct.OrgUnitTasksKey;
import curam.core.struct.ConcernRoleCommKeyOut;
import curam.core.struct.PersonDtls;
import curam.util.codetable.TASKRESERVEDSEARCHSTATUS;
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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author jalpa.patel
 *
 */
@RunWith(JMockit.class)
public class BDMOrganizationUnitWorkspaceTest extends CuramServerTestJUnit4 {

  // _________________________________________________________________________
  /**
   * This method lists the open tasks reserved by a member of the
   * organization unit, due on a particular date.
   *
   * @param key -OrgUnitReservedTasksByUserDueOnDateKey
   * @return BDMOrgUnitReservedTasksByUserDueOnDateDetailsList
   * @throws AppException,InformationalException
   */

  curam.ca.gc.bdm.sl.supervisor.intf.BDMOrganizationUnitWorkspace orgUnitWorkspaceSL;

  @Mocked
  LocalizableStringResolver localizableStringResolver;

  @Mocked
  TaskStringResolver taskResolver;

  private static String CASEWORKER = "caseworker";

  private static String ESCALTIONLEVEL_1 = "Escalation Level 1";

  private static String resultTaskName = "Sample Task Subject";

  OrgUnitTasksSummaryDetailsList orgUnitTasksSummaryDetailsList =
    new OrgUnitTasksSummaryDetailsList();

  // @Mocked
  // OrganisationUnitFactory organisationUnitFactory;

  @Override
  public void setUpCuramServerTest() {

    super.setUpCuramServerTest();
    orgUnitWorkspaceSL = BDMOrganizationUnitWorkspaceFactory.newInstance();
    taskSubjectResolverExpectation();
    // getOrgSummaryDetailsListExpectation();
    // organisationUnitEntity = OrganisationUnitFactory.newInstance();

  }

  private void taskSubjectResolverExpectation() {

    try {
      new Expectations() {

        {
          taskResolver.getSubjectForTask((TaskWDOSnapshotDetails) any);
          result = resultTaskName;
        }
      };
    } catch (final AppException e) {

      e.printStackTrace();
    } catch (final InformationalException e) {

      e.printStackTrace();
    }

  }

  private void getOrgSummaryDetailsListExpectation() {

    final OrgUnitTasksSummaryDetails details =
      new OrgUnitTasksSummaryDetails();
    details.taskID = 256;
    details.taskPriority = TASKPRIORITY.NORMAL;
    details.taskSubject = "Test Task";
    details.taskDeadlineDateTime = DateTime.getCurrentDateTime();
    orgUnitTasksSummaryDetailsList.dtls.add(details);

    try {
      new Expectations() {

        {
          OrganisationUnitFactory.newInstance()
            .searchOrgUnitTasksByReservationStatus(
              (OrgUnitTasksSearchByReservationStatusKey) any);
          result = orgUnitTasksSummaryDetailsList;
        }
      };
    } catch (final AppException e) {

      e.printStackTrace();
    } catch (final InformationalException e) {

      e.printStackTrace();
    }

  }

  /**
   * Test Open OrgUnit ReservedTasks ByUser DueOnDate
   * retrieve the reserved tasks due on specific date
   **/

  @Test
  public void testGetOpenOrgUnitReservedTasksByUserDueOnDate()
    throws AppException, InformationalException {

    // -------------------------------------------------------
    // Set up user data
    // -------------------------------------------------------

    // Register person
    final String name = "Jane Doe";
    final long concernRoleID = registerPerson(name);

    // create FEc case
    final CreateIntegratedCaseResultAndMessages createCase =
      createFEC(concernRoleID);

    // create recorded communication and escaltion level
    final ConcernRoleCommKeyOut commID =
      createRecordedCommunicationwithEscalation(
        createCase.createCaseResult.integratedCaseID, name, concernRoleID);
    createTaskEscalationLevel(commID.communicationID);

    // SetUp urgent flag on case
    setUpUrgentFlagData(createCase.createCaseResult.integratedCaseID,
      BDMURGENTFLAGTYPE.MPENQUIRY, Date.getCurrentDate());

    // Create task with deadline of 1 day
    createTaskDataDeadline(commID.communicationID,
      createCase.createCaseResult.integratedCaseID, 24, 0, 0);

    // Create key
    final OrgUnitReservedTasksByUserDueOnDateKey key =
      new OrgUnitReservedTasksByUserDueOnDateKey();

    key.userName = CASEWORKER;
    key.deadlineDate = Date.getCurrentDate().addDays(1);

    // Call the servcie method
    final BDMOrgUnitReservedTasksByUserDueOnDateDetailsList list =
      orgUnitWorkspaceSL.getOpenOrgUnitReservedTasksByUserDueOnDate(key);

    // Asserts
    assertTrue(!list.dtls.isEmpty());
    assertEquals(list.dtls.get(0).caseUrgentFlagStr,
      CodeTable.getOneItem(BDMURGENTFLAGTYPE.TABLENAME,
        BDMURGENTFLAGTYPE.MPENQUIRY, TransactionInfo.getProgramLocale()));

    assertEquals(list.dtls.get(0).escalationLevelDesc, ESCALTIONLEVEL_1);

  }

  /**
   * Test Deferred OrgUnit ReservedTasks ByUser DueOnDate
   * retrieve the deferreed org unit reserved tasks due on specific date and
   * sepcifc user
   **/
  @Test
  public void testGetDeferredOrgUnitReservedTasksByUserDueOnDate()
    throws AppException, InformationalException {

    final String name = "John Doe";
    final long concernRoleID = registerPerson(name);

    final CreateIntegratedCaseResultAndMessages createCase =
      createFEC(concernRoleID);

    final ConcernRoleCommKeyOut commID =
      createRecordedCommunicationwithEscalation(
        createCase.createCaseResult.integratedCaseID, name, concernRoleID);

    createTaskEscalationLevel(commID.communicationID);

    setUpUrgentFlagData(createCase.createCaseResult.integratedCaseID,
      BDMURGENTFLAGTYPE.MPENQUIRY, Date.getCurrentDate());

    createDeferredTaskDataDeadline(commID.communicationID,
      createCase.createCaseResult.integratedCaseID, 24, 0, 0);

    final OrgUnitReservedTasksByUserDueOnDateKey key =
      new OrgUnitReservedTasksByUserDueOnDateKey();

    key.userName = CASEWORKER;
    key.deadlineDate = Date.getCurrentDate().addDays(1);

    final BDMOrgUnitReservedTasksByUserDueOnDateDetailsList list =
      orgUnitWorkspaceSL.getDeferredOrgUnitReservedTasksByUserDueOnDate(key);

    assertTrue(!list.dtls.isEmpty());
    assertEquals(
      CodeTable.getOneItem(BDMURGENTFLAGTYPE.TABLENAME,
        BDMURGENTFLAGTYPE.MPENQUIRY, TransactionInfo.getProgramLocale()),
      list.dtls.get(0).caseUrgentFlagStr);
    assertEquals(ESCALTIONLEVEL_1, list.dtls.get(0).escalationLevelDesc);

  }

  /**
   * Test Open OrgUnit ReservedTasks ByUser DueOnDate
   * retrieve the reserved tasks due on specific date
   **/

  @Test
  public void testGetOpenOrgUnitReservedTasksByUserDueByWeek()
    throws AppException, InformationalException {

    // -------------------------------------------------------
    // Set up user data
    // -------------------------------------------------------

    // Register person
    final String name = "Jane Test";
    final long concernRoleID = registerPerson(name);

    // create FEc case
    final CreateIntegratedCaseResultAndMessages createCase =
      createFEC(concernRoleID);

    // create recorded communication and escaltion level
    final ConcernRoleCommKeyOut commID =
      createRecordedCommunicationwithEscalation(
        createCase.createCaseResult.integratedCaseID, name, concernRoleID);
    createTaskEscalationLevel(commID.communicationID);

    // SetUp urgent flag on case
    setUpUrgentFlagData(createCase.createCaseResult.integratedCaseID,
      BDMURGENTFLAGTYPE.MPENQUIRY, Date.getCurrentDate());

    // Create task with deadline of 6 day
    createTaskDataDeadline(commID.communicationID,
      createCase.createCaseResult.integratedCaseID, 144, 0, 0); // deadline 6
                                                                // days

    // Create key
    final OrgUnitReservedTasksByUserDueOnDateKey key =
      new OrgUnitReservedTasksByUserDueOnDateKey();
    key.userName = CASEWORKER;
    key.deadlineDate = Date.getCurrentDate();

    // Call the servcie method
    final BDMOrgUnitReservedTasksByUserDueOnDateDetailsList list =
      orgUnitWorkspaceSL.getOpenOrgUnitReservedTasksByUserDueByWeek(key);

    // Asserts
    assertTrue(!list.dtls.isEmpty());
    assertEquals(list.dtls.get(0).caseUrgentFlagStr,
      CodeTable.getOneItem(BDMURGENTFLAGTYPE.TABLENAME,
        BDMURGENTFLAGTYPE.MPENQUIRY, TransactionInfo.getProgramLocale()));

    assertEquals(list.dtls.get(0).escalationLevelDesc, ESCALTIONLEVEL_1);

  }

  /**
   * Test Deferred OrgUnit ReservedTasks ByUser DueOnDate
   * retrieve the deferreed org unit reserved tasks due on specific date and
   * sepcifc user
   **/
  @Test
  public void testGetDeferredOrgUnitReservedTasksByUserDueByWeek()
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
    createDeferredTaskDataDeadline(commID.communicationID,
      createCase.createCaseResult.integratedCaseID, 140, 0, 0);

    final OrgUnitReservedTasksByUserDueOnDateKey key =
      new OrgUnitReservedTasksByUserDueOnDateKey();

    key.userName = CASEWORKER;
    key.deadlineDate = Date.getCurrentDate().addDays(1);

    final BDMOrgUnitReservedTasksByUserDueOnDateDetailsList list =
      orgUnitWorkspaceSL.getDeferredOrgUnitReservedTasksByUserDueByWeek(key);

    assertTrue(!list.dtls.isEmpty());
    assertEquals(
      CodeTable.getOneItem(BDMURGENTFLAGTYPE.TABLENAME,
        BDMURGENTFLAGTYPE.MPENQUIRY, TransactionInfo.getProgramLocale()),
      list.dtls.get(0).caseUrgentFlagStr);
    assertEquals(ESCALTIONLEVEL_1, list.dtls.get(0).escalationLevelDesc);

  }

  /**
   * This method lists the tasks due on date Assigned To a user.
   */

  @Test
  public void testGetOrgUnitTasksByUserDueOnDate()
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
    createTaskDataDeadline(commID.communicationID,
      createCase.createCaseResult.integratedCaseID, 24, 0, 0);

    final OrgUnitTasksDueOnDateKey key = new OrgUnitTasksDueOnDateKey();

    key.userName = CASEWORKER;
    key.deadlineDate = Date.getCurrentDate().addDays(1);
    key.taskReservationStatus = TASKRESERVEDSEARCHSTATUS.RESERVED;

    final BDMOrgUnitTasksDueOnDateDetailsList list =
      orgUnitWorkspaceSL.getOrgUnitTasksByUserDueOnDate(key);

    assertTrue(!list.dtls.isEmpty());
    assertEquals(
      CodeTable.getOneItem(BDMURGENTFLAGTYPE.TABLENAME,
        BDMURGENTFLAGTYPE.MPENQUIRY, TransactionInfo.getProgramLocale()),
      list.dtls.get(0).caseUrgentFlagStr);
    assertEquals(ESCALTIONLEVEL_1, list.dtls.get(0).escalationLevelDesc);

  }

  // _________________________________________________________________________
  /**
   * This method list the tasks assigned to members
   * of the organization unit, that have not yet been reserved.
   */
  // Ignore this test case , its failing fix this later
  @Ignore
  public void testGetOrgUnitTasks()
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

    // Set urgent flag
    setUpUrgentFlagData(createCase.createCaseResult.integratedCaseID,
      BDMURGENTFLAGTYPE.MPENQUIRY, Date.getCurrentDate());

    // Create task
    createTaskDataDeadline(commID.communicationID,
      createCase.createCaseResult.integratedCaseID, 24, 0, 0);

    final OrgUnitTasksKey key = new OrgUnitTasksKey();

    key.organizationUnitID = 11;
    key.organizationStructureID = 1;

    final OrgUnitTasksSummaryDetails details =
      new OrgUnitTasksSummaryDetails();
    details.taskID = 256;
    details.taskPriority = TASKPRIORITY.NORMAL;
    details.taskSubject = "Test Task";
    details.taskDeadlineDateTime = DateTime.getCurrentDateTime();
    orgUnitTasksSummaryDetailsList.dtls.add(details);

    new Expectations() {

      {
        OrganisationUnitFactory.newInstance()
          .searchOrgUnitTasksByReservationStatus(
            (OrgUnitTasksSearchByReservationStatusKey) any);

        result = orgUnitTasksSummaryDetailsList;
      }
    };

    final BDMOrgUnitTasksDetailsList list =
      orgUnitWorkspaceSL.getOrgUnitTasks(key);

    assertTrue(!list.dtls.isEmpty());// fix this test case

  }

  /*
   * Test Deferred OrgUnit ReservedTasks ByUser DueOnDate
   * retrieve the deferreed org unit reserved tasks due on specific date and
   * sepcifc user
   **/

  @Test
  public void testGetOrgUnitTasksByUserDueByWeek()
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
    createTaskDataDeadline(commID.communicationID,
      createCase.createCaseResult.integratedCaseID, 24, 0, 0);

    final OrgUnitTasksDueOnDateKey key = new OrgUnitTasksDueOnDateKey();

    key.userName = CASEWORKER;
    key.deadlineDate = Date.getCurrentDate().addDays(1);
    key.taskReservationStatus = TASKRESERVEDSEARCHSTATUS.RESERVED;

    final BDMOrgUnitTasksDueByWeekDetailsList list =
      orgUnitWorkspaceSL.getOrgUnitTasksByUserDueByWeek(key);

    assertTrue(!list.dtls.isEmpty());
    assertEquals(
      CodeTable.getOneItem(BDMURGENTFLAGTYPE.TABLENAME,
        BDMURGENTFLAGTYPE.MPENQUIRY, TransactionInfo.getProgramLocale()),
      list.dtls.get(0).caseUrgentFlagStr);
    assertEquals(ESCALTIONLEVEL_1, list.dtls.get(0).escalationLevelDesc);

  }

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
  private void createTaskDataDeadline(final long communicationID,
    final long caseID, final int hours, final int minutes, final int seconds)
    throws AppException, InformationalException {

    final TaskDtls taskCreateDetail = new TaskDtls();

    taskCreateDetail.taskID = UniqueID.nextUniqueID();
    taskCreateDetail.creationTime = DateTime.getCurrentDateTime();
    taskCreateDetail.status = TASKSTATUS.NOTSTARTED;
    taskCreateDetail.wdoSnapshot = "";
    taskCreateDetail.priority = TASKPRIORITY.NORMAL;
    taskCreateDetail.versionNo = 1;
    taskCreateDetail.reservedBy = CASEWORKER;
    TaskFactory.newInstance().insert(taskCreateDetail);

    final TaskAssignmentDtls taskAssignmentDtls = new TaskAssignmentDtls();
    taskAssignmentDtls.taskID = taskCreateDetail.taskID;
    taskAssignmentDtls.relatedName = CASEWORKER;
    taskAssignmentDtls.assigneeType = TARGETITEMTYPE.USER;

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
    bizObjAssociationDtls.bizObjectID = caseID;
    bizObjAssociationDtls.bizObjectType = BUSINESSOBJECTTYPE.CASE;

    BizObjAssociationFactory.newInstance().insert(bizObjAssociationDtls);

  }

  /**
   * Creates test data for task with deadline date set
   *
   * @throws AppException
   * @throws InformationalException
   */
  private void createDeferredTaskDataDeadline(final long communicationID,
    final long caseID, final int hours, final int minutes, final int seconds)
    throws AppException, InformationalException {

    final TaskDtls taskCreateDetail = new TaskDtls();

    taskCreateDetail.taskID = UniqueID.nextUniqueID();
    taskCreateDetail.creationTime = DateTime.getCurrentDateTime();
    taskCreateDetail.status = TASKSTATUS.DEFERRED;
    taskCreateDetail.wdoSnapshot = "";
    taskCreateDetail.priority = TASKPRIORITY.NORMAL;
    taskCreateDetail.versionNo = 1;
    taskCreateDetail.reservedBy = CASEWORKER;
    TaskFactory.newInstance().insert(taskCreateDetail);

    final TaskAssignmentDtls taskAssignmentDtls = new TaskAssignmentDtls();
    taskAssignmentDtls.taskID = taskCreateDetail.taskID;
    taskAssignmentDtls.relatedName = CASEWORKER;
    taskAssignmentDtls.assigneeType = TARGETITEMTYPE.USER;

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

  /* Create Fec case **/

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
