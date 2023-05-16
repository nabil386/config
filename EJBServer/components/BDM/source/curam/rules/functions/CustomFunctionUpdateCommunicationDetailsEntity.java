package curam.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventDatastoreConstants;
import curam.ca.gc.bdm.codetable.BDMEMAILADDRESSCHANGETYPE;
import curam.ca.gc.bdm.codetable.BDMPHONENUMBERCHANGETYPE;
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
 * custom function to update Communication Details Entity
 *
 * @since
 * @author
 *
 */
public class CustomFunctionUpdateCommunicationDetailsEntity
  extends BDMFunctor {

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

      final String valueChangeType = ((StringAdaptor) getParameters().get(0))
        .getStringValue(rulesParameters);

      final IEG2Context ieg2context = (IEG2Context) rulesParameters;
      final IEG2ExecutionContext ieg2ExecutionContext =
        new IEG2ExecutionContext(rulesParameters);
      final Datastore datastore = ieg2ExecutionContext.getDatastore();

      // using IEG context fetch root entity ID
      final Entity rootEntity = readRoot(rulesParameters);
      // this is one person entity on life event
      final Entity personEntity = BDMApplicationEventsUtil
        .retrieveChildEntity(rootEntity, BDMDatastoreConstants.PERSON);

      String valueSelected = "";
      String idSelected = "";
      boolean addInd = false;
      boolean updateInd = false;
      boolean removeInd = false;
      if (valueChangeType.equals(BDMPHONENUMBERCHANGETYPE.ADD)
        || valueChangeType.equals(BDMPHONENUMBERCHANGETYPE.UPDATE)
        || valueChangeType.equals(BDMPHONENUMBERCHANGETYPE.REMOVE)) {
        valueSelected = BDMLifeEventDatastoreConstants.PHONE_NUMBER_SELECTED;
        idSelected = BDMLifeEventDatastoreConstants.SELECTED_PHONE_NUMBER_ID;
      }
      if (valueChangeType.equals(BDMEMAILADDRESSCHANGETYPE.ADD)
        || valueChangeType.equals(BDMEMAILADDRESSCHANGETYPE.UPDATE)
        || valueChangeType.equals(BDMEMAILADDRESSCHANGETYPE.REMOVE)) {
        valueSelected = BDMLifeEventDatastoreConstants.EMAIL_ADDRESS_SELECTED;
        idSelected = BDMLifeEventDatastoreConstants.SELECTED_EMAIL_ADDRESS_ID;
      }
      if (valueChangeType.equals(BDMPHONENUMBERCHANGETYPE.ADD)
        || valueChangeType.equals(BDMEMAILADDRESSCHANGETYPE.ADD)) {
        addInd = true;
      }
      if (valueChangeType.equals(BDMPHONENUMBERCHANGETYPE.UPDATE)
        || valueChangeType.equals(BDMEMAILADDRESSCHANGETYPE.UPDATE)) {
        updateInd = true;
      }
      if (valueChangeType.equals(BDMPHONENUMBERCHANGETYPE.REMOVE)
        || valueChangeType.equals(BDMEMAILADDRESSCHANGETYPE.REMOVE)) {
        removeInd = true;
      }
      // get the selected value list
      final java.util.List<java.lang.Long> selectedEntityIDList =
        ieg2context.getListQuestionSelectedEntityIDs(valueSelected,
          BDMLifeEventDatastoreConstants.COMMUNICATION_DETAILS);

      if (addInd) {
        if (!selectedEntityIDList.isEmpty()) {
          return AdaptorFactory.getBooleanAdaptor(Boolean.FALSE);
        }
        removeChildEntityUniqueID(personEntity);
        final Entity communicationDetailsEntity =
          datastore.newEntity(datastore
            .getEntityType(BDMDatastoreConstants.COMMUNICATION_DETAILS));
        personEntity.addChildEntity(communicationDetailsEntity);
        communicationDetailsEntity.setTypedAttribute(valueSelected, true);
        communicationDetailsEntity.setAttribute(
          BDMLifeEventDatastoreConstants.CURAM_DATASTORE_UNIQUE_ID,
          Long.toString(communicationDetailsEntity.getUniqueID()));
        communicationDetailsEntity.update();

        personEntity.setAttribute(idSelected,
          Long.toString(communicationDetailsEntity.getUniqueID()));
        personEntity.update();
      } else if (updateInd || removeInd) {
        if (selectedEntityIDList == null || selectedEntityIDList.isEmpty()
          || selectedEntityIDList.size() > 1) {
          return AdaptorFactory.getBooleanAdaptor(Boolean.FALSE);
        }
        setChildEntityUniqueID(personEntity, selectedEntityIDList,
          idSelected);
      }

    } catch (final Exception e) {

      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + " Error while updating communication details entity");

    }

    return AdaptorFactory.getBooleanAdaptor(Boolean.TRUE);
  }

  private void setChildEntityUniqueID(final Entity personEntity,
    final java.util.List<java.lang.Long> selectedEntityIDList,
    final String idSelected) {

    final Entity[] communicationDetailsEntities =
      BDMApplicationEventsUtil.getEntities(personEntity.getUniqueID(),
        BDMDatastoreConstants.COMMUNICATION_DETAILS);

    if (communicationDetailsEntities != null
      && selectedEntityIDList.size() > 0) {
      for (final Entity communicationDetailsEntityIn : communicationDetailsEntities) {
        if (communicationDetailsEntityIn.getUniqueID() == selectedEntityIDList
          .get(0)) {
          personEntity.setAttribute(idSelected,
            Long.toString(communicationDetailsEntityIn.getUniqueID()));
          communicationDetailsEntityIn.setAttribute(
            BDMLifeEventDatastoreConstants.CURAM_DATASTORE_UNIQUE_ID,
            Long.toString(communicationDetailsEntityIn.getUniqueID()));
          communicationDetailsEntityIn.update();
          personEntity.update();
        }
        if (communicationDetailsEntityIn.getUniqueID() != selectedEntityIDList
          .get(0)) {
          communicationDetailsEntityIn.removeAttribute(
            BDMLifeEventDatastoreConstants.CURAM_DATASTORE_UNIQUE_ID);
          communicationDetailsEntityIn.update();
        }
      }
    }
  }

  private void removeChildEntityUniqueID(final Entity personEntity) {

    final Entity[] communicationDetailsEntities =
      BDMApplicationEventsUtil.getEntities(personEntity.getUniqueID(),
        BDMDatastoreConstants.COMMUNICATION_DETAILS);

    if (communicationDetailsEntities != null) {
      for (final Entity communicationDetailsEntityIn : communicationDetailsEntities) {
        communicationDetailsEntityIn.removeAttribute(
          BDMLifeEventDatastoreConstants.CURAM_DATASTORE_UNIQUE_ID);
        communicationDetailsEntityIn.update();
      }
    }
  }

}
