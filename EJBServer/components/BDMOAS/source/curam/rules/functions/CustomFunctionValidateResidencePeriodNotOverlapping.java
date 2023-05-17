package curam.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdmoas.application.impl.BDMOASDatastoreConstants;
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
public class CustomFunctionValidateResidencePeriodNotOverlapping
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

    final Boolean isPrimaryParticipant =
      getOptionalBooleanParam(rulesParameters, 2);

    // using IEG context fetch root entity ID
    final Entity rootEntity = readRoot(rulesParameters);

    // this is one person entity on life event
    final Entity personEntity = getPersonEntity(rootEntity,
      BDMDatastoreConstants.PERSON, isPrimaryParticipant.booleanValue());

    if (personEntity != null) {
      final Entity[] residencePeriodEntities =
        personEntity.getChildEntities(rootEntity.getDatastore()
          .getEntityType(BDMOASDatastoreConstants.RESIDENCE_HISTORY));
      if (residencePeriodEntities != null) {
        for (final Entity residencePeriodEntity : residencePeriodEntities) {
          final Date fromDate =
            Date.fromISO8601(residencePeriodEntity.getAttribute(
              BDMOASDatastoreConstants.RESIDENCE_HISTORY_START_ATTR));
          final String endDateStr = residencePeriodEntity.getAttribute(
            BDMOASDatastoreConstants.RESIDENCE_HISTORY_END_ATTR);
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

  /**
   * This method assumes there is only child for the parent entity and returns
   * the first child found, otherwise it returns a null object
   *
   * @param parentEntity
   * @param entityType
   * @return
   */
  private static Entity getPersonEntity(final Entity rootEntity,
    final String entityType, final boolean isPrimaryParticipant) {

    final Entity[] personEntites = rootEntity.getChildEntities(
      rootEntity.getDatastore().getEntityType(BDMDatastoreConstants.PERSON));

    for (final Entity personEntity : personEntites) {
      if (personEntity
        .hasAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT)
        && personEntity
          .getTypedAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT)
          .equals(isPrimaryParticipant)) {
        return personEntity;
      }
    }
    return null;
  }

}
