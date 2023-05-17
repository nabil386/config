/**
 *
 */
package curam.ca.gc.bdm.sl.supervisor.impl;

import curam.ca.gc.bdm.sl.supervisor.struct.BDMOrgUnitReservedTasksByUserDueOnDateDetails;
import curam.ca.gc.bdm.sl.supervisor.struct.BDMOrgUnitReservedTasksByUserDueOnDateDetailsList;
import curam.ca.gc.bdm.sl.supervisor.struct.BDMOrgUnitTasksDetails;
import curam.ca.gc.bdm.sl.supervisor.struct.BDMOrgUnitTasksDetailsList;
import curam.ca.gc.bdm.sl.supervisor.struct.BDMOrgUnitTasksDueByWeekDetails;
import curam.ca.gc.bdm.sl.supervisor.struct.BDMOrgUnitTasksDueByWeekDetailsList;
import curam.ca.gc.bdm.sl.supervisor.struct.BDMOrgUnitTasksDueOnDateDetails;
import curam.ca.gc.bdm.sl.supervisor.struct.BDMOrgUnitTasksDueOnDateDetailsList;
import curam.ca.gc.bdm.util.impl.BDMTaskUtil;
import curam.codetable.RECORDSTATUS;
import curam.codetable.TASKSTATUS;
import curam.core.sl.entity.fact.OrganisationUnitFactory;
import curam.core.sl.entity.intf.OrganisationUnit;
import curam.core.sl.entity.struct.OrgUnitReservedTasksByUserDueInTheNextTimePeriodDetails;
import curam.core.sl.entity.struct.OrgUnitReservedTasksByUserDueInTheNextTimePeriodDetailsList;
import curam.core.sl.entity.struct.OrgUnitReservedTasksByUserDueInTheNextTimePeriodKey;
import curam.core.sl.entity.struct.OrgUnitTasksByDueDateDetails;
import curam.core.sl.entity.struct.OrgUnitTasksByDueDateDetailsList;
import curam.core.sl.entity.struct.OrgUnitTasksDueOnWeekDetails;
import curam.core.sl.entity.struct.OrgUnitTasksDueOnWeekDetailsList;
import curam.core.sl.entity.struct.OrgUnitTasksSearchByReservationStatusKey;
import curam.core.sl.entity.struct.OrgUnitTasksSummaryDetails;
import curam.core.sl.entity.struct.OrgUnitTasksSummaryDetailsList;
import curam.core.sl.entity.struct.OrganisationUnitTasksByUserDueOnDateKey;
import curam.core.sl.supervisor.struct.OrgUnitReservedTasksByUserDueOnDateKey;
import curam.core.sl.supervisor.struct.OrgUnitTasksDueOnDateKey;
import curam.core.sl.supervisor.struct.OrgUnitTasksKey;
import curam.util.codetable.TASKRESERVEDSEARCHSTATUS;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.Date;
import curam.util.workflow.impl.LocalizableStringResolver;
import curam.util.workflow.struct.TaskWDOSnapshotDetails;

/**
 * @author jalpa.patel
 *
 */
