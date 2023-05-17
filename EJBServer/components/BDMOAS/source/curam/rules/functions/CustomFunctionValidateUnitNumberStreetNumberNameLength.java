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
 *
 * Same as validateLength in BDM but eliminating the numeric check
 *
 *
 * @curam.exclude
 */
public class CustomFunctionValidateUnitNumberStreetNumberNameLength
  extends BDMFunctor {

  /**
   * A custom function to validate address length.
   *
   * @param rulesParameters The rules parameters containing the objects to
   * check.
   * checks for length of unit/apt number, street number/street 1, and street
   * name/street 2
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

    final Adaptor unitNumberAdaptor = getParameters().get(0);
    final Adaptor streetNumberAdaptor = getParameters().get(1);
    final Adaptor streetNameAdaptor = getParameters().get(2);

    final Integer addresLineLimit = Integer
      .parseInt(Configuration.getProperty(EnvVars.BDM_ADDRESSLINE_LENGTH));

    boolean result = true;
    try {

      final String unitNumber = unitNumberAdaptor == null ? ""
        : getStringValue(rulesParameters, unitNumberAdaptor).trim();

      final String streetNumber = streetNumberAdaptor == null ? ""
        : getStringValue(rulesParameters, streetNumberAdaptor).trim();

      final String streetName = streetNameAdaptor == null ? ""
        : getStringValue(rulesParameters, streetNameAdaptor).trim();

      if ((unitNumber + CuramConst.gkSpace + streetNumber + CuramConst.gkSpace
        + CuramConst.gkSpace + streetName).length() >= addresLineLimit) {
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
