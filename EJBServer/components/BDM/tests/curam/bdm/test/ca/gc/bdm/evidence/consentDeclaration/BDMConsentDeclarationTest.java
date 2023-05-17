
package curam.bdm.test.ca.gc.bdm.evidence.consentDeclaration;

import curam.ca.gc.bdm.codetable.BDMSIGNATURETYPE;
import curam.ca.gc.bdm.codetable.BDMWITNESSRELATIONSHIP;
import curam.creole.execution.session.InterpretedRuleObjectFactory;
import curam.creole.execution.session.ManualRecalculation;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.ruleclass.BDMConsentDeclarationRuleSet.impl.BDMConsentDeclaration;
import curam.creole.ruleclass.BDMConsentDeclarationRuleSet.impl.BDMConsentDeclaration_Factory;
import curam.creole.ruleclass.BDMConsentDeclarationSummaryRuleSet.impl.SummaryInformation;
import curam.creole.ruleclass.BDMConsentDeclarationSummaryRuleSet.impl.SummaryInformation_Factory;
import curam.creole.ruleclass.BDMConsentDeclarationValidationRuleSet.impl.ValidationResult;
import curam.creole.ruleclass.BDMConsentDeclarationValidationRuleSet.impl.ValidationResult_Factory;
import curam.creole.storage.inmemory.InMemoryDataStorage;
import curam.creole.value.CodeTableItem;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// Task 58899 DEV: Implement Manage Client Marital Status
/**
 * Test configured rules-based validations for BDMMaritalStatus evidence.
 */
