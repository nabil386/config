package curam.ca.gc.bdm.sl.organization.impl;

import curam.ca.gc.bdm.sl.bdminbox.struct.BDMDeferredTaskDetails;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMReservedByStatusTaskDetails;
import curam.ca.gc.bdm.sl.organization.struct.BDMListDeferredTasksReservedByOrgUnitUserDetails;
import curam.ca.gc.bdm.sl.organization.struct.BDMListOpenTasksReservedByOrgUnitUserDetails;
import curam.ca.gc.bdm.util.impl.BDMTaskUtil;
import curam.codetable.TASKSTATUS;
import curam.core.sl.entity.fact.OrganisationUnitFactory;
import curam.core.sl.entity.struct.OrganisationUnitKey;
import curam.core.sl.fact.InboxFactory;
import curam.core.sl.impl.TaskSortByPriority;
import curam.core.sl.intf.Inbox;
import curam.core.sl.struct.DeferredTaskDetails;
import curam.core.sl.struct.ListTaskKey;
import curam.core.sl.struct.ReservedByStatusTaskDetails;
import curam.core.sl.struct.UserNameAndStatusKey;
import curam.supervisor.sl.struct.ListDeferredTasksReservedByOrgUnitUserDetails;
import curam.supervisor.sl.struct.ListOpenTasksReservedByOrgUnitUserDetails;
import curam.supervisor.sl.struct.UserNameOrgUnitIDKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;

