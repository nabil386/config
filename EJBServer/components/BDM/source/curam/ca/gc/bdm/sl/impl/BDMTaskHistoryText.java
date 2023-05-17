package curam.ca.gc.bdm.sl.impl;

import curam.codetable.TASKCHANGETYPE;
import curam.core.sl.struct.TaskHistoryInformationDetails;
import curam.message.TASKHISTORYTEXT;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.LocalisableString;
import curam.util.resources.GeneralConstants;
import curam.util.workflow.fact.TaskAdminFactory;
import curam.util.workflow.fact.TaskHistoryAdminFactory;
import curam.util.workflow.intf.TaskAdmin;
import curam.util.workflow.intf.TaskHistoryAdmin;
import curam.util.workflow.struct.TaskHistoryInfo;
import curam.util.workflow.struct.TaskInfo;

// This is BDM class extending OOTB to TaskHistoryText
// Task 61300 Store on hold change type history
public class BDMTaskHistoryText
  extends curam.ca.gc.bdm.sl.base.BDMTaskHistoryText {

  /**
   * This is overridden method for BDM to add logic for task change types
   * On Hold, Brought Forward, Assigned
   */
  @Override
  protected String
    buildCommentText(final TaskHistoryInformationDetails details)
      throws AppException, InformationalException {

    String taskHistoryMessage = GeneralConstants.kEmpty;

    // If the comments field is empty then the word "null" will be displayed
    // in the client message, to workaround this we need to set the comments to
    // a space just for the message.
    if (details.comments.equals(GeneralConstants.kEmpty)) {
      details.comments = GeneralConstants.kSpace;
    }
    if (TASKCHANGETYPE.RESERVED.equals(details.taskChangeType)) {
      taskHistoryMessage = getReservedMessage(details);
    } else if (TASKCHANGETYPE.COMMENTADDED.equals(details.taskChangeType)) {
      taskHistoryMessage = getCommentMessage(details);
    } else if (TASKCHANGETYPE.TIMEWORKEDCHANGED
      .equals(details.taskChangeType)) {
      taskHistoryMessage = getTimeWorkedChangedMessage(details);
    } else if (TASKCHANGETYPE.UNRESERVED.equals(details.taskChangeType)) {
      taskHistoryMessage = getMadeAvailableMessage(details);
    } else if (TASKCHANGETYPE.FORWARDED.equals(details.taskChangeType)) {
      taskHistoryMessage = getForwardedMessage(details);
    } else if (TASKCHANGETYPE.REALLOCATION.equals(details.taskChangeType)) {
      taskHistoryMessage = getReallocatedMessage(details);
    } else if (TASKCHANGETYPE.DEFERRED.equals(details.taskChangeType)) {
      taskHistoryMessage = getDeferredMessage(details);
    } else if (TASKCHANGETYPE.RESTARTED.equals(details.taskChangeType)) {
      taskHistoryMessage = getRestartedMessage(details);
    } else if (TASKCHANGETYPE.CLOSED.equals(details.taskChangeType)) {
      taskHistoryMessage = getClosedMessage(details);
    } else if (TASKCHANGETYPE.CREATED.equals(details.taskChangeType)) {
      taskHistoryMessage = getCreatedMessage(details);
    } else if (TASKCHANGETYPE.DEADLINECHANGED
      .equals(details.taskChangeType)) {
      taskHistoryMessage = getDeadlineModifiedMessage(details);
    } else if (TASKCHANGETYPE.PRIORITYCHANGED
      .equals(details.taskChangeType)) {
      taskHistoryMessage = getPriorityModifiedMessage(details);
    } else if (TASKCHANGETYPE.DEADLINEEXPIRED
      .equals(details.taskChangeType)) {
      taskHistoryMessage = getDeadlineExpiredMessage(details);
    } else if (TASKCHANGETYPE.ALLOCATIONFAILED
      .equals(details.taskChangeType)) {
      taskHistoryMessage = getAllocationFailedMessage(details);
    } else if (TASKCHANGETYPE.ALLOCATEDTODEFAULTQUEUE
      .equals(details.taskChangeType)) {
      taskHistoryMessage = getAllocatedToDefaultQueueMessage(details);
    } else if (TASKCHANGETYPE.AUTOMATICADDTOUSERTASKS
      .equals(details.taskChangeType)) {
      taskHistoryMessage = getAutomaticAddToUserTasksMessage(details);
    } else if (TASKCHANGETYPE.ONHOLD.equals(details.taskChangeType)) {
      taskHistoryMessage = getOnHoldMessage(details);
    }
    return taskHistoryMessage;

  }

  /**
   * Method converts a Task History of type reserved to a textual format.
   *
   * @param taskHistoryDetails a Task History Details to be converted to text.
   *
   * @return A task history in text format.
   *
   * Task 61300 Store on hold change type history 2022-07-08 Mohan
   */
  protected String
    getOnHoldMessage(final TaskHistoryInformationDetails taskHistoryDetails)
      throws AppException, InformationalException {

    final LocalisableString taskHistory =
      new LocalisableString(TASKHISTORYTEXT.INF_TASK_HISTORY_ONHOLD);
    taskHistory.arg(convertDateTimeToClientFormattedMessageText(
      taskHistoryDetails.changedDateTime));
    taskHistory
      .arg(getUserTimeZoneDisplayName(taskHistoryDetails.changedDateTime));
    taskHistory.arg(taskHistoryDetails.userFullName);

    // Get task restart time
    final TaskHistoryAdmin taskHistoryAdminObj =
      TaskHistoryAdminFactory.newInstance();
    final TaskHistoryInfo taskHistoryInfo =
      taskHistoryAdminObj.read(taskHistoryDetails.taskHistoryID);
    final TaskAdmin taskAdminObj = TaskAdminFactory.newInstance();
    final TaskInfo taskInfo = taskAdminObj.read(taskHistoryInfo.taskID);
    taskHistory
      .arg(convertDateTimeToClientFormattedMessageText(taskInfo.restartTime));

    taskHistory.arg(taskHistoryDetails.comments);

    return taskHistory.toClientFormattedText();
  }
}
