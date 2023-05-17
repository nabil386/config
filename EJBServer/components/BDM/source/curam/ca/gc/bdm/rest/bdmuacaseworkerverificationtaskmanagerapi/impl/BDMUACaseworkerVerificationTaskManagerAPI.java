package curam.ca.gc.bdm.rest.bdmuacaseworkerverificationtaskmanagerapi.impl;

import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskSkillTypeKey;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskSkillTypeList;
import curam.ca.gc.bdm.rest.bdmuacaseworkerverificationtaskmanagerapi.struct.BDMUACaseWorkerVerificationEnactKey;
import curam.ca.gc.bdm.sl.bdmworkallocation.fact.BDMWorkAllocationTaskFactory;
import curam.ca.gc.bdm.sl.bdmworkallocation.intf.BDMWorkAllocationTask;
import curam.ca.gc.bdm.sl.bdmworkallocation.struct.BDMEvidenceTypeKey;
import curam.citizenaccount.facade.struct.CaseParticipantEvdUploadKey;
import curam.citizenworkspace.rest.facade.impl.CitizenUploadForVerifcationUtils;
import curam.citizenworkspace.rest.facade.struct.SPMVerification;
import curam.citizenworkspace.rest.facade.struct.SPMVerificationList;
import curam.codetable.ASSIGNEETYPE;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASETYPECODE;
import curam.codetable.SKILLTYPE;
import curam.codetable.TASKPRIORITY;
import curam.codetable.VERIFICATIONTYPE;
import curam.core.facade.infrastructure.struct.PersonAndEvidenceTypeList;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.intf.CaseHeader;
import curam.core.intf.ConcernRole;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.struct.TaskCreateDetails;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.pdc.facade.fact.PDCPersonFactory;
import curam.pdc.facade.intf.PDCPerson;
import curam.pdc.facade.struct.PDCEvidenceDetailsList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.LocalisableString;
import curam.util.resources.Configuration;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.workflow.impl.EnactmentService;
import curam.verification.sl.entity.fact.VerifiableDataItemFactory;
import curam.verification.sl.entity.intf.VerifiableDataItem;
import curam.verification.sl.entity.struct.VerifiableDataItemDtls;
import curam.verification.sl.entity.struct.VerifiableDataItemKey;
import curam.verification.sl.infrastructure.entity.fact.VDIEDLinkFactory;
import curam.verification.sl.infrastructure.entity.fact.VerificationFactory;
import curam.verification.sl.infrastructure.entity.intf.VDIEDLink;
import curam.verification.sl.infrastructure.entity.intf.Verification;
import curam.verification.sl.infrastructure.entity.struct.VDIEDLinkDtls;
import curam.verification.sl.infrastructure.entity.struct.VDIEDLinkKey;
import curam.verification.sl.infrastructure.entity.struct.VerificationDtls;
import curam.verification.sl.infrastructure.entity.struct.VerificationKey;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * REST API to create task to work queue when client uploads verification item
 *
 */
