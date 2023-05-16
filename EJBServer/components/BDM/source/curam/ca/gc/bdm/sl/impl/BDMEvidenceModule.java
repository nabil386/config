package curam.ca.gc.bdm.sl.impl;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;
import curam.ca.gc.bdm.evidence.impl.BDMDynamicEvidenceTypeImpl;
import curam.ca.gc.bdm.evidence.impl.BDMPDCBankAccountEvidencePopulatorImpl;
import curam.ca.gc.bdm.evidence.impl.BDMPDCBankAccountReplicatorExtenderImpl;
import curam.ca.gc.bdm.sl.fact.BDMEvidenceControllerHookFactory;
import curam.codetable.CASECATTYPECODE;
import curam.codetable.CASETYPECODE;
import curam.core.impl.FactoryMethodHelper;
import curam.core.impl.Registrar.RegistrarType;
import curam.core.impl.RegistrarImpl;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface.EvidenceActivationEvents;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface.EvidencePreActivationEvents;
import curam.evidence.impl.DynamicEvidenceTypeImpl;
import curam.pdc.impl.PDCBankAccountEvidencePopulator;
import curam.pdc.impl.PDCBankAccountReplicatorExtender;
import java.lang.reflect.Method;

/**
 * The custom module class for the BDM evidences implementation bindings.
 *
 */
public class BDMEvidenceModule extends AbstractModule {

  /**
   * binds event listeners or hookpoints so that even handlers become aware that
   * there are more than one even listener.
   */
  @Override
  protected void configure() {

    final MapBinder<String, Method> evidenceControllerMapBinder =
      MapBinder.newMapBinder(binder(), String.class, Method.class,
        new RegistrarImpl(RegistrarType.EVIDENCE_CONTROLLER_HOOK));

    evidenceControllerMapBinder.addBinding(CASETYPECODE.PARTICIPANTDATACASE)
      .toInstance(FactoryMethodHelper
        .getNewInstanceMethod(BDMEvidenceControllerHookFactory.class));

    evidenceControllerMapBinder.addBinding(CASECATTYPECODE.BDM_IC_FEC_PROGRAM)
      .toInstance(FactoryMethodHelper
        .getNewInstanceMethod(BDMEvidenceControllerHookFactory.class));

    final Multibinder<EvidenceControllerInterface.EvidencePreModifyEvent> evidencePreModifyEvent =
      Multibinder.newSetBinder(binder(),
        EvidenceControllerInterface.EvidencePreModifyEvent.class);

    evidencePreModifyEvent.addBinding()
      .to(BDMPDCEvidencePreModifyEvent.class);

    // BEGIN TASK 16366 - Bank Account Evidence, bind the bank account
    // replicator extender to set the value of sort code before creating bank
    // account record.
    final Multibinder<PDCBankAccountReplicatorExtender> pdcBankAccountReplicatorExtenders =
      Multibinder.newSetBinder(binder(),
        PDCBankAccountReplicatorExtender.class);

    pdcBankAccountReplicatorExtenders.addBinding()
      .to(BDMPDCBankAccountReplicatorExtenderImpl.class);
    // END TASK 16366 - Bank Account Evidence

    // BEGIN TASK - 16793 Bank account evidence mapping, bind the bank account
    // evidence populator to set the value of financial institution number and
    // bank transit number from bank sort code.
    final Multibinder<PDCBankAccountEvidencePopulator> pdcBankAccountEvidencePopulator =
      Multibinder.newSetBinder(binder(),
        PDCBankAccountEvidencePopulator.class);

    pdcBankAccountEvidencePopulator.addBinding()
      .to(BDMPDCBankAccountEvidencePopulatorImpl.class);
    // END TASK - 16793 Bank account evidence mapping

    // Start : 17825 Automate VTW
    final Multibinder<EvidenceActivationEvents> bdmEvidenceActivationEventsListener =
      Multibinder.newSetBinder(binder(), EvidenceActivationEvents.class);
    bdmEvidenceActivationEventsListener.addBinding()
      .to(BDMEvidenceActivationEventsListener.class);
    // End : 17825 Automate VTW

    // Start : 17825 Automate VTW
    final Multibinder<EvidencePreActivationEvents> bdmEvidencePreActivationEventsListener =
      Multibinder.newSetBinder(binder(), EvidencePreActivationEvents.class);
    bdmEvidencePreActivationEventsListener.addBinding()
      .to(BDMEvidencePreActivationEventsListener.class);
    // End : 17825 Automate VTW

    // BEGIN Task - 19844 Bank account sort code not updated in evidence entity
    bind(DynamicEvidenceTypeImpl.class).to(BDMDynamicEvidenceTypeImpl.class);
    // END Task - 19844 Bank account sort code

    // BEGIN 90018 DEV: Evidence- creditable residency period in Canada
    evidencePreModifyEvent.addBinding()
      .to(BDMEvidencePreModifyEventsListener.class);
    // END 90018 DEV: Evidence- creditable residency period in Canada
  }
}
