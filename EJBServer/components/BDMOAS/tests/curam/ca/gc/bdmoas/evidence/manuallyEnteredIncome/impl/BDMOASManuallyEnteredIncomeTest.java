package curam.ca.gc.bdmoas.evidence.manuallyEnteredIncome.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMMODEOFRECEIPT;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.codetable.GENDER;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.codetable.impl.EVIDENCECHANGEREASONEntry;
import curam.core.facade.fact.PersonFactory;
import curam.core.facade.intf.Person;
import curam.core.facade.struct.CreateIntegratedCaseResult;
import curam.core.facade.struct.PersonRegistrationDetails;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.impl.CuramConst;
import curam.core.sl.fact.AlternateNameFactory;
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
import curam.core.sl.intf.AlternateName;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.core.sl.struct.NameAndAgeStruct;
import curam.core.struct.CaseKey;
import curam.core.struct.ConcernRoleKey;
import curam.creole.execution.session.InterpretedRuleObjectFactory;
import curam.creole.execution.session.ManualRecalculation;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.ruleclass.BDMOASManuallyEnteredIncomeRuleSet.impl.BDMOASManuallyEnteredIncome;
import curam.creole.ruleclass.BDMOASManuallyEnteredIncomeRuleSet.impl.BDMOASManuallyEnteredIncome_Factory;
import curam.creole.ruleclass.BDMOASManuallyEnteredIncomeSummaryRuleSet.impl.SummaryInformation;
import curam.creole.ruleclass.BDMOASManuallyEnteredIncomeSummaryRuleSet.impl.SummaryInformation_Factory;
import curam.creole.storage.inmemory.InMemoryDataStorage;
import curam.creole.value.CodeTableItem;
import curam.dynamicevidence.impl.DynamicEvidenceDataAttributeDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.pdc.fact.PDCUtilFactory;
import curam.pdc.impl.PDCConst;
import curam.pdc.intf.PDCUtil;
import curam.pdc.struct.PDCCaseIDCaseParticipantRoleID;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

// 74615 DEV: Implement Manually Entered Income
/**
 * Test configured for Manually Entered Income
 */
public class BDMOASManuallyEnteredIncomeTest extends BDMOASCaseTest {

  private Session session;

  public static final String CANADAPENSIONPLANORQUEBECPENSIONBENEFIT =
    "canadaPensionPlanOrQuebecPensionBenefit";

  public static final String NETSELFEMPLOYMENT = "netSelfEmploymentIncome";

  public static final String OTHERCANADIANPENSIONINCOME =
    "otherCanadianPensionIncome";

  public static final String EMPLOYMENTINSURANCE = "employmentInsurance";

  public static final String INTERESTANDOTHERINVESTMENTINCOME =
    "interestAndOtherInvestmentIncome";

  public static final String OTHERINCOME = "otherIncome";

  public static final String NETRENTALINCOME = "netRentalIncome";

  public static final String FOREIGNPENSIONINCOME = "foreignPensionIncome";

  public static final String WORKERSCOMPENSATIONBENEFITS =
    "workersCompensationBenefits";

  public static final String ELIGIBLEANDOTHETHANELIGIBLEDIVIDENDS =
    "eligibleAndOtherThanEligibleDividends";

  public static final String NETEMPLOYMETINCOME = "netEmploymentIncome";

  public static final String CAPITALGAINS = "capitalGains";

  public static final String PREFERRED = "preferred";

  public static final String YEAR = "year";

  public static final String CASEPARTICIPANTROLE = "caseParticipantRole";

  private static final String EVD_ATTRIBUTE_DOD = "dateOfDeath";

  private static final String EVD_ATTRIBUTE_DEATH_DATE_NOTIFIED =
    "dateNotifiedOfDeath";

  private static final String EVD_ATTRIBUTE_COUNTRY_OF_DEATH =
    "countryOfDeath";

  private static final String ERR_YEAR_BEFORE_BIRTH_YEAR =
    "A Manually Reported Income year cannot be before year of birth.";

  private static final String ERR_YEAR_AFTER_DEATH_YEAR =
    "A Manually Reported Income year cannot be after year of death.";

