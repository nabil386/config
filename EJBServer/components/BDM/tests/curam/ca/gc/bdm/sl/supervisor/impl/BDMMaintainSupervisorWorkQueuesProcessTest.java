/**
 *
 */
package curam.ca.gc.bdm.sl.supervisor.impl;

import curam.ca.gc.bdm.codetable.BDMESCALATIONLEVELS;
import curam.ca.gc.bdm.codetable.BDMURGENTFLAGTYPE;
import curam.ca.gc.bdm.test.BDMBaseTest;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.core.facade.struct.CreateIntegratedCaseResultAndMessages;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.facade.struct.ReadTaskSummaryDetailsKey;
import curam.core.struct.ConcernRoleCommKeyOut;
import curam.supervisor.sl.struct.TaskFilterCriteriaDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import org.junit.Test;

/**
 * @author donghua.jin
 *
 */
public class BDMMaintainSupervisorWorkQueuesProcessTest extends BDMBaseTest {

  BDMMaintainSupervisorWorkQueuesProcess impl =
    new BDMMaintainSupervisorWorkQueuesProcess();

  @Test
  public void testListBDMAssignedTasks()
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

    final TaskFilterCriteriaDetails dtls = new TaskFilterCriteriaDetails();

    dtls.assigneeType = "RL23";
    dtls.businessObjectID = caseID;
    dtls.businessObjectType = "BOT1";
    dtls.maxTasksShown = 100;
    dtls.pageID = "Supervisor_reserveWorkQueueTasksToUser";
    dtls.relatedID = 80013;

    impl.listBDMAssignedTasks(dtls);

    // For now, make sure no error.
  }

}
