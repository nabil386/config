package curam.ca.gc.bdm.sl.supervisor.impl;

import curam.ca.gc.bdm.sl.supervisor.struct.BDMUserTasksDueByWeekDetails;
import curam.ca.gc.bdm.sl.supervisor.struct.BDMUserTasksDueByWeekDetailsList;
import curam.ca.gc.bdm.sl.supervisor.struct.BDMUserTasksDueOnDateDetails;
import curam.ca.gc.bdm.sl.supervisor.struct.BDMUserTasksDueOnDateDetailsList;
import curam.ca.gc.bdm.util.impl.BDMTaskUtil;
import curam.codetable.TARGETITEMTYPE;
import curam.core.fact.UsersFactory;
import curam.core.intf.Users;
import curam.core.sl.entity.struct.TasksByDueDateForUserDetails;
import curam.core.sl.entity.struct.TasksByDueDateForUserDetailsList;
import curam.core.sl.entity.struct.TasksByDueDateKey;
import curam.core.sl.fact.UserAccessFactory;
import curam.core.sl.supervisor.struct.UserTasksDueOnDateKey;
import curam.core.struct.UsersKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import curam.util.workflow.impl.LocalizableStringResolver;
import curam.util.workflow.struct.TaskKey;
import curam.util.workflow.struct.TaskWDOSnapshotDetails;

