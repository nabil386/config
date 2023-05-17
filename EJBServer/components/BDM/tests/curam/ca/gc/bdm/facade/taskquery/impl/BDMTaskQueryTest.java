/**
 *
 */
package curam.ca.gc.bdm.facade.taskquery.impl;

import curam.ca.gc.bdm.codetable.BDMESCALATIONLEVELS;
import curam.ca.gc.bdm.codetable.BDMURGENTFLAGTYPE;
import curam.ca.gc.bdm.hooks.impl.BDMTaskQueryImpl;
import curam.ca.gc.bdm.sl.taskquery.fact.BDMTaskQueryProcessFactory;
import curam.ca.gc.bdm.sl.taskquery.intf.BDMTaskQueryProcess;
import curam.ca.gc.bdm.sl.taskquery.struct.BDMTaskQueryDetails;
import curam.ca.gc.bdm.sl.taskquery.struct.BDMTaskQueryResult;
import curam.ca.gc.bdm.test.BDMBaseTest;
import curam.ca.gc.bdm.test.BDMTestConstants;
import curam.core.sl.entity.struct.QueryKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * JUnit class for BDMTaskQuery.
 *
 * @author donghua.jin
 *
 */
public class BDMTaskQueryTest extends BDMBaseTest {

  BDMTaskQuery facade = new BDMTaskQuery();

  BDMTaskQueryProcess process = BDMTaskQueryProcessFactory.newInstance();

  @Test
  public void testCreateOrRunBDMQuery()
    throws AppException, InformationalException {

    final BDMTaskQueryDetails bdmTaskQueryDetails = new BDMTaskQueryDetails();
    bdmTaskQueryDetails.actionIDProperty = "SAVE_QUERY";
    bdmTaskQueryDetails.criteria.criteria.queryName = "Task Query 3";
    bdmTaskQueryDetails.criteria.criteria.searchMyTasksOnly = true;
    bdmTaskQueryDetails.criteria.criteria.creationLastNumberOfDays = -1;
    bdmTaskQueryDetails.criteria.criteria.creationLastNumberOfWeeks = -1;
    bdmTaskQueryDetails.criteria.caseUrgentFlagTypeCode =
      BDMURGENTFLAGTYPE.DIRENEED;
    bdmTaskQueryDetails.criteria.escalationLevel =
      BDMESCALATIONLEVELS.ESCALATION_LEVEL_2;

    final BDMTaskQueryResult result =
      facade.createOrRunBDMQuery(bdmTaskQueryDetails);

    // verify result
    assertNotEquals(0, result.queryID);
  }

  @Test
  public void testCreateOrRunBDMQuery_RUN_QUERY()
    throws AppException, InformationalException {

    final BDMTaskQueryDetails bdmTaskQueryDetails = new BDMTaskQueryDetails();
    bdmTaskQueryDetails.actionIDProperty = "RUN_QUERY";
    bdmTaskQueryDetails.criteria.criteria.queryName = "Task Query 3";
    bdmTaskQueryDetails.criteria.criteria.searchMyTasksOnly = true;
    bdmTaskQueryDetails.criteria.criteria.creationLastNumberOfDays = -1;
    bdmTaskQueryDetails.criteria.criteria.creationLastNumberOfWeeks = -1;
    bdmTaskQueryDetails.criteria.caseUrgentFlagTypeCode =
      BDMURGENTFLAGTYPE.DIRENEED;
    bdmTaskQueryDetails.criteria.escalationLevel =
      BDMESCALATIONLEVELS.ESCALATION_LEVEL_2;

    final BDMTaskQueryResult result =
      facade.createOrRunBDMQuery(bdmTaskQueryDetails);

    // verify result
    assertEquals(0, result.queryID);
    assertNotEquals(BDMTestConstants.kMinusOne,
      result.resultList.informationalMsgDetailsList.dtls
        .item(0).informationMsgTxt.indexOf(
          "2::curam.message.BpoInbox:INF_TASK_XFV_MATCHED_RESULTS_FOUND:(0)"));

  }

