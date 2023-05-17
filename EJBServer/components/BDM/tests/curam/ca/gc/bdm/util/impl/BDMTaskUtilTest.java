/**
 *
 */
package curam.ca.gc.bdm.util.impl;

import curam.ca.gc.bdm.codetable.BDMESCALATIONLEVELS;
import curam.ca.gc.bdm.codetable.BDMURGENTFLAGTYPE;
import curam.ca.gc.bdm.entity.fact.BDMTaskClassificationFactory;
import curam.ca.gc.bdm.entity.struct.BDMProcessDefinitionDetailsList;
import curam.ca.gc.bdm.test.BDMBaseTest;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.codetable.TARGETITEMTYPE;
import curam.codetable.TASKPRIORITY;
import curam.codetable.TASKSTATUS;
import curam.core.facade.struct.CreateIntegratedCaseResultAndMessages;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.facade.struct.ReadTaskSummaryDetailsKey;
import curam.core.sl.entity.struct.TaskAssignmentDtls;
import curam.core.sl.entity.struct.TaskKey;
import curam.core.struct.ConcernRoleCommKeyOut;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.DateTime;
import curam.util.type.UniqueID;
import curam.util.workflow.fact.TaskFactory;
import curam.util.workflow.struct.TaskDtls;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author donghua.jin
 *
 */
public class BDMTaskUtilTest extends BDMBaseTest {

  @Test
  public void testGetCaseUrgentFlagsByTaskID()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    final long caseID = icResult.createCaseResult.integratedCaseID;

    final ConcernRoleCommKeyOut commKey = this.createRecordedCommunication(
      caseID, "Homer Simpson", person.registrationIDDetails.concernRoleID);

    final long communicationID = this.createEscalationLevelForCommunication(
      commKey.communicationID, BDMESCALATIONLEVELS.ESCALATION_LEVEL_1);

    this.createUrgentFlagForCase(caseID, BDMURGENTFLAGTYPE.DIRENEED);

    final ReadTaskSummaryDetailsKey taskSumKey =
      this.createTaskAndReserve(caseID, "caseworker");

    this.createBizObjAssociationForTask(BUSINESSOBJECTTYPE.BDMCOMMUNICATION,
      communicationID, taskSumKey.dtls.dtls.taskID);

    final String result =
      BDMTaskUtil.getCaseUrgentFlagsByTaskID(taskSumKey.dtls.dtls.taskID);

    // System.out.println("result: " + result);

    // Just make sure no exception
  }

  @Test
  public void testGetEscalationLevelsByTaskID()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    final long caseID = icResult.createCaseResult.integratedCaseID;

    final ConcernRoleCommKeyOut commKey = this.createRecordedCommunication(
      caseID, "Homer Simpson", person.registrationIDDetails.concernRoleID);

    final long communicationID = this.createEscalationLevelForCommunication(
      commKey.communicationID, BDMESCALATIONLEVELS.ESCALATION_LEVEL_1);

    this.createUrgentFlagForCase(caseID, BDMURGENTFLAGTYPE.DIRENEED);

    final ReadTaskSummaryDetailsKey taskSumKey =
      this.createTaskAndReserve(caseID, "caseworker");

    this.createBizObjAssociationForTask(BUSINESSOBJECTTYPE.BDMCOMMUNICATION,
      communicationID, taskSumKey.dtls.dtls.taskID);

    final String result =
      BDMTaskUtil.getEscalationLevelsByTaskID(taskSumKey.dtls.dtls.taskID);

    // System.out.println("result: " + result);

    // Just make sure no exception
  }

  @Test
  public void testProcessIdFromTask()
    throws AppException, InformationalException {

    final TaskDtls taskCreateDetail = new TaskDtls();

    taskCreateDetail.taskID = UniqueID.nextUniqueID();
    taskCreateDetail.creationTime = DateTime.getCurrentDateTime();
    taskCreateDetail.status = TASKSTATUS.NOTSTARTED;
    taskCreateDetail.wdoSnapshot = "";
    taskCreateDetail.priority = TASKPRIORITY.NORMAL;
    taskCreateDetail.versionNo = 1;
    taskCreateDetail.reservedBy = "caseworker";
    TaskFactory.newInstance().insert(taskCreateDetail);

    final TaskAssignmentDtls taskAssignmentDtls = new TaskAssignmentDtls();
    taskAssignmentDtls.taskID = taskCreateDetail.taskID;
    taskAssignmentDtls.relatedName = "caseworker";
    taskAssignmentDtls.assigneeType = TARGETITEMTYPE.WORKQUEUE;

    final TaskKey ke = new TaskKey();
    ke.taskID = taskCreateDetail.taskID;

    final BDMProcessDefinitionDetailsList list =
      BDMTaskClassificationFactory.newInstance().getProcessFromTaskId(ke);
    assertNotNull(list);
    assertEquals(list.dtls.size(), 0);

  }

}
