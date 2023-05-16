package curam.rules.functions;

import curam.codetable.COUNTRY;
import curam.codetable.IEGYESNO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.StringUtil;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import java.util.List;

/**
 * BDMOAS FEATURE 94380: Class Added
 * Contains a custom function to validate foreign identifier
 *
 * @author SMisal
 *
 */
public class CustomFunctionValidateForeignIdentifier extends BDMFunctor {

  /**
   * A custom function that will validate foreign identifier.
   * If country is not Canada and user selects yes for 'Have you applied for a
   * benefit from this country' then user needs to provide insurance or
   * identification number.
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

    boolean isValid = true;
    final List<Adaptor> parameters = getParameters();
    if (parameters.size() == 3) {
      if (parameters.get(0) == null || parameters.get(1) == null) {
        // it means no data is entered
        return AdaptorFactory.getBooleanAdaptor(isValid);
      }
    }

    final String parameter1 =
      ((StringAdaptor) parameters.get(0)).getStringValue(rulesParameters);

    final String parameter2 =
      ((StringAdaptor) parameters.get(1)).getStringValue(rulesParameters);

    if (parameter1 != null && !parameter1.equals(COUNTRY.CA)
      && parameter2 != null && parameter2.equals(IEGYESNO.YES)) {
      final Adaptor strAdapter = parameters.get(2);
      if (strAdapter == null) {
        isValid = false;
      } else {
        final String parameter3 =
          ((StringAdaptor) parameters.get(2)).getStringValue(rulesParameters);
        if (StringUtil.isNullOrEmpty(parameter3)) {
          isValid = false;
        }
      }
    }

    return AdaptorFactory.getBooleanAdaptor(isValid);
  }

}
