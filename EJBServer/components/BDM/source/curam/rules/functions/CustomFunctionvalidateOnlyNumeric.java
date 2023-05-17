package curam.rules.functions;

import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.rules.functor.CustomFunctor;

/**
 * Contains a custom function that may be called to ensure that the input only
 * contains numeric values.
 *
 * @curam.exclude
 */
public class CustomFunctionvalidateOnlyNumeric extends CustomFunctor {

  private static final String kExtensionNumberPattern = "^(0|[0-9][0-9]*)$";

  /**
   * A custom function that may be called to ensure that numeric values are only
   * entered.
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
    final String extNumber =
      ((StringAdaptor) parameter).getStringValue(rulesParameters);

    return AdaptorFactory.getBooleanAdaptor(isNumericValueValid(extNumber));
  }

  /**
   * Validates if an input is numeric or not.
   *
   * @param extNumber
   * @return
   */
  private static boolean isNumericValueValid(final String extNumber) {

    final Boolean exactMatch = extNumber.matches(kExtensionNumberPattern);
    return exactMatch;
  }

}
