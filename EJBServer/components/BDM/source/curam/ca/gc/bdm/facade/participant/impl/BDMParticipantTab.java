package curam.ca.gc.bdm.facade.participant.impl;

import curam.ca.gc.bdm.sl.fact.BDMParticipantTabProcessFactory;
import curam.core.facade.struct.MaintainConcernRoleKey;
import curam.core.facade.struct.ParticipantTabXML;
import curam.core.sl.struct.ParticipantContentDetailsXML;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMParticipantTab
  extends curam.ca.gc.bdm.facade.participant.base.BDMParticipantTab {

  @Override
  public ParticipantTabXML readPerson(final MaintainConcernRoleKey key)
    throws AppException, InformationalException {

    final ParticipantTabXML participantTabXML = new ParticipantTabXML();
    ParticipantContentDetailsXML participantContentDetailsXML =
      new ParticipantContentDetailsXML();

    // Call custom SL implementation
    participantContentDetailsXML = BDMParticipantTabProcessFactory
      .newInstance().readPerson(key.maintainConcernRoleKey);
    participantTabXML.xmlPanelData =
      participantContentDetailsXML.xmlPanelData;
    participantTabXML.description =
      participantContentDetailsXML.participantName;
    return participantTabXML;

  }

  @Override
  public ParticipantTabXML
    readProspectPerson(final MaintainConcernRoleKey key)
      throws AppException, InformationalException {

    final ParticipantTabXML participantTabXML = new ParticipantTabXML();
    ParticipantContentDetailsXML participantContentDetailsXML =
      new ParticipantContentDetailsXML();

    // Call custom SL implementation
    participantContentDetailsXML = BDMParticipantTabProcessFactory
      .newInstance().readProspectPerson(key.maintainConcernRoleKey);
    participantTabXML.xmlPanelData =
      participantContentDetailsXML.xmlPanelData;
    participantTabXML.description =
      participantContentDetailsXML.participantName;
    return participantTabXML;

  }

}
