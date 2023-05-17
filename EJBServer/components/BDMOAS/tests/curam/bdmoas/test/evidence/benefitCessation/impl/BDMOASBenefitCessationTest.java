package curam.bdmoas.test.evidence.benefitCessation.impl;

import curam.ca.gc.bdmoas.codetable.BDMOASAPPLICATIONSTATUS;
import curam.ca.gc.bdmoas.codetable.BDMOASBENEFITTYPE;
import curam.ca.gc.bdmoas.codetable.BDMOASMETHODOFAPPLICATION;
import curam.ca.gc.bdmoas.evidence.constants.impl.BDMOASApplicationDetailsConstants;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.facade.struct.CreateIntegratedCaseResult;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.EvidenceDescriptorDetails;
import curam.core.sl.struct.EvidenceKey;
import curam.core.sl.struct.EvidenceTypeKey;
import curam.creole.execution.session.InterpretedRuleObjectFactory;
import curam.creole.execution.session.ManualRecalculation;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.ruleclass.BDMOASApplicationDetailsRuleSet.impl.BDMOASApplicationDetails;
import curam.creole.ruleclass.BDMOASApplicationDetailsRuleSet.impl.BDMOASApplicationDetails_Factory;
import curam.creole.ruleclass.BDMOASBenefitCessationRuleSet.impl.BDMOASBenefitCessation;
import curam.creole.ruleclass.BDMOASBenefitCessationRuleSet.impl.BDMOASBenefitCessation_Factory;
import curam.creole.ruleclass.BDMOASBenefitCessationSummaryRuleSet.impl.SummaryInformation;
import curam.creole.ruleclass.BDMOASBenefitCessationSummaryRuleSet.impl.SummaryInformation_Factory;
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
import curam.util.type.Date;
import curam.wizard.util.impl.CodetableUtil;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class BDMOASBenefitCessationTest extends BDMOASCaseTest {

  private static final String CESSATION_DATE = "cessationDate";

  private static final String REINSTATEMENT_DATE = "reinstatementDate";

  private static final String ERR_OVERLAP =
    "An Application Details can only have one Benefit Cessation evidence for an overlapping period.";

  private static final String ERR_REINSTATEMENT_AFTER_CESSATION =
    "Reinstatement Date must be on or after Cessation Date.";

  private static final String ERR_NON_OAS_REINSTATEMENT =
    "Reinstatement Date can only be entered if the Application Details evidence Benefit Type is 'Old Age Security Pension'.";

  private Session session;

  @Override
  protected void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    this.session =
      Session_Factory.getFactory().newInstance(new ManualRecalculation(),
        new InMemoryDataStorage(new InterpretedRuleObjectFactory()));
  }

  /**
   * PASS-IF Summary message indicates ceased benefit when no reinstatement date
   * is present.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void ceasedSummaryTest() {

    final String summaryMessage =
      CodetableUtil.getCodetableDescription(BDMOASBENEFITTYPE.TABLENAME,
        BDMOASBENEFITTYPE.OAS_PENSION) + " ceased";

    final BDMOASBenefitCessation evidence =
      BDMOASBenefitCessation_Factory.getFactory().newInstance(this.session);

    final BDMOASApplicationDetails applicationDetails =
      BDMOASApplicationDetails_Factory.getFactory().newInstance(this.session);
    applicationDetails.benefitType().specifyValue(new CodeTableItem(
      BDMOASBENEFITTYPE.TABLENAME, BDMOASBENEFITTYPE.OAS_PENSION));

    evidence.applicationDetails().specifyValue(applicationDetails);
    evidence.cessationDate()
      .specifyValue(Date.getCurrentDate().addDays(-100));
    evidence.reinstatementDate().specifyValue(null);

    final SummaryInformation summary =
      SummaryInformation_Factory.getFactory().newInstance(this.session);
    summary.evidence().specifyValue(evidence);

    assertTrue(summary.summary().getValue().toLocale(Locale.ENGLISH)
      .equals(summaryMessage));
  }

  /**
   * PASS-IF Summary message indicates reinstated benefit when no reinstatement
   * date
   * is present.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void reinstatedSummaryTest() {

    final String summaryMessage =
      CodetableUtil.getCodetableDescription(BDMOASBENEFITTYPE.TABLENAME,
        BDMOASBENEFITTYPE.OAS_PENSION) + " reinstated";

    final BDMOASBenefitCessation evidence =
      BDMOASBenefitCessation_Factory.getFactory().newInstance(this.session);

    final BDMOASApplicationDetails applicationDetails =
      BDMOASApplicationDetails_Factory.getFactory().newInstance(this.session);
    applicationDetails.benefitType().specifyValue(new CodeTableItem(
      BDMOASBENEFITTYPE.TABLENAME, BDMOASBENEFITTYPE.OAS_PENSION));

    evidence.applicationDetails().specifyValue(applicationDetails);
    evidence.cessationDate()
      .specifyValue(Date.getCurrentDate().addDays(-100));
    evidence.reinstatementDate().specifyValue(Date.getCurrentDate());

    final SummaryInformation summary =
      SummaryInformation_Factory.getFactory().newInstance(this.session);
    summary.evidence().specifyValue(evidence);

    assertTrue(summary.summary().getValue().toLocale(Locale.ENGLISH)
      .equals(summaryMessage));
  }

  /**
   * PASS-IF a validation message is thrown when a second Benefit Cessation
   * record is created for an overlapping period for the same parent
   * Application Details evidence record.
   */
  @Test
  public void benefitCessationOverlapFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final Long concernRoleID = person.registrationIDDetails.concernRoleID;
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(concernRoleID);

    final EIEvidenceKey applicationDetailsEvidence =
      createApplicationDetailsEvidence(person, integratedCase,
        BDMOASBENEFITTYPE.OAS_PENSION);

    // create 1st Benefit Cessation Evidence Record
    createBenefitCessationEvidence(integratedCase.integratedCaseID,
      concernRoleID, Date.getCurrentDate().addDays(-100),
      Date.getCurrentDate(), applicationDetailsEvidence);

    // Create overlapping evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        createBenefitCessationEvidence(integratedCase.integratedCaseID,
          concernRoleID, Date.getCurrentDate().addDays(-20),
          Date.getCurrentDate(), applicationDetailsEvidence);
      });

    // Assert valid exception message thrown
    assertTrue(exception.getMessage().contains(ERR_OVERLAP));
  }

  /**
   * PASS-IF a validation message is thrown when reinstatement date is before
   * cessation date.
   */
  @Test
  public void reinstatementAfterCessationValidationFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final Long concernRoleID = person.registrationIDDetails.concernRoleID;
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(concernRoleID);

    final EIEvidenceKey applicationDetailsEvidence =
      createApplicationDetailsEvidence(person, integratedCase,
        BDMOASBENEFITTYPE.OAS_PENSION);

    // Create Benefit Cessation evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        createBenefitCessationEvidence(integratedCase.integratedCaseID,
          concernRoleID, Date.getCurrentDate().addDays(-20),
          Date.getCurrentDate().addDays(-21), applicationDetailsEvidence);
      });

    // Assert valid exception message thrown
    assertTrue(
      exception.getMessage().contains(ERR_REINSTATEMENT_AFTER_CESSATION));
  }

  /**
   * PASS-IF a validation message is thrown when reinstatement date is entered
   * for non oas benefit type.
   */
  @Test
  public void nonOASReinstatementValidationFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final Long concernRoleID = person.registrationIDDetails.concernRoleID;
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(concernRoleID);

    final EIEvidenceKey applicationDetailsEvidence =
      createApplicationDetailsEvidence(person, integratedCase,
        BDMOASBENEFITTYPE.GUARANTEED_INCOME_SUPPLEMENT);

    // Create Benefit Cessation evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        createBenefitCessationEvidence(integratedCase.integratedCaseID,
          concernRoleID, Date.getCurrentDate().addDays(-20),
          Date.getCurrentDate(), applicationDetailsEvidence);
      });

    // Assert valid exception message thrown
    assertTrue(exception.getMessage().contains(ERR_NON_OAS_REINSTATEMENT));
  }

  private curam.core.sl.struct.EvidenceKey createBenefitCessationEvidence(
    final Long caseID, final long concernroleID, final Date cessationDate,
    final Date reinstatementDate,
    final EIEvidenceKey applicationDetailsEvidence)
    throws AppException, InformationalException {

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.OAS_BENEFIT_CESSATION;

    // get Latest Version of Evidence
    final GenericSLDataDetails genericDtls = new GenericSLDataDetails();
    final EvidenceServiceInterface evidenceServiceInterface =
      EvidenceGenericSLFactory.instance(eType, Date.getCurrentDate());

    final DynamicEvidenceDataDetails dynamicEvidencedataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(eType.evidenceType,
        Date.getCurrentDate());
    // assigning all the required attributes
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(CESSATION_DATE), cessationDate);
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(REINSTATEMENT_DATE),
      reinstatementDate);

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
      formattedDate(Date.getCurrentDate().addDays(-50)));

    return this.createEvidence(integratedCase.integratedCaseID,
      person.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.OAS_APPLICATION_DETAILS, attributes, getToday());
  }

  private String formattedDate(final Date date) {

    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    // move date to the first of the month
    final Calendar cal = date.getCalendar();
    cal.set(Calendar.DAY_OF_MONTH, 1);
    final Date firstOfMonthDate = new Date(cal);

    return dateFormat.format(firstOfMonthDate.getCalendar().getTime());
  }
}
