package curam.ca.gc.bdm.sl.bdmrfr.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDM_FILE_SOURCE;
import curam.ca.gc.bdm.entity.fact.BDMOnlineAppealRequestAttachmentLinkFactory;
import curam.ca.gc.bdm.entity.fact.BDMOnlineAppealRequestFactory;
import curam.ca.gc.bdm.entity.struct.BDMOnlineAppealRequestAttachmentLinkDtls;
import curam.ca.gc.bdm.entity.struct.BDMOnlineAppealRequestDtls;
import curam.ca.gc.bdm.entity.struct.BDMOnlineAppealRequestKey;
import curam.codetable.DOCUMENTTYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.AttachmentFactory;
import curam.core.fact.AttachmentInfoFactory;
import curam.core.fact.ConcernRoleAttachmentLinkFactory;
import curam.core.impl.CuramConst;
import curam.core.sl.fact.OnlineAppealRequestFactory;
import curam.core.sl.struct.OnlineAppealRequestDtls;
import curam.core.sl.struct.OnlineAppealRequestKey;
import curam.core.struct.AttachmentDtls;
import curam.core.struct.AttachmentInfoDtls;
import curam.core.struct.AttachmentKey;
import curam.core.struct.ConcernRoleAttachmentLinkKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.transaction.TransactionInfo;
import curam.util.type.UniqueID;
import curam.workspaceservices.util.impl.ResourceStoreHelper;
import curam.workspaceservices.workflow.fact.WSAppealsHelperFactory;
import curam.workspaceservices.workflow.struct.ProcessOnlineAppealDetails;

public class BDMWSRFRHelper
  extends curam.ca.gc.bdm.sl.bdmrfr.base.BDMWSRFRHelper {

  @Inject
  private ResourceStoreHelper resourceStoreHelper;

  protected BDMWSRFRHelper() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public void generateOnlineAppealPDF(
    final ProcessOnlineAppealDetails processOnlineAppealDetails)
    throws AppException, InformationalException {

    WSAppealsHelperFactory.newInstance()
      .generateAppealPDF(processOnlineAppealDetails);

    final OnlineAppealRequestKey OnlineAppealRequestKey =
      new OnlineAppealRequestKey();
    OnlineAppealRequestKey.onlineAppealRequestID =
      processOnlineAppealDetails.onlineAppealRequestID;
    final OnlineAppealRequestDtls onlineAppealRequestDtls =
      OnlineAppealRequestFactory.newInstance().read(OnlineAppealRequestKey);

    final BDMOnlineAppealRequestKey bdmOnlineAppealRequestKey =
      new BDMOnlineAppealRequestKey();
    bdmOnlineAppealRequestKey.onlineAppealRequestID =
      processOnlineAppealDetails.onlineAppealRequestID;
    final BDMOnlineAppealRequestDtls bdmOnlineAppealRequestDtls =
      BDMOnlineAppealRequestFactory.newInstance()
        .read(bdmOnlineAppealRequestKey);

    final ConcernRoleAttachmentLinkKey concernRoleAttachmentLinkKey =
      new ConcernRoleAttachmentLinkKey();
    concernRoleAttachmentLinkKey.attachmentLinkID =
      onlineAppealRequestDtls.attachmentID;

    final AttachmentKey attachmentKey = new AttachmentKey();
    attachmentKey.attachmentID = ConcernRoleAttachmentLinkFactory
      .newInstance().read(concernRoleAttachmentLinkKey).attachmentID;

    final AttachmentDtls attachmentDtls =
      AttachmentFactory.newInstance().read(attachmentKey);
    attachmentDtls.attachmentName = this.resourceStoreHelper
      .getPropertyValue("WSAppealsHelper", "pdf.filename") + CuramConst.gkDash
      + bdmOnlineAppealRequestDtls.onlineAppealRequestReference + ".pdf";

    attachmentDtls.documentType = DOCUMENTTYPE.REQUEST_FOR_RECONSIDERATION;

    AttachmentFactory.newInstance().modify(attachmentKey, attachmentDtls);

    final AttachmentInfoDtls attachmentInfoDtls = new AttachmentInfoDtls();
    attachmentInfoDtls.attachmentID = attachmentKey.attachmentID;
    attachmentInfoDtls.attachmentInfoID = UniqueID.nextUniqueID();
    attachmentInfoDtls.recordStatus = RECORDSTATUS.NORMAL;
    attachmentInfoDtls.sourceSystem = BDM_FILE_SOURCE.CLIENT;
    attachmentInfoDtls.createdBy = TransactionInfo.getProgramUser();

    AttachmentInfoFactory.newInstance().insert(attachmentInfoDtls);

    final BDMOnlineAppealRequestAttachmentLinkDtls onlineAppealRequestAttachmentLinkDtls =
      new BDMOnlineAppealRequestAttachmentLinkDtls();
    onlineAppealRequestAttachmentLinkDtls.attachmentID =
      attachmentKey.attachmentID;
    onlineAppealRequestAttachmentLinkDtls.onlineAppealRequestID =
      processOnlineAppealDetails.onlineAppealRequestID;
    onlineAppealRequestAttachmentLinkDtls.onlineAppealRequestAttachmentLinkID =
      UniqueID.nextUniqueID();

    BDMOnlineAppealRequestAttachmentLinkFactory.newInstance()
      .insert(onlineAppealRequestAttachmentLinkDtls);

  }

}
