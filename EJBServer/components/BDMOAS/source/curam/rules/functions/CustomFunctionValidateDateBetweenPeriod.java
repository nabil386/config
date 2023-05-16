package curam.rules.functions;

import curam.core.impl.CuramConst;
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
public class CustomFunctionValidateDateBetweenPeriod extends BDMFunctor {

  /**
   * A custom function that will validate if date falls between today's date and
   * the date the given amount of years ago.
   *
   * @param rulesParameters The rules parameters containing the object to
   * check. The first parameter in the list of parameters is checked. The second
   * parameter is how many years ago should the date be checked against.
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

    final Date checkedDate =
      ((DateAdaptor) getParameters().get(0)).getValue(rulesParameters);

    final String yearsAgo = ((StringAdaptor) getParameters().get(1))
      .getStringValue(rulesParameters);

    final Date today = curam.util.type.Date.getCurrentDate();
    final String date = today.toString();
    String[] split = {CuramConst.gkEmpty };

    int yearCheck = 0;
    if (date.contains(CuramConst.gkSlashString)) {
      split = date.split(CuramConst.gkSlashString);
    } else if (date.contains(CuramConst.gkDash)) {
      split = date.split(CuramConst.gkDash);
    }

    yearCheck = Integer.parseInt(split[2]) - Integer.parseInt(yearsAgo);

    if (split[0].length() == 1) {
      split[0] = "0" + split[0];
    }

    if (split[1].length() == 1) {
      split[1] = "0" + split[1];
    }

    final Date checkingDate = curam.util.type.Date
      .getDate(Integer.toString(yearCheck) + split[0] + split[1]);

    if (checkedDate.before(checkingDate)) {
      validationPassed = false;
    }

    return AdaptorFactory.getBooleanAdaptor(validationPassed);
  }

}
