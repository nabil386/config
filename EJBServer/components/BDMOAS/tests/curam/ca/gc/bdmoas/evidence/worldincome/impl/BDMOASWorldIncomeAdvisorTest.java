package curam.ca.gc.bdmoas.evidence.worldincome.impl;

import curam.advisor.facade.fact.AdvisorFactory;
import curam.advisor.facade.struct.AdviceKey;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdmoas.codetable.BDMOASTAXTHRESHOLD;
import curam.ca.gc.bdmoas.codetable.BDMOASYESNONA;
import curam.ca.gc.bdmoas.codetable.impl.BDMOASYESNONAEntry;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.ca.gc.bdmoas.impl.BDMOASConstants;
import curam.codetable.CASEEVIDENCE;
import curam.commonintake.entity.struct.ApplicationCaseKey;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.core.sl.struct.EvidenceDescriptorDetails;
import curam.core.sl.struct.EvidenceTypeKey;
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

/**
 * Tests for Issue Advisor for BDMOASWorldIncome evidence.
 */
public class BDMOASWorldIncomeAdvisorTest extends BDMOASCaseTest {

  @Override
  protected void setUpCuramServerTest() {

    super.setUpCuramServerTest();
  }

  /**
   * PASS-IF an advise issue does not get raised when overThreshold is
   * specified.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void
    advisorIssueNotCreatedWhenOverThresholdIsProvidedForWorldIncomeEvidence()
      throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final ApplicationCaseKey applicationCase = this.createApplicationCase(
      person.registrationIDDetails.concernRoleID, 920000);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(applicationCase.applicationCaseID,
        person.registrationIDDetails.concernRoleID);

    createWorldIncomeEvidence(applicationCase.applicationCaseID,
      cprObj.caseParticipantRoleID,
      person.registrationIDDetails.concernRoleID, BDMOASYESNONAEntry.NA);

    final AdviceKey adviceKey = new AdviceKey();

    adviceKey.adviceContextKeyName =
      BDMOASConstants.BDM_OAS_APPLICATIONCASE_HOME_PAGE_ID;
    adviceKey.parameters =
      BDMOASConstants.CASE_ID + applicationCase.applicationCaseID;

    final curam.advisor.facade.struct.AdviceDetails adviceDetails =
      AdvisorFactory.newInstance().getAdvice(adviceKey);

    assertTrue(
      !adviceDetails.adviceXML.contains(BDMOASConstants.INCOMPLETE_DATA));

  }

  private curam.core.sl.struct.EvidenceKey createWorldIncomeEvidence(
    final Long caseID, final Long cprID, final long concernroleID,
    final BDMOASYESNONAEntry yesNoEntry)
    throws AppException, InformationalException {

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.OAS_WORLD_INCOME;

    // get Latest Version of Evidence
    final GenericSLDataDetails genericDtls = new GenericSLDataDetails();
    final EvidenceServiceInterface evidenceServiceInterface =
      EvidenceGenericSLFactory.instance(eType, Date.getCurrentDate());

    final DynamicEvidenceDataDetails dynamicEvidencedataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(eType.evidenceType,
        Date.getCurrentDate());

    // assigning all the required attributes
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute("participant"), cprID);
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute("threshold"), new CodeTableItem(
        BDMOASTAXTHRESHOLD.TABLENAME, BDMOASTAXTHRESHOLD.Y2021));
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute("overThreshold"),
      new CodeTableItem(BDMOASYESNONA.TABLENAME, yesNoEntry.getCode()));

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
    genericDtls.addRelCp("participant", cpAdaptor);

    return evidenceServiceInterface.createEvidence(genericDtls).evidenceKey;
  }

}
