package curam.rules.functions;

import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;

/**
 * Contains a custom function to validate min and max number of characters in a
 * given string.
 *
 * Same as validateLength in BDM but eliminating the numeric check
 *
 *
 * @curam.exclude
 */
public class CustomFunctionValidateCityProvincePostalCodeLength
  extends BDMFunctor {

  /**
   * A custom function to validate address length.
   *
   * @param rulesParameters The rules parameters containing the objects to
   * check.
   *
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

    final Adaptor cityAdaptor = getParameters().get(0);
    final Adaptor provinceAdaptor = getParameters().get(1);
    final Adaptor postalCodeAdaptor = getParameters().get(2);

    final Integer addresLineLimit = Integer
      .parseInt(Configuration.getProperty(EnvVars.BDM_ADDRESSLINE_LENGTH));

    boolean result = true;
    try {

      final String city = cityAdaptor == null ? ""
        : getStringValue(rulesParameters, cityAdaptor).trim();

      final String province = provinceAdaptor == null ? ""
        : getStringValue(rulesParameters, provinceAdaptor).trim();

      final String postalCode = postalCodeAdaptor == null ? ""
        : getStringValue(rulesParameters, postalCodeAdaptor).trim()
          .replaceAll(CuramConst.gkSpace, CuramConst.gkEmpty);

      if ((city + CuramConst.gkSpace + province + CuramConst.gkSpace
        + CuramConst.gkSpace + postalCode).length() >= addresLineLimit) {
        result = false;
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