public class BDMMaintainSupervisorOrgUnits
  extends curam.ca.gc.bdm.sl.organization.base.BDMMaintainSupervisorOrgUnits {

  public BDMMaintainSupervisorOrgUnits() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  // ___________________________________________________________________________
  /**
   * This method gets a list of the tasks that have been reserved, and then
   * deferred by members of the organization unit.
   *
   * @param key -
   * UserNameOrgUnitIDKey
   * @return the BDMListDeferredTasksReservedByOrgUnitUserDetails object.
   * @throws InformationalException
   * @throws AppException
   */
  @Override
  public BDMListDeferredTasksReservedByOrgUnitUserDetails
    listDeferredTasksReservedByOrgUnitUser(final UserNameOrgUnitIDKey key)
      throws AppException, InformationalException {

    final Inbox inbox = InboxFactory.newInstance();

    final curam.core.sl.entity.intf.OrganisationUnit organisationUnitObj =
      OrganisationUnitFactory.newInstance();

    final OrganisationUnitKey organisationUnitKey = new OrganisationUnitKey();

    organisationUnitKey.organisationUnitID = key.orgUnitID;

    final ListTaskKey listTaskKey = new ListTaskKey();

    final BDMListDeferredTasksReservedByOrgUnitUserDetails bdmListDeferredTasksReservedByOrgUnitUserDetails =
      new BDMListDeferredTasksReservedByOrgUnitUserDetails();

    final ListDeferredTasksReservedByOrgUnitUserDetails listDeferredTasksReservedByOrgUnitUserDetails =
      new ListDeferredTasksReservedByOrgUnitUserDetails();
    listTaskKey.userName = key.userName;

    listDeferredTasksReservedByOrgUnitUserDetails.details =
      inbox.listLimitedDeferredTasks(listTaskKey,
        inbox.getInboxTaskReadMultiDetails());

    for (int i =
      0; i < listDeferredTasksReservedByOrgUnitUserDetails.details.taskDetailsList
        .size(); i++) {

      final DeferredTaskDetails deferredTaskDetails =
        listDeferredTasksReservedByOrgUnitUserDetails.details.taskDetailsList
          .get(i);

      final BDMDeferredTaskDetails bdmDeferredTaskDetails =
        new BDMDeferredTaskDetails();
      bdmDeferredTaskDetails.taskID = deferredTaskDetails.taskID;
      bdmDeferredTaskDetails.priority = deferredTaskDetails.priority;
      bdmDeferredTaskDetails.restartDateTime =
        deferredTaskDetails.restartDateTime;
      bdmDeferredTaskDetails.versionNo = deferredTaskDetails.versionNo;
      bdmDeferredTaskDetails.deadlineDateTime =
        deferredTaskDetails.deadlineDateTime;

      bdmDeferredTaskDetails.subject = deferredTaskDetails.subject;
      // Get escaltion level and urgent flag
      bdmDeferredTaskDetails.caseUrgentFlagStr =
        BDMTaskUtil.getCaseUrgentFlagsByTaskID(deferredTaskDetails.taskID);
      bdmDeferredTaskDetails.escalationLevelDesc =
        BDMTaskUtil.getEscalationLevelsByTaskID(deferredTaskDetails.taskID);

      bdmListDeferredTasksReservedByOrgUnitUserDetails.details.taskDetailsList
        .addRef(bdmDeferredTaskDetails);

    }

    final TaskSortByPriority<BDMDeferredTaskDetails> prioritySort =
      new TaskSortByPriority<BDMDeferredTaskDetails>();

    prioritySort.sortIfEnabled(
      bdmListDeferredTasksReservedByOrgUnitUserDetails.details.taskDetailsList);

    bdmListDeferredTasksReservedByOrgUnitUserDetails.orgUnitName =
      organisationUnitObj.readOrgUnitName(organisationUnitKey).name;

    return bdmListDeferredTasksReservedByOrgUnitUserDetails;
  }

  // ___________________________________________________________________________
  /**
   * This method gets a list of the tasks reserved by members of the
   * organization unit, that are open.
   *
   * @param key -
   * UserNameOrgUnitIDKey
   * @return the BDMListOpenTasksReservedByOrgUnitUserDetails object.
   * @throws InformationalException
   * @throws AppException
   */
  @Override
  public BDMListOpenTasksReservedByOrgUnitUserDetails
    listOpenTasksReservedByOrgUnitUser(final UserNameOrgUnitIDKey key)
      throws AppException, InformationalException {

    final Inbox inbox = InboxFactory.newInstance();

    final curam.core.sl.entity.intf.OrganisationUnit organisationUnitObj =
      OrganisationUnitFactory.newInstance();

    final UserNameAndStatusKey userNameAndStatusKey =
      new UserNameAndStatusKey();

    final ListOpenTasksReservedByOrgUnitUserDetails listOpenTasksReservedByOrgUnitUserDetails =
      new ListOpenTasksReservedByOrgUnitUserDetails();
    final BDMListOpenTasksReservedByOrgUnitUserDetails bdmlistOpenTasksReservedByOrgUnitUserDetails =
      new BDMListOpenTasksReservedByOrgUnitUserDetails();

    final OrganisationUnitKey organisationUnitKey = new OrganisationUnitKey();
    organisationUnitKey.organisationUnitID = key.orgUnitID;

    userNameAndStatusKey.userName = key.userName;
    userNameAndStatusKey.statusCode = TASKSTATUS.NOTSTARTED;

    listOpenTasksReservedByOrgUnitUserDetails.taskDetailsList =
      inbox.listLimitedReservedTasksByStatus(userNameAndStatusKey,
        inbox.getInboxTaskReadMultiDetails());

    for (int i =
      0; i < listOpenTasksReservedByOrgUnitUserDetails.taskDetailsList.taskDetailsList
        .size(); i++) {

      final ReservedByStatusTaskDetails reservedTaskDetails =
        listOpenTasksReservedByOrgUnitUserDetails.taskDetailsList.taskDetailsList
          .get(i);

      final BDMReservedByStatusTaskDetails bdmReservedByStatusTask =
        new BDMReservedByStatusTaskDetails();
      bdmReservedByStatusTask.taskID = reservedTaskDetails.taskID;
      bdmReservedByStatusTask.priority = reservedTaskDetails.priority;
      bdmReservedByStatusTask.assignedDateTime =
        reservedTaskDetails.assignedDateTime;
      bdmReservedByStatusTask.dueDateTime = reservedTaskDetails.dueDateTime;

      bdmReservedByStatusTask.versionNo = reservedTaskDetails.versionNo;

      bdmReservedByStatusTask.subject = reservedTaskDetails.subject;
      // Get escaltion level and urgent flag
      bdmReservedByStatusTask.caseUrgentFlagStr =
        BDMTaskUtil.getCaseUrgentFlagsByTaskID(reservedTaskDetails.taskID);
      bdmReservedByStatusTask.escalationLevelDesc =
        BDMTaskUtil.getEscalationLevelsByTaskID(reservedTaskDetails.taskID);

      bdmlistOpenTasksReservedByOrgUnitUserDetails.taskDetailsList.taskDetailsList
        .addRef(bdmReservedByStatusTask);

    }

    final TaskSortByPriority<BDMReservedByStatusTaskDetails> prioritySort =
      new TaskSortByPriority<BDMReservedByStatusTaskDetails>();

    prioritySort.sortIfEnabled(
      bdmlistOpenTasksReservedByOrgUnitUserDetails.taskDetailsList.taskDetailsList);

    bdmlistOpenTasksReservedByOrgUnitUserDetails.orgUnitName =
      organisationUnitObj.readOrgUnitName(organisationUnitKey).name;

    return bdmlistOpenTasksReservedByOrgUnitUserDetails;

  }

}
