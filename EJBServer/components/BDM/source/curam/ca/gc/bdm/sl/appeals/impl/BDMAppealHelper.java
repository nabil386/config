package curam.ca.gc.bdm.sl.appeals.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.entity.fact.BDMOnlineAppealRequestFactory;
import curam.ca.gc.bdm.entity.struct.BDMOnlineAppealRequestDtls;
import curam.ca.gc.bdm.entity.struct.BDMOnlineAppealRequestKey;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskSkillTypeKey;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskSkillTypeList;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.rest.struct.BDMApplication;
import curam.ca.gc.bdm.rest.struct.BDMRFROnlineRequest;
import curam.ca.gc.bdm.sl.bdmworkallocation.fact.BDMWorkAllocationTaskFactory;
import curam.ca.gc.bdm.sl.bdmworkallocation.struct.BDMDocumentTypeKey;
import curam.ca.gc.bdm.sl.bdmworkallocation.struct.BDMVSGTaskCreateDetails;
import curam.citizenaccount.impl.CitizenAccountHelper;
import curam.citizenworkspace.appeals.impl.AppealDatastorePrepopulator;
import curam.citizenworkspace.codetable.impl.IntakeClientTypeEntry;
import curam.citizenworkspace.codetable.impl.OnlineScriptTypeEntry;
import curam.citizenworkspace.facade.struct.SetupAppealResult;
import curam.citizenworkspace.message.impl.CITIZENWORKSPACEExceptionCreator;
import curam.citizenworkspace.rest.facade.fact.ApplicationAPIFactory;
import curam.citizenworkspace.rest.facade.impl.CitizenScriptInfoHelper;
import curam.citizenworkspace.rest.facade.intf.ApplicationAPI;
import curam.citizenworkspace.rest.facade.struct.UASubmittedApplicationList;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccountDAO;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccountManager;
import curam.codetable.APPEALTYPE;
import curam.codetable.DOCUMENTTYPE;
import curam.codetable.PRODUCTCATEGORY;
import curam.codetable.SKILLTYPE;
import curam.codetable.TASKPRIORITY;
import curam.commonintake.authorisation.entity.fact.ProgramAuthorisationDataFactory;
import curam.commonintake.authorisation.entity.struct.ProgramAuthorisationDataDtlsList;
import curam.commonintake.authorisation.entity.struct.SearchByProgramKey;
import curam.commonintake.entity.fact.ApplicationCaseFactory;
import curam.commonintake.entity.struct.ApplicationCaseDtls;
import curam.commonintake.entity.struct.ApplicationCaseKey;
import curam.core.facade.infrastructure.struct.PersonAndEvidenceTypeList;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.onlineappealrequest.impl.OnlineAppealRequest;
import curam.core.onlineappealrequest.impl.OnlineAppealRequestDAO;
import curam.core.sl.struct.CaseIDKey;
import curam.core.sl.struct.TaskCreateDetails;
import curam.core.struct.CaseHeaderKey;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.ieg.codetable.impl.IEGSCRIPTSTATUSEntry;
import curam.ieg.definition.impl.IEGScriptIdentifier;
import curam.ieg.entity.fact.IEGScriptInfoFactory;
import curam.ieg.entity.intf.IEGScriptInfo;
import curam.ieg.entity.struct.IEGScriptInfoDtls;
import curam.ieg.entity.struct.IEGScriptInfoDtlsList;
import curam.ieg.entity.struct.SearchByScriptIDAndStatusKey;
import curam.ieg.impl.IEGScriptExecution;
import curam.ieg.impl.IEGScriptExecutionFactory;
import curam.participant.impl.ConcernRole;
import curam.participant.impl.ConcernRoleDAO;
import curam.participant.person.impl.Person;
import curam.participant.person.impl.PersonDAO;
import curam.pdc.facade.fact.PDCPersonFactory;
import curam.pdc.facade.intf.PDCPerson;
import curam.pdc.facade.struct.PDCEvidenceDetailsList;
import curam.struct.ApplicationIDKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.ValidationHelper;
import curam.util.resources.Configuration;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.Date;
import curam.util.type.StringHelper;
import curam.util.type.UniqueID;
import curam.util.workflow.impl.EnactmentService;
import curam.workspaceservices.codetable.IntakeApplicationStatus;
import curam.workspaceservices.codetable.IntakeProgramApplicationStatus;
import curam.workspaceservices.intake.fact.IntakeAppConcernRoleLinkFactory;
import curam.workspaceservices.intake.fact.IntakeProgramAppCaseLinkFactory;
import curam.workspaceservices.intake.fact.IntakeProgramApplicationFactory;
import curam.workspaceservices.intake.fact.ProgramTypeFactory;
import curam.workspaceservices.intake.impl.IntakeApplication;
import curam.workspaceservices.intake.impl.IntakeApplicationDAO;
import curam.workspaceservices.intake.intf.IntakeAppConcernRoleLink;
import curam.workspaceservices.intake.struct.ConcernRoleIDAndNameDetailsList;
import curam.workspaceservices.intake.struct.IntakeProgramAppCaseLinkDtls;
import curam.workspaceservices.intake.struct.IntakeProgramAppCaseLinkDtlsList;
import curam.workspaceservices.intake.struct.IntakeProgramApplicationDtls;
import curam.workspaceservices.intake.struct.IntakeProgramApplicationKey;
import curam.workspaceservices.intake.struct.ProgramTypeDtls;
import curam.workspaceservices.intake.struct.ProgramTypeKey;
import curam.workspaceservices.intake.struct.SearchByUsernameKey;
import curam.workspaceservices.localization.fact.TextTranslationFactory;
import curam.workspaceservices.localization.intf.TextTranslation;
import curam.workspaceservices.localization.struct.ReadByLocalizableTextIDandLocaleKey;
import curam.workspaceservices.localization.struct.TextTranslationDtls;
import curam.workspaceservices.util.impl.DatastoreHelper;
import curam.workspaceservices.workflow.struct.ProcessOnlineAppealDetails;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BDMAppealHelper {

  @Inject
  private CitizenWorkspaceAccountManager citizenWorkspaceAccountManager;

  @Inject
  private Provider<AppealDatastorePrepopulator> dsPrepopulator;

  @Inject
  private CitizenAccountHelper citizenAccountHelper;

  @Inject
  private OnlineAppealRequestDAO onlineAppealRequestDAO;

  @Inject
  private CitizenScriptInfoHelper citizenScriptInfoHelper;

  @Inject
  private IntakeApplicationDAO intakeApplicationDAO;

  @Inject
  private CitizenWorkspaceAccountDAO citizenWorkspaceAccountDAO;

  @Inject
  private ConcernRoleDAO concernRoleDAO;

  @Inject
  private PersonDAO personDAO;

  public SetupAppealResult
    startAppeal(final ApplicationIDKey applicationIDKey)
      throws AppException, InformationalException {

    final SetupAppealResult result = new SetupAppealResult();

    final OnlineAppealRequest onlineAppealRequest =
      createOnlineAppealRequest(applicationIDKey.applicationID);

    final String scriptID =
      Configuration.getProperty(EnvVars.ENV_CW_APPEALS_SCRIPT_ID);

    final String schemaName =
      Configuration.getProperty(EnvVars.ENV_CW_APPEALS_DATASTORE_SCHEMA);

    final long datastoreID =
      createAppealsDataStore(schemaName, applicationIDKey.applicationID);

    final IEGScriptIdentifier scriptIdentifier =
      createIEGScriptIdentifier(scriptID);

    final IEGScriptExecution execution =
      createScriptExecution(datastoreID, schemaName, scriptIdentifier);

    this.citizenScriptInfoHelper.createCitizenScriptInfo(
      execution.getExecutionID(), onlineAppealRequest.getID().longValue(),
      OnlineScriptTypeEntry.APPEAL, datastoreID, schemaName,

      TransactionInfo.getProgramUser());

    final OnlineAppealRequest readOnlineAppeal =
      this.onlineAppealRequestDAO.get(onlineAppealRequest.getID());

    final Datastore datastore = DatastoreHelper.openDatastore(schemaName);
    final Entity rootEntity = datastore.readEntity(datastoreID);

    readOnlineAppeal.start(rootEntity, readOnlineAppeal.getVersionNo(),
      execution.getExecutionID());

    result.iegExecutionID = execution.getExecutionID();
    return result;
  }

  private OnlineAppealRequest
    createOnlineAppealRequest(final long applicationCaseID)
      throws InformationalException, AppException {

    final ConcernRole person = this.getConcernRoleForUser();
    final OnlineAppealRequest onlineAppealRequest =
      this.onlineAppealRequestDAO.newInstance();
    onlineAppealRequest.setCreatedBy(person);
    onlineAppealRequest.insert();

    this.createBDMOnlineAppealRequest(onlineAppealRequest.getID(),
      applicationCaseID);

    return onlineAppealRequest;
  }

  private long createAppealsDataStore(final String schemaName,
    final long applicationCaseID)
    throws InformationalException, AppException {

    final Datastore datastore = DatastoreHelper.openDatastore(schemaName);
    final long datastoreID = DatastoreHelper.createRootEntity(datastore);

    final Entity rootEntity = datastore.readEntity(datastoreID);

    final ConcernRole role = this.getConcernRoleForUser();
    this.dsPrepopulator.get().populate(rootEntity, role);

    final Entity[] people =
      rootEntity.getChildEntities(datastore.getEntityType("Person"));

    final Entity metadataEntity = datastore.newEntity("Metadata");
    metadataEntity.setTypedAttribute("hasMultipleCandidates",
      Boolean.valueOf(people.length > 1));
    rootEntity.addChildEntity(metadataEntity);

    this.addProgramDetails(rootEntity, applicationCaseID, datastore);

    return datastoreID;
  }

  private void addProgramDetails(final Entity rootEntity,
    final long applicationCaseID, final Datastore datastore)
    throws InformationalException, AppException {

    final ApplicationCaseKey applicationCaseKey = new ApplicationCaseKey();
    applicationCaseKey.applicationCaseID = applicationCaseID;
    final ApplicationCaseDtls applicationCaseDtls =
      ApplicationCaseFactory.newInstance().read(applicationCaseKey);

    final Entity applicationDetailsEntity =
      datastore.newEntity(datastore.getEntityType("ApplicationDetails"));
    applicationDetailsEntity.setTypedAttribute("applicationCaseID",
      applicationCaseID);
    applicationDetailsEntity.setTypedAttribute("applicationReferenceNumber",
      applicationCaseDtls.applicationReference);
    final int numOfDaysForLateRequest =
      Configuration.getIntProperty(EnvVars.BDM_ENV_RFR_LATE_REQUEST_DEADLINE);
    applicationDetailsEntity.setTypedAttribute("numOfDaysForLateRequest",
      numOfDaysForLateRequest);
    rootEntity.addChildEntity(applicationDetailsEntity);

    final CaseIDKey caseIDKey = new CaseIDKey();
    caseIDKey.caseID = applicationCaseID;

    final IntakeProgramAppCaseLinkDtlsList intakeProgramAppCaseLinkDtlsList =
      IntakeProgramAppCaseLinkFactory.newInstance().searchByCaseID(caseIDKey);

    for (int j = 0; j < intakeProgramAppCaseLinkDtlsList.dtls.size(); j++) {

      final Entity benefitDetails = datastore.newEntity("BenefitDetails");
      // benefitDetails.setTypedAttribute("name", applicationCaseID);

      applicationDetailsEntity.addChildEntity(benefitDetails);

      final IntakeProgramAppCaseLinkDtls intakeProgramAppCaseLinkDtls =
        intakeProgramAppCaseLinkDtlsList.dtls.item(j);

      final IntakeProgramApplicationKey intakeProgramApplicationKey =
        new IntakeProgramApplicationKey();
      intakeProgramApplicationKey.intakeProgramApplicationID =
        intakeProgramAppCaseLinkDtls.intakeProgramApplicationID;

      final IntakeProgramApplicationDtls intakeProgramApplicationDtls =
        IntakeProgramApplicationFactory.newInstance()
          .read(intakeProgramApplicationKey);

      final ProgramTypeKey programTypeKey = new ProgramTypeKey();
      programTypeKey.programTypeID =
        intakeProgramApplicationDtls.programTypeID;
      final ProgramTypeDtls programTypeDtls =
        ProgramTypeFactory.newInstance().read(programTypeKey);

      applicationDetailsEntity.setTypedAttribute("program",
        CodeTable.getOneItem(PRODUCTCATEGORY.TABLENAME,
          programTypeDtls.integratedCaseType,
          TransactionInfo.getProgramLocale()));

      final ReadByLocalizableTextIDandLocaleKey paramReadByLocalizableTextIDandLocaleKey =
        new ReadByLocalizableTextIDandLocaleKey();
      paramReadByLocalizableTextIDandLocaleKey.localeCode =
        TransactionInfo.getProgramLocale();

      paramReadByLocalizableTextIDandLocaleKey.localizableTextID =
        programTypeDtls.nameTextID;

      final TextTranslation textTranslationObj =
        TextTranslationFactory.newInstance();

      final TextTranslationDtls textTranslationDtls =
        textTranslationObj.readByLocalizableTextIDandLocale(
          paramReadByLocalizableTextIDandLocaleKey);

      final StringBuffer stringBuffer = new StringBuffer();
      stringBuffer.append(textTranslationDtls.text);

      if (IntakeProgramApplicationStatus.APPROVED
        .equals(intakeProgramApplicationDtls.status)) {

        final SearchByProgramKey searchByProgramKey =
          new SearchByProgramKey();
        searchByProgramKey.programID =
          intakeProgramApplicationDtls.intakeProgramApplicationID;
        final ProgramAuthorisationDataDtlsList programAuthorisationDataDtlsList =
          ProgramAuthorisationDataFactory.newInstance()
            .searchByProgramID(searchByProgramKey);
        final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
        if (programAuthorisationDataDtlsList.dtls.size() > 0) {
          caseHeaderKey.caseID = programAuthorisationDataDtlsList.dtls
            .get(0).productDeliveryCaseID;
          benefitDetails.setTypedAttribute("referenceNumber",
            curam.core.fact.CaseHeaderFactory.newInstance()
              .read(caseHeaderKey).caseReference);
          stringBuffer.append(BDMConstants.kStringSpace)
            .append(CuramConst.gkRoundOpeningBracket)
            .append(benefitDetails.getTypedAttribute("referenceNumber"))
            .append(CuramConst.gkRoundClosingBracket);
        }
      } else {
        benefitDetails.setTypedAttribute("referenceNumber",
          applicationCaseDtls.applicationReference);
        stringBuffer.append(BDMConstants.kStringSpace)
          .append(CuramConst.gkRoundOpeningBracket)
          .append(benefitDetails.getTypedAttribute("referenceNumber"))
          .append(CuramConst.gkRoundClosingBracket);
      }
      benefitDetails.setTypedAttribute("name", stringBuffer.toString());
      benefitDetails.update();
      applicationDetailsEntity.update();

    }

  }

  private IEGScriptIdentifier createIEGScriptIdentifier(final String scriptID)
    throws InformationalException, AppException {

    final String schemaName =
      Configuration.getProperty(EnvVars.ENV_CW_APPEALS_DATASTORE_SCHEMA);
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
      scriptIdentifier = new IEGScriptIdentifier(scriptID, "Appeal", "V1");
    }
    return scriptIdentifier;
  }

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

  public long getIntakeProgramAppByAppCaseID(final long applicationCaseID)
    throws AppException, InformationalException {

    long intakeApplicationID = 0;

    final CaseIDKey caseIDKey = new CaseIDKey();
    caseIDKey.caseID = applicationCaseID;
    final IntakeProgramAppCaseLinkDtlsList intakeProgramAppCaseLinkDtlsList =
      IntakeProgramAppCaseLinkFactory.newInstance().searchByCaseID(caseIDKey);

    if (intakeProgramAppCaseLinkDtlsList.dtls.size() > 0) {

      final IntakeProgramApplicationKey intakeProgramApplicationKey =
        new IntakeProgramApplicationKey();
      intakeProgramApplicationKey.intakeProgramApplicationID =
        intakeProgramAppCaseLinkDtlsList.dtls
          .item(0).intakeProgramApplicationID;

      final IntakeProgramApplicationDtls intakeProgramApplicationDtls =
        IntakeProgramApplicationFactory.newInstance()
          .read(intakeProgramApplicationKey);

      intakeApplicationID = intakeProgramApplicationDtls.intakeApplicationID;

    }

    return intakeApplicationID;
  }

  public BDMApplication getMyAppDetails(final long applicationCaseID)
    throws AppException, InformationalException {

    final BDMApplication bdmApplication = new BDMApplication();
    bdmApplication.applicationID = applicationCaseID;

    final CaseIDKey caseIDKey = new CaseIDKey();
    caseIDKey.caseID = applicationCaseID;

    final IntakeProgramAppCaseLinkDtlsList intakeProgramAppCaseLinkDtlsList =
      IntakeProgramAppCaseLinkFactory.newInstance().searchByCaseID(caseIDKey);

    final HashMap<Long, Long> keyValue = new HashMap<>();

    for (int j = 0; j < intakeProgramAppCaseLinkDtlsList.dtls.size(); j++) {

      final IntakeProgramAppCaseLinkDtls dtls =
        intakeProgramAppCaseLinkDtlsList.dtls.item(j);

      if (!keyValue.containsKey(dtls.caseID)) {

        keyValue.put(dtls.caseID, dtls.intakeProgramApplicationID);

        final IntakeProgramApplicationKey intakeProgramApplicationKey =
          new IntakeProgramApplicationKey();
        intakeProgramApplicationKey.intakeProgramApplicationID =
          dtls.intakeProgramApplicationID;

        final IntakeProgramApplicationDtls intakeProgramApplicationDtls =
          IntakeProgramApplicationFactory.newInstance()
            .read(intakeProgramApplicationKey);

        final IntakeApplication intakeApplication = intakeApplicationDAO
          .get(intakeProgramApplicationDtls.intakeApplicationID);

        bdmApplication.intakeApplicationID = intakeApplication.getID();
        bdmApplication.applicationName =
          intakeApplication.getIntakeApplicationType().name().getValue();

        final ProgramTypeKey programTypeKey = new ProgramTypeKey();
        programTypeKey.programTypeID =
          intakeProgramApplicationDtls.programTypeID;
        final ProgramTypeDtls programTypeDtls =
          ProgramTypeFactory.newInstance().read(programTypeKey);

        bdmApplication.programName = programTypeDtls.integratedCaseType;
        bdmApplication.referenceNumber = intakeApplication.getReference();

      }

    }

    return bdmApplication;

  }

  private BDMOnlineAppealRequestDtls createBDMOnlineAppealRequest(
    final long appealRequestID, final long applicationCaseID)
    throws AppException, InformationalException {

    final BDMApplication application =
      this.getMyAppDetails(applicationCaseID);

    final BDMOnlineAppealRequestDtls dtls = new BDMOnlineAppealRequestDtls();
    dtls.applicationCaseID = applicationCaseID;
    dtls.onlineAppealRequestID = appealRequestID;
    dtls.intakeApplicationID = application.intakeApplicationID;
    dtls.programCode = application.programName;

    BDMOnlineAppealRequestFactory.newInstance().insert(dtls);

    return dtls;
  }

  /**
   * Returns the ConcernRole of the logged in User.
   */
  public ConcernRole getConcernRoleForUser()
    throws AppException, InformationalException {

    ConcernRole concernRole = null;
    final String userName = TransactionInfo.getProgramUser();
    if (this.citizenWorkspaceAccountDAO.readByUserName(userName).getType()
      .equals(IntakeClientTypeEntry.LINKED)) {
      concernRole = this.citizenAccountHelper.getLoggedInConcernRole();
    } else {
      try {
        final ApplicationAPI applicationAPIObj =
          ApplicationAPIFactory.newInstance();
        final UASubmittedApplicationList uaSubmittedApplicationsList =
          applicationAPIObj.listSubmittedApplications();
        for (int i = 0; i < uaSubmittedApplicationsList.data.size(); i++) {
          if (uaSubmittedApplicationsList.data.get(i).status
            .equals(IntakeApplicationStatus.DISPOSED)) {
            final IntakeAppConcernRoleLink intakeAppConcernRoleLink =
              IntakeAppConcernRoleLinkFactory.newInstance();
            final SearchByUsernameKey searchByUsernameKey =
              new SearchByUsernameKey();
            searchByUsernameKey.username = userName;
            final ConcernRoleIDAndNameDetailsList concernRoleIDAndNameDetailsList =
              intakeAppConcernRoleLink
                .searchConcernByCWAccount(searchByUsernameKey);
            final long conernRoleID =
              concernRoleIDAndNameDetailsList.dtls.get(0).concernRoleID;
            concernRole = this.concernRoleDAO.get(conernRoleID);
          }
        }
      } catch (final Exception e) {
      }
    }
    return concernRole;
  }

  public BDMRFROnlineRequest finishAppeal(final long iegExecutionID)
    throws AppException, InformationalException, NoSuchSchemaException {

    final ConcernRole person = this.citizenWorkspaceAccountManager
      .readAccountBy(TransactionInfo.getProgramUser()).getConcernRole();
    final Person appellant = this.personDAO.get(person.getID());
    final List<OnlineAppealRequest> requestsForAppellant =
      this.onlineAppealRequestDAO.searchByCreatedBy(appellant);
    long appealRequestID = 0L;
    for (final OnlineAppealRequest onlineAppealRequest : requestsForAppellant) {

      if (iegExecutionID == onlineAppealRequest.getIEGExecutionID()) {
        appealRequestID = onlineAppealRequest.getID().longValue();
        break;
      }
    }
    return submitAppealWorkflow(appealRequestID);
  }

  private BDMRFROnlineRequest submitAppealWorkflow(final long appealRequestID)
    throws AppException, InformationalException, NoSuchSchemaException {

    final BDMRFROnlineRequest onlineRequest = new BDMRFROnlineRequest();

    final OnlineAppealRequest onlineAppealRequest =
      this.onlineAppealRequestDAO.get(Long.valueOf(appealRequestID));

    final BDMOnlineAppealRequestKey bdmOnlineAppealRequestKey =
      new BDMOnlineAppealRequestKey();
    bdmOnlineAppealRequestKey.onlineAppealRequestID = appealRequestID;
    final BDMOnlineAppealRequestDtls bdmOnlineAppealRequest =
      BDMOnlineAppealRequestFactory.newInstance()
        .read(bdmOnlineAppealRequestKey);

    bdmOnlineAppealRequest.onlineAppealRequestReference =
      StringHelper.EMPTY_STRING + UniqueID.nextUniqueID("BDMAPPEAL");

    BDMOnlineAppealRequestFactory.newInstance()
      .modify(bdmOnlineAppealRequestKey, bdmOnlineAppealRequest);

    final ConcernRole selectedPrimaryAppellant =
      this.dsPrepopulator.get().getSelectedPrimaryAppellant(
        onlineAppealRequest.getDatastoreRootEntity());

    onlineAppealRequest.submit(selectedPrimaryAppellant,
      onlineAppealRequest.getVersionNo());

    final ConcernRole primaryAppellant =
      onlineAppealRequest.getPrimaryAppellant();

    final ProcessOnlineAppealDetails dtls = new ProcessOnlineAppealDetails();
    dtls.datastoreID =
      onlineAppealRequest.getDatastoreRootEntity().getUniqueID();
    dtls.concernRoleID = primaryAppellant.getID().longValue();
    dtls.onlineAppealRequestID = onlineAppealRequest.getID().longValue();
    dtls.onlineAppealType = APPEALTYPE.HEARING;
    dtls.appellantName = primaryAppellant.getName();
    dtls.appellantUserName = TransactionInfo.getProgramUser();

    final TaskCreateDetails taskDetails = new TaskCreateDetails();
    taskDetails.priority = TASKPRIORITY.HIGH;
    taskDetails.deadlineDateTime =
      Date.getCurrentDate().addDays(2).getDateTime();
    taskDetails.participantRoleID = primaryAppellant.getID().longValue();

    final BDMDocumentTypeKey documentTypeKey = new BDMDocumentTypeKey();
    documentTypeKey.documentType = DOCUMENTTYPE.REQUEST_FOR_RECONSIDERATION;

    final BDMTaskSkillTypeKey skillType = BDMWorkAllocationTaskFactory
      .newInstance().getSkillTypeByDocumentType(documentTypeKey);

    final BDMVSGTaskCreateDetails bdmVSGTaskCreateDetails =
      new BDMVSGTaskCreateDetails();
    bdmVSGTaskCreateDetails.participantRoleID =
      primaryAppellant.getID().longValue();
    bdmVSGTaskCreateDetails.workQueueID = BDMWorkAllocationTaskFactory
      .newInstance().getWorkQueueIDBySkillType(skillType).workQueueID;

    final BDMTaskSkillTypeList bdmTaskSkillTypeList =
      BDMWorkAllocationTaskFactory.newInstance()
        .searchBDMTaskSkillType(skillType);

    if (bdmTaskSkillTypeList.dtls.size() > 0) {
      bdmVSGTaskCreateDetails.bdmTaskClassificationID =
        bdmTaskSkillTypeList.dtls.get(0).bdmTaskClassificationID;
    }

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

    // Task 61198 to fix bug 61197 : Get client oral language
    taskDetails.subject =
      BDMUtil.getCodeTableDescriptionForUserLocale(SKILLTYPE.TABLENAME,
        skillType.skillType) + CuramConst.gkSpace + orallanuage
        + CuramConst.gkSpace + "Request For Reconsideration"
        + CuramConst.gkSpace
        + bdmOnlineAppealRequest.onlineAppealRequestReference
        + CuramConst.gkSpace + "for" + CuramConst.gkSpace
        + selectedPrimaryAppellant.getName() + CuramConst.gkSpace
        + selectedPrimaryAppellant.getPrimaryAlternateID();

    final List<Object> enactmentStructs = new ArrayList();
    enactmentStructs.add(taskDetails);
    enactmentStructs.add(dtls);
    enactmentStructs.add(bdmVSGTaskCreateDetails);

    String workflowDisabled =
      Configuration.getProperty(EnvVars.ENV_ENACT_WORKFLOW_PROCESS_DISABLED);

    if (workflowDisabled == null)
      workflowDisabled = "NO";

    if (workflowDisabled.equalsIgnoreCase(EnvVars.ENV_VALUE_NO)) {

      final OnlineAppealRequest request =
        this.onlineAppealRequestDAO.get(Long.valueOf(appealRequestID));

      request.taskRaised(request.getVersionNo());

      EnactmentService.startProcess("ProcessOnlineAppealRequest",
        enactmentStructs);
    }

    onlineRequest.onlineAppealRequestID = appealRequestID;
    onlineRequest.confirmation =
      bdmOnlineAppealRequest.onlineAppealRequestReference;

    return onlineRequest;
  }

}
