package curam.rules.functions;

import curam.ca.gc.bdm.impl.BDMConstants;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.ieg.impl.IEG2Context;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Trace;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;

/**
 * Contains a custom function that will set PersonID for the Voluntary Tax
 * Withdrawal
 *
 * @since ADO-13709
 * @curam.exclude
 */
public class CustomFunctionSetVTWPersonID extends BDMFunctor {

  /**
   * A custom function that will set PersonID for the Voluntary Tax Withdrawal
   *
   * @param rulesParameters will have no parameters.
   *
   * @return A rule adaptor containing the previous tax year.
   *
   * @throws AppException Exceptions that may occur when retrieving the
   * adaptor value.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    try {
      // using IEG contect fetch root entity ID
      final IEG2Context ieg2context = (IEG2Context) rulesParameters;
      final long rootEntityID = ieg2context.getRootEntityID();

      final IEG2ExecutionContext ieg2ExecutionContext =
        new IEG2ExecutionContext(rulesParameters);
      final Datastore datastore = ieg2ExecutionContext.getDatastore();

      final Entity applicationEntity = datastore.readEntity(rootEntityID);
      final Entity taxInfoEntity = applicationEntity
        .getChildEntities(datastore.getEntityType("TaxInfo"))[0];

      final Entity primaryPersonEntity =
        applicationEntity.getChildEntities(datastore.getEntityType("Person"),
          "isPrimaryParticipant==true")[0];
      taxInfoEntity.setTypedAttribute("taxPersonID",
        primaryPersonEntity.getUniqueID());
      taxInfoEntity.update();
    } catch (final Exception e) {

      Trace.kTopLevelLogger
        .info(BDMConstants.BDM_LOGS_PREFIX + " Error in tax info update");

    }

    return AdaptorFactory.getBooleanAdaptor(true);
  }

}
