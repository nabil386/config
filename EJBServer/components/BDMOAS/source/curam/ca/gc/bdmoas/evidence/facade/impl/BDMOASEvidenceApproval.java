package curam.ca.gc.bdmoas.evidence.facade.impl;

import curam.ca.gc.bdmoas.evidence.sl.fact.BDMOASMaintainEvidenceApprovalFactory;
import curam.core.sl.infrastructure.struct.EvidenceKey;
import curam.core.sl.infrastructure.struct.EvidenceRejectionDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

/**
 * 95630 DEV: Implement Manage Override Evidence - R2
 * Custom facade class to support evidence approval task actions.
 *
 * @author guruvamshikrishna.va
 *
 */
public class BDMOASEvidenceApproval
  implements curam.ca.gc.bdmoas.evidence.facade.intf.BDMOASEvidenceApproval {

  @Override
  public void approveApprovalRequest(final EvidenceKey key)
    throws AppException, InformationalException {

    BDMOASMaintainEvidenceApprovalFactory.newInstance()
      .approveApprovalRequest(key);

  }

  @Override
  public void rejectApprovalRequest(final EvidenceKey key,
    final EvidenceRejectionDetails details)
    throws AppException, InformationalException {

    BDMOASMaintainEvidenceApprovalFactory.newInstance()
      .rejectApprovalRequest(key, details);

  }

}
