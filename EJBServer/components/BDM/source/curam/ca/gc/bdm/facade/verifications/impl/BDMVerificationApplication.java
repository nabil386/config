
package curam.ca.gc.bdm.facade.verifications.impl;

import curam.ca.gc.bdm.codetable.BDMACCEPTANCESTATUS;
import curam.ca.gc.bdm.codetable.BDMATTACHREJECTIONREASON;
import curam.ca.gc.bdm.codetable.impl.BDMATTACHREJECTIONREASONEntry;
import curam.ca.gc.bdm.entity.fact.BDMAttachmentLinkFactory;
import curam.ca.gc.bdm.entity.fact.BDMVerificationAttachmentLinkFactory;
import curam.ca.gc.bdm.entity.fact.BDMVerificationItemProvidedFactory;
import curam.ca.gc.bdm.entity.intf.BDMVerificationAttachmentLink;
import curam.ca.gc.bdm.entity.intf.BDMVerificationItemProvided;
import curam.ca.gc.bdm.entity.struct.BDMAttachmentLinkDtls;
import curam.ca.gc.bdm.entity.struct.BDMAttachmentLinkKey;
import curam.ca.gc.bdm.entity.struct.BDMVerificationAttachmentLinkDtls;
import curam.ca.gc.bdm.entity.struct.BDMVerificationAttachmentLinkKey;
import curam.ca.gc.bdm.entity.struct.BDMVerificationItemProvidedDtls;
import curam.ca.gc.bdm.entity.struct.BDMVerificationItemProvidedKey;
import curam.ca.gc.bdm.facade.verifications.struct.BDMCaseEvidenceVerificationDisplayDetails;
import curam.ca.gc.bdm.facade.verifications.struct.BDMCaseEvidenceVerificationDisplayDetailsList;
import curam.ca.gc.bdm.facade.verifications.struct.BDMCaseIDAndVDIEDLinkIDKey;
import curam.ca.gc.bdm.facade.verifications.struct.BDMCreateVerificationAttachmentDetails;
import curam.ca.gc.bdm.facade.verifications.struct.BDMEvidenceVerificationDetails;
import curam.ca.gc.bdm.facade.verifications.struct.BDMModifyVerificationAttachmentDetails;
import curam.ca.gc.bdm.facade.verifications.struct.BDMNewUserProvidedVerItemAndEvdDetails;
import curam.ca.gc.bdm.facade.verifications.struct.BDMNewUserProvidedVerItemAndWizardDetails;
import curam.ca.gc.bdm.facade.verifications.struct.BDMNewUserProvidedVerificationItemDetails;
import curam.ca.gc.bdm.facade.verifications.struct.BDMReadUserProvidedVerificationItemDetails;
import curam.ca.gc.bdm.facade.verifications.struct.BDMReadVerificationAttachmentDetails;
import curam.ca.gc.bdm.facade.verifications.struct.BDMReadVerificationAttachmentLinkKey;
import curam.ca.gc.bdm.facade.verifications.struct.BDMReadVerificationDetails;
import curam.ca.gc.bdm.facade.verifications.struct.BDMRejectedDocumentDetails;
import curam.ca.gc.bdm.facade.verifications.struct.BDMRejectedDocumentDetailsList;
import curam.ca.gc.bdm.facade.verifications.struct.BDMUserProvidedVerificationItem;
import curam.ca.gc.bdm.facade.verifications.struct.BDMUserProvidedVerificationItemKey;
import curam.ca.gc.bdm.facade.verifications.struct.BDMVerificationAttachmentLinkDetails;
import curam.ca.gc.bdm.sl.struct.BDMUserProvidedVerificationItemDetails;
import curam.ca.gc.bdm.sl.struct.BDMVerificationAttachmentSummaryDetails;
import curam.cefwidgets.docbuilder.impl.ContentPanelBuilder;
import curam.cefwidgets.docbuilder.impl.ImageBuilder;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.codetable.RECORDSTATUS;
import curam.codetable.VERIFICATIONSTATUS;
import curam.codetable.VERIFICATIONTYPE;
import curam.core.facade.fact.ConcernRoleFactory;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.sl.entity.struct.CountDetails;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.core.sl.infrastructure.entity.struct.ReadRelatedIDParticipantIDAndEvidenceTypeDetails;
import curam.core.sl.pods.impl.PodsConst;
import curam.core.sl.struct.ParticipantKeyStruct;
import curam.core.sl.struct.WizardStateID;
import curam.core.struct.AttachmentDtls;
import curam.core.struct.CaseKey;
import curam.message.BDMENTVERIFICATION;
import curam.message.BPOATTACHMENTLINK;
import curam.message.verification.VERIFICATIONDETAILSPANEL;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.LocalisableString;
import curam.util.exception.RecordNotFoundException;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.NotFoundIndicator;
import curam.util.type.ValueList;
import curam.verification.facade.infrastructure.fact.VerificationApplicationFactory;
import curam.verification.facade.infrastructure.struct.CaseEvidenceVerificationDisplayDetails;
import curam.verification.facade.infrastructure.struct.CaseEvidenceVerificationDisplayDetailsList;
import curam.verification.facade.infrastructure.struct.CreateVerificationAttachmentDetails;
import curam.verification.facade.infrastructure.struct.EvidenceVerificationDetails;
import curam.verification.facade.infrastructure.struct.NewUserProvidedVerItemAndWizardDetails;
import curam.verification.facade.infrastructure.struct.ReadUserProvidedVerificationItemDetails;
import curam.verification.facade.infrastructure.struct.ReadVerificationAttachmentDetails;
import curam.verification.facade.infrastructure.struct.ReadVerificationAttachmentLinkKey;
import curam.verification.facade.infrastructure.struct.ReadVerificationDetails;
import curam.verification.facade.infrastructure.struct.UserProvidedVerificationItem;
import curam.verification.facade.infrastructure.struct.UserProvidedVerificationItemKey;
import curam.verification.facade.infrastructure.struct.VerificationAttachmentLinkKey;
import curam.verification.impl.VerificationConstant;
import curam.verification.sl.entity.fact.VerificationItemFactory;
import curam.verification.sl.entity.struct.VerificationItemKey;
import curam.verification.sl.entity.struct.VerificationItemUtilizationDtls;
import curam.verification.sl.infrastructure.entity.fact.VDIEDLinkFactory;
import curam.verification.sl.infrastructure.entity.fact.VerificationAttachmentLinkFactory;
import curam.verification.sl.infrastructure.entity.fact.VerificationFactory;
import curam.verification.sl.infrastructure.entity.fact.VerificationItemProvidedFactory;
import curam.verification.sl.infrastructure.entity.intf.VDIEDLink;
import curam.verification.sl.infrastructure.entity.intf.VerificationItemProvided;
import curam.verification.sl.infrastructure.entity.struct.ItemProvidedIDAndVDIEDLinkIDDetails;
import curam.verification.sl.infrastructure.entity.struct.ReadByVerLinkedIDTypeAndStatusKey;
import curam.verification.sl.infrastructure.entity.struct.ReadVerificationStatusLinkedIDandTypeKey;
import curam.verification.sl.infrastructure.entity.struct.StatusAndVDIEDLinkIDDetails;
import curam.verification.sl.infrastructure.entity.struct.VDIEDLinkDtls;
import curam.verification.sl.infrastructure.entity.struct.VDIEDLinkKey;
import curam.verification.sl.infrastructure.entity.struct.VerificationAndVDIEDLinkDetailsList;
import curam.verification.sl.infrastructure.entity.struct.VerificationAttachmentLinkDtls;
import curam.verification.sl.infrastructure.entity.struct.VerificationAttachmentSummaryDetails;
import curam.verification.sl.infrastructure.entity.struct.VerificationAttachmentSummaryDetailsList;
import curam.verification.sl.infrastructure.entity.struct.VerificationDtls;
import curam.verification.sl.infrastructure.entity.struct.VerificationDtlsList;
import curam.verification.sl.infrastructure.entity.struct.VerificationItemProvidedDtls;
import curam.verification.sl.infrastructure.entity.struct.VerificationItemProvidedDtlsList;
import curam.verification.sl.infrastructure.entity.struct.VerificationItemProvidedKey;
import curam.verification.sl.infrastructure.entity.struct.VerificationItemProvidedListDetails;
import curam.verification.sl.infrastructure.entity.struct.VerificationItemProvidedListDetailsList;
import curam.verification.sl.infrastructure.entity.struct.VerificationKey;
import curam.verification.sl.infrastructure.entity.struct.VerificationStatusDetails;
import curam.verification.sl.infrastructure.fact.OutstandingVerificationAttachmentLinkFactory;
import curam.verification.sl.infrastructure.impl.VerificationConfigurationCacheUtils;
import curam.verification.sl.infrastructure.intf.OutstandingVerificationAttachmentLink;
import curam.verification.sl.infrastructure.struct.AttachmentLinkID;
import curam.verification.sl.infrastructure.struct.CaseEvidenceVerificationDetails;
import curam.verification.sl.infrastructure.struct.CaseEvidenceVerificationDetailsList;
import curam.verification.sl.infrastructure.struct.CaseIDAndVDIEDLinkIDKey;
import curam.verification.sl.infrastructure.struct.CreateVerificationAttachmentLinkDetails;
import curam.verification.sl.infrastructure.struct.OutstandingIndicator;
import curam.verification.sl.infrastructure.struct.ReadVerificationItemProvidedDetails;
import curam.verification.sl.infrastructure.struct.SubmittedDocumentDetailsList;
import curam.verification.sl.infrastructure.struct.UserProvidedVerificationItemDetails;
import curam.verification.sl.struct.VerificationMessage;
import curam.wizardpersistence.impl.WizardPersistentState;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author amod.gole
 *
 * 9408 06-03-2022 Fetch the Verification Item List for the Applicatio Case
 *
 * @version 1.1 - 23374 Task - Agent portal verification changes By Siva
 * 04/29/2022
 * Customizing the facade methods -
 * newUserProvidedVerificationItem(),fetchVerificationDetails(),editUserProvidedVerificationItem()
 * @version 1.1 - 23374 Task - Agent portal verification changes By Siva
 * 05/09/2022
 * Customizing the facade methods -
 * createVerificationAttachment(),modifyVerificationAttachment(),readVerificationAttachment(),readUserProvidedVerificationItem()
 * @version 1.3 Altered for 56003 Dev bug 55654 to fix Validation changes
 * @version 1.3 Altered for 56110-56153-Tasks-Dev-bugs-54712-55629-Verification
 * by Siva 06/13/2022
 * @version 1.4 Altered
 * rejectAllDocumentsPendingReviewForVerification(),rejectDocumentPendingReviewForVerification
 * for 54845-54846 Tasks Dev bugs 54840-54842 Verification by Siva 06/14/2022
 *
 */
