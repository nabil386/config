package curam.ca.gc.bdm.facade.bdmrfr.impl;

import com.google.inject.Inject;
import curam.appeal.facade.fact.AppealedCaseApprovalFactory;
import curam.appeal.facade.fact.AppealedCaseFactory;
import curam.appeal.facade.intf.AppealedCaseApproval;
import curam.appeal.facade.struct.AppealedCaseApproveDetails;
import curam.appeal.facade.struct.ModifyResolutionDetails;
import curam.appeal.sl.entity.fact.AppealRelationshipFactory;
import curam.appeal.sl.entity.fact.HearingDecisionAttachmentLinkFactory;
import curam.appeal.sl.entity.fact.HearingDecisionFactory;
import curam.appeal.sl.entity.fact.HearingFactory;
import curam.appeal.sl.entity.fact.HearingUserRoleFactory;
import curam.appeal.sl.entity.intf.AppealRelationship;
import curam.appeal.sl.entity.intf.Hearing;
import curam.appeal.sl.entity.intf.HearingDecision;
import curam.appeal.sl.entity.intf.HearingDecisionAttachmentLink;
import curam.appeal.sl.entity.intf.HearingUserRole;
import curam.appeal.sl.entity.struct.ActiveDecisionAttachmentsKey;
import curam.appeal.sl.entity.struct.AppealCountDetails;
import curam.appeal.sl.entity.struct.AppealRelationshipDtls;
import curam.appeal.sl.entity.struct.AppealRelationshipKey;
import curam.appeal.sl.entity.struct.CaseAndStatusKey;
import curam.appeal.sl.entity.struct.DecisionDateDetails;
import curam.appeal.sl.entity.struct.HearingCaseUserNameDtlsList;
import curam.appeal.sl.entity.struct.HearingDecisionAttachmentLinkDtls;
import curam.appeal.sl.entity.struct.HearingDecisionCaseKey;
import curam.appeal.sl.entity.struct.HearingDecisionDtls;
import curam.appeal.sl.entity.struct.HearingDtls;
import curam.appeal.sl.entity.struct.HearingKey;
import curam.appeal.sl.entity.struct.HearingUserRoleDtls;
import curam.appeal.sl.entity.struct.HearingUserRoleStatusKey;
import curam.appeal.sl.entity.struct.ModifyStatusCodesDetails;
import curam.attachmentlink.fact.AttachmentLinkFactory;
import curam.attachmentlink.struct.AttachmentLinkDtls;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.codetable.BDMRFRISSUELEVEL;
import curam.ca.gc.bdm.codetable.BDMRFRISSUESTATUS;
import curam.ca.gc.bdm.entity.fact.BDMRFRIssueFactory;
import curam.ca.gc.bdm.entity.struct.BDMRFRIssueDtls;
import curam.ca.gc.bdm.entity.struct.BDMRFRIssueDtlsList;
import curam.ca.gc.bdm.entity.struct.BDMRFRIssueKeyStruct1;
import curam.ca.gc.bdm.events.BDMITEMUNDERAPPEAL;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.facade.bdmrfr.struct.BDMAppealedCaseSummaryDetails;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskSkillTypeKey;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskSkillTypeList;
import curam.ca.gc.bdm.message.BDMRFRSISSUE;
import curam.ca.gc.bdm.pdc.impl.BDMProductDetailsCalculatorBase;
import curam.ca.gc.bdm.sl.bdmrfr.fact.BDMMaintainRFRIssueFactory;
import curam.ca.gc.bdm.sl.bdmrfr.struct.BDMRFRIssueAppealRelationshipKey;
import curam.ca.gc.bdm.sl.bdmworkallocation.fact.BDMWorkAllocationTaskFactory;
import curam.ca.gc.bdm.sl.bdmworkallocation.struct.BDMVSGTaskCreateDetails;
import curam.codetable.APPEALCASERESOLUTION;
import curam.codetable.ATTACHMENTOBJECTLINKTYPE;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.CASEUSERROLETYPE;
import curam.codetable.DECISIONATTACHMENTTYPE;
import curam.codetable.HEARINGDECISIONSTATUS;
import curam.codetable.HEARINGPARTICIPATION;
import curam.codetable.HEARINGSTATUS;
import curam.codetable.HEARINGTYPE;
import curam.codetable.ORGOBJECTTYPE;
import curam.codetable.RECORDSTATUS;
import curam.codetable.SKILLTYPE;
import curam.core.facade.infrastructure.struct.PersonAndEvidenceTypeList;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.ProductDeliveryFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.sl.entity.fact.CaseUserRoleFactory;
import curam.core.sl.entity.fact.OrgObjectLinkFactory;
import curam.core.sl.entity.intf.CaseUserRole;
import curam.core.sl.entity.struct.CaseUserRoleDtls;
import curam.core.sl.entity.struct.CaseUserRoleSearchByCaseAndStatusAndTypeAndDate;
import curam.core.sl.entity.struct.OrgObjLinkIDAndCaseUserRoleIDList;
import curam.core.sl.entity.struct.OrgObjectLinkDtls;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.ProductDeliveryDtls;
import curam.core.struct.ProductDeliveryKey;
import curam.core.struct.UsersKey;
import curam.participant.impl.ConcernRoleDAO;
import curam.pdc.facade.fact.PDCPersonFactory;
import curam.pdc.facade.intf.PDCPerson;
import curam.pdc.facade.struct.PDCEvidenceDetailsList;
import curam.piwrapper.caseheader.impl.CaseHeaderDAO;
import curam.util.events.impl.EventService;
import curam.util.events.struct.Event;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.NotFoundIndicator;
import curam.util.workflow.impl.EnactmentService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BDMAppealedCase
  extends curam.ca.gc.bdm.facade.bdmrfr.base.BDMAppealedCase {

  @Inject
  protected CaseHeaderDAO caseHeaderDAO;

  @Inject
  ConcernRoleDAO concernRoleDAO;

  @Inject
  public Map<Long, BDMProductDetailsCalculatorBase> pdDetailsCalculatorMap;

  public BDMAppealedCase() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public BDMAppealedCaseSummaryDetails readItemUnderAppealDetails(
    final curam.appeal.facade.struct.AppealRelationshipKey key)
    throws AppException, InformationalException {

    final BDMAppealedCaseSummaryDetails summaryDetails =
      new BDMAppealedCaseSummaryDetails();

    summaryDetails.dtls = AppealedCaseFactory.newInstance().read(key);

    final BDMRFRIssueAppealRelationshipKey key1 =
      new BDMRFRIssueAppealRelationshipKey();
    key1.appealRelationshipID =
      key.appealRelationshipKey.appealRelationshipID;

    final BDMRFRIssueDtlsList issueList = BDMMaintainRFRIssueFactory
      .newInstance().listBDMRFRIssuesByAppealRelationshipID(key1);

    final CaseHeaderDtls caseDtls = getAppealCaseByAppealRelationshipID(
      key.appealRelationshipKey.appealRelationshipID);

    for (final BDMRFRIssueDtls dtls : issueList.dtls) {

      final curam.ca.gc.bdm.facade.bdmrfr.struct.BDMRFRIssueDetails details =
        new curam.ca.gc.bdm.facade.bdmrfr.struct.BDMRFRIssueDetails();

      details.appealRelationshipID = dtls.appealRelationshipID;
      details.bdmRFRIssueID = dtls.bdmRFRIssueID;
      details.issueLevel = dtls.issueLevel;
      details.issueType = dtls.issueType;
      details.status = dtls.status;
      details.decision = dtls.decision;
      details.creationDate = dtls.creationDate;
      details.createdBy = dtls.createdBy;
      details.comments = dtls.comments;
      details.issueEditEnabledInd = true;
      details.issueDeleteEnabledInd = true;
      details.issueCompleteEnabledInd = true;

      // calculate indicator
      if (dtls.status.equals(BDMRFRISSUESTATUS.CANCELLED)
        || dtls.status.equals(BDMRFRISSUESTATUS.COMPLETED)
        || caseDtls.statusCode.equals(CASESTATUS.CLOSED)) {

        details.issueEditEnabledInd = false;
        details.issueCompleteEnabledInd = false;
      }

      if (dtls.status.equals(BDMRFRISSUESTATUS.CANCELLED)
        || caseDtls.statusCode.equals(CASESTATUS.CLOSED)) {
        details.issueDeleteEnabledInd = false;
      }

      summaryDetails.issueDtls.add(details);
    }
    return summaryDetails;
  }

  /**
   *
   * @param appealRelationshipID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private CaseHeaderDtls
    getAppealCaseByAppealRelationshipID(final long appealRelationshipID)
      throws AppException, InformationalException {

    final curam.appeal.sl.entity.struct.AppealRelationshipKey arg0 =
      new curam.appeal.sl.entity.struct.AppealRelationshipKey();
    arg0.appealRelationshipID = appealRelationshipID;
    // get appeal case
    final AppealRelationshipDtls appealRelationshipDtls =
      AppealRelationshipFactory.newInstance().read(arg0);
    final CaseHeaderKey caseKey = new CaseHeaderKey();
    caseKey.caseID = appealRelationshipDtls.appealCaseID;
    final CaseHeaderDtls caseDtls =
      CaseHeaderFactory.newInstance().read(caseKey);
    return caseDtls;
  }

  @Override
  public void modifyResolution(final ModifyResolutionDetails dtls)
    throws AppException, InformationalException {

    final BDMRFRIssueKeyStruct1 issueKeyStruct1 = new BDMRFRIssueKeyStruct1();

    issueKeyStruct1.appealRelationshipID =
      dtls.modifyResolutionDetails.appealRelationshipID;

    final BDMRFRIssueDtlsList rfrIssueDtlsList = BDMRFRIssueFactory
      .newInstance().listIssuesByAppealRelationshipID(issueKeyStruct1);

    boolean isCompletedIssueItem = false;
    boolean isActveIssueItem = false;

    for (final BDMRFRIssueDtls bdmRFRIssueDtls : rfrIssueDtlsList.dtls) {

      // check if atleast one issue item is completed.
      if (BDMRFRISSUESTATUS.COMPLETED.equals(bdmRFRIssueDtls.status)) {
        isCompletedIssueItem = true;
      }

      // check no active items
      if (BDMRFRISSUESTATUS.ACTIVE.equals(bdmRFRIssueDtls.status)) {
        isActveIssueItem = true;
      }

    }

    // check no issue item is active and has atleast one issue completed to
    // modify resolution
    if (!isCompletedIssueItem || isActveIssueItem) {

      throw new AppException(BDMRFRSISSUE.ERR_BDM_RFR_ISSUE_RESOLUTION);
    }

    AppealedCaseFactory.newInstance().modifyResolution(dtls);

    // AppealRelationship Object
    final AppealRelationship appealRelationshipObj =
      AppealRelationshipFactory.newInstance();
    final curam.appeal.sl.entity.struct.AppealRelationshipKey appealRelationshipKey =
      new curam.appeal.sl.entity.struct.AppealRelationshipKey();
    curam.appeal.sl.entity.struct.AppealCaseStatusDetails appealCaseStatusDetails;
    appealRelationshipKey.appealRelationshipID =
      dtls.modifyResolutionDetails.appealRelationshipID;
    appealCaseStatusDetails = appealRelationshipObj
      .readAppealCaseAndAppealStatus(appealRelationshipKey);

    final CaseHeaderKey chKey = new CaseHeaderKey();
    chKey.caseID = appealCaseStatusDetails.appealCaseID;
    final CaseHeaderDtls chDtls = CaseHeaderFactory.newInstance().read(chKey);
    if (!chDtls.statusCode.equals(CASESTATUS.ACTIVE)) {
      chDtls.statusCode = CASESTATUS.ACTIVE;
      CaseHeaderFactory.newInstance().modify(chKey, chDtls);
    }

    final CaseUserRole caseUserRoleObj = CaseUserRoleFactory.newInstance();
    final CaseUserRoleSearchByCaseAndStatusAndTypeAndDate caseUserRoleSearchByCaseAndStatusAndTypeAndDate =
      new CaseUserRoleSearchByCaseAndStatusAndTypeAndDate();
    OrgObjLinkIDAndCaseUserRoleIDList orgObjLinkIDAndCaseUserRoleIDList;
    caseUserRoleSearchByCaseAndStatusAndTypeAndDate.typeCode =
      CASEUSERROLETYPE.HEARINGOFFICIAL;
    caseUserRoleSearchByCaseAndStatusAndTypeAndDate.activeDate =
      Date.getCurrentDate();
    caseUserRoleSearchByCaseAndStatusAndTypeAndDate.caseID =
      appealCaseStatusDetails.appealCaseID;

    orgObjLinkIDAndCaseUserRoleIDList =
      caseUserRoleObj.searchActiveRoleByCaseIDDateAndType(
        caseUserRoleSearchByCaseAndStatusAndTypeAndDate);

    if (orgObjLinkIDAndCaseUserRoleIDList.dtls.size() == 0) {
      final CaseUserRoleDtls caseUserRoleDtls = new CaseUserRoleDtls();
      caseUserRoleDtls.caseID = appealCaseStatusDetails.appealCaseID;
      caseUserRoleDtls.fromDate = Date.getCurrentDate();
      caseUserRoleDtls.recordStatus = RECORDSTATUS.NORMAL;
      caseUserRoleDtls.typeCode = CASEUSERROLETYPE.HEARINGOFFICIAL;

      final UsersKey userKey = new UsersKey();
      userKey.userName = TransactionInfo.getProgramUser();
      final NotFoundIndicator nfi = new NotFoundIndicator();
      OrgObjectLinkDtls orgObjectLinkDtls =
        OrgObjectLinkFactory.newInstance().readByUsername(nfi, userKey);
      if (nfi.isNotFound()) {
        orgObjectLinkDtls = new OrgObjectLinkDtls();
        orgObjectLinkDtls.userName = TransactionInfo.getProgramUser();
        orgObjectLinkDtls.orgObjectType = ORGOBJECTTYPE.USER;
        OrgObjectLinkFactory.newInstance().insert(orgObjectLinkDtls);
      }

      caseUserRoleDtls.orgObjectLinkID = orgObjectLinkDtls.orgObjectLinkID;
      caseUserRoleObj.insert(caseUserRoleDtls);
    }

  }

  /**
   * This method creates attachment and hearing schedule
   *
   * @param dtls
   * @throws AppException
   * @throws InformationalException
   */
  private void postApprove(final AppealedCaseApproveDetails dtls)
    throws AppException, InformationalException {

    // AppealRelationship Object
    final AppealRelationship appealRelationshipObj =
      AppealRelationshipFactory.newInstance();
    final curam.appeal.sl.entity.struct.AppealRelationshipKey appealRelationshipKey =
      new curam.appeal.sl.entity.struct.AppealRelationshipKey();
    curam.appeal.sl.entity.struct.AppealCaseStatusDetails appealCaseStatusDetails;
    appealRelationshipKey.appealRelationshipID =
      dtls.appealedCaseApproveDetails.appealRelationshipID;
    appealCaseStatusDetails = appealRelationshipObj
      .readAppealCaseAndAppealStatus(appealRelationshipKey);

    final Hearing hearingObj = HearingFactory.newInstance();
    // HearingDecision Object
    final HearingDecision hearingDecisionObj =
      HearingDecisionFactory.newInstance();
    final HearingDecisionCaseKey hearingDecisionCaseKey =
      new HearingDecisionCaseKey();
    final CaseAndStatusKey caseAndStatusKey = new CaseAndStatusKey();
    caseAndStatusKey.caseID = appealCaseStatusDetails.appealCaseID;
    caseAndStatusKey.statusCode = HEARINGSTATUS.COMPLETED;
    HearingDtls hearingDtls = new HearingDtls();
    try {
      final HearingKey hearingKey =
        hearingObj.readLatestHearingByCaseAndStatus(caseAndStatusKey);
      hearingDtls = hearingObj.read(hearingKey);
    } catch (final curam.util.exception.RecordNotFoundException e) {

      // hearingDtls.hearingID = 1l;
      hearingDtls.caseID = appealCaseStatusDetails.appealCaseID;
      hearingDtls.versionNo = 1;
      hearingDtls.typeCode = HEARINGTYPE.HEARINGREVIEW;
      hearingDtls.statusCode = HEARINGSTATUS.SCHEDULED;
      hearingDtls.scheduledDateTime = DateTime.getCurrentDateTime();
      hearingObj.insert(hearingDtls);
    }

    // Hearing user role
    final HearingUserRole hearingUserRoleObj =
      HearingUserRoleFactory.newInstance();
    final HearingCaseUserNameDtlsList hearingCaseUserNameDtlsList;
    final HearingUserRoleStatusKey hearingUserRoleStatusKey =
      new HearingUserRoleStatusKey();
    hearingUserRoleStatusKey.hearingID = hearingDtls.hearingID;
    hearingUserRoleStatusKey.recordStatus = RECORDSTATUS.NORMAL;
    hearingCaseUserNameDtlsList = hearingUserRoleObj
      .searchActiveUserNameByHearingIDAndType(hearingUserRoleStatusKey);
    if (hearingCaseUserNameDtlsList.dtls.size() == 0) {

      final CaseUserRole caseUserRoleObj = CaseUserRoleFactory.newInstance();
      final CaseUserRoleSearchByCaseAndStatusAndTypeAndDate caseUserRoleSearchByCaseAndStatusAndTypeAndDate =
        new CaseUserRoleSearchByCaseAndStatusAndTypeAndDate();
      OrgObjLinkIDAndCaseUserRoleIDList orgObjLinkIDAndCaseUserRoleIDList;
      caseUserRoleSearchByCaseAndStatusAndTypeAndDate.typeCode =
        CASEUSERROLETYPE.HEARINGOFFICIAL;
      caseUserRoleSearchByCaseAndStatusAndTypeAndDate.activeDate =
        Date.getCurrentDate();
      caseUserRoleSearchByCaseAndStatusAndTypeAndDate.caseID =
        appealCaseStatusDetails.appealCaseID;

      orgObjLinkIDAndCaseUserRoleIDList =
        caseUserRoleObj.searchActiveRoleByCaseIDDateAndType(
          caseUserRoleSearchByCaseAndStatusAndTypeAndDate);

      final CaseUserRoleDtls caseUserRoleDtls = new CaseUserRoleDtls();
      if (orgObjLinkIDAndCaseUserRoleIDList.dtls.size() == 0) {

        caseUserRoleDtls.caseID = appealCaseStatusDetails.appealCaseID;
        caseUserRoleDtls.fromDate = Date.getCurrentDate();
        caseUserRoleDtls.recordStatus = RECORDSTATUS.NORMAL;
        caseUserRoleDtls.typeCode = CASEUSERROLETYPE.HEARINGOFFICIAL;

        final UsersKey userKey = new UsersKey();
        userKey.userName = TransactionInfo.getProgramUser();
        final NotFoundIndicator nfi = new NotFoundIndicator();
        OrgObjectLinkDtls orgObjectLinkDtls =
          OrgObjectLinkFactory.newInstance().readByUsername(nfi, userKey);
        if (nfi.isNotFound()) {
          orgObjectLinkDtls = new OrgObjectLinkDtls();
          orgObjectLinkDtls.userName = TransactionInfo.getProgramUser();
          orgObjectLinkDtls.orgObjectType = ORGOBJECTTYPE.USER;
          OrgObjectLinkFactory.newInstance().insert(orgObjectLinkDtls);
        }

        caseUserRoleDtls.orgObjectLinkID = orgObjectLinkDtls.orgObjectLinkID;
        caseUserRoleObj.insert(caseUserRoleDtls);

      }

      final HearingUserRoleDtls hearingUserRoleDtls =
        new HearingUserRoleDtls();
      hearingUserRoleDtls.hearingID = hearingDtls.hearingID;

      caseUserRoleSearchByCaseAndStatusAndTypeAndDate.typeCode =
        CASEUSERROLETYPE.HEARINGOFFICIAL;
      caseUserRoleSearchByCaseAndStatusAndTypeAndDate.activeDate =
        Date.getCurrentDate();
      caseUserRoleSearchByCaseAndStatusAndTypeAndDate.caseID =
        appealCaseStatusDetails.appealCaseID;
      hearingUserRoleDtls.caseUserRoleID = caseUserRoleDtls.caseUserRoleID;
      hearingUserRoleDtls.participatedCode =
        HEARINGPARTICIPATION.PARTICIPATED;
      hearingUserRoleDtls.versionNo = 1;
      hearingUserRoleObj.insert(hearingUserRoleDtls);

      final HearingKey hearingKey = new HearingKey();
      hearingKey.hearingID = hearingDtls.hearingID;
      final ModifyStatusCodesDetails hearingModifyStatusDtls =
        new ModifyStatusCodesDetails();
      hearingModifyStatusDtls.statusCode = HEARINGSTATUS.COMPLETED;
      hearingObj.modifyStatus(hearingKey, hearingModifyStatusDtls);
    }

    DecisionDateDetails decisionDetail = new DecisionDateDetails();
    final HearingDecisionDtls hearingDecisionDtls = new HearingDecisionDtls();
    hearingDecisionCaseKey.caseID = appealCaseStatusDetails.appealCaseID;
    try {
      hearingDecisionObj.readStatusByCase(hearingDecisionCaseKey);
      decisionDetail = hearingDecisionObj
        .readDecisionIDDateAndVersionByCase(hearingDecisionCaseKey);
    } catch (final curam.util.exception.RecordNotFoundException e) {
      hearingDecisionDtls.caseID = appealCaseStatusDetails.appealCaseID;
      hearingDecisionDtls.decisionStatus = HEARINGDECISIONSTATUS.INPROGRESS;
      hearingDecisionDtls.resolutionCode = APPEALCASERESOLUTION.ACCEPTED;
      hearingDecisionDtls.decisionDate = Date.getCurrentDate();
      hearingDecisionDtls.versionNo = 1;
      hearingDecisionObj.insert(hearingDecisionDtls);
    }

    // HearingDecision Attachment Link object and structs
    final HearingDecisionAttachmentLink hearingDecisionAttachmentLinkObj =
      HearingDecisionAttachmentLinkFactory.newInstance();
    final ActiveDecisionAttachmentsKey activeDecisionAttachmentsKey =
      new ActiveDecisionAttachmentsKey();
    AppealCountDetails appealCountDetails;
    // Check for active decision documents

    if (decisionDetail.hearingDecisionID != 0) {
      activeDecisionAttachmentsKey.hearingDecisionID =
        decisionDetail.hearingDecisionID;
    } else {

      activeDecisionAttachmentsKey.hearingDecisionID =
        hearingDecisionDtls.hearingDecisionID;
    }
    appealCountDetails = hearingDecisionAttachmentLinkObj
      .countActiveAttachmentsByDecision(activeDecisionAttachmentsKey);
    if (appealCountDetails.recordCount == 0) {
      final HearingDecisionAttachmentLinkDtls hearingDecisionAttLinkDtls =
        new HearingDecisionAttachmentLinkDtls();

      if (decisionDetail.hearingDecisionID != 0) {
        hearingDecisionAttLinkDtls.hearingDecisionID =
          decisionDetail.hearingDecisionID;
      } else {

        hearingDecisionAttLinkDtls.hearingDecisionID =
          hearingDecisionDtls.hearingDecisionID;
      }

      hearingDecisionAttLinkDtls.recordStatus = RECORDSTATUS.NORMAL;
      hearingDecisionAttLinkDtls.versionNo = 1;
      hearingDecisionAttLinkDtls.attachmentID = 4001l;

      final AttachmentLinkDtls attLinkDtls = new AttachmentLinkDtls();
      attLinkDtls.attachmentID = 4001l;
      attLinkDtls.recordStatus = RECORDSTATUS.NORMAL;

      if (decisionDetail.hearingDecisionID != 0) {
        attLinkDtls.relatedObjectID = decisionDetail.hearingDecisionID;
      } else {

        attLinkDtls.relatedObjectID = hearingDecisionDtls.hearingDecisionID;
      }

      attLinkDtls.relatedObjectType =
        ATTACHMENTOBJECTLINKTYPE.EXTERNALDOCUMENT;
      attLinkDtls.creatorUserName = TransactionInfo.getProgramUser();
      attLinkDtls.description = "Bypass attachment Link";
      AttachmentLinkFactory.newInstance().insert(attLinkDtls);
      hearingDecisionAttLinkDtls.attachmentLinkID =
        attLinkDtls.attachmentLinkID;
      hearingDecisionAttLinkDtls.decisionAttachmentType =
        DECISIONATTACHMENTTYPE.EXTERNAL;
      hearingDecisionAttachmentLinkObj.insert(hearingDecisionAttLinkDtls);
    }

  }

  /**
   * This method is used to approve an appealed case
   *
   * @param dtls AppealedCaseApproveDetails
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void approve(final AppealedCaseApproveDetails dtls)
    throws AppException, InformationalException {

    final AppealRelationshipKey appealRelationshipKey =
      new AppealRelationshipKey();
    appealRelationshipKey.appealRelationshipID =
      dtls.appealedCaseApproveDetails.appealRelationshipID;
    final AppealRelationshipDtls appealRelationshipDtls =
      AppealRelationshipFactory.newInstance().read(appealRelationshipKey);

    final curam.piwrapper.caseheader.impl.CaseHeader caseHeader =
      caseHeaderDAO.get(appealRelationshipDtls.caseID);

    // set search key
    final BDMRFRIssueKeyStruct1 key1 = new BDMRFRIssueKeyStruct1();
    key1.appealRelationshipID =
      dtls.appealedCaseApproveDetails.appealRelationshipID;
    // return the list
    final BDMRFRIssueDtlsList issuesList =
      BDMRFRIssueFactory.newInstance().listIssuesByAppealRelationshipID(key1);

    // validate approve action
    validateApprove(issuesList);

    final AppealedCaseApproval appealedCaseApproval =
      AppealedCaseApprovalFactory.newInstance();
    appealedCaseApproval.approve(dtls);

    // Close tasks RFR Online Appeal Request Task and Documentation Task
    final Event event = new Event();
    event.eventKey = BDMITEMUNDERAPPEAL.ONLINEAPPEALREQUESTTASKCLOSE;
    event.primaryEventData =
      Long.valueOf(caseHeader.getConcernRole().getID());
    EventService.raiseEvent(event);

    // create level1 and leve2 tasks absed on the BDMRfr issues
    this.createLevel1OrLevel2Tasks(issuesList, appealRelationshipDtls,
      caseHeader);

    // create attachment and hearing schedule
    postApprove(dtls);
  }

  /**
   * This method is to create Level1 or Level2 or Both RFR Reconsideration Tasks
   *
   * @param dtls AppealedCaseApproveDetails
   * @throws AppException
   * @throws InformationalException
   */
  private void createLevel1OrLevel2Tasks(final BDMRFRIssueDtlsList issuesList,
    final AppealRelationshipDtls appealRelationshipDtls,
    final curam.piwrapper.caseheader.impl.CaseHeader caseHeader)
    throws AppException, InformationalException {

    boolean createLevelOneTask = false;
    boolean createLevelTwoTask = false;
    String levelDesc = "";

    for (final BDMRFRIssueDtls issueDtls : issuesList.dtls) {
      if (BDMRFRISSUELEVEL.LEVELONE.equals(issueDtls.issueLevel)) {
        createLevelOneTask = true;
      } else if (BDMRFRISSUELEVEL.LEVELTWO.equals(issueDtls.issueLevel)) {
        createLevelTwoTask = true;
      }
    }
    final BDMTaskSkillTypeKey skillType = new BDMTaskSkillTypeKey();

    if (createLevelOneTask) {
      skillType.skillType = this.getSkillTypeBasedOnItemsUnderAppeal(
        appealRelationshipDtls, BDMRFRISSUELEVEL.LEVELONE, caseHeader);
      levelDesc = BDMUtil.getCodeTableDescriptionForUserLocale(
        BDMRFRISSUELEVEL.TABLENAME, BDMRFRISSUELEVEL.LEVELONE)
        + CuramConst.gkSpace;
      this.createTask(skillType, caseHeader, levelDesc,
        appealRelationshipDtls);

    }

    if (createLevelTwoTask) {
      skillType.skillType = this.getSkillTypeBasedOnItemsUnderAppeal(
        appealRelationshipDtls, BDMRFRISSUELEVEL.LEVELTWO, caseHeader);
      levelDesc = BDMUtil.getCodeTableDescriptionForUserLocale(
        BDMRFRISSUELEVEL.TABLENAME, BDMRFRISSUELEVEL.LEVELTWO);
      this.createTask(skillType, caseHeader, levelDesc,
        appealRelationshipDtls);

    }

  }

  private void createTask(final BDMTaskSkillTypeKey skillType,
    final curam.piwrapper.caseheader.impl.CaseHeader caseHeader,
    final String levelDesc,
    final AppealRelationshipDtls appealRelationshipDtls)
    throws AppException, InformationalException {

    final BDMVSGTaskCreateDetails bdmVSGTaskCreateDetails =
      new BDMVSGTaskCreateDetails();
    bdmVSGTaskCreateDetails.workQueueID = BDMWorkAllocationTaskFactory
      .newInstance().getWorkQueueIDBySkillType(skillType).workQueueID;

    final BDMTaskSkillTypeList bdmTaskSkillTypeList =
      BDMWorkAllocationTaskFactory.newInstance()
        .searchBDMTaskSkillType(skillType);

    if (bdmTaskSkillTypeList.dtls.size() > 0) {
      bdmVSGTaskCreateDetails.bdmTaskClassificationID =
        bdmTaskSkillTypeList.dtls.get(0).bdmTaskClassificationID;
    }

    /*
     * final String localisableString =
     * caseHeader.getAdminCaseConfiguration().getCaseConfigurationName()
     * .toString() + "|" + caseHeader.getCaseReference();
     */

    final String localisableString = caseHeader.getAdminCaseConfiguration()
      .getCaseConfigurationName().toString();

    bdmVSGTaskCreateDetails.participantRoleID =
      caseHeader.getConcernRole().getID();
    bdmVSGTaskCreateDetails.caseID = appealRelationshipDtls.appealCaseID;
    bdmVSGTaskCreateDetails.deadlineDateTime =
      Date.getCurrentDate().addDays(7).getDateTime();

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

    // Task 58975 Add client oral language in task subject
    bdmVSGTaskCreateDetails.subject =
      BDMUtil.getCodeTableDescriptionForUserLocale(SKILLTYPE.TABLENAME,
        skillType.skillType) + CuramConst.gkSpace + orallanuage
        + CuramConst.gkSpace + "Perform a Reconsideration for"
        + CuramConst.gkSpace + localisableString + CuramConst.gkSpace
        + levelDesc + CuramConst.gkSpace
        + caseHeader.getConcernRole().getName() + CuramConst.gkSpace
        + caseHeader.getConcernRole().getPrimaryAlternateID()
        + CuramConst.gkSpace + "for " + CuramConst.gkSpace
        + caseHeader.getCaseReference();

    final List<Object> enactmentStructs = new ArrayList();
    enactmentStructs.add(bdmVSGTaskCreateDetails);

    String workflowDisabled =
      Configuration.getProperty(EnvVars.ENV_ENACT_WORKFLOW_PROCESS_DISABLED);

    if (workflowDisabled == null)
      workflowDisabled = "NO";

    if (workflowDisabled.equalsIgnoreCase(EnvVars.ENV_VALUE_NO)) {

      EnactmentService.startProcess("BDMItemsUnderAppealLevel1OrLevel2Task",
        enactmentStructs);
    }
  }

  /**
   * This method is to get a skill type for the task
   *
   * @param dtls AppealedCaseApproveDetails
   * @throws AppException
   * @throws InformationalException
   */

  private String getSkillTypeBasedOnItemsUnderAppeal(
    final AppealRelationshipDtls appealRelationshipDtls,
    final String issueLevel,
    final curam.piwrapper.caseheader.impl.CaseHeader caseHeader)
    throws AppException, InformationalException {

    String skillType = SKILLTYPE.VSG07;

    if (CASETYPECODE.APPLICATION_CASE
      .equals(caseHeader.getCaseType().getCode())) {
      if (BDMRFRISSUELEVEL.LEVELONE.equals(issueLevel)) {
        skillType = SKILLTYPE.VSG03;
      } else if (BDMRFRISSUELEVEL.LEVELTWO.equals(issueLevel)) {
        skillType = SKILLTYPE.VSG02;
      }
    } else if (CASETYPECODE.PRODUCTDELIVERY
      .equals(caseHeader.getCaseType().getCode())) {
      final ProductDeliveryKey productDeliveryKey = new ProductDeliveryKey();
      productDeliveryKey.caseID = caseHeader.getID();
      final ProductDeliveryDtls productDeliveryDtls =
        ProductDeliveryFactory.newInstance().read(productDeliveryKey);
      final BDMProductDetailsCalculatorBase pdDetailsCalculator =
        pdDetailsCalculatorMap.get(productDeliveryDtls.productID);
      if (pdDetailsCalculator != null) {
        skillType =
          pdDetailsCalculator.getSkillTypeBasedOnProduct(issueLevel);
      }
    }
    return skillType;

  }

  /**
   * This method is validate approve appealed case
   *
   * @param dtls AppealedCaseApproveDetails
   * @throws AppException
   * @throws InformationalException
   */
  private void validateApprove(final BDMRFRIssueDtlsList issuesList)
    throws AppException, InformationalException {

    // validate-There must be at least 1 active Issue in order for the Item
    // Under Appeal to be approved.
    boolean noActiveIssueIndicator = true;

    if (!issuesList.dtls.isEmpty()) {

      for (final BDMRFRIssueDtls issue : issuesList.dtls) {
        if (issue.status.equals(BDMRFRISSUESTATUS.ACTIVE)) {
          noActiveIssueIndicator = false;
          break;
        }
      }
    }

    if (noActiveIssueIndicator) {
      throw new AppException(
        BDMRFRSISSUE.ERR_BDM_RFR_ISSUE_NOT_ACTIVE_CANNOT_APPROVE);
    }
  }
}
