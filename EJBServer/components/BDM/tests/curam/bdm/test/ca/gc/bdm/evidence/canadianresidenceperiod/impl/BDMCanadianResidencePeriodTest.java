package curam.bdm.test.ca.gc.bdm.evidence.canadianresidenceperiod.impl;

import curam.advisor.entity.fact.AdviceCaseIssueFactory;
import curam.advisor.entity.intf.AdviceCaseIssue;
import curam.advisor.entity.struct.AdviceCaseIssueDtlsList;
import curam.bdm.test.ca.gc.bdm.evidence.foreigncontributionperiod.impl.BDMForeignContributionPeriodTest;
import curam.bdm.test.ca.gc.bdm.evidence.foreignresidenceperiod.impl.BDMForeignResidencePeriodTest;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMISSUERESOLUTION;
import curam.ca.gc.bdm.codetable.BDMRESIDENCETYPE;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.facade.fec.fact.BDMForeignEngagementCaseFactory;
import curam.ca.gc.bdm.facade.fec.intf.BDMForeignEngagementCase;
import curam.ca.gc.bdm.facade.fec.struct.BDMFECaseDetails;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASEPRIORITY;
import curam.codetable.impl.EVIDENCECHANGEREASONEntry;
import curam.core.facade.infrastructure.struct.CaseIDAndEvidenceTypeKey;
import curam.core.facade.struct.CreateIntegratedCaseResultAndMessages;
import curam.core.impl.CuramConst;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceModifyDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.impl.EvidenceMap;
import curam.core.sl.infrastructure.impl.StandardEvidenceInterface;
import curam.core.sl.infrastructure.struct.ECActiveEvidenceDtls;
import curam.core.sl.infrastructure.struct.ECActiveEvidenceDtlsList;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.core.sl.struct.EvidenceDescriptorDetails;
import curam.core.sl.struct.EvidenceKey;
import curam.core.sl.struct.EvidenceTypeKey;
import curam.core.struct.CaseKey;
import curam.creole.value.CodeTableItem;
import curam.dynamicevidence.impl.DynamicEvidenceDataAttributeDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.dynamicevidence.sl.impl.CpDetailsAdaptor;
import curam.dynamicevidence.sl.impl.EvidenceGenericSLFactory;
import curam.dynamicevidence.sl.impl.EvidenceServiceInterface;
import curam.dynamicevidence.sl.struct.impl.GenericSLDataDetails;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test Canadian Residence Period evidence business validations and Evidence
 * Issues.
 *
 * 90018 DEV: Evidence- creditable residency period in Canada
 */
