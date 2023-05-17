package curam.ca.gc.bdm.sl.impl;

import curam.ca.gc.bdm.communication.impl.BDMCommunicationHelper;
import curam.ca.gc.bdm.entity.struct.BDMWMInstanceDataDtls;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMCORRESPONDENCETASKDATA;
import curam.codetable.TARGETITEMTYPE;
import curam.codetable.TASKPRIORITY;
import curam.core.facade.struct.StandardManualTaskDtls;
import curam.core.fact.ConcernRoleCommunicationFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.fact.WMInstanceDataFactory;
import curam.core.intf.ConcernRole;
import curam.core.intf.ConcernRoleCommunication;
import curam.core.intf.WMInstanceData;
import curam.core.sl.fact.WorkAllocationTaskFactory;
import curam.core.sl.intf.WorkAllocationTask;
import curam.core.struct.ConcernRoleCommunicationDtls;
import curam.core.struct.ConcernRoleCommunicationKey;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.WMInstanceDataDtls;
import curam.core.struct.WMInstanceDataKey;
import curam.util.deferredprocessing.impl.DPCallback;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.DateTime;

public class BDMSubmitCorrespondenceDPErrorHandler implements DPCallback {

  /**
   * Handles DP Process error when submit DP correspondence is unable to submit
   * communication record or validation issue happened
   * successfully
   *
   * @param process name
   * @param instanceDataID
   */
  @Override
  public void dpHandleError(final String processName,
    final long instanceDataID) throws AppException, InformationalException {

    final BDMCommunicationHelper bdmCommHelper = new BDMCommunicationHelper();
    final WMInstanceData wmInstanceDataObj =
      WMInstanceDataFactory.newInstance();
    final WMInstanceDataDtls wmInstanceDataDtls = new WMInstanceDataDtls();
    wmInstanceDataDtls.wm_instDataID = instanceDataID;
    final WMInstanceDataKey wm_Key = new WMInstanceDataKey();
    wm_Key.wm_instDataID = instanceDataID;
    final WMInstanceDataDtls wmInstanceData = wmInstanceDataObj.read(wm_Key);
    final BDMWMInstanceDataDtls bdmWMInstanceDataDtls =
      bdmCommHelper.readBDMWMInstanceDatabyID(instanceDataID);
    final ConcernRoleCommunication crcObj =
      ConcernRoleCommunicationFactory.newInstance();
    final ConcernRoleCommunicationKey crcKey =
      new ConcernRoleCommunicationKey();
    crcKey.communicationID = bdmWMInstanceDataDtls.communicationID;
    final ConcernRoleCommunicationDtls crcDtls = crcObj.read(crcKey);
    // create WorkFlow for user to review the communication record
    final WorkAllocationTask workAllocationTaskObj =
      WorkAllocationTaskFactory.newInstance();
    final StandardManualTaskDtls taskKey = new StandardManualTaskDtls();
    taskKey.dtls.assignDtls.assignmentID = BDMConstants.kBDMCoreWorkQueueID;
    taskKey.dtls.assignDtls.assignType = TARGETITEMTYPE.WORKQUEUE;
    taskKey.dtls.concerningDtls.caseID = crcDtls.caseID;
    taskKey.dtls.concerningDtls.participantRoleID =
      crcDtls.correspondentConcernRoleID;

    final ConcernRole crObj = ConcernRoleFactory.newInstance();
    final ConcernRoleKey crKey = new ConcernRoleKey();
    crKey.concernRoleID = crcDtls.correspondentConcernRoleID;
    final ConcernRoleDtls crDtls = crObj.read(crKey);

    // create the workflow subject with the parameter
    final AppException e = new AppException(
      BDMCORRESPONDENCETASKDATA.ERR_COMMUNICATION_SUBMIT_ERROR_TASK_SUBJECT);

    // Read the participant name and add to the exception message.
    e.arg(crDtls.concernRoleName);
    e.arg(crDtls.primaryAlternateID);
    taskKey.dtls.taskDtls.taskDefinitionID =
      BDMConstants.kCommunicationFailedTaskDefinition;
    taskKey.dtls.taskDtls.subject = e.getMessage();
    taskKey.dtls.taskDtls.priority = TASKPRIORITY.NORMAL;
    taskKey.dtls.taskDtls.deadlineTime =
      DateTime.getCurrentDateTime().addTime(48, 0, 0);

    workAllocationTaskObj.createManualTask(taskKey);

  }

}
