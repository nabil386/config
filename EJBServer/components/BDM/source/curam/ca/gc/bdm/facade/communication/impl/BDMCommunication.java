package curam.ca.gc.bdm.facade.communication.impl;

import com.google.inject.Inject;
import curam.attachment.impl.Attachment;
import curam.ca.gc.bdm.codetable.BDMPHONECOUNTRY;
import curam.ca.gc.bdm.communication.impl.BDMCommunicationHelper;
import curam.ca.gc.bdm.communication.impl.BDMCommunicationImpl;
import curam.ca.gc.bdm.communication.impl.BDMMSWordDocuments;
import curam.ca.gc.bdm.entity.bdmcct.fact.BDMCCTTemplateFactory;
import curam.ca.gc.bdm.entity.bdmcct.struct.BDMCCTTemplateKey;
import curam.ca.gc.bdm.entity.communication.fact.BDMCommStatusHistoryFactory;
import curam.ca.gc.bdm.entity.communication.fact.BDMConcernRoleCommunicationFactory;
import curam.ca.gc.bdm.entity.communication.intf.BDMConcernRoleCommunication;
import curam.ca.gc.bdm.entity.communication.struct.BDMCommStatusHistoryDtls;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationDtls;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationKey;
import curam.ca.gc.bdm.facade.communication.struct.BDMCancelCommunicationDetails;
import curam.ca.gc.bdm.facade.communication.struct.BDMCategorySubCategoryList;
import curam.ca.gc.bdm.facade.communication.struct.BDMCommunicationKey;
import curam.ca.gc.bdm.facade.communication.struct.BDMConcernRoleIDEvidenceIDAndNameDetailsList;
import curam.ca.gc.bdm.facade.communication.struct.BDMCorrespondenceDetails;
import curam.ca.gc.bdm.facade.communication.struct.BDMCorrespondenceWizardKey;
import curam.ca.gc.bdm.facade.communication.struct.BDMFileNameAndDataDtls;
import curam.ca.gc.bdm.facade.communication.struct.BDMListProFormaTemplateByTypeAndParticipantKey;
import curam.ca.gc.bdm.facade.communication.struct.BDMListProFormaTemplateByTypeAndParticpant;
import curam.ca.gc.bdm.facade.communication.struct.BDMModifyRecordedCommDetails;
import curam.ca.gc.bdm.facade.communication.struct.BDMReadMSWordCommunicationCaseMember;
import curam.ca.gc.bdm.facade.communication.struct.BDMReadPreferredLanguage;
import curam.ca.gc.bdm.facade.communication.struct.BDMReadProFormaCommunicationCaseMember;
import curam.ca.gc.bdm.facade.communication.struct.BDMRecordCommunicationDetails;
import curam.ca.gc.bdm.facade.communication.struct.BDMSearchMSWordTemplateDetails;
import curam.ca.gc.bdm.facade.communication.struct.BDMTaskName;
import curam.ca.gc.bdm.facade.communication.struct.BDMTemplateDetails;
import curam.ca.gc.bdm.facade.communication.struct.BDMTemplateSearchCriteria;
import curam.ca.gc.bdm.facade.communication.struct.BDMTemplateSearchDetails;
import curam.ca.gc.bdm.facade.communication.struct.BDMTemplateSearchDetailsList;
import curam.ca.gc.bdm.facade.communication.struct.BDMTemplateSearchKey;
import curam.ca.gc.bdm.facade.communication.struct.BDMViewCorrespondenceDetails;
import curam.ca.gc.bdm.facade.communication.struct.BDMWorkQueueID;
import curam.ca.gc.bdm.facade.fec.struct.BDMFileLocationURL;
import curam.ca.gc.bdm.facade.participant.fact.BDMParticipantFactory;
import curam.ca.gc.bdm.facade.participant.struct.BDMCommunicationAndListRowActionDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMReadParticipantAddressDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMBPOCCT;
import curam.ca.gc.bdm.message.BDMCORRESPONDENCE;
import curam.ca.gc.bdm.message.BDMCORRESPONDENCETASKDATA;
import curam.ca.gc.bdm.message.BDMPERSON;
import curam.ca.gc.bdm.sl.attachment.fact.BDMMaintainXSLTemplateFactory;
import curam.ca.gc.bdm.sl.attachment.intf.BDMMaintainXSLTemplate;
import curam.ca.gc.bdm.sl.commstatushistory.impl.BDMCommStatusHistoryDAO;
import curam.ca.gc.bdm.sl.communication.fact.BDMCommunicationFactory;
import curam.ca.gc.bdm.sl.communication.struct.BDMCommunicationStatusDetails;
import curam.ca.gc.bdm.sl.communication.struct.BDMSearchTemplateResult;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.BDMCCTOutboundInterfaceImpl;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTCancelPrintRequest;
import curam.ca.gc.bdm.util.rest.impl.BDMRestResponse;
import curam.codetable.ATTACHMENTSTATUS;
import curam.codetable.BDMCCTCOMMUNICATIONSTATUS;
import curam.codetable.BDMCOMMCANCELREASONCODE;
import curam.codetable.CASETYPECODE;
import curam.codetable.COMMUNICATIONDIRECTION;
import curam.codetable.COMMUNICATIONFORMAT;
import curam.codetable.COMMUNICATIONSTATUS;
import curam.codetable.COMMUNICATIONTYPE;
import curam.codetable.CONCERNROLETYPE;
import curam.codetable.DOCUMENTCATEGORYCODE;
import curam.codetable.DOCUMENTTYPE;
import curam.codetable.RECORDSTATUS;
import curam.codetable.TARGETITEMTYPE;
import curam.codetable.TASKPRIORITY;
import curam.codetable.TEMPLATEIDCODE;
import curam.core.facade.fact.CommunicationFactory;
import curam.core.facade.fact.SystemFactory;
import curam.core.facade.intf.Communication;
import curam.core.facade.struct.CancelCommunicationDetails;
import curam.core.facade.struct.CodeDetails;
import curam.core.facade.struct.ConcernRoleKeyStruct;
import curam.core.facade.struct.CreateEmailCommDetails;
import curam.core.facade.struct.CreateProFormaCommDetails1;
import curam.core.facade.struct.FileNameAndDataDtls;
import curam.core.facade.struct.ListCodeDetails;
import curam.core.facade.struct.ListProFormaTemplateByTypeAndParticpant;
import curam.core.facade.struct.ListSelectedFieldsDocumentTemplateDetails;
import curam.core.facade.struct.ModifyMSWordCommunicationDetails1;
import curam.core.facade.struct.ModifyProFormaCommDetails1;
import curam.core.facade.struct.ModifyRecordedCommKey;
import curam.core.facade.struct.ReadMSWordCommunicationKey;
import curam.core.facade.struct.ReadParticipantAddressListKey;
import curam.core.facade.struct.ReadProFormaCommKey;
import curam.core.facade.struct.ReadRecordedCommDetails1;
import curam.core.facade.struct.ReadRecordedCommKey;
import curam.core.facade.struct.ReadRecordedCommunicationCaseMember;
import curam.core.facade.struct.SearchByRelatedIDKey;
import curam.core.facade.struct.StandardManualTaskDtls;
import curam.core.facade.struct.TemplateAndDocumentDataKey;
import curam.core.facade.struct.WizardDetails;
import curam.core.facade.struct.WizardStateID;
import curam.core.facade.struct.WordTemplateDocumentAndDataDetails;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.CommAttachmentLinkFactory;
import curam.core.fact.ConcernRoleCommunicationFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.fact.UsersFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.intf.CaseHeader;
import curam.core.intf.CommAttachmentLink;
import curam.core.intf.ConcernRole;
import curam.core.intf.ConcernRoleCommunication;
import curam.core.sl.entity.fact.DocumentTemplateFactory;
import curam.core.sl.entity.struct.SearchByDocumentCategory;
import curam.core.sl.entity.struct.SelectedFieldsDocumentTemplateDetails;
import curam.core.sl.entity.struct.SelectedFieldsDocumentTemplateDetailsList;
import curam.core.sl.fact.LanguageLocaleMapFactory;
import curam.core.sl.fact.WorkAllocationTaskFactory;
import curam.core.sl.infrastructure.impl.ValidationManagerFactory;
import curam.core.sl.intf.LanguageLocaleMap;
import curam.core.sl.intf.WorkAllocationTask;
import curam.core.sl.struct.CommunicationIDKey;
import curam.core.sl.struct.LanguageLocaleDetails;
import curam.core.sl.struct.LanguageLocaleMapKey;
import curam.core.sl.struct.MSWordCommunicationDetails1;
import curam.core.sl.struct.PreviewProFormaKey;
import curam.core.sl.struct.ProFormaCommDetails1;
import curam.core.sl.struct.ProFormaCommKey;
import curam.core.sl.struct.ProFormaReturnDocDetails;
import curam.core.sl.struct.RecordedCommDetails1;
import curam.core.struct.AttachmentDtls;
import curam.core.struct.AttachmentKey;
import curam.core.struct.CaseKey;
import curam.core.struct.CaseTypeCode;
import curam.core.struct.CommAttachmentLinkDtls;
import curam.core.struct.CommAttachmentLinkDtlsList;
import curam.core.struct.CommunicationAttachmentLinkReadmultiKey;
import curam.core.struct.ConcernRoleCommKeyOut;
import curam.core.struct.ConcernRoleCommunicationDetails;
import curam.core.struct.ConcernRoleCommunicationDtls;
import curam.core.struct.ConcernRoleCommunicationKey;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.ConcernRoleNameTypeDateDetails;
import curam.core.struct.ConcernRoleTypeDetails;
import curam.core.struct.SearchTemplatesByConcernAndTypeResult;
import curam.core.struct.SearchTemplatesKey;
import curam.core.struct.UsersDtls;
import curam.core.struct.UsersKey;
import curam.core.struct.XSLTemplateDetails;
import curam.creole.value.CodeTableItem;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.message.BPOCOMMUNICATION;
import curam.struct.ConcernRoleIDCaseIDKey;
import curam.util.administration.fact.CodeTableAdminFactory;
import curam.util.administration.intf.CodeTableAdmin;
import curam.util.administration.struct.CodeTableAdminListAllItemsAndDefaultOut;
import curam.util.administration.struct.CodeTableItemDetails;
import curam.util.administration.struct.CodeTableItemUniqueKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement;
import curam.util.exception.InformationalElement.InformationalType;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.exception.LocalisableString;
import curam.util.exception.RecordNotFoundException;
import curam.util.internal.xml.fact.XSLTemplateFactory;
import curam.util.internal.xml.intf.XSLTemplate;
import curam.util.internal.xml.struct.XSLTemplateDtls;
import curam.util.internal.xml.struct.XSLTemplateKey;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.NotFoundIndicator;
import curam.util.type.UniqueID;
import curam.util.xml.fact.XSLTemplateUtilityFactory;
import curam.wizardpersistence.impl.WizardPersistentState;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.httpclient.HttpStatus;

