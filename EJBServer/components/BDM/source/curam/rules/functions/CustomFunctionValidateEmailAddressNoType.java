package curam.rules.functions;

import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.rules.functor.CustomFunctor;

/**
 * Ensure that if an email address is entered, a type is entered
 *
 */
public class CustomFunctionValidateEmailAddressNoType extends CustomFunctor {

  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    boolean validEmailAddrTypeComboEntered = true;

    final java.util.List<Adaptor> params = getParameters();

    if (null == params) {
      return AdaptorFactory.getBooleanAdaptor(true);
    }

    final Adaptor emailAddressAdaptor = params.get(0);
    final Adaptor emailTypeAdaptor = params.get(1);

    if (null != emailAddressAdaptor && !((StringAdaptor) emailAddressAdaptor)
      .getStringValue(rulesParameters).isEmpty()) {
      if (null == emailTypeAdaptor || ((StringAdaptor) emailTypeAdaptor)
        .getStringValue(rulesParameters).isEmpty()) {
        validEmailAddrTypeComboEntered = false;
      }
    }

    return AdaptorFactory.getBooleanAdaptor(validEmailAddrTypeComboEntered);
  }

}
