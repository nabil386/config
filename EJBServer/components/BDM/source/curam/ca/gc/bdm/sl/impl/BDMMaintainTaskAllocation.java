package curam.ca.gc.bdm.sl.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.entity.fact.BDMTaskClassificationFactory;
import curam.ca.gc.bdm.entity.intf.BDMTaskClassification;
import curam.ca.gc.bdm.sl.bdminbox.struct.CaseUrgentFlagStringDetails;
import curam.ca.gc.bdm.sl.struct.BDMAllocateTaskKey;
import curam.ca.gc.bdm.sl.struct.BDMBundledTaskDetails;
import curam.ca.gc.bdm.sl.struct.BDMBundledTaskList;
import curam.ca.gc.bdm.sl.struct.BDMSearchBundledTaskKey;
import curam.ca.gc.bdm.sl.struct.BDMSkillLevelTaskList;
import curam.ca.gc.bdm.sl.struct.BDMStage1TaskList;
import curam.ca.gc.bdm.sl.struct.BDMWorkQueueTaskKey;
import curam.core.sl.entity.struct.TaskKey;
import curam.core.sl.fact.TaskManagementFactory;
import curam.core.sl.intf.TaskManagement;
import curam.core.sl.struct.TaskDeadlineDetails;
import curam.core.sl.struct.TaskManagementTaskKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.DateTime;

