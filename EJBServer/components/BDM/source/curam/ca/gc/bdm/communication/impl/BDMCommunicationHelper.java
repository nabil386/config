package curam.ca.gc.bdm.communication.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import curam.attachment.impl.Attachment;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.entity.communication.fact.BDMConcernRoleCommunicationFactory;
import curam.ca.gc.bdm.entity.communication.impl.BDMConcernRoleCommunication;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationDtls;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationKey;
import curam.ca.gc.bdm.entity.fact.BDMFEWorkQueueCountryLinkFactory;
import curam.ca.gc.bdm.entity.fact.BDMWMInstanceDataFactory;
import curam.ca.gc.bdm.entity.fec.fact.BDMFECaseFactory;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseDtls;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseKey;
import curam.ca.gc.bdm.entity.intf.BDMWMInstanceData;
import curam.ca.gc.bdm.entity.struct.BDMFEWorkQueueCountryLinkDtls;
import curam.ca.gc.bdm.entity.struct.BDMFEWorkQueueCountryLinkKey;
import curam.ca.gc.bdm.entity.struct.BDMWMInstanceDataDtls;
import curam.ca.gc.bdm.entity.struct.BDMWMInstanceDataKey;
import curam.ca.gc.bdm.events.BDMCOMMUNICATIONSTATUSTASK;
import curam.ca.gc.bdm.facade.address.fact.BDMAddressFactory;
import curam.ca.gc.bdm.facade.participant.struct.BDMCommunicationAndListRowActionDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMBPOCCT;
import curam.ca.gc.bdm.sl.communication.fact.BDMCommunicationDPFactory;
import curam.ca.gc.bdm.sl.impl.BDMSubmitCorrespondenceDPErrorHandler;
import curam.citizenworkspace.entity.fact.CWExternalPartyLinkFactory;
import curam.citizenworkspace.entity.struct.CWExternalPartyLinkDtls;
import curam.citizenworkspace.entity.struct.ExternalPartySystemRecordStatusKey;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccountManager;
import curam.codetable.ADDRESSCOUNTRY;
import curam.codetable.ALTERNATENAMETYPE;
import curam.codetable.ATTACHMENTSTATUS;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.COMMUNICATIONFORMAT;
import curam.codetable.COMMUNICATIONMETHOD;
import curam.codetable.COMMUNICATIONSTATUS;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.CONCERNROLETYPE;
import curam.codetable.COUNTRY;
import curam.codetable.DOCUMENTTYPE;
import curam.codetable.EXTERNALSYSREFERENCETYPE;
import curam.codetable.LANGUAGE;
import curam.codetable.LOCALE;
import curam.codetable.RECORDSTATUS;
import curam.codetable.TARGETITEMTYPE;
import curam.codetable.TASKPRIORITY;
import curam.codetable.TASKSTATUS;
import curam.core.facade.infrastructure.assessment.fact.CaseDeterminationFactory;
import curam.core.facade.infrastructure.assessment.intf.CaseDetermination;
import curam.core.facade.infrastructure.assessment.struct.CaseDeterminationDecisionDetailsList;
import curam.core.facade.infrastructure.assessment.struct.CaseDeterminationDecisionListDetails;
import curam.core.facade.infrastructure.assessment.struct.CaseIDDeterminationIDKey;
import curam.core.facade.infrastructure.struct.ActiveEvdInstanceDtls;
import curam.core.facade.infrastructure.struct.ActiveEvdInstanceDtlsList;
import curam.core.facade.infrastructure.struct.EvidenceTypeDetails;
import curam.core.facade.infrastructure.struct.EvidenceTypeLimit;
import curam.core.facade.infrastructure.struct.EvidenceTypeList;
import curam.core.facade.infrastructure.struct.PersonAndEvidenceTypeList;
import curam.core.facade.struct.FileNameAndDataDtls;
import curam.core.fact.AlternateNameFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.CommAttachmentLinkFactory;
import curam.core.fact.ConcernRoleAddressFactory;
import curam.core.fact.ConcernRoleCommunicationFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.fact.PersonFactory;
import curam.core.fact.ProductDeliveryFactory;
import curam.core.fact.WMInstanceDataFactory;
import curam.core.impl.EnvVars;
import curam.core.intf.CaseHeader;
import curam.core.intf.CommAttachmentLink;
import curam.core.intf.ConcernRole;
import curam.core.intf.ConcernRoleCommunication;
import curam.core.intf.Person;
import curam.core.intf.ProductDelivery;
import curam.core.intf.WMInstanceData;
import curam.core.sl.fact.CommunicationFactory;
import curam.core.sl.fact.LanguageLocaleMapFactory;
import curam.core.sl.fact.TaskManagementUtilityFactory;
import curam.core.sl.infrastructure.assessment.codetable.CASEDETERMINATIONINTERVALRESULT;
import curam.core.sl.infrastructure.cmis.impl.CMSMetadataConst;
import curam.core.sl.infrastructure.cmis.impl.CMSMetadataInterface;
import curam.core.sl.infrastructure.entity.struct.EvidenceTypeKey;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.impl.ExtensionConst;
import curam.core.sl.infrastructure.impl.ValidationManagerConst;
import curam.core.sl.infrastructure.impl.ValidationManagerFactory;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.intf.Communication;
import curam.core.sl.intf.LanguageLocaleMap;
import curam.core.sl.intf.TaskManagementUtility;
import curam.core.sl.struct.DateTimeInSecondsKey;
import curam.core.sl.struct.DeadlineDuration;
import curam.core.sl.struct.LanguageLocaleMapDetailsList;
import curam.core.sl.struct.LanguageLocaleMapKey;
import curam.core.sl.struct.MSWordCommunicationDetails1;
import curam.core.sl.struct.ProFormaCommDetails1;
import curam.core.sl.struct.ProFormaCommKey;
import curam.core.sl.struct.ReadMSWordCommunicationKey;
import curam.core.sl.struct.TaskCreateDetails;
import curam.core.sl.struct.TemplateAndDocumentDataKey;
import curam.core.struct.ActiveCasesConcernRoleIDAndTypeKey;
import curam.core.struct.AddressKey;
import curam.core.struct.AddressReadMultiDtls;
import curam.core.struct.AddressReadMultiDtlsList;
import curam.core.struct.AlternateNameReadMultiStatusStruct;
import curam.core.struct.AttachmentDtls;
import curam.core.struct.AttachmentKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderDtlsList;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseKey;
import curam.core.struct.CommAttachmentLinkDtls;
import curam.core.struct.CommAttachmentLinkDtlsList;
import curam.core.struct.CommunicationAttachmentLinkReadmultiKey;
import curam.core.struct.ConcernRoleCommRMDtls;
import curam.core.struct.ConcernRoleCommRMDtlsList;
import curam.core.struct.ConcernRoleCommRMKey;
import curam.core.struct.ConcernRoleCommunicationDtls;
import curam.core.struct.ConcernRoleCommunicationKey;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleIDStatusCodeKey;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.CreateAttachmentCommStruct;
import curam.core.struct.NameReadMultiDtls;
import curam.core.struct.NameReadMultiDtlsList;
import curam.core.struct.OtherAddressData;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonKey;
import curam.core.struct.ProductDeliveryDtls;
import curam.core.struct.ProductDeliveryKey;
import curam.core.struct.WMInstanceDataDtls;
import curam.creole.value.CodeTableItem;
import curam.dynamicevidence.datastore.impl.DynamicEvidenceDatastoreFactory;
import curam.dynamicevidence.impl.DynamicEvidenceDataAttributeDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.sl.entity.fact.DynamicEvidenceDataAttributeFactory;
import curam.dynamicevidence.sl.entity.intf.DynamicEvidenceDataAttribute;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataAttributeDtlsList;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataIDAndAttrKey;
import curam.dynamicevidence.sl.entity.struct.EvidenceIDDetails;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.litereferral.impl.CuramConst;
import curam.message.BPOCOMMUNICATION;
import curam.participant.impl.ConcernRoleDAO;
import curam.pdc.facade.fact.PDCPersonFactory;
import curam.pdc.facade.intf.PDCPerson;
import curam.pdc.facade.struct.PDCEvidenceDetails;
import curam.pdc.facade.struct.PDCEvidenceDetailsList;
import curam.pdc.impl.PDCConst;
import curam.piwrapper.caseheader.impl.ProductDeliveryDAO;
import curam.util.administration.fact.CodeTableAdminFactory;
import curam.util.administration.struct.CodeTableItemDetails;
import curam.util.administration.struct.CodeTableItemUniqueKey;
import curam.util.events.impl.EventService;
import curam.util.events.struct.Event;
import curam.util.exception.AppException;
import curam.util.exception.AppRuntimeException;
import curam.util.exception.DatabaseException;
import curam.util.exception.InformationalException;
import curam.util.exception.LocalisableString;
import curam.util.exception.RecordNotFoundException;
import curam.util.fact.DeferredProcessingFactory;
import curam.util.internal.xml.fact.XSLTemplateFactory;
import curam.util.internal.xml.struct.XSLTemplateKey;
import curam.util.internal.xml.struct.XSLTemplateVersionAndTemplateNameDetails;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import curam.util.transaction.TransactionInfo.TransactionType;
import curam.util.type.CodeTable;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.NotFoundIndicator;
import curam.util.type.ValueList;
import curam.util.workflow.fact.BizObjAssociationFactory;
import curam.util.workflow.impl.EnactmentService;
import curam.util.workflow.intf.BizObjAssociation;
import curam.util.workflow.struct.BizObjAssocCountOpenTasksKey;
import curam.util.workflow.struct.TaskCount;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class BDMCommunicationHelper {

  Communication commObj = CommunicationFactory.newInstance();

  public BDMCommunicationHelper() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Inject
  private Provider<CMSMetadataInterface> cmsMetadataProvider;

  @Inject
  protected Attachment attachment;

  @Inject
  protected ProductDeliveryDAO productDeliveryDAO;

  @Inject
  private CitizenWorkspaceAccountManager citizenWorkspaceAccountManager;

  @Inject
  private ConcernRoleDAO concernRoleDAO;

  @Inject
  BDMCorrespondenceProcessingCenters processingCenters;

  /**
   * Mapping Language to Locale based on the code table description
   *
   * @param languageCode Language from CT_Language
   * @param locale Code table key
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public boolean verifyLanguageToLocale(final String languageCode,
    final String locale) throws AppException, InformationalException {

    final CodeTableItemUniqueKey codeTableItemUniKey =
      new CodeTableItemUniqueKey();

    codeTableItemUniKey.code = locale;
    codeTableItemUniKey.locale = LOCALE.ENGLISH;
    codeTableItemUniKey.tableName = LOCALE.TABLENAME;
    final CodeTableItemDetails localectDetails = CodeTableAdminFactory
      .newInstance().readCodeTableItemDetails(codeTableItemUniKey);
    codeTableItemUniKey.code = languageCode;
    codeTableItemUniKey.locale = LOCALE.ENGLISH;
    codeTableItemUniKey.tableName = LANGUAGE.TABLENAME;
    final CodeTableItemDetails languagectDetails = CodeTableAdminFactory
      .newInstance().readCodeTableItemDetails(codeTableItemUniKey);

    return localectDetails.description
      .contains(languagectDetails.description);

  }

  /**
   * Get locale from languageLocaleMap table with the matching language code
   *
   * @param languageCode Language from CT_Language
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public String getLocaleFromLanguage(final String languageCode)
    throws AppException, InformationalException {

    final LanguageLocaleMap lanLocMap =
      LanguageLocaleMapFactory.newInstance();
    final LanguageLocaleMapKey lanLocMapKey = new LanguageLocaleMapKey();
    lanLocMapKey.key.languageCode = languageCode;
    final LanguageLocaleMapDetailsList lanLocMapList =
      lanLocMap.listLanguageLocaleMap();
    for (final curam.core.sl.entity.struct.LanguageLocaleMapDetails lanLocMapDetails : lanLocMapList.list.dtls
      .items()) {
      if (lanLocMapDetails.languageCode.equals(languageCode)) {
        return lanLocMapDetails.localeIdentifier;
      }
    }
    return "";
  }

  /**
   * Create communication in Letter type of attachment
   *
   * @param commIDKey Communication ID Key
   * @param fileContent File content
   * @param caseID Case ID
   * @param correspondentParticipantRoleID Corres. participant role ID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public TemplateAndDocumentDataKey createCommLetterAttachment(
    final curam.core.sl.struct.CommunicationIDKey commIDKey,
    final FileNameAndDataDtls fileNameAndDataDtls, final long caseID,
    final long correspondentParticipantRoleID)
    throws AppException, InformationalException {

    final TemplateAndDocumentDataKey templateAndDocumentDataKey =
      new TemplateAndDocumentDataKey();
    // Microsoft Word Attachment manipulation variables
    final curam.core.intf.MaintainAttachmentAssistant maintainAttachmentAssistantObj =
      curam.core.fact.MaintainAttachmentAssistantFactory.newInstance();
    final CreateAttachmentCommStruct createAttachmentCommStruct =
      new CreateAttachmentCommStruct();

    // Set details to create attachment
    createAttachmentCommStruct.newAttachmentName =
      fileNameAndDataDtls.fileName;

    // Without the filename extension the document will not open
    // check if file extension is already appended
    if (!createAttachmentCommStruct.newAttachmentName
      .endsWith(ExtensionConst.kPdfFileNameExtension)) {
      createAttachmentCommStruct.newAttachmentName =
        createAttachmentCommStruct.newAttachmentName
          .concat(ExtensionConst.kPdfFileNameExtension);
    }
    createAttachmentCommStruct.newAttachmentContents =
      fileNameAndDataDtls.fileContent;
    createAttachmentCommStruct.documentType = DOCUMENTTYPE.LETTER;
    createAttachmentCommStruct.communicationID = commIDKey.communicationID;

    // CMS implementation -- needs to update when CMS is available
    final CMSMetadataInterface cmsMetadataInt = cmsMetadataProvider.get();

    cmsMetadataInt.add(CMSMetadataConst.kCaseID, Long.toString(caseID));

    cmsMetadataInt.add(CMSMetadataConst.kParticipantID,
      Long.toString(correspondentParticipantRoleID));

    // Insert attachment
    maintainAttachmentAssistantObj
      .insertAttachmentDetails(createAttachmentCommStruct);
    templateAndDocumentDataKey.communicationID = commIDKey.communicationID;
    return templateAndDocumentDataKey;
  }

  /**
   * Create Proforma communication attachment for the generated pdf
   *
   * @param commIDKey Communication ID Key
   * @param fileContent File content
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public TemplateAndDocumentDataKey createProformaCommAttachment(
    final curam.core.sl.struct.CommunicationIDKey commIDKey,
    final FileNameAndDataDtls fileNameAndDataDtls)
    throws AppException, InformationalException {

    long caseID = 0l, correspondentParticipantRoleID = 0l;
    final ProFormaCommKey readMSWordCommunicationKey = new ProFormaCommKey();
    readMSWordCommunicationKey.communicationID = commIDKey.communicationID;
    final ProFormaCommDetails1 proformaCommDetails =
      commObj.readProForma1(readMSWordCommunicationKey);
    caseID = proformaCommDetails.caseID;
    correspondentParticipantRoleID =
      proformaCommDetails.correspondentParticipantRoleID;
    final TemplateAndDocumentDataKey templateAndDocumentDataKey =
      createCommLetterAttachment(commIDKey, fileNameAndDataDtls, caseID,
        correspondentParticipantRoleID);
    templateAndDocumentDataKey.templateID =
      Long.toString(proformaCommDetails.proFormaID);
    return templateAndDocumentDataKey;
  }

  /**
   * create MS Word pdf attachment with different attachment type
   *
   * @param commIDKey Communication ID key
   * @param fileContent File content
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public TemplateAndDocumentDataKey createMSWordPdfCommAttachment(
    final curam.core.sl.struct.CommunicationIDKey commIDKey,
    final FileNameAndDataDtls fileNameAndDataDtls)
    throws AppException, InformationalException {

    // BEGIN Task-17815
    // remove existing attachment before inserting pdf version, to avoid
    // multiple record exception
    supersededAllCommAttachmentsByDocumentType(commIDKey.communicationID,
      null);
    // END Task-17815

    long caseID = 0l, correspondentParticipantRoleID = 0l;
    final ReadMSWordCommunicationKey readMSWordCommunicationKey =
      new ReadMSWordCommunicationKey();
    readMSWordCommunicationKey.communicationID = commIDKey.communicationID;
    final MSWordCommunicationDetails1 msWordCommunicationDetails =
      commObj.readMSWordCommunication1(readMSWordCommunicationKey);
    caseID = msWordCommunicationDetails.caseID;
    correspondentParticipantRoleID =
      msWordCommunicationDetails.correspondentParticipantRoleID;

    final TemplateAndDocumentDataKey templateAndDocumentDataKey =
      createCommLetterAttachment(commIDKey, fileNameAndDataDtls, caseID,
        correspondentParticipantRoleID);
    templateAndDocumentDataKey.templateID =
      msWordCommunicationDetails.templateID;
    return templateAndDocumentDataKey;
  }

  /**
   * Cancel all communication attachment by communication ID by documentType
   *
   * @param communicationID
   * @param documentType
   * @throws AppException
   * @throws InformationalException
   */
  public void removeAllCommAttachmentsByDocumentType(
    final long communicationID, final String documentType)
    throws AppException, InformationalException {

    final CommAttachmentLinkDtlsList commAttLnkList =
      getCommAttachmentLinkList(communicationID);
    for (final CommAttachmentLinkDtls commAttLnk : commAttLnkList.dtls
      .items()) {

      final AttachmentKey attKey = new AttachmentKey();
      attKey.attachmentID = commAttLnk.attachmentID;
      final AttachmentDtls attachmentDtls = attachment.read(attKey);
      if (StringUtil.isNullOrEmpty(documentType)
        && StringUtil.isNullOrEmpty(attachmentDtls.documentType)
        || attachmentDtls.documentType.equals(documentType)) {
        attachment.cancel(attKey);
      }
    }
  }

  /**
   * modify all communication attachment by communication ID and type to
   * superseded status
   *
   * @param communicationID
   * @param documentType
   * @throws AppException
   * @throws InformationalException
   */
  public void supersededAllCommAttachmentsByDocumentType(
    final long communicationID, final String documentType)
    throws AppException, InformationalException {

    final CommAttachmentLinkDtlsList commAttLnkList =
      getCommAttachmentLinkList(communicationID);
    for (final CommAttachmentLinkDtls commAttLnk : commAttLnkList.dtls
      .items()) {

      final AttachmentKey attKey = new AttachmentKey();
      attKey.attachmentID = commAttLnk.attachmentID;
      final AttachmentDtls attachmentDtls = attachment.read(attKey);
      if (StringUtil.isNullOrEmpty(documentType)
        && StringUtil.isNullOrEmpty(attachmentDtls.documentType)
        || attachmentDtls.documentType.equals(documentType)) {
        attachmentDtls.attachmentStatus = ATTACHMENTSTATUS.SUPERSEDED;
        attachment.modify(attKey, attachmentDtls);
      }
    }
  }

  /**
   * Update proforma communication status with communicationID,
   * communicationStatus and communication status date
   *
   * @param modifyCommDetails
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public ProFormaCommDetails1 updateProFormaCommunicationStatus(
    ProFormaCommDetails1 rProformaCommDetails)
    throws AppException, InformationalException {

    final Communication commObj = CommunicationFactory.newInstance();
    final ProFormaCommKey commKey = new ProFormaCommKey();
    commKey.communicationID = rProformaCommDetails.communicationID;
    final ProFormaCommDetails1 proformaDetails =
      commObj.readProForma1(commKey);
    final ProFormaCommDetails1 modifyProformaDetails = proformaDetails;
    modifyProformaDetails.communicationDate =
      rProformaCommDetails.communicationDate;
    modifyProformaDetails.communicationStatus =
      rProformaCommDetails.communicationStatus;
    modifyProformaNoAddressID(modifyProformaDetails);
    rProformaCommDetails = modifyProformaDetails;
    return rProformaCommDetails;
  }

  /**
   * Update MS word communication status
   *
   * @param commDetails
   * @throws AppException
   * @throws InformationalException
   */
  public void updateMSWordCommunicationStatus(
    final MSWordCommunicationDetails1 commDetails)
    throws AppException, InformationalException {

    final ReadMSWordCommunicationKey readMSWordCommKey =
      new ReadMSWordCommunicationKey();
    readMSWordCommKey.communicationID = commDetails.communicationID;
    final MSWordCommunicationDetails1 wordCommDetails =
      commObj.readMSWordCommunication1(readMSWordCommKey);
    wordCommDetails.addressData = CuramConst.kEmptyString;
    wordCommDetails.communicationStatus = commDetails.communicationStatus;
    wordCommDetails.communicationDate = commDetails.communicationDate;
    commObj.modifyMSWordCommunication1(wordCommDetails);
  }

  /**
   * determine the action menu option based on the communication status that
   * passed to this function
   *
   * @param bdmCommAndListRowActionDtls
   */
  public void determineActionMenuIndicator(
    final BDMCommunicationAndListRowActionDetails bdmCommAndListRowActionDtls)
    throws AppException, InformationalException {

    bdmCommAndListRowActionDtls.commDtls.canceledInd =
      bdmCommAndListRowActionDtls.commDtls.statusCode
        .equals(RECORDSTATUS.CANCELLED);
    bdmCommAndListRowActionDtls.commDtls.proFormaInd =
      bdmCommAndListRowActionDtls.commDtls.communicationFormat
        .equals(COMMUNICATIONFORMAT.PROFORMA);
    bdmCommAndListRowActionDtls.commDtls.msWordInd =
      bdmCommAndListRowActionDtls.commDtls.communicationFormat
        .equals(COMMUNICATIONFORMAT.MSWORD);
    bdmCommAndListRowActionDtls.editActionInd = true; // ootb action
    bdmCommAndListRowActionDtls.previewActionInd = true; // ootb action
    bdmCommAndListRowActionDtls.viewActionInd =
      !bdmCommAndListRowActionDtls.previewActionInd;
    bdmCommAndListRowActionDtls.deleteActionInd = true; // ootb action
    bdmCommAndListRowActionDtls.sendNowActionInd =
      bdmCommAndListRowActionDtls.commDtls.draftEmailInd; // ootb
    // action
    bdmCommAndListRowActionDtls.openWordActionInd =
      bdmCommAndListRowActionDtls.commDtls.msWordInd; // ootb
    // action

    // TASK-119696, Start
    // If the communication type is "Migrated Correspondence",
    // No action should be available
    final ConcernRoleCommunicationKey communicationKey =
      new ConcernRoleCommunicationKey();
    communicationKey.communicationID =
      bdmCommAndListRowActionDtls.commDtls.communicationID;
    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    final ConcernRoleCommunicationDtls commDtls =
      ConcernRoleCommunicationFactory.newInstance().read(nfIndicator,
        communicationKey);
    if (!nfIndicator.isNotFound()) {
      if (BDMConstants.kConversionIdentifier
        .equalsIgnoreCase(commDtls.documentLocation.trim())) {
        bdmCommAndListRowActionDtls.editActionInd = false;
        bdmCommAndListRowActionDtls.modifyStatusActionInd = false;
        bdmCommAndListRowActionDtls.deleteActionInd = false;
        bdmCommAndListRowActionDtls.enableTrackingInd = false;
        bdmCommAndListRowActionDtls.misdirectActionInd = false;
        bdmCommAndListRowActionDtls.previewActionInd = false;
        bdmCommAndListRowActionDtls.recallActionInd = false;
        bdmCommAndListRowActionDtls.resubmitActionInd = false;
        bdmCommAndListRowActionDtls.returnActionInd = false;
        bdmCommAndListRowActionDtls.returnedActionInd = false;
        bdmCommAndListRowActionDtls.sendNowActionInd = false;
        bdmCommAndListRowActionDtls.submitActionInd = false;
        bdmCommAndListRowActionDtls.viewActionInd = false;
        bdmCommAndListRowActionDtls.voidActionInd = false;
        // Set the document location to be used at the calling class
        bdmCommAndListRowActionDtls.documentLocation =
          commDtls.documentLocation.trim();
        return;
      }
    }
    // TASK-119696, End

    // BUG-91269, Start
    // For correspondence letters, only 3 indicators will be enabled
    // 1. Edit
    // 2. Edit Status
    // 3. Cancel
    if (COMMUNICATIONFORMAT.CORRESPONDENCE.equalsIgnoreCase(
      bdmCommAndListRowActionDtls.commDtls.communicationFormat))

    {
      if (COMMUNICATIONSTATUS.DRAFT.equalsIgnoreCase(
        bdmCommAndListRowActionDtls.commDtls.communicationStatus)
        || COMMUNICATIONSTATUS.SUBMITTED.equalsIgnoreCase(
          bdmCommAndListRowActionDtls.commDtls.communicationStatus)
        || COMMUNICATIONSTATUS.UNDER_REVIEW.equalsIgnoreCase(
          bdmCommAndListRowActionDtls.commDtls.communicationStatus)
        || COMMUNICATIONSTATUS.QA_REJECTED.equalsIgnoreCase(
          bdmCommAndListRowActionDtls.commDtls.communicationStatus)
        || COMMUNICATIONSTATUS.FAILED_DELIVERY.equalsIgnoreCase(
          bdmCommAndListRowActionDtls.commDtls.communicationStatus)) {
        bdmCommAndListRowActionDtls.editActionInd = true;
        bdmCommAndListRowActionDtls.modifyStatusActionInd = false;
        bdmCommAndListRowActionDtls.deleteActionInd = true;
        bdmCommAndListRowActionDtls.enableTrackingInd = true;
        bdmCommAndListRowActionDtls.misdirectActionInd = false;
        bdmCommAndListRowActionDtls.previewActionInd = false;
        bdmCommAndListRowActionDtls.recallActionInd = false;
        bdmCommAndListRowActionDtls.resubmitActionInd = false;
        bdmCommAndListRowActionDtls.returnActionInd = false;
        bdmCommAndListRowActionDtls.returnedActionInd = false;
        bdmCommAndListRowActionDtls.sendNowActionInd = false;
        bdmCommAndListRowActionDtls.submitActionInd = false;
        bdmCommAndListRowActionDtls.viewActionInd = false;
        bdmCommAndListRowActionDtls.voidActionInd = false;
      } else if (COMMUNICATIONSTATUS.SENT.equalsIgnoreCase(
        bdmCommAndListRowActionDtls.commDtls.communicationStatus)
        || COMMUNICATIONSTATUS.RETURNED.equalsIgnoreCase(
          bdmCommAndListRowActionDtls.commDtls.communicationStatus)
        || COMMUNICATIONSTATUS.VOID.equalsIgnoreCase(
          bdmCommAndListRowActionDtls.commDtls.communicationStatus)
        || COMMUNICATIONSTATUS.MISDIRECTED.equalsIgnoreCase(
          bdmCommAndListRowActionDtls.commDtls.communicationStatus)
        || COMMUNICATIONSTATUS.SUPPRESS.equalsIgnoreCase(
          bdmCommAndListRowActionDtls.commDtls.communicationStatus)
        || COMMUNICATIONSTATUS.PRINTFAILED.equalsIgnoreCase(
          bdmCommAndListRowActionDtls.commDtls.communicationStatus)) {
        bdmCommAndListRowActionDtls.editActionInd = false;
        bdmCommAndListRowActionDtls.modifyStatusActionInd = true;
        bdmCommAndListRowActionDtls.deleteActionInd = false;
        bdmCommAndListRowActionDtls.enableTrackingInd = true;
        bdmCommAndListRowActionDtls.misdirectActionInd = false;
        bdmCommAndListRowActionDtls.previewActionInd = false;
        bdmCommAndListRowActionDtls.recallActionInd = false;
        bdmCommAndListRowActionDtls.resubmitActionInd = false;
        bdmCommAndListRowActionDtls.returnActionInd = false;
        bdmCommAndListRowActionDtls.returnedActionInd = false;
        bdmCommAndListRowActionDtls.sendNowActionInd = false;
        bdmCommAndListRowActionDtls.submitActionInd = false;
        bdmCommAndListRowActionDtls.viewActionInd = false;
        bdmCommAndListRowActionDtls.voidActionInd = false;
      } else if (COMMUNICATIONSTATUS.CANCELLED.equalsIgnoreCase(
        bdmCommAndListRowActionDtls.commDtls.communicationStatus)) {
        bdmCommAndListRowActionDtls.editActionInd = false;
        bdmCommAndListRowActionDtls.modifyStatusActionInd = false;
        bdmCommAndListRowActionDtls.deleteActionInd = false;
        bdmCommAndListRowActionDtls.enableTrackingInd = false;
        bdmCommAndListRowActionDtls.misdirectActionInd = false;
        bdmCommAndListRowActionDtls.previewActionInd = false;
        bdmCommAndListRowActionDtls.recallActionInd = false;
        bdmCommAndListRowActionDtls.resubmitActionInd = false;
        bdmCommAndListRowActionDtls.returnActionInd = false;
        bdmCommAndListRowActionDtls.returnedActionInd = false;
        bdmCommAndListRowActionDtls.sendNowActionInd = false;
        bdmCommAndListRowActionDtls.submitActionInd = false;
        bdmCommAndListRowActionDtls.viewActionInd = false;
        bdmCommAndListRowActionDtls.voidActionInd = false;
      } else if (COMMUNICATIONSTATUS.SUBMITTED_OVERSIZE.equalsIgnoreCase(
        bdmCommAndListRowActionDtls.commDtls.communicationStatus)) {
        bdmCommAndListRowActionDtls.editActionInd = false;
        bdmCommAndListRowActionDtls.modifyStatusActionInd = true;
        bdmCommAndListRowActionDtls.deleteActionInd = true;
        bdmCommAndListRowActionDtls.enableTrackingInd = false;
        bdmCommAndListRowActionDtls.misdirectActionInd = false;
        bdmCommAndListRowActionDtls.previewActionInd = false;
        bdmCommAndListRowActionDtls.recallActionInd = false;
        bdmCommAndListRowActionDtls.resubmitActionInd = false;
        bdmCommAndListRowActionDtls.returnActionInd = false;
        bdmCommAndListRowActionDtls.returnedActionInd = false;
        bdmCommAndListRowActionDtls.sendNowActionInd = false;
        bdmCommAndListRowActionDtls.submitActionInd = false;
        bdmCommAndListRowActionDtls.viewActionInd = false;
        bdmCommAndListRowActionDtls.voidActionInd = false;
      }
    }
    // BUG-91269, End
  }

  /**
   * Read Dynamic evidence from dynamic evidence data details struct and
   * attribute
   *
   * @param dynEvidDataDtls
   * @param attribute for Dynamic Evidence
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public CodeTableItem getDynamicEvidenceCodetableByAttribute(
    final DynamicEvidenceDataDetails dynEvidDataDtls, final String attribute)
    throws AppException, InformationalException {

    // BEGIN TASK 16487 Modified For Notification Message
    if (dynEvidDataDtls != null) {
      return (curam.creole.value.CodeTableItem) DynamicEvidenceTypeConverter
        .convert(dynEvidDataDtls.getAttribute(attribute));
    } else {
      return null;
    }
    // END TASK 16487 Modified For Notification Message
  }

  /**
   * Get Preferred communication method codetable code from conatct Preference
   *
   * @param concernroleid
   * @return
   * @throws Exception
   */
  public CodeTableItem getPreferredCommunicationCTI(final long concernroleid)
    throws Exception {

    final DynamicEvidenceDataDetails evid =
      getContactPreferenceEvid(concernroleid);

    return getDynamicEvidenceCodetableByAttribute(evid,
      BDMConstants.kpreferredCommunicationMethod);
  }

  /**
   * Get Preferred language codetable code from conatct Preference
   *
   * @param concernroleid
   * @return
   * @throws Exception
   */
  public CodeTableItem getPreferredLanguageCTI(final long concernroleid)
    throws Exception {

    final DynamicEvidenceDataDetails dynEvidDataDtls =
      getContactPreferenceEvid(concernroleid);

    return getDynamicEvidenceCodetableByAttribute(dynEvidDataDtls,
      BDMConstants.kpreferredLanguage);
  }

  // Get Preferred Written language from conatct Preference
  public CodeTableItem getPreferredWrittenLanguageCTI(
    final long concernroleid) throws AppException, InformationalException {

    final DynamicEvidenceDataDetails dynEvidDataDtls =
      getContactPreferenceEvid(concernroleid);

    return getDynamicEvidenceCodetableByAttribute(dynEvidDataDtls,
      BDMConstants.kpreferredWrittenLanguage);
  }

  // Get PDC Contact Preference evidence
  public DynamicEvidenceDataDetails
    getContactPreferenceEvid(final long correspondentConcernRoleID)
      throws AppException, InformationalException {

    return getPersonEvidenceByType(correspondentConcernRoleID,
      PDCConst.PDCCONTACTPREFERENCES)[0];

  }

  // Get PDC Email address evidence
  public DynamicEvidenceDataDetails[]
    getEmailAddressEvidence(final long correspondentConcernRoleID)
      throws AppException, InformationalException {

    return getPersonEvidenceByType(correspondentConcernRoleID,
      PDCConst.PDCEMAILADDRESS);

  }

  // Get PDC Phone number Dynamic Evidence
  public DynamicEvidenceDataDetails[]
    getPhoneNumberEvidence(final long correspondentConcernRoleID)
      throws AppException, InformationalException {

    return getPersonEvidenceByType(correspondentConcernRoleID,
      PDCConst.PDCPHONENUMBER);
  }

  // Get PDC Address Dynamic Evidence Data Detail
  public DynamicEvidenceDataDetails[]
    getAddressEvidence(final long correspondentConcernRoleID)
      throws AppException, InformationalException {

    return getPersonEvidenceByType(correspondentConcernRoleID,
      PDCConst.PDCADDRESS);
  }

  /**
   * Read Person Evidence list by Type and concernroleid
   *
   * @param correspondentConcernRoleID
   * @param evidType
   * @return
   * @throws Exception
   */
  public DynamicEvidenceDataDetails[] getPersonEvidenceByType(
    final long correspondentConcernRoleID, final String evidType)
    throws AppException, InformationalException {

    final PersonAndEvidenceTypeList personEvidTypeList =
      new PersonAndEvidenceTypeList();
    personEvidTypeList.concernRoleID = correspondentConcernRoleID;

    personEvidTypeList.evidenceTypeList = evidType;
    final PDCEvidenceDetailsList pdcEvidDtlsList = PDCPersonFactory
      .newInstance().listParticipantEvidenceByTypes(personEvidTypeList);
    final ArrayList<DynamicEvidenceDataDetails> dynEvidDataDetailsList =
      new ArrayList<DynamicEvidenceDataDetails>();
    for (final PDCEvidenceDetails pdcEvidDtls : pdcEvidDtlsList.list
      .items()) {
      final EvidenceControllerInterface evidContInterface =
        (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
      final EIEvidenceKey evidKey = new EIEvidenceKey();
      evidKey.evidenceID = pdcEvidDtls.evidenceID;
      evidKey.evidenceType = pdcEvidDtls.evidenceType;
      final EIEvidenceReadDtls evidReadDtls =
        evidContInterface.readEvidence(evidKey);
      dynEvidDataDetailsList
        .add((DynamicEvidenceDataDetails) evidReadDtls.evidenceObject);

    }

    // BEGIN Task 19944: Commented unwanted else block.
    /*
     * else {
     * // BEGIN TASK 16487 Modified For Notification Message
     * Trace.kTopLevelLogger
     * .info("No Evidence Data Found for correspondentConcernRoleID : "
     * + correspondentConcernRoleID + " for Evidence Type :" + evidType);
     * // throw new Exception("no contact preference");
     * // END TASK 16487 Modified For Notification Message
     * }
     */
    // END Task 19944:
    final DynamicEvidenceDataDetails[] returnList =
      new DynamicEvidenceDataDetails[dynEvidDataDetailsList.size()];
    dynEvidDataDetailsList.toArray(returnList);
    return returnList;

  }

  /**
   * Create DP Process for the DP submit communication
   *
   * @param ticketID
   * @param inst_Data_ID
   * @param flag
   */
  public void createDPSubmitCommunication(final long communicationID)
    throws DatabaseException, AppRuntimeException, AppException,
    InformationalException {

    // create WM Instance Data
    final WMInstanceData wmInstanceDataObj =
      WMInstanceDataFactory.newInstance();
    final WMInstanceDataDtls wmInstanceDataDtls = new WMInstanceDataDtls();
    wmInstanceDataDtls.wm_instDataID =
      curam.util.type.UniqueID.nextUniqueID("DPTICKET");
    wmInstanceDataDtls.enteredByID = TransactionInfo.getProgramUser();
    wmInstanceDataDtls.comments = Long.toString(communicationID);
    wmInstanceDataObj.insert(wmInstanceDataDtls);

    final BDMWMInstanceData bdmWMInstanceDataObj =
      BDMWMInstanceDataFactory.newInstance();
    final BDMWMInstanceDataDtls bdmWMInstanceDataDtls =
      new BDMWMInstanceDataDtls();
    bdmWMInstanceDataDtls.wm_instDataID = wmInstanceDataDtls.wm_instDataID;
    bdmWMInstanceDataDtls.communicationID = communicationID;
    bdmWMInstanceDataObj.insert(bdmWMInstanceDataDtls);

    if (TransactionInfo.getTransactionType().equals(TransactionType.kBatch)) {
      Trace.kTopLevelLogger
        .info("Process Batch Transaction - submitCommunicationDP: "
          + communicationID);
      try {
        BDMCommunicationDPFactory.newInstance().submitCommunicationDP(
          curam.core.impl.CuramConst.kLongZero,
          wmInstanceDataDtls.wm_instDataID, false);
      } catch (final AppException | InformationalException e) {
        final BDMSubmitCorrespondenceDPErrorHandler errorHandler =
          new BDMSubmitCorrespondenceDPErrorHandler();
        errorHandler.dpHandleError(
          BDMSubmitCorrespondenceDPErrorHandler.class.getCanonicalName(),
          wmInstanceDataDtls.wm_instDataID);
      }
    } else {
      // start the DP Process
      DeferredProcessingFactory.newInstance().startProcess(
        BDMConstants.kSUBMITCORRESPONDENCEDP,
        wmInstanceDataDtls.wm_instDataID,
        BDMSubmitCorrespondenceDPErrorHandler.class.getCanonicalName());
    }
  }

  /**
   * Get communication attachment list by communicationID
   *
   * @param communicationID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public CommAttachmentLinkDtlsList getCommAttachmentLinkList(
    final long communicationID) throws AppException, InformationalException {

    final CommAttachmentLink commAttachmentLinkObj =
      CommAttachmentLinkFactory.newInstance();
    final CommunicationAttachmentLinkReadmultiKey commAttLinkReadMultiKey =
      new CommunicationAttachmentLinkReadmultiKey();
    commAttLinkReadMultiKey.communicationID = communicationID;
    final CommAttachmentLinkDtlsList commAttLnkList =
      commAttachmentLinkObj.searchByCommunication(commAttLinkReadMultiKey);
    return commAttLnkList;
  }

  // Remove communication attachment by communication ID
  public void removeCommAttachment(final long communicationID)
    throws AppException, InformationalException {

    final CommAttachmentLinkDtlsList commAttLnkDtlsList =
      getCommAttachmentLinkList(communicationID);
    for (final CommAttachmentLinkDtls commAttLnk : commAttLnkDtlsList.dtls
      .items()) {

      final AttachmentKey attKey = new AttachmentKey();
      attKey.attachmentID = commAttLnk.attachmentID;
      final AttachmentDtls attDtls = attachment.read(attKey);
      if (attDtls.documentType.equals(DOCUMENTTYPE.LETTER)) {
        attDtls.attachmentStatus = ATTACHMENTSTATUS.REMOVED;
        attachment.modify(attKey, attDtls);
      }
    }
  }

  /**
   * Get the active communication by communicationID
   *
   * @param communicationID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public CommAttachmentLinkDtlsList getActiveCommAttachmentLink(
    final long communicationID) throws AppException, InformationalException {

    final CommAttachmentLinkDtlsList commAttLinkDtlsList =
      getCommAttachmentLinkList(communicationID);
    for (final CommAttachmentLinkDtls commAttLnk : commAttLinkDtlsList.dtls
      .items()) {

      final AttachmentKey attKey = new AttachmentKey();
      attKey.attachmentID = commAttLnk.attachmentID;
      final AttachmentDtls attDtls = attachment.read(attKey);
      if (attDtls.documentType.equals(DOCUMENTTYPE.LETTER)
        && attDtls.attachmentStatus.equals(ATTACHMENTSTATUS.ACTIVE)) {
        continue;
      } else {
        commAttLinkDtlsList.dtls.remove(commAttLnk);
      }
    }
    return commAttLinkDtlsList;
  }

  /**
   * Modify Proforma communication without address ID
   *
   * @param proFormaCommDetails
   * @throws AppException
   * @throws InformationalException
   */
  public void
    modifyProformaNoAddressID(final ProFormaCommDetails1 proFormaCommDetails)
      throws AppException, InformationalException {

    final long tempaddressid = 1l;
    if (proFormaCommDetails.addressID == 0l) {
      proFormaCommDetails.addressID = tempaddressid;
    }
    commObj.modifyProForma1(proFormaCommDetails);

    final ConcernRoleCommunication concernRoleCommObj =
      ConcernRoleCommunicationFactory.newInstance();
    final ConcernRoleCommunicationKey concernRoleCommKey =
      new ConcernRoleCommunicationKey();
    concernRoleCommKey.communicationID = proFormaCommDetails.communicationID;
    final ConcernRoleCommunicationDtls concernRoleCommDtls =
      concernRoleCommObj.read(concernRoleCommKey);
    if (concernRoleCommDtls.addressID == tempaddressid) {
      concernRoleCommDtls.addressID = 0l;
      concernRoleCommObj.modify(concernRoleCommKey, concernRoleCommDtls);
    }
  }

  /**
   * Read external party Link by external party ID
   *
   * @param extPartyID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public CWExternalPartyLinkDtls readCWExternalUserDetailsByEXTPartyID(
    final String extPartyID) throws AppException, InformationalException {

    final ExternalPartySystemRecordStatusKey extPartySysRecStatusKey =
      new ExternalPartySystemRecordStatusKey();
    extPartySysRecStatusKey.externalPartyID = extPartyID;
    extPartySysRecStatusKey.recordStatus = RECORDSTATUS.NORMAL;
    extPartySysRecStatusKey.externalSystemID =
      EXTERNALSYSREFERENCETYPE.INTERNAL_CURAM;
    CWExternalPartyLinkDtls extUserRole = null;
    try {
      extUserRole = CWExternalPartyLinkFactory.newInstance()
        .readActiveByExternalPartyAndSystemRecordStatus(
          extPartySysRecStatusKey);
    } catch (final RecordNotFoundException rnfe) {
      Trace.kTopLevelLogger.info(rnfe.getLocalizedMessage());
    }
    return extUserRole;
  }

  /**
   * calculate delivery mode by participant Role ID
   *
   * @param correspondentParticipantRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public String
    calculateDeliveryModes(final long correspondentParticipantRoleID)
      throws AppException, InformationalException {

    String deliveryMode = "";
    final BDMCommunicationHelper bdmCommHelper = new BDMCommunicationHelper();
    try {
      deliveryMode = bdmCommHelper
        .getPreferredCommunicationCTI(correspondentParticipantRoleID).code();
    } catch (final AppException ae) {
      // Do nothing.
      // Trace.kTopLevelLogger.info(ae.getLocalizedMessage());
    } catch (final Exception e) {
      // Do nothing.
      // Trace.kTopLevelLogger.info(e.getLocalizedMessage());
    }

    if (!StringUtil.isNullOrEmpty(deliveryMode)) {
      if (deliveryMode.equals(COMMUNICATIONMETHOD.DIGITAL)
        && citizenWorkspaceAccountManager.hasLinkedAccount(
          concernRoleDAO.get(correspondentParticipantRoleID))) {
        deliveryMode = COMMUNICATIONMETHOD.DIGITAL;
      } else {
        deliveryMode = COMMUNICATIONMETHOD.HARDCOPY;
      }
    } else {
      deliveryMode = COMMUNICATIONMETHOD.HARDCOPY;
    }

    final boolean hardcopyoverride = Configuration
      .getBooleanProperty(EnvVars.BDM_ENV_COMMUNICATION_HARDCOPY_OVERRIDE);

    if (StringUtil.isNullOrEmpty(deliveryMode) || hardcopyoverride) {
      deliveryMode = COMMUNICATIONMETHOD.HARDCOPY;
    }
    return deliveryMode;
  }

  // read codetableitem by code, tablename, locale string
  public CodeTableItemDetails readCodeTableItem(final String code,
    final String tableName, final String locale)
    throws AppException, InformationalException {

    final CodeTableItemUniqueKey ctItemUniKey = new CodeTableItemUniqueKey();
    ctItemUniKey.code = code;
    ctItemUniKey.locale = locale;
    ctItemUniKey.tableName = tableName;
    return CodeTableAdminFactory.newInstance()
      .readCodeTableItemDetails(ctItemUniKey);
  }

  /**
   * Get Unread correspondence count by concern role id
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public int getUnreadCount(final long concernRoleID)
    throws AppException, InformationalException {

    final BDMConcernRoleCommunication bdmConcernRoleCommObj =
      (BDMConcernRoleCommunication) BDMConcernRoleCommunicationFactory
        .newInstance();
    final ConcernRoleCommRMKey concernRoleCommKey =
      new ConcernRoleCommRMKey();
    concernRoleCommKey.concernRoleID = concernRoleID;
    final ConcernRoleCommRMDtlsList crCommDtlsList =
      ConcernRoleCommunicationFactory.newInstance()
        .searchByConcernRole(concernRoleCommKey);

    int unreadCount = 0;
    for (final ConcernRoleCommRMDtls crCommDtls : crCommDtlsList.dtls
      .items()) {
      final BDMConcernRoleCommunicationKey bdmCommKey =
        new BDMConcernRoleCommunicationKey();
      bdmCommKey.communicationID = crCommDtls.communicationID;
      final NotFoundIndicator nfi = new NotFoundIndicator();
      final BDMConcernRoleCommunicationDtls bdmCommDtls =
        bdmConcernRoleCommObj.read(nfi, bdmCommKey);
      if (crCommDtls.communicationStatus.equals(COMMUNICATIONSTATUS.SENT)
        || crCommDtls.communicationStatus.equals(COMMUNICATIONSTATUS.RETURNED)
        || crCommDtls.communicationStatus.equals(COMMUNICATIONSTATUS.VOID)) {
        if (nfi.isNotFound())
          unreadCount++;
        else if (bdmCommDtls.cwFirstReadDateTime
          .equals(DateTime.kZeroDateTime))
          unreadCount++;
      }
    }
    return unreadCount;
  }

  /**
   * Read product delivery by Case ID
   *
   * @param caseID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public ProductDeliveryDtls readProductDeliveryByCaseID(final long caseID)
    throws AppException, InformationalException {

    final ProductDelivery prodDelObj = ProductDeliveryFactory.newInstance();
    final ProductDeliveryKey prodDelKey = new ProductDeliveryKey();
    prodDelKey.caseID = caseID;
    final NotFoundIndicator nfInd = new NotFoundIndicator();
    final ProductDeliveryDtls pdDtls = prodDelObj.read(nfInd, prodDelKey);
    return pdDtls;
  }

  // START : TASK 16339 Display incarceration periods in Disentitlement letter -
  // JP

  /**
   * Retrieve all ineligible periods from determinations
   *
   * @param caseID
   * @Param evidType evidenceType
   * @param concernRoleID
   *
   *
   * @return dynEvidDataDetailsList
   */

  public List<CaseDeterminationDecisionListDetails>
    retrieveIneligiblePeriodsFromDeterminations(final long caseID)
      throws AppException, InformationalException {

    final List<CaseDeterminationDecisionListDetails> listIncarcerationPeriods =
      new ArrayList<CaseDeterminationDecisionListDetails>();

    final CaseDetermination caseDetermination =
      CaseDeterminationFactory.newInstance();
    final CaseIDDeterminationIDKey caseIDDeterminationIDKey =
      new CaseIDDeterminationIDKey();
    caseIDDeterminationIDKey.caseID = caseID;

    final CaseDeterminationDecisionDetailsList determinationDecisionDetailsList =
      caseDetermination
        .listDecisionPeriodsForDetermination(caseIDDeterminationIDKey);

    // List all determinations periords from coverrage period.
    final ValueList<CaseDeterminationDecisionListDetails> listCaseDeterminationsPeriods =
      determinationDecisionDetailsList.dtls;

    for (int i = 0; i < listCaseDeterminationsPeriods.size(); i++) {

      // Filter all ineligible periods
      final CaseDeterminationDecisionListDetails details =
        listCaseDeterminationsPeriods.get(i);
      if (details.resultCode
        .equalsIgnoreCase(CASEDETERMINATIONINTERVALRESULT.INELIGIBLE)) {

        listIncarcerationPeriods.add(details);
      }
    }
    return listIncarcerationPeriods;
  }

  /**
   * Get list of incarceration evidence(s) from Integrated case
   *
   * @param caseID
   * @Param evidType evidenceType
   * @param concernRoleID
   *
   *
   * @return dynEvidDataDetailsList
   */

  public List<DynamicEvidenceDataDetails> listEvidenceByType(
    final String evidType, final long concernRoleID) throws Exception {

    final List<DynamicEvidenceDataDetails> dynEvidDataDetailsList =
      new ArrayList<DynamicEvidenceDataDetails>();
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();

    final ActiveCasesConcernRoleIDAndTypeKey activeACCasesConcernRoleIDAndTypeKey =
      new ActiveCasesConcernRoleIDAndTypeKey();

    activeACCasesConcernRoleIDAndTypeKey.caseTypeCode =
      CASETYPECODE.INTEGRATEDCASE;
    activeACCasesConcernRoleIDAndTypeKey.concernRoleID = concernRoleID;
    activeACCasesConcernRoleIDAndTypeKey.statusCode = CASESTATUS.ACTIVE;

    final CaseHeaderDtlsList caseHeaderDtlsList = caseHeaderObj
      .searchByConcernRoleIDType(activeACCasesConcernRoleIDAndTypeKey);

    if (!caseHeaderDtlsList.dtls.isEmpty()) {

      final long applicationCaseID = caseHeaderDtlsList.dtls.item(0).caseID;

      final ActiveEvdInstanceDtlsList activeEvdDtlsList =
        listActiveEvidence(applicationCaseID, evidType);

      for (final ActiveEvdInstanceDtls activeEvdInstanceDtls : activeEvdDtlsList.dtls) {
        final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
          DynamicEvidenceDatastoreFactory.newDatastoreInstance()
            .readEvidence(activeEvdInstanceDtls.evidenceID);

        dynEvidDataDetailsList.add(dynamicEvidenceDataDetails);

      }
    }

    return dynEvidDataDetailsList;

  }

  /**
   * Get incarceration end date if person has multiple incarcerations in current
   * year
   * Return EOY if Incarceration extends to next year or no end date specified
   *
   * @param list list of incarceartion endDate
   *
   *
   * @return endDate
   */

  // BUG Fix 59960

  public Date getIncarceraationEndDate(
    final List<DynamicEvidenceDataDetails> dynEvidDataDtls) {

    Date tempDate = null;
    Date incarEndDate = null;

    for (int i = 0; i < dynEvidDataDtls.size(); i++) {
      final DynamicEvidenceDataDetails details = dynEvidDataDtls.get(i);

      final DynamicEvidenceDataAttributeDetails endDateAttr =
        details.getAttribute("endDate");
      final String date = endDateAttr.getValue();

      // IF no enddate specified than last day of the year
      if (date.equals("00010101") || date.isEmpty())
        tempDate = Date.fromISO8601(BDMConstants.lastDayOfTheYear);
      else
        tempDate = Date.getDate(date);

      // Pick future endadte as incarceratin enddate
      if (incarEndDate == null) {

        incarEndDate = tempDate;
      } else {
        if (tempDate.after(incarEndDate))
          incarEndDate = tempDate;
      }

    }

    final int currentYear =
      Date.getCurrentDate().getCalendar().get(Calendar.YEAR);
    final int incarcerationEndYear =
      incarEndDate.getCalendar().get(Calendar.YEAR);

    // If incarceration extends from currrent year to next year , incarceration
    // is calculated untill EOY - returnlast day of the year

    if (incarcerationEndYear > currentYear)
      return Date.fromISO8601(BDMConstants.lastDayOfTheYear);
    else
      return incarEndDate;

  }

  /**
   * List the active evidence on a given case of a given evidence type.
   *
   * @param caseID The case identifier.
   * @param evidenceType The evidence type.
   *
   * @return The list of matching evidence records.
   *
   * @throws AppException
   * @throws InformationalException
   */
  private ActiveEvdInstanceDtlsList
    listActiveEvidence(final long integratedCaseID, final String evidenceType)
      throws AppException, InformationalException {

    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCaseID;

    final EvidenceTypeList evidTypeList = new EvidenceTypeList();
    final EvidenceTypeDetails evidenceTypeDetails = new EvidenceTypeDetails();
    evidenceTypeDetails.evidenceType = evidenceType;
    evidTypeList.dtls.add(evidenceTypeDetails);

    final EvidenceTypeLimit evidenceTypeLimit = new EvidenceTypeLimit();
    evidenceTypeLimit.limit = 0;

    final ActiveEvdInstanceDtlsList activeEvidDtlsList =
      EvidenceControllerFactory.newInstance()
        .listAllActiveEvdInstancesByTypes(caseKey, evidTypeList,
          evidenceTypeLimit);

    return activeEvidDtlsList;
  }

  // END TASK 16339 Display incarceration periods in Disentitlement letter - JP

  /**
   * TASK 8949
   * Method to supersed the communication attachment.
   *
   * @param communicationID
   * @throws AppException
   * @throws InformationalException
   */
  public void supersedCommAttachments(final long communicationID)
    throws AppException, InformationalException {

    final CommAttachmentLinkDtlsList commAttLnkList =
      getCommAttachmentLinkList(communicationID);
    for (final CommAttachmentLinkDtls commAttLnk : commAttLnkList.dtls
      .items()) {

      final AttachmentKey attKey = new AttachmentKey();
      attKey.attachmentID = commAttLnk.attachmentID;

      // Read before modify
      final AttachmentDtls attachmentDtls = attachment.read(attKey);

      if (attachmentDtls.attachmentStatus.equals(ATTACHMENTSTATUS.ACTIVE)) {
        // Update status to superseded.
        attachmentDtls.attachmentStatus = ATTACHMENTSTATUS.SUPERSEDED;
        attachment.modify(attKey, attachmentDtls);
      }
    }
  }

  /**
   * TASK 8949
   * Method to supersed the communication attachment by document type.
   *
   * @param communicationID
   * @param documentType
   * @throws AppException
   * @throws InformationalException
   */
  public void supersedCommAttachmentByDocumentType(final long communicationID,
    final String documentType) throws AppException, InformationalException {

    final CommAttachmentLinkDtlsList commAttLnkList =
      getCommAttachmentLinkList(communicationID);
    for (final CommAttachmentLinkDtls commAttLnk : commAttLnkList.dtls
      .items()) {

      final AttachmentKey attKey = new AttachmentKey();
      attKey.attachmentID = commAttLnk.attachmentID;

      // Read before modify
      final AttachmentDtls attachmentDtls = attachment.read(attKey);

      if (attachmentDtls.documentType.equals(documentType)
        && attachmentDtls.attachmentStatus.equals(ATTACHMENTSTATUS.ACTIVE)) {
        // Update status to superseded.
        attachmentDtls.attachmentStatus = ATTACHMENTSTATUS.SUPERSEDED;
        attachment.modify(attKey, attachmentDtls);
        break;
      }
    }
  }

  /**
   * TASK 8949
   * Method to supersed the communication attachment by document type.
   *
   * @param communicationID
   * @param documentType
   * @throws AppException
   * @throws InformationalException
   */
  public XSLTemplateVersionAndTemplateNameDetails
    readProFormaVersionNoAndName(final long proformaID)
      throws AppException, InformationalException {

    final XSLTemplateKey xslTemplateKey = new XSLTemplateKey();
    xslTemplateKey.templateID = proformaID;
    // xslTemplateKey.localeIdentifier = "en";
    final XSLTemplateVersionAndTemplateNameDetails xslTemplateVersionDtls =
      XSLTemplateFactory.newInstance()
        .readLatestVersionAndTemplateName(xslTemplateKey);
    return xslTemplateVersionDtls;
  }

  /**
   * BUG 27254 recalculation trigger
   * Added this method to set locale based on
   * concerRole contact preference
   */
  public XSLTemplateVersionAndTemplateNameDetails
    readProFormaVersionNoAndName(final long proformaID, final String locale)
      throws AppException, InformationalException {

    final XSLTemplateKey xslTemplateKey = new XSLTemplateKey();
    xslTemplateKey.templateID = proformaID;
    xslTemplateKey.localeIdentifier = locale;
    final XSLTemplateVersionAndTemplateNameDetails xslTemplateVersionDtls =
      XSLTemplateFactory.newInstance()
        .readLatestVersionAndTemplateName(xslTemplateKey);
    return xslTemplateVersionDtls;
  }

  /**
   * Method to read BDM WMInstanceData by ID
   *
   * @param wm_InstDataID
   * @return BDMWMInstanceDataDtls
   * @throws AppException
   * @throws InformationalException
   */
  public BDMWMInstanceDataDtls readBDMWMInstanceDatabyID(
    final long wm_InstDataID) throws AppException, InformationalException {

    final BDMWMInstanceDataKey bdmWMInstanceDataKey =
      new BDMWMInstanceDataKey();
    bdmWMInstanceDataKey.wm_instDataID = wm_InstDataID;
    final BDMWMInstanceData bdmWMInstanceDataObj =
      BDMWMInstanceDataFactory.newInstance();
    final BDMWMInstanceDataDtls bdmWMInstanceDataDtls =
      bdmWMInstanceDataObj.read(bdmWMInstanceDataKey);
    return bdmWMInstanceDataDtls;
  }

  /**
   * Method to create BDM WMInstanceData by ID, proformaID and proforma
   * VersionNo
   *
   * @param wm_InstDataID
   * @param proformaID
   * @param proformaVersionNo
   * @return BDMWMInstanceDataDtls
   * @throws AppException
   * @throws InformationalException
   */
  public BDMWMInstanceDataDtls createBDMWMInstanceDataForProFormaComm(
    final long wm_InstDataID, final long proformaID)
    throws AppException, InformationalException {

    final BDMWMInstanceDataDtls bdmWMInstanceDataDtls =
      new BDMWMInstanceDataDtls();
    final BDMWMInstanceData bdmWMInstanceDataObj =
      BDMWMInstanceDataFactory.newInstance();

    // Triggering correspondence deferred process.
    bdmWMInstanceDataDtls.wm_instDataID = wm_InstDataID;
    bdmWMInstanceDataDtls.proformaID = proformaID;
    bdmWMInstanceDataDtls.communicationFormat = COMMUNICATIONFORMAT.PROFORMA;
    bdmWMInstanceDataObj.insert(bdmWMInstanceDataDtls);
    return bdmWMInstanceDataDtls;
  }

  /**
   * Method to create BDM WMInstanceData by ID, proformaID and proforma
   * VersionNo
   *
   * @param caseID
   * @param concernRoleID
   * @return WMInstanceDataDtls
   * @throws AppException
   * @throws InformationalException
   */
  public WMInstanceDataDtls createWMInstanceDataForCommunication(
    final long caseID, final long concernRoleID)
    throws AppException, InformationalException {

    final WMInstanceDataDtls wmInstanceDataDtls = new WMInstanceDataDtls();
    final WMInstanceData wmInstanceDataObj =
      WMInstanceDataFactory.newInstance();

    // Triggering correspondence deferred process.
    wmInstanceDataDtls.wm_instDataID =
      curam.util.type.UniqueID.nextUniqueID(BDMConstants.kDPTICKET);
    wmInstanceDataDtls.enteredByID = TransactionInfo.getProgramUser();
    wmInstanceDataDtls.caseID = caseID;
    wmInstanceDataDtls.concernRoleID = concernRoleID;
    wmInstanceDataObj.insert(wmInstanceDataDtls);

    return wmInstanceDataDtls;
  }

  /**
   * This method is to get the client's mailing address.
   *
   * @param concernRoleID
   * @return OtherAddressData
   *
   * @throws AppException
   * @throws InformationalException
   */
  public OtherAddressData getMailingAddressDataByConcernRole(
    final long concernRoleID) throws AppException, InformationalException {

    final OtherAddressData addressData = new OtherAddressData();

    // get all the addresses for the concernRole
    final ConcernRoleIDStatusCodeKey key = new ConcernRoleIDStatusCodeKey();
    key.concernRoleID = concernRoleID;
    key.statusCode = RECORDSTATUS.NORMAL;

    final AddressReadMultiDtlsList addressList = ConcernRoleAddressFactory
      .newInstance().searchAddressesByConcernRoleIDAndStatus(key);

    // get the mailing address
    for (final AddressReadMultiDtls details : addressList.dtls) {
      if (CONCERNROLEADDRESSTYPE.MAILING.equalsIgnoreCase(details.typeCode)) {
        final AddressKey addressKey = new AddressKey();
        addressKey.addressID = details.addressID;
        addressData.addressData = BDMAddressFactory.newInstance()
          .getCanadaPostAddressFormat(addressKey, null).addressData;
      }
    }

    return addressData;
  }

  /**
   * This method is to get concernRole name of the concernRoleID passed.
   *
   * The following conditions must be checked.
   * 1. Name cannot be more than 40 characters
   * 2. If it's a person/prospect person
   * 2.1 If entire name is more than 40 chars, remove middle name
   * 2.2 If still more than 40 chars, make first name an initial. e.g James
   * Smith
   * becomes J. Smith
   * 3. For other participant types, send the 1st 40 chars.
   *
   * @param concernRoleID - long
   * @return String - ConcernRoleName
   *
   * @throws AppException
   * @throws InformationalException
   */
  public String getConcernRoleName(final long concernRoleID)
    throws AppException, InformationalException {

    String concernRoleName = "";
    final NotFoundIndicator nfInd = new NotFoundIndicator();

    // get the name and type
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = concernRoleID;

    final ConcernRoleDtls concernRoleDtls =
      ConcernRoleFactory.newInstance().read(nfInd, concernRoleKey);

    if (!nfInd.isNotFound()) {

      // If participant is not person or prospect, return name from
      // ConcernRoleDtls
      // after checking for length
      if (!CONCERNROLETYPE.PERSON
        .equalsIgnoreCase(concernRoleDtls.concernRoleType)
        && !CONCERNROLETYPE.PROSPECTPERSON
          .equalsIgnoreCase(concernRoleDtls.concernRoleType)) {
        if (BDMConstants.kCorrespondenceNameLengthMax >= concernRoleDtls.concernRoleName
          .length()) {
          return concernRoleDtls.concernRoleName;
        } else {
          return concernRoleDtls.concernRoleName.substring(
            curam.core.impl.CuramConst.gkZero,
            BDMConstants.kCorrespondenceNameLengthMax);
        }
      } else {
        // For a person or propsect person,
        // get the alternate name type of registered.
        final AlternateNameReadMultiStatusStruct nameKey =
          new AlternateNameReadMultiStatusStruct();
        nameKey.concernRoleID = concernRoleID;
        nameKey.nameStatus = RECORDSTATUS.NORMAL;
        final NameReadMultiDtlsList nameList = AlternateNameFactory
          .newInstance().searchActiveByConcernRole(nameKey);
        for (final NameReadMultiDtls nameDetails : nameList.dtls) {
          if (ALTERNATENAMETYPE.REGISTERED
            .equalsIgnoreCase(nameDetails.nameType)) {
            // check if firstname + Space + lastname is less than maximum
            // allowed
            if (BDMConstants.kCorrespondenceNameLengthMax >= nameDetails.firstForename
              .concat(nameDetails.surname).length() - 1) {
              concernRoleName =
                nameDetails.firstForename + " " + nameDetails.surname;
            } else {
              // Else return "Initial Surname"
              concernRoleName = nameDetails.firstForename.substring(0, 1)
                + " " + nameDetails.surname;
            }
            break;
          }
        }
        // Check the person is not deceased
        validatePersonNotDeceased(concernRoleID);
      }
    }
    return concernRoleName;
  }

  /**
   * This method is to get concernRole name of the concernRoleID passed.
   *
   * This is to print the name inside the letter and not in address.
   * Hence the 40 char limit doesn't apply
   *
   * @param concernRoleID - long
   * @return String - ConcernRoleName
   *
   * @throws AppException
   * @throws InformationalException
   */
  public String getConcernRoleNameForLetter(final long concernRoleID)
    throws AppException, InformationalException {

    String concernRoleName = "";
    final NotFoundIndicator nfInd = new NotFoundIndicator();
    // get the name and type
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = concernRoleID;

    final ConcernRoleDtls concernRoleDtls =
      ConcernRoleFactory.newInstance().read(nfInd, concernRoleKey);

    if (!nfInd.isNotFound()) {
      concernRoleName = concernRoleDtls.concernRoleName;
    }
    return concernRoleName;
  }

  /**
   * This method validates if a person is deceased.
   *
   * @param nameAndTypeDetails
   *
   * @throws AppException
   * @throws InformationalException
   */
  public void validatePersonNotDeceased(final long concernRoleID)
    throws AppException, InformationalException {

    // Check the Person's date of death
    final Person personObj = PersonFactory.newInstance();
    final PersonKey personKey = new PersonKey();
    final NotFoundIndicator nfIndicator = new NotFoundIndicator();

    personKey.concernRoleID = concernRoleID;
    final PersonDtls personDtls = personObj.read(nfIndicator, personKey);

    if (!nfIndicator.isNotFound() && !personDtls.dateOfDeath.isZero()) {

      ValidationManagerFactory.getManager().throwWithLookup(
        new AppException(
          BPOCOMMUNICATION.ERR_COMM_CORRESPONDENT__IS_DECEASED),
        ValidationManagerConst.kSetOne, 1);
    }
  }

  /**
   * This method to pull the address from db
   * and split it to 4 lines according to
   * Canada Post Standards
   *
   * @param concernRoleID
   *
   * @throws AppException
   * @throws InformationalException
   *
   */
  public long getAddressIDforConcern(final long concernRoleID,
    final boolean isThirdPartyContactInd)
    throws AppException, InformationalException {

    final long addressID = readMailingAddress(concernRoleID);

    if (0 == addressID) {
      final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
      concernRoleKey.concernRoleID = concernRoleID;
      // TASK-112710, Start
      // A valid mailing address is needed for every recipient.
      // If not available, throw an error
      final NotFoundIndicator nfIndicator = new NotFoundIndicator();
      final ConcernRoleDtls concernRoleDtls =
        ConcernRoleFactory.newInstance().read(nfIndicator, concernRoleKey);
      if (!nfIndicator.isNotFound()) {
        throw new AppException(BDMBPOCCT.ERR_ADDRESS_IS_MISSING)
          .arg(concernRoleDtls.concernRoleName);
      }
      // TASK-112710, End
    }

    return addressID;
  }

  /**
   * This method returns the mailing address for a concernRole
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public long readMailingAddress(final long concernRoleID)
    throws AppException, InformationalException {

    long addressID = 0;
    final ConcernRoleIDStatusCodeKey concernRoleIDStatusCodeKey =
      new ConcernRoleIDStatusCodeKey();
    concernRoleIDStatusCodeKey.statusCode = RECORDSTATUS.NORMAL;
    concernRoleIDStatusCodeKey.concernRoleID = concernRoleID;
    final AddressReadMultiDtlsList addressReadMultiDtlsList =
      ConcernRoleAddressFactory.newInstance()
        .searchAddressesByConcernRoleIDAndStatus(concernRoleIDStatusCodeKey);
    final Date currentDate = Date.getCurrentDate();
    for (final AddressReadMultiDtls addrDtls : addressReadMultiDtlsList.dtls) {
      // The following conditions should be satisfied
      // 1. Address type should be mailing
      // 2. Address Start date should be current date or before current date
      // 3. Address End date should be null or after current date
      if (CONCERNROLEADDRESSTYPE.MAILING.equalsIgnoreCase(addrDtls.typeCode)
        && (currentDate.equals(addrDtls.startDate)
          || currentDate.after(addrDtls.startDate))
        && (addrDtls.endDate.isZero()
          || currentDate.before(addrDtls.endDate))) {
        addressID = addrDtls.addressID;
        break;
      }

    }

    return addressID;
  }

  public String[] getAddressMapper(final long addressID)
    throws AppException, InformationalException {

    final Map<String, String> addressElmMap =
      processingCenters.getAddressElementMap(addressID);

    final String countryCode =
      addressElmMap.get(BDMConstants.kADDRESSELEMENT_COUNTRY);
    if (StringUtil.isNullOrEmpty(countryCode)) {
      throw new AppException(BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE);
    }

    final StringBuffer line1 = new StringBuffer();
    final StringBuffer line2 = new StringBuffer();
    final StringBuffer line3 = new StringBuffer();
    final StringBuffer line4 = new StringBuffer();
    // Line 5 will hold unparsed
    final StringBuffer line5 = new StringBuffer();

    final String unParsedAddress =
      addressElmMap.get(BDMConstants.kBDMUNPARSE);
    if (!StringUtil.isNullOrEmpty(unParsedAddress)
      && BDMConstants.kOneString.equalsIgnoreCase(unParsedAddress)) {

      // For unparsed addresses, the following will be true
      // 1. Country Code will always be populated
      // 2. Address will be formatted in the following format.
      // APT -> Address Line 1
      // ADD1 -> Address Line 2
      // ADD2 -> Address Line 3
      // City -> Address Line 4
      if (!StringUtil.isNullOrEmpty(
        addressElmMap.get(BDMConstants.kADDRESSELEMENT_APTUNITNUM))) {
        line1
          .append(addressElmMap.get(BDMConstants.kADDRESSELEMENT_APTUNITNUM));
      }
      if (!StringUtil.isNullOrEmpty(
        addressElmMap.get(BDMConstants.kADDRESSELEMENT_STREETNUM))) {
        line2
          .append(addressElmMap.get(BDMConstants.kADDRESSELEMENT_STREETNUM));
      }
      if (!StringUtil.isNullOrEmpty(
        addressElmMap.get(BDMConstants.kADDRESSELEMENT_STREETNAME))) {
        line3
          .append(addressElmMap.get(BDMConstants.kADDRESSELEMENT_STREETNAME));
      }
      if (!StringUtil.isNullOrEmpty(
        addressElmMap.get(BDMConstants.kADDRESSELEMENT_CITY))) {
        line4.append(addressElmMap.get(BDMConstants.kADDRESSELEMENT_CITY));
      }
      line5.append(unParsedAddress);

    } else if (COUNTRY.CA.equalsIgnoreCase(countryCode)) {

      if (!StringUtil.isNullOrEmpty(
        addressElmMap.get(BDMConstants.kADDRESSELEMENT_APTUNITNUM))) {
        line1
          .append(addressElmMap.get(BDMConstants.kADDRESSELEMENT_APTUNITNUM));
        line1.append("-");
      }
      if (!StringUtil.isNullOrEmpty(
        addressElmMap.get(BDMConstants.kADDRESSELEMENT_STREETNUM))) {
        line1
          .append(addressElmMap.get(BDMConstants.kADDRESSELEMENT_STREETNUM));
        line1.append(" ");
      }
      if (!StringUtil.isNullOrEmpty(
        addressElmMap.get(BDMConstants.kADDRESSELEMENT_STREETNAME))) {
        line1
          .append(addressElmMap.get(BDMConstants.kADDRESSELEMENT_STREETNAME));
      }
      if (!StringUtil.isNullOrEmpty(
        addressElmMap.get(BDMConstants.kADDRESSELEMENT_POBOX))) {
        line2.append(addressElmMap.get(BDMConstants.kADDRESSELEMENT_POBOX));
      }
      if (!StringUtil.isNullOrEmpty(
        addressElmMap.get(BDMConstants.kADDRESSELEMENT_CITY))) {
        line3.append(addressElmMap.get(BDMConstants.kADDRESSELEMENT_CITY));
        line3.append(" ");
      }
      if (!StringUtil.isNullOrEmpty(
        addressElmMap.get(BDMConstants.kADDRESSELEMENT_PROVINCE))) {
        line3
          .append(addressElmMap.get(BDMConstants.kADDRESSELEMENT_PROVINCE));
        line3.append("  ");
      }
      if (!StringUtil.isNullOrEmpty(
        addressElmMap.get(BDMConstants.kADDRESSELEMENT_POSTALCODE))) {
        line3
          .append(addressElmMap.get(BDMConstants.kADDRESSELEMENT_POSTALCODE));
      }

      // BUG-100565, Start
      // Get the country full form instead of code.
      // The form will be only in English
      if (!StringUtil.isNullOrEmpty(countryCode)) {
        line4.append(CodeTable.getOneItem(ADDRESSCOUNTRY.TABLENAME,
          countryCode, LOCALE.ENGLISH));
      }
      // BUG-100565, End
    } else if (COUNTRY.US.equalsIgnoreCase(countryCode)) {

      // BUG-91574, Start
      // Add details for US based addresses.

      // Line 1 would be in the following format
      // [AptNum]-Street Number Street Name
      // AptNum is optional
      if (!StringUtil.isNullOrEmpty(
        addressElmMap.get(BDMConstants.kADDRESSELEMENT_APTUNITNUM))) {
        line1
          .append(addressElmMap.get(BDMConstants.kADDRESSELEMENT_APTUNITNUM));
        line1.append("-");
      }
      if (!StringUtil.isNullOrEmpty(
        addressElmMap.get(BDMConstants.kADDRESSELEMENT_STREETNUM))) {
        line1
          .append(addressElmMap.get(BDMConstants.kADDRESSELEMENT_STREETNUM));
        line1.append(" ");
      }
      if (!StringUtil.isNullOrEmpty(
        addressElmMap.get(BDMConstants.kADDRESSELEMENT_STREETNAME))) {
        line1
          .append(addressElmMap.get(BDMConstants.kADDRESSELEMENT_STREETNAME));
      }

      // If there's a PO BOX, add it to line 2.
      if (!StringUtil.isNullOrEmpty(
        addressElmMap.get(BDMConstants.kADDRESSELEMENT_POBOX))) {
        line2.append(addressElmMap.get(BDMConstants.kADDRESSELEMENT_POBOX));
      }

      // Line 3 would be City State and ZIP

      if (!StringUtil.isNullOrEmpty(
        addressElmMap.get(BDMConstants.kADDRESSELEMENT_CITY))) {
        line3.append(addressElmMap.get(BDMConstants.kADDRESSELEMENT_CITY));
        line3.append(" ");
      }
      if (!StringUtil.isNullOrEmpty(
        addressElmMap.get(BDMConstants.kADDRESSELEMENT_STATEPROV))) {
        line3
          .append(addressElmMap.get(BDMConstants.kADDRESSELEMENT_STATEPROV));
        line3.append("  ");
      }
      if (!StringUtil
        .isNullOrEmpty(addressElmMap.get(BDMConstants.kADDRESSELEMENT_ZIP))) {
        line3.append(addressElmMap.get(BDMConstants.kADDRESSELEMENT_ZIP));
      }

      // Line 4 would be country
      // BUG-100565, Start
      // Get the country full form instead of code.
      // The form will be only in English
      if (!StringUtil.isNullOrEmpty(countryCode)) {
        line4.append(CodeTable.getOneItem(ADDRESSCOUNTRY.TABLENAME,
          countryCode, LOCALE.ENGLISH));
      }
      // BUG-100565, End
    } else {
      // BUG-92171, Start
      // For all International addresses

      // line 1 would be
      // [Apt] - Address line 1
      if (!StringUtil.isNullOrEmpty(
        addressElmMap.get(BDMConstants.kADDRESSELEMENT_APTUNITNUM))) {
        line1
          .append(addressElmMap.get(BDMConstants.kADDRESSELEMENT_APTUNITNUM));
        line1.append("-");
      }
      if (!StringUtil.isNullOrEmpty(
        addressElmMap.get(BDMConstants.kADDRESSELEMENT_STREETNUM))) {
        line1
          .append(addressElmMap.get(BDMConstants.kADDRESSELEMENT_STREETNUM));
      }

      if (!StringUtil.isNullOrEmpty(
        addressElmMap.get(BDMConstants.kADDRESSELEMENT_STREETNAME))) {
        line2
          .append(addressElmMap.get(BDMConstants.kADDRESSELEMENT_STREETNAME));
      }

      if (!StringUtil.isNullOrEmpty(
        addressElmMap.get(BDMConstants.kADDRESSELEMENT_CITY))) {
        line2.append(" ");
        line2.append(addressElmMap.get(BDMConstants.kADDRESSELEMENT_CITY));
      }

      if (!StringUtil.isNullOrEmpty(
        addressElmMap.get(BDMConstants.kADDRESSELEMENT_BDMSTPROVX))) {
        line3
          .append(addressElmMap.get(BDMConstants.kADDRESSELEMENT_BDMSTPROVX));
      }

      if (!StringUtil.isNullOrEmpty(
        addressElmMap.get(BDMConstants.kADDRESSELEMENT_BDMZIPX))) {
        line3.append(" ");
        line3.append(addressElmMap.get(BDMConstants.kADDRESSELEMENT_BDMZIPX));
      }

      // line 4 would be country code
      // BUG-100565, Start
      // Get the country full form instead of code.
      // The form will be only in English
      if (!StringUtil.isNullOrEmpty(countryCode)) {
        line4.append(CodeTable.getOneItem(ADDRESSCOUNTRY.TABLENAME,
          countryCode, LOCALE.ENGLISH));
      }
      // BUG-100565, End
    }

    final String[] splittedAddress = new String[5];
    splittedAddress[0] = line1.toString().trim();
    splittedAddress[1] = line2.toString().trim();
    splittedAddress[2] = line3.toString().trim();
    splittedAddress[3] = line4.toString().trim();
    splittedAddress[4] = line5.toString().trim();
    return stripSpecialCharsFromAddress(splittedAddress);

  }

  /**
   * This method removes the characters "#" and "," from address lines.
   *
   * @param splittedAddress
   * @return
   */
  public String[]
    stripSpecialCharsFromAddress(final String[] splittedAddress) {

    String replacedString;
    for (int i = 0; i < splittedAddress.length; i++) {
      replacedString = splittedAddress[i];
      replacedString = replacedString.replace(BDMConstants.kHashSymbol,
        curam.core.impl.CuramConst.gkSpace);
      replacedString =
        replacedString.replace(curam.core.impl.CuramConst.gkCommaDelimiter,
          curam.core.impl.CuramConst.gkSpace);
      // replace double spaces with one space
      while (replacedString.contains(BDMConstants.kDoubleSpacesString)) {
        replacedString =
          replacedString.replace(BDMConstants.kDoubleSpacesString,
            curam.core.impl.CuramConst.gkSpace);
      }
      splittedAddress[i] = replacedString.trim();
    }

    return splittedAddress;
  }

  /**
   * This method returns the preferred language preferences
   * for written communication of a concern role.
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public String getPreferredLanguage(final long concernRoleID)
    throws AppException, InformationalException {

    String preferredLanguage = CuramConst.kEmptyString;
    // Check the concernrole type
    // If Representative then get the preferred language from
    // ConcernRole.preferredLanguage
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = concernRoleID;
    final ConcernRoleDtls concernRoleDtls =
      ConcernRoleFactory.newInstance().read(concernRoleKey);
    if (!CONCERNROLETYPE.PERSON
      .equalsIgnoreCase(concernRoleDtls.concernRoleType)
      && !CONCERNROLETYPE.PROSPECTPERSON
        .equalsIgnoreCase(concernRoleDtls.concernRoleType)) {
      if (!concernRoleDtls.preferredLanguage.isEmpty()) {
        if (LANGUAGE.ENGLISH
          .equalsIgnoreCase(concernRoleDtls.preferredLanguage)
          || BDMLANGUAGE.ENGLISHL
            .equalsIgnoreCase(concernRoleDtls.preferredLanguage)) {
          preferredLanguage = BDMConstants.kClientPreferredLanguageEnglish;
        } else if (LANGUAGE.FRENCH
          .equalsIgnoreCase(concernRoleDtls.preferredLanguage)
          || BDMLANGUAGE.FRENCHL
            .equalsIgnoreCase(concernRoleDtls.preferredLanguage)) {
          preferredLanguage = BDMConstants.kClientPreferredLanguageFrench;
        }
      }
    } else {
      // Otherwise get the preferred language from the dynamic evidence
      final EvidenceTypeKey eType = new EvidenceTypeKey();
      eType.evidenceType = PDCConst.PDCCONTACTPREFERENCES;

      final DynamicEvidenceDataAttribute dynamicEvidenceDataAttribute =
        DynamicEvidenceDataAttributeFactory.newInstance();
      final EvidenceIDDetails evidenceIDDetails = new EvidenceIDDetails();

      PDCEvidenceDetailsList pdcEvidenceList = new PDCEvidenceDetailsList();
      final PDCPerson pdcPersonObj = PDCPersonFactory.newInstance();
      final PersonAndEvidenceTypeList personAndEvidenceType =
        new PersonAndEvidenceTypeList();
      personAndEvidenceType.concernRoleID = concernRoleID;
      personAndEvidenceType.evidenceTypeList = PDCConst.PDCCONTACTPREFERENCES;
      pdcEvidenceList =
        pdcPersonObj.listParticipantEvidenceByTypes(personAndEvidenceType);
      for (int i = 0; i < pdcEvidenceList.list.size(); i++) {
        if (eType.evidenceType
          .equalsIgnoreCase(pdcEvidenceList.list.get(i).evidenceType)) {
          evidenceIDDetails.evidenceID =
            pdcEvidenceList.list.get(i).evidenceID;
          break;
        }
      }
      final DynamicEvidenceDataIDAndAttrKey idAndAttrKey =
        new DynamicEvidenceDataIDAndAttrKey();
      idAndAttrKey.evidenceID = evidenceIDDetails.evidenceID;
      idAndAttrKey.name = BDMConstants.kpreferredWrittenLanguage;
      final DynamicEvidenceDataAttributeDtlsList dataAttributeDtlsList =
        dynamicEvidenceDataAttribute
          .searchByEvidenceIDAndAttribute(idAndAttrKey);

      if (!dataAttributeDtlsList.dtls.isEmpty()) {
        if (BDMLANGUAGE.ENGLISHL
          .equalsIgnoreCase(dataAttributeDtlsList.dtls.get(0).value)) {
          preferredLanguage = BDMConstants.kClientPreferredLanguageEnglish;
        } else if (BDMLANGUAGE.FRENCHL
          .equalsIgnoreCase(dataAttributeDtlsList.dtls.get(0).value)) {
          preferredLanguage = BDMConstants.kClientPreferredLanguageFrench;
        }
      }
    }
    return preferredLanguage;
  }

  /**
   * This method creates a task when a communication item of status SENT
   * is modified to VOID, RETURNED or MISDIRECTED.
   *
   * @param communicationID
   * @throws AppException
   * @throws InformationalException
   */
  public void createStatusChangeTask(
    final ConcernRoleCommunicationDtls commDtls,
    final BDMConcernRoleCommunicationDtls bdmCrDtls)
    throws AppException, InformationalException {

    // Confirm if a task exists for this communicationID

    final BizObjAssociation bizObjAssociationObj =
      BizObjAssociationFactory.newInstance();

    final BizObjAssocCountOpenTasksKey openTasksKey =
      new BizObjAssocCountOpenTasksKey();
    openTasksKey.bizObjectID = commDtls.communicationID;
    openTasksKey.bizObjectType = BUSINESSOBJECTTYPE.BDMCOMMUNICATION;
    openTasksKey.closedTaskStatusToIgnore = TASKSTATUS.CLOSED;
    openTasksKey.completedTaskStatusToIgnore = TASKSTATUS.COMPLETED;
    final TaskCount taskCount =
      bizObjAssociationObj.countOpenTasksByBizObjectTypeAndID(openTasksKey);
    // If there's a task already for this communication, return
    if (curam.core.impl.CuramConst.gkZero < taskCount.count) {
      return;
    }

    long workQueueID = 0;
    // search For the workqueue for the assignment if there is a case
    if (curam.core.impl.CuramConst.gkZero != commDtls.caseID) {
      // Get the foreign country code.
      final BDMFECaseKey feCaseKey = new BDMFECaseKey();
      feCaseKey.caseID = commDtls.caseID;
      final NotFoundIndicator nfIndicator = new NotFoundIndicator();
      final BDMFECaseDtls feCaseDtls =
        BDMFECaseFactory.newInstance().read(nfIndicator, feCaseKey);
      if (!nfIndicator.isNotFound()) {
        final BDMFEWorkQueueCountryLinkKey bdmFEWorkQueueCountryLinkKey =
          new BDMFEWorkQueueCountryLinkKey();
        bdmFEWorkQueueCountryLinkKey.countryCode = feCaseDtls.countryCode;
        final BDMFEWorkQueueCountryLinkDtls countryLinkDtls =
          BDMFEWorkQueueCountryLinkFactory.newInstance().read(nfIndicator,
            bdmFEWorkQueueCountryLinkKey);
        if (!nfIndicator.isNotFound()) {
          workQueueID = countryLinkDtls.workQueueID;
        } else {
          workQueueID = Long.parseLong(BDMConstants.kBDMCoreWorkQueueID);
        }
      }
    } else {
      workQueueID = Long.parseLong(BDMConstants.kBDMCoreWorkQueueID);
    }

    String taskDefinitionID = "";

    final ConcernRole crObj = ConcernRoleFactory.newInstance();
    final ConcernRoleKey crKey = new ConcernRoleKey();
    crKey.concernRoleID = commDtls.correspondentConcernRoleID;
    final ConcernRoleDtls crDtls = crObj.read(crKey);
    LocalisableString ls = null;
    if (curam.core.impl.CuramConst.gkZero != commDtls.caseID) {
      ls = new LocalisableString(BDMBPOCCT.INF_STATUS_CHANGED_SUBJECT_CASE);
      ls.arg(bdmCrDtls.trackingNumber);

      final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
      caseHeaderKey.caseID = commDtls.caseID;
      final NotFoundIndicator nfIndicator = new NotFoundIndicator();
      final CaseHeaderDtls caseHeaderDtls =
        CaseHeaderFactory.newInstance().read(nfIndicator, caseHeaderKey);
      if (!nfIndicator.isNotFound()) {

        ls.arg(CodeTable.getOneItem(CASETYPECODE.TABLENAME,
          caseHeaderDtls.caseTypeCode));
        ls.arg(caseHeaderDtls.caseReference);
        taskDefinitionID =
          BDMConstants.kCommunicationStatusTaskDefinitionCase;
      }
    } else {
      ls = new LocalisableString(BDMBPOCCT.INF_STATUS_CHANGED_SUBJECT_PERSON);
      ls.arg(bdmCrDtls.trackingNumber);
      ls.arg(crDtls.concernRoleName);
      ls.arg(crDtls.primaryAlternateID);

      taskDefinitionID =
        BDMConstants.kCommunicationStatusTaskDefinitionParticipant;
    }

    final List<Object> enactmentStructs = new ArrayList<>();

    final TaskCreateDetails taskCreateDetails = new TaskCreateDetails();

    taskCreateDetails.assignedTo = workQueueID + "";
    taskCreateDetails.assigneeType = TARGETITEMTYPE.WORKQUEUE;
    taskCreateDetails.caseID = commDtls.caseID;
    taskCreateDetails.comments = ls.getMessage();
    taskCreateDetails.deadlineDateTime =
      DateTime.getCurrentDateTime().addTime(90 * 24, 0, 0);
    taskCreateDetails.participantRoleID = commDtls.correspondentConcernRoleID;
    taskCreateDetails.participantType = crDtls.concernRoleType;
    taskCreateDetails.priority = TASKPRIORITY.DEFAULTCODE;
    taskCreateDetails.subject = ls.getMessage();

    enactmentStructs.add(taskCreateDetails);

    final DeadlineDuration deadlineDuration = new DeadlineDuration();

    final DateTimeInSecondsKey dateTimeInSecondsKey =
      new DateTimeInSecondsKey();

    dateTimeInSecondsKey.dateTime = taskCreateDetails.deadlineDateTime;

    final TaskManagementUtility taskManagementUtilityObj =
      TaskManagementUtilityFactory.newInstance();

    deadlineDuration.deadlineDuration = taskManagementUtilityObj
      .convertDateTimeToSeconds(dateTimeInSecondsKey).seconds;
    enactmentStructs.add(deadlineDuration);

    final ConcernRoleCommunicationKey concernRoleCommunicationKey =
      new ConcernRoleCommunicationKey();
    concernRoleCommunicationKey.communicationID = commDtls.communicationID;
    enactmentStructs.add(concernRoleCommunicationKey);

    EnactmentService.startProcessInV3CompatibilityMode(taskDefinitionID,
      enactmentStructs);

  }

  /**
   * This method closes a task if exists that's related to the given
   * communication
   *
   * @param communicationID
   * @throws AppException
   * @throws InformationalException
   */
  public void closeCommunicationStatusTask(final long communicationID,
    final long concernRoleID) throws AppException, InformationalException {

    final Event communicationStatusTask = new Event();
    communicationStatusTask.eventKey.eventClass =
      BDMCOMMUNICATIONSTATUSTASK.BDMCOMMUNICATIONSTATUSTASK.eventClass;
    communicationStatusTask.eventKey.eventType =
      BDMCOMMUNICATIONSTATUSTASK.BDMCOMMUNICATIONSTATUSTASK.eventType;
    communicationStatusTask.primaryEventData = communicationID;
    EventService.raiseEvent(communicationStatusTask);
  }

}
