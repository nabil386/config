package curam.ca.gc.bdm.sl.ratetable.impl;

import com.google.inject.AbstractModule;
import curam.ca.gc.bdm.sl.ratetable.intf.MaintainRateTable;

public class BDMMaintainRateTableModule extends AbstractModule {

  @Override
  protected void configure() {

    bind(MaintainRateTable.class)
      .to(curam.ca.gc.bdm.sl.ratetable.impl.MaintainRateTable.class);
  }

}
