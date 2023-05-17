package curam.ca.gc.bdm.communication.impl;

import curam.ca.gc.bdm.codetable.BDMFOREIGNAPPSTATUS;
import curam.ca.gc.bdm.codetable.BDMFOREIGNAPPTYPE;
import curam.ca.gc.bdm.entity.fact.BDMForeignApplicationFactory;
import curam.ca.gc.bdm.entity.fec.fact.BDMFECaseFactory;
import curam.ca.gc.bdm.entity.fec.intf.BDMFECase;
import curam.ca.gc.bdm.entity.fec.struct.BDMConcernRoleCommRMKey;
import curam.ca.gc.bdm.entity.fec.struct.BDMRecordCommunicationsTaskDetails;
import curam.ca.gc.bdm.entity.fec.struct.BDMRecordCommunicationsTaskDetailsList;
import curam.ca.gc.bdm.entity.intf.BDMForeignApplication;
import curam.ca.gc.bdm.entity.struct.BDMFAKeyStruct3;
import curam.ca.gc.bdm.entity.struct.BDMForeignApplicationDtls;
import curam.ca.gc.bdm.entity.struct.BDMForeignApplicationDtlsList;
import curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey;
import curam.ca.gc.bdm.events.BDMINTERIMAPPCOMMUNICATION;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.core.fact.ConcernRoleCommunicationFactory;
import curam.core.impl.CuramConst;
import curam.core.intf.ConcernRoleCommunication;
import curam.core.struct.ConcernRoleCommunicationDtls;
import curam.core.struct.ConcernRoleCommunicationKey;
import curam.events.TASK;
import curam.util.events.impl.EventFilter;
import curam.util.events.impl.EventHandler;
import curam.util.events.impl.EventService;
import curam.util.events.struct.Event;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMCommunicationEventHandler
  implements EventHandler, EventFilter {

  @Override
  /**
   * This method will accept only if the event is related to
   * Interim Application
   *
   */
  public boolean accept(final Event event)
    throws AppException, InformationalException {

    // check if the event is the BDMInterimAppCommunication event class
    if (event.eventKey.eventClass
      .equals(BDMINTERIMAPPCOMMUNICATION.COMMUNICATIONMARKEDSENT.eventClass))
      return true;

    return false;
  }

  @Override
  /**
   * Once event is raised, use the primaryEventData communicationID for
   * further processing.
   */
  public void eventRaised(final Event event)
    throws AppException, InformationalException {

    final long communicationID = event.primaryEventData;
    final ConcernRoleCommunication communicationObj =
      ConcernRoleCommunicationFactory.newInstance();
    final ConcernRoleCommunicationKey crcKey =
      new ConcernRoleCommunicationKey();
    crcKey.communicationID = communicationID;
    final ConcernRoleCommunicationDtls crcDtls =
      communicationObj.read(crcKey);

    final BDMFECase bdmfeCase = BDMFECaseFactory.newInstance();
    final BDMConcernRoleCommRMKey bdmCncrCmmRmKey =
      new BDMConcernRoleCommRMKey();
    bdmCncrCmmRmKey.wdoName = BDMConstants.kBdmTaskCreateDetails;
    bdmCncrCmmRmKey.caseID = crcDtls.caseID;
    bdmCncrCmmRmKey.concernRoleID = crcDtls.correspondentConcernRoleID;
    bdmCncrCmmRmKey.communicationTypeCode = crcDtls.typeCode;

    final BDMRecordCommunicationsTaskDetailsList bdmRecordCommunicationsTaskDetailsList =
      bdmfeCase.getFECaseListOfTaskForCommunication(bdmCncrCmmRmKey);

    for (final BDMRecordCommunicationsTaskDetails bdmRecordCommunicationsTaskDetails : bdmRecordCommunicationsTaskDetailsList.dtls) {
      if (bdmRecordCommunicationsTaskDetails.taskID != CuramConst.gkZero) {
        final Event closeTaskEvent = new Event();
        closeTaskEvent.eventKey.eventClass = TASK.CLOSED.eventClass;
        closeTaskEvent.eventKey.eventType = TASK.CLOSED.eventType;
        closeTaskEvent.primaryEventData =
          bdmRecordCommunicationsTaskDetails.taskID;
        EventService.raiseEvent(closeTaskEvent);
      }
    }

    // START: Bug 99144: foreign application with type interim - status does not
    // change when correspondence is created

    final Event completeIterimFA = new Event();

    completeIterimFA.primaryEventData = communicationID;

    completeIterimFA.eventKey.eventClass =
      BDMINTERIMAPPCOMMUNICATION.COMMUNICATIONMARKEDSENT.eventClass;

    if (accept(completeIterimFA)) {

      BDMForeignApplication bdmForeignApplication =
        BDMForeignApplicationFactory.newInstance();

      BDMFAKeyStruct3 bdmfaKeyStruct3 = new BDMFAKeyStruct3();
      bdmfaKeyStruct3.caseID = crcDtls.caseID;

      BDMForeignApplicationDtlsList bdmForeignApplicationDtlsList =
        bdmForeignApplication.readByCaseID(bdmfaKeyStruct3);

      BDMForeignApplicationKey bdmForeignApplicationKey = null;

      for (BDMForeignApplicationDtls bdmForeignApplicationDtls : bdmForeignApplicationDtlsList.dtls) {

        bdmForeignApplicationKey = new BDMForeignApplicationKey();

        bdmForeignApplicationKey.fApplicationID =
          bdmForeignApplicationDtls.fApplicationID;

        if (bdmForeignApplicationDtls.typeCode
          .equals(BDMFOREIGNAPPTYPE.FOREIGN_BEN_INTERIM)) {

          bdmForeignApplicationDtls.status = BDMFOREIGNAPPSTATUS.COMPLETED;

          bdmForeignApplication.modify(bdmForeignApplicationKey,
            bdmForeignApplicationDtls);

        }

      }

    }

    // END: Bug 99144

  }

}
