package curam.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMRESTAPIERRORMESSAGE;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.BDMWSAddressInterfaceImpl;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.validatepojos.WsaddrValidationRequest;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.intf.BDMWSAddressInterfaceIntf;
import curam.ca.gc.bdmoas.application.impl.BDMOASDatastoreConstants;
import curam.ca.gc.bdmoas.impl.BDMOASConstants;
import curam.codetable.LOCALE;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.ieg.impl.IEG2Context;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.transaction.TransactionInfo;

/**
 * Custom function to validate an address.
 * - External service validation: Invokes WS Address to validate the address
 * provided
 * {@link curam.ca.gc.bdm.ws.address.impl.BDMWSAddressService#validate(WSAddressRequest)
 * WS Address Validation}.
 *
 * @see EnvVars#ENV_BDM_WSADDRESS_SERVICE_ENABLEMENT
 * @see EnvVars#ENV_BDM_WSADDRESS_TARGET_SYSTEM_NAME
 * @see EnvVars#ENV_BDM_WSADDRESS_SERVICE_TIMEOUT
 * @curam.exclude
 */
public class CustomFunctionValidateWitnessMailingAddress extends BDMFunctor {

  /** The Default Country. */
  private static final String DEFAULT_COUNTRY = "CAN";

  public static final String CA_COUNTRY_CODE = "CA";

  /**
   * This constant has to be sync with the front end "Add Manually" id Option .
   */
  public static final String ID_ADD_MANUAL = "-2";

  /** The Constant for English language. */
  public static final String LANGUAGE_ENGLISH = "English";

  /** The Constant for French language. */
  public static final String LANGUAGE_FRENCH = "French";

  /** Indicates a valid address */
  protected static final boolean VALID_ADDRESS = true;

  /**
   * Instantiates a new custom functionvalidate address.
   */
  public CustomFunctionValidateWitnessMailingAddress() {

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

    if (!isWSValidationEnabled()) {
      return AdaptorFactory.getBooleanAdaptor(VALID_ADDRESS);
    }

    return validateAddress(rulesParameters);

  }

  /**
   * Validate address.
   * If the address was introduced manually , there should not be a validation
   * againsts the web service.
   *
   * @param rulesParameters the rules parameters in the following order
   * [id, suiteNum, streetNumber, streetName, city, province, postalCode]
   * @return the validation outcome
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  private Adaptor validateAddress(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    // BEGIN TASK- 9418 Input Application For Client - AP Script
    final IEG2ExecutionContext ieg2ExecutionContext =
      new IEG2ExecutionContext(rulesParameters);

    final Datastore datastore = ieg2ExecutionContext.getDatastore();

    // Read Person Entity
    final IEG2Context ieg2Context = (IEG2Context) rulesParameters;
    final long rootEntityID = ieg2Context.getRootEntityID();
    final Entity applicationEntity = datastore.readEntity(rootEntityID);

    final Entity declarationEntity =
      applicationEntity.getChildEntities(datastore
        .getEntityType(BDMOASDatastoreConstants.DECLARATION_ENTITY))[0];

    // Read Mailing Address Entity
    final Entity[] mailingAddressArray = declarationEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.MAILING_ADDRESS));
    Entity mailingAddress;
    if (mailingAddressArray == null || mailingAddressArray.length == 0) {
      mailingAddress = datastore.newEntity(
        datastore.getEntityType(BDMDatastoreConstants.MAILING_ADDRESS));
      declarationEntity.addChildEntity(mailingAddress);
      declarationEntity.update();
    } else {
      mailingAddress = mailingAddressArray[0];
    }

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
    boolean returnValue = VALID_ADDRESS;
    final String country = getOptionalStringParam(rulesParameters, index++);
    if (country != null && !country.equals("")
      && country.equalsIgnoreCase(CA_COUNTRY_CODE)) {

      // check validation is enabled
      // If Address is valid set status to 1 for invalid 2 and for Exception 3
      if (!isWSValidationEnabled()) {
        mailingAddress.setAttribute(BDMOASDatastoreConstants.kValidateStatus,
          "1");
        return AdaptorFactory.getBooleanAdaptor(VALID_ADDRESS);

      }

      if (declarationEntity
        .getTypedAttribute(BDMDatastoreConstants.WS_MAIL_CONTINUE)
        .equals(id)) {
        mailingAddress.setAttribute(BDMOASDatastoreConstants.kValidateStatus,
          "1");
        return AdaptorFactory.getBooleanAdaptor(VALID_ADDRESS);
      }

      declarationEntity
        .setTypedAttribute(BDMDatastoreConstants.WS_MAIL_CONTINUE, id);
      declarationEntity.update();
      // }

      // If it is not empty, nor PO Box, nor Manual , then validate against the
      // external web service.
      try {
        // PO Box validation is currently not handled by this implementation for
        // validation
        // The only case where streetname can be null is when it is passed in
        // from a pobox from the UA
        // Adding this check to avoid throwing a misleading error on the user
        // side
        if (streetName != null) {
          returnValue = validateAddressWS(suiteNum, streetNumber, streetName,
            city, province, postalCode);
          if (returnValue) {
            mailingAddress
              .setAttribute(BDMOASDatastoreConstants.kValidateStatus, "1");
          } else {
            mailingAddress
              .setAttribute(BDMOASDatastoreConstants.kValidateStatus, "2");
          }

        } else {
          mailingAddress
            .setAttribute(BDMOASDatastoreConstants.kValidateStatus, "1");
        }
      } catch (final AppException e) {
        // If the server fails to validate for any reason, the callout must
        // allow
        // the process to continue.
        // Note: WS Address Framework need to be corrected, for invalid and
        // Exception scenarion returns same value.
        returnValue = false;
        if (BDMRESTAPIERRORMESSAGE.BDM_ERR_WS_ADDRESS_INVALID_EXCEPTION
          .getMessageText().equalsIgnoreCase(e.getMessage())
          || BDMRESTAPIERRORMESSAGE.BDM_WSADDRESS_SEARCH_BY_POSTALCODE_REST_API_ERR_EXCEPTION_INVALID_ADDRESS
            .getMessageText().equalsIgnoreCase(e.getMessage())) { // Invalid
                                                                  // Address
          mailingAddress
            .setAttribute(BDMOASDatastoreConstants.kValidateStatus, "2");
          // BEGIN TASK 26213 : Does not display validation message

          // END TASK 26213
        } else { // Exception
          mailingAddress
            .setAttribute(BDMOASDatastoreConstants.kValidateStatus, "3");
        }
        //
        Trace.kTopLevelLogger.info(
          BDMOASConstants.ERROR_OCCURED_VALIDATING_ADDRESS, e.getMessage());
      }
    } else {
      mailingAddress.setAttribute(BDMOASDatastoreConstants.kValidateStatus,
        "1");
    }
    if (mailingAddress.getUniqueID() != 0) {
      mailingAddress.update();
    }

    declarationEntity.update();

    return AdaptorFactory.getBooleanAdaptor(returnValue);
  }

  /**
   * Checks if is address empty by checking all mandatory questions of the
   * address.
   *
   * @param city the city
   * @param province the province
   * @param postalCode the postal code
   * @return true, if is address empty
   */
  protected boolean isAddressEmpty(final String city, final String province,
    final String postalCode) {

    return StringUtil.isNullOrEmpty(city)
      && StringUtil.isNullOrEmpty(province)
      && StringUtil.isNullOrEmpty(postalCode);
  }

