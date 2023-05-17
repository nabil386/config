package curam.ca.gc.bdm.test.facade.organization.userskill.impl;

import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.facade.organization.fact.BDMOrganizationFactory;
import curam.ca.gc.bdm.facade.organization.intf.BDMOrganization;
import curam.ca.gc.bdm.facade.organization.struct.BDMUserSkillListKey;
import curam.ca.gc.bdm.facade.organization.struct.BDMUserSkillPriority;
import curam.ca.gc.bdm.facade.organization.struct.BDMUserSkillPriorityList;
import curam.ca.gc.bdm.message.BDMUSERSKILLS;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.test.util.organization.impl.UserUtil;
import curam.codetable.RECORDSTATUS;
import curam.codetable.SKILLLEVEL;
import curam.codetable.SKILLTYPE;
import curam.contextviewer.constant.impl.CuramConst;
import curam.core.facade.struct.CancelUserSkillDetails;
import curam.core.facade.struct.UserSkillCreateDetails;
import curam.core.facade.struct.UserSkillListKey;
import curam.core.facade.struct.UserSkillModifyDetails;
import curam.core.sl.entity.fact.UserSkillFactory;
import curam.core.sl.entity.struct.UserSkillDtls;
import curam.core.sl.entity.struct.UserSkillDtlsList;
import curam.core.sl.entity.struct.UserSkillKey;
import curam.core.sl.struct.CreateUserSkillDetails;
import curam.core.sl.struct.ListUserSkillKey;
import curam.core.sl.struct.ModifyUserSkillDetails;
import curam.core.sl.struct.UserSkillCancelDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BDMUserSkillTest extends CuramServerTestJUnit4 {

  /* OBJECT IN TEST */
  BDMOrganization bdmOrgObj;

  /* GLOBAL VARIABLES */
  UserSkillCreateDetails createDetails;

  UserSkillModifyDetails modifyDetails;

  BDMUserSkillListKey bdmUserSkillListKey;

  UserSkillListKey userSkillListKey;

  CancelUserSkillDetails cancelDetails;

  UserSkillDtls userSkillDetails;

  BDMUserSkillPriorityList skillPriorityList;

  final String kUsername = "user1";

  @Before
  public void setUp() throws AppException, InformationalException {

    UserUtil.createUser(kUsername, BDMLANGUAGE.ENGLISHL);
    userSkillDetails = new UserSkillDtls();
    bdmUserSkillListKey = new BDMUserSkillListKey();
    userSkillListKey = new UserSkillListKey();
    bdmUserSkillListKey.userSkillListKey = userSkillListKey;
    bdmOrgObj = BDMOrganizationFactory.newInstance();
  }

  private void createUserSkill(final String skillLevel,
    final String skillType) throws InformationalException, AppException {

    createDetails = new UserSkillCreateDetails();
    createDetails.userSkillCreateDetails = new CreateUserSkillDetails();
    createDetails.userSkillCreateDetails.createUserSkillDetails =
      userSkillDetails;

    userSkillDetails.userName = kUsername;
    userSkillDetails.skillLevel = skillLevel;
    userSkillDetails.skillType = skillType;
    bdmOrgObj.createUserSkill(createDetails);
  }

  private void addToUserSkillPriorityList(final String skillPriority,
    final String skillType) {

    final BDMUserSkillPriority userSkillPriority = new BDMUserSkillPriority();
    userSkillPriority.skillPriority = skillPriority;
    userSkillPriority.skillType = skillType;
    skillPriorityList.dtls.add(userSkillPriority);
  }

  private void testCancel(final UserSkillKey userSkillKey,
    final String skillLevel, final String skillType)
    throws AppException, InformationalException {

    createUserSkill(skillLevel, skillType);
    userSkillKey.userSkillID =
      createDetails.userSkillCreateDetails.createUserSkillDetails.userSkillID;

    try {
      bdmOrgObj.cancelUserSkillsDetails(cancelDetails);
      Assert.fail();
    } catch (final AppException e) {

      assertEquals(BDMUSERSKILLS.ERR_DELETE_PRIORITY_SKILL.getMessageText(),
        e.getMessage());
    }

  }

  /**
   * Add a tertiary skill without a primary or secondary skill
   *
   * @throws AppException
   */
  @Test
  public void testAddTertiaryNoPrimarySecondary() throws AppException {

    try {
      createUserSkill(SKILLLEVEL.TERTIARY, SKILLTYPE.VSG01);
      Assert.fail();
    } catch (final InformationalException e) {
      final List<String> exceptions =
        Arrays.asList(e.getLocalizedMessage().split(CuramConst.gkNewLine));

      assertEquals(2, exceptions.size());
      assertTrue(exceptions
        .contains(BDMUSERSKILLS.ERR_NO_PRIMARY_PRIORITY.getMessageText()));

      assertTrue(exceptions
        .contains(BDMUSERSKILLS.ERR_NO_SECONDARY_PRIORITY.getMessageText()));

    }

  }

  /**
   * Add a tertiary skill without a primary skill
   *
   * @throws AppException
   */
  @Test
  public void testAddTertiaryNoPrimary()
    throws AppException, InformationalException {

    // do not execute in try block - if an error is thrown here the test should
    // fail. Manual DB add to bypass validations
    UserUtil.addUserSkill(kUsername, SKILLTYPE.VSG02, SKILLLEVEL.SECONDARY,
      RECORDSTATUS.NORMAL);

    try {
      createUserSkill(SKILLLEVEL.TERTIARY, SKILLTYPE.VSG01);
      Assert.fail();
    } catch (final InformationalException e) {
      final List<String> exceptions =
        Arrays.asList(e.getLocalizedMessage().split(CuramConst.gkNewLine));

      assertEquals(1, exceptions.size());

      assertTrue(exceptions
        .contains(BDMUSERSKILLS.ERR_NO_PRIMARY_PRIORITY.getMessageText()));

    }
  }

  /**
   * Add a tertiary skill without a primary skill
   *
   * @throws AppException
   */
  @Test
  public void testAddTertiaryNoSecondary()
    throws AppException, InformationalException {

    // do not execute in try block - if an error is thrown here the test should
    // fail
    createUserSkill(SKILLLEVEL.PRIMARY, SKILLTYPE.VSG02);

    try {
      createUserSkill(SKILLLEVEL.TERTIARY, SKILLTYPE.VSG01);
      Assert.fail();
    } catch (final InformationalException e) {
      final List<String> exceptions =
        Arrays.asList(e.getLocalizedMessage().split(CuramConst.gkNewLine));

      assertEquals(1, exceptions.size());

      assertTrue(exceptions
        .contains(BDMUSERSKILLS.ERR_NO_SECONDARY_PRIORITY.getMessageText()));

    }
  }

  // START: TASK 115765 - MP - Added test scenarios
  /**
   * Add a post training monitoring secondary skill without post training
   * monitoring primary
   *
   * @throws AppException
   */
  @Test
  public void testAddPTMSecondaryNoPTMPrimary()
    throws AppException, InformationalException {

    try {
      createUserSkill(SKILLLEVEL.PTMS, SKILLTYPE.VSG11);
      Assert.fail();
    } catch (final InformationalException e) {
      final List<String> exceptions =
        Arrays.asList(e.getLocalizedMessage().split(CuramConst.gkNewLine));

      assertEquals(1, exceptions.size());

      assertTrue(exceptions.contains(
        BDMUSERSKILLS.ERR_NO_POST_TRAINING_MONITOR_PRIMARY.getMessageText()));

    }
  }

 

  /**
   * Add more than 3 priorities
   * Add more than 1 post training monitoring priorities
   *
   * @throws AppException
   */
  @Test
  public void testAddMoreThanThreeActivePriorites()
    throws AppException, InformationalException {

    createUserSkill(SKILLLEVEL.PRIMARY, SKILLTYPE.VSG11);
    createUserSkill(SKILLLEVEL.PTM, SKILLTYPE.VSG12);
    createUserSkill(SKILLLEVEL.PTMS, SKILLTYPE.VSG13);
    createUserSkill(SKILLLEVEL.NOALLOC, SKILLTYPE.VSG14);
    createUserSkill(SKILLLEVEL.NOALLOC, SKILLTYPE.VSG15);

    try {
      createUserSkill(SKILLLEVEL.PTM, SKILLTYPE.VSG16);
      Assert.fail();
    } catch (final InformationalException e) {
      final List<String> exceptions =
        Arrays.asList(e.getLocalizedMessage().split(CuramConst.gkNewLine));

      assertEquals(2, exceptions.size());

      assertTrue(exceptions
        .contains(BDMUSERSKILLS.ERR_MAX_PRIORITY_SKILLS.getMessageText()));
      assertTrue(exceptions
        .contains(BDMUSERSKILLS.ERR_MULTIPLE_SAME_POST_TRAINING_MONITOR
          .getMessageText()));

    }
  }
  
//END: TASK 115765 - MP - Added test scenarios

  /**
   * Add a secondary skill without a primary skill
   *
   * @throws AppException
   */
  @Test
  public void testAddSecondaryNoPrimary()
    throws AppException, InformationalException {

    try {
      createUserSkill(SKILLLEVEL.SECONDARY, SKILLTYPE.VSG01);
      Assert.fail();
    } catch (final InformationalException e) {
      final List<String> exceptions =
        Arrays.asList(e.getLocalizedMessage().split(CuramConst.gkNewLine));

      assertEquals(1, exceptions.size());

      assertTrue(exceptions
        .contains(BDMUSERSKILLS.ERR_NO_PRIMARY_PRIORITY.getMessageText()));

    }
  }

  /**
   * Add a tertiary skill with a deleted (canceled) primary skill
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testAddTertiaryDeletedPrimary()
    throws AppException, InformationalException {

    // do not execute in try block - if an error is thrown here the test should
    // fail. Manual DB add to bypass validations
    UserUtil.addUserSkill(kUsername, SKILLTYPE.VSG02, SKILLLEVEL.SECONDARY,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(kUsername, SKILLTYPE.VSG05, SKILLLEVEL.PRIMARY,
      RECORDSTATUS.CANCELLED);

    try {
      createUserSkill(SKILLLEVEL.TERTIARY, SKILLTYPE.VSG01);
      Assert.fail();
    } catch (final InformationalException e) {
      final List<String> exceptions =
        Arrays.asList(e.getLocalizedMessage().split(CuramConst.gkNewLine));

      assertEquals(1, exceptions.size());

      assertTrue(exceptions
        .contains(BDMUSERSKILLS.ERR_NO_PRIMARY_PRIORITY.getMessageText()));

    }
  }

  /**
   * Adds duplicate skills
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testAddDuplicatePrioritizedSkills()
    throws AppException, InformationalException {

    createUserSkill(SKILLLEVEL.PRIMARY, SKILLTYPE.VSG01);
    createUserSkill(SKILLLEVEL.SECONDARY, SKILLTYPE.VSG02);
    createUserSkill(SKILLLEVEL.TERTIARY, SKILLTYPE.VSG05);

    final String[] priorities = new String[]{SKILLLEVEL.PRIMARY,
      SKILLLEVEL.SECONDARY, SKILLLEVEL.TERTIARY };

    for (int i = 0; i < 3; i++) {
      try {
        createUserSkill(priorities[i], SKILLTYPE.VSG06);
        Assert.fail();
      } catch (final InformationalException e) {

        final List<String> exceptions =
          Arrays.asList(e.getLocalizedMessage().split(CuramConst.gkNewLine));

        assertEquals(i + 1, exceptions.size());

        assertEquals(
          BDMUSERSKILLS.ERR_MULTIPLE_SAME_PRIORITIES.getMessageText(),
          exceptions.get(i));

      }
    }
  }

  /**
   * Tests getting active skills
   */
  @Test
  public void testGetActiveSkills()
    throws AppException, InformationalException {

    UserUtil.addUserSkill(kUsername, SKILLTYPE.VSG02, SKILLLEVEL.TERTIARY,
      RECORDSTATUS.CANCELLED);
    UserUtil.addUserSkill(kUsername, SKILLTYPE.VSG02, SKILLLEVEL.SECONDARY,
      RECORDSTATUS.NORMAL);
    UserUtil.addUserSkill(kUsername, SKILLTYPE.VSG05, SKILLLEVEL.PTM,
      RECORDSTATUS.CANCELLED);
    UserUtil.addUserSkill(kUsername, SKILLTYPE.VSG07, SKILLLEVEL.TERTIARY,
      RECORDSTATUS.NORMAL);

    userSkillListKey.userSkillList = new ListUserSkillKey();
    userSkillListKey.userSkillList.userName = kUsername;
    final UserSkillDtlsList activeUserSkills =
      bdmOrgObj.getActiveUserSkills(bdmUserSkillListKey);

    assertEquals(2, activeUserSkills.dtls.size());
    assertEquals(SKILLTYPE.VSG02, activeUserSkills.dtls.get(0).skillType);
    assertEquals(SKILLLEVEL.SECONDARY,
      activeUserSkills.dtls.get(0).skillLevel);

    assertEquals(SKILLTYPE.VSG07, activeUserSkills.dtls.get(1).skillType);
    assertEquals(SKILLLEVEL.TERTIARY,
      activeUserSkills.dtls.get(1).skillLevel);
  }

  /**
   * Test modifying skills by adding tertiary and no primary or secondary
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testMassModifyTertiaryNoPrimarySecondary()
    throws AppException, InformationalException {

    skillPriorityList = new BDMUserSkillPriorityList();
    addToUserSkillPriorityList(SKILLLEVEL.TERTIARY, SKILLTYPE.VSG01);
    addToUserSkillPriorityList(SKILLLEVEL.PTM, SKILLTYPE.VSG02);
    addToUserSkillPriorityList(SKILLLEVEL.PTM, SKILLTYPE.VSG06);
    addToUserSkillPriorityList(SKILLLEVEL.NOALLOC, SKILLTYPE.VSG05);

    try {
      bdmOrgObj.modifyListUserSkills(skillPriorityList);
    } catch (final InformationalException e) {
      final List<String> exceptions =
        Arrays.asList(e.getLocalizedMessage().split(CuramConst.gkNewLine));

      assertEquals(2, exceptions.size());
      assertTrue(exceptions
        .contains(BDMUSERSKILLS.ERR_NO_PRIMARY_PRIORITY.getMessageText()));

      assertTrue(exceptions
        .contains(BDMUSERSKILLS.ERR_NO_SECONDARY_PRIORITY.getMessageText()));

    }
  }

  /**
   * Test modifying skills by adding 2 secondary and no primary
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testMassModifyDoubleSecondaryNoPrimary()
    throws AppException, InformationalException {

    skillPriorityList = new BDMUserSkillPriorityList();
    addToUserSkillPriorityList(SKILLLEVEL.SECONDARY, SKILLTYPE.VSG01);
    addToUserSkillPriorityList(SKILLLEVEL.PTM, SKILLTYPE.VSG02);
    addToUserSkillPriorityList(SKILLLEVEL.PTM, SKILLTYPE.VSG06);
    addToUserSkillPriorityList(SKILLLEVEL.SECONDARY, SKILLTYPE.VSG07);
    addToUserSkillPriorityList(SKILLLEVEL.NOALLOC, SKILLTYPE.VSG05);

    try {
      bdmOrgObj.modifyListUserSkills(skillPriorityList);
    } catch (final InformationalException e) {
      final List<String> exceptions =
        Arrays.asList(e.getLocalizedMessage().split(CuramConst.gkNewLine));

      assertEquals(2, exceptions.size());
      assertTrue(exceptions
        .contains(BDMUSERSKILLS.ERR_NO_PRIMARY_PRIORITY.getMessageText()));

      assertTrue(exceptions.contains(
        BDMUSERSKILLS.ERR_MULTIPLE_SAME_PRIORITIES.getMessageText()));

    }
  }

  /**
   * Test modifying skills by adding 2 secondary and no primary
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testValidMassModify()
    throws AppException, InformationalException {

    createUserSkill(SKILLLEVEL.NOALLOC, SKILLTYPE.VSG09);
    createUserSkill(SKILLLEVEL.NOALLOC, SKILLTYPE.VSG01);
    createUserSkill(SKILLLEVEL.NOALLOC, SKILLTYPE.VSG02);
    createUserSkill(SKILLLEVEL.NOALLOC, SKILLTYPE.VSG06);
    createUserSkill(SKILLLEVEL.NOALLOC, SKILLTYPE.VSG07);
    createUserSkill(SKILLLEVEL.NOALLOC, SKILLTYPE.VSG08);
    createUserSkill(SKILLLEVEL.NOALLOC, SKILLTYPE.VSG05);

    userSkillListKey.userSkillList = new ListUserSkillKey();
    userSkillListKey.userSkillList.userName = kUsername;
    UserSkillDtlsList activeUserSkills =
      bdmOrgObj.getActiveUserSkills(bdmUserSkillListKey);

    skillPriorityList = new BDMUserSkillPriorityList();

    final String[] priorities = new String[]{SKILLLEVEL.TERTIARY,
      SKILLLEVEL.SECONDARY, SKILLLEVEL.PTM, SKILLLEVEL.PRIMARY,
      SKILLLEVEL.NOALLOC, SKILLLEVEL.PTM, SKILLLEVEL.NOALLOC };
    for (int i = 0; i < activeUserSkills.dtls.size(); i++) {
      createUserSkillPriorityList(priorities[i],
        activeUserSkills.dtls.get(i).skillType,
        activeUserSkills.dtls.get(i).userSkillID);
    }

    skillPriorityList.userName = kUsername;

    bdmOrgObj.modifyListUserSkills(skillPriorityList);

    activeUserSkills = bdmOrgObj.getActiveUserSkills(bdmUserSkillListKey);

    for (int i = 0; i < skillPriorityList.dtls.size(); i++) {
      assertEquals(skillPriorityList.dtls.get(i).skillPriority,
        activeUserSkills.dtls.get(i).skillLevel);

      assertEquals(skillPriorityList.dtls.get(i).skillType,
        activeUserSkills.dtls.get(i).skillType);
    }

  }

  private void createUserSkillPriorityList(final String skillPriority,
    final String skillType, final long userSkillID) {

    final BDMUserSkillPriority userSkillPriority = new BDMUserSkillPriority();
    userSkillPriority.skillPriority = skillPriority;
    userSkillPriority.skillType = skillType;
    userSkillPriority.userskillID = userSkillID;
    skillPriorityList.dtls.add(userSkillPriority);
  }

  /**
   * Test that modifies properly populates the skill level field
   *
   * @throws InformationalException
   * @throws AppException
   */
  @Test
  public void testModifyUserSkill()
    throws InformationalException, AppException {

    createUserSkill(SKILLLEVEL.PRIMARY, SKILLTYPE.VSG01);

    final long userSkillID =
      createDetails.userSkillCreateDetails.createUserSkillDetails.userSkillID;
    modifyDetails = new UserSkillModifyDetails();
    modifyDetails.modifyUserSkillDetails = new ModifyUserSkillDetails();
    modifyDetails.modifyUserSkillDetails.modifyUserSkillDetails =
      userSkillDetails;
    userSkillDetails.userSkillID = userSkillID;
    userSkillDetails.userName = kUsername;
    userSkillDetails.skillType = SKILLTYPE.VSG02;
    userSkillDetails.recordStatus = RECORDSTATUS.NORMAL;

    bdmOrgObj.modifyUserSkill(modifyDetails);

    final UserSkillKey userSkillKey = new UserSkillKey();
    userSkillKey.userSkillID = userSkillID;
    final UserSkillDtls actualDtls =
      UserSkillFactory.newInstance().read(userSkillKey);

    assertEquals(SKILLTYPE.VSG02, actualDtls.skillType);
    assertEquals(SKILLLEVEL.PRIMARY, actualDtls.skillLevel);

  }

  /**
   * Tests that if a user skill does not have a priority level of No Allocation,
   * that an error is thrown when a cancel is attempted
   *
   * @throws InformationalException
   * @throws AppException
   */
  @Test
  public void testCancelUserSkill()
    throws InformationalException, AppException {

    cancelDetails = new CancelUserSkillDetails();
    cancelDetails.cancelUserSkillDetails = new UserSkillCancelDetails();
    cancelDetails.cancelUserSkillDetails.details =
      new curam.core.sl.entity.struct.CancelUserSkillDetails();
    cancelDetails.cancelUserSkillDetails.details.recordStatus =
      RECORDSTATUS.NORMAL;
    final UserSkillKey userSkillKey = new UserSkillKey();
    cancelDetails.cancelUserSkillDetails.key = userSkillKey;

    testCancel(userSkillKey, SKILLLEVEL.PRIMARY, SKILLTYPE.VSG01);

    testCancel(userSkillKey, SKILLLEVEL.SECONDARY, SKILLTYPE.VSG02);

    testCancel(userSkillKey, SKILLLEVEL.TERTIARY, SKILLTYPE.VSG05);

    testCancel(userSkillKey, SKILLLEVEL.PTM, SKILLTYPE.VSG06);

    testCancel(userSkillKey, SKILLLEVEL.PTMS, SKILLTYPE.VSG11);

  }

  /**
   * Test that a user skill with the priority level of No Allocation is properly
   * cancelled
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testWorkingCancel()
    throws AppException, InformationalException {

    cancelDetails = new CancelUserSkillDetails();
    cancelDetails.cancelUserSkillDetails = new UserSkillCancelDetails();
    cancelDetails.cancelUserSkillDetails.details =
      new curam.core.sl.entity.struct.CancelUserSkillDetails();
    final UserSkillKey userSkillKey = new UserSkillKey();
    cancelDetails.cancelUserSkillDetails.key = userSkillKey;

    createUserSkill(SKILLLEVEL.NOALLOC, SKILLTYPE.VSG01);

    userSkillKey.userSkillID =
      createDetails.userSkillCreateDetails.createUserSkillDetails.userSkillID;
    cancelDetails.cancelUserSkillDetails.details.recordStatus =
      RECORDSTATUS.NORMAL;
    cancelDetails.cancelUserSkillDetails.details.versionNo =
      createDetails.userSkillCreateDetails.createUserSkillDetails.versionNo;

    bdmOrgObj.cancelUserSkillsDetails(cancelDetails);

    final UserSkillDtls actualDtls =
      UserSkillFactory.newInstance().read(userSkillKey);
    assertEquals(RECORDSTATUS.CANCELLED, actualDtls.recordStatus);

  }
}
