package curam.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.rules.functor.CustomFunctor;

/**
 * Contains a custom function that may be called to ensure that postal code is
 * correct.
 *
 * @curam.exclude
 */
public class CustomFunctionvalidateCAPostalCode extends CustomFunctor {

  BDMUtil bdmUtil = new BDMUtil();

  /**
   * A custom function that may be called to ensure that postal code is correct.
   *
   * @param rulesParameters The rules parameters containing the object to
   * check. The first parameter in the list of parameters is checked.
   *
   * @return A rule adaptor indicating whether the object is null or not.
   *
   * @throws AppException Exceptions that may occur when retrieving the
   * adaptor value.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    final Adaptor parameter = getParameters().get(0);
    if (parameter == null || parameter.equals("")) {
      return AdaptorFactory.getBooleanAdaptor(true);
    }
    final String postalCode =
      ((StringAdaptor) parameter).getStringValue(rulesParameters);

    return AdaptorFactory
      .getBooleanAdaptor(bdmUtil.isValidPostalCode(postalCode));
  }

}
