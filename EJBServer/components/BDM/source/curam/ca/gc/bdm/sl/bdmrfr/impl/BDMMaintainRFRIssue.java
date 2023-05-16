package curam.ca.gc.bdm.sl.bdmrfr.impl;

import curam.appeal.facade.struct.AppealRelationshipKey;
import curam.appeal.sl.entity.fact.AppealRelationshipFactory;
import curam.appeal.sl.entity.struct.AppealRelationshipDtls;
import curam.ca.gc.bdm.codetable.BDMRFRISSUEDECISION;
import curam.ca.gc.bdm.codetable.BDMRFRISSUESTATUS;
import curam.ca.gc.bdm.codetable.BDMRFRISSUETYPE;
import curam.ca.gc.bdm.entity.fact.BDMRFRIssueFactory;
import curam.ca.gc.bdm.entity.intf.BDMRFRIssue;
import curam.ca.gc.bdm.entity.struct.BDMRFRIssueDtls;
import curam.ca.gc.bdm.entity.struct.BDMRFRIssueDtlsList;
import curam.ca.gc.bdm.entity.struct.BDMRFRIssueKey;
import curam.ca.gc.bdm.entity.struct.BDMRFRIssueKeyStruct1;
import curam.ca.gc.bdm.facade.bdmrfr.fact.BDMAppealedCaseFactory;
import curam.ca.gc.bdm.facade.bdmrfr.intf.BDMAppealedCase;
import curam.ca.gc.bdm.facade.bdmrfr.struct.BDMAppealedCaseSummaryDetails;
import curam.ca.gc.bdm.message.BDMRFRSISSUE;
import curam.ca.gc.bdm.sl.bdmrfr.struct.BDMRFRIssueAppealRelationshipKey;
import curam.ca.gc.bdm.sl.bdmrfr.struct.BDMRFRIssueDecisionDetails;
import curam.ca.gc.bdm.sl.bdmrfr.struct.BDMRFRIssueDetails;
import curam.codetable.APPEALRELATIONSHIPSTATUS;
import curam.codetable.CASESTATUS;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.CodeTable;
import curam.util.type.Date;

