package curam.ca.gc.bdmoas.evidence.maritalrelationship.impl;

import curam.advisor.facade.fact.AdvisorFactory;
import curam.advisor.facade.struct.AdviceKey;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdmoas.codetable.BDMOASMARITALCHANGETYPE;
import curam.ca.gc.bdmoas.codetable.BDMOASMARITALRELATION;
import curam.ca.gc.bdmoas.evidence.constants.impl.BDMOASMaritalRelationshipConstants;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.RECORDSTATUS;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.facade.struct.CreateIntegratedCaseResult;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceModifyDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.impl.EvidenceMap;
import curam.core.sl.infrastructure.impl.StandardEvidenceInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.core.struct.CaseKey;
import curam.creole.execution.session.InterpretedRuleObjectFactory;
import curam.creole.execution.session.ManualRecalculation;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.ruleclass.BDMOASMaritalRelationshipRuleSet.impl.BDMOASMaritalRelationship;
import curam.creole.ruleclass.BDMOASMaritalRelationshipRuleSet.impl.BDMOASMaritalRelationship_Factory;
import curam.creole.ruleclass.BDMOASMaritalRelationshipSummaryRuleSet.impl.SummaryInformation;
import curam.creole.ruleclass.BDMOASMaritalRelationshipSummaryRuleSet.impl.SummaryInformation_Factory;
import curam.creole.storage.inmemory.InMemoryDataStorage;
import curam.creole.value.CodeTableItem;
import curam.dynamicevidence.impl.DynamicEvidenceDataAttributeDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import curam.verification.sl.entity.fact.VerificationRequirementUsageFactory;
import curam.verification.sl.entity.intf.VerificationRequirementUsage;
import curam.verification.sl.entity.struct.VerificationRequirementUsageCancelDetails;
import curam.verification.sl.entity.struct.VerificationRequirementUsageKey;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.xml.bind.JAXBException;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 *
 * JUnit test class to test the Marital Relationship evidence
 * creation,summary and validations.
 *
 */
public class BDMOASMaritalRelationshipTest extends BDMOASCaseTest {

  /**
   * Set the session
   */
  @Override
  protected void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    this.session =
      Session_Factory.getFactory().newInstance(new ManualRecalculation(),
        new InMemoryDataStorage(new InterpretedRuleObjectFactory()));

