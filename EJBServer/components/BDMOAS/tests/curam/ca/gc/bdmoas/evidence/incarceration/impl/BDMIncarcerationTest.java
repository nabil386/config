package curam.ca.gc.bdmoas.evidence.incarceration.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMOFFENDERSTATUS;
import curam.ca.gc.bdm.message.BDMEVIDENCE;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.ca.gc.bdmoas.test.data.impl.BDMOASServiceSupplierTestData;
import curam.ca.gc.bdmoas.test.data.impl.BDMOASServiceSupplierTestDataDetails;
import curam.codetable.CASEPARTICIPANTROLETYPE;
import curam.codetable.RECORDSTATUS;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.facade.struct.CreateIntegratedCaseResult;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.facade.struct.ReadServiceSupplierHomeKey;
import curam.core.sl.entity.fact.CaseParticipantRoleFactory;
import curam.core.sl.entity.struct.CaseParticipantRoleDtls;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.struct.CaseKey;
import curam.creole.execution.session.InterpretedRuleObjectFactory;
import curam.creole.execution.session.ManualRecalculation;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.ruleclass.BDMIncarcerationRuleSet.impl.BDMIncarceration;
import curam.creole.ruleclass.BDMIncarcerationRuleSet.impl.BDMIncarceration_Factory;
import curam.creole.ruleclass.BDMIncarcerationValidationRuleSet.impl.ValidationResult;
import curam.creole.ruleclass.BDMIncarcerationValidationRuleSet.impl.ValidationResult_Factory;
import curam.creole.storage.inmemory.InMemoryDataStorage;
import curam.creole.value.CodeTableItem;
import curam.testframework.CuramServerTestJUnit4;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

// Task DEV: Implement Manage Incarceration Evidence
/**
 * Test configured rules-based validations for Incarceration evidence.
 */
public class BDMIncarcerationTest extends CuramServerTestJUnit4 {

  private Session session;

  final BDMOASCaseTest bdmOASCaseTest = new BDMOASCaseTest();

  private static final String CASEPARTICIPANT_ROLE = "caseParticipantRole";

  private static final String OFFENDER_STATUS = "offenderStatus";

  private static final String INSTITUTION = "institution";

  private static final String OFFENDER_ID = "offenderID";

  private static final String START_DATE = "startDate";

  private static final String END_DATE = "endDate";

