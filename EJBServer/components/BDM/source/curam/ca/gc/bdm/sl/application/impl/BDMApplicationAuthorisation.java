package curam.ca.gc.bdm.sl.application.impl;

import com.google.inject.Inject;
import com.ibm.icu.util.Calendar;
import curam.ca.gc.bdm.communication.impl.BDMCommunicationImpl;
import curam.ca.gc.bdm.events.BDMNOTIFICATION;
import curam.ca.gc.bdm.facade.bdmcommonintake.fact.BDMApplicationCaseCheckEligibilityFactory;
import curam.ca.gc.bdm.facade.bdmcommonintake.intf.BDMApplicationCaseCheckEligibility;
import curam.ca.gc.bdm.facade.integritycheck.fact.BDMIntegrityCheckFactory;
import curam.ca.gc.bdm.facade.integritycheck.struct.BDMIntegrityCheckKey;
import curam.ca.gc.bdm.notification.impl.BDMNotification;
import curam.ca.gc.bdm.sl.application.struct.BDMVerificationResult;
import curam.ca.gc.bdm.sl.application.struct.CaseCheckResult;
import curam.ca.gc.bdm.sl.application.struct.CaseEvidenceVerificationResult;
import curam.ca.gc.bdm.sl.application.struct.CheckEligibilityResult;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.codetable.VERIFICATIONSTATUS;
import curam.codetable.VERIFICATIONTYPE;
import curam.codetable.impl.CONCERNROLETYPEEntry;
import curam.commonintake.authorisation.impl.ProgramAuthorisation;
import curam.commonintake.codetable.impl.ApplicationCaseEligibilityResultEntry;
import curam.commonintake.entity.struct.ApplicationCaseKey;
import curam.commonintake.impl.ApplicationCase;
import curam.commonintake.impl.ApplicationCaseDAO;
import curam.commonintake.impl.ApplicationCaseEligibilityResult;
import curam.commonintake.impl.ApplicationCaseEligibilityResultDAO;
import curam.core.fact.CaseHeaderFactory;
import curam.core.intf.CaseHeader;
import curam.core.sl.entity.struct.CaseIDStruct;
import curam.core.sl.entity.struct.CaseIDStructList;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.core.sl.infrastructure.entity.struct.ReadRelatedIDParticipantIDAndEvidenceTypeDetails;
import curam.core.sl.struct.CaseIDKey;
import curam.core.sl.struct.ParticipantKeyStruct;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.ConcernRoleID;
import curam.core.struct.ConcernRoleIDList;
import curam.core.struct.ConcernRoleKey;
import curam.piwrapper.casemanager.impl.CaseParticipantRole;
import curam.util.events.impl.EventService;
import curam.util.events.struct.Event;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Trace;
import curam.util.type.CodeTable;
import curam.util.workflow.impl.EnactmentService;
import curam.verification.facade.infrastructure.fact.VerificationApplicationFactory;
import curam.verification.facade.infrastructure.intf.VerificationApplication;
import curam.verification.facade.infrastructure.struct.CaseEvidenceVerificationDisplayDetails;
import curam.verification.facade.infrastructure.struct.CaseEvidenceVerificationDisplayDetailsList;
import curam.verification.sl.infrastructure.entity.fact.VerificationFactory;
import curam.verification.sl.infrastructure.entity.struct.ReadByVerLinkedIDTypeAndStatusKey;
import curam.verification.sl.infrastructure.entity.struct.ReadVerificationStatusLinkedIDandTypeKey;
import curam.verification.sl.infrastructure.entity.struct.VerificationAndVDIEDLinkDetailsList;
import curam.verification.sl.infrastructure.entity.struct.VerificationKey;
import curam.verification.sl.infrastructure.struct.CaseEvidenceVerificationDetails;
import curam.verification.sl.infrastructure.struct.CaseEvidenceVerificationDetailsList;
import curam.verification.sl.infrastructure.struct.OutstandingIndicator;
import curam.workspaceservices.codetable.IntakeProgramApplicationStatus;
import curam.workspaceservices.codetable.impl.IntakeProgramApplicationDenialReasonEntry;
import curam.workspaceservices.intake.fact.IntakeProgramApplicationFactory;
import curam.workspaceservices.intake.impl.IntakeProgramApplication;
import curam.workspaceservices.intake.struct.IntakeProgramApplicationDtls;
import curam.workspaceservices.intake.struct.IntakeProgramApplicationKey;
import java.util.ArrayList;
import java.util.List;

