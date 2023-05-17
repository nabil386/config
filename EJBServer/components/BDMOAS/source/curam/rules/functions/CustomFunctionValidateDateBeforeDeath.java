package curam.rules.functions;

import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.type.Date;

/**
 * Contains a custom function that will validate the ‘Date applicant first
 * entered Canada’ cannot be after Date of Death.
 *
 * @curam.exclude
 */
public class CustomFunctionValidateDateBeforeDeath extends BDMFunctor {

  /**
   * A custom function that will validate a Date isn't after Date of Death.
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

    boolean validationPassed = true;
    final Date checkedDateParameter =
      ((DateAdaptor) getParameters().get(0)).getValue(rulesParameters);

    final Date dateOfDeathParameter =
      ((DateAdaptor) getParameters().get(1)).getValue(rulesParameters);

    if (null != dateOfDeathParameter && null != checkedDateParameter
      && !dateOfDeathParameter.isZero()
      && !dateOfDeathParameter.after(checkedDateParameter)) {
      validationPassed = false;
    }

    return AdaptorFactory.getBooleanAdaptor(validationPassed);
  }

}
