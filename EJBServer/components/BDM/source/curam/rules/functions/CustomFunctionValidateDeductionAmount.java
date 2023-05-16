package curam.rules.functions;

import curam.ca.gc.bdm.codetable.impl.BDMDEDMETHODEntry;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.rules.functor.CustomFunctor;

/**
 * Contains a custom function to validate deduction amount cannot be greater
 * than 400
 */
public class CustomFunctionValidateDeductionAmount extends CustomFunctor {

  /**
   * A custom function to validate a amount.
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
    if (params.size() == 2 && params.get(0) == null && params.get(1) == null
      || params.get(1) == null) {
      // Parameters are null, the questions were not displayed so no need to
      // validate
      return AdaptorFactory.getBooleanAdaptor(true);
    }

    final Adaptor deductionPArameter = params.get(0);
    final MoneyAdaptor amountParameter = (MoneyAdaptor) params.get(1);

    boolean result = true;

    try {
      final String deductionMethod =
        getStringValue(rulesParameters, deductionPArameter);
      final Double amount = amountParameter.getDoubleValue(rulesParameters);

      if (!deductionMethod.equals(BDMDEDMETHODEntry.DOLLAR.getCode())) {
        return AdaptorFactory.getBooleanAdaptor(true);
      }

      if (amount <= 0 || amount > 400) {
        result = false;
      }

    } catch (final Exception e) {
      return AdaptorFactory.getBooleanAdaptor(false);
    }
    return AdaptorFactory.getBooleanAdaptor(result);
  }

  /**
   * Gets a parameter string value.
   *
   * @param rulesParameters the rules parameters
   * @param stringParameter the string parameter
   * @return the string value
   * @throws InformationalException the informational exception
   * @throws AppException the app exception
   */
  private String getStringValue(final RulesParameters rulesParameters,
    final Adaptor stringParameter)
    throws InformationalException, AppException {

    return ((StringAdaptor) stringParameter).getStringValue(rulesParameters);
  }

}
