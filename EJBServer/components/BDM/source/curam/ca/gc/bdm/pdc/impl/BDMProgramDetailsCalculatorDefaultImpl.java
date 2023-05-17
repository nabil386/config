package curam.ca.gc.bdm.pdc.impl;

import curam.ca.gc.bdm.codetable.BDMCALLSUBJECT;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskSkillTypeKey;
import curam.codetable.SKILLTYPE;
import curam.util.resources.StringUtil;

/**
 * Base class for PDC Details Calculator
 *
 * @author kumar.dhananjay
 *
 */
public class BDMProgramDetailsCalculatorDefaultImpl
  extends BDMProgramDetailsCalculatorBase {

  @Override
  public BDMTaskSkillTypeKey getSkillTypeBasedOnProgram(
    final String callBackSubjectCode, final String benefitNames) {

    // Task 57837 Fix Call back task skill type
    final BDMTaskSkillTypeKey bdmTaskSkillTypeKey = new BDMTaskSkillTypeKey();
    if (StringUtil.isNullOrEmpty(benefitNames)) {
      if (BDMCALLSUBJECT.EXPBENEFITRATE.equals(callBackSubjectCode)
        || BDMCALLSUBJECT.EXPPAYMENTSTATUS.equals(callBackSubjectCode)
        || BDMCALLSUBJECT.EXPOFOVERPAYMENT.equals(callBackSubjectCode)
        || BDMCALLSUBJECT.EXPCLAIMSTATUS.equals(callBackSubjectCode)
        || BDMCALLSUBJECT.OTHERSUBJECT.equals(callBackSubjectCode)) {
        bdmTaskSkillTypeKey.skillType = SKILLTYPE.VSG02;
      } else {
        bdmTaskSkillTypeKey.skillType = SKILLTYPE.VSG02;
        // this is written in case benefit not selected and other subject codes
      }
    }
    if (!StringUtil.isNullOrEmpty(benefitNames)) {
      if (BDMCALLSUBJECT.EXPBENEFITRATE.equals(callBackSubjectCode)
        || BDMCALLSUBJECT.EXPCLAIMSTATUS.equals(callBackSubjectCode)) {
        bdmTaskSkillTypeKey.skillType = SKILLTYPE.VSG02;
      } else if (BDMCALLSUBJECT.EXPPAYMENTSTATUS.equals(callBackSubjectCode)
        || BDMCALLSUBJECT.EXPOFOVERPAYMENT.equals(callBackSubjectCode)
        || BDMCALLSUBJECT.OTHERSUBJECT.equals(callBackSubjectCode)) {
        bdmTaskSkillTypeKey.skillType = SKILLTYPE.VSG03;
      } else {
        bdmTaskSkillTypeKey.skillType = SKILLTYPE.VSG02;
      }
    }
    return bdmTaskSkillTypeKey;
  }
}
