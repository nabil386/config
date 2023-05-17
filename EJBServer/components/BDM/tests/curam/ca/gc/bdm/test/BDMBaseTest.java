/**
 *
 */
package curam.ca.gc.bdm.test;

import curam.ca.gc.bdm.codetable.BDMCALLBACKREQUESTSTATUS;
import curam.ca.gc.bdm.codetable.BDMESCALATIONLEVELS;
import curam.ca.gc.bdm.codetable.BDMFOREIGNAPPTYPE;
import curam.ca.gc.bdm.codetable.BDMLIAISONDELREASON;
import curam.ca.gc.bdm.codetable.BDMLIAISONDIRECTION;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.codetable.BDM_FILE_SOURCE;
import curam.ca.gc.bdm.entity.bdmcaseurgentflag.fact.BDMCaseUrgentFlagFactory;
import curam.ca.gc.bdm.entity.bdmcaseurgentflag.struct.BDMCaseUrgentFlagDtls;
import curam.ca.gc.bdm.entity.fact.BDMDECDRequestForCallbackFactory;
import curam.ca.gc.bdm.entity.fact.BDMEscalationLevelFactory;
import curam.ca.gc.bdm.entity.fact.BDMFEAttachmentLinkFactory;
import curam.ca.gc.bdm.entity.fact.BDMForeignLiaisonFactory;
import curam.ca.gc.bdm.entity.fact.BDMForeignLiaisonHistFactory;
import curam.ca.gc.bdm.entity.fact.BDMTaskFactory;
import curam.ca.gc.bdm.entity.fec.struct.BDMConcernRoleCommRMKey;
import curam.ca.gc.bdm.entity.intf.BDMDECDRequestForCallback;
import curam.ca.gc.bdm.entity.intf.BDMFEAttachmentLink;
import curam.ca.gc.bdm.entity.intf.BDMForeignLiaisonHist;
import curam.ca.gc.bdm.entity.struct.BDMDECDRequestForCallbackDtls;
import curam.ca.gc.bdm.entity.struct.BDMEscalationLevelDtls;
import curam.ca.gc.bdm.entity.struct.BDMFEAttachmentLinkDtls;
import curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey;
import curam.ca.gc.bdm.entity.struct.BDMForeignLiaisonDtls;
import curam.ca.gc.bdm.entity.struct.BDMForeignLiaisonHistDtls;
import curam.ca.gc.bdm.entity.struct.BDMForeignLiaisonKey;
import curam.ca.gc.bdm.entity.struct.BDMTaskDtls;
import curam.ca.gc.bdm.entity.struct.BDMTaskKey;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.struct.BDMCaseUrgentFlagDetails;
import curam.ca.gc.bdm.facade.bdmworkallocation.fact.BDMWorkAllocationFactory;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMStandardManualTaskDtls;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMExternalPartyRegistrationDetails;
import curam.ca.gc.bdm.facade.fec.fact.BDMForeignEngagementCaseFactory;
import curam.ca.gc.bdm.facade.fec.struct.BDMAttachmentIDs;
import curam.ca.gc.bdm.facade.fec.struct.BDMCreateModifyFA;
import curam.ca.gc.bdm.facade.fec.struct.BDMFAAttachmentDetails;
import curam.ca.gc.bdm.facade.fec.struct.BDMFECaseDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMPersonRegistrationDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.rest.bdmcallbackrequestapi.struct.BDMDECDCallbackRequest;
import curam.ca.gc.bdm.sl.communication.fact.BDMCommunicationFactory;
import curam.ca.gc.bdm.sl.externalparty.struct.BDMExternalPartyOfficeList;
import curam.ca.gc.bdm.sl.fec.fact.BDMMaintainForeignEngagementCaseFactory;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.ca.gc.bdm.test.data.impl.BDMExternalPartyTestDataDetails;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.BDMTASKTYPE;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.codetable.CASEPRIORITY;
import curam.codetable.CONCERNINGLINKTYPE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.DOCUMENTTYPE;
import curam.codetable.PROVINCETYPE;
import curam.codetable.RECORDSTATUS;
import curam.codetable.TARGETITEMTYPE;
import curam.codetable.TASKPRIORITY;
import curam.codetable.TASKSTATUS;
import curam.codetable.impl.METHODOFDELIVERYEntry;
import curam.core.facade.fact.PersonFactory;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.facade.struct.CreateIntegratedCaseResultAndMessages;
import curam.core.facade.struct.ExternalPartyRegistrationDetails;
import curam.core.facade.struct.InformationMsgDtlsList;
import curam.core.facade.struct.ParticipantRegistrationWizardResult;
import curam.core.facade.struct.PersonRegistrationDetails;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.facade.struct.ReadTaskSummaryDetailsKey;
import curam.core.facade.struct.SubscribeUserWorkQueueKey;
import curam.core.fact.AddressFactory;
import curam.core.fact.ConcernRoleAddressFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.intf.ConcernRoleAddress;
import curam.core.sl.entity.fact.ExternalPartyOfficeAddressFactory;
import curam.core.sl.entity.fact.TaskAssignmentFactory;
import curam.core.sl.entity.intf.ExternalPartyOfficeAddress;
import curam.core.sl.entity.struct.ExternalPartyKey;
import curam.core.sl.entity.struct.ExternalPartyOfficeAddressDtls;
import curam.core.sl.entity.struct.TaskAssignmentDtls;
import curam.core.sl.struct.ExternalPartyOfficeDetails;
import curam.core.sl.struct.RecordedCommDetails1;
import curam.core.sl.struct.WizardStateID;
import curam.core.struct.AddressDtls;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.ConcernRoleAddressDtls;
import curam.core.struct.ConcernRoleCommKeyOut;
import curam.core.struct.OtherAddressData;
import curam.core.struct.PersonDtls;
import curam.core.struct.UniqueIDKeySet;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Blob;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.StringList;
import curam.util.type.UniqueID;
import curam.util.workflow.fact.BizObjAssociationFactory;
import curam.util.workflow.fact.BusinessObjectAssociationAdminFactory;
import curam.util.workflow.fact.TaskFactory;
import curam.util.workflow.fact.WorkflowDeadlineFactory;
import curam.util.workflow.intf.BusinessObjectAssociationAdmin;
import curam.util.workflow.struct.BizObjAssocSearchDetails;
import curam.util.workflow.struct.BizObjAssocSearchDetailsList;
import curam.util.workflow.struct.BizObjAssociationDtls;
import curam.util.workflow.struct.TaskDtls;
import curam.util.workflow.struct.WorkflowDeadlineDtls;
import data.impl.BDMExternalPartyTestData;
import java.lang.reflect.Field;
import static org.junit.Assert.assertTrue;

