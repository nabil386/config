package curam.ca.gc.bdmoas.application.impl;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import curam.ca.gc.bdm.application.impl.BDMConcernRoleMappingStrategyImpl;
import curam.ca.gc.bdm.application.impl.BDMStartIntakeEventsListener;
import curam.workspaceservices.applicationprocessing.impl.SubmitApplicationEvents;
import curam.workspaceservices.intake.impl.IntakeApplication;
import curam.workspaceservices.intake.impl.IntakeApplication.IntakeApplicationEvents;
import curam.workspaceservices.intake.impl.IntakeApplicationMappingEvents;

/**
 * BDMOAS FEATURE 92921: Class Added.
 * Bind custom implementations to OOTB interfaces/functionalities.
 *
 * @author SMisal
 *
 */
public class BDMOASApplicationModule extends AbstractModule {

  @Override
  protected void configure() {

    /**
     * Bind in a submit application event to perform some pre/post mapping
     * during the application submission process
     */
    final Multibinder<SubmitApplicationEvents> submitApplicationEventsBinder =
      Multibinder.newSetBinder(binder(), SubmitApplicationEvents.class);

    submitApplicationEventsBinder.addBinding()
      .to(BDMSOASSubmitApplicationEventsListener.class);

    /**
     * Bind in an intake application mapping event to perform some pre mapping
     * during the application submission process.
     */
    final Multibinder<IntakeApplicationMappingEvents> intakeApplicationMappingEvents =
      Multibinder.newSetBinder(binder(),
        IntakeApplicationMappingEvents.class);

    intakeApplicationMappingEvents.addBinding()
      .to(BDMOASIntakeApplicationMappingEvents.class);

    /**
     * Bind in the Start Intake Events.
     */

    bind(BDMStartIntakeEventsListener.class)
      .to(BDMOASStartIntakeEventsListener.class);

    /**
     * Bind intake events to map post mapping of evidences
     */

    final Multibinder<IntakeApplicationEvents> intakeEventBinder =
      Multibinder.newSetBinder(binder(),
        IntakeApplication.IntakeApplicationEvents.class);
    intakeEventBinder.addBinding().to(BDMOASIntakeApplicationEvents.class);

    /**
     * binds concern role mapping
     */
    bind(BDMConcernRoleMappingStrategyImpl.class)
      .to(BDMOASConcernRoleMappingStrategyImpl.class);

  }

}
