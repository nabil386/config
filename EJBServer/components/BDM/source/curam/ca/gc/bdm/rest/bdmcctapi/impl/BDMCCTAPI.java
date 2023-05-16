package curam.ca.gc.bdm.rest.bdmcctapi.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.communication.impl.BDMCommunicationHelper;
import curam.ca.gc.bdm.entity.communication.fact.BDMCommStatusHistoryFactory;
import curam.ca.gc.bdm.entity.communication.fact.BDMConcernRoleCommunicationFactory;
import curam.ca.gc.bdm.entity.communication.intf.BDMConcernRoleCommunication;
import curam.ca.gc.bdm.entity.communication.struct.BDMCommStatusHistoryDtls;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationDtls;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationKey;
import curam.ca.gc.bdm.entity.communication.struct.BDMCorrespondenceSearchKey;
import curam.ca.gc.bdm.entity.fact.BDMUserFactory;
import curam.ca.gc.bdm.entity.struct.BDMSamAccountNameKey;
import curam.ca.gc.bdm.entity.struct.BDMUserDtls;
import curam.ca.gc.bdm.entity.struct.BDMUserKey;
import curam.ca.gc.bdm.events.BDMINTERIMAPPCOMMUNICATION;
import curam.ca.gc.bdm.facade.communication.struct.BDMCommunicationKey;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMBPOCCT;
import curam.ca.gc.bdm.rest.bdmcctapi.struct.BDMCCTWorkItemStatusDetails;
import curam.ca.gc.bdm.util.impl.BDMCacheUtil;
import curam.ca.gc.bdm.util.rest.impl.BDMInterfaceLogger;
import curam.ca.gc.bdm.util.rest.impl.BDMRestUtil;
import curam.codetable.COMMUNICATIONSTATUS;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.ConcernRoleCommunicationFactory;
import curam.core.impl.CuramConst;
import curam.core.intf.ConcernRoleCommunication;
import curam.core.struct.ConcernRoleCommunicationDtls;
import curam.core.struct.ConcernRoleCommunicationKey;
import curam.util.events.impl.EventService;
import curam.util.events.struct.Event;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;
import curam.util.type.DateTime;
import curam.util.type.NotFoundIndicator;
import curam.util.type.UniqueID;

