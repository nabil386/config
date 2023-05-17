package curam.ca.gc.bdm.test.facade.organization.impl;

import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.facade.organization.fact.BDMOrganizationFactory;
import curam.ca.gc.bdm.facade.organization.intf.BDMOrganization;
import curam.ca.gc.bdm.facade.organization.struct.BDMAlternateLoginEnabled;
import curam.ca.gc.bdm.facade.organization.struct.BDMCreateAndAssignUser;
import curam.ca.gc.bdm.facade.organization.struct.BDMCreateUserDetails;
import curam.ca.gc.bdm.facade.organization.struct.BDMModifyUserDetails;
import curam.ca.gc.bdm.facade.organization.struct.BDMReadUserDetails;
import curam.ca.gc.bdm.facade.organization.struct.BDMUserSkillListKey;
import curam.ca.gc.bdm.facade.organization.struct.BDMUserSkillPriority;
import curam.ca.gc.bdm.facade.organization.struct.BDMUserSkillPriorityList;
import curam.ca.gc.bdm.facade.organization.struct.UserLanguageDetailsList;
import curam.ca.gc.bdm.message.BDMUSERSKILLS;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.LOCALE;
import curam.codetable.RECORDSTATUS;
import curam.codetable.SKILLLEVEL;
import curam.codetable.SKILLTYPE;
import curam.core.facade.struct.CancelUserSkillDetails;
import curam.core.facade.struct.ReadUserDetails_fo;
import curam.core.facade.struct.ReadUserHomePageKey;
import curam.core.facade.struct.ReadUserKey;
import curam.core.facade.struct.UserSkillCreateDetails;
import curam.core.facade.struct.UserSkillModifyDetails;
import curam.core.impl.EnvVars;
import curam.core.sl.entity.struct.UserSkillDtls;
import curam.core.sl.entity.struct.UserSkillDtlsList;
import curam.message.BPOADMINEXTERNALUSER;
import curam.util.resources.Configuration;
import curam.util.type.Date;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.tools.configuration.base.MethodRef;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)
public class BDMOrganizationTest extends CuramServerTestJUnit4 {

  /* OBJECT IN TEST */
  curam.ca.gc.bdm.facade.organization.intf.BDMOrganization bdmOrganizationObj;

  private BDMOrganization createTestSubject() {

    bdmOrganizationObj = BDMOrganizationFactory.newInstance();

    return bdmOrganizationObj;
  }

  private void setupUser() throws Exception {

    final BDMOrganization testSubject;
    final BDMCreateUserDetails details = new BDMCreateUserDetails();

    details.userLangList = BDMLANGUAGE.ENGLISHL + "\t" + BDMLANGUAGE.FRENCHL;
    details.createUserDetails.userDetails.userName = "testuser";
    details.createUserDetails.userDetails.accountEnabled = true;
    details.createUserDetails.userDetails.roleName = "SUPERROLE";
    details.createUserDetails.userDetails.fullName = "TEST USER";
    details.createUserDetails.userDetails.firstname = "TEST";
    details.createUserDetails.userDetails.surname = "USER";
    details.createUserDetails.userDetails.locationID = 1;
    details.createUserDetails.userDetails.sensitivity = "1";
    details.createUserDetails.userDetails.password = "password";
    details.createUserDetails.userDetails.passwordConfirm = "password";
    Configuration.setProperty(EnvVars.BDM_ENV_ALTERNATE_LOGIN_ID_ENABLED,"false");

    // default test
    testSubject = createTestSubject();
    testSubject.createUser(details);

  }
  
