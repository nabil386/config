package curam.ca.gc.bdm.notification.impl;

import com.google.inject.ImplementedBy;
import curam.participant.impl.ConcernRole;
import curam.participantmessages.persistence.impl.ParticipantMessage;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

@ImplementedBy(BDMCitizenMessageClaimEstablishDeniedImpl.class)
public interface BDMCitizenMessageClaimEstablishDenied {

  ParticipantMessage getMessageByRelatedID(ConcernRole concernRole,
    long caseID) throws InformationalException, AppException;

  ParticipantMessage createClaimEstablishedMessage(ConcernRole concernRole,
    long caseID) throws InformationalException, AppException;

  void expireClaimEstablishedNotificationMessage(
    ParticipantMessage participantMessage) throws InformationalException;

  ParticipantMessage createClaimDeniedMessage(ConcernRole concernRole,
    long caseID) throws InformationalException, AppException;

  void expireClaimDeniedNotificationMessage(
    final ParticipantMessage participantMessage)
    throws InformationalException;

  ParticipantMessage
    updateClaimEstablishedMessage(ParticipantMessage participantMessage)
      throws InformationalException, AppException;

  ParticipantMessage
    updateClaimDeniedMessage(ParticipantMessage participantMessage)
      throws InformationalException, AppException;
}
