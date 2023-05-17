/**
 *
 */
package curam.ca.gc.bdm.facade.supervisor.cases.impl;

import curam.ca.gc.bdm.facade.supervisor.cases.struct.BDMListCaseTasksDetails;
import curam.ca.gc.bdm.facade.supervisor.cases.struct.BDMListCaseTasksDueByWeekDetails;
import curam.ca.gc.bdm.facade.supervisor.cases.struct.BDMListCaseTasksDueOnDateDetails;
import curam.ca.gc.bdm.facade.supervisor.cases.struct.BDMListDeferredCaseTasksReservedByUserDetails;
import curam.ca.gc.bdm.facade.supervisor.cases.struct.BDMListOpenCaseTasksReservedByUserDetails;
import curam.ca.gc.bdm.sl.supervisor.struct.BDMCaseTasksByUserDetails;
import curam.ca.gc.bdm.sl.supervisor.struct.BDMCaseTasksDetails;
import curam.ca.gc.bdm.sl.supervisor.struct.BDMCaseTasksDueOnDateDetails;
import curam.ca.gc.bdm.util.impl.BDMTaskUtil;
import curam.core.sl.supervisor.struct.CaseTasksByUserDetails;
import curam.core.sl.supervisor.struct.CaseTasksDetails;
import curam.core.sl.supervisor.struct.CaseTasksDueOnDateDetails;
import curam.supervisor.facade.struct.CaseIDAndDeadlineDateKey;
import curam.supervisor.facade.struct.CaseIDAndUserKey;
import curam.supervisor.facade.struct.CaseIDKey;
import curam.supervisor.facade.struct.ListCaseTasksDetails;
import curam.supervisor.facade.struct.ListCaseTasksDueByWeekDetails;
import curam.supervisor.facade.struct.ListCaseTasksDueOnDateDetails;
import curam.supervisor.facade.struct.ListDeferredCaseTasksReservedByUserDetails;
import curam.supervisor.facade.struct.ListOpenCaseTasksReservedByUserDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

/**
 * Override to pull case urgent flag and escalation level.
 *
 * @author donghua.jin
 *
 */
