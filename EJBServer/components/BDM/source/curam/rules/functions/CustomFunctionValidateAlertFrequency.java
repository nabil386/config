package curam.rules.functions;

import curam.ca.gc.bdm.codetable.BDMALERTOCCUR;
import curam.codetable.IEGYESNO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.rules.functor.CustomFunctor;

/**
 * validate alert frequency.
 *
 */
public class CustomFunctionValidateAlertFrequency extends CustomFunctor {

  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    final java.util.List<Adaptor> params = getParameters();

    if (null == params) {
      return AdaptorFactory.getBooleanAdaptor(true);
    }

    final Adaptor receiveAlertsAdaptor = params.get(0);
    final Adaptor alertFrequencyAdaptor = params.get(1);

    if (null != receiveAlertsAdaptor && null != alertFrequencyAdaptor) {
      final String receiveAlerts = ((StringAdaptor) receiveAlertsAdaptor)
        .getStringValue(rulesParameters);
      final String alertFrequency = ((StringAdaptor) alertFrequencyAdaptor)
        .getStringValue(rulesParameters);

      if (receiveAlerts.equals(IEGYESNO.YES)) {
        if (alertFrequency.equals(BDMALERTOCCUR.PERDAY)
          || alertFrequency.equals(BDMALERTOCCUR.PERINFO)) {
          return AdaptorFactory.getBooleanAdaptor(true);
        } else {
          return AdaptorFactory.getBooleanAdaptor(false);
        }
      }
    }
    return AdaptorFactory.getBooleanAdaptor(true);
  }

}
