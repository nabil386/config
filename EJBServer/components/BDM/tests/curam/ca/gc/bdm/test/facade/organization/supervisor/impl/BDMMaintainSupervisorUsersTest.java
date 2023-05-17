package curam.ca.gc.bdm.test.facade.organization.supervisor.impl;

import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.facade.organization.supervisor.fact.BDMMaintainSupervisorUsersFactory;
import curam.ca.gc.bdm.facade.organization.supervisor.intf.BDMMaintainSupervisorUsers;
import curam.ca.gc.bdm.facade.organization.supervisor.struct.BDMAssignedTaskForUserList;
import curam.ca.gc.bdm.facade.organization.supervisor.struct.BDMListReservedDeferredTasksByUserDetails;
import curam.ca.gc.bdm.facade.organization.supervisor.struct.BDMListReservedOpenTasksByUserDetails;
import curam.ca.gc.bdm.facade.organization.supervisor.struct.BDMListUnreservedTasksForUserDetails;
import curam.ca.gc.bdm.facade.organization.supervisor.struct.BDMListUserTasksByWeekDetails;
import curam.ca.gc.bdm.facade.organization.supervisor.struct.BDMListUserTasksDueOnDateDetails;
import curam.ca.gc.bdm.facade.organization.supervisor.struct.BDMOpenAndDeferredTasksForReallocateTasksDetails;
import curam.ca.gc.bdm.facade.organization.supervisor.struct.BDMReservedTaskByUserList;
import curam.ca.gc.bdm.facade.organization.supervisor.struct.BDMUserAssignedTaskDetailsAndFilterCriteriaDetails;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMDeferredTaskDetails;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMReservedByStatusTaskDetails;
import curam.ca.gc.bdm.sl.organization.supervisor.struct.BDMSupervisorUserWithTaskCountDetails;
import curam.ca.gc.bdm.sl.organization.supervisor.struct.BDMSupervisorUserWithTaskCountDetailsList;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.test.util.organization.impl.UserUtil;
import curam.codetable.RECORDSTATUS;
import curam.codetable.SKILLLEVEL;
import curam.codetable.SKILLTYPE;
import curam.core.impl.CuramConst;
import curam.core.struct.UserNameKey;
import curam.supervisor.facade.struct.AssignedTaskForwardDetails;
import curam.supervisor.facade.struct.ListDeferredTasksReservedByUserKey;
import curam.supervisor.facade.struct.ListOpenTasksReservedByUserKey;
import curam.supervisor.facade.struct.ListUnreservedTasksForUserKey;
import curam.supervisor.facade.struct.ReservedTaskForwardDetails;
import curam.supervisor.facade.struct.UserNameAndDeadLineDateKey;
import curam.supervisor.sl.fact.SupervisorApplicationPageContextDescriptionFactory;
import curam.supervisor.sl.impl.MaintainSupervisorUsers;
import curam.supervisor.sl.struct.SupervisorUserWithTaskCountDetails;
import curam.supervisor.sl.struct.SupervisorUserWithTaskCountDetailsList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

@RunWith(JMockit.class)
public class BDMMaintainSupervisorUsersTest extends CuramServerTestJUnit4 {

  /* OBJECT IN TEST */
  BDMMaintainSupervisorUsers bdmMaintainSupervisorUsersObj;

  /* MOCKED */
  @Mocked
  SupervisorApplicationPageContextDescriptionFactory supervisorApplicationPageContextDescriptionFactory;

  /* Global Variables */
  SupervisorUserWithTaskCountDetailsList supervisorUserWithTaskCountDetailsList;

  @Before
  public void setUp() throws AppException, InformationalException {

    bdmMaintainSupervisorUsersObj =
      BDMMaintainSupervisorUsersFactory.newInstance();

    supervisorUserWithTaskCountDetailsList =
      new SupervisorUserWithTaskCountDetailsList();

    expectReadPageContext();
  }

