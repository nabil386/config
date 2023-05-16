package curam.ca.gc.bdmoas.facade.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMSIGNATURETYPE;
import curam.ca.gc.bdm.entity.fact.BDMTaskClassificationFactory;
import curam.ca.gc.bdm.entity.intf.BDMTaskClassification;
import curam.ca.gc.bdm.entity.struct.BDMProcessDefinitionDetails;
import curam.ca.gc.bdm.entity.struct.BDMProcessDefinitionDetailsList;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.facade.verifications.fact.BDMVerificationApplicationFactory;
import curam.ca.gc.bdmoas.facade.struct.BDMOASCloseAllBenefitsDetails;
import curam.ca.gc.bdmoas.impl.BDMOASConstants;
import curam.ca.gc.bdmoas.message.impl.BDMOASAPPLICATIONCASEExceptionCreator;
import curam.casetasks.impl.CaseTasks;
import curam.casetasks.impl.TaskDetails;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASEPARTICIPANTROLETYPE;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.CONCERNROLETYPE;
import curam.codetable.TASKSTATUS;
import curam.commonintake.authorisation.entity.struct.ProgramDenialDtls;
import curam.commonintake.authorisation.facade.fact.ProgramAuthorisationFactory;
import curam.commonintake.authorisation.facade.intf.ProgramAuthorisation;
import curam.commonintake.authorisation.facade.struct.AuthorisationDetails;
import curam.commonintake.authorisation.facade.struct.DenyProgramDetails;
import curam.commonintake.impl.ApplicationCase;
import curam.commonintake.impl.ApplicationCaseDAO;
import curam.core.facade.infrastructure.fact.EvidenceFactory;
import curam.core.facade.infrastructure.struct.CaseKey;
import curam.core.facade.infrastructure.struct.EvidenceIssuesDetailsList;
import curam.core.facade.infrastructure.struct.EvidenceParticipantDtls;
import curam.core.fact.CaseHeaderFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.sl.entity.fact.CaseParticipantRoleFactory;
import curam.core.sl.entity.struct.CaseIDStruct;
import curam.core.sl.entity.struct.CaseIDStructList;
import curam.core.sl.entity.struct.CaseParticipantRoleDtls;
import curam.core.sl.entity.struct.CaseParticipantRole_eoCaseIDKey;
import curam.core.sl.entity.struct.TaskKey;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.ConcernRoleKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.participant.impl.ConcernRoleDAO;
import curam.piwrapper.caseheader.impl.CaseHeader;
import curam.piwrapper.caseheader.impl.CaseHeaderDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.persistence.ValidationHelper;
import curam.util.resources.Configuration;
import curam.verification.facade.infrastructure.struct.CaseEvidenceVerificationDisplayDetails;
import curam.workspaceservices.codetable.impl.IntakeProgramApplicationStatusEntry;
import curam.workspaceservices.intake.impl.IntakeProgramApplication;
import curam.workspaceservices.intake.impl.IntakeProgramApplicationDAO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author naveen.garg
 *
 * Customize OOTB class for authorize and Close All benefits functionality
 */
