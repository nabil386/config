package curam.ca.gc.bdm.citizenmessages.impl;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import curam.citizenmessages.listeners.internal.impl.PaymentInstrumentEventListener;
import curam.participantmessages.events.impl.CitizenMessagesEvent;

public class BDMCitizenMessagesModule extends AbstractModule {

  @Override
  protected void configure() {

    /**
     * Bind in a Citizen Messages Event to add & extend current UA messages
     *
     */
    final Multibinder<CitizenMessagesEvent> citizenMessagesEventBinder =
      Multibinder.newSetBinder(binder(), CitizenMessagesEvent.class);
    citizenMessagesEventBinder.addBinding()
      .to(BDMCitizenMessagesEventListener.class);

    citizenMessagesEventBinder.addBinding()
      .to(BDMDynamicUnreadCorrespondenceEventListener.class);

    citizenMessagesEventBinder.addBinding()
      .to(BDMCitizenMessageClaimEstablishDeniedEventListener.class);

    citizenMessagesEventBinder.addBinding()
      .to(BDMCitizenMessagesRequestForCallBackEventListener.class);

    // BEGIN TASK 19912: Customizing for GC notify call.
    /**
     * binds payment instrument event listener.
     */
    bind(PaymentInstrumentEventListener.class)
      .to(BDMPaymentInstrumentEventListener.class);
    // END TASK 19912:

  }

}
