/**
 *
 */
package curam.ca.gc.bdm.sl.supervisor.impl;

import curam.ca.gc.bdm.sl.struct.BDMWorkQueueAssignedTasksSummaryDetails;
import curam.ca.gc.bdm.sl.struct.BDMWorkQueueAssignedTasksSummaryDetailsList;
import curam.ca.gc.bdm.util.impl.BDMTaskUtil;
import curam.core.sl.supervisor.struct.WorkQueueAssignedTasksSummaryDetails;
import curam.supervisor.sl.struct.TaskFilterCriteriaDetails;
import curam.supervisor.sl.struct.WorkQueueAssignedTasksSummaryDetailsList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

/**
 * Customized OOTB MaintainSupervisorWorkQueues class to add urgent flags and
 * escalation levels.
 *
 * @author donghua.jin
 *
 */
public class BDMMaintainSupervisorWorkQueuesProcess extends
  curam.ca.gc.bdm.sl.supervisor.base.BDMMaintainSupervisorWorkQueuesProcess {

  /**
   * Returns the list of tasks that are assigned to work queue based on the
   * provided task search criteria.
   *
   * Customized to add case urgent flag and escalation level information
   * for each task.
   *
   * @param dtls search criteria
   */
  @Override
  public BDMWorkQueueAssignedTasksSummaryDetailsList
    listBDMAssignedTasks(final TaskFilterCriteriaDetails dtls)
      throws AppException, InformationalException {

    final BDMWorkQueueAssignedTasksSummaryDetailsList newList =
      new BDMWorkQueueAssignedTasksSummaryDetailsList();

    final WorkQueueAssignedTasksSummaryDetailsList list =
      super.listAssignedTasks(dtls);

    for (final WorkQueueAssignedTasksSummaryDetails taskDtls : list.assignedTaskDetails.dtls) {
      final BDMWorkQueueAssignedTasksSummaryDetails newTaskDtls =
        new BDMWorkQueueAssignedTasksSummaryDetails();

      newTaskDtls.assign(taskDtls);

      newTaskDtls.caseUrgentFlagStr =
        BDMTaskUtil.getCaseUrgentFlagsByTaskID(taskDtls.taskID);
      newTaskDtls.escalationLevelDesc =
        BDMTaskUtil.getEscalationLevelsByTaskID(taskDtls.taskID);

      newList.dtls.addRef(newTaskDtls);
    }

    return newList;
  }

}
