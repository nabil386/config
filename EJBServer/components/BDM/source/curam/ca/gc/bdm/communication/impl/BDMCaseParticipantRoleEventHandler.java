package curam.ca.gc.bdm.communication.impl;

import curam.codetable.CASEPARTICIPANTROLETYPE;
import curam.core.events.CASEPARTICIPANTROLE;
import curam.core.fact.SynchronizeEventsFactory;
import curam.core.sl.entity.fact.CaseParticipantRoleFactory;
import curam.core.sl.entity.struct.CaseParticipantRoleCaseAndTypeKey;
import curam.core.sl.entity.struct.CaseParticipantRoleDtls;
import curam.core.sl.entity.struct.CaseParticipantRoleKey;
import curam.core.sl.entity.struct.CaseParticipantRoleNameDetails;
import curam.core.sl.entity.struct.CaseParticipantRoleNameDetailsList;
import curam.util.events.impl.EventFilter;
import curam.util.events.impl.EventHandler;
import curam.util.events.struct.Event;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMCaseParticipantRoleEventHandler
  implements EventHandler, EventFilter {

  @Override
  public boolean accept(final Event event)
    throws AppException, InformationalException {

    final curam.core.sl.entity.intf.CaseParticipantRole cprEntityObj =
      CaseParticipantRoleFactory.newInstance();

    final CaseParticipantRoleKey caseParticipantRoleKey =
      new CaseParticipantRoleKey();
    caseParticipantRoleKey.caseParticipantRoleID = event.primaryEventData;

    final CaseParticipantRoleDtls caseParticipantRoleDtls =
      cprEntityObj.read(caseParticipantRoleKey);

    // accept when event is INSERT_CASE_PARTICIPANT_ROLE
    if (event.eventKey.eventClass
      .equals(CASEPARTICIPANTROLE.INSERT_CASE_PARTICIPANT_ROLE.eventClass)) {
      if (event.eventKey.eventType
        .equals(CASEPARTICIPANTROLE.INSERT_CASE_PARTICIPANT_ROLE.eventType)) {

        // set search parameter to look up list of correspondent from all case
        // participant role
        final CaseParticipantRoleCaseAndTypeKey caseIDTypeCodeKey =
          new CaseParticipantRoleCaseAndTypeKey();
        caseIDTypeCodeKey.caseID = event.secondaryEventData;
        caseIDTypeCodeKey.typeCode = CASEPARTICIPANTROLETYPE.CORRESPONDENT;

        final CaseParticipantRoleNameDetailsList cprList = cprEntityObj
          .searchActiveRoleAndNameByCaseAndType(caseIDTypeCodeKey);

        // loop through all correspondent case participant role
        for (final CaseParticipantRoleNameDetails cpr : cprList.dtls
          .items()) {
          if (cpr.participantRoleID == caseParticipantRoleDtls.participantRoleID)
            return false;
        }
        // when no correspondent case participant role found for the current
        // insert raise the event to create correspondent case participant role
        return true;
      }
    }
    return false;
  }

  @Override
  public void eventRaised(final Event event)
    throws AppException, InformationalException {

    SynchronizeEventsFactory.newInstance();
    // CaseParticipantRole entity object
    final curam.core.sl.entity.intf.CaseParticipantRole cprEntityObj =
      CaseParticipantRoleFactory.newInstance();

    final CaseParticipantRoleKey caseParticipantRoleKey =
      new CaseParticipantRoleKey();
    caseParticipantRoleKey.caseParticipantRoleID = event.primaryEventData;

    // read the current case participant role details
    final CaseParticipantRoleDtls caseParticipantRoleDtls =
      cprEntityObj.read(caseParticipantRoleKey);

    // create the correspondent case participant role record
    final CaseParticipantRoleDtls cprDtls = new CaseParticipantRoleDtls();
    cprDtls.caseID = caseParticipantRoleDtls.caseID;
    cprDtls.participantRoleID = caseParticipantRoleDtls.participantRoleID;
    cprDtls.fromDate = caseParticipantRoleDtls.fromDate;
    cprDtls.typeCode = CASEPARTICIPANTROLETYPE.CORRESPONDENT;

    cprEntityObj.insert(cprDtls);
  }

}
