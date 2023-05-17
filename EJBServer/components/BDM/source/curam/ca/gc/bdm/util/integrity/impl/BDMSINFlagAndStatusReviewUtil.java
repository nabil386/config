package curam.ca.gc.bdm.util.integrity.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.facade.productdelivery.fact.BDMProductDeliveryFactory;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRRestResponse;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRValidation;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASEPARTICIPANTROLETYPE;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.RECORDSTATUS;
import curam.commonintake.authorisation.facade.fact.ProgramAuthorisationFactory;
import curam.commonintake.authorisation.facade.intf.ProgramAuthorisation;
import curam.commonintake.authorisation.facade.struct.DenyProgramDetails;
import curam.commonintake.authorisation.struct.AuthorisationKey;
import curam.commonintake.entity.struct.ApplicationCaseKey;
import curam.commonintake.impl.ApplicationCaseDAO;
import curam.core.facade.infrastructure.struct.PersonAndEvidenceTypeList;
import curam.core.facade.struct.ProductDeliverySuspensionKey;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.ProductDeliveryFactory;
import curam.core.sl.entity.fact.CaseParticipantRoleFactory;
import curam.core.sl.entity.struct.CaseIDParticipantRoleKey;
import curam.core.sl.entity.struct.CaseIDStruct;
import curam.core.sl.entity.struct.CaseIDStructList;
import curam.core.sl.entity.struct.CaseParticipantRoleDtls;
import curam.core.sl.entity.struct.CaseParticipantRoleDtlsList;
import curam.core.sl.entity.struct.ParticipantRoleIDAndRecordStatusKey;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.ICCaseAndStatusKey;
import curam.core.struct.ProductDeliveryDtls;
import curam.core.struct.ProductDeliveryDtlsList;
import curam.core.struct.ProductDeliveryForCaseDetails;
import curam.core.struct.ProductDeliveryForCaseDetailsList;
import curam.core.struct.ProductDeliveryTypeClientIDIn;
import curam.creole.execution.RuleObject;
import curam.creole.execution.session.InterpretedRuleObjectFactory;
import curam.creole.execution.session.RecalculationsProhibited;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.SessionDoc;
import curam.creole.execution.session.Session_Factory;
import curam.creole.ruleitem.RuleClass;
import curam.creole.ruleitem.RuleSet;
import curam.creole.storage.DataStorage;
import curam.creole.storage.database.RuleSetManager;
import curam.creole.storage.hybrid.DataStorageChooser;
import curam.creole.storage.hybrid.DataStorageType;
import curam.creole.storage.hybrid.HybridDataStorage;
import curam.pdc.facade.fact.PDCPersonFactory;
import curam.pdc.facade.struct.PDCEvidenceDetailsList;
import curam.piwrapper.caseheader.impl.CaseHeader;
import curam.piwrapper.caseheader.impl.CaseHeaderDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Trace;
import curam.util.type.Date;
import curam.workspaceservices.codetable.IntakeProgramApplicationDenialReason;
import curam.workspaceservices.codetable.IntakeProgramApplicationStatus;
import curam.workspaceservices.intake.fact.IntakeProgramApplicationFactory;
import curam.workspaceservices.intake.impl.IntakeProgramApplication;
import curam.workspaceservices.intake.struct.IntakeProgramApplicationDtls;
import curam.workspaceservices.intake.struct.IntakeProgramApplicationKey;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class contains all the required methods for the SIN Flag and Status
 * Review Process.
 *
 *
 * @author vaibhav.patil
 *
 */
public class BDMSINFlagAndStatusReviewUtil {

  private static final String BDMSIN_IDENTIFICATION_PCR =
    "BDMSINIdentificationPCR";

  private static final String BDMSIN_IDENTIFICATION_PCR_RULE_SET =
    "BDMSINIdentificationPCRRuleSet";

  private static final String RISK_LEVEL_HIGH = "CC3";

  @Inject
  private CaseHeaderDAO caseHeaderDAO;

  @Inject
  private ApplicationCaseDAO applicationCaseDAO;

  private static final int DEPENDANTROGRAMTYPEID =
    BDMConstants.DEPENDANTROGRAMTYPEID;

  private String specialAccountCodeTypeValue = "";

  private int deathStatusValue = 0;

  private int sinStatusCodeValue = 0;

  private String ssSINExpirationDate = "";

  private String dormantStatusValue = "";

  private String miscellaneousTypeFlagValue = "";

