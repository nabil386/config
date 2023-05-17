package curam.ca.gc.bdm.lifeevent.impl;

import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.pdc.impl.PDCConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @since- ADO-19764
 *
 * util class for Date of Birth
 *
 */
public class BDMContactPreferenceEvidence {

  /**
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public List<BDMContactPreferenceEvidenceVO> getEvidenceValueObject(
    final long concernRoleID) throws AppException, InformationalException {

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new BDMEvidenceUtil().getEvdDtlsByConcernroleIDandEvidenceType(
        concernRoleID, PDCConst.PDCCONTACTPREFERENCES);

    final List<BDMContactPreferenceEvidenceVO> contactPrefEvidenceVOList =
      new ArrayList<BDMContactPreferenceEvidenceVO>();

    for (final DynamicEvidenceDataDetails details : evidenceDataDetailsList) {

      final BDMContactPreferenceEvidenceVO contactPrefEvidenceVO =
        new BDMContactPreferenceEvidenceVO();

      new BDMLifeEventUtil().setEvidenceData(contactPrefEvidenceVO, details);

      contactPrefEvidenceVOList.add(contactPrefEvidenceVO);
    }

    return contactPrefEvidenceVOList;
  }

  /**
   *
   * @param concernRoleID
   * @param evidenceDetails
   * @throws AppException
   * @throws InformationalException
   */
  public void createContactPreference(final long concernRoleID,
    final BDMContactPreferenceEvidenceVO evidenceDetails,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    final long evidenceID = evidenceDetails.getEvidenceID();
    if (evidenceID == 0) {

      final HashMap<String, String> evidenceData =
        new BDMLifeEventUtil().getEvidenceData(evidenceDetails);

      BDMEvidenceUtil.createPDCDynamicEvidence(concernRoleID, evidenceData,
        PDCConst.PDCCONTACTPREFERENCES, evidenceChangeReason);
    } else {
      // contact preference life event is modify only
      BDMEvidenceUtil.modifyEvidence(evidenceID,
        PDCConst.PDCCONTACTPREFERENCES, evidenceDetails, evidenceChangeReason,
        true); // Task 26150 fix evidence status code
    }
  }

}
