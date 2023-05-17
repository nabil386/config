package curam.ca.gc.bdm.test.batch.bdmtaskallocation.impl;

import curam.ca.gc.bdm.batch.bdmtaskallocation.fact.BDMTaskAllocationBatchFactory;
import curam.ca.gc.bdm.batch.bdmtaskallocation.intf.BDMTaskAllocationBatch;
import curam.ca.gc.bdm.batch.bdmtaskallocation.struct.BDMTaskAllocationBatchKey;
import curam.ca.gc.bdm.codetable.BDMURGENTFLAGTYPE;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.fact.BDMCaseUrgentFlagFactory;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.struct.BDMCaseUrgentFlagDetails;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.ASSIGNEETYPE;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.codetable.RECORDSTATUS;
import curam.codetable.SKILLLEVEL;
import curam.codetable.SKILLTYPE;
import curam.codetable.TARGETITEMTYPE;
import curam.codetable.TASKPRIORITY;
import curam.codetable.TASKSTATUS;
import curam.codetable.WORKINGHOURSTYPE;
import curam.codetable.impl.METHODOFDELIVERYEntry;
import curam.core.facade.fact.OrganizationFactory;
import curam.core.facade.intf.Organization;
import curam.core.facade.struct.UserStatusDetails_fo;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.WorkingPatternFactory;
import curam.core.intf.WorkingPattern;
import curam.core.sl.entity.fact.TaskAssignmentFactory;
import curam.core.sl.entity.fact.UserSkillFactory;
import curam.core.sl.entity.intf.UserSkill;
import curam.core.sl.entity.struct.CancelUserSkillDetails;
import curam.core.sl.entity.struct.CountDetails;
import curam.core.sl.entity.struct.TaskAssignmentDtls;
import curam.core.sl.entity.struct.TaskAssignmentDtlsList;
import curam.core.sl.entity.struct.TaskIDRelatedIDAndTypeKey;
import curam.core.sl.entity.struct.TaskKey;
import curam.core.sl.entity.struct.TaskUserNameStatusAndRestartDateTimeKey;
import curam.core.sl.entity.struct.TasksByDueDateForUserDetailsList;
import curam.core.sl.entity.struct.UserSkillDtls;
import curam.core.sl.entity.struct.UserSkillKey;
import curam.core.sl.entity.struct.UserSkillReadmultiKey;
import curam.core.sl.entity.struct.UserTasksByDueDatePriorityStatusKey;
import curam.core.sl.fact.UserAccessFactory;
import curam.core.sl.intf.UserAccess;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.PersonDtls;
import curam.core.struct.UsersKey;
import curam.core.struct.WorkingPatternDtls;
import curam.core.struct.WorkingPatternDtlsList;
import curam.core.struct.WorkingPatternKey;
import curam.util.dataaccess.CuramValueList;
import curam.util.dataaccess.DynamicDataAccess;
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
import static org.junit.Assert.assertTrue;

// Task 9045 BAD Task Allocation batch junit
public class BDMTaskAllocationBatchTest extends CuramServerTestJUnit4 {

  /**
   *
   * Skill type VSG01 is assigned to user having VSG01 skill type at Primary
   * level
   * Mode- stage1
   *
   * @throws Exception
   */
  @Test
  public void testBDMVSGTaskAllocation_Stage1Flow() throws Exception {

    // -------------------------------------------------------
    // Set up user data
    // -------------------------------------------------------
    final UserSkill userSkillObj = UserSkillFactory.newInstance();
    final UserSkillDtls userSkillDtls = new UserSkillDtls();
    final StringBuffer sqlStr = new StringBuffer();
    sqlStr.append(
      "SELECT USERSKILLID INTO :userSkillID FROM USERSKILL WHERE RECORDSTATUS ='RST1'");
    final CuramValueList<UserSkillDtls> curamValueList = DynamicDataAccess
      .executeNsMulti(UserSkillDtls.class, null, false, sqlStr.toString());
    System.out.println(curamValueList.size());
    UserSkillKey userSkillKey = null;
    CancelUserSkillDetails cancelUserSkillDetails = null;
    for (int i = 0; i < curamValueList.size(); i++) {
      userSkillKey = new UserSkillKey();
      userSkillKey.userSkillID = curamValueList.item(i).userSkillID;
      cancelUserSkillDetails = new CancelUserSkillDetails();
      cancelUserSkillDetails.recordStatus = "RST2";
      cancelUserSkillDetails.versionNo = 1;
      userSkillObj.cancel(userSkillKey, cancelUserSkillDetails);

    }

    final CuramValueList<UserSkillDtls> curamValueList1 = DynamicDataAccess
      .executeNsMulti(UserSkillDtls.class, null, false, sqlStr.toString());
    System.out.println(curamValueList1.size());

    registerPersonCreateCase("111111", 111111);
    setUpUrgentFlagData(111111, BDMURGENTFLAGTYPE.MPENQUIRY,
      Date.getCurrentDate());

    createUserSkillData("mason", SKILLLEVEL.PRIMARY, SKILLTYPE.VSG01, 101,
      curam.codetable.LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);

    createTaskData(101, 80000, 111111); // BDMSKL8001 - VSG01 LN1

    createTaskData_1(102, 80000, 111111); // BDMSKL8001 - VSG01 LN1)

    final BDMTaskAllocationBatch bdmTaskAllocationBatchObj =
      BDMTaskAllocationBatchFactory.newInstance();

    final BDMTaskAllocationBatchKey bdmTaskAllocationBatchKey =
      new BDMTaskAllocationBatchKey();
    bdmTaskAllocationBatchKey.batchMode = "stage2";

    bdmTaskAllocationBatchObj.process(bdmTaskAllocationBatchKey);

    final TaskKey taskKey = new TaskKey();
    taskKey.taskID = 101;

    final TaskAssignmentDtlsList taskAssignmentDtlsList =
      TaskAssignmentFactory.newInstance().searchAssignmentsByTaskID(taskKey);

    assertTrue(taskAssignmentDtlsList.dtls.size() > 0);
    assertTrue(
      taskAssignmentDtlsList.dtls.item(0).relatedName.equals("mason"));

  }

  @Test
  public void testBDMVSGTaskAllocation_Stage2_Flow() throws Exception {

    // -------------------------------------------------------
    // Set up user data
    // -------------------------------------------------------
    final UserSkill userSkillObj = UserSkillFactory.newInstance();
    final UserSkillDtls userSkillDtls = new UserSkillDtls();
    final StringBuffer sqlStr = new StringBuffer();
    sqlStr.append(
      "SELECT USERSKILLID INTO :userSkillID FROM USERSKILL WHERE RECORDSTATUS ='RST1'");
    final CuramValueList<UserSkillDtls> curamValueList = DynamicDataAccess
      .executeNsMulti(UserSkillDtls.class, null, false, sqlStr.toString());
    System.out.println(curamValueList.size());
    UserSkillKey userSkillKey = null;
    CancelUserSkillDetails cancelUserSkillDetails = null;
    for (int i = 0; i < curamValueList.size(); i++) {
      userSkillKey = new UserSkillKey();
      userSkillKey.userSkillID = curamValueList.item(i).userSkillID;
      cancelUserSkillDetails = new CancelUserSkillDetails();
      cancelUserSkillDetails.recordStatus = "RST2";
      cancelUserSkillDetails.versionNo = 1;
      userSkillObj.cancel(userSkillKey, cancelUserSkillDetails);

    }

    final CuramValueList<UserSkillDtls> curamValueList1 = DynamicDataAccess
      .executeNsMulti(UserSkillDtls.class, null, false, sqlStr.toString());
    System.out.println(curamValueList1.size());

    registerPersonCreateCase("111111", 111111);

    createUserSkillData("mason", SKILLLEVEL.PRIMARY, SKILLTYPE.VSG01, 101,
      curam.codetable.LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);

    createTaskData(101, 80000, 111111); // BDMSKL8001 - VSG01 LN1

    final BDMTaskAllocationBatch bdmTaskAllocationBatchObj =
      BDMTaskAllocationBatchFactory.newInstance();

    final BDMTaskAllocationBatchKey bdmTaskAllocationBatchKey =
      new BDMTaskAllocationBatchKey();
    bdmTaskAllocationBatchKey.batchMode = "stage2";

    bdmTaskAllocationBatchObj.process(bdmTaskAllocationBatchKey);

    final TaskKey taskKey = new TaskKey();
    taskKey.taskID = 101;

    final TaskAssignmentDtlsList taskAssignmentDtlsList =
      TaskAssignmentFactory.newInstance().searchAssignmentsByTaskID(taskKey);

    assertTrue(taskAssignmentDtlsList.dtls.size() > 0);
    assertTrue(
      taskAssignmentDtlsList.dtls.item(0).relatedName.equals("mason"));

  }

