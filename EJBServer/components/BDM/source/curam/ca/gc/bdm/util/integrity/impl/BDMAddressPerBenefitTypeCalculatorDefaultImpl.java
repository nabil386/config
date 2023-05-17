package curam.ca.gc.bdm.util.integrity.impl;

/**
 * Base class for Address Threshold calculation
 *
 * @author kumar.dhananjay
 *
 */
public class BDMAddressPerBenefitTypeCalculatorDefaultImpl
  extends BDMAddressPerBenefitTypeCalculatorBase {

  @Override
  public int getBenefitPerAddressAndBenefitTypeLimit() {

    return Integer.MAX_VALUE;
  }
}
