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
 * Contains a custom function that will validate if the period is overlapping
 * with existing periods
 *
 * @curam.exclude
 */
public class CustomFunctionValidateTaxWithholdPeriodNotOverlapping
  extends BDMFunctor {

  /**
   *
   * @param rulesParameters The rules parameters containing the object to
   * check. The first parameter in the list of parameters is checked.
   * @return A rule adaptor indicating whether validation passes or fails.
   * @throws AppException Exceptions that may occur when retrieving the
   * adaptor value.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    final DateAdaptor fromDateAdapter = (DateAdaptor) getParameters().get(0);
    final Date inputFromDate = fromDateAdapter.getValue(rulesParameters);
    final DateAdaptor toDateAdapter = (DateAdaptor) getParameters().get(1);
    final Date inputToDate = toDateAdapter != null
      ? toDateAdapter.getValue(rulesParameters) : Date.kZeroDate;

    // using IEG context fetch root entity ID
    final Entity rootEntity = readRoot(rulesParameters);
    // this is one person entity on life event
    final Entity personEntity = BDMApplicationEventsUtil
      .retrieveChildEntity(rootEntity, BDMDatastoreConstants.PERSON);

    final Entity[] taxInfoEntities = BDMApplicationEventsUtil.getEntities(
      personEntity.getUniqueID(), BDMLifeEventDatastoreConstants.TAX_INFO);

    if (taxInfoEntities != null) {
      for (final Entity taxInfoEntityIn : taxInfoEntities) {
        if (!taxInfoEntityIn
          .getAttribute(
            BDMLifeEventDatastoreConstants.TAX_TAX_WITHHOLD_SELECTED)
          .equals("true")) {
          final Date fromDate = Date.fromISO8601(taxInfoEntityIn
            .getAttribute(BDMLifeEventDatastoreConstants.TAX_START_DATE));
          final String endDateStr = taxInfoEntityIn
            .getAttribute(BDMLifeEventDatastoreConstants.TAX_END_DATE);
          final Date toDate = endDateStr.isEmpty() ? Date.kZeroDate
            : Date.fromISO8601(endDateStr);
          if (checkOverlappingDate(fromDate, toDate, inputFromDate,
            inputToDate)
            || checkOverlappingDate(inputFromDate, inputToDate, fromDate,
              toDate)) {
            return AdaptorFactory.getBooleanAdaptor(false);
          }
        }
      }
    }
    return AdaptorFactory.getBooleanAdaptor(true);

  }

  private boolean checkOverlappingDate(final Date refFromDate,
    final Date refToDate, final Date fromDate, final Date toDate)
    throws AppException, InformationalException {

    boolean overlapInd = false;
    if (refToDate.isZero() && toDate.isZero()) {
      overlapInd = true;
    }
    // toDate is on or after refFromDate
    if (refToDate.isZero() && !toDate.isZero()
      && !toDate.before(refFromDate)) {
      overlapInd = true;
    }
    // fromDate is between refFromDate and refToDate
    if (!refToDate.isZero() && !toDate.isZero()
      && !fromDate.before(refFromDate) && !fromDate.after(refToDate)) {
      overlapInd = true;
    }
    // toDate is between refFromDate and refToDate
    if (!refToDate.isZero() && !toDate.isZero() && !toDate.before(refFromDate)
      && !toDate.after(refToDate)) {
      overlapInd = true;
    }
    return overlapInd;
  }

}
