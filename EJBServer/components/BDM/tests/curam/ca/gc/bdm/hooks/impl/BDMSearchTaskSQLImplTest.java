/**
 *
 */
package curam.ca.gc.bdm.hooks.impl;

import curam.ca.gc.bdm.codetable.BDMESCALATIONLEVELS;
import curam.ca.gc.bdm.codetable.BDMURGENTFLAGTYPE;
import curam.ca.gc.bdm.sl.taskquery.struct.BDMTaskQueryCriteria;
import curam.ca.gc.bdm.test.BDMBaseTest;
import curam.codetable.ASSIGNEETYPE;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.codetable.TASKCATEGORY;
import curam.codetable.TASKSTATUS;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * JUnit class for BDMSearchTaskSQLImpl;
 *
 * @author donghua.jin
 *
 */
public class BDMSearchTaskSQLImplTest extends BDMBaseTest {

  BDMSearchTaskSQLImpl impl = new BDMSearchTaskSQLImpl();

  @Test
  public void testGetFromClause()
    throws AppException, InformationalException {

    final BDMTaskQueryCriteria criteria = new BDMTaskQueryCriteria();

    String fromClause = impl.getFromClause(criteria);
    assertFalse(fromClause.contains("BizObjAssociation BOAUF "));
    assertFalse(fromClause.contains("BizObjAssociation BOAEL "));

    criteria.caseUrgentFlagTypeCode = BDMURGENTFLAGTYPE.DIRENEED;

    fromClause = impl.getFromClause(criteria);
    assertTrue(fromClause.contains("BizObjAssociation BOAUF "));
    assertFalse(fromClause.contains("BizObjAssociation BOAEL "));

    criteria.caseUrgentFlagTypeCode = "";
    criteria.escalationLevel = BDMESCALATIONLEVELS.ESCALATION_LEVEL_1;
    fromClause = impl.getFromClause(criteria);
    assertFalse(fromClause.contains("BizObjAssociation BOAUF "));
    assertTrue(fromClause.contains("BizObjAssociation BOAEL "));

    criteria.caseUrgentFlagTypeCode = BDMURGENTFLAGTYPE.DIRENEED;
    criteria.escalationLevel = BDMESCALATIONLEVELS.ESCALATION_LEVEL_1;
    fromClause = impl.getFromClause(criteria);
    assertFalse(fromClause.contains("BizObjAssociation BOAUF "));
    assertFalse(fromClause.contains("BizObjAssociation BOAEL "));
    assertTrue(fromClause.contains("BizObjAssociation BOAELUF "));
    assertTrue(fromClause.contains("BDMEscalationLevel BDMEL "));
    assertTrue(fromClause.contains("ConcernRoleCommunication COMM "));
  }

  @Test
  public void testGetWhereClause()
    throws AppException, InformationalException {

    final BDMTaskQueryCriteria criteria = new BDMTaskQueryCriteria();

    String fromClause = impl.getWhereClause(criteria);
    assertFalse(fromClause.contains("BDMCaseUrgentFlag BDMUF"));
    assertFalse(fromClause.contains("BDMEL.escalationLevel="));

    criteria.caseUrgentFlagTypeCode = BDMURGENTFLAGTYPE.DIRENEED;

    fromClause = impl.getWhereClause(criteria);
    assertTrue(fromClause.contains("BDMCaseUrgentFlag BDMUF"));
    assertFalse(fromClause.contains("BDMEL.escalationLevel="));

    criteria.caseUrgentFlagTypeCode = "";
    criteria.escalationLevel = BDMESCALATIONLEVELS.ESCALATION_LEVEL_1;
    fromClause = impl.getWhereClause(criteria);
    assertFalse(fromClause.contains("BDMCaseUrgentFlag BDMUF"));
    assertTrue(fromClause.contains("BDMEL.escalationLevel="));

    criteria.caseUrgentFlagTypeCode = BDMURGENTFLAGTYPE.DIRENEED;
    criteria.escalationLevel = BDMESCALATIONLEVELS.ESCALATION_LEVEL_1;
    fromClause = impl.getWhereClause(criteria);
    assertTrue(fromClause.contains("BDMCaseUrgentFlag BDMUF"));
    assertTrue(fromClause.contains("BDMEL.escalationLevel="));

  }

