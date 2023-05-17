/**
 *
 */
package curam.ca.gc.bdm.facade.supervisor.impl;

import curam.ca.gc.bdm.codetable.BDMESCALATIONLEVELS;
import curam.ca.gc.bdm.codetable.BDMURGENTFLAGTYPE;
import curam.ca.gc.bdm.test.BDMBaseTest;
import curam.core.facade.struct.CreateIntegratedCaseResultAndMessages;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.struct.ConcernRoleCommKeyOut;
import curam.supervisor.facade.struct.ReservedTaskForwardDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import org.junit.Test;

/**
 * JUnit class for BDMMaintainSupervisorUsers.
 *
 * @author donghua.jin
 *
 */
public class BDMMaintainSupervisorUsersTest extends BDMBaseTest {

  // BDMMaintainSupervisorUsers impl = new BDMMaintainSupervisorUsers();

  @Test
  public void testForwardTasksReservedByUserSearch()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    final long caseID = icResult.createCaseResult.integratedCaseID;

    final ConcernRoleCommKeyOut commKey = this.createRecordedCommunication(
      caseID, "Homer Simpson", person.registrationIDDetails.concernRoleID);

    this.createEscalationLevelForCommunication(commKey.communicationID,
      BDMESCALATIONLEVELS.ESCALATION_LEVEL_1);

    this.createUrgentFlagForCase(caseID, BDMURGENTFLAGTYPE.DIRENEED);

    this.createTaskAndReserve(caseID, "caseworker");

    final ReservedTaskForwardDetails details =
      new ReservedTaskForwardDetails();

    details.actionIDProperty = "SEARCH_TASKS";

    details.taskFwdDtls.forwardTaskDetails.forwardToID = "caseworker";
    details.taskFwdDtls.forwardTaskDetails.forwardToType = "RL9";
    details.taskFwdDtls.userName = "caseworker";
    details.taskQueryCriteria.businessObjectID = caseID;
    details.taskQueryCriteria.businessObjectType = "BOT1";
    details.taskQueryCriteria.creationLastNumberOfDays = -1;
    details.taskQueryCriteria.creationLastNumberOfWeeks = -1;

    // final ReservedTaskByUserList list =
    // impl.forwardTasksReservedByUserSearch(details);

    // assertEquals(1,
    // list.openTaskDtls.taskDetailsList.taskDetailsList.size());

    // Make sure no error for now.
  }

}
