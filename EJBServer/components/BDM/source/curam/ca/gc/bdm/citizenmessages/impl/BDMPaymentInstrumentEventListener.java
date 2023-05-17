package curam.ca.gc.bdm.citizenmessages.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMGCNotifyTemplateType;
import curam.ca.gc.bdm.gcnotify.impl.BDMGCNotifyHelper;
import curam.ca.gc.bdm.message.BDMCORRESPONDENCE;
import curam.ca.gc.bdm.notification.impl.BDMNotificationImpl;
import curam.citizenmessages.listeners.internal.impl.PaymentInstrumentEventListener;
import curam.citizenworkspace.message.PARTICIPANTPAYMENTMESSAGES;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccountManager;
import curam.core.impl.EnvVars;
import curam.core.struct.PaymentInstrumentDtls;
import curam.participant.impl.ConcernRole;
import curam.participant.impl.ConcernRoleDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.Trace;
import java.io.IOException;

public class BDMPaymentInstrumentEventListener
  extends PaymentInstrumentEventListener {

  @Inject
  private ConcernRoleDAO concernRoleDAO;

  @Inject
  private CitizenWorkspaceAccountManager citizenWorkspaceAccountManager;

  @Inject
  BDMNotificationImpl bdmNotificationImpl;

  public BDMPaymentInstrumentEventListener() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Method added for "TASK 19912: Call to GC notify".
   */
  @Override
  public void paymentIssued(final PaymentInstrumentDtls paymentInstrumentDtls)
    throws InformationalException {

    final ConcernRole concernRole = this.concernRoleDAO
      .get(Long.valueOf(paymentInstrumentDtls.concernRoleID));

    if (!isLinkedUser(concernRole)) {
      return;
    }

    // Call to super class method.
    super.paymentIssued(paymentInstrumentDtls);

    // Custom call to GC Notify.
    // if GC notify is true, send the GC notify Alert
    if (Configuration.getBooleanProperty(
      EnvVars.BDM_ENV_NOTIFICATION_PAYMENTISSUED_GCNOTIFY_IND)) {
      final String gcNotifyTemplateType = BDMGCNotifyTemplateType.BDM_GC_NF;

      try {
        // bdmNotificationImpl.startDPGCNotifyAlert(
        // paymentInstrumentDtls.concernRoleID, templateType);
        if (isLinkedUser(concernRole)) {
          final BDMGCNotifyHelper gcNotifyHelper = new BDMGCNotifyHelper();
          gcNotifyHelper.sendGCNotifyRealTimeAlert(concernRole.getID(),
            gcNotifyTemplateType);
        }
      } catch (final AppException appException) {
        Trace.kTopLevelLogger
          .error(BDMCORRESPONDENCE.ERR_GCNOTIFY_PAYMENT_ISSUED_MESSAGE
            + appException.getMessage());
      } catch (final IOException ioe) {
        Trace.kTopLevelLogger
          .error(BDMCORRESPONDENCE.ERR_GCNOTIFY_PAYMENT_ISSUED_MESSAGE
            + ioe.getMessage());
      }
    }
  }

  /**
   * Method added for "TASK 19912: Call to GC notify".
   */
  private boolean isLinkedUser(final ConcernRole concernRole)
    throws InformationalException {

    boolean isLinkedUser = false;
    Boolean hasLinkedAccntObj = null;
    try {
      hasLinkedAccntObj =
        this.citizenWorkspaceAccountManager.hasLinkedAccount(concernRole);
      isLinkedUser = hasLinkedAccntObj.booleanValue();
    } catch (final AppException e) {
      Trace.kTopLevelLogger.error(
        PARTICIPANTPAYMENTMESSAGES.ERR_PAYMENT_INSTRUMENT_PAYMENT_ISSUED_MESSAGE
          + e.getMessage());
    }

    return isLinkedUser;
  }
}
