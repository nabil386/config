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
public class CustomFunctionValidateDatastoreNameValueChanged
  extends BDMFunctor {

  /**
   * Instantiates a new custom function validate.
   */
  public CustomFunctionValidateDatastoreNameValueChanged() {

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
   * [firstName, middleName, lastName, PreferredName, dateChanged]
   * @return the validation outcome
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  private Adaptor
    validateDatastoreValueChanged(final RulesParameters rulesParameters)
      throws AppException, InformationalException {

    int index = 0;
    final String firstName = getOptionalStringParam(rulesParameters, index++);
    final String middleName =
      getOptionalStringParam(rulesParameters, index++);
    final String lastName = getOptionalStringParam(rulesParameters, index++);
    final String PreferredName =
      getOptionalStringParam(rulesParameters, index++);
    final Date dateChanged = getOptionalDateParam(rulesParameters, index++);

    if (dateChanged == null) {
      final Entity rootEntity = readRoot(rulesParameters);
      final Entity personEntity = BDMApplicationEventsUtil
        .retrieveChildEntity(rootEntity, BDMDatastoreConstants.PERSON);
      if (!StringUtil.isNullOrEmpty(firstName)) {
        if (personEntity.getTypedAttribute(BDMDatastoreConstants.FIRST_NAME)
          .equals(firstName)) { // no firstName change
          if (!StringUtil.isNullOrEmpty(middleName)) {
            if (personEntity
              .getTypedAttribute(BDMDatastoreConstants.MIDDLE_NAME)
              .equals(middleName)) { // no middleName change
              if (!StringUtil.isNullOrEmpty(lastName)) {
                if (personEntity
                  .getTypedAttribute(BDMDatastoreConstants.LAST_NAME)
                  .equals(lastName)) { // no lastName change
                  if (!StringUtil.isNullOrEmpty(PreferredName)) {
                    if (personEntity
                      .getTypedAttribute(BDMDatastoreConstants.PREFERRED_NAME)
                      .equals(PreferredName)) { // no fields changed
                      return AdaptorFactory.getBooleanAdaptor(true);
                    } else // PreferredName changed
                      return AdaptorFactory.getBooleanAdaptor(false);
                  }
                } else // lastName changed
                  return AdaptorFactory.getBooleanAdaptor(false);
              }
            } else // middleName changed
              return AdaptorFactory.getBooleanAdaptor(false);
          }
        } else // firstName changed
          return AdaptorFactory.getBooleanAdaptor(false);
      }
    } else // dateChanged not empty
      return AdaptorFactory.getBooleanAdaptor(true);

    return AdaptorFactory.getBooleanAdaptor(true);
  }
}
