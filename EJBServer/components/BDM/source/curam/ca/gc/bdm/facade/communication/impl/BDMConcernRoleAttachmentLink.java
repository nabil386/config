package curam.ca.gc.bdm.facade.communication.impl;

import com.google.inject.Inject;
import curam.attachment.impl.Attachment;
import curam.ca.gc.bdm.entity.attachment.struct.BDMConcernRoleAttachmentDetailsForList;
import curam.ca.gc.bdm.entity.attachment.struct.BDMConcernRoleAttachmentLinkKey;
import curam.ca.gc.bdm.facade.communication.struct.BDMConcernRoleAttachmentDetails;
import curam.ca.gc.bdm.facade.communication.struct.BDMConcernRoleAttachmentListView;
import curam.ca.gc.bdm.facade.communication.struct.BDMConcernRoleListAttachmentDetails;
import curam.ca.gc.bdm.sl.attachment.fact.BDMConcernRoleAttachmentFactory;
import curam.ca.gc.bdm.sl.attachment.intf.BDMConcernRoleAttachment;
import curam.core.fact.ConcernRoleFactory;
import curam.core.intf.ConcernRole;
import curam.core.sl.fact.ConcernRoleAttachmentFactory;
import curam.core.struct.AttachmentDtls;
import curam.core.struct.AttachmentKey;
import curam.core.struct.ConcernRoleAttachmentLinkDtls;
import curam.core.struct.ConcernRoleAttachmentLinkKey;
import curam.core.struct.ConcernRoleAttachmentLinkReadmultiKey;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.ConcernRoleNameAndAlternateID;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Trace;

