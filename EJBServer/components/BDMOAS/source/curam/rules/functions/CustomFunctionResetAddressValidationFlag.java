package curam.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.datastore.impl.Entity;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;

/**
 * BDMOAS FEATURE 52093: Class Added
 * Contains a custom function that will Reset validate Flag
 *
 * @author SMisal
 *
 */
public class CustomFunctionResetAddressValidationFlag extends BDMFunctor {

  /**
   * A custom function that will rest ID value to invoke validate if user
   * validate the page again
   * date.
   *
   *
   * @return A rule adaptor .
   *
   * @throws AppException Exceptions that may occur when retrieving the
   * adaptor value.
   * @throws InformationalException Generic Exception Signature.
   */

  public static final String ID_RESET = "-1";

  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    final Entity rootEntity = readRoot(rulesParameters);
    // Read Person Entity
    final Entity personEntity = BDMApplicationEventsUtil
      .retrieveChildEntity(rootEntity, BDMDatastoreConstants.PERSON);
    personEntity.setTypedAttribute(BDMDatastoreConstants.WS_RES_CONTINUE,
      ID_RESET);
    personEntity.setTypedAttribute(BDMDatastoreConstants.WS_MAIL_CONTINUE,
      ID_RESET);
    personEntity.update();
    return AdaptorFactory.getBooleanAdaptor(true);
  }

}
