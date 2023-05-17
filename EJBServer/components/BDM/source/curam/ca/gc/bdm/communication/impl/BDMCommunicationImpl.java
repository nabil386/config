package curam.ca.gc.bdm.communication.impl;

import com.google.inject.Inject;
import curam.appeal.sl.entity.fact.AppealRelationshipFactory;
import curam.appeal.sl.entity.intf.AppealRelationship;
import curam.appeal.sl.entity.struct.AppealCaseID;
import curam.appeal.sl.entity.struct.AppealRelationShipDetails;
import curam.appeal.sl.entity.struct.AppealRelationShipDetailsList;
import curam.ca.gc.bdm.codetable.BDMRFRISSUEDECISION;
import curam.ca.gc.bdm.entity.fact.BDMRFRIssueFactory;
import curam.ca.gc.bdm.entity.struct.BDMRFRIssueDtls;
import curam.ca.gc.bdm.entity.struct.BDMRFRIssueDtlsList;
import curam.ca.gc.bdm.entity.struct.BDMRFRIssueKey;
import curam.ca.gc.bdm.entity.struct.BDMRFRIssueKeyStruct1;
import curam.ca.gc.bdm.entity.struct.BDMWMInstanceDataDtls;
import curam.ca.gc.bdm.events.BDMNOTIFICATION;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMStandardManualTaskDtls;
import curam.ca.gc.bdm.facade.communication.fact.BDMCommunicationFactory;
import curam.ca.gc.bdm.facade.communication.struct.BDMFileNameAndDataDtls;
import curam.ca.gc.bdm.facade.communication.struct.BDMTaskName;
import curam.ca.gc.bdm.facade.communication.struct.BDMWorkQueueID;
import curam.ca.gc.bdm.facade.participant.fact.BDMParticipantFactory;
import curam.ca.gc.bdm.facade.participant.intf.BDMParticipant;
import curam.ca.gc.bdm.facade.participant.struct.BDMReadParticipantAddressDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMCORRESPONDENCE;
import curam.ca.gc.bdm.message.BDMCORRESPONDENCETASKDATA;
import curam.ca.gc.bdm.notification.impl.BDMNotification;
import curam.ca.gc.bdm.pdc.impl.BDMProductDetailsCalculatorBase;
import curam.ca.gc.bdm.sl.bdmworkallocation.fact.BDMWorkAllocationTaskFactory;
import curam.ca.gc.bdm.sl.bdmworkallocation.intf.BDMWorkAllocationTask;
import curam.ca.gc.bdm.sl.impl.BDMCreateCorrespondenceDPErrorHandler;
import curam.codetable.ATTACHMENTSTATUS;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.codetable.CASETYPECODE;
import curam.codetable.COMMUNICATIONFORMAT;
import curam.codetable.COMMUNICATIONSTATUS;
import curam.codetable.CORRESPONDENT;
import curam.codetable.DOCUMENTTYPE;
import curam.codetable.RECORDSTATUS;
import curam.codetable.TARGETITEMTYPE;
import curam.codetable.TASKPRIORITY;
import curam.codetable.TASKSTATUS;
import curam.codetable.impl.CMSLINKRELATEDTYPEEntry;
import curam.core.facade.infrastructure.struct.PersonAndEvidenceTypeList;
import curam.core.facade.struct.FileNameAndDataDtls;
import curam.core.facade.struct.ReadParticipantAddressListKey;
import curam.core.facade.struct.StandardManualTaskDtls;
import curam.core.fact.AttachmentFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.ConcernRoleCommunicationFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.fact.WMInstanceDataFactory;
import curam.core.impl.CuramConst;
import curam.core.intf.CaseHeader;
import curam.core.intf.ConcernRole;
import curam.core.intf.ConcernRoleCommunication;
import curam.core.intf.WMInstanceData;
import curam.core.sl.entity.struct.DocumentTemplateDtls;
import curam.core.sl.fact.CommunicationFactory;
import curam.core.sl.fact.LanguageLocaleMapFactory;
import curam.core.sl.fact.WorkAllocationTaskFactory;
import curam.core.sl.impl.Communication;
import curam.core.sl.infrastructure.cmis.impl.CMISAccessInterface;
import curam.core.sl.infrastructure.impl.ValidationManagerFactory;
import curam.core.sl.intf.LanguageLocaleMap;
import curam.core.sl.intf.WorkAllocationTask;
import curam.core.sl.struct.CommunicationIDKey;
import curam.core.sl.struct.CreateMSWordCommunicationDetails1;
import curam.core.sl.struct.CreateManualTaskKey_eo;
import curam.core.sl.struct.LanguageLocaleDetails;
import curam.core.sl.struct.LanguageLocaleMapKey;
import curam.core.sl.struct.MSWordCommunicationDetails1;
import curam.core.sl.struct.PreviewProFormaKey;
import curam.core.sl.struct.ProFormaCommDetails1;
import curam.core.sl.struct.ProFormaCommKey;
import curam.core.sl.struct.ProFormaReturnDocDetails;
import curam.core.sl.struct.ReadMSWordCommunicationKey;
import curam.core.sl.struct.TemplateAndDocumentDataKey;
import curam.core.sl.struct.WordTemplateDocumentAndDataDetails;
import curam.core.struct.AttachmentDtls;
import curam.core.struct.AttachmentKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CommAttachmentLinkDtls;
import curam.core.struct.CommAttachmentLinkDtlsList;
import curam.core.struct.ConcernRoleCommKeyOut;
import curam.core.struct.ConcernRoleCommunicationDtls;
import curam.core.struct.ConcernRoleCommunicationKey;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.WMInstanceDataDtls;
import curam.core.struct.WMInstanceDataDtlsList;
import curam.core.struct.WMInstanceDataKey;
import curam.creole.value.CodeTableItem;
import curam.participant.impl.ConcernRoleDAO;
import curam.piwrapper.caseheader.impl.CaseHeaderDAO;
import curam.piwrapper.caseheader.impl.ProductDeliveryDAO;
import curam.util.events.impl.EventService;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement.InformationalType;
import curam.util.exception.InformationalException;
import curam.util.fact.DeferredProcessingFactory;
import curam.util.internal.xml.fact.XSLTemplateFactory;
import curam.util.internal.xml.struct.XSLTemplateDtls;
import curam.util.internal.xml.struct.XSLTemplateKey;
import curam.util.internal.xml.struct.XSLTemplateVersionAndTemplateNameDetails;
import curam.util.persistence.GuiceWrapper;
import curam.util.persistence.TransactionScope;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import curam.util.type.AccessLevel;
import curam.util.type.AccessLevelType;
import curam.util.type.Blob;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.Implementable;
import curam.util.workflow.fact.BizObjAssociationFactory;
import curam.util.workflow.intf.BizObjAssociation;
import curam.util.workflow.struct.BizObjAssociationDtls;
import curam.workspaceservices.codetable.impl.IntakeProgramApplicationDenialReasonEntry;
import curam.workspaceservices.intake.struct.IntakeProgramApplicationDtls;
import java.util.Map;

/**
 * Communication Impl Class
 * Correspondence level logic on a case
 * This includes logic for MSWord, ProForma, Template documentation and
 * communication DP.
 * Also contains logic on Claim DENIED and Claim ESTABLISHED for corresponding
 * cases.
 *
 * @author gowtham.mohan
 * @author ka.chan
 */
@Implementable
@AccessLevel(AccessLevelType.EXTERNAL)
@TransactionScope
public class BDMCommunicationImpl extends Communication {

  @Inject
  private CMISAccessInterface cmisAccess;

  @Inject
  protected ConcernRoleDAO concernRoleDAO;

  @Inject
  protected BDMConcernRoleDocumentsImpl concernRoleDocuments;

  @Inject
  private BDMMSWordDocuments wordDocuments;

  @Inject
  protected BDMNotification bdmNotification;

