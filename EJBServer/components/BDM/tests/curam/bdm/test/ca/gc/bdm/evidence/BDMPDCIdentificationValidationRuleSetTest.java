package curam.bdm.test.ca.gc.bdm.evidence;

import com.google.inject.Inject;
import curam.bdm.test.ca.gc.bdm.foreignsource.impl.ForeignCountryValidationHelperTest;
import curam.ca.gc.bdm.codetable.BDMMODEOFRECEIPT;
import curam.ca.gc.bdm.codetable.BDMRECEIVEDFROM;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest;
import curam.ca.gc.bdm.test.evidence.BDMPDCContactPreferenceEvidenceTest;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.creole.calculator.CREOLETestHelper;
import curam.creole.execution.session.RecalculationsProhibited;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.execution.session.StronglyTypedRuleObjectFactory;
import curam.creole.ruleclass.PDCIdentificationRuleSet.impl.PDCIdentification;
import curam.creole.ruleclass.PDCIdentificationRuleSet.impl.PDCIdentification_Factory;
import curam.creole.ruleclass.PDCIdentificationValidationRuleSet.impl.ValidationResult;
import curam.creole.ruleclass.PDCIdentificationValidationRuleSet.impl.ValidationResult_Factory;
import curam.creole.storage.inmemory.InMemoryDataStorage;
import curam.creole.value.CodeTableItem;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDefDAO;
import curam.participant.impl.ConcernRoleDAO;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 * 01/26/2022 10847 agole New Test class to test Identification EVidence.
 *
 */

