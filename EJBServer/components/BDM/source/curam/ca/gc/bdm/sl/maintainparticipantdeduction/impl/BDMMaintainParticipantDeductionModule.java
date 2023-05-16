package curam.ca.gc.bdm.sl.maintainparticipantdeduction.impl;

import com.google.inject.AbstractModule;
import curam.ca.gc.bdm.sl.maintainparticipantdeduction.intf.MaintainParticipantDeduction;

public class BDMMaintainParticipantDeductionModule extends AbstractModule {

  @Override
  protected void configure() {

    bind(MaintainParticipantDeduction.class).to(
      curam.ca.gc.bdm.sl.maintainparticipantdeduction.impl.MaintainParticipantDeduction.class);
  }

}
