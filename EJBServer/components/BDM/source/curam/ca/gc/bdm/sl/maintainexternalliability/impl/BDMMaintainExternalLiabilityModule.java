package curam.ca.gc.bdm.sl.maintainexternalliability.impl;

import com.google.inject.AbstractModule;
import curam.ca.gc.bdm.sl.maintainexternalliability.intf.MaintainExternalLiability;

public class BDMMaintainExternalLiabilityModule extends AbstractModule {

  @Override
  protected void configure() {

    bind(MaintainExternalLiability.class).to(
      curam.ca.gc.bdm.sl.maintainexternalliability.impl.MaintainExternalLiability.class);
  }

}
