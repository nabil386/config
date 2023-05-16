package curam.ca.gc.bdmoas.application.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.bdmpaymentdestination.impl.BDMPaymentDestination;
import curam.ca.gc.bdm.entity.financial.struct.BDMSearchEFTDestinationDetails;
import curam.ca.gc.bdm.entity.financial.struct.BDMSearchEFTDestinationDetailsList;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.util.impl.BDMDateUtil;
import curam.ca.gc.bdmoas.evidencemapping.util.impl.BDMOASEvidenceMappingUtil;
import curam.ca.gc.bdmoas.impl.BDMOASConstants;
import curam.ca.gc.bdmoas.workflow.impl.BDMOASWorkflowConstants;
import curam.ca.gc.bdmoas.workflow.struct.BDMOASParticipantAndCaseEnactDetails;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASETYPECODE;
import curam.codetable.impl.CONCERNROLETYPEEntry;
import curam.commonintake.impl.ApplicationCase;
import curam.commonintake.impl.ApplicationCaseDAO;
import curam.core.struct.ConcernRoleKey;
import curam.ctm.targetsystem.impl.TargetSystem;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.dynamicevidence.sl.struct.impl.GenericSLDataDetails;
import curam.piwrapper.caseheader.impl.CaseHeaderDAO;
import curam.piwrapper.casemanager.impl.CaseParticipantRole;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.workflow.impl.EnactmentService;
import curam.workspaceservices.externalservices.configuration.impl.ExternalSystem;
import curam.workspaceservices.intake.impl.IntakeApplication;
import curam.workspaceservices.intake.impl.IntakeProgramApplication;
import curam.workspaceservices.util.impl.DatastoreHelper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * BDMOAS FEATURE 92921: Class Added
 * Class for OAS Intake application events.
 *
 * @author SMisal
 *
 */
