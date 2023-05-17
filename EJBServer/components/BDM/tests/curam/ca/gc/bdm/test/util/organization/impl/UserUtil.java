package curam.ca.gc.bdm.test.util.organization.impl;

import curam.ca.gc.bdm.entity.fact.BDMUserFactory;
import curam.ca.gc.bdm.entity.struct.BDMUserDtls;
import curam.codetable.LOCALE;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.UsersFactory;
import curam.core.sl.entity.fact.UserSkillFactory;
import curam.core.sl.entity.intf.UserSkill;
import curam.core.sl.entity.struct.UserSkillDtls;
import curam.core.sl.entity.struct.UserSkillKey;
import curam.core.struct.UsersDtls;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class UserUtil {

  /**
   * Creates a user in the Users and BDMUser table
   *
   * @param username
   * @param languages
   * @throws AppException
   * @throws InformationalException
   */
  public static void createUser(final String username, final String languages)
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
   * Adds a skill with a given level for a given user
   *
   * @param username
   * @param skill
   * @param priority
   * @param status
   * @throws AppException
   * @throws InformationalException
   */
  public static void addUserSkill(final String username, final String skill,
    final String priority, final String status)
    throws AppException, InformationalException {

    final UserSkill userSkillObj = UserSkillFactory.newInstance();

    final UserSkillDtls dtls = new UserSkillDtls();
    dtls.userName = username;
    dtls.skillType = skill;
    dtls.skillLevel = priority;

    userSkillObj.insert(dtls);

    // OOTB sets status automatically on insert. If we want a different status
    // than RST1, we have to modify the record
    if (!status.equals(RECORDSTATUS.NORMAL)) {

      final UserSkillKey key = new UserSkillKey();
      key.userSkillID = dtls.userSkillID;

      dtls.recordStatus = status;
      userSkillObj.modify(key, dtls);
    }

  }
}