public class BDMOrganizationUnitWorkspace
  extends curam.ca.gc.bdm.sl.supervisor.base.BDMOrganizationUnitWorkspace {

  public BDMOrganizationUnitWorkspace() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  // _________________________________________________________________________
  /**
   * This method lists the open tasks reserved by a member of the
   * organization unit, due on a particular date.
   *
   * @param key -OrgUnitReservedTasksByUserDueOnDateKey
   * @return BDMOrgUnitReservedTasksByUserDueOnDateDetailsList
   * @throws AppException,InformationalException
   */
  @Override
  public BDMOrgUnitReservedTasksByUserDueOnDateDetailsList
    getOpenOrgUnitReservedTasksByUserDueOnDate(
      final OrgUnitReservedTasksByUserDueOnDateKey key)
      throws AppException, InformationalException {

    // Create Objects
    final BDMOrgUnitReservedTasksByUserDueOnDateDetailsList result =
      new BDMOrgUnitReservedTasksByUserDueOnDateDetailsList();
    final OrganisationUnit organisationUnitObj =
      OrganisationUnitFactory.newInstance();
    final OrgUnitReservedTasksByUserDueInTheNextTimePeriodKey orgUnitReservedTasksByUserDueInTheNextTimePeriodKey =
      new OrgUnitReservedTasksByUserDueInTheNextTimePeriodKey();
    new OrgUnitReservedTasksByUserDueInTheNextTimePeriodDetailsList();
    orgUnitReservedTasksByUserDueInTheNextTimePeriodKey.reservedBy =
      key.userName;
    orgUnitReservedTasksByUserDueInTheNextTimePeriodKey.fromDeadlineDateTime =
      key.deadlineDate.getDateTime();
    orgUnitReservedTasksByUserDueInTheNextTimePeriodKey.toDeadlineDateTime =
      key.deadlineDate.addDays(1).getDateTime();
    orgUnitReservedTasksByUserDueInTheNextTimePeriodKey.status =
      TASKSTATUS.NOTSTARTED;

    // search Org unit reserved task
    final OrgUnitReservedTasksByUserDueInTheNextTimePeriodDetailsList orgUnitReservedTasksByUserDueInTheNextTimePeriodDetailsList =
      organisationUnitObj
        .searchOrgUnitReservedTasksByUserDueInTheNextTimePeriodOrderByReservedDateTime(
          orgUnitReservedTasksByUserDueInTheNextTimePeriodKey);
    final int numberOfRecords =
      orgUnitReservedTasksByUserDueInTheNextTimePeriodDetailsList.dtls.size();
    result.dtls.ensureCapacity(numberOfRecords);

    for (int i = 0; i < numberOfRecords; ++i) {
      final TaskWDOSnapshotDetails taskWDOSnapshotDetails =
        new TaskWDOSnapshotDetails();
      final OrgUnitReservedTasksByUserDueInTheNextTimePeriodDetails orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetailsList.dtls
          .get(i);

      final long taskID =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.taskID;
      final BDMOrgUnitReservedTasksByUserDueOnDateDetails orgUnitReservedTasksByUserDueOnDateDetails =
        new BDMOrgUnitReservedTasksByUserDueOnDateDetails();
      orgUnitReservedTasksByUserDueOnDateDetails.dtls.taskID =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.taskID;
      orgUnitReservedTasksByUserDueOnDateDetails.dtls.taskPriority =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.priority;
      orgUnitReservedTasksByUserDueOnDateDetails.dtls.taskReservedDateTime =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.reservedDateTime;
      orgUnitReservedTasksByUserDueOnDateDetails.dtls.taskDeadlineDateTime =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.deadlineDateTime;
      orgUnitReservedTasksByUserDueOnDateDetails.dtls.taskRestartDateTime =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.restartDateTime;
      orgUnitReservedTasksByUserDueOnDateDetails.dtls.versionNo =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.versionNo;
      taskWDOSnapshotDetails.taskID =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.taskID;
      taskWDOSnapshotDetails.wdoSnapshot =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.wdoSnapshot;
      orgUnitReservedTasksByUserDueOnDateDetails.dtls.taskSubject =
        LocalizableStringResolver.getTaskStringResolver()
          .getSubjectForTask(taskWDOSnapshotDetails);

      // Escalation Level and Urgent flag
      orgUnitReservedTasksByUserDueOnDateDetails.escalationLevelDesc =
        BDMTaskUtil.getEscalationLevelsByTaskID(taskID);
      orgUnitReservedTasksByUserDueOnDateDetails.caseUrgentFlagStr =
        BDMTaskUtil.getCaseUrgentFlagsByTaskID(taskID);

      result.dtls.addRef(orgUnitReservedTasksByUserDueOnDateDetails);
    }
    return result;
  }

  // _________________________________________________________________________
  /**
   * This method lists the deferred tasks reserved by a member of the
   * organization unit, due on a particular date.
   *
   * @param key -OrgUnitReservedTasksByUserDueOnDateKey
   * @return BDMOrgUnitReservedTasksByUserDueOnDateDetailsList
   * @throws AppException,InformationalException
   */
  @Override
  public BDMOrgUnitReservedTasksByUserDueOnDateDetailsList
    getDeferredOrgUnitReservedTasksByUserDueOnDate(
      final OrgUnitReservedTasksByUserDueOnDateKey key)
      throws AppException, InformationalException {

    final BDMOrgUnitReservedTasksByUserDueOnDateDetailsList result =
      new BDMOrgUnitReservedTasksByUserDueOnDateDetailsList();
    final OrganisationUnit organisationUnitObj =
      OrganisationUnitFactory.newInstance();
    final OrgUnitReservedTasksByUserDueInTheNextTimePeriodKey orgUnitReservedTasksByUserDueInTheNextTimePeriodKey =
      new OrgUnitReservedTasksByUserDueInTheNextTimePeriodKey();

    orgUnitReservedTasksByUserDueInTheNextTimePeriodKey.reservedBy =
      key.userName;
    orgUnitReservedTasksByUserDueInTheNextTimePeriodKey.fromDeadlineDateTime =
      key.deadlineDate.getDateTime();
    orgUnitReservedTasksByUserDueInTheNextTimePeriodKey.toDeadlineDateTime =
      key.deadlineDate.addDays(1).getDateTime();
    orgUnitReservedTasksByUserDueInTheNextTimePeriodKey.status =
      TASKSTATUS.DEFERRED;
    final OrgUnitReservedTasksByUserDueInTheNextTimePeriodDetailsList orgUnitReservedTasksByUserDueInTheNextTimePeriodDetailsList =
      organisationUnitObj
        .searchOrgUnitReservedTasksByUserDueInTheNextTimePeriodOrderByRestartDateTime(
          orgUnitReservedTasksByUserDueInTheNextTimePeriodKey);
    final int numberOfRecords =
      orgUnitReservedTasksByUserDueInTheNextTimePeriodDetailsList.dtls.size();
    result.dtls.ensureCapacity(numberOfRecords);

    for (int i = 0; i < numberOfRecords; ++i) {
      final TaskWDOSnapshotDetails taskWDOSnapshotDetails =
        new TaskWDOSnapshotDetails();
      final OrgUnitReservedTasksByUserDueInTheNextTimePeriodDetails orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetailsList.dtls
          .get(i);
      final BDMOrgUnitReservedTasksByUserDueOnDateDetails orgUnitReservedTasksByUserDueOnDateDetails =
        new BDMOrgUnitReservedTasksByUserDueOnDateDetails();
      orgUnitReservedTasksByUserDueOnDateDetails.dtls.taskID =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.taskID;
      orgUnitReservedTasksByUserDueOnDateDetails.dtls.taskPriority =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.priority;
      orgUnitReservedTasksByUserDueOnDateDetails.dtls.taskReservedDateTime =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.reservedDateTime;
      orgUnitReservedTasksByUserDueOnDateDetails.dtls.taskDeadlineDateTime =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.deadlineDateTime;
      orgUnitReservedTasksByUserDueOnDateDetails.dtls.taskRestartDateTime =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.restartDateTime;
      orgUnitReservedTasksByUserDueOnDateDetails.dtls.versionNo =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.versionNo;
      taskWDOSnapshotDetails.taskID =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.taskID;
      taskWDOSnapshotDetails.wdoSnapshot =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.wdoSnapshot;
      orgUnitReservedTasksByUserDueOnDateDetails.dtls.taskSubject =
        LocalizableStringResolver.getTaskStringResolver()
          .getSubjectForTask(taskWDOSnapshotDetails);

      // Escalation Level and Urgent flag
      orgUnitReservedTasksByUserDueOnDateDetails.escalationLevelDesc =
        BDMTaskUtil.getEscalationLevelsByTaskID(
          orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.taskID);
      orgUnitReservedTasksByUserDueOnDateDetails.caseUrgentFlagStr =
        BDMTaskUtil.getCaseUrgentFlagsByTaskID(
          orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.taskID);

      result.dtls.addRef(orgUnitReservedTasksByUserDueOnDateDetails);
    }

    return result;
  }

  // _________________________________________________________________________
  /**
   * This method fetchs the Open Reserved Tasks By Week.
   *
   * @param key -
   * OrgUnitReservedTasksByUserDueOnDateKey
   * @return BDMOrgUnitReservedTasksByUserDueOnDateDetailsList
   * @throws AppException
   * @throws InformationalException
   */

  @Override
  public BDMOrgUnitReservedTasksByUserDueOnDateDetailsList
    getOpenOrgUnitReservedTasksByUserDueByWeek(
      final OrgUnitReservedTasksByUserDueOnDateKey key)
      throws AppException, InformationalException {

    final BDMOrgUnitReservedTasksByUserDueOnDateDetailsList result =
      new BDMOrgUnitReservedTasksByUserDueOnDateDetailsList();
    final OrganisationUnit organisationUnitObj =
      OrganisationUnitFactory.newInstance();
    final OrgUnitReservedTasksByUserDueInTheNextTimePeriodKey orgUnitReservedTasksByUserDueInTheNextTimePeriodKey =
      new OrgUnitReservedTasksByUserDueInTheNextTimePeriodKey();

    orgUnitReservedTasksByUserDueInTheNextTimePeriodKey.reservedBy =
      key.userName;
    orgUnitReservedTasksByUserDueInTheNextTimePeriodKey.fromDeadlineDateTime =
      key.deadlineDate.getDateTime();
    orgUnitReservedTasksByUserDueInTheNextTimePeriodKey.toDeadlineDateTime =
      key.deadlineDate.addDays(7).getDateTime();
    orgUnitReservedTasksByUserDueInTheNextTimePeriodKey.status =
      TASKSTATUS.NOTSTARTED;
    final OrgUnitReservedTasksByUserDueInTheNextTimePeriodDetailsList orgUnitReservedTasksByUserDueInTheNextTimePeriodDetailsList =
      organisationUnitObj
        .searchOrgUnitReservedTasksByUserDueInTheNextTimePeriodOrderByReservedDateTime(
          orgUnitReservedTasksByUserDueInTheNextTimePeriodKey);
    final int numberOfRecords =
      orgUnitReservedTasksByUserDueInTheNextTimePeriodDetailsList.dtls.size();
    result.dtls.ensureCapacity(numberOfRecords);

    for (int i = 0; i < numberOfRecords; ++i) {
      final TaskWDOSnapshotDetails taskWDOSnapshotDetails =
        new TaskWDOSnapshotDetails();
      final OrgUnitReservedTasksByUserDueInTheNextTimePeriodDetails orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetailsList.dtls
          .get(i);
      final BDMOrgUnitReservedTasksByUserDueOnDateDetails orgUnitReservedTasksByUserDueOnDateDetails =
        new BDMOrgUnitReservedTasksByUserDueOnDateDetails();
      orgUnitReservedTasksByUserDueOnDateDetails.dtls.taskID =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.taskID;
      orgUnitReservedTasksByUserDueOnDateDetails.dtls.taskPriority =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.priority;
      orgUnitReservedTasksByUserDueOnDateDetails.dtls.taskReservedDateTime =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.reservedDateTime;
      orgUnitReservedTasksByUserDueOnDateDetails.dtls.taskDeadlineDateTime =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.deadlineDateTime;
      orgUnitReservedTasksByUserDueOnDateDetails.dtls.taskRestartDateTime =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.restartDateTime;
      orgUnitReservedTasksByUserDueOnDateDetails.dtls.versionNo =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.versionNo;
      taskWDOSnapshotDetails.taskID =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.taskID;
      taskWDOSnapshotDetails.wdoSnapshot =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.wdoSnapshot;
      orgUnitReservedTasksByUserDueOnDateDetails.dtls.taskSubject =
        LocalizableStringResolver.getTaskStringResolver()
          .getSubjectForTask(taskWDOSnapshotDetails);

      // Escalation Level and Urgent flag
      orgUnitReservedTasksByUserDueOnDateDetails.escalationLevelDesc =
        BDMTaskUtil.getEscalationLevelsByTaskID(
          orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.taskID);
      orgUnitReservedTasksByUserDueOnDateDetails.caseUrgentFlagStr =
        BDMTaskUtil.getCaseUrgentFlagsByTaskID(
          orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.taskID);
      result.dtls.addRef(orgUnitReservedTasksByUserDueOnDateDetails);
    }

    return result;
  }

  // _________________________________________________________________________
  /**
   * This method fetch the Deferred Reserved Tasks By
   * Week.
   *
   * @param key -
   * OrgUnitReservedTasksByUserDueOnDateKey
   * @return BDMOrgUnitReservedTasksByUserDueOnDateDetailsList
   * @throws AppException
   * @throws InformationalException
   */

  @Override
  public BDMOrgUnitReservedTasksByUserDueOnDateDetailsList
    getDeferredOrgUnitReservedTasksByUserDueByWeek(
      final OrgUnitReservedTasksByUserDueOnDateKey key)
      throws AppException, InformationalException {

    final BDMOrgUnitReservedTasksByUserDueOnDateDetailsList result =
      new BDMOrgUnitReservedTasksByUserDueOnDateDetailsList();
    final OrganisationUnit organisationUnitObj =
      OrganisationUnitFactory.newInstance();
    final OrgUnitReservedTasksByUserDueInTheNextTimePeriodKey orgUnitReservedTasksByUserDueInTheNextTimePeriodKey =
      new OrgUnitReservedTasksByUserDueInTheNextTimePeriodKey();
    new OrgUnitReservedTasksByUserDueInTheNextTimePeriodDetailsList();
    orgUnitReservedTasksByUserDueInTheNextTimePeriodKey.reservedBy =
      key.userName;
    orgUnitReservedTasksByUserDueInTheNextTimePeriodKey.fromDeadlineDateTime =
      key.deadlineDate.getDateTime();
    orgUnitReservedTasksByUserDueInTheNextTimePeriodKey.toDeadlineDateTime =
      key.deadlineDate.addDays(7).getDateTime();
    orgUnitReservedTasksByUserDueInTheNextTimePeriodKey.status =
      TASKSTATUS.DEFERRED;
    final OrgUnitReservedTasksByUserDueInTheNextTimePeriodDetailsList orgUnitReservedTasksByUserDueInTheNextTimePeriodDetailsList =
      organisationUnitObj
        .searchOrgUnitReservedTasksByUserDueInTheNextTimePeriodOrderByRestartDateTime(
          orgUnitReservedTasksByUserDueInTheNextTimePeriodKey);
    final int numberOfRecords =
      orgUnitReservedTasksByUserDueInTheNextTimePeriodDetailsList.dtls.size();
    result.dtls.ensureCapacity(numberOfRecords);

    for (int i = 0; i < numberOfRecords; ++i) {
      final TaskWDOSnapshotDetails taskWDOSnapshotDetails =
        new TaskWDOSnapshotDetails();
      final OrgUnitReservedTasksByUserDueInTheNextTimePeriodDetails orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetailsList.dtls
          .get(i);
      final BDMOrgUnitReservedTasksByUserDueOnDateDetails orgUnitReservedTasksByUserDueOnDateDetails =
        new BDMOrgUnitReservedTasksByUserDueOnDateDetails();
      orgUnitReservedTasksByUserDueOnDateDetails.dtls.taskID =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.taskID;
      orgUnitReservedTasksByUserDueOnDateDetails.dtls.taskPriority =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.priority;
      orgUnitReservedTasksByUserDueOnDateDetails.dtls.taskReservedDateTime =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.reservedDateTime;
      orgUnitReservedTasksByUserDueOnDateDetails.dtls.taskDeadlineDateTime =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.deadlineDateTime;
      orgUnitReservedTasksByUserDueOnDateDetails.dtls.taskRestartDateTime =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.restartDateTime;
      orgUnitReservedTasksByUserDueOnDateDetails.dtls.versionNo =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.versionNo;
      taskWDOSnapshotDetails.taskID =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.taskID;
      taskWDOSnapshotDetails.wdoSnapshot =
        orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.wdoSnapshot;
      orgUnitReservedTasksByUserDueOnDateDetails.dtls.taskSubject =
        LocalizableStringResolver.getTaskStringResolver()
          .getSubjectForTask(taskWDOSnapshotDetails);

      // Escalation Level and Urgent flag
      orgUnitReservedTasksByUserDueOnDateDetails.escalationLevelDesc =
        BDMTaskUtil.getEscalationLevelsByTaskID(
          orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.taskID);
      orgUnitReservedTasksByUserDueOnDateDetails.caseUrgentFlagStr =
        BDMTaskUtil.getCaseUrgentFlagsByTaskID(
          orgUnitReservedTasksByUserDueInTheNextTimePeriodDetails.taskID);

      result.dtls.addRef(orgUnitReservedTasksByUserDueOnDateDetails);
    }

    return result;
  }

  // _________________________________________________________________________
  /**
   * This method lists tasks of organization unit
   * for a specified week.
   *
   * @param key -
   * OrgUnitTasksDueOnDateKey
   * @return BDMOrgUnitTasksDueByWeekDetailsList
   * @throws AppException
   * @throws InformationalException
   */

  @Override
  public BDMOrgUnitTasksDueByWeekDetailsList
    getOrgUnitTasksByUserDueByWeek(final OrgUnitTasksDueOnDateKey key)
      throws AppException, InformationalException {

    final BDMOrgUnitTasksDueByWeekDetailsList result =
      new BDMOrgUnitTasksDueByWeekDetailsList();
    final OrganisationUnit organisationUnitObj =
      OrganisationUnitFactory.newInstance();
    final OrganisationUnitTasksByUserDueOnDateKey orgUnitTasksByUserDueOnDateKey =
      new OrganisationUnitTasksByUserDueOnDateKey();

    orgUnitTasksByUserDueOnDateKey.userName = key.userName;
    orgUnitTasksByUserDueOnDateKey.fromDeadlineDateTime =
      key.deadlineDate.getDateTime();
    orgUnitTasksByUserDueOnDateKey.toDeadlineDateTime =
      key.deadlineDate.addDays(7).getDateTime();
    if (key.taskReservationStatus.equals(TASKRESERVEDSEARCHSTATUS.RESERVED)) {
      orgUnitTasksByUserDueOnDateKey.allReservedOrgUnitTasksInd = true;
    } else if (key.taskReservationStatus
      .equals(TASKRESERVEDSEARCHSTATUS.UNRESERVED)) {
      orgUnitTasksByUserDueOnDateKey.allAssignedOrgUnitTasksInd = true;
    } else {
      orgUnitTasksByUserDueOnDateKey.allOrgUnitTasksInd = true;
    }

    final OrgUnitTasksDueOnWeekDetailsList orgUnitTasksDueOnWeekDetailsList =
      organisationUnitObj.searchOrgUnitTasksByUserDueInTheNextWeek(
        orgUnitTasksByUserDueOnDateKey);
    final int numberOfRecords = orgUnitTasksDueOnWeekDetailsList.dtls.size();
    result.dtls.ensureCapacity(numberOfRecords);

    for (int i = 0; i < numberOfRecords; ++i) {
      final TaskWDOSnapshotDetails taskWDOSnapshotDetails =
        new TaskWDOSnapshotDetails();
      final OrgUnitTasksDueOnWeekDetails orgUnitTasksDueOnWeekDetails =
        orgUnitTasksDueOnWeekDetailsList.dtls.get(i);
      final BDMOrgUnitTasksDueByWeekDetails orgUnitTasksDueByWeekDetails =
        new BDMOrgUnitTasksDueByWeekDetails();
      orgUnitTasksDueByWeekDetails.details.taskID =
        orgUnitTasksDueOnWeekDetails.taskID;
      orgUnitTasksDueByWeekDetails.details.taskPriority =
        orgUnitTasksDueOnWeekDetails.taskPriority;
      orgUnitTasksDueByWeekDetails.details.taskReservedByUserName =
        orgUnitTasksDueOnWeekDetails.taskReservedByUserName;
      orgUnitTasksDueByWeekDetails.details.taskReservedByFullUserName =
        orgUnitTasksDueOnWeekDetails.taskReservedByFullUserName;
      orgUnitTasksDueByWeekDetails.details.taskAssignedDateTime =
        orgUnitTasksDueOnWeekDetails.taskAssignedDateTime;
      orgUnitTasksDueByWeekDetails.details.taskDeadlineDateTime =
        orgUnitTasksDueOnWeekDetails.taskDeadlineDateTime;
      orgUnitTasksDueByWeekDetails.details.versionNo =
        orgUnitTasksDueOnWeekDetails.versionNo;
      taskWDOSnapshotDetails.taskID = orgUnitTasksDueOnWeekDetails.taskID;
      taskWDOSnapshotDetails.wdoSnapshot =
        orgUnitTasksDueOnWeekDetails.taskWDOSnapshot;
      orgUnitTasksDueByWeekDetails.details.taskSubject =
        LocalizableStringResolver.getTaskStringResolver()
          .getSubjectForTask(taskWDOSnapshotDetails);

      orgUnitTasksDueByWeekDetails.caseUrgentFlagStr = BDMTaskUtil
        .getCaseUrgentFlagsByTaskID(orgUnitTasksDueOnWeekDetails.taskID);
      orgUnitTasksDueByWeekDetails.escalationLevelDesc = BDMTaskUtil
        .getEscalationLevelsByTaskID(orgUnitTasksDueOnWeekDetails.taskID);

      result.dtls.addRef(orgUnitTasksDueByWeekDetails);
    }

    return result;
  }

  // ___________________________________________________________________________
  /**
   * This method lists the tasks due on date Assigned To a user.
   *
   * @param key -
   * OrgUnitTasksDueOnDateKey,
   *
   * @return BDMOrgUnitTasksDueOnDateDetailsList
   * @throws InformationalException
   * @throws AppException
   */

  @Override
  public BDMOrgUnitTasksDueOnDateDetailsList
    getOrgUnitTasksByUserDueOnDate(final OrgUnitTasksDueOnDateKey key)
      throws AppException, InformationalException {

    final BDMOrgUnitTasksDueOnDateDetailsList result =
      new BDMOrgUnitTasksDueOnDateDetailsList();
    final OrganisationUnit organisationUnitObj =
      OrganisationUnitFactory.newInstance();
    final OrganisationUnitTasksByUserDueOnDateKey orgUnitTasksByUserDueOnDateKey =
      new OrganisationUnitTasksByUserDueOnDateKey();
    orgUnitTasksByUserDueOnDateKey.userName = key.userName;
    orgUnitTasksByUserDueOnDateKey.fromDeadlineDateTime =
      key.deadlineDate.getDateTime();
    orgUnitTasksByUserDueOnDateKey.toDeadlineDateTime =
      key.deadlineDate.addDays(1).getDateTime();
    if (key.taskReservationStatus.equals("TRSS1")) {
      orgUnitTasksByUserDueOnDateKey.allReservedOrgUnitTasksInd = true;
    } else if (key.taskReservationStatus.equals("TRSS2")) {
      orgUnitTasksByUserDueOnDateKey.allAssignedOrgUnitTasksInd = true;
    } else {
      orgUnitTasksByUserDueOnDateKey.allOrgUnitTasksInd = true;
    }

    final OrgUnitTasksByDueDateDetailsList orgUnitTasksByDueDateDetailsList =
      organisationUnitObj
        .searchOrgUnitTasksByUserDueOnDate(orgUnitTasksByUserDueOnDateKey);
    final int numberOfRecords = orgUnitTasksByDueDateDetailsList.dtls.size();
    result.dtls.ensureCapacity(numberOfRecords);

    for (int i = 0; i < numberOfRecords; ++i) {
      final TaskWDOSnapshotDetails taskWDOSnapshotDetails =
        new TaskWDOSnapshotDetails();
      final OrgUnitTasksByDueDateDetails orgUnitTasksByDueDateDetails =
        orgUnitTasksByDueDateDetailsList.dtls.get(i);
      final BDMOrgUnitTasksDueOnDateDetails bdmorgUnitTasksDueOnDateDetails =
        new BDMOrgUnitTasksDueOnDateDetails();
      bdmorgUnitTasksDueOnDateDetails.dtls.taskID =
        orgUnitTasksByDueDateDetails.taskID;
      bdmorgUnitTasksDueOnDateDetails.dtls.taskPriority =
        orgUnitTasksByDueDateDetails.taskPriority;
      bdmorgUnitTasksDueOnDateDetails.dtls.taskReservedByUserName =
        orgUnitTasksByDueDateDetails.taskReservedByUserName;
      bdmorgUnitTasksDueOnDateDetails.dtls.taskReservedByFullUserName =
        orgUnitTasksByDueDateDetails.taskReservedByFullUserName;
      bdmorgUnitTasksDueOnDateDetails.dtls.taskAssignedDateTime =
        orgUnitTasksByDueDateDetails.taskAssignedDateTime;
      bdmorgUnitTasksDueOnDateDetails.dtls.versionNo =
        orgUnitTasksByDueDateDetails.versionNo;
      taskWDOSnapshotDetails.taskID = orgUnitTasksByDueDateDetails.taskID;
      taskWDOSnapshotDetails.wdoSnapshot =
        orgUnitTasksByDueDateDetails.taskWDOSnapshot;
      bdmorgUnitTasksDueOnDateDetails.dtls.taskSubject =
        LocalizableStringResolver.getTaskStringResolver()
          .getSubjectForTask(taskWDOSnapshotDetails);

      bdmorgUnitTasksDueOnDateDetails.caseUrgentFlagStr = BDMTaskUtil
        .getCaseUrgentFlagsByTaskID(orgUnitTasksByDueDateDetails.taskID);
      bdmorgUnitTasksDueOnDateDetails.escalationLevelDesc = BDMTaskUtil
        .getEscalationLevelsByTaskID(orgUnitTasksByDueDateDetails.taskID);

      result.dtls.addRef(bdmorgUnitTasksDueOnDateDetails);
    }

    return result;
  }

  // _________________________________________________________________________
  /**
   * This method list the tasks assigned to members
   * of the organization unit, that have not yet been reserved.
   *
   * @param key -
   * OrgUnitTasksKey
   * @return BDMOrgUnitTasksDetailsList
   * @throws AppException
   * @throws InformationalException
   */

  @Override
  public BDMOrgUnitTasksDetailsList getOrgUnitTasks(final OrgUnitTasksKey key)
    throws AppException, InformationalException {

    final BDMOrgUnitTasksDetailsList result =
      new BDMOrgUnitTasksDetailsList();
    final OrganisationUnit organisationUnitObj =
      OrganisationUnitFactory.newInstance();
    final OrgUnitTasksSearchByReservationStatusKey orgUnitTasksSearchByReservationStatusKey =
      new OrgUnitTasksSearchByReservationStatusKey();

    orgUnitTasksSearchByReservationStatusKey.organizationStructureID =
      key.organizationStructureID;
    orgUnitTasksSearchByReservationStatusKey.organizationUnitID =
      key.organizationUnitID;
    if (key.taskReservationStatus.equals("TRSS1")) {
      orgUnitTasksSearchByReservationStatusKey.allReservedOrgUnitTasksInd =
        true;
    } else if (key.taskReservationStatus.equals("TRSS2")) {
      orgUnitTasksSearchByReservationStatusKey.allAssignedOrgUnitTasksInd =
        true;
    } else {
      orgUnitTasksSearchByReservationStatusKey.allOrgUnitTasksInd = true;
    }

    orgUnitTasksSearchByReservationStatusKey.currentDate =
      Date.getCurrentDate();
    orgUnitTasksSearchByReservationStatusKey.orgUnitPositionLinkRecordStatus =
      RECORDSTATUS.NORMAL;
    orgUnitTasksSearchByReservationStatusKey.positionHolderLinkRecordStatus =
      RECORDSTATUS.NORMAL;
    orgUnitTasksSearchByReservationStatusKey.positionRecordStatus =
      RECORDSTATUS.NORMAL;
    orgUnitTasksSearchByReservationStatusKey.userRecordStatus =
      RECORDSTATUS.NORMAL;
    final OrgUnitTasksSummaryDetailsList orgUnitTasksSummaryDetailsList =
      organisationUnitObj.searchOrgUnitTasksByReservationStatus(
        orgUnitTasksSearchByReservationStatusKey);
    final int numberOfTaskDetails =
      orgUnitTasksSummaryDetailsList.dtls.size();
    result.dtls.ensureCapacity(numberOfTaskDetails);

    for (int i = 0; i < numberOfTaskDetails; ++i) {
      final TaskWDOSnapshotDetails taskWDOSnapshotDetails =
        new TaskWDOSnapshotDetails();
      final OrgUnitTasksSummaryDetails orgUnitTasksSummaryDetails =
        orgUnitTasksSummaryDetailsList.dtls.get(i);
      final BDMOrgUnitTasksDetails orgUnitTasksDetails =
        new BDMOrgUnitTasksDetails();
      orgUnitTasksDetails.dtls.taskID = orgUnitTasksSummaryDetails.taskID;
      orgUnitTasksDetails.dtls.taskPriority =
        orgUnitTasksSummaryDetails.taskPriority;
      orgUnitTasksDetails.dtls.taskAssignedDateTime =
        orgUnitTasksSummaryDetails.taskAssignedDateTime;
      orgUnitTasksDetails.dtls.taskDeadlineDateTime =
        orgUnitTasksSummaryDetails.taskDeadlineDateTime;
      orgUnitTasksDetails.dtls.versionNo =
        orgUnitTasksSummaryDetails.versionNo;
      taskWDOSnapshotDetails.taskID = orgUnitTasksSummaryDetails.taskID;
      taskWDOSnapshotDetails.wdoSnapshot =
        orgUnitTasksSummaryDetails.taskWDOSnapshot;
      orgUnitTasksDetails.dtls.taskSubject = LocalizableStringResolver
        .getTaskStringResolver().getSubjectForTask(taskWDOSnapshotDetails);

      // Escalation Level And Case urgent flag
      orgUnitTasksDetails.caseUrgentFlagStr = BDMTaskUtil
        .getCaseUrgentFlagsByTaskID(orgUnitTasksSummaryDetails.taskID);
      orgUnitTasksDetails.escalationLevelDesc = BDMTaskUtil
        .getEscalationLevelsByTaskID(orgUnitTasksSummaryDetails.taskID);

      result.dtls.addRef(orgUnitTasksDetails);
    }

    return result;
  }

}