  /**
   * Test the creation of New User and Language Field
   *
   * @throws AppException
   * @throws InformationalException
   */
  @MethodRef(name = "createUser", signature = "(QBDMCreateUserDetails;)V")
  @Test
  public void testCreateUser() throws Exception {

    final BDMOrganization testSubject;
    final BDMCreateUserDetails details = new BDMCreateUserDetails();

    details.userLangList = BDMLANGUAGE.ENGLISHL + "\t" + BDMLANGUAGE.FRENCHL;
    details.createUserDetails.userDetails.userName = "testuser";
    details.createUserDetails.userDetails.accountEnabled = true;
    details.createUserDetails.userDetails.roleName = "SUPERROLE";
    details.createUserDetails.userDetails.fullName = "TEST USER";
    details.createUserDetails.userDetails.firstname = "TEST";
    details.createUserDetails.userDetails.surname = "USER";
    details.createUserDetails.userDetails.locationID = 1;
    details.createUserDetails.userDetails.locationName = "LocationName";
    details.createUserDetails.userDetails.defaultLocale = LOCALE.ENGLISH_CA;
    details.createUserDetails.userDetails.sensitivity = "1";
    details.createUserDetails.userDetails.password = "password";
    details.createUserDetails.userDetails.passwordConfirm = "password";

    // default test
    testSubject = createTestSubject();
    testSubject.createUser(details);

    final ReadUserKey readKey = new ReadUserKey();
    readKey.userKeyStruct.userName = "testuser";
    final ReadUserDetails_fo createdUserDetails =
      testSubject.readUser(readKey);
    assertNotNull(createdUserDetails);
    assertTrue(createdUserDetails.userDetails.userName.equals("testuser"));
  }
  
  /**
   * Test the creation of New User and Language Field
   *
   * @throws AppException
   * @throws InformationalException
   */
  @MethodRef(name = "createUser", signature = "(QBDMCreateUserDetails;)V")
  @Test
  public void testCreateAADUser() throws Exception {

    final BDMOrganization testSubject;
    final BDMCreateUserDetails details = new BDMCreateUserDetails();

    details.userLangList = BDMLANGUAGE.ENGLISHL + "\t" + BDMLANGUAGE.FRENCHL;
    details.createUserDetails.userDetails.accountEnabled = true;
    details.createUserDetails.userDetails.roleName = "SUPERROLE";
    details.createUserDetails.userDetails.fullName = "TEST USER";
    details.createUserDetails.userDetails.firstname = "TEST";
    details.createUserDetails.userDetails.surname = "USER";
    details.createUserDetails.userDetails.locationID = 1;
    details.createUserDetails.userDetails.locationName = "LocationName";
    details.createUserDetails.userDetails.defaultLocale = LOCALE.ENGLISH_CA;
    details.createUserDetails.userDetails.sensitivity = "1";
    details.createUserDetails.userDetails.password = "password";
    details.createUserDetails.userDetails.passwordConfirm = "password";
    details.createUserDetails.loginId = "testuser@testuser.org";
    Configuration.setProperty(EnvVars.BDM_ENV_ALTERNATE_LOGIN_ID_ENABLED,"true");

    // default test
    testSubject = createTestSubject();
    testSubject.createUser(details);

    final ReadUserKey readKey = new ReadUserKey();
    readKey.userKeyStruct.userName = "testuser";
    final ReadUserDetails_fo createdUserDetails =
      testSubject.readUser(readKey);
    assertNotNull(createdUserDetails);
    assertTrue(createdUserDetails.userDetails.userName.equals("testuser"));
  }
  
  /**
   * Test the creation of New User and Language Field
   *
   * @throws AppException
   * @throws InformationalException
   */
  @MethodRef(name = "createUser", signature = "(QBDMCreateUserDetails;)V")
  @Test
  public void testCreateAADUser_NoUsername() throws Exception {

    final BDMOrganization testSubject;
    final BDMCreateUserDetails details = new BDMCreateUserDetails();

    details.userLangList = BDMLANGUAGE.ENGLISHL + "\t" + BDMLANGUAGE.FRENCHL;
    details.createUserDetails.userDetails.accountEnabled = true;
    details.createUserDetails.userDetails.roleName = "SUPERROLE";
    details.createUserDetails.userDetails.fullName = "TEST USER";
    details.createUserDetails.userDetails.firstname = "TEST";
    details.createUserDetails.userDetails.surname = "USER";
    details.createUserDetails.userDetails.locationID = 1;
    details.createUserDetails.userDetails.locationName = "LocationName";
    details.createUserDetails.userDetails.defaultLocale = LOCALE.ENGLISH_CA;
    details.createUserDetails.userDetails.sensitivity = "1";
    details.createUserDetails.userDetails.password = "password";
    details.createUserDetails.userDetails.passwordConfirm = "password";
    Configuration.setProperty(EnvVars.BDM_ENV_ALTERNATE_LOGIN_ID_ENABLED,"true");

    // default test
    testSubject = createTestSubject();
    try {
    testSubject.createUser(details);
    } catch (Exception e) {
      assertTrue(e.getMessage().equals(BDMUSERSKILLS.ERR_USER_LOGIN_ID_MISSING.getMessageText()));
    }
    
  }

