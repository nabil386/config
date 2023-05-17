package curam.ca.gc.bdm.sl.attachment.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.codetable.BDM_FILE_SOURCE;
import curam.ca.gc.bdm.facade.communication.struct.BDMConcernRoleAttachmentDetails;
import curam.ca.gc.bdm.facade.fec.fact.BDMForeignEngagementCaseFactory;
import curam.ca.gc.bdm.facade.fec.intf.BDMForeignEngagementCase;
import curam.ca.gc.bdm.facade.fec.struct.BDMFECaseDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMPersonRegistrationDetails;
import curam.ca.gc.bdm.facade.struct.BDMCreateCaseAttachmentDetails;
import curam.ca.gc.bdm.sl.attachment.fact.BDMMaintainAttachmentTaskFactory;
import curam.ca.gc.bdm.sl.attachment.intf.BDMMaintainAttachmentTask;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.CASEPRIORITY;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.DOCUMENTTYPE;
import curam.codetable.PROVINCETYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.facade.fact.PersonFactory;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.facade.struct.CreateIntegratedCaseResultAndMessages;
import curam.core.facade.struct.PersonRegistrationDetails;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.fact.AttachmentFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.ConcernRoleAttachmentLinkFactory;
import curam.core.fact.PersonRegistrationFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.impl.CuramConst;
import curam.core.intf.CaseHeader;
import curam.core.intf.PersonRegistration;
import curam.core.struct.ActiveCasesConcernRoleIDAndTypeKey;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.AttachmentDtls;
import curam.core.struct.AttachmentIDAndAttachmentLinkIDStruct;
import curam.core.struct.AttachmentKey;
import curam.core.struct.CaseHeaderDtlsList;
import curam.core.struct.ConcernRoleAttachmentLinkDtls;
import curam.core.struct.OtherAddressData;
import curam.core.struct.PersonDtls;
import curam.core.struct.RegistrationIDDetails;
import curam.dynamicevidence.definition.impl.EvidenceTypeDefDAO;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDefDAO;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)

public class BDMMaintainAttachmentTaskTest extends CuramServerTestJUnit4 {

  BDMForeignEngagementCase kBdmForeignEngagementCaseObj =
    BDMForeignEngagementCaseFactory.newInstance();

  @Inject
  private EvidenceTypeDefDAO evidenceTypeDefDAO;

  @Inject
  private EvidenceTypeVersionDefDAO evidenceTypeVersionDefDAO;

  public BDMMaintainAttachmentTaskTest() {

    super();

  }

  @Test
  public void testcreatePersonAttachmentTask()
    throws AppException, InformationalException {

    final BDMConcernRoleAttachmentDetails details =
      new BDMConcernRoleAttachmentDetails();

    final curam.core.intf.Attachment attachment =
      AttachmentFactory.newInstance();
    final AttachmentKey attachmentKey = new AttachmentKey();

    details.dtls.attachmentDtls.attachmentName = "attachmentName";
    details.dtls.attachmentDtls.documentType = DOCUMENTTYPE.BESS;
    details.dtls.attachmentDtls.fileLocation = "FILE LOCATION";
    details.dtls.attachmentDtls.attachedFileInd = false;
    details.dtls.attachmentDtls.attachmentID =
      UniqueIDFactory.newInstance().getNextID();
    details.dtls.attachmentDtls.attachmentStatus = RECORDSTATUS.NORMAL;
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

    details.dtls.linkDtls.concernRoleID = originalConcernRoleID;

    details.dtls.concernRoleAltID = "ID";
    details.dtls.concernRoleName = "CRA";
    details.dtls.isCancelled = Boolean.TRUE;
    details.fileSource = BDM_FILE_SOURCE.CLIENT;

    final BDMMaintainAttachmentTask maintainTask =
      BDMMaintainAttachmentTaskFactory.newInstance();
    maintainTask.createPersonAttachmentTask(details);
    // assertTrue(check.equals().length > 0);

  }

  @Test
  public void testcreatePersonAttachmentTask_Address()
    throws AppException, InformationalException {

    final BDMConcernRoleAttachmentDetails details =
      new BDMConcernRoleAttachmentDetails();

    final curam.core.intf.Attachment attachment =
      AttachmentFactory.newInstance();
    final AttachmentKey attachmentKey = new AttachmentKey();

    details.dtls.attachmentDtls.attachmentName = "attachmentName";
    details.dtls.attachmentDtls.documentType = DOCUMENTTYPE.ADDRESS;
    details.dtls.attachmentDtls.fileLocation = "FILE LOCATION";
    details.dtls.attachmentDtls.attachedFileInd = false;
    details.dtls.attachmentDtls.attachmentID =
      UniqueIDFactory.newInstance().getNextID();
    details.dtls.attachmentDtls.attachmentStatus = RECORDSTATUS.NORMAL;

    details.dtls.concernRoleAltID = "ID";
    details.dtls.concernRoleName = "concernRoleName";
    details.dtls.isCancelled = Boolean.TRUE;
    details.fileSource = BDM_FILE_SOURCE.CLIENT;
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

    details.dtls.linkDtls.concernRoleID = originalConcernRoleID;
    final BDMMaintainAttachmentTask maintainTask =
      BDMMaintainAttachmentTaskFactory.newInstance();
    maintainTask.createPersonAttachmentTask(details);
    // assertTrue(check.equals().length > 0);

  }

