package curam.rules.functions;

import curam.ca.gc.bdm.ws.sinvalidation.impl.SINValidationMod10;
import curam.datastore.impl.Entity;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.GeneralConstants;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;

/**
 * Contains a custom function that will validate SIN using MOD10.
 *
 * @curam.exclude
 */
public class CustomFunctionValidateSINMOD10 extends SINValidator {

  /**
   * A custom function that will validate SIN using MOD10.
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
    final boolean validSIN = getContinueSIN(rootEntity);

    if (!validSIN) {
      // previous sin check has failed, don't run this one
      return AdaptorFactory.getBooleanAdaptor(true);
    }

    final String stringParameter =
      ((StringAdaptor) getParameters().get(0)).getStringValue(rulesParameters)
        .replaceAll(SINValidator.SIN_MASK_SEPARATOR, GeneralConstants.kEmpty);

    final boolean isValidMOD10SIN =
      SINValidationMod10.isValidMOD10(stringParameter);

    updateContinueSIN(rootEntity, isValidMOD10SIN);

    return AdaptorFactory.getBooleanAdaptor(isValidMOD10SIN);
  }
}
