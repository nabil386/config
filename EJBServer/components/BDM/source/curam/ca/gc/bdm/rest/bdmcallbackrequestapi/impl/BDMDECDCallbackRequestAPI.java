package curam.ca.gc.bdm.rest.bdmcallbackrequestapi.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMCALLBACKREQUESTSTATUS;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.entity.fact.BDMDECDRequestForCallbackFactory;
import curam.ca.gc.bdm.entity.intf.BDMDECDRequestForCallback;
import curam.ca.gc.bdm.entity.struct.BDMDECDRequestForCallbackDtls;
import curam.ca.gc.bdm.entity.struct.BDMDECDRequestForCallbackDtlsStruct1List;
import curam.ca.gc.bdm.entity.struct.BDMDECDRequestForCallbackGuidStatusKey;
import curam.ca.gc.bdm.entity.struct.BDMDECDRequestForCallbackPhoneStatusKey;
import curam.ca.gc.bdm.entity.struct.BDMTaskCreateCallbackDetails;
import curam.ca.gc.bdm.entity.struct.BDMUsernameGuidLinkDetails;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskSkillTypeKey;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskSkillTypeList;
import curam.ca.gc.bdm.message.BDMREQUESTFORCALLBACK;
import curam.ca.gc.bdm.message.BDMRESTAPIERRORMESSAGE;
import curam.ca.gc.bdm.rest.bdmcallbackrequestapi.struct.BDMDECDCallbackRequest;
import curam.ca.gc.bdm.rest.bdmdecdrestapi.impl.BDMDECDApplicationAPI;
import curam.ca.gc.bdm.rest.bdmdecdrestapi.struct.BDMDECDGuidKey;
import curam.ca.gc.bdm.sl.bdmworkallocation.fact.BDMWorkAllocationTaskFactory;
import curam.ca.gc.bdm.sl.bdmworkallocation.intf.BDMWorkAllocationTask;
import curam.ca.gc.bdm.sl.bdmworkallocation.struct.BDMVSGTaskCreateDetails;
import curam.citizenworkspace.message.CITIZENPORTALEXTERNALPARTYLINK;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccountInfo;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccountManager;
import curam.codetable.ASSIGNEETYPE;
import curam.codetable.SKILLTYPE;
import curam.codetable.TASKPRIORITY;
import curam.core.facade.infrastructure.struct.PersonAndEvidenceTypeList;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.participant.impl.ConcernRole;
import curam.pdc.facade.fact.PDCPersonFactory;
import curam.pdc.facade.intf.PDCPerson;
import curam.pdc.facade.struct.PDCEvidenceDetailsList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.LocalisableString;
import curam.util.internal.workflow.fact.ActivityInstanceFactory;
import curam.util.internal.workflow.intf.ActivityInstance;
import curam.util.internal.workflow.struct.ActivityInstanceDtlsList;
import curam.util.internal.workflow.struct.ProcessInstanceKey;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.type.CodeTable;
import curam.util.type.DateTime;
import curam.util.type.UniqueID;
import curam.util.workflow.impl.EnactmentService;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * User story 1066 : INT10.3 Request for Callback (DECD)
 * API to accept and process the information available from
 * Request for Callback from DECD, resulting in a work ticket/task for an Agent
 * to
 * call the Client back.
 *
 * @author raghunath.govindaraj
 *
 */