    cancelMaritalInformationVerificationRequirementUsage();
  }

  private Session session;

  /**
   * Test for summary with Married
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void maritalrelationshipWithMarriedStatusSummary() {

    final String summaryMessage = "Married";

    final BDMOASMaritalRelationship oasMaritalRelationship =
      this.getEvidence();

    oasMaritalRelationship.relationshipStatus()
      .specifyValue(new CodeTableItem("BDMOASMaritalRelation", "MR1"));

    oasMaritalRelationship.startDate().specifyValue(new Date());
    oasMaritalRelationship.relatedCaseParticipantRoleID().specifyValue(0);

    final SummaryInformation summary =
      this.getSummary(oasMaritalRelationship);

    assertTrue(summary.summary().getValue().toLocale(Locale.ENGLISH)
      .equals(summaryMessage));
  }

  /**
   * PASS-IF A validation is thrown when end date is present and relationship
   * change type is not present.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void relationshipChangeMustBeEnterdIfEndDateIsPresent()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();

    attributes.put(
      BDMOASMaritalRelationshipConstants.CASE_PARTICIPANT_ROLE_ID,
      String.valueOf(cprObj.caseParticipantRoleID));

    attributes.put(BDMOASMaritalRelationshipConstants.RELATIONSHIP_STATUS,
      BDMOASMARITALRELATION.OAS_MARITAL_RELATION_MARRIED);

    attributes.put(BDMOASMaritalRelationshipConstants.START_DATE, "19980101");

    attributes.put(BDMOASMaritalRelationshipConstants.END_DATE, "20200101");

    Assert.assertThrows(InformationalException.class,
      () -> this.createEvidence(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID,
        CASEEVIDENCEEntry.OAS_MARITAL_RELATIONSHIP, attributes, getToday()));
  }

  /**
   * PASS-IF A validation is thrown when annulment is selected and relationship
   * status is not married.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void annulmentMustOnlyBeEnterdIfRelationshipStatusIsMarried()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();

    attributes.put(
      BDMOASMaritalRelationshipConstants.CASE_PARTICIPANT_ROLE_ID,
      String.valueOf(cprObj.caseParticipantRoleID));

    attributes.put(BDMOASMaritalRelationshipConstants.RELATIONSHIP_STATUS,
      BDMOASMARITALRELATION.OAS_MARITAL_RELATION_COMMON_LAW);

    attributes.put(
      BDMOASMaritalRelationshipConstants.RELATIONSHIP_CHANGE_TYPE,
      BDMOASMARITALCHANGETYPE.OAS_MARITAL_CHANGE_TYPE_ANNULMENT);

    attributes.put(BDMOASMaritalRelationshipConstants.START_DATE, "19980101");

    attributes.put(BDMOASMaritalRelationshipConstants.END_DATE, "20200101");

    Assert.assertThrows(InformationalException.class,
      () -> this.createEvidence(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID,
        CASEEVIDENCEEntry.OAS_MARITAL_RELATIONSHIP, attributes, getToday()));
  }

  /**
   * PASS-IF A validation is thrown when end may not be entered if marital
   * relationship
   * status is married and marital relation change type is Separated.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void
    endDateIsRequiredIfRelationshiStatusIsMarriedAndChangeTypeisSeparated()
      throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();

    attributes.put(
      BDMOASMaritalRelationshipConstants.CASE_PARTICIPANT_ROLE_ID,
      String.valueOf(cprObj.caseParticipantRoleID));

    attributes.put(BDMOASMaritalRelationshipConstants.RELATIONSHIP_STATUS,
      BDMOASMARITALRELATION.OAS_MARITAL_RELATION_MARRIED);

    attributes.put(
      BDMOASMaritalRelationshipConstants.RELATIONSHIP_CHANGE_TYPE,
      BDMOASMARITALCHANGETYPE.OAS_MARITAL_CHANGE_TYPE_SEPARATION);

    attributes.put(BDMOASMaritalRelationshipConstants.START_DATE, "19980101");

    attributes.put(BDMOASMaritalRelationshipConstants.END_DATE, "20200101");

    Assert.assertThrows(InformationalException.class,
      () -> this.createEvidence(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID,
        CASEEVIDENCEEntry.OAS_MARITAL_RELATIONSHIP, attributes, getToday()));
  }

  /**
   * PASS-IF A validation is thrown when start date is after end date
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void startDateIsAfterEndDate()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();

    attributes.put(
      BDMOASMaritalRelationshipConstants.CASE_PARTICIPANT_ROLE_ID,
      String.valueOf(cprObj.caseParticipantRoleID));

    attributes.put(BDMOASMaritalRelationshipConstants.RELATIONSHIP_STATUS,
      BDMOASMARITALRELATION.OAS_MARITAL_RELATION_MARRIED);

    attributes.put(
      BDMOASMaritalRelationshipConstants.RELATIONSHIP_CHANGE_TYPE,
      BDMOASMARITALCHANGETYPE.OAS_MARITAL_CHANGE_TYPE_DIVORCE);

    attributes.put(BDMOASMaritalRelationshipConstants.START_DATE, "20200101");

    attributes.put(BDMOASMaritalRelationshipConstants.END_DATE, "19980101");

    Assert.assertThrows(InformationalException.class,
      () -> this.createEvidence(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID,
        CASEEVIDENCEEntry.OAS_MARITAL_RELATIONSHIP, attributes, getToday()));
  }

  /**
   * PASS-IF A validation is thrown when start date is after end date
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void effectiveDateIsAfterEndDate()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();

    attributes.put(
      BDMOASMaritalRelationshipConstants.CASE_PARTICIPANT_ROLE_ID,
      String.valueOf(cprObj.caseParticipantRoleID));

    attributes.put(BDMOASMaritalRelationshipConstants.RELATIONSHIP_STATUS,
      BDMOASMARITALRELATION.OAS_MARITAL_RELATION_MARRIED);

    attributes.put(
      BDMOASMaritalRelationshipConstants.RELATIONSHIP_CHANGE_TYPE,
      BDMOASMARITALCHANGETYPE.OAS_MARITAL_CHANGE_TYPE_DIVORCE);

    attributes.put(BDMOASMaritalRelationshipConstants.START_DATE, "19980101");

    attributes.put(BDMOASMaritalRelationshipConstants.END_DATE, "20200101");

    final EIEvidenceKey eiEvidenceKey =
      this.createEvidence(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID,
        CASEEVIDENCEEntry.OAS_MARITAL_RELATIONSHIP, attributes, getToday());

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCase.integratedCaseID;
    evidenceController.applyAllChanges(caseKey);

    // Read DynamicEvidenceDataDetails
    final EvidenceMap map =
      curam.core.sl.infrastructure.impl.EvidenceController.getEvidenceMap();
    final StandardEvidenceInterface standardEvidenceInterface =
      map.getEvidenceType(CASEEVIDENCE.OAS_MARITAL_RELATIONSHIP);

    final EIEvidenceKey evidenceKey = new EIEvidenceKey();
    evidenceKey.evidenceID = eiEvidenceKey.evidenceID;
    evidenceKey.evidenceType = CASEEVIDENCE.OAS_MARITAL_RELATIONSHIP;

    final DynamicEvidenceDataDetails evidenceData =
      (DynamicEvidenceDataDetails) standardEvidenceInterface
        .readEvidence(evidenceKey);

    // update the Relationship Type
    final DynamicEvidenceDataAttributeDetails attributeObj = evidenceData
      .getAttribute(BDMOASMaritalRelationshipConstants.RELATIONSHIP_STATUS);

    DynamicEvidenceTypeConverter.setAttribute(attributeObj,
      new CodeTableItem(BDMOASMARITALRELATION.TABLENAME,
        BDMOASMARITALRELATION.OAS_MARITAL_RELATION_MARRIED));

    // evidence descriptor details for this evidence
    final RelatedIDAndEvidenceTypeKey relatedIDAndTypeKey =
      new RelatedIDAndEvidenceTypeKey();
    relatedIDAndTypeKey.relatedID = eiEvidenceKey.evidenceID;
    relatedIDAndTypeKey.evidenceType = CASEEVIDENCE.OAS_MARITAL_RELATIONSHIP;

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
    modifyEvidenceDetails.descriptor.effectiveFrom = getToday();

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    evidenceControllerObj.modifyEvidence(evidenceKey, modifyEvidenceDetails);

    Assert.assertThrows(InformationalException.class,
      () -> this.createEvidence(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID,
        CASEEVIDENCEEntry.OAS_MARITAL_RELATIONSHIP, attributes, getToday()));
  }

  /**
   * PASS-IF Evidence Issue is created.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void isEvidenceIssueCreatedForRelationshipChangeTypeDeath()
    throws AppException, InformationalException, JAXBException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();

    attributes.put(
      BDMOASMaritalRelationshipConstants.CASE_PARTICIPANT_ROLE_ID,
      String.valueOf(cprObj.caseParticipantRoleID));

    attributes.put(BDMOASMaritalRelationshipConstants.RELATIONSHIP_STATUS,
      BDMOASMARITALRELATION.OAS_MARITAL_RELATION_MARRIED);

    attributes.put(
      BDMOASMaritalRelationshipConstants.RELATIONSHIP_CHANGE_TYPE,
      BDMOASMARITALCHANGETYPE.OAS_MARITAL_CHANGE_TYPE_DEATH);

    attributes.put(BDMOASMaritalRelationshipConstants.START_DATE, "19980101");

    attributes.put(BDMOASMaritalRelationshipConstants.END_DATE, "20200101");

    this.createEvidence(integratedCase.integratedCaseID,
      person.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.OAS_MARITAL_RELATIONSHIP, attributes, getToday());

    final AdviceKey adviceKey = new AdviceKey();

    adviceKey.adviceContextKeyName = "BDMOASIntegratedCase_home";
    adviceKey.parameters = "caseID=" + integratedCase.integratedCaseID;

    final curam.advisor.facade.struct.AdviceDetails adviceDetails =
      AdvisorFactory.newInstance().getAdvice(adviceKey);

    final String issueText =
      "A Marital Relationship evidence with a Relationship "
        + "Change Type of 'Death' exists, but neither participant has a "
        + "date of death on Birth and Death Details evidence.";

    if (adviceDetails.adviceXML.contains(issueText)) {
      assertTrue(true);
    }
  }

  /**
   * Return the evidence for the session
   *
   * @return
   */
  private BDMOASMaritalRelationship getEvidence() {

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
    getSummary(final BDMOASMaritalRelationship evidence) {

    final SummaryInformation summary =
      SummaryInformation_Factory.getFactory().newInstance(this.session);
    summary.evidence().specifyValue(evidence);

    return summary;
  }

  private void cancelMaritalInformationVerificationRequirementUsage() {

    final VerificationRequirementUsage verReqUsageObj =
      VerificationRequirementUsageFactory.newInstance();

    final VerificationRequirementUsageKey key =
      new VerificationRequirementUsageKey();
    final VerificationRequirementUsageCancelDetails details =
      new VerificationRequirementUsageCancelDetails();

    try {
      // OAS GIS AC
      key.verificationRequirementUsageID = 920007L;
      details.recordStatus = RECORDSTATUS.NORMAL;
      details.verificationRequirementUsageID = 920007L;
      details.versionNo = 1;
      verReqUsageObj.cancel(key, details);

      // OAS IC
      key.verificationRequirementUsageID = 920009L;
      details.recordStatus = RECORDSTATUS.NORMAL;
      details.verificationRequirementUsageID = 920009L;
      details.versionNo = 1;
      verReqUsageObj.cancel(key, details);
    } catch (final Exception e) {
      // Do Nothing
    }
  }

}
