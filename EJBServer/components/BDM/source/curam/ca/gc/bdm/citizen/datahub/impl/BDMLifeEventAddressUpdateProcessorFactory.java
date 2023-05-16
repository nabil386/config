package curam.ca.gc.bdm.citizen.datahub.impl;

import curam.citizen.datahub.impl.CustomUpdateProcessor;
import curam.citizen.datahub.impl.CustomUpdateProcessorFactory;

public class BDMLifeEventAddressUpdateProcessorFactory
  implements CustomUpdateProcessorFactory {

  @Override
  public CustomUpdateProcessor createCustomUpdateProcessor() {

    return new BDMLifeEventAddressUpdateProcessor();
  }

}