  @Test
  public void testBDMVSGTaskAllocation_PTM_Flow() throws Exception {

    // -------------------------------------------------------
    // Set up user data
    // -------------------------------------------------------
    final UserSkill userSkillObj = UserSkillFactory.newInstance();
    final UserSkillDtls userSkillDtls = new UserSkillDtls();
    final StringBuffer sqlStr = new StringBuffer();
    sqlStr.append(
      "SELECT USERSKILLID INTO :userSkillID FROM USERSKILL WHERE RECORDSTATUS ='RST1'");
    final CuramValueList<UserSkillDtls> curamValueList = DynamicDataAccess
      .executeNsMulti(UserSkillDtls.class, null, false, sqlStr.toString());
    System.out.println(curamValueList.size());
    UserSkillKey userSkillKey = null;
    CancelUserSkillDetails cancelUserSkillDetails = null;
    for (int i = 0; i < curamValueList.size(); i++) {
      userSkillKey = new UserSkillKey();
      userSkillKey.userSkillID = curamValueList.item(i).userSkillID;
      cancelUserSkillDetails = new CancelUserSkillDetails();
      cancelUserSkillDetails.recordStatus = "RST2";
      cancelUserSkillDetails.versionNo = 1;
      userSkillObj.cancel(userSkillKey, cancelUserSkillDetails);

    }

    final CuramValueList<UserSkillDtls> curamValueList1 = DynamicDataAccess
      .executeNsMulti(UserSkillDtls.class, null, false, sqlStr.toString());
    System.out.println(curamValueList1.size());

    registerPersonCreateCase("111111", 111111);
    setUpUrgentFlagData(111111, BDMURGENTFLAGTYPE.MPENQUIRY,
      Date.getCurrentDate());

    createUserSkillData("mason", SKILLLEVEL.PTM, SKILLTYPE.VSG01, 101,
      curam.codetable.LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);

    createTaskData(101, 80000, 111111); // BDMSKL8001 - VSG01 LN1

    final BDMTaskAllocationBatch bdmTaskAllocationBatchObj =
      BDMTaskAllocationBatchFactory.newInstance();

    final BDMTaskAllocationBatchKey bdmTaskAllocationBatchKey =
      new BDMTaskAllocationBatchKey();
    bdmTaskAllocationBatchKey.batchMode = "ptm";

    bdmTaskAllocationBatchObj.process(bdmTaskAllocationBatchKey);

    final TaskKey taskKey = new TaskKey();
    taskKey.taskID = 101;

    final TaskAssignmentDtlsList taskAssignmentDtlsList =
      TaskAssignmentFactory.newInstance().searchAssignmentsByTaskID(taskKey);

    assertTrue(taskAssignmentDtlsList.dtls.size() > 0);
    assertTrue(
      taskAssignmentDtlsList.dtls.item(0).relatedName.equals("mason"));

  }

  @Test
  public void testBDMVSGTaskAllocation_PTM_Flow_taskDeadline()
    throws Exception {

    // -------------------------------------------------------
    // Set up user data
    // -------------------------------------------------------
    final UserSkill userSkillObj = UserSkillFactory.newInstance();
    final UserSkillDtls userSkillDtls = new UserSkillDtls();
    final StringBuffer sqlStr = new StringBuffer();
    sqlStr.append(
      "SELECT USERSKILLID INTO :userSkillID FROM USERSKILL WHERE RECORDSTATUS ='RST1'");
    final CuramValueList<UserSkillDtls> curamValueList = DynamicDataAccess
      .executeNsMulti(UserSkillDtls.class, null, false, sqlStr.toString());
    System.out.println(curamValueList.size());
    UserSkillKey userSkillKey = null;
    CancelUserSkillDetails cancelUserSkillDetails = null;
    for (int i = 0; i < curamValueList.size(); i++) {
      userSkillKey = new UserSkillKey();
      userSkillKey.userSkillID = curamValueList.item(i).userSkillID;
      cancelUserSkillDetails = new CancelUserSkillDetails();
      cancelUserSkillDetails.recordStatus = "RST2";
      cancelUserSkillDetails.versionNo = 1;
      userSkillObj.cancel(userSkillKey, cancelUserSkillDetails);

    }

    final CuramValueList<UserSkillDtls> curamValueList1 = DynamicDataAccess
      .executeNsMulti(UserSkillDtls.class, null, false, sqlStr.toString());
    System.out.println(curamValueList1.size());

    registerPersonCreateCase("111111", 111111);
    setUpUrgentFlagData(111111, BDMURGENTFLAGTYPE.MPENQUIRY,
      Date.getCurrentDate());

    createUserSkillData("mason", SKILLLEVEL.PTM, SKILLTYPE.VSG01, 101,
      curam.codetable.LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);

    createTaskData(101, 80000, 111111); // BDMSKL8001 - VSG01 LN1

    final BDMTaskAllocationBatch bdmTaskAllocationBatchObj =
      BDMTaskAllocationBatchFactory.newInstance();

    final BDMTaskAllocationBatchKey bdmTaskAllocationBatchKey =
      new BDMTaskAllocationBatchKey();
    bdmTaskAllocationBatchKey.batchMode = "ptm";

    bdmTaskAllocationBatchObj.process(bdmTaskAllocationBatchKey);

    final TaskKey taskKey = new TaskKey();
    taskKey.taskID = 101;

    final TaskAssignmentDtlsList taskAssignmentDtlsList =
      TaskAssignmentFactory.newInstance().searchAssignmentsByTaskID(taskKey);

    assertTrue(taskAssignmentDtlsList.dtls.size() > 0);
    assertTrue(
      taskAssignmentDtlsList.dtls.item(0).relatedName.equals("mason"));

  }

  @Test
  public void testBDMVSGTaskAllocation_PTM_mutipleTask_taskDeadline()
    throws Exception {

    // -------------------------------------------------------
    // Set up user data
    // -------------------------------------------------------
    final UserSkill userSkillObj = UserSkillFactory.newInstance();
    final UserSkillDtls userSkillDtls = new UserSkillDtls();
    final StringBuffer sqlStr = new StringBuffer();
    sqlStr.append(
      "SELECT USERSKILLID INTO :userSkillID FROM USERSKILL WHERE RECORDSTATUS ='RST1'");
    final CuramValueList<UserSkillDtls> curamValueList = DynamicDataAccess
      .executeNsMulti(UserSkillDtls.class, null, false, sqlStr.toString());
    System.out.println(curamValueList.size());
    UserSkillKey userSkillKey = null;
    CancelUserSkillDetails cancelUserSkillDetails = null;
    for (int i = 0; i < curamValueList.size(); i++) {
      userSkillKey = new UserSkillKey();
      userSkillKey.userSkillID = curamValueList.item(i).userSkillID;
      cancelUserSkillDetails = new CancelUserSkillDetails();
      cancelUserSkillDetails.recordStatus = "RST2";
      cancelUserSkillDetails.versionNo = 1;
      userSkillObj.cancel(userSkillKey, cancelUserSkillDetails);

    }

    final CuramValueList<UserSkillDtls> curamValueList1 = DynamicDataAccess
      .executeNsMulti(UserSkillDtls.class, null, false, sqlStr.toString());
    System.out.println(curamValueList1.size());

    registerPersonCreateCase("111111", 111111);
    registerPersonCreateCase("111112", 111112);
    registerPersonCreateCase("111113", 111113);

    createTaskDataDeadline(101, 80000, 111111, 36, 0, 0); // BDMSKL8001 - VSG01
                                                          // LN1

    createTaskDataDeadline(102, 80000, 111112, 20, 0, 0); // BDMSKL8001 - VSG01

    createTaskDataDeadline(103, 80000, 111113, 50, 0, 0); // BDMSKL8001 -
                                                          // VSG01
    // LN1

    createUserSkillData("mason", SKILLLEVEL.PTM, SKILLTYPE.VSG01, 101,
      curam.codetable.LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);

    final BDMTaskAllocationBatch bdmTaskAllocationBatchObj =
      BDMTaskAllocationBatchFactory.newInstance();

    final BDMTaskAllocationBatchKey bdmTaskAllocationBatchKey =
      new BDMTaskAllocationBatchKey();
    bdmTaskAllocationBatchKey.batchMode = "ptm";

    bdmTaskAllocationBatchObj.process(bdmTaskAllocationBatchKey);

    final UserTasksByDueDatePriorityStatusKey tasksByDueDatePriorityStatusKey =
      new UserTasksByDueDatePriorityStatusKey();
    tasksByDueDatePriorityStatusKey.relatedName = "mason";
    final TasksByDueDateForUserDetailsList byDueDateForUserDetailsList =
      TaskAssignmentFactory.newInstance()
        .searchUserTasksDueByDatesPriorityStatus(
          tasksByDueDatePriorityStatusKey);

    assertTrue(byDueDateForUserDetailsList.dtls.size() > 0);

  }

