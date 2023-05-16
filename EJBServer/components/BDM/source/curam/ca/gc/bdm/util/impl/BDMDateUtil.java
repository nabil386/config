package curam.ca.gc.bdm.util.impl;

import curam.ca.gc.bdm.impl.BDMConstants;
import curam.util.type.Date;
import curam.util.type.DateTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Utility class to handle common logic for Federal data hub services
 *
 *
 * @author NLynch
 *
 */
public class BDMDateUtil {

  /**
   * Change date format from yyyy/MM/dd, MM-dd-yyyy or yyyy-MM-dd to yyyyMMdd
   *
   * @param String
   * inputDate
   * @return String Date in yyyyMMdd format
   * @throws ParseException
   *
   */
  public static String dateFormatter(final String inputDate) {

    String dateFormat = BDMConstants.YYYY_MM_DD_DATE_FORMAT_SLASH_DELIMITER;
    final String yyyyMMddFormatter = BDMConstants.YYYYMMDD_DATE_FORMAT;

    if (inputDate.contains("/") && inputDate.indexOf('/') < 4) {
      dateFormat = BDMConstants.MM_DD_YYYY_DATE_FORMAT_SLASH_DELIMITER;
    } else if (inputDate.contains("-") && inputDate.indexOf('-') < 4) {
      dateFormat = BDMConstants.MM_DD_YYYY_DATE_FORMAT_DASH_DELIMITER;
    } else if (inputDate.contains("-") && inputDate.indexOf('-') == 4) {
      dateFormat = BDMConstants.YYYY_MM_DD_DATE_FORMAT_DASH_DELIMITER;
    }

    final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    // USVI-1614 - MK - Modify Java Date to Curam Date
    java.util.Date d = DateTime.getCurrentDateTime().getCalendar().getTime();
    try {
      d = sdf.parse(inputDate);
    } catch (final ParseException e) {
      e.printStackTrace();
    }
    sdf.applyPattern(yyyyMMddFormatter);
    final String s = sdf.format(d);
    return s;
  }

  /*
   * To form the requisition number require to append the due date in YYMM
   * format
   */

  public static String dateFormatToYYMM(final String inputDate) {

    String dateFormat = BDMConstants.YYYY_MM_DD_DATE_FORMAT_SLASH_DELIMITER;
    final String yyMMFormatter = "YYMM";

    if (inputDate.contains("/") && inputDate.indexOf('/') < 4) {
      dateFormat = BDMConstants.MM_DD_YYYY_DATE_FORMAT_SLASH_DELIMITER;
    } else if (inputDate.contains("-") && inputDate.indexOf('-') < 4) {
      dateFormat = BDMConstants.MM_DD_YYYY_DATE_FORMAT_DASH_DELIMITER;
    } else if (inputDate.contains("-") && inputDate.indexOf('-') == 4) {
      dateFormat = BDMConstants.YYYY_MM_DD_DATE_FORMAT_DASH_DELIMITER;
    }

    final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    // USVI-1614 - MK - Modify Java Date to Curam Date
    java.util.Date d = DateTime.getCurrentDateTime().getCalendar().getTime();
    try {
      d = sdf.parse(inputDate);
    } catch (final ParseException e) {
      e.printStackTrace();
    }
    sdf.applyPattern(yyMMFormatter);
    final String s = sdf.format(d);
    return s;
  }

