package curam.bdm.test.ca.gc.bdm.evidence.foreigncontributionperiod.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMCREDITABLEPERIODSOURCE;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.facade.fec.fact.BDMForeignEngagementCaseFactory;
import curam.ca.gc.bdm.facade.fec.intf.BDMForeignEngagementCase;
import curam.ca.gc.bdm.facade.fec.struct.BDMFECaseDetails;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASEPRIORITY;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.facade.struct.CreateIntegratedCaseResultAndMessages;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
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
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test Foreign Contribution Period evidence business validations and calculated
 * attribute.
 *
 * 73158 DEV: Contribution details Evidence in Foreign Engagement Case
 */
public class BDMForeignContributionPeriodTest
  extends curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest {

  private static final String CASE_PARTICIPANT_ROLE = "caseParticipantRole";

  private static final String START_DATE = "startDate";

  private static final String END_DATE = "endDate";

  private static final String COUNTRY = "country";

  private static final String CREDITABLE_PERIOD_SOURCE =
    "creditablePeriodsource";

  private static final String ERR_START_DATE_BEFORE_END_DATE =
    "‘Start Date’ must be before ‘End Date’.";

  private static final String ERR_OVERLAPPING_FOREIGN_CONTRIBUTION_PERIOD =
    "A Contribution Period record already exists for an overlapping period.";

  private static final String ERR_FUTURE_END_DATE_DISALLOWED =
    "‘End Date’ must not be a future date.";

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
    details.countryCode = BDMSOURCECOUNTRY.IRELAND;
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
        createForeignContributionPeriodEvidence(integratedCaseID,
          cprObj.caseParticipantRoleID, pdcPersonDetails.concernRoleID,
          Date.fromISO8601("20200101"), Date.fromISO8601("20191231"));
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
    details.countryCode = BDMSOURCECOUNTRY.IRELAND;
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
        createForeignContributionPeriodEvidence(integratedCaseID,
          cprObj.caseParticipantRoleID, pdcPersonDetails.concernRoleID,
          Date.fromISO8601("20200101"), Date.getCurrentDate().addDays(1));
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
  public void foreignContributionPeriodOverlapFailure()
    throws AppException, InformationalException {

    // Register Person
    final PDCPersonDetails pdcPersonDetails = this.createPDCPerson();

    // Create FEC
    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages;

    final BDMFECaseDetails details = new BDMFECaseDetails();
    details.concernRoleID = pdcPersonDetails.concernRoleID;
    details.countryCode = BDMSOURCECOUNTRY.IRELAND;
    details.priorityCode = CASEPRIORITY.HIGH;

    createIntegratedCaseResultAndMessages =
      kBdmForeignEngagementCaseObj.createFEIntegratedCase(details);

    final Long integratedCaseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj = util.getCaseParticipantRoleID(
      integratedCaseID, pdcPersonDetails.concernRoleID);

    // Create first evidence record
    createForeignContributionPeriodEvidence(integratedCaseID,
      cprObj.caseParticipantRoleID, pdcPersonDetails.concernRoleID,
      Date.fromISO8601("20200101"), Date.fromISO8601("20201231"));

    // Apply First Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    evidenceController.applyAllChanges(caseKey);

    // Create overlapping evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        createForeignContributionPeriodEvidence(integratedCaseID,
          cprObj.caseParticipantRoleID, pdcPersonDetails.concernRoleID,
          Date.fromISO8601("20200601"), Date.fromISO8601("20210531"));
      });

    // Assert valid exception message thrown
    assertTrue(exception.getMessage()
      .contains(ERR_OVERLAPPING_FOREIGN_CONTRIBUTION_PERIOD));
  }

  /**
   * PASS-IF a validation doesn't fail and evidence records activated when there
   * is no overlap in periods of evidence records.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void foreignContributionPeriodOverlapSuccess()
    throws AppException, InformationalException {

    // Register Person
    final PDCPersonDetails pdcPersonDetails = this.createPDCPerson();

    // Create FEC
    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages;

    final BDMFECaseDetails details = new BDMFECaseDetails();
    details.concernRoleID = pdcPersonDetails.concernRoleID;
    details.countryCode = BDMSOURCECOUNTRY.IRELAND;
    details.priorityCode = CASEPRIORITY.HIGH;

    createIntegratedCaseResultAndMessages =
      kBdmForeignEngagementCaseObj.createFEIntegratedCase(details);

    final Long integratedCaseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj = util.getCaseParticipantRoleID(
      integratedCaseID, pdcPersonDetails.concernRoleID);

    // Create first evidence record
    final EvidenceKey evidenceKey1 =
      createForeignContributionPeriodEvidence(integratedCaseID,
        cprObj.caseParticipantRoleID, pdcPersonDetails.concernRoleID,
        Date.fromISO8601("20200101"), Date.fromISO8601("20201231"));

    // Create overlapping evidence record
    final EvidenceKey evidenceKey2 =
      createForeignContributionPeriodEvidence(integratedCaseID,
        cprObj.caseParticipantRoleID, pdcPersonDetails.concernRoleID,
        Date.fromISO8601("20210101"), Date.fromISO8601("20210531"));

    // Apply Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    evidenceController.applyAllChanges(caseKey);

    // Check if the two foreign contribution period records are activated
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
   * PASS-IF The "country" attribute of the evidence is auto populated
   * from the FEC country.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void foreignContributionPeriodCountryTest()
    throws AppException, InformationalException {

    // Register Person
    final PDCPersonDetails pdcPersonDetails = this.createPDCPerson();

    // Create FEC
    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages;

    final BDMFECaseDetails details = new BDMFECaseDetails();
    details.concernRoleID = pdcPersonDetails.concernRoleID;
    details.countryCode = BDMSOURCECOUNTRY.IRELAND;
    details.priorityCode = CASEPRIORITY.HIGH;

    createIntegratedCaseResultAndMessages =
      kBdmForeignEngagementCaseObj.createFEIntegratedCase(details);

    final Long integratedCaseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj = util.getCaseParticipantRoleID(
      integratedCaseID, pdcPersonDetails.concernRoleID);

    // Create evidence
    final EvidenceKey evidenceKey =
      createForeignContributionPeriodEvidence(integratedCaseID,
        cprObj.caseParticipantRoleID, pdcPersonDetails.concernRoleID,
        Date.fromISO8601("20200101"), Date.fromISO8601("20201231"));

    // Apply Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    evidenceController.applyAllChanges(caseKey);

    // Read DynamicEvidenceDataDetails
    final EvidenceMap map =
      curam.core.sl.infrastructure.impl.EvidenceController.getEvidenceMap();
    final StandardEvidenceInterface standardEvidenceInterface =
      map.getEvidenceType(
        CASEEVIDENCEEntry.BDM_FOREIGN_CONTRIBUTION_PERIOD.getCode());

    final EIEvidenceKey readEvidenceKey = new EIEvidenceKey();
    readEvidenceKey.evidenceID = evidenceKey.evidenceID;
    readEvidenceKey.evidenceType =
      CASEEVIDENCEEntry.BDM_FOREIGN_CONTRIBUTION_PERIOD.getCode();

    final DynamicEvidenceDataDetails evidenceData =
      (DynamicEvidenceDataDetails) standardEvidenceInterface
        .readEvidence(readEvidenceKey);

    // Assert evidence "country" attribute value equals FEC country value
    assertTrue(BDMSOURCECOUNTRY.IRELAND
      .equals(evidenceData.getAttribute(COUNTRY).getValue()));
  }

  public curam.core.sl.struct.EvidenceKey
    createForeignContributionPeriodEvidence(final Long caseID,
      final Long cprID, final long concernroleID, final Date startDate,
      final Date endDate) throws AppException, InformationalException {

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_FOREIGN_CONTRIBUTION_PERIOD;

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
      dynamicEvidencedataDetails.getAttribute(CREDITABLE_PERIOD_SOURCE),
      new CodeTableItem(BDMCREDITABLEPERIODSOURCE.TABLENAME,
        BDMCREDITABLEPERIODSOURCE.LIAISON));

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

}