public class BDMPDCIdentificationValidationRuleSetTest
  extends BDMEvidenceUtilsTest {

  @Inject
  protected ConcernRoleDAO concernRoleDAO;

  @Inject
  protected EvidenceTypeVersionDefDAO etVerDefDAO;

  Session session;

  private final String ERR_SIN_MOD10 =
    "The SIN number entered is not a valid number. Please check and enter a valid SIN number.";

  private final String ERR_EXISTING_ACTIVE_SIN =
    "An active SIN number is present in the system. Please end date the existing SIN number to add a new SIN number.";

  private final String ERR_ID_TYPE_PREFERRED =
    "This Identification type cannot be selected as Preferred.";

  /*
   * private final String ERR_EXISTING_ACTIVE_DL =
   * "An active Drivers License number is present in the system. Please end date the existing Drivers License number to add a new SIN number."
   * ;
   *
   * private final String ERR_EXISTING_ACTIVE_PROVNUM =
   * "An active Provincial Identification Number is present in the system. Please end date the existing Provincial Identification Number to add a new SIN number."
   * ;
   *
   * private final String ERR_EXISTING_ACTIVE_HCNUM =
   * "An active Health Card Number is present in the system. Please end date the existing Health Card Numberto add a new SIN number."
   * ;
   * private final String ERR_DUPLICATE_ID =
   * "An Identification of the same type already exists for the client for the for the same period."
   * ;
   *
   * private final String ERR_OVERLAP_DLID =
   * "An Identification of the same type already exists for the client for the same period."
   * ;
   */

  @Inject
  BDMPDCContactPreferenceEvidenceTest bdmPDCContactPreferenceEvidenceTest;

  // BEGIN 57386 DEV: Implement Identification Evidence Changes
  private final ForeignCountryValidationHelperTest helper =
    new ForeignCountryValidationHelperTest();
  // END 57386 DEV: Implement Identification Evidence Changes

  @Override
  public void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    session =
      Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
        new InMemoryDataStorage(new StronglyTypedRuleObjectFactory()));
  }

  /**
   * @date: Jan. 26, 2022
   * @decription: Test to validate valid SIN number
   *
   * @param
   * @revoid
   * @throws
   */
  @Test
  public void testIsSINValid() {

    final curam.creole.ruleclass.PDCIdentificationValidationRuleSet.impl.ValidationResult validationResult =
      ValidationResult_Factory.getFactory().newInstance(session);

    final curam.creole.ruleclass.PDCIdentificationRuleSet.impl.PDCIdentification pDCIdentification =
      PDCIdentification_Factory.getFactory().newInstance(session);

    pDCIdentification.altIDType()
      .specifyValue(new CodeTableItem(CONCERNROLEALTERNATEID.TABLENAME,
        CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER)); // BDMCA80002

    pDCIdentification.alternateID().specifyValue("366698942");

    validationResult.evidence().specifyValue(pDCIdentification);

    CREOLETestHelper.assertEquals(false, validationResult.sinMod10Validation()
      .getValue().isFailure().getValue());
  }

  /**
   * @date: Jan. 26, 2022
   * @decription: Test Duplicate SIN Evidence
   *
   * @param @throws AppException
   * @param @throws InformationalException
   * @revoid
   * @throws
   */
  @Ignore
  @Test
  public void testDuplicateSINIdentificationEvidence()
    throws AppException, InformationalException {

    final PDCPersonDetails pdcPersonDetails = createPDCPerson();
    // create new Identification evidence with some data ,
    createIdentificationEvidence(pdcPersonDetails, "366698942",
      CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER, true);
    // create new Identification evidence with same data ,should throw an
    // duplicate error message "existing SIN is present"
    try {
      createIdentificationEvidence(pdcPersonDetails, "366698942",
        CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER, true);
    } catch (final Exception e) {
      assertEquals(ERR_EXISTING_ACTIVE_SIN, e.getMessage());
    }
  }

  /**
   * Test that a Validate Invalid SIN number
   * specified
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testIsSINInvalid() throws AppException, InformationalException {

    final curam.creole.ruleclass.PDCIdentificationValidationRuleSet.impl.ValidationResult validationResult =
      ValidationResult_Factory.getFactory().newInstance(session);

    final curam.creole.ruleclass.PDCIdentificationRuleSet.impl.PDCIdentification pDCIdentification =
      PDCIdentification_Factory.getFactory().newInstance(session);
    // create new Identification evidence with invalid SIN data ,
    pDCIdentification.altIDType()
      .specifyValue(new CodeTableItem(CONCERNROLEALTERNATEID.TABLENAME,
        CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER)); // BDMCA80002

    pDCIdentification.alternateID().specifyValue("2635");

    validationResult.evidence().specifyValue(pDCIdentification);

    CREOLETestHelper.assertEquals(true, validationResult.sinMod10Validation()
      .getValue().isFailure().getValue());

  }

  /**
   * @date: Jan. 26, 2022
   * @decription: Test that a Validate Invalid SIN number error message
   * Identification evidence
   *
   * @param @throws AppException
   * @param @throws InformationalException
   * @revoid
   * @throws
   */
  @Ignore
  @Test
  public void testIsSINInvalid_1()
    throws AppException, InformationalException {

    boolean caught = false;
    final PDCPersonDetails pdcPersonDetails = createPDCPerson();
    try {
      // create new Identification evidence with invalid SIN data ,
      createIdentificationEvidence(pdcPersonDetails, "2365",
        CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER, true);

    } catch (final Exception e) {
      caught = true;
      assertEquals(ERR_SIN_MOD10, e.getMessage());
    }
    assertTrue(caught);
  }

  /**
   * @date: Jan. 26, 2022
   * @decription: System should not accept two evidence selected as a preferred
   * Identification evidence
   *
   * @param @throws AppException
   * @param @throws InformationalException
   * @revoid
   * @throws
   */
  @Test
  public void testPreferredIDValidation()
    throws AppException, InformationalException {

    boolean caught = false;
    final PDCPersonDetails pdcPersonDetails = createPDCPerson();
    // create new Identification evidence for SIN and set it as Preferred
    // evidence ,
    final EIEvidenceKey key = createIdentificationEvidence(pdcPersonDetails,
      "366698942", CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER, true);

    // create new Identification evidence for DL and set it as Preferred
    // evidence , should
    // throw an error message This Identification type cannot be selected as
    // Preferred
    try {
      createIdentificationEvidence(pdcPersonDetails, "444173033",
        CONCERNROLEALTERNATEID.DRIVERSLICENSENUMBER, true);
    } catch (final Exception e) {
      caught = true;
      assertEquals(ERR_ID_TYPE_PREFERRED, e.getMessage());
    }
    assertTrue(caught);
  }

  // BEGIN 57386 DEV: Implement Identification Evidence Changes
  /**
   * PASS-IF a validation fails when additional required source fields are
   * not entered when the Received From is Foreign Government.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void mandatorySourceFieldFailure()
    throws AppException, InformationalException {

    final PDCIdentification evidence = this.getEvidence();
    PDCIdentification_Factory.getFactory().newInstance(this.session);

    final ValidationResult validator = this.getValidator(evidence);

    validator.evidence().specifyValue(evidence);

    final CodeTableItem receivedFrom =
      this.helper.getReceivedFrom(BDMRECEIVEDFROM.FOREIGN_GOVERNMENT);

    evidence.bdmReceivedFrom().specifyValue(receivedFrom);
    evidence.bdmReceivedFromCountry().specifyValue(null);
    evidence.bdmModeOfReceipt().specifyValue(null);

    assertTrue(validator.mandatorySourceFieldFailure().getValue());

  }

  /**
   * PASS-IF a validation passes when additional required source fields
   * are entered when the Received From is Foreign Government.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void mandatorySourceFieldPass()
    throws AppException, InformationalException {

    final PDCIdentification evidence = this.getEvidence();
    PDCIdentification_Factory.getFactory().newInstance(this.session);

    final ValidationResult validator = this.getValidator(evidence);

    validator.evidence().specifyValue(evidence);

    final CodeTableItem receivedFrom =
      this.helper.getReceivedFrom(BDMRECEIVEDFROM.FOREIGN_GOVERNMENT);
    final CodeTableItem receivedFromCountry =
      this.helper.getReceivedFromCountry(BDMSOURCECOUNTRY.US);
    final CodeTableItem modeOfReceipt =
      this.helper.getModeOfReceipt(BDMMODEOFRECEIPT.CERTIFIED_APP);

    evidence.bdmReceivedFrom().specifyValue(receivedFrom);
    evidence.bdmReceivedFromCountry().specifyValue(receivedFromCountry);
    evidence.bdmModeOfReceipt().specifyValue(modeOfReceipt);

    assertFalse(validator.mandatorySourceFieldFailure().getValue());

  }

  /**
   * PASS-IF a validation fails when additional source fields are
   * entered and the Received From is not Foreign Government.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void disallowedSourceFieldFailure()
    throws AppException, InformationalException {

    final PDCIdentification evidence = this.getEvidence();
    PDCIdentification_Factory.getFactory().newInstance(this.session);

    final ValidationResult validator = this.getValidator(evidence);

    validator.evidence().specifyValue(evidence);

    final CodeTableItem receivedFrom =
      this.helper.getReceivedFrom(BDMRECEIVEDFROM.CLIENT_REPORTED);
    final CodeTableItem receivedFromCountry =
      this.helper.getReceivedFromCountry(BDMSOURCECOUNTRY.US);
    final CodeTableItem modeOfReceipt =
      this.helper.getModeOfReceipt(BDMMODEOFRECEIPT.CERTIFIED_APP);

    evidence.bdmReceivedFrom().specifyValue(receivedFrom);
    evidence.bdmReceivedFromCountry().specifyValue(receivedFromCountry);
    evidence.bdmModeOfReceipt().specifyValue(modeOfReceipt);

    assertTrue(validator.disallowedSourceFieldFailure().getValue());

  }

  /**
   * PASS-IF a validation passes when additional source fields are
   * entered and the Received From is Foreign Government.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void disallowedSourceFieldPass()
    throws AppException, InformationalException {

    final PDCIdentification evidence = this.getEvidence();
    PDCIdentification_Factory.getFactory().newInstance(this.session);

    final ValidationResult validator = this.getValidator(evidence);

    validator.evidence().specifyValue(evidence);

    final CodeTableItem receivedFrom =
      this.helper.getReceivedFrom(BDMRECEIVEDFROM.FOREIGN_GOVERNMENT);
    final CodeTableItem receivedFromCountry =
      this.helper.getReceivedFromCountry(BDMSOURCECOUNTRY.US);
    final CodeTableItem modeOfReceipt =
      this.helper.getModeOfReceipt(BDMMODEOFRECEIPT.CERTIFIED_APP);

    evidence.bdmReceivedFrom().specifyValue(receivedFrom);
    evidence.bdmModeOfReceipt().specifyValue(modeOfReceipt);
    evidence.bdmReceivedFromCountry().specifyValue(receivedFromCountry);

    assertFalse(validator.disallowedSourceFieldFailure().getValue());

  }

  /**
   * PASS-IF a validation fails when an Received From is Foreign
   * Government, Mode of Receipt is Liaison and the Received From Country is one
   * of the restricted countries.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void restrictedCountryFailure()
    throws AppException, InformationalException {

    final PDCIdentification evidence = this.getEvidence();
    PDCIdentification_Factory.getFactory().newInstance(this.session);

    final ValidationResult validator = this.getValidator(evidence);

    validator.evidence().specifyValue(evidence);

    final CodeTableItem receivedFrom =
      this.helper.getReceivedFrom(BDMRECEIVEDFROM.FOREIGN_GOVERNMENT);
    final CodeTableItem receivedFromCountry =
      this.helper.getReceivedFromCountry(BDMSOURCECOUNTRY.US);
    final CodeTableItem modeOfReceipt =
      this.helper.getModeOfReceipt(BDMMODEOFRECEIPT.LIAISON);

    evidence.bdmReceivedFrom().specifyValue(receivedFrom);
    evidence.bdmReceivedFromCountry().specifyValue(receivedFromCountry);
    evidence.bdmModeOfReceipt().specifyValue(modeOfReceipt);

    assertTrue(validator.restrictedCountryFailure().getValue());

  }

  /**
   * PASS-IF a validation passes when an Received From is Foreign
   * Government, Mode of Receipt is Liaison and the Received From Country is not
   * one of the restricted countries.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void restrictedCountryPass()
    throws AppException, InformationalException {

    final PDCIdentification evidence = this.getEvidence();
    PDCIdentification_Factory.getFactory().newInstance(this.session);

    final ValidationResult validator = this.getValidator(evidence);

    validator.evidence().specifyValue(evidence);

    final CodeTableItem receivedFrom =
      this.helper.getReceivedFrom(BDMRECEIVEDFROM.FOREIGN_GOVERNMENT);
    final CodeTableItem receivedFromCountry =
      this.helper.getReceivedFromCountry(BDMSOURCECOUNTRY.IRELAND);
    final CodeTableItem modeOfReceipt =
      this.helper.getModeOfReceipt(BDMMODEOFRECEIPT.LIAISON);

    evidence.bdmReceivedFrom().specifyValue(receivedFrom);
    evidence.bdmReceivedFromCountry().specifyValue(receivedFromCountry);
    evidence.bdmModeOfReceipt().specifyValue(modeOfReceipt);

    assertFalse(validator.restrictedCountryFailure().getValue());

  }

  /**
   * PASS-IF a validation fails when an OAS Account Number is not 9-digits long.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void validBDMAccountNumberLengthFailure()
    throws AppException, InformationalException {

    final CodeTableItem altIDType =
      this.getAltIDType(CONCERNROLEALTERNATEID.BDM_ACCOUNT_NUMBER);

    final PDCIdentification evidence = this.getEvidence();

    final ValidationResult validator = this.getValidator(evidence);

    validator.evidence().specifyValue(evidence);

    evidence.altIDType().specifyValue(altIDType);

    evidence.alternateID().specifyValue("0123");

    assertTrue(validator.validLengthFailure().getValue());

  }

  /**
   * PASS-IF a validation passes when an OAS Account Number is 9-digits long.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void validBDMAccountNumberLengthPass()
    throws AppException, InformationalException {

    final CodeTableItem altIDType =
      this.getAltIDType(CONCERNROLEALTERNATEID.BDM_ACCOUNT_NUMBER);

    final PDCIdentification evidence = this.getEvidence();

    final ValidationResult validator = this.getValidator(evidence);

    validator.evidence().specifyValue(evidence);

    evidence.altIDType().specifyValue(altIDType);

    evidence.alternateID().specifyValue("123456789");

    assertFalse(validator.validLengthFailure().getValue());

  }

  /**
   * PASS-IF a validation fails when an IA Account Number is not 9-digits long.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void validIAAccountNumberLengthFailure()
    throws AppException, InformationalException {

    final CodeTableItem altIDType =
      this.getAltIDType(CONCERNROLEALTERNATEID.BDM_IA_ACCOUNT_NUMBER);

    final PDCIdentification evidence = this.getEvidence();

    final ValidationResult validator = this.getValidator(evidence);

    validator.evidence().specifyValue(evidence);

    evidence.altIDType().specifyValue(altIDType);

    evidence.alternateID().specifyValue("0123");

    assertTrue(validator.validLengthFailure().getValue());

  }

  /**
   * PASS-IF a validation passes when an IA Account Number is 9-digits long.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void validIAAccountNumberLengthPass()
    throws AppException, InformationalException {

    final CodeTableItem altIDType =
      this.getAltIDType(CONCERNROLEALTERNATEID.BDM_IA_ACCOUNT_NUMBER);

    final PDCIdentification evidence = this.getEvidence();

    final ValidationResult validator = this.getValidator(evidence);

    validator.evidence().specifyValue(evidence);

    evidence.altIDType().specifyValue(altIDType);

    evidence.alternateID().specifyValue("123456789");

    assertFalse(validator.validLengthFailure().getValue());

  }

  /**
   * PASS-IF a validation fails when an OAS Account Number is not 9-digits long.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void validBDMAccountNumberFormatFailure()
    throws AppException, InformationalException {

    final CodeTableItem altIDType =
      this.getAltIDType(CONCERNROLEALTERNATEID.BDM_ACCOUNT_NUMBER);

    final PDCIdentification evidence = this.getEvidence();

    final ValidationResult validator = this.getValidator(evidence);

    validator.evidence().specifyValue(evidence);

    evidence.altIDType().specifyValue(altIDType);

    evidence.alternateID().specifyValue("123456789");

    assertTrue(validator.validFormatFailure().getValue());

  }

  /**
   * PASS-IF a validation passes when an OAS Account Number is 9-digits long
   * starting with 0.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void validBDMAccountNumberFormatPass()
    throws AppException, InformationalException {

    final CodeTableItem altIDType =
      this.getAltIDType(CONCERNROLEALTERNATEID.BDM_ACCOUNT_NUMBER);

    final PDCIdentification evidence = this.getEvidence();

    final ValidationResult validator = this.getValidator(evidence);

    validator.evidence().specifyValue(evidence);

    evidence.altIDType().specifyValue(altIDType);

    evidence.alternateID().specifyValue("023456789");

    assertFalse(validator.validFormatFailure().getValue());

  }

  /**
   * PASS-IF a validation fails when an IA Account Number is not 9-digits long
   * starting with 0.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void validIAAccountNumberFormatFailure()
    throws AppException, InformationalException {

    final CodeTableItem altIDType =
      this.getAltIDType(CONCERNROLEALTERNATEID.BDM_IA_ACCOUNT_NUMBER);

    final PDCIdentification evidence = this.getEvidence();

    final ValidationResult validator = this.getValidator(evidence);

    validator.evidence().specifyValue(evidence);

    evidence.altIDType().specifyValue(altIDType);

    evidence.alternateID().specifyValue("123456789");

    assertTrue(validator.validFormatFailure().getValue());

  }

  /**
   * PASS-IF a validation passes when an IA Account Number is 9-digits long
   * starting with 0.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void validIAAccountNumberFormatPass()
    throws AppException, InformationalException {

    final CodeTableItem altIDType =
      this.getAltIDType(CONCERNROLEALTERNATEID.BDM_IA_ACCOUNT_NUMBER);

    final PDCIdentification evidence = this.getEvidence();

    final ValidationResult validator = this.getValidator(evidence);

    validator.evidence().specifyValue(evidence);

    evidence.altIDType().specifyValue(altIDType);

    evidence.alternateID().specifyValue("023456789");

    assertFalse(validator.validFormatFailure().getValue());

  }

  /**
   * PASS-IF a validation fails when an IA Account Number is not 9-digits long.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void validCIDNLengthFailure()
    throws AppException, InformationalException {

    final CodeTableItem altIDType =
      this.getAltIDType(CONCERNROLEALTERNATEID.BDM_CIDN);

    final PDCIdentification evidence = this.getEvidence();

    final ValidationResult validator = this.getValidator(evidence);

    validator.evidence().specifyValue(evidence);

    evidence.altIDType().specifyValue(altIDType);

    evidence.alternateID().specifyValue("0123");

    assertTrue(validator.validLengthFailure().getValue());

  }

  /**
   * PASS-IF a validation passes when a CIDN is 12-digits long.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void validCIDNLengthPass()
    throws AppException, InformationalException {

    final CodeTableItem altIDType =
      this.getAltIDType(CONCERNROLEALTERNATEID.BDM_CIDN);

    final PDCIdentification evidence = this.getEvidence();

    final ValidationResult validator = this.getValidator(evidence);

    validator.evidence().specifyValue(evidence);

    evidence.altIDType().specifyValue(altIDType);
    evidence.alternateID().specifyValue("123456789012");

    assertFalse(validator.validLengthFailure().getValue());

  }

  /**
   * PASS-IF a validation fails when the alternate ID type is Foreign Identifier
   * and Received From is not Foreign Government.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void receivedFromFINFailure()
    throws AppException, InformationalException {

    final CodeTableItem altIDType =
      this.getAltIDType(CONCERNROLEALTERNATEID.BDM_FOREIGN_IDENTIFIER);

    final CodeTableItem receivedFrom =
      this.getReceivedFrom(BDMRECEIVEDFROM.CLIENT_REPORTED);

    final PDCIdentification evidence = this.getEvidence();
    final ValidationResult validator = this.getValidator(evidence);

    validator.evidence().specifyValue(evidence);
    evidence.altIDType().specifyValue(altIDType);
    evidence.bdmReceivedFrom().specifyValue(receivedFrom);

    // assertTrue(validator.receivedFromFINFailure().getValue());

  }

  /**
   * PASS-IF a validation passes when the alternate ID type is Foreign
   * Identifier and Received From is Foreign Government.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void receivedFromFINPass()
    throws AppException, InformationalException {

    final CodeTableItem altIDType =
      this.getAltIDType(CONCERNROLEALTERNATEID.BDM_FOREIGN_IDENTIFIER);

    final CodeTableItem receivedFrom =
      this.getReceivedFrom(BDMRECEIVEDFROM.FOREIGN_GOVERNMENT);

    final PDCIdentification evidence = this.getEvidence();
    final ValidationResult validator = this.getValidator(evidence);

    validator.evidence().specifyValue(evidence);
    evidence.altIDType().specifyValue(altIDType);
    evidence.bdmReceivedFrom().specifyValue(receivedFrom);

    // assertFalse(validator.receivedFromFINFailure().getValue());

  }

  /**
   * Instantiates and returns the evidence rule object.
   *
   * @return
   */
  private PDCIdentification getEvidence() {

    return PDCIdentification_Factory.getFactory().newInstance(this.session);

  }

  /**
   * Instantiates and returns a validator rule object for the given evidence.
   *
   * @param evidence
   * @return
   */
  private ValidationResult getValidator(final PDCIdentification evidence) {

    final ValidationResult validator =
      ValidationResult_Factory.getFactory().newInstance(session);

    validator.evidence().specifyValue(evidence);

    return validator;

  }

  public CodeTableItem getAltIDType(final String code) {

    return new CodeTableItem(CONCERNROLEALTERNATEID.TABLENAME, code);

  }

  public CodeTableItem getReceivedFrom(final String code) {

    return new CodeTableItem(BDMRECEIVEDFROM.TABLENAME, code);

  }

  // END 57386 DEV: Implement Identification Evidence Changes

}
