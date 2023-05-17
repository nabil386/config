package curam.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ieg.impl.IEG2Context;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.rules.functor.CustomFunctor;
import java.util.List;
import java.util.Map;

public class CustomFunctionvalidateAlerts extends CustomFunctor {

  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    final IEG2Context ieg2Context = (IEG2Context) rulesParameters;
    final String receiveAlertYesNo = ((StringAdaptor) getParameters().get(0))
      .getStringValue(rulesParameters);

    if (receiveAlertYesNo != null
      && receiveAlertYesNo.equalsIgnoreCase("YN1")) {
      if (checkCommunicationPref(BDMDatastoreConstants.PHONE_NUMBER_SELECTED,
        ieg2Context)
        || checkCommunicationPref(BDMDatastoreConstants.EMAIL_ADR_SELECTED,
          ieg2Context)) {
        return AdaptorFactory.getBooleanAdaptor(true);
      }
      return AdaptorFactory.getBooleanAdaptor(false);
    }
    return AdaptorFactory.getBooleanAdaptor(true);

  }

  public boolean checkCommunicationPref(final String prefType,
    final IEG2Context ieg2Context) {

    final List<Map<Long, String>> listOfaltPrefEmail =
      ieg2Context.getListQuestionValues(prefType,
        BDMDatastoreConstants.COMMUNICATION_DETAILS);
    for (final Map<Long, String> tempObj : listOfaltPrefEmail) {
      if (tempObj != null && tempObj.values() != null) {
        for (final String stringBoolean : tempObj.values()) {
          if (stringBoolean.equalsIgnoreCase("true")) {
            return true;
          }
        }
      }
    }
    return false;

  }
}
