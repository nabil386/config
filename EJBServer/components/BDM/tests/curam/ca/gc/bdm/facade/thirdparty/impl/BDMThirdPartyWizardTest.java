package curam.ca.gc.bdm.facade.thirdparty.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMMODEOFRECEIPT;
import curam.ca.gc.bdm.codetable.BDMPHONECOUNTRY;
import curam.ca.gc.bdm.codetable.BDMRECEIVEDFROM;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.codetable.BDMTHIRDPARTYROLE;
import curam.ca.gc.bdm.codetable.BDMTHIRDPARTYROLETYPE;
import curam.ca.gc.bdm.facade.externalparty.fact.BDMExternalPartyFactory;
import curam.ca.gc.bdm.facade.externalparty.intf.BDMExternalParty;
import curam.ca.gc.bdm.facade.thirdparty.fact.BDMThirdPartyWizardFactory;
import curam.ca.gc.bdm.facade.thirdparty.struct.BDMThirdPartyProgramCodeDetailsList;
import curam.ca.gc.bdm.facade.thirdparty.struct.IndividualsWithinOrgDetails;
import curam.ca.gc.bdm.facade.thirdparty.struct.OfficeDetailsTab;
import curam.ca.gc.bdm.facade.thirdparty.struct.ThirdPartyAdditionalDetails;
import curam.ca.gc.bdm.facade.thirdparty.struct.ThirdPartyDetails;
import curam.ca.gc.bdm.facade.thirdparty.struct.ThirdPartyEvidenceWizardDetailsResult;
import curam.ca.gc.bdm.facade.thirdparty.struct.ThirdPartyEvidenceWizardKey;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMTHIRDPARTYCONTACT;
import curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.CONCERNROLETYPE;
import curam.codetable.EMAILTYPE;
import curam.codetable.LANGUAGE;
import curam.codetable.PHONETYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.facade.struct.InformationMsgDtlsList;
import curam.core.fact.ConcernRoleAddressFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.sl.fact.CaseParticipantRoleFactory;
import curam.core.sl.infrastructure.impl.ValidationManagerFactory;
import curam.core.sl.intf.CaseParticipantRole;
import curam.core.sl.struct.CaseIDAndParticipantRoleIDKey;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.core.sl.struct.ExternalPartyOfficeDetails;
import curam.core.struct.AddressConcernRoleTypeStatusDtlsList;
import curam.core.struct.AddressConcernRoleTypeStatusKey;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.MaintainAddressKey;
import curam.core.struct.OtherAddressData;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement.InformationalType;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.UniqueID;
import curam.wizardpersistence.entity.base.WizardState;
import curam.wizardpersistence.entity.struct.WizardStateDtls;
import curam.wizardpersistence.entity.struct.WizardStateKey;
import curam.wizardpersistence.impl.WizardPersistentState;
import jdk.nashorn.internal.ir.annotations.Ignore;
import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)
@Ignore
public class BDMThirdPartyWizardTest extends CuramServerTestJUnit4 {

  public BDMThirdPartyWizardTest() {

    super();
  }

  @Before
  public void setUp() throws AppException, InformationalException {

  }

  private final String _addressDataINTL = null;

  @Test
  public void testgetThirdParytEvidenceWizardDetails()
    throws AppException, InformationalException {

    final ThirdPartyEvidenceWizardKey wizardKey =
      new ThirdPartyEvidenceWizardKey();

    final ThirdPartyEvidenceWizardDetailsResult thirdPartyEvidenceWizardDetailsResult =
      new ThirdPartyEvidenceWizardDetailsResult();

    wizardKey.evidenceID = 132466l;
    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    wizardKey.wizardStateID = wizardPersistentState
      .create(new ThirdPartyEvidenceWizardDetailsResult());

    final curam.ca.gc.bdm.facade.thirdparty.intf.BDMThirdPartyWizard bdmthirdpartyObj =
      BDMThirdPartyWizardFactory.newInstance();

    bdmthirdpartyObj.getThirdParytEvidenceWizardDetails(wizardKey);

  }

