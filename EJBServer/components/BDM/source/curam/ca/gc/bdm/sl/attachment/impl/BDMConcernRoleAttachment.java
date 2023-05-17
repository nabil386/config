package curam.ca.gc.bdm.sl.attachment.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.entity.attachment.fact.BDMConcernRoleAttachmentLinkFactory;
import curam.ca.gc.bdm.entity.attachment.intf.BDMConcernRoleAttachmentLink;
import curam.ca.gc.bdm.entity.attachment.struct.BDMConcernRoleAttachmentLinkDtls;
import curam.ca.gc.bdm.entity.attachment.struct.BDMConcernRoleAttachmentLinkKey;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskSkillTypeKey;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskSkillTypeList;
import curam.ca.gc.bdm.facade.communication.struct.BDMConcernRoleAttachmentDetails;
import curam.ca.gc.bdm.facade.communication.struct.BDMConcernRoleListAttachmentDetails;
import curam.ca.gc.bdm.rest.bdmuacaseworkerverificationtaskmanagerapi.struct.BDMUACaseWorkerVerificationEnactKey;
import curam.ca.gc.bdm.sl.attachment.fact.BDMMaintainAttachmentTaskFactory;
import curam.ca.gc.bdm.sl.bdmworkallocation.fact.BDMWorkAllocationTaskFactory;
import curam.ca.gc.bdm.sl.bdmworkallocation.intf.BDMWorkAllocationTask;
import curam.ca.gc.bdm.sl.bdmworkallocation.struct.BDMDocumentTypeKey;
import curam.ca.gc.bdm.sl.bdmworkallocation.struct.BDMVSGTaskCreateDetails;
import curam.ca.gc.bdm.sl.bdmworkallocation.struct.BDMWorkQueueID;
import curam.codetable.ASSIGNEETYPE;
import curam.codetable.DOCUMENTTYPE;
import curam.codetable.DUPLICATESTATUS;
import curam.codetable.SKILLTYPE;
import curam.codetable.TASKPRIORITY;
import curam.core.facade.infrastructure.struct.PersonAndEvidenceTypeList;
import curam.core.facade.struct.ConcernRoleAttachmentDetails;
import curam.core.facade.struct.ConcernRoleAttachmentDetailsList;
import curam.core.fact.ConcernRoleFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.intf.ConcernRole;
import curam.core.sl.entity.struct.DuplicateForConcernRoleDtls;
import curam.core.sl.entity.fact.ConcernRoleDuplicateFactory;
import curam.core.sl.entity.struct.DuplicateForConcernRoleDtls;
import curam.core.sl.fact.ConcernRoleAttachmentFactory;
import curam.core.sl.intf.ConcernRoleAttachment;
import curam.core.struct.ConcernRoleAttachmentLinkDtls;
import curam.core.struct.ConcernRoleAttachmentLinkKey;
import curam.core.struct.ConcernRoleAttachmentLinkReadmultiKey;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleIDStatusCodeKey;
import curam.core.struct.ConcernRoleKey;
import curam.pdc.facade.fact.PDCPersonFactory;
import curam.pdc.facade.intf.PDCPerson;
import curam.pdc.facade.struct.PDCEvidenceDetailsList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.NotFoundIndicator;
import curam.util.workflow.impl.EnactmentService;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * */

/**
 *
 * */

