
package curam.bdmoas.test.evidence.applicationDetails.impl;

import curam.ca.gc.bdmoas.codetable.BDMOASAPPLICATIONSTATUS;
import curam.ca.gc.bdmoas.codetable.BDMOASBENEFITTYPE;
import curam.ca.gc.bdmoas.codetable.BDMOASCANCELSTATUS;
import curam.ca.gc.bdmoas.codetable.BDMOASMETHODOFAPPLICATION;
import curam.ca.gc.bdmoas.evidence.constants.impl.BDMOASApplicationDetailsConstants;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.facade.struct.CreateIntegratedCaseResult;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.impl.EvidenceMap;
import curam.core.sl.infrastructure.impl.StandardEvidenceInterface;
import curam.core.sl.infrastructure.struct.ECActiveEvidenceDtls;
import curam.core.sl.infrastructure.struct.ECActiveEvidenceDtlsList;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.EvidenceDescriptorDetails;
import curam.core.sl.struct.EvidenceTypeKey;
import curam.core.struct.CaseKey;
import curam.creole.execution.session.InterpretedRuleObjectFactory;
import curam.creole.execution.session.ManualRecalculation;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.ruleclass.BDMOASApplicationDetailsRuleSet.impl.BDMOASApplicationDetails;
import curam.creole.ruleclass.BDMOASApplicationDetailsRuleSet.impl.BDMOASApplicationDetails_Factory;
import curam.creole.ruleclass.BDMOASApplicationDetailsSummaryRuleSet.impl.SummaryInformation;
import curam.creole.ruleclass.BDMOASApplicationDetailsSummaryRuleSet.impl.SummaryInformation_Factory;
import curam.creole.ruleclass.BDMOASBenefitCancellationRequestRuleSet.impl.BDMOASBenefitCancellationRequest;
import curam.creole.ruleclass.BDMOASBenefitCancellationRequestRuleSet.impl.BDMOASBenefitCancellationRequest_Factory;
import curam.creole.ruleclass.BDMOASBenefitCessationRuleSet.impl.BDMOASBenefitCessation;
import curam.creole.ruleclass.BDMOASBenefitCessationRuleSet.impl.BDMOASBenefitCessation_Factory;
import curam.creole.storage.inmemory.InMemoryDataStorage;
import curam.creole.value.CodeTableItem;
import curam.dynamicevidence.impl.DynamicEvidenceDataAttributeDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.sl.impl.EvidenceGenericSLFactory;
import curam.dynamicevidence.sl.impl.EvidenceServiceInterface;
import curam.dynamicevidence.sl.struct.impl.GenericSLDataDetails;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import curam.wizard.util.impl.CodetableUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class BDMOASApplicationDetailsTest extends BDMOASCaseTest {

  private Session session;

  private static final String ERR_RECEIPT_DATE_CANNOT_BE_MODIFIED =
    "Receipt Date cannot be modified if the Method of Application is 'Online'.";

  private static final String ERR_WITHDRAWAL_FORBIDDEN_GIS =
    "Application Status cannot be 'Withdrawn' if Benefit Type is not 'Old Age Security Pension'.";

  private static final String ERR_WITHDRAWAL_FORBIDDEN_ALW =
    "Application Status cannot be 'Withdrawn' if Benefit Type is not 'Old Age Security Pension'.";

  private static final String ERR_WITHDRAWAL_DATE_REQUIRED =
    "Withdrawal Date must be entered and can only be entered if Application Status is 'Withdrawn'.";

  private static final String ERR_REQUESTED_PENSION_START_DATE_GIS =
    "Requested Pension Start Date can only be entered if Benefit Type is 'Old Age Security Pension'.";

  private static final String ERR_REQUESTED_PENSION_START_DATE_ALW =
    "Requested Pension Start Date can only be entered if Benefit Type is 'Old Age Security Pension'.";

  private static final String ERR_REQUEST_PENSION_START_DATE_NOT_FIRST =
    "Requested Pension Start Date must be the first of a month.";

  @Override
  protected void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    this.session =
      Session_Factory.getFactory().newInstance(new ManualRecalculation(),
        new InMemoryDataStorage(new InterpretedRuleObjectFactory()));
  }

  /**
   * PASS-IF summary message is the default summary message
   * when cancellation, cessation and requested pension start date don't exist.
   */
  @Test
  public void defaultSummaryTest() {

    final String summaryMessage =
      CodetableUtil.getCodetableDescription(BDMOASBENEFITTYPE.TABLENAME,
        BDMOASBENEFITTYPE.OAS_PENSION)
        + " ("
        + CodetableUtil.getCodetableDescription(
          BDMOASAPPLICATIONSTATUS.TABLENAME, BDMOASAPPLICATIONSTATUS.ACTIVE)
        + ")";

    final BDMOASApplicationDetails evidence =
      this.getApplicationDetailsEvidence();
    evidence.benefitType().specifyValue(new CodeTableItem(
      BDMOASBENEFITTYPE.TABLENAME, BDMOASBENEFITTYPE.OAS_PENSION));
    evidence.applicationStatus().specifyValue(new CodeTableItem(
      BDMOASAPPLICATIONSTATUS.TABLENAME, BDMOASAPPLICATIONSTATUS.ACTIVE));
    evidence.related_BDMOASBenefitCancellationRequest().specifyValue(null);
    evidence.related_BDMOASBenefitCessation().specifyValue(null);
    evidence.requestedPensionStartDate().specifyValue(null);

    final SummaryInformation summary = this.getSummary(evidence);

    assertTrue(summary.summary().getValue().toLocale(Locale.ENGLISH)
      .equals(summaryMessage));
  }

  /**
   * PASS-IF summary message includes the requested pension start date
   * when requested pension start date exists.
   */
  @Test
  public void requestedPensionStartDateSummaryTest() {

    final Date requestedPensionStartDate = Date.getCurrentDate().addDays(-50);

    final String summaryMessage =
      CodetableUtil.getCodetableDescription(BDMOASBENEFITTYPE.TABLENAME,
        BDMOASBENEFITTYPE.OAS_PENSION) + " (Requested Pension Start Date  "
        + formattedDate(requestedPensionStartDate, "yyyy-MM-dd") + ")";

    final BDMOASApplicationDetails evidence =
      this.getApplicationDetailsEvidence();
    evidence.benefitType().specifyValue(new CodeTableItem(
      BDMOASBENEFITTYPE.TABLENAME, BDMOASBENEFITTYPE.OAS_PENSION));
    evidence.applicationStatus().specifyValue(new CodeTableItem(
      BDMOASAPPLICATIONSTATUS.TABLENAME, BDMOASAPPLICATIONSTATUS.ACTIVE));
    evidence.related_BDMOASBenefitCancellationRequest().specifyValue(null);
    evidence.related_BDMOASBenefitCessation().specifyValue(null);
    evidence.requestedPensionStartDate()
      .specifyValue(requestedPensionStartDate);

    final SummaryInformation summary = this.getSummary(evidence);

    assertTrue(summary.summary().getValue().toLocale(Locale.ENGLISH)
      .equals(summaryMessage));
  }

  /**
   * PASS-IF summary message includes the cessation date
   * when Benefit Cessation child evidence exits with no reinstatement date.
   */
  @Test
  public void cessationDateSummaryTest() {

    final Date cessationDate = Date.getCurrentDate();

    final String summaryMessage =
      CodetableUtil.getCodetableDescription(BDMOASBENEFITTYPE.TABLENAME,
        BDMOASBENEFITTYPE.OAS_PENSION) + " (Ceased Effective  "
        + formattedDate(cessationDate, "yyyy-MM-dd") + ")";

    final BDMOASApplicationDetails evidence =
      this.getApplicationDetailsEvidence();
    evidence.benefitType().specifyValue(new CodeTableItem(
      BDMOASBENEFITTYPE.TABLENAME, BDMOASBENEFITTYPE.OAS_PENSION));
    evidence.applicationStatus().specifyValue(new CodeTableItem(
      BDMOASAPPLICATIONSTATUS.TABLENAME, BDMOASAPPLICATIONSTATUS.ACTIVE));
    evidence.related_BDMOASBenefitCancellationRequest().specifyValue(null);
    final BDMOASBenefitCessation benefitCessationEvidence =
      getBenefitCessationEvidence();
    benefitCessationEvidence.status().specifyValue(new CodeTableItem(
      EVIDENCEDESCRIPTORSTATUS.TABLENAME, EVIDENCEDESCRIPTORSTATUS.ACTIVE));
    benefitCessationEvidence.reinstatementDate().specifyValue(null);
    benefitCessationEvidence.cessationDate().specifyValue(cessationDate);
    final List<BDMOASBenefitCessation> cessationRecords =
      new ArrayList<BDMOASBenefitCessation>();
    cessationRecords.add(benefitCessationEvidence);
    evidence.related_BDMOASBenefitCessation().specifyValue(cessationRecords);
    evidence.requestedPensionStartDate().specifyValue(null);

    final SummaryInformation summary = this.getSummary(evidence);

    assertTrue(summary.summary().getValue().toLocale(Locale.ENGLISH)
      .equals(summaryMessage));
  }

  /**
   * PASS-IF summary message indicates Cancellation Granted when child benefit
   * cancellation record with granted status exists.
   */
  @Test
  public void cancellationGrantedSummaryTest() {

    final String summaryMessage =
      CodetableUtil.getCodetableDescription(BDMOASBENEFITTYPE.TABLENAME,
        BDMOASBENEFITTYPE.OAS_PENSION) + " (Cancellation Granted)";

    final BDMOASApplicationDetails evidence =
      this.getApplicationDetailsEvidence();
    evidence.benefitType().specifyValue(new CodeTableItem(
      BDMOASBENEFITTYPE.TABLENAME, BDMOASBENEFITTYPE.OAS_PENSION));
    evidence.applicationStatus().specifyValue(new CodeTableItem(
      BDMOASAPPLICATIONSTATUS.TABLENAME, BDMOASAPPLICATIONSTATUS.ACTIVE));
    evidence.related_BDMOASBenefitCancellationRequest().specifyValue(null);
    final BDMOASBenefitCancellationRequest benefitCancellationEvidence =
      getBenefitCancellationEvidence();
    benefitCancellationEvidence.status().specifyValue(new CodeTableItem(
      EVIDENCEDESCRIPTORSTATUS.TABLENAME, EVIDENCEDESCRIPTORSTATUS.ACTIVE));
    benefitCancellationEvidence.cancellationStatus()
      .specifyValue(new CodeTableItem(BDMOASCANCELSTATUS.TABLENAME,
        BDMOASCANCELSTATUS.GRANTED));
    final List<BDMOASBenefitCancellationRequest> cancellationRecords =
      new ArrayList<BDMOASBenefitCancellationRequest>();
    cancellationRecords.add(benefitCancellationEvidence);
    evidence.related_BDMOASBenefitCancellationRequest()
      .specifyValue(cancellationRecords);
    evidence.related_BDMOASBenefitCessation().specifyValue(null);
    evidence.requestedPensionStartDate().specifyValue(null);

    final SummaryInformation summary = this.getSummary(evidence);

    assertTrue(summary.summary().getValue().toLocale(Locale.ENGLISH)
      .equals(summaryMessage));
  }

  /**
   * PASS-IF a validation message is thrown when Receipt Date is modified and
   * the method of application is online.
   */
  @Test
  public void receiptDateModificationForbiddenFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final Long concernRoleID = person.registrationIDDetails.concernRoleID;
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(BDMOASApplicationDetailsConstants.BENEFIT_TYPE,
      BDMOASBENEFITTYPE.OAS_PENSION);
    attributes.put(BDMOASApplicationDetailsConstants.APPLICATION_STATUS,
      BDMOASAPPLICATIONSTATUS.ACTIVE);
    attributes.put(BDMOASApplicationDetailsConstants.METHOD_OF_APPLICATION,
      BDMOASMETHODOFAPPLICATION.ONLINE);
    attributes.put(BDMOASApplicationDetailsConstants.RECEIPT_DATE,
      formattedDate(Date.getCurrentDate().addDays(-50), "yyyyMMdd"));

    final EIEvidenceKey eiEvidenceKey =
      this.createEvidence(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID,
        CASEEVIDENCEEntry.OAS_APPLICATION_DETAILS, attributes, getToday());

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCase.integratedCaseID;
    evidenceController.applyAllChanges(caseKey);

    // Read DynamicEvidenceDataDetails
    final EvidenceMap map =
      curam.core.sl.infrastructure.impl.EvidenceController.getEvidenceMap();
    final StandardEvidenceInterface standardEvidenceInterface =
      map.getEvidenceType(CASEEVIDENCE.OAS_APPLICATION_DETAILS);

    final EIEvidenceKey evidenceKey = new EIEvidenceKey();
    evidenceKey.evidenceID = eiEvidenceKey.evidenceID;
    evidenceKey.evidenceType = CASEEVIDENCE.OAS_APPLICATION_DETAILS;

    final DynamicEvidenceDataDetails evidenceData =
      (DynamicEvidenceDataDetails) standardEvidenceInterface
        .readEvidence(evidenceKey);

    // update the Receipt Date
    final DynamicEvidenceDataAttributeDetails attributeObj = evidenceData
      .getAttribute(BDMOASApplicationDetailsConstants.RECEIPT_DATE);

    DynamicEvidenceTypeConverter.setAttribute(attributeObj,
      Date.getCurrentDate());

    // evidence descriptor details for this evidence
    final RelatedIDAndEvidenceTypeKey relatedIDAndTypeKey =
      new RelatedIDAndEvidenceTypeKey();
    relatedIDAndTypeKey.relatedID = eiEvidenceKey.evidenceID;
    relatedIDAndTypeKey.evidenceType = CASEEVIDENCE.OAS_APPLICATION_DETAILS;

    final EvidenceDescriptorDtls evidenceDescriptorDtls =
      EvidenceDescriptorFactory.newInstance()
        .readByRelatedIDAndType(relatedIDAndTypeKey);

    // set descriptor attributes
    final EvidenceDescriptorDetails descriptor =
      new EvidenceDescriptorDetails();
    descriptor.evidenceType = evidenceDescriptorDtls.evidenceType;
    descriptor.caseID = evidenceDescriptorDtls.caseID;
    descriptor.receivedDate = Date.getCurrentDate();
    descriptor.participantID = evidenceDescriptorDtls.participantID;

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.OAS_APPLICATION_DETAILS;

    // get Latest Version of Evidence
    final GenericSLDataDetails genericDtls = new GenericSLDataDetails();
    final EvidenceServiceInterface evidenceServiceInterface =
      EvidenceGenericSLFactory.instance(eType, Date.getCurrentDate());

    genericDtls.setData(evidenceData);
    genericDtls.setDescriptor(descriptor);
    genericDtls.setCaseIdKey(descriptor.caseID);

    final Exception exception =
      Assert.assertThrows(InformationalException.class,
        () -> evidenceServiceInterface.modifyEvidence(genericDtls));

    // Assert valid exception message thrown
    assertTrue(
      exception.getMessage().contains(ERR_RECEIPT_DATE_CANNOT_BE_MODIFIED));
  }

  /**
   * PASS-IF a validation message is thrown when application Status is Withdrawn
   * for benefit type is GIS.
   */
  @Test
  public void applicationStatusWithdrawnForbiddenForGISFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final Long concernRoleID = person.registrationIDDetails.concernRoleID;
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(BDMOASApplicationDetailsConstants.BENEFIT_TYPE,
      BDMOASBENEFITTYPE.GUARANTEED_INCOME_SUPPLEMENT);
    attributes.put(BDMOASApplicationDetailsConstants.APPLICATION_STATUS,
      BDMOASAPPLICATIONSTATUS.WITHDRAWN);
    attributes.put(BDMOASApplicationDetailsConstants.METHOD_OF_APPLICATION,
      BDMOASMETHODOFAPPLICATION.ONLINE);
    attributes.put(BDMOASApplicationDetailsConstants.RECEIPT_DATE,
      formattedDate(Date.getCurrentDate().addDays(-50), "yyyyMMdd"));

    final Exception exception =
      Assert.assertThrows(InformationalException.class,
        () -> this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_APPLICATION_DETAILS, attributes, getToday()));

    // Assert valid exception message thrown
    assertTrue(exception.getMessage().contains(ERR_WITHDRAWAL_FORBIDDEN_GIS));
  }

  /**
   * PASS-IF a validation message is thrown when application Status is Withdrawn
   * for benefit type is ALW.
   */
  @Test
  public void applicationStatusWithdrawnForbiddenForALWFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final Long concernRoleID = person.registrationIDDetails.concernRoleID;
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(BDMOASApplicationDetailsConstants.BENEFIT_TYPE,
      BDMOASBENEFITTYPE.ALLOWANCE);
    attributes.put(BDMOASApplicationDetailsConstants.APPLICATION_STATUS,
      BDMOASAPPLICATIONSTATUS.WITHDRAWN);
    attributes.put(BDMOASApplicationDetailsConstants.METHOD_OF_APPLICATION,
      BDMOASMETHODOFAPPLICATION.ONLINE);
    attributes.put(BDMOASApplicationDetailsConstants.RECEIPT_DATE,
      formattedDate(Date.getCurrentDate().addDays(-50), "yyyyMMdd"));

    final Exception exception =
      Assert.assertThrows(InformationalException.class,
        () -> this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_APPLICATION_DETAILS, attributes, getToday()));

    // Assert valid exception message thrown
    assertTrue(exception.getMessage().contains(ERR_WITHDRAWAL_FORBIDDEN_ALW));
  }

  /**
   * PASS-IF a validation message is not thrown when application Status is
   * Withdrawn for benefit type is OAS Pension.
   */
  @Test
  public void applicationStatusWithdrawnAllowedForOASPensionSuccess()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final Long concernRoleID = person.registrationIDDetails.concernRoleID;
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(BDMOASApplicationDetailsConstants.BENEFIT_TYPE,
      BDMOASBENEFITTYPE.OAS_PENSION);
    attributes.put(BDMOASApplicationDetailsConstants.APPLICATION_STATUS,
      BDMOASAPPLICATIONSTATUS.WITHDRAWN);
    attributes.put(BDMOASApplicationDetailsConstants.WITHDRAWAL_DATE,
      formattedDate(Date.getCurrentDate(), "yyyyMMdd"));
    attributes.put(BDMOASApplicationDetailsConstants.METHOD_OF_APPLICATION,
      BDMOASMETHODOFAPPLICATION.ONLINE);
    attributes.put(BDMOASApplicationDetailsConstants.RECEIPT_DATE,
      formattedDate(Date.getCurrentDate().addDays(-50), "yyyyMMdd"));

    this.createEvidence(integratedCase.integratedCaseID,
      person.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.OAS_APPLICATION_DETAILS, attributes, getToday());

    // Apply Evidence Changes
    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCase.integratedCaseID;
    evidenceController.applyAllChanges(caseKey);

    // Check if the evidence records were activated
    final ECActiveEvidenceDtlsList activeEvdList =
      evidenceController.listActive(caseKey);

    // Assert both records activated
    final List<ECActiveEvidenceDtls> filteredList1 =
      activeEvdList.dtls.stream()
        .filter(
          (x) -> x.evidenceType.equals(CASEEVIDENCE.OAS_APPLICATION_DETAILS))
        .collect(Collectors.toList());
    assertTrue(filteredList1.size() == 1);
  }

  /**
   * PASS-IF a validation message is thrown when application Status is
   * Withdrawn for benefit type is OAS Pension and withdrawal date is not
   * entered.
   */
  @Test
  public void withdrawalDateRequiredFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final Long concernRoleID = person.registrationIDDetails.concernRoleID;
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(BDMOASApplicationDetailsConstants.BENEFIT_TYPE,
      BDMOASBENEFITTYPE.OAS_PENSION);
    attributes.put(BDMOASApplicationDetailsConstants.APPLICATION_STATUS,
      BDMOASAPPLICATIONSTATUS.WITHDRAWN);
    attributes.put(BDMOASApplicationDetailsConstants.METHOD_OF_APPLICATION,
      BDMOASMETHODOFAPPLICATION.ONLINE);
    attributes.put(BDMOASApplicationDetailsConstants.RECEIPT_DATE,
      formattedDate(Date.getCurrentDate().addDays(-50), "yyyyMMdd"));

    final Exception exception =
      Assert.assertThrows(InformationalException.class,
        () -> this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_APPLICATION_DETAILS, attributes, getToday()));

    // Assert valid exception message thrown
    assertTrue(exception.getMessage().contains(ERR_WITHDRAWAL_DATE_REQUIRED));
  }

  /**
   * PASS-IF a validation message is thrown when application Status is not
   * Withdrawn for benefit type is OAS Pension and withdrawal date is entered.
   */
  @Test
  public void withdrawalDateForbiddenFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final Long concernRoleID = person.registrationIDDetails.concernRoleID;
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(BDMOASApplicationDetailsConstants.BENEFIT_TYPE,
      BDMOASBENEFITTYPE.OAS_PENSION);
    attributes.put(BDMOASApplicationDetailsConstants.APPLICATION_STATUS,
      BDMOASAPPLICATIONSTATUS.ACTIVE);
    attributes.put(BDMOASApplicationDetailsConstants.WITHDRAWAL_DATE,
      formattedDate(Date.getCurrentDate(), "yyyyMMdd"));
    attributes.put(BDMOASApplicationDetailsConstants.METHOD_OF_APPLICATION,
      BDMOASMETHODOFAPPLICATION.ONLINE);
    attributes.put(BDMOASApplicationDetailsConstants.RECEIPT_DATE,
      formattedDate(Date.getCurrentDate().addDays(-50), "yyyyMMdd"));

    final Exception exception =
      Assert.assertThrows(InformationalException.class,
        () -> this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_APPLICATION_DETAILS, attributes, getToday()));

    // Assert valid exception message thrown
    assertTrue(exception.getMessage().contains(ERR_WITHDRAWAL_DATE_REQUIRED));
  }

  /**
   * PASS-IF a validation message is thrown when requested pension start date is
   * entered and the benefit type is GIS
   */
  @Test
  public void requestedPensionStartDateForbiddenForGISFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final Long concernRoleID = person.registrationIDDetails.concernRoleID;
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(BDMOASApplicationDetailsConstants.BENEFIT_TYPE,
      BDMOASBENEFITTYPE.GUARANTEED_INCOME_SUPPLEMENT);
    attributes.put(BDMOASApplicationDetailsConstants.APPLICATION_STATUS,
      BDMOASAPPLICATIONSTATUS.ACTIVE);
    attributes.put(BDMOASApplicationDetailsConstants.METHOD_OF_APPLICATION,
      BDMOASMETHODOFAPPLICATION.ONLINE);
    attributes.put(BDMOASApplicationDetailsConstants.RECEIPT_DATE,
      formattedDate(Date.getCurrentDate().addDays(-20), "yyyyMMdd"));
    attributes.put(
      BDMOASApplicationDetailsConstants.REQUESTED_PENSION_START_DATE,
      formattedDate(Date.getCurrentDate().addDays(-50), "yyyyMMdd"));

    final Exception exception =
      Assert.assertThrows(InformationalException.class,
        () -> this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_APPLICATION_DETAILS, attributes, getToday()));

    // Assert valid exception message thrown
    assertTrue(
      exception.getMessage().contains(ERR_REQUESTED_PENSION_START_DATE_GIS));
  }

  /**
   * PASS-IF a validation message is thrown when requested pension start date is
   * entered and the benefit type is ALW
   */
  @Test
  public void requestedPensionStartDateForbiddenForALWFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final Long concernRoleID = person.registrationIDDetails.concernRoleID;
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(BDMOASApplicationDetailsConstants.BENEFIT_TYPE,
      BDMOASBENEFITTYPE.ALLOWANCE);
    attributes.put(BDMOASApplicationDetailsConstants.APPLICATION_STATUS,
      BDMOASAPPLICATIONSTATUS.ACTIVE);
    attributes.put(BDMOASApplicationDetailsConstants.METHOD_OF_APPLICATION,
      BDMOASMETHODOFAPPLICATION.ONLINE);
    attributes.put(BDMOASApplicationDetailsConstants.RECEIPT_DATE,
      formattedDate(Date.getCurrentDate().addDays(-20), "yyyyMMdd"));
    attributes.put(
      BDMOASApplicationDetailsConstants.REQUESTED_PENSION_START_DATE,
      formattedDate(Date.getCurrentDate().addDays(-50), "yyyyMMdd"));

    final Exception exception =
      Assert.assertThrows(InformationalException.class,
        () -> this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_APPLICATION_DETAILS, attributes, getToday()));

    // Assert valid exception message thrown
    assertTrue(
      exception.getMessage().contains(ERR_REQUESTED_PENSION_START_DATE_ALW));
  }

  /**
   * PASS-IF a validation message is thrown when requested pension start date is
   * entered but not the first of month for OAS Pension.
   */
  @Test
  public void requestedPensionStartDateFirstOfMonthFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final Long concernRoleID = person.registrationIDDetails.concernRoleID;
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(BDMOASApplicationDetailsConstants.BENEFIT_TYPE,
      BDMOASBENEFITTYPE.OAS_PENSION);
    attributes.put(BDMOASApplicationDetailsConstants.APPLICATION_STATUS,
      BDMOASAPPLICATIONSTATUS.ACTIVE);
    attributes.put(BDMOASApplicationDetailsConstants.METHOD_OF_APPLICATION,
      BDMOASMETHODOFAPPLICATION.ONLINE);
    attributes.put(BDMOASApplicationDetailsConstants.RECEIPT_DATE,
      formattedDate(Date.getCurrentDate().addDays(-20), "yyyyMMdd"));
    attributes.put(
      BDMOASApplicationDetailsConstants.REQUESTED_PENSION_START_DATE,
      formattedDate(Date.getCurrentDate().addDays(-50), "yyyyMMdd"));

    final Exception exception =
      Assert.assertThrows(InformationalException.class,
        () -> this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_APPLICATION_DETAILS, attributes, getToday()));

    // Assert valid exception message thrown
    assertTrue(exception.getMessage()
      .contains(ERR_REQUEST_PENSION_START_DATE_NOT_FIRST));
  }

  /**
   * PASS-IF a validation message is not thrown when requested pension start
   * date is
   * entered as first of month for OAS Pension.
   */
  @Test
  public void requestedPensionStartDateFirstOfMonthSuccess()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final Long concernRoleID = person.registrationIDDetails.concernRoleID;
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(BDMOASApplicationDetailsConstants.BENEFIT_TYPE,
      BDMOASBENEFITTYPE.OAS_PENSION);
    attributes.put(BDMOASApplicationDetailsConstants.APPLICATION_STATUS,
      BDMOASAPPLICATIONSTATUS.ACTIVE);
    attributes.put(BDMOASApplicationDetailsConstants.METHOD_OF_APPLICATION,
      BDMOASMETHODOFAPPLICATION.ONLINE);
    attributes.put(BDMOASApplicationDetailsConstants.RECEIPT_DATE,
      formattedDate(Date.getCurrentDate().addDays(-20), "yyyyMMdd"));
    attributes.put(
      BDMOASApplicationDetailsConstants.REQUESTED_PENSION_START_DATE,
      formattedDateMovedToFirstOfMonth(Date.getCurrentDate().addDays(-50)));

    this.createEvidence(integratedCase.integratedCaseID,
      person.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.OAS_APPLICATION_DETAILS, attributes, getToday());

    // Apply Evidence Changes
    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCase.integratedCaseID;
    evidenceController.applyAllChanges(caseKey);

    // Check if the evidence records were activated
    final ECActiveEvidenceDtlsList activeEvdList =
      evidenceController.listActive(caseKey);

    // Assert both records activated
    final List<ECActiveEvidenceDtls> filteredList1 =
      activeEvdList.dtls.stream()
        .filter(
          (x) -> x.evidenceType.equals(CASEEVIDENCE.OAS_APPLICATION_DETAILS))
        .collect(Collectors.toList());
    assertTrue(filteredList1.size() == 1);
  }

  private BDMOASApplicationDetails getApplicationDetailsEvidence() {

    return BDMOASApplicationDetails_Factory.getFactory()
      .newInstance(this.session);
  }

  private BDMOASBenefitCessation getBenefitCessationEvidence() {

    return BDMOASBenefitCessation_Factory.getFactory()
      .newInstance(this.session);
  }

  private BDMOASBenefitCancellationRequest getBenefitCancellationEvidence() {

    return BDMOASBenefitCancellationRequest_Factory.getFactory()
      .newInstance(this.session);
  }

  private SummaryInformation
    getSummary(final BDMOASApplicationDetails evidence) {

    final SummaryInformation summary =
      SummaryInformation_Factory.getFactory().newInstance(this.session);
    summary.evidence().specifyValue(evidence);

    return summary;
  }

  private String formattedDate(final Date date, final String format) {

    final SimpleDateFormat dateFormat = new SimpleDateFormat(format);

    return dateFormat.format(date.getCalendar().getTime());
  }

  private String formattedDateMovedToFirstOfMonth(final Date date) {

    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    // move date to the first of the month
    final Calendar cal = date.getCalendar();
    cal.set(Calendar.DAY_OF_MONTH, 1);
    final Date firstOfMonthDate = new Date(cal);

    return dateFormat.format(firstOfMonthDate.getCalendar().getTime());
  }
}
