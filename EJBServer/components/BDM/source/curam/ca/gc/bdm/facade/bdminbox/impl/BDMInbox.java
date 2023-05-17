package curam.ca.gc.bdm.facade.bdminbox.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.facade.bdminbox.struct.BDMLimitedListDeferredTaskDetails;
import curam.ca.gc.bdm.facade.bdminbox.struct.BDMLimitedListReservedTaskDetails;
import curam.ca.gc.bdm.facade.bdminbox.struct.BDMLimitedListWorkQueueUnreservedTasksDetails;
import curam.ca.gc.bdm.facade.bdminbox.struct.BDMUnreservedWorkQueueTaskDetails;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskQueryKey;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskSkillTypeKey;
import curam.ca.gc.bdm.sl.bdminbox.fact.BDMInboxProcessFactory;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMAvailableTaskSearchResult;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMDeferredTaskDetails;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMEscalationLevelString;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMReservedByStatusTaskDetails;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMTaskQueryResultDetails;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMTaskQueryResultDetailsList;
import curam.ca.gc.bdm.sl.bdminbox.struct.CaseUrgentFlagStringDetails;
import curam.ca.gc.bdm.util.impl.BDMTaskUtil;
import curam.core.facade.struct.LimitedListDeferredTaskDetails;
import curam.core.facade.struct.LimitedListReservedTaskDetails;
import curam.core.facade.struct.LimitedListWorkQueueUnreservedTasksDetails;
import curam.core.facade.struct.ListWorkQueueUnreservedTasksKey;
import curam.core.sl.entity.struct.TaskKey;
import curam.core.struct.AvailableTaskSearchResult;
import curam.core.struct.InformationalMsgDtls;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
// import curam.util.workflow.struct.TaskKey;
import curam.util.transaction.TransactionInfo;

public class BDMInbox extends curam.ca.gc.bdm.facade.bdminbox.base.BDMInbox {

  BDMUtil bdmUtil = new BDMUtil();

  @Inject
  curam.ca.gc.bdm.sl.organization.supervisor.intf.BDMMaintainSupervisorUsers bdmMaintainSupervisorUsers;

  public BDMInbox() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  // ___________________________________________________________________________
  /**
   * Returns a list of tasks of a particular status(e.g. Open or Deferred) that
   * are reserved by the current user. This method limits the number of tasks
   * that are returned to the client.
   *
   * @return A list of tasks of that are reserved by the current user.
   */
  @Override
  public BDMLimitedListReservedTaskDetails listBDMLimitedReservedTasks()
    throws AppException, InformationalException {

    final BDMLimitedListReservedTaskDetails bdmLimitedListReservedTaskDetails =
      new BDMLimitedListReservedTaskDetails();
    LimitedListReservedTaskDetails limitedListReservedTaskDetails =
      new LimitedListReservedTaskDetails();

    // Call Super class method
    limitedListReservedTaskDetails = super.listLimitedReservedTasks();

    bdmLimitedListReservedTaskDetails.assign(limitedListReservedTaskDetails);

    TaskKey taskKey = null;
    CaseUrgentFlagStringDetails caseUrgentFlagStringDetails = null;
    BDMReservedByStatusTaskDetails bdmReservedByStatusTaskDetails = null;

    BDMEscalationLevelString escalationLevelDesc = null;

    BDMTaskSkillTypeKey bdmTaskSkillTypeKey = null;
    for (int i =
      0; i < bdmLimitedListReservedTaskDetails.taskDetailsList.taskDetailsList
        .size(); i++) {

      bdmReservedByStatusTaskDetails = new BDMReservedByStatusTaskDetails();
      bdmReservedByStatusTaskDetails =
        bdmLimitedListReservedTaskDetails.taskDetailsList.taskDetailsList
          .get(i);

      taskKey = new TaskKey();
      caseUrgentFlagStringDetails = new CaseUrgentFlagStringDetails();
      taskKey.taskID = bdmReservedByStatusTaskDetails.taskID;
      caseUrgentFlagStringDetails =
        bdmMaintainSupervisorUsers.getCaseUrgentFlagsByTaskID(taskKey);

      bdmReservedByStatusTaskDetails.caseUrgentFlagStr =
        caseUrgentFlagStringDetails.caseUrgentFlagStr;

      bdmTaskSkillTypeKey = new BDMTaskSkillTypeKey();
      bdmTaskSkillTypeKey =
        bdmMaintainSupervisorUsers.readSkillTypeByTaskID(taskKey);

      bdmReservedByStatusTaskDetails.skillType =
        bdmTaskSkillTypeKey.skillType;

      escalationLevelDesc = new BDMEscalationLevelString();
      // Retrieve escalation Level
      escalationLevelDesc =
        bdmMaintainSupervisorUsers.readEscalationLevelByTaskID(taskKey);

      bdmReservedByStatusTaskDetails.escalationLevelDesc =
        escalationLevelDesc.escalationLevelDesc;

      bdmLimitedListReservedTaskDetails.taskDetailsList.taskDetailsList
        .get(i).editTaskDeadlineListActionInd =
          bdmUtil.isAllowedToEditTaskDeadline(
            bdmLimitedListReservedTaskDetails.taskDetailsList.taskDetailsList
              .get(i).taskID);

    }

    return bdmLimitedListReservedTaskDetails;
  }

