/**
 *
 */
package curam.ca.gc.bdm.hooks.impl;

import curam.ca.gc.bdm.codetable.BDMESCALATIONLEVELS;
import curam.ca.gc.bdm.codetable.BDMURGENTFLAGTYPE;
import curam.ca.gc.bdm.sl.taskquery.struct.BDMTaskQueryDetails;
import curam.ca.gc.bdm.sl.taskquery.struct.BDMTaskQueryResult;
import curam.ca.gc.bdm.test.BDMBaseTest;
import curam.ca.gc.bdm.test.BDMTestConstants;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.codetable.RECORDSTATUS;
import curam.codetable.TASKDEADLINEDUE;
import curam.core.facade.struct.CreateIntegratedCaseResultAndMessages;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.facade.struct.ReadTaskSummaryDetailsKey;
import curam.core.impl.CuramConst;
import curam.core.sl.entity.fact.QueryFactory;
import curam.core.sl.entity.struct.QueryDtls;
import curam.core.sl.entity.struct.QueryKey;
import curam.core.sl.struct.ReadMultiOperationDetails;
import curam.core.struct.ConcernRoleCommKeyOut;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * JUnit class for BDMTaskQueryImpl.
 *
 * @author donghua.jin
 *
 */
public class BDMTaskQueryImplTest extends BDMBaseTest {

  BDMTaskQueryImpl bdmTaskQueryImpl = new BDMTaskQueryImpl();

  @Test
  public void testCreateTaskQuery()
    throws AppException, InformationalException {

    final BDMTaskQueryDetails details = new BDMTaskQueryDetails();
    details.actionIDProperty = "SAVE_QUERY";
    details.criteria.criteria.queryName = "Task Query 3";
    details.criteria.criteria.searchMyTasksOnly = true;
    details.criteria.criteria.creationLastNumberOfDays =
      BDMTestConstants.kMinusOne;
    details.criteria.criteria.creationLastNumberOfWeeks =
      BDMTestConstants.kMinusOne;
    details.criteria.caseUrgentFlagTypeCode = BDMURGENTFLAGTYPE.DIRENEED;
    details.criteria.escalationLevel = BDMESCALATIONLEVELS.ESCALATION_LEVEL_2;

    final long queryID = bdmTaskQueryImpl.createTaskQuery(details);
    assertNotEquals(0, queryID);

    // verify result
    final QueryKey qKey = new QueryKey();
    qKey.queryID = queryID;
    final QueryDtls qDtls = QueryFactory.newInstance().read(qKey);
    assertTrue(qDtls.query.contains(">BDMFG8000<"));
    assertTrue(qDtls.query.contains(">BDMEL2<"));
  }

  @Test
  public void testModifyTaskQuery()
    throws AppException, InformationalException {

    final BDMTaskQueryDetails details = new BDMTaskQueryDetails();
    details.actionIDProperty = "SAVE_QUERY";
    details.criteria.criteria.queryName = "Task Query 3";
    details.criteria.criteria.searchMyTasksOnly = true;
    details.criteria.criteria.creationLastNumberOfDays =
      BDMTestConstants.kMinusOne;
    details.criteria.criteria.creationLastNumberOfWeeks =
      BDMTestConstants.kMinusOne;
    details.criteria.caseUrgentFlagTypeCode = BDMURGENTFLAGTYPE.DIRENEED;
    details.criteria.escalationLevel = BDMESCALATIONLEVELS.ESCALATION_LEVEL_2;

    final long queryID = bdmTaskQueryImpl.createTaskQuery(details);

    final QueryKey qKey = new QueryKey();
    qKey.queryID = queryID;
    QueryDtls qDtls = QueryFactory.newInstance().read(qKey);
    assertTrue(qDtls.query.contains(">BDMEL2<"));
    details.queryDtls.queryID = queryID;
    details.queryDtls.versionNo = qDtls.versionNo;
    details.criteria.caseUrgentFlagTypeCode = BDMURGENTFLAGTYPE.MPENQUIRY;
    details.criteria.escalationLevel = BDMESCALATIONLEVELS.ESCALATION_LEVEL_3;

    bdmTaskQueryImpl.modifyTaskQuery(details);

    // verify result
    qDtls = QueryFactory.newInstance().read(qKey);
    assertTrue(qDtls.query.contains(">BDMFG8001<"));
    assertTrue(qDtls.query.contains(">BDMEL3<"));

  }

