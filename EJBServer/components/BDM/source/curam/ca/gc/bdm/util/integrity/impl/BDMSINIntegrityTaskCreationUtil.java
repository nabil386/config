package curam.ca.gc.bdm.util.integrity.impl;

import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.entity.fact.BDMSINIntegrityTaskFactory;
import curam.ca.gc.bdm.entity.intf.BDMSINIntegrityTask;
import curam.ca.gc.bdm.entity.struct.BDMSINIntegrityTaskDetails;
import curam.ca.gc.bdm.entity.struct.BDMSINIntegrityTaskDtls;
import curam.ca.gc.bdm.entity.struct.BDMSINIntegrityTaskKey;
import curam.ca.gc.bdm.entity.struct.BDMSINIntegrityTaskSearchKey;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskSkillTypeKey;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskSkillTypeList;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.sl.bdmworkallocation.fact.BDMWorkAllocationTaskFactory;
import curam.ca.gc.bdm.sl.bdmworkallocation.intf.BDMWorkAllocationTask;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.SKILLTYPE;
import curam.codetable.TASKSTATUS;
import curam.core.fact.ConcernRoleFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.pdc.impl.PDCConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.NotFoundIndicator;
import curam.util.workflow.fact.TaskFactory;
import curam.util.workflow.impl.EnactmentService;
import curam.util.workflow.struct.TaskDtls;
import curam.util.workflow.struct.TaskKey;
import java.util.ArrayList;
import java.util.List;

public class BDMSINIntegrityTaskCreationUtil {

  /**
   * Process flow to create task for SIN Integrity review.
   */
  private static final String kSINIntegrityProcess =
    "BDMSINIntegrityTaskWorkflow";

  /**
   * Process flow to create task for SIN Mismatch review.
   */
  private static final String kSINMismatchProcess =
    "BDMSINMismatchTaskWorkflow";

  /**
   * Process flow to create task for SIN Investigation review.
   */
  private static final String kSINInvestigationyProcess =
    "BDMSINInvestigationTaskWorkflow";

  /**
   * Create Task for SIN Integrity Review process for specified Person and
   * specified task type
   *
   * @param concernRoleID
   * @param taskType
   * @throws AppException
   * @throws InformationalException
   */
  public static void createTaskForSINIntegrity(final long concernRoleID,
    final String taskType) throws AppException, InformationalException {

    // get the existing task details if created for this person of same review
    // process
    final BDMSINIntegrityTaskSearchKey taskSearchKey =
      new BDMSINIntegrityTaskSearchKey();
    taskSearchKey.concernRoleID = concernRoleID;
    taskSearchKey.taskType = taskType;
    final NotFoundIndicator nfi = new NotFoundIndicator();
    final BDMSINIntegrityTask sinTaskObj =
      BDMSINIntegrityTaskFactory.newInstance();
    final BDMSINIntegrityTaskDtls sinIntegrityTaskDtlsdtls =
      sinTaskObj.readByConcernRoleIDAndTaskType(nfi, taskSearchKey);
    if (nfi.isNotFound()) {// no record found
      // create a new task and set the linked task id.
      final long processInstanceID = createTask(concernRoleID, taskType);
      createSINIntegrityTaskEntry(concernRoleID, taskType, sinTaskObj,
        processInstanceID);
    } else { // record found
      // Get the task details
      TaskDtls taskDetails = new TaskDtls();
      if (sinIntegrityTaskDtlsdtls.linkedProcessInstanceID != 0) {
        final TaskKey taskKey = new TaskKey();
        taskKey.taskID = BDMIntegrityRulesUtil.getTaskIDFromProcessInstanceID(
          sinIntegrityTaskDtlsdtls.linkedProcessInstanceID);
        if (taskKey.taskID != 0l) {
          taskDetails = TaskFactory.newInstance().read(taskKey);
        }
      }
      // Check for task status
      if (sinIntegrityTaskDtlsdtls.linkedProcessInstanceID == 0l
        || taskDetails.taskID == 0l
        || taskDetails.status.equals(TASKSTATUS.CLOSED)
        || taskDetails.status.equals(TASKSTATUS.COMPLETED)) {
        // create a new task and set the linked task id.
        final long processInstanceID = createTask(concernRoleID, taskType);
        modifySINIntegrityTaskEntry(sinTaskObj, sinIntegrityTaskDtlsdtls,
          processInstanceID);
      }
    }
  }

