package curam.ca.gc.bdm.sl.bdmworkallocation.impl;

import curam.ca.gc.bdm.entity.fact.BDMTaskClassificationFactory;
import curam.ca.gc.bdm.entity.fact.BDMTaskFactory;
import curam.ca.gc.bdm.entity.intf.BDMTask;
import curam.ca.gc.bdm.entity.intf.BDMTaskClassification;
import curam.ca.gc.bdm.entity.struct.BDMTaskClassificationDtlsList;
import curam.ca.gc.bdm.entity.struct.BDMTaskDtls;
import curam.ca.gc.bdm.entity.struct.BDMTaskKey;
import curam.ca.gc.bdm.entity.struct.BDMTaskSkillKey;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMStandardManualTaskDtls;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskCategoryAndTypeDetails;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskSkillTypeKey;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskSkillTypeList;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMVSGTaskCreateDetails;
import curam.ca.gc.bdm.sl.bdmworkallocation.struct.BDMDocumentTypeKey;
import curam.ca.gc.bdm.sl.bdmworkallocation.struct.BDMEvidenceTypeKey;
import curam.ca.gc.bdm.sl.bdmworkallocation.struct.BDMTaskBringForwardDtls;
import curam.ca.gc.bdm.sl.bdmworkallocation.struct.BDMWorkQueueID;
import curam.codetable.ASSIGNEETYPE;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.DOCUMENTTYPE;
import curam.codetable.LOCATIONACCESSTYPE;
import curam.codetable.SKILLTYPE;
import curam.codetable.TARGETITEMTYPE;
import curam.codetable.TASKCHANGETYPE;
import curam.codetable.TASKSTATUS;
import curam.core.impl.DataBasedSecurity;
import curam.core.impl.EnvVars;
import curam.core.impl.SecurityImplementationFactory;
import curam.core.sl.entity.fact.TaskAssignmentFactory;
import curam.core.sl.entity.fact.WorkQueueFactory;
import curam.core.sl.entity.intf.TaskAssignment;
import curam.core.sl.entity.intf.WorkQueue;
import curam.core.sl.entity.struct.TaskAssignmentNameDetailsList;
import curam.core.sl.entity.struct.TaskIDRelatedIDAndTypeKey;
import curam.core.sl.entity.struct.WorkQueueDtls;
import curam.core.sl.entity.struct.WorkQueueKey;
import curam.core.sl.fact.ClientMergeFactory;
import curam.core.sl.fact.TaskManagementFactory;
import curam.core.sl.fact.TaskManagementUtilityFactory;
import curam.core.sl.impl.DefaultWorkResolverAdapter;
import curam.core.sl.impl.DefaultWorkResolverHelper;
import curam.core.sl.infrastructure.impl.ValidationManagerFactory;
import curam.core.sl.intf.ClientMerge;
import curam.core.sl.intf.TaskManagement;
import curam.core.sl.intf.TaskManagementUtility;
import curam.core.sl.struct.AllocationTargetDetails;
import curam.core.sl.struct.AllocationTargetList;
import curam.core.sl.struct.CreateManualTaskKey_eo;
import curam.core.sl.struct.DateTimeInSecondsKey;
import curam.core.sl.struct.DeadlineDuration;
import curam.core.sl.struct.ParticipantSecurityCheckKey;
import curam.core.sl.struct.TaskCreateDetails;
import curam.core.sl.struct.TaskManagementTaskKey;
import curam.core.struct.CaseSecurityCheckKey;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.CuramInd;
import curam.core.struct.DataBasedSecurityResult;
import curam.message.BPOTASKMANAGEMENT;
import curam.message.BPOWORKALLOCATIONTASK;
import curam.message.GENERALCASE;
import curam.message.GENERALCONCERN;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.internal.workflow.fact.ActivityInstanceFactory;
import curam.util.internal.workflow.intf.ActivityInstance;
import curam.util.internal.workflow.struct.ActivityInstanceDtlsList;
import curam.util.internal.workflow.struct.ProcessInstanceKey;
import curam.util.resources.Configuration;
import curam.util.transaction.TransactionInfo;
import curam.util.type.DateTime;
import curam.util.type.NotFoundIndicator;
import curam.util.workflow.fact.BizObjAssociationFactory;
import curam.util.workflow.fact.TaskAdminFactory;
import curam.util.workflow.fact.TaskFactory;
import curam.util.workflow.fact.TaskHistoryAdminFactory;
import curam.util.workflow.impl.EnactmentService;
import curam.util.workflow.intf.BizObjAssociation;
import curam.util.workflow.intf.Task;
import curam.util.workflow.intf.TaskAdmin;
import curam.util.workflow.intf.TaskHistoryAdmin;
import curam.util.workflow.struct.BizObjAssociationDtls;
import curam.util.workflow.struct.TaskDetails;
import curam.util.workflow.struct.TaskDtls;
import curam.util.workflow.struct.TaskKey;
import java.util.ArrayList;
import java.util.List;

