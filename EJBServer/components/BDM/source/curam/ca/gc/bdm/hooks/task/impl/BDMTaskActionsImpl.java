package curam.ca.gc.bdm.hooks.task.impl;

import curam.codetable.TASKSTATUS;
import curam.core.hook.task.impl.TaskActionsImpl;
import curam.core.sl.infrastructure.impl.ValidationManagerFactory;
import curam.message.BPOTASKMANAGEMENT;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.Date;
import curam.util.workflow.fact.TaskAdminFactory;
import curam.util.workflow.intf.TaskAdmin;
import curam.util.workflow.struct.TaskDetailsWithDueDate;

public class BDMTaskActionsImpl extends TaskActionsImpl {

  public BDMTaskActionsImpl() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Task 58368 Validate defer task - suppress validation to allow any date for
   * deadline
   * skillType
   */
  @Override
  protected void validateDefer(final long taskID, final String userName,
    final Date restartDate) throws AppException, InformationalException {

    this.checkMaintainSecurity(taskID);
    final TaskAdmin taskObj = TaskAdminFactory.newInstance();
    final TaskDetailsWithDueDate taskFullDtlsWithDueDate =
      taskObj.readTaskDetailsWithDueDate(taskID);
    if (TASKSTATUS.DEFERRED.equals(taskFullDtlsWithDueDate.status)) {
      ValidationManagerFactory.getManager().throwWithLookup(
        new AppException(BPOTASKMANAGEMENT.ERR_TASK_ALREADY_DEFERRED), "a",
        0);
    }

    if (TASKSTATUS.CLOSED.equals(taskFullDtlsWithDueDate.status)) {
      ValidationManagerFactory.getManager().throwWithLookup(new AppException(
        BPOTASKMANAGEMENT.ERR_TASK_CLOSED_CANNOT_BE_DEFERRED), "a", 0);
    }

    if (!taskFullDtlsWithDueDate.reservedBy.equals(userName)) {
      ValidationManagerFactory.getManager().throwWithLookup(
        new AppException(BPOTASKMANAGEMENT.ERR_TASK_MUST_BE_RESERVED_TO_YOU),
        "a", 0);
    }

    if (!restartDate.isZero()) {
      if (restartDate.before(Date.getCurrentDate())
        || restartDate.equals(Date.getCurrentDate())) {
        ValidationManagerFactory.getManager()
          .throwWithLookup(new AppException(
            BPOTASKMANAGEMENT.ERR_RESTART_DATE_MUST_BE_LATER_THAN_CURRENT_DATE),
            "a", 0);
      }

      // Task 58368 - Suppress validation to allow any date for deadline
      // if (!taskFullDtlsWithDueDate.dueDateTime.isZero() && restartDate
      // .getDateTime().after(taskFullDtlsWithDueDate.dueDateTime)) {
      // ValidationManagerFactory.getManager()
      // .throwWithLookup(new AppException(
      // BPOTASKMANAGEMENT.ERR_RESTART_DATE_MUST_BE_EARLIER_THAN_TASK_DEADLINE),
      // "a", 0);
      // }
    }

  }

}