  private boolean stopFlagExist = false;

  private boolean investigationReviewFlagExist = false;

  private boolean integrityReviewFlagExist = false;

  private boolean confirmIdentityFlagExist = false;

  private boolean addressFlagExist = false;

  public BDMSINFlagAndStatusReviewUtil() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * This method handles the SIN Flag and Status review process.
   *
   * @param concernRoleID
   * @param caseID
   * @throws AppException
   * @throws InformationalException
   */
  public boolean
    processSINFlagAndStatusReview(final BDMSINIntegrityCheckResult result)
      throws AppException, InformationalException {

    final long concernRoleID = result.getDetails().getConcernRoleID();

    // Get ApplicationCaseID

    final long applicationCaseID = result.getDetails().getApplicationCaseID();

    // Get IC CaseID
    final long icCaseID = getCaseParticipantRolePC(concernRoleID);

    // final long icCaseID = caseParticipantDtls.caseID;

    // final long icCaseID =
    // getIntegratedCaseForConcernRole(result.getDetails().getICCaseID();
    long caseID = 0l;

    if (icCaseID != 0l) {
      caseID = icCaseID;
    } else {
      caseID = applicationCaseID;
    }

    if (result.getSIRResponse() == null) {
      return false;
    }

    final BDMSINSIRRestResponse sirRestResponse = result.getSIRResponse();

    if (!sirRestResponse.isSINSIRValidatonSuccess()) {

      return false;
    }

    // Setting the Flags and Statuses which are consumed by the next processes.
    setSINFlagsAndStatuses(sirRestResponse);

    final Set<Long> pdcCaseIDSet = new HashSet<>();

    // Getting all the flags details

    stopFlagExist = isSINStopFlagExists();

    investigationReviewFlagExist = isSINInvestigationReviewFlagExists();

    integrityReviewFlagExist = isSINIntegrityReviewFlagExists();

    confirmIdentityFlagExist = isSINConfirmIdentityFlagExists();

    addressFlagExist = isSINAddressFlagExists();

    // If Stop Flag exists
    if (stopFlagExist) {

      final CaseHeader caseHeader = caseHeaderDAO.get(caseID);

      // Check the Case Status
      if (caseHeader.getIntegratedCaseType().getCode().isEmpty()
        && !caseHeader.getStatus().getCode().equals(CASESTATUS.CLOSED)) {
        // Deny the Program
        denyApplicationPrograms(concernRoleID, applicationCaseID);

      } else if (caseHeader.getCaseType().getCode()
        .equals(CASETYPECODE.INTEGRATEDCASE)
        && !caseHeader.getStatus().getCode().equals(CASESTATUS.CLOSED)) {

        // Check if Person receiving Active Benefits
        final ICCaseAndStatusKey clientIDIn = new ICCaseAndStatusKey();
        clientIDIn.integratedCaseID = caseHeader.getID();
        clientIDIn.statusCode = CASESTATUS.ACTIVE;

        final ProductDeliveryForCaseDetailsList deliveryDtlsList =
          ProductDeliveryFactory.newInstance()
            .searchActiveProductDeliveriesByCaseID(clientIDIn);

        for (final ProductDeliveryForCaseDetails deliveryDtls : deliveryDtlsList.dtls) {

          final ProductDeliveryTypeClientIDIn clientAndTypeDetails =
            new ProductDeliveryTypeClientIDIn();
          clientAndTypeDetails.productType = deliveryDtls.productType;
          clientAndTypeDetails.recipConcernRoleID = concernRoleID;

          final ProductDeliveryDtlsList clientProductDeliveryDtlsList =
            ProductDeliveryFactory.newInstance()
              .searchByTypeClientID(clientAndTypeDetails);

          for (final ProductDeliveryDtls productDelDetails : clientProductDeliveryDtlsList.dtls) {
            // TODO check for the
            if (caseHeaderDAO.get(productDelDetails.caseID).getStatus()
              .getCode().equals(CASESTATUS.ACTIVE)) {

              pdcCaseIDSet.add(productDelDetails.caseID);
              // Suspend the PDC
              final ProductDeliverySuspensionKey deliverySuspensionKey =
                new ProductDeliverySuspensionKey();
              deliverySuspensionKey.caseID = productDelDetails.caseID;
              deliverySuspensionKey.reasonCode = "CSR2";
              BDMProductDeliveryFactory.newInstance()
                .suspend(deliverySuspensionKey);
            }
          }
        }
      }
    }
    if (investigationReviewFlagExist) {

      // Investigation Review Required
      // Set the Risk Level to High

      return processSINInvestigationReview(concernRoleID, applicationCaseID);

    } else {
      if (stopFlagExist) {
        // Check if SIN flags require Identity to be confirmed.
        processSINIdentityReview(concernRoleID, caseID, result);
      } else {

        if (integrityReviewFlagExist) {

          processSINIntegrityReview(concernRoleID, caseID, result,
            applicationCaseID);

        } else {
          // Check SIN Flags/Statuses requires Identity to be confirmed.
          // Raise Internal Verification on the SIN Response Evidence

          processSINIdentityReview(concernRoleID, caseID, result);
        }
      }
    }
    return false;
  }