public class BDMOASProgramAuthorisation
  extends curam.ca.gc.bdmoas.facade.base.BDMOASProgramAuthorisation {

  public BDMOASProgramAuthorisation() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Inject
  private CaseHeaderDAO caseHeaderDAO;

  @Inject
  private IntakeProgramApplicationDAO intakeProgramApplicationDAO;

  @Inject
  ConcernRoleDAO concernRoleDAO;

  @Inject
  private CaseTasks caseTasks;

  @Inject
  private ApplicationCaseDAO applicationCaseDAO;

  /**
   *
   * @description: Task 94704 - Authorize an OAS Program application. Add
   * business validations
   * @param :@param key
   * @param :@return
   * @param :@throws AppException
   * @param :@throws InformationalException
   * @throws
   */
  @Override
  public void authorise(final AuthorisationDetails key)
    throws AppException, InformationalException {

    final ProgramAuthorisation programAuthorisation =
      ProgramAuthorisationFactory.newInstance();

    final IntakeProgramApplication intakeProgramApplication =
      this.intakeProgramApplicationDAO.get(key.intakeProgramApplicationID);

    if (BDMOASConstants.OAS_PROGRM_TYP_REF.equals(
      intakeProgramApplication.getProgramType().getProgramTypeReference())) {
      validateOASBenefitType(key);
    } else {

      final List<CaseParticipantRoleDtls> primaryPtcptRoleIDList =
        getCaseParticipantByType(key.applicationCaseID,
          CASEPARTICIPANTROLETYPE.PRIMARY);

      if (!isConsentEvidencePresent(key.applicationCaseID)) {
        ValidationHelper
          .addValidationError(BDMOASAPPLICATIONCASEExceptionCreator
            .ERR_CONSENT_EVIDENCE_MISSING());
      }
      if (isEvidenceIssue(key.applicationCaseID)) {
        ValidationHelper.addValidationError(
          BDMOASAPPLICATIONCASEExceptionCreator.ERR_EVIDENCE_ISSUE());
      }

      if (isMandatoryVerificationExists(key.applicationCaseID)) {

        ValidationHelper.addValidationError(
          BDMOASAPPLICATIONCASEExceptionCreator.ERR_VERIFICATION_ISSUE());
      }

      key.primaryClientID = primaryPtcptRoleIDList.isEmpty() ? 0
        : primaryPtcptRoleIDList.get(0).participantRoleID;

      final long memberClientId = primaryPtcptRoleIDList.isEmpty() ? 0
        : primaryPtcptRoleIDList.get(0).participantRoleID;

      if (isProspectPerson(key.primaryClientID)
        || isProspectPerson(memberClientId)) {
        ValidationHelper
          .addValidationError(BDMOASAPPLICATIONCASEExceptionCreator
            .ERR_GIS_PROSPECT_PERSON_ISSUE());
      }

      if (ifOtherOpenOASCase(key.primaryClientID, key.applicationCaseID,
        BDMOASConstants.GIS_PROGRM_TYP_REF, CASEPARTICIPANTROLETYPE.PRIMARY,
        false)
        || ifOtherOpenOASCase(key.primaryClientID, key.applicationCaseID,
          BDMOASConstants.GIS_PROGRM_TYP_REF, CASEPARTICIPANTROLETYPE.MEMBER,
          false)) {
        ValidationHelper.addValidationError(
          BDMOASAPPLICATIONCASEExceptionCreator.ERR_DUPLICATE_APPLICATION());
      }

      if (ifOtherOpenOASCase(memberClientId, key.applicationCaseID,
        BDMOASConstants.GIS_PROGRM_TYP_REF, CASEPARTICIPANTROLETYPE.PRIMARY,
        false)
        || ifOtherOpenOASCase(memberClientId, key.applicationCaseID,
          BDMOASConstants.GIS_PROGRM_TYP_REF, CASEPARTICIPANTROLETYPE.MEMBER,
          false)) {
        ValidationHelper.addValidationError(
          BDMOASAPPLICATIONCASEExceptionCreator.ERR_DUPLICATE_APPLICATION());
      }

      if (ifOtherOpenOASCase(memberClientId, key.applicationCaseID,
        BDMOASConstants.OAS_PROGRM_TYP_REF, CASEPARTICIPANTROLETYPE.PRIMARY,
        true)
        || ifOtherOpenOASCase(memberClientId, key.applicationCaseID,
          BDMOASConstants.OAS_PROGRM_TYP_REF, CASEPARTICIPANTROLETYPE.MEMBER,
          true)) {
        ValidationHelper.addValidationError(
          BDMOASAPPLICATIONCASEExceptionCreator.ERR_GIS_OAS_ISSUE());
      }

      if (ifOpenTaskExists(key.applicationCaseID)) {
        ValidationHelper.addValidationError(
          BDMOASAPPLICATIONCASEExceptionCreator.ERR_GISTASK_ISSUE());

      }
    }

    // BEGIN Bug 110191
    ValidationHelper.failIfErrorsExist();
    // END Bug 110191

    programAuthorisation.authorise(key);
  }

  /**
   * Method to validate OAS Business Rules
   *
   * @param key
   * @throws AppException
   * @throws InformationalException
   */
  private void validateOASBenefitType(final AuthorisationDetails key)
    throws AppException, InformationalException {

    final List<CaseParticipantRoleDtls> memberCaseParticipantRoleDtlsList =
      getCaseParticipantByType(key.applicationCaseID,
        CASEPARTICIPANTROLETYPE.PRIMARY);

    if (!isConsentEvidencePresent(key.applicationCaseID)) {
      ValidationHelper.addValidationError(
        BDMOASAPPLICATIONCASEExceptionCreator.ERR_CONSENT_EVIDENCE_MISSING());
    }
    if (isEvidenceIssue(key.applicationCaseID)) {
      ValidationHelper.addValidationError(
        BDMOASAPPLICATIONCASEExceptionCreator.ERR_EVIDENCE_ISSUE());
    }

    if (isMandatoryVerificationExists(key.applicationCaseID)) {
      ValidationHelper.addValidationError(
        BDMOASAPPLICATIONCASEExceptionCreator.ERR_VERIFICATION_ISSUE());
    }

    key.primaryClientID = memberCaseParticipantRoleDtlsList.isEmpty() ? 0
      : memberCaseParticipantRoleDtlsList.get(0).participantRoleID;

    if (isProspectPerson(key.primaryClientID)) {
      ValidationHelper.addValidationError(
        BDMOASAPPLICATIONCASEExceptionCreator.ERR_PROSPECT_PERSON_ISSUE());
    }

    if (ifOtherOpenOASCase(key.primaryClientID, key.applicationCaseID,
      BDMOASConstants.OAS_PROGRM_TYP_REF, CASEPARTICIPANTROLETYPE.PRIMARY,
      false)) {
      ValidationHelper.addValidationError(
        BDMOASAPPLICATIONCASEExceptionCreator.ERR_DUPLICATE_APPLICATION());
    }

    if (checkOnlyBenefitPending(key.applicationCaseID,
      key.intakeProgramApplicationID)
      && ifOpenTaskExists(key.applicationCaseID)) {
      ValidationHelper.addValidationError(
        BDMOASAPPLICATIONCASEExceptionCreator.ERR_OPEN_TASK());

    }
    if (!checkOnlyBenefitPending(key.applicationCaseID,
      key.intakeProgramApplicationID)
      && ifMandatoryTaskOpen(key.applicationCaseID, Configuration.getProperty(
        EnvVars.BDMOAS_APPLICATIONCASE_BENEFIT_AUTHORIZE_MANDATORYTASK))) {
      ValidationHelper.addValidationError(
        BDMOASAPPLICATIONCASEExceptionCreator.ERR_TASK_ISSUE());

    }
  }

  /**
   * Method to check if input benefit is the only benefit in pending state for
   * this application case
   *
   * @param caseId
   * @param intakeProgramId
   * @return
   */
  private boolean checkOnlyBenefitPending(final long caseId,
    final long intakeProgramId) {

    final curam.commonintake.impl.ApplicationCase applicationCase =
      this.applicationCaseDAO.get(caseId);

    final Iterator intakeProgAppIterator =
      applicationCase.getPrograms().iterator();
    while (intakeProgAppIterator.hasNext()) {
      final IntakeProgramApplication program =
        (IntakeProgramApplication) intakeProgAppIterator.next();

      if (program.getLifecycleState()
        .equals(IntakeProgramApplicationStatusEntry.PENDING)) {
        if (intakeProgramId != program.getID()) {
          return false;
        }

      }

    }
    return true;

  }

  /**
   * Method to check if Tasks # 7, 11, 15 and 50 are in Open status
   *
   * @param caseId
   * @return
   */
  private boolean ifMandatoryTaskOpen(final long caseId,
    final String mandatoryTaskProceeName)
    throws AppException, InformationalException {

    final CaseHeader caseHeader = this.caseHeaderDAO.get(caseId);
    final BDMTaskClassification taskClassification =
      BDMTaskClassificationFactory.newInstance();

    final Iterator taskDetailsIterator =
      this.caseTasks.readAllCaseTasks(caseHeader).iterator();
    while (taskDetailsIterator.hasNext()) {
      final TaskDetails taskDetails =
        (TaskDetails) taskDetailsIterator.next();

      final TaskKey taskKey = new TaskKey();
      taskKey.taskID = taskDetails.getTaskID();
      final BDMProcessDefinitionDetailsList processDefinitionDetailsList =
        taskClassification.getProcessFromTaskId(taskKey);

      final List<BDMProcessDefinitionDetails> list =
        processDefinitionDetailsList.dtls;
      final Iterator<BDMProcessDefinitionDetails> iter = list.iterator();
      while (iter.hasNext()) {
        final BDMProcessDefinitionDetails detail = iter.next();
        if (ifProcessNameExistInTask(detail.processName,
          mandatoryTaskProceeName)) {
          if (!TASKSTATUS.CLOSED.equals(taskDetails.getStatus())) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * Method to check if open task exists
   *
   * @param caseId
   * @return
   */
  private boolean ifOpenTaskExists(final long caseId)
    throws AppException, InformationalException {

    final CaseHeader caseHeader = this.caseHeaderDAO.get(caseId);
    final Iterator caseTaskIterator =
      this.caseTasks.readAllCaseTasks(caseHeader).iterator();
    while (caseTaskIterator.hasNext()) {
      final TaskDetails taskDetails = (TaskDetails) caseTaskIterator.next();
      if (!TASKSTATUS.CLOSED.equals(taskDetails.getStatus())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Method to check if this primary applicant has another Open application case
   *
   * @param concernRoleID, caseId
   * @return
   */
  private boolean ifOtherOpenOASCase(final long concernRoleID,
    final long caseId, final String programType,
    final String caseParticipantType, final boolean isSameCaseAllowed)
    throws AppException, InformationalException {

    final curam.core.intf.CaseHeader caseHeaderObj =
      CaseHeaderFactory.newInstance();
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = concernRoleID;

    // get all client cases
    final CaseIDStructList caseIDList =
      caseHeaderObj.searchCaseIDByConcernRoleID(concernRoleKey);
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    for (final CaseIDStruct caseID : caseIDList.dtls) {

      caseHeaderKey.caseID = caseID.caseID;

      // return true if client has open application case
      // Checking for Case type for OPEN.
      if (CASETYPECODE.APPLICATION_CASE
        .equals(caseHeaderObj.read(caseHeaderKey).caseTypeCode)
        && (isSameCaseAllowed || caseID.caseID != caseId)) {
        final ApplicationCase appCase =
          this.applicationCaseDAO.get(Long.valueOf(caseID.caseID));

        if (CASESTATUS.OPEN.equals(appCase.getStatus().getCode())) {

          final List<CaseParticipantRoleDtls> memberCaseParticipantRoleDtlsList =
            getCaseParticipantByType(appCase.getID(), caseParticipantType);

          final long primaryClientID =
            memberCaseParticipantRoleDtlsList.isEmpty() ? 0
              : memberCaseParticipantRoleDtlsList.get(0).participantRoleID;

          if (primaryClientID == concernRoleID) {

            final Iterator intakeProgramAppIterator =
              appCase.getPrograms().iterator();
            while (intakeProgramAppIterator.hasNext()) {
              final IntakeProgramApplication program =
                (IntakeProgramApplication) intakeProgramAppIterator.next();
              // only if OAS pending
              if (programType
                .equals(program.getProgramType().getProgramTypeReference())
                && program.getLifecycleState()
                  .equals(IntakeProgramApplicationStatusEntry.PENDING)) {

                return true;
              }
            }
          }
        }
      }

    }
    return false;
  }

  /**
   * Method checks if Evidence Issue
   *
   * @param applicationCaseId
   * @return
   */
  private boolean isEvidenceIssue(final long applicationCaseId)
    throws AppException, InformationalException {

    final curam.core.struct.CaseKey caseKey1 =
      new curam.core.struct.CaseKey();
    caseKey1.caseID = applicationCaseId;
    final EvidenceIssuesDetailsList issuesList =
      EvidenceFactory.newInstance().listIssuesForCase(caseKey1);

    if (!issuesList.dtls.isEmpty()) {
      return true;
    }
    return false;
  }

  /**
   * Method checks if verification Issue
   *
   * @param applicationCaseId
   * @return
   */
  private boolean isMandatoryVerificationExists(final long applicationCaseId)
    throws AppException, InformationalException {

    final curam.core.struct.CaseKey caseKey = new curam.core.struct.CaseKey();
    caseKey.caseID = applicationCaseId;

    final Iterator verificationsIterator =
      BDMVerificationApplicationFactory.newInstance()
        .listOutstandingVerificationDetailsForCaseEvidence(caseKey).dtls
          .iterator();
    while (verificationsIterator.hasNext()) {
      final CaseEvidenceVerificationDisplayDetails dtl =
        (CaseEvidenceVerificationDisplayDetails) verificationsIterator.next();
      if (dtl.mandatory.equals("Yes")) {
        return true;
      }
    }
    return false;
  }

  /**
   * Method checks for Consent Evidence Present
   *
   * @param applicationCaseId
   * @return
   */
  private boolean isConsentEvidencePresent(final long applicationCaseId)
    throws AppException, InformationalException {

    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = applicationCaseId;

    final List evidenceIdList = new ArrayList();

    // Get evidence ID
    final Iterator iterEvdLst = EvidenceFactory.newInstance()
      .listAllEvidence(caseKey).evidenceParticipantDtlsList.dtls.iterator();
    while (iterEvdLst.hasNext()) {
      final EvidenceParticipantDtls dtl =
        (EvidenceParticipantDtls) iterEvdLst.next();
      if (CASEEVIDENCE.BDM_CONSENT_DECLARATION.equals(dtl.evidenceType)) {
        evidenceIdList.add(dtl.evidenceID);
      }
    }

    // Get dynamic evidence details
    final List<DynamicEvidenceDataDetails> dynEvdDtls =
      BDMEvidenceUtil.getDynamicEvidences(evidenceIdList);

    // If Consent and Declaration does not exist, return false
    if (!dynEvdDtls.isEmpty()) {
      final DynamicEvidenceDataDetails dynamicEvidenceData =
        dynEvdDtls.get(0);

      Boolean isPrimaryParticipant = false;

      final String signatureType =
        dynamicEvidenceData.getAttribute("signatureType").getValue();
      final String witnessDateSigned =
        dynamicEvidenceData.getAttribute("witnessDateSigned").getValue();
      final String dateSigned =
        dynamicEvidenceData.getAttribute("dateSigned").getValue();
      final Boolean isWitness = Boolean.parseBoolean(
        dynamicEvidenceData.getAttribute("isWitness").getValue());
      final Boolean isSigningOnBehalf = Boolean.parseBoolean(
        dynamicEvidenceData.getAttribute("isSigningOnBehalf").getValue());
      final long caseParticipantRoleID = Long.parseLong(
        dynamicEvidenceData.getAttribute("participant").getValue());

      final BDMUtil bdmUtil = new BDMUtil();
      final long participantRoleID = bdmUtil
        .getParticipantRoleID(applicationCaseId, caseParticipantRoleID);

      // Check if participant is primary
      List<CaseParticipantRoleDtls> participantRoleDtls =
        new ArrayList<CaseParticipantRoleDtls>();
      participantRoleDtls =
        getCaseParticipantByType(applicationCaseId, "PRI");
      if (participantRoleDtls.get(0).participantRoleID == participantRoleID) {
        isPrimaryParticipant = true;
      }

      // check 3 business rules
      if (signatureType.equals(BDMSIGNATURETYPE.SIGNED)
        && isPrimaryParticipant && dateSigned != null
        || signatureType.equals(BDMSIGNATURETYPE.SIGNED_WITH_MARK)
          && isWitness && witnessDateSigned != null
        || signatureType.equals(BDMSIGNATURETYPE.NOT_SIGNED)
          && isSigningOnBehalf && witnessDateSigned != null) {
        return true;
      }
    }
    return false;
  }

  /**
   * Method checks for the Prospect Person
   *
   * @param concernRoleID
   * @return
   */
  private boolean isProspectPerson(final long concernRoleID) {

    return concernRoleDAO.get(concernRoleID).getConcernRoleType().getCode()
      .equals(CONCERNROLETYPE.PROSPECTPERSON);
  }

  /**
   * @description: Task 94695 - DEV: 224.2-01 Close all Benefits associated to
   * the OAS Application.
   * System should not allow user to close all benefit if either GIS benefit or
   * OAS Pension is NOT in pending status.
   * System should not allow user to close all benefits if any open tasks exist
   * for the application case.
   * @param :@param closeAllBenefitsDetails
   * @param :@return
   * @param :@throws AppException
   * @param :@throws InformationalException
   * @throws
   */
  @Override
  public void closeAllBenefits(
    final BDMOASCloseAllBenefitsDetails closeAllBenefitsDetails)
    throws AppException, InformationalException {

    final ProgramAuthorisation programAuthorisation =
      ProgramAuthorisationFactory.newInstance();

    final curam.commonintake.impl.ApplicationCase applicationCase =
      this.applicationCaseDAO.get(closeAllBenefitsDetails.caseID);
    validateOpenTask(closeAllBenefitsDetails.caseID);

    DenyProgramDetails dtl = null;
    ProgramDenialDtls details = null;
    final Iterator appProgramIterator =
      applicationCase.getPrograms().iterator();
    while (appProgramIterator.hasNext()) {
      final IntakeProgramApplication program =
        (IntakeProgramApplication) appProgramIterator.next();
      details = new ProgramDenialDtls();
      details.comments = closeAllBenefitsDetails.comments;
      details.intakeProgramApplicationID = program.getID();
      details.reason = closeAllBenefitsDetails.denialReason;
      if (!program.getLifecycleState()
        .equals(IntakeProgramApplicationStatusEntry.PENDING)) {
        ValidationHelper
          .addValidationError(BDMOASAPPLICATIONCASEExceptionCreator
            .ERR_CLOSE_ALL_BENEFITS_NOT_PENDING());
        ValidationHelper.failIfErrorsExist();
      }
      dtl = new DenyProgramDetails();
      dtl.intakeProgramVersionNo = program.getVersionNo();
      dtl.denialDtls = details;

      programAuthorisation.denyProgram(dtl);
    }
  }

  /**
   * Method checks for the open task
   *
   * @param caseid
   * @return
   */
  private void validateOpenTask(final long caseid)
    throws AppException, InformationalException {

    final CaseHeader caseHeader = this.caseHeaderDAO.get(caseid);
    final Iterator caseTaskIterator =
      this.caseTasks.readAllCaseTasks(caseHeader).iterator();
    while (caseTaskIterator.hasNext()) {
      final TaskDetails taskDetails = (TaskDetails) caseTaskIterator.next();
      if (!TASKSTATUS.CLOSED.equals(taskDetails.getStatus())) {
        ValidationHelper
          .addValidationError(BDMOASAPPLICATIONCASEExceptionCreator
            .ERR_CLOSE_ALL_BENEFITS_OPEN_TASK());
        ValidationHelper.failIfErrorsExist();
        break;
      }
    }
  }

  /**
   * Method to close benefits from application case.
   *
   * @param details
   * @return
   */
  @Override
  public void denyProgram(final DenyProgramDetails details)
    throws AppException, InformationalException {

    final IntakeProgramApplication intakeProgramApplication =
      this.intakeProgramApplicationDAO
        .get(details.denialDtls.intakeProgramApplicationID);
    final List<Long> cases = intakeProgramApplication.listAssociatedCases();
    final ApplicationCase applicationCase =
      this.applicationCaseDAO.get(cases.get(0));
    // only if GIS
    if (BDMOASConstants.GIS_PROGRM_TYP_REF.equals(
      intakeProgramApplication.getProgramType().getProgramTypeReference())) {
      validateOpenTask(applicationCase.getID());
    }

    if (ifMandatoryTaskOpen(applicationCase.getID(),
      Configuration.getProperty(
        EnvVars.BDMOAS_APPLICATIONCASE_OASPENSION_CLOSED_MANDATORYTASK))) {
      ValidationHelper
        .addValidationError(BDMOASAPPLICATIONCASEExceptionCreator
          .ERR_CLOSE_BENEFIT_MANDATORY_TASKS_OPEN());
    }

    ValidationHelper.failIfErrorsExist();
    final ProgramAuthorisation programAuthorisation =
      ProgramAuthorisationFactory.newInstance();
    programAuthorisation.denyProgram(details);
  }

  /**
   * This method returns the list of Case Participant Roles by the Type i.e. PRI
   * or MEM.
   *
   * @param caseId
   * @param type
   * @return
   */
  private List<CaseParticipantRoleDtls>
    getCaseParticipantByType(final long caseId, final String type)
      throws AppException, InformationalException {

    final CaseParticipantRole_eoCaseIDKey eoCaseIDKey =
      new CaseParticipantRole_eoCaseIDKey();
    eoCaseIDKey.caseID = caseId;

    return CaseParticipantRoleFactory.newInstance()
      .searchByCaseID(eoCaseIDKey).dtls.stream()
        .filter(cpr -> cpr.typeCode.equals(type))
        .collect(Collectors.toList());
  }

  /**
   * This method check the processName exist in mandatory process name.
   *
   * @param processName
   * @param mandatoryTaskProceeName
   * @return
   */
  public boolean ifProcessNameExistInTask(final String processName,
    final String mandatoryTaskProceeName) {

    final String[] processNames =
      mandatoryTaskProceeName.split(CuramConst.gkComma);
    for (final String code : processNames) {
      if (code.equals(processName)) {
        return true;
      }
    }
    return false;
  }

}
