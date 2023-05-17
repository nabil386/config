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
public class BDMDateofBirthEvidence {

  /**
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public List<BDMDateofBirthEvidenceVO> getDOBEvidenceValueObject(
    final long concernRoleID) throws AppException, InformationalException {

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new BDMEvidenceUtil().getEvdDtlsByConcernroleIDandEvidenceType(
        concernRoleID, PDCConst.PDCBIRTHANDDEATH);

    final List<BDMDateofBirthEvidenceVO> dateofBirthEvidenceVOList =
      new ArrayList<BDMDateofBirthEvidenceVO>();

    for (final DynamicEvidenceDataDetails details : evidenceDataDetailsList) {

      final BDMDateofBirthEvidenceVO dateofBirthEvidenceVO =
        new BDMDateofBirthEvidenceVO();

      new BDMLifeEventUtil().setEvidenceData(dateofBirthEvidenceVO, details);

      dateofBirthEvidenceVOList.add(dateofBirthEvidenceVO);
    }

    return dateofBirthEvidenceVOList;
  }

  /**
   *
   * @param concernRoleID
   * @param evidenceDetails
   * @throws AppException
   * @throws InformationalException
   */
  public void createDateOfBirthEvidence(final long concernRoleID,
    final BDMDateofBirthEvidenceVO evidenceDetails,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    final long evidenceID = evidenceDetails.getEvidenceID();

    if (evidenceID == 0) {

      final HashMap<String, String> evidenceData =
        new BDMLifeEventUtil().getEvidenceData(evidenceDetails);

      BDMEvidenceUtil.createPDCDynamicEvidence(concernRoleID, evidenceData,
        PDCConst.PDCBIRTHANDDEATH, evidenceChangeReason);
    } else {
      // Date of birth life event is modify only

      // BEGIN TASK - 26338 - Do not set EffectiveFrom when the DOB is updated.
      BDMEvidenceUtil.modifyEvidence(evidenceID, PDCConst.PDCBIRTHANDDEATH,
        evidenceDetails, evidenceChangeReason, true);
      // END TASK - 26338
    }
  }

}
