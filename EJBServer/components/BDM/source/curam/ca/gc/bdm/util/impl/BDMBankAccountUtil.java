package curam.ca.gc.bdm.util.impl;

/**
 * Utility class for Bank account Details.
 *
 *
 * @author Ruturaj Varne
 *
 */
public class BDMBankAccountUtil {

  /**
   * This method split sortcode value into Institution Number & Transit Number
   *
   * @param String
   * @return BDMBankAccountDetails
   */
  public static BDMBankAccountDetails
    getInstitutionNumberAndTransitNumber(final String sortCode) {

    // Instantiate BDMBankAccountDetails class.
    final BDMBankAccountDetails details = new BDMBankAccountDetails();

    // Split the Sort code to get Institution Number
    details.setInstitutionNumber(sortCode.substring(0, 3));

    // Split the Sort code to get Transit Number
    details.setTransitNumber(sortCode.substring(3));

    // Return the object
    return details;
  }
}
