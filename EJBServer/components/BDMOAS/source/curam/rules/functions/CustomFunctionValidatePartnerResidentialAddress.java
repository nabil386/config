package curam.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.message.BDMRESTAPIERRORMESSAGE;
import curam.ca.gc.bdmoas.application.impl.BDMOASDatastoreConstants;
import curam.ca.gc.bdmoas.impl.BDMOASConstants;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.ieg.impl.IEG2Context;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Trace;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;

/**
 * Custom Function to validate Residential address.
 * - Custom validations : PO Boxes are not allowed.
 * - External service validation: Invokes WS Address to validate the address
 * provided.
 *
 * @curam.exclude
 */
public class CustomFunctionValidatePartnerResidentialAddress
  extends CustomFunctionValidateAddress {

  /** The PO Box address prefix. */
  public static final String PO_BOX_PREFIX = "PO ";

  public static final String CA_COUNTRY_CODE = "CA";

  /**
   * Instantiates a new custom function validate residential address.
   */
  public CustomFunctionValidatePartnerResidentialAddress() {

    super();

  }

  /**
   * A custom function that will be called to validate an address by checking an
   * external registry of addresses.
   * If the WS is not enable
   * {@value EnvVars#ENV_BDM_WSADDRESS_SERVICE_ENABLEMENT} the callout does not
   * validate the address.
   * If the address is added manually {@value #ID_ADD_MANUAL} , the callout does
   * not validate the address.
   * If the address does not provide the mandatory fields, the callout does not
   * validate the address.
   * If the residential address contains a PO Box address it returns false.
   *
   * @param rulesParameters The rules parameters containing the object to
   * check. The first parameter in the list of parameters is checked.
   *
   * @return A rule adaptor indicating whether the address is valid.
   *
   * @throws AppException Exceptions that may occur when retrieving the
   * adaptor value.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    // BEGIN TASK-9148 Input Application for Client
    final IEG2Context ieg2Context = (IEG2Context) rulesParameters;

    final IEG2ExecutionContext ieg2ExecutionContext =
      new IEG2ExecutionContext(rulesParameters);

    // Read Person Entity
    final Datastore datastore = ieg2ExecutionContext.getDatastore();
    final long rootEntityID = ieg2Context.getRootEntityID();
    final Entity applicationEntity = datastore.readEntity(rootEntityID);

    final Entity personEntity_Partner = applicationEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.PERSON),
      "isPrimaryParticipant==false")[0];

    // Read Residential Entity
    final Entity[] residentialAddressArrya =
      personEntity_Partner.getChildEntities(datastore
        .getEntityType(BDMOASDatastoreConstants.kResidentialAddress));

    Entity residentialAddress;

    if (residentialAddressArrya == null
      || residentialAddressArrya.length == 0) {
      residentialAddress = datastore.newEntity(datastore
        .getEntityType(BDMOASDatastoreConstants.kResidentialAddress));
    } else {
      residentialAddress = residentialAddressArrya[0];
    }
    // Get All parameters
    int index = 0;
    final String id = getOptionalStringParam(rulesParameters, index++);
    final String suiteNum = getOptionalStringParam(rulesParameters, index++);
    final String streetNumber =
      getOptionalStringParam(rulesParameters, index++);
    final String streetName =
      getOptionalStringParam(rulesParameters, index++);
    final String city = getOptionalStringParam(rulesParameters, index++);
    final String province = getOptionalStringParam(rulesParameters, index++);
    final String postalCode =
      getOptionalStringParam(rulesParameters, index++);
    final String country = getOptionalStringParam(rulesParameters, index++);

    // The following verifications must be in order to avoid errors.
    boolean returnValue = VALID_ADDRESS;
    if (country != null && !country.equals("")
      && country.equalsIgnoreCase(CA_COUNTRY_CODE)) {
      // If Address is valid set status to 1 for invalid 2 and for Exception 3
      if (isAddressEmpty(city, province, postalCode)) {
        residentialAddress
          .setAttribute(BDMOASDatastoreConstants.kValidateStatus, "1");
        return AdaptorFactory.getBooleanAdaptor(VALID_ADDRESS);
      }

      // PO Box validation should be trigger regardless it is a manual address
      if (isAddressPOBox(streetName)) {
        residentialAddress
          .setAttribute(BDMOASDatastoreConstants.kValidateStatus, "2");
        return AdaptorFactory.getBooleanAdaptor(!VALID_ADDRESS);
      }

      // check validation is enabled
      if (!super.isWSValidationEnabled()) {
        residentialAddress
          .setAttribute(BDMOASDatastoreConstants.kValidateStatus, "1");
        return AdaptorFactory.getBooleanAdaptor(VALID_ADDRESS);
      }

      // is the address manual, or is this the 2nd attempt to validate
      // BEGIN TASK-28743 Manual Address also need to be validated
      /*
       * if (isAddressManual(id)) {
       * return AdaptorFactory.getBooleanAdaptor(VALID_ADDRESS);
       * } else {
       */

      // END TASK 28743
      // ID will be reset to -1 once user move out of the section to -1 to
      // invoke validate again if user visit this page again
      // as per current implementation -2 is for manuall validation
      if (personEntity_Partner
        .getTypedAttribute(BDMDatastoreConstants.WS_RES_CONTINUE)
        .equals(id)) {
        residentialAddress
          .setAttribute(BDMOASDatastoreConstants.kValidateStatus, "1");
        return AdaptorFactory.getBooleanAdaptor(VALID_ADDRESS);
      }

      personEntity_Partner
        .setTypedAttribute(BDMDatastoreConstants.WS_RES_CONTINUE, id);

      // }

      // If it is not empty, nor PO Box, nor Manual , then validate against the
      // external web service.

      try {
        returnValue = validateAddressWS(suiteNum, streetNumber, streetName,
          city, province, postalCode);

        if (returnValue) {
          residentialAddress
            .setAttribute(BDMOASDatastoreConstants.kValidateStatus, "1");
        } else {
          residentialAddress
            .setAttribute(BDMOASDatastoreConstants.kValidateStatus, "2");
        }

      } catch (final AppException e) {
        // If the server fails to validate for any reason, the callout must
        // allow
        // the process to continue.
        // Note: WS Address Framework need to be corrected, :Invalid and
        // Exception
        // scenarion returns same value.
        returnValue = false;
        if (BDMRESTAPIERRORMESSAGE.BDM_ERR_WS_ADDRESS_INVALID_EXCEPTION
          .getMessageText().equalsIgnoreCase(e.getMessage())
          || BDMRESTAPIERRORMESSAGE.BDM_WSADDRESS_SEARCH_BY_POSTALCODE_REST_API_ERR_EXCEPTION_INVALID_ADDRESS
            .getMessageText().equalsIgnoreCase(e.getMessage())) { // Invalid
                                                                  // Address
          residentialAddress
            .setAttribute(BDMOASDatastoreConstants.kValidateStatus, "2");
        } else { // Exception
          residentialAddress
            .setAttribute(BDMOASDatastoreConstants.kValidateStatus, "3");
        }

        Trace.kTopLevelLogger.info(
          BDMOASConstants.ERROR_OCCURED_VALIDATING_ADDRESS, e.getMessage());
      }
    } else {
      residentialAddress
        .setAttribute(BDMOASDatastoreConstants.kValidateStatus, "1");
    }
    if (residentialAddress.getUniqueID() != 0)

    {
      residentialAddress.update();
    }

    personEntity_Partner.update();
    return AdaptorFactory.getBooleanAdaptor(returnValue);

  }

  /**
   * Checks if is address PO box.
   *
   * @param streetName the street name
   * @return true, if is address PO box
   */
  private boolean isAddressPOBox(final String streetName) {

    return streetName.toUpperCase().startsWith(PO_BOX_PREFIX);
  }

}
