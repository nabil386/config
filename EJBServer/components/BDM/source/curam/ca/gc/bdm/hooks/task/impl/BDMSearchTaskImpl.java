package curam.ca.gc.bdm.hooks.task.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskQueryKey;
import curam.ca.gc.bdm.hooks.impl.BDMSearchTaskSQL;
import curam.ca.gc.bdm.message.BDMTASKSEARCH;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMTaskQueryResultDetails;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMTaskQueryResultDetailsList;
import curam.ca.gc.bdm.sl.taskquery.struct.BDMTaskQueryCriteria;
import curam.ca.gc.bdm.sl.taskquery.struct.BDMTaskQueryCriteriaSQLParams;
import curam.ca.gc.bdm.util.impl.BDMTaskUtil;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.core.hook.task.impl.SearchTaskUtilities;
import curam.core.impl.CuramConst;
import curam.core.sl.fact.TaskAssignmentFactory;
import curam.core.sl.impl.InboxListSize;
import curam.core.sl.struct.BusinessObjectKey;
import curam.core.sl.struct.ReadMultiOperationDetails;
import curam.core.sl.struct.TaskAssigneeTypeKey;
import curam.core.sl.struct.TaskQueryKey;
import curam.core.struct.Count;
import curam.core.struct.OrganisationObjectDetails;
import curam.core.struct.PriorityCodeDetails;
import curam.message.BPOINBOX;
import curam.message.BPOTASKSEARCH;
import curam.util.dataaccess.CuramValueList;
import curam.util.dataaccess.DataAccess;
import curam.util.dataaccess.DataAccessFactory;
import curam.util.dataaccess.DatabaseMetaData;
import curam.util.dataaccess.DynamicDataAccess;
import curam.util.dataaccess.ReadmultiOperation;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement;
import curam.util.exception.InformationalException;
import curam.util.exception.LocalisableString;
import curam.util.exception.ReadmultiMaxException;
import curam.util.persistence.GuiceWrapper;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTableItemIdentifier;
import curam.util.type.DateTime;
import curam.util.workflow.impl.LocalizableStringResolver;
import curam.util.workflow.struct.TaskWDOSnapshotDetails;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Customized version of OOTB class { @link SearchTaskImpl }.
 *
 * @author donghua.jin
 *
 */
public class BDMSearchTaskImpl implements BDMSearchTask {

  /**
   * This date time is used when a task has no specified deadline.
   */
  static final DateTime highDate =
    new DateTime(new GregorianCalendar(9999, 11, 31));

  /**
   * The task search SQL hook.
   */
  @Inject
  protected BDMSearchTaskSQL bdmSearchTaskSQL;

