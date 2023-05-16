package curam.ca.gc.bdm.rest.bdmuaacceptedverificationsapi.impl;

import curam.ca.gc.bdm.rest.bdmuaacceptedverificationsapi.struct.AcceptedVerificaiton;
import curam.ca.gc.bdm.rest.bdmuaacceptedverificationsapi.struct.AcceptedVerificationsList;
import curam.citizenworkspace.rest.facade.fact.ApplicationAPIFactory;
import curam.citizenworkspace.rest.facade.intf.ApplicationAPI;
import curam.citizenworkspace.rest.facade.struct.UASubmittedApplication;
import curam.citizenworkspace.rest.facade.struct.UASubmittedApplicationList;
import curam.citizenworkspace.rest.facade.struct.UASubmittedApplicationProgram;
import curam.core.fact.CaseHeaderFactory;
import curam.core.intf.CaseHeader;
import curam.core.sl.fact.AttachmentInfoFactory;
import curam.core.sl.intf.AttachmentInfo;
import curam.core.struct.AttachmentKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.ConcernRoleKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.verification.sl.infrastructure.entity.fact.VDIEDLinkFactory;
import curam.verification.sl.infrastructure.entity.fact.VerificationAttachmentLinkFactory;
import curam.verification.sl.infrastructure.entity.fact.VerificationFactory;
import curam.verification.sl.infrastructure.entity.fact.VerificationItemProvidedFactory;
import curam.verification.sl.infrastructure.entity.intf.VDIEDLink;
import curam.verification.sl.infrastructure.entity.intf.Verification;
import curam.verification.sl.infrastructure.entity.intf.VerificationAttachmentLink;
import curam.verification.sl.infrastructure.entity.intf.VerificationItemProvided;
import curam.verification.sl.infrastructure.entity.struct.VDIEDLinkKey;
import curam.verification.sl.infrastructure.entity.struct.VDIEDLinkKeyList;
import curam.verification.sl.infrastructure.entity.struct.VerificationAttachmentLinkDtls;
import curam.verification.sl.infrastructure.entity.struct.VerificationAttachmentLinkKey;
import curam.verification.sl.infrastructure.entity.struct.VerificationAttachmentSummaryDetails;
import curam.verification.sl.infrastructure.entity.struct.VerificationAttachmentSummaryDetailsList;
import curam.verification.sl.infrastructure.entity.struct.VerificationDtlsList;
import curam.verification.sl.infrastructure.entity.struct.VerificationItemProvidedDtls;
import curam.verification.sl.infrastructure.entity.struct.VerificationItemProvidedDtlsList;
import curam.verification.sl.infrastructure.entity.struct.VerificationItemProvidedKey;

