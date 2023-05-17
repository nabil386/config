package curam.ca.gc.bdmoas.rules.impl;

import curam.creole.execution.RuleObject;
import curam.creole.value.Interval;
import curam.creole.value.Timeline;
import curam.util.type.Date;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class BDMOASAggregateYearCalculator {

  private static final String START_DATE = "startDate";

  private static final String END_DATE = "endDate";

  private final List<RuleObject> residencePeriodList;

  private final List<Interval<Number>> intervals =
    new LinkedList<Interval<Number>>();

  private final Date toDate;

  private long aggregateYears = 0;

  private long accruedDays = 0;

  public BDMOASAggregateYearCalculator(
    final List<RuleObject> residencePeriodList, final Date toDate) {

    this.residencePeriodList = residencePeriodList;
    this.toDate = toDate;
    this.intervals.add(new Interval<Number>(null, 0));

  }

  public Timeline<Number> calculate() {

    for (final RuleObject residencePeriod : this.residencePeriodList) {

      final Date startDate =
        (Date) residencePeriod.getAttributeValue(START_DATE).getValue();
      final Date endDate =
        (Date) residencePeriod.getAttributeValue(END_DATE).getValue();

      if (startDate.after(this.toDate)) {
        continue;
      }

      final BDMOASResidenceInterval residenceInterval =
        new BDMOASResidenceInterval(startDate, endDate);

      if (residenceInterval.isEmpty()) {
        continue;
      }

      if (residenceInterval.isPartialYear()) {
        this.accruedDays = residenceInterval.getDays();
      } else {
        this.processMultiYear(residenceInterval);
      }

    }

    return new Timeline<>(this.intervals);

  }

  private void
    processMultiYear(final BDMOASResidenceInterval residenceInterval) {

    Date startDate = residenceInterval.getStartDate();

    while (startDate.before(residenceInterval.getEndDate())) {

      if (40 == this.aggregateYears) {
        break;
      }

      boolean isFinal = false;

      final Calendar calendar = startDate.getCalendar();
      calendar.add(Calendar.YEAR, 1);
      calendar.add(Calendar.DATE, -1);

      Date endDate = new Date(calendar);

      if (endDate.after(this.toDate)
        || endDate.after(residenceInterval.getEndDate())) {
        endDate = this.toDate;
        isFinal = true;
      }

      if (endDate.after(residenceInterval.getEndDate()) || isFinal) {
        final long days =
          ChronoUnit.DAYS.between(residenceInterval.getLocalDate(startDate),
            residenceInterval.getLocalEndDate());
        this.accruedDays = this.accruedDays + days;
      } else {
        this.aggregateYears = this.aggregateYears + 1;
        final Calendar updateStartDateCalendar = endDate.getCalendar();
        updateStartDateCalendar.add(Calendar.DATE, 1);
        startDate = new Date(updateStartDateCalendar);
        final Interval<Number> interval =
          new Interval<Number>(startDate, this.aggregateYears);
        this.intervals.add(interval);
      }
      if (isFinal) {
        break;
      }

    }

  }

}
