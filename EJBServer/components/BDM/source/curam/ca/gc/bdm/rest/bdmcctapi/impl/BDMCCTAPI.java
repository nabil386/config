package curam.ca.gc.bdm.rest.bdmcctapi.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMAUDITAPITYPE;
import curam.ca.gc.bdm.codetable.BDMCORRESPONDENCEIDTYPE;
import curam.ca.gc.bdm.communication.impl.BDMCommunicationHelper;
import curam.ca.gc.bdm.entity.communication.fact.BDMCommStatusHistoryFactory;
import curam.ca.gc.bdm.entity.communication.fact.BDMConcernRoleCommunicationFactory;
import curam.ca.gc.bdm.entity.communication.fact.BDMCorrespondenceStagingFactory;
import curam.ca.gc.bdm.entity.communication.intf.BDMConcernRoleCommunication;
import curam.ca.gc.bdm.entity.communication.struct.BDMCommStatusHistoryDtls;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationDtls;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationKey;
import curam.ca.gc.bdm.entity.communication.struct.BDMCorrespondenceSearchKey;
import curam.ca.gc.bdm.entity.communication.struct.BDMCorrespondenceStagingJobID;
import curam.ca.gc.bdm.entity.communication.struct.BDMCorrespondenceStagingKeyList;
import curam.ca.gc.bdm.entity.communication.struct.BDMSearchByTrackingNumberKey;
import curam.ca.gc.bdm.entity.fact.BDMUserFactory;
import curam.ca.gc.bdm.entity.struct.BDMSamAccountNameKey;
import curam.ca.gc.bdm.entity.struct.BDMUserDtls;
import curam.ca.gc.bdm.entity.struct.BDMUserKey;
import curam.ca.gc.bdm.events.BDMINTERIMAPPCOMMUNICATION;
import curam.ca.gc.bdm.facade.communication.struct.BDMCommunicationKey;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMBPOCCT;
import curam.ca.gc.bdm.rest.bdmcctapi.struct.BDMCCTCorrespondenceStatusDetails;
import curam.ca.gc.bdm.rest.bdmcctapi.struct.BDMCCTWorkItemStatusDetails;
import curam.ca.gc.bdm.sl.impl.BDMUpdateCorrespondenceJobDPErrorHandler;
import curam.ca.gc.bdm.util.impl.BDMCacheUtil;
import curam.ca.gc.bdm.util.rest.impl.BDMAPIAuditDetails;
import curam.ca.gc.bdm.util.rest.impl.BDMAPIAuditDetails.BDMAPIAuditDetailsBuilder;
import curam.ca.gc.bdm.util.rest.impl.BDMAPIAuditUtil;
import curam.ca.gc.bdm.util.rest.impl.BDMInterfaceLogger;
import curam.ca.gc.bdm.util.rest.impl.BDMRestUtil;
import curam.codetable.BDMCCTCOMMUNICATIONSTATUS;
import curam.codetable.COMMUNICATIONSTATUS;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.ConcernRoleCommunicationFactory;
import curam.core.fact.WMInstanceDataFactory;
import curam.core.impl.CuramConst;
import curam.core.intf.ConcernRoleCommunication;
import curam.core.intf.WMInstanceData;
import curam.core.struct.ConcernRoleCommunicationDtls;
import curam.core.struct.ConcernRoleCommunicationKey;
import curam.core.struct.WMInstanceDataDtls;
import curam.util.events.impl.EventService;
import curam.util.events.struct.Event;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.fact.DeferredProcessingFactory;
import curam.util.message.INFRASTRUCTURE;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.DateTime;
import curam.util.type.NotFoundIndicator;
import curam.util.type.UniqueID;
import org.apache.commons.httpclient.HttpStatus;

