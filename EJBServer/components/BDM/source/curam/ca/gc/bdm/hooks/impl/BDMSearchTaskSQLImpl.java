/**
 *
 */
package curam.ca.gc.bdm.hooks.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.sl.taskquery.struct.BDMTaskQueryCriteria;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.core.hook.task.impl.SearchTaskSQL;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;

/**
 * Customize the OOTB class to add urgent flags and escalation level.
 *
 * @author donghua.jin
 *
 */
public class BDMSearchTaskSQLImpl implements BDMSearchTaskSQL {

  protected final String kBothEscLvlnUgntFlgFromClause =
    " INNER JOIN BizObjAssociation BOAELUF ON ( BOAELUF.TASKID = T.TASKID ) INNER JOIN BDMEscalationLevel BDMEL ON (BDMEL.COMMUNICATIONID = BOAELUF.BIZOBJECTID ) INNER JOIN ConcernRoleCommunication COMM ON (BDMEL.COMMUNICATIONID = COMM.COMMUNICATIONID ) ";

  protected final String kEscLvlOnlyFromClause =
    " INNER JOIN BizObjAssociation BOAEL ON ( BOAEL.TASKID = T.TASKID ) INNER JOIN BDMEscalationLevel BDMEL ON (BDMEL.COMMUNICATIONID = BOAEL.BIZOBJECTID ) ";

  protected final String kUgntFlgOnlyFromClause =
    " INNER JOIN BizObjAssociation BOAUF ON ( BOAUF.TASKID = T.TASKID ) ";

  protected final String kTaskTypeOnlyFromClause =
    " INNER JOIN BDMTask BDMTSK ON ( BDMTSK.TASKID = T.TASKID ) ";

  protected final String kBothEscLvlnUgntFlgWhereClause =
    " BOAELUF.bizObjectType='" + BUSINESSOBJECTTYPE.BDMCOMMUNICATION
      + "' AND BDMEL.escalationLevel= :escalationLevel AND COMM.statusCode='RST1' AND EXISTS (SELECT 1 FROM BDMCaseUrgentFlag BDMUF WHERE BDMUF.caseID = COMM.caseID AND BDMUF.type = :caseUrgentFlagTypeCode AND BDMUF.recordStatus='RST1' AND ( CURRENT_DATE BETWEEN BDMUF.startDate AND COALESCE(BDMUF.endDate, CURRENT_DATE) OR BDMUF.startDate > CURRENT_DATE ) ) ";

  protected final String kEscLvlOnlyWhereClause =
    " BOAEL.bizObjectType='" + BUSINESSOBJECTTYPE.BDMCOMMUNICATION
      + "'  AND BDMEL.escalationLevel= :escalationLevel ";

  protected final String kUgntFlgOnlyWhereClause = " BOAUF.bizObjectType='"
    + BUSINESSOBJECTTYPE.CASE
    + "' AND EXISTS (SELECT 1 FROM BDMCaseUrgentFlag BDMUF WHERE BDMUF.caseID = BOAUF.bizObjectID AND BDMUF.type = :caseUrgentFlagTypeCode AND BDMUF.recordStatus='RST1' AND ( CURRENT_DATE BETWEEN BDMUF.startDate AND COALESCE(BDMUF.endDate, CURRENT_DATE) OR BDMUF.startDate > CURRENT_DATE ) ) ";

  protected final String kTaskTypeOnlyWhereClause =
    " BDMTSK.type= :taskType ";

  protected final String kCountSqlSelectClause =
    "SELECT COUNT (*) INTO :numberOfRecords ";

  protected final String kSqlKeyWordWhere = " WHERE ";

  protected final String kSqlKeyWordAnd = " AND ";

  @Inject
  SearchTaskSQL searchTaskSql;

