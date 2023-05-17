package curam.ca.gc.bdm.facade.bdmrfr.impl;

import com.google.inject.Inject;
import curam.appeal.facade.fact.DecisionApprovalFactory;
import curam.appeal.facade.intf.DecisionApproval;
import curam.appeal.facade.struct.HearingDecisionVersionKey;
import curam.appeal.sl.entity.fact.AppealRelationshipFactory;
import curam.appeal.sl.entity.fact.HearingDecisionFactory;
import curam.appeal.sl.entity.intf.AppealRelationship;
import curam.appeal.sl.entity.struct.AppealCaseID;
import curam.appeal.sl.entity.struct.AppealRelationShipDetails;
import curam.appeal.sl.entity.struct.AppealRelationShipDetailsList;
import curam.appeal.sl.entity.struct.HearingDecisionDtls;
import curam.appeal.sl.entity.struct.HearingDecisionKey;
import curam.ca.gc.bdm.codetable.BDMRFRISSUELEVEL;
import curam.ca.gc.bdm.communication.impl.BDMCommunicationImpl;
import curam.ca.gc.bdm.entity.fact.BDMRFRIssueFactory;
import curam.ca.gc.bdm.entity.struct.BDMRFRIssueDtls;
import curam.ca.gc.bdm.entity.struct.BDMRFRIssueDtlsList;
import curam.ca.gc.bdm.entity.struct.BDMRFRIssueKeyStruct1;
import curam.ca.gc.bdm.events.BDMITEMUNDERAPPEAL;
import curam.codetable.HEARINGDECISIONSTATUS;
import curam.codetable.RECORDSTATUS;
import curam.util.events.impl.EventService;
import curam.util.events.struct.Event;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;

public class BDMDecisionApproval
  implements curam.ca.gc.bdm.facade.bdmrfr.intf.BDMDecisionApproval {

  @Inject
  private BDMCommunicationImpl bdmCommunicationImpl;

  public BDMDecisionApproval() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public void submit(final HearingDecisionVersionKey details)
    throws AppException, InformationalException {

    final DecisionApproval decisionApproval =
      DecisionApprovalFactory.newInstance();
    decisionApproval.submit(details);

    final HearingDecisionKey hearingDecisionKey = new HearingDecisionKey();
    hearingDecisionKey.hearingDecisionID =
      details.hearingDecisionVersionKey.hearingDecisionID;
    HearingDecisionDtls hearingDecisionDtls =
      HearingDecisionFactory.newInstance().read(hearingDecisionKey);

    if (!hearingDecisionDtls.decisionStatus
      .equals(HEARINGDECISIONSTATUS.APPROVED)) {
      // Task-61691 approve and close hearing case
      details.hearingDecisionVersionKey.versionNo =
        hearingDecisionDtls.versionNo;
      decisionApproval.approve(details);
    }

    hearingDecisionDtls =
      HearingDecisionFactory.newInstance().read(hearingDecisionKey);

    // AppealRelationship object and structs
    final AppealRelationship appealRelationshipObj =
      AppealRelationshipFactory.newInstance();
    final AppealCaseID appealCaseID = new AppealCaseID();

    // Check for any 'not decided' resolutions
    appealCaseID.appealCaseID = hearingDecisionDtls.caseID;

    final AppealRelationShipDetailsList appealRelationshipDetailsLIst =
      appealRelationshipObj.searchByAppealCase(appealCaseID);

    for (final AppealRelationShipDetails appealRelationshipDetails : appealRelationshipDetailsLIst.dtls
      .items()) {

      if (appealRelationshipDetails.recordStatus
        .equals(RECORDSTATUS.NORMAL)) {
        final BDMRFRIssueKeyStruct1 bdmRFRIssueKey =
          new BDMRFRIssueKeyStruct1();
        bdmRFRIssueKey.appealRelationshipID =
          appealRelationshipDetails.appealRelationshipID;
        final BDMRFRIssueDtlsList bdmRFRIssueDtlsList = BDMRFRIssueFactory
          .newInstance().listIssuesByAppealRelationshipID(bdmRFRIssueKey);

        bdmCommunicationImpl
          .createRFRCorrespondenceTrigger(hearingDecisionDtls.caseID);

        // TASK-61692 Close the level 1 and 2 tasks
        final Event event = new Event();
        for (final BDMRFRIssueDtls issueDtls : bdmRFRIssueDtlsList.dtls
          .items()) {
          if (BDMRFRISSUELEVEL.LEVELONE.equals(issueDtls.issueLevel)) {
            event.eventKey = BDMITEMUNDERAPPEAL.LEVELONETASKCLOSE;
          } else if (BDMRFRISSUELEVEL.LEVELTWO.equals(issueDtls.issueLevel)) {
            event.eventKey = BDMITEMUNDERAPPEAL.LEVELTWOTASKCLOSE;
          }

          event.primaryEventData =
            Long.valueOf(appealRelationshipDetails.appealCaseID);
          EventService.raiseEvent(event);
        }
      }
    }
  }

}
