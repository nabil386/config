package curam.ca.gc.bdm.communication.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.sl.commstatushistory.impl.BDMCommStatusHistoryDAO;
import curam.core.events.CONCERNROLEACOMMUNICATION;
import curam.core.fact.ConcernRoleCommunicationFactory;
import curam.core.intf.ConcernRoleCommunication;
import curam.core.struct.ConcernRoleCommunicationDtls;
import curam.core.struct.ConcernRoleCommunicationKey;
import curam.util.events.impl.EventFilter;
import curam.util.events.impl.EventHandler;
import curam.util.events.struct.Event;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;

public class BDMCommunicationStatusHistoryEventHandler
  implements EventHandler, EventFilter {

  @Inject
  private BDMCommStatusHistoryDAO bdmStatusHistoryDAO;

  public BDMCommunicationStatusHistoryEventHandler() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  /**
   * If event type equals to concern role communication details
   */
  public boolean accept(final Event eventDetails)
    throws AppException, InformationalException {

    if (eventDetails.eventKey.eventType.equals(
      CONCERNROLEACOMMUNICATION.INSERT_CONCERN_ROLE_COMMUNICATION.eventType)) {
      return true;
    }
    return false;
  }

  @Override
  /**
   * Raise event for a communication ID and create communication status history
   */
  public void eventRaised(final Event eventDetails)
    throws AppException, InformationalException {

    if (eventDetails.eventKey.eventType.equals(
      CONCERNROLEACOMMUNICATION.INSERT_CONCERN_ROLE_COMMUNICATION.eventType)) {
      final long communicationID = eventDetails.primaryEventData;
      final ConcernRoleCommunication communicationObj =
        ConcernRoleCommunicationFactory.newInstance();
      final ConcernRoleCommunicationKey crcKey =
        new ConcernRoleCommunicationKey();
      crcKey.communicationID = communicationID;
      final ConcernRoleCommunicationDtls crcDtls =
        communicationObj.read(crcKey);
      bdmStatusHistoryDAO.createCommunicationRecordStatusHistory(
        communicationID, crcDtls.statusCode, crcDtls.userName);
      bdmStatusHistoryDAO.createCommunicationStatusHistory(communicationID,
        crcDtls.communicationStatus, crcDtls.userName);

    }
  }

}
