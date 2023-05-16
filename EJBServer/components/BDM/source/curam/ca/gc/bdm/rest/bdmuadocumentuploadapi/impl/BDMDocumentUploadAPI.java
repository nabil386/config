package curam.ca.gc.bdm.rest.bdmuadocumentuploadapi.impl;

import com.google.inject.Inject;
import curam.appeal.sl.entity.fact.AppealOnlineRequestLinkFactory;
import curam.appeal.sl.entity.intf.AppealOnlineRequestLink;
import curam.appeal.sl.entity.struct.AppealOnlineRequestIDKey;
import curam.appeal.sl.entity.struct.AppealOnlineRequestLinkDetails;
import curam.ca.gc.bdm.codetable.BDMRFRDOCUMENTTYPE;
import curam.ca.gc.bdm.codetable.impl.BDMRFRDOCUMENTTYPEEntry;
import curam.ca.gc.bdm.entity.fact.BDMOnlineAppealRequestAttachmentLinkFactory;
import curam.ca.gc.bdm.entity.fact.BDMOnlineAppealRequestFactory;
import curam.ca.gc.bdm.entity.intf.BDMOnlineAppealRequest;
import curam.ca.gc.bdm.entity.intf.BDMOnlineAppealRequestAttachmentLink;
import curam.ca.gc.bdm.entity.struct.BDMOnlineAppealRequestAttachmentLinkDtls;
import curam.ca.gc.bdm.entity.struct.BDMOnlineAppealRequestAttachmentLinkDtlsList;
import curam.ca.gc.bdm.entity.struct.BDMOnlineAppealRequestAttachmentLinkKeyStruct1;
import curam.ca.gc.bdm.entity.struct.BDMOnlineAppealRequestDtls;
import curam.ca.gc.bdm.entity.struct.BDMOnlineAppealRequestKey;
import curam.ca.gc.bdm.rest.bdmuadocumentuploadapi.struct.BDMAppealCaseInfo;
import curam.ca.gc.bdm.rest.bdmuadocumentuploadapi.struct.BDMDocumentUploadAttachmentLinkCreation;
import curam.ca.gc.bdm.rest.bdmuadocumentuploadapi.struct.BDMDocumentUploadList;
import curam.ca.gc.bdm.rest.bdmuadocumentuploadapi.struct.BDMProvidedAttachment;
import curam.ca.gc.bdm.sl.bdmworkallocation.struct.BDMAgentSPMNotificationDetails;
import curam.citizenworkspace.rest.message.impl.RESTAPIERRORMESSAGESExceptionCreator;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.codetable.DOCUMENTTYPE;
import curam.codetable.TASKSTATUS;
import curam.codetable.impl.DOCUMENTTYPEEntry;
import curam.codetable.impl.RECORDSTATUSEntry;
import curam.core.fact.AttachmentFactory;
import curam.core.fact.ConcernRoleAttachmentLinkFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.impl.CuramConst;
import curam.core.intf.Attachment;
import curam.core.intf.ConcernRole;
import curam.core.intf.ConcernRoleAttachmentLink;
import curam.core.sl.fact.AttachmentInfoFactory;
import curam.core.sl.fact.OnlineAppealRequestFactory;
import curam.core.sl.fact.TaskManagementFactory;
import curam.core.sl.intf.AttachmentInfo;
import curam.core.sl.intf.OnlineAppealRequest;
import curam.core.sl.intf.TaskManagement;
import curam.core.sl.struct.OnlineAppealRequestDtls;
import curam.core.sl.struct.OnlineAppealRequestKey;
import curam.core.sl.struct.TaskManagementTaskKey;
import curam.core.sl.struct.ViewTaskDetails;
import curam.core.struct.AttachmentDtls;
import curam.core.struct.AttachmentHeaderDetails;
import curam.core.struct.AttachmentKey;
import curam.core.struct.ConcernRoleAttachmentLinkDtls;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.piwrapper.user.impl.UserDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.RecordNotFoundException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.type.Date;
import curam.util.type.NotFoundIndicator;
import curam.util.type.UniqueID;
import curam.util.workflow.fact.BizObjAssociationFactory;
import curam.util.workflow.impl.EnactmentService;
import curam.util.workflow.intf.BizObjAssociation;
import curam.util.workflow.struct.BizObjAssocSearchDetailsList;
import curam.util.workflow.struct.BizObjectTypeKey;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BDMDocumentUploadAPI extends
  curam.ca.gc.bdm.rest.bdmuadocumentuploadapi.base.BDMDocumentUploadAPI {

  @Inject
  private UserDAO userDAO;

  private final Attachment attachmentObj;

  private final AttachmentInfo attachmentInfoObj;

  public BDMDocumentUploadAPI() {

    GuiceWrapper.getInjector().injectMembers(this);
    this.attachmentObj = AttachmentFactory.newInstance();
    this.attachmentInfoObj = AttachmentInfoFactory.newInstance();
  }

  /**
   * Takes caseInfo and returns a List of documents that have been uploaded to
   * relate to that case
   *
   * @param caseInfo
   */
  @Override
  public BDMDocumentUploadList
    getBDMDocumentUploads(final BDMAppealCaseInfo caseInfo)
      throws AppException, InformationalException {

    this.validateAppealCaseID(caseInfo.appealID);
    final BDMDocumentUploadList bdmDocumentUploadList =
      new BDMDocumentUploadList();
    bdmDocumentUploadList.appealID = caseInfo.appealID;
    bdmDocumentUploadList.documentCodeTablename =
      BDMRFRDOCUMENTTYPE.TABLENAME;

    bdmDocumentUploadList.providedAttachments
      .addAll(this.getProvidedAttachments(caseInfo));

    return bdmDocumentUploadList;
  }

  /**
   * Takes a specific bdmDocumentUploadAttachmentLinkCreation containing and
   * attachmentID and a appeal caseID and creates a record in the
   * BDMOnlineAppealsAttachmentLink table
   *
   * @param bdmDocumentUploadAttachmentLinkCreation
   */
  @Override
  public void createBDMDocumentUploadAttachmentLink(
    final BDMDocumentUploadAttachmentLinkCreation attachmentLinkCreation)
    throws AppException, InformationalException {

    this.validateAttachmentID(attachmentLinkCreation.attachmentID);
    this.validateAppealCaseID(attachmentLinkCreation.appealID);
    final BDMOnlineAppealRequestAttachmentLinkDtls bdmAttachmentLinkDtls =
      new BDMOnlineAppealRequestAttachmentLinkDtls();
    bdmAttachmentLinkDtls.attachmentID = attachmentLinkCreation.attachmentID;
    bdmAttachmentLinkDtls.onlineAppealRequestID =
      attachmentLinkCreation.appealID;
    bdmAttachmentLinkDtls.onlineAppealRequestAttachmentLinkID =
      UniqueID.nextUniqueID();
    final BDMOnlineAppealRequestAttachmentLink bdmOnlineAppealRequestAttachmentLinkObj =
      BDMOnlineAppealRequestAttachmentLinkFactory.newInstance();
    try {
      bdmOnlineAppealRequestAttachmentLinkObj.insert(bdmAttachmentLinkDtls);
    } catch (final Exception e) {
      e.printStackTrace();
    }

    // Task 64911 to fix bug 64721 : Send notificatio when client submits RFR
    // supporting document
    // Step1 : Check if attachment is not of type Request for Reconsideration
    // Form
    final curam.core.sl.intf.Attachment attachmentObj =
      curam.core.sl.fact.AttachmentFactory.newInstance();
    final AttachmentKey attachmentKey = new AttachmentKey();
    attachmentKey.attachmentID = attachmentLinkCreation.attachmentID;
    final AttachmentDtls attachmentDtls =
      attachmentObj.readAttachment(attachmentKey);
    if (!DOCUMENTTYPE.REQUEST_FOR_RECONSIDERATION
      .equals(attachmentDtls.documentType)) {
      // Check if Request for Request For Reconsideration task is still open for
      // online appeal request ID
      String notificationTargetUserNmae = CuramConst.gkEmpty;
      final TaskManagement taskManagementObj =
        TaskManagementFactory.newInstance();
      final TaskManagementTaskKey taskManagementTaskKey =
        new TaskManagementTaskKey();
      ViewTaskDetails viewTaskDetails = new ViewTaskDetails();
      final BizObjAssociation bizObjAssociationObj =
        BizObjAssociationFactory.newInstance();
      final BizObjectTypeKey bizObjectTypeKey = new BizObjectTypeKey();
      bizObjectTypeKey.bizObjectType =
        BUSINESSOBJECTTYPE.BDMONLINEAPPEALREQUEST;
      bizObjectTypeKey.bizObjectID =
        bdmAttachmentLinkDtls.onlineAppealRequestID;
      final BizObjAssocSearchDetailsList bizObjAssocSearchDetailsList =
        bizObjAssociationObj.searchByBizObjectTypeAndID(bizObjectTypeKey);
      if (bizObjAssocSearchDetailsList.dtls.size() > 0) {
        for (int cnt = 0; cnt < bizObjAssocSearchDetailsList.dtls
          .size(); cnt++) {
          taskManagementTaskKey.taskID =
            bizObjAssocSearchDetailsList.dtls.item(cnt).taskID;
          viewTaskDetails =
            taskManagementObj.viewTaskHome(taskManagementTaskKey);
          if (TASKSTATUS.NOTSTARTED.equals(viewTaskDetails.status)
            && viewTaskDetails.reservedBy.length() != 0) {
            if (viewTaskDetails.subject
              .contains("Request For Reconsideration")) {
              notificationTargetUserNmae = viewTaskDetails.reservedBy;
              break;
            }

          }
        }
        /*
         * taskManagementTaskKey.taskID =
         * bizObjAssocSearchDetailsList.dtls.item(0).taskID;
         * viewTaskDetails =
         * taskManagementObj.viewTaskHome(taskManagementTaskKey);
         * if (TASKSTATUS.NOTSTARTED.equals(viewTaskDetails.status)
         * && viewTaskDetails.reservedBy.length() != 0) {
         * notificationTargetUserNmae = viewTaskDetails.reservedBy;
         * }
         */
      }
      if (StringUtil.isNullOrEmpty(notificationTargetUserNmae)) {
        // Check if Perform a Reconsideration for task is still open for appeal
        // case ID
        final AppealOnlineRequestLink appealOnlineRequestLinkObj =
          AppealOnlineRequestLinkFactory.newInstance();
        final AppealOnlineRequestIDKey appealOnlineRequestIDKey =
          new AppealOnlineRequestIDKey();
        appealOnlineRequestIDKey.onlineAppealRequestID =
          bdmAttachmentLinkDtls.onlineAppealRequestID;
        AppealOnlineRequestLinkDetails appealOnlineRequestLinkDetails =
          new AppealOnlineRequestLinkDetails();
        try {
          appealOnlineRequestLinkDetails = appealOnlineRequestLinkObj
            .readAppealCaseIDForRequest(appealOnlineRequestIDKey);
        } catch (final RecordNotFoundException ex) {

        }
        if (appealOnlineRequestLinkDetails.appealCaseID != 0) {
          bizObjectTypeKey.bizObjectType = BUSINESSOBJECTTYPE.CASE;
          bizObjectTypeKey.bizObjectID =
            appealOnlineRequestLinkDetails.appealCaseID;
          final BizObjAssocSearchDetailsList bizObjAssocSearchDtlsList =
            bizObjAssociationObj.searchByBizObjectTypeAndID(bizObjectTypeKey);
          if (bizObjAssocSearchDtlsList.dtls.size() > 0) {
            for (int cnt = 0; cnt < bizObjAssocSearchDtlsList.dtls
              .size(); cnt++) {
              taskManagementTaskKey.taskID =
                bizObjAssocSearchDtlsList.dtls.item(cnt).taskID;
              viewTaskDetails =
                taskManagementObj.viewTaskHome(taskManagementTaskKey);
              if (TASKSTATUS.NOTSTARTED.equals(viewTaskDetails.status)
                && viewTaskDetails.reservedBy.length() != 0) {
                if (viewTaskDetails.subject
                  .contains("Perform a Reconsideration")) {
                  notificationTargetUserNmae = viewTaskDetails.reservedBy;
                  break;
                }

              }
            }

            /*
             * taskManagementTaskKey.taskID =
             * bizObjAssocSearchDtlsList.dtls.item(0).taskID;
             * viewTaskDetails =
             * taskManagementObj.viewTaskHome(taskManagementTaskKey);
             * if (TASKSTATUS.NOTSTARTED.equals(viewTaskDetails.status)
             * && viewTaskDetails.reservedBy.length() != 0) {
             * notificationTargetUserNmae = viewTaskDetails.reservedBy;
             * }
             */
          }

        }

      }
      // enact workflow to send notification to user to inform RFR Supporting
      // document is uploaded
      if (!StringUtil.isNullOrEmpty(notificationTargetUserNmae)) {
        final BDMAgentSPMNotificationDetails bdmAgentSPMNotificationDetails =
          new BDMAgentSPMNotificationDetails();
        bdmAgentSPMNotificationDetails.userName = notificationTargetUserNmae;
        final StringBuffer notificationSubject = new StringBuffer();
        notificationSubject.append("Supporting Document(s) uploaded for ");
        final ConcernRole concernRoleobj = ConcernRoleFactory.newInstance();
        final ConcernRoleKey concernRolekey = new ConcernRoleKey();
        concernRolekey.concernRoleID =
          getConcernRoleforAppealCase(attachmentLinkCreation.appealID);
        final ConcernRoleDtls concernRoleDtls =
          concernRoleobj.read(concernRolekey);
        notificationSubject.append(concernRoleDtls.concernRoleName)
          .append(" ");
        notificationSubject.append(concernRoleDtls.primaryAlternateID)
          .append(" ");
        final BDMOnlineAppealRequest bdmOnlineAppealRequestObj =
          BDMOnlineAppealRequestFactory.newInstance();
        final BDMOnlineAppealRequestKey bdmOnlineAppealRequestKey =
          new BDMOnlineAppealRequestKey();
        final NotFoundIndicator notFoundIndicator1 = new NotFoundIndicator();
        bdmOnlineAppealRequestKey.onlineAppealRequestID =
          attachmentLinkCreation.appealID;
        final BDMOnlineAppealRequestDtls onlineAppealRequestDtls =
          bdmOnlineAppealRequestObj.read(notFoundIndicator1,
            bdmOnlineAppealRequestKey);
        if (!notFoundIndicator1.isNotFound()) {
          notificationSubject
            .append(onlineAppealRequestDtls.onlineAppealRequestReference);

        }
        bdmAgentSPMNotificationDetails.subject =
          notificationSubject.toString();

        final StringBuffer notificationBody = new StringBuffer();
        notificationBody.append(concernRoleDtls.concernRoleName).append(" ");
        notificationBody
          .append("has uploaded supporting documents for a reconsideration.");
        bdmAgentSPMNotificationDetails.body = notificationBody.toString();
        bdmAgentSPMNotificationDetails.concernRoleID =
          concernRolekey.concernRoleID;
        final List<Object> enactmentStructs = new ArrayList();
        enactmentStructs.add(bdmAgentSPMNotificationDetails);
        EnactmentService.startProcess("BDMAgentSPMNotification",
          enactmentStructs);

      }

    }
    // Create a concernRoleAttachmentLink
    final ConcernRoleAttachmentLinkDtls attachmentLinkDtls =
      new ConcernRoleAttachmentLinkDtls();
    attachmentLinkDtls.attachmentID = attachmentLinkCreation.attachmentID;
    attachmentLinkDtls.attachmentLinkID = UniqueID.nextUniqueID();
    attachmentLinkDtls.dateReceived = Date.getCurrentDate();
    attachmentLinkDtls.concernRoleID =
      getConcernRoleforAppealCase(attachmentLinkCreation.appealID);
    attachmentLinkDtls.statusCode = RECORDSTATUSEntry.NORMAL.getCode();
    final ConcernRoleAttachmentLink concernRoleAttachmentLinkObj =
      ConcernRoleAttachmentLinkFactory.newInstance();
    try {
      concernRoleAttachmentLinkObj.insert(attachmentLinkDtls);
    } catch (final Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * Checks if the passed attachmentID is a valid attachment
   *
   * @param attachmentID
   */
  private void validateAttachmentID(final Long attachmentID)
    throws AppException, InformationalException {

    final Attachment attachmentObj = AttachmentFactory.newInstance();
    final AttachmentKey attachmentKey = new AttachmentKey();
    attachmentKey.attachmentID = attachmentID;
    try {
      attachmentObj.readHeader(attachmentKey);
    } catch (final RecordNotFoundException notFoundException) {
      throw new AppException(
        RESTAPIERRORMESSAGESExceptionCreator
          .HTTP_400_IDS_SPECIFIED_NOT_FOUND().getCatEntry(),
        notFoundException);
    }
  }

  /**
   * Checks if the passed caseId is a valid appeal
   *
   * @param caseId
   */
  private void validateAppealCaseID(final Long caseId)
    throws AppException, InformationalException {

    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
    final OnlineAppealRequest onlineAppealRequest =
      OnlineAppealRequestFactory.newInstance();
    final OnlineAppealRequestKey onlineAppealRequestKey =
      new OnlineAppealRequestKey();
    onlineAppealRequestKey.onlineAppealRequestID = caseId;
    onlineAppealRequest.read(notFoundIndicator, onlineAppealRequestKey);
    if (notFoundIndicator.isNotFound()) {
      final RecordNotFoundException notFoundException = null;
      throw new AppException(
        RESTAPIERRORMESSAGESExceptionCreator
          .HTTP_400_IDS_SPECIFIED_NOT_FOUND().getCatEntry(),
        notFoundException);
    }
  }

  /**
   * gets the concernRoleID for a valid case
   *
   * @param caseId
   */
  private long getConcernRoleforAppealCase(final Long caseId)
    throws AppException, InformationalException {

    long concernRoleID = 0l;
    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
    final OnlineAppealRequest onlineAppealRequest =
      OnlineAppealRequestFactory.newInstance();
    final OnlineAppealRequestKey onlineAppealRequestKey =
      new OnlineAppealRequestKey();
    onlineAppealRequestKey.onlineAppealRequestID = caseId;
    final OnlineAppealRequestDtls request =
      onlineAppealRequest.read(notFoundIndicator, onlineAppealRequestKey);
    if (notFoundIndicator.isNotFound()) {
      final RecordNotFoundException notFoundException = null;
      throw new AppException(
        RESTAPIERRORMESSAGESExceptionCreator
          .HTTP_400_IDS_SPECIFIED_NOT_FOUND().getCatEntry(),
        notFoundException);
    }
    concernRoleID = request.primaryAppellantID;
    return concernRoleID;
  }

  /**
   * gets all the provided attachments linked for the passed appeal case
   *
   * @param caseId
   */
  private ArrayList<BDMProvidedAttachment>
    getProvidedAttachments(final BDMAppealCaseInfo caseInfo)
      throws AppException, InformationalException {

    final ArrayList<BDMProvidedAttachment> result = new ArrayList<>();
    BDMProvidedAttachment providedAttachment;
    final BDMOnlineAppealRequestAttachmentLink bdmOnlineAppealRequestAttachmentLinkObj =
      BDMOnlineAppealRequestAttachmentLinkFactory.newInstance();
    final BDMOnlineAppealRequestAttachmentLinkKeyStruct1 readAllKeyStruct =
      new BDMOnlineAppealRequestAttachmentLinkKeyStruct1();
    readAllKeyStruct.onlineAppealRequestID = caseInfo.appealID;
    final BDMOnlineAppealRequestAttachmentLinkDtlsList readAllDtlsList =
      bdmOnlineAppealRequestAttachmentLinkObj
        .readAllByOnlineAppealRequestID(readAllKeyStruct);
    final Iterator<BDMOnlineAppealRequestAttachmentLinkDtls> iterator =
      readAllDtlsList.dtls.iterator();
    while (iterator.hasNext()) {
      final BDMOnlineAppealRequestAttachmentLinkDtls attachmentLink =
        iterator.next();
      final long attachmentID = attachmentLink.attachmentID;
      final AttachmentKey attachmentKey = new AttachmentKey();
      attachmentKey.attachmentID = attachmentID;
      final AttachmentHeaderDetails attachment =
        this.attachmentObj.readHeader(attachmentKey);
      if (!attachment.documentType
        .equals(DOCUMENTTYPEEntry.REQUEST_FOR_RECONSIDERATION.getCode())) {
        // Make sure the pdf application is not returned in the provided
        // documents
        // section
        final AttachmentKey attachmentIDKey = new AttachmentKey();
        attachmentIDKey.attachmentID = attachmentID;
        try {
          final curam.core.struct.AttachmentInfo attachmentInfo =
            this.attachmentInfoObj.readInfoByAttachmentID(attachmentIDKey);
          providedAttachment = new BDMProvidedAttachment();
          providedAttachment.linkID =
            attachmentLink.onlineAppealRequestAttachmentLinkID;
          providedAttachment.attachmentID = attachmentID;
          providedAttachment.attachmentName = attachment.attachmentName;
          providedAttachment.receivedDate = attachment.receiptDate;
          providedAttachment.type = BDMRFRDOCUMENTTYPEEntry
            .get(attachmentInfo.documentClassification).toUserLocaleString();
          providedAttachment.addedBy =
            this.userDAO.get(attachmentInfo.createdBy).getFullName();
          result.add(providedAttachment);
        } catch (final Exception e) {
        }
      }
    }
    return result;
  }
}
