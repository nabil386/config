package curam.rules.functions;

import curam.codetable.COUNTRY;
import curam.util.exception.AppException;
import curam.util.exception.AppRuntimeException;
import curam.util.exception.DatabaseException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.type.CodeTable;

/**
 * Contains a custom function to validate min and max number of characters in a
 * given string.
 *
 * Same as validateLength in BDM but eliminating the numeric check
 *
 *
 * @curam.exclude
 */
public class CustomFunctionValidateAddressLength extends BDMFunctor {

  /**
   * A custom function to validate address length.
   *
   * @param rulesParameters The rules parameters containing the objects to
   * check.
   * -firstParameter, secondParameter, third Parameter are the three address
   * fields being checked.
   * -country: The given address country.
   * -type: If true then address check includes provinces or US States, change
   * the saved abbreviated version to full name of province/state.
   * true: "Unit/Apt/Suite Number, Street 1 and Street 2 combined must be less
   * than 40 characters."
   * false:"City, Town, Village, State/Province/Region and Zip Code combined
   * must be less than 40 characters."
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

    final Adaptor firstParameter = getParameters().get(0);
    final Adaptor secondParameter = getParameters().get(1);
    final Adaptor thirdParameter = getParameters().get(2);

    final String country = getOptionalStringParam(rulesParameters, 3);
    final boolean validationType =
      getOptionalBooleanParam(rulesParameters, 4);

    boolean result = true;
    try {

      final String firstStr = firstParameter == null ? ""
        : getStringValue(rulesParameters, firstParameter);

      String secondStr = secondParameter == null ? ""
        : getStringValue(rulesParameters, secondParameter);

      if (validationType && country.equals(COUNTRY.CA)) {
        secondStr = getProvince(secondStr);

      } else if (validationType && country.equals(COUNTRY.US)) {
        secondStr = getState(secondStr);

      }

      final String thirdStr = thirdParameter == null ? ""
        : getStringValue(rulesParameters, thirdParameter);

      if (firstStr.length() + secondStr.length() + thirdStr.length() >= 40) {
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

  /**
   * Gets the full description name of the province.
   *
   * @param enteredProv the abbreviated name of province
   * @return the full description name
   */
  private String getProvince(final String enteredProv)
    throws DatabaseException, AppRuntimeException, AppException,
    InformationalException {

    final String desc =
      CodeTable.getOneItemForUserLocale("ProvinceType", enteredProv);

    return desc;
  }

  /**
   * Gets the full description name of the state.
   *
   * @param enteredProv the abbreviated name of state.
   * @return the full description name
   */
  private String getState(final String enteredState) throws DatabaseException,
    AppRuntimeException, AppException, InformationalException {

    final String desc =
      CodeTable.getOneItemForUserLocale("AddressState", enteredState);

    return desc;
  }

}