public class BDMUACaseworkerVerificationTaskManagerAPI extends
  curam.ca.gc.bdm.rest.bdmuacaseworkerverificationtaskmanagerapi.base.BDMUACaseworkerVerificationTaskManagerAPI {

  private List<Object> enactmentStructs;

  public List<Object> getEnactmentStructs() {

    return this.enactmentStructs;
  }

  public void setEnactmentStructs(final List<Object> enactmentStructs) {

    this.enactmentStructs = enactmentStructs;
  }

  /**
   * This method is called by REST resource path URL
   * /ua/verifications/create_caseworker_task
   * to create client verification item upload notification
   *
   * @param CaseParticipantEvdUploadKey key
   * @throws AppException, InformationalException
   *
   */
  @Override
  public void
    createCaseworkerTaskOnUpload(final CaseParticipantEvdUploadKey key)
      throws AppException, InformationalException {

    boolean isCaseEvidence = key.isCaseEvidence;
    final Long participantID = Long.valueOf(key.participantID);
    final Long verificationID = Long.valueOf(key.verificationID);
    final Verification verificationObj = VerificationFactory.newInstance();
    final VerificationKey verificationKey = new VerificationKey();
    verificationKey.verificationID = verificationID;
    final VerificationDtls verificationDtls =
      verificationObj.read(verificationKey);
    // this logic is written as incoming isCaseEvidence is always false
    if (!VERIFICATIONTYPE.NONCASEDATA
      .equals(verificationDtls.verificationLinkedType)) {
      key.isCaseEvidence = true;
      isCaseEvidence = true;
    }

    final Long caseID =
      provideValidCaseID(Long.valueOf(key.caseID), verificationID);

    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    caseHeaderKey.caseID = caseID.longValue();
    final CaseHeaderDtls caseHeaderDtls = caseHeaderObj.read(caseHeaderKey);

    final SPMVerificationList spmVerificationList =
      CitizenUploadForVerifcationUtils.getSPMVerificationList(caseID,
        participantID, isCaseEvidence);
    final boolean taskNotificationOnAllEvd =
      isTaskNotificationsDisplayedOnlyOnAllEvd();
    SPMVerification currentVerification = null;
    boolean areAllDocsProvided = false;
    final int currentEvdRecordNum =
      getEvidenceRecordStatusForCaseOrParticpant(caseID, participantID,
        spmVerificationList, isCaseEvidence);
    final int totalNumEvidenceRecords = spmVerificationList.data.size();
    for (final SPMVerification verification : spmVerificationList.data) {
      if (verification.verificationId == verificationID.longValue())
        currentVerification = verification;
    }

    if (currentVerification != null)
      areAllDocsProvided =
        CitizenUploadForVerifcationUtils.areAllFilesProvidedForVerification(
          currentVerification, spmVerificationList);
    if (taskNotificationOnAllEvd) {
      if (currentEvdRecordNum == totalNumEvidenceRecords
        && areAllDocsProvided)
        enactWorkFlow(caseID, participantID, totalNumEvidenceRecords,
          totalNumEvidenceRecords, isCaseEvidence, key.verificationID,
          caseHeaderDtls.caseReference, caseHeaderDtls.caseTypeCode,
          caseHeaderDtls.registrationDate);
    } else if (currentEvdRecordNum <= totalNumEvidenceRecords
      && areAllDocsProvided) {
      enactWorkFlow(caseID, participantID, totalNumEvidenceRecords,
        currentEvdRecordNum, isCaseEvidence, key.verificationID,
        caseHeaderDtls.caseReference, caseHeaderDtls.caseTypeCode,
        caseHeaderDtls.registrationDate);
    }

  }

  /**
   * This method returns caseID using verification ID
   *
   * @param caseID
   * @param verificationID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private Long provideValidCaseID(final Long caseID,
    final Long verificationID) throws AppException, InformationalException {

    if (caseID.longValue() == 0L) {
      final VerificationKey verificationKey = new VerificationKey();
      verificationKey.verificationID = verificationID.longValue();
      final EvidenceDescriptorDtls evdDescriptorDtls =
        CitizenUploadForVerifcationUtils
          .readEvidenceDescriptor(verificationKey);
      return Long.valueOf(evdDescriptorDtls.caseID);
    }
    return caseID;
  }

  /**
   * Get evidence record status
   *
   * @param caseID
   * @param participantID
   * @param spmVerificationList
   * @param isCaseEvd
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public int getEvidenceRecordStatusForCaseOrParticpant(final Long caseID,
    final Long participantID, final SPMVerificationList spmVerificationList,
    final boolean isCaseEvd) throws AppException, InformationalException {

    int numEvidencdesSatisfied = 0;
    final boolean multipleProofsRequired = false;
    if (spmVerificationList != null)
      for (final SPMVerification verification : spmVerificationList.data) {
        if (CitizenUploadForVerifcationUtils
          .areAllFilesProvidedForVerification(verification,
            spmVerificationList))
          numEvidencdesSatisfied++;
      }
    return numEvidencdesSatisfied;
  }

  /**
   * This method enacts the workflow to create manual task
   *
   * @param caseID
   * @param participantID
   * @param totalNumEvidenceRecords
   * @param currentEvdNum
   * @param isCaseEvd
   * @param verificationID
   * @throws AppException
   * @throws InformationalException
   */

  private void enactWorkFlow(final Long caseID, final Long participantID,
    final int totalNumEvidenceRecords, final int currentEvdNum,
    final boolean isCaseEvd, final long verificationID,
    final String caseReference, final String caseTypeCode,
    final Date registrationDate) throws AppException, InformationalException {

    final List<Object> enactmentStructs =
      populateWorkflowEnactmentStruct(caseID, participantID,
        totalNumEvidenceRecords, currentEvdNum, isCaseEvd, verificationID,
        caseReference, caseTypeCode, registrationDate);
    setEnactmentStructs(enactmentStructs);
    EnactmentService.startProcess(
      "BDMCaseworkerVerificationTaskFromCitizenUpload", enactmentStructs);
  }

  /**
   * This method populated worklow enactment struct
   *
   * @param caseID
   * @param participantID
   * @param numEvidenceRecordsOnCase
   * @param currentEvdNum
   * @param isCaseEvd
   * @param verificationID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private List<Object> populateWorkflowEnactmentStruct(final Long caseID,
    final Long participantID, final int numEvidenceRecordsOnCase,
    final int currentEvdNum, final boolean isCaseEvd,
    final long verificationID, final String caseReference,
    final String caseTypeCode, final Date registrationDate)
    throws AppException, InformationalException {

    final List<Object> enactmentStructs = new ArrayList<>();
    final TaskCreateDetails taskCreateDetails = new TaskCreateDetails();
    final CaseParticipantEvdUploadKey caseEvdNotificationStatus =
      new CaseParticipantEvdUploadKey();
    caseEvdNotificationStatus.isCaseEvidence = isCaseEvd;
    caseEvdNotificationStatus.verificationID = verificationID;
    enactmentStructs.add(caseEvdNotificationStatus);
    final BDMUACaseWorkerVerificationEnactKey bdmuaCaseWorkerVerificationEnactKey =
      new BDMUACaseWorkerVerificationEnactKey();
    final long currentDateTimeInMills = Date.getCurrentDate().asLong();
    final int taskDeadline = Configuration
      .getIntProperty(EnvVars.BDM_ENV_DOC_UPLOAD_VERIFICATION_TASK_DEADLINE);
    final long inputDateTimeInMills =
      Date.getCurrentDate().addDays(taskDeadline).asLong();
    final long durationMills = inputDateTimeInMills - currentDateTimeInMills;
    final long durationSeconds = durationMills / DateTime.kMilliSecsInSecond;
    bdmuaCaseWorkerVerificationEnactKey.deadLineDurationInSec =
      (int) durationSeconds;
    final String skillType = getSkillTypeByVerificationID(verificationID);
    final BDMWorkAllocationTask bdmWorkAllocationTaskObj =
      BDMWorkAllocationTaskFactory.newInstance();
    final BDMTaskSkillTypeKey bdmTaskSkillTypeKey = new BDMTaskSkillTypeKey();
    bdmTaskSkillTypeKey.skillType = skillType;
    bdmuaCaseWorkerVerificationEnactKey.workQueueID = bdmWorkAllocationTaskObj
      .getWorkQueueIDBySkillType(bdmTaskSkillTypeKey).workQueueID;

    // Get bdmTaskClassificationID by skillType
    BDMTaskSkillTypeList bdmTaskSkillTypeList = new BDMTaskSkillTypeList();
    bdmTaskSkillTypeList =
      bdmWorkAllocationTaskObj.searchBDMTaskSkillType(bdmTaskSkillTypeKey);
    if (bdmTaskSkillTypeList.dtls.size() > 0) {
      bdmuaCaseWorkerVerificationEnactKey.bdmTaskClassificationID =
        bdmTaskSkillTypeList.dtls.get(0).bdmTaskClassificationID;
    }

    enactmentStructs.add(bdmuaCaseWorkerVerificationEnactKey);
    taskCreateDetails.caseID = caseID.longValue();
    taskCreateDetails.participantRoleID = participantID.longValue();
    taskCreateDetails.participantType = ASSIGNEETYPE.PERSON;
    taskCreateDetails.priority = TASKPRIORITY.NORMAL;
    final String taskSubject = createNotificationMessage(caseID,
      participantID, numEvidenceRecordsOnCase, currentEvdNum, isCaseEvd,
      verificationID, caseReference, caseTypeCode, registrationDate);
    taskCreateDetails.subject = taskSubject;
    enactmentStructs.add(taskCreateDetails);
    return enactmentStructs;
  }

  /**
   * This method returns task subject text.
   *
   * @param caseID
   * @param participantID
   * @param totalNumEvidences
   * @param currentEvdNum
   * @param isCaseTypeMsg
   * @param verificationID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private String createNotificationMessage(final Long caseID,
    final Long participantID, final int totalNumEvidences,
    final int currentEvdNum, final boolean isCaseTypeMsg,
    final long verificationID, final String caseReference,
    final String caseTypeCode, final Date registrationDate)
    throws AppException, InformationalException {

    // Get verification Item name using verification ID
    final Verification verificationObj = VerificationFactory.newInstance();
    final VerificationKey verificationKey = new VerificationKey();
    verificationKey.verificationID = verificationID;
    final VerificationDtls verificationDtls =
      verificationObj.read(verificationKey);

    final VDIEDLink vdiedLinkObj = VDIEDLinkFactory.newInstance();
    final VDIEDLinkKey vdiedLinkKey = new VDIEDLinkKey();
    vdiedLinkKey.VDIEDLinkID = verificationDtls.VDIEDLinkID;
    final VDIEDLinkDtls vdiedLinkDtls = vdiedLinkObj.read(vdiedLinkKey);

    final VerifiableDataItem verifiableDataItemObj =
      VerifiableDataItemFactory.newInstance();
    final VerifiableDataItemKey verifiableDataItemKey =
      new VerifiableDataItemKey();
    verifiableDataItemKey.verifiableDataItemID =
      vdiedLinkDtls.verifiableDataItemID;
    final VerifiableDataItemDtls verifiableDataItemDtls =
      verifiableDataItemObj.read(verifiableDataItemKey);

    final String verificationItemName = CodeTable.getOneItem(
      CASEEVIDENCE.TABLENAME, verifiableDataItemDtls.evidenceType);

    // TODO: Need to create separate message for Person evidence types
    final LocalisableString notificationMsg = new LocalisableString(
      curam.ca.gc.bdm.message.BDMCASEWORKERVERIFICATIONTASKFROMCITIZENUPLOAD.INF_CASEWORKER_TASK_NOTIFICATION_UPLOAD_FOR_CASE_EVD);

    final String skillTypeCode = getSkillTypeByVerificationID(verificationID);
    final String skillType =
      CodeTable.getOneItem(SKILLTYPE.TABLENAME, skillTypeCode);
    // first argument Skill Type
    notificationMsg.arg(skillType);
    // Task - 56792 for Bug 56319 Get client oral language
    String orallanuage = CuramConst.gkEmpty;
    final PDCPerson pdcPerson = PDCPersonFactory.newInstance();
    final PersonAndEvidenceTypeList personAndEvidenceTypeList =
      new PersonAndEvidenceTypeList();
    personAndEvidenceTypeList.concernRoleID = participantID;
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
    notificationMsg.arg(orallanuage);

    notificationMsg.arg(verificationItemName);
    final ConcernRole concernRoleobj = ConcernRoleFactory.newInstance();
    final ConcernRoleKey concernRolekey = new ConcernRoleKey();
    concernRolekey.concernRoleID = participantID.longValue();
    final ConcernRoleDtls concernRoleDtls =
      concernRoleobj.read(concernRolekey);
    notificationMsg.arg(concernRoleDtls.concernRoleName);
    notificationMsg.arg(concernRoleDtls.primaryAlternateID);
    notificationMsg
      .arg(CodeTable.getOneItem(CASETYPECODE.TABLENAME, caseTypeCode));
    notificationMsg.arg(caseReference);

    final String taskSubject =
      notificationMsg.getMessage(TransactionInfo.getProgramLocale());

    return taskSubject.toString();
  }

  private String getSkillTypeByVerificationID(final long verificationID)
    throws AppException, InformationalException {

    // Get verification Item name using verification ID
    final Verification verificationObj = VerificationFactory.newInstance();
    final VerificationKey verificationKey = new VerificationKey();
    verificationKey.verificationID = verificationID;
    final VerificationDtls verificationDtls =
      verificationObj.read(verificationKey);

    final VDIEDLink vdiedLinkObj = VDIEDLinkFactory.newInstance();
    final VDIEDLinkKey vdiedLinkKey = new VDIEDLinkKey();
    vdiedLinkKey.VDIEDLinkID = verificationDtls.VDIEDLinkID;
    final VDIEDLinkDtls vdiedLinkDtls = vdiedLinkObj.read(vdiedLinkKey);

    final VerifiableDataItem verifiableDataItemObj =
      VerifiableDataItemFactory.newInstance();
    final VerifiableDataItemKey verifiableDataItemKey =
      new VerifiableDataItemKey();
    verifiableDataItemKey.verifiableDataItemID =
      vdiedLinkDtls.verifiableDataItemID;
    final VerifiableDataItemDtls verifiableDataItemDtls =
      verifiableDataItemObj.read(verifiableDataItemKey);

    final BDMWorkAllocationTask bdmWorkAllocationTaskObj =
      BDMWorkAllocationTaskFactory.newInstance();
    final BDMEvidenceTypeKey bdmEvidenceTypeKey = new BDMEvidenceTypeKey();
    bdmEvidenceTypeKey.evidenceType = verifiableDataItemDtls.evidenceType;
    final BDMTaskSkillTypeKey bdmTaskSkillTypeKey =
      bdmWorkAllocationTaskObj.getSkillTypeByEvidenceType(bdmEvidenceTypeKey);

    return bdmTaskSkillTypeKey.skillType;
  }

  /**
   * Check if task shoule be created only when all evidence verifications are
   * uploaded
   *
   * @return boolean
   */
  private boolean isTaskNotificationsDisplayedOnlyOnAllEvd() {

    boolean displayTaskNotificationsOnAllEvdUplods = false;
    try {
      displayTaskNotificationsOnAllEvdUplods =
        Configuration.getBooleanProperty(
          EnvVars.ENV_CW_DISPLAY_TASK_NOTIFICATIONS_ALL_EVIDENCE_UPLOADS);
    } catch (final NullPointerException nullPointerException) {
    }
    return displayTaskNotificationsOnAllEvdUplods;
  }

}
