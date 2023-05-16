package curam.rules.functions;

import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.type.Date;

/**
 * Contains a custom function that will validate if the provided date is not a
 * future date.
 *
 * @curam.exclude
 */
public class CustomFunctiongetPreviousYear extends BDMFunctor {

  /**
   * A custom function that will give previous tax year
   *
   * @param rulesParameters will have no parameters.
   *
   * @return A rule adaptor containing the previous tax year.
   *
   * @throws AppException Exceptions that may occur when retrieving the
   * adaptor value.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    final Date today = curam.util.type.Date.getCurrentDate();
    final String date = today.toString();
    final String[] split = date.split("/");
    String year = new String();

    for (final String piece : split) {
      if (piece.length() == 4) {
        year = piece;
        Integer yearInt = Integer.parseInt(year);
        yearInt = yearInt - 1;
        year = yearInt.toString();

      }
    }

    return AdaptorFactory.getStringAdaptor(year);
  }

}