public class BDMCommunication
  extends curam.ca.gc.bdm.facade.communication.base.BDMCommunication {

  final BDMCommunicationHelper bdmCommHelper = new BDMCommunicationHelper();

  public BDMCommunication() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Inject
  protected Attachment attachment;

  @Inject
  protected BDMCommunicationImpl bdmCommunicationImpl;

  @Inject
  BDMCommStatusHistoryDAO bdmCommStatusHistoryDAO;

  @Inject
  private BDMMSWordDocuments wordDocuments;

  @Override
  public BDMReadProFormaCommunicationCaseMember
    readProFormaAndCaseMember(final ReadProFormaCommKey key)
      throws AppException, InformationalException {

    final BDMReadProFormaCommunicationCaseMember proformaDetails =
      new BDMReadProFormaCommunicationCaseMember();

    proformaDetails.dtls =
      CommunicationFactory.newInstance().readProFormaAndCaseMember(key);
    if (StringUtil.isNullOrEmpty(
      proformaDetails.dtls.communicationDtls.readProFormaCommDetails.localeIdentifier)) {
      String languageCode = "";
      try {
        final CodeTableItem preferredLangCodeItem =
          bdmCommHelper.getPreferredWrittenLanguageCTI(
            proformaDetails.dtls.communicationDtls.readProFormaCommDetails.correspondentParticipantRoleID);
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
      proformaDetails.dtls.communicationDtls.readProFormaCommDetails.localeIdentifier =
        languageLocaleDtls.localeIdentifier;
    }
    final XSLTemplateKey xslTemplateKey = new XSLTemplateKey();
    xslTemplateKey.templateID =
      proformaDetails.dtls.communicationDtls.readProFormaCommDetails.proFormaID;
    xslTemplateKey.localeIdentifier =
      proformaDetails.dtls.communicationDtls.readProFormaCommDetails.localeIdentifier;

    final XSLTemplateDtls xslTemplateDtls =
      XSLTemplateFactory.newInstance().read(xslTemplateKey);
    final CodeTableItemDetails ctiDetails =
      bdmCommHelper.readCodeTableItem(xslTemplateDtls.templateIDCode,
        TEMPLATEIDCODE.TABLENAME, xslTemplateDtls.localeIdentifier);

    proformaDetails.templateID = ctiDetails.description;
    final UsersKey userKey = new UsersKey();
    userKey.userName =
      proformaDetails.dtls.communicationDtls.readProFormaCommDetails.userName;
    final ConcernRoleCommunicationDetails commDetails =
      new ConcernRoleCommunicationDetails();
    commDetails
      .assign(proformaDetails.dtls.communicationDtls.readProFormaCommDetails);

    final UsersDtls usersObj = UsersFactory.newInstance().read(userKey);
    proformaDetails.userFullName = usersObj.fullName;

    if (proformaDetails.dtls.communicationDtls.readProFormaCommDetails.statusCode
      .equals(RECORDSTATUS.CANCELLED)) {
      proformaDetails.canceledInd = true;
      final BDMConcernRoleCommunicationKey bdmcrcKey =
        new BDMConcernRoleCommunicationKey();
      bdmcrcKey.communicationID = key.proFormaCommKey.communicationID;
      final BDMConcernRoleCommunicationDtls bdmCRCDtls =
        BDMConcernRoleCommunicationFactory.newInstance().read(bdmcrcKey);
      proformaDetails.cancelReason = bdmCRCDtls.cancelReason;
      return proformaDetails;
    }
    // START : TASK 8962 -- read delivery mode from contact preference when
    // communication is not sent --JP
    if (!commDetails.communicationStatus
      .equalsIgnoreCase(COMMUNICATIONSTATUS.SENT)) {

      proformaDetails.dtls.communicationDtls.readProFormaCommDetails.methodTypeCode =
        bdmCommHelper.calculateDeliveryModes(
          proformaDetails.dtls.communicationDtls.readProFormaCommDetails.correspondentParticipantRoleID);
    }
    // END : TASK 8962

    if (proformaDetails.dtls.communicationDtls.readProFormaCommDetails.addressID == 0l) {

      final ReadParticipantAddressListKey participantKey =
        new ReadParticipantAddressListKey();
      participantKey.maintainAddressKey.concernRoleID =
        proformaDetails.dtls.communicationDtls.readProFormaCommDetails.correspondentParticipantRoleID;

      final BDMReadParticipantAddressDetails addressDetails =
        BDMParticipantFactory.newInstance()
          .readMailingAddress(participantKey);
      proformaDetails.dtls.communicationDtls.readProFormaCommDetails.addressID =
        addressDetails.addressID;
      proformaDetails.dtls.communicationDtls.readProFormaCommDetails.addressData =
        addressDetails.dtls.addressDetails.formattedAddressData;
    }

    return proformaDetails;
  }

  protected void validateCommunicationAddress(
    final ProFormaCommDetails1 proformaDtls) throws InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    if (proformaDtls.addressID == 0l) {
      final AppException e =
        new AppException(BPOCOMMUNICATION.ERR_COMM_ADDRESS_NOT_SUPPLIED);

      // Read the participant name and add to the exception message.
      e.arg(proformaDtls.correspondentName);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(e.arg(true), CuramConst.gkEmpty,
          InformationalElement.InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          2);
    }

    informationalManager.failOperation();
  }

  @Override
  public void cancelCommunication(final BDMCancelCommunicationDetails details)
    throws AppException, InformationalException {

    // if the communication format is Correspondence, call CCT to cancel.

    // Get the communication format by querying concernRoleCommunication
    final ConcernRoleCommunicationKey communicationKey =
      new ConcernRoleCommunicationKey();
    communicationKey.communicationID =
      details.dtls.details.key.communicationID;

    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    final ConcernRoleCommunication concernRoleCommObj =
      ConcernRoleCommunicationFactory.newInstance();
    ConcernRoleCommunicationDtls commDtls =
      concernRoleCommObj.read(nfIndicator, communicationKey);

    if (!nfIndicator.isNotFound()) {
      // Call OOTB API in any case. This checks the security
      CancelCommunicationDetails cancelCommDetails =
        new CancelCommunicationDetails();
      cancelCommDetails = details.dtls;
      final Communication commObj = CommunicationFactory.newInstance();
      commObj.cancel(cancelCommDetails);

      if (COMMUNICATIONFORMAT.CORRESPONDENCE
        .equalsIgnoreCase(commDtls.communicationFormat)) {

        // BUG-93094, Start
        // If reason is other, comments must be entered.
        if (BDMCOMMCANCELREASONCODE.OTHER
          .equalsIgnoreCase(details.cancelReasonCode)
          && StringUtil.isNullOrEmpty(details.cancelReason)) {
          throw new AppException(BDMBPOCCT.ERR_COMMENTS_MANDATORY);
        }
        // BUG-93094, End

        // If communication format is CORRESPONDENCE,
        // call the outbound API to cancel
        final BDMConcernRoleCommunicationKey bdmCommKey =
          new BDMConcernRoleCommunicationKey();
        bdmCommKey.communicationID = details.dtls.details.key.communicationID;
        BDMConcernRoleCommunicationDtls bdmCRCommDtls =
          new BDMConcernRoleCommunicationDtls();

        final BDMConcernRoleCommunication bdmCommunicationObj =
          BDMConcernRoleCommunicationFactory.newInstance();
        bdmCRCommDtls = bdmCommunicationObj.read(nfIndicator, bdmCommKey);
        if (!nfIndicator.isNotFound()) {
          final BDMCCTCancelPrintRequest cancelPrintRequest =
            new BDMCCTCancelPrintRequest();
          cancelPrintRequest.setWorkItemID(bdmCRCommDtls.workItemID);
          cancelPrintRequest.setCommunity(
            Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
          cancelPrintRequest.setUserID(
            Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
          final BDMCCTOutboundInterfaceImpl interfaceObj =
            new BDMCCTOutboundInterfaceImpl();
          // BUG-92561, Start
          // Read the response and update communication Status too
          // BUG-97801, Start
          // Display appropriate error message to user after logging.
          BDMRestResponse response = null;
          try {
            response = interfaceObj.cancelPrint(cancelPrintRequest);
          } catch (final Exception e) {
            e.printStackTrace();
            throw new AppException(BDMBPOCCT.ERR_DOC_CANNOT_BE_CANCELLED);
          }
          // BUG-97801, End
          if (null != response
            && HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {

            // Update ConcernRoleCommunicationDtls as well
            // Need to read again since the cancel operation changes
            // the version no
            commDtls = concernRoleCommObj.read(nfIndicator, communicationKey);
            if (!nfIndicator.isNotFound()) {
              commDtls.communicationStatus = COMMUNICATIONSTATUS.CANCELLED;
              concernRoleCommObj.modify(communicationKey, commDtls);
            }
            // BUG-92561, End

            // Insert the cancelled status in to History
            final BDMCommStatusHistoryDtls historyDtls =
              new BDMCommStatusHistoryDtls();
            historyDtls.BDMCommStatusHistoryID = UniqueID.nextUniqueID();
            historyDtls.communicationID = commDtls.communicationID;
            historyDtls.recordStatus = RECORDSTATUS.CANCELLED;
            historyDtls.statusCode = commDtls.communicationStatus;
            historyDtls.statusDateTime = DateTime.getCurrentDateTime();
            BDMCommStatusHistoryFactory.newInstance().insert(historyDtls);

            bdmCRCommDtls.communicationID =
              details.dtls.details.key.communicationID;
            bdmCRCommDtls.cancelReason = details.cancelReason;
            // BUG-92810, Start
            // Store drop down value as well.
            bdmCRCommDtls.cancelReasonCode = details.cancelReasonCode;
            // BUG-92104, Start
            bdmCRCommDtls.cctStatus = BDMCCTCOMMUNICATIONSTATUS.CANCELED;
            // BUG-92104, End
            // BUG-92810, End
            bdmCommunicationObj.modify(bdmCommKey, bdmCRCommDtls);
          }
        }
      }
    }

  }

  /***
   * This method returns refined list of xsl templates returned by OOTB
   * listTemplateByTypeAndParticipant method.
   * Xsltemplates list is refined based on user selected language locale and
   * name of the template
   *
   * @param listProFormaTemplateByTypeAndParticipantKey - search criteria
   * @return BDMListProFormaTemplateByTypeAndParticpant list of xsltemplates
   * based on search criteria
   */

  @Override
  public BDMListProFormaTemplateByTypeAndParticpant
    listTemplateByTypeAndParticipant(
      final BDMListProFormaTemplateByTypeAndParticipantKey listProFormaTemplateByTypeAndParticipantKey)
      throws AppException, InformationalException {

    final BDMListProFormaTemplateByTypeAndParticpant returnStrut =
      new BDMListProFormaTemplateByTypeAndParticpant();
    final Communication commObj = CommunicationFactory.newInstance();
    final BDMMaintainXSLTemplate bdmMaintainXSLTemplateObj =
      BDMMaintainXSLTemplateFactory.newInstance();

    ListProFormaTemplateByTypeAndParticpant tempList =
      new ListProFormaTemplateByTypeAndParticpant();

    final SearchTemplatesKey searchTemplatesKey = new SearchTemplatesKey();
    // Set key details to retrieve the list of templates
    searchTemplatesKey.concernRoleID =
      listProFormaTemplateByTypeAndParticipantKey.dtls.participantRoleID;
    searchTemplatesKey.templateType =
      listProFormaTemplateByTypeAndParticipantKey.dtls.templateType;
    searchTemplatesKey.caseID =
      listProFormaTemplateByTypeAndParticipantKey.dtls.caseID;
    final SearchTemplatesByConcernAndTypeResult tempList2 =
      bdmMaintainXSLTemplateObj
        .searchTemplatesByConcernAndType(searchTemplatesKey);

    XSLTemplateUtilityFactory.newInstance();
    final XSLTemplate xslTemplateObj = XSLTemplateFactory.newInstance();

    final Map<String, ListProFormaTemplateByTypeAndParticpant> proformaListMap =
      new HashMap<String, ListProFormaTemplateByTypeAndParticpant>();
    if (!StringUtil.isNullOrEmpty(
      listProFormaTemplateByTypeAndParticipantKey.dtls.templateType)) {
      tempList = commObj.listTemplateByTypeAndParticipant(
        listProFormaTemplateByTypeAndParticipantKey.dtls);
      tempList.searchTemplatesByConcernAndTypeResult.xslTemplateDetailsListOut.dtls
        .addAll(tempList2.xslTemplateDetailsListOut.dtls);

      proformaListMap.put(
        listProFormaTemplateByTypeAndParticipantKey.dtls.templateType,
        tempList);

    } else {
      final CodeTableAdminListAllItemsAndDefaultOut ctOutput =
        CodeTableAdminFactory.newInstance()
          .listEnabledItems(COMMUNICATIONTYPE.TABLENAME);
      for (final CodeTableItemDetails ctItem : ctOutput.dtls.items()) {
        listProFormaTemplateByTypeAndParticipantKey.dtls.templateType =
          ctItem.code;
        final ListProFormaTemplateByTypeAndParticpant proformaTypeList =
          commObj.listTemplateByTypeAndParticipant(
            listProFormaTemplateByTypeAndParticipantKey.dtls);
        proformaListMap.put(ctItem.code, proformaTypeList);
      }

    }

    for (final Object proformaType : proformaListMap.keySet().toArray()) {
      // proformaListMap.get(proformaType);
      for (final XSLTemplateDetails xslTemplateDetails : proformaListMap.get(
        proformaType).searchTemplatesByConcernAndTypeResult.xslTemplateDetailsListOut.dtls
          .items()) {

        final XSLTemplateKey xslTemplateKey = new XSLTemplateKey();
        xslTemplateKey.templateID = xslTemplateDetails.templateID;
        xslTemplateKey.localeIdentifier = xslTemplateDetails.localeIdentifier;
        final XSLTemplateDtls xslTemplateDtls =
          xslTemplateObj.read(xslTemplateKey);
        if (!StringUtil.isNullOrEmpty(
          listProFormaTemplateByTypeAndParticipantKey.language)) {

          if (!bdmCommHelper
            .getLocaleFromLanguage(
              listProFormaTemplateByTypeAndParticipantKey.language)
            .equals(xslTemplateDetails.localeIdentifier)) {
            continue;
          }
        }

        final CodeTableItemDetails ctiDetails =
          bdmCommHelper.readCodeTableItem(xslTemplateDtls.templateIDCode,
            TEMPLATEIDCODE.TABLENAME, xslTemplateDtls.localeIdentifier);

        if (StringUtil.isNullOrEmpty(xslTemplateDtls.templateIDCode)
          || !StringUtil.isNullOrEmpty(
            listProFormaTemplateByTypeAndParticipantKey.templateIDCode)
            && !ctiDetails.description.contains(
              listProFormaTemplateByTypeAndParticipantKey.templateIDCode)) {
          // xslTemplateList.dtls.remove(xslTemplateDtls);
          continue;
        }

        if (!StringUtil.isNullOrEmpty(
          listProFormaTemplateByTypeAndParticipantKey.templateName)
          && !xslTemplateDtls.templateName.contains(
            listProFormaTemplateByTypeAndParticipantKey.templateName)) {
          // xslTemplateList.dtls.remove(xslTemplateDtls);
          continue;
        }
        final BDMSearchTemplateResult result = new BDMSearchTemplateResult();
        result.assign(xslTemplateDetails);
        result.templateID = xslTemplateDetails.templateID;
        result.templateIDCode = ctiDetails.description;
        result.type = (String) proformaType;
        result.localeIdentifier = xslTemplateDetails.localeIdentifier;
        returnStrut.dtls.dtls.add(result);
      }
    }
    return returnStrut;
  }

  @Override
  public BDMFileNameAndDataDtls
    printCommunication(final CommunicationIDKey communicationIDKey)
      throws AppException, InformationalException {

    BDMFileNameAndDataDtls bdmfileNameAndDataDtls =
      new BDMFileNameAndDataDtls();
    FileNameAndDataDtls fileNameAndDataDtls = new FileNameAndDataDtls();

    final ConcernRoleCommunication concernRoleCommunicationObj =
      ConcernRoleCommunicationFactory.newInstance();
    final ConcernRoleCommunicationKey key = new ConcernRoleCommunicationKey();

    key.communicationID = communicationIDKey.communicationID;
    final ConcernRoleCommunicationDtls communicationDetails =
      concernRoleCommunicationObj.read(key);

    if (communicationDetails.communicationStatus
      .equals(COMMUNICATIONSTATUS.DRAFT)) {

      if (COMMUNICATIONFORMAT.PROFORMA
        .equals(communicationDetails.communicationFormat)) {

        final PreviewProFormaKey previewProFormaKey =
          new PreviewProFormaKey();

        previewProFormaKey.communicationID =
          communicationIDKey.communicationID;
        previewProFormaKey.localeIdentifier =
          communicationDetails.localeIdentifier;
        final ProFormaReturnDocDetails proformaDetails =
          bdmCommunicationImpl.previewProForma(previewProFormaKey);

        bdmfileNameAndDataDtls.fileContent = proformaDetails.fileDate;
        bdmfileNameAndDataDtls.fileName = proformaDetails.fileName;

      } else if (COMMUNICATIONFORMAT.MSWORD
        .equals(communicationDetails.communicationFormat)) {

        final CommAttachmentLink commAttachmentLinkObj =
          CommAttachmentLinkFactory.newInstance();
        final CommunicationAttachmentLinkReadmultiKey rmkey =
          new CommunicationAttachmentLinkReadmultiKey();
        rmkey.communicationID = communicationIDKey.communicationID;

        fileNameAndDataDtls =
          bdmCommunicationImpl.previewMSWord(communicationIDKey);
        bdmfileNameAndDataDtls.fileName = fileNameAndDataDtls.fileName;
        bdmfileNameAndDataDtls.fileContent = fileNameAndDataDtls.fileContent;
      }
    } else {

      final CommAttachmentLinkDtlsList commAttLnkList = bdmCommHelper
        .getCommAttachmentLinkList(communicationIDKey.communicationID);
      for (final CommAttachmentLinkDtls commAttLnk : commAttLnkList.dtls
        .items()) {
        final AttachmentKey attKey = new AttachmentKey();
        attKey.attachmentID = commAttLnk.attachmentID;
        final AttachmentDtls attDtls = attachment.read(attKey);
        if (attDtls.attachmentStatus.equals(ATTACHMENTSTATUS.ACTIVE)) {
          if (COMMUNICATIONFORMAT.MSWORD
            .equals(communicationDetails.communicationFormat)
            || attDtls.documentType.equals(DOCUMENTTYPE.LETTER)) {
            bdmfileNameAndDataDtls.fileContent = attDtls.attachmentContents;
            bdmfileNameAndDataDtls.fileName = attDtls.attachmentName;
          }
        }
      }
    }

    // call the interface layer if the communication is correspondence
    if (COMMUNICATIONFORMAT.CORRESPONDENCE
      .equals(communicationDetails.communicationFormat)) {

      bdmfileNameAndDataDtls = BDMCommunicationFactory.newInstance()
        .downloadCorrespondence(communicationIDKey);

    }

    return bdmfileNameAndDataDtls;
  }

  @Override
  public ListSelectedFieldsDocumentTemplateDetails

    listMSWordDocumentType(final BDMSearchMSWordTemplateDetails key)
      throws AppException, InformationalException {

    final ListSelectedFieldsDocumentTemplateDetails typeList =
      new ListSelectedFieldsDocumentTemplateDetails();
    final SearchByRelatedIDKey relatedKey = new SearchByRelatedIDKey();
    relatedKey.SearchByRelatedIDKey.concernRoleID =
      key.relatedIDKey.SearchByRelatedIDKey.concernRoleID;
    relatedKey.SearchByRelatedIDKey.caseID =
      key.relatedIDKey.SearchByRelatedIDKey.caseID;
    final ListSelectedFieldsDocumentTemplateDetails docTempList =
      SystemFactory.newInstance()
        .listDocumentTemplatesByRelatedID(relatedKey);

    // -- START TASK 27007 - SIR letter for Application case - JP
    final SearchByDocumentCategory searchByApplicationCaseDocumentCategory =
      new SearchByDocumentCategory();

    SelectedFieldsDocumentTemplateDetailsList selectedFieldsDocumentTemplateDetailsList =
      new SelectedFieldsDocumentTemplateDetailsList();
    final curam.core.sl.entity.intf.DocumentTemplate documentTemplateObj =
      DocumentTemplateFactory.newInstance();

    if (key.relatedIDKey.SearchByRelatedIDKey.caseID != 0) {

      final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
      final CaseKey caseKey = new CaseKey();

      caseKey.caseID = key.relatedIDKey.SearchByRelatedIDKey.caseID;

      // IF case is Application case
      final CaseTypeCode caseTypeCode =
        caseHeaderObj.readCaseTypeCode(caseKey);
      if (caseTypeCode.caseTypeCode.equals(CASETYPECODE.APPLICATION_CASE)
        || caseTypeCode.caseTypeCode.equals(CASETYPECODE.PRODUCTDELIVERY)) {

        // category code be DC3
        searchByApplicationCaseDocumentCategory.categoryCode =
          DOCUMENTCATEGORYCODE.CASEANDPDC;
      }

      // select all templates with category application case
      selectedFieldsDocumentTemplateDetailsList = documentTemplateObj
        .searchByDocumentCategory(searchByApplicationCaseDocumentCategory);

      for (final SelectedFieldsDocumentTemplateDetails appCaseTemplate : selectedFieldsDocumentTemplateDetailsList.dtls) {

        docTempList.dtls.list.dtls.add(appCaseTemplate);
      }

    }
    // -- END TASK 27007 - SIR letter for Application case - JP
    final ConcernRoleKeyStruct keyStruct = new ConcernRoleKeyStruct();
    keyStruct.concernRoleID =
      key.relatedIDKey.SearchByRelatedIDKey.concernRoleID;

    for (final SelectedFieldsDocumentTemplateDetails docTemp : docTempList.dtls.list.dtls
      .items()) {

      if (!StringUtil.isNullOrEmpty(key.language)
        && !bdmCommHelper.getLocaleFromLanguage(key.language)
          .equals(docTemp.localeIdentifier)) {
        continue;
      }

      /*
       * if
       * (!StringUtil.isNullOrEmpty(getPreferredLanguageFromContactPreference(
       * keyStruct).preferredLanguage)
       * && !bdmCommHelper.getLocaleFromLanguage(key.language)
       * .equals(docTemp.localeIdentifier)) {
       * continue;
       * }
       */
      if (!StringUtil.isNullOrEmpty(key.templateDetails.documentTemplateID)
        && !docTemp.documentTemplateID
          .contains(key.templateDetails.documentTemplateID))
        continue;
      if (!StringUtil.isNullOrEmpty(key.templateDetails.name)
        && !docTemp.name.contains(key.templateDetails.name))
        continue;

      typeList.dtls.list.dtls.add(docTemp);
    }
    return typeList;

  }

  @Override
  public WordTemplateDocumentAndDataDetails

    getWordTemplateDocumentAndData(final TemplateAndDocumentDataKey key)
      throws AppException, InformationalException {

    // Create return object
    final WordTemplateDocumentAndDataDetails wordTemplateDocumentAndDataDetails =
      new WordTemplateDocumentAndDataDetails();

    curam.core.sl.fact.CommunicationFactory.newInstance();

    // Call service layer method to retrieve word template and document data
    wordTemplateDocumentAndDataDetails.dtls =
      bdmCommunicationImpl.getWordTemplateDocumentAndData(key.dtls);

    return wordTemplateDocumentAndDataDetails;
  }

  @Override
  public void modifyMSWordCommunication1(
    final ModifyMSWordCommunicationDetails1 details)
    throws AppException, InformationalException {

    final long tempaddressid = 1l;
    if (details.dtls.addressID == 0l) {
      details.dtls.addressID = tempaddressid;
    }

    final ConcernRoleCommunication crCommObj =
      ConcernRoleCommunicationFactory.newInstance();
    final ConcernRoleCommunicationKey crcKey =
      new ConcernRoleCommunicationKey();
    crcKey.communicationID = details.dtls.communicationID;
    final ConcernRoleCommunicationDtls crcDtls = crCommObj.read(crcKey);

    if (crcDtls.addressID == tempaddressid) {
      crcDtls.addressID = 0l;
      crCommObj.modify(crcKey, crcDtls);
    }
  }

  @Override
  public void modifyProForma1(
    final ModifyProFormaCommDetails1 modifyProFormaCommDetails)
    throws AppException, InformationalException {

    final long tempaddressid = 1l;
    if (modifyProFormaCommDetails.dtls.addressID == 0l) {
      modifyProFormaCommDetails.dtls.addressID = tempaddressid;
    }
    bdmCommunicationImpl.modifyProForma1(modifyProFormaCommDetails.dtls);

    final ConcernRoleCommunication crCommObj =
      ConcernRoleCommunicationFactory.newInstance();
    final ConcernRoleCommunicationKey crcKey =
      new ConcernRoleCommunicationKey();
    crcKey.communicationID = modifyProFormaCommDetails.dtls.communicationID;
    final ConcernRoleCommunicationDtls crcDtls = crCommObj.read(crcKey);
    if (crcDtls.addressID == tempaddressid) {
      crcDtls.addressID = 0l;
      crCommObj.modify(crcKey, crcDtls);
    }

  }

  @Override
  public void
    modifyCommunication(final ConcernRoleCommunicationDtls communicationDtls)
      throws AppException, InformationalException {

    validateModifyCommunication(communicationDtls);
    // create concern role communication instance

    final ConcernRoleCommunicationDtls crcDtls = bdmCommunicationImpl
      .readConcernRoleCommunicationByID(communicationDtls.communicationID);

    if (crcDtls.communicationFormat.equals(COMMUNICATIONFORMAT.PROFORMA)) {
      final ProFormaCommKey proformaKey = new ProFormaCommKey();
      proformaKey.communicationID = communicationDtls.communicationID;
      final ProFormaCommDetails1 rProformaCommDetails =
        bdmCommunicationImpl.readProForma1(proformaKey);
      // BEGIN Task 56106 submit communication -->
      if (communicationDtls.communicationStatus
        .equals(COMMUNICATIONSTATUS.SUBMITTED)) {

        try {
          bdmCommunicationImpl.submitProformaComm(rProformaCommDetails);
          rProformaCommDetails.communicationDate = Date.getCurrentDate();

        } catch (final Exception e) {
          ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
            new AppException(
              BDMCORRESPONDENCE.ERR_PROFORMA_COMMUNICATION_MISSING_REQUIRED_DATA),
            null, InformationalType.kError,
            curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine,
            0);

          TransactionInfo.getInformationalManager().failOperation();
        }
        // <!-- Task 56106 submit communication -->
        // BEGIN TASK 8949 - Resubmit
      } else if (communicationDtls.communicationStatus
        .equals(COMMUNICATIONSTATUS.RESUBMITTED)) {
        // If current communication status is not sent then you can not resubmit
        // the communication.
        this.validateResubmitCommunication(crcDtls);

        // 1: Supersed the previously submitted communication attachment.
        bdmCommHelper
          .supersedCommAttachments(communicationDtls.communicationID);

        // 2: Resubmit the communication.
        bdmCommunicationImpl.submitProformaComm(rProformaCommDetails);
        rProformaCommDetails.communicationDate = Date.getCurrentDate();
        // END TASK 8949 - Resubmit
      } else if (communicationDtls.communicationStatus
        .equals(COMMUNICATIONSTATUS.DRAFT)) {
        // logic for recall action
        rProformaCommDetails.communicationDate = Date.kZeroDate;
        recallProforma(rProformaCommDetails);
      } else if (communicationDtls.communicationStatus
        .equals(COMMUNICATIONSTATUS.VOID)) {
        // TASK 24567 : Refactoring
        bdmCommunicationImpl.voidCommunication(crcDtls);
      } else if (communicationDtls.communicationStatus
        .equals(COMMUNICATIONSTATUS.RETURNED)) {
        // TASK 24567 : Refactoring
        bdmCommunicationImpl.returnCommunication(crcDtls);
      } else if (communicationDtls.communicationStatus
        .equals(COMMUNICATIONSTATUS.MISDIRECTED)) {
        // TASK 24567 : Refactoring
        bdmCommunicationImpl.misdirectCommunication(crcDtls);
      }
      rProformaCommDetails.communicationID =
        communicationDtls.communicationID;
      rProformaCommDetails.communicationStatus =
        communicationDtls.communicationStatus;
      bdmCommHelper.updateProFormaCommunicationStatus(rProformaCommDetails);
    } else if (crcDtls.communicationFormat
      .equals(COMMUNICATIONFORMAT.MSWORD)) {

      final MSWordCommunicationDetails1 wordCommDetails =
        new MSWordCommunicationDetails1();
      wordCommDetails.communicationStatus =
        communicationDtls.communicationStatus;
      wordCommDetails.communicationDate = Date.getCurrentDate();
      wordCommDetails.communicationID = communicationDtls.communicationID;
      wordCommDetails.documentTemplateID = crcDtls.documentTemplateID;
      bdmCommHelper.updateMSWordCommunicationStatus(wordCommDetails);
      // BEGIN Task-17815
      if (communicationDtls.communicationStatus
        .equals(COMMUNICATIONSTATUS.SUBMITTED)) {
        // first call submit function to generate pdf
        bdmCommunicationImpl.submitMSWordComm(wordCommDetails);
        // second, update communication status once pdf attachment is created

        // END Task-17815
        // BEGIN Task 8949.
      } else if (communicationDtls.communicationStatus
        .equals(COMMUNICATIONSTATUS.RESUBMITTED)) {
        // If current communication status is not sent then you can not resubmit
        // the communication.

        bdmCommunicationImpl.resubmitMSWord(wordCommDetails);
        // END Task 8949.
      } else if (communicationDtls.communicationStatus
        .equals(COMMUNICATIONSTATUS.DRAFT)) {

        bdmCommunicationImpl.recallMSWord(wordCommDetails);
      } else if (communicationDtls.communicationStatus
        .equals(COMMUNICATIONSTATUS.VOID)) {

        // TASK 24567 : Refactoring
        bdmCommunicationImpl.voidCommunication(crcDtls);
      } else if (communicationDtls.communicationStatus
        .equals(COMMUNICATIONSTATUS.RETURNED)) {

        // TASK 24567 : Refactoring
        bdmCommunicationImpl.returnCommunication(crcDtls);

      } else if (communicationDtls.communicationStatus
        .equals(COMMUNICATIONSTATUS.MISDIRECTED)) {

        // TASK 24567 : Refactoring
        bdmCommunicationImpl.misdirectCommunication(crcDtls);

      }

    }

  }

  private void recallProforma(final ProFormaCommDetails1 rProformaCommDetails)
    throws AppException, InformationalException {

    bdmCommHelper.removeCommAttachment(rProformaCommDetails.communicationID);
    rProformaCommDetails.addressID = 0l;
    bdmCommHelper.modifyProformaNoAddressID(rProformaCommDetails);

  }

  private void validateModifyCommunication(
    final ConcernRoleCommunicationDtls communicationDtls)
    throws InformationalException {

    if (StringUtil.isNullOrEmpty(communicationDtls.communicationStatus)) {
      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(
          new AppException(
            curam.message.BPOCOMMUNICATION.ERR_COMM_SUBJECT_NOT_SUPPLIED),
          CuramConst.gkEmpty, InformationalElement.InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          2);
    }

  }

  private void validateResubmitCommunication(
    final ConcernRoleCommunicationDtls communicationDtls)
    throws AppException, InformationalException {

    if (!communicationDtls.communicationStatus
      .equals(COMMUNICATIONSTATUS.SENT)) {
      final AppException appException =
        new AppException(BDMCORRESPONDENCE.ERR_CONCERN_ROLE_COMM_RV_NOT_SENT);
      throw appException;
    }
  }

  @Override
  public BDMCommunicationAndListRowActionDetails
    getListRowActionMenuLabel(final CommunicationIDKey communicationIDKey)
      throws AppException, InformationalException {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void createProForma1(
    final CreateProFormaCommDetails1 createProFormaCommDetails)
    throws AppException, InformationalException {

    bdmCommunicationImpl
      .createProFormaReturningIDNoAddressID(createProFormaCommDetails.dtls);

  }

  @Override
  public BDMCancelCommunicationDetails
    viewCancelReason(final CommunicationIDKey commKey)
      throws AppException, InformationalException {

    final BDMCancelCommunicationDetails cancelReason =
      new BDMCancelCommunicationDetails();

    if (commKey.communicationID == 0l)
      return cancelReason;
    final BDMReadProFormaCommunicationCaseMember proformaDetails =
      new BDMReadProFormaCommunicationCaseMember();

    final ReadProFormaCommKey key = new ReadProFormaCommKey();
    key.proFormaCommKey.communicationID = commKey.communicationID;
    proformaDetails.dtls =
      CommunicationFactory.newInstance().readProFormaAndCaseMember(key);

    if (proformaDetails.dtls.communicationDtls.readProFormaCommDetails.statusCode
      .equals(RECORDSTATUS.CANCELLED)) {
      cancelReason.canceledInd = true;
      final BDMConcernRoleCommunicationKey bdmcrcKey =
        new BDMConcernRoleCommunicationKey();
      bdmcrcKey.communicationID = key.proFormaCommKey.communicationID;
      final BDMConcernRoleCommunicationDtls bdmCRCDtls =
        BDMConcernRoleCommunicationFactory.newInstance().read(bdmcrcKey);
      cancelReason.cancelReason = bdmCRCDtls.cancelReason;

    }
    return cancelReason;
  }

  // START : TASK 8567 Dev - Template Search for Pro Forma and MS Word: Default
  // language to client's preferred language of written communication
  /**
   * Get Preferred Written Language from Contact Preference and display when
   * creating new MsWord of Pro Forma Communication
   *
   * @param concernRoleID
   * @return BDMReadPreferredLanguage
   */
  @Override
  public BDMReadPreferredLanguage getPreferredLanguageFromContactPreference(
    final ConcernRoleKeyStruct concernRoleID)
    throws AppException, InformationalException {

    final BDMReadPreferredLanguage language = new BDMReadPreferredLanguage();
    CodeTableItem preferredLangCodeItem;

    try {
      preferredLangCodeItem = bdmCommHelper
        .getPreferredWrittenLanguageCTI(concernRoleID.concernRoleID);
      language.preferredLanguage = preferredLangCodeItem.code();

    } catch (final Exception e) {
      language.preferredLanguage = "";
    }

    return language;
  }
  // END : TASK 8567 Dev - Template Search for Pro Forma and MS Word: Default
  // language to client's preferred language of written communication

  // START : TASK 7103 Implement Void - Task Creation
  /**
   * Create a task when Communication Status is changed to VOID. Task has set
   * for a deadline for 2 days, with low priority.
   *
   * @param commKey CommunicationIDKey in order to get Communication ID and sub
   * information.
   * @param taskName String for task name
   * @param wqID Work queue ID
   */
  @Override
  public void assignTaskToVoidWorkQueue(
    final curam.core.struct.CommunicationIDKey commKey,
    final BDMTaskName taskName, final BDMWorkQueueID wqID)
    throws AppException, InformationalException {

    final ConcernRoleCommunication concernRoleCommunicationObj =
      ConcernRoleCommunicationFactory.newInstance();
    final ConcernRoleCommunicationKey key = new ConcernRoleCommunicationKey();

    key.communicationID = commKey.communicationID;
    final ConcernRoleCommunicationDtls communicationDetails =
      concernRoleCommunicationObj.read(key);

    final WorkAllocationTask workAllocationTaskObj =
      WorkAllocationTaskFactory.newInstance();
    final StandardManualTaskDtls taskKey = new StandardManualTaskDtls();
    taskKey.dtls.assignDtls.assignmentID = wqID.workQueueID;
    taskKey.dtls.assignDtls.assignType = TARGETITEMTYPE.WORKQUEUE;
    taskKey.dtls.concerningDtls.participantRoleID =
      communicationDetails.concernRoleID;
    taskKey.dtls.concerningDtls.caseID = communicationDetails.caseID;
    taskKey.dtls.taskDtls.subject = taskName.taskName;

    final ConcernRole crObj = ConcernRoleFactory.newInstance();
    final ConcernRoleKey crKey = new ConcernRoleKey();
    crKey.concernRoleID = communicationDetails.correspondentConcernRoleID;
    final ConcernRoleDtls crDtls = crObj.read(crKey);
    final AppException e = new AppException(
      BDMCORRESPONDENCETASKDATA.ERR_COMMUNICATION_FAILED_TASK_SUBJECT);

    e.arg(crDtls.concernRoleName);
    e.arg(crDtls.primaryAlternateID);
    taskKey.dtls.taskDtls.taskDefinitionID =
      BDMConstants.kCommunicationFailedTaskDefinition;
    taskKey.dtls.taskDtls.priority = TASKPRIORITY.LOW;
    taskKey.dtls.taskDtls.deadlineTime =
      DateTime.getCurrentDateTime().addTime(48, 0, 0);

    // Create task
    workAllocationTaskObj.createManualTask(taskKey);

  }

  // END : TASK 7103 Implement Void - Task Creation

  /**
   * Task-15603 MS Word Communication method
   */
  @Override
  public BDMReadMSWordCommunicationCaseMember
    readMSWordCommunicationAndCaseMember(final ReadMSWordCommunicationKey key)
      throws AppException, InformationalException {

    final Communication communicationObj = CommunicationFactory.newInstance();
    final BDMReadMSWordCommunicationCaseMember msWordCommunicationCaseMemberDetails =
      new BDMReadMSWordCommunicationCaseMember();
    msWordCommunicationCaseMemberDetails.dtls = communicationObj
      .readMSWordCommunicationAndCaseMember(key).communicationDtls;

    if (!msWordCommunicationCaseMemberDetails.dtls.dtls.communicationStatus
      .equalsIgnoreCase(COMMUNICATIONSTATUS.SENT)) {

      // read delivery mode / communication method type
      msWordCommunicationCaseMemberDetails.methodTypeCode =
        bdmCommHelper.calculateDeliveryModes(
          msWordCommunicationCaseMemberDetails.dtls.dtls.correspondentParticipantRoleID);
    }

    return msWordCommunicationCaseMemberDetails;
  }

  /**
   * Creates a recorded communication for the case.
   *
   * @param recordedCommDetails
   * Contains recorded communication details.
   *
   * @throws InformationalException
   * Generic Exception Signature.
   * @throws AppException
   * Generic Exception Signature.
   */
  @Override
  public BDMCommunicationKey createRecordedComm1(
    final BDMRecordCommunicationDetails recordedCommDetails)
    throws AppException, InformationalException {

    recordedCommDetails.dtls.recordedCommDetails.phoneCountryCode =
      recordedCommDetails.phoneCountryCode;

    validateRecordCommunicationDetails(
      recordedCommDetails.dtls.recordedCommDetails.newEmailAddress,
      recordedCommDetails.dtls.recordedCommDetails.phoneCountryCode,
      recordedCommDetails.dtls.recordedCommDetails.phoneAreaCode,
      recordedCommDetails.dtls.recordedCommDetails.phoneNumber,
      recordedCommDetails.dtls.recordedCommDetails.phoneExtension,
      recordedCommDetails.dtls.recordedCommDetails.communicationDirection,
      recordedCommDetails.dtls.recordedCommDetails.communicationTypeCode);

    final ConcernRoleCommKeyOut concernRoleCommKeyOut =
      BDMCommunicationFactory.newInstance().createRecordedCommWithReturningID(
        recordedCommDetails.dtls.recordedCommDetails);
    final BDMCommunicationKey bdmCommunicationKey = new BDMCommunicationKey();
    bdmCommunicationKey.communicationID =
      concernRoleCommKeyOut.communicationID;
    return bdmCommunicationKey;
  }

  /**
   * Modifies a recorded communication.
   *
   * @param modifyRecordedCommKey
   * Communication key.
   *
   * @param modifyRecordedCommDetails
   * Communication Details.
   * @throws InformationalException
   * Generic Exception Signature.
   * @throws AppException
   * Generic Exception Signature.
   */
  @Override
  public void modifyRecordedCommunication1(
    final ModifyRecordedCommKey modifyRecordedCommKey,
    final BDMModifyRecordedCommDetails modifyRecordedCommDetails)
    throws AppException, InformationalException {

    modifyRecordedCommDetails.dtls.recordedCommDetails.phoneCountryCode =
      modifyRecordedCommDetails.phoneCountryCode;
    validateRecordCommunicationDetails(
      modifyRecordedCommDetails.dtls.recordedCommDetails.newEmailAddress,
      modifyRecordedCommDetails.dtls.recordedCommDetails.phoneCountryCode,
      modifyRecordedCommDetails.dtls.recordedCommDetails.phoneAreaCode,
      modifyRecordedCommDetails.dtls.recordedCommDetails.phoneNumber,
      modifyRecordedCommDetails.dtls.recordedCommDetails.phoneExtension,
      modifyRecordedCommDetails.dtls.recordedCommDetails.communicationDirection,
      modifyRecordedCommDetails.dtls.recordedCommDetails.communicationTypeCode);

    // Call service layer method to modify a recorded communication.
    BDMCommunicationFactory.newInstance().modifyRecordedComm(
      modifyRecordedCommKey.recordedCommKey,
      modifyRecordedCommDetails.dtls.recordedCommDetails);
  }

  /**
   * Validation method
   * Task 21122 Email formatting validation
   * Task 21121 Phone number validations
   *
   * @since
   * @param
   */
  private void validateRecordCommunicationDetails(final String emailAddress,
    final String phoneCountryCode, final String phoneAreaCode,
    final String phoneNumber, final String phoneExtension,
    final String communicationDirection, final String communicationType)
    throws InformationalException, AppException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    if (communicationType
      .equalsIgnoreCase(COMMUNICATIONTYPE.INTERIM_APP_OUTGOING)
      && communicationDirection
        .equalsIgnoreCase(COMMUNICATIONDIRECTION.INCOMING)) {
      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_DIRECTION_INCOMING_SELECTED);
      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);
    }

    final String emailRegex =
      "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^]+)*@(?:[A-Za-z0-9-]+\\.)+[A-Za-z]{2,6}$";

    // validation: Email address must be valid.
    if (!emailAddress.isEmpty()) {
      final String[] emailAddressArray = emailAddress.split("@");
      if (!emailAddress.matches(emailRegex) || emailAddressArray.length == 2
        && (emailAddressArray[0].length() > 64
          || emailAddressArray[1].length() > 253)) {
        final LocalisableString localisableString =
          new LocalisableString(BDMPERSON.ERR_INVALIE_EMAIL_ADDRESS);
        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kError);
      }
    }

    final CodeTableAdmin codeTableAdminObj =
      CodeTableAdminFactory.newInstance();
    final CodeTableItemUniqueKey codeTableItemUniqueKey =
      new CodeTableItemUniqueKey();

    codeTableItemUniqueKey.code = phoneCountryCode;
    codeTableItemUniqueKey.locale = TransactionInfo.getProgramLocale();
    codeTableItemUniqueKey.tableName = BDMPHONECOUNTRY.TABLENAME;

    final Boolean isPlusOneCountry = phoneCountryCode.isEmpty() ? false
      : codeTableAdminObj
        .readCTIDetailsForLocaleOrLanguage(codeTableItemUniqueKey).annotation
          .trim().equals(BDMConstants.kphonePrefix);

    // validation: if country code is +1, then area code is mandatory
    if (isPlusOneCountry && phoneAreaCode.isEmpty()) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PHONE_AREA_CODE);

      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);
    } else if (isPlusOneCountry
      && (!isNumeric(phoneAreaCode) || phoneAreaCode.length() != 3)) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PHONE_AREA_CODE_3DIGIT);

      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);

    }

    // validation: if country code is +1 then phone Number must be 7 digits and
    // numeric
    if (isPlusOneCountry && phoneNumber.isEmpty()) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PHONE_NUMBER_MISSING);

      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);

    } else if (isPlusOneCountry
      && (!isNumeric(phoneNumber) || phoneNumber.length() != 7)) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PHONE_NUMBER_7DIGIT);

      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);

    }
    // validation: if country code is not +1 then phone Number and Area code
    // must be numeric
    if (!isPlusOneCountry
      && (!isNumeric(phoneAreaCode) || !isNumeric(phoneNumber))) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PHONE_NUMBER_OTHER_COUNTRY);

      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);

    }

    // validation: Extension can only have numeric values
    if (!isNumeric(phoneExtension)) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PHONE_EXTENSION_NUMERIC);

      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);

    }

    informationalManager.failOperation();

  }

  /**
   * Util Method to validate if given string is a number
   *
   * @since
   * @param phoneNumber
   * @return
   */
  private boolean isNumeric(final String phoneNumber) {

    if (phoneNumber.isEmpty()) {

      return true;

    }
    // regex to check for Numeric Values
    final Pattern pattern = Pattern.compile("^[0-9]*$");
    final Matcher matcher = pattern.matcher(phoneNumber);
    return matcher.matches();
  }

  /**
   * Creates an Email communication.
   *
   * @param createEmailCommDetails
   * Details to create an email communication.
   */
  @Override
  public void createEmail(final CreateEmailCommDetails createEmailCommDetails)
    throws AppException, InformationalException {

    validateEmailDetails(createEmailCommDetails.details.emailAddress);
    final Communication communicationObj = CommunicationFactory.newInstance();
    communicationObj.createEmail(createEmailCommDetails);
  }

  /**
   * Validation method
   * Task 21122 Email formatting validation
   *
   * @since
   * @param
   */
  private void validateEmailDetails(final String emailAddress)
    throws InformationalException, AppException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    final String emailRegex =
      "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^]+)*@(?:[A-Za-z0-9-]+\\.)+[A-Za-z]{2,6}$";

    // validation: Email address must be valid.
    if (!emailAddress.isEmpty()) {
      final String[] emailAddressArray = emailAddress.split("@");
      if (!emailAddress.matches(emailRegex) || emailAddressArray.length == 2
        && (emailAddressArray[0].length() > 64
          || emailAddressArray[1].length() > 253)) {
        final LocalisableString localisableString =
          new LocalisableString(BDMPERSON.ERR_INVALIE_EMAIL_ADDRESS);
        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kError);
      }
    }
    informationalManager.failOperation();
  }

  @Override
  public ReadRecordedCommunicationCaseMember
    readRecordedCommunicationAndCaseMember(
      final ReadRecordedCommKey readRecordedCommKey)
      throws AppException, InformationalException {

    final ReadRecordedCommunicationCaseMember readRecordedCommunicationCaseMember =
      new ReadRecordedCommunicationCaseMember();

    final RecordedCommDetails1 recordedCommDetails = BDMCommunicationFactory
      .newInstance().readRecordedComm(readRecordedCommKey.recordedCommKey);

    // Bug 102768: Unable to view details of Incoming Tier 2 Privacy Request
    // from ATIP
    final curam.core.struct.ConcernRoleKey concernRoleKey =
      new curam.core.struct.ConcernRoleKey();
    concernRoleKey.concernRoleID =
      recordedCommDetails.correspondentParticipantRoleID;
    final ConcernRole concernRoleObj = ConcernRoleFactory.newInstance();
    final ConcernRoleTypeDetails concernRoleTypeDetails =
      concernRoleObj.readConcernRoleType(concernRoleKey);

    // Add the regarding case member name to the return struct
    if (recordedCommDetails.clientParticipantRoleID != 0) {

      final ConcernRoleNameTypeDateDetails concernRoleNameTypeDateDetails =
        ConcernRoleFactory.newInstance().readNameTypeAndDate(concernRoleKey);

      readRecordedCommunicationCaseMember.clientConcernRoleName =
        concernRoleNameTypeDateDetails.concernRoleName;
      readRecordedCommunicationCaseMember.clientConcernRoleType =
        concernRoleNameTypeDateDetails.concernRoleType;
    }

    if (recordedCommDetails.addressID == 0l) {

      final ReadParticipantAddressListKey participantKey =
        new ReadParticipantAddressListKey();
      participantKey.maintainAddressKey.concernRoleID =
        recordedCommDetails.correspondentParticipantRoleID;

      final BDMReadParticipantAddressDetails addressDetails =
        BDMParticipantFactory.newInstance()
          .readMailingAddress(participantKey);
      recordedCommDetails.addressID = addressDetails.addressID;
      recordedCommDetails.addressData =
        addressDetails.dtls.addressDetails.formattedAddressData;
      recordedCommDetails.formattedAddressData =
        addressDetails.dtls.addressDetails.formattedAddressData;
    }
    // Bug 102768: Unable to view details of Incoming Tier 2 Privacy Request
    // from ATIP
    if (recordedCommDetails.phoneNumberID == 0l
      && !concernRoleTypeDetails.concernRoleType
        .equals(CONCERNROLETYPE.REPRESENTATIVE)) {

      try {
        final DynamicEvidenceDataDetails[] phoneNumberEvidList =
          bdmCommHelper.getPhoneNumberEvidence(
            recordedCommDetails.correspondentParticipantRoleID);
        for (final DynamicEvidenceDataDetails phoneNumberEvid : phoneNumberEvidList) {
          if ((Boolean) DynamicEvidenceTypeConverter.convert(phoneNumberEvid
            .getAttribute(BDMConstants.kEvidenceAttrPreferredInd))) {
            final CodeTableItem phoneCountryCodeCT =
              (CodeTableItem) DynamicEvidenceTypeConverter
                .convert(phoneNumberEvid
                  .getAttribute(BDMConstants.kEvidenceAttrPhoneCountryCode));
            final String phoneCountryCode =
              phoneCountryCodeCT.toString().substring(0,
                phoneCountryCodeCT.toString().indexOf(CuramConst.gkSpace));
            final String phoneNumberFull = phoneCountryCode
              + (String) DynamicEvidenceTypeConverter.convert(phoneNumberEvid
                .getAttribute(BDMConstants.kEvidenceAttrPhoneAreaCode))
              + (String) DynamicEvidenceTypeConverter.convert(phoneNumberEvid
                .getAttribute(BDMConstants.kEvidenceAttrPhoneNumber));

            recordedCommDetails.phoneCountryCode = phoneCountryCode;
            recordedCommDetails.phoneAreaCode =
              (String) DynamicEvidenceTypeConverter.convert(phoneNumberEvid
                .getAttribute(BDMConstants.kEvidenceAttrPhoneAreaCode));
            recordedCommDetails.phoneNumber =
              (String) DynamicEvidenceTypeConverter.convert(phoneNumberEvid
                .getAttribute(BDMConstants.kEvidenceAttrPhoneNumber));
            recordedCommDetails.phoneNumberString = phoneNumberFull;
            break;
          }
        }
      } catch (final RecordNotFoundException rnfe) {

      }
    }

    // Bug 102768: Unable to view details of Incoming Tier 2 Privacy Request
    // from ATIP -Added Represntative check

    if (StringUtil.isNullOrEmpty(recordedCommDetails.emailAddress)
      && !concernRoleTypeDetails.concernRoleType
        .equals(CONCERNROLETYPE.REPRESENTATIVE)) {

      final DynamicEvidenceDataDetails[] emailAddressEvidList =
        bdmCommHelper.getEmailAddressEvidence(
          recordedCommDetails.correspondentParticipantRoleID);
      for (final DynamicEvidenceDataDetails emailAddressEvid : emailAddressEvidList) {
        if ((Boolean) DynamicEvidenceTypeConverter.convert(emailAddressEvid
          .getAttribute(BDMConstants.kEvidenceAttrPreferredInd))) {
          final String email =
            (String) DynamicEvidenceTypeConverter.convert(emailAddressEvid
              .getAttribute(BDMConstants.kEvidenceAttrEmailAddress));
          recordedCommDetails.emailAddress = email;
          break;

        }
      }
    }

    readRecordedCommunicationCaseMember.recordedCommCaseMember.recordedCommDetails =
      recordedCommDetails;

    return readRecordedCommunicationCaseMember;
  }

  /**
   * This method is to create a correspondence record in Curam and
   * send the request to CCT. Update the record with the CCT response data.
   *
   * @param wizardKey
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMTemplateDetails
    createCorrespondence(final BDMCorrespondenceWizardKey key)
      throws AppException, InformationalException {

    final BDMTemplateDetails bdmTemplateDetails =
      curam.ca.gc.bdm.sl.communication.fact.BDMCommunicationFactory
        .newInstance().createCorrespondence(key);

    return bdmTemplateDetails;
  }

  /**
   * This method is to search templates based on the search criteria
   *
   * @param searchKey
   * @param BDMTemplateSearchDetailsList
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMTemplateSearchDetailsList
    searchTemplates(final BDMTemplateSearchKey searchKey)
      throws AppException, InformationalException {

    return curam.ca.gc.bdm.sl.communication.fact.BDMCommunicationFactory
      .newInstance().searchTemplates(searchKey);
  }

  /**
   * This method used to get the list of all the template categories and
   * sub-categories.
   *
   * @return BDMCategorySubCategoryList
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMCategorySubCategoryList getCategoryAndSubCategoryList()
    throws AppException, InformationalException {

    return BDMCommunicationFactory.newInstance()
      .getCategoryAndSubCategoryList();
  }

  /**
   * This method used to get the Create Correspondence
   * wizard menu property details.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public WizardDetails getCorrespondenceWizard()
    throws AppException, InformationalException {

    final WizardDetails wizardDetails = new WizardDetails();
    wizardDetails.wizardMenu =
      BDMConstants.kCreateCorrespondenceWizardProperties;

    return wizardDetails;
  }

  /**
   * This method used to create wizard and
   * save all the correspondence details in it.
   *
   * @param corresDetails
   * @return WizardStateID
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public WizardStateID
    saveCorrespondenceWizard(final BDMCorrespondenceDetails corresDetails)
      throws AppException, InformationalException {

    return BDMCommunicationFactory.newInstance()
      .saveCorrespondenceWizard(corresDetails);
  }

  /**
   * This method used to get the correspondence details
   * saved in the wizard along with the template details.
   *
   * @param wizardKey
   * @return BDMCorrespondenceDetails
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMCorrespondenceDetails
    getCorrespondenceDetails(final BDMCorrespondenceWizardKey wizardKey)
      throws AppException, InformationalException {

    final WizardPersistentState wizardObj = new WizardPersistentState();

    final BDMCorrespondenceDetails corresDetails =
      (BDMCorrespondenceDetails) wizardObj.read(wizardKey.wizardStateID);

    // get the template details including category and sub-category names
    final BDMCCTTemplateKey templateKey = new BDMCCTTemplateKey();
    templateKey.templateID = wizardKey.templateID;

    final BDMTemplateSearchDetails templateDetails = BDMCCTTemplateFactory
      .newInstance().readTemplateDetailsByTemplateID(templateKey);

    // set the template details
    corresDetails.templateID = templateDetails.templateID;
    corresDetails.templateName = templateDetails.templateName;
    corresDetails.templatePath = templateDetails.templatePath;

    // set the category and sub-category as subject
    if (!templateDetails.subCategory.isEmpty()) {
      corresDetails.subject = templateDetails.category + CuramConst.gkDash
        + templateDetails.subCategory;
    } else {
      corresDetails.subject = templateDetails.category;
    }

    // save the details in wizard
    wizardObj.modify(wizardKey.wizardStateID, corresDetails);

    return corresDetails;
  }

  /**
   * This method used to get the correspondence details
   * for the Inline Details page.
   *
   * @param key
   * @return BDMViewCorrespondenceDetails
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMViewCorrespondenceDetails
    getViewCorrespondence(final BDMCommunicationKey key)
      throws AppException, InformationalException {

    return BDMCommunicationFactory.newInstance().getViewCorrespondence(key);
  }

  /**
   * This method returns a list of codes that are allowed for
   * a correspondence record to have once it's marked as sent.
   *
   * @return list of acceptable codes and description
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public ListCodeDetails getCommunicationStatusesForModify()
    throws AppException, InformationalException {

    final ListCodeDetails listCodeDetails = new ListCodeDetails();

    String statusListString = Configuration
      .getProperty(EnvVars.BDM_MODIFY_CORRESPONDENCE_STATUS_LIST);
    if (StringUtil.isNullOrEmpty(statusListString)) {
      statusListString =
        EnvVars.BDM_MODIFY_CORRESPONDENCE_STATUS_LIST_DEFAULT;
    }

    final String[] statuses =
      statusListString.split(CuramConst.gkCommaDelimiter);
    for (final String status : statuses) {
      final String description =
        CodeTable.getOneItem(COMMUNICATIONSTATUS.TABLENAME, status);
      if (!StringUtil.isNullOrEmpty(description)) {
        final CodeDetails codeDetails = new CodeDetails();
        codeDetails.code = status;
        codeDetails.description = description;
        codeDetails.codetable = COMMUNICATIONSTATUS.TABLENAME;
        listCodeDetails.codeDetails.addRef(codeDetails);
      }
    }
    return listCodeDetails;
  }

  /**
   *
   * This method modifies the status of a communication record.
   * The current status is saved in the status history entity before
   * modification.
   *
   * @param communicationID and communication status
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void
    modifyCommunicationStatus(final BDMCommunicationStatusDetails details)
      throws AppException, InformationalException {

    BDMCommunicationFactory.newInstance().modifyCommunicationStatus(details);

  }

  /**
   * This method used to get the Create Case Correspondence
   * wizard menu property details.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public WizardDetails getCaseCorrespondenceWizard()
    throws AppException, InformationalException {

    final WizardDetails wizardDetails = new WizardDetails();
    wizardDetails.wizardMenu =
      BDMConstants.kCreateCaseCorrespondenceWizardProperties;

    return wizardDetails;
  }

  @Override
  public BDMTemplateSearchCriteria getSubCategorySearchCriteria()
    throws AppException, InformationalException {

    // BDMCommunication service layer object
    final curam.ca.gc.bdm.sl.communication.intf.BDMCommunication bdmCommunicationObj =
      curam.ca.gc.bdm.sl.communication.fact.BDMCommunicationFactory
        .newInstance();

    BDMTemplateSearchCriteria templateSearchCriteria =
      new BDMTemplateSearchCriteria();

    templateSearchCriteria =
      bdmCommunicationObj.getSubCategorySearchCriteria();

    return templateSearchCriteria;
  }

  /**
   * This method returns the record from BDMConcernRoleCommunication
   * entity for the given communicationID.
   *
   * @param communicationID
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMConcernRoleCommunicationDtls
    readBDMConcernRoleCommunicationDetails(
      final BDMConcernRoleCommunicationKey key)
      throws AppException, InformationalException {

    return BDMCommunicationFactory.newInstance()
      .readBDMConcernRoleCommunicationDetails(key);
  }

  /**
   * Retrieve External File link from provided FileLocation URL
   */
  @Override
  public BDMFileLocationURL
    retrieveExternalFileLink(final ReadRecordedCommKey key)
      throws AppException, InformationalException {

    final BDMFileLocationURL bdmFileLocationURL = new BDMFileLocationURL();

    // Communication service layer object
    final curam.core.facade.intf.Communication communicationObj =
      curam.core.facade.fact.CommunicationFactory.newInstance();

    final ReadRecordedCommDetails1 readRecordedCommDetails1 =
      communicationObj.readRecordedCommunication1(key);

    bdmFileLocationURL.fileLocationURL =
      readRecordedCommDetails1.recordedCommDetails.fileLocation;
    getFileLocationURL(bdmFileLocationURL);

    return bdmFileLocationURL;
  }

  /**
   *
   * Get file location information for display
   *
   * @param bdmfaAttachmentDetailsForRead
   */
  private void
    getFileLocationURL(final BDMFileLocationURL bdmFileLocationURL) {

    final String fileLocationURL = bdmFileLocationURL.fileLocationURL.trim();
    String fileName = "";

    try {
      fileName =
        fileLocationURL.substring(fileLocationURL.lastIndexOf("\\") + 1);

      Trace.kTopLevelLogger.info("filename ----" + fileName);
    } catch (final Exception e) {

      Trace.kTopLevelLogger.info(
        fileLocationURL + " is not valid file." + e.getLocalizedMessage());
    }

    fileLocationURL.replace("\\", "/");

    bdmFileLocationURL.fileName = fileName;
    bdmFileLocationURL.fileLocationURL = fileLocationURL;

  }

  @Override
  public BDMConcernRoleIDEvidenceIDAndNameDetailsList
    listThirdPartyContactsForCommunication(final ConcernRoleIDCaseIDKey key)
      throws AppException, InformationalException {

    return BDMCommunicationFactory.newInstance()
      .listThirdPartyContactsForCommunication(key);

  }

}