  /**
   * Expect a call to the super method that the current facade method is based
   * on. Since this is OOTB, we are not testing it and rather assuming that it
   * will provide the correct list of users for us to modify within our
   * facade/sl class
   *
   */
  private void setUpMaintainSupervisorUsers() {

    new MockUp<MaintainSupervisorUsers>() {

      @Mock
      public SupervisorUserWithTaskCountDetailsList
        listSupervisorUsersWithTaskCount() {

        return supervisorUserWithTaskCountDetailsList;
      }
    };
  }

  /**
   * Dummy call to read page context. This is OOTB and we do not need this value
   * for any further processing
   *
   * @throws AppException
   * @throws InformationalException
   */
  private void expectReadPageContext()
    throws AppException, InformationalException {

    new Expectations() {

      {
        SupervisorApplicationPageContextDescriptionFactory.newInstance()
          .readUserNamePageContextDescription((UserNameKey) any);

      }
    };
  }

  private void setUpUser(final String username, final String languages)
    throws AppException, InformationalException {

    UserUtil.createUser(username, languages);

    // add user to return struct
    final SupervisorUserWithTaskCountDetails userDtls =
      new SupervisorUserWithTaskCountDetails();
    userDtls.userName = username;
    supervisorUserWithTaskCountDetailsList.dtls.add(userDtls);
  }

