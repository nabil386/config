package curam.ca.gc.bdm.sl.organization.impl;

import curam.ca.gc.bdm.entity.fact.BDMUserFactory;
import curam.ca.gc.bdm.entity.intf.BDMUser;
import curam.ca.gc.bdm.entity.struct.BDMUserDtls;
import curam.ca.gc.bdm.entity.struct.BDMUserKey;
import curam.ca.gc.bdm.facade.organization.struct.BDMCreateAndAssignUser;
import curam.ca.gc.bdm.facade.organization.struct.BDMCreateUserDetails;
import curam.ca.gc.bdm.facade.organization.struct.BDMModifyUserDetails;
import curam.ca.gc.bdm.facade.organization.struct.BDMReadUserDetails;
import curam.core.facade.struct.ReadUserDetails_fo;
import curam.core.facade.struct.ReadUserKey;
import curam.core.fact.AdminUserFactory;
import curam.core.impl.EnvVars;
import curam.util.administration.struct.ExtendedUsersInfoDetails;
import curam.util.administration.struct.ExtendedUsersInfoLoginID;
import curam.util.administration.struct.ExtendedUsersInfoUserName;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.RecordNotFoundException;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;

public class BDMMaintainUser
  extends curam.ca.gc.bdm.sl.organization.base.BDMMaintainUser {

  @Override
  public void createUser(final BDMCreateUserDetails details)
    throws AppException, InformationalException {

    details.createUserDetails.userDetails.alternateLoginEnabled =
      Boolean.valueOf(Configuration
        .getProperty(EnvVars.BDM_ENV_ALTERNATE_LOGIN_ID_ENABLED));

    // Create the user
    AdminUserFactory.newInstance()
      .insert(details.createUserDetails.userDetails);

    final BDMUserDtls bdmUserDtls = new BDMUserDtls();

    bdmUserDtls.userName = details.createUserDetails.userDetails.userName;
    bdmUserDtls.userLangList = details.userLangList;
    bdmUserDtls.samAccountName = details.samAccountName;

    BDMUserFactory.newInstance().insert(bdmUserDtls);

    if (details.createUserDetails.userDetails.alternateLoginEnabled) {
      final curam.util.administration.intf.SecurityAdmin securityAdminObj =
        curam.util.administration.fact.SecurityAdminFactory.newInstance();

      final ExtendedUsersInfoDetails extendedUsersInfoDetails =
        new ExtendedUsersInfoDetails();

      extendedUsersInfoDetails.loginID = details.createUserDetails.loginId;
      extendedUsersInfoDetails.userName =
        details.createUserDetails.userDetails.userName;
      extendedUsersInfoDetails.upperLoginID =
        details.createUserDetails.loginId.toUpperCase();
      securityAdminObj.addLoginID(extendedUsersInfoDetails);

    }

  }

  @Override
  public BDMReadUserDetails readBDMUser(final ReadUserKey key)
    throws AppException, InformationalException {

    final BDMReadUserDetails bdmReadUserDetails = new BDMReadUserDetails();

    // Details to be returned
    final ReadUserDetails_fo readUserDetails = new ReadUserDetails_fo();

    // Read the users homepage details
    readUserDetails.userDetails =
      AdminUserFactory.newInstance().read(key.userKeyStruct);

    readUserDetails.userDetails.alternateLoginEnabled = Boolean.valueOf(
      Configuration.getProperty(EnvVars.BDM_ENV_ALTERNATE_LOGIN_ID_ENABLED));

    if (readUserDetails.userDetails.alternateLoginEnabled) {
      final curam.util.administration.intf.SecurityAdmin securityAdminObj =
        curam.util.administration.fact.SecurityAdminFactory.newInstance();
      ExtendedUsersInfoLoginID extendedUsersInfoLoginID =
        new ExtendedUsersInfoLoginID();
      final ExtendedUsersInfoUserName extendedUsersInfoUserName =
        new ExtendedUsersInfoUserName();

      extendedUsersInfoUserName.userName = key.userKeyStruct.userName;

      extendedUsersInfoLoginID =
        securityAdminObj.readLoginID(extendedUsersInfoUserName);

      readUserDetails.userDetails.loginID = extendedUsersInfoLoginID.loginID;
    }

    bdmReadUserDetails.readUserDtls = readUserDetails;

    final BDMUserKey bdmUserKey = new BDMUserKey();

    bdmUserKey.userName = key.userKeyStruct.userName;

    try {

      final BDMUserDtls bdmUserDtls =
        BDMUserFactory.newInstance().read(bdmUserKey);

      bdmReadUserDetails.userLangList = bdmUserDtls.userLangList;
      bdmReadUserDetails.samAccountName = bdmUserDtls.samAccountName;

    } catch (final RecordNotFoundException rne) {
      // do nothing for existing users without the lang skill
    }

    // Return details
    return bdmReadUserDetails;

  }

  @Override
  public void modifyUserLang(final BDMModifyUserDetails details)
    throws AppException, InformationalException {

    final BDMUserKey bdmUserKey = new BDMUserKey();
    BDMUserDtls bdmUserDtls = new BDMUserDtls();
    final BDMUser bdmUser = BDMUserFactory.newInstance();

    bdmUserKey.userName = details.modifyUserdtls.userDetails.userName;

    try {
      // START: Bug 122035: Modifying a User removes samAccountName
      bdmUserDtls = bdmUser.read(bdmUserKey);

      if (StringUtil.isNullOrEmpty(details.samAccountName)) {
        details.samAccountName = bdmUserDtls.samAccountName;
      }

      bdmUserDtls.userName = details.modifyUserdtls.userDetails.userName;
      bdmUserDtls.userLangList = details.userLangList;
      bdmUserDtls.samAccountName = details.samAccountName;
      // END: Bug 122035

      bdmUser.modify(bdmUserKey, bdmUserDtls);

    } catch (final RecordNotFoundException rne) {
      BDMUserFactory.newInstance().insert(bdmUserDtls);
    }

  }

  @Override
  public void insertUserLang(final BDMCreateAndAssignUser details)
    throws AppException, InformationalException {

    final BDMUserDtls bdmUserDtls = new BDMUserDtls();

    bdmUserDtls.userName = details.createUserDtls.createUserDetails.userName;

    bdmUserDtls.userLangList = details.userLangList;

    BDMUserFactory.newInstance().insert(bdmUserDtls);

  }

}