  /**
   * Test the creation of New User and Language Field
   *
   * @throws AppException
   * @throws InformationalException
   */
  @MethodRef(name = "createUser", signature = "(QBDMCreateUserDetails;)V")
  @Test
  public void testCreateAADUser_LongUsername() throws Exception {

    final BDMOrganization testSubject;
    final BDMCreateUserDetails details = new BDMCreateUserDetails();

    details.userLangList = BDMLANGUAGE.ENGLISHL + "\t" + BDMLANGUAGE.FRENCHL;
    details.createUserDetails.userDetails.accountEnabled = true;
    details.createUserDetails.userDetails.roleName = "SUPERROLE";
    details.createUserDetails.userDetails.fullName = "TEST USER";
    details.createUserDetails.userDetails.firstname = "TEST";
    details.createUserDetails.userDetails.surname = "USER";
    details.createUserDetails.userDetails.locationID = 1;
    details.createUserDetails.userDetails.locationName = "LocationName";
    details.createUserDetails.userDetails.defaultLocale = LOCALE.ENGLISH_CA;
    details.createUserDetails.userDetails.sensitivity = "1";
    details.createUserDetails.userDetails.password = "password";
    details.createUserDetails.userDetails.passwordConfirm = "password";
    details.createUserDetails.loginId = "testusertestusertestusertestusertestusertestusertestusertestuserte@testuser.org";
    Configuration.setProperty(EnvVars.BDM_ENV_ALTERNATE_LOGIN_ID_ENABLED,"true");

    // default test
    testSubject = createTestSubject();
    try {
    testSubject.createUser(details);
    } catch (Exception e) {
      assertTrue(e.getMessage().equals(BDMUSERSKILLS.ERR_USER_LOGIN_ID_SIZE.getMessageText()));
    }    
  }
  
  /**
   * Test the creation of New User and Language Field
   *
   * @throws AppException
   * @throws InformationalException
   */
  @MethodRef(name = "createUser", signature = "(QBDMCreateUserDetails;)V")
  @Test
  public void testCreateAADUser_NonUnique() throws Exception {

    final BDMOrganization testSubject;
    final BDMCreateUserDetails details = new BDMCreateUserDetails();

    details.userLangList = BDMLANGUAGE.ENGLISHL + "\t" + BDMLANGUAGE.FRENCHL;
    details.createUserDetails.userDetails.accountEnabled = true;
    details.createUserDetails.userDetails.roleName = "SUPERROLE";
    details.createUserDetails.userDetails.fullName = "TEST USER";
    details.createUserDetails.userDetails.firstname = "TEST";
    details.createUserDetails.userDetails.surname = "USER";
    details.createUserDetails.userDetails.locationID = 1;
    details.createUserDetails.userDetails.locationName = "LocationName";
    details.createUserDetails.userDetails.defaultLocale = LOCALE.ENGLISH_CA;
    details.createUserDetails.userDetails.sensitivity = "1";
    details.createUserDetails.userDetails.password = "password";
    details.createUserDetails.userDetails.passwordConfirm = "password";
    details.createUserDetails.loginId = "testuser@testuser.org";
    Configuration.setProperty(EnvVars.BDM_ENV_ALTERNATE_LOGIN_ID_ENABLED,"true");

    // default test
    testSubject = createTestSubject();
    testSubject.createUser(details);
    try {
    testSubject.createUser(details);
    } catch (Exception e) {
      assertTrue(e.getMessage().equals(BPOADMINEXTERNALUSER.ERR_USER_XRV_DUPLICATE_RECORD.getMessageText()));
    }    
  }
  
