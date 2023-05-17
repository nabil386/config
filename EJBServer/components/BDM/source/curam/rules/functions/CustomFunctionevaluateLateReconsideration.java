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
import curam.util.type.Date;
import curam.workspaceservices.util.impl.DateTimeTools;

/**
 * Contains a custom function that set the reference date for late request
 *
 * @curam.exclude
 */
public class CustomFunctionevaluateLateReconsideration extends BDMFunctor {

  /**
   * A custom function that will set reference date for late request
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

      final Entity ApplicationDetails = applicationEntity
        .getChildEntities(datastore.getEntityType("ApplicationDetails"))[0];

      // The late request for reconsideration option is only displayed if
      // current date > 30 days from date of decision entered (the 30 days is
      // configurable by program).*Include date of decision (Start counting date
      // of decision as Day 1)
      final Date today = curam.util.type.Date.getCurrentDate();
      final String numOfDaysForLateRequest =
        ApplicationDetails.getAttribute("numOfDaysForLateRequest");
      final int numOfDays = Integer.parseInt(numOfDaysForLateRequest) + 1;
      ApplicationDetails.setAttribute(BDMConstants.REF_DATE_FOR_LATE_REQUEST,
        DateTimeTools.formatDateISO(today.addDays(-numOfDays)));
      ApplicationDetails.update();
      return AdaptorFactory.getBooleanAdaptor(Boolean.TRUE);
    } catch (final Exception e) {

      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + " ERROR while evaluating late reconsideration - " + e.getMessage());
      e.printStackTrace();
      return AdaptorFactory.getBooleanAdaptor(Boolean.TRUE);
    }
  }

}
