package curam.ca.gc.bdmoas.evidence.eligibilityentitlementoverride.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdmoas.codetable.BDMOASAPPLICATIONSTATUS;
import curam.ca.gc.bdmoas.codetable.BDMOASBENEFITTYPE;
import curam.ca.gc.bdmoas.codetable.BDMOASMETHODOFAPPLICATION;
import curam.ca.gc.bdmoas.codetable.BDMOASOVERRIDEBENEFITTYPE;
import curam.ca.gc.bdmoas.codetable.BDMOASOVERRIDEREASON;
import curam.ca.gc.bdmoas.codetable.BDMOASOVERRIDEVALUE;
import curam.ca.gc.bdmoas.evidence.constants.impl.BDMOASApplicationDetailsConstants;
import curam.ca.gc.bdmoas.evidence.constants.impl.BDMOASEligibilityEntitlementOverrideConstants;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.commonintake.entity.struct.ApplicationCaseKey;
import curam.core.facade.fact.CaseFactory;
import curam.core.facade.infrastructure.fact.EvidenceFactory;
import curam.core.facade.infrastructure.intf.Evidence;
import curam.core.facade.intf.Case;
import curam.core.facade.struct.AddClientRoleDetails;
import curam.core.facade.struct.CreateIntegratedCaseResult;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.core.struct.CaseKey;
import curam.creole.execution.session.InterpretedRuleObjectFactory;
import curam.creole.execution.session.ManualRecalculation;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.storage.inmemory.InMemoryDataStorage;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for dynamic evidence BDMOASEligibilityEntitlementOverride
 */