  /**
   * Test if user account is disabled then task will not be assigned to that
   * user
   *
   * @throws Exception
   */
  @Test
  public void testBDMVSGTaskAllocation_Stage1Flow2() throws Exception {

    // -------------------------------------------------------
    // Set up user data
    // -------------------------------------------------------
    final UserSkill userSkillObj = UserSkillFactory.newInstance();
    final UserSkillDtls userSkillDtls = new UserSkillDtls();
    final StringBuffer sqlStr = new StringBuffer();
    sqlStr.append(
      "SELECT USERSKILLID INTO :userSkillID FROM USERSKILL WHERE RECORDSTATUS ='RST1'");
    final CuramValueList<UserSkillDtls> curamValueList = DynamicDataAccess
      .executeNsMulti(UserSkillDtls.class, null, false, sqlStr.toString());
    System.out.println(curamValueList.size());
    UserSkillKey userSkillKey = null;
    CancelUserSkillDetails cancelUserSkillDetails = null;
    for (int i = 0; i < curamValueList.size(); i++) {
      userSkillKey = new UserSkillKey();
      userSkillKey.userSkillID = curamValueList.item(i).userSkillID;
      cancelUserSkillDetails = new CancelUserSkillDetails();
      cancelUserSkillDetails.recordStatus = "RST2";
      cancelUserSkillDetails.versionNo = 1;
      userSkillObj.cancel(userSkillKey, cancelUserSkillDetails);

    }

    final CuramValueList<UserSkillDtls> curamValueList1 = DynamicDataAccess
      .executeNsMulti(UserSkillDtls.class, null, false, sqlStr.toString());
    System.out.println(curamValueList1.size());

    registerPersonCreateCase("111111", 111111);
    setUpUrgentFlagData(111111, BDMURGENTFLAGTYPE.MPENQUIRY,
      Date.getCurrentDate());

    createUserSkillData("mason", SKILLLEVEL.PRIMARY, SKILLTYPE.VSG01, 101,
      curam.codetable.LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);

    createTaskData(101, 80000, 111111); // BDMSKL8001 - VSG01 LN1

    // disable user
    final Organization organizationObj = OrganizationFactory.newInstance();
    final UserStatusDetails_fo details_fo = new UserStatusDetails_fo();
    details_fo.details.userName = "mason";
    details_fo.details.details.accountEnabled = false;
    final UserAccess userAccessObj = UserAccessFactory.newInstance();
    final UsersKey userKey = new UsersKey();
    userKey.userName = "mason";
    details_fo.details.details.versionNo =
      userAccessObj.readUserDetails(userKey).versionNo;
    organizationObj.disableUser(details_fo);

    final BDMTaskAllocationBatch bdmTaskAllocationBatchObj =
      BDMTaskAllocationBatchFactory.newInstance();

    final BDMTaskAllocationBatchKey bdmTaskAllocationBatchKey =
      new BDMTaskAllocationBatchKey();
    bdmTaskAllocationBatchKey.batchMode = "stage1";

    bdmTaskAllocationBatchObj.process(bdmTaskAllocationBatchKey);

    final TaskIDRelatedIDAndTypeKey idRelatedIDAndTypeKey =
      new TaskIDRelatedIDAndTypeKey();
    idRelatedIDAndTypeKey.taskID = 101;
    idRelatedIDAndTypeKey.assigneeType = ASSIGNEETYPE.USER;
    idRelatedIDAndTypeKey.relatedName = "mason";

    final CountDetails countDetails = TaskAssignmentFactory.newInstance()
      .isTaskAssignedToUser(idRelatedIDAndTypeKey);

    assertTrue(countDetails.numberOfRecords == 0);

  }

  /**
   * Task assigned to each skill level Primary/Secondary/tertiary
   *
   * @throws Exception
   */
  @Test
  public void testBDMVSGTaskAllocation_Stage1Flow1() throws Exception {

    // -------------------------------------------------------
    // Set up user data
    // -------------------------------------------------------
    final UserSkill userSkillObj = UserSkillFactory.newInstance();
    final UserSkillDtls userSkillDtls = new UserSkillDtls();
    final StringBuffer sqlStr = new StringBuffer();
    sqlStr.append(
      "SELECT USERSKILLID INTO :userSkillID FROM USERSKILL WHERE RECORDSTATUS ='RST1'");
    final CuramValueList<UserSkillDtls> curamValueList = DynamicDataAccess
      .executeNsMulti(UserSkillDtls.class, null, false, sqlStr.toString());
    System.out.println(curamValueList.size());
    UserSkillKey userSkillKey = null;
    CancelUserSkillDetails cancelUserSkillDetails = null;
    for (int i = 0; i < curamValueList.size(); i++) {
      userSkillKey = new UserSkillKey();
      userSkillKey.userSkillID = curamValueList.item(i).userSkillID;
      cancelUserSkillDetails = new CancelUserSkillDetails();
      cancelUserSkillDetails.recordStatus = "RST2";
      cancelUserSkillDetails.versionNo = 1;
      userSkillObj.cancel(userSkillKey, cancelUserSkillDetails);

    }

    final CuramValueList<UserSkillDtls> curamValueList1 = DynamicDataAccess
      .executeNsMulti(UserSkillDtls.class, null, false, sqlStr.toString());
    System.out.println(curamValueList1.size());

    registerPersonCreateCase("111111", 111111);
    setUpUrgentFlagData(111111, BDMURGENTFLAGTYPE.MPENQUIRY,
      Date.getCurrentDate());
    // create PRIMARY User skill type and level
    createUserSkillData("mason", SKILLLEVEL.PRIMARY, SKILLTYPE.VSG01, 101,
      curam.codetable.LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);
    createUserSkillData("clive", SKILLLEVEL.SECONDARY, SKILLTYPE.VSG02, 102,
      curam.codetable.LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);
    createUserSkillData("kevin", SKILLLEVEL.TERTIARY, SKILLTYPE.VSG03, 103,
      curam.codetable.LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);

    createTaskData(101, 80000, 111111); // BDMSKL8001 - VSG01 LN1
    createTaskData(102, 80001, 111111); // BDMSKL8002 - VSG02 LN1
    createTaskData(103, 80002, 111111); // BDMSKL8003 - VSG03 LN1

    final BDMTaskAllocationBatch bdmTaskAllocationBatchObj =
      BDMTaskAllocationBatchFactory.newInstance();

    final BDMTaskAllocationBatchKey bdmTaskAllocationBatchKey =
      new BDMTaskAllocationBatchKey();
    bdmTaskAllocationBatchKey.batchMode = "stage1";

    bdmTaskAllocationBatchObj.process(bdmTaskAllocationBatchKey);

    final TaskKey taskKey = new TaskKey();
    taskKey.taskID = 101;

    TaskAssignmentDtlsList taskAssignmentDtlsList =
      TaskAssignmentFactory.newInstance().searchAssignmentsByTaskID(taskKey);
    assertTrue(taskAssignmentDtlsList.dtls.size() == 1);

    taskKey.taskID = 102;
    taskAssignmentDtlsList =
      TaskAssignmentFactory.newInstance().searchAssignmentsByTaskID(taskKey);
    assertTrue(taskAssignmentDtlsList.dtls.size() == 1);

    taskKey.taskID = 103;
    taskAssignmentDtlsList =
      TaskAssignmentFactory.newInstance().searchAssignmentsByTaskID(taskKey);
    assertTrue(taskAssignmentDtlsList.dtls.size() == 1);

  }

  /**
   * This method test maximum task assigned to agent.
   *
   * @throws Exception
   */
  // @Test
  // public void testBDMVSGTaskAllocation_Stage1Flow_MaxTaskCount()
  // throws Exception {
  //
  // // -------------------------------------------------------
  // // Set up user data
  // // -------------------------------------------------------
  // final UserSkill userSkillObj = UserSkillFactory.newInstance();
  // final UserSkillDtls userSkillDtls = new UserSkillDtls();
  // final StringBuffer sqlStr = new StringBuffer();
  // sqlStr.append(
  // "SELECT USERSKILLID INTO :userSkillID FROM USERSKILL WHERE RECORDSTATUS
  // ='RST1'");
  // final CuramValueList<UserSkillDtls> curamValueList = DynamicDataAccess
  // .executeNsMulti(UserSkillDtls.class, null, false, sqlStr.toString());
  //
  // UserSkillKey userSkillKey = null;
  // CancelUserSkillDetails cancelUserSkillDetails = null;
  // for (int i = 0; i < curamValueList.size(); i++) {
  // userSkillKey = new UserSkillKey();
  // userSkillKey.userSkillID = curamValueList.item(i).userSkillID;
  // cancelUserSkillDetails = new CancelUserSkillDetails();
  // cancelUserSkillDetails.recordStatus = "RST2";
  // cancelUserSkillDetails.versionNo = 1;
  // userSkillObj.cancel(userSkillKey, cancelUserSkillDetails);
  //
  // }
  //
  // final CuramValueList<UserSkillDtls> curamValueList1 = DynamicDataAccess
  // .executeNsMulti(UserSkillDtls.class, null, false, sqlStr.toString());
  //
  // registerPersonCreateCase("111111", 111111);
  // setUpUrgentFlagData(111111, BDMURGENTFLAGTYPE.MPENQUIRY,
  // Date.getCurrentDate());
  // registerPersonCreateCase("111112", 111112);
  // setUpUrgentFlagData(111112, BDMURGENTFLAGTYPE.MPENQUIRY,
  // Date.getCurrentDate());
  // registerPersonCreateCase("111113", 111113);
  // setUpUrgentFlagData(111113, BDMURGENTFLAGTYPE.MPENQUIRY,
  // Date.getCurrentDate());
  // registerPersonCreateCase("111114", 111114);
  // setUpUrgentFlagData(111114, BDMURGENTFLAGTYPE.MPENQUIRY,
  // Date.getCurrentDate());
  // registerPersonCreateCase("111115", 111115);
  // setUpUrgentFlagData(111115, BDMURGENTFLAGTYPE.MPENQUIRY,
  // Date.getCurrentDate());
  // registerPersonCreateCase("111116", 111116);
  // setUpUrgentFlagData(111116, BDMURGENTFLAGTYPE.MPENQUIRY,
  // Date.getCurrentDate());
  //
  // // create PRIMARY User skill type and level
  // createUserSkillData("mason", SKILLLEVEL.PRIMARY, SKILLTYPE.VSG01, 101,
  // curam.codetable.LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);
  //
  // createTaskData(101, 80000, 111111);
  // createTaskData(102, 80000, 111112);
  // createTaskData(103, 80000, 111113);
  // createTaskData(104, 80000, 111114);
  // createTaskData(105, 80000, 111115);
  // createTaskData(106, 80000, 111116);
  //
  // final BDMTaskAllocationBatch bdmTaskAllocationBatchObj =
  // BDMTaskAllocationBatchFactory.newInstance();
  //
  // final BDMTaskAllocationBatchKey bdmTaskAllocationBatchKey =
  // new BDMTaskAllocationBatchKey();
  // bdmTaskAllocationBatchKey.batchMode = "stage1";
  //
  // bdmTaskAllocationBatchObj.process(bdmTaskAllocationBatchKey);
  //
  // final TaskUserNameStatusAndRestartDateTimeKey nameStatusAndRestartDateTimeKey
  // =
  // new TaskUserNameStatusAndRestartDateTimeKey();
  // nameStatusAndRestartDateTimeKey.relatedName = "mason";
  // nameStatusAndRestartDateTimeKey.status = TASKSTATUS.NOTSTARTED;
  //
  // CountDetails countDetails = TaskAssignmentFactory.newInstance()
  // .countOpenTaskAssignByUser(nameStatusAndRestartDateTimeKey);
  // assertTrue(countDetails.numberOfRecords == 5);
  //
  // createUserSkillData("clive", SKILLLEVEL.PRIMARY, SKILLTYPE.VSG01, 102,
  // curam.codetable.LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);
  //
  // bdmTaskAllocationBatchObj.process(bdmTaskAllocationBatchKey);
  //
  // nameStatusAndRestartDateTimeKey.relatedName = "clive";
  // nameStatusAndRestartDateTimeKey.status = TASKSTATUS.NOTSTARTED;
  //
  // countDetails = TaskAssignmentFactory.newInstance()
  // .countOpenTaskAssignByUser(nameStatusAndRestartDateTimeKey);
  // assertTrue(countDetails.numberOfRecords == 1);
  //
  // }