  @Test
  public void testsaveThirdPartyAdditionalDetails()
    throws AppException, InformationalException {

    final ThirdPartyAdditionalDetails thirdPartyDetails =
      new ThirdPartyAdditionalDetails();

    // Case Participant Role
    final CaseParticipantRole cprObj =
      CaseParticipantRoleFactory.newInstance();
    final CaseIDAndParticipantRoleIDKey cprKey =
      new CaseIDAndParticipantRoleIDKey();
    final CaseParticipantRoleIDStruct cprStruct =
      cprObj.readCaseParticipantRoleIDByParticipantRoleIDAndCaseID(cprKey);
    final Long cprID = cprStruct.caseParticipantRoleID;

    final BDMEvidenceUtilsTest bdmEvidenceUtilsTest =
      new BDMEvidenceUtilsTest();
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();
    final long concernRoleID = pdcPersonDetails.concernRoleID;
    final MaintainAddressKey addressKey1 = new MaintainAddressKey();
    addressKey1.concernRoleID = concernRoleID;
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = pdcPersonDetails.concernRoleID;
    thirdPartyDetails.altPhoneAreaCode = "123";
    thirdPartyDetails.altPhoneCountryCode = BDMPHONECOUNTRY.CANADA;
    thirdPartyDetails.comments = "Merge Unit Test";
    thirdPartyDetails.altPhoneExtension = "123";
    thirdPartyDetails.altPhoneNumber = "1232323";
    thirdPartyDetails.altPhoneType = PHONETYPE.PERSONAL_MOBILE;
    thirdPartyDetails.bdmModeOfReceipt = BDMMODEOFRECEIPT.CERTIFIED_APP;
    thirdPartyDetails.bdmReceivedFrom = BDMRECEIVEDFROM.FOREIGN_GOVERNMENT;
    thirdPartyDetails.bdmReceivedFromCountry = BDMSOURCECOUNTRY.AUSTRIA;
    thirdPartyDetails.phoneAreaCode = "123";
    thirdPartyDetails.phoneCountryCode = BDMPHONECOUNTRY.CANADA;
    thirdPartyDetails.phoneNumber = "4545456";
    thirdPartyDetails.phoneType = PHONETYPE.PERSONAL_MOBILE;
    thirdPartyDetails.phoneExtension = "123";
    thirdPartyDetails.contactEmailAddress = "test@gmail.com";
    thirdPartyDetails.contactEmailType = EMAILTYPE.PERSONAL;

    thirdPartyDetails.mailingAddress =
      "1\n" + "0\n" + "BDMINTL\n" + "IE\n" + "0\n" + "0\n" + "COUNTRY=IE\n"
        + "POSTCODE=\n" + "ZIP=\n" + "BDMZIPX=\n" + "APT=\n" + "POBOXNO=\n"
        + "ADD1=1234\n" + "ADD2=Bay St\n" + "CITY=Toronto\n" + "PROV=\n"
        + "STATEPROV=\n" + "BDMSTPROVX=\n" + "BDMUNPARSE=";

    final ThirdPartyEvidenceWizardKey wizardKey =
      new ThirdPartyEvidenceWizardKey();

    final curam.ca.gc.bdm.facade.thirdparty.intf.BDMThirdPartyWizard bdmthirdpartyObj =
      BDMThirdPartyWizardFactory.newInstance();
    wizardKey.evidenceID = 0;

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    wizardKey.wizardStateID = wizardPersistentState
      .create(new ThirdPartyEvidenceWizardDetailsResult());

    final ThirdPartyEvidenceWizardDetailsResult thirdPartyEvd =
      (ThirdPartyEvidenceWizardDetailsResult) wizardPersistentState
        .read(wizardKey.wizardStateID);
    thirdPartyEvd.thirdPartyDetails.caseID = 0l;
    bdmthirdpartyObj.saveThirdPartyAdditionalDetails(thirdPartyDetails,
      wizardKey);

    // assertTrue(!thirdPartyEvd.dtls.officeDetails.isEmpty());

  }

  public void mockUserObjects() throws AppException, InformationalException {

    new MockUp<WizardState>() {

      @Mock
      public WizardStateDtls read(final WizardStateKey key,
        final boolean forUpdate) throws AppException, InformationalException {

        final ThirdPartyEvidenceWizardDetailsResult thirdpartyResult =
          new ThirdPartyEvidenceWizardDetailsResult();
        final WizardStateDtls dtls = new WizardStateDtls();
        dtls.serializedState = "State";
        return dtls;

      }

    };

  }

