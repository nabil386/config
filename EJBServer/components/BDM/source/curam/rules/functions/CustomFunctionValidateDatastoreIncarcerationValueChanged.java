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
import curam.util.type.Date;

/**
 * Custom function to validate a pre-populated datastore value change.
 * provided
 *
 */
public class CustomFunctionValidateDatastoreIncarcerationValueChanged
  extends BDMFunctor {

  /**
   * Instantiates a new custom function validate.
   */
  public CustomFunctionValidateDatastoreIncarcerationValueChanged() {

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
    final String instName = getOptionalStringParam(rulesParameters, index++);
    final Date startDateInca = getOptionalDateParam(rulesParameters, index++);
    final Date endDateInca = getOptionalDateParam(rulesParameters, index++);

    final Entity rootEntity = readRoot(rulesParameters);
    final Entity personEntity = BDMApplicationEventsUtil
      .retrieveChildEntity(rootEntity, BDMDatastoreConstants.PERSON);

    final Entity[] incarcerationEntities =
      BDMApplicationEventsUtil.getEntities(personEntity.getUniqueID(),
        BDMLifeEventDatastoreConstants.INCARCERATION);

    if (incarcerationEntities != null) {
      for (final Entity incarcerationEntityIn : incarcerationEntities) {
        if (personEntity
          .getAttribute(
            BDMLifeEventDatastoreConstants.SELECTED_INCARCERATION_ID)
          .equals(incarcerationEntityIn.getAttribute(
            BDMLifeEventDatastoreConstants.CURAM_DATASTORE_UNIQUE_ID))) {

          if (hasChanged(incarcerationEntityIn,
            BDMLifeEventDatastoreConstants.INCARCERATION_Name, instName)) {
            return AdaptorFactory.getBooleanAdaptor(true);
          }

          if (hasChanged(incarcerationEntityIn,
            BDMLifeEventDatastoreConstants.INCARCERATION_START_DATE,
            startDateInca)) {
            return AdaptorFactory.getBooleanAdaptor(true);
          }

          if (hasChanged(incarcerationEntityIn,
            BDMLifeEventDatastoreConstants.INCARCERATION_END_DATE,
            endDateInca)) {
            return AdaptorFactory.getBooleanAdaptor(true);
          }
        }
      }
    }

    return AdaptorFactory.getBooleanAdaptor(false);
  }
}
