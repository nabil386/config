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
public class CustomFunctionvalidateValueConditionalMandatory
  extends CustomFunctor {

  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    final java.util.List<Adaptor> params = getParameters();

    if (null == params) {
      return AdaptorFactory.getBooleanAdaptor(true);
    }

    final Adaptor valueAdaptor = params.get(0);
    final Adaptor referenceValueAdaptor = params.get(1);

    if (null != referenceValueAdaptor
      && !((StringAdaptor) referenceValueAdaptor)
        .getStringValue(rulesParameters).isEmpty()) {
      if (null == valueAdaptor || ((StringAdaptor) valueAdaptor)
        .getStringValue(rulesParameters).isEmpty()) {
        return AdaptorFactory.getBooleanAdaptor(false);
      }
    }
    return AdaptorFactory.getBooleanAdaptor(true);
  }

}