public class BDMWorkAllocationTask
  extends curam.ca.gc.bdm.sl.bdmworkallocation.base.BDMWorkAllocationTask {

  /**
   * Task 12746 - Create manual task with skill type
   */
  @Override
  public CreateManualTaskKey_eo
    createTaskWithVSG(final BDMStandardManualTaskDtls details)
      throws AppException, InformationalException {

    final DataBasedSecurity dataBasedSecurity =
      SecurityImplementationFactory.get();
    final CaseSecurityCheckKey caseSecurityCheckKey =
      new CaseSecurityCheckKey();

    caseSecurityCheckKey.caseID = details.dtls.concerningDtls.caseID;
    caseSecurityCheckKey.type = 1;
    DataBasedSecurityResult dataBasedSecurityResult;
    if (0L != caseSecurityCheckKey.caseID) {
      dataBasedSecurityResult =
        dataBasedSecurity.checkCaseSecurity1(caseSecurityCheckKey);

      if (!dataBasedSecurityResult.result) {
        if (dataBasedSecurityResult.readOnly) {
          throw new AppException(
            GENERALCASE.ERR_CASESECURITY_CHECK_READONLY_RIGHTS);
        }
        if (dataBasedSecurityResult.restricted) {
          throw new AppException(GENERALCASE.ERR_CASESECURITY_CHECK_RIGHTS);
        }

        throw new AppException(
          GENERALCASE.ERR_CASESECURITY_CHECK_ACCESS_RIGHTS);
      }
    } else if (0L != details.dtls.concerningDtls.caseParticipantRoleID) {
      final ParticipantSecurityCheckKey participantKey =
        new ParticipantSecurityCheckKey();

      participantKey.participantID =
        details.dtls.concerningDtls.caseParticipantRoleID;
      participantKey.type = LOCATIONACCESSTYPE.MAINTAIN;

      dataBasedSecurityResult =
        dataBasedSecurity.checkParticipantSecurity(participantKey);

      if (!dataBasedSecurityResult.result) {

        throw new AppException(GENERALCONCERN.ERR_CONCERNROLE_FV_SENSITIVE);
      }
    }

    if (details.dtls.taskDtls.taskDefinitionID
      .equals("BDMMANUALPARTICIPANT")) {
      final ClientMerge clientMergeObj = ClientMergeFactory.newInstance();

      final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

      concernRoleKey.concernRoleID =
        details.dtls.concerningDtls.participantRoleID;

      final CuramInd curamInd =
        clientMergeObj.isConcernRoleDuplicate(concernRoleKey);

      if (curamInd.statusInd) {

        ValidationManagerFactory.getManager()
          .throwWithLookup(new AppException(
            BPOWORKALLOCATIONTASK.ERR_WORK_ALLOCATION_TASK_XRV_DUPLICATE_CLIENT_CREATE),
            "a", 0);

      }
    }

    final CreateManualTaskKey_eo createManualTaskKey_eo =
      new CreateManualTaskKey_eo();

    final List<Object> enactmentStructs = new ArrayList<>();

    final BDMVSGTaskCreateDetails taskCreateDetails =
      new BDMVSGTaskCreateDetails();

    taskCreateDetails.assignedTo = details.dtls.assignDtls.assignmentID;
    taskCreateDetails.caseID = details.dtls.concerningDtls.caseID;
    taskCreateDetails.participantRoleID =
      details.dtls.concerningDtls.participantRoleID;
    taskCreateDetails.subject = details.dtls.taskDtls.subject;
    taskCreateDetails.comments = details.dtls.taskDtls.comments;
    taskCreateDetails.deadlineDateTime = details.dtls.taskDtls.deadlineTime;
    taskCreateDetails.assigneeType = details.dtls.assignDtls.assignType;
    taskCreateDetails.reserveToMeInd = details.dtls.assignDtls.reserveToMeInd;
    taskCreateDetails.participantType =
      details.dtls.concerningDtls.participantType;
    taskCreateDetails.priority = details.dtls.taskDtls.priority;
    taskCreateDetails.bdmTaskClassificationID =
      details.dtls.taskDtls.bdmTaskClassificationID;
    taskCreateDetails.taskType = details.dtls.taskDtls.taskType;

    // Deadline date time is mandatory
    if (DateTime.kZeroDateTime.equals(taskCreateDetails.deadlineDateTime)) {
      throw new AppException(BPOTASKMANAGEMENT.ERR_TASK_FV_DEADLINE_EMPTY);
    }

    final DeadlineDuration deadlineDuration = new DeadlineDuration();

    final DateTimeInSecondsKey dateTimeInSecondsKey =
      new DateTimeInSecondsKey();

    dateTimeInSecondsKey.dateTime = taskCreateDetails.deadlineDateTime;

    final TaskManagementUtility taskManagementUtilityObj =
      TaskManagementUtilityFactory.newInstance();

    deadlineDuration.deadlineDuration = taskManagementUtilityObj
      .convertDateTimeToSeconds(dateTimeInSecondsKey).seconds;
    enactmentStructs.add(deadlineDuration);

    // this.validateCreate(taskCreateDetails);
    final TaskManagement taskManagement = TaskManagementFactory.newInstance();
    final TaskCreateDetails OOTBtaskCreateDetails = new TaskCreateDetails();
    OOTBtaskCreateDetails.assign(taskCreateDetails);
    taskManagement.validateCreateWithoutPriority(OOTBtaskCreateDetails);

    if (taskCreateDetails.reserveToMeInd) {
      taskCreateDetails.assignedTo = TransactionInfo.getProgramUser();
      taskCreateDetails.assigneeType = ASSIGNEETYPE.USER;
    }
    // Task 68842
    enactmentStructs.add(taskCreateDetails);
    // OOTBtaskCreateDetails.deadline
    enactmentStructs.add(OOTBtaskCreateDetails);

    String workflowDisabled =
      Configuration.getProperty(EnvVars.ENV_ENACT_WORKFLOW_PROCESS_DISABLED);

    if (workflowDisabled == null) {
      workflowDisabled = "NO";
    }

    long kProcessInstanceID = 0;

    if (workflowDisabled.equalsIgnoreCase(EnvVars.ENV_VALUE_NO)) {

      kProcessInstanceID = EnactmentService.startProcessInV3CompatibilityMode(
        details.dtls.taskDtls.taskDefinitionID, enactmentStructs);

    }

    long taskID = 0;
    // Get taskID using processInstanceID
    if (kProcessInstanceID != 0) {
      taskID = getTaskIDForProcessInstanceID(kProcessInstanceID);
    }

    if (taskID != 0) {
      // Create bizObjectAssociation for linking skillType with task
      if (taskCreateDetails.bdmTaskClassificationID != 0) {
        createBizObjectAssociationWithSkillType(taskID,
          taskCreateDetails.bdmTaskClassificationID);
      }

      if (taskCreateDetails.taskType.length() > 0) {
        addTaskType(taskID, taskCreateDetails.taskType);
      }
    }

    createManualTaskKey_eo.key.dtls.dtls.taskID = taskID;

    return createManualTaskKey_eo;

  }

  protected void addTaskType(final long taskID, final String taskType)
    throws AppException, InformationalException {

    final BDMTask bdmTask = BDMTaskFactory.newInstance();
    final BDMTaskKey key = new BDMTaskKey();
    key.taskID = taskID;
    final BDMTaskDtls newDtls = new BDMTaskDtls();
    newDtls.taskID = taskID;
    newDtls.type = taskType;
    bdmTask.insert(newDtls);

  }

  /**
   * Helper method to get taskID using processInstanceID
   *
   * @param processInstanceID
   * @return taskID
   * @throws AppException
   * @throws InformationalException
   */
  protected long getTaskIDForProcessInstanceID(final long processInstanceID)
    throws AppException, InformationalException {

    final ActivityInstance activityInstanceObj =
      ActivityInstanceFactory.newInstance();
    final ProcessInstanceKey instanceKey = new ProcessInstanceKey();
    long taskID = 0;

    instanceKey.processInstanceID = processInstanceID;

    final ActivityInstanceDtlsList instanceDtlsList =
      activityInstanceObj.searchByProcessInstanceID(instanceKey);

    for (int i = 0; i < instanceDtlsList.dtls.size(); i++) {
      if (instanceDtlsList.dtls.item(i).taskID != 0) {
        taskID = instanceDtlsList.dtls.item(i).taskID;
        if (taskID != 0) {
          break;
        }
      }
    }

    return taskID;
  }

  protected void createBizObjectAssociationWithSkillType(final long taskID,
    final long bdmTaskClassificationID)
    throws AppException, InformationalException {

    final BizObjAssociation bizObjAssociationObj =
      BizObjAssociationFactory.newInstance();

    final BizObjAssociationDtls bizObjAssociationDtls =
      new BizObjAssociationDtls();

    bizObjAssociationDtls.bizObjAssocID =
      bizObjAssociationObj.getNewPrimaryKey();
    bizObjAssociationDtls.bizObjectID = bdmTaskClassificationID;
    bizObjAssociationDtls.bizObjectType = BUSINESSOBJECTTYPE.TASKSKILLTYPE;
    bizObjAssociationDtls.taskID = taskID;
    bizObjAssociationObj.insert(bizObjAssociationDtls);

  }

  /**
   * Search skill type
   */
  @Override
  public BDMTaskSkillTypeList
    searchBDMTaskSkillType(final BDMTaskSkillTypeKey key)
      throws AppException, InformationalException {

    final BDMTaskSkillTypeList bdmTaskSkillTypeList =
      new BDMTaskSkillTypeList();

    final BDMTaskClassification bdmTaskSkillTypeObj =
      BDMTaskClassificationFactory.newInstance();
    final BDMTaskSkillKey bdmTaskSkillKey = new BDMTaskSkillKey();
    bdmTaskSkillKey.assign(key);
    BDMTaskClassificationDtlsList enBDMTaskSkillTypeDtlsList =
      new BDMTaskClassificationDtlsList();

    enBDMTaskSkillTypeDtlsList =
      bdmTaskSkillTypeObj.searchBySkillType(bdmTaskSkillKey);

    bdmTaskSkillTypeList.assign(enBDMTaskSkillTypeDtlsList);

    return bdmTaskSkillTypeList;

  }

  /**
   * Task-25356 Bring forward task
   */
  @Override
  public void bringForward(final BDMTaskBringForwardDtls bringForwardTaskDtls)
    throws AppException, InformationalException {

    // 1. set status = on hold
    final Task taskObj = TaskFactory.newInstance();
    final TaskKey taskKey = new TaskKey();
    final long taskID = bringForwardTaskDtls.taskID;
    taskKey.taskID = taskID;
    final TaskDtls taskDtls = taskObj.read(taskKey);

    // bring forward, only if it is either open or deferred task
    if (taskDtls.status.equals(TASKSTATUS.NOTSTARTED)
      || taskDtls.status.equals(TASKSTATUS.DEFERRED)) {

      // store bring-fwd date-time in restart time
      taskDtls.restartTime = bringForwardTaskDtls.bringForwardDateTime;

      // reset assigned date time
      taskDtls.assignedDateTime = DateTime.getCurrentDateTime();

      taskDtls.status = TASKSTATUS.ONHOLD;

      final TaskAdmin taskAdminObj = TaskAdminFactory.newInstance();
      taskAdminObj.modify(taskID, taskDtls.administrationSID,
        taskDtls.allowForwardInd, taskDtls.assignedDateTime,
        taskDtls.category, taskDtls.creationTime, taskDtls.deadTimeOverInd,
        taskDtls.overflowInd, taskDtls.priority, taskDtls.reservedBy,
        taskDtls.restartTime, taskDtls.status, taskDtls.totalTimeWorked,
        taskDtls.wdoSnapshot, taskDtls.versionNo);

      // Read work queue name
      final WorkQueue workQueueObj = WorkQueueFactory.newInstance();
      final WorkQueueKey readWorkQueueKey = new WorkQueueKey();
      readWorkQueueKey.workQueueID = bringForwardTaskDtls.bringForwardToID;
      final WorkQueueDtls readWorkQueueDetails =
        workQueueObj.read(readWorkQueueKey);

      // 2. assign to workqueue
      final DefaultWorkResolverAdapter workResolverAdapterObj =
        DefaultWorkResolverHelper.instantiateDefaultWorkResolverAdapter();
      final AllocationTargetList allocationTargets =
        new AllocationTargetList();
      final AllocationTargetDetails taskAllocationDetails =
        new AllocationTargetDetails();
      taskAllocationDetails.name =
        Long.toString(bringForwardTaskDtls.bringForwardToID);
      taskAllocationDetails.type = TARGETITEMTYPE.WORKQUEUE;
      allocationTargets.dtls.addRef(taskAllocationDetails);

      final TaskDetails taskDetails = new TaskDetails();
      taskDetails.taskID = taskID;
      workResolverAdapterObj.resolveWork(taskDetails, allocationTargets,
        true);

      // 3. update task history and comments
      final TaskHistoryAdmin taskHistoryAdminObj =
        TaskHistoryAdminFactory.newInstance();
      taskHistoryAdminObj.create(taskID, DateTime.getCurrentDateTime(),
        TASKCHANGETYPE.ONHOLD, bringForwardTaskDtls.comments,
        readWorkQueueDetails.name, taskDtls.reservedBy,
        TransactionInfo.getProgramUser());
    }
  }

  @Override
  public BDMTaskBringForwardDtls
    viewAssignmentForTask(final TaskManagementTaskKey taskKey)
      throws AppException, InformationalException {

    final TaskAssignment taskAssignmentObj =
      TaskAssignmentFactory.newInstance();
    final TaskIDRelatedIDAndTypeKey workQueueTaskKey =
      new TaskIDRelatedIDAndTypeKey();
    workQueueTaskKey.taskID = taskKey.taskID;
    workQueueTaskKey.assigneeType = TARGETITEMTYPE.WORKQUEUE;
    final TaskAssignmentNameDetailsList taskAssignmentNameDetailsList =
      taskAssignmentObj
        .searchWQAssignmentsWithNamesByTaskID(workQueueTaskKey);

    final BDMTaskBringForwardDtls result = new BDMTaskBringForwardDtls();
    if (!taskAssignmentNameDetailsList.dtls.isEmpty()) {
      result.bringForwardToID =
        taskAssignmentNameDetailsList.dtls.get(0).relatedID;
      result.workQueueName = taskAssignmentNameDetailsList.dtls.get(0).name;
    }
    result.bringForwardDateTime = TransactionInfo.getSystemDateTime();
    return result;
  }

  @Override
  public BDMWorkQueueID
    getWorkQueueIDBySkillType(final BDMTaskSkillTypeKey key)
      throws AppException, InformationalException {

    final BDMWorkQueueID bdmWorkQueueID = new BDMWorkQueueID();
    if (SKILLTYPE.VSG01.equals(key.skillType)) {
      bdmWorkQueueID.workQueueID = 80013; // BDM SP L1 Work Queue
    } else if (SKILLTYPE.VSG02.equals(key.skillType)
      || SKILLTYPE.VSG03.equals(key.skillType)
      || SKILLTYPE.VSG04.equals(key.skillType)) {
      bdmWorkQueueID.workQueueID = 80014; // BDM SP L1 Work Queue
    } else if (SKILLTYPE.VSG05.equals(key.skillType)
      || SKILLTYPE.VSG06.equals(key.skillType)
      || SKILLTYPE.VSG07.equals(key.skillType)) {
      bdmWorkQueueID.workQueueID = 80015; // BDM SP L2 Work Queue
    } else if (SKILLTYPE.VSG08.equals(key.skillType)
      || SKILLTYPE.VSG09.equals(key.skillType)) {
      bdmWorkQueueID.workQueueID = 80016; // app4
    } else if (SKILLTYPE.VSG10.equals(key.skillType)) {
      bdmWorkQueueID.workQueueID = 80017; // app5
    } else {
      // Default workQueue to BDM Core
      bdmWorkQueueID.workQueueID = 80013; // app1
    }

    return bdmWorkQueueID;
  }

  @Override
  public BDMTaskSkillTypeKey
    getSkillTypeByDocumentType(final BDMDocumentTypeKey key)
      throws AppException, InformationalException {

    final BDMTaskSkillTypeKey bdmTaskSkillTypeKey = new BDMTaskSkillTypeKey();

    if (DOCUMENTTYPE.PERSONAL_EVIDENCE.equals(key.documentType)
      || DOCUMENTTYPE.DIRECT_DEPOSIT.equals(key.documentType)) {
      bdmTaskSkillTypeKey.skillType = SKILLTYPE.VSG01;
    } else if (DOCUMENTTYPE.INCARCERATION_LETTER.equals(key.documentType)) {
      bdmTaskSkillTypeKey.skillType = SKILLTYPE.VSG02;
    } else if (DOCUMENTTYPE.INCOME_RELATED.equals(key.documentType)) {
      bdmTaskSkillTypeKey.skillType = SKILLTYPE.VSG03;
    } else if (DOCUMENTTYPE.DEPENDENT_INFORMATION.equals(key.documentType)) {
      bdmTaskSkillTypeKey.skillType = SKILLTYPE.VSG04;
    } else if (DOCUMENTTYPE.REQUEST_FOR_RECONSIDERATION
      .equals(key.documentType)) {
      bdmTaskSkillTypeKey.skillType = SKILLTYPE.VSG02;
    } else if (DOCUMENTTYPE.RFR_BIRTH_CERTIFICATE.equals(key.documentType)) {
      bdmTaskSkillTypeKey.skillType = SKILLTYPE.VSG02;
    } else if (DOCUMENTTYPE.RFR_CERTIFICATE_COMPLETION
      .equals(key.documentType)) {
      bdmTaskSkillTypeKey.skillType = SKILLTYPE.VSG02;
    } else if (DOCUMENTTYPE.RFR_CERTIFICATE_PARTICIPATION
      .equals(key.documentType)) {
      bdmTaskSkillTypeKey.skillType = SKILLTYPE.VSG02;
    } else if (DOCUMENTTYPE.RFR_DRIVING_LICENSE.equals(key.documentType)) {
      bdmTaskSkillTypeKey.skillType = SKILLTYPE.VSG02;
    } else if (DOCUMENTTYPE.RFR_INTERNAL_REVIEW.equals(key.documentType)) {
      bdmTaskSkillTypeKey.skillType = SKILLTYPE.VSG02;
    } else if (DOCUMENTTYPE.RFR_NOTICE_ASSESSMENT.equals(key.documentType)) {
      bdmTaskSkillTypeKey.skillType = SKILLTYPE.VSG02;
    } else if (DOCUMENTTYPE.RFR_PAYMENT_RECEIPT.equals(key.documentType)) {
      bdmTaskSkillTypeKey.skillType = SKILLTYPE.VSG02;
    } else if (DOCUMENTTYPE.RFR_PAYMENT_INVOICE.equals(key.documentType)) {
      bdmTaskSkillTypeKey.skillType = SKILLTYPE.VSG02;
    } else if (DOCUMENTTYPE.RFR_PASSPORT.equals(key.documentType)) {
      bdmTaskSkillTypeKey.skillType = SKILLTYPE.VSG02;
    } else if (DOCUMENTTYPE.RFR_OTHERS.equals(key.documentType)) {
      bdmTaskSkillTypeKey.skillType = SKILLTYPE.VSG02;
    } else {
      // default skill type for any other document type
      bdmTaskSkillTypeKey.skillType = SKILLTYPE.VSG01;
    }

    return bdmTaskSkillTypeKey;
  }

  @Override
  public BDMTaskSkillTypeKey
    getSkillTypeByEvidenceType(final BDMEvidenceTypeKey key)
      throws AppException, InformationalException {

    final BDMTaskSkillTypeKey bdmTaskSkillTypeKey = new BDMTaskSkillTypeKey();
    if (CASEEVIDENCE.BDMINCOME.equals(key.evidenceType)) {
      bdmTaskSkillTypeKey.skillType = SKILLTYPE.VSG03;
    } else if (CASEEVIDENCE.BDMDEPENDANT.equals(key.evidenceType)) {
      bdmTaskSkillTypeKey.skillType = SKILLTYPE.VSG04;
    } else if (CASEEVIDENCE.BDMINCARCERATION.equals(key.evidenceType)) {
      bdmTaskSkillTypeKey.skillType = SKILLTYPE.VSG02;
    } else if (CASEEVIDENCE.IDENTIFICATIONS.equals(key.evidenceType)
      || CASEEVIDENCE.BDMADDRESS.equals(key.evidenceType)) {
      bdmTaskSkillTypeKey.skillType = SKILLTYPE.VSG01;
    } else {
      // default skill type for any other evidence type
      bdmTaskSkillTypeKey.skillType = SKILLTYPE.VSG01;
    }
    return bdmTaskSkillTypeKey;
  }

  /**
   * Update the task type with the information supplied.
   *
   * @param details
   */
  @Override
  public void modifyTaskType(final BDMTaskCategoryAndTypeDetails details)
    throws AppException, InformationalException {

    final BDMTask bdmTask = BDMTaskFactory.newInstance();
    final NotFoundIndicator nfi = new NotFoundIndicator();
    final BDMTaskKey key = new BDMTaskKey();
    key.taskID = details.taskID;

    final BDMTaskDtls dtls = bdmTask.read(nfi, key);

    if (dtls == null) {
      final BDMTaskDtls newDtls = new BDMTaskDtls();
      newDtls.taskID = details.taskID;
      newDtls.type = details.taskType;
      newDtls.comments = details.comments;
      bdmTask.insert(newDtls);
    } else {
      dtls.type = details.taskType;
      dtls.comments = details.comments;
      bdmTask.modify(key, dtls);
    }
  }
}