public class BDMMaintainSupervisorCase extends
  curam.ca.gc.bdm.facade.supervisor.cases.base.BDMMaintainSupervisorCase {

  /**
   * { @InheritDoc }
   */
  @Override
  public BDMListCaseTasksDetails listBDMCaseAssignedTasks(final CaseIDKey key)
    throws AppException, InformationalException {

    final BDMListCaseTasksDetails newList = new BDMListCaseTasksDetails();

    final ListCaseTasksDetails list = super.listCaseAssignedTasks(key);

    newList.pageContextDescription = list.pageContextDescription;

    for (final CaseTasksDetails caseTaskDetails : list.taskDetails.dtls) {

      final BDMCaseTasksDetails newCaseTaskDetails =
        new BDMCaseTasksDetails();

      newCaseTaskDetails.caseTasksDetails = caseTaskDetails;
      newCaseTaskDetails.caseUrgentFlagStr =
        BDMTaskUtil.getCaseUrgentFlagsByTaskID(caseTaskDetails.taskID);
      newCaseTaskDetails.escalationLevelDesc =
        BDMTaskUtil.getEscalationLevelsByTaskID(caseTaskDetails.taskID);
      newList.taskDetails.dtls.addRef(newCaseTaskDetails);
    }

    return newList;
  }

  /**
   * { @InheritDoc }
   */
  @Override
  public BDMListCaseTasksDueOnDateDetails
    listBDMCaseTasksDueOnDate(final CaseIDAndDeadlineDateKey key)
      throws AppException, InformationalException {

    final BDMListCaseTasksDueOnDateDetails newList =
      new BDMListCaseTasksDueOnDateDetails();

    final ListCaseTasksDueOnDateDetails list =
      super.listCaseTasksDueOnDate(key);

    newList.contextDescription = list.contextDescription;

    for (final CaseTasksDueOnDateDetails caseTaskDetails : list.dtls.dtls) {

      final BDMCaseTasksDueOnDateDetails newCaseTaskDetails =
        new BDMCaseTasksDueOnDateDetails();

      newCaseTaskDetails.caseTasksDetails = caseTaskDetails;
      newCaseTaskDetails.caseUrgentFlagStr =
        BDMTaskUtil.getCaseUrgentFlagsByTaskID(caseTaskDetails.taskID);
      newCaseTaskDetails.escalationLevelDesc =
        BDMTaskUtil.getEscalationLevelsByTaskID(caseTaskDetails.taskID);
      newList.dtls.dtls.addRef(newCaseTaskDetails);
    }

    return newList;
  }

  /**
   * { @InheritDoc }
   */
  @Override
  public BDMListCaseTasksDueOnDateDetails
    listBDMCaseAssignedTasksDueOnDate(final CaseIDAndDeadlineDateKey key)
      throws AppException, InformationalException {

    final BDMListCaseTasksDueOnDateDetails newList =
      new BDMListCaseTasksDueOnDateDetails();

    final ListCaseTasksDueOnDateDetails list =
      super.listCaseAssignedTasksDueOnDate(key);

    newList.contextDescription = list.contextDescription;

    for (final CaseTasksDueOnDateDetails caseTaskDetails : list.dtls.dtls) {

      final BDMCaseTasksDueOnDateDetails newCaseTaskDetails =
        new BDMCaseTasksDueOnDateDetails();

      newCaseTaskDetails.caseTasksDetails = caseTaskDetails;
      newCaseTaskDetails.caseUrgentFlagStr =
        BDMTaskUtil.getCaseUrgentFlagsByTaskID(caseTaskDetails.taskID);
      newCaseTaskDetails.escalationLevelDesc =
        BDMTaskUtil.getEscalationLevelsByTaskID(caseTaskDetails.taskID);
      newList.dtls.dtls.addRef(newCaseTaskDetails);
    }

    return newList;
  }

  /**
   * { @InheritDoc }
   */
  @Override
  public BDMListCaseTasksDueByWeekDetails
    listBDMCaseTasksDueByWeek(final CaseIDAndDeadlineDateKey caseIDKey)
      throws AppException, InformationalException {

    final BDMListCaseTasksDueByWeekDetails newList =
      new BDMListCaseTasksDueByWeekDetails();

    final ListCaseTasksDueByWeekDetails list =
      super.listCaseTasksDueByWeek(caseIDKey);

    newList.contextDescription = list.contextDescription;

    for (final CaseTasksDetails caseTaskDetails : list.dtls.dtls) {

      final BDMCaseTasksDetails newCaseTaskDetails =
        new BDMCaseTasksDetails();

      newCaseTaskDetails.caseTasksDetails = caseTaskDetails;
      newCaseTaskDetails.caseUrgentFlagStr =
        BDMTaskUtil.getCaseUrgentFlagsByTaskID(caseTaskDetails.taskID);
      newCaseTaskDetails.escalationLevelDesc =
        BDMTaskUtil.getEscalationLevelsByTaskID(caseTaskDetails.taskID);
      newList.dtls.dtls.addRef(newCaseTaskDetails);
    }

    return newList;
  }

  /**
   * { @InheritDoc }
   */
  @Override
  public BDMListCaseTasksDueByWeekDetails listBDMCaseAssignedTasksDueByWeek(
    final CaseIDAndDeadlineDateKey caseIDKey)
    throws AppException, InformationalException {

    final BDMListCaseTasksDueByWeekDetails newList =
      new BDMListCaseTasksDueByWeekDetails();

    final ListCaseTasksDueByWeekDetails list =
      super.listCaseAssignedTasksDueByWeek(caseIDKey);

    newList.contextDescription = list.contextDescription;

    for (final CaseTasksDetails caseTaskDetails : list.dtls.dtls) {

      final BDMCaseTasksDetails newCaseTaskDetails =
        new BDMCaseTasksDetails();

      newCaseTaskDetails.caseTasksDetails = caseTaskDetails;
      newCaseTaskDetails.caseUrgentFlagStr =
        BDMTaskUtil.getCaseUrgentFlagsByTaskID(caseTaskDetails.taskID);
      newCaseTaskDetails.escalationLevelDesc =
        BDMTaskUtil.getEscalationLevelsByTaskID(caseTaskDetails.taskID);
      newList.dtls.dtls.addRef(newCaseTaskDetails);
    }

    return newList;
  }

  /**
   * { @InheritDoc }
   */
  @Override
  public BDMListCaseTasksDueByWeekDetails listBDMCaseReservedTasksDueByWeek(
    final CaseIDAndDeadlineDateKey caseIDKey)
    throws AppException, InformationalException {

    final BDMListCaseTasksDueByWeekDetails newList =
      new BDMListCaseTasksDueByWeekDetails();

    final ListCaseTasksDueByWeekDetails list =
      super.listCaseReservedTasksDueByWeek(caseIDKey);

    newList.contextDescription = list.contextDescription;

    for (final CaseTasksDetails caseTaskDetails : list.dtls.dtls) {

      final BDMCaseTasksDetails newCaseTaskDetails =
        new BDMCaseTasksDetails();

      newCaseTaskDetails.caseTasksDetails = caseTaskDetails;
      newCaseTaskDetails.caseUrgentFlagStr =
        BDMTaskUtil.getCaseUrgentFlagsByTaskID(caseTaskDetails.taskID);
      newCaseTaskDetails.escalationLevelDesc =
        BDMTaskUtil.getEscalationLevelsByTaskID(caseTaskDetails.taskID);
      newList.dtls.dtls.addRef(newCaseTaskDetails);
    }

    return newList;
  }

  /**
   * { @InheritDoc }
   */
  @Override
  public BDMListDeferredCaseTasksReservedByUserDetails
    listBDMDeferredCaseTasksReservedByUser(final CaseIDAndUserKey key)
      throws AppException, InformationalException {

    final BDMListDeferredCaseTasksReservedByUserDetails newList =
      new BDMListDeferredCaseTasksReservedByUserDetails();

    final ListDeferredCaseTasksReservedByUserDetails list =
      super.listDeferredCaseTasksReservedByUser(key);

    newList.description = list.description;

    for (final CaseTasksByUserDetails caseTaskDetails : list.defferedTaskDetails.dtls) {

      final BDMCaseTasksByUserDetails newCaseTaskDetails =
        new BDMCaseTasksByUserDetails();

      newCaseTaskDetails.caseTasksDetails = caseTaskDetails;
      newCaseTaskDetails.caseUrgentFlagStr =
        BDMTaskUtil.getCaseUrgentFlagsByTaskID(caseTaskDetails.taskID);
      newCaseTaskDetails.escalationLevelDesc =
        BDMTaskUtil.getEscalationLevelsByTaskID(caseTaskDetails.taskID);
      newList.deferredTaskDetails.dtls.addRef(newCaseTaskDetails);
    }

    return newList;
  }

  /**
   * { @InheritDoc }
   */
  @Override
  public BDMListOpenCaseTasksReservedByUserDetails
    listBDMOpenCaseTasksReservedByUser(final CaseIDAndUserKey key)
      throws AppException, InformationalException {

    final BDMListOpenCaseTasksReservedByUserDetails newList =
      new BDMListOpenCaseTasksReservedByUserDetails();

    final ListOpenCaseTasksReservedByUserDetails list =
      super.listOpenCaseTasksReservedByUser(key);

    newList.description = list.description;

    for (final CaseTasksByUserDetails caseTaskDetails : list.openTaskDetails.dtls) {

      final BDMCaseTasksByUserDetails newCaseTaskDetails =
        new BDMCaseTasksByUserDetails();

      newCaseTaskDetails.caseTasksDetails = caseTaskDetails;
      newCaseTaskDetails.caseUrgentFlagStr =
        BDMTaskUtil.getCaseUrgentFlagsByTaskID(caseTaskDetails.taskID);
      newCaseTaskDetails.escalationLevelDesc =
        BDMTaskUtil.getEscalationLevelsByTaskID(caseTaskDetails.taskID);
      newList.openTaskDetails.dtls.addRef(newCaseTaskDetails);
    }

    return newList;
  }

  /**
   * { @InheritDoc }
   */
  @Override
  public BDMListCaseTasksDueOnDateDetails
    listBDMCaseReservedTasksDueOnDate(final CaseIDAndDeadlineDateKey key)
      throws AppException, InformationalException {

    final BDMListCaseTasksDueOnDateDetails newList =
      new BDMListCaseTasksDueOnDateDetails();

    final ListCaseTasksDueOnDateDetails list =
      super.listCaseReservedTasksDueOnDate(key);

    newList.contextDescription = list.contextDescription;

    for (final CaseTasksDueOnDateDetails caseTaskDetails : list.dtls.dtls) {

      final BDMCaseTasksDueOnDateDetails newCaseTaskDetails =
        new BDMCaseTasksDueOnDateDetails();

      newCaseTaskDetails.caseTasksDetails = caseTaskDetails;
      newCaseTaskDetails.caseUrgentFlagStr =
        BDMTaskUtil.getCaseUrgentFlagsByTaskID(caseTaskDetails.taskID);
      newCaseTaskDetails.escalationLevelDesc =
        BDMTaskUtil.getEscalationLevelsByTaskID(caseTaskDetails.taskID);
      newList.dtls.dtls.addRef(newCaseTaskDetails);
    }

    return newList;
  }

}
