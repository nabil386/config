package curam.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventDatastoreConstants;
import curam.codetable.METHODOFDELIVERY;
import curam.datastore.impl.Entity;
import curam.ieg.impl.IEG2Context;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.StringUtil;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;

/**
 * Contains a custom function that will be called to validate whether the
 * selected account is different for the bank
 * account currently linked to the program
 *
 *
 * @curam.exclude
 */
public class CustomFunctionvalidateDifferentBankAccount extends BDMFunctor {

  /**
   * A custom function that will be called to validate different branch
   * account.
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

    final IEG2Context ieg2context = (IEG2Context) rulesParameters;

    final Entity rootEntity = readRoot(rulesParameters);
    final Entity personEntity = BDMApplicationEventsUtil
      .retrieveChildEntity(rootEntity, BDMDatastoreConstants.PERSON);

    final Entity[] paymentBanksEntities =
      BDMApplicationEventsUtil.retrieveChildEntities(personEntity,
        BDMLifeEventDatastoreConstants.PAYMENT_BANKS);

    // get the selected phone number list
    final java.util.List<java.lang.Long> selectedEntityIDList =
      ieg2context.getListQuestionSelectedEntityIDs(
        BDMLifeEventDatastoreConstants.PAYMENT_PREFERRED_BANK_FOR_PROGRAM,
        BDMLifeEventDatastoreConstants.PAYMENT_BANKS);

    final Entity[] programEntities =
      BDMApplicationEventsUtil.retrieveChildEntities(personEntity,
        BDMLifeEventDatastoreConstants.PROGRAMS_ENTITY);
    if (programEntities != null && !selectedEntityIDList.isEmpty()) {
      for (final Entity programEntityIn : programEntities) {
        if (programEntityIn.getEntityType().getName()
          .equals(BDMLifeEventDatastoreConstants.PROGRAMS_ENTITY)
          && !StringUtil.isNullOrEmpty(programEntityIn.getAttribute(
            BDMLifeEventDatastoreConstants.PROGRAMS_ACTIVE_PROGRAM_SELECTED))) {
          final boolean activeProgramSelected =
            Boolean.parseBoolean(programEntityIn.getAttribute(
              BDMLifeEventDatastoreConstants.PROGRAMS_ACTIVE_PROGRAM_SELECTED));
          final String programPaymentMethod = programEntityIn.getAttribute(
            BDMLifeEventDatastoreConstants.PROGRAM_PAYMENT_METHOD);
          if (activeProgramSelected
            && programPaymentMethod.equals(METHODOFDELIVERY.EFT)) {
            // get the bank account info the program is currently using
            final String programBankAccountID = programEntityIn.getAttribute(
              BDMLifeEventDatastoreConstants.PROGRAM_PAYMENT_BANK_ACCOUNT_ID);
            final String programBankAccountNum = programEntityIn.getAttribute(
              BDMLifeEventDatastoreConstants.PROGRAM_PAYMENT_BANK_ACCOUNT_NUMBER);
            final String programBankSortCode = programEntityIn.getAttribute(
              BDMLifeEventDatastoreConstants.PROGRAM_PAYMENT_BANK_SORT_CODE);
            if (paymentBanksEntities != null) {
              for (final Entity paymentBankEntityIn : paymentBanksEntities) {
                if (paymentBankEntityIn.getEntityType().getName()
                  .equals(BDMLifeEventDatastoreConstants.PAYMENT_BANKS)
                  && paymentBankEntityIn.getUniqueID() == selectedEntityIDList
                    .get(0)) {
                  // get the bank account info that user selects
                  final String selectedBankAccountNum =
                    paymentBankEntityIn.getAttribute(
                      BDMLifeEventDatastoreConstants.BANK_ACCT_NUM);
                  final String selectedBankSortCode =
                    paymentBankEntityIn.getAttribute(
                      BDMLifeEventDatastoreConstants.BANK_SORT_CODE);
                  if (programBankSortCode.equals(selectedBankSortCode)
                    && programBankAccountNum.equals(selectedBankAccountNum)) {
                    return AdaptorFactory.getBooleanAdaptor(false);
                  }
                }
              }
            }
          }
        }
      }
    }
    return AdaptorFactory.getBooleanAdaptor(true);
  }
}
