package curam.ca.gc.bdm.citizen.datahub.impl;

import curam.citizen.datahub.impl.CustomUpdateProcessor;
import curam.citizen.datahub.impl.CustomUpdateProcessorFactory;

/**
 * @author vipresh.sharma
 * @since 9010 Life Events Update preference and alerts
 */
public class BDMLifeEventCommunicationAlertUpdateProcessorFactory
  implements CustomUpdateProcessorFactory {

  @Override
  public CustomUpdateProcessor createCustomUpdateProcessor() {

    return new BDMLifeEventCommunicationAlertUpdateProcessor();
  }

}
