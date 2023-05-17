package curam.ca.gc.bdm.evidence.impl;

import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.fact.BDMPaymentInstrumentDAFactory;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMCountIssuedPaymentsForBankAccountKey;
import curam.ca.gc.bdm.entity.financial.fact.BDMPaymentDestinationFactory;
import curam.ca.gc.bdm.entity.financial.intf.BDMPaymentDestination;
import curam.ca.gc.bdm.entity.financial.struct.BDMConcernRoleDestinationKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentDestinationDtlsList;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.message.BDMEVIDENCE;
import curam.codetable.DESTINATIONTYPECODE;
import curam.codetable.PMTRECONCILIATIONSTATUS;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.ConcernRoleBankAccountFactory;
import curam.core.intf.ConcernRoleBankAccount;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.impl.ValidationManagerFactory;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.RecordCount;
import curam.core.struct.BankAccountKey;
import curam.core.struct.BankAccountRMDtls;
import curam.core.struct.BankAccountRMDtlsList;
import curam.core.struct.CaseKey;
import curam.core.struct.ConcernRoleBankAccountDtls;
import curam.core.struct.ConcernRoleBankAccountKey;
import curam.core.struct.ConcernRoleIDStatusCodeKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.pdc.impl.PDCConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement.InformationalType;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;

/**
 * TASK 12976
 *
 * PDC Bank account evidence validation added for BDA
 */
public class BDMBankAccountValidation {

