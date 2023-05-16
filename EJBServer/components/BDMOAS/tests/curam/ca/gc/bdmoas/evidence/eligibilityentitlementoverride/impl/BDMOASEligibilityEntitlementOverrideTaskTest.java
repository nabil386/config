package curam.ca.gc.bdmoas.evidence.eligibilityentitlementoverride.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMMARITALSTATUS;
import curam.ca.gc.bdm.codetable.BDMOFFENDERSTATUS;
import curam.ca.gc.bdm.codetable.BDMYESNO;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdmoas.codetable.BDMOASAPPLICATIONSTATUS;
import curam.ca.gc.bdmoas.codetable.BDMOASBENEFITTYPE;
import curam.ca.gc.bdmoas.codetable.BDMOASLIVINGAPARTREASON;
import curam.ca.gc.bdmoas.codetable.BDMOASMARITALRELATION;
import curam.ca.gc.bdmoas.codetable.BDMOASMETHODOFAPPLICATION;
import curam.ca.gc.bdmoas.codetable.BDMOASOVERRIDEBENEFITTYPE;
import curam.ca.gc.bdmoas.codetable.BDMOASOVERRIDEREASON;
import curam.ca.gc.bdmoas.codetable.BDMOASOVERRIDEVALUE;
import curam.ca.gc.bdmoas.codetable.impl.BDMOASOVERRIDEEVDTASKTYPEEntry;
import curam.ca.gc.bdmoas.evidence.constants.impl.BDMOASApplicationDetailsConstants;
import curam.ca.gc.bdmoas.evidence.constants.impl.BDMOASEligibilityEntitlementOverrideConstants;
import curam.ca.gc.bdmoas.evidence.constants.impl.BDMOASMaritalRelationshipConstants;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.ca.gc.bdmoas.test.data.impl.BDMOASServiceSupplierTestData;
import curam.ca.gc.bdmoas.test.data.impl.BDMOASServiceSupplierTestDataDetails;
import curam.ca.gc.bdmoas.util.impl.BDMOASTaskUtil;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASEPARTICIPANTROLETYPE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.codetable.RECORDSTATUS;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.codetable.impl.EVIDENCECHANGEREASONEntry;
import curam.core.facade.struct.CreateIntegratedCaseResult;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.facade.struct.ReadServiceSupplierHomeKey;
import curam.core.fact.AddressFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.intf.Address;
import curam.core.sl.entity.fact.CaseParticipantRoleFactory;
import curam.core.sl.entity.struct.CaseParticipantRoleDtls;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor;
import curam.core.sl.infrastructure.entity.struct.CaseIDParticipantIDStatusCode;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtlsList;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorInsertDtls;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKeyList;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceInsertDtls;
import curam.core.sl.infrastructure.impl.EIEvidenceModifyDtls;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.impl.EvidenceMap;
import curam.core.sl.infrastructure.impl.StandardEvidenceInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.core.sl.struct.EvidenceDescriptorDetails;
import curam.core.sl.struct.EvidenceKey;
import curam.core.sl.struct.EvidenceTypeKey;
import curam.core.struct.AddressDtls;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.CaseKey;
import curam.core.struct.ConcernRoleKey;
import curam.creole.value.CodeTableItem;
import curam.dynamicevidence.definition.impl.EvidenceTypeDef;
import curam.dynamicevidence.definition.impl.EvidenceTypeDefDAO;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDef;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDefDAO;
import curam.dynamicevidence.impl.DynamicEvidenceDataAttributeDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.dynamicevidence.sl.impl.CpDetailsAdaptor;
import curam.dynamicevidence.sl.impl.EvidenceGenericSLFactory;
import curam.dynamicevidence.sl.impl.EvidenceServiceInterface;
import curam.dynamicevidence.sl.struct.impl.GenericSLDataDetails;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.pdc.fact.PDCUtilFactory;
import curam.pdc.impl.PDCConst;
import curam.pdc.intf.PDCUtil;
import curam.pdc.struct.PDCCaseIDCaseParticipantRoleID;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.Date;
import curam.util.workflow.impl.EnactmentService;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for dynamic evidence BDMOASEligibilityEntitlementOverride
 */
