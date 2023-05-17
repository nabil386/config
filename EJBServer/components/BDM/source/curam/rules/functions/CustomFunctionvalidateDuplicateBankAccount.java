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
import java.util.List;

/**
 * Contains a custom function that will be called to validate duplicate bank
 * branch
 *
 *
 * @curam.exclude
 */
public class CustomFunctionvalidateDuplicateBankAccount extends BDMFunctor {

  /**
   * A custom function that will be called to validate validate duplicate bank
   * branch
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

    final List<Adaptor> parameters = getParameters();
    final String bankInstNum =
      ((StringAdaptor) parameters.get(0)).getStringValue(rulesParameters);

    final String bankBranchNum =
      ((StringAdaptor) parameters.get(1)).getStringValue(rulesParameters);

    final String bankAccountNum =
      ((StringAdaptor) parameters.get(2)).getStringValue(rulesParameters);

    final Entity rootEntity = readRoot(rulesParameters);
    final Entity personEntity = BDMApplicationEventsUtil
      .retrieveChildEntity(rootEntity, BDMDatastoreConstants.PERSON);

    final Entity[] paymentBanksEntities =
      BDMApplicationEventsUtil.retrieveChildEntities(personEntity,
        BDMLifeEventDatastoreConstants.PAYMENT_BANKS);

    if (paymentBanksEntities != null) {
      for (final Entity paymentBanksEntityIn : paymentBanksEntities) {
        if (paymentBanksEntityIn.getEntityType().getName()
          .equals(BDMLifeEventDatastoreConstants.PAYMENT_BANKS)) {
          final String existingInstNum = paymentBanksEntityIn.getAttribute(
            BDMLifeEventDatastoreConstants.BANK_INSTITUTION_CODE);
          final String existingBankBranchNum = paymentBanksEntityIn
            .getAttribute(BDMLifeEventDatastoreConstants.BANK_BRANCH_NUM);
          final String existingBankAccountNum = paymentBanksEntityIn
            .getAttribute(BDMLifeEventDatastoreConstants.BANK_ACCT_NUM);
          if (existingInstNum.equals(bankInstNum)
            && existingBankBranchNum.equals(bankBranchNum)
            && existingBankAccountNum.equals(bankAccountNum)) {
            return AdaptorFactory.getBooleanAdaptor(false);
          }
        }
      }
    }
    return AdaptorFactory.getBooleanAdaptor(true);
  }
}
