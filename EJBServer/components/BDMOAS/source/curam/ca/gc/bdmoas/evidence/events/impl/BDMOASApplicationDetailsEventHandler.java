package curam.ca.gc.bdmoas.evidence.events.impl;

import curam.ca.gc.bdm.evidence.events.impl.BDMAbstractEvidenceEventHandler;
import curam.ca.gc.bdmoas.codetable.BDMOASAPPLICATIONSTATUS;
import curam.ca.gc.bdmoas.evidence.constants.impl.BDMOASApplicationDetailsConstants;
import curam.codetable.CASETYPECODE;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.commonintake.authorisation.facade.fact.ProgramAuthorisationFactory;
import curam.commonintake.authorisation.facade.intf.ProgramAuthorisation;
import curam.commonintake.authorisation.facade.struct.ProgramWithdrawalDetails;
import curam.core.fact.CaseHeaderFactory;
import curam.core.intf.CaseHeader;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.infrastructure.struct.EIEvidenceKeyList;
import curam.core.sl.struct.CaseIDKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.participant.person.impl.Person;
import curam.participant.person.impl.PersonDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.Date;
import curam.workspaceservices.codetable.IntakeApplicationMethod;
import curam.workspaceservices.codetable.IntakeProgramApplicationStatus;
import curam.workspaceservices.codetable.WithdrawalRequestReason;
import curam.workspaceservices.intake.fact.IntakeProgramAppCaseLinkFactory;
import curam.workspaceservices.intake.fact.IntakeProgramApplicationFactory;
import curam.workspaceservices.intake.impl.ProgramType;
import curam.workspaceservices.intake.impl.ProgramTypeDAO;
import curam.workspaceservices.intake.intf.IntakeProgramAppCaseLink;
import curam.workspaceservices.intake.intf.IntakeProgramApplication;
import curam.workspaceservices.intake.struct.IntakeProgramAppCaseLinkDtls;
import curam.workspaceservices.intake.struct.IntakeProgramAppCaseLinkDtlsList;
import curam.workspaceservices.intake.struct.IntakeProgramApplicationDtls;
import curam.workspaceservices.intake.struct.IntakeProgramApplicationKey;

public class BDMOASApplicationDetailsEventHandler
  extends BDMAbstractEvidenceEventHandler {

  private static final String OAS_PENSION_REF = "OASPensionProgram";

  @Override
  public CASEEVIDENCEEntry evidenceType() {

    return CASEEVIDENCEEntry.OAS_APPLICATION_DETAILS;

  }

  @Override
  public boolean subscribePostActivation() {

    return true;

  }

  /**
   * Sets the associated program to Withdrawn if the case type is Application
   * Case.
   */
  @Override
  public void postActivation(
    final EvidenceControllerInterface evidenceController, final CaseKey key,
    final EIEvidenceKeyList list)
    throws AppException, InformationalException {

    final CaseHeader caseHeader = CaseHeaderFactory.newInstance();
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = key.caseID;
    final CaseHeaderDtls caseHeaderDtls = caseHeader.read(caseHeaderKey);

    if (CASETYPECODE.APPLICATION_CASE.equals(caseHeaderDtls.caseTypeCode)) {

      final EIEvidenceKeyList evidenceKeyList =
        this.filterEvidenceKeyList(list);

      for (final EIEvidenceKey evidenceKey : evidenceKeyList.dtls) {

        final EIEvidenceReadDtls evidenceReadDtls =
          evidenceController.readEvidence(evidenceKey);

        final PersonDAO personDAO =
          GuiceWrapper.getInjector().getInstance(PersonDAO.class);
        final EvidenceDescriptor descriptor =
          EvidenceDescriptorFactory.newInstance();
        final EvidenceDescriptorKey descriptorKey =
          new EvidenceDescriptorKey();
        descriptorKey.evidenceDescriptorID =
          evidenceReadDtls.descriptor.evidenceDescriptorID;
        final EvidenceDescriptorDtls descriptorDtls =
          descriptor.read(descriptorKey);

        final Person person = personDAO.get(descriptorDtls.participantID);

        final DynamicEvidenceDataDetails evidence =
          (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;

        final String applicationStatus = evidence
          .getAttribute(BDMOASApplicationDetailsConstants.APPLICATION_STATUS)
          .getValue();

        if (BDMOASAPPLICATIONSTATUS.WITHDRAWN.equals(applicationStatus)) {

          final Date withdrawalDate = Date.fromISO8601(evidence
            .getAttribute(BDMOASApplicationDetailsConstants.WITHDRAWAL_DATE)
            .getValue());

          final ProgramAuthorisation authorisation =
            ProgramAuthorisationFactory.newInstance();
          final ProgramWithdrawalDetails details =
            new ProgramWithdrawalDetails();
          details.withdrawalDate = withdrawalDate;
          details.dtls.requestMethod = IntakeApplicationMethod.PAPER;
          details.dtls.withdrawalRequestReason =
            WithdrawalRequestReason.CHANGEOFCIRC;
          details.dtls.intakeProgramApplicationID =
            this.getOASIntakeProgramApplicationID(key.caseID);
          details.clientFullName = person.getName();
          details.dtls.firstName = person.getFirstName();
          details.dtls.surname = person.getLastName();

          if (0 != details.dtls.intakeProgramApplicationID) {
            authorisation.withdrawProgram(details);
          }

        }

      }

    }

  }

  private long getOASIntakeProgramApplicationID(final long caseID)
    throws AppException, InformationalException {

    final IntakeProgramAppCaseLink appCaseLink =
      IntakeProgramAppCaseLinkFactory.newInstance();
    final CaseIDKey caseIDKey = new CaseIDKey();
    caseIDKey.caseID = caseID;

    final IntakeProgramAppCaseLinkDtlsList dtlsList =
      appCaseLink.searchByCaseID(caseIDKey);

    final IntakeProgramApplication application =
      IntakeProgramApplicationFactory.newInstance();
    final IntakeProgramApplicationKey appKey =
      new IntakeProgramApplicationKey();
    final ProgramTypeDAO programTypeDAO =
      GuiceWrapper.getInjector().getInstance(ProgramTypeDAO.class);

    for (final IntakeProgramAppCaseLinkDtls dtls : dtlsList.dtls) {

      appKey.intakeProgramApplicationID = dtls.intakeProgramApplicationID;

      final IntakeProgramApplicationDtls appDtls = application.read(appKey);

      if (IntakeProgramApplicationStatus.PENDING.equals(appDtls.status)) {

        final ProgramType programType =
          programTypeDAO.get(appDtls.programTypeID);

        if (OAS_PENSION_REF.equals(programType.getProgramTypeReference())) {
          return appDtls.intakeProgramApplicationID;
        }

      }

    }

    return 0L;

  }

}
