package curam.ca.gc.bdm.test.batch.bdmvsgtaskallocation.impl;

import curam.ca.gc.bdm.batch.bdmvsgtaskallocation.fact.BDMVSGTaskAllocationBatchStreamFactory;
import curam.ca.gc.bdm.batch.bdmvsgtaskallocation.impl.BDMVSGTaskAllocationBatchStreamWrapper;
import curam.ca.gc.bdm.batch.bdmvsgtaskallocation.struct.BDMVSGTaskAllocationBatchKey;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.fact.BDMCaseUrgentFlagFactory;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.struct.BDMCaseUrgentFlagDetails;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.ca.gc.bdm.test.framework.CuramServerTest;
import curam.codetable.BATCHPROCESSNAME;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.codetable.LANGUAGE;
import curam.codetable.RECORDSTATUS;
import curam.codetable.SKILLLEVEL;
import curam.codetable.SKILLTYPE;
import curam.codetable.TARGETITEMTYPE;
import curam.codetable.TASKPRIORITY;
import curam.codetable.TASKSTATUS;
import curam.codetable.impl.METHODOFDELIVERYEntry;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.WorkingPatternFactory;
import curam.core.intf.WorkingPattern;
import curam.core.sl.entity.fact.TaskAssignmentFactory;
import curam.core.sl.entity.fact.UserSkillFactory;
import curam.core.sl.entity.fact.UserSkillLanguagesFactory;
import curam.core.sl.entity.struct.TaskAssignmentDtls;
import curam.core.sl.entity.struct.TaskAssignmentDtlsList;
import curam.core.sl.entity.struct.TaskKey;
import curam.core.sl.entity.struct.UserSkillDtls;
import curam.core.sl.entity.struct.UserSkillDtlsList;
import curam.core.sl.entity.struct.UserSkillLanguagesDtls;
import curam.core.sl.entity.struct.UserSkillReadmultiKey;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.PersonDtls;
import curam.core.struct.WorkingPatternDtls;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.workflow.fact.BizObjAssociationFactory;
import curam.util.workflow.fact.TaskFactory;
import curam.util.workflow.fact.WorkflowDeadlineFactory;
import curam.util.workflow.struct.BizObjAssociationDtls;
import curam.util.workflow.struct.TaskDtls;
import curam.util.workflow.struct.WorkflowDeadlineDtls;
import java.util.Calendar;
import org.junit.Test;