public class BDMUserWorkspace
  extends curam.ca.gc.bdm.sl.supervisor.base.BDMUserWorkspace {

  @Override
  public BDMUserTasksDueByWeekDetailsList
    getUserTasksDueByWeek(final UserTasksDueOnDateKey key)
      throws AppException, InformationalException {

    final BDMUserTasksDueByWeekDetailsList userTasksDueByWeekDetailsList =
      new BDMUserTasksDueByWeekDetailsList();
    final TasksByDueDateKey tasksByDueDateKey = new TasksByDueDateKey();

    tasksByDueDateKey.relatedName = key.userName;
    tasksByDueDateKey.fromDeadlineDateTime = key.deadlineDate.getDateTime();
    tasksByDueDateKey.toDeadlineDateTime =
      key.deadlineDate.addDays(7).getDateTime();
    if (key.taskReservationStatus.equals("TRSS1")) {
      tasksByDueDateKey.allReservedTasksInd = true;
    } else if (key.taskReservationStatus.equals("TRSS2")) {
      tasksByDueDateKey.allAssignedTasksInd = true;
    } else {
      tasksByDueDateKey.allTasksInd = true;
    }

    final TasksByDueDateKey taskKey = new TasksByDueDateKey();
    taskKey.fromDeadlineDateTime = tasksByDueDateKey.fromDeadlineDateTime;
    taskKey.toDeadlineDateTime = tasksByDueDateKey.toDeadlineDateTime;
    taskKey.relatedName = tasksByDueDateKey.relatedName;
    taskKey.assigneeType = TARGETITEMTYPE.USER;
    taskKey.allTasksInd = tasksByDueDateKey.allTasksInd;
    taskKey.allAssignedTasksInd = tasksByDueDateKey.allAssignedTasksInd;
    taskKey.allReservedTasksInd = tasksByDueDateKey.allReservedTasksInd;
    final TasksByDueDateForUserDetailsList userTasksByDueDateDetailsList =
      UserAccessFactory.newInstance()
        .searchUserTasksDueInTheNextWeek(taskKey);
    final int numberOfRecords = userTasksByDueDateDetailsList.dtls.size();
    userTasksDueByWeekDetailsList.dtls.ensureCapacity(numberOfRecords);

    for (int i = 0; i < numberOfRecords; ++i) {
      final TasksByDueDateForUserDetails userTasksDueOnWeekDetails =
        userTasksByDueDateDetailsList.dtls.get(i);
      final BDMUserTasksDueByWeekDetails bdmUuserTasksDueByWeekDetails =
        new BDMUserTasksDueByWeekDetails();
      bdmUuserTasksDueByWeekDetails.userTaskDueByWeekDetails.taskID =
        userTasksDueOnWeekDetails.taskID;
      bdmUuserTasksDueByWeekDetails.userTaskDueByWeekDetails.taskPriority =
        userTasksDueOnWeekDetails.taskPriority;
      bdmUuserTasksDueByWeekDetails.userTaskDueByWeekDetails.taskReservedByUserName =
        userTasksDueOnWeekDetails.taskReservedByUserName;
      bdmUuserTasksDueByWeekDetails.userTaskDueByWeekDetails.taskReservedByFullUserName =
        userTasksDueOnWeekDetails.taskReservedByFullUserName;
      bdmUuserTasksDueByWeekDetails.userTaskDueByWeekDetails.taskAssignedDateTime =
        userTasksDueOnWeekDetails.taskAssignedDateTime;
      bdmUuserTasksDueByWeekDetails.userTaskDueByWeekDetails.taskDeadlineDateTime =
        userTasksDueOnWeekDetails.taskDeadlineDateTime;
      bdmUuserTasksDueByWeekDetails.userTaskDueByWeekDetails.versionNo =
        userTasksDueOnWeekDetails.versionNo;

      bdmUuserTasksDueByWeekDetails.caseUrgentFlagStr =
        BDMTaskUtil.getCaseUrgentFlagsByTaskID(
          bdmUuserTasksDueByWeekDetails.userTaskDueByWeekDetails.taskID);
      bdmUuserTasksDueByWeekDetails.escalationLevelDesc =
        BDMTaskUtil.getEscalationLevelsByTaskID(
          bdmUuserTasksDueByWeekDetails.userTaskDueByWeekDetails.taskID);

      final TaskWDOSnapshotDetails taskWDOSnapshotDetails =
        new TaskWDOSnapshotDetails();
      taskWDOSnapshotDetails.taskID =
        bdmUuserTasksDueByWeekDetails.userTaskDueByWeekDetails.taskID;
      taskWDOSnapshotDetails.wdoSnapshot =
        userTasksByDueDateDetailsList.dtls.item(i).wdoSnapshot;
      bdmUuserTasksDueByWeekDetails.userTaskDueByWeekDetails.taskSubject =
        LocalizableStringResolver.getTaskStringResolver()
          .getSubjectForTask(taskWDOSnapshotDetails);

      userTasksDueByWeekDetailsList.dtls
        .addRef(bdmUuserTasksDueByWeekDetails);
    }

    return userTasksDueByWeekDetailsList;

  }

  @Override
  public BDMUserTasksDueOnDateDetailsList
    getUserTasksDueOnDate(final UserTasksDueOnDateKey key)
      throws AppException, InformationalException {

    final BDMUserTasksDueOnDateDetailsList bdmUserTasksDueOnDateDetailsList =
      new BDMUserTasksDueOnDateDetailsList();
    final TasksByDueDateKey tasksByDueDateKey = new TasksByDueDateKey();

    tasksByDueDateKey.relatedName = key.userName;
    tasksByDueDateKey.fromDeadlineDateTime = key.deadlineDate
      .getTimeZoneAdjustedDateTime(TransactionInfo.getUserTimeZone());
    tasksByDueDateKey.toDeadlineDateTime = key.deadlineDate.addDays(1)
      .getTimeZoneAdjustedDateTime(TransactionInfo.getUserTimeZone());
    if (key.taskReservationStatus.equals("TRSS1")) {
      tasksByDueDateKey.allReservedTasksInd = true;
    } else if (key.taskReservationStatus.equals("TRSS2")) {
      tasksByDueDateKey.allAssignedTasksInd = true;
    } else {
      tasksByDueDateKey.allTasksInd = true;
    }

    final TasksByDueDateForUserDetailsList userTasksByDueDateDetailsList =
      UserAccessFactory.newInstance()
        .searchUserTasksByDueDate(tasksByDueDateKey);
    final int numberOfRecords = userTasksByDueDateDetailsList.dtls.size();
    bdmUserTasksDueOnDateDetailsList.dtls.ensureCapacity(numberOfRecords);
    final TaskKey taskKey = new TaskKey();
    final UsersKey usersKey = new UsersKey();
    final Users userObj = UsersFactory.newInstance();

    for (int i = 0; i < numberOfRecords; ++i) {
      final TasksByDueDateForUserDetails userTasksByDueDateDetails =
        userTasksByDueDateDetailsList.dtls.get(i);
      final BDMUserTasksDueOnDateDetails bdmUserTasksDueOnDateDetails =
        new BDMUserTasksDueOnDateDetails();
      bdmUserTasksDueOnDateDetails.userTasksDueOnDateDetails.taskID =
        userTasksByDueDateDetails.taskID;
      bdmUserTasksDueOnDateDetails.userTasksDueOnDateDetails.taskPriority =
        userTasksByDueDateDetails.taskPriority;
      bdmUserTasksDueOnDateDetails.userTasksDueOnDateDetails.taskReservedByUserName =
        userTasksByDueDateDetails.taskReservedByUserName;
      usersKey.userName = userTasksByDueDateDetails.taskReservedByUserName;
      bdmUserTasksDueOnDateDetails.userTasksDueOnDateDetails.taskReservedByFullUserName =
        userObj.getFullName(usersKey).fullname;
      bdmUserTasksDueOnDateDetails.userTasksDueOnDateDetails.taskAssignedDateTime =
        userTasksByDueDateDetails.taskAssignedDateTime;
      bdmUserTasksDueOnDateDetails.userTasksDueOnDateDetails.versionNo =
        userTasksByDueDateDetails.versionNo;

      bdmUserTasksDueOnDateDetails.caseUrgentFlagStr = BDMTaskUtil
        .getCaseUrgentFlagsByTaskID(userTasksByDueDateDetails.taskID);
      bdmUserTasksDueOnDateDetails.escalationLevelDesc = BDMTaskUtil
        .getEscalationLevelsByTaskID(userTasksByDueDateDetails.taskID);

      final TaskWDOSnapshotDetails taskWDOSnapshotDetails =
        new TaskWDOSnapshotDetails();
      taskWDOSnapshotDetails.taskID =
        userTasksByDueDateDetailsList.dtls.item(i).taskID;
      taskWDOSnapshotDetails.wdoSnapshot =
        userTasksByDueDateDetailsList.dtls.item(i).wdoSnapshot;
      taskKey.taskID = userTasksByDueDateDetails.taskID;
      bdmUserTasksDueOnDateDetails.userTasksDueOnDateDetails.taskSubject =
        LocalizableStringResolver.getTaskStringResolver()
          .getSubjectForTask(taskWDOSnapshotDetails);
      bdmUserTasksDueOnDateDetailsList.dtls
        .addRef(bdmUserTasksDueOnDateDetails);
    }

    return bdmUserTasksDueOnDateDetailsList;
  }

}
