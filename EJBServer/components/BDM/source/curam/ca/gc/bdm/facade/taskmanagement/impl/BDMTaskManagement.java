/**
 *
 */
package curam.ca.gc.bdm.facade.taskmanagement.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMCALLBACKREQUESTSTATUS;
import curam.ca.gc.bdm.codetable.BDMFOREIGNAPPSTATUS;
import curam.ca.gc.bdm.entity.fact.BDMDECDRequestForCallbackFactory;
import curam.ca.gc.bdm.entity.intf.BDMDECDRequestForCallback;
import curam.ca.gc.bdm.entity.struct.BDMDECDRequestForCallbackDtls;
import curam.ca.gc.bdm.entity.struct.BDMDECDRequestForCallbackDtlsStruct1;
import curam.ca.gc.bdm.entity.struct.BDMDECDRequestForCallbackKey;
import curam.ca.gc.bdm.entity.struct.BDMDECDRequestForCallbackTaskIDKey;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskSkillTypeKey;
import curam.ca.gc.bdm.facade.taskmanagement.struct.BizObjectIDKey;
import curam.ca.gc.bdm.facade.taskmanagement.struct.TaskIDAndBizObjectTypeKey;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMEscalationLevelString;
import curam.ca.gc.bdm.sl.bdminbox.struct.CaseUrgentFlagStringDetails;
import curam.ca.gc.bdm.util.impl.BDMTaskUtil;
import curam.cefwidgets.docbuilder.impl.ContentPanelBuilder;
import curam.cefwidgets.docbuilder.impl.ImageBuilder;
import curam.cefwidgets.docbuilder.impl.LinkBuilder;
import curam.cefwidgets.docbuilder.impl.ListBuilder;
import curam.cefwidgets.docbuilder.impl.helper.impl.DocBuilderHelperFactory;
import curam.cefwidgets.docbuilder.impl.helper.impl.EntryOptions;
import curam.cefwidgets.utilities.impl.RendererConfig;
import curam.cefwidgets.utilities.impl.RendererConfig.RendererConfigType;
import curam.codetable.SKILLTYPE;
import curam.codetable.TASKPRIORITY;
import curam.codetable.TASKSTATUS;
import curam.core.facade.struct.DeferTaskDetails;
import curam.core.facade.struct.TaskContextPanelDetails;
import curam.core.facade.struct.TaskForwardDetails;
import curam.core.facade.struct.TaskManagementTaskKey;
import curam.core.sl.fact.TaskManagementFactory;
import curam.core.sl.struct.ViewTaskDetails;
import curam.message.BDMBPOTASKMANAGEMENT;
import curam.message.BPOTASKMANAGEMENT;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.LocalisableString;
import curam.util.exception.RecordNotFoundException;
import curam.util.persistence.GuiceWrapper;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.DateTime;
import curam.util.workflow.fact.BizObjAssociationFactory;
import curam.util.workflow.fact.TaskFactory;
import curam.util.workflow.intf.BizObjAssociation;
import curam.util.workflow.intf.Task;
import curam.util.workflow.struct.BizObjAssociationDtlsList;
import curam.util.workflow.struct.TaskKey;

/**
 *
 */
