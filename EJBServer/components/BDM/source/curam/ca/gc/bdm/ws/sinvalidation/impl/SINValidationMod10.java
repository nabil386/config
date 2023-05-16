package curam.ca.gc.bdm.ws.sinvalidation.impl;

import curam.rules.functions.SINValidator;

/**
 * Provides utility methods used for the external SIN validation service within
 * this package.
 */
public class SINValidationMod10 {

  /**
   * Validates a SIN number against MOD10.
   *
   * @param rulesParameters
   * @return Boolean value indicating if SIN number is valid or not.
   * @throws InformationalException
   * @throws AppException
   */
  public static boolean isValidMOD10(final String rulesStringParameters) {

    boolean returnValue = false;
    int totalDigits = 0;

    try {
      totalDigits = rulesStringParameters.length();
      returnValue = totalDigits == SINValidator.SIN_LENGTH
        && checkMOD10(rulesStringParameters);
    } catch (final NumberFormatException e) {
      e.printStackTrace();
    }
    return returnValue;
  }

  /**
   * Validates a number against MOD10.
   * The algorithm doubles every second digit (and takes the sum of the 2
   * digits if the result is greater than 9) and sums all the results with the
   * other (non-doubled) digits. If the result ends with 0, the number is
   * valid.
   *
   * @param sin the number to check
   * @return true if the number is valid
   */
  private static boolean checkMOD10(final String sin) {

    final int nDigits = sin.length();

    int nSum = 0;
    boolean isSecond = false;
    for (int i = nDigits - 1; i >= 0; i--) {

      int d = sin.charAt(i) - '0';

      if (isSecond == true)
        d = d * 2;

      // We add two digits to handle
      // cases that make two digits
      // after doubling
      nSum += d / 10;
      nSum += d % 10;

      isSecond = !isSecond;
    }
    return nSum % 10 == 0;
  }

}
