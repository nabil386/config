package curam.ca.gc.bdm.facade.bdmworkallocation.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMListDeferredTasksReservedByWorkQueueUserDetails;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMWorkQueueAssignedTasksSummaryDetailsList;
import curam.ca.gc.bdm.sl.struct.BDMWorkQueueAssignedTasksSummaryDetails;
import curam.ca.gc.bdm.sl.struct.BDMWorkQueueReservedTasksForUserDetails;
import curam.ca.gc.bdm.util.impl.BDMTaskUtil;
import curam.codetable.TARGETITEMTYPE;
import curam.core.sl.entity.struct.WorkQueueKey;
import curam.core.sl.supervisor.struct.WorkQueueAssignedTasksSummaryDetails;
import curam.core.sl.supervisor.struct.WorkQueueReservedTasksForUserDetails;
import curam.core.struct.InformationalMsgDtls;
import curam.supervisor.facade.struct.ListDeferredTasksReservedByWorkQueueUserDetails;
import curam.supervisor.facade.struct.ReserveWorkQueueTasksToUserDetails;
import curam.supervisor.facade.struct.TaskFilterCriteriaDetails;
import curam.supervisor.facade.struct.UserNameAndWorkQueueIDKey;
import curam.supervisor.facade.struct.WorkQueueAssignedTasksSummaryDetailsList;
import curam.supervisor.sl.fact.SupervisorApplicationPageContextDescriptionFactory;
import curam.supervisor.sl.struct.SupervisorApplicationPageContextDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.transaction.TransactionInfo;
import java.util.Collections;
import java.util.Comparator;

public class BDMMaintainSupervisorWorkQueues extends
  curam.ca.gc.bdm.facade.bdmworkallocation.base.BDMMaintainSupervisorWorkQueues {

  @Inject
  curam.ca.gc.bdm.sl.supervisor.intf.BDMMaintainSupervisorWorkQueuesProcess bdmMaintainSupervisorWorkQueuesProcess;

  public BDMMaintainSupervisorWorkQueues() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * {@inheritDoc}
   *
   * Customized to add case urgent flag and escalation level information
   * for each task.
   *
   */
  @Override
  public BDMWorkQueueAssignedTasksSummaryDetailsList
    listBDMTasksAssignedToWorkQueue(
      final TaskFilterCriteriaDetails taskFilterCriteriaDetails)
      throws AppException, InformationalException {

    final BDMWorkQueueAssignedTasksSummaryDetailsList bdmWorkQueueAssignedTasksSummaryDetailsList =
      new BDMWorkQueueAssignedTasksSummaryDetailsList();

    final SupervisorApplicationPageContextDetails pageContextDetails =
      new SupervisorApplicationPageContextDetails();

    final TaskFilterCriteriaDetails taskSearchcriteria =
      new TaskFilterCriteriaDetails();

    taskSearchcriteria.assign(taskFilterCriteriaDetails);
    taskSearchcriteria.details.assigneeType = TARGETITEMTYPE.WORKQUEUE;

    bdmWorkQueueAssignedTasksSummaryDetailsList.assignedTaskDetails =
      bdmMaintainSupervisorWorkQueuesProcess
        .listBDMAssignedTasks(taskSearchcriteria.details);

    final WorkQueueKey workQueueKey = new WorkQueueKey();
    workQueueKey.workQueueID = taskFilterCriteriaDetails.details.relatedID;

    pageContextDetails.description =
      SupervisorApplicationPageContextDescriptionFactory.newInstance()
        .readWorkQueuePageContextDescription(workQueueKey).description;

    bdmWorkQueueAssignedTasksSummaryDetailsList.pageContext
      .assign(pageContextDetails);

    for (final String warning : TransactionInfo.getInformationalManager()
      .obtainInformationalAsString()) {

      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();

      informationalMsgDtls.informationMsgTxt = warning;

      bdmWorkQueueAssignedTasksSummaryDetailsList.informationalMsgDetlsList.dtls
        .addRef(informationalMsgDtls);

    }

    Collections.sort(
      bdmWorkQueueAssignedTasksSummaryDetailsList.assignedTaskDetails.dtls,
      new BDMSortByEscalationAscending());

    return bdmWorkQueueAssignedTasksSummaryDetailsList;
  }

  /**
   * Comparator class to sort
   * <code>BDMWorkQueueAssignedTasksSummaryDetails</code>.
   */
  class BDMSortByEscalationAscending
    implements Comparator<BDMWorkQueueAssignedTasksSummaryDetails> {

    @Override
    public int compare(final BDMWorkQueueAssignedTasksSummaryDetails o1,
      final BDMWorkQueueAssignedTasksSummaryDetails o2) {

      return o1.escalationLevelDesc.compareTo(o2.escalationLevelDesc);
    }
  }

  /**
   * {@inheritDoc}
   *
   * Customized to add case urgent flag and escalation level information
   * for each task.
   */
  @Override
  public BDMWorkQueueAssignedTasksSummaryDetailsList
    reserveBDMWorkQueueAssignedTasksToUser(
      final ReserveWorkQueueTasksToUserDetails key)
      throws AppException, InformationalException {

    final BDMWorkQueueAssignedTasksSummaryDetailsList newList =
      new BDMWorkQueueAssignedTasksSummaryDetailsList();

    final WorkQueueAssignedTasksSummaryDetailsList list =
      super.reserveWorkQueueAssignedTasksToUser(key);

    newList.assign(list);

    for (final WorkQueueAssignedTasksSummaryDetails details : list.assignedTaskDetails.assignedTaskDetails.dtls) {
      final BDMWorkQueueAssignedTasksSummaryDetails newDetails =
        new BDMWorkQueueAssignedTasksSummaryDetails();

      newDetails.assign(details);
      newDetails.caseUrgentFlagStr =
        BDMTaskUtil.getCaseUrgentFlagsByTaskID(details.taskID);
      newDetails.escalationLevelDesc =
        BDMTaskUtil.getEscalationLevelsByTaskID(details.taskID);
      newList.assignedTaskDetails.dtls.addRef(newDetails);
    }

    return newList;
  }

  /**
   * {@inheritDoc}
   *
   * Customized to add case urgent flag and escalation level information
   * for each task.
   */
  @Override
  public BDMListDeferredTasksReservedByWorkQueueUserDetails
    listBDMDeferredTasksReservedByWorkQueueUser(
      final UserNameAndWorkQueueIDKey key)
      throws AppException, InformationalException {

    final BDMListDeferredTasksReservedByWorkQueueUserDetails newDetails =
      new BDMListDeferredTasksReservedByWorkQueueUserDetails();

    final ListDeferredTasksReservedByWorkQueueUserDetails details =
      super.listDeferredTasksReservedByWorkQueueUser(key);

    newDetails.pageContext = details.pageContext;
    for (final WorkQueueReservedTasksForUserDetails wqRtDetails : details.details.deferredTaskList.dtls) {
      final BDMWorkQueueReservedTasksForUserDetails newWqRtDetails =
        new BDMWorkQueueReservedTasksForUserDetails();

      newWqRtDetails.assign(wqRtDetails);
      newWqRtDetails.caseUrgentFlagStr =
        BDMTaskUtil.getCaseUrgentFlagsByTaskID(wqRtDetails.taskID);
      newWqRtDetails.escalationLevelDesc =
        BDMTaskUtil.getEscalationLevelsByTaskID(wqRtDetails.taskID);
      newDetails.details.deferredTaskList.dtls.addRef(newWqRtDetails);
    }

    return newDetails;
  }

}
