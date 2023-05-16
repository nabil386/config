package curam.ca.gc.bdm.facade.bdmworkallocation.impl;

import curam.ca.gc.bdm.entity.fact.BDMTaskFactory;
import curam.ca.gc.bdm.entity.intf.BDMTask;
import curam.ca.gc.bdm.entity.struct.BDMTaskDtls;
import curam.ca.gc.bdm.entity.struct.BDMTaskKey;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMStandardManualTaskDtls;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskBringForwardDetails;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskCategoryAndTypeDetails;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskSkillTypeKey;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskSkillTypeList;
import curam.ca.gc.bdm.message.BDMWORKALLOCATION;
import curam.ca.gc.bdm.sl.bdmworkallocation.fact.BDMWorkAllocationTaskFactory;
import curam.ca.gc.bdm.sl.bdmworkallocation.intf.BDMWorkAllocationTask;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.codetable.CASETYPECODE;
import curam.codetable.TASKSTATUS;
import curam.core.facade.struct.ReadTaskSummaryDetailsKey;
import curam.core.fact.CachedCaseParticipantRoleFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.fact.UsersFactory;
import curam.core.impl.EnvVars;
import curam.core.intf.CachedCaseParticipantRole;
import curam.core.intf.CaseHeader;
import curam.core.intf.ConcernRole;
import curam.core.sl.entity.fact.CaseParticipantRoleFactory;
import curam.core.sl.entity.intf.CaseParticipantRole;
import curam.core.sl.entity.struct.CaseIDAndParticipantRoleIDDetails;
import curam.core.sl.entity.struct.CaseParticipantConcernRoleDetails;
import curam.core.sl.entity.struct.CaseParticipantRoleKey;
import curam.core.sl.struct.CreateManualTaskKey_eo;
import curam.core.sl.struct.TaskManagementTaskKey;
import curam.core.struct.CaseKey;
import curam.core.struct.CaseTypeCode;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.ConcernRoleTypeDetails;
import curam.core.struct.UserRoleDetails;
import curam.core.struct.UsersKey;
import curam.message.BPOTASKDEFINITION;
import curam.serviceplans.facade.fact.ServicePlanDeliveryFactory;
import curam.serviceplans.facade.intf.ServicePlanDelivery;
import curam.serviceplans.facade.struct.ServicePlanSecurityKey;
import curam.serviceplans.sl.impl.ServicePlanSecurityImplementationFactory;
import curam.util.exception.AppException;
import curam.util.exception.AppRuntimeException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.transaction.TransactionInfo;
import curam.util.type.DateTime;
import curam.util.type.NotFoundIndicator;
import curam.util.workflow.fact.BizObjAssociationFactory;
import curam.util.workflow.fact.TaskFactory;
import curam.util.workflow.intf.BizObjAssociation;
import curam.util.workflow.intf.Task;
import curam.util.workflow.struct.BizObjAssociationDtls;
import curam.util.workflow.struct.BizObjAssociationDtlsList;
import curam.util.workflow.struct.TaskDtls;
import curam.util.workflow.struct.TaskKey;

