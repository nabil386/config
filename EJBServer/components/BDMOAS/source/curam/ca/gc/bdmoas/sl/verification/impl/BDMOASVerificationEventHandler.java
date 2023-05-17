package curam.ca.gc.bdmoas.sl.verification.impl;

import curam.ca.gc.bdmoas.util.impl.BDMOASTaskUtil;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASETYPECODE;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.events.Verification;
import curam.util.events.impl.EventFilter;
import curam.util.events.impl.EventHandler;
import curam.util.events.struct.Event;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.verification.sl.infrastructure.entity.fact.VDIEDLinkFactory;
import curam.verification.sl.infrastructure.entity.fact.VerificationFactory;
import curam.verification.sl.infrastructure.entity.struct.VDIEDLinkDtls;
import curam.verification.sl.infrastructure.entity.struct.VDIEDLinkKey;
import curam.verification.sl.infrastructure.entity.struct.VerificationDtls;
import curam.verification.sl.infrastructure.entity.struct.VerificationKey;
import java.util.ArrayList;

/**
 * BDM OAS Component Verification Event Handler class
 */
public class BDMOASVerificationEventHandler
  implements EventHandler, EventFilter {

  /**
   * Accept.
   *
   * @param event the event
   * @return true, if successful
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Override
  public boolean accept(final Event event)
    throws AppException, InformationalException {

    boolean accept = false;
    if (Verification.VerificationAdded.eventClass
      .equals(event.eventKey.eventClass)
      && Verification.VerificationAdded.eventType
        .equals(event.eventKey.eventType)
      || Verification.EvidenceDeleted.eventClass
        .equals(event.eventKey.eventClass)
        && Verification.EvidenceDeleted.eventType
          .equals(event.eventKey.eventType)
      || Verification.VerificationSatisified.eventClass
        .equals(event.eventKey.eventClass)
        && Verification.VerificationSatisified.eventType
          .equals(event.eventKey.eventType)) {
      accept = true;
    }

    return accept;
  }

  /**
   * Event raised based on events are created
   *
   * @param event the event
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Override
  public void eventRaised(final Event event)
    throws AppException, InformationalException {

    if (Verification.VerificationAdded.eventType
      .equals(event.eventKey.eventType)) {
      maintainVerificationAdded(event.primaryEventData,
        event.secondaryEventData);
    } else if (Verification.VerificationSatisified.eventType
      .equals(event.eventKey.eventType)) {
      this.maintainVerificationSatisfied(event.primaryEventData,
        event.secondaryEventData);
    } else if (Verification.EvidenceDeleted.eventType
      .equals(event.eventKey.eventType)) {
      this.maintainEvidenceDeleted(event.primaryEventData,
        event.secondaryEventData);
    }

  }

  /**
   * If add verification Event called , Task created if open task not available
   * .
   *
   * @param priID the pri ID
   * @param secID the sec ID
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  public void maintainVerificationAdded(final long priID, final long secID)
    throws AppException, InformationalException {

    final EIEvidenceKey evidenceKey = new EIEvidenceKey();
    final VerificationKey verifcationKey = new VerificationKey();
    verifcationKey.verificationID = priID;
    final VerificationDtls evidenceDetails =
      VerificationFactory.newInstance().read(verifcationKey);
    final VDIEDLinkKey vdIeLink = new VDIEDLinkKey();
    vdIeLink.VDIEDLinkID = evidenceDetails.VDIEDLinkID;
    final VDIEDLinkDtls vdlinkDtils =
      VDIEDLinkFactory.newInstance().read(vdIeLink);
    final EvidenceDescriptorKey evidenceDescriptorKey =
      new EvidenceDescriptorKey();
    evidenceDescriptorKey.evidenceDescriptorID =
      vdlinkDtils.evidenceDescriptorID;
    final EvidenceDescriptorDtls evidenceDescriptorDtls =
      EvidenceDescriptorFactory.newInstance().read(evidenceDescriptorKey);
    evidenceKey.evidenceID = evidenceDescriptorDtls.relatedID;
    evidenceKey.evidenceType = evidenceDescriptorDtls.evidenceType;
    final BDMOASTaskUtil bdmoasTaskUtil = new BDMOASTaskUtil();
    if (CASEEVIDENCE.OAS_LEGAL_STATUS
      .equals(evidenceDescriptorDtls.evidenceType)
      || CASEEVIDENCE.OAS_MARITAL_RELATIONSHIP
        .equals(evidenceDescriptorDtls.evidenceType)
      || CASEEVIDENCE.OAS_LIVING_APART_FOR_REASONS_BEYOND_CONTROL
        .equals(evidenceDescriptorDtls.evidenceType)) {
      // checking if any existing task are present
      final ArrayList<Long> taskList =
        bdmoasTaskUtil.checkIfOutstandingVerTaskAlreadyExistForCase(
          evidenceDescriptorDtls.caseID, evidenceDescriptorDtls.evidenceType,
          CASETYPECODE.INTEGRATEDCASE);

      for (final Long task : taskList)
        bdmoasTaskUtil.raiseTaskCloseEvent(task);

      // Task created
      bdmoasTaskUtil.createOutstandingVerificationTaskForCase(
        evidenceDescriptorDtls.caseID, evidenceKey);
    }
    if (CASEEVIDENCE.BDM_MARITAL_STATUS
      .equals(evidenceDescriptorDtls.evidenceType)) {
      final ArrayList<Long> taskList =
        bdmoasTaskUtil.checkIfOutstandingVerTaskAlreadyExistForCase(
          evidenceDescriptorDtls.participantID,
          evidenceDescriptorDtls.evidenceType,
          CASETYPECODE.PARTICIPANTDATACASE);
      for (final Long task : taskList)
        bdmoasTaskUtil.raiseTaskCloseEvent(task);
      // Task created
      bdmoasTaskUtil.createOutstandingVerificationTaskForPerson(
        evidenceDescriptorDtls.participantID, evidenceKey);
    }

  }

  /**
   * If verification completed ,Verification Satisfied Event will call and close
   * any outstanding task are present and attached to that evidence
   *
   * @param priID the pri ID
   * @param secID the sec ID
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  public void maintainVerificationSatisfied(final long priID,
    final long secID) throws AppException, InformationalException {

    final VerificationKey verifcationKey = new VerificationKey();

    verifcationKey.verificationID = priID;
    final VerificationDtls evidenceDetails =
      VerificationFactory.newInstance().read(verifcationKey);
    final VDIEDLinkKey vdIeLink = new VDIEDLinkKey();
    vdIeLink.VDIEDLinkID = evidenceDetails.VDIEDLinkID;
    final VDIEDLinkDtls vdlinkDtils =
      VDIEDLinkFactory.newInstance().read(vdIeLink);
    final EvidenceDescriptorKey evidenceDescriptorKey =
      new EvidenceDescriptorKey();
    evidenceDescriptorKey.evidenceDescriptorID =
      vdlinkDtils.evidenceDescriptorID;
    final EvidenceDescriptorDtls evidenceDescriptorDtls =
      EvidenceDescriptorFactory.newInstance().read(evidenceDescriptorKey);
    final BDMOASTaskUtil bdmoasTaskUtil = new BDMOASTaskUtil();
    if (CASEEVIDENCE.OAS_LEGAL_STATUS
      .equals(evidenceDescriptorDtls.evidenceType)
      || CASEEVIDENCE.BDM_MARITAL_STATUS
        .equals(evidenceDescriptorDtls.evidenceType)
      || CASEEVIDENCE.OAS_MARITAL_RELATIONSHIP
        .equals(evidenceDescriptorDtls.evidenceType)
      || CASEEVIDENCE.OAS_LIVING_APART_FOR_REASONS_BEYOND_CONTROL
        .equals(evidenceDescriptorDtls.evidenceType)) {
      final Long taskID = bdmoasTaskUtil
        .getTaskIdFromEvidenceID(evidenceDescriptorDtls.relatedID);
      if (taskID != 0l)
        bdmoasTaskUtil.raiseTaskCloseEvent(taskID);
    }

  }

  /**
   * If Evidence Deleted , verification delete Event will call and close any
   * outstanding task are present and attached to that evidence
   *
   * @param priID the pri ID
   * @param secID the sec ID
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  public void maintainEvidenceDeleted(final long priID, final long secID)
    throws AppException, InformationalException {

    final EvidenceDescriptorKey evidenceDescriptorKey =
      new EvidenceDescriptorKey();
    evidenceDescriptorKey.evidenceDescriptorID = priID;
    final EvidenceDescriptorDtls evidenceDescriptorDtls =
      EvidenceDescriptorFactory.newInstance().read(evidenceDescriptorKey);
    final BDMOASTaskUtil bdmoasTaskUtil = new BDMOASTaskUtil();
    if (CASEEVIDENCE.OAS_LEGAL_STATUS
      .equals(evidenceDescriptorDtls.evidenceType)
      || CASEEVIDENCE.BDM_MARITAL_STATUS
        .equals(evidenceDescriptorDtls.evidenceType)
      || CASEEVIDENCE.OAS_MARITAL_RELATIONSHIP
        .equals(evidenceDescriptorDtls.evidenceType)
      || CASEEVIDENCE.OAS_LIVING_APART_FOR_REASONS_BEYOND_CONTROL
        .equals(evidenceDescriptorDtls.evidenceType)) {
      final Long taskID = bdmoasTaskUtil
        .getTaskIdFromEvidenceID(evidenceDescriptorDtls.relatedID);
      if (taskID != 0l)
        bdmoasTaskUtil.raiseTaskCloseEvent(taskID);
    }

  }

}
