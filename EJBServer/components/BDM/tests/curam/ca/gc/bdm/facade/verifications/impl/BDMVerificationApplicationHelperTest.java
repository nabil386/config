package curam.ca.gc.bdm.facade.verifications.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMACCEPTANCESTATUS;
import curam.ca.gc.bdm.codetable.BDMATTACHREJECTIONREASON;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.facade.externalparty.fact.BDMExternalPartyFactory;
import curam.ca.gc.bdm.facade.externalparty.intf.BDMExternalParty;
import curam.ca.gc.bdm.facade.fec.fact.BDMForeignEngagementCaseFactory;
import curam.ca.gc.bdm.facade.fec.struct.BDMFECaseDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMPersonRegistrationDetails;
import curam.ca.gc.bdm.facade.verifications.struct.BDMNewUserProvidedVerItemAndEvdDetails;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.CASEPRIORITY;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.DOCUMENTTYPE;
import curam.codetable.PROVINCETYPE;
import curam.core.facade.fact.PersonFactory;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.facade.struct.CreateIntegratedCaseResultAndMessages;
import curam.core.facade.struct.PersonRegistrationDetails;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.PersonRegistrationFactory;
import curam.core.intf.CaseHeader;
import curam.core.intf.PersonRegistration;
import curam.core.struct.ActiveCasesConcernRoleIDAndTypeKey;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.CaseHeaderDtlsList;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.OtherAddressData;
import curam.core.struct.PersonDtls;
import curam.core.struct.RegistrationIDDetails;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.verification.sl.infrastructure.entity.struct.VDIEDLinkKey;
import curam.verification.sl.infrastructure.struct.CaseIDAndVDIEDLinkIDKey;
import curam.verification.sl.infrastructure.struct.NewUserProvidedVerificationItemDetails;
import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)

public class BDMVerificationApplicationHelperTest
  extends CuramServerTestJUnit4 {

  final BDMExternalParty bdmExtPartyObj =
    BDMExternalPartyFactory.newInstance();

  @Inject
  BDMVerificationApplicationHelper verificationApplicationHelper;

  public BDMVerificationApplicationHelperTest() {

    super();

  }

  @Test
  public void testnewUserProvidedVerificationItem()
    throws AppException, InformationalException {

    final NewUserProvidedVerificationItemDetails details =
      new NewUserProvidedVerificationItemDetails();

    details.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachmentID =
      100000035;
    details.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachedFileInd =
      false;
    details.createVerificationAttachmentLinkDetails.createAttachmentDtls.fileLocation =
      "SUB";
    details.createVerificationAttachmentLinkDetails.createAttachmentDtls.fileReference =
      "subbbb2";
    details.createVerificationAttachmentLinkDetails.createAttachmentDtls.documentType =
      DOCUMENTTYPE.INCOME_RELATED;
    details.createVerificationAttachmentLinkDetails.createAttachmentDtls.receiptDate =
      Date.getCurrentDate();

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final String name = "Original Person";
    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;

    details.itemProvidedDetailsdtls.caseID = pdcID;
    details.itemProvidedDetailsdtls.caseParticipantConcernRoleID =
      originalConcernRoleID;
    details.itemProvidedDetailsdtls.caseParticipantRoleID =
      originalConcernRoleID;
    details.itemProvidedDetailsdtls.participantConcernRoleID =
      originalConcernRoleID;

    final String verificationItemUsageType = "";

    final BDMVerificationApplicationHelper verificationApplicationHelper1 =
      new BDMVerificationApplicationHelper();

    verificationApplicationHelper1.newUserProvidedVerificationItem(details);
    assertTrue(
      details.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachmentID != 0);

  }

  public PDCPersonDetails createOriginalPDCPerson()
    throws AppException, InformationalException {

    final RegisterPerson registerPerson =
      new RegisterPerson("RegisterPerson");

    final curam.core.struct.PersonRegistrationDetails personRegistrationDetails =
      registerPerson.getPersonRegistrationDetails();

    final PersonRegistration personRegistration =
      PersonRegistrationFactory.newInstance();

    personRegistrationDetails.firstForename = "Original Person";
    final RegistrationIDDetails registrationIDDetails =
      personRegistration.registerPerson(personRegistrationDetails);

    final PersonDtls personDtls =
      registerPerson.readPerson(registrationIDDetails.concernRoleID);

    final PDCPersonDetails pdcPersonDetails = new PDCPersonDetails();
    pdcPersonDetails.assign(personRegistrationDetails);
    pdcPersonDetails.assign(personDtls);
    return pdcPersonDetails;

  }

  @Test
  public void testcheckForSecurity()
    throws AppException, InformationalException {

    final VDIEDLinkKey key = new VDIEDLinkKey();
    key.VDIEDLinkID = 123242;

    final BDMVerificationApplicationHelper verificationApplicationHelper1 =
      new BDMVerificationApplicationHelper();

    verificationApplicationHelper1.checkForSecurity(key, (short) 1);
    assertTrue(key.VDIEDLinkID != 0);

  }

  @Test
  public void testnewUserProvidedVerificationItemAE()
    throws AppException, InformationalException {

    final BDMNewUserProvidedVerItemAndEvdDetails details =
      new BDMNewUserProvidedVerItemAndEvdDetails();
    details.bdmNewUserProvidedVerificationItemDetails.acceptanceStatus =
      BDMACCEPTANCESTATUS.BDMVIS8000;
    details.bdmNewUserProvidedVerificationItemDetails.rejectionReason =
      BDMATTACHREJECTIONREASON.BDMVRR8004;

    final BDMVerificationApplicationHelper verificationApplicationHelper1 =
      new BDMVerificationApplicationHelper();

    verificationApplicationHelper1
      .newUserProvidedVerificationItemAndAssociatedEvidences(details);
  }

  @Test
  public void testlistReviewedAndRejectedDocuments()
    throws AppException, InformationalException {

    final CaseIDAndVDIEDLinkIDKey key = new CaseIDAndVDIEDLinkIDKey();
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final String name = "Original Person";
    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;

    key.caseID = pdcID;
    key.vdiedLinkID = 123242;
    final BDMVerificationApplicationHelper verificationApplicationHelper1 =
      new BDMVerificationApplicationHelper();

    verificationApplicationHelper1.listReviewedAndRejectedDocuments(key);
    assertTrue(key.caseID != 0);
  }

  protected CreateIntegratedCaseResultAndMessages createFEC(
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

  protected PersonRegistrationResult registerPerson()
    throws AppException, InformationalException {

    // BEGIN: REGISTER PERSON
    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = "Save";

    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "Homer";
    bdmPersonRegistrationDetails.dtls.surname = "Simpson";
    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19770101");
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

  public void mockUserObjects() throws AppException, InformationalException {

    new MockUp<TransactionInfo>() {

      @Mock
      public String getProgramUser()
        throws AppException, InformationalException {

        return "caseworker";

      }

    };

  }

}