  @Test
  public void testAppendWhereClause()
    throws AppException, InformationalException {

    final StringBuilder currentWhereClause =
      new StringBuilder("WHERE a.Type = 'C'");
    final StringBuilder emptyWhereClause = new StringBuilder();

    final String whereClauseToAppend = "b.name = 'John'";
    impl.appendWhereClause(emptyWhereClause, whereClauseToAppend);
    assertEquals("WHERE b.name = 'John'", emptyWhereClause.toString().trim());

    impl.appendWhereClause(currentWhereClause, whereClauseToAppend);
    assertEquals("WHERE a.Type = 'C' AND b.name = 'John'",
      currentWhereClause.toString().trim());
  }

  @Test
  public void testGetSQLStatement()
    throws AppException, InformationalException {

    final BDMTaskQueryCriteria bdmTaskQueryCriteria =
      new BDMTaskQueryCriteria();

    bdmTaskQueryCriteria.criteria.searchMyTasksOnly = true;
    bdmTaskQueryCriteria.caseUrgentFlagTypeCode = BDMURGENTFLAGTYPE.DIRENEED;
    bdmTaskQueryCriteria.escalationLevel =
      BDMESCALATIONLEVELS.ESCALATION_LEVEL_1;

    final String sqlStatement = impl.getSQLStatement(bdmTaskQueryCriteria);

    assertTrue(sqlStatement.contains("SELECT DISTINCT T.TASKID"));
    assertTrue(sqlStatement.contains("ConcernRoleCommunication"));
    assertTrue(sqlStatement.contains("BDMEscalationLevel"));
    assertTrue(sqlStatement.contains("BDMCaseUrgentFlag"));
    assertTrue(sqlStatement.contains("bizObjectType='BDMBOT8001'"));
    assertTrue(sqlStatement.contains("BDMEL.escalationLevel='BDMEL1'"));
  }

  @Test
  public void testGetSelectClause()
    throws AppException, InformationalException {

    final BDMTaskQueryCriteria bdmTaskQueryCriteria =
      new BDMTaskQueryCriteria();

    bdmTaskQueryCriteria.criteria.searchMyTasksOnly = true;
    bdmTaskQueryCriteria.caseUrgentFlagTypeCode = BDMURGENTFLAGTYPE.DIRENEED;
    bdmTaskQueryCriteria.escalationLevel =
      BDMESCALATIONLEVELS.ESCALATION_LEVEL_1;

    final String sqlStatement = impl.getSelectClause(bdmTaskQueryCriteria);

    assertTrue(sqlStatement.contains("SELECT DISTINCT T.TASKID"));
  }

  @Test
  public void testGetOrderBySQL()
    throws AppException, InformationalException {

    final BDMTaskQueryCriteria bdmTaskQueryCriteria =
      new BDMTaskQueryCriteria();

    bdmTaskQueryCriteria.criteria.searchMyTasksOnly = true;
    bdmTaskQueryCriteria.caseUrgentFlagTypeCode = BDMURGENTFLAGTYPE.DIRENEED;
    bdmTaskQueryCriteria.escalationLevel =
      BDMESCALATIONLEVELS.ESCALATION_LEVEL_1;

    assertTrue(impl.getOrderBySQL(bdmTaskQueryCriteria)
      .contains("ORDER BY WFD.DEADLINETIME"));
  }

  @Test
  public void testGetCountSQLStatement()
    throws AppException, InformationalException {

    final BDMTaskQueryCriteria bdmTaskQueryCriteria =
      new BDMTaskQueryCriteria();

    bdmTaskQueryCriteria.criteria.searchMyTasksOnly = true;
    bdmTaskQueryCriteria.caseUrgentFlagTypeCode = BDMURGENTFLAGTYPE.DIRENEED;
    bdmTaskQueryCriteria.escalationLevel =
      BDMESCALATIONLEVELS.ESCALATION_LEVEL_1;

    final String sqlStatement =
      impl.getCountSQLStatement(bdmTaskQueryCriteria);

    assertTrue(sqlStatement.contains("ConcernRoleCommunication"));
    assertTrue(sqlStatement.contains("BDMEscalationLevel"));
    assertTrue(sqlStatement.contains("BDMCaseUrgentFlag"));
    assertTrue(sqlStatement.contains("bizObjectType='BDMBOT8001'"));
    assertTrue(sqlStatement.contains("BDMEL.escalationLevel='BDMEL1'"));
  }

