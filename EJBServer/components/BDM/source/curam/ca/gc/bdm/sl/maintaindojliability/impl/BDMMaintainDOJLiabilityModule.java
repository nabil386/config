package curam.ca.gc.bdm.sl.maintaindojliability.impl;

import com.google.inject.AbstractModule;
import curam.ca.gc.bdm.sl.maintaindojliability.intf.MaintainDOJLiability;

public class BDMMaintainDOJLiabilityModule extends AbstractModule {

  @Override
  protected void configure() {

    bind(MaintainDOJLiability.class).to(
      curam.ca.gc.bdm.sl.maintaindojliability.impl.MaintainDOJLiability.class);
  }

}
