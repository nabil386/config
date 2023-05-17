package curam.ca.gc.bdm.evidence.sl.impl;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import curam.ca.gc.bdm.evidence.events.impl.BDMEvidenceControllerEventDispatcher;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;

/**
 * Module for binding evidence event handlers.
 */
public class BDMEvidenceModule extends AbstractModule {

  @Override
  protected void configure() {

    super.configure();

    this.bindEvidenceControllerEvents();

  }

  /**
   * Binds a common Evidence Controller Interface event dispatcher.
   */
  private void bindEvidenceControllerEvents() {

    final Multibinder<EvidenceControllerInterface.EvidencePreActivationEvents> preActivationEvents =
      Multibinder.newSetBinder(binder(),
        EvidenceControllerInterface.EvidencePreActivationEvents.class);

    final Multibinder<EvidenceControllerInterface.EvidenceActivationEvents> activationEvents =
      Multibinder.newSetBinder(binder(),
        EvidenceControllerInterface.EvidenceActivationEvents.class);

    final Multibinder<EvidenceControllerInterface.EvidencePreModifyEvent> preModifyEvents =
      Multibinder.newSetBinder(binder(),
        EvidenceControllerInterface.EvidencePreModifyEvent.class);

    final Multibinder<EvidenceControllerInterface.EvidenceValidationEvents> validationEvents =
      Multibinder.newSetBinder(binder(),
        EvidenceControllerInterface.EvidenceValidationEvents.class);

    preActivationEvents.addBinding()
      .to(BDMEvidenceControllerEventDispatcher.class);
    activationEvents.addBinding()
      .to(BDMEvidenceControllerEventDispatcher.class);
    preModifyEvents.addBinding()
      .to(BDMEvidenceControllerEventDispatcher.class);
    validationEvents.addBinding()
      .to(BDMEvidenceControllerEventDispatcher.class);

  }

}
