package curam.ca.gc.bdmoas.evidence.legalstatus.impl;

import curam.advisor.facade.fact.AdvisorFactory;
import curam.advisor.facade.struct.AdviceKey;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdmoas.codetable.BDMOASLEGALSTATUS;
import curam.ca.gc.bdmoas.evidence.constants.impl.BDMOASLegalStatusConstants;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.ca.gc.bdmoas.impl.BDMOASConstants;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASLegalStatusEvidence;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASLegalStatusEvidenceVO;
import curam.codetable.CASEEVIDENCE;
import curam.commonintake.entity.struct.ApplicationCaseKey;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.core.sl.struct.EvidenceDescriptorDetails;
import curam.core.sl.struct.EvidenceTypeKey;
import curam.creole.calculator.CREOLETestHelper;
import curam.creole.value.CodeTableItem;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.dynamicevidence.sl.impl.CpDetailsAdaptor;
import curam.dynamicevidence.sl.impl.EvidenceGenericSLFactory;
import curam.dynamicevidence.sl.impl.EvidenceServiceInterface;
import curam.dynamicevidence.sl.struct.impl.GenericSLDataDetails;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test validations for OASLegalStatus evidence.
 */
public class BDMOASLegalStatusAdvisorTest extends BDMOASCaseTest {

  @Override
  protected void setUpCuramServerTest() {

    super.setUpCuramServerTest();

  }

  /**
   * PASS-IF an advise issue is raised when start date is blank.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void advisorIssueRaisedWhenStartDateIsBlankForLegalStatusEvidence()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final ApplicationCaseKey applicationCase = this.createApplicationCase(
      person.registrationIDDetails.concernRoleID, 920000);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(applicationCase.applicationCaseID,
        person.registrationIDDetails.concernRoleID);

    createLegalStatusEvidence(applicationCase.applicationCaseID,
      cprObj.caseParticipantRoleID,
      person.registrationIDDetails.concernRoleID, Date.kZeroDate, null);

    final List<BDMOASLegalStatusEvidenceVO> legalStatusEvidenceValueObject =
      new BDMOASLegalStatusEvidence().getLegalStatusEvidenceValueObject(
        person.registrationIDDetails.concernRoleID);

    CREOLETestHelper.assertEquals(1, legalStatusEvidenceValueObject.size());

    final AdviceKey adviceKey = new AdviceKey();

    adviceKey.adviceContextKeyName =
      BDMOASConstants.BDM_OAS_APPLICATIONCASE_HOME_PAGE_ID;
    adviceKey.parameters =
      BDMOASConstants.CASE_ID + applicationCase.applicationCaseID;

    final curam.advisor.facade.struct.AdviceDetails adviceDetails =
      AdvisorFactory.newInstance().getAdvice(adviceKey);

    final String issueText = BDMOASConstants.INCOMPLETE_DATA;

    assertTrue(adviceDetails.adviceXML.contains(issueText));

  }

  /**
   * PASS-IF an advise issue is not raised when non-zero start date is provided.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void
    advisorIssueNotRaisedWhenStartDateNotBlankForLegalStatusEvidence()
      throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final ApplicationCaseKey applicationCase = this.createApplicationCase(
      person.registrationIDDetails.concernRoleID, 920000);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(applicationCase.applicationCaseID,
        person.registrationIDDetails.concernRoleID);

    createLegalStatusEvidence(applicationCase.applicationCaseID,
      cprObj.caseParticipantRoleID,
      person.registrationIDDetails.concernRoleID,
      Date.getCurrentDate().addDays(-10), null);

    final List<BDMOASLegalStatusEvidenceVO> legalStatusEvidenceValueObject =
      new BDMOASLegalStatusEvidence().getLegalStatusEvidenceValueObject(
        person.registrationIDDetails.concernRoleID);

    CREOLETestHelper.assertEquals(1, legalStatusEvidenceValueObject.size());

    final AdviceKey adviceKey = new AdviceKey();

    adviceKey.adviceContextKeyName =
      BDMOASConstants.BDM_OAS_APPLICATIONCASE_HOME_PAGE_ID;
    adviceKey.parameters =
      BDMOASConstants.CASE_ID + applicationCase.applicationCaseID;

    final curam.advisor.facade.struct.AdviceDetails adviceDetails =
      AdvisorFactory.newInstance().getAdvice(adviceKey);

    final String issueText = BDMOASConstants.INCOMPLETE_DATA;

    assertTrue(!adviceDetails.adviceXML.contains(issueText));

  }

  private curam.core.sl.struct.EvidenceKey createLegalStatusEvidence(
    final Long caseID, final Long cprID, final long concernroleID,
    final Date startDate, final Date endDate)
    throws AppException, InformationalException {

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.OAS_LEGAL_STATUS;

    // get Latest Version of Evidence
    final GenericSLDataDetails genericDtls = new GenericSLDataDetails();
    final EvidenceServiceInterface evidenceServiceInterface =
      EvidenceGenericSLFactory.instance(eType, Date.getCurrentDate());

    final DynamicEvidenceDataDetails dynamicEvidencedataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(eType.evidenceType,
        Date.getCurrentDate());
    // assigning all the required attributes
    DynamicEvidenceTypeConverter.setAttribute(dynamicEvidencedataDetails
      .getAttribute(BDMOASLegalStatusConstants.PARTICIPANT), cprID);
    DynamicEvidenceTypeConverter.setAttribute(dynamicEvidencedataDetails
      .getAttribute(BDMOASLegalStatusConstants.START_DATE), startDate);
    DynamicEvidenceTypeConverter.setAttribute(dynamicEvidencedataDetails
      .getAttribute(BDMOASLegalStatusConstants.END_DATE), endDate);

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails
        .getAttribute(BDMOASLegalStatusConstants.LEGAL_STATUS),
      new CodeTableItem(BDMOASLEGALSTATUS.TABLENAME,
        BDMOASLEGALSTATUS.CANADIAN_CITIZEN));

    // set descriptor attributes to call OOTB Evidence creation logic
    final EvidenceDescriptorDetails descriptor =
      new EvidenceDescriptorDetails();
    descriptor.evidenceType = eType.evidenceType;
    descriptor.caseID = caseID;
    descriptor.receivedDate = Date.getCurrentDate();
    descriptor.participantID = concernroleID;

    genericDtls.setData(dynamicEvidencedataDetails);
    genericDtls.setDescriptor(descriptor);
    genericDtls.setCaseIdKey(descriptor.caseID);

    final CpDetailsAdaptor cpAdaptor = new CpDetailsAdaptor();
    cpAdaptor.setCaseParticipantRoleID(cprID);
    genericDtls.addRelCp(BDMOASLegalStatusConstants.PARTICIPANT, cpAdaptor);

    return evidenceServiceInterface.createEvidence(genericDtls).evidenceKey;
  }

}