  @Test
  public void testsaveIndividualDetails()
    throws AppException, InformationalException {

    final IndividualsWithinOrgDetails orgDetails =
      new IndividualsWithinOrgDetails();

    final BDMEvidenceUtilsTest bdmEvidenceUtilsTest =
      new BDMEvidenceUtilsTest();
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();
    final long concernRoleID = pdcPersonDetails.concernRoleID;
    final MaintainAddressKey addressKey1 = new MaintainAddressKey();
    addressKey1.concernRoleID = concernRoleID;

    orgDetails.concernRoleID = pdcPersonDetails.concernRoleID;
    orgDetails.concernRoleName = "N/A";
    orgDetails.officeMemberID = UniqueID.nextUniqueID();
    orgDetails.officeAddress =
      "1\n" + "0\n" + "BDMINTL\n" + "IE\n" + "0\n" + "0\n" + "COUNTRY=IE\n"
        + "POSTCODE=\n" + "ZIP=\n" + "BDMZIPX=\n" + "APT=\n" + "POBOXNO=\n"
        + "ADD1=1234\n" + "ADD2=Bay St\n" + "CITY=Toronto\n" + "PROV=\n"
        + "STATEPROV=\n" + "BDMSTPROVX=\n" + "BDMUNPARSE=";

    final curam.ca.gc.bdm.facade.thirdparty.intf.BDMThirdPartyWizard bdmthirdpartyObj =
      BDMThirdPartyWizardFactory.newInstance();

    final ThirdPartyEvidenceWizardKey wizardKey =
      new ThirdPartyEvidenceWizardKey();

    wizardKey.evidenceID = 0L;
    final WizardPersistentState wizardPersistentState1 =
      new WizardPersistentState();

    wizardKey.wizardStateID = wizardPersistentState1
      .create(new ThirdPartyEvidenceWizardDetailsResult());
    final ThirdPartyEvidenceWizardDetailsResult thirdPartyEvd =
      (ThirdPartyEvidenceWizardDetailsResult) wizardPersistentState1
        .read(wizardKey.wizardStateID);

    thirdPartyEvd.individuallList.clear();
    thirdPartyEvd.userSelectedOffices.dtls.clear();
    final IndividualsWithinOrgDetails naIndWithinOrg =
      new IndividualsWithinOrgDetails();
    naIndWithinOrg.concernRoleName = BDMConstants.kNotApplicable;
    thirdPartyEvd.individuallList.add(naIndWithinOrg);

    validateStep3(thirdPartyEvd, orgDetails);
    bdmthirdpartyObj.saveIndividualDetails(orgDetails, wizardKey);

    assertTrue(wizardKey.wizardStateID != 0);

  }

  @Test
  public void testlistBDMThirdPartyProgramCodes()
    throws AppException, InformationalException {

    final curam.ca.gc.bdm.facade.thirdparty.intf.BDMThirdPartyWizard bdmthirdpartyObj =
      BDMThirdPartyWizardFactory.newInstance();
    bdmthirdpartyObj.listBDMThirdPartyProgramCodes();
    final BDMThirdPartyProgramCodeDetailsList programCodeDetailsList =
      new BDMThirdPartyProgramCodeDetailsList();

    assertTrue(programCodeDetailsList.thirdPartyPrograms.isEmpty());
  }

  protected InformationMsgDtlsList createExternalPartyOffice(
    final long concernRoleId) throws AppException, InformationalException {

    final ExternalPartyOfficeDetails officeDetails =
      new ExternalPartyOfficeDetails();
    officeDetails.externalPartyOfficeDtls.concernRoleID = concernRoleId;
    officeDetails.externalPartyOfficeDtls.name = "office";
    officeDetails.externalPartyOfficeDtls.recordStatus = RECORDSTATUS.NORMAL;
    officeDetails.externalPartyOfficeDtls.startDate = Date.getCurrentDate();
    officeDetails.addressDtls.addressData =
      "1\n" + "1\n" + "BDMINTL\n" + "IE\n" + "1\n" + "1\n" + "POBOXNO=\n"
        + "APT=\n" + "ADD1=1153\n" + "ADD2=Mole Street\n" + "CITY=Midway\n"
        + "PROV=\n" + "STATEPROV=\n" + "BDMSTPROVX=UT\n" + "COUNTRY=IE\n"
        + "POSTCODE=\n" + "ZIP=\n" + "BDMZIPX=60656\n" + "BDMUNPARSE=";
    return curam.ca.gc.bdm.facade.externalparty.fact.BDMExternalPartyFactory
      .newInstance().createExternalPartyOffice(officeDetails);
  }

