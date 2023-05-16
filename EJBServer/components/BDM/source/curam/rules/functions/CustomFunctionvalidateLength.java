package curam.rules.functions;

import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.StringUtil;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.rules.functor.CustomFunctor;

/**
 * Contains a custom function to validate min and max number of characters in a
 * given string.
 *
 * @curam.exclude
 */
public class CustomFunctionvalidateLength extends CustomFunctor {

  private static final String kPhoneNumberPattern = "^[0-9]+-?([0-9])*$";

  /**
   * A custom function to validate a string length.
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

    final Adaptor stringParameter = getParameters().get(0);
    final Adaptor minParameter = getParameters().get(1);
    final Adaptor maxParameter = getParameters().get(2);
    boolean result = true;
    try {
      final String stringValue =
        getStringValue(rulesParameters, stringParameter);

      final String minValue = getStringValue(rulesParameters, minParameter);
      final String maxValue = getStringValue(rulesParameters, maxParameter);
      final Integer min = StringUtil.isNullOrEmpty(minValue) ? null
        : Integer.parseInt(minValue);
      final Integer max = StringUtil.isNullOrEmpty(maxValue) ? null
        : Integer.parseInt(maxValue);

      if (!stringValue.matches(kPhoneNumberPattern)) {
        result = false;
      } else {
        if (null != max && max == min
          && (stringValue.length() != max || stringValue.length() != min)) {
          result = false;
        } else if (null != max && stringValue.length() > max
          || null != min && stringValue.length() < min) {
          result = false;
        }
      }

      return AdaptorFactory.getBooleanAdaptor(result);
    } catch (final Exception e) {
      return AdaptorFactory.getBooleanAdaptor(false);
    }
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
