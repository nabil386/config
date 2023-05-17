package curam.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventDatastoreConstants;
import curam.datastore.impl.Entity;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;

/**
 * Custom function to validate a pre-populated datastore value change.
 * provided
 *
 */
public class CustomFunctionValidateDatastorePhoneValueChanged
  extends BDMFunctor {

  /**
   * Instantiates a new custom function validate.
   */
  public CustomFunctionValidateDatastorePhoneValueChanged() {

    super();

  }

  /**
   * A custom function that will be called to validate pre-populated datastore
   * value change.
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

    return validateDatastoreValueChanged(rulesParameters);

  }

  /**
   * Checks if a value has changed.
   *
   * Null is equivalent to an empty string.
   *
   * @param entity the existing entity
   * @param name the attribute name
   * @param value the current value
   * @return true if there's a change; otherwise, false
   */
  private boolean hasChanged(final Entity entity, final String name,
    Object value) {

    Object original = entity.getTypedAttribute(name);
    if (original == null) {
      original = "";
    }
    if (value == null) {
      value = "";
    }
    return !value.equals(original);
  }

  /**
   * Validate pre-populated datastore value change.
   *
   * @param rulesParameters the rules parameters in the following order
   * [isValidCommunication, countryCode, phoneAreaCode, phoneNumber, phoneExt,
   * phoneType, isMor, isAftr, isEve, isPreferredCommunication, receiveAlerts,
   * communicationDateChange]
   * @return the validation outcome
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  private Adaptor
    validateDatastoreValueChanged(final RulesParameters rulesParameters)
      throws AppException, InformationalException {

    int index = 0;
    final String countryCode =
      getOptionalStringParam(rulesParameters, index++);
    final String phoneAreaCode =
      getOptionalStringParam(rulesParameters, index++);
    final String phoneNumber =
      getOptionalStringParam(rulesParameters, index++);
    final String phoneExt = getOptionalStringParam(rulesParameters, index++);
    final String phoneType = getOptionalStringParam(rulesParameters, index++);
    final Boolean isMor = getOptionalBooleanParam(rulesParameters, index++);
    final Boolean isAftr = getOptionalBooleanParam(rulesParameters, index++);
    final Boolean isEve = getOptionalBooleanParam(rulesParameters, index++);
    final String isPreferredCommunication =
      getOptionalStringParam(rulesParameters, index++);
    final String receiveAlerts =
      getOptionalStringParam(rulesParameters, index++);
    final String alertFrequency =
      getOptionalStringParam(rulesParameters, index++);

    final Entity rootEntity = readRoot(rulesParameters);
    final Entity personEntity = BDMApplicationEventsUtil
      .retrieveChildEntity(rootEntity, BDMDatastoreConstants.PERSON);

    final Entity[] communicationDetailsEntities =
      BDMApplicationEventsUtil.getEntities(personEntity.getUniqueID(),
        BDMDatastoreConstants.COMMUNICATION_DETAILS);

    if (communicationDetailsEntities != null) {
      for (final Entity communicationDetailsEntityIn : communicationDetailsEntities) {
        if (personEntity
          .getAttribute(
            BDMLifeEventDatastoreConstants.SELECTED_PHONE_NUMBER_ID)
          .equals(communicationDetailsEntityIn.getAttribute(
            BDMLifeEventDatastoreConstants.CURAM_DATASTORE_UNIQUE_ID))) {

          if (hasChanged(communicationDetailsEntityIn,
            BDMDatastoreConstants.COUNTRY_CODE, countryCode)) {
            return AdaptorFactory.getBooleanAdaptor(true);
          }

          if (hasChanged(communicationDetailsEntityIn,
            BDMDatastoreConstants.PHONE_AREA_CODE, phoneAreaCode)) {
            return AdaptorFactory.getBooleanAdaptor(true);
          }

          if (hasChanged(communicationDetailsEntityIn,
            BDMDatastoreConstants.PHONE_NUMBER, phoneNumber)) {
            return AdaptorFactory.getBooleanAdaptor(true);
          }

          if (hasChanged(communicationDetailsEntityIn,
            BDMDatastoreConstants.PHONE_EXT, phoneExt)) {
            return AdaptorFactory.getBooleanAdaptor(true);
          }

          if (hasChanged(communicationDetailsEntityIn,
            BDMDatastoreConstants.PHONE_TYPE, phoneType)) {
            return AdaptorFactory.getBooleanAdaptor(true);
          }

          if (hasChanged(communicationDetailsEntityIn,
            BDMDatastoreConstants.PHONE_IS_MOR, isMor)) {
            return AdaptorFactory.getBooleanAdaptor(true);
          }

          if (hasChanged(communicationDetailsEntityIn,
            BDMDatastoreConstants.PHONE_IS_AFTNOON, isAftr)) {
            return AdaptorFactory.getBooleanAdaptor(true);
          }

          if (hasChanged(communicationDetailsEntityIn,
            BDMDatastoreConstants.PHONE_IS_EVE, isEve)) {
            return AdaptorFactory.getBooleanAdaptor(true);
          }

          if (hasChanged(communicationDetailsEntityIn,
            BDMDatastoreConstants.PREFERRED_COMMUNICATION,
            isPreferredCommunication)) {
            return AdaptorFactory.getBooleanAdaptor(true);
          }

          if (hasChanged(communicationDetailsEntityIn,
            BDMDatastoreConstants.RECEIVE_ALERT, receiveAlerts)) {
            return AdaptorFactory.getBooleanAdaptor(true);
          }
          if (hasChanged(communicationDetailsEntityIn,
            BDMLifeEventDatastoreConstants.ALERT_FREQUENCY, alertFrequency)) {
            return AdaptorFactory.getBooleanAdaptor(true);
          }
        }
      }
    }

    return AdaptorFactory.getBooleanAdaptor(false);
  }
}
