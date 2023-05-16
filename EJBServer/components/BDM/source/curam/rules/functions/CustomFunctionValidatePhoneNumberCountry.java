package curam.rules.functions;

import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.rules.functor.CustomFunctor;

/**
 * Ensure that if Phone number is entered Phone Country must also be entered
 *
 */
public class CustomFunctionValidatePhoneNumberCountry extends CustomFunctor {

  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    boolean validPhoneCountryEntered = true;

    final java.util.List<Adaptor> params = getParameters();

    if (null == params) {
      return AdaptorFactory.getBooleanAdaptor(true);
    }

    final Adaptor phoneCountryAdaptor = params.get(0);
    final Adaptor phoneNumberAdaptor = params.get(1);

    if (null != phoneNumberAdaptor && !((StringAdaptor) phoneNumberAdaptor)
      .getStringValue(rulesParameters).isEmpty()) {
      if (null == phoneCountryAdaptor || ((StringAdaptor) phoneCountryAdaptor)
        .getStringValue(rulesParameters).isEmpty()) {
        validPhoneCountryEntered = false;
      }
    } /*
       * else if (null != phoneCountryAdaptor
       * && !((StringAdaptor) phoneCountryAdaptor)
       * .getStringValue(rulesParameters).isEmpty()) {
       * validPhoneCountryEntered = false;
       * }
       */

    return AdaptorFactory.getBooleanAdaptor(validPhoneCountryEntered);
  }

}