  @Test
  public void testValidateTaskQuery()
    throws AppException, InformationalException {

    BDMTaskQueryDetails details = new BDMTaskQueryDetails();
    details.queryDtls.queryName = "Test Query 1";
    details.criteria.criteria.creationLastNumberOfDays =
      BDMTestConstants.kMinusOne;
    details.criteria.criteria.creationLastNumberOfWeeks =
      BDMTestConstants.kMinusOne;

    try {
      bdmTaskQueryImpl.validateTaskQuery(details);
      TransactionInfo.getInformationalManager().failOperation();
      fail();
    } catch (final InformationalException ie) {
      ie.getExceptionDetails(0).contains("ERR_TASK_SEARCH_NO_CRITERIA");
    }

    TransactionInfo.setInformationalManager();

    details.criteria.escalationLevel = BDMESCALATIONLEVELS.ESCALATION_LEVEL_1;

    try {
      bdmTaskQueryImpl.validateTaskQuery(details);
      TransactionInfo.getInformationalManager().failOperation();
      fail();
    } catch (final InformationalException ie) {
      assertTrue(ie.getExceptionDetails(0)
        .contains("ERR_MINIMUM_CRITERIA_NOT_ENTERED"));
    }

    TransactionInfo.setInformationalManager();

    details.criteria.criteria.businessObjectID = CuramConst.kLongZero;
    details.criteria.criteria.businessObjectType = BUSINESSOBJECTTYPE.PERSON;

    try {
      bdmTaskQueryImpl.validateTaskQuery(details);
      TransactionInfo.getInformationalManager().failOperation();
      fail();
    } catch (final InformationalException ie) {
      assertTrue(ie.getExceptionDetails(0)
        .contains("ERR_TASK_BUSINESS_OBJECT_TYPE_NOT_SELECTED"));
    }

    TransactionInfo.setInformationalManager();

    details.criteria.criteria.businessObjectID =
      BDMTestConstants.kDummyLongValue;
    details.criteria.criteria.businessObjectType = CuramConst.gkEmpty;

    try {
      bdmTaskQueryImpl.validateTaskQuery(details);
      TransactionInfo.getInformationalManager().failOperation();
      fail();
    } catch (final InformationalException ie) {
      assertTrue(ie.getMessage().indexOf(
        "Please select a Business Object Type search criteria.") != -1);
    }

    TransactionInfo.setInformationalManager();

    details.criteria.criteria.selectedOrgObjects =
      "this is dummy selected org objects";
    details.criteria.criteria.searchMyTasksOnly = true;

    try {
      bdmTaskQueryImpl.validateTaskQuery(details);
      TransactionInfo.getInformationalManager().failOperation();
      fail();
    } catch (final InformationalException ie) {
      assertTrue(ie.getMessage().indexOf(
        "Only one of \"From My Tasks Only\" or \"Assigned To\" can be populated.") != -1);
    }
    TransactionInfo.setInformationalManager();

    details = new BDMTaskQueryDetails();
    details.queryDtls.queryName = "Test Query 1";
    details.criteria.criteria.creationLastNumberOfDays =
      BDMTestConstants.kMinusOne;
    details.criteria.criteria.creationLastNumberOfWeeks =
      BDMTestConstants.kMinusOne;
    details.criteria.criteria.deadlineDue = TASKDEADLINEDUE.DUETHISMONTH;
    details.criteria.criteria.deadlineFromDate =
      Date.getCurrentDate().addDays(-10);
    details.criteria.criteria.deadlineToDate =
      Date.getCurrentDate().addDays(10);

    try {
      bdmTaskQueryImpl.validateTaskQuery(details);
      TransactionInfo.getInformationalManager().failOperation();
      fail();
    } catch (final InformationalException ie) {
      String exceptionArray[] = ie.getExceptionDetails();
      for (String arrayItem : exceptionArray) {
        if (arrayItem.equals(
          "1::curam.message.BpoTaskSearch:ERR_DEADLINE_DUE_MUTUALLY_EXCLUSIVE:()")) {
          assertTrue(true);
          break;
        }
      }
    }

    TransactionInfo.setInformationalManager();

    details = new BDMTaskQueryDetails();
    details.queryDtls.queryName = "Test Query 1";
    details.criteria.criteria.creationLastNumberOfDays =
      BDMTestConstants.kMinusOne;
    details.criteria.criteria.creationLastNumberOfWeeks =
      BDMTestConstants.kMinusOne;

    details.criteria.criteria.creationFromDate =
      Date.getCurrentDate().addDays(20);

    details.criteria.criteria.creationToDate =
      Date.getCurrentDate().addDays(10);

    try {
      bdmTaskQueryImpl.validateTaskQuery(details);
      TransactionInfo.getInformationalManager().failOperation();
      fail();
    } catch (final InformationalException ie) {
      String exceptionArray[] = ie.getExceptionDetails();
      for (String arrayItem : exceptionArray) {
        if (arrayItem.equals(
          "1::curam.message.BpoTaskSearch:ERR_CREATION_FROM_BEFORE_CREATION_TO:()")) {
          assertTrue(true);
          break;
        }
      }
    }

    TransactionInfo.setInformationalManager();

    details = new BDMTaskQueryDetails();
    details.queryDtls.queryName = "Test Query 1";
    details.criteria.criteria.creationLastNumberOfDays =
      BDMTestConstants.kMinusOne;
    details.criteria.criteria.creationLastNumberOfWeeks =
      BDMTestConstants.kMinusOne;

    details.criteria.criteria.restartFromDate =
      Date.getCurrentDate().addDays(20);

    details.criteria.criteria.restartToDate =
      Date.getCurrentDate().addDays(10);

    try {
      bdmTaskQueryImpl.validateTaskQuery(details);
      TransactionInfo.getInformationalManager().failOperation();
      fail();
    } catch (final InformationalException ie) {
      String exceptionArray[] = ie.getExceptionDetails();
      for (String arrayItem : exceptionArray) {
        if (arrayItem.equals(
          "1::curam.message.BpoTaskSearch:ERR_RESTART_FROM_BEFORE_RESTART_TO:()")) {
          assertTrue(true);
          break;
        }
      }
    }

    TransactionInfo.setInformationalManager();

    details = new BDMTaskQueryDetails();
    details.queryDtls.queryName = "Test Query 1";
    details.criteria.criteria.creationLastNumberOfDays =
      BDMTestConstants.kMinusOne;
    details.criteria.criteria.creationLastNumberOfWeeks =
      BDMTestConstants.kMinusOne;

    details.criteria.criteria.deadlineFromDate =
      Date.getCurrentDate().addDays(20);

    details.criteria.criteria.deadlineToDate =
      Date.getCurrentDate().addDays(10);

    try {
      bdmTaskQueryImpl.validateTaskQuery(details);
      TransactionInfo.getInformationalManager().failOperation();
      fail();
    } catch (final InformationalException ie) {
      String exceptionArray[] = ie.getExceptionDetails();
      for (String arrayItem : exceptionArray) {
        if (arrayItem.equals(
          "1::curam.message.BpoTaskSearch:ERR_DEADLINE_FROM_BEFORE_DEADLINE_TO:()")) {
          assertTrue(true);
          break;
        }
      }
    }

    TransactionInfo.setInformationalManager();

    details = new BDMTaskQueryDetails();
    details.queryDtls.queryName = "Test Query 1";
    details.criteria.criteria.creationLastNumberOfDays =
      BDMTestConstants.kTen;
    details.criteria.criteria.creationLastNumberOfWeeks =
      BDMTestConstants.kTen;

    details.criteria.criteria.creationFromDate =
      Date.getCurrentDate().addDays(20);

    try {
      bdmTaskQueryImpl.validateTaskQuery(details);
      TransactionInfo.getInformationalManager().failOperation();
      fail();
    } catch (final InformationalException ie) {
      String exceptionArray[] = ie.getExceptionDetails();
      for (String arrayItem : exceptionArray) {
        if (arrayItem.equals(
          "1::curam.message.BpoTaskSearch:ERR_CREATION_DATE_MUTUALLY_EXCLUSIVE:()")) {
          assertTrue(true);
          break;
        }
      }
    }

    TransactionInfo.setInformationalManager();

    details = new BDMTaskQueryDetails();
    details.queryDtls.queryName = "Test Query 1";
    details.criteria.criteria.creationLastNumberOfDays =
      BDMTestConstants.kTen;
    details.criteria.criteria.creationLastNumberOfWeeks =
      BDMTestConstants.kTen;

    details.criteria.criteria.creationToDate =
      Date.getCurrentDate().addDays(20);

    try {
      bdmTaskQueryImpl.validateTaskQuery(details);
      TransactionInfo.getInformationalManager().failOperation();
      fail();
    } catch (final InformationalException ie) {
      String exceptionArray[] = ie.getExceptionDetails();
      for (String arrayItem : exceptionArray) {
        if (arrayItem.equals(
          "1::curam.message.BpoTaskSearch:ERR_CREATION_DATE_MUTUALLY_EXCLUSIVE:()")) {
          assertTrue(true);
          break;
        }
      }
    }

    TransactionInfo.setInformationalManager();

    details = new BDMTaskQueryDetails();
    details.queryDtls.queryName = "Test Query 1";
    details.criteria.criteria.creationLastNumberOfDays =
      BDMTestConstants.kTen;
    details.criteria.criteria.creationLastNumberOfWeeks =
      BDMTestConstants.kTen;

    try {
      bdmTaskQueryImpl.validateTaskQuery(details);
      TransactionInfo.getInformationalManager().failOperation();
      fail();
    } catch (final InformationalException ie) {
      String exceptionArray[] = ie.getExceptionDetails();
      for (String arrayItem : exceptionArray) {
        if (arrayItem.equals(
          "1::curam.message.BpoTaskSearch:ERR_CREATION_DATE_NUMBER_DAYS_AND_WEEKS_MUTUALLY_EXCLUSIVE:()")) {
          assertTrue(true);
          break;
        }
      }
    }

  }

