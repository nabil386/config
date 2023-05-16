package curam.rules.functions;

import curam.ca.gc.bdmoas.impl.BDMOASConstants;
import curam.core.impl.CuramConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.type.Date;

/**
 * Contains a custom function that will validate if the provided income year is
 * a whole number between 1967 and the previous calendar year.
 *
 * @curam.exclude
 */
public class CustomFunctionValidateIncomeYear extends BDMFunctor {

  private static final String kNumberPattern = "^(0|[0-9][0-9]*)$";

  /**
   * A custom function that will validate if the provided income year is a whole
   * number between 1967 and the previous calendar year.
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

    final java.util.List<Adaptor> params = getParameters();
    if (params.get(0) == null) {
      // Parameters are null, the questions were not displayed so no need to
      // validate
      return AdaptorFactory.getBooleanAdaptor(true);
    }

    final Adaptor yearParameter = params.get(0);

    final String incomeYearStr =
      ((StringAdaptor) yearParameter).getStringValue(rulesParameters);

    final int previousCalendarYear = this.getPreviousCalendarYear();

    boolean result = true;

    if (!isNumericValueValid(incomeYearStr)) {
      result = false;
    } else {
      final int incomeYear = Integer.parseInt(incomeYearStr);
      if (!(incomeYear > BDMOASConstants.kIncmYrToCompare
        && incomeYear <= previousCalendarYear)) {
        result = false;
      }
    }

    return AdaptorFactory.getBooleanAdaptor(result);
  }

  /**
   * Method to check if an input is numeric or not.
   *
   * @param extNumber
   * @return
   */
  private static boolean isNumericValueValid(final String strNumber) {

    final Boolean exactMatch = strNumber.matches(kNumberPattern);
    return exactMatch;
  }

  /**
   * Method to get the previous calendar year.
   *
   * @param extNumber
   * @return
   */
  private int getPreviousCalendarYear() {

    int previousCalendarYear = 0;
    final Date today = curam.util.type.Date.getCurrentDate();
    final String date = today.toString();
    String[] split = {CuramConst.gkEmpty };

    if (date.contains(CuramConst.gkSlashString)) {
      split = date.split(CuramConst.gkSlashString);
    } else if (date.contains(CuramConst.gkDash)) {
      split = date.split(CuramConst.gkDash);
    }

    String year = new String();

    for (final String piece : split) {
      if (piece.length() == 4) {
        year = piece;
        previousCalendarYear = Integer.parseInt(year) - 1;
      }
    }

    return previousCalendarYear;
  }

}
