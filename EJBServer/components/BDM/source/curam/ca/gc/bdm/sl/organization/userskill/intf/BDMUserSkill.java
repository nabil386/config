package curam.ca.gc.bdm.sl.organization.userskill.intf;

import curam.ca.gc.bdm.facade.organization.struct.BDMUserSkillPriorityList;
import curam.core.facade.struct.UserSkillListKey;
import curam.core.sl.entity.struct.UserSkillDtls;
import curam.core.sl.entity.struct.UserSkillDtlsList;
import curam.core.sl.struct.UserSkillCancelDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public interface BDMUserSkill {

  public void validateUserSkill(final UserSkillDtls userSkillDtls)
    throws AppException, InformationalException;

  public void validateMassModifyUserSkills(
    final BDMUserSkillPriorityList skillPriorityList)
    throws InformationalException;

  public UserSkillDtlsList
    getActiveUserSkills(final UserSkillListKey userSkillListKey)
      throws AppException, InformationalException;

  public void modifyUserSkill(final UserSkillDtls userSkillDtls)
    throws AppException, InformationalException;

  public void cancel(final UserSkillCancelDetails userSkillCancelDetails)
    throws AppException, InformationalException;
}