  /**
   * constructor
   */
  public BDMSearchTaskSQLImpl() {

    super();
    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * {@inheritDoc}
   *
   * Customized to include urgent flag and escalation level.
   *
   */
  @Override
  public String getFromClause(final BDMTaskQueryCriteria criteria)
    throws AppException, InformationalException {

    final StringBuilder fromClause =
      new StringBuilder(searchTaskSql.getFromClause(criteria.criteria));

    if (!StringUtil.isNullOrEmpty(criteria.escalationLevel)
      && !StringUtil.isNullOrEmpty(criteria.caseUrgentFlagTypeCode)) {
      fromClause.append(kBothEscLvlnUgntFlgFromClause);
    } else {

      if (!StringUtil.isNullOrEmpty(criteria.escalationLevel)) {
        fromClause.append(kEscLvlOnlyFromClause);
      }

      if (!StringUtil.isNullOrEmpty(criteria.caseUrgentFlagTypeCode)) {
        fromClause.append(kUgntFlgOnlyFromClause);
      }
    }

    if (!StringUtil.isNullOrEmpty(criteria.taskType)) {
      fromClause.append(kTaskTypeOnlyFromClause);
    }

    return fromClause.toString();
  }

  /**
   * {@inheritDoc}
   *
   * Customized to include urgent flag and escalation level.
   */
  @Override
  public String getWhereClause(final BDMTaskQueryCriteria criteria)
    throws AppException, InformationalException {

    final StringBuilder whereClause =
      new StringBuilder(searchTaskSql.getWhereClause(criteria.criteria));

    if (!StringUtil.isNullOrEmpty(criteria.escalationLevel)
      && !StringUtil.isNullOrEmpty(criteria.caseUrgentFlagTypeCode)) {

      appendWhereClause(whereClause,
        getWhereClauseForBothEscalationLevelAndUrgentFlag(criteria));

    } else {
      if (!StringUtil.isNullOrEmpty(criteria.escalationLevel)) {

        appendWhereClause(whereClause,
          getWhereClauseForEscalationLevel(criteria));
      }

      if (!StringUtil.isNullOrEmpty(criteria.caseUrgentFlagTypeCode)) {

        appendWhereClause(whereClause, getWhereClauseForUrgentFlag(criteria));
      }
    }

    if (!StringUtil.isNullOrEmpty(criteria.taskType)) {

      appendWhereClause(whereClause, getWhereClauseForTaskType(criteria));
    }

    return whereClause.toString();
  }

  /**
   * Construct the SQL statement for both urgent flag and escalation level.
   *
   * @param criteria
   * @return
   */
  protected String getWhereClauseForBothEscalationLevelAndUrgentFlag(
    final BDMTaskQueryCriteria criteria) {

    return kBothEscLvlnUgntFlgWhereClause;
  }

  /**
   * Construct the SQL statement for escalation level.
   *
   * @param criteria
   * @return
   */
  protected String
    getWhereClauseForEscalationLevel(final BDMTaskQueryCriteria criteria) {

    return kEscLvlOnlyWhereClause;
  }

  /**
   * Construct the SQL statement for urgent flag.
   *
   * @param criteria
   * @return
   */
  protected String
    getWhereClauseForUrgentFlag(final BDMTaskQueryCriteria criteria) {

    return kUgntFlgOnlyWhereClause;
  }

  /**
   * Construct the SQL statement for task type.
   *
   * @param criteria
   * @return
   */
  protected String
    getWhereClauseForTaskType(final BDMTaskQueryCriteria criteria) {

    return kTaskTypeOnlyWhereClause;
  }

  /**
   * Append the new where clause to an existing one.
   *
   * @param currentWhereClause
   * @param whereClauseToAppend
   */
  protected void appendWhereClause(final StringBuilder currentWhereClause,
    final String whereClauseToAppend) {

    if (currentWhereClause.length() == 0) {
      currentWhereClause.append(kSqlKeyWordWhere);
    } else {
      currentWhereClause.append(kSqlKeyWordAnd);
    }

    currentWhereClause.append(whereClauseToAppend);
  }

  /**
   * {@inheritDoc}
   *
   * Call the OOTB method to complete the same functionality with a BDM
   * parameter.
   */
  @Override
  public String
    getSQLStatement(final BDMTaskQueryCriteria bdmTaskQueryCriteria)
      throws AppException, InformationalException {

    return getSelectClause(bdmTaskQueryCriteria)
      + getFromClause(bdmTaskQueryCriteria)
      + getWhereClause(bdmTaskQueryCriteria)
      + getOrderBySQL(bdmTaskQueryCriteria);
  }

  /**
   * {@inheritDoc}
   *
   * Call the OOTB method to complete the same functionality with a BDM
   * parameter.
   */
  @Override
  public String getSelectClause(final BDMTaskQueryCriteria criteria)
    throws AppException, InformationalException {

    return searchTaskSql.getSelectClause(criteria.criteria);
  }

  /**
   * {@inheritDoc}
   *
   * Call the OOTB method to complete the same functionality with a BDM
   * parameter.
   */
  @Override
  public String getOrderBySQL(final BDMTaskQueryCriteria criteria)
    throws AppException, InformationalException {

    return searchTaskSql.getOrderBySQL(criteria.criteria);
  }

  /**
   * {@inheritDoc}
   *
   * Call the OOTB method to complete the same functionality with a BDM
   * parameter.
   */
  @Override
  public String getCountSQLStatement(final BDMTaskQueryCriteria criteria)
    throws AppException, InformationalException {

    final StringBuffer selectClause = new StringBuffer();

    selectClause.append(kCountSqlSelectClause);

    return selectClause + getFromClause(criteria) + getWhereClause(criteria);
  }

  /**
   * {@inheritDoc}
   *
   * Call the OOTB method to complete the same functionality with a BDM
   * parameter.
   */
  @Override
  public String getBusinessObjectTypeSQL(final BDMTaskQueryCriteria criteria)
    throws AppException, InformationalException {

    return searchTaskSql.getBusinessObjectTypeSQL(criteria.criteria);
  }

  /**
   * {@inheritDoc}
   *
   * Call the OOTB method to complete the same functionality with a BDM
   * parameter.
   */
  @Override
  public String getCategorySQL(final BDMTaskQueryCriteria criteria)
    throws AppException, InformationalException {

    return searchTaskSql.getCategorySQL(criteria.criteria);
  }

  /**
   * {@inheritDoc}
   *
   * Call the OOTB method to complete the same functionality with a BDM
   * parameter.
   */
  @Override
  public String getOrgObjectSQL(final BDMTaskQueryCriteria criteria)
    throws AppException, InformationalException {

    return searchTaskSql.getOrgObjectSQL(criteria.criteria);
  }

  /**
   * {@inheritDoc}
   *
   * Call the OOTB method to complete the same functionality with a BDM
   * parameter.
   */
  @Override
  public String getStatusSQL(final BDMTaskQueryCriteria criteria)
    throws AppException, InformationalException {

    return searchTaskSql.getStatusSQL(criteria.criteria);
  }

  /**
   * {@inheritDoc}
   *
   * Call the OOTB method to complete the same functionality with a BDM
   * parameter.
   */
  @Override
  public String getTaskIDSQL(final BDMTaskQueryCriteria criteria)
    throws AppException, InformationalException {

    return searchTaskSql.getTaskIDSQL(criteria.criteria);
  }

  /**
   * {@inheritDoc}
   *
   * Call the OOTB method to complete the same functionality with a BDM
   * parameter.
   */
  @Override
  public String getReservedBySQL(final BDMTaskQueryCriteria criteria)
    throws AppException, InformationalException {

    return searchTaskSql.getReservedBySQL(criteria.criteria);
  }

  /**
   * {@inheritDoc}
   *
   * Call the OOTB method to complete the same functionality with a BDM
   * parameter.
   */
  @Override
  public String getPrioritySQL(final BDMTaskQueryCriteria criteria)
    throws AppException, InformationalException {

    return searchTaskSql.getPrioritySQL(criteria.criteria);
  }

  /**
   * {@inheritDoc}
   *
   * Call the OOTB method to complete the same functionality with a BDM
   * parameter.
   */
  @Override
  public String getDeadlineSQL(final BDMTaskQueryCriteria criteria)
    throws AppException, InformationalException {

    return searchTaskSql.getDeadlineSQL(criteria.criteria);
  }

  /**
   * {@inheritDoc}
   *
   * Call the OOTB method to complete the same functionality with a BDM
   * parameter.
   */
  @Override
  public String getCreationDateSQL(final BDMTaskQueryCriteria criteria)
    throws AppException, InformationalException {

    return searchTaskSql.getCreationDateSQL(criteria.criteria);
  }

  /**
   * {@inheritDoc}
   *
   * Call the OOTB method to complete the same functionality with a BDM
   * parameter.
   */
  @Override
  public String getRestartDateSQL(final BDMTaskQueryCriteria criteria)
    throws AppException, InformationalException {

    return searchTaskSql.getCreationDateSQL(criteria.criteria);
  }
}
