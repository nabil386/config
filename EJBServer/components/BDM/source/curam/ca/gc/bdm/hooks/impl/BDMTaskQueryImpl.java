/**
 *
 */
package curam.ca.gc.bdm.hooks.impl;

import curam.ca.gc.bdm.hooks.task.impl.BDMSearchTaskUtilities;
import curam.ca.gc.bdm.hooks.task.impl.BDMTaskQueryHook;
import curam.ca.gc.bdm.sl.taskquery.struct.BDMTaskQueryCriteria;
import curam.ca.gc.bdm.sl.taskquery.struct.BDMTaskQueryDetails;
import curam.ca.gc.bdm.sl.taskquery.struct.BDMTaskQueryResult;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.codetable.QUERYTYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.hook.task.impl.SearchTaskUtilities;
import curam.core.impl.CuramConst;
import curam.core.sl.entity.fact.QueryFactory;
import curam.core.sl.entity.struct.QueryDtls;
import curam.core.sl.entity.struct.QueryKey;
import curam.core.sl.fact.UserAccessFactory;
import curam.core.sl.infrastructure.impl.ClientActionConst;
import curam.core.sl.intf.Query;
import curam.core.sl.struct.BusinessObjectKey;
import curam.core.sl.struct.ReadMultiOperationDetails;
import curam.message.BPOINBOX;
import curam.message.BPOTASKSEARCH;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTableItemIdentifier;

/**
 * Customized Task Query creation and modification.
 *
 * @author donghua.jin
 *
 */
public class BDMTaskQueryImpl implements BDMTaskQueryHook {