  /**
   * Validate on delete.
   * <p/>
   * To prevent removal of PDC Bank Account if any of the following are met:
   * <ul>
   * <li>The PDC Bank Account being removed has already been used for payment. .
   * If this check fails then the following informational is thrown:
   * {@link BDMEVIDENCE #ERR_BANK_ACCOUNT_CANNOT_DELETE_USED_FOR_PAYMENT}.
   * </li>
   * </ul>
   *
   * @param caseKey {@link CaseKey}
   * @param evidenceKey {@link EIEvidenceKey}
   * @return Boolean result.
   *
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  public void validateRemovePDCBankAccountEvidence(final CaseKey caseKey,
    final EIEvidenceKey evidenceKey)
    throws AppException, InformationalException {

    if (evidenceKey.evidenceType.equals(PDCConst.PDCBANKACCOUNT)) {
      if (isDeleteToBankAccountAlreadyUsedForPayments(evidenceKey)) {

        throwErrorDeleteBankAccountUsedForPayments();
      }

    }
  }

  /**
   * Returns true if the evidence being removed is PDCBankAccount and it has
   * already been used for payment, false otherwise.
   *
   * @param evidenceKey {@link EIEvidenceKey}
   * @return Boolean result.
   *
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  public boolean isDeleteToBankAccountAlreadyUsedForPayments(
    final EIEvidenceKey evidenceKey)
    throws AppException, InformationalException {

    final BankAccountKey bankAccountKey = getBankAccountKey(evidenceKey);

    if (bankAccountKey != null) {

      // check for issued payments
      final BDMCountIssuedPaymentsForBankAccountKey bankAccountStatusCodeKey =
        new BDMCountIssuedPaymentsForBankAccountKey();
      bankAccountStatusCodeKey.bankAccountID = bankAccountKey.bankAccountID;
      bankAccountStatusCodeKey.reconcilStatusCode =
        PMTRECONCILIATIONSTATUS.CANCELLED;

      final RecordCount issuedPaymentsCount =
        BDMPaymentInstrumentDAFactory.newInstance()
          .countIssuedPaymentsForBankAccount(bankAccountStatusCodeKey);

      return issuedPaymentsCount.count > 0 ? true : false;
    }

    return false;
  }

  // Check if the account is not linked by seeing if there is data for it in the
  // table
  /**
   * Returns true if there is a bank link, false otherwise.
   *
   * @param evidenceKey {@link EIEvidenceKey}
   * @return Boolean result.
   *
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  public boolean isThereABankLink(final EIEvidenceKey evidenceKey)
    throws AppException, InformationalException {

    final BDMPaymentDestinationDtlsList bdmPaymentDestinationDtlsList =
      getPaymentDestinations(evidenceKey);

    if (!bdmPaymentDestinationDtlsList.dtls.isEmpty()) {
      return true;
    }
    return false;
  }

  /**
   * Returns the list of payment destinations for concern role and concern role
   * bank account.
   *
   * @param evidenceKey {@link EIEvidenceKey}
   * @return the list of payment destinations
   *
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  public BDMPaymentDestinationDtlsList
    getPaymentDestinations(final EIEvidenceKey evidenceKey)
      throws AppException, InformationalException {

    final BDMPaymentDestination bdmPaymentDestinationObj =
      BDMPaymentDestinationFactory.newInstance();

    final ConcernRoleBankAccountKey concernRoleBankAccountKey =
      getConcernRoleBankAccountKey(evidenceKey);

    // get existing bank account details
    final ConcernRoleBankAccount concernRoleBankAccountObj =
      ConcernRoleBankAccountFactory.newInstance();

    final ConcernRoleBankAccountDtls concernRoleBankAccountDtls =
      concernRoleBankAccountObj.read(concernRoleBankAccountKey);

    final BDMConcernRoleDestinationKey concernRoleDestinationKey =
      new BDMConcernRoleDestinationKey();

    concernRoleDestinationKey.concernRoleID =
      concernRoleBankAccountDtls.concernRoleID;
    concernRoleDestinationKey.destinationID =
      concernRoleBankAccountDtls.concernRoleBankAccountID;
    concernRoleDestinationKey.destinationType =
      DESTINATIONTYPECODE.BANKACCOUNT;
    concernRoleDestinationKey.recordStatusCode = RECORDSTATUS.NORMAL;

    final BDMPaymentDestinationDtlsList bdmPaymentDestinationDtlsList =
      bdmPaymentDestinationObj
        .searchActiveByConcernRoleDestination(concernRoleDestinationKey);
    return bdmPaymentDestinationDtlsList;
  }

  /**
   * Returns the Concern's {@link BankAccountKey} if
   * matching sort code and account number with the PDC Bank Account read by the
   * EIEvidenceKey. This method will return null on every other path
   * including the EIEvidenceKey not representing a PDC Bank Account or no Bank
   * Account record matching on sort code and account number.
   *
   * @param evidenceKey {@link EIEvidenceKey}
   * @return BankAccountKey
   *
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  private BankAccountKey getBankAccountKey(final EIEvidenceKey evidenceKey)
    throws AppException, InformationalException {

    if (evidenceKey.evidenceType.equals(PDCConst.PDCBANKACCOUNT)) {

      final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
        readDynamicEvidence(evidenceKey);

      BDMEvidenceUtil
        .updateDynamicEvidenceDataDetails(dynamicEvidenceDataDetails);

      final String accountNumber =
        getAttribute(dynamicEvidenceDataDetails, "accountNumber");
      final String sortCode =
        getAttribute(dynamicEvidenceDataDetails, "sortCode");

      final BankAccountRMDtlsList bankAccountRMDtlsList =
        readAllConcernRoleBankAccountDtls(evidenceKey);

      for (final BankAccountRMDtls dtls : bankAccountRMDtlsList.dtls) {

        if (dtls.accountNumber.equals(accountNumber)
          && dtls.bankSortCode.equals(sortCode)) {

          final BankAccountKey bankAccountKey = new BankAccountKey();
          bankAccountKey.bankAccountID = dtls.bankAccountID;

          return bankAccountKey;
        }
      }
    }
    return null;
  }

  public ConcernRoleBankAccountKey
    getConcernRoleBankAccountKey(final EIEvidenceKey evidenceKey)
      throws AppException, InformationalException {

    if (evidenceKey.evidenceType.equals(PDCConst.PDCBANKACCOUNT)) {

      final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
        readDynamicEvidence(evidenceKey);

      BDMEvidenceUtil
        .updateDynamicEvidenceDataDetails(dynamicEvidenceDataDetails);

      final String accountNumber =
        getAttribute(dynamicEvidenceDataDetails, "accountNumber");
      final String sortCode =
        getAttribute(dynamicEvidenceDataDetails, "sortCode");

      final BankAccountRMDtlsList bankAccountRMDtlsList =
        readAllConcernRoleBankAccountDtls(evidenceKey);

      for (final BankAccountRMDtls dtls : bankAccountRMDtlsList.dtls) {

        if (dtls.accountNumber.equals(accountNumber)
          && dtls.bankSortCode.equals(sortCode)) {

          final ConcernRoleBankAccountKey concernRoleBankAccountKey =
            new ConcernRoleBankAccountKey();
          concernRoleBankAccountKey.concernRoleBankAccountID =
            dtls.concernRoleBankAccountID;

          return concernRoleBankAccountKey;
        }
      }
    }
    return new ConcernRoleBankAccountKey();
  }

  /**
   * Utility to retrieve a String attribute from dynamic evidence.
   *
   * @param details {@link DynamicEvidenceDataDetails}
   * @param key String key identifying attribute to be retrieved.
   * @return String attribute value.
   */
  private String getAttribute(final DynamicEvidenceDataDetails details,
    final String key) {

    return details.getAttribute(key).getValue();
  }