public class BDMDECDCallbackRequestAPI extends
  curam.ca.gc.bdm.rest.bdmcallbackrequestapi.base.BDMDECDCallbackRequestAPI {

  @Inject
  private CitizenWorkspaceAccountManager citizenWorkspaceAccountManager;

  public BDMDECDCallbackRequestAPI() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public void
    callbackRequestFromDECD(final BDMDECDCallbackRequest callbackrequest)
      throws AppException, InformationalException {

    Trace.kTopLevelLogger.info(" Callbackrequest from DECD");
    Trace.kTopLevelLogger
      .info(" Callbackrequest GUID:" + callbackrequest.on_Behalf_Of
        + " AND Phonenumber =   " + callbackrequest.telephonenumber);
    Trace.kTopLevelLogger.info(" Callbackrequest Firstname:"
      + callbackrequest.firstname + " Lastname :" + callbackrequest.lastname);

    // validate
    validateMandatoryFields(callbackrequest);
    validateEmailAddress(callbackrequest.email);
    validatePhoneNumber(callbackrequest.telephonenumber);
    validateDuplicateRequest(callbackrequest);
    if (callbackrequest.language == null) {
      callbackrequest.language = "EN";
    } else {
      if (callbackrequest.language.equalsIgnoreCase("en")) {
        callbackrequest.language = "EN";
      }
      if (callbackrequest.language.equalsIgnoreCase("fr")) {
        callbackrequest.language = "FR";
      }
    }

    // create task
    final long taskID = createCallBackRequestTaskForDECD(callbackrequest);

    // save to table
    final BDMDECDRequestForCallback requestCallbackObj =
      BDMDECDRequestForCallbackFactory.newInstance();

    final BDMDECDRequestForCallbackDtls requestForCallbackDtls =
      new BDMDECDRequestForCallbackDtls();
    requestForCallbackDtls.assign(callbackrequest);
    requestForCallbackDtls.guid = callbackrequest.on_Behalf_Of;
    requestForCallbackDtls.firstName = callbackrequest.firstname;
    requestForCallbackDtls.lastName = callbackrequest.lastname;
    requestForCallbackDtls.status = BDMCALLBACKREQUESTSTATUS.PENDING;
    requestForCallbackDtls.createdOn = DateTime.getCurrentDateTime();
    requestForCallbackDtls.callbackID = UniqueID.nextUniqueID();
    requestForCallbackDtls.taskID = taskID;
    requestForCallbackDtls.telephoneNumber = callbackrequest.telephonenumber;
    requestForCallbackDtls.preferredContactTime =
      callbackrequest.preferredcontacttime;
    requestForCallbackDtls.accessibilityDetails =
      callbackrequest.accessibilitydetails;
    requestForCallbackDtls.benefitType = callbackrequest.benefittype;
    requestForCallbackDtls.freeText = callbackrequest.freetext;
    requestForCallbackDtls.discussionTopic = callbackrequest.discussiontopic;
    requestCallbackObj.insert(requestForCallbackDtls);

  }

  /**
   *
   * @param callbackrequest
   */
  void validateMandatoryFields(final BDMDECDCallbackRequest callbackrequest)
    throws AppException {

    Trace.kTopLevelLogger.info(
      " Callbackrequest phoneNumber:" + callbackrequest.telephonenumber);
    if (StringUtil.isNullOrEmpty(callbackrequest.telephonenumber.trim())) {

      final AppException exceptionErr = new AppException(
        BDMRESTAPIERRORMESSAGE.BDM_REQUEST_FOR_CALLBACK_API_FIELD_IS_MANDATORY);
      exceptionErr.arg("Telephone Number");
      Trace.kTopLevelLogger
        .debug("Callbackrequest telephonenumber is mandatory: "
          + exceptionErr.getMessage());
      throw exceptionErr;

    }
    if (StringUtil.isNullOrEmpty(callbackrequest.language.trim())) {

      final AppException exceptionErr = new AppException(
        BDMRESTAPIERRORMESSAGE.BDM_REQUEST_FOR_CALLBACK_API_FIELD_IS_MANDATORY);
      exceptionErr.arg("Language of Communication");
      Trace.kTopLevelLogger
        .debug("Callbackrequest Language of Communication: "
          + exceptionErr.getMessage());
      throw exceptionErr;

    }
    if (StringUtil.isNullOrEmpty(callbackrequest.firstname.trim())) {

      final AppException exceptionErr = new AppException(
        BDMRESTAPIERRORMESSAGE.BDM_REQUEST_FOR_CALLBACK_API_FIELD_IS_MANDATORY);
      exceptionErr.arg("First Name");
      Trace.kTopLevelLogger.debug("Callbackrequest First Name is mandatory: "
        + exceptionErr.getMessage());
      throw exceptionErr;

    }
    if (StringUtil.isNullOrEmpty(callbackrequest.lastname.trim())) {

      final AppException exceptionErr = new AppException(
        BDMRESTAPIERRORMESSAGE.BDM_REQUEST_FOR_CALLBACK_API_FIELD_IS_MANDATORY);
      exceptionErr.arg("Last Name");
      Trace.kTopLevelLogger.debug("Callbackrequest Last name is mandatory: "
        + exceptionErr.getMessage());
      throw exceptionErr;

    }
    if (StringUtil.isNullOrEmpty(callbackrequest.benefittype.trim())) {

      final AppException exceptionErr = new AppException(
        BDMRESTAPIERRORMESSAGE.BDM_REQUEST_FOR_CALLBACK_API_FIELD_IS_MANDATORY);
      exceptionErr.arg("Benefit Type");
      Trace.kTopLevelLogger
        .debug("Callbackrequest Benefit type is mandatory: "
          + exceptionErr.getMessage());
      throw exceptionErr;

    }
    if (StringUtil.isNullOrEmpty(callbackrequest.freetext.trim())) {

      final AppException exceptionErr = new AppException(
        BDMRESTAPIERRORMESSAGE.BDM_REQUEST_FOR_CALLBACK_API_FIELD_IS_MANDATORY);
      exceptionErr.arg("Free Text");
      Trace.kTopLevelLogger.debug("Callbackrequest Free text is mandatory: "
        + exceptionErr.getMessage());
      throw exceptionErr;

    }
    if (StringUtil.isNullOrEmpty(callbackrequest.discussiontopic.trim())) {

      final AppException exceptionErr = new AppException(
        BDMRESTAPIERRORMESSAGE.BDM_REQUEST_FOR_CALLBACK_API_FIELD_IS_MANDATORY);
      exceptionErr.arg("Discussion Topic�");
      Trace.kTopLevelLogger
        .debug("Callbackrequest Discussion Topic�  is mandatory: "
          + exceptionErr.getMessage());
      throw exceptionErr;

    }
    if (StringUtil
      .isNullOrEmpty(callbackrequest.preferredcontacttime.trim())) {

      final AppException exceptionErr = new AppException(
        BDMRESTAPIERRORMESSAGE.BDM_REQUEST_FOR_CALLBACK_API_FIELD_IS_MANDATORY);
      exceptionErr.arg("Contact preference � Time of the day");
      Trace.kTopLevelLogger.debug(
        "Callbackrequest Preferred Contact preference � Time of the day  is mandatory: "
          + exceptionErr.getMessage());
      throw exceptionErr;

    }

  }

  /**
   *
   * @param email
   * @return
   */
  String validateEmailAddress(final String email) throws AppException {

    Trace.kTopLevelLogger.info(" Callbackrequest email:" + email);
    final String validateEmail = CuramConst.gkEmpty;
    final String emailRegex =
      "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^]+)*@(?:[A-Za-z0-9-]+\\.)+[A-Za-z]{2,6}$";

    if (email != null && !email.trim().isEmpty()) {
      if (email.length() > 254) {
        final AppException exceptionErr = new AppException(
          BDMRESTAPIERRORMESSAGE.BDM_REQUEST_FOR_CALLBACK_API_EMAIL_LENGTH_ABOVE_LIMIT);
        Trace.kTopLevelLogger.debug(
          "Callbackrequest email length: " + exceptionErr.getMessage());
        throw exceptionErr;
      } else if (!email.matches(emailRegex)) {
        final AppException exceptionErr = new AppException(
          BDMRESTAPIERRORMESSAGE.BDM_REQUEST_FOR_CALLBACK_API_INVALID_EMAIL);
        Trace.kTopLevelLogger.debug(
          "Callbackrequest email format : " + exceptionErr.getMessage());
        throw exceptionErr;
      }
    }
    return validateEmail;
  }

  /**
   *
   * @param phoneNumber
   * @return
   */
  String validatePhoneNumber(final String phoneNumber) throws AppException {

    Trace.kTopLevelLogger.info(" Callbackrequest phoneNumber:" + phoneNumber);
    final String validatePhoneNumber = CuramConst.gkEmpty;
    final String patterns =
      "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
        + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
        + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";

    final Pattern pattern = Pattern.compile(patterns);
    if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
      final Matcher matcher = pattern.matcher(phoneNumber);
      if (!matcher.matches()) {
        final AppException exceptionErr = new AppException(
          BDMRESTAPIERRORMESSAGE.BDM_REQUEST_FOR_CALLBACK_API_INVALID_PHONE_NUMBER);
        Trace.kTopLevelLogger
          .debug("Validate phonenumber error : " + exceptionErr.getMessage());
        throw exceptionErr;
      }
    }
    return validatePhoneNumber;
  }

  /**
   *
   * @param callbackrequest
   */
  void validateDuplicateRequest(final BDMDECDCallbackRequest callbackrequest)
    throws AppException, InformationalException {

    Trace.kTopLevelLogger
      .info(" Callbackrequest GUID:" + callbackrequest.on_Behalf_Of);
    if (!StringUtil.isNullOrEmpty(callbackrequest.on_Behalf_Of)) {
      // This is request from DECD authenticate user
      final BDMDECDRequestForCallback callbackReqObj =
        BDMDECDRequestForCallbackFactory.newInstance();
      final BDMDECDRequestForCallbackGuidStatusKey guidStatusKey =
        new BDMDECDRequestForCallbackGuidStatusKey();
      guidStatusKey.guid = callbackrequest.on_Behalf_Of;
      guidStatusKey.status = BDMCALLBACKREQUESTSTATUS.PENDING;

      final BDMDECDRequestForCallbackDtlsStruct1List callbackRecList =
        callbackReqObj.readByGuidAndStatus(guidStatusKey);
      if (callbackRecList != null && callbackRecList.dtls.size() > 0) {

        final AppException exceptionErr = new AppException(
          BDMRESTAPIERRORMESSAGE.BDM_REQUEST_FOR_CALLBACK_API_PENDING_REQUEST_ALREADY_USER);
        Trace.kTopLevelLogger
          .debug("Duplicate request for User : " + exceptionErr.getMessage());
        throw exceptionErr;
      }
    } else if (StringUtil.isNullOrEmpty(callbackrequest.on_Behalf_Of)) {
      // This is request from DECD un-authenticate user
      final BDMDECDRequestForCallback callbackReqObj =
        BDMDECDRequestForCallbackFactory.newInstance();
      final BDMDECDRequestForCallbackPhoneStatusKey telephoneStatusKey =
        new BDMDECDRequestForCallbackPhoneStatusKey();
      telephoneStatusKey.telephoneNumber = callbackrequest.telephonenumber;
      telephoneStatusKey.status = BDMCALLBACKREQUESTSTATUS.PENDING;

      final BDMDECDRequestForCallbackDtlsStruct1List callbackRecList =
        callbackReqObj.readByPhoneAndStatus(telephoneStatusKey);
      if (callbackRecList != null && callbackRecList.dtls.size() > 0) {
        final AppException exceptionErr = new AppException(
          BDMRESTAPIERRORMESSAGE.BDM_REQUEST_FOR_CALLBACK_API_PENDING_REQUEST_ALREADY_PHONENUMBER);
        Trace.kTopLevelLogger.debug(
          "Duplicate phonenumber error : " + exceptionErr.getMessage());
        throw exceptionErr;
      }
    }

  }

  /**
   *
   * @throws AppException
   * @throws InformationalException
   */
  public long createCallBackRequestTaskForDECD(
    final BDMDECDCallbackRequest callbackrequest)
    throws AppException, InformationalException {

    final long kProcessInstanceID =
      this.createCallBackRequestTask(callbackrequest);

    long taskID = 0;
    // Get taskID using processInstanceID
    if (kProcessInstanceID != 0) {
      taskID = this.getTaskIDForProcessInstanceID(kProcessInstanceID);
    }

    /*
     * if (taskID != 0) {
     * This is not required as Task will be linked to Skill Type in Workflow
     * // Create bizObjectAssociation for linking skillType with task
     * this.createBizObjectAssociationWithSkillType(taskID);
     * }
     */

    return taskID;
  }

  /***
   *
   * @param callbackrequest
   * @param role
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private long
    createCallBackRequestTask(final BDMDECDCallbackRequest callbackrequest)
      throws AppException, InformationalException {

    // Task 63079 2022-07-16 Get person participantID using GUID
    final BDMDECDGuidKey guidKey = new BDMDECDGuidKey();
    guidKey.on_Behalf_Of = callbackrequest.on_Behalf_Of;
    final BDMUsernameGuidLinkDetails guidUserNameDetail =
      new BDMDECDApplicationAPI().getUAUserNameByGuid(guidKey);

    final ConcernRole concernRole =
      getConcernRoleBy(guidUserNameDetail.username);

    final List enactmentStructs = new ArrayList();
    final BDMVSGTaskCreateDetails bdmVSGTaskCreateDetails =
      new BDMVSGTaskCreateDetails();
    bdmVSGTaskCreateDetails.participantRoleID = concernRole.getID();
    bdmVSGTaskCreateDetails.participantType = ASSIGNEETYPE.PERSON;
    bdmVSGTaskCreateDetails.priority = TASKPRIORITY.NORMAL;

    final BDMTaskCreateCallbackDetails bdmTaskCreateDetails =
      new BDMTaskCreateCallbackDetails();

    final curam.core.sl.struct.TaskCreateDetails taskcreateDetails =
      new curam.core.sl.struct.TaskCreateDetails();

    final BDMWorkAllocationTask bdmWorkAllocationTaskObj =
      BDMWorkAllocationTaskFactory.newInstance();

    final BDMTaskSkillTypeKey bdmTaskSkillTypeKey = new BDMTaskSkillTypeKey();
    bdmTaskSkillTypeKey.skillType = SKILLTYPE.VSG02;

    curam.ca.gc.bdm.sl.bdmworkallocation.struct.BDMWorkQueueID bdmWorkQueueID =
      new curam.ca.gc.bdm.sl.bdmworkallocation.struct.BDMWorkQueueID();

    bdmWorkQueueID =
      bdmWorkAllocationTaskObj.getWorkQueueIDBySkillType(bdmTaskSkillTypeKey);

    // Get bdmTaskClassificationID by skillType
    BDMTaskSkillTypeList bdmTaskSkillTypeList = new BDMTaskSkillTypeList();
    bdmTaskSkillTypeList =
      bdmWorkAllocationTaskObj.searchBDMTaskSkillType(bdmTaskSkillTypeKey);

    if (bdmTaskSkillTypeList.dtls.size() > 0) {
      bdmVSGTaskCreateDetails.bdmTaskClassificationID =
        bdmTaskSkillTypeList.dtls.get(0).bdmTaskClassificationID;
    }

    bdmVSGTaskCreateDetails.workQueueID = bdmWorkQueueID.workQueueID;

    // Subject: <SkillType> <client oral communication> Call Back Required for
    // <client name> <person reference ID>
    final LocalisableString notificationMsg = new LocalisableString(
      curam.ca.gc.bdm.message.BDMCALLBACKREQUESTTASKSUBJECT.INF_BDM_CALLBACK_REQUEST_TASK_SUBJECT);
    notificationMsg.arg(BDMUtil.getCodeTableDescriptionForUserLocale(
      SKILLTYPE.TABLENAME, bdmTaskSkillTypeKey.skillType));

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

    final LocalisableString taskSubject = new LocalisableString(
      BDMREQUESTFORCALLBACK.BDM_DECD_REQUEST_FOR_CALLBACK_TASK_SUBJECT);
    taskSubject.arg(CodeTable.getOneItem(SKILLTYPE.TABLENAME,
      bdmTaskSkillTypeKey.skillType));
    taskSubject.arg(orallanuage);
    taskSubject.arg(callbackrequest.firstname + CuramConst.gkSpace
      + callbackrequest.lastname);

    final LocalisableString taskDetails = new LocalisableString(
      BDMREQUESTFORCALLBACK.BDM_DECD_REQUEST_FOR_CALLBACK_TASK_DETAILS);
    taskDetails.arg(callbackrequest.telephonenumber);
    taskDetails.arg(callbackrequest.language);
    taskDetails.arg(callbackrequest.preferredcontacttime);
    taskDetails.arg(callbackrequest.benefittype);
    taskDetails.arg(callbackrequest.sin);
    taskDetails.arg(callbackrequest.discussiontopic);
    taskDetails.arg(callbackrequest.freetext);
    taskDetails.arg(callbackrequest.email);
    taskDetails.arg(callbackrequest.accessibilitydetails);

    taskcreateDetails.subject = taskSubject.getMessage();
    bdmTaskCreateDetails.taskDetails = taskSubject.getMessage()
      + CuramConst.gkNewLine + taskDetails.getMessage();

    Trace.kTopLevelLogger.info(" Callback request details for Task :"
      + bdmTaskCreateDetails.taskDetails);

    enactmentStructs.add(taskcreateDetails);
    enactmentStructs.add(bdmVSGTaskCreateDetails);
    enactmentStructs.add(callbackrequest);

    String workflowDisabled =
      Configuration.getProperty(EnvVars.ENV_ENACT_WORKFLOW_PROCESS_DISABLED);

    if (workflowDisabled == null) {
      workflowDisabled = "NO";
    }

    long kProcessInstanceID = 0;
    final String kTaskDefinitionID = "BDMDECDRequestForCallBack";

    if (workflowDisabled.equalsIgnoreCase(EnvVars.ENV_VALUE_NO)) {

      kProcessInstanceID = EnactmentService.startProcessInV3CompatibilityMode(
        kTaskDefinitionID, enactmentStructs);

    }

    return kProcessInstanceID;
  }

  /**
   * Helper method to get taskID using processInstanceID
   *
   * @param processInstanceID
   * @return taskID
   * @throws AppException
   * @throws InformationalException
   */
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

  /***
   *
   * @param taskID
   * @param bdmTaskClassificationID
   * @throws AppException
   * @throws InformationalException
   */
  protected void createBizObjectAssociationWithSkillType(final long taskID)
    throws AppException,
    InformationalException {/*
                             *
                             * final BizObjAssociation bizObjAssociationObj =
                             * BizObjAssociationFactory.newInstance();
                             *
                             * final BizObjAssociationDtls bizObjAssociationDtls
                             * =
                             * new BizObjAssociationDtls();
                             *
                             * bizObjAssociationDtls.bizObjAssocID =
                             * bizObjAssociationObj.getNewPrimaryKey();
                             * // bizObjAssociationDtls.bizObjectID =
                             * bdmTaskClassificationID;
                             * bizObjAssociationDtls.bizObjectType =
                             * BUSINESSOBJECTTYPE.PERSON;
                             * bizObjAssociationDtls.taskID = taskID;
                             * bizObjAssociationObj.insert(bizObjAssociationDtls
                             * );
                             */

  }

  /**
   *
   * @param citizenWorkspaceUsername
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public ConcernRole getConcernRoleBy(final String citizenWorkspaceUsername)
    throws AppException, InformationalException {

    final CitizenWorkspaceAccountInfo cwAccountInfo =
      this.citizenWorkspaceAccountManager
        .readAccountBy(citizenWorkspaceUsername);
    final ConcernRole userConcernRole = cwAccountInfo.getConcernRole();
    if (userConcernRole == null) {
      final AppException appEx = new AppException(
        CITIZENPORTALEXTERNALPARTYLINK.ERR_NOT_A_LINKED_USER);
      appEx.arg(citizenWorkspaceUsername);
      throw appEx;
    } else {
      return userConcernRole;
    }
  }

}
