package curam.ca.gc.bdmoas.evidence.firstEntryIntoCanada.impl;

import curam.advisor.facade.fact.AdvisorFactory;
import curam.advisor.facade.struct.AdviceKey;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdmoas.codetable.BDMOASIMMIGRATIONDOC;
import curam.ca.gc.bdmoas.codetable.impl.BDMOASIMMIGRATIONDOCEntry;
import curam.ca.gc.bdmoas.evidence.constants.impl.OASFirstEntryIntoCanadaDetailsConstants;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.ca.gc.bdmoas.impl.BDMOASConstants;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.PROVINCETYPE;
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
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test configured rules-based validations for First Entry Into Canada Details
 * Advisor
 * evidence.
 */
public class BDMOASFirstEntryIntoCanadaDetailsAdvisorTest
  extends BDMOASCaseTest {

  private static final String VANCOUVER = "Vancouver";

  @Override
  protected void setUpCuramServerTest() {

    super.setUpCuramServerTest();

  }

  @Before
  public void setUp() throws AppException, InformationalException {


  }

  /**
   * PASS-IF issue is not raised when document is provided.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void evidenceAdvisorIssueNotRaisedWhenDocuemntIsGiven()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final ApplicationCaseKey applicationCase = this.createApplicationCase(
      person.registrationIDDetails.concernRoleID, 920000);
    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(applicationCase.applicationCaseID,
        person.registrationIDDetails.concernRoleID);

    createFEICEvidence(applicationCase.applicationCaseID,
      cprObj.caseParticipantRoleID,
      person.registrationIDDetails.concernRoleID,
      Date.getCurrentDate().addDays(-100), Date.kZeroDate, VANCOUVER,
      BDMOASIMMIGRATIONDOCEntry.PERMANENTRESIDENT);

    // Apply Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = applicationCase.applicationCaseID;

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

  /**
   * PASS-IF issue is raised for evidence
   * if document is not provided.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void evidenceAdvisorIssueRaisedWhenDocumentIsBlank()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final ApplicationCaseKey applicationCase = this.createApplicationCase(
      person.registrationIDDetails.concernRoleID, 920000);
    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(applicationCase.applicationCaseID,
        person.registrationIDDetails.concernRoleID);

    createFEICEvidence(applicationCase.applicationCaseID,
      cprObj.caseParticipantRoleID,
      person.registrationIDDetails.concernRoleID,
      Date.getCurrentDate().addDays(-100),
      Date.getCurrentDate().addDays(-100), VANCOUVER,
      BDMOASIMMIGRATIONDOCEntry.NOT_SPECIFIED);

    // Apply Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = applicationCase.applicationCaseID;

    final AdviceKey adviceKey = new AdviceKey();

    adviceKey.adviceContextKeyName =
      BDMOASConstants.BDM_OAS_APPLICATIONCASE_HOME_PAGE_ID;
    adviceKey.parameters =
      BDMOASConstants.CASE_ID + applicationCase.applicationCaseID;

    final curam.advisor.facade.struct.AdviceDetails adviceDetails =
      AdvisorFactory.newInstance().getAdvice(adviceKey);

    assertTrue(
      adviceDetails.adviceXML.contains(BDMOASConstants.INCOMPLETE_DATA));
  }

  /**
   * PASS-IF evidence
   * issue is raised when arrival date is not provided.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void evidenceAdvisorIssueIsRaisedWhenArrivalDateIsBlank()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final ApplicationCaseKey applicationCase = this.createApplicationCase(
      person.registrationIDDetails.concernRoleID, 920000);
    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(applicationCase.applicationCaseID,
        person.registrationIDDetails.concernRoleID);

    createFEICEvidence(applicationCase.applicationCaseID,
      cprObj.caseParticipantRoleID,
      person.registrationIDDetails.concernRoleID, Date.kZeroDate, null,
      VANCOUVER, BDMOASIMMIGRATIONDOCEntry.PERMANENTRESIDENT);

    // Apply Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = applicationCase.applicationCaseID;

    final AdviceKey adviceKey = new AdviceKey();

    adviceKey.adviceContextKeyName =
      BDMOASConstants.BDM_OAS_APPLICATIONCASE_HOME_PAGE_ID;
    adviceKey.parameters =
      BDMOASConstants.CASE_ID + applicationCase.applicationCaseID;

    final curam.advisor.facade.struct.AdviceDetails adviceDetails =
      AdvisorFactory.newInstance().getAdvice(adviceKey);

    assertTrue(
      adviceDetails.adviceXML.contains(BDMOASConstants.INCOMPLETE_DATA));
  }

  /**
   * PASS-IF evidence
   * issue is not raised when arrival date and immigration doc fields are
   * populated.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void evidenceAdvisorIssueIsNotRaisedWhenArrivalDateAndDocAreGiven()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final ApplicationCaseKey applicationCase = this.createApplicationCase(
      person.registrationIDDetails.concernRoleID, 920000);
    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(applicationCase.applicationCaseID,
        person.registrationIDDetails.concernRoleID);

    createFEICEvidence(applicationCase.applicationCaseID,
      cprObj.caseParticipantRoleID,
      person.registrationIDDetails.concernRoleID,
      Date.getCurrentDate().addDays(-25), Date.kZeroDate, VANCOUVER,
      BDMOASIMMIGRATIONDOCEntry.PERMANENTRESIDENT);

    // Apply Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = applicationCase.applicationCaseID;

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

  private curam.core.sl.struct.EvidenceKey createFEICEvidence(
    final Long caseID, final Long cprID, final long concernroleID,
    final Date arrivalDate, final Date endDate, final String arrivalCity,
    final BDMOASIMMIGRATIONDOCEntry immDocEntry)
    throws AppException, InformationalException {

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.OAS_FIRST_ENTRY_INTO_CANADA;

    // get Latest Version of Evidence
    final GenericSLDataDetails genericDtls = new GenericSLDataDetails();
    final EvidenceServiceInterface evidenceServiceInterface =
      EvidenceGenericSLFactory.instance(eType, Date.getCurrentDate());

    final DynamicEvidenceDataDetails dynamicEvidencedataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(eType.evidenceType,
        Date.getCurrentDate());

    // assigning all the required attributes
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(
        OASFirstEntryIntoCanadaDetailsConstants.CASE_PARTICIPANT_ROLE),
      cprID);
    DynamicEvidenceTypeConverter.setAttribute(dynamicEvidencedataDetails
      .getAttribute(OASFirstEntryIntoCanadaDetailsConstants.ARRIVAL_DATE),
      arrivalDate);
    DynamicEvidenceTypeConverter.setAttribute(dynamicEvidencedataDetails
      .getAttribute(OASFirstEntryIntoCanadaDetailsConstants.ARRIVAL_CITY),
      arrivalCity);

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(
        OASFirstEntryIntoCanadaDetailsConstants.IMMIGRATION_DOC),
      new CodeTableItem(BDMOASIMMIGRATIONDOC.TABLENAME,
        immDocEntry.getCode()));

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(
        OASFirstEntryIntoCanadaDetailsConstants.ARRIVAL_PROVINCE),
      new CodeTableItem(PROVINCETYPE.TABLENAME,
        PROVINCETYPE.BRITISHCOLUMBIA));

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
    genericDtls.addRelCp(
      OASFirstEntryIntoCanadaDetailsConstants.CASE_PARTICIPANT_ROLE,
      cpAdaptor);

    return evidenceServiceInterface.createEvidence(genericDtls).evidenceKey;
  }

}
