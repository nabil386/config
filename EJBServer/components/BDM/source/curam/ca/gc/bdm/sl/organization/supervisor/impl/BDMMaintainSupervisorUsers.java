package curam.ca.gc.bdm.sl.organization.supervisor.impl;

import curam.ca.gc.bdm.codetable.BDMESCALATIONLEVELS;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.codetable.BDMURGENTFLAGTYPE;
import curam.ca.gc.bdm.entity.fact.BDMEscalationLevelFactory;
import curam.ca.gc.bdm.entity.fact.BDMTaskClassificationFactory;
import curam.ca.gc.bdm.entity.fact.BDMUserFactory;
import curam.ca.gc.bdm.entity.intf.BDMEscalationLevel;
import curam.ca.gc.bdm.entity.intf.BDMTaskClassification;
import curam.ca.gc.bdm.entity.struct.BDMEscalationLevelDtlsStruct2;
import curam.ca.gc.bdm.entity.struct.BDMEscalationLevelKeyStruct1;
import curam.ca.gc.bdm.entity.struct.BDMTaskClassificationDtls;
import curam.ca.gc.bdm.entity.struct.BDMTaskClassificationKey;
import curam.ca.gc.bdm.entity.struct.BDMUserDtls;
import curam.ca.gc.bdm.entity.struct.BDMUserKey;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.fact.BDMCaseUrgentFlagFactory;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.struct.BDMCaseUrgentFlagDetailsList;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskSkillTypeKey;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMAssignedTaskDetails;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMEscalationLevelString;
import curam.ca.gc.bdm.sl.bdminbox.struct.CaseUrgentFlagStringDetails;
import curam.ca.gc.bdm.sl.organization.supervisor.struct.BDMListUnreservedTasksForUserDetails;
import curam.ca.gc.bdm.sl.organization.supervisor.struct.BDMSupervisorUserWithTaskCountDetails;
import curam.ca.gc.bdm.sl.organization.supervisor.struct.BDMSupervisorUserWithTaskCountDetailsList;
import curam.ca.gc.bdm.util.impl.BDMTaskUtil;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.codetable.RECORDSTATUS;
import curam.codetable.SKILLLEVEL;
import curam.codetable.SKILLTYPE;
import curam.core.facade.struct.CaseIDDetails;
import curam.core.fact.ConcernRoleCommunicationFactory;
import curam.core.impl.CuramConst;
import curam.core.sl.entity.fact.UserSkillFactory;
import curam.core.sl.entity.struct.TaskKey;
import curam.core.sl.entity.struct.UserSkillDtls;
import curam.core.sl.entity.struct.UserSkillDtlsList;
import curam.core.sl.entity.struct.UserSkillReadmultiKey;
import curam.core.sl.fact.InboxFactory;
import curam.core.sl.struct.AssignedTaskDetails;
import curam.core.sl.struct.ListTaskKey;
import curam.core.struct.ConcernRoleCommunicationDtls;
import curam.core.struct.ConcernRoleCommunicationKey;
import curam.supervisor.sl.fact.MaintainSupervisorUsersFactory;
import curam.supervisor.sl.struct.ListUnreservedTasksForUserDetails;
import curam.supervisor.sl.struct.ListUnreservedTasksForUserKey;
import curam.supervisor.sl.struct.SupervisorUserWithTaskCountDetails;
import curam.supervisor.sl.struct.SupervisorUserWithTaskCountDetailsList;
import curam.util.exception.AppException;
import curam.util.exception.AppRuntimeException;
import curam.util.exception.DatabaseException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.NotFoundIndicator;
import curam.util.workflow.fact.BizObjAssociationFactory;
import curam.util.workflow.intf.BizObjAssociation;
import curam.util.workflow.struct.BizObjAssociationDtls;
import curam.util.workflow.struct.BizObjAssociationDtlsList;
// import curam.util.workflow.struct.TaskKey;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;

