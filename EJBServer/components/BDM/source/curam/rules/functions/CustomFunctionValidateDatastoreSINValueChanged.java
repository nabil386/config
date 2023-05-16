package curam.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.datastore.impl.Entity;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.StringUtil;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.type.Date;

/**
 * Custom function to validate a pre-populated datastore value change.
 * provided
 *
 */
public class CustomFunctionValidateDatastoreSINValueChanged
  extends BDMFunctor {

  /**
   * Instantiates a new custom function validate.
   */
  public CustomFunctionValidateDatastoreSINValueChanged() {

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
   * Validate pre-populated datastore value change.
   *
   * @param rulesParameters the rules parameters in the following order
   * [sinRaw, sinRawDate]
   * @return the validation outcome
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  private Adaptor
    validateDatastoreValueChanged(final RulesParameters rulesParameters)
      throws AppException, InformationalException {

    int index = 0;
    final String sinRaw = getOptionalStringParam(rulesParameters, index++);
    final Date sinRawDate = getOptionalDateParam(rulesParameters, index++);

    if (sinRawDate == null) {
      final Entity rootEntity = readRoot(rulesParameters);
      final Entity personEntity = BDMApplicationEventsUtil
        .retrieveChildEntity(rootEntity, BDMDatastoreConstants.PERSON);

      if (!StringUtil.isNullOrEmpty(sinRaw)) {
        if (personEntity.getTypedAttribute(BDMDatastoreConstants.SIN)
          .equals(sinRaw)) {
          // no change
          return AdaptorFactory.getBooleanAdaptor(true);
        } else {
          // sin value changed and sinRawDateChange is empty
          return AdaptorFactory.getBooleanAdaptor(false);
        }
      }
    } else {
      // sinRawDate not empty
      return AdaptorFactory.getBooleanAdaptor(true);
    }
    return AdaptorFactory.getBooleanAdaptor(false);
  }
}
