package curam.ca.gc.bdmoas.evidence.eligibilityentitlementoverride.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdmoas.codetable.BDMOASOVERRIDEBENEFITTYPE;
import curam.ca.gc.bdmoas.codetable.BDMOASOVERRIDEREASON;
import curam.ca.gc.bdmoas.codetable.BDMOASOVERRIDEVALUE;
import curam.ca.gc.bdmoas.evidence.constants.impl.BDMOASEligibilityEntitlementOverrideConstants;
import curam.ca.gc.bdmoas.evidence.facade.fact.BDMOASEvidenceApprovalFactory;
import curam.ca.gc.bdmoas.evidence.facade.intf.BDMOASEvidenceApproval;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.ca.gc.bdmoas.message.BDMOASTASKMESSAGE;
import curam.ca.gc.bdmoas.util.impl.BDMOASTaskUtil;
import curam.codetable.REJECTIONREASON;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.facade.struct.CreateIntegratedCaseResult;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.impl.CuramConst;
import curam.core.sl.entity.fact.ApprovalRequestFactory;
import curam.core.sl.entity.intf.ApprovalRequest;
import curam.core.sl.entity.struct.ApprovalRequestDtls;
import curam.core.sl.entity.struct.ApprovalRequestKey;
import curam.core.sl.infrastructure.entity.fact.EDApprovalRequestFactory;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.intf.EDApprovalRequest;
import curam.core.sl.infrastructure.entity.struct.CurrentEDApprovalRequestKey;
import curam.core.sl.infrastructure.entity.struct.EDApprovalRequestDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.impl.TemporalEvidenceApprovalCheck;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.infrastructure.struct.EvidenceKey;
import curam.core.sl.infrastructure.struct.EvidenceRejectionDetails;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.core.sl.struct.CheckEvidenceForAutomaticApprovalDetails;
import curam.core.struct.CaseKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import curam.util.workflow.impl.EnactmentService;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Test;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * The Class BDMOASEligibilityEntitlementOverrideApprovalTest.
 *
 * This class tests the Evidence Approval Workflow task and task actions such as
 * approval and rejection.
 */