  /**
   * Test the creation of New User and Language Field
   *
   * @throws AppException
   * @throws InformationalException
   */
  @MethodRef(name = "createUser", signature = "(QBDMCreateUserDetails;)V")
  @Test
  public void testCreateUser_NoUsername() throws Exception {

    final BDMOrganization testSubject;
    final BDMCreateUserDetails details = new BDMCreateUserDetails();

    details.userLangList = BDMLANGUAGE.ENGLISHL + "\t" + BDMLANGUAGE.FRENCHL;
    details.createUserDetails.userDetails.accountEnabled = true;
    details.createUserDetails.userDetails.roleName = "SUPERROLE";
    details.createUserDetails.userDetails.fullName = "TEST USER";
    details.createUserDetails.userDetails.firstname = "TEST";
    details.createUserDetails.userDetails.surname = "USER";
    details.createUserDetails.userDetails.locationID = 1;
    details.createUserDetails.userDetails.locationName = "LocationName";
    details.createUserDetails.userDetails.defaultLocale = LOCALE.ENGLISH_CA;
    details.createUserDetails.userDetails.sensitivity = "1";
    details.createUserDetails.userDetails.password = "password";
    details.createUserDetails.userDetails.passwordConfirm = "password";

    // default test
    testSubject = createTestSubject();
    try {
    testSubject.createUser(details);
    } catch (Exception e) {
      assertEquals(e.getMessage(), BDMUSERSKILLS.ERR_USER_USERNAME_MISSING.getMessageText());
    }    
  }
  
  /**
   * Test the creation of New User and Language Field
   *
   * @throws AppException
   * @throws InformationalException
   */
  @MethodRef(name = "createUser", signature = "(QBDMCreateUserDetails;)V")
  @Test
  public void testCreateUser_NoPaswd() throws Exception {

    final BDMOrganization testSubject;
    final BDMCreateUserDetails details = new BDMCreateUserDetails();

    details.userLangList = BDMLANGUAGE.ENGLISHL + "\t" + BDMLANGUAGE.FRENCHL;
    details.createUserDetails.userDetails.accountEnabled = true;
    details.createUserDetails.userDetails.roleName = "SUPERROLE";
    details.createUserDetails.userDetails.fullName = "TEST USER";
    details.createUserDetails.userDetails.firstname = "TEST";
    details.createUserDetails.userDetails.surname = "USER";
    details.createUserDetails.userDetails.locationID = 1;
    details.createUserDetails.userDetails.locationName = "LocationName";
    details.createUserDetails.userDetails.defaultLocale = LOCALE.ENGLISH_CA;
    details.createUserDetails.userDetails.sensitivity = "1";
    details.createUserDetails.userDetails.passwordConfirm = "password";
    details.createUserDetails.userDetails.userName = "testuser";

    // default test
    testSubject = createTestSubject();
    try {
    testSubject.createUser(details);
    } catch (Exception e) {
      assertEquals(e.getMessage(), BDMUSERSKILLS.ERR_USER_PASSWORD_MISSING.getMessageText());
    }    
  }
  