  /**
   * Check that a user returned with no skills has empty strings for its skill
   * lists
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testListNoSkills() throws AppException, InformationalException {

    setUpUser("user1", BDMLANGUAGE.ENGLISHL);
    setUpMaintainSupervisorUsers();

    final BDMSupervisorUserWithTaskCountDetailsList actualList =
      bdmMaintainSupervisorUsersObj
        .listSupervisorUsersWithTaskCountAndSkills();

    assertEquals(1, actualList.dtls.size());
    final BDMSupervisorUserWithTaskCountDetails actualItem =
      actualList.dtls.get(0);
    assertEquals("English", actualItem.langList);
    assertEquals("", actualItem.prioritizedSkills);
    assertEquals("", actualItem.ptmSkills);

  }

  /**
   * Tests only PTM skills appear in the correct attribute and that
   * tab-delimited languages are parsed and formatted correctly
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testListOnlyPTMSkills()
    throws AppException, InformationalException {

    final String username = "user1";
    setUpUser(username, BDMLANGUAGE.ENGLISHL + CuramConst.gkTabDelimiterChar
      + BDMLANGUAGE.FRENCHL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG01, SKILLLEVEL.PTM,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG02, SKILLLEVEL.PTM,
      RECORDSTATUS.NORMAL);

    setUpMaintainSupervisorUsers();

    final BDMSupervisorUserWithTaskCountDetailsList actualList =
      bdmMaintainSupervisorUsersObj
        .listSupervisorUsersWithTaskCountAndSkills();

    assertEquals(1, actualList.dtls.size());
    final BDMSupervisorUserWithTaskCountDetails actualItem =
      actualList.dtls.get(0);
    assertEquals("English, French", actualItem.langList);
    assertEquals("", actualItem.prioritizedSkills);
    assertEquals("VSG01, SP-VSG02", actualItem.ptmSkills);

  }

  /**
   * Test prioritized user skills are retrieved correctly and sorted properly
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testListOnlyPrioritizedSkills()
    throws AppException, InformationalException {

    final String username = "user1";
    setUpUser(username, BDMLANGUAGE.FRENCHL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG01, SKILLLEVEL.SECONDARY,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG02, SKILLLEVEL.TERTIARY,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG05, SKILLLEVEL.PRIMARY,
      RECORDSTATUS.NORMAL);
    // TODO: add unallocated once sort order has been decided

    setUpMaintainSupervisorUsers();

    final BDMSupervisorUserWithTaskCountDetailsList actualList =
      bdmMaintainSupervisorUsersObj
        .listSupervisorUsersWithTaskCountAndSkills();

    assertEquals(1, actualList.dtls.size());
    final BDMSupervisorUserWithTaskCountDetails actualItem =
      actualList.dtls.get(0);
    assertEquals("French", actualItem.langList);
    assertEquals("SP-VSG05, VSG01, SP-VSG02", actualItem.prioritizedSkills);
    assertEquals("", actualItem.ptmSkills);

  }

  @Test
  public void testforwardBDMTasksNotReservedByUserSearch()
    throws AppException, InformationalException {

    final String username = "user1";
    setUpUser(username, BDMLANGUAGE.FRENCHL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG01, SKILLLEVEL.SECONDARY,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG02, SKILLLEVEL.TERTIARY,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG05, SKILLLEVEL.PRIMARY,
      RECORDSTATUS.NORMAL);
    // TODO: add unallocated once sort order has been decided
    final AssignedTaskForwardDetails details =
      new AssignedTaskForwardDetails();

    details.actionIDProperty = "Save";
    details.allTasksAssignedToUser = true;

    setUpMaintainSupervisorUsers();

    final BDMAssignedTaskForUserList actualList =
      bdmMaintainSupervisorUsersObj
        .forwardBDMTasksNotReservedByUserSearch(details);

  }

  @Test
  public void testlistBDMUserAssignedTasksByWeek()
    throws AppException, InformationalException {

    final String username = "user1";
    setUpUser(username, BDMLANGUAGE.FRENCHL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG01, SKILLLEVEL.SECONDARY,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG02, SKILLLEVEL.TERTIARY,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG05, SKILLLEVEL.PRIMARY,
      RECORDSTATUS.NORMAL);

    // object creation
    final curam.supervisor.facade.struct.ListUserTasksByWeekDetails listUserTasksByWeekDetails =
      new curam.supervisor.facade.struct.ListUserTasksByWeekDetails();
    final curam.core.sl.supervisor.struct.UserTasksDueOnDateKey userTasksDueOnDateKey =
      new curam.core.sl.supervisor.struct.UserTasksDueOnDateKey();

    final UserNameAndDeadLineDateKey key = new UserNameAndDeadLineDateKey();

    key.key.userName = "JOHN Smith";
    key.key.taskType = userTasksDueOnDateKey.taskReservationStatus;

    setUpMaintainSupervisorUsers();

    final BDMListUserTasksByWeekDetails actualList =
      bdmMaintainSupervisorUsersObj.listBDMUserAssignedTasksByWeek(key);
    assertEquals("French", actualList.dtls);

  }

  @Test
  public void testlistBDMUserReservedTasksByWeek()
    throws AppException, InformationalException {

    final String username = "user1";
    setUpUser(username, BDMLANGUAGE.FRENCHL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG01, SKILLLEVEL.SECONDARY,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG02, SKILLLEVEL.TERTIARY,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG05, SKILLLEVEL.PRIMARY,
      RECORDSTATUS.NORMAL);

    // object creation
    final curam.supervisor.facade.struct.ListUserTasksByWeekDetails listUserTasksByWeekDetails =
      new curam.supervisor.facade.struct.ListUserTasksByWeekDetails();
    final curam.core.sl.supervisor.struct.UserTasksDueOnDateKey userTasksDueOnDateKey =
      new curam.core.sl.supervisor.struct.UserTasksDueOnDateKey();

    final UserNameAndDeadLineDateKey key = new UserNameAndDeadLineDateKey();

    key.key.userName = "JOHN Smith";
    key.key.taskType = userTasksDueOnDateKey.taskReservationStatus;

    setUpMaintainSupervisorUsers();

    final BDMListUserTasksByWeekDetails actualList =
      bdmMaintainSupervisorUsersObj.listBDMUserReservedTasksByWeek(key);
    assertEquals("French", actualList.dtls);

  }

  @Test
  public void testlistBDMUserTasksByWeek()
    throws AppException, InformationalException {

    final String username = "user1";
    setUpUser(username, BDMLANGUAGE.FRENCHL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG01, SKILLLEVEL.SECONDARY,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG02, SKILLLEVEL.TERTIARY,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG05, SKILLLEVEL.PRIMARY,
      RECORDSTATUS.NORMAL);

    // object creation
    final curam.supervisor.facade.struct.ListUserTasksByWeekDetails listUserTasksByWeekDetails =
      new curam.supervisor.facade.struct.ListUserTasksByWeekDetails();
    final curam.core.sl.supervisor.struct.UserTasksDueOnDateKey userTasksDueOnDateKey =
      new curam.core.sl.supervisor.struct.UserTasksDueOnDateKey();

    final UserNameAndDeadLineDateKey key = new UserNameAndDeadLineDateKey();

    key.key.userName = "JOHN Smith";
    key.key.taskType = userTasksDueOnDateKey.taskReservationStatus;
    key.key.deadlineDate = userTasksDueOnDateKey.deadlineDate;

    setUpMaintainSupervisorUsers();

    final BDMListUserTasksByWeekDetails actualList =
      bdmMaintainSupervisorUsersObj.listBDMUserTasksByWeek(key);

  }

  @Test
  public void testbdmListTasksAssignedToUser()
    throws AppException, InformationalException {

    final String username = "user1";
    setUpUser(username, BDMLANGUAGE.FRENCHL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG01, SKILLLEVEL.SECONDARY,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG02, SKILLLEVEL.TERTIARY,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG05, SKILLLEVEL.PRIMARY,
      RECORDSTATUS.NORMAL);

    // object creation
    final curam.supervisor.facade.struct.ListUserTasksByWeekDetails listUserTasksByWeekDetails =
      new curam.supervisor.facade.struct.ListUserTasksByWeekDetails();
    final curam.core.sl.supervisor.struct.UserTasksDueOnDateKey userTasksDueOnDateKey =
      new curam.core.sl.supervisor.struct.UserTasksDueOnDateKey();

    final ListUnreservedTasksForUserKey key =
      new ListUnreservedTasksForUserKey();

    key.key.userName = "JOHN Smith";

    setUpMaintainSupervisorUsers();

    final BDMListUnreservedTasksForUserDetails actualList =
      bdmMaintainSupervisorUsersObj.bdmListTasksAssignedToUser(key);

  }

  @Test
  public void testlistBDMUserTasksDueOnDate()
    throws AppException, InformationalException {

    final String username = "user1";
    setUpUser(username, BDMLANGUAGE.FRENCHL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG01, SKILLLEVEL.SECONDARY,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG02, SKILLLEVEL.TERTIARY,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG05, SKILLLEVEL.PRIMARY,
      RECORDSTATUS.NORMAL);

    // object creation
    final curam.supervisor.facade.struct.ListUserTasksByWeekDetails listUserTasksByWeekDetails =
      new curam.supervisor.facade.struct.ListUserTasksByWeekDetails();
    final curam.core.sl.supervisor.struct.UserTasksDueOnDateKey userTasksDueOnDateKey =
      new curam.core.sl.supervisor.struct.UserTasksDueOnDateKey();

    final UserNameAndDeadLineDateKey key = new UserNameAndDeadLineDateKey();

    key.key.userName = "JOHN Smith";
    key.key.taskType = userTasksDueOnDateKey.taskReservationStatus;
    key.key.deadlineDate = userTasksDueOnDateKey.deadlineDate;

    setUpMaintainSupervisorUsers();

    final BDMListUserTasksDueOnDateDetails actualList =
      bdmMaintainSupervisorUsersObj.listBDMUserTasksDueOnDate(key);

  }

  @Test
  public void testlistBDMUserAssignedTasksDueOnDate()
    throws AppException, InformationalException {

    final String username = "user1";
    setUpUser(username, BDMLANGUAGE.FRENCHL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG01, SKILLLEVEL.SECONDARY,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG02, SKILLLEVEL.TERTIARY,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG05, SKILLLEVEL.PRIMARY,
      RECORDSTATUS.NORMAL);

    // object creation
    final curam.supervisor.facade.struct.ListUserTasksByWeekDetails listUserTasksByWeekDetails =
      new curam.supervisor.facade.struct.ListUserTasksByWeekDetails();
    final curam.core.sl.supervisor.struct.UserTasksDueOnDateKey userTasksDueOnDateKey =
      new curam.core.sl.supervisor.struct.UserTasksDueOnDateKey();

    final UserNameAndDeadLineDateKey key = new UserNameAndDeadLineDateKey();

    key.key.userName = "JOHN Smith";
    key.key.taskType = userTasksDueOnDateKey.taskReservationStatus;
    key.key.deadlineDate = userTasksDueOnDateKey.deadlineDate;

    setUpMaintainSupervisorUsers();

    final BDMListUserTasksDueOnDateDetails actualList =
      bdmMaintainSupervisorUsersObj.listBDMUserAssignedTasksDueOnDate(key);

  }

  @Test
  public void testlistBDMUserReservedTasksDueOnDate()
    throws AppException, InformationalException {

    final String username = "user1";
    setUpUser(username, BDMLANGUAGE.FRENCHL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG01, SKILLLEVEL.SECONDARY,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG02, SKILLLEVEL.TERTIARY,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG05, SKILLLEVEL.PRIMARY,
      RECORDSTATUS.NORMAL);

    // object creation
    final curam.supervisor.facade.struct.ListUserTasksByWeekDetails listUserTasksByWeekDetails =
      new curam.supervisor.facade.struct.ListUserTasksByWeekDetails();
    final curam.core.sl.supervisor.struct.UserTasksDueOnDateKey userTasksDueOnDateKey =
      new curam.core.sl.supervisor.struct.UserTasksDueOnDateKey();

    final UserNameAndDeadLineDateKey key = new UserNameAndDeadLineDateKey();

    key.key.userName = "JOHN Smith";
    key.key.taskType = userTasksDueOnDateKey.taskReservationStatus;
    key.key.deadlineDate = userTasksDueOnDateKey.deadlineDate;

    setUpMaintainSupervisorUsers();

    final BDMListUserTasksDueOnDateDetails actualList =
      bdmMaintainSupervisorUsersObj.listBDMUserReservedTasksDueOnDate(key);

  }

  /**
   * Test mixed skills are sorted properly, and no allocations/cancelled skills
   * are not displayed
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testListMixedSkills()
    throws AppException, InformationalException {

    final String username = "user1";
    setUpUser(username, BDMLANGUAGE.FRENCHL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG09, SKILLLEVEL.PTM,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG08, SKILLLEVEL.NOALLOC,
      RECORDSTATUS.CANCELLED);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG01, SKILLLEVEL.SECONDARY,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG02, SKILLLEVEL.TERTIARY,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG10, SKILLLEVEL.PTM,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG06, SKILLLEVEL.NOALLOC,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG05, SKILLLEVEL.PRIMARY,
      RECORDSTATUS.NORMAL);
    // TODO: add unallocated once sort order has been decided

    setUpMaintainSupervisorUsers();

    final BDMSupervisorUserWithTaskCountDetailsList actualList =
      bdmMaintainSupervisorUsersObj
        .listSupervisorUsersWithTaskCountAndSkills();

    assertEquals(1, actualList.dtls.size());
    final BDMSupervisorUserWithTaskCountDetails actualItem =
      actualList.dtls.get(0);
    assertEquals("French", actualItem.langList);
    assertEquals("SP-VSG05, VSG01, SP-VSG02", actualItem.prioritizedSkills);
    assertEquals("SP-VSG09, SP-VSG10", actualItem.ptmSkills);

  }

  @Test
  public void testbdmReadOpenAndDererredTasksForReallocateTasks()
    throws AppException, InformationalException {

    final String username = "user1";
    setUpUser(username, BDMLANGUAGE.FRENCHL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG09, SKILLLEVEL.PTM,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG08, SKILLLEVEL.NOALLOC,
      RECORDSTATUS.CANCELLED);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG01, SKILLLEVEL.SECONDARY,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG02, SKILLLEVEL.TERTIARY,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG10, SKILLLEVEL.PTM,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG06, SKILLLEVEL.NOALLOC,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG05, SKILLLEVEL.PRIMARY,
      RECORDSTATUS.NORMAL);
    // TODO: add unallocated once sort order has been decided

    final UserNameKey key = new UserNameKey();
    key.userName = "Jphn Smith";
    setUpMaintainSupervisorUsers();

    final BDMOpenAndDeferredTasksForReallocateTasksDetails actualList =
      bdmMaintainSupervisorUsersObj
        .bdmReadOpenAndDererredTasksForReallocateTasks(key);

    assertEquals("French", actualList.deferredTasksDetails);

  }

  @Test
  public void testforwardBDMTasksReservedByUserSearch()
    throws AppException, InformationalException {

    final String username = "user1";
    setUpUser(username, BDMLANGUAGE.FRENCHL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG09, SKILLLEVEL.PTM,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG08, SKILLLEVEL.NOALLOC,
      RECORDSTATUS.CANCELLED);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG01, SKILLLEVEL.SECONDARY,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG02, SKILLLEVEL.TERTIARY,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG10, SKILLLEVEL.PTM,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG06, SKILLLEVEL.NOALLOC,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG05, SKILLLEVEL.PRIMARY,
      RECORDSTATUS.NORMAL);
    // TODO: add unallocated once sort order has been decided

    final ReservedTaskForwardDetails details =
      new ReservedTaskForwardDetails();
    details.actionIDProperty = "Save";
    details.allTasksReservedByUser = true;
    setUpMaintainSupervisorUsers();

    final BDMReservedTaskByUserList actualList = bdmMaintainSupervisorUsersObj
      .forwardBDMTasksReservedByUserSearch(details);

    assertEquals("French", actualList.deferredTaskDtls);

  }

  @Test
  public void testlistBDMUserAssignedTasksAndFilterCriteriaDetails()
    throws AppException, InformationalException {

    final String username = "user1";
    setUpUser(username, BDMLANGUAGE.FRENCHL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG09, SKILLLEVEL.PTM,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG08, SKILLLEVEL.NOALLOC,
      RECORDSTATUS.CANCELLED);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG01, SKILLLEVEL.SECONDARY,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG02, SKILLLEVEL.TERTIARY,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG10, SKILLLEVEL.PTM,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG06, SKILLLEVEL.NOALLOC,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG05, SKILLLEVEL.PRIMARY,
      RECORDSTATUS.NORMAL);
    // TODO: add unallocated once sort order has been decided
    final ListUnreservedTasksForUserKey key =
      new ListUnreservedTasksForUserKey();
    key.key.userName = "SELLY";
    setUpMaintainSupervisorUsers();

    final BDMUserAssignedTaskDetailsAndFilterCriteriaDetails actualList =
      bdmMaintainSupervisorUsersObj
        .listBDMUserAssignedTasksAndFilterCriteriaDetails(key);

  }

  /**
   * Test that when given a list of multiple users, it still parses correctly
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testMultipleUsers()
    throws AppException, InformationalException {

    String username = "user1";
    setUpUser(username, BDMLANGUAGE.FRENCHL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG09, SKILLLEVEL.PTM,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG01, SKILLLEVEL.SECONDARY,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG05, SKILLLEVEL.PRIMARY,
      RECORDSTATUS.NORMAL);

    username = "user2";
    setUpUser(username, BDMLANGUAGE.ENGLISHL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG02, SKILLLEVEL.PRIMARY,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG10, SKILLLEVEL.PTM,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG01, SKILLLEVEL.SECONDARY,
      RECORDSTATUS.NORMAL);
    // TODO: add unallocated once sort order has been decided

    setUpMaintainSupervisorUsers();

    final BDMSupervisorUserWithTaskCountDetailsList actualList =
      bdmMaintainSupervisorUsersObj
        .listSupervisorUsersWithTaskCountAndSkills();

    assertEquals(2, actualList.dtls.size());
    BDMSupervisorUserWithTaskCountDetails actualItem = actualList.dtls.get(0);
    assertEquals("French", actualItem.langList);
    assertEquals("SP-VSG05, VSG01", actualItem.prioritizedSkills);
    assertEquals("SP-VSG09", actualItem.ptmSkills);

    actualItem = actualList.dtls.get(1);
    assertEquals("English", actualItem.langList);
    assertEquals("SP-VSG02, VSG01", actualItem.prioritizedSkills);
    assertEquals("SP-VSG10", actualItem.ptmSkills);

  }

  /**
   * Test for a given a user, list all the deferred tasks .
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testBDMListReservedDeferredTasksByUserDetails()
    throws AppException, InformationalException {

    final String username = "user1";

    setUpUser(username, BDMLANGUAGE.ENGLISHL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG02, SKILLLEVEL.PRIMARY,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG10, SKILLLEVEL.PTM,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG01, SKILLLEVEL.SECONDARY,
      RECORDSTATUS.NORMAL);
    // TODO: add unallocated once sort order has been decided

    setUpMaintainSupervisorUsers();

    final ListDeferredTasksReservedByUserKey listDeferredTasksReservedByUserKey =
      new ListDeferredTasksReservedByUserKey();
    listDeferredTasksReservedByUserKey.key.userName = username;

    final BDMListReservedDeferredTasksByUserDetails actualOut =
      bdmMaintainSupervisorUsersObj.listBDMReservedDeferredTasksByUser(
        listDeferredTasksReservedByUserKey);
    if (actualOut.dtls.taskDetailsList.taskDetailsList.size() > 0) {
      final BDMDeferredTaskDetails bdmDeferredTaskDetails =
        actualOut.dtls.taskDetailsList.taskDetailsList.get(0);
      assertEquals("SUBJECT", bdmDeferredTaskDetails.subject);
      assertEquals(0, bdmDeferredTaskDetails.taskID);
      assertEquals(0, bdmDeferredTaskDetails.priority);
    }

  }

  /**
   * Test for a given a user,list all the reserved open tasks .
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testListBDMReservedOpenTasksByUser()
    throws AppException, InformationalException {

    final String username = "user1";

    setUpUser(username, BDMLANGUAGE.ENGLISHL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG02, SKILLLEVEL.PRIMARY,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG10, SKILLLEVEL.PTM,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(username, SKILLTYPE.VSG01, SKILLLEVEL.SECONDARY,
      RECORDSTATUS.NORMAL);
    // TODO: add unallocated once sort order has been decided

    setUpMaintainSupervisorUsers();

    final ListOpenTasksReservedByUserKey Key =
      new ListOpenTasksReservedByUserKey();
    Key.key.userName = username;
    final BDMListReservedOpenTasksByUserDetails actualOut =
      bdmMaintainSupervisorUsersObj.listBDMReservedOpenTasksByUser(Key);
    if (actualOut.dtls.taskDetailsList.taskDetailsList.size() > 0) {
      final BDMReservedByStatusTaskDetails bdmReservedByStatusTaskDetails =
        actualOut.dtls.taskDetailsList.taskDetailsList.get(0);
      assertEquals("SUBJECT", bdmReservedByStatusTaskDetails.subject);
      assertEquals(0, bdmReservedByStatusTaskDetails.taskID);
      assertEquals(0, bdmReservedByStatusTaskDetails.priority);
    }

  }

}