/**
 * Base class to share ways to create test data.
 *
 * @author donghua.jin
 *
 */
public class BDMBaseTest extends CuramServerTestJUnit4 {

  protected BDMForeignLiaisonDtls createForeignLiaison()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    final BDMForeignApplicationKey faKey =
      createFA(icResult.createCaseResult.integratedCaseID);

    final BDMForeignLiaisonDtls flDtls = new BDMForeignLiaisonDtls();
    flDtls.foreignLiaisonID = UniqueID.nextUniqueID();
    flDtls.bessInd = false;
    flDtls.caseID = icResult.createCaseResult.integratedCaseID;
    flDtls.comments = "Test creating a foreign liaison";
    flDtls.direction = BDMLIAISONDIRECTION.INCOMING;
    flDtls.receiveDate = Date.getCurrentDate();
    flDtls.recordStatus = RECORDSTATUS.NORMAL;
    flDtls.liaisonChkStrList = "LC01\tLC05\tLC16";
    flDtls.foreignAppIDs = "" + faKey.fApplicationID;
    BDMForeignLiaisonFactory.newInstance().insert(flDtls);

    return flDtls;
  }

  protected BDMForeignLiaisonHistDtls createForeignLiaisonHistory(
    final Long foreignLiaisonID) throws AppException, InformationalException {

    final BDMForeignLiaisonKey key = new BDMForeignLiaisonKey();
    key.foreignLiaisonID = foreignLiaisonID;
    final BDMForeignLiaisonDtls liasnDtls =
      BDMForeignLiaisonFactory.newInstance().read(key);

    final BDMForeignLiaisonHist flHistObj =
      BDMForeignLiaisonHistFactory.newInstance();

    final curam.core.intf.UniqueID uniqueIDObj =
      UniqueIDFactory.newInstance();
    final UniqueIDKeySet uniqueIDKeySet = new UniqueIDKeySet();
    uniqueIDKeySet.keySetName = BDMConstants.kBDMDEFKEYSET;

    final BDMForeignLiaisonHistDtls flHistDtls =
      new BDMForeignLiaisonHistDtls();
    flHistDtls.assign(liasnDtls);
    flHistDtls.frgnLsnHstID = uniqueIDObj.getNextIDFromKeySet(uniqueIDKeySet);

    flHistObj.insert(flHistDtls);

    return flHistDtls;
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

  protected PersonRegistrationResult registerPersonWithBDMIntlAddress()
    throws AppException, InformationalException {

    // BEGIN: REGISTER PERSON
    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = "Save";

    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "Marge";
    bdmPersonRegistrationDetails.dtls.surname = "Simpson";
    bdmPersonRegistrationDetails.dtls.sex = "SX2";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19860101");
    bdmPersonRegistrationDetails.dtls.socialSecurityNumber = "505571208";
    bdmPersonRegistrationDetails.dtls.alternateIDTypeCodeOpt =
      CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER;

    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;

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

  protected BDMForeignLiaisonDtls createForeignLiaisonWithRelatedFl()
    throws AppException, InformationalException {

    // create the main liaison record

    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    final BDMForeignLiaisonDtls flDtls = new BDMForeignLiaisonDtls();
    flDtls.foreignLiaisonID = UniqueIDFactory.newInstance().getNextID();
    flDtls.bessInd = false;
    flDtls.caseID = icResult.createCaseResult.integratedCaseID;
    flDtls.comments = "Test creating a foreign liaison 2";
    flDtls.direction = BDMLIAISONDIRECTION.INCOMING;
    flDtls.receiveDate = Date.getCurrentDate();
    flDtls.recordStatus = RECORDSTATUS.NORMAL;
    BDMForeignLiaisonFactory.newInstance().insert(flDtls);

    // create a related liaison record
    final BDMForeignLiaisonDtls relatedFlDtls = new BDMForeignLiaisonDtls();
    relatedFlDtls.foreignLiaisonID =
      UniqueIDFactory.newInstance().getNextID();
    relatedFlDtls.bessInd = false;
    relatedFlDtls.caseID = icResult.createCaseResult.integratedCaseID;
    relatedFlDtls.comments = "Test creating a related foreign liaison";
    relatedFlDtls.direction = BDMLIAISONDIRECTION.OUTGOING;
    relatedFlDtls.receiveDate = Date.getCurrentDate();
    relatedFlDtls.recordStatus = RECORDSTATUS.NORMAL;
    // make this FL reference the main FL
    relatedFlDtls.relatedFrgnLisnIDs = "" + flDtls.foreignLiaisonID;
    BDMForeignLiaisonFactory.newInstance().insert(relatedFlDtls);

    // return the main FL
    return flDtls;
  }

  protected BDMForeignLiaisonDtls createForeignLiaisonWithCanceledRelatedFl()
    throws AppException, InformationalException {

    // create the main liaison record

    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    final BDMForeignLiaisonDtls flDtls = new BDMForeignLiaisonDtls();
    flDtls.foreignLiaisonID = UniqueIDFactory.newInstance().getNextID();
    flDtls.bessInd = false;
    flDtls.caseID = icResult.createCaseResult.integratedCaseID;
    flDtls.comments = "Test creating a foreign liaison 2";
    flDtls.direction = BDMLIAISONDIRECTION.INCOMING;
    flDtls.receiveDate = Date.getCurrentDate();
    flDtls.recordStatus = RECORDSTATUS.NORMAL;
    BDMForeignLiaisonFactory.newInstance().insert(flDtls);

    // create a related liaison record
    final BDMForeignLiaisonDtls relatedFlDtls = new BDMForeignLiaisonDtls();
    relatedFlDtls.foreignLiaisonID =
      UniqueIDFactory.newInstance().getNextID();
    relatedFlDtls.bessInd = false;
    relatedFlDtls.caseID = icResult.createCaseResult.integratedCaseID;
    relatedFlDtls.comments = "Test creating a related foreign liaison";
    relatedFlDtls.direction = BDMLIAISONDIRECTION.OUTGOING;
    relatedFlDtls.receiveDate = Date.getCurrentDate();
    relatedFlDtls.recordStatus = RECORDSTATUS.CANCELLED;
    relatedFlDtls.deleteReason = BDMLIAISONDELREASON.ENTEREDINERROR;
    // make this FL reference the main FL
    relatedFlDtls.relatedFrgnLisnIDs = "" + flDtls.foreignLiaisonID;
    BDMForeignLiaisonFactory.newInstance().insert(relatedFlDtls);

    // return the main FL
    return flDtls;
  }

  protected BDMAttachmentIDs createFECAttachment(final long caseID,
    final Date receiptDate) throws AppException, InformationalException {

    return createFECAttachment(caseID, receiptDate,
      DOCUMENTTYPE.FOREIGN_LIAISON);
  }

  protected BDMAttachmentIDs createFECAttachment(final long caseID,
    final Date receiptDate, final String docType)
    throws AppException, InformationalException {

    final BDMFAAttachmentDetails createFAAttachment =
      new BDMFAAttachmentDetails();

    createFAAttachment.actionIDProperty = BDMConstants.kSaveAndClose;
    createFAAttachment.bdmCreateAttachmentDetails.fileSource =
      BDM_FILE_SOURCE.CLIENT;
    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.caseID =
      caseID;
    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.receiptDate =
      receiptDate;
    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.description =
      "Test FEC attachment";
    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.newCaseAttachmentName =
      "TestFecAttachment.pdf";
    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.newCaseAttachmentContents =
      new Blob("Test".getBytes());
    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.documentType =
      docType;

    return BDMMaintainForeignEngagementCaseFactory.newInstance()
      .createFECaseAttachment(createFAAttachment);
  }

  protected BDMFEAttachmentLinkDtls
    insertFLAttLnk(final long foreignLiaisonID, final long attachmentID)
      throws AppException, InformationalException {

    final BDMFEAttachmentLink flAttLnkObj =
      BDMFEAttachmentLinkFactory.newInstance();

    final curam.core.intf.UniqueID uniqueIDObj =
      UniqueIDFactory.newInstance();
    final UniqueIDKeySet uniqueIDKeySet = new UniqueIDKeySet();
    uniqueIDKeySet.keySetName = BDMConstants.kBDMDEFKEYSET;

    final BDMFEAttachmentLinkDtls flAttLnkDtls =
      new BDMFEAttachmentLinkDtls();
    flAttLnkDtls.feAttachmentLinkID =
      uniqueIDObj.getNextIDFromKeySet(uniqueIDKeySet);
    flAttLnkDtls.relatedID = foreignLiaisonID;
    flAttLnkDtls.recordStatus = RECORDSTATUS.NORMAL;
    flAttLnkDtls.attachmentID = attachmentID;
    flAttLnkObj.insert(flAttLnkDtls);

    return flAttLnkDtls;
  }

  protected BDMForeignApplicationKey createFA(final long caseID)
    throws AppException, InformationalException {

    final BDMCreateModifyFA bdmCreateModifyFA = new BDMCreateModifyFA();
    bdmCreateModifyFA.caseID = caseID;
    bdmCreateModifyFA.comments =
      "This comments entered on the New Foreign Application screen";
    bdmCreateModifyFA.typeCode = BDMFOREIGNAPPTYPE.INVALIDITY;
    bdmCreateModifyFA.receiveDate = Date.getCurrentDate();

    return BDMForeignEngagementCaseFactory.newInstance()
      .createForeignApplication(bdmCreateModifyFA);
  }

  protected ConcernRoleCommKeyOut createRecordedCommunication(
    final long caseID, final String concernRoleName, final long concernRoleID)
    throws AppException, InformationalException {

    final RecordedCommDetails1 recordedCommDetails =
      new RecordedCommDetails1();

    recordedCommDetails.addressData =
      "1\n0\nBDMINTL\nUS\n1\n0\nZIP=\nBDMSTPROVX=\nCITY=\nCOUNTRY=\nPOBOXNO=\nAPT=\nPOSTCODE=\nBDMZIPX=\nSTATEPROV=\nPROV=\nADD1=\nADD2=\n";
    recordedCommDetails.caseID = caseID;
    recordedCommDetails.communicationDirection = "CD2";
    recordedCommDetails.communicationText = "Test communication";
    recordedCommDetails.communicationTypeCode = "CT8000";
    recordedCommDetails.correspondentName = concernRoleName;
    recordedCommDetails.correspondentParticipantRoleID = concernRoleID;
    recordedCommDetails.correspondentType = "CO2";
    recordedCommDetails.methodTypeCode = "CM1";
    recordedCommDetails.subject = "Test comm 2";

    final ConcernRoleCommKeyOut concernRoleCommKeyOut =
      BDMCommunicationFactory.newInstance()
        .createRecordedCommWithReturningID(recordedCommDetails);

    return concernRoleCommKeyOut;
  }

  protected long createEscalationLevelForCommunication(
    final long communicationID, final String escalationLevelCode)
    throws AppException, InformationalException {

    final BDMEscalationLevelDtls dtls = new BDMEscalationLevelDtls();
    dtls.bdmEscalationLevelID = UniqueIDFactory.newInstance().getNextID();
    dtls.communicationID = communicationID;
    dtls.escalationLevel = escalationLevelCode;
    BDMEscalationLevelFactory.newInstance().insert(dtls);
    return dtls.bdmEscalationLevelID;
  }

  protected long createUrgentFlagForCase(final long caseID,
    final String urgentFlagTypeCode)
    throws AppException, InformationalException {

    final BDMCaseUrgentFlagDtls dtls = new BDMCaseUrgentFlagDtls();
    dtls.bdmCaseUrgentFlagID = UniqueIDFactory.newInstance().getNextID();
    dtls.caseID = caseID;
    dtls.type = urgentFlagTypeCode;
    dtls.recordStatus = RECORDSTATUS.NORMAL;
    dtls.startDate = Date.getCurrentDate().addDays(-7);
    BDMCaseUrgentFlagFactory.newInstance().insert(dtls);

    return dtls.bdmCaseUrgentFlagID;
  }

  protected ReadTaskSummaryDetailsKey createTaskAndReserve(final long caseID,
    final String reservedByUserName)
    throws AppException, InformationalException {

    final BDMStandardManualTaskDtls dtls = new BDMStandardManualTaskDtls();
    dtls.dtls.assignDtls.assignmentID = reservedByUserName;
    dtls.dtls.assignDtls.assignType = "RL9";

    dtls.dtls.concerningDtls.caseID = caseID;
    dtls.dtls.concerningDtls.participantType = "RL1";

    dtls.dtls.taskDtls.deadlineTime =
      DateTime.getCurrentDateTime().addTime(720, 0, 0); // 30 days later
    dtls.dtls.taskDtls.priority = "TP2";
    dtls.dtls.taskDtls.subject = "task 8";
    dtls.dtls.taskDtls.status = "WS1";

    final ReadTaskSummaryDetailsKey key =
      BDMWorkAllocationFactory.newInstance().createTaskWithVSG(dtls);

    return key;
  }

  protected ReadTaskSummaryDetailsKey createDeferredTaskAndReserve(
    final long caseID, final String reservedByUserName)
    throws AppException, InformationalException {

    final BDMStandardManualTaskDtls dtls = new BDMStandardManualTaskDtls();
    dtls.dtls.assignDtls.assignmentID = reservedByUserName;
    dtls.dtls.assignDtls.assignType = "RL9";

    dtls.dtls.concerningDtls.caseID = caseID;
    dtls.dtls.concerningDtls.participantType = "RL1";

    dtls.dtls.taskDtls.deadlineTime =
      DateTime.getCurrentDateTime().addTime(720, 0, 0); // 30 days later
    dtls.dtls.taskDtls.priority = "TP2";
    dtls.dtls.taskDtls.subject = "task 7";
    dtls.dtls.taskDtls.status = "WS6";

    final ReadTaskSummaryDetailsKey key =
      BDMWorkAllocationFactory.newInstance().createTaskWithVSG(dtls);

    return key;
  }

  protected long createBizObjAssociationForTask(final String bizObjectType,
    final long bizObjectID, final long taskID)
    throws AppException, InformationalException {

    final BizObjAssociationDtls dtls = new BizObjAssociationDtls();
    dtls.bizObjAssocID = UniqueIDFactory.newInstance().getNextID();
    dtls.bizObjectID = bizObjectID;
    dtls.bizObjectType = bizObjectType;
    dtls.taskID = taskID;
    BizObjAssociationFactory.newInstance().insert(dtls);

    return dtls.bizObjAssocID;
  }

  /**
   * Get the existing task ID, if there is an open task available, raised for a
   * particular record communication type.
   *
   * @param bdmCncrCmmRmKey
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  @SuppressWarnings("unused")
  protected BizObjAssocSearchDetails
    getBizObjectCommunicationID(final BDMConcernRoleCommRMKey bdmCncrCmmRmKey)
      throws AppException, InformationalException {

    final BusinessObjectAssociationAdmin bizObjAssociationAdminObj =
      BusinessObjectAssociationAdminFactory.newInstance();

    final long bizObjectID = bdmCncrCmmRmKey.communicationID;
    final String bizObjectType = BUSINESSOBJECTTYPE.BDMCOMMUNICATION;

    final BizObjAssocSearchDetailsList bizObjAssocSearchDetailsList =
      bizObjAssociationAdminObj.searchByBizObjectTypeAndID(bizObjectID,
        bizObjectType);

    final int listSize = bizObjAssocSearchDetailsList.dtls.size();

    BizObjAssocSearchDetails bizObjAssocSearchDetails = null;

    for (int i = 0; i < listSize; i++) {

      bizObjAssocSearchDetails = new BizObjAssocSearchDetails();

      bizObjAssocSearchDetails = bizObjAssocSearchDetailsList.dtls.item(i);

      break;
    }
    return bizObjAssocSearchDetails;
  }

  protected void setDeferredTransaction()
    throws AppException, InformationalException {

    try {
      Configuration.setProperty(
        "curam.test.stubdeferredprocessinsametransaction", "true");

      // TransactionInfo.getInfo().commit();

      // TransactionInfo.getInfo().closeConnection();

      TransactionInfo.enactStubbedDeferredProcessCalls();
      final Field field =
        TransactionInfo.getInfo().getClass().getDeclaredField("userName");

      field.setAccessible(true);
      field.set(TransactionInfo.getInfo(), "caseworker");
    } catch (final Exception e) {
      // do nothing
    }
  }

  protected void setDeferredProcessingInNewTransaction()
    throws AppException, InformationalException {

    try {
      Configuration.setProperty(
        "curam.test.stubdeferredprocessinsametransaction", "true");

      TransactionInfo.getInfo().commit();

      TransactionInfo.getInfo().closeConnection();

      TransactionInfo.enactStubbedDeferredProcessCalls();

      final Field field =
        TransactionInfo.getInfo().getClass().getDeclaredField("userName");

      field.setAccessible(true);
      field.set(TransactionInfo.getInfo(), "caseworker");
    } catch (final Exception e) {
      // do nothing
    }
  }

  protected long getATaskID() throws AppException, InformationalException {

    final PersonRegistrationResult personRegistrationResult =
      registerPerson();

    final CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      createFEC(personRegistrationResult.registrationIDDetails.concernRoleID);

    final ReadTaskSummaryDetailsKey readTaskSummaryDetailsKey =
      createTaskAndAssignToWorkQueue(
        createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID,
        "aroo");

    // create call back record
    final BDMDECDCallbackRequest callbackrequest =
      new BDMDECDCallbackRequest();
    callbackrequest.on_Behalf_Of = "On Behalf of: unauthenticated";
    callbackrequest.accessibilitydetails = "accessibility details";
    callbackrequest.benefittype =
      "International call back request benefit type";
    callbackrequest.discussiontopic = "Topic of discussion";
    callbackrequest.email = "test.email@gmail.com";
    callbackrequest.firstname = "TestFirstName";
    callbackrequest.freetext = "some free text entered";
    callbackrequest.language = "EN";
    callbackrequest.lastname = "TestLastName";
    callbackrequest.preferredcontacttime = "4 PM";
    callbackrequest.sin = "456987321";
    callbackrequest.telephonenumber = "16579876545";

    final BDMDECDRequestForCallback requestCallbackObj =
      BDMDECDRequestForCallbackFactory.newInstance();

    final BDMDECDRequestForCallbackDtls requestForCallbackDtls =
      new BDMDECDRequestForCallbackDtls();
    requestForCallbackDtls.assign(callbackrequest);
    requestForCallbackDtls.guid = callbackrequest.on_Behalf_Of;
    requestForCallbackDtls.firstName = callbackrequest.firstname;
    requestForCallbackDtls.lastName = callbackrequest.lastname;
    requestForCallbackDtls.status = BDMCALLBACKREQUESTSTATUS.PENDING;
    requestForCallbackDtls.createdOn = DateTime.getCurrentDateTime();
    requestForCallbackDtls.callbackID = UniqueID.nextUniqueID();
    requestForCallbackDtls.taskID =
      readTaskSummaryDetailsKey.dtls.dtls.taskID;
    requestForCallbackDtls.telephoneNumber = callbackrequest.telephonenumber;
    requestForCallbackDtls.preferredContactTime =
      callbackrequest.preferredcontacttime;
    requestForCallbackDtls.accessibilityDetails =
      callbackrequest.accessibilitydetails;
    requestForCallbackDtls.benefitType = callbackrequest.benefittype;
    requestForCallbackDtls.freeText = callbackrequest.freetext;
    requestForCallbackDtls.discussionTopic = callbackrequest.discussiontopic;
    requestCallbackObj.insert(requestForCallbackDtls);

    return readTaskSummaryDetailsKey.dtls.dtls.taskID;

  }

  protected ReadTaskSummaryDetailsKey createTaskAndAssignToWorkQueue(
    final long caseID, final String reservedByUserName)
    throws AppException, InformationalException {

    Configuration.setProperty(EnvVars.ENV_ENACT_WORKFLOW_PROCESS_DISABLED,
      BDMTestConstants.kNo);

    final BDMStandardManualTaskDtls dtls = new BDMStandardManualTaskDtls();
    // dtls.dtls.assignDtls.assignmentID = reservedByUserName;
    dtls.dtls.assignDtls.reserveToMeInd = BDMTestConstants.kTrue;
    dtls.dtls.assignDtls.assignType = TARGETITEMTYPE.USER;

    dtls.dtls.concerningDtls.caseID = caseID;
    dtls.dtls.concerningDtls.participantType = CONCERNINGLINKTYPE.PERSON;

    dtls.dtls.taskDtls.deadlineTime = DateTime.getCurrentDateTime().addTime(
      BDMTestConstants.kOneDayHours * 30, BDMTestConstants.kMinutes * 1,
      BDMTestConstants.kSeconds * 1);

    dtls.dtls.taskDtls.priority = TASKPRIORITY.NORMAL;
    dtls.dtls.taskDtls.subject = "This is TASK creation subject";
    dtls.dtls.taskDtls.status = TASKSTATUS.NOTSTARTED;
    dtls.dtls.taskDtls.taskType = BDMTASKTYPE.ADDRESSCHANGE_DIRECTDEPOSIT_PWS;
    dtls.dtls.taskDtls.bdmTaskClassificationID =
      BDMTestConstants.kBDMTaskClassificationID;

    final ReadTaskSummaryDetailsKey key =
      BDMWorkAllocationFactory.newInstance().createTaskWithVSG(dtls);

    return key;
  }

  protected BDMTaskDtls getBDMTaskDetails(final long taskID)
    throws AppException, InformationalException {

    final BDMTaskKey bdmTaskKey = new BDMTaskKey();
    bdmTaskKey.taskID = taskID;
    final BDMTaskDtls bdmTaskDtls =
      BDMTaskFactory.newInstance().read(bdmTaskKey);
    return bdmTaskDtls;
  }

  /**
   * This is a utility method to subscribe to WORKQUEUEs for the logged in user.
   *
   * @author hamed.mohammed
   * @throws AppException
   * @throws InformationalException
   */
  protected void subscribeLoggedInUserToWorkqueues()
    throws AppException, InformationalException {

    final curam.core.facade.intf.Inbox inboxObj =
      curam.core.facade.fact.InboxFactory.newInstance();

    // Parse tab delimited list of selected work queue id into list.
    final StringList workQueueIDNameList = StringUtil
      .tabText2StringListWithTrim(BDMTestConstants.kListOfWorkQueueIds);

    final int size = workQueueIDNameList.size();

    for (int i = 0; i < size; i++) {
      final String id = workQueueIDNameList.get(i);
      final SubscribeUserWorkQueueKey key = new SubscribeUserWorkQueueKey();

      key.subscribeUserWorkQueue.key.workQueueID = Long.valueOf(id);
      inboxObj.subscribeUserWorkQueue(key);

    }
  }

  /** Create recorded Communication with escalation level */
  protected ConcernRoleCommKeyOut createRecordedCommunicationwithEscalation(
    final long caseID, final String concernRoleName, final long concernRoleID)
    throws AppException, InformationalException {

    final RecordedCommDetails1 recordedCommDetails =
      new RecordedCommDetails1();

    recordedCommDetails.addressData =
      "1\n0\nBDMINTL\nUS\n1\n0\nZIP=\nBDMSTPROVX=\nCITY=\nCOUNTRY=\nPOBOXNO=\nAPT=\nPOSTCODE=\nBDMZIPX=\nSTATEPROV=\nPROV=\nADD1=\nADD2=\n";
    recordedCommDetails.caseID = caseID;
    recordedCommDetails.communicationDirection = "CD2";
    recordedCommDetails.communicationText = "Test communication";
    recordedCommDetails.communicationTypeCode = "CT8007"; // requires attendtion
    recordedCommDetails.correspondentName = concernRoleName;
    recordedCommDetails.correspondentParticipantRoleID = concernRoleID;
    recordedCommDetails.correspondentType = "CO2";
    recordedCommDetails.methodTypeCode = "CM1";
    recordedCommDetails.subject = "Test comm 2";
    recordedCommDetails.communicationFormat = "CF4"; // recorded Communication

    return BDMCommunicationFactory.newInstance()
      .createRecordedCommWithReturningID(recordedCommDetails);
  }

  /**
   * Setup urgent flag data
   *
   * @param caseID
   * @param urgentFlagType
   * @param startDate
   * @throws AppException
   * @throws InformationalException
   */
  protected void setUpUrgentFlagData(final long caseID,
    final String urgentFlagType, final Date startDate)
    throws AppException, InformationalException {

    final BDMCaseUrgentFlagDetails flagDtls = new BDMCaseUrgentFlagDetails();
    flagDtls.caseID = caseID;
    TransactionInfo.getInfo();
    flagDtls.createdBy = TransactionInfo.getProgramUser();
    TransactionInfo.getInfo();
    flagDtls.createdByFullName = TransactionInfo.getProgramUser();
    flagDtls.recordStatus = RECORDSTATUS.NORMAL;
    flagDtls.startDate = startDate;
    flagDtls.type = urgentFlagType;
    curam.ca.gc.bdm.facade.bdmcaseurgentflag.fact.BDMCaseUrgentFlagFactory
      .newInstance().createCaseUrgentFlag(flagDtls);

  }

  /**
   * Creates test data for task with deadline date set
   *
   * @throws AppException
   * @throws InformationalException
   */
  protected void createDeferredTaskDataDeadline(final String username,
    final long communicationID, final long caseID, final int hours,
    final int minutes, final int seconds)
    throws AppException, InformationalException {

    final TaskDtls taskCreateDetail = new TaskDtls();

    taskCreateDetail.taskID = curam.util.type.UniqueID.nextUniqueID();
    taskCreateDetail.creationTime = DateTime.getCurrentDateTime();
    taskCreateDetail.status = TASKSTATUS.DEFERRED;
    taskCreateDetail.wdoSnapshot = "";
    taskCreateDetail.priority = TASKPRIORITY.NORMAL;
    taskCreateDetail.versionNo = 1;
    taskCreateDetail.reservedBy = username;
    TaskFactory.newInstance().insert(taskCreateDetail);

    // Create task assignment to user
    final TaskAssignmentDtls taskAssignmentDtls = new TaskAssignmentDtls();
    taskAssignmentDtls.taskID = taskCreateDetail.taskID;
    taskAssignmentDtls.relatedName = username;
    taskAssignmentDtls.assigneeType = TARGETITEMTYPE.USER;

    TaskAssignmentFactory.newInstance().insert(taskAssignmentDtls);

    // Create task deadline
    final WorkflowDeadlineDtls workflowDeadlineDtls =
      new WorkflowDeadlineDtls();
    workflowDeadlineDtls.deadlineID = curam.util.type.UniqueID.nextUniqueID();
    workflowDeadlineDtls.taskID = taskCreateDetail.taskID;
    workflowDeadlineDtls.suspended = false;
    workflowDeadlineDtls.deadlineTime =
      DateTime.getCurrentDateTime().addTime(hours, minutes, seconds);
    workflowDeadlineDtls.deadlineType = "WDT1";
    workflowDeadlineDtls.versionNo = 1;

    WorkflowDeadlineFactory.newInstance().insert(workflowDeadlineDtls);

    // Create Biz Object association
    BizObjAssociationDtls bizObjAssociationDtls = new BizObjAssociationDtls();
    bizObjAssociationDtls.bizObjAssocID =
      curam.util.type.UniqueID.nextUniqueID();
    bizObjAssociationDtls.taskID = taskCreateDetail.taskID;
    bizObjAssociationDtls.bizObjectID = communicationID;
    bizObjAssociationDtls.bizObjectType = BUSINESSOBJECTTYPE.BDMCOMMUNICATION;

    BizObjAssociationFactory.newInstance().insert(bizObjAssociationDtls);

    bizObjAssociationDtls = new BizObjAssociationDtls();
    bizObjAssociationDtls.bizObjAssocID = taskCreateDetail.taskID;
    bizObjAssociationDtls.taskID = taskCreateDetail.taskID;
    bizObjAssociationDtls.bizObjectID = caseID;
    bizObjAssociationDtls.bizObjectType = BUSINESSOBJECTTYPE.CASE;

    BizObjAssociationFactory.newInstance().insert(bizObjAssociationDtls);

  }

  /**
   * Create BDMTAskEscalationLevel Object
   */
  protected void createTaskEscalationLevel(final long commID)
    throws AppException, InformationalException {

    final BDMEscalationLevelDtls escalationLevelDtls =
      new BDMEscalationLevelDtls();
    escalationLevelDtls.bdmEscalationLevelID =
      curam.util.type.UniqueID.nextUniqueID();
    escalationLevelDtls.communicationID = commID;
    escalationLevelDtls.escalationLevel =
      BDMESCALATIONLEVELS.ESCALATION_LEVEL_1;
    escalationLevelDtls.versionNo = 1;
    BDMEscalationLevelFactory.newInstance().insert(escalationLevelDtls);

  }

  /**
   * Register person
   *
   *
   * @throws AppException
   * @throws InformationalException
   */
  protected long registerPerson(final String name)
    throws AppException, InformationalException {

    final RegisterPerson registerPersonObj = new RegisterPerson("");
    final PersonDtls personDtls =
      registerPersonObj.registerDefault(name, METHODOFDELIVERYEntry.CHEQUE);
    return personDtls.concernRoleID;

  }

  protected ExternalPartyOfficeAddressDtls createExternalOfficeAddress(
    final long officeID) throws AppException, InformationalException {

    final AddressDtls addrDtls = new AddressDtls();
    addrDtls.addressID = UniqueID.nextUniqueID();
    addrDtls.addressData =
      "1\n" + "0\n" + "BDMINTL\n" + "IE\n" + "0\n" + "0\n" + "COUNTRY=IE\n"
        + "POSTCODE=\n" + "ZIP=\n" + "BDMZIPX=\n" + "APT=\n" + "POBOXNO=\n"
        + "ADD1=1234\n" + "ADD2=Bay St\n" + "CITY=Toronto\n" + "PROV=\n"
        + "STATEPROV=\n" + "BDMSTPROVX=\n" + "BDMUNPARSE=";
    addrDtls.countryCode = "IE";
    addrDtls.modifiableInd = true;
    addrDtls.addressLayoutType = "BDMINTL";
    AddressFactory.newInstance().insert(addrDtls);

    final ExternalPartyOfficeAddress externalPartyOfficeAddress =
      ExternalPartyOfficeAddressFactory.newInstance();

    final ExternalPartyOfficeAddressDtls dtls =
      new ExternalPartyOfficeAddressDtls();
    dtls.addressID = addrDtls.addressID;
    dtls.comments = "Test";
    dtls.externalPartyOfficeID = officeID;
    dtls.officeAddressID = UniqueID.nextUniqueID();
    dtls.recordStatus = RECORDSTATUS.NORMAL;
    dtls.type = CONCERNROLEADDRESSTYPE.BUSINESS;
    dtls.startDate = Date.getCurrentDate();
    externalPartyOfficeAddress.insert(dtls);

    return dtls;
  }

  protected ConcernRoleAddressDtls createExternalPartyAddress(
    final long concernRoleID) throws AppException, InformationalException {

    final AddressDtls addrDtls = new AddressDtls();
    addrDtls.addressID = UniqueID.nextUniqueID();
    addrDtls.addressData =
      "1\n" + "0\n" + "BDMINTL\n" + "IE\n" + "0\n" + "0\n" + "COUNTRY=IE\n"
        + "POSTCODE=\n" + "ZIP=\n" + "BDMZIPX=\n" + "APT=\n" + "POBOXNO=\n"
        + "ADD1=1234\n" + "ADD2=Bay St\n" + "CITY=Toronto\n" + "PROV=\n"
        + "STATEPROV=\n" + "BDMSTPROVX=\n" + "BDMUNPARSE=";
    addrDtls.countryCode = "IE";
    addrDtls.modifiableInd = true;
    addrDtls.addressLayoutType = "BDMINTL";
    AddressFactory.newInstance().insert(addrDtls);

    final ConcernRoleAddress crAddr = ConcernRoleAddressFactory.newInstance();

    final ConcernRoleAddressDtls dtls = new ConcernRoleAddressDtls();
    dtls.addressID = addrDtls.addressID;
    dtls.comments = "Test";
    dtls.concernRoleAddressID = UniqueID.nextUniqueID();
    dtls.concernRoleID = concernRoleID;
    dtls.statusCode = RECORDSTATUS.NORMAL;
    dtls.startDate = Date.getCurrentDate();
    dtls.typeCode = CONCERNROLEADDRESSTYPE.BUSINESS;
    crAddr.insert(dtls);

    return dtls;
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
      "1\n" + "1\n" + "BDMINTL\n" + "AO\n" + "1\n" + "1\n" + "POBOXNO=\n"
        + "APT=\n" + "ADD1=1153\n" + "ADD2=Mole Street\n" + "CITY=Midway\n"
        + "PROV=\n" + "STATEPROV=\n" + "BDMSTPROVX=UT\n" + "COUNTRY=AO\n"
        + "POSTCODE=\n" + "ZIP=\n" + "BDMZIPX=60656\n" + "BDMUNPARSE=";
    return curam.ca.gc.bdm.facade.externalparty.fact.BDMExternalPartyFactory
      .newInstance().createExternalPartyOffice(officeDetails);
  }

  protected BDMExternalPartyOfficeList listExternalPartyOffice()
    throws AppException, InformationalException {

    final ParticipantRegistrationWizardResult ssaCountry = createSSACountry();
    createExternalPartyOffice(ssaCountry.registrationResult.concernRoleID);
    final ExternalPartyKey externalPartyKey = new ExternalPartyKey();
    externalPartyKey.concernRoleID =
      ssaCountry.registrationResult.concernRoleID;
    return curam.ca.gc.bdm.facade.externalparty.fact.BDMExternalPartyFactory
      .newInstance().listExternalPartyOffice(externalPartyKey);
  }

  protected ParticipantRegistrationWizardResult createSSACountry()
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
      curam.ca.gc.bdm.facade.externalparty.fact.BDMExternalPartyFactory
        .newInstance().setRegisterExternalPartyDetails(registrationDtls,
          wizardStateID, actionIDProperty, bdmextPartyRegDetails);
    return wizardResult;

  }

  /**
   * Creates test data for task with deadline date set
   *
   * @throws AppException
   * @throws InformationalException
   */
  protected void createTaskData(final String username,
    final long communicationID, final long caseID)
    throws AppException, InformationalException {

    // create task object
    final TaskDtls taskCreateDetail = new TaskDtls();

    taskCreateDetail.taskID = UniqueID.nextUniqueID();
    taskCreateDetail.creationTime = DateTime.getCurrentDateTime();
    taskCreateDetail.status = TASKSTATUS.NOTSTARTED;
    taskCreateDetail.wdoSnapshot = "";
    taskCreateDetail.priority = TASKPRIORITY.NORMAL;
    taskCreateDetail.versionNo = 1;
    taskCreateDetail.reservedBy = username;
    TaskFactory.newInstance().insert(taskCreateDetail);

    // create task assignment
    final TaskAssignmentDtls taskAssignmentDtls = new TaskAssignmentDtls();
    taskAssignmentDtls.taskID = taskCreateDetail.taskID;
    taskAssignmentDtls.relatedName = username;
    taskAssignmentDtls.assigneeType = TARGETITEMTYPE.USER;

    TaskAssignmentFactory.newInstance().insert(taskAssignmentDtls);

    // Create task deadline
    final WorkflowDeadlineDtls workflowDeadlineDtls =
      new WorkflowDeadlineDtls();
    workflowDeadlineDtls.deadlineID = UniqueID.nextUniqueID();
    workflowDeadlineDtls.taskID = taskCreateDetail.taskID;
    workflowDeadlineDtls.suspended = false;
    workflowDeadlineDtls.deadlineTime =
      DateTime.getCurrentDateTime().addTime(72, 0, 0);
    workflowDeadlineDtls.deadlineType = "WDT1";
    workflowDeadlineDtls.versionNo = 1;

    WorkflowDeadlineFactory.newInstance().insert(workflowDeadlineDtls);

    // Create Biz Obje assocuation
    BizObjAssociationDtls bizObjAssociationDtls = new BizObjAssociationDtls();
    bizObjAssociationDtls.bizObjAssocID = UniqueID.nextUniqueID();
    bizObjAssociationDtls.taskID = taskCreateDetail.taskID;
    bizObjAssociationDtls.bizObjectID = communicationID;
    bizObjAssociationDtls.bizObjectType = BUSINESSOBJECTTYPE.BDMCOMMUNICATION;

    BizObjAssociationFactory.newInstance().insert(bizObjAssociationDtls);

    bizObjAssociationDtls = new BizObjAssociationDtls();
    bizObjAssociationDtls.bizObjAssocID = taskCreateDetail.taskID;
    bizObjAssociationDtls.taskID = taskCreateDetail.taskID;
    bizObjAssociationDtls.bizObjectID = caseID;
    bizObjAssociationDtls.bizObjectType = BUSINESSOBJECTTYPE.CASE;

    BizObjAssociationFactory.newInstance().insert(bizObjAssociationDtls);

  }

  /**
   * Creates test data for task with deadline date set
   *
   * @throws AppException
   * @throws InformationalException
   */
  protected ReadTaskSummaryDetailsKey
    createTaskForConcernRole(final String username, final long concernRoleID)
      throws AppException, InformationalException {

    final BDMStandardManualTaskDtls dtls = new BDMStandardManualTaskDtls();
    dtls.dtls.assignDtls.assignmentID = username;
    dtls.dtls.assignDtls.assignType = "RL9";

    dtls.dtls.concerningDtls.participantRoleID = concernRoleID;
    dtls.dtls.concerningDtls.participantType = "RL1";

    dtls.dtls.taskDtls.deadlineTime =
      DateTime.getCurrentDateTime().addTime(720, 0, 0); // 30 days later
    dtls.dtls.taskDtls.priority = "TP2";
    dtls.dtls.taskDtls.subject = "task 8";
    dtls.dtls.taskDtls.status = "WS1";

    final String existingValue =
      Configuration.getProperty(EnvVars.ENV_ENACT_WORKFLOW_PROCESS_DISABLED);
    Configuration.setProperty(EnvVars.ENV_ENACT_WORKFLOW_PROCESS_DISABLED,
      BDMTestConstants.kNo);

    final ReadTaskSummaryDetailsKey key =
      BDMWorkAllocationFactory.newInstance().createTaskWithVSG(dtls);

    Configuration.setProperty(EnvVars.ENV_ENACT_WORKFLOW_PROCESS_DISABLED,
      existingValue);

    return key;
  }

}
