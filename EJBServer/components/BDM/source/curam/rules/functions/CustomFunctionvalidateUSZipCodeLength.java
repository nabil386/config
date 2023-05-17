package curam.rules.functions;

import curam.codetable.COUNTRY;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.rules.functor.CustomFunctor;

/**
 * Contains a custom function to validate US zip code
 * If country is United States, Zip Code has the following format:
 * 5 digits OR 5 digits followed by a hyphen and then 4 digits
 *
 * @curam.exclude
 */
public class CustomFunctionvalidateUSZipCodeLength extends CustomFunctor {

  private static final String kZipCodePattern = "^[0-9]+-?([0-9])*$";

  /**
   * A custom function to validate a string length.
   *
   * @param rulesParameters The rules parameters containing the object to
   * check.
   *
   * @return A rule adaptor indicating whether the object is valid or not.
   *
   * @throws AppException Exceptions that may occur when retrieving the
   * adaptor value.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    final java.util.List<Adaptor> params = getParameters();

    if (null == params) {
      return AdaptorFactory.getBooleanAdaptor(true);
    }

    try {
      final Adaptor countryAdaptor = params.get(0);
      final Adaptor zipCodeAdaptor = params.get(1);

      if (null != countryAdaptor) {
        final String country =
          ((StringAdaptor) countryAdaptor).getStringValue(rulesParameters);
        if (country.equals(COUNTRY.US)) {
          if (null != zipCodeAdaptor) {
            final String zipCode = ((StringAdaptor) zipCodeAdaptor)
              .getStringValue(rulesParameters);
            if (!zipCode.matches(kZipCodePattern)) {
              return AdaptorFactory.getBooleanAdaptor(false); // wrong zip code
                                                              // pattern
            } else {
              final String[] result = zipCode.split("-");
              if (result.length == 1 && result[0].length() != 5) {
                return AdaptorFactory.getBooleanAdaptor(false); // wrong zip
                                                                // code length
              }
              if (result.length == 2
                && (result[0].length() != 5 || result[1].length() != 4)) {
                return AdaptorFactory.getBooleanAdaptor(false); // wrong zip
                                                                // code length
              }
            }
          } else {
            return AdaptorFactory.getBooleanAdaptor(false); // missing zip code
          }
        }
        return AdaptorFactory.getBooleanAdaptor(true);
      }
      return AdaptorFactory.getBooleanAdaptor(true);
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
