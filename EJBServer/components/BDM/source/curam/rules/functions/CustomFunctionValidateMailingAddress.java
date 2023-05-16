package curam.rules.functions;

import curam.codetable.impl.COUNTRYEntry;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.StringUtil;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;

/**
 * Custom function to validate a mailing address pobox.
 */
public class CustomFunctionValidateMailingAddress extends BDMFunctor {

  /** Indicates a valid address */
  protected static final boolean VALID_ADDRESS = true;

  /** Indicates an invalid address */
  protected static final boolean INVALID_ADDRESS = false;

  /** The PO Box address prefix. */
  public static final String PO_BOX_PREFIX = "PO ";

  /**
   * Instantiates a new custom functionvalidate address.
   */
  public CustomFunctionValidateMailingAddress() {

    super();
  }

  /**
   * A custom function that will be called to validate po box
   *
   * @param rulesParameters The rules parameters containing the object to
   * check. The first parameter in the list of parameters is checked.
   * @return A rule adaptor indicating whether the address is valid.
   * @throws AppException Exceptions that may occur when retrieving the
   * adaptor value.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    return validateAddress(rulesParameters);

  }

  /**
   * Validate address.
   *
   * @param rulesParameters the rules parameters in the following order
   * [id, suiteNum, streetNumber, streetName, city, province, postalCode]
   * @return the validation outcome
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  private Adaptor validateAddress(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    int index = 0;
    final String country = getOptionalStringParam(rulesParameters, index++);
    final String pobox = getOptionalStringParam(rulesParameters, index++);
    final String streetNumber =
      getOptionalStringParam(rulesParameters, index++);
    final String streetName =
      getOptionalStringParam(rulesParameters, index++);
    final String suiteNum = getOptionalStringParam(rulesParameters, index++);
    final String intlpobox = getOptionalStringParam(rulesParameters, index++);
    final String intlstreetNumber =
      getOptionalStringParam(rulesParameters, index++);
    final String intlstreetName =
      getOptionalStringParam(rulesParameters, index++);
    final String intlsuiteNum =
      getOptionalStringParam(rulesParameters, index++);

    boolean addressEmpty = INVALID_ADDRESS;
    boolean addressNotComplete = INVALID_ADDRESS;
    boolean poboxEmpty = VALID_ADDRESS;
    boolean returnValue = INVALID_ADDRESS;
    if (country.equals(COUNTRYEntry.CA.toString())) {
      addressEmpty =
        !isAnyAddressFieldFilled(streetNumber, streetName, suiteNum);
      addressNotComplete =
        isAnyAddressFieldEmpty(streetNumber, streetName, suiteNum);
      poboxEmpty = StringUtil.isNullOrEmpty(pobox);
    } else {
      addressEmpty = !isAnyAddressFieldFilled(intlstreetNumber,
        intlstreetName, intlsuiteNum);
      if (country.equals(COUNTRYEntry.US.toString())) {
        addressNotComplete = isAnyAddressFieldEmpty(intlstreetNumber,
          intlstreetName, intlsuiteNum);
      } else {
        addressNotComplete = isAnyAddressFieldEmptyForNonUS_CA(
          intlstreetNumber, intlstreetName, intlsuiteNum);
      }
      poboxEmpty = StringUtil.isNullOrEmpty(intlpobox);
    }

    if (addressEmpty) {
      if (poboxEmpty) {
        // If both address and PO box are empty then the address is invalid
        returnValue = INVALID_ADDRESS;
      } else {
        // If address is empty and PO box is filled then the address is valid
        returnValue = VALID_ADDRESS;
      }
    } else if (addressNotComplete) {
      returnValue = INVALID_ADDRESS;
    } else {
      // The Address is completed and there is valid
      returnValue = VALID_ADDRESS;
    }

    // Check that PO box contains a box Number
    if (country.equals(COUNTRYEntry.CA.toString()) && !poboxEmpty) {
      if (pobox.toUpperCase().startsWith(PO_BOX_PREFIX)) {
        if (pobox.length() >= 4) {
          final char isInt = pobox.toCharArray()[3];
          if (!Character.isDigit(isInt)) {
            returnValue = INVALID_ADDRESS;
          }
        } else {
          returnValue = INVALID_ADDRESS;
        }
      }
    }

    return AdaptorFactory.getBooleanAdaptor(returnValue);
  }

  /**
   * Checks if any address field is filled by checking all optional questions of
   * the
   * address.
   *
   * @param streetNumber
   * @param streetName
   * @param suiteNum
   * @return true, if any address is filled
   */
  protected boolean isAnyAddressFieldFilled(final String streetNumber,
    final String streetName, final String suiteNum) {

    if (!StringUtil.isNullOrEmpty(streetNumber)
      || !StringUtil.isNullOrEmpty(streetName)
      || !StringUtil.isNullOrEmpty(suiteNum)) {
      return true;
    }
    return false;
  }

  /**
   * Checks if any address field is empty by checking all optional questions of
   * the
   * address.
   *
   * @param streetNumber
   * @param streetName
   * @param suiteNum
   * @return true, if any address is empty
   */
  protected boolean isAnyAddressFieldEmpty(final String streetNumber,
    final String streetName, final String suiteNum) {

    // BEGIN TASK-55231 Suite Number is not mandatory in Address
    if (StringUtil.isNullOrEmpty(streetNumber)
      || StringUtil.isNullOrEmpty(streetName)
    // || StringUtil.isNullOrEmpty(suiteNum)
    ) {
      return true;
    }
    return false;
  }

  /**
   * TASK-55638_Invalid_Error_Message_For_Street_Number_On_Mailing_Address
   * Checks if any address field is empty by checking all optional questions of
   * the
   * address.
   *
   * @param streetNumber
   * @param streetName
   * @param suiteNum
   * @return true, if any address is empty
   */
  protected boolean isAnyAddressFieldEmptyForNonUS_CA(
    final String streetNumber, final String streetName,
    final String suiteNum) {

    // BEGIN TASK-55231 Suite Number is not mandatory in Address
    if (StringUtil.isNullOrEmpty(streetNumber)
    // || StringUtil.isNullOrEmpty(streetName)
    // || StringUtil.isNullOrEmpty(suiteNum)
    ) {
      return true;
    }
    return false;
  }

}
