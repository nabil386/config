package curam.ca.gc.bdm.facade.impl;

import curam.ca.gc.bdm.communication.impl.BDMCommunicationHelper;
import curam.ca.gc.bdm.facade.participant.struct.BDMCommunicationAndListRowActionDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMCommunicationDetailList;
import curam.ca.gc.bdm.facade.struct.BDMListCaseAttachmentDetails;
import curam.ca.gc.bdm.facade.struct.BDMModifyCaseAttachmentDetails;
import curam.ca.gc.bdm.facade.struct.BDMReadCaseAttachmentDetails;
import curam.ca.gc.bdm.facade.struct.BDMReadCaseAttachmentForModifyDetails;
import curam.ca.gc.bdm.facade.struct.BDMReadCaseAttachmentOut;
import curam.ca.gc.bdm.sl.attachment.fact.BDMMaintainAttachmentFactory;
import curam.ca.gc.bdm.sl.attachment.intf.BDMMaintainAttachment;
import curam.codetable.CASETYPECODE;
import curam.codetable.RECORDSTATUS;
import curam.core.facade.fact.CaseFactory;
import curam.core.facade.fact.IntegratedCaseFactory;
import curam.core.facade.intf.Case;
import curam.core.facade.intf.IntegratedCase;
import curam.core.facade.struct.CaseContextDescriptionKey;
import curam.core.facade.struct.CaseMenuData;
import curam.core.facade.struct.CommunicationDetailList;
import curam.core.facade.struct.ListCaseAttachmentKey;
import curam.core.facade.struct.ListCommunicationsDtls;
import curam.core.facade.struct.ListCommunicationsKey;
import curam.core.facade.struct.ReadCaseAttachmentForModifyKey;
import curam.core.facade.struct.ReadCaseAttachmentKey;
import curam.core.sl.fact.AlternateNameFactory;
import curam.core.sl.intf.AlternateName;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseKey;
import curam.core.struct.CaseTypeCode;
import curam.serviceplans.facade.struct.ServicePlanSecurityKey;
import curam.serviceplans.sl.impl.ServicePlanSecurity;
import curam.serviceplans.sl.impl.ServicePlanSecurityImplementationFactory;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Trace;

public class BDMCase extends curam.ca.gc.bdm.facade.base.BDMCase {

  final AlternateName alternateNameObj = AlternateNameFactory.newInstance();

  Case caseObj = CaseFactory.newInstance();

  @Override
  public ListCommunicationsDtls
    listCaseCommunications(final ListCommunicationsKey key)
      throws AppException, InformationalException {

    return caseObj.listCaseCommunications(key);
  }

  @Override
  public BDMCommunicationDetailList
    listCaseCommunication(final ListCommunicationsKey key)
      throws AppException, InformationalException {

    final BDMCommunicationDetailList bdmCommList =
      new BDMCommunicationDetailList();
    final CommunicationDetailList commList =
      caseObj.listCaseCommunication(key);
    bdmCommList.assign(commList);
    for (int i = 0; i < commList.communicationDtls.size(); i++) {
      bdmCommList.communicationDtls.get(i).commDtls =
        commList.communicationDtls.get(i);
      final BDMCommunicationAndListRowActionDetails item =
        bdmCommList.communicationDtls.get(i);
      final BDMCommunicationHelper commHelper = new BDMCommunicationHelper();
      commHelper.determineActionMenuIndicator(item);

    }

    return bdmCommList;
  }

