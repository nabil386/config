package curam.ca.gc.bdm.application.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDM_FILE_SOURCE;
import curam.ca.gc.bdm.entity.attachment.fact.BDMCaseAttachmentLinkFactory;
import curam.ca.gc.bdm.entity.attachment.intf.BDMCaseAttachmentLink;
import curam.ca.gc.bdm.entity.attachment.struct.BDMCaseAttachmentLinkDtls;
import curam.ca.gc.bdm.entity.attachment.struct.BDMCaseAttachmentLinkKey;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccountInfo;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccountManager;
import curam.codetable.CASETYPECODE;
import curam.codetable.impl.CONCERNROLETYPEEntry;
import curam.commonintake.authorisation.struct.AuthorisationKey;
import curam.commonintake.impl.ApplicationCase;
import curam.core.struct.AttachmentCaseID;
import curam.core.struct.CaseAttachmentDetailsList;
import curam.ctm.targetsystem.impl.TargetSystem;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.participant.person.impl.Person;
import curam.participant.person.impl.PersonDAO;
import curam.piwrapper.caseheader.impl.CaseHeader;
import curam.piwrapper.caseheader.impl.CaseHeaderDAO;
import curam.piwrapper.casemanager.impl.CaseParticipantRole;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.type.DeepCloneable;
import curam.util.type.NotFoundIndicator;
import curam.util.workflow.impl.EnactmentService;
import curam.workspaceservices.codetable.impl.IntakeApplicationMethodEntry;
import curam.workspaceservices.externalservices.configuration.impl.ExternalSystem;
import curam.workspaceservices.intake.impl.IntakeApplication;
import curam.workspaceservices.intake.impl.IntakeProgramApplication;
import curam.workspaceservices.util.impl.DatastoreHelper;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @since ADO-16437
 * @author teja.konda
 *
 */
