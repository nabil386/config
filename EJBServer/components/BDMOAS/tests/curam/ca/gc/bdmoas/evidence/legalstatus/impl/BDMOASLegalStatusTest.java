package curam.ca.gc.bdmoas.evidence.legalstatus.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdmoas.codetable.BDMOASLEGALSTATUS;
import curam.ca.gc.bdmoas.evidence.constants.impl.BDMOASLegalStatusConstants;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.facade.struct.CreateIntegratedCaseResult;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.creole.execution.session.InterpretedRuleObjectFactory;
import curam.creole.execution.session.ManualRecalculation;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.ruleclass.BDMOASLegalStatusRuleSet.impl.BDMOASLegalStatus_Factory;
import curam.creole.ruleclass.BDMOASLegalStatusValidationRuleSet.impl.ValidationResult;
import curam.creole.ruleclass.BDMOASLegalStatusValidationRuleSet.impl.ValidationResult_Factory;
import curam.creole.storage.inmemory.InMemoryDataStorage;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// Task 82485 DEV: Implement BDM OAS Legal Status Case Evidence
/**
 * Test validations for OASLegalStatus evidence.
 */
public class BDMOASLegalStatusTest extends BDMOASCaseTest {

  private Session session;

  public static final String ERR_OVERLAPPING_LEGAL_STATUS =
    "A Legal Status record already exists for an overlapping period.";

  public static final String ERR_START_DATE_AFTER_CURRENT_DATE =
    "The Start Date must be on or before the Current Date.";

  public static final String ERR_LEGAL_STATUS_OTHER_THEN_OTHERLEGALSTATUS_MANDATORY =
    "'Other' Legal Status Details must be entered if Legal Status is 'Other'.";

  public static final String ERR_OTHERLEGALSTATUSDETAILS_ONLY_WEHN_LEGAL_STATUS_OTHER =
    "'Other' Legal Status Details can only be entered if Legal Status is 'Other'.";

  @Override
  protected void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    this.session =
      Session_Factory.getFactory().newInstance(new ManualRecalculation(),
        new InMemoryDataStorage(new InterpretedRuleObjectFactory()));

  }

  /**
   * PASS-IF a validation fails when overlapping evidences are applied.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void overlappingEvidenceFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(BDMOASLegalStatusConstants.LEGAL_STATUS,
      BDMOASLEGALSTATUS.CANADIAN_CITIZEN);
    attributes.put(BDMOASLegalStatusConstants.START_DATE, "19980101");
    attributes.put(BDMOASLegalStatusConstants.PARTICIPANT,
      String.valueOf(cprObj.caseParticipantRoleID));

    this.createEvidence(integratedCase.integratedCaseID,
      person.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.OAS_LEGAL_STATUS, attributes, getToday());

    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_LEGAL_STATUS, attributes, getToday());
      });

    assertTrue(ERR_OVERLAPPING_LEGAL_STATUS.equals(exception.getMessage()));

  }

  /**
   * Pass-IF Start Date must be on or before Current Date Failure.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void startDateMustBeOnorBeforeCurrentDateFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(BDMOASLegalStatusConstants.LEGAL_STATUS,
      BDMOASLEGALSTATUS.CANADIAN_CITIZEN);
    attributes.put(BDMOASLegalStatusConstants.START_DATE, "20250101");
    attributes.put(BDMOASLegalStatusConstants.PARTICIPANT,
      String.valueOf(cprObj.caseParticipantRoleID));

    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_LEGAL_STATUS, attributes, getToday());
      });

    assertTrue(
      ERR_START_DATE_AFTER_CURRENT_DATE.equals(exception.getMessage()));

  }

  /**
   * Pass-IF Legal Status is 'Other' and 'Other' Legal Status Details is not
   * entered.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void legalStatusIsOtherAndOtherLegalStatusDetailsNotEnteredFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(BDMOASLegalStatusConstants.LEGAL_STATUS,
      BDMOASLEGALSTATUS.OTHER);
    attributes.put(BDMOASLegalStatusConstants.START_DATE, "20200101");
    attributes.put(BDMOASLegalStatusConstants.PARTICIPANT,
      String.valueOf(cprObj.caseParticipantRoleID));

    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_LEGAL_STATUS, attributes, getToday());
      });

    assertTrue(ERR_LEGAL_STATUS_OTHER_THEN_OTHERLEGALSTATUS_MANDATORY
      .equals(exception.getMessage()));

  }

  /**
   * Pass-IF 'Other' Legal Status Details is entered then Legal Status should be
   * 'Other' selected.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void OtherLegalStatusDetailsEnteredTheLegalStatusIsNotOtherFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(BDMOASLegalStatusConstants.LEGAL_STATUS,
      BDMOASLEGALSTATUS.CANADIAN_CITIZEN);
    attributes.put(BDMOASLegalStatusConstants.OTHER_LEGAL_STATUS_DETAILS,
      "Test Other Legal Status Details");
    attributes.put(BDMOASLegalStatusConstants.START_DATE, "20200101");
    attributes.put(BDMOASLegalStatusConstants.PARTICIPANT,
      String.valueOf(cprObj.caseParticipantRoleID));

    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_LEGAL_STATUS, attributes, getToday());
      });

    assertTrue(ERR_OTHERLEGALSTATUSDETAILS_ONLY_WEHN_LEGAL_STATUS_OTHER
      .equals(exception.getMessage()));

  }

  /**
   * PASS-IF a validation is thrown when the effective date is after the end
   * date.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void effectiveDateEndDateFailure() {

    final curam.creole.ruleclass.BDMOASLegalStatusRuleSet.impl.BDMOASLegalStatus evidence =
      this.getEvidence();
    evidence.effectiveFrom().specifyValue(Date.getCurrentDate());
    evidence.endDate().specifyValue(Date.fromISO8601("19980101"));

    final ValidationResult validation = this.getValidator(evidence);

    assertTrue(validation.effectiveDateEndDateFailure().getValue());

  }

  /**
   * PASS-IF a validation is not thrown when the effective date is before the
   * end
   * date.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void effectiveDateEndDatePass() {

    final curam.creole.ruleclass.BDMOASLegalStatusRuleSet.impl.BDMOASLegalStatus evidence =
      this.getEvidence();
    evidence.effectiveFrom().specifyValue(Date.fromISO8601("19980101"));
    evidence.endDate().specifyValue(Date.getCurrentDate());

    final ValidationResult validation = this.getValidator(evidence);

    assertFalse(validation.effectiveDateEndDateFailure().getValue());

  }

  private
    curam.creole.ruleclass.BDMOASLegalStatusRuleSet.impl.BDMOASLegalStatus
    getEvidence() {

    return BDMOASLegalStatus_Factory.getFactory().newInstance(this.session);

  }

  /**
   * Instatiates and returns a validator rule object for the given evidence.
   *
   * @param evidence
   * @return
   */
  private ValidationResult getValidator(
    final curam.creole.ruleclass.BDMOASLegalStatusRuleSet.impl.BDMOASLegalStatus evidence) {

    final ValidationResult validator =
      ValidationResult_Factory.getFactory().newInstance(session);

    validator.evidence().specifyValue(evidence);

    return validator;

  }

}
