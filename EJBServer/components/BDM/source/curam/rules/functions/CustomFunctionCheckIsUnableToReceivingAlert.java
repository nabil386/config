package curam.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventDatastoreConstants;
import curam.ca.gc.bdm.codetable.BDMEMAILADDRESSCHANGETYPE;
import curam.ca.gc.bdm.codetable.BDMPHONENUMBERCHANGETYPE;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.codetable.IEGYESNO;
import curam.datastore.impl.Entity;
import curam.ieg.impl.IEG2Context;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Trace;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;

/**
 * custom function to check client is unable to receive alert if the phone
 * number is removed
 *
 * @since
 * @author
 *
 */
public class CustomFunctionCheckIsUnableToReceivingAlert extends BDMFunctor {

  /**
   * custom function to update Communication Details Entity
   *
   * @param rulesParameters The rules parameters containing the object to
   * check.
   */
  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    try {
      final IEG2Context ieg2context = (IEG2Context) rulesParameters;
      // Get All parameters
      int index = 0;
      final String actionString =
        getOptionalStringParam(rulesParameters, index++);
      final String valueChangeType =
        getOptionalStringParam(rulesParameters, index++);
      // using IEG context fetch root entity ID
      final Entity rootEntity = readRoot(rulesParameters);
      // this is one person entity on life event
      final Entity personEntity = BDMApplicationEventsUtil
        .retrieveChildEntity(rootEntity, BDMDatastoreConstants.PERSON);

      if (actionString.equals("VALIDATION")) {
        if (valueChangeType.equals(BDMPHONENUMBERCHANGETYPE.REMOVE)) {
          // get the selected value list
          final java.util.List<java.lang.Long> selectedEntityIDList =
            ieg2context.getListQuestionSelectedEntityIDs(
              BDMLifeEventDatastoreConstants.PHONE_NUMBER_SELECTED,
              BDMLifeEventDatastoreConstants.COMMUNICATION_DETAILS);
          if (selectedEntityIDList == null || selectedEntityIDList.isEmpty()
            || selectedEntityIDList.size() > 1) {
            throw new Exception();
          } else {
            final long selectedItemID = selectedEntityIDList.get(0);

            final String isReceivingEmailAlert = personEntity.getAttribute(
              BDMLifeEventDatastoreConstants.IS_RECEIVING_EMAIL_ALERT);

            final String isProceedAllowed = personEntity.getAttribute(
              BDMLifeEventDatastoreConstants.IS_PROCEED_ALLOWED);
            if (isItemReceivingAlertNow(personEntity, selectedItemID,
              BDMLifeEventDatastoreConstants.RECEIVE_ALERT)
              && "false".equals(isReceivingEmailAlert)) {
              if (isProceedAllowed.equals("false")) {
                // set isProceedAllowed to true
                personEntity.setAttribute(
                  BDMLifeEventDatastoreConstants.IS_PROCEED_ALLOWED, "true");
                personEntity.setAttribute(
                  BDMLifeEventDatastoreConstants.IS_SHOWING_WARNING, "true");
                personEntity.update();
                // show validation message
                return AdaptorFactory.getBooleanAdaptor(Boolean.FALSE);
              } else {
                return AdaptorFactory.getBooleanAdaptor(Boolean.TRUE);
              }
            } else {
              personEntity.setAttribute(
                BDMLifeEventDatastoreConstants.IS_SHOWING_WARNING, "false");
              personEntity.update();
              return AdaptorFactory.getBooleanAdaptor(Boolean.TRUE);
            }
          }
        }
        if (valueChangeType.equals(BDMEMAILADDRESSCHANGETYPE.REMOVE)) {
          // get the selected value list
          final java.util.List<java.lang.Long> selectedEntityIDList =
            ieg2context.getListQuestionSelectedEntityIDs(
              BDMLifeEventDatastoreConstants.EMAIL_ADDRESS_SELECTED,
              BDMLifeEventDatastoreConstants.COMMUNICATION_DETAILS);
          if (selectedEntityIDList == null || selectedEntityIDList.isEmpty()
            || selectedEntityIDList.size() > 1) {
            throw new Exception();
          } else {
            final Long selectedItemID = selectedEntityIDList.get(0);
            final String isReceivingTextAlert = personEntity.getAttribute(
              BDMLifeEventDatastoreConstants.IS_RECEIVING_TEXT_ALERT);

            final String isProceedAllowed = personEntity.getAttribute(
              BDMLifeEventDatastoreConstants.IS_PROCEED_ALLOWED);
            if (isItemReceivingAlertNow(personEntity, selectedItemID,
              BDMLifeEventDatastoreConstants.ALT_PREF_EMAIL)
              && "false".equals(isReceivingTextAlert)) {
              if (isProceedAllowed.equals("false")) {
                // set isProceedAllowed to true
                personEntity.setAttribute(
                  BDMLifeEventDatastoreConstants.IS_PROCEED_ALLOWED, "true");
                personEntity.setAttribute(
                  BDMLifeEventDatastoreConstants.IS_SHOWING_WARNING, "true");
                personEntity.update();
                // show validation message
                return AdaptorFactory.getBooleanAdaptor(Boolean.FALSE);
              } else {
                return AdaptorFactory.getBooleanAdaptor(Boolean.TRUE);
              }
            } else {
              personEntity.setAttribute(
                BDMLifeEventDatastoreConstants.IS_SHOWING_WARNING, "false");
              personEntity.update();
              return AdaptorFactory.getBooleanAdaptor(Boolean.TRUE);
            }
          }
        }
      }
      if (actionString.equals("RESET_VALUE")) {
        // set isProceedAllowed to default value false
        personEntity.setAttribute(
          BDMLifeEventDatastoreConstants.IS_PROCEED_ALLOWED, "false");
        personEntity.update();
      }
    } catch (

    final Exception e) {

      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + " Error while checking whether client is able to receive alert going forward");

    }

    return AdaptorFactory.getBooleanAdaptor(Boolean.TRUE);
  }

  /**
   * Check whether the selected item is currently receiving alert
   *
   * @param personEntity
   * @param selectedItemID
   * @param attributeName
   */
  private boolean isItemReceivingAlertNow(final Entity personEntity,
    final long selectedItemID, final String attributeName) {

    final Entity[] communicationDetailsEntities =
      BDMApplicationEventsUtil.getEntities(personEntity.getUniqueID(),
        BDMDatastoreConstants.COMMUNICATION_DETAILS);

    if (communicationDetailsEntities != null) {
      for (final Entity communicationDetailsEntityIn : communicationDetailsEntities) {
        final long dataStoreUniqueID =
          communicationDetailsEntityIn.getUniqueID();
        final String receiveAlerts =
          communicationDetailsEntityIn.getAttribute(attributeName);
        if (selectedItemID == dataStoreUniqueID
          && receiveAlerts.equals(IEGYESNO.YES)) {
          return true;
        }
      }
    }
    return false;
  }

}
