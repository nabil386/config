package curam.ca.gc.bdmoas.benefitSuspension.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdmoas.codetable.BDMOASSUSPENDEDBENEFIT;
import curam.ca.gc.bdmoas.codetable.BDMOASSUSPENSIONREASON;
import curam.ca.gc.bdmoas.codetable.BDMOASSUSPENSIONSTATUS;
import curam.ca.gc.bdmoas.codetable.BDMOASUNSUSPENSIONREASON;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.codetable.impl.EVIDENCECHANGEREASONEntry;
import curam.core.facade.struct.CreateIntegratedCaseResult;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKeyList;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceModifyDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.impl.EvidenceMap;
import curam.core.sl.infrastructure.impl.StandardEvidenceInterface;
import curam.core.sl.infrastructure.struct.ECActiveEvidenceDtls;
import curam.core.sl.infrastructure.struct.ECActiveEvidenceDtlsList;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.core.struct.CaseKey;
import curam.creole.value.CodeTableItem;
import curam.dynamicevidence.impl.DynamicEvidenceDataAttributeDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

// 90159 DEV: Implement Benefit Suspension Evidence
/**
 * Test configured rules-based validations for Benefit Suspension evidence.
 */
public class BDMOASBenefitSuspensionTest extends BDMOASCaseTest {

  public static final String CASE_PARTICIPANT_ROLE = "caseParticipantRole";

  public static final String SUSPENDED_BENEFIT = "suspendedBenefit";

  public static final String SUSPENSION_STATUS = "suspensionStatus";

  public static final String SUSPENSION_REASON = "suspensionReason";

  public static final String SUSPENSION_REASON_DESCRIPTION =
    "suspensionRsnDesc";

  public static final String UNSUSPENSION_REASON = "unsuspensionReason";

  public static final String UNSUSPENSION_REASON_DESCRIPTION =
    "unsuspensionRsnDesc";

  public static final String START_DATE = "startDate";

  public static final String END_DATE = "endDate";

  /**
   * FAIL-IF Initial Suspension Status is not 'Suspended'
   */
  @Test
  public void initialSuspensionStatusFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final String validationMessage =
      "The initial value of Suspension Status must be 'Suspended'.";

