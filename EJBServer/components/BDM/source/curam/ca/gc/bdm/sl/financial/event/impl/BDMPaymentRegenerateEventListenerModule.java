package curam.ca.gc.bdm.sl.financial.event.impl;

import com.google.inject.AbstractModule;
import curam.core.sl.event.impl.PaymentRegenerateEventListener;

public class BDMPaymentRegenerateEventListenerModule extends AbstractModule {

  @Override
  protected void configure() {

    bind(PaymentRegenerateEventListener.class)
      .to(BDMPaymentRegenerateEventListener.class);
  }

}
