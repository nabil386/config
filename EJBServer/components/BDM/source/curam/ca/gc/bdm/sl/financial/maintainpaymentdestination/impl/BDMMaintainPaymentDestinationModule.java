package curam.ca.gc.bdm.sl.financial.maintainpaymentdestination.impl;

import com.google.inject.AbstractModule;
import curam.ca.gc.bdm.sl.financial.maintainpaymentdestination.intf.BDMMaintainPaymentDestination;

public class BDMMaintainPaymentDestinationModule extends AbstractModule {

  @Override
  protected void configure() {

    bind(BDMMaintainPaymentDestination.class).to(
      curam.ca.gc.bdm.sl.financial.maintainpaymentdestination.impl.BDMMaintainPaymentDestination.class);
  }

}