  // Setting the SIN Flags and Statuses

  public void
    setSINFlagsAndStatuses(final BDMSINSIRRestResponse sirRestResponse) {

    final BDMSINSIRValidation sirResponse =
      sirRestResponse.getValidatedSINResults();

    // final String specialAccountTypeFlagValue =
    // sirResponse.getSsSpecialAccounts().getSsSpecialAccountCodeType();

    specialAccountCodeTypeValue =
      sirResponse.getSsSpecialAccounts().getSsSpecialAccountCodeType();

    deathStatusValue =
      sirResponse.getSsDeathIndividuals().getNcPersonLivingIndicator();
    sinStatusCodeValue =
      sirResponse.getSsIndividual().getSsSIN().getNcIdentificationStatus();

    final String sinExpirationDate =
      sirResponse.getSsExpiryDate().getSsSINExpirationDate();
    ssSINExpirationDate = sinExpirationDate == null ? ""
      : sirResponse.getSsExpiryDate().getSsSINExpirationDate();

    // final String dormantFlagValue =
    // sirResponse.getSsIndividual().getSsSIN().getSsDormantFlag();
    dormantStatusValue =
      sirResponse.getSsIndividual().getSsSIN().getSsDormantFlag();

    // final String miscFlagValue =
    // sirResponse.getSsMiscellaneous().getSsMiscType();
    miscellaneousTypeFlagValue =
      sirResponse.getSsMiscellaneous().getSsMiscType();
  }

  /**
   * Setting the application Case Risk Level to High
   *
   * @param applicationCaseID
   * @throws AppException
   * @throws InformationalException
   */
  private void setRiskLevelHigh(final long applicationCaseID)
    throws AppException, InformationalException {

    final RuleSetManager ruleSetManager =
      GuiceWrapper.getInjector().getInstance(RuleSetManager.class);

    final RuleSet sinIdentificationPCRRuleset =
      ruleSetManager.readRuleSet(BDMSIN_IDENTIFICATION_PCR_RULE_SET);
    final RuleClass sinIDPCRRuleClass =
      sinIdentificationPCRRuleset.findClass(BDMSIN_IDENTIFICATION_PCR);

    final DataStorage storage = new HybridDataStorage(
      new InterpretedRuleObjectFactory(), new DataStorageChooser() {

        @Override
        public DataStorageType
          chooseDataStorage(final RuleClass ruleClassToCheck) {

          if (sinIDPCRRuleClass.isAssignableFrom(ruleClassToCheck)) {
            return DataStorageType.IN_MEMORY;
          }
          return DataStorageType.DATABASE;

        }
      });
    final Session session = Session_Factory.getFactory()
      .newInstance(new RecalculationsProhibited(), storage);
    final SessionDoc sessionDoc = new SessionDoc(session);

    final RuleObject sinIDRuleObject =
      session.createRuleObject(sinIDPCRRuleClass, new Object[0]);

    sinIDRuleObject.getAttributeValue("caseID")
      .specifyValue(applicationCaseID);

    // Get the PCR Rule Results
    final boolean isRiskConditionMet = (boolean) sinIDRuleObject
      .getAttributeValue("isRiskConditionMet").getValue();

    if (isRiskConditionMet && applicationCaseID != 0) {
      final curam.core.intf.CaseHeader caseHeader =
        CaseHeaderFactory.newInstance();

      // Set the Risk Level to High
      final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
      caseHeaderKey.caseID = applicationCaseID;
      final CaseHeaderDtls caseHeaderDtls = caseHeader.read(caseHeaderKey);
      // Setting Risk Level to High
      caseHeaderDtls.classificationCode = RISK_LEVEL_HIGH;
      caseHeader.modify(caseHeaderKey, caseHeaderDtls);
    }

    // Keep the session doc generated code commented.
    // sessionDoc.write(new File("C:\\Dev\\SessionDOC\\PCR"));
  }

