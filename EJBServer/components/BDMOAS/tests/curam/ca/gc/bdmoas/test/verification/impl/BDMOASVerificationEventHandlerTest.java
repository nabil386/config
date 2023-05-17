package curam.ca.gc.bdmoas.test.verification.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdmoas.codetable.BDMOASLEGALSTATUS;
import curam.ca.gc.bdmoas.evidence.constants.impl.BDMOASLegalStatusConstants;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.ca.gc.bdmoas.sl.verification.impl.BDMOASVerificationEventHandler;
import curam.ca.gc.bdmoas.util.impl.BDMOASTaskUtil;
import curam.codetable.TASKSTATUS;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.facade.struct.CreateIntegratedCaseResult;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.sl.entity.struct.TaskKey;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.creole.execution.session.Session;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.workflow.fact.TaskAdminFactory;
import curam.util.workflow.impl.EnactmentService;
import curam.util.workflow.intf.TaskAdmin;
import curam.util.workflow.struct.TaskDetailsWithSnapshot;
import curam.verification.sl.infrastructure.entity.fact.VDIEDLinkFactory;
import curam.verification.sl.infrastructure.entity.fact.VerificationFactory;
import curam.verification.sl.infrastructure.entity.struct.VDIEDLinkAndDataItemIDDetailsList;
import curam.verification.sl.infrastructure.entity.struct.VDIEDLinkKey;
import curam.verification.sl.infrastructure.entity.struct.VerificationDtlsList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BDMOASVerificationEventHandlerTest extends BDMOASCaseTest {

  /**
   * Sets the up curam server test.
   */
  private Session session;

  PersonRegistrationResult person = null;

  @Override
  protected void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    mockEnactmentService();
  }

  /**
   * After verification added to the evidence ,task should be generated for
   * Legal status and martial relationship and beyond control evidences as per
   * configuration
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */

  @Test
  public void testMaintainVerificationAdded()
    throws AppException, InformationalException {

    final BDMUtil util = new BDMUtil();
    person = this.registerPerson();
    // Get Integrated case
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);
    final BDMOASTaskUtil bdmoasTaskUtil = new BDMOASTaskUtil();
    // Create Evidence
    final EIEvidenceKey legalevidenceKey =
      createEvidence(integratedCase.integratedCaseID,
        cprObj.caseParticipantRoleID, BDMOASLegalStatusConstants.LEGAL_STATUS,
        person.registrationIDDetails.concernRoleID);
    final TaskKey taskKey = new TaskKey();
    taskKey.taskID =
      bdmoasTaskUtil.getTaskIdFromEvidenceID(legalevidenceKey.evidenceID);
    final TaskAdmin taskAdminObj = TaskAdminFactory.newInstance();
    final long taskID = taskKey.taskID;
    final TaskDetailsWithSnapshot taskDetailsWithSnapshot =
      taskAdminObj.readDetailsWithSnapshot(taskID);
    assertEquals(TASKSTATUS.NOTSTARTED, taskDetailsWithSnapshot.status);

  }

  /**
   * After evidence deleted ,any outstanding task 27 related to evidence will be
   * closed .
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testMaintainEvidenceDeleted()
    throws AppException, InformationalException {

    final BDMOASTaskUtil bdmoasTaskUtil = new BDMOASTaskUtil();
    final BDMUtil util = new BDMUtil();
    final BDMOASVerificationEventHandler oasVerEventHandler =
      new BDMOASVerificationEventHandler();
    person = this.registerPerson();
    // Get Integrated case
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);
    // create Evidence
    final EIEvidenceKey legalevidenceKey =
      createEvidence(integratedCase.integratedCaseID,
        cprObj.caseParticipantRoleID, BDMOASLegalStatusConstants.LEGAL_STATUS,
        person.registrationIDDetails.concernRoleID);
    // read Evidence
    final RelatedIDAndEvidenceTypeKey evidenceTypeKey =
      new RelatedIDAndEvidenceTypeKey();
    evidenceTypeKey.evidenceType = legalevidenceKey.evidenceType;
    evidenceTypeKey.relatedID = legalevidenceKey.evidenceID;
    final EvidenceDescriptorDtls evidenceDescriptorDtls =
      EvidenceDescriptorFactory.newInstance()
        .readByRelatedIDAndType(evidenceTypeKey);
    final long taskID =
      bdmoasTaskUtil.getTaskIdFromEvidenceID(legalevidenceKey.evidenceID);
    oasVerEventHandler.maintainEvidenceDeleted(
      evidenceDescriptorDtls.evidenceDescriptorID, 0l);
    // Getting Task details
    final TaskAdmin taskAdminObj = TaskAdminFactory.newInstance();
    final TaskDetailsWithSnapshot taskDetailsWithSnapshot =
      taskAdminObj.readDetailsWithSnapshot(taskID);
    assertEquals(TASKSTATUS.CLOSED, taskDetailsWithSnapshot.status);

  }

  /**
   * Once verification is verified ,any outstanding task 27 related to evidence
   * will be closed .
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testMaintainVerificationSatisfied()
    throws AppException, InformationalException {

    final BDMOASTaskUtil bdmoasTaskUtil = new BDMOASTaskUtil();
    final BDMUtil util = new BDMUtil();
    final BDMOASVerificationEventHandler oasVerEventHandler =
      new BDMOASVerificationEventHandler();
    person = this.registerPerson();
    // Get Integrated case
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);
    // create Evidence
    final EIEvidenceKey legalevidenceKey =
      createEvidence(integratedCase.integratedCaseID,
        cprObj.caseParticipantRoleID, BDMOASLegalStatusConstants.LEGAL_STATUS,
        person.registrationIDDetails.concernRoleID);
    // read Evidence
    final RelatedIDAndEvidenceTypeKey evidenceTypeKey =
      new RelatedIDAndEvidenceTypeKey();
    evidenceTypeKey.evidenceType = legalevidenceKey.evidenceType;
    evidenceTypeKey.relatedID = legalevidenceKey.evidenceID;
    final EvidenceDescriptorDtls evidenceDescriptorDtls =
      EvidenceDescriptorFactory.newInstance()
        .readByRelatedIDAndType(evidenceTypeKey);
    final long taskID =
      bdmoasTaskUtil.getTaskIdFromEvidenceID(legalevidenceKey.evidenceID);

    final EvidenceDescriptorKey key = new EvidenceDescriptorKey();
    key.evidenceDescriptorID = evidenceDescriptorDtls.evidenceDescriptorID;
    final VDIEDLinkAndDataItemIDDetailsList vdlinkDtils =
      VDIEDLinkFactory.newInstance().readByEvidenceDescriptor(key);
    final VDIEDLinkKey linkKey = new VDIEDLinkKey();
    linkKey.VDIEDLinkID = vdlinkDtils.dtls.get(0).vdIEDLinkID;
    final VerificationDtlsList list = VerificationFactory.newInstance()
      .searchActiveVerificationByVDIEDLinkID(linkKey);

    oasVerEventHandler
      .maintainVerificationSatisfied(list.dtls.get(0).verificationID, 0l);
    // Getting Task details
    final TaskAdmin taskAdminObj = TaskAdminFactory.newInstance();
    final TaskDetailsWithSnapshot taskDetailsWithSnapshot =
      taskAdminObj.readDetailsWithSnapshot(taskID);
    assertEquals(TASKSTATUS.CLOSED, taskDetailsWithSnapshot.status);

  }

  /**
   * Creates the evidence to Test.
   *
   * @param evidenceName the evidence name
   * @return the EI evidence key
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  private EIEvidenceKey createEvidence(final long integratedCase,
    final long casepartipantId, final String evidenceName,
    final Long concernRoleID) throws AppException, InformationalException {

    final Map<String, String> attributes = new HashMap<String, String>();
    EIEvidenceKey evidenceKey = new EIEvidenceKey();
    if (BDMOASLegalStatusConstants.LEGAL_STATUS.equals(evidenceName)) {
      attributes.put(BDMOASLegalStatusConstants.LEGAL_STATUS,
        BDMOASLEGALSTATUS.TEMPORARY_RESIDENT);
      attributes.put(BDMOASLegalStatusConstants.START_DATE, "20200101");
      attributes.put(BDMOASLegalStatusConstants.PARTICIPANT,
        String.valueOf(casepartipantId));

      evidenceKey = this.createEvidence(integratedCase, concernRoleID,
        CASEEVIDENCEEntry.OAS_LEGAL_STATUS, attributes, getToday());

    }
    return evidenceKey;

  }

  /**
   * Mock enactment service.
   */
  private void mockEnactmentService() {

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
  }
}
