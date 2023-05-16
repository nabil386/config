package curam.ca.gc.bdm.citizenmessages.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.communication.impl.BDMCommunicationHelper;
import curam.ca.gc.bdm.notification.impl.BDMNotification;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccountManager;
import curam.participant.impl.ConcernRole;
import curam.participantmessages.events.impl.CitizenMessagesEvent;
import curam.participantmessages.impl.ParticipantMessages;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;

public class BDMDynamicUnreadCorrespondenceEventListener
  extends CitizenMessagesEvent {

  @Inject
  private CitizenWorkspaceAccountManager citizenWorkspaceAccountManager;

  @Inject
  private BDMNotification bdmNotification;

  protected BDMDynamicUnreadCorrespondenceEventListener() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public void
    userRequestsMessages(final ParticipantMessages participantMessages)
      throws AppException, InformationalException {

    final ConcernRole concernRole =
      this.citizenWorkspaceAccountManager.getLoggedInUserConcernRoleID();
    final BDMCommunicationHelper bdmCommHelper = new BDMCommunicationHelper();

    // read the Unread Count
    final int unreadCount = bdmCommHelper.getUnreadCount(concernRole.getID());

    if (unreadCount > 0) {
      final curam.participantmessages.impl.ParticipantMessage unreadCorrMessage =
        bdmNotification.createUnreadInformationalMessage(concernRole.getID(),
          unreadCount);
      participantMessages.addMessage(unreadCorrMessage);
    }
  }
}