  public boolean isSINStopFlagExists()
    throws AppException, InformationalException {

    // Check if STOP Flag exists
    if (BDMIntegrityRulesUtil.flagCheckInResponseData(
      BDMSINIntegrityCheckConstants.SIR_FLAG_FRAUD,
      specialAccountCodeTypeValue)
      || BDMIntegrityRulesUtil.flagCheckInResponseData(
        BDMSINIntegrityCheckConstants.SIR_FLAG_FROZEN,
        specialAccountCodeTypeValue)
      || BDMSINIntegrityCheckConstants.SIR_FLAG_DEATH == deathStatusValue
      || BDMSINIntegrityCheckConstants.SIR_FLAG_CANCELLED == sinStatusCodeValue
      || BDMSINIntegrityCheckConstants.SIR_FLAG_VOID == sinStatusCodeValue) {

      return true;
    }
    return false;

  }

  public boolean isSINInvestigationReviewFlagExists()
    throws AppException, InformationalException {

    if (BDMIntegrityRulesUtil.flagCheckInResponseData(
      BDMSINIntegrityCheckConstants.SIR_FLAG_UNDER_REVIEW_FRAUD,
      specialAccountCodeTypeValue)
      || BDMIntegrityRulesUtil.flagCheckInResponseData(
        BDMSINIntegrityCheckConstants.SIR_FLAG_DUPLICATE,
        specialAccountCodeTypeValue)) {

      return true;
    }
    return false;

  }

  public boolean processSINInvestigationReview(final long concernRoleID,
    final long applicationCaseID)
    throws AppException, InformationalException {

    // Investigation Review Required
    // Set the Risk Level to High
    setRiskLevelHigh(applicationCaseID);
    // Checking if Investigation Task Already Exists and if not exists then
    // creating a Investigation Task
    BDMSINIntegrityTaskCreationUtil.createTaskForSINIntegrity(concernRoleID,
      BDMSINIntegrityCheckConstants.SIN_TASKTYPE_INVESTIGATION);
    return true;
  }

  public boolean isSINIntegrityReviewFlagExists()
    throws AppException, InformationalException {

    if (BDMIntegrityRulesUtil.flagCheckInResponseData(
      BDMSINIntegrityCheckConstants.SIR_FLAG_PREVIOUS_INVESTIGATION,
      specialAccountCodeTypeValue)
      || !ssSINExpirationDate.isEmpty()
        && Date.getDate(ssSINExpirationDate).before(Date.getCurrentDate())) {

      return true;
    }
    return false;

  }

  public void processSINIntegrityReview(final long concernRoleID,
    final long caseID, final BDMSINIntegrityCheckResult result,
    final long applicationCaseID)
    throws AppException, InformationalException {

    // Setting risk level to High
    setRiskLevelHigh(applicationCaseID);

    // Check if Integrity review task has already been created for
    // this SIN.
    BDMSINIntegrityTaskCreationUtil.createTaskForSINIntegrity(concernRoleID,
      BDMSINIntegrityCheckConstants.SIN_TASKTYPE_INTEGRITY);

    // Raise SIN Response Verification Verification
    final EvidenceDescriptorKey evidenceDescriptorKey =
      new EvidenceDescriptorKey();
    // Getting SIN Response i.e. SIN Identification Status
    // evidence ID
    evidenceDescriptorKey.evidenceDescriptorID =
      getSINIdentificationStatusEvidence(concernRoleID);
    // Trigger Verification
    new BDMSINIntegrityCheckUtil().triggerVerifiction(evidenceDescriptorKey);

    // Trigger SIN Identity review process
    processSINIdentityReview(concernRoleID, caseID, result);

  }

  public boolean isSINConfirmIdentityFlagExists()
    throws AppException, InformationalException {

    if (BDMIntegrityRulesUtil.flagCheckInResponseData(
      BDMSINIntegrityCheckConstants.SIR_FLAG_DORMANT, dormantStatusValue)) {
      return true;
    }
    return false;

  }

