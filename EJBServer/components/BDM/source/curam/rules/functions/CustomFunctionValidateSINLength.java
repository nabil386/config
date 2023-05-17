package curam.rules.functions;

import curam.datastore.impl.Entity;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.GeneralConstants;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;

/**
 * Contains a custom function that will validate SIN length.
 *
 * @curam.exclude
 */
public class CustomFunctionValidateSINLength extends SINValidator {

  /**
   * A custom function that will validate SIN length.
   *
   * @param rulesParameters The rules parameters containing the object to
   * check. The first parameter in the list of parameters is checked.
   *
   * @return A rule adaptor indicating whether validation passes or fails.
   *
   * @throws AppException Exceptions that may occur when retrieving the
   * adaptor value.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    final Entity rootEntity = readRoot(rulesParameters);

    final String stringParameter =
      ((StringAdaptor) getParameters().get(0)).getStringValue(rulesParameters)
        .replaceAll(SIN_MASK_SEPARATOR, GeneralConstants.kEmpty);

    final boolean isValidLength = stringParameter.length() == SIN_LENGTH;

    updateContinueSIN(rootEntity, isValidLength);

    return AdaptorFactory.getBooleanAdaptor(isValidLength);
  }

}