public class BDMTaskManagement
  extends curam.ca.gc.bdm.facade.taskmanagement.base.BDMTaskManagement {

  @Inject
  curam.ca.gc.bdm.sl.organization.supervisor.intf.BDMMaintainSupervisorUsers bdmMaintainSupervisorUsers;

  public BDMTaskManagement() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public TaskContextPanelDetails
    getBDMTaskContextPanelDetails(final TaskManagementTaskKey taskKey)
      throws AppException, InformationalException {

    final curam.core.sl.intf.TaskManagement taskManagementObj =
      TaskManagementFactory.newInstance();

    final ViewTaskDetails taskDetails =
      taskManagementObj.viewTaskHome(taskKey.taskKey);
    final ContentPanelBuilder containerPanel =
      ContentPanelBuilder.createPanel("task-container-panel");
    final ContentPanelBuilder contentPanelBuilder =
      ContentPanelBuilder.createPanel("task-context-panel-details");
    contentPanelBuilder.addRoundedCorners();
    contentPanelBuilder.addlocalisableStringItem(
      new LocalisableString(BPOTASKMANAGEMENT.INF_TASK_NUMBER_LABEL)
        .arg(taskDetails.taskID).toClientFormattedText(),
      "task-context-header");
    final EntryOptions cellLabel = DocBuilderHelperFactory
      .getEntryOptions(" task-context-table-cell-label");
    final EntryOptions cellValue = DocBuilderHelperFactory
      .getEntryOptions(" task-context-table-cell-value");
    final ListBuilder taskDetailsList = ListBuilder.createHorizontalList(6);
    taskDetailsList.addRow();
    taskDetailsList.addEntry(1, 1,
      new LocalisableString(BPOTASKMANAGEMENT.INF_STATUS_LABEL), cellLabel);
    taskDetailsList.addEntry(2, 1, CodeTable.getOneItem(TASKSTATUS.TABLENAME,
      taskDetails.status, TransactionInfo.getProgramLocale()), cellValue);
    taskDetailsList.addEntry(3, 1,
      new LocalisableString(BPOTASKMANAGEMENT.INF_WORKED_ON_BY_LABEL),
      cellLabel);
    final LinkBuilder linkBuilder = LinkBuilder.createLink(
      taskDetails.reservedByFullName, "Organization_viewUserDetailsPage.do");
    linkBuilder.addParameter("userName", taskDetails.reservedBy);
    linkBuilder.openAsModal();
    final RendererConfig linkRendererConfig =
      new RendererConfig(RendererConfigType.STYLE, "link");
    final EntryOptions linkValue =
      DocBuilderHelperFactory.getEntryOptions("task-context-table-link");
    taskDetailsList.addEntry(4, 1, linkBuilder, linkRendererConfig,
      linkValue);
    taskDetailsList.addEntry(5, 1,
      new LocalisableString(BPOTASKMANAGEMENT.INF_DEADLINE_LABEL), cellLabel);
    taskDetailsList.addEntry(6, 1, taskDetails.dueDateTime, cellValue);
    taskDetailsList.addRow();
    taskDetailsList.addEntry(1, 2,
      new LocalisableString(BPOTASKMANAGEMENT.INF_PRIORITY_LABEL), cellLabel);
    taskDetailsList
      .addEntry(
        2, 2, CodeTable.getOneItem(TASKPRIORITY.TABLENAME,
          taskDetails.priority, TransactionInfo.getProgramLocale()),
        cellValue);
    taskDetailsList.addEntry(3, 2,
      new LocalisableString(BPOTASKMANAGEMENT.INF_TIME_WORKED_LABEL),
      cellLabel);
    taskDetailsList.addEntry(4, 2, taskDetails.totalTimeWorked, cellValue);
    taskDetailsList.addEntry(5, 2,
      new LocalisableString(BPOTASKMANAGEMENT.INF_LAST_ASSIGNED_LABEL),
      cellLabel);
    taskDetailsList.addEntry(6, 2, taskDetails.assignedDateTime, cellValue);

    // Task - 25517 - show skillType in task context panel
    // Retrieve skillType using taskID\
    final curam.core.sl.entity.struct.TaskKey customTaskKey =
      new curam.core.sl.entity.struct.TaskKey();
    customTaskKey.taskID = taskKey.taskKey.taskID;
    final BDMTaskSkillTypeKey bdmTaskSkillTypeKey =
      bdmMaintainSupervisorUsers.readSkillTypeByTaskID(customTaskKey);

    taskDetailsList.addRow();
    taskDetailsList.addEntry(1, 3,
      new LocalisableString(BDMBPOTASKMANAGEMENT.INF_SKILL_TYPE_LABEL),
      cellLabel);
    taskDetailsList.addEntry(
      2, 3, CodeTable.getOneItem(SKILLTYPE.TABLENAME,
        bdmTaskSkillTypeKey.skillType, TransactionInfo.getProgramLocale()),
      cellValue);

    // Task 81173
    final CaseUrgentFlagStringDetails caseUrgentFlagStringDetails =
      bdmMaintainSupervisorUsers.getCaseUrgentFlagsByTaskID(customTaskKey);

    taskDetailsList.addEntry(3, 3,
      new LocalisableString(BDMBPOTASKMANAGEMENT.INF_URGENT_FLAGS_LABEL),
      cellLabel);
    taskDetailsList.addEntry(4, 3,
      caseUrgentFlagStringDetails.caseUrgentFlagStr, cellValue);

    // Retrieve escalation Level
    final BDMEscalationLevelString escalationLevelDesc =
      bdmMaintainSupervisorUsers.readEscalationLevelByTaskID(customTaskKey);

    taskDetailsList.addEntry(5, 3,
      new LocalisableString(BDMBPOTASKMANAGEMENT.INF_ESCALATION_LEVEL_LABEL),
      cellLabel);
    taskDetailsList.addEntry(6, 3, escalationLevelDesc.escalationLevelDesc,
      cellValue);

    // End Task 81173

    // Begin, Feature 90148
    taskDetailsList.addRow();

    final String taskCategoryDesc =
      BDMTaskUtil.getTaskCategoryDesc(customTaskKey.taskID);

    taskDetailsList.addEntry(1, 4,
      new LocalisableString(BDMBPOTASKMANAGEMENT.INF_TASK_CATEGORY_LABEL),
      cellLabel);
    taskDetailsList.addEntry(2, 4, taskCategoryDesc, cellValue);

    final String taskTypeDesc =
      BDMTaskUtil.getTaskTypeDesc(customTaskKey.taskID);

    taskDetailsList.addEntry(3, 4,
      new LocalisableString(BDMBPOTASKMANAGEMENT.INF_TASK_TYPE_LABEL),
      cellLabel);
    taskDetailsList.addEntry(4, 4, taskTypeDesc, cellValue);

    // End, Feature 90148

    contentPanelBuilder.addSingleListItem(taskDetailsList,
      "task-context-table");
    final ContentPanelBuilder subjectContent =
      ContentPanelBuilder.createPanel("task-context-subject-details");
    String icon = "ScheduleIcon";
    if (!taskDetails.dueDateTime.equals(DateTime.kZeroDateTime)
      && taskDetails.dueDateTime.before(DateTime.getCurrentDateTime())) {
      icon = "TimeOverIcon";
    }

    final ImageBuilder image = ImageBuilder.createImage(icon, "");
    image.setImageResource("curam.util.properties.global.Image");
    subjectContent.addImageItem(image);
    subjectContent.addStringItem(taskDetails.subject,
      "task-context-subject-label");
    contentPanelBuilder.addWidgetItem(subjectContent, "style",
      "content-panel");
    containerPanel.addWidgetItem(contentPanelBuilder, "style",
      "content-panel", "task-content-panel");
    final TaskContextPanelDetails contextPanelDetails =
      new TaskContextPanelDetails();
    contextPanelDetails.xmlPanelData = containerPanel.toString();
    return contextPanelDetails;
  }

  /**
   * Method to get bizObjectID using TasksID and BizObjectType
   */
  @Override
  public BizObjectIDKey
    getBizObjIDByBizObjectTypeAndTaskID(final TaskIDAndBizObjectTypeKey key)
      throws AppException, InformationalException {

    final BizObjectIDKey bizObjectIDKey = new BizObjectIDKey();
    final TaskKey taskKey = new TaskKey();
    taskKey.taskID = key.taskID;
    final BizObjAssociation bizObjAssociationObj =
      BizObjAssociationFactory.newInstance();
    final BizObjAssociationDtlsList bizObjList =
      bizObjAssociationObj.searchByTaskID(taskKey);
    final int listSize = bizObjList.dtls.size();
    for (int i = 0; i < listSize; i++) {
      if (key.bizObjectType.equals(bizObjList.dtls.item(i).bizObjectType)) {
        bizObjectIDKey.bizObjectID = bizObjList.dtls.item(i).bizObjectID;
        break;
      }
    }
    return bizObjectIDKey;
  }

  /***
   *
   */
  @Override
  public void close(final TaskManagementTaskKey taskKey)
    throws AppException, InformationalException {

    final BDMDECDRequestForCallback requestCallbackObj =
      BDMDECDRequestForCallbackFactory.newInstance();
    final BDMDECDRequestForCallbackTaskIDKey taskIDKey =
      new BDMDECDRequestForCallbackTaskIDKey();
    taskIDKey.taskID = taskKey.taskKey.taskID;
    final Task task = TaskFactory.newInstance();
    final TaskKey key = new TaskKey();
    key.taskID = taskKey.taskKey.taskID;

    try {
      final BDMDECDRequestForCallbackDtlsStruct1 details =
        requestCallbackObj.readByTaskID(taskIDKey);
      final BDMDECDRequestForCallbackDtls requestForCallbackDtls =
        new BDMDECDRequestForCallbackDtls();
      requestForCallbackDtls.assign(details);
      final BDMDECDRequestForCallbackKey decdKey =
        new BDMDECDRequestForCallbackKey();
      requestForCallbackDtls.status = BDMCALLBACKREQUESTSTATUS.COMPLETED;
      decdKey.callbackID = details.callbackID;
      requestCallbackObj.modify(decdKey, requestForCallbackDtls);
    } catch (final RecordNotFoundException e) {
      // do nothing
    }

    BDMTaskUtil.findFAandUpdateFAStatus(taskKey.taskKey.taskID,
      BDMFOREIGNAPPSTATUS.COMPLETED);

    super.close(taskKey);
  }

  /**
   * Method to defer a reserved task. Deferring a task effectively parks the
   * task until a later date.
   *
   * @param deferTaskDetails details of the task to be deferred
   */
  @Override
  public void defer(final DeferTaskDetails deferTaskDetails)
    throws AppException, InformationalException {

    BDMTaskUtil.findFAandUpdateFAStatus(
      deferTaskDetails.deferTaskDetails.taskID, BDMFOREIGNAPPSTATUS.ON_HOLD);

    super.defer(deferTaskDetails);

  }

  // START :Bug 118209: E2E Defect - Foreign Benefit Application moving to 'In
  // Progress' status upon linking attachment
  /**
   * Method allows to forward a task to a job, position, organization unit,
   * user, work queue or allocation target. This un-reserves the task therefore
   * making the task available to any user to whom the task is now assigned.
   *
   * And update status of Foreign Application to In-Progress
   *
   * @param forwardTaskDetails details of the task to be forwarded
   */

  @Override
  public void forward(final TaskForwardDetails taskForwardDetails)
    throws AppException, InformationalException {

    super.forward(taskForwardDetails);
    BDMTaskUtil.findFAandUpdateFAStatus(
      taskForwardDetails.forwardTaskDetails.taskID,
      BDMFOREIGNAPPSTATUS.IN_PROGRESS);

  }

}
