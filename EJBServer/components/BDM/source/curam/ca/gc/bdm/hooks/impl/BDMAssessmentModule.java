package curam.ca.gc.bdm.hooks.impl;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import curam.core.sl.infrastructure.assessment.event.impl.AssessmentEngineEvent;

public class BDMAssessmentModule extends AbstractModule {

  @Override
  protected void configure() {

    /**
     * Bind assessment engine hook
     */
    final Multibinder<AssessmentEngineEvent> assessmentEngineEvents =
      Multibinder.newSetBinder(binder(), AssessmentEngineEvent.class);

    assessmentEngineEvents.addBinding()
      .to(BDMAssessmentEngineEventListener.class);

  }
}
