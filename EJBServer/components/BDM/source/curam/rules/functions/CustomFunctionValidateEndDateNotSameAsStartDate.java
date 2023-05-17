package curam.rules.functions;

import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.type.Date;

/**
 * Contains a custom function that will validate if the end date is same as
 * start date.
 *
 * @curam.exclude
 */
public class CustomFunctionValidateEndDateNotSameAsStartDate
  extends BDMFunctor {

  /**
   * A custom function that will validate if the provided end date is before and
   * not on the start date.
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

    final DateAdaptor fromDateAdapter = (DateAdaptor) getParameters().get(0);
    final Date inputFromDate = fromDateAdapter.getValue(rulesParameters);
    final DateAdaptor toDateAdapter = (DateAdaptor) getParameters().get(1);
    final Date inputToDate = toDateAdapter != null
      ? toDateAdapter.getValue(rulesParameters) : Date.kZeroDate;

    if (!inputToDate.isZero()) {
      return AdaptorFactory
        .getBooleanAdaptor(!inputToDate.equals(inputFromDate));
    }
    return AdaptorFactory.getBooleanAdaptor(true);
  }

}
