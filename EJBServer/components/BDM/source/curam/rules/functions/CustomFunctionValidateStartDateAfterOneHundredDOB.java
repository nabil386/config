package curam.rules.functions;

import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.type.Date;

/**
 * Contains a custom function that will validate if the start date is after
 * 100th birthday
 *
 * @curam.exclude
 */
public class CustomFunctionValidateStartDateAfterOneHundredDOB
  extends BDMFunctor {

  /**
   * A custom function that will validate if the provided date is after
   * 100th birthday
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

    final Date dateParameter =
      ((DateAdaptor) getParameters().get(0)).getValue(rulesParameters);
    final Date birthdate =
      ((DateAdaptor) getParameters().get(1)).getValue(rulesParameters);

    return AdaptorFactory
      .getBooleanAdaptor(!dateParameter.after(birthdate.addDays(365 * 100)));
  }

}