  /**
   * This method Returns a list of deferred tasks belonging to the current user.
   *
   * @return BDMLimitedListDeferredTaskDetails A list of deferred tasks
   * belonging to the current user.
   */
  @Override
  public BDMLimitedListDeferredTaskDetails listBDMLimitedDeferredTasks()
    throws AppException, InformationalException {

    final BDMLimitedListDeferredTaskDetails bdmLimitedListDeferredTaskDetails =
      new BDMLimitedListDeferredTaskDetails();

    LimitedListDeferredTaskDetails limitedListDeferredTaskDetails =
      new LimitedListDeferredTaskDetails();

    // Call superclass method
    limitedListDeferredTaskDetails = super.listLimitedDeferredTasks();

    bdmLimitedListDeferredTaskDetails.assign(limitedListDeferredTaskDetails);

    BDMEscalationLevelString escalationLevelDesc = null;
    TaskKey taskKey = null;
    CaseUrgentFlagStringDetails caseUrgentFlagStringDetails = null;
    BDMDeferredTaskDetails bdmDeferredTaskDetails = null;
    // Task 25517 - return skill type
    BDMTaskSkillTypeKey bdmTaskSkillTypeKey = null;
    for (int i =
      0; i < bdmLimitedListDeferredTaskDetails.taskDetailsList.taskDetailsList
        .size(); i++) {

      bdmDeferredTaskDetails = new BDMDeferredTaskDetails();
      bdmDeferredTaskDetails =
        bdmLimitedListDeferredTaskDetails.taskDetailsList.taskDetailsList
          .get(i);

      taskKey = new TaskKey();
      caseUrgentFlagStringDetails = new CaseUrgentFlagStringDetails();
      taskKey.taskID = bdmDeferredTaskDetails.taskID;
      caseUrgentFlagStringDetails =
        bdmMaintainSupervisorUsers.getCaseUrgentFlagsByTaskID(taskKey);

      bdmDeferredTaskDetails.caseUrgentFlagStr =
        caseUrgentFlagStringDetails.caseUrgentFlagStr;

      // Task 25517 - return skill type
      bdmTaskSkillTypeKey = new BDMTaskSkillTypeKey();
      bdmTaskSkillTypeKey =
        bdmMaintainSupervisorUsers.readSkillTypeByTaskID(taskKey);

      bdmDeferredTaskDetails.skillType = bdmTaskSkillTypeKey.skillType;

      // Retrieve escalation Level
      escalationLevelDesc = new BDMEscalationLevelString();
      escalationLevelDesc =
        bdmMaintainSupervisorUsers.readEscalationLevelByTaskID(taskKey);

      bdmDeferredTaskDetails.escalationLevelDesc =
        escalationLevelDesc.escalationLevelDesc;

    }

    return bdmLimitedListDeferredTaskDetails;
  }

