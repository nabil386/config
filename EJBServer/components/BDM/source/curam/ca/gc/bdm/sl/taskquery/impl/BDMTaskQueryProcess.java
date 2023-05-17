/**
 *
 */
package curam.ca.gc.bdm.sl.taskquery.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.hooks.task.impl.BDMSearchTaskUtilities;
import curam.ca.gc.bdm.hooks.task.impl.BDMTaskQueryHook;
import curam.ca.gc.bdm.sl.taskquery.struct.BDMTaskQueryDetails;
import curam.ca.gc.bdm.sl.taskquery.struct.BDMTaskQueryResult;
import curam.core.sl.entity.fact.QueryFactory;
import curam.core.sl.entity.struct.QueryKey;
import curam.core.sl.struct.ReadMultiOperationDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;

/**
 * Customized the OOTB process class to include customized
 * search criteria.
 *
 * @author donghua.jin
 *
 */
public class BDMTaskQueryProcess
  extends curam.ca.gc.bdm.sl.taskquery.base.BDMTaskQueryProcess {

  @Inject
  BDMTaskQueryHook bdmTaskQueryHook;

  /**
   * constructor
   */
  public BDMTaskQueryProcess() {

    super();
    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Customization: call a customized method instead of the OOTB one
   * to accommodate the customized attributes case urgent flags and
   * escalation level on a task.
   *
   * This replaces the OOTB { @link read() } method.
   *
   * @param key
   */
  @Override
  public BDMTaskQueryDetails readBDM(final QueryKey key)
    throws AppException, InformationalException {

    final BDMTaskQueryDetails answer = new BDMTaskQueryDetails();

    answer.queryDtls = QueryFactory.newInstance().read(key);
    // Customization: Task 81204 Begins
    answer.criteria =
      BDMSearchTaskUtilities.getTaskQueryCriteria(answer.queryDtls);
    // Customization: Task 81204 Ends

    return answer;
  }

  /**
   * Customization: call a customized method instead of the OOTB one
   * to accommodate the customized attributes case urgent flags and
   * escalation level on a task.
   *
   * This replaces the OOTB { @link create() } method.
   *
   * @param bdmTaskQueryDetails
   */
  @Override
  public BDMTaskQueryResult
    createBDM(final BDMTaskQueryDetails bdmTaskQueryDetails)
      throws AppException, InformationalException {

    final BDMTaskQueryResult bdmTaskQueryResult = new BDMTaskQueryResult();

    bdmTaskQueryResult.queryID =
      bdmTaskQueryHook.createTaskQuery(bdmTaskQueryDetails);

    return bdmTaskQueryResult;
  }

  /**
   * Customization: call a customized method instead of the OOTB one
   * to accommodate the customized attributes case urgent flags and
   * escalation level on a task.
   *
   * This replaces the OOTB { @link run() } method with same parameters..
   *
   * @param details
   * @param readMultiDetails
   */
  @Override
  public BDMTaskQueryResult runBDM(final BDMTaskQueryDetails details,
    final ReadMultiOperationDetails readMultiDetails)
    throws AppException, InformationalException {

    return bdmTaskQueryHook.runTaskQuery(details, readMultiDetails);
  }

  /**
   * Customization: call a customized method instead of the OOTB one
   * to accommodate the customized attributes case urgent flags and
   * escalation level on a task.
   *
   * This replaces the OOTB { @link modify() } method.
   *
   */
  @Override
  public BDMTaskQueryResult modifyBDM(final BDMTaskQueryDetails details)
    throws AppException, InformationalException {

    final BDMTaskQueryResult result = new BDMTaskQueryResult();
    result.queryID = bdmTaskQueryHook.modifyTaskQuery(details);
    return result;
  }

  /**
   * Customization: call a customized method instead of the OOTB one
   * to accommodate the customized attributes case urgent flags and
   * escalation level on a task.
   *
   * This replaces the OOTB { @link run() } method with same parameters.
   *
   * @param queryKey
   * @param readMultiDetails
   */
  @Override
  public BDMTaskQueryResult runBDM(final QueryKey queryKey,
    final ReadMultiOperationDetails readMultiDetails)
    throws AppException, InformationalException {

    return bdmTaskQueryHook.runTaskQuery(queryKey, readMultiDetails);
  }

}