public class BDMMaintainTaskAllocation
  extends curam.ca.gc.bdm.sl.base.BDMMaintainTaskAllocation {

  @Inject
  curam.ca.gc.bdm.sl.organization.supervisor.intf.BDMMaintainSupervisorUsers bdmMaintainSupervisorUsers;

  public BDMMaintainTaskAllocation() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Allocate task to user
   *
   * @param key BDMAllocateTaskKey
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void allocateTask(final BDMAllocateTaskKey key)
    throws AppException, InformationalException {

    // Read deadline details by taskID
    final TaskManagement taskManagementObj =
      TaskManagementFactory.newInstance();
    final TaskManagementTaskKey taskManagementTaskKey =
      new TaskManagementTaskKey();
    taskManagementTaskKey.taskID = key.taskID;
    TaskDeadlineDetails taskDeadlineDetails = new TaskDeadlineDetails();
    taskDeadlineDetails =
      taskManagementObj.readDeadlineDetails(taskManagementTaskKey);

    // Read case urgent (active & not end dated) flag by taskID
    final TaskKey taskKey = new TaskKey();
    CaseUrgentFlagStringDetails caseUrgentFlagStringDetails =
      new CaseUrgentFlagStringDetails();
    taskKey.taskID = key.taskID;
    caseUrgentFlagStringDetails =
      bdmMaintainSupervisorUsers.getCaseUrgentFlagsByTaskID(taskKey);

    // Tasks are associated with an case urgent flag or overdue
    if (caseUrgentFlagStringDetails.caseUrgentFlagStr.length() > 0
      || taskDeadlineDetails.deadline.before(DateTime.getCurrentDateTime())) {

      // Read skillType and language
      // final BDMTaskClassificationDtls bdmTaskSkillTypeDtls =
      // readSkillTypeByTaskID(taskKey);
    }
  }

  /**
   * Return tasks list for stage 1 processing
   */
  @Override
  public BDMStage1TaskList getStage1UrgentTasks(final BDMWorkQueueTaskKey key)
    throws AppException, InformationalException {

    final BDMStage1TaskList bdmStage1TaskList = new BDMStage1TaskList();

    curam.ca.gc.bdm.entity.struct.BDMStage1TaskDetailsList enBDMStage1TaskDetailsList =
      new curam.ca.gc.bdm.entity.struct.BDMStage1TaskDetailsList();
    final curam.ca.gc.bdm.entity.struct.BDMWorkQueueTaskKey enBDMWorkQueueTaskKey =
      new curam.ca.gc.bdm.entity.struct.BDMWorkQueueTaskKey();
    final BDMTaskClassification bdmTaskClassificationObj =
      BDMTaskClassificationFactory.newInstance();

    enBDMWorkQueueTaskKey.commaSeperatedWorkQueueIDs =
      key.commaSeperatedWorkQueueIDs;

    enBDMStage1TaskDetailsList =
      bdmTaskClassificationObj.getStage1UrgentTasks(enBDMWorkQueueTaskKey);

    bdmStage1TaskList.assign(enBDMStage1TaskDetailsList);

    return bdmStage1TaskList;

  }

  /**
   * This method will return list of bundled task for Stgae 1 task batch
   * processing.
   * Task 28409 2022-06-03
   */
  @Override
  public BDMBundledTaskList
    getBundledTaskList(final BDMSearchBundledTaskKey key)
      throws AppException, InformationalException {

    final BDMBundledTaskList bdmBundledTaskList = new BDMBundledTaskList();
    BDMBundledTaskDetails bdmBundledTaskDetails = new BDMBundledTaskDetails();
    curam.ca.gc.bdm.entity.struct.BDMBundledTaskDetailsList enBDMBundledTaskDetailsList =
      new curam.ca.gc.bdm.entity.struct.BDMBundledTaskDetailsList();
    final curam.ca.gc.bdm.entity.struct.BDMSearchBundledTaskKey enBDMSearchBundledTaskKey =
      new curam.ca.gc.bdm.entity.struct.BDMSearchBundledTaskKey();
    final BDMTaskClassification bdmTaskClassificationObj =
      BDMTaskClassificationFactory.newInstance();

    enBDMSearchBundledTaskKey.caseID = key.caseID;
    enBDMSearchBundledTaskKey.taskID = key.taskID;
    enBDMSearchBundledTaskKey.skillType = key.skillType;
    enBDMSearchBundledTaskKey.userName = key.userName;
    enBDMSearchBundledTaskKey.commaSeperatedExcludeSkillTypes =
      key.commaSeperatedExcludeSkillTypes;
    enBDMSearchBundledTaskKey.commaSeperatedWorkQueueIDs =
      key.commaSeperatedWorkQueueIDs;

    enBDMBundledTaskDetailsList =
      bdmTaskClassificationObj.getBundledTaskList(enBDMSearchBundledTaskKey);

    curam.ca.gc.bdm.entity.struct.BDMBundledTaskDetails enBDMBundledTaskDetails =
      new curam.ca.gc.bdm.entity.struct.BDMBundledTaskDetails();

    for (int i = 0; i < enBDMBundledTaskDetailsList.dtls.size(); i++) {

      enBDMBundledTaskDetails =
        new curam.ca.gc.bdm.entity.struct.BDMBundledTaskDetails();
      enBDMBundledTaskDetails = enBDMBundledTaskDetailsList.dtls.get(i);

      bdmBundledTaskDetails = new BDMBundledTaskDetails();
      bdmBundledTaskDetails.assign(enBDMBundledTaskDetails);

      bdmBundledTaskList.bundleTaskDtls.add(bdmBundledTaskDetails);

    }

    return bdmBundledTaskList;
  }

  /**
   * Return tasks list for PTM flow that will be processed by batch job
   */
  @Override
  public BDMSkillLevelTaskList getPTMTasks(final BDMWorkQueueTaskKey key)
    throws AppException, InformationalException {

    final BDMSkillLevelTaskList bdmSkillLevelTaskList =
      new BDMSkillLevelTaskList();

    curam.ca.gc.bdm.entity.struct.BDMSkillLevelTaskDetailsList enBDMSkillLevelTaskDetailsList =
      new curam.ca.gc.bdm.entity.struct.BDMSkillLevelTaskDetailsList();
    final curam.ca.gc.bdm.entity.struct.BDMWorkQueueTaskKey enBDMWorkQueueTaskKey =
      new curam.ca.gc.bdm.entity.struct.BDMWorkQueueTaskKey();
    final BDMTaskClassification bdmTaskClassificationObj =
      BDMTaskClassificationFactory.newInstance();

    enBDMWorkQueueTaskKey.commaSeperatedWorkQueueIDs =
      key.commaSeperatedWorkQueueIDs;

    enBDMSkillLevelTaskDetailsList =
      bdmTaskClassificationObj.getPTMTasks(enBDMWorkQueueTaskKey);

    bdmSkillLevelTaskList.assign(enBDMSkillLevelTaskDetailsList);

    return bdmSkillLevelTaskList;
  }

  /**
   * Return tasks list for Stage2 flow
   */
  @Override
  public BDMSkillLevelTaskList getStage2Tasks(final BDMWorkQueueTaskKey key)
    throws AppException, InformationalException {

    final BDMSkillLevelTaskList bdmSkillLevelTaskList =
      new BDMSkillLevelTaskList();

    curam.ca.gc.bdm.entity.struct.BDMSkillLevelTaskDetailsList enBDMSkillLevelTaskDetailsList =
      new curam.ca.gc.bdm.entity.struct.BDMSkillLevelTaskDetailsList();
    final curam.ca.gc.bdm.entity.struct.BDMWorkQueueTaskKey enBDMWorkQueueTaskKey =
      new curam.ca.gc.bdm.entity.struct.BDMWorkQueueTaskKey();
    final BDMTaskClassification bdmTaskClassificationObj =
      BDMTaskClassificationFactory.newInstance();

    enBDMWorkQueueTaskKey.commaSeperatedWorkQueueIDs =
      key.commaSeperatedWorkQueueIDs;

    enBDMSkillLevelTaskDetailsList =
      bdmTaskClassificationObj.getStage2Tasks(enBDMWorkQueueTaskKey);

    bdmSkillLevelTaskList.assign(enBDMSkillLevelTaskDetailsList);

    return bdmSkillLevelTaskList;

  }

}
