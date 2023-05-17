/**
 *
 */
package curam.ca.gc.bdm.sl.supervisor.impl;

import curam.ca.gc.bdm.sl.struct.BDMWorkQueueReservedTasksForUserDetails;
import curam.ca.gc.bdm.sl.struct.BDMWorkQueueReservedTasksForUserDetailsList;
import curam.ca.gc.bdm.util.impl.BDMTaskUtil;
import curam.core.sl.supervisor.struct.WorkQueueReservedTasksForUserDetails;
import curam.core.sl.supervisor.struct.WorkQueueReservedTasksForUserDetailsList;
import curam.core.sl.supervisor.struct.WorkQueueReservedTasksForUserKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

/**
 * Customized WorkQueueWorkspace class to add urgent flags and escalation
 * levels.
 *
 * @author donghua.jin
 *
 */
public class BDMWorkQueueWorkspaceProcess
  extends curam.ca.gc.bdm.sl.supervisor.base.BDMWorkQueueWorkspaceProcess {

  /**
   * Returns a list of the tasks reserved by the specified user for the
   * required work queue.
   *
   * @param key
   */
  @Override
  public BDMWorkQueueReservedTasksForUserDetailsList
    getBDMWorkQueueReservedTasksForUser(
      final WorkQueueReservedTasksForUserKey key)
      throws AppException, InformationalException {

    final BDMWorkQueueReservedTasksForUserDetailsList newList =
      new BDMWorkQueueReservedTasksForUserDetailsList();

    final WorkQueueReservedTasksForUserDetailsList list =
      super.getWorkQueueReservedTasksForUser(key);

    for (final WorkQueueReservedTasksForUserDetails taskDtls : list.dtls) {
      final BDMWorkQueueReservedTasksForUserDetails newTaskDtls =
        new BDMWorkQueueReservedTasksForUserDetails();

      newTaskDtls.caseUrgentFlagStr =
        BDMTaskUtil.getCaseUrgentFlagsByTaskID(taskDtls.taskID);
      newTaskDtls.escalationLevelDesc =
        BDMTaskUtil.getEscalationLevelsByTaskID(taskDtls.taskID);

      newList.dtls.add(newTaskDtls);
    }

    return newList;
  }

}