public class BDMVSGTaskAllocationBatchStreamWrapperUnitTest
  extends CuramServerTest {

  /**
   * Instantiates test.
   */
  public BDMVSGTaskAllocationBatchStreamWrapperUnitTest(final String arg0) {

    super(arg0);
  }

  /**
   * Gets a default instance of the stream wrapper given a set of batch
   * parameters.
   *
   * @param params The batch parameters.
   * @return A wrapper.
   */
  private BDMVSGTaskAllocationBatchStreamWrapper
    getStreamWrapper(final String instanceID) {

    final curam.ca.gc.bdm.batch.bdmvsgtaskallocation.intf.BDMVSGTaskAllocationBatchStream streamObj =
      BDMVSGTaskAllocationBatchStreamFactory.newInstance();

    final BDMVSGTaskAllocationBatchStreamWrapper streamWrapper =
      new BDMVSGTaskAllocationBatchStreamWrapper(streamObj, instanceID);

    return streamWrapper;
  }

  /**
   * Junit method to test task allocation logic for tasks that are not linked to
   * Urgent flag or are not overdue
   *
   * @throws Exception
   */
  @Test
  public void testBDMVSGTaskAllocationBatchStream() throws Exception {

    final BDMVSGTaskAllocationBatchKey parameters =
      new BDMVSGTaskAllocationBatchKey();
    parameters.instanceID = BATCHPROCESSNAME.BDM_VSG_TASK_ALLOCATION_BATCH;
    parameters.workQueueID = 8000;

    createUserSkillData_VSG01();
    createTaskData_VSG01();

    final BatchProcessingID batchProcessingID = new BatchProcessingID();
    batchProcessingID.recordID = 100;

    final BDMVSGTaskAllocationBatchStreamWrapper streamWrapper =
      getStreamWrapper(parameters.instanceID);

    streamWrapper.processRecord(batchProcessingID, parameters);

    final TaskKey taskKey = new TaskKey();
    taskKey.taskID = 100;

    final TaskAssignmentDtlsList taskAssignmentDtlsList =
      TaskAssignmentFactory.newInstance().searchAssignmentsByTaskID(taskKey);

    assertTrue(taskAssignmentDtlsList.dtls.size() > 0);

    TaskAssignmentDtls taskAssignmentDtls = new TaskAssignmentDtls();
    taskAssignmentDtls = taskAssignmentDtlsList.dtls.get(0);

    // assertTrue(
    // !TARGETITEMTYPE.WORKQUEUE.equals(taskAssignmentDtls.assigneeType));

  }

  /**
   * Create test data for user skill
   *
   * @throws AppException
   * @throws InformationalException
   */
  public void createUserSkillData_VSG01()
    throws AppException, InformationalException {

    final UserSkillDtls userSkillDtls = new UserSkillDtls();
    userSkillDtls.userName = "caseworker";
    userSkillDtls.recordStatus = "RST1";
    userSkillDtls.skillLevel = SKILLLEVEL.PRIMARY;
    userSkillDtls.skillType = SKILLTYPE.VSG01;
    userSkillDtls.versionNo = 1;

    UserSkillFactory.newInstance().insert(userSkillDtls);

    final UserSkillReadmultiKey userSkillReadmultiKey =
      new UserSkillReadmultiKey();
    userSkillReadmultiKey.userName = "caseworker";

    final UserSkillDtlsList userSkillDtlsList =
      UserSkillFactory.newInstance().searchByUser(userSkillReadmultiKey);

    final UserSkillLanguagesDtls userSkillLanguagesDtls =
      new UserSkillLanguagesDtls();
    userSkillLanguagesDtls.userSkillLanguagesID = 123;
    userSkillLanguagesDtls.userSkillID =
      userSkillDtlsList.dtls.get(0).userSkillID;
    userSkillLanguagesDtls.languageCode = LANGUAGE.ENGLISH;

    UserSkillLanguagesFactory.newInstance().insert(userSkillLanguagesDtls);

  }

  /**
   * Creates test data for task
   *
   * @throws AppException
   * @throws InformationalException
   */
  public void createTaskData_VSG01()
    throws AppException, InformationalException {

    final TaskDtls taskCreateDetail = new TaskDtls();

    taskCreateDetail.taskID = 100;
    taskCreateDetail.creationTime = DateTime.getCurrentDateTime();
    taskCreateDetail.wdoSnapshot = "";
    taskCreateDetail.priority = TASKPRIORITY.NORMAL;
    taskCreateDetail.versionNo = 1;

    TaskFactory.newInstance().insert(taskCreateDetail);

    final TaskAssignmentDtls taskAssignmentDtls = new TaskAssignmentDtls();
    taskAssignmentDtls.taskID = 100;
    taskAssignmentDtls.relatedID = 8000;
    taskAssignmentDtls.assigneeType = TARGETITEMTYPE.WORKQUEUE;

    TaskAssignmentFactory.newInstance().insert(taskAssignmentDtls);

    final WorkflowDeadlineDtls workflowDeadlineDtls =
      new WorkflowDeadlineDtls();
    workflowDeadlineDtls.deadlineID = 100;
    workflowDeadlineDtls.taskID = 100;
    workflowDeadlineDtls.suspended = false;
    workflowDeadlineDtls.deadlineTime =
      DateTime.getCurrentDateTime().addTime(1000, 0, 0);
    workflowDeadlineDtls.deadlineType = "WDT1";
    workflowDeadlineDtls.versionNo = 1;

    WorkflowDeadlineFactory.newInstance().insert(workflowDeadlineDtls);

    final UserSkillReadmultiKey userSkillReadmultiKey =
      new UserSkillReadmultiKey();
    userSkillReadmultiKey.userName = "caseworker";

    final UserSkillDtlsList userSkillDtlsList =
      UserSkillFactory.newInstance().searchByUser(userSkillReadmultiKey);

    final BizObjAssociationDtls bizObjAssociationDtls =
      new BizObjAssociationDtls();
    bizObjAssociationDtls.bizObjAssocID = 1111;
    bizObjAssociationDtls.taskID = 100;
    bizObjAssociationDtls.bizObjectID =
      userSkillDtlsList.dtls.get(0).userSkillID; // skillID
    bizObjAssociationDtls.bizObjectType = "USERSKILL";// BUSINESSOBJECTTYPE.SKILL;

    BizObjAssociationFactory.newInstance().insert(bizObjAssociationDtls);

  }

  /**
   * Junit method to test task allocation logic for tasks that are not linked to
   * Urgent flag or are not overdue
   *
   * @throws Exception
   */
  @Test
  public void testBDMVSGTaskAllocationBatch() throws Exception {

    final BDMVSGTaskAllocationBatchKey parameters =
      new BDMVSGTaskAllocationBatchKey();
    parameters.instanceID = BATCHPROCESSNAME.BDM_VSG_TASK_ALLOCATION_BATCH;
    parameters.workQueueID = 8001;

    createUserSkillData_VSG02();
    createTaskData_VSG02();

    final BatchProcessingID batchProcessingID = new BatchProcessingID();
    batchProcessingID.recordID = 100;

    final BDMVSGTaskAllocationBatchStreamWrapper streamWrapper =
      getStreamWrapper(parameters.instanceID);

    streamWrapper.processRecord(batchProcessingID, parameters);

    final TaskKey taskKey = new TaskKey();
    taskKey.taskID = 102;

    final TaskAssignmentDtlsList taskAssignmentDtlsList =
      TaskAssignmentFactory.newInstance().searchAssignmentsByTaskID(taskKey);

    assertTrue(taskAssignmentDtlsList.dtls.size() > 0);

    TaskAssignmentDtls taskAssignmentDtls = new TaskAssignmentDtls();
    taskAssignmentDtls = taskAssignmentDtlsList.dtls.get(0);

    // assertTrue(
    // !TARGETITEMTYPE.WORKQUEUE.equals(taskAssignmentDtls.assigneeType));

  }

  /**
   * Creates test data for user skill
   *
   * @throws AppException
   * @throws InformationalException
   */
  public void createUserSkillData_VSG02()
    throws AppException, InformationalException {

    final UserSkillDtls userSkillDtls = new UserSkillDtls();
    userSkillDtls.userName = "caseworker";
    userSkillDtls.recordStatus = "RST1";
    userSkillDtls.skillLevel = SKILLLEVEL.HIGH;
    userSkillDtls.skillType = SKILLTYPE.VSG02;
    userSkillDtls.versionNo = 1;

    UserSkillFactory.newInstance().insert(userSkillDtls);

    final UserSkillReadmultiKey userSkillReadmultiKey =
      new UserSkillReadmultiKey();
    userSkillReadmultiKey.userName = "caseworker";

    final UserSkillDtlsList userSkillDtlsList =
      UserSkillFactory.newInstance().searchByUser(userSkillReadmultiKey);

    final UserSkillLanguagesDtls userSkillLanguagesDtls =
      new UserSkillLanguagesDtls();
    userSkillLanguagesDtls.userSkillLanguagesID = 124;
    userSkillLanguagesDtls.userSkillID =
      userSkillDtlsList.dtls.get(0).userSkillID;
    userSkillLanguagesDtls.languageCode = LANGUAGE.FRENCH;

    UserSkillLanguagesFactory.newInstance().insert(userSkillLanguagesDtls);

  }

  /**
   * Creates test data for task
   *
   * @throws AppException
   * @throws InformationalException
   */
  public void createTaskData_VSG02()
    throws AppException, InformationalException {

    final TaskDtls taskCreateDetail = new TaskDtls();

    taskCreateDetail.taskID = 102;
    taskCreateDetail.creationTime = DateTime.getCurrentDateTime();
    taskCreateDetail.wdoSnapshot = "";
    taskCreateDetail.priority = TASKPRIORITY.NORMAL;
    taskCreateDetail.versionNo = 1;

    TaskFactory.newInstance().insert(taskCreateDetail);

    final TaskAssignmentDtls taskAssignmentDtls = new TaskAssignmentDtls();
    taskAssignmentDtls.taskID = 102;
    taskAssignmentDtls.relatedID = 8001;
    taskAssignmentDtls.assigneeType = TARGETITEMTYPE.WORKQUEUE;

    TaskAssignmentFactory.newInstance().insert(taskAssignmentDtls);

    final WorkflowDeadlineDtls workflowDeadlineDtls =
      new WorkflowDeadlineDtls();
    workflowDeadlineDtls.deadlineID = 102;
    workflowDeadlineDtls.taskID = 102;
    workflowDeadlineDtls.suspended = false;
    workflowDeadlineDtls.deadlineTime =
      DateTime.getCurrentDateTime().addTime(1000, 0, 0);
    workflowDeadlineDtls.deadlineType = "WDT1";
    workflowDeadlineDtls.versionNo = 1;

    WorkflowDeadlineFactory.newInstance().insert(workflowDeadlineDtls);

    final UserSkillReadmultiKey userSkillReadmultiKey =
      new UserSkillReadmultiKey();
    userSkillReadmultiKey.userName = "caseworker";

    final UserSkillDtlsList userSkillDtlsList =
      UserSkillFactory.newInstance().searchByUser(userSkillReadmultiKey);

    final BizObjAssociationDtls bizObjAssociationDtls =
      new BizObjAssociationDtls();
    bizObjAssociationDtls.bizObjAssocID = 1113;
    bizObjAssociationDtls.taskID = 102;
    bizObjAssociationDtls.bizObjectID =
      userSkillDtlsList.dtls.get(0).userSkillID; // skillID
    bizObjAssociationDtls.bizObjectType = "USERSKILL";// BUSINESSOBJECTTYPE.SKILL;

    BizObjAssociationFactory.newInstance().insert(bizObjAssociationDtls);

  }

  /**
   * Creates test data for user skill and working patterns
   *
   * @throws AppException
   * @throws InformationalException
   */
  private void createUserSkillData(final String userName,
    final String skillLevel, final String skillType, final long commonID,
    final String language, final String workingHoursType)
    throws AppException, InformationalException {

    final UserSkillDtls userSkillDtls = new UserSkillDtls();
    userSkillDtls.userName = userName;
    userSkillDtls.recordStatus = "RST1";
    userSkillDtls.skillLevel = skillLevel; // SKILLLEVEL.SECONDARY;
    userSkillDtls.skillType = skillType; // SKILLTYPE.VSG02;
    userSkillDtls.versionNo = 1;

    UserSkillFactory.newInstance().insert(userSkillDtls);

    final UserSkillReadmultiKey userSkillReadmultiKey =
      new UserSkillReadmultiKey();
    userSkillReadmultiKey.userName = userName;

    // final UserSkillDtlsList userSkillDtlsList =
    // UserSkillFactory.newInstance().searchByUser(userSkillReadmultiKey);
    //
    // final UserSkillLanguagesDtls userSkillLanguagesDtls =
    // new UserSkillLanguagesDtls();
    // userSkillLanguagesDtls.userSkillLanguagesID = commonID; // 125;
    // userSkillLanguagesDtls.userSkillID =
    // userSkillDtlsList.dtls.get(0).userSkillID;
    // userSkillLanguagesDtls.languageCode = language; // LANGUAGE.FRENCH;
    // UserSkillLanguagesFactory.newInstance().insert(userSkillLanguagesDtls);

    final WorkingPattern workingPatternObj =
      WorkingPatternFactory.newInstance();

    final WorkingPatternDtls workingPatternDtls = new WorkingPatternDtls();
    workingPatternDtls.workingPatternID = commonID; // 111;
    workingPatternDtls.userName = userName; // "sally";
    workingPatternDtls.recordStatusCode = "RST1";
    workingPatternDtls.workingHoursType = workingHoursType; // WORKINGHOURSTYPE.WORKINGHOURS;
    workingPatternDtls.startDate = Date.getDate("20220606");
    workingPatternDtls.defaultStartTime =
      DateTime.getDateTime("20220606T000000");
    workingPatternDtls.defaultEndTime =
      nextSundayDate().getDateTime().addTime(24, 0, 0);
    workingPatternDtls.endDate = nextSundayDate();
    workingPatternDtls.versionNo = 1;

    workingPatternObj.insert(workingPatternDtls);

  }

  /**
   * Creates test data for task
   *
   * @throws AppException
   * @throws InformationalException
   */
  private void createTaskData(final long commonID,
    final long bdmTaskClassificationID, final long caseID)
    throws AppException, InformationalException {

    final TaskDtls taskCreateDetail = new TaskDtls();

    taskCreateDetail.taskID = commonID;
    taskCreateDetail.creationTime = DateTime.getCurrentDateTime();
    taskCreateDetail.status = TASKSTATUS.NOTSTARTED;
    taskCreateDetail.wdoSnapshot = "";
    taskCreateDetail.priority = TASKPRIORITY.NORMAL;
    taskCreateDetail.versionNo = 1;

    TaskFactory.newInstance().insert(taskCreateDetail);

    final TaskAssignmentDtls taskAssignmentDtls = new TaskAssignmentDtls();
    taskAssignmentDtls.taskID = commonID;
    taskAssignmentDtls.relatedID = 80013;
    taskAssignmentDtls.assigneeType = TARGETITEMTYPE.WORKQUEUE;

    TaskAssignmentFactory.newInstance().insert(taskAssignmentDtls);

    final WorkflowDeadlineDtls workflowDeadlineDtls =
      new WorkflowDeadlineDtls();
    workflowDeadlineDtls.deadlineID = commonID;
    workflowDeadlineDtls.taskID = commonID;
    workflowDeadlineDtls.suspended = false;
    workflowDeadlineDtls.deadlineTime =
      DateTime.getCurrentDateTime().addTime(-1000, 0, 0);
    workflowDeadlineDtls.deadlineType = "WDT1";
    workflowDeadlineDtls.versionNo = 1;

    WorkflowDeadlineFactory.newInstance().insert(workflowDeadlineDtls);

    BizObjAssociationDtls bizObjAssociationDtls = new BizObjAssociationDtls();
    bizObjAssociationDtls.bizObjAssocID = commonID;
    bizObjAssociationDtls.taskID = commonID;
    bizObjAssociationDtls.bizObjectID = bdmTaskClassificationID; // 80009;
    bizObjAssociationDtls.bizObjectType = BUSINESSOBJECTTYPE.TASKSKILLTYPE;

    BizObjAssociationFactory.newInstance().insert(bizObjAssociationDtls);

    bizObjAssociationDtls = new BizObjAssociationDtls();
    bizObjAssociationDtls.bizObjAssocID = commonID + 100001;
    bizObjAssociationDtls.taskID = commonID;
    bizObjAssociationDtls.bizObjectID = caseID;
    bizObjAssociationDtls.bizObjectType = BUSINESSOBJECTTYPE.CASE;

    BizObjAssociationFactory.newInstance().insert(bizObjAssociationDtls);

  }

  /**
   * Register person and create case
   *
   * @param caseReference
   * @param caseID
   * @throws AppException
   * @throws InformationalException
   */
  private void registerPersonCreateCase(final String caseReference,
    final long caseID) throws AppException, InformationalException {

    final RegisterPerson registerPersonObj = new RegisterPerson("");
    final PersonDtls personDtls = registerPersonObj
      .registerDefault("Joe Test", METHODOFDELIVERYEntry.CHEQUE);
    final long concernRoleID = personDtls.concernRoleID;

    final CaseHeaderDtls chDtls = new CaseHeaderDtls();
    chDtls.appealIndicator = false;
    chDtls.caseReference = caseReference;
    chDtls.versionNo = 1;
    chDtls.concernRoleID = concernRoleID;
    chDtls.caseID = caseID;
    CaseHeaderFactory.newInstance().insert(chDtls);
  }

  /**
   * Setup urgent flag data
   *
   * @param caseID
   * @param urgentFlagType
   * @param startDate
   * @throws AppException
   * @throws InformationalException
   */
  private void setUpUrgentFlagData(final long caseID,
    final String urgentFlagType, final Date startDate)
    throws AppException, InformationalException {

    final BDMCaseUrgentFlagDetails flagDtls = new BDMCaseUrgentFlagDetails();
    flagDtls.caseID = caseID;
    TransactionInfo.getInfo();
    flagDtls.createdBy = TransactionInfo.getProgramUser();
    TransactionInfo.getInfo();
    flagDtls.createdByFullName = TransactionInfo.getProgramUser();
    flagDtls.recordStatus = RECORDSTATUS.NORMAL;
    flagDtls.startDate = startDate;
    flagDtls.type = urgentFlagType;
    BDMCaseUrgentFlagFactory.newInstance().createCaseUrgentFlag(flagDtls);

  }

  /**
   * Helper method to get next sunday
   *
   * @param date
   * @return
   */
  private Date nextSundayDate() {

    final Date currentDate = Date.getCurrentDate();
    Date nextSunday = Date.getCurrentDate();
    final Calendar calendar = currentDate.getCalendar();
    final int weekday = calendar.get(Calendar.DAY_OF_WEEK);
    int daysDiff = Calendar.SUNDAY - weekday;
    if (daysDiff <= 0) {
      daysDiff += 7;
    }

    nextSunday = currentDate.addDays(daysDiff);

    return nextSunday;

  }

}
