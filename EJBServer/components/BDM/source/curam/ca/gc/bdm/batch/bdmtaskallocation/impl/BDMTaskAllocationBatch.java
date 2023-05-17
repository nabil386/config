package curam.ca.gc.bdm.batch.bdmtaskallocation.impl;

import curam.ca.gc.bdm.batch.bdmtaskallocation.struct.BDMTaskAllocationBatchKey;
import curam.ca.gc.bdm.batch.bdmtaskallocation.struct.BDMTaskAllocationUsersList;
import curam.ca.gc.bdm.entity.fact.BDMUserFactory;
import curam.ca.gc.bdm.entity.intf.BDMUser;
import curam.ca.gc.bdm.entity.struct.BDMTaskSearchResultList;
import curam.ca.gc.bdm.entity.struct.BDMUserDtls;
import curam.ca.gc.bdm.entity.struct.BDMUserDtlsList;
import curam.ca.gc.bdm.entity.struct.BDMUserKey;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMTASKALLOCATIONBATCH;
import curam.ca.gc.bdm.sl.fact.BDMMaintainTaskAllocationFactory;
import curam.ca.gc.bdm.sl.intf.BDMMaintainTaskAllocation;
import curam.ca.gc.bdm.sl.struct.BDMBundledTaskDetails;
import curam.ca.gc.bdm.sl.struct.BDMBundledTaskList;
import curam.ca.gc.bdm.sl.struct.BDMSearchBundledTaskKey;
import curam.ca.gc.bdm.sl.struct.BDMSkillLevelTaskDetails;
import curam.ca.gc.bdm.sl.struct.BDMSkillLevelTaskList;
import curam.ca.gc.bdm.sl.struct.BDMStage1TaskDetails;
import curam.ca.gc.bdm.sl.struct.BDMStage1TaskList;
import curam.ca.gc.bdm.sl.struct.BDMWorkQueueTaskKey;
import curam.codetable.ASSIGNEETYPE;
import curam.codetable.RECORDSTATUS;
import curam.codetable.SKILLLEVEL;
import curam.codetable.TARGETITEMTYPE;
import curam.codetable.TASKCHANGETYPE;
import curam.codetable.TASKSTATUS;
import curam.codetable.WORKINGHOURSTYPE;
import curam.core.fact.WorkingPatternFactory;
import curam.core.impl.CuramBatch;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.intf.WorkingPattern;
import curam.core.sl.entity.fact.TaskAssignmentFactory;
import curam.core.sl.entity.fact.UserSkillFactory;
import curam.core.sl.entity.fact.UserSkillLanguagesFactory;
import curam.core.sl.entity.intf.TaskAssignment;
import curam.core.sl.entity.intf.UserSkill;
import curam.core.sl.entity.intf.UserSkillLanguages;
import curam.core.sl.entity.struct.FromUserNameAndBlockTaskAllocationKey;
import curam.core.sl.entity.struct.TaskAssignmentDtls;
import curam.core.sl.entity.struct.TaskRedirectionHistoryFromUserDetails;
import curam.core.sl.entity.struct.TaskRedirectionHistoryFromUserDetailsList;
import curam.core.sl.entity.struct.UserSkillDtls;
import curam.core.sl.entity.struct.UserSkillLanguagesDtlsList;
import curam.core.sl.impl.DefaultWorkResolverAdapter;
import curam.core.sl.impl.DefaultWorkResolverHelper;
import curam.core.sl.struct.AllocationTargetDetails;
import curam.core.sl.struct.AllocationTargetList;
import curam.core.sl.struct.ReadUserSkillIKeyByUserStatusAndType;
import curam.core.sl.struct.SQLStatement;
import curam.core.sl.struct.UserSkillLanguageReadMultiKey;
import curam.core.struct.UsersKey;
import curam.core.struct.WorkingPatternDtls;
import curam.core.struct.WorkingPatternOwnerDateTypeStatusKey;
import curam.message.GENERALBATCH;
import curam.util.dataaccess.CuramValueList;
import curam.util.dataaccess.DynamicDataAccess;
import curam.util.exception.AppException;
import curam.util.exception.AppRuntimeException;
import curam.util.exception.InformationalException;
import curam.util.exception.RecordNotFoundException;
import curam.util.resources.Configuration;
import curam.util.resources.Locale;
import curam.util.resources.ProgramLocale;
import curam.util.resources.Trace;
import curam.util.resources.UserPreference;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.NotFoundIndicator;
import curam.util.workflow.fact.TaskAdminFactory;
import curam.util.workflow.fact.TaskFactory;
import curam.util.workflow.fact.TaskHistoryAdminFactory;
import curam.util.workflow.intf.Task;
import curam.util.workflow.intf.TaskAdmin;
import curam.util.workflow.intf.TaskHistoryAdmin;
import curam.util.workflow.struct.Count;
import curam.util.workflow.struct.TaskDetails;
import curam.util.workflow.struct.TaskDtls;
import curam.util.workflow.struct.TaskKey;
import curam.util.workflow.struct.TaskKeyList;
import curam.util.workflow.struct.TaskUsernameKey;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.stream.Collectors;
import org.apache.logging.log4j.Level;

