package curam.ca.gc.bdm.facade.externalparty.impl;

import curam.ca.gc.bdm.codetable.BDMCOSLOCATION;
import curam.ca.gc.bdm.codetable.BDMSTATUSOFAGREEMENT;
import curam.ca.gc.bdm.codetable.BDMYESNO;
import curam.ca.gc.bdm.entity.fact.BDMExternalPartyHistoryFactory;
import curam.ca.gc.bdm.entity.struct.BDMExtPartyHistSearchKey;
import curam.ca.gc.bdm.entity.struct.BDMExtPartyHistoryDetailsList;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMExternalPartyHomePageDetails;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMExternalPartyModifyDetails;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMExternalPartyRegistrationDetails;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMExternalPartySearchKey;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMExternalPartySearchWizardKey;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMReadExternalPartyHistoryKey;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMReadExternalPartyKey;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMRegisterExternalPartyState;
import curam.ca.gc.bdm.facade.participant.struct.BDMPersonRegistrationDetails;
import curam.ca.gc.bdm.facade.thirdparty.struct.ThirdPartyEvidenceWizardDetailsResult;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMEXTERNALPARTY;
import curam.ca.gc.bdm.message.BDMTHIRDPARTYCONTACT;
import curam.ca.gc.bdm.sl.externalparty.struct.BDMExternalPartyOfficeList;
import curam.ca.gc.bdm.test.BDMBaseTest;
import curam.ca.gc.bdm.test.data.impl.BDMExternalPartyTestDataDetails;
import curam.codetable.CASEPARTICIPANTROLETYPE;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.EXTERNALPARTYTYPE;
import curam.codetable.PROVINCETYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.facade.fact.PersonFactory;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.facade.struct.CreateIntegratedCaseResultAndMessages;
import curam.core.facade.struct.ExternalPartyRegistrationDetails;
import curam.core.facade.struct.ExternalPartySearchKey1;
import curam.core.facade.struct.InformationMsgDtlsList;
import curam.core.facade.struct.MaintainParticipantAddressDetails;
import curam.core.facade.struct.ParticipantRegistrationWizardResult;
import curam.core.facade.struct.ParticipantRegistrationWizardSearchDetails;
import curam.core.facade.struct.ParticipantSearchDetails;
import curam.core.facade.struct.PersonRegistrationDetails;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.fact.ConcernRoleAddressFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.impl.CuramConst;
import curam.core.sl.entity.fact.ExternalPartyOfficeMemberFactory;
import curam.core.sl.entity.struct.CaseParticipantRoleDtls;
import curam.core.sl.entity.struct.CaseParticipantRoleDtlsList;
import curam.core.sl.entity.struct.ExternalPartyKey;
import curam.core.sl.entity.struct.ExternalPartyOfficeAddressDtls;
import curam.core.sl.entity.struct.ExternalPartyOfficeDtls;
import curam.core.sl.entity.struct.ExternalPartyOfficeKey;
import curam.core.sl.entity.struct.ExternalPartyOfficeMemberDtls;
import curam.core.sl.entity.struct.ExternalPartyOfficeMemberKey;
import curam.core.sl.entity.struct.ParticipantRoleIDAndTypeCodeKey;
import curam.core.sl.fact.ExternalPartyOfficeFactory;
import curam.core.sl.intf.ExternalPartyOffice;
import curam.core.sl.struct.ExternalPartyOfficeAddressDetails;
import curam.core.sl.struct.ExternalPartyOfficeDetails;
import curam.core.sl.struct.WizardStateID;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.AddressReadMultiDtls;
import curam.core.struct.AddressReadMultiDtlsList;
import curam.core.struct.AddressReadMultiKey;
import curam.core.struct.AddressSearchKey;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.OtherAddressData;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.UniqueID;
import curam.wizardpersistence.impl.WizardPersistentState;
import data.impl.BDMExternalPartyTestData;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class BDMExternalPartyTest extends BDMBaseTest {

  BDMExternalParty bdmExternalPartyFacade = new BDMExternalParty();

  @Override
  public void setUpCuramServerTest() {

    super.setUpCuramServerTest();
  }

  @Test
  public void testSearchExternalPartyDetails_onValidations()
    throws AppException, InformationalException {

    ExternalPartySearchKey1 searchKey = new ExternalPartySearchKey1();
    BDMExternalPartySearchKey bdmSearchKey = new BDMExternalPartySearchKey();

    try {
      bdmExternalPartyFacade.searchExternalPartyDetails(searchKey,
        bdmSearchKey);
      fail();
    } catch (final InformationalException ie) {
      assertEquals("Reference or partial Name criteria are required.",
        ie.getLocalizedMessage());
    }

    TransactionInfo.setInformationalManager();

    searchKey = new ExternalPartySearchKey1();
    bdmSearchKey = new BDMExternalPartySearchKey();

    searchKey.key.key.concernRoleName = "Jon Smith";
    searchKey.key.key.addressDtls.city = "T";

    try {
      bdmExternalPartyFacade.searchExternalPartyDetails(searchKey,
        bdmSearchKey);
      fail();
    } catch (final InformationalException ie) {
      assertEquals(
        "City, Town or Village field must contain at least 2 alphanumeric characters.",
        ie.getLocalizedMessage());
    }

    TransactionInfo.setInformationalManager();

    searchKey = new ExternalPartySearchKey1();
    bdmSearchKey = new BDMExternalPartySearchKey();

    searchKey.key.key.concernRoleName = "Jon Smith";
    searchKey.key.key.addressDtls.addressLine1 = "1";

    try {
      bdmExternalPartyFacade.searchExternalPartyDetails(searchKey,
        bdmSearchKey);
      fail();
    } catch (final InformationalException ie) {
      assertEquals(
        "Street Number or Street 1 field must contain at least 2 alphanumeric character.",
        ie.getLocalizedMessage());
    }

    TransactionInfo.setInformationalManager();

    searchKey = new ExternalPartySearchKey1();
    bdmSearchKey = new BDMExternalPartySearchKey();

    searchKey.key.key.referenceNumber = "32212";
    searchKey.key.key.addressDtls.addressLine2 = "B";

    try {
      bdmExternalPartyFacade.searchExternalPartyDetails(searchKey,
        bdmSearchKey);
      fail();
    } catch (final InformationalException ie) {
      assertEquals(
        "Street Name or Street 2 field must contain at least 2 alphanumeric characters.",
        ie.getLocalizedMessage());
    }

    TransactionInfo.setInformationalManager();

    searchKey = new ExternalPartySearchKey1();
    bdmSearchKey = new BDMExternalPartySearchKey();

    searchKey.key.key.concernRoleName = "Jon Smith";
    bdmSearchKey.stateProvince = "O";

    try {
      bdmExternalPartyFacade.searchExternalPartyDetails(searchKey,
        bdmSearchKey);
      fail();
    } catch (final InformationalException ie) {
      assertEquals(
        "Province, State or Territory field must contain at least 2 alphanumeric characters.",
        ie.getLocalizedMessage());
    }

    TransactionInfo.setInformationalManager();

    searchKey = new ExternalPartySearchKey1();
    bdmSearchKey = new BDMExternalPartySearchKey();

    searchKey.key.key.concernRoleName = "Jon Smith";
    bdmSearchKey.postalCode = "M1";

    try {
      bdmExternalPartyFacade.searchExternalPartyDetails(searchKey,
        bdmSearchKey);
      fail();
    } catch (final InformationalException ie) {
      assertEquals(
        "Postal or Zip Code field must contain at least 3 alphanumeric characters.",
        ie.getLocalizedMessage());
    }

    TransactionInfo.setInformationalManager();

    searchKey = new ExternalPartySearchKey1();
    bdmSearchKey = new BDMExternalPartySearchKey();

    searchKey.key.key.concernRoleName = "Jon Smith";
    bdmSearchKey.poBox = "1";

    try {
      bdmExternalPartyFacade.searchExternalPartyDetails(searchKey,
        bdmSearchKey);
      fail();
    } catch (final InformationalException ie) {
      assertEquals(
        "PO Box/Rural Route/General Delivery field must contain at least 2 alphanumeric characters.",
        ie.getLocalizedMessage());
    }

    TransactionInfo.setInformationalManager();

    searchKey = new ExternalPartySearchKey1();
    bdmSearchKey = new BDMExternalPartySearchKey();

    searchKey.key.key.concernRoleName = "Jon Smith";
    searchKey.key.key.addressDtls.city = "To";
    searchKey.key.key.addressDtls.addressLine2 = "12";
    searchKey.key.key.addressDtls.addressLine2 = "Ba";
    bdmSearchKey.stateProvince = "On";
    bdmSearchKey.postalCode = "M1M";
    bdmSearchKey.poBox = "10";

    try {
      bdmExternalPartyFacade.searchExternalPartyDetails(searchKey,
        bdmSearchKey);
    } catch (final InformationalException ie) {
      assertEquals(
        "There are no matching items based on the Search Criteria entered.",
        ie.getLocalizedMessage());
    }
  }

  @Test
  public void testGetRegisterExternalPartySearchCriteria()
    throws AppException, InformationalException {

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();
    final BDMRegisterExternalPartyState regExtPtyState =
      new BDMRegisterExternalPartyState();

    final long wizID = wizardPersistentState.create(regExtPtyState);

    final WizardStateID wizardStateID = new WizardStateID();
    assertNotEquals(0,
      bdmExternalPartyFacade.getRegisterExternalPartySearchCriteria(
        wizardStateID).key.stateID.wizardStateID);

    wizardStateID.wizardStateID = wizID;
    assertEquals(wizID,
      bdmExternalPartyFacade.getRegisterExternalPartySearchCriteria(
        wizardStateID).key.stateID.wizardStateID);
  }

  @Test
  public void testGetAddressDataFromFields()
    throws AppException, InformationalException {

    final AddressSearchKey addressSearchKey = new AddressSearchKey();
    addressSearchKey.addressLine1 = "1234";
    addressSearchKey.addressLine2 = "Bay St";
    addressSearchKey.city = "Toronto";
    addressSearchKey.state = "ON";

    final OtherAddressData otherAddrData =
      bdmExternalPartyFacade.getAddressDataFromFields(addressSearchKey);

    final String addrData =
      "1\n" + "0\n" + "BDMINTL\n" + "CA\n" + "0\n" + "0\n" + "COUNTRY=CA\n"
        + "POSTCODE=\n" + "ZIP=\n" + "BDMZIPX=\n" + "APT=\n" + "POBOXNO=\n"
        + "ADD1=1234\n" + "ADD2=Bay St\n" + "CITY=Toronto\n" + "PROV=\n"
        + "STATEPROV=\n" + "BDMSTPROVX=\n" + "BDMUNPARSE=";

    assertEquals(addrData, otherAddrData.addressData);
  }

  @Test
  public void testSetRegisterExternalPartyDetails_SSACountry()
    throws AppException, InformationalException {

    final BDMExternalPartyTestData extPartyTestDataObj =
      new BDMExternalPartyTestData();

    final BDMExternalPartyTestDataDetails extPartyTestDataDetails =
      extPartyTestDataObj.getRegisterExtPartySearchCriteria_Empty();

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = extPartyTestDataDetails.wizardStateID;

    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = CuramConst.kSaveAction;

    final BDMExternalPartyRegistrationDetails bdmextPartyRegDetails =
      extPartyTestDataObj.getExternalPartyRegistrationCustomDetails();

    // this is SSA COUNTRY
    final ExternalPartyRegistrationDetails registrationDtls =
      extPartyTestDataObj.getExternalPartyRegistrationDetails_Default();

    extPartyTestDataDetails.extPartyName = "Angola";

    final ParticipantRegistrationWizardResult wizardResult =
      bdmExternalPartyFacade.setRegisterExternalPartyDetails(registrationDtls,
        wizardStateID, actionIDProperty, bdmextPartyRegDetails);

    assertNotNull(wizardResult);
  }

  @Test
  public void testSetRegisterExternalPartyDetails_ThirdParty()
    throws AppException, InformationalException {

    final BDMExternalPartyTestData extPartyTestDataObj =
      new BDMExternalPartyTestData();

    final BDMExternalPartyTestDataDetails extPartyTestDataDetails =
      extPartyTestDataObj.getRegisterExtPartySearchCriteria_Empty();

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = extPartyTestDataDetails.wizardStateID;

    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = CuramConst.kSaveAction;

    final BDMExternalPartyRegistrationDetails bdmextPartyRegDetails =
      extPartyTestDataObj.getExternalPartyRegistrationCustomDetails();

    final ExternalPartyRegistrationDetails registrationDtls =
      extPartyTestDataObj.getExternalPartyRegistrationDetails_Default();

    extPartyTestDataDetails.extPartyName =
      registrationDtls.externalPartyRegistrationDetails.externalPartyDtls.name;

    registrationDtls.externalPartyRegistrationDetails.externalPartyDtls.type =
      EXTERNALPARTYTYPE.THIRDPARTY;

    final ParticipantRegistrationWizardResult wizardResult =
      bdmExternalPartyFacade.setRegisterExternalPartyDetails(registrationDtls,
        wizardStateID, actionIDProperty, bdmextPartyRegDetails);

    assertNotNull(wizardResult);
  }

  @Test
  public void testSetRegisterExternalPartyDetails_OtherExternalPartyType()
    throws AppException, InformationalException {

    final BDMExternalPartyTestData extPartyTestDataObj =
      new BDMExternalPartyTestData();

    final BDMExternalPartyTestDataDetails extPartyTestDataDetails =
      extPartyTestDataObj.getRegisterExtPartySearchCriteria_Empty();

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = extPartyTestDataDetails.wizardStateID;

    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = CuramConst.kSaveAction;

    final BDMExternalPartyRegistrationDetails bdmextPartyRegDetails =
      extPartyTestDataObj.getExternalPartyRegistrationCustomDetails();

    final ExternalPartyRegistrationDetails registrationDtls =
      extPartyTestDataObj.getExternalPartyRegistrationDetails_Default();

    extPartyTestDataDetails.extPartyName =
      registrationDtls.externalPartyRegistrationDetails.externalPartyDtls.name;

    registrationDtls.externalPartyRegistrationDetails.externalPartyDtls.type =
      EXTERNALPARTYTYPE.OTHERGOVDEPT;

    final ParticipantRegistrationWizardResult wizardResult =
      bdmExternalPartyFacade.setRegisterExternalPartyDetails(registrationDtls,
        wizardStateID, actionIDProperty, bdmextPartyRegDetails);

    assertNotNull(wizardResult);
  }

  @Test
  public void testCreateExternalPartyOffice_ssaCountry()
    throws AppException, InformationalException {

    final ParticipantRegistrationWizardResult ssaCountry = createSSACountry();
    final InformationMsgDtlsList externalPartyOffice =
      createExternalPartyOffice(ssaCountry.registrationResult.concernRoleID);
    assertTrue(externalPartyOffice.informationalMsgDtlsList.dtls.isEmpty());
  }

  @Test
  public void testCreateExternalPartyOffice_nonSsaCountry()
    throws AppException, InformationalException {

    final InformationMsgDtlsList externalPartyOffice =
      createExternalPartyOffice(80001);

    assertTrue(externalPartyOffice.informationalMsgDtlsList.dtls.isEmpty());
  }

  @Test
  public void testListExternalPartyOffice()
    throws AppException, InformationalException {

    final BDMExternalPartyOfficeList bdmExternalPartyOfficeList =
      listExternalPartyOffice();
    assertTrue(!bdmExternalPartyOfficeList.dtls.list.isEmpty());
  }

  @Test
  public void testModifyExternalPartyOffice()
    throws AppException, InformationalException {

    final BDMExternalPartyOfficeList bdmExternalPartyOfficeList =
      listExternalPartyOffice();

    final ExternalPartyOfficeKey externalPartyOfficeKey =
      new ExternalPartyOfficeKey();
    externalPartyOfficeKey.externalPartyOfficeID =
      bdmExternalPartyOfficeList.dtls.list
        .get(0).externalPartyOfficeDtls.externalPartyOfficeID;
    final ExternalPartyOfficeDtls externalPartyOfficeDtls =
      ExternalPartyOfficeFactory.newInstance()
        .readExternalPartyOffice(externalPartyOfficeKey);
    externalPartyOfficeDtls.type = "test";

    final InformationMsgDtlsList informationMsgDtlsList =
      bdmExternalPartyFacade
        .modifyExternalPartyOffice(externalPartyOfficeDtls);
    assertTrue(
      informationMsgDtlsList.informationalMsgDtlsList.dtls.isEmpty());
  }

  @Test
  public void testModifyExternalPartyOffice_diffName()
    throws AppException, InformationalException {

    final BDMExternalPartyOfficeList bdmExternalPartyOfficeList =
      listExternalPartyOffice();

    final ExternalPartyOfficeKey externalPartyOfficeKey =
      new ExternalPartyOfficeKey();
    externalPartyOfficeKey.externalPartyOfficeID =
      bdmExternalPartyOfficeList.dtls.list
        .get(0).externalPartyOfficeDtls.externalPartyOfficeID;
    final ExternalPartyOfficeDtls externalPartyOfficeDtls =
      ExternalPartyOfficeFactory.newInstance()
        .readExternalPartyOffice(externalPartyOfficeKey);
    externalPartyOfficeDtls.type = "test";

    externalPartyOfficeDtls.name += "y";

    final InformationMsgDtlsList informationMsgDtlsList =
      bdmExternalPartyFacade
        .modifyExternalPartyOffice(externalPartyOfficeDtls);
    assertTrue(
      informationMsgDtlsList.informationalMsgDtlsList.dtls.isEmpty());
  }

  @Test
  public void testCancelExternalPartyOffice()
    throws AppException, InformationalException {

    final BDMExternalPartyOfficeList bdmExternalPartyOfficeList =
      listExternalPartyOffice();
    final ExternalPartyOfficeKey externalPartyOfficeKey =
      new ExternalPartyOfficeKey();
    externalPartyOfficeKey.externalPartyOfficeID = 80241;
    bdmExternalPartyFacade.cancelExternalPartyOffice(externalPartyOfficeKey);
    final ExternalPartyOffice externalPartyOffice =
      ExternalPartyOfficeFactory.newInstance();
    final ExternalPartyOfficeDtls externalPartyOfficeDtls =
      externalPartyOffice.readExternalPartyOffice(externalPartyOfficeKey);
    assertTrue(
      RECORDSTATUS.CANCELLED.equals(externalPartyOfficeDtls.recordStatus));
  }

  @Test
  public void testCancelExternalPartyOffice_withFEC()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = registerPerson();
    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);
    final BDMExternalPartyOfficeList bdmExternalPartyOfficeList =
      listExternalPartyOffice();
    final ExternalPartyOfficeKey externalPartyOfficeKey =
      new ExternalPartyOfficeKey();
    externalPartyOfficeKey.externalPartyOfficeID =
      bdmExternalPartyOfficeList.dtls.list
        .get(0).externalPartyOfficeDtls.externalPartyOfficeID;
    try {
      bdmExternalPartyFacade
        .cancelExternalPartyOffice(externalPartyOfficeKey);
      fail();
    } catch (final InformationalException ie) {
      assertTrue(ie.getLocalizedMessage()
        .contains(BDMEXTERNALPARTY.ERR_EXT_PARTY_OFFICE_APP_LINK_EXISTS
          .getMessageText()));
    }

    TransactionInfo.setInformationalManager();
  }

  @Test
  public void testValidateRegisterExternalPartyDetails()
    throws AppException, InformationalException {

    TransactionInfo.setInformationalManager();

    final ExternalPartyRegistrationDetails registrationDtls =
      new ExternalPartyRegistrationDetails();
    final BDMExternalPartyRegistrationDetails bdmextPartyRegDetails =
      new BDMExternalPartyRegistrationDetails();

    try {
      bdmExternalPartyFacade.validateRegisterExternalPartyDetails(
        registrationDtls, bdmextPartyRegDetails);
      fail();
    } catch (final InformationalException ie) {
      assertTrue(ie.getLocalizedMessage()
        .contains(BDMEXTERNALPARTY.ERR_CENTER_OF_SPCLZN_MUST_BE_ENTERED
          .getMessageText()));
      assertTrue(ie.getLocalizedMessage().contains(
        BDMEXTERNALPARTY.ERR_TOTALIZATION_WITH_3RD_CNTRY_MUST_BE_ENTERED
          .getMessageText()));
      assertTrue(ie.getLocalizedMessage()
        .contains(BDMEXTERNALPARTY.ERR_STATUS_OF_AGREEMENT_MUST_BE_ENTERED
          .getMessageText()));
      assertTrue(ie.getLocalizedMessage().contains(
        BDMEXTERNALPARTY.ERR_PROTECTIVE_DATE_WITH_CNTRY_MUST_BE_ENTERED
          .getMessageText()));
      assertTrue(ie.getLocalizedMessage().contains(
        BDMEXTERNALPARTY.ERR_SSA_LINK_MUST_BE_ENTERED.getMessageText()));
    }

    TransactionInfo.setInformationalManager();

    bdmextPartyRegDetails.centerOfSpeclizn = BDMCOSLOCATION.ALBERTA;
    bdmextPartyRegDetails.totalizatnCountry = BDMYESNO.YES;
    bdmextPartyRegDetails.isProtectiveDate = BDMYESNO.YES;
    bdmextPartyRegDetails.statusOfAgreement = BDMSTATUSOFAGREEMENT.INFORCE;
    bdmextPartyRegDetails.ssaLink =
      "https://www.canada.ca/en/employment-social-development.html";

    registrationDtls.externalPartyRegistrationDetails.externalPartyDtls.name =
      BDMConstants.kCanada.toUpperCase();
    registrationDtls.externalPartyRegistrationDetails.addressData =
      "1\n" + "0\n" + "BDMINTL\n" + "CA\n" + "0\n" + "0\n" + "COUNTRY=CA\n"
        + "POSTCODE=\n" + "ZIP=\n" + "BDMZIPX=\n" + "APT=\n" + "POBOXNO=\n"
        + "ADD1=1234\n" + "ADD2=Bay St\n" + "CITY=Toronto\n" + "PROV=\n"
        + "STATEPROV=\n" + "BDMSTPROVX=\n" + "BDMUNPARSE=";

    try {
      bdmExternalPartyFacade.validateRegisterExternalPartyDetails(
        registrationDtls, bdmextPartyRegDetails);
      fail();
    } catch (final InformationalException ie) {
      assertTrue(ie.getLocalizedMessage().contains(
        BDMEXTERNALPARTY.ERR_CANADA_CAN_NOT_BE_SELECTED_AS_A_FOREIGN_COUNTRY
          .getMessageText()));
    }

    TransactionInfo.setInformationalManager();

    registrationDtls.externalPartyRegistrationDetails.externalPartyDtls.name =
      "Angola";
    registrationDtls.externalPartyRegistrationDetails.addressData =
      "1\n" + "0\n" + "BDMINTL\n" + "IE\n" + "0\n" + "0\n" + "COUNTRY=AO\n"
        + "POSTCODE=\n" + "ZIP=\n" + "BDMZIPX=\n" + "APT=\n" + "POBOXNO=\n"
        + "ADD1=1234\n" + "ADD2=Bay St\n" + "CITY=Toronto\n" + "PROV=\n"
        + "STATEPROV=\n" + "BDMSTPROVX=\n" + "BDMUNPARSE=";

    try {
      bdmExternalPartyFacade.validateRegisterExternalPartyDetails(
        registrationDtls, bdmextPartyRegDetails);
      fail();
    } catch (final InformationalException ie) {
      assertTrue(ie.getLocalizedMessage().contains(
        BDMEXTERNALPARTY.ERR_EXT_PARTY_NAME_DOES_NOT_MATCH_WITH_SELCTD_CNTRY
          .getMessageText()));
      assertTrue(ie.getLocalizedMessage()
        .contains(BDMEXTERNALPARTY.ERR_EXT_PARTY_NAME_COUNTRY_ALREADY_EXISTS
          .getMessageText()));
    }

    TransactionInfo.setInformationalManager();
    bdmextPartyRegDetails.centerOfSpeclizn = BDMCOSLOCATION.ALBERTA;
    bdmextPartyRegDetails.totalizatnCountry = BDMYESNO.YES;
    bdmextPartyRegDetails.isProtectiveDate = BDMYESNO.YES;
    bdmextPartyRegDetails.statusOfAgreement = BDMSTATUSOFAGREEMENT.INFORCE;
    bdmextPartyRegDetails.ssaLink =
      "https://www.canada.ca/en/employment-social-development.html";

    registrationDtls.externalPartyRegistrationDetails.externalPartyDtls.name =
      "Andorra";
    registrationDtls.externalPartyRegistrationDetails.addressData =
      "1\n" + "0\n" + "BDMINTL\n" + "IE\n" + "0\n" + "0\n" + "COUNTRY=AD\n"
        + "POSTCODE=\n" + "ZIP=\n" + "BDMZIPX=\n" + "APT=\n" + "POBOXNO=\n"
        + "ADD1=1234\n" + "ADD2=Bay St\n" + "CITY=Toronto\n" + "PROV=\n"
        + "STATEPROV=\n" + "BDMSTPROVX=\n" + "BDMUNPARSE=";
    bdmextPartyRegDetails.revisedAgrmntDate =
      Date.getCurrentDate().addDays(-10);
    bdmextPartyRegDetails.forceEntryDate = Date.getCurrentDate().addDays(-5);

    try {
      bdmExternalPartyFacade.validateRegisterExternalPartyDetails(
        registrationDtls, bdmextPartyRegDetails);
      fail();
    } catch (final InformationalException ie) {
      assertTrue(ie.getLocalizedMessage().contains(
        BDMEXTERNALPARTY.ERR_DATE_OF_REVISED_AGREEMENT_MUST_BE_AFTER_DATE_OF_ENTRY_INTO_FORCE
          .getMessageText()));
    }

    TransactionInfo.setInformationalManager();

    bdmextPartyRegDetails.revisedAgrmntDate =
      Date.getCurrentDate().addDays(-9);
    bdmextPartyRegDetails.forceEntryDate = Date.getCurrentDate().addDays(-10);
    bdmextPartyRegDetails.ssaEndDate = Date.getCurrentDate().addDays(-11);

    try {
      bdmExternalPartyFacade.validateRegisterExternalPartyDetails(
        registrationDtls, bdmextPartyRegDetails);
      fail();
    } catch (final InformationalException ie) {
      assertTrue(ie.getLocalizedMessage().contains(
        BDMEXTERNALPARTY.ERR_END_DATE_MUST_BE_AFTER_DATE_OF_ENTRY_INTO_FORCE
          .getMessageText()));
    }

    TransactionInfo.setInformationalManager();

    bdmextPartyRegDetails.revisedAgrmntDate =
      Date.getCurrentDate().addDays(-5);
    bdmextPartyRegDetails.forceEntryDate = Date.kZeroDate;
    bdmextPartyRegDetails.ssaEndDate = Date.getCurrentDate().addDays(-6);

    try {
      bdmExternalPartyFacade.validateRegisterExternalPartyDetails(
        registrationDtls, bdmextPartyRegDetails);
      fail();
    } catch (final InformationalException ie) {
      assertEquals(
        BDMEXTERNALPARTY.ERR_END_DATE_MUST_BE_AFTER_THE_DATE_OF_REVISED_AGREEMENT
          .getMessageText(),
        ie.getLocalizedMessage());
    }

    TransactionInfo.setInformationalManager();

    bdmextPartyRegDetails.revisedAgrmntDate =
      Date.getCurrentDate().addDays(-5);
    bdmextPartyRegDetails.forceEntryDate = Date.getCurrentDate().addDays(-5);
    bdmextPartyRegDetails.ssaEndDate = Date.getCurrentDate().addDays(-5);
    bdmextPartyRegDetails.qcEntryStartDate =
      Date.getCurrentDate().addDays(-1);

    try {
      bdmExternalPartyFacade.validateRegisterExternalPartyDetails(
        registrationDtls, bdmextPartyRegDetails);
      fail();
    } catch (final InformationalException ie) {
      assertEquals(
        BDMEXTERNALPARTY.ERR_END_DATE_MUST_BE_AFTER_QUBEC_START_DATE
          .getMessageText(),
        ie.getLocalizedMessage());
    }

    TransactionInfo.setInformationalManager();

    bdmextPartyRegDetails.forceEntryDate = Date.getCurrentDate().addDays(-5);
    bdmextPartyRegDetails.statusOfAgreement = BDMSTATUSOFAGREEMENT.NOTINFORCE;

    try {
      bdmExternalPartyFacade.validateRegisterExternalPartyDetails(
        registrationDtls, bdmextPartyRegDetails);
      fail();
    } catch (final InformationalException ie) {
      assertTrue(ie.getLocalizedMessage().contains(
        BDMEXTERNALPARTY.ERR_DATE_OF_ENTRY_INTO_FORCE_CAN_NOT_BE_ENTERED
          .getMessageText()));
    }

    TransactionInfo.setInformationalManager();

    bdmextPartyRegDetails.qcEntryStartDate = Date.kZeroDate;
    bdmextPartyRegDetails.qcEntryEndDate = Date.getCurrentDate().addDays(-5);
    try {
      bdmExternalPartyFacade.validateRegisterExternalPartyDetails(
        registrationDtls, bdmextPartyRegDetails);
      fail();
    } catch (final InformationalException ie) {
      assertTrue(ie.getLocalizedMessage().contains(
        BDMEXTERNALPARTY.ERR_QUBEC_START_DATE_CAN_NOT_BE_EMPTY_IF_QUBEC_END_DATE_ENTERED
          .getMessageText()));
    }

    TransactionInfo.setInformationalManager();

    bdmextPartyRegDetails.statusOfAgreement = BDMSTATUSOFAGREEMENT.NOTINFORCE;

    try {
      bdmExternalPartyFacade.validateRegisterExternalPartyDetails(
        registrationDtls, bdmextPartyRegDetails);
      fail();
    } catch (final InformationalException ie) {
      assertTrue(ie.getLocalizedMessage().contains(
        BDMEXTERNALPARTY.ERR_DATE_OF_ENTRY_INTO_FORCE_CAN_NOT_BE_ENTERED
          .getMessageText()));
    }

    TransactionInfo.setInformationalManager();

    bdmextPartyRegDetails.qcEntryStartDate =
      Date.getCurrentDate().addDays(-5);
    bdmextPartyRegDetails.qcEntryEndDate = Date.getCurrentDate().addDays(-10);

    try {
      bdmExternalPartyFacade.validateRegisterExternalPartyDetails(
        registrationDtls, bdmextPartyRegDetails);
      fail();
    } catch (final InformationalException ie) {
      assertTrue(ie.getLocalizedMessage().contains(
        BDMEXTERNALPARTY.ERR_QUBEC_END_DATE_MUST_BE_AFTER_THE_QUBEC_START_DATE
          .getMessageText()));
    }

    TransactionInfo.setInformationalManager();

    bdmextPartyRegDetails.qcEntryStartDate =
      Date.getCurrentDate().addDays(-10);
    bdmextPartyRegDetails.qcEntryEndDate = Date.getCurrentDate().addDays(-5);
    bdmextPartyRegDetails.statusOfAgreement =
      BDMSTATUSOFAGREEMENT.INFORMATIONONLY;
    bdmextPartyRegDetails.totalizatnCountry = BDMYESNO.YES;

    try {
      bdmExternalPartyFacade.validateRegisterExternalPartyDetails(
        registrationDtls, bdmextPartyRegDetails);
      fail();
    } catch (final InformationalException ie) {
      assertTrue(ie.getLocalizedMessage().contains(
        BDMEXTERNALPARTY.ERR_TOTALIZATION_WITH_THIRD_COUNTRY_CAN_NOT_BE_YES
          .getMessageText()));
    }

    TransactionInfo.setInformationalManager();

    bdmextPartyRegDetails.statusOfAgreement = BDMSTATUSOFAGREEMENT.INFORCE;
    bdmextPartyRegDetails.totalizatnCountry = "AD";

    bdmExternalPartyFacade.validateRegisterExternalPartyDetails(
      registrationDtls, bdmextPartyRegDetails);

    TransactionInfo.setInformationalManager();

    this.createSSACountry();

    registrationDtls.externalPartyRegistrationDetails.addressData =
      "1\n" + "0\n" + "BDMINTL\n" + "IE\n" + "0\n" + "0\n" + "COUNTRY=AD\n"
        + "POSTCODE=\n" + "ZIP=\n" + "BDMZIPX=\n" + "APT=\n" + "POBOXNO=\n"
        + "ADD1=1234\n" + "ADD2=Bay St\n" + "CITY=Toronto\n" + "PROV=\n"
        + "STATEPROV=\n" + "BDMSTPROVX=\n" + "BDMUNPARSE=";

    try {
      bdmExternalPartyFacade.validateRegisterExternalPartyDetails(
        registrationDtls, bdmextPartyRegDetails);
      fail();
    } catch (final InformationalException ie) {
      assertTrue(ie.getLocalizedMessage()
        .contains(BDMEXTERNALPARTY.ERR_EXT_PARTY_NAME_COUNTRY_ALREADY_EXISTS
          .getMessageText()));
    }
  }

  @Test
  public void testValidateModifyExternalPartyDetails()
    throws AppException, InformationalException {

    final BDMExternalPartyModifyDetails details =
      new BDMExternalPartyModifyDetails();

    try {
      bdmExternalPartyFacade.validateModifyExternalPartyDetails(details);
      fail();
    } catch (final InformationalException ie) {
      assertTrue(ie.getLocalizedMessage()
        .contains(BDMEXTERNALPARTY.ERR_CENTER_OF_SPCLZN_MUST_BE_ENTERED
          .getMessageText()));
      assertTrue(ie.getLocalizedMessage().contains(
        BDMEXTERNALPARTY.ERR_TOTALIZATION_WITH_3RD_CNTRY_MUST_BE_ENTERED
          .getMessageText()));
      assertTrue(ie.getLocalizedMessage()
        .contains(BDMEXTERNALPARTY.ERR_STATUS_OF_AGREEMENT_MUST_BE_ENTERED
          .getMessageText()));
      assertTrue(ie.getLocalizedMessage().contains(
        BDMEXTERNALPARTY.ERR_PROTECTIVE_DATE_WITH_CNTRY_MUST_BE_ENTERED
          .getMessageText()));
      assertTrue(ie.getLocalizedMessage().contains(
        BDMEXTERNALPARTY.ERR_SSA_LINK_MUST_BE_ENTERED.getMessageText()));

    }

    TransactionInfo.setInformationalManager();

    details.centerOfSpeclizn = "COS";
    details.totalizatnCountry = BDMYESNO.NO;
    details.statusOfAgreement = BDMSTATUSOFAGREEMENT.NOTINFORCE;
    details.isProtectiveDate = BDMYESNO.NO;
    details.ssaLink = "LNK";

    // Canada cannot be selected as a Foreign Country
    details.details.externalPartyDtls.name = BDMConstants.kCanada;

    try {
      bdmExternalPartyFacade.validateModifyExternalPartyDetails(details);
      fail();
    } catch (final InformationalException ie) {
      assertTrue(ie.getLocalizedMessage().contains(
        BDMEXTERNALPARTY.ERR_CANADA_CAN_NOT_BE_SELECTED_AS_A_FOREIGN_COUNTRY
          .getMessageText()));
    }

    TransactionInfo.setInformationalManager();

    details.details.externalPartyDtls.name = "United Kingdom";
    details.details.externalPartyDtls.concernRoleID = 80011L;

    try {
      bdmExternalPartyFacade.validateModifyExternalPartyDetails(details);
      fail();
    } catch (final InformationalException ie) {
      assertTrue(ie.getLocalizedMessage()
        .contains(BDMEXTERNALPARTY.ERR_EXT_PARTY_NAME_COUNTRY_ALREADY_EXISTS
          .getMessageText()));
    }

    TransactionInfo.setInformationalManager();

    details.details.externalPartyDtls.name = "Andorra";
    details.details.externalPartyDtls.concernRoleID = 8001000001L;

    details.revisedAgrmntDate = Date.getCurrentDate().addDays(-21);
    details.forceEntryDate = Date.getCurrentDate().addDays(-20);

    try {
      bdmExternalPartyFacade.validateModifyExternalPartyDetails(details);
      fail();
    } catch (final InformationalException ie) {
      assertTrue(ie.getLocalizedMessage().contains(
        BDMEXTERNALPARTY.ERR_DATE_OF_REVISED_AGREEMENT_MUST_BE_AFTER_DATE_OF_ENTRY_INTO_FORCE
          .getMessageText()));
    }

    TransactionInfo.setInformationalManager();

    details.revisedAgrmntDate = Date.getCurrentDate().addDays(-20);
    details.forceEntryDate = Date.getCurrentDate().addDays(-20);

    details.ssaEndDate = Date.getCurrentDate().addDays(-21);

    try {
      bdmExternalPartyFacade.validateModifyExternalPartyDetails(details);
      fail();
    } catch (final InformationalException ie) {
      assertTrue(ie.getLocalizedMessage().contains(
        BDMEXTERNALPARTY.ERR_END_DATE_MUST_BE_AFTER_DATE_OF_ENTRY_INTO_FORCE
          .getMessageText()));
      assertTrue(ie.getLocalizedMessage().contains(
        BDMEXTERNALPARTY.ERR_END_DATE_MUST_BE_AFTER_THE_DATE_OF_REVISED_AGREEMENT
          .getMessageText()));
    }

    TransactionInfo.setInformationalManager();

    details.ssaEndDate = Date.getCurrentDate().addDays(-20);
    details.qcEntryStartDate = Date.getCurrentDate().addDays(-19);

    try {
      bdmExternalPartyFacade.validateModifyExternalPartyDetails(details);
      fail();
    } catch (final InformationalException ie) {
      assertTrue(ie.getLocalizedMessage()
        .contains(BDMEXTERNALPARTY.ERR_END_DATE_MUST_BE_AFTER_QUBEC_START_DATE
          .getMessageText()));
    }

    TransactionInfo.setInformationalManager();

    details.statusOfAgreement = BDMSTATUSOFAGREEMENT.NOTINFORCE;
    details.forceEntryDate = Date.getCurrentDate().addDays(-20);

    try {
      bdmExternalPartyFacade.validateModifyExternalPartyDetails(details);
      fail();
    } catch (final InformationalException ie) {
      assertTrue(ie.getLocalizedMessage().contains(
        BDMEXTERNALPARTY.ERR_DATE_OF_ENTRY_INTO_FORCE_CAN_NOT_BE_ENTERED
          .getMessageText()));
    }

    TransactionInfo.setInformationalManager();

    details.forceEntryDate = Date.kZeroDate;
    details.qcEntryStartDate = Date.kZeroDate;
    details.qcEntryEndDate = Date.getCurrentDate().addDays(-20);

    try {
      bdmExternalPartyFacade.validateModifyExternalPartyDetails(details);
      fail();
    } catch (final InformationalException ie) {
      assertTrue(ie.getLocalizedMessage().contains(
        BDMEXTERNALPARTY.ERR_QUBEC_START_DATE_CAN_NOT_BE_EMPTY_IF_QUBEC_END_DATE_ENTERED
          .getMessageText()));
    }

    TransactionInfo.setInformationalManager();

    details.forceEntryDate = Date.kZeroDate;
    details.qcEntryStartDate = Date.getCurrentDate().addDays(-10);
    details.qcEntryEndDate = Date.getCurrentDate().addDays(-20);

    try {
      bdmExternalPartyFacade.validateModifyExternalPartyDetails(details);
      fail();
    } catch (final InformationalException ie) {
      assertTrue(ie.getLocalizedMessage().contains(
        BDMEXTERNALPARTY.ERR_QUBEC_END_DATE_MUST_BE_AFTER_THE_QUBEC_START_DATE
          .getMessageText()));
    }

    TransactionInfo.setInformationalManager();

    details.qcEntryStartDate = Date.kZeroDate;
    details.qcEntryEndDate = Date.kZeroDate;

    details.statusOfAgreement = BDMSTATUSOFAGREEMENT.INFORMATIONONLY;
    details.totalizatnCountry = BDMYESNO.YES;

    try {
      bdmExternalPartyFacade.validateModifyExternalPartyDetails(details);
      fail();
    } catch (final InformationalException ie) {
      assertTrue(ie.getLocalizedMessage().contains(
        BDMEXTERNALPARTY.ERR_TOTALIZATION_WITH_THIRD_COUNTRY_CAN_NOT_BE_YES
          .getMessageText()));
    }

    TransactionInfo.setInformationalManager();

    details.totalizatnCountry = BDMYESNO.NO;

    bdmExternalPartyFacade.validateModifyExternalPartyDetails(details);

    TransactionInfo.setInformationalManager();
  }

  @Test
  public void testValidateCancelOfficeMember()
    throws AppException, InformationalException {

    final PersonRegistrationResult crDtls = registerConcernrole();
    // create an office member
    final BDMExternalPartyOfficeList bdmExternalPartyOfficeList =
      listExternalPartyOffice();
    final ExternalPartyOfficeMemberDtls offMemberDtls =
      new ExternalPartyOfficeMemberDtls();
    offMemberDtls.officeMemberID = UniqueID.nextUniqueID();
    offMemberDtls.comments = "Test";
    offMemberDtls.concernRoleID = crDtls.registrationIDDetails.concernRoleID;
    offMemberDtls.externalPartyOfficeID = bdmExternalPartyOfficeList.dtls.list
      .get(0).externalPartyOfficeDtls.externalPartyOfficeID;
    offMemberDtls.profile = "Test";
    offMemberDtls.recordStatus = RECORDSTATUS.NORMAL;
    ExternalPartyOfficeMemberFactory.newInstance().insert(offMemberDtls);

    final ExternalPartyOfficeMemberKey officeMemberKey =
      new ExternalPartyOfficeMemberKey();
    officeMemberKey.officeMemberID = offMemberDtls.officeMemberID;

    bdmExternalPartyFacade.validateCancelOfficeMember(officeMemberKey);
  }

  @Test
  public void testValidateCancelOfficeMember_thirdPartyContact()
    throws AppException, InformationalException {

    final PersonRegistrationResult crDtls = registerConcernrole();
    // create an office member
    final BDMExternalPartyOfficeList bdmExternalPartyOfficeList =
      listExternalPartyOffice();
    final ExternalPartyOfficeMemberDtls offMemberDtls =
      new ExternalPartyOfficeMemberDtls();
    offMemberDtls.officeMemberID = UniqueID.nextUniqueID();
    offMemberDtls.comments = "Test";
    offMemberDtls.concernRoleID = crDtls.registrationIDDetails.concernRoleID;
    offMemberDtls.externalPartyOfficeID = bdmExternalPartyOfficeList.dtls.list
      .get(0).externalPartyOfficeDtls.externalPartyOfficeID;
    offMemberDtls.profile = "Test";
    offMemberDtls.recordStatus = RECORDSTATUS.NORMAL;
    ExternalPartyOfficeMemberFactory.newInstance().insert(offMemberDtls);

    final ExternalPartyOfficeMemberKey officeMemberKey =
      new ExternalPartyOfficeMemberKey();
    officeMemberKey.officeMemberID = offMemberDtls.officeMemberID;

    final ThirdPartyEvidenceWizardDetailsResult thirdPartyEvd =
      new ThirdPartyEvidenceWizardDetailsResult();

    final BDMEvidenceUtil bdmEvidenceUtil = new BDMEvidenceUtil();
    thirdPartyEvd.thirdPartyDetails.participantRoleID =
      offMemberDtls.concernRoleID;

    final ParticipantRoleIDAndTypeCodeKey cprSearchKey =
      new ParticipantRoleIDAndTypeCodeKey();
    cprSearchKey.participantRoleID = offMemberDtls.concernRoleID;
    cprSearchKey.typeCode = CASEPARTICIPANTROLETYPE.PRIMARY;
    final CaseParticipantRoleDtlsList cprList =
      curam.core.sl.entity.fact.CaseParticipantRoleFactory.newInstance()
        .searchByParticipantRoleIDTypeCode(cprSearchKey);

    final ConcernRoleKey crKey = new ConcernRoleKey();
    crKey.concernRoleID = offMemberDtls.concernRoleID;
    final ConcernRoleDtls memberCrDtls =
      ConcernRoleFactory.newInstance().read(crKey);

    final CaseParticipantRoleDtls cprDtls = cprList.dtls.get(0);
    thirdPartyEvd.thirdPartyDetails.caseID = cprDtls.caseID;
    thirdPartyEvd.selectedIndividual.concernRoleName =
      memberCrDtls.concernRoleName;

    bdmEvidenceUtil.createThirdPartyContactEvidence(thirdPartyEvd);

    try {
      bdmExternalPartyFacade.validateCancelOfficeMember(officeMemberKey);
      fail();
    } catch (final InformationalException ie) {
      assertTrue(ie.getLocalizedMessage().contains(
        BDMTHIRDPARTYCONTACT.ERR_CANNOT_DELETE_OFFICE_MEMBER_WHO_IS_THIRD_PARTY_CONTACT
          .getMessageText()));
    }

    TransactionInfo.setInformationalManager();
  }

  @Test
  public void testCancelOfficeMember()
    throws AppException, InformationalException {

    final PersonRegistrationResult crDtls = registerConcernrole();
    final BDMExternalPartyOfficeList bdmExternalPartyOfficeList =
      listExternalPartyOffice();
    // create an office member
    final ExternalPartyOfficeMemberDtls offMemberDtls =
      new ExternalPartyOfficeMemberDtls();
    offMemberDtls.officeMemberID = UniqueID.nextUniqueID();
    offMemberDtls.comments = "Test";
    offMemberDtls.concernRoleID = crDtls.registrationIDDetails.concernRoleID;
    offMemberDtls.externalPartyOfficeID = bdmExternalPartyOfficeList.dtls.list
      .get(0).externalPartyOfficeDtls.externalPartyOfficeID;
    offMemberDtls.profile = "Test";
    offMemberDtls.recordStatus = RECORDSTATUS.NORMAL;
    ExternalPartyOfficeMemberFactory.newInstance().insert(offMemberDtls);

    final ExternalPartyOfficeMemberKey officeMemberKey =
      new ExternalPartyOfficeMemberKey();
    officeMemberKey.officeMemberID = offMemberDtls.officeMemberID;

    bdmExternalPartyFacade.cancelOfficeMember(officeMemberKey);
  }

  protected PersonRegistrationResult registerConcernrole()
    throws AppException, InformationalException {

    // BEGIN: REGISTER PERSON
    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = "Save";

    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "Test";
    bdmPersonRegistrationDetails.dtls.surname = "OfficeMember";
    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("20230101");
    bdmPersonRegistrationDetails.dtls.socialSecurityNumber = "519132708";
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

    assertTrue(
      registrationResult.registrationIDDetails.alternateID.length() > 0);

    assertTrue(registrationResult.registrationIDDetails.concernRoleID != 0);

    return registrationResult;
  }

  @Test
  public void testReadExternalPartyHomePageDetails()
    throws AppException, InformationalException {

    final BDMExternalPartyTestData extPartyTestDataObj =
      new BDMExternalPartyTestData();

    final BDMExternalPartyTestDataDetails extPartyTestDataDetails =
      extPartyTestDataObj.setRegisterExternalPartyDetails();

    final ExternalPartyKey key = new ExternalPartyKey();
    key.concernRoleID = extPartyTestDataDetails.extPartyCRoleID;

    assertNotNull(
      bdmExternalPartyFacade.readExternalPartyHomePageDetails(key));
  }

  @Test
  public void testListSSACountries()
    throws AppException, InformationalException {

    bdmExternalPartyFacade.listSSACountries();

  }

  @Test
  public void testListExternalPartyTypeCodetableItems()
    throws AppException, InformationalException {

    bdmExternalPartyFacade.listExternalPartyTypeCodetableItems();
  }

  @Test
  public void testListExternalPartyHistory()
    throws AppException, InformationalException {

    final BDMReadExternalPartyKey key = new BDMReadExternalPartyKey();
    bdmExternalPartyFacade.listExternalPartyHistory(key);
  }

  @Test
  public void testReadExternalPartyHistoryDetails()
    throws AppException, InformationalException {

    final BDMExternalPartyTestData extPartyTestDataObj =
      new BDMExternalPartyTestData();

    final BDMExternalPartyTestDataDetails extPartyTestDataDetails =
      extPartyTestDataObj.setRegisterExternalPartyDetails();

    final ExternalPartyKey key = new ExternalPartyKey();
    key.concernRoleID = extPartyTestDataDetails.extPartyCRoleID;

    final BDMExtPartyHistSearchKey searchKey = new BDMExtPartyHistSearchKey();
    searchKey.concernRoleID = key.concernRoleID;
    final BDMExtPartyHistoryDetailsList histList =
      BDMExternalPartyHistoryFactory.newInstance()
        .readHistoryByExtPartyID(searchKey);

    final BDMReadExternalPartyHistoryKey hKey =
      new BDMReadExternalPartyHistoryKey();
    hKey.extPartyHistoryID = histList.dtls.get(0).extPartyHistoryID;
    bdmExternalPartyFacade.readExternalPartyHistoryDetails(hKey);
  }

  @Test
  public void testModifyAddress()
    throws AppException, InformationalException {

    final ParticipantRegistrationWizardResult ssaCountry = createSSACountry();
    final ExternalPartyKey externalPartyKey = new ExternalPartyKey();
    externalPartyKey.concernRoleID =
      ssaCountry.registrationResult.concernRoleID;

    final ConcernRoleKey crKey = new ConcernRoleKey();
    crKey.concernRoleID = externalPartyKey.concernRoleID;
    final ConcernRoleDtls crDtls =
      ConcernRoleFactory.newInstance().read(crKey);

    createExternalPartyAddress(externalPartyKey.concernRoleID);

    final AddressReadMultiKey addrSearchKey = new AddressReadMultiKey();
    addrSearchKey.concernRoleID = externalPartyKey.concernRoleID;
    final AddressReadMultiDtlsList crAddrList = ConcernRoleAddressFactory
      .newInstance().searchAddressesByConcernRole(addrSearchKey);

    AddressReadMultiDtls crAddrDtls = crAddrList.dtls.get(0);
    for (final AddressReadMultiDtls dtls : crAddrList.dtls) {
      if (dtls.addressID != crDtls.primaryAddressID) {
        crAddrDtls = dtls;
        break;
      }
    }

    final MaintainParticipantAddressDetails details =
      new MaintainParticipantAddressDetails();

    details.addressDetails.concernRoleID = externalPartyKey.concernRoleID;
    details.addressDetails.startDate = Date.getCurrentDate();
    details.addressDetails.assign(crAddrDtls);
    details.addressDetails.primaryAddressInd = false;

    bdmExternalPartyFacade.modifyAddress(details);
  }

  @Test
  public void testModifyAddress_CA()
    throws AppException, InformationalException {

    final ParticipantRegistrationWizardResult ssaCountry = createSSACountry();
    final ExternalPartyKey externalPartyKey = new ExternalPartyKey();
    externalPartyKey.concernRoleID =
      ssaCountry.registrationResult.concernRoleID;

    final ConcernRoleKey crKey = new ConcernRoleKey();
    crKey.concernRoleID = externalPartyKey.concernRoleID;
    final ConcernRoleDtls crDtls =
      ConcernRoleFactory.newInstance().read(crKey);

    createExternalPartyAddress(externalPartyKey.concernRoleID);

    final AddressReadMultiKey addrSearchKey = new AddressReadMultiKey();
    addrSearchKey.concernRoleID = externalPartyKey.concernRoleID;
    final AddressReadMultiDtlsList crAddrList = ConcernRoleAddressFactory
      .newInstance().searchAddressesByConcernRole(addrSearchKey);

    AddressReadMultiDtls crAddrDtls = crAddrList.dtls.get(0);
    for (final AddressReadMultiDtls dtls : crAddrList.dtls) {
      if (dtls.addressID != crDtls.primaryAddressID) {
        crAddrDtls = dtls;
        break;
      }
    }

    final MaintainParticipantAddressDetails details =
      new MaintainParticipantAddressDetails();

    details.addressDetails.concernRoleID = externalPartyKey.concernRoleID;
    details.addressDetails.startDate = Date.getCurrentDate();
    details.addressDetails.assign(crAddrDtls);
    details.addressDetails.primaryAddressInd = false;

    details.addressDetails.addressData =
      "1\n" + "1\n" + "BDMINTL\n" + "CA\n" + "1\n" + "1\n" + "POBOXNO=\n"
        + "APT=\n" + "ADD1=1153\n" + "ADD2=Mole Street\n" + "CITY=Midway\n"
        + "PROV=\n" + "STATEPROV=\n" + "BDMSTPROVX=UT\n" + "COUNTRY=CA\n"
        + "POSTCODE=\n" + "ZIP=\n" + "BDMZIPX=60656\n" + "BDMUNPARSE=";

    try {
      bdmExternalPartyFacade.modifyAddress(details);
      fail();
    } catch (final AppException ae) {
      assertEquals(
        BDMEXTERNALPARTY.ERR_CANADA_CAN_NOT_BE_SELECTED_AS_A_FOREIGN_COUNTRY,
        ae.getCatEntry());
    }

    TransactionInfo.setInformationalManager();
  }

  @Test
  public void testModifyOfficeAddress()
    throws AppException, InformationalException {

    final BDMExternalPartyOfficeList officeList = listExternalPartyOffice();
    final ExternalPartyOfficeDetails officeDetails =
      officeList.dtls.list.get(0);

    final ExternalPartyOfficeKey officeKey = new ExternalPartyOfficeKey();
    officeKey.externalPartyOfficeID =
      officeDetails.externalPartyOfficeDtls.externalPartyOfficeID;

    final ExternalPartyOfficeAddressDtls officeAddressDtls =
      this.createExternalOfficeAddress(officeKey.externalPartyOfficeID);

    final ExternalPartyOfficeAddressDetails details =
      new ExternalPartyOfficeAddressDetails();
    details.officeAddressDtls.externalPartyOfficeID =
      officeDetails.externalPartyOfficeDtls.externalPartyOfficeID;
    details.officeAddressDtls.externalPartyOfficeID =
      officeDetails.externalPartyOfficeDtls.externalPartyOfficeID;
    details.officeAddressDtls.officeAddressID =
      officeAddressDtls.officeAddressID;
    details.officeAddressDtls.assign(officeAddressDtls);
    details.addressDtls = officeDetails.addressDtls;

    details.addressDtls.addressData =
      "1\n" + "0\n" + "BDMINTL\n" + "AO\n" + "0\n" + "0\n" + "COUNTRY=AO\n"
        + "POSTCODE=\n" + "ZIP=\n" + "BDMZIPX=\n" + "APT=\n" + "POBOXNO=\n"
        + "ADD1=1234\n" + "ADD2=Bay St\n" + "CITY=Toronto\n" + "PROV=\n"
        + "STATEPROV=\n" + "BDMSTPROVX=\n" + "BDMUNPARSE=";

    final InformationMsgDtlsList infoMsgDtlsList =
      bdmExternalPartyFacade.modifyOfficeAddress(details);

    assertTrue(infoMsgDtlsList.informationalMsgDtlsList.dtls.isEmpty());
  }

  @Test
  public void testListOfficeAddress()
    throws AppException, InformationalException {

    final BDMExternalPartyOfficeList officeList = listExternalPartyOffice();
    final ExternalPartyOfficeDetails officeDetails =
      officeList.dtls.list.get(0);

    final ExternalPartyOfficeKey officeKey = new ExternalPartyOfficeKey();
    officeKey.externalPartyOfficeID =
      officeDetails.externalPartyOfficeDtls.externalPartyOfficeID;
    assertEquals(1,
      bdmExternalPartyFacade.listOfficeAddress(officeKey).dtls.dtls.size());
  }

  @Test
  public void testListExternalPartyAddress()
    throws AppException, InformationalException {

    final ParticipantRegistrationWizardResult ssaCountry = createSSACountry();
    createExternalPartyOffice(ssaCountry.registrationResult.concernRoleID);
    final ExternalPartyKey externalPartyKey = new ExternalPartyKey();
    externalPartyKey.concernRoleID =
      ssaCountry.registrationResult.concernRoleID;
    assertTrue(!bdmExternalPartyFacade
      .listExternalPartyAddress(externalPartyKey).listDetails.dtls.dtls
        .isEmpty());
  }

  @Test
  public void testListExternalPartyTask()
    throws AppException, InformationalException {

    final ParticipantRegistrationWizardResult ssaCountry = createSSACountry();
    final PersonRegistrationResult crDtls = registerConcernrole();

    final ExternalPartyKey extPtyKey = new ExternalPartyKey();
    extPtyKey.concernRoleID = crDtls.registrationIDDetails.concernRoleID;

    createTaskForConcernRole("scott", extPtyKey.concernRoleID);

    bdmExternalPartyFacade.listExternalPartyTask(extPtyKey);
  }

  ///
  /**
   * Test method to test getRegisterExternalPartySearchCriteria();
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testGetRegisterExternalPartySearchCriteria_fromScratch()
    throws AppException, InformationalException {

    final WizardStateID wizardStateID = new WizardStateID();

    final BDMExternalPartySearchWizardKey wizardKey = bdmExternalPartyFacade
      .getRegisterExternalPartySearchCriteria(wizardStateID);

    assertTrue(wizardKey.key.stateID.wizardStateID != 0);

  }

  /**
   * Test method to test setRegisterExternalPartySearchCriteriaDetails();
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSetRegisterExternalPartySearchCriteriaDetails()
    throws AppException, InformationalException {

    final BDMExternalPartyTestData extPartyTestDataObj =
      new BDMExternalPartyTestData();

    final BDMExternalPartyTestDataDetails extPartyTestDataDetails =
      extPartyTestDataObj.getRegisterExtPartySearchCriteria_Empty();

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = extPartyTestDataDetails.wizardStateID;

    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = CuramConst.kNextPageAction;

    final BDMExternalPartySearchWizardKey searchWizardKey =
      new BDMExternalPartySearchWizardKey();

    searchWizardKey.key.searchKey.key.key.concernRoleName = "TestServSupp";
    searchWizardKey.key.searchKey.type = EXTERNALPARTYTYPE.SSACOUNTRY;

    final ParticipantRegistrationWizardSearchDetails details =
      bdmExternalPartyFacade.setRegisterExternalPartySearchCriteriaDetails(
        searchWizardKey, wizardStateID, actionIDProperty);

    final WizardStateID wizardStateID2 = new WizardStateID();
    wizardStateID2.wizardStateID = details.wizardStateID.wizardStateID;

    final BDMExternalPartySearchWizardKey searchWizardKey2 =
      bdmExternalPartyFacade
        .getRegisterExternalPartySearchCriteria(wizardStateID2);

    assertTrue(searchWizardKey2.key.searchKey.type
      .equals(EXTERNALPARTYTYPE.SSACOUNTRY));

    assertTrue(searchWizardKey2.key.searchKey.key.key.concernRoleName
      .equals(searchWizardKey.key.searchKey.key.key.concernRoleName));

  }

  /**
   * Test method to test setRegisterExternalPartySearchCriteriaDetails();
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSetRegisterExternalPartySearchCriteriaDetails_ForSearch()
    throws AppException, InformationalException {

    final BDMExternalPartyTestData extPartyTestDataObj =
      new BDMExternalPartyTestData();

    final BDMExternalPartyTestDataDetails extPartyTestDataDetails =
      extPartyTestDataObj.setRegisterExternalPartyDetails();

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = extPartyTestDataDetails.wizardStateID;

    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = CuramConst.kSearchAction;

    final BDMExternalPartySearchWizardKey searchWizardKey =
      new BDMExternalPartySearchWizardKey();

    searchWizardKey.key.searchKey.key.key.concernRoleName =
      extPartyTestDataDetails.extPartyName;
    searchWizardKey.key.searchKey.type = EXTERNALPARTYTYPE.SSACOUNTRY;

    final ParticipantRegistrationWizardSearchDetails details =
      bdmExternalPartyFacade.setRegisterExternalPartySearchCriteriaDetails(
        searchWizardKey, wizardStateID, actionIDProperty);

    assertTrue(!details.searchResult.dtls.dtlsList.isEmpty());
  }

  /**
   * Test method to test setRegisterExternalPartySearchCriteriaDetails();
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSetRegisterExternalPartySearchCriteriaDetails_ForReset()
    throws AppException, InformationalException {

    final BDMExternalPartyTestData extPartyTestDataObj =
      new BDMExternalPartyTestData();

    final BDMExternalPartyTestDataDetails extPartyTestDataDetails =
      extPartyTestDataObj.getRegisterExtPartySearchCriteria_Empty();

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = extPartyTestDataDetails.wizardStateID;

    ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = CuramConst.kNextPageAction;

    final BDMExternalPartySearchWizardKey searchWizardKey =
      new BDMExternalPartySearchWizardKey();

    searchWizardKey.key.searchKey.key.key.concernRoleName = "TestServSupp";
    searchWizardKey.key.searchKey.type = EXTERNALPARTYTYPE.SSACOUNTRY;

    ParticipantRegistrationWizardSearchDetails details =
      bdmExternalPartyFacade.setRegisterExternalPartySearchCriteriaDetails(
        searchWizardKey, wizardStateID, actionIDProperty);

    actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = CuramConst.kResetAction;

    details =
      bdmExternalPartyFacade.setRegisterExternalPartySearchCriteriaDetails(
        searchWizardKey, wizardStateID, actionIDProperty);

    assertTrue(details.searchResult.dtls.dtlsList.isEmpty());
  }

  /**
   * Test method to test setRegisterExternalPartyDetails();
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSetRegisterExternalPartyDetails()
    throws AppException, InformationalException {

    final BDMExternalPartyTestData extPartyTestDataObj =
      new BDMExternalPartyTestData();

    final BDMExternalPartyTestDataDetails extPartyTestDataDetails =
      extPartyTestDataObj.getRegisterExtPartySearchCriteria_Empty();

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = extPartyTestDataDetails.wizardStateID;

    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = CuramConst.kSaveAction;

    final BDMExternalPartyRegistrationDetails bdmextPartyRegDetails =
      extPartyTestDataObj.getExternalPartyRegistrationCustomDetails();

    final ExternalPartyRegistrationDetails registrationDtls =
      extPartyTestDataObj.getExternalPartyRegistrationDetails_Default();

    extPartyTestDataDetails.extPartyName =
      registrationDtls.externalPartyRegistrationDetails.externalPartyDtls.name;

    final ParticipantRegistrationWizardResult wizardResult =
      bdmExternalPartyFacade.setRegisterExternalPartyDetails(registrationDtls,
        wizardStateID, actionIDProperty, bdmextPartyRegDetails);

    assertTrue(wizardResult.registrationResult.concernRoleID != 0);
  }

  /**
   * Test method to test readExternalPartyHomePageDetails();
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void tesReadExternalPartyHomePageDetails()
    throws AppException, InformationalException {

    final BDMExternalPartyTestData extPartyTestDataObj =
      new BDMExternalPartyTestData();

    final BDMExternalPartyTestDataDetails extPartyTestDataDetails =
      extPartyTestDataObj.setRegisterExternalPartyDetails();

    final ExternalPartyKey key = new ExternalPartyKey();
    key.concernRoleID = extPartyTestDataDetails.extPartyCRoleID;

    final BDMExternalPartyHomePageDetails homeDetails =
      bdmExternalPartyFacade.readExternalPartyHomePageDetails(key);

    assertTrue(homeDetails.details.externalPartyDetails.externalPartyDtls.type
      .equals(EXTERNALPARTYTYPE.SSACOUNTRY));

  }

  /**
   * Test method to test modifyExternalParty();
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testModifyExternalParty()
    throws AppException, InformationalException {

    final BDMExternalPartyTestData extPartyTestDataObj =
      new BDMExternalPartyTestData();

    final BDMExternalPartyTestDataDetails extPartyTestDataDetails =
      extPartyTestDataObj.setRegisterExternalPartyDetails();

    final ExternalPartyKey key = new ExternalPartyKey();
    key.concernRoleID = extPartyTestDataDetails.extPartyCRoleID;

    final BDMExternalPartyHomePageDetails readDetails =
      bdmExternalPartyFacade.readExternalPartyHomePageDetails(key);

    final BDMExternalPartyModifyDetails modifyExtPartyDetails =
      new BDMExternalPartyModifyDetails();

    modifyExtPartyDetails.assign(readDetails);
    modifyExtPartyDetails.details
      .assign(readDetails.details.externalPartyDetails);

    final String modifiedName =
      "Modified" + modifyExtPartyDetails.details.externalPartyDtls.name;

    modifyExtPartyDetails.details.externalPartyDtls.name = modifiedName;

    bdmExternalPartyFacade.modifyExternalParty(modifyExtPartyDetails);

    final BDMExternalPartyHomePageDetails readDetails2 =
      bdmExternalPartyFacade.readExternalPartyHomePageDetails(key);

    assertTrue(
      readDetails2.details.externalPartyDetails.externalPartyDtls.name
        .equals(modifiedName));

  }

  /**
   * Test method to test searchExternalPartyDetails();
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSearchExternalPartyDetails()
    throws AppException, InformationalException {

    final BDMExternalPartyTestData extPartyTestDataObj =
      new BDMExternalPartyTestData();

    final BDMExternalPartyTestDataDetails extPartyTestDataDetails =
      extPartyTestDataObj.setRegisterExternalPartyDetails();

    final ExternalPartyKey key = new ExternalPartyKey();
    key.concernRoleID = extPartyTestDataDetails.extPartyCRoleID;

    final BDMExternalPartyHomePageDetails readDetails =
      bdmExternalPartyFacade.readExternalPartyHomePageDetails(key);

    final ExternalPartySearchKey1 searchKey = new ExternalPartySearchKey1();

    searchKey.key.key.concernRoleName =
      readDetails.details.externalPartyDetails.externalPartyDtls.name;
    searchKey.key.key.concernRoleType =
      readDetails.details.externalPartyDetails.externalPartyDtls.type;

    final BDMExternalPartySearchKey bdmSearchKey =
      new BDMExternalPartySearchKey();

    final ParticipantSearchDetails extPartySearchDetails =
      bdmExternalPartyFacade.searchExternalPartyDetails(searchKey,
        bdmSearchKey);

    assertTrue(!extPartySearchDetails.dtls.dtlsList.isEmpty());
  }

  @Test
  public void testValidateExternalOffice()
    throws AppException, InformationalException {

    final BDMExternalPartyOfficeList officeList = listExternalPartyOffice();
    final ExternalPartyOfficeDetails officeDetails =
      officeList.dtls.list.get(0);

    try {
      bdmExternalPartyFacade.validateExternalOffice(
        officeDetails.externalPartyOfficeDtls.concernRoleID,
        officeDetails.externalPartyOfficeDtls.name);
      TransactionInfo.getInformationalManager().failOperation();
      fail();
    } catch (final InformationalException ie) {
      assertTrue(ie.getLocalizedMessage().contains(
        BDMEXTERNALPARTY.ERR_EXT_PARTY_OFFICE_NAME_EXISTS.getMessageText()));
    }

    TransactionInfo.setInformationalManager();
  }

  @Test
  public void testValidateExternalOfficeAddress()
    throws AppException, InformationalException {

    final BDMExternalPartyOfficeList officeList = listExternalPartyOffice();
    final ExternalPartyOfficeDetails officeDetails =
      officeList.dtls.list.get(0);

    final long concernRoleId =
      officeDetails.externalPartyOfficeDtls.concernRoleID;

    final ExternalPartyKey extPtyKey = new ExternalPartyKey();
    extPtyKey.concernRoleID = concernRoleId;
    final BDMExternalPartyOfficeList extPtyOfficeList =
      bdmExternalPartyFacade.listExternalPartyOffice(extPtyKey);

    assertTrue(!extPtyOfficeList.dtls.list.isEmpty());

    String addressData =
      "1\n" + "1\n" + "BDMINTL\n" + "IE\n" + "1\n" + "1\n" + "POBOXNO=\n"
        + "APT=\n" + "ADD1=1153\n" + "ADD2=Mole Street\n" + "CITY=Midway\n"
        + "PROV=\n" + "STATEPROV=\n" + "BDMSTPROVX=UT\n" + "COUNTRY=AD\n"
        + "POSTCODE=\n" + "ZIP=\n" + "BDMZIPX=60656\n" + "BDMUNPARSE=";

    try {
      bdmExternalPartyFacade.validateExternalOfficeAddress(concernRoleId,
        addressData, 0);
      TransactionInfo.getInformationalManager().failOperation();
      fail();
    } catch (final InformationalException ie) {
      assertTrue(ie.getLocalizedMessage()
        .contains(BDMEXTERNALPARTY.ERR_EXT_PARTY_OFFICE_ADDRESS_EXISTS
          .getMessageText()));
    }

    TransactionInfo.setInformationalManager();

    addressData =
      "1\n" + "1\n" + "BDMINTL\n" + "CA\n" + "1\n" + "1\n" + "POBOXNO=\n"
        + "APT=\n" + "ADD1=1153\n" + "ADD2=Mole Street\n" + "CITY=Midway\n"
        + "PROV=\n" + "STATEPROV=\n" + "BDMSTPROVX=UT\n" + "COUNTRY=CA\n"
        + "POSTCODE=\n" + "ZIP=\n" + "BDMZIPX=60656\n" + "BDMUNPARSE=";

    try {
      bdmExternalPartyFacade.validateExternalOfficeAddress(concernRoleId,
        addressData, 0);
      TransactionInfo.getInformationalManager().failOperation();
      fail();
    } catch (final InformationalException ie) {
      assertTrue(ie.getLocalizedMessage().contains(
        BDMEXTERNALPARTY.ERR_CANADA_CAN_NOT_BE_SELECTED_AS_A_FOREIGN_COUNTRY
          .getMessageText()));
      assertTrue(ie.getLocalizedMessage()
        .contains(BDMEXTERNALPARTY.ERR_EXT_PARTY_OFFICE_ADDRESS_COUNTRY_EXISTS
          .getMessageText()));
    }

    TransactionInfo.setInformationalManager();
  }

}