public class BDMVerificationApplication extends
  curam.ca.gc.bdm.facade.verifications.base.BDMVerificationApplication {

  @Override
  public void acceptAllDocumentsPendingReviewForVerification(
    final BDMCaseIDAndVDIEDLinkIDKey input)
    throws AppException, InformationalException {

    // BEGIN - the OOTB code does not work for participant type verification,
    // below code
    // would work for both case type verification and participant type
    // verification.
    final CaseIDAndVDIEDLinkIDKey caseIDAndVDIEDLinkIDKey =
      new CaseIDAndVDIEDLinkIDKey();
    caseIDAndVDIEDLinkIDKey.vdiedLinkID =
      input.caseIDAndVDIEDLinkIDKeyDtls.vdiedLinkID;

    final VDIEDLinkKey vdiedLinkKey = new VDIEDLinkKey();

    vdiedLinkKey.VDIEDLinkID = caseIDAndVDIEDLinkIDKey.vdiedLinkID;
    final curam.verification.sl.infrastructure.entity.intf.Verification verification =
      curam.verification.sl.infrastructure.entity.fact.VerificationFactory
        .newInstance();
    final VerificationDtlsList verificationDtlsList =
      verification.readByVDIEDLinkID(vdiedLinkKey);
    if (verificationDtlsList.dtls.size() > 0) {
      caseIDAndVDIEDLinkIDKey.caseID =
        verificationDtlsList.dtls.item(0).verificationLinkedID;
    }
    // END
    final SubmittedDocumentDetailsList documentsList =
      curam.verification.sl.infrastructure.fact.VerificationApplicationFactory
        .newInstance().listSubmittedDocuments(caseIDAndVDIEDLinkIDKey);

    for (final curam.verification.sl.infrastructure.struct.SubmittedDocumentDetails documentDetails : documentsList.dtls) {
      final AttachmentLinkID linkID = new AttachmentLinkID();
      linkID.attachmentLinkID = documentDetails.attachmentLinkID;
      acceptExistingDocumentAsProof(linkID);
    }

  }

  final long kInternalVerificationID = 80001;

  /**
   * BGIN Task 9408 Add new columns to Verification Item List on Application
   * Case
   * Method to fetch verification details
   *
   * @param CaseKey
   * @return BDMCaseEvidenceVerificationDisplayDetailsList
   *
   */

  @Override

  public BDMCaseEvidenceVerificationDisplayDetailsList

    listVerificationDetailsforCaseEvidence1(final CaseKey key)
      throws AppException, InformationalException {

    final BDMCaseEvidenceVerificationDisplayDetailsList bdmCaseEvidenceVerificationDisplayDetailsList =
      new BDMCaseEvidenceVerificationDisplayDetailsList();

    // Fetch the Verification List from Super Class
    final CaseEvidenceVerificationDisplayDetailsList caseEvidenceVerificationDisplayDetailsList =
      super.listVerificationDetailsforCaseEvidence(key);

    final OutstandingVerificationAttachmentLink outstandingVerificationAttachmentLinkObj =
      OutstandingVerificationAttachmentLinkFactory.newInstance();

    for (final CaseEvidenceVerificationDisplayDetails caseEvdVeriDisplayDetails : caseEvidenceVerificationDisplayDetailsList.dtls) {

      final BDMCaseEvidenceVerificationDisplayDetails bdmCaseEvidenceVerificationDisplayDetails =
        new BDMCaseEvidenceVerificationDisplayDetails();

      // BEGIN : Task 27331 set documentSubmitted ind for Participant Type
      // verification
      // retrieve the verificationID for participant type
      // verification, which is missed in OOTB
      if (caseEvdVeriDisplayDetails.verificationID == 0L) {

        final VDIEDLinkKey vdiedLinkKey = new VDIEDLinkKey();

        vdiedLinkKey.VDIEDLinkID = caseEvdVeriDisplayDetails.vDIEDLinkID;
        final curam.verification.sl.infrastructure.entity.intf.Verification verification =
          curam.verification.sl.infrastructure.entity.fact.VerificationFactory
            .newInstance();
        final VerificationDtlsList verificationDtlsList =
          verification.readByVDIEDLinkID(vdiedLinkKey);
        if (verificationDtlsList.dtls.size() > 0) {
          caseEvdVeriDisplayDetails.verificationID =
            verificationDtlsList.dtls.item(0).verificationID;
        }

        if (caseEvdVeriDisplayDetails.verificationID != 0L) {
          final VerificationKey verificationKey = new VerificationKey();
          verificationKey.verificationID =
            caseEvdVeriDisplayDetails.verificationID;
          final CountDetails documentsSubmittedCount =
            outstandingVerificationAttachmentLinkObj
              .countPendingReviewVerificationAttachments(verificationKey);
          final boolean documentSubmittedOpt =
            documentsSubmittedCount.numberOfRecords > 0 ? true : false;
          caseEvdVeriDisplayDetails.verifiableDataItemNameXMLOpt =
            getVerifiableDataItemDetails(
              caseEvdVeriDisplayDetails.verifiableDataItemName,
              documentSubmittedOpt).toString();
        }
      }
      // END : Task 27331 set documentSubmitted ind for Participant Type
      // verification

      bdmCaseEvidenceVerificationDisplayDetails.dtls
        .assign(caseEvdVeriDisplayDetails);

      final curam.verification.facade.infrastructure.struct.EvidenceVerificationDetails evdVerifDetails =
        new EvidenceVerificationDetails();
      evdVerifDetails.evidenceVerificationDetails.caseID = key.caseID;
      evdVerifDetails.evidenceVerificationDetails.dataItemName =
        bdmCaseEvidenceVerificationDisplayDetails.dtls.verifiableDataItemName;
      evdVerifDetails.evidenceVerificationDetails.evidenceDescriptorID =
        bdmCaseEvidenceVerificationDisplayDetails.dtls.evidenceDescriptorID;

      evdVerifDetails.evidenceVerificationDetails.summary =
        bdmCaseEvidenceVerificationDisplayDetails.dtls.summary;
      evdVerifDetails.evidenceVerificationDetails.vdIEDLinkID =
        bdmCaseEvidenceVerificationDisplayDetails.dtls.vDIEDLinkID;
      evdVerifDetails.evidenceVerificationDetails.verificationLinkedID =
        key.caseID;
      evdVerifDetails.evidenceVerificationDetails.verificationStatus =
        bdmCaseEvidenceVerificationDisplayDetails.dtls.verificationStatus;
      // Fetch the Verification DEtails for each verification item from super
      // class
      final ReadVerificationDetails readVerificationDetails =
        fetchVerificationDetails(evdVerifDetails);
      final Iterator<UserProvidedVerificationItemDetails> iterator =
        readVerificationDetails.readVerificationDetails.userProvidedVerificationItemDetailsList.userProvidedVerificationItemDetails
          .iterator();
      // Iterate on each Item details
      while (iterator.hasNext()) {
        final UserProvidedVerificationItemDetails userProvidedVerificationItemDetails =
          iterator.next();
        final UserProvidedVerificationItem userProvidedVerificationItem =
          new UserProvidedVerificationItem();
        userProvidedVerificationItem.userProvidedVerificationItem.verificationItemProvidedID =
          userProvidedVerificationItemDetails.verificationItemProvidedID;
        final ReadUserProvidedVerificationItemDetails readUserProvidedVerificationItemDetails =
          readUserProvidedVerificationItem(userProvidedVerificationItem);

        // Get the latest received Date Item for display
        if (bdmCaseEvidenceVerificationDisplayDetails.dateReceived != null) {
          if (readUserProvidedVerificationItemDetails.readUserProvidedVerificationItemDetails.userProvidedVerificationInfo.dateReceived
            .after(bdmCaseEvidenceVerificationDisplayDetails.dateReceived)) {
            bdmCaseEvidenceVerificationDisplayDetails.dateAdded =
              readUserProvidedVerificationItemDetails.readUserProvidedVerificationItemDetails.userProvidedVerificationInfo.dateAdded;
            bdmCaseEvidenceVerificationDisplayDetails.dateReceived =
              readUserProvidedVerificationItemDetails.readUserProvidedVerificationItemDetails.userProvidedVerificationInfo.dateReceived;
            bdmCaseEvidenceVerificationDisplayDetails.documentProvidedBy =
              readUserProvidedVerificationItemDetails.readUserProvidedVerificationItemDetails.userProvidedVerificationInfo.addedByUser;
            bdmCaseEvidenceVerificationDisplayDetails.documentType =
              readUserProvidedVerificationItemDetails.readUserProvidedVerificationItemDetails.itemProvidedMandatoryAndLevelDetails.verificationItemName;

          }

        } else {
          bdmCaseEvidenceVerificationDisplayDetails.dateAdded =
            readUserProvidedVerificationItemDetails.readUserProvidedVerificationItemDetails.userProvidedVerificationInfo.dateAdded;
          bdmCaseEvidenceVerificationDisplayDetails.dateReceived =
            readUserProvidedVerificationItemDetails.readUserProvidedVerificationItemDetails.userProvidedVerificationInfo.dateReceived;
          bdmCaseEvidenceVerificationDisplayDetails.documentProvidedBy =
            readUserProvidedVerificationItemDetails.readUserProvidedVerificationItemDetails.userProvidedVerificationInfo.addedByUser;
          bdmCaseEvidenceVerificationDisplayDetails.documentType =
            readUserProvidedVerificationItemDetails.readUserProvidedVerificationItemDetails.itemProvidedMandatoryAndLevelDetails.verificationItemName;
        }
      }

      bdmCaseEvidenceVerificationDisplayDetailsList.dtls
        .add(bdmCaseEvidenceVerificationDisplayDetails);
    }

    return bdmCaseEvidenceVerificationDisplayDetailsList;
  }
  // END TASK 9408
  // START 23374 Task - Agent portal verification changes

  @Override
  public BDMUserProvidedVerificationItemKey newUserProvidedVerificationItem(
    final BDMNewUserProvidedVerificationItemDetails details)
    throws AppException, InformationalException {

    /**
     * Step 1 Validation
     */

    validateVerificationItemDetails(details);

    // TASK 53251 - file attachment validation if file is uploaded
    if (!details.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachmentName
      .isEmpty()) {
      validateAttachment(
        details.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls);
    }

    /**
     * Step 2 Internal check and swapping the verificationItemUtilizationID
     * internally to stop the verifying process and maintain a internal dummy
     * verificationItemUtilizationID code
     * accordingly
     */
    final long userProvidedVerificationItemUtilizationID =
      details.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.verificationItemUtilizationID;

    // Task 69390 : Verification Item should not be verified when acceptance
    // status in blank or Rejected
    if (BDMACCEPTANCESTATUS.BDMVIS8001.equals(details.acceptanceStatus)
      || StringUtil.isNullOrEmpty(details.acceptanceStatus)) {
      details.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.verificationItemUtilizationID =
        kInternalVerificationID;
    }

    /**
     * Step 3 Customized to return VerificationAttachmentLinkID
     * newUserProvidedVerificationItem() method is called to do all
     * the tasks .
     */
    final BDMVerificationApplicationHelper verificationApplicationHelper =
      new BDMVerificationApplicationHelper();

    final BDMUserProvidedVerificationItemKey bdmUserProvidedVerificationItemKey =
      verificationApplicationHelper.newUserProvidedVerificationItem(
        details.newUserProvidedVerificationItemDetails);

    final long verificationItemProvidedID =
      bdmUserProvidedVerificationItemKey.bdmUserProvidedVerificationItemKey.userProvidedVerificationItemKey.verificationItemProvidedID;

    /**
     * Step 4 Inserts the 2 fields in to BDMVerificationItemProvided.
     */
    if (BDMACCEPTANCESTATUS.BDMVIS8001.equals(details.acceptanceStatus)
      || StringUtil.isNullOrEmpty(details.acceptanceStatus)) {
      insertBDMVerificationItemProvided(verificationItemProvidedID,
        userProvidedVerificationItemUtilizationID);
    }

    /**
     * Step 5 Inserts the 3 new fields in to the new entity .
     */
    details.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.verificationItemUtilizationID =
      userProvidedVerificationItemUtilizationID;

    // Task 63907 202207-21 Link Rejected verification item user provided with
    // attachment created
    // Link is not happening in OOTB code. So this custom logic is written for
    // 'When user adds a Proof but the acceptance status is Rejected'
    long verificationAtachmentLinkID = 0l;
    if (bdmUserProvidedVerificationItemKey.verificationAtachmentLinkID == 0
      && bdmUserProvidedVerificationItemKey.attachmentID != 0) {

      final curam.verification.sl.infrastructure.entity.intf.VerificationAttachmentLink verificationAttachmentLink =
        curam.verification.sl.infrastructure.entity.fact.VerificationAttachmentLinkFactory
          .newInstance();
      final VerificationAttachmentLinkKey verificationAttachmentLinkKey =
        new VerificationAttachmentLinkKey();

      final BDMVerificationItemProvided bdmVerificationItemProvidedObj =
        BDMVerificationItemProvidedFactory.newInstance();
      final BDMVerificationItemProvidedKey bdmVerificationItemProvidedKey =
        new BDMVerificationItemProvidedKey();
      bdmVerificationItemProvidedKey.verificationItemProvidedID =
        verificationItemProvidedID;
      try {
        final BDMVerificationItemProvidedDtls bdmVerificationItemProvidedDtls =
          bdmVerificationItemProvidedObj.read(bdmVerificationItemProvidedKey);

        final VerificationItemUtilizationDtls verificationItemUtilizationDtls1 =
          VerificationConfigurationCacheUtils
            .getVerificationItemUtilizationDtls(
              bdmVerificationItemProvidedDtls.verificationItemUtilizationID);

        final VDIEDLink vdiedLink = VDIEDLinkFactory.newInstance();
        final VDIEDLinkKey vDIEDLinkKey = new VDIEDLinkKey();
        vDIEDLinkKey.VDIEDLinkID =
          details.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.VDIEDLinkID;
        final VDIEDLinkDtls vdiedLinkDtls = vdiedLink.read(vDIEDLinkKey);

        final Set<VerificationItemUtilizationDtls> verificationItemUtilizationDtlsSet =
          VerificationConfigurationCacheUtils
            .fetchVerificationItemUtilizationDtlsList(
              vdiedLinkDtls.verifiableDataItemID,
              verificationItemUtilizationDtls1.verificationItemID);

        final VerificationItemProvided verificationItemProvided =
          VerificationItemProvidedFactory.newInstance();

        for (final VerificationItemUtilizationDtls verificationItemUtilizationUsageDetails : verificationItemUtilizationDtlsSet) {

          // BEGIN, CR00414157, GK
          final ItemProvidedIDAndVDIEDLinkIDDetails itemProvidedIDAndVDIEDLinkIDDetails =
            new ItemProvidedIDAndVDIEDLinkIDDetails();

          itemProvidedIDAndVDIEDLinkIDDetails.verificationItemUtilizationID =
            verificationItemUtilizationUsageDetails.verificationItemUtilizationID;
          itemProvidedIDAndVDIEDLinkIDDetails.VDIEDLinkID =
            vDIEDLinkKey.VDIEDLinkID;
          itemProvidedIDAndVDIEDLinkIDDetails.recordStatus =
            RECORDSTATUS.NORMAL;
          final VerificationItemProvidedDtlsList verificationItemProvidedDtlsList =
            verificationItemProvided
              .searchItemProvidedByItemUtilizationIDAndVDIEDLinkID(
                itemProvidedIDAndVDIEDLinkIDDetails);
          CreateVerificationAttachmentDetails createVerificationAttachmentDetails;

          createVerificationAttachmentDetails =
            new CreateVerificationAttachmentDetails();
          final VerificationAttachmentLinkDtls createVerificationAttachmentLinkDetails =
            new VerificationAttachmentLinkDtls();
          createVerificationAttachmentLinkDetails.attachmentID =
            bdmUserProvidedVerificationItemKey.attachmentID;
          createVerificationAttachmentLinkDetails.verificationItemProvidedID =
            verificationItemProvidedID;
          createVerificationAttachmentLinkDetails.recordStatus =
            RECORDSTATUS.DEFAULTCODE;
          createVerificationAttachmentLinkDetails.description =
            details.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createLinkDtls.description;

          verificationAttachmentLink
            .insert(createVerificationAttachmentLinkDetails);

          verificationAtachmentLinkID =
            createVerificationAttachmentLinkDetails.verificationAttachmentLinkID;
          // END, CR00414157
          // }
          bdmUserProvidedVerificationItemKey.verificationAtachmentLinkID =
            verificationAtachmentLinkID;

        }

      } catch (final RecordNotFoundException ex) {

      }

    }

    if (bdmUserProvidedVerificationItemKey.verificationAtachmentLinkID != 0) {
      final BDMVerificationAttachmentLinkDtls bdmVerificationAttachmentLinkDtls =
        new BDMVerificationAttachmentLinkDtls();
      bdmVerificationAttachmentLinkDtls.acceptanceStatus =
        details.acceptanceStatus;
      bdmVerificationAttachmentLinkDtls.otherComments = details.otherComments;
      bdmVerificationAttachmentLinkDtls.rejectionReason =
        details.rejectionReason;

      bdmVerificationAttachmentLinkDtls.verificationAttachmentLinkID =
        bdmUserProvidedVerificationItemKey.verificationAtachmentLinkID;

      insertBDMVerificationAttachmentLink(bdmVerificationAttachmentLinkDtls);
    }

    /**
     * All the rest of the OOTB steps followed.
     */

    // BEGIN, CR00400972, RPB
    final String[] messages =
      TransactionInfo.getInformationalManager().obtainInformationalAsString();

    for (int i = 0; i != messages.length; i++) {
      final VerificationMessage warning = new VerificationMessage();

      warning.message = messages[i];
      bdmUserProvidedVerificationItemKey.bdmUserProvidedVerificationItemKey.infoMsgListOpt
        .addRef(warning);
    }
    // END, CR00400972

    return bdmUserProvidedVerificationItemKey;
  }

  /**
   * @param details
   * @param actionIDProperty
   * @param wizardStateID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public NewUserProvidedVerItemAndWizardDetails
    validateBulkEvidenceVerificationAddProofPage(
      final curam.verification.facade.infrastructure.struct.NewUserProvidedVerificationItemDetails details,
      final ActionIDProperty actionIDProperty,
      final curam.core.sl.struct.WizardStateID wizardStateID)
      throws curam.util.exception.AppException,
      curam.util.exception.InformationalException {

    // TASK 53251 - file attachment validation if file is uploaded
    if (!details.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachmentName
      .isEmpty()) {
      validateAttachment(
        details.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls);
    }

    final NewUserProvidedVerItemAndWizardDetails newUserProvidedVerItemAndWizardDetails =
      new NewUserProvidedVerItemAndWizardDetails();

    newUserProvidedVerItemAndWizardDetails.newUserProvdedVerificationItemDetails =
      details;
    final curam.core.facade.struct.ConcernRoleIDKey concernRoleIDKey =
      new curam.core.facade.struct.ConcernRoleIDKey();

    if (0 != details.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.participantConcernRoleID) {
      concernRoleIDKey.concernRoleID =
        details.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.participantConcernRoleID;
      details.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.participantNameOpt =
        ConcernRoleFactory.newInstance()
          .readConcernRoleName(concernRoleIDKey).concernRoleName;
    }
    curam.verification.sl.infrastructure.fact.VerificationApplicationFactory
      .newInstance().validateBulkEvidenceVerificationAddProofPage(details);
    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    if (0 == wizardStateID.wizardStateID) {
      wizardStateID.wizardStateID =
        wizardPersistentState.create(newUserProvidedVerItemAndWizardDetails);
    } else {

      wizardPersistentState.modify(wizardStateID.wizardStateID,
        newUserProvidedVerItemAndWizardDetails);
    }
    newUserProvidedVerItemAndWizardDetails.wizardStateID = wizardStateID;
    return newUserProvidedVerItemAndWizardDetails;
  }

  /**
   *
   * @param BDMUserProvidedVerificationItemKey
   * bdmUserProvidedVerificationItemKey
   * @param BDMNewUserProvidedVerificationItemDetails details
   * @throws AppException
   * @throws InformationalException
   */
  public void insertBDMVerificationAttachmentLink(
    final BDMVerificationAttachmentLinkDtls bdmVerificationAttachmentLinkDtls)

    throws AppException, InformationalException {

    BDMVerificationAttachmentLinkFactory.newInstance()
      .insert(bdmVerificationAttachmentLinkDtls);

  }

  /**
   *
   * @param verificationItemProvidedID
   * @param verificationItemUtilizationID
   * @throws AppException
   * @throws InformationalException
   */
  public void insertBDMVerificationItemProvided(
    final long verificationItemProvidedID,
    final long verificationItemUtilizationID)

    throws AppException, InformationalException {

    final BDMVerificationItemProvidedDtls bdmVerificationItemProvidedDtls =
      new BDMVerificationItemProvidedDtls();
    bdmVerificationItemProvidedDtls.verificationItemProvidedID =
      verificationItemProvidedID;
    bdmVerificationItemProvidedDtls.verificationItemUtilizationID =
      verificationItemUtilizationID;
    BDMVerificationItemProvidedFactory.newInstance()
      .insert(bdmVerificationItemProvidedDtls);
  }

  /**
   *
   * @param verificationItemProvidedID
   * @param verificationItemUtilizationID
   * @throws AppException
   * @throws InformationalException
   */
  public void createIfBDMverificationItemProvidedNotFound(
    final long verificationItemProvidedID)
    throws AppException, InformationalException {

    final BDMVerificationItemProvidedKey bdmVerificationItemProvidedKey =
      new BDMVerificationItemProvidedKey();
    bdmVerificationItemProvidedKey.verificationItemProvidedID =
      verificationItemProvidedID;
    BDMVerificationItemProvidedDtls bdmVerificationItemProvidedDtls =
      new BDMVerificationItemProvidedDtls();
    final NotFoundIndicator nFoundInd = new NotFoundIndicator();

    bdmVerificationItemProvidedDtls = BDMVerificationItemProvidedFactory
      .newInstance().read(nFoundInd, bdmVerificationItemProvidedKey);

    if (nFoundInd.isNotFound()) {
      final VerificationItemProvidedKey verificationItemProvidedKey =
        new VerificationItemProvidedKey();
      verificationItemProvidedKey.verificationItemProvidedID =
        verificationItemProvidedID;
      final ReadVerificationItemProvidedDetails readVerificationItemProvidedDetails =
        curam.verification.sl.infrastructure.fact.VerificationItemProvidedFactory
          .newInstance()
          .readVerificationItemProvided(verificationItemProvidedKey);
      insertBDMVerificationItemProvided(verificationItemProvidedID,
        readVerificationItemProvidedDetails.readDtls.verificationItemUtilizationID);
    }

  }

  /**
   * This method is to read from BDMVerificationItemProvided
   *
   * @param verificationItemProvidedID
   * @param verificationItemUtilizationID
   * @throws AppException
   * @throws InformationalException
   */
  public BDMVerificationItemProvidedDtls
    getUserProvidedverificationItemUtilizationID(
      final long verificationItemProvidedID)

      throws AppException, InformationalException {

    final BDMVerificationItemProvidedKey bdmVerificationItemProvidedKey =
      new BDMVerificationItemProvidedKey();
    bdmVerificationItemProvidedKey.verificationItemProvidedID =
      verificationItemProvidedID;
    BDMVerificationItemProvidedDtls bdmVerificationItemProvidedDtls =
      new BDMVerificationItemProvidedDtls();
    final NotFoundIndicator nFoundInd = new NotFoundIndicator();

    bdmVerificationItemProvidedDtls = BDMVerificationItemProvidedFactory
      .newInstance().read(nFoundInd, bdmVerificationItemProvidedKey);

    if (nFoundInd.isNotFound()) {
      bdmVerificationItemProvidedDtls = new BDMVerificationItemProvidedDtls();
      final VerificationItemProvidedKey verificationItemProvidedKey =
        new VerificationItemProvidedKey();
      verificationItemProvidedKey.verificationItemProvidedID =
        verificationItemProvidedID;
      final VerificationItemProvidedDtls verificationItemProvidedDtls =
        VerificationItemProvidedFactory.newInstance()
          .read(verificationItemProvidedKey);
      bdmVerificationItemProvidedDtls.verificationItemUtilizationID =
        verificationItemProvidedDtls.verificationItemUtilizationID;
    }
    return bdmVerificationItemProvidedDtls;
  }

  /**
   *
   * @param bdmUserProvidedVerificationItemKey
   * @param details
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public BDMVerificationAttachmentLinkDtls
    readBDMVerificationAttachmentLink(final long verificationAttachmentLinkID)
      throws AppException, InformationalException {

    final BDMVerificationAttachmentLinkKey bdmVerificationAttachmentLinkKey =
      new BDMVerificationAttachmentLinkKey();
    bdmVerificationAttachmentLinkKey.verificationAttachmentLinkID =
      verificationAttachmentLinkID;

    BDMVerificationAttachmentLinkDtls bdmVerificationAttachmentLinkDtls =
      new BDMVerificationAttachmentLinkDtls();

    if (verificationAttachmentLinkID != 0) {
      final NotFoundIndicator nFIndicator = new NotFoundIndicator();
      bdmVerificationAttachmentLinkDtls = BDMVerificationAttachmentLinkFactory
        .newInstance().read(nFIndicator, bdmVerificationAttachmentLinkKey);
      if (nFIndicator.isNotFound()) {
        bdmVerificationAttachmentLinkDtls =
          new BDMVerificationAttachmentLinkDtls();
      }
    }

    return bdmVerificationAttachmentLinkDtls;
  }

  @Override
  public BDMReadVerificationDetails
    fetchVerificationDetails(final BDMEvidenceVerificationDetails dtls)
      throws AppException, InformationalException {

    final BDMReadVerificationDetails bdmReadVerificationDetails =
      new BDMReadVerificationDetails();
    final EvidenceVerificationDetails evidenceVerificationDetails =
      new EvidenceVerificationDetails();
    evidenceVerificationDetails.evidenceVerificationDetails =
      dtls.bdmDtls.evidenceVerificationDetails;

    final ReadVerificationDetails readVerificationDetails =
      curam.verification.facade.infrastructure.fact.VerificationApplicationFactory
        .newInstance().fetchVerificationDetails(evidenceVerificationDetails);

    final String paramOverAllVerificationStatus =
      readVerificationDetails.readVerificationDetails.verificationRequirementSummaryDetailsList
        .get(0).verificationStatus;
    final long paramOverAllVerificationID =
      readVerificationDetails.readVerificationDetails.verificationRequirementSummaryDetailsList
        .get(0).verificationID;

    // BEGIN : Task 27331 retrieve document list for Participant Type
    // verification
    final curam.verification.sl.infrastructure.intf.VerificationApplication verificationApplicationObj =
      curam.verification.sl.infrastructure.fact.VerificationApplicationFactory
        .newInstance();
    final VDIEDLinkKey vdiedLinkKey = new VDIEDLinkKey();

    vdiedLinkKey.VDIEDLinkID =
      dtls.bdmDtls.evidenceVerificationDetails.vdIEDLinkID;
    final curam.verification.sl.infrastructure.entity.intf.Verification verification =
      curam.verification.sl.infrastructure.entity.fact.VerificationFactory
        .newInstance();
    final VerificationDtlsList verificationDtlsList =
      verification.readByVDIEDLinkID(vdiedLinkKey);
    for (final VerificationDtls verificationDtls : verificationDtlsList.dtls) {
      if (verificationDtls.verificationLinkedType
        .equals(VERIFICATIONTYPE.NONCASEDATA)) {
        final boolean isEnabled = Configuration.getBooleanProperty(
          EnvVars.ENV_VERIFICATION_SUBMITTED_DOCUMENT_DISPLAY_ENABLED);
        readVerificationDetails.submittedDocumentDetails.isDisplayEnabledInd =
          isEnabled;
        if (readVerificationDetails.submittedDocumentDetails.isDisplayEnabledInd) {
          final CaseIDAndVDIEDLinkIDKey key = new CaseIDAndVDIEDLinkIDKey();

          key.caseID = verificationDtls.verificationLinkedID;
          key.vdiedLinkID =
            dtls.bdmDtls.evidenceVerificationDetails.vdIEDLinkID;

          final ValueList<curam.verification.sl.infrastructure.struct.SubmittedDocumentDetails> submittedDocs =
            verificationApplicationObj.listSubmittedDocuments(key).dtls;
          readVerificationDetails.submittedDocumentDetails.submittedDocumentDetailsList
            .addAll(submittedDocs);

        }
      }
    }
    // END : Task 27331 retrieve document list for Participant Type verification

    // START 56348-Task bug Dev bug 54842 Verification Reject Document Display
    // issue
    final BDMVerificationApplicationHelper verificationHelper =
      new BDMVerificationApplicationHelper();
    for (final VerificationDtls verificationDtls : verificationDtlsList.dtls) {

      final boolean isEnabled = Configuration.getBooleanProperty(
        EnvVars.ENV_VERIFICATION_SUBMITTED_DOCUMENT_DISPLAY_ENABLED);
      readVerificationDetails.submittedDocumentDetails.isDisplayEnabledInd =
        isEnabled;
      if (readVerificationDetails.submittedDocumentDetails.isDisplayEnabledInd) {
        final CaseIDAndVDIEDLinkIDKey key = new CaseIDAndVDIEDLinkIDKey();

        key.caseID = verificationDtls.verificationLinkedID;
        key.vdiedLinkID =
          dtls.bdmDtls.evidenceVerificationDetails.vdIEDLinkID;

        final ValueList<curam.verification.sl.infrastructure.struct.SubmittedDocumentDetails> submittedDocs =
          verificationHelper.listReviewedAndRejectedDocuments(key).dtls;
        final BDMRejectedDocumentDetailsList bdmRejectedDocumentDetailsList =
          new BDMRejectedDocumentDetailsList();
        for (final curam.verification.sl.infrastructure.struct.SubmittedDocumentDetails submittedDocumentDetails : submittedDocs) {
          final BDMRejectedDocumentDetails bdmRejectedDocumentDetails =
            new BDMRejectedDocumentDetails();

          bdmRejectedDocumentDetails.attachmentID =
            submittedDocumentDetails.attachmentID;
          bdmRejectedDocumentDetails.attachmentLinkID =
            submittedDocumentDetails.attachmentLinkID;
          bdmRejectedDocumentDetails.name = submittedDocumentDetails.name;
          bdmRejectedDocumentDetails.providedBy =
            submittedDocumentDetails.providedBy;
          bdmRejectedDocumentDetails.reviewStatus =
            submittedDocumentDetails.reviewStatus;
          bdmRejectedDocumentDetails.receivedDate =
            submittedDocumentDetails.receivedDate;
          final BDMAttachmentLinkDtls bdmAttachmentLinkDtls =
            readBDMAttachmentLink(submittedDocumentDetails.attachmentLinkID);
          bdmRejectedDocumentDetails.rejectionReason =
            bdmAttachmentLinkDtls.rejectionReason;
          bdmRejectedDocumentDetails.otherComments =
            bdmAttachmentLinkDtls.otherComments;
          bdmRejectedDocumentDetailsList.dtls.add(bdmRejectedDocumentDetails);
        }
        bdmReadVerificationDetails.rejectedDocuments
          .assign(bdmRejectedDocumentDetailsList);

      }

    }
    // END 56348-Task bug Dev bug 54842 Verification Reject Document Display
    // issue
    bdmReadVerificationDetails.verificationPageContextDetails
      .assign(readVerificationDetails.verificationPageContextDetails);
    bdmReadVerificationDetails.submittedDocumentDetails.submittedDocumentDetailsList
      .addAll(
        readVerificationDetails.submittedDocumentDetails.submittedDocumentDetailsList);

    bdmReadVerificationDetails.submittedDocumentDetails.isDisplayEnabledInd =
      readVerificationDetails.submittedDocumentDetails.isDisplayEnabledInd;

    bdmReadVerificationDetails.message
      .addAll(readVerificationDetails.message);

    bdmReadVerificationDetails.readVerificationDetails.commentsDetails.assign(
      readVerificationDetails.readVerificationDetails.commentsDetails);
    bdmReadVerificationDetails.readVerificationDetails.evidenceAndDataItemNameDetails
      .assign(
        readVerificationDetails.readVerificationDetails.evidenceAndDataItemNameDetails);

    bdmReadVerificationDetails.readVerificationDetails.verificationRequirementSummaryDetailsList
      .addAll(
        readVerificationDetails.readVerificationDetails.verificationRequirementSummaryDetailsList);

    // Task 63907 202207-21 Display Rejected verification item user provided
    // details
    final curam.verification.sl.infrastructure.intf.VerificationItemProvided verificationItemProvidedObj =
      curam.verification.sl.infrastructure.fact.VerificationItemProvidedFactory
        .newInstance();
    final VerificationItemProvidedKey verificationItemProvidedKey =
      new VerificationItemProvidedKey();
    final Set<Long> vIUtilizationSet = new HashSet<Long>();
    for (final UserProvidedVerificationItemDetails userVerifDetails : readVerificationDetails.readVerificationDetails.userProvidedVerificationItemDetailsList.userProvidedVerificationItemDetails) {
      vIUtilizationSet.add(userVerifDetails.verificationItemProvidedID);
    }
    final StatusAndVDIEDLinkIDDetails statusAndVDIEDLinkIDDetails =
      new StatusAndVDIEDLinkIDDetails();
    statusAndVDIEDLinkIDDetails.vdIEDLinkID = vdiedLinkKey.VDIEDLinkID;
    statusAndVDIEDLinkIDDetails.recordStatus = RECORDSTATUS.NORMAL;
    final VerificationItemProvided verificationItemProvided =
      VerificationItemProvidedFactory.newInstance();
    final VerificationItemProvidedListDetailsList verificationItemProvidedListDetailsList =
      verificationItemProvided
        .searchListDetailsByStatusAndVDIEDLinkID(statusAndVDIEDLinkIDDetails);
    final BDMVerificationItemProvided bdmVerificationItemProvidedObj =
      BDMVerificationItemProvidedFactory.newInstance();
    final BDMVerificationItemProvidedKey bdmVerificationItemProvidedKey =
      new BDMVerificationItemProvidedKey();
    for (int i = 0; i < verificationItemProvidedListDetailsList.dtls
      .size(); ++i) {
      final VerificationItemProvidedListDetails tempDetails =
        verificationItemProvidedListDetailsList.dtls.item(i);
      bdmVerificationItemProvidedKey.verificationItemProvidedID =
        tempDetails.verificationItemProvidedID;
      try {
        final BDMVerificationItemProvidedDtls bdmVerificationItemProvidedDtls =
          bdmVerificationItemProvidedObj.read(bdmVerificationItemProvidedKey);
        if (!vIUtilizationSet.contains(
          bdmVerificationItemProvidedDtls.verificationItemProvidedID)) {
          final UserProvidedVerificationItemDetails userProvidedVerificationItemDetails =
            new UserProvidedVerificationItemDetails();
          userProvidedVerificationItemDetails.dateReceived =
            tempDetails.dateReceived;
          userProvidedVerificationItemDetails.level = tempDetails.level;
          userProvidedVerificationItemDetails.receivedFrom =
            tempDetails.receivedFrom;
          userProvidedVerificationItemDetails.verificationItemName =
            tempDetails.verificationItemName;
          userProvidedVerificationItemDetails.verificationItemProvidedID =
            tempDetails.verificationItemProvidedID;
          verificationItemProvidedKey.verificationItemProvidedID =
            tempDetails.verificationItemProvidedID;
          userProvidedVerificationItemDetails.expiryDate =
            verificationItemProvidedObj
              .calculateExpiryDate(verificationItemProvidedKey).expiryDate;
          userProvidedVerificationItemDetails.verificationItemUtilizationID =
            bdmVerificationItemProvidedDtls.verificationItemUtilizationID;
          readVerificationDetails.readVerificationDetails.userProvidedVerificationItemDetailsList.userProvidedVerificationItemDetails
            .addRef(userProvidedVerificationItemDetails);

        }
      } catch (final RecordNotFoundException ex) {

      }

    }

    // START TASK 94272---ADD custom code here JP

    // ***************************************************************************************

    final ValueList<UserProvidedVerificationItemDetails> userProvidedVerificationItemDetailsList =
      readVerificationDetails.readVerificationDetails.userProvidedVerificationItemDetailsList.userProvidedVerificationItemDetails;
    // END TASK 94272---ADD custome code here JP
    for (final UserProvidedVerificationItemDetails userVerifDetails : userProvidedVerificationItemDetailsList) {

      final BDMUserProvidedVerificationItemDetails bdmuserProvidedVerificationItemDetails =
        new BDMUserProvidedVerificationItemDetails();
      bdmuserProvidedVerificationItemDetails.assign(userVerifDetails);

      final BDMVerificationItemProvidedDtls bdmVerificationItemProvidedDtls =
        getUserProvidedverificationItemUtilizationID(
          userVerifDetails.verificationItemProvidedID);

      bdmuserProvidedVerificationItemDetails.verificationItemUtilizationID =
        bdmVerificationItemProvidedDtls.verificationItemUtilizationID;

      final VerificationItemKey verificationItemKey =
        new VerificationItemKey();
      verificationItemKey.verificationItemID =
        bdmVerificationItemProvidedDtls.verificationItemUtilizationID;
      bdmuserProvidedVerificationItemDetails.verificationItemName =
        VerificationItemFactory.newInstance().read(verificationItemKey).name;

      bdmReadVerificationDetails.readVerificationDetails.userProvidedVerificationItemDetailsList.userProvidedVerificationItemDetails
        .addRef(bdmuserProvidedVerificationItemDetails);

    }
    updateVerificationStatusByAttachmentStatus(paramOverAllVerificationStatus,
      paramOverAllVerificationID, userProvidedVerificationItemDetailsList);

    return bdmReadVerificationDetails;
  }

  /**
   *
   * Update OverAll Veirfication Status to Verified only if atleast 1 Attachment
   * has Accptance status 'Accepted' and
   * is Active or VerificationItem has no attachment , Not verified otherwise
   *
   * @param paramOverAllVerificationStatus
   * @param paramOverAllVerificationID
   * @param vIUtilizationSet
   * @throws AppException
   * @throws InformationalException
   */

  private void updateVerificationStatusByAttachmentStatus(
    final String paramOverAllVerificationStatus,
    final long paramOverAllVerificationID,
    final ValueList<UserProvidedVerificationItemDetails> verificationItemProvidedIDList)
    throws AppException, InformationalException {

    String verificationStatus = VERIFICATIONSTATUS.NOTVERIFIED;
    /// Iterate thru All Verification Item Provided
    for (int x = 0; x < verificationItemProvidedIDList.size(); x++) {
      final UserProvidedVerificationItemDetails userVerifDetails =
        verificationItemProvidedIDList.get(x);

      final VerificationItemProvidedKey verificationItemProvidedKey1 =
        new VerificationItemProvidedKey();
      verificationItemProvidedKey1.verificationItemProvidedID =
        userVerifDetails.verificationItemProvidedID;

      // List all Attachments by Verfification Item Provided
      final VerificationAttachmentSummaryDetailsList verificationAttachmentSummaryDetailsList =
        VerificationAttachmentLinkFactory.newInstance()
          .searchAttachmentByVerificationItemProvided(
            verificationItemProvidedKey1);

      if (!verificationAttachmentSummaryDetailsList.dtls.isEmpty()) {

        for (int i = 0; i < verificationAttachmentSummaryDetailsList.dtls
          .size(); i++) {

          final VerificationAttachmentSummaryDetails verificationAttachmentSummaryDetails =
            verificationAttachmentSummaryDetailsList.dtls.get(i);
          final long linkID =
            verificationAttachmentSummaryDetails.verificationAttachmentLinkID;

          // Fetch Active Attachment Links only
          if (verificationAttachmentSummaryDetails.recordStatus
            .equals(RECORDSTATUS.NORMAL)) {

            final BDMVerificationAttachmentLinkDtls bdmVerificationAttachmentLinkDtls =
              readBDMVerificationAttachmentLink(linkID);

            if (bdmVerificationAttachmentLinkDtls.acceptanceStatus
              .equals(BDMACCEPTANCESTATUS.BDMVIS8000)) {
              verificationStatus = VERIFICATIONSTATUS.VERIFIED;
              break;
            }

          }

        } // FOR

      } else {
        verificationStatus = VERIFICATIONSTATUS.VERIFIED;
        break;
      }

    }

    // Update verification status
    if (!verificationStatus.equals(paramOverAllVerificationStatus)) {
      final VerificationStatusDetails verificationStatusDetails =
        new VerificationStatusDetails();
      verificationStatusDetails.verificationStatus = verificationStatus;
      final VerificationKey key = new VerificationKey();
      key.verificationID = paramOverAllVerificationID;
      final curam.verification.sl.infrastructure.entity.intf.Verification verificationObj =
        VerificationFactory.newInstance();

      verificationObj.modifyVerificationStatus(key,
        verificationStatusDetails);
    }
  }

  /**
   * This method validate the newly added fields for Add Proof /Add
   * Attachment/Edit Attachment Screens under Verification Proof
   * custom entity.
   *
   * @param BDMNewUserProvidedVerificationItemDetails details
   * @throws AppException
   * @throws InformationalException
   */

  public void validateVerificationItemDetails(
    final BDMNewUserProvidedVerificationItemDetails details)
    throws AppException, InformationalException {

    // START Altered for 56003 Dev bug 55654 to fix Validation changes

    if (!details.acceptanceStatus.isEmpty()
      && BDMACCEPTANCESTATUS.BDMVIS8001.equals(details.acceptanceStatus)) {
      if (details.rejectionReason.isEmpty()) {
        throw new AppException(
          BDMENTVERIFICATION.ERR_VERIFICATION_PROOF_REJ_REASON_SHOULD_BE_SELECTED_IF_STATUS_SELECTED_REJECTED);

      } else if (BDMATTACHREJECTIONREASON.BDMVRR8004
        .equals(details.rejectionReason) && details.otherComments.isEmpty()) {
        throw new AppException(
          BDMENTVERIFICATION.ERR_VERIFICATION_PROOF_COMMENTS_MUST_BE_ENTERED_IF_REJ_REASON_OTHER);
      }
    } else if (!details.acceptanceStatus.isEmpty()
      && BDMACCEPTANCESTATUS.BDMVIS8000.equals(details.acceptanceStatus)) {

      if (!details.rejectionReason.isEmpty()) {
        throw new AppException(
          BDMENTVERIFICATION.ERR_VERIFICATION_PROOF_REJ_REASON_SHOULD_BE_BLANK_IF_STATUS_SELECTED_ACCEPTED);
      }

    }

  }

  public void validateVerificationItemDetailsModify(
    final BDMNewUserProvidedVerificationItemDetails details)
    throws AppException, InformationalException {

    // START Altered for 56003 Dev bug 55654 to fix Validation changes
    if (!details.acceptanceStatus.isEmpty()
      && details.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachmentID == 0) {
      // END Altered for 56003 Dev bug 55654 to fix Validation changes
      throw new AppException(
        BDMENTVERIFICATION.ERR_VERIFICATION_PROOF_STATUS_BE_SELECTED);
    } else if (!details.acceptanceStatus.isEmpty()
      && BDMACCEPTANCESTATUS.BDMVIS8001.equals(details.acceptanceStatus)) {
      if (details.rejectionReason.isEmpty()) {
        throw new AppException(
          BDMENTVERIFICATION.ERR_VERIFICATION_PROOF_REJ_REASON_SHOULD_BE_SELECTED_IF_STATUS_SELECTED_REJECTED);

      } else if (BDMATTACHREJECTIONREASON.BDMVRR8004
        .equals(details.rejectionReason) && details.otherComments.isEmpty()) {
        throw new AppException(
          BDMENTVERIFICATION.ERR_VERIFICATION_PROOF_COMMENTS_MUST_BE_ENTERED_IF_REJ_REASON_OTHER);
      }
    } else if (!details.acceptanceStatus.isEmpty()
      && BDMACCEPTANCESTATUS.BDMVIS8000.equals(details.acceptanceStatus)) {

      if (!details.rejectionReason.isEmpty()) {
        throw new AppException(
          BDMENTVERIFICATION.ERR_VERIFICATION_PROOF_REJ_REASON_SHOULD_BE_BLANK_IF_STATUS_SELECTED_ACCEPTED);
      }

    }

  }

  /**
   *
   */
  @Override
  public
    curam.ca.gc.bdm.facade.verifications.struct.BDMVerificationAttachmentLinkKey
    createVerificationAttachment(
      final BDMCreateVerificationAttachmentDetails inputDetails)
      throws AppException, InformationalException {

    // TASK 53251 - file attachment validation if file is uploaded
    if (!inputDetails.createVerificationAttachmentDetails.details.createAttachmentDtls.attachmentName
      .isEmpty()) {
      validateAttachment(
        inputDetails.createVerificationAttachmentDetails.details.createAttachmentDtls);
    }

    final long verificationItemProvidedID =
      inputDetails.createVerificationAttachmentDetails.details.createLinkDtls.verificationItemProvidedID;

    final curam.ca.gc.bdm.facade.verifications.struct.BDMVerificationAttachmentLinkKey returnKey =
      new curam.ca.gc.bdm.facade.verifications.struct.BDMVerificationAttachmentLinkKey();
    final BDMNewUserProvidedVerificationItemDetails validateDtls =
      new BDMNewUserProvidedVerificationItemDetails();
    validateDtls.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls
      .assign(
        inputDetails.createVerificationAttachmentDetails.details.createAttachmentDtls);
    validateDtls.acceptanceStatus =
      inputDetails.bdmVerificationAttachmentLinkDetails.acceptanceStatus;
    validateDtls.rejectionReason =
      inputDetails.bdmVerificationAttachmentLinkDetails.rejectionReason;
    validateDtls.otherComments =
      inputDetails.bdmVerificationAttachmentLinkDetails.otherComments;

    inputDetails.bdmVerificationAttachmentLinkDetails.verificationItemProvidedID =
      verificationItemProvidedID;
    /**
     * Step 1 Validation
     */
    validateVerificationItemDetails(validateDtls);

    long attachmentID = 0L;

    final CreateVerificationAttachmentLinkDetails createVerificationAttachmentLinkDetails =
      new CreateVerificationAttachmentLinkDetails();

    createVerificationAttachmentLinkDetails
      .assign(inputDetails.createVerificationAttachmentDetails.details);

    final curam.verification.sl.infrastructure.intf.VerificationAttachmentLink verificationAttachmentLink =
      curam.verification.sl.infrastructure.fact.VerificationAttachmentLinkFactory
        .newInstance();

    // Setting the verificationAttachmentLinkID

    final curam.verification.sl.infrastructure.entity.struct.VerificationAttachmentLinkKey verificationAttachmentLinkKey =
      verificationAttachmentLink.createVerificationAttachmentLink(
        createVerificationAttachmentLinkDetails);

    if (verificationAttachmentLinkKey.verificationAttachmentLinkID == 0L) {
      attachmentID =
        createVerificationAttachmentLinkDetails.createLinkDtls.attachmentID;

      if (attachmentID == 0)
        attachmentID =
          createVerificationAttachmentLinkDetails.createAttachmentDtls.attachmentID;

      createVerificationAttachmentLinkDetails.createLinkDtls.attachmentID =
        attachmentID;

      createVerificationAttachmentLinkDetails.createLinkDtls.recordStatus =
        RECORDSTATUS.DEFAULTCODE;

      VerificationAttachmentLinkFactory.newInstance()
        .insert(createVerificationAttachmentLinkDetails.createLinkDtls);

    }

    final BDMVerificationAttachmentLinkDtls bdmVerificationAttachmentLinkDtls =
      new BDMVerificationAttachmentLinkDtls();
    bdmVerificationAttachmentLinkDtls.acceptanceStatus =
      inputDetails.bdmVerificationAttachmentLinkDetails.acceptanceStatus;
    bdmVerificationAttachmentLinkDtls.otherComments =
      inputDetails.bdmVerificationAttachmentLinkDetails.otherComments;
    bdmVerificationAttachmentLinkDtls.rejectionReason =
      inputDetails.bdmVerificationAttachmentLinkDetails.rejectionReason;

    bdmVerificationAttachmentLinkDtls.verificationAttachmentLinkID =
      createVerificationAttachmentLinkDetails.createLinkDtls.verificationAttachmentLinkID;
    /**
     * To Clear the rejection reason if the Acceptance Status is Accepted
     */
    if (BDMACCEPTANCESTATUS.BDMVIS8000
      .equals(bdmVerificationAttachmentLinkDtls.acceptanceStatus)) {
      bdmVerificationAttachmentLinkDtls.rejectionReason = "";
    }

    insertBDMVerificationAttachmentLink(bdmVerificationAttachmentLinkDtls);

    createIfBDMverificationItemProvidedNotFound(
      inputDetails.createVerificationAttachmentDetails.details.createLinkDtls.verificationItemProvidedID);

    /**
     * Final Step Internal check and swapping the verificationItemUtilizationID
     * internally to stop the verifying process and maintain a internal dummy
     * verificationItemUtilizationID code
     * accordingly
     */

    if (BDMACCEPTANCESTATUS.BDMVIS8000.equals(
      inputDetails.bdmVerificationAttachmentLinkDetails.acceptanceStatus)) {
      /**
       * Final step for updating the Original verificationItemUtilizationID for
       * the verification process to cleared
       */
      modifyVerificationItemProvidedbyID(
        inputDetails.createVerificationAttachmentDetails.details.createLinkDtls.verificationItemProvidedID);
    }

    return returnKey;
  }

  @Override
  public void modifyVerificationAttachment(
    final BDMModifyVerificationAttachmentDetails details)
    throws AppException, InformationalException {

    final BDMNewUserProvidedVerificationItemDetails validateDtls =
      new BDMNewUserProvidedVerificationItemDetails();
    validateDtls.acceptanceStatus =
      details.bdmVerificationAttachmentLinkDetails.acceptanceStatus;
    validateDtls.rejectionReason =
      details.bdmVerificationAttachmentLinkDetails.rejectionReason;
    validateDtls.otherComments =
      details.bdmVerificationAttachmentLinkDetails.otherComments;

    validateDtls.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls
      .assign(details.bdmDtls.details.modifyAttachmentDtls);

    /**
     * Step 1 Validation
     */
    validateVerificationItemDetailsModify(validateDtls);

    if (!validateDtls.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachmentName
      .isEmpty()) {
      validateAttachment(
        validateDtls.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls);
    }

    final curam.verification.sl.infrastructure.intf.VerificationAttachmentLink verificationAttachmentLink =
      curam.verification.sl.infrastructure.fact.VerificationAttachmentLinkFactory
        .newInstance();

    verificationAttachmentLink
      .modifyVerificationAttachmentLink(details.bdmDtls.details);

    final BDMVerificationAttachmentLinkDtls bdmVerificationAttachmentLinkDtls =
      new BDMVerificationAttachmentLinkDtls();
    bdmVerificationAttachmentLinkDtls.acceptanceStatus =
      details.bdmVerificationAttachmentLinkDetails.acceptanceStatus;

    bdmVerificationAttachmentLinkDtls.otherComments =
      details.bdmVerificationAttachmentLinkDetails.otherComments;
    bdmVerificationAttachmentLinkDtls.rejectionReason =
      details.bdmVerificationAttachmentLinkDetails.rejectionReason;

    bdmVerificationAttachmentLinkDtls.verificationAttachmentLinkID =
      details.bdmDtls.details.modifyLinkDtls.verificationAttachmentLinkID;

    /**
     * To Clear the rejection reason if the Acceptance Status is Accepted
     */
    if (BDMACCEPTANCESTATUS.BDMVIS8000.equals(
      details.bdmVerificationAttachmentLinkDetails.acceptanceStatus)) {
      details.bdmVerificationAttachmentLinkDetails.rejectionReason = "";
    }

    modifyBDMVerificationAttachmentLink(bdmVerificationAttachmentLinkDtls,
      details.bdmDtls.details.modifyLinkDtls.verificationItemProvidedID);

  }

  /**
   * This method is to modify the newly passed values for the
   * BDMVerificationAttachmentLink.
   *
   * @param BDMVerificationAttachmentLinkDtls bdmVerificationAttachmentLinkDtls
   * @throws AppException
   * @throws InformationalException
   */
  public void modifyBDMVerificationAttachmentLink(
    final BDMVerificationAttachmentLinkDtls bdmVerificationAttachmentLinkDtls,
    final long verificationProvidedID)

    throws AppException, InformationalException {

    final BDMVerificationAttachmentLink bdmVerificationAttachmentLink =
      BDMVerificationAttachmentLinkFactory.newInstance();

    final BDMVerificationAttachmentLinkKey bdmVerificationAttachmentLinkKey =
      new BDMVerificationAttachmentLinkKey();
    bdmVerificationAttachmentLinkKey.verificationAttachmentLinkID =
      bdmVerificationAttachmentLinkDtls.verificationAttachmentLinkID;

    final NotFoundIndicator nFoundInd = new NotFoundIndicator();
    bdmVerificationAttachmentLink.read(nFoundInd,
      bdmVerificationAttachmentLinkKey);

    if (!nFoundInd.isNotFound()) {
      bdmVerificationAttachmentLink.modify(bdmVerificationAttachmentLinkKey,
        bdmVerificationAttachmentLinkDtls);
    } else {
      insertBDMVerificationAttachmentLink(bdmVerificationAttachmentLinkDtls);
    }

    if (BDMACCEPTANCESTATUS.BDMVIS8000
      .equals(bdmVerificationAttachmentLinkDtls.acceptanceStatus)) {

      /**
       * Final step for updating the Original verificationItemUtilizationID for
       * the verification process to cleared
       */
      modifyVerificationItemProvidedbyID(verificationProvidedID);

    }

  }

  /**
   *
   * @param verificationProvidedID
   * @throws AppException
   * @throws InformationalException
   */
  public void
    modifyVerificationItemProvidedbyID(final long verificationProvidedID)
      throws AppException, InformationalException {

    final BDMVerificationItemProvidedDtls userProvidedVerificationItemUtilizationID =
      getUserProvidedverificationItemUtilizationID(verificationProvidedID);

    final VerificationItemProvided verificationItemProvidedObj =
      VerificationItemProvidedFactory.newInstance();

    final VerificationItemProvidedKey key = new VerificationItemProvidedKey();
    key.verificationItemProvidedID = verificationProvidedID;
    final VerificationItemProvidedDtls verificationItemProvidedDtls =
      verificationItemProvidedObj.read(key);
    verificationItemProvidedDtls.verificationItemUtilizationID =
      userProvidedVerificationItemUtilizationID.verificationItemUtilizationID;
    verificationItemProvidedObj.modify(key, verificationItemProvidedDtls);

  }

  @Override
  public BDMReadVerificationAttachmentDetails readVerificationAttachment(
    final BDMReadVerificationAttachmentLinkKey details)
    throws AppException, InformationalException {

    final BDMReadVerificationAttachmentDetails bdmReadVerificationAttachmentDetails =
      new BDMReadVerificationAttachmentDetails();

    final curam.verification.sl.infrastructure.intf.VerificationAttachmentLink verificationAttachmentLink =
      curam.verification.sl.infrastructure.fact.VerificationAttachmentLinkFactory
        .newInstance();

    final ReadVerificationAttachmentDetails viewVerificationAttachmentDetails =
      new ReadVerificationAttachmentDetails();

    final ReadVerificationAttachmentLinkKey readVerificationAttachmentLinkKey =
      new ReadVerificationAttachmentLinkKey();
    readVerificationAttachmentLinkKey.key.verificationAttachmentLinkID =
      details.dtlsReadVerificationAttachmentLinkKey.key.verificationAttachmentLinkID;
    readVerificationAttachmentLinkKey.key.attachmentID =
      details.dtlsReadVerificationAttachmentLinkKey.key.attachmentID;

    viewVerificationAttachmentDetails.pageContextDescription =
      verificationAttachmentLink.readPageContextDescAttachmentByAttachID(
        readVerificationAttachmentLinkKey);
    viewVerificationAttachmentDetails.details = verificationAttachmentLink
      .readVerificationAttachmentLink(readVerificationAttachmentLinkKey.key);

    // TASK 53251: Disable attachment link if attachment has been deleted
    if (!viewVerificationAttachmentDetails.details.readLinkDtls.recordStatus
      .equals(RECORDSTATUS.CANCELLED)) {

      bdmReadVerificationAttachmentDetails.bdmDtls
        .assign(viewVerificationAttachmentDetails);
      bdmReadVerificationAttachmentDetails.bdmVerificationAttachmentLinkDetails
        .assign(readBDMVerificationAttachmentLink(
          details.dtlsReadVerificationAttachmentLinkKey.key.verificationAttachmentLinkID));
    }

    getFileLocationURL(bdmReadVerificationAttachmentDetails);

    return bdmReadVerificationAttachmentDetails;
  }

  @Override
  public BDMReadUserProvidedVerificationItemDetails
    readUserProvidedVerificationItem(
      final BDMUserProvidedVerificationItem key)
      throws AppException, InformationalException {

    final BDMReadUserProvidedVerificationItemDetails bdmReadUserProvidedVerificationItemDetails =
      new BDMReadUserProvidedVerificationItemDetails();

    final UserProvidedVerificationItem userProvidedVerificationItem =
      new UserProvidedVerificationItem();
    userProvidedVerificationItem.userProvidedVerificationItem.verificationItemProvidedID =
      key.userProvidedVerificationItemDtls.userProvidedVerificationItem.verificationItemProvidedID;
    final ReadUserProvidedVerificationItemDetails readUserProvidedVerificationItemDetails =
      curam.verification.facade.infrastructure.fact.VerificationApplicationFactory
        .newInstance()
        .readUserProvidedVerificationItem(userProvidedVerificationItem);

    bdmReadUserProvidedVerificationItemDetails.infoMsgListOpt
      .addAll(readUserProvidedVerificationItemDetails.infoMsgListOpt);
    bdmReadUserProvidedVerificationItemDetails.verificationPageContextDetails
      .assign(
        readUserProvidedVerificationItemDetails.verificationPageContextDetails);
    bdmReadUserProvidedVerificationItemDetails.readUserProvidedVerificationItemDetails.itemProvidedMandatoryAndLevelDetails
      .assign(
        readUserProvidedVerificationItemDetails.readUserProvidedVerificationItemDetails.itemProvidedMandatoryAndLevelDetails);
    bdmReadUserProvidedVerificationItemDetails.readUserProvidedVerificationItemDetails.userProvidedVerificationInfo
      .assign(
        readUserProvidedVerificationItemDetails.readUserProvidedVerificationItemDetails.userProvidedVerificationInfo);

    final ValueList<BDMVerificationAttachmentSummaryDetails> bdmVerificationAttachmentSummaryDetailsList =
      new ValueList<BDMVerificationAttachmentSummaryDetails>(
        BDMVerificationAttachmentSummaryDetails.class);

    for (final VerificationAttachmentSummaryDetails verificationAttachmentSummaryDetails : readUserProvidedVerificationItemDetails.readUserProvidedVerificationItemDetails.verificationAttachmentSummaryDetailsList) {

      final BDMVerificationAttachmentSummaryDetails bdmVerificationAttachmentSummaryDetails =
        new BDMVerificationAttachmentSummaryDetails();

      bdmVerificationAttachmentSummaryDetails.verificationAttachmentSummaryDetails
        .assign(verificationAttachmentSummaryDetails);

      final BDMVerificationAttachmentLinkDtls bdmVerificationAttachmentLinkDtls =
        readBDMVerificationAttachmentLink(
          verificationAttachmentSummaryDetails.verificationAttachmentLinkID);

      final BDMVerificationAttachmentLinkDetails bdmVerificationAttachmentLinkDetails =
        new BDMVerificationAttachmentLinkDetails();
      if (!bdmVerificationAttachmentLinkDtls.acceptanceStatus.isEmpty()) {
        bdmVerificationAttachmentLinkDetails.acceptanceStatus =
          bdmVerificationAttachmentLinkDtls.acceptanceStatus;
      }
      if (!bdmVerificationAttachmentLinkDtls.rejectionReason.isEmpty()) {
        bdmVerificationAttachmentLinkDetails.rejectionReason =
          bdmVerificationAttachmentLinkDtls.rejectionReason;
      }
      if (!bdmVerificationAttachmentLinkDtls.otherComments.isEmpty()) {
        bdmVerificationAttachmentLinkDetails.otherComments =
          bdmVerificationAttachmentLinkDtls.otherComments;
      }

      bdmVerificationAttachmentSummaryDetails.bdmVerificationAttachmentLinkDtls
        .assign(bdmVerificationAttachmentLinkDetails);

      bdmVerificationAttachmentSummaryDetailsList
        .add(bdmVerificationAttachmentSummaryDetails);

    }
    bdmReadUserProvidedVerificationItemDetails.readUserProvidedVerificationItemDetails.verificationAttachmentSummaryDetailsList
      .addAll(bdmVerificationAttachmentSummaryDetailsList);

    return bdmReadUserProvidedVerificationItemDetails;
  }

  // END 23374 Task - Agent portal verification change

  /*
   * BEGIN Task 27331 set documentSubmitted ind for Participant Type
   * verification
   * record
   * Case
   * Method to fetch verification details
   *
   * @param CaseKey
   *
   * @return CaseEvidenceVerificationDisplayDetailsList
   *
   */
  @Override
  public CaseEvidenceVerificationDisplayDetailsList
    listOutstandingVerificationDetailsForCaseEvidence(final CaseKey key)
      throws AppException, InformationalException {

    final CaseEvidenceVerificationDisplayDetailsList caseEvidenceVerificationDisplayDetailsList =
      super.listOutstandingVerificationDetailsForCaseEvidence(key);

    final OutstandingVerificationAttachmentLink outstandingVerificationAttachmentLinkObj =
      OutstandingVerificationAttachmentLinkFactory.newInstance();

    for (final CaseEvidenceVerificationDisplayDetails caseEvidenceVerificationDisplayDetails : caseEvidenceVerificationDisplayDetailsList.dtls) {

      // BEGIN : Task 27331 set documentSubmitted ind for Participant Type
      // verification
      // retrieve the verificationID for participant type
      // verification, which is missed in OOTB
      if (caseEvidenceVerificationDisplayDetails.verificationID == 0L) {

        final VDIEDLinkKey vdiedLinkKey = new VDIEDLinkKey();

        vdiedLinkKey.VDIEDLinkID =
          caseEvidenceVerificationDisplayDetails.vDIEDLinkID;
        final curam.verification.sl.infrastructure.entity.intf.Verification verification =
          curam.verification.sl.infrastructure.entity.fact.VerificationFactory
            .newInstance();
        final VerificationDtlsList verificationDtlsList =
          verification.readByVDIEDLinkID(vdiedLinkKey);
        if (verificationDtlsList.dtls.size() > 0) {
          caseEvidenceVerificationDisplayDetails.verificationID =
            verificationDtlsList.dtls.item(0).verificationID;
        }
        if (caseEvidenceVerificationDisplayDetails.verificationID != 0L) {
          final VerificationKey verificationKey = new VerificationKey();
          verificationKey.verificationID =
            caseEvidenceVerificationDisplayDetails.verificationID;
          final CountDetails documentsSubmittedCount =
            outstandingVerificationAttachmentLinkObj
              .countPendingReviewVerificationAttachments(verificationKey);
          final boolean documentSubmittedOpt =
            documentsSubmittedCount.numberOfRecords > 0 ? true : false;
          caseEvidenceVerificationDisplayDetails.verifiableDataItemNameXMLOpt =
            getVerifiableDataItemDetails(
              caseEvidenceVerificationDisplayDetails.verifiableDataItemName,
              documentSubmittedOpt).toString();
        }
      }
      // END : Task 27331 set documentSubmitted ind for Participant Type
    }
    return caseEvidenceVerificationDisplayDetailsList;
  }

  private long getVerificationIDForParticipantVerificationByVdiedLinkID(
    final long concernRoleID, final long vDIEDLinkID,
    final long evidenceDescriptorID)
    throws AppException, InformationalException {

    final ParticipantKeyStruct participantKeyStruct =
      new ParticipantKeyStruct();
    participantKeyStruct.participantID = concernRoleID;
    final OutstandingIndicator outstandingIndicator =
      new OutstandingIndicator();
    outstandingIndicator.verificationStatus = VERIFICATIONSTATUS.NOTVERIFIED;
    final CaseEvidenceVerificationDetailsList caseEvidenceVerificationDetailsList =
      this.listAllParticipantVerificationDetails(participantKeyStruct,
        outstandingIndicator);
    for (final CaseEvidenceVerificationDetails caseEvidenceVerificationDetails : caseEvidenceVerificationDetailsList.dtls) {
      if (caseEvidenceVerificationDetails.vDIEDLinkID == vDIEDLinkID
        && caseEvidenceVerificationDetails.evidenceDescriptorID == evidenceDescriptorID) {
        return caseEvidenceVerificationDetails.verificationID;
      }
    }
    return 0L;
  }

  /**
   * Format the XML data for the Verifiable Data Item Name - this will always
   * contain the verifiable item name, and will conditionally contain an icon
   * image next to the name, if both the
   * 'curam.verification.submittedDocuments.display.enabled' property is
   * enabled and if submitted documents that are pending a review exist.
   *
   * @param verifiableDataItemName
   * The verifiable data item name.
   * @param documentsPendingReviewExist flag that indicates if any documents
   * that are pending a review for a verification exist.
   *
   * @return a content panel builder, which contains the verifiable data item
   * name, and conditionally contains an icon (if documents pending a review
   * exist and if the display property is enabled).
   */
  private ContentPanelBuilder getVerifiableDataItemDetails(
    final String verifiableDataItemName,
    final boolean documentsPendingReviewExist)
    throws AppException, InformationalException {

    final ContentPanelBuilder verifiableDataItemNamePanelBuilder =
      ContentPanelBuilder.createPanel(CuramConst.gkContentPanel);

    verifiableDataItemNamePanelBuilder.addStringItem(verifiableDataItemName,
      VerificationConstant.gkVerfiableDataItemNameCSSStyle);

    final boolean isEnabled = Configuration.getBooleanProperty(
      EnvVars.ENV_VERIFICATION_SUBMITTED_DOCUMENT_DISPLAY_ENABLED);

    if (isEnabled && documentsPendingReviewExist) {
      final ImageBuilder imageBuilder =
        ImageBuilder.createImage(PodsConst.kSubmittedDocumentImage);

      imageBuilder.setImageResource(PodsConst.kImageResource);

      final String tooltipText = new LocalisableString(
        VERIFICATIONDETAILSPANEL.INF_TOOLTIP_VERIFICATION_SUBMITTED_DOCUMENT)
          .toClientFormattedText();

      imageBuilder.setImageAltText(tooltipText);

      verifiableDataItemNamePanelBuilder.addImageItem(imageBuilder,
        VerificationConstant.gkVerifiableSubmittedDocumentsIconCSSStyle);

    }
    return verifiableDataItemNamePanelBuilder;
  }

  /**
   * This method retrieves the list of outstanding verifications for
   * participants.
   * This is to fix the defect in OOTB method
   * curam.verification.sl.infrastructure.impl.Verification.listAllParticipantVerificationDetails
   * where the verificationID is not set.
   *
   * @param key participant identifier
   * @return list of outstanding verification Details for the participants
   */
  private CaseEvidenceVerificationDetailsList
    listAllParticipantVerificationDetails(final ParticipantKeyStruct key,
      final OutstandingIndicator indicator)
      throws AppException, InformationalException {

    final curam.verification.sl.infrastructure.entity.intf.Verification verification =
      VerificationFactory.newInstance();

    // EvidenceDescriptor variables
    final EvidenceDescriptor evidenceDescriptor =
      EvidenceDescriptorFactory.newInstance();

    final CaseEvidenceVerificationDetailsList caseEvidenceVerificationDetailsList =
      new CaseEvidenceVerificationDetailsList();

    CaseEvidenceVerificationDetails caseEvidenceVerificationDetails;
    final EvidenceDescriptorKey evidenceDescriptorKey =
      new EvidenceDescriptorKey();
    ReadRelatedIDParticipantIDAndEvidenceTypeDetails readRelatedIDParticipantIDAndEvidenceTypeDetails =
      new ReadRelatedIDParticipantIDAndEvidenceTypeDetails();

    VerificationAndVDIEDLinkDetailsList verificationAndVDIEDLinkDetailsList;
    final ReadVerificationStatusLinkedIDandTypeKey readVerificationStatusLinkedIDandTypeKey =
      new ReadVerificationStatusLinkedIDandTypeKey();

    final ReadByVerLinkedIDTypeAndStatusKey readByVerLinkedIDTypeAndStatusKey =
      new ReadByVerLinkedIDTypeAndStatusKey();

    readByVerLinkedIDTypeAndStatusKey.verificationLinkedID =
      key.participantID;
    readByVerLinkedIDTypeAndStatusKey.evidenceDescriptorStatus1 =
      EVIDENCEDESCRIPTORSTATUS.ACTIVE;
    readByVerLinkedIDTypeAndStatusKey.evidenceDescriptorStatus2 =
      EVIDENCEDESCRIPTORSTATUS.INEDIT;
    readByVerLinkedIDTypeAndStatusKey.verificationLinkedType =
      VERIFICATIONTYPE.NONCASEDATA;

    readVerificationStatusLinkedIDandTypeKey.verificationLinkedID =
      key.participantID;
    readVerificationStatusLinkedIDandTypeKey.verificationStatus =
      indicator.verificationStatus;
    readVerificationStatusLinkedIDandTypeKey.evidenceDescriptorStatus1 =
      EVIDENCEDESCRIPTORSTATUS.ACTIVE;
    readVerificationStatusLinkedIDandTypeKey.evidenceDescriptorStatus2 =
      EVIDENCEDESCRIPTORSTATUS.INEDIT;

    readVerificationStatusLinkedIDandTypeKey.verificationLinkedType =
      VERIFICATIONTYPE.NONCASEDATA;
    verificationAndVDIEDLinkDetailsList =
      verification.searchOutstandingVerAndVDIEDLinkDetailsByLinkedIDandType(
        readVerificationStatusLinkedIDandTypeKey);

    for (int i = 0; i < verificationAndVDIEDLinkDetailsList.dtls
      .size(); i++) {
      evidenceDescriptorKey.evidenceDescriptorID =
        verificationAndVDIEDLinkDetailsList.dtls.item(i).evidenceDescriptorID;

      readRelatedIDParticipantIDAndEvidenceTypeDetails = evidenceDescriptor
        .readRelatedIDParticipantIDAndEvidenceType(evidenceDescriptorKey);

      caseEvidenceVerificationDetails = new CaseEvidenceVerificationDetails();
      caseEvidenceVerificationDetails.evidenceTypeOpt =
        readRelatedIDParticipantIDAndEvidenceTypeDetails.evidenceType;

      caseEvidenceVerificationDetails.vDIEDLinkID =
        verificationAndVDIEDLinkDetailsList.dtls.item(i).VDIEDLinkID;
      caseEvidenceVerificationDetails.evidenceDescriptorID =
        verificationAndVDIEDLinkDetailsList.dtls.item(i).evidenceDescriptorID;

      caseEvidenceVerificationDetails.mandatory =
        verificationAndVDIEDLinkDetailsList.dtls.item(i).mandatory;

      caseEvidenceVerificationDetails.verificationID =
        verificationAndVDIEDLinkDetailsList.dtls.item(i).verificationID;
      // since status has been updated fetch it again
      final VerificationKey verificationKey = new VerificationKey();

      verificationKey.verificationID =
        verificationAndVDIEDLinkDetailsList.dtls.item(i).verificationID;
      caseEvidenceVerificationDetails.verificationStatus = verification
        .readVerificationStatus(verificationKey).verificationStatus;

      if (!caseEvidenceVerificationDetails.verificationStatus
        .equals(VERIFICATIONSTATUS.CANCELLED)) {
        caseEvidenceVerificationDetailsList.dtls
          .addRef(caseEvidenceVerificationDetails);
      }
    }
    return caseEvidenceVerificationDetailsList;

  }

  /**
   *
   */
  @Override
  public void rejectDocumentPendingReviewForVerification(
    final BDMCaseIDAndVDIEDLinkIDKey input)
    throws AppException, InformationalException {

    final BDMAttachmentLinkDtls bdmAttachmentLinkDtls =
      new BDMAttachmentLinkDtls();

    bdmAttachmentLinkDtls.rejectionReason =
      input.bdmAttachmentLinkDtls.rejectionReason;
    bdmAttachmentLinkDtls.otherComments =
      input.bdmAttachmentLinkDtls.otherComments;

    bdmAttachmentLinkDtls.attachmentLinkID =
      input.attachmentLinkIDdtls.attachmentLinkID;

    validateRejectReason(bdmAttachmentLinkDtls);

    final AttachmentLinkID attachmentLinkID = new AttachmentLinkID();
    attachmentLinkID.attachmentLinkID =
      input.attachmentLinkIDdtls.attachmentLinkID;

    VerificationApplicationFactory.newInstance()
      .rejectDocumentPendingReviewForVerification(attachmentLinkID);

    BDMAttachmentLinkFactory.newInstance().insert(bdmAttachmentLinkDtls);

  }

  /**
   *
   */
  @Override
  public void rejectAllDocumentsPendingReviewForVerification(
    final BDMCaseIDAndVDIEDLinkIDKey input)
    throws AppException, InformationalException {

    final BDMAttachmentLinkDtls bdmAttachmentLinkDtls =
      new BDMAttachmentLinkDtls();

    bdmAttachmentLinkDtls.rejectionReason =
      input.bdmAttachmentLinkDtls.rejectionReason;
    bdmAttachmentLinkDtls.otherComments =
      input.bdmAttachmentLinkDtls.otherComments;

    validateRejectReason(bdmAttachmentLinkDtls);

    // BEGIN: the OOTB code does not work for participant type verification,
    // below code
    // would work for both case type verification and participant type
    // verification.
    final CaseIDAndVDIEDLinkIDKey caseIDAndVDIEDLinkIDKey =
      new CaseIDAndVDIEDLinkIDKey();
    caseIDAndVDIEDLinkIDKey.vdiedLinkID =
      input.caseIDAndVDIEDLinkIDKeyDtls.vdiedLinkID;

    final VDIEDLinkKey vdiedLinkKey = new VDIEDLinkKey();

    vdiedLinkKey.VDIEDLinkID = caseIDAndVDIEDLinkIDKey.vdiedLinkID;
    final curam.verification.sl.infrastructure.entity.intf.Verification verification =
      curam.verification.sl.infrastructure.entity.fact.VerificationFactory
        .newInstance();
    final VerificationDtlsList verificationDtlsList =
      verification.readByVDIEDLinkID(vdiedLinkKey);
    if (verificationDtlsList.dtls.size() > 0) {
      caseIDAndVDIEDLinkIDKey.caseID =
        verificationDtlsList.dtls.item(0).verificationLinkedID;
    }
    // END

    final SubmittedDocumentDetailsList documentsList =
      curam.verification.sl.infrastructure.fact.VerificationApplicationFactory
        .newInstance().listSubmittedDocuments(caseIDAndVDIEDLinkIDKey);

    for (final curam.verification.sl.infrastructure.struct.SubmittedDocumentDetails documentDetails : documentsList.dtls) {
      final AttachmentLinkID linkID = new AttachmentLinkID();
      linkID.attachmentLinkID = documentDetails.attachmentLinkID;
      rejectDocumentPendingReviewForVerification(linkID);
      bdmAttachmentLinkDtls.attachmentLinkID =
        documentDetails.attachmentLinkID;
      BDMAttachmentLinkFactory.newInstance().insert(bdmAttachmentLinkDtls);

    }
  }

  /**
   *
   * @param bdmAttachmentLinkDtls
   * @throws AppException
   * @throws InformationalException
   */
  public void
    validateRejectReason(final BDMAttachmentLinkDtls bdmAttachmentLinkDtls)
      throws AppException, InformationalException {

    // START Altered for 56003 Dev bug 55654 to fix Validation changes
    if (BDMATTACHREJECTIONREASONEntry.BDMVRR8004
      .equals(bdmAttachmentLinkDtls.rejectionReason)
      && bdmAttachmentLinkDtls.otherComments.isEmpty()) {
      throw new AppException(
        BDMENTVERIFICATION.ERR_VERIFICATION_PROOF_COMMENTS_MUST_BE_ENTERED_IF_REJ_REASON_OTHER);

    }
  }

  /**
   *
   * @param attachmentLinkID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public BDMAttachmentLinkDtls readBDMAttachmentLink(
    final long attachmentLinkID) throws AppException, InformationalException {

    final BDMAttachmentLinkKey bdmAttachmentLinkKey =
      new BDMAttachmentLinkKey();
    bdmAttachmentLinkKey.attachmentLinkID = attachmentLinkID;

    BDMAttachmentLinkDtls bdmAttachmentLinkDtls = new BDMAttachmentLinkDtls();
    if (attachmentLinkID != 0) {
      final NotFoundIndicator nFIndicator = new NotFoundIndicator();

      bdmAttachmentLinkDtls = BDMAttachmentLinkFactory.newInstance()
        .read(nFIndicator, bdmAttachmentLinkKey);

      if (nFIndicator.isNotFound()) {
        bdmAttachmentLinkDtls = new BDMAttachmentLinkDtls();
      }
    }

    return bdmAttachmentLinkDtls;
  }

  // validate attachment file extension
  private void validateAttachment(final AttachmentDtls details)
    throws AppException {

    final String attachmentName = details.attachmentName;
    final String fileExtension =
      attachmentName.substring(attachmentName.lastIndexOf(".") + 1);

    final boolean isValidFileExtension = Arrays
      .stream(
        Configuration.getProperty(EnvVars.BDM_ENV_UPLOAD_FILE_EXTENSION_LIST)
          .split(CuramConst.gkComma))
      .anyMatch(fileExtension::equalsIgnoreCase);
    if (!isValidFileExtension) {
      throw new AppException(
        BDMENTVERIFICATION.ERR_VERIFICATION_VALID_ATTACHMENT_EXTENSIONS);
    }
  }

  /**
   * Override OOTb validateBulkEvidenceVerificationAddProofPage as acceptance
   * status and rejection reason are aded to the screen
   *
   */
  @Override
  public BDMNewUserProvidedVerItemAndWizardDetails
    bdmValidateBulkEvidenceVerificationAddProofPage(
      final BDMNewUserProvidedVerificationItemDetails details,
      final ActionIDProperty arg2, final WizardStateID wizardStateID)
      throws AppException, InformationalException {

    final BDMNewUserProvidedVerItemAndWizardDetails bdmNewUserProvidedVerItemAndWizardDetails =
      new BDMNewUserProvidedVerItemAndWizardDetails();

    bdmNewUserProvidedVerItemAndWizardDetails.bdmNewUserProvdedVerificationItemDetails.newUserProvidedVerificationItemDetails =
      details.newUserProvidedVerificationItemDetails;

    final curam.core.facade.struct.ConcernRoleIDKey concernRoleIDKey =
      new curam.core.facade.struct.ConcernRoleIDKey();

    if (0 != details.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.participantConcernRoleID) {
      concernRoleIDKey.concernRoleID =
        details.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.participantConcernRoleID;
      details.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.participantNameOpt =
        ConcernRoleFactory.newInstance()
          .readConcernRoleName(concernRoleIDKey).concernRoleName;
    }

    final curam.verification.facade.infrastructure.struct.NewUserProvidedVerificationItemDetails newUserProvidedVerificationItemDetails =
      new curam.verification.facade.infrastructure.struct.NewUserProvidedVerificationItemDetails();
    newUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails
      .assign(details.newUserProvidedVerificationItemDetails);

    curam.verification.sl.infrastructure.fact.VerificationApplicationFactory
      .newInstance().validateBulkEvidenceVerificationAddProofPage(
        newUserProvidedVerificationItemDetails);
    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    if (StringUtil.isNullOrEmpty(
      bdmNewUserProvidedVerItemAndWizardDetails.bdmNewUserProvdedVerificationItemDetails.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.fileLocation)
      && !StringUtil.isNullOrEmpty(
        bdmNewUserProvidedVerItemAndWizardDetails.bdmNewUserProvdedVerificationItemDetails.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.fileReference)
      || !StringUtil.isNullOrEmpty(
        bdmNewUserProvidedVerItemAndWizardDetails.bdmNewUserProvdedVerificationItemDetails.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.fileLocation)
        && StringUtil.isNullOrEmpty(
          bdmNewUserProvidedVerItemAndWizardDetails.bdmNewUserProvdedVerificationItemDetails.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.fileReference)) {
      throw new AppException(
        BPOATTACHMENTLINK.ERR_ATTACHMENT_XRV_FILENAME_LOCATION_REFERENCE);
    }

    if (0 == wizardStateID.wizardStateID) {
      wizardStateID.wizardStateID = wizardPersistentState
        .create(bdmNewUserProvidedVerItemAndWizardDetails);
    } else {

      wizardPersistentState.modify(wizardStateID.wizardStateID,
        bdmNewUserProvidedVerItemAndWizardDetails);
    }
    bdmNewUserProvidedVerItemAndWizardDetails.wizardStateID = wizardStateID;
    return bdmNewUserProvidedVerItemAndWizardDetails;
  }

  /**
   *
   * Get file location information for display
   *
   * @param bdmfaAttachmentDetailsForRead
   */
  private void getFileLocationURL(
    final BDMReadVerificationAttachmentDetails bdmReadVerificationAttachmentDetails) {

    final String fileLocationURL =
      bdmReadVerificationAttachmentDetails.bdmDtls.details.readAttachmentDtls.fileLocation
        .trim();
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

    bdmReadVerificationAttachmentDetails.extrenalFileLink.fileName = fileName;
    bdmReadVerificationAttachmentDetails.extrenalFileLink.fileLocationURL =
      fileLocationURL;

  }

  /**
   * Get BDM User Provided VerificationItem Wizard Details
   *
   * @param WizardStateID
   * @throws AppException, InformationalException
   */
  @Override
  public BDMNewUserProvidedVerItemAndWizardDetails
    getBDMUserProvidedVerificationItemWizardDetails(final WizardStateID arg1)
      throws AppException, InformationalException {

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();
    BDMNewUserProvidedVerItemAndWizardDetails newUserProvidedVerItemAndWizardDetails =
      new BDMNewUserProvidedVerItemAndWizardDetails();

    if (0 != arg1.wizardStateID) {
      newUserProvidedVerItemAndWizardDetails =
        (BDMNewUserProvidedVerItemAndWizardDetails) wizardPersistentState
          .read(arg1.wizardStateID);
    } else {
      arg1.wizardStateID =
        wizardPersistentState.create(newUserProvidedVerItemAndWizardDetails);
      newUserProvidedVerItemAndWizardDetails.wizardStateID = arg1;

      newUserProvidedVerItemAndWizardDetails.bdmNewUserProvdedVerificationItemDetails.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.dateReceived =
        Date.getCurrentDate();

    }
    return newUserProvidedVerItemAndWizardDetails;

  }

  /**
   * Verify using new UserProvided Verification Item And Associated Evidences
   *
   * @param WizardStateID
   * @param ActionIDProperty actionIDProperty
   * @param BDMNewUserProvidedVerItemAndEvdDetails
   * @throws AppException, InformationalException
   */

  @Override
  public UserProvidedVerificationItemKey
    newUserProvidedVerificationItemAndAssociatedEvidences(
      final ActionIDProperty actionIDProperty, final WizardStateID arg3,
      BDMNewUserProvidedVerItemAndEvdDetails details)
      throws AppException, InformationalException {

    details = updateUserProvidedVerificationDetails(arg3, details);
    final curam.verification.sl.infrastructure.struct.UserProvidedVerificationItemKey userProvidedVerificationItemKeyStruct =
      this.persistUserProvidedVerificationItemWithAssociatedEvidence(arg3,
        details);
    return createFacadeReturnKey(userProvidedVerificationItemKeyStruct);

  }

  private BDMNewUserProvidedVerItemAndEvdDetails
    updateUserProvidedVerificationDetails(final WizardStateID wizardStateID,
      BDMNewUserProvidedVerItemAndEvdDetails details) {

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();
    BDMNewUserProvidedVerItemAndWizardDetails newUserProvidedVerItemAndWizardDetails =
      new BDMNewUserProvidedVerItemAndWizardDetails();
    if (wizardStateID.wizardStateID != 0) {
      newUserProvidedVerItemAndWizardDetails =
        (BDMNewUserProvidedVerItemAndWizardDetails) wizardPersistentState
          .read(wizardStateID.wizardStateID);

    }
    details = preserveUserProvidedCommentsDuringUpdate(
      newUserProvidedVerItemAndWizardDetails, details);
    return details;
  }

  private BDMNewUserProvidedVerItemAndEvdDetails
    preserveUserProvidedCommentsDuringUpdate(
      final BDMNewUserProvidedVerItemAndWizardDetails newUserProvidedVerItemAndWizardDetails,
      final BDMNewUserProvidedVerItemAndEvdDetails details) {

    final String userSubmittedVerificationComments =
      details.bdmNewUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.comments;
    // BEGIN, CR00451497, GK
    details.bdmNewUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails =
      newUserProvidedVerItemAndWizardDetails.bdmNewUserProvdedVerificationItemDetails.newUserProvidedVerificationItemDetails;
    // END, CR00451497
    // BEGIN, 170463, dmorton
    details.comments = userSubmittedVerificationComments;
    // END, 170463
    return details;
  }

  private UserProvidedVerificationItemKey createFacadeReturnKey(
    final curam.verification.sl.infrastructure.struct.UserProvidedVerificationItemKey userProvidedVerificationItemKeyStuct) {

    UserProvidedVerificationItemKey facadeReturnKey =
      new UserProvidedVerificationItemKey();
    facadeReturnKey.userProvidedVerificationItemKey =
      userProvidedVerificationItemKeyStuct;
    facadeReturnKey =
      includeWarningMessagesInFacadeReturnKey(facadeReturnKey);
    return facadeReturnKey;
  }

  private UserProvidedVerificationItemKey
    includeWarningMessagesInFacadeReturnKey(
      final UserProvidedVerificationItemKey facadeReturnKey) {

    final String[] messages =
      TransactionInfo.getInformationalManager().obtainInformationalAsString();
    for (int i = 0; i != messages.length; i++) {
      final VerificationMessage warning = new VerificationMessage();
      warning.message = messages[i];
      facadeReturnKey.infoMsgListOpt.addRef(warning);
    }
    return facadeReturnKey;
  }

  curam.verification.sl.infrastructure.struct.UserProvidedVerificationItemKey
    persistUserProvidedVerificationItemWithAssociatedEvidence(
      final WizardStateID wizardStateID,
      final BDMNewUserProvidedVerItemAndEvdDetails details)
      throws AppException, InformationalException {

    return new BDMVerificationApplicationHelper()
      .newUserProvidedVerificationItemAndAssociatedEvidences(details);
  }

}