  /**
   * This method searches for available tasks for the currently logged in user
   * using the search criteria stored for the user in the database. OOTb method
   * is customized to reflect case urgent flag and escalation level in search
   * result
   *
   * @return BDMAvailableTaskSearchResult
   *
   */
  @Override
  public BDMAvailableTaskSearchResult searchAvailableTasksExt()
    throws AppException, InformationalException {

    final BDMAvailableTaskSearchResult bdmAvailableTaskSearchResult =
      new BDMAvailableTaskSearchResult();

    AvailableTaskSearchResult availableTaskSearchResult =
      new AvailableTaskSearchResult();

    // Call super class mthod
    availableTaskSearchResult = super.searchAvailableTasks();

    bdmAvailableTaskSearchResult.assign(availableTaskSearchResult);

    TaskKey taskKey = null;
    CaseUrgentFlagStringDetails caseUrgentFlagStringDetails = null;
    BDMTaskQueryResultDetails bdmaskQueryResultDetails = null;
    // TASK 81174
    BDMEscalationLevelString escalationLevelDesc = null;
    // Task 25517 - return skill type
    BDMTaskSkillTypeKey bdmTaskSkillTypeKey = null;
    for (int i =
      0; i < bdmAvailableTaskSearchResult.resultList.taskDetailsList
        .size(); i++) {

      bdmaskQueryResultDetails = new BDMTaskQueryResultDetails();
      bdmaskQueryResultDetails =
        bdmAvailableTaskSearchResult.resultList.taskDetailsList.get(i);

      taskKey = new TaskKey();
      caseUrgentFlagStringDetails = new CaseUrgentFlagStringDetails();
      taskKey.taskID = bdmaskQueryResultDetails.taskID;
      caseUrgentFlagStringDetails =
        bdmMaintainSupervisorUsers.getCaseUrgentFlagsByTaskID(taskKey);

      bdmaskQueryResultDetails.caseUrgentFlagStr =
        caseUrgentFlagStringDetails.caseUrgentFlagStr;

      // Task 25517 - return skill type

      bdmTaskSkillTypeKey = new BDMTaskSkillTypeKey();
      bdmTaskSkillTypeKey =
        bdmMaintainSupervisorUsers.readSkillTypeByTaskID(taskKey);

      bdmaskQueryResultDetails.skillType = bdmTaskSkillTypeKey.skillType;

      // TASK 81174 : Retrieve escalation Level
      escalationLevelDesc =
        bdmMaintainSupervisorUsers.readEscalationLevelByTaskID(taskKey);

      bdmaskQueryResultDetails.escalationLevelDesc =
        escalationLevelDesc.escalationLevelDesc;

    }

    return bdmAvailableTaskSearchResult;
  }

  // START TASK 81174

  /**
   * Returns a list of work queue tasks that are unreserved. OOtb method is
   * customized to display case urgent flag and escaltion level
   *
   * @return BDMLimitedListWorkQueueUnreservedTasksDetails
   *
   */

