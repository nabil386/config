package curam.ca.gc.bdm.facade.productdelivery.impl;

import curam.ca.gc.bdm.codetable.BDMFININSTRUCTIONSTATUS;
import curam.ca.gc.bdm.codetable.BDMPMTRECONCILIATIONSTATUS;
import curam.ca.gc.bdm.entity.financial.fact.BDMPaymentInstrumentFactory;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentInstrumentDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentInstrumentKey;
import curam.codetable.FININSTRUCTIONSTATUS;
import curam.core.facade.struct.PaymentDisplayIndicators;
import curam.core.fact.FinancialInstructionFactory;
import curam.core.fact.PaymentInstructionFactory;
import curam.core.fact.PaymentInstrumentFactory;
import curam.core.impl.CuramConst;
import curam.core.struct.FinInstructionID;
import curam.core.struct.FinancialInstructionDtls;
import curam.core.struct.FinancialInstructionKey;
import curam.core.struct.InvalidatedInd;
import curam.core.struct.PaymentInstructionDtls;
import curam.core.struct.PaymentInstrumentKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.LocalisableString;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTableItemIdentifier;
import curam.util.type.NotFoundIndicator;
import java.util.HashSet;
import java.util.Set;

public class BDMViewPaymentsUtil {

  /**
   * Determines the status from the BDMFinancialInstruction table
   *
   * @param key
   * @param invalidatedInd
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static String getPaymentStatus(final String status,
    final boolean invalidatedInd)
    throws AppException, InformationalException {

    final LocalisableString paymentStatus =
      new LocalisableString(curam.message.GENERAL.INF_STATUSCODE_DETAILS);
    paymentStatus.arg(new CodeTableItemIdentifier(
      BDMPMTRECONCILIATIONSTATUS.TABLENAME, status));

    if (status.equals(BDMPMTRECONCILIATIONSTATUS.CANCELED)
      && invalidatedInd) {

      paymentStatus
        .arg(CuramConst.gkSpace + curam.message.GENERAL.INF_INVALIDATED
          .getMessageText(TransactionInfo.getProgramLocale()));

    } else {
      // If the payment is not cancelled and invalidated there is no second
      // argument.
      paymentStatus.arg("");
    }

    return paymentStatus.toClientFormattedText();

  }

  /**
   * Determines the status from the BDMFinancialInstruction table
   *
   * @param key
   * @param invalidatedInd
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static String getPaymentStatusForFinInstruction(final String status,
    final boolean invalidatedInd)
    throws AppException, InformationalException {

    final LocalisableString paymentStatus =
      new LocalisableString(curam.message.GENERAL.INF_STATUSCODE_DETAILS);
    paymentStatus.arg(
      new CodeTableItemIdentifier(FININSTRUCTIONSTATUS.TABLENAME, status));

    if (status.equals(FININSTRUCTIONSTATUS.CANCELLED) && invalidatedInd) {

      paymentStatus
        .arg(CuramConst.gkSpace + curam.message.GENERAL.INF_INVALIDATED
          .getMessageText(TransactionInfo.getProgramLocale()));

    } else {
      // If the payment is not cancelled and invalidated there is no second
      // argument.
      paymentStatus.arg("");
    }

    return paymentStatus.toClientFormattedText();

  }

  /**
   * Check if a financial instruction is invalidated
   *
   * @param bdmViewCaseInstructionDetails
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static boolean isInvalidated(final long finInstructionID)
    throws AppException, InformationalException {

    // find the associated payment instrument, if it exists
    boolean isInvalidated = false;
    final FinInstructionID finInstructionIDKey = new FinInstructionID();
    finInstructionIDKey.finInstructionID = finInstructionID;
    final NotFoundIndicator nfIndicator = new NotFoundIndicator();

    final PaymentInstructionDtls paymentInstructionDtls =
      PaymentInstructionFactory.newInstance()
        .readByFinInstructionID(nfIndicator, finInstructionIDKey);

    final NotFoundIndicator pmtInstrumentNFIndicator =
      new NotFoundIndicator();
    if (!nfIndicator.isNotFound()) {

      // determine if payment instrument is invalidated
      final PaymentInstrumentKey pmtInstrumentKey =
        new PaymentInstrumentKey();
      pmtInstrumentKey.pmtInstrumentID =
        paymentInstructionDtls.pmtInstrumentID;
      final InvalidatedInd invalidatedInd =
        PaymentInstrumentFactory.newInstance()
          .readInvalidatedInd(pmtInstrumentNFIndicator, pmtInstrumentKey);

      if (!pmtInstrumentNFIndicator.isNotFound()) {
        isInvalidated = invalidatedInd.invalidatedInd;
      }

    }
    return isInvalidated;
  }

  /**
   * Modifies the payment display indicators based off the
   * BDMFinancialInstruction status
   *
   * @param displayIndicators
   * @param statusCode
   * @param isInvalidated
   */
  public static PaymentDisplayIndicators modifyPaymentDisplayIndicators(
    final PaymentDisplayIndicators displayIndicators, final String statusCode,
    final boolean isInvalidated) {

    final Set<String> disableMenuItemStatuses = new HashSet<String>() {

      {
        add(BDMFININSTRUCTIONSTATUS.NIL);
        add(BDMFININSTRUCTIONSTATUS.NOTINTHRESHOLD);
        add(BDMFININSTRUCTIONSTATUS.PAYMENTDUE);
        add(BDMFININSTRUCTIONSTATUS.TRANSFERRED);
        add(BDMFININSTRUCTIONSTATUS.ISSUED);
        add(BDMFININSTRUCTIONSTATUS.REISSUE);
      }
    };
    // disable all menu items if it is invalidated, or a non cancelled payment

    if (isInvalidated) {
      return new PaymentDisplayIndicators();
    } // AGAUR - For temporary basis, enable cancel till interface team complete
      // this work.
    if (disableMenuItemStatuses.contains(statusCode)) {
      final PaymentDisplayIndicators indicators =
        new PaymentDisplayIndicators();
      indicators.cancelInd = true;
      return indicators;

    }
    // ootb already ensures a cancelled payment can only be reissued or
    // invalidated if it hasn't been reissued once already, reissuing with
    // deductions must be disabled
    else if (statusCode.equals(BDMFININSTRUCTIONSTATUS.CANCELLED)) {
      displayIndicators.reissueDeductionIndOpt = false;

    }
    return displayIndicators;
  }

