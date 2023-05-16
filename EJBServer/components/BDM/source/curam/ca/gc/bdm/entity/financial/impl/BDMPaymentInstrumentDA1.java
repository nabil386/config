package curam.ca.gc.bdm.entity.financial.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMPMTRECONCILIATIONSTATUS;
import curam.ca.gc.bdm.entity.financial.fact.BDMPaymentInstrumentDA1Factory;
import curam.ca.gc.bdm.entity.financial.fact.BDMPaymentInstrumentFactory;
import curam.ca.gc.bdm.entity.financial.intf.BDMPaymentInstrument;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentInstrumentDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentInstrumentKey;
import curam.ca.gc.bdm.notification.impl.BDMCitizenMessagePaymentDueImpl;
import curam.citizenmessages.listeners.internal.impl.PaymentInstrumentEventListener;
import curam.codetable.PMTRECONCILIATIONSTATUS;
import curam.core.fact.PaymentInstrumentFactory;
import curam.core.impl.EnvVars;
import curam.core.struct.Count;
import curam.core.struct.EffectiveDateReconcilStatusVersionNo;
import curam.core.struct.PIAmountEffectDate;
import curam.core.struct.PIInvalidatedVersionNo;
import curam.core.struct.PIReconcilStatusCode;
import curam.core.struct.PIReconcilStatusCodeInvalidatedVersionNo;
import curam.core.struct.PIStatusAmountEffectDate;
import curam.core.struct.PaymentInstrumentDtls;
import curam.core.struct.PaymentInstrumentKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.persistence.helper.EventDispatcherFactory;
import curam.util.resources.Configuration;
import curam.util.type.Money;
import curam.util.type.NotFoundIndicator;

