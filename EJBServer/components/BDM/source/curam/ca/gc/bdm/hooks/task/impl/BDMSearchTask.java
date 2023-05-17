package curam.ca.gc.bdm.hooks.task.impl;

import com.google.inject.ImplementedBy;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskQueryKey;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMTaskQueryResultDetailsList;
import curam.ca.gc.bdm.sl.taskquery.struct.BDMTaskQueryCriteria;
import curam.core.sl.struct.ReadMultiOperationDetails;
import curam.core.struct.Count;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Implementable;

/**
 * Contains a number of hooks which allow for custom specific search actions
 * for the task search and available task search functionality.
 */
@Implementable
@ImplementedBy(BDMSearchTaskImpl.class)
public interface BDMSearchTask {

  /**
   * Returns a list of task details using the specified search criteria. This
   * method can be implemented to limit the number of tasks that are returned
   * to the client.
   *
   * @param searchTaskKey The search criteria.
   * @param readMultiDetails Specifies the maximum size of the return list.
   *
   * @return A list of tasks satisfying the search criteria.
   */
  public BDMTaskQueryResultDetailsList searchTask(
    BDMTaskQueryKey searchTaskKey, ReadMultiOperationDetails readMultiDetails)
    throws AppException, InformationalException;

  /**
   * Validates the task search details before performing a search.
   *
   * @param searchTaskKey The task search criteria key.
   */
  public void validateSearchTask(final BDMTaskQueryKey searchTaskKey)
    throws AppException, InformationalException;

  /**
   * Returns a count of the tasks satisfying the specified search criteria.
   *
   * @param criteria The search criteria.
   *
   * @return The number of tasks satisfying the search criteria.
   */
  public Count countTasks(BDMTaskQueryCriteria criteria)
    throws AppException, InformationalException;
}