  /**
   * Test the creation of New User and Language Field
   *
   * @throws AppException
   * @throws InformationalException
   */
  @MethodRef(name = "createUser", signature = "(QBDMCreateUserDetails;)V")
  @Test
  public void testCreateUser_NoConfPaswd() throws Exception {

    final BDMOrganization testSubject;
    final BDMCreateUserDetails details = new BDMCreateUserDetails();

    details.userLangList = BDMLANGUAGE.ENGLISHL + "\t" + BDMLANGUAGE.FRENCHL;
    details.createUserDetails.userDetails.accountEnabled = true;
    details.createUserDetails.userDetails.roleName = "SUPERROLE";
    details.createUserDetails.userDetails.fullName = "TEST USER";
    details.createUserDetails.userDetails.firstname = "TEST";
    details.createUserDetails.userDetails.surname = "USER";
    details.createUserDetails.userDetails.locationID = 1;
    details.createUserDetails.userDetails.locationName = "LocationName";
    details.createUserDetails.userDetails.defaultLocale = LOCALE.ENGLISH_CA;
    details.createUserDetails.userDetails.sensitivity = "1";
    details.createUserDetails.userDetails.password = "password";
    details.createUserDetails.userDetails.userName = "testuser";

    // default test
    testSubject = createTestSubject();
    try {
    testSubject.createUser(details);
    } catch (Exception e) {
      assertEquals(e.getMessage(), BDMUSERSKILLS.ERR_USER_CONFPASSWORD_MISSING.getMessageText());
    }    
  }
  
  /**
   * Test the Read of Alternate Login Enabled and
   * population of the language checkboxes
   *
   * @throws AppException
   * @throws InformationalException
   */

  @MethodRef(name = "readBDMAlternateLoginEnabled",
    signature = "()QBDMAlternateLoginEnabled;")
  @Test
  public void testReadBDMAlternateLoginEnabled() throws Exception {

    setupUser();

    BDMOrganization testSubject;
    BDMAlternateLoginEnabled result;

    // default test
    testSubject = createTestSubject();
    result = testSubject.readBDMAlternateLoginEnabled();
  }

  /**
   * Test the Read of the user
   *
   * @throws AppException
   * @throws InformationalException
   */

  @MethodRef(name = "readBDMUser",
    signature = "(QReadUserKey;)QBDMReadUserDetails;")
  @Test
  public void testReadBDMUser() throws Exception {

    setupUser();

    BDMOrganization testSubject;
    final ReadUserKey key = new ReadUserKey();
    key.userKeyStruct.userName = "testuser";

    BDMReadUserDetails result;

    // default test
    testSubject = createTestSubject();
    result = testSubject.readBDMUser(key);
  }

  /**
   * Test the modification of New User and Language Field
   *
   * @throws AppException
   * @throws InformationalException
   */

  @MethodRef(name = "modifyUser", signature = "(QBDMModifyUserDetails;)V")
  @Test
  public void testModifyUser() throws Exception {

    setupUser();

    BDMOrganization testSubject;

    final ReadUserKey key = new ReadUserKey();
    key.userKeyStruct.userName = "testuser";

    BDMReadUserDetails readResult;

    // default test
    testSubject = createTestSubject();
    readResult = testSubject.readBDMUser(key);

    final BDMModifyUserDetails details = new BDMModifyUserDetails();

    // readResult.readUserDtls.userDetails
    // .assign(details.modifyUserdtls.userDetails);

    details.modifyUserdtls.userDetails
      .assign(readResult.readUserDtls.userDetails);

    details.userLangList = BDMLANGUAGE.ENGLISHL;

    // default test
    testSubject = createTestSubject();
    testSubject.modifyUser(details);
    readResult = testSubject.readBDMUser(key);
    assertTrue(readResult.allLangList.dtls.get(0).langCode.equals(BDMLANGUAGE.ENGLISHL));
  }

  /**
   * Test the read of the language list
   *
   * @throws AppException
   * @throws InformationalException
   */

  @MethodRef(name = "readUserLangList",
    signature = "()QUserLanguageDetailsList;")
  @Test
  public void testReadUserLangList() throws Exception {

    setupUser();

    BDMOrganization testSubject;
    UserLanguageDetailsList result;

    // default test
    testSubject = createTestSubject();
    result = testSubject.readUserLangList();
  }

  /**
   * Test the creation of position and assign new user
   *
   * @throws AppException
   * @throws InformationalException
   */