  /**
   *
   *
   * @throws Exception
   */
  @Test
  public void testBDMVSGTaskAllocation_Stage1Flow_UserTaskCount()
    throws Exception {

    // -------------------------------------------------------
    // Set up user data
    // -------------------------------------------------------
    final UserSkill userSkillObj = UserSkillFactory.newInstance();
    final UserSkillDtls userSkillDtls = new UserSkillDtls();
    final StringBuffer sqlStr = new StringBuffer();
    sqlStr.append(
      "SELECT USERSKILLID INTO :userSkillID FROM USERSKILL WHERE RECORDSTATUS ='RST1'");
    final CuramValueList<UserSkillDtls> curamValueList = DynamicDataAccess
      .executeNsMulti(UserSkillDtls.class, null, false, sqlStr.toString());

    UserSkillKey userSkillKey = null;
    CancelUserSkillDetails cancelUserSkillDetails = null;
    for (int i = 0; i < curamValueList.size(); i++) {
      userSkillKey = new UserSkillKey();
      userSkillKey.userSkillID = curamValueList.item(i).userSkillID;
      cancelUserSkillDetails = new CancelUserSkillDetails();
      cancelUserSkillDetails.recordStatus = "RST2";
      cancelUserSkillDetails.versionNo = 1;
      userSkillObj.cancel(userSkillKey, cancelUserSkillDetails);

    }

    final CuramValueList<UserSkillDtls> curamValueList1 = DynamicDataAccess
      .executeNsMulti(UserSkillDtls.class, null, false, sqlStr.toString());

    registerPersonCreateCase("111111", 111111);
    setUpUrgentFlagData(111111, BDMURGENTFLAGTYPE.MPENQUIRY,
      Date.getCurrentDate());
    registerPersonCreateCase("111112", 111112);
    setUpUrgentFlagData(111112, BDMURGENTFLAGTYPE.MPENQUIRY,
      Date.getCurrentDate());
    registerPersonCreateCase("111113", 111113);
    setUpUrgentFlagData(111113, BDMURGENTFLAGTYPE.MPENQUIRY,
      Date.getCurrentDate());
    registerPersonCreateCase("111114", 111114);
    setUpUrgentFlagData(111114, BDMURGENTFLAGTYPE.MPENQUIRY,
      Date.getCurrentDate());
    registerPersonCreateCase("111115", 111115);
    setUpUrgentFlagData(111115, BDMURGENTFLAGTYPE.MPENQUIRY,
      Date.getCurrentDate());
    registerPersonCreateCase("111116", 111116);
    setUpUrgentFlagData(111116, BDMURGENTFLAGTYPE.MPENQUIRY,
      Date.getCurrentDate());

    // create PRIMARY User skill type and level
    createUserSkillData("mason", SKILLLEVEL.PRIMARY, SKILLTYPE.VSG01, 101,
      curam.codetable.LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);

    createTaskData(101, 80000, 111111);
    createTaskData(102, 80000, 111112);
    createTaskData(103, 80000, 111113);
    createTaskData(104, 80000, 111114);
    createTaskData(105, 80000, 111115);
    createTaskData(106, 80000, 111116);

    final BDMTaskAllocationBatch bdmTaskAllocationBatchObj =
      BDMTaskAllocationBatchFactory.newInstance();

    final BDMTaskAllocationBatchKey bdmTaskAllocationBatchKey =
      new BDMTaskAllocationBatchKey();
    bdmTaskAllocationBatchKey.batchMode = "stage1";

    bdmTaskAllocationBatchObj.process(bdmTaskAllocationBatchKey);

    final TaskUserNameStatusAndRestartDateTimeKey nameStatusAndRestartDateTimeKey =
      new TaskUserNameStatusAndRestartDateTimeKey();
    nameStatusAndRestartDateTimeKey.relatedName = "mason";
    nameStatusAndRestartDateTimeKey.status = TASKSTATUS.NOTSTARTED;

    final CountDetails countDetails = TaskAssignmentFactory.newInstance()
      .countOpenTaskAssignByUser(nameStatusAndRestartDateTimeKey);
    assertTrue(countDetails.numberOfRecords == 5);

  }

  @Test
  public void testBDMVSGTaskAllocation_Stage1Flow_ErrorQueue()
    throws Exception {

    // -------------------------------------------------------
    // Set up user data
    // -------------------------------------------------------
    final UserSkill userSkillObj = UserSkillFactory.newInstance();
    final UserSkillDtls userSkillDtls = new UserSkillDtls();
    final StringBuffer sqlStr = new StringBuffer();
    sqlStr.append(
      "SELECT USERSKILLID INTO :userSkillID FROM USERSKILL WHERE RECORDSTATUS ='RST1'");
    final CuramValueList<UserSkillDtls> curamValueList = DynamicDataAccess
      .executeNsMulti(UserSkillDtls.class, null, false, sqlStr.toString());
    System.out.println(curamValueList.size());
    UserSkillKey userSkillKey = null;
    CancelUserSkillDetails cancelUserSkillDetails = null;
    for (int i = 0; i < curamValueList.size(); i++) {
      userSkillKey = new UserSkillKey();
      userSkillKey.userSkillID = curamValueList.item(i).userSkillID;
      cancelUserSkillDetails = new CancelUserSkillDetails();
      cancelUserSkillDetails.recordStatus = "RST2";
      cancelUserSkillDetails.versionNo = 1;
      userSkillObj.cancel(userSkillKey, cancelUserSkillDetails);

    }

    final CuramValueList<UserSkillDtls> curamValueList1 = DynamicDataAccess
      .executeNsMulti(UserSkillDtls.class, null, false, sqlStr.toString());
    System.out.println(curamValueList1.size());

    registerPersonCreateCase("111111", 111111);
    setUpUrgentFlagData(111111, BDMURGENTFLAGTYPE.MPENQUIRY,
      Date.getCurrentDate());

    createUserSkillData("mason", SKILLLEVEL.PRIMARY, SKILLTYPE.VSG01, 101,
      curam.codetable.LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);

    createTaskData(101, 0, 111111); // BDMSKL8001 - VSG01 LN1

    final BDMTaskAllocationBatch bdmTaskAllocationBatchObj =
      BDMTaskAllocationBatchFactory.newInstance();

    final BDMTaskAllocationBatchKey bdmTaskAllocationBatchKey =
      new BDMTaskAllocationBatchKey();
    bdmTaskAllocationBatchKey.batchMode = "stage1";

    bdmTaskAllocationBatchObj.process(bdmTaskAllocationBatchKey);

    final TaskKey taskKey = new TaskKey();
    taskKey.taskID = 101;

    final TaskAssignmentDtlsList taskAssignmentDtlsList =
      TaskAssignmentFactory.newInstance().searchAssignmentsByTaskID(taskKey);

    assertTrue(taskAssignmentDtlsList.dtls.size() > 0);
    // assert task is assigned to error queue
    assertTrue(taskAssignmentDtlsList.dtls.item(0).relatedID == 80020);

  }