public class BDMMaintainRFRIssue
  extends curam.ca.gc.bdm.sl.bdmrfr.base.BDMMaintainRFRIssue {

  @Override
  public void createBDMRFRIssue(final BDMRFRIssueDetails details)
    throws AppException, InformationalException {

    // validate- Appeal Case is Not Closed
    final AppealRelationshipDtls caseHeaderDtls =
      getAppealCaseByAppealRelationshipID(details.dtls.appealRelationshipID);
    validateBDMRFRIssues(details);
    if (caseHeaderDtls.statusCode.equals(CASESTATUS.CLOSED)) {
      throw new AppException(BDMRFRSISSUE.ERR_BDM_RFR_APPEAL_CASE_CLOSED);
    }

    // set the hierarchichal code table values
    final String issueLevel = CodeTable
      .getParentCode(BDMRFRISSUETYPE.TABLENAME, details.dtls.issueType);
    details.dtls.issueLevel = issueLevel;
    details.dtls.decision = BDMRFRISSUEDECISION.PENDING;
    details.dtls.status = BDMRFRISSUESTATUS.ACTIVE;
    details.dtls.creationDate = Date.getCurrentDate();

    // set the ID
    details.dtls.bdmRFRIssueID = UniqueIDFactory.newInstance().getNextID();
    // call insert on entity
    BDMRFRIssueFactory.newInstance().insert(details.dtls);
  }

  @Override
  public void modifyBDMRFRIssue(final BDMRFRIssueDetails details)
    throws AppException, InformationalException {

    // read the details
    final BDMRFRIssue issueObj = BDMRFRIssueFactory.newInstance();
    final BDMRFRIssueKey key = new BDMRFRIssueKey();
    key.bdmRFRIssueID = details.dtls.bdmRFRIssueID;
    final BDMRFRIssueDtls issueDtls = issueObj.read(key);
    // set the hierarchical code table values
    final String issueLevel = CodeTable
      .getParentCode(BDMRFRISSUETYPE.TABLENAME, details.dtls.issueType);
    issueDtls.issueLevel = issueLevel;
    issueDtls.issueType = details.dtls.issueType;
    issueDtls.comments = details.dtls.comments;
    // call entity modify
    issueObj.modify(key, issueDtls);
  }

  @Override
  public BDMRFRIssueDtlsList listBDMRFRIssuesByAppealRelationshipID(
    final BDMRFRIssueAppealRelationshipKey key)
    throws AppException, InformationalException {

    // set search key
    final BDMRFRIssueKeyStruct1 key1 = new BDMRFRIssueKeyStruct1();
    key1.appealRelationshipID = key.appealRelationshipID;
    // return the list
    return BDMRFRIssueFactory.newInstance()
      .listIssuesByAppealRelationshipID(key1);
  }

  @Override
  public BDMRFRIssueDetails readBDMRFRIssueDetails(
    final curam.ca.gc.bdm.sl.bdmrfr.struct.BDMRFRIssueKey key)
    throws AppException, InformationalException {

    final BDMRFRIssueDetails details = new BDMRFRIssueDetails();
    final BDMRFRIssueKey key1 = new BDMRFRIssueKey();
    key1.bdmRFRIssueID = key.bdmRFRIssueID;
    details.dtls = BDMRFRIssueFactory.newInstance().read(key1);
    return details;
  }

  /**
   *
   * @param details
   */
  private void validateBDMRFRIssues(final BDMRFRIssueDetails details)
    throws AppException, InformationalException {

    final AppealRelationshipDtls caseDtls =
      getAppealCaseByAppealRelationshipID(details.dtls.appealRelationshipID);

    final CaseHeaderKey caseKey = new CaseHeaderKey();
    caseKey.caseID = caseDtls.appealCaseID;
    final CaseHeaderDtls caseHeaderDtls =
      CaseHeaderFactory.newInstance().read(caseKey);

    if (CASESTATUS.CLOSED.equals(caseHeaderDtls.statusCode)) {
      throw new AppException(BDMRFRSISSUE.ERR_BDM_RFR_APPEAL_CASE_CLOSED);

    }
  }

  /**
   *
   * @param appealRelationshipID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private AppealRelationshipDtls
    getAppealCaseByAppealRelationshipID(final long appealRelationshipID)
      throws AppException, InformationalException {

    final curam.appeal.sl.entity.struct.AppealRelationshipKey arg0 =
      new curam.appeal.sl.entity.struct.AppealRelationshipKey();
    arg0.appealRelationshipID = appealRelationshipID;
    // get appeal case
    final AppealRelationshipDtls appealRelationshipDtls =
      AppealRelationshipFactory.newInstance().read(arg0);
    return appealRelationshipDtls;
  }

  /**
   * This method modifies the Issue Decision
   *
   * @param details BDMRFRIssueDecisionDetails
   * @throws AppException, InformationalException
   * @since Task-56872
   */
  @Override
  public void
    modifyBDMRFRIssueDecision(final BDMRFRIssueDecisionDetails details)
      throws AppException, InformationalException {

    // read the details
    final BDMRFRIssue issueObj = BDMRFRIssueFactory.newInstance();
    final BDMRFRIssueKey key = new BDMRFRIssueKey();
    key.bdmRFRIssueID = details.bdmRFRIssueID;
    final BDMRFRIssueDtls issueDtls = issueObj.read(key);

    // validate edit action
    validateEditIssueDecision(details, issueDtls);

    // set user provided values
    issueDtls.decision = details.decision;
    issueDtls.correspondenceComments = details.correspondenceComments;
    issueDtls.decisionRationale = details.decisionRationale;

    // call entity modify
    issueObj.modify(key, issueDtls);

  }

  /**
   * @param details
   * @param issueDtls
   * @throws AppException
   * @throws InformationalException
   * @since Task-56872
   */
  private void validateEditIssueDecision(
    final BDMRFRIssueDecisionDetails details, final BDMRFRIssueDtls issueDtls)
    throws AppException, InformationalException {

    // validate Item Under Appeal is Approved
    final BDMAppealedCase bdmAppealedCase =
      BDMAppealedCaseFactory.newInstance();
    final AppealRelationshipKey appealRelationshipKey =
      new AppealRelationshipKey();
    appealRelationshipKey.appealRelationshipKey.appealRelationshipID =
      issueDtls.appealRelationshipID;
    final BDMAppealedCaseSummaryDetails appealItem =
      bdmAppealedCase.readItemUnderAppealDetails(appealRelationshipKey);

    if (!appealItem.dtls.appealedCaseSummaryDetails.readSummaryDetails.statusCode
      .equals(APPEALRELATIONSHIPSTATUS.APPROVED)) {
      throw new AppException(
        BDMRFRSISSUE.ERR_BDM_RFR_ISSUE_ITEM_NOT_APPROVED);
    }

    // validate is Issue status active
    if (!issueDtls.status.equals(BDMRFRISSUESTATUS.ACTIVE)) {
      throw new AppException(BDMRFRSISSUE.ERR_BDM_RFR_ISSUE_NOT_ACTIVE);
    }
  }
}
