package curam.ca.gc.bdm.test.data;

import curam.ca.gc.bdm.entity.fact.BDMUserFactory;
import curam.ca.gc.bdm.entity.struct.BDMUserDtls;
import curam.citizenworkspace.codetable.IntakeClientType;
import curam.citizenworkspace.codetable.SecretQuestionType;
import curam.citizenworkspace.entity.fact.CWExternalPartyLinkFactory;
import curam.citizenworkspace.entity.intf.CWExternalPartyLink;
import curam.citizenworkspace.entity.struct.CWExternalPartyLinkDtls;
import curam.citizenworkspace.security.fact.IntakeClientFactory;
import curam.citizenworkspace.security.struct.IntakeClientDtls;
import curam.codetable.APPLICATION_CODE;
import curam.codetable.EXTERNALUSERSPARTRELTYPE;
import curam.codetable.LOCALE;
import curam.codetable.RECORDSTATUS;
import curam.core.facade.fact.UniqueIDFactory;
import curam.core.facade.intf.UniqueID;
import curam.core.fact.UsersFactory;
import curam.core.sl.entity.fact.ExternalUserFactory;
import curam.core.sl.entity.fact.ExternalUserParticipantLinkFactory;
import curam.core.sl.entity.intf.ExternalUserParticipantLink;
import curam.core.sl.entity.struct.ExternalUserDtls;
import curam.core.sl.entity.struct.ExternalUserParticipantLinkDtls;
import curam.core.struct.UsersDtls;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;

public class BDMUserTestData {

  /**
   * Creates a user in the Users and BDMUser table
   *
   * @param username
   * @param languages
   * @throws AppException
   * @throws InformationalException
   */
  public void createUser(final String username, final String languages)
    throws AppException, InformationalException {

    // create the user
    final UsersDtls dtls = new UsersDtls();
    dtls.userName = username;
    dtls.locationID = 1;
    dtls.versionNo = 1;
    dtls.defaultLocale = LOCALE.DEFAULTCODE;
    dtls.sensitivity = "1";
    dtls.statusCode = RECORDSTATUS.DEFAULTCODE;

    UsersFactory.newInstance().insert(dtls);

    // create the BDM User with languages
    final BDMUserDtls bdmUserDetails = new BDMUserDtls();
    bdmUserDetails.userName = username;
    bdmUserDetails.userLangList = languages;

    BDMUserFactory.newInstance().insert(bdmUserDetails);
  }

  /**
   * Creates a external user and links it to the external party.
   *
   * @param username
   * @param languages
   * @throws AppException
   * @throws InformationalException
   */
  public void createLinkedUser(final String username,
    final long concernRoleID) throws AppException, InformationalException {

    // 1: Create external user account.
    createExternalUser(username, APPLICATION_CODE.CITIZEN_WORKSPACE,
      "LINKEDCITIZENROLE");

    // 2: Create intake client
    createIntakeClient(username);

    // 3: Create CW external party link
    final String externalSystemID = "CORE_CURAM";
    final String externalPartyID = Long.valueOf(concernRoleID).toString();

    createCWExternalPartyLink(username, externalPartyID, externalSystemID);

    // 4: Create external user participant link.
    createExternalUserPparticipantLink(username, concernRoleID);
  }

  /**
   * Creates a user in the ExternalUser table
   *
   * @param username
   * @param languages
   * @throws AppException
   * @throws InformationalException
   */
  public void createExternalUser(final String username,
    final String applicationCode, final String roleName)
    throws AppException, InformationalException {

    // create the user
    final ExternalUserDtls externalUserDtls = new ExternalUserDtls();
    externalUserDtls.userName = username;
    externalUserDtls.accountEnabled = true;
    externalUserDtls.applicationCode = applicationCode;
    externalUserDtls.creationDate = Date.getCurrentDate();
    externalUserDtls.firstname = "Joe";
    externalUserDtls.surname = "Smith";
    externalUserDtls.fullName = "Joe Smith";
    externalUserDtls.loginDayMon = true;
    externalUserDtls.loginDayTues = true;
    externalUserDtls.loginDayWed = true;
    externalUserDtls.loginDayThurs = true;
    externalUserDtls.loginDayFri = true;
    externalUserDtls.loginRestrictions = true;
    externalUserDtls.loginFailures = 0;
    externalUserDtls.logsSincePWDChange = 0;
    externalUserDtls.password =
      "XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg=";
    externalUserDtls.pwdChangeAfterXLog = 0;
    externalUserDtls.pwdChangeEveryXDay = 0;
    externalUserDtls.roleName = roleName;
    externalUserDtls.type = "EUT1";
    externalUserDtls.upperUserName = username.toUpperCase();
    externalUserDtls.sensitivity = "1";
    externalUserDtls.statusCode = RECORDSTATUS.DEFAULTCODE;
    externalUserDtls.defaultLocale = LOCALE.DEFAULTCODE;
    externalUserDtls.versionNo = 1;

    ExternalUserFactory.newInstance().insert(externalUserDtls);
  }

