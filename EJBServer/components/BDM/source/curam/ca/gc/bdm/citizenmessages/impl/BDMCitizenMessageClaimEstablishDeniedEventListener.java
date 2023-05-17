package curam.ca.gc.bdm.citizenmessages.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.notification.impl.BDMNotification;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccountManager;
import curam.participant.impl.ConcernRole;
import curam.participantmessages.events.impl.CitizenMessagesEvent;
import curam.participantmessages.impl.ParticipantMessages;
import curam.participantmessages.persistence.impl.ParticipantMessage;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.workspaceservices.util.impl.ResourceStoreHelper;
import java.util.List;

public class BDMCitizenMessageClaimEstablishDeniedEventListener
  extends CitizenMessagesEvent {

  @Inject
  private CitizenWorkspaceAccountManager citizenWorkspaceAccountManager;

  @Inject
  private BDMNotification bdmNotification;

  @Inject
  private ResourceStoreHelper resourceStoreHelper;

  public BDMCitizenMessageClaimEstablishDeniedEventListener() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public void
    userRequestsMessages(final ParticipantMessages participantMessages)
      throws AppException, InformationalException {

    final ConcernRole concernRole =
      this.citizenWorkspaceAccountManager.getLoggedInUserConcernRoleID();
    final List<ParticipantMessage> participantMessageList = bdmNotification
      .searchActiveClaimEstablishDeniedParticipantMessages(concernRole);

    for (final ParticipantMessage participantMessage : participantMessageList) {
      final curam.participantmessages.impl.ParticipantMessage partMsg =
        new curam.participantmessages.impl.ParticipantMessage(
          participantMessage.getMessageType());

      // set essential details for correspondence unread information message
      partMsg.setEffectiveDateTime(participantMessage.getEffectiveDateTime());

      partMsg.setMessageBody(resourceStoreHelper.getPropertyValue(
        participantMessage.getPropertyFileName(),
        participantMessage.getMessageProperty()));
      partMsg.setMessageTitle(resourceStoreHelper.getPropertyValue(
        participantMessage.getPropertyFileName(),
        participantMessage.getTitleProperty()));

      participantMessages.addMessage(partMsg);
    }
  }
}
