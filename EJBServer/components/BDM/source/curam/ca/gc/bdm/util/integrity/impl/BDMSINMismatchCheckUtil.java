package curam.ca.gc.bdm.util.integrity.impl;

import curam.ca.gc.bdm.codetable.BDMSIRMATCHRESULT;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRRestResponse;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRValidation;
import curam.core.impl.EnvVars;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;

public class BDMSINMismatchCheckUtil {

  public static String mismatchCheck(final BDMSINIntegrityCheckResult result)
    throws AppException, InformationalException {

    if (result == null || result.getSIRResponse() == null) {
      return BDMSINIntegrityCheckConstants.CHECK_RESULT_UNAVAILABLE;
    }

    final BDMSINSIRRestResponse sirResponse = result.getSIRResponse();

    if (!sirResponse.isSINSIRValidatonSuccess()) {

      return BDMSINIntegrityCheckConstants.CHECK_RESULT_ERROR;
    }

    final BDMSINSIRValidation sirResponseResult =
      sirResponse.getValidatedSINResults();

    if (sirResponseResult.getSsMatchFlag()) {

      return BDMSINIntegrityCheckConstants.CHECK_RESULT_PASS;

    }

    final String sirSin = convertNull(sirResponseResult.getSsIndividual()
      .getSsSearchTable().getPerPersonSINIdentification());

    final String sirParentName = convertNull(sirResponseResult
      .getSsIndividual().getSsSearchTable().getSsParentMaidenName());

    final String sirDob = convertNull(sirResponseResult.getSsIndividual()
      .getSsSearchTable().getNcPersonBirthDate());

    final String sirGivenName = getFirstResponse(sirResponseResult
      .getSsIndividual().getSsSearchTable().getNcPersonGivenName());

    final String sirMiddleName = getFirstResponse(sirResponseResult
      .getSsIndividual().getSsSearchTable().getNcPersonMiddleName());

    final String sirSurName = getFirstResponse(sirResponseResult
      .getSsIndividual().getSsSearchTable().getNcPersonSurName());

    final String sin =
      convertNull(result.getSIRRequest().getPerPersonSINIdentification());
    final String parentName =
      convertNull(result.getSIRRequest().getSsParentMaidenName());
    final String dob =
      convertNull(result.getSIRRequest().getNcPersonBirthDate());
    final String givenName =
      convertNull(result.getSIRRequest().getNcPersonGivenName());
    final String middleName =
      convertNull(result.getSIRRequest().getNcPersonMiddleName());
    final String surName =
      convertNull(result.getSIRRequest().getNcPersonSurName());
    final String birthLastName =
      convertNull(result.getDetails().getBirthLastName());

    if (!sin.equals(sirSin) || !dob.equals(sirDob)) {

      /*
       * Task 93103:
       * The Response for the SIR call must NOT check for Parent’s Last Name at
       * Birth
       * || !parentName.equalsIgnoreCase(sirParentName)) {
       */
      return BDMSINIntegrityCheckConstants.CHECK_RESULT_MISMATCH;
    }

    final boolean givenNameMatched =
      matchGivenName(sirGivenName, sirMiddleName, givenName, middleName);

    boolean surNameMatched = false;
    if (givenNameMatched) {
      surNameMatched = surName.equalsIgnoreCase(sirSurName)
        || birthLastName.equalsIgnoreCase(sirSurName);
      /*
       * Task 93103:
       * The Response for the SIR call must NOT check for Parent’s Last Name at
       * Birth
       *
       * || surName.equalsIgnoreCase(sirParentName)
       * || birthLastName.equalsIgnoreCase(sirParentName);
       */ }

    if (surNameMatched)
      return BDMSINIntegrityCheckConstants.CHECK_RESULT_PASS;

    if (sirGivenName.equalsIgnoreCase(surName)
      && sirSurName.equalsIgnoreCase(givenName)) {
      return BDMSINIntegrityCheckConstants.CHECK_RESULT_PASS;
    }

    if (sirGivenName.equalsIgnoreCase(surName)
      && sirSurName.equalsIgnoreCase(middleName)) {
      return BDMSINIntegrityCheckConstants.CHECK_RESULT_PASS;
    }

    if (sirGivenName.equalsIgnoreCase(birthLastName)
      && sirSurName.equalsIgnoreCase(givenName)) {
      return BDMSINIntegrityCheckConstants.CHECK_RESULT_PASS;
    }

    if (sirGivenName.equalsIgnoreCase(birthLastName)
      && sirSurName.equalsIgnoreCase(middleName)) {
      return BDMSINIntegrityCheckConstants.CHECK_RESULT_PASS;
    }

    if (sirMiddleName.equalsIgnoreCase(surName)
      && sirSurName.equalsIgnoreCase(givenName)) {
      return BDMSINIntegrityCheckConstants.CHECK_RESULT_PASS;
    }

    if (sirMiddleName.equalsIgnoreCase(surName)
      && sirSurName.equalsIgnoreCase(middleName)) {
      return BDMSINIntegrityCheckConstants.CHECK_RESULT_PASS;
    }

    if (sirMiddleName.equalsIgnoreCase(birthLastName)
      && sirSurName.equalsIgnoreCase(givenName)) {
      return BDMSINIntegrityCheckConstants.CHECK_RESULT_PASS;
    }

    if (sirMiddleName.equalsIgnoreCase(birthLastName)
      && sirSurName.equalsIgnoreCase(middleName)) {
      return BDMSINIntegrityCheckConstants.CHECK_RESULT_PASS;
    }

    /*
     * Task 93103:
     * The Response for the SIR call must NOT check for Parent’s Last Name at
     * Birth
     *
     * if (sirMiddleName.equalsIgnoreCase(parentName)
     * && sirSurName.equalsIgnoreCase(givenName)) {
     * return BDMSINIntegrityCheckConstants.CHECK_RESULT_PASS;
     * }
     *
     * if (sirMiddleName.equalsIgnoreCase(parentName)
     * && sirSurName.equalsIgnoreCase(middleName)) {
     * return BDMSINIntegrityCheckConstants.CHECK_RESULT_PASS;
     * }
     */

    return BDMSINIntegrityCheckConstants.CHECK_RESULT_MISMATCH;

  }