  @Test
  public void testValidateTaskQueryForMinCriteria()
    throws AppException, InformationalException {

    final BDMTaskQueryDetails details = new BDMTaskQueryDetails();
    details.queryDtls.queryName = "Test Query 1";
    details.criteria.criteria.creationLastNumberOfDays =
      BDMTestConstants.kMinusOne;
    details.criteria.criteria.creationLastNumberOfWeeks =
      BDMTestConstants.kMinusOne;

    try {
      bdmTaskQueryImpl.validateTaskQueryForMinCriteria(details);
      TransactionInfo.getInformationalManager().failOperation();
      fail();
    } catch (final InformationalException ie) {
      assertTrue(
        ie.getExceptionDetails(0).contains("ERR_TASK_SEARCH_NO_CRITERIA"));
    }

    TransactionInfo.setInformationalManager();

    details.criteria.escalationLevel = BDMESCALATIONLEVELS.ESCALATION_LEVEL_1;

    try {
      bdmTaskQueryImpl.validateTaskQueryForMinCriteria(details);
      TransactionInfo.getInformationalManager().failOperation();
      fail();
    } catch (final InformationalException ie) {
      assertTrue(ie.getExceptionDetails(0)
        .contains("ERR_MINIMUM_CRITERIA_NOT_ENTERED"));
    }

    TransactionInfo.setInformationalManager();

    details.criteria.escalationLevel = "";
    details.criteria.caseUrgentFlagTypeCode = BDMURGENTFLAGTYPE.DIRENEED;

    try {
      bdmTaskQueryImpl.validateTaskQueryForMinCriteria(details);
      TransactionInfo.getInformationalManager().failOperation();
      fail();
    } catch (final InformationalException ie) {
      assertTrue(ie.getExceptionDetails(0)
        .contains("ERR_MINIMUM_CRITERIA_NOT_ENTERED"));
    }

    TransactionInfo.setInformationalManager();

    details.criteria.criteria.searchMyTasksOnly = true;
    try {
      bdmTaskQueryImpl.validateTaskQueryForMinCriteria(details);
      TransactionInfo.getInformationalManager().failOperation();
    } catch (final InformationalException ie) {
      fail("Unexpected exception" + ie.getExceptionDetails(0));
    }

    TransactionInfo.setInformationalManager();
  }

