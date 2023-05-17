package curam.ca.gc.bdmoas.evidence.sponsorshipagreement.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMYESNO;
import curam.ca.gc.bdmoas.codetable.BDMOASBREAKDOWNREASON;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.facade.struct.CreateIntegratedCaseResult;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.core.sl.struct.EvidenceDescriptorDetails;
import curam.core.sl.struct.EvidenceKey;
import curam.core.sl.struct.EvidenceTypeKey;
import curam.creole.execution.session.InterpretedRuleObjectFactory;
import curam.creole.execution.session.ManualRecalculation;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.ruleclass.BDMOASSponsorshipAgreementRuleSet.impl.BDMOASSponsorshipAgreement;
import curam.creole.ruleclass.BDMOASSponsorshipAgreementRuleSet.impl.BDMOASSponsorshipAgreement_Factory;
import curam.creole.ruleclass.BDMOASSponsorshipAgreementSummaryRuleSet.impl.BDMOASSponsorshipAgreementSummary;
import curam.creole.ruleclass.BDMOASSponsorshipAgreementSummaryRuleSet.impl.BDMOASSponsorshipAgreementSummary_Factory;
import curam.creole.ruleclass.BDMOASSponsorshipRuleSet.impl.BDMOASSponsorship;
import curam.creole.ruleclass.BDMOASSponsorshipRuleSet.impl.BDMOASSponsorship_Factory;
import curam.creole.storage.inmemory.InMemoryDataStorage;
import curam.creole.value.CodeTableItem;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.dynamicevidence.sl.impl.EvidenceGenericSLFactory;
import curam.dynamicevidence.sl.impl.EvidenceServiceInterface;
import curam.dynamicevidence.sl.struct.impl.GenericSLDataDetails;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class BDMOASSponsorshipAgreementTest extends BDMOASCaseTest {

  private Session session;

  @Override
  protected void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    this.session =
      Session_Factory.getFactory().newInstance(new ManualRecalculation(),
        new InMemoryDataStorage(new InterpretedRuleObjectFactory()));

  }

  public static final String CASEPARTICIPANTROLE = "caseParticipantRole";

  public static final String ISSPONSORED = "isSponsored";

  public static final String START_DATE = "startDate";

  public static final String END_DATE = "endDate";

  public static final String SPONSORSHIP = "sponsorship";

  public static final String BREAKDOWN_REASON = "breakdownReason";

  public static final String BREAKDOWN_DATE = "breakdownDate";

  /**
   * Test for summary with Brokedown date for BDMOASSponsorshipAgreement
   * evidence.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void bdmSponsorshipAgreementWithBrokeDownDateEvidenceYesSummary() {

    final LocalDate localDate = LocalDate.now();
    final DateTimeFormatter dateFormatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd");
    final String date = localDate.format(dateFormatter);

    final String summaryMessage = "Sponsored, broke down " + date;

    final curam.creole.ruleclass.BDMOASSponsorshipAgreementRuleSet.impl.BDMOASSponsorshipAgreement bdmOASSponsorshipAgreement =
      this.getEvidence();

    final curam.creole.ruleclass.BDMOASSponsorshipRuleSet.impl.BDMOASSponsorship bdmOASSponsorship =
      getSponsorshipEvidence();

    bdmOASSponsorship.isSponsored()
      .specifyValue(new CodeTableItem("BDMYesNo", "BOASYN001"));

    bdmOASSponsorshipAgreement.sponsorship().specifyValue(bdmOASSponsorship);

    bdmOASSponsorshipAgreement.breakdownDate().specifyValue(getToday());

    final BDMOASSponsorshipAgreementSummary summary =
      this.getSummary(bdmOASSponsorshipAgreement);

    assertTrue(summary.summary().getValue().toLocale(Locale.ENGLISH)
      .equals(summaryMessage));
  }

  /**
   * Test for summary with Brokedown date for BDMOASSponsorshipAgreement
   * evidence.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void bdmSponsorshipAgreementWithNoBrokeDownDateEvidenceYesSummary() {

    final String summaryMessage = "Sponsored";

    final curam.creole.ruleclass.BDMOASSponsorshipAgreementRuleSet.impl.BDMOASSponsorshipAgreement bdmOASSponsorshipAgreement =
      this.getEvidence();

    final curam.creole.ruleclass.BDMOASSponsorshipRuleSet.impl.BDMOASSponsorship bdmOASSponsorship =
      getSponsorshipEvidence();

    bdmOASSponsorship.isSponsored()
      .specifyValue(new CodeTableItem("BDMYesNo", "BOASYN001"));
    bdmOASSponsorshipAgreement.breakdownDate().specifyValue(null);

    bdmOASSponsorshipAgreement.sponsorship().specifyValue(bdmOASSponsorship);

    final BDMOASSponsorshipAgreementSummary summary =
      this.getSummary(bdmOASSponsorshipAgreement);

    assertTrue(summary.summary().getValue().toLocale(Locale.ENGLISH)
      .equals(summaryMessage));
  }

  /**
   * Test start and end date validation for the Sponsorship Agreement evidence.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void checkStartandEndDateValidationForSponsorshipAgeement()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> sponsorshipAttributes =
      new HashMap<String, String>();
    sponsorshipAttributes.put(CASEPARTICIPANTROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    sponsorshipAttributes.put(ISSPONSORED, BDMYESNO.YES);

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
      Date.getCurrentDate().addDays(10));

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

    // create evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        evidenceServiceInterface.createEvidence(genericDtls);
      });

    final String validationMessage =
      "The Start Date must be before the Current Date.";

    assertTrue(validationMessage.equals(exception.getMessage()));

  }

  /**
   * Test start and end date validation for the Sponsorship Agreement evidence.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void duplicateEvidenceValidationForSponsorshipAgeement()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> sponsorshipAttributes =
      new HashMap<String, String>();
    sponsorshipAttributes.put(CASEPARTICIPANTROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    sponsorshipAttributes.put(ISSPONSORED, BDMYESNO.YES);

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

    // create evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        evidenceServiceInterface.createEvidence(genericDtls);
      });

    final String validationMessage =
      "A Sponsorship Agreement record already exists for this Participant for an overlapping period.";

    assertTrue(validationMessage.equals(exception.getMessage()));

  }

  /**
   * Test Breakdown date is required when a Breakdown Reason is selected
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void
    breakdownDateAndBreakdownReasonValidationForSponsorshipAgeement()
      throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> sponsorshipAttributes =
      new HashMap<String, String>();
    sponsorshipAttributes.put(CASEPARTICIPANTROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    sponsorshipAttributes.put(ISSPONSORED, BDMYESNO.YES);

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

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(BREAKDOWN_REASON),
      new CodeTableItem(BDMOASBREAKDOWNREASON.TABLENAME,
        BDMOASBREAKDOWNREASON.OAS_SPONSOR_DIED));

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
      "Breakdown Date is required when a Breakdown Reason is selected.";

    assertTrue(validationMessage.equals(exception.getMessage()));

  }

  /**
   * Test Breakdown date is required when a Breakdown Reason is selected
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void breakdownDateValidationForSponsorshipAgeement()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> sponsorshipAttributes =
      new HashMap<String, String>();
    sponsorshipAttributes.put(CASEPARTICIPANTROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    sponsorshipAttributes.put(ISSPONSORED, BDMYESNO.YES);

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

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(BREAKDOWN_REASON),
      new CodeTableItem(BDMOASBREAKDOWNREASON.TABLENAME,
        BDMOASBREAKDOWNREASON.OAS_SPONSOR_DIED));

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(BREAKDOWN_DATE),
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

    // create evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        evidenceServiceInterface.createEvidence(genericDtls);
      });

    final String validationMessage =
      "Breakdown Date must be after the Start and before the End Date.";

    assertTrue(validationMessage.equals(exception.getMessage()));

  }

  /**
   * Test Breakdown Reason is required when a Breakdown Date is selected
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void
    breakdownReasonAndBreakdownDateValidationForSponsorshipAgeement()
      throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> sponsorshipAttributes =
      new HashMap<String, String>();
    sponsorshipAttributes.put(CASEPARTICIPANTROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    sponsorshipAttributes.put(ISSPONSORED, BDMYESNO.YES);

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

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(BREAKDOWN_DATE),
      Date.getCurrentDate().addDays(-1));

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
      "Breakdown Reason is required when Breakdown Date is entered.";

    assertTrue(validationMessage.equals(exception.getMessage()));

  }

  /**
   * Test that this evidence cannot be created if the parent shows no for
   * entered under an agreement.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void checkEnteredUnderAgreementValidationForSponsorshipAgeement()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> sponsorshipAttributes =
      new HashMap<String, String>();
    sponsorshipAttributes.put(CASEPARTICIPANTROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    sponsorshipAttributes.put(ISSPONSORED, BDMYESNO.NO);

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
      Date.getCurrentDate().addDays(-1));

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(END_DATE),
      Date.getCurrentDate().addDays(10));

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
      "A Sponsorship Agreement record cannot exist for a client who did not enter Canada under a sponsorship agreement.";

    assertTrue(validationMessage.equals(exception.getMessage()));

  }

  /**
   * Instantiates and returns a summary rule object for the given evidence.
   *
   * @param evidence
   * @return
   */
  private BDMOASSponsorshipAgreementSummary

    getSummary(final BDMOASSponsorshipAgreement evidence) {

    final BDMOASSponsorshipAgreementSummary summary =
      BDMOASSponsorshipAgreementSummary_Factory.getFactory()
        .newInstance(this.session);
    summary.evidence().specifyValue(evidence);

    return summary;

  }

  /**
   * Return the evidence for the session
   *
   * @return
   */
  private BDMOASSponsorshipAgreement getEvidence() {

    return BDMOASSponsorshipAgreement_Factory.getFactory()
      .newInstance(this.session);

  }

  /**
   * Return the evidence for the session
   *
   * @return
   */
  private BDMOASSponsorship getSponsorshipEvidence() {

    return BDMOASSponsorship_Factory.getFactory().newInstance(this.session);

  }

}