public class BDMConcernRoleAttachmentLink extends
  curam.ca.gc.bdm.facade.communication.base.BDMConcernRoleAttachmentLink {

  @Inject
  protected Attachment attachment;

  public BDMConcernRoleAttachmentLink() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Facade method to list concern role attachments given the specified
   * concern role identifier
   *
   * @param listAttachmentsKey contains the concern role identifier
   * @return List of concern role attachments
   */
  @Override
  public BDMConcernRoleAttachmentDetailsForList listConcernRoleAttachments(
    final ConcernRoleAttachmentLinkReadmultiKey listAttachmentsKey)
    throws AppException, InformationalException {

    final BDMConcernRoleAttachment bdmattachmentObj =
      BDMConcernRoleAttachmentFactory.newInstance();

    // Create concern role objects
    final ConcernRole concernRoleObj = ConcernRoleFactory.newInstance();
    // Create and populate structs

    final BDMConcernRoleAttachmentDetailsForList bdmConcernRoleAttachmentDetailsForList =
      new BDMConcernRoleAttachmentDetailsForList();
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

    concernRoleKey.concernRoleID = listAttachmentsKey.concernRoleID;

    // Get list of attachments

    final BDMConcernRoleListAttachmentDetails bdmConcernRoleListAttachmentDetails =
      bdmattachmentObj.searchConcernRoleAttachment(listAttachmentsKey);

    ConcernRoleAttachmentLinkDtls concernRoleAttachmentLinkDtls =
      new ConcernRoleAttachmentLinkDtls();

    BDMConcernRoleAttachmentListView view = null;

    // final ConcernRoleAttachmentLinkKey linkKey;
    AttachmentKey attchKey;
    // final ConcernRoleAttachmentDetails concernRoleAttachmentDetails;
    AttachmentDtls attachmentDtls;
    for (int i =
      0; i < bdmConcernRoleListAttachmentDetails.concernRoleAttchmentDetailsList.attachList
        .size(); ++i) {

      concernRoleAttachmentLinkDtls =
        bdmConcernRoleListAttachmentDetails.concernRoleAttchmentDetailsList.attachList
          .item(i);

      view = new BDMConcernRoleAttachmentListView();
      view.assign(concernRoleAttachmentLinkDtls);
      view.fileSource =
        bdmConcernRoleListAttachmentDetails.dtls.item(i).fileSource;
      // Read attchment details and assign to return struct
      attchKey = new AttachmentKey();
      attchKey.attachmentID = view.attachmentID;
      attachmentDtls = attachment.read(attchKey);

      view.documentType = attachmentDtls.documentType;
      view.dateReceipt = attachmentDtls.receiptDate;
      view.fileLocation = attachmentDtls.fileLocation;
      view.fileReference = attachmentDtls.fileReference;
      bdmConcernRoleAttachmentDetailsForList.bdmCRDetails.addRef(view);

    }
    // Assign alternateID and name
    final ConcernRoleNameAndAlternateID nameAndAltID =
      concernRoleObj.readConcernRoleNameAndAlternateID(concernRoleKey);
    bdmConcernRoleAttachmentDetailsForList.dtls.personName =
      nameAndAltID.concernRoleName;
    bdmConcernRoleAttachmentDetailsForList.dtls.alternateID =
      nameAndAltID.primaryAlternateID;

    return bdmConcernRoleAttachmentDetailsForList;
  }

  /**
   * Facade method to create concern role attachments
   *
   * @param attchmentDetails contains details of concern role attachments
   * @return void
   */
  @Override
  public void createConcernRoleAttachment(
    final BDMConcernRoleAttachmentDetails attchmentDetails)
    throws AppException, InformationalException {

    // Attachment manipulation variables
    final BDMConcernRoleAttachment bdmattachmentObj =
      BDMConcernRoleAttachmentFactory.newInstance();
    bdmattachmentObj.createConcernRoleAttachment(attchmentDetails);

  }

  /**
   * Facade method to read concern role attachments
   *
   * @param linkKey contains attachments link id
   * @return Concern Role Attachment Details for specified link id
   */
  @Override
  public BDMConcernRoleAttachmentDetails
    readConcernRoleAttachment(final BDMConcernRoleAttachmentLinkKey linkKey)
      throws AppException, InformationalException {

    BDMConcernRoleAttachmentDetails bdmConcernRoleAttachmentDetails =
      new BDMConcernRoleAttachmentDetails();

    bdmConcernRoleAttachmentDetails = BDMConcernRoleAttachmentFactory
      .newInstance().readConcernRoleAttachment(linkKey);
    getFileLocationURL(bdmConcernRoleAttachmentDetails);

    return bdmConcernRoleAttachmentDetails;

  }

  /**
   *
   * Get file location information for display
   *
   * @param bdmfaAttachmentDetailsForRead
   */
  private void getFileLocationURL(
    final BDMConcernRoleAttachmentDetails bdmConcernRoleAttachmentDetails) {

    final String fileLocationURL =
      bdmConcernRoleAttachmentDetails.dtls.attachmentDtls.fileLocation.trim();

    String fileName = "";
    String extension = "";

    try {
      fileName =
        fileLocationURL.substring(fileLocationURL.lastIndexOf("\\") + 1);

      final int i = fileName.lastIndexOf('.');
      if (i > 0) {
        extension = fileName.substring(i + 1);

      }

    } catch (final Exception e) {

      Trace.kTopLevelLogger.info(
        fileLocationURL + " is not valid file." + e.getLocalizedMessage());
    }

    fileLocationURL.replace("\\", "/");

    bdmConcernRoleAttachmentDetails.externalFileLink.fileName = fileName;
    bdmConcernRoleAttachmentDetails.externalFileLink.fileLocationURL =
      fileLocationURL;

  }

  // ___________________________________________________________________________
  /**
   * Facade method to cancel the concern role attachment
   *
   * @param linkKey The concern role attachment link identifier
   */
  @Override
  public void
    cancelConcernRoleAttachment(final ConcernRoleAttachmentLinkKey linkKey)
      throws AppException, InformationalException {

    // Create concern role attachment manipulation variables
    final curam.core.sl.intf.ConcernRoleAttachment attachmentLinkObj =
      ConcernRoleAttachmentFactory.newInstance();

    // Call OOTB service layer cancel
    attachmentLinkObj.cancelConcernRoleAttachment(linkKey);

  }

  // ___________________________________________________________________________
  /**
   * Facade method to modify concern role attachments given the specified
   * concern role identifier
   *
   * @param attachmentDetails contains the concern role attachment details
   */
  @Override
  public void modifyConcernRoleAttachment(
    final BDMConcernRoleAttachmentDetails attachmentDetails)
    throws AppException, InformationalException {

    // Call the service layer modify

    final BDMConcernRoleAttachment bdmattachmentObj =
      BDMConcernRoleAttachmentFactory.newInstance();

    bdmattachmentObj.modifyConcernRoleAttachment(attachmentDetails);

  }

}
