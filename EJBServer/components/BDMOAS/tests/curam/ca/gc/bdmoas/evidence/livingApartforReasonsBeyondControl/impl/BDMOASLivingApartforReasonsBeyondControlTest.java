package curam.ca.gc.bdmoas.evidence.livingApartforReasonsBeyondControl.impl;

import curam.advisor.facade.fact.AdvisorFactory;
import curam.advisor.facade.struct.AdviceKey;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdmoas.codetable.BDMOASLIVINGAPARTREASON;
import curam.ca.gc.bdmoas.codetable.BDMOASMARITALRELATION;
import curam.ca.gc.bdmoas.codetable.impl.BDMOASLIVINGAPARTREASONEntry;
import curam.ca.gc.bdmoas.evidence.constants.impl.BDMOASMaritalRelationshipConstants;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.ca.gc.bdmoas.impl.BDMOASConstants;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASEPARTICIPANTROLETYPE;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.GENDER;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.commonintake.entity.struct.ApplicationCaseKey;
import curam.core.facade.fact.PersonFactory;
import curam.core.facade.intf.Person;
import curam.core.facade.struct.CreateIntegratedCaseResult;
import curam.core.facade.struct.PersonRegistrationDetails;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.sl.fact.CaseParticipantRoleFactory;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.CaseParticipantRoleDetails;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.core.sl.struct.EvidenceDescriptorDetails;
import curam.core.sl.struct.EvidenceKey;
import curam.core.sl.struct.EvidenceTypeKey;
import curam.creole.execution.session.InterpretedRuleObjectFactory;
import curam.creole.execution.session.ManualRecalculation;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.ruleclass.BDMOASLivingApartforReasonsBeyondControlRuleSet.impl.BDMOASLivingApartforReasonsBeyondControl;
import curam.creole.ruleclass.BDMOASLivingApartforReasonsBeyondControlRuleSet.impl.BDMOASLivingApartforReasonsBeyondControl_Factory;
import curam.creole.ruleclass.BDMOASLivingApartforReasonsBeyondControlSummaryRuleSet.impl.SummaryInformation;
import curam.creole.ruleclass.BDMOASLivingApartforReasonsBeyondControlSummaryRuleSet.impl.SummaryInformation_Factory;
import curam.creole.ruleclass.BDMOASMaritalRelationshipRuleSet.impl.BDMOASMaritalRelationship;
import curam.creole.ruleclass.BDMOASMaritalRelationshipRuleSet.impl.BDMOASMaritalRelationship_Factory;
import curam.creole.ruleclass.DynamicEvidenceRuleSet.impl.CaseParticipantRole_Factory;
import curam.creole.ruleclass.ParticipantEntitiesRuleSet.impl.ConcernRole_Factory;
import curam.creole.storage.inmemory.InMemoryDataStorage;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class BDMOASLivingApartforReasonsBeyondControlTest
  extends BDMOASCaseTest {

  private static final String _20080101 = "20080101";

  private Session session;

  @Override
  protected void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    this.session =
      Session_Factory.getFactory().newInstance(new ManualRecalculation(),
        new InMemoryDataStorage(new InterpretedRuleObjectFactory()));

    setCaseUserRole();

  }

  private void setCaseUserRole() {

    new MockUp<TransactionInfo>() {

      @Mock
      public String getProgramUser()
        throws AppException, InformationalException {

        return "unauthenticated";

      }

    };
  }

  public static final String START_DATE = "startDate";

  public static final String END_DATE = "endDate";

  public static final String LIVING_APART_REASON = "reasonForLivingApart";

  /**
   * Return the evidence for the session
   *
   * @return
   */
  private BDMOASLivingApartforReasonsBeyondControl getEvidence() {

    return BDMOASLivingApartforReasonsBeyondControl_Factory.getFactory()
      .newInstance(this.session);

  }

  /**
   * Return the Marital Relationship evidence for the session
   *
   * @return
   */
  private BDMOASMaritalRelationship getMaritalRelationshipEvidence() {

    return BDMOASMaritalRelationship_Factory.getFactory()
      .newInstance(this.session);

  }

  /**
   * Return summary information rule class.
   *
   * @param evidence
   * @return
   */
  private SummaryInformation
    getSummary(final BDMOASLivingApartforReasonsBeyondControl evidence) {

    final SummaryInformation summary =
      SummaryInformation_Factory.getFactory().newInstance(this.session);
    summary.evidence().specifyValue(evidence);

    return summary;
  }

  /**
   * Test for summary for the Living Apart for Reasons Beyond Control
   * for reason Long-Term Care/Health Reasons
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void livingApartForReasonsBeyondControlSummary()
    throws AppException, InformationalException {

    final String summaryMessage =
      "Living apart from Case Member due to Incarceration";

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct primaryCprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final PersonRegistrationResult relatedMember = this.registerCaseMember();

    final CaseParticipantRoleIDStruct relatedMemberCprObj = this
      .getCaseMemberDetails(integratedCase.integratedCaseID, relatedMember);

    final BDMOASLivingApartforReasonsBeyondControl bdmoasLivingApartforReasonsBeyondControl =
      this.getEvidence();

    final BDMOASMaritalRelationship bdmOASMaritalRelationship =
      this.getMaritalRelationshipEvidence();

    bdmOASMaritalRelationship.caseParticipantRoleID()
      .specifyValue(primaryCprObj.caseParticipantRoleID);

    bdmOASMaritalRelationship.relatedCaseParticipantRoleID()
      .specifyValue(relatedMemberCprObj.caseParticipantRoleID);

    final curam.creole.ruleclass.CaseEntitiesRuleSet.impl.CaseParticipantRole caseParticipantRole =
      CaseParticipantRole_Factory.getFactory().newInstance(session);
    caseParticipantRole.caseParticipantRoleID()
      .specifyValue(primaryCprObj.caseParticipantRoleID);

    final curam.creole.ruleclass.CaseEntitiesRuleSet.impl.CaseParticipantRole relatedCaseParticipantRole =
      CaseParticipantRole_Factory.getFactory().newInstance(session);

    relatedCaseParticipantRole.caseParticipantRoleID()
      .specifyValue(relatedMemberCprObj.caseParticipantRoleID);

    relatedCaseParticipantRole.participantRoleID()
      .specifyValue(relatedMember.registrationIDDetails.concernRoleID);

    bdmOASMaritalRelationship.related_caseParticipantRoleID()
      .specifyValue(caseParticipantRole);

    bdmOASMaritalRelationship.related_relatedCaseParticipantRoleID()
      .specifyValue(relatedCaseParticipantRole);

    final curam.creole.ruleclass.ParticipantEntitiesRuleSet.impl.ConcernRole concernRole =
      ConcernRole_Factory.getFactory().newInstance(session);
    concernRole.concernRoleID()
      .specifyValue(relatedMember.registrationIDDetails.concernRoleID);
    concernRole.concernRoleName().specifyValue("Case Member");

    bdmOASMaritalRelationship.relationshipStatus()
      .specifyValue(new CodeTableItem("BDMOASMaritalRelation", "MR1"));

    bdmOASMaritalRelationship.startDate().specifyValue(new Date());

    bdmoasLivingApartforReasonsBeyondControl.maritalRelationship()
      .specifyValue(bdmOASMaritalRelationship);

    bdmoasLivingApartforReasonsBeyondControl.startDate()
      .specifyValue(Date.fromISO8601("19980101"));

    bdmoasLivingApartforReasonsBeyondControl.reasonForLivingApart()
      .specifyValue(new CodeTableItem("BDMOASLivingApartReason", "LPR1"));

    final SummaryInformation summary =
      this.getSummary(bdmoasLivingApartforReasonsBeyondControl);

    assertTrue(summary.summary().getValue().toLocale(Locale.ENGLISH)
      .equals(summaryMessage));
  }

  /**
   * Test for Living Apart for Reasons Beyond Control start date must be
   * before end date validation.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void livingApartForReasonsBeyondControlStartDateAfterEndDateTest()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> maritalRelationship =
      new HashMap<String, String>();

    maritalRelationship.put(
      BDMOASMaritalRelationshipConstants.CASE_PARTICIPANT_ROLE_ID,
      String.valueOf(cprObj.caseParticipantRoleID));

    maritalRelationship.put(
      BDMOASMaritalRelationshipConstants.RELATIONSHIP_STATUS,
      BDMOASMARITALRELATION.OAS_MARITAL_RELATION_MARRIED);

    maritalRelationship.put(BDMOASMaritalRelationshipConstants.START_DATE,
      "19980101");

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
      dynamicEvidencedataDetails.getAttribute(END_DATE),
      Date.getCurrentDate().addDays(-10));

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
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        evidenceServiceInterface.createEvidence(genericDtls);
      });

    final String validationMessage =
      "The Start Date cannot be after End Date.";

    assertTrue(validationMessage.equals(exception.getMessage()));

  }

  /**
   * Test for Living Apart for Reasons Beyond Control description is required
   * if the reason is other.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void
    livingApartForReasonsBeyondControlDescriptionRequiredIfReasonIsOtherTest()
      throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> maritalRelationship =
      new HashMap<String, String>();

    maritalRelationship.put(
      BDMOASMaritalRelationshipConstants.CASE_PARTICIPANT_ROLE_ID,
      String.valueOf(cprObj.caseParticipantRoleID));

    maritalRelationship.put(
      BDMOASMaritalRelationshipConstants.RELATIONSHIP_STATUS,
      BDMOASMARITALRELATION.OAS_MARITAL_RELATION_MARRIED);

    maritalRelationship.put(BDMOASMaritalRelationshipConstants.START_DATE,
      "19980101");

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
      Date.getCurrentDate().addDays(-10));

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(END_DATE),
      Date.getCurrentDate().addDays(-5));

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(LIVING_APART_REASON),
      new CodeTableItem(BDMOASLIVINGAPARTREASON.TABLENAME,
        BDMOASLIVINGAPARTREASON.OAS_OTHER));

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
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        evidenceServiceInterface.createEvidence(genericDtls);
      });

    final String validationMessage =
      "Description must be entered if Living Apart for Reasons "
        + "Beyond Control Reason is selected as 'Other'.";

    assertTrue(validationMessage.equals(exception.getMessage()));

  }

  /**
   * Test for Living Apart for Reasons Beyond start and end date is outside
   * Martial Relationship evidence dates.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void
    livingApartForReasonsBeyondControlEvidenceDatesareWithInMaritalRelationshipDatesTest()
      throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> maritalRelationship =
      new HashMap<String, String>();

    maritalRelationship.put(
      BDMOASMaritalRelationshipConstants.CASE_PARTICIPANT_ROLE_ID,
      String.valueOf(cprObj.caseParticipantRoleID));

    maritalRelationship.put(
      BDMOASMaritalRelationshipConstants.RELATIONSHIP_STATUS,
      BDMOASMARITALRELATION.OAS_MARITAL_RELATION_MARRIED);

    maritalRelationship.put(BDMOASMaritalRelationshipConstants.START_DATE,
      "20000101");

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
      Date.fromISO8601("19990101"));

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(END_DATE),
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
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        evidenceServiceInterface.createEvidence(genericDtls);
      });

    final String validationMessage =
      "A Living Apart for Reasons Beyond Control record cannot have a "
        + "Start or End Date that falls outside the period of the related Marital Relationship.";

    assertTrue(validationMessage.equals(exception.getMessage()));

  }

  /**
   * This method will return the case member details
   *
   * @param caseID
   * @throws AppException
   * @throws InformationalException
   */
  public CaseParticipantRoleIDStruct getCaseMemberDetails(final long caseID,
    final PersonRegistrationResult member)
    throws AppException, InformationalException {

    final CaseParticipantRoleDetails caseParticipantRoleDetails =
      new CaseParticipantRoleDetails();
    caseParticipantRoleDetails.dtls.caseID = caseID;
    caseParticipantRoleDetails.dtls.typeCode = CASEPARTICIPANTROLETYPE.MEMBER;
    caseParticipantRoleDetails.dtls.participantRoleID =
      member.registrationIDDetails.concernRoleID;
    caseParticipantRoleDetails.dtls.fromDate = Date.getCurrentDate();
    CaseParticipantRoleFactory.newInstance()
      .insertCaseParticipantRole(caseParticipantRoleDetails);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct caseMemberCaseParticipantRoleObj =
      util.getCaseParticipantRoleID(caseID,
        member.registrationIDDetails.concernRoleID);

    return caseMemberCaseParticipantRoleObj;
  }

  /**
   * Registers a case member person for test purposes.
   *
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public PersonRegistrationResult registerCaseMember()
    throws AppException, InformationalException {

    final Person person = PersonFactory.newInstance();
    final PersonRegistrationDetails personDtls =
      new PersonRegistrationDetails();

    personDtls.personRegistrationDetails.firstForename = "Case";
    personDtls.personRegistrationDetails.surname = "Member";
    personDtls.personRegistrationDetails.dateOfBirth =
      Date.fromISO8601("19500101");
    personDtls.personRegistrationDetails.sex = GENDER.FEMALE;
    personDtls.personRegistrationDetails.addressData = this.getAddressData();
    personDtls.personRegistrationDetails.mailingAddressData =
      personDtls.personRegistrationDetails.addressData;
    personDtls.personRegistrationDetails.registrationDate =
      Date.getCurrentDate();
    personDtls.personRegistrationDetails.nationality = "CA";
    personDtls.personRegistrationDetails.birthCountry = "CA";
    personDtls.personRegistrationDetails.currentMaritalStatus = "Married";

    final PersonRegistrationResult result = person.register(personDtls);

    return result;

  }

  /**
   * PASS-IF an advise issue is not raised when reason is specified.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void advisorIssueForLivingApartReasonEvidence()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final ApplicationCaseKey applicationCase = this.createApplicationCase(
      person.registrationIDDetails.concernRoleID, 920000);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(applicationCase.applicationCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> maritalRelationship = new HashMap<>();

    maritalRelationship.put(
      BDMOASMaritalRelationshipConstants.CASE_PARTICIPANT_ROLE_ID,
      String.valueOf(cprObj.caseParticipantRoleID));

    maritalRelationship.put(
      BDMOASMaritalRelationshipConstants.RELATIONSHIP_STATUS,
      BDMOASMARITALRELATION.OAS_MARITAL_RELATION_MARRIED);

    maritalRelationship.put(BDMOASMaritalRelationshipConstants.START_DATE,
      _20080101);

    final EIEvidenceKey eiEvidenceKey =
      this.createEvidence(applicationCase.applicationCaseID,
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

    final DynamicEvidenceDataDetails dynamicEvidencedataDetails =
      DynamicEvidenceDataDetailsFactory
        .newInstance(evidenceTypeKey.evidenceType, Date.getCurrentDate());

    // get Latest Version of Evidence

    final EvidenceServiceInterface evidenceServiceInterface =
      EvidenceGenericSLFactory.instance(evidenceTypeKey,
        Date.getCurrentDate());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(START_DATE),
      Date.fromISO8601(_20080101));

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(LIVING_APART_REASON),
      new CodeTableItem(BDMOASLIVINGAPARTREASON.TABLENAME,
        BDMOASLIVINGAPARTREASONEntry.OAS_EMPLOYMENT.getCode()));

    final EvidenceDescriptorDetails descriptor =
      new EvidenceDescriptorDetails();
    descriptor.evidenceType = evidenceTypeKey.evidenceType;
    descriptor.caseID = applicationCase.applicationCaseID;
    descriptor.receivedDate = Date.getCurrentDate();
    descriptor.participantID = person.registrationIDDetails.concernRoleID;
    descriptor.changeReason =
      EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT;

    genericDtls.setData(dynamicEvidencedataDetails);
    genericDtls.setDescriptor(descriptor);
    genericDtls.setCaseIdKey(descriptor.caseID);

    final CpDetailsAdaptor cpAdaptor = new CpDetailsAdaptor();
    cpAdaptor.setCaseParticipantRoleID(cprObj.caseParticipantRoleID);
    genericDtls.addRelCp(
      BDMOASMaritalRelationshipConstants.CASE_PARTICIPANT_ROLE_ID, cpAdaptor);

    evidenceServiceInterface.createEvidence(genericDtls);

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

}