  @MethodRef(name = "createAndAssignUserForPosition",
    signature = "(QBDMCreateAndAssignUser;)V")
  @Test
  public void testCreateAndAssignUserForPosition() throws Exception {

    BDMOrganization testSubject;
    final BDMCreateAndAssignUser details = new BDMCreateAndAssignUser();

    details.userLangList = BDMLANGUAGE.ENGLISHL + "\t" + BDMLANGUAGE.FRENCHL;
    details.createUserDtls.createUserDetails.userName = "testuser";
    details.createUserDtls.createUserDetails.accountEnabled = true;
    details.createUserDtls.createUserDetails.roleName = "SUPERROLE";
    details.createUserDtls.createUserDetails.fullName = "TEST USER";
    details.createUserDtls.createUserDetails.firstname = "TEST";
    details.createUserDtls.createUserDetails.surname = "USER";
    details.createUserDtls.createUserDetails.locationID = 1;
    details.createUserDtls.createUserDetails.sensitivity = "1";
    details.createUserDtls.createUserDetails.password = "password";
    details.createUserDtls.createUserDetails.passwordConfirm = "password";

    details.createUserDtls.assignDetails.organisationID = 80000;
    details.createUserDtls.assignDetails.positionHolderLinkDetails.positionHolderLinkDtls.fromDate =
      Date.getCurrentDate();
    details.createUserDtls.assignDetails.positionHolderLinkDetails.positionHolderLinkDtls.organisationStructureID =
      1;
    details.createUserDtls.assignDetails.positionHolderLinkDetails.positionHolderLinkDtls.positionID =
      80000;

    details.createUserDtls.assignDetails.positionHolderLinkDetails.positionHolderLinkDtls.recordStatus =
      "RST1";

    details.createUserDtls.assignDetails.positionHolderLinkDetails.positionHolderLinkDtls.userName =
      "testuser";

    // default test
    testSubject = createTestSubject();
    testSubject.createAndAssignUserForPosition(details);
  }

  /**
   * Test the read of the User home page with new lang field
   *
   * @throws AppException
   * @throws InformationalException
   */

  @MethodRef(name = "readBDMOrganizationUserHomePage",
    signature = "(QReadUserHomePageKey;)QBDMReadOrgUserHomePageDetails;")
  @Test
  public void testReadBDMOrganizationUserHomePage_AAD() throws Exception {

    testCreateAADUser();

    BDMOrganization testSubject;
    final ReadUserHomePageKey key = new ReadUserHomePageKey();

    key.userKeyStruct.userName = "testuser";

    // default test
    testSubject = createTestSubject();
    testSubject.readBDMOrganizationUserHomePage(key);
  
  }

    /**
     * Test the read of the User home page with new lang field
     *
     * @throws AppException
     * @throws InformationalException
     */

    @MethodRef(name = "readBDMOrganizationUserHomePage",
      signature = "(QReadUserHomePageKey;)QBDMReadOrgUserHomePageDetails;")
    @Test
    public void testReadBDMOrganizationUserHomePage() throws Exception {

      setupUser();

      BDMOrganization testSubject;
      final ReadUserHomePageKey key = new ReadUserHomePageKey();

      key.userKeyStruct.userName = "testuser";

      // default test
      testSubject = createTestSubject();
      testSubject.readBDMOrganizationUserHomePage(key);
    }
    