public class BDMWorkAllocation
  extends curam.ca.gc.bdm.facade.bdmworkallocation.base.BDMWorkAllocation {

  /**
   * Task 12746 - Create task with skill type
   */
  @Override
  public ReadTaskSummaryDetailsKey
    createTaskWithVSG(final BDMStandardManualTaskDtls dtls)
      throws AppException, InformationalException {

    ReadTaskSummaryDetailsKey readTaskSummaryDetailsKey =
      new ReadTaskSummaryDetailsKey();
    final BDMWorkAllocationTask bdmWorkAllocationTaskObj =
      BDMWorkAllocationTaskFactory.newInstance();
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = dtls.dtls.concerningDtls.caseID;
    if (caseKey.caseID != 0L) {
      final CaseTypeCode caseTypeCode =
        caseHeaderObj.readCaseTypeCode(caseKey);
      if (caseTypeCode.caseTypeCode.equals(CASETYPECODE.SERVICEPLAN)) {
        final ServicePlanDelivery servicePlanDeliveryObj =
          ServicePlanDeliveryFactory.newInstance();
        final ServicePlanSecurityKey servicePlanSecurityKey =
          new ServicePlanSecurityKey();
        ServicePlanSecurityImplementationFactory.register();
        servicePlanSecurityKey.caseID = dtls.dtls.concerningDtls.caseID;
        servicePlanSecurityKey.securityCheckType = 3;
        servicePlanDeliveryObj.checkSecurity(servicePlanSecurityKey);
      }
    }

    String processDefinitionName = "";

    if (dtls.dtls.concerningDtls.participantRoleID == 0L
      && dtls.dtls.concerningDtls.caseID == 0L
      && dtls.dtls.concerningDtls.caseParticipantRoleID == 0L) {
      processDefinitionName = "BDMMANUAL";

    } else if (dtls.dtls.concerningDtls.participantRoleID != 0L
      && dtls.dtls.concerningDtls.caseID == 0L
      && dtls.dtls.concerningDtls.caseParticipantRoleID == 0L) {
      processDefinitionName = "BDMMANUALPARTICIPANT";
      dtls.dtls.concerningDtls.caseParticipantRoleID =
        dtls.dtls.concerningDtls.participantRoleID;

    } else if (dtls.dtls.concerningDtls.participantRoleID == 0L
      && dtls.dtls.concerningDtls.caseParticipantRoleID == 0L
      && dtls.dtls.concerningDtls.caseID != 0L) {
      processDefinitionName = "BDMMANUALCASE";

    } else {
      CachedCaseParticipantRole cachedCaseParticipantRole_eoObj;
      CaseParticipantRoleKey caseParticipantRole_eoKey;
      CaseIDAndParticipantRoleIDDetails caseIDAndParticipantRoleIDDetails;
      if (dtls.dtls.concerningDtls.participantRoleID == 0L
        && dtls.dtls.concerningDtls.caseParticipantRoleID != 0L
        && dtls.dtls.concerningDtls.caseID != 0L) {
        cachedCaseParticipantRole_eoObj =
          CachedCaseParticipantRoleFactory.newInstance();
        caseParticipantRole_eoKey = new CaseParticipantRoleKey();
        caseParticipantRole_eoKey.caseParticipantRoleID =
          dtls.dtls.concerningDtls.caseParticipantRoleID;
        caseIDAndParticipantRoleIDDetails = cachedCaseParticipantRole_eoObj
          .readCaseIDAndParticipantRoleIDDetails(caseParticipantRole_eoKey);
        dtls.dtls.concerningDtls.participantRoleID =
          caseIDAndParticipantRoleIDDetails.participantRoleID;
        processDefinitionName = "BDMMANUALPARTICIPANTCASE";

      } else if (dtls.dtls.concerningDtls.participantRoleID != 0L
        && dtls.dtls.concerningDtls.caseParticipantRoleID == 0L
        && dtls.dtls.concerningDtls.caseID != 0L) {
        processDefinitionName = "BDMMANUALPARTICIPANTCASE";
        // dtls.dtls.concerningDtls.caseParticipantRoleID =
        // dtls.dtls.concerningDtls.participantRoleID;

      } else if (dtls.dtls.concerningDtls.participantRoleID == 0L
        && dtls.dtls.concerningDtls.caseParticipantRoleID != 0L
        && dtls.dtls.concerningDtls.caseID == 0L) {
        cachedCaseParticipantRole_eoObj =
          CachedCaseParticipantRoleFactory.newInstance();
        caseParticipantRole_eoKey = new CaseParticipantRoleKey();
        caseParticipantRole_eoKey.caseParticipantRoleID =
          dtls.dtls.concerningDtls.caseParticipantRoleID;
        caseIDAndParticipantRoleIDDetails = cachedCaseParticipantRole_eoObj
          .readCaseIDAndParticipantRoleIDDetails(caseParticipantRole_eoKey);
        dtls.dtls.concerningDtls.caseID =
          caseIDAndParticipantRoleIDDetails.caseID;
        dtls.dtls.concerningDtls.participantRoleID =
          caseIDAndParticipantRoleIDDetails.participantRoleID;
        processDefinitionName = "BDMMANUALPARTICIPANTCASE";

      }
    }

    dtls.dtls.taskDtls.taskDefinitionID = processDefinitionName;
    if ("BDMMANUALPARTICIPANTCASE".equals(processDefinitionName)) {
      // Task 68842
      if (dtls.dtls.concerningDtls.caseParticipantRoleID != 0) {
        final CaseParticipantRoleKey caseParticipantRoleKey =
          new CaseParticipantRoleKey();
        final CaseParticipantRole caseParticipantRoleObj =
          CaseParticipantRoleFactory.newInstance();
        caseParticipantRoleKey.caseParticipantRoleID =
          dtls.dtls.concerningDtls.caseParticipantRoleID;
        final CaseParticipantConcernRoleDetails caseParticipantConcernRoleDetails =
          caseParticipantRoleObj
            .readParticipantRoleDetails(caseParticipantRoleKey);
        dtls.dtls.concerningDtls.participantType =
          caseParticipantConcernRoleDetails.concernRoleType;
      } else if (dtls.dtls.concerningDtls.participantRoleID != 0) {
        final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
        final ConcernRole concernRoleObj = ConcernRoleFactory.newInstance();
        concernRoleKey.concernRoleID =
          dtls.dtls.concerningDtls.participantRoleID;
        final ConcernRoleTypeDetails concernRoleTypeDtls =
          concernRoleObj.readConcernRoleType(concernRoleKey);
        dtls.dtls.concerningDtls.participantType =
          concernRoleTypeDtls.concernRoleType;
      }

    } else if ("BDMMANUALPARTICIPANT".equals(processDefinitionName)) {
      final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
      final ConcernRole concernRoleObj = ConcernRoleFactory.newInstance();
      concernRoleKey.concernRoleID =
        dtls.dtls.concerningDtls.participantRoleID;
      final ConcernRoleTypeDetails concernRoleTypeDtls =
        concernRoleObj.readConcernRoleType(concernRoleKey);
      dtls.dtls.concerningDtls.participantType =
        concernRoleTypeDtls.concernRoleType;
    }

    try {
      CreateManualTaskKey_eo createManualTaskKey_eo =
        new CreateManualTaskKey_eo();
      createManualTaskKey_eo =
        bdmWorkAllocationTaskObj.createTaskWithVSG(dtls);
      readTaskSummaryDetailsKey = createManualTaskKey_eo.key;
      return readTaskSummaryDetailsKey;
    } catch (final AppException var13) {
      if (var13.getNestedThrowable() instanceof AppRuntimeException) {
        throw new AppException(
          BPOTASKDEFINITION.ERR_TASK_XFV_DEFINITION_TYPE_NOT_EXIST, var13);
      } else {
        throw var13;
      }
    }
  }

  /**
   * Task 12746 - Search skill type for task
   */
  @Override
  public BDMTaskSkillTypeList
    searchBDMTaskSkillType(final BDMTaskSkillTypeKey key)
      throws AppException, InformationalException {

    BDMTaskSkillTypeList bdmTaskSkillTypeList = new BDMTaskSkillTypeList();
    final BDMWorkAllocationTask bdmWorkAllocationTaskObj =
      BDMWorkAllocationTaskFactory.newInstance();

    bdmTaskSkillTypeList =
      bdmWorkAllocationTaskObj.searchBDMTaskSkillType(key);

    return bdmTaskSkillTypeList;
  }

  /**
   * This method brings forward a task.
   *
   * @param bringFwdTaskDetails
   *
   * Task-25356
   */
  @Override
  public void
    bringForward(final BDMTaskBringForwardDetails bringFwdTaskDetails)
      throws AppException, InformationalException {

    // validate
    validateBringForward(bringFwdTaskDetails);

    // bring forward task
    final BDMWorkAllocationTask bdmWorkAllocationTaskObj =
      BDMWorkAllocationTaskFactory.newInstance();
    bdmWorkAllocationTaskObj.bringForward(bringFwdTaskDetails.dtls);

  }

  /**
   * @param bringFwdTaskDetails
   * @throws AppException
   * @throws InformationalException
   */
  private void
    validateBringForward(final BDMTaskBringForwardDetails bringFwdTaskDetails)
      throws AppException, InformationalException {

    final Task taskObj = TaskFactory.newInstance();
    final TaskKey taskKey = new TaskKey();
    taskKey.taskID = bringFwdTaskDetails.dtls.taskID;
    final TaskDtls taskDtls = taskObj.read(taskKey);

    // bring forward, only if it is either open or deferred task
    if (taskDtls.status.equals(TASKSTATUS.CLOSED)) {
      throw new AppException(BDMWORKALLOCATION.ERR_TASK_BRINGFWD_CLOSED);
    }

    // find skill type association
    final BizObjAssociation bizObjAssociationObj =
      BizObjAssociationFactory.newInstance();
    boolean skillTypeAssociatedInd = false;
    final BizObjAssociationDtlsList bizObjList =
      bizObjAssociationObj.searchByTaskID(taskKey);
    for (final BizObjAssociationDtls bizObj : bizObjList.dtls) {
      if (bizObj.bizObjectType.equals(BUSINESSOBJECTTYPE.TASKSKILLTYPE)) {
        skillTypeAssociatedInd = true;
        break;
      }
    }
    // if no skill type is associated with the task
    if (!skillTypeAssociatedInd) {
      throw new AppException(
        BDMWORKALLOCATION.ERR_TASK_BRINGFWD_NO_SKILLTYPE);
    }

    // if date is not in future
    if (!bringFwdTaskDetails.dtls.bringForwardDateTime
      .after(DateTime.getCurrentDateTime())) {
      throw new AppException(
        BDMWORKALLOCATION.ERR_TASK_BRINGFWD_NOT_FUTURE_DATE);
    }

    // Task 63065 Fix Bring Forward task post batch allocation to user
    // Business rule - Work Queue must be selected for allocation in order to
    // move task from On Hold to Open.
    // Error message - Bring Forward tasks must be assigned to an allocation
    // work queue.
    if (bringFwdTaskDetails.dtls.bringForwardToID == 0) {
      throw new AppException(
        BDMWORKALLOCATION.ERR_TASK_BRINGFWD_NOT_IN_ALLOC_WORKQ);
    } else {
      boolean validTaskForwrdQueueSelected = false;
      final String allocationWQCommaSepList =
        Configuration.getProperty(EnvVars.BDM_ENV_ALLOCATION_WORKQUEUE_LIST);

      final String[] allocationWQList = allocationWQCommaSepList.split(",");
      for (int i = 0; i < allocationWQList.length; i++) {
        if (bringFwdTaskDetails.dtls.bringForwardToID == Long
          .parseLong(allocationWQList[i])) {
          validTaskForwrdQueueSelected = true;
          break;
        }
      }
      if (!validTaskForwrdQueueSelected) {
        throw new AppException(
          BDMWORKALLOCATION.ERR_TASK_BRINGFWD_NOT_IN_ALLOC_WORKQ);
      }
    }

    /*
     * final TaskAssignment taskAssignmentObj =
     * TaskAssignmentFactory.newInstance();
     * final TaskIDRelatedIDAndTypeKey workQueueTaskKey =
     * new TaskIDRelatedIDAndTypeKey();
     * workQueueTaskKey.taskID = taskKey.taskID;
     * workQueueTaskKey.assigneeType = TARGETITEMTYPE.WORKQUEUE;
     * final TaskAssignmentNameDetailsList taskAssignmentNameDetailsList =
     * taskAssignmentObj
     * .searchWQAssignmentsWithNamesByTaskID(workQueueTaskKey);
     * // if task does not belong to allocation work queue
     * if (taskAssignmentNameDetailsList.dtls.isEmpty()) {
     * throw new AppException(
     * BDMWORKALLOCATION.ERR_TASK_BRINGFWD_NOT_IN_ALLOC_WORKQ);
     * } else {
     * final String allocationWQList =
     * Configuration.getProperty(EnvVars.BDM_ENV_ALLOCATION_WORKQUEUE_LIST);
     * final String workqueueID =
     * Long.toString(taskAssignmentNameDetailsList.dtls.get(0).relatedID);
     *
     * if (!StringUtil.delimitedText2StringList(allocationWQList, ',')
     * .contains(workqueueID)) {
     * throw new AppException(
     * BDMWORKALLOCATION.ERR_TASK_BRINGFWD_NOT_IN_ALLOC_WORKQ);
     * }
     * }
     */

    final String user = TransactionInfo.getProgramUser();
    final UsersKey usersKey = new UsersKey();
    usersKey.userName = user;
    final UserRoleDetails userRoleDetails =
      UsersFactory.newInstance().readUserRole(usersKey);
    final boolean isSupervisor =
      userRoleDetails.roleName.contains("SUPERVISOR");

    // user is non supervisor and task not reserved by that user
    if (!isSupervisor && !taskDtls.reservedBy.equals(user)) {
      throw new AppException(
        BDMWORKALLOCATION.ERR_TASK_BRINGFWD_NOT_IN_MYTASKS);
    }

    // user is suoervisor and task is not reserved by any user
    if (isSupervisor && taskDtls.reservedBy.isEmpty()) {
      throw new AppException(
        BDMWORKALLOCATION.ERR_TASK_BRINGFWD_NOT_RESERVED);
    }
  }

  @Override
  public BDMTaskBringForwardDetails
    viewAssignmentForTask(final TaskManagementTaskKey taskKey)
      throws AppException, InformationalException {

    final BDMWorkAllocationTask bdmWorkAllocationTaskObj =
      BDMWorkAllocationTaskFactory.newInstance();

    final BDMTaskBringForwardDetails taskBringForwardDetails =
      new BDMTaskBringForwardDetails();
    taskBringForwardDetails.dtls
      .assign(bdmWorkAllocationTaskObj.viewAssignmentForTask(taskKey));
    return taskBringForwardDetails;
  }

  /**
   * Retrieve task type information from data store.
   *
   * @param taskKey
   */
  @Override
  public BDMTaskCategoryAndTypeDetails
    readTaskType(final TaskManagementTaskKey taskKey)
      throws AppException, InformationalException {

    final BDMTaskCategoryAndTypeDetails details =
      new BDMTaskCategoryAndTypeDetails();

    final BDMTask bdmTask = BDMTaskFactory.newInstance();
    final NotFoundIndicator nfi = new NotFoundIndicator();
    final BDMTaskKey key = new BDMTaskKey();
    key.taskID = taskKey.taskID;
    final BDMTaskDtls dtls = bdmTask.read(nfi, key);

    if (dtls != null) {
      details.assign(dtls);
    }

    return details;
  }

  /**
   * Update the task type information.
   *
   * @param details
   */
  @Override
  public void modifyTaskType(final BDMTaskCategoryAndTypeDetails details)
    throws AppException, InformationalException {

    final BDMWorkAllocationTask bdmWorkAllocationTaskObj =
      BDMWorkAllocationTaskFactory.newInstance();
    bdmWorkAllocationTaskObj.modifyTaskType(details);
  }

}