  @Override
  protected void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    this.session =
      Session_Factory.getFactory().newInstance(new ManualRecalculation(),
        new InMemoryDataStorage(new InterpretedRuleObjectFactory()));

  }

  /**
   * Test for summary with Preferred Yes and amount greater than zero.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void
    oasManuallyReportedIncomeWithPreferredYesTestAndAmountGreaterThanZero() {

    final String summaryMessage = "2000 Total Income $10 (Designated)";

    final BDMOASManuallyEnteredIncome bdmOASManuallyEnteredIncomeEvidence =
      this.getEvidence();
    bdmOASManuallyEnteredIncomeEvidence
      .canadaPensionPlanOrQuebecPensionBenefit().specifyValue(10);

    bdmOASManuallyEnteredIncomeEvidence.netSelfEmploymentIncome()
      .specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.otherCanadianPensionIncome()
      .specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.employmentInsurance().specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.interestAndOtherInvestmentIncome()
      .specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.otherIncome().specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.netRentalIncome().specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.capitalGains().specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.foreignPensionIncome()
      .specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.workersCompensationBenefits()
      .specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence
      .eligibleAndOtherThanEligibleDividends().specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.netEmploymentIncome().specifyValue(0);

    bdmOASManuallyEnteredIncomeEvidence.preferred()
      .specifyValue(new CodeTableItem("BDMYesNo", "BOASYN001"));
    bdmOASManuallyEnteredIncomeEvidence.year().specifyValue("2000");

    final SummaryInformation summary =
      this.getSummary(bdmOASManuallyEnteredIncomeEvidence);

    assertTrue(summary.summary().getValue().toLocale(Locale.ENGLISH)
      .equals(summaryMessage));
  }

  /**
   * Test for summary with Non Preferred and amount greater than zero.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void
    oasManuallyReportedIncomeWithNonPreferredNoTestAndAmountGreaterThanZero() {

    final String summaryMessage = "2000 Total Income $10";

    final BDMOASManuallyEnteredIncome bdmOASManuallyEnteredIncomeEvidence =
      this.getEvidence();
    bdmOASManuallyEnteredIncomeEvidence
      .canadaPensionPlanOrQuebecPensionBenefit().specifyValue(10);

    bdmOASManuallyEnteredIncomeEvidence.netSelfEmploymentIncome()
      .specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.otherCanadianPensionIncome()
      .specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.employmentInsurance().specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.interestAndOtherInvestmentIncome()
      .specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.otherIncome().specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.netRentalIncome().specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.capitalGains().specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.foreignPensionIncome()
      .specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.workersCompensationBenefits()
      .specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence
      .eligibleAndOtherThanEligibleDividends().specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.netEmploymentIncome().specifyValue(0);

    bdmOASManuallyEnteredIncomeEvidence.preferred()
      .specifyValue(new CodeTableItem("BDMYesNo", "BOASYN002"));
    bdmOASManuallyEnteredIncomeEvidence.year().specifyValue("2000");

    final SummaryInformation summary =
      this.getSummary(bdmOASManuallyEnteredIncomeEvidence);

    assertTrue(summary.summary().getValue().toLocale(Locale.ENGLISH)
      .equals(summaryMessage));
  }

  /**
   * Test for summary with Non Preferred and amount less than zero.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void
    oasManuallyReportedIncomeWithPreferredNoTestAndAmountLessThanZero() {

    final String summaryMessage = "2000 Total Income $0 (Designated)";

    final BDMOASManuallyEnteredIncome bdmOASManuallyEnteredIncomeEvidence =
      this.getEvidence();
    bdmOASManuallyEnteredIncomeEvidence
      .canadaPensionPlanOrQuebecPensionBenefit().specifyValue(10);

    bdmOASManuallyEnteredIncomeEvidence.netSelfEmploymentIncome()
      .specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.otherCanadianPensionIncome()
      .specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.employmentInsurance().specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.interestAndOtherInvestmentIncome()
      .specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.otherIncome().specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.netRentalIncome().specifyValue(-15);
    bdmOASManuallyEnteredIncomeEvidence.capitalGains().specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.foreignPensionIncome()
      .specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.workersCompensationBenefits()
      .specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence
      .eligibleAndOtherThanEligibleDividends().specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.netEmploymentIncome().specifyValue(0);

    bdmOASManuallyEnteredIncomeEvidence.preferred()
      .specifyValue(new CodeTableItem("BDMYesNo", "BOASYN001"));
    bdmOASManuallyEnteredIncomeEvidence.year().specifyValue("2000");

    final SummaryInformation summary =
      this.getSummary(bdmOASManuallyEnteredIncomeEvidence);

    assertTrue(summary.summary().getValue().toLocale(Locale.ENGLISH)
      .equals(summaryMessage));
  }

  /**
   * Test for summary with Non Preferred and amount less than zero.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void
    oasManuallyReportedIncomeWithPreferredYesTestAndAmountLessThanZero() {

    final String summaryMessage = "2000 Total Income $0";

    final BDMOASManuallyEnteredIncome bdmOASManuallyEnteredIncomeEvidence =
      this.getEvidence();
    bdmOASManuallyEnteredIncomeEvidence
      .canadaPensionPlanOrQuebecPensionBenefit().specifyValue(10);

    bdmOASManuallyEnteredIncomeEvidence.netSelfEmploymentIncome()
      .specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.otherCanadianPensionIncome()
      .specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.employmentInsurance().specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.interestAndOtherInvestmentIncome()
      .specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.otherIncome().specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.netRentalIncome().specifyValue(-15);
    bdmOASManuallyEnteredIncomeEvidence.capitalGains().specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.foreignPensionIncome()
      .specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.workersCompensationBenefits()
      .specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence
      .eligibleAndOtherThanEligibleDividends().specifyValue(0);
    bdmOASManuallyEnteredIncomeEvidence.netEmploymentIncome().specifyValue(0);

    bdmOASManuallyEnteredIncomeEvidence.preferred()
      .specifyValue(new CodeTableItem("BDMYesNo", "BOASYN002"));
    bdmOASManuallyEnteredIncomeEvidence.year().specifyValue("2000");

    final SummaryInformation summary =
      this.getSummary(bdmOASManuallyEnteredIncomeEvidence);

    assertTrue(summary.summary().getValue().toLocale(Locale.ENGLISH)
      .equals(summaryMessage));
  }

  /**
   * Return the evidence for the session
   *
   * @return
   */
  private BDMOASManuallyEnteredIncome getEvidence() {

    return BDMOASManuallyEnteredIncome_Factory.getFactory()
      .newInstance(this.session);

  }

  /**
   * Return summary information rule class.
   *
   * @param evidence
   * @return
   */
  private SummaryInformation
    getSummary(final BDMOASManuallyEnteredIncome evidence) {

    final SummaryInformation summary =
      SummaryInformation_Factory.getFactory().newInstance(this.session);
    summary.evidence().specifyValue(evidence);

    return summary;
  }

  /**
   * Successful creation of the manually reported income evidence
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void successfulManuallyReportedIncomeEvidenceCreation()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(YEAR, "2015");
    attributes.put(PREFERRED, "YN1");
    attributes.put(CASEPARTICIPANTROLE,
      String.valueOf(cprObj.caseParticipantRoleID));

    final EIEvidenceKey eiEvidenceKey = this.createEvidence(
      integratedCase.integratedCaseID,
      person.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.OAS_MANUALLY_ENTERED_INCOME, attributes, getToday());

    assertTrue(eiEvidenceKey.evidenceID != CuramConst.gkZero);
  }

  /**
   * 'Year' field validation of the manually reported income evidence
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void manuallyReportedIncomeEvidenceYearValidation()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(YEAR, "1965");
    attributes.put(PREFERRED, "YN1");
    attributes.put(CASEPARTICIPANTROLE,
      String.valueOf(cprObj.caseParticipantRoleID));

    // create evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_MANUALLY_ENTERED_INCOME, attributes,
          getToday());
      });

    final int previousYear =
      Date.getCurrentDate().getCalendar().get(Calendar.YEAR) - 1;

    final String validationMessageExpected =
      "'Year' must be a whole number between 1967 and " + previousYear + ".";

    assertTrue(exception.getMessage().equals(validationMessageExpected));
  }

  /**
   * 'Year' field validation of the manually reported income evidence
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void
    manuallyReportedIncomeEvidenceCanadaianPensionPlanNegetiveAmountValidation()
      throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(YEAR, "2000");
    attributes.put(PREFERRED, "YN1");
    attributes.put(CANADAPENSIONPLANORQUEBECPENSIONBENEFIT, "-10");
    attributes.put(CASEPARTICIPANTROLE,
      String.valueOf(cprObj.caseParticipantRoleID));

    // create evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_MANUALLY_ENTERED_INCOME, attributes,
          getToday());
      });

    final String validationMessageExpected =
      "Canadian Pension Plan or Quebec Pension Plan benefits cannot be a negative amount.";

    assertTrue(exception.getMessage().equals(validationMessageExpected));
  }

  /**
   * 'Net Rental Income' field negetive amount test for manually reported income
   * evidence
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void manuallyReportedIncomeEvidenceRentalIncomeNegetiveAmount()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(YEAR, "2000");
    attributes.put(PREFERRED, "YN1");
    attributes.put(NETRENTALINCOME, "-10");
    attributes.put(CASEPARTICIPANTROLE,
      String.valueOf(cprObj.caseParticipantRoleID));

    // create evidence record
    final EIEvidenceKey eiEvidenceKey = this.createEvidence(
      integratedCase.integratedCaseID,
      person.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.OAS_MANUALLY_ENTERED_INCOME, attributes, getToday());

    assertTrue(eiEvidenceKey.evidenceID != CuramConst.gkZero);

  }

  /**
   * Duplicate Evidence With Same Year for the manually reported income evidence
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void manuallyReportedIncomeEvidenceDuplicateEvidenceWithSameYear()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(YEAR, "2000");
    attributes.put(PREFERRED, "YN1");
    attributes.put(NETRENTALINCOME, "-10");
    attributes.put(CASEPARTICIPANTROLE,
      String.valueOf(cprObj.caseParticipantRoleID));

    // create evidence record
    this.createEvidence(integratedCase.integratedCaseID,
      person.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.OAS_MANUALLY_ENTERED_INCOME, attributes,
      getToday().addDays(-2));

    // assertTrue(eiEvidenceKey.evidenceID != CuramConst.gkZero);
    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCase.integratedCaseID;
    evidenceController.applyAllChanges(caseKey);

    // create evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_MANUALLY_ENTERED_INCOME, attributes,
          getToday());
      });

    final String validationMessageExpected = getConcernRoleNameAndAge(
      person.registrationIDDetails.concernRoleID)
      + " can only have one Manually Entered Income record for a given year.";

    assertTrue(exception.getMessage().equals(validationMessageExpected));

  }

  /**
   * Year Before DOB for the manually reported income evidence
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void manuallyReportedIncomeEvidenceYearBeforeDOB()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(YEAR, "1978");
    attributes.put(PREFERRED, "YN1");
    attributes.put(NETRENTALINCOME, "-10");
    attributes.put(CASEPARTICIPANTROLE,
      String.valueOf(cprObj.caseParticipantRoleID));

    // create evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_MANUALLY_ENTERED_INCOME, attributes,
          getToday().addDays(-2));
      });

    assertTrue(exception.getMessage().equals(ERR_YEAR_BEFORE_BIRTH_YEAR));

  }

  /**
   * Year After DOD for the manually reported income evidence
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void manuallyReportedIncomeEvidenceYearAfterDOD()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(YEAR, "2003");
    attributes.put(PREFERRED, "YN1");
    attributes.put(NETRENTALINCOME, "-10");
    attributes.put(CASEPARTICIPANTROLE,
      String.valueOf(cprObj.caseParticipantRoleID));

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
          CASEEVIDENCEEntry.OAS_MANUALLY_ENTERED_INCOME, attributes,
          getToday());
      });

    assertTrue(exception.getMessage().equals(ERR_YEAR_AFTER_DEATH_YEAR));

  }

  /**
   * Returns the ConcernRole Name and Age
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private String getConcernRoleNameAndAge(final Long concernRoleID)
    throws AppException, InformationalException {

    final AlternateName alternateName = AlternateNameFactory.newInstance();
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = concernRoleID;
    final NameAndAgeStruct nameAndAgeStruct =
      alternateName.getNameAndAge(concernRoleKey);

    return nameAndAgeStruct.nameAndAgeString;
  }

  /**
   * Registers a person for test purposes.
   *
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public PersonRegistrationResult registerPerson()
    throws AppException, InformationalException {

    final Person person = PersonFactory.newInstance();
    final PersonRegistrationDetails personDtls =
      new PersonRegistrationDetails();

    personDtls.personRegistrationDetails.firstForename = "Testington";
    personDtls.personRegistrationDetails.surname = "Bear";
    personDtls.personRegistrationDetails.dateOfBirth =
      Date.fromISO8601("19800101");
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

}
