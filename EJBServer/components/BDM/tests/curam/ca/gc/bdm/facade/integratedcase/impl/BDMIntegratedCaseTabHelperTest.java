package curam.ca.gc.bdm.facade.integratedcase.impl;

import curam.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.codetable.BDMURGENTFLAGTYPE;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.fact.BDMCaseUrgentFlagFactory;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.struct.BDMCaseUrgentFlagDetails;
import curam.ca.gc.bdm.facade.fec.fact.BDMForeignEngagementCaseFactory;
import curam.ca.gc.bdm.facade.fec.struct.BDMFECaseDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMPersonRegistrationDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.test.facade.urgentflag.impl.BDMUrgentFlagHelper;
import curam.codetable.CASEPARTICIPANTROLETYPE;
import curam.codetable.CASEPRIORITY;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.PROVINCETYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.facade.fact.PersonFactory;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.facade.struct.CreateIntegratedCaseResultAndMessages;
import curam.core.facade.struct.PersonRegistrationDetails;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.sl.entity.fact.CaseParticipantRoleFactory;
import curam.core.sl.entity.struct.CaseParticipantRoleDtls;
import curam.core.sl.struct.CaseIDKey;
import curam.core.sl.struct.IntegratedCaseTabDetail;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.OtherAddressData;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.UniqueID;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BDMIntegratedCaseTabHelperTest extends CuramServerTestJUnit4 {

  BDMIntegratedCaseTabHelper helper = null;

  BDMUrgentFlagHelper urgentFlagHelper = null;

  private static String FEC_CASE = "Foreign Engagement Case";

  @Before
  public void setUp() throws AppException, InformationalException {

    helper = new BDMIntegratedCaseTabHelper();
    urgentFlagHelper = new BDMUrgentFlagHelper();
  }

  /**
   * Test Read Integrated Case Tab Detail ,contains unmasked SIN
   */
  @Test
  public void testReadIntegratedCaseTabDetail()
    throws AppException, InformationalException {

    final PersonRegistrationResult personRegistrationResult =
      registerAPersonForTest("451775399");
    final CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      createFEC(personRegistrationResult.registrationIDDetails.concernRoleID);

    setUpUrgentFlagData(
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID,
      BDMURGENTFLAGTYPE.MPENQUIRY, RECORDSTATUS.NORMAL, Date.getCurrentDate(),
      Date.kZeroDate);

    final CaseIDKey key = new CaseIDKey();

    key.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;

    final IntegratedCaseTabDetail integratedCaseTabDetail =
      helper.readIntegratedCaseTabDetail(key);

    assertEquals(FEC_CASE, integratedCaseTabDetail.caseType);
    assertEquals("- John Doe",
      integratedCaseTabDetail.conditionalFormedPersonNameOpt.trim());

  }

  @Test
  public void testReadIntegratedCaseTabDetail_Multiple()
    throws AppException, InformationalException {

    final PersonRegistrationResult personRegistrationResult =
      registerAPersonForTest("372245340");

    final PersonRegistrationResult caseMember =
      registerAPersonForTest("235947322");
    final CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      createFEC(personRegistrationResult.registrationIDDetails.concernRoleID);

    createCPR(
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID,
      caseMember.registrationIDDetails.concernRoleID);

    setUpUrgentFlagData(
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID,
      BDMURGENTFLAGTYPE.MPENQUIRY, RECORDSTATUS.NORMAL, Date.getCurrentDate(),
      Date.kZeroDate);

    final CaseIDKey key = new CaseIDKey();

    key.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;

    final IntegratedCaseTabDetail integratedCaseTabDetail =
      helper.readIntegratedCaseTabDetail(key);

    assertEquals(FEC_CASE, integratedCaseTabDetail.caseType);
    assertEquals("- John Doe",
      integratedCaseTabDetail.conditionalFormedPersonNameOpt.trim());
    // Check Integrated context panel has person name only when multiple case
    // members
    assertTrue(integratedCaseTabDetail.xmlPanelData.contains("John Doe"));

  }

  /**
   * Creates a case participant role Case Member
   */
  public long createCPR(final long caseID, final long concernRoleID)
    throws AppException, InformationalException {

    // create the CPR
    final CaseParticipantRoleDtls caseParticipantRoleDtls =
      new CaseParticipantRoleDtls();
    caseParticipantRoleDtls.caseID = caseID;
    caseParticipantRoleDtls.caseParticipantRoleID = UniqueID.nextUniqueID();

    caseParticipantRoleDtls.participantRoleID = concernRoleID;
    caseParticipantRoleDtls.fromDate = Date.getCurrentDate();
    caseParticipantRoleDtls.versionNo = 1;
    caseParticipantRoleDtls.recordStatus = RECORDSTATUS.DEFAULTCODE;
    caseParticipantRoleDtls.typeCode = CASEPARTICIPANTROLETYPE.MEMBER;
    CaseParticipantRoleFactory.newInstance().insert(caseParticipantRoleDtls);

    return caseParticipantRoleDtls.caseParticipantRoleID;
  }

  /** Set up Urgent Flg details */
  public BDMCaseUrgentFlagDetails setUpUrgentFlagData(final long caseID,
    final String urgentFlagType, final String recordStatus,
    final Date startDate, final Date endDate)
    throws AppException, InformationalException {

    final BDMCaseUrgentFlagDetails flagDtls = new BDMCaseUrgentFlagDetails();
    flagDtls.bdmCaseUrgentFlagID = UniqueID.nextUniqueID();
    flagDtls.caseID = caseID;
    TransactionInfo.getInfo();
    flagDtls.createdBy = TransactionInfo.getProgramUser();
    TransactionInfo.getInfo();
    flagDtls.createdByFullName = TransactionInfo.getProgramUser();
    flagDtls.recordStatus = recordStatus;
    flagDtls.startDate = startDate;
    flagDtls.endDate = endDate;
    flagDtls.type = urgentFlagType;
    BDMCaseUrgentFlagFactory.newInstance().createCaseUrgentFlag(flagDtls);
    return flagDtls;
  }

  /** Set up data for FEC case */
  private CreateIntegratedCaseResultAndMessages createFEC(
    final long concernRoleID) throws AppException, InformationalException {

    // BEGIN: create FE IC for the person
    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      new CreateIntegratedCaseResultAndMessages();

    final BDMFECaseDetails details = new BDMFECaseDetails();
    details.concernRoleID = concernRoleID;

    details.countryCode = BDMSOURCECOUNTRY.IRELAND;

    details.priorityCode = CASEPRIORITY.HIGH;

    createIntegratedCaseResultAndMessages = BDMForeignEngagementCaseFactory
      .newInstance().createFEIntegratedCase(details);
    // END: create FE IC for the person

    return createIntegratedCaseResultAndMessages;
  }

  /**
   * This is the utility method to register person for the test class.
   *
   * @throws AppException
   * @throws InformationalException
   */
  private PersonRegistrationResult registerAPersonForTest(
    final String nineDigitSIN) throws AppException, InformationalException {

    // BEGIN: REGISTER PERSON
    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = "Save";

    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "John";
    bdmPersonRegistrationDetails.dtls.surname = "Doe";
    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19770102");
    bdmPersonRegistrationDetails.dtls.socialSecurityNumber = nineDigitSIN;
    bdmPersonRegistrationDetails.dtls.alternateIDTypeCodeOpt =
      CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER;

    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;

    addressFieldDetails.suiteNum = "207";
    addressFieldDetails.addressLine1 = "6511";
    addressFieldDetails.addressLine2 = "GILBER RD";
    addressFieldDetails.province = PROVINCETYPE.BRITISHCOLUMBIA;
    addressFieldDetails.city = "RICHMOND";
    addressFieldDetails.postalCode = "V7C 3V9";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;

    OtherAddressData otherAddressData = null;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    bdmPersonRegistrationDetails.dtls.addressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.mailingAddressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.registrationDate =
      Date.getCurrentDate();

    final PersonRegistrationDetails details = new PersonRegistrationDetails();
    details.personRegistrationDetails = bdmPersonRegistrationDetails.dtls;

    final PersonRegistrationResult registrationResult =
      PersonFactory.newInstance().register(details);

    final BDMUtil bdmUtil = new BDMUtil();
    bdmUtil.createContactPreferenceEvidence(
      registrationResult.registrationIDDetails.concernRoleID,
      BDMLANGUAGE.ENGLISHL, BDMLANGUAGE.ENGLISHL, BDMConstants.EMPTY_STRING);

    return registrationResult;

  }

}