public class BDMMaintainSupervisorUsers extends
  curam.ca.gc.bdm.sl.organization.supervisor.base.BDMMaintainSupervisorUsers {

  /**
   * Gets a list of a supervisor's users, their task counts, as well as skills
   * and languages
   */
  @Override
  public BDMSupervisorUserWithTaskCountDetailsList
    listSupervisorUsersWithTaskCountAndSkills()
      throws AppException, InformationalException {

    final BDMSupervisorUserWithTaskCountDetailsList supervisorUsersWithTaskCountAndSkillslDetailsList =
      new BDMSupervisorUserWithTaskCountDetailsList();

    // call OOTB method to retrieve list of users and task counts
    final SupervisorUserWithTaskCountDetailsList supervisorUsersWithTaskCountDetailsList =
      MaintainSupervisorUsersFactory.newInstance()
        .listSupervisorUsersWithTaskCount();

    final Set<String> uniqueUsernames = new HashSet<>();

    // iterate through each of the retrieved users to add their languages and
    // skills
    for (final SupervisorUserWithTaskCountDetails userDtls : supervisorUsersWithTaskCountDetailsList.dtls) {

      if (uniqueUsernames.contains(userDtls.userName)) {
        continue;
      } else {
        uniqueUsernames.add(userDtls.userName);
      }

      final BDMSupervisorUserWithTaskCountDetails bdmUserTaskDtls =
        new BDMSupervisorUserWithTaskCountDetails();
      bdmUserTaskDtls.assign(userDtls);

      // retrieve and assign the user's languages
      final BDMUserKey bdmUserKey = new BDMUserKey();
      bdmUserKey.userName = userDtls.userName;
      final BDMUserDtls bdmUserDtls = BDMUserFactory.newInstance()
        .read(new NotFoundIndicator(), bdmUserKey);

      if (bdmUserDtls != null) {
        bdmUserTaskDtls.langList = formatLanguages(bdmUserDtls.userLangList);
      }

      // retrieve and assign the users prioritized and PMT skills
      final UserSkillReadmultiKey userSkillKey = new UserSkillReadmultiKey();
      userSkillKey.userName = userDtls.userName;
      final UserSkillDtlsList userSkillDtlsList =
        UserSkillFactory.newInstance().searchByUser(userSkillKey);
      formatUserSkills(userSkillDtlsList, bdmUserTaskDtls);

      // add to return struct
      supervisorUsersWithTaskCountAndSkillslDetailsList.dtls
        .add(bdmUserTaskDtls);
    }

    return supervisorUsersWithTaskCountAndSkillslDetailsList;
  }

  /**
   * Given a list of a user's languages (from BDMUSER), retrieve their codetable
   * descriptions and format it into a comma separated list
   *
   * @param userLangList
   * @return
   * @throws DatabaseException
   * @throws AppRuntimeException
   * @throws AppException
   * @throws InformationalException
   */
  private String formatLanguages(final String userLangList)
    throws DatabaseException, AppRuntimeException, AppException,
    InformationalException {

    final String[] languages = userLangList.split(CuramConst.gkTabDelimiter);

    final StringBuffer languageList = new StringBuffer();
    for (int i = 0; i < languages.length; i++) {
      final String languageStr =
        CodeTable.getOneItem(BDMLANGUAGE.TABLENAME, languages[i]);
      languageList.append(languageStr);
      if (i != languages.length - 1) {
        languageList.append(CuramConst.kCommaSeparator);
      }
    }

    return languageList.toString();
  }

  /**
   * Retrieve all active skills for a user and split it up into prioritized and
   * PMT skills. Sort the prioritized skills by their priority.
   *
   * @param userSkillDtlsList
   * @param bdmUserTaskDtls
   * @throws DatabaseException
   * @throws AppRuntimeException
   * @throws AppException
   * @throws InformationalException
   */
  private void formatUserSkills(final UserSkillDtlsList userSkillDtlsList,
    final BDMSupervisorUserWithTaskCountDetails bdmUserTaskDtls)
    throws DatabaseException, AppRuntimeException, AppException,
    InformationalException {

    // remove all skills that are not active, we only want to display active
    // ones
    final Predicate<UserSkillDtls> predicate =
      dtls -> !dtls.recordStatus.equals(RECORDSTATUS.DEFAULTCODE);
    userSkillDtlsList.dtls.removeIf(predicate);

    // comparator to sort the skills by their skill level (Primary, Secondary,
    // Tertiary)
    final Comparator<UserSkillDtls> userSkillsComparator =
      new Comparator<UserSkillDtls>() {

        @Override
        public int compare(final UserSkillDtls user1,
          final UserSkillDtls user2) {

          return user1.skillLevel.compareTo(user2.skillLevel);
        }
      };

    userSkillDtlsList.dtls.sort(userSkillsComparator);

    // given the list of sorted user skills, split into the PMT skills and
    // prioritized skills. Create a comma separated list for both
    final StringBuilder userSkills = new StringBuilder();
    final StringBuilder ptmSkills = new StringBuilder();
    for (final UserSkillDtls userSkillDtls : userSkillDtlsList.dtls) {
      if (userSkillDtls.skillLevel.equals(SKILLLEVEL.PTM)) {
        ptmSkills.append(
          CodeTable.getOneItem(SKILLTYPE.TABLENAME, userSkillDtls.skillType));
        ptmSkills.append(CuramConst.kCommaSeparator);
      } else if (userSkillDtls.skillLevel.equals(SKILLLEVEL.NOALLOC)) {
        continue;
      } else {
        userSkills.append(
          CodeTable.getOneItem(SKILLTYPE.TABLENAME, userSkillDtls.skillType));
        userSkills.append(CuramConst.kCommaSeparator);
      }
    }

    bdmUserTaskDtls.prioritizedSkills = userSkills.toString();
    bdmUserTaskDtls.ptmSkills = ptmSkills.toString();

    // removes the last comma and space
    final int lastDelimiterLength = CuramConst.kCommaSeparator.length();
    if (userSkills.length() != 0) {
      bdmUserTaskDtls.prioritizedSkills = bdmUserTaskDtls.prioritizedSkills
        .substring(0, userSkills.length() - lastDelimiterLength);
    }
    if (ptmSkills.length() != 0) {
      bdmUserTaskDtls.ptmSkills = bdmUserTaskDtls.ptmSkills.substring(0,
        ptmSkills.length() - lastDelimiterLength);
    }
  }

  @Override
  public CaseUrgentFlagStringDetails getCaseUrgentFlagsByTaskID(
    final TaskKey key) throws AppException, InformationalException {

    final CaseUrgentFlagStringDetails returnStr =
      new CaseUrgentFlagStringDetails();

    final BizObjAssociation bizObjAssociationObj =
      BizObjAssociationFactory.newInstance();
    final curam.util.workflow.struct.TaskKey taskKey =
      new curam.util.workflow.struct.TaskKey();
    taskKey.taskID = key.taskID;

    final BizObjAssociationDtlsList bizObjAssociationDtlsList =
      bizObjAssociationObj.searchByTaskID(taskKey);

    for (final BizObjAssociationDtls bizObjAssociationDtls : bizObjAssociationDtlsList.dtls) {

      if (BUSINESSOBJECTTYPE.CASE
        .equals(bizObjAssociationDtls.bizObjectType)) {

        returnStr.caseUrgentFlagStr =
          getCaseUrgentFlagStr(bizObjAssociationDtls.bizObjectID);

        break;

      } else if (BUSINESSOBJECTTYPE.BDMCOMMUNICATION
        .equals(bizObjAssociationDtls.bizObjectType)) {

        final NotFoundIndicator crCommNfi = new NotFoundIndicator();
        final ConcernRoleCommunicationKey crCommKey =
          new ConcernRoleCommunicationKey();
        crCommKey.communicationID = bizObjAssociationDtls.bizObjectID;
        final ConcernRoleCommunicationDtls crCommDtls =
          ConcernRoleCommunicationFactory.newInstance().read(crCommNfi,
            crCommKey);

        if (crCommDtls.caseID != 0) {
          returnStr.caseUrgentFlagStr =
            getCaseUrgentFlagStr(crCommDtls.caseID);
        }

        break;
      }
    } // End for

    return returnStr;
  }

  /**
   * Get the case urgent flag list text by case ID.
   *
   * @param caseID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  protected String getCaseUrgentFlagStr(final long caseID)
    throws AppException, InformationalException {

    final CaseIDDetails caseDtlskey = new CaseIDDetails();
    caseDtlskey.caseID = caseID;

    final BDMCaseUrgentFlagDetailsList urgentFlagDtlsList =
      BDMCaseUrgentFlagFactory.newInstance()
        .listCurrentUrgentFlags(caseDtlskey);

    final StringBuilder sb = new StringBuilder();

    int numOfItems = 0;
    for (int j = 0; j < urgentFlagDtlsList.dtls.size(); j++) {

      final String caseUrgentFlagString = CodeTable.getOneItem(
        BDMURGENTFLAGTYPE.TABLENAME, urgentFlagDtlsList.dtls.get(j).type,
        TransactionInfo.getProgramLocale());

      if (numOfItems > 0) {
        sb.append(BDMConstants.gkCommaSpace);
      }

      sb.append(caseUrgentFlagString);
      numOfItems++;
    } // End for

    return sb.toString();
  }

  /**
   * Read skillType by taskID
   */
  @Override
  public BDMTaskSkillTypeKey readSkillTypeByTaskID(final TaskKey key)
    throws AppException, InformationalException {

    final BDMTaskSkillTypeKey bdmTaskSkillTypeKey = new BDMTaskSkillTypeKey();

    final BDMTaskClassification bdmTaskClassificationObj =
      BDMTaskClassificationFactory.newInstance();
    final BDMTaskClassificationKey bdmTaskClassificationKey =
      new BDMTaskClassificationKey();
    BDMTaskClassificationDtls bdmTaskClassificationDtls =
      new BDMTaskClassificationDtls();
    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();

    // final TaskKey bizObjectTaskKey = new TaskKey();
    // bizObjectTaskKey.taskID = key.taskID;

    final BizObjAssociation bizObjAssociationObj =
      BizObjAssociationFactory.newInstance();

    final curam.util.workflow.struct.TaskKey t1 =
      new curam.util.workflow.struct.TaskKey();
    t1.assign(key);

    final BizObjAssociationDtlsList bizObjAssociationDtlsList =
      bizObjAssociationObj.searchByTaskID(t1);

    final Iterator it = bizObjAssociationDtlsList.dtls.iterator();
    while (it.hasNext()) {
      BizObjAssociationDtls bizObjAssociationDtls =
        new BizObjAssociationDtls();
      bizObjAssociationDtls = (BizObjAssociationDtls) it.next();

      if (BUSINESSOBJECTTYPE.TASKSKILLTYPE
        .equals(bizObjAssociationDtls.bizObjectType)) {

        bdmTaskClassificationKey.bdmTaskClassificationID =
          bizObjAssociationDtls.bizObjectID;

        bdmTaskClassificationDtls = bdmTaskClassificationObj
          .read(notFoundIndicator, bdmTaskClassificationKey);
        bdmTaskSkillTypeKey.skillType = bdmTaskClassificationDtls.skillType;
        break;

      } // End if
    } // End while

    return bdmTaskSkillTypeKey;

  }

  // START : TASK 81188
  /**
   * Read escalation level By task ID
   *
   * @param key TaskKey containing taskID
   * @return BDMEscalationLevelString description for Escalation Level
   *
   */

  @Override
  public BDMEscalationLevelString
    readEscalationLevelByTaskID(final curam.core.sl.entity.struct.TaskKey key)
      throws AppException, InformationalException {

    final BDMEscalationLevelString bdmEscalationLevelString =
      new BDMEscalationLevelString();

    final BDMEscalationLevel bdmEscalationLevelObj =
      BDMEscalationLevelFactory.newInstance();
    final BDMEscalationLevelKeyStruct1 bdmEscalationLevelKey =
      new BDMEscalationLevelKeyStruct1();
    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();

    final curam.util.workflow.struct.TaskKey t1 =
      new curam.util.workflow.struct.TaskKey();
    t1.taskID = key.taskID;

    final BizObjAssociation bizObjAssociationObj =
      BizObjAssociationFactory.newInstance();

    // Find Biz ogj associtaion using task id
    final BizObjAssociationDtlsList bizObjAssociationDtlsList =
      bizObjAssociationObj.searchByTaskID(t1);
    final Iterator it = bizObjAssociationDtlsList.dtls.iterator();
    while (it.hasNext()) {
      BizObjAssociationDtls bizObjAssociationDtls =
        new BizObjAssociationDtls();
      bizObjAssociationDtls = (BizObjAssociationDtls) it.next();

      // Find BUSINESSOBJECTTYPE communication
      if (BUSINESSOBJECTTYPE.BDMCOMMUNICATION
        .equals(bizObjAssociationDtls.bizObjectType)) {
        bdmEscalationLevelKey.communicationID =
          bizObjAssociationDtls.bizObjectID;

        final BDMEscalationLevelDtlsStruct2 bdmEscalationLevelDtlsStruct2 =
          bdmEscalationLevelObj.readByCommunicationID(notFoundIndicator,
            bdmEscalationLevelKey);

        if (notFoundIndicator.isNotFound()) {
          bdmEscalationLevelString.escalationLevelDesc = "";

        } else
          bdmEscalationLevelString.escalationLevelDesc =
            CodeTable.getOneItem(BDMESCALATIONLEVELS.TABLENAME,
              bdmEscalationLevelDtlsStruct2.escalationLevel,
              TransactionInfo.getProgramLocale());

        break;

      } // End if
    } // End while

    return bdmEscalationLevelString;
  }

  // ___________________________________________________________________________
  /**
   * This method lists the tasks assigned to a user, but not currently reserved
   * by the user. The list does not include tasks assigned to work queues that
   * the user is subscribed to.
   *
   * @param key -
   * ListUnreservedTasksForUserKey
   * @return BDMListUnreservedTasksForUserDetails
   * @throws InformationalException
   * @throws AppException
   */

  @Override
  public BDMListUnreservedTasksForUserDetails
    listbdmTasksAssignedToUser(final ListUnreservedTasksForUserKey key)
      throws AppException, InformationalException {

    final BDMListUnreservedTasksForUserDetails bdmListUnreservedTasksForUserDetails =
      new BDMListUnreservedTasksForUserDetails();

    // CaseHeader manipulation variables
    final curam.core.sl.intf.Inbox inbox = InboxFactory.newInstance();

    final ListTaskKey listTaskKey = new ListTaskKey();

    listTaskKey.userName = key.userName;
    final ListUnreservedTasksForUserDetails listUnreservedTasksForUserDetails =
      new ListUnreservedTasksForUserDetails();

    listUnreservedTasksForUserDetails.taskDetailsList =
      inbox.listAssigned(listTaskKey);

    for (int i =
      0; i < listUnreservedTasksForUserDetails.taskDetailsList.taskDetailsList
        .size(); i++) {

      final AssignedTaskDetails assignedTasksDetails =
        listUnreservedTasksForUserDetails.taskDetailsList.taskDetailsList
          .get(i);

      final BDMAssignedTaskDetails bdmAssignedTaskDetails =
        new BDMAssignedTaskDetails();
      bdmAssignedTaskDetails.assign(assignedTasksDetails);

      // retrieve case urgent flag and escaltion level
      bdmAssignedTaskDetails.caseUrgentFlagStr =
        BDMTaskUtil.getCaseUrgentFlagsByTaskID(assignedTasksDetails.taskID);
      bdmAssignedTaskDetails.escalationLevelDesc =
        BDMTaskUtil.getEscalationLevelsByTaskID(assignedTasksDetails.taskID);

      bdmListUnreservedTasksForUserDetails.taskDetailsList.taskDetailsList
        .addRef(bdmAssignedTaskDetails);
    }

    return bdmListUnreservedTasksForUserDetails;

  }

}
