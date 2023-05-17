package curam.ca.gc.bdmoas.evidence.residenceperiod.impl;

import curam.advisor.facade.fact.AdvisorFactory;
import curam.advisor.facade.struct.AdviceKey;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdmoas.codetable.OASRESIDENCETYPE;
import curam.ca.gc.bdmoas.codetable.impl.OASRESIDENCETYPEEntry;
import curam.ca.gc.bdmoas.evidence.constants.impl.OASResidencePeriodConstants;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.ca.gc.bdmoas.impl.BDMOASConstants;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.COUNTRY;
import curam.commonintake.entity.struct.ApplicationCaseKey;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.core.sl.struct.EvidenceDescriptorDetails;
import curam.core.sl.struct.EvidenceTypeKey;
import curam.core.struct.CaseKey;
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
import org.junit.Test;
import static org.junit.Assert.assertTrue;

// 64790: Residence Period
/**
 * Test validations for Residence Period Application Case Evidence Advisor.
 */

public class BDMOASResidencePeriodAdvisorTest extends BDMOASCaseTest {

  @Override
  protected void setUpCuramServerTest() {

    super.setUpCuramServerTest();

  }

  /**
   * PASS-IF an advisor issue is raised when residence type is blank.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void advisorIssueRaisedWhenResidenceTypeIsBlank()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();

    final ApplicationCaseKey applicationCase2 = this.createApplicationCase(
      person.registrationIDDetails.concernRoleID, 920000);

    final CaseParticipantRoleIDStruct cprObj = new BDMUtil()
      .getCaseParticipantRoleID(applicationCase2.applicationCaseID,
        person.registrationIDDetails.concernRoleID);

    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = applicationCase2.applicationCaseID;

    createResidencePeriodEvidence(applicationCase2.applicationCaseID,
      cprObj.caseParticipantRoleID,
      person.registrationIDDetails.concernRoleID,
      Date.getCurrentDate().addDays(-100), null,
      OASRESIDENCETYPEEntry.CREDITABLE);

    final AdviceKey adviceKey = new AdviceKey();

    adviceKey.adviceContextKeyName =
      BDMOASConstants.BDM_OAS_APPLICATIONCASE_HOME_PAGE_ID;
    adviceKey.parameters =
      BDMOASConstants.CASE_ID + applicationCase2.applicationCaseID;

    final curam.advisor.facade.struct.AdviceDetails adviceDetails =
      AdvisorFactory.newInstance().getAdvice(adviceKey);

    assertTrue(
      !adviceDetails.adviceXML.contains(BDMOASConstants.INCOMPLETE_DATA));
  }

  /**
   * PASS-IF an advisor issue is not raised when residence type is provided.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void advisorIssueNotRaisedWhenResidenceTypeIsProvided()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();

    final ApplicationCaseKey applicationCase2 = this.createApplicationCase(
      person.registrationIDDetails.concernRoleID, 920000);

    final CaseParticipantRoleIDStruct cprObj = new BDMUtil()
      .getCaseParticipantRoleID(applicationCase2.applicationCaseID,
        person.registrationIDDetails.concernRoleID);

    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = applicationCase2.applicationCaseID;

    createResidencePeriodEvidence(applicationCase2.applicationCaseID,
      cprObj.caseParticipantRoleID,
      person.registrationIDDetails.concernRoleID,
      Date.getCurrentDate().addDays(-100), null,
      OASRESIDENCETYPEEntry.PRESENCE);

    final AdviceKey adviceKey = new AdviceKey();

    adviceKey.adviceContextKeyName =
      BDMOASConstants.BDM_OAS_APPLICATIONCASE_HOME_PAGE_ID;
    adviceKey.parameters =
      BDMOASConstants.CASE_ID + applicationCase2.applicationCaseID;

    final curam.advisor.facade.struct.AdviceDetails adviceDetails =
      AdvisorFactory.newInstance().getAdvice(adviceKey);

    assertTrue(
      !adviceDetails.adviceXML.contains(BDMOASConstants.INCOMPLETE_DATA));

  }

  private curam.core.sl.struct.EvidenceKey createResidencePeriodEvidence(
    final Long caseID, final Long cprID, final long concernroleID,
    final Date startDate, final Date endDate,
    final OASRESIDENCETYPEEntry residenceTypeEntry)
    throws AppException, InformationalException {

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.OAS_RESIDENCE_PERIOD;

    // get Latest Version of Evidence
    final GenericSLDataDetails genericDtls = new GenericSLDataDetails();
    final EvidenceServiceInterface evidenceServiceInterface =
      EvidenceGenericSLFactory.instance(eType, Date.getCurrentDate());

    final DynamicEvidenceDataDetails dynamicEvidencedataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(eType.evidenceType,
        Date.getCurrentDate());
    // assigning all the required attributes
    DynamicEvidenceTypeConverter.setAttribute(dynamicEvidencedataDetails
      .getAttribute(OASResidencePeriodConstants.CASE_PARTICIPANT_ROLE),
      cprID);
    DynamicEvidenceTypeConverter.setAttribute(dynamicEvidencedataDetails
      .getAttribute(OASResidencePeriodConstants.START_DATE), startDate);
    DynamicEvidenceTypeConverter.setAttribute(dynamicEvidencedataDetails
      .getAttribute(OASResidencePeriodConstants.END_DATE), endDate);
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails
        .getAttribute(OASResidencePeriodConstants.COUNTRY),
      new CodeTableItem(COUNTRY.TABLENAME, COUNTRY.CA));

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails
        .getAttribute(OASResidencePeriodConstants.RESIDENCE_TYPE),
      new CodeTableItem(OASRESIDENCETYPE.TABLENAME,
        residenceTypeEntry.getCode()));

    // new CodeTableItem(BDMRESIDENCETYPE.TABLENAME,
    // BDMRESIDENCETYPE.RESIDENCE)

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
    genericDtls.addRelCp(OASResidencePeriodConstants.CASE_PARTICIPANT_ROLE,
      cpAdaptor);

    return evidenceServiceInterface.createEvidence(genericDtls).evidenceKey;
  }

}
