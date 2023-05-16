package curam.ca.gc.bdmoas.evidence.foreignincome.impl;

import curam.advisor.facade.fact.AdvisorFactory;
import curam.advisor.facade.struct.AdviceKey;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMMODEOFRECEIPT;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.ca.gc.bdmoas.impl.BDMOASConstants;
import curam.codetable.BDMCURRENCY;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.GENDER;
import curam.codetable.INCOMETYPECODE;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.codetable.impl.EVIDENCECHANGEREASONEntry;
import curam.codetable.impl.INCOMETYPECODEEntry;
import curam.commonintake.entity.struct.ApplicationCaseKey;
import curam.core.facade.fact.PersonFactory;
import curam.core.facade.intf.Person;
import curam.core.facade.struct.CreateIntegratedCaseResult;
import curam.core.facade.struct.PersonRegistrationDetails;
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
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.core.sl.struct.EvidenceDescriptorDetails;
import curam.core.sl.struct.EvidenceTypeKey;
import curam.core.struct.CaseKey;
import curam.core.struct.ConcernRoleKey;
import curam.creole.execution.session.InterpretedRuleObjectFactory;
import curam.creole.execution.session.ManualRecalculation;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.ruleclass.BDMOASForeignIncomeRuleSet.impl.BDMOASForeignIncome;
import curam.creole.ruleclass.BDMOASForeignIncomeRuleSet.impl.BDMOASForeignIncome_Factory;
import curam.creole.ruleclass.BDMOASForeignIncomeSummaryRuleSet.impl.SummaryInformation;
import curam.creole.ruleclass.BDMOASForeignIncomeSummaryRuleSet.impl.SummaryInformation_Factory;
import curam.creole.storage.inmemory.InMemoryDataStorage;
import curam.creole.value.CodeTableItem;
import curam.datastore.impl.NoSuchSchemaException;
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
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * JUnit class for Manage Foreign Income Evidence
 */
public class BDMOASForeignIncomeTest extends BDMOASCaseTest {

  private static final String _1980 = "1980";

  private static final String CASE_PARTICIPANT_ROLE_ID =
    "caseParticipantRoleID";

  private Session session;