public class BDMPaymentInstrumentDA1
  extends curam.ca.gc.bdm.entity.financial.base.BDMPaymentInstrumentDA1 {

  @Inject
  protected EventDispatcherFactory<PaymentInstrumentEventListener> paymentInstrumentEventsDispatcher;

  /**
   * Method called after an update/insert is made on the payment instrument
   * table. The BDMPaymentInstrument table is updated with the derived status
   * of the payment instrument, based off the updated status value
   *
   * @param paymentInstrumentID
   * @throws AppException
   * @throws InformationalException
   */
  public BDMPaymentInstrumentDA1() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  private void
    insertOrUpdateBDMPaymentInstrument(final long paymentInstrumentID)
      throws AppException, InformationalException {

    final PaymentInstrumentKey paymentInstrumentKey =
      new PaymentInstrumentKey();

    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    paymentInstrumentKey.pmtInstrumentID = paymentInstrumentID;
    final PaymentInstrumentDtls paymentInstrumentDtls =
      PaymentInstrumentFactory.newInstance().read(nfIndicator,
        paymentInstrumentKey);

    final BDMPaymentInstrumentDtls bdmPaymentInstrumentDtls =
      new BDMPaymentInstrumentDtls();
    bdmPaymentInstrumentDtls.pmtInstrumentID =
      paymentInstrumentDtls.pmtInstrumentID;
    bdmPaymentInstrumentDtls.reconcilStatusCode =
      paymentInstrumentDtls.reconcilStatusCode;
    bdmPaymentInstrumentDtls.spsPmtRefNo =
      paymentInstrumentDtls.referenceNumber;

    final BDMPaymentInstrument bdmPaymentInstrumentObj =
      BDMPaymentInstrumentFactory.newInstance();

    final BDMPaymentInstrumentKey bdmPaymentInstrumentKey =
      new BDMPaymentInstrumentKey();
    bdmPaymentInstrumentKey.pmtInstrumentID = paymentInstrumentID;

    BDMPaymentInstrumentDtls bdmPaymentInstrumentDtls1 =
      new BDMPaymentInstrumentDtls();

    bdmPaymentInstrumentDtls1 =
      bdmPaymentInstrumentObj.read(nfIndicator, bdmPaymentInstrumentKey);

    bdmPaymentInstrumentDtls.reconcilStatusCode =
      this.getCustomBDMPaymentInstrumentStatus(paymentInstrumentDtls);
    final BDMCitizenMessagePaymentDueImpl bdmCitizenMessagePaymentDueImpl =
      new BDMCitizenMessagePaymentDueImpl();
    if (nfIndicator.isNotFound()) {
      // insert custom BDM Payment Instrument
      bdmPaymentInstrumentObj.insert(bdmPaymentInstrumentDtls);
      // send a payment due message to the client portal
      if (bdmPaymentInstrumentDtls.reconcilStatusCode
        .equals(BDMPMTRECONCILIATIONSTATUS.PAYMENT_DUE)) {
        try {
          bdmCitizenMessagePaymentDueImpl
            .CreatePaymentDueMessage(paymentInstrumentDtls);
        } catch (final Exception e) {
          e.printStackTrace();
        }
      }
    } else {
      // modify custom BDM Payment Instrument
      bdmPaymentInstrumentDtls1.reconcilStatusCode =
        bdmPaymentInstrumentDtls.reconcilStatusCode;

      bdmPaymentInstrumentObj.modify(bdmPaymentInstrumentKey,
        bdmPaymentInstrumentDtls1);
      /*
       * START TASK : 9922 Check if payment status = ISSUED trigger notification
       */
      if (bdmPaymentInstrumentDtls.reconcilStatusCode
        .equals(BDMPMTRECONCILIATIONSTATUS.ISSUED)) {
        try {
          paymentInstrumentEventsDispatcher
            .get(PaymentInstrumentEventListener.class)
            .paymentIssued(paymentInstrumentDtls);
        } catch (final Exception e) {
          e.printStackTrace();
        }
        try {
          bdmCitizenMessagePaymentDueImpl
            .UpdatePaymentDueMessageToIssued(paymentInstrumentDtls);
        } catch (final Exception e) {
          e.printStackTrace();
        }
        // send a payment due message to the client portal
      } else if (bdmPaymentInstrumentDtls.reconcilStatusCode
        .equals(BDMPMTRECONCILIATIONSTATUS.PAYMENT_DUE)) {
        try {
          bdmCitizenMessagePaymentDueImpl
            .CreatePaymentDueMessage(paymentInstrumentDtls);
        } catch (final Exception e) {
          e.printStackTrace();
        }
      }
      /*
       * END TASK : 9922 Check if payment status = ISSUED trigger notification
       */
    }

  }

  /**
   * Private method to get derived status of the BDMPaymentInstrument
   *
   * @param paymentInstrumentDtls
   * @throws AppException
   * @throws InformationalException
   */
  private String getCustomBDMPaymentInstrumentStatus(
    final PaymentInstrumentDtls paymentInstrumentDtls)
    throws AppException, InformationalException {

    String customBDMPaymentReconciliationStatus =
      paymentInstrumentDtls.reconcilStatusCode;

    if (PMTRECONCILIATIONSTATUS.PROCESSED
      .equals(paymentInstrumentDtls.reconcilStatusCode)) {
      customBDMPaymentReconciliationStatus =
        BDMPMTRECONCILIATIONSTATUS.PAYMENT_DUE;
      // START - added changes for Task 17820
      final String minIssueAmtStr =
        Configuration.getProperty(EnvVars.BDM_ENV_BDM_MINIMUM_ISSUE_AMOUNT);
      final Money minIssueAmt = new Money(minIssueAmtStr);
      if (paymentInstrumentDtls.amount.isZero()) {
        customBDMPaymentReconciliationStatus = BDMPMTRECONCILIATIONSTATUS.NIL;
      } else if (paymentInstrumentDtls.amount.compareTo(minIssueAmt) <= 0) {
        customBDMPaymentReconciliationStatus =
          BDMPMTRECONCILIATIONSTATUS.UNDERTHRESHOLD;
      }
      // END - JSHAH - Task 17820
    } else if (PMTRECONCILIATIONSTATUS.ISSUED
      .equals(paymentInstrumentDtls.reconcilStatusCode)) {
      if (paymentInstrumentDtls.amount.isZero()) {
        customBDMPaymentReconciliationStatus = BDMPMTRECONCILIATIONSTATUS.NIL;
      } else {
        customBDMPaymentReconciliationStatus =
          BDMPMTRECONCILIATIONSTATUS.ISSUED;
      }
    } else {
      customBDMPaymentReconciliationStatus =
        paymentInstrumentDtls.reconcilStatusCode;
    }
    /*
     * else if (PMTRECONCILIATIONSTATUS.CANCELLED
     * .equals(paymentInstrumentDtls.reconcilStatusCode)) {
     * customBDMPaymentReconciliationStatus =
     * BDMPMTRECONCILIATIONSTATUS.CANCELED;
     * } else if (PMTRECONCILIATIONSTATUS.RECONCILED // Reconciled
     * .equals(paymentInstrumentDtls.reconcilStatusCode)) {
     * customBDMPaymentReconciliationStatus =
     * BDMPMTRECONCILIATIONSTATUS.ISSUED;
     * }
     */
    // Check for REISSUED STATUS
    if (BDMPMTRECONCILIATIONSTATUS.PAYMENT_DUE
      .equals(customBDMPaymentReconciliationStatus)) {
      final curam.ca.gc.bdm.entity.financial.intf.BDMPaymentInstrumentDA1 bdmPaymentInstrumentDA1 =
        BDMPaymentInstrumentDA1Factory.newInstance();
      final PaymentInstrumentKey paymentInstrumentKey =
        new PaymentInstrumentKey();
      paymentInstrumentKey.pmtInstrumentID =
        paymentInstrumentDtls.pmtInstrumentID;
      final Count count =
        bdmPaymentInstrumentDA1.isPaymentRegenerated(paymentInstrumentKey);

      if (count.numberOfRecords > 0) {
        customBDMPaymentReconciliationStatus =
          BDMPMTRECONCILIATIONSTATUS.REISSUED;
      }
    }

    return customBDMPaymentReconciliationStatus;
  }

  @Override
  protected void postinsert(final PaymentInstrumentDtls details)
    throws AppException, InformationalException {

    super.postinsert(details);

    this.insertOrUpdateBDMPaymentInstrument(details.pmtInstrumentID);
  }

  @Override
  protected void postmodify(final PaymentInstrumentKey key,
    final PaymentInstrumentDtls details)
    throws AppException, InformationalException {

    this.insertOrUpdateBDMPaymentInstrument(details.pmtInstrumentID);

  }

  @Override
  protected void postmodifyEffectiveDateReconcilStatus(
    final PaymentInstrumentKey key,
    final EffectiveDateReconcilStatusVersionNo dtls)
    throws AppException, InformationalException {

    super.postmodifyEffectiveDateReconcilStatus(key, dtls);

    this.insertOrUpdateBDMPaymentInstrument(key.pmtInstrumentID);
  }

  @Override
  protected void postmodifyInvalidatedInd(final PaymentInstrumentKey key,
    final PIInvalidatedVersionNo details)
    throws AppException, InformationalException {

    super.postmodifyInvalidatedInd(key, details);

    this.insertOrUpdateBDMPaymentInstrument(key.pmtInstrumentID);
  }

  @Override
  protected void postmodifyReconcilStatusCode(
    final PaymentInstrumentKey paymentInstrumentKey,
    final PIReconcilStatusCode piStatusCodeVersionNo)
    throws AppException, InformationalException {

    super.postmodifyReconcilStatusCode(paymentInstrumentKey,
      piStatusCodeVersionNo);

    this.insertOrUpdateBDMPaymentInstrument(
      paymentInstrumentKey.pmtInstrumentID);
  }

  @Override
  protected void postmodifyReconcilStatusCodeInvalidatedInd(
    final PaymentInstrumentKey key,
    final PIReconcilStatusCodeInvalidatedVersionNo details)
    throws AppException, InformationalException {

    super.postmodifyReconcilStatusCodeInvalidatedInd(key, details);

    this.insertOrUpdateBDMPaymentInstrument(key.pmtInstrumentID);
  }

  @Override
  protected void postmodifyStatusAmountEffectDate(
    final PaymentInstrumentKey paymentInstrumentKey,
    final PIStatusAmountEffectDate piStatusAmountEffectDate)
    throws AppException, InformationalException {

    super.postmodifyStatusAmountEffectDate(paymentInstrumentKey,
      piStatusAmountEffectDate);

    this.insertOrUpdateBDMPaymentInstrument(
      paymentInstrumentKey.pmtInstrumentID);
  }

  @Override
  protected void postmodifyAmountEffectDate(
    final PaymentInstrumentKey pmtInstrumentKey,
    final PIAmountEffectDate piAmountEffectDate)
    throws AppException, InformationalException {

    super.postmodifyAmountEffectDate(pmtInstrumentKey, piAmountEffectDate);
    this.insertOrUpdateBDMPaymentInstrument(pmtInstrumentKey.pmtInstrumentID);
  }

}