public class BDMCCTAPI
  implements curam.ca.gc.bdm.rest.bdmcctapi.intf.BDMCCTAPI {

  public BDMCCTAPI() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Inject
  BDMInterfaceLogger logger;

  private final String kMethod_updateWorkItemStatus =
    "BDMCCTAPI.updateWorkItemStatus() ";

  /**
   * Rest API to update the status of work item whenever there
   * has been an update to the status of a work item in CCT.
   *
   * @param req
   * - workitemid, status, date, community
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMCommunicationKey
    updateWorkItemStatus(final BDMCCTWorkItemStatusDetails req)
      throws AppException, InformationalException {

    // validate the inputs
    validateWorkItemStatus(req);

    // record the time of the call
    final long startTime = System.currentTimeMillis();

    // log the request object
    logger.logRequest(kMethod_updateWorkItemStatus, req.workitemid, req);

    // update the CCT Status in BDMConcernRoleCommunication Database
    final BDMCommunicationKey key = new BDMCommunicationKey();

    key.communicationID = updateCCTStatus(req);

    // update the Correspondence Status in ConcernRoleCommunication
    updateCorrespondenceStatus(key.communicationID, req.status, req.username);

    // log the performance time in milliseconds
    logger.logRestAPIPerf(kMethod_updateWorkItemStatus, startTime,
      req.workitemid);

    return key;
  }

  /**
   * Validates the workItemID and status.
   *
   * @param req
   *
   * @throws AppException
   * @throws InformationalException
   */
  private void validateWorkItemStatus(final BDMCCTWorkItemStatusDetails req)
    throws AppException, InformationalException {

    // throw 404 resource not found message when workItemID is not found
    if (req.workitemid.isEmpty()) {
      throw BDMRestUtil.throwHTTP404Status(BDMConstants.kWorkItemID);
    }

    // throw 404 resource not found message when status is not found
    if (req.status.isEmpty()) {
      throw BDMRestUtil.throwHTTP404Status(BDMConstants.kWorkItemStatus);
    }

    // throw 400 error that username is mandatory
    if (StringUtil.isNullOrEmpty(req.username)) {
      final AppException ae =
        new AppException(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST);
      // This is not a client facing error message
      ae.arg(kMethod_updateWorkItemStatus + "Username is Mandatory");
      throw BDMRestUtil.getHTTP400Status(ae);
    }

  }

  /**
   * This method updates the CCT status for the workItemID
   * in BDMConcernRoleCommunication table.
   *
   * @param req
   * @return long - communicationID
   *
   * @throws AppException
   * @throws InformationalException
   */
  private long updateCCTStatus(final BDMCCTWorkItemStatusDetails req)
    throws AppException, InformationalException {

    // get the correspondence record by workItemID
    final BDMConcernRoleCommunication corresObj =
      BDMConcernRoleCommunicationFactory.newInstance();
    final BDMCorrespondenceSearchKey searchKey =
      new BDMCorrespondenceSearchKey();
    searchKey.workItemID = Integer.parseInt(req.workitemid);

    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    final BDMConcernRoleCommunicationDtls corresDtls =
      corresObj.getCorrespondenceByWorkItemID(nfIndicator, searchKey);
    // Throw a 404 if no corresponding item is found.
    if (nfIndicator.isNotFound()) {
      throw BDMRestUtil.throwHTTP404Status(BDMConstants.kWorkItemID);
    }

    // set the key parameter
    final BDMConcernRoleCommunicationKey corresKey =
      new BDMConcernRoleCommunicationKey();

    corresKey.communicationID = corresDtls.communicationID;

    // set and save the CCT Status in BDMConcernRoleCommunication for the
    // workItemID
    final String cctStatusCode =
      BDMUtil.getCCTDescriptionToCodeMap().get(req.status);
    if (!StringUtil.isNullOrEmpty(cctStatusCode)) {
      corresDtls.cctStatus = cctStatusCode;
      // Set the status in req Object as well to get corresponding BDM Status
      req.status = cctStatusCode;
    } else {
      // TODO: Invalid status sent
    }

    corresObj.modify(corresKey, corresDtls);

    return corresDtls.communicationID;
  }

  /**
   * This method updates the correspondence status and save the previous status
   * in the status history table.
   *
   * @param communicationID
   * @param cctStatus
   *
   * @throws AppException
   * @throws InformationalException
   */
  private void updateCorrespondenceStatus(final long communicationID,
    final String cctStatus, final String cctUsername)
    throws AppException, InformationalException {

    // get the correspondence record from ConcernRoleCommunication
    final ConcernRoleCommunication commObj =
      ConcernRoleCommunicationFactory.newInstance();
    final ConcernRoleCommunicationKey commKey =
      new ConcernRoleCommunicationKey();
    commKey.communicationID = communicationID;

    final ConcernRoleCommunicationDtls commDtls = commObj.read(commKey);
    final String username = getBDMUserNameFromCCTUserName(cctUsername);

    // insert the current correspondence status in status history
    final BDMCommStatusHistoryDtls historyDtls =
      new BDMCommStatusHistoryDtls();
    historyDtls.BDMCommStatusHistoryID = UniqueID.nextUniqueID();
    historyDtls.communicationID = commDtls.communicationID;
    historyDtls.recordStatus = RECORDSTATUS.CANCELLED;
    historyDtls.statusCode = commDtls.communicationStatus;
    historyDtls.statusDateTime = DateTime.getCurrentDateTime();
    historyDtls.statusCTTableName = RECORDSTATUS.TABLENAME;
    historyDtls.userName = username;

    BDMCommStatusHistoryFactory.newInstance().insert(historyDtls);

    // set and save the correspondence status in ConcernRoleCommunication
    final BDMUtil bdmUtilObj = new BDMUtil();
    final String newCommunicationStatus =
      bdmUtilObj.getBDMStatusForCCTStatus(cctStatus);
    // If there's a task existing for this communicationID
    // and the communication status is changed, close the task.
    if (!StringUtil.isNullOrEmpty(newCommunicationStatus)
      && !newCommunicationStatus
        .equalsIgnoreCase(commDtls.communicationStatus)) {
      final BDMCommunicationHelper helper = new BDMCommunicationHelper();
      helper.closeCommunicationStatusTask(communicationID,
        commDtls.concernRoleID);
    }
    commDtls.communicationStatus = newCommunicationStatus;

    // BUG-94138, Start
    if (COMMUNICATIONSTATUS.CANCELLED
      .equalsIgnoreCase(commDtls.communicationStatus)) {
      commDtls.statusCode = RECORDSTATUS.CANCELLED;
    }
    // BUG-94138, End

    // BUG-111905, Start
    // Don't insert into Status thru premodify
    // Explicitly insert as the username needs to be passed on
    BDMCacheUtil.setWorkItemIDUpdatedThruAPI(communicationID);
    commObj.modify(commKey, commDtls);
    // Insert into BDMCommStatusHistory
    historyDtls.statusCode = newCommunicationStatus;
    historyDtls.recordStatus = RECORDSTATUS.NORMAL;
    historyDtls.statusDateTime = DateTime.getCurrentDateTime();
    historyDtls.BDMCommStatusHistoryID = UniqueID.nextUniqueID();
    historyDtls.statusCTTableName = COMMUNICATIONSTATUS.TABLENAME;
    BDMCommStatusHistoryFactory.newInstance().insert(historyDtls);

    // TASK-99477, Start
    // If a communication that's
    // - Part of the configured template list
    // - Has a caseID attached to it
    // is marked as sent, then fire an event so further processing can happen
    if (COMMUNICATIONSTATUS.SENT.equalsIgnoreCase(
      commDtls.communicationStatus) && CuramConst.gkZero != commDtls.caseID) {
      // get the template code for the given communicationID
      final BDMConcernRoleCommunicationKey bdmCommKey =
        new BDMConcernRoleCommunicationKey();
      bdmCommKey.communicationID = commKey.communicationID;
      final NotFoundIndicator nfIndicator = new NotFoundIndicator();
      final BDMConcernRoleCommunicationDtls bdmCommDtls =
        BDMConcernRoleCommunicationFactory.newInstance().read(nfIndicator,
          bdmCommKey);
      if (!nfIndicator.isNotFound()
        && !StringUtil.isNullOrEmpty(bdmCommDtls.templateName) && bdmUtilObj
          .isInterimApplicationTemplate(bdmCommDtls.templateName)) {
        // Create a new Event
        final Event event = new Event();
        event.eventKey = BDMINTERIMAPPCOMMUNICATION.COMMUNICATIONMARKEDSENT;
        event.primaryEventData = commDtls.communicationID;
        // Raise event
        EventService.raiseEvent(event);

      }
    }
    // TASK-99477, End

  }

  // BUG-111905
  // Using the user sent by CCT to capture the status rather
  // than the service account.
  /**
   * CCT will only send FirstName.LastName as the user name.
   *
   * If the user isn't found, use the service account as a fail safe.
   *
   * @param userName
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private String getBDMUserNameFromCCTUserName(final String userName)
    throws AppException, InformationalException {

    // TASK-119554, Start
    // The userName that's passed from CCT is the samAccountName
    // Get the corresponding Curam UserName based on the same.
    final BDMUserKey bdmUserKey = new BDMUserKey();
    bdmUserKey.userName = userName;
    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    final BDMSamAccountNameKey bdmSamAccountNameKey =
      new BDMSamAccountNameKey();
    bdmSamAccountNameKey.samAccountName = userName;
    final BDMUserDtls bdmUserDtls = BDMUserFactory.newInstance()
      .readBySAMAccountName(nfIndicator, bdmSamAccountNameKey);
    if (!nfIndicator.isNotFound()) {
      return bdmUserDtls.userName;
    }
    // TASK-119554, End
    return TransactionInfo.getProgramUser();
  }
}
