package curam.ca.gc.bdmoas.evidence.sharing.impl;

import curam.aes.sl.entity.fact.AESShareItemFactory;
import curam.aes.sl.entity.struct.AESShareItemDtls;
import curam.aes.sl.entity.struct.AESShareItemKey;
import curam.ca.gc.bdmoas.codetable.BDMOASBENEFITTYPE;
import curam.ca.gc.bdmoas.evidence.constants.impl.BDMOASApplicationDetailsConstants;
import curam.ca.gc.bdmoas.impl.BDMOASConstants;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASEPARTICIPANTROLETYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.sl.entity.fact.CaseParticipantRoleFactory;
import curam.core.sl.entity.intf.CaseParticipantRole;
import curam.core.sl.entity.struct.ReadByParticipantRoleTypeAndCaseKey;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.CaseIDKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.NotFoundIndicator;
import curam.workspaceservices.codetable.IntakeProgramApplicationStatus;
import curam.workspaceservices.intake.fact.IntakeProgramAppCaseLinkFactory;
import curam.workspaceservices.intake.fact.IntakeProgramApplicationFactory;
import curam.workspaceservices.intake.intf.IntakeProgramAppCaseLink;
import curam.workspaceservices.intake.intf.IntakeProgramApplication;
import curam.workspaceservices.intake.struct.IntakeProgramAppCaseLinkDtls;
import curam.workspaceservices.intake.struct.IntakeProgramAppCaseLinkDtlsList;
import curam.workspaceservices.intake.struct.IntakeProgramApplicationDtls;
import curam.workspaceservices.intake.struct.IntakeProgramApplicationKey;
import java.util.Arrays;

/**
 * The Class BDMOASAESDeliveryPlanFilterHelper.
 *
 * 104240: DEV: Implement evidence brokering
 */
public class BDMOASAESDeliveryPlanFilterHelper {

  private static final String[] kOasGisAppPrimaryParticipantEvidences =
    {CASEEVIDENCE.OAS_APPLICATION_DETAILS, CASEEVIDENCE.OAS_LEGAL_STATUS,
      CASEEVIDENCE.OAS_SPONSORSHIP, CASEEVIDENCE.OAS_FIRST_ENTRY_INTO_CANADA,
      CASEEVIDENCE.OAS_RESIDENCE_PERIOD, CASEEVIDENCE.BDMINCARCERATION,
      CASEEVIDENCE.OAS_RETIREMENT_PENSION_REDUCTION,
      CASEEVIDENCE.OAS_NATURE_OF_ABSENCES, CASEEVIDENCE.OAS_WORLD_INCOME,
      CASEEVIDENCE.OAS_FOREIGN_INCOME, CASEEVIDENCE.BDMVTW,
      CASEEVIDENCE.OAS_ELG_ENTITLEMNT_OVERRIDE };