  @Test
  public void testcreatePersonAttachmentTask_nodocument()
    throws AppException, InformationalException {

    final BDMConcernRoleAttachmentDetails details =
      new BDMConcernRoleAttachmentDetails();

    final AttachmentIDAndAttachmentLinkIDStruct attachmentlink =
      new AttachmentIDAndAttachmentLinkIDStruct();
    final curam.core.intf.Attachment attachment =
      AttachmentFactory.newInstance();
    final AttachmentKey attachmentKey = new AttachmentKey();

    details.dtls.attachmentDtls.attachmentName = "attachmentName";
    details.dtls.attachmentDtls.documentType = DOCUMENTTYPE.APPOINTMENT;
    details.dtls.attachmentDtls.fileLocation = "FILE LOCATION";
    details.dtls.attachmentDtls.attachedFileInd = false;
    details.dtls.attachmentDtls.attachmentID =
      UniqueIDFactory.newInstance().getNextID();
    details.dtls.attachmentDtls.attachmentStatus = RECORDSTATUS.NORMAL;

    details.dtls.concernRoleAltID = "ID";
    details.dtls.concernRoleName = "concernRoleName";
    details.dtls.isCancelled = Boolean.TRUE;
    details.fileSource = BDM_FILE_SOURCE.CLIENT;

    final BDMMaintainAttachmentTask maintainTask =
      BDMMaintainAttachmentTaskFactory.newInstance();
    maintainTask.createPersonAttachmentTask(details);
    final AttachmentDtls attachmentDetails = createAttachmentDtls();
    attachmentlink.attachmentID = attachmentDetails.attachmentID;

    // assertTrue(check.equals().length > 0);
    assertTrue(attachmentlink.attachmentID != CuramConst.gkZero);

  }

  @Test
  public void testcreateFECAttachmentTask()
    throws AppException, InformationalException {

    final BDMCreateCaseAttachmentDetails details =
      new BDMCreateCaseAttachmentDetails();

    final AttachmentIDAndAttachmentLinkIDStruct attachmentlink =
      new AttachmentIDAndAttachmentLinkIDStruct();

    final PersonRegistrationResult person = registerPerson();

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

    final AttachmentDtls attachmentDetails = createAttachmentDtls();

    final ConcernRoleAttachmentLinkDtls concernRoleAttachmentLinkDtls =
      createConcernRoleAttachmentLinkDtls(attachmentDetails.attachmentID,
        person.registrationIDDetails.concernRoleID);
    // final long caseID = icResult.createCaseResult.integratedCaseID;

    details.dtls.createCaseAttachmentDetails.caseID = pdcID;
    details.dtls.createCaseAttachmentDetails.caseParticipantRoleID =
      originalConcernRoleID;
    details.dtls.createCaseAttachmentDetails.attachedFileInd = false;
    details.dtls.createCaseAttachmentDetails.documentType =
      DOCUMENTTYPE.APPOINTMENT;
    details.dtls.createCaseAttachmentDetails.attachedFileInd = false;
    attachmentlink.attachmentID = attachmentDetails.attachmentID;

    final BDMMaintainAttachmentTask maintainTask =
      BDMMaintainAttachmentTaskFactory.newInstance();
    maintainTask.createFECAttachmentTask(details, attachmentlink);
    assertTrue(attachmentlink.attachmentID != CuramConst.gkZero);

  }

