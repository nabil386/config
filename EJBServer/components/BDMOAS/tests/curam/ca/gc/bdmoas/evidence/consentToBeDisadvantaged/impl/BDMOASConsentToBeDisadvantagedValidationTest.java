package curam.ca.gc.bdmoas.evidence.consentToBeDisadvantaged.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdmoas.codetable.BDMOASCONSENTDISSTATUS;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.codetable.CASEPARTICIPANTROLETYPE;
import curam.codetable.GENDER;
import curam.core.facade.fact.PersonFactory;
import curam.core.facade.intf.Person;
import curam.core.facade.struct.CreateIntegratedCaseResult;
import curam.core.facade.struct.PersonRegistrationDetails;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.fact.ConcernRoleFactory;
import curam.core.intf.ConcernRole;
import curam.core.sl.fact.CaseParticipantRoleFactory;
import curam.core.sl.struct.CaseParticipantRoleDetails;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.core.struct.ConcernRoleKey;
import curam.creole.execution.session.InterpretedRuleObjectFactory;
import curam.creole.execution.session.ManualRecalculation;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.ruleclass.BDMOASConsentToBeDisadvantagedRuleSet.impl.BDMOASConsentToBeDisadvantaged;
import curam.creole.ruleclass.BDMOASConsentToBeDisadvantagedRuleSet.impl.BDMOASConsentToBeDisadvantaged_Factory;
import curam.creole.ruleclass.BDMOASConsentToBeDisadvantagedValidationRuleSet.impl.ValidationResult;
import curam.creole.ruleclass.BDMOASConsentToBeDisadvantagedValidationRuleSet.impl.ValidationResult_Factory;
import curam.creole.ruleclass.BDMOASLivingApartforReasonsBeyondControlRuleSet.impl.BDMOASLivingApartforReasonsBeyondControl;
import curam.creole.ruleclass.BDMOASLivingApartforReasonsBeyondControlRuleSet.impl.BDMOASLivingApartforReasonsBeyondControl_Factory;
import curam.creole.ruleclass.BDMOASMaritalRelationshipRuleSet.impl.BDMOASMaritalRelationship;
import curam.creole.ruleclass.BDMOASMaritalRelationshipRuleSet.impl.BDMOASMaritalRelationship_Factory;
import curam.creole.ruleclass.DynamicEvidenceRuleSet.impl.CaseParticipantRole_Factory;
import curam.creole.ruleclass.ParticipantEntitiesRuleSet.impl.ConcernRole_Factory;
import curam.creole.storage.inmemory.InMemoryDataStorage;
import curam.creole.value.CodeTableItem;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import java.util.Locale;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for dynamic evidence BDMOASConsentToBeDisadvantaged
 */
