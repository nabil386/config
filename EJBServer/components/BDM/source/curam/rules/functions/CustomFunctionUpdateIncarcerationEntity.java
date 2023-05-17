package curam.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventDatastoreConstants;
import curam.ca.gc.bdm.codetable.BDMINCARCERATIONCHANGETYPE;
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
 * custom function to update Incarceration Entity
 * Store Incarceration entity unique ID to its own attribute
 * curamDataStoreUniqueID, as well to Person entity attribute BDMIncarChangeType
 * This is the strategy used to identify which incarceration period is selected.
 *
 * @since
 * @author
 *
 */
public class CustomFunctionUpdateIncarcerationEntity extends BDMFunctor {

  /**
   * custom function to update Incarceration Entity
   *
   * @param rulesParameters The rules parameters containing the object to
   * check.
   */
  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    try {

      final String incarcerationChangeType =
        ((StringAdaptor) getParameters().get(0))
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

      // get the selected Incarceration list
      final java.util.List<java.lang.Long> selectedEntityIDList =
        ieg2context.getListQuestionSelectedEntityIDs(
          BDMLifeEventDatastoreConstants.INCARCERATION_SELECTED,
          BDMLifeEventDatastoreConstants.INCARCERATION);

      if (incarcerationChangeType.equals(BDMINCARCERATIONCHANGETYPE.ADD)) {
        if (selectedEntityIDList != null && !selectedEntityIDList.isEmpty()) {
          return AdaptorFactory.getBooleanAdaptor(Boolean.FALSE);
        }
        removeChildEntityUniqueID(personEntity);
        final Entity incarcerationEntity = datastore.newEntity(datastore
          .getEntityType(BDMLifeEventDatastoreConstants.INCARCERATION));
        personEntity.addChildEntity(incarcerationEntity);
        incarcerationEntity.setTypedAttribute(
          BDMLifeEventDatastoreConstants.INCARCERATION_SELECTED, true);
        incarcerationEntity.setAttribute(
          BDMLifeEventDatastoreConstants.CURAM_DATASTORE_UNIQUE_ID,
          Long.toString(incarcerationEntity.getUniqueID()));
        incarcerationEntity.update();

        personEntity.setAttribute(
          BDMLifeEventDatastoreConstants.SELECTED_INCARCERATION_ID,
          Long.toString(incarcerationEntity.getUniqueID()));
        personEntity.update();
      } else if (incarcerationChangeType
        .equals(BDMINCARCERATIONCHANGETYPE.UPDATE)
        || incarcerationChangeType
          .equals(BDMINCARCERATIONCHANGETYPE.REMOVE)) {
        if (selectedEntityIDList == null || selectedEntityIDList.isEmpty()
          || selectedEntityIDList.size() > 1) {
          return AdaptorFactory.getBooleanAdaptor(Boolean.FALSE);
        }
        setChildEntityUniqueID(personEntity, selectedEntityIDList);
      }

    } catch (final Exception e) {

      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + " Error while updating Incarceration details entity");

    }

    return AdaptorFactory.getBooleanAdaptor(Boolean.TRUE);
  }

  private void setChildEntityUniqueID(final Entity personEntity,
    final java.util.List<java.lang.Long> selectedEntityIDList) {

    final Entity[] incarcerationEntities =
      BDMApplicationEventsUtil.getEntities(personEntity.getUniqueID(),
        BDMLifeEventDatastoreConstants.INCARCERATION);

    if (incarcerationEntities != null && selectedEntityIDList.size() > 0) {
      for (final Entity incarcerationEntityIn : incarcerationEntities) {
        if (incarcerationEntityIn.getUniqueID() == selectedEntityIDList
          .get(0)) {
          personEntity.setAttribute(
            BDMLifeEventDatastoreConstants.SELECTED_INCARCERATION_ID,
            Long.toString(incarcerationEntityIn.getUniqueID()));
          incarcerationEntityIn.setAttribute(
            BDMLifeEventDatastoreConstants.CURAM_DATASTORE_UNIQUE_ID,
            Long.toString(incarcerationEntityIn.getUniqueID()));
          incarcerationEntityIn.update();
          personEntity.update();
        }
        if (incarcerationEntityIn.getUniqueID() != selectedEntityIDList
          .get(0)) {
          incarcerationEntityIn.removeAttribute(
            BDMLifeEventDatastoreConstants.CURAM_DATASTORE_UNIQUE_ID);
          incarcerationEntityIn.update();
        }
      }
    }
  }

  private void removeChildEntityUniqueID(final Entity personEntity) {

    final Entity[] incarcerationEntities =
      BDMApplicationEventsUtil.getEntities(personEntity.getUniqueID(),
        BDMLifeEventDatastoreConstants.INCARCERATION);

    if (incarcerationEntities != null) {
      for (final Entity incarcerationEntityIn : incarcerationEntities) {
        incarcerationEntityIn.removeAttribute(
          BDMLifeEventDatastoreConstants.CURAM_DATASTORE_UNIQUE_ID);
        incarcerationEntityIn.update();
      }
    }
  }

}