  @MethodRef(name = "modifyListUserSkills",
    signature = "(QBDMUserSkillPriorityList;)V")
  @Test
  public void testModifyListUserSkills() throws Exception {


    setupUser();
    
    BDMOrganization testSubject;
    final UserSkillCreateDetails userSkillCreateDetails = new UserSkillCreateDetails();
    userSkillCreateDetails.userSkillCreateDetails.createUserSkillDetails.userSkillID =1;
    userSkillCreateDetails.userSkillCreateDetails.createUserSkillDetails.recordStatus = RECORDSTATUS.NORMAL;
    userSkillCreateDetails.userSkillCreateDetails.createUserSkillDetails.skillType = SKILLTYPE.LANGUAGES;
    userSkillCreateDetails.userSkillCreateDetails.createUserSkillDetails.userName = "testuser";
    userSkillCreateDetails.userSkillCreateDetails.createUserSkillDetails.skillLevel = SKILLLEVEL.MEDIUM;
    
    // default test
    testSubject = createTestSubject();
    testSubject.createUserSkill(userSkillCreateDetails);

    
    
    BDMUserSkillListKey userSkillListKey = new BDMUserSkillListKey();
    userSkillListKey.userSkillListKey.userSkillList.userName = "testuser";
    UserSkillDtlsList result = testSubject.getActiveUserSkills(userSkillListKey );
    final UserSkillDtls userSkillDtls = result.dtls.get(0);
    
    final BDMUserSkillPriorityList skillPriorityList = new BDMUserSkillPriorityList();
    skillPriorityList.userName = "testuser";
    BDMUserSkillPriority skillDetails = new BDMUserSkillPriority();
    
    skillDetails.userskillID = userSkillDtls.userSkillID;
    skillDetails.skillPriority = userSkillDtls.skillLevel;
    
    skillPriorityList.dtls.add(skillDetails);
    
    // default test
    testSubject.modifyListUserSkills(skillPriorityList);
  }

  @MethodRef(name = "createUserSkill",
    signature = "(QUserSkillCreateDetails;)V")
  @Test
  public void testCreateUserSkill() throws Exception {

    setupUser();
    
    BDMOrganization testSubject;
    final UserSkillCreateDetails userSkillCreateDetails = new UserSkillCreateDetails();
    userSkillCreateDetails.userSkillCreateDetails.createUserSkillDetails.userSkillID =1;
    userSkillCreateDetails.userSkillCreateDetails.createUserSkillDetails.recordStatus = RECORDSTATUS.NORMAL;
    userSkillCreateDetails.userSkillCreateDetails.createUserSkillDetails.skillType = SKILLTYPE.LANGUAGES;
    userSkillCreateDetails.userSkillCreateDetails.createUserSkillDetails.userName = "testuser";
    userSkillCreateDetails.userSkillCreateDetails.createUserSkillDetails.skillLevel = SKILLLEVEL.MEDIUM;
    
    // default test
    testSubject = createTestSubject();
    testSubject.createUserSkill(userSkillCreateDetails);
  }

  @MethodRef(name = "modifyUserSkill",
    signature = "(QUserSkillModifyDetails;)V")
  @Test
  public void testModifyUserSkill() throws Exception {

    setupUser();
    
    BDMOrganization testSubject;
    final UserSkillCreateDetails userSkillCreateDetails = new UserSkillCreateDetails();
    userSkillCreateDetails.userSkillCreateDetails.createUserSkillDetails.userSkillID =1;
    userSkillCreateDetails.userSkillCreateDetails.createUserSkillDetails.recordStatus = RECORDSTATUS.NORMAL;
    userSkillCreateDetails.userSkillCreateDetails.createUserSkillDetails.skillType = SKILLTYPE.LANGUAGES;
    userSkillCreateDetails.userSkillCreateDetails.createUserSkillDetails.userName = "testuser";
    userSkillCreateDetails.userSkillCreateDetails.createUserSkillDetails.skillLevel = SKILLLEVEL.MEDIUM;
    
    // default test
    testSubject = createTestSubject();
    testSubject.createUserSkill(userSkillCreateDetails);
    
    BDMUserSkillListKey userSkillListKey = new BDMUserSkillListKey();
    userSkillListKey.userSkillListKey.userSkillList.userName = "testuser";
    UserSkillDtlsList result = testSubject.getActiveUserSkills(userSkillListKey );
    final UserSkillDtls userSkillDtls = result.dtls.get(0);
    
    UserSkillModifyDetails userSkillModifyDetails = new UserSkillModifyDetails();
    userSkillModifyDetails.modifyUserSkillDetails.modifyUserSkillDetails.userSkillID = userSkillDtls.userSkillID;
    userSkillModifyDetails.modifyUserSkillDetails.modifyUserSkillDetails.recordStatus = RECORDSTATUS.NORMAL;
    userSkillModifyDetails.modifyUserSkillDetails.modifyUserSkillDetails.skillLevel = userSkillDtls.skillLevel;
    userSkillModifyDetails.modifyUserSkillDetails.modifyUserSkillDetails.userName = "testuser";
    userSkillModifyDetails.modifyUserSkillDetails.modifyUserSkillDetails.skillType = SKILLTYPE.LANGUAGES;
    userSkillModifyDetails.modifyUserSkillDetails.modifyUserSkillDetails.versionNo = userSkillDtls.versionNo;
    
    testSubject.modifyUserSkill(userSkillModifyDetails );
  }

