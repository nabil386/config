package curam.ca.gc.bdm.notification.impl;

import com.google.inject.ImplementedBy;
import curam.advisor.impl.Parameter;
import curam.participant.impl.ConcernRole;
import curam.participantmessages.codetable.impl.ParticipantMessageTypeEntry;
import curam.participantmessages.persistence.impl.ParticipantMessage;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import java.util.List;

@ImplementedBy(BDMNotificationImpl.class)
public interface BDMNotification {

  ParticipantMessage
    createClaimEstablishedNotificationMessage(long concernRoleID, long caseID)
      throws InformationalException, AppException;

  ParticipantMessage createClaimDeniedNotificationMessage(long concernRoleID,
    long caseID) throws InformationalException, AppException;

  curam.participantmessages.impl.ParticipantMessage
    createUnreadInformationalMessage(long concernRoleID, int unreadCount)
      throws InformationalException, AppException;

  Parameter createUnreadCountParam(int unreadCount)
    throws InformationalException;

  List<ParticipantMessage> getMessageByConcernRoleTypeAndRelatedID(
    ConcernRole concernRole, ParticipantMessageTypeEntry messageType,
    long relatedID) throws InformationalException, AppException;

  List<ParticipantMessage>
    searchActiveClaimEstablishDeniedParticipantMessages(
      ConcernRole concernRole) throws InformationalException, AppException;

  List<ParticipantMessage>
    searchSuspendBenefitParticipantMessages(ConcernRole concernRole);

  List<ParticipantMessage> searchActiveParticipantMessagesByType(
    final ConcernRole concernRole, final ParticipantMessageTypeEntry type);

  public ParticipantMessage createSuspendBenefitCaseNotificationMessage(
    final long concernRoleID, final long caseID)
    throws InformationalException, AppException;

  void triggerUnsuspendBenefitCaseNotificationMessage(long concernRoleID,
    final long caseID) throws AppException, InformationalException;

}