  BDMCommunicationHelper bdmCommHelper = new BDMCommunicationHelper();

  @Inject
  protected ProductDeliveryDAO productDeliveryDAO;

  @Inject
  private CaseHeaderDAO caseHeaderDAO;

  @Inject
  public Map<Long, BDMProductDetailsCalculatorBase> pdDetailsCalculatorMap;

  public BDMCommunicationImpl() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Generate the proforma in pdf format with the current data
   *
   * @return ProFormaReturnDocDetails
   * @param PreviewProFormaKey
   */
  @Override
  public ProFormaReturnDocDetails
    previewProForma(final PreviewProFormaKey previewProFormaKey)
      throws AppException, InformationalException {

    ProFormaReturnDocDetails proFormaReturnDocDetails =
      new ProFormaReturnDocDetails();

    curam.core.fact.ConcernRoleDocumentsFactory.newInstance();
    final curam.core.struct.ConcernRoleDocumentKey concernRoleDocumentKey =
      new curam.core.struct.ConcernRoleDocumentKey();
    final curam.core.struct.ConcernRoleDocumentDetails concernRoleDocumentDetails =
      new curam.core.struct.ConcernRoleDocumentDetails();

    // ConcernRoleCommunication manipulation variables
    final curam.core.intf.ConcernRoleCommunication concernRoleCommunicationObj =
      curam.core.fact.ConcernRoleCommunicationFactory.newInstance();
    final curam.core.struct.ConcernRoleCommunicationKey concernRoleCommunicationKey =
      new curam.core.struct.ConcernRoleCommunicationKey();
    ConcernRoleCommunicationDtls concernRoleCommunicationDtls;

    // Set key to read ConcernRoleCommunication
    concernRoleCommunicationKey.communicationID =
      previewProFormaKey.communicationID;

    // Read ConcernRoleCommunication
    concernRoleCommunicationDtls =
      concernRoleCommunicationObj.read(concernRoleCommunicationKey);

    // Assign key to print document
    concernRoleDocumentKey.caseID = concernRoleCommunicationDtls.caseID;

    concernRoleDocumentKey.concernRoleID =
      concernRoleCommunicationDtls.correspondentConcernRoleID;

    concernRoleDocumentKey.documentID =
      concernRoleCommunicationDtls.proFormaID;

    // Assign details to print document
    concernRoleDocumentDetails.comments =
      concernRoleCommunicationDtls.comments;
    concernRoleDocumentDetails.documentID =
      concernRoleCommunicationDtls.proFormaID;
    concernRoleDocumentDetails.subject =
      concernRoleCommunicationDtls.subjectText;
    concernRoleDocumentDetails.versionNo =
      concernRoleCommunicationDtls.proFormaVersionNo;
    concernRoleDocumentDetails.communicationID =
      concernRoleCommunicationDtls.communicationID;

    // If local identifier is empty, assign TransactionInfo
    // else assign to type of communication key
    if (previewProFormaKey.localeIdentifier == null
      || previewProFormaKey.localeIdentifier.length() == 0) {
      concernRoleDocumentDetails.localeIdentifier =
        TransactionInfo.getProgramLocale();
    } else {
      concernRoleDocumentDetails.localeIdentifier =
        previewProFormaKey.localeIdentifier;
    }

    if (concernRoleCommunicationDtls.communicationStatus
      .equals(COMMUNICATIONSTATUS.DRAFT) == false
      && cmisAccess.isCMISEnabledFor(
        CMSLINKRELATEDTYPEEntry.PROFORMA_CONCERNROLECOMMUNICATION)
      && cmisAccess.contentExists(previewProFormaKey.communicationID,
        CMSLINKRELATEDTYPEEntry.PROFORMA_CONCERNROLECOMMUNICATION)) {
      final FileNameAndDataDtls fileDetails =
        cmisAccess.read(previewProFormaKey.communicationID,
          CMSLINKRELATEDTYPEEntry.PROFORMA_CONCERNROLECOMMUNICATION);

      proFormaReturnDocDetails.fileName = fileDetails.fileName;
      proFormaReturnDocDetails.fileDate = fileDetails.fileContent;
    } else {
      // Call ConcernRoleDocuments BPO to print the document
      proFormaReturnDocDetails = concernRoleDocuments
        .previewDocument(concernRoleDocumentKey, concernRoleDocumentDetails);
    }

    return proFormaReturnDocDetails;
  }

  /**
   * Generate the MS Word in pdf format with the current data
   *
   * @return ProFormaReturnDocDetails
   * @param PreviewProFormaKey
   */
  public FileNameAndDataDtls
    previewMSWord(final CommunicationIDKey communicationIDKey)
      throws AppException, InformationalException {

    final FileNameAndDataDtls fileNameAndDataDtls = new FileNameAndDataDtls();

    final ReadMSWordCommunicationKey readMSWordCommKey =
      new ReadMSWordCommunicationKey();
    readMSWordCommKey.communicationID = communicationIDKey.communicationID;
    final MSWordCommunicationDetails1 msWordCommDetails =
      this.readMSWordCommunication1(readMSWordCommKey);
    fileNameAndDataDtls.fileContent =
      wordDocuments.convertToPDF(msWordCommDetails);
    // ReadDocumentTemplateDetails docTemplateDtls =
    // wordDocuments.getDocumentTemplateByID(msWordCommDetails.templateID);
    final String fileName = formatFileName(msWordCommDetails.subjectText);
    fileNameAndDataDtls.fileName =
      fileName + BDMConstants.kPDFFileExtensionString;

    return fileNameAndDataDtls;

  }

  /**
   * Get word template document and data by templateAndDocumentDataKey with
   * communicationID
   *
   * @return WordTemplateDocumentAndDataDetails
   * @param TemplateAndDocumentDataKey
   */
  @Override
  public WordTemplateDocumentAndDataDetails
    getWordTemplateDocumentAndData(final TemplateAndDocumentDataKey key)
      throws AppException, InformationalException {

    // Set key to retrieve communication
    final ReadMSWordCommunicationKey readMSWordCommunicationKey =
      new ReadMSWordCommunicationKey();
    readMSWordCommunicationKey.communicationID = key.communicationID;

    // read ms Word Communication details
    final MSWordCommunicationDetails1 commDetails =
      this.readMSWordCommunication1(readMSWordCommunicationKey);
    final Blob initialWordDoc =
      wordDocuments.updateMSWordCustomProperties(commDetails);
    final AttachmentKey attachmentKey = new AttachmentKey();
    attachmentKey.attachmentID = commDetails.attachmentID;
    final AttachmentDtls attachmentDtls =
      AttachmentFactory.newInstance().read(attachmentKey);
    attachmentDtls.attachmentContents = initialWordDoc;
    AttachmentFactory.newInstance().modify(attachmentKey, attachmentDtls);
    return wordDocuments.getWordTemplateDocumentAndData(commDetails);
  }

