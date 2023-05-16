package curam.bdmoas.test.evidence.deemedDate.impl;

import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdmoas.codetable.BDMOASAPPLICATIONSTATUS;
import curam.ca.gc.bdmoas.codetable.BDMOASBENEFITTYPE;
import curam.ca.gc.bdmoas.codetable.BDMOASDEEMEDDATEREASON;
import curam.ca.gc.bdmoas.codetable.BDMOASMETHODOFAPPLICATION;
import curam.ca.gc.bdmoas.creole.impl.Statics;
import curam.ca.gc.bdmoas.evidence.constants.impl.BDMOASApplicationDetailsConstants;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.facade.struct.CreateIntegratedCaseResult;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.ECActiveEvidenceDtls;
import curam.core.sl.infrastructure.struct.ECActiveEvidenceDtlsList;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.EvidenceDescriptorDetails;
import curam.core.sl.struct.EvidenceKey;
import curam.core.sl.struct.EvidenceTypeKey;
import curam.core.struct.CaseKey;
import curam.creole.execution.session.InterpretedRuleObjectFactory;
import curam.creole.execution.session.ManualRecalculation;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.ruleclass.BDMOASDeemedDateRuleSet.impl.BDMOASDeemedDate;
import curam.creole.ruleclass.BDMOASDeemedDateRuleSet.impl.BDMOASDeemedDate_Factory;
import curam.creole.ruleclass.BDMOASDeemedDateSummaryRuleSet.impl.SummaryInformation;
import curam.creole.ruleclass.BDMOASDeemedDateSummaryRuleSet.impl.SummaryInformation_Factory;
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
import curam.util.resources.StringUtil;
import curam.util.type.Date;
import curam.wizard.util.impl.CodetableUtil;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BDMOASDeemedDateTest extends BDMOASCaseTest {

  private Session session;

  private static final String DEEMED_DATE = "deemedDate";

  private static final String DEEMED_DATE_REASON = "deemedDateReason";

  private static final String OTHER_DEEMED_DATE_REASON =
    "otherDeemedDateReason";

  private static final String COUNTRY = "country";

  private static final String ERR_SINGLE_INSTANCE =
    "Only one Deemed Date evidence is allowed if the reason is not 'Protective Date under SSA' or 'Other'.";

  private static final String ERR_SINGLE_PROTECTIVE_DATE =
    "Only one Deemed Date evidence for a given Application Details record is allowed with the reason of 'Protective Date under SSA' for a given country.";

  private static final String ERR_OTHER_REASON_REQUIRED =
    "Other Deemed Date Reason must be entered if Deemed Date Reason is entered as 'Other'.";

  private static final String ERR_COUNTRY_REQUIRED =
    "Country must be selected if Deemed Date Reason is 'Protective Date under SSA'.";

  private static final String ERR_DEEMED_DATE_MIN_LENGTH =
    "Other Deemed Date Reason must be at least 3 characters.";

  private static final String ERR_PROTECTIVE_DATE =
    "A Country must have a Protective Date provision in force to be selected. ";

  @Override
  protected void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    this.session =
      Session_Factory.getFactory().newInstance(new ManualRecalculation(),
        new InMemoryDataStorage(new InterpretedRuleObjectFactory()));
  }

  /**
   * PASS-IF the given country has a protective date provision.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void isProtectedTest() throws AppException, InformationalException {

    final CodeTableItem countryCode =
      new CodeTableItem(BDMSOURCECOUNTRY.TABLENAME, BDMSOURCECOUNTRY.US);

    assertTrue(Statics.isProtectiveDateCountry(null, countryCode));

  }

  /**
   * PASS-IF the given country has a protective date provision.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void isNotProtectedTest()
    throws AppException, InformationalException {

    final CodeTableItem countryCode =
      new CodeTableItem(BDMSOURCECOUNTRY.TABLENAME, BDMSOURCECOUNTRY.JAPAN);

    assertFalse(Statics.isProtectiveDateCountry(null, countryCode));

  }

  /**
   * PASS-IF Summary message indicates the country when the Deemed Date
   * Reason is 'Protective Date under SSA'.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void protectiveDateSummaryTest() {

    final String summaryMessage = "Protective Date under SSA with "
      + CodetableUtil.getCodetableDescription(BDMSOURCECOUNTRY.TABLENAME,
        BDMSOURCECOUNTRY.US);

    final BDMOASDeemedDate evidence =
      BDMOASDeemedDate_Factory.getFactory().newInstance(this.session);

    evidence.deemedDate().specifyValue(Date.getCurrentDate().addDays(-20));
    evidence.deemedDateReason()
      .specifyValue(new CodeTableItem(BDMOASDEEMEDDATEREASON.TABLENAME,
        BDMOASDEEMEDDATEREASON.PROTECTIVE_DATE));
    evidence.country().specifyValue(
      new CodeTableItem(BDMSOURCECOUNTRY.TABLENAME, BDMSOURCECOUNTRY.US));

    final SummaryInformation summary =
      SummaryInformation_Factory.getFactory().newInstance(this.session);
    summary.evidence().specifyValue(evidence);

    assertTrue(summary.summary().getValue().toLocale(Locale.ENGLISH)
      .equals(summaryMessage));
  }

  /**
   * PASS-IF Summary message indicates just the Deemed Date Reason when the
   * Deemed Date Reason is not 'Protective Date under SSA'.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void nonProtectiveDateSummaryTest() {

    final String summaryMessage =
      CodetableUtil.getCodetableDescription(BDMOASDEEMEDDATEREASON.TABLENAME,
        BDMOASDEEMEDDATEREASON.ADMINISTRATIVE_ERROR);

    final BDMOASDeemedDate evidence =
      BDMOASDeemedDate_Factory.getFactory().newInstance(this.session);

    evidence.deemedDate().specifyValue(Date.getCurrentDate().addDays(-20));
    evidence.deemedDateReason()
      .specifyValue(new CodeTableItem(BDMOASDEEMEDDATEREASON.TABLENAME,
        BDMOASDEEMEDDATEREASON.ADMINISTRATIVE_ERROR));

    final SummaryInformation summary =
      SummaryInformation_Factory.getFactory().newInstance(this.session);
    summary.evidence().specifyValue(evidence);

    assertTrue(summary.summary().getValue().toLocale(Locale.ENGLISH)
      .equals(summaryMessage));
  }

  /**
   * PASS-IF a validation message is thrown when multiple deemed date evidences
   * are created with deemed date reason not in ‘Protective Date under SSA’ or
   * ‘Other’ for an application details evidence.
   */
  @Test
  public void multipleDeemedDateValidationFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final Long concernRoleID = person.registrationIDDetails.concernRoleID;
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(concernRoleID);

    final EIEvidenceKey applicationDetailsEvidence =
      createApplicationDetailsEvidence(person, integratedCase,
        BDMOASBENEFITTYPE.OAS_PENSION);

    // Create First Deemed Date evidence record
    createDeemedDateEvidence(integratedCase.integratedCaseID, concernRoleID,
      Date.getCurrentDate().addDays(-20),
      BDMOASDEEMEDDATEREASON.ADMINISTRATIVE_ERROR, null, null,
      applicationDetailsEvidence);

    // Create Deemed Date evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        createDeemedDateEvidence(integratedCase.integratedCaseID,
          concernRoleID, Date.getCurrentDate().addDays(-20),
          BDMOASDEEMEDDATEREASON.ERRONEOUS_ADVICE, null, null,
          applicationDetailsEvidence);
      });

    // Assert valid exception message thrown
    assertTrue(exception.getMessage().contains(ERR_SINGLE_INSTANCE));
  }

  /**
   * PASS-IF multiple deemed date evidences can be created with deemed date
   * reason as ‘Protective Date under SSA’ or ‘Other’ for an application details
   * evidence.
   */
  @Test
  public void multipleDeemedDateValidationSucess()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final Long concernRoleID = person.registrationIDDetails.concernRoleID;
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(concernRoleID);

    final EIEvidenceKey applicationDetailsEvidence =
      createApplicationDetailsEvidence(person, integratedCase,
        BDMOASBENEFITTYPE.OAS_PENSION);

    // Create First Deemed Date evidence record
    createDeemedDateEvidence(integratedCase.integratedCaseID, concernRoleID,
      Date.getCurrentDate().addDays(-20),
      BDMOASDEEMEDDATEREASON.PROTECTIVE_DATE, null, BDMSOURCECOUNTRY.US,
      applicationDetailsEvidence);

    // Create Second Deemed Date evidence record
    createDeemedDateEvidence(integratedCase.integratedCaseID, concernRoleID,
      Date.getCurrentDate().addDays(-20), BDMOASDEEMEDDATEREASON.OTHER,
      "other reason description", null, applicationDetailsEvidence);

    // Apply Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCase.integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    evidenceController.applyAllChanges(caseKey);

    // Check if the evidence records were activated
    final ECActiveEvidenceDtlsList activeEvdList =
      evidenceController.listActive(caseKey);

    // Assert both records activated
    final List<ECActiveEvidenceDtls> filteredList1 =
      activeEvdList.dtls.stream()
        .filter((x) -> x.evidenceType.equals(CASEEVIDENCE.OAS_DEEMED_DATE))
        .collect(Collectors.toList());
    assertTrue(filteredList1.size() == 2);
  }

  /**
   * PASS-IF a validation message is thrown when multiple deemed date evidences
   * are created with deemed date reason as ‘Protective Date under SSA’ for a
   * given country for an application details evidence.
   */
  @Test
  public void multipleDeemedDateForSSACountryValidationFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final Long concernRoleID = person.registrationIDDetails.concernRoleID;
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(concernRoleID);

    final EIEvidenceKey applicationDetailsEvidence =
      createApplicationDetailsEvidence(person, integratedCase,
        BDMOASBENEFITTYPE.OAS_PENSION);

    // Create First Deemed Date evidence record
    createDeemedDateEvidence(integratedCase.integratedCaseID, concernRoleID,
      Date.getCurrentDate().addDays(-20),
      BDMOASDEEMEDDATEREASON.PROTECTIVE_DATE, null,
      BDMSOURCECOUNTRY.CZECH_REPUBLIC, applicationDetailsEvidence);

    // Create Second Deemed Date evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        createDeemedDateEvidence(integratedCase.integratedCaseID,
          concernRoleID, Date.getCurrentDate().addDays(-20),
          BDMOASDEEMEDDATEREASON.PROTECTIVE_DATE, null,
          BDMSOURCECOUNTRY.CZECH_REPUBLIC, applicationDetailsEvidence);
      });

    // Assert valid exception message thrown
    assertTrue(exception.getMessage().contains(ERR_SINGLE_PROTECTIVE_DATE));
  }

  /**
   * PASS-IF a validation message is thrown when Deemed Date Reason is Other but
   * Other Deemed Date Reason is not entered.
   */
  @Test
  public void otherDeemedDateReasonNotEnteredFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final Long concernRoleID = person.registrationIDDetails.concernRoleID;
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(concernRoleID);

    final EIEvidenceKey applicationDetailsEvidence =
      createApplicationDetailsEvidence(person, integratedCase,
        BDMOASBENEFITTYPE.OAS_PENSION);

    // Create Deemed Date evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        createDeemedDateEvidence(integratedCase.integratedCaseID,
          concernRoleID, Date.getCurrentDate().addDays(-20),
          BDMOASDEEMEDDATEREASON.OTHER, null, null,
          applicationDetailsEvidence);
      });

    // Assert valid exception message thrown
    assertTrue(exception.getMessage().contains(ERR_OTHER_REASON_REQUIRED));
  }

  /**
   * PASS-IF a validation message is thrown when Deemed Date Reason is Other but
   * Other Deemed Date Reason is not entered.
   */
  @Test
  public void otherDeemedDateReasonMinLengthFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final Long concernRoleID = person.registrationIDDetails.concernRoleID;
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(concernRoleID);

    final EIEvidenceKey applicationDetailsEvidence =
      createApplicationDetailsEvidence(person, integratedCase,
        BDMOASBENEFITTYPE.OAS_PENSION);

    // Create Deemed Date evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        createDeemedDateEvidence(integratedCase.integratedCaseID,
          concernRoleID, Date.getCurrentDate().addDays(-20),
          BDMOASDEEMEDDATEREASON.OTHER, "NA", null,
          applicationDetailsEvidence);
      });

    // Assert valid exception message thrown
    assertTrue(exception.getMessage().contains(ERR_DEEMED_DATE_MIN_LENGTH));
  }

  /**
   * PASS-IF a validation message is thrown when Deemed Date Reason is
   * Protective Date under SSA but Country is not selected.
   */
  @Test
  public void countyMustBeSelectedFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final Long concernRoleID = person.registrationIDDetails.concernRoleID;
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(concernRoleID);

    final EIEvidenceKey applicationDetailsEvidence =
      createApplicationDetailsEvidence(person, integratedCase,
        BDMOASBENEFITTYPE.OAS_PENSION);

    // Create Deemed Date evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        createDeemedDateEvidence(integratedCase.integratedCaseID,
          concernRoleID, Date.getCurrentDate().addDays(-20),
          BDMOASDEEMEDDATEREASON.PROTECTIVE_DATE, null, null,
          applicationDetailsEvidence);
      });

    // Assert valid exception message thrown
    assertTrue(exception.getMessage().contains(ERR_COUNTRY_REQUIRED));
  }

  /**
   * PASS-IF no validation message is thrown when Deemed Date Reason is
   * Protective Date under SSA and Country is selected.
   */
  @Test
  public void countyMustBeSelectedSuccess()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final Long concernRoleID = person.registrationIDDetails.concernRoleID;
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(concernRoleID);

    final EIEvidenceKey applicationDetailsEvidence =
      createApplicationDetailsEvidence(person, integratedCase,
        BDMOASBENEFITTYPE.OAS_PENSION);

    // Create Deemed Date evidence record
    createDeemedDateEvidence(integratedCase.integratedCaseID, concernRoleID,
      Date.getCurrentDate().addDays(-20),
      BDMOASDEEMEDDATEREASON.PROTECTIVE_DATE, null,
      BDMSOURCECOUNTRY.CZECH_REPUBLIC, applicationDetailsEvidence);

    // Apply Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCase.integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    evidenceController.applyAllChanges(caseKey);

    // Check if the evidence record was activated
    final ECActiveEvidenceDtlsList activeEvdList =
      evidenceController.listActive(caseKey);

    // Assert both records activated
    final List<ECActiveEvidenceDtls> filteredList1 =
      activeEvdList.dtls.stream()
        .filter((x) -> x.evidenceType.equals(CASEEVIDENCE.OAS_DEEMED_DATE))
        .collect(Collectors.toList());
    assertTrue(filteredList1.size() == 1);
  }

  /**
   * PASS-IF a validation message is thrown when Deemed Date Reason is
   * Protective Date under SSA but Country selected does not have a protective
   * date provision in force.
   */
  @Test
  public void countySelectedMustHaveProtectiveDateProvisionFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final Long concernRoleID = person.registrationIDDetails.concernRoleID;
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(concernRoleID);

    final EIEvidenceKey applicationDetailsEvidence =
      createApplicationDetailsEvidence(person, integratedCase,
        BDMOASBENEFITTYPE.OAS_PENSION);

    // Create Deemed Date evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        createDeemedDateEvidence(integratedCase.integratedCaseID,
          concernRoleID, Date.getCurrentDate().addDays(-20),
          BDMOASDEEMEDDATEREASON.PROTECTIVE_DATE, null,
          BDMSOURCECOUNTRY.JAPAN, applicationDetailsEvidence);
      });

    // Assert valid exception message thrown
    assertTrue(exception.getMessage().contains(ERR_PROTECTIVE_DATE));
  }

  private curam.core.sl.struct.EvidenceKey createDeemedDateEvidence(
    final Long caseID, final long concernroleID, final Date deemedDate,
    final String deemedDateReason, final String otherReason,
    final String country, final EIEvidenceKey applicationDetailsEvidence)
    throws AppException, InformationalException {

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.OAS_DEEMED_DATE;

    // get Latest Version of Evidence
    final GenericSLDataDetails genericDtls = new GenericSLDataDetails();
    final EvidenceServiceInterface evidenceServiceInterface =
      EvidenceGenericSLFactory.instance(eType, Date.getCurrentDate());

    final DynamicEvidenceDataDetails dynamicEvidencedataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(eType.evidenceType,
        Date.getCurrentDate());
    // assigning all the required attributes
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(DEEMED_DATE), deemedDate);
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(DEEMED_DATE_REASON),
      new CodeTableItem(BDMOASDEEMEDDATEREASON.TABLENAME, deemedDateReason));
    if (!StringUtil.isNullOrEmpty(otherReason))
      DynamicEvidenceTypeConverter.setAttribute(
        dynamicEvidencedataDetails.getAttribute(OTHER_DEEMED_DATE_REASON),
        otherReason);
    if (!StringUtil.isNullOrEmpty(country))
      DynamicEvidenceTypeConverter.setAttribute(
        dynamicEvidencedataDetails.getAttribute(COUNTRY),
        new CodeTableItem(BDMSOURCECOUNTRY.TABLENAME, country));

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

    final EvidenceKey evidenceKey = new EvidenceKey();
    evidenceKey.evidenceID = applicationDetailsEvidence.evidenceID;
    evidenceKey.evType = CASEEVIDENCE.OAS_APPLICATION_DETAILS;
    genericDtls.addParent(CASEEVIDENCE.OAS_APPLICATION_DETAILS, evidenceKey);

    return evidenceServiceInterface.createEvidence(genericDtls).evidenceKey;
  }

  private EIEvidenceKey createApplicationDetailsEvidence(
    final PersonRegistrationResult person,
    final CreateIntegratedCaseResult integratedCase, final String benefitType)
    throws AppException, InformationalException {

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(BDMOASApplicationDetailsConstants.BENEFIT_TYPE,
      benefitType);
    attributes.put(BDMOASApplicationDetailsConstants.APPLICATION_STATUS,
      BDMOASAPPLICATIONSTATUS.ACTIVE);
    attributes.put(BDMOASApplicationDetailsConstants.METHOD_OF_APPLICATION,
      BDMOASMETHODOFAPPLICATION.ONLINE);
    attributes.put(BDMOASApplicationDetailsConstants.RECEIPT_DATE,
      formattedDate(Date.getCurrentDate().addDays(-50), "yyyyMMdd"));

    return this.createEvidence(integratedCase.integratedCaseID,
      person.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.OAS_APPLICATION_DETAILS, attributes, getToday());
  }

  private String formattedDate(final Date date, final String format) {

    final SimpleDateFormat dateFormat = new SimpleDateFormat(format);

    return dateFormat.format(date.getCalendar().getTime());
  }
}
