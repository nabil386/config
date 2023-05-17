package curam.bdm.test.ca.gc.bdm.facade.bdmworkallocation.impl;

import curam.ca.gc.bdm.entity.fact.BDMTaskFactory;
import curam.ca.gc.bdm.entity.struct.BDMTaskDtls;
import curam.ca.gc.bdm.entity.struct.BDMTaskKey;
import curam.ca.gc.bdm.facade.bdmworkallocation.fact.BDMWorkAllocationFactory;
import curam.ca.gc.bdm.facade.bdmworkallocation.intf.BDMWorkAllocation;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMStandardManualTaskDtls;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskBringForwardDetails;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskCategoryAndTypeDetails;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskSkillTypeKey;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskSkillTypeList;
import curam.ca.gc.bdm.sl.bdmworkallocation.fact.BDMWorkAllocationTaskFactory;
import curam.ca.gc.bdm.sl.bdmworkallocation.intf.BDMWorkAllocationTask;
import curam.ca.gc.bdm.sl.bdmworkallocation.struct.BDMDocumentTypeKey;
import curam.ca.gc.bdm.test.BDMBaseTest;
import curam.ca.gc.bdm.test.BDMTestConstants;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.codetable.BDMTASKTYPE;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.DOCUMENTTYPE;
import curam.codetable.SKILLTYPE;
import curam.codetable.TARGETITEMTYPE;
import curam.codetable.TASKPRIORITY;
import curam.codetable.TASKSTATUS;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.PersonRegistrationFactory;
import curam.core.impl.CuramConst;
import curam.core.intf.CaseHeader;
import curam.core.intf.PersonRegistration;
import curam.core.struct.ActiveCasesConcernRoleIDAndTypeKey;
import curam.core.struct.CaseHeaderDtlsList;
import curam.core.struct.PersonDtls;
import curam.core.struct.RegistrationIDDetails;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.DateTime;
import curam.util.workflow.fact.BizObjAssociationFactory;
import curam.util.workflow.fact.TaskFactory;
import curam.util.workflow.intf.BizObjAssociation;
import curam.util.workflow.intf.Task;
import curam.util.workflow.struct.BizObjAssocSearchDetailsList;
import curam.util.workflow.struct.BizObjectTypeKey;
import curam.util.workflow.struct.TaskDtls;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author hamed.mohammed
 *
 */
public class BDMWorkAllocationTest extends BDMBaseTest {

  // object created
  BDMWorkAllocation bdmWorkAllocation =
    BDMWorkAllocationFactory.newInstance();

  final BizObjAssociation bizObjAssociationObj =
    BizObjAssociationFactory.newInstance();

  BDMWorkAllocation kBdmWorkAllocation =
    BDMWorkAllocationFactory.newInstance();

  BDMWorkAllocationTask kBdmWorkAllocationTask =
    BDMWorkAllocationTaskFactory.newInstance();

  public BDMWorkAllocationTest() {

    super();
  }

  @Test
  public void testSearchBDMTaskSkillType()
    throws AppException, InformationalException {

    final BDMTaskSkillTypeKey bdmTaskSkillTypeKey = new BDMTaskSkillTypeKey();

    bdmTaskSkillTypeKey.skillType = SKILLTYPE.VSG01;

    BDMTaskSkillTypeList taskSkillTypeList = new BDMTaskSkillTypeList();

    taskSkillTypeList =
      bdmWorkAllocation.searchBDMTaskSkillType(bdmTaskSkillTypeKey);

    assertEquals(taskSkillTypeList.dtls.size(),
      BDMTestConstants.kOneRecordReturned);

    assertEquals(taskSkillTypeList.dtls.item(0).bdmTaskClassificationID,
      BDMTestConstants.kBDMTaskClassificationID);

  }