  /**
   * Create an attachment for a case.
   *
   * @param details
   * Case attachment details.
   */
  @Override
  public void createCaseAttachment(
    final curam.ca.gc.bdm.facade.struct.BDMCreateCaseAttachmentDetails details)
    throws AppException, InformationalException {

    // MaintainAttachment manipulation variable
    final BDMMaintainAttachment bdmmaintainAttachmentObj =
      BDMMaintainAttachmentFactory.newInstance();

    // Case Header manipulation variables
    final curam.core.intf.CaseHeader caseHeaderObj =
      curam.core.fact.CaseHeaderFactory.newInstance();
    final CaseKey caseKey = new CaseKey();

    // set case key
    caseKey.caseID = details.dtls.createCaseAttachmentDetails.caseID;

    // read case type code
    final CaseTypeCode caseTypeCode = caseHeaderObj.readCaseTypeCode(caseKey);

    // if case type is service plan, check service plan security
    if (caseTypeCode.caseTypeCode.equals(CASETYPECODE.SERVICEPLAN)) {

      // ServicePlanDelivery facade
      final curam.serviceplans.facade.intf.ServicePlanDelivery servicePlanDeliveryObj =
        curam.serviceplans.facade.fact.ServicePlanDeliveryFactory
          .newInstance();
      final ServicePlanSecurityKey servicePlanSecurityKey =
        new ServicePlanSecurityKey();

      // register the service plan security implementation
      ServicePlanSecurityImplementationFactory.register();

      // set the key
      servicePlanSecurityKey.caseID = caseKey.caseID;
      servicePlanSecurityKey.securityCheckType =
        ServicePlanSecurity.kCreateSecurityCheck;

      // check security
      servicePlanDeliveryObj.checkSecurity(servicePlanSecurityKey);
    }

    // Create the case attachment
    bdmmaintainAttachmentObj.insertCaseAttachmentDetails(details);
  }

  /**
   * List all the attachments for a case.
   *
   * @param key
   * Contains case identifier.
   *
   * @return List of attachments for a case.
   */
  @Override
  public BDMListCaseAttachmentDetails
    listCaseAttachment(final ListCaseAttachmentKey key)
      throws AppException, InformationalException {

    // MaintainAttachment manipulation variable
    final BDMMaintainAttachment maintainAttachmentObj =
      BDMMaintainAttachmentFactory.newInstance();

    // Create return object
    final BDMListCaseAttachmentDetails listCaseAttachmentDetails =
      new BDMListCaseAttachmentDetails();

    // Case Header manipulation variables
    final curam.core.intf.CaseHeader caseHeaderObj =
      curam.core.fact.CaseHeaderFactory.newInstance();
    final CaseKey caseKey = new CaseKey();

    // set case key
    caseKey.caseID = key.attachmentCaseID.caseID;

    // read case type code
    final CaseTypeCode caseTypeCode = caseHeaderObj.readCaseTypeCode(caseKey);

    // if case type is service plan, check service plan security
    if (caseTypeCode.caseTypeCode.equals(CASETYPECODE.SERVICEPLAN)) {

      // ServicePlanDelivery facade
      final curam.serviceplans.facade.intf.ServicePlanDelivery servicePlanDeliveryObj =
        curam.serviceplans.facade.fact.ServicePlanDeliveryFactory
          .newInstance();
      final ServicePlanSecurityKey servicePlanSecurityKey =
        new ServicePlanSecurityKey();

      // register the service plan security implementation
      ServicePlanSecurityImplementationFactory.register();

      // set the key
      servicePlanSecurityKey.caseID = caseKey.caseID;
      servicePlanSecurityKey.securityCheckType =
        ServicePlanSecurity.kReadSecurityCheck;

      // check security
      servicePlanDeliveryObj.checkSecurity(servicePlanSecurityKey);
    }

    // Read the list of attachments
    listCaseAttachmentDetails.attachmentDetails =
      maintainAttachmentObj.readCaseAttachments(key.attachmentCaseID);

    // CaseContextDescriptionKey object
    final CaseContextDescriptionKey caseContextDescriptionKey =
      new CaseContextDescriptionKey();

    // Set key to read context description
    caseContextDescriptionKey.caseID = key.attachmentCaseID.caseID;

    // Read context description from OOTB readCaseContextDescription and assign
    // to output object

    final Case caseObj1 = CaseFactory.newInstance();
    listCaseAttachmentDetails.contextDescription =
      caseObj1.readCaseContextDescription(caseContextDescriptionKey);

    // Set the case menu data
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();

    caseHeaderKey.caseID = caseContextDescriptionKey.caseID;

    // Reads the service Plan menu data
    final CaseMenuData caseMenuData =
      caseObj1.getCaseMenuDataDetails(caseHeaderKey);

    // Will be not set for specific cases e.g. stand alone cases
    if (caseMenuData != null) {
      listCaseAttachmentDetails.menuData.menuData = caseMenuData.menuData;
    }

    return listCaseAttachmentDetails;
  }

