package curam.ca.gc.bdm.sl.bdmcaseurgentflag.impl;

import com.google.inject.AbstractModule;
import curam.ca.gc.bdm.sl.bdmcaseurgentflag.intf.BDMMaintainCaseUrgentFlag;

public class BDMMaintainCaseUrgentFlagModule extends AbstractModule {

  @Override
  protected void configure() {

    bind(BDMMaintainCaseUrgentFlag.class).to(
      curam.ca.gc.bdm.sl.bdmcaseurgentflag.impl.BDMMaintainCaseUrgentFlag.class);
  }

}