/**
 * The intake application straight through processing authorizes the eligible
 * applications. This class implements the check eligibility that is invoked in
 * the straight through processing to determine the clients eligibility prior to
 * authorizing the application case.
 *
 * This class also checks if the client has an open application case or benefit
 * case created in the same year.
 *
 */
public class BDMApplicationAuthorisation
  extends curam.ca.gc.bdm.sl.application.base.BDMApplicationAuthorisation {

  @Inject
  private ApplicationCaseDAO applicationCaseDAO;

  @Inject
  private ApplicationCaseEligibilityResultDAO applicationCaseEligibilityResultDAO;

  @Inject
  private ProgramAuthorisation programAuthorisation;

  @Inject
  private BDMCommunicationImpl bdmCommunicationImpl;

  @Inject
  private BDMNotification bdmNotification;

  public BDMApplicationAuthorisation() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * This method checks the application case eligibility and returns the
   * eligibility decision based on the results. Returns true if eligible
   * otherwise false.
   * If client is ineligible, all application programs are denied and
   * application is desposed.
   *
   * @param caseIDKey application case identifier
   * @return true if eligible otherwise false
   *
   * @throws AppException
   * Generic Exception Signature
   * @throws InformationalException
   * Generic Exception Signature
   */
  @Override
  public CheckEligibilityResult checkEligibility(final CaseIDKey caseIDKey)
    throws AppException, InformationalException {

    Trace.kTopLevelLogger.info(
      "Intake application straight through processing eligibility check for application case "
        + caseIDKey.caseID);

    final CheckEligibilityResult eligibilityResult =
      new CheckEligibilityResult();

    final BDMApplicationCaseCheckEligibility appCaseCheckEligibility =
      BDMApplicationCaseCheckEligibilityFactory.newInstance();

    final ApplicationCaseKey appCaseKey = new ApplicationCaseKey();
    appCaseKey.applicationCaseID = caseIDKey.caseID;

    // invoke the OOTB check eligibility API
    appCaseCheckEligibility.checkEligibility(appCaseKey);

    final ApplicationCase applicationCase =
      this.applicationCaseDAO.get(Long.valueOf(caseIDKey.caseID));

    // get all application case check eligibility results
    final List<ApplicationCaseEligibilityResult> applicationCaseEligibilityResultList =
      applicationCaseEligibilityResultDAO
        .listAllByApplicationCase(applicationCase);

    // check eligibility decision to set the return struct with result
    for (final ApplicationCaseEligibilityResult applicationCaseEligibilityResult : applicationCaseEligibilityResultList) {

      if (ApplicationCaseEligibilityResultEntry.ELIGIBLE.getCode()
        .equals(applicationCaseEligibilityResult.getDecision().getCode())) {

        eligibilityResult.isEligible = true;
        break;
      }
    }

    // if client is ineligible, deny the application and close application case
    if (!eligibilityResult.isEligible) {

      // BEGIN: TASK 17429 - Straight through processing
      denyApplication(applicationCase,
        IntakeProgramApplicationDenialReasonEntry.CLIENTINELIGIBLE);
      // END: TASK 17429 - Straight through processing

      final Event notificationEvent = new Event();

      notificationEvent.eventKey.eventClass =
        BDMNOTIFICATION.CREATECLAIMDENIEDNOTIFICATION.eventClass;
      notificationEvent.eventKey.eventType =
        BDMNOTIFICATION.CREATECLAIMDENIEDNOTIFICATION.eventType;

      notificationEvent.primaryEventData =
        applicationCase.getConcernRole().getID();
      notificationEvent.secondaryEventData = caseIDKey.caseID;

      EventService.raiseEvent(notificationEvent);
    } else {
      final Event notificationEvent = new Event();

      notificationEvent.eventKey =
        BDMNOTIFICATION.CREATECLAIMESTABLISHEDNOTIFICATION;
      notificationEvent.primaryEventData =
        applicationCase.getConcernRole().getID();
      notificationEvent.secondaryEventData = caseIDKey.caseID;
      EventService.raiseEvent(notificationEvent);
    }

    Trace.kTopLevelLogger.info(
      "Intake application straight through processing eligibility check result: "
        + eligibilityResult.isEligible);

    return eligibilityResult;
  }

  // BEGIN: TASK 17429 - Straight through processing

  /**
   * This method denies the application programs and close the application case.
   *
   * @param applicationCase application case identifier for the current
   * application
   * @throws InformationalException Generic Exception Signature
   * @throws AppException Generic Exception Signature
   */
  private void denyApplication(final ApplicationCase applicationCase,
    final IntakeProgramApplicationDenialReasonEntry denialReason)
    throws InformationalException, AppException {

    // deny all application programs
    for (final IntakeProgramApplication programApplication : applicationCase
      .getPrograms()) {

      final IntakeProgramApplicationDtls applicationDtls =
        readIntakeProgramApplicationRecord(programApplication);

      if (applicationDtls.status
        .equals(IntakeProgramApplicationStatus.PENDING)) {

        programAuthorisation.deny(
          denialReason, programApplication, CodeTable
            .getOneItem(denialReason.getTableName(), denialReason.getCode()),
          programApplication.getVersionNo());

        bdmCommunicationImpl.createCorrespondenceTrigger(
          applicationCase.getID(), applicationDtls, denialReason);
      }
    }
  }

  /**
   * This method denies the application if client has an open application case
   * or benefit case created in the same year. Returns true if client has open
   * application case or benefit case otherwise false.
   *
   * @param caseIDKey application case identifier
   * @return true if client has open case otherwise false
   *
   * @throws InformationalException Generic Exception Signature
   * @throws AppException Generic Exception Signature
   */
  @Override
  public CaseCheckResult hasOpenCase(final CaseIDKey caseIDKey)
    throws AppException, InformationalException {

    final CaseCheckResult caseCheckResult = new CaseCheckResult();
    caseCheckResult.hasOpenCase = false;

    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();

    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = caseIDKey.caseID;

    final CaseHeaderDtls appCaseHeader = caseHeaderObj.read(caseHeaderKey);

    final ApplicationCase applicationCase =
      this.applicationCaseDAO.get(Long.valueOf(caseIDKey.caseID));

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = appCaseHeader.concernRoleID;

    // get all client cases
    final CaseIDStructList caseIDList =
      caseHeaderObj.searchCaseIDByConcernRoleID(concernRoleKey);

    for (final CaseIDStruct caseID : caseIDList.dtls) {

      caseHeaderKey.caseID = caseID.caseID;
      final CaseHeaderDtls caseHeaderDtls = caseHeaderObj.read(caseHeaderKey);

      // return true if client has open application case
      // BEGIN TASK 23597 - Checking for Case type for OPEN.
      if (CASETYPECODE.APPLICATION_CASE.equals(caseHeaderDtls.caseTypeCode)
        && CASESTATUS.OPEN.equals(this.applicationCaseDAO
          .get(Long.valueOf(caseID.caseID)).getStatus().getCode())
        && caseID.caseID != caseIDKey.caseID) {

        caseCheckResult.hasOpenCase = true;
        break;
        // END TASK 23597
      } else if (CASETYPECODE.PRODUCTDELIVERY
        .equals(caseHeaderDtls.caseTypeCode)) {

        // check the start date of PDC, if PDC was created in current year
        // the PDC was created this year so return true
        caseCheckResult.hasOpenCase = applicationCase.getSubmittedDateTime()
          .getCalendar().get(Calendar.YEAR) == caseHeaderDtls.startDate
            .getCalendar().get(Calendar.YEAR) ? true : false;
      }
    }

    // deny the application if client has open application or benefit case
    // with multiple applications denial reason
    if (caseCheckResult.hasOpenCase) {
      denyApplication(applicationCase,
        IntakeProgramApplicationDenialReasonEntry.MULTIPLEAPPLICATIONS);
    }

    return caseCheckResult;
  }
  // END: TASK 17429 - Straight through processing

  // BEGIN - TASK 17774 - Straight through processing
  /**
   * This verification deadline event handler sends the correspondence
   * to the client and dispose the application.
   *
   * @param caseIDKey application case identifier
   * @return deadline expired result to complete the workflow
   *
   * @throws InformationalException Generic Exception Signature
   * @throws AppException Generic Exception Signature
   */
  @Override
  public BDMVerificationResult verificationDeadlineExpired(
    final CaseIDKey caseIDKey) throws AppException, InformationalException {

    // dispose the application case
    final ApplicationCase applicationCase =
      this.applicationCaseDAO.get(Long.valueOf(caseIDKey.caseID));

    denyApplication(applicationCase,
      IntakeProgramApplicationDenialReasonEntry.NOT_SPECIFIED);

    final BDMVerificationResult verificationResult =
      new BDMVerificationResult();

    Trace.kTopLevelLogger.info(
      "Intake application straight through processing - send verification deadline expired correspondence for the application case "
        + caseIDKey.caseID);

    verificationResult.deadlineExpired = true;

    return verificationResult;
  }

  /**
   * This method returns the list of prospect persons for the application case.
   *
   * @param caseIDKey application case identifier
   * @return list of prospect persons for application case
   *
   * @throws InformationalException Generic Exception Signature
   * @throws AppException Generic Exception Signature
   */
  @Override
  public ConcernRoleIDList listApplicationCaseProspectPersons(
    final CaseIDKey caseIDKey) throws AppException, InformationalException {

    final ConcernRoleIDList prospectPersonlist = new ConcernRoleIDList();

    final ApplicationCaseKey appCaseKey = new ApplicationCaseKey();
    appCaseKey.applicationCaseID = caseIDKey.caseID;

    final ApplicationCase applicationCase =
      this.applicationCaseDAO.get(Long.valueOf(caseIDKey.caseID));

    final List<CaseParticipantRole> caseParticipantRoleList =
      applicationCase.listActiveCaseMembers();

    // get all active case members on the application case and checks for the
    // prospect person concern role type
    for (final CaseParticipantRole caseParticipantRole : caseParticipantRoleList) {

      if (!caseParticipantRole.getConcernRole().getConcernRoleType().getCode()
        .equals(CONCERNROLETYPEEntry.PROSPECTPERSON.getCode())) {

        continue;
      }

      // adds all prospect persons to the list to return
      final ConcernRoleID concernRoleID = new ConcernRoleID();
      concernRoleID.concernRoleID =
        caseParticipantRole.getConcernRole().getID();

      prospectPersonlist.dtls.add(concernRoleID);
    }

    return prospectPersonlist;
  }

  /**
   * Performs the SIN/SIR integrity review.
   *
   * @param caseIDKey application case identifier
   *
   * @throws InformationalException Generic Exception Signature
   * @throws AppException Generic Exception Signature
   */
  @Override
  public void checkIntegrity(final CaseIDKey caseIDKey)
    throws AppException, InformationalException {

    final BDMIntegrityCheckKey bdmIntegrityCheckKey =
      new BDMIntegrityCheckKey();
    bdmIntegrityCheckKey.caseID = caseIDKey.caseID;

    BDMIntegrityCheckFactory.newInstance()
      .sinIntegrityCheck(bdmIntegrityCheckKey);
  }

  // END - TASK 17774 - Straight through processing

  /**
   * This method retrieves the list of outstanding verifications for AC and
   * participants.
   *
   * @param caseIDKey application case identifier
   * @return list of outstanding verification Details for AC and participants
   *
   * @throws InformationalException Generic Exception Signature
   * @throws AppException Generic Exception Signature
   */
  @Override
  public CaseEvidenceVerificationResult listOutstandingVerificationDetails(
    final CaseIDKey caseIDKey) throws AppException, InformationalException {

    final CaseEvidenceVerificationResult returnStruct =
      new CaseEvidenceVerificationResult();
    CaseEvidenceVerificationDisplayDetails caseEvidenceVerificationDetailsRe;

    final VerificationApplication verificationApplicationObj =
      VerificationApplicationFactory.newInstance();
    final curam.core.struct.CaseKey caseKey = new curam.core.struct.CaseKey();
    caseKey.assign(caseIDKey);
    final CaseEvidenceVerificationDisplayDetailsList CaseEvidenceVerificationDisplayDetailsList =
      verificationApplicationObj
        .listOutstandingVerificationDetailsForCaseEvidence(caseKey);

    final OutstandingIndicator outstandingIndicator =
      new OutstandingIndicator();
    final ParticipantKeyStruct participantKeyStruct =
      new ParticipantKeyStruct();

    outstandingIndicator.verificationStatus = VERIFICATIONSTATUS.NOTVERIFIED;

    for (final CaseEvidenceVerificationDisplayDetails verificationDisplayDetails : CaseEvidenceVerificationDisplayDetailsList.dtls) {
      caseEvidenceVerificationDetailsRe =
        new CaseEvidenceVerificationDisplayDetails();
      if (verificationDisplayDetails.verificationID == 0L) {
        participantKeyStruct.participantID =
          verificationDisplayDetails.concernRoleID;
        final CaseEvidenceVerificationDetailsList caseEvidenceVerificationDetailsList =
          this.listAllParticipantVerificationDetails(participantKeyStruct,
            outstandingIndicator);
        for (final CaseEvidenceVerificationDetails caseEvidenceVerificationDetails : caseEvidenceVerificationDetailsList.dtls) {
          if (caseEvidenceVerificationDetails.vDIEDLinkID == verificationDisplayDetails.vDIEDLinkID
            && caseEvidenceVerificationDetails.evidenceDescriptorID == verificationDisplayDetails.evidenceDescriptorID) {
            verificationDisplayDetails.verificationID =
              caseEvidenceVerificationDetails.verificationID;
          }
        }
      }
      caseEvidenceVerificationDetailsRe.assign(verificationDisplayDetails);
      returnStruct.dtls.add(caseEvidenceVerificationDetailsRe);
    }

    if (returnStruct.dtls.size() > 0) {
      returnStruct.hasMoreEvidenceToVerify = true;
    }

    return returnStruct;
  }

  /**
   * This method retrieves the list of outstanding verifications for
   * participants.
   * This is to fix the defect in OOTB method
   * curam.verification.sl.infrastructure.impl.Verification.listAllParticipantVerificationDetails
   * where the verificationID is not set.
   *
   * @param key participant identifier
   * @return list of outstanding verification Details for the participants
   */
  private CaseEvidenceVerificationDetailsList
    listAllParticipantVerificationDetails(final ParticipantKeyStruct key,
      final OutstandingIndicator indicator)
      throws AppException, InformationalException {

    final curam.verification.sl.infrastructure.entity.intf.Verification verification =
      VerificationFactory.newInstance();

    // EvidenceDescriptor variables
    final EvidenceDescriptor evidenceDescriptor =
      EvidenceDescriptorFactory.newInstance();

    final CaseEvidenceVerificationDetailsList caseEvidenceVerificationDetailsList =
      new CaseEvidenceVerificationDetailsList();

    CaseEvidenceVerificationDetails caseEvidenceVerificationDetails;
    final EvidenceDescriptorKey evidenceDescriptorKey =
      new EvidenceDescriptorKey();
    ReadRelatedIDParticipantIDAndEvidenceTypeDetails readRelatedIDParticipantIDAndEvidenceTypeDetails =
      new ReadRelatedIDParticipantIDAndEvidenceTypeDetails();

    VerificationAndVDIEDLinkDetailsList verificationAndVDIEDLinkDetailsList;
    final ReadVerificationStatusLinkedIDandTypeKey readVerificationStatusLinkedIDandTypeKey =
      new ReadVerificationStatusLinkedIDandTypeKey();

    final ReadByVerLinkedIDTypeAndStatusKey readByVerLinkedIDTypeAndStatusKey =
      new ReadByVerLinkedIDTypeAndStatusKey();

    readByVerLinkedIDTypeAndStatusKey.verificationLinkedID =
      key.participantID;
    readByVerLinkedIDTypeAndStatusKey.evidenceDescriptorStatus1 =
      EVIDENCEDESCRIPTORSTATUS.ACTIVE;
    readByVerLinkedIDTypeAndStatusKey.evidenceDescriptorStatus2 =
      EVIDENCEDESCRIPTORSTATUS.INEDIT;
    readByVerLinkedIDTypeAndStatusKey.verificationLinkedType =
      VERIFICATIONTYPE.NONCASEDATA;

    readVerificationStatusLinkedIDandTypeKey.verificationLinkedID =
      key.participantID;
    readVerificationStatusLinkedIDandTypeKey.verificationStatus =
      indicator.verificationStatus;
    readVerificationStatusLinkedIDandTypeKey.evidenceDescriptorStatus1 =
      EVIDENCEDESCRIPTORSTATUS.ACTIVE;
    readVerificationStatusLinkedIDandTypeKey.evidenceDescriptorStatus2 =
      EVIDENCEDESCRIPTORSTATUS.INEDIT;

    readVerificationStatusLinkedIDandTypeKey.verificationLinkedType =
      VERIFICATIONTYPE.NONCASEDATA;
    verificationAndVDIEDLinkDetailsList =
      verification.searchOutstandingVerAndVDIEDLinkDetailsByLinkedIDandType(
        readVerificationStatusLinkedIDandTypeKey);

    for (int i = 0; i < verificationAndVDIEDLinkDetailsList.dtls
      .size(); i++) {
      evidenceDescriptorKey.evidenceDescriptorID =
        verificationAndVDIEDLinkDetailsList.dtls.item(i).evidenceDescriptorID;

      readRelatedIDParticipantIDAndEvidenceTypeDetails = evidenceDescriptor
        .readRelatedIDParticipantIDAndEvidenceType(evidenceDescriptorKey);

      caseEvidenceVerificationDetails = new CaseEvidenceVerificationDetails();
      caseEvidenceVerificationDetails.evidenceTypeOpt =
        readRelatedIDParticipantIDAndEvidenceTypeDetails.evidenceType;

      caseEvidenceVerificationDetails.vDIEDLinkID =
        verificationAndVDIEDLinkDetailsList.dtls.item(i).VDIEDLinkID;
      caseEvidenceVerificationDetails.evidenceDescriptorID =
        verificationAndVDIEDLinkDetailsList.dtls.item(i).evidenceDescriptorID;

      caseEvidenceVerificationDetails.mandatory =
        verificationAndVDIEDLinkDetailsList.dtls.item(i).mandatory;

      caseEvidenceVerificationDetails.verificationID =
        verificationAndVDIEDLinkDetailsList.dtls.item(i).verificationID;
      // since status has been updated fetch it again
      final VerificationKey verificationKey = new VerificationKey();

      verificationKey.verificationID =
        verificationAndVDIEDLinkDetailsList.dtls.item(i).verificationID;
      caseEvidenceVerificationDetails.verificationStatus = verification
        .readVerificationStatus(verificationKey).verificationStatus;

      if (!caseEvidenceVerificationDetails.verificationStatus
        .equals(VERIFICATIONSTATUS.CANCELLED)) {
        caseEvidenceVerificationDetailsList.dtls
          .addRef(caseEvidenceVerificationDetails);
      }
    }
    return caseEvidenceVerificationDetailsList;

  }

  // BEGIN TASK 19667 Authorisation failure task
  /**
   * This method enacts the benefit failure authorization task workflow to
   * creates an authorization failure task when clients benefits authorization
   * fails.
   *
   * @param caseIDKey application case identifier
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  public void createAuthorisationFailedTask(final CaseIDKey caseIDKey)
    throws AppException, InformationalException {

    final List<Object> enactmentStructs = new ArrayList<>();
    enactmentStructs.add(caseIDKey);

    EnactmentService.startProcess("BDMAuthorisationFailureTaskWorkflow",
      enactmentStructs);
  }
  // END TASK 19667 Authorisation failure task

  /**
   * This method returns the IntakeProgramApplication Record.
   *
   * @param intakeProgramApplication
   * @return
   */
  private IntakeProgramApplicationDtls readIntakeProgramApplicationRecord(
    final IntakeProgramApplication intakeProgramApplication) {

    final curam.workspaceservices.intake.intf.IntakeProgramApplication programApplicaton =
      IntakeProgramApplicationFactory.newInstance();
    final IntakeProgramApplicationKey programApplicationKey =
      new IntakeProgramApplicationKey();
    programApplicationKey.intakeProgramApplicationID =
      intakeProgramApplication.getID();

    IntakeProgramApplicationDtls intakeProgramApplicationDtls = null;
    try {
      intakeProgramApplicationDtls =
        programApplicaton.read(programApplicationKey);
    } catch (final AppException e) {
      e.printStackTrace();
    } catch (final InformationalException e) {
      e.printStackTrace();
    }

    return intakeProgramApplicationDtls;

  }
}
