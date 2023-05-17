package curam.ca.gc.bdm.facade.organization.supervisor.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskSkillTypeKey;
import curam.ca.gc.bdm.facade.organization.supervisor.struct.BDMAssignedTaskForUserList;
import curam.ca.gc.bdm.facade.organization.supervisor.struct.BDMListReservedDeferredTasksByUserDetails;
import curam.ca.gc.bdm.facade.organization.supervisor.struct.BDMListReservedOpenTasksByUserDetails;
import curam.ca.gc.bdm.facade.organization.supervisor.struct.BDMListUnreservedTasksForUserDetails;
import curam.ca.gc.bdm.facade.organization.supervisor.struct.BDMListUserTasksByWeekDetails;
import curam.ca.gc.bdm.facade.organization.supervisor.struct.BDMListUserTasksDueOnDateDetails;
import curam.ca.gc.bdm.facade.organization.supervisor.struct.BDMOpenAndDeferredTasksForReallocateTasksDetails;
import curam.ca.gc.bdm.facade.organization.supervisor.struct.BDMReservedTaskByUserList;
import curam.ca.gc.bdm.facade.organization.supervisor.struct.BDMUserAssignedTaskDetailsAndFilterCriteriaDetails;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMAssignedTaskDetails;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMDeferredTaskDetails;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMEscalationLevelString;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMReservedByStatusTaskDetails;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMTaskQueryResultDetails;
import curam.ca.gc.bdm.sl.bdminbox.struct.CaseUrgentFlagStringDetails;
import curam.ca.gc.bdm.sl.organization.supervisor.struct.BDMSupervisorUserWithTaskCountDetailsList;
import curam.ca.gc.bdm.sl.supervisor.fact.BDMUserWorkspaceFactory;
import curam.ca.gc.bdm.sl.supervisor.intf.BDMUserWorkspace;
import curam.ca.gc.bdm.util.impl.BDMTaskUtil;
import curam.codetable.ASSIGNEETYPE;
import curam.core.fact.GenericBatchProcessInputFactory;
import curam.core.fact.SystemUserFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.SecurityImplementationFactory;
import curam.core.intf.GenericBatchProcessInput;
import curam.core.intf.SystemUser;
import curam.core.sl.entity.struct.TaskCountDetails;
import curam.core.sl.entity.struct.TaskKey;
import curam.core.sl.infrastructure.impl.ClientActionConst;
import curam.core.sl.struct.DeferredTaskDetails;
import curam.core.sl.struct.ReservedByStatusTaskDetails;
import curam.core.sl.struct.TaskQueryResultDetails;
import curam.core.struct.BatchQueuedTaskID;
import curam.core.struct.BatchQueuedTaskIDList;
import curam.core.struct.InformationalMsgDtls;
import curam.core.struct.InformationalMsgDtlsList;
import curam.core.struct.UserNameKey;
import curam.supervisor.facade.struct.AssignedTaskForUserList;
import curam.supervisor.facade.struct.AssignedTaskForwardDetails;
import curam.supervisor.facade.struct.ListDeferredTasksReservedByUserKey;
import curam.supervisor.facade.struct.ListOpenTasksReservedByUserKey;
import curam.supervisor.facade.struct.ListReservedDeferredTasksByUserDetails;
import curam.supervisor.facade.struct.ListReservedOpenTasksByUserDetails;
import curam.supervisor.facade.struct.ListUnreservedTasksForUserDetails;
import curam.supervisor.facade.struct.ListUnreservedTasksForUserKey;
import curam.supervisor.facade.struct.ReservedTaskByUserList;
import curam.supervisor.facade.struct.ReservedTaskForwardDetails;
import curam.supervisor.facade.struct.SupervisorTaskFwdDetails;
import curam.supervisor.facade.struct.UserAssignedTaskDetailsAndFilterCriteriaDetails;
import curam.supervisor.facade.struct.UserNameAndDeadLineDateKey;
import curam.supervisor.sl.fact.MaintainSupervisorUsersFactory;
import curam.supervisor.sl.fact.SupervisorApplicationPageContextDescriptionFactory;
import curam.supervisor.sl.struct.SupervisorApplicationPageContextDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement.InformationalType;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.message.CatEntry;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;
import curam.util.type.StringList;

