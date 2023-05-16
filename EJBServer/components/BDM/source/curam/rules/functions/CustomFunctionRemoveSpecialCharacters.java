package curam.rules.functions;

import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.GeneralConstants;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.rules.functor.CustomFunctor;

/**
 * Contains a custom function that removes special characters from strings.
 *
 * @curam.exclude
 */
public class CustomFunctionRemoveSpecialCharacters extends CustomFunctor {

  /**
   * Contains a custom function that removes special characters from strings.
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

    final java.util.List<Adaptor> params = getParameters();

    if (params.size() == 0) {
      return AdaptorFactory.getStringAdaptor(new String());
    }

    final Adaptor parameter = params.get(0);

    if (parameter == null) {
      return AdaptorFactory.getStringAdaptor(new String());
    }

    final String stringParameter =
      ((StringAdaptor) parameter).getStringValue(rulesParameters);

    if (!stringParameter.isEmpty()) {
      return AdaptorFactory.getStringAdaptor(
        stringParameter.replaceAll("[^a-zA-Z0-9]", GeneralConstants.kEmpty));
    }

    return AdaptorFactory.getStringAdaptor(stringParameter);
  }

}