  /**
   * Should ignore sharing between source oas gis application case target oas
   * integrated case.
   *
   * @param concernRoleID the concern role ID
   * @param sourceCaseID the source case ID
   * @param targetCaseID the target case ID
   * @param evidenceID the evidence ID
   * @param evidenceType the evidence type
   * @return the boolean
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  public Boolean shouldIgnoreSharingBtwnSourceOasGisAppCaseTargetOasIntCase(
    final Long concernRoleID, final Long sourceCaseID,
    final Long targetCaseID, final Long evidenceID, final String evidenceType)
    throws AppException, InformationalException {

    Boolean shouldIgnore = false;

    if (Arrays.asList(kOasGisAppPrimaryParticipantEvidences)
      .contains(evidenceType)) {

      // check if the participant is a primary participant in the target case
      final CaseParticipantRole cprObj =
        CaseParticipantRoleFactory.newInstance();
      final NotFoundIndicator primaryNotFoundInd = new NotFoundIndicator();
      final ReadByParticipantRoleTypeAndCaseKey readByParticipantRoleTypeAndCaseKey =
        new ReadByParticipantRoleTypeAndCaseKey();
      readByParticipantRoleTypeAndCaseKey.caseID = targetCaseID;
      readByParticipantRoleTypeAndCaseKey.participantRoleID = concernRoleID;
      readByParticipantRoleTypeAndCaseKey.recordStatus = RECORDSTATUS.NORMAL;
      readByParticipantRoleTypeAndCaseKey.typeCode =
        CASEPARTICIPANTROLETYPE.PRIMARY;
      cprObj
        .readCaseParticipantRoleIDByParticipantRoleIDCaseIDTypeAndRecordStatus(
          primaryNotFoundInd, readByParticipantRoleTypeAndCaseKey);

      // if the participant is not primary
      if (primaryNotFoundInd.isNotFound()) {
        shouldIgnore = true;
      }

      // check if the application details evidence is for the authorized benefit
      if (CASEEVIDENCE.OAS_APPLICATION_DETAILS.equals(evidenceType)) {
        // get program applications by application case id
        shouldIgnore =
          !isAppDetailsBenefitTypeForAuthProgram(evidenceID, sourceCaseID);
      }
    }
    return shouldIgnore;
  }

  /**
   * Checks if application details evidence benefit type is for authorization
   * program.
   *
   * @param evidenceID the evidence ID
   * @param applicationCaseID the application case ID
   * @return the boolean
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  public Boolean isAppDetailsBenefitTypeForAuthProgram(final Long evidenceID,
    final Long applicationCaseID)
    throws AppException, InformationalException {

    final Boolean isBenefitTypeSameAsAuthProgram = false;

    // read application details evidence benefit type
    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EIEvidenceKey evidenceKey = new EIEvidenceKey();
    evidenceKey.evidenceID = evidenceID;
    evidenceKey.evidenceType = CASEEVIDENCE.OAS_APPLICATION_DETAILS;
    final EIEvidenceReadDtls evidenceReadDtls =
      evidenceController.readEvidence(evidenceKey);

    final DynamicEvidenceDataDetails evidence =
      (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;
    final String benefitType =
      evidence.getAttribute(BDMOASApplicationDetailsConstants.BENEFIT_TYPE)
        .getValue();

    // get program type being authorized
    final Long programType = getProgramTypeBeingAuthorized(applicationCaseID);

    if (benefitType.equals(BDMOASBENEFITTYPE.OAS_PENSION)
      && programType == BDMOASConstants.OAS_PENSION_PROGRAM_TYPE_ID
      || benefitType.equals(BDMOASBENEFITTYPE.GUARANTEED_INCOME_SUPPLEMENT)
        && programType == BDMOASConstants.GIS_PROGRAM_TYPE_ID)
      return true;

    return isBenefitTypeSameAsAuthProgram;
  }

  /**
   * Gets the program type being authorized.
   *
   * @param applicationCaseID the application case ID
   * @return the program type being authorized
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  private Long getProgramTypeBeingAuthorized(final Long applicationCaseID)
    throws AppException, InformationalException {

    final IntakeProgramAppCaseLink appCaseLink =
      IntakeProgramAppCaseLinkFactory.newInstance();
    final CaseIDKey caseIDKey = new CaseIDKey();
    caseIDKey.caseID = applicationCaseID;

    final IntakeProgramAppCaseLinkDtlsList dtlsList =
      appCaseLink.searchByCaseID(caseIDKey);

    final IntakeProgramApplication application =
      IntakeProgramApplicationFactory.newInstance();
    final IntakeProgramApplicationKey appKey =
      new IntakeProgramApplicationKey();

    for (final IntakeProgramAppCaseLinkDtls dtls : dtlsList.dtls) {

      appKey.intakeProgramApplicationID = dtls.intakeProgramApplicationID;

      final IntakeProgramApplicationDtls appDtls = application.read(appKey);

      if (IntakeProgramApplicationStatus.AUTHORIZATIONINPROGRESS
        .equals(appDtls.status)) {
        if (appDtls.programTypeID == BDMOASConstants.OAS_PENSION_PROGRAM_TYPE_ID)
          return BDMOASConstants.OAS_PENSION_PROGRAM_TYPE_ID;
        if (appDtls.programTypeID == BDMOASConstants.GIS_PROGRAM_TYPE_ID)
          return BDMOASConstants.GIS_PROGRAM_TYPE_ID;
      }
    }

    return BDMOASConstants.OAS_PENSION_PROGRAM_TYPE_ID;
  }

  public AESShareItemDtls getAESShareItemDtlsByShareItemID(
    final long shareItemID) throws AppException, InformationalException {

    final AESShareItemKey key = new AESShareItemKey();
    key.shareItemID = shareItemID;

    return AESShareItemFactory.newInstance().read(key);
  }
}
