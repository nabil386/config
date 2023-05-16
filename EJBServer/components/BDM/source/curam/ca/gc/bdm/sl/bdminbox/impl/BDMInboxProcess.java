/**
 *
 */
package curam.ca.gc.bdm.sl.bdminbox.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskQueryKey;
import curam.ca.gc.bdm.hooks.task.impl.BDMSearchTask;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMTaskQueryResultDetailsList;
import curam.core.sl.struct.ReadMultiOperationDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;

/**
 * Service layer processing for Inbox functionalities.
 *
 * @author donghua.jin
 *
 */
public class BDMInboxProcess
  extends curam.ca.gc.bdm.sl.bdminbox.base.BDMInboxProcess {

  @Inject
  BDMSearchTask searckTask;

  /**
   * Constructor
   */
  public BDMInboxProcess() {

    super();
    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Do task search.
   *
   */
  @Override
  public BDMTaskQueryResultDetailsList searchBDMForTasks(
    final BDMTaskQueryKey searchTaskKey,
    final ReadMultiOperationDetails readMultiDetails)
    throws AppException, InformationalException {

    return searckTask.searchTask(searchTaskKey, readMultiDetails);
  }

}
