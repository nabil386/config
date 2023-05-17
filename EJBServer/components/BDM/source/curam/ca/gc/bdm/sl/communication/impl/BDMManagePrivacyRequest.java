package curam.ca.gc.bdm.sl.communication.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.entity.fact.BDMTaskFactory;
import curam.ca.gc.bdm.entity.fact.BDMWorkQueueCountryLinkFactory;
import curam.ca.gc.bdm.entity.fec.fact.BDMFECaseFactory;
import curam.ca.gc.bdm.entity.fec.struct.BDMConcernRoleCommRMKey;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseDtls;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseKey;
import curam.ca.gc.bdm.entity.fec.struct.BDMRecordCommunicationsTaskDetails;
import curam.ca.gc.bdm.entity.fec.struct.BDMRecordCommunicationsTaskDetailsList;
import curam.ca.gc.bdm.entity.intf.BDMWorkQueueCountryLink;
import curam.ca.gc.bdm.entity.struct.BDMWorkQueueCountryLinkKey;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.rest.bdmuacaseworkerverificationtaskmanagerapi.struct.BDMUACaseWorkerVerificationEnactKey;
import curam.ca.gc.bdm.sl.fec.struct.BDMFECTaskCreateDetails;
import curam.ca.gc.bdm.entity.intf.BDMTask;
import curam.ca.gc.bdm.entity.intf.BDMWorkQueueCountryLink;
import curam.ca.gc.bdm.entity.struct.BDMTaskDtls;
import curam.ca.gc.bdm.entity.struct.BDMTaskKey;
import curam.ca.gc.bdm.entity.struct.BDMWorkQueueCountryLinkDtls;
import curam.ca.gc.bdm.entity.struct.BDMWorkQueueCountryLinkKey;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.rest.bdmuacaseworkerverificationtaskmanagerapi.struct.BDMUACaseWorkerVerificationEnactKey;
import curam.ca.gc.bdm.sl.communication.fact.BDMCommunicationFactory;
import curam.ca.gc.bdm.sl.fec.struct.BDMFECTaskCreateDetails;
import curam.codetable.BDMTASKCATEGORY;
import curam.codetable.BDMTASKTYPE;
import curam.codetable.CASECATEGORY;
import curam.codetable.CASESTATUS;
import curam.codetable.COMMUNICATIONDIRECTION;
import curam.codetable.COMMUNICATIONTYPE;
import curam.codetable.CORRESPONDENT;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.codetable.PRODUCTCATEGORY;
import curam.codetable.TASKCHANGETYPE;
import curam.codetable.TASKPRIORITY;
import curam.core.facade.fact.TaskManagementFactory;
import curam.core.facade.struct.ConcernRoleIDStatusCodeKey;
import curam.core.facade.struct.TaskDeadlineDetails;
import curam.core.facade.struct.TaskManagementTaskKey;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.ConcernRoleCommunicationFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.struct.CaseHeaderDtlsList;
import curam.core.sl.struct.CommunicationIDKey;
import curam.core.sl.struct.RecordedCommDetails1;
import curam.core.sl.struct.RecordedCommKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.ConcernRoleCommunicationKey;
import curam.core.struct.ConcernRoleKey;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataAttributeDtls;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataAttributeDtlsList;
import curam.pdc.impl.PDCConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.events.TASK;
import curam.pdc.impl.PDCConst;
import curam.util.events.impl.EventService;
import curam.util.events.struct.Event;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.internal.workflow.fact.ActivityInstanceFactory;
import curam.util.internal.workflow.intf.ActivityInstance;
import curam.util.internal.workflow.struct.ActivityInstanceDtlsList;
import curam.util.internal.workflow.struct.ProcessInstanceKey;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.NotFoundIndicator;
import curam.util.workflow.fact.TaskFactory;
import curam.util.workflow.fact.TaskHistoryAdminFactory;
import curam.util.workflow.fact.WorkflowDeadlineAdminFactory;
import curam.util.workflow.impl.EnactmentService;
import curam.util.workflow.intf.Task;
import curam.util.workflow.intf.TaskHistoryAdmin;
import curam.util.workflow.intf.WorkflowDeadlineAdmin;
import curam.util.workflow.struct.TaskKey;
import curam.util.workflow.struct.WorkflowDeadlineInfo;
import curam.wizard.util.impl.CodetableUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Utility Class for manage privacy request
 *
 * @author karthikeyan.rangasamy
 *
 */
