package curam.rules.functions;

import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;

/**
 * Contains a custom function that will validate whether Street name and Street
 * number,
 * or PO Box have been entered for a US address
 *
 */

public class CustomFunctionValidateMailingAddressFields extends BDMFunctor {

  /**
   * Validation Rule: Either "Street name and Street number, or PO Box/Rural
   * Route/General Delivery" must be entered.
   *
   * @param rulesParameters The rules parameters containing the object to
   * check. All parameters in the list of parameters are checked.
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

    int index = 0;
    final String pobox = getOptionalStringParam(rulesParameters, index++);
    final String streetNumber =
      getOptionalStringParam(rulesParameters, index++);
    final String streetName =
      getOptionalStringParam(rulesParameters, index++);

    if ((streetName == null || streetName.isEmpty())
      && (streetNumber == null || streetNumber.isEmpty()) && pobox != null
      && !pobox.isEmpty()) {
      // street number and street name
      return AdaptorFactory.getBooleanAdaptor(true);

    } else if ((pobox == null || pobox.isEmpty()) && streetName != null
      && !streetName.isEmpty() && streetNumber != null
      && !streetNumber.isEmpty()) {
      // PO Box
      return AdaptorFactory.getBooleanAdaptor(true);

    } else {
      return AdaptorFactory.getBooleanAdaptor(false);
    }
  }
}