  @Test
  public void testBDMVSGTaskAllocation_Stage1Flow_workingPattern()
    throws Exception {

    // -------------------------------------------------------
    // Set up user data
    // -------------------------------------------------------
    final UserSkill userSkillObj = UserSkillFactory.newInstance();
    final UserSkillDtls userSkillDtls = new UserSkillDtls();
    final StringBuffer sqlStr = new StringBuffer();
    sqlStr.append(
      "SELECT USERSKILLID INTO :userSkillID FROM USERSKILL WHERE RECORDSTATUS ='RST1'");
    final CuramValueList<UserSkillDtls> curamValueList = DynamicDataAccess
      .executeNsMulti(UserSkillDtls.class, null, false, sqlStr.toString());

    UserSkillKey userSkillKey = null;
    CancelUserSkillDetails cancelUserSkillDetails = null;
    for (int i = 0; i < curamValueList.size(); i++) {
      userSkillKey = new UserSkillKey();
      userSkillKey.userSkillID = curamValueList.item(i).userSkillID;
      cancelUserSkillDetails = new CancelUserSkillDetails();
      cancelUserSkillDetails.recordStatus = "RST2";
      cancelUserSkillDetails.versionNo = 1;
      userSkillObj.cancel(userSkillKey, cancelUserSkillDetails);

    }

    final CuramValueList<UserSkillDtls> curamValueList1 = DynamicDataAccess
      .executeNsMulti(UserSkillDtls.class, null, false, sqlStr.toString());

    registerPersonCreateCase("111111", 111111);
    setUpUrgentFlagData(111111, BDMURGENTFLAGTYPE.MPENQUIRY,
      Date.getCurrentDate());

    createUserSkillData("mason", SKILLLEVEL.PRIMARY, SKILLTYPE.VSG01, 101,
      curam.codetable.LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);

    createTaskData(101, 80000, 111111); // BDMSKL8001 - VSG01 LN1

    final BDMTaskAllocationBatch bdmTaskAllocationBatchObj =
      BDMTaskAllocationBatchFactory.newInstance();

    final BDMTaskAllocationBatchKey bdmTaskAllocationBatchKey =
      new BDMTaskAllocationBatchKey();
    bdmTaskAllocationBatchKey.batchMode = "stage1";

    // read working pattern for user
    final WorkingPattern workingPatternObj =
      WorkingPatternFactory.newInstance();
    final UsersKey usersKey = new UsersKey();
    usersKey.userName = "mason";
    final WorkingPatternDtlsList workingPatternDtlsList =
      workingPatternObj.searchByUserName(usersKey);
    final int listSize = workingPatternDtlsList.dtls.size();
    final WorkingPatternKey workingPatternKey = new WorkingPatternKey();

    for (int cnt = 0; cnt < listSize; cnt++) {
      workingPatternKey.workingPatternID =
        workingPatternDtlsList.dtls.item(cnt).workingPatternID;
      final WorkingPatternDtls workingPatternDtls =
        workingPatternObj.read(workingPatternKey);
      workingPatternDtls.recordStatusCode = RECORDSTATUS.CANCELLED;
      workingPatternObj.modify(workingPatternKey, workingPatternDtls);
    }

    bdmTaskAllocationBatchObj.process(bdmTaskAllocationBatchKey);

    final TaskKey taskKey = new TaskKey();
    taskKey.taskID = 101;

    final TaskAssignmentDtlsList taskAssignmentDtlsList =
      TaskAssignmentFactory.newInstance().searchAssignmentsByTaskID(taskKey);

    assertTrue(taskAssignmentDtlsList.dtls.size() > 0);
    assertTrue(taskAssignmentDtlsList.dtls.item(0).relatedID == 80013);

  }

