package curam.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.ieg.impl.IEG2Context;
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
public class CustomFunctionResetPartnerAddressValidationFlag
  extends BDMFunctor {

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

    // get the datastore root entity identifier from the rules parameters
    final IEG2Context ieg2context = (IEG2Context) rulesParameters;
    final long rootEntityID = ieg2context.getRootEntityID();

    final IEG2ExecutionContext ieg2ExecutionContext =
      new IEG2ExecutionContext(rulesParameters);

    final Datastore datastore = ieg2ExecutionContext.getDatastore();

    final Entity applicationEntity = datastore.readEntity(rootEntityID);

    final Entity personEntity_Partner = applicationEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.PERSON),
      "isPrimaryParticipant==false")[0];

    personEntity_Partner
      .setTypedAttribute(BDMDatastoreConstants.WS_RES_CONTINUE, ID_RESET);

    personEntity_Partner.update();

    return AdaptorFactory.getBooleanAdaptor(true);
  }

}
