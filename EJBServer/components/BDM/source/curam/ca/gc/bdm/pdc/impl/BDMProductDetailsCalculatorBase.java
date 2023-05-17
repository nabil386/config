package curam.ca.gc.bdm.pdc.impl;

/**
 * Base class for PDC Details Calculator
 *
 * @author kumar.dhananjay
 *
 */
public abstract class BDMProductDetailsCalculatorBase {

  public abstract String getProdcutDisplayCategory();

  public abstract String getSkillTypeBasedOnProduct(String issueLevel);
}