  /**
   * Change date format from yyyy/MM/dd, MM-dd-yyyy or yyyy-MM-dd to other
   * format
   *
   * @param String inputDate
   * @param String formatter
   * @return String Date in other format specified by the input parameter
   * @throws ParseException
   *
   */
  public static String dateFormatter(final String inputDate,
    final String formatter) {

    String dateFormat = BDMConstants.YYYY_MM_DD_DATE_FORMAT_SLASH_DELIMITER;

    if (inputDate.contains("/") && inputDate.indexOf('/') < 4) {
      dateFormat = BDMConstants.MM_DD_YYYY_DATE_FORMAT_SLASH_DELIMITER;
    } else if (inputDate.contains("-") && inputDate.indexOf('-') < 4) {
      dateFormat = BDMConstants.MM_DD_YYYY_DATE_FORMAT_DASH_DELIMITER;
    } else if (inputDate.contains("-") && inputDate.indexOf('-') == 4) {
      dateFormat = BDMConstants.YYYY_MM_DD_DATE_FORMAT_DASH_DELIMITER;
    }

    final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    // USVI-1614 - MK - Modify Java Date to Curam Date
    java.util.Date d = DateTime.getCurrentDateTime().getCalendar().getTime();
    try {
      d = sdf.parse(inputDate);
    } catch (final ParseException e) {
      e.printStackTrace();
    }
    sdf.applyPattern(formatter);
    final String s = sdf.format(d);
    return s;
  }

  /**
   *
   * Get Java Util date by passing date in String format
   *
   * @param date
   * @return
   */

  public static java.util.Date getDate(final String date) {

    final String dateFormat = BDMConstants.YYYYMMDD_DATE_FORMAT;
    final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    java.util.Date d = DateTime.getCurrentDateTime().getCalendar().getTime();
    try {
      d = sdf.parse(date);
    } catch (final ParseException e) {
    }
    return d;
  }

  /**
   * check the overlapping time period
   *
   * @param startDate1
   * @param endDate1
   * @param startDate2
   * @param endDate2
   * @return
   */

  public static boolean checkTimeOverlaps(final java.util.Date startDate1,
    final java.util.Date endDate1, final java.util.Date startDate2,
    final java.util.Date endDate2) {

    if (startDate1 == null || endDate1 == null || startDate2 == null
      || endDate2 == null)
      return false;

    return startDate1.getTime() <= endDate2.getTime()
      && startDate2.getTime() <= endDate1.getTime();
  }

  /**
   * Returns true if the calculation date is between the startDate and endDate.
   * <p>
   * If the specified startDate is zero (Date.isZero()), then it is treated as
   * being infinitely in the past. If the specified endDate is zero, then it is
   * treated as being infinitely in the future.
   * </p>
   * <p>
   * If the specified calculation date is zero, then this method always
   * returns true.
   * <p>
   * <h2>Examples</h2>
   * <table>
   * <tr>
   * <th>calculationDate</th>
   * <th>startDate</th>
   * <th>endDate</th>
   * <th>return</th>
   * </tr>
   * <tr>
   * <td>2015-07-24</td>
   * <td>2015-07-24</td>
   * <td>2015-07-24</td>
   * <td>true</td>
   * </tr>
   * <tr>
   * <td>kZeroDate</td>
   * <td>2015-07-24</td>
   * <td>2015-07-24</td>
   * <td>true</td>
   * </tr>
   * <tr>
   * <td>2015-07-24</td>
   * <td>kZeroDate</td>
   * <td>2015-07-24</td>
   * <td>true</td>
   * </tr>
   * <tr>
   * <td>2015-07-24</td>
   * <td>kZeroDate</td>
   * <td>2015-07-23</td>
   * <td>false</td>
   * </tr>
   * <tr>
   * <td>2015-07-24</td>
   * <td>kZeroDate</td>
   * <td>kZeroDate</td>
   * <td>true</td>
   * </tr>
   * <tr>
   * <td>2015-07-24</td>
   * <td>2015-07-25</td>
   * <td>2015-07-23</td>
   * <td>false</td>
   * </tr>
   * <tr>
   * <td>2015-07-24</td>
   * <td>2015-07-25</td>
   * <td>2015-07-26</td>
   * <td>false</td>
   * </tr>
   * </tr>
   * </table>
   *
   * @param calculationDate
   * @param startDate
   * @param endDate
   * @return
   */
  public static final boolean isDateBetween(final Date calculationDate,
    final Date startDate, final Date endDate) {

    return calculationDate.isZero()
      || (startDate.isZero() || !startDate.after(calculationDate))
        && (endDate.isZero() || !endDate.before(calculationDate));
  }
}
