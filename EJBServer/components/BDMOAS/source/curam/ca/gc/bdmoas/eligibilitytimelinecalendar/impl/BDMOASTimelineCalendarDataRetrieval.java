package curam.ca.gc.bdmoas.eligibilitytimelinecalendar.impl;

import curam.core.sl.util.timelinecalendar.impl.TimelineCalendarIntervalValue;
import curam.core.sl.util.timelinecalendar.impl.TimelineCalendarRow;
import curam.core.sl.util.timelinecalendar.impl.TimelineCalendarTimeline;
import curam.creole.value.Interval;
import curam.creole.value.Timeline;
import curam.eligibilitytimelinecalendar.impl.TimelineCalendarDataRetrieval;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import java.util.LinkedList;
import java.util.List;

public class BDMOASTimelineCalendarDataRetrieval
  implements TimelineCalendarDataRetrieval {

  private static final String OAS = "OAS";

  private static final String OAS_INACTIVE = "OAS_INACTIVE";

  @Override
  public List<TimelineCalendarRow> retrieveData(final long integratedCaseID,
    final int year) throws AppException, InformationalException {

    final List<TimelineCalendarRow> rows = new LinkedList<>();

    final TimelineCalendarRow oasRow = new TimelineCalendarRow();
    oasRow.id = OAS;
    oasRow.title = OAS;

    final TimelineCalendarTimeline oasTimeline =
      new TimelineCalendarTimeline();
    final TimelineCalendarIntervalValue oasIntervalValue =
      new TimelineCalendarIntervalValue();
    oasIntervalValue.id = OAS;
    oasIntervalValue.title = OAS;

    final List<Interval<TimelineCalendarIntervalValue>> oasIntervalList =
      new LinkedList<>();
    final Interval<TimelineCalendarIntervalValue> oasInterval =
      new Interval<TimelineCalendarIntervalValue>(
        Date.fromISO8601("20220501"), oasIntervalValue);

    oasIntervalList
      .add(new Interval<TimelineCalendarIntervalValue>(null, null));
    oasIntervalList.add(oasInterval);

    oasTimeline.timeline = new Timeline<>(oasIntervalList);

    oasRow.timelines.add(oasTimeline);

    rows.add(oasRow);

    return rows;
  }

}
