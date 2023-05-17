package curam.ca.gc.bdm.application.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.communication.impl.BDMCommunicationImpl;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.sl.application.fact.BDMApplicationAuthorisationFactory;
import curam.ca.gc.bdm.sl.application.fact.BDMAuthoriseWorkflowFactory;
import curam.ca.gc.bdm.sl.application.intf.BDMApplicationAuthorisation;
import curam.ca.gc.bdm.sl.application.struct.CaseEvidenceVerificationResult;
import curam.codetable.CASEEVIDENCE;
import curam.commonintake.authorisation.impl.ProgramAuthorisationData;
import curam.commonintake.authorisation.impl.ProgramAuthorisationDataDAO;
import curam.commonintake.authorisation.struct.AuthorisationKey;
import curam.commonintake.codetable.impl.APPLICATIONCASESTATUSEntry;
import curam.commonintake.impl.ApplicationCase;
import curam.commonintake.impl.ApplicationCaseDAO;
import curam.commonintake.impl.ApplicationCaseEligibilityResult;
import curam.commonintake.impl.ApplicationCaseEligibilityResultDAO;
import curam.core.facade.fact.ProductDeliveryFactory;
import curam.core.facade.intf.ProductDelivery;
import curam.core.facade.struct.CreateCertificationDetails;
import curam.core.fact.ProductDeliveryActivationEligibilityFactory;
import curam.core.sl.struct.CaseIDKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseIDDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import curam.verification.facade.infrastructure.struct.CaseEvidenceVerificationDisplayDetails;
import curam.workspaceservices.codetable.IntakeProgramApplicationStatus;
import curam.workspaceservices.codetable.impl.IntakeProgramApplicationStatusEntry;
import curam.workspaceservices.intake.fact.IntakeProgramApplicationFactory;
import curam.workspaceservices.intake.impl.IntakeApplication;
import curam.workspaceservices.intake.impl.IntakeProgramApplication;
import curam.workspaceservices.intake.impl.IntakeProgramApplication.IntakeProgramApplicationEvents;
import curam.workspaceservices.intake.impl.IntakeProgramApplicationDAO;
import curam.workspaceservices.intake.impl.ProgramType;
import curam.workspaceservices.intake.struct.IntakeApplicationKey;
import curam.workspaceservices.intake.struct.IntakeProgramApplicationDtls;
import curam.workspaceservices.intake.struct.IntakeProgramApplicationKey;
import java.util.Calendar;
import java.util.List;

/**
 * BDM Intake Program Application Events.
 */