  @Test
  public void testCreateTaskWithVSG()
    throws AppException, InformationalException {

    final long taskID = getATaskID();
    final BDMStandardManualTaskDtls details = new BDMStandardManualTaskDtls();

    details.dtls.assignDtls.assignmentID = "test";
    details.dtls.assignDtls.assignType = TARGETITEMTYPE.USER;
    details.dtls.assignDtls.reserveToMeInd = BDMTestConstants.kTrue;
    details.dtls.taskDtls.deadlineTime =
      DateTime.getCurrentDateTime().addTime(72, 0, 0);
    details.dtls.taskDtls.subject = "Test comm 2";
    details.dtls.taskDtls.taskType =
      BDMTASKTYPE.ADDRESSCHANGE_DIRECTDEPOSIT_PWS;
    details.dtls.taskDtls.bdmTaskClassificationID =
      BDMTestConstants.kBDMTaskClassificationID;
    details.dtls.taskDtls.status = TASKSTATUS.NOTSTARTED;
    final String name = "Original Person";
    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;
    details.dtls.concerningDtls.caseID = pdcID;
    details.dtls.concerningDtls.caseParticipantRoleID =
      pdcOriginalPersonDetails.concernRoleID;
    details.dtls.concerningDtls.participantRoleID =
      pdcOriginalPersonDetails.concernRoleID;
    details.dtls.concerningDtls.participantType = "RL1";
    details.dtls.assign(pdcList);
    // details.dtls.concerningDtls.caseID =
    // icResult.createCaseResult.integratedCaseID;

    assertTrue(taskID != CuramConst.gkZero);

    final BDMTaskKey bdmTaskKey = new BDMTaskKey();
    bdmTaskKey.taskID = taskID;

    final BDMTaskDtls bdmTaskDtls =
      BDMTaskFactory.newInstance().read(bdmTaskKey);

    assertEquals(bdmTaskDtls.type,
      BDMTASKTYPE.ADDRESSCHANGE_DIRECTDEPOSIT_PWS);

    final BizObjectTypeKey bizObjectTypeKey = new BizObjectTypeKey();

    bizObjectTypeKey.bizObjectID = BDMTestConstants.kBDMTaskClassificationID;

    bizObjectTypeKey.bizObjectType = BUSINESSOBJECTTYPE.TASKSKILLTYPE;

    final BizObjAssocSearchDetailsList bizObjAssocSearchDetailsList =
      bizObjAssociationObj.searchByBizObjectTypeAndID(bizObjectTypeKey);

    kBdmWorkAllocation.createTaskWithVSG(details);
    assertEquals(bizObjAssocSearchDetailsList.dtls.item(0).taskID, taskID);

    assertTrue(bizObjAssocSearchDetailsList.dtls
      .item(0).bizObjAssocID != CuramConst.kLongZero);

  }

  public PDCPersonDetails createOriginalPDCPerson()
    throws AppException, InformationalException {

    final RegisterPerson registerPerson =
      new RegisterPerson("RegisterPerson");

    final curam.core.struct.PersonRegistrationDetails personRegistrationDetails =
      registerPerson.getPersonRegistrationDetails();

    final PersonRegistration personRegistration =
      PersonRegistrationFactory.newInstance();

    personRegistrationDetails.firstForename = "Original Person";
    final RegistrationIDDetails registrationIDDetails =
      personRegistration.registerPerson(personRegistrationDetails);

    final PersonDtls personDtls =
      registerPerson.readPerson(registrationIDDetails.concernRoleID);

    final PDCPersonDetails pdcPersonDetails = new PDCPersonDetails();
    pdcPersonDetails.assign(personRegistrationDetails);
    pdcPersonDetails.assign(personDtls);
    return pdcPersonDetails;

  }

  @Test
  public void testViewAssignmentForTask()
    throws AppException, InformationalException {

    final long taskID = getATaskID();

    final curam.core.sl.struct.TaskManagementTaskKey taskKey =
      new curam.core.sl.struct.TaskManagementTaskKey();

    taskKey.taskID = taskID;

    final Task task = TaskFactory.newInstance();
    final curam.util.workflow.struct.TaskKey taskKey2 =
      new curam.util.workflow.struct.TaskKey();
    taskKey2.taskID = taskID;
    final TaskDtls taskDtls = task.read(taskKey2);

    assertEquals(taskDtls.taskID, taskID);
    assertEquals(taskDtls.status, TASKSTATUS.NOTSTARTED);
    assertEquals(taskDtls.priority, TASKPRIORITY.NORMAL);

    final BDMTaskDtls bdmTaskDtls = getBDMTaskDetails(taskID);

    kBdmWorkAllocation.viewAssignmentForTask(taskKey);

    assertEquals(bdmTaskDtls.type,
      BDMTASKTYPE.ADDRESSCHANGE_DIRECTDEPOSIT_PWS);

  }

