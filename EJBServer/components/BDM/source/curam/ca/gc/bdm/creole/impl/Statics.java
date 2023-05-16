package curam.ca.gc.bdm.creole.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMPHONECOUNTRY;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.entity.fact.BDMBankBranchFactory;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseDtls;
import curam.ca.gc.bdm.entity.person.fact.BDMPersonFactory;
import curam.ca.gc.bdm.entity.struct.BDMBankBranchKeyStruct1;
import curam.ca.gc.bdm.evidence.impl.BDMBankAccountValidation;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.sl.fec.fact.BDMMaintainForeignEngagementCaseFactory;
import curam.ca.gc.bdm.sl.fec.intf.BDMMaintainForeignEngagementCase;
import curam.ca.gc.bdm.util.impl.BDMSinNumberUtil;
import curam.ca.gc.bdm.ws.sinvalidation.impl.SINValidationMod10;
import curam.codetable.ADDRESSELEMENTTYPE;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASETYPECODE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.CONCERNROLETYPE;
import curam.codetable.COUNTRYCODE;
import curam.codetable.RECORDSTATUS;
import curam.commonintake.impl.ApplicationCase;
import curam.commonintake.impl.ApplicationCaseDAO;
import curam.core.facade.fact.ConcernRoleFactory;
import curam.core.fact.AddressFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.CaseRelationshipFactory;
import curam.core.fact.ConcernRoleAlternateIDFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.intf.Address;
import curam.core.intf.ConcernRoleAlternateID;
import curam.core.sl.entity.struct.CaseParticipantRoleKey;
import curam.core.sl.fact.CaseParticipantRoleFactory;
import curam.core.sl.fact.ClientMergeFactory;
import curam.core.sl.fact.TabDetailFormatterFactory;
import curam.core.sl.infrastructure.assessment.impl.DecisionDetailsRuleObjectXMLFormatter;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.ECActiveEvidenceDtls;
import curam.core.sl.infrastructure.struct.ECActiveEvidenceDtlsList;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.intf.CaseParticipantRole;
import curam.core.sl.intf.TabDetailFormatter;
import curam.core.sl.struct.AgeDetails;
import curam.core.sl.struct.CalculationEndDate;
import curam.core.sl.struct.CalculationStartDate;
import curam.core.struct.AddressDtls;
import curam.core.struct.AddressKey;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseKey;
import curam.core.struct.CaseRelationshipCaseIDKey;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.CuramInd;
import curam.core.struct.DupConcernRoleAltIDDetails;
import curam.core.struct.DupConcernRoleAltIDDetailsList;
import curam.core.struct.OtherAddressData;
import curam.core.struct.RelatedCasesDetails;
import curam.core.struct.RelatedCasesDetailsList;
import curam.core.struct.SearchForAltIDTypeKey;
import curam.creole.execution.RuleObject;
import curam.creole.execution.session.Session;
import curam.creole.value.BoundedInterval;
import curam.creole.value.CodeTableItem;
import curam.creole.value.Interval;
import curam.creole.value.Timeline;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.pdc.impl.PDCConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.RecordNotFoundException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Statics {

  /**
   * Validate POBox number is not acceptable for Residential Address
   *
   * This calculation is too complex for rules and so has been
   * coded in java.
   *
   * @param session
   * The rule session
   * @param person
   * the person
   * @return string value
   */

  public static Boolean validatePOBOXNOForResidentialAddress(
    final Session session, final String pdcAddress,
    final CodeTableItem addressType) {

    boolean flag = false;

    if (addressType.code().equals(CONCERNROLEADDRESSTYPE.PRIVATE)) {

      final String poBoxValue = extractPOBoxValue(pdcAddress);
      if (!poBoxValue.trim().isEmpty())
        flag = true;

    }
    return flag;

  }

  /**
   * Return POBOX number from address array
   *
   * @param String
   * pdcAddress
   *
   * @return string value
   */
  private static String extractPOBoxValue(final String pdcAddress) {

    final String[] addressLines = pdcAddress.split(CuramConst.gkNewLine);
    String poBoxValue = "";

    for (final String line : addressLines) {
      if (line.startsWith(ADDRESSELEMENTTYPE.POBOXNO)) {
        poBoxValue =
          line.substring(line.indexOf(CuramConst.gkEqualsNoSpaces) + 1);
        break;
      }
    }
    return poBoxValue;
  }

  // END PO BOX validation JP
  /**
   * Validate email address matches with given email address format.
   * This method return true when email address complies with given regular
   * expression , false otherwise.
   *
   * @param session
   * The rule session
   * @param emailAddress
   * the emailAddress
   * @return boolean
   */
  public static Boolean validateEmailAddress(final Session session,
    final String emailAddress) {

    boolean isValid = false;
    final String emailRegex =
      "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^]+)*@(?:[A-Za-z0-9-]+\\.)+[A-Za-z]{2,6}$";

    if (emailAddress != null && !emailAddress.trim().isEmpty()) {
      if (emailAddress.matches(emailRegex))
        isValid = true;
    }

    return isValid;

  }

  /// TASK 2421
  /**
   * Validate BirthLastName is not null. this method returns null when
   * empty or
   * null, false otherwise
   *
   * @param session
   * The rule session
   * @param birthLastName
   * the birthLastName
   * @return boolean
   */
  public static Boolean isBirthLastNameEmpty(final Session session,
    final String birthLastName) {

    Boolean flag = false;
    if (birthLastName == null || birthLastName.trim().isEmpty())
      flag = true;
    return flag;

  }

  /**
   * Validate parentLastName is not null. this method returns null when empty or
   * null, false otherwise
   *
   * @param session
   * @param parentLastName
   * @return
   */
  public static Boolean isParentLastNameEmpty(final Session session,
    final String parentLastName) {

    Boolean flag = false;
    if (parentLastName == null || parentLastName.trim().isEmpty())
      flag = true;
    return flag;

  }
  // END- TASK 2421- DOD DOB

  /**
   * Return true if SIN Number starts with 9, else false
   *
   * @param session
   * @param alternateID
   * @return
   */
  public static Boolean isSINNumberStartsWith9Series(final Session session,
    final String alternateID) {

    if (alternateID == null || alternateID.isEmpty())
      return false;
    return alternateID.startsWith(BDMConstants.kCA_Accepted_9_SIN_Series);

  }

  /**
   * Task 3460, Age check to be called from CER Eligibility Rules
   *
   * @param concernRoleID
   * @param dateOfBirth
   * @param givenDate
   * @return
   */
  public static Number getPersonAgeAsOnGivenDate(final Session session,
    final Number concernRoleID, final Date dateOfBirth, final Date givenDate)
    throws AppException, InformationalException {

    final TabDetailFormatter ageCalculator =
      TabDetailFormatterFactory.newInstance();
    final CalculationStartDate startDate = new CalculationStartDate();
    final CalculationEndDate endDate = new CalculationEndDate();
    startDate.startDate = dateOfBirth;
    endDate.endDate = givenDate;
    final AgeDetails age = ageCalculator.calculateAge(startDate, endDate);
    return age.ageYears;

  }

  /**
   * Task 3349 , application filed within 2021
   *
   * Return actual submission date , if application doesn't exists returns the
   * current date
   *
   * @param concernRoleID
   *
   * @return
   */
  /*
   * BEGIN - TASK 3695 - AM - get application submission date based off of the
   * integratedCaseID
   */

  public static Date getApplicationSubmissionDate(final Session session,
    final Number caseID) throws AppException, InformationalException {

    Date submissionDate = Date.kZeroDate;

    submissionDate = getAppDateFromAppCase(caseID.longValue());

    if (submissionDate.isZero()) {

      final CaseRelationshipCaseIDKey caseRelationshipCaseIDKey =
        new CaseRelationshipCaseIDKey();
      caseRelationshipCaseIDKey.caseID = caseID.longValue();
      RelatedCasesDetailsList relatedCases;

      relatedCases = CaseRelationshipFactory.newInstance()
        .searchCaseRelationshipByCaseID(caseRelationshipCaseIDKey);

      for (final RelatedCasesDetails details : relatedCases.dtls) {

        submissionDate = getAppDateFromAppCase(details.relatedCaseID);
        if (!submissionDate.isZero()) {
          return submissionDate;
        }
      }
    } else {
      return submissionDate;
    }

    return submissionDate.isZero() ? Date.getCurrentDate() : submissionDate;
  }

  private static Date getAppDateFromAppCase(final long caseID)
    throws AppException, InformationalException {

    Date submissionDate = Date.kZeroDate;
    final curam.core.struct.CaseKey caseKey = new curam.core.struct.CaseKey();
    final curam.core.intf.CaseHeader caseHeader =
      CaseHeaderFactory.newInstance();

    caseKey.caseID = caseID;
    final String caseTypeCode =
      caseHeader.readCaseTypeCode(caseKey).caseTypeCode;

    if (caseTypeCode.equals(CASETYPECODE.APPLICATION_CASE)) {
      final ApplicationCaseDAO applicationCaseDAO =
        GuiceWrapper.getInjector().getInstance(ApplicationCaseDAO.class);
      final ApplicationCase applicationCase = applicationCaseDAO.get(caseID);
      submissionDate =
        new Date(applicationCase.getSubmittedDateTime().getCalendar());
    }
    return submissionDate;
  }
  /*
   * END - TASK 3695 - AM - get application submission date based off of the
   * integratedCaseID
   */

  /* BEGIN - TASK 4245 - CKwong - Identification Validations */
  /**
   * Checks that the sin value follows the mod 10 validation
   *
   * @param session
   * @param sin
   * @return
   */
  public static Boolean validateSINMod10(final Session session,
    final String sin) {

    return !SINValidationMod10.isValidMOD10(sin);

  }

  /**
   * Method to validate if edit operation can be performed on SSN
   *
   * @param session
   * @param sin
   * @return
   */

  public static Boolean canModifySSN(final Session session,
    final RuleObject evidence) throws AppException, InformationalException {

    boolean isGuidedChange = false;
    if (TransactionInfo.getFacadeScopeObject("guidedChange") != null) {
      isGuidedChange =
        (boolean) TransactionInfo.getFacadeScopeObject("guidedChange");
    }

    // get all active SIN evidences for the client
    final EIEvidenceKey evidenceKey = new EIEvidenceKey();
    final Number evidenceIDNumber =
      (Number) evidence.getAttributeValue("evidenceID").getValue();

    final long evidenceID = evidenceIDNumber.longValue();
    evidenceKey.evidenceID = evidenceID;
    evidenceKey.evidenceType = CASEEVIDENCE.IDENTIFICATIONS;

    final Number caseIDNumber =
      (Number) evidence.getAttributeValue("caseID").getValue();
    final long caseID = caseIDNumber.longValue();

    final Number successionIDNumber =
      (Number) evidence.getAttributeValue("successionID").getValue();
    final long successionID = successionIDNumber.longValue();

    final CodeTableItem identificationType =
      (CodeTableItem) evidence.getAttributeValue("altIDType").getValue();
    if (identificationType.code().equals(
      CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER) && !isGuidedChange) {

      final String sinNumber =
        (String) evidence.getAttributeValue("alternateID").getValue();

      final ECActiveEvidenceDtlsList sinEvdList =
        getPDCActiveEvidences(caseID, evidenceKey.evidenceType);

      for (final ECActiveEvidenceDtls activeEvidenceDtls : sinEvdList.dtls) {

        if (activeEvidenceDtls.successionID == successionID
          && activeEvidenceDtls.evidenceID != evidenceID) {
          final String earlierSINValue =
            BDMEvidenceUtil.getDynEvdAttrValue(activeEvidenceDtls.evidenceID,
              evidenceKey.evidenceType, "alternateID");

          if (!sinNumber.equals(earlierSINValue)) {
            return true;
          }
        }
      }
    } else if (identificationType.code()
      .equals(CONCERNROLEALTERNATEID.REFERENCE_NUMBER)) {

      final String referenceNumber =
        (String) evidence.getAttributeValue("alternateID").getValue();
      final Date fromDate =
        (Date) evidence.getAttributeValue("fromDate").getValue();
      Date toDate = (Date) evidence.getAttributeValue("toDate").getValue();
      if (toDate == null) {
        toDate = Date.kZeroDate;
      }

      final ECActiveEvidenceDtlsList referenceEvdList =
        getPDCActiveEvidences(caseID, evidenceKey.evidenceType);

      for (final ECActiveEvidenceDtls activeEvidenceDtls : referenceEvdList.dtls) {

        if (activeEvidenceDtls.successionID == successionID
          && activeEvidenceDtls.evidenceID != evidenceID) {

          final String earlierReferenceValue =
            BDMEvidenceUtil.getDynEvdAttrValue(activeEvidenceDtls.evidenceID,
              evidenceKey.evidenceType, "alternateID");
          final String earlierFromDateValue =
            BDMEvidenceUtil.getDynEvdAttrValue(activeEvidenceDtls.evidenceID,
              evidenceKey.evidenceType, "fromDate");
          final String earlierToDateValue =
            BDMEvidenceUtil.getDynEvdAttrValue(activeEvidenceDtls.evidenceID,
              evidenceKey.evidenceType, "toDate");

          Date earlierFromDate = Date.kZeroDate;
          Date earlierToDate = Date.kZeroDate;

          if (!StringUtil.isNullOrEmpty(earlierFromDateValue)) {
            earlierFromDate = Date.fromISO8601(earlierFromDateValue);
          }
          if (!StringUtil.isNullOrEmpty(earlierToDateValue)) {
            earlierToDate = Date.fromISO8601(earlierToDateValue);
          }

          if (!fromDate.equals(earlierFromDate)) {
            return true;
          }
          if (!earlierToDate.equals(toDate)) {
            return true;
          }
          if (!referenceNumber.equals(earlierReferenceValue)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /* END - TASK 4245 - CKwong - Identification Validations */

  public static Boolean isValidNumber(final Session session,
    final String phoneNumberEvidence) {

    boolean flag = false;

    if (!StringUtil.isNullOrEmpty(phoneNumberEvidence)) {
      if (!allNumber(phoneNumberEvidence))
        flag = true;
    }

    return flag;

  }

  /**
   * Validate given String matches 7 digits. Return True when matches 7
   * digits, false otherwise
   *
   * @param phoneNumberEvidence
   * @return {@link Boolean}
   */

  public static Boolean
    whenMatchesSevenDigitNumber(final String phoneNumberEvidence) {

    final Pattern pattern = Pattern.compile("^\\d{7}$");
    final Matcher matcher = pattern.matcher(phoneNumberEvidence);
    return matcher.matches();
  }

  /** Return True when matches 3 digits */
  /**
   * Validate given String matches 3 digits. Return True when matches all
   * digits, false otherwise
   *
   * @param phoneNumberEvidence
   * @return {@link Boolean}
   */
  public static Boolean
    whenMatchesThreeDigitNumber(final String phoneNumberEvidence) {

    final Pattern pattern = Pattern.compile("^\\d{3}$");
    final Matcher matcher = pattern.matcher(phoneNumberEvidence);
    return matcher.matches();
  }

  /**
   * Validate given String matches all digits. Return True when matches all
   * digits, false otherwise
   *
   * @param phoneNumberEvidence
   * @return {@link Boolean}
   */
  public static Boolean allNumber(final String phoneNumberEvidence) {

    final Pattern pattern = Pattern.compile("^[0-9]*$");
    final Matcher matcher = pattern.matcher(phoneNumberEvidence);
    return matcher.matches();
  }

  /** This method is not used and must be removed **/

  public static Boolean validateAreaCode_CA_US(final Session session,
    final RuleObject phoneNumberEvidence) {

    boolean flag = false;
    final String areaCode = (String) phoneNumberEvidence
      .getAttributeValue(BDMConstants.kEvidenceAttrPhoneAreaCode).getValue();

    final CodeTableItem countryCode = (CodeTableItem) phoneNumberEvidence
      .getAttributeValue(BDMConstants.kEvidenceAttrPhoneCountryCode)
      .getValue();

    if (countryCode.code().equals(BDMPHONECOUNTRY.USA)
      || countryCode.code().equals(BDMPHONECOUNTRY.CANADA)) {

      if (whenMatchesThreeDigitNumber(areaCode)) {
        flag = true;
      }

    }
    return flag;

  }

  /**
   * Validate specified area code is all digits and has length 3 digits. This
   * method returns true
   * when specified are code
   * does match regular expression , false otherwise.
   *
   * @param session
   * @param phoneNumberEvidence
   * @return
   */
  public static Boolean validateAreaCode_CA_US1(final Session session,
    final RuleObject phoneNumberEvidence) {

    boolean flag = false;
    final String areaCode = (String) phoneNumberEvidence
      .getAttributeValue(BDMConstants.kEvidenceAttrPhoneAreaCode).getValue();

    if (!whenMatchesThreeDigitNumber(areaCode)) {
      flag = true;
    }

    return flag;

  }

  /** This method is not used and must be removed **/
  public static Boolean validatePhoneNumber_CA_US(final Session session,
    final RuleObject phoneNumberEvidence) {

    boolean flag = false;

    final String phoneNumber = (String) phoneNumberEvidence
      .getAttributeValue(BDMConstants.kEvidenceAttrPhoneNumber).getValue();

    final CodeTableItem countryCode = (CodeTableItem) phoneNumberEvidence
      .getAttributeValue(BDMConstants.kEvidenceAttrPhoneCountryCode)
      .getValue();

    if (countryCode.code().equals(BDMPHONECOUNTRY.CANADA)
      || countryCode.code().equals(BDMPHONECOUNTRY.USA)) {

      if (whenMatchesSevenDigitNumber(phoneNumber)) {
        flag = true;
      }

    }
    return flag;

  }

  /**
   * Validate phone number matches length of 7 digits. This method returns true
   * when specified phonenumber
   * does match regular expression , false otherwise.
   *
   * @param session
   * @param phoneNumberEvidence
   * @return
   */
  public static Boolean validatePhoneNumber_CA_US1(final Session session,
    final RuleObject phoneNumberEvidence) {

    boolean flag = false;

    final String phoneNumber = (String) phoneNumberEvidence
      .getAttributeValue(BDMConstants.kEvidenceAttrPhoneNumber).getValue();

    if (!whenMatchesSevenDigitNumber(phoneNumber)) {
      flag = true;
    }

    return flag;

  }

  /**
   * Validate phone number matches all digits. This method returns true
   * when specified phonenumber
   * does match regular expression , false otherwise.
   *
   * @param session
   * @param phoneNumberEvidence
   * @return
   */

  public static Boolean validatePhoneNumberOther1(final Session session,
    final RuleObject phoneNumberEvidence) {

    boolean flag = false;
    final String areaCode = (String) phoneNumberEvidence
      .getAttributeValue(BDMConstants.kEvidenceAttrPhoneAreaCode).getValue();
    final String phoneNumber = (String) phoneNumberEvidence
      .getAttributeValue(BDMConstants.kEvidenceAttrPhoneNumber).getValue();

    if (!(allNumber(areaCode) && allNumber(phoneNumber))) {
      flag = true;
    }

    return flag;

  }

  // Phone area code and phone number must be numeric for all countries which do
  // not have a country phone code of + 1.
  public static Boolean validatePhoneNumberOther(final Session session,
    final RuleObject phoneNumberEvidence) {

    boolean flag = false;
    final String areaCode = (String) phoneNumberEvidence
      .getAttributeValue(BDMConstants.kEvidenceAttrPhoneAreaCode).getValue();
    final String phoneNumber = (String) phoneNumberEvidence
      .getAttributeValue(BDMConstants.kEvidenceAttrPhoneNumber).getValue();

    final CodeTableItem countryCode = (CodeTableItem) phoneNumberEvidence
      .getAttributeValue(BDMConstants.kEvidenceAttrPhoneCountryCode)
      .getValue();

    if (!(countryCode.code().equals(BDMPHONECOUNTRY.USA)
      && countryCode.code().equals(BDMPHONECOUNTRY.CANADA))) {

      if (allNumber(areaCode) && allNumber(phoneNumber)) {
        flag = true;
      }

    }
    return flag;

  }

  /**
   * Returns a timeline representation somebody's age over time based on their
   * birthday. maxAge dictates how high
   * the intervals will go. set to 125 right now to account for the age of the
   * oldest human.
   *
   * @param session
   * @param birthDate Date of birth to calculate age timeline for
   * @return age timeline starting from the dateOfBirth
   */
  public static Timeline<Number> getAgeTimeline(final Session session,
    final Date birthDate) {

    final int maxAge = 125;

    final List<Interval<Number>> intervals =
      new ArrayList<Interval<Number>>();

    // create initial interval before birthDate (age of 0)
    intervals.add(new Interval<Number>(null, 0));

    final Calendar iterCal = birthDate.getCalendar();

    // create intervals for ages 1 - maxAge
    for (int i = 1; i < maxAge; i++) {
      iterCal.roll(Calendar.YEAR, 1);
      intervals.add(new Interval<Number>(new Date(iterCal), i));
    }

    return new Timeline<Number>(intervals);
  }

  /**
   * Adds the specified number of days to the specified date
   *
   * @param session
   * @param date date to add days to
   * @param numDays days to add to date
   * @return New date numDays after specified date
   */
  public static Date addDaysToDate(final Session session, final Date date,
    final Number numDays) {

    return date.addDays(numDays.intValue());
  }

  // START : TASK 8841 Income Evidence validation
  /**
   * This method verifies given Year is four digit number.
   *
   * @param Session
   * @param String
   * @return Boolean
   */
  public static Boolean validateYear(final Session session,
    final RuleObject dependencyEvidence) {

    final String year = (String) dependencyEvidence
      .getAttributeValue(BDMConstants.kYearAttribute).getValue();
    return validateYearValue(year);
  }

  /**
   * This method verifies given Year is four digit number.
   *
   * @param String
   * @return Boolean
   */
  public static Boolean validateYearValue(final String year) {

    Boolean isValidYear = false;
    final boolean isNumeric = year.chars().allMatch(Character::isDigit);
    if (isNumeric && year.length() == 4) {
      final int yearVal = convertYearStringToInteger(year);

      if (yearVal != 0 && yearVal >= 1900 && yearVal <= 2999) {
        isValidYear = true;
      }
    }

    // Return boolean result.
    return isValidYear;
  }

  public static int convertYearStringToInteger(final String year) {

    int yearValue = 0;
    try {
      yearValue = Integer.parseInt(year);
    } catch (final NumberFormatException nfe) {
      nfe.printStackTrace();
    }
    return yearValue;
  }

  /**
   * This method verifies given Year is a previous year value.
   *
   * @param Session
   * @param String
   * @return Boolean
   */
  public static Boolean validateYearForPreviousYear(final Session session,
    final RuleObject incomeEvidence) {

    boolean flag = false;
    final String yearValue = (String) incomeEvidence
      .getAttributeValue(BDMConstants.kYearAttribute).getValue();

    if (!StringUtil.isNullOrEmpty(yearValue)
      && validateYearValue(yearValue)) {
      final int year = Integer.parseInt(yearValue);
      final int currentYear = Calendar.getInstance().get(Calendar.YEAR);
      if (currentYear <= year) {
        flag = true;
      }
    }

    return flag;
  }

  //
  /**
   * This method verifies given income value is valid double value greater than
   * 0.
   *
   * @param Session
   *
   * @return Boolean
   */
  public static Boolean isValidIncome(final Session session,
    final RuleObject incomeEvidence) {

    boolean flag = false;
    final Number netIncome = (Number) incomeEvidence
      .getAttributeValue(BDMConstants.kIncomeAttribute).getValue();

    if (null != netIncome) {
      if (Double.parseDouble(netIncome.toString()) > 0)

        flag = true;
    }

    return flag;

  }
  // END : TASK 8841 Income Evidence validation

  /**
   * This method creates an incomeTimeline based on the given income and year.
   *
   * @param Session
   * @param Income Evidence
   * @return Income Timeline
   */
  public static Date getIncomeStartDate(final Session session,
    final String year) {

    // parse the year into the date starting Jan 1st on the year entered
    final int currentYear = Integer.parseInt(year) + 1;

    final Date startDate =
      Date.fromISO8601(String.valueOf(currentYear) + "0101");

    return startDate;
  }

  public static Date getIncomeEndDate(final Session session,
    final String year) {

    // parse the end date of Dec 31st on the year after the one entered
    final int currentYear = Integer.parseInt(year) + 1;

    final Date endDate =
      Date.fromISO8601(String.valueOf(currentYear) + "1231");

    return endDate;
  }

  /**
   * method to check if enddate is after current date
   *
   * @param session
   * @param emailAddressEvdEnddate
   * @param phoneNumberEvdEnddate
   * @return
   */
  public static Boolean isAlertIndEnddated(final Session session,
    final Date endDate) {

    final Date currentDate = TransactionInfo.getSystemDate();

    if (null != endDate && !endDate.isZero() && currentDate.after(endDate)) {

      return true;

    }

    return false;

  }

  /**
   * Creates a timeline of ages that are snapshotted for each application date.
   * an age interval will go on indefinitely until the next application date.
   * the final interval will go on indefinitely
   *
   * @param session
   * @param applicationDates List of application dates for the IC
   * @param ageTimeline Unaligned timeline representative of a person's age
   * @return
   */
  public static Timeline<Number> getApplicationDateAlignedAgeTimeline(
    final Session session, final List<Date> applicationDates,
    final Timeline<Number> ageTimeline) {

    // instantiate interval list
    final List<Interval<Number>> ageIntervalList =
      new ArrayList<Interval<Number>>();

    // first interval will always start at 0 date with a value of 0
    ageIntervalList.add(new Interval<Number>(null, 0));

    // create an interval for each application date, grabbing the age value from
    // the age timeline on that date
    for (final Date applicationDate : applicationDates) {
      ageIntervalList.add(new Interval<Number>(applicationDate,
        ageTimeline.valueOn(applicationDate)));
    }

    return new Timeline<Number>(ageIntervalList);

  }

  /**
   * Util Method to remove country name from the display on summary screen
   *
   * @since TASK-10566
   * @param session
   * @param endDate
   * @return
   */
  public static String getCountryCode(final Session session,
    final RuleObject phoneNumberEvd) {

    final String countryCode = phoneNumberEvd
      .getAttributeValue(BDMConstants.kEvidenceAttrPhoneCountryCode)
      .getValue().toString();

    String formattedCountryCode =
      countryCode.substring(0, countryCode.indexOf(CuramConst.gkSpace));

    if (formattedCountryCode.indexOf(CuramConst.gkDash) > 0) {
      formattedCountryCode =
        formattedCountryCode.replace(CuramConst.gkDash, CuramConst.gkEmpty);
    }

    // BUG-10898 Tkonda- removed '-'
    formattedCountryCode = formattedCountryCode + " ";

    return formattedCountryCode;

  }

  /**
   * @since
   * @param session
   * @param phoneNumberEvd
   * @return
   */
  public static String getStringDate(final Session session, final Date date) {

    String stringDate = "";

    if (null != date) {

      stringDate = date.toString();

    }

    return stringDate;

  }

  /**
   * This method returns the Current Date.
   *
   * @param session
   * @return
   */

  public static Date getCurrentDate(final Session session) {

    return Date.getCurrentDate();
  }

  /**
   * Get the value on the timeline on current date.
   *
   * @param <T>
   * @param session
   * @param timeline
   * @return
   */
  public static <T> T getTimelineValueBasedOnDate(final Session session,
    final Timeline<T> timeline, final Date applicationDate) {

    return timeline
      .valueOn(applicationDate == null || applicationDate.isZero()
        ? Date.getCurrentDate() : applicationDate);
  }

  /**
   * This method returns the XML String present on the current date.
   *
   * @param session
   * @param ruleObject
   * @return
   */
  public static String getDisplayXml(final Session session,
    final RuleObject ruleObject, final Date applicationDate) {

    final DecisionDetailsRuleObjectXMLFormatter xmlFormatter =
      new DecisionDetailsRuleObjectXMLFormatter(ruleObject);
    final String xmlString = xmlFormatter
      .formatAsDisplayXml(applicationDate == null || applicationDate.isZero()
        ? Date.getCurrentDate() : applicationDate);

    return xmlString;
  }

  /**
   * @since ADO-16437
   * @param session
   * @param startDate
   * @param endDate
   * @param caseID
   * @param dependantConcernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static Boolean isDependantClaimed(final Session session,
    final Date startDate, final Date endDate, final Number caseID,
    final Number dependantCaseParticipantRoleID)
    throws AppException, InformationalException {

    final BDMUtil bdmUtil = new BDMUtil();

    final long dependantConcernRoleID = bdmUtil.getParticipantRoleID(caseID,
      dependantCaseParticipantRoleID.longValue());

    return bdmUtil.isDependantClaimedOnAnotherCase(caseID.longValue(),
      dependantConcernRoleID, startDate, endDate);

  }

  /**
   *
   * @param session
   * @param participantRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static Boolean isValidProvinceTypeResident(final Session session,
    final Number participantRoleID, final String provinceType)
    throws AppException, InformationalException {

    Boolean isValidProvinceTypeResident = false;

    final BDMEvidenceUtil bdmEvidenceUtil = new BDMEvidenceUtil();
    final List<DynamicEvidenceDataDetails> addressEvdDtls =
      bdmEvidenceUtil.getEvdDtlsByConcernroleIDandEvidenceType(
        participantRoleID.longValue(), PDCConst.PDCADDRESS);

    final AddressKey addressKey = new AddressKey();
    final Address addressObj = AddressFactory.newInstance();
    AddressDtls addressDtls = new AddressDtls();

    for (final DynamicEvidenceDataDetails details : addressEvdDtls) {

      if (details.getAttribute("addressType").getValue()
        .equals(CONCERNROLEADDRESSTYPE.PRIVATE)
        && details.getAttribute("toDate").getValue().isEmpty()) {

        addressKey.addressID =
          Long.parseLong(details.getAttribute("address").getValue());
        addressDtls = addressObj.read(addressKey);

        final OtherAddressData otherAddressData = new OtherAddressData();
        otherAddressData.addressData = addressDtls.addressData;

        // get Country
        final String country = bdmEvidenceUtil
          .getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.COUNTRY);

        // province
        final String province = bdmEvidenceUtil
          .getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.PROVINCE);

        if (country.equals(COUNTRYCODE.CACODE)
          && province.equals(provinceType)) {

          isValidProvinceTypeResident = true;
          break;

        }

      }

    }

    return isValidProvinceTypeResident;

  }

  /**
   * Converts the eligibile timeline to a one day timeline.
   * Called from the dependant eligibile rules
   *
   * @param session
   * @param inptuTimeline
   * @return
   */
  public static Timeline<Boolean> getOneDayEligibilityTimeline(
    final Session session, final Timeline<Boolean> inptuTimeline) {

    if (inptuTimeline.equals(Timeline.FALSE_FOREVER)) {
      return inptuTimeline;
    }
    final List<Interval<Boolean>> condensedIntervals =
      new ArrayList<Interval<Boolean>>();
    // set the null date as false
    condensedIntervals.add(new Interval((Date) null, false));
    for (final BoundedInterval<Boolean> boundedInterval : inptuTimeline
      .intervals()) {
      if (boundedInterval.value().booleanValue()
        && boundedInterval.startDate() != null) {
        // set the one day as true and then next day as false and then break.
        condensedIntervals
          .add(new Interval<Boolean>(boundedInterval.startDate(), true));
        condensedIntervals.add(new Interval<Boolean>(
          boundedInterval.startDate().addDays(1), false));
        break;
      }
    }
    return new Timeline<Boolean>(condensedIntervals);
  }

  /**
   * This method returns the bank name using financialInstitutionNumber And
   * TransitNumber and Locale.
   *
   * @param session
   * @param bankAccountEvd
   * @return
   */
  public static String getBankNameUsingInstitutionNumberAndTransitNumber(
    final Session session, final RuleObject bankAccountEvd)
    throws AppException, InformationalException {

    String bankName = "";
    final BDMBankBranchKeyStruct1 keyStruct1 = new BDMBankBranchKeyStruct1();
    // Read Financial Institute Number form the evidence
    final String financialInstitutionNumber = bankAccountEvd
      .getAttributeValue(BDMConstants.kFINANCIAL_INSTITUTION_NUMBER)
      .getValue().toString();

    // Read Transit Number form the evidence
    final String branchTransitNumber =
      bankAccountEvd.getAttributeValue(BDMConstants.kBRANCH_TRANSIT_NUMBER)
        .getValue().toString();

    // Assign values to the key struct.
    keyStruct1.bic = financialInstitutionNumber.concat(branchTransitNumber);
    keyStruct1.localeCode = TransactionInfo.getProgramLocale();

    // Invoke entity method to get the name corresponding to Locale.
    try {
      bankName =
        BDMBankBranchFactory.newInstance().readBySortCode(keyStruct1).name;
    } catch (final RecordNotFoundException rnfe) {
      // If record not found then assign blank value to bankName attribute.
      bankName = "";
      rnfe.printStackTrace();
    }

    // Return the name
    return bankName;

  }

  /**
   * This method validates if newly added or updated bank account evidence
   * already exists. Returns true if the newly added or updated evidence details
   * "Financial Institution Number", "Transit Number" and "Account Number"
   * matches to the existing evidence otherwise returns false.
   *
   * @param session
   * @param bankAccountEvd bank account evidence details
   * @return true if bank account exists otherwise false
   *
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  public static boolean isBankAccountExists(final Session session,
    final RuleObject bankAccountEvd)
    throws AppException, InformationalException {

    // get all active bank account evidences for the client
    final EIEvidenceKey evidenceKey = new EIEvidenceKey();
    final Number evidenceIDNumber =
      (Number) bankAccountEvd.getAttributeValue("evidenceID").getValue();

    final long evidenceID = evidenceIDNumber.longValue();
    evidenceKey.evidenceID = evidenceID;
    evidenceKey.evidenceType = PDCConst.PDCBANKACCOUNT;

    final Number caseIDNumber =
      (Number) bankAccountEvd.getAttributeValue("caseID").getValue();
    final long caseID = caseIDNumber.longValue();

    final Number successionIDNumber =
      (Number) bankAccountEvd.getAttributeValue("successionID").getValue();
    final long successionID = successionIDNumber.longValue();

    final ECActiveEvidenceDtlsList bankAccountEvdList =
      getPDCActiveEvidences(caseID, evidenceKey.evidenceType);

    // check the duplicate bank account - get all active bank account evidences
    // for the person except current evidence
    for (final ECActiveEvidenceDtls activeEvidenceDtls : bankAccountEvdList.dtls) {

      if (activeEvidenceDtls.successionID == successionID
        || activeEvidenceDtls.evidenceID == evidenceID) {
        continue;
      }

      if (isDuplicateBankAccount(bankAccountEvd, activeEvidenceDtls)) {
        return true;
      }
    }

    return false;
  }

  /**
   * This method compares the new or updated bank account evidence with the
   * active evidence for the duplicate account.
   *
   * @param bankAccountEvd bank account evidence details
   * @param activeEvidenceDtls active bank account evidence details
   * @return true if evidence is duplicate otherwise false
   */
  private static boolean isDuplicateBankAccount(
    final RuleObject bankAccountEvd,
    final ECActiveEvidenceDtls activeEvidenceDtls)
    throws AppException, InformationalException {

    // Read Financial Institute Number form the evidence
    final String financialInstitutionNumber = bankAccountEvd
      .getAttributeValue(BDMConstants.kFINANCIAL_INSTITUTION_NUMBER)
      .getValue().toString();

    // Read Transit Number form the evidence
    final String branchTransitNumber =
      bankAccountEvd.getAttributeValue(BDMConstants.kBRANCH_TRANSIT_NUMBER)
        .getValue().toString();

    // Read account Number form the evidence
    final String accountNumber =
      bankAccountEvd.getAttributeValue("accountNumber").getValue().toString();

    // Assign values to the key struct
    final String sortCode =
      financialInstitutionNumber.concat(branchTransitNumber);

    final EIEvidenceKey evidenceKey = new EIEvidenceKey();
    evidenceKey.evidenceID = activeEvidenceDtls.evidenceID;
    evidenceKey.evidenceType = PDCConst.PDCBANKACCOUNT;

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      new BDMBankAccountValidation().readDynamicEvidence(evidenceKey);

    final String activeEvdAccountNumber =
      dynamicEvidenceDataDetails.getAttribute("accountNumber").getValue();
    final String activeEvdSortCode =
      dynamicEvidenceDataDetails.getAttribute("sortCode").getValue();

    // compares current evidence bank sortCode (Financial institution
    // number + transit number) and bankAccount number evidence details with the
    // existing active evidence.
    if (activeEvdAccountNumber.equals(accountNumber)
      && activeEvdSortCode.equals(sortCode)) {
      return true;
    }

    return false;
  }

  /**
   * Returns the active bank account evidences for the client.
   *
   * @param concernRoleKey concern role identifier
   * @param evidenceType evidence type
   * @param evidenceID evidence identifier
   * @return list of active evidence details for the client
   *
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  private static ECActiveEvidenceDtlsList
    getPDCActiveEvidences(final long caseID, final String evidenceType)
      throws AppException, InformationalException {

    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = caseID;

    // get all active evidences for the client
    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final ECActiveEvidenceDtlsList ecActiveEvidenceDtlsList =
      evidenceControllerObj.listActive(caseKey);

    final ECActiveEvidenceDtlsList activeBankAccountEvidenceDtlsList =
      new ECActiveEvidenceDtlsList();

    // return the active evidences for the evidence type like bank account
    // evidence
    for (final ECActiveEvidenceDtls ecActiveEvidenceDtls : ecActiveEvidenceDtlsList.dtls) {
      if (ecActiveEvidenceDtls.evidenceType.equals(evidenceType)) {
        activeBankAccountEvidenceDtlsList.dtls.add(ecActiveEvidenceDtls);
      }
    }

    return activeBankAccountEvidenceDtlsList;
  }

  /**
   * Task 26388, GEt Participant Name from CaseParticipantRoleID
   *
   * @param concernRoleID
   * @param dateOfBirth
   * @param givenDate
   * @return
   */
  public static String getParticipantNameFromCaseParticipantRoleID(
    final Session session, final Number caseParticipant)
    throws AppException, InformationalException {

    final BDMUtil bdmUtil = new BDMUtil();
    final long participantRoleID =
      bdmUtil.getParticipantRoleID(null, caseParticipant.longValue());

    final curam.core.facade.struct.ConcernRoleIDKey concernRoleIDKey =
      new curam.core.facade.struct.ConcernRoleIDKey();

    concernRoleIDKey.concernRoleID = participantRoleID;
    return ConcernRoleFactory.newInstance()
      .readConcernRoleName(concernRoleIDKey).concernRoleName;

  }

  /**
   * This method checks whether the percentage number of dollar amount got
   * changed during modification.
   *
   * @param session
   * @param vtwEvd
   * @return boolean flag.
   * @throws AppException
   * @throws InformationalException
   */
  public static boolean isPercentageOrAmountIsChanged(final Session session,
    final RuleObject vtwEvd) throws AppException, InformationalException {

    // get all active VTW evidences for the client
    final EIEvidenceKey evidenceKey = new EIEvidenceKey();
    final Number evidenceIDNumber =
      (Number) vtwEvd.getAttributeValue("evidenceID").getValue();

    final long evidenceID = evidenceIDNumber.longValue();
    evidenceKey.evidenceID = evidenceID;
    evidenceKey.evidenceType = CASEEVIDENCE.BDMVTW;

    final Number caseIDNumber =
      (Number) vtwEvd.getAttributeValue("caseID").getValue();
    final long caseID = caseIDNumber.longValue();

    final Number successionIDNumber =
      (Number) vtwEvd.getAttributeValue("successionID").getValue();
    final long successionID = successionIDNumber.longValue();

    final Number amountValue =
      (Number) vtwEvd.getAttributeValue("amount").getValue();

    final boolean isDollarAmountExists = amountValue.doubleValue() > 0.0;

    final Number percentageValue =
      (Number) vtwEvd.getAttributeValue("percentageValue").getValue();
    final boolean isPercentagevalueExists = percentageValue.longValue() > 0.0;

    final ECActiveEvidenceDtlsList bankAccountEvdList =
      getPDCActiveEvidences(caseID, evidenceKey.evidenceType);

    for (final ECActiveEvidenceDtls activeEvidenceDtls : bankAccountEvdList.dtls) {

      if (activeEvidenceDtls.successionID == successionID
        && activeEvidenceDtls.evidenceID != evidenceID) {

        final String earlierPercentageValue =
          BDMEvidenceUtil.getDynEvdAttrValue(activeEvidenceDtls.evidenceID,
            evidenceKey.evidenceType, "percentageValue");
        final String earlierDollarAmounteValue =
          BDMEvidenceUtil.getDynEvdAttrValue(activeEvidenceDtls.evidenceID,
            evidenceKey.evidenceType, "amount");

        final boolean isEarlierPercentageValueExists =
          !StringUtil.isNullOrEmpty(earlierPercentageValue)
            && !earlierPercentageValue.equals("0");
        final boolean isEarlierDollarAmounteValueExists =
          !StringUtil.isNullOrEmpty(earlierDollarAmounteValue)
            && !earlierDollarAmounteValue.equals("0.0");
        if (isEarlierPercentageValueExists && !isPercentagevalueExists) {
          return true;
        } else if (!isEarlierPercentageValueExists
          && isPercentagevalueExists) {
          return true;
        } else if (!isEarlierDollarAmounteValueExists
          && isDollarAmountExists) {
          return true;
        } else if (isEarlierDollarAmounteValueExists
          && !isDollarAmountExists) {
          return true;
        }

      }
    }

    return false;
  }

  /**
   * Method to validate if edit operation can be performed on VTW
   *
   * @param session
   * @param vtw
   * @return
   */

  public static Boolean canVTWModified(final Session session,
    final RuleObject evidence) throws AppException, InformationalException {

    // get all active VTW evidences for the client
    final EIEvidenceKey evidenceKey = new EIEvidenceKey();
    final Number evidenceIDNumber =
      (Number) evidence.getAttributeValue("evidenceID").getValue();

    final long evidenceID = evidenceIDNumber.longValue();
    evidenceKey.evidenceID = evidenceID;
    evidenceKey.evidenceType = CASEEVIDENCE.BDMVTW;

    final Number caseIDNumber =
      (Number) evidence.getAttributeValue("caseID").getValue();
    final long caseID = caseIDNumber.longValue();

    final Number successionIDNumber =
      (Number) evidence.getAttributeValue("successionID").getValue();
    final long successionID = successionIDNumber.longValue();

    final ECActiveEvidenceDtlsList vtwEvdList =
      getPDCActiveEvidences(caseID, evidenceKey.evidenceType);

    for (final ECActiveEvidenceDtls activeEvidenceDtls : vtwEvdList.dtls) {

      if (activeEvidenceDtls.successionID == successionID
        && activeEvidenceDtls.evidenceID != evidenceID) {

        final String earlierEndDateValue = BDMEvidenceUtil.getDynEvdAttrValue(
          activeEvidenceDtls.evidenceID, evidenceKey.evidenceType, "endDate");

        Date earlierEndDate = Date.kZeroDate;

        if (!StringUtil.isNullOrEmpty(earlierEndDateValue)) {
          earlierEndDate = Date.fromISO8601(earlierEndDateValue);
        }

        if (!earlierEndDate.isZero()
          && earlierEndDate.before(Date.getCurrentDate())) {
          return true;
        }

      }
    }

    return false;
  }

  // Task 58892 DEV: Implement 'Add Source of Client Phone Number'
  // TODO: Restricted Countries. If this check is required elsewhere in the
  // application, the internal logic of this method should be extract to a
  // common utility.
  /**
   * Checks if the given Country code table item corresponds to a country in the
   * list of countries whose data is restricted.
   *
   * @param session
   * @param countryCode
   * @return
   */
  public static Boolean isRestrictedCountry(final Session session,
    final CodeTableItem countryCode) {

    if (countryCode == null) {
      return false;
    }

    final String[] restrictedCountries =
      Configuration.getProperty(EnvVars.BDM_ENV_RESTRICTED_COUNTRY_LIST)
        .split(CuramConst.gkComma);

    for (final String country : restrictedCountries) {
      if (country.trim().equals(countryCode.code())) {
        return true;
      }
    }

    return false;

  }

  // Task 57386 DEV: Implement Identification Evidence Changes
  /**
   * Tests whether the given identifier is a number of digits for its type.
   *
   * @param session
   * @param identifierType
   * @param identifier
   * @return
   */
  public static Boolean isValidIdentifierLength(final Session session,
    final CodeTableItem identifierType, final String identifier) {

    final Pattern pattern = Pattern.compile("\\d+");

    if (CONCERNROLEALTERNATEID.BDM_ACCOUNT_NUMBER
      .equals(identifierType.code())
      || CONCERNROLEALTERNATEID.BDM_IA_ACCOUNT_NUMBER
        .equals(identifierType.code())) {
      return identifier.trim().length() == 9
        && pattern.matcher(identifier).matches();
    } else if (CONCERNROLEALTERNATEID.BDM_CIDN
      .equals(identifierType.code())) {
      return identifier.trim().length() == 12
        && pattern.matcher(identifier).matches();
    } else {
      return true;
    }

  }

  // Task 57386 DEV: Implement Identification Evidence Changes
  /**
   * Tests whether the given identifier is a valid format for its type.
   *
   * @param session
   * @param identifierType
   * @param identifier
   * @return
   */
  public static Boolean isValidIdentifierFormat(final Session session,
    final CodeTableItem identifierType, final String identifier) {

    if (!CONCERNROLEALTERNATEID.BDM_ACCOUNT_NUMBER
      .equals(identifierType.code())
      && !CONCERNROLEALTERNATEID.BDM_IA_ACCOUNT_NUMBER
        .equals(identifierType.code())) {
      return true;
    } else if (isValidIdentifierLength(session, identifierType, identifier)) {
      return identifier.startsWith("0");
    } else {
      return false;
    }

  }

  // Task 57386 DEV: Implement Identification Evidence Changes
  /**
   * Checks whether a disallowed overlap for the given identifier type
   */
  public static Boolean isDisallowedOverlappingID(final Session session,
    final RuleObject identificationEvidence)
    throws AppException, InformationalException {

    final Number participant = (Number) identificationEvidence
      .getAttributeValue("participant").getValue();
    final String altId = (String) identificationEvidence
      .getAttributeValue("alternateID").getValue();
    final CodeTableItem type = (CodeTableItem) identificationEvidence
      .getAttributeValue("altIDType").getValue();

    if (CONCERNROLEALTERNATEID.BDM_FOREIGN_IDENTIFIER.equals(type.code())) {
      return false;
    }

    final CaseParticipantRole cprObj =
      CaseParticipantRoleFactory.newInstance();
    final CaseParticipantRoleKey cprKey = new CaseParticipantRoleKey();

    cprKey.caseParticipantRoleID = participant.longValue();

    final ConcernRoleAlternateID concernRoleAltIDObj =
      ConcernRoleAlternateIDFactory.newInstance();

    final SearchForAltIDTypeKey searchForAltIDTypeKey =
      new SearchForAltIDTypeKey();

    searchForAltIDTypeKey.alternateID = altId;
    searchForAltIDTypeKey.alternateIDType = type.code();
    searchForAltIDTypeKey.statusCode = RECORDSTATUS.NORMAL;
    searchForAltIDTypeKey.concernRoleID =
      cprObj.readCaseIDandParticipantID(cprKey).participantRoleID;

    final DupConcernRoleAltIDDetailsList dupConcernRoleAltIDDetailsList =
      concernRoleAltIDObj.searchByConcernAltIDAndType(searchForAltIDTypeKey);

    for (final DupConcernRoleAltIDDetails dupConcernRoleAltIDDetails : dupConcernRoleAltIDDetailsList.dtls) {

      if (!dupConcernRoleAltIDDetails.endDate.isZero()) {
        continue;
      }

      return true;

    }

    return false;

  }

  // Task 63026 DEV: Implement SIN Masking Via Summary Rule Set
  /**
   * Masks the given SIN via
   * {@link BDMSinNumberUtil#replaceSinNumberWithAsterisks(String)}.
   *
   * @param session
   * @param sin
   * @return
   */
  public static String maskSIN(final Session session, final String sin) {

    return BDMSinNumberUtil.replaceSinNumberWithAsterisks(sin);

  }

  // BEGIN 73158 DEV: Contribution details Evidence in Foreign Engagement Case
  /**
   * Read the FEC country code based on case ID.
   *
   * @param session
   * @param fecCaseID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static CodeTableItem getFecCountry(final Session session,
    final Number fecCaseID) throws AppException, InformationalException {

    final BDMMaintainForeignEngagementCase fecCase =
      BDMMaintainForeignEngagementCaseFactory.newInstance();
    final CaseHeaderKey key = new CaseHeaderKey();

    key.caseID = fecCaseID.longValue();
    final BDMFECaseDtls bdmfeCaseDtls = fecCase.readCountryCode(key);

    return new CodeTableItem(BDMSOURCECOUNTRY.TABLENAME,
      bdmfeCaseDtls.countryCode);
  }

  public static Boolean doesSINExistForConcernType(final Session session,
    final RuleObject identificationEvidence)
    throws AppException, InformationalException {

    String typeCode;

    final Number participant = (Number) identificationEvidence
      .getAttributeValue("participant").getValue();
    final String altId = (String) identificationEvidence
      .getAttributeValue("alternateID").getValue();
    final CodeTableItem type = (CodeTableItem) identificationEvidence
      .getAttributeValue("altIDType").getValue();

    if (type == null) {
      typeCode = "";
    } else {
      typeCode = type.code();
    }

    final CaseParticipantRole cprObj =
      CaseParticipantRoleFactory.newInstance();
    final CaseParticipantRoleKey cprKey = new CaseParticipantRoleKey();
    cprKey.caseParticipantRoleID = participant.longValue();

    final SearchForAltIDTypeKey searchForAltIDTypeKey =
      new SearchForAltIDTypeKey();
    searchForAltIDTypeKey.alternateID = altId;
    searchForAltIDTypeKey.alternateIDType = typeCode;
    searchForAltIDTypeKey.statusCode = RECORDSTATUS.NORMAL;
    searchForAltIDTypeKey.concernRoleID =
      cprObj.readCaseIDandParticipantID(cprKey).participantRoleID;

    final DupConcernRoleAltIDDetailsList dupConcernRoleAltIDDetailsList =
      BDMPersonFactory.newInstance()
        .searchByConcernAltID(searchForAltIDTypeKey);
    boolean duplicateFound = false;

    for (final DupConcernRoleAltIDDetails dupConcernRoleAltIDDetails : dupConcernRoleAltIDDetailsList.dtls) {

      final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

      concernRoleKey.concernRoleID = dupConcernRoleAltIDDetails.concernRoleID;

      final CuramInd isDuplicateInd = ClientMergeFactory.newInstance()
        .isConcernRoleDuplicate(concernRoleKey);

      // Start 99491
      Boolean isProspectPerson = false;
      if (ConcernRoleFactory.newInstance()
        .readConcernRole(concernRoleKey).concernRoleType
          .equals(CONCERNROLETYPE.PROSPECTPERSON)) {
        isProspectPerson = true;
      }
      // End 99491

      if (!isProspectPerson && !isDuplicateInd.statusInd) {
        duplicateFound = true;
        break;
      }
    }
    return Boolean.valueOf(duplicateFound);

  }

}