  @Override
  public BDMReadCaseAttachmentDetails
    readCaseAttachment(final ReadCaseAttachmentKey key)
      throws AppException, InformationalException {

    final BDMReadCaseAttachmentDetails readCaseAttachmentDetails =
      new BDMReadCaseAttachmentDetails();

    // MaintainAttachment manipulation variable
    final BDMMaintainAttachment bdmmaintainAttachmentObj =
      BDMMaintainAttachmentFactory.newInstance();

    final BDMReadCaseAttachmentOut out =
      bdmmaintainAttachmentObj.readCaseAttachment(key.readCaseAttachmentIn);

    // assign CaseAttachmentDetails to return struct
    readCaseAttachmentDetails.fileSource = out.fileSource;

    readCaseAttachmentDetails.readCaseAttchDtls.readCaseAttachmentOut
      .assign(out.dtls);
    // readCaseAttachmentDetails.readCaseAttchDtls.readCaseAttachmentOut =
    // out.dtls;

    getFileLocationURL(readCaseAttachmentDetails);

    // Case Header manipulation variables
    final curam.core.intf.CaseHeader caseHeaderObj =
      curam.core.fact.CaseHeaderFactory.newInstance();
    final CaseKey caseKey = new CaseKey();

    // set case key
    caseKey.caseID = out.dtls.caseID;

    // read case type code
    final CaseTypeCode caseTypeCode = caseHeaderObj.readCaseTypeCode(caseKey);

    // if case type is service plan, check service plan security
    if (caseTypeCode.caseTypeCode.equals(CASETYPECODE.SERVICEPLAN)) {

      // ServicePlanDelivery facade
      final curam.serviceplans.facade.intf.ServicePlanDelivery servicePlanDeliveryObj =
        curam.serviceplans.facade.fact.ServicePlanDeliveryFactory
          .newInstance();
      final ServicePlanSecurityKey servicePlanSecurityKey =
        new ServicePlanSecurityKey();

      // register the service plan security implementation
      ServicePlanSecurityImplementationFactory.register();

      // set the key
      servicePlanSecurityKey.caseID = caseKey.caseID;
      servicePlanSecurityKey.securityCheckType =
        ServicePlanSecurity.kReadSecurityCheck;

      // check security
      servicePlanDeliveryObj.checkSecurity(servicePlanSecurityKey);
    }

    // BEGIN, 213970,

    if (out.dtls.statusCode.equalsIgnoreCase(RECORDSTATUS.CANCELLED)) {
      readCaseAttachmentDetails.readCaseAttchDtls.cancelledInd = true;
    }
    // END, 213970

    return readCaseAttachmentDetails;
  }

  // ___________________________________________________________________________
  /**
   * Cancel an attachment on a case.
   *
   * @param key
   * Case attachment key.
   */
  @Override

  public void cancelCaseAttachment(
    final curam.core.facade.struct.CancelCaseAttachmentKey key)
    throws AppException, InformationalException {

    caseObj.cancelCaseAttachment(key);
    // Case maintenance object

  }

