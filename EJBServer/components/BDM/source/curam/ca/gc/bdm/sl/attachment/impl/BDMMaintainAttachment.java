package curam.ca.gc.bdm.sl.attachment.impl;

import com.google.inject.Inject;
import curam.attachment.impl.Attachment;
import curam.ca.gc.bdm.entity.attachment.fact.BDMCaseAttachmentLinkFactory;
import curam.ca.gc.bdm.entity.attachment.intf.BDMCaseAttachmentLink;
import curam.ca.gc.bdm.entity.attachment.struct.BDMCaseAttachmentLinkDtls;
import curam.ca.gc.bdm.entity.attachment.struct.BDMCaseAttachmentLinkKey;
import curam.ca.gc.bdm.facade.struct.BDMAttachmentDetails;
import curam.ca.gc.bdm.facade.struct.BDMAttachmentDetailsList;
import curam.ca.gc.bdm.facade.struct.BDMCreateCaseAttachmentDetails;
import curam.ca.gc.bdm.facade.struct.BDMModifyCaseAttachmentDetails;
import curam.ca.gc.bdm.facade.struct.BDMReadCaseAttachmentOut;
import curam.ca.gc.bdm.sl.attachment.struct.BDMCaseAttachmentAndLinkDetails;
import curam.ca.gc.bdm.sl.attachment.struct.BDMCaseAttachmentAndLinkDetailsList;
import curam.core.struct.AttachmentCaseID;
import curam.core.struct.AttachmentDetails;
import curam.core.struct.AttachmentDetailsList;
import curam.core.struct.AttachmentDtls;
import curam.core.struct.AttachmentIDAndAttachmentLinkIDStruct;
import curam.core.struct.AttachmentKey;
import curam.core.struct.CaseAttachmentAndLinkDetails;
import curam.core.struct.CaseAttachmentDetailsList;
import curam.core.struct.CaseAttachmentLinkDtls;
import curam.core.struct.CaseAttachmentLinkKey;
import curam.core.struct.ReadCaseAttachmentIn;
import curam.core.struct.ReadCaseAttachmentOut;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;