  @Test
  public void testgetSkillTypeByDocumentType()
    throws AppException, InformationalException {

    final long taskID = getATaskID();

    final BDMDocumentTypeKey taskKey = new BDMDocumentTypeKey();

    taskKey.documentType = DOCUMENTTYPE.FOREIGN_LIAISON;

    final Task task = TaskFactory.newInstance();
    final curam.util.workflow.struct.TaskKey taskKey2 =
      new curam.util.workflow.struct.TaskKey();
    taskKey2.taskID = taskID;
    final TaskDtls taskDtls = task.read(taskKey2);

    assertEquals(taskDtls.taskID, taskID);
    assertEquals(taskDtls.status, TASKSTATUS.NOTSTARTED);
    assertEquals(taskDtls.priority, TASKPRIORITY.NORMAL);

    final BDMTaskDtls bdmTaskDtls = getBDMTaskDetails(taskID);

    kBdmWorkAllocationTask.getSkillTypeByDocumentType(taskKey);

    assertEquals(bdmTaskDtls.type,
      BDMTASKTYPE.ADDRESSCHANGE_DIRECTDEPOSIT_PWS);

  }

  @Test
  public void testReadTaskType() throws AppException, InformationalException {

    final long taskID = getATaskID();

    final curam.core.sl.struct.TaskManagementTaskKey taskKey =
      new curam.core.sl.struct.TaskManagementTaskKey();

    taskKey.taskID = taskID;

    final BDMTaskCategoryAndTypeDetails bdmTaskCategoryAndTypeDetails =
      kBdmWorkAllocation.readTaskType(taskKey);

    assertEquals(taskID, bdmTaskCategoryAndTypeDetails.taskID);

    final BDMTaskDtls bdmTaskDtls = getBDMTaskDetails(taskID);

    assertEquals(BDMTASKTYPE.ADDRESSCHANGE_DIRECTDEPOSIT_PWS,
      bdmTaskDtls.type);

  }

  @Test
  public void testModifyTaskType()
    throws AppException, InformationalException {

    final BDMTaskCategoryAndTypeDetails details =
      new BDMTaskCategoryAndTypeDetails();

    final long taskID = getATaskID();

    details.taskID = taskID;
    details.taskType = BDMTASKTYPE.BDMACCOUNTVERIFICATION_PWS;

    kBdmWorkAllocation.modifyTaskType(details);

    final BDMTaskDtls bdmTaskDtls = getBDMTaskDetails(taskID);

    assertEquals(bdmTaskDtls.type, BDMTASKTYPE.BDMACCOUNTVERIFICATION_PWS);

  }

  @Test
  public void testbringForward() throws AppException, InformationalException {

    final BDMTaskBringForwardDetails details =
      new BDMTaskBringForwardDetails();

    // subscribeLoggedInUserToWorkqueues();
    final long taskID = getATaskID();

    details.dtls.taskID = taskID;
    details.dtls.bringForwardDateTime =
      DateTime.getCurrentDateTime().addTime(720, 0, 0); // 30 days later
    details.dtls.bringForwardToID = 80013;

    // details.dtls.bringForwardToID = BDMTASKTYPE.BDMACCOUNTVERIFICATION_PWS;

    kBdmWorkAllocation.bringForward(details);

    final BDMTaskDtls bdmTaskDtls = getBDMTaskDetails(taskID);

    // assertEquals(bdmTaskDtls.type, BDMTASKTYPE.BDMACCOUNTVERIFICATION_PWS);

  }

}
