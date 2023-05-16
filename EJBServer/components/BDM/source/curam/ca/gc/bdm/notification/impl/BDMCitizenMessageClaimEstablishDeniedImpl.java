package curam.ca.gc.bdm.notification.impl;

import com.google.inject.Inject;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.participant.impl.ConcernRole;
import curam.participantmessages.codetable.impl.ParticipantMessageTypeEntry;
import curam.participantmessages.persistence.impl.ParticipantMessage;
import curam.participantmessages.persistence.impl.ParticipantMessageDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.type.DateTime;
import java.util.List;

public class BDMCitizenMessageClaimEstablishDeniedImpl
  implements BDMCitizenMessageClaimEstablishDenied {

  @Inject
  private ParticipantMessageDAO participantMessageDAO;

  public BDMCitizenMessageClaimEstablishDeniedImpl() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Create and setup the Claim Established notification message for the citizen
   * portal
   *
   * @return ParticipantMessage
   * @param Concern Role Persistent Object
   */
  @Override
  public ParticipantMessage createClaimEstablishedMessage(
    final ConcernRole concernRole, final long caseID)
    throws InformationalException, AppException {

    final curam.participantmessages.persistence.impl.ParticipantMessage participantMessage =
      participantMessageDAO.newInstance();

    // set concern role, case ID
    participantMessage.setConcernRole(concernRole);
    participantMessage.setRelatedID(caseID);

    setClaimEstablishedMessage(participantMessage);
    participantMessage.insert();

    return participantMessage;

  }

  /**
   * modify existing message to be Claim Established notification message
   *
   * @return ParticipantMessage
   * @param Concern Role Persistent Object
   */
  @Override
  public ParticipantMessage updateClaimEstablishedMessage(
    final curam.participantmessages.persistence.impl.ParticipantMessage participantMessage)
    throws InformationalException, AppException {

    setClaimEstablishedMessage(participantMessage);
    participantMessage.modify();

    return participantMessage;

  }

  // @Override
  public ParticipantMessage setClaimEstablishedMessage(
    final curam.participantmessages.persistence.impl.ParticipantMessage participantMessage)
    throws InformationalException, AppException {

    // set effect data time
    participantMessage.setEffectiveDateTime(DateTime.getCurrentDateTime());
    // read duration property and set the expiry day
    final int durationDay = Configuration
      .getIntProperty(EnvVars.BDM_ENV_NOTIFICATION_DURATION_CLAIMESTABLISHED);
    participantMessage
      .setExpiryDateTime(participantMessage.getEffectiveDateTime()
        .addTime(durationDay * CuramConst.gkNumberOfHoursInADay, 0, 0));
    // set property file, message title and body
    participantMessage.setMessageProperty(Configuration
      .getProperty(EnvVars.BDM_ENV_NOTIFICATION_BODY_CLAIMESTABLISHED));
    participantMessage.setPropertyFileName(Configuration
      .getProperty(EnvVars.BDM_ENV_NOTIFICATION_FILENAME_CLAIMESTABLISHED));
    participantMessage.setTitleProperty(Configuration
      .getProperty(EnvVars.BDM_ENV_NOTIFICATION_TITLE_CLAIMESTABLISHED));
    // set message type to Application and display it on the notification card
    participantMessage
      .setMessageType(ParticipantMessageTypeEntry.APPLICATION);

    return participantMessage;

  }

  /**
   * expire the Claim Establish notification message for the citizen portal to
   * remove the message out of the notification card
   *
   * @param Concern Role Persistent Object
   */
  @Override
  public void expireClaimEstablishedNotificationMessage(
    final ParticipantMessage participantMessage)
    throws InformationalException {

    participantMessage.setExpiryDateTime(DateTime.getCurrentDateTime());
    participantMessage.modify();

  }

  /**
   * Create the Claim Denied notification message for the citizen portal
   *
   * @return ParticipantMessage
   * @param Concern Role Persistent Object
   */
  @Override
  public ParticipantMessage
    createClaimDeniedMessage(final ConcernRole concernRole, final long caseID)
      throws InformationalException, AppException {

    final curam.participantmessages.persistence.impl.ParticipantMessage participantMessage =
      participantMessageDAO.newInstance();

    // set Concern Role instance
    participantMessage.setConcernRole(concernRole);
    participantMessage.setRelatedID(caseID);
    setClaimDeniedMessage(participantMessage);
    participantMessage.insert();

    return participantMessage;

  }

  /**
   * Update the message to Claim Denied notification message
   *
   * @return ParticipantMessage
   * @param Concern Role Persistent Object
   */
  @Override
  public ParticipantMessage
    updateClaimDeniedMessage(final ParticipantMessage participantMessage)
      throws InformationalException, AppException {

    setClaimDeniedMessage(participantMessage);
    participantMessage.modify();

    return participantMessage;

  }

  public ParticipantMessage setClaimDeniedMessage(
    final curam.participantmessages.persistence.impl.ParticipantMessage participantMessage)
    throws InformationalException, AppException {

    // set effective date time
    participantMessage.setEffectiveDateTime(DateTime.getCurrentDateTime());
    // read duration property and set the expiry day
    final int durationDay = Configuration
      .getIntProperty(EnvVars.BDM_ENV_NOTIFICATION_DURATION_CLAIMDENIED);
    participantMessage
      .setExpiryDateTime(participantMessage.getEffectiveDateTime()
        .addTime(durationDay * CuramConst.gkNumberOfHoursInADay, 0, 0));
    // set property file, message title and body
    participantMessage.setMessageProperty(Configuration
      .getProperty(EnvVars.BDM_ENV_NOTIFICATION_BODY_CLAIMDENIED));
    participantMessage.setPropertyFileName(Configuration
      .getProperty(EnvVars.BDM_ENV_NOTIFICATION_FILENAME_CLAIMDENIED));
    participantMessage.setTitleProperty(Configuration
      .getProperty(EnvVars.BDM_ENV_NOTIFICATION_TITLE_CLAIMDENIED));
    // set message type to Application and display it on the notification card
    participantMessage
      .setMessageType(ParticipantMessageTypeEntry.APPLICATION);

    return participantMessage;

  }

  /**
   * expire the Claim Denied notification message for the citizen portal to
   * remove the message out of the notification card
   *
   * @param Concern Role Persistent Object
   */
  @Override
  public void expireClaimDeniedNotificationMessage(
    final ParticipantMessage participantMessage)
    throws InformationalException {

    participantMessage.setExpiryDateTime(DateTime.getCurrentDateTime());
    participantMessage.modify();

  }

  @Override
  public ParticipantMessage
    getMessageByRelatedID(final ConcernRole concernRole, final long relatedID)
      throws InformationalException, AppException {

    // get all current Application type messages
    final List<ParticipantMessage> participantMessageList =
      participantMessageDAO.searchByParticipantAndMessageType(concernRole,
        ParticipantMessageTypeEntry.APPLICATION);

    ParticipantMessage participantMessage = null;
    // Sort through all current messages
    for (final ParticipantMessage existingMessage : participantMessageList) {
      if (existingMessage.getRelatedID() == relatedID) {
        participantMessage = existingMessage;
        break;
      }
    }

    return participantMessage;
  }

}
