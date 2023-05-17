package curam.ca.gc.bdmoas.creole.impl;

import curam.ca.gc.bdm.codetable.BDMYESNO;
import curam.ca.gc.bdm.entity.fact.BDMExternalPartyFactory;
import curam.ca.gc.bdm.entity.intf.BDMExternalParty;
import curam.ca.gc.bdm.entity.struct.BDMExtPartyNameAndTypeKey;
import curam.ca.gc.bdm.entity.struct.BDMExternalPartyDetails;
import curam.ca.gc.bdm.entity.struct.BDMExternalPartyDetailsList;
import curam.ca.gc.bdm.entity.struct.BDMExternalPartyDtls;
import curam.ca.gc.bdm.entity.struct.BDMExternalPartyKey;
import curam.ca.gc.bdm.facade.integratedcase.struct.BDMCurrentDeductionsForProductDeliveryList;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.util.impl.BDMDateUtil;
import curam.ca.gc.bdmoas.deductions.impl.BDMOASMaintainDeduction;
import curam.ca.gc.bdmoas.entity.paymentschedule.fact.BDMOASAnnualPaymentScheduleFactory;
import curam.ca.gc.bdmoas.entity.paymentschedule.struct.BDMOASAnnualPaymentScheduleDtls;
import curam.ca.gc.bdmoas.entity.paymentschedule.struct.BDMOASAnnualPaymentScheduleMonthAndYearDetails;
import curam.ca.gc.bdmoas.impl.BDMOASConstants;
import curam.ca.gc.bdmoas.rules.impl.BDMOASAggregateYearCalculator;
import curam.ca.gc.bdmoas.util.impl.BDMOASEvidenceUtil;
import curam.codetable.CASERELATIONSHIPREASONCODE;
import curam.codetable.CASERELATIONSHIPTYPECODE;
import curam.codetable.CASESTATUS;
import curam.codetable.EXTERNALPARTYTYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.AddressDataFactory;
import curam.core.fact.AddressFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.CaseRelationshipFactory;
import curam.core.impl.CuramConst;
import curam.core.intf.Address;
import curam.core.intf.AddressData;
import curam.core.intf.CaseHeader;
import curam.core.intf.CaseRelationship;
import curam.core.struct.AddressKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseRelByRelatedCaseIDStatusReasonTypeKey;
import curam.core.struct.CaseRelationshipDtls;
import curam.core.struct.CaseRelationshipDtlsList;
import curam.core.struct.OtherAddressData;
import curam.creole.execution.RuleObject;
import curam.creole.execution.session.Session;
import curam.creole.value.BoundedInterval;
import curam.creole.value.CodeTableItem;
import curam.creole.value.Interval;
import curam.creole.value.Timeline;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.StringUtil;
import curam.util.type.Date;
import curam.util.type.NotFoundIndicator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Statics {

  private Statics() {

  }

  /**
   * Calculates the 6 month repayment date from the given date.
   *
   * @param session
   * @param date
   * @return
   */
  public static Date getCancellationRepaymentDate(final Session session,
    final Date date) {

    final Calendar calendar = date.getCalendar();
    calendar.add(Calendar.MONTH, 6);

    return new Date(calendar);

  }

  /**
   * Determines whether a given country has a protective date provision.
   *
   * @param session
   * @param countryCode
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static Boolean isProtectiveDateCountry(final Session session,
    final CodeTableItem countryCode)
    throws AppException, InformationalException {

    Boolean isProtectiveDateCountry = false;

    final BDMExternalParty externalParty =
      BDMExternalPartyFactory.newInstance();
    final BDMExtPartyNameAndTypeKey externalPartyNameKey =
      new BDMExtPartyNameAndTypeKey();
    externalPartyNameKey.name =
      countryCode.toLocale(Locale.ENGLISH).toUpperCase();
    externalPartyNameKey.type = EXTERNALPARTYTYPE.SSACOUNTRY;

    final BDMExternalPartyDetailsList externalPartyList =
      externalParty.searchExternalPartiesByNameAndType(externalPartyNameKey);

    if (externalPartyList.dtls.size() > 0) {

      final BDMExternalPartyDetails details = externalPartyList.dtls.get(0);

      final BDMExternalPartyKey externalPartyKey = new BDMExternalPartyKey();
      externalPartyKey.concernRoleID = details.concernRoleID;

      final BDMExternalPartyDtls externalPartyDtls =
        externalParty.read(externalPartyKey);

      isProtectiveDateCountry =
        BDMYESNO.YES.equals(externalPartyDtls.isProtectiveDate);

    }

    return isProtectiveDateCountry;

  }

  /**
   * This method will return the manually entered income start date.
   *
   * @param session
   * @param year
   * @return
   */
  public static Date getManuallyEnteredIncomeStartDate(final Session session,
    final String year) {

    if (!StringUtil.isNullOrEmpty(year)
      && year.matches(BDMConstants.NUMERIC_PATTERN)) {

      // Convert year to Integer
      final Integer yearValue = Integer.valueOf(year);

      final Calendar startDateCalendar = Date.getCurrentDate().getCalendar();
      startDateCalendar.set(Calendar.YEAR, yearValue + CuramConst.gkOne);
      startDateCalendar.set(Calendar.MONTH, Calendar.JULY);
      startDateCalendar.set(Calendar.DAY_OF_MONTH, CuramConst.gkOne);

      return new Date(startDateCalendar.getTimeInMillis());

    }

    return Date.kZeroDate;
  }

  /**
   * This method will return the manually entered income end date.
   *
   * @param session
   * @param year
   * @return
   */
  public static Date getManuallyEnteredIncomeEndDate(final Session session,
    final String year) {

    if (!StringUtil.isNullOrEmpty(year)
      && year.matches(BDMConstants.NUMERIC_PATTERN)) {

      // Convert year to Integer
      final Integer yearValue = Integer.valueOf(year);

      final Calendar endDateCalendar = Date.getCurrentDate().getCalendar();
      endDateCalendar.set(Calendar.YEAR, yearValue + BDMOASConstants.kTwo);
      endDateCalendar.set(Calendar.MONTH, Calendar.JUNE);
      endDateCalendar.set(Calendar.DAY_OF_MONTH, BDMOASConstants.kThirty);

      return new Date(endDateCalendar.getTimeInMillis());

    }

    return Date.kZeroDate;
  }

  /**
   * This method will check the size of the description text and if
   * its less than 3 then returns <code>true</code> otherwise <code>false</code>
   *
   * @param session
   *
   * @param description
   * Contains the description text value.
   *
   * @return <code>true</code> if the description more than 3 charcters long
   * else <code>false</code>
   */
  public static boolean isValidLengthDescription(final Session session,
    final String description) {

    if (!StringUtil.isNullOrEmpty(description)
      && description.length() < BDMOASConstants.kThree) {

      return false;
    }

    return true;

  }

  /**
   * Truncates a string at the given limit with the suffix appended.
   *
   * @param session
   * @param input
   * @param limit
   * @param suffix
   * @return
   */
  public static String truncateString(final Session session,
    final String input, final Number limit, final String suffix) {

    if (input.length() <= limit.intValue()) {
      return input;
    } else {
      return input.substring(0, limit.intValue()) + suffix;
    }

  }

  /**
   * Determines whether the target case is currently a participant in
   * authorization.
   *
   * @param session
   * @param caseID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static Boolean isAuthorizationInProgress(final Session session,
    final Number caseID) throws AppException, InformationalException {

    boolean isAuthorizationInProgress = false;

    final CaseRelationship caseRelationshipObj =
      CaseRelationshipFactory.newInstance();
    final CaseRelByRelatedCaseIDStatusReasonTypeKey caseRelationshipKey =
      new CaseRelByRelatedCaseIDStatusReasonTypeKey();
    caseRelationshipKey.relatedCaseID = caseID.longValue();
    caseRelationshipKey.statusCode = RECORDSTATUS.NORMAL;
    caseRelationshipKey.typeCode = CASERELATIONSHIPTYPECODE.RELATEDCASES;
    caseRelationshipKey.reasonCode = CASERELATIONSHIPREASONCODE.LINKEDCASES;

    final CaseRelationshipDtlsList caseRelationshipDtlsList =
      caseRelationshipObj
        .searchByRelatedCaseIDStatusReasonType(caseRelationshipKey);

    for (final CaseRelationshipDtls caseRelationshipDtls : caseRelationshipDtlsList.dtls) {

      final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
      final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
      caseHeaderKey.caseID = caseRelationshipDtls.caseID;
      final CaseHeaderDtls caseHeaderDtls = caseHeaderObj.read(caseHeaderKey);

      isAuthorizationInProgress =
        CASESTATUS.AUTHORIZATIONINPROGRESS.equals(caseHeaderDtls.statusCode);

    }

    return isAuthorizationInProgress;

  }

  /**
   * This method validates if the year is valid year value.
   *
   * @param session
   * CREOLE session.
   * @param year
   * Year value
   * @return
   * Returns true if year valid Year value else return false.
   */
  public static boolean isValidYear(final Session session,
    final String year) {

    if (!StringUtil.isNullOrEmpty(year)
      && year.matches(BDMConstants.NUMERIC_PATTERN)) {

      return true;
    }

    return false;
  }

  /**
   * This method validates if the year is valid year and
   * is between the current year and greater than
   * number of previous years.
   *
   * @param session
   * CREOLE session.
   * @param year
   * year
   * @return
   * True if the valid else false.
   */
  public static boolean isYearWithInRangeFromPreviousYearsAndCurrentYear(
    final Session session, final String year,
    final Number numberOfPreviousYears) {

    if (isValidYear(session, year)) {

      // Convert year to Integer
      final Integer yearValue = Integer.valueOf(year);

      final Integer previousYear =
        Date.getCurrentDate().getCalendar().get(Calendar.YEAR)
          - numberOfPreviousYears.intValue();

      if (yearValue > previousYear && yearValue < Date.getCurrentDate()
        .getCalendar().get(Calendar.YEAR)) {

        return true;
      }
    }
    return false;
  }

  /**
   * This method will return the year value of the date.
   *
   * @param session
   * @param endDate
   */
  public static int getDateYearValue(final Session session, final Date date) {

    if (!Date.kZeroDate.equals(date)) {

      return date.getCalendar().get(Calendar.YEAR);

    }

    return CuramConst.gkZero;

  }

  /**
   * This method will return the month value of the date.
   * Month value will start from 0 for January.
   *
   * @param session
   * @param endDate
   */
  public static int getDateMonthValue(final Session session,
    final Date date) {

    if (!Date.kZeroDate.equals(date)) {

      return date.getCalendar().get(Calendar.MONTH);

    }

    return CuramConst.gkZero;

  }

  /**
   * This method will return the max date for the month and year.
   *
   * @param session
   * CREOLE Session
   * @param month
   * Month value.
   * @param year
   * Year value.
   *
   * @return
   * Max month date value.
   *
   */
  public static Date getMaxDateForMonth(final Session session,
    final Number month, final Number year) {

    final Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.MONTH, month.intValue());
    calendar.set(Calendar.YEAR, year.intValue());
    final int maxday = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    calendar.set(Calendar.DAY_OF_MONTH, maxday);

    return new Date(calendar);

  }

  /**
   * This method will return the Payment Issue date for the
   * year and month passed from the configured BDMOASAnnualPaymentSchedule
   * table.
   *
   * @param session
   * CREOLE Session
   * @param month
   * Payment Schedule Month
   * @param year
   * Payment Schedule Year
   * @return
   * Payment Issue date for the Month and Year from the configuration else
   * return the max date of the June month for the year.
   * @throws AppException
   * Application Exception
   * @throws InformationalException
   * Informational Exception.
   */
  public static Date getAnnualPaymentScheduleDate(final Session session,
    final Number month, final Number year)
    throws AppException, InformationalException {

    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
    final BDMOASAnnualPaymentScheduleMonthAndYearDetails annualPaymentScheduleMonthAndYearDetails =
      new BDMOASAnnualPaymentScheduleMonthAndYearDetails();
    annualPaymentScheduleMonthAndYearDetails.month = month.longValue();
    annualPaymentScheduleMonthAndYearDetails.year = year.longValue();

    final BDMOASAnnualPaymentScheduleDtls bdmOASAnnualPaymentScheduleDtls =
      BDMOASAnnualPaymentScheduleFactory.newInstance().readByMonthAndYear(
        notFoundIndicator, annualPaymentScheduleMonthAndYearDetails);

    // If the Payment Issue is not present in the table for the year
    // then get the maximum date for the month of June for that year.
    if (notFoundIndicator.isNotFound()) {

      return getMaxDateForMonth(session, Calendar.JUNE, year);

    }
    return bdmOASAnnualPaymentScheduleDtls.paymentIssueDate;
  }

  public static Timeline<Number> getAggregateResidenceTimeline(
    final Session session, final List<RuleObject> residencePeriodList,
    final Date toDate) {

    Date calculatedToDate;

    if (null == toDate) {
      final Calendar calendar = Date.getCurrentDate().getCalendar();
      calendar.add(Calendar.YEAR, 1);
      calculatedToDate = new Date(calendar);
    } else {
      calculatedToDate = toDate;
    }

    return new BDMOASAggregateYearCalculator(residencePeriodList,
      calculatedToDate).calculate();

  }

  public static Date getStartOfMonth(final Session session, final Date date)
    throws AppException, InformationalException {

    final Calendar calendar = date.getCalendar();
    calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));

    return new Date(calendar);

  }

  public static Date getEndOfMonth(final Session session, final Date date)
    throws AppException, InformationalException {

    final Calendar calendar = date.getCalendar();
    calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));

    return new Date(calendar);

  }

  public static Number getTotalTrueMonths(final Session session,
    final Timeline<Boolean> timeline) {

    Long totalTrueMonths = 0L;

    for (final BoundedInterval<Boolean> interval : timeline.intervals()) {
      if (interval.value() && interval.endDate() != null) {
        final Calendar startDateCalendar = interval.startDate().getCalendar();
        final Calendar endDateCalendar = interval.endDate().getCalendar();
        endDateCalendar.add(Calendar.DATE, 1);
        final LocalDate localStartDate =
          LocalDateTime.ofInstant(startDateCalendar.toInstant(),
            startDateCalendar.getTimeZone().toZoneId()).toLocalDate();
        final LocalDate localEndDate =
          LocalDateTime.ofInstant(endDateCalendar.toInstant(),
            endDateCalendar.getTimeZone().toZoneId()).toLocalDate();
        totalTrueMonths +=
          ChronoUnit.MONTHS.between(localStartDate, localEndDate);
      }
    }

    return totalTrueMonths;

  }

  public static Timeline<Boolean> getSubsequentMonthTrueTimeline(
    final Session session, final Timeline<Boolean> timeline) {

    final List<Interval<Boolean>> intervals = new LinkedList<>();
    final Iterator<? extends BoundedInterval<Boolean>> iterator =
      timeline.intervals().iterator();

    intervals.add(iterator.next());

    final Map<Date, Boolean> dateValueMap = new HashMap<>();

    while (iterator.hasNext()) {

      final Interval<Boolean> interval = iterator.next();
      final Date startOfNextMonth =
        BDMDateUtil.getStartOfNextMonth(interval.startDate());

      if (interval.value() && timeline.valueOn(startOfNextMonth)) {
        dateValueMap.put(startOfNextMonth, interval.value());
      }

    }

    dateValueMap.entrySet().forEach(entry -> {
      final Interval<Boolean> interval =
        new Interval<>(entry.getKey(), entry.getValue());
      intervals.add(interval);
    });

    return new Timeline<Boolean>(intervals);

  }

  // BEGIN : 106244 NRT changes

  /**
   * Method will check if the Overlap exists NRT Correction
   * evidence start and end date and any existing NRT deductions.
   * If there are any overlap exits then return true else return false.
   *
   * @param session
   * CREOLE Session
   * @param caseID
   * Product Delivery Case ID
   * @param nrtCorrectionStart
   * NRT Correction Evidence Start Date
   * @param nrtCorrectionEndDate
   * NRT Correction Evidence End Date
   * @return
   * <code>true</code> if the overlap exists else <code>false</code>
   * @throws AppException
   * Application Exception.
   * @throws InformationalException
   * Informational Exception.
   */
  public static boolean isNRTCorrectionAndDeductionOverlap(
    final Session session, final Number caseID, final Date nrtCorrectionStart,
    final Date nrtCorrectionEndDate)
    throws AppException, InformationalException {

    final BDMOASMaintainDeduction bdmOASMaintainDeduction =
      new BDMOASMaintainDeduction();

    // Get the Overlapping Deductions list.
    final BDMCurrentDeductionsForProductDeliveryList bdmCurrentDeductionsForProductDeliveryList =
      bdmOASMaintainDeduction.getNRTOverlappingDeductions(caseID.longValue(),
        nrtCorrectionStart, nrtCorrectionEndDate);

    if (bdmCurrentDeductionsForProductDeliveryList != null
      && bdmCurrentDeductionsForProductDeliveryList.dtls
        .size() > CuramConst.gkZero) {

      // Overlap exists
      return true;
    }

    // No overlap exists
    return false;

  }
  // END : 106244 NRT Changes

  // BEGIN 119238 DEV: Implement Conditional Verification RuleSet
  /**
   * Checks if IC legal status verification is required.
   *
   * @param session the session
   * @param evidenceDescriptorID the evidence descriptor ID
   * @return the boolean
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  public static Boolean isICLegalStatusVerificationRequired(
    final Session session, final Number evidenceDescriptorID)
    throws AppException, InformationalException {

    return BDMOASEvidenceUtil
      .isLegalStatusVerificationRequired(evidenceDescriptorID.longValue());
  }
  // END 119238 DEV: Implement Conditional Verification RuleSet

  // BEGIN 103431 DEV: Implement OAS RI Non Filer Verification
  /**
   * This method validates if the year is valid, and
   * is less than the current year and greater than
   * 1957.
   *
   * @param session
   * CREOLE session.
   * @param year
   * year
   * @return
   * True if the valid else false.
   */
  public static boolean isValidTaxYear(final Session session,
    final String year) {

    if (isValidYear(session, year)) {

      // Convert year to Integer
      final Integer enteredYearValue = Integer.valueOf(year);

      if (enteredYearValue > Integer.valueOf("1957")
        && enteredYearValue < Date.getCurrentDate().getCalendar()
          .get(Calendar.YEAR)) {

        return true;
      }
    }
    return false;
  }

  // BEGIN 121703 DEV: Implement Conditional Verification Ruleset
  /**
   * Checks if is similar address by comparing other address data.
   *
   * @param session the session
   * @param firstAddressID the first address ID
   * @param secondAddressID the second address ID
   * @return true, if is similar address by address I ds
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  public static boolean isSimilarAddressByAddressIDs(final Session session,
    final Number firstAddressID, final Number secondAddressID)
    throws AppException, InformationalException {

    final AddressData addressDataObj = AddressDataFactory.newInstance();

    final Address addressObj = AddressFactory.newInstance();

    final AddressKey addressKey = new AddressKey();

    addressKey.addressID = firstAddressID.longValue();
    final OtherAddressData firstOtherAddressData =
      addressObj.readAddressData(addressKey);

    addressKey.addressID = secondAddressID.longValue();
    final OtherAddressData secondOtherAddressData =
      addressObj.readAddressData(addressKey);

    return !addressDataObj.hasAddressDataChanged(firstOtherAddressData,
      secondOtherAddressData).addressChangedInd;
  }
  // END 121703 DEV: Implement Conditional Verification Ruleset
}