  /**
   * Creates a user in the ExternalUser table
   *
   * @param username
   * @param languages
   * @throws AppException
   * @throws InformationalException
   */
  public void createExternalUser()
    throws AppException, InformationalException {

    createExternalUser("joesmith", APPLICATION_CODE.CITIZEN_WORKSPACE,
      "LINKEDCITIZENROLE");
  }

  /**
   * Creates a client in the IntakeClient table
   *
   * @param username
   * @param languages
   * @throws AppException
   * @throws InformationalException
   */
  public void createIntakeClient(final String username)
    throws AppException, InformationalException {

    // create the user
    final IntakeClientDtls intakeClientDtls = new IntakeClientDtls();
    final UniqueID uniqueIDObj = UniqueIDFactory.newInstance();
    intakeClientDtls.intakeClientID = uniqueIDObj.getNextID().uniqueID;
    intakeClientDtls.userName = username;
    intakeClientDtls.type = IntakeClientType.LINKED;
    intakeClientDtls.secretQuestionType =
      SecretQuestionType.MOTHERS_MAIDEN_NAME;
    intakeClientDtls.answer = "Nelson";
    intakeClientDtls.versionNo = 1;

    IntakeClientFactory.newInstance().insert(intakeClientDtls);
  }

  /**
   * Creates a external user party link in the CWExternalPartyLink table
   *
   * @param username
   * @param languages
   * @throws AppException
   * @throws InformationalException
   */
  public void createCWExternalPartyLink(final String username,
    final String externalPartyID, final String externalSystemID)
    throws AppException, InformationalException {

    final UniqueID uniqueIDObj = UniqueIDFactory.newInstance();
    final CWExternalPartyLink cwExternalPartyLinkObj =
      CWExternalPartyLinkFactory.newInstance();
    final CWExternalPartyLinkDtls cwExternalPartyLinkDtls =
      new CWExternalPartyLinkDtls();

    cwExternalPartyLinkDtls.cwExternalPartyLinkID =
      uniqueIDObj.getNextID().uniqueID;
    cwExternalPartyLinkDtls.cwUserName = username;
    cwExternalPartyLinkDtls.externalPartyID = externalPartyID;
    cwExternalPartyLinkDtls.externalSystemID = externalSystemID;
    cwExternalPartyLinkDtls.recordStatus = RECORDSTATUS.NORMAL;

    cwExternalPartyLinkObj.insert(cwExternalPartyLinkDtls);
  }

  /**
   * Creates a external user party link in the CWExternalPartyLink table
   *
   * @param username
   * @param languages
   * @throws AppException
   * @throws InformationalException
   */
  public void createExternalUserPparticipantLink(final String username,
    final long concernRoleID) throws AppException, InformationalException {

    final UniqueID uniqueIDObj = UniqueIDFactory.newInstance();
    final ExternalUserParticipantLink externalUserPtcptLinkObj =
      ExternalUserParticipantLinkFactory.newInstance();
    final ExternalUserParticipantLinkDtls externalUserPtcptLinkDtls =
      new ExternalUserParticipantLinkDtls();

    externalUserPtcptLinkDtls.linkID = uniqueIDObj.getNextID().uniqueID;
    externalUserPtcptLinkDtls.userName = username;
    externalUserPtcptLinkDtls.participantRoleID = concernRoleID;
    externalUserPtcptLinkDtls.extUserPartRelType =
      EXTERNALUSERSPARTRELTYPE.PERSON;
    externalUserPtcptLinkDtls.versionNo = 1;
    externalUserPtcptLinkObj.insert(externalUserPtcptLinkDtls);
  }

}
