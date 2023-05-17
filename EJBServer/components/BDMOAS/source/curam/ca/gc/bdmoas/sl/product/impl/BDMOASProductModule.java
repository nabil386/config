package curam.ca.gc.bdmoas.sl.product.impl;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import curam.ca.gc.bdmoas.eligibilitytimelinecalendar.impl.BDMOASTimelineCalendarDataRetrieval;
import curam.ca.gc.bdmoas.eligibilitytimelinecalendar.impl.BDMOASTimelineCalendarKeyEventsDataRetrieval;
import curam.codetable.CASECATTYPECODE;
import curam.core.sl.util.timelinecalendar.impl.TimelineCalendarKeyEventsDataRetrival;
import curam.eligibilitytimelinecalendar.impl.TimelineCalendarDataRetrieval;

public class BDMOASProductModule extends AbstractModule {

  @Override
  protected void configure() {

    this.bindTimelineCalendar();

  }

  private void bindTimelineCalendar() {

    MapBinder
      .newMapBinder(binder(), String.class,
        TimelineCalendarDataRetrieval.class)
      .addBinding(CASECATTYPECODE.OAS_OLD_AGE_SECURITY)
      .to(BDMOASTimelineCalendarDataRetrieval.class);

    MapBinder
      .newMapBinder(binder(), String.class,
        TimelineCalendarKeyEventsDataRetrival.class)
      .addBinding(CASECATTYPECODE.OAS_OLD_AGE_SECURITY)
      .to(BDMOASTimelineCalendarKeyEventsDataRetrieval.class);

  }

}