  private static boolean matchGivenName(final String sirGivenName,
    final String sirMiddleName, final String givenName,
    final String middleName) {

    final boolean sirGivenNameBlank = StringUtil.isNullOrEmpty(sirGivenName);
    final boolean sirMiddleNameBlank =
      StringUtil.isNullOrEmpty(sirMiddleName);

    if (sirGivenNameBlank && sirMiddleNameBlank) {
      return true;
    }

    final String[] jrSuffixList = getSuffixList(EnvVars.BDM_ENV_JR_SUFFIX);
    final String[] srSuffixList = getSuffixList(EnvVars.BDM_ENV_SR_SUFFIX);

    if (!sirGivenNameBlank) {
      if (sirGivenName.equalsIgnoreCase(givenName)
        || sirGivenName.equalsIgnoreCase(middleName)
        || sirGivenName.equalsIgnoreCase(
          givenName + BDMSINIntegrityCheckConstants.NAME_SPACE + middleName)
        || sirGivenName.equalsIgnoreCase(
          middleName + BDMSINIntegrityCheckConstants.NAME_SPACE + givenName))
        return true;

      for (final String surfix : jrSuffixList) {
        if (sirGivenName.equalsIgnoreCase(
          givenName + BDMSINIntegrityCheckConstants.NAME_SPACE + surfix)
          || sirGivenName.equalsIgnoreCase(
            middleName + BDMSINIntegrityCheckConstants.NAME_SPACE + surfix)
          || sirGivenName.equalsIgnoreCase(
            givenName + BDMSINIntegrityCheckConstants.NAME_SPACE + middleName
              + BDMSINIntegrityCheckConstants.NAME_SPACE + surfix)
          || sirGivenName.equalsIgnoreCase(
            middleName + BDMSINIntegrityCheckConstants.NAME_SPACE + givenName
              + BDMSINIntegrityCheckConstants.NAME_SPACE + surfix))
          return true;
      }

      for (final String surfix : srSuffixList) {
        if (sirGivenName.equalsIgnoreCase(
          givenName + BDMSINIntegrityCheckConstants.NAME_SPACE + surfix)
          || sirGivenName.equalsIgnoreCase(
            middleName + BDMSINIntegrityCheckConstants.NAME_SPACE + surfix)
          || sirGivenName.equalsIgnoreCase(
            givenName + BDMSINIntegrityCheckConstants.NAME_SPACE + middleName
              + BDMSINIntegrityCheckConstants.NAME_SPACE + surfix)
          || sirGivenName.equalsIgnoreCase(
            middleName + BDMSINIntegrityCheckConstants.NAME_SPACE + givenName
              + BDMSINIntegrityCheckConstants.NAME_SPACE + surfix))
          return true;
      }
    }

    if (!sirMiddleNameBlank) {
      if (sirMiddleName.equalsIgnoreCase(givenName)
        || sirMiddleName.equalsIgnoreCase(middleName)
        || sirMiddleName.equalsIgnoreCase(
          givenName + BDMSINIntegrityCheckConstants.NAME_SPACE + middleName)
        || sirMiddleName.equalsIgnoreCase(
          middleName + BDMSINIntegrityCheckConstants.NAME_SPACE + givenName))
        return true;

      for (final String surfix : jrSuffixList) {
        if (sirMiddleName.equalsIgnoreCase(
          givenName + BDMSINIntegrityCheckConstants.NAME_SPACE + surfix)
          || sirMiddleName.equalsIgnoreCase(
            middleName + BDMSINIntegrityCheckConstants.NAME_SPACE + surfix)
          || sirMiddleName.equalsIgnoreCase(
            givenName + BDMSINIntegrityCheckConstants.NAME_SPACE + middleName
              + BDMSINIntegrityCheckConstants.NAME_SPACE + surfix)
          || sirMiddleName.equalsIgnoreCase(
            middleName + BDMSINIntegrityCheckConstants.NAME_SPACE + givenName
              + BDMSINIntegrityCheckConstants.NAME_SPACE + surfix))
          return true;
      }

      for (final String surfix : srSuffixList) {
        if (sirMiddleName.equalsIgnoreCase(
          givenName + BDMSINIntegrityCheckConstants.NAME_SPACE + surfix)
          || sirMiddleName.equalsIgnoreCase(
            middleName + BDMSINIntegrityCheckConstants.NAME_SPACE + surfix)
          || sirMiddleName.equalsIgnoreCase(givenName + middleName
            + BDMSINIntegrityCheckConstants.NAME_SPACE + surfix)
          || sirMiddleName.equalsIgnoreCase(middleName + givenName
            + BDMSINIntegrityCheckConstants.NAME_SPACE + surfix))
          return true;
      }
    }

    if (!sirMiddleNameBlank && !sirGivenNameBlank) {
      if (givenName.equalsIgnoreCase(sirGivenName
        + BDMSINIntegrityCheckConstants.NAME_SPACE + sirMiddleName))
        return true;

      for (final String surfix : jrSuffixList) {
        if (givenName.equalsIgnoreCase(sirGivenName
          + BDMSINIntegrityCheckConstants.NAME_SPACE + sirMiddleName
          + BDMSINIntegrityCheckConstants.NAME_SPACE + surfix))
          return true;
      }

      if (middleName.equalsIgnoreCase(sirGivenName
        + BDMSINIntegrityCheckConstants.NAME_SPACE + sirMiddleName))
        return true;

      for (final String surfix : jrSuffixList) {
        if (middleName.equalsIgnoreCase(sirGivenName
          + BDMSINIntegrityCheckConstants.NAME_SPACE + sirMiddleName
          + BDMSINIntegrityCheckConstants.NAME_SPACE + surfix))
          return true;
      }
    }

    return false;

  }

