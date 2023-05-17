package curam.ca.gc.bdm.citizenmessages.impl;

import com.google.inject.Inject;
import curam.advisor.impl.ParameterDAO;
import curam.ca.gc.bdm.notification.impl.BDMNotification;
import curam.ca.gc.bdm.notification.impl.BDMNotificationConstants;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccountManager;
import curam.codetable.impl.CASESTATUSEntry;
import curam.codetable.impl.PRODUCTTYPEEntry;
import curam.core.fact.CaseStatusFactory;
import curam.core.fact.ProductDeliveryFactory;
import curam.core.impl.EnvVars;
import curam.core.struct.CaseStatusDtls;
import curam.core.struct.CurrentCaseStatusKey;
import curam.core.struct.ProductDeliveryKey;
import curam.core.struct.ProductDeliveryTypeDetails;
import curam.participant.impl.ConcernRole;
import curam.participantmessages.codetable.impl.ParticipantMessageTypeEntry;
import curam.participantmessages.events.impl.CitizenMessagesEvent;
import curam.participantmessages.impl.ParticipantMessages;
import curam.participantmessages.persistence.impl.ParticipantMessage;
import curam.participantmessages.persistence.impl.ParticipantMessageDAO;
import curam.piwrapper.caseheader.impl.CaseHeader;
import curam.piwrapper.caseheader.impl.CaseHeaderDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.transaction.TransactionInfo;
import curam.util.type.DateTime;
import curam.workspaceservices.util.impl.ResourceStoreHelper;
import java.util.List;

public class BDMCitizenMessageSuspendBenefitEventListener
  extends CitizenMessagesEvent {

  @Inject
  private CitizenWorkspaceAccountManager citizenWorkspaceAccountManager;

  @Inject
  private ParticipantMessageDAO participantMessageDAO;

  @Inject
  private CaseHeaderDAO caseHeaderDAO;

  @Inject
  private ParameterDAO parameterDAO;

  @Inject
  private BDMNotification bdmNotification;

  @Inject
  private ResourceStoreHelper resourceStoreHelper;

  public BDMCitizenMessageSuspendBenefitEventListener() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public void
    userRequestsMessages(final ParticipantMessages participantMessages)
      throws AppException, InformationalException {

    final ConcernRole concernRole =
      this.citizenWorkspaceAccountManager.getLoggedInUserConcernRoleID();
    final List<ParticipantMessage> participantMessageList =
      bdmNotification.searchSuspendBenefitParticipantMessages(concernRole);

    long pdCaseID = 0l;
    for (final ParticipantMessage participantMessage : participantMessageList) {
      final curam.participantmessages.impl.ParticipantMessage partMsg =
        new curam.participantmessages.impl.ParticipantMessage(
          participantMessage.getMessageType());

      if (participantMessage.getExpiryDateTime()
        .after(DateTime.getCurrentDateTime())) {
        pdCaseID = participantMessage.getRelatedID();

        final ProductDeliveryKey pdKey = new ProductDeliveryKey();
        pdKey.caseID = pdCaseID;
        final ProductDeliveryTypeDetails pdTypeDetail =
          ProductDeliveryFactory.newInstance().readProductType(pdKey);

        final CaseHeader pdCase = caseHeaderDAO.get(pdCaseID);
        if (pdCase.getStatus().equals(CASESTATUSEntry.SUSPENDED)) {
          removeAllPaymentMessage(participantMessages);
          final CurrentCaseStatusKey currentCaseKey =
            new CurrentCaseStatusKey();
          currentCaseKey.caseID = pdCaseID;
          final CaseStatusDtls caseStatusDtls = CaseStatusFactory
            .newInstance().readCurrentStatusByCaseID1(currentCaseKey);

          // set essential details for Suspend Benefit message
          partMsg.setEffectiveDateTime(caseStatusDtls.startDateTime);

          partMsg.setMessageBody(resourceStoreHelper.getPropertyValue(
            BDMNotificationConstants.kBDMNotificationPropertyFile,
            Configuration.getProperty(
              EnvVars.BDM_ENV_NOTIFICATION_BODY_SUSPENDBENEFIT)));
          partMsg.setMessageTitle(resourceStoreHelper.getPropertyValue(
            BDMNotificationConstants.kBDMNotificationPropertyFile,
            Configuration.getProperty(
              EnvVars.BDM_ENV_NOTIFICATION_TITLE_SUSPENDBENEFIT)));

          partMsg.addParameter(
            BDMNotificationConstants.kSuspendBenefitNotiticationBenefitName,
            PRODUCTTYPEEntry.get(pdTypeDetail.productType)
              .toUserLocaleString());
          partMsg.addParameter(
            BDMNotificationConstants.kSuspendBenefitNotiticationDate,
            caseStatusDtls.startDate
              .toString(TransactionInfo.getProgramLocale()));
        }

        // participantMessages.addMessage(partMsg);
      }
    }

  }

  private void
    removeAllPaymentMessage(final ParticipantMessages participantMessages) {

    for (final curam.participantmessages.impl.ParticipantMessage participantMessage : participantMessages
      .getMessages()) {
      if (participantMessage.getType()
        .equals(ParticipantMessageTypeEntry.PAYMENT)) {
        participantMessages.getMessages().remove(participantMessage);
      }
    }
  }
}
