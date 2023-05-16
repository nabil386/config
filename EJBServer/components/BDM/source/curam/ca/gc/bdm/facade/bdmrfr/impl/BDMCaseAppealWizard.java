package curam.ca.gc.bdm.facade.bdmrfr.impl;

import curam.appeal.facade.fact.AppealFactory;
import curam.appeal.facade.fact.CaseAppealOnlineRequestsFactory;
import curam.appeal.facade.intf.Appeal;
import curam.appeal.facade.intf.CaseAppealOnlineRequests;
import curam.appeal.facade.struct.AppealCaseKey;
import curam.appeal.facade.struct.AppealWizCaseDetails;
import curam.appeal.facade.struct.AppealWizParticipantOptions;
import curam.appeal.facade.struct.AppealWizRecordOptions;
import curam.appeal.facade.struct.CaseID;
import curam.appeal.facade.struct.CaseIDPriorAppealID;
import curam.appeal.facade.struct.CreateAppealDetails;
import curam.appeal.facade.struct.ReadForCreateAppealDetails;
import curam.appeal.facade.struct.WizardProperties;
import curam.appeal.sl.entity.struct.AppealCaseIDKey;
import curam.appeal.sl.entity.struct.AppealOnlineRequestIDKey;
import curam.appeal.sl.entity.struct.OnlineRequestAppealCaseIDKey;
import curam.appeal.sl.struct.AppellantAndRespondentDetails;
import curam.ca.gc.bdm.entity.struct.BDMAppealDtls;
import curam.ca.gc.bdm.facade.bdmrfr.struct.BDMAppealWizDetailsState;
import curam.ca.gc.bdm.message.impl.BDMAPPEALWIZARDExceptionCreator;
import curam.ca.gc.bdm.sl.bdmrfr.fact.BDMMaintainAppealFactory;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.sl.struct.WizardStateID;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.ValidationHelper;
import curam.util.type.Date;
import curam.wizardpersistence.impl.WizardPersistentState;

