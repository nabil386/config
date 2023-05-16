package curam.bdmoas.test.evidence.benefitCancellation.impl;

import curam.ca.gc.bdmoas.codetable.BDMOASAPPLICATIONSTATUS;
import curam.ca.gc.bdmoas.codetable.BDMOASBENEFITTYPE;
import curam.ca.gc.bdmoas.codetable.BDMOASCANCELSTATUS;
import curam.ca.gc.bdmoas.codetable.BDMOASMETHODOFAPPLICATION;
import curam.ca.gc.bdmoas.creole.impl.Statics;
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
import curam.creole.ruleclass.BDMOASBenefitCancellationRequestRuleSet.impl.BDMOASBenefitCancellationRequest;
import curam.creole.ruleclass.BDMOASBenefitCancellationRequestRuleSet.impl.BDMOASBenefitCancellationRequest_Factory;
import curam.creole.ruleclass.BDMOASBenefitCancellationRequestSummaryRuleSet.impl.SummaryInformation;
import curam.creole.ruleclass.BDMOASBenefitCancellationRequestSummaryRuleSet.impl.SummaryInformation_Factory;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BDMOASBenefitCancellationTest extends BDMOASCaseTest {

  private Session session;

  private static final String REQUEST_DATE = "requestDate";

  private static final String CANCELLATION_STATUS = "cancellationStatus";

  private static final String GRANT_DATE = "grantDate";

  private static final String REPAYMENT_DUE_DATE_OVERRIDE =
    "repaymentDueDateOverride";

  private static final String ERR_EXISTING_GRANTED =
    "A granted Benefit Cancellation Request already exists for this Application Details evidence.";

  private static final String ERR_GRANT_DATE_AFTER_REQUEST_DATE =
    "Grant Date must be on or after Request Date.";

  private static final String ERR_GRANT_DATE_REQUIRED_GRANTED =
    "Grant Date must be entered if the Cancellation Status is not 'Requested', 'Rescinded', or 'Denied'.";

  private static final String ERR_REPAYMENT_DATE_AFTER_GRANT_DATE =
    "Repayment Due Date Override must be after Grant Date.";

  private static final String ERR_GRANT_DATE_FORBIDDEN_REQUESTED =
    "Grant Date cannot be entered if the Cancellation Status is 'Requested', 'Rescinded', or 'Denied'.";

  private static final String ERR_ALW_CANCELLATION =
    "A Benefit Cancellation Request cannot be created if the Benefit Type is 'Allowance or Allowance for the Survivor'.";

  @Override
  protected void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    this.session =
      Session_Factory.getFactory().newInstance(new ManualRecalculation(),
        new InMemoryDataStorage(new InterpretedRuleObjectFactory()));
  }

  /**
   * PASS-IF the calculated repayment due date is 6 months from the given date.
   */
  @Test
  public void calculatedRepaymentDateTest() {

    final Date grantDate = Date.getCurrentDate();
    final Calendar calendar = grantDate.getCalendar();
    calendar.add(Calendar.MONTH, 6);
    final Date repaymentDate = new Date(calendar);

    assertEquals(repaymentDate,
      Statics.getCancellationRepaymentDate(null, grantDate));

  }

  /**
   * PASS-IF Summary message indicates cancellation is requested for a specific
   * benefit type.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void cancellationRequestedSummaryTest() {

    final String summaryMessage =
      CodetableUtil.getCodetableDescription(BDMOASCANCELSTATUS.TABLENAME,
        BDMOASCANCELSTATUS.REQUESTED) + " cancellation of "
        + CodetableUtil.getCodetableDescription(BDMOASBENEFITTYPE.TABLENAME,
          BDMOASBENEFITTYPE.OAS_PENSION);

    final BDMOASBenefitCancellationRequest evidence =
      BDMOASBenefitCancellationRequest_Factory.getFactory()
        .newInstance(this.session);

    final BDMOASApplicationDetails applicationDetails =
      BDMOASApplicationDetails_Factory.getFactory().newInstance(this.session);
    applicationDetails.benefitType().specifyValue(new CodeTableItem(
      BDMOASBENEFITTYPE.TABLENAME, BDMOASBENEFITTYPE.OAS_PENSION));

    evidence.applicationDetails().specifyValue(applicationDetails);
    evidence.requestDate().specifyValue(Date.getCurrentDate().addDays(-20));
    evidence.cancellationStatus().specifyValue(new CodeTableItem(
      BDMOASCANCELSTATUS.TABLENAME, BDMOASCANCELSTATUS.REQUESTED));

    final SummaryInformation summary =
      SummaryInformation_Factory.getFactory().newInstance(this.session);
    summary.evidence().specifyValue(evidence);

    assertTrue(summary.summary().getValue().toLocale(Locale.ENGLISH)
      .equals(summaryMessage));
  }

  /**
   * PASS-IF Summary message indicates cancellation status and repayment due
   * date.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void cancellationRepaymentDateSummaryTest() {

    final Date grantDate = Date.getCurrentDate();
    final Calendar calendar = grantDate.getCalendar();
    calendar.add(Calendar.MONTH, 6);
    final Date repaymentDate = new Date(calendar);

    final String summaryMessage =
      CodetableUtil.getCodetableDescription(BDMOASCANCELSTATUS.TABLENAME,
        BDMOASCANCELSTATUS.GRANTED) + ", repayment due  "
        + formattedDate(repaymentDate, "yyyy-MM-dd");

    final BDMOASBenefitCancellationRequest evidence =
      BDMOASBenefitCancellationRequest_Factory.getFactory()
        .newInstance(this.session);

    final BDMOASApplicationDetails applicationDetails =
      BDMOASApplicationDetails_Factory.getFactory().newInstance(this.session);
    applicationDetails.benefitType().specifyValue(new CodeTableItem(
      BDMOASBENEFITTYPE.TABLENAME, BDMOASBENEFITTYPE.OAS_PENSION));

    evidence.applicationDetails().specifyValue(applicationDetails);
    evidence.requestDate().specifyValue(Date.getCurrentDate().addDays(-20));
    evidence.grantDate().specifyValue(grantDate);
    evidence.repaymentDueDateOverride().specifyValue(null);
    evidence.cancellationStatus().specifyValue(new CodeTableItem(
      BDMOASCANCELSTATUS.TABLENAME, BDMOASCANCELSTATUS.GRANTED));

    final SummaryInformation summary =
      SummaryInformation_Factory.getFactory().newInstance(this.session);
    summary.evidence().specifyValue(evidence);

    assertTrue(summary.summary().getValue().toLocale(Locale.ENGLISH)
      .equals(summaryMessage));
  }

  /**
   * PASS-IF Summary message indicates cancellation status, repayment due
   * date and repayment due date override.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void cancellationRepaymentDueDateOverrideSummaryTest() {

    final Date grantDate = Date.getCurrentDate();
    final Calendar calendar = grantDate.getCalendar();
    calendar.add(Calendar.MONTH, 6);
    final Date repaymentDate = new Date(calendar);
    final Date repaymentDueDateOverride = Date.getCurrentDate().addDays(210);

    final String summaryMessage =
      CodetableUtil.getCodetableDescription(BDMOASCANCELSTATUS.TABLENAME,
        BDMOASCANCELSTATUS.GRANTED) + ", repayment due  "
        + formattedDate(repaymentDueDateOverride, "yyyy-MM-dd")
        + " (Overridden from  " + formattedDate(repaymentDate, "yyyy-MM-dd")
        + ")";

    final BDMOASBenefitCancellationRequest evidence =
      BDMOASBenefitCancellationRequest_Factory.getFactory()
        .newInstance(this.session);

    final BDMOASApplicationDetails applicationDetails =
      BDMOASApplicationDetails_Factory.getFactory().newInstance(this.session);
    applicationDetails.benefitType().specifyValue(new CodeTableItem(
      BDMOASBENEFITTYPE.TABLENAME, BDMOASBENEFITTYPE.OAS_PENSION));

    evidence.applicationDetails().specifyValue(applicationDetails);
    evidence.requestDate().specifyValue(Date.getCurrentDate().addDays(-20));
    evidence.grantDate().specifyValue(grantDate);
    evidence.repaymentDueDateOverride()
      .specifyValue(repaymentDueDateOverride);
    evidence.cancellationStatus().specifyValue(new CodeTableItem(
      BDMOASCANCELSTATUS.TABLENAME, BDMOASCANCELSTATUS.GRANTED));

    final SummaryInformation summary =
      SummaryInformation_Factory.getFactory().newInstance(this.session);
    summary.evidence().specifyValue(evidence);

    assertTrue(summary.summary().getValue().toLocale(Locale.ENGLISH)
      .equals(summaryMessage));
  }

  /**
   * PASS-IF a validation message is thrown when multiple benefit cancellation
   * request evidences are created with Granted status for an application
   * details evidence.
   */
  @Test
  public void multipleCancellationGrantedValidationFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final Long concernRoleID = person.registrationIDDetails.concernRoleID;
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(concernRoleID);

    final EIEvidenceKey applicationDetailsEvidence =
      createApplicationDetailsEvidence(person, integratedCase,
        BDMOASBENEFITTYPE.OAS_PENSION);

    // Create First Benefit Cancellation evidence record with Granted Status.
    createBenefitCancellationEvidence(integratedCase.integratedCaseID,
      concernRoleID, Date.getCurrentDate().addDays(-20),
      Date.getCurrentDate(), null, BDMOASCANCELSTATUS.GRANTED,
      applicationDetailsEvidence);

    // Create Second Benefit Cancellation evidence record with Granted Status.
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        createBenefitCancellationEvidence(integratedCase.integratedCaseID,
          concernRoleID, Date.getCurrentDate().addDays(-20),
          Date.getCurrentDate(), null, BDMOASCANCELSTATUS.GRANTED,
          applicationDetailsEvidence);
      });

    // Assert valid exception message thrown
    assertTrue(exception.getMessage().contains(ERR_EXISTING_GRANTED));
  }

  /**
   * PASS-IF a validation message is thrown when Grant Date is before Request
   * Date.
   */
  @Test
  public void grantDateAfterRequestDateValidationFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final Long concernRoleID = person.registrationIDDetails.concernRoleID;
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(concernRoleID);

    final EIEvidenceKey applicationDetailsEvidence =
      createApplicationDetailsEvidence(person, integratedCase,
        BDMOASBENEFITTYPE.OAS_PENSION);

    // Create Benefit Cancellation evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        createBenefitCancellationEvidence(integratedCase.integratedCaseID,
          concernRoleID, Date.getCurrentDate().addDays(-20),
          Date.getCurrentDate().addDays(-25), null,
          BDMOASCANCELSTATUS.GRANTED, applicationDetailsEvidence);
      });

    // Assert valid exception message thrown
    assertTrue(
      exception.getMessage().contains(ERR_GRANT_DATE_AFTER_REQUEST_DATE));
  }

  /**
   * PASS-IF a validation message is thrown when Grant Date is not entered when
   * the cancellation status is other than Requested, Rescinded or Denied.
   */
  @Test
  public void grantDateRequiredValidationFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final Long concernRoleID = person.registrationIDDetails.concernRoleID;
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(concernRoleID);

    final EIEvidenceKey applicationDetailsEvidence =
      createApplicationDetailsEvidence(person, integratedCase,
        BDMOASBENEFITTYPE.OAS_PENSION);

    // Create Benefit Cancellation evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        createBenefitCancellationEvidence(integratedCase.integratedCaseID,
          concernRoleID, Date.getCurrentDate().addDays(-20), null, null,
          BDMOASCANCELSTATUS.GRANTED, applicationDetailsEvidence);
      });

    // Assert valid exception message thrown
    assertTrue(
      exception.getMessage().contains(ERR_GRANT_DATE_REQUIRED_GRANTED));
  }

  /**
   * PASS-IF a validation message is thrown when Repayment Due Date Override is
   * before Grant Date.
   */
  @Test
  public void repaymentDueDateOverrideAfterGrantDateValidationFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final Long concernRoleID = person.registrationIDDetails.concernRoleID;
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(concernRoleID);

    final EIEvidenceKey applicationDetailsEvidence =
      createApplicationDetailsEvidence(person, integratedCase,
        BDMOASBENEFITTYPE.OAS_PENSION);

    // Create Benefit Cancellation evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        createBenefitCancellationEvidence(integratedCase.integratedCaseID,
          concernRoleID, Date.getCurrentDate().addDays(-20),
          Date.getCurrentDate(), Date.getCurrentDate().addDays(-5),
          BDMOASCANCELSTATUS.GRANTED, applicationDetailsEvidence);
      });

    // Assert valid exception message thrown
    assertTrue(
      exception.getMessage().contains(ERR_REPAYMENT_DATE_AFTER_GRANT_DATE));
  }

  /**
   * PASS-IF a validation message is thrown when Grant Date is entered when
   * the cancellation status is Requested, Rescinded or Denied.
   */
  @Test
  public void grantDateForbiddenValidationFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final Long concernRoleID = person.registrationIDDetails.concernRoleID;
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(concernRoleID);

    final EIEvidenceKey applicationDetailsEvidence =
      createApplicationDetailsEvidence(person, integratedCase,
        BDMOASBENEFITTYPE.OAS_PENSION);

    // Create Benefit Cancellation evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        createBenefitCancellationEvidence(integratedCase.integratedCaseID,
          concernRoleID, Date.getCurrentDate().addDays(-20),
          Date.getCurrentDate(), null, BDMOASCANCELSTATUS.REQUESTED,
          applicationDetailsEvidence);
      });

    // Assert valid exception message thrown
    assertTrue(
      exception.getMessage().contains(ERR_GRANT_DATE_FORBIDDEN_REQUESTED));
  }

  /**
   * PASS-IF a validation message is thrown when Benefit Cancellation Request is
   * created for the benefit of type 'Allowance or Allowance for the Survivor'.
   */
  @Test
  public void cancellationRequestNotAllowedValidationFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final Long concernRoleID = person.registrationIDDetails.concernRoleID;
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(concernRoleID);

    final EIEvidenceKey applicationDetailsEvidence =
      createApplicationDetailsEvidence(person, integratedCase,
        BDMOASBENEFITTYPE.ALLOWANCE);

    // Create Benefit Cancellation evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        createBenefitCancellationEvidence(integratedCase.integratedCaseID,
          concernRoleID, Date.getCurrentDate().addDays(-20), null, null,
          BDMOASCANCELSTATUS.REQUESTED, applicationDetailsEvidence);
      });

    // Assert valid exception message thrown
    assertTrue(exception.getMessage().contains(ERR_ALW_CANCELLATION));
  }

  private curam.core.sl.struct.EvidenceKey createBenefitCancellationEvidence(
    final Long caseID, final long concernroleID, final Date requestDate,
    final Date grantDate, final Date repaymentDueOverride,
    final String cancellationStatus,
    final EIEvidenceKey applicationDetailsEvidence)
    throws AppException, InformationalException {

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.OAS_BENEFIT_CANCELLATION;

    // get Latest Version of Evidence
    final GenericSLDataDetails genericDtls = new GenericSLDataDetails();
    final EvidenceServiceInterface evidenceServiceInterface =
      EvidenceGenericSLFactory.instance(eType, Date.getCurrentDate());

    final DynamicEvidenceDataDetails dynamicEvidencedataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(eType.evidenceType,
        Date.getCurrentDate());
    // assigning all the required attributes
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(GRANT_DATE), grantDate);
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(REQUEST_DATE), requestDate);
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(REPAYMENT_DUE_DATE_OVERRIDE),
      repaymentDueOverride);
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(CANCELLATION_STATUS),
      new CodeTableItem(BDMOASCANCELSTATUS.TABLENAME, cancellationStatus));

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