  @Test
  public void testGetBusinessObjectTypeSQL()
    throws AppException, InformationalException {

    final BDMTaskQueryCriteria bdmTaskQueryCriteria =
      new BDMTaskQueryCriteria();

    bdmTaskQueryCriteria.criteria.searchMyTasksOnly = true;
    bdmTaskQueryCriteria.criteria.businessObjectID = 10001L;
    bdmTaskQueryCriteria.criteria.businessObjectType =
      BUSINESSOBJECTTYPE.BDMCOMMUNICATION;
    bdmTaskQueryCriteria.caseUrgentFlagTypeCode = BDMURGENTFLAGTYPE.DIRENEED;
    bdmTaskQueryCriteria.escalationLevel =
      BDMESCALATIONLEVELS.ESCALATION_LEVEL_1;

    final String sqlStatement =
      impl.getBusinessObjectTypeSQL(bdmTaskQueryCriteria);
    assertTrue(
      sqlStatement.contains("BOA.BIZOBJECTTYPE = :businessObjectType"));
    assertTrue(sqlStatement.contains("BOA.BIZOBJECTID = :businessObjectID"));
  }

  @Test
  public void testGetCategorySQL()
    throws AppException, InformationalException {

    final BDMTaskQueryCriteria bdmTaskQueryCriteria =
      new BDMTaskQueryCriteria();

    bdmTaskQueryCriteria.criteria.searchMyTasksOnly = true;
    bdmTaskQueryCriteria.caseUrgentFlagTypeCode = BDMURGENTFLAGTYPE.DIRENEED;
    bdmTaskQueryCriteria.escalationLevel =
      BDMESCALATIONLEVELS.ESCALATION_LEVEL_1;
    bdmTaskQueryCriteria.criteria.taskCategory = TASKCATEGORY.DEFAULT;

    assertTrue(impl.getCategorySQL(bdmTaskQueryCriteria)
      .contains("T.CATEGORY = :taskCategory"));
  }

  @Test
  public void testGetOrgObjectSQL()
    throws AppException, InformationalException {

    final BDMTaskQueryCriteria bdmTaskQueryCriteria =
      new BDMTaskQueryCriteria();

    bdmTaskQueryCriteria.criteria.searchMyTasksOnly = true;
    bdmTaskQueryCriteria.caseUrgentFlagTypeCode = BDMURGENTFLAGTYPE.DIRENEED;
    bdmTaskQueryCriteria.escalationLevel =
      BDMESCALATIONLEVELS.ESCALATION_LEVEL_1;
    bdmTaskQueryCriteria.criteria.assigneeType = ASSIGNEETYPE.USER;
    bdmTaskQueryCriteria.criteria.relatedName = "caseworker";

    final String sqlStatement = impl.getOrgObjectSQL(bdmTaskQueryCriteria);

    assertTrue(sqlStatement.contains("TA.ASSIGNEETYPE = :assigneeType"));
    assertTrue(sqlStatement.contains("TA.RELATEDNAME = :relatedName"));
  }

  @Test
  public void testGetStatusSQL() throws AppException, InformationalException {

    final BDMTaskQueryCriteria bdmTaskQueryCriteria =
      new BDMTaskQueryCriteria();

    bdmTaskQueryCriteria.criteria.searchMyTasksOnly = true;
    bdmTaskQueryCriteria.caseUrgentFlagTypeCode = BDMURGENTFLAGTYPE.DIRENEED;
    bdmTaskQueryCriteria.escalationLevel =
      BDMESCALATIONLEVELS.ESCALATION_LEVEL_1;
    bdmTaskQueryCriteria.criteria.status = TASKSTATUS.INPROGRESS;

    assertTrue(
      impl.getStatusSQL(bdmTaskQueryCriteria).contains("T.STATUS = :status"));
  }
}