  @Test
  public void testRunTaskQuery() throws AppException, InformationalException {

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

    QueryDtls queryDtls = new QueryDtls();
    queryDtls.queryName = "Test Task Query 1";
    queryDtls.queryType = "QYT2";
    queryDtls.userName = "caseworker";
    queryDtls.statusCode = RECORDSTATUS.NORMAL;
    queryDtls.runFrequency = 1;
    queryDtls.versionNo = 1;
    queryDtls.query =
      "<Query><SelectedOrgObjects>RL9|caseworker</SelectedOrgObjects></Query>";

    final curam.core.sl.entity.intf.Query query = QueryFactory.newInstance();
    query.insert(queryDtls);

    QueryKey key = new QueryKey();
    key.queryID = queryDtls.queryID;

    ReadMultiOperationDetails readMultiDetails =
      new ReadMultiOperationDetails();
    BDMTaskQueryResult bdmSearchResult =
      bdmTaskQueryImpl.runTaskQuery(key, readMultiDetails);

    assertEquals(bdmSearchResult.criteria.criteria.queryName,
      queryDtls.queryName);
    assertEquals(bdmSearchResult.queryID, queryDtls.queryID);

  }

  @Test
  public void testRunTaskQuery01()
    throws AppException, InformationalException {

    BDMTaskQueryResult bdmTaskQueryResult = new BDMTaskQueryResult();

    BDMTaskQueryDetails details = new BDMTaskQueryDetails();

    ReadMultiOperationDetails readMultiDetails =
      new ReadMultiOperationDetails();
    try {
      BDMTaskQueryResult bdmSearchResult =
        bdmTaskQueryImpl.runTaskQuery(details, readMultiDetails);
    } catch (InformationalException ie) {
      assertTrue(ie.getMessage().indexOf("Must enter a Query Name.") != -1);
    }

  }

}