  @Test
  public void testsaveThirdPartyDetails()
    throws AppException, InformationalException {

    final ThirdPartyDetails thirdPartyDetails = new ThirdPartyDetails();
    validateStep1(thirdPartyDetails);
    // Case Participant Role
    final CaseParticipantRole cprObj =
      CaseParticipantRoleFactory.newInstance();
    final CaseIDAndParticipantRoleIDKey cprKey =
      new CaseIDAndParticipantRoleIDKey();
    final CaseParticipantRoleIDStruct cprStruct =
      cprObj.readCaseParticipantRoleIDByParticipantRoleIDAndCaseID(cprKey);
    final Long cprID = cprStruct.caseParticipantRoleID;

    final BDMEvidenceUtilsTest bdmEvidenceUtilsTest =
      new BDMEvidenceUtilsTest();
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();
    final long concernRoleID = pdcPersonDetails.concernRoleID;
    final MaintainAddressKey addressKey1 = new MaintainAddressKey();
    addressKey1.concernRoleID = concernRoleID;
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = pdcPersonDetails.concernRoleID;

    thirdPartyDetails.caseID = UniqueIDFactory.newInstance().getNextID();
    thirdPartyDetails.caseParticipantRoleID = cprID;
    thirdPartyDetails.comments = "Merge Unit Test";
    thirdPartyDetails.displayAdditionalInfoCluster = true;
    thirdPartyDetails.firstName = "John";
    thirdPartyDetails.fromDate = Date.getDate("20190101");
    thirdPartyDetails.languagePref = LANGUAGE.ENGLISH;
    thirdPartyDetails.lastName = "Doe";
    thirdPartyDetails.middleName = "JOHN";
    thirdPartyDetails.participantRoleID = pdcPersonDetails.concernRoleID;
    thirdPartyDetails.thirdPartyCaseParticipantRoleID =
      pdcPersonDetails.concernRoleID;
    thirdPartyDetails.toDate = Date.getDate("20200101");

    final curam.ca.gc.bdm.facade.thirdparty.intf.BDMThirdPartyWizard bdmthirdpartyObj =
      BDMThirdPartyWizardFactory.newInstance();

    final BDMExternalParty bdmExtPartyObj =
      BDMExternalPartyFactory.newInstance();

    final ThirdPartyEvidenceWizardKey wizardKey =
      new ThirdPartyEvidenceWizardKey();

    wizardKey.evidenceID = 0L;

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    wizardKey.wizardStateID = wizardPersistentState
      .create(new ThirdPartyEvidenceWizardDetailsResult());
    final ThirdPartyEvidenceWizardDetailsResult thirdPartyEvd =
      (ThirdPartyEvidenceWizardDetailsResult) wizardPersistentState
        .read(wizardKey.wizardStateID);
    bdmthirdpartyObj.saveThirdPartyDetails(thirdPartyDetails, wizardKey);

    assertTrue(wizardKey.wizardStateID != 0);

  }

  @Test
  public void testsaveOfficeDetails()
    throws AppException, InformationalException {

    final OfficeDetailsTab officeDetails = new OfficeDetailsTab();

    final ThirdPartyEvidenceWizardKey wizardKey =
      new ThirdPartyEvidenceWizardKey();

    final curam.ca.gc.bdm.facade.thirdparty.intf.BDMThirdPartyWizard bdmthirdpartyObj =
      BDMThirdPartyWizardFactory.newInstance();

    wizardKey.evidenceID = 0L;
    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    wizardKey.wizardStateID = wizardPersistentState
      .create(new ThirdPartyEvidenceWizardDetailsResult());
    final ThirdPartyEvidenceWizardDetailsResult thirdPartyEvd =
      (ThirdPartyEvidenceWizardDetailsResult) wizardPersistentState
        .read(wizardKey.wizardStateID);

    validateStep2(thirdPartyEvd, officeDetails);

    bdmthirdpartyObj.saveOfficeDetails(officeDetails, wizardKey);
    assertTrue(wizardKey.wizardStateID != 0);

  }

  /**
   * Validate inputs for wizard step 2.
   *
   * @param thirdPartyEvd
   * @param officeDetails
   * @throws AppException
   * @throws InformationalException
   */
  protected void validateStep2(
    final ThirdPartyEvidenceWizardDetailsResult thirdPartyEvd,
    final OfficeDetailsTab officeDetails)
    throws AppException, InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    final ThirdPartyDetails thirdPartyDetails =
      thirdPartyEvd.thirdPartyDetails;