  @Override
  protected void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    this.session =
      Session_Factory.getFactory().newInstance(new ManualRecalculation(),
        new InMemoryDataStorage(new InterpretedRuleObjectFactory()));

  }

  public static final String CASEPARTICIPANTROLE = CASE_PARTICIPANT_ROLE_ID;

  public static final String YEAR = "year";

  public static final String INCOME_TYPE = "incomeType";

  public static final String AMOUNT = "amount";

  public static final String CURRENCY = "currency";

  public static final String CANADIAN_AMOUNT = "canadianAmount";

  public static final String OTHER_DESCRIPTION = "otherDescription";

  public static final String COUNTRY = "country";

  private static final String EVD_ATTRIBUTE_DOD = "dateOfDeath";

  private static final String EVD_ATTRIBUTE_DEATH_DATE_NOTIFIED =
    "dateNotifiedOfDeath";

  private static final String EVD_ATTRIBUTE_COUNTRY_OF_DEATH =
    "countryOfDeath";

  /**
   * Before each test, init the tested class and other objects.
   */
  @Before
  public void before() throws NoSuchSchemaException {

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

  /**
   * PASS-IF Exception thrown on if the invalid year value for evidence records
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void foreignIncomeYearValueVaidationTest()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(CASEPARTICIPANTROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(YEAR, "1950");
    attributes.put(INCOME_TYPE, INCOMETYPECODE.ANNUITIESPENSIONS);
    attributes.put(AMOUNT, "100");
    attributes.put(CURRENCY, BDMCURRENCY.NEWZEALANDDOLLAR);
    attributes.put(CANADIAN_AMOUNT, "100");

    // create evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_FOREIGN_INCOME, attributes, getToday());
      });

    final int previousYear =
      Date.getCurrentDate().getCalendar().get(Calendar.YEAR) - 1;

    final String validationMessage =
      "Year must be a whole number between 1967 and " + previousYear + ".";

    assertTrue(validationMessage.equals(exception.getMessage()));

  }

  /**
   * PASS-IF Exception thrown if the other description is not provided if the
   * income type is 'Other'.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void foreignIncomeDescriptionRequiredIfIncomeTypeOtherVaidationTest()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(CASEPARTICIPANTROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(YEAR, "2021");
    attributes.put(INCOME_TYPE, INCOMETYPECODE.OTHER);
    attributes.put(AMOUNT, "100");
    attributes.put(CURRENCY, BDMCURRENCY.NEWZEALANDDOLLAR);
    attributes.put(CANADIAN_AMOUNT, "100");

    // create evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_FOREIGN_INCOME, attributes, getToday());
      });

    final String validationMessage =
      "Description must be entered if Income Type is 'Other'.";

    assertTrue(validationMessage.equals(exception.getMessage()));

  }

  /**
   * PASS-IF Exception thrown if the other description is not 3 charcters long
   * if the
   * income type is 'Other'.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void foreignIncomeDescriptionIsofInvalidLengthVaidationTest()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(CASEPARTICIPANTROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(YEAR, "2021");
    attributes.put(INCOME_TYPE, INCOMETYPECODE.OTHER);
    attributes.put(AMOUNT, "100");
    attributes.put(CURRENCY, BDMCURRENCY.NEWZEALANDDOLLAR);
    attributes.put(CANADIAN_AMOUNT, "100");
    attributes.put(OTHER_DESCRIPTION, "Te");

    // create evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_FOREIGN_INCOME, attributes, getToday());
      });

    final String validationMessage =
      "Description must be at least 3 characters.";

    assertTrue(validationMessage.equals(exception.getMessage()));

  }

  /**
   * PASS-IF Exception thrown if Year is not entered if the Canadian amount is
   * entered.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void
    foreignIncomeYearMustBeEnteredIfCanadianAmountIsPresentVaidationTest()
      throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(CASEPARTICIPANTROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    // attributes.put(YEAR, "2021");
    attributes.put(INCOME_TYPE, INCOMETYPECODE.ASSISTANCEPMTS);
    attributes.put(AMOUNT, "100");
    attributes.put(CURRENCY, BDMCURRENCY.NEWZEALANDDOLLAR);
    attributes.put(CANADIAN_AMOUNT, "100");

    // create evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_FOREIGN_INCOME, attributes, getToday());
      });

    final String validationMessage =
      "Year must be entered if Canadian Dollar Amount is entered.";

    assertTrue(validationMessage.equals(exception.getMessage()));

  }

  /**
   * PASS-IF Exception thrown if Year is not entered if the Canadian amount is
   * entered.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void
    foreignIncomeCanadianDollarAmountIsAMustIfYearPresentVaidationTest()
      throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(CASEPARTICIPANTROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(YEAR, "2021");
    attributes.put(INCOME_TYPE, INCOMETYPECODE.ASSISTANCEPMTS);
    attributes.put(AMOUNT, "100");
    attributes.put(CURRENCY, BDMCURRENCY.NEWZEALANDDOLLAR);
    attributes.put(CANADIAN_AMOUNT, "0");

    // create evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_FOREIGN_INCOME, attributes, getToday());
      });

    final String validationMessage =
      "Canadian Dollar Amount must be entered if Year is entered.";

    assertTrue(validationMessage.equals(exception.getMessage()));

  }

  /**
   * PASS-IF Exception thrown if Foreign Income year before year of birth
   * entered.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void foreignIncomeYearBeforeDateOfBirthTest()
    throws AppException, InformationalException {

    final PersonRegistrationResult person =
      this.registerPersonForValidIncomeYear();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(CASEPARTICIPANTROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(YEAR, "1969");
    attributes.put(INCOME_TYPE, INCOMETYPECODE.ASSISTANCEPMTS);
    attributes.put(AMOUNT, "100");
    attributes.put(CURRENCY, BDMCURRENCY.NEWZEALANDDOLLAR);
    attributes.put(CANADIAN_AMOUNT, "100");

    // create evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_FOREIGN_INCOME, attributes, getToday());
      });

    final String validationMessage =
      "A Foreign Income cannot be before year of birth.";

    assertTrue(validationMessage.equals(exception.getMessage()));

  }

  /**
   * PASS-IF Exception thrown if if Foreign Income year after year of death
   * entered.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void foreignIncomeYearAfterDateOfDeathTest()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPersonDateOfDeath();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(CASEPARTICIPANTROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(YEAR, "2021");
    attributes.put(INCOME_TYPE, INCOMETYPECODE.ASSISTANCEPMTS);
    attributes.put(AMOUNT, "100");
    attributes.put(CURRENCY, BDMCURRENCY.NEWZEALANDDOLLAR);
    attributes.put(CANADIAN_AMOUNT, "100");

    // Modify Date of Death on PDC Birth and Death Evidence
    final HashMap<String, Object> birthAndDeathAttributes =
      new HashMap<String, Object>();
    birthAndDeathAttributes.put(EVD_ATTRIBUTE_DOD,
      Date.fromISO8601("20010102"));
    birthAndDeathAttributes.put(EVD_ATTRIBUTE_DEATH_DATE_NOTIFIED,
      Date.fromISO8601("20010102"));
    birthAndDeathAttributes.put(EVD_ATTRIBUTE_COUNTRY_OF_DEATH,
      new CodeTableItem(BDMMODEOFRECEIPT.TABLENAME, "CA"));
    modifyPDCBirthAndDeathDetailsEvidence(
      person.registrationIDDetails.concernRoleID, birthAndDeathAttributes);

    // create evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_FOREIGN_INCOME, attributes, getToday());
      });

    final String validationMessage =
      "A Foreign Income cannot be after year of death.";

    assertTrue(validationMessage.equals(exception.getMessage()));

  }

  /**
   * Test for summary with mandatory values missing.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void bdmForeignIncomeEvidenceMandatoryValuesMissingSummary() {

    final String summaryMessage = "Mandatory fields missing values";

    final curam.creole.ruleclass.BDMOASForeignIncomeRuleSet.impl.BDMOASForeignIncome bdmoasForeignIncome =
      this.getEvidence();

    bdmoasForeignIncome.amount().specifyValue(Float.valueOf(100));
    bdmoasForeignIncome.caseParticipantRoleID().specifyValue(0);
    bdmoasForeignIncome.incomeType().specifyValue(null);
    bdmoasForeignIncome.currency().specifyValue(null);

    final SummaryInformation summary = this.getSummary(bdmoasForeignIncome);

    assertTrue(summary.summary().getValue().toLocale(Locale.ENGLISH)
      .equals(summaryMessage));
  }

  /**
   * Test for summary with Canadian Dollar Amount Entered
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void bdmForeignIncomeEvidenceCanadianDollarAmountSpecifiedSummary() {

    final String summaryMessage =
      "Wages and Salaries for 2021 is $100 Canadian Dollar";

    final curam.creole.ruleclass.BDMOASForeignIncomeRuleSet.impl.BDMOASForeignIncome bdmoasForeignIncome =
      this.getEvidence();

    bdmoasForeignIncome.amount().specifyValue(Float.valueOf(100));
    bdmoasForeignIncome.canadianAmount().specifyValue(Float.valueOf(100));
    bdmoasForeignIncome.caseParticipantRoleID().specifyValue(100000);
    bdmoasForeignIncome.incomeType()
      .specifyValue(new CodeTableItem("IncomeTypeCode", "IT1"));
    bdmoasForeignIncome.currency()
      .specifyValue(new CodeTableItem("BDMCurrency", "ADP"));
    bdmoasForeignIncome.year().specifyValue("2021");

    final SummaryInformation summary = this.getSummary(bdmoasForeignIncome);

    assertTrue(summary.summary().getValue().toLocale(Locale.ENGLISH)
      .equals(summaryMessage));
  }

  /**
   * Test for summary with no year and no Canadian Dollar Amount Entered
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void
    bdmForeignIncomeEvidenceNoCanadianDollarAmountAndNoYearSpecifiedSummary() {

    final String summaryMessage = "Wages and Salaries is 100";

    final curam.creole.ruleclass.BDMOASForeignIncomeRuleSet.impl.BDMOASForeignIncome bdmoasForeignIncome =
      this.getEvidence();

    bdmoasForeignIncome.amount().specifyValue(Float.valueOf(100));
    bdmoasForeignIncome.caseParticipantRoleID().specifyValue(100000);
    bdmoasForeignIncome.incomeType()
      .specifyValue(new CodeTableItem("IncomeTypeCode", "IT1"));
    bdmoasForeignIncome.currency()
      .specifyValue(new CodeTableItem("BDMCurrency", "ADP"));
    bdmoasForeignIncome.canadianAmount().specifyValue(Float.valueOf(0));

    final SummaryInformation summary = this.getSummary(bdmoasForeignIncome);

    assertTrue(summary.summary().getValue().toLocale(Locale.ENGLISH)
      .equals(summaryMessage));
  }

  /**
   * Instantiates and returns a summary rule object for the given evidence.
   *
   * @param evidence
   * @return
   */
  private SummaryInformation getSummary(final BDMOASForeignIncome evidence) {

    final SummaryInformation summary =
      SummaryInformation_Factory.getFactory().newInstance(this.session);
    summary.evidence().specifyValue(evidence);

    return summary;

  }

  /**
   * Return the evidence for the session
   *
   * @return
   */
  private BDMOASForeignIncome getEvidence() {

    return BDMOASForeignIncome_Factory.getFactory().newInstance(this.session);

  }

  /**
   * Registers a person for test purposes.
   *
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public PersonRegistrationResult registerPersonForValidIncomeYear()
    throws AppException, InformationalException {

    final Person person = PersonFactory.newInstance();
    final PersonRegistrationDetails personDtls =
      new PersonRegistrationDetails();

    personDtls.personRegistrationDetails.firstForename = "Testington";
    personDtls.personRegistrationDetails.surname = "Bear";
    personDtls.personRegistrationDetails.dateOfBirth =
      Date.fromISO8601("19700101");
    personDtls.personRegistrationDetails.sex = GENDER.MALE;
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
   * Registers a person for test purposes.
   *
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public PersonRegistrationResult registerPersonDateOfDeath()
    throws AppException, InformationalException {

    final Person person = PersonFactory.newInstance();
    final PersonRegistrationDetails personDtls =
      new PersonRegistrationDetails();

    personDtls.personRegistrationDetails.firstForename = "Testington";
    personDtls.personRegistrationDetails.surname = "Bear";
    personDtls.personRegistrationDetails.dateOfBirth =
      Date.fromISO8601("19700101");
    personDtls.personRegistrationDetails.sex = GENDER.MALE;
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

  /**
   * PASS-IF when amount and type are provided, no issue gets raised.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void foreignIncomeAdvisorTest()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final ApplicationCaseKey applicationCase = this.createApplicationCase(
      person.registrationIDDetails.concernRoleID, 920000);
    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(applicationCase.applicationCaseID,
        person.registrationIDDetails.concernRoleID);

    createForeignIncomeEvidence(applicationCase.applicationCaseID,
      cprObj.caseParticipantRoleID,
      person.registrationIDDetails.concernRoleID,
      INCOMETYPECODEEntry.INKINDINCOME);

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

  private curam.core.sl.struct.EvidenceKey createForeignIncomeEvidence(
    final Long caseID, final Long cprID, final long concernroleID,
    final INCOMETYPECODEEntry incomeTypeEntry)
    throws AppException, InformationalException {

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.OAS_FOREIGN_INCOME;

    // get Latest Version of Evidence
    final GenericSLDataDetails genericDtls = new GenericSLDataDetails();
    final EvidenceServiceInterface evidenceServiceInterface =
      EvidenceGenericSLFactory.instance(eType, Date.getCurrentDate());

    final DynamicEvidenceDataDetails dynamicEvidencedataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(eType.evidenceType,
        Date.getCurrentDate());

    // assigning all the required attributes
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(CASEPARTICIPANTROLE), cprID);

    DynamicEvidenceTypeConverter
      .setAttribute(dynamicEvidencedataDetails.getAttribute(YEAR), _1980);

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(INCOME_TYPE),
      new CodeTableItem(INCOMETYPECODE.TABLENAME, incomeTypeEntry.getCode()));

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(AMOUNT),
      Double.valueOf(100.00));

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(CURRENCY),
      new CodeTableItem(BDMCURRENCY.TABLENAME, BDMCURRENCY.UKPOUND));

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(CANADIAN_AMOUNT),
      Double.valueOf(100.00));

    // set descriptor attributes to call OOTB Evidence creation logic
    final EvidenceDescriptorDetails descriptor =
      new EvidenceDescriptorDetails();
    descriptor.evidenceType = eType.evidenceType;
    descriptor.caseID = caseID;
    descriptor.receivedDate = Date.getCurrentDate();
    descriptor.participantID = concernroleID;

    genericDtls.setData(dynamicEvidencedataDetails);
    genericDtls.setDescriptor(descriptor);
    genericDtls.setCaseIdKey(descriptor.caseID);

    final CpDetailsAdaptor cpAdaptor = new CpDetailsAdaptor();
    cpAdaptor.setCaseParticipantRoleID(cprID);
    genericDtls.addRelCp(CASE_PARTICIPANT_ROLE_ID, cpAdaptor);

    return evidenceServiceInterface.createEvidence(genericDtls).evidenceKey;
  }

}
