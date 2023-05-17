package curam.ca.gc.bdm.facade.bdmrfr.impl;

import com.google.inject.Inject;
import curam.appeal.facade.fact.CaseAppealOnlineRequestsFactory;
import curam.appeal.facade.intf.CaseAppealOnlineRequests;
import curam.appeal.facade.struct.AppealCaseKey;
import curam.appeal.facade.struct.AppealCaseParticipantRoleKey;
import curam.appeal.facade.struct.AppealObjectTabList;
import curam.appeal.facade.struct.AppealParticipantRoleDetails;
import curam.appeal.facade.struct.AppealWizCaseDetails;
import curam.appeal.facade.struct.AppealWizKey;
import curam.appeal.facade.struct.AppealWizObjectOptions;
import curam.appeal.facade.struct.AppealWizParticipantOptions;
import curam.appeal.facade.struct.AppealWizRecordOptions;
import curam.appeal.facade.struct.CaseID;
import curam.appeal.facade.struct.CaseIDPriorAppealID;
import curam.appeal.facade.struct.CreateAppealDetails;
import curam.appeal.facade.struct.DeterminationObjectDetails;
import curam.appeal.facade.struct.ReadForCreateAppealDetails;
import curam.appeal.facade.struct.WizardProperties;
import curam.appeal.impl.AppealableCaseType;
import curam.appeal.sl.entity.fact.AppealOnlineRequestLinkFactory;
import curam.appeal.sl.entity.intf.AppealOnlineRequestLink;
import curam.appeal.sl.entity.struct.AppealCaseIDKey;
import curam.appeal.sl.entity.struct.AppealObjectKeyStruct1;
import curam.appeal.sl.entity.struct.AppealOnlineRequestIDKey;
import curam.appeal.sl.entity.struct.OnlineRequestAppealCaseIDKey;
import curam.appeal.sl.fact.AppealFactory;
import curam.appeal.sl.intf.Appeal;
import curam.appeal.sl.struct.AppealObjectActiveCaseInd;
import curam.appeal.sl.struct.AppellantAndRespondentDetails;
import curam.ca.gc.bdm.entity.fact.BDMOnlineAppealRequestFactory;
import curam.ca.gc.bdm.entity.struct.BDMAppealDtls;
import curam.ca.gc.bdm.entity.struct.BDMOnlineAppealRequestDtls;
import curam.ca.gc.bdm.entity.struct.BDMOnlineAppealRequestKey;
import curam.ca.gc.bdm.facade.bdmrfr.struct.BDMAppealWizDetailsState;
import curam.ca.gc.bdm.facade.bdmrfr.struct.BDMOnlineAppealRequestDetails;
import curam.ca.gc.bdm.facade.bdmrfr.struct.BDMOnlineAppealRequestsList;
import curam.ca.gc.bdm.message.impl.BDMAPPEALWIZARDExceptionCreator;
import curam.ca.gc.bdm.sl.bdmrfr.fact.BDMMaintainAppealFactory;
import curam.codetable.APPEALSTAGEAPPEALTYPE;
import curam.codetable.impl.APPEALOBJECTTYPEEntry;
import curam.codetable.impl.CASETYPECODEEntry;
import curam.commonintake.impl.ApplicationCaseDAO;
import curam.core.facade.infrastructure.assessment.fact.CaseDeterminationFactory;
import curam.core.facade.infrastructure.assessment.intf.CaseDetermination;
import curam.core.facade.infrastructure.assessment.struct.CaseDeterminationAssessmentList;
import curam.core.facade.infrastructure.assessment.struct.CaseDeterminationDetails;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.facade.struct.CaseIDKey;
import curam.core.fact.CaseHeaderFactory;
import curam.core.onlineappealrequest.impl.OnlineAppealRequest;
import curam.core.onlineappealrequest.impl.OnlineAppealRequestDAO;
import curam.core.sl.infrastructure.impl.ValidationManagerFactory;
import curam.core.sl.struct.WizardStateID;
import curam.core.struct.CaseKey;
import curam.core.struct.CaseTypeCode;
import curam.datastore.impl.NoSuchSchemaException;
import curam.message.APPEALDETERMINATION;
import curam.message.APPEALOBJECT;
import curam.participant.person.impl.Person;
import curam.participant.person.impl.PersonDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.persistence.ValidationHelper;
import curam.util.type.Date;
import curam.util.type.NotFoundIndicator;
import curam.wizardpersistence.impl.WizardPersistentState;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BDMAppealWizard
  extends curam.ca.gc.bdm.facade.bdmrfr.base.BDMAppealWizard {

  @Inject
  private Map<CASETYPECODEEntry, AppealableCaseType> appealableCaseTypeMap;

  @Inject
  private PersonDAO personDAO;

  @Inject
  private ApplicationCaseDAO applicationCaseDAO;

  @Inject
  private OnlineAppealRequestDAO onlineAppealRequestDAO;

  private static CaseAppealOnlineRequests caseRequestsObj =
    CaseAppealOnlineRequestsFactory.newInstance();

  public BDMAppealWizard() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public AppealWizCaseDetails setWizRecordDetails(
    final BDMAppealWizDetailsState details, final WizardStateID wizardStateID,
    final ActionIDProperty actionID,
    final AppealOnlineRequestIDKey requestIDKey)
    throws AppException, InformationalException {

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    final AppealWizCaseDetails appealWizCaseDetails =
      new AppealWizCaseDetails();

    appealWizCaseDetails.wizardStateID = wizardStateID.wizardStateID;

    if ("Save".equalsIgnoreCase(actionID.actionIDProperty)) {

      // the decision disputed date entered is in future
      if (details.recordDetails.dateDecisionDisputed
        .after(Date.getCurrentDate())) {
        ValidationHelper.addValidationError(BDMAPPEALWIZARDExceptionCreator
          .ERR_FV_DATE_DECISION_DISPUTED_IN_FUTURE());
      }

      // the date RFR received is in future
      if (details.recordDetails.dateRFRReceived
        .after(Date.getCurrentDate())) {
        ValidationHelper.addValidationError(BDMAPPEALWIZARDExceptionCreator
          .ERR_FV_DATE_RFR_RECEIVED_IN_FUTURE());
      }

      ValidationHelper.failIfErrorsExist();

      BDMAppealWizDetailsState existingWizardDetails =
        new BDMAppealWizDetailsState();

      existingWizardDetails = (BDMAppealWizDetailsState) wizardPersistentState
        .read(wizardStateID.wizardStateID);

      existingWizardDetails.recordDetails.assign(details.recordDetails);

      wizardPersistentState.modify(wizardStateID.wizardStateID,
        existingWizardDetails);

      final curam.appeal.facade.intf.Appeal appealObj =
        curam.appeal.facade.fact.AppealFactory.newInstance();

      final CreateAppealDetails createAppealDetails =
        new CreateAppealDetails();

      createAppealDetails.dtls.appAndResDetails
        .assign(existingWizardDetails.participantDetails);
      createAppealDetails.dtls.appAndResDetails.implCaseID =
        existingWizardDetails.caseID;

      createAppealDetails.dtls.appAndResDetails.priorAppealCaseID =
        existingWizardDetails.priorAppealID;

      createAppealDetails.dtls.assign(existingWizardDetails.objDetails);

      createAppealDetails.dtls.assign(existingWizardDetails.recordDetails);

      createAppealDetails.dtls.appealType = existingWizardDetails.appealType;
      createAppealDetails.dtls.appAndResDetails.appealTypeCode =
        existingWizardDetails.appealType;

      final AppealCaseKey appealCaseDetails =
        appealObj.createAppeal(createAppealDetails);

      if (requestIDKey != null && requestIDKey.onlineAppealRequestID != 0L) {
        final OnlineRequestAppealCaseIDKey caseIDKey =
          new OnlineRequestAppealCaseIDKey();

        caseIDKey.appealCaseID = appealCaseDetails.appealCaseKey.caseID;
        caseRequestsObj.addOnlineRequestToAppealCase(caseIDKey, requestIDKey);
      }
      final AppealCaseIDKey appealCaseKey = new AppealCaseIDKey();
      appealCaseKey.caseID = appealCaseDetails.appealCaseKey.caseID;

      // create BDM Appeal details
      final BDMAppealDtls bdmAppealDetails = new BDMAppealDtls();

      bdmAppealDetails.appealID = curam.appeal.sl.entity.fact.AppealFactory
        .newInstance().readByCaseID(appealCaseKey).appealID;

      bdmAppealDetails.admsReference =
        existingWizardDetails.recordDetails.admsReference;
      bdmAppealDetails.dateDecisionDisputed =
        existingWizardDetails.recordDetails.dateDecisionDisputed;
      bdmAppealDetails.dateRFRReceived =
        existingWizardDetails.recordDetails.dateRFRReceived;

      BDMMaintainAppealFactory.newInstance()
        .createBDMAppeal(bdmAppealDetails);

      appealWizCaseDetails.appealCaseID =
        appealCaseDetails.appealCaseKey.caseID;

      appealWizCaseDetails.appealType = createAppealDetails.dtls.appealType;
    } else if ("Back".equalsIgnoreCase(actionID.actionIDProperty)) {

      BDMAppealWizDetailsState existingWizardDetails =
        new BDMAppealWizDetailsState();

      existingWizardDetails = (BDMAppealWizDetailsState) wizardPersistentState
        .read(wizardStateID.wizardStateID);

      existingWizardDetails.recordDetails.assign(details.recordDetails);

      wizardPersistentState.modify(wizardStateID.wizardStateID,
        existingWizardDetails);

    } else if ("Cancel".equalsIgnoreCase(actionID.actionIDProperty)) {

      final BDMAppealWizDetailsState appealWizDetailsState =
        new BDMAppealWizDetailsState();

      wizardPersistentState.modify(wizardStateID.wizardStateID,
        appealWizDetailsState);
    }

    return appealWizCaseDetails;
  }

  @Override
  public BDMAppealWizDetailsState
    getWizRecordDetails(final AppealWizKey wizardStateID)
      throws AppException, InformationalException {

    final BDMAppealWizDetailsState appealWizDetailsState =
      new BDMAppealWizDetailsState();

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    appealWizDetailsState.wizardProperties =
      getConfigureWizard(wizardStateID.caseID);

    final BDMAppealWizDetailsState existingWizardDetails =
      (BDMAppealWizDetailsState) wizardPersistentState
        .read(wizardStateID.wizardStateID);

    appealWizDetailsState.caseID = existingWizardDetails.caseID;
    appealWizDetailsState.stateID.wizardStateID = wizardStateID.wizardStateID;

    appealWizDetailsState.recordDetails
      .assign(existingWizardDetails.recordDetails);

    if (appealWizDetailsState.recordDetails.dateReceived.isZero()) {

      appealWizDetailsState.recordDetails.dateReceived =
        Date.getCurrentDate();
    }
    if (appealWizDetailsState.recordDetails.effectiveDate.isZero()) {
      appealWizDetailsState.recordDetails.effectiveDate =
        Date.getCurrentDate();
    }

    return appealWizDetailsState;
  }

  private WizardProperties getConfigureWizard(final long caseID)
    throws AppException, InformationalException {

    final WizardProperties wizardProperties = new WizardProperties();

    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = caseID;

    final CaseTypeCode caseType =
      CaseHeaderFactory.newInstance().readCaseTypeCode(caseKey);

    final AppealableCaseType appealableCaseType = this.appealableCaseTypeMap
      .get(CASETYPECODEEntry.get(caseType.caseTypeCode));

    if (appealableCaseType != null) {
      wizardProperties.wizardMenu =
        appealableCaseType.getCreateWizardProperties();
    } else {

      wizardProperties.wizardMenu =
        "CreateAppealDeterminationWizard.properties";
    }

    return wizardProperties;
  }

  @Override
  public WizardStateID setWizObjectDetails(
    final BDMAppealWizDetailsState updatedWizardDetails,
    final WizardStateID wizardStateID, final ActionIDProperty actionID)
    throws AppException, InformationalException {

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    if ("NextPage".equalsIgnoreCase(actionID.actionIDProperty)) {

      BDMAppealWizDetailsState existingWizardDetails =
        new BDMAppealWizDetailsState();

      if (updatedWizardDetails.objDetails.objectTabList.isEmpty()) {
        ValidationManagerFactory.getManager().throwNoLookup(
          new AppException(APPEALDETERMINATION.ERR_APPEAL_FV_DETERMINATION));
      }

      final curam.appeal.facade.intf.Appeal appealObj =
        curam.appeal.facade.fact.AppealFactory.newInstance();
      final AppealObjectTabList appealObjectTabList =
        new AppealObjectTabList();

      DeterminationObjectDetails determinationObjectDetails =
        new DeterminationObjectDetails();

      appealObjectTabList.tabList =
        updatedWizardDetails.objDetails.objectTabList;

      determinationObjectDetails =
        appealObj.getAppealableObjectsFromTabList(appealObjectTabList);

      updatedWizardDetails.objDetails.appealObjectsDelimitedList =
        determinationObjectDetails.objectIDListString;

      existingWizardDetails = (BDMAppealWizDetailsState) wizardPersistentState
        .read(wizardStateID.wizardStateID);

      existingWizardDetails.objDetails
        .assign(updatedWizardDetails.objDetails);
      existingWizardDetails.caseID = updatedWizardDetails.caseID;
      existingWizardDetails.priorAppealID =
        updatedWizardDetails.priorAppealID;

      existingWizardDetails.recordDetails.effectiveDate =
        determinationObjectDetails.effectiveDate;

      wizardPersistentState.modify(wizardStateID.wizardStateID,
        existingWizardDetails);

    } else if ("Cancel".equalsIgnoreCase(actionID.actionIDProperty)) {

      final BDMAppealWizDetailsState appealWizDetailsState =
        new BDMAppealWizDetailsState();

      wizardPersistentState.modify(wizardStateID.wizardStateID,
        appealWizDetailsState);
    }

    return wizardStateID;
  }

  @Override
  public BDMAppealWizDetailsState
    getWizObjectDetails(final AppealWizKey wizardStateID)
      throws AppException, InformationalException {

    final BDMAppealWizDetailsState appealWizDetailsState =
      new BDMAppealWizDetailsState();

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    appealWizDetailsState.wizardProperties =
      getConfigureWizard(wizardStateID.caseID);

    if (wizardStateID.wizardStateID != 0L) {

      final BDMAppealWizDetailsState existingWizardDetails =
        (BDMAppealWizDetailsState) wizardPersistentState
          .read(wizardStateID.wizardStateID);

      appealWizDetailsState.stateID.wizardStateID =
        wizardStateID.wizardStateID;

      appealWizDetailsState.objDetails
        .assign(existingWizardDetails.objDetails);
    } else {

      appealWizDetailsState.stateID.wizardStateID =
        wizardPersistentState.create(new BDMAppealWizDetailsState());
    }
    return appealWizDetailsState;
  }

  @Override
  public AppealWizObjectOptions getWizObjectOptions(
    final WizardStateID wizardStateID, final CaseID caseID)
    throws AppException, InformationalException {

    final AppealWizObjectOptions appealWizObjectOptions =
      new AppealWizObjectOptions();

    final CaseDetermination caseDeterminationObj =
      CaseDeterminationFactory.newInstance();
    final CaseIDKey caseIDKey = new CaseIDKey();

    final Appeal appealObj = AppealFactory.newInstance();

    if (wizardStateID.wizardStateID != 0L) {
      final WizardPersistentState wizardPersistentState =
        new WizardPersistentState();

      final BDMAppealWizDetailsState existingWizardDetails =
        (BDMAppealWizDetailsState) wizardPersistentState
          .read(wizardStateID.wizardStateID);

      caseIDKey.dtls.dtls.caseID = existingWizardDetails.caseID;
    } else {
      caseIDKey.dtls.dtls.caseID = caseID.caseID;
    }

    final CaseDeterminationAssessmentList determinationList =
      caseDeterminationObj.listAssessmentHistory(caseIDKey);

    final APPEALOBJECTTYPEEntry objectType =
      APPEALOBJECTTYPEEntry.DETERMINATION;

    for (final CaseDeterminationDetails caseDeterminationDetails : determinationList.caseAssessmentDtls) {

      final AppealObjectKeyStruct1 appealObjectKey =
        new AppealObjectKeyStruct1();

      AppealObjectActiveCaseInd isObjectActiveOnAppealCase =
        new AppealObjectActiveCaseInd();

      appealObjectKey.objectID = caseDeterminationDetails.determinationID;
      appealObjectKey.objectType = objectType.getCode();

      isObjectActiveOnAppealCase =
        appealObj.isObjectOnActiveAppealsCase(appealObjectKey);

      if (!isObjectActiveOnAppealCase.result) {
        appealWizObjectOptions.caseList.caseAssessmentDtls
          .add(caseDeterminationDetails);
      }
    }

    return appealWizObjectOptions;
  }

  @Override
  public WizardStateID setWizParticipantDetails(
    final BDMAppealWizDetailsState updatedWizardDetails,
    final WizardStateID wizardStateID, final ActionIDProperty actionID)
    throws AppException, InformationalException {

    final WizardStateID existingWizardStateID = new WizardStateID();
    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    existingWizardStateID.wizardStateID = wizardStateID.wizardStateID;

    if ("NextPage".equalsIgnoreCase(actionID.actionIDProperty)) {
      final AppellantAndRespondentDetails appellantAndRespondentDetails =
        new AppellantAndRespondentDetails();

      appellantAndRespondentDetails
        .assign(updatedWizardDetails.participantDetails);
      appellantAndRespondentDetails.appealTypeCode =
        updatedWizardDetails.appealType;

      AppealFactory.newInstance().validateAppellantAndRespondentCombination(
        appellantAndRespondentDetails);
    }

    if ("NextPage".equalsIgnoreCase(actionID.actionIDProperty)
      || "Back".equalsIgnoreCase(actionID.actionIDProperty)) {

      BDMAppealWizDetailsState existingWizardDetails =
        new BDMAppealWizDetailsState();

      existingWizardDetails = (BDMAppealWizDetailsState) wizardPersistentState
        .read(wizardStateID.wizardStateID);

      existingWizardDetails.participantDetails.appealTypeCode =
        updatedWizardDetails.appealType;

      existingWizardDetails.appealType = updatedWizardDetails.appealType;
      existingWizardDetails.participantDetails
        .assign(updatedWizardDetails.participantDetails);
      existingWizardDetails.caseID = updatedWizardDetails.caseID;

      if (!updatedWizardDetails.participantDetails.appealObjectsDelimitedList
        .isEmpty()) {
        existingWizardDetails.objDetails.appealObjectsDelimitedList =
          updatedWizardDetails.participantDetails.appealObjectsDelimitedList;
      }

      wizardPersistentState.modify(wizardStateID.wizardStateID,
        existingWizardDetails);

    } else if ("Cancel".equalsIgnoreCase(actionID.actionIDProperty)) {

      final BDMAppealWizDetailsState appealWizDetailsState =
        new BDMAppealWizDetailsState();

      wizardPersistentState.modify(wizardStateID.wizardStateID,
        appealWizDetailsState);
    }

    return existingWizardStateID;
  }

  @Override
  public BDMAppealWizDetailsState
    getWizParticipantDetails(final AppealWizKey wizardStateID)
      throws AppException, InformationalException {

    final BDMAppealWizDetailsState appealWizDetailsState =
      new BDMAppealWizDetailsState();

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    appealWizDetailsState.wizardProperties =
      getConfigureWizard(wizardStateID.caseID);

    if (wizardStateID.wizardStateID != 0L) {

      final BDMAppealWizDetailsState existingWizardDetails =
        (BDMAppealWizDetailsState) wizardPersistentState
          .read(wizardStateID.wizardStateID);

      appealWizDetailsState.caseID = existingWizardDetails.caseID;
      appealWizDetailsState.stateID.wizardStateID =
        wizardStateID.wizardStateID;

      appealWizDetailsState.participantDetails
        .assign(existingWizardDetails.participantDetails);
    } else {

      appealWizDetailsState.stateID.wizardStateID =
        wizardPersistentState.create(new BDMAppealWizDetailsState());
    }

    final CaseID caseID = new CaseID();

    caseID.caseID = wizardStateID.caseID;
    appealWizDetailsState.startPageURI =
      curam.appeal.facade.fact.AppealFactory.newInstance()
        .getCreateWizardStartPage(caseID).pageURI;

    return appealWizDetailsState;
  }

  @Override
  public AppealWizParticipantOptions
    getWizParticipantOptions(final WizardStateID wizardStateID)
      throws AppException, InformationalException {

    final AppealWizParticipantOptions appealWizParticipantOptions =
      new AppealWizParticipantOptions();

    final curam.appeal.facade.intf.Appeal appealObj =
      curam.appeal.facade.fact.AppealFactory.newInstance();
    final CaseIDPriorAppealID caseIDPriorAppealID = new CaseIDPriorAppealID();
    ReadForCreateAppealDetails readForCreateAppealDetails =
      new ReadForCreateAppealDetails();

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    final BDMAppealWizDetailsState existingWizardDetails =
      (BDMAppealWizDetailsState) wizardPersistentState
        .read(wizardStateID.wizardStateID);

    caseIDPriorAppealID.caseIDpriorAppealID.caseID =
      existingWizardDetails.caseID;

    readForCreateAppealDetails =
      appealObj.readForCreateNewAppeal(caseIDPriorAppealID);

    appealWizParticipantOptions.anyIndicator =
      readForCreateAppealDetails.dtls.anyIndicator;

    appealWizParticipantOptions.appealStageNumber =
      readForCreateAppealDetails.dtls.appealStageNumber;

    if (!APPEALSTAGEAPPEALTYPE.ANY
      .equalsIgnoreCase(readForCreateAppealDetails.dtls.appealType)) {
      appealWizParticipantOptions.appealType =
        readForCreateAppealDetails.dtls.appealType;
    }

    appealWizParticipantOptions.listDetails
      .assign(readForCreateAppealDetails.AppAndResDtls);

    return appealWizParticipantOptions;
  }

  @Override
  public AppealWizParticipantOptions getWizParticipantOptionsByWizOrCase(
    final WizardStateID wizardStateID, final CaseID caseID)
    throws AppException, InformationalException {

    final AppealWizParticipantOptions appealWizParticipantOptions =
      new AppealWizParticipantOptions();

    final curam.appeal.facade.intf.Appeal appealObj =
      curam.appeal.facade.fact.AppealFactory.newInstance();
    final CaseIDPriorAppealID caseIDPriorAppealID = new CaseIDPriorAppealID();
    ReadForCreateAppealDetails readForCreateAppealDetails =
      new ReadForCreateAppealDetails();

    if (wizardStateID.wizardStateID != 0L) {

      final WizardPersistentState wizardPersistentState =
        new WizardPersistentState();

      final BDMAppealWizDetailsState existingWizardDetails =
        (BDMAppealWizDetailsState) wizardPersistentState
          .read(wizardStateID.wizardStateID);

      caseIDPriorAppealID.caseIDpriorAppealID.caseID =
        existingWizardDetails.caseID;

      caseIDPriorAppealID.caseIDpriorAppealID.priorAppealID =
        existingWizardDetails.priorAppealID;

      caseIDPriorAppealID.caseIDpriorAppealID.appealType =
        existingWizardDetails.appealType;
    } else {

      caseIDPriorAppealID.caseIDpriorAppealID.caseID = caseID.caseID;
    }

    readForCreateAppealDetails =
      appealObj.readForCreateNewAppeal(caseIDPriorAppealID);

    appealWizParticipantOptions.anyIndicator =
      readForCreateAppealDetails.dtls.anyIndicator;

    appealWizParticipantOptions.appealStageNumber =
      readForCreateAppealDetails.dtls.appealStageNumber;

    if (!APPEALSTAGEAPPEALTYPE.ANY
      .equalsIgnoreCase(readForCreateAppealDetails.dtls.appealType)) {
      appealWizParticipantOptions.appealType =
        readForCreateAppealDetails.dtls.appealType;
    }

    appealWizParticipantOptions.listDetails
      .assign(readForCreateAppealDetails.AppAndResDtls);

    return appealWizParticipantOptions;
  }

  @Override
  public AppealWizRecordOptions
    getWizRecordOptions(final WizardStateID wizardStateID)
      throws AppException, InformationalException {

    final AppealWizRecordOptions appealWizRecordOptions =
      new AppealWizRecordOptions();

    final Appeal appealObj = AppealFactory.newInstance();
    final CaseIDPriorAppealID key = new CaseIDPriorAppealID();
    curam.appeal.sl.struct.ReadForCreateAppealDetails readForCreateAppealDetails =
      new curam.appeal.sl.struct.ReadForCreateAppealDetails();

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    final BDMAppealWizDetailsState existingWizardDetails =
      (BDMAppealWizDetailsState) wizardPersistentState
        .read(wizardStateID.wizardStateID);

    key.caseIDpriorAppealID.caseID = existingWizardDetails.caseID;
    key.caseIDpriorAppealID.priorAppealID =
      existingWizardDetails.priorAppealID;

    key.caseIDpriorAppealID.appealType = existingWizardDetails.appealType;

    readForCreateAppealDetails =
      appealObj.readForCreateAppeal(key.caseIDpriorAppealID);

    appealWizRecordOptions.benefitsIndicator =
      readForCreateAppealDetails.benefitsIndicator;

    return appealWizRecordOptions;
  }

  @Override
  public BDMOnlineAppealRequestsList
    getOnlineAppealRequestsForDetAppealCreation(
      final WizardStateID wizardStateID)
      throws AppException, InformationalException {

    BDMOnlineAppealRequestsList returnList =
      new BDMOnlineAppealRequestsList();

    if (wizardStateID.wizardStateID != 0L) {

      final WizardPersistentState wizardPersistentState =
        new WizardPersistentState();

      final BDMAppealWizDetailsState existingWizardDetails =
        (BDMAppealWizDetailsState) wizardPersistentState
          .read(wizardStateID.wizardStateID);

      if (!existingWizardDetails.participantDetails.appellantOrganizationInd) {
        returnList = listOpenRequestsForAppellant(
          existingWizardDetails.participantDetails.appellantCaseParticipantRoleID);
      }
    }
    return returnList;
  }

  @Override
  public BDMOnlineAppealRequestsList getOnlineAppealRequestsForAppealCreation(
    final WizardStateID wizardStateID)
    throws AppException, InformationalException {

    BDMOnlineAppealRequestsList returnList =
      new BDMOnlineAppealRequestsList();

    if (wizardStateID.wizardStateID != 0L) {

      final WizardPersistentState wizardPersistentState =
        new WizardPersistentState();

      final BDMAppealWizDetailsState existingWizardDetails =
        (BDMAppealWizDetailsState) wizardPersistentState
          .read(wizardStateID.wizardStateID);

      if (!existingWizardDetails.participantDetails.appellantOrganizationInd) {

        returnList = listOpenRequestsForAppellant(
          existingWizardDetails.participantDetails.appellantCaseParticipantRoleID);
      }
    }
    return returnList;
  }

  /**
   * Returns the list of online appeal requests for the appellant.
   *
   * @param appellantID appellant identifier
   * @return online appeal requests list
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  private BDMOnlineAppealRequestsList listOpenRequestsForAppellant(
    final long appellantID) throws AppException, InformationalException {

    final BDMOnlineAppealRequestsList returnList =
      new BDMOnlineAppealRequestsList();

    final AppealCaseParticipantRoleKey appellantKey =
      new AppealCaseParticipantRoleKey();

    if (appellantID != 0L) {
      appellantKey.appealCaseParticipantRoleKey.caseParticipantRoleKey.caseParticipantRoleID =
        appellantID;

      final curam.appeal.facade.intf.Appeal appealObj =
        curam.appeal.facade.fact.AppealFactory.newInstance();

      final AppealParticipantRoleDetails appellantDetails =
        appealObj.readConcernRoleDetails(appellantKey);

      final long concernRoleID =
        appellantDetails.appealParticipantRoleDetails.caseParticipantConcernRoleDetails.concernRoleID;

      final Person appellant =
        this.personDAO.get(Long.valueOf(concernRoleID));

      final List<OnlineAppealRequest> requestsForAppellant =
        this.onlineAppealRequestDAO.searchByPrimaryAppellant(appellant);

      final AppealOnlineRequestLink onlineRequestLink =
        AppealOnlineRequestLinkFactory.newInstance();

      final List<OnlineAppealRequest> filteredRequestList = new ArrayList<>();

      final NotFoundIndicator recordNotFound = new NotFoundIndicator();
      final AppealOnlineRequestIDKey appealOnlineRequestIDKey =
        new AppealOnlineRequestIDKey();

      for (final OnlineAppealRequest onlineAppealRequest : requestsForAppellant) {
        appealOnlineRequestIDKey.onlineAppealRequestID =
          onlineAppealRequest.getID().longValue();
        onlineRequestLink.readAppealCaseIDForRequest(recordNotFound,
          appealOnlineRequestIDKey);

        if (recordNotFound.isNotFound()) {
          filteredRequestList.add(onlineAppealRequest);
        }
      }

      returnList.dtls.addAll(getRequestsDetailsList(filteredRequestList));
    }
    return returnList;
  }

  /**
   * Returns the list of online appeal requests details.
   *
   * @param onlineAppealRequests
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private List<BDMOnlineAppealRequestDetails> getRequestsDetailsList(
    final List<OnlineAppealRequest> onlineAppealRequests)
    throws AppException, InformationalException {

    final List<BDMOnlineAppealRequestDetails> appealRequestDetailsList =
      new ArrayList<>();

    for (final OnlineAppealRequest request : onlineAppealRequests) {

      final BDMOnlineAppealRequestDetails details =
        getRequestDetails(request);

      appealRequestDetailsList.add(details);
    }
    return appealRequestDetailsList;
  }

  /**
   * This method populates and returns the online appeal request details from
   * the object.
   *
   * @param onlineAppealRequest online appeal request
   * @return online appeal request details
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  public BDMOnlineAppealRequestDetails
    getRequestDetails(final OnlineAppealRequest onlineAppealRequest)
      throws AppException, InformationalException {

    final BDMOnlineAppealRequestDetails onlineRequestDtls =
      new BDMOnlineAppealRequestDetails();

    onlineRequestDtls.requestID = onlineAppealRequest.getID().longValue();

    onlineRequestDtls.primaryAppellant =
      onlineAppealRequest.getPrimaryAppellant().getName();

    try {
      onlineRequestDtls.requestURI =
        onlineAppealRequest.getDocumentURI("CaseOnlineAppealRequests_list");
    } catch (final NoSuchSchemaException e) {

      onlineRequestDtls.requestURI = "";
    }

    onlineRequestDtls.submittedDateTime =
      onlineAppealRequest.getSubmittedDateTime();

    onlineRequestDtls.title =
      APPEALOBJECT.INF_APPEAL_ONLINEREQUEST_PREFIX.getMessageText()
        + onlineRequestDtls.submittedDateTime;

    // get reference number for the request
    final BDMOnlineAppealRequestKey key = new BDMOnlineAppealRequestKey();
    key.onlineAppealRequestID = onlineAppealRequest.getID();

    final BDMOnlineAppealRequestDtls bdmOnlineAppealRequestDetails =
      BDMOnlineAppealRequestFactory.newInstance().read(key);
    onlineRequestDtls.reference =
      bdmOnlineAppealRequestDetails.onlineAppealRequestReference;

    // get program name
    onlineRequestDtls.program = bdmOnlineAppealRequestDetails.programCode;

    return onlineRequestDtls;
  }
}
