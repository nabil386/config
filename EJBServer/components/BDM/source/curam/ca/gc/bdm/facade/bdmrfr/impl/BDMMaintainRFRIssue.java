package curam.ca.gc.bdm.facade.bdmrfr.impl;

import curam.ca.gc.bdm.codetable.BDMRFRISSUEDECISION;
import curam.ca.gc.bdm.codetable.BDMRFRISSUESTATUS;
import curam.ca.gc.bdm.entity.fact.BDMRFRIssueFactory;
import curam.ca.gc.bdm.entity.struct.BDMRFRIssueDtlsList;
import curam.ca.gc.bdm.message.BDMRFRSISSUE;
import curam.ca.gc.bdm.sl.bdmrfr.fact.BDMMaintainRFRIssueFactory;
import curam.ca.gc.bdm.sl.bdmrfr.struct.BDMRFRIssueAppealRelationshipKey;
import curam.ca.gc.bdm.sl.bdmrfr.struct.BDMRFRIssueDecisionDetails;
import curam.ca.gc.bdm.sl.bdmrfr.struct.BDMRFRIssueDetails;
import curam.ca.gc.bdm.sl.bdmrfr.struct.BDMRFRIssueKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.StringUtil;

public class BDMMaintainRFRIssue
  extends curam.ca.gc.bdm.facade.bdmrfr.base.BDMMaintainRFRIssue {

  @Override
  public void createBDMRFRIssue(final BDMRFRIssueDetails details)
    throws AppException, InformationalException {

    BDMMaintainRFRIssueFactory.newInstance().createBDMRFRIssue(details);

  }

  @Override
  public void modifyBDMRFRIssue(final BDMRFRIssueDetails details)
    throws AppException, InformationalException {

    BDMMaintainRFRIssueFactory.newInstance().modifyBDMRFRIssue(details);

  }

  @Override
  public BDMRFRIssueDtlsList listBDMRFRIssuesByAppealRelationshipID(
    final BDMRFRIssueAppealRelationshipKey key)
    throws AppException, InformationalException {

    return BDMMaintainRFRIssueFactory.newInstance()
      .listBDMRFRIssuesByAppealRelationshipID(key);
  }

  @Override
  public void deleteBDMRFRIssue(final BDMRFRIssueDetails details)
    throws AppException, InformationalException {

    // set the status to cancelled and call modify
    final curam.ca.gc.bdm.entity.struct.BDMRFRIssueKey key1 =
      new curam.ca.gc.bdm.entity.struct.BDMRFRIssueKey();
    key1.bdmRFRIssueID = details.dtls.bdmRFRIssueID;
    details.dtls = BDMRFRIssueFactory.newInstance().read(key1);
    details.dtls.status = BDMRFRISSUESTATUS.CANCELLED;
    BDMRFRIssueFactory.newInstance().modify(key1, details.dtls);
  }

  @Override
  public BDMRFRIssueDetails readBDMRFRIssueDetails(final BDMRFRIssueKey key)
    throws AppException, InformationalException {

    return BDMMaintainRFRIssueFactory.newInstance()
      .readBDMRFRIssueDetails(key);
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

    BDMMaintainRFRIssueFactory.newInstance()
      .modifyBDMRFRIssueDecision(details);

  }

  /**
   * This method Marks the Issue to complete provided Key
   */
  @Override
  public void completeBDMRFRIssue(final BDMRFRIssueDetails details)
    throws AppException, InformationalException {

    // set the status to cancelled and call modify
    final curam.ca.gc.bdm.entity.struct.BDMRFRIssueKey key1 =
      new curam.ca.gc.bdm.entity.struct.BDMRFRIssueKey();
    key1.bdmRFRIssueID = details.dtls.bdmRFRIssueID;
    details.dtls = BDMRFRIssueFactory.newInstance().read(key1);

    // if the current Issue Decision = ‘Pending’ and the Agent selects to
    // ‘Complete’ the Issue, the System displays the error message. Issue cannot
    // be completed since Issue Decision is not recorded.
    if (StringUtil.isNullOrEmpty(details.dtls.decision)
      || BDMRFRISSUEDECISION.PENDING.equals(details.dtls.decision)) {

      throw new AppException(BDMRFRSISSUE.ERR_BDM_RFR_ISSUE_DECISION_PENDING);
    }

    // Issue cannot be completed since it is cancelled.
    if (BDMRFRISSUESTATUS.CANCELLED.equals(details.dtls.status)) {

      throw new AppException(
        BDMRFRSISSUE.ERR_BDM_RFR_ISSUE_CANCELLED_CANNOT_COMPLETED);
    }

    details.dtls.status = BDMRFRISSUESTATUS.COMPLETED;
    BDMRFRIssueFactory.newInstance().modify(key1, details.dtls);
  }

}
