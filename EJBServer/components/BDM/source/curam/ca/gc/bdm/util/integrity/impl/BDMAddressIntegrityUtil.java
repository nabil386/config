package curam.ca.gc.bdm.util.integrity.impl;

import com.google.inject.Inject;
import curam.util.persistence.GuiceWrapper;
import java.util.Map;

/**
 * Class to map the Address Threshold calculation for different product
 *
 * @author kumar.dhananjay
 *
 */
public class BDMAddressIntegrityUtil {

  @Inject
  public Map<Long, BDMAddressPerBenefitTypeCalculatorBase> benefitTypeCalculatorMap;

  public BDMAddressIntegrityUtil() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Return the implementation class for the address property calculation
   * defined for a
   * product id.
   *
   * @param productid
   * @return
   */
  public BDMAddressPerBenefitTypeCalculatorBase
    getCalculatorClassImpl(final long productid) {

    return benefitTypeCalculatorMap.get(productid);
  }
}