  /**
   * Modify the SIN Task entity to have the new process instance id
   *
   * @param sinTaskObj
   * @param sinIntegrityTaskDtlsdtls
   * @param processInstanceID
   * @throws AppException
   * @throws InformationalException
   */
  private static void modifySINIntegrityTaskEntry(
    final BDMSINIntegrityTask sinTaskObj,
    final BDMSINIntegrityTaskDtls sinIntegrityTaskDtlsdtls,
    final long processInstanceID)
    throws AppException, InformationalException {

    // modify dtls and call entity modify operation
    sinIntegrityTaskDtlsdtls.linkedProcessInstanceID = processInstanceID;
    final BDMSINIntegrityTaskKey key = new BDMSINIntegrityTaskKey();
    key.bdmSINIntegrityTaskID =
      sinIntegrityTaskDtlsdtls.bdmSINIntegrityTaskID;
    sinTaskObj.modify(key, sinIntegrityTaskDtlsdtls);
  }

  /**
   * Create BDM Task entity entry to keep track of the task for specified person
   *
   * @param concernRoleID
   * @param taskType
   * @param sinTaskObj
   * @param processInstanceID
   * @throws AppException
   * @throws InformationalException
   */
  private static void createSINIntegrityTaskEntry(final long concernRoleID,
    final String taskType, final BDMSINIntegrityTask sinTaskObj,
    final long processInstanceID)
    throws AppException, InformationalException {

    // populate entity dtls and call inset method
    final BDMSINIntegrityTaskDtls bdmSINtaskDtls =
      new BDMSINIntegrityTaskDtls();
    // primary key
    bdmSINtaskDtls.bdmSINIntegrityTaskID =
      UniqueIDFactory.newInstance().getNextID();
    bdmSINtaskDtls.concernRoleID = concernRoleID;
    bdmSINtaskDtls.linkedProcessInstanceID = processInstanceID;
    bdmSINtaskDtls.taskType = taskType;
    sinTaskObj.insert(bdmSINtaskDtls);
  }

