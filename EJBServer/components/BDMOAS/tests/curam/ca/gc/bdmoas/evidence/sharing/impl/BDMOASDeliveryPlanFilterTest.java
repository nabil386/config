package curam.ca.gc.bdmoas.evidence.sharing.impl;

import curam.aes.sl.entity.struct.AESDeliveryPlanDtls;
import curam.aes.sl.entity.struct.AESShareItemDtls;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdmoas.codetable.BDMOASAPPLICATIONSTATUS;
import curam.ca.gc.bdmoas.codetable.BDMOASBENEFITTYPE;
import curam.ca.gc.bdmoas.codetable.BDMOASMETHODOFAPPLICATION;
import curam.ca.gc.bdmoas.evidence.constants.impl.BDMOASApplicationDetailsConstants;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.ca.gc.bdmoas.impl.BDMOASConstants;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.commonintake.entity.struct.ApplicationCaseKey;
import curam.commonintake.facade.fact.ApplicationCaseFactory;
import curam.commonintake.facade.struct.ApplicationCaseProgramDetails;
import curam.core.facade.struct.CreateIntegratedCaseResult;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.CaseIDKey;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import curam.workspaceservices.codetable.IntakeProgramApplicationStatus;
import curam.workspaceservices.intake.fact.IntakeProgramAppCaseLinkFactory;
import curam.workspaceservices.intake.fact.IntakeProgramApplicationFactory;
import curam.workspaceservices.intake.intf.IntakeProgramAppCaseLink;
import curam.workspaceservices.intake.intf.IntakeProgramApplication;
import curam.workspaceservices.intake.struct.IntakeProgramAppCaseLinkDtls;
import curam.workspaceservices.intake.struct.IntakeProgramAppCaseLinkDtlsList;
import curam.workspaceservices.intake.struct.IntakeProgramApplicationDtls;
import curam.workspaceservices.intake.struct.IntakeProgramApplicationKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Class to test Advanced Evidence Sharing filter implementation
 *
 */
@RunWith(JMockit.class)
public class BDMOASDeliveryPlanFilterTest extends BDMOASCaseTest {

  @Tested
  BDMOASAESDeliveryPlanFilterImpl aesDeliveryPlanFilterObj;