public class BDMOASEligibilityEntitlementOverrideValidationTest
  extends BDMOASCaseTest {

  private Session session;

  @Override
  protected void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    this.session =
      Session_Factory.getFactory().newInstance(new ManualRecalculation(),
        new InMemoryDataStorage(new InterpretedRuleObjectFactory()));

  }

  /**
   * Test that an override cannot be created for a non-primary member on an OAS
   * IC.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void primaryPersonFailure()
    throws AppException, InformationalException {

    final String validationMessage =
      "A record cannot exist for a client who is not the primary participant on this case.";

    final PersonRegistrationResult primary = this.registerPerson();
    final PersonRegistrationResult member = this.registerPerson();

    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(primary.registrationIDDetails.concernRoleID);

    final Case caseObj = CaseFactory.newInstance();
    final AddClientRoleDetails clientRoleDetails = new AddClientRoleDetails();
    clientRoleDetails.caseID = integratedCase.integratedCaseID;
    clientRoleDetails.concernRoleID =
      member.registrationIDDetails.concernRoleID;
    clientRoleDetails.startDate = getToday();

    caseObj.createCaseMember(clientRoleDetails);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        member.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.PARTICIPANT,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.BENEFIT_TYPE,
      BDMOASOVERRIDEBENEFITTYPE.ALL_BENEFITS);
    attributes.put(
      BDMOASEligibilityEntitlementOverrideConstants.OVERRIDE_VALUE,
      BDMOASOVERRIDEVALUE.COUNTRY_OF_RESIDENCE);
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.REASON,
      BDMOASOVERRIDEREASON.MINISTER);

    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(integratedCase.integratedCaseID,
          member.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_ELG_ENTITLEMNT_OVERRIDE, attributes,
          getToday());
      });

    assertTrue(validationMessage.equals(exception.getMessage()));

  }

  /**
   * Test that an override can be created for a non-primary member on an OAS
   * AC.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void memberACPass() throws AppException, InformationalException {

    final PersonRegistrationResult primary = this.registerPerson();
    final PersonRegistrationResult member = this.registerPerson();

    final ApplicationCaseKey applicationCaseKey = this.createApplicationCase(
      primary.registrationIDDetails.concernRoleID, 920000);

    final Case caseObj = CaseFactory.newInstance();
    final AddClientRoleDetails clientRoleDetails = new AddClientRoleDetails();
    clientRoleDetails.caseID = applicationCaseKey.applicationCaseID;
    clientRoleDetails.concernRoleID =
      member.registrationIDDetails.concernRoleID;
    clientRoleDetails.startDate = getToday();

    caseObj.createCaseMember(clientRoleDetails);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(applicationCaseKey.applicationCaseID,
        member.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.PARTICIPANT,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.BENEFIT_TYPE,
      BDMOASOVERRIDEBENEFITTYPE.ALL_BENEFITS);
    attributes.put(
      BDMOASEligibilityEntitlementOverrideConstants.OVERRIDE_VALUE,
      BDMOASOVERRIDEVALUE.COUNTRY_OF_RESIDENCE);
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.REASON,
      BDMOASOVERRIDEREASON.MINISTER);
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.START_DATE,
      formattedDateMovedToFirstOfMonth(Date.getCurrentDate()));

    this.createEvidence(applicationCaseKey.applicationCaseID,
      member.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.OAS_ELG_ENTITLEMNT_OVERRIDE, attributes, getToday());

  }

  @Test
  public void applicaionDetailsValidationFailureNoEvidence()
    throws AppException, InformationalException {

    final String validationMessage =
      "An override cannot exist as Testington Bear is not an applicant for 'Old Age Security Pension'.";

    final PersonRegistrationResult participant = this.registerPerson();

    final CreateIntegratedCaseResult integratedCase = this
      .createIntegratedCase(participant.registrationIDDetails.concernRoleID);

    final long caseID = integratedCase.integratedCaseID;
    final long concernRoleID =
      participant.registrationIDDetails.concernRoleID;

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(caseID, concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.PARTICIPANT,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.BENEFIT_TYPE,
      BDMOASOVERRIDEBENEFITTYPE.OLD_AGE_SECURITY_PENSION);
    attributes.put(
      BDMOASEligibilityEntitlementOverrideConstants.OVERRIDE_VALUE,
      BDMOASOVERRIDEVALUE.IS_ELIGIBLE);
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.REASON,
      BDMOASOVERRIDEREASON.MINISTER);

    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(caseID, concernRoleID,
          CASEEVIDENCEEntry.OAS_ELG_ENTITLEMNT_OVERRIDE, attributes,
          getToday());
      });

    assertTrue(validationMessage.equals(exception.getMessage()));

  }

  @Test
  public void applicaionDetailsValidationFailureIncorrectEvidence()
    throws AppException, InformationalException {

    final String validationMessage =
      "An override cannot exist as Testington Bear is not an applicant for 'Old Age Security Pension'.";

    final PersonRegistrationResult participant = this.registerPerson();

    final CreateIntegratedCaseResult integratedCase = this
      .createIntegratedCase(participant.registrationIDDetails.concernRoleID);

    final long caseID = integratedCase.integratedCaseID;
    final long concernRoleID =
      participant.registrationIDDetails.concernRoleID;

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(caseID, concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.PARTICIPANT,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.BENEFIT_TYPE,
      BDMOASOVERRIDEBENEFITTYPE.OLD_AGE_SECURITY_PENSION);
    attributes.put(
      BDMOASEligibilityEntitlementOverrideConstants.OVERRIDE_VALUE,
      BDMOASOVERRIDEVALUE.IS_ELIGIBLE);
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.REASON,
      BDMOASOVERRIDEREASON.MINISTER);

    final Map<String, String> applicationDetailsAttributes =
      new HashMap<String, String>();
    applicationDetailsAttributes.put(
      BDMOASApplicationDetailsConstants.BENEFIT_TYPE,
      BDMOASBENEFITTYPE.GUARANTEED_INCOME_SUPPLEMENT);
    applicationDetailsAttributes.put(
      BDMOASApplicationDetailsConstants.APPLICATION_STATUS,
      BDMOASAPPLICATIONSTATUS.ACTIVE);
    applicationDetailsAttributes.put(
      BDMOASApplicationDetailsConstants.METHOD_OF_APPLICATION,
      BDMOASMETHODOFAPPLICATION.ONLINE);
    applicationDetailsAttributes
      .put(BDMOASApplicationDetailsConstants.RECEIPT_DATE, "20210101");

    this.createEvidence(caseID, concernRoleID,
      CASEEVIDENCEEntry.OAS_APPLICATION_DETAILS, applicationDetailsAttributes,
      getToday());

    final Evidence evidenceObj = EvidenceFactory.newInstance();
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = caseID;
    evidenceObj.applyAllChanges(caseKey);

    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(caseID, concernRoleID,
          CASEEVIDENCEEntry.OAS_ELG_ENTITLEMNT_OVERRIDE, attributes,
          getToday());
      });

    assertTrue(validationMessage.equals(exception.getMessage()));

  }

  @Test
  public void applicationDetailsValidationPass()
    throws AppException, InformationalException {

    final PersonRegistrationResult participant = this.registerPerson();

    final CreateIntegratedCaseResult integratedCase = this
      .createIntegratedCase(participant.registrationIDDetails.concernRoleID);

    final long caseID = integratedCase.integratedCaseID;
    final long concernRoleID =
      participant.registrationIDDetails.concernRoleID;

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(caseID, concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.PARTICIPANT,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.BENEFIT_TYPE,
      BDMOASOVERRIDEBENEFITTYPE.OLD_AGE_SECURITY_PENSION);
    attributes.put(
      BDMOASEligibilityEntitlementOverrideConstants.OVERRIDE_VALUE,
      BDMOASOVERRIDEVALUE.IS_ELIGIBLE);
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.REASON,
      BDMOASOVERRIDEREASON.MINISTER);
    attributes.put(BDMOASEligibilityEntitlementOverrideConstants.START_DATE,
      formattedDateMovedToFirstOfMonth(Date.getCurrentDate()));

    final Map<String, String> applicationDetailsAttributes =
      new HashMap<String, String>();
    applicationDetailsAttributes.put("caseParticipantRole",
      String.valueOf(cprObj.caseParticipantRoleID));
    applicationDetailsAttributes.put(
      BDMOASApplicationDetailsConstants.BENEFIT_TYPE,
      BDMOASBENEFITTYPE.OAS_PENSION);
    applicationDetailsAttributes.put(
      BDMOASApplicationDetailsConstants.APPLICATION_STATUS,
      BDMOASAPPLICATIONSTATUS.ACTIVE);
    applicationDetailsAttributes.put(
      BDMOASApplicationDetailsConstants.METHOD_OF_APPLICATION,
      BDMOASMETHODOFAPPLICATION.ONLINE);
    applicationDetailsAttributes
      .put(BDMOASApplicationDetailsConstants.RECEIPT_DATE, "20210101");

    this.createEvidence(caseID, concernRoleID,
      CASEEVIDENCEEntry.OAS_APPLICATION_DETAILS, applicationDetailsAttributes,
      getToday());

    final Evidence evidenceObj = EvidenceFactory.newInstance();
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = caseID;
    evidenceObj.applyAllChanges(caseKey);

    this.createEvidence(caseID, concernRoleID,
      CASEEVIDENCEEntry.OAS_ELG_ENTITLEMNT_OVERRIDE, attributes, getToday());

  }

  private String formattedDateMovedToFirstOfMonth(final Date date) {

    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    // move date to the first of the month
    final Calendar cal = date.getCalendar();
    cal.set(Calendar.DAY_OF_MONTH, 1);
    final Date firstOfMonthDate = new Date(cal);

    return dateFormat.format(firstOfMonthDate.getCalendar().getTime());
  }

}
