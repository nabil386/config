package curam.ca.gc.bdm.sl.productdelivery.impl;

import com.google.inject.AbstractModule;
import curam.ca.gc.bdm.sl.productdelivery.intf.MaintainDeductionDetails;

public class BDMMaintainDeductionDetailsModule extends AbstractModule {

  @Override
  protected void configure() {

    bind(MaintainDeductionDetails.class).to(
      curam.ca.gc.bdm.sl.productdelivery.impl.MaintainDeductionDetails.class);
  }

}