  @Override
  protected void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    aesDeliveryPlanFilterObj = new BDMOASAESDeliveryPlanFilterImpl();
  }

  /**
   * Test application details evidence benefit type matches authorization
   * program for OAS.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testAppDetailsEvdBenefitTypeMatchesAuthProgramForOAS()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();

    // Submit OAS/GIS Application
    final ApplicationCaseKey applicationCaseKey = this.createApplicationCase(
      person.registrationIDDetails.concernRoleID, 920000);

    // Add OASPension Benefit
    final ApplicationCaseProgramDetails oasProgramDetails =
      new ApplicationCaseProgramDetails();
    oasProgramDetails.applicationCaseID =
      applicationCaseKey.applicationCaseID;
    oasProgramDetails.dateProgramAdded = getToday();
    oasProgramDetails.programTypeID =
      BDMOASConstants.OAS_PENSION_PROGRAM_TYPE_ID;
    ApplicationCaseFactory.newInstance().addProgram(oasProgramDetails);

    // Add Application Details Evidence with OAS Benefit
    final EIEvidenceKey appDetailsEvdKey = createApplicationDetailsEvidence(
      person.registrationIDDetails.concernRoleID,
      applicationCaseKey.applicationCaseID, BDMOASBENEFITTYPE.OAS_PENSION);

    // Update the program status to "authorization in progress"
    final IntakeProgramAppCaseLink appCaseLink =
      IntakeProgramAppCaseLinkFactory.newInstance();
    final CaseIDKey caseIDKey = new CaseIDKey();
    caseIDKey.caseID = applicationCaseKey.applicationCaseID;

    final IntakeProgramAppCaseLinkDtlsList dtlsList =
      appCaseLink.searchByCaseID(caseIDKey);

    final IntakeProgramApplication application =
      IntakeProgramApplicationFactory.newInstance();
    final IntakeProgramApplicationKey appKey =
      new IntakeProgramApplicationKey();

    for (final IntakeProgramAppCaseLinkDtls dtls : dtlsList.dtls) {

      appKey.intakeProgramApplicationID = dtls.intakeProgramApplicationID;
      final IntakeProgramApplicationDtls programAppDtls =
        application.read(appKey);
      programAppDtls.status =
        IntakeProgramApplicationStatus.AUTHORIZATIONINPROGRESS;

      application.modify(appKey, programAppDtls);
    }

    final BDMOASAESDeliveryPlanFilterHelper filterHelper =
      new BDMOASAESDeliveryPlanFilterHelper();

    assertTrue(filterHelper.isAppDetailsBenefitTypeForAuthProgram(
      appDetailsEvdKey.evidenceID, applicationCaseKey.applicationCaseID));

  }

  /**
   * Test application details evidence benefit type matches authorization
   * program for GIS.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testAppDetailsEvdBenefitTypeMatchesAuthProgramForGIS()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();

    // Submit OAS/GIS Application
    final ApplicationCaseKey applicationCaseKey = this.createApplicationCase(
      person.registrationIDDetails.concernRoleID, 920000);

    // Add OASPension Benefit
    final ApplicationCaseProgramDetails oasProgramDetails =
      new ApplicationCaseProgramDetails();
    oasProgramDetails.applicationCaseID =
      applicationCaseKey.applicationCaseID;
    oasProgramDetails.dateProgramAdded = getToday();
    oasProgramDetails.programTypeID = BDMOASConstants.GIS_PROGRAM_TYPE_ID;
    ApplicationCaseFactory.newInstance().addProgram(oasProgramDetails);

    // Add Application Details Evidence with GIS Benefit
    final EIEvidenceKey appDetailsEvdKey = createApplicationDetailsEvidence(
      person.registrationIDDetails.concernRoleID,
      applicationCaseKey.applicationCaseID,
      BDMOASBENEFITTYPE.GUARANTEED_INCOME_SUPPLEMENT);

    // Update the program status to "authorization in progress"
    final IntakeProgramAppCaseLink appCaseLink =
      IntakeProgramAppCaseLinkFactory.newInstance();
    final CaseIDKey caseIDKey = new CaseIDKey();
    caseIDKey.caseID = applicationCaseKey.applicationCaseID;

    final IntakeProgramAppCaseLinkDtlsList dtlsList =
      appCaseLink.searchByCaseID(caseIDKey);

    final IntakeProgramApplication application =
      IntakeProgramApplicationFactory.newInstance();
    final IntakeProgramApplicationKey appKey =
      new IntakeProgramApplicationKey();

    for (final IntakeProgramAppCaseLinkDtls dtls : dtlsList.dtls) {

      appKey.intakeProgramApplicationID = dtls.intakeProgramApplicationID;
      final IntakeProgramApplicationDtls programAppDtls =
        application.read(appKey);
      programAppDtls.status =
        IntakeProgramApplicationStatus.AUTHORIZATIONINPROGRESS;

      application.modify(appKey, programAppDtls);
    }

    final BDMOASAESDeliveryPlanFilterHelper filterHelper =
      new BDMOASAESDeliveryPlanFilterHelper();

    assertTrue(filterHelper.isAppDetailsBenefitTypeForAuthProgram(
      appDetailsEvdKey.evidenceID, applicationCaseKey.applicationCaseID));

  }

  /**
   * Test application details evidence benefit type OAS does not match GIS
   * authorization
   * program.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testAppDetailsEvdBenefitTypeDoesNotMatchGisAuthProgram()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();

    // Submit OAS/GIS Application
    final ApplicationCaseKey applicationCaseKey = this.createApplicationCase(
      person.registrationIDDetails.concernRoleID, 920000);

    // Add OASPension Benefit
    final ApplicationCaseProgramDetails oasProgramDetails =
      new ApplicationCaseProgramDetails();
    oasProgramDetails.applicationCaseID =
      applicationCaseKey.applicationCaseID;
    oasProgramDetails.dateProgramAdded = getToday();
    oasProgramDetails.programTypeID = BDMOASConstants.GIS_PROGRAM_TYPE_ID;
    ApplicationCaseFactory.newInstance().addProgram(oasProgramDetails);

    // Add Application Details Evidence with OAS Benefit
    final EIEvidenceKey appDetailsEvdKey = createApplicationDetailsEvidence(
      person.registrationIDDetails.concernRoleID,
      applicationCaseKey.applicationCaseID, BDMOASBENEFITTYPE.OAS_PENSION);

    // Update the program status to "authorization in progress"
    final IntakeProgramAppCaseLink appCaseLink =
      IntakeProgramAppCaseLinkFactory.newInstance();
    final CaseIDKey caseIDKey = new CaseIDKey();
    caseIDKey.caseID = applicationCaseKey.applicationCaseID;

    final IntakeProgramAppCaseLinkDtlsList dtlsList =
      appCaseLink.searchByCaseID(caseIDKey);

    final IntakeProgramApplication application =
      IntakeProgramApplicationFactory.newInstance();
    final IntakeProgramApplicationKey appKey =
      new IntakeProgramApplicationKey();

    for (final IntakeProgramAppCaseLinkDtls dtls : dtlsList.dtls) {

      appKey.intakeProgramApplicationID = dtls.intakeProgramApplicationID;
      final IntakeProgramApplicationDtls programAppDtls =
        application.read(appKey);
      programAppDtls.status =
        IntakeProgramApplicationStatus.AUTHORIZATIONINPROGRESS;

      application.modify(appKey, programAppDtls);
    }

    final BDMOASAESDeliveryPlanFilterHelper filterHelper =
      new BDMOASAESDeliveryPlanFilterHelper();

    assertFalse(filterHelper.isAppDetailsBenefitTypeForAuthProgram(
      appDetailsEvdKey.evidenceID, applicationCaseKey.applicationCaseID));

  }

  /**
   * Tests BDMOASDeliveryPlanFilterHelper class
   * shouldIgnoreSharingBtwnSourceOasGisAppCaseTargetOasIntCase method.
   * The evidence FirstEntrlyIntoCanadaDetails should be shared between
   * OAS Application Case and OAS Integrated Case when the concernrole is
   * primary participant in the OAS IntegratedCase.
   * So, should ignore sharing method should return false.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testShouldIgnoreSharingForFirstEntryIntoCanadaDetails()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();

    // Submit OAS/GIS Application
    final ApplicationCaseKey applicationCaseKey = this.createApplicationCase(
      person.registrationIDDetails.concernRoleID, 920000);

    // Create OAS Integrated Case
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMOASAESDeliveryPlanFilterHelper filterHelper =
      new BDMOASAESDeliveryPlanFilterHelper();

    assertFalse(
      filterHelper.shouldIgnoreSharingBtwnSourceOasGisAppCaseTargetOasIntCase(
        person.registrationIDDetails.concernRoleID,
        applicationCaseKey.applicationCaseID, integratedCase.integratedCaseID,
        0l, CASEEVIDENCE.OAS_FIRST_ENTRY_INTO_CANADA));
  }

  /**
   * Tests BDMOASDeliveryPlanFilterHelper class
   * shouldIgnoreSharingBtwnSourceOasGisAppCaseTargetOasIntCase method.
   * The evidence ApplicationDetails should be shared between
   * OAS Application Case and OAS Integrated Case when the concernrole is
   * primary participant in the OAS IntegratedCase but since Application Details
   * Evidence is for benefit type of OAS Pension but the authorization is for
   * GIS, the sharing should be ignored.
   * So, should ignore sharing method should return true.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testShouldIgnoreSharingForApplicationDetails()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();

    // Submit OAS/GIS Application
    final ApplicationCaseKey applicationCaseKey = this.createApplicationCase(
      person.registrationIDDetails.concernRoleID, 920000);

    // Add GIS Benefit
    final ApplicationCaseProgramDetails oasProgramDetails =
      new ApplicationCaseProgramDetails();
    oasProgramDetails.applicationCaseID =
      applicationCaseKey.applicationCaseID;
    oasProgramDetails.dateProgramAdded = getToday();
    oasProgramDetails.programTypeID = BDMOASConstants.GIS_PROGRAM_TYPE_ID;
    ApplicationCaseFactory.newInstance().addProgram(oasProgramDetails);

    // Add Application Details Evidence for OAS Pension
    final EIEvidenceKey appDetailsEvdKey = createApplicationDetailsEvidence(
      person.registrationIDDetails.concernRoleID,
      applicationCaseKey.applicationCaseID, BDMOASBENEFITTYPE.OAS_PENSION);

    // Update the program status to "authorization in progress"
    final IntakeProgramAppCaseLink appCaseLink =
      IntakeProgramAppCaseLinkFactory.newInstance();
    final CaseIDKey caseIDKey = new CaseIDKey();
    caseIDKey.caseID = applicationCaseKey.applicationCaseID;

    final IntakeProgramAppCaseLinkDtlsList dtlsList =
      appCaseLink.searchByCaseID(caseIDKey);

    final IntakeProgramApplication application =
      IntakeProgramApplicationFactory.newInstance();
    final IntakeProgramApplicationKey appKey =
      new IntakeProgramApplicationKey();

    for (final IntakeProgramAppCaseLinkDtls dtls : dtlsList.dtls) {

      appKey.intakeProgramApplicationID = dtls.intakeProgramApplicationID;
      final IntakeProgramApplicationDtls programAppDtls =
        application.read(appKey);
      programAppDtls.status =
        IntakeProgramApplicationStatus.AUTHORIZATIONINPROGRESS;

      application.modify(appKey, programAppDtls);
    }

    // Create OAS Integrated Case
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMOASAESDeliveryPlanFilterHelper filterHelper =
      new BDMOASAESDeliveryPlanFilterHelper();

    assertTrue(
      filterHelper.shouldIgnoreSharingBtwnSourceOasGisAppCaseTargetOasIntCase(
        person.registrationIDDetails.concernRoleID,
        applicationCaseKey.applicationCaseID, integratedCase.integratedCaseID,
        appDetailsEvdKey.evidenceID, CASEEVIDENCE.OAS_APPLICATION_DETAILS));
  }

  /**
   * BDMOASAESDeliveryPlanFilter class filter method return list should contain
   * the delivery plan object for the evidence determined to be ignored by the
   * helper class method
   * shouldIgnoreSharingBtwnSourceOasGisAppCaseTargetOasIntCase.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testAESDeliveryPlanFilter(@Mocked
  final BDMOASAESDeliveryPlanFilterHelper filterHelperObj)
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();

    // Submit OAS/GIS Application
    final ApplicationCaseKey applicationCaseKey = this.createApplicationCase(
      person.registrationIDDetails.concernRoleID, 920000);

    // Create OAS Integrated Case
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final AESDeliveryPlanDtls deliveryPlanDtls = new AESDeliveryPlanDtls();
    deliveryPlanDtls.targetCaseID = integratedCase.integratedCaseID;
    deliveryPlanDtls.shareItemID = 123l;

    final List<AESDeliveryPlanDtls> deliveryPlanDtlsList = new ArrayList<>();
    deliveryPlanDtlsList.add(deliveryPlanDtls);

    final AESShareItemDtls shareItemDtls = new AESShareItemDtls();
    shareItemDtls.sourceCaseID = applicationCaseKey.applicationCaseID;

    new Expectations() {

      {
        filterHelperObj.getAESShareItemDtlsByShareItemID(anyLong);
        result = shareItemDtls;
        filterHelperObj
          .shouldIgnoreSharingBtwnSourceOasGisAppCaseTargetOasIntCase(anyLong,
            anyLong, anyLong, anyLong, anyString);
        result = true;
      }
    };

    final List<AESDeliveryPlanDtls> ignoredDeliveryPlanList =
      aesDeliveryPlanFilterObj.filter(deliveryPlanDtlsList);

    assertEquals(deliveryPlanDtls, ignoredDeliveryPlanList.get(0));
  }

  /**
   * Creates the application details evidence.
   *
   * @param concernRoleID the concern role ID
   * @param caseID the case ID
   * @param benefitType the benefit type
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  private EIEvidenceKey createApplicationDetailsEvidence(
    final Long concernRoleID, final Long caseID, final String benefitType)
    throws AppException, InformationalException {

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(caseID, concernRoleID);

    final Map<String, String> applicationDetailsAttributes =
      new HashMap<String, String>();
    applicationDetailsAttributes.put("caseParticipantRole",
      String.valueOf(cprObj.caseParticipantRoleID));
    applicationDetailsAttributes
      .put(BDMOASApplicationDetailsConstants.BENEFIT_TYPE, benefitType);
    applicationDetailsAttributes.put(
      BDMOASApplicationDetailsConstants.APPLICATION_STATUS,
      BDMOASAPPLICATIONSTATUS.ACTIVE);
    applicationDetailsAttributes.put(
      BDMOASApplicationDetailsConstants.METHOD_OF_APPLICATION,
      BDMOASMETHODOFAPPLICATION.ONLINE);
    applicationDetailsAttributes.put(
      BDMOASApplicationDetailsConstants.RECEIPT_DATE,
      formattedDateMovedToFirstOfMonth(Date.getCurrentDate()));

    return this.createEvidence(caseID, concernRoleID,
      CASEEVIDENCEEntry.OAS_APPLICATION_DETAILS, applicationDetailsAttributes,
      getToday());
  }

  /**
   * Formatted date moved to first of month.
   *
   * @param date the date
   * @return the string
   */
  private String formattedDateMovedToFirstOfMonth(final Date date) {

    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    // move date to the first of the month
    final Calendar cal = date.getCalendar();
    cal.set(Calendar.DAY_OF_MONTH, 1);
    final Date firstOfMonthDate = new Date(cal);

    return dateFormat.format(firstOfMonthDate.getCalendar().getTime());
  }
}
