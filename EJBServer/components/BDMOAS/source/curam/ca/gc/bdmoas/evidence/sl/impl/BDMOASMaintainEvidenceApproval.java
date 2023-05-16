package curam.ca.gc.bdmoas.evidence.sl.impl;

import curam.ca.gc.bdmoas.evidence.sl.struct.BDMOASEvidenceApprovalDetails;
import curam.ca.gc.bdmoas.message.impl.BDMOASTASKMESSAGEExceptionCreator;
import curam.ca.gc.bdmoas.util.impl.BDMOASTaskUtil;
import curam.codetable.TARGETITEMTYPE;
import curam.core.facade.infrastructure.fact.EvidenceFactory;
import curam.core.impl.EnvVars;
import curam.core.sl.entity.fact.ApprovalRequestFactory;
import curam.core.sl.entity.intf.ApprovalRequest;
import curam.core.sl.entity.struct.ApprovalRequestDtls;
import curam.core.sl.entity.struct.ApprovalRequestKey;
import curam.core.sl.infrastructure.entity.fact.EDApprovalRequestFactory;
import curam.core.sl.infrastructure.entity.intf.EDApprovalRequest;
import curam.core.sl.infrastructure.entity.struct.CurrentEDApprovalRequestKey;
import curam.core.sl.infrastructure.entity.struct.EDApprovalRequestDtls;
import curam.core.sl.infrastructure.struct.EvidenceKey;
import curam.core.sl.infrastructure.struct.EvidenceRejectionDetails;
import curam.core.sl.struct.AllocationTargetDetails;
import curam.core.sl.struct.AllocationTargetList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.transaction.TransactionInfo;

/**
 * 95630 DEV: Implement Manage Override Evidence - R2
 * Custom service class to support evidence approval task actions and evidence
 * approval workflow activities.
 *
 * @author guruvamshikrishna.va
 *
 */
public class BDMOASMaintainEvidenceApproval implements
  curam.ca.gc.bdmoas.evidence.sl.intf.BDMOASMaintainEvidenceApproval {

  /**
   * Calls the OOTB evidence approval functionality then performs evidence
   * specific functionality
   */
  @Override
  public void approveApprovalRequest(final EvidenceKey key)
    throws AppException, InformationalException {

    validateApprover(key);

    // OOTB API
    EvidenceFactory.newInstance().approveApprovalRequest(key);

    // Forward the task to the requester
    forwardEvidenceApprovalTask(key);

  }

  /**
   * Compares the Original Requester and Current Approver, throws exception if
   * Requester and Approver are same
   *
   * @param key
   * @throws AppException
   * @throws InformationalException
   */
  private void validateApprover(final EvidenceKey key)
    throws AppException, InformationalException {

    // Read Approval Requester
    final EDApprovalRequest edApprovalRequestObj =
      EDApprovalRequestFactory.newInstance();
    final CurrentEDApprovalRequestKey edApprovalRequestKey =
      new CurrentEDApprovalRequestKey();
    edApprovalRequestKey.currentRequestInd = true;
    edApprovalRequestKey.evidenceDescriptorID = key.evidenceDescriptorID;
    final EDApprovalRequestDtls edApprovalRequestDtls = edApprovalRequestObj
      .readCurrentEDApprovalRequestDetails(edApprovalRequestKey);

    final ApprovalRequest approvalRequestObj =
      ApprovalRequestFactory.newInstance();
    final ApprovalRequestKey approvalRequestKey = new ApprovalRequestKey();
    approvalRequestKey.approvalRequestID =
      edApprovalRequestDtls.approvalRequestID;
    final ApprovalRequestDtls approvalRequestDtls =
      approvalRequestObj.read(approvalRequestKey);

    if (TransactionInfo.getProgramUser()
      .equals(approvalRequestDtls.requestedByUser)) {
      throw BDMOASTASKMESSAGEExceptionCreator
        .ERR_REQUESTER_CANNOT_BE_APPROVER();
    }

  }

  /**
   * Forward the approval task back to the requester
   *
   * @param key
   * @throws AppException
   * @throws InformationalException
   */
  private void forwardEvidenceApprovalTask(final EvidenceKey key)
    throws AppException, InformationalException {

    // TODO: Add implementation to forward the task to the worker who raised the
    // approval request.
    // Will be implemented after task allocation is defined, so that task can be
    // forwarded. Right now the OAS IC Case Owner is a workqueue so there is no
    // supervisor and the task is not assigned to any user.
  }

  /**
   * Calls the OOTB evidence rejection functionality then performs evidence
   * specific functionality
   */
  @Override
  public void rejectApprovalRequest(final EvidenceKey key,
    final EvidenceRejectionDetails details)
    throws AppException, InformationalException {

    validateApprover(key);

    // OOTB API
    EvidenceFactory.newInstance().rejectApprovalRequest(key, details);

    // Forward the task to the requester to make necessary changes
    forwardEvidenceApprovalTask(key);
  }

  /**
   * Checks if Open Evidence Approval Task Exists by EvidenceDescriptorID
   * If existing approval task exists, forward it to the appropriate reviewer,
   * set the taskDoesNotExist indicator to false.
   * If no existing approval task exists, set the taskDoesNotExist indicator to
   * true.
   */
  @Override
  public BDMOASEvidenceApprovalDetails processApprovalTaskIfExists(
    final EvidenceKey key) throws AppException, InformationalException {

    // Read task ID for the evidence descriptor ID
    final BDMOASTaskUtil taskUtilObj = new BDMOASTaskUtil();
    final Long taskID =
      taskUtilObj.getEvidenceApprovalTaskIDByEvidenceDescriptorID(key);

    final Boolean taskDoesNotExist = taskID == 0l ? true : false;
    final BDMOASEvidenceApprovalDetails evidenceApprovalDetails =
      new BDMOASEvidenceApprovalDetails();
    evidenceApprovalDetails.taskDoesNotExist = taskDoesNotExist;

    if (!taskDoesNotExist) {
      // TODO: Forward task to the appropriate reviewer if one already exists.
    }
    return evidenceApprovalDetails;
  }

  @Override
  public AllocationTargetList approvalManualAllocationStrategy()
    throws AppException, InformationalException {

    final AllocationTargetList allocationTargetList =
      new AllocationTargetList();

    final AllocationTargetDetails allocationTargetDetails =
      new AllocationTargetDetails();

    // Set the allocation name
    allocationTargetDetails.name = Configuration
      .getProperty(EnvVars.BDM_ENV_BDM_DEFAULT_CASEOWNER_WORKQUEUE);

    // Set the allocation type
    allocationTargetDetails.type = TARGETITEMTYPE.WORKQUEUE;

    allocationTargetList.dtls.addRef(allocationTargetDetails);

    // Return the allocation list
    return allocationTargetList;
  }

}
