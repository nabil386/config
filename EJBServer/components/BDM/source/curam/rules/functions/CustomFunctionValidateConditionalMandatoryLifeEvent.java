package curam.rules.functions;

import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.rules.functor.CustomFunctor;

/**
 * Ensure that if reference value is entered, value must be entered as well
 *
 */
public class CustomFunctionValidateConditionalMandatoryLifeEvent
  extends CustomFunctor {

  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    boolean validPhoneNumTypeComboEntered = true;

    final java.util.List<Adaptor> params = getParameters();

    if (null == params) {
      return AdaptorFactory.getBooleanAdaptor(true);
    }

    // EMAIL ACTION
    final StringAdaptor phoneNumberAdaptor =
      (StringAdaptor) getParameters().get(0);
    // SELECTED EMAIL
    final StringAdaptor phoneTypeAdaptor =
      (StringAdaptor) getParameters().get(1);

    if (null != phoneNumberAdaptor
      && (!phoneNumberAdaptor.getStringValue(rulesParameters).isEmpty()
        && !phoneNumberAdaptor.getStringValue(rulesParameters)
          .equals("BDMEACT02")
        && !phoneNumberAdaptor.getStringValue(rulesParameters)
          .equals("BDMEACT03")
        || !phoneNumberAdaptor.getStringValue(rulesParameters).isEmpty()
          && !phoneNumberAdaptor.getStringValue(rulesParameters)
            .equals("BDMPNCT02")
          && !phoneNumberAdaptor.getStringValue(rulesParameters)
            .equals("BDMPNCT03"))) {
      if (null == phoneTypeAdaptor
        || phoneTypeAdaptor.getStringValue(rulesParameters).isEmpty()) {
        validPhoneNumTypeComboEntered = false;
      }
    } else if (null != phoneTypeAdaptor
      && !phoneTypeAdaptor.getStringValue(rulesParameters).isEmpty()) {
      validPhoneNumTypeComboEntered = false;
    }

    return AdaptorFactory.getBooleanAdaptor(validPhoneNumTypeComboEntered);
  }

}