public class BDMMaintainAttachment
  extends curam.ca.gc.bdm.sl.attachment.base.BDMMaintainAttachment {

  @Inject
  protected Attachment attachment;

  // Add injection for using the new CaseTransactionLog API
  public BDMMaintainAttachment() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Service layer method to create Attchemnt in Case .
   *
   * @param details to create attchment
   * @return AttachmentIDAndAttachmentLinkIDStruct
   */
  @Override
  public AttachmentIDAndAttachmentLinkIDStruct
    insertCaseAttachmentDetails(final BDMCreateCaseAttachmentDetails details)
      throws AppException, InformationalException {

    // Call OOTH Service layer MaintainAttachment
    final curam.core.intf.MaintainAttachment maintainAttachmentObj =
      curam.core.fact.MaintainAttachmentFactory.newInstance();

    final BDMCaseAttachmentLink bdmCaseAttachmentLink =
      BDMCaseAttachmentLinkFactory.newInstance();

    final AttachmentIDAndAttachmentLinkIDStruct attachmentIDAndAttachmentLinkIDStruct =
      maintainAttachmentObj.insertCaseAttachmentDetails(
        details.dtls.createCaseAttachmentDetails);

    final BDMCaseAttachmentLinkDtls bdmCaseAttachmentLinkDtls =
      new BDMCaseAttachmentLinkDtls();

    bdmCaseAttachmentLinkDtls.caseAttachmentLinkID =
      attachmentIDAndAttachmentLinkIDStruct.caseAttachmentLinkID;
    bdmCaseAttachmentLinkDtls.fileSource = details.fileSource;

    // insert into newly created entity
    bdmCaseAttachmentLink.insert(bdmCaseAttachmentLinkDtls);

    return attachmentIDAndAttachmentLinkIDStruct;
  }

  /**
   * Method to return case attachment details.
   *
   * @param key
   * Key to read case attachment details.
   *
   * @return Case attachment details.
   *
   * @throws InformationalException
   * Generic Exception Signature.
   * @throws AppException
   * Generic Exception Signature.
   */
  @Override
  public BDMReadCaseAttachmentOut
    readCaseAttachment(final ReadCaseAttachmentIn key)
      throws AppException, InformationalException {

    // Call OOTH Service layer MaintainAttachment
    final curam.core.intf.MaintainAttachment maintainAttachmentObj =
      curam.core.fact.MaintainAttachmentFactory.newInstance();

    final ReadCaseAttachmentOut out =
      maintainAttachmentObj.readCaseAttachment(key);

    final BDMReadCaseAttachmentOut dtls = new BDMReadCaseAttachmentOut();

    // OTB objext assigned
    dtls.dtls.assign(out);

    final BDMCaseAttachmentLinkKey bdmKey = new BDMCaseAttachmentLinkKey();
    bdmKey.caseAttachmentLinkID = key.caseAttachmentLinkID;

    final BDMCaseAttachmentLink entity =
      BDMCaseAttachmentLinkFactory.newInstance();
    final BDMCaseAttachmentLinkDtls bdmDtls = entity.read(bdmKey);
    dtls.fileSource = bdmDtls.fileSource;

    return dtls;

  }

  /**
   * Operation to modify a Case Attachment.
   *
   * @param details
   * Modified attachment details.
   *
   * @throws InformationalException
   * Generic Exception Signature.
   * @throws AppException
   * Generic Exception Signature.
   */
  @Override
  public void
    modifyCaseAttachment(final BDMModifyCaseAttachmentDetails details)
      throws AppException, InformationalException {

    // Call OOTH Service layer MaintainAttachment
    final curam.core.intf.MaintainAttachment maintainAttachmentObj =
      curam.core.fact.MaintainAttachmentFactory.newInstance();

    maintainAttachmentObj
      .modifyCaseAttachment(details.dtls.modifyAttachmentDetails);

    // Modify custom entity
    final BDMCaseAttachmentLinkKey bdmKey = new BDMCaseAttachmentLinkKey();
    bdmKey.caseAttachmentLinkID =
      details.dtls.modifyAttachmentDetails.caseAttachmentLinkID;

    final BDMCaseAttachmentLink entity =
      BDMCaseAttachmentLinkFactory.newInstance();
    final BDMCaseAttachmentLinkDtls bdmDtls = new BDMCaseAttachmentLinkDtls();
    bdmDtls.caseAttachmentLinkID = bdmKey.caseAttachmentLinkID;
    bdmDtls.fileSource = details.fileSource;

    entity.modify(bdmKey, bdmDtls);

  }

  /**
   * Method to retrieve attachments for the Case.
   *
   * @param attachmentCaseID
   * Attachment Case ID
   *
   * @return List of attachment details
   */
  @Override
  public BDMAttachmentDetailsList
    readCaseAttachments(final AttachmentCaseID attachmentCaseID)
      throws AppException, InformationalException {

    // Call OOTH Service layer MaintainAttachment
    final curam.core.intf.MaintainAttachment maintainAttachmentObj =
      curam.core.fact.MaintainAttachmentFactory.newInstance();

    final BDMCaseAttachmentLink bdmCaseAttachmentLink =
      BDMCaseAttachmentLinkFactory.newInstance();

    final curam.core.intf.CaseAttachmentLink caseAttachmentLinkObj =
      curam.core.fact.CaseAttachmentLinkFactory.newInstance();

    final BDMAttachmentDetailsList returnList =
      new BDMAttachmentDetailsList();

    // final curam.core.intf.Attachment attachmentObj =
    // curam.core.fact.AttachmentFactory.newInstance();
    CaseAttachmentLinkKey caseAttachmentLinkKey;
    CaseAttachmentLinkDtls caseAttachmentLinkDtls =
      new CaseAttachmentLinkDtls();

    final AttachmentDetailsList list =
      maintainAttachmentObj.readCaseAttachments(attachmentCaseID);

    final BDMCaseAttachmentLink entity =
      BDMCaseAttachmentLinkFactory.newInstance();
    BDMCaseAttachmentLinkKey bdmKey;

    AttachmentDetails attachmentDetails;
    BDMAttachmentDetails bdmattachmentDetails;
    AttachmentKey attachmentKey;
    AttachmentDtls attachDtls;
    // final AttachmentDtls attachmentDtls = new AttachmentDtls();
    for (int j = 0; j < list.dtls.size(); j++) {

      attachmentDetails = list.dtls.item(j);
      bdmattachmentDetails = new BDMAttachmentDetails();
      bdmattachmentDetails.attachmentDetails.assign(attachmentDetails);

      // READ from caseAttchmentLink
      caseAttachmentLinkKey = new CaseAttachmentLinkKey();
      caseAttachmentLinkKey.caseAttachmentLinkID =
        attachmentDetails.caseAttachmentLinkID;

      caseAttachmentLinkDtls =
        caseAttachmentLinkObj.read(caseAttachmentLinkKey);
      bdmattachmentDetails.dateReceipt =
        caseAttachmentLinkDtls.attachmentDate;

      bdmKey = new BDMCaseAttachmentLinkKey();

      attachmentKey = new AttachmentKey();
      attachmentKey.attachmentID = attachmentDetails.attachmentID;

      bdmKey.caseAttachmentLinkID = attachmentDetails.caseAttachmentLinkID;
      bdmattachmentDetails.fileSource = entity.read(bdmKey).fileSource;

      attachDtls = new AttachmentDtls();
      attachDtls = attachment.read(attachmentKey);

      bdmattachmentDetails.documentType = attachDtls.documentType;
      bdmattachmentDetails.dateReceipt = attachDtls.receiptDate;

      returnList.dtls.addRef(bdmattachmentDetails);

    }

    return returnList;
  }

  @Override
  public BDMCaseAttachmentAndLinkDetailsList searchIntegCaseAttachments(
    final AttachmentCaseID key) throws AppException, InformationalException {

    // Call OOTH Service layer MaintainAttachment
    final curam.core.intf.MaintainAttachment maintainAttachmentObj =
      curam.core.fact.MaintainAttachmentFactory.newInstance();
    CaseAttachmentDetailsList caseAttachmentDetailsList =
      new CaseAttachmentDetailsList();

    // An element of the returned list
    CaseAttachmentAndLinkDetails caseAttachmentAndLinkDetails;

    final BDMCaseAttachmentAndLinkDetailsList bdmcaseAttachmentDetailsList =
      new BDMCaseAttachmentAndLinkDetailsList();

    BDMCaseAttachmentAndLinkDetails bdmCaseAttachmentAndLinkDetails;
    BDMCaseAttachmentLinkKey bdmKey;
    AttachmentKey attachmentKey;
    final BDMCaseAttachmentLink entity =
      BDMCaseAttachmentLinkFactory.newInstance();

    caseAttachmentDetailsList =
      maintainAttachmentObj.searchIntegCaseAttachments(key);
    AttachmentDtls attachDtls;
    for (int i = 0; i < caseAttachmentDetailsList.dtls.size(); i++) {

      caseAttachmentAndLinkDetails = new CaseAttachmentAndLinkDetails();
      caseAttachmentAndLinkDetails = caseAttachmentDetailsList.dtls.item(i);
      bdmCaseAttachmentAndLinkDetails = new BDMCaseAttachmentAndLinkDetails();

      bdmKey = new BDMCaseAttachmentLinkKey();

      attachmentKey = new AttachmentKey();
      attachmentKey.attachmentID = caseAttachmentAndLinkDetails.attachmentID;
      bdmKey.caseAttachmentLinkID =
        caseAttachmentAndLinkDetails.caseAttachmentLinkID;
      bdmCaseAttachmentAndLinkDetails.dtls
        .assign(caseAttachmentAndLinkDetails);

      attachDtls = new AttachmentDtls();
      attachDtls = attachment.read(attachmentKey);

      bdmCaseAttachmentAndLinkDetails.documentType = attachDtls.documentType;
      bdmCaseAttachmentAndLinkDetails.dateReceipt = attachDtls.receiptDate;

      // Add New fields
      bdmCaseAttachmentAndLinkDetails.fileSource =
        entity.read(bdmKey).fileSource;

      bdmcaseAttachmentDetailsList.dtls
        .addRef(bdmCaseAttachmentAndLinkDetails);

    }

    return bdmcaseAttachmentDetailsList;
  }

}
