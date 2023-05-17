package curam.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.codetable.BDMMARITALSTATUS;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdmoas.application.impl.BDMOASDatastoreConstants;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.ieg.impl.IEG2Context;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;

/**
 * Contains a custom function to set Spouse or CLP Declaration and Signature
 * indicator.
 *
 * @curam.exclude
 */
public class CustomFunctionSetSpouseOrCLPDeclarationSignInd
  extends BDMFunctor {

  /**
   * A custom function to set Spouse or CLP Declaration and Signature indicator.
   *
   * @param rulesParameters The rules parameters containing the object to
   * check.
   *
   * @return A rule adaptor indicating whether the object is valid or not.
   *
   * @throws AppException Exceptions that may occur when retrieving the
   * adaptor value.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    final IEG2Context ieg2context = (IEG2Context) rulesParameters;

    final long rootEntityID = ieg2context.getRootEntityID();

    final IEG2ExecutionContext ieg2ExecutionContext =
      new IEG2ExecutionContext(rulesParameters);

    final Datastore datastore = ieg2ExecutionContext.getDatastore();

    final Entity applicationEntity = datastore.readEntity(rootEntityID);

    final Entity[] personEntities = applicationEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.PERSON),
      "isPrimaryParticipant==true");

    String currentMaritalStatus = BDMConstants.EMPTY_STRING;
    String spouseOrCLPDetailsEnteredInd = BDMConstants.EMPTY_STRING;

    if (personEntities != null && personEntities.length > 0) {
      final Entity personEntity = personEntities[0];

      spouseOrCLPDetailsEnteredInd =
        personEntity.getAttribute(BDMOASDatastoreConstants.SPOUSE_OR_CLP_IND);

      final Entity[] maritalStatusInfoEntityList =
        personEntity.getChildEntities(datastore.getEntityType(
          BDMOASDatastoreConstants.MARITAL_STATUS_INFO_ENTITY));
      if (maritalStatusInfoEntityList != null
        && maritalStatusInfoEntityList.length > 0) {
        final Entity maritalStatusInfoEntity = maritalStatusInfoEntityList[0];
        currentMaritalStatus = maritalStatusInfoEntity.getAttribute(
          BDMOASDatastoreConstants.MARITAL_STATUS_INFO_CURNTSTATUS_ATTR);
      }

      if (!currentMaritalStatus.isEmpty()
        && !currentMaritalStatus.equals(BDMMARITALSTATUS.SINGLE)
        || !spouseOrCLPDetailsEnteredInd.isEmpty()
          && spouseOrCLPDetailsEnteredInd
            .equals(BDMDatastoreConstants.TRUE)) {
        personEntity.setAttribute(
          BDMOASDatastoreConstants.SPOUSE_OR_CLP_DECL_SIGN_IND,
          BDMDatastoreConstants.TRUE);
        personEntity.update();
      } else {
        if (personEntity
          .getAttribute(BDMOASDatastoreConstants.SPOUSE_OR_CLP_DECL_SIGN_IND)
          .equals(BDMDatastoreConstants.TRUE)) {
          personEntity.setAttribute(
            BDMOASDatastoreConstants.SPOUSE_OR_CLP_DECL_SIGN_IND,
            BDMOASDatastoreConstants.FALSE);
          personEntity.update();
        }
      }
    }

    return AdaptorFactory.getBooleanAdaptor(true);
  }

}
