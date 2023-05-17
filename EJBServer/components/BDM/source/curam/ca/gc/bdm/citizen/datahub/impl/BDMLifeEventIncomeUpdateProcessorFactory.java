package curam.ca.gc.bdm.citizen.datahub.impl;

import curam.citizen.datahub.impl.CustomUpdateProcessor;
import curam.citizen.datahub.impl.CustomUpdateProcessorFactory;

public class BDMLifeEventIncomeUpdateProcessorFactory
  implements CustomUpdateProcessorFactory {

  @Override
  public CustomUpdateProcessor createCustomUpdateProcessor() {

    return new BDMLifeEventIncomeUpdateProcessor();
  }

}
