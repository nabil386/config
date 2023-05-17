package curam.ca.gc.bdm.pdc.impl;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;
import curam.ca.gc.bdm.util.integrity.impl.BDMAddressPerBenefitTypeCalculatorBase;
import curam.ca.gc.bdm.util.integrity.impl.BDMAddressPerBenefitTypeCalculatorDefaultImpl;
import curam.pdc.impl.PDCPhoneNumberReplicatorExtender;

public class BDMPDCModule extends AbstractModule {

  /**
   * Module class for configuring replicator extender to add custom logic
   */
  @Override
  public void configure() {

    // PHONENUMBER
    final Multibinder<PDCPhoneNumberReplicatorExtender> customPhoneNumberReplicatorExtenderImpl =
      Multibinder.newSetBinder(binder(),
        PDCPhoneNumberReplicatorExtender.class);

    customPhoneNumberReplicatorExtenderImpl.addBinding()
      .to(BDMPDCPhoneNumberReplicatorExtenderImpl.class);

    // PHONENUMBER

    // Task 21121 Phone number evidence
    bind(curam.pdc.impl.PDCPhoneNumberReplicator.class)
      .to(BDMPDCPhoneNumberReplicatorImpl.class);

    // default Benefit Imple
    final MapBinder<Long, BDMProductDetailsCalculatorBase> productDetailsCalculatorMap =
      MapBinder.newMapBinder(binder(), Long.class,
        BDMProductDetailsCalculatorBase.class);
    productDetailsCalculatorMap.addBinding(0l)
      .to(BDMProductDetailsCalculatorDefaultImpl.class);
    // default address integrity binding with product id
    final MapBinder<Long, BDMAddressPerBenefitTypeCalculatorBase> addressIntegrityCalculatorMap =
      MapBinder.newMapBinder(binder(), Long.class,
        BDMAddressPerBenefitTypeCalculatorBase.class);
    addressIntegrityCalculatorMap.addBinding(0l)
      .to(BDMAddressPerBenefitTypeCalculatorDefaultImpl.class);
  }

}