public class BDMConsentDeclarationTest
  extends curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest {

  private Session session;

  @Override
  public void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    this.session =
      Session_Factory.getFactory().newInstance(new ManualRecalculation(),
        new InMemoryDataStorage(new InterpretedRuleObjectFactory()));

  }

  /**
   * PASS-IF a validation fails when
   * 1. signature type is 'Signed with a mark'
   * 2. signing on behalf is false
   * 3. date signed and witness date signed are not same
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void dateSignedWitnessDateSignedValidationFailure()
    throws AppException, InformationalException {

    final BDMConsentDeclaration evidence = this.getEvidence();

    final ValidationResult validator = this.getValidator(evidence);

    evidence.signatureType().specifyValue(new CodeTableItem(
      BDMSIGNATURETYPE.TABLENAME, BDMSIGNATURETYPE.SIGNED_WITH_MARK));
    evidence.isSigningOnBehalf().specifyValue(false);
    evidence.dateSigned().specifyValue(Date.getCurrentDate());
    evidence.witnessDateSigned()
      .specifyValue(Date.getCurrentDate().addDays(-1));

    assertTrue(
      validator.dateSignedWitnessDateSignedValidationFailure().getValue());

  }

  /**
   * PASS-IF a validation does not fail when
   * 1. signature type is 'Signed with a mark'
   * 2. signing on behalf is false
   * 3. date signed and witness date signed are same
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void dateSignedWitnessDateSignedValidationSuccess()
    throws AppException, InformationalException {

    final BDMConsentDeclaration evidence = this.getEvidence();

    final ValidationResult validator = this.getValidator(evidence);

    evidence.signatureType().specifyValue(new CodeTableItem(
      BDMSIGNATURETYPE.TABLENAME, BDMSIGNATURETYPE.SIGNED_WITH_MARK));
    evidence.isSigningOnBehalf().specifyValue(false);
    evidence.dateSigned().specifyValue(Date.getCurrentDate());
    evidence.witnessDateSigned().specifyValue(Date.getCurrentDate());

    assertFalse(
      validator.dateSignedWitnessDateSignedValidationFailure().getValue());

  }

  /**
   * Mandatory field participant missing summary test.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void mandatoryFieldParticipantMissingSummaryTest()
    throws AppException, InformationalException {

    final BDMConsentDeclaration evidence = this.getEvidence();

    final SummaryInformation summary = this.getSummary(evidence);

    evidence.signatureType().specifyValue(
      new CodeTableItem(BDMSIGNATURETYPE.TABLENAME, BDMSIGNATURETYPE.SIGNED));
    evidence.participant().specifyValue(null);

    final ResourceBundle resourceBundle = ResourceBundle.getBundle(
      "curam.ca.gc.bdm.evidence.BDMConsentDeclarationSummaryRuleSet");

    final String validationMessage =
      resourceBundle.getString("MandatoryFieldsMissing.Message");

    assertEquals(validationMessage, summary.summary().getValue().toString());
  }

  /**
   * Mandatory field signature type missing summary test.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void mandatoryFieldSignatureTypeMissingSummaryTest()
    throws AppException, InformationalException {

    final BDMConsentDeclaration evidence = this.getEvidence();

    final SummaryInformation summary = this.getSummary(evidence);

    evidence.signatureType().specifyValue(null);
    evidence.participant().specifyValue(123l);

    final ResourceBundle resourceBundle = ResourceBundle.getBundle(
      "curam.ca.gc.bdm.evidence.BDMConsentDeclarationSummaryRuleSet");

    final String validationMessage =
      resourceBundle.getString("MandatoryFieldsMissing.Message");

    assertEquals(validationMessage, summary.summary().getValue().toString());
  }

  /**
   * Conditionally mandatory field date signed missing summary test.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void conditionallyMandatoryFieldDateSignedMissingSummaryTest()
    throws AppException, InformationalException {

    final BDMConsentDeclaration evidence = this.getEvidence();

    final SummaryInformation summary = this.getSummary(evidence);

    evidence.signatureType().specifyValue(
      new CodeTableItem(BDMSIGNATURETYPE.TABLENAME, BDMSIGNATURETYPE.SIGNED));
    evidence.dateSigned().specifyValue(null);
    evidence.participant().specifyValue(123l);

    final ResourceBundle resourceBundle = ResourceBundle.getBundle(
      "curam.ca.gc.bdm.evidence.BDMConsentDeclarationSummaryRuleSet");

    final String validationMessage =
      resourceBundle.getString("MandatoryFieldsMissing.Message");

    assertEquals(validationMessage, summary.summary().getValue().toString());
  }

  /**
   * Conditionally mandatory field acting as witness not checked summary test.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void
    conditionallyMandatoryFieldActingAsWitnessNotCheckedSummaryTest()
      throws AppException, InformationalException {

    final BDMConsentDeclaration evidence = this.getEvidence();

    final SummaryInformation summary = this.getSummary(evidence);

    evidence.signatureType().specifyValue(new CodeTableItem(
      BDMSIGNATURETYPE.TABLENAME, BDMSIGNATURETYPE.SIGNED_WITH_MARK));
    evidence.dateSigned().specifyValue(Date.getCurrentDate());
    evidence.participant().specifyValue(123l);
    evidence.witness().specifyValue(456l);
    evidence.isWitness().specifyValue(false);

    final ResourceBundle resourceBundle = ResourceBundle.getBundle(
      "curam.ca.gc.bdm.evidence.BDMConsentDeclarationSummaryRuleSet");

    final String validationMessage =
      resourceBundle.getString("MandatoryFieldsMissing.Message");

    assertEquals(validationMessage, summary.summary().getValue().toString());
  }

  /**
   * Conditionally mandatory field signing on behalf not checked summary test.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void
    conditionallyMandatoryFieldSigningOnBehalfNotCheckedSummaryTest()
      throws AppException, InformationalException {

    final BDMConsentDeclaration evidence = this.getEvidence();

    final SummaryInformation summary = this.getSummary(evidence);

    evidence.signatureType().specifyValue(new CodeTableItem(
      BDMSIGNATURETYPE.TABLENAME, BDMSIGNATURETYPE.NOT_SIGNED));
    evidence.dateSigned().specifyValue(null);
    evidence.participant().specifyValue(123l);
    evidence.witness().specifyValue(456l);
    evidence.isSigningOnBehalf().specifyValue(false);

    final ResourceBundle resourceBundle = ResourceBundle.getBundle(
      "curam.ca.gc.bdm.evidence.BDMConsentDeclarationSummaryRuleSet");

    final String validationMessage =
      resourceBundle.getString("MandatoryFieldsMissing.Message");

    assertEquals(validationMessage, summary.summary().getValue().toString());
  }

  /**
   * Conditionally mandatory field witness date signed missing summary test.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void conditionallyMandatoryFieldWitnessDateSignedMissingSummaryTest()
    throws AppException, InformationalException {

    final BDMConsentDeclaration evidence = this.getEvidence();

    final SummaryInformation summary = this.getSummary(evidence);

    evidence.signatureType().specifyValue(new CodeTableItem(
      BDMSIGNATURETYPE.TABLENAME, BDMSIGNATURETYPE.NOT_SIGNED));
    evidence.dateSigned().specifyValue(null);
    evidence.participant().specifyValue(123l);
    evidence.isSigningOnBehalf().specifyValue(false);
    evidence.witness().specifyValue(456l);
    evidence.witnessDateSigned().specifyValue(null);

    final ResourceBundle resourceBundle = ResourceBundle.getBundle(
      "curam.ca.gc.bdm.evidence.BDMConsentDeclarationSummaryRuleSet");

    final String validationMessage =
      resourceBundle.getString("MandatoryFieldsMissing.Message");

    assertEquals(validationMessage, summary.summary().getValue().toString());
  }

  /**
   * Conditionally mandatory field witness relationship missing summary test.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void
    conditionallyMandatoryFieldWitnessRelationshipMissingSummaryTest()
      throws AppException, InformationalException {

    final BDMConsentDeclaration evidence = this.getEvidence();

    final SummaryInformation summary = this.getSummary(evidence);

    evidence.signatureType().specifyValue(new CodeTableItem(
      BDMSIGNATURETYPE.TABLENAME, BDMSIGNATURETYPE.SIGNED_WITH_MARK));
    evidence.dateSigned().specifyValue(Date.getCurrentDate());
    evidence.participant().specifyValue(123l);
    evidence.witness().specifyValue(456l);
    evidence.isWitness().specifyValue(true);
    evidence.witnessDateSigned().specifyValue(Date.getCurrentDate());
    evidence.dateSigned().specifyValue(Date.getCurrentDate());
    evidence.relationshipType().specifyValue(null);

    final ResourceBundle resourceBundle = ResourceBundle.getBundle(
      "curam.ca.gc.bdm.evidence.BDMConsentDeclarationSummaryRuleSet");

    final String validationMessage =
      resourceBundle.getString("MandatoryFieldsMissing.Message");

    assertEquals(validationMessage, summary.summary().getValue().toString());
  }

  /**
   * Singed with mark witness missing summary test.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void singedWithMarkWitnessMissingSummaryTest()
    throws AppException, InformationalException {

    final BDMConsentDeclaration evidence = this.getEvidence();

    final SummaryInformation summary = this.getSummary(evidence);

    evidence.signatureType().specifyValue(new CodeTableItem(
      BDMSIGNATURETYPE.TABLENAME, BDMSIGNATURETYPE.SIGNED_WITH_MARK));
    evidence.dateSigned().specifyValue(Date.getCurrentDate());
    evidence.participant().specifyValue(123l);
    evidence.witness().specifyValue(null);

    final ResourceBundle resourceBundle = ResourceBundle.getBundle(
      "curam.ca.gc.bdm.evidence.BDMConsentDeclarationSummaryRuleSet");

    final String validationMessage =
      resourceBundle.getString("MarkedWitnessMissing.Message");

    assertEquals(validationMessage, summary.summary().getValue().toString());
  }

  /**
   * Singed with mark summary test.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void singedWithMarkSummaryTest()
    throws AppException, InformationalException {

    final BDMConsentDeclaration evidence = this.getEvidence();

    final SummaryInformation summary = this.getSummary(evidence);

    evidence.signatureType().specifyValue(new CodeTableItem(
      BDMSIGNATURETYPE.TABLENAME, BDMSIGNATURETYPE.SIGNED_WITH_MARK));
    evidence.dateSigned().specifyValue(Date.getCurrentDate());
    evidence.participant().specifyValue(123l);
    evidence.witness().specifyValue(456l);
    evidence.isWitness().specifyValue(true);
    evidence.witnessDateSigned().specifyValue(Date.getCurrentDate());
    evidence.relationshipType().specifyValue(new CodeTableItem(
      BDMWITNESSRELATIONSHIP.TABLENAME, BDMWITNESSRELATIONSHIP.UNCLE));
    summary.witnessName().specifyValue("Ricky");

    final String validationMessage = "Signed with a mark  "
      + formattedDate(Date.getCurrentDate(), "yyyy-MM-dd")
      + ", witnessed by Ricky";

    assertEquals(validationMessage, summary.summary().getValue().toString());
  }

  /**
   * Not signed witness missing summary test.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void notSignedWitnessMissingSummaryTest()
    throws AppException, InformationalException {

    final BDMConsentDeclaration evidence = this.getEvidence();

    final SummaryInformation summary = this.getSummary(evidence);

    evidence.signatureType().specifyValue(new CodeTableItem(
      BDMSIGNATURETYPE.TABLENAME, BDMSIGNATURETYPE.NOT_SIGNED));
    evidence.dateSigned().specifyValue(null);
    evidence.participant().specifyValue(123l);
    evidence.witness().specifyValue(456l);
    evidence.isSigningOnBehalf().specifyValue(true);
    evidence.witnessDateSigned().specifyValue(Date.getCurrentDate());
    evidence.relationshipType().specifyValue(new CodeTableItem(
      BDMWITNESSRELATIONSHIP.TABLENAME, BDMWITNESSRELATIONSHIP.UNCLE));
    summary.witnessName().specifyValue("Ricky");

    final String validationMessage = "Signed by Ricky  "
      + formattedDate(Date.getCurrentDate(), "yyyy-MM-dd");

    assertEquals(validationMessage, summary.summary().getValue().toString());
  }

  /**
   * Signed summary test.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void signedSummaryTest()
    throws AppException, InformationalException {

    final BDMConsentDeclaration evidence = this.getEvidence();

    final SummaryInformation summary = this.getSummary(evidence);

    evidence.signatureType().specifyValue(
      new CodeTableItem(BDMSIGNATURETYPE.TABLENAME, BDMSIGNATURETYPE.SIGNED));
    evidence.dateSigned().specifyValue(Date.getCurrentDate());
    evidence.participant().specifyValue(123l);
    evidence.witness().specifyValue(null);

    final String validationMessage =
      "Signed  " + formattedDate(Date.getCurrentDate(), "yyyy-MM-dd");

    assertEquals(validationMessage, summary.summary().getValue().toString());
  }

  /**
   * Instantiates and returns the evidence rule object.
   *
   * @return
   */
  private BDMConsentDeclaration getEvidence() {

    return BDMConsentDeclaration_Factory.getFactory().newInstance(session);

  }

  /**
   * Instatiates and returns a validator rule object for the given evidence.
   *
   * @param evidence
   * @return
   */
  private ValidationResult
    getValidator(final BDMConsentDeclaration evidence) {

    final ValidationResult validator =
      ValidationResult_Factory.getFactory().newInstance(session);

    validator.evidence().specifyValue(evidence);

    return validator;

  }

  /**
   * Instatiates and returns a summary rule object for the given evidence.
   *
   * @param evidence
   * @return
   */
  private SummaryInformation
    getSummary(final BDMConsentDeclaration evidence) {

    final SummaryInformation summary =
      SummaryInformation_Factory.getFactory().newInstance(session);

    summary.evidence().specifyValue(evidence);

    return summary;

  }

  /**
   * Formatted date.
   *
   * @param date the date
   * @param format the format
   * @return the string
   */
  private String formattedDate(final Date date, final String format) {

    final SimpleDateFormat dateFormat = new SimpleDateFormat(format);

    return dateFormat.format(date.getCalendar().getTime());
  }

}
