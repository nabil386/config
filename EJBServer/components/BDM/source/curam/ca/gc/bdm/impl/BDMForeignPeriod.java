/*
 * Foreign contribution period POJO
 * holds the evidence start and date
 * and set the period elements
 */
package curam.ca.gc.bdm.impl;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class BDMForeignPeriod {

  private final String startDate;

  private final String endDate;

  private int yearCount;

  private int monthCount;

  private int dayCount;

  public BDMForeignPeriod(final String startDate, final String endDate) {

    this.startDate = startDate;
    this.endDate = endDate;
  }

  public void setPeriod() {

    final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("yyyyMMdd");
    final LocalDate sDate = LocalDate.parse(startDate, formatter);
    final LocalDate eDate = LocalDate.parse(endDate, formatter);
    final Period period = Period.between(sDate, eDate.plusDays(1));
    yearCount = period.getYears();
    monthCount = period.getMonths();
    dayCount = period.getDays();
  }

  public int getYearCount() {

    return yearCount;
  }

  public int getMonthCount() {

    return monthCount;
  }

  public int getDayCount() {

    return dayCount;
  }

}
