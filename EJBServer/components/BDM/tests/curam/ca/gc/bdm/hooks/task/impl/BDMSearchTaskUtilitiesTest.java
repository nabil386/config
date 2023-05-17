/**
 *
 */
package curam.ca.gc.bdm.hooks.task.impl;

import curam.ca.gc.bdm.codetable.BDMESCALATIONLEVELS;
import curam.ca.gc.bdm.codetable.BDMURGENTFLAGTYPE;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMTaskQueryResultDetailsList;
import curam.ca.gc.bdm.sl.taskquery.struct.BDMTaskQueryCriteria;
import curam.ca.gc.bdm.test.BDMBaseTest;
import curam.codetable.ASSIGNEETYPE;
import curam.core.sl.entity.struct.QueryDtls;
import curam.core.sl.struct.ReadMultiOperationDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * JUnit Test class for BDMSearchTaskUtilities.
 *
 * @author donghua.jin
 *
 */
public class BDMSearchTaskUtilitiesTest extends BDMBaseTest {

  @Test
  public void testFormatBDMXMLQueryCriteria()
    throws AppException, InformationalException {

    final BDMTaskQueryCriteria criteria = new BDMTaskQueryCriteria();
    criteria.criteria.searchMyTasksOnly = true;
    criteria.caseUrgentFlagTypeCode = BDMURGENTFLAGTYPE.DIRENEED;
    criteria.escalationLevel = BDMESCALATIONLEVELS.ESCALATION_LEVEL_2;

    final String xmlCriteria =
      BDMSearchTaskUtilities.formatXMLQueryCriteria(criteria);

    // verify result
    assertTrue(xmlCriteria.contains("<UrgentFlag>BDMFG8000</UrgentFlag>"));
    assertTrue(
      xmlCriteria.contains("<EscalationLevel>BDMEL2</EscalationLevel>"));
  }

  @Test
  public void testGetBDMTaskQueryCriteria()
    throws AppException, InformationalException {

    final QueryDtls qDtls = new QueryDtls();
    qDtls.query = "<Query>" + "<FromMyTasksOnly>true</FromMyTasksOnly>"
      + "<UrgentFlag>BDMFG8000</UrgentFlag>"
      + "<EscalationLevel>BDMEL8</EscalationLevel>" + "</Query>";

    final BDMTaskQueryCriteria criteria =
      BDMSearchTaskUtilities.getTaskQueryCriteria(qDtls);

    // verify result
    assertTrue(criteria.criteria.searchMyTasksOnly);
    assertEquals("BDMFG8000", criteria.caseUrgentFlagTypeCode);
    assertEquals("BDMEL8", criteria.escalationLevel);
  }

  @Test
  public void testParseBDMXMLQueryCriteria()
    throws AppException, InformationalException {

    final String xmlCriteria =
      "<Query>" + "<FromMyTasksOnly>true</FromMyTasksOnly>"
        + "<UrgentFlag>BDMFG8000</UrgentFlag>"
        + "<EscalationLevel>BDMEL8</EscalationLevel>" + "</Query>";

    final BDMTaskQueryCriteria criteria =
      BDMSearchTaskUtilities.parseXMLQueryCriteria(xmlCriteria);

    // verify result
    assertTrue(criteria.criteria.searchMyTasksOnly);
    assertEquals("BDMFG8000", criteria.caseUrgentFlagTypeCode);
    assertEquals("BDMEL8", criteria.escalationLevel);
  }

  @Test
  public void testSearchTask() throws AppException, InformationalException {

    final BDMTaskQueryCriteria bdmTaskQueryCriteria =
      new BDMTaskQueryCriteria();
    final ReadMultiOperationDetails readMultiDetails =
      new ReadMultiOperationDetails();

    bdmTaskQueryCriteria.escalationLevel =
      BDMESCALATIONLEVELS.ESCALATION_LEVEL_1;
    bdmTaskQueryCriteria.criteria.assigneeType = ASSIGNEETYPE.USER;
    bdmTaskQueryCriteria.criteria.assignedToID = "caseworker";
    final BDMTaskQueryResultDetailsList list = BDMSearchTaskUtilities
      .searchTask(bdmTaskQueryCriteria, readMultiDetails);

    assertTrue(list.taskDetailsList.isEmpty());
  }

}