public class BDMMaintainSupervisorUsers extends
  curam.ca.gc.bdm.facade.organization.supervisor.base.BDMMaintainSupervisorUsers {

  @Inject
  curam.ca.gc.bdm.sl.organization.supervisor.impl.BDMMaintainSupervisorUsers bdmMaintainSupervisorUsersImpl;

  @Inject
  curam.ca.gc.bdm.sl.organization.supervisor.intf.BDMMaintainSupervisorUsers bdmMaintainSupervisorUsersIntf;

  public BDMMaintainSupervisorUsers() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Facade Method go retrieve the list of users for a supervisor with their
   * task count and their skills. This is a copy of the OOTB method, where the
   * only difference is the SL method being used
   */
  @Override
  public BDMSupervisorUserWithTaskCountDetailsList
    listSupervisorUsersWithTaskCountAndSkills()
      throws AppException, InformationalException {

    // register the security implementation
    SecurityImplementationFactory.register();

    final curam.supervisor.sl.intf.SupervisorApplicationPageContextDescription pageContextDescription =
      SupervisorApplicationPageContextDescriptionFactory.newInstance();

    final UserNameKey userNameKey = new UserNameKey();
    final SystemUser systemUser = SystemUserFactory.newInstance();

    // call the SL method to get the list of supervisor's users
    final BDMSupervisorUserWithTaskCountDetailsList userDetails =
      bdmMaintainSupervisorUsersImpl
        .listSupervisorUsersWithTaskCountAndSkills();

    // get the user name and the page context details for the user
    userNameKey.userName = systemUser.getUserDetails().userName;
    final SupervisorApplicationPageContextDetails pageContextDetails =
      pageContextDescription.readUserNamePageContextDescription(userNameKey);

    userDetails.pageContextDescription = pageContextDetails.description;

    return userDetails;
  }

  /**
   * This method allows the supervisor to list the tasks assigned to a user, but
   * not currently reserved by the user. The list does not include tasks
   * assigned to work queues that the user is subscribed to. This method also
   * returns the saved filter criteria details for UserAssignedTasks page.
   *
   * @param ListUnreservedTasksForUserKey
   * User name details.
   *
   * @return BDMUserAssignedTaskDetailsAndFilterCriteriaDetails List of tasks
   * assigned to work queue and filter criteria details
   *
   * @throws AppException
   * Generic Exception Signature.
   * @throws InformationalException
   * Generic Exception Signature.
   */

  @Override
  public BDMUserAssignedTaskDetailsAndFilterCriteriaDetails
    listBDMUserAssignedTasksAndFilterCriteriaDetails(
      final ListUnreservedTasksForUserKey key)
      throws AppException, InformationalException {

    final BDMUserAssignedTaskDetailsAndFilterCriteriaDetails bdmUserAssignedTaskDetailsAndFilterCriteriaDetails =
      new BDMUserAssignedTaskDetailsAndFilterCriteriaDetails();

    UserAssignedTaskDetailsAndFilterCriteriaDetails userAssignedTaskDetailsAndFilterCriteriaDetails =
      new UserAssignedTaskDetailsAndFilterCriteriaDetails();

    // Call OOTB method
    userAssignedTaskDetailsAndFilterCriteriaDetails =
      super.listUserAssignedTasksAndFilterCriteriaDetails(key);

    bdmUserAssignedTaskDetailsAndFilterCriteriaDetails
      .assign(userAssignedTaskDetailsAndFilterCriteriaDetails);

    BDMEscalationLevelString bdmEscalationLevel = null;
    TaskKey taskKey = null;
    CaseUrgentFlagStringDetails caseUrgentFlagStringDetails = null;
    BDMAssignedTaskDetails bdmAssignedTaskDetails = null;

    BDMTaskSkillTypeKey bdmTaskSkillTypeKey = null;
    for (int i =
      0; i < bdmUserAssignedTaskDetailsAndFilterCriteriaDetails.dtls.dtls.taskDetailsList.taskDetailsList
        .size(); i++) {

      bdmAssignedTaskDetails = new BDMAssignedTaskDetails();
      bdmAssignedTaskDetails =
        bdmUserAssignedTaskDetailsAndFilterCriteriaDetails.dtls.dtls.taskDetailsList.taskDetailsList
          .get(i);

      taskKey = new TaskKey();
      caseUrgentFlagStringDetails = new CaseUrgentFlagStringDetails();
      taskKey.taskID = bdmAssignedTaskDetails.taskID;
      caseUrgentFlagStringDetails =
        bdmMaintainSupervisorUsersImpl.getCaseUrgentFlagsByTaskID(taskKey);

      bdmAssignedTaskDetails.caseUrgentFlagStr =
        caseUrgentFlagStringDetails.caseUrgentFlagStr;

      bdmTaskSkillTypeKey = new BDMTaskSkillTypeKey();
      bdmTaskSkillTypeKey =
        bdmMaintainSupervisorUsersIntf.readSkillTypeByTaskID(taskKey);

      bdmAssignedTaskDetails.skillType = bdmTaskSkillTypeKey.skillType;

      // TASK : 81188 Retrieve escalation Level
      bdmEscalationLevel = new BDMEscalationLevelString();
      bdmEscalationLevel =
        bdmMaintainSupervisorUsersIntf.readEscalationLevelByTaskID(taskKey);

      bdmAssignedTaskDetails.escalationLevelDesc =
        bdmEscalationLevel.escalationLevelDesc;

    }

    return bdmUserAssignedTaskDetailsAndFilterCriteriaDetails;
  }

  /**
   * This method allows the supervisor to fetch list of tasks associated with
   * the case, which have been reserved by a particular user, and then deferred.
   *
   * @param key
   * Contains the user name.
   *
   * @return BDMListReservedDeferredTasksByUserDetails -The list of details of
   * deferred tasks reserved by user.
   *
   * @throws AppException
   * Generic Exception Signature.
   * @throws InformationalException
   * Generic Exception Signature.
   */
  @Override
  public BDMListReservedDeferredTasksByUserDetails
    listBDMReservedDeferredTasksByUser(
      final ListDeferredTasksReservedByUserKey key)
      throws AppException, InformationalException {

    final BDMListReservedDeferredTasksByUserDetails bdmListReservedDeferredTasksByUserDetails =
      new BDMListReservedDeferredTasksByUserDetails();

    ListReservedDeferredTasksByUserDetails listReservedDeferredTasksByUserDetails =
      new ListReservedDeferredTasksByUserDetails();

    // Call Super class method
    listReservedDeferredTasksByUserDetails =
      super.listReservedDeferredTasksByUser(key);

    bdmListReservedDeferredTasksByUserDetails
      .assign(listReservedDeferredTasksByUserDetails);

    TaskKey taskKey = null;
    CaseUrgentFlagStringDetails caseUrgentFlagStringDetails = null;
    BDMDeferredTaskDetails bdmDeferredTaskDetails = null;
    // Task - 25517 - retrieve skill type
    BDMTaskSkillTypeKey bdmTaskSkillTypeKey = null;
    BDMEscalationLevelString bdmEscalationLevel = null;
    for (int i =
      0; i < bdmListReservedDeferredTasksByUserDetails.dtls.taskDetailsList.taskDetailsList
        .size(); i++) {

      bdmDeferredTaskDetails = new BDMDeferredTaskDetails();
      bdmDeferredTaskDetails =
        bdmListReservedDeferredTasksByUserDetails.dtls.taskDetailsList.taskDetailsList
          .get(i);

      taskKey = new TaskKey();
      caseUrgentFlagStringDetails = new CaseUrgentFlagStringDetails();
      taskKey.taskID = bdmDeferredTaskDetails.taskID;
      caseUrgentFlagStringDetails =
        bdmMaintainSupervisorUsersImpl.getCaseUrgentFlagsByTaskID(taskKey);

      bdmDeferredTaskDetails.caseUrgentFlagStr =
        caseUrgentFlagStringDetails.caseUrgentFlagStr;

      bdmTaskSkillTypeKey = new BDMTaskSkillTypeKey();
      bdmTaskSkillTypeKey =
        bdmMaintainSupervisorUsersIntf.readSkillTypeByTaskID(taskKey);

      bdmDeferredTaskDetails.skillType = bdmTaskSkillTypeKey.skillType;

      // TASK : 81173 Retrieve escalation Level
      bdmEscalationLevel = new BDMEscalationLevelString();
      bdmEscalationLevel =
        bdmMaintainSupervisorUsersIntf.readEscalationLevelByTaskID(taskKey);

      bdmDeferredTaskDetails.escalationLevelDesc =
        bdmEscalationLevel.escalationLevelDesc;

    }

    return bdmListReservedDeferredTasksByUserDetails;
  }

  /**
   * This method allows the supervisor to fetch a list of tasks associated with
   * the case, which have been reserved by a particular user, that are open.
   *
   * @param key
   * Contains the user name.
   *
   * @return BDMListReservedOpenTasksByUserDetails The list of details of open
   * tasks reserved by user.
   *
   * @throws AppException
   * Generic Exception Signature.
   * @throws InformationalException
   * Generic Exception Signature.
   */
  @Override
  public BDMListReservedOpenTasksByUserDetails
    listBDMReservedOpenTasksByUser(final ListOpenTasksReservedByUserKey key)
      throws AppException, InformationalException {

    // create return object
    final BDMListReservedOpenTasksByUserDetails bdmListReservedOpenTasksByUserDetails =
      new BDMListReservedOpenTasksByUserDetails();

    ListReservedOpenTasksByUserDetails listReservedOpenTasksByUserDetails =
      new ListReservedOpenTasksByUserDetails();

    // Call Super class method
    listReservedOpenTasksByUserDetails =
      super.listReservedOpenTasksByUser(key);

    bdmListReservedOpenTasksByUserDetails
      .assign(listReservedOpenTasksByUserDetails);

    TaskKey taskKey = null;
    CaseUrgentFlagStringDetails caseUrgentFlagStringDetails = null;
    BDMReservedByStatusTaskDetails bdmReservedByStatusTaskDetails = null;

    BDMTaskSkillTypeKey bdmTaskSkillTypeKey = null;
    BDMEscalationLevelString bdmEscalationLevel = null;
    for (int i =
      0; i < bdmListReservedOpenTasksByUserDetails.dtls.taskDetailsList.taskDetailsList
        .size(); i++) {

      bdmReservedByStatusTaskDetails = new BDMReservedByStatusTaskDetails();
      bdmReservedByStatusTaskDetails =
        bdmListReservedOpenTasksByUserDetails.dtls.taskDetailsList.taskDetailsList
          .get(i);

      taskKey = new TaskKey();
      caseUrgentFlagStringDetails = new CaseUrgentFlagStringDetails();
      taskKey.taskID = bdmReservedByStatusTaskDetails.taskID;
      caseUrgentFlagStringDetails =
        bdmMaintainSupervisorUsersImpl.getCaseUrgentFlagsByTaskID(taskKey);

      bdmReservedByStatusTaskDetails.caseUrgentFlagStr =
        caseUrgentFlagStringDetails.caseUrgentFlagStr;

      // Skill type
      bdmTaskSkillTypeKey = new BDMTaskSkillTypeKey();
      bdmTaskSkillTypeKey =
        bdmMaintainSupervisorUsersIntf.readSkillTypeByTaskID(taskKey);

      bdmReservedByStatusTaskDetails.skillType =
        bdmTaskSkillTypeKey.skillType;

      // TASK : 81173 Retrieve escalation Level
      bdmEscalationLevel = new BDMEscalationLevelString();
      bdmEscalationLevel =
        bdmMaintainSupervisorUsersIntf.readEscalationLevelByTaskID(taskKey);

      bdmReservedByStatusTaskDetails.escalationLevelDesc =
        bdmEscalationLevel.escalationLevelDesc;

    }

    return bdmListReservedOpenTasksByUserDetails;
  }

  /**
   * This method allows the supervisor to reallocate selected tasks that have
   * been reserved by the user to read, open and deferred tasks. The tasks
   * reserved by the user will be divided into 2 lists. A list of open and a
   * list of deferred tasks.
   *
   * @param key - UserNameKey
   * @return BDMOpenAndDeferredTasksForReallocateTasksDetails
   * @throws AppException, InformationalException
   * @throws InformationalException
   */
  @Override
  public BDMOpenAndDeferredTasksForReallocateTasksDetails
    bdmReadOpenAndDererredTasksForReallocateTasks(final UserNameKey key)
      throws AppException, InformationalException {

    final BDMOpenAndDeferredTasksForReallocateTasksDetails bdmOpenAndDeferredTasksForReallocateTasksDetails =
      new BDMOpenAndDeferredTasksForReallocateTasksDetails();

    // object creation
    final ListDeferredTasksReservedByUserKey listDeferredTasksReservedByUserKey =
      new ListDeferredTasksReservedByUserKey();
    final curam.supervisor.sl.intf.SupervisorApplicationPageContextDescription contextDescription =
      SupervisorApplicationPageContextDescriptionFactory.newInstance();
    final ListOpenTasksReservedByUserKey listOpenTasksReservedByUserKey =
      new ListOpenTasksReservedByUserKey();

    // assign key values
    listDeferredTasksReservedByUserKey.key.userName = key.userName;
    listOpenTasksReservedByUserKey.key.userName = key.userName;

    // get deferred tasks list
    final BDMListReservedDeferredTasksByUserDetails bdmListReservedDeferredTasksByUserDetails =
      listBDMReservedDeferredTasksByUser(listDeferredTasksReservedByUserKey);

    // assign values to OpenAndDeferredTasksForReallocateTasksDetails
    for (int i =
      0; i < bdmListReservedDeferredTasksByUserDetails.dtls.taskDetailsList.taskDetailsList
        .size(); i++) {

      final BDMDeferredTaskDetails bdmDeferredTaskDetails =
        bdmListReservedDeferredTasksByUserDetails.dtls.taskDetailsList.taskDetailsList
          .get(i);

      bdmOpenAndDeferredTasksForReallocateTasksDetails.deferredTasksDetails.dtls.taskDetailsList.taskDetailsList
        .addRef(bdmDeferredTaskDetails);

    }

    // read open tasks list
    final BDMListReservedOpenTasksByUserDetails listOpenTasksReservedByUserDetails =
      listBDMReservedOpenTasksByUser(listOpenTasksReservedByUserKey);

    // assign values to OpenAndDeferredTasksForReallocateTasksDetails
    for (int i =
      0; i < listOpenTasksReservedByUserDetails.dtls.taskDetailsList.taskDetailsList
        .size(); i++) {

      final BDMReservedByStatusTaskDetails bdmReservedByStatusTaskDetails =
        listOpenTasksReservedByUserDetails.dtls.taskDetailsList.taskDetailsList
          .get(i);

      bdmOpenAndDeferredTasksForReallocateTasksDetails.listOpenTaskDtls.taskDetailsList.taskDetailsList
        .addRef(bdmReservedByStatusTaskDetails);

    }

    bdmOpenAndDeferredTasksForReallocateTasksDetails.pageContext =
      contextDescription.readUserNamePageContextDescription(key);

    return bdmOpenAndDeferredTasksForReallocateTasksDetails;
  }

  /**
   * This method allows the supervisor to list the tasks assigned to a user, but
   * not currently reserved by the user. The list does not include tasks
   * assigned to work queues that the user is subscribed to.
   *
   * @param key
   * - ListUnreservedTasksForUserKey
   * @return ListUnreservedTasksForUserDetails.
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMListUnreservedTasksForUserDetails
    bdmListTasksAssignedToUser(final ListUnreservedTasksForUserKey key)
      throws AppException, InformationalException {

    final BDMListUnreservedTasksForUserDetails bdmListUnreservedTasksForUserDetails =
      new BDMListUnreservedTasksForUserDetails();
    final UserNameKey userNameKey = new UserNameKey();
    final curam.supervisor.sl.intf.SupervisorApplicationPageContextDescription contextDescription =
      SupervisorApplicationPageContextDescriptionFactory.newInstance();

    // call the SL method to list the tasks assigned to user
    bdmListUnreservedTasksForUserDetails.dtls =
      bdmMaintainSupervisorUsersImpl.listbdmTasksAssignedToUser(key.key);

    // get the user name and the page context details for the user
    userNameKey.userName = key.key.userName;
    bdmListUnreservedTasksForUserDetails.pageContext =
      contextDescription.readUserNamePageContextDescription(userNameKey);
    return bdmListUnreservedTasksForUserDetails;

  }

  /**
   * This method allows the supervisor to list all the assigned tasks due on the
   * specified date for the User. Lists Tasks details (such as assigned date,
   * deadline date, priority, Task type etc.) assigned to a user for a specified
   * date.
   *
   * @param key
   * - UserNameAndDeadLineDateKey
   * @return BDMListUserTasksDueOnDateDetails
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMListUserTasksDueOnDateDetails
    listBDMUserAssignedTasksDueOnDate(final UserNameAndDeadLineDateKey key)
      throws AppException, InformationalException {

    final BDMListUserTasksDueOnDateDetails listUserTasksDueOnDateDetails =
      new BDMListUserTasksDueOnDateDetails();
    final BDMUserWorkspace userWorkspaceObj =
      BDMUserWorkspaceFactory.newInstance();
    final curam.core.sl.supervisor.struct.UserTasksDueOnDateKey userTAsksDueOnDateKey =
      new curam.core.sl.supervisor.struct.UserTasksDueOnDateKey();

    // assign the key values
    userTAsksDueOnDateKey.userName = key.key.userName;
    userTAsksDueOnDateKey.deadlineDate = key.key.deadlineDate;
    userTAsksDueOnDateKey.taskReservationStatus = key.key.taskType;

    // call the SL method to get the user tasks
    listUserTasksDueOnDateDetails.dtls =
      userWorkspaceObj.getUserTasksDueOnDate(userTAsksDueOnDateKey);

    return listUserTasksDueOnDateDetails;
  }

  /**
   * This method allows the supervisor to list all the Assigned tasks due on a
   * specified week, for the user. Lists Tasks details (such as assigned date,
   * deadline date, priority, Task type etc., escalation level , caseUrgentFlag)
   * assigned to a user for a period
   * of specified week.
   *
   * @param key
   * - UserNameAndDeadLineDateKey
   * @return BDMListUserTasksByWeekDetails
   * @throws AppException
   * @throws InformationalException
   */

  @Override
  public BDMListUserTasksByWeekDetails
    listBDMUserAssignedTasksByWeek(final UserNameAndDeadLineDateKey key)
      throws AppException, InformationalException {

    final BDMListUserTasksByWeekDetails bdmListUserTasksByWeekDetails =
      new BDMListUserTasksByWeekDetails();
    final BDMUserWorkspace userWorkspaceObj =
      BDMUserWorkspaceFactory.newInstance();
    // object creation
    final curam.supervisor.facade.struct.ListUserTasksByWeekDetails listUserTasksByWeekDetails =
      new curam.supervisor.facade.struct.ListUserTasksByWeekDetails();
    final curam.core.sl.supervisor.struct.UserTasksDueOnDateKey userTasksDueOnDateKey =
      new curam.core.sl.supervisor.struct.UserTasksDueOnDateKey();

    // assign the key values
    userTasksDueOnDateKey.deadlineDate = key.key.deadlineDate;
    userTasksDueOnDateKey.taskReservationStatus = key.key.taskType;
    userTasksDueOnDateKey.userName = key.key.userName;

    // call the SL method to get the user tasks and assign them
    listUserTasksByWeekDetails.dtls
      .assign(userWorkspaceObj.getUserTasksDueByWeek(userTasksDueOnDateKey));

    return bdmListUserTasksByWeekDetails;
  }

  /**
   * This method allows the supervisor to list all the reserved tasks due on a
   * specified week, for the user. Lists Tasks details (such as assigned date,
   * deadline date, priority etc.) assigned to a user for a specified week.
   *
   * @param key
   * - UserNameAndDeadLineDateKey
   * @return BDMListUserTasksByWeekDetails
   * @throws AppException
   * @throws InformationalException
   */

  @Override
  public BDMListUserTasksByWeekDetails
    listBDMUserReservedTasksByWeek(final UserNameAndDeadLineDateKey key)
      throws AppException, InformationalException {

    // object creation

    final BDMListUserTasksByWeekDetails bdmListUserTasksByWeekDetails =
      new BDMListUserTasksByWeekDetails();
    final BDMUserWorkspace userWorkspaceObj =
      BDMUserWorkspaceFactory.newInstance();

    final curam.core.sl.supervisor.struct.UserTasksDueOnDateKey userTasksDueOnDateKey =
      new curam.core.sl.supervisor.struct.UserTasksDueOnDateKey();

    // assign key values
    userTasksDueOnDateKey.deadlineDate = key.key.deadlineDate;
    userTasksDueOnDateKey.taskReservationStatus = key.key.taskType;
    userTasksDueOnDateKey.userName = key.key.userName;

    // call the SL method to get the user tasks
    bdmListUserTasksByWeekDetails.dtls
      .assign(userWorkspaceObj.getUserTasksDueByWeek(userTasksDueOnDateKey));

    return bdmListUserTasksByWeekDetails;
  }

  /**
   * This method allows the supervisor to list all the reserved tasks due on the
   * specified date for the User. Lists Tasks details (such as assigned date,
   * deadline date, priority etc.) assigned to a user for a specified date.
   *
   * @param key
   * - UserNameAndDeadLineDateKey
   * @return BDMListUserTasksDueOnDateDetails
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMListUserTasksDueOnDateDetails
    listBDMUserReservedTasksDueOnDate(final UserNameAndDeadLineDateKey key)
      throws AppException, InformationalException {

    // object creation
    final BDMListUserTasksDueOnDateDetails bdmListUserTasksDueOnDateDetails =
      new BDMListUserTasksDueOnDateDetails();
    final BDMUserWorkspace userWorkspaceObj =
      BDMUserWorkspaceFactory.newInstance();
    final curam.core.sl.supervisor.struct.UserTasksDueOnDateKey userTAsksDueOnDateKey =
      new curam.core.sl.supervisor.struct.UserTasksDueOnDateKey();

    // assign key values
    userTAsksDueOnDateKey.userName = key.key.userName;
    userTAsksDueOnDateKey.deadlineDate = key.key.deadlineDate;
    userTAsksDueOnDateKey.taskReservationStatus = key.key.taskType;

    // call the SL method to get the user tasks
    bdmListUserTasksDueOnDateDetails.dtls =
      userWorkspaceObj.getUserTasksDueOnDate(userTAsksDueOnDateKey);
    return bdmListUserTasksDueOnDateDetails;
  }

  /**
   * This method allows the supervisor to list the user's task record details
   * due on a period of specified week.
   *
   * @param key
   * - UserNameAndDeadLineDateKey
   * @return BDMListUserTasksByWeekDetails
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMListUserTasksByWeekDetails
    listBDMUserTasksByWeek(final UserNameAndDeadLineDateKey key)
      throws AppException, InformationalException {

    // object creation
    final BDMListUserTasksByWeekDetails bdmListUserTasksByWeekDetails =
      new BDMListUserTasksByWeekDetails();
    final BDMUserWorkspace userWorkspaceObj =
      BDMUserWorkspaceFactory.newInstance();
    final curam.core.sl.supervisor.struct.UserTasksDueOnDateKey userTasksDueOnDateKey =
      new curam.core.sl.supervisor.struct.UserTasksDueOnDateKey();

    // assign key values
    userTasksDueOnDateKey.deadlineDate = key.key.deadlineDate;
    userTasksDueOnDateKey.taskReservationStatus = key.key.taskType;
    userTasksDueOnDateKey.userName = key.key.userName;

    // call the SL method to get user tasks
    bdmListUserTasksByWeekDetails.dtls
      .assign(userWorkspaceObj.getUserTasksDueByWeek(userTasksDueOnDateKey));

    return bdmListUserTasksByWeekDetails;
  }

  // _________________________________________________________________________
  /**
   * This method allows the supervisor to list the user's task record details
   * due on the specified date.
   *
   * @param key
   * - UserNameAndDeadLineDateKey
   * @return BDMListUserTasksDueOnDateDetails
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMListUserTasksDueOnDateDetails
    listBDMUserTasksDueOnDate(final UserNameAndDeadLineDateKey key)
      throws AppException, InformationalException {

    // object creation
    final BDMListUserTasksDueOnDateDetails bdmListUserTasksDueOnDateDetails =
      new BDMListUserTasksDueOnDateDetails();
    final BDMUserWorkspace userWorkspaceObj =
      BDMUserWorkspaceFactory.newInstance();
    final curam.core.sl.supervisor.struct.UserTasksDueOnDateKey userTasksDueOnDateKey =
      new curam.core.sl.supervisor.struct.UserTasksDueOnDateKey();

    // assign key values
    userTasksDueOnDateKey.userName = key.key.userName;
    userTasksDueOnDateKey.deadlineDate = key.key.deadlineDate;
    userTasksDueOnDateKey.taskReservationStatus = key.key.taskType;

    // call the SL method to get user tasks
    bdmListUserTasksDueOnDateDetails.dtls =
      userWorkspaceObj.getUserTasksDueOnDate(userTasksDueOnDateKey);
    return bdmListUserTasksDueOnDateDetails;
  }

  /**
   * Populates and returns the information message details.
   *
   * @param catEntry The informational message entry.
   * @param count Total number of case reassignment or tasks forward count.
   *
   * @throws InformationalException Generic Exception Signature.
   * @throws AppException Generic Exception Signature.
   */
  private InformationalMsgDtlsList
    populateInformationalMsgDtsList(final CatEntry catEntry, final int count)
      throws AppException, InformationalException {

    final InformationalMsgDtlsList informationalMsgDtlsList =
      new InformationalMsgDtlsList();
    final AppException infoMessage = new AppException(catEntry);

    infoMessage.arg(count);

    TransactionInfo.getInformationalManager().addInformationalMsg(infoMessage,
      CuramConst.gkEmpty, InformationalType.kWarning);

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();
    final String[] messages =
      informationalManager.obtainInformationalAsString();

    for (final String message : messages) {
      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();

      informationalMsgDtls.informationMsgTxt = message;
      informationalMsgDtlsList.dtls.addRef(informationalMsgDtls);
    }

    return informationalMsgDtlsList;
  }

  /**
   * Forwards the selected tasks reserved by an user to another user or
   * to a work queue or to a position or to an organization unit.
   *
   * @param reservedTaskForwardDetails The details of the reserved tasks that
   * need to be forwarded.
   *
   * @return List of tasks currently reserved by the user.
   *
   * @throws AppException Generic Exception Signature.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  public BDMReservedTaskByUserList forwardBDMTasksReservedByUserSearch(
    final ReservedTaskForwardDetails reservedTaskForwardDetails)
    throws AppException, InformationalException {

    final BDMReservedTaskByUserList reservedTaskByUserList =
      new BDMReservedTaskByUserList();

    final ReservedTaskByUserList list =
      super.forwardTasksReservedByUserSearch(reservedTaskForwardDetails);

    for (final ReservedByStatusTaskDetails taskDtls : list.openTaskDtls.taskDetailsList.taskDetailsList) {

      final BDMReservedByStatusTaskDetails bdmTask =
        new BDMReservedByStatusTaskDetails();

      bdmTask.assign(taskDtls);

      bdmTask.caseUrgentFlagStr =
        BDMTaskUtil.getCaseUrgentFlagsByTaskID(taskDtls.taskID);
      bdmTask.escalationLevelDesc =
        BDMTaskUtil.getEscalationLevelsByTaskID(taskDtls.taskID);
      reservedTaskByUserList.openTaskDtls.taskDetailsList.taskDetailsList
        .addRef(bdmTask);

    }

    for (final DeferredTaskDetails taskDtls : list.deferredTaskDtls.taskDetailsList.taskDetailsList) {

      final BDMDeferredTaskDetails bdmDeferredTask =
        new BDMDeferredTaskDetails();
      bdmDeferredTask.assign(taskDtls);

      bdmDeferredTask.caseUrgentFlagStr =
        BDMTaskUtil.getCaseUrgentFlagsByTaskID(taskDtls.taskID);
      bdmDeferredTask.escalationLevelDesc =
        BDMTaskUtil.getEscalationLevelsByTaskID(taskDtls.taskID);
      reservedTaskByUserList.deferredTaskDtls.taskDetailsList.taskDetailsList
        .addRef(bdmDeferredTask);

    }

    return reservedTaskByUserList;
  }

  /**
   * Forwards the selected tasks assigned to an user to another user or
   * to a work queue or to a position or to an organization unit.
   *
   * @param assignedTaskForwardDetails
   * The details of the unreserved tasks that need to be forwarded.
   *
   * @return List of tasks currently assigned to the user.
   *
   * @throws AppException Generic Exception Signature.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  public BDMAssignedTaskForUserList forwardBDMTasksNotReservedByUserSearch(
    final AssignedTaskForwardDetails assignedTaskForwardDetails)
    throws AppException, InformationalException {

    final AssignedTaskForUserList assignedTaskForUserList =
      new AssignedTaskForUserList();

    BDMAssignedTaskForUserList bdmAssignedTaskForUserList =
      new BDMAssignedTaskForUserList();

    final curam.supervisor.sl.intf.MaintainSupervisorUsers maintainSupervisorObj =
      MaintainSupervisorUsersFactory.newInstance();

    if (ClientActionConst.kSearchTasks
      .equals(assignedTaskForwardDetails.actionIDProperty)) {

      // BEGIN, CR00379440, IBM
      final GenericBatchProcessInput batchProcessInput =
        GenericBatchProcessInputFactory.newInstance();
      boolean isTaskBatchQueued;

      final BatchQueuedTaskIDList batchQueuedTaskIDList =
        batchProcessInput.searchAssignedTaskByBatchStatus();

      // END, CR00379440

      if (assignedTaskForwardDetails.allTasksAssignedToUser) {

        final ListUnreservedTasksForUserKey listUnreservedTasksForUserKey =
          new ListUnreservedTasksForUserKey();

        listUnreservedTasksForUserKey.key.userName =
          assignedTaskForwardDetails.taskFwdDtls.userName;

        final ListUnreservedTasksForUserDetails listUnreservedTasksForUserDetails =
          listTasksAssignedToUser(listUnreservedTasksForUserKey);

        final BDMListUnreservedTasksForUserDetails bdmlistUnreservedTasksForUserDetails =
          bdmListTasksAssignedToUser(listUnreservedTasksForUserKey);

        TaskQueryResultDetails taskQueryResultDetails = null;

        for (final BDMAssignedTaskDetails assignedTaskDetails : bdmlistUnreservedTasksForUserDetails.dtls.taskDetailsList.taskDetailsList
          .items()) {

          taskQueryResultDetails = new TaskQueryResultDetails();
          taskQueryResultDetails.taskID = assignedTaskDetails.taskID;
          taskQueryResultDetails.subject = assignedTaskDetails.subject;
          taskQueryResultDetails.priority = assignedTaskDetails.priority;
          taskQueryResultDetails.assignedDateTime =
            assignedTaskDetails.assignedDateTime;
          taskQueryResultDetails.deadlineDateTime =
            assignedTaskDetails.deadlineDateTime;
          taskQueryResultDetails.versionNo = assignedTaskDetails.versionNo;

          assignedTaskForUserList.taskQueryResultDetailsList.taskDetailsList
            .addRef(taskQueryResultDetails);

        }

      } else {

        assignedTaskForwardDetails.taskQueryCriteria.isAvailableTaskSearch =
          true;
        assignedTaskForwardDetails.taskQueryCriteria.assigneeType =
          ASSIGNEETYPE.USER;
        assignedTaskForwardDetails.taskQueryCriteria.assignedToID =
          assignedTaskForwardDetails.taskFwdDtls.userName;
        assignedTaskForwardDetails.taskQueryCriteria.relatedName =
          assignedTaskForwardDetails.taskFwdDtls.userName;

        assignedTaskForUserList.taskQueryResultDetailsList =
          maintainSupervisorObj
            .taskSearch(assignedTaskForwardDetails.taskQueryCriteria);
      }

      // BEGIN, CR00379440, IBM
      // final AssignedTaskForUserList filteredAssignedTaskForUserList =
      // new AssignedTaskForUserList();
      final BDMAssignedTaskForUserList bdmFilteredAssignedTaskForUserList =
        new BDMAssignedTaskForUserList();

      for (final TaskQueryResultDetails taskQueryResultDetails : assignedTaskForUserList.taskQueryResultDetailsList.taskDetailsList
        .items()) {
        isTaskBatchQueued = false;

        for (final BatchQueuedTaskID batchQueuedTaskID : batchQueuedTaskIDList.dtls
          .items()) {
          if (taskQueryResultDetails.taskID == batchQueuedTaskID.taskID) {
            isTaskBatchQueued = true;
            break;
          }
        }

        if (!isTaskBatchQueued) {
          final BDMTaskQueryResultDetails bdmTask =
            new BDMTaskQueryResultDetails();

          bdmTask.assign(taskQueryResultDetails);
          bdmTask.escalationLevelDesc = BDMTaskUtil
            .getEscalationLevelsByTaskID(taskQueryResultDetails.taskID);
          bdmTask.caseUrgentFlagStr = BDMTaskUtil
            .getCaseUrgentFlagsByTaskID(taskQueryResultDetails.taskID);

          bdmFilteredAssignedTaskForUserList.taskQueryResultDetailsList.taskDetailsList
            .addRef(bdmTask);
        }
      }

      // assignedTaskForUserList = filteredAssignedTaskForUserList;
      bdmAssignedTaskForUserList = bdmFilteredAssignedTaskForUserList;
      // END, CR00379440

    } else if (ClientActionConst.kForwardTasks
      .equals(assignedTaskForwardDetails.actionIDProperty)) {

      final SupervisorTaskFwdDetails supervisorTaskFwdDetails =
        new SupervisorTaskFwdDetails();

      supervisorTaskFwdDetails.details =
        assignedTaskForwardDetails.taskFwdDtls;

      // BEGIN, CR00379440, IBM
      forwardTasksNotReservedByUser(supervisorTaskFwdDetails);

      final StringList assignedTaskIDList = StringUtil.tabText2StringList(
        supervisorTaskFwdDetails.details.assignedTaskIDList);

      final TaskCountDetails countDetails = new TaskCountDetails();

      countDetails.taskCount = assignedTaskIDList.size();

      bdmAssignedTaskForUserList.taskQueryResultDetailsList.informationalMsgDetailsList =
        displayTaskForwardInformation(countDetails);
      // END, CR00379440
    }

    return bdmAssignedTaskForUserList;
  }

}
