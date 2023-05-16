package curam.ca.gc.bdm.application.impl;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;
import curam.ca.gc.bdm.sl.application.impl.BDMDefaultProgramAuthorisation;
import curam.ca.gc.bdm.sl.application.impl.BDMProgramAuthorisation;
import curam.citizenworkspace.impl.StartIntakeEvents;
import curam.commonintake.authorisation.impl.ProgramAuthorisationEvents;
import curam.commonintake.impl.ProgramReopenEvents;
import curam.intake.infrastructure.impl.ApplicationTaskSearchImpl;
import curam.workspaceservices.applicationprocessing.impl.ConcernRoleMappingStrategy;
import curam.workspaceservices.applicationprocessing.impl.SubmitApplicationEvents;
import curam.workspaceservices.intake.impl.IntakeApplication;
import curam.workspaceservices.intake.impl.IntakeApplication.IntakeApplicationEvents;
import curam.workspaceservices.intake.impl.IntakeApplicationMappingEvents;
import curam.workspaceservices.intake.impl.IntakeProgramApplication;

public class BDMApplicationModule extends AbstractModule {

  @Override
  protected void configure() {

    /**
     * Bind in a submit application event to perform some pre/post mapping
     * during the application submission process
     */
    final Multibinder<SubmitApplicationEvents> submitApplicationEventsBinder =
      Multibinder.newSetBinder(binder(), SubmitApplicationEvents.class);
    submitApplicationEventsBinder.addBinding()
      .to(BDMSubmitApplicationEventsListener.class);

    /**
     * Bind in an intake application mapping event to perform some pre mapping
     * during the application submission process.
     */
    final Multibinder<IntakeApplicationMappingEvents> intakeApplicationMappingEvents =
      Multibinder.newSetBinder(binder(),
        IntakeApplicationMappingEvents.class);

    intakeApplicationMappingEvents.addBinding()
      .to(BDMIntakeApplicationMappingEvents.class);

    /**
     * Bind in the Intake Program Application Events.
     */
    final Multibinder<IntakeProgramApplication.IntakeProgramApplicationEvents> intakeProgramApplicationEventListeners =
      Multibinder.newSetBinder(binder(),
        new TypeLiteral<IntakeProgramApplication.IntakeProgramApplicationEvents>() { /**/
        });
    intakeProgramApplicationEventListeners.addBinding()
      .to(BDMIntakeProgramApplicationEventsListener.class);

    /**
     * binds concern role mapping
     */
    bind(ConcernRoleMappingStrategy.class)
      .to(BDMConcernRoleMappingStrategyImpl.class);

    /**
     * Bind in the Start Intake Events.
     */
    final Multibinder<StartIntakeEvents> StartIntakeEventsBinder =
      Multibinder.newSetBinder(binder(), StartIntakeEvents.class);

    StartIntakeEventsBinder.addBinding()
      .to(BDMStartIntakeEventsListener.class);

    /**
     * Bind in the program reopen Events.
     */
    final Multibinder<ProgramReopenEvents> programReopenEvents =
      Multibinder.newSetBinder(binder(), ProgramReopenEvents.class);

    programReopenEvents.addBinding().to(BDMProgramReopenEvents.class);

    /**
     * Bind intake events to map post mapping of evidences
     */
    final Multibinder<IntakeApplicationEvents> intakeEventBinder =
      Multibinder.newSetBinder(binder(),
        IntakeApplicationEvents.class);

    intakeEventBinder.addBinding().to(BDMIntakeApplicationEvents.class);

    // BEGIN TASK-17646 - Bind ProgramAuthorisationEvents
    final Multibinder<ProgramAuthorisationEvents> programApplicaionEventsBinder =
      Multibinder.newSetBinder(binder(), ProgramAuthorisationEvents.class);
    programApplicaionEventsBinder.addBinding()
      .to(BDMProgramAuthorisationEventListener.class);
    // END TASK-17646

    // the default implementation for the BDM application case authorisation
    final Long defaultApplicationCaseAdminID = 0l;

    final MapBinder<Long, BDMProgramAuthorisation> programAuthorisationMapBinder =
      MapBinder.newMapBinder(binder(), Long.class,
        BDMProgramAuthorisation.class);

    programAuthorisationMapBinder.addBinding(defaultApplicationCaseAdminID)
      .to(BDMDefaultProgramAuthorisation.class);

    // Binding Application Task Search to new implementation in BDM

    bind(ApplicationTaskSearchImpl.class)
      .to(BDMApplicationTaskSearchImpl.class);

  }

}
