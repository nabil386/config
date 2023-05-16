package curam.ca.gc.bdm.sl.organization.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.entity.fact.BDMFEWorkQueueCountryLinkFactory;
import curam.ca.gc.bdm.entity.fec.fact.BDMFECaseFactory;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseDtls;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseKey;
import curam.ca.gc.bdm.entity.struct.BDMFEWorkQueueCountryLinkKey;
import curam.ca.gc.bdm.sl.fact.BDMCaseUserRoleFactory;
import curam.codetable.CASEREASSIGNREASON;
import curam.codetable.ORGOBJECTTYPE;
import curam.core.impl.CuramConst;
import curam.core.sl.entity.struct.OrgObjectLinkDtls;
import curam.core.sl.struct.CaseIDKey;
import curam.core.sl.struct.ReadWorkQueueDetails;
import curam.core.sl.struct.ReadWorkQueueKey;
import curam.core.sl.struct.ReasonEndDateComments;
import curam.core.sl.struct.SendNotificationInd;
import curam.core.struct.CaseHeaderKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import curam.util.type.NotFoundIndicator;

/**
 *
 * @author prashant.raut
 *
 * TASK 54344, 89066 Manage Allocation - R1
 *
 * Assign FE integrated case based on province and country.
 *
 *
 */
public class BDMFECaseOwnershipStrategy
  extends curam.ca.gc.bdm.sl.organization.base.BDMFECaseOwnershipStrategy {

  private final BDMUtil bdmUtil = new BDMUtil();

  @Override
  public void assignCase(final CaseIDKey chkey)
    throws AppException, InformationalException {

    final ReadWorkQueueKey readWorkQueueKey = new ReadWorkQueueKey();
    final OrgObjectLinkDtls orgObjectLinkDtls = new OrgObjectLinkDtls();
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();

    final curam.core.sl.intf.CaseUserRole caseUserRoleObj =
      curam.core.sl.fact.CaseUserRoleFactory.newInstance();

    final curam.ca.gc.bdm.entity.intf.BDMFEWorkQueueCountryLink bdmWorkQueueCountryLink =
      BDMFEWorkQueueCountryLinkFactory.newInstance();

    final BDMFEWorkQueueCountryLinkKey bdmFEWorkQueueCountryLinkKey =
      new BDMFEWorkQueueCountryLinkKey();
    final curam.ca.gc.bdm.entity.fec.intf.BDMFECase bdmfeCase =
      BDMFECaseFactory.newInstance();
    final BDMFECaseKey bdmfeCaseKey = new BDMFECaseKey();
    bdmfeCaseKey.caseID = chkey.caseID;
    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    final BDMFECaseDtls feCaseDtls =
      bdmfeCase.read(nfIndicator, bdmfeCaseKey);
    boolean fecCaseFoundInd = false;

    if (!nfIndicator.isNotFound()) {
      // get country code and read work queue id based on country.
      bdmFEWorkQueueCountryLinkKey.countryCode = feCaseDtls.countryCode;
      final NotFoundIndicator nfCountryIndicator = new NotFoundIndicator();
      final curam.ca.gc.bdm.entity.struct.BDMFEWorkQueueCountryLinkDtls countryLinkDtls =
        bdmWorkQueueCountryLink.read(nfCountryIndicator,
          bdmFEWorkQueueCountryLinkKey);
      if (!nfCountryIndicator.isNotFound()) {
        readWorkQueueKey.key.workQueueID = countryLinkDtls.workQueueID;
        fecCaseFoundInd = true;
      }
    }
    // if work queue not found based on country, then read work queue based on
    // logged in user location province.
    if (readWorkQueueKey.key.workQueueID == CuramConst.kLongZero
      && !fecCaseFoundInd) {
      final String currentLoggedInUser = TransactionInfo.getProgramUser();
      readWorkQueueKey.key.workQueueID =
        bdmUtil.getFEWorkQueueIDByProvOfLoggedInUser(currentLoggedInUser);
    }

    final curam.core.sl.intf.WorkQueue workqueue =
      curam.core.sl.fact.WorkQueueFactory.newInstance();

    final ReadWorkQueueDetails readWorkQueueDetails;

    readWorkQueueDetails = workqueue.read(readWorkQueueKey);
    // Removing USernMae ,The user name when the Organization object is of type
    // user.
    // orgObjectLinkDtls.userName = readWorkQueueDetails.dtls.name;
    orgObjectLinkDtls.orgObjectType = ORGOBJECTTYPE.WORKQUEUE;
    orgObjectLinkDtls.orgObjectReference =
      readWorkQueueDetails.dtls.workQueueID;

    caseHeaderKey.caseID = chkey.caseID;
    // Start Bug 103204: IOSupervisor - Unable to assign a new owner to an FEC
    // -JP
    final SendNotificationInd sendNotificationInd = new SendNotificationInd();

    sendNotificationInd.sendNotification = false;

    final ReasonEndDateComments reasonEndDateComments =
      new ReasonEndDateComments();
    reasonEndDateComments.comments = "";
    reasonEndDateComments.reasonCode = CASEREASSIGNREASON.getDefaultCode();
    // Modify case ownership to Work Queue
    BDMCaseUserRoleFactory.newInstance().modifyCaseOwner(caseHeaderKey,
      orgObjectLinkDtls, reasonEndDateComments, sendNotificationInd);

  }

}
