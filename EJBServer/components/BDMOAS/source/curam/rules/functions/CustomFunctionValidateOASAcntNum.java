package curam.rules.functions;

import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;

/**
 * BDMOAS FEATURE 52093: Class Added
 * Contains a custom function that will validate OAS Account number
 *
 * @author SMisal
 *
 */
public class CustomFunctionValidateOASAcntNum extends BDMFunctor {

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

    final String stringParameter = ((StringAdaptor) getParameters().get(0))
      .getStringValue(rulesParameters);

    boolean isValid = false;

    final boolean isValidLength = stringParameter.length() == 9;

    final boolean isStartsWithZero = stringParameter.startsWith("0");

    final boolean isNumeric =
      stringParameter.chars().allMatch(Character::isDigit);

    if (isValidLength && isStartsWithZero && isNumeric) {
      isValid = true;
    }

    return AdaptorFactory.getBooleanAdaptor(isValid);
  }

}