  /**
   * Checks if the address was added manually. {@value #ID_ADD_MANUAL}
   *
   * @param id the Address ComboBox id
   * @return true, if is address was added manually.
   */
  protected boolean isAddressManual(final String id) {

    return !StringUtil.isNullOrEmpty(id) && id.equals(ID_ADD_MANUAL);
  }

  /**
   * Checks if is validation enable
   * {@value EnvVars#ENV_BDM_WSADDRESS_SERVICE_ENABLEMENT}.
   *
   * @return true, if is validation enable
   */
  protected boolean isWSValidationEnabled() {

    return Configuration
      .getBooleanProperty(EnvVars.BDM_ENV_BDM_WSADDRESS_SERVICE_ENABLEMENT);
  }

  /**
   * Validate address against BDMWSAddress External REST API
   *
   * @param suiteNum the suite num
   * @param streetNumber the address line 1
   * @param streetName the address line 2
   * @param city the city
   * @param province the province
   * @param postalCode the postal code
   * @return true, if successful
   * @throws AppException the app exception
   */
  protected boolean validateAddressWS(final String suiteNum,
    final String streetNumber, final String streetName, final String city,
    final String province, final String postalCode)
    throws AppException, InformationalException {

    /* BEGIN - TASK 9862 - Expose WSAddressRESTAPI for Client portal */
    final BDMWSAddressInterfaceIntf wsAddressImplObj =
      new BDMWSAddressInterfaceImpl();
    final WsaddrValidationRequest addressKey = new WsaddrValidationRequest();
    addressKey.setCanProvinceCode(province);
    addressKey.setNcAddressCityName(city);
    addressKey.setNcAddressPostalCode(postalCode);
    addressKey.setNcCountryCode(DEFAULT_COUNTRY);
    addressKey.setNcLanguageName(getLanguage());
    // This on accounts for the expected AddressFullText format for UrbanAddress
    // "{postCode}{streetNum} StreetName {StreetAddressSequence}
    // {StreetCategoryCode}"
    // To validate against other address types we'll need to validate against
    // the AddressFullText for other address types eg for RuralLockBox
    // "PO BOX {LockBoxNumber} DeliveryInstallationDescription
    // DeliveryInstallationQualifierName"
    addressKey.setNcAddressFullText(postalCode + CuramConst.gkSpace
      + streetNumber + CuramConst.gkSpace + streetName);
    return wsAddressImplObj.validateAddress(addressKey);
    /* END - TASK 9862 - Expose WSAddressRESTAPI for Client portal */

  }

  /**
   * Gets the language. language of the transaction is return.
   *
   * @param addressSearchKey the address search key
   * @return the language
   */
  private String getLanguage() {

    final String programLocale =
      TransactionInfo.getProgramLocale().toLowerCase();
    String languageForWSAddress = "";
    if (programLocale.equalsIgnoreCase(LOCALE.ENGLISH)
      || programLocale.equalsIgnoreCase(LOCALE.ENGLISH_CA)) {
      languageForWSAddress = BDMConstants.kAddress_Search_Locale_en_CA;
    } else if (programLocale.equalsIgnoreCase(LOCALE.FRENCH)) {
      languageForWSAddress = BDMConstants.kAddress_Search_Locale_fr_CA;
    } else {
      languageForWSAddress = BDMConstants.kAddress_Search_Locale_en_CA;
    }
    return languageForWSAddress;

  }
}
