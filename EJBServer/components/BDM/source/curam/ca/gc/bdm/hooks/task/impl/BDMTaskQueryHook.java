package curam.ca.gc.bdm.hooks.task.impl;

import com.google.inject.ImplementedBy;
import curam.ca.gc.bdm.hooks.impl.BDMTaskQueryImpl;
import curam.ca.gc.bdm.sl.taskquery.struct.BDMTaskQueryDetails;
import curam.ca.gc.bdm.sl.taskquery.struct.BDMTaskQueryResult;
import curam.core.sl.entity.struct.QueryKey;
import curam.core.sl.struct.ReadMultiOperationDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Implementable;

/**
 * This class replaces the OOTB { @link curam.core.hook.task.impl.TaskQuery }
 * class.
 *
 * Contains a number of hook points which allow for custom specific processing
 * of task query actions.
 *
 * @author donghua.jin
 *
 */
@Implementable
@ImplementedBy(BDMTaskQueryImpl.class)
public interface BDMTaskQueryHook {

  /**
   * Creates a task query.
   *
   * @param details The task query criteria for a query.
   *
   * @return The identifier of the new query.
   */
  public long createTaskQuery(BDMTaskQueryDetails details)
    throws AppException, InformationalException;

  /**
   * Modifies a task query's criteria.
   *
   * @param details The task query criteria to replace the existing criteria.
   *
   * @return The identifier of the modified query.
   */
  public long modifyTaskQuery(BDMTaskQueryDetails details)
    throws AppException, InformationalException;

  /**
   * Runs a task query that has been previously saved.
   *
   * @param key The identifier of the task query.
   * @param readMultiDetails Specifies the maximum size of the return list.
   *
   * @return A list of tasks that satisfy the query's criteria.
   */
  public BDMTaskQueryResult runTaskQuery(QueryKey key,
    ReadMultiOperationDetails readMultiDetails)
    throws AppException, InformationalException;

  /**
   * Runs a task query using the criteria details as entered by the user.
   *
   * @param details The task query criteria.
   * @param readMultiDetails Specifies the maximum size of the return list.
   *
   * @return A list of tasks that satisfy the query criteria.
   */
  public BDMTaskQueryResult runTaskQuery(BDMTaskQueryDetails details,
    ReadMultiOperationDetails readMultiDetails)
    throws AppException, InformationalException;

  /**
   * Validates the task query criteria before performing a search.
   *
   * @param details The task query criteria.
   */
  public void validateTaskQuery(BDMTaskQueryDetails details)
    throws AppException, InformationalException;
}
