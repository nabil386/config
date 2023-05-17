package curam.ca.gc.bdm.sl.attachment.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.entity.fact.BDMTaskFactory;
import curam.ca.gc.bdm.entity.fact.BDMWorkQueueCountryLinkFactory;
import curam.ca.gc.bdm.entity.fec.fact.BDMFECaseFactory;
import curam.ca.gc.bdm.entity.fec.intf.BDMFECase;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseDtls;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseKey;
import curam.ca.gc.bdm.entity.intf.BDMTask;
import curam.ca.gc.bdm.entity.intf.BDMWorkQueueCountryLink;
import curam.ca.gc.bdm.entity.struct.BDMTaskDtls;
import curam.ca.gc.bdm.entity.struct.BDMTaskKey;
import curam.ca.gc.bdm.entity.struct.BDMWorkQueueCountryLinkDtls;
import curam.ca.gc.bdm.entity.struct.BDMWorkQueueCountryLinkKey;
import curam.ca.gc.bdm.facade.communication.struct.BDMConcernRoleAttachmentDetails;
import curam.ca.gc.bdm.facade.struct.BDMCreateCaseAttachmentDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.rest.bdmuacaseworkerverificationtaskmanagerapi.struct.BDMUACaseWorkerVerificationEnactKey;
import curam.ca.gc.bdm.sl.attachment.struct.BDMAttachmentTaskCreateDetails;
import curam.codetable.BDMTASKCATEGORY;
import curam.codetable.BDMTASKTYPE;
import curam.codetable.CASECATEGORY;
import curam.codetable.CASESTATUS;
import curam.codetable.DOCUMENTTYPE;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.codetable.PRODUCTCATEGORY;
import curam.codetable.TASKPRIORITY;
import curam.core.facade.struct.ConcernRoleIDStatusCodeKey;
import curam.core.fact.CaseHeaderFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.struct.CaseHeaderDtlsList;
import curam.core.struct.AttachmentIDAndAttachmentLinkIDStruct;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataAttributeDtls;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataAttributeDtlsList;
import curam.pdc.impl.PDCConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.internal.workflow.fact.ActivityInstanceFactory;
import curam.util.internal.workflow.intf.ActivityInstance;
import curam.util.internal.workflow.struct.ActivityInstanceDtlsList;
import curam.util.internal.workflow.struct.ProcessInstanceKey;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.NotFoundIndicator;
import curam.util.workflow.impl.EnactmentService;
import curam.wizard.util.impl.CodetableUtil;
import java.util.ArrayList;
import java.util.List;