  /**
   * Create communication from with automatic language and address read from
   * / personal record. The second half of the process will create a DP process
   * for the communication submittion
   *
   * @return concern role communication key
   * @param concern role communication details struct
   */
  public ConcernRoleCommunicationKey createAutoCommunication(
    final ConcernRoleCommunicationDtls concernRoleCommDtls)
    throws AppException, InformationalException {

    final ConcernRoleCommunicationKey concernRoleCommKey =
      new ConcernRoleCommunicationKey();

    // Check if the proforma is or MS Word template ID has been set
    if (concernRoleCommDtls.proFormaID != 0l)
      concernRoleCommDtls.communicationFormat = COMMUNICATIONFORMAT.PROFORMA;
    else if (!StringUtil
      .isNullOrEmpty(concernRoleCommDtls.documentTemplateID))
      concernRoleCommDtls.communicationFormat = COMMUNICATIONFORMAT.MSWORD;

    String languageCode = "";

    final CodeTableItem preferredLangCodeItem =
      bdmCommHelper.getPreferredWrittenLanguageCTI(
        concernRoleCommDtls.correspondentConcernRoleID);
    languageCode = preferredLangCodeItem.code();

    if (StringUtil.isNullOrEmpty(languageCode)) {
      // create WorkFlow if language code is unable to find from person
      // evidences
      final WorkAllocationTask workAllocationTaskObj =
        WorkAllocationTaskFactory.newInstance();
      final StandardManualTaskDtls stanManualTaskDtls =
        new StandardManualTaskDtls();
      stanManualTaskDtls.dtls.assignDtls.assignmentID =
        BDMConstants.kBDMCoreWorkQueueID;
      stanManualTaskDtls.dtls.assignDtls.assignType =
        TARGETITEMTYPE.WORKQUEUE;
      stanManualTaskDtls.dtls.concerningDtls.caseID =
        concernRoleCommDtls.caseID;
      stanManualTaskDtls.dtls.concerningDtls.participantRoleID =
        concernRoleCommDtls.correspondentConcernRoleID;
      stanManualTaskDtls.dtls.concerningDtls.participantType =
        concernRoleDAO.get(concernRoleCommDtls.correspondentConcernRoleID)
          .getConcernRoleType().getCode();

      final AppException e = new AppException(
        BDMCORRESPONDENCETASKDATA.ERR_COMMUNICATION_FAILED_TASK_SUBJECT);

      // Read the participant name and add to the exception message.
      // Create task for work allocation
      // e.arg(proformaDetails.proFormaID);
      e.arg(concernRoleDAO.get(concernRoleCommDtls.correspondentConcernRoleID)
        .getName());
      e.arg(concernRoleDAO.get(concernRoleCommDtls.correspondentConcernRoleID)
        .getPrimaryAlternateID());
      stanManualTaskDtls.dtls.taskDtls.subject = e.getMessage();
      stanManualTaskDtls.dtls.taskDtls.priority = TASKPRIORITY.NORMAL;
      stanManualTaskDtls.dtls.taskDtls.deadlineTime =
        DateTime.getCurrentDateTime().addTime(48, 0, 0);
      stanManualTaskDtls.dtls.taskDtls.taskDefinitionID =
        BDMConstants.kCommunicationFailedTaskDefinition;

      workAllocationTaskObj.createManualTask(stanManualTaskDtls);
      throw e;
    }

    // if format is proforma
    if (concernRoleCommDtls.communicationFormat
      .equals(COMMUNICATIONFORMAT.PROFORMA)) {
      // set the proforma communication struct
      final ProFormaCommDetails1 proformaDetails = new ProFormaCommDetails1();
      proformaDetails.proFormaInd = true;
      proformaDetails.communicationFormat =
        concernRoleCommDtls.communicationFormat;
      proformaDetails.proFormaID = concernRoleCommDtls.proFormaID;
      proformaDetails.caseID = concernRoleCommDtls.caseID;
      proformaDetails.clientParticipantRoleID =
        concernRoleCommDtls.concernRoleID;
      proformaDetails.caseParticipantRoleID = caseHeaderDAO
        .get(concernRoleCommDtls.caseID).getConcernRole().getID();
      proformaDetails.correspondentParticipantRoleID =
        concernRoleCommDtls.correspondentConcernRoleID;
      // call the create proforma with returning ID to create the communication
      final ConcernRoleCommKeyOut crcKeyOut =
        createProFormaReturningID(proformaDetails);
      concernRoleCommKey.communicationID = crcKeyOut.communicationID;
    } else if (concernRoleCommDtls.communicationFormat
      .equals(COMMUNICATIONFORMAT.MSWORD)) {
      // if format is MS Word
      // set the MS Word communication struct
      final CreateMSWordCommunicationDetails1 msWordDetails =
        new CreateMSWordCommunicationDetails1();
      msWordDetails.dtls.templateID = concernRoleCommDtls.documentTemplateID;
      msWordDetails.dtls.caseID = concernRoleCommDtls.caseID;
      msWordDetails.dtls.participantRoleID = caseHeaderDAO
        .get(concernRoleCommDtls.caseID).getConcernRole().getID();
      msWordDetails.dtls.correspondentParticipantRoleID =
        concernRoleCommDtls.correspondentConcernRoleID;
      // create the MS Word communication with returning ID
      final TemplateAndDocumentDataKey msWordOutKey =
        createMSWordCommunication1(msWordDetails);
      msWordOutKey.communicationID = concernRoleCommKey.communicationID;
    }
    return concernRoleCommKey;
  }

  /**
   * Create communication from with reading preferred language from personal
   * evidence and return the communication ID on success
   *
   * @return concern role communication key
   * @param concern role communication details struct
   */
  @Override
  public ConcernRoleCommKeyOut
    createProFormaReturningID(final ProFormaCommDetails1 proformaDetails)
      throws AppException, InformationalException {

    final PersonAndEvidenceTypeList personEvdTypeKey =
      new PersonAndEvidenceTypeList();
    personEvdTypeKey.concernRoleID =
      proformaDetails.correspondentParticipantRoleID;

    final ConcernRoleKey crKey = new ConcernRoleKey();
    crKey.concernRoleID = proformaDetails.correspondentParticipantRoleID;
    final ConcernRoleDtls crDtls =
      ConcernRoleFactory.newInstance().read(crKey);

    proformaDetails.correspondentConcernRoleID =
      proformaDetails.correspondentParticipantRoleID;

    String languageCode = "";
    try {
      final CodeTableItem preferredLangCodeItem =
        bdmCommHelper.getPreferredWrittenLanguageCTI(
          proformaDetails.correspondentParticipantRoleID);
      languageCode = preferredLangCodeItem.code();
    } catch (final Exception e1) {
      // Trace.kTopLevelLogger.info(e1.getMessage());
      // Do nothing.
    }

    final LanguageLocaleMap lanLocMap =
      LanguageLocaleMapFactory.newInstance();
    final LanguageLocaleMapKey lanLocMapKey = new LanguageLocaleMapKey();
    lanLocMapKey.key.languageCode = languageCode;
    final LanguageLocaleDetails languageLocaleDtls =
      lanLocMap.readLanguageLocaleMap(lanLocMapKey);

    final XSLTemplateKey xsltemplateKey = new XSLTemplateKey();
    xsltemplateKey.localeIdentifier = languageLocaleDtls.localeIdentifier;
    xsltemplateKey.templateID = proformaDetails.proFormaID;

    final XSLTemplateVersionAndTemplateNameDetails xsltTemplateVersionNo =
      XSLTemplateFactory.newInstance()
        .readLatestVersionAndTemplateName(xsltemplateKey);

    final XSLTemplateDtls xsltTempDtls =
      XSLTemplateFactory.newInstance().read(xsltemplateKey);

    proformaDetails.localeIdentifier = xsltTempDtls.localeIdentifier;
    proformaDetails.proFormaVersionNo = xsltTemplateVersionNo.latestVersion;
    proformaDetails.subject = xsltTempDtls.templateName;

    proformaDetails.correspondentName = crDtls.concernRoleName;
    proformaDetails.correspondentConcernRoleType = crDtls.concernRoleType;
    proformaDetails.correspondentType = CORRESPONDENT.CLIENT;

    final ConcernRoleCommKeyOut concernRoleCommKeyOut =
      createProFormaReturningIDNoAddressID(proformaDetails);
    return concernRoleCommKeyOut;
  }

