package curam.ca.gc.bdm.facade.organization.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.entity.fact.BDMUserFactory;
import curam.ca.gc.bdm.entity.struct.BDMSamAccountNameKey;
import curam.ca.gc.bdm.facade.organization.struct.BDMAlternateLoginEnabled;
import curam.ca.gc.bdm.facade.organization.struct.BDMCreateAndAssignUser;
import curam.ca.gc.bdm.facade.organization.struct.BDMCreateUserDetails;
import curam.ca.gc.bdm.facade.organization.struct.BDMModifyUserDetails;
import curam.ca.gc.bdm.facade.organization.struct.BDMReadOrgUserHomePageDetails;
import curam.ca.gc.bdm.facade.organization.struct.BDMReadUserDetails;
import curam.ca.gc.bdm.facade.organization.struct.BDMUserSkillListKey;
import curam.ca.gc.bdm.facade.organization.struct.BDMUserSkillPriority;
import curam.ca.gc.bdm.facade.organization.struct.BDMUserSkillPriorityList;
import curam.ca.gc.bdm.facade.organization.struct.UserLanguageDetails;
import curam.ca.gc.bdm.facade.organization.struct.UserLanguageDetailsList;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMUSERSKILLS;
import curam.ca.gc.bdm.sl.organization.fact.BDMMaintainUserFactory;
import curam.codetable.SKILLLEVEL;
import curam.core.facade.struct.AlternateLoginEnabled;
import curam.core.facade.struct.CancelUserSkillDetails;
import curam.core.facade.struct.CreateUserDetails;
import curam.core.facade.struct.OrganizationUserContextDescriptionKey;
import curam.core.facade.struct.ReadUserHomePageKey;
import curam.core.facade.struct.ReadUserKey;
import curam.core.facade.struct.RuleSetNodeKey;
import curam.core.facade.struct.UserForPositionDetails;
import curam.core.facade.struct.UserSkillCreateDetails;
import curam.core.facade.struct.UserSkillModifyDetails;
import curam.core.impl.EnvVars;
import curam.core.sl.entity.struct.UserSkillDtls;
import curam.core.sl.entity.struct.UserSkillDtlsList;
import curam.core.sl.fact.UserSkillFactory;
import curam.core.sl.intf.UserSkill;
import curam.core.sl.struct.ModifyUserSkillDetails;
import curam.core.sl.struct.ReadUserSkillDetails;
import curam.core.sl.struct.ReadUserSkillKey;
import curam.core.struct.InformationalMsgDtlsList;
import curam.core.struct.UsersDtls;
import curam.util.administration.fact.SecurityAdminFactory;
import curam.util.administration.intf.SecurityAdmin;
import curam.util.administration.struct.ExtendedUsersInfoLoginID;
import curam.util.administration.struct.ExtendedUsersInfoUserName;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.exception.LocalisableString;
import curam.util.exception.RecordNotFoundException;
import curam.util.internal.security.fact.ExtendedUsersInfoFactory;
import curam.util.internal.security.struct.ExtendedUsersInfoKey;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.NotFoundIndicator;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.logging.log4j.Level;

/**
 *
 * Class with methods to create user, user skill etc.
 *
 */