public class BDMUAAcceptedVerificationsAPI extends
  curam.ca.gc.bdm.rest.bdmuaacceptedverificationsapi.base.BDMUAAcceptedVerificationsAPI {

  public BDMUAAcceptedVerificationsAPI() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public AcceptedVerificationsList listAcceptedVerifications()
    throws AppException, InformationalException {

    // Get list of applications for user
    final ApplicationAPI applicationAPIObj =
      ApplicationAPIFactory.newInstance();
    final UASubmittedApplicationList uaSubmittedApplicationsList =
      applicationAPIObj.listSubmittedApplications();

    final AcceptedVerificationsList acceptedVerificationsList =
      new AcceptedVerificationsList();

    for (final UASubmittedApplication uaSubmittedApplication : uaSubmittedApplicationsList.data) {
      // Get a list of VDIEDLinks that relate to submitted Active
      // Verifications
      final VDIEDLink vdIedLinkObj = VDIEDLinkFactory.newInstance();
      final UASubmittedApplicationProgram uaSubmittedApplicationProgram =
        uaSubmittedApplication.applicationPrograms.get(0);

      final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
      final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
      caseHeaderKey.caseID = uaSubmittedApplicationProgram.case_id;
      final CaseHeaderDtls caseHeaderDtls = caseHeaderObj.read(caseHeaderKey);

      final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
      concernRoleKey.concernRoleID = caseHeaderDtls.concernRoleID;
      final VDIEDLinkKeyList vdIEDLinkKeyList =
        vdIedLinkObj.searchVDIEDLinkIDsByConcernRoleID(concernRoleKey);
      for (final VDIEDLinkKey vdIEDLinkKey : vdIEDLinkKeyList.dtls) {
        // Get the VerificationItemProvidedID so we can search the
        // VerificationAttachmentLink table

        final VerificationItemProvided verificationItemProvidedObj =
          VerificationItemProvidedFactory.newInstance();
        final VerificationItemProvidedDtlsList verificationItemsProvidedDtlsList =
          verificationItemProvidedObj.searchByVDIEDLinkID(vdIEDLinkKey);

        for (final VerificationItemProvidedDtls verificationItemProvidedDtls : verificationItemsProvidedDtlsList.dtls) {

          final VerificationAttachmentLink verificationAttachemtLinkObj =
            VerificationAttachmentLinkFactory.newInstance();
          final VerificationItemProvidedKey verificationItemProvidedKey =
            new VerificationItemProvidedKey();
          verificationItemProvidedKey.verificationItemProvidedID =
            verificationItemProvidedDtls.verificationItemProvidedID;

          final VerificationAttachmentSummaryDetailsList verificationAttachmentSummaryDetailsList =
            verificationAttachemtLinkObj
              .searchAttachmentByVerificationItemProvided(
                verificationItemProvidedKey);

          for (final VerificationAttachmentSummaryDetails verificationAttachmentSummaryDetails : verificationAttachmentSummaryDetailsList.dtls) {

            final AcceptedVerificaiton acceptedVerificaiton =
              new AcceptedVerificaiton();
            // Fill Fields from VerificationAttachmentLinkTable
            acceptedVerificaiton.fileName =
              verificationAttachmentSummaryDetails.description;
            acceptedVerificaiton.fileType =
              verificationAttachmentSummaryDetails.documentType;
            acceptedVerificaiton.relatedCaseID =
              Long.toString(uaSubmittedApplicationProgram.case_id);
            acceptedVerificaiton.relatedCaseReference =
              caseHeaderDtls.caseReference;

            final VerificationAttachmentLinkKey verificationAttachmentLinkKey =
              new VerificationAttachmentLinkKey();
            verificationAttachmentLinkKey.verificationAttachmentLinkID =
              verificationAttachmentSummaryDetails.verificationAttachmentLinkID;
            final VerificationAttachmentLinkDtls verificationAttachemntLinkDtls =
              verificationAttachemtLinkObj
                .read(verificationAttachmentLinkKey);

            acceptedVerificaiton.fileID =
              Long.toString(verificationAttachemntLinkDtls.attachmentID);

            // Get verificationID from Verification table

            acceptedVerificaiton.verificationID =
              getVerificationID(vdIEDLinkKey);

            acceptedVerificaiton.fileType =
              getFileType(verificationAttachemntLinkDtls);

            acceptedVerificationsList.dtls.add(acceptedVerificaiton);
          }

        }

      }
    }
    return acceptedVerificationsList;
  }

  /*
   * Returns VerificationID in string form given the VDIEDLinkKey
   *
   */
  public static String getVerificationID(final VDIEDLinkKey vdIEDLinkKey)
    throws AppException, InformationalException {

    String verificationID = new String();
    final Verification verificationObj = VerificationFactory.newInstance();

    final VerificationDtlsList verificationDtlsList =
      verificationObj.readByVDIEDLinkID(vdIEDLinkKey);

    if (!verificationDtlsList.dtls.isEmpty()) {
      verificationID =
        Long.toString(verificationDtlsList.dtls.get(0).verificationID);
    }
    return verificationID;
  }

  /*
   * Returns Verification File Type in string form given the AttachmentID
   *
   */
  public static String getFileType(
    final VerificationAttachmentLinkDtls verificationAttachemntLinkDtls)
    throws AppException, InformationalException {

    String fileType = new String();

    final AttachmentInfo attachmentInfoObj =
      AttachmentInfoFactory.newInstance();

    final AttachmentKey attachmentKey = new AttachmentKey();

    attachmentKey.attachmentID = verificationAttachemntLinkDtls.attachmentID;
    final curam.core.struct.AttachmentInfo attachmentInfo =
      attachmentInfoObj.readInfoByAttachmentID(attachmentKey);
    fileType = attachmentInfo.documentClassification;
    return fileType;
  }

}