  /**
   * Create Proforma communication which bypassing mandatory address ID check
   * and return the communication ID on success
   *
   * @return concern role communication key
   * @param concern role communication details struct
   */
  public ConcernRoleCommKeyOut createProFormaReturningIDNoAddressID(
    final ProFormaCommDetails1 proFormaCommDetails)
    throws AppException, InformationalException {

    // if address is invalid then add temp address id
    final long tempaddressid = 1l;
    if (proFormaCommDetails.addressID == 0l) {
      proFormaCommDetails.addressID = tempaddressid;
    }
    final ConcernRoleCommKeyOut concernRoleCommKeyOut =
      super.createProFormaReturningID(proFormaCommDetails);

    // creation of ProFroma for corresponding communication ID
    final ConcernRoleCommunication concernRoleComm =
      ConcernRoleCommunicationFactory.newInstance();
    final ConcernRoleCommunicationKey concernRoleCommKey =
      new ConcernRoleCommunicationKey();
    concernRoleCommKey.communicationID =
      concernRoleCommKeyOut.communicationID;
    final ConcernRoleCommunicationDtls concernRoleCommDtls =
      concernRoleComm.read(concernRoleCommKey);
    if (concernRoleCommDtls.addressID == tempaddressid) {
      concernRoleCommDtls.addressID = 0l;
      concernRoleComm.modify(concernRoleCommKey, concernRoleCommDtls);
    }
    return concernRoleCommKeyOut;
  }

  /**
   * Create Ms Word communication and return the communication ID on success
   *
   * @return concern role communication key
   * @param concern role communication details struct
   */
  @Override
  public TemplateAndDocumentDataKey createMSWordCommunication1(
    final CreateMSWordCommunicationDetails1 details)
    throws AppException, InformationalException {

    // Creation of MS Word Template and Doc Key with document details
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID =
      details.dtls.correspondentParticipantRoleID;
    final ConcernRoleDtls concernRoleDtls =
      ConcernRoleFactory.newInstance().read(concernRoleKey);
    details.dtls.correspondentName = concernRoleDtls.concernRoleName;
    details.dtls.correspondentConcernRoleType =
      concernRoleDtls.concernRoleType;
    details.dtls.correspondentTypeCode = CORRESPONDENT.CLIENT;
    final TemplateAndDocumentDataKey templateAndDocumentDataKey =
      createMSWordCommunication1NoAddressID(details);
    return templateAndDocumentDataKey;

  }

  /**
   * Create Ms Word communication which bypassing mandatory address ID check
   * and return the communication ID on success
   *
   * @return concern role communication key
   * @param concern role communication details struct
   */
  public TemplateAndDocumentDataKey createMSWordCommunication1NoAddressID(
    final CreateMSWordCommunicationDetails1 details)
    throws AppException, InformationalException {

    BDMCommunicationFactory.newInstance();

    // Add temp address ID id address ID is 0
    final long tempaddressid = 1l;
    if (details.dtls.addressID == 0l) {
      details.dtls.addressID = tempaddressid;
    }
    final TemplateAndDocumentDataKey templateAndDocumentDataKey =
      CommunicationFactory.newInstance().createMSWordCommunication1(details);

    // Create MS Word with Temp. Document Data Key
    final ConcernRoleCommunication concernRoleComm =
      ConcernRoleCommunicationFactory.newInstance();
    final ConcernRoleCommunicationKey concernRoleCommKey =
      new ConcernRoleCommunicationKey();
    concernRoleCommKey.communicationID =
      templateAndDocumentDataKey.communicationID;
    final ConcernRoleCommunicationDtls concernRoleCommDtls =
      concernRoleComm.read(concernRoleCommKey);
    if (concernRoleCommDtls.addressID == tempaddressid) {
      concernRoleCommDtls.addressID = 0l;
      concernRoleComm.modify(concernRoleCommKey, concernRoleCommDtls);
    }
    return templateAndDocumentDataKey;
  }

  /**
   * Create communication in DP Process mode
   * and return the communication ID on success
   *
   * @param ticketID
   * @param inst_Data_ID
   * @param flag
   */
  public void createCommunicationDP(final long ticketID,
    final long inst_data_id, final boolean flag)
    throws AppException, InformationalException {

    final WMInstanceDataKey wmInstanceDataKey = new WMInstanceDataKey();
    wmInstanceDataKey.wm_instDataID = inst_data_id;

    final WMInstanceData wmInstanceDataObj =
      WMInstanceDataFactory.newInstance();
    final WMInstanceDataDtls wmInstanceDataDtls =
      wmInstanceDataObj.read(wmInstanceDataKey);

    final BDMWMInstanceDataDtls bdmWMInstanceData =
      bdmCommHelper.readBDMWMInstanceDatabyID(inst_data_id);

    // create concernrolecommunication struct
    // create communication dm for communication ProForma
    final ConcernRoleCommunicationDtls concernRoleCommDtls =
      new ConcernRoleCommunicationDtls();
    concernRoleCommDtls.caseID = wmInstanceDataDtls.caseID;
    concernRoleCommDtls.concernRoleID =
      caseHeaderDAO.get(wmInstanceDataDtls.caseID).getConcernRole().getID();
    concernRoleCommDtls.correspondentConcernRoleID =
      wmInstanceDataDtls.concernRoleID;
    concernRoleCommDtls.proFormaID = bdmWMInstanceData.proformaID;
    concernRoleCommDtls.proFormaVersionNo =
      bdmWMInstanceData.proformaVersionNo;
    concernRoleCommDtls.communicationFormat =
      bdmWMInstanceData.communicationFormat;
    concernRoleCommDtls.proFormaInd = bdmWMInstanceData.communicationFormat
      .equals(COMMUNICATIONFORMAT.PROFORMA);
    final ConcernRoleCommunicationKey concernRoleCommKey =
      createAutoCommunication(concernRoleCommDtls);
    if (concernRoleCommKey.communicationID != 0l) {
      final BDMCommunicationHelper bdmCommHelper =
        new BDMCommunicationHelper();
      bdmCommHelper
        .createDPSubmitCommunication(concernRoleCommKey.communicationID);
    }
  }

  /**
   * Submit Proforma for overnight batch processing, this method finalized the
   * proforma by gathering the data and create the PDF file and stored it as
   * attachment
   *
   * @return TemplateAndDocumentDataKey
   * @param ProFormaCommDetails1
   */
  public TemplateAndDocumentDataKey
    submitProformaComm(final ProFormaCommDetails1 proFormaCommDtls)
      throws AppException, InformationalException {

    // Validate ProForma
    validateSubmitProforma(proFormaCommDtls);

    // Assigning address details
    final BDMParticipant participantObj = BDMParticipantFactory.newInstance();
    final ReadParticipantAddressListKey participantAddrKey =
      new ReadParticipantAddressListKey();
    participantAddrKey.maintainAddressKey.concernRoleID =
      proFormaCommDtls.correspondentParticipantRoleID;
    final BDMReadParticipantAddressDetails addressDtls =
      participantObj.readMailingAddress(participantAddrKey);
    proFormaCommDtls.addressID = addressDtls.addressID;

    // Create proforma communication
    // and return it as a TemplateAndDocumentDataKey
    bdmCommHelper.modifyProformaNoAddressID(proFormaCommDtls);
    final curam.ca.gc.bdm.facade.communication.intf.BDMCommunication bdmCommObj =
      BDMCommunicationFactory.newInstance();
    final CommunicationIDKey commIDKey = new CommunicationIDKey();
    commIDKey.communicationID = proFormaCommDtls.communicationID;
    final FileNameAndDataDtls fileNameAndDataDtls = new FileNameAndDataDtls();
    final BDMFileNameAndDataDtls bdmFileNameAndDataDtls =
      bdmCommObj.printCommunication(commIDKey);
    fileNameAndDataDtls.fileName = bdmFileNameAndDataDtls.fileName;
    fileNameAndDataDtls.fileContent = bdmFileNameAndDataDtls.fileContent;
    final TemplateAndDocumentDataKey temDocDataKey = bdmCommHelper
      .createProformaCommAttachment(commIDKey, fileNameAndDataDtls);
    return temDocDataKey;
  }