public class BDMOASConsentToBeDisadvantagedValidationTest
  extends BDMOASCaseTest {

  private Session session;

  public static final String CONSENT_STATUS = "Consent Received";

  @Override
  protected void setUpCuramServerTest() {

    this.session =
      Session_Factory.getFactory().newInstance(new ManualRecalculation(),
        new InMemoryDataStorage(new InterpretedRuleObjectFactory()));

  }

  /**
   * Return the evidence for the session
   *
   * @return
   */
  private BDMOASConsentToBeDisadvantaged getEvidence() {

    final BDMOASConsentToBeDisadvantaged evidence =
      BDMOASConsentToBeDisadvantaged_Factory.getFactory()
        .newInstance(this.session);

    evidence.consentStatus().specifyValue(new CodeTableItem(
      BDMOASCONSENTDISSTATUS.TABLENAME, BDMOASCONSENTDISSTATUS.RECEIVED));

    return evidence;

  }

  /**
   * Return the Living Apart evidence for the session
   *
   * @return
   */
  private BDMOASLivingApartforReasonsBeyondControl getLivingApartEvidence() {

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
  private ValidationResult
    getValidation(final BDMOASConsentToBeDisadvantaged evidence) {

    final ValidationResult validation =
      ValidationResult_Factory.getFactory().newInstance(this.session);
    validation.evidence().specifyValue(evidence);

    return validation;
  }

  /***
   * Validation for The Start Date has to be on or later than the Living Apart
   * for Reasons Beyond Control Start
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void consentStartDateBeforeLivingApartStartDate()
    throws AppException, InformationalException {

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

    final BDMOASConsentToBeDisadvantaged bdmoasConsentToBeDisadvantaged =
      this.getEvidence();

    final BDMOASLivingApartforReasonsBeyondControl bdmoasLivingApartforReasonsBeyondControl =
      this.getLivingApartEvidence();

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
      .specifyValue(Date.getCurrentDate());
    bdmoasLivingApartforReasonsBeyondControl.endDate().specifyValue(null);

    bdmoasLivingApartforReasonsBeyondControl.reasonForLivingApart()
      .specifyValue(new CodeTableItem("BDMOASLivingApartReason", "LPR1"));

    bdmoasConsentToBeDisadvantaged.livingApartforReasonsBeyondControl()
      .specifyValue(bdmoasLivingApartforReasonsBeyondControl);
    bdmoasConsentToBeDisadvantaged.caseParticipantRole()
      .specifyValue(primaryCprObj.caseParticipantRoleID);
    bdmoasConsentToBeDisadvantaged.consentStatus()
      .specifyValue(new CodeTableItem("BDMOASConsentDisStatus", "CDS1"));
    bdmoasConsentToBeDisadvantaged.startDate()
      .specifyValue(Date.getCurrentDate().addDays(-1));

    final ValidationResult validation =
      this.getValidation(bdmoasConsentToBeDisadvantaged);

    final ConcernRole concern = ConcernRoleFactory.newInstance();
    final ConcernRoleKey key = new ConcernRoleKey();
    key.concernRoleID = relatedMember.registrationIDDetails.concernRoleID;

    final String validationMessage =
      "The Start Date has to be on or later than the Living Apart for Reasons Beyond Control Start Date.";
    ;

    assertTrue(validation.consentStartDateAfterLivingApartStartDateFailure()
      .getValue());
    assertEquals(
      validation.consentStartDateAfterLivingApartStartDateFailureMessage()
        .getValue().toLocale(Locale.ENGLISH),
      validationMessage);
  }

  /***
   * Validation for The Start Date cannot be later than the Living Apart for
   * Reasons Beyond Control End Date
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void consentStartDateAfterLivingApartEndDate()
    throws AppException, InformationalException {

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

    final BDMOASConsentToBeDisadvantaged bdmoasConsentToBeDisadvantaged =
      this.getEvidence();

    final BDMOASLivingApartforReasonsBeyondControl bdmoasLivingApartforReasonsBeyondControl =
      this.getLivingApartEvidence();

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
      .specifyValue(Date.getCurrentDate());
    bdmoasLivingApartforReasonsBeyondControl.endDate()
      .specifyValue(Date.getCurrentDate().addDays(1));

    bdmoasLivingApartforReasonsBeyondControl.reasonForLivingApart()
      .specifyValue(new CodeTableItem("BDMOASLivingApartReason", "LPR1"));

    bdmoasConsentToBeDisadvantaged.livingApartforReasonsBeyondControl()
      .specifyValue(bdmoasLivingApartforReasonsBeyondControl);
    bdmoasConsentToBeDisadvantaged.caseParticipantRole()
      .specifyValue(primaryCprObj.caseParticipantRoleID);
    bdmoasConsentToBeDisadvantaged.consentStatus()
      .specifyValue(new CodeTableItem("BDMOASConsentDisStatus", "CDS1"));
    bdmoasConsentToBeDisadvantaged.startDate()
      .specifyValue(Date.getCurrentDate().addDays(2));

    final ValidationResult validation =
      this.getValidation(bdmoasConsentToBeDisadvantaged);

    final ConcernRole concern = ConcernRoleFactory.newInstance();
    final ConcernRoleKey key = new ConcernRoleKey();
    key.concernRoleID = relatedMember.registrationIDDetails.concernRoleID;

    final String validationMessage =
      "The Start Date cannot be later than the Living Apart for Reasons Beyond Control End Date.";

    assertTrue(validation.consentStartDateBeforeLivingApartEndDateFailure()
      .getValue());
    assertEquals(
      validation.consentStartDateBeforeLivingApartEndDateFailureMessage()
        .getValue().toLocale(Locale.ENGLISH),
      validationMessage);
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

}
