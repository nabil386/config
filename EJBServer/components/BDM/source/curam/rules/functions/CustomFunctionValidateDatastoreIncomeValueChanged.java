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

public class CustomFunctionValidateDatastoreIncomeValueChanged
  extends BDMFunctor {

  /**
   * Instantiates a new custom function validate.
   */
  public CustomFunctionValidateDatastoreIncomeValueChanged() {

    super();
  }

  /**
   * A custom function that will be called to validate pre-populated datastore
   * value change.
   *
   * @param rulesParameters The rules parameters containing the object to
   * check. The first parameter in the list of parameters is checked.
   *
   * @return A rule adaptor indicating whether the value is changed.
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
    return !original.toString().equals(value.toString());
  }

  /**
   * Validate pre-populated datastore value change.
   *
   * @param rulesParameters the rules parameters in the following order
   * [incomeAmount]
   * @return the validation outcome
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  private Adaptor
    validateDatastoreValueChanged(final RulesParameters rulesParameters)
      throws AppException, InformationalException {

    int index = 0;
    final String incomeAmount =
      getOptionalMoneyParam(rulesParameters, index++);

    final Entity rootEntity = readRoot(rulesParameters);
    final Entity personEntity = BDMApplicationEventsUtil
      .retrieveChildEntity(rootEntity, BDMDatastoreConstants.PERSON);
    final Entity incomeEntity = BDMApplicationEventsUtil.retrieveChildEntity(
      personEntity, BDMLifeEventDatastoreConstants.INCOME);

    if (incomeEntity != null) {
      if (hasChanged(incomeEntity,
        BDMLifeEventDatastoreConstants.INCOME_AMOUNT, incomeAmount)) {
        return AdaptorFactory.getBooleanAdaptor(true);
      } else {
        return AdaptorFactory.getBooleanAdaptor(false);
      }

    }

    return AdaptorFactory.getBooleanAdaptor(true);
  }
}