  /**
   * {@inheritDoc}
   *
   * Customization:
   * Task 81204:
   *
   * On top of what OOTB validation. This method updated the
   * No Criteria Supplied validation with the new attributes,
   * urgent flag and escalation level.
   *
   * @param details The task query criteria.
   */
  @Override
  public void validateTaskQuery(final BDMTaskQueryDetails details)
    throws AppException, InformationalException {

    final BDMTaskQueryCriteria criteria = details.criteria;

    if (!details.actionIDProperty.equals(ClientActionConst.kRun_Query)) {
      QueryFactory.newInstance().validate(details.queryDtls);
    }

    // Ensure at at least 1 search criteria is entered
    // Customization: Task 81204 Begins
    validateTaskQueryForMinCriteria(details);
    // Customization: Task 81204 Ends

    // Ensure that both the business object type and business object name are
    // both populated or both empty
    if (!"".equals(criteria.criteria.businessObjectType)
      && criteria.criteria.businessObjectID == 0
      || criteria.criteria.businessObjectID != 0
        && "".equals(criteria.criteria.businessObjectType)) {
      final AppException appEx =
        new AppException(BPOINBOX.ERR_TASK_BUSINESS_OBJECT_TYPE_NOT_SELECTED);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(appEx, CuramConst.gkEmpty,
          InformationalElement.InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          1);
    }

    // Ensure the selected business object's type matches the business object
    // type in the drop down list. An exception will be thrown if this not is
    // the case.
    try {
      final BusinessObjectKey busObjKey = new BusinessObjectKey();

      busObjKey.businessObjectID = criteria.criteria.businessObjectID;
      busObjKey.businessObjectType = criteria.criteria.businessObjectType;
      SearchTaskUtilities.getBusinessObjectName(busObjKey);
    } catch (final Exception e) {
      final AppException appEx =
        new AppException(BPOTASKSEARCH.ERR_BUSINESS_OBJECT_TYPE_MISMATCH);

      appEx.arg(new CodeTableItemIdentifier(BUSINESSOBJECTTYPE.TABLENAME,
        criteria.criteria.businessObjectType));
      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(appEx, CuramConst.gkEmpty,
          InformationalElement.InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          1);
    }

    if (criteria.criteria.selectedOrgObjects.length() != 0
      && criteria.criteria.searchMyTasksOnly) {
      // My tasks only and assignee type cannot be selected together
      final AppException appEx = new AppException(
        BPOTASKSEARCH.ERR_USER_TASKS_ONLY_AND_ASSIGNEE_MUTUALLY_EXCLUSIVE);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(appEx, CuramConst.gkEmpty,
          InformationalElement.InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }

    // The deadline due value is mutually exclusive with the deadline from and
    // to dates
    if (criteria.criteria.deadlineDue.length() != 0
      && (!criteria.criteria.deadlineFromDate.isZero()
        || !criteria.criteria.deadlineToDate.isZero())) {
      final AppException appEx =
        new AppException(BPOTASKSEARCH.ERR_DEADLINE_DUE_MUTUALLY_EXCLUSIVE);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(appEx, CuramConst.gkEmpty,
          InformationalElement.InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }

    // Creation from date must be before creation to date
    if (!criteria.criteria.creationToDate.isZero()
      && criteria.criteria.creationFromDate
        .after(criteria.criteria.creationToDate)) {
      final AppException appEx =
        new AppException(BPOTASKSEARCH.ERR_CREATION_FROM_BEFORE_CREATION_TO);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(appEx, CuramConst.gkEmpty,
          InformationalElement.InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }

    // Restart from date must be before restart to date
    if (!criteria.criteria.restartToDate.isZero()
      && criteria.criteria.restartFromDate
        .after(criteria.criteria.restartToDate)) {
      final AppException appEx =
        new AppException(BPOTASKSEARCH.ERR_RESTART_FROM_BEFORE_RESTART_TO);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(appEx, CuramConst.gkEmpty,
          InformationalElement.InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }

    // Deadline from date must be before deadline to date
    if (!criteria.criteria.deadlineToDate.isZero()
      && criteria.criteria.deadlineFromDate
        .after(criteria.criteria.deadlineToDate)) {
      final AppException appEx =
        new AppException(BPOTASKSEARCH.ERR_DEADLINE_FROM_BEFORE_DEADLINE_TO);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(appEx, CuramConst.gkEmpty,
          InformationalElement.InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }

    // Creation from date, to date is mutually exclusive with the last number of
    // days and
    // last number of weeks
    if ((!criteria.criteria.creationFromDate.isZero()
      || !criteria.criteria.creationToDate.isZero())
      && (criteria.criteria.creationLastNumberOfDays != -1
        || criteria.criteria.creationLastNumberOfWeeks != -1)) {
      final AppException appEx =
        new AppException(BPOTASKSEARCH.ERR_CREATION_DATE_MUTUALLY_EXCLUSIVE);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(appEx, CuramConst.gkEmpty,
          InformationalElement.InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          1);
    }

    // Last number of days field is mutually exclusive with last number of weeks
    // field
    else if (criteria.criteria.creationLastNumberOfDays != -1
      && criteria.criteria.creationLastNumberOfWeeks != -1) {
      final AppException appEx = new AppException(
        BPOTASKSEARCH.ERR_CREATION_DATE_NUMBER_DAYS_AND_WEEKS_MUTUALLY_EXCLUSIVE);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(appEx, CuramConst.gkEmpty,
          InformationalElement.InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }

  }

  /**
   * This method validate at least some criteria is selected.
   * Customized OOTB method to factor in the additional criteria
   * from customization.
   *
   * @param details
   * @throws AppException
   * @throws InformationalException
   */
  protected void
    validateTaskQueryForMinCriteria(final BDMTaskQueryDetails details)
      throws AppException, InformationalException {

    final BDMTaskQueryCriteria criteria = details.criteria;

    // Ensure at at least 1 search criteria is entered
    if (!criteria.criteria.searchMyTasksOnly
      && criteria.criteria.selectedOrgObjects.length() == 0
      && criteria.criteria.businessObjectType.length() == 0
      && criteria.criteria.businessObjectID == 0
      && criteria.criteria.status.length() == 0
      && criteria.criteria.creationFromDate.isZero()
      && criteria.criteria.creationLastNumberOfDays == -1
      && criteria.criteria.creationLastNumberOfWeeks == -1
      && criteria.criteria.creationToDate.isZero()
      && criteria.criteria.deadlineDue.length() == 0
      && criteria.criteria.deadlineFromDate.isZero()
      && criteria.criteria.deadlineToDate.isZero()
      && criteria.criteria.restartFromDate.isZero()
      && criteria.criteria.restartToDate.isZero()
      // Customization: 90145
      // && criteria.criteria.taskCategory.length() == 0
      && criteria.taskType.length() == 0
      // Customization: 90145 Ends
      && criteria.criteria.priority.length() == 0
      // Customization: Task 81204 Begins
      && criteria.caseUrgentFlagTypeCode.length() == 0
      && criteria.escalationLevel.length() == 0
    // Customization: Task 81204 Ends
    ) {
      final AppException appEx =
        new AppException(BPOINBOX.ERR_TASK_SEARCH_NO_CRITERIA);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(appEx, CuramConst.gkEmpty,
          InformationalElement.InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          1);
    } else if (criteria.criteria.selectedOrgObjects.length() == 0
      && criteria.criteria.businessObjectType.length() == 0
      && !criteria.criteria.searchMyTasksOnly) {
      // One of assigned to or business object type or my tasks only must be
      // selected.
      final AppException appEx =
        new AppException(BPOTASKSEARCH.ERR_MINIMUM_CRITERIA_NOT_ENTERED);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(appEx, CuramConst.gkEmpty,
          InformationalElement.InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }
  }

  /**
   * Customization: call customized method instead of the OOTB one.
   * Task 81204:
   */
  @Override
  public long createTaskQuery(final BDMTaskQueryDetails details)
    throws AppException, InformationalException {

    final QueryDtls queryDetails = details.queryDtls;

    // Customization: Task 81204 Begins
    queryDetails.query =
      BDMSearchTaskUtilities.formatXMLQueryCriteria(details.criteria);
    // Customization: Task 81204 Ends

    queryDetails.queryName = details.criteria.criteria.queryName;
    queryDetails.queryType = QUERYTYPE.TASKQUERY;
    queryDetails.statusCode = RECORDSTATUS.NORMAL;
    queryDetails.userName =
      UserAccessFactory.newInstance().getUserDetails().userName;

    validateTaskQuery(details);

    TransactionInfo.getInformationalManager().failOperation();

    return QueryFactory.newInstance().create(queryDetails).queryID;
  }

  /**
   * Customization: call customized method instead of the OOTB one.
   * Task 81204:
   */
  @Override
  public long modifyTaskQuery(final BDMTaskQueryDetails details)
    throws AppException, InformationalException {

    final QueryDtls queryDetails = details.queryDtls;

    // Customization: Task 81204 Begins
    queryDetails.query =
      BDMSearchTaskUtilities.formatXMLQueryCriteria(details.criteria);
    // Customization: Task 81204 Ends
    queryDetails.queryName = details.criteria.criteria.queryName;
    queryDetails.queryType = QUERYTYPE.TASKQUERY;
    queryDetails.statusCode = RECORDSTATUS.NORMAL;
    queryDetails.userName =
      UserAccessFactory.newInstance().getUserDetails().userName;

    validateTaskQuery(details);

    TransactionInfo.getInformationalManager().failOperation();

    return QueryFactory.newInstance().modify(queryDetails).queryID;
  }

  /**
   * Customization: call customized method instead of the OOTB one
   * to get proper search criteria.
   *
   * @param key
   * @param readMultiDetails
   */
  @Override
  public BDMTaskQueryResult runTaskQuery(final QueryKey key,
    final ReadMultiOperationDetails readMultiDetails)
    throws AppException, InformationalException {

    final QueryDtls details = QueryFactory.newInstance().read(key);
    final BDMTaskQueryDetails bdmTaskQueryDetails = new BDMTaskQueryDetails();

    bdmTaskQueryDetails.actionIDProperty = ClientActionConst.kRun_Query;
    // Customization: Task 81204 Begins
    bdmTaskQueryDetails.criteria =
      BDMSearchTaskUtilities.getTaskQueryCriteria(details);
    // Customization: Task 81204 Ends
    bdmTaskQueryDetails.queryDtls = details;

    return runTaskQuery(bdmTaskQueryDetails, readMultiDetails);
  }

  /**
   * Customization: call customized method instead of the OOTB one
   * to get proper search criteria.
   *
   * @param details
   * @param readMultiDetails
   */
  @Override
  public BDMTaskQueryResult runTaskQuery(final BDMTaskQueryDetails details,
    final ReadMultiOperationDetails readMultiDetails)
    throws AppException, InformationalException {

    final BDMTaskQueryResult result = new BDMTaskQueryResult();

    details.criteria.criteria.isTaskQuery = true;
    result.queryID = details.queryDtls.queryID;
    result.criteria = details.criteria;
    if (details.queryDtls.queryName.length() == 0) {
      details.queryDtls.queryName = result.criteria.criteria.queryName;
    }

    validateTaskQuery(details);

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    informationalManager.failOperation();

    if (informationalManager.obtainInformationalAsString().length == 0) {
      result.resultList =
        BDMSearchTaskUtilities.searchTask(details.criteria, readMultiDetails);
      final BusinessObjectKey busObjKey = new BusinessObjectKey();

      busObjKey.businessObjectID = details.criteria.criteria.businessObjectID;
      busObjKey.businessObjectType =
        details.criteria.criteria.businessObjectType;
      result.criteria.criteria.businessObjectName = SearchTaskUtilities
        .getBusinessObjectName(busObjKey).businessObjectName;

      // Don't update the run frequency on creation of the query
      if (details.queryDtls.queryID != 0) {
        final Query queryObj = curam.core.sl.fact.QueryFactory.newInstance();
        final QueryKey key = new QueryKey();

        key.queryID = details.queryDtls.queryID;
        queryObj.modifyQueryRunFrequency(key);
      }
    }
    return result;
  }

}
