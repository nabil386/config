package curam.ca.gc.bdm.citizen.datahub.impl;

import curam.citizen.datahub.impl.CustomViewProcessor;
import curam.citizen.datahub.impl.CustomViewProcessorFactory;

public class BDMLifeEventPhoneNumberViewProcessorFactory
  implements CustomViewProcessorFactory {

  @Override
  public CustomViewProcessor createCustomViewProcessor() {

    return new BDMLifeEventPhoneNumberViewProcessor();
  }
}
