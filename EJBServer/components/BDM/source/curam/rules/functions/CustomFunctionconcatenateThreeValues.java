package curam.rules.functions;

import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.GeneralConstants;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.rules.functor.CustomFunctor;
import java.util.List;

/**
 * Contains a custom function that will concatenate the parameters.
 *
 * @curam.exclude
 */
public class CustomFunctionconcatenateThreeValues extends CustomFunctor {

  /**
   * A custom function that will concatenate the parameters.
   *
   * @param rulesParameters The rules parameters containing the object to
   * check. The first parameter in the list of parameters is checked.
   *
   * @return A rule adaptor containing the string concatenation of the two
   * parameters.
   *
   * @throws AppException Exceptions that may occur when retrieving the
   * adaptor value.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    final List<Adaptor> parameters = getParameters();
    if (parameters.size() == 3 && (parameters.get(0) == null
      || parameters.get(1) == null || parameters.get(2) == null)) {
      // Parameters are null, the questions were not displayed so return an
      // empty string
      return AdaptorFactory.getStringAdaptor(GeneralConstants.kEmpty);
    }

    final String parameter1 =
      ((StringAdaptor) parameters.get(0)).getStringValue(rulesParameters);

    final String parameter2 =
      ((StringAdaptor) parameters.get(1)).getStringValue(rulesParameters);

    final String parameter3 =
      ((StringAdaptor) parameters.get(2)).getStringValue(rulesParameters);

    return AdaptorFactory
      .getStringAdaptor(parameter1 + parameter2 + parameter3);
  }
}