  // ___________________________________________________________________________
  /**
   * Modify case attachment.
   *
   * @param details
   * Case Attachment details.
   */
  @Override
  public void
    modifyCaseAttachment(final BDMModifyCaseAttachmentDetails details)
      throws AppException, InformationalException {

    // MaintainAttachment manipulation variable
    final BDMMaintainAttachment maintainAttachmentObj =
      BDMMaintainAttachmentFactory.newInstance();

    // Case Header manipulation variables
    final curam.core.intf.CaseHeader caseHeaderObj =
      curam.core.fact.CaseHeaderFactory.newInstance();
    final CaseKey caseKey = new CaseKey();

    // set case key
    caseKey.caseID = details.dtls.modifyAttachmentDetails.caseID;

    // read case type code
    final CaseTypeCode caseTypeCode = caseHeaderObj.readCaseTypeCode(caseKey);

    // if case type is service plan, check service plan security
    if (caseTypeCode.caseTypeCode.equals(CASETYPECODE.SERVICEPLAN)) {

      // ServicePlanDelivery facade
      final curam.serviceplans.facade.intf.ServicePlanDelivery servicePlanDeliveryObj =
        curam.serviceplans.facade.fact.ServicePlanDeliveryFactory
          .newInstance();
      final ServicePlanSecurityKey servicePlanSecurityKey =
        new ServicePlanSecurityKey();

      // register the service plan security implementation
      ServicePlanSecurityImplementationFactory.register();

      // set the key
      servicePlanSecurityKey.caseID = caseKey.caseID;
      servicePlanSecurityKey.securityCheckType =
        ServicePlanSecurity.kMaintainSecurityCheck;

      // check security
      servicePlanDeliveryObj.checkSecurity(servicePlanSecurityKey);
    }

    // Modify the Case Attachment
    maintainAttachmentObj.modifyCaseAttachment(details);
  }

  /**
   * Reads the details for a case attachment and the list of available case
   * members.
   *
   * @param key
   * case attachment identifier, case identifier.
   *
   * @return Details of the case attachment and list of case members
   */
  @Override
  public BDMReadCaseAttachmentForModifyDetails
    readCaseAttachmentDetailsForModiy(
      final ReadCaseAttachmentForModifyKey key)
      throws AppException, InformationalException {

    // return struct
    final BDMReadCaseAttachmentForModifyDetails details =
      new BDMReadCaseAttachmentForModifyDetails();

    // Instantiate facade object for integrated case
    final IntegratedCase integratedCaseObj =
      IntegratedCaseFactory.newInstance();

    // Populate return struct

    final BDMReadCaseAttachmentDetails bdmReadCaseAttachmentDetails =
      readCaseAttachment(key.attachmentKey);
    details.dtls.attachmentDetails =
      bdmReadCaseAttachmentDetails.readCaseAttchDtls;
    details.dtls.listMemberDetails =
      integratedCaseObj.listMemberDetails(key.listMemberKey);
    details.fileSource = bdmReadCaseAttachmentDetails.fileSource;

    return details;
  }

  /**
   *
   * Get file location information for display
   *
   * @param bdmReadCaseAttachmentDetails
   */
  private void getFileLocationURL(
    final BDMReadCaseAttachmentDetails bdmReadCaseAttachmentDetails) {

    final String fileLocationURL =
      bdmReadCaseAttachmentDetails.readCaseAttchDtls.readCaseAttachmentOut.fileLocation
        .trim();

    String fileName = "";

    try {
      fileName =
        fileLocationURL.substring(fileLocationURL.lastIndexOf("\\") + 1);

      Trace.kTopLevelLogger.info("Filename : " + fileName);
    } catch (final Exception e) {

      Trace.kTopLevelLogger.info(
        fileLocationURL + " is not valid file." + e.getLocalizedMessage());
    }

    fileLocationURL.replace("\\", "/");
    bdmReadCaseAttachmentDetails.fileExternalLink.fileLocationURL =
      fileLocationURL;
    bdmReadCaseAttachmentDetails.fileExternalLink.fileName = fileName;

  }

}
