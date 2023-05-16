package curam.ca.gc.bdmoas.sl.organization.impl;

import curam.ca.gc.bdm.entity.fact.BDMSSACountryAgreementFactory;
import curam.ca.gc.bdm.entity.intf.BDMSSACountryAgreement;
import curam.ca.gc.bdm.entity.struct.BDMCountryAgrmntDetails;
import curam.ca.gc.bdm.entity.struct.BDMCountryCodeAndStatusKey;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASResidencePeriodEvidenceVO;
import curam.codetable.RECORDSTATUS;
import curam.core.impl.CuramConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.MultipleRecordException;
import curam.util.exception.RecordNotFoundException;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apache.xerces.impl.xpath.regex.RegularExpression;

/**
 * Utility class to house common functions specific to Office of Control case
 * ownership assignment.
 *
 * @author abid.a.khan
 *
 */
final class BDMOASOfficeOfControlHelper {

  private BDMOASOfficeOfControlHelper() {

    // prevent instantiation
  }

  /**
   * @param postalCode
   * Postal Code
   *
   * @return true if postal code matches pattern
   */
  public static boolean isValidPostalCode(final String postalCode) {

    // CA postal code regular expression.
    final RegularExpression regExpWithSpace =
      new RegularExpression(CuramConst.gkCanadianREWithSpace);
    final RegularExpression regExpWithoutSpace =
      new RegularExpression(CuramConst.gkCanadianRE);

    return regExpWithSpace.matches(postalCode)
      || regExpWithoutSpace.matches(postalCode);

  }

  /**
   * Given a Map<K, V>, and a value,
   * finds whether or not the given value exists in the map.
   *
   * @param map
   * @param value
   * @return
   */
  public static <K, V> boolean containsValue(final Map<K, V> map,
    final V value) {

    final Set<V> valuesSet = new HashSet<>(map.values());
    return valuesSet.contains(value);
  }

  /**
   * Finds entry with longest residency period.
   * If more than one match the same longest period, returns first entry in
   * alphabetical order.
   *
   * @param map
   * @return
   */
  public static Map.Entry<String, Integer>
    getEntryHavingMaxValue(final Map<String, Integer> map) {

    int maxValue = Integer.MIN_VALUE;
    Map.Entry<String, Integer> entryWithMaxValue = null;

    for (final Map.Entry<String, Integer> entry : map.entrySet()) {
      if (entry.getValue() > maxValue) {
        maxValue = entry.getValue();
        entryWithMaxValue = entry;
      } else if (entry.getValue() == maxValue
        && Objects.nonNull(entryWithMaxValue)
        && entry.getKey().compareTo(entryWithMaxValue.getKey()) < 0) {
        entryWithMaxValue = entry;
      }
    }
    return entryWithMaxValue;
  }

  public static Map<String, Integer> getSSACountryPeriodMapFromResidencyList(
    final List<BDMOASResidencePeriodEvidenceVO> bdmoasResPeriodList)
    throws AppException, InformationalException {

    final Map<String, Integer> countryToPeriodMap = new HashMap<>();

    for (final BDMOASResidencePeriodEvidenceVO residPeriodEvid : bdmoasResPeriodList) {

      final BDMCountryAgrmntDetails readSSACountryAgreementDetails =
        readSSACountryAgreementDetails(residPeriodEvid.getCountry());
      if (readSSACountryAgreementDetails.ssaCntryAgrmntID != 0) {
        if (countryToPeriodMap.containsKey(residPeriodEvid.getCountry())) {
          countryToPeriodMap.computeIfPresent(residPeriodEvid.getCountry(),
            (k, v) -> v + getResidencePeriodByCountry(bdmoasResPeriodList,
              residPeriodEvid.getCountry()));
        } else {
          countryToPeriodMap.put(residPeriodEvid.getCountry(),
            getResidencePeriodByCountry(bdmoasResPeriodList,
              residPeriodEvid.getCountry()));
        }
      }
    }

    return countryToPeriodMap;
  }

  public static BDMCountryAgrmntDetails readSSACountryAgreementDetails(
    final String countryCode) throws AppException, InformationalException {

    final BDMSSACountryAgreement ssaCntryObj =
      BDMSSACountryAgreementFactory.newInstance();

    final BDMCountryCodeAndStatusKey countryCodeStatusKey =
      new BDMCountryCodeAndStatusKey();
    countryCodeStatusKey.countryCode = countryCode.toUpperCase();
    countryCodeStatusKey.recordStatus = RECORDSTATUS.NORMAL;

    BDMCountryAgrmntDetails ssaCntryAgrmntDetails = null;

    try {

      ssaCntryAgrmntDetails =
        ssaCntryObj.readAgrmntByCountryCodeAndStatus(countryCodeStatusKey);
    } catch (final RecordNotFoundException
      | MultipleRecordException recNotFnExc) {
      // Do nothing.
      ssaCntryAgrmntDetails = new BDMCountryAgrmntDetails();
    }

    return ssaCntryAgrmntDetails;
  }

  /**
   * Finds number of years of residence in a given country
   *
   * @param list residence period list
   * @param country country code for which residence period needs to be
   * determined.
   * @return
   */
  public static int getResidencePeriodByCountry(
    final List<BDMOASResidencePeriodEvidenceVO> list, final String country) {

    int totalYears = 0;

    for (final BDMOASResidencePeriodEvidenceVO obj : list) {
      if (obj.getCountry().equals(country)) {

        final java.util.Calendar calendarStart =
          obj.getStartDate().getCalendar();
        final java.util.Calendar calendarEnd = obj.getEndDate().getCalendar();

        final int years =
          Period
            .between(
              LocalDateTime.ofInstant(calendarStart.toInstant(),
                calendarStart.getTimeZone().toZoneId()).toLocalDate(),
              LocalDateTime.ofInstant(calendarEnd.toInstant(),
                calendarEnd.getTimeZone().toZoneId()).toLocalDate())
            .getYears();
        totalYears += years;
      }
    }
    return totalYears;
  }

}
