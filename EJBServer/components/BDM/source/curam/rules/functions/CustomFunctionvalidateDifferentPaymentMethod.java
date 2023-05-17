package curam.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventDatastoreConstants;
import curam.codetable.METHODOFDELIVERY;
import curam.datastore.impl.Entity;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.StringUtil;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import java.util.List;

/**
 * Contains a custom function that will be called to validate different payment
 * method
 *
 *
 * @curam.exclude
 */
public class CustomFunctionvalidateDifferentPaymentMethod extends BDMFunctor {

  /**
   * A custom function that will be called to validate validate different
   * payment method
   *
   * @param rulesParameters The rules parameters containing the object to
   * check. The first parameter in the list of parameters is checked.
   *
   * @return A rule adaptor indicating whether the object is null or not.
   *
   * @throws AppException Exceptions that may occur when retrieving the
   * adaptor value.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    final Entity rootEntity = readRoot(rulesParameters);
    final Entity personEntity = BDMApplicationEventsUtil
      .retrieveChildEntity(rootEntity, BDMDatastoreConstants.PERSON);

    // get the payment method that user selects
    final List<Adaptor> parameters = getParameters();
    final String userSelectedPmtMethod =
      ((StringAdaptor) parameters.get(0)).getStringValue(rulesParameters);
    final Entity[] programEntities =
      BDMApplicationEventsUtil.getEntities(personEntity.getUniqueID(),
        BDMLifeEventDatastoreConstants.PROGRAMS_ENTITY);
    if (programEntities != null) {
      for (final Entity programEntityIn : programEntities) {
        if (programEntityIn.getEntityType().getName()
          .equals(BDMLifeEventDatastoreConstants.PROGRAMS_ENTITY)
          && !StringUtil.isNullOrEmpty(programEntityIn.getAttribute(
            BDMLifeEventDatastoreConstants.PROGRAMS_ACTIVE_PROGRAM_SELECTED))) {
          final boolean activeProgramSelected =
            Boolean.parseBoolean(programEntityIn.getAttribute(
              BDMLifeEventDatastoreConstants.PROGRAMS_ACTIVE_PROGRAM_SELECTED));
          if (activeProgramSelected) {
            // get the payment method the program is currently using
            final String programPaymentMethod = programEntityIn.getAttribute(
              BDMLifeEventDatastoreConstants.PROGRAM_PAYMENT_METHOD);
            if (programPaymentMethod.equals(METHODOFDELIVERY.CHEQUE)
              && userSelectedPmtMethod.equals(METHODOFDELIVERY.CHEQUE)) {
              return AdaptorFactory.getBooleanAdaptor(false);
            }
          }
        }
      }
    }
    return AdaptorFactory.getBooleanAdaptor(true);
  }
}