  /**
   * Given a pmtInstrumentID, gets the bdm status code
   *
   * @param pmtInstrumentID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static String getBDMPaymentInstructionStatusCode(
    final long pmtInstrumentID) throws AppException, InformationalException {

    // read the status
    /*
     * final BDMFinancialInstructionKey bdmFinInsKey =
     * new BDMFinancialInstructionKey();
     * bdmFinInsKey.finInstructionID = finInstructionID;
     * final BDMFinancialInstructionDtls bdmFinancialInstructionDtls =
     * BDMFinancialInstructionFactory.newInstance().read(bdmFinInsKey);
     * return bdmFinancialInstructionDtls.statusCode;
     */
    // START - Updated the fix for Task 21028 - Refactoring the code after
    // removing BDMFinancialInstruction entity
    final BDMPaymentInstrumentKey bdmPaymentInstrumentKey =
      new BDMPaymentInstrumentKey();
    bdmPaymentInstrumentKey.pmtInstrumentID = pmtInstrumentID;
    final BDMPaymentInstrumentDtls bdmPaymentInstrumentDtls =
      BDMPaymentInstrumentFactory.newInstance().read(bdmPaymentInstrumentKey);
    return bdmPaymentInstrumentDtls.reconcilStatusCode;
    // END - Task 21028 - JSHAH
  }

  /**
   * Given a financial instruction ID, gets the bdm status code
   *
   * @param finInstructionID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static String getFinInstructionStatusCode(
    final long finInstructionID) throws AppException, InformationalException {

    // read the status
    // START - Updated the fix for Task 21028 - Refactoring the code after
    // removing FinancialInstruction entity
    final FinancialInstructionKey financialInstructionKey =
      new FinancialInstructionKey();
    financialInstructionKey.finInstructionID = finInstructionID;

    final FinancialInstructionDtls finInstructionDtls =
      FinancialInstructionFactory.newInstance().read(financialInstructionKey);
    return finInstructionDtls.statusCode;
    // END - Task 21028 - JSHAH
  }

}
