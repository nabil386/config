package curam.ca.gc.bdm.sl.financial.impl;

import com.google.inject.AbstractModule;
import curam.ca.gc.bdm.sl.financial.intf.BDMFinancial;
import curam.ca.gc.bdm.sl.financial.managetaxslips.intf.BDMManageTaxSlips;

public class BDMFinancialModule extends AbstractModule {

  @Override
  protected void configure() {

    bind(BDMFinancial.class)
      .to(curam.ca.gc.bdm.sl.financial.impl.BDMFinancial.class);

    bind(BDMManageTaxSlips.class).to(
      curam.ca.gc.bdm.sl.financial.managetaxslips.impl.BDMManageTaxSlips.class);
  }

}