public abstract class BDMTaskAllocationBatch extends
  curam.ca.gc.bdm.batch.bdmtaskallocation.base.BDMTaskAllocationBatch {

  DateTime startDateTime = DateTime.getCurrentDateTime();

  CuramBatch curamBatchObj = new CuramBatch();

  protected static PrintStream stOutstream;

  // unilingual users list
  BDMUserDtlsList unilingualUserList = new BDMUserDtlsList();

  // bilingual users list
  BDMUserDtlsList bilingualUserList = new BDMUserDtlsList();

  final BDMMaintainTaskAllocation bdmMaintainTaskAllocationObj =
    BDMMaintainTaskAllocationFactory.newInstance();

  final Set<Long> taskAssignedToUserOrQueue = new HashSet<>();

  final Set<Long> agentList = new HashSet<>();

  final Set<Long> stage1ProcessedTaskList = new HashSet<>();

  final Set<Long> ptmProcessedTaskList = new HashSet<>();

  final Set<Long> stage2ProcessedTaskList = new HashSet<>();

  final Set<Long> errorTaskList = new HashSet<>();

  final String kBDMWorkQueueIDList =
    Configuration.getProperty(EnvVars.BDM_ENV_ALLOCATION_WORKQUEUE_LIST);
  // "80013, 80014, 80015, 80016, 80017";

  final String kExcludeSkillTypeList = Configuration
    .getProperty(EnvVars.BDM_ENV_SKILLTYPE_EXCLUDE_IN_TASK_BUNDLING_LIST);

  final int maxTaskAssingedToAgent = Configuration
    .getIntProperty(EnvVars.BDM_ENV_MAX_TASK_ALLOWED_TO_ASSIGN_TO_AGENT);

  long kVSGErrorWorkQueueID = 80020;

  final StringBuffer excludeSkillTypeStr = new StringBuffer();

  @Override
  public void process(final BDMTaskAllocationBatchKey key)
    throws AppException, InformationalException {

    final BDMTaskAllocationUsersList agentLists = preProcess(key);

    unilingualUserList.dtls.addAll(agentLists.unilingualAgents);

    bilingualUserList.dtls.addAll(agentLists.bilingualAgents);

    if (unilingualUserList.dtls.size() > 0
      || bilingualUserList.dtls.size() > 0) {

      // process based on batch mode
      if (key.batchMode.isEmpty()) {
        // process all modes
        allocateTaskStage1();
        allocatePTMTask();
        allocateStage2Task();
      } else {
        // process based on mode argument
        final StringTokenizer stringTokenizer =
          new StringTokenizer(key.batchMode, ",");
        while (stringTokenizer.hasMoreTokens()) {
          final String mode = stringTokenizer.nextToken();
          if (mode.equals("stage1")) {
            // process stage1
            allocateTaskStage1();
          } else if (mode.equals("ptm")) {
            // process ptm
            allocatePTMTask();
          } else if (mode.equals("stage2")) {
            // process stage2
            allocateStage2Task();
          }
        }
      } // End else

    } // End if

    final BDMTaskSearchResultList openedTaskList =
      new BDMTaskSearchResultList();
    // call post process
    postProcess(key, openedTaskList);
  }

  @Override
  public BDMTaskAllocationUsersList
    preProcess(final BDMTaskAllocationBatchKey key)
      throws AppException, InformationalException {

    curamBatchObj.outputFileID = BDMTASKALLOCATIONBATCH.INF_BATCH_FILE_NAME
      .getMessageText(ProgramLocale.getDefaultServerLocale());
    curamBatchObj.datFileExt = ".dat";
    curamBatchObj.setFileName();

    try {
      stOutstream =
        new PrintStream(new FileOutputStream(curamBatchObj.outputFilename));
    } catch (final FileNotFoundException var14) {
      throw new AppRuntimeException(var14);
    }

    // Process ON_HOLD task
    // Get a list of tasks which are BRING - FORWARD - ON_HOLD status
    // Update their status to OPEN and update task history
    openAllOnHoldTasks();

    final BDMTaskAllocationUsersList usersList =
      new BDMTaskAllocationUsersList();

    // Get list of unilingual agents with account enabled
    BDMUserDtlsList uniLingualAgentsList = findUsersByNumberOfLanguages(1);
    // Filter unilingual agents for Task Allocation
    uniLingualAgentsList = filterUsersForTaskAllocation(uniLingualAgentsList);

    usersList.unilingualAgents.addAll(uniLingualAgentsList.dtls);

    // Get list of bilingual agents with account enabled
    BDMUserDtlsList biLingualAgentsList = findUsersByNumberOfLanguages(2);
    // Filter bilingual agents for Task Allocation
    biLingualAgentsList = filterUsersForTaskAllocation(biLingualAgentsList);

    usersList.bilingualAgents.addAll(biLingualAgentsList.dtls);

    // Get comma separated list of exclude skill types
    final String[] excludeSkillTypeList = kExcludeSkillTypeList.split(",");
    for (int i = 0; i < excludeSkillTypeList.length; i++) {
      excludeSkillTypeStr.append("'").append(excludeSkillTypeList[i])
        .append("'");
      if (i < excludeSkillTypeList.length - 1)
        excludeSkillTypeStr.append(",");
    }

    return usersList;
  }

  /**
   * Return tasks in On-hold status
   */
  private void openAllOnHoldTasks()
    throws AppException, InformationalException {

    final TaskKeyList bdmOnHoldTaskList = listOnholdTasks();

    for (final TaskKey onHoldTaskID : bdmOnHoldTaskList.dtls) {
      openTask(onHoldTaskID);
    }

    if (bdmOnHoldTaskList.dtls.size() > 0) {
      // commit transaction
      commit();
    }
  }

  /**
   * Return tasks in On-hold status
   */
  private TaskKeyList listOnholdTasks()
    throws AppException, InformationalException {

    final TaskKeyList bdmOnHoldTaskList = new TaskKeyList();
    final TaskKey taskKey = new TaskKey();

    CuramValueList<TaskKey> curamValueList =
      new CuramValueList<TaskKey>(TaskKey.class);

    final SQLStatement sqlStatement = new SQLStatement();

    sqlStatement.sqlStatement = "SELECT taskid ";
    sqlStatement.sqlStatement += "INTO :taskID ";
    sqlStatement.sqlStatement += "FROM task ";
    sqlStatement.sqlStatement +=
      "WHERE status = 'BDMWS00001' and restarttime <= SYSDATE ";
    sqlStatement.sqlStatement += "ORDER BY restarttime;";

    curamValueList = DynamicDataAccess.executeNsMulti(TaskKey.class, taskKey,
      false, true, sqlStatement.sqlStatement);

    Trace.kTopLevelLogger.log(Level.INFO,
      "Number of tasks to Open = " + curamValueList.size());

    /*
     * for (int i = 0; i < curamValueList.size(); i++) {
     * taskKey = new TaskKey();
     * taskKey = (TaskKey) curamValueList.get(i);
     * bdmOnHoldTaskList.dtls.add(taskKey);
     * }
     */
    bdmOnHoldTaskList.dtls.addAll(curamValueList);

    return bdmOnHoldTaskList;

  }

  /**
   * Task-28407 Open task which is in On-Hold status.
   *
   * @param taskID Task ID of on-hold task
   */
  private void openTask(final TaskKey onHoldTaskID)
    throws AppException, InformationalException {

    final Task taskObj = TaskFactory.newInstance();
    final TaskDtls taskDtls = taskObj.read(onHoldTaskID);

    // open task
    final TaskAdmin taskAdminObj = TaskAdminFactory.newInstance();
    taskAdminObj.modifyStatus(onHoldTaskID.taskID, TASKSTATUS.NOTSTARTED,
      taskDtls.versionNo);

    // update task history
    final TaskHistoryAdmin taskHistoryAdminObj =
      TaskHistoryAdminFactory.newInstance();
    taskHistoryAdminObj.create(onHoldTaskID.taskID,
      DateTime.getCurrentDateTime(), TASKCHANGETYPE.STATUSCHANGED, "",
      TASKSTATUS.NOTSTARTED, TASKSTATUS.ONHOLD,
      TransactionInfo.getProgramUser());
  }

  /**
   * Helper method to search users by language
   *
   * @param language
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  protected BDMUserDtlsList findUsersByNumberOfLanguages(final int langCount)
    throws AppException, InformationalException {

    // Retrieve list of users by language count
    final BDMUserDtlsList bdmAgentList = new BDMUserDtlsList();

    CuramValueList<BDMUserDtls> curamValueList =
      new CuramValueList<BDMUserDtls>(BDMUserDtls.class);

    final SQLStatement sqlStatement = new SQLStatement();

    sqlStatement.sqlStatement =
      "SELECT bdmuser.username, bdmuser.userLangList ";
    sqlStatement.sqlStatement += "INTO  :username, :userLangList ";
    sqlStatement.sqlStatement += "FROM BDMUSER bdmuser JOIN USERS agents ";
    sqlStatement.sqlStatement += "ON bdmuser.username = agents.username ";
    sqlStatement.sqlStatement += "AND agents.accountenabled = 1  ";
    sqlStatement.sqlStatement += "AND agents.statuscode = 'RST1' ";
    sqlStatement.sqlStatement +=
      "AND (CURRENT_DATE BETWEEN agents.creationDate AND COALESCE(agents.endDate,CURRENT_DATE))";
    sqlStatement.sqlStatement += "AND REGEXP_COUNT( bdmuser.userLangList, '"
      + CuramConst.gkTabDelimiter + "' ) = ";
    // there will be 0 commas for 1 language, 1 comma for 2 languages
    // ex. LN1, LN4 for 2 languages
    sqlStatement.sqlStatement += langCount - 1;

    curamValueList = DynamicDataAccess.executeNsMulti(BDMUserDtls.class, null,
      false, true, sqlStatement.sqlStatement);

    Trace.kTopLevelLogger.log(Level.INFO, "Number of agents speaking "
      + langCount + " languages = " + curamValueList.size());

    bdmAgentList.dtls.addAll(curamValueList);

    return bdmAgentList;
  }

  /**
   * Helper method that filters users in list and removes them based on active
   * task allocation blocking, users working pattern and
   *
   * @param userList
   * @return BDMUserDtlsList
   * @throws AppException
   * @throws InformationalException
   */
  protected BDMUserDtlsList
    filterUsersForTaskAllocation(final BDMUserDtlsList userList)
      throws AppException, InformationalException {

    BDMUserDtlsList filterUsersList = new BDMUserDtlsList();
    // Search users with no active blocking
    Trace.kTopLevelLogger
      .info("Remove users which have task allocation blocking");
    filterUsersList = findUserWithNoActiveAllocationBlocking(userList);
    Trace.kTopLevelLogger
      .info("After task allocation blocking, userList count = "
        + filterUsersList.dtls.size());

    // Search users with active working pattern
    Trace.kTopLevelLogger.info("Filter users wth not Active Working Pattern");
    filterUsersList = findUserWithActiveWorkPattern(filterUsersList);
    Trace.kTopLevelLogger
      .info("After working pattern check, userList count = "
        + filterUsersList.dtls.size());

    Trace.kTopLevelLogger
      .info("Find users with less than " + maxTaskAssingedToAgent + " tasks");
    filterUsersList = findUsersWithLessThanMaxTask(filterUsersList);
    Trace.kTopLevelLogger.info("After max task count check, userList count = "
      + filterUsersList.dtls.size());

    Trace.kTopLevelLogger
      .info("Sort users with number of open tasks assigned to them");
    filterUsersList = orderUsersListByOpenTaskCountAscOrderk(filterUsersList);

    return filterUsersList;
  }

  /**
   * Helper method that will iterate through users list to check for task
   * allocation blocking
   *
   * @param userList
   * @return BDMUserDtlsList
   * @throws AppException
   * @throws InformationalException
   */
  protected BDMUserDtlsList
    findUserWithNoActiveAllocationBlocking(final BDMUserDtlsList userList)
      throws AppException, InformationalException {

    final BDMUserDtlsList returnUserList = new BDMUserDtlsList();
    BDMUserDtls userListDetails = null;
    final curam.core.sl.entity.intf.TaskRedirection taskRedirectionObj =
      curam.core.sl.entity.fact.TaskRedirectionFactory.newInstance();

    for (int i = 0; i < userList.dtls.size(); i++) {
      userListDetails = new BDMUserDtls();
      userListDetails = userList.dtls.get(i);

      final FromUserNameAndBlockTaskAllocationKey userNameAndDateTimeKey =
        new FromUserNameAndBlockTaskAllocationKey();
      userNameAndDateTimeKey.fromUserName = userList.dtls.get(i).userName;
      userNameAndDateTimeKey.blockTaskAllocation = true;

      final TaskRedirectionHistoryFromUserDetailsList redirectionHistoryFromUserDetailsList =
        taskRedirectionObj
          .searchByRedirectFromUserName(userNameAndDateTimeKey);
      final int listSize = redirectionHistoryFromUserDetailsList.dtls.size();
      boolean isProgramTimeInUserTaskBlockHours = false;
      if (listSize == 0) {
        // No task allocation block defined for user
        returnUserList.dtls.add(userListDetails);
      } else {
        for (int cnt = 0; cnt < listSize; cnt++) {
          final TaskRedirectionHistoryFromUserDetails redirectionHistoryFromUserDetails =
            redirectionHistoryFromUserDetailsList.dtls.item(cnt);
          isProgramTimeInUserTaskBlockHours =
            isProgramTimeInUserTaskAllocationBlockingTime(userListDetails,
              redirectionHistoryFromUserDetails);
          if (isProgramTimeInUserTaskBlockHours) {
            // batch run time is in user task allocation block time. So exit
            // the
            // loop
            break;
          }
        } // end for
        if (!isProgramTimeInUserTaskBlockHours) {
          // User has defined active task allocation blocking. But batch run
          // time is not in none of them. So its okay to add user for task
          // processing
          returnUserList.dtls.add(userListDetails);
        }
      }

    } // End for

    return returnUserList;
  }

  /**
   * Helper method that will iterate through users list to check that current
   * date time is within their active work pattern
   *
   * @param userList
   * @return BDMUserDtlsList
   * @throws AppException
   * @throws InformationalException
   */
  protected BDMUserDtlsList
    findUserWithActiveWorkPattern(final BDMUserDtlsList userList)
      throws AppException, InformationalException {

    final BDMUserDtlsList returnUserList = new BDMUserDtlsList();
    BDMUserDtls userListDetails = null;
    final WorkingPattern workingPatternObj =
      WorkingPatternFactory.newInstance();
    WorkingPatternDtls workingPatternDtls = null;
    WorkingPatternOwnerDateTypeStatusKey workingPatternOwnerDateTypeStatusKey =
      null;

    for (int i = 0; i < userList.dtls.size(); i++) {

      userListDetails = new BDMUserDtls();
      userListDetails = userList.dtls.get(i);

      workingPatternDtls = new WorkingPatternDtls();

      workingPatternOwnerDateTypeStatusKey =
        new WorkingPatternOwnerDateTypeStatusKey();
      workingPatternOwnerDateTypeStatusKey.userName =
        userList.dtls.get(i).userName;
      workingPatternOwnerDateTypeStatusKey.recordStatusCode =
        RECORDSTATUS.NORMAL;
      workingPatternOwnerDateTypeStatusKey.workingHoursType =
        WORKINGHOURSTYPE.WORKINGHOURS;
      workingPatternOwnerDateTypeStatusKey.effectiveDate =
        Date.getCurrentDate();

      // search users where current date time is within their active
      // working pattern
      try {
        workingPatternDtls =
          workingPatternObj.readActivePatternByOwnerDateAndType(
            workingPatternOwnerDateTypeStatusKey);

        final boolean isProgramTimeInUserWorkingHours =
          isProgramTimeInUserWorkingHours(userListDetails,
            workingPatternDtls);

        if (isProgramTimeInUserWorkingHours) {
          returnUserList.dtls.add(userListDetails);
        }

      } catch (final RecordNotFoundException rnfe) {
        // Ignore
      }
    } // End for
    return returnUserList;
  }

  /**
   * Returns true if the program time is during the user's working hours, taking
   * time zone into consideration
   *
   * @param userListDetails
   * @param workingPatternDtls
   * @return boolean
   */
  protected boolean isProgramTimeInUserWorkingHours(
    final BDMUserDtls userListDetails,
    final WorkingPatternDtls workingPatternDtls) {

    final String timeZone = UserPreference
      .getPreference(UserPreference.kTimeZone, userListDetails.userName);

    final java.util.TimeZone tzOfUser =
      java.util.TimeZone.getTimeZone(timeZone);

    // calendar object representing batch runtime in user's time zone.
    // final Calendar programTimeInUserTimeZone =
    // Calendar.getInstance(tzOfUser);

    final LocalDateTime localDateTime = LocalDateTime.now();

    final ZoneId userZoneID = ZoneId.of(timeZone);

    final LocalDateTime programTimeInUserTimeZone =
      LocalDateTime.now(userZoneID);


    // get user working start date time on current date
    final Calendar startTimeCal =
      workingPatternDtls.defaultStartTime.getCalendar();

    // get user working end date time on current date
    final Calendar endTimeCal =
      workingPatternDtls.defaultEndTime.getCalendar();

    final SimpleDateFormat inSDF =
      new SimpleDateFormat(BDMConstants.TWENTY_FOUR_HOUR_TIME_FORMAT);


    final TimeZone tz = startTimeCal.getTimeZone();

    // Getting zone id
    final ZoneId zoneId = tz.toZoneId();

    // conversion
    final LocalDateTime startTimeCallocTm =
      LocalDateTime.ofInstant(startTimeCal.toInstant(), zoneId);


    final LocalDateTime endTimeCallocTm =
      LocalDateTime.ofInstant(endTimeCal.toInstant(), zoneId);


    final boolean isProgramTimeInUserWorkingHours = programTimeInUserTimeZone
      .toLocalTime().isAfter(startTimeCallocTm.toLocalTime())
      && programTimeInUserTimeZone.toLocalTime()
        .isBefore(endTimeCallocTm.toLocalTime());

    /*
     * // is batch execution time between user start and end time
     * final boolean isProgramTimeInUserWorkingHours =
     * programTimeInUserTimeZone.after(startTimeCal)
     * && programTimeInUserTimeZone.before(endTimeCal);
     *
     * final SimpleDateFormat inSDF =
     * new SimpleDateFormat(BDMConstants.TWENTY_FOUR_HOUR_TIME_FORMAT);
     *
     * System.out.println("programTimeInUserTimeZone="
     * + inSDF.format(programTimeInUserTimeZone.getTime()));
     *
     * System.out.println(
     * "programTimeInUserTimeZone=" + programTimeInUserTimeZone.getTime());
     * System.out
     * .println("startTimeCal=" + inSDF.format(startTimeCal.getTime()));
     * System.out.println("endTimeCal=" + inSDF.format(endTimeCal.getTime()));
     */

    return isProgramTimeInUserWorkingHours;
  }

  /**
   * Returns true if the program time is during the user's task allocation
   * blocking hours, taking
   * time zone into consideration
   *
   * @param userListDetails
   * @param redirectionHistoryFromUserDetails
   * @return boolean
   */
  protected boolean isProgramTimeInUserTaskAllocationBlockingTime(
    final BDMUserDtls userListDetails,
    final TaskRedirectionHistoryFromUserDetails redirectionHistoryFromUserDetails) {

    boolean isProgramTimeInUserTaskBlockHours = false;
    final String timeZone = UserPreference
      .getPreference(UserPreference.kTimeZone, userListDetails.userName);

    final ZoneId userZoneID = ZoneId.of(timeZone);

    final LocalDateTime programTimeInUserTimeZone =
      LocalDateTime.now(userZoneID);


    // get user working start date time on current date
    final Calendar startTimeCal =
      redirectionHistoryFromUserDetails.startDateTime.getCalendar();

    final TimeZone tz = startTimeCal.getTimeZone();

    // Getting zone id
    final ZoneId zoneId = tz.toZoneId();

    // conversion
    final LocalDateTime startTimeCallocTm =
      LocalDateTime.ofInstant(startTimeCal.toInstant(), zoneId);


    // get user working end date time on current date
    if (!redirectionHistoryFromUserDetails.endDateTime.isZero()) {
      final Calendar endTimeCal =
        redirectionHistoryFromUserDetails.endDateTime.getCalendar();

      final LocalDateTime endTimeCallocTm =
        LocalDateTime.ofInstant(endTimeCal.toInstant(), zoneId);


      isProgramTimeInUserTaskBlockHours =
        programTimeInUserTimeZone.isAfter(startTimeCallocTm)
          && programTimeInUserTimeZone.isBefore(endTimeCallocTm);
    } else {
      isProgramTimeInUserTaskBlockHours =
        programTimeInUserTimeZone.isAfter(startTimeCallocTm);
    }

    return isProgramTimeInUserTaskBlockHours;
  }

  /**
   * Post Process creates a report of processed records
   *
   * @param modeDetails
   * @param taskList
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void postProcess(final BDMTaskAllocationBatchKey modeDetails,
    final BDMTaskSearchResultList taskList)
    throws AppException, InformationalException {

    final String elapsedTime = getTimeAsString(
      DateTime.getCurrentDateTime().asLong() - startDateTime.asLong());

    final AppException runtimeText =
      new AppException(GENERALBATCH.INF_JOB_RUNTIME);
    runtimeText.arg(Locale.getFormattedTime(startDateTime));
    runtimeText.arg(elapsedTime);

    final StringBuffer outputMessage = new StringBuffer();

    final AppException reportMsg = new AppException(
      BDMTASKALLOCATIONBATCH.INF_TASK_ALLOCATION_BATCH_HEADER);
    stOutstream.print(
      reportMsg.getMessage(ProgramLocale.getDefaultServerLocale()) + "\n\n");

    stOutstream
      .print(runtimeText.getMessage(ProgramLocale.getDefaultServerLocale())
        + "\n\n");

    AppException msg =
      new AppException(BDMTASKALLOCATIONBATCH.INF_UNILINGUAL_AGENT_COUNT);
    msg.arg(unilingualUserList.dtls.size());
    outputMessage
      .append(msg.getMessage(ProgramLocale.getDefaultServerLocale()) + "\n");

    msg = new AppException(BDMTASKALLOCATIONBATCH.INF_BILINGUAL_AGENT_COUNT);
    msg.arg(bilingualUserList.dtls.size());
    outputMessage
      .append(msg.getMessage(ProgramLocale.getDefaultServerLocale()) + "\n");

    msg =
      new AppException(BDMTASKALLOCATIONBATCH.INF_TOTAL_RECORDS_PROCESSED);
    msg.arg(taskAssignedToUserOrQueue.size());
    outputMessage
      .append(msg.getMessage(ProgramLocale.getDefaultServerLocale()) + "\n");

    msg = new AppException(BDMTASKALLOCATIONBATCH.INF_TASK_STAGE1);
    msg.arg(stage1ProcessedTaskList.size());
    outputMessage
      .append(msg.getMessage(ProgramLocale.getDefaultServerLocale()) + "\n");

    msg = new AppException(BDMTASKALLOCATIONBATCH.INF_TASK_PTM);
    msg.arg(+ptmProcessedTaskList.size());
    outputMessage
      .append(msg.getMessage(ProgramLocale.getDefaultServerLocale()) + "\n");

    msg = new AppException(BDMTASKALLOCATIONBATCH.INF_TASK_STAGE2);
    msg.arg(stage2ProcessedTaskList.size());
    outputMessage
      .append(msg.getMessage(ProgramLocale.getDefaultServerLocale()) + "\n");

    msg = new AppException(BDMTASKALLOCATIONBATCH.INF_TASK_ERROR_QUEUE);
    msg.arg(errorTaskList.size());
    outputMessage.append(
      msg.getMessage(ProgramLocale.getDefaultServerLocale()) + "\n\n");

    msg = new AppException(BDMTASKALLOCATIONBATCH.INF_TASK_LIST);
    outputMessage
      .append(msg.getMessage(ProgramLocale.getDefaultServerLocale()) + "\n");

    String listString = stage1ProcessedTaskList.stream().map(Object::toString)
      .collect(Collectors.joining(", "));

    outputMessage.append("Stage 1 task list = " + listString + "\n");

    listString = ptmProcessedTaskList.stream().map(Object::toString)
      .collect(Collectors.joining(", "));

    outputMessage.append("PTM task list = " + listString + "\n");

    listString = stage2ProcessedTaskList.stream().map(Object::toString)
      .collect(Collectors.joining(", "));

    outputMessage.append("Stage 2 task list = " + listString + "\n");

    stOutstream.print(outputMessage.toString());
    stOutstream.close();

    // curamBatchObj.emailMessage = outputMessage.toString();
    // curamBatchObj.setEmailSubject(
    // BDMTASKALLOCATIONBATCH.INF_TASK_ALLOCATION_BATCH_SUBJECT);
    //
    // curamBatchObj.sendEmail();

  }

  /**
   * Process tasks for stage 1 flow
   *
   * @param key
   * @throws AppException
   * @throws InformationalException
   */
  protected void allocateTaskStage1()
    throws AppException, InformationalException {

    BDMStage1TaskList bdmStage1TaskList = new BDMStage1TaskList();
    BDMStage1TaskDetails bdmStage1TaskDetails = new BDMStage1TaskDetails();

    final BDMWorkQueueTaskKey bdmWorkQueueTaskKey = new BDMWorkQueueTaskKey();

    bdmWorkQueueTaskKey.commaSeperatedWorkQueueIDs = kBDMWorkQueueIDList;

    bdmStage1TaskList =
      bdmMaintainTaskAllocationObj.getStage1UrgentTasks(bdmWorkQueueTaskKey);

    // if skillType is not present then move to error handling queue
    for (int i = 0; i < bdmStage1TaskList.dtls.size(); i++) {

      bdmStage1TaskDetails = new BDMStage1TaskDetails();
      bdmStage1TaskDetails = bdmStage1TaskList.dtls.get(i);

      if (bdmStage1TaskDetails.bdmTaskClassificationID == 0) {
        // move task to error handling workqueue
        assignTaskToErrorHandlingWorkQueue(bdmStage1TaskDetails.taskID);
      } else if (bdmStage1TaskDetails.deadlineDateTime.isZero()) {
        // move task to error handling workqueue
        assignTaskToErrorHandlingWorkQueue(bdmStage1TaskDetails.taskID);
      } // End if

    } // End for

    loopStage1TasksBySkillLevel(bdmStage1TaskList, SKILLLEVEL.PRIMARY);
    loopStage1TasksBySkillLevel(bdmStage1TaskList, SKILLLEVEL.SECONDARY);
    loopStage1TasksBySkillLevel(bdmStage1TaskList, SKILLLEVEL.TERTIARY);

  }

  protected void loopStage1TasksBySkillLevel(
    final BDMStage1TaskList bdmStage1TaskList, final String skillLevel)
    throws AppException, InformationalException {

    // Agent list for stage 1 flow
    BDMUserDtlsList unilingualUserListStage1 = new BDMUserDtlsList();
    BDMUserDtlsList bilingualUserListStage1 = new BDMUserDtlsList();

    BDMStage1TaskDetails bdmStage1TaskDetails;

    for (int i = 0; i < bdmStage1TaskList.dtls.size(); i++) {

      bdmStage1TaskDetails = new BDMStage1TaskDetails();
      bdmStage1TaskDetails = bdmStage1TaskList.dtls.get(i);

      // Check if task is already assigned to user
      if (taskAssignedToUserOrQueue.contains(bdmStage1TaskDetails.taskID)) {
        continue;
      }

      unilingualUserListStage1 = filterUsersBySkillTypeLevelOtherCriteria1(
        skillLevel, bdmStage1TaskDetails.skillType, unilingualUserList,
        bdmStage1TaskDetails.language);

      if (unilingualUserListStage1.dtls.size() > 0) {

        // Allocate task to user
        assignTaskToUser(bdmStage1TaskDetails.taskID,
          unilingualUserListStage1.dtls.get(0).userName, skillLevel,
          bdmStage1TaskDetails.skillType, bdmStage1TaskDetails.language,
          stage1ProcessedTaskList);

        // call bundling logic
        bundleTasksAndAssignToUser(bdmStage1TaskDetails.taskID,
          bdmStage1TaskDetails.caseID, bdmStage1TaskDetails.skillType,
          skillLevel, bdmStage1TaskDetails.language,
          unilingualUserListStage1.dtls.get(0).userName,
          stage1ProcessedTaskList);

        // commit transaction
        commit();

      } else {

        // Lets try to find agent in bilingual list
        bilingualUserListStage1 = filterUsersBySkillTypeLevelOtherCriteria1(
          skillLevel, bdmStage1TaskDetails.skillType, bilingualUserList,
          bdmStage1TaskDetails.language);

        if (bilingualUserListStage1.dtls.size() > 0) {
          // Allocate task to user
          assignTaskToUser(bdmStage1TaskDetails.taskID,
            bilingualUserListStage1.dtls.get(0).userName, skillLevel,
            bdmStage1TaskDetails.skillType, bdmStage1TaskDetails.language,
            stage1ProcessedTaskList);

          // call bundling logic
          bundleTasksAndAssignToUser(bdmStage1TaskDetails.taskID,
            bdmStage1TaskDetails.caseID, bdmStage1TaskDetails.skillType,
            skillLevel, bdmStage1TaskDetails.language,
            bilingualUserListStage1.dtls.get(0).userName,
            stage1ProcessedTaskList);

          // commit transaction
          commit();

        } else {
          Trace.kTopLevelLogger.info(skillLevel
            + ": No users found for taskID=" + bdmStage1TaskDetails.taskID
            + ", skillType=" + bdmStage1TaskDetails.skillType + ", language="
            + bdmStage1TaskDetails.language);
        }
      }

    } // End for

  }

  protected BDMUserDtlsList filterUsersBySkillTypeLevelOtherCriteria1(
    final String skillLevel, final String skillType,
    final BDMUserDtlsList bdmUserDtlsList, final String language)
    throws AppException, InformationalException {

    BDMUserDtlsList userList = new BDMUserDtlsList();

    Trace.kTopLevelLogger.info("skillLevel = " + skillLevel);
    userList = searchUsersBySkillTypeLevel(skillType, skillLevel,
      bdmUserDtlsList, language);

    if (userList.dtls.size() > 0) {
      // Find users with less than max tasks
      // read max tasks from property
      // curam.ca.gc.bdm.batch.bdmtaskallocationbatch.maxTaskAssignedToAgent
      Trace.kTopLevelLogger.info(
        "Find users with less than " + maxTaskAssingedToAgent + " max tasks");
      userList = findUsersWithLessThanMaxTask(userList);

      Trace.kTopLevelLogger
        .info("Sort users with number of open tasks assigned to them");
      userList = orderUsersListByOpenTaskCountAscOrderk(userList);
    }

    return userList;

  }

  /**
   * Assign task to user
   *
   * @param taskID
   * @param userName
   * @throws AppException
   * @throws InformationalException
   */
  protected void assignTaskToUser(final long taskID, final String userName,
    final String skillLevel, final String skillType, final String language,
    final Set<Long> processedTaskList)
    throws AppException, InformationalException {

    final boolean isValid = validateAssignTaskToUser(taskID);

    if (isValid) {
      final Task taskObj = TaskFactory.newInstance();
      final TaskKey taskKey = new TaskKey();
      taskKey.taskID = taskID;
      final TaskDtls taskDtls = taskObj.read(taskKey);

      final UsersKey usersKey = new UsersKey();
      usersKey.userName = userName;

      // Remove existing task assignment
      final TaskAssignment taskAssignmentObj =
        TaskAssignmentFactory.newInstance();
      final curam.core.sl.entity.struct.TaskKey slTaskKey =
        new curam.core.sl.entity.struct.TaskKey();
      slTaskKey.taskID = taskID;

      try {
        taskAssignmentObj.removeAssignmentsForTask(slTaskKey);
      } catch (final RecordNotFoundException var5) {
        // Ignore
      }
      // Create new assignment record
      final TaskAssignmentDtls taskAssignmentDtls = new TaskAssignmentDtls();
      taskAssignmentDtls.relatedName = userName;
      taskAssignmentDtls.taskID = taskID;
      taskAssignmentDtls.assigneeType = ASSIGNEETYPE.USER;
      taskAssignmentObj.insert(taskAssignmentDtls);

      // Create task history record
      final TaskHistoryAdmin taskHistoryAdminObj =
        TaskHistoryAdminFactory.newInstance();

      taskHistoryAdminObj.create(taskID, DateTime.getCurrentDateTime(),
        TASKCHANGETYPE.RESERVED, "", userName, "", userName);

      final TaskAdmin taskAdminObj = TaskAdminFactory.newInstance();

      taskAdminObj.modifyReservedBy(taskID, userName,
        DateTime.getCurrentDateTime(), taskDtls.versionNo);

      taskAssignedToUserOrQueue.add(taskID);
      processedTaskList.add(taskID);

      Trace.kTopLevelLogger
        .info(skillLevel + ": Assigned TaskID=" + taskID + ", skillType="
          + skillType + ", language=" + language + " to user - " + userName);

    } // End if

  }

  /**
   * Validate task assignment to user
   *
   * @param taskID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  protected boolean validateAssignTaskToUser(final long taskID)
    throws AppException, InformationalException {

    boolean isValid = true;

    final Task taskObj = TaskFactory.newInstance();
    final TaskKey taskKey = new TaskKey();
    taskKey.taskID = taskID;
    final TaskDtls taskDtls = taskObj.read(taskKey);

    if (TASKSTATUS.CLOSED.equals(taskDtls.status)) {
      Trace.kTopLevelLogger.info("Task is closed, taskID=" + taskID);
      isValid = false;
    }

    return isValid;

  }

  /**
   * Helper method that will iterate through the users in the list to check if
   * they have specific skill and language referenced in the task
   *
   * @param skillType
   * @param skillLevel
   * @param userList
   * @param language
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  protected BDMUserDtlsList searchUsersBySkillTypeLevel(
    final String skillType, final String skillLevel,
    final BDMUserDtlsList userList, final String language)
    throws AppException, InformationalException {

    final BDMUserDtlsList retUserList = new BDMUserDtlsList();
    BDMUserDtls userListDetails = null;

    final BDMUser bdmUserObj = BDMUserFactory.newInstance();
    final BDMUserKey bdmUserKey = new BDMUserKey();

    final UserSkill userSkillObj = UserSkillFactory.newInstance();
    UserSkillDtls userSkillDtls = null;
    ReadUserSkillIKeyByUserStatusAndType readUserSkillIKeyByUserStatusAndType =
      null;
    NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
    final UserSkillLanguageReadMultiKey userSkillLanguageReadMultiKey = null;

    final UserSkillLanguages userSkillLanguagesObj =
      UserSkillLanguagesFactory.newInstance();
    final UserSkillLanguagesDtlsList userSkillLanguagesDtlsList = null;
    for (int i = 0; i < userList.dtls.size(); i++) {

      userListDetails = new BDMUserDtls();
      userListDetails.userName = userList.dtls.get(i).userName;

      notFoundIndicator = new NotFoundIndicator();
      userSkillDtls = new UserSkillDtls();
      readUserSkillIKeyByUserStatusAndType =
        new ReadUserSkillIKeyByUserStatusAndType();
      readUserSkillIKeyByUserStatusAndType.userName =
        userList.dtls.get(i).userName;
      readUserSkillIKeyByUserStatusAndType.skillType = skillType;
      readUserSkillIKeyByUserStatusAndType.recordStatus = RECORDSTATUS.NORMAL;

      userSkillDtls = userSkillObj.readByUserSkillTypeAndStatus(
        notFoundIndicator, readUserSkillIKeyByUserStatusAndType);
      if (!notFoundIndicator.isNotFound()) {

        if (skillLevel.equals(userSkillDtls.skillLevel)) {
          bdmUserKey.userName = userListDetails.userName;
          final BDMUserDtls bdmUserDtls = bdmUserObj.read(bdmUserKey);
          final String[] languages =
            bdmUserDtls.userLangList.split(CuramConst.gkTabDelimiter);
          for (int j = 0; j < languages.length; j++) {
            if (language.equals(languages[j])) {
              // then final add to final the usersSkillTypeList
              retUserList.dtls.add(userListDetails);
            }
          }
          /*
           * userSkillLanguagesDtlsList = new UserSkillLanguagesDtlsList();
           * userSkillLanguageReadMultiKey = new
           * UserSkillLanguageReadMultiKey();
           * userSkillLanguageReadMultiKey.userSkillID =
           * userSkillDtls.userSkillID;
           * userSkillLanguagesDtlsList = userSkillLanguagesObj
           * .searchByUserSkillID(userSkillLanguageReadMultiKey);
           * for (int j = 0; j < userSkillLanguagesDtlsList.dtls.size(); j++) {
           *
           * if (language
           * .equals(userSkillLanguagesDtlsList.dtls.get(j).languageCode)) {
           * // then final add to final the usersSkillTypeList
           * retUserList.dtls.add(userListDetails);
           * }
           *
           * } // End for
           */ }
      } // End if
    } // End for
    return retUserList;
  }

  /**
   * Helper method that will iterate through the users list and find users with
   * less than max tasks defined in application property
   * curam.ca.gc.bdm.batch.bdmtaskallocationbatch.maxTaskAssignedToAgent
   *
   * @param userList
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  protected BDMUserDtlsList
    findUsersWithLessThanMaxTask(final BDMUserDtlsList userList)
      throws AppException, InformationalException {

    final BDMUserDtlsList returnUserList = new BDMUserDtlsList();

    final Task taskObj = TaskFactory.newInstance();
    Count count = null;
    TaskUsernameKey taskUsernameKey = null;
    BDMUserDtls userListDetails = null;

    for (int i = 0; i < userList.dtls.size(); i++) {

      userListDetails = new BDMUserDtls();
      userListDetails = userList.dtls.get(i);

      count = new Count();

      taskUsernameKey = new TaskUsernameKey();
      taskUsernameKey.username = userListDetails.userName;

      // Count tasks by username
      count = taskObj.countReservedByUsername(taskUsernameKey);
      if (count.count < maxTaskAssingedToAgent) {
        // add to return list
        returnUserList.dtls.add(userListDetails);
      }
    } // End for

    return returnUserList;

  }

  /**
   * Helper method that will iterate through the users list and order the user
   * list by task count
   *
   * @param userList
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  protected BDMUserDtlsList
    orderUsersListByOpenTaskCountAscOrderk(final BDMUserDtlsList userList)
      throws AppException, InformationalException {

    final Map<BDMUserDtls, Integer> unSortedMap =
      new HashMap<BDMUserDtls, Integer>();

    final BDMUserDtlsList returnUserList = new BDMUserDtlsList();

    final Task taskObj = TaskFactory.newInstance();
    Count count = null;
    TaskUsernameKey taskUsernameKey = null;
    BDMUserDtls userListDetails = null;

    for (int i = 0; i < userList.dtls.size(); i++) {

      userListDetails = new BDMUserDtls();
      userListDetails = userList.dtls.get(i);

      count = new Count();

      taskUsernameKey = new TaskUsernameKey();
      taskUsernameKey.username = userListDetails.userName;

      // Count tasks by username
      count = taskObj.countReservedByUsername(taskUsernameKey);
      unSortedMap.put(userListDetails, count.count);
    } // End for

    // LinkedHashMap preserve the ordering of elements in which they are
    // inserted
    final LinkedHashMap<BDMUserDtls, Integer> sortedMap =
      new LinkedHashMap<>();

    unSortedMap.entrySet().stream().sorted(Map.Entry.comparingByValue())
      .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

    for (final BDMUserDtls key : sortedMap.keySet()) {
      returnUserList.dtls.add(key);
    }

    return returnUserList;

  }

  /**
   * Bundle tasks and assign to the agent
   *
   * @param taskID
   * @param caseID
   * @param skillType
   * @param skillLevel
   * @param language
   * @param userName
   * @throws AppException
   * @throws InformationalException
   */
  protected void bundleTasksAndAssignToUser(final long taskID,
    final long caseID, final String skillType, final String skillLevel,
    final String language, final String userName,
    final Set<Long> processedTaskList)
    throws AppException, InformationalException {

    // call bundling logic
    BDMBundledTaskList bdmBundledTaskList = new BDMBundledTaskList();
    final BDMSearchBundledTaskKey bdmSearchBundledTaskKey =
      new BDMSearchBundledTaskKey();

    bdmSearchBundledTaskKey.taskID = taskID;
    bdmSearchBundledTaskKey.caseID = caseID;
    bdmSearchBundledTaskKey.commaSeperatedWorkQueueIDs = kBDMWorkQueueIDList;
    bdmSearchBundledTaskKey.skillType = skillType;
    bdmSearchBundledTaskKey.userName = userName;
    bdmSearchBundledTaskKey.commaSeperatedExcludeSkillTypes =
      excludeSkillTypeStr.toString();

    bdmBundledTaskList = bdmMaintainTaskAllocationObj
      .getBundledTaskList(bdmSearchBundledTaskKey);

    BDMBundledTaskDetails bdmBundledTaskDetails = null;
    for (int i = 0; i < bdmBundledTaskList.bundleTaskDtls.size(); i++) {
      bdmBundledTaskDetails = bdmBundledTaskList.bundleTaskDtls.item(i);
      // Allocate task to user
      assignTaskToUser(bdmBundledTaskDetails.taskID, userName, skillLevel,
        bdmBundledTaskDetails.skillType, language, processedTaskList);

    }
  }

  /**
   * Commit transaction
   *
   * @throws AppException
   * @throws InformationalException
   */
  protected void commit() throws AppException, InformationalException {

    try {
      TransactionInfo.getTransactionManager().commit();
      TransactionInfo.getTransactionManager().begin();

      Trace.kTopLevelLogger.info("commit() performed");
    } catch (final Exception e) {
      Trace.kTopLevelLogger.log(Level.ERROR, e.getLocalizedMessage());
    }
  }

  /**
   * PMT skill level task allocation flow
   *
   * @throws AppException
   * @throws InformationalException
   */
  protected void allocatePTMTask()
    throws AppException, InformationalException {

    BDMSkillLevelTaskList bdmSkillLevelTaskList = new BDMSkillLevelTaskList();

    BDMSkillLevelTaskDetails bdmSkillLevelTaskDetails =
      new BDMSkillLevelTaskDetails();

    final BDMWorkQueueTaskKey bdmWorkQueueTaskKey = new BDMWorkQueueTaskKey();

    bdmWorkQueueTaskKey.commaSeperatedWorkQueueIDs = kBDMWorkQueueIDList;

    bdmSkillLevelTaskList =
      bdmMaintainTaskAllocationObj.getPTMTasks(bdmWorkQueueTaskKey);

    // if skillType is not present then move to error handling queue
    for (int i = 0; i < bdmSkillLevelTaskList.dtls.size(); i++) {

      bdmSkillLevelTaskDetails = new BDMSkillLevelTaskDetails();
      bdmSkillLevelTaskDetails = bdmSkillLevelTaskList.dtls.get(i);

      if (bdmSkillLevelTaskDetails.bdmTaskClassificationID == 0) {
        // move task to error handling workqueue
        assignTaskToErrorHandlingWorkQueue(bdmSkillLevelTaskDetails.taskID);
      } else if (bdmSkillLevelTaskDetails.deadlineDateTime.isZero()) {
        // move task to error handling workqueue
        assignTaskToErrorHandlingWorkQueue(bdmSkillLevelTaskDetails.taskID);
      } // End if
    } // End for

    loopTasksBySkillLevel(bdmSkillLevelTaskList, SKILLLEVEL.PTM,
      ptmProcessedTaskList);

  }

  protected void loopTasksBySkillLevel(
    final BDMSkillLevelTaskList bdmSkillLevelTaskList,
    final String skillLevel, final Set processedTaskList)
    throws AppException, InformationalException {

    // Agent list available for PMT flow
    BDMUserDtlsList pmtUnilingualUserList = new BDMUserDtlsList();
    BDMUserDtlsList pmtBilingualUserList = new BDMUserDtlsList();

    BDMSkillLevelTaskDetails bdmSkillLevelTaskDetails;

    for (int i = 0; i < bdmSkillLevelTaskList.dtls.size(); i++) {

      bdmSkillLevelTaskDetails = new BDMSkillLevelTaskDetails();
      bdmSkillLevelTaskDetails = bdmSkillLevelTaskList.dtls.get(i);

      // Check if task is already assigned to user
      if (taskAssignedToUserOrQueue
        .contains(bdmSkillLevelTaskDetails.taskID)) {
        continue;
      }

      pmtUnilingualUserList = filterUsersBySkillTypeLevelOtherCriteria1(
        skillLevel, bdmSkillLevelTaskDetails.skillType, unilingualUserList,
        bdmSkillLevelTaskDetails.language);

      if (pmtUnilingualUserList.dtls.size() > 0) {

        // Allocate task to user
        assignTaskToUser(bdmSkillLevelTaskDetails.taskID,
          pmtUnilingualUserList.dtls.get(0).userName, skillLevel,
          bdmSkillLevelTaskDetails.skillType,
          bdmSkillLevelTaskDetails.language, processedTaskList);

        // call bundling logic
        bundleTasksAndAssignToUser(bdmSkillLevelTaskDetails.taskID,
          bdmSkillLevelTaskDetails.caseID, bdmSkillLevelTaskDetails.skillType,
          skillLevel, bdmSkillLevelTaskDetails.language,
          pmtUnilingualUserList.dtls.get(0).userName, processedTaskList);

        // commit transaction
        commit();

      } else {

        // Lets try to find agent in bilingual list
        pmtBilingualUserList = filterUsersBySkillTypeLevelOtherCriteria1(
          skillLevel, bdmSkillLevelTaskDetails.skillType, bilingualUserList,
          bdmSkillLevelTaskDetails.language);

        if (pmtBilingualUserList.dtls.size() > 0) {
          // Allocate task to user
          assignTaskToUser(bdmSkillLevelTaskDetails.taskID,
            pmtBilingualUserList.dtls.get(0).userName, skillLevel,
            bdmSkillLevelTaskDetails.skillType,
            bdmSkillLevelTaskDetails.language, processedTaskList);

          // call bundling logic
          bundleTasksAndAssignToUser(bdmSkillLevelTaskDetails.taskID,
            bdmSkillLevelTaskDetails.caseID,
            bdmSkillLevelTaskDetails.skillType, skillLevel,
            bdmSkillLevelTaskDetails.language,
            pmtBilingualUserList.dtls.get(0).userName, processedTaskList);

          // commit transaction
          commit();

        } else {
          Trace.kTopLevelLogger.info(skillLevel
            + ": No users found for taskID=" + bdmSkillLevelTaskDetails.taskID
            + ", skillType=" + bdmSkillLevelTaskDetails.skillType
            + ", language=" + bdmSkillLevelTaskDetails.language);
        }
      }

    } // End for

  }

  /**
   * Helper method to assign task to work queue
   *
   * @param taskID
   * @throws AppException
   * @throws InformationalException
   */
  protected void assignTaskToErrorHandlingWorkQueue(final long taskID)
    throws AppException, InformationalException {

    final DefaultWorkResolverAdapter workResolverAdapterObj =
      DefaultWorkResolverHelper.instantiateDefaultWorkResolverAdapter();
    final AllocationTargetList allocationTargets = new AllocationTargetList();
    final AllocationTargetDetails taskAllocationDetails =
      new AllocationTargetDetails();

    taskAllocationDetails.name = Long.toString(kVSGErrorWorkQueueID);
    taskAllocationDetails.type = TARGETITEMTYPE.WORKQUEUE;
    allocationTargets.dtls.addRef(taskAllocationDetails);

    final TaskDetails taskDetails = new TaskDetails();
    taskDetails.taskID = taskID;
    workResolverAdapterObj.resolveWork(taskDetails, allocationTargets, true);

    taskAssignedToUserOrQueue.add(taskDetails.taskID);
    errorTaskList.add(taskDetails.taskID);
  }

  /**
   * Stage 2 task allocation flow
   *
   * @throws AppException
   * @throws InformationalException
   */
  protected void allocateStage2Task()
    throws AppException, InformationalException {

    BDMSkillLevelTaskList bdmSkillLevelTaskList = new BDMSkillLevelTaskList();

    BDMSkillLevelTaskDetails bdmSkillLevelTaskDetails =
      new BDMSkillLevelTaskDetails();

    final BDMWorkQueueTaskKey bdmWorkQueueTaskKey = new BDMWorkQueueTaskKey();

    bdmWorkQueueTaskKey.commaSeperatedWorkQueueIDs = kBDMWorkQueueIDList;

    bdmSkillLevelTaskList =
      bdmMaintainTaskAllocationObj.getStage2Tasks(bdmWorkQueueTaskKey);

    // if skillType is not present then move to error handling queue
    for (int i = 0; i < bdmSkillLevelTaskList.dtls.size(); i++) {

      bdmSkillLevelTaskDetails = new BDMSkillLevelTaskDetails();
      bdmSkillLevelTaskDetails = bdmSkillLevelTaskList.dtls.get(i);

      if (bdmSkillLevelTaskDetails.bdmTaskClassificationID == 0) {
        // move task to error handling workqueue
        assignTaskToErrorHandlingWorkQueue(bdmSkillLevelTaskDetails.taskID);
      } else if (bdmSkillLevelTaskDetails.deadlineDateTime.isZero()) {
        // move task to error handling workqueue
        assignTaskToErrorHandlingWorkQueue(bdmSkillLevelTaskDetails.taskID);
      } // End if
    } // End for

    loopTasksBySkillLevel(bdmSkillLevelTaskList, SKILLLEVEL.PRIMARY,
      stage2ProcessedTaskList);
    loopTasksBySkillLevel(bdmSkillLevelTaskList, SKILLLEVEL.SECONDARY,
      stage2ProcessedTaskList);
    loopTasksBySkillLevel(bdmSkillLevelTaskList, SKILLLEVEL.TERTIARY,
      stage2ProcessedTaskList);

  }

  protected String getTimeAsString(long elapsed) {

    final AppException format =
      new AppException(GENERALBATCH.INF_TIME_FORMAT);
    final long hours = elapsed / 3600000L;
    elapsed -= hours * 3600000L;
    final long minutes = elapsed / 60000L;
    elapsed -= minutes * 60000L;
    final long seconds = elapsed / 1000L;
    final NumberFormat nf = NumberFormat.getInstance();
    nf.setMinimumIntegerDigits(2);
    format.arg(nf.format(hours));
    format.arg(nf.format(minutes));
    format.arg(nf.format(seconds));
    return format.getLocalizedMessage();
  }

}
