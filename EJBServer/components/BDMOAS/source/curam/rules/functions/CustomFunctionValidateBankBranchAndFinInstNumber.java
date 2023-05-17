package curam.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventDatastoreConstants;
import curam.ca.gc.bdm.codetable.BDMBANKACCOUNTCHOICE;
import curam.codetable.METHODOFDELIVERY;
import curam.core.fact.BankBranchFactory;
import curam.core.impl.EnvVars;
import curam.core.intf.BankBranch;
import curam.core.struct.BankBranchDtlsList;
import curam.core.struct.SortCodeStruct;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.ieg.impl.IEG2Context;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.rules.functor.CustomFunctor;

/**
 * Contains a custom function that will be called to validate "Financial
 * Institution number and Branch Transit mismatch"
 *
 * @curam.exclude
 */
public class CustomFunctionValidateBankBranchAndFinInstNumber
  extends CustomFunctor {

  /**
   * A custom function that will be called to validate "Financial
   * Institution number and Branch Transit mismatch"
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

    if (!Configuration
      .getBooleanProperty(EnvVars.BDM_ENV_BDM_BANKBRANCH_VALIDATION)) {
      return AdaptorFactory.getBooleanAdaptor(true);
    }

    final IEG2Context ieg2context = (IEG2Context) rulesParameters;
    final long rootEntityID = ieg2context.getRootEntityID();

    final IEG2ExecutionContext ieg2ExecutionContext =
      new IEG2ExecutionContext(rulesParameters);

    final Datastore datastore = ieg2ExecutionContext.getDatastore();

    final Entity applicationEntity = datastore.readEntity(rootEntityID);

    final Entity personEntity = applicationEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.PERSON),
      "isPrimaryParticipant==true")[0];

    final String preferredPaymentMethod =
      personEntity.getAttribute(BDMDatastoreConstants.PAYMENT_METHOD);

    boolean isValid = true;

    if (!StringUtil.isNullOrEmpty(preferredPaymentMethod)) {
      // Get payment entity which contains the Bank information
      final Entity paymentEntity = BDMApplicationEventsUtil
        .retrieveChildEntity(personEntity, BDMDatastoreConstants.PAYMENT);

      if (preferredPaymentMethod.equals(METHODOFDELIVERY.EFT)) {
        // Get new payment entity for new bank from datastore
        final String preferredBankAccount = paymentEntity.getAttribute(
          BDMLifeEventDatastoreConstants.PREFERRED_BANK_ACCOUNT);

        if (StringUtil.isNullOrEmpty(preferredBankAccount)
          || preferredBankAccount.equals(BDMBANKACCOUNTCHOICE.ADD_NEW)) {

          final String datastoreBankAccountNumber =
            paymentEntity.getAttribute(BDMDatastoreConstants.BANK_ACCT_NUM);

          if (!StringUtil.isNullOrEmpty(datastoreBankAccountNumber)) {

            final String bankInstNum = paymentEntity
              .getAttribute(BDMDatastoreConstants.BANK_INSTITUTION_CODE);

            final String bankBranchNum = paymentEntity
              .getAttribute(BDMDatastoreConstants.BANK_BRANCH_NUM);

            final String bankSortCode = bankInstNum + bankBranchNum;

            if (!(bankInstNum.length() == 3 && bankBranchNum.length() == 5)) {
              isValid = false;
            } else {
              // search BankBranch table using sortCode
              final BankBranch bankBranch = BankBranchFactory.newInstance();
              final SortCodeStruct key = new SortCodeStruct();
              key.bankSortCode = bankSortCode;
              final BankBranchDtlsList branchList =
                bankBranch.searchBySortCode(key);
              if (branchList.dtls.isEmpty()) {
                isValid = false;
              }
            }
          }
        }
      }
    }

    return AdaptorFactory.getBooleanAdaptor(isValid);
  }
}