@SuppressWarnings("deprecation")
public class BDMOASIntakeApplicationEvents
  implements IntakeApplication.IntakeApplicationEvents {

  private static final String IS_PRIMARY_PARTICIPANT_TRUE =
    "isPrimaryParticipant==true";

  private static final String LOCAL_ID = "localID";

  private static final String RECEIPT_DATE = "receiptDate";

  private static final String ACCOUNT_NUMBER = "accountNumber";

  @Inject
  private CaseHeaderDAO caseHeaderDAO;

  @Inject
  private ApplicationCaseDAO applicationCaseDAO;

  @Override
  public void postDisposeIntakeApplication(
    final IntakeApplication param1IntakeApplication)
    throws AppException, InformationalException {

    // Not required to implement.
  }

  @Override
  public void
    preMapDataToCuram(final IntakeApplication param1IntakeApplication)
      throws AppException, InformationalException {

    // Not required to implement.
  }

  /**
   * @param intakeApplication
   *
   * post mapping of evidence implementation
   *
   */
  @Override
  public void postMapDataToCuram(final IntakeApplication intakeApplication)
    throws AppException, InformationalException {

    final String intakeScriptID =
      intakeApplication.getIntakeApplicationType().getIntakeScriptID();
    if (intakeScriptID.equals(
      BDMOASDatastoreConstants.BDMOASGIS_COMBINED_APPLICATION_AP_SCRIPT)) {
      this.postMapDataForOASGISCombinedApplication(intakeApplication,
        intakeScriptID);

    } else if (intakeScriptID
      .equals(BDMOASDatastoreConstants.BDM_GIS_APPLICATION_AP_SCRIPT)) {
      this.postMapDataForGISApplication(intakeApplication, intakeScriptID);
    } else if (intakeScriptID
      .equals(BDMOASDatastoreConstants.BDM_GIS_APPLICATION_CP_SCRIPT)) {
      this.postMapDataForClientPortalGISApplication(intakeApplication,
        intakeScriptID);
    }
  }

  /**
   * Method to map evidences for OASGIS_Combined Intake Application.
   *
   * @param intakeApplication
   * @param intakeScriptID
   *
   */
  private void postMapDataForOASGISCombinedApplication(
    final IntakeApplication intakeApplication, final String intakeScriptID)
    throws AppException, InformationalException {

    final List<IntakeProgramApplication> programs =
      intakeApplication.listIntakeProgramApplications();

    Datastore dataStore;
    Entity[] personEntites;
    Entity applicationEntity;
    long caseID;
    long appCaseID = 0l;

    final BDMEvidenceUtil bdmEvidenceUtil = new BDMEvidenceUtil();
    final BDMOASEvidenceMappingUtil bdmoasEvidenceUtil =
      new BDMOASEvidenceMappingUtil();

    bdmoasEvidenceUtil.intakeScriptID = intakeScriptID;

    long programTypeID = 0;
    for (final IntakeProgramApplication intakeProgramApplication : programs) {
      programTypeID = intakeProgramApplication.getProgramType().getID();
      if (programTypeID == BDMOASConstants.OAS_PENSION_PROGRAM_TYPE_ID) {

        final int size = intakeProgramApplication.getIntakeApplication()
          .listAssociatedCases().size();
        caseID = size > 0 ? intakeProgramApplication.getIntakeApplication()
          .listAssociatedCases().get(0) : 0l;
        // END TASK 17644
        if (caseHeaderDAO.get(caseID).getCaseType().toString()
          .equals(CASETYPECODE.APPLICATION_CASE)) {
          appCaseID = caseID;
          boolean primaryParticipantInd = true;
          // using intake application get application & person Entites
          dataStore =
            DatastoreHelper.openDatastore(intakeApplication.getSchemaName());
          applicationEntity =
            dataStore.readEntity(intakeApplication.getRootEntityID());

          // Application Details Evidence Mapping.
          bdmoasEvidenceUtil.mapApplicationDetailsEvidence(applicationEntity,
            caseID, intakeApplication.getDataStoreToCEFMappings(),
            intakeApplication.getSchemaName(), primaryParticipantInd);

          // Legal Status Evidence Mapping.
          bdmoasEvidenceUtil.mapLegalStatusEvidence(applicationEntity, caseID,
            intakeApplication.getDataStoreToCEFMappings(),
            intakeApplication.getSchemaName(), primaryParticipantInd);

          // First Entry into Canada Evidence Mapping.
          bdmoasEvidenceUtil.mapFirstEntryIntoCanadaEvidence(
            applicationEntity, caseID,
            intakeApplication.getDataStoreToCEFMappings(),
            intakeApplication.getSchemaName(), primaryParticipantInd);

          // Nature of Absences Evidence Mapping
          bdmoasEvidenceUtil.mapNatureOfAbsencesEvidence(applicationEntity,
            caseID, intakeApplication.getDataStoreToCEFMappings(),
            intakeApplication.getSchemaName());

          // World Income Evidence Mapping
          bdmoasEvidenceUtil.mapWorldIncomeEvidence(applicationEntity, caseID,
            intakeApplication.getDataStoreToCEFMappings(),
            intakeApplication.getSchemaName());

          // Sponsorship Evidence Mapping.
          bdmoasEvidenceUtil.mapSponsorshipEvidence(applicationEntity, caseID,
            intakeApplication.getDataStoreToCEFMappings(),
            intakeApplication.getSchemaName(), primaryParticipantInd);

          // Foreign Income Evidence Mapping.
          bdmoasEvidenceUtil.mapForeignIncomeEvidence(applicationEntity,
            caseID, intakeApplication.getDataStoreToCEFMappings(),
            intakeApplication.getSchemaName(), primaryParticipantInd);

          // Residence Period Evidence Mapping.
          bdmoasEvidenceUtil.mapResidencePeriodEvidence(applicationEntity,
            caseID, intakeApplication.getDataStoreToCEFMappings(),
            intakeApplication.getSchemaName(), primaryParticipantInd);

          // Consent and Declaration Evidence Mapping
          bdmoasEvidenceUtil.mapConsentDeclarationEvidence(applicationEntity,
            caseID, intakeApplication.getDataStoreToCEFMappings(),
            intakeApplication.getSchemaName());

          // Retirement or Pension Reduction Evidence Mapping
          bdmoasEvidenceUtil.mapRetirementOrPensionReductionEvidence(
            applicationEntity, caseID,
            intakeApplication.getDataStoreToCEFMappings(),
            intakeApplication.getSchemaName(), primaryParticipantInd);

          // Marital Relationship Evidence Mapping.
          bdmoasEvidenceUtil.mapMaritalRelationshipEvidence(applicationEntity,
            caseID, intakeApplication.getDataStoreToCEFMappings(),
            intakeApplication.getSchemaName());

          personEntites = applicationEntity.getChildEntities(
            dataStore.getEntityType(BDMDatastoreConstants.PERSON));
          // execute this logic only if we have more than one person on the
          // Application
          if (personEntites.length > 1) {

            primaryParticipantInd = false;

            // Retirement or Pension Reduction Evidence Mapping for
            // Spouse/Common Law partner.
            bdmoasEvidenceUtil.mapRetirementOrPensionReductionEvidence(
              applicationEntity, caseID,
              intakeApplication.getDataStoreToCEFMappings(),
              intakeApplication.getSchemaName(), primaryParticipantInd);

            // Foreign Income Evidence Mapping for Spouse/Common Law partner.
            bdmoasEvidenceUtil.mapForeignIncomeEvidence(applicationEntity,
              caseID, intakeApplication.getDataStoreToCEFMappings(),
              intakeApplication.getSchemaName(), primaryParticipantInd);

            // Sponsorship Evidence Mapping for Spouse/Common Law partner.
            bdmoasEvidenceUtil.mapSponsorshipEvidence(applicationEntity,
              caseID, intakeApplication.getDataStoreToCEFMappings(),
              intakeApplication.getSchemaName(), primaryParticipantInd);

            // Application Details Evidence Mapping for Spouse/Common Law
            // partner.
            bdmoasEvidenceUtil.mapApplicationDetailsEvidence(
              applicationEntity, caseID,
              intakeApplication.getDataStoreToCEFMappings(),
              intakeApplication.getSchemaName(), primaryParticipantInd);
          }

          // Update Attestaion Evidence
          bdmEvidenceUtil.updatePrimaryCaseParticipantRoleIDForAttestation(
            caseID, intakeApplication.getDataStoreToCEFMappings());
        }
      }
    }

    createTasks(intakeApplication, appCaseID, intakeScriptID);
  }

  /**
   * Method to map evidences for GIS Intake Application.
   *
   * @param intakeApplication
   * @param intakeScriptID
   *
   */
  private void postMapDataForGISApplication(
    final IntakeApplication intakeApplication, final String intakeScriptID)
    throws AppException, InformationalException {

    final List<IntakeProgramApplication> programs =
      intakeApplication.listIntakeProgramApplications();

    Datastore dataStore;
    Entity[] personEntites;
    Entity applicationEntity;
    long caseID;
    long appCaseID = 0l;

    final BDMEvidenceUtil bdmEvidenceUtil = new BDMEvidenceUtil();
    final BDMOASEvidenceMappingUtil bdmoasEvidenceUtil =
      new BDMOASEvidenceMappingUtil();

    bdmoasEvidenceUtil.intakeScriptID = intakeScriptID;

    long programTypeID = 0;
    for (final IntakeProgramApplication intakeProgramApplication : programs) {
      programTypeID = intakeProgramApplication.getProgramType().getID();
      if (programTypeID == BDMOASConstants.GIS_PROGRAM_TYPE_ID) {
        final int size = intakeProgramApplication.getIntakeApplication()
          .listAssociatedCases().size();
        caseID = size > 0 ? intakeProgramApplication.getIntakeApplication()
          .listAssociatedCases().get(0) : 0l;
        // END TASK 17644
        if (caseHeaderDAO.get(caseID).getCaseType().toString()
          .equals(CASETYPECODE.APPLICATION_CASE)) {
          appCaseID = caseID;
          boolean primaryParticipantInd = true;
          // using intake application get application & person Entites
          dataStore =
            DatastoreHelper.openDatastore(intakeApplication.getSchemaName());
          applicationEntity =
            dataStore.readEntity(intakeApplication.getRootEntityID());

          // Application Details Evidence Mapping.
          bdmoasEvidenceUtil.mapApplicationDetailsEvidence(applicationEntity,
            caseID, intakeApplication.getDataStoreToCEFMappings(),
            intakeApplication.getSchemaName(), primaryParticipantInd);

          // Sponsorship Evidence Mapping.
          bdmoasEvidenceUtil.mapSponsorshipEvidence(applicationEntity, caseID,
            intakeApplication.getDataStoreToCEFMappings(),
            intakeApplication.getSchemaName(), primaryParticipantInd);

          // Foreign Income Evidence Mapping.
          bdmoasEvidenceUtil.mapForeignIncomeEvidence(applicationEntity,
            caseID, intakeApplication.getDataStoreToCEFMappings(),
            intakeApplication.getSchemaName(), primaryParticipantInd);

          // Residence Period Evidence Mapping.
          bdmoasEvidenceUtil.mapResidencePeriodEvidence(applicationEntity,
            caseID, intakeApplication.getDataStoreToCEFMappings(),
            intakeApplication.getSchemaName(), primaryParticipantInd);

          // Consent and Declaration Evidence Mapping
          bdmoasEvidenceUtil.mapConsentDeclarationEvidence(applicationEntity,
            caseID, intakeApplication.getDataStoreToCEFMappings(),
            intakeApplication.getSchemaName());

          // Retirement or Pension Reduction Evidence Mapping
          bdmoasEvidenceUtil.mapRetirementOrPensionReductionEvidence(
            applicationEntity, caseID,
            intakeApplication.getDataStoreToCEFMappings(),
            intakeApplication.getSchemaName(), primaryParticipantInd);

          // Manually Reported/Entered Income Evidence Mapping
          bdmoasEvidenceUtil.mapManuallyEnteredIncomeEvidence(
            applicationEntity, caseID,
            intakeApplication.getDataStoreToCEFMappings(),
            intakeApplication.getSchemaName(), primaryParticipantInd);

          // Marital Relationship Evidence Mapping.
          bdmoasEvidenceUtil.mapMaritalRelationshipEvidence(applicationEntity,
            caseID, intakeApplication.getDataStoreToCEFMappings(),
            intakeApplication.getSchemaName());

          personEntites = applicationEntity.getChildEntities(
            dataStore.getEntityType(BDMDatastoreConstants.PERSON));
          // execute this logic only if we have more than one person on the
          // Application
          if (personEntites.length > 1) {

            primaryParticipantInd = false;

            // Retirement or Pension Reduction Evidence Mapping for
            // Spouse/Common Law partner.
            bdmoasEvidenceUtil.mapRetirementOrPensionReductionEvidence(
              applicationEntity, caseID,
              intakeApplication.getDataStoreToCEFMappings(),
              intakeApplication.getSchemaName(), primaryParticipantInd);

            // Foreign Income Evidence Mapping for Spouse/Common Law partner.
            bdmoasEvidenceUtil.mapForeignIncomeEvidence(applicationEntity,
              caseID, intakeApplication.getDataStoreToCEFMappings(),
              intakeApplication.getSchemaName(), primaryParticipantInd);

            // Sponsorship Evidence Mapping for Spouse/Common Law partner.
            bdmoasEvidenceUtil.mapSponsorshipEvidence(applicationEntity,
              caseID, intakeApplication.getDataStoreToCEFMappings(),
              intakeApplication.getSchemaName(), primaryParticipantInd);

            // Application Details Evidence Mapping for Spouse/Common Law
            // partner.
            bdmoasEvidenceUtil.mapApplicationDetailsEvidence(
              applicationEntity, caseID,
              intakeApplication.getDataStoreToCEFMappings(),
              intakeApplication.getSchemaName(), primaryParticipantInd);

            // Manually Reported/Entered Income Evidence Mapping
            bdmoasEvidenceUtil.mapManuallyEnteredIncomeEvidence(
              applicationEntity, caseID,
              intakeApplication.getDataStoreToCEFMappings(),
              intakeApplication.getSchemaName(), primaryParticipantInd);
          }

          // Update Attestaion Evidence
          bdmEvidenceUtil.updatePrimaryCaseParticipantRoleIDForAttestation(
            caseID, intakeApplication.getDataStoreToCEFMappings());
        }
      }
    }
    createTasks(intakeApplication, appCaseID, intakeScriptID);
  }

  /**
   * Method to map evidences for Client Portal GIS Intake Application.
   *
   * @param intakeApplication
   * @param intakeScriptID
   *
   */
  private void postMapDataForClientPortalGISApplication(
    final IntakeApplication intakeApplication, final String intakeScriptID)
    throws AppException, InformationalException {

    final List<IntakeProgramApplication> programs =
      intakeApplication.listIntakeProgramApplications();

    Datastore dataStore;
    Entity[] personEntites;
    Entity applicationEntity;
    long caseID;
    long appCaseID = 0l;

    final BDMEvidenceUtil bdmEvidenceUtil = new BDMEvidenceUtil();
    final BDMOASEvidenceMappingUtil bdmoasEvidenceUtil =
      new BDMOASEvidenceMappingUtil();

    bdmoasEvidenceUtil.intakeScriptID = intakeScriptID;

    long programTypeID = 0;
    for (final IntakeProgramApplication intakeProgramApplication : programs) {
      programTypeID = intakeProgramApplication.getProgramType().getID();
      if (programTypeID == BDMOASConstants.GIS_PROGRAM_TYPE_ID) {
        final int size = intakeProgramApplication.getIntakeApplication()
          .listAssociatedCases().size();
        caseID = size > 0 ? intakeProgramApplication.getIntakeApplication()
          .listAssociatedCases().get(0) : 0l;
        // END TASK 17644
        if (caseHeaderDAO.get(caseID).getCaseType().toString()
          .equals(CASETYPECODE.APPLICATION_CASE)) {
          appCaseID = caseID;
          boolean primaryParticipantInd = true;
          // using intake application get application & person Entites
          dataStore =
            DatastoreHelper.openDatastore(intakeApplication.getSchemaName());
          applicationEntity =
            dataStore.readEntity(intakeApplication.getRootEntityID());

          // Application Details Evidence Mapping.
          bdmoasEvidenceUtil.mapApplicationDetailsEvidence(applicationEntity,
            caseID, intakeApplication.getDataStoreToCEFMappings(),
            intakeApplication.getSchemaName(), primaryParticipantInd);

          // Sponsorship Evidence Mapping.
          bdmoasEvidenceUtil.mapSponsorshipEvidence(applicationEntity, caseID,
            intakeApplication.getDataStoreToCEFMappings(),
            intakeApplication.getSchemaName(), primaryParticipantInd);

          // Foreign Income Evidence Mapping.
          bdmoasEvidenceUtil.mapForeignIncomeEvidence(applicationEntity,
            caseID, intakeApplication.getDataStoreToCEFMappings(),
            intakeApplication.getSchemaName(), primaryParticipantInd);

          // Residence Period Evidence Mapping.
          bdmoasEvidenceUtil.mapResidencePeriodEvidence(applicationEntity,
            caseID, intakeApplication.getDataStoreToCEFMappings(),
            intakeApplication.getSchemaName(), primaryParticipantInd);

          // Consent and Declaration Evidence Mapping
          bdmoasEvidenceUtil.mapConsentDeclarationEvidence(applicationEntity,
            caseID, intakeApplication.getDataStoreToCEFMappings(),
            intakeApplication.getSchemaName());

          // Retirement or Pension Reduction Evidence Mapping
          bdmoasEvidenceUtil.mapRetirementOrPensionReductionEvidence(
            applicationEntity, caseID,
            intakeApplication.getDataStoreToCEFMappings(),
            intakeApplication.getSchemaName(), primaryParticipantInd);

          personEntites = applicationEntity.getChildEntities(
            dataStore.getEntityType(BDMDatastoreConstants.PERSON));
          // execute this logic only if we have more than one person on the
          // Application
          if (personEntites.length > 1) {

            primaryParticipantInd = false;

            // Marital Relationship Evidence Mapping.
            bdmoasEvidenceUtil.mapMaritalRelationshipEvidence(
              applicationEntity, caseID,
              intakeApplication.getDataStoreToCEFMappings(),
              intakeApplication.getSchemaName());

            // Retirement or Pension Reduction Evidence Mapping for
            // Spouse/Common Law partner.
            bdmoasEvidenceUtil.mapRetirementOrPensionReductionEvidence(
              applicationEntity, caseID,
              intakeApplication.getDataStoreToCEFMappings(),
              intakeApplication.getSchemaName(), primaryParticipantInd);

            // Foreign Income Evidence Mapping for Spouse/Common Law partner.
            bdmoasEvidenceUtil.mapForeignIncomeEvidence(applicationEntity,
              caseID, intakeApplication.getDataStoreToCEFMappings(),
              intakeApplication.getSchemaName(), primaryParticipantInd);

            // Sponsorship Evidence Mapping for Spouse/Common Law partner.
            bdmoasEvidenceUtil.mapSponsorshipEvidence(applicationEntity,
              caseID, intakeApplication.getDataStoreToCEFMappings(),
              intakeApplication.getSchemaName(), primaryParticipantInd);

            // Application Details Evidence Mapping for Spouse/Common Law
            // partner.
            bdmoasEvidenceUtil.mapApplicationDetailsEvidence(
              applicationEntity, caseID,
              intakeApplication.getDataStoreToCEFMappings(),
              intakeApplication.getSchemaName(), primaryParticipantInd);
          }

          // Update Attestaion Evidence
          bdmEvidenceUtil.updatePrimaryCaseParticipantRoleIDForAttestation(
            caseID, intakeApplication.getDataStoreToCEFMappings());
        }
      }
    }
    createTasks(intakeApplication, appCaseID, intakeScriptID);
  }

  private void createTasks(final IntakeApplication intakeApplication,
    final long appCaseID, final String intakeScriptID)
    throws InformationalException, AppException {

    // Task Triggers
    if (appCaseID != 0l) {
      if (intakeScriptID.equals(
        BDMOASDatastoreConstants.BDMOASGIS_COMBINED_APPLICATION_AP_SCRIPT)) {
        // 1: If app case has a prospect person participant then enact the task.
        final ApplicationCase applicationCase =
          this.applicationCaseDAO.get(appCaseID);

        final boolean isAppCaseHasPPPtcpt =
          this.isProspectPersonParticipantAddedOnCase(applicationCase);

        if (isAppCaseHasPPPtcpt) {
          this.enactAppCaseHasPPParticipantTaskWorkflow(appCaseID);
        }

        // 2: If residence period is missing then enact task.
        final List<GenericSLDataDetails> residencePeriodEvdList =
          BDMEvidenceUtil.getEvidenceDetailsListForCaseIDAndType(appCaseID,
            CASEEVIDENCE.OAS_RESIDENCE_PERIOD);

        if (residencePeriodEvdList.isEmpty()) {
          this.enactRessidencePeriodMissingTaskWorkflow(appCaseID);
        }

        // 3: If "Nature of Absence Evidence" added on application case then
        // enact task.
        final List<GenericSLDataDetails> natureOfAbsenceEvdList =
          BDMEvidenceUtil.getEvidenceDetailsListForCaseIDAndType(appCaseID,
            CASEEVIDENCE.OAS_NATURE_OF_ABSENCES);

        if (!natureOfAbsenceEvdList.isEmpty()) {
          this.enactNatureOfAbsenceEvdAddedTaskWorkflow(appCaseID);
        }

        // 4. If direct deposit info is conflicting then enact task
        createUpdateDirectDepositLinkInfoTask(intakeApplication, appCaseID);
      } else if (intakeScriptID
        .equals(BDMOASDatastoreConstants.BDM_GIS_APPLICATION_AP_SCRIPT)) {
        // Agent portal GIS Application.
        final ApplicationCase applicationCase =
          this.applicationCaseDAO.get(appCaseID);

        final boolean isAppCaseHasPPPtcpt =
          this.isProspectPersonParticipantAddedOnCase(applicationCase);

        if (isAppCaseHasPPPtcpt) {
          this.enactAppCaseHasPPParticipantTaskWorkflow(appCaseID);
        }

      } else if (intakeScriptID
        .equals(BDMOASDatastoreConstants.BDM_GIS_APPLICATION_CP_SCRIPT)) {
        // Client portal GIS Application.
        // 1: Spouse's/common-Law partner's Consent requires task.
        final ApplicationCase applicationCase =
          this.applicationCaseDAO.get(appCaseID);

        final boolean isSpouseOrCLPAddedOnCaseInd =
          this.isSpouseOrCLPAddedOnCase(applicationCase);

        if (isSpouseOrCLPAddedOnCaseInd) {
          this.enactSpouseOrCLPConsentRequiresTaskWorkflow(appCaseID);
        }

        // 2: If "Foreign Income Evidence" added on application case then
        // enact task.
        final List<GenericSLDataDetails> foreignIncomeEvdList =
          BDMEvidenceUtil.getEvidenceDetailsListForCaseIDAndType(appCaseID,
            CASEEVIDENCE.OAS_FOREIGN_INCOME);

        if (!foreignIncomeEvdList.isEmpty()) {
          this.enactForeignIncomeEvdAddedTaskWorkflow(appCaseID);
        }

      }
    }
  }

  /**
   * If direct deposit info is conflicting then enact task.
   *
   * @param intakeApplication
   * @param appCaseID
   * @throws InformationalException
   * @throws AppException
   */
  private void createUpdateDirectDepositLinkInfoTask(
    final IntakeApplication intakeApplication, final long appCaseID)
    throws InformationalException, AppException {

    Datastore dataStore;
    Entity applicationEntity;
    // 4. If direct deposit info is conflicting then enact task
    dataStore =
      DatastoreHelper.openDatastore(intakeApplication.getSchemaName());
    applicationEntity =
      dataStore.readEntity(intakeApplication.getRootEntityID());

    final Entity personEntity = applicationEntity.getChildEntities(
      dataStore.getEntityType(BDMDatastoreConstants.PERSON),
      IS_PRIMARY_PARTICIPANT_TRUE)[0];

    // get application banking info
    final Entity[] paymentEntities = personEntity.getChildEntities(
      dataStore.getEntityType(BDMDatastoreConstants.PAYMENT));

    // If info entered, look for existing direct deposit evidence
    if (paymentEntities != null && paymentEntities.length > 0) {
      final Entity paymentEntity = paymentEntities[0];
      final ConcernRoleKey key = new ConcernRoleKey();
      key.concernRoleID = Long.parseLong(personEntity.getAttribute(LOCAL_ID));

      final BDMPaymentDestination BDMPaymentDestination =
        new BDMPaymentDestination();
      final BDMSearchEFTDestinationDetailsList BDMEFTDestinationDetailsList =
        BDMPaymentDestination.listEFTDestinations(key);

      // If evidence exists, compare if the Application receipt date minus 1
      // day is earlier than start date of the existing direct deposit
      if (BDMEFTDestinationDetailsList != null) {
        java.util.Date appReceiptDate =
          BDMDateUtil.getDate(applicationEntity.getAttribute(RECEIPT_DATE));

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(appReceiptDate);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        appReceiptDate = calendar.getTime();

        boolean enact = false;
        for (final BDMSearchEFTDestinationDetails dtl : BDMEFTDestinationDetailsList.dtls) {
          if (!dtl.accountNumber
            .equals(paymentEntity.getAttribute(ACCOUNT_NUMBER))) {
            final java.util.Date evdStartDate =
              BDMDateUtil.getDate(dtl.startDate.toString());

            enact = appReceiptDate.compareTo(evdStartDate) < 0;
            if (enact) {
              this.enactUpdateDirectDepositLinkInfoTaskWorkflow(appCaseID);
              break;
            }
          }
        }

        // TODO: if payments have been made to the bank account in the
        // existing
        // direct deposit link on or after this date, enact task
      }
    }
  }

  @Override
  public void postPassIntakeApplicationToExternalSystem(
    final IntakeApplication param1IntakeApplication,
    final ExternalSystem param1ExternalSystem)
    throws AppException, InformationalException {

    // Empty method - no action required at hook point.

  }

  @Override
  public void postPassIntakeApplicationToTargetSystem(
    final IntakeApplication param1IntakeApplication,
    final TargetSystem param1TargetSystem)
    throws AppException, InformationalException {

    // Empty method - no action required at hook point.

  }

  /**
   * Method to enact "application case has prospect person participant" task
   * workflow.
   *
   * @param application case ID
   */
  private void
    enactAppCaseHasPPParticipantTaskWorkflow(final long applicationCaseID)
      throws InformationalException, AppException {

    final BDMOASParticipantAndCaseEnactDetails enactmentDetails =
      this.getEnactmentDetails(applicationCaseID);

    final List<Object> enactmentStructs = new ArrayList<Object>();
    enactmentStructs.add(enactmentDetails);

    // Enact workflow.
    EnactmentService.startProcess(
      BDMOASWorkflowConstants.kAppCasePPParticipantTaskWFName,
      enactmentStructs);
  }

  /**
   * Method to enact "nature of absence evidence added" task workflow.
   *
   * @param application case ID
   */
  private void
    enactNatureOfAbsenceEvdAddedTaskWorkflow(final long applicationCaseID)
      throws InformationalException, AppException {

    final BDMOASParticipantAndCaseEnactDetails enactmentDetails =
      this.getEnactmentDetails(applicationCaseID);

    final List<Object> enactmentStructs = new ArrayList<Object>();
    enactmentStructs.add(enactmentDetails);

    // Enact workflow.
    EnactmentService.startProcess(
      BDMOASWorkflowConstants.kNatureOfAbsenceEvdTaskWFName,
      enactmentStructs);
  }

  /**
   * Method to enact "residence period is missing" task workflow.
   *
   * @param application case ID
   */
  private void
    enactRessidencePeriodMissingTaskWorkflow(final long applicationCaseID)
      throws InformationalException, AppException {

    final BDMOASParticipantAndCaseEnactDetails enactmentDetails =
      this.getEnactmentDetails(applicationCaseID);

    final List<Object> enactmentStructs = new ArrayList<Object>();
    enactmentStructs.add(enactmentDetails);
    // Enact workflow.
    EnactmentService.startProcess(
      BDMOASWorkflowConstants.kResPeriodMissingTaskWFName, enactmentStructs);
  }

  /**
   * Method to enact "Update Direct Deposit Link Info" task
   * workflow.
   *
   * @param application case ID
   */
  private void
    enactUpdateDirectDepositLinkInfoTaskWorkflow(final long applicationCaseID)
      throws InformationalException, AppException {

    final BDMOASParticipantAndCaseEnactDetails enactmentDetails =
      this.getEnactmentDetails(applicationCaseID);

    final List<Object> enactmentStructs = new ArrayList<Object>();
    enactmentStructs.add(enactmentDetails);

    // Enact workflow.
    EnactmentService.startProcess(
      BDMOASWorkflowConstants.kUpdateDirectDepositLinkTaskWFName,
      enactmentStructs);
  }

  /**
   * Method to get the details required for workflow enactment.
   *
   * @param application case ID
   * @return BDMOASParticipantAndCaseEnactDetails
   */
  private BDMOASParticipantAndCaseEnactDetails
    getEnactmentDetails(final long applicationCaseID)
      throws InformationalException, AppException {

    final BDMOASParticipantAndCaseEnactDetails enactmentDetails =
      new BDMOASParticipantAndCaseEnactDetails();
    enactmentDetails.caseID = applicationCaseID;
    enactmentDetails.caseRef =
      caseHeaderDAO.get(applicationCaseID).getCaseReference();
    enactmentDetails.concernRoleID =
      caseHeaderDAO.get(applicationCaseID).getConcernRole().getID();
    enactmentDetails.primaryParticipantName =
      caseHeaderDAO.get(applicationCaseID).getConcernRole().getName();
    return enactmentDetails;
  }

  /**
   * Returns true if prospect person participant added on the case.
   *
   * @param applicationCase application case
   * @return true if any client is added as a prospect person participant
   */
  boolean isProspectPersonParticipantAddedOnCase(
    final ApplicationCase applicationCase) {

    // get all application case clients
    for (final CaseParticipantRole role : applicationCase
      .listActiveCaseMembers()) {

      // check if client is prospect person
      if (role.getConcernRole().getConcernRoleType()
        .equals(CONCERNROLETYPEEntry.PROSPECTPERSON)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns true if spouse or CLP added on the case.
   *
   * @param applicationCase application case
   * @return true if any client is added as a prospect person participant
   */
  boolean isSpouseOrCLPAddedOnCase(final ApplicationCase applicationCase) {

    boolean isSpouseOrCLPExists = false;
    final List<CaseParticipantRole> casePtcptRoleList =
      applicationCase.listActiveCaseMembers();

    if (casePtcptRoleList != null && casePtcptRoleList.size() > 1) {
      isSpouseOrCLPExists = true;
    }
    return isSpouseOrCLPExists;
  }

  /**
   * Method to enact "Spouse's/common-Law partner's Consent requires" task
   * workflow.
   *
   * @param application case ID
   */
  private void
    enactSpouseOrCLPConsentRequiresTaskWorkflow(final long applicationCaseID)
      throws InformationalException, AppException {

    final BDMOASParticipantAndCaseEnactDetails enactmentDetails =
      this.getEnactmentDetails(applicationCaseID);

    final List<Object> enactmentStructs = new ArrayList<Object>();
    enactmentStructs.add(enactmentDetails);

    // Enact workflow.
    EnactmentService.startProcess(
      BDMOASWorkflowConstants.kSpouseOrCLPConsentRequiresTaskWFName,
      enactmentStructs);
  }

  /**
   * Method to enact "Foreign Income evidence added" task workflow.
   *
   * @param application case ID
   */
  private void
    enactForeignIncomeEvdAddedTaskWorkflow(final long applicationCaseID)
      throws InformationalException, AppException {

    final BDMOASParticipantAndCaseEnactDetails enactmentDetails =
      this.getEnactmentDetails(applicationCaseID);

    final List<Object> enactmentStructs = new ArrayList<Object>();
    enactmentStructs.add(enactmentDetails);

    // Enact workflow.
    EnactmentService.startProcess(
      BDMOASWorkflowConstants.kForeignIncomeEvdTaskWFName, enactmentStructs);
  }
}
