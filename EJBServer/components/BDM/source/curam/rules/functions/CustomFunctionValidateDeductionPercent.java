package curam.rules.functions;

import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.rules.functor.CustomFunctor;

/**
 * Contains a custom function to validate deduction percentage range be between
 * 1 - 100
 *
 * @curam.exclude
 */
public class CustomFunctionValidateDeductionPercent extends CustomFunctor {

  /**
   * A custom function to validate Deduction percentage range
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

    // BUG: 11109 fixed added || params.get(1) == null
    if (params.size() == 2 && params.get(0) == null && params.get(1) == null
      || params.get(1) == null) {
      // Parameters are null, the questions were not displayed so no need to
      // validate
      return AdaptorFactory.getBooleanAdaptor(true);
    }

    final Adaptor deductionPArameter = params.get(0);
    final LongAdaptor percentParameter = (LongAdaptor) params.get(1);

    boolean result = true;

    try {
      final String deductionMethod =
        getStringValue(rulesParameters, deductionPArameter);
      final long percent = percentParameter.getLongValue(rulesParameters);

      if (deductionMethod
        .equals(CustomFunctionConstants.DEDUCTION_TYPE_METHOD_AMT)) {
        return AdaptorFactory.getBooleanAdaptor(true);
      }

      if (percent <= 0 || percent > 100) {
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