  /**
   * Return {@link BankAccountRMDtlsList} listing the concern's bank account
   * details (concern is from concernroleid on the evidence
   * descriptor for the evidence key passed).
   *
   * @param evidenceKey {@link EIEvidenceKey}
   * @return concern's bank account details list
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  private BankAccountRMDtlsList
    readAllConcernRoleBankAccountDtls(final EIEvidenceKey evidenceKey)
      throws AppException, InformationalException {

    final EvidenceDescriptorDtls evidenceDescriptorDtls =
      getEvidenceDescriptorDtls(evidenceKey);

    final ConcernRoleIDStatusCodeKey concernRoleIDStatusCodeKey =
      new ConcernRoleIDStatusCodeKey();
    concernRoleIDStatusCodeKey.concernRoleID =
      evidenceDescriptorDtls.participantID;
    concernRoleIDStatusCodeKey.statusCode = RECORDSTATUS.NORMAL;

    return ConcernRoleBankAccountFactory.newInstance()
      .searchByConcernRoleIDAndStatus(concernRoleIDStatusCodeKey);
  }

  /**
   * Utility to return the {@link DynamicEvidenceDataDetails} for the evidence
   * key passed.
   *
   * @param evidenceKey {@link EIEvidenceKey}
   * @return {@link DynamicEvidenceDataDetails}
   *
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  public DynamicEvidenceDataDetails
    readDynamicEvidence(final EIEvidenceKey evidenceKey)
      throws AppException, InformationalException {

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final EIEvidenceReadDtls eiEvidenceReadDtls =
      evidenceControllerObj.readEvidence(evidenceKey);
    return (DynamicEvidenceDataDetails) eiEvidenceReadDtls.evidenceObject;

  }

  /**
   * Returns the {@link EvidenceDescriptorDtls} for the evidence key passed.
   *
   * @param evidenceKey {@link EIEvidenceKey}
   * @return {@link EvidenceDescriptorDtls}
   *
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  public EvidenceDescriptorDtls
    getEvidenceDescriptorDtls(final EIEvidenceKey evidenceKey)
      throws AppException, InformationalException {

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final RelatedIDAndEvidenceTypeKey key = new RelatedIDAndEvidenceTypeKey();
    key.evidenceType = evidenceKey.evidenceType;
    key.relatedID = evidenceKey.evidenceID;
    return evidenceControllerObj
      .readEvidenceDescriptorByRelatedIDAndType(key);
  }

  /**
   * Throws informational:
   * {@link BDMEVIDENCE #ERR_BANK_ACCOUNT_CANNOT_DELETE_USED_FOR_PAYMENT}
   * i.e. creates the {@link AppException} for this message and calls
   * {@link InformationalManager #failOperation()}.
   *
   * @throws InformationalException Generic Exception Signature
   */
  private void throwErrorDeleteBankAccountUsedForPayments()
    throws InformationalException {

    ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
      new AppException(
        BDMEVIDENCE.ERR_BANK_ACCOUNT_CANNOT_DELETE_USED_FOR_PAYMENT),
      null, InformationalType.kError,
      curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine, 0);

    TransactionInfo.getInformationalManager().failOperation();
  }

  /**
   * Throws informational:
   * {@link BDMEVIDENCE #ERR_BANK_ACCOUNT_CANNOT_MODIFY_LINK_EXISTS}
   * i.e. creates the {@link AppException} for this message and calls
   * {@link InformationalManager #failOperation()}.
   *
   * @throws InformationalException Generic Exception Signature
   */
  private void throwErrorBankProgramLinkExists()
    throws InformationalException {

    ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
      new AppException(
        BDMEVIDENCE.ERR_BANK_ACCOUNT_CANNOT_MODIFY_LINK_EXISTS),
      null, InformationalType.kError,
      curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine, 0);

    TransactionInfo.getInformationalManager().failOperation();
  }

}