  @Override
  public BDMLimitedListWorkQueueUnreservedTasksDetails
    listBDMLimitedUnreservedWorkQueueTasks(
      final ListWorkQueueUnreservedTasksKey key)
      throws AppException, InformationalException {

    LimitedListWorkQueueUnreservedTasksDetails returnList =
      new LimitedListWorkQueueUnreservedTasksDetails();

    final BDMLimitedListWorkQueueUnreservedTasksDetails bdmReturnList =
      new BDMLimitedListWorkQueueUnreservedTasksDetails();
    returnList = super.listLimitedUnreservedWorkQueueTasks(key);

    bdmReturnList.dtls.dtlsList.assign(returnList.dtls.dtlslist);

    TaskKey taskKey = null;
    CaseUrgentFlagStringDetails caseUrgentFlagStringDetails = null;

    BDMEscalationLevelString escalationLevelDesc = null;

    BDMUnreservedWorkQueueTaskDetails bdmUnreservedWorkQueueTaskDetails =
      null;

    // For each task in the liost retrieve case urgent flag and escalation level
    for (int i = 0; i < bdmReturnList.dtls.dtlsList.dtls.size(); i++) {

      bdmUnreservedWorkQueueTaskDetails =
        new BDMUnreservedWorkQueueTaskDetails();

      bdmUnreservedWorkQueueTaskDetails =
        bdmReturnList.dtls.dtlsList.dtls.get(i);

      taskKey = new TaskKey();
      caseUrgentFlagStringDetails = new CaseUrgentFlagStringDetails();
      taskKey.taskID = bdmUnreservedWorkQueueTaskDetails.taskID;
      caseUrgentFlagStringDetails =
        bdmMaintainSupervisorUsers.getCaseUrgentFlagsByTaskID(taskKey);

      bdmUnreservedWorkQueueTaskDetails.caseUrgentFlagStr =
        caseUrgentFlagStringDetails.caseUrgentFlagStr;

      // TASK 81174 : Retrieve escalation Level
      escalationLevelDesc =
        bdmMaintainSupervisorUsers.readEscalationLevelByTaskID(taskKey);

      bdmUnreservedWorkQueueTaskDetails.escalationLevelDesc =
        escalationLevelDesc.escalationLevelDesc;

    }

    // Obtain the informational(s) to be returned to the client
    final String[] warnings =
      TransactionInfo.getInformationalManager().obtainInformationalAsString();

    for (int i = 0; i < warnings.length; i++) {
      final InformationalMsgDtls infoMsgDtls = new InformationalMsgDtls();

      infoMsgDtls.informationMsgTxt = warnings[i];
      returnList.informationalMsgDetailsList.dtls.addRef(infoMsgDtls);
    }

    return bdmReturnList;

  }

  /**
   * {@inheritDoc}
   *
   * Customized to pull urgent flag and escalation level.
   */
  @Override
  public BDMTaskQueryResultDetailsList
    searchBDMForTasks(final BDMTaskQueryKey searchTaskKey)
      throws AppException, InformationalException {

    final curam.core.sl.intf.Inbox inboxObj =
      curam.core.sl.fact.InboxFactory.newInstance();

    final BDMTaskQueryResultDetailsList list =
      BDMInboxProcessFactory.newInstance().searchBDMForTasks(searchTaskKey,
        inboxObj.getInboxTaskReadMultiDetails());

    // Obtain the informational(s) to be returned to the client
    final String[] warnings = curam.util.transaction.TransactionInfo
      .getInformationalManager().obtainInformationalAsString();

    for (int i = 0; i < warnings.length; i++) {

      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();

      informationalMsgDtls.informationMsgTxt = warnings[i];
      list.informationalMsgDetailsList.dtls.addRef(informationalMsgDtls);
    }

    for (final BDMTaskQueryResultDetails taskQueryResultDetails : list.taskDetailsList) {

      final long taskID = taskQueryResultDetails.taskID;

      taskQueryResultDetails.caseUrgentFlagStr =
        BDMTaskUtil.getCaseUrgentFlagsByTaskID(taskID);
      taskQueryResultDetails.escalationLevelDesc =
        BDMTaskUtil.getEscalationLevelsByTaskID(taskID);
      taskQueryResultDetails.taskCategoryDesc =
        BDMTaskUtil.getTaskCategoryDesc(taskID);
      taskQueryResultDetails.taskTypeDesc =
        BDMTaskUtil.getTaskTypeDesc(taskID);
    }

    return list;
  }

}
