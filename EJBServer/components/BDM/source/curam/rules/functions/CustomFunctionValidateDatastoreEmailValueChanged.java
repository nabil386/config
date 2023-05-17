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
public class CustomFunctionValidateDatastoreEmailValueChanged
  extends BDMFunctor {

  /**
   * Instantiates a new custom function validate.
   */
  public CustomFunctionValidateDatastoreEmailValueChanged() {

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
    String value) {

    Object original = null;

    if (entity != null) {
      original = entity.getTypedAttribute(name);

    }
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
   * [emailAdrValid, emailType, emailAdr, isPreferredCommunication,
   * alertPrefEmail]
   * @return the validation outcome
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  private Adaptor
    validateDatastoreValueChanged(final RulesParameters rulesParameters)
      throws AppException, InformationalException {

    int index = 0;
    final String emailType = getOptionalStringParam(rulesParameters, index++);
    final String emailAdr = getOptionalStringParam(rulesParameters, index++);
    final String isPreferredCommunication =
      getOptionalStringParam(rulesParameters, index++);
    final String alertPrefEmail =
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
      for (final Entity communicationEntity : communicationDetailsEntities) {
        if (personEntity
          .getAttribute(
            BDMLifeEventDatastoreConstants.SELECTED_EMAIL_ADDRESS_ID)
          .equals(communicationEntity.getAttribute(
            BDMLifeEventDatastoreConstants.CURAM_DATASTORE_UNIQUE_ID))) {
          if (!hasChanged(communicationEntity,
            BDMDatastoreConstants.EMAIL_TYPE, emailType)
            && !hasChanged(communicationEntity,
              BDMDatastoreConstants.EMAIL_ADDRESS, emailAdr)
            && !hasChanged(communicationEntity,
              BDMDatastoreConstants.PREFERRED_COMMUNICATION,
              isPreferredCommunication)
            && !hasChanged(communicationEntity,
              BDMDatastoreConstants.ALT_PREF_EMAIL, alertPrefEmail)
            && !hasChanged(communicationEntity,
              BDMLifeEventDatastoreConstants.ALERT_FREQUENCY,
              alertFrequency)) {
            // If nothing has been updated then return false
            return AdaptorFactory.getBooleanAdaptor(false);
          }
        }

      }

    }

    return AdaptorFactory.getBooleanAdaptor(true);
  }
}
