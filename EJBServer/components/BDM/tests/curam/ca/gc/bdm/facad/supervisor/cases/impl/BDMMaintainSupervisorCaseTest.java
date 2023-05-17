/**
 *
 */
package curam.ca.gc.bdm.facad.supervisor.cases.impl;

import curam.ca.gc.bdm.facade.supervisor.cases.impl.BDMMaintainSupervisorCase;
import curam.ca.gc.bdm.facade.supervisor.cases.struct.BDMListCaseTasksDetails;
import curam.ca.gc.bdm.facade.supervisor.cases.struct.BDMListCaseTasksDueByWeekDetails;
import curam.ca.gc.bdm.facade.supervisor.cases.struct.BDMListCaseTasksDueOnDateDetails;
import curam.ca.gc.bdm.facade.supervisor.cases.struct.BDMListDeferredCaseTasksReservedByUserDetails;
import curam.ca.gc.bdm.facade.supervisor.cases.struct.BDMListOpenCaseTasksReservedByUserDetails;
import curam.ca.gc.bdm.test.BDMBaseTest;
import curam.codetable.BDMTASKTYPE;
import curam.core.facade.struct.CreateIntegratedCaseResultAndMessages;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.supervisor.facade.struct.CaseIDAndDeadlineDateKey;
import curam.supervisor.facade.struct.CaseIDAndUserKey;
import curam.supervisor.facade.struct.CaseIDKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import org.junit.Test;

/**
 * @author donghua.jin
 *
 */
public class BDMMaintainSupervisorCaseTest extends BDMBaseTest {

  BDMMaintainSupervisorCase facade = new BDMMaintainSupervisorCase();

  @Test
  public void testListBDMCaseAssignedTasks()
    throws AppException, InformationalException {

    final CaseIDKey key = new CaseIDKey();
    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    final long caseID = icResult.createCaseResult.integratedCaseID;

    key.caseID = caseID;
    final BDMListCaseTasksDetails newList =
      facade.listBDMCaseAssignedTasks(key);
  }

  @Test
  public void testListBDMCaseTasksDueOnDate()
    throws AppException, InformationalException {

    final CaseIDAndDeadlineDateKey key = new CaseIDAndDeadlineDateKey();
    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    final long caseID = icResult.createCaseResult.integratedCaseID;

    key.caseID = caseID;
    key.deadlineDate = Date.getCurrentDate().addDays(1);
    key.taskType = BDMTASKTYPE.ADDRESSCHANGE_DIRECTDEPOSIT_PWS;

    final BDMListCaseTasksDueOnDateDetails newList =
      facade.listBDMCaseTasksDueOnDate(key);

  }

  @Test
  public void testListBDMCaseAssignedTasksDueOnDate()
    throws AppException, InformationalException {

    final CaseIDAndDeadlineDateKey key = new CaseIDAndDeadlineDateKey();
    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    final long caseID = icResult.createCaseResult.integratedCaseID;

    key.caseID = caseID;
    key.deadlineDate = Date.getCurrentDate().addDays(1);
    key.taskType = BDMTASKTYPE.ADDRESSCHANGE_DIRECTDEPOSIT_PWS;

    final BDMListCaseTasksDueOnDateDetails newList =
      facade.listBDMCaseAssignedTasksDueOnDate(key);

  }

  @Test
  public void testListBDMCaseTasksDueByWeek()
    throws AppException, InformationalException {

    final CaseIDAndDeadlineDateKey caseIDKey = new CaseIDAndDeadlineDateKey();
    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    final long caseID = icResult.createCaseResult.integratedCaseID;

    caseIDKey.caseID = caseID;
    caseIDKey.deadlineDate = Date.getCurrentDate().addDays(1);
    caseIDKey.taskType = BDMTASKTYPE.ADDRESSCHANGE_DIRECTDEPOSIT_PWS;

    final BDMListCaseTasksDueByWeekDetails newList =
      facade.listBDMCaseTasksDueByWeek(caseIDKey);
  }

  @Test
  public void testListBDMCaseAssignedTasksDueByWeek()
    throws AppException, InformationalException {

    final CaseIDAndDeadlineDateKey caseIDKey = new CaseIDAndDeadlineDateKey();
    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    final long caseID = icResult.createCaseResult.integratedCaseID;

    caseIDKey.caseID = caseID;
    caseIDKey.deadlineDate = Date.getCurrentDate().addDays(1);
    caseIDKey.taskType = BDMTASKTYPE.ADDRESSCHANGE_DIRECTDEPOSIT_PWS;

    final BDMListCaseTasksDueByWeekDetails newList =
      facade.listBDMCaseAssignedTasksDueByWeek(caseIDKey);
  }

  @Test
  public void testListBDMCaseReservedTasksDueByWeek()
    throws AppException, InformationalException {

    final CaseIDAndDeadlineDateKey caseIDKey = new CaseIDAndDeadlineDateKey();
    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    final long caseID = icResult.createCaseResult.integratedCaseID;

    caseIDKey.caseID = caseID;
    caseIDKey.deadlineDate = Date.getCurrentDate().addDays(1);
    caseIDKey.taskType = BDMTASKTYPE.ADDRESSCHANGE_DIRECTDEPOSIT_PWS;

    final BDMListCaseTasksDueByWeekDetails newList =
      facade.listBDMCaseReservedTasksDueByWeek(caseIDKey);
  }

  @Test
  public void testListBDMDeferredCaseTasksReservedByUser()
    throws AppException, InformationalException {

    final CaseIDAndUserKey key = new CaseIDAndUserKey();
    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    final long caseID = icResult.createCaseResult.integratedCaseID;

    key.key.caseID = caseID;
    key.key.userName = "John SMith";
    final BDMListDeferredCaseTasksReservedByUserDetails newList =
      facade.listBDMDeferredCaseTasksReservedByUser(key);
  }

  @Test
  public void testListBDMOpenCaseTasksReservedByUser()
    throws AppException, InformationalException {

    final CaseIDAndUserKey key = new CaseIDAndUserKey();
    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    final long caseID = icResult.createCaseResult.integratedCaseID;

    key.key.caseID = caseID;
    key.key.userName = "John SMith";

    final BDMListOpenCaseTasksReservedByUserDetails newList =
      facade.listBDMOpenCaseTasksReservedByUser(key);
  }

  @Test
  public void testListBDMCaseReservedTasksDueOnDate()
    throws AppException, InformationalException {

    final CaseIDAndDeadlineDateKey key = new CaseIDAndDeadlineDateKey();
    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    final long caseID = icResult.createCaseResult.integratedCaseID;

    key.caseID = caseID;

    final BDMListCaseTasksDueOnDateDetails newList =
      facade.listBDMCaseReservedTasksDueOnDate(key);
  }

}