public class BDMManagePrivacyRequest
  extends curam.ca.gc.bdm.sl.communication.base.BDMManagePrivacyRequest {

  private final BDMUtil bdmUtil = new BDMUtil();

  public BDMManagePrivacyRequest() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Method to process tasks for the Manage Privacy Request
   *
   * @param concernRoleCommunicationKey
   * @param recordedCommDetails
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void processTaskForPrivacyRequest(
    final CommunicationIDKey communicationIDKey,
    final RecordedCommDetails1 recordedCommDetails)
    throws AppException, InformationalException {

    final BDMConcernRoleCommRMKey bdmCncrCmmRmKey =
      new BDMConcernRoleCommRMKey();

    if (isPrivacyRequestTaskNeeded(recordedCommDetails)) {

      // Process task for person communication
      if (recordedCommDetails.clientParticipantRoleID != CuramConst.kLongZero
        && recordedCommDetails.caseID == CuramConst.kLongZero) {

        bdmCncrCmmRmKey.communicationDate =
          recordedCommDetails.communicationDate;
        bdmCncrCmmRmKey.concernRoleID =
          recordedCommDetails.clientParticipantRoleID;
        bdmCncrCmmRmKey.communicationTypeCode =
          recordedCommDetails.communicationTypeCode;
        bdmCncrCmmRmKey.communicationID = communicationIDKey.communicationID;

        processPrivacyRequestNotificationTaskForPerson(bdmCncrCmmRmKey);

      } else if (recordedCommDetails.caseID != CuramConst.kLongZero) {

        // Process Task for case communication
        bdmCncrCmmRmKey.communicationDate =
          recordedCommDetails.communicationDate;
        bdmCncrCmmRmKey.communicationID = communicationIDKey.communicationID;

        final NotFoundIndicator nfIndicator = new NotFoundIndicator();

        final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
        caseHeaderKey.caseID = recordedCommDetails.caseID;
        final CaseHeaderDtls caseHeaderDtls =
          CaseHeaderFactory.newInstance().read(nfIndicator, caseHeaderKey);

        bdmCncrCmmRmKey.caseID = recordedCommDetails.caseID;
        bdmCncrCmmRmKey.concernRoleID = caseHeaderDtls.concernRoleID;
        bdmCncrCmmRmKey.communicationTypeCode =
          recordedCommDetails.communicationTypeCode;

        processPrivacyRequestNotificationTaskForIC(bdmCncrCmmRmKey);

      }

    }

    closeCommunicationTask(communicationIDKey, recordedCommDetails);

  }

  /**
   * Method to process Manage Privacy Request tasks notification for Person
   *
   * @param bdmCncrCmmRmKey
   * @throws AppException
   * @throws InformationalException
   */
  private void processPrivacyRequestNotificationTaskForPerson(
    final BDMConcernRoleCommRMKey bdmCncrCmmRmKey)
    throws AppException, InformationalException {

    final Task taskObj = TaskFactory.newInstance();

    final Long existingTaskID =
      BDMUtil.getExistingTaskIDForPerson(bdmCncrCmmRmKey);

    // If there is no existing privacy request tasks then create new task else
    // update deadline if needed
    if (existingTaskID == CuramConst.gkZero) {
      createPersonPrivacyRequestTask(bdmCncrCmmRmKey);
    } else if (existingTaskID != CuramConst.gkZero) {

      final TaskKey taskKey = new TaskKey();
      taskKey.taskID = existingTaskID;

      final NotFoundIndicator nfIndicator = new NotFoundIndicator();

      taskObj.read(nfIndicator, taskKey);

      if (!nfIndicator.isNotFound()) {

        final TaskManagementTaskKey taskManagementTaskKey =
          new TaskManagementTaskKey();
        taskManagementTaskKey.taskKey.taskID = existingTaskID;
        final TaskDeadlineDetails taskDeadlineDetails = TaskManagementFactory
          .newInstance().readDeadlineDetails(taskManagementTaskKey);

        final DateTime cmmDtTmInput =
          new DateTime(bdmCncrCmmRmKey.communicationDate.asLong());

        final DateTime newDeadLineDay = cmmDtTmInput.addTime(Integer
          .parseInt(Configuration
            .getProperty(EnvVars.BDM_RECORD_MANAGE_PRIVACY_REQUEST_DEADLINE))
          * BDMConstants.kBdm24Hours, 0, 0);

        taskDeadlineDetails.deadlineDetails.deadline = newDeadLineDay;
        taskDeadlineDetails.deadlineDetails.taskID = existingTaskID;
        taskDeadlineDetails.deadlineDetails.versionNo =
          taskDeadlineDetails.deadlineDetails.versionNo;
        modifyDeadline(taskDeadlineDetails.deadlineDetails.taskID,
          taskDeadlineDetails.deadlineDetails.deadline, CuramConst.gkEmpty,
          taskDeadlineDetails.deadlineDetails.versionNo);

      }
    }
  }

  /**
   * Method to create Privacy Request Task for Person
   *
   * @param bdmCncrCmmRmKey
   * @throws AppException
   * @throws InformationalException
   */
  private void createPersonPrivacyRequestTask(
    final BDMConcernRoleCommRMKey bdmCncrCmmRmKey)
    throws AppException, InformationalException {

    final List<Object> enactmentStructs = new ArrayList<>();

    final RecordedCommKey recordedCommKey =
      new curam.core.sl.struct.RecordedCommKey();
    recordedCommKey.communicationID = bdmCncrCmmRmKey.communicationID;

    final RecordedCommDetails1 recordedCommDetails =
      BDMCommunicationFactory.newInstance().readRecordedComm(recordedCommKey);

    final String taskSubject = new String();

    // Add Task enactment struct
    final BDMFECTaskCreateDetails bfmFECTaskCrtDetails =
      new BDMFECTaskCreateDetails();

    boolean subjectLineCreatedForPriReqTask_Ind = false;
    boolean subjectLineCreatedForInterimAppReqTask_Ind = false;

    if (isPrivacyRequestTaskNeeded(recordedCommDetails)) {
      bfmFECTaskCrtDetails.communicationDate =
        bdmCncrCmmRmKey.communicationDate;
      bfmFECTaskCrtDetails.oralWrittenLanguage =
        getOralWrittenLanguageForTask(bdmCncrCmmRmKey).toString();
      // Start - Fix for Bug 114911
      bfmFECTaskCrtDetails.commTypCdDesc =
        CodetableUtil.getNonCachedCodetableItem(
          COMMUNICATIONTYPE.TABLENAME, bdmCncrCmmRmKey.communicationTypeCode,
          BDMConstants.gkLocaleLowerFR).description
          + " / "
          + CodetableUtil.getNonCachedCodetableItem(
            COMMUNICATIONTYPE.TABLENAME,
            bdmCncrCmmRmKey.communicationTypeCode,
            BDMConstants.gkLocaleLowerEN).description;
      // End - Fix for Bug 114911
      bfmFECTaskCrtDetails.strConcernRoleFullName =
        getConcernRoleName(bdmCncrCmmRmKey.concernRoleID);
      bfmFECTaskCrtDetails.strIdentificationRefernce =
        getPersonIdentificationDetail(bdmCncrCmmRmKey);

      subjectLineCreatedForPriReqTask_Ind = true;

    } else if (isInterimApplicationTaskNeeded(recordedCommDetails)) {
      bfmFECTaskCrtDetails.communicationDate =
        bdmCncrCmmRmKey.communicationDate;

      // Start - Firx for Bug 114911
      bfmFECTaskCrtDetails.commTypCdDesc =
        CodetableUtil.getNonCachedCodetableItem(
          COMMUNICATIONTYPE.TABLENAME, bdmCncrCmmRmKey.communicationTypeCode,
          BDMConstants.gkLocaleLowerFR).description
          + " / "
          + CodetableUtil.getNonCachedCodetableItem(
            COMMUNICATIONTYPE.TABLENAME,
            bdmCncrCmmRmKey.communicationTypeCode,
            BDMConstants.gkLocaleLowerEN).description;
      // End - Firx for Bug 114911

      bfmFECTaskCrtDetails.oralWrittenLanguage =
        getOralWrittenLanguageForTask(bdmCncrCmmRmKey).toString();
      bfmFECTaskCrtDetails.strConcernRoleFullName =
        getConcernRoleName(bdmCncrCmmRmKey.concernRoleID);
      bfmFECTaskCrtDetails.strIdentificationRefernce =
        getPersonIdentificationDetail(bdmCncrCmmRmKey);

      subjectLineCreatedForInterimAppReqTask_Ind = true;

    }

    final Long workQueueID =
      getWorkQueueIDForPerson(bdmCncrCmmRmKey.concernRoleID);

    bfmFECTaskCrtDetails.concernRoleID = bdmCncrCmmRmKey.concernRoleID;
    bfmFECTaskCrtDetails.communicationID = bdmCncrCmmRmKey.communicationID;
    bfmFECTaskCrtDetails.priority = TASKPRIORITY.NORMAL;
    bfmFECTaskCrtDetails.communicationTypeCode =
      bdmCncrCmmRmKey.communicationTypeCode;
    bfmFECTaskCrtDetails.workQueueID = workQueueID;
    bfmFECTaskCrtDetails.subject = taskSubject;

    final ConcernRoleCommunicationKey concernRoleCommunicationKey =
      new ConcernRoleCommunicationKey();
    concernRoleCommunicationKey.communicationID =
      bdmCncrCmmRmKey.communicationID;
    bfmFECTaskCrtDetails.correspondentName = ConcernRoleCommunicationFactory
      .newInstance().read(concernRoleCommunicationKey).correspondentName;

    enactmentStructs.add(bfmFECTaskCrtDetails);

    // Set Task deadline
    final long currentDateTimeInMills = Date.getCurrentDate().asLong();

    final int taskDeadlineDays = Integer.parseInt(Configuration
      .getProperty(EnvVars.BDM_RECORD_MANAGE_PRIVACY_REQUEST_DEADLINE));

    final long inputDateTimeInMills =
      bdmCncrCmmRmKey.communicationDate.addDays(taskDeadlineDays).asLong();
    final long durationMills = inputDateTimeInMills - currentDateTimeInMills;
    final long durationSeconds = durationMills / DateTime.kMilliSecsInSecond;

    final BDMUACaseWorkerVerificationEnactKey bdmuaCaseWorkerVerificationEnactKey =
      new BDMUACaseWorkerVerificationEnactKey();
    bdmuaCaseWorkerVerificationEnactKey.deadLineDurationInSec =
      (int) durationSeconds;
    enactmentStructs.add(bdmuaCaseWorkerVerificationEnactKey);

    if (subjectLineCreatedForPriReqTask_Ind) {

      final long kProcessInstanceID =
        EnactmentService.startProcessInV3CompatibilityMode(
          BDMConstants.kBDMPersonPrivacyRequestNotificationTask,
          enactmentStructs);

      addBDMTask(kProcessInstanceID,
        recordedCommDetails.communicationTypeCode);

    }

    if (subjectLineCreatedForInterimAppReqTask_Ind) {

      final long kProcessInstanceID =
        EnactmentService.startProcessInV3CompatibilityMode(
          BDMConstants.kBDMPersonCommTyInterimAppReqTask, enactmentStructs);

      addBDMTask(kProcessInstanceID,
        recordedCommDetails.communicationTypeCode);

    }

  }

  private void addBDMTask(final long kProcessInstanceID,
    final String communicationTypeCode)
    throws AppException, InformationalException {

    if (kProcessInstanceID != 0) {

      final long taskID = getTaskIDForProcessInstanceID(kProcessInstanceID);

      if (taskID != 0) {
        final curam.ca.gc.bdm.entity.intf.BDMTask bdmTask =
          BDMTaskFactory.newInstance();
        final BDMTaskKey key = new BDMTaskKey();
        key.taskID = taskID;
        final BDMTaskDtls newDtls = new BDMTaskDtls();
        newDtls.taskID = taskID;

        if (communicationTypeCode
          .equals(COMMUNICATIONTYPE.PRIVACY_REQUEST_TIER_1)) {
          newDtls.category = BDMTASKCATEGORY.BDMIOAPPLICATIONFB;
          newDtls.type = BDMTASKTYPE.BDMFBPRIVACYREQUESTTIER01;
        } else if (communicationTypeCode
          .equals(COMMUNICATIONTYPE.PRIVACY_REQUEST_TIER_2)) {
          newDtls.category = BDMTASKCATEGORY.BDMIOAPPLICATIONFB;
          newDtls.type = BDMTASKTYPE.BDMFBTIER02FROMATIPGROUP;
        } else if (communicationTypeCode
          .equals(COMMUNICATIONTYPE.INTERIM_APP_REQUEST)) {
          newDtls.category = BDMTASKCATEGORY.BDMIOAPPLICATIONFB;
          newDtls.type = BDMTASKTYPE.FB_INTERIM_APP;
        }

        bdmTask.insert(newDtls);
      }
    }
  }

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

  /**
   * Method to process Manage Privacy Request task notification for Integrated
   * Case
   *
   * @param bdmCncrCmmRmKey
   * @throws AppException
   * @throws InformationalException
   */
  private void processPrivacyRequestNotificationTaskForIC(
    final BDMConcernRoleCommRMKey bdmCncrCmmRmKey)
    throws AppException, InformationalException {

    final Task taskObj = TaskFactory.newInstance();

    final Long existingTaskID =
      BDMUtil.getExistingTaskIDForCase(bdmCncrCmmRmKey);

    // If there is no task for privacy request then create new task else update
    // the existing task deadline
    if (existingTaskID == CuramConst.gkZero) {
      createICPrivacyRequestTask(bdmCncrCmmRmKey);
    } else if (existingTaskID != CuramConst.gkZero) {

      final TaskKey taskKey = new TaskKey();
      taskKey.taskID = existingTaskID;

      final NotFoundIndicator nfIndicator = new NotFoundIndicator();

      taskObj.read(nfIndicator, taskKey);

      if (!nfIndicator.isNotFound()) {

        final TaskManagementTaskKey taskManagementTaskKey =
          new TaskManagementTaskKey();
        taskManagementTaskKey.taskKey.taskID = existingTaskID;

        final TaskDeadlineDetails taskDeadlineDetails = TaskManagementFactory
          .newInstance().readDeadlineDetails(taskManagementTaskKey);

        final DateTime cmmDtTmInput =
          new DateTime(bdmCncrCmmRmKey.communicationDate.asLong());

        final DateTime newDeadLineDay = cmmDtTmInput.addTime(Integer
          .parseInt(Configuration
            .getProperty(EnvVars.BDM_RECORD_MANAGE_PRIVACY_REQUEST_DEADLINE))
          * BDMConstants.kBdm24Hours, 0, 0);

        taskDeadlineDetails.deadlineDetails.deadline = newDeadLineDay;
        taskDeadlineDetails.deadlineDetails.taskID = existingTaskID;
        taskDeadlineDetails.deadlineDetails.versionNo =
          taskDeadlineDetails.deadlineDetails.versionNo;

        modifyDeadline(taskDeadlineDetails.deadlineDetails.taskID,
          taskDeadlineDetails.deadlineDetails.deadline, CuramConst.gkEmpty,
          taskDeadlineDetails.deadlineDetails.versionNo);
      }
    }
  }

  /**
   * Method to create Privacy Request Tasks for Integrated Case
   *
   * @param bdmCncrCmmRmKey
   * @throws AppException
   * @throws InformationalException
   */
  private void
    createICPrivacyRequestTask(final BDMConcernRoleCommRMKey bdmCncrCmmRmKey)
      throws AppException, InformationalException {

    if (bdmCncrCmmRmKey.caseID != CuramConst.kLongZero) {

      final List<Object> enactmentStructs = new ArrayList<>();

      final NotFoundIndicator caseHeaderNotFoundIndicator =
        new NotFoundIndicator();
      final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
      caseHeaderKey.caseID = bdmCncrCmmRmKey.caseID;
      final CaseHeaderDtls caseHeaderDtls = CaseHeaderFactory.newInstance()
        .read(caseHeaderNotFoundIndicator, caseHeaderKey);

      if (!caseHeaderNotFoundIndicator.isNotFound()) {

        // START: Task 96969: DEV: Curam Task 16 Implementation
        final RecordedCommKey recordedCommKey =
          new curam.core.sl.struct.RecordedCommKey();
        recordedCommKey.communicationID = bdmCncrCmmRmKey.communicationID;

        final RecordedCommDetails1 recordedCommDetails =
          BDMCommunicationFactory.newInstance()
            .readRecordedComm(recordedCommKey);

        final String fecTypeDesc = CodetableUtil.getCodetableDescription(
          PRODUCTCATEGORY.TABLENAME, caseHeaderDtls.integratedCaseType);

        final BDMFECTaskCreateDetails bfmFECTaskCrtDetails =
          new BDMFECTaskCreateDetails();

        final String taskSubject = new String();

        boolean subjectLineCreatedForPrivacyReqTask_Ind = false;
        boolean subjectLineCreatedForInterimAppReqTask_Ind = false;

        if (isPrivacyRequestTaskNeeded(recordedCommDetails)) {

          bfmFECTaskCrtDetails.communicationDate =
            bdmCncrCmmRmKey.communicationDate;

          bfmFECTaskCrtDetails.oralWrittenLanguage =
            getOralWrittenLanguageForTask(bdmCncrCmmRmKey).toString();

          // Start - Fix for Bug 114911
          bfmFECTaskCrtDetails.commTypCdDesc = CodetableUtil
            .getNonCachedCodetableItem(COMMUNICATIONTYPE.TABLENAME,
              bdmCncrCmmRmKey.communicationTypeCode,
              BDMConstants.gkLocaleLowerFR).description
            + " / "
            + CodetableUtil.getNonCachedCodetableItem(
              COMMUNICATIONTYPE.TABLENAME,
              bdmCncrCmmRmKey.communicationTypeCode,
              BDMConstants.gkLocaleLowerEN).description;
          // End - Fix for Bug 114911

          bfmFECTaskCrtDetails.strConcernRoleFullName =
            getConcernRoleName(bdmCncrCmmRmKey.concernRoleID);

          bfmFECTaskCrtDetails.fecTypeDesc = fecTypeDesc;

          bfmFECTaskCrtDetails.strIdentificationRefernce =
            getPersonIdentificationDetail(bdmCncrCmmRmKey);

          bfmFECTaskCrtDetails.feCaseReference = caseHeaderDtls.caseReference;

          subjectLineCreatedForPrivacyReqTask_Ind = true;

        } else if (isInterimApplicationTaskNeeded(recordedCommDetails)) {

          bfmFECTaskCrtDetails.communicationDate =
            bdmCncrCmmRmKey.communicationDate;

          bfmFECTaskCrtDetails.oralWrittenLanguage =
            getOralWrittenLanguageForTask(bdmCncrCmmRmKey).toString();

          // Start - Fix for Bug 114911
          bfmFECTaskCrtDetails.commTypCdDesc = CodetableUtil
            .getNonCachedCodetableItem(COMMUNICATIONTYPE.TABLENAME,
              bdmCncrCmmRmKey.communicationTypeCode,
              BDMConstants.gkLocaleLowerFR).description
            + " / "
            + CodetableUtil.getNonCachedCodetableItem(
              COMMUNICATIONTYPE.TABLENAME,
              bdmCncrCmmRmKey.communicationTypeCode,
              BDMConstants.gkLocaleLowerEN).description;
          // End - Fix for Bug 114911

          bfmFECTaskCrtDetails.strConcernRoleFullName =
            getConcernRoleName(bdmCncrCmmRmKey.concernRoleID);

          bfmFECTaskCrtDetails.strIdentificationRefernce =
            getPersonIdentificationDetail(bdmCncrCmmRmKey);

          bfmFECTaskCrtDetails.fecTypeDesc = fecTypeDesc;

          bfmFECTaskCrtDetails.feCaseReference = caseHeaderDtls.caseReference;

          subjectLineCreatedForInterimAppReqTask_Ind = true;

        }
        // END: Task 96969: DEV: Curam Task 16 Implementation
        // Set Task enactment Struct

        bfmFECTaskCrtDetails.concernRoleID = bdmCncrCmmRmKey.concernRoleID;
        bfmFECTaskCrtDetails.caseID = bdmCncrCmmRmKey.caseID;
        bfmFECTaskCrtDetails.communicationID =
          bdmCncrCmmRmKey.communicationID;
        bfmFECTaskCrtDetails.priority = TASKPRIORITY.NORMAL;
        bfmFECTaskCrtDetails.communicationTypeCode =
          bdmCncrCmmRmKey.communicationTypeCode;
        bfmFECTaskCrtDetails.workQueueID =
          getWorkQueueIDForICCase(caseHeaderDtls);

        bfmFECTaskCrtDetails.subject = taskSubject;

        final ConcernRoleCommunicationKey concernRoleCommunicationKey =
          new ConcernRoleCommunicationKey();
        concernRoleCommunicationKey.communicationID =
          bdmCncrCmmRmKey.communicationID;
        bfmFECTaskCrtDetails.correspondentName =
          ConcernRoleCommunicationFactory.newInstance()
            .read(concernRoleCommunicationKey).correspondentName;

        enactmentStructs.add(bfmFECTaskCrtDetails);

        // Set Task Deadline
        final long currentDateTimeInMills = Date.getCurrentDate().asLong();

        final int taskDeadlineDays = Integer.parseInt(Configuration
          .getProperty(EnvVars.BDM_RECORD_MANAGE_PRIVACY_REQUEST_DEADLINE));
        final long inputDateTimeInMills = bdmCncrCmmRmKey.communicationDate
          .addDays(taskDeadlineDays).asLong();
        final long durationMills =
          inputDateTimeInMills - currentDateTimeInMills;
        final long durationSeconds =
          durationMills / DateTime.kMilliSecsInSecond;

        final BDMUACaseWorkerVerificationEnactKey bdmuaCaseWorkerVerificationEnactKey =
          new BDMUACaseWorkerVerificationEnactKey();
        bdmuaCaseWorkerVerificationEnactKey.deadLineDurationInSec =
          (int) durationSeconds;
        enactmentStructs.add(bdmuaCaseWorkerVerificationEnactKey);

        if (subjectLineCreatedForPrivacyReqTask_Ind) {

          final long kProcessInstanceID =
            EnactmentService.startProcessInV3CompatibilityMode(
              BDMConstants.kBDMIntegratedCasePrivacyRequestNotificationTask,
              enactmentStructs);

          addBDMTask(kProcessInstanceID,
            recordedCommDetails.communicationTypeCode);

        }

        if (subjectLineCreatedForInterimAppReqTask_Ind) {

          final long kProcessInstanceID =
            EnactmentService.startProcessInV3CompatibilityMode(
              BDMConstants.kBDMIntegratedCaseCommTypeInterimRequestNotificationTask,
              enactmentStructs);

          addBDMTask(kProcessInstanceID,
            recordedCommDetails.communicationTypeCode);

        }

      }
    }
  }

  /**
   * Method to get the Name of the person for Task Subject
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private String getConcernRoleName(final long concernRoleID)
    throws AppException, InformationalException {

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = concernRoleID;

    final String strConcernRoleFullName =
      ConcernRoleFactory.newInstance().read(concernRoleKey).concernRoleName;
    return strConcernRoleFullName;
  }

  /**
   * Modify the task deadline
   *
   * @param taskID
   * @param deadline
   * @param comments
   * @param versionNo
   *
   * @throws AppException
   * @throws InformationalException
   */
  private void modifyDeadline(final long taskID, final DateTime deadline,
    final String comments, final int versionNo)
    throws AppException, InformationalException {

    final String userName = TransactionInfo.getProgramUser();

    final WorkflowDeadlineAdmin workflowDeadlineAdminObj =
      WorkflowDeadlineAdminFactory.newInstance();
    final TaskHistoryAdmin taskHistoryAdminObj =
      TaskHistoryAdminFactory.newInstance();

    final WorkflowDeadlineInfo workflowDeadlineDtls =
      workflowDeadlineAdminObj.readDeadlineDetailsByTaskID(taskID);

    final DateTime originalDeadline = workflowDeadlineDtls.deadlineTime;

    if (!originalDeadline.equals(deadline)) {

      workflowDeadlineAdminObj.modifyDeadline(taskID, deadline,
        workflowDeadlineDtls.versionNo);

      taskHistoryAdminObj.create(taskID, DateTime.getCurrentDateTime(),
        TASKCHANGETYPE.DEADLINECHANGED, comments,
        Long.toString(deadline.asLong()),
        originalDeadline.equals(DateTime.kZeroDateTime) ? CuramConst.gkEmpty
          : Long.toString(originalDeadline.asLong()),
        userName);
    }

  }

  /**
   * Method to check the privacy request task processing is required or not
   *
   * @param recordedCommDetails
   * @return
   */
  private Boolean isPrivacyRequestTaskNeeded(
    final RecordedCommDetails1 recordedCommDetails) {

    final Boolean isPrivacyRequestTier1TaskNeeded =
      recordedCommDetails.communicationDirection
        .equals(COMMUNICATIONDIRECTION.INCOMING)
        && recordedCommDetails.communicationTypeCode
          .equals(COMMUNICATIONTYPE.PRIVACY_REQUEST_TIER_1)
        && !recordedCommDetails.correspondentType
          .equals(CORRESPONDENT.ATIPGROUP);

    final Boolean isPrivacyRequestTier2TaskNeeded =
      recordedCommDetails.communicationDirection
        .equals(COMMUNICATIONDIRECTION.INCOMING)
        && recordedCommDetails.communicationTypeCode
          .equals(COMMUNICATIONTYPE.PRIVACY_REQUEST_TIER_2)
        && recordedCommDetails.correspondentType
          .equals(CORRESPONDENT.ATIPGROUP);

    return isPrivacyRequestTier1TaskNeeded || isPrivacyRequestTier2TaskNeeded;
  }

  // START: Task 96969: DEV: Curam Task 16 Implementation
  /**
   * Method to check the Interim Application - Request task is required or not
   *
   * @param recordedCommDetails
   * @return
   */
  private Boolean isInterimApplicationTaskNeeded(
    final RecordedCommDetails1 recordedCommDetails) {

    return recordedCommDetails.communicationDirection
      .equals(COMMUNICATIONDIRECTION.INCOMING)
      && recordedCommDetails.communicationTypeCode
        .equals(COMMUNICATIONTYPE.INTERIM_APP_REQUEST);

  }

  /**
   * Method to check the Interim Application - Request task is required or not
   *
   * @param recordedCommDetails
   * @return
   */
  private Boolean isInterimApplicationTaskNeededToClose(
    final RecordedCommDetails1 recordedCommDetails) {

    return recordedCommDetails.communicationDirection
      .equals(COMMUNICATIONDIRECTION.OUTGOING)
      && recordedCommDetails.communicationTypeCode
        .equals(COMMUNICATIONTYPE.INTERIM_APP_OUTGOING);

  }

  // END: Task 96969: DEV: Curam Task 16 Implementation
  /**
   * Method to get the Oral Written Communication details for task subject
   *
   * @param bdmConcernRoleCommRMKey
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private StringBuffer getOralWrittenLanguageForTask(
    final BDMConcernRoleCommRMKey bdmConcernRoleCommRMKey)
    throws AppException, InformationalException {

    final curam.ca.gc.bdm.entity.fec.intf.BDMFECase bdmfeCase =
      BDMFECaseFactory.newInstance();

    String orallanuage = CuramConst.gkEmpty;
    String writtenlanuage = CuramConst.gkEmpty;
    StringBuffer oralWrittenLanguage =
      new StringBuffer(BDMConstants.gkLocaleUpperEN);

    DynamicEvidenceDataAttributeDtlsList dynamicEvidenceDataAttributeDtlsList =
      new DynamicEvidenceDataAttributeDtlsList();

    final EvidenceDescriptorDtls evidenceDescriptorDtlsKey =
      new EvidenceDescriptorDtls();
    evidenceDescriptorDtlsKey.participantID =
      bdmConcernRoleCommRMKey.concernRoleID;
    evidenceDescriptorDtlsKey.evidenceType = PDCConst.PDCCONTACTPREFERENCES;
    evidenceDescriptorDtlsKey.statusCode = EVIDENCEDESCRIPTORSTATUS.ACTIVE;

    dynamicEvidenceDataAttributeDtlsList =
      bdmfeCase.getContactPreferencesEvidenceList(evidenceDescriptorDtlsKey);

    DynamicEvidenceDataAttributeDtls dynamicEvidenceDataAttributeDtls = null;

    final int listSize = dynamicEvidenceDataAttributeDtlsList.dtls.size();

    if (listSize > 0) {

      for (int i = 0; i < listSize; i++) {
        dynamicEvidenceDataAttributeDtls =
          new DynamicEvidenceDataAttributeDtls();

        dynamicEvidenceDataAttributeDtls =
          dynamicEvidenceDataAttributeDtlsList.dtls.item(i);

        if (dynamicEvidenceDataAttributeDtls.name
          .equals(BDMConstants.kpreferredOralLanguage)) {

          if (BDMLANGUAGE.ENGLISHL
            .equals(dynamicEvidenceDataAttributeDtls.value)) {
            orallanuage = BDMConstants.gkLocaleUpperEN;
          } else if (BDMLANGUAGE.FRENCHL
            .equals(dynamicEvidenceDataAttributeDtls.value)) {
            orallanuage = BDMConstants.gkLocaleFR;
          }

        } else if (dynamicEvidenceDataAttributeDtls.name
          .equals(BDMConstants.kpreferredWrittenLanguage)) {

          if (BDMLANGUAGE.ENGLISHL
            .equals(dynamicEvidenceDataAttributeDtls.value)) {
            writtenlanuage = BDMConstants.gkLocaleUpperEN;
          } else if (BDMLANGUAGE.FRENCHL
            .equals(dynamicEvidenceDataAttributeDtls.value)) {
            writtenlanuage = BDMConstants.gkLocaleFR;
          }

        }

        if (!StringUtil.isNullOrEmpty(orallanuage)
          && !StringUtil.isNullOrEmpty(writtenlanuage)
          && orallanuage.equals(writtenlanuage)) {
          oralWrittenLanguage = new StringBuffer();
          oralWrittenLanguage.append(orallanuage);
        } else if (!StringUtil.isNullOrEmpty(orallanuage)
          && !StringUtil.isNullOrEmpty(writtenlanuage)
          && !orallanuage.equals(writtenlanuage)) {
          oralWrittenLanguage = new StringBuffer();
          oralWrittenLanguage.append(BDMConstants.gkBIL);

        } else {
          oralWrittenLanguage.append(BDMConstants.gkLocaleUpperEN);
        }

      }

    }
    return oralWrittenLanguage;
  }

  /**
   * Method to get the Person SIN/Identification Details
   *
   * @param bdmCncrCmmRmKey
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private String getPersonIdentificationDetail(
    final BDMConcernRoleCommRMKey bdmCncrCmmRmKey)
    throws AppException, InformationalException {

    String strIdentificationRefernce = CuramConst.gkEmpty;

    final String personSINNumber =
      bdmUtil.getSINNumberForPerson(bdmCncrCmmRmKey.concernRoleID);

    if (!StringUtil.isNullOrEmpty(personSINNumber)) {
      strIdentificationRefernce = personSINNumber;
    } else {
      strIdentificationRefernce =
        bdmUtil.getPersonReferenceNumber(bdmCncrCmmRmKey.concernRoleID);

    }
    return strIdentificationRefernce;
  }

  /**
   * Method to get the WorkQueueID for the person privacy request task
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private Long getWorkQueueIDForPerson(final Long concernRoleID)
    throws AppException, InformationalException {

    Long workQueueID = CuramConst.kLongZero;

    final curam.ca.gc.bdm.entity.fec.intf.BDMFECase bdmfeCase =
      BDMFECaseFactory.newInstance();
    final curam.ca.gc.bdm.entity.intf.BDMWorkQueueCountryLink bdmWorkQueueCountryLink =
      BDMWorkQueueCountryLinkFactory.newInstance();
    final curam.core.facade.intf.CaseHeader caseHeaderFacade =
      curam.core.facade.fact.CaseHeaderFactory.newInstance();

    final ConcernRoleIDStatusCodeKey concernRoleIDStatusCodeKey =
      new ConcernRoleIDStatusCodeKey();
    concernRoleIDStatusCodeKey.dtls.concernRoleID = concernRoleID;
    concernRoleIDStatusCodeKey.dtls.statusCode = CASESTATUS.OPEN;

    final CaseHeaderDtlsList caseHeaderDtlsList =
      caseHeaderFacade.searchByConcernRoleID(concernRoleIDStatusCodeKey);

    for (int i = 0; i < caseHeaderDtlsList.dtlsList.dtls.size(); i++) {

      final CaseHeaderDtls caseHeaderDtls =
        caseHeaderDtlsList.dtlsList.dtls.item(i);

      if (caseHeaderDtls.caseTypeCode.equals(CASECATEGORY.INTEGRATEDCASE)) {

        final BDMFECaseKey bdmfeCaseKey = new BDMFECaseKey();
        bdmfeCaseKey.caseID = caseHeaderDtls.caseID;
        final NotFoundIndicator bdmfeCaseNotFoundIndicator =
          new NotFoundIndicator();

        final BDMFECaseDtls feCaseDtls =
          bdmfeCase.read(bdmfeCaseNotFoundIndicator, bdmfeCaseKey);

        final NotFoundIndicator bdmWorkQueueCountryLinkNotFoundIndicator =
          new NotFoundIndicator();

        if (!bdmfeCaseNotFoundIndicator.isNotFound()) {

          final BDMWorkQueueCountryLinkKey bdmWorkQueueCountryLinkKey =
            new BDMWorkQueueCountryLinkKey();
          bdmWorkQueueCountryLinkKey.countryCode = feCaseDtls.countryCode;

          final BDMWorkQueueCountryLinkDtls bdmWorkQueueCountryLinkDtls =
            bdmWorkQueueCountryLink.read(
              bdmWorkQueueCountryLinkNotFoundIndicator,
              bdmWorkQueueCountryLinkKey);

          if (!bdmWorkQueueCountryLinkNotFoundIndicator.isNotFound()) {
            workQueueID = bdmWorkQueueCountryLinkDtls.workQueueID;
          }

          break;
        }
      }
    }

    if (workQueueID == CuramConst.kLongZero) {

      final String currentLoggedInUser = TransactionInfo.getProgramUser();
      workQueueID =
        bdmUtil.getWorkQueueIDByProvOfLoggedInUser(currentLoggedInUser);

    }

    return workQueueID;
  }

  /**
   * Method to get the WorkQueueID for the IC privacy request task
   *
   * @param caseHeaderDtls
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private Long getWorkQueueIDForICCase(final CaseHeaderDtls caseHeaderDtls)
    throws AppException, InformationalException {

    Long workQueueID = CuramConst.kLongZero;
    final boolean fecCaseFoundInd = false;

    final curam.ca.gc.bdm.entity.fec.intf.BDMFECase bdmfeCase =
      BDMFECaseFactory.newInstance();
    final curam.ca.gc.bdm.entity.intf.BDMWorkQueueCountryLink bdmWorkQueueCountryLink =
      BDMWorkQueueCountryLinkFactory.newInstance();

    BDMWorkQueueCountryLinkDtls workQueueCountryLinkDtls =
      new BDMWorkQueueCountryLinkDtls();
    final NotFoundIndicator bdmWorkQueueCountryLinkNotFoundIndicator =
      new NotFoundIndicator();
    if (caseHeaderDtls.caseTypeCode.equals(CASECATEGORY.INTEGRATEDCASE)) {

      final BDMFECaseKey bdmfeCaseKey = new BDMFECaseKey();
      bdmfeCaseKey.caseID = caseHeaderDtls.caseID;

      final NotFoundIndicator bdmFECaseNotFoundIndicator =
        new NotFoundIndicator();

      final BDMFECaseDtls feCaseDtls =
        bdmfeCase.read(bdmFECaseNotFoundIndicator, bdmfeCaseKey);

      if (!bdmFECaseNotFoundIndicator.isNotFound()) {

        final BDMWorkQueueCountryLinkKey bdmWorkQueueCountryLinkKey =
          new BDMWorkQueueCountryLinkKey();
        bdmWorkQueueCountryLinkKey.countryCode = feCaseDtls.countryCode;

        workQueueCountryLinkDtls = bdmWorkQueueCountryLink.read(
          bdmWorkQueueCountryLinkNotFoundIndicator,
          bdmWorkQueueCountryLinkKey);

      }

    }

    if (!bdmWorkQueueCountryLinkNotFoundIndicator.isNotFound()) {

      workQueueID = workQueueCountryLinkDtls.workQueueID;
    }

    if (workQueueID == CuramConst.kLongZero && !fecCaseFoundInd) {

      final String currentLoggedInUser = TransactionInfo.getProgramUser();
      workQueueID =
        bdmUtil.getWorkQueueIDByProvOfLoggedInUser(currentLoggedInUser);

    }

    return workQueueID;
  }

  // START: Task 96969: DEV: Curam Task 16 Implementation
  private void closeCommunicationTask(
    final CommunicationIDKey communicationIDKey,
    final RecordedCommDetails1 recordedCommDetails)
    throws AppException, InformationalException {

    final BDMConcernRoleCommRMKey bdmCncrCmmRmKey =
      new BDMConcernRoleCommRMKey();

    if (isInterimApplicationTaskNeededToClose(recordedCommDetails)) {

      bdmCncrCmmRmKey.communicationTypeCode =
        COMMUNICATIONTYPE.INTERIM_APP_REQUEST;

      if (recordedCommDetails.clientParticipantRoleID != CuramConst.kLongZero
        && recordedCommDetails.caseID == CuramConst.kLongZero) {
        bdmCncrCmmRmKey.concernRoleID =
          recordedCommDetails.clientParticipantRoleID;

        final Long existingTaskID =
          BDMUtil.getExistingTaskIDForPerson(bdmCncrCmmRmKey);

        if (existingTaskID != CuramConst.gkZero) {
          final Event closeTaskEvent = new Event();
          closeTaskEvent.eventKey.eventClass = TASK.CLOSED.eventClass;
          closeTaskEvent.eventKey.eventType = TASK.CLOSED.eventType;
          closeTaskEvent.primaryEventData = existingTaskID;
          EventService.raiseEvent(closeTaskEvent);
        }
      } else if (recordedCommDetails.caseID != CuramConst.kLongZero) {
        bdmCncrCmmRmKey.caseID = recordedCommDetails.caseID;
        bdmCncrCmmRmKey.concernRoleID =
          recordedCommDetails.correspondentConcernRoleID;

        final Long existingTaskID =
          BDMUtil.getExistingTaskIDForCase(bdmCncrCmmRmKey);

        if (existingTaskID != CuramConst.gkZero) {
          final Event closeTaskEvent = new Event();
          closeTaskEvent.eventKey.eventClass = TASK.CLOSED.eventClass;
          closeTaskEvent.eventKey.eventType = TASK.CLOSED.eventType;
          closeTaskEvent.primaryEventData = existingTaskID;
          EventService.raiseEvent(closeTaskEvent);
        }
      }

    }

  }
  // END: Task 96969: DEV: Curam Task 16 Implementation

}
