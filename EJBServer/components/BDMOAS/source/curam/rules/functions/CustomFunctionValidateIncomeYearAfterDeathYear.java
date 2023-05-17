package curam.rules.functions;

import curam.core.impl.CuramConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.StringUtil;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.type.Date;

/**
 * Contains a custom function that will validate provided income year cannot be
 * after death year.
 *
 * @curam.exclude
 */
public class CustomFunctionValidateIncomeYearAfterDeathYear
  extends BDMFunctor {

  private static final String kNumberPattern = "^(0|[0-9][0-9]*)$";

  /**
   * A custom function that will validate provided income year cannot be after
   * death year.
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
    if (params.get(0) == null && params.get(1) == null) {
      // Parameters are null, the questions were not displayed so no need to
      // validate
      return AdaptorFactory.getBooleanAdaptor(true);
    }

    final Adaptor yearParameter = params.get(0);

    final String incomeYearStr =
      ((StringAdaptor) yearParameter).getStringValue(rulesParameters);

    final Date dateParameter =
      ((DateAdaptor) params.get(1)).getValue(rulesParameters);

    final String date = dateParameter.toString();
    String[] split = {CuramConst.gkEmpty };

    if (date.contains(CuramConst.gkSlashString)) {
      split = date.split(CuramConst.gkSlashString);
    } else if (date.contains(CuramConst.gkDash)) {
      split = date.split(CuramConst.gkDash);
    }

    String deathYearStr = new String();

    for (final String strListItem : split) {
      if (strListItem.length() == 4) {
        deathYearStr = strListItem;
      }
    }

    boolean result = true;

    if (isNumericValueValid(incomeYearStr)
      && !StringUtil.isNullOrEmpty(incomeYearStr)
      && !StringUtil.isNullOrEmpty(deathYearStr)) {
      final int incomeYear = Integer.parseInt(incomeYearStr);
      final int deathYear = Integer.parseInt(deathYearStr);

      if (incomeYear > deathYear) {
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

    return strNumber.matches(kNumberPattern);
  }
}