    // Evidence Details
    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(SUSPENDED_BENEFIT,
      BDMOASSUSPENDEDBENEFIT.OLD_AGE_SECURITY_PENSION);
    attributes.put(SUSPENSION_STATUS, BDMOASSUSPENSIONSTATUS.UNSUSPENDED);
    attributes.put(SUSPENSION_REASON, BDMOASSUSPENSIONREASON.ISB_REQUEST);
    attributes.put(UNSUSPENSION_REASON,
      BDMOASUNSUSPENSIONREASON.SUSPENDED_IN_ERROR);
    attributes.put(CASE_PARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(START_DATE, "20220501");

    // create evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_BENEFIT_SUSPENSION, attributes, getToday());
      });

    assertTrue(validationMessage.equals(exception.getMessage()));
  }

  /**
   * FAIL-IF Start Date is after Current Date
   */
  @Test
  public void startDateOnOrBeforeCurrentDateFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final String validationMessage =
      "'Start Date' must be on or before the current date.";

    // Evidence Details
    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(SUSPENDED_BENEFIT,
      BDMOASSUSPENDEDBENEFIT.OLD_AGE_SECURITY_PENSION);
    attributes.put(SUSPENSION_STATUS, BDMOASSUSPENSIONSTATUS.SUSPENDED);
    attributes.put(SUSPENSION_REASON, BDMOASSUSPENSIONREASON.ISB_REQUEST);
    attributes.put(CASE_PARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(START_DATE,
      getDateString(Date.getCurrentDate().addDays(10)));

    // create evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_BENEFIT_SUSPENSION, attributes, getToday());
      });

    assertTrue(validationMessage.equals(exception.getMessage()));
  }

  /**
   * FAIL-IF Start Date after End Date
   */
  @Test
  public void endDateAfterStartDateFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final String validationMessage = "'End Date' must be after 'Start Date'.";

    // Evidence Details
    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(SUSPENDED_BENEFIT,
      BDMOASSUSPENDEDBENEFIT.OLD_AGE_SECURITY_PENSION);
    attributes.put(SUSPENSION_STATUS, BDMOASSUSPENSIONSTATUS.SUSPENDED);
    attributes.put(SUSPENSION_REASON, BDMOASSUSPENSIONREASON.ISB_REQUEST);
    attributes.put(CASE_PARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(START_DATE,
      getDateString(Date.getCurrentDate().addDays(-10)));
    attributes.put(END_DATE,
      getDateString(Date.getCurrentDate().addDays(-11)));

    // create evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_BENEFIT_SUSPENSION, attributes, getToday());
      });

    assertTrue(validationMessage.equals(exception.getMessage()));
  }

  /**
   * FAIL-IF Unsuspension Reason is not entered when suspension status is
   * Unsuspended.
   */
  @Test
  public void UnsuspensionReasonRequiredFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final String validationMessage =
      "Unsuspension Reason must be entered if Suspension Status is 'Unsuspended'.";

    // Evidence Details
    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(SUSPENDED_BENEFIT,
      BDMOASSUSPENDEDBENEFIT.OLD_AGE_SECURITY_PENSION);
    attributes.put(SUSPENSION_STATUS, BDMOASSUSPENSIONSTATUS.SUSPENDED);
    attributes.put(SUSPENSION_REASON, BDMOASSUSPENSIONREASON.ISB_REQUEST);
    attributes.put(CASE_PARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(START_DATE,
      getDateString(Date.getCurrentDate().addDays(-50)));

    // create evidence record
    this.createEvidence(integratedCase.integratedCaseID,
      person.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.OAS_BENEFIT_SUSPENSION, attributes, getToday());

    // Apply Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCase.integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    evidenceController.applyAllChanges(caseKey);

    // modify evidence record
    final HashMap<String, Object> benefitSuspensionAttributes =
      new HashMap<String, Object>();
    benefitSuspensionAttributes.put(SUSPENSION_STATUS, new CodeTableItem(
      BDMOASSUSPENSIONSTATUS.TABLENAME, BDMOASSUSPENSIONSTATUS.UNSUSPENDED));

    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        modifyBenefitSuspensionEvidence(integratedCase.integratedCaseID,
          benefitSuspensionAttributes);
      });

    assertTrue(validationMessage.equals(exception.getMessage()));
  }

  /**
   * FAIL-IF Suspended Benefits is not "All Benefits" when Suspension Reason is
   * "Presumed or Potentially Deceased"
   */
  @Test
  public void suspendedBenefitsFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final String validationMessage =
      "Suspended Benefit(s) must be 'All Benefits' if Suspension Reason is 'Presumed or Potentially Deceased' or 'Cannot Locate'.";

    // Evidence Details
    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(SUSPENDED_BENEFIT,
      BDMOASSUSPENDEDBENEFIT.OLD_AGE_SECURITY_PENSION);
    attributes.put(SUSPENSION_STATUS, BDMOASSUSPENSIONSTATUS.SUSPENDED);
    attributes.put(SUSPENSION_REASON,
      BDMOASSUSPENSIONREASON.PRESUMED_OR_POTENTIALLY_DECEASED);
    attributes.put(CASE_PARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(START_DATE,
      getDateString(Date.getCurrentDate().addDays(-10)));

    // create evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_BENEFIT_SUSPENSION, attributes, getToday());
      });

    assertTrue(validationMessage.equals(exception.getMessage()));
  }

  /**
   * FAIL-IF Suspension reason description is not entered when Suspension Reason
   * is 'Other'.
   */
  @Test
  public void suspensionReasonDescriptionFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final String validationMessage =
      "Suspension Reason Description must be entered if Suspension Reason is 'Other'.";

    // Evidence Details
    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(SUSPENDED_BENEFIT,
      BDMOASSUSPENDEDBENEFIT.OLD_AGE_SECURITY_PENSION);
    attributes.put(SUSPENSION_STATUS, BDMOASSUSPENSIONSTATUS.SUSPENDED);
    attributes.put(SUSPENSION_REASON, BDMOASSUSPENSIONREASON.OTHER);
    attributes.put(CASE_PARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(START_DATE,
      getDateString(Date.getCurrentDate().addDays(-10)));

    // create evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_BENEFIT_SUSPENSION, attributes, getToday());
      });

    assertTrue(validationMessage.equals(exception.getMessage()));
  }

  /**
   * FAIL-IF Suspension reason is not 'Other' when Suspension Reason Description
   * is entered.
   */
  @Test
  public void suspensionReasonFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final String validationMessage =
      "Suspension Reason must be 'Other' if Suspension Reason Description is entered.";

    // Evidence Details
    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(SUSPENDED_BENEFIT,
      BDMOASSUSPENDEDBENEFIT.OLD_AGE_SECURITY_PENSION);
    attributes.put(SUSPENSION_STATUS, BDMOASSUSPENSIONSTATUS.SUSPENDED);
    attributes.put(SUSPENSION_REASON, BDMOASSUSPENSIONREASON.ISB_REQUEST);
    attributes.put(SUSPENSION_REASON_DESCRIPTION,
      "Other reason details for Suspension.");
    attributes.put(CASE_PARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(START_DATE,
      getDateString(Date.getCurrentDate().addDays(-10)));

    // create evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_BENEFIT_SUSPENSION, attributes, getToday());
      });

    assertTrue(validationMessage.equals(exception.getMessage()));
  }

  /**
   * FAIL-IF Unsuspension Reason Description is not entered when Unsuspension
   * Reason is entered.
   */
  @Test
  public void UnsuspensionReasonDescriptionFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final String validationMessage =
      "Unsuspension Reason Description must be entered if Unsuspension Reason is 'Other'.";

    // Evidence Details
    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(SUSPENDED_BENEFIT,
      BDMOASSUSPENDEDBENEFIT.OLD_AGE_SECURITY_PENSION);
    attributes.put(SUSPENSION_STATUS, BDMOASSUSPENSIONSTATUS.SUSPENDED);
    attributes.put(SUSPENSION_REASON, BDMOASSUSPENSIONREASON.ISB_REQUEST);
    attributes.put(CASE_PARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(START_DATE,
      getDateString(Date.getCurrentDate().addDays(-50)));

    // create evidence record
    this.createEvidence(integratedCase.integratedCaseID,
      person.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.OAS_BENEFIT_SUSPENSION, attributes, getToday());

    // Apply Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCase.integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    evidenceController.applyAllChanges(caseKey);

    // modify evidence record
    final HashMap<String, Object> benefitSuspensionAttributes =
      new HashMap<String, Object>();
    benefitSuspensionAttributes.put(SUSPENSION_STATUS, new CodeTableItem(
      BDMOASSUSPENSIONSTATUS.TABLENAME, BDMOASSUSPENSIONSTATUS.UNSUSPENDED));
    benefitSuspensionAttributes.put(UNSUSPENSION_REASON, new CodeTableItem(
      BDMOASUNSUSPENSIONREASON.TABLENAME, BDMOASUNSUSPENSIONREASON.OTHER));

    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        modifyBenefitSuspensionEvidence(integratedCase.integratedCaseID,
          benefitSuspensionAttributes);
      });

    assertTrue(validationMessage.equals(exception.getMessage()));
  }

  /**
   * FAIL-IF Unsuspension Reason is not 'Other' Unsuspension Reason Description
   * is entered.
   */
  @Test
  public void UnsuspensionReasonOtherFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final String validationMessage =
      "Unsuspension Reason must be 'Other' if Unsuspension Reason Description is entered.";

    // Evidence Details
    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(SUSPENDED_BENEFIT,
      BDMOASSUSPENDEDBENEFIT.OLD_AGE_SECURITY_PENSION);
    attributes.put(SUSPENSION_STATUS, BDMOASSUSPENSIONSTATUS.SUSPENDED);
    attributes.put(SUSPENSION_REASON, BDMOASSUSPENSIONREASON.ISB_REQUEST);
    attributes.put(CASE_PARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(START_DATE,
      getDateString(Date.getCurrentDate().addDays(-50)));

    // create evidence record
    this.createEvidence(integratedCase.integratedCaseID,
      person.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.OAS_BENEFIT_SUSPENSION, attributes, getToday());

    // Apply Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCase.integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    evidenceController.applyAllChanges(caseKey);

    // modify evidence record
    final HashMap<String, Object> benefitSuspensionAttributes =
      new HashMap<String, Object>();
    benefitSuspensionAttributes.put(SUSPENSION_STATUS, new CodeTableItem(
      BDMOASSUSPENSIONSTATUS.TABLENAME, BDMOASSUSPENSIONSTATUS.UNSUSPENDED));
    benefitSuspensionAttributes.put(UNSUSPENSION_REASON,
      new CodeTableItem(BDMOASUNSUSPENSIONREASON.TABLENAME,
        BDMOASUNSUSPENSIONREASON.INVESTIGATION_COMPLETE));
    benefitSuspensionAttributes.put(UNSUSPENSION_REASON_DESCRIPTION,
      "Other reason details for Unsuspension.");

    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        modifyBenefitSuspensionEvidence(integratedCase.integratedCaseID,
          benefitSuspensionAttributes);
      });

    assertTrue(validationMessage.equals(exception.getMessage()));
  }

  /**
   * FAIL-IF New Benefit Suspension record is being created whose period
   * overlaps with an Active Benefit Suspension record which has Suspended
   * Benefit as 'All Benefits'.
   */
  @Test
  public void suspensionOfAllBenefitsOverlapFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final String validationMessage =
      "Only one 'Suspended' Benefit Suspension record that impacts a benefit may exist for any period.";

    // Evidence Details
    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(SUSPENDED_BENEFIT, BDMOASSUSPENDEDBENEFIT.ALL_BENEFITS);
    attributes.put(SUSPENSION_STATUS, BDMOASSUSPENSIONSTATUS.SUSPENDED);
    attributes.put(SUSPENSION_REASON, BDMOASSUSPENSIONREASON.ISB_REQUEST);
    attributes.put(CASE_PARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(START_DATE,
      getDateString(Date.getCurrentDate().addDays(-100)));
    attributes.put(END_DATE,
      getDateString(Date.getCurrentDate().addDays(-50)));

    // create first evidence record
    this.createEvidence(integratedCase.integratedCaseID,
      person.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.OAS_BENEFIT_SUSPENSION, attributes, getToday());

    // Apply Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCase.integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    evidenceController.applyAllChanges(caseKey);

    // Second Evidence Details
    final Map<String, String> secondAttributes =
      new HashMap<String, String>();
    secondAttributes.put(SUSPENDED_BENEFIT,
      BDMOASSUSPENDEDBENEFIT.OLD_AGE_SECURITY_PENSION);
    secondAttributes.put(SUSPENSION_STATUS, BDMOASSUSPENSIONSTATUS.SUSPENDED);
    secondAttributes.put(SUSPENSION_REASON,
      BDMOASSUSPENSIONREASON.ISB_REQUEST);
    secondAttributes.put(CASE_PARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    secondAttributes.put(START_DATE,
      getDateString(Date.getCurrentDate().addDays(-75)));

    // create second evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_BENEFIT_SUSPENSION, secondAttributes,
          getToday());
      });

    assertTrue(validationMessage.equals(exception.getMessage()));
  }

  /**
   * FAIL-IF New Benefit Suspension record is being created with Suspended
   * Benefit as 'Allowance' (this is an income tested benefit) whose period
   * overlaps with an Active Benefit Suspension record which has Suspended
   * Benefit as 'Income-Tested Benefits'.
   */
  @Test
  public void suspensionOfIncomeTestedBenefitsOverlapFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final String validationMessage =
      "Only one 'Suspended' Benefit Suspension record that impacts a benefit may exist for any period.";

    // Evidence Details
    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(SUSPENDED_BENEFIT,
      BDMOASSUSPENDEDBENEFIT.INCOME_TESTED_BENEFITS);
    attributes.put(SUSPENSION_STATUS, BDMOASSUSPENSIONSTATUS.SUSPENDED);
    attributes.put(SUSPENSION_REASON, BDMOASSUSPENSIONREASON.ISB_REQUEST);
    attributes.put(CASE_PARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(START_DATE,
      getDateString(Date.getCurrentDate().addDays(-100)));
    attributes.put(END_DATE,
      getDateString(Date.getCurrentDate().addDays(-50)));

    // create first evidence record
    this.createEvidence(integratedCase.integratedCaseID,
      person.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.OAS_BENEFIT_SUSPENSION, attributes, getToday());

    // Apply Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCase.integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    evidenceController.applyAllChanges(caseKey);

    // Second Evidence Details
    final Map<String, String> secondAttributes =
      new HashMap<String, String>();
    secondAttributes.put(SUSPENDED_BENEFIT, BDMOASSUSPENDEDBENEFIT.ALLOWANCE);
    secondAttributes.put(SUSPENSION_STATUS, BDMOASSUSPENSIONSTATUS.SUSPENDED);
    secondAttributes.put(SUSPENSION_REASON,
      BDMOASSUSPENSIONREASON.ISB_REQUEST);
    secondAttributes.put(CASE_PARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    secondAttributes.put(START_DATE,
      getDateString(Date.getCurrentDate().addDays(-75)));

    // create second evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_BENEFIT_SUSPENSION, secondAttributes,
          getToday());
      });

    assertTrue(validationMessage.equals(exception.getMessage()));
  }

  /**
   * PASS-IF New Benefit Suspension record can be created with Suspended
   * Benefit as 'Old Age Security Pension' (this is not an income tested
   * benefit) whose period overlaps with an Active Benefit Suspension record
   * which has Suspended Benefit as 'Income-Tested Benefits'.
   */
  @Test
  public void suspensionOfIncomeTestedBenefitsOverlapWithNonItbsSuccess()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    // Evidence Details
    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(SUSPENDED_BENEFIT,
      BDMOASSUSPENDEDBENEFIT.INCOME_TESTED_BENEFITS);
    attributes.put(SUSPENSION_STATUS, BDMOASSUSPENSIONSTATUS.SUSPENDED);
    attributes.put(SUSPENSION_REASON, BDMOASSUSPENSIONREASON.ISB_REQUEST);
    attributes.put(CASE_PARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(START_DATE,
      getDateString(Date.getCurrentDate().addDays(-100)));
    attributes.put(END_DATE,
      getDateString(Date.getCurrentDate().addDays(-50)));

    // create first evidence record
    this.createEvidence(integratedCase.integratedCaseID,
      person.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.OAS_BENEFIT_SUSPENSION, attributes, getToday());

    // Apply Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCase.integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    evidenceController.applyAllChanges(caseKey);

    // Second Evidence Details
    final Map<String, String> secondAttributes =
      new HashMap<String, String>();
    secondAttributes.put(SUSPENDED_BENEFIT,
      BDMOASSUSPENDEDBENEFIT.OLD_AGE_SECURITY_PENSION);
    secondAttributes.put(SUSPENSION_STATUS, BDMOASSUSPENSIONSTATUS.SUSPENDED);
    secondAttributes.put(SUSPENSION_REASON,
      BDMOASSUSPENSIONREASON.ISB_REQUEST);
    secondAttributes.put(CASE_PARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    secondAttributes.put(START_DATE,
      getDateString(Date.getCurrentDate().addDays(-75)));

    // create second evidence record
    this.createEvidence(integratedCase.integratedCaseID,
      person.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.OAS_BENEFIT_SUSPENSION, secondAttributes, getToday());

    // Apply Evidence changes
    evidenceController.applyAllChanges(caseKey);

    // Check if the evidence record was activated
    final ECActiveEvidenceDtlsList activeEvdList =
      evidenceController.listActive(caseKey);

    // Assert second record activated
    final List<ECActiveEvidenceDtls> filteredList =
      activeEvdList.dtls.stream()
        .filter(
          (x) -> x.evidenceType.equals(CASEEVIDENCE.OAS_BENEFIT_SUSPENSION))
        .collect(Collectors.toList());
    assertTrue(filteredList.size() == 2);
  }

  private String getDateString(final Date date) {

    final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");

    return dateFormatter.format(date.getCalendar().getTime());
  }

  private void modifyBenefitSuspensionEvidence(final Long caseID,
    final HashMap<String, Object> modifyData)
    throws AppException, InformationalException {

    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = caseID;

    final BDMEvidenceUtil evdUtil = new BDMEvidenceUtil();

    // Read benefit suspension evidence records
    final RelatedIDAndEvidenceTypeKeyList benefitSuspensionEvidenceList =
      evdUtil.getActiveEvidenceIDByEvidenceTypeAndCase(
        CASEEVIDENCE.OAS_BENEFIT_SUSPENSION, caseKey);

    // Pick one evidence details from evidence list
    final RelatedIDAndEvidenceTypeKey evidenceDetails =
      benefitSuspensionEvidenceList.dtls.get(0);

    // Read DynamicEvidenceDataDetails
    final EvidenceMap map =
      curam.core.sl.infrastructure.impl.EvidenceController.getEvidenceMap();
    final StandardEvidenceInterface standardEvidenceInterface =
      map.getEvidenceType(CASEEVIDENCE.OAS_BENEFIT_SUSPENSION);

    final EIEvidenceKey evidenceKey = new EIEvidenceKey();
    evidenceKey.evidenceID = evidenceDetails.relatedID;
    evidenceKey.evidenceType = CASEEVIDENCE.OAS_BENEFIT_SUSPENSION;

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
    relatedIDAndTypeKey.evidenceType = CASEEVIDENCE.OAS_BENEFIT_SUSPENSION;

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
