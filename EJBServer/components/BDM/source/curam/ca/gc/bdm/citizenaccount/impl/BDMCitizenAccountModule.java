package curam.ca.gc.bdm.citizenaccount.impl;

import com.google.inject.AbstractModule;
import curam.citizenaccount.impl.CitizenCommunicationsStrategy;

public class BDMCitizenAccountModule extends AbstractModule {

  @Override
  protected void configure() {

    // START : TASK 23187
    /**
     * binds Citizen Communications Strategy mapping
     */
    bind(CitizenCommunicationsStrategy.class)
      .to(BDMCitizenCommunicationsStrategyImpl.class);

  }
  // END : TASK 23187

}