public class BDMOASEligibilityEntitlementOverrideTaskTest
  extends BDMOASCaseTest {

  private static final String EVD_ATTRIBUTE_DOB = "dateOfBirth";

  private static final String CASE_PARTICIPANT_ROLE = "caseParticipantRole";

  private static final String INCARCERATION_ATTR_OFFENDER_STATUS =
    "offenderStatus";

  private static final String INCARCERATION_ATTR_INSTITUTION = "institution";

  private static final String INCARCERATION_ATTR_OFFENDER_ID = "offenderID";

  private static final String START_DATE = "startDate";

  private static final String END_DATE = "endDate";

  public static final String IS_SPONSORED = "isSponsored";

  public static final String SPONSORSHIP = "sponsorship";

  public static final String LIVING_APART_REASON = "reasonForLivingApart";

  @Inject
  private EvidenceTypeDefDAO etDefDAO;

  @Inject
  protected EvidenceTypeVersionDefDAO etVerDefDAO;

  @Override
  protected void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    mockEnactmentService();

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Test that an override cannot be created for a non-primary member on an OAS
   * IC.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void ageOverrideTaskSuccess()
    throws AppException, InformationalException {

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

    this.createEvidence(integratedCase.integratedCaseID,
      person.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.OAS_ELG_ENTITLEMNT_OVERRIDE, attributes, getToday());

    // Apply Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCase.integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    evidenceController.applyAllChanges(caseKey);

    // Modify Date of Birth on PDC Birth and Death Evidence
    final HashMap<String, Object> birthAndDeathAttributes =
      new HashMap<String, Object>();
    birthAndDeathAttributes.put(EVD_ATTRIBUTE_DOB,
      getPastDateFromInputDateByYears(Date.getCurrentDate(), 65));
    modifyPDCBirthAndDeathDetailsEvidence(
      person.registrationIDDetails.concernRoleID, birthAndDeathAttributes);

    // Modify PDC Birth and Death Details
    modifyPDCBirthAndDeathDetailsEvidence(
      person.registrationIDDetails.concernRoleID, birthAndDeathAttributes);

    final BDMOASTaskUtil taskUtil = new BDMOASTaskUtil();
    final Long taskID = taskUtil.getOverrideEvidenceTaskIDByCaseIDTaskType(
      integratedCase.integratedCaseID, BDMOASOVERRIDEEVDTASKTYPEEntry.AGE);

    assertTrue(taskID != 0l);
  }

  @Test
  public void ageOverrideForSeventyFifthBirthMonthTaskSuccess()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();

    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCase.integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    // Create Application Details Evidence for OAS Benefit
    createApplicationDetailsEvidence(
      person.registrationIDDetails.concernRoleID,
      integratedCase.integratedCaseID, BDMOASBENEFITTYPE.OAS_PENSION);

    // Apply Evidence changes
    evidenceController.applyAllChanges(caseKey);

    // Create Eligibility Entitlement Override Evidence for OAS Pension Benefit
    // and Entitlement Amount override
    createEligibilityEntitlementOverrideEvidence(
      person.registrationIDDetails.concernRoleID,
      integratedCase.integratedCaseID,
      BDMOASOVERRIDEBENEFITTYPE.OLD_AGE_SECURITY_PENSION);

    // Apply Evidence changes
    evidenceController.applyAllChanges(caseKey);

    // Modify Date of Birth on PDC Birth and Death Evidence
    final HashMap<String, Object> birthAndDeathAttributes =
      new HashMap<String, Object>();
    birthAndDeathAttributes.put(EVD_ATTRIBUTE_DOB,
      getPastDateFromInputDateByYears(Date.getCurrentDate(), 75));
    modifyPDCBirthAndDeathDetailsEvidence(
      person.registrationIDDetails.concernRoleID, birthAndDeathAttributes);

    // Modify PDC Birth and Death Details
    modifyPDCBirthAndDeathDetailsEvidence(
      person.registrationIDDetails.concernRoleID, birthAndDeathAttributes);

    final BDMOASTaskUtil taskUtil = new BDMOASTaskUtil();
    final Long taskID = taskUtil.getOverrideEvidenceTaskIDByCaseIDTaskType(
      integratedCase.integratedCaseID, BDMOASOVERRIDEEVDTASKTYPEEntry.AGE);

    assertTrue(taskID != 0l);
  }

  @Test
  public void incarcerationOverrideTaskSuccess()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();

    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    // Apply Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCase.integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    // Create Application Details Evidence for GIS Benefit
    createApplicationDetailsEvidence(
      person.registrationIDDetails.concernRoleID,
      integratedCase.integratedCaseID,
      BDMOASBENEFITTYPE.GUARANTEED_INCOME_SUPPLEMENT);

    evidenceController.applyAllChanges(caseKey);

    // Create Eligibility Entitlement Override Evidence for GIS Benefit and
    // Entitlement Amount override
    createEligibilityEntitlementOverrideEvidence(
      person.registrationIDDetails.concernRoleID,
      integratedCase.integratedCaseID,
      BDMOASOVERRIDEBENEFITTYPE.GUARANTEED_INCOME_SUPPLEMENT);

    evidenceController.applyAllChanges(caseKey);

    // Create Incarceration Evidence
    createIncarcerationEvidence(person.registrationIDDetails.concernRoleID,
      integratedCase.integratedCaseID);

    final BDMOASTaskUtil taskUtil = new BDMOASTaskUtil();
    final Long taskID = taskUtil.getOverrideEvidenceTaskIDByCaseIDTaskType(
      integratedCase.integratedCaseID,
      BDMOASOVERRIDEEVDTASKTYPEEntry.INCARCERATION);

    assertTrue(taskID != 0l);
  }

  @Test
  public void sponsorshipAgreementOverrideTaskSuccess()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();

    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    // Apply Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCase.integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    // Create Application Details Evidence for GIS Benefit
    createApplicationDetailsEvidence(
      person.registrationIDDetails.concernRoleID,
      integratedCase.integratedCaseID,
      BDMOASBENEFITTYPE.GUARANTEED_INCOME_SUPPLEMENT);

    evidenceController.applyAllChanges(caseKey);

    // Create Eligibility Entitlement Override Evidence for GIS Benefit and
    // Entitlement Amount override
    createEligibilityEntitlementOverrideEvidence(
      person.registrationIDDetails.concernRoleID,
      integratedCase.integratedCaseID,
      BDMOASOVERRIDEBENEFITTYPE.GUARANTEED_INCOME_SUPPLEMENT);

    evidenceController.applyAllChanges(caseKey);

    // Sponsorship Agreement Evidence
    final Map<String, String> sponsorshipAttributes =
      new HashMap<String, String>();
    sponsorshipAttributes.put(CASE_PARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    sponsorshipAttributes.put(IS_SPONSORED, BDMYESNO.YES);

    final EIEvidenceKey eiEvidenceKey =
      this.createEvidence(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID,
        CASEEVIDENCEEntry.OAS_SPONSORSHIP, sponsorshipAttributes, getToday());

    final EvidenceTypeKey evidenceTypeKey = new EvidenceTypeKey();
    evidenceTypeKey.evidenceType = CASEEVIDENCE.OAS_SPONSORSHIP_AGREEMENT;

    final GenericSLDataDetails genericDtls = new GenericSLDataDetails();

    final EvidenceKey evidenceKey = new EvidenceKey();
    evidenceKey.evidenceID = eiEvidenceKey.evidenceID;
    evidenceKey.evType = CASEEVIDENCE.OAS_SPONSORSHIP;

    genericDtls.addParent(CASEEVIDENCE.OAS_SPONSORSHIP, evidenceKey);
    final EvidenceServiceInterface evidenceServiceInterface =
      EvidenceGenericSLFactory.instance(evidenceTypeKey,
        Date.getCurrentDate());

    final DynamicEvidenceDataDetails dynamicEvidencedataDetails =
      DynamicEvidenceDataDetailsFactory
        .newInstance(evidenceTypeKey.evidenceType, Date.getCurrentDate());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(START_DATE),
      Date.getCurrentDate().addDays(-10));

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(END_DATE),
      Date.getCurrentDate());

    final EvidenceDescriptorDetails descriptor =
      new EvidenceDescriptorDetails();
    descriptor.evidenceType = evidenceTypeKey.evidenceType;
    descriptor.caseID = integratedCase.integratedCaseID;
    descriptor.receivedDate = Date.getCurrentDate();
    descriptor.participantID = person.registrationIDDetails.concernRoleID;
    descriptor.changeReason = EVIDENCECHANGEREASON.INITIAL;

    genericDtls.setData(dynamicEvidencedataDetails);
    genericDtls.setDescriptor(descriptor);
    genericDtls.setCaseIdKey(descriptor.caseID);

    evidenceServiceInterface.createEvidence(genericDtls);

    final BDMOASTaskUtil taskUtil = new BDMOASTaskUtil();
    final Long taskID = taskUtil.getOverrideEvidenceTaskIDByCaseIDTaskType(
      integratedCase.integratedCaseID,
      BDMOASOVERRIDEEVDTASKTYPEEntry.SPONSORSHIP_AGREEMENT);

    assertTrue(taskID != 0l);
  }

  @Test
  public void livingApartOverrideTaskSuccess()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();

    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    // Apply Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCase.integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    // Create Application Details Evidence for GIS Benefit
    createApplicationDetailsEvidence(
      person.registrationIDDetails.concernRoleID,
      integratedCase.integratedCaseID,
      BDMOASBENEFITTYPE.GUARANTEED_INCOME_SUPPLEMENT);

    evidenceController.applyAllChanges(caseKey);

    // Create Eligibility Entitlement Override Evidence for GIS Benefit and
    // Entitlement Amount override
    createEligibilityEntitlementOverrideEvidence(
      person.registrationIDDetails.concernRoleID,
      integratedCase.integratedCaseID,
      BDMOASOVERRIDEBENEFITTYPE.GUARANTEED_INCOME_SUPPLEMENT);

    evidenceController.applyAllChanges(caseKey);

    // Living Apart for Reasons Beyond Control Evidence
    final Map<String, String> maritalRelationship =
      new HashMap<String, String>();

    maritalRelationship.put(
      BDMOASMaritalRelationshipConstants.CASE_PARTICIPANT_ROLE_ID,
      String.valueOf(cprObj.caseParticipantRoleID));

    maritalRelationship.put(
      BDMOASMaritalRelationshipConstants.RELATIONSHIP_STATUS,
      BDMOASMARITALRELATION.OAS_MARITAL_RELATION_MARRIED);

    maritalRelationship.put(BDMOASMaritalRelationshipConstants.START_DATE,
      formattedDate(Date.getCurrentDate().addDays(-100)));

    final EIEvidenceKey eiEvidenceKey =
      this.createEvidence(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID,
        CASEEVIDENCEEntry.OAS_MARITAL_RELATIONSHIP, maritalRelationship,
        getToday());

    final EvidenceTypeKey evidenceTypeKey = new EvidenceTypeKey();
    evidenceTypeKey.evidenceType =
      CASEEVIDENCE.OAS_LIVING_APART_FOR_REASONS_BEYOND_CONTROL;

    final GenericSLDataDetails genericDtls = new GenericSLDataDetails();

    final EvidenceKey evidenceKey = new EvidenceKey();
    evidenceKey.evidenceID = eiEvidenceKey.evidenceID;
    evidenceKey.evType = CASEEVIDENCE.OAS_MARITAL_RELATIONSHIP;

    genericDtls.addParent(CASEEVIDENCE.OAS_MARITAL_RELATIONSHIP, evidenceKey);

    final EvidenceServiceInterface evidenceServiceInterface =
      EvidenceGenericSLFactory.instance(evidenceTypeKey,
        Date.getCurrentDate());

    final DynamicEvidenceDataDetails dynamicEvidencedataDetails =
      DynamicEvidenceDataDetailsFactory
        .newInstance(evidenceTypeKey.evidenceType, Date.getCurrentDate());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(START_DATE),
      Date.getCurrentDate().addDays(-5));

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(LIVING_APART_REASON),
      new CodeTableItem(BDMOASLIVINGAPARTREASON.TABLENAME,
        BDMOASLIVINGAPARTREASON.OAS_EMPLOYMENT));

    final EvidenceDescriptorDetails descriptor =
      new EvidenceDescriptorDetails();
    descriptor.evidenceType = evidenceTypeKey.evidenceType;
    descriptor.caseID = integratedCase.integratedCaseID;
    descriptor.receivedDate = Date.getCurrentDate();
    descriptor.participantID = person.registrationIDDetails.concernRoleID;
    descriptor.changeReason = EVIDENCECHANGEREASON.INITIAL;

    genericDtls.setData(dynamicEvidencedataDetails);
    genericDtls.setDescriptor(descriptor);
    genericDtls.setCaseIdKey(descriptor.caseID);

    // create evidence record
    evidenceServiceInterface.createEvidence(genericDtls);

    final BDMOASTaskUtil taskUtil = new BDMOASTaskUtil();
    final Long taskID = taskUtil.getOverrideEvidenceTaskIDByCaseIDTaskType(
      integratedCase.integratedCaseID,
      BDMOASOVERRIDEEVDTASKTYPEEntry.LIVING_APART);

    assertTrue(taskID != 0l);
  }

  @Test
  public void deferralMonthsOverrideTaskSuccess()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();

    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    // Apply Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCase.integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    // Create Application Details Evidence for OAS Benefit
    createApplicationDetailsEvidence(
      person.registrationIDDetails.concernRoleID,
      integratedCase.integratedCaseID, BDMOASBENEFITTYPE.OAS_PENSION);

    // Create Application Details Evidence for GIS Benefit
    createApplicationDetailsEvidence(
      person.registrationIDDetails.concernRoleID,
      integratedCase.integratedCaseID,
      BDMOASBENEFITTYPE.GUARANTEED_INCOME_SUPPLEMENT);

    evidenceController.applyAllChanges(caseKey);

    // Create Eligibility Entitlement Override Evidence for GIS Benefit and
    // Entitlement Amount override
    createEligibilityEntitlementOverrideEvidence(
      person.registrationIDDetails.concernRoleID,
      integratedCase.integratedCaseID,
      BDMOASOVERRIDEBENEFITTYPE.GUARANTEED_INCOME_SUPPLEMENT);

    // Create Eligibility Entitlement Override Evidence for Deferral Months
    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.PARTICIPANT,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.BENEFIT_TYPE,
      BDMOASOVERRIDEBENEFITTYPE.OLD_AGE_SECURITY_PENSION);
    attributes.put(
      BDMOASEligibilityEntitlementOverrideConstants.OVERRIDE_VALUE,
      BDMOASOVERRIDEVALUE.DEFERRAL_MONTHS);
    attributes
      .put(BDMOASEligibilityEntitlementOverrideConstants.NUMBER_AMOUNT, "10");
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.REASON,
      BDMOASOVERRIDEREASON.MINISTER);

    final EIEvidenceKey activeDeferralMonthsOverrideEvdKey =
      this.createEvidence(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID,
        CASEEVIDENCEEntry.OAS_ELG_ENTITLEMNT_OVERRIDE, attributes,
        getToday());

    evidenceController.applyAllChanges(caseKey);

    // Modify Deferral Months override value

    // Read DynamicEvidenceDataDetails
    final EvidenceMap map =
      curam.core.sl.infrastructure.impl.EvidenceController.getEvidenceMap();
    final StandardEvidenceInterface standardEvidenceInterface =
      map.getEvidenceType(
        CASEEVIDENCEEntry.OAS_ELG_ENTITLEMNT_OVERRIDE.getCode());

    final DynamicEvidenceDataDetails evidenceData =
      (DynamicEvidenceDataDetails) standardEvidenceInterface
        .readEvidence(activeDeferralMonthsOverrideEvdKey);

    // Modify evidence data
    final DynamicEvidenceDataAttributeDetails attributeObj =
      evidenceData.getAttribute(
        BDMOASEligibilityEntitlementOverrideConstants.NUMBER_AMOUNT);

    DynamicEvidenceTypeConverter.setAttribute(attributeObj, 11);

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    // evidence descriptor details for this evidence
    final RelatedIDAndEvidenceTypeKey relatedIDAndTypeKey =
      new RelatedIDAndEvidenceTypeKey();
    relatedIDAndTypeKey.relatedID =
      activeDeferralMonthsOverrideEvdKey.evidenceID;
    relatedIDAndTypeKey.evidenceType =
      CASEEVIDENCEEntry.OAS_ELG_ENTITLEMNT_OVERRIDE.getCode();

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

    evidenceControllerObj.modifyEvidence(activeDeferralMonthsOverrideEvdKey,
      modifyEvidenceDetails);

    final BDMOASTaskUtil taskUtil = new BDMOASTaskUtil();
    final Long taskID = taskUtil.getOverrideEvidenceTaskIDByCaseIDTaskType(
      integratedCase.integratedCaseID,
      BDMOASOVERRIDEEVDTASKTYPEEntry.DEFERRAL_MONTHS);

    assertTrue(taskID != 0l);
  }

  @Test
  public void fortiethsOverrideTaskSuccess()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();

    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    // Apply Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCase.integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    // Create Application Details Evidence for OAS Benefit
    createApplicationDetailsEvidence(
      person.registrationIDDetails.concernRoleID,
      integratedCase.integratedCaseID, BDMOASBENEFITTYPE.OAS_PENSION);

    // Create Application Details Evidence for GIS Benefit
    createApplicationDetailsEvidence(
      person.registrationIDDetails.concernRoleID,
      integratedCase.integratedCaseID,
      BDMOASBENEFITTYPE.GUARANTEED_INCOME_SUPPLEMENT);

    evidenceController.applyAllChanges(caseKey);

    // Create Eligibility Entitlement Override Evidence for GIS Benefit and
    // Entitlement Amount override
    createEligibilityEntitlementOverrideEvidence(
      person.registrationIDDetails.concernRoleID,
      integratedCase.integratedCaseID,
      BDMOASOVERRIDEBENEFITTYPE.GUARANTEED_INCOME_SUPPLEMENT);

    // Create Eligibility Entitlement Override Evidence for Fortieths
    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.PARTICIPANT,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.BENEFIT_TYPE,
      BDMOASOVERRIDEBENEFITTYPE.OLD_AGE_SECURITY_PENSION);
    attributes.put(
      BDMOASEligibilityEntitlementOverrideConstants.OVERRIDE_VALUE,
      BDMOASOVERRIDEVALUE.FORTIETHS);
    attributes
      .put(BDMOASEligibilityEntitlementOverrideConstants.NUMBER_AMOUNT, "20");
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.REASON,
      BDMOASOVERRIDEREASON.MINISTER);

    final EIEvidenceKey activeDeferralMonthsOverrideEvdKey =
      this.createEvidence(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID,
        CASEEVIDENCEEntry.OAS_ELG_ENTITLEMNT_OVERRIDE, attributes,
        getToday());

    evidenceController.applyAllChanges(caseKey);

    // Modify Fortieths override value

    // Read DynamicEvidenceDataDetails
    final EvidenceMap map =
      curam.core.sl.infrastructure.impl.EvidenceController.getEvidenceMap();
    final StandardEvidenceInterface standardEvidenceInterface =
      map.getEvidenceType(
        CASEEVIDENCEEntry.OAS_ELG_ENTITLEMNT_OVERRIDE.getCode());

    final DynamicEvidenceDataDetails evidenceData =
      (DynamicEvidenceDataDetails) standardEvidenceInterface
        .readEvidence(activeDeferralMonthsOverrideEvdKey);

    // Modify evidence data
    final DynamicEvidenceDataAttributeDetails attributeObj =
      evidenceData.getAttribute(
        BDMOASEligibilityEntitlementOverrideConstants.NUMBER_AMOUNT);

    DynamicEvidenceTypeConverter.setAttribute(attributeObj, 21);

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    // evidence descriptor details for this evidence
    final RelatedIDAndEvidenceTypeKey relatedIDAndTypeKey =
      new RelatedIDAndEvidenceTypeKey();
    relatedIDAndTypeKey.relatedID =
      activeDeferralMonthsOverrideEvdKey.evidenceID;
    relatedIDAndTypeKey.evidenceType =
      CASEEVIDENCEEntry.OAS_ELG_ENTITLEMNT_OVERRIDE.getCode();

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

    evidenceControllerObj.modifyEvidence(activeDeferralMonthsOverrideEvdKey,
      modifyEvidenceDetails);

    final BDMOASTaskUtil taskUtil = new BDMOASTaskUtil();
    final Long taskID = taskUtil.getOverrideEvidenceTaskIDByCaseIDTaskType(
      integratedCase.integratedCaseID,
      BDMOASOVERRIDEEVDTASKTYPEEntry.FORTIETH);

    assertTrue(taskID != 0l);
  }

  @Test
  public void countryOfResidenceOverrideTaskSuccess()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();

    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    // Apply Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCase.integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    // Create Application Details Evidence for GIS Benefit
    createApplicationDetailsEvidence(
      person.registrationIDDetails.concernRoleID,
      integratedCase.integratedCaseID,
      BDMOASBENEFITTYPE.GUARANTEED_INCOME_SUPPLEMENT);

    evidenceController.applyAllChanges(caseKey);

    // Create Eligibility Entitlement Override Evidence for GIS Benefit and
    // Entitlement Amount override
    createEligibilityEntitlementOverrideEvidence(
      person.registrationIDDetails.concernRoleID,
      integratedCase.integratedCaseID,
      BDMOASOVERRIDEBENEFITTYPE.GUARANTEED_INCOME_SUPPLEMENT);

    evidenceController.applyAllChanges(caseKey);

    // End date current residential address
    endDateParticipantResidentialAddress(
      person.registrationIDDetails.concernRoleID);

    // Add different country residential address
    createAddressEvidence(person.registrationIDDetails.concernRoleID,
      CONCERNROLEADDRESSTYPE.PRIVATE, Date.getCurrentDate(), null);

    final BDMOASTaskUtil taskUtil = new BDMOASTaskUtil();
    final Long taskID = taskUtil.getOverrideEvidenceTaskIDByCaseIDTaskType(
      integratedCase.integratedCaseID,
      BDMOASOVERRIDEEVDTASKTYPEEntry.COUNTRY);

    assertTrue(taskID != 0l);
  }

  @Test
  public void maritalStatusOverrideTaskSuccess()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();

    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    // Apply Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCase.integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    // Create Application Details Evidence for GIS Benefit
    createApplicationDetailsEvidence(
      person.registrationIDDetails.concernRoleID,
      integratedCase.integratedCaseID,
      BDMOASBENEFITTYPE.GUARANTEED_INCOME_SUPPLEMENT);

    evidenceController.applyAllChanges(caseKey);

    // Create Eligibility Entitlement Override Evidence for GIS Benefit and
    // Entitlement Amount override
    createEligibilityEntitlementOverrideEvidence(
      person.registrationIDDetails.concernRoleID,
      integratedCase.integratedCaseID,
      BDMOASOVERRIDEBENEFITTYPE.GUARANTEED_INCOME_SUPPLEMENT);

    evidenceController.applyAllChanges(caseKey);

    // Create Marital Status evidence with status of Married
    final curam.core.sl.struct.EvidenceKey maritalStatusKey =
      createMaritalStatusEvidence(person.registrationIDDetails.concernRoleID);

    // Update Marital Status evidence with status of Common-Law
    updateMaritalStatusEvidence(maritalStatusKey);

    final BDMOASTaskUtil taskUtil = new BDMOASTaskUtil();
    final Long taskID = taskUtil.getOverrideEvidenceTaskIDByCaseIDTaskType(
      integratedCase.integratedCaseID,
      BDMOASOVERRIDEEVDTASKTYPEEntry.MARITAL_STATUS);

    assertTrue(taskID != 0l);
  }

  private void createEligibilityEntitlementOverrideEvidence(
    final Long concernRoleID, final Long caseID,
    final String overrideBenefitType)
    throws AppException, InformationalException {

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(caseID, concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.PARTICIPANT,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.BENEFIT_TYPE,
      overrideBenefitType);
    attributes.put(
      BDMOASEligibilityEntitlementOverrideConstants.OVERRIDE_VALUE,
      BDMOASOVERRIDEVALUE.ENTITLEMENT_AMOUNT);
    attributes.put(
      BDMOASEligibilityEntitlementOverrideConstants.DOLLAR_AMOUNT, "200");
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.START_DATE,
      formattedDateMovedToFirstOfMonth(Date.getCurrentDate()));
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.REASON,
      BDMOASOVERRIDEREASON.MINISTER);

    this.createEvidence(caseID, concernRoleID,
      CASEEVIDENCEEntry.OAS_ELG_ENTITLEMNT_OVERRIDE, attributes, getToday());
  }

  private void createApplicationDetailsEvidence(final Long concernRoleID,
    final Long caseID, final String benefitType)
    throws AppException, InformationalException {

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(caseID, concernRoleID);

    final Map<String, String> applicationDetailsAttributes =
      new HashMap<String, String>();
    applicationDetailsAttributes.put("caseParticipantRole",
      String.valueOf(cprObj.caseParticipantRoleID));
    applicationDetailsAttributes
      .put(BDMOASApplicationDetailsConstants.BENEFIT_TYPE, benefitType);
    applicationDetailsAttributes.put(
      BDMOASApplicationDetailsConstants.APPLICATION_STATUS,
      BDMOASAPPLICATIONSTATUS.ACTIVE);
    applicationDetailsAttributes.put(
      BDMOASApplicationDetailsConstants.METHOD_OF_APPLICATION,
      BDMOASMETHODOFAPPLICATION.ONLINE);
    applicationDetailsAttributes.put(
      BDMOASApplicationDetailsConstants.RECEIPT_DATE,
      formattedDateMovedToFirstOfMonth(Date.getCurrentDate()));

    this.createEvidence(caseID, concernRoleID,
      CASEEVIDENCEEntry.OAS_APPLICATION_DETAILS, applicationDetailsAttributes,
      getToday());
  }

  private void updateMaritalStatusEvidence(
    final curam.core.sl.struct.EvidenceKey participantMaritalStatusEvidenceKey)
    throws AppException, InformationalException {

    // Read DynamicEvidenceDataDetails
    final EvidenceMap map =
      curam.core.sl.infrastructure.impl.EvidenceController.getEvidenceMap();
    final StandardEvidenceInterface standardEvidenceInterface =
      map.getEvidenceType(CASEEVIDENCE.BDM_MARITAL_STATUS);

    final EIEvidenceKey evidenceKey = new EIEvidenceKey();
    evidenceKey.evidenceID = participantMaritalStatusEvidenceKey.evidenceID;
    evidenceKey.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;

    final DynamicEvidenceDataDetails evidenceData =
      (DynamicEvidenceDataDetails) standardEvidenceInterface
        .readEvidence(evidenceKey);

    // update the Marital Status
    final DynamicEvidenceDataAttributeDetails attributeObj =
      evidenceData.getAttribute("maritalStatus");

    DynamicEvidenceTypeConverter.setAttribute(attributeObj, new CodeTableItem(
      BDMMARITALSTATUS.TABLENAME, BDMMARITALSTATUS.COMMONLAW));

    // evidence descriptor details for this evidence
    final RelatedIDAndEvidenceTypeKey relatedIDAndTypeKey =
      new RelatedIDAndEvidenceTypeKey();
    relatedIDAndTypeKey.relatedID =
      participantMaritalStatusEvidenceKey.evidenceID;
    relatedIDAndTypeKey.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;

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
      EVIDENCECHANGEREASON.REPORTEDBYCLIENT;
    modifyEvidenceDetails.descriptor.effectiveFrom = Date.getCurrentDate();

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    evidenceControllerObj.modifyEvidence(evidenceKey, modifyEvidenceDetails);
  }

  private curam.core.sl.struct.EvidenceKey createMaritalStatusEvidence(
    final long concernRoleID) throws AppException, InformationalException {

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    concernRoleKey.concernRoleID = concernRoleID;
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;

    // get Latest Version of Evidence
    final GenericSLDataDetails genericDtls = new GenericSLDataDetails();
    final EvidenceServiceInterface evidenceServiceInterface =
      EvidenceGenericSLFactory.instance(eType, Date.getCurrentDate());

    final DynamicEvidenceDataDetails dynamicEvidencedataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(eType.evidenceType,
        Date.getCurrentDate());
    // assigning all the required attributes
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute("caseParticipantRoleID"),
      pdcCaseIDCaseParticipantRoleID.caseParticipantRoleID);
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(START_DATE),
      Date.getCurrentDate().addDays(-100));
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute("maritalStatus"),
      new CodeTableItem(BDMMARITALSTATUS.TABLENAME,
        BDMMARITALSTATUS.MARRIED));

    // set descriptor attributes to call OOTB Evidence creation logic
    final EvidenceDescriptorDetails descriptor =
      new EvidenceDescriptorDetails();
    descriptor.evidenceType = eType.evidenceType;
    descriptor.caseID = pdcCaseIDCaseParticipantRoleID.caseID;
    descriptor.receivedDate = Date.getCurrentDate();
    descriptor.participantID = concernRoleID;
    descriptor.changeReason = EVIDENCECHANGEREASON.INITIAL;

    genericDtls.setData(dynamicEvidencedataDetails);
    genericDtls.setDescriptor(descriptor);
    genericDtls.setCaseIdKey(descriptor.caseID);

    final CpDetailsAdaptor cpAdaptor = new CpDetailsAdaptor();
    cpAdaptor.setCaseParticipantRoleID(
      pdcCaseIDCaseParticipantRoleID.caseParticipantRoleID);
    genericDtls.addRelCp("caseParticipantRoleID", cpAdaptor);

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
  private void modifyPDCBirthAndDeathDetailsEvidence(final Long concernRoleID,
    final HashMap<String, Object> modifyData)
    throws AppException, InformationalException {

    // PDC case key
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    concernRoleKey.concernRoleID = concernRoleID;
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = pdcCaseIDCaseParticipantRoleID.caseID;

    final BDMEvidenceUtil evdUtil = new BDMEvidenceUtil();

    // Read PDC Birth and Death details evidences
    final RelatedIDAndEvidenceTypeKeyList pdcBirthAndDeathEvidenceList =
      evdUtil.getActiveEvidenceIDByEvidenceTypeAndCase(
        PDCConst.PDCBIRTHANDDEATH, caseKey);

    // Pick one evidence details from evidence list
    final RelatedIDAndEvidenceTypeKey evidenceDetails =
      pdcBirthAndDeathEvidenceList.dtls.get(0);

    // Read DynamicEvidenceDataDetails
    final EvidenceMap map =
      curam.core.sl.infrastructure.impl.EvidenceController.getEvidenceMap();
    final StandardEvidenceInterface standardEvidenceInterface =
      map.getEvidenceType(PDCConst.PDCBIRTHANDDEATH);

    final EIEvidenceKey evidenceKey = new EIEvidenceKey();
    evidenceKey.evidenceID = evidenceDetails.relatedID;
    evidenceKey.evidenceType = PDCConst.PDCBIRTHANDDEATH;

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
    relatedIDAndTypeKey.relatedID = evidenceDetails.relatedID;
    relatedIDAndTypeKey.evidenceType = PDCConst.PDCBIRTHANDDEATH;

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

  private void endDateParticipantResidentialAddress(final Long concernRoleID)
    throws AppException, InformationalException {

    // PDC case key
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    concernRoleKey.concernRoleID = concernRoleID;
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);

    final Long caseID = pdcCaseIDCaseParticipantRoleID.caseID;

    // Search Active Residential Address records
    final EvidenceDescriptor evidenceDescriptorObj =
      EvidenceDescriptorFactory.newInstance();
    final CaseIDParticipantIDStatusCode paramCaseIDParticipantIDStatusCode =
      new CaseIDParticipantIDStatusCode();
    paramCaseIDParticipantIDStatusCode.caseID = caseID;
    paramCaseIDParticipantIDStatusCode.participantID = concernRoleID;
    paramCaseIDParticipantIDStatusCode.statusCode =
      EVIDENCEDESCRIPTORSTATUS.ACTIVE;
    final EvidenceDescriptorDtlsList allActiveEvdList = evidenceDescriptorObj
      .searchActiveByCaseIDParticipantID(paramCaseIDParticipantIDStatusCode);

    final List<EvidenceDescriptorDtls> activeAddressListForParticipant =
      allActiveEvdList.dtls.stream()
        .filter(ed -> ed.evidenceType.equals(CASEEVIDENCE.BDMADDRESS))
        .collect(Collectors.toList());

    final EvidenceControllerInterface evidenceControllerInterface =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    EIEvidenceKey activeResidentialAddressKey = new EIEvidenceKey();

    for (final EvidenceDescriptorDtls evidenceDescriptorDtls : activeAddressListForParticipant) {

      // Read address evidence data
      final EIEvidenceKey evidenceKey = new EIEvidenceKey();
      evidenceKey.evidenceID = evidenceDescriptorDtls.relatedID;
      evidenceKey.evidenceType = evidenceDescriptorDtls.evidenceType;
      final EIEvidenceReadDtls evidenceReadDtls =
        evidenceControllerInterface.readEvidence(evidenceKey);

      final DynamicEvidenceDataDetails evidenceData =
        (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;

      final Boolean isResidentialAddress =
        evidenceData.getAttribute("addressType").getValue()
          .equals(CONCERNROLEADDRESSTYPE.PRIVATE);

      if (isResidentialAddress) {
        activeResidentialAddressKey = evidenceKey;
      }
    }

    // Read DynamicEvidenceDataDetails
    final EvidenceMap map =
      curam.core.sl.infrastructure.impl.EvidenceController.getEvidenceMap();
    final StandardEvidenceInterface standardEvidenceInterface =
      map.getEvidenceType(CASEEVIDENCE.BDMADDRESS);

    final DynamicEvidenceDataDetails evidenceData =
      (DynamicEvidenceDataDetails) standardEvidenceInterface
        .readEvidence(activeResidentialAddressKey);

    final DynamicEvidenceDataAttributeDetails fromDate =
      evidenceData.getAttribute("fromDate");
    DynamicEvidenceTypeConverter.setAttribute(fromDate,
      Date.getCurrentDate().addDays(-10));
    final DynamicEvidenceDataAttributeDetails toDate =
      evidenceData.getAttribute("toDate");
    DynamicEvidenceTypeConverter.setAttribute(toDate,
      Date.getCurrentDate().addDays(-1));

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    // evidence descriptor details for this evidence
    final RelatedIDAndEvidenceTypeKey relatedIDAndTypeKey =
      new RelatedIDAndEvidenceTypeKey();
    relatedIDAndTypeKey.relatedID = activeResidentialAddressKey.evidenceID;
    relatedIDAndTypeKey.evidenceType = CASEEVIDENCE.BDMADDRESS;

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

    evidenceControllerObj.modifyEvidence(activeResidentialAddressKey,
      modifyEvidenceDetails);
  }

  private void createIncarcerationEvidence(final Long concernRoleID,
    final Long caseID) throws AppException, InformationalException {

    final BDMUtil util = new BDMUtil();
    final curam.core.sl.struct.CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(caseID, concernRoleID);

    final BDMOASServiceSupplierTestData servSuppTestDateObj =
      new BDMOASServiceSupplierTestData();

    final BDMOASServiceSupplierTestDataDetails servSuppTestDataDetails =
      servSuppTestDateObj.setRegisterServiceSupplierDetails();

    final ReadServiceSupplierHomeKey key = new ReadServiceSupplierHomeKey();
    key.concernRoleHomePageKey.concernRoleID =
      servSuppTestDataDetails.servSuppCRoleID;

    final CaseParticipantRoleDtls caseParticipantRoleDtls =
      new CaseParticipantRoleDtls();
    caseParticipantRoleDtls.caseID = caseID;
    caseParticipantRoleDtls.fromDate = Date.getCurrentDate();
    caseParticipantRoleDtls.typeCode = CASEPARTICIPANTROLETYPE.BDMINST;
    caseParticipantRoleDtls.participantRoleID =
      servSuppTestDataDetails.servSuppCRoleID;
    caseParticipantRoleDtls.recordStatus = RECORDSTATUS.DEFAULTCODE;
    CaseParticipantRoleFactory.newInstance().insert(caseParticipantRoleDtls);

    // Evidence Details
    final Map<String, String> attributes = new HashMap<String, String>();

    attributes.put(INCARCERATION_ATTR_OFFENDER_STATUS,
      BDMOFFENDERSTATUS.INCARCERATED);
    attributes.put(INCARCERATION_ATTR_OFFENDER_ID, "123");
    attributes.put(START_DATE, formattedDate(Date.getCurrentDate()));
    attributes.put(CASE_PARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));

    attributes.put(INCARCERATION_ATTR_INSTITUTION,
      String.valueOf(caseParticipantRoleDtls.caseParticipantRoleID));

    this.createEvidence(caseID, concernRoleID,
      CASEEVIDENCEEntry.BDMINCARCERATION, attributes, getToday());
  }

  private String formattedDateMovedToFirstOfMonth(final Date date) {

    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    // move date to the first of the month
    final Calendar cal = date.getCalendar();
    cal.set(Calendar.DAY_OF_MONTH, 1);
    final Date firstOfMonthDate = new Date(cal);

    return dateFormat.format(firstOfMonthDate.getCalendar().getTime());
  }

  private String formattedDate(final Date date) {

    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    return dateFormat.format(date.getCalendar().getTime());
  }

  private Date getPastDateFromInputDateByYears(final Date inputDate,
    final int years) {

    final Calendar seventyFifthBirthdayCal = inputDate.getCalendar();
    seventyFifthBirthdayCal.add(Calendar.YEAR, 0 - years);

    return new Date(seventyFifthBirthdayCal);
  }

  protected EIEvidenceKey createAddressEvidence(final Long concernRoleID,
    final String addresType, final Date fromDate, final Date toDate)
    throws AppException, InformationalException {

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

    concernRoleKey.concernRoleID = concernRoleID;

    // Get the PDC case id and primary case participant role for that case.
    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = PDCConst.PDCADDRESS;

    final EvidenceTypeDef evidenceType =
      etDefDAO.readActiveEvidenceTypeDefByTypeCode(eType.evidenceType);

    final EvidenceTypeVersionDef evTypeVersion =
      etVerDefDAO.getActiveEvidenceTypeVersionAtDate(evidenceType,
        Date.getCurrentDate());

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(evTypeVersion);

    final DynamicEvidenceDataAttributeDetails participant =
      dynamicEvidenceDataDetails.getAttribute("participant");

    DynamicEvidenceTypeConverter.setAttribute(participant,
      pdcCaseIDCaseParticipantRoleID.caseParticipantRoleID);

    assignAddressEvidenceDetails(dynamicEvidenceDataDetails, addresType,
      fromDate, toDate);

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    // Call the EvidenceController object and insert evidence
    final EvidenceDescriptorInsertDtls evidenceDescriptorInsertDtls =
      new EvidenceDescriptorInsertDtls();

    evidenceDescriptorInsertDtls.participantID = concernRoleID;
    evidenceDescriptorInsertDtls.evidenceType = eType.evidenceType;
    evidenceDescriptorInsertDtls.receivedDate = Date.getCurrentDate();

    evidenceDescriptorInsertDtls.caseID =
      pdcCaseIDCaseParticipantRoleID.caseID;

    // Evidence Interface details
    final EIEvidenceInsertDtls eiEvidenceInsertDtls =
      new EIEvidenceInsertDtls();

    eiEvidenceInsertDtls.descriptor.assign(evidenceDescriptorInsertDtls);
    eiEvidenceInsertDtls.descriptor.participantID = concernRoleID;
    eiEvidenceInsertDtls.descriptor.changeReason =
      EVIDENCECHANGEREASON.REPORTEDBYCLIENT;
    eiEvidenceInsertDtls.evidenceObject = dynamicEvidenceDataDetails;

    return evidenceControllerObj.insertEvidence(eiEvidenceInsertDtls);
  }

  private void assignAddressEvidenceDetails(
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails,
    final String paramAddressType, final Date paramFromDate,
    final Date paramToDate) throws AppException, InformationalException {

    final DynamicEvidenceDataAttributeDetails addressType =
      dynamicEvidenceDataDetails.getAttribute("addressType");
    DynamicEvidenceTypeConverter.setAttribute(addressType,
      new CodeTableItem(CONCERNROLEADDRESSTYPE.TABLENAME, paramAddressType));

    final DynamicEvidenceDataAttributeDetails fromDate =
      dynamicEvidenceDataDetails.getAttribute("fromDate");
    DynamicEvidenceTypeConverter.setAttribute(fromDate, paramFromDate);

    if (paramToDate != null) {
      final DynamicEvidenceDataAttributeDetails toDate =
        dynamicEvidenceDataDetails.getAttribute("toDate");
      DynamicEvidenceTypeConverter.setAttribute(toDate, paramToDate);
    }

    final AddressDtls addressDtls = new AddressDtls();
    addressDtls.addressID = UniqueIDFactory.newInstance().getNextID();
    final Address addressObj = AddressFactory.newInstance();
    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    // modify person address details
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;
    addressFieldDetails.suiteNum = "Unit 123";
    addressFieldDetails.addressLine1 = "180";
    addressFieldDetails.addressLine2 = "Richmond Road";
    addressFieldDetails.stateProvince = "STATE4";
    addressFieldDetails.city = "City";
    addressFieldDetails.zipCode = "12345";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.US;
    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;
    addressDtls.countryCode = curam.codetable.COUNTRY.US;

    addressObj.insert(addressDtls);

    final DynamicEvidenceDataAttributeDetails address =
      dynamicEvidenceDataDetails.getAttribute("address");
    DynamicEvidenceTypeConverter.setAttribute(address,
      Long.valueOf(addressDtls.addressID));

  }

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