  @Override
  public void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    this.session =
      Session_Factory.getFactory().newInstance(new ManualRecalculation(),
        new InMemoryDataStorage(new InterpretedRuleObjectFactory()));

  }

  @Test
  public void offenderStatusSentenceCompletedOrSupervised()
    throws AppException, InformationalException {

    final BDMIncarceration evidence = this.getEvidence();
    BDMIncarceration_Factory.getFactory().newInstance(this.session);

    final ValidationResult validator = this.getValidator(evidence);

    validator.evidence().specifyValue(evidence);

    final CodeTableItem sentenceCompleted =
      this.getOffenderStatus(BDMOFFENDERSTATUS.SENTENCE_COMPLETED);

    evidence.offenderStatus().specifyValue(sentenceCompleted);

    assertTrue(
      validator.isOffenderStatusSentenceCompletedOrSupervised().getValue());

  }

  /**
   * Institution is mandatory
   *
   * @throws AppException
   * @throws InformationalException
   */

  @Test
  public void mandatoryIncarcerationInstitution()
    throws AppException, InformationalException {

    BDMIncarceration_Factory.getFactory().newInstance(this.session);

    final PersonRegistrationResult personRegistrationResult =
      this.registerPerson();

    final CreateIntegratedCaseResult integratedCase =
      this.createOASIntegratedCase(personRegistrationResult);

    final BDMUtil util = new BDMUtil();
    final curam.core.sl.struct.CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        personRegistrationResult.registrationIDDetails.concernRoleID);

    final ResourceBundle resourceBundle = ResourceBundle.getBundle(
      "curam.ca.gc.bdm.evidence.BDMIncarcerationValidationRuleSet");

    final String validationMessage =
      resourceBundle.getString("ERR_INCARCERATION_INSTITUTION_IS_MANDATORY");

    // Evidence Details
    final Map<String, String> attributes = new HashMap<String, String>();

    attributes.put(OFFENDER_STATUS, BDMOFFENDERSTATUS.INCARCERATED);
    attributes.put(OFFENDER_ID, "123");
    attributes.put(START_DATE, "20220818");
    attributes.put(CASEPARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));

    // create evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        bdmOASCaseTest.createEvidence(integratedCase.integratedCaseID,
          personRegistrationResult.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.BDMINCARCERATION, attributes, getToday());
      });

    assertTrue(validationMessage.equals(exception.getMessage()));

  }

  /**
   * CSC Release Date can only be entered if the Offender Status is ‘Sentence
   * Completed’ or ‘Supervised’.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void cscReleaseDateWithOffenderStatusValidation()
    throws AppException, InformationalException {

    BDMIncarceration_Factory.getFactory().newInstance(this.session);

    final PersonRegistrationResult personRegistrationResult =
      this.registerPerson();

    final CreateIntegratedCaseResult integratedCase =
      this.createOASIntegratedCase(personRegistrationResult);

    final BDMUtil util = new BDMUtil();
    final curam.core.sl.struct.CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        personRegistrationResult.registrationIDDetails.concernRoleID);

    final ResourceBundle resourceBundle = ResourceBundle.getBundle(
      "curam.ca.gc.bdm.evidence.BDMIncarcerationValidationRuleSet");

    final String validationMessage =
      resourceBundle.getString("ERR_OFFENDER_STATUS_NOT_VALID");

    final BDMOASServiceSupplierTestData servSuppTestDateObj =
      new BDMOASServiceSupplierTestData();

    final BDMOASServiceSupplierTestDataDetails servSuppTestDataDetails =
      servSuppTestDateObj.setRegisterServiceSupplierDetails();

    final ReadServiceSupplierHomeKey key = new ReadServiceSupplierHomeKey();
    key.concernRoleHomePageKey.concernRoleID =
      servSuppTestDataDetails.servSuppCRoleID;

    final CaseParticipantRoleDtls caseParticipantRoleDtls =
      new CaseParticipantRoleDtls();
    caseParticipantRoleDtls.caseID = integratedCase.integratedCaseID;
    caseParticipantRoleDtls.fromDate = Date.getCurrentDate();
    caseParticipantRoleDtls.typeCode = CASEPARTICIPANTROLETYPE.BDMINST;
    caseParticipantRoleDtls.participantRoleID =
      servSuppTestDataDetails.servSuppCRoleID;
    caseParticipantRoleDtls.recordStatus = RECORDSTATUS.DEFAULTCODE;
    CaseParticipantRoleFactory.newInstance().insert(caseParticipantRoleDtls);

    // Evidence Details
    final Map<String, String> attributes = new HashMap<String, String>();
    final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("yyyyMMdd");
    final LocalDate date = LocalDate.now();
    final String startDate = formatter.format(date);
    final String endDate = formatter.format(date.plusDays(10));

    attributes.put(OFFENDER_STATUS, BDMOFFENDERSTATUS.INCARCERATED);
    attributes.put(OFFENDER_ID, "123");
    attributes.put(START_DATE, startDate);
    attributes.put(END_DATE, endDate);
    attributes.put(CASEPARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));

    attributes.put(INSTITUTION,
      String.valueOf(caseParticipantRoleDtls.caseParticipantRoleID));

    // create evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        bdmOASCaseTest.createEvidence(integratedCase.integratedCaseID,
          personRegistrationResult.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.BDMINCARCERATION, attributes, getToday());
      });

    assertTrue(validationMessage.equals(exception.getMessage()));

  }

  /**
   * ‘Sentence Completed’ or ‘Supervised’ may only be selected if a CSC Release
   * Date is entered.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void OffenderStatusWithCSCReleaseDateWithValidation()
    throws AppException, InformationalException {

    BDMIncarceration_Factory.getFactory().newInstance(this.session);

    final PersonRegistrationResult personRegistrationResult =
      this.registerPerson();

    final CreateIntegratedCaseResult integratedCase =
      this.createOASIntegratedCase(personRegistrationResult);

    final BDMUtil util = new BDMUtil();
    final curam.core.sl.struct.CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        personRegistrationResult.registrationIDDetails.concernRoleID);

    final ResourceBundle resourceBundle = ResourceBundle.getBundle(
      "curam.ca.gc.bdm.evidence.BDMIncarcerationValidationRuleSet");

    final String validationMessage = resourceBundle
      .getString("ERR_CSC_RELEASE_DATE_EMPTY_FOR_VALID_OFFENDER_STATUS");

    final BDMOASServiceSupplierTestData servSuppTestDateObj =
      new BDMOASServiceSupplierTestData();

    final BDMOASServiceSupplierTestDataDetails servSuppTestDataDetails =
      servSuppTestDateObj.setRegisterServiceSupplierDetails();

    final ReadServiceSupplierHomeKey key = new ReadServiceSupplierHomeKey();
    key.concernRoleHomePageKey.concernRoleID =
      servSuppTestDataDetails.servSuppCRoleID;

    final CaseParticipantRoleDtls caseParticipantRoleDtls =
      new CaseParticipantRoleDtls();
    caseParticipantRoleDtls.caseID = integratedCase.integratedCaseID;
    caseParticipantRoleDtls.fromDate = Date.getCurrentDate();
    caseParticipantRoleDtls.typeCode = CASEPARTICIPANTROLETYPE.BDMINST;
    caseParticipantRoleDtls.participantRoleID =
      servSuppTestDataDetails.servSuppCRoleID;
    caseParticipantRoleDtls.recordStatus = RECORDSTATUS.DEFAULTCODE;
    CaseParticipantRoleFactory.newInstance().insert(caseParticipantRoleDtls);

    // Evidence Details
    final Map<String, String> attributes = new HashMap<String, String>();
    final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("yyyyMMdd");
    final LocalDate date = LocalDate.now();
    final String startDate = formatter.format(date);

    attributes.put(OFFENDER_STATUS, BDMOFFENDERSTATUS.SUPERVISED);
    attributes.put(OFFENDER_ID, "123");
    attributes.put(START_DATE, startDate);
    attributes.put(CASEPARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));

    attributes.put(INSTITUTION,
      String.valueOf(caseParticipantRoleDtls.caseParticipantRoleID));

    // create evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        bdmOASCaseTest.createEvidence(integratedCase.integratedCaseID,
          personRegistrationResult.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.BDMINCARCERATION, attributes, getToday());
      });

    assertTrue(validationMessage.equals(exception.getMessage()));

  }

  /**
   * Offender Identification Number can only contain letters and numbers.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void offenderIdentificationValidation()
    throws AppException, InformationalException {

    BDMIncarceration_Factory.getFactory().newInstance(this.session);

    final PersonRegistrationResult personRegistrationResult =
      this.registerPerson();

    final CreateIntegratedCaseResult integratedCase =
      this.createOASIntegratedCase(personRegistrationResult);

    final BDMUtil util = new BDMUtil();
    final curam.core.sl.struct.CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        personRegistrationResult.registrationIDDetails.concernRoleID);

    final ResourceBundle resourceBundle = ResourceBundle.getBundle(
      "curam.ca.gc.bdm.evidence.BDMIncarcerationValidationRuleSet");

    final String validationMessage = resourceBundle
      .getString("ERR_OFFENDER_ID_CONTAINS_NONALPHANUMERIC_CHARACTERS");

    final BDMOASServiceSupplierTestData servSuppTestDateObj =
      new BDMOASServiceSupplierTestData();

    final BDMOASServiceSupplierTestDataDetails servSuppTestDataDetails =
      servSuppTestDateObj.setRegisterServiceSupplierDetails();

    final ReadServiceSupplierHomeKey key = new ReadServiceSupplierHomeKey();
    key.concernRoleHomePageKey.concernRoleID =
      servSuppTestDataDetails.servSuppCRoleID;

    final CaseParticipantRoleDtls caseParticipantRoleDtls =
      new CaseParticipantRoleDtls();
    caseParticipantRoleDtls.caseID = integratedCase.integratedCaseID;
    caseParticipantRoleDtls.fromDate = Date.getCurrentDate();
    caseParticipantRoleDtls.typeCode = CASEPARTICIPANTROLETYPE.BDMINST;
    caseParticipantRoleDtls.participantRoleID =
      servSuppTestDataDetails.servSuppCRoleID;
    caseParticipantRoleDtls.recordStatus = RECORDSTATUS.DEFAULTCODE;
    CaseParticipantRoleFactory.newInstance().insert(caseParticipantRoleDtls);

    // Evidence Details
    final Map<String, String> attributes = new HashMap<String, String>();
    final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("yyyyMMdd");
    final LocalDate date = LocalDate.now();
    final String startDate = formatter.format(date);
    final String endDate = formatter.format(date.plusDays(10));
    attributes.put(OFFENDER_STATUS, BDMOFFENDERSTATUS.SUPERVISED);
    attributes.put(OFFENDER_ID, "123!");
    attributes.put(START_DATE, startDate);
    attributes.put(END_DATE, endDate);
    attributes.put(CASEPARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));

    attributes.put(INSTITUTION,
      String.valueOf(caseParticipantRoleDtls.caseParticipantRoleID));

    // create evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        bdmOASCaseTest.createEvidence(integratedCase.integratedCaseID,
          personRegistrationResult.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.BDMINCARCERATION, attributes, getToday());
      });

    assertTrue(validationMessage.equals(exception.getMessage()));

  }

  /**
   * The selected Institution cannot be used as it was not open for the entire
   * period of incarceration.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void invalidInstitutionValidation()
    throws AppException, InformationalException {

    BDMIncarceration_Factory.getFactory().newInstance(this.session);

    final PersonRegistrationResult personRegistrationResult =
      this.registerPerson();

    final CreateIntegratedCaseResult integratedCase =
      this.createOASIntegratedCase(personRegistrationResult);

    final BDMUtil util = new BDMUtil();
    final curam.core.sl.struct.CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        personRegistrationResult.registrationIDDetails.concernRoleID);

    final ResourceBundle resourceBundle = ResourceBundle.getBundle(
      "curam.ca.gc.bdm.evidence.BDMIncarcerationValidationRuleSet");

    final String validationMessage =
      resourceBundle.getString("ERR_INVALID_INCARCERATION_INSTITUTION");

    final BDMOASServiceSupplierTestData servSuppTestDateObj =
      new BDMOASServiceSupplierTestData();

    final BDMOASServiceSupplierTestDataDetails servSuppTestDataDetails =
      servSuppTestDateObj.setRegisterServiceSupplierDetails();

    final ReadServiceSupplierHomeKey key = new ReadServiceSupplierHomeKey();
    key.concernRoleHomePageKey.concernRoleID =
      servSuppTestDataDetails.servSuppCRoleID;

    final CaseParticipantRoleDtls caseParticipantRoleDtls =
      new CaseParticipantRoleDtls();
    caseParticipantRoleDtls.caseID = integratedCase.integratedCaseID;
    caseParticipantRoleDtls.fromDate = Date.getCurrentDate();
    caseParticipantRoleDtls.typeCode = CASEPARTICIPANTROLETYPE.BDMINST;
    caseParticipantRoleDtls.participantRoleID =
      servSuppTestDataDetails.servSuppCRoleID;
    caseParticipantRoleDtls.recordStatus = RECORDSTATUS.DEFAULTCODE;
    CaseParticipantRoleFactory.newInstance().insert(caseParticipantRoleDtls);

    // Evidence Details
    final Map<String, String> attributes = new HashMap<String, String>();
    final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("yyyyMMdd");
    final LocalDate date = LocalDate.now();
    final String startDate = formatter.format(date.minusDays(6));
    final String endDate = formatter.format(date.plusDays(10));
    attributes.put(OFFENDER_STATUS, BDMOFFENDERSTATUS.SUPERVISED);
    attributes.put(OFFENDER_ID, "123");
    attributes.put(START_DATE, startDate);
    attributes.put(END_DATE, endDate);
    attributes.put(CASEPARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));

    attributes.put(INSTITUTION,
      String.valueOf(caseParticipantRoleDtls.caseParticipantRoleID));

    // create evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        bdmOASCaseTest.createEvidence(integratedCase.integratedCaseID,
          personRegistrationResult.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.BDMINCARCERATION, attributes, getToday());
      });

    assertTrue(validationMessage.equals(exception.getMessage()));

  }

  /**
   * The Admission Date cannot be after CSC Release Date.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void admissionDateAndCSCReleaseDateValidation()
    throws AppException, InformationalException {

    BDMIncarceration_Factory.getFactory().newInstance(this.session);

    final PersonRegistrationResult personRegistrationResult =
      this.registerPerson();

    final CreateIntegratedCaseResult integratedCase =
      this.createOASIntegratedCase(personRegistrationResult);

    final BDMUtil util = new BDMUtil();
    final curam.core.sl.struct.CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        personRegistrationResult.registrationIDDetails.concernRoleID);

    final String validationMessage =
      "The Admission Date cannot be after Release Date.";

    final BDMOASServiceSupplierTestData servSuppTestDateObj =
      new BDMOASServiceSupplierTestData();

    final BDMOASServiceSupplierTestDataDetails servSuppTestDataDetails =
      servSuppTestDateObj.setRegisterServiceSupplierDetails();

    final ReadServiceSupplierHomeKey key = new ReadServiceSupplierHomeKey();
    key.concernRoleHomePageKey.concernRoleID =
      servSuppTestDataDetails.servSuppCRoleID;

    final CaseParticipantRoleDtls caseParticipantRoleDtls =
      new CaseParticipantRoleDtls();
    caseParticipantRoleDtls.caseID = integratedCase.integratedCaseID;
    caseParticipantRoleDtls.fromDate = Date.getCurrentDate();
    caseParticipantRoleDtls.typeCode = CASEPARTICIPANTROLETYPE.BDMINST;
    caseParticipantRoleDtls.participantRoleID =
      servSuppTestDataDetails.servSuppCRoleID;
    caseParticipantRoleDtls.recordStatus = RECORDSTATUS.DEFAULTCODE;
    CaseParticipantRoleFactory.newInstance().insert(caseParticipantRoleDtls);

    // Evidence Details
    final Map<String, String> attributes = new HashMap<String, String>();
    final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("yyyyMMdd");
    final LocalDate date = LocalDate.now();
    final String startDate = formatter.format(date);
    final String endDate = formatter.format(date.minusDays(10));
    attributes.put(OFFENDER_STATUS, BDMOFFENDERSTATUS.SUPERVISED);
    attributes.put(OFFENDER_ID, "123");
    attributes.put(START_DATE, startDate);
    attributes.put(END_DATE, endDate);
    attributes.put(CASEPARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));

    attributes.put(INSTITUTION,
      String.valueOf(caseParticipantRoleDtls.caseParticipantRoleID));

    // create evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        bdmOASCaseTest.createEvidence(integratedCase.integratedCaseID,
          personRegistrationResult.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.BDMINCARCERATION, attributes, getToday());
      });

    assertTrue(validationMessage.equals(exception.getMessage()));

  }

  /**
   * The Admission Date cannot be after CSC Release Date.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void offenderStatusDeceasedAndDateOfDeath()
    throws AppException, InformationalException {

    BDMIncarceration_Factory.getFactory().newInstance(this.session);

    final PersonRegistrationResult personRegistrationResult =
      this.registerPerson();

    final CreateIntegratedCaseResult integratedCase =
      this.createOASIntegratedCase(personRegistrationResult);

    final BDMUtil util = new BDMUtil();
    final curam.core.sl.struct.CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        personRegistrationResult.registrationIDDetails.concernRoleID);

    final String validationMessage =
      BDMEVIDENCE.ERR_DATE_OF_DEATH_REQUIRED_FOR_OFFENDER_STATUS_DECEASED
        .toString();

    final BDMOASServiceSupplierTestData servSuppTestDateObj =
      new BDMOASServiceSupplierTestData();

    final BDMOASServiceSupplierTestDataDetails servSuppTestDataDetails =
      servSuppTestDateObj.setRegisterServiceSupplierDetails();

    final ReadServiceSupplierHomeKey key = new ReadServiceSupplierHomeKey();
    key.concernRoleHomePageKey.concernRoleID =
      servSuppTestDataDetails.servSuppCRoleID;

    final CaseParticipantRoleDtls caseParticipantRoleDtls =
      new CaseParticipantRoleDtls();
    caseParticipantRoleDtls.caseID = integratedCase.integratedCaseID;
    caseParticipantRoleDtls.fromDate = Date.getCurrentDate();
    caseParticipantRoleDtls.typeCode = CASEPARTICIPANTROLETYPE.BDMINST;
    caseParticipantRoleDtls.participantRoleID =
      servSuppTestDataDetails.servSuppCRoleID;
    caseParticipantRoleDtls.recordStatus = RECORDSTATUS.DEFAULTCODE;
    CaseParticipantRoleFactory.newInstance().insert(caseParticipantRoleDtls);

    // Evidence Details
    final Map<String, String> attributes = new HashMap<String, String>();
    final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("yyyyMMdd");
    final LocalDate date = LocalDate.now();
    final String startDate = formatter.format(date);
    attributes.put(OFFENDER_STATUS, BDMOFFENDERSTATUS.DECEASED);
    attributes.put(OFFENDER_ID, "123");
    attributes.put(START_DATE, startDate);
    attributes.put(CASEPARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(INSTITUTION,
      String.valueOf(caseParticipantRoleDtls.caseParticipantRoleID));

    bdmOASCaseTest.createEvidence(integratedCase.integratedCaseID,
      personRegistrationResult.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.BDMINCARCERATION, attributes, getToday());

    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCase.integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final Exception exception =
      Assert.assertThrows(AppException.class, () -> {
        evidenceController.applyAllChanges(caseKey);
      });

    assertTrue(validationMessage.equals(exception.getMessage()));

  }

  /**
   * The Admission Date cannot be after CSC Release Date.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void overlapingIncarcerationEvidenceValidation()
    throws AppException, InformationalException {

    BDMIncarceration_Factory.getFactory().newInstance(this.session);

    final PersonRegistrationResult personRegistrationResult =
      this.registerPerson();

    final CreateIntegratedCaseResult integratedCase =
      this.createOASIntegratedCase(personRegistrationResult);

    final BDMUtil util = new BDMUtil();
    final curam.core.sl.struct.CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        personRegistrationResult.registrationIDDetails.concernRoleID);

    final String validationMessage =
      BDMEVIDENCE.ERR_INCARCERATION_EVIDENCE_OVERLAP.toString();

    final BDMOASServiceSupplierTestData servSuppTestDateObj =
      new BDMOASServiceSupplierTestData();

    final BDMOASServiceSupplierTestDataDetails servSuppTestDataDetails =
      servSuppTestDateObj.setRegisterServiceSupplierDetails();

    final ReadServiceSupplierHomeKey key = new ReadServiceSupplierHomeKey();
    key.concernRoleHomePageKey.concernRoleID =
      servSuppTestDataDetails.servSuppCRoleID;

    final CaseParticipantRoleDtls caseParticipantRoleDtls =
      new CaseParticipantRoleDtls();
    caseParticipantRoleDtls.caseID = integratedCase.integratedCaseID;
    caseParticipantRoleDtls.fromDate = Date.getCurrentDate();
    caseParticipantRoleDtls.typeCode = CASEPARTICIPANTROLETYPE.BDMINST;
    caseParticipantRoleDtls.participantRoleID =
      servSuppTestDataDetails.servSuppCRoleID;
    caseParticipantRoleDtls.recordStatus = RECORDSTATUS.DEFAULTCODE;
    CaseParticipantRoleFactory.newInstance().insert(caseParticipantRoleDtls);

    // Evidence Details
    final Map<String, String> attributes = new HashMap<String, String>();
    final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("yyyyMMdd");
    final LocalDate date = LocalDate.now();
    final String startDate = formatter.format(date);
    attributes.put(OFFENDER_STATUS, BDMOFFENDERSTATUS.INCARCERATED);
    attributes.put(OFFENDER_ID, "123");
    attributes.put(START_DATE, startDate);
    attributes.put(CASEPARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(INSTITUTION,
      String.valueOf(caseParticipantRoleDtls.caseParticipantRoleID));

    bdmOASCaseTest.createEvidence(integratedCase.integratedCaseID,
      personRegistrationResult.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.BDMINCARCERATION, attributes, getToday());

    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCase.integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    evidenceController.applyAllChanges(caseKey);

    // Evidence Details
    final Map<String, String> newEVAttributes = new HashMap<String, String>();
    newEVAttributes.put(OFFENDER_STATUS, BDMOFFENDERSTATUS.INCARCERATED);
    newEVAttributes.put(OFFENDER_ID, "123");
    newEVAttributes.put(START_DATE, startDate);
    newEVAttributes.put(CASEPARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    newEVAttributes.put(INSTITUTION,
      String.valueOf(caseParticipantRoleDtls.caseParticipantRoleID));

    bdmOASCaseTest.createEvidence(integratedCase.integratedCaseID,
      personRegistrationResult.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.BDMINCARCERATION, attributes, getToday());

    final Exception exception =
      Assert.assertThrows(AppException.class, () -> {
        evidenceController.applyAllChanges(caseKey);
      });

    assertTrue(validationMessage.equals(exception.getMessage()));

  }

  /**
   * Instantiates and returns the evidence rule object.
   *
   * @return
   */
  private BDMIncarceration getEvidence() {

    return BDMIncarceration_Factory.getFactory().newInstance(this.session);

  }

  /**
   * Instatiates and returns a validator rule object for the given evidence.
   *
   * @param evidence
   * @return
   */
  private ValidationResult getValidator(final BDMIncarceration evidence) {

    final ValidationResult validator =
      ValidationResult_Factory.getFactory().newInstance(session);

    validator.evidence().specifyValue(evidence);

    return validator;

  }

  /**
   * Returns the offender status code value
   *
   * @param code
   * @return
   */
  public CodeTableItem getOffenderStatus(final String code) {

    return new CodeTableItem(BDMOFFENDERSTATUS.TABLENAME, code);

  }

  public PersonRegistrationResult registerPerson()
    throws AppException, InformationalException {

    return bdmOASCaseTest.registerPerson();
  }

  /**
   * This method creates the OAS IC case
   *
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public CreateIntegratedCaseResult createOASIntegratedCase(
    final PersonRegistrationResult personRegistrationResult)
    throws AppException, InformationalException {

    final CreateIntegratedCaseResult createIntegratedCaseResult =
      bdmOASCaseTest.createIntegratedCase(
        personRegistrationResult.registrationIDDetails.concernRoleID);

    return createIntegratedCaseResult;

  }

  @Override
  protected boolean shouldCommit() {

    return false;
  }

}