  /**
   * Constructor.
   */
  public BDMSearchTaskImpl() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Customized version of the OOTB { @link validateSearchTask() } method
   */
  @Override
  public void validateSearchTask(final BDMTaskQueryKey bdmSearchTaskKey)
    throws AppException, InformationalException {

    boolean criteriaEntered = true;

    if ("".equals(bdmSearchTaskKey.businessObjectType)
      && bdmSearchTaskKey.businessObjectID == 0
      && bdmSearchTaskKey.deadlineTime.equals(DateTime.kZeroDateTime)
      && "".equals(bdmSearchTaskKey.priority)
      && "".equals(bdmSearchTaskKey.status)
      && "".equals(bdmSearchTaskKey.assigneeType)
      && "".equals(bdmSearchTaskKey.relatedName)
      && "".equals(bdmSearchTaskKey.assignedToID)
      && "".equals(bdmSearchTaskKey.caseUrgentFlagTypeCode)
      && "".equals(bdmSearchTaskKey.escalationLevel)
      && "".equals(bdmSearchTaskKey.taskType)) {
      criteriaEntered = false;
    }

    // The user must enter some criteria if the task ID field is empty
    if (bdmSearchTaskKey.taskID == 0 && !criteriaEntered) {
      final AppException appEx =
        new AppException(curam.message.BPOINBOX.ERR_TASK_SEARCH_NO_CRITERIA);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(appEx, CuramConst.gkEmpty,
          InformationalElement.InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    } else if (bdmSearchTaskKey.taskID != 0 && criteriaEntered) {
      // The user cannot supply a task ID and other criteria together
      final AppException appEx = new AppException(
        curam.message.BPOINBOX.ERR_TASK_SEARCH_BY_TASKID_ONLY);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(appEx, CuramConst.gkEmpty,
          InformationalElement.InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    } else if (!"".equals(bdmSearchTaskKey.assigneeType)
      && "".equals(bdmSearchTaskKey.assignedToID)
      || !"".equals(bdmSearchTaskKey.assignedToID)
        && "".equals(bdmSearchTaskKey.assigneeType)) {
      // Ensure that both the assignee type and assignee name are both populated
      // or both empty
      final AppException appEx =
        new AppException(BPOINBOX.ERR_TASK_ASSIGNED_TO_NOT_SELECTED);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(appEx, CuramConst.gkEmpty,
          InformationalElement.InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    } else if (!"".equals(bdmSearchTaskKey.businessObjectType)
      && bdmSearchTaskKey.businessObjectID == 0
      || bdmSearchTaskKey.businessObjectID != 0
        && "".equals(bdmSearchTaskKey.businessObjectType)) {
      // Ensure that both the business object type and business object name are
      // both populated or both empty
      final AppException appEx =
        new AppException(BPOINBOX.ERR_TASK_BUSINESS_OBJECT_TYPE_NOT_SELECTED);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(appEx, CuramConst.gkEmpty,
          InformationalElement.InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    } else if (bdmSearchTaskKey.taskID == 0
      && bdmSearchTaskKey.assigneeType.length() == 0
      && bdmSearchTaskKey.businessObjectType.length() == 0) {
      // One of assigned to or business object type must be selected.
      final AppException appEx =
        new AppException(BPOINBOX.ERR_TASK_SEARCH_MINIMUM_CRITERIA);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(appEx, CuramConst.gkEmpty,
          InformationalElement.InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    } else {
      // Ensure the selected assignee's type matches the assignee type in the
      // drop down list. An exception will be thrown if this not is the case.
      try {
        final TaskAssigneeTypeKey assigneeTypeKey = new TaskAssigneeTypeKey();

        assigneeTypeKey.assigneeType = bdmSearchTaskKey.assigneeType;
        assigneeTypeKey.assignedToID = bdmSearchTaskKey.assignedToID;
        TaskAssignmentFactory.newInstance().getAssigneeName(assigneeTypeKey);
      } catch (final Exception e) {
        final AppException appEx =
          new AppException(BPOTASKSEARCH.ERR_ASSIGNEE_TYPE_MISMATCH);

        appEx.arg(new CodeTableItemIdentifier(
          curam.codetable.TARGETITEMTYPE.TABLENAME,
          bdmSearchTaskKey.assigneeType));
        curam.core.sl.infrastructure.impl.ValidationManagerFactory
          .getManager().addInfoMgrExceptionWithLookup(appEx,
            CuramConst.gkEmpty, InformationalElement.InformationalType.kError,
            curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
            0);
      }

      // Ensure the selected business object's type matches the business object
      // type in the drop down list. An exception will be thrown if this not is
      // the case.
      try {
        final BusinessObjectKey busObjKey = new BusinessObjectKey();

        busObjKey.businessObjectID = bdmSearchTaskKey.businessObjectID;
        busObjKey.businessObjectType = bdmSearchTaskKey.businessObjectType;
        SearchTaskUtilities.getBusinessObjectName(busObjKey);
      } catch (final Exception e) {
        final AppException appEx =
          new AppException(BPOTASKSEARCH.ERR_BUSINESS_OBJECT_TYPE_MISMATCH);

        appEx.arg(new CodeTableItemIdentifier(BUSINESSOBJECTTYPE.TABLENAME,
          bdmSearchTaskKey.businessObjectType));
        curam.core.sl.infrastructure.impl.ValidationManagerFactory
          .getManager().addInfoMgrExceptionWithLookup(appEx,
            CuramConst.gkEmpty, InformationalElement.InformationalType.kError,
            curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
            0);
      }
    }
  }

  /**
   * Task 25516 Validate search task - add validation when searching by
   * skillType
   */
  public void validateSearchTask(final TaskQueryKey searchTaskKey)
    throws AppException, InformationalException {

    final BDMTaskQueryKey bdmSearchTaskKey = new BDMTaskQueryKey();
    bdmSearchTaskKey.assign(searchTaskKey);
    validateSearchTask(bdmSearchTaskKey);

    if (BUSINESSOBJECTTYPE.TASKSKILLTYPE
      .equals(searchTaskKey.businessObjectType)
      && searchTaskKey.businessObjectID != 0
      && "".equals(searchTaskKey.assignedToID)
      && DateTime.kZeroDateTime.equals(searchTaskKey.deadlineTime)) {
      throw new AppException(
        BDMTASKSEARCH.ERR_SKILLTYPE_CRITERIA_WITH_ASSIGNED_OR_DEADLINE_ONLY);
    }

  }

  /**
   * Customized the OOTB method to pull new custom attributes.
   *
   * @param bdmSearchTaskKey
   * @param readMultiDetails
   * @param a list of search results
   */
  @Override
  public BDMTaskQueryResultDetailsList searchTask(
    final BDMTaskQueryKey bdmSearchTaskKey,
    final ReadMultiOperationDetails readMultiDetails)
    throws AppException, InformationalException {

    validateSearchTask(bdmSearchTaskKey);

    TransactionInfo.getInformationalManager().failOperation();

    // Deadline time as search criteria
    bdmSearchTaskKey.searchDateTime = bdmSearchTaskKey.deadlineTime;

    final BDMTaskQueryCriteria bdmTaskQueryCriteria =
      new BDMTaskQueryCriteria();

    bdmTaskQueryCriteria.caseUrgentFlagTypeCode =
      bdmSearchTaskKey.caseUrgentFlagTypeCode;

    bdmTaskQueryCriteria.escalationLevel = bdmSearchTaskKey.escalationLevel;

    bdmTaskQueryCriteria.taskType = bdmSearchTaskKey.taskType;

    bdmTaskQueryCriteria.criteria.assign(bdmSearchTaskKey);

    // The criteria struct is also used for the saved task queries, so we must
    // set the creation last number of days and weeks attribute to the sentinel
    // value of -1, otherwise they will default to 0 and show in the SQL query
    // which would be incorrect.
    bdmTaskQueryCriteria.criteria.creationLastNumberOfDays = -1;
    bdmTaskQueryCriteria.criteria.creationLastNumberOfWeeks = -1;

    final BDMTaskQueryResultDetailsList resultList =
      searchTask(bdmTaskQueryCriteria, readMultiDetails);

    return resultList;
  }

  /**
   * Do the search with customized parameters.
   *
   * @param criteria
   * @param readMultiDetails
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  protected BDMTaskQueryResultDetailsList searchTask(
    final BDMTaskQueryCriteria bdmTaskQueryCriteria,
    final ReadMultiOperationDetails readMultiDetails)
    throws AppException, InformationalException {

    // variables used to return results
    final BDMTaskQueryResultDetailsList bdmSearchedTaskDetailsList =
      new BDMTaskQueryResultDetailsList();

    bdmSearchedTaskDetailsList.taskDetailsList
      .addAll(performSearch(bdmTaskQueryCriteria, readMultiDetails));

    // size of records
    final int searchSize = bdmSearchedTaskDetailsList.taskDetailsList.size();

    if (searchSize > 0) {
      for (int i = 0; i < searchSize; i++) {

        final TaskWDOSnapshotDetails taskWDOSnapshotDetails =
          new TaskWDOSnapshotDetails();

        taskWDOSnapshotDetails.taskID =
          bdmSearchedTaskDetailsList.taskDetailsList.item(i).taskID;
        taskWDOSnapshotDetails.wdoSnapshot =
          bdmSearchedTaskDetailsList.taskDetailsList.item(i).subject;

        bdmSearchedTaskDetailsList.taskDetailsList.item(i).subject =
          LocalizableStringResolver.getTaskStringResolver()
            .getSubjectForTask(taskWDOSnapshotDetails);
      }
    } else {
      final AppException e = new AppException(
        curam.message.BPOINBOX.INF_TASK_XFV_MATCHED_RESULTS_FOUND);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(
          e.arg(bdmSearchedTaskDetailsList.taskDetailsList.size()),
          curam.core.impl.CuramConst.gkEmpty,
          curam.util.exception.InformationalElement.InformationalType.kWarning,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);

    }

    return bdmSearchedTaskDetailsList;
  }

  /**
   * Perform the query with customized parameters.
   *
   * @param bdmTaskQueryCriteria
   * @param readMultiDetails
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  protected List<BDMTaskQueryResultDetails> performSearch(
    final BDMTaskQueryCriteria bdmTaskQueryCriteria,
    final ReadMultiOperationDetails readMultiDetails)
    throws AppException, InformationalException {

    final InboxListSize<BDMTaskQueryResultDetails> rmop =
      new InboxListSize<BDMTaskQueryResultDetails>(
        readMultiDetails.maxListSize,
        new BDMTaskQueryResultDetailsList().taskDetailsList);

    final Set<BDMTaskQueryResultDetails> resultList =
      new TreeSet<BDMTaskQueryResultDetails>(
        new Comparator<BDMTaskQueryResultDetails>() {

          @Override
          public int compare(final BDMTaskQueryResultDetails o1,
            final BDMTaskQueryResultDetails o2) {

            return Long.valueOf(o1.taskID).compareTo(o2.taskID);
          }
        });

    int maxListSize = readMultiDetails.maxListSize;

    if (maxListSize == 0) {
      maxListSize = Integer.MAX_VALUE;
    }

    try {
      // All the criteria must be bound to struct attributes before the search.
      // This means all the selected org objects and priority lists must be
      // flattened and each criteria in the flattened list must be searched
      // separately in a loop.
      final Iterator<PriorityCodeDetails> priorityIter =
        SearchTaskUtilities.getTaskPriorityCodes(
          bdmTaskQueryCriteria.criteria.selectedPriorities).iterator();
      final List<OrganisationObjectDetails> orgObjectList =
        SearchTaskUtilities
          .getSelectedOrgObjects(bdmTaskQueryCriteria.criteria);

      // Priority is an optional search criteria, so it doesn't have to exist in
      // the criteria, however we still need to search for the org objects which
      // will exist.
      do {
        if (priorityIter.hasNext()) {
          bdmTaskQueryCriteria.criteria.priority = priorityIter.next().code;
        }
        final Iterator<OrganisationObjectDetails> orgObjectIter =
          orgObjectList.iterator();

        while (orgObjectIter.hasNext()) {
          SearchTaskUtilities.setAssigneeCriteria(
            bdmTaskQueryCriteria.criteria, orgObjectIter.next());
          // Call dynamic SQL API to execute SQL
          final String sql =
            bdmSearchTaskSQL.getSQLStatement(bdmTaskQueryCriteria);

          final BDMTaskQueryCriteriaSQLParams params =
            new BDMTaskQueryCriteriaSQLParams();
          params.assign(bdmTaskQueryCriteria.criteria);
          params.caseUrgentFlagTypeCode =
            bdmTaskQueryCriteria.caseUrgentFlagTypeCode;
          params.escalationLevel = bdmTaskQueryCriteria.escalationLevel;
          params.taskType = bdmTaskQueryCriteria.taskType;

          executeSQL(BDMTaskQueryResultDetails.class, params, rmop, sql);
          final Iterator<BDMTaskQueryResultDetails> iter =
            rmop.getResult().iterator();

          while (iter.hasNext() && resultList.size() < maxListSize) {
            // Add to the result list until the max list size is reached
            resultList.add(iter.next());
          }
          if (iter.hasNext()) {
            throw new ReadmultiMaxException();
          }
        }
      } while (priorityIter.hasNext());
    } catch (final ReadmultiMaxException e) {
      // if the read multi max was reached on the any query then save the result
      resultList.addAll(rmop.getResult().subList(0, maxListSize));
      final Count count = countTasks(bdmTaskQueryCriteria);
      final LocalisableString str =
        new LocalisableString(BPOINBOX.INF_READMULTI_MAX_EXCEEDED);

      str.arg(rmop.getMaximum());
      str.arg(count.numberOfRecords);
      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(str, CuramConst.gkEmpty,
          InformationalElement.InformationalType.kWarning,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }

    // Checking the reserving user to ensure the correct options are presented
    // on each task
    final Iterator<BDMTaskQueryResultDetails> iter = resultList.iterator();

    while (iter.hasNext()) {
      final BDMTaskQueryResultDetails nextResult = iter.next();

      // Check if the task is reserved by the user
      if (nextResult.reservedBy.trim()
        .equalsIgnoreCase(TransactionInfo.getProgramUser().trim())) {
        nextResult.isReservedByUser = true;
      } else {
        nextResult.isReservedByUser = false;
      }

      nextResult.isOverdue =
        SearchTaskUtilities.isOverdue(nextResult.deadlineDateTime);
    }
    final List<BDMTaskQueryResultDetails> result =
      new ArrayList<BDMTaskQueryResultDetails>();

    result.addAll(resultList);

    for (final BDMTaskQueryResultDetails item : result) {
      item.caseUrgentFlagStr =
        BDMTaskUtil.getCaseUrgentFlagsByTaskID(item.taskID);
      item.escalationLevelDesc =
        BDMTaskUtil.getEscalationLevelsByTaskID(item.taskID);
    }
    // sort in ascending order.
    Collections.sort(result, new Comparator<BDMTaskQueryResultDetails>() {

      @Override
      public int compare(final BDMTaskQueryResultDetails o1,
        final BDMTaskQueryResultDetails o2) {

        DateTime deadline1 = o1.deadlineDateTime;
        DateTime deadline2 = o2.deadlineDateTime;

        if (deadline1.equals(DateTime.kZeroDateTime)) {
          deadline1 = highDate;
        }
        if (deadline2.equals(DateTime.kZeroDateTime)) {
          deadline2 = highDate;
        }
        return deadline1.compareTo(deadline2);
      }
    });
    return result;
  }

  /**
   * Help method to execute a SQL statement. Same as OOTB.
   *
   * @param <T>
   * @param dtlsClass
   * @param key
   * @param readmultiOperation
   * @param dynamicSQL
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  protected static final <T> CuramValueList<T> executeSQL(
    final Class<T> dtlsClass, final Object key,
    final ReadmultiOperation<?> readmultiOperation, final String dynamicSQL)
    throws AppException, InformationalException {

    // The Class of the String.
    Class<?> keyClass = null;

    if (key != null) {
      keyClass = key.getClass();
    }

    final DatabaseMetaData dbmd = new DatabaseMetaData(dtlsClass, keyClass,
      curam.util.dataaccess.DataAccess.kNoArg2Class,
      curam.util.dataaccess.DataAccess.kNsMulti, "DynamicSQL", "dynamicSQL",
      false, dynamicSQL);

    final DataAccess ns = DataAccessFactory.newInstance(dbmd);

    final CuramValueList<T> results = new CuramValueList<T>(dtlsClass);

    ns.execute(null, key, readmultiOperation, false);

    return results;
  }

  /**
   * Count tasks with customized parameter.
   *
   * @param bdmTaskQueryCriteria
   */
  @Override
  public Count countTasks(final BDMTaskQueryCriteria bdmTaskQueryCriteria)
    throws AppException, InformationalException {

    int count = 0;
    // All the criteria must be bound to struct attributes before the search.
    // This means all the selected org objects and priority lists must be
    // flattened and each criteria in the flattened list must be searched
    // separately in a loop.
    final Iterator<PriorityCodeDetails> priorityIter = SearchTaskUtilities
      .getTaskPriorityCodes(bdmTaskQueryCriteria.criteria.selectedPriorities)
      .iterator();
    final List<OrganisationObjectDetails> orgObjectList = SearchTaskUtilities
      .getSelectedOrgObjects(bdmTaskQueryCriteria.criteria);

    // Priority is an optional search criteria, so it doesn't have to exist in
    // the criteria, however we still need to search for the org objects which
    // will exist.
    do {
      if (priorityIter.hasNext()) {
        bdmTaskQueryCriteria.criteria.priority = priorityIter.next().code;
      }
      final Iterator<OrganisationObjectDetails> orgObjectIter =
        orgObjectList.iterator();

      while (orgObjectIter.hasNext()) {
        SearchTaskUtilities.setAssigneeCriteria(bdmTaskQueryCriteria.criteria,
          orgObjectIter.next());
        final String sql =
          bdmSearchTaskSQL.getCountSQLStatement(bdmTaskQueryCriteria);

        final BDMTaskQueryCriteriaSQLParams params =
          new BDMTaskQueryCriteriaSQLParams();
        params.assign(bdmTaskQueryCriteria.criteria);
        params.caseUrgentFlagTypeCode =
          bdmTaskQueryCriteria.caseUrgentFlagTypeCode;
        params.escalationLevel = bdmTaskQueryCriteria.escalationLevel;

        count = count + ((Count) DynamicDataAccess.executeNs(Count.class,
          params, false, sql)).numberOfRecords;
      }
    } while (priorityIter.hasNext());
    final Count returnCount = new Count();

    returnCount.numberOfRecords = count;
    return returnCount;
  }

}
