package curam.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdmoas.application.impl.BDMOASDatastoreConstants;
import curam.ca.gc.bdmoas.impl.BDMOASConstants;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.ieg.impl.IEG2Context;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Trace;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;

/**
 * BDMOAS FEATURE 52093: Class Added
 * Custom function to pre-populate the application details.
 *
 * @author SMisal
 *
 */
public class CustomFunctionPrepopulateWitnessAddressDetails
  extends BDMFunctor {

  public CustomFunctionPrepopulateWitnessAddressDetails() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Custom function to pre populate the application details.
   *
   * @param rulesParameters the rules parameters
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

      final Entity declarationEntity =
        applicationEntity.getChildEntities(datastore
          .getEntityType(BDMOASDatastoreConstants.DECLARATION_ENTITY))[0];

      // set Address Entity Attribute
      addWitnessAddress(applicationEntity, datastore, declarationEntity);

    } catch (final Exception e) {

      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + BDMOASConstants.ERROR_PREPOPULATING_WITNESS_ADDRESS
        + e.getMessage());
    }

    return AdaptorFactory.getBooleanAdaptor(Boolean.TRUE);
  }

  /**
   *
   * @param applicationEntity
   * @param datastore
   * @param personEntity
   * @param addressKey
   * @throws AppException
   * @throws InformationalException
   */
  private void addWitnessAddress(final Entity applicationEntity,
    final Datastore datastore, final Entity declarationEntity)
    throws AppException, InformationalException {

    final Entity[] mailingAddressEntityList =
      declarationEntity.getChildEntities(
        datastore.getEntityType(BDMDatastoreConstants.MAILING_ADDRESS));

    if (mailingAddressEntityList != null
      && mailingAddressEntityList.length == 0) {
      final Entity mailingAddress = datastore.newEntity(
        datastore.getEntityType(BDMDatastoreConstants.MAILING_ADDRESS));
      declarationEntity.addChildEntity(mailingAddress);
      declarationEntity.update();
    }
  }

}