  /**
   * Validate proforma communication data for submit action
   *
   * @param commDtls
   * @throws AppException
   * @throws InformationalException
   */
  private void validateSubmitProforma(final ProFormaCommDetails1 commDtls)
    throws AppException, InformationalException {

    // validate the proformaID is Benefit Denial
    if (commDtls.proFormaID == BDMConstants.kBenefitDenialLetterID) {
      // valid the case related data
      if (commDtls.caseID != 0l && !caseHeaderDAO.get(commDtls.caseID)
        .getCaseType().toString().equals(CASETYPECODE.APPLICATION_CASE)) {
        if (determineEligibilityForPDC(commDtls.caseID)) {
          // THROW VALIDATION
          ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
            new AppException(
              BDMCORRESPONDENCE.ERR_PROFORMA_DOCUMENT_SUBMISSION),
            null, InformationalType.kError,
            curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine,
            0);
          TransactionInfo.getInformationalManager().failOperation();
        }
      }
    }
  }

  /**
   * Submit MS Word for overnight batch processing, this method finalized the
   * by gathering the data and use the MS Word to PDF library to create the PDF
   * file and stored it as
   * attachment
   *
   * @return TemplateAndDocumentDataKey
   * @param ProFormaCommDetails1
   */
  public TemplateAndDocumentDataKey
    submitMSWordComm(final MSWordCommunicationDetails1 commDTls)
      throws AppException, InformationalException {

    final curam.ca.gc.bdm.facade.communication.intf.BDMCommunication bdmCommObj =
      BDMCommunicationFactory.newInstance();
    final CommunicationIDKey commKey = new CommunicationIDKey();
    commKey.communicationID = commDTls.communicationID;
    final FileNameAndDataDtls fileNameAndDataDtls =
      this.previewMSWord(commKey);

    final TemplateAndDocumentDataKey temDocData = bdmCommHelper
      .createMSWordPdfCommAttachment(commKey, fileNameAndDataDtls);
    return temDocData;
  }

  /**
   * create DP process to submit proforma or MS word document asynchronously.
   * Failed submittion will result in create a task for the work queue
   *
   * @return TemplateAndDocumentDataKey
   * @param ProFormaCommDetails1
   */
  public void submitCommunicationDP(final long ticketID,
    final long inst_data_id, final boolean flag)
    throws AppException, InformationalException {

    final WMInstanceDataKey wmInstanceDataKey = new WMInstanceDataKey();
    wmInstanceDataKey.wm_instDataID = inst_data_id;

    final WMInstanceData wmInstanceDataObj =
      WMInstanceDataFactory.newInstance();
    final WMInstanceDataDtls wmInstanceDataDtls =
      wmInstanceDataObj.read(wmInstanceDataKey);
    final BDMWMInstanceDataDtls bdmWMInstanceData =
      bdmCommHelper.readBDMWMInstanceDatabyID(inst_data_id);
    final ConcernRoleCommunicationKey concernRoleCommunicationKey =
      new ConcernRoleCommunicationKey();

    concernRoleCommunicationKey.communicationID =
      bdmWMInstanceData.communicationID;

    final ConcernRoleCommunication concernRoleCommunication =
      ConcernRoleCommunicationFactory.newInstance();
    new TemplateAndDocumentDataKey();
    final ConcernRoleCommunicationDtls concernRoleCommunicationDtls =
      concernRoleCommunication.read(concernRoleCommunicationKey);
    final BDMCommunicationHelper bdmCommHelper = new BDMCommunicationHelper();
    // read ProForma details
    // if eligibility fails -> throw validation
    if (concernRoleCommunicationDtls.communicationFormat
      .equals(COMMUNICATIONFORMAT.PROFORMA)) {
      final ProFormaCommKey proformaKey = new ProFormaCommKey();
      proformaKey.communicationID =
        concernRoleCommunicationDtls.communicationID;
      ProFormaCommDetails1 modifyProformaDetails = new ProFormaCommDetails1();
      modifyProformaDetails = super.readProForma1(proformaKey);

      // BEGIN TASK 22414
      if (caseHeaderDAO.get(concernRoleCommunicationDtls.caseID).getCaseType()
        .toString().equals(CASETYPECODE.APPLICATION_CASE)) {
        submitProformaComm(modifyProformaDetails);
      } else {
        if (!determineEligibilityForPDC(
          concernRoleCommunicationDtls.caseID)) {
          submitProformaComm(modifyProformaDetails);
        } else {
          // THROW VALIDATION
          ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
            new AppException(
              BDMCORRESPONDENCE.ERR_PROFORMA_DOCUMENT_SUBMISSION),
            null, InformationalType.kError,
            curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine,
            0);
          TransactionInfo.getInformationalManager().failOperation();
        }
      }
      // END TASK 22414

      modifyProformaDetails.communicationStatus =
        COMMUNICATIONSTATUS.SUBMITTED;
      modifyProformaDetails.communicationDate = Date.getCurrentDate();
      bdmCommHelper.updateProFormaCommunicationStatus(modifyProformaDetails);

      // read MSWord details
      // set communication status as submitted
      // Update the status
    } else if (concernRoleCommunicationDtls.communicationFormat
      .equals(COMMUNICATIONFORMAT.MSWORD)) {

      final ReadMSWordCommunicationKey msWordKey =
        new ReadMSWordCommunicationKey();
      msWordKey.communicationID =
        concernRoleCommunicationDtls.communicationID;
      final MSWordCommunicationDetails1 modifyMSWordDetails =
        super.readMSWordCommunication1(msWordKey);
      modifyMSWordDetails.communicationStatus = COMMUNICATIONSTATUS.SUBMITTED;
      bdmCommHelper.updateMSWordCommunicationStatus(modifyMSWordDetails);
      // temDocDataKkey = submitMSWordComm(modifyMSWordDetails);
    }

  }

  // BEGIN TASK 16487 Modified to avoid Null poiter Exception
  /**
   * Modified to avoid Null poiter Exception
   *
   * @param caseID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public boolean determineEligibilityForPDC(final long caseID)
    throws AppException, InformationalException {

    // default implementation for BDM
    return false;
  }
  // END TASK 16487 Modified to avoid Null poiter Exception

  // START : TASK 4160 Entitlement Issues letter logic

  /**
   * Determine if ineligible dependents exists in current determination
   *
   * @Param case ID
   * @Throws AppException, InformationalException
   */

  public boolean determineOverallEligibilityDep(final long caseID)
    throws AppException, InformationalException {

    // default implementation for BDM
    return false;
  }

  /**
   * Create Dependent Benefit Entitlement Issues letter
   * Task-16432
   *
   * @param caseHeaderDtls
   * @throws AppException
   * @throws InformationalException
   */
  public void
    createDependentBenefitEntitlementIssuesLetter(final long caseID) {

    // default implementation for BDM
  }

  /**
   * read wmInstanceData for the benefit denial letter
   *
   * @param long caseID
   */
  public WMInstanceDataDtlsList searchDPProcessLetterByCaseIDConcernRoleID(
    final long caseID, final long concernRoleID, final long proformaID) {

    // default implementation for BDM
    return new WMInstanceDataDtlsList();
  }

  /**
   * create DP process to create the correspondence asynchronously.
   * This method will determine if the case determination and client data are
   * valid. Failed creation will result in create a task for the work queue
   *
   * @param long caseID
   */
  public void createDPBenefitDenialLetterByCaseID(final long caseID,
    final long concernRoleID) {

    // default implementation for BDM
  }

  // TASK 16339 : Generate Disentitlement letter
  /**
   * create DP process to create the correspondence fro disentitlement letter
   * asynchronously.
   * This method will determine if th client has active incarceration data
   *
   * @param long caseID
   */
  public void createDisentitlementLetterByCaseID(final long caseID,
    final long concernRoleID) {

    // default implementation for BDM
  }

  /**
   * TASK 9395: Method added
   * Method to create the correspondence asynchronously by enacting DP Process.
   * This method will determine if the case determination and client data are
   * valid. Failed creation will result in create a task for the work queue
   *
   * @param long application caseID
   */
  public void createDPBenefitDenialLetterByApplicationCaseID(
    final long applicationCaseID) {

    try {
      final CaseHeaderDtls caseHeaderDtls =
        this.readCaseHeaderDetails(applicationCaseID);
      // Create denial notification.
      this.createClaimDeniedNotification(applicationCaseID,
        caseHeaderDtls.concernRoleID);

    } catch (final Exception e) {
      Trace.kTopLevelLogger.info(e.getLocalizedMessage());
    }
  }

  /**
   * TASK 9395: Method added
   * Read case header.
   *
   * @param long caseID
   * @return CaseHeaderDtls
   */
  private CaseHeaderDtls readCaseHeaderDetails(final long caseID)
    throws AppException, InformationalException {

    final CaseHeader caseHeader = CaseHeaderFactory.newInstance();

    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = caseID;

    final CaseHeaderDtls caseHeaderDtls = caseHeader.read(caseHeaderKey);

    return caseHeaderDtls;
  }

  /**
   * TASK 9395: Method added
   * Create claim denied notification.
   *
   * @param long caseID, long concernRoleID
   * @return void
   */
  private void createClaimDeniedNotification(final long caseID,
    final long concernRoleID) throws AppException, InformationalException {

    // final WMInstanceDataDtls wmInstanceDataDtls = bdmCommHelper
    // .createWMInstanceDataForCommunication(caseID, concernRoleID);

    // bdmCommHelper.createBDMWMInstanceDataForProFormaComm(
    // wmInstanceDataDtls.wm_instDataID, BDMConstants.kBenefitDenialLetterID);

    // start the dp process to create correspondence
    // DeferredProcessingFactory.newInstance().startProcess(
    // BDMConstants.kCREATECORRESPONDENCEDP, wmInstanceDataDtls.wm_instDataID,
    // BDMCreateCorrespondenceDPErrorHandler.class.getCanonicalName());
    createDPBenefitDenialLetterByCaseID(caseID, concernRoleID);
    // BEGIN TASK 16487 Modified For Notification Message
    // create Benefit Denial notification
    // bdmNotification.createClaimDeniedNotificationMessage(wmInstanceDataDtls.concernRoleID);
    // // Already raising EVent to generate message
    // END TASK 16487 Modified For Notification Message
    final curam.util.events.struct.Event communicatinEvent =
      new curam.util.events.struct.Event();

    communicatinEvent.eventKey.eventClass =
      BDMNOTIFICATION.CREATECLAIMDENIEDNOTIFICATION.eventClass;
    communicatinEvent.eventKey.eventType =
      BDMNOTIFICATION.CREATECLAIMDENIEDNOTIFICATION.eventType;

    communicatinEvent.primaryEventData = concernRoleID;
    communicatinEvent.secondaryEventData = caseID;

    EventService.raiseEvent(communicatinEvent);
  }

  /**
   * TASK 9395: Method added
   * Create claim established notification.
   *
   * @param long concernRoleID
   * @return void
   **/
  public void createClaimEstablishedNotification(final long concernRoleID,
    final long caseID) throws AppException, InformationalException {

    // BEGIN TASK 16487 Modified For Notification Message
    bdmNotification.createClaimEstablishedNotificationMessage(concernRoleID,
      caseID);
    // // Already raising EVent to generate message
    // END TASK 16487 Modified For Notification Message
    final curam.util.events.struct.Event communicatinEvent =
      new curam.util.events.struct.Event();

    communicatinEvent.eventKey.eventClass =
      BDMNOTIFICATION.CREATECLAIMESTABLISHEDNOTIFICATION.eventClass;
    communicatinEvent.eventKey.eventType =
      BDMNOTIFICATION.CREATECLAIMESTABLISHEDNOTIFICATION.eventType;

    communicatinEvent.primaryEventData = concernRoleID;
    communicatinEvent.secondaryEventData = caseID;

    EventService.raiseEvent(communicatinEvent);
  }

  // Recall action for MSWord
  public void recallMSWord(final MSWordCommunicationDetails1 wordCommDetails)
    throws AppException, InformationalException {

    bdmCommHelper.removeAllCommAttachmentsByDocumentType(
      wordCommDetails.communicationID, DOCUMENTTYPE.LETTER);
    final DocumentTemplateDtls documentTemplate = wordDocuments
      .getDocumentTemplateByID(wordCommDetails.documentTemplateID);
    final String wordAttachmentFileName = documentTemplate.name;
    final CommAttachmentLinkDtlsList commAttLnkList = bdmCommHelper
      .getCommAttachmentLinkList(wordCommDetails.communicationID);
    for (final CommAttachmentLinkDtls commAttLnk : commAttLnkList.dtls
      .items()) {
      final AttachmentKey attKey = new AttachmentKey();
      attKey.attachmentID = commAttLnk.attachmentID;
      final AttachmentDtls attachmentDtls = attachment.read(attKey);
      if (StringUtil.isNullOrEmpty(attachmentDtls.documentType)
        && attachmentDtls.attachmentName.startsWith(wordAttachmentFileName)) {

        final curam.core.sl.struct.ReadMSWordCommunicationKey readMSWordCommKey =
          new curam.core.sl.struct.ReadMSWordCommunicationKey();
        readMSWordCommKey.communicationID = wordCommDetails.communicationID;

        final MSWordCommunicationDetails1 msWordCommDetails =
          readMSWordCommunication1(readMSWordCommKey);

        msWordCommDetails.communicationDate =
          wordCommDetails.communicationDate;
        msWordCommDetails.communicationStatus =
          wordCommDetails.communicationStatus;
        msWordCommDetails.documentTemplateID =
          wordCommDetails.documentTemplateID;
        msWordCommDetails.attachmentID = attachmentDtls.attachmentID;

        attachmentDtls.attachmentStatus = ATTACHMENTSTATUS.ACTIVE;
        attachmentDtls.attachmentContents =
          wordDocuments.updateMSWordCustomProperties(msWordCommDetails);
        attachment.modify(attKey, attachmentDtls);
        break;
      }
    }

    final ConcernRoleCommunication crcEntityObj =
      ConcernRoleCommunicationFactory.newInstance();
    final ConcernRoleCommunicationKey crcKey =
      new ConcernRoleCommunicationKey();
    crcKey.communicationID = wordCommDetails.communicationID;
    final ConcernRoleCommunicationDtls crcDtls = crcEntityObj.read(crcKey);
    crcDtls.communicationDate = Date.kZeroDate;
    crcEntityObj.modify(crcKey, crcDtls);

  }

  // START TASK 14504 : Create Recalculation letter

  /**
   * Create Dependent Benefit Recalculation letter
   * Task-
   *
   * @param caseHeaderDtls
   * @throws AppException
   * @throws InformationalException
   */
  public void createRecalculationLetter(final long caseID) {

    // default implementation for BDM
  }

  /**
   * Determine if there is change in entitlement amount or dependents supplement
   * amount from previous
   * determination
   *
   * @Param case ID
   * @Throws AppException, InformationalException
   */
  public boolean determineOverallEligibility_Recalculation(final long caseID)
    throws AppException, InformationalException {

    // default implementation of BDM
    return false;

  }

  // END TASK 14504 : Create Recalculation letter

  // TASK -24567 Code refactoring
  /**
   * @param communicationDtls
   * @param crcDtls
   * @throws AppException
   * @throws InformationalException
   */
  public void createTaskandAssignToWorkQueue(
    final ConcernRoleCommunicationDtls communicationDtls,
    final String subject, final String workQueueID, final long skillTypeID)
    throws AppException, InformationalException {

    final curam.core.struct.CommunicationIDKey commKey =
      new curam.core.struct.CommunicationIDKey();
    commKey.communicationID = communicationDtls.communicationID;

    final String taskName = subject;

    final String wqID = workQueueID;
    final BDMTaskName bdmTaskName = new BDMTaskName();
    bdmTaskName.taskName = taskName;
    final BDMWorkQueueID bdmWrkQueueID = new BDMWorkQueueID();
    bdmWrkQueueID.workQueueID = wqID;
    assignTaskToWorkQueue(commKey, bdmTaskName, bdmWrkQueueID, skillTypeID);
  }

  /**
   * Create a task when Communication Status is changed to VOID. Task has set
   * for a deadline for 2 days, with low priority.
   *
   * @param commKey CommunicationIDKey in order to get Communication ID and sub
   * information.
   * @param taskName String for task name
   * @param wqID Work queue ID
   */

  private void assignTaskToWorkQueue(
    final curam.core.struct.CommunicationIDKey commKey,
    final BDMTaskName taskName, final BDMWorkQueueID wqID,
    final long skillTypeID) throws AppException, InformationalException {

    final ConcernRoleCommunication concernRoleCommunicationObj =
      ConcernRoleCommunicationFactory.newInstance();
    final ConcernRoleCommunicationKey key = new ConcernRoleCommunicationKey();

    key.communicationID = commKey.communicationID;
    final ConcernRoleCommunicationDtls communicationDetails =
      concernRoleCommunicationObj.read(key);

    // Assign task to 'Stimulus Program Void Mail' Work Queue.
    final BDMWorkAllocationTask workAllocationTaskObj =
      BDMWorkAllocationTaskFactory.newInstance();
    final BDMStandardManualTaskDtls taskKey = new BDMStandardManualTaskDtls();
    taskKey.dtls.assignDtls.assignmentID = wqID.workQueueID;
    taskKey.dtls.assignDtls.assignType = TARGETITEMTYPE.WORKQUEUE;
    taskKey.dtls.concerningDtls.participantRoleID =
      communicationDetails.correspondentConcernRoleID;
    taskKey.dtls.concerningDtls.caseID = communicationDetails.caseID;
    taskKey.dtls.taskDtls.subject = taskName.taskName;

    final ConcernRole crObj = ConcernRoleFactory.newInstance();
    final ConcernRoleKey crKey = new ConcernRoleKey();
    crKey.concernRoleID = communicationDetails.correspondentConcernRoleID;
    final ConcernRoleDtls crDtls = crObj.read(crKey);
    taskKey.dtls.concerningDtls.participantType = crDtls.concernRoleType;

    // taskKey.dtls.taskDtls.bdmTaskClassificationID = skillTypeID;
    taskKey.dtls.taskDtls.taskDefinitionID =
      BDMConstants.kCommunicationFailedTaskDefinition;
    taskKey.dtls.taskDtls.priority = TASKPRIORITY.LOW;
    taskKey.dtls.taskDtls.deadlineTime =
      DateTime.getCurrentDateTime().addTime(48, 0, 0);
    taskKey.dtls.taskDtls.comments = taskName.taskName;
    taskKey.dtls.taskDtls.status = TASKSTATUS.NOTSTARTED;

    // Create task
    final CreateManualTaskKey_eo manualTaskKey =
      workAllocationTaskObj.createTaskWithVSG(taskKey);

    final BizObjAssociation bizObjAssociationObj =
      BizObjAssociationFactory.newInstance();
    BizObjAssociationDtls bizObjAssocDtls = new BizObjAssociationDtls();

    bizObjAssocDtls.bizObjAssocID = bizObjAssociationObj.getNewPrimaryKey();
    bizObjAssocDtls.taskID = manualTaskKey.key.dtls.dtls.taskID;
    bizObjAssocDtls.bizObjectType = BUSINESSOBJECTTYPE.TASKSKILLTYPE;
    bizObjAssocDtls.bizObjectID = skillTypeID;
    bizObjAssociationObj.insert(bizObjAssocDtls);

    bizObjAssocDtls = new BizObjAssociationDtls();
    bizObjAssocDtls.bizObjAssocID = bizObjAssociationObj.getNewPrimaryKey();
    bizObjAssocDtls.taskID = manualTaskKey.key.dtls.dtls.taskID;
    bizObjAssocDtls.bizObjectType = BUSINESSOBJECTTYPE.BDMCOMMUNICATION;
    bizObjAssocDtls.bizObjectID = commKey.communicationID;
    bizObjAssociationObj.insert(bizObjAssocDtls);

  }
  // END -TASK -24567 Code refactoring

  /**
   * VOID Communication Status process: create a task with void communication
   * subject and assign task to voidMail Work Queue
   *
   * @param concern role communication dtls
   */
  public void voidCommunication(final ConcernRoleCommunicationDtls crcDtls)
    throws AppException, InformationalException {

    final AppException e = new AppException(
      BDMCORRESPONDENCETASKDATA.VOID_COMMUNICATION_TASK_SUBJECT);
    e.arg(crcDtls.correspondentName);
    final curam.participant.impl.ConcernRole corrConcernRole =
      concernRoleDAO.get(crcDtls.correspondentConcernRoleID);
    e.arg(corrConcernRole.getPrimaryAlternateID());
    createTaskandAssignToWorkQueue(crcDtls, e.getMessage(),
      BDMConstants.kBDMCoreWorkQueueID,
      BDMConstants.kVoidCorrespondenceSkillTypeID);
  }

  /**
   * RETURN Communication Status process: create a task with undeliverable task
   * subject and assign task to undeliverable Mail Work Queue
   *
   * @param concern role communication dtls
   */
  public void returnCommunication(final ConcernRoleCommunicationDtls crcDtls)
    throws AppException, InformationalException {

    final AppException e = new AppException(
      BDMCORRESPONDENCETASKDATA.RETURNED_COMMUNICATION_TASK_SUBJECT);
    e.arg(crcDtls.correspondentName);
    final curam.participant.impl.ConcernRole corrConcernRole =
      concernRoleDAO.get(crcDtls.correspondentConcernRoleID);
    e.arg(corrConcernRole.getPrimaryAlternateID());

    createTaskandAssignToWorkQueue(crcDtls, e.getMessage(),
      BDMConstants.kBDMCoreWorkQueueID,
      BDMConstants.kReturnCorrespondenceSkillTypeID);

  }

  /**
   * MISDIRECT Communication Status process: create a task with misdirected mail
   * task
   * subject and assign task to misdirect Mail Work Queue
   *
   * @param concern role communication dtls
   */
  public void
    misdirectCommunication(final ConcernRoleCommunicationDtls crcDtls)
      throws AppException, InformationalException {

    final AppException e = new AppException(
      BDMCORRESPONDENCETASKDATA.MISDIRECTED_COMMUNICATION_TASK_SUBJECT);
    e.arg(crcDtls.correspondentName);
    final curam.participant.impl.ConcernRole corrConcernRole =
      concernRoleDAO.get(crcDtls.correspondentConcernRoleID);
    e.arg(corrConcernRole.getPrimaryAlternateID());
    createTaskandAssignToWorkQueue(crcDtls, e.getMessage(),
      BDMConstants.kBDMMisdirectWorkQueueID,
      BDMConstants.kMisdirectCorrespondenceSkillTypeID);

  }

  /**
   * TASK 8949
   * Method to resubmit MS word attachment.
   *
   * @param communicationID
   * @throws AppException
   * @throws InformationalException
   */
  public void
    resubmitMSWord(final MSWordCommunicationDetails1 wordCommDetails)
      throws AppException, InformationalException {

    // Validate
    if (!wordCommDetails.communicationStatus
      .equals(COMMUNICATIONSTATUS.SENT)) {
      final AppException appException =
        new AppException(BDMCORRESPONDENCE.ERR_CONCERN_ROLE_COMM_RV_NOT_SENT);
      throw appException;
    }
    // 1: Supersed the previously submitted MS word communication's pdf
    // attachment.
    bdmCommHelper.supersedCommAttachmentByDocumentType(
      wordCommDetails.communicationID, DOCUMENTTYPE.LETTER);

    // 2: Reopen the MS Word document attachment superseded while
    // submitting the communication.
    final DocumentTemplateDtls documentTemplate = wordDocuments
      .getDocumentTemplateByID(wordCommDetails.documentTemplateID);

    final String wordAttachmentFileName = documentTemplate.name;

    // get communication attachments.
    final CommAttachmentLinkDtlsList commAttLnkList = bdmCommHelper
      .getCommAttachmentLinkList(wordCommDetails.communicationID);

    for (final CommAttachmentLinkDtls commAttLnk : commAttLnkList.dtls
      .items()) {
      final AttachmentKey attKey = new AttachmentKey();
      attKey.attachmentID = commAttLnk.attachmentID;
      // read the attachment.
      final AttachmentDtls attachmentDtls = attachment.read(attKey);

      if (StringUtil.isNullOrEmpty(attachmentDtls.documentType)
        && attachmentDtls.attachmentName.startsWith(wordAttachmentFileName)
        && attachmentDtls.attachmentStatus
          .equals(ATTACHMENTSTATUS.SUPERSEDED)) {
        final curam.core.sl.struct.ReadMSWordCommunicationKey readMSWordCommKey =
          new curam.core.sl.struct.ReadMSWordCommunicationKey();
        readMSWordCommKey.communicationID = wordCommDetails.communicationID;

        final MSWordCommunicationDetails1 msWordCommDetails =
          readMSWordCommunication1(readMSWordCommKey);

        msWordCommDetails.communicationDate =
          wordCommDetails.communicationDate;
        msWordCommDetails.communicationStatus =
          wordCommDetails.communicationStatus;
        msWordCommDetails.documentTemplateID =
          wordCommDetails.documentTemplateID;
        msWordCommDetails.attachmentID = attachmentDtls.attachmentID;

        // Update attachment status to active.
        attachmentDtls.attachmentStatus = ATTACHMENTSTATUS.ACTIVE;
        attachmentDtls.attachmentContents =
          wordDocuments.updateMSWordCustomProperties(msWordCommDetails);
        attachment.modify(attKey, attachmentDtls);
        break;
      }
    }

    // 3: Resubmit the MS Word communication.
    // 3.1: first call submit function to generate pdf
    submitMSWordComm(wordCommDetails);

    // 3.2: second, update communication status once pdf attachment is created
    bdmCommHelper.updateMSWordCommunicationStatus(wordCommDetails);
  }

  public ConcernRoleCommunicationDtls readConcernRoleCommunicationByID(
    final long communicationID) throws AppException, InformationalException {

    final curam.core.intf.ConcernRoleCommunication concernRoleCommunicationObj =
      curam.core.fact.ConcernRoleCommunicationFactory.newInstance();
    final ConcernRoleCommunicationKey crcKey =
      new ConcernRoleCommunicationKey();
    crcKey.communicationID = communicationID;
    final ConcernRoleCommunicationDtls crcDtls =
      concernRoleCommunicationObj.read(crcKey);
    return crcDtls;
  }

  /** Replace spaces with underscore */

  private String formatFileName(String templateName) {

    templateName = templateName.replace(CuramConst.gkSpaceChar,
      CuramConst.gkUnderscoreChar);

    return templateName;

  }

  public void createCorrespondenceTrigger(final long caseID,
    final IntakeProgramApplicationDtls applicationDtls,
    final IntakeProgramApplicationDenialReasonEntry denialReason)
    throws AppException {
    // Default Implementation for BDM

  }

  public void createRFRCorrespondenceTrigger(final long appCaseID) {

    boolean notPerformed = false;
    boolean decisionletter = false;
    // AppealRelationship object and structs
    final AppealRelationship appealRelationshipObj =
      AppealRelationshipFactory.newInstance();
    final AppealCaseID appealCaseID = new AppealCaseID();
    appealCaseID.appealCaseID = appCaseID;
    try {
      final AppealRelationShipDetailsList appealRelationshipDetailsLIst =
        appealRelationshipObj.searchByAppealCase(appealCaseID);

      for (final AppealRelationShipDetails appealRelationshipDetails : appealRelationshipDetailsLIst.dtls
        .items()) {
        // Filter active records
        if (appealRelationshipDetails.recordStatus
          .equals(RECORDSTATUS.NORMAL)) {
          final BDMRFRIssueKeyStruct1 bdmRFRIssueKey =
            new BDMRFRIssueKeyStruct1();
          bdmRFRIssueKey.appealRelationshipID =
            appealRelationshipDetails.appealRelationshipID;
          final BDMRFRIssueDtlsList bdmRFRIssueDtlsList = BDMRFRIssueFactory
            .newInstance().listIssuesByAppealRelationshipID(bdmRFRIssueKey);
          for (final BDMRFRIssueDtls bdmRFRIssueDtls : bdmRFRIssueDtlsList.dtls
            .items()) {
            final BDMRFRIssueKey bdmRFRIssueKey1 = new BDMRFRIssueKey();
            bdmRFRIssueKey1.bdmRFRIssueID = bdmRFRIssueDtls.bdmRFRIssueID;
            if (bdmRFRIssueDtls.decision
              .equals(BDMRFRISSUEDECISION.NOTPERFORMED)) {
              notPerformed = true;

            } else if (bdmRFRIssueDtls.decision
              .equals(BDMRFRISSUEDECISION.APPROVED)
              || bdmRFRIssueDtls.decision.equals(BDMRFRISSUEDECISION.DENIED)
              || bdmRFRIssueDtls.decision
                .equals(BDMRFRISSUEDECISION.DECISIONMAINTAINED)) {
              decisionletter = true;

            }

          }

        } // if
      }

      // flags used to ensure only one letter is generated
      if (notPerformed)
        createRFRNotPerformLetter(appCaseID);
      if (decisionletter)
        composeRFRDecisionLetter(appCaseID);
    } catch (final Exception e) {
      Trace.kTopLevelLogger.info(e.getLocalizedMessage());
    }

  }

  private void createRFRNotPerformLetter(final long appealCaseID)
    throws AppException, InformationalException {

    final curam.piwrapper.caseheader.impl.CaseHeader caseHeader =
      caseHeaderDAO.get(appealCaseID);

    final WMInstanceDataDtls wmInstanceDataDtls =
      bdmCommHelper.createWMInstanceDataForCommunication(caseHeader.getID(),
        caseHeader.getConcernRole().getID());

    bdmCommHelper.createBDMWMInstanceDataForProFormaComm(
      wmInstanceDataDtls.wm_instDataID,
      BDMCorrespondenceConstants.kRFRNotPerformedProformaID);

    // start the dp process to create correspondence
    DeferredProcessingFactory.newInstance().startProcess(
      BDMConstants.kCREATECORRESPONDENCEDP, wmInstanceDataDtls.wm_instDataID,
      BDMCreateCorrespondenceDPErrorHandler.class.getCanonicalName());

  }

  private void composeRFRDecisionLetter(final long appealCaseID)
    throws AppException, InformationalException {

    final curam.piwrapper.caseheader.impl.CaseHeader caseHeader =
      caseHeaderDAO.get(appealCaseID);

    final WMInstanceDataDtls wmInstanceDataDtls =
      bdmCommHelper.createWMInstanceDataForCommunication(caseHeader.getID(),
        caseHeader.getConcernRole().getID());

    bdmCommHelper.createBDMWMInstanceDataForProFormaComm(
      wmInstanceDataDtls.wm_instDataID,
      BDMCorrespondenceConstants.kRFRDecisionProformaID);

    // start the dp process to create correspondence
    DeferredProcessingFactory.newInstance().startProcess(
      BDMConstants.kCREATECORRESPONDENCEDP, wmInstanceDataDtls.wm_instDataID,
      BDMCreateCorrespondenceDPErrorHandler.class.getCanonicalName());

  }

}
