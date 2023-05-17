package curam.ca.gc.bdm.sl.taskquery.impl;

import curam.ca.gc.bdm.codetable.BDMESCALATIONLEVELS;
import curam.ca.gc.bdm.codetable.BDMURGENTFLAGTYPE;
import curam.ca.gc.bdm.hooks.impl.BDMTaskQueryImpl;
import curam.ca.gc.bdm.sl.taskquery.struct.BDMTaskQueryDetails;
import curam.ca.gc.bdm.sl.taskquery.struct.BDMTaskQueryResult;
import curam.ca.gc.bdm.test.BDMBaseTest;
import curam.core.sl.entity.struct.QueryKey;
import curam.core.sl.struct.ReadMultiOperationDetails;
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
public class BDMTaskQueryProcessTest extends BDMBaseTest {

  BDMTaskQueryProcess bdmTaskQueryProcess = new BDMTaskQueryProcess();

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

    final BDMTaskQueryImpl bdmTaskQueryImpl = new BDMTaskQueryImpl();
    final long queryID = bdmTaskQueryImpl.createTaskQuery(details);

    final QueryKey qKey = new QueryKey();
    qKey.queryID = queryID;
    final BDMTaskQueryDetails tqDetials = bdmTaskQueryProcess.readBDM(qKey);

    // verify result
    assertEquals(BDMURGENTFLAGTYPE.DIRENEED,
      tqDetials.criteria.caseUrgentFlagTypeCode);
    assertEquals(BDMESCALATIONLEVELS.ESCALATION_LEVEL_2,
      tqDetials.criteria.escalationLevel);
  }

  @Test
  public void testCreateBDM() throws AppException, InformationalException {

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
      bdmTaskQueryProcess.createBDM(bdmTaskQueryDetails);

    // verify result
    assertNotEquals(0, result.queryID);
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

    final BDMTaskQueryImpl bdmTaskQueryImpl = new BDMTaskQueryImpl();
    final long queryID = bdmTaskQueryImpl.createTaskQuery(details);

    final QueryKey qKey = new QueryKey();
    qKey.queryID = queryID;
    final BDMTaskQueryDetails tqDetials = bdmTaskQueryProcess.readBDM(qKey);

    tqDetials.criteria.caseUrgentFlagTypeCode = BDMURGENTFLAGTYPE.MPENQUIRY;

    bdmTaskQueryProcess.modifyBDM(tqDetials);

    // verify result
    final BDMTaskQueryDetails modifiedTqDetials =
      bdmTaskQueryProcess.readBDM(qKey);
    assertEquals(BDMURGENTFLAGTYPE.MPENQUIRY,
      modifiedTqDetials.criteria.caseUrgentFlagTypeCode);
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
    bdmTaskQueryProcess.runBDM(qKey, new ReadMultiOperationDetails());

    // verify result
    // Just make sure no exception for now.
  }
}
