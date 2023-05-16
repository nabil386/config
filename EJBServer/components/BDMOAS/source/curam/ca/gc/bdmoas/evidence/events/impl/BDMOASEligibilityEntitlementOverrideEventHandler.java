package curam.ca.gc.bdmoas.evidence.events.impl;

import curam.ca.gc.bdm.evidence.events.impl.BDMAbstractEvidenceEventHandler;
import curam.ca.gc.bdmoas.util.impl.BDMOASTaskUtil;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.infrastructure.struct.EIEvidenceKeyList;
import curam.core.sl.infrastructure.struct.EvidenceKey;
import curam.core.struct.CaseKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

/**
 * 95630 DEV: Implement Manage Override Evidence - R2
 * Evidence Events Handler class for EligibilityEntitlementOverride Evidence.
 *
 * @author guruvamshikrishna.va
 *
 */
public class BDMOASEligibilityEntitlementOverrideEventHandler
  extends BDMAbstractEvidenceEventHandler {

  @Override
  public CASEEVIDENCEEntry evidenceType() {

    return CASEEVIDENCEEntry.OAS_ELG_ENTITLEMNT_OVERRIDE;

  }

  @Override
  public boolean subscribePostActivation() {

    return true;

  }

  /**
   * Closes the Evidence Approval Task if one exists
   */
  @Override
  public void postActivation(
    final EvidenceControllerInterface evidenceController, final CaseKey key,
    final EIEvidenceKeyList list)
    throws AppException, InformationalException {

    final EIEvidenceKeyList evidenceKeyList =
      this.filterEvidenceKeyList(list);

    for (final EIEvidenceKey evidenceKey : evidenceKeyList.dtls) {

      // Read evidence descriptor record
      final EIEvidenceReadDtls evidenceReadDtls =
        evidenceController.readEvidence(evidenceKey);
      final Long evidenceDescriptorID =
        evidenceReadDtls.descriptor.evidenceDescriptorID;

      // Close the open approval task related to the evidence descriptor
      final EvidenceKey key1 = new EvidenceKey();
      key1.evidenceDescriptorID = evidenceDescriptorID;
      final BDMOASTaskUtil taskUtilObj = new BDMOASTaskUtil();
      final Long taskID =
        taskUtilObj.getEvidenceApprovalTaskIDByEvidenceDescriptorID(key1);

      if (taskID != 0l) {
        taskUtilObj.raiseTaskCloseEvent(taskID);
      }

    }
  }

}