  @MethodRef(name = "getActiveUserSkills",
    signature = "(QBDMUserSkillListKey;)QUserSkillDtlsList;")
  @Test
  public void testGetActiveUserSkills() throws Exception {

    setupUser();
    
    BDMOrganization testSubject;
    final UserSkillCreateDetails userSkillCreateDetails = new UserSkillCreateDetails();
    userSkillCreateDetails.userSkillCreateDetails.createUserSkillDetails.userSkillID =1;
    userSkillCreateDetails.userSkillCreateDetails.createUserSkillDetails.recordStatus = RECORDSTATUS.NORMAL;
    userSkillCreateDetails.userSkillCreateDetails.createUserSkillDetails.skillType = SKILLTYPE.LANGUAGES;
    userSkillCreateDetails.userSkillCreateDetails.createUserSkillDetails.userName = "testuser";
    userSkillCreateDetails.userSkillCreateDetails.createUserSkillDetails.skillLevel = SKILLLEVEL.MEDIUM;
    
    // default test
    testSubject = createTestSubject();
    testSubject.createUserSkill(userSkillCreateDetails);

    
    
    BDMUserSkillListKey userSkillListKey = new BDMUserSkillListKey();
    userSkillListKey.userSkillListKey.userSkillList.userName = "testuser";
    UserSkillDtlsList result = testSubject.getActiveUserSkills(userSkillListKey );
    assertTrue(result.dtls.size()>0);
    
  }

  @MethodRef(name = "cancelUserSkillsDetails",
    signature = "(QCancelUserSkillDetails;)QInformationalMsgDtlsList;")
  @Test
  public void testCancelUserSkillsDetails() throws Exception {

    setupUser();
    
    BDMOrganization testSubject;
    final UserSkillCreateDetails userSkillCreateDetails = new UserSkillCreateDetails();
    userSkillCreateDetails.userSkillCreateDetails.createUserSkillDetails.userSkillID =1;
    userSkillCreateDetails.userSkillCreateDetails.createUserSkillDetails.recordStatus = RECORDSTATUS.NORMAL;
    userSkillCreateDetails.userSkillCreateDetails.createUserSkillDetails.skillType = SKILLTYPE.LANGUAGES;
    userSkillCreateDetails.userSkillCreateDetails.createUserSkillDetails.userName = "testuser";
    userSkillCreateDetails.userSkillCreateDetails.createUserSkillDetails.skillLevel = SKILLLEVEL.NOALLOC;
    
    // default test
    testSubject = createTestSubject();
    testSubject.createUserSkill(userSkillCreateDetails);
    
    BDMUserSkillListKey userSkillListKey = new BDMUserSkillListKey();
    userSkillListKey.userSkillListKey.userSkillList.userName = "testuser";
    UserSkillDtlsList result = testSubject.getActiveUserSkills(userSkillListKey );
    final UserSkillDtls userSkillDtls = result.dtls.get(0);
    
    final CancelUserSkillDetails cancelUserSkillDetails = new CancelUserSkillDetails();
    cancelUserSkillDetails.cancelUserSkillDetails.key.userSkillID = userSkillDtls.userSkillID;
    cancelUserSkillDetails.cancelUserSkillDetails.details.recordStatus = RECORDSTATUS.CANCELLED;
    cancelUserSkillDetails.cancelUserSkillDetails.details.versionNo = userSkillDtls.versionNo;
    
    // default test
    
     testSubject.cancelUserSkillsDetails(cancelUserSkillDetails);
  }
}