  private void processSINIdentityReview(final long concernRoleID,
    final long caseID, final BDMSINIntegrityCheckResult checkResult)
    throws AppException, InformationalException {

    if (confirmIdentityFlagExist) {
      // Raise SIN Identity Verification
      final EvidenceDescriptorKey evidenceDescriptorKey =
        new EvidenceDescriptorKey();
      evidenceDescriptorKey.evidenceDescriptorID =
        checkResult.getDetails().getSinEvidenceDescriptorID();
      new BDMSINIntegrityCheckUtil()
        .triggerVerifiction(evidenceDescriptorKey);

    } else {
      if (addressFlagExist) {
        // Process SIN Address Flag Review Checks
        processSINAdddressFlagReview(concernRoleID, caseID, checkResult);

      }
    }

  }

  public boolean isSINAddressFlagExists()
    throws AppException, InformationalException {

    if (BDMIntegrityRulesUtil.flagCheckInResponseData(
      BDMSINIntegrityCheckConstants.SIR_FLAG_DOCUMENT_RETURNED,
      miscellaneousTypeFlagValue)
      || BDMIntegrityRulesUtil.flagCheckInResponseData(
        BDMSINIntegrityCheckConstants.SIR_FLAG_CARD_RETURNED,
        miscellaneousTypeFlagValue)
      || BDMIntegrityRulesUtil.flagCheckInResponseData(
        BDMSINIntegrityCheckConstants.SIR_FLAG_CONFIRMATION_LETTER_RETURNED,
        miscellaneousTypeFlagValue)) {

      return true;
    }
    return false;

  }

  public void processSINAdddressFlagReview(final long concernRoleID,
    final long caseID, final BDMSINIntegrityCheckResult checkResult)
    throws AppException, InformationalException {

    // Raise SIN Confirm Address Verification Requirement
    final EvidenceDescriptorKey evidenceDescriptorKey =
      new EvidenceDescriptorKey();
    evidenceDescriptorKey.evidenceDescriptorID =
      checkResult.getDetails().getAddressEvidenceDecriptorID();
    new BDMSINIntegrityCheckUtil().triggerVerifiction(evidenceDescriptorKey);
  }

  private void denyApplicationPrograms(final long concernRoleID,
    final long caseID) throws InformationalException, AppException {

    final ProgramAuthorisation programAuthorisation =
      ProgramAuthorisationFactory.newInstance();
    final AuthorisationKey authorisationKey = new AuthorisationKey();
    authorisationKey.caseID = caseID;
    boolean isPrimary = false;

    isPrimary = isMemberPrimary(concernRoleID, caseID);

    // if member is primary Deny All Programs
    if (isPrimary) {

      for (final IntakeProgramApplication program : getProgramsForApplication(
        authorisationKey)) {

        final IntakeProgramApplicationDtls programApplicationDtls =
          getIntakeProgramApplicationRecord(program);

        if (programApplicationDtls.status
          .equals(IntakeProgramApplicationStatus.PENDING)) {

          // Deny Program and continue
          final DenyProgramDetails denyProgramDetails =
            new DenyProgramDetails();

          denyProgramDetails.denialDtls.reason =
            IntakeProgramApplicationDenialReason.CLIENTINELIGIBLE;
          denyProgramDetails.denialDtls.intakeProgramApplicationID =
            programApplicationDtls.intakeProgramApplicationID;

          denyProgramDetails.intakeProgramVersionNo = program.getVersionNo();
          Trace.kTopLevelLogger.info("Denied IntProgAppID :"
            + denyProgramDetails.denialDtls.intakeProgramApplicationID);
          programAuthorisation.denyProgram(denyProgramDetails);
        }
      }
    } else {
      // If Member is not Primary only deny one Dependant Program

      final List<IntakeProgramApplication> applicationDtlsList =
        getProgramsForApplicationByType(authorisationKey,
          DEPENDANTROGRAMTYPEID);

      for (final IntakeProgramApplication program : applicationDtlsList) {

        final IntakeProgramApplicationDtls programApplicationDtls =
          getIntakeProgramApplicationRecord(program);

        if (programApplicationDtls.status
          .equals(IntakeProgramApplicationStatus.PENDING)) {

          // Deny Program and continue
          final DenyProgramDetails denyProgramDetails =
            new DenyProgramDetails();

          denyProgramDetails.denialDtls.reason =
            IntakeProgramApplicationDenialReason.CLIENTINELIGIBLE;
          denyProgramDetails.denialDtls.intakeProgramApplicationID =
            programApplicationDtls.intakeProgramApplicationID;

          denyProgramDetails.intakeProgramVersionNo = program.getVersionNo();
          Trace.kTopLevelLogger
            .info("Denied Dependant Program For IntProgAppID :"
              + denyProgramDetails.denialDtls.intakeProgramApplicationID);
          programAuthorisation.denyProgram(denyProgramDetails);
          break;
        }
      }
    }
  }