  /**
   * This method will test batch for 5000 tasks assigned to 5000 cases.
   *
   * @throws Exception
   */
  // @Test
  // public void testBDMVSGTaskAllocation_Stage1Flow_performanceTest()
  // throws Exception {
  //
  // // -------------------------------------------------------
  // // Set up user data
  // // -------------------------------------------------------
  // final UserSkill userSkillObj = UserSkillFactory.newInstance();
  // final UserSkillDtls userSkillDtls = new UserSkillDtls();
  // final StringBuffer sqlStr = new StringBuffer();
  // sqlStr.append(
  // "SELECT USERSKILLID INTO :userSkillID FROM USERSKILL WHERE RECORDSTATUS
  // ='RST1'");
  // final CuramValueList<UserSkillDtls> curamValueList = DynamicDataAccess
  // .executeNsMulti(UserSkillDtls.class, null, false, sqlStr.toString());
  // System.out.println(curamValueList.size());
  // UserSkillKey userSkillKey = null;
  // CancelUserSkillDetails cancelUserSkillDetails = null;
  // for (int i = 0; i < curamValueList.size(); i++) {
  // userSkillKey = new UserSkillKey();
  // userSkillKey.userSkillID = curamValueList.item(i).userSkillID;
  // cancelUserSkillDetails = new CancelUserSkillDetails();
  // cancelUserSkillDetails.recordStatus = "RST2";
  // cancelUserSkillDetails.versionNo = 1;
  // userSkillObj.cancel(userSkillKey, cancelUserSkillDetails);
  //
  // }
  //
  // final WorkingPattern workingPatternObj =
  // WorkingPatternFactory.newInstance();
  // final StringBuffer swkrqlStr = new StringBuffer();
  // swkrqlStr.append(
  // "SELECT WORKINGPATTERNID INTO :workingPatternID FROM WorkingPattern");
  // final CuramValueList<WorkingPatternDtls> wkrValueList =
  // DynamicDataAccess.executeNsMulti(WorkingPatternDtls.class, null, false,
  // swkrqlStr.toString());
  // WorkingPatternKey workingPatternKey = null;
  // for (int i = 0; i < wkrValueList.size(); i++) {
  // workingPatternKey = new WorkingPatternKey();
  // workingPatternKey.workingPatternID =
  // wkrValueList.item(i).workingPatternID;
  // workingPatternObj.remove(workingPatternKey);
  //
  // }
  //
  // final TaskUserNameStatusAndRestartDateTimeKey nameStatusAndRestartDateTimeKey
  // =
  // new TaskUserNameStatusAndRestartDateTimeKey();
  // nameStatusAndRestartDateTimeKey.relatedName = "robert";
  // nameStatusAndRestartDateTimeKey.status = TASKSTATUS.NOTSTARTED;
  // CountDetails robertCountDetails = TaskAssignmentFactory.newInstance()
  // .countOpenTaskAssignByUser(nameStatusAndRestartDateTimeKey);
  // System.out.println("robert user task count before processing = "
  // + robertCountDetails.numberOfRecords);
  //
  // nameStatusAndRestartDateTimeKey.relatedName = "alice";
  // nameStatusAndRestartDateTimeKey.status = TASKSTATUS.NOTSTARTED;
  // CountDetails aliceCountDetails = TaskAssignmentFactory.newInstance()
  // .countOpenTaskAssignByUser(nameStatusAndRestartDateTimeKey);
  // System.out.println("alice user task count before processing = "
  // + aliceCountDetails.numberOfRecords);
  //
  // nameStatusAndRestartDateTimeKey.relatedName = "kevin";
  // nameStatusAndRestartDateTimeKey.status = TASKSTATUS.NOTSTARTED;
  // CountDetails kevinCountDetails = TaskAssignmentFactory.newInstance()
  // .countOpenTaskAssignByUser(nameStatusAndRestartDateTimeKey);
  // System.out.println("kevin user task count before processing = "
  // + kevinCountDetails.numberOfRecords);
  //
  // nameStatusAndRestartDateTimeKey.relatedName = "jean";
  // nameStatusAndRestartDateTimeKey.status = TASKSTATUS.NOTSTARTED;
  // CountDetails jeanCountDetails = TaskAssignmentFactory.newInstance()
  // .countOpenTaskAssignByUser(nameStatusAndRestartDateTimeKey);
  // System.out.println("jean user task count before processing = "
  // + jeanCountDetails.numberOfRecords);
  //
  // nameStatusAndRestartDateTimeKey.relatedName = "olivier";
  // nameStatusAndRestartDateTimeKey.status = TASKSTATUS.NOTSTARTED;
  // CountDetails olivierCountDetails = TaskAssignmentFactory.newInstance()
  // .countOpenTaskAssignByUser(nameStatusAndRestartDateTimeKey);
  // System.out.println("olivier user task count before processing = "
  // + olivierCountDetails.numberOfRecords);
  //
  // nameStatusAndRestartDateTimeKey.relatedName = "sally";
  // nameStatusAndRestartDateTimeKey.status = TASKSTATUS.NOTSTARTED;
  // CountDetails sallyCountDetails = TaskAssignmentFactory.newInstance()
  // .countOpenTaskAssignByUser(nameStatusAndRestartDateTimeKey);
  // System.out.println("sally user task count before processing = "
  // + sallyCountDetails.numberOfRecords);
  //
  // nameStatusAndRestartDateTimeKey.relatedName = "clive";
  // nameStatusAndRestartDateTimeKey.status = TASKSTATUS.NOTSTARTED;
  // CountDetails cliveCountDetails = TaskAssignmentFactory.newInstance()
  // .countOpenTaskAssignByUser(nameStatusAndRestartDateTimeKey);
  // System.out.println("clive user task count before processing = "
  // + cliveCountDetails.numberOfRecords);
  //
  // nameStatusAndRestartDateTimeKey.relatedName = "melissa";
  // nameStatusAndRestartDateTimeKey.status = TASKSTATUS.NOTSTARTED;
  // CountDetails melissaCountDetails = TaskAssignmentFactory.newInstance()
  // .countOpenTaskAssignByUser(nameStatusAndRestartDateTimeKey);
  // System.out.println("melissa user task count before processing = "
  // + melissaCountDetails.numberOfRecords);
  //
  // nameStatusAndRestartDateTimeKey.relatedName = "janice";
  // nameStatusAndRestartDateTimeKey.status = TASKSTATUS.NOTSTARTED;
  // CountDetails janiceCountDetails = TaskAssignmentFactory.newInstance()
  // .countOpenTaskAssignByUser(nameStatusAndRestartDateTimeKey);
  // System.out.println("janice user task count before processing = "
  // + janiceCountDetails.numberOfRecords);
  //
  // nameStatusAndRestartDateTimeKey.relatedName = "mason";
  // nameStatusAndRestartDateTimeKey.status = TASKSTATUS.NOTSTARTED;
  // CountDetails masonCountDetails = TaskAssignmentFactory.newInstance()
  // .countOpenTaskAssignByUser(nameStatusAndRestartDateTimeKey);
  // System.out.println("mason user task count before processing = "
  // + masonCountDetails.numberOfRecords);
  //
  // final CuramValueList<UserSkillDtls> curamValueList1 = DynamicDataAccess
  // .executeNsMulti(UserSkillDtls.class, null, false, sqlStr.toString());
  // System.out.println(curamValueList1.size());
  // final long personCrole1 = registerDynamicPerson("batchTestPerson1");
  // long caseRefernce = 1000000l;
  // long caseid = 0;
  // long taskID = 1;
  // for (int i = 1; i <= 500; i++) {
  // caseRefernce = caseRefernce + 100;
  // caseid =
  // registerDynamicCreateCase(personCrole1, caseRefernce, caseRefernce);
  // setUpUrgentFlagData(caseid, BDMURGENTFLAGTYPE.MPENQUIRY,
  // Date.getCurrentDate());
  // taskID = taskID + 1;
  // // createTaskData(taskID, 80000, caseid); // BDMSKL8001 - VSG01 LN1
  //
  // createDynaTaskData(taskID, 80000, caseid, personCrole1);
  // }
  //
  // final BizObjAssociation bizObjAssociationObj =
  // BizObjAssociationFactory.newInstance();
  // final BizObjectTypeKey bizObjectTypeKey = new BizObjectTypeKey();
  // bizObjectTypeKey.bizObjectID = personCrole1;
  // bizObjectTypeKey.bizObjectType = BUSINESSOBJECTTYPE.PERSON;
  // BizObjAssocSearchDetailsList bizObjAssocSearchDetailsList =
  // bizObjAssociationObj.searchByBizObjectTypeAndID(bizObjectTypeKey);
  //
  // System.out.println("Number of task linked to person batchTestPerson1="
  // + " " + bizObjAssocSearchDetailsList.dtls.size());
  //
  // final long personCrole2 = registerDynamicPerson("batchTestPerson2");
  // long caseRefernce2 = 2000000l;
  // long caseid2 = 0;
  // long taskID2 = taskID + 1;
  // for (int i = 1; i <= 500; i++) {
  // caseRefernce2 = caseRefernce2 + 100;
  // caseid2 =
  // registerDynamicCreateCase(personCrole2, caseRefernce2, caseRefernce2);
  // setUpUrgentFlagData(caseid2, BDMURGENTFLAGTYPE.MPENQUIRY,
  // Date.getCurrentDate());
  // taskID2 = taskID2 + 1;
  // // createTaskData(taskID2, 80001, caseid2); // BDMSKL8001 - VSG01 LN1
  // createDynaTaskData(taskID2, 80001, caseid2, personCrole2);
  // }
  //
  // bizObjectTypeKey.bizObjectID = personCrole2;
  // bizObjectTypeKey.bizObjectType = BUSINESSOBJECTTYPE.PERSON;
  // bizObjAssocSearchDetailsList =
  // bizObjAssociationObj.searchByBizObjectTypeAndID(bizObjectTypeKey);
  //
  // System.out.println("Number of task linked to person batchTestPerson2="
  // + " " + bizObjAssocSearchDetailsList.dtls.size());
  //
  // final long personCrole3 = registerDynamicPerson("batchTestPerson3");
  // long caseRefernce3 = 3000000l;
  // long caseid3 = 0;
  // long taskID3 = taskID2 + 1;
  // for (int i = 1; i <= 500; i++) {
  // caseRefernce3 = caseRefernce3 + 100;
  // caseid3 =
  // registerDynamicCreateCase(personCrole3, caseRefernce3, caseRefernce3);
  // setUpUrgentFlagData(caseid3, BDMURGENTFLAGTYPE.MPENQUIRY,
  // Date.getCurrentDate());
  // taskID3 = taskID3 + 1;
  // // createTaskData(taskID3, 80002, caseid3); // BDMSKL8001 - VSG01 LN1
  // createDynaTaskData(taskID3, 80002, caseid3, personCrole3);
  // }
  //
  // bizObjectTypeKey.bizObjectID = personCrole3;
  // bizObjectTypeKey.bizObjectType = BUSINESSOBJECTTYPE.PERSON;
  // bizObjAssocSearchDetailsList =
  // bizObjAssociationObj.searchByBizObjectTypeAndID(bizObjectTypeKey);
  // System.out.println("Number of task linked to person batchTestPerson3="
  // + " " + bizObjAssocSearchDetailsList.dtls.size());
  //
  // final long personCrole4 = registerDynamicPerson("batchTestPerson4");
  // long caseRefernce4 = 4000000l;
  // long caseid4 = 0;
  // long taskID4 = taskID3 + 1;
  // for (int i = 1; i <= 500; i++) {
  // caseRefernce4 = caseRefernce4 + 100;
  // caseid4 =
  // registerDynamicCreateCase(personCrole4, caseRefernce4, caseRefernce4);
  //
  // setUpUrgentFlagData(caseid4, BDMURGENTFLAGTYPE.MPENQUIRY,
  // Date.getCurrentDate());
  //
  // taskID4 = taskID4 + 1;
  // // createTaskData(taskID4, 80003, caseid4); // BDMSKL8001 - VSG01 LN1
  // createDynaTaskData(taskID4, 80003, caseid4, personCrole4);
  // }
  // bizObjectTypeKey.bizObjectID = personCrole4;
  // bizObjectTypeKey.bizObjectType = BUSINESSOBJECTTYPE.PERSON;
  // bizObjAssocSearchDetailsList =
  // bizObjAssociationObj.searchByBizObjectTypeAndID(bizObjectTypeKey);
  // System.out.println("Number of task linked to person batchTestPerson4="
  // + " " + bizObjAssocSearchDetailsList.dtls.size());
  //
  // final long personCrole5 = registerDynamicPerson("batchTestPerson5");
  // long caseRefernce5 = 5000000l;
  // long caseid5 = 0;
  // long taskID5 = taskID4 + 1;
  // for (int i = 1; i <= 500; i++) {
  // caseRefernce5 = caseRefernce5 + 100;
  // caseid5 =
  // registerDynamicCreateCase(personCrole5, caseRefernce5, caseRefernce5);
  //
  // setUpUrgentFlagData(caseid5, BDMURGENTFLAGTYPE.MPENQUIRY,
  // Date.getCurrentDate());
  //
  // taskID5 = taskID5 + 1;
  // // createTaskData(taskID5, 80004, caseid5); // BDMSKL8001 - VSG01 LN1
  // createDynaTaskData(taskID5, 80004, caseid5, personCrole5);
  // }
  //
  // bizObjectTypeKey.bizObjectID = personCrole5;
  // bizObjectTypeKey.bizObjectType = BUSINESSOBJECTTYPE.PERSON;
  // bizObjAssocSearchDetailsList =
  // bizObjAssociationObj.searchByBizObjectTypeAndID(bizObjectTypeKey);
  // System.out.println("Number of task linked to person batchTestPerson5="
  // + " " + bizObjAssocSearchDetailsList.dtls.size());
  //
  // final long personCrole6 = registerDynamicPerson("batchTestPerson6");
  // long caseRefernce6 = 6000000l;
  // long caseid6 = 0;
  // long taskID6 = taskID5 + 1;
  // for (int i = 1; i <= 500; i++) {
  // caseRefernce6 = caseRefernce6 + 100;
  // caseid6 =
  // registerDynamicCreateCase(personCrole6, caseRefernce6, caseRefernce6);
  //
  // // setUpUrgentFlagData(caseid6,
  // // BDMURGENTFLAGTYPE.MPENQUIRY,Date.getCurrentDate());
  //
  // taskID6 = taskID6 + 1;
  // // createTaskData(taskID6, 80005, caseid6); // BDMSKL8001 - VSG01 LN1
  // createDynaTaskData(taskID6, 80005, caseid6, personCrole6);
  // }
  // bizObjectTypeKey.bizObjectID = personCrole6;
  // bizObjectTypeKey.bizObjectType = BUSINESSOBJECTTYPE.PERSON;
  // bizObjAssocSearchDetailsList =
  // bizObjAssociationObj.searchByBizObjectTypeAndID(bizObjectTypeKey);
  // System.out.println("Number of task linked to person batchTestPerson6="
  // + " " + bizObjAssocSearchDetailsList.dtls.size());
  //
  // final long personCrole7 = registerDynamicPerson("batchTestPerson7");
  // long caseRefernce7 = 7000000l;
  // long caseid7 = 0;
  // long taskID7 = taskID6 + 1;
  // for (int i = 1; i <= 500; i++) {
  // caseRefernce7 = caseRefernce7 + 100;
  // caseid7 =
  // registerDynamicCreateCase(personCrole7, caseRefernce7, caseRefernce7);
  //
  // /*
  // * setUpUrgentFlagData(caseid7, BDMURGENTFLAGTYPE.MPENQUIRY,
  // * Date.getCurrentDate());
  // */
  //
  // taskID7 = taskID7 + 1;
  // // createTaskData(taskID7, 80006, caseid7); // BDMSKL8001 - VSG01 LN1
  // createDynaTaskData(taskID7, 80006, caseid7, personCrole7);
  // }
  // bizObjectTypeKey.bizObjectID = personCrole7;
  // bizObjectTypeKey.bizObjectType = BUSINESSOBJECTTYPE.PERSON;
  // bizObjAssocSearchDetailsList =
  // bizObjAssociationObj.searchByBizObjectTypeAndID(bizObjectTypeKey);
  // System.out.println("Number of task linked to person batchTestPerson7="
  // + " " + bizObjAssocSearchDetailsList.dtls.size());
  //
  // final long personCrole8 = registerDynamicPerson("batchTestPerson8");
  // long caseRefernce8 = 8000000l;
  // long caseid8 = 0;
  // long taskID8 = taskID7 + 1;
  // for (int i = 1; i <= 500; i++) {
  // caseRefernce8 = caseRefernce8 + 100;
  // caseid8 =
  // registerDynamicCreateCase(personCrole8, caseRefernce8, caseRefernce8);
  //
  // /*
  // * setUpUrgentFlagData(caseid8, BDMURGENTFLAGTYPE.MPENQUIRY,
  // * Date.getCurrentDate());
  // */
  //
  // taskID8 = taskID8 + 1;
  // // createTaskData(taskID8, 80007, caseid8); // BDMSKL8001 - VSG01 LN1
  // createDynaTaskData(taskID8, 80007, caseid8, personCrole8);
  // }
  // bizObjectTypeKey.bizObjectID = personCrole8;
  // bizObjectTypeKey.bizObjectType = BUSINESSOBJECTTYPE.PERSON;
  // bizObjAssocSearchDetailsList =
  // bizObjAssociationObj.searchByBizObjectTypeAndID(bizObjectTypeKey);
  // System.out.println("Number of task linked to person batchTestPerson8="
  // + " " + bizObjAssocSearchDetailsList.dtls.size());
  //
  // final long personCrole9 = registerDynamicPerson("batchTestPerson9");
  // long caseRefernce9 = 9000000l;
  // long caseid9 = 0;
  // long taskID9 = taskID8 + 1;
  // for (int i = 1; i <= 500; i++) {
  // caseRefernce9 = caseRefernce9 + 100;
  // caseid9 =
  // registerDynamicCreateCase(personCrole9, caseRefernce9, caseRefernce9);
  //
  // /*
  // * setUpUrgentFlagData(caseid9, BDMURGENTFLAGTYPE.MPENQUIRY,
  // * Date.getCurrentDate());
  // */
  //
  // taskID9 = taskID9 + 1;
  // // createTaskData(taskID9, 80008, caseid9); // BDMSKL8001 - VSG01 LN1
  // createDynaTaskData(taskID9, 80008, caseid9, personCrole9);
  // }
  // bizObjectTypeKey.bizObjectID = personCrole9;
  // bizObjectTypeKey.bizObjectType = BUSINESSOBJECTTYPE.PERSON;
  // bizObjAssocSearchDetailsList =
  // bizObjAssociationObj.searchByBizObjectTypeAndID(bizObjectTypeKey);
  // System.out.println("Number of task linked to person batchTestPerson9="
  // + " " + bizObjAssocSearchDetailsList.dtls.size());
  //
  // final long personCrole10 = registerDynamicPerson("batchTestPerson10");
  // long caseRefernce10 = 9100000l;
  // long caseid10 = 0;
  // long taskID10 = taskID9 + 1;
  // for (int i = 1; i <= 500; i++) {
  // caseRefernce10 = caseRefernce10 + 100;
  // caseid10 = registerDynamicCreateCase(personCrole10, caseRefernce10,
  // caseRefernce10);
  //
  // /*
  // * setUpUrgentFlagData(caseid10, BDMURGENTFLAGTYPE.MPENQUIRY,
  // * Date.getCurrentDate());
  // */
  //
  // taskID10 = taskID10 + 1;
  // // createTaskData(taskID10, 80009, caseid10); // BDMSKL8001 - VSG01 LN1
  // createDynaTaskData(taskID10, 80009, caseid10, personCrole10);
  // }
  // bizObjectTypeKey.bizObjectID = personCrole10;
  // bizObjectTypeKey.bizObjectType = BUSINESSOBJECTTYPE.PERSON;
  // bizObjAssocSearchDetailsList =
  // bizObjAssociationObj.searchByBizObjectTypeAndID(bizObjectTypeKey);
  // System.out.println("Number of task linked to person batchTestPerson10="
  // + " " + bizObjAssocSearchDetailsList.dtls.size());
  //
  // createDynaUserSkillData("robert", SKILLLEVEL.PRIMARY, SKILLTYPE.VSG01);
  // createDynaUserSkillData("robert", SKILLLEVEL.SECONDARY, SKILLTYPE.VSG02);
  // createDynaWorkingPattern("robert", 101, WORKINGHOURSTYPE.WORKINGHOURS);
  //
  // /*
  // * createUserSkillData("robert", SKILLLEVEL.PRIMARY, SKILLTYPE.VSG01, 101,
  // * LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);
  // * createUserSkillData("robert", SKILLLEVEL.SECONDARY, SKILLTYPE.VSG02, 102,
  // * LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);
  // */
  //
  // createDynaUserSkillData("alice", SKILLLEVEL.PRIMARY, SKILLTYPE.VSG01);
  // createDynaUserSkillData("alice", SKILLLEVEL.TERTIARY, SKILLTYPE.VSG03);
  // createDynaWorkingPattern("alice", 102, WORKINGHOURSTYPE.WORKINGHOURS);
  //
  // /*
  // * createUserSkillData("alice", SKILLLEVEL.PRIMARY, SKILLTYPE.VSG01, 103,
  // * LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);
  // * createUserSkillData("alice", SKILLLEVEL.TERTIARY, SKILLTYPE.VSG03, 104,
  // * LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);
  // */
  //
  // createDynaUserSkillData("kevin", SKILLLEVEL.PRIMARY, SKILLTYPE.VSG03);
  // createDynaUserSkillData("kevin", SKILLLEVEL.TERTIARY, SKILLTYPE.VSG10);
  // createDynaWorkingPattern("kevin", 103, WORKINGHOURSTYPE.WORKINGHOURS);
  //
  // /*
  // * createUserSkillData("kevin", SKILLLEVEL.PRIMARY, SKILLTYPE.VSG03, 105,
  // * LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);
  // * createUserSkillData("kevin", SKILLLEVEL.TERTIARY, SKILLTYPE.VSG10, 106,
  // * LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);
  // */
  //
  // createDynaUserSkillData("jean", SKILLLEVEL.PRIMARY, SKILLTYPE.VSG04);
  // createDynaUserSkillData("jean", SKILLLEVEL.SECONDARY, SKILLTYPE.VSG02);
  // createDynaWorkingPattern("jean", 104, WORKINGHOURSTYPE.WORKINGHOURS);
  //
  // /*
  // * createUserSkillData("jean", SKILLLEVEL.PRIMARY, SKILLTYPE.VSG04, 107,
  // * LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);
  // * createUserSkillData("jean", SKILLLEVEL.SECONDARY, SKILLTYPE.VSG02, 108,
  // * LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);
  // */
  //
  // createDynaUserSkillData("olivier", SKILLLEVEL.PRIMARY, SKILLTYPE.VSG05);
  // createDynaUserSkillData("olivier", SKILLLEVEL.SECONDARY, SKILLTYPE.VSG04);
  // createDynaWorkingPattern("olivier", 105, WORKINGHOURSTYPE.WORKINGHOURS);
  //
  // /*
  // * createUserSkillData("olivier", SKILLLEVEL.PRIMARY, SKILLTYPE.VSG05, 109,
  // * LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);
  // * createUserSkillData("olivier", SKILLLEVEL.SECONDARY, SKILLTYPE.VSG04,
  // * 110,
  // * LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);
  // */
  //
  // createDynaUserSkillData("sally", SKILLLEVEL.PTM, SKILLTYPE.VSG06);
  // createDynaUserSkillData("sally", SKILLLEVEL.TERTIARY, SKILLTYPE.VSG05);
  // createDynaWorkingPattern("sally", 106, WORKINGHOURSTYPE.WORKINGHOURS);
  //
  // /*
  // * createUserSkillData("sally", SKILLLEVEL.PTM, SKILLTYPE.VSG06, 111,
  // * LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);
  // * createUserSkillData("sally", SKILLLEVEL.TERTIARY, SKILLTYPE.VSG05, 112,
  // * LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);
  // */
  //
  // createDynaUserSkillData("clive", SKILLLEVEL.PRIMARY, SKILLTYPE.VSG07);
  // createDynaUserSkillData("clive", SKILLLEVEL.PTM, SKILLTYPE.VSG06);
  // createDynaWorkingPattern("clive", 107, WORKINGHOURSTYPE.WORKINGHOURS);
  //
  // /*
  // * createUserSkillData("clive", SKILLLEVEL.PRIMARY, SKILLTYPE.VSG07, 113,
  // * LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);
  // * createUserSkillData("clive", SKILLLEVEL.PTM, SKILLTYPE.VSG06, 114,
  // * LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);
  // */
  //
  // createDynaUserSkillData("melissa", SKILLLEVEL.PRIMARY, SKILLTYPE.VSG07);
  // createDynaUserSkillData("melissa", SKILLLEVEL.PTM, SKILLTYPE.VSG08);
  // createDynaWorkingPattern("melissa", 108, WORKINGHOURSTYPE.WORKINGHOURS);
  //
  // /*
  // * createUserSkillData("melissa", SKILLLEVEL.PRIMARY, SKILLTYPE.VSG07, 115,
  // * LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);
  // * createUserSkillData("melissa", SKILLLEVEL.PTM, SKILLTYPE.VSG08, 116,
  // * LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);
  // */
  //
  // createDynaUserSkillData("janice", SKILLLEVEL.PRIMARY, SKILLTYPE.VSG09);
  // createDynaUserSkillData("janice", SKILLLEVEL.SECONDARY, SKILLTYPE.VSG08);
  // createDynaWorkingPattern("janice", 109, WORKINGHOURSTYPE.WORKINGHOURS);
  //
  // /*
  // * createUserSkillData("janice", SKILLLEVEL.PRIMARY, SKILLTYPE.VSG09, 117,
  // * LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);
  // * createUserSkillData("janice", SKILLLEVEL.SECONDARY, SKILLTYPE.VSG08, 118,
  // * LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);
  // */
  //
  // createDynaUserSkillData("mason", SKILLLEVEL.PTM, SKILLTYPE.VSG10);
  // createDynaUserSkillData("mason", SKILLLEVEL.SECONDARY, SKILLTYPE.VSG09);
  // createDynaWorkingPattern("mason", 110, WORKINGHOURSTYPE.WORKINGHOURS);
  //
  // /*
  // * createUserSkillData("mason", SKILLLEVEL.PTM, SKILLTYPE.VSG10, 119,
  // * LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);
  // * createUserSkillData("mason", SKILLLEVEL.SECONDARY, SKILLTYPE.VSG09, 120,
  // * LANGUAGE.ENGLISH, WORKINGHOURSTYPE.WORKINGHOURS);
  // */
  //
  // final BDMTaskAllocationBatch bdmTaskAllocationBatchObj =
  // BDMTaskAllocationBatchFactory.newInstance();
  //
  // final BDMTaskAllocationBatchKey bdmTaskAllocationBatchKey =
  // new BDMTaskAllocationBatchKey();
  // // bdmTaskAllocationBatchKey.batchMode = "stage1";
  //
  // System.out
  // .println("Processing start time=" + DateTime.getCurrentDateTime());
  // bdmTaskAllocationBatchObj.process(bdmTaskAllocationBatchKey);
  // System.out
  // .println("Processing End time=" + DateTime.getCurrentDateTime());
  // nameStatusAndRestartDateTimeKey.relatedName = "robert";
  // nameStatusAndRestartDateTimeKey.status = TASKSTATUS.NOTSTARTED;
  // robertCountDetails = TaskAssignmentFactory.newInstance()
  // .countOpenTaskAssignByUser(nameStatusAndRestartDateTimeKey);
  // System.out.println("robert user task count after processing = "
  // + robertCountDetails.numberOfRecords);
  //
  // nameStatusAndRestartDateTimeKey.relatedName = "alice";
  // nameStatusAndRestartDateTimeKey.status = TASKSTATUS.NOTSTARTED;
  // aliceCountDetails = TaskAssignmentFactory.newInstance()
  // .countOpenTaskAssignByUser(nameStatusAndRestartDateTimeKey);
  // System.out.println("alice user task count after processing = "
  // + aliceCountDetails.numberOfRecords);
  //
  // nameStatusAndRestartDateTimeKey.relatedName = "kevin";
  // nameStatusAndRestartDateTimeKey.status = TASKSTATUS.NOTSTARTED;
  // kevinCountDetails = TaskAssignmentFactory.newInstance()
  // .countOpenTaskAssignByUser(nameStatusAndRestartDateTimeKey);
  // System.out.println("kevin user task count after processing = "
  // + kevinCountDetails.numberOfRecords);
  //
  // nameStatusAndRestartDateTimeKey.relatedName = "jean";
  // nameStatusAndRestartDateTimeKey.status = TASKSTATUS.NOTSTARTED;
  // jeanCountDetails = TaskAssignmentFactory.newInstance()
  // .countOpenTaskAssignByUser(nameStatusAndRestartDateTimeKey);
  // System.out.println("jean user task count after processing = "
  // + jeanCountDetails.numberOfRecords);
  //
  // nameStatusAndRestartDateTimeKey.relatedName = "olivier";
  // nameStatusAndRestartDateTimeKey.status = TASKSTATUS.NOTSTARTED;
  // olivierCountDetails = TaskAssignmentFactory.newInstance()
  // .countOpenTaskAssignByUser(nameStatusAndRestartDateTimeKey);
  // System.out.println("olivier user task count after processing = "
  // + olivierCountDetails.numberOfRecords);
  //
  // nameStatusAndRestartDateTimeKey.relatedName = "sally";
  // nameStatusAndRestartDateTimeKey.status = TASKSTATUS.NOTSTARTED;
  // sallyCountDetails = TaskAssignmentFactory.newInstance()
  // .countOpenTaskAssignByUser(nameStatusAndRestartDateTimeKey);
  // System.out.println("sally user task count after processing = "
  // + sallyCountDetails.numberOfRecords);
  //
  // nameStatusAndRestartDateTimeKey.relatedName = "clive";
  // nameStatusAndRestartDateTimeKey.status = TASKSTATUS.NOTSTARTED;
  // cliveCountDetails = TaskAssignmentFactory.newInstance()
  // .countOpenTaskAssignByUser(nameStatusAndRestartDateTimeKey);
  // System.out.println("clive user task count after processing = "
  // + cliveCountDetails.numberOfRecords);
  //
  // nameStatusAndRestartDateTimeKey.relatedName = "melissa";
  // nameStatusAndRestartDateTimeKey.status = TASKSTATUS.NOTSTARTED;
  // melissaCountDetails = TaskAssignmentFactory.newInstance()
  // .countOpenTaskAssignByUser(nameStatusAndRestartDateTimeKey);
  // System.out.println("melissa user task count after processing = "
  // + melissaCountDetails.numberOfRecords);
  //
  // nameStatusAndRestartDateTimeKey.relatedName = "janice";
  // nameStatusAndRestartDateTimeKey.status = TASKSTATUS.NOTSTARTED;
  // janiceCountDetails = TaskAssignmentFactory.newInstance()
  // .countOpenTaskAssignByUser(nameStatusAndRestartDateTimeKey);
  // System.out.println("janice user task count after processing = "
  // + janiceCountDetails.numberOfRecords);
  //
  // nameStatusAndRestartDateTimeKey.relatedName = "mason";
  // nameStatusAndRestartDateTimeKey.status = TASKSTATUS.NOTSTARTED;
  // masonCountDetails = TaskAssignmentFactory.newInstance()
  // .countOpenTaskAssignByUser(nameStatusAndRestartDateTimeKey);
  // System.out.println("mason user task count after processing = "
  // + masonCountDetails.numberOfRecords);
  //
  // }

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
   * Register person and create case
   *
   * @param caseReference
   * @param caseID
   * @throws AppException
   * @throws InformationalException
   */
  private long registerDynamicPerson(final String personName)
    throws AppException, InformationalException {

    final RegisterPerson registerPersonObj = new RegisterPerson("");
    final PersonDtls personDtls = registerPersonObj
      .registerDefault(personName, METHODOFDELIVERYEntry.CHEQUE);
    final long concernRoleID = personDtls.concernRoleID;
    return concernRoleID;

  }

