package curam.ca.gc.bdm.sl.organization.userskill.impl;

import curam.ca.gc.bdm.entity.struct.BDMUserStatusKey;
import curam.ca.gc.bdm.facade.organization.struct.BDMUserSkillPriority;
import curam.ca.gc.bdm.facade.organization.struct.BDMUserSkillPriorityList;
import curam.ca.gc.bdm.message.BDMUSERSKILLS;
import curam.codetable.RECORDSTATUS;
import curam.codetable.SKILLLEVEL;
import curam.core.facade.struct.UserSkillListKey;
import curam.core.impl.CuramConst;
import curam.core.sl.entity.fact.UserSkillFactory;
import curam.core.sl.entity.struct.UserSkillDtls;
import curam.core.sl.entity.struct.UserSkillDtlsList;
import curam.core.sl.entity.struct.UserSkillKey;
import curam.core.sl.entity.struct.UserSkillReadmultiKey;
import curam.core.sl.struct.UserSkillCancelDetails;
import curam.core.sl.struct.UserSkillDetailsList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.transaction.TransactionInfo;
import java.util.function.Predicate;

public class BDMUserSkill
  implements curam.ca.gc.bdm.sl.organization.userskill.intf.BDMUserSkill {

  /**
   * Validate on create/modify that if it is primary/secondary/tertiary, no
   * existing active skills exist that have the same priority. Also verifies
   * that higher priority skills exist before a lower priority skill is
   * added/modified.
   */
  @Override
  public void validateUserSkill(final UserSkillDtls userSkillDtls)
    throws AppException, InformationalException {

    final boolean hasPrimary;
    final boolean hasSecondary;
    final boolean hasTertiary;

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    final UserSkillReadmultiKey userKey = new UserSkillReadmultiKey();
    userKey.userName = userSkillDtls.userName;
    final UserSkillDtlsList userSkillDtlsList =
      UserSkillFactory.newInstance().searchByUser(userKey);

    // remove cancelled records
    Predicate<UserSkillDtls> predicate =
      dtls -> !dtls.recordStatus.equals(RECORDSTATUS.DEFAULTCODE);
    userSkillDtlsList.dtls.removeIf(predicate);

    predicate = dtls -> dtls.skillLevel.equals(SKILLLEVEL.PRIMARY);
    hasPrimary = userSkillDtlsList.dtls.removeIf(predicate);

    predicate = dtls -> dtls.skillLevel.equals(SKILLLEVEL.SECONDARY);
    hasSecondary = userSkillDtlsList.dtls.removeIf(predicate);

    predicate = dtls -> dtls.skillLevel.equals(SKILLLEVEL.TERTIARY);
    hasTertiary = userSkillDtlsList.dtls.removeIf(predicate);

    if (userSkillDtls.skillLevel.equals(SKILLLEVEL.PRIMARY) && hasPrimary) {

      informationalManager.addInformationalMsg(
        new AppException(BDMUSERSKILLS.ERR_MULTIPLE_SAME_PRIORITIES),
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);

    } else if (userSkillDtls.skillLevel.equals(SKILLLEVEL.SECONDARY)) {

      if (hasSecondary) {
        informationalManager.addInformationalMsg(
          new AppException(BDMUSERSKILLS.ERR_MULTIPLE_SAME_PRIORITIES),
          CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
      }

      if (!hasPrimary) {
        informationalManager.addInformationalMsg(
          new AppException(BDMUSERSKILLS.ERR_NO_PRIMARY_PRIORITY),
          CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
      }

    }

    else if (userSkillDtls.skillLevel.equals(SKILLLEVEL.TERTIARY)) {

      if (hasTertiary) {
        informationalManager.addInformationalMsg(
          new AppException(BDMUSERSKILLS.ERR_MULTIPLE_SAME_PRIORITIES),
          CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
      }

      if (!hasSecondary) {
        informationalManager.addInformationalMsg(
          new AppException(BDMUSERSKILLS.ERR_NO_SECONDARY_PRIORITY),
          CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
      }

      if (!hasPrimary) {
        informationalManager.addInformationalMsg(
          new AppException(BDMUSERSKILLS.ERR_NO_PRIMARY_PRIORITY),
          CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
      }

    }

    if (informationalManager.operationHasInformationals()) {
      informationalManager.failOperation();
    }

  }

  /**
   * Adds the existing skill priority to the modified user skill
   */
  @Override
  public void modifyUserSkill(final UserSkillDtls userSkillDtls)
    throws AppException, InformationalException {

    final UserSkillKey key = new UserSkillKey();
    key.userSkillID = userSkillDtls.userSkillID;
    final UserSkillDtls oldUserSkillDtls =
      UserSkillFactory.newInstance().read(key);

    userSkillDtls.skillLevel = oldUserSkillDtls.skillLevel;

  }

  /**
   * Called when updating multiple skill priorities. Verifies that there is only
   * 1 primary, secondary, and tertiary skill each. Verifies that if secondary
   * skill exists, a primary skill exists. Verifies that if tertiary skill
   * exists, primary and secondary skills exist.
   */
  @Override
  public void validateMassModifyUserSkills(
    final BDMUserSkillPriorityList skillPriorityList)
    throws InformationalException {

    int primary = 0;
    int secondary = 0;
    int tertiary = 0;

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    // count how many primary, secondary, and tertiary skills exist
    for (final BDMUserSkillPriority skillPriority : skillPriorityList.dtls) {
      if (skillPriority.skillPriority.equals(SKILLLEVEL.PRIMARY)) {
        primary++;
      } else if (skillPriority.skillPriority.equals(SKILLLEVEL.SECONDARY)) {
        secondary++;
      } else if (skillPriority.skillPriority.equals(SKILLLEVEL.TERTIARY)) {
        tertiary++;
      }
    }

    // validations
    if (primary > 1 || secondary > 1 || tertiary > 1) {
      informationalManager.addInformationalMsg(
        new AppException(BDMUSERSKILLS.ERR_MULTIPLE_SAME_PRIORITIES),
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
    }
    if ((secondary > 0 || tertiary > 0) && primary == 0) {
      informationalManager.addInformationalMsg(
        new AppException(BDMUSERSKILLS.ERR_NO_PRIMARY_PRIORITY),
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
    }
    if (tertiary > 0 && secondary == 0) {
      informationalManager.addInformationalMsg(
        new AppException(BDMUSERSKILLS.ERR_NO_SECONDARY_PRIORITY),
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
    }

    if (informationalManager.operationHasInformationals()) {
      informationalManager.failOperation();
    }

  }

  /**
   * Gets a user's list of skills that are currently active
   */
  @Override
  public UserSkillDtlsList
    getActiveUserSkills(final UserSkillListKey userSkillListKey)
      throws AppException, InformationalException {

    final UserSkillDetailsList userSkillDetailsList =
      new UserSkillDetailsList();

    final BDMUserStatusKey key = new BDMUserStatusKey();
    key.userName = userSkillListKey.userSkillList.userName;
    key.recordStatus = RECORDSTATUS.NORMAL;

    final UserSkillDtlsList userSkillDtlsList =
      curam.ca.gc.bdm.entity.fact.BDMUserSkillFactory.newInstance()
        .searchByUserStatus(key);

    return userSkillDtlsList;
  }

  @Override
  public void cancel(final UserSkillCancelDetails userSkillCancelDetails)
    throws AppException, InformationalException {

    final UserSkillDtls userSkillDtls =
      UserSkillFactory.newInstance().read(userSkillCancelDetails.key);
    if (!userSkillDtls.skillLevel.equals(SKILLLEVEL.NOALLOC)) {
      throw new AppException(BDMUSERSKILLS.ERR_DELETE_PRIORITY_SKILL);
    }
    curam.core.sl.fact.UserSkillFactory.newInstance()
      .cancel(userSkillCancelDetails);
  }

}