public class BDMCanadianResidencePeriodTest
  extends curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest {

  private static final String CASE_PARTICIPANT_ROLE = "caseParticipantRole";

  private static final String START_DATE = "startDate";

  private static final String END_DATE = "endDate";

  private static final String RESIDENCE_TYPE = "residenceType";

  private static final String RESOLUTION = "resolution";

  private static final String ERR_START_DATE_BEFORE_END_DATE =
    "‘Start Date’ must be before ‘End Date’.";

  private static final String ERR_OVERLAPPING_CANADIAN_RESIDENCE_PERIOD =
    "A Canadian Residence Period record already exists for an overlapping period.";

  private static final String ERR_FUTURE_END_DATE_DISALLOWED =
    "‘End Date’ must not be a future date.";

  private static final String ERR_NO_ASSOCIATED_ISSUE =
    "Evidence record has no associated Issue. Set Resolution value to blank.";

  private static final String ERR_RESOLVE_ASSOCIATED_ISSUE =
    "Evidence record cannot be activated. Please resolve associated issue.";

  private final BDMForeignEngagementCase kBdmForeignEngagementCaseObj =
    BDMForeignEngagementCaseFactory.newInstance();

  /**
   * PASS-IF a validation fails when Start Date is after End Date
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void startDateBeforeEndDateValidationFailure()
    throws AppException, InformationalException {

    // Register Person
    final PDCPersonDetails pdcPersonDetails = this.createPDCPerson();

    // Create FEC
    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages;

    final BDMFECaseDetails details = new BDMFECaseDetails();
    details.concernRoleID = pdcPersonDetails.concernRoleID;
    details.countryCode = BDMSOURCECOUNTRY.US;
    details.priorityCode = CASEPRIORITY.HIGH;

    createIntegratedCaseResultAndMessages =
      kBdmForeignEngagementCaseObj.createFEIntegratedCase(details);

    final Long integratedCaseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj = util.getCaseParticipantRoleID(
      integratedCaseID, pdcPersonDetails.concernRoleID);

    // Create evidence
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        createCanadianResidencePeriodEvidence(integratedCaseID,
          cprObj.caseParticipantRoleID, pdcPersonDetails.concernRoleID,
          Date.fromISO8601("20200101"), Date.fromISO8601("20191231"),
          CuramConst.gkEmpty);
      });

    // Assert valid exception message thrown
    assertTrue(
      exception.getMessage().contains(ERR_START_DATE_BEFORE_END_DATE));
  }

  /**
   * PASS-IF a validation fails when End Date is in future.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void futureEndDateValidationFailure()
    throws AppException, InformationalException {

    // Register Person
    final PDCPersonDetails pdcPersonDetails = this.createPDCPerson();

    // Create FEC
    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages;

    final BDMFECaseDetails details = new BDMFECaseDetails();
    details.concernRoleID = pdcPersonDetails.concernRoleID;
    details.countryCode = BDMSOURCECOUNTRY.US;
    details.priorityCode = CASEPRIORITY.HIGH;

    createIntegratedCaseResultAndMessages =
      kBdmForeignEngagementCaseObj.createFEIntegratedCase(details);

    final Long integratedCaseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj = util.getCaseParticipantRoleID(
      integratedCaseID, pdcPersonDetails.concernRoleID);

    // Create evidence
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        createCanadianResidencePeriodEvidence(integratedCaseID,
          cprObj.caseParticipantRoleID, pdcPersonDetails.concernRoleID,
          Date.fromISO8601("20200101"), Date.getCurrentDate().addDays(1),
          CuramConst.gkEmpty);
      });

    // Assert valid exception message thrown
    assertTrue(
      exception.getMessage().contains(ERR_FUTURE_END_DATE_DISALLOWED));
  }

  /**
   * PASS-IF a validation fails when there is an overlap in periods
   * of evidence records.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void canadianResidencePeriodOverlapFailure()
    throws AppException, InformationalException {

    // Register Person
    final PDCPersonDetails pdcPersonDetails = this.createPDCPerson();

    // Create FEC
    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages;

    final BDMFECaseDetails details = new BDMFECaseDetails();
    details.concernRoleID = pdcPersonDetails.concernRoleID;
    details.countryCode = BDMSOURCECOUNTRY.US;
    details.priorityCode = CASEPRIORITY.HIGH;

    createIntegratedCaseResultAndMessages =
      kBdmForeignEngagementCaseObj.createFEIntegratedCase(details);

    final Long integratedCaseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj = util.getCaseParticipantRoleID(
      integratedCaseID, pdcPersonDetails.concernRoleID);

    // Create first evidence record
    createCanadianResidencePeriodEvidence(integratedCaseID,
      cprObj.caseParticipantRoleID, pdcPersonDetails.concernRoleID,
      Date.fromISO8601("20200101"), Date.fromISO8601("20201231"),
      CuramConst.gkEmpty);

    // Apply First Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    evidenceController.applyAllChanges(caseKey);

    // Create overlapping evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        createCanadianResidencePeriodEvidence(integratedCaseID,
          cprObj.caseParticipantRoleID, pdcPersonDetails.concernRoleID,
          Date.fromISO8601("20200601"), Date.fromISO8601("20210531"),
          CuramConst.gkEmpty);
      });

    // Assert valid exception message thrown
    assertTrue(exception.getMessage()
      .contains(ERR_OVERLAPPING_CANADIAN_RESIDENCE_PERIOD));
  }

  /**
   * PASS-IF a validation doesn't fail and evidence records activated when there
   * is no overlap in periods of evidence records.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void canadianResidencePeriodOverlapSuccess()
    throws AppException, InformationalException {

    // Register Person
    final PDCPersonDetails pdcPersonDetails = this.createPDCPerson();

    // Create FEC
    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages;

    final BDMFECaseDetails details = new BDMFECaseDetails();
    details.concernRoleID = pdcPersonDetails.concernRoleID;
    details.countryCode = BDMSOURCECOUNTRY.US;
    details.priorityCode = CASEPRIORITY.HIGH;

    createIntegratedCaseResultAndMessages =
      kBdmForeignEngagementCaseObj.createFEIntegratedCase(details);

    final Long integratedCaseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj = util.getCaseParticipantRoleID(
      integratedCaseID, pdcPersonDetails.concernRoleID);

    // Create first evidence record
    final EvidenceKey evidenceKey1 = createCanadianResidencePeriodEvidence(
      integratedCaseID, cprObj.caseParticipantRoleID,
      pdcPersonDetails.concernRoleID, Date.fromISO8601("20200101"),
      Date.fromISO8601("20201231"), CuramConst.gkEmpty);

    // Create overlapping evidence record
    final EvidenceKey evidenceKey2 = createCanadianResidencePeriodEvidence(
      integratedCaseID, cprObj.caseParticipantRoleID,
      pdcPersonDetails.concernRoleID, Date.fromISO8601("20210101"),
      Date.fromISO8601("20210531"), CuramConst.gkEmpty);

    // Apply Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    evidenceController.applyAllChanges(caseKey);

    // Check if the two foreign residence period records are activated
    final ECActiveEvidenceDtlsList activeEvdList =
      evidenceController.listActive(caseKey);

    // Assert first record activated
    final List<ECActiveEvidenceDtls> filteredList1 = activeEvdList.dtls
      .stream().filter((x) -> x.evidenceID == evidenceKey1.evidenceID)
      .collect(Collectors.toList());
    assertTrue(filteredList1.size() == 1);

    // Assert second record activated
    final List<ECActiveEvidenceDtls> filteredList2 = activeEvdList.dtls
      .stream().filter((x) -> x.evidenceID == evidenceKey2.evidenceID)
      .collect(Collectors.toList());
    assertTrue(filteredList2.size() == 1);
  }

  /**
   * PASS-IF Evidence Issue is created when Canadian Residence Period overlaps
   * with active foreign residence period.
   */
  @Test
  public void foreignResidenceOverlapIssueCreationSuccess()
    throws AppException, InformationalException {

    // Register Person
    final PDCPersonDetails pdcPersonDetails = this.createPDCPerson();

    // Create FEC
    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages;

    final BDMFECaseDetails details = new BDMFECaseDetails();
    details.concernRoleID = pdcPersonDetails.concernRoleID;
    details.countryCode = BDMSOURCECOUNTRY.US;
    details.priorityCode = CASEPRIORITY.HIGH;

    createIntegratedCaseResultAndMessages =
      kBdmForeignEngagementCaseObj.createFEIntegratedCase(details);

    final Long integratedCaseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj = util.getCaseParticipantRoleID(
      integratedCaseID, pdcPersonDetails.concernRoleID);

    final BDMForeignResidencePeriodTest foreignResidencePeriodTest =
      new BDMForeignResidencePeriodTest();

    // Create Foreign Residence Period
    foreignResidencePeriodTest.createForeignResidencePeriodEvidence(
      integratedCaseID, cprObj.caseParticipantRoleID,
      pdcPersonDetails.concernRoleID, Date.fromISO8601("20220101"),
      Date.fromISO8601("20220630"));

    // Apply Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    evidenceController.applyAllChanges(caseKey);

    // Create overlapping Canadian Residence Period
    final EvidenceKey canadianResidencePeriodEvd =
      createCanadianResidencePeriodEvidence(integratedCaseID,
        cprObj.caseParticipantRoleID, pdcPersonDetails.concernRoleID,
        Date.fromISO8601("20220601"), Date.fromISO8601("20220831"),
        CuramConst.gkEmpty);

    // Check for creation of evidence issue
    final AdviceCaseIssue adviceCaseIssueObj =
      AdviceCaseIssueFactory.newInstance();

    final CaseIDAndEvidenceTypeKey adviceCaseIssueKey =
      new CaseIDAndEvidenceTypeKey();
    adviceCaseIssueKey.caseID = integratedCaseID;
    adviceCaseIssueKey.evidenceType =
      CASEEVIDENCE.BDM_CANADIAN_RESIDENCE_PERIOD;
    final AdviceCaseIssueDtlsList adviceCaseIssueDtls =
      adviceCaseIssueObj.searchByCaseIDAndEvidenceType(adviceCaseIssueKey);

    assertTrue(adviceCaseIssueDtls.dtls.stream()
      .anyMatch(x -> x.evidenceID == canadianResidencePeriodEvd.evidenceID));
  }

  /**
   * PASS-IF Evidence Issue is not created and issue resolution not needed
   * validation is thrown when Canadian Residence Period does
   * not overlap with active foreign residence period.
   */
  @Test
  public void noAssociatedIssueValidationFailure()
    throws AppException, InformationalException {

    // Register Person
    final PDCPersonDetails pdcPersonDetails = this.createPDCPerson();

    // Create FEC
    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages;

    final BDMFECaseDetails details = new BDMFECaseDetails();
    details.concernRoleID = pdcPersonDetails.concernRoleID;
    details.countryCode = BDMSOURCECOUNTRY.US;
    details.priorityCode = CASEPRIORITY.HIGH;

    createIntegratedCaseResultAndMessages =
      kBdmForeignEngagementCaseObj.createFEIntegratedCase(details);

    final Long integratedCaseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj = util.getCaseParticipantRoleID(
      integratedCaseID, pdcPersonDetails.concernRoleID);

    final BDMForeignResidencePeriodTest foreignResidencePeriodTest =
      new BDMForeignResidencePeriodTest();

    // Create Foreign Residence Period
    foreignResidencePeriodTest.createForeignResidencePeriodEvidence(
      integratedCaseID, cprObj.caseParticipantRoleID,
      pdcPersonDetails.concernRoleID, Date.fromISO8601("20220101"),
      Date.fromISO8601("20220531"));

    // Apply Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    evidenceController.applyAllChanges(caseKey);

    // Create non-overlapping Canadian Residence Period
    final EvidenceKey canadianResidencePeriodEvd =
      createCanadianResidencePeriodEvidence(integratedCaseID,
        cprObj.caseParticipantRoleID, pdcPersonDetails.concernRoleID,
        Date.fromISO8601("20220601"), Date.fromISO8601("20220831"),
        CuramConst.gkEmpty);

    // modify canadian Residence Period evidence to include the resolution
    final HashMap<String, Object> attributes = new HashMap<String, Object>();
    attributes.put(RESOLUTION, new CodeTableItem(BDMISSUERESOLUTION.TABLENAME,
      BDMISSUERESOLUTION.ACCEPTABLE));

    final Exception exception =
      Assert.assertThrows(AppException.class, () -> {
        modifyCanadianResidencePeriodEvidence(integratedCaseID,
          canadianResidencePeriodEvd.evidenceID, attributes);
      });

    // Assert valid exception message thrown
    assertTrue(exception.getMessage().contains(ERR_NO_ASSOCIATED_ISSUE));

    // Check for creation of evidence issue
    final AdviceCaseIssue adviceCaseIssueObj =
      AdviceCaseIssueFactory.newInstance();

    final CaseIDAndEvidenceTypeKey adviceCaseIssueKey =
      new CaseIDAndEvidenceTypeKey();
    adviceCaseIssueKey.caseID = integratedCaseID;
    adviceCaseIssueKey.evidenceType =
      CASEEVIDENCE.BDM_CANADIAN_RESIDENCE_PERIOD;
    final AdviceCaseIssueDtlsList adviceCaseIssueDtls =
      adviceCaseIssueObj.searchByCaseIDAndEvidenceType(adviceCaseIssueKey);

    // Assert no evidence issue was created
    assertTrue(adviceCaseIssueDtls.dtls.size() == 0);
  }

  /**
   * PASS-IF Evidence Issue is created when Canadian Residence Period overlaps
   * with active foreign contribution period.
   */
  @Test
  public void foreignContributionOverlapIssueCreationSuccess()
    throws AppException, InformationalException {

    // Register Person
    final PDCPersonDetails pdcPersonDetails = this.createPDCPerson();

    // Create FEC
    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages;

    final BDMFECaseDetails details = new BDMFECaseDetails();
    details.concernRoleID = pdcPersonDetails.concernRoleID;
    details.countryCode = BDMSOURCECOUNTRY.US;
    details.priorityCode = CASEPRIORITY.HIGH;

    createIntegratedCaseResultAndMessages =
      kBdmForeignEngagementCaseObj.createFEIntegratedCase(details);

    final Long integratedCaseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj = util.getCaseParticipantRoleID(
      integratedCaseID, pdcPersonDetails.concernRoleID);

    final BDMForeignContributionPeriodTest foreignContributionPeriodTest =
      new BDMForeignContributionPeriodTest();

    // Create Foreign Contribution Period
    foreignContributionPeriodTest.createForeignContributionPeriodEvidence(
      integratedCaseID, cprObj.caseParticipantRoleID,
      pdcPersonDetails.concernRoleID, Date.fromISO8601("20220101"),
      Date.fromISO8601("20220630"));

    // Apply Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    evidenceController.applyAllChanges(caseKey);

    // Create overlapping Canadian Residence Period
    final EvidenceKey canadianResidencePeriodEvd =
      createCanadianResidencePeriodEvidence(integratedCaseID,
        cprObj.caseParticipantRoleID, pdcPersonDetails.concernRoleID,
        Date.fromISO8601("20220601"), Date.fromISO8601("20220831"),
        CuramConst.gkEmpty);

    // Check for creation of evidence issue
    final AdviceCaseIssue adviceCaseIssueObj =
      AdviceCaseIssueFactory.newInstance();

    final CaseIDAndEvidenceTypeKey adviceCaseIssueKey =
      new CaseIDAndEvidenceTypeKey();
    adviceCaseIssueKey.caseID = integratedCaseID;
    adviceCaseIssueKey.evidenceType =
      CASEEVIDENCE.BDM_CANADIAN_RESIDENCE_PERIOD;
    final AdviceCaseIssueDtlsList adviceCaseIssueDtls =
      adviceCaseIssueObj.searchByCaseIDAndEvidenceType(adviceCaseIssueKey);

    assertTrue(adviceCaseIssueDtls.dtls.stream()
      .anyMatch(x -> x.evidenceID == canadianResidencePeriodEvd.evidenceID));
  }

  /**
   * PASS-IF Evidence Issue is created but no issue resolution added to the
   * evidence and validation is thrown when activating Canadian residence period
   * evidence with overlap.
   */
  @Test
  public void resolveAssociatedIssueFailure()
    throws AppException, InformationalException {

    // Register Person
    final PDCPersonDetails pdcPersonDetails = this.createPDCPerson();

    // Create FEC
    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages;

    final BDMFECaseDetails details = new BDMFECaseDetails();
    details.concernRoleID = pdcPersonDetails.concernRoleID;
    details.countryCode = BDMSOURCECOUNTRY.US;
    details.priorityCode = CASEPRIORITY.HIGH;

    createIntegratedCaseResultAndMessages =
      kBdmForeignEngagementCaseObj.createFEIntegratedCase(details);

    final Long integratedCaseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj = util.getCaseParticipantRoleID(
      integratedCaseID, pdcPersonDetails.concernRoleID);

    final BDMForeignContributionPeriodTest foreignContributionPeriodTest =
      new BDMForeignContributionPeriodTest();

    // Create Foreign Contribution Period
    foreignContributionPeriodTest.createForeignContributionPeriodEvidence(
      integratedCaseID, cprObj.caseParticipantRoleID,
      pdcPersonDetails.concernRoleID, Date.fromISO8601("20220101"),
      Date.fromISO8601("20220630"));

    // Apply Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    evidenceController.applyAllChanges(caseKey);

    // Create overlapping Canadian Residence Period
    final EvidenceKey canadianResidencePeriodEvd =
      createCanadianResidencePeriodEvidence(integratedCaseID,
        cprObj.caseParticipantRoleID, pdcPersonDetails.concernRoleID,
        Date.fromISO8601("20220601"), Date.fromISO8601("20220831"),
        CuramConst.gkEmpty);

    // Check for creation of evidence issue
    final AdviceCaseIssue adviceCaseIssueObj =
      AdviceCaseIssueFactory.newInstance();

    final CaseIDAndEvidenceTypeKey adviceCaseIssueKey =
      new CaseIDAndEvidenceTypeKey();
    adviceCaseIssueKey.caseID = integratedCaseID;
    adviceCaseIssueKey.evidenceType =
      CASEEVIDENCE.BDM_CANADIAN_RESIDENCE_PERIOD;
    final AdviceCaseIssueDtlsList adviceCaseIssueDtls =
      adviceCaseIssueObj.searchByCaseIDAndEvidenceType(adviceCaseIssueKey);

    assertTrue(adviceCaseIssueDtls.dtls.stream()
      .anyMatch(x -> x.evidenceID == canadianResidencePeriodEvd.evidenceID));

    // modify canadian Residence Period evidence without including the
    // resolution
    final HashMap<String, Object> attributes = new HashMap<String, Object>();
    attributes.put(END_DATE, Date.fromISO8601("20220930"));

    modifyCanadianResidencePeriodEvidence(integratedCaseID,
      canadianResidencePeriodEvd.evidenceID, attributes);

    // Apply changes to canadian residence period even though overlap exists
    // and no resolution was provided
    final Exception exception =
      Assert.assertThrows(AppException.class, () -> {
        evidenceController.applyAllChanges(caseKey);
      });

    // Assert valid exception message thrown
    assertTrue(exception.getMessage().contains(ERR_RESOLVE_ASSOCIATED_ISSUE));
  }

  private curam.core.sl.struct.EvidenceKey
    createCanadianResidencePeriodEvidence(final Long caseID, final Long cprID,
      final long concernroleID, final Date startDate, final Date endDate,
      final String resolution) throws AppException, InformationalException {

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_CANADIAN_RESIDENCE_PERIOD;

    // get Latest Version of Evidence
    final GenericSLDataDetails genericDtls = new GenericSLDataDetails();
    final EvidenceServiceInterface evidenceServiceInterface =
      EvidenceGenericSLFactory.instance(eType, Date.getCurrentDate());

    final DynamicEvidenceDataDetails dynamicEvidencedataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(eType.evidenceType,
        Date.getCurrentDate());
    // assigning all the required attributes
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(CASE_PARTICIPANT_ROLE), cprID);
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(START_DATE), startDate);
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(END_DATE), endDate);
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(RESIDENCE_TYPE),
      new CodeTableItem(BDMRESIDENCETYPE.TABLENAME,
        BDMRESIDENCETYPE.RESIDENCE));

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
    genericDtls.addRelCp(CASE_PARTICIPANT_ROLE, cpAdaptor);

    return evidenceServiceInterface.createEvidence(genericDtls).evidenceKey;
  }

  /**
   * Modify the PDC Birth and Death evidence based on evidence details
   * and map containing the evidence fields and corresponding data.
   *
   * @param details
   * @param modifyData
   * @throws AppException
   * @throws InformationalException
   */
  private void modifyCanadianResidencePeriodEvidence(final Long caseID,
    final Long evidenceID, final HashMap<String, Object> modifyData)
    throws AppException, InformationalException {

    // Read DynamicEvidenceDataDetails
    final EvidenceMap map =
      curam.core.sl.infrastructure.impl.EvidenceController.getEvidenceMap();
    final StandardEvidenceInterface standardEvidenceInterface =
      map.getEvidenceType(CASEEVIDENCE.BDM_CANADIAN_RESIDENCE_PERIOD);

    final EIEvidenceKey evidenceKey = new EIEvidenceKey();
    evidenceKey.evidenceID = evidenceID;
    evidenceKey.evidenceType = CASEEVIDENCE.BDM_CANADIAN_RESIDENCE_PERIOD;

    final DynamicEvidenceDataDetails evidenceData =
      (DynamicEvidenceDataDetails) standardEvidenceInterface
        .readEvidence(evidenceKey);

    // Modify evidence data
    for (final Entry<String, Object> attributeEntry : modifyData.entrySet()) {

      final DynamicEvidenceDataAttributeDetails attributeObj =
        evidenceData.getAttribute(attributeEntry.getKey());

      DynamicEvidenceTypeConverter.setAttribute(attributeObj,
        attributeEntry.getValue());
    }

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    // evidence descriptor details for this evidence
    final RelatedIDAndEvidenceTypeKey relatedIDAndTypeKey =
      new RelatedIDAndEvidenceTypeKey();
    relatedIDAndTypeKey.relatedID = evidenceID;
    relatedIDAndTypeKey.evidenceType =
      CASEEVIDENCE.BDM_CANADIAN_RESIDENCE_PERIOD;

    final EvidenceDescriptorDtls evidenceDescriptorDtls =
      EvidenceDescriptorFactory.newInstance()
        .readByRelatedIDAndType(relatedIDAndTypeKey);

    // modify evidence details
    final EIEvidenceModifyDtls modifyEvidenceDetails =
      new EIEvidenceModifyDtls();

    modifyEvidenceDetails.evidenceObject = evidenceData;
    modifyEvidenceDetails.descriptor.assign(evidenceDescriptorDtls);
    modifyEvidenceDetails.descriptor.versionNo =
      evidenceDescriptorDtls.versionNo;
    modifyEvidenceDetails.descriptor.changeReason =
      EVIDENCECHANGEREASONEntry.CORRECTION.getCode();
    modifyEvidenceDetails.descriptor.effectiveFrom = Date.kZeroDate;

    evidenceControllerObj.modifyEvidence(evidenceKey, modifyEvidenceDetails);
  }
}
