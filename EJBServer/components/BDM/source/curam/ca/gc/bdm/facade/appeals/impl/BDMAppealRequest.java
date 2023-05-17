package curam.ca.gc.bdm.facade.appeals.impl;

import com.google.inject.Inject;
import curam.appeal.facade.intf.Appeal;
import curam.appeal.facade.struct.AppealCaseKey_fo;
import curam.appeal.sl.entity.fact.AppealOnlineRequestLinkFactory;
import curam.appeal.sl.entity.struct.AppealOnlineRequestIDKey;
import curam.appeal.sl.entity.struct.AppealOnlineRequestLinkDetails;
import curam.ca.gc.bdm.entity.attachment.struct.BDMConcernRoleAttachmentDetailsForList;
import curam.ca.gc.bdm.entity.fact.BDMOnlineAppealRequestAttachmentLinkFactory;
import curam.ca.gc.bdm.entity.fact.BDMOnlineAppealRequestFactory;
import curam.ca.gc.bdm.entity.intf.BDMOnlineAppealRequest;
import curam.ca.gc.bdm.entity.struct.BDMOnlineAppealRequestAttachmentLinkDtls;
import curam.ca.gc.bdm.entity.struct.BDMOnlineAppealRequestAttachmentLinkDtlsList;
import curam.ca.gc.bdm.entity.struct.BDMOnlineAppealRequestDtls;
import curam.ca.gc.bdm.entity.struct.BDMOnlineAppealRequestKey;
import curam.ca.gc.bdm.facade.appeals.struct.BDMAppealRequestsForConcernRoleDetails;
import curam.ca.gc.bdm.facade.appeals.struct.BDMAppealRequestsForConcernRoleDetailsList;
import curam.ca.gc.bdm.facade.appeals.struct.BDMConcernRoleAppealRequestKey;
import curam.ca.gc.bdm.facade.communication.struct.BDMConcernRoleAttachmentListView;
import curam.codetable.APPEALTYPE;
import curam.core.fact.AttachmentFactory;
import curam.core.fact.AttachmentInfoFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.ConcernRoleAttachmentLinkFactory;
import curam.core.intf.Attachment;
import curam.core.intf.CaseHeader;
import curam.core.onlineappealrequest.impl.OnlineAppealRequest;
import curam.core.onlineappealrequest.impl.OnlineAppealRequestDAO;
import curam.core.struct.AttachmentDtls;
import curam.core.struct.AttachmentInfo;
import curam.core.struct.AttachmentKey;
import curam.core.struct.CaseReference;
import curam.core.struct.CaseSearchKey;
import curam.core.struct.ConcernRoleAttachmentLinkDtls;
import curam.core.struct.ConcernRoleAttachmentLinkDtlsList;
import curam.core.struct.ConcernRoleAttachmentLinkReadmultiKey;
import curam.core.struct.RelationshipConcernRoleIDKey;
import curam.datastore.impl.NoSuchSchemaException;
import curam.message.APPEALREQUESTOBJECT;
import curam.participant.impl.ConcernRoleDAO;
import curam.participant.person.impl.PersonDAO;
import curam.piwrapper.impl.ClientURI;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.LocalisableString;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.CodeTableItemIdentifier;
import curam.util.type.NotFoundIndicator;
import java.util.List;

