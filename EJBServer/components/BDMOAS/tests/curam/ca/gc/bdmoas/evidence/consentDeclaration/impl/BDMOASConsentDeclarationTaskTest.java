package curam.ca.gc.bdmoas.evidence.consentDeclaration.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMSIGNATURETYPE;
import curam.ca.gc.bdm.codetable.BDMWITNESSRELATIONSHIP;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.ca.gc.bdmoas.workflow.impl.BDMOASWorkflowConstants;
import curam.codetable.BIZOBJASSOCIATION;
import curam.codetable.CASEPARTICIPANTROLETYPE;
import curam.codetable.TASKSTATUS;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.commonintake.entity.struct.ApplicationCaseKey;
import curam.core.facade.fact.RepresentativeFactory;
import curam.core.facade.intf.Representative;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.facade.struct.RepresentativeID;
import curam.core.facade.struct.RepresentativeRegistrationDetails;
import curam.core.sl.fact.CaseParticipantRoleFactory;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.intf.CaseParticipantRole;
import curam.core.sl.struct.CaseIDAndParticipantRoleIDKey;
import curam.core.sl.struct.CaseParticipantRoleDetails;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.core.struct.CaseKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import curam.util.workflow.fact.BizObjAssociationFactory;
import curam.util.workflow.fact.TaskAdminFactory;
import curam.util.workflow.impl.EnactmentService;
import curam.util.workflow.impl.ProcessInstanceAdmin;
import curam.util.workflow.intf.BizObjAssociation;
import curam.util.workflow.intf.TaskAdmin;
import curam.util.workflow.struct.BizObjAssocSearchDetails;
import curam.util.workflow.struct.BizObjAssocSearchDetailsList;
import curam.util.workflow.struct.BizObjectTypeKey;
import curam.util.workflow.struct.ProcessInstanceSearchDetails;
import curam.util.workflow.struct.TaskDetailsWithSnapshot;
import curam.util.workflow.struct.TaskKey;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * The Class BDMOASConsentDeclarationTaskTest.
 *
 * JUnit class to test the task functionality related to the Consent and
 * Declaration Evidence.
 */
public class BDMOASConsentDeclarationTaskTest extends BDMOASCaseTest {

  /**
   * Test verify authorized person task was created on creating conset and
   * declaration evidence with a witness.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testVerifyAuthorizedPersonTask()
    throws AppException, InformationalException {

    // Mock the Enactment Service to enact the workflow in the same transaction
    new MockUp<EnactmentService>() {

      @Mock
      public long startProcess(final String processName,
        final List<? extends Object> enactmentStructs)
        throws AppException, InformationalException {

        return EnactmentService.startProcessInV3CompatibilityMode(processName,
          enactmentStructs);
      }
    };

    final PersonRegistrationResult person = this.registerPerson();

    // Submit OAS/GIS Application
    final ApplicationCaseKey applicationCaseKey = this.createApplicationCase(
      person.registrationIDDetails.concernRoleID, 920000);

    createConsentDeclarationEvidence(
      person.registrationIDDetails.concernRoleID,
      applicationCaseKey.applicationCaseID);

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = applicationCaseKey.applicationCaseID;
    evidenceController.applyAllChanges(caseKey);

    assertTrue(getVerifyAuthorizedPersonTaskIDByCaseID(
      applicationCaseKey.applicationCaseID) != 0l);
  }

  /**
   * Creates the consent declaration evidence.
   *
   * @param concernRoleID the concern role ID
   * @param caseID the case ID
   * @return the EI evidence key
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  private EIEvidenceKey createConsentDeclarationEvidence(
    final Long concernRoleID, final Long caseID)
    throws AppException, InformationalException {

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(caseID, concernRoleID);

    final Map<String, String> consentDeclarationAttributes =
      new HashMap<String, String>();
    consentDeclarationAttributes.put("participant",
      String.valueOf(cprObj.caseParticipantRoleID));
    consentDeclarationAttributes.put("signatureType",
      BDMSIGNATURETYPE.NOT_SIGNED);
    consentDeclarationAttributes.put("dateSigned",
      formattedDateMovedToFirstOfMonth(Date.getCurrentDate()));
    consentDeclarationAttributes.put("witness",
      String.valueOf(registerRepresentativeAndAddToTheCase(caseID)));
    consentDeclarationAttributes.put("isSigningOnBehalf",
      Boolean.TRUE.toString());
    consentDeclarationAttributes.put("relationshipType",
      BDMWITNESSRELATIONSHIP.BROTHER);
    consentDeclarationAttributes.put("witnessDateSigned",
      formattedDateMovedToFirstOfMonth(Date.getCurrentDate()));

    return this.createEvidence(caseID, concernRoleID,
      CASEEVIDENCEEntry.BDM_CONSENT_DECLARATION, consentDeclarationAttributes,
      getToday());
  }

  /**
   * Formatted date moved to first of month.
   *
   * @param date the date
   * @return the string
   */
  private String formattedDateMovedToFirstOfMonth(final Date date) {

    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    // move date to the first of the month
    final Calendar cal = date.getCalendar();
    cal.set(Calendar.DAY_OF_MONTH, 1);
    final Date firstOfMonthDate = new Date(cal);

    return dateFormat.format(firstOfMonthDate.getCalendar().getTime());
  }