  @Test
  public void testCreateOrRunBDMQuery_UNKNOWN_QUERY()
    throws AppException, InformationalException {

    final BDMTaskQueryDetails bdmTaskQueryDetails = new BDMTaskQueryDetails();
    bdmTaskQueryDetails.actionIDProperty = "UNKNOWN_QUERY";
    bdmTaskQueryDetails.criteria.criteria.queryName = "Task Query 3";
    bdmTaskQueryDetails.criteria.criteria.searchMyTasksOnly = true;
    bdmTaskQueryDetails.criteria.criteria.creationLastNumberOfDays = -1;
    bdmTaskQueryDetails.criteria.criteria.creationLastNumberOfWeeks = -1;
    bdmTaskQueryDetails.criteria.caseUrgentFlagTypeCode =
      BDMURGENTFLAGTYPE.DIRENEED;
    bdmTaskQueryDetails.criteria.escalationLevel =
      BDMESCALATIONLEVELS.ESCALATION_LEVEL_2;

    BDMTaskQueryResult result = new BDMTaskQueryResult();

    try {
      result = facade.createOrRunBDMQuery(bdmTaskQueryDetails);
    } catch (AppException ae) {
      // verify result
      assertEquals(0, result.queryID);
      assertNotEquals(BDMTestConstants.kMinusOne, ae.getMessage().indexOf(
        "The action 'UNKNOWN_QUERY' requested by the page is unknown. Please contact your administrator."));

    }

  }

  @Test
  public void testModifyBDM() throws AppException, InformationalException {

    final BDMTaskQueryDetails details = new BDMTaskQueryDetails();
    details.actionIDProperty = "SAVE_QUERY";
    details.criteria.criteria.queryName = "Task Query 3";
    details.criteria.criteria.searchMyTasksOnly = true;
    details.criteria.criteria.creationLastNumberOfDays = -1;
    details.criteria.criteria.creationLastNumberOfWeeks = -1;
    details.criteria.caseUrgentFlagTypeCode = BDMURGENTFLAGTYPE.DIRENEED;
    details.criteria.escalationLevel = BDMESCALATIONLEVELS.ESCALATION_LEVEL_2;

    final long queryID = process.createBDM(details).queryID;

    final QueryKey qKey = new QueryKey();
    qKey.queryID = queryID;
    final BDMTaskQueryDetails tqDetials = facade.readBDM(qKey);

    tqDetials.criteria.caseUrgentFlagTypeCode = BDMURGENTFLAGTYPE.MPENQUIRY;

    facade.modifyBDM(tqDetials);

    // verify result
    final BDMTaskQueryDetails modifiedTqDetials = facade.readBDM(qKey);
    assertEquals(BDMURGENTFLAGTYPE.MPENQUIRY,
      modifiedTqDetials.criteria.caseUrgentFlagTypeCode);
  }

  @Test
  public void testReadBDM() throws AppException, InformationalException {

    final BDMTaskQueryDetails details = new BDMTaskQueryDetails();
    details.actionIDProperty = "SAVE_QUERY";
    details.criteria.criteria.queryName = "Task Query 3";
    details.criteria.criteria.searchMyTasksOnly = true;
    details.criteria.criteria.creationLastNumberOfDays = -1;
    details.criteria.criteria.creationLastNumberOfWeeks = -1;
    details.criteria.caseUrgentFlagTypeCode = BDMURGENTFLAGTYPE.DIRENEED;
    details.criteria.escalationLevel = BDMESCALATIONLEVELS.ESCALATION_LEVEL_2;

    final long queryID = process.createBDM(details).queryID;

    final QueryKey queryKey = new QueryKey();
    queryKey.queryID = queryID;
    final BDMTaskQueryDetails bdmTaskQueryDetails = facade.readBDM(queryKey);

    // verify result
    assertEquals(BDMURGENTFLAGTYPE.DIRENEED,
      bdmTaskQueryDetails.criteria.caseUrgentFlagTypeCode);
    assertEquals(BDMESCALATIONLEVELS.ESCALATION_LEVEL_2,
      bdmTaskQueryDetails.criteria.escalationLevel);
  }

  @Test
  public void testRunBDM() throws AppException, InformationalException {

    final BDMTaskQueryDetails details = new BDMTaskQueryDetails();
    details.actionIDProperty = "SAVE_QUERY";
    details.criteria.criteria.queryName = "Task Query 3";
    details.criteria.criteria.searchMyTasksOnly = true;
    details.criteria.criteria.creationLastNumberOfDays = -1;
    details.criteria.criteria.creationLastNumberOfWeeks = -1;
    details.criteria.caseUrgentFlagTypeCode = BDMURGENTFLAGTYPE.DIRENEED;
    details.criteria.escalationLevel = BDMESCALATIONLEVELS.ESCALATION_LEVEL_2;

    final BDMTaskQueryImpl bdmTaskQueryImpl = new BDMTaskQueryImpl();
    final long queryID = bdmTaskQueryImpl.createTaskQuery(details);

    final QueryKey qKey = new QueryKey();
    qKey.queryID = queryID;
    facade.runBDM(qKey);

    // verify result
    // Just make sure no exception for now.
  }
}
