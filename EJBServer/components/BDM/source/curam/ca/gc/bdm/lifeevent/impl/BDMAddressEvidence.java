package curam.ca.gc.bdm.lifeevent.impl;

import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.pdc.impl.PDCConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import java.util.HashMap;

public class BDMAddressEvidence {

  /**
   *
   * @param concernRoleID
   * @param evidenceDetails
   * @throws AppException
   * @throws InformationalException
   */
  public void createAddressEvidence(final long concernRoleID,
    final BDMAddressEvidenceVO evidenceDetails,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    final long evidenceID = evidenceDetails.getEvidenceID();
    if (evidenceID == 0) {

      final HashMap<String, String> evidenceData =
        new BDMLifeEventUtil().getEvidenceData(evidenceDetails);

      BDMEvidenceUtil.createPDCDynamicEvidence(concernRoleID, evidenceData,
        PDCConst.PDCADDRESS, evidenceChangeReason);
    } else {
      // contact preference life event is modify only
      BDMEvidenceUtil.modifyEvidence(evidenceID, PDCConst.PDCADDRESS,
        evidenceDetails, evidenceChangeReason);
    }
  }

}
