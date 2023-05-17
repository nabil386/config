package curam.ca.gc.bdm.sl.maintaincasedeductions.impl;

import com.google.inject.AbstractModule;

public class BDMMaintainCaseDeductionModule extends AbstractModule {

  @Override
  protected void configure() {

    bind(
      curam.ca.gc.bdm.sl.maintaincasedeductions.intf.MaintainCaseDeductions.class)
        .to(
          curam.ca.gc.bdm.sl.maintaincasedeductions.impl.MaintainCaseDeductions.class);
  }

}
