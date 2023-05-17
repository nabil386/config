package curam.rules.functions;

import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.rules.functor.CustomFunctor;

/**
 * Ensure that if either a Phone Number or Phone Type is entered, the other
 * must also be entered
 *
 */
public class CustomFunctionValidatePhoneNumberAndType extends CustomFunctor {

  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    boolean validPhoneNumTypeComboEntered = true;

    final java.util.List<Adaptor> params = getParameters();

    if (null == params) {
      return AdaptorFactory.getBooleanAdaptor(true);
    }

    final Adaptor phoneNumberAdaptor = params.get(0);
    final Adaptor phoneTypeAdaptor = params.get(1);

    if (null != phoneNumberAdaptor && !((StringAdaptor) phoneNumberAdaptor)
      .getStringValue(rulesParameters).isEmpty()) {
      if (null == phoneTypeAdaptor || ((StringAdaptor) phoneTypeAdaptor)
        .getStringValue(rulesParameters).isEmpty()) {
        validPhoneNumTypeComboEntered = false;
      }
    } else if (null != phoneTypeAdaptor && !((StringAdaptor) phoneTypeAdaptor)
      .getStringValue(rulesParameters).isEmpty()) {
      validPhoneNumTypeComboEntered = false;
    }

    return AdaptorFactory.getBooleanAdaptor(validPhoneNumTypeComboEntered);
  }

}
