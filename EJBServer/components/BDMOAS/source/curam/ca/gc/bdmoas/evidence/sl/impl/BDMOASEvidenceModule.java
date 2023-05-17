package curam.ca.gc.bdmoas.evidence.sl.impl;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;
import curam.aes.sl.deliveryplan.impl.AESDeliveryPlanFilter;
import curam.ca.gc.bdm.sl.fact.BDMEvidenceControllerHookFactory;
import curam.ca.gc.bdm.sl.impl.BDMEvidenceControllerHookExtensionImpl;
import curam.ca.gc.bdmoas.evidence.events.impl.BDMOASEvidenceControllerEventDispatcher;
import curam.ca.gc.bdmoas.evidence.sharing.impl.BDMOASAESDeliveryPlanFilterImpl;
import curam.codetable.CASECATTYPECODE;
import curam.core.impl.FactoryMethodHelper;
import curam.core.impl.Registrar.RegistrarType;
import curam.core.impl.RegistrarImpl;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import java.lang.reflect.Method;

/**
 * Module for binding evidence event handlers.
 */
public class BDMOASEvidenceModule extends AbstractModule {

  @Override
  protected void configure() {

    super.configure();

    this.bindEvidenceControllerEvents();

    this.bindEvidenceControllerHook();

    // 104240: DEV: Implement evidence brokering
    this.bindAESDeliveryPlanFilter();

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
      .to(BDMOASEvidenceControllerEventDispatcher.class);
    activationEvents.addBinding()
      .to(BDMOASEvidenceControllerEventDispatcher.class);
    preModifyEvents.addBinding()
      .to(BDMOASEvidenceControllerEventDispatcher.class);
    validationEvents.addBinding()
      .to(BDMOASEvidenceControllerEventDispatcher.class);

  }

  private void bindEvidenceControllerHook() {

    final MapBinder<String, Method> evidenceControllerMapBinder =
      MapBinder.newMapBinder(binder(), String.class, Method.class,
        new RegistrarImpl(RegistrarType.EVIDENCE_CONTROLLER_HOOK));

    evidenceControllerMapBinder
      .addBinding(CASECATTYPECODE.OAS_OLD_AGE_SECURITY)
      .toInstance(FactoryMethodHelper
        .getNewInstanceMethod(BDMEvidenceControllerHookFactory.class));

    // Extending BDMEvidenceControllerHookExtensionImpl(BDM component) to
    // add logic for PDC, Application Case as well as OAS IC evidences.
    bind(BDMEvidenceControllerHookExtensionImpl.class)
      .to(BDMOASEvidenceControllerHookExtensionImpl.class);

  }

  /**
   * Bind AES delivery plan filter.
   */
  private void bindAESDeliveryPlanFilter() {

    bind(AESDeliveryPlanFilter.class)
      .to(BDMOASAESDeliveryPlanFilterImpl.class);
  }

}
