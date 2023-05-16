package curam.ca.gc.bdmoas.rules.impl;

import curam.util.type.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

public class BDMOASResidenceInterval {

  private final Date startDate;

  private final Date endDate;

  private final LocalDate localStartDate;

  private final LocalDate localEndDate;

  private final long years;

  private final long days;

  public BDMOASResidenceInterval(final Date startDate, final Date endDate) {

    this.startDate = startDate;

    if (null == endDate || Date.kZeroDate.equals(endDate)) {
      final Calendar calendar = Date.getCurrentDate().getCalendar();
      calendar.add(Calendar.YEAR, 2);
      this.endDate = new Date(calendar);
    } else {
      this.endDate = endDate;
    }

    this.localStartDate = this.getLocalDate(startDate);
    this.localEndDate = this.getLocalDate(this.endDate);

    this.years = ChronoUnit.YEARS.between(localStartDate, localEndDate);

    if (this.startDate.equals(this.endDate)) {
      if (this.isLeapYear()
        && this.getFeb28th(this.endDate).compareTo(this.endDate) > 0) {
        this.days = 0;
      } else {
        this.days = 1;
      }
    } else {
      final long days =
        ChronoUnit.DAYS.between(this.localStartDate, this.localEndDate);
      if (this.isLeapYear()
        && this.getFeb28th(this.endDate).compareTo(this.endDate) > 0) {
        this.days = days - 1;
      } else {
        this.days = days;
      }
    }

  }

  public LocalDate getLocalDate(final Date date) {

    final Calendar calendar = date.getCalendar();

    return LocalDateTime
      .ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId())
      .toLocalDate();

  }

  private Date getFeb28th(final Date date) {

    final Calendar calendar = date.getCalendar();
    calendar.set(Calendar.MONTH, Calendar.FEBRUARY);
    calendar.set(Calendar.DATE, 27);
    return new Date(calendar);

  }

  private boolean isLeapYear() {

    final Calendar calendar = this.endDate.getCalendar();
    return calendar.get(Calendar.YEAR) % 4 == 0;

  }

  public boolean isEmpty() {

    return this.years == 0 && this.days == 0;

  }

  public boolean isPartialYear() {

    return this.years == 0 && this.days > 0;

  }

  public long getDays() {

    return this.days;

  }

  public Date getStartDate() {

    return this.startDate;
  }

  public Date getEndDate() {

    return this.endDate;

  }

  public LocalDate getLocalEndDate() {

    return this.localEndDate;

  }

}