public class BDMOASEligibilityEntitlementOverrideApprovalTest
  extends BDMOASCaseTest {

  private BDMOASTaskUtil taskUtilObj;

  private EvidenceControllerInterface evidenceController;

  @Override
  protected void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    taskUtilObj = new BDMOASTaskUtil();

    evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

  }

  /**
   * Approval requester cannot be approver failure.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void approvalRequesterCannotBeApproverFailure()
    throws AppException, InformationalException {

    // Mock the temporal evidence approval check to enable approval requirement
    // for Eligibility and Entitlement Override Evidence
    new MockUp<TemporalEvidenceApprovalCheck>() {

      @Mock
      CheckEvidenceForAutomaticApprovalDetails
        checkForAutomaticApproval(final EvidenceKey k) {

        final CheckEvidenceForAutomaticApprovalDetails mockApprovalDetails =
          new CheckEvidenceForAutomaticApprovalDetails();
        if (k.evidenceType
          .equals(CASEEVIDENCEEntry.OAS_ELG_ENTITLEMNT_OVERRIDE.getCode())) {
          mockApprovalDetails.automaticApproval = false;
          mockApprovalDetails.percentage = 100;
        } else {
          mockApprovalDetails.automaticApproval = true;
          mockApprovalDetails.percentage = 0;
        }

        return mockApprovalDetails;
      }
    };

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

    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.PARTICIPANT,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.BENEFIT_TYPE,
      BDMOASOVERRIDEBENEFITTYPE.ALL_BENEFITS);
    attributes.put(
      BDMOASEligibilityEntitlementOverrideConstants.OVERRIDE_VALUE,
      BDMOASOVERRIDEVALUE.SIXTY);
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.START_DATE,
      formattedDateMovedToFirstOfMonth(Date.getCurrentDate()));
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.REASON,
      BDMOASOVERRIDEREASON.MINISTER);

    // Create Eligibility and Entitlement Override Evidence
    final EIEvidenceKey overrideEvdKey = this.createEvidence(
      integratedCase.integratedCaseID,
      person.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.OAS_ELG_ENTITLEMNT_OVERRIDE, attributes, getToday());

    // evidence descriptor details for this evidence
    final RelatedIDAndEvidenceTypeKey relatedIDAndTypeKey =
      new RelatedIDAndEvidenceTypeKey();
    relatedIDAndTypeKey.relatedID = overrideEvdKey.evidenceID;
    relatedIDAndTypeKey.evidenceType =
      CASEEVIDENCEEntry.OAS_ELG_ENTITLEMNT_OVERRIDE.getCode();

    final EvidenceDescriptorDtls evidenceDescriptorDtls =
      EvidenceDescriptorFactory.newInstance()
        .readByRelatedIDAndType(relatedIDAndTypeKey);

    // Apply Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCase.integratedCaseID;

    evidenceController.applyAllChanges(caseKey);

    // Check if evidence approval task is created
    final EvidenceKey evdKey = new EvidenceKey();
    evdKey.evidenceID = overrideEvdKey.evidenceID;
    evdKey.evidenceType = overrideEvdKey.evidenceType;
    evdKey.evidenceDescriptorID = evidenceDescriptorDtls.evidenceDescriptorID;

    final boolean taskCreated = taskUtilObj
      .getEvidenceApprovalTaskIDByEvidenceDescriptorID(evdKey) != 0l;

    assertTrue(taskCreated);

    // Approve approval request
    final BDMOASEvidenceApproval evidenceApprovalObj =
      BDMOASEvidenceApprovalFactory.newInstance();

    assertThrows(
      BDMOASTASKMESSAGE.ERR_REQUESTER_CANNOT_BE_APPROVER.getMessageText(),
      AppException.class,
      () -> evidenceApprovalObj.approveApprovalRequest(evdKey));

  }

  /**
   * Approval success.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void approvalSuccess() throws AppException, InformationalException {

    // Mock the temporal evidence approval check to enable approval requirement
    // for Eligibility and Entitlement Override Evidence
    new MockUp<TemporalEvidenceApprovalCheck>() {

      @Mock
      CheckEvidenceForAutomaticApprovalDetails
        checkForAutomaticApproval(final EvidenceKey k) {

        final CheckEvidenceForAutomaticApprovalDetails mockApprovalDetails =
          new CheckEvidenceForAutomaticApprovalDetails();
        if (k.evidenceType
          .equals(CASEEVIDENCEEntry.OAS_ELG_ENTITLEMNT_OVERRIDE.getCode())) {
          mockApprovalDetails.automaticApproval = false;
          mockApprovalDetails.percentage = 100;
        } else {
          mockApprovalDetails.automaticApproval = true;
          mockApprovalDetails.percentage = 0;
        }

        return mockApprovalDetails;
      }
    };

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

    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.PARTICIPANT,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.BENEFIT_TYPE,
      BDMOASOVERRIDEBENEFITTYPE.ALL_BENEFITS);
    attributes.put(
      BDMOASEligibilityEntitlementOverrideConstants.OVERRIDE_VALUE,
      BDMOASOVERRIDEVALUE.SIXTY);
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.START_DATE,
      formattedDateMovedToFirstOfMonth(Date.getCurrentDate()));
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.REASON,
      BDMOASOVERRIDEREASON.MINISTER);

    // Create Eligibility and Entitlement Override Evidence
    final EIEvidenceKey overrideEvdKey = this.createEvidence(
      integratedCase.integratedCaseID,
      person.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.OAS_ELG_ENTITLEMNT_OVERRIDE, attributes, getToday());

    // evidence descriptor details for this evidence
    final RelatedIDAndEvidenceTypeKey relatedIDAndTypeKey =
      new RelatedIDAndEvidenceTypeKey();
    relatedIDAndTypeKey.relatedID = overrideEvdKey.evidenceID;
    relatedIDAndTypeKey.evidenceType =
      CASEEVIDENCEEntry.OAS_ELG_ENTITLEMNT_OVERRIDE.getCode();

    final EvidenceDescriptorDtls evidenceDescriptorDtls =
      EvidenceDescriptorFactory.newInstance()
        .readByRelatedIDAndType(relatedIDAndTypeKey);

    // Apply Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCase.integratedCaseID;

    evidenceController.applyAllChanges(caseKey);

    // Check if evidence approval task is created
    final EvidenceKey evdKey = new EvidenceKey();
    evdKey.evidenceID = overrideEvdKey.evidenceID;
    evdKey.evidenceType = overrideEvdKey.evidenceType;
    evdKey.evidenceDescriptorID = evidenceDescriptorDtls.evidenceDescriptorID;

    final boolean taskCreated = taskUtilObj
      .getEvidenceApprovalTaskIDByEvidenceDescriptorID(evdKey) != 0l;

    assertTrue(taskCreated);

    // Read Approval Requester
    final EDApprovalRequest edApprovalRequestObj =
      EDApprovalRequestFactory.newInstance();
    final CurrentEDApprovalRequestKey edApprovalRequestKey =
      new CurrentEDApprovalRequestKey();
    edApprovalRequestKey.currentRequestInd = true;
    edApprovalRequestKey.evidenceDescriptorID =
      evidenceDescriptorDtls.evidenceDescriptorID;
    final EDApprovalRequestDtls edApprovalRequestDtls = edApprovalRequestObj
      .readCurrentEDApprovalRequestDetails(edApprovalRequestKey);

    final ApprovalRequest approvalRequestObj =
      ApprovalRequestFactory.newInstance();
    final ApprovalRequestKey approvalRequestKey = new ApprovalRequestKey();
    approvalRequestKey.approvalRequestID =
      edApprovalRequestDtls.approvalRequestID;
    final ApprovalRequestDtls approvalRequestDtls =
      approvalRequestObj.read(approvalRequestKey);

    // Modify the approval requester
    approvalRequestDtls.requestedByUser = "testUser";
    approvalRequestObj.modify(approvalRequestKey, approvalRequestDtls);

    // Approve approval request
    final BDMOASEvidenceApproval evidenceApprovalObj =
      BDMOASEvidenceApprovalFactory.newInstance();

    evidenceApprovalObj.approveApprovalRequest(evdKey);
  }

  /**
   * Rejection success.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void rejectionSuccess() throws AppException, InformationalException {

    // Mock the temporal evidence approval check to enable approval requirement
    // for Eligibility and Entitlement Override Evidence
    new MockUp<TemporalEvidenceApprovalCheck>() {

      @Mock
      CheckEvidenceForAutomaticApprovalDetails
        checkForAutomaticApproval(final EvidenceKey k) {

        final CheckEvidenceForAutomaticApprovalDetails mockApprovalDetails =
          new CheckEvidenceForAutomaticApprovalDetails();
        if (k.evidenceType
          .equals(CASEEVIDENCEEntry.OAS_ELG_ENTITLEMNT_OVERRIDE.getCode())) {
          mockApprovalDetails.automaticApproval = false;
          mockApprovalDetails.percentage = 100;
        } else {
          mockApprovalDetails.automaticApproval = true;
          mockApprovalDetails.percentage = 0;
        }

        return mockApprovalDetails;
      }
    };

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

    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.PARTICIPANT,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.BENEFIT_TYPE,
      BDMOASOVERRIDEBENEFITTYPE.ALL_BENEFITS);
    attributes.put(
      BDMOASEligibilityEntitlementOverrideConstants.OVERRIDE_VALUE,
      BDMOASOVERRIDEVALUE.SIXTY);
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.START_DATE,
      formattedDateMovedToFirstOfMonth(Date.getCurrentDate()));
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.REASON,
      BDMOASOVERRIDEREASON.MINISTER);

    // Create Eligibility and Entitlement Override Evidence
    final EIEvidenceKey overrideEvdKey = this.createEvidence(
      integratedCase.integratedCaseID,
      person.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.OAS_ELG_ENTITLEMNT_OVERRIDE, attributes, getToday());

    // evidence descriptor details for this evidence
    final RelatedIDAndEvidenceTypeKey relatedIDAndTypeKey =
      new RelatedIDAndEvidenceTypeKey();
    relatedIDAndTypeKey.relatedID = overrideEvdKey.evidenceID;
    relatedIDAndTypeKey.evidenceType =
      CASEEVIDENCEEntry.OAS_ELG_ENTITLEMNT_OVERRIDE.getCode();

    final EvidenceDescriptorDtls evidenceDescriptorDtls =
      EvidenceDescriptorFactory.newInstance()
        .readByRelatedIDAndType(relatedIDAndTypeKey);

    // Apply Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCase.integratedCaseID;

    evidenceController.applyAllChanges(caseKey);

    // Check if evidence approval task is created
    final EvidenceKey evdKey = new EvidenceKey();
    evdKey.evidenceID = overrideEvdKey.evidenceID;
    evdKey.evidenceType = overrideEvdKey.evidenceType;
    evdKey.evidenceDescriptorID = evidenceDescriptorDtls.evidenceDescriptorID;

    final boolean taskCreated = taskUtilObj
      .getEvidenceApprovalTaskIDByEvidenceDescriptorID(evdKey) != 0l;

    assertTrue(taskCreated);

    // Read Approval Requester
    final EDApprovalRequest edApprovalRequestObj =
      EDApprovalRequestFactory.newInstance();
    final CurrentEDApprovalRequestKey edApprovalRequestKey =
      new CurrentEDApprovalRequestKey();
    edApprovalRequestKey.currentRequestInd = true;
    edApprovalRequestKey.evidenceDescriptorID =
      evidenceDescriptorDtls.evidenceDescriptorID;
    final EDApprovalRequestDtls edApprovalRequestDtls = edApprovalRequestObj
      .readCurrentEDApprovalRequestDetails(edApprovalRequestKey);

    final ApprovalRequest approvalRequestObj =
      ApprovalRequestFactory.newInstance();
    final ApprovalRequestKey approvalRequestKey = new ApprovalRequestKey();
    approvalRequestKey.approvalRequestID =
      edApprovalRequestDtls.approvalRequestID;
    final ApprovalRequestDtls approvalRequestDtls =
      approvalRequestObj.read(approvalRequestKey);

    // Modify the approval requester
    approvalRequestDtls.requestedByUser = "testUser";
    approvalRequestObj.modify(approvalRequestKey, approvalRequestDtls);

    // Reject approval request
    final BDMOASEvidenceApproval evidenceApprovalObj =
      BDMOASEvidenceApprovalFactory.newInstance();
    final EvidenceRejectionDetails rejectionDetails =
      new EvidenceRejectionDetails();
    rejectionDetails.comments = CuramConst.gkEmpty;
    rejectionDetails.rejectionReason = REJECTIONREASON.FAILEDAPPROVALCRITERIA;
    evidenceApprovalObj.rejectApprovalRequest(evdKey, rejectionDetails);
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
}
