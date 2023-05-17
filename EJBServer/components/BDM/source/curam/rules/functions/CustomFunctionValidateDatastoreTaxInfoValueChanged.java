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
 */
public class CustomFunctionValidateDatastoreTaxInfoValueChanged
  extends BDMFunctor {

  /**
   * Instantiates a new custom function validate.
   */
  public CustomFunctionValidateDatastoreTaxInfoValueChanged() {

    super();

  }

  /**
   * A custom function that will be called to validate pre-populated datastore
   * value change.
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

    return validateDatastoreValueChanged(rulesParameters);

  }

  /**
   * Checks if a value has changed.
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
   * [dedMethod, dollarAmount, dollarPerct, startDate, changeEffectiveDate,
   * endDate]
   * @return the validation outcome
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  private Adaptor
    validateDatastoreValueChanged(final RulesParameters rulesParameters)
      throws AppException, InformationalException {

    int index = 0;
    final String dedMethod = getOptionalStringParam(rulesParameters, index++);
    final String dollarAmount =
      getOptionalMoneyParam(rulesParameters, index++);
    final String dollarPerct =
      getOptionalDoubleParam(rulesParameters, index++);
    final Date startDate = getOptionalDateParam(rulesParameters, index++);
    final Date changeEffectiveDate =
      getOptionalDateParam(rulesParameters, index++);
    final Date endDate = getOptionalDateParam(rulesParameters, index++);

    // If there is no change effective date, then this is and add case, and
    // comparison is not required
    if (changeEffectiveDate == null) {
      return AdaptorFactory.getBooleanAdaptor(true);
    }

    final Entity rootEntity = readRoot(rulesParameters);
    final Entity personEntity = BDMApplicationEventsUtil
      .retrieveChildEntity(rootEntity, BDMDatastoreConstants.PERSON);
    final Entity[] taxInfoEntities = BDMApplicationEventsUtil.getEntities(
      personEntity.getUniqueID(), BDMLifeEventDatastoreConstants.TAX_INFO);

    if (taxInfoEntities != null) {
      for (final Entity taxInfoEntityIn : taxInfoEntities) {
        if (taxInfoEntityIn
          .getAttribute(
            BDMLifeEventDatastoreConstants.TAX_TAX_WITHHOLD_SELECTED)
          .equals("true")) {
          if (hasChanged(taxInfoEntityIn,
            BDMLifeEventDatastoreConstants.TAX_DED_METHOD, dedMethod)) {
            return AdaptorFactory.getBooleanAdaptor(true);
          }
          if (hasChanged(taxInfoEntityIn,
            BDMLifeEventDatastoreConstants.TAX_DOLLAR_AMOUNT, dollarAmount)) {
            return AdaptorFactory.getBooleanAdaptor(true);
          }
          if (hasChanged(taxInfoEntityIn,
            BDMLifeEventDatastoreConstants.TAX_DOLLAR_PERCENT, dollarPerct)) {
            return AdaptorFactory.getBooleanAdaptor(true);
          }
          if (hasChanged(taxInfoEntityIn,
            BDMLifeEventDatastoreConstants.TAX_START_DATE, startDate)) {
            return AdaptorFactory.getBooleanAdaptor(true);
          }
          if (hasChanged(taxInfoEntityIn,
            BDMLifeEventDatastoreConstants.TAX_CHANGE_EFFECTIVE_DATE,
            changeEffectiveDate)) {
            return AdaptorFactory.getBooleanAdaptor(true);
          }
          if (hasChanged(taxInfoEntityIn,
            BDMLifeEventDatastoreConstants.TAX_END_DATE, endDate)) {
            return AdaptorFactory.getBooleanAdaptor(true);
          }
        }
      }
    }
    return AdaptorFactory.getBooleanAdaptor(false);
  }
}
