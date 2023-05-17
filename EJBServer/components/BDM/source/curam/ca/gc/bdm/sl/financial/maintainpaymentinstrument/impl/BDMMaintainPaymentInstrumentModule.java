package curam.ca.gc.bdm.sl.financial.maintainpaymentinstrument.impl;

import com.google.inject.AbstractModule;
import curam.ca.gc.bdm.sl.financial.maintainpaymentinstrument.intf.MaintainPaymentInstrument;

public class BDMMaintainPaymentInstrumentModule extends AbstractModule {

  @Override
  protected void configure() {

    bind(MaintainPaymentInstrument.class).to(
      curam.ca.gc.bdm.sl.financial.maintainpaymentinstrument.impl.MaintainPaymentInstrument.class);
  }

}