  /**
   * Register representative and add to the case.
   *
   * @param caseID the case ID
   * @return the long, the representative caseparticipantroleid
   */
  private long registerRepresentativeAndAddToTheCase(final long caseID)
    throws AppException, InformationalException {

    final Representative representativeObj =
      RepresentativeFactory.newInstance();

    final RepresentativeRegistrationDetails representativeRegistrationDetails =
      new RepresentativeRegistrationDetails();
    representativeRegistrationDetails.representativeRegistrationDetails =
      new curam.core.sl.struct.RepresentativeRegistrationDetails();
    representativeRegistrationDetails.representativeRegistrationDetails.representativeDtls.representativeName =
      "Consent Witness";
    representativeRegistrationDetails.representativeRegistrationDetails.representativeRegistrationDetails.registrationDate =
      Date.getCurrentDate();
    representativeRegistrationDetails.representativeRegistrationDetails.representativeRegistrationDetails.addressData =
      this.getAddressData();
    final RepresentativeID representative = representativeObj
      .registerRepresentative(representativeRegistrationDetails);

    final long representativeConernRoleID = representative.representativeID;

    final CaseParticipantRole caseParticipantRoleObj =
      CaseParticipantRoleFactory.newInstance();
    final CaseParticipantRoleDetails caseParticipantRoleDetails =
      new CaseParticipantRoleDetails();
    caseParticipantRoleDetails.dtls.caseID = caseID;
    caseParticipantRoleDetails.dtls.participantRoleID =
      representativeConernRoleID;
    caseParticipantRoleDetails.dtls.typeCode =
      CASEPARTICIPANTROLETYPE.BDMWITNESS;
    caseParticipantRoleDetails.dtls.fromDate = Date.getCurrentDate();
    caseParticipantRoleObj
      .insertCaseParticipantRole(caseParticipantRoleDetails);

    final CaseIDAndParticipantRoleIDKey caseIDAndParticipantRoleIDKey =
      new CaseIDAndParticipantRoleIDKey();
    caseIDAndParticipantRoleIDKey.caseID = caseID;
    caseIDAndParticipantRoleIDKey.participantRoleID =
      representativeConernRoleID;
    caseParticipantRoleObj
      .readCaseParticipantRoleIDByParticipantRoleIDAndCaseID(
        caseIDAndParticipantRoleIDKey);

    final CaseParticipantRoleIDStruct cpr = caseParticipantRoleObj
      .readCaseParticipantRoleIDByParticipantRoleIDAndCaseID(
        caseIDAndParticipantRoleIDKey);

    return cpr.caseParticipantRoleID;
  }

  /**
   * Gets the verify authorized person task ID by case ID.
   *
   * @param caseID the case ID
   * @return the verify authorized person task ID by case ID
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  private long getVerifyAuthorizedPersonTaskIDByCaseID(final long caseID)
    throws AppException, InformationalException {

    // read bizObjAssociations for this case
    final BizObjAssociation bizObjAssoc =
      BizObjAssociationFactory.newInstance();
    final BizObjectTypeKey bizObjectTypeKey = new BizObjectTypeKey();
    bizObjectTypeKey.bizObjectType = BIZOBJASSOCIATION.CASE;
    bizObjectTypeKey.bizObjectID = caseID;
    final BizObjAssocSearchDetailsList bizObjAssocList =
      bizObjAssoc.searchByBizObjectTypeAndID(bizObjectTypeKey);
    if (bizObjAssocList.dtls.isEmpty()) {
      return 0l;
    }
    // Loop through bizObjAssociations
    for (final BizObjAssocSearchDetails bizObjSearchDetailsObj : bizObjAssocList.dtls) {

      final long taskID = bizObjSearchDetailsObj.taskID;

      // Read task wdo snapshot
      final TaskAdmin taskAdminObj = TaskAdminFactory.newInstance();
      final TaskDetailsWithSnapshot taskDetailsWithSnapshot =
        taskAdminObj.readDetailsWithSnapshot(taskID);

      // Skip closed task
      if (TASKSTATUS.CLOSED.equals(taskDetailsWithSnapshot.status)) {
        continue;
      }

      // Read Process details
      final TaskKey taskKey = new TaskKey();
      taskKey.taskID = taskID;
      final ProcessInstanceSearchDetails processInstanceSearchDetails =
        ProcessInstanceAdmin.searchByTaskID(taskKey);

      // If the task was created by Verify Authorized Person Task Workflow
      // return the taskID
      if (processInstanceSearchDetails.processName.equals(
        BDMOASWorkflowConstants.VERIFY_AUTHORIZED_PERSON_TASK_WORKFLOW_NAME))
        return taskID;

    }
    return 0l;
  }
}