public class BDMConcernRoleAttachment
  extends curam.ca.gc.bdm.sl.attachment.base.BDMConcernRoleAttachment {

  /**
   * Reads an attachment from a concern role
   *
   * @param key
   * The key for the link table
   */
  @Override
  public BDMConcernRoleAttachmentDetails
    readConcernRoleAttachment(final BDMConcernRoleAttachmentLinkKey key)
      throws AppException, InformationalException {

    final ConcernRoleAttachment concernRoleAttachmentObj =
      ConcernRoleAttachmentFactory.newInstance();

    // ##### Set key to link key
    final ConcernRoleAttachmentLinkKey linkKey =
      new ConcernRoleAttachmentLinkKey();
    linkKey.attachmentLinkID = key.attachmentLinkID;

    // Call OOTB readConcernRoleAttachment
    final ConcernRoleAttachmentDetails concernRoleAttachmentDetails =
      concernRoleAttachmentObj.readConcernRoleAttachment(linkKey);

    final BDMConcernRoleAttachmentDetails bdmConcernRoleDetails =
      new BDMConcernRoleAttachmentDetails();

    final BDMConcernRoleAttachmentLink bdmConcernRoleAttachmentLinkObj =
      BDMConcernRoleAttachmentLinkFactory.newInstance();

    // ## read from Custom Table BDMConcernRoleAttachmentLink
    final BDMConcernRoleAttachmentLinkDtls bdmLinkDtls =
      bdmConcernRoleAttachmentLinkObj.read(key);

    bdmConcernRoleDetails.fileSource = bdmLinkDtls.fileSource;

    bdmConcernRoleDetails.dtls = concernRoleAttachmentDetails;
    // return concernRoleAttachmentDetails;
    return bdmConcernRoleDetails;
  }

  @Override
  public BDMConcernRoleAttachmentLinkDtls
    createConcernRoleAttachment(final BDMConcernRoleAttachmentDetails details)
      throws AppException, InformationalException {

    final ConcernRoleAttachment concernRoleAttachmentObj =
      ConcernRoleAttachmentFactory.newInstance();

    // details.dtls.linkDtls.concernRoleID
    final BDMConcernRoleAttachmentLink bdmConcernRoleAttachmentLinkObj =
      BDMConcernRoleAttachmentLinkFactory.newInstance();

    final ConcernRoleAttachmentLinkDtls concernRoleAttachmentLinkDtls =
      concernRoleAttachmentObj.createConcernRoleAttachment(details.dtls);

    final BDMConcernRoleAttachmentLinkDtls bdmConcernRoleAttachmentLinkDtls =
      new BDMConcernRoleAttachmentLinkDtls();

    bdmConcernRoleAttachmentLinkDtls.attachmentLinkID =
      concernRoleAttachmentLinkDtls.attachmentLinkID;
    bdmConcernRoleAttachmentLinkDtls.fileSource = details.fileSource;

    bdmConcernRoleAttachmentLinkObj.insert(bdmConcernRoleAttachmentLinkDtls);

    // 9066:: BEGIN - create task on upload document to person
    if (DOCUMENTTYPE.REQUEST_FOR_RECONSIDERATION
      .equals(details.dtls.attachmentDtls.documentType)) {
      enactRFRTaskWorkFlow(details.dtls.attachmentDtls.documentType,
        details.dtls.linkDtls.concernRoleID);
    } else {
      enactTaskWorkFlow(details.dtls.attachmentDtls.documentType,
        details.dtls.linkDtls.concernRoleID);
    }

    // 9066:: END

    // START: Task 94070: DEV: Person Attachment Task Implementation
    BDMMaintainAttachmentTaskFactory.newInstance()
      .createPersonAttachmentTask(details);
    // START: Task 94070: DEV: Person Attachment Task Implementation

    return bdmConcernRoleAttachmentLinkDtls;
  }

  /**
   * This method searches concern role attachments according to the specified
   * concern role.
   *
   * @param rmKey
   * Key containing the concernRoleID
   * @return list A list of concern role attachments
   */
  @Override
  public BDMConcernRoleListAttachmentDetails searchConcernRoleAttachment(
    final ConcernRoleAttachmentLinkReadmultiKey rmKey)
    throws AppException, InformationalException {

    final ConcernRoleAttachment concernRoleAttachmentObj =
      ConcernRoleAttachmentFactory.newInstance();

    ConcernRoleAttachmentDetailsList concernRoleAttachmentDetailsList =
      new ConcernRoleAttachmentDetailsList();
    concernRoleAttachmentDetailsList =
      concernRoleAttachmentObj.searchConcernRoleAttachment(rmKey);

    // Start - Get duplication person of the original person, if any...
    final curam.core.sl.entity.intf.ConcernRoleDuplicate concernRoleDuplicateObj =
      ConcernRoleDuplicateFactory.newInstance();
    curam.core.sl.entity.struct.DuplicateForConcernRoleDtlsList duplicateForConcernRoleDtlsList =
      new curam.core.sl.entity.struct.DuplicateForConcernRoleDtlsList();

    // Set an indicator if this concern has duplicates
    final ConcernRoleIDStatusCodeKey concernRoleIDStatusCodeKey =
      new ConcernRoleIDStatusCodeKey();

    // Set the concern role id and status
    concernRoleIDStatusCodeKey.concernRoleID = rmKey.concernRoleID;
    concernRoleIDStatusCodeKey.statusCode = DUPLICATESTATUS.UNMARKED;

    // Check if this concern role has any duplicates
    duplicateForConcernRoleDtlsList = concernRoleDuplicateObj
      .searchByOriginalConcernRoleIDNotStatus(concernRoleIDStatusCodeKey);

    ConcernRoleAttachmentDetailsList dupConcernRoleAttachmentDetailsList =
      new ConcernRoleAttachmentDetailsList();

    if (duplicateForConcernRoleDtlsList.dtls.size() > 0) {

      for (final DuplicateForConcernRoleDtls duplicateForConcernRoleDtls : duplicateForConcernRoleDtlsList.dtls) {

        final ConcernRoleAttachmentLinkReadmultiKey duplicateConcernKey =
          new ConcernRoleAttachmentLinkReadmultiKey();
        duplicateConcernKey.concernRoleID =
          duplicateForConcernRoleDtls.duplicateConcernRoleID;
        dupConcernRoleAttachmentDetailsList = concernRoleAttachmentObj
          .searchConcernRoleAttachment(duplicateConcernKey);
      }
    }

    final BDMConcernRoleListAttachmentDetails bdmConcernRoleAttachmentDetailsList =
      new BDMConcernRoleListAttachmentDetails();

    // ##
    final BDMConcernRoleAttachmentLink bdmlinkObj =
      BDMConcernRoleAttachmentLinkFactory.newInstance();
    // Search on concern role

    ConcernRoleAttachmentLinkDtls concernRoleAttachmentLinkDtls;
    BDMConcernRoleAttachmentLinkKey bdmKEy;
    ConcernRoleAttachmentLinkDtls dupConcernRoleAttachmentLinkDtls;

    // Populate list for original
    for (int i = 0; i < concernRoleAttachmentDetailsList.attachList
      .size(); i++) {
      concernRoleAttachmentLinkDtls =
        concernRoleAttachmentDetailsList.attachList.item(i);

      // concernRoleAttachmentLinkDtls = linkDtlsList.dtls.item(i);

      bdmConcernRoleAttachmentDetailsList.concernRoleAttchmentDetailsList.attachList
        .addRef(concernRoleAttachmentLinkDtls);

      bdmKEy = new BDMConcernRoleAttachmentLinkKey();
      bdmKEy.attachmentLinkID =
        concernRoleAttachmentLinkDtls.attachmentLinkID;

      final BDMConcernRoleAttachmentLinkDtls bdmDtls =
        bdmlinkObj.read(bdmKEy);

      bdmConcernRoleAttachmentDetailsList.dtls.addRef(bdmDtls);
    }

    // Populate list for duplicate
    for (int i = 0; i < dupConcernRoleAttachmentDetailsList.attachList
      .size(); i++) {
      dupConcernRoleAttachmentLinkDtls =
        dupConcernRoleAttachmentDetailsList.attachList.item(i);

      // concernRoleAttachmentLinkDtls = linkDtlsList.dtls.item(i);

      bdmConcernRoleAttachmentDetailsList.concernRoleAttchmentDetailsList.attachList
        .addRef(dupConcernRoleAttachmentLinkDtls);

      bdmKEy = new BDMConcernRoleAttachmentLinkKey();
      bdmKEy.attachmentLinkID =
        dupConcernRoleAttachmentLinkDtls.attachmentLinkID;

      final BDMConcernRoleAttachmentLinkDtls bdmDtls =
        bdmlinkObj.read(bdmKEy);

      bdmConcernRoleAttachmentDetailsList.dtls.addRef(bdmDtls);
    }

    return bdmConcernRoleAttachmentDetailsList;
  }

  /**
   * Modifies a concern role attachment record
   *
   * @param details
   * - concern role attachment details
   */

  @Override
  public void
    modifyConcernRoleAttachment(final BDMConcernRoleAttachmentDetails details)
      throws AppException, InformationalException {

    final ConcernRoleAttachment concernRoleAttachmentObj =
      ConcernRoleAttachmentFactory.newInstance();

    // CAll OOTB SL to modify ConcernRoleAttachment
    concernRoleAttachmentObj.modifyConcernRoleAttachment(details.dtls);

    // modify Custom ConcernRoleAttachment entity
    final BDMConcernRoleAttachmentLink bdmlinkObj =
      BDMConcernRoleAttachmentLinkFactory.newInstance();
    final BDMConcernRoleAttachmentLinkKey bdmKEy =
      new BDMConcernRoleAttachmentLinkKey();
    bdmKEy.attachmentLinkID = details.dtls.linkDtls.attachmentLinkID;

    final BDMConcernRoleAttachmentLinkDtls bdmDtls =
      new BDMConcernRoleAttachmentLinkDtls();
    bdmDtls.fileSource = details.fileSource;
    bdmDtls.attachmentLinkID = details.dtls.linkDtls.attachmentLinkID;

    bdmlinkObj.modify(bdmKEy, bdmDtls);

  }

  /**
   * Enact task workflow
   *
   * @param documentType
   * @param concernRoleID
   * @throws AppException
   * @throws InformationalException
   */
  private void enactTaskWorkFlow(String documentType,
    final Long concernRoleID) throws AppException, InformationalException {

    final List<Object> enactmentStructs = new ArrayList<>();
    final BDMVSGTaskCreateDetails bdmVSGTaskCreateDetails =
      new BDMVSGTaskCreateDetails();

    bdmVSGTaskCreateDetails.participantRoleID = concernRoleID.longValue();
    bdmVSGTaskCreateDetails.participantType = ASSIGNEETYPE.PERSON;
    bdmVSGTaskCreateDetails.priority = TASKPRIORITY.NORMAL;

    // Task 22568 - Find taskClassificationID for skillType
    final BDMDocumentTypeKey bdmDocumentTypeKey = new BDMDocumentTypeKey();
    bdmDocumentTypeKey.documentType = documentType;
    BDMTaskSkillTypeKey bdmTaskSkillTypeKey = new BDMTaskSkillTypeKey();
    BDMTaskSkillTypeList bdmTaskSkillTypeList = new BDMTaskSkillTypeList();
    final BDMWorkAllocationTask bdmWorkAllocationTaskObj =
      BDMWorkAllocationTaskFactory.newInstance();

    // Get skillType by documentType
    bdmTaskSkillTypeKey =
      bdmWorkAllocationTaskObj.getSkillTypeByDocumentType(bdmDocumentTypeKey);

    // Get workQueueID by skillType
    BDMWorkQueueID bdmWorkQueueID = new BDMWorkQueueID();

    bdmWorkQueueID =
      bdmWorkAllocationTaskObj.getWorkQueueIDBySkillType(bdmTaskSkillTypeKey);

    bdmVSGTaskCreateDetails.workQueueID = bdmWorkQueueID.workQueueID;

    // Get bdmTaskClassificationID by skillType
    bdmTaskSkillTypeList =
      bdmWorkAllocationTaskObj.searchBDMTaskSkillType(bdmTaskSkillTypeKey);

    if (bdmTaskSkillTypeList.dtls.size() > 0) {
      bdmVSGTaskCreateDetails.bdmTaskClassificationID =
        bdmTaskSkillTypeList.dtls.get(0).bdmTaskClassificationID;
    }

    final String skillTypeDesc = CodeTable.getOneItem(SKILLTYPE.TABLENAME,
      bdmTaskSkillTypeKey.skillType, TransactionInfo.getProgramLocale());
    final StringBuffer taskSubject = new StringBuffer();
    taskSubject.append(skillTypeDesc);
    // Task - 56792 for Bug 56319 Get client oral language
    String orallanuage = CuramConst.gkEmpty;
    final PDCPerson pdcPerson = PDCPersonFactory.newInstance();
    final PersonAndEvidenceTypeList personAndEvidenceTypeList =
      new PersonAndEvidenceTypeList();
    personAndEvidenceTypeList.concernRoleID =
      bdmVSGTaskCreateDetails.participantRoleID;
    personAndEvidenceTypeList.evidenceTypeList = "PDC0000263";
    PDCEvidenceDetailsList evidenceDetailsList = null;
    try{
      evidenceDetailsList = pdcPerson
              .listCurrentParticipantEvidenceByTypes(personAndEvidenceTypeList);
    }catch (Exception e){
      // record not found
    }

    if (evidenceDetailsList != null && evidenceDetailsList.list.size() > 0) {
      final String oralLanugageCode = BDMEvidenceUtil.getDynEvdAttrValue(
        evidenceDetailsList.list.item(0).evidenceID, "PDC0000263",
        "preferredOralLanguage");
      if (BDMLANGUAGE.ENGLISHL.equals(oralLanugageCode)) {
        orallanuage = "EN";
      } else if (BDMLANGUAGE.FRENCHL.equals(oralLanugageCode)) {
        orallanuage = "FR";
      }
    } else {
      orallanuage = "EN";
    }

    taskSubject.append(" " + orallanuage + " Attachment '");

    documentType = CodeTable.getOneItem(DOCUMENTTYPE.TABLENAME, documentType,
      TransactionInfo.getProgramLocale());
    taskSubject.append(documentType);
    final ConcernRole concernRoleobj = ConcernRoleFactory.newInstance();
    final ConcernRoleKey concernRolekey = new ConcernRoleKey();
    concernRolekey.concernRoleID = concernRoleID.longValue();
    final ConcernRoleDtls concernRoleDtls =
      concernRoleobj.read(concernRolekey);
    taskSubject.append("' for ").append(concernRoleDtls.concernRoleName)
      .append(" ").append(concernRoleDtls.primaryAlternateID);

    bdmVSGTaskCreateDetails.subject = taskSubject.toString();

    enactmentStructs.add(bdmVSGTaskCreateDetails);

    // Task deadline
    final long currentDateTimeInMills = Date.getCurrentDate().asLong();
    final int taskDeadline = Configuration
      .getIntProperty(EnvVars.BDM_ENV_PERSON_DOC_UPLOAD_TASK_DEADLINE);
    // final int taskDeadline = 21;
    final long inputDateTimeInMills =
      Date.getCurrentDate().addDays(taskDeadline).asLong();
    final long durationMills = inputDateTimeInMills - currentDateTimeInMills;
    final long durationSeconds = durationMills / DateTime.kMilliSecsInSecond;
    final BDMUACaseWorkerVerificationEnactKey bdmuaCaseWorkerVerificationEnactKey =
      new BDMUACaseWorkerVerificationEnactKey();
    bdmuaCaseWorkerVerificationEnactKey.deadLineDurationInSec =
      (int) durationSeconds;
    enactmentStructs.add(bdmuaCaseWorkerVerificationEnactKey);

    EnactmentService.startProcess("BDMPersonDocumentUploadTask",
      enactmentStructs);
  }

  /**
   * Enact task workflow - BDMPersonRFRDocumentUploadTask
   * Task - 58974
   *
   * @param documentType
   * @param concernRoleID
   * @throws AppException
   * @throws InformationalException
   */
  private void enactRFRTaskWorkFlow(final String documentType,
    final Long concernRoleID) throws AppException, InformationalException {

    final List<Object> enactmentStructs = new ArrayList<>();
    final BDMVSGTaskCreateDetails bdmVSGTaskCreateDetails =
      new BDMVSGTaskCreateDetails();

    bdmVSGTaskCreateDetails.participantRoleID = concernRoleID.longValue();
    bdmVSGTaskCreateDetails.participantType = ASSIGNEETYPE.PERSON;
    bdmVSGTaskCreateDetails.priority = TASKPRIORITY.NORMAL;

    // Task 22568 - Find taskClassificationID for skillType
    final BDMDocumentTypeKey bdmDocumentTypeKey = new BDMDocumentTypeKey();
    bdmDocumentTypeKey.documentType = documentType;
    BDMTaskSkillTypeKey bdmTaskSkillTypeKey = new BDMTaskSkillTypeKey();
    BDMTaskSkillTypeList bdmTaskSkillTypeList = new BDMTaskSkillTypeList();
    final BDMWorkAllocationTask bdmWorkAllocationTaskObj =
      BDMWorkAllocationTaskFactory.newInstance();

    // Get skillType by documentType
    bdmTaskSkillTypeKey =
      bdmWorkAllocationTaskObj.getSkillTypeByDocumentType(bdmDocumentTypeKey);

    // Get workQueueID by skillType
    BDMWorkQueueID bdmWorkQueueID = new BDMWorkQueueID();

    bdmWorkQueueID =
      bdmWorkAllocationTaskObj.getWorkQueueIDBySkillType(bdmTaskSkillTypeKey);

    bdmVSGTaskCreateDetails.workQueueID = bdmWorkQueueID.workQueueID;

    // Get bdmTaskClassificationID by skillType
    bdmTaskSkillTypeList =
      bdmWorkAllocationTaskObj.searchBDMTaskSkillType(bdmTaskSkillTypeKey);

    if (bdmTaskSkillTypeList.dtls.size() > 0) {
      bdmVSGTaskCreateDetails.bdmTaskClassificationID =
        bdmTaskSkillTypeList.dtls.get(0).bdmTaskClassificationID;
    }

    final String skillTypeDesc = CodeTable.getOneItem(SKILLTYPE.TABLENAME,
      bdmTaskSkillTypeKey.skillType, TransactionInfo.getProgramLocale());
    final StringBuffer taskSubject = new StringBuffer();
    taskSubject.append(skillTypeDesc);
    // Task - 56792 for Bug 56319 Get client oral language
    String orallanuage = CuramConst.gkEmpty;
    final PDCPerson pdcPerson = PDCPersonFactory.newInstance();
    final PersonAndEvidenceTypeList personAndEvidenceTypeList =
      new PersonAndEvidenceTypeList();
    personAndEvidenceTypeList.concernRoleID =
      bdmVSGTaskCreateDetails.participantRoleID;
    personAndEvidenceTypeList.evidenceTypeList = "PDC0000263";
    final PDCEvidenceDetailsList evidenceDetailsList = pdcPerson
      .listCurrentParticipantEvidenceByTypes(personAndEvidenceTypeList);
    if (evidenceDetailsList.list.size() > 0) {
      final String oralLanugageCode = BDMEvidenceUtil.getDynEvdAttrValue(
        evidenceDetailsList.list.item(0).evidenceID, "PDC0000263",
        "preferredOralLanguage");
      if (BDMLANGUAGE.ENGLISHL.equals(oralLanugageCode)) {
        orallanuage = "EN";
      } else if (BDMLANGUAGE.FRENCHL.equals(oralLanugageCode)) {
        orallanuage = "FR";
      }
    } else {
      orallanuage = "EN";
    }

    taskSubject
      .append(" " + orallanuage + " Request for Reconsideration Hard Copy");

    final ConcernRole concernRoleobj = ConcernRoleFactory.newInstance();
    final ConcernRoleKey concernRolekey = new ConcernRoleKey();
    concernRolekey.concernRoleID = concernRoleID.longValue();
    final ConcernRoleDtls concernRoleDtls =
      concernRoleobj.read(concernRolekey);
    taskSubject.append(" for ").append(concernRoleDtls.concernRoleName)
      .append(" ").append(concernRoleDtls.primaryAlternateID);

    bdmVSGTaskCreateDetails.subject = taskSubject.toString();

    enactmentStructs.add(bdmVSGTaskCreateDetails);

    // Task deadline
    final long currentDateTimeInMills = Date.getCurrentDate().asLong();
    /*
     * final int taskDeadline = Configuration
     * .getIntProperty(EnvVars.BDM_ENV_PERSON_DOC_UPLOAD_TASK_DEADLINE);
     */
    final int taskDeadline = 2;
    final long inputDateTimeInMills =
      Date.getCurrentDate().addDays(taskDeadline).asLong();
    final long durationMills = inputDateTimeInMills - currentDateTimeInMills;
    final long durationSeconds = durationMills / DateTime.kMilliSecsInSecond;
    final BDMUACaseWorkerVerificationEnactKey bdmuaCaseWorkerVerificationEnactKey =
      new BDMUACaseWorkerVerificationEnactKey();
    bdmuaCaseWorkerVerificationEnactKey.deadLineDurationInSec =
      (int) durationSeconds;
    enactmentStructs.add(bdmuaCaseWorkerVerificationEnactKey);

    EnactmentService.startProcess("BDMPersonRFRDocumentUploadTask",
      enactmentStructs);
  }

}
