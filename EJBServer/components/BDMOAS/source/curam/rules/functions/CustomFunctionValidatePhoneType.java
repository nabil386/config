package curam.rules.functions;

import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.rules.functor.CustomFunctor;

/**
 * Ensure that if Phone number or phone area code is entered the phone type must
 * also be entered
 *
 */
public class CustomFunctionValidatePhoneType extends CustomFunctor {

  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    boolean validPhoneCountryEntered = true;

    final java.util.List<Adaptor> params = getParameters();

    if (null == params) {
      return AdaptorFactory.getBooleanAdaptor(true);
    }

    final Adaptor phoneTypeAdaptor = params.get(0);
    final Adaptor phoneNumberOrAreaCodeAdaptor = params.get(1);

    if (null != phoneNumberOrAreaCodeAdaptor
      && !((StringAdaptor) phoneNumberOrAreaCodeAdaptor)
        .getStringValue(rulesParameters).isEmpty()) {
      if (null == phoneTypeAdaptor || ((StringAdaptor) phoneTypeAdaptor)
        .getStringValue(rulesParameters).isEmpty()) {
        validPhoneCountryEntered = false;
      }
    }
    return AdaptorFactory.getBooleanAdaptor(validPhoneCountryEntered);
  }

}