public class BDMOrganization
  extends curam.ca.gc.bdm.facade.organization.base.BDMOrganization {

  @Inject
  public curam.ca.gc.bdm.sl.organization.userskill.intf.BDMUserSkill bdmUserSkillObj;

  public BDMOrganization() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Creates a user record in BDM system.
   *
   * @param details
   * @throws AppException, InformationalException
   *
   */
  @Override
  public void createUser(final BDMCreateUserDetails details)
    throws AppException, InformationalException {

    // BEGIN- Task-11387 SSO user with Azure Active directory
    final boolean isAlternateLoginEnabled = Boolean.valueOf(
      Configuration.getProperty(EnvVars.BDM_ENV_ALTERNATE_LOGIN_ID_ENABLED));

    // validate mandatory fields for create user
    validateMandatoryFieldsForCreateUser(isAlternateLoginEnabled,
      details.createUserDetails.userDetails.userName,
      details.createUserDetails.loginId,
      details.createUserDetails.userDetails.password,
      details.createUserDetails.userDetails.passwordConfirm,
      details.samAccountName);

    // If SSO is enabled
    if (isAlternateLoginEnabled) {
      // assign Azure Active Directory ID to username
      final StringTokenizer st =
        new StringTokenizer(details.createUserDetails.loginId, "@");
      details.createUserDetails.userDetails.userName = st.nextToken();

      // If SSO login is enabled, password is not captured
      // generate a random password, as it is required.
      // This password will not be used to login

      details.createUserDetails.userDetails.password =
        generateStrongRandomPassword();
      details.createUserDetails.userDetails.passwordConfirm =
        details.createUserDetails.userDetails.password;
    }
    // END- Task-11387 SSO user with Azure Active directory

    final UsersDtls dtls = new UsersDtls();

    validateUserName(dtls.assign(details.createUserDetails.userDetails));

    BDMMaintainUserFactory.newInstance().createUser(details);

    // clearing the cache after creating the new user so no need to restart the
    // server
    if (Configuration.runningInAppServer()) {
      try {
        TransactionInfo.getTransactionManager().commit();
        TransactionInfo.getTransactionManager().begin();
      } catch (final Exception e) {
        Trace.kTopLevelLogger.log(Level.ERROR, e.getLocalizedMessage());
      }
    }

    // reload cache business process object
    final curam.util.administration.intf.CacheAdmin reloadCacheObj =
      curam.util.administration.fact.CacheAdminFactory.newInstance();

    // reload security cache
    reloadCacheObj.reloadSecurityCache();

  }

  /**
   * This method validates the user entered fields for creating a user
   *
   * @param userName
   * @param loginID
   * @param password
   * @param passwordConfirm
   * @throws AppException
   * @throws InformationalException
   */
  private void validateMandatoryFieldsForCreateUser(
    final boolean isAlternateLoginEnabled, final String userName,
    final String ssoUsername, final String password,
    final String passwordConfirm, final String samAccountName)
    throws AppException, InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    // if SSO login enabled, validate Azure Active Directory login ID
    if (isAlternateLoginEnabled) {
      // validate- user entered login ID
      if (ssoUsername.isEmpty()) {

        final LocalisableString errorMessage =
          new LocalisableString(BDMUSERSKILLS.ERR_USER_LOGIN_ID_MISSING);

        informationalManager.addInformationalMsg(errorMessage, "",
          InformationalElement.InformationalType.kError);

      } else {

        final StringTokenizer st = new StringTokenizer(ssoUsername, "@");
        final String userID = st.nextToken();

        if (userID.length() > BDMConstants.kUSERNAME_LENGTH_LIMIT) {

          final LocalisableString errorMessage =
            new LocalisableString(BDMUSERSKILLS.ERR_USER_LOGIN_ID_SIZE);

          informationalManager.addInformationalMsg(errorMessage, "",
            InformationalElement.InformationalType.kError);
        }

        // validate- Azure Active Directory Username must be unique in Curam
        final NotFoundIndicator notFound = new NotFoundIndicator();
        final ExtendedUsersInfoKey usersKey = new ExtendedUsersInfoKey();
        usersKey.userName = ssoUsername;
        ExtendedUsersInfoFactory.newInstance().read(notFound, usersKey);

        if (!notFound.isNotFound()) {
          final LocalisableString errorMessage =
            new LocalisableString(BDMUSERSKILLS.ERR_USER_LOGIN_ID_NOT_UNIQUE);

          informationalManager.addInformationalMsg(errorMessage, "",
            InformationalElement.InformationalType.kError);
        }
        // validation for unique SAMAccountName
        final BDMSamAccountNameKey bdmSamAccountNameKey =
          new BDMSamAccountNameKey();

        bdmSamAccountNameKey.samAccountName = samAccountName;

        final NotFoundIndicator nfIndicator = new NotFoundIndicator();

        BDMUserFactory.newInstance().readBySAMAccountName(nfIndicator,
          bdmSamAccountNameKey);

        if (!nfIndicator.isNotFound()) {
          final LocalisableString errorMessage = new LocalisableString(
            BDMUSERSKILLS.ERR_SAM_ACCOUNT_NAME_NOT_UNIQUE);

          informationalManager.addInformationalMsg(errorMessage, "",
            InformationalElement.InformationalType.kError);
        }
      }
    } else { // SSO login is disabled

      if (userName.isEmpty()) {

        final LocalisableString errorMessage =
          new LocalisableString(BDMUSERSKILLS.ERR_USER_USERNAME_MISSING);

        informationalManager.addInformationalMsg(errorMessage, "",
          InformationalElement.InformationalType.kError);
      }
      if (password.isEmpty()) {

        final LocalisableString errorMessage =
          new LocalisableString(BDMUSERSKILLS.ERR_USER_PASSWORD_MISSING);

        informationalManager.addInformationalMsg(errorMessage, "",
          InformationalElement.InformationalType.kError);
      }
      if (passwordConfirm.isEmpty()) {

        final LocalisableString errorMessage =
          new LocalisableString(BDMUSERSKILLS.ERR_USER_CONFPASSWORD_MISSING);

        informationalManager.addInformationalMsg(errorMessage, "",
          InformationalElement.InformationalType.kError);
      }
    }
    informationalManager.failOperation();
  }

  /**
   * This method returns a random password String
   *
   * @return String
   */
  private String generateStrongRandomPassword() {

    final String upperCaseLetters =
      RandomStringUtils.random(2, 65, 90, true, true);
    final String lowerCaseLetters =
      RandomStringUtils.random(2, 97, 122, true, true);
    final String numbers = RandomStringUtils.randomNumeric(2);
    final String specialChar =
      RandomStringUtils.random(2, 33, 47, false, false);
    final String totalChars = RandomStringUtils.randomAlphanumeric(2);
    final String combinedChars = upperCaseLetters.concat(lowerCaseLetters)
      .concat(numbers).concat(specialChar).concat(totalChars);
    final List<Character> pwdChars = combinedChars.chars()
      .mapToObj(c -> (char) c).collect(Collectors.toList());
    Collections.shuffle(pwdChars);
    final String password = pwdChars.stream().collect(StringBuilder::new,
      StringBuilder::append, StringBuilder::append).toString();
    return password;
  }

  /**
   * Check if alternate login is enabled and returns BDM language list.
   *
   * @throws AppException, InformationalException
   *
   */
  @Override
  public BDMAlternateLoginEnabled readBDMAlternateLoginEnabled()
    throws AppException, InformationalException {

    final BDMAlternateLoginEnabled result = new BDMAlternateLoginEnabled();

    final AlternateLoginEnabled alternateLoginEnabled =
      new AlternateLoginEnabled();
    alternateLoginEnabled.alternateLoginEnabled = Boolean.valueOf(
      Configuration.getProperty(EnvVars.BDM_ENV_ALTERNATE_LOGIN_ID_ENABLED));
    result.dtls = alternateLoginEnabled;

    final LinkedHashMap<String, String> allCodes =
      CodeTable.getAllEnabledItems(BDMLANGUAGE.TABLENAME,
        TransactionInfo.getProgramLocale());

    allCodes.forEach((key, value) -> {
      final UserLanguageDetails uld = new UserLanguageDetails();

      uld.langCode = key;
      uld.langDesc = value;
      result.userLangList.dtls.add(uld);
    });

    return result;
  }

  /**
   * Read BDM user details.
   *
   * @param key
   * @return BDMReadUserDetails
   * @throws AppException, InformationalException
   *
   */
  @Override
  public BDMReadUserDetails readBDMUser(final ReadUserKey key)
    throws AppException, InformationalException {

    final BDMReadUserDetails bdmReadUserDetails =
      BDMMaintainUserFactory.newInstance().readBDMUser(key);

    // Read the users context description
    final OrganizationUserContextDescriptionKey organizationUserContextDescriptionKey =
      new OrganizationUserContextDescriptionKey();

    organizationUserContextDescriptionKey.userName =
      key.userKeyStruct.userName;
    bdmReadUserDetails.readUserDtls.userContextDescriptionDetails =
      readUserContextDescription(organizationUserContextDescriptionKey);

    final LinkedHashMap<String, String> allCodes =
      CodeTable.getAllEnabledItems(BDMLANGUAGE.TABLENAME,
        TransactionInfo.getProgramLocale());

    allCodes.forEach((k, v) -> {
      final UserLanguageDetails uld = new UserLanguageDetails();

      uld.langCode = k;
      uld.langDesc = v;
      bdmReadUserDetails.allLangList.dtls.add(uld);
    });

    return bdmReadUserDetails;
  }

  /**
   * Modify BDM user details.
   *
   * @param details
   * @throws AppException, InformationalException
   *
   */
  @Override
  public void modifyUser(final BDMModifyUserDetails details)
    throws AppException, InformationalException {

    super.modifyUser(details.modifyUserdtls);

    BDMMaintainUserFactory.newInstance().modifyUserLang(details);

  }

  /**
   * Read BDM user language list.
   *
   * @return UserLanguageDetailsList
   * @throws AppException, InformationalException
   *
   */
  @Override
  public UserLanguageDetailsList readUserLangList()
    throws AppException, InformationalException {

    final UserLanguageDetailsList userLangList =
      new UserLanguageDetailsList();

    final LinkedHashMap<String, String> allCodes =
      CodeTable.getAllEnabledItems(BDMLANGUAGE.TABLENAME,
        TransactionInfo.getProgramLocale());

    allCodes.forEach((k, v) -> {
      final UserLanguageDetails uld = new UserLanguageDetails();

      uld.langCode = k;
      uld.langDesc = v;
      userLangList.dtls.add(uld);
    });

    return userLangList;
  }

  /**
   * Creates new user and assigns the same user to a position.
   *
   * @param details
   * contains all the details of the user and position
   * @throws AppException, InformationalException
   */
  @Override
  public void
    createAndAssignUserForPosition(final BDMCreateAndAssignUser details)
      throws AppException, InformationalException {

    // BEGIN- Task-11387 SSO user with Azure Active directory

    final CreateUserDetails createUserDetails = new CreateUserDetails();
    createUserDetails.userDetails = details.createUserDtls.createUserDetails;
    final BDMCreateUserDetails bdmCreateUserDetails =
      new BDMCreateUserDetails();
    bdmCreateUserDetails.createUserDetails.loginId =
      details.createUserDtls.createUserDetails.loginID;
    bdmCreateUserDetails.createUserDetails.userDetails =
      details.createUserDtls.createUserDetails;
    bdmCreateUserDetails.userLangList = details.userLangList;
    bdmCreateUserDetails.samAccountName = details.samAccountName;

    // call custom create user, includes validations
    createUser(bdmCreateUserDetails);

    final UserForPositionDetails userForPositionDetails =
      details.createUserDtls.assignDetails;
    userForPositionDetails.positionHolderLinkDetails.positionHolderLinkDtls.userName =
      details.createUserDtls.createUserDetails.userName;
    final RuleSetNodeKey ruleSetNodeKey = new RuleSetNodeKey();
    ruleSetNodeKey.ruleSetID = String.valueOf(
      details.createUserDtls.assignDetails.positionHolderLinkDetails.positionHolderLinkDtls.organisationStructureID);
    ruleSetNodeKey.nodeID =
      details.createUserDtls.assignDetails.positionHolderLinkDetails.positionHolderLinkDtls.positionID;
    addUserForPosition(userForPositionDetails);
    final String userSelectionPath =
      setUserSelectionPath(details.createUserDtls);
    OrganisationpostUpdate(ruleSetNodeKey, userSelectionPath);
    // END- Task-11387 SSO user with Azure Active directory

  }

  /**
   * Retrieve the homepage details for a user.
   *
   * @param key Identifier for a user
   * @return BDMReadOrgUserHomePageDetails details for a user
   * @throws AppException, InformationalException
   */
  @Override
  public BDMReadOrgUserHomePageDetails
    readBDMOrganizationUserHomePage(final ReadUserHomePageKey key)
      throws AppException, InformationalException {

    final BDMReadOrgUserHomePageDetails bdmReadOrgUserHPDetails =
      new BDMReadOrgUserHomePageDetails();

    bdmReadOrgUserHPDetails.ReadOrgUsrHPDtls =
      super.readOrganizationUserHomePage(key);

    bdmReadOrgUserHPDetails.ReadOrgUsrHPDtls.organizationUserDetails.alternateLoginEnabled =
      Boolean.valueOf(Configuration
        .getProperty(EnvVars.BDM_ENV_ALTERNATE_LOGIN_ID_ENABLED));

    if (bdmReadOrgUserHPDetails.ReadOrgUsrHPDtls.organizationUserDetails.alternateLoginEnabled) {
      final SecurityAdmin securityAdminObj =
        SecurityAdminFactory.newInstance();
      final ExtendedUsersInfoUserName extendedUsersInfoUserName =
        new ExtendedUsersInfoUserName();
      ExtendedUsersInfoLoginID extendedUsersInfoLoginID =
        new ExtendedUsersInfoLoginID();
      extendedUsersInfoUserName.userName = key.userKeyStruct.userName;
      extendedUsersInfoLoginID =
        securityAdminObj.readLoginID(extendedUsersInfoUserName);
      bdmReadOrgUserHPDetails.ReadOrgUsrHPDtls.alternateLoginID.loginId =
        extendedUsersInfoLoginID.loginID;
    }

    final LinkedHashMap<String, String> allCodes =
      CodeTable.getAllEnabledItems(BDMLANGUAGE.TABLENAME,
        TransactionInfo.getProgramLocale());

    allCodes.forEach((k, v) -> {
      final UserLanguageDetails uld = new UserLanguageDetails();

      uld.langCode = k;
      uld.langDesc = v;
      bdmReadOrgUserHPDetails.allLangList.dtls.add(uld);
    });

    final ReadUserKey rdk = new ReadUserKey();

    rdk.userKeyStruct.userName = key.userKeyStruct.userName;

    String userLangList = null;

    try {

      final BDMReadUserDetails bdmReadUserDetails =
        BDMMaintainUserFactory.newInstance().readBDMUser(rdk);

      userLangList = bdmReadUserDetails.userLangList;
      bdmReadOrgUserHPDetails.samAccountName =
        bdmReadUserDetails.samAccountName;
    } catch (final RecordNotFoundException rne) {
      // do nothing
    }

    if (userLangList != null) {
      final StringTokenizer stringTokenizer =
        new StringTokenizer(userLangList, "\t");

      while (stringTokenizer.hasMoreTokens()) {
        final String code = stringTokenizer.nextToken();

        if (bdmReadOrgUserHPDetails.userLangList.length() > 0) {
          bdmReadOrgUserHPDetails.userLangList += ", ";
        }

        bdmReadOrgUserHPDetails.userLangList +=
          CodeTable.getOneItemForUserLocale(BDMLANGUAGE.TABLENAME, code);

      }
    }
    return bdmReadOrgUserHPDetails;
  }

  /**
   * Modify user skill from list
   *
   * @param skillPriorityList
   * @throws AppException, InformationalException
   */
  @Override
  public void
    modifyListUserSkills(final BDMUserSkillPriorityList skillPriorityList)
      throws AppException, InformationalException {

    // validates the mass modify priorities

    bdmUserSkillObj.validateMassModifyUserSkills(skillPriorityList);

    final UserSkill userSkillObj = UserSkillFactory.newInstance();
    UserSkillDtls userSkillDtls = null;
    ReadUserSkillDetails readUserSkillDetails = null;
    ReadUserSkillKey readUserSkillKey = null;
    ModifyUserSkillDetails mUserSkillDetails = null;

    for (final BDMUserSkillPriority newSkillPriority : skillPriorityList.dtls
      .items()) {

      // Read before modify
      readUserSkillKey = new ReadUserSkillKey();
      readUserSkillKey.userSkillID = newSkillPriority.userskillID;

      readUserSkillDetails = userSkillObj.read(readUserSkillKey);

      // Modify the details.
      userSkillDtls = new UserSkillDtls();
      userSkillDtls = readUserSkillDetails.details;
      userSkillDtls.skillLevel = newSkillPriority.skillPriority;

      mUserSkillDetails = new ModifyUserSkillDetails();
      mUserSkillDetails.modifyUserSkillDetails = userSkillDtls;

      userSkillObj.modify(mUserSkillDetails);
    }
  }

  /**
   * Create a user skill
   *
   * @param userSkillCreateDetails
   * @throws AppException, InformationalException
   */
  @Override
  public void
    createUserSkill(final UserSkillCreateDetails userSkillCreateDetails)
      throws AppException, InformationalException {

    bdmUserSkillObj.validateUserSkill(
      userSkillCreateDetails.userSkillCreateDetails.createUserSkillDetails);

    super.createUserSkill(userSkillCreateDetails);
  }

  /**
   * Modify a user skill
   *
   * @param userSkillCreateDetails
   * @throws AppException, InformationalException
   */
  @Override
  public void
    modifyUserSkill(final UserSkillModifyDetails userSkillModifyDetails)
      throws AppException, InformationalException {

    bdmUserSkillObj.modifyUserSkill(
      userSkillModifyDetails.modifyUserSkillDetails.modifyUserSkillDetails);

    super.modifyUserSkill(userSkillModifyDetails);

  }

  /**
   * Returns a list of user skill
   *
   * @param userSkillListKey
   * @throws AppException, InformationalException
   */
  @Override
  public UserSkillDtlsList
    getActiveUserSkills(final BDMUserSkillListKey userSkillListKey)
      throws AppException, InformationalException {

    final UserSkillDtlsList activeUserSkills =
      bdmUserSkillObj.getActiveUserSkills(userSkillListKey.userSkillListKey);

    if (userSkillListKey.isResetInd) {
      for (final UserSkillDtls skill : activeUserSkills.dtls.items()) {
        skill.skillLevel = SKILLLEVEL.NOALLOC;
      }
    }

    return activeUserSkills;
  }

  /**
   * Returns a list of user skill
   *
   * @param cancelUserSkillDetails
   * @return InformationalMsgDtlsList
   * @throws AppException, InformationalException
   */
  @Override
  public InformationalMsgDtlsList cancelUserSkillsDetails(
    final CancelUserSkillDetails cancelUserSkillDetails)
    throws AppException, InformationalException {

    bdmUserSkillObj.cancel(cancelUserSkillDetails.cancelUserSkillDetails);

    // super method includes logic to handle skill type of "language" which has
    // been omitted here as the language skill type does not exist in this
    // implementation
    return new InformationalMsgDtlsList();
  }

}