public class BDMCCTAPI
  implements curam.ca.gc.bdm.rest.bdmcctapi.intf.BDMCCTAPI {

  public BDMCCTAPI() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Inject
  BDMInterfaceLogger logger;

  @Inject
  BDMAPIAuditUtil bdmapiAuditUtil;

  private final String kMethod_updateWorkItemStatus =
    "BDMCCTAPI.updateWorkItemStatus";

  private final String kMethod_updateCorrespondenceStatus =
    "BDMCCTAPI.updateCorrespondenceStatus";

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

    // record the time of the call
    final long startTime = System.currentTimeMillis();

    // log the request object
    logger.logRequest(kMethod_updateWorkItemStatus, req.workitemid, req);

    // BEGIN, ADO 107574 , Audit API's
    final String correlationID = req.cloudEvent.id;
    final String source = req.cloudEvent.source;
    Trace.kTopLevelLogger.info(
      kMethod_updateWorkItemStatus + ": CorrelationID = " + correlationID);
    final BDMAPIAuditDetailsBuilder auditDetailsBuilder =
      new BDMAPIAuditDetailsBuilder();
    BDMAPIAuditDetails bdmapiAuditDetails = auditDetailsBuilder
      .setMethod(kMethod_updateWorkItemStatus).setRelatedID(req.workitemid)
      .setRequestObject(req).setApiType(BDMAUDITAPITYPE.BDMINBOUND)
      .setRequestTransactionDateTime(DateTime.getCurrentDateTime())
      .setSource(source).setCorrelationID(correlationID).build();
    // END, ADO 107574 , Audit API's

    // update the CCT Status in BDMConcernRoleCommunication Database
    final BDMCommunicationKey key = new BDMCommunicationKey();

    try {
      // validate the inputs
      validateWorkItemStatus(req, bdmapiAuditDetails);

      key.communicationID = updateCCTStatus(req, bdmapiAuditDetails);

      // update the Correspondence Status in ConcernRoleCommunication
      updateCorrespondenceStatus(key.communicationID, req.status,
        req.username);
      // updateCorrespondenceStatus(key.communicationID, req.status);

      // BEGIN, ADO 107574 , Audit APIs
      bdmapiAuditDetails = auditDetailsBuilder.setResponseObject(key)
        .setResponseTransactionDateTime(DateTime.getCurrentDateTime())
        .setStatusCode(HttpStatus.SC_CREATED).build();

      bdmapiAuditUtil.auditAPI(bdmapiAuditDetails);

    } catch (final Exception e) {
      if (!e.getClass().getCanonicalName()
        .equals(AppException.class.getCanonicalName())) {
        // If there are any other exceptions other than AppException which is
        // handled in
        // APIs, then 500:Internal Server error will be
        // thrown by Curam REST infra and this logic will log that entry in
        // audit table.
        // The exception caught is thrown after storing in auditing
        BDMRestUtil.auditErrorResponse(
          new AppException(INFRASTRUCTURE.ID_UNHANDLED, e),
          HttpStatus.SC_INTERNAL_SERVER_ERROR, bdmapiAuditDetails);
      }
      // ensure exception is thrown
      throw e;
    }
    // END, ADO 107574 , Audit APIs

    // log the performance time in milliseconds
    logger.logRestAPIPerf(kMethod_updateWorkItemStatus, startTime,
      req.workitemid);

    return key;
  }

  @Override
  public BDMCommunicationKey
    updateCorrespondenceStatus(final BDMCCTCorrespondenceStatusDetails req)
      throws AppException, InformationalException {

    // record the time of the call
    final long startTime = System.currentTimeMillis();

    // log the request object
    logger.logRequest(kMethod_updateCorrespondenceStatus,
      req.correspondenceID, req);

    // BEGIN, ADO 107574 , Audit API's
    // BEGIN, ADO 121965 , CloudEvent
    final String correlationID = req.cloudEvent.id;
    final String source = req.cloudEvent.source;
    Trace.kTopLevelLogger.info(kMethod_updateCorrespondenceStatus
      + ": CorrelationID = " + correlationID);
    // END, ADO 121965 , CloudEvent
    final BDMAPIAuditDetailsBuilder auditDetailsBuilder =
      new BDMAPIAuditDetailsBuilder();
    BDMAPIAuditDetails bdmapiAuditDetails =
      auditDetailsBuilder.setMethod(kMethod_updateCorrespondenceStatus)
        .setRelatedID(req.correspondenceID).setRequestObject(req)
        .setApiType(BDMAUDITAPITYPE.BDMINBOUND)
        .setRequestTransactionDateTime(DateTime.getCurrentDateTime())
        .setSource(source).setCorrelationID(correlationID).build();
    // END, ADO 107574 , Audit API's

    // update the CCT Status in BDMConcernRoleCommunication Database
    final BDMCommunicationKey key = new BDMCommunicationKey();

    try {
      // validate the inputs
      validateCorrespondenceStatus(req, bdmapiAuditDetails);

      key.communicationID = updateCCTStatus(req, bdmapiAuditDetails);

      // update the Correspondence Status in ConcernRoleCommunication
      // If the jobID is being updated, skip this step since it would
      // be done thru CommunicationDP.updateJobStatusDP deferred process
      if (!BDMCORRESPONDENCEIDTYPE.JOB_ID.equalsIgnoreCase(req.type)) {
        updateCorrespondenceStatus(key.communicationID, req.status,
          req.username);
      }

      // BEGIN, ADO 107574 , Audit APIs
      bdmapiAuditDetails = auditDetailsBuilder.setResponseObject(key)
        .setResponseTransactionDateTime(DateTime.getCurrentDateTime())
        .setStatusCode(HttpStatus.SC_CREATED).build();

      bdmapiAuditUtil.auditAPI(bdmapiAuditDetails);

    } catch (final Exception e) {
      if (!e.getClass().getCanonicalName()
        .equals(AppException.class.getCanonicalName())) {
        // If there are any other exceptions other than AppException which is
        // handled in
        // APIs, then 500:Internal Server error will be
        // thrown by Curam REST infra and this logic will log that entry in
        // audit table.
        // The exception caught is thrown after storing in auditing
        BDMRestUtil.auditErrorResponse(
          new AppException(INFRASTRUCTURE.ID_UNHANDLED, e),
          HttpStatus.SC_INTERNAL_SERVER_ERROR, bdmapiAuditDetails);
      }
      // ensure exception is thrown
      throw e;
    }
    // END, ADO 107574 , Audit APIs

    // log the performance time in milliseconds
    logger.logRestAPIPerf(kMethod_updateCorrespondenceStatus, startTime,
      req.correspondenceID);

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
  protected void validateWorkItemStatus(final BDMCCTWorkItemStatusDetails req,
    final BDMAPIAuditDetails bdmapiAuditDetails)
    throws AppException, InformationalException {

    // throw 404 resource not found message when workItemID is not found
    if (req.workitemid.isEmpty()) {

      // throw BDMRestUtil.throwHTTP404Status(BDMConstants.kWorkItemID);
      BDMRestUtil.throwHTTP404Status(BDMConstants.kWorkItemID,
        bdmapiAuditDetails);
    }

    // throw 404 resource not found message when status is not found
    if (req.status.isEmpty()) {

      // throw BDMRestUtil.throwHTTP404Status(BDMConstants.kWorkItemStatus);
      BDMRestUtil.throwHTTP404Status(BDMConstants.kWorkItemID,
        bdmapiAuditDetails);

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
   * Validates the workItemID and status.
   *
   * @param req
   *
   * @throws AppException
   * @throws InformationalException
   */
  protected void validateCorrespondenceStatus(
    final BDMCCTCorrespondenceStatusDetails req,
    final BDMAPIAuditDetails bdmapiAuditDetails)
    throws AppException, InformationalException {

    // throw 404 resource not found message when workItemID is not found
    if (req.correspondenceID.isEmpty()) {

      // throw BDMRestUtil.throwHTTP404Status(BDMConstants.kCorrespondenceID);
      BDMRestUtil.throwHTTP404Status(BDMConstants.kCorrespondenceID,
        bdmapiAuditDetails);
    }

    // throw 404 resource not found message when status is not found
    if (req.status.isEmpty()) {

      // throw BDMRestUtil.throwHTTP404Status(BDMConstants.kWorkItemStatus);
      BDMRestUtil.throwHTTP404Status(BDMConstants.kWorkItemStatus,
        bdmapiAuditDetails);

    }

    // throw 400 error that username is mandatory
    if (StringUtil.isNullOrEmpty(req.username)) {
      final AppException ae =
        new AppException(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST);
      // This is not a client facing error message
      ae.arg(kMethod_updateCorrespondenceStatus + "Username is Mandatory");
      throw BDMRestUtil.getHTTP400Status(ae);
    }

    // throw 400 error the type is mandatory

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
  protected long updateCCTStatus(final BDMCCTWorkItemStatusDetails req,
    final BDMAPIAuditDetails bdmapiAuditDetails)
    throws AppException, InformationalException {

    // get the correspondence record by workItemID
    final BDMConcernRoleCommunication corresObj =
      BDMConcernRoleCommunicationFactory.newInstance();
    final BDMCorrespondenceSearchKey searchKey =
      new BDMCorrespondenceSearchKey();
    searchKey.workItemID = Integer.parseInt(req.workitemid);

    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    final BDMConcernRoleCommunicationDtls corresDtls;
    if (CodeTable
      .getOneItem(BDMCCTCOMMUNICATIONSTATUS.TABLENAME,
        BDMCCTCOMMUNICATIONSTATUS.FAILED_OVERSIZED)
      .equalsIgnoreCase(req.status)) {
      final BDMSearchByTrackingNumberKey searchByTrackingNumberKey =
        new BDMSearchByTrackingNumberKey();
      searchByTrackingNumberKey.trackingNumber =
        Long.parseLong(req.workitemid);
      corresDtls = corresObj.getCorrespondenceByTrackingNumber(nfIndicator,
        searchByTrackingNumberKey);
      if (nfIndicator.isNotFound()) {

        // BEGIN, ADO-107574, Audit interfaces
        BDMRestUtil.throwHTTP404Status(
          BDMConstants.kGenerateCorrespondenceTrackingNumber,
          bdmapiAuditDetails);
        // END, ADO-107574, Audit interfaces
      }
    } else {
      corresDtls =
        corresObj.getCorrespondenceByWorkItemID(nfIndicator, searchKey);
    }
    // Throw a 404 if no corresponding item is found.
    if (nfIndicator.isNotFound()) {

      // BEGIN, ADO-107574, Audit interfaces
      BDMRestUtil.throwHTTP404Status(BDMConstants.kWorkItemID,
        bdmapiAuditDetails);
      // END, ADO-107574, Audit interfaces
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
   * This method updates the CCT status for the workItemID
   * in BDMConcernRoleCommunication table.
   *
   * @param req
   * @return long - communicationID
   *
   * @throws AppException
   * @throws InformationalException
   */
  protected long updateCCTStatus(final BDMCCTCorrespondenceStatusDetails req,
    final BDMAPIAuditDetails bdmapiAuditDetails)
    throws AppException, InformationalException {

    final BDMConcernRoleCommunication corresObj =
      BDMConcernRoleCommunicationFactory.newInstance();

    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    BDMConcernRoleCommunicationDtls corresDtls =
      new BDMConcernRoleCommunicationDtls();

    final String cctStatusCode =
      BDMUtil.getCCTDescriptionToCodeMap().get(req.status);

    // BULK_* statuses can be used only for JOBID
    if ((BDMCCTCOMMUNICATIONSTATUS.BULK_SUCCESS
      .equalsIgnoreCase(cctStatusCode)
      || BDMCCTCOMMUNICATIONSTATUS.BULK_FAILURE
        .equalsIgnoreCase(cctStatusCode))
      && !BDMCORRESPONDENCEIDTYPE.JOB_ID.equalsIgnoreCase(req.type)) {
      final AppException ae =
        new AppException(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST);
      // This is not a client facing error message
      ae.arg(kMethod_updateCorrespondenceStatus
        + " Bulk statuses are applicable to only JOBID");
      throw BDMRestUtil.getHTTP400Status(ae);
    }

    // BULK_* statuses can be used only for JOBID
    if (BDMCORRESPONDENCEIDTYPE.JOB_ID.equalsIgnoreCase(req.type)
      && !BDMCCTCOMMUNICATIONSTATUS.BULK_SUCCESS
        .equalsIgnoreCase(cctStatusCode)
      && !BDMCCTCOMMUNICATIONSTATUS.BULK_FAILURE
        .equalsIgnoreCase(cctStatusCode)) {
      final AppException ae =
        new AppException(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST);
      // This is not a client facing error message
      ae.arg(kMethod_updateCorrespondenceStatus
        + " Only Bulk statuses are applicable to JOBID");
      throw BDMRestUtil.getHTTP400Status(ae);
    }

    // if the type is workitem, search by workitemID

    // get the correspondence record by workItemID
    if (BDMCORRESPONDENCEIDTYPE.WORKITEM_ID.equalsIgnoreCase(req.type)) {
      final BDMCorrespondenceSearchKey searchKey =
        new BDMCorrespondenceSearchKey();
      searchKey.workItemID = Integer.parseInt(req.correspondenceID);
      corresDtls =
        corresObj.getCorrespondenceByWorkItemID(nfIndicator, searchKey);
      if (nfIndicator.isNotFound()) {
        // BEGIN, ADO-107574, Audit interfaces
        BDMRestUtil.throwHTTP404Status(BDMConstants.kWorkItemID,
          bdmapiAuditDetails);
      }
    } else if (BDMCORRESPONDENCEIDTYPE.TRACKING_NUMBER
      .equalsIgnoreCase(req.type)) {
      // Tracking number status would be only oversized
      final BDMSearchByTrackingNumberKey searchByTrackingNumberKey =
        new BDMSearchByTrackingNumberKey();
      searchByTrackingNumberKey.trackingNumber =
        Long.parseLong(req.correspondenceID);
      corresDtls = corresObj.getCorrespondenceByTrackingNumber(nfIndicator,
        searchByTrackingNumberKey);
      if (nfIndicator.isNotFound()) {

        // BEGIN, ADO-107574, Audit interfaces
        BDMRestUtil.throwHTTP404Status(
          BDMConstants.kGenerateCorrespondenceTrackingNumber,
          bdmapiAuditDetails);
      }
    } else if (BDMCORRESPONDENCEIDTYPE.JOB_ID.equalsIgnoreCase(req.type)
      && CodeTable
        .getOneItem(BDMCCTCOMMUNICATIONSTATUS.TABLENAME,
          BDMCCTCOMMUNICATIONSTATUS.BULK_SUCCESS)
        .equalsIgnoreCase(req.status)) {

      // Check if the jobID exists
      final BDMCorrespondenceStagingJobID bdmCorrespondenceStagingJobID =
        new BDMCorrespondenceStagingJobID();
      bdmCorrespondenceStagingJobID.jobID =
        Long.parseLong(req.correspondenceID);
      final BDMCorrespondenceStagingKeyList keyList =
        BDMCorrespondenceStagingFactory.newInstance()
          .searchByJobID(bdmCorrespondenceStagingJobID);
      if (keyList.dtls.isEmpty()) {
        // BEGIN, ADO-107574, Audit interfaces
        BDMRestUtil.throwHTTP404Status(BDMConstants.kJobID,
          bdmapiAuditDetails);
      }

      // Kick of deferred processing only if status is success

      final WMInstanceDataDtls wmInstanceDataDtls = new WMInstanceDataDtls();
      final WMInstanceData wmInstanceDataObj =
        WMInstanceDataFactory.newInstance();

      // Triggering correspondence deferred process.
      wmInstanceDataDtls.wm_instDataID =
        curam.util.type.UniqueID.nextUniqueID(BDMConstants.kDPTICKET);
      wmInstanceDataDtls.enteredByID = TransactionInfo.getProgramUser();
      // Using comments as no logical field is found.
      wmInstanceDataDtls.comments = req.correspondenceID;
      wmInstanceDataDtls.caseStatus = BDMCCTCOMMUNICATIONSTATUS.BULK_SUCCESS;
      wmInstanceDataObj.insert(wmInstanceDataDtls);

      DeferredProcessingFactory.newInstance().startProcess(
        BDMConstants.kUPDATECORRESPONDENCEDP,
        wmInstanceDataDtls.wm_instDataID,
        BDMUpdateCorrespondenceJobDPErrorHandler.class.getCanonicalName());
      return corresDtls.communicationID;
    } else {
      // Currently don't handle failures.
      return corresDtls.communicationID;
    }

    // set the key parameter
    final BDMConcernRoleCommunicationKey corresKey =
      new BDMConcernRoleCommunicationKey();

    corresKey.communicationID = corresDtls.communicationID;

    // set and save the CCT Status in BDMConcernRoleCommunication for the
    // correspondence
    if (!StringUtil.isNullOrEmpty(cctStatusCode)) {
      corresDtls.cctStatus = cctStatusCode;
      // Set the status in req Object as well to get corresponding BDM Status
      req.status = cctStatusCode;
    } else {
      final AppException ae =
        new AppException(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST);
      // This is not a client facing error message
      ae.arg(kMethod_updateCorrespondenceStatus + " Status doesn't match");
      throw BDMRestUtil.getHTTP400Status(ae);
    }

    corresObj.modify(corresKey, corresDtls);
    if (COMMUNICATIONSTATUS.SUBMITTED_OVERSIZE
      .equalsIgnoreCase(corresDtls.cctStatus)) {
      // TODO: Trigger task 72.
    }

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
  protected void updateCorrespondenceStatus(final long communicationID,
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
    } else if (COMMUNICATIONSTATUS.SUBMITTED_OVERSIZE
      .equalsIgnoreCase(commDtls.communicationStatus)) {
      // TODO: Trigger Task-72
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