public class BDMCaseAppealWizard
  extends curam.ca.gc.bdm.facade.bdmrfr.base.BDMCaseAppealWizard {

  // private static Appeal appealObj = AppealFactory.newInstance();

  private static CaseAppealOnlineRequests caseRequestsObj =
    CaseAppealOnlineRequestsFactory.newInstance();

  @Override
  public WizardProperties getConfigureWizard()
    throws AppException, InformationalException {

    final WizardProperties wizardProperties = new WizardProperties();

    wizardProperties.wizardMenu = "CaseAppealWizard.properties";

    return wizardProperties;
  }

  @Override
  public BDMAppealWizDetailsState
    getWizParticipantDetails(final WizardStateID wizardStateID)
      throws AppException, InformationalException {

    final BDMAppealWizDetailsState appealWizDetailsState =
      new BDMAppealWizDetailsState();

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    appealWizDetailsState.wizardProperties = getConfigureWizard();

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

    return appealWizDetailsState;
  }

  @Override
  public AppealWizParticipantOptions getWizParticipantOptions(
    final WizardStateID wizardStateID, final CaseID caseID)
    throws AppException, InformationalException {

    final AppealWizParticipantOptions appealWizParticipantOptions =
      new AppealWizParticipantOptions();

    final Appeal appealObj = AppealFactory.newInstance();

    ReadForCreateAppealDetails readForCreateAppealDetails =
      new ReadForCreateAppealDetails();

    final CaseIDPriorAppealID caseIDPriorAppealID = new CaseIDPriorAppealID();

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

    appealWizParticipantOptions.listDetails
      .assign(readForCreateAppealDetails.AppAndResDtls);

    appealWizParticipantOptions.anyIndicator =
      readForCreateAppealDetails.dtls.anyIndicator;

    appealWizParticipantOptions.appealStageNumber =
      readForCreateAppealDetails.dtls.appealStageNumber;

    if (!appealWizParticipantOptions.anyIndicator) {
      appealWizParticipantOptions.appealType =
        readForCreateAppealDetails.dtls.appealType;
    }
    return appealWizParticipantOptions;
  }

  @Override
  public BDMAppealWizDetailsState
    getWizRecordDetails(final WizardStateID wizardStateID)
      throws AppException, InformationalException {

    final BDMAppealWizDetailsState appealWizDetailsState =
      new BDMAppealWizDetailsState();

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    appealWizDetailsState.wizardProperties = getConfigureWizard();

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

  @Override
  public WizardStateID setWizParticipantDetails(
    final BDMAppealWizDetailsState wizState,
    final WizardStateID wizardStateID, final ActionIDProperty actionID)
    throws AppException, InformationalException {

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    final AppellantAndRespondentDetails appellantAndRespondentDetails =
      new AppellantAndRespondentDetails();

    appellantAndRespondentDetails.assign(wizState.participantDetails);
    appellantAndRespondentDetails.appealTypeCode = wizState.appealType;
    curam.appeal.sl.fact.AppealFactory.newInstance()
      .validateAppellantAndRespondentCombination(
        appellantAndRespondentDetails);

    if ("NextPage".equalsIgnoreCase(actionID.actionIDProperty)) {

      BDMAppealWizDetailsState existingWizardDetails =
        new BDMAppealWizDetailsState();
      existingWizardDetails = (BDMAppealWizDetailsState) wizardPersistentState
        .read(wizardStateID.wizardStateID);
      existingWizardDetails.participantDetails.appealTypeCode =
        wizState.appealType;

      existingWizardDetails.appealType = wizState.appealType;
      existingWizardDetails.participantDetails
        .assign(wizState.participantDetails);
      existingWizardDetails.caseID = wizState.caseID;

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
  public AppealWizCaseDetails setWizRecordDetails(
    final BDMAppealWizDetailsState wizState,
    final WizardStateID wizardStateID, final ActionIDProperty actionID,
    final AppealOnlineRequestIDKey requestIDKey)
    throws AppException, InformationalException {

    final AppealWizCaseDetails appealWizCaseDetails =
      new AppealWizCaseDetails();

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    if ("Save".equalsIgnoreCase(actionID.actionIDProperty)) {

      // the decision disputed date entered is in future
      if (wizState.recordDetails.dateDecisionDisputed
        .after(Date.getCurrentDate())) {
        ValidationHelper.addValidationError(BDMAPPEALWIZARDExceptionCreator
          .ERR_FV_DATE_DECISION_DISPUTED_IN_FUTURE());
      }

      // the date RFR received is in future
      if (wizState.recordDetails.dateRFRReceived
        .after(Date.getCurrentDate())) {
        ValidationHelper.addValidationError(BDMAPPEALWIZARDExceptionCreator
          .ERR_FV_DATE_RFR_RECEIVED_IN_FUTURE());
      }

      BDMAppealWizDetailsState existingWizardDetails =
        new BDMAppealWizDetailsState();

      existingWizardDetails = (BDMAppealWizDetailsState) wizardPersistentState
        .read(wizardStateID.wizardStateID);

      existingWizardDetails.recordDetails.assign(wizState.recordDetails);

      wizardPersistentState.modify(wizardStateID.wizardStateID,
        existingWizardDetails);

      final Appeal appealObj = AppealFactory.newInstance();
      final CreateAppealDetails createAppealDetails =
        new CreateAppealDetails();

      createAppealDetails.dtls.appAndResDetails
        .assign(existingWizardDetails.participantDetails);
      createAppealDetails.dtls.appAndResDetails.implCaseID =
        existingWizardDetails.caseID;

      createAppealDetails.dtls.appAndResDetails.priorAppealCaseID =
        existingWizardDetails.priorAppealID;

      createAppealDetails.dtls.assign(existingWizardDetails.recordDetails);
      createAppealDetails.dtls.appealType = existingWizardDetails.appealType;
      createAppealDetails.dtls.appAndResDetails.appealTypeCode =
        existingWizardDetails.appealType;

      final AppealCaseKey appealKey =
        appealObj.createAppeal(createAppealDetails);

      if (requestIDKey != null && requestIDKey.onlineAppealRequestID != 0L) {
        final OnlineRequestAppealCaseIDKey caseIDKey =
          new OnlineRequestAppealCaseIDKey();

        caseIDKey.appealCaseID = appealKey.appealCaseKey.caseID;
        caseRequestsObj.addOnlineRequestToAppealCase(caseIDKey, requestIDKey);
      }

      final AppealCaseIDKey appealCaseKey = new AppealCaseIDKey();
      appealCaseKey.caseID = appealKey.appealCaseKey.caseID;

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

      appealWizCaseDetails.appealCaseID = appealKey.appealCaseKey.caseID;
      appealWizCaseDetails.appealType = createAppealDetails.dtls.appealType;
    } else if ("Back".equalsIgnoreCase(actionID.actionIDProperty)) {

      BDMAppealWizDetailsState existingWizardDetails =
        new BDMAppealWizDetailsState();

      existingWizardDetails = (BDMAppealWizDetailsState) wizardPersistentState
        .read(wizardStateID.wizardStateID);

      existingWizardDetails.recordDetails.assign(wizState.recordDetails);

      wizardPersistentState.modify(wizardStateID.wizardStateID,
        existingWizardDetails);

    } else if ("Cancel".equalsIgnoreCase(actionID.actionIDProperty)) {

      final BDMAppealWizDetailsState appealWizDetailsState =
        new BDMAppealWizDetailsState();

      wizardPersistentState.modify(wizardStateID.wizardStateID,
        appealWizDetailsState);
    }

    appealWizCaseDetails.wizardStateID = wizardStateID.wizardStateID;

    return appealWizCaseDetails;
  }

  @Override
  public AppealWizRecordOptions
    getWizRecordOptions(final WizardStateID wizardStateID)
      throws AppException, InformationalException {

    final AppealWizRecordOptions appealWizRecordOptions =
      new AppealWizRecordOptions();

    final curam.appeal.sl.intf.Appeal appealObj =
      curam.appeal.sl.fact.AppealFactory.newInstance();
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

}