  private static String convertNull(final String s) {

    if (StringUtil.isNullOrEmpty(s)) {
      return "";
    }
    return s.trim();
  }

  private static String getFirstResponse(final String s) {

    final String s2 = convertNull(s);
    final String[] sl = s2.split(",");
    return sl[0];
  }

  private static String[] getSuffixList(final String suffixProperty) {

    return Configuration.getProperty(suffixProperty)
      .split(BDMSINIntegrityCheckConstants.SUFFIX_DELIMITER);
  }

  /**
   * Calculate Identity Confirmation message
   *
   * @param checkResult
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static String
    calculateIdentityMatchResult(final BDMSINIntegrityCheckResult checkResult)
      throws AppException, InformationalException {

    // calculate field identity confirmed
    // SIR response flag
    if (checkResult == null || checkResult.getSIRResponse() == null
      || !checkResult.getSIRResponse().isSINSIRValidatonSuccess()) {
      return BDMSIRMATCHRESULT.NOTMATCHED;
    }
    // Match logic
    final Boolean matchFromSIR =
      checkResult.getSIRResponse().getValidatedSINResults().getSsMatchFlag();
    if (matchFromSIR) {
      return BDMSIRMATCHRESULT.MATCHBYSIR;
    }
    // match result after tolerance check
    final String misMatchResult = checkResult.getMismatchCheckResult();
    if (misMatchResult
      .equals(BDMSINIntegrityCheckConstants.CHECK_RESULT_PASS)) {
      return BDMSIRMATCHRESULT.MATCHBYTOLERANCE;
    }
    // return
    return BDMSIRMATCHRESULT.NOTMATCHED;
  }
}
