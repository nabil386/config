package curam.ca.gc.bdm.rest.bdmcallbackrequestapi.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMBANKACCOUNTCHOICE;
import curam.ca.gc.bdm.codetable.BDMBENEFITPROGRAMTYPE;
import curam.ca.gc.bdm.codetable.BDMCALLSUBJECT;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.codetable.BDMPHONECOUNTRY;
import curam.ca.gc.bdm.codetable.BDMTIMEZONE;
import curam.ca.gc.bdm.entity.fact.BDMRequestForCallBackFactory;
import curam.ca.gc.bdm.entity.struct.BDMRequestForCallBackDtls;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskSkillTypeKey;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskSkillTypeList;
import curam.ca.gc.bdm.facade.communication.fact.BDMCommunicationFactory;
import curam.ca.gc.bdm.facade.communication.struct.BDMCommunicationKey;
import curam.ca.gc.bdm.facade.communication.struct.BDMRecordCommunicationDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.notification.impl.BDMNotificationConstants;
import curam.ca.gc.bdm.pdc.impl.BDMProgramDetailsCalculatorBase;
import curam.ca.gc.bdm.pdc.impl.BDMProgramDetailsCalculatorDefaultImpl;
import curam.ca.gc.bdm.sl.bdmworkallocation.fact.BDMWorkAllocationTaskFactory;
import curam.ca.gc.bdm.sl.bdmworkallocation.intf.BDMWorkAllocationTask;
import curam.ca.gc.bdm.sl.bdmworkallocation.struct.BDMVSGTaskCreateDetails;
import curam.citizenaccount.impl.CitizenAccountHelper;
import curam.citizenworkspace.codetable.impl.OnlineScriptTypeEntry;
import curam.citizenworkspace.message.impl.CITIZENWORKSPACEExceptionCreator;
import curam.citizenworkspace.rest.facade.impl.CitizenScriptInfoHelper;
import curam.citizenworkspace.rest.message.impl.RESTAPIERRORMESSAGESExceptionCreator;
import curam.codetable.ASSIGNEETYPE;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.codetable.COMMUNICATIONDIRECTION;
import curam.codetable.COMMUNICATIONMETHOD;
import curam.codetable.COMMUNICATIONTYPE;
import curam.codetable.CORRESPONDENT;
import curam.codetable.IEGYESNO;
import curam.codetable.SKILLTYPE;
import curam.codetable.TASKPRIORITY;
import curam.core.facade.infrastructure.struct.PersonAndEvidenceTypeList;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.EntityType;
import curam.datastore.impl.NoSuchSchemaException;
import curam.decisionassist.sl.entity.struct.IEGExecutionKey;
import curam.ieg.codetable.impl.IEGSCRIPTSTATUSEntry;
import curam.ieg.definition.impl.IEGScriptIdentifier;
import curam.ieg.entity.fact.IEGExecutionStateFactory;
import curam.ieg.entity.fact.IEGScriptInfoFactory;
import curam.ieg.entity.intf.IEGExecutionState;
import curam.ieg.entity.intf.IEGScriptInfo;
import curam.ieg.entity.struct.IEGExecutionStateDtls;
import curam.ieg.entity.struct.IEGExecutionStateKey;
import curam.ieg.entity.struct.IEGScriptInfoDtls;
import curam.ieg.entity.struct.IEGScriptInfoDtlsList;
import curam.ieg.entity.struct.SearchByScriptIDAndStatusKey;
import curam.ieg.impl.IEGScriptExecution;
import curam.ieg.impl.IEGScriptExecutionFactory;
import curam.ieg.xmlbinding.impl.IEGExecutionBinding;
import curam.intelligentevidencegathering.message.IEG;
import curam.participant.impl.ConcernRole;
import curam.participantmessages.persistence.fact.ParticipantMessageFactory;
import curam.participantmessages.persistence.struct.ParticipantMessageDtls;
import curam.pdc.facade.fact.PDCPersonFactory;
import curam.pdc.facade.intf.PDCPerson;
import curam.pdc.facade.struct.PDCEvidenceDetailsList;
import curam.util.administration.fact.CodeTableAdminFactory;
import curam.util.administration.intf.CodeTableAdmin;
import curam.util.administration.struct.CodeTableItemUniqueKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.LocalisableString;
import curam.util.internal.workflow.fact.ActivityInstanceFactory;
import curam.util.internal.workflow.intf.ActivityInstance;
import curam.util.internal.workflow.struct.ActivityInstanceDtlsList;
import curam.util.internal.workflow.struct.ProcessInstanceKey;
import curam.util.persistence.GuiceWrapper;
import curam.util.persistence.ValidationHelper;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.DeepCloneable;
import curam.util.type.StringHelper;
import curam.util.type.UniqueID;
import curam.util.workflow.fact.BizObjAssociationFactory;
import curam.util.workflow.impl.EnactmentService;
import curam.util.workflow.intf.BizObjAssociation;
import curam.util.workflow.struct.BizObjAssociationDtls;
import curam.workspaceservices.util.impl.DatastoreHelper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import org.apache.commons.codec.binary.Base64;
import org.xml.sax.InputSource;