@SuppressWarnings("deprecation")
public class BDMIntakeApplicationEvents
  implements IntakeApplication.IntakeApplicationEvents {

  @Inject
  private CaseHeaderDAO caseHeaderDAO;

  @Inject
  private PersonDAO personDAO;

  @Override
  public void postDisposeIntakeApplication(
    final IntakeApplication param1IntakeApplication)
    throws AppException, InformationalException {

  }

  @Override
  public void
    preMapDataToCuram(final IntakeApplication param1IntakeApplication)
      throws AppException, InformationalException {

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

    mapApplicationForLinkedApplicants(intakeApplication);

    final List<IntakeProgramApplication> programs =
      intakeApplication.listIntakeProgramApplications();

    Datastore dataStore;
    Entity[] personEntites;
    Entity applicationEntity;
    long caseID = 0l;

    final BDMEvidenceUtil bdmEvidenceUtil = new BDMEvidenceUtil();
    // create dependant evidence for the associated application case there
    // should only be one

    for (final IntakeProgramApplication intakeProgramApplication : programs) {

      if (intakeProgramApplication.getProgramType()
        .getID() == BDMConstants.SAMPLEBENEFITPROGRAMTYPEID) {

        // BEGIN TASK 17644 - VPatil - Made temporary fix to get first IC ID in
        // the list.
        final int size = intakeProgramApplication.getIntakeApplication()
          .listAssociatedCases().size();
        caseID = size > 0 ? intakeProgramApplication.getIntakeApplication()
          .listAssociatedCases().get(0) : 0l;
        // END TASK 17644
        if (caseHeaderDAO.get(caseID).getCaseType().toString()
          .equals(CASETYPECODE.APPLICATION_CASE)) {

          // using intake application get application & person Entites
          dataStore =
            DatastoreHelper.openDatastore(intakeApplication.getSchemaName());
          applicationEntity =
            dataStore.readEntity(intakeApplication.getRootEntityID());
          personEntites = applicationEntity
            .getChildEntities(dataStore.getEntityType("Person"));
          // execute this logic only if we have more than one person on the
          // Application
          if (personEntites.length > 1) {

            // call helper method to create dependant evidence
            bdmEvidenceUtil.mapDependantEvidence(personEntites, caseID,
              intakeApplication.getDataStoreToCEFMappings());
          }

          // BEGIN TASK -25425 UpdateAttestaionEvidence
          //
          bdmEvidenceUtil.updatePrimaryCaseParticipantRoleIDForAttestation(
            caseID, intakeApplication.getDataStoreToCEFMappings());
          // END TASK- 25425
          // map Tax Credit Evidence
          bdmEvidenceUtil.mapTaxCreditEvidence(applicationEntity,
            intakeApplication.getDataStoreToCEFMappings(),
            intakeApplication.getSchemaName());
          // TASK 27043 Map Incarceration Evidence, so that
          // attesteeCaseParticipant could be set
          bdmEvidenceUtil.mapIncarcerationEvidence(applicationEntity, caseID,
            intakeApplication.getDataStoreToCEFMappings(),
            intakeApplication.getSchemaName());
          // TASK 27043 Map Income Evidence, so that attesteeCaseParticipant
          // could be set
          bdmEvidenceUtil.mapIncomeEvidence(applicationEntity, caseID,
            intakeApplication.getDataStoreToCEFMappings(),
            intakeApplication.getSchemaName());
          // ADO-21128 Start Relationship evidence mapping
          bdmEvidenceUtil.mapRelationshipEvidence(applicationEntity,
            intakeApplication.getDataStoreToCEFMappings(),
            intakeApplication.getSchemaName());
        }
      }
    }
    // START : TASK 17299 --JP
    // if ApplicationPDFAttchment exists , create bdmcasAttachment link
    if (intakeApplication.getApplicationPDFAttachmentID() != 0L)
      createApplicationPDFBDMCaseAttchmentLink(programs);

    // START - TASK 17774 - Straight through processing
    // the OOTB implementation enact the straight through workflow only if
    // all clients are registered as person.
    // enacting the straight through workflow if clients are prospects
    final Set<Long> caseIDS = new HashSet<>();
    for (final IntakeProgramApplication programApplication : programs) {

      for (final long appProgCaseID : programApplication
        .listAssociatedCases()) {

        final CaseHeader caseHeader = this.caseHeaderDAO.get(appProgCaseID);

        // check if case is application case
        if (caseHeader.getCaseType().toString()
          .equals(CASETYPECODE.APPLICATION_CASE)
          && !caseIDS.contains(Long.valueOf(appProgCaseID))) {

          final ApplicationCase applicationCase =
            (ApplicationCase) caseHeader;

          // get the straight through workflow configured for the application
          // case
          final String straightThroughWorkflow = applicationCase
            .getApplicationCaseAdmin().getStraightThroughWorkflow();

          if (!StringUtil.isNullOrEmpty(straightThroughWorkflow)) {

            // enact the straight through workflow if clients are prospect
            // persons
            if (!areAllClientsRegistered(applicationCase)) {

              caseIDS.add(Long.valueOf(appProgCaseID));
              final List<DeepCloneable> enactmentStructs = new ArrayList<>();

              final AuthorisationKey authorisationKey =
                new AuthorisationKey();

              authorisationKey.caseID = appProgCaseID;
              authorisationKey.programID =
                programApplication.getID().longValue();
              authorisationKey.enactedBy =
                intakeApplication.getEnteredByUser();
              enactmentStructs.add(authorisationKey);
              EnactmentService.startProcess(straightThroughWorkflow,
                enactmentStructs);
            }
          }
        }
      }
    }
    // END - TASK 17774 - Straight through processing

  }

  // START : TASK 17299 Resolve UHSE in Application >Contacts >Attachments on
  // ApplicationPDF--JP
  /**
   * Add file source to extension class when generating Application PDF
   */
  private void createApplicationPDFBDMCaseAttchmentLink(

    final List<IntakeProgramApplication> programs)
    throws AppException, InformationalException {

    // Call OOTH Service layer MaintainAttachment
    final curam.core.intf.MaintainAttachment maintainAttachmentObj =
      curam.core.fact.MaintainAttachmentFactory.newInstance();
    final AttachmentCaseID attachCaseID = new AttachmentCaseID();

    for (final IntakeProgramApplication program : programs) {
      final List<Long> cases = program.listAssociatedCases();
      for (final Iterator<Long> iterator = cases.iterator(); iterator
        .hasNext();) {
        final long caseID = iterator.next().longValue();

        if (caseHeaderDAO.get(caseID).getCaseType().toString()
          .equals(CASETYPECODE.APPLICATION_CASE)) {

          attachCaseID.caseID = caseID;

          // Search case attachment using attchmnet id
          final CaseAttachmentDetailsList caseAttachmentDetailsList =
            maintainAttachmentObj.searchCaseAttachmentsByCaseID(attachCaseID);

          final BDMCaseAttachmentLink bdmCaseAttachmentLinkObj =
            BDMCaseAttachmentLinkFactory.newInstance();

          BDMCaseAttachmentLinkKey bdmCaseAttachmentLinkKey = null;
          BDMCaseAttachmentLinkDtls bdmCaseAttachmentLinkDtls = null;

          // create bdmCaseAttachmentLink to ApplicationPDF attchment
          for (int i = 0; i < caseAttachmentDetailsList.dtls.size(); i++) {

            bdmCaseAttachmentLinkKey = new BDMCaseAttachmentLinkKey();
            bdmCaseAttachmentLinkKey.caseAttachmentLinkID =
              caseAttachmentDetailsList.dtls.get(i).caseAttachmentLinkID;

            final NotFoundIndicator nfi = new NotFoundIndicator();
            // Read bdmCaseAttachmentLink table
            bdmCaseAttachmentLinkDtls =
              bdmCaseAttachmentLinkObj.read(nfi, bdmCaseAttachmentLinkKey);

            // if link does not exist insert into BDMCaseAttachmentLink
            // otherwise
            // modify the existing record
            if (nfi.isNotFound()) {
              bdmCaseAttachmentLinkDtls = new BDMCaseAttachmentLinkDtls();
              bdmCaseAttachmentLinkDtls.caseAttachmentLinkID =
                caseAttachmentDetailsList.dtls.get(i).caseAttachmentLinkID;
              bdmCaseAttachmentLinkDtls.fileSource = BDM_FILE_SOURCE.CLIENT;
              // TASK :16364 uncomment insert statement
              // insert into newly BDMcaseAttachmentLink entity
              bdmCaseAttachmentLinkObj.insert(bdmCaseAttachmentLinkDtls);
            } else {
              bdmCaseAttachmentLinkDtls.fileSource = BDM_FILE_SOURCE.CLIENT;
              bdmCaseAttachmentLinkObj.modify(bdmCaseAttachmentLinkKey,
                bdmCaseAttachmentLinkDtls);
            }

          }

        }
      }
    }

  }

  // START - TASK 17774 - Straight through processing
  /**
   * Returns true if all application case clients are not prospect persons
   * otherwise false.
   *
   * @param applicationCase application case
   * @return true if all clients are registered otherwise false
   */
  boolean areAllClientsRegistered(final ApplicationCase applicationCase) {

    // get all application case clients
    for (final CaseParticipantRole role : applicationCase
      .listActiveCaseMembers()) {

      // check if client is prospect person
      if (role.getConcernRole().getConcernRoleType()
        .equals(CONCERNROLETYPEEntry.PROSPECTPERSON)) {
        return false;
      }
    }

    return true;
  }
  // END - TASK 17774 - Straight through processing

  // END : TASK 17299 Resolve UHSE in Application >Contacts >Attachments on
  // ApplicationPDF--JP
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
   * Check if the applicants on applications submitted via the agent portal on
   * behalf of that applicant have a linked external user account, then map
   * the application back to that external user to expose the application on the
   * client portal
   *
   * @param intakeApplication
   */
  public void mapApplicationForLinkedApplicants(
    final IntakeApplication intakeApplication)
    throws InformationalException, AppException {

    try {
      final CitizenWorkspaceAccountManager citizenWorkspaceAccountManager =
        GuiceWrapper.getInjector()
          .getInstance(CitizenWorkspaceAccountManager.class);
      // If the application is Via the client portal then do no further
      // processing
      if (IntakeApplicationMethodEntry.ONLINE.getCode()
        .equals(intakeApplication.getApplicationMethod().getCode())) {
        return;
      }
      // read ApplicationCaseID
      final List<Long> listAssociatedCases =
        intakeApplication.listAssociatedCases();
      // If no associated case, then no need to update
      if (!listAssociatedCases.isEmpty()) {
        final long concernRoleID =
          caseHeaderDAO.get(listAssociatedCases.get(0)).getConcernRole()
            .getID().longValue();
        // get Primary participant for the applicationCase
        final Person person = personDAO.get(concernRoleID);
        // if so then check if applicant is linked to an external user
        if (citizenWorkspaceAccountManager.hasLinkedAccount(person)) {
          final CitizenWorkspaceAccountInfo citizenWorkspaceAccountInfo =
            citizenWorkspaceAccountManager.readAccountBy(person);
          // set the enteredbyUser as the external user
          intakeApplication
            .setEnteredByUser(citizenWorkspaceAccountInfo.getUserName());
          intakeApplication.modify(intakeApplication.getVersionNo());
        }
      }
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

}
