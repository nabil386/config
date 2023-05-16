/**
 *
 */
package curam.ca.gc.bdm.hooks.impl;

import com.google.inject.ImplementedBy;
import curam.ca.gc.bdm.sl.taskquery.struct.BDMTaskQueryCriteria;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Implementable;

/**
 * This is a customized version of the OOTB
 * {@link curam.core.hook.task.impl.SearchTaskSQL} class.
 *
 * @author donghua.jin
 *
 */
@Implementable
@ImplementedBy(BDMSearchTaskSQLImpl.class)
public interface BDMSearchTaskSQL {

  /**
   * Returns the SQL select query used to return tasks satisfying the
   * specified criteria.
   *
   * @param criteria The search criteria from which to generate the SQL.
   *
   * @return A string representing the select query.
   */
  public String getSQLStatement(BDMTaskQueryCriteria criteria)
    throws AppException, InformationalException;

  /**
   * Returns the SQL select query used to count the number of tasks satisfying
   * the specified criteria.
   *
   * @param criteria The search criteria from which to generate the SQL.
   *
   * @return A string representing the count query.
   */
  public String getCountSQLStatement(BDMTaskQueryCriteria criteria)
    throws AppException, InformationalException;

  /**
   * Returns the SQL <code>SELECT</code> clause of the query.
   *
   * @param criteria The search criteria from which to generate the SQL.
   *
   * @return A string representing the SQL <code>SELECT</code> clause.
   */
  public String getSelectClause(BDMTaskQueryCriteria criteria)
    throws AppException, InformationalException;

  /**
   * Returns the SQL <code>FROM</code> clause of the query.
   *
   * @param criteria The search criteria from which to generate the SQL.
   *
   * @return A string representing the SQL <code>FROM</code> clause.
   */
  public String getFromClause(BDMTaskQueryCriteria criteria)
    throws AppException, InformationalException;

  /**
   * Returns the SQL <code>WHERE</code> clause of the query.
   *
   * @param criteria The search criteria from which to generate the SQL.
   *
   * @return A string representing the SQL where clause.
   */
  public String getWhereClause(BDMTaskQueryCriteria criteria)
    throws AppException, InformationalException;

  /**
   * Generates the SQL for the organization objects that tasks may be assigned
   * to and selected by the user as search criteria. An organizational object
   * may be an organizational unit, position or job that the current user is
   * a member of or a work queue that the user is subscribed to.
   *
   * @param criteria The search criteria from which to generate the SQL.
   *
   * @return A string representing the SQL to select the organization objects.
   */
  public String getOrgObjectSQL(BDMTaskQueryCriteria criteria)
    throws AppException, InformationalException;

  /**
   * Generates the SQL for the task <code>status<code> selected by the user.
   *
   * &#64;param criteria The search criteria from which to generate the SQL.
   *
   * &#64;return A string representing the SQL to select tasks by
   * <code>status</code>.
   */
  public String getStatusSQL(BDMTaskQueryCriteria criteria)
    throws AppException, InformationalException;

  /**
   * Generates the SQL for the task identifier selected by the user to be used
   * as a search criteria.
   *
   * @param criteria The search criteria from which to generate the SQL.
   *
   * @return A string representing the SQL to select tasks by their identifier.
   */
  public String getTaskIDSQL(BDMTaskQueryCriteria criteria)
    throws AppException, InformationalException;

  /**
   * Generates the <code>reserved by</code> SQL selected by the user.
   *
   * @param criteria The search criteria from which to generate the SQL.
   *
   * @return A string representing the SQL to select tasks by the
   * <code>reserved by</code> field.
   */
  public String getReservedBySQL(BDMTaskQueryCriteria criteria)
    throws AppException, InformationalException;

  /**
   * Generates the SQL for the task <code>priority</code> selected by the user
   * to be used as a search criteria.
   *
   * @param criteria The search criteria from which to generate the SQL.
   *
   * @return A string representing the SQL to select tasks by
   * <code>priority</code>.
   */
  public String getPrioritySQL(BDMTaskQueryCriteria criteria)
    throws AppException, InformationalException;

  /**
   * Generates the SQL for the task <code>deadline</code> selected by the user
   * to be used as a search criteria.
   *
   * @param criteria The search criteria from which to generate the SQL.
   *
   * @return A string representing the SQL to select tasks by
   * <code>deadline</code>.
   */
  public String getDeadlineSQL(BDMTaskQueryCriteria criteria)
    throws AppException, InformationalException;

  /**
   * Generates the SQL for the <code>creation date</code> selected by the user
   * to be used as a search criteria.
   *
   * @param criteria The search criteria from which to generate the SQL.
   *
   * @return A string representing the SQL to select tasks by
   * <code>creation date</code>.
   */
  public String getCreationDateSQL(BDMTaskQueryCriteria criteria)
    throws AppException, InformationalException;

  /**
   * Generates the SQL for the <code>restart date</code> selected by the user
   * to be used as a search criteria.
   *
   * @param criteria The search criteria from which to generate the SQL.
   *
   * @return A string representing the SQL to select tasks by
   * <code>restart date</code>.
   */
  public String getRestartDateSQL(BDMTaskQueryCriteria criteria)
    throws AppException, InformationalException;

  /**
   * Generates the SQL <code>order by</code> clause.
   *
   * @param criteria The search criteria from which to generate the SQL.
   *
   * @return A string representing SQL <code>order by</code> clause.
   */
  public String getOrderBySQL(BDMTaskQueryCriteria criteria)
    throws AppException, InformationalException;

  /**
   * Generates the SQL for the associated <code>business object</code> selected
   * by the user to be used as a search criteria.
   *
   * @param criteria The search criteria from which to generate the SQL.
   *
   * @return A string representing the SQL to select tasks by associated
   * <code>business object</code>.
   */
  public String getBusinessObjectTypeSQL(BDMTaskQueryCriteria criteria)
    throws AppException, InformationalException;

  /**
   * Generates the SQL for the <code>category</code> selected by the user to
   * be used as a search criteria.
   *
   * @param criteria The search criteria from which to generate the SQL.
   *
   * @return A string representing the SQL to select tasks by
   * <code>category</code>.
   */
  public String getCategorySQL(BDMTaskQueryCriteria criteria)
    throws AppException, InformationalException;

}
