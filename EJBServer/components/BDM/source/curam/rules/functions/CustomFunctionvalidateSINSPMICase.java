package curam.rules.functions;

import curam.ca.gc.bdm.ws.sinvalidation.impl.WSSINValidationRequestDetails;
import curam.datastore.impl.Entity;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.GeneralConstants;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.type.Date;
import java.util.List;

/**
 * Validates personal information within SPM records.
 *
 * @curam.exclude
 */
public class CustomFunctionvalidateSINSPMICase extends SINValidator {

  /**
   * A custom function that will be called to validate a SIN ,
   * first name, last name, date of birth.
   *
   * @param rulesParameters The rules parameters containing the object to
   * check. The first parameter in the list of parameters is checked.
   *
   * @return A rule adaptor indicating whether the details are valid.
   *
   * @throws AppException Exceptions that may occur when retrieving the
   * adaptor value.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    final Entity rootEntity = readRoot(rulesParameters);

    final boolean validSIN = getContinueSIN(rootEntity);

    if (!validSIN) {
      // previous sin check has failed, don't run this one
      return AdaptorFactory.getBooleanAdaptor(true);
    }

    final List<Adaptor> parameters = getParameters();
    final String sin =
      ((StringAdaptor) parameters.get(0)).getStringValue(rulesParameters)
        .replaceAll(GeneralConstants.kSpace, GeneralConstants.kEmpty);

    final String firstName =
      ((StringAdaptor) parameters.get(1)).getStringValue(rulesParameters);

    final String lastName =
      ((StringAdaptor) parameters.get(2)).getStringValue(rulesParameters);

    final Date dob =
      ((DateAdaptor) parameters.get(3)).getValue(rulesParameters);

    final WSSINValidationRequestDetails requestDetails =
      new WSSINValidationRequestDetails();
    requestDetails.setSin(sin);
    requestDetails.setGivenName(firstName);
    requestDetails.setSurname(lastName);
    requestDetails.setDateOfBirth(dob);

    final boolean returnValue = false;
    // returnValue = !firstName.equals("Robert");

    updateContinueSIN(rootEntity, returnValue);

    return AdaptorFactory.getBooleanAdaptor(returnValue);
  }

}
