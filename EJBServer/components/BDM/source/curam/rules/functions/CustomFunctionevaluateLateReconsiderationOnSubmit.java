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
 * Contains a custom function that set the reference date for late request
 *
 * @curam.exclude
 */
public class CustomFunctionevaluateLateReconsiderationOnSubmit
  extends BDMFunctor {

  /**
   * A custom function that will reevaluate the late consideration condition for
   * RFR
   *
   * @param rulesParameters The rules parameters containing the object to
   * check. The first parameter in the list of parameters is checked.
   *
   * @return A rule adaptor indicating whether validation passes or fails.
   *
   * @throws AppException Exceptions that may occur when retrieving the
   * adaptor value.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    try {

      // get the datastore root entity identifier from the rules parameters
      final IEG2Context ieg2context = (IEG2Context) rulesParameters;
      final long rootEntityID = ieg2context.getRootEntityID();

      final IEG2ExecutionContext ieg2ExecutionContext =
        new IEG2ExecutionContext(rulesParameters);

      final Datastore datastore = ieg2ExecutionContext.getDatastore();

      final Entity applicationEntity = datastore.readEntity(rootEntityID);

      final Entity rfrDetailsEntity = applicationEntity
        .getChildEntities(datastore.getEntityType("RFRDetails"))[0];

      final String lateReconsiderationReason =
        rfrDetailsEntity.getAttribute("lateReconsiderationReason");

      final Entity ApplicationDetails = applicationEntity
        .getChildEntities(datastore.getEntityType("ApplicationDetails"))[0];

      final boolean lateRequestInd = Boolean
        .parseBoolean(ApplicationDetails.getAttribute("lateRequestInd"));

      if (lateRequestInd && lateReconsiderationReason.isEmpty()) {
        return AdaptorFactory.getBooleanAdaptor(Boolean.FALSE);
      }
      return AdaptorFactory.getBooleanAdaptor(Boolean.TRUE);
    } catch (final Exception e) {

      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + " ERROR while evaluating late reconsideration - " + e.getMessage());
      e.printStackTrace();
      return AdaptorFactory.getBooleanAdaptor(Boolean.TRUE);
    }
  }

}