    if (BDMTHIRDPARTYROLETYPE.ORGANIZATION.equals(thirdPartyDetails.roleType)
      && thirdPartyDetails.thirdPartyParticipantRoleID != 0
      && officeDetails.officeIDetailsTABList.isEmpty()) {

      final ConcernRoleKey crKey = new ConcernRoleKey();
      crKey.concernRoleID = thirdPartyDetails.thirdPartyParticipantRoleID;
      final ConcernRoleDtls dtls =
        ConcernRoleFactory.newInstance().read(crKey);

      if (CONCERNROLETYPE.EXTERNALPARTY.equals(dtls.concernRoleType)) {
        // BR-8
        ValidationManagerFactory.getManager()
          .addInfoMgrExceptionWithLookup(new AppException(
            BDMTHIRDPARTYCONTACT.ERR_MISSING_OFFICE_FOR_ROLE_TYPE_ORGANIZATION),
            "", InformationalType.kError);
      }
    }

    informationalManager.failOperation();
  }

  /**
   * Validate inputs for wizard step 1.
   *
   * @param thirdPartyDetails
   * @throws AppException
   * @throws InformationalException
   */
  protected void validateStep1(final ThirdPartyDetails thirdPartyDetails)
    throws AppException, InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    if (BDMTHIRDPARTYROLE.POA.equals(thirdPartyDetails.role)) {

      if (thirdPartyDetails.poaClass.isEmpty()) {
        // BR-1
        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          new AppException(
            BDMTHIRDPARTYCONTACT.ERR_MISSING_POA_CLASS_FOR_ROLE_POA),
          "", InformationalType.kError);
      }

      if (thirdPartyDetails.poaType.isEmpty()) {
        // BR-2
        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          new AppException(
            BDMTHIRDPARTYCONTACT.ERR_MISSING_POA_TYPE_FOR_ROLE_POA),
          "", InformationalType.kError);
      }
    }
    // BUG CR 97309 An External Party should be selected as Third Party Contact
    // if Role Type is 'Organization' in order to proceed
    if (BDMTHIRDPARTYROLETYPE.INDIVIDUAL.equals(thirdPartyDetails.roleType)
      && (thirdPartyDetails.firstName.isEmpty()
        || thirdPartyDetails.lastName.isEmpty())
      && thirdPartyDetails.thirdPartyParticipantRoleID == 0) {
      // BR-3
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMTHIRDPARTYCONTACT.ERR_NEED_TO_SELECT_ORGNIZATION_OR_REPRESENTATIVE_DETAILS),
          "", InformationalType.kError);
    }

    // BUG CR 97309 An External Party should be selected as Third Party Contact
    // if Role Type is 'Organization' in order to proceed
    if (BDMTHIRDPARTYROLETYPE.ORGANIZATION.equals(thirdPartyDetails.roleType)
      && thirdPartyDetails.thirdPartyParticipantRoleID == 0) {
      // BR-17
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMTHIRDPARTYCONTACT.ERR_NEED_TO_SELECT_ORGNIZATION_OR_REPRESENTATIVE_DETAILS),
          "", InformationalType.kError);
    }

    if (BDMTHIRDPARTYROLETYPE.ORGANIZATION.equals(thirdPartyDetails.roleType)
      && !(thirdPartyDetails.firstName.isEmpty()
        && thirdPartyDetails.lastName.isEmpty()
        && thirdPartyDetails.middleName.isEmpty()
        && thirdPartyDetails.title.isEmpty())) {
      // BR-5
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMTHIRDPARTYCONTACT.ERR_NAME_NOT_EMPTY_FOR_ROLE_TYPE_ORGANIZATION),
          "", InformationalType.kError);
    }

    informationalManager.failOperation();

    if (BDMTHIRDPARTYROLETYPE.INDIVIDUAL.equals(thirdPartyDetails.roleType)
      && thirdPartyDetails.thirdPartyParticipantRoleID != 0
      && !(thirdPartyDetails.firstName.isEmpty()
        && thirdPartyDetails.lastName.isEmpty()
        && thirdPartyDetails.middleName.isEmpty()
        && thirdPartyDetails.title.isEmpty())) {

      final ConcernRoleKey crKey = new ConcernRoleKey();
      crKey.concernRoleID = thirdPartyDetails.thirdPartyParticipantRoleID;
      final ConcernRoleDtls dtls =
        ConcernRoleFactory.newInstance().read(crKey);

      if (CONCERNROLETYPE.PERSON.equals(dtls.concernRoleType)
        || CONCERNROLETYPE.PROSPECTPERSON.equals(dtls.concernRoleType)) {
        // BR-6
        ValidationManagerFactory.getManager()
          .addInfoMgrExceptionWithLookup(new AppException(
            BDMTHIRDPARTYCONTACT.ERR_NAME_NOT_EMPTY_FOR_ROLE_TYPE_INDIVIDUAL_WHEN_PERSON_ALREADY_EXISTS),
            "", InformationalType.kError);
      }
    }

    if (BDMTHIRDPARTYROLETYPE.ORGANIZATION.equals(thirdPartyDetails.roleType)
      && thirdPartyDetails.thirdPartyParticipantRoleID != 0
      && !(thirdPartyDetails.firstName.isEmpty()
        && thirdPartyDetails.lastName.isEmpty()
        && thirdPartyDetails.middleName.isEmpty()
        && thirdPartyDetails.title.isEmpty())) {

      final ConcernRoleKey crKey = new ConcernRoleKey();
      crKey.concernRoleID = thirdPartyDetails.thirdPartyParticipantRoleID;
      final ConcernRoleDtls dtls =
        ConcernRoleFactory.newInstance().read(crKey);

      if (CONCERNROLETYPE.EXTERNALPARTY.equals(dtls.concernRoleType)) {
        // BR-7
        ValidationManagerFactory.getManager()
          .addInfoMgrExceptionWithLookup(new AppException(
            BDMTHIRDPARTYCONTACT.ERR_NAME_NOT_EMPTY_FOR_ROLE_TYPE_ORGANIZATION_WHEN_EXTERNAL_PARTY_ALREAY_EXISTS),
            "", InformationalType.kError);
      }
    }

    if (!(BDMTHIRDPARTYROLETYPE.INDIVIDUAL.equals(thirdPartyDetails.roleType)
      && (BDMTHIRDPARTYROLE.POA.equals(thirdPartyDetails.role)
        || BDMTHIRDPARTYROLE.PERSONAL.equals(thirdPartyDetails.role)))
      && thirdPartyDetails.thirdPartyParticipantRoleID != 0) {

      final ConcernRoleKey crKey = new ConcernRoleKey();
      crKey.concernRoleID = thirdPartyDetails.thirdPartyParticipantRoleID;
      final ConcernRoleDtls dtls =
        ConcernRoleFactory.newInstance().read(crKey);

      if (CONCERNROLETYPE.EXTERNALPARTY.equals(dtls.concernRoleType)) {

        // look up mailing address
        if (!hasMailingAddress(
          thirdPartyDetails.thirdPartyParticipantRoleID)) {
          // BR-9
          ValidationManagerFactory.getManager()
            .addInfoMgrExceptionWithLookup(new AppException(
              BDMTHIRDPARTYCONTACT.ERR_MISSING_MAILING_ADDR_WHEN_REGISTER_3RD_PARTY_CONTACT_1),
              "", InformationalType.kError);
        }
      }
    }

    if (BDMTHIRDPARTYROLETYPE.INDIVIDUAL.equals(thirdPartyDetails.roleType)
      && !(BDMTHIRDPARTYROLE.POA.equals(thirdPartyDetails.role)
        || BDMTHIRDPARTYROLE.PERSONAL.equals(thirdPartyDetails.role))
      && thirdPartyDetails.thirdPartyParticipantRoleID != 0) {

      final ConcernRoleKey crKey = new ConcernRoleKey();
      crKey.concernRoleID = thirdPartyDetails.thirdPartyParticipantRoleID;
      final ConcernRoleDtls dtls =
        ConcernRoleFactory.newInstance().read(crKey);

      if (CONCERNROLETYPE.PROSPECTPERSON.equals(dtls.concernRoleType)) {

        // look up mailing address
        if (!hasMailingAddress(
          thirdPartyDetails.thirdPartyParticipantRoleID)) {
          // BR-11
          ValidationManagerFactory.getManager()
            .addInfoMgrExceptionWithLookup(new AppException(
              BDMTHIRDPARTYCONTACT.ERR_MISSING_MAILING_ADDR_WHEN_REGISTER_3RD_PARTY_CONTACT_3),
              "", InformationalType.kError);
        }
      }
    }

    if (BDMTHIRDPARTYROLETYPE.ORGANIZATION.equals(thirdPartyDetails.roleType)
      && thirdPartyDetails.thirdPartyParticipantRoleID != 0) {

      final ConcernRoleKey crKey = new ConcernRoleKey();
      crKey.concernRoleID = thirdPartyDetails.thirdPartyParticipantRoleID;
      final ConcernRoleDtls dtls =
        ConcernRoleFactory.newInstance().read(crKey);

      if (CONCERNROLETYPE.PERSON.equals(dtls.concernRoleType)
        || CONCERNROLETYPE.PROSPECTPERSON.equals(dtls.concernRoleType)) {

        // BR-15
        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          new AppException(
            BDMTHIRDPARTYCONTACT.ERR_CANNOT_SELECT_ROLE_TYPE_ORGANIZATION),
          "", InformationalType.kError);
      }
    }

    if (BDMTHIRDPARTYROLETYPE.INDIVIDUAL.equals(thirdPartyDetails.roleType)
      && thirdPartyDetails.thirdPartyParticipantRoleID != 0) {

      final ConcernRoleKey crKey = new ConcernRoleKey();
      crKey.concernRoleID = thirdPartyDetails.thirdPartyParticipantRoleID;
      final ConcernRoleDtls dtls =
        ConcernRoleFactory.newInstance().read(crKey);

      if (CONCERNROLETYPE.EXTERNALPARTY.equals(dtls.concernRoleType)) {
        // BR-16
        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          new AppException(
            BDMTHIRDPARTYCONTACT.ERR_CANNOT_SELECT_ROLE_TYPE_INDIVIDUAL),
          "", InformationalType.kError);
      }
    }

    informationalManager.failOperation();
  }

  /**
   * Look up mailing address for a concern role.
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  protected boolean hasMailingAddress(final long concernRoleID)
    throws AppException, InformationalException {

    boolean hasMailAddress = false;

    final AddressConcernRoleTypeStatusKey addressSearchKey =
      new AddressConcernRoleTypeStatusKey();
    addressSearchKey.concernRoleID = concernRoleID;
    addressSearchKey.statusCode = RECORDSTATUS.NORMAL;
    addressSearchKey.typeCode = CONCERNROLEADDRESSTYPE.MAILING;
    final AddressConcernRoleTypeStatusDtlsList addressList =
      ConcernRoleAddressFactory.newInstance()
        .searchAddressByConcernRoleTypeStatus(addressSearchKey);

    if (!addressList.dtls.isEmpty()) {
      hasMailAddress = true;
    }

    return hasMailAddress;
  }

  /**
   * Validate inputs for wizard step 3.
   *
   * @param thirdPartyEvd
   * @param individuals
   * @throws AppException
   * @throws InformationalException
   */
  protected void validateStep3(
    final ThirdPartyEvidenceWizardDetailsResult thirdPartyEvd,
    final IndividualsWithinOrgDetails individuals)
    throws AppException, InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    // Bug fix 98040 - Incorrect error message and N/A option on screen 2 and 3
    // when adding an Organization as Third Party
    if (null != thirdPartyEvd.userSelectedOffices
      && thirdPartyEvd.userSelectedOffices.dtls.size() > 1
      && individuals.concernRoleName.equals(BDMConstants.kNotApplicable)) {
      // BR-14
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMTHIRDPARTYCONTACT.ERR_MORE_THAN_ONE_OFFICE_SELECTED_FOR_EXTERNAL_PARTY),
          "", InformationalType.kError);
    }
    // Fix BUG-98040 Incorrect error message and N/A option on screen 2 and 3
    // when adding an Organization as Third Party
    if (thirdPartyEvd.individuallList.size() == 0
      || StringUtil.isNullOrEmpty(individuals.concernRoleName)) {
      // BR-18
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMTHIRDPARTYCONTACT.ERR_NEED_TO_SELECT_INDIVIDUAL_IN_ORGANIZATION_OR_NA),
          "", InformationalType.kError);
    }

    informationalManager.failOperation();
  }

  /**
   * Validate inputs for wizard step 4.
   *
   * @param thirdPartyEvd
   * @param additionalDetails
   * @throws AppException
   * @throws InformationalException
   */
  protected void validateStep4(
    final ThirdPartyEvidenceWizardDetailsResult thirdPartyEvd,
    final ThirdPartyAdditionalDetails additionalDetails)
    throws AppException, InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    final ThirdPartyDetails thirdPartyDetails =
      thirdPartyEvd.thirdPartyDetails;

    final OtherAddressData mailingAddressData = new OtherAddressData();
    mailingAddressData.addressData = additionalDetails.mailingAddress;

    final OtherAddressData residentialAddressData = new OtherAddressData();
    residentialAddressData.addressData = additionalDetails.residentialAddress;

    final BDMUtil bdmUtil = new BDMUtil();
    // BR-08
    if (BDMTHIRDPARTYROLETYPE.INDIVIDUAL.equals(thirdPartyDetails.roleType)
      && !(BDMTHIRDPARTYROLE.POA.equals(thirdPartyDetails.role)
        || BDMTHIRDPARTYROLE.PERSONAL.equals(thirdPartyDetails.role))
      && thirdPartyDetails.thirdPartyParticipantRoleID == 0
      && bdmUtil.isAddressEmpty(mailingAddressData, residentialAddressData,
        additionalDetails.isMailingAddSame)) {
      // BR-10
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMTHIRDPARTYCONTACT.ERR_MISSING_MAILING_ADDR_WHEN_REGISTER_3RD_PARTY_CONTACT_2),
          "", InformationalType.kError);
    }

    // BR-09
    if (BDMTHIRDPARTYROLETYPE.INDIVIDUAL.equals(thirdPartyDetails.roleType)
      && !(BDMTHIRDPARTYROLE.POA.equals(thirdPartyDetails.role)
        || BDMTHIRDPARTYROLE.PERSONAL.equals(thirdPartyDetails.role))
      && thirdPartyDetails.thirdPartyParticipantRoleID != 0
      && !hasMailingAddress(thirdPartyDetails.thirdPartyParticipantRoleID)) {
      // BR-10
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMTHIRDPARTYCONTACT.ERR_MISSING_MAILING_ADDR_WHEN_REGISTER_3RD_PARTY_CONTACT_2),
          "", InformationalType.kError);
    }

    if (BDMRECEIVEDFROM.FOREIGN_GOVERNMENT
      .equals(additionalDetails.receivedFrom)
      && (additionalDetails.modeOfReceipt.isEmpty()
        || additionalDetails.receivedFromCountry.isEmpty())) {
      // BR-12
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMTHIRDPARTYCONTACT.ERR_RECV_FROM_COUNTRY_AND_MODE_OF_RECPT_MUST_BE_SELECTED),
          "", InformationalType.kError);
    }

    if (!BDMRECEIVEDFROM.FOREIGN_GOVERNMENT
      .equals(additionalDetails.receivedFrom)
      && (!additionalDetails.modeOfReceipt.isEmpty()
        || !additionalDetails.receivedFromCountry.isEmpty())) {
      // BR-13
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMTHIRDPARTYCONTACT.ERR_RECV_FROM_COUNTRY_AND_MODE_OF_RECPT_CANNOT_BE_SELECTED),
          "", InformationalType.kError);
    }

    final boolean isPhoneDtlsEntered =
      !additionalDetails.phoneAreaCode.trim().isEmpty()
        || !additionalDetails.phoneCountryCode.trim().isEmpty()
        || !additionalDetails.phoneExtension.trim().isEmpty()
        || !additionalDetails.phoneNumber.trim().isEmpty();

    final boolean isAltPhoneDtlsEntered =
      !additionalDetails.altPhoneAreaCode.trim().isEmpty()
        || !additionalDetails.altPhoneCountryCode.trim().isEmpty()
        || !additionalDetails.altPhoneExtension.trim().isEmpty()
        || !additionalDetails.altPhoneNumber.trim().isEmpty();

    if (isPhoneDtlsEntered && additionalDetails.phoneType.trim().isEmpty()
      || isAltPhoneDtlsEntered
        && additionalDetails.altPhoneType.trim().isEmpty()) {
      // BR-18
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMTHIRDPARTYCONTACT.ERR_PHONE_TYPE_MISSING_FOR_PHONE_NUMBER),
        "", InformationalType.kError);
    }

    informationalManager.failOperation();
  }

}