public class BDMIntakeProgramApplicationEventsListener
  implements IntakeProgramApplicationEvents {

  @Inject
  private ProgramAuthorisationDataDAO programAuthorisationDataDAO;

  @Inject
  private ApplicationCaseDAO applicationCaseDAO;

  @Inject
  private IntakeProgramApplicationDAO intakeProgramApplicationDAO;

  @Inject
  private ApplicationCaseEligibilityResultDAO applicationCaseEligibilityResultDAO;

  // @Inject
  // private BDMConcernRoleDocumentsImpl concernRoleDocuments;

  /**
   * {@inheritDoc}
   */
  @Override
  public void postApproveIntakeProgramApplication(
    final IntakeProgramApplication program)
    throws AppException, InformationalException {

    /*
     * Only perform the mapping for specific application types.
     */

    // TODO: check intake status
    addPDCertificationAndActivate(program);

    // BEGIN :TASK 17644 - Authorise Dependant Program
    authorizeDependantProgram(program);
    // END :TASK 17644 - Authorise Dependant Program

  }

  /**
   * Add Product Delivery certification and activate.
   *
   * @param program The intake program application.
   *
   * @throws AppException
   * @throws InformationalException
   */
  private void
    addPDCertificationAndActivate(final IntakeProgramApplication program)
      throws AppException, InformationalException {

    final List<ProgramAuthorisationData> authorisations =
      programAuthorisationDataDAO.search(program);
    /*
     * Since authorization is invoked as part of the straight through processing
     * there should be no historical authorization attempts, i.e. there should
     * only be one present because the program has just been approved.
     */
    if (authorisations.size() != 1) {
      return;
    }

    final ProgramAuthorisationData data = authorisations.get(0);

    // If the PD is not null add a certification
    if (null != data.getProductDelivery()) {

      final ProductDelivery productDelivery =
        ProductDeliveryFactory.newInstance();
      final CreateCertificationDetails details =
        new CreateCertificationDetails();
      final curam.piwrapper.caseheader.impl.ProductDelivery pd =
        data.getProductDelivery();
      details.caseID = pd.getID();
      details.certificationReceivedDate = pd.getStartDate();

      /**
       * Set certification from current date til end of the year
       * TODO: change to dynamically determine end date based off certification
       * --> may need environment variable
       */
      details.periodFromDate = Date.getCurrentDate();
      // Bug 57715 : Set the certification period start date to app submission
      // date for which the program is authorized.Get the application submission
      // date
      final Date submittedDate =
        new Date(program.getIntakeApplication().getSubmittedDateTime());
      if (!submittedDate.isZero()) {
        details.periodFromDate = submittedDate;
      }
      final int currentYear =
        details.periodFromDate.getCalendar().get(Calendar.YEAR);
      final Calendar calendar = Calendar.getInstance();
      calendar.set(currentYear, Calendar.DECEMBER, 31);
      details.periodToDate = new Date(calendar);

      productDelivery.createCertification(details);

      final curam.core.intf.CachedCaseHeader cachedCaseHeaderObj =
        curam.core.fact.CachedCaseHeaderFactory.newInstance();
      final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
      caseHeaderKey.caseID = pd.getID();

      final CaseHeaderDtls caseHeaderDtls =
        cachedCaseHeaderObj.read(caseHeaderKey);

      /**
       * NB
       *
       * TODO: In the BDM 4-weekly pattern set-up, we need to 'tell'
       * the system which date is our 'anchor' date for payments e.g. which
       * Sunday is the correct Sunday to pay out on? The
       * CaseHeader.effectiveDate is that
       * 'anchor' date and must be set appropriately here.
       *
       */
      caseHeaderDtls.effectiveDate = Date.getCurrentDate();

      cachedCaseHeaderObj.modify(caseHeaderKey, caseHeaderDtls);

      final CaseIDDetails caseIDDetails = new CaseIDDetails();
      caseIDDetails.caseID = pd.getID();
      caseIDDetails.caseType = pd.getCaseType().getCode();

      // This should activate the PD even if in-eligible, the idea here is the
      // PD will be reassessed once the evidence has been broker since the PD is
      // active.
      ProductDeliveryActivationEligibilityFactory.newInstance()
        .assessEligibilityForCase(caseIDDetails);

      // TASK 24199 - commenting this , its generating 2 letter on intake
      // authorization
      // communicationImpl
      // .createDPBenefitDenialLetterByCaseID(caseIDDetails.caseID);
      // communicationImpl
      // .createDisentitlementLetterByCaseID(caseIDDetails.caseID);

      // Task-16432 If applicable, create Dependent Benefit Entitlement Issues
      // letter
      // communicationImpl
      // .createDependentBenefitEntitlementIssuesLetter(caseHeaderKey);
    }

  }

  @Override
  public void postCreateIntakeProgramApplication(
    final IntakeProgramApplication intakeProgramApplication)
    throws AppException, InformationalException {

  }

  @Override
  public void postDenyIntakeProgramApplication(
    final IntakeProgramApplication intakeProgramApplication)
    throws AppException, InformationalException {

    // BEGIN Task 22414
    final List<Long> cases = intakeProgramApplication.listAssociatedCases();

    final ApplicationCase applicationCase =
      this.applicationCaseDAO.get(cases.get(0));

    // 1: Check whether all programs are disposed.
    boolean allProgramsDisposed = false;

    final APPLICATIONCASESTATUSEntry applicationCaseStatus =
      applicationCase.determineApplicationCaseStatus();

    if (APPLICATIONCASESTATUSEntry.CLOSED.equals(applicationCaseStatus)) {
      allProgramsDisposed = true;
    }

    // 2: If all programs are disposed then check sample benefit program
    // application status is denied.
    final IntakeApplication intakeApplication =
      intakeProgramApplication.getIntakeApplication();

    final List<IntakeProgramApplication> intakeProgramApplcnsList =
      intakeProgramApplicationDAO.listByIntakeApplication(intakeApplication);

    if (allProgramsDisposed) {
      boolean isSampleBenftProgrmStsDenied = false;
      for (final IntakeProgramApplication intakeProgrmAppln : intakeProgramApplcnsList) {
        final ProgramType prgramType = intakeProgrmAppln.getProgramType();

        final long programTypeID = prgramType.getID().longValue();

        if (programTypeID == BDMConstants.SAMPLEBENEFITPROGRAMTYPEID
          && intakeProgrmAppln.getLifecycleState()
            .equals(IntakeProgramApplicationStatusEntry.DENIED)) {
          isSampleBenftProgrmStsDenied = true;
        }
      }

      // 3: Trigger Sample Benefit denial letter.
      if (isSampleBenftProgrmStsDenied) {
        // BEGIN Task 24389
        // 1: Check whether eligibility decision is made yet.
        final List<ApplicationCaseEligibilityResult> applicationCaseEligibilityResultList =
          applicationCaseEligibilityResultDAO
            .listAllByApplicationCase(applicationCase);

        boolean isDenialDueToInEligibility = false;
        // Note: No need to check eligibility result here as this method gets
        // called in case denial happened due to in-eligibility.
        if (applicationCaseEligibilityResultList.size() > 0) {
          isDenialDueToInEligibility = true;
        }

        // 2: Check for outstanding income verification.
        final boolean isDenlDue2OutstandingVerificn =
          this.isDenialDueToOutstandingVerification(applicationCase.getID());

        // If no decision on the AC, and there is no outstanding Income
        // Verification then do not generate the benefit denial letter.
        if (isDenlDue2OutstandingVerificn) {
          new BDMCommunicationImpl()
            .createDPBenefitDenialLetterByApplicationCaseID(
              applicationCase.getID());
        }
        // END Task 24389
      }
    }
    // END Task 22414
  }

  @Override
  public void postWithdrawIntakeProgramApplication(
    final IntakeProgramApplication intakeProgramApplication)
    throws AppException, InformationalException {

  }

  @Override
  public void disposalDeadlineApproaching(
    final IntakeProgramApplication intakeProgramApplication)
    throws AppException, InformationalException {

  }

  @Override
  public void postAssociateProgramWithCase(
    final IntakeProgramApplication intakeProgramApplication,
    final long caseID) throws AppException, InformationalException {

  }

  @Override
  public void postChangeDeadlineDate(
    final IntakeProgramApplication intakeProgramApplication)
    throws AppException, InformationalException {

  }

  /**
   * This method authorises the the Dependant Benefit Program, once the Sample
   * Benefit Program Authorisation is complete.
   *
   * @param program
   * @throws AppException
   * @throws InformationalException
   */
  private void
    authorizeDependantProgram(final IntakeProgramApplication program)
      throws AppException, InformationalException {

    final IntakeApplicationKey applicationKey = new IntakeApplicationKey();
    applicationKey.intakeApplicationID =
      program.getIntakeApplication().getID();

    final AuthorisationKey authorisationKey = new AuthorisationKey();

    final curam.workspaceservices.intake.intf.IntakeProgramApplication programApplicaton =
      IntakeProgramApplicationFactory.newInstance();
    final IntakeProgramApplicationKey programApplicationKey =
      new IntakeProgramApplicationKey();
    programApplicationKey.intakeProgramApplicationID = program.getID();

    // Reading IntakeProgramApplication record.
    final IntakeProgramApplicationDtls applicationDtls =
      programApplicaton.read(programApplicationKey);

    long caseID = 0l;
    if (applicationDtls.programTypeID == BDMConstants.SAMPLEBENEFITPROGRAMTYPEID
      && applicationDtls.status
        .equals(IntakeProgramApplicationStatus.APPROVED)) {

      final List<Long> caseIDList =
        program.getIntakeApplication().listAssociatedCases();

      caseID = caseIDList.isEmpty() ? 0 : caseIDList.get(0);

      if (caseID != 0) {
        authorisationKey.caseID = caseID;

        // Calling method to authorise the Dependant benefit program
        BDMAuthoriseWorkflowFactory.newInstance()
          .authoriseDependantBenefitProgram(authorisationKey);
      }
    }
  }

  /**
   * Method to check denial is due to outstanding income verification.
   *
   * @param long applicationCaseID
   **/
  private boolean
    isDenialDueToOutstandingVerification(final long applicationCaseID)
      throws AppException, InformationalException {

    boolean outstandingVerifcnDenial = false;

    final BDMApplicationAuthorisation bdmApplicationAuthorisationObj =
      BDMApplicationAuthorisationFactory.newInstance();

    final CaseIDKey caseIDKey = new CaseIDKey();
    caseIDKey.caseID = applicationCaseID;

    final CaseEvidenceVerificationResult caseEvidenceVerificationResult =
      bdmApplicationAuthorisationObj
        .listOutstandingVerificationDetails(caseIDKey);

    if (caseEvidenceVerificationResult.hasMoreEvidenceToVerify) {
      // Denial letter contains details for income verification, so check
      // verification is remained for income evidence.
      for (final CaseEvidenceVerificationDisplayDetails verificationDisplayDetails : caseEvidenceVerificationResult.dtls) {
        if (verificationDisplayDetails.evidenceType
          .equals(CASEEVIDENCE.BDMINCOME)) {
          outstandingVerifcnDenial = true;
          break;
        }
      }
    }

    return outstandingVerifcnDenial;
  }
}
