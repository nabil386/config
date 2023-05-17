package curam.rules.functions;

import curam.ca.gc.bdm.ws.sinvalidation.impl.WSSINValidationRequestDetails;
import curam.ca.gc.bdm.ws.sinvalidation.impl.WSSINValidationService;
import curam.core.impl.EnvVars;
import curam.datastore.impl.Entity;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.GeneralConstants;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.type.Date;

/**
 * Contains a custom function that will call an external service to
 * determine if a SIN is valid.
 *
 * @curam.exclude
 */
public class CustomFunctionValidateSINService extends SINValidator {

  /**
   * A custom function that will be called to validate a SIN by checking an
   * external registry of SINs.
   *
   * @param rulesParameters The rules parameters containing the object to
   * check. The first parameter in the list of parameters is checked.
   *
   * @return A rule adaptor indicating whether the SIN is valid.
   *
   * @throws AppException Exceptions that may occur when retrieving the
   * adaptor value.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    if (!Configuration
      .getBooleanProperty(EnvVars.BDM_ENV_BDM_SIN_SERVICE_VALIDATION)) {
      return AdaptorFactory.getBooleanAdaptor(true);
    }

    final Entity rootEntity = readRoot(rulesParameters);

    final boolean validSIN = getContinueSIN(rootEntity);

    if (!validSIN) {
      // previous sin check has failed, don't run this one
      return AdaptorFactory.getBooleanAdaptor(true);
    }

    final String sin =
      ((StringAdaptor) getParameters().get(0)).getStringValue(rulesParameters)
        .replaceAll(GeneralConstants.kSpace, GeneralConstants.kEmpty);

    final String firstName = ((StringAdaptor) getParameters().get(1))
      .getStringValue(rulesParameters);

    final String lastName = ((StringAdaptor) getParameters().get(2))
      .getStringValue(rulesParameters);

    final Date dob =
      ((DateAdaptor) getParameters().get(3)).getValue(rulesParameters);

    final WSSINValidationRequestDetails requestDetails =
      new WSSINValidationRequestDetails();
    requestDetails.setSin(sin);
    requestDetails.setGivenName(firstName);
    requestDetails.setSurname(lastName);
    requestDetails.setDateOfBirth(dob);

    final WSSINValidationService extService = new WSSINValidationService();

    boolean returnValue = false;
    try {
      returnValue = extService.validate(requestDetails);
    } catch (final AppException e) {
      // Currently no way of giving more details to the user,
      // just that the SIN is invalid
    }

    updateContinueSIN(rootEntity, returnValue);

    return AdaptorFactory.getBooleanAdaptor(returnValue);
  }

}