public class BDMMaintainAttachmentTask
  extends curam.ca.gc.bdm.sl.attachment.base.BDMMaintainAttachmentTask {

  private final BDMUtil bdmUtil = new BDMUtil();

  public BDMMaintainAttachmentTask() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Method to process tasks for attachment for person
   *
   */
  @Override
  public void createPersonAttachmentTask(
    final BDMConcernRoleAttachmentDetails bdmConcernRoleAttachmentDetails)
    throws AppException, InformationalException {

    if (bdmUtil.isAttachmentTaskVIRequired(
      bdmConcernRoleAttachmentDetails.dtls.attachmentDtls.documentType)
      // BEGIN: BUG 98038: Task Issue fix
      || bdmUtil.isAttachmentTaskIIRequired(
        bdmConcernRoleAttachmentDetails.dtls.attachmentDtls.documentType)) {
      // END: BUG 98038: Task Issue fix
      final List<Object> enactmentStructs = new ArrayList<>();

      // START, BUG 98671, Fox for Close Task 06
      final BDMAttachmentTaskCreateDetails bdmAttachmentTaskCreateDetails =
        new BDMAttachmentTaskCreateDetails();
      bdmAttachmentTaskCreateDetails.concernRoleID =
        bdmConcernRoleAttachmentDetails.dtls.linkDtls.concernRoleID;
      bdmAttachmentTaskCreateDetails.participantRoleID =
        bdmConcernRoleAttachmentDetails.dtls.linkDtls.concernRoleID;
      bdmAttachmentTaskCreateDetails.documentType =
        bdmConcernRoleAttachmentDetails.dtls.attachmentDtls.documentType;
      bdmAttachmentTaskCreateDetails.priority = TASKPRIORITY.NORMAL;
      bdmAttachmentTaskCreateDetails.workQueueID = getWorkQueueIDForPerson(
        bdmConcernRoleAttachmentDetails.dtls.linkDtls.concernRoleID);
      bdmAttachmentTaskCreateDetails.attachmentID =
        bdmConcernRoleAttachmentDetails.dtls.attachmentDtls.attachmentID;

      // START Bug 110431: When language switched to French via preference from
      // the user profile the Task subject line is still English
      bdmAttachmentTaskCreateDetails.attachmentReceipt =
        bdmConcernRoleAttachmentDetails.dtls.attachmentDtls.receiptDate;

      bdmAttachmentTaskCreateDetails.oralWrittenLanguage =
        getOralWrittenLanguageForTask(
          bdmConcernRoleAttachmentDetails.dtls.linkDtls.concernRoleID)
            .toString();

      bdmAttachmentTaskCreateDetails.documentTypeCdDesc =
        CodetableUtil.getCodetableDescription(DOCUMENTTYPE.TABLENAME,
          bdmConcernRoleAttachmentDetails.dtls.attachmentDtls.documentType);

      bdmAttachmentTaskCreateDetails.strConcernRoleFullName =
        bdmUtil.getConcernRoleName(
          bdmConcernRoleAttachmentDetails.dtls.linkDtls.concernRoleID);

      bdmAttachmentTaskCreateDetails.strIdentificationRefernce =
        bdmUtil.getPersonIdentificationDetail(
          bdmConcernRoleAttachmentDetails.dtls.linkDtls.concernRoleID);
      // END Bug 110431

      enactmentStructs.add(bdmAttachmentTaskCreateDetails);
      // END, BUG 98671, Fox for Close Task 06

      // Task deadline
      // START: Bug 99492: Task #2: FEC Attachment BESS Deadline on Task is 1
      // year instead of 30 days
      final long currentDateTimeInMills = Date.getCurrentDate().asLong();

      long inputDateTimeInMills = CuramConst.kLongZero;

      long durationMills = CuramConst.kLongZero;

      boolean recieptDtIsNotZeroInd = false;

      final int taskDeadlineDays = getDeadlineNoOfDays(
        bdmConcernRoleAttachmentDetails.dtls.attachmentDtls.documentType);

      if (!bdmConcernRoleAttachmentDetails.dtls.attachmentDtls.receiptDate
        .isZero()) {

        inputDateTimeInMills =
          bdmConcernRoleAttachmentDetails.dtls.attachmentDtls.receiptDate
            .addDays(taskDeadlineDays).asLong();

        recieptDtIsNotZeroInd = true;

      } else {
        inputDateTimeInMills =
          Date.getCurrentDate().addDays(taskDeadlineDays).asLong();
      }
      if (recieptDtIsNotZeroInd)
        durationMills = inputDateTimeInMills - currentDateTimeInMills;
      else
        durationMills = inputDateTimeInMills - currentDateTimeInMills;
      // END: Bug 99492: Task #2: FEC Attachment BESS Deadline on Task is 1
      // year instead of 30 days

      final long durationSeconds =
        durationMills / DateTime.kMilliSecsInSecond;

      final BDMUACaseWorkerVerificationEnactKey bdmuaCaseWorkerVerificationEnactKey =
        new BDMUACaseWorkerVerificationEnactKey();
      bdmuaCaseWorkerVerificationEnactKey.deadLineDurationInSec =
        (int) durationSeconds;
      enactmentStructs.add(bdmuaCaseWorkerVerificationEnactKey);

      final long kProcessInstanceID =
        EnactmentService.startProcessInV3CompatibilityMode(
          "BDMPersonBEESFBDocAttachmentUploadTask", enactmentStructs);

      addBDMTask(kProcessInstanceID,
        bdmConcernRoleAttachmentDetails.dtls.attachmentDtls.documentType);
    }
  }

  private void addBDMTask(final long kProcessInstanceID,
    final String documentType) throws AppException, InformationalException {

    if (kProcessInstanceID != 0) {

      final long taskID = getTaskIDForProcessInstanceID(kProcessInstanceID);

      if (kProcessInstanceID != 0) {
        final BDMTask bdmTask = BDMTaskFactory.newInstance();
        final BDMTaskKey key = new BDMTaskKey();
        key.taskID = taskID;
        final BDMTaskDtls newDtls = new BDMTaskDtls();
        newDtls.taskID = taskID;

        if (documentType.equals(DOCUMENTTYPE.BESS)
          || documentType.equals(DOCUMENTTYPE.BESS_ADDITIONAL_DOCUMENTS)) {
          newDtls.category = BDMTASKCATEGORY.BDMIOAPPLICATIONFB;
          newDtls.type = BDMTASKTYPE.BDMFBBESS;
        } else if (documentType
          .equals(DOCUMENTTYPE.FB_INCOMING_LIAISON_MEDICAL)) {
          newDtls.category = BDMTASKCATEGORY.BDMIOAPPLICATIONFB;
          newDtls.type = BDMTASKTYPE.BDMFBLIASONMEDICAL;
        } else if (documentType
          .equals(DOCUMENTTYPE.FB_INCOMING_LIAISON_NONMEDICAL)) {
          newDtls.category = BDMTASKCATEGORY.BDMIOAPPLICATIONFB;
          newDtls.type = BDMTASKTYPE.BDMFBLIASONNONMEDICAL;
        } else if (documentType.equals(DOCUMENTTYPE.INTERIM_APPLICATION)) {
          newDtls.category = BDMTASKCATEGORY.BDMIOAPPLICATIONFB;
          newDtls.type = BDMTASKTYPE.FB_INTERIM_APP;
        } else if (documentType.equals(DOCUMENTTYPE.ADDRESS)) {
          newDtls.category = BDMTASKCATEGORY.BDMIOAPPLICATIONFB;
          newDtls.type = BDMTASKTYPE.BDMFBADDRESS;
        } else if (documentType
          .equals(DOCUMENTTYPE.FOR_TASKTYPE_DIRECT_DEPOSIT)) {
          newDtls.category = BDMTASKCATEGORY.BDMIOAPPLICATIONFB;
          newDtls.type = BDMTASKTYPE.ADDRESSCHANGE_DIRECTDEPOSIT_PWS;
        } else if (documentType
          .equals(DOCUMENTTYPE.CONSENT_TO_COMMUNICATE_1603)) {
          newDtls.category = BDMTASKCATEGORY.BDMIOAPPLICATIONFB;
          newDtls.type = BDMTASKTYPE.BDMFBCONSENTTOCUMMUNICATE1603;
        } else if (documentType
          .equals(DOCUMENTTYPE.AUTHORIZATION_TO_COMMUNICATE_3015)) {
          newDtls.category = BDMTASKCATEGORY.BDMIOAPPLICATIONFB;
          newDtls.type = BDMTASKTYPE.BDMFBAUTHORIZATIONTOCOMMUNICATE3015;
        } else if (documentType.equals(DOCUMENTTYPE.CORRESPONDENCE)) {
          newDtls.category = BDMTASKCATEGORY.BDMIOAPPLICATIONFB;
          newDtls.type = BDMTASKTYPE.BDMFBCORRESPONDENCE;
        } else if (documentType.equals(DOCUMENTTYPE.POA)) {
          newDtls.category = BDMTASKCATEGORY.BDMIOAPPLICATIONFB;
          newDtls.type = BDMTASKTYPE.BDMFBPOA;
        } else if (documentType.equals(DOCUMENTTYPE.TRUSTEE)) {
          newDtls.category = BDMTASKCATEGORY.BDMIOAPPLICATIONFB;
          newDtls.type = BDMTASKTYPE.BDMFBTRUSTEE;
        }

        bdmTask.insert(newDtls);
      }
    }
  }

  protected long getTaskIDForProcessInstanceID(final long processInstanceID)
    throws AppException, InformationalException {

    final ActivityInstance activityInstanceObj =
      ActivityInstanceFactory.newInstance();
    final ProcessInstanceKey instanceKey = new ProcessInstanceKey();
    long taskID = 0;

    instanceKey.processInstanceID = processInstanceID;

    final ActivityInstanceDtlsList instanceDtlsList =
      activityInstanceObj.searchByProcessInstanceID(instanceKey);

    for (int i = 0; i < instanceDtlsList.dtls.size(); i++) {
      if (instanceDtlsList.dtls.item(i).taskID != 0) {
        taskID = instanceDtlsList.dtls.item(i).taskID;
        if (taskID != 0) {
          break;
        }
      }
    }

    return taskID;
  }

  /**
   * Method to get the WorkQueueID for the person privacy request task
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private Long getWorkQueueIDForPerson(final Long concernRoleID)
    throws AppException, InformationalException {

    Long workQueueID = CuramConst.kLongZero;

    final BDMFECase bdmfeCase = BDMFECaseFactory.newInstance();
    final BDMWorkQueueCountryLink bdmWorkQueueCountryLink =
      BDMWorkQueueCountryLinkFactory.newInstance();
    final curam.core.facade.intf.CaseHeader caseHeaderFacade =
      curam.core.facade.fact.CaseHeaderFactory.newInstance();

    final ConcernRoleIDStatusCodeKey concernRoleIDStatusCodeKey =
      new ConcernRoleIDStatusCodeKey();
    concernRoleIDStatusCodeKey.dtls.concernRoleID = concernRoleID;
    concernRoleIDStatusCodeKey.dtls.statusCode = CASESTATUS.OPEN;

    final CaseHeaderDtlsList caseHeaderDtlsList =
      caseHeaderFacade.searchByConcernRoleID(concernRoleIDStatusCodeKey);

    for (int i = 0; i < caseHeaderDtlsList.dtlsList.dtls.size(); i++) {

      final CaseHeaderDtls caseHeaderDtls =
        caseHeaderDtlsList.dtlsList.dtls.item(i);

      if (caseHeaderDtls.caseTypeCode.equals(CASECATEGORY.INTEGRATEDCASE)) {

        final BDMFECaseKey bdmfeCaseKey = new BDMFECaseKey();
        bdmfeCaseKey.caseID = caseHeaderDtls.caseID;
        final NotFoundIndicator bdmfeCaseNotFoundIndicator =
          new NotFoundIndicator();

        final BDMFECaseDtls feCaseDtls =
          bdmfeCase.read(bdmfeCaseNotFoundIndicator, bdmfeCaseKey);

        if (!bdmfeCaseNotFoundIndicator.isNotFound()) {

          final BDMWorkQueueCountryLinkKey bdmWorkQueueCountryLinkKey =
            new BDMWorkQueueCountryLinkKey();
          bdmWorkQueueCountryLinkKey.countryCode = feCaseDtls.countryCode;
          workQueueID = bdmWorkQueueCountryLink
            .read(bdmWorkQueueCountryLinkKey).workQueueID;
          break;
        }
      }
    }

    if (workQueueID == CuramConst.kLongZero) {

      final String currentLoggedInUser = TransactionInfo.getProgramUser();
      workQueueID =
        bdmUtil.getWorkQueueIDByProvOfLoggedInUser(currentLoggedInUser);

    }

    return workQueueID;
  }

  /**
   * Method to get the Oral Written Communication details for task subject
   *
   * @param bdmConcernRoleCommRMKey
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private StringBuffer getOralWrittenLanguageForTask(final Long concernRoleID)
    throws AppException, InformationalException {

    final BDMFECase bdmfeCase = BDMFECaseFactory.newInstance();

    String orallanuage = CuramConst.gkEmpty;
    String writtenlanuage = CuramConst.gkEmpty;
    StringBuffer oralWrittenLanguage =
      new StringBuffer(BDMConstants.gkLocaleUpperEN);

    DynamicEvidenceDataAttributeDtlsList dynamicEvidenceDataAttributeDtlsList =
      new DynamicEvidenceDataAttributeDtlsList();

    final EvidenceDescriptorDtls evidenceDescriptorDtlsKey =
      new EvidenceDescriptorDtls();
    evidenceDescriptorDtlsKey.participantID = concernRoleID;
    evidenceDescriptorDtlsKey.evidenceType = PDCConst.PDCCONTACTPREFERENCES;
    evidenceDescriptorDtlsKey.statusCode = EVIDENCEDESCRIPTORSTATUS.ACTIVE;

    dynamicEvidenceDataAttributeDtlsList =
      bdmfeCase.getContactPreferencesEvidenceList(evidenceDescriptorDtlsKey);

    DynamicEvidenceDataAttributeDtls dynamicEvidenceDataAttributeDtls = null;

    final int listSize = dynamicEvidenceDataAttributeDtlsList.dtls.size();

    if (listSize > 0) {

      for (int i = 0; i < listSize; i++) {
        dynamicEvidenceDataAttributeDtls =
          new DynamicEvidenceDataAttributeDtls();

        dynamicEvidenceDataAttributeDtls =
          dynamicEvidenceDataAttributeDtlsList.dtls.item(i);

        if (dynamicEvidenceDataAttributeDtls.name
          .equals(BDMConstants.kpreferredOralLanguage)) {

          if (BDMLANGUAGE.ENGLISHL
            .equals(dynamicEvidenceDataAttributeDtls.value)) {
            orallanuage = BDMConstants.gkLocaleUpperEN;
          } else if (BDMLANGUAGE.FRENCHL
            .equals(dynamicEvidenceDataAttributeDtls.value)) {
            orallanuage = BDMConstants.gkLocaleFR;
          }

        } else if (dynamicEvidenceDataAttributeDtls.name
          .equals(BDMConstants.kpreferredWrittenLanguage)) {

          if (BDMLANGUAGE.ENGLISHL
            .equals(dynamicEvidenceDataAttributeDtls.value)) {
            writtenlanuage = BDMConstants.gkLocaleUpperEN;
          } else if (BDMLANGUAGE.FRENCHL
            .equals(dynamicEvidenceDataAttributeDtls.value)) {
            writtenlanuage = BDMConstants.gkLocaleFR;
          }

        }

        if (!StringUtil.isNullOrEmpty(orallanuage)
          && !StringUtil.isNullOrEmpty(writtenlanuage)
          && orallanuage.equals(writtenlanuage)) {
          oralWrittenLanguage = new StringBuffer();
          oralWrittenLanguage.append(orallanuage);
        } else if (!StringUtil.isNullOrEmpty(orallanuage)
          && !StringUtil.isNullOrEmpty(writtenlanuage)
          && !orallanuage.equals(writtenlanuage)) {
          oralWrittenLanguage = new StringBuffer();
          oralWrittenLanguage.append(BDMConstants.gkBIL);

        } else {
          oralWrittenLanguage.append(BDMConstants.gkLocaleUpperEN);
        }

      }

    }
    return oralWrittenLanguage;
  }

  /**
   * Method to get the WorkQueueID for the IC privacy request task
   *
   * @param caseHeaderDtls
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private Long getWorkQueueIDForICCase(final CaseHeaderDtls caseHeaderDtls)
    throws AppException, InformationalException {

    Long workQueueID = CuramConst.kLongZero;
    boolean fecCaseFoundInd = false;

    final BDMFECase bdmfeCase = BDMFECaseFactory.newInstance();
    final curam.ca.gc.bdm.entity.intf.BDMWorkQueueCountryLink bdmWorkQueueCountryLink =
      BDMWorkQueueCountryLinkFactory.newInstance();

    if (caseHeaderDtls.caseTypeCode.equals(CASECATEGORY.INTEGRATEDCASE)) {

      final BDMFECaseKey bdmfeCaseKey = new BDMFECaseKey();
      bdmfeCaseKey.caseID = caseHeaderDtls.caseID;

      final NotFoundIndicator bdmFECaseNotFoundIndicator =
        new NotFoundIndicator();

      final BDMFECaseDtls feCaseDtls =
        bdmfeCase.read(bdmFECaseNotFoundIndicator, bdmfeCaseKey);

      if (!bdmFECaseNotFoundIndicator.isNotFound()) {

        final BDMWorkQueueCountryLinkKey bdmWorkQueueCountryLinkKey =
          new BDMWorkQueueCountryLinkKey();
        bdmWorkQueueCountryLinkKey.countryCode = feCaseDtls.countryCode;

        final NotFoundIndicator bdmWorkQueueCountryLinkNotFoundIndicator =
          new NotFoundIndicator();

        final BDMWorkQueueCountryLinkDtls bdmWorkQueueCountryLinkDtls =
          bdmWorkQueueCountryLink.read(
            bdmWorkQueueCountryLinkNotFoundIndicator,
            bdmWorkQueueCountryLinkKey);

        if (!bdmWorkQueueCountryLinkNotFoundIndicator.isNotFound()) {
          workQueueID = bdmWorkQueueCountryLinkDtls.workQueueID;
          fecCaseFoundInd = true;
        }

      }

    }

    if (workQueueID == CuramConst.kLongZero && !fecCaseFoundInd) {

      final String currentLoggedInUser = TransactionInfo.getProgramUser();
      workQueueID =
        bdmUtil.getWorkQueueIDByProvOfLoggedInUser(currentLoggedInUser);

    }

    return workQueueID;
  }

  /**
   * Method to get the task IV deadline
   *
   * @param documentType
   * @return
   */
  private Integer getDeadLineForTaskVIRequired(final String documentType) {

    Integer taskDeadlineDays = 0;

    if (documentType.equals(DOCUMENTTYPE.ADDRESS)
      || documentType.equals(DOCUMENTTYPE.DIRECT_DEPOSIT)) {

      taskDeadlineDays = Integer.parseInt(Configuration.getProperty(
        EnvVars.BDM_UPLOAD_DIRECTDEPOSIT_ATTACHMENT_TASK_DEADLINE));

    } else if (documentType.equals(DOCUMENTTYPE.CONSENT_TO_COMMUNICATE_1603)
      || documentType
        .equals(DOCUMENTTYPE.AUTHORIZATION_TO_COMMUNICATE_3015)) {

      taskDeadlineDays = Integer.parseInt(Configuration.getProperty(
        EnvVars.BDM_UPLOAD_AUTHORCONSENT_ATTACHMENT_TASK_DEADLINE));

    } else if (documentType.equals(DOCUMENTTYPE.CORRESPONDENCE)) {

      taskDeadlineDays = Integer.parseInt(Configuration.getProperty(
        EnvVars.BDM_UPLOAD_CORRESPONDANCE_ATTACHMENT_TASK_DEADLINE));
    } else if (documentType.equals(DOCUMENTTYPE.POA)
      || documentType.equals(DOCUMENTTYPE.TRUSTEE)) {

      taskDeadlineDays = Integer.parseInt(Configuration.getProperty(
        EnvVars.BDM_UPLOAD_POAORTRUSTEE_ATTACHMENT_TASK_DEADLINE));
    }

    return taskDeadlineDays;
  }

  private Integer getDeadlineNoOfDays(final String documentType) {

    Integer taskDeadlineDays = 0;

    if (documentType.equals(DOCUMENTTYPE.FB_INCOMING_LIAISON_MEDICAL)
      || documentType.equals(DOCUMENTTYPE.FB_INCOMING_LIAISON_NONMEDICAL)) {

      taskDeadlineDays = Integer.parseInt(Configuration.getProperty(
        EnvVars.BDM_FEC_UPLOAD_LIAISON_ATTACHMENT_TASK_DEADLINE));

    } else if (documentType.equals(DOCUMENTTYPE.BESS)
      || documentType.equals(DOCUMENTTYPE.BESS_ADDITIONAL_DOCUMENTS)
      || documentType.equals(DOCUMENTTYPE.INTERIM_APPLICATION)) {

      taskDeadlineDays = Integer.parseInt(Configuration
        .getProperty(EnvVars.BDM_FEC_UPLOAD_BEES_ATTACHMENT_TASK_DEADLINE));
    }
    // START: Bug 99654: Task #6 deadline missing for Direct deposit attachment
    else if (documentType.equals(DOCUMENTTYPE.ADDRESS)) {

      taskDeadlineDays = Integer.parseInt(Configuration.getProperty(
        EnvVars.BDM_FEC_UPLOAD_ADDRESS_ATTACHMENT_TASK_DEADLINE));

    } else if (documentType
      .equals(DOCUMENTTYPE.FOR_TASKTYPE_DIRECT_DEPOSIT)) {

      taskDeadlineDays = Integer.parseInt(Configuration.getProperty(
        EnvVars.BDM_FEC_UPLOAD_DIRECTDEPOSIT_ATTACHMENT_TASK_DEADLINE));
    } else if (documentType
      .equals(DOCUMENTTYPE.AUTHORIZATION_TO_COMMUNICATE_3015)) {

      taskDeadlineDays = Integer.parseInt(Configuration.getProperty(
        EnvVars.BDM_FEC_UPLOAD_AUTHORIZE_COMMUNICATE_ATTACHMENT_TASK_DEADLINE));

    } else if (documentType
      .equals(DOCUMENTTYPE.CONSENT_TO_COMMUNICATE_1603)) {

      taskDeadlineDays = Integer.parseInt(Configuration.getProperty(
        EnvVars.BDM_FEC_UPLOAD_CONSENT_COMMUNICATE_1603_ATTACHMENT_TASK_DEADLINE));
    } else if (documentType.equals(DOCUMENTTYPE.CORRESPONDENCE)) {

      taskDeadlineDays = Integer.parseInt(Configuration.getProperty(
        EnvVars.BDM_FEC_UPLOAD_CORRESPONDENCE_ATTACHMENT_TASK_DEADLINE));
    } else if (documentType.equals(DOCUMENTTYPE.POA)) {

      taskDeadlineDays = Integer.parseInt(Configuration
        .getProperty(EnvVars.BDM_FEC_UPLOAD_POA_ATTACHMENT_TASK_DEADLINE));
    } else if (documentType.equals(DOCUMENTTYPE.TRUSTEE)) {

      taskDeadlineDays = Integer.parseInt(Configuration.getProperty(
        EnvVars.BDM_FEC_UPLOAD_TRUSTEE_ATTACHMENT_TASK_DEADLINE));
    }
    // END: Bug 99654: Task #6 deadline missing for Direct deposit attachment
    return taskDeadlineDays;
  }

  @Override
  public void createFECAttachmentTask(
    final BDMCreateCaseAttachmentDetails bdmCreateCaseAttachmentDetails,
    final AttachmentIDAndAttachmentLinkIDStruct attachmentIDAndAttachmentLinkIDStruct)
    throws AppException, InformationalException {

    if (bdmUtil.isAttachmentTaskIIRequired(
      bdmCreateCaseAttachmentDetails.dtls.createCaseAttachmentDetails.documentType)
      // BEGIN: BUG 98038: Task Issue fix
      || bdmUtil.isAttachmentTaskVIRequired(
        bdmCreateCaseAttachmentDetails.dtls.createCaseAttachmentDetails.documentType)) {
      // END: BUG 98038: Task Issue fix
      final NotFoundIndicator caseHeaderNotFoundIndicator =
        new NotFoundIndicator();
      final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
      caseHeaderKey.caseID =
        bdmCreateCaseAttachmentDetails.dtls.createCaseAttachmentDetails.caseID;
      final CaseHeaderDtls caseHeaderDtls = CaseHeaderFactory.newInstance()
        .read(caseHeaderNotFoundIndicator, caseHeaderKey);

      // Bug 99298: Bug on Task #02 - The Task subject summary does not display
      // as specified in Cofiguration
      final BDMFECaseKey bdmfeCaseKey = new BDMFECaseKey();
      bdmfeCaseKey.caseID =
        bdmCreateCaseAttachmentDetails.dtls.createCaseAttachmentDetails.caseID;
      final BDMFECaseDtls bdmfeCaseDtls =
        BDMFECaseFactory.newInstance().read(bdmfeCaseKey);

      final BDMAttachmentTaskCreateDetails bdmAttachmentTaskCreateDetails =
        new BDMAttachmentTaskCreateDetails();

      boolean subjectParameterPopulatedForTaskII_Ind = false;
      boolean subjectParameterPopulatedForTaskVI_Ind = false;

      if (!caseHeaderNotFoundIndicator.isNotFound()) {

        final List<Object> enactmentStructs = new ArrayList<>();

        if (bdmUtil.isAttachmentTaskIIRequired(
          bdmCreateCaseAttachmentDetails.dtls.createCaseAttachmentDetails.documentType)) {

          final String fecTypCdDesc = CodetableUtil.getCodetableDescription(
            PRODUCTCATEGORY.TABLENAME, caseHeaderDtls.integratedCaseType);

          final String countryTypCdDesc =
            CodetableUtil.getCodetableDescription(BDMSOURCECOUNTRY.TABLENAME,
              bdmfeCaseDtls.countryCode);

          // START Bug 110431: When language switched to French via preference
          // from the user profile the Task subject line is still English
          bdmAttachmentTaskCreateDetails.attachmentReceipt =
            bdmCreateCaseAttachmentDetails.dtls.createCaseAttachmentDetails.receiptDate;
          bdmAttachmentTaskCreateDetails.oralWrittenLanguage =
            getOralWrittenLanguageForTask(caseHeaderDtls.concernRoleID)
              .toString();
          bdmAttachmentTaskCreateDetails.documentTypeCdDesc =
            CodetableUtil.getCodetableDescription(DOCUMENTTYPE.TABLENAME,
              bdmCreateCaseAttachmentDetails.dtls.createCaseAttachmentDetails.documentType);

          bdmAttachmentTaskCreateDetails.fecTypeDesc = fecTypCdDesc;
          bdmAttachmentTaskCreateDetails.feCaseReference =
            caseHeaderDtls.caseReference;
          bdmAttachmentTaskCreateDetails.coutryCodeDesc = countryTypCdDesc;
          bdmAttachmentTaskCreateDetails.strConcernRoleFullName =
            bdmUtil.getConcernRoleName(caseHeaderDtls.concernRoleID);
          bdmAttachmentTaskCreateDetails.strIdentificationRefernce = bdmUtil
            .getPersonIdentificationDetail(caseHeaderDtls.concernRoleID);

          subjectParameterPopulatedForTaskII_Ind = true;
          // END Bug 110431

        }
        // START: Bug 103263: E2E Defect - Task subject name does not match R1
        // Task Configuration doc for Task-06
        if (bdmUtil.isAttachmentTaskVIRequired(
          bdmCreateCaseAttachmentDetails.dtls.createCaseAttachmentDetails.documentType)) {

          // START Bug 110431: When language switched to French via preference
          // from the user profile the Task subject line is still English
          bdmAttachmentTaskCreateDetails.attachmentReceipt =
            bdmCreateCaseAttachmentDetails.dtls.createCaseAttachmentDetails.receiptDate;

          bdmAttachmentTaskCreateDetails.oralWrittenLanguage =
            getOralWrittenLanguageForTask(caseHeaderDtls.concernRoleID)
              .toString();

          bdmAttachmentTaskCreateDetails.documentTypeCdDesc =
            CodetableUtil.getCodetableDescription(DOCUMENTTYPE.TABLENAME,
              bdmCreateCaseAttachmentDetails.dtls.createCaseAttachmentDetails.documentType);

          bdmAttachmentTaskCreateDetails.strConcernRoleFullName =
            bdmUtil.getConcernRoleName(caseHeaderDtls.concernRoleID);

          bdmAttachmentTaskCreateDetails.strIdentificationRefernce = bdmUtil
            .getPersonIdentificationDetail(caseHeaderDtls.concernRoleID);

          subjectParameterPopulatedForTaskVI_Ind = true;
          // END Bug 110431: When language switched to French via preference
          // from the user profile the Task subject line is still English

        }
        // END: Bug 103263: E2E Defect - Task subject name does not match R1
        // Task Configuration doc for Task-06

        bdmAttachmentTaskCreateDetails.caseID =
          bdmCreateCaseAttachmentDetails.dtls.createCaseAttachmentDetails.caseID;
        bdmAttachmentTaskCreateDetails.concernRoleID =
          caseHeaderDtls.concernRoleID;
        bdmAttachmentTaskCreateDetails.participantRoleID =
          caseHeaderDtls.concernRoleID;
        bdmAttachmentTaskCreateDetails.documentType =
          bdmCreateCaseAttachmentDetails.dtls.createCaseAttachmentDetails.documentType;
        bdmAttachmentTaskCreateDetails.priority = TASKPRIORITY.NORMAL;
        bdmAttachmentTaskCreateDetails.workQueueID =
          getWorkQueueIDForICCase(caseHeaderDtls);
        bdmAttachmentTaskCreateDetails.attachmentID =
          attachmentIDAndAttachmentLinkIDStruct.attachmentID;

        enactmentStructs.add(bdmAttachmentTaskCreateDetails);

        // Task deadline
        // START: Bug 99492: Task #2: FEC Attachment BESS Deadline on Task is 1
        // year instead of 30 days
        final long currentDateTimeInMills = Date.getCurrentDate().asLong();

        long inputDateTimeInMills = CuramConst.kLongZero;

        long durationMills = CuramConst.kLongZero;

        boolean recieptDtIsNotZeroInd = false;

        final int taskDeadlineDays = getDeadlineNoOfDays(
          bdmCreateCaseAttachmentDetails.dtls.createCaseAttachmentDetails.documentType);

        if (!bdmCreateCaseAttachmentDetails.dtls.createCaseAttachmentDetails.receiptDate
          .isZero()) {

          inputDateTimeInMills =
            bdmCreateCaseAttachmentDetails.dtls.createCaseAttachmentDetails.receiptDate
              .addDays(taskDeadlineDays).asLong();

          recieptDtIsNotZeroInd = true;

        } else {
          inputDateTimeInMills =
            Date.getCurrentDate().addDays(taskDeadlineDays).asLong();
        }
        if (recieptDtIsNotZeroInd)
          durationMills = inputDateTimeInMills - currentDateTimeInMills;
        else
          durationMills = inputDateTimeInMills - currentDateTimeInMills;
        // END: Bug 99492: Task #2: FEC Attachment BESS Deadline on Task is 1
        // year instead of 30 days

        final long durationSeconds =
          durationMills / DateTime.kMilliSecsInSecond;

        final BDMUACaseWorkerVerificationEnactKey bdmuaCaseWorkerVerificationEnactKey =
          new BDMUACaseWorkerVerificationEnactKey();
        bdmuaCaseWorkerVerificationEnactKey.deadLineDurationInSec =
          (int) durationSeconds;

        enactmentStructs.add(bdmuaCaseWorkerVerificationEnactKey);

        if (subjectParameterPopulatedForTaskII_Ind) {

          final long kProcessInstanceID =
            EnactmentService.startProcessInV3CompatibilityMode(
              "BDMICBEESFBDocAttachmentUploadTask", enactmentStructs);

          addBDMTask(kProcessInstanceID,
            bdmCreateCaseAttachmentDetails.dtls.createCaseAttachmentDetails.documentType);

        }

        if (subjectParameterPopulatedForTaskVI_Ind) {

          final long kProcessInstanceID =
            EnactmentService.startProcessInV3CompatibilityMode(
              "BDMICDocAttachmentUploadTask", enactmentStructs);

          addBDMTask(kProcessInstanceID,
            bdmCreateCaseAttachmentDetails.dtls.createCaseAttachmentDetails.documentType);

        }

      }
    }

  }
}