/***
 * Request for Callback from UA
 * API to accept and process the information available from
 * Request for Callback from UA, resulting in a work ticket/task for an Agent
 * to
 * call the Client back.
 *
 */
public class BDMCallbackRequestAPI extends
  curam.ca.gc.bdm.rest.bdmcallbackrequestapi.base.BDMCallbackRequestAPI {

  @Inject
  private CitizenAccountHelper citizenAccountHelper;

  @Inject
  private CitizenScriptInfoHelper citizenScriptInfoHelper;

  private static int stBufferSize = 1024;

  @Inject
  public Map<String, BDMProgramDetailsCalculatorBase> programDetailsCalculatorMap;

  public BDMCallbackRequestAPI() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Rest API for call back request
   *
   * @return IEGExecutionKey
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public IEGExecutionKey callbackRequest()
    throws AppException, InformationalException {

    // this.citizenAccountSecurity.performDefaultSecurityChecks();
    final String scriptID = "BDMRequestForCallBack";
    final String schemaName = "BDMRequestForCallBack";
    final long datastoreID = createRequestForCallBackDataStore(schemaName);

    final IEGScriptIdentifier scriptIdentifier =
      createIEGScriptIdentifier(scriptID);

    final IEGScriptExecution execution =
      createScriptExecution(datastoreID, schemaName, scriptIdentifier);

    this.citizenScriptInfoHelper.createCitizenScriptInfo(
      execution.getExecutionID(), 0,
      OnlineScriptTypeEntry.REQUEST_FOR_CALLBACK, datastoreID, schemaName,
      TransactionInfo.getProgramUser());

    final IEGExecutionKey executionKey = new IEGExecutionKey();
    executionKey.iegExecutionID = execution.getExecutionID();

    return executionKey;

  }

  /**
   * Setup call back request datastore
   *
   * @param schemaName
   * @return long
   * @throws AppException
   * @throws InformationalException
   */
  private long createRequestForCallBackDataStore(final String schemaName)
    throws InformationalException, AppException {

    final Datastore datastore = DatastoreHelper.openDatastore(schemaName);
    final long datastoreID = DatastoreHelper.createRootEntity(datastore);

    final Entity rootEntity = datastore.readEntity(datastoreID);
    final ConcernRole role =
      this.citizenAccountHelper.getLoggedInConcernRole();
    // this.dsPrepopulator.get().populate(rootEntity, role);

    final Datastore ds = rootEntity.getDatastore();
    final EntityType personType = ds.getEntityType("Person");
    final Entity personEntity = ds.newEntity(personType);
    personEntity.setAttribute("firstName", role.getName());
    personEntity.setAttribute("lastName", role.getName());
    personEntity.setTypedAttribute("personID", role.getID());

    rootEntity.addChildEntity(personEntity);

    return datastoreID;
  }

  /**
   * create call back IEG script identifier
   *
   * @param scriptID
   * @return IEGScriptIdentifier
   * @throws AppException
   * @throws InformationalException
   */
  private IEGScriptIdentifier createIEGScriptIdentifier(final String scriptID)
    throws InformationalException, AppException {

    final String schemaName = "BDMRequestForCallBack";
    if (StringHelper.isEmpty(scriptID))
      ValidationHelper.addValidationError(
        CITIZENWORKSPACEExceptionCreator.ERR_APPEAL_SCRIPT_NOT_DEFINED());
    if (StringHelper.isEmpty(schemaName))
      ValidationHelper.addValidationError(
        CITIZENWORKSPACEExceptionCreator.ERR_APPEAL_SCHEMA_NOT_DEFINED());
    ValidationHelper.failIfErrorsExist();
    final IEGScriptIdentifier scriptIdentifier =
      createScriptIdentifier(scriptID);

    return scriptIdentifier;
  }

  /**
   * create script identifer
   *
   * @param scriptID
   * @return IEGScriptIdentifier
   * @throws AppException
   * @throws InformationalException
   */
  private IEGScriptIdentifier createScriptIdentifier(final String scriptID)
    throws AppException, InformationalException {

    IEGScriptIdentifier scriptIdentifier;
    final IEGScriptInfo iegScriptInfo = IEGScriptInfoFactory.newInstance();
    final SearchByScriptIDAndStatusKey searchByScriptIDAndStatusKey =
      new SearchByScriptIDAndStatusKey();

    searchByScriptIDAndStatusKey.scriptID = scriptID;
    searchByScriptIDAndStatusKey.status =
      IEGSCRIPTSTATUSEntry.ACTIVE.getCode();

    final IEGScriptInfoDtlsList scriptInfoDtlsList =
      iegScriptInfo.searchByScriptIDAndStatus(searchByScriptIDAndStatusKey);

    if (scriptInfoDtlsList.dtls.size() > 0) {

      final IEGScriptInfoDtls scriptInfoDtls =
        scriptInfoDtlsList.dtls.item(0);

      scriptIdentifier = new IEGScriptIdentifier(scriptID,
        scriptInfoDtls.type, scriptInfoDtls.scriptVersion);

    } else {
      scriptIdentifier =
        new IEGScriptIdentifier(scriptID, "REQUEST_FOR_CALLBACK", "V1");
    }
    return scriptIdentifier;
  }

  /**
   * create script execution
   *
   * @param datastoreID
   * @param schemaName
   * @param scriptIdentifier
   * @return IEGScriptExecution
   * @throws AppException
   * @throws InformationalException
   */
  private IEGScriptExecution createScriptExecution(final long datastoreID,
    final String schemaName, final IEGScriptIdentifier scriptIdentifier)
    throws AppException, InformationalException {

    final IEGScriptExecution scriptExecution =
      IEGScriptExecutionFactory.getInstance()
        .createScriptExecutionObject(scriptIdentifier, datastoreID);

    scriptExecution.setSchemaName(schemaName);

    IEGScriptExecutionFactory.getInstance()
      .saveScriptExecutionObject(scriptExecution);

    return scriptExecution;
  }

  /**
   * Rest API to exit call back request
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public IEGExecutionKey
    exitCallBackRequest(final IEGExecutionKey executionKey)
      throws AppException, InformationalException {

    final IEGExecutionKey executionKeyResult = new IEGExecutionKey();
    executionKeyResult.iegExecutionID = executionKey.iegExecutionID;

    try {
      finishRequestForCallBack(executionKey.iegExecutionID);
    } catch (final NoSuchSchemaException e) {
      throw RESTAPIERRORMESSAGESExceptionCreator
        .HTTP_404_APPEAL_SCHEMA_NOT_DEFINED();
    } catch (final InformationalException e) {
      throw RESTAPIERRORMESSAGESExceptionCreator
        .HTTP_400_APPLICATION_ALREADY_IN_PROGRESS(1);

    }

    return executionKeyResult;

  }

  /**
   * Process call back request
   *
   * @throws AppException
   * @throws InformationalException
   */
  private void finishRequestForCallBack(final long iegExecutionID)
    throws AppException, InformationalException, NoSuchSchemaException {

    final ConcernRole role =
      this.citizenAccountHelper.getLoggedInConcernRole();

    final IEGExecutionState executionState =
      IEGExecutionStateFactory.newInstance();
    final IEGExecutionStateKey executionStateKey = new IEGExecutionStateKey();
    executionStateKey.executionID = iegExecutionID;
    final IEGExecutionStateDtls executionStateDtls =
      executionState.read(executionStateKey);

    final String outputString =
      decompressExecutionXML(executionStateDtls.executionXML);
    final IEGScriptExecution executionObj =
      (IEGScriptExecution) IEGExecutionBinding.getInstance()
        .getUnMarshalledExecutionObject(
          new InputSource(new StringReader(outputString)));

    final long dataStoreID = executionObj.getRootEntityID();

    final Datastore datastore = DatastoreFactory.newInstance()
      .openDatastore(executionStateDtls.scriptID);
    final Entity rootEntity = datastore.readEntity(dataStoreID);

    final Entity personEntity =
      rootEntity.getChildEntities(datastore.getEntityType("Person"))[0];

    final Entity callBackDetailsEntity = rootEntity
      .getChildEntities(datastore.getEntityType("CallBackDetails"))[0];

    final Entity[] benefitDetailsEntityList = callBackDetailsEntity
      .getChildEntities(datastore.getEntityType("BenefitDetails"));

    final Entity[] phoneNumberEntityList =
      personEntity.getChildEntities(datastore.getEntityType("PhoneNumber"));

    final Entity[] emailEntityList =
      personEntity.getChildEntities(datastore.getEntityType("EmailAddress"));

    final String callBackSubjectCode =
      callBackDetailsEntity.getAttribute("callBackSubject");
    final String callBackSubject =
      BDMUtil.getCodeTableDescriptionForUserLocale(BDMCALLSUBJECT.TABLENAME,
        callBackSubjectCode);

    final String programType =
      callBackDetailsEntity.getAttribute("programType");
    final String programName = BDMUtil.getCodeTableDescriptionForUserLocale(
      BDMBENEFITPROGRAMTYPE.TABLENAME, programType);
    String benefitNames = "";
    for (final Entity entity : benefitDetailsEntityList) {
      if (Boolean.valueOf(entity.getAttribute("benefitSelected"))) {
        if (!benefitNames.isEmpty()) {
          benefitNames = benefitNames + BDMConstants.kcomma;
        }
        benefitNames = benefitNames + entity.getAttribute("benefitName");
      }
    }
    String existingPhoneNumber = "";
    for (final Entity entity : phoneNumberEntityList) {
      // entity.getEntityType().
      if (Boolean.valueOf(entity.getAttribute("phoneSelected"))) {
        existingPhoneNumber = entity.getAttribute("existingPhoneNumber");
        break;
      }
    }

    String phoneNumber = "", newPhoneNumber = "";
    boolean newPhoneNumberExistInd = false;
    if (BDMBANKACCOUNTCHOICE.USE_EXISTING
      .equals(personEntity.getAttribute("existingOrAddNewPhone"))) {
      phoneNumber = existingPhoneNumber;
    } else if (BDMBANKACCOUNTCHOICE.ADD_NEW
      .equals(personEntity.getAttribute("existingOrAddNewPhone"))
      || personEntity.getAttribute("existingOrAddNewPhone").isEmpty()) {
      phoneNumber =
        assembleFullPhoneNumber(personEntity.getAttribute("newcountryCode"),
          personEntity.getAttribute("newareaCode"),
          personEntity.getAttribute("newphoneNumber"));
      newPhoneNumberExistInd = true;
      newPhoneNumber = phoneNumber;

    }

    String altPhone = "";

    if (IEGYESNO.YES.equals(personEntity.getAttribute("altPrefPhone"))) {
      altPhone =
        assembleFullPhoneNumber(personEntity.getAttribute("altcountryCode"),
          personEntity.getAttribute("altareaCode"),
          personEntity.getAttribute("altphoneNumber"));
    }

    String bestTimeTocall = "";

    if (Boolean.valueOf(personEntity.getAttribute("isMor"))) {
      bestTimeTocall = "Morning";
    }

    if (Boolean.valueOf(personEntity.getAttribute("isAftr"))) {
      if (!bestTimeTocall.isEmpty())
        bestTimeTocall = bestTimeTocall + CuramConst.gkComma + "Afternoon";
      else
        bestTimeTocall = "Afternoon";
    }

    if (Boolean.valueOf(personEntity.getAttribute("isEve"))) {
      if (!bestTimeTocall.isEmpty())
        bestTimeTocall = bestTimeTocall + CuramConst.gkComma + "Evening";
      else
        bestTimeTocall = "Evening";
    }

    String email = "", newEmail = CuramConst.gkEmpty;
    for (final Entity entity : emailEntityList) {
      if (Boolean.valueOf(entity.getAttribute("emailSelected"))) {
        email = entity.getAttribute("existingEmailAdr");
        break;
      }
    }

    if (!personEntity.getAttribute("altemailAdr").isEmpty()) {
      email = personEntity.getAttribute("altemailAdr");
      newEmail = personEntity.getAttribute("altemailAdr");
    }

    final String callBackAdditionalDetails =
      callBackDetailsEntity.getAttribute("callBackAdditionalDetails");
    final String timeZone = BDMUtil.getCodeTableDescriptionForUserLocale(
      BDMTIMEZONE.TABLENAME, personEntity.getAttribute("timeZone"));
    final String accessibilityDetails =
      personEntity.getAttribute("accessibilityDetails");

    // Create Task for the callbackrequest

    final long kProcessInstanceID = this.createCallBackRequestTask(
      callBackSubjectCode, role, programType, benefitNames);

    long taskID = 0;
    // Get taskID using processInstanceID
    if (kProcessInstanceID != 0) {
      taskID = this.getTaskIDForProcessInstanceID(kProcessInstanceID);
    }

    /*
     * Text: Following data from call back request will be mapped to Text field:
     *
     * Program selected
     *
     * Benefit (if selected)
     *
     * Request Details
     *
     * Phone number
     *
     * Alternate Phone number
     *
     * Best time to call
     *
     * Time Zone
     *
     * Email (if entered)
     *
     * Accessibility Requirements (if added)
     */
    final StringBuffer communicationText = new StringBuffer();
    communicationText.append(programName);
    if (!benefitNames.isEmpty()) {
      communicationText.append(CuramConst.gkNewLine).append(benefitNames);
    }
    communicationText.append(CuramConst.gkNewLine)
      .append(callBackAdditionalDetails).append(CuramConst.gkNewLine)
      .append(phoneNumber);
    if (!altPhone.isEmpty()) {
      communicationText.append(CuramConst.gkNewLine).append(altPhone);
    }
    if (!bestTimeTocall.isEmpty()) {
      communicationText.append(CuramConst.gkNewLine).append(bestTimeTocall);
    }
    communicationText.append(CuramConst.gkNewLine).append(timeZone);
    if (!email.isEmpty()) {
      communicationText.append(CuramConst.gkNewLine).append(email);
    }
    if (!accessibilityDetails.isEmpty()) {
      communicationText.append(CuramConst.gkNewLine)
        .append(accessibilityDetails);
    }

    // Create communication record
    final BDMRecordCommunicationDetails recordCommunicationDetails =
      new BDMRecordCommunicationDetails();

    recordCommunicationDetails.dtls.recordedCommDetails.correspondentParticipantRoleID =
      recordCommunicationDetails.dtls.recordedCommDetails.correspondentConcernRoleID =
        recordCommunicationDetails.dtls.recordedCommDetails.clientParticipantRoleID =
          role.getID();
    recordCommunicationDetails.dtls.recordedCommDetails.correspondentName =
      role.getName();
    recordCommunicationDetails.dtls.recordedCommDetails.correspondentConcernRoleType =
      role.getConcernRoleType().getCode();
    // Subject: <Task ID> <Program> <Subject> Call back request
    recordCommunicationDetails.dtls.recordedCommDetails.subject =
      taskID + CuramConst.gkSpace + programName + CuramConst.gkSpace
        + callBackSubject + CuramConst.gkSpace + "Call Back Request";

    recordCommunicationDetails.dtls.recordedCommDetails.communicationText =
      communicationText.toString();
    recordCommunicationDetails.dtls.recordedCommDetails.communicationDirection =
      COMMUNICATIONDIRECTION.INCOMING;
    recordCommunicationDetails.dtls.recordedCommDetails.methodTypeCode =
      COMMUNICATIONMETHOD.DIGITAL;
    recordCommunicationDetails.dtls.recordedCommDetails.communicationDate =
      Date.getCurrentDate();
    recordCommunicationDetails.dtls.recordedCommDetails.communicationTypeCode =
      COMMUNICATIONTYPE.REQUESTCALLBACK;
    recordCommunicationDetails.dtls.recordedCommDetails.correspondentType =
      CORRESPONDENT.CLIENT;

    // Task 62602 2022-07-14
    // Set email. This will create new email evidence
    if (!StringUtil.isNullOrEmpty(newEmail)) {
      recordCommunicationDetails.dtls.recordedCommDetails.newEmailAddress =
        newEmail;
    }
    // Set Phone. This will create new phone evidence
    if (newPhoneNumberExistInd) {
      recordCommunicationDetails.dtls.recordedCommDetails.phoneNumber =
        newPhoneNumber;
      recordCommunicationDetails.dtls.recordedCommDetails.phoneCountryCode =
        personEntity.getAttribute("newcountryCode");
      recordCommunicationDetails.phoneCountryCode =
        personEntity.getAttribute("newcountryCode");
      recordCommunicationDetails.dtls.recordedCommDetails.phoneAreaCode =
        personEntity.getAttribute("newareaCode");
      recordCommunicationDetails.dtls.recordedCommDetails.phoneNumber =
        personEntity.getAttribute("newphoneNumber");
    }

    final BDMCommunicationKey bdmCommunicationKey = BDMCommunicationFactory
      .newInstance().createRecordedComm1(recordCommunicationDetails);

    final BDMRequestForCallBackDtls requestForCallBackDtls =
      new BDMRequestForCallBackDtls();
    requestForCallBackDtls.requestForCallBackID = UniqueID.nextUniqueID();
    requestForCallBackDtls.participantRoleID = role.getID();
    requestForCallBackDtls.processInstanceID = kProcessInstanceID;
    requestForCallBackDtls.dataStoreID = dataStoreID;
    requestForCallBackDtls.programType = programType;

    BDMRequestForCallBackFactory.newInstance().insert(requestForCallBackDtls);

    this.createParticipantMessages(role,
      requestForCallBackDtls.requestForCallBackID);

    if (taskID != 0) {
      // Create bizObjectAssociation for linking communication with task
      this.createBizObjectAssociationWithCommunication(taskID,
        bdmCommunicationKey.communicationID);
    }

  }

  /**
   * Helper method to assemble full phone number
   *
   * @param countryCode
   * @param areaCode
   * @param phoneNumber
   * @return String
   * @throws AppException
   * @throws InformationalException
   */
  private String assembleFullPhoneNumber(final String countryCode,
    final String areaCode, final String phoneNumber)
    throws AppException, InformationalException {

    final String phoneAreaCodeAndNumber = areaCode + phoneNumber;
    final CodeTableAdmin codeTableAdminObj =
      CodeTableAdminFactory.newInstance();
    final CodeTableItemUniqueKey codeTableItemUniqueKey =
      new CodeTableItemUniqueKey();
    codeTableItemUniqueKey.code = countryCode;
    codeTableItemUniqueKey.locale = TransactionInfo.getProgramLocale();
    codeTableItemUniqueKey.tableName = BDMPHONECOUNTRY.TABLENAME;
    final StringBuffer fullPhoneNumber = new StringBuffer();
    fullPhoneNumber
      .append(codeTableAdminObj
        .readCTIDetailsForLocaleOrLanguage(codeTableItemUniqueKey).annotation
          .trim())
      .append(BDMConstants.kStringSpace).append(phoneAreaCodeAndNumber
        .replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3"))
      .append(BDMConstants.kStringSpace);
    return fullPhoneNumber.toString();
  }

  /**
   * Helper method to create participant message - notification
   *
   * @param role
   * @param requestForCallBackID
   * @throws AppException
   * @throws InformationalException
   */
  private void createParticipantMessages(final ConcernRole role,
    final long requestForCallBackID)
    throws AppException, InformationalException {

    final ParticipantMessageDtls participantMessageDtls =
      new ParticipantMessageDtls();

    participantMessageDtls.participantMessageID = UniqueID.nextUniqueID();
    participantMessageDtls.concernRoleID = role.getID();
    participantMessageDtls.createdDateTime = DateTime.getCurrentDateTime();
    participantMessageDtls.effectiveDateTime = DateTime.getCurrentDateTime();
    participantMessageDtls.messageType =
      curam.participantmessages.codetable.ParticipantMessageType.APPLICATION;
    participantMessageDtls.relatedID = requestForCallBackID;
    participantMessageDtls.propertyFileName =
      BDMNotificationConstants.kBDMNotificationPropertyFile;
    participantMessageDtls.titleProperty =
      BDMNotificationConstants.kCallBackRequestTitle;
    participantMessageDtls.messageProperty =
      BDMNotificationConstants.kCallBackRequestMessageBody;
    participantMessageDtls.expiryDateTime =
      DateTime.getCurrentDateTime().addTime(48, 0, 0);

    ParticipantMessageFactory.newInstance().insert(participantMessageDtls);

  }

  /**
   * Rest API for call back request
   *
   * @param compressedString
   * @return String
   * @throws AppException
   * @throws InformationalException
   */
  private String decompressExecutionXML(final String compressedString)
    throws AppException {

    if (compressedString.startsWith("<"))
      return compressedString;

    byte[] bytesToDecompress = null;

    bytesToDecompress = Base64.decodeBase64(compressedString.getBytes());

    final Inflater decompresser = new Inflater();
    decompresser.setInput(bytesToDecompress, 0, bytesToDecompress.length);
    final byte[] result = new byte[stBufferSize];
    final ByteArrayOutputStream bos =
      new ByteArrayOutputStream(bytesToDecompress.length);

    int offset = 0;
    while (!decompresser.finished()) {
      try {
        final int count = decompresser.inflate(result);
        bos.write(result, 0, count);
        offset += count;
      } catch (final DataFormatException e) {
        throw new AppException(IEG.ID_SCRIPT_EXECUTION_NOT_READABLE, e);
      }
    }
    decompresser.end();
    final byte[] decompressedData = bos.toByteArray();
    try {
      bos.close();
    } catch (final IOException e) {
      throw new AppException(IEG.ID_SCRIPT_EXECUTION_NOT_READABLE, e);

    }
    String outputString = null;
    try {
      outputString =
        new String(decompressedData, 0, decompressedData.length, "UTF-8");

    } catch (final UnsupportedEncodingException e) {
      throw new AppException(IEG.ID_SCRIPT_EXECUTION_NOT_READABLE, e);
    }
    return outputString;
  }

  /**
   * Helper method to enact call back request WF
   *
   * @param callBackSubjectCode
   * @param role
   * @param programType
   * @param benefitNames
   * @return long
   * @throws AppException
   * @throws InformationalException
   */
  private long createCallBackRequestTask(final String callBackSubjectCode,
    final ConcernRole role, final String programType,
    final String benefitNames) throws AppException, InformationalException {

    final List<DeepCloneable> enactmentStructs = new ArrayList<>();
    final BDMVSGTaskCreateDetails bdmVSGTaskCreateDetails =
      new BDMVSGTaskCreateDetails();
    bdmVSGTaskCreateDetails.participantRoleID = role.getID();
    bdmVSGTaskCreateDetails.participantType = ASSIGNEETYPE.PERSON;
    bdmVSGTaskCreateDetails.priority = TASKPRIORITY.NORMAL;

    final BDMWorkAllocationTask bdmWorkAllocationTaskObj =
      BDMWorkAllocationTaskFactory.newInstance();

    BDMTaskSkillTypeKey bdmTaskSkillTypeKey = new BDMTaskSkillTypeKey();
    bdmTaskSkillTypeKey = getSkillLevelByRequestCallBackSubject(
      callBackSubjectCode, programType, benefitNames);

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
    personAndEvidenceTypeList.concernRoleID = role.getID();
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
    notificationMsg.arg(role.getName());
    notificationMsg.arg(role.getPrimaryAlternateID());
    // bdmVSGTaskCreateDetails.subject = notificationMsg.toString();
    bdmVSGTaskCreateDetails.subject =
      notificationMsg.getMessage(TransactionInfo.getProgramLocale());

    enactmentStructs.add(bdmVSGTaskCreateDetails);

    String workflowDisabled =
      Configuration.getProperty(EnvVars.ENV_ENACT_WORKFLOW_PROCESS_DISABLED);

    if (workflowDisabled == null) {
      workflowDisabled = "NO";
    }

    long kProcessInstanceID = 0;
    final String kTaskDefinitionID = "BDMRequestForCallBack";

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

  /**
   * Helper method to get biz object association with communication
   *
   * @param taskID
   * @param recordCommunicationID
   * @throws AppException
   * @throws InformationalException
   */
  protected void createBizObjectAssociationWithCommunication(
    final long taskID, final long recordCommunicationID)
    throws AppException, InformationalException {

    final BizObjAssociation bizObjAssociationObj =
      BizObjAssociationFactory.newInstance();

    final BizObjAssociationDtls bizObjAssociationDtls =
      new BizObjAssociationDtls();

    bizObjAssociationDtls.bizObjAssocID =
      bizObjAssociationObj.getNewPrimaryKey();
    bizObjAssociationDtls.bizObjectID = recordCommunicationID;
    bizObjAssociationDtls.bizObjectType = BUSINESSOBJECTTYPE.BDMCOMMUNICATION;
    bizObjAssociationDtls.taskID = taskID;
    bizObjAssociationObj.insert(bizObjAssociationDtls);

  }

  /**
   * get skill level by request call back subject
   *
   * @param callBackSubjectCode
   * @param programType
   * @param benefitNames
   * @return BDMTaskSkillTypeKey
   * @throws AppException
   * @throws InformationalException
   */
  private BDMTaskSkillTypeKey getSkillLevelByRequestCallBackSubject(
    final String callBackSubjectCode, final String programType,
    final String benefitNames) throws AppException, InformationalException {

    // Provided Porgram type implementation for the skill type calculation
    final BDMProgramDetailsCalculatorBase progrmDetailsCalculator =
      programDetailsCalculatorMap.get(programType);
    if (progrmDetailsCalculator != null) {
      return progrmDetailsCalculator
        .getSkillTypeBasedOnProgram(callBackSubjectCode, benefitNames);
    } else {
      return new BDMProgramDetailsCalculatorDefaultImpl()
        .getSkillTypeBasedOnProgram(callBackSubjectCode, benefitNames);
    }
  }

}