  @Test
  public void testcreateFECAttachmentTask_document()
    throws AppException, InformationalException {

    final BDMCreateCaseAttachmentDetails details =
      new BDMCreateCaseAttachmentDetails();

    final AttachmentIDAndAttachmentLinkIDStruct attachmentlink =
      new AttachmentIDAndAttachmentLinkIDStruct();

    final PersonRegistrationResult person = registerPerson();

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

    final AttachmentDtls attachmentDetails = createAttachmentDtls();

    final ConcernRoleAttachmentLinkDtls concernRoleAttachmentLinkDtls =
      createConcernRoleAttachmentLinkDtls(attachmentDetails.attachmentID,
        person.registrationIDDetails.concernRoleID);
    // final long caseID = icResult.createCaseResult.integratedCaseID;

    details.dtls.createCaseAttachmentDetails.caseID = pdcID;
    details.dtls.createCaseAttachmentDetails.caseParticipantRoleID =
      originalConcernRoleID;
    details.dtls.createCaseAttachmentDetails.attachedFileInd = false;
    details.dtls.createCaseAttachmentDetails.documentType = DOCUMENTTYPE.BESS;
    details.dtls.createCaseAttachmentDetails.attachedFileInd = false;
    attachmentlink.attachmentID = attachmentDetails.attachmentID;

    final BDMMaintainAttachmentTask maintainTask =
      BDMMaintainAttachmentTaskFactory.newInstance();
    maintainTask.createFECAttachmentTask(details, attachmentlink);
    assertTrue(attachmentlink.attachmentID != CuramConst.gkZero);

  }

  @Test
  public void testcreateFECAttachmentTask_address()
    throws AppException, InformationalException {

    final BDMCreateCaseAttachmentDetails details =
      new BDMCreateCaseAttachmentDetails();

    final AttachmentIDAndAttachmentLinkIDStruct attachmentlink =
      new AttachmentIDAndAttachmentLinkIDStruct();

    final PersonRegistrationResult person = registerPerson();

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

    final AttachmentDtls attachmentDetails = createAttachmentDtls();

    final ConcernRoleAttachmentLinkDtls concernRoleAttachmentLinkDtls =
      createConcernRoleAttachmentLinkDtls(attachmentDetails.attachmentID,
        person.registrationIDDetails.concernRoleID);
    // final long caseID = icResult.createCaseResult.integratedCaseID;

    details.dtls.createCaseAttachmentDetails.caseID = pdcID;
    details.dtls.createCaseAttachmentDetails.caseParticipantRoleID =
      originalConcernRoleID;
    details.dtls.createCaseAttachmentDetails.attachedFileInd = false;
    details.dtls.createCaseAttachmentDetails.documentType =
      DOCUMENTTYPE.ADDRESS;
    details.dtls.createCaseAttachmentDetails.attachedFileInd = false;
    attachmentlink.attachmentID = attachmentDetails.attachmentID;

    final BDMMaintainAttachmentTask maintainTask =
      BDMMaintainAttachmentTaskFactory.newInstance();
    maintainTask.createFECAttachmentTask(details, attachmentlink);
    assertTrue(attachmentlink.attachmentID != CuramConst.gkZero);

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

  /***** Set up attchment details */

  private AttachmentDtls createAttachmentDtls()
    throws AppException, InformationalException {

    final AttachmentDtls attachmentDetails = new AttachmentDtls();

    attachmentDetails.attachmentName = "attachmentName";
    attachmentDetails.documentType = DOCUMENTTYPE.DIRECT_DEPOSIT;
    attachmentDetails.fileLocation = "FileLocation";
    attachmentDetails.receiptDate = Date.getCurrentDate();
    attachmentDetails.attachmentStatus = RECORDSTATUS.NORMAL;
    attachmentDetails.versionNo = 1;
    attachmentDetails.attachmentID =
      UniqueIDFactory.newInstance().getNextID();
    attachmentDetails.statusCode = RECORDSTATUS.NORMAL;

    AttachmentFactory.newInstance().insert(attachmentDetails);
    return attachmentDetails;

  }

  /**
   * Set up ConcernRoleAttachmentLinkDtls object
   */
  private ConcernRoleAttachmentLinkDtls createConcernRoleAttachmentLinkDtls(
    final long attachmentID, final long concernRoleID)
    throws AppException, InformationalException {

    final ConcernRoleAttachmentLinkDtls concernRoleAttachmentLinkDtls =
      new ConcernRoleAttachmentLinkDtls();

    concernRoleAttachmentLinkDtls.attachmentID = attachmentID;
    concernRoleAttachmentLinkDtls.attachmentLinkID =
      UniqueIDFactory.newInstance().getNextID();
    concernRoleAttachmentLinkDtls.concernRoleID = concernRoleID;
    concernRoleAttachmentLinkDtls.dateReceived = Date.getCurrentDate();
    concernRoleAttachmentLinkDtls.description = "link descritpion";
    concernRoleAttachmentLinkDtls.statusCode = RECORDSTATUS.NORMAL;
    concernRoleAttachmentLinkDtls.versionNo = 1;

    ConcernRoleAttachmentLinkFactory.newInstance()
      .insert(concernRoleAttachmentLinkDtls);

    return concernRoleAttachmentLinkDtls;

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
