package curam.ca.gc.bdm.pdc.impl;

import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskSkillTypeKey;

/**
 * Base class for PDC Details Calculator
 *
 * @author kumar.dhananjay
 *
 */
public abstract class BDMProgramDetailsCalculatorBase {

  public abstract BDMTaskSkillTypeKey getSkillTypeBasedOnProgram(
    final String callBackSubjectCode, final String benefitNames);
}