  /**
   * Task enactment for give person and task type(possible value Mismatch,
   * Integrity, Investigation); return process instance id.
   *
   * @param concernRoleID
   * @param taskType
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private static long createTask(final long concernRoleID,
    final String taskType) throws AppException, InformationalException {

    // Task deadline
    final long currentDateTimeInMills = Date.getCurrentDate().asLong();
    // get the person details
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = concernRoleID;
    final ConcernRoleDtls dtls =
      ConcernRoleFactory.newInstance().read(concernRoleKey);
    // Create the task struct for SIN Integrity process
    final BDMSINIntegrityTaskDetails taskDetails =
      new BDMSINIntegrityTaskDetails();
    taskDetails.concernRoleID = concernRoleID;
    taskDetails.concernRoleName = dtls.concernRoleName;
    taskDetails.personReferenceID = dtls.primaryAlternateID;
    final BDMEvidenceUtil bdmEvidenceUtil = new BDMEvidenceUtil();
    final List<DynamicEvidenceDataDetails> contactEvidence =
      bdmEvidenceUtil.getEvdDtlsByConcernroleIDandEvidenceType(concernRoleID,
        PDCConst.PDCCONTACTPREFERENCES);
    if (contactEvidence.size() != 0) {
      final String languagePref = contactEvidence.get(0)
        .getAttribute(BDMConstants.kpreferredOralLanguage).getValue();
      if (!curam.util.resources.StringUtil.isNullOrEmpty(languagePref)) {

        if (BDMLANGUAGE.ENGLISHL.equals(languagePref)) {
          taskDetails.languagePref = "EN";
        } else if (BDMLANGUAGE.FRENCHL.equals(languagePref)) {
          taskDetails.languagePref = "FR";
        }
      }
    } else {
      taskDetails.languagePref = "EN";
    }

    // BEGIN - 57898 -

    // retrieve prospect person PDC evidence
    String integrityReviewflagType = "";
    String investigationReferralflagType = "";
    final List<DynamicEvidenceDataDetails> sinEvidenceList =
      bdmEvidenceUtil.getEvdDtlsByConcernroleIDandEvidenceType(concernRoleID,
        CASEEVIDENCE.BDMSINIDENTIFICATIONSTATUS);
    if (sinEvidenceList.size() != 0) {
      integrityReviewflagType = sinEvidenceList.get(0)
        .getAttribute(BDMConstants.kresIntegrityReviewFlagType).getValue();

      investigationReferralflagType = sinEvidenceList.get(0)
        .getAttribute(BDMConstants.kresInvestigationReferralFlagType)
        .getValue();
    }

    String skillTypeDesc = "";
    final StringBuffer taskSubject = new StringBuffer();

    // workflow name
    String processName = kSINMismatchProcess;
    // Prepare taskSubject
    if (taskType
      .equals(BDMSINIntegrityCheckConstants.SIN_TASKTYPE_INTEGRITY)) {
      processName = kSINIntegrityProcess;

      taskDetails.skillType = SKILLTYPE.VSG02;
      skillTypeDesc = CodeTable.getOneItem(SKILLTYPE.TABLENAME,
        taskDetails.skillType, TransactionInfo.getProgramLocale());
      taskSubject.append(skillTypeDesc);

      taskSubject.append(" " + taskDetails.languagePref);
      taskSubject.append(" SIN " + integrityReviewflagType);
      taskSubject.append(" Flag Integrity Review for "
        + taskDetails.concernRoleName + " " + taskDetails.personReferenceID);

      final int taskDeadlineDays = BDMConstants.kIntegrityReviewDeadlineDays;
      final long inputDateTimeInMills =
        Date.getCurrentDate().addDays(taskDeadlineDays).asLong();
      final long durationMills =
        inputDateTimeInMills - currentDateTimeInMills;
      final long durationSeconds =
        durationMills / DateTime.kMilliSecsInSecond;
      taskDetails.deadLineDurationInSec = (int) durationSeconds;

    } else if (taskType
      .equals(BDMSINIntegrityCheckConstants.SIN_TASKTYPE_INVESTIGATION)) {
      processName = kSINInvestigationyProcess;

      taskDetails.skillType = SKILLTYPE.VSG08;
      skillTypeDesc = CodeTable.getOneItem(SKILLTYPE.TABLENAME,
        taskDetails.skillType, TransactionInfo.getProgramLocale());
      taskSubject.append(skillTypeDesc);

      taskSubject.append(" " + taskDetails.languagePref);
      taskSubject.append(" SIN " + investigationReferralflagType);
      taskSubject.append(" Flag Investigation Review for "
        + taskDetails.concernRoleName + " " + taskDetails.personReferenceID);
      final int taskDeadlineDays =
        BDMConstants.kInvestigationReferralDeadlineDays;
      final long inputDateTimeInMills =
        Date.getCurrentDate().addDays(taskDeadlineDays).asLong();
      final long durationMills =
        inputDateTimeInMills - currentDateTimeInMills;
      final long durationSeconds =
        durationMills / DateTime.kMilliSecsInSecond;
      taskDetails.deadLineDurationInSec = (int) durationSeconds;
    } else {
      // SIR non match task
      taskDetails.skillType = SKILLTYPE.VSG02;
      skillTypeDesc = CodeTable.getOneItem(SKILLTYPE.TABLENAME,
        taskDetails.skillType, TransactionInfo.getProgramLocale());
      taskSubject.append(skillTypeDesc);

      taskSubject.append(" " + taskDetails.languagePref);
      taskSubject.append(" SIR Non Match for " + taskDetails.concernRoleName
        + " " + taskDetails.personReferenceID);
      final int taskDeadlineDays = BDMConstants.kSINMisMatchDeadlineDays;
      final long inputDateTimeInMills =
        Date.getCurrentDate().addDays(taskDeadlineDays).asLong();
      final long durationMills =
        inputDateTimeInMills - currentDateTimeInMills;
      final long durationSeconds =
        durationMills / DateTime.kMilliSecsInSecond;
      taskDetails.deadLineDurationInSec = (int) durationSeconds;
    }

    taskDetails.subject = taskSubject.toString();

    // Find taskClassificationID for skillType
    final BDMTaskSkillTypeKey bdmTaskSkillTypeKey = new BDMTaskSkillTypeKey();
    BDMTaskSkillTypeList bdmTaskSkillTypeList = new BDMTaskSkillTypeList();
    final BDMWorkAllocationTask bdmWorkAllocationTaskObj =
      BDMWorkAllocationTaskFactory.newInstance();

    // Get skillType by documentType
    bdmTaskSkillTypeKey.skillType = taskDetails.skillType;

    // Get bdmTaskClassificationID by skillType
    bdmTaskSkillTypeList =
      bdmWorkAllocationTaskObj.searchBDMTaskSkillType(bdmTaskSkillTypeKey);

    if (bdmTaskSkillTypeList.dtls.size() > 0) {
      taskDetails.bdmTaskClassificationID =
        bdmTaskSkillTypeList.dtls.get(0).bdmTaskClassificationID;
    }
    // END - 57898

    // Create the list we will pass to the enactment service.
    final List<Object> enactmentStructs = new ArrayList<Object>();
    enactmentStructs.add(taskDetails);
    // Enact work flow to create the appeal deadline Task.
    return EnactmentService.startProcess(processName, enactmentStructs);
  }
}