  private long registerDynamicCreateCase(final long concernRoleID,
    final long caseReference, final long caseID)
    throws AppException, InformationalException {

    final CaseHeaderDtls chDtls = new CaseHeaderDtls();
    chDtls.appealIndicator = false;
    chDtls.caseReference = Long.toString(concernRoleID);
    chDtls.versionNo = 1;
    chDtls.concernRoleID = concernRoleID;
    chDtls.caseID = caseID;
    CaseHeaderFactory.newInstance().insert(chDtls);
    return chDtls.caseID;
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

  private void createDynaTaskData(final long commonID,
    final long bdmTaskClassificationID, final long caseID,
    final long personID) throws AppException, InformationalException {

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

    bizObjAssociationDtls = new BizObjAssociationDtls();
    bizObjAssociationDtls.bizObjAssocID = commonID + 200001;
    bizObjAssociationDtls.taskID = commonID;
    bizObjAssociationDtls.bizObjectID = personID;
    bizObjAssociationDtls.bizObjectType = BUSINESSOBJECTTYPE.PERSON;

    BizObjAssociationFactory.newInstance().insert(bizObjAssociationDtls);

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
   * Creates test data for task
   *
   * @throws AppException
   * @throws InformationalException
   */
  private void createTaskDataDeadline(final long commonID,
    final long bdmTaskClassificationID, final long caseID, final int hours,
    final int minutes, final int seconds)
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
      DateTime.getCurrentDateTime().addTime(hours, minutes, seconds);
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
      nextSundayDate().getDateTime().addTime(23, 30, 0);
    // workingPatternDtls.endDate = nextSundayDate();
    workingPatternDtls.versionNo = 1;

    workingPatternObj.insert(workingPatternDtls);

  }

  private void createDynaUserSkillData(final String userName,
    final String skillLevel, final String skillType)
    throws AppException, InformationalException {

    final UserSkillDtls userSkillDtls = new UserSkillDtls();
    userSkillDtls.userName = userName;
    userSkillDtls.recordStatus = "RST1";
    userSkillDtls.skillLevel = skillLevel; // SKILLLEVEL.SECONDARY;
    userSkillDtls.skillType = skillType; // SKILLTYPE.VSG02;
    userSkillDtls.versionNo = 1;

    UserSkillFactory.newInstance().insert(userSkillDtls);
  }

  private void createDynaWorkingPattern(final String userName,
    final long commonID, final String workingHoursType)
    throws AppException, InformationalException {

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
      nextSundayDate().getDateTime().addTime(23, 30, 0);
    // workingPatternDtls.endDate = nextSundayDate();
    workingPatternDtls.versionNo = 1;

    workingPatternObj.insert(workingPatternDtls);
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

  private void createTaskData_1(final long commonID,
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
    taskAssignmentDtls.relatedID = 80014;
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

}