  private boolean isMemberPrimary(final long concernRoleID, final long caseID)
    throws AppException, InformationalException {

    // Check if the Participant is Primary Member
    final CaseIDParticipantRoleKey caseIDParticipantRoleKey =
      new CaseIDParticipantRoleKey();

    caseIDParticipantRoleKey.caseID = caseID;
    caseIDParticipantRoleKey.participantRoleID = concernRoleID;
    final CaseParticipantRoleDtlsList caseParticipantRoleDtlsList =
      CaseParticipantRoleFactory.newInstance()
        .searchByParticipantRoleAndCase(caseIDParticipantRoleKey);

    for (final CaseParticipantRoleDtls caseParticipantRoleDtls : caseParticipantRoleDtlsList.dtls) {

      if (caseParticipantRoleDtls.typeCode
        .equals(CASEPARTICIPANTROLETYPE.PRIMARY)) {
        return true;
      }
    }
    return false;
  }

  public List<IntakeProgramApplication> getProgramsForApplication(
    final AuthorisationKey key) throws AppException, InformationalException {

    final ApplicationCaseKey applicationCaseKey = new ApplicationCaseKey();
    applicationCaseKey.applicationCaseID = key.caseID;

    final curam.commonintake.impl.ApplicationCase applicationCase =
      this.applicationCaseDAO
        .get(Long.valueOf(applicationCaseKey.applicationCaseID));

    return applicationCase.getPrograms();

  }

  /**
   * This method returns the IntakeProgramApplication Record.
   *
   * @param intakeProgramApplication
   * @return
   */
  public IntakeProgramApplicationDtls getIntakeProgramApplicationRecord(
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

  /**
   * This method returns the IntakeProgramApplication List by the program Type.
   *
   * @param key
   * @param programTypeID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private List<IntakeProgramApplication> getProgramsForApplicationByType(
    final AuthorisationKey key, final long programTypeID)
    throws AppException, InformationalException {

    final ApplicationCaseKey applicationCaseKey = new ApplicationCaseKey();
    applicationCaseKey.applicationCaseID = key.caseID;

    final curam.commonintake.impl.ApplicationCase applicationCase =
      this.applicationCaseDAO
        .get(Long.valueOf(applicationCaseKey.applicationCaseID));

    final List<IntakeProgramApplication> allProgramsList =
      applicationCase.getPrograms();

    final List<IntakeProgramApplication> programDetailsList =
      allProgramsList.stream()
        .filter(program -> getIntakeProgramApplicationRecord(
          program).programTypeID == programTypeID)
        .collect(Collectors.toList());

    return programDetailsList;
  }

  private long getSINIdentificationStatusEvidence(final long concernRoleID)
    throws AppException, InformationalException {

    final PersonAndEvidenceTypeList pdcEvidenceTypeList =
      new PersonAndEvidenceTypeList();
    pdcEvidenceTypeList.concernRoleID = concernRoleID;
    pdcEvidenceTypeList.evidenceTypeList =
      CASEEVIDENCE.BDMSINIDENTIFICATIONSTATUS;

    final PDCEvidenceDetailsList pdcEvidenceDetailsList =
      PDCPersonFactory.newInstance()
        .listCurrentParticipantEvidenceByTypes(pdcEvidenceTypeList);

    return pdcEvidenceDetailsList.list.isEmpty() ? 0
      : pdcEvidenceDetailsList.list.get(0).evidenceDescriptorID;

  }

  private long getCaseParticipantRolePC(final long concernRoleID)
    throws AppException, InformationalException {

    final ParticipantRoleIDAndRecordStatusKey participantKey =
      new ParticipantRoleIDAndRecordStatusKey();
    participantKey.participantRoleID = concernRoleID;
    participantKey.recordStatus = RECORDSTATUS.NORMAL;
    final CaseIDStructList caseIDList = CaseParticipantRoleFactory
      .newInstance().searchActiveCaseIDByParticipantID(participantKey);

    for (final CaseIDStruct caseIDStruct : caseIDList.dtls) {
      final CaseHeader caseHeader = caseHeaderDAO.get(caseIDStruct.caseID);
      if (!caseHeader.getIntegratedCaseType().getCode().isEmpty()) {
        return caseIDStruct.caseID;
      }

    }
    return 0l;

  }
}
