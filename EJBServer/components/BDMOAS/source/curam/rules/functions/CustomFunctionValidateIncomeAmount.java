package curam.rules.functions;

import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.rules.functor.CustomFunctor;

/**
 * Contains a custom function to validate income amount can not be a negative
 * amount.
 */
public class CustomFunctionValidateIncomeAmount extends CustomFunctor {

  /**
   * A custom function to validate "Income amount cannot be a negative amount."
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
    if (params.get(0) == null) {
      // Parameters are null, the questions were not displayed so no need to
      // validate
      return AdaptorFactory.getBooleanAdaptor(true);
    }

    final MoneyAdaptor amountParameter = (MoneyAdaptor) params.get(0);

    boolean result = true;

    try {

      final Double amount = amountParameter.getDoubleValue(rulesParameters);

      if (amount < 0) {
        result = false;
      }

    } catch (final Exception e) {
      return AdaptorFactory.getBooleanAdaptor(false);
    }
    return AdaptorFactory.getBooleanAdaptor(result);
  }

}
