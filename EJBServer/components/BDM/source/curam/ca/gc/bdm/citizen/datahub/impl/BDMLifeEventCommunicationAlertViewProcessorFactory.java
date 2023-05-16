package curam.ca.gc.bdm.citizen.datahub.impl;

import curam.citizen.datahub.impl.CustomViewProcessor;
import curam.citizen.datahub.impl.CustomViewProcessorFactory;

/**
 * @author vipresh.sharma
 * @since 9010 Life Events Update preference and alerts
 */
public class BDMLifeEventCommunicationAlertViewProcessorFactory
  implements CustomViewProcessorFactory {

  @Override
  public CustomViewProcessor createCustomViewProcessor() {

    return new BDMLifeEventCommunicationAlertViewProcessor();
  }
}