public class BDMAppealRequest
  extends curam.ca.gc.bdm.facade.appeals.base.BDMAppealRequest {

  @Inject
  OnlineAppealRequestDAO onlineAppealRequestDAO;

  @Inject
  PersonDAO personDAO;

  @Inject
  ConcernRoleDAO concernRoleDAO;

  public BDMAppealRequest() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public BDMAppealRequestsForConcernRoleDetailsList
    listAppealRequestsForConcernRole(
      final RelationshipConcernRoleIDKey relationshipConcernRoleIDKey)
      throws AppException, InformationalException {

    final BDMAppealRequestsForConcernRoleDetailsList list =
      new BDMAppealRequestsForConcernRoleDetailsList();

    final BDMOnlineAppealRequest bdmOnlineAppealRequestObj =
      BDMOnlineAppealRequestFactory.newInstance();
    final BDMOnlineAppealRequestKey bdmOnlineAppealRequestKey =
      new BDMOnlineAppealRequestKey();

    final Appeal appealObj =
      curam.appeal.facade.fact.AppealFactory.newInstance();

    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();

    final List<OnlineAppealRequest> onlineAppealRequests =
      this.onlineAppealRequestDAO
        .searchSubmittedRequestByPrimaryAppellant(this.personDAO
          .get(Long.valueOf(relationshipConcernRoleIDKey.concernRoleID)));

    for (final OnlineAppealRequest dtls : onlineAppealRequests) {

      final BDMAppealRequestsForConcernRoleDetails appealRequestsForConcernRoleDetails =
        new BDMAppealRequestsForConcernRoleDetails();

      final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
      final AppealOnlineRequestIDKey appealOnlineRequestIDKey =
        new AppealOnlineRequestIDKey();

      appealOnlineRequestIDKey.onlineAppealRequestID =
        dtls.getID().longValue();

      final AppealOnlineRequestLinkDetails appealOnlineRequestLinkDetails =
        AppealOnlineRequestLinkFactory.newInstance()
          .readAppealCaseIDForRequest(notFoundIndicator,
            appealOnlineRequestIDKey);

      final LocalisableString displayDateMsg = new LocalisableString(
        APPEALREQUESTOBJECT.INF_APPEAL_REQUEST_DATEDESCRIPTION);

      displayDateMsg.arg(dtls.getSubmittedDateTime());
      appealRequestsForConcernRoleDetails.displayText =
        displayDateMsg.toClientFormattedText();

      if (!notFoundIndicator.isNotFound()) {
        final long appealCaseID = appealOnlineRequestLinkDetails.appealCaseID;

        final AppealCaseKey_fo appealCaseKey_fo = new AppealCaseKey_fo();

        appealCaseKey_fo.appealCaseKey.caseID = appealCaseID;

        final String appealTypeCode = appealObj.readAppealType(
          appealCaseKey_fo).appealTypeDetails.appealTypeDetails.appealTypeCode;

        CaseReference caseReference = new CaseReference();
        final CaseSearchKey caseSearchKey = new CaseSearchKey();

        caseSearchKey.caseID = appealCaseID;

        caseReference =
          caseHeaderObj.readCaseReferenceByCaseID(caseSearchKey);

        final LocalisableString appealSummaryMsg = new LocalisableString(
          APPEALREQUESTOBJECT.INF_APPEAL_REQUEST_APPEALSUMMARY);

        appealSummaryMsg.arg(
          new CodeTableItemIdentifier(APPEALTYPE.TABLENAME, appealTypeCode));

        appealSummaryMsg.arg(caseReference.caseReference);
        appealRequestsForConcernRoleDetails.appealDetails =
          appealSummaryMsg.toClientFormattedText();
        appealRequestsForConcernRoleDetails.appealCaseID = appealCaseID;

      }

      appealRequestsForConcernRoleDetails.primaryAppellantRoleID =
        dtls.getPrimaryAppellant().getID().longValue();
      appealRequestsForConcernRoleDetails.primaryAppellantName =
        dtls.getPrimaryAppellant().getName();

      final NotFoundIndicator notFoundIndicator1 = new NotFoundIndicator();

      bdmOnlineAppealRequestKey.onlineAppealRequestID =
        appealOnlineRequestIDKey.onlineAppealRequestID;
      final BDMOnlineAppealRequestDtls onlineAppealRequestDtls =
        bdmOnlineAppealRequestObj.read(notFoundIndicator1,
          bdmOnlineAppealRequestKey);

      if (!notFoundIndicator1.isNotFound()) {
        appealRequestsForConcernRoleDetails.referenceNumber = Long
          .parseLong(onlineAppealRequestDtls.onlineAppealRequestReference);
        appealRequestsForConcernRoleDetails.program =
          onlineAppealRequestDtls.programCode;
      }
      try {
        appealRequestsForConcernRoleDetails.appealUrl =
          dtls.getDocumentURI("Person_listAppealRequests");
      } catch (final NoSuchSchemaException e) {

        final ClientURI personCommunicationPage =
          new ClientURI("Person_listCommunication");

        personCommunicationPage.appendParam("concernRoleID",
          new Long(relationshipConcernRoleIDKey.concernRoleID)

            .toString());
        appealRequestsForConcernRoleDetails.appealUrl =
          personCommunicationPage.getURI();

      }

      appealRequestsForConcernRoleDetails.onlineAppealRequestID =
        appealOnlineRequestIDKey.onlineAppealRequestID;

      list.dtls.add(appealRequestsForConcernRoleDetails);

    }
    return list;

  }

  @Override
  public BDMConcernRoleAttachmentDetailsForList listAppealRequestsAttachments(
    final BDMConcernRoleAppealRequestKey listAttachmentsKey)
    throws AppException, InformationalException {

    final curam.ca.gc.bdm.entity.struct.BDMOnlineAppealRequestAttachmentLinkKeyStruct1 onlineAppealRequestAttachmentLinkKeyStruct1 =
      new curam.ca.gc.bdm.entity.struct.BDMOnlineAppealRequestAttachmentLinkKeyStruct1();
    onlineAppealRequestAttachmentLinkKeyStruct1.onlineAppealRequestID =
      listAttachmentsKey.onlineAppealRequestID;

    final BDMOnlineAppealRequestAttachmentLinkDtlsList list =
      BDMOnlineAppealRequestAttachmentLinkFactory.newInstance()
        .readAllByOnlineAppealRequestID(
          onlineAppealRequestAttachmentLinkKeyStruct1);

    final BDMConcernRoleAttachmentDetailsForList ret =
      new BDMConcernRoleAttachmentDetailsForList();

    for (final BDMOnlineAppealRequestAttachmentLinkDtls onlineAppealRequestAttachmentLinkDtls : list.dtls) {

      ret.bdmCRDetails.add(this.getConcernRoleAttachmentDetails(
        onlineAppealRequestAttachmentLinkDtls.attachmentID,
        listAttachmentsKey.concernRoleID));
    }

    return ret;
  }

  private BDMConcernRoleAttachmentListView getConcernRoleAttachmentDetails(
    final long attachmentID, final long concernroleID)
    throws AppException, InformationalException {

    final Attachment attachmentObj = AttachmentFactory.newInstance();
    final AttachmentKey attachmentKey = new AttachmentKey();

    final ConcernRoleAttachmentLinkReadmultiKey concernRoleAttachmentLinkReadmultiKey =
      new ConcernRoleAttachmentLinkReadmultiKey();
    concernRoleAttachmentLinkReadmultiKey.concernRoleID = concernroleID;

    final ConcernRoleAttachmentLinkDtlsList list =
      ConcernRoleAttachmentLinkFactory.newInstance()
        .searchByConcernRole(concernRoleAttachmentLinkReadmultiKey);

    final BDMConcernRoleAttachmentListView concernRoleAttachmentListView =
      new BDMConcernRoleAttachmentListView();

    for (final ConcernRoleAttachmentLinkDtls concernRoleAttachmentLinkDtls : list.dtls) {

      if (concernRoleAttachmentLinkDtls.attachmentID == attachmentID) {
        concernRoleAttachmentListView.attachmentLinkID =
          concernRoleAttachmentLinkDtls.attachmentLinkID;
        break;
      }
    }

    attachmentKey.attachmentID = attachmentID;
    final AttachmentDtls attachmentDtls = attachmentObj.read(attachmentKey);

    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
    final AttachmentInfo attachmentInfoDtls = AttachmentInfoFactory
      .newInstance().readByAttachmentID(notFoundIndicator, attachmentKey);

    if (!notFoundIndicator.isNotFound()) {
      concernRoleAttachmentListView.fileSource =
        attachmentInfoDtls.sourceSystem;
    }
    concernRoleAttachmentListView.description = attachmentDtls.attachmentName;
    concernRoleAttachmentListView.documentType = attachmentDtls.documentType;
    concernRoleAttachmentListView.dateReceipt = attachmentDtls.receiptDate;
    concernRoleAttachmentListView.dateReceived = attachmentDtls.receiptDate;
    concernRoleAttachmentListView.statusCode = attachmentDtls.statusCode;

    return concernRoleAttachmentListView;
  }

}
