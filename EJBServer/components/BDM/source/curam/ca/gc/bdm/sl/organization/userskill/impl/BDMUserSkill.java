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
   *
   * Added new validations on create
   * 1. Not to create more than 3 active priority skills other than 'No
   * Allocations'.
   * 2. No more than one priority related to each Post Monitor Training.
   * 3. Post Monitor Training Secondary should not be added without Post Monitor
   * Training Primary.
   */
  @Override
  public void validateUserSkill(final UserSkillDtls userSkillDtls)
    throws AppException, InformationalException {

    final boolean hasPrimary;
    final boolean hasSecondary;
    final boolean hasTertiary;
    final boolean hasPTMPrimary;
    final boolean hasPTMSecondary;
    final int skillLevelDtlsSize;

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

    // START: TASK 115764 - MP -- Added New validations for Managing user skills
    predicate = dtls -> dtls.skillLevel.equals(SKILLLEVEL.NOALLOC);
    userSkillDtlsList.dtls.removeIf(predicate);

    skillLevelDtlsSize = userSkillDtlsList.dtls.size();

    if (skillLevelDtlsSize >= 3
      && !userSkillDtls.skillLevel.equals(SKILLLEVEL.NOALLOC))
      informationalManager.addInformationalMsg(
        new AppException(BDMUSERSKILLS.ERR_MAX_PRIORITY_SKILLS),
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);

    // END: TASK 115764 - MP -- Added New validations for Managing user skills

    predicate = dtls -> dtls.skillLevel.equals(SKILLLEVEL.PRIMARY);
    hasPrimary = userSkillDtlsList.dtls.removeIf(predicate);

    predicate = dtls -> dtls.skillLevel.equals(SKILLLEVEL.SECONDARY);
    hasSecondary = userSkillDtlsList.dtls.removeIf(predicate);

    predicate = dtls -> dtls.skillLevel.equals(SKILLLEVEL.TERTIARY);
    hasTertiary = userSkillDtlsList.dtls.removeIf(predicate);

    predicate = dtls -> dtls.skillLevel.equals(SKILLLEVEL.PTM);
    hasPTMPrimary = userSkillDtlsList.dtls.removeIf(predicate);

    predicate = dtls -> dtls.skillLevel.equals(SKILLLEVEL.PTMS);
    hasPTMSecondary = userSkillDtlsList.dtls.removeIf(predicate);

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

    // START: TASK 115764 - MP -- Added New validations for Managing user skills
    else if (userSkillDtls.skillLevel.equals(SKILLLEVEL.PTM)) {
      if (hasPTMPrimary) {
        informationalManager.addInformationalMsg(
          new AppException(
            BDMUSERSKILLS.ERR_MULTIPLE_SAME_POST_TRAINING_MONITOR),
          CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
      }
    }

    else if (userSkillDtls.skillLevel.equals(SKILLLEVEL.PTMS)) {
      if (hasPTMSecondary) {
        informationalManager.addInformationalMsg(
          new AppException(
            BDMUSERSKILLS.ERR_MULTIPLE_SAME_POST_TRAINING_MONITOR),
          CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
      }

      if (!hasPTMPrimary) {
        informationalManager.addInformationalMsg(
          new AppException(
            BDMUSERSKILLS.ERR_NO_POST_TRAINING_MONITOR_PRIMARY),
          CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
      }

    }
    // END: TASK 115764 - MP -- Added New validations for Managing user skills

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
   * 
   * Added new validations on modify screen
   * 1. Not to create more than 3 active priority skills other than 'No
   * Allocations'.
   * 2. No more than one priority related to each Post Monitor Training.
   * 3. Post Monitor Training Secondary should not be added without Post Monitor
   * Training Primary.
   */
  @Override
  public void validateMassModifyUserSkills(
    final BDMUserSkillPriorityList skillPriorityList)
    throws InformationalException {

    int primary = 0;
    int secondary = 0;
    int tertiary = 0;
    int postTrainingMonintoringPrimary = 0;
    int postTrainingMonintoringSecondary = 0;
    int noAllocations = 0;

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();
    // START: TASK 115764 - MP - Added new validations on Manage User Skills screen
    // count how many primary, secondary, and tertiary skills exist
    for (final BDMUserSkillPriority skillPriority : skillPriorityList.dtls) {
      if (skillPriority.skillPriority.equals(SKILLLEVEL.PRIMARY)) {
        primary++;
      } else if (skillPriority.skillPriority.equals(SKILLLEVEL.SECONDARY)) {
        secondary++;
      } else if (skillPriority.skillPriority.equals(SKILLLEVEL.TERTIARY)) {
        tertiary++;
      } else if (skillPriority.skillPriority.equals(SKILLLEVEL.PTM)) {
        postTrainingMonintoringPrimary++;
      } else if (skillPriority.skillPriority.equals(SKILLLEVEL.PTMS)) {
        postTrainingMonintoringSecondary++;
      } else if (skillPriority.skillPriority.equals(SKILLLEVEL.NOALLOC)) {
        noAllocations++;
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
    if (postTrainingMonintoringPrimary > 1 || postTrainingMonintoringSecondary > 1) {
      informationalManager.addInformationalMsg(
        new AppException(BDMUSERSKILLS.ERR_MULTIPLE_SAME_POST_TRAINING_MONITOR),
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
    }
    if (postTrainingMonintoringSecondary > 0 && postTrainingMonintoringPrimary == 0) {
      informationalManager.addInformationalMsg(
        new AppException(BDMUSERSKILLS.ERR_NO_POST_TRAINING_MONITOR_PRIMARY),
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
    }
    if ((skillPriorityList.dtls.size() - noAllocations) > 3) {
      informationalManager.addInformationalMsg(
        new AppException(BDMUSERSKILLS.ERR_MAX_PRIORITY_SKILLS),
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
    }
 // END: TASK 115764 - MP - Added new validations on Manage User Skills screen

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
