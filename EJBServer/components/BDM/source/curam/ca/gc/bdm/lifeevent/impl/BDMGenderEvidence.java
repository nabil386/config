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
 * @since ADO-21129
 *
 * The utility class for gender evidence.
 */
public class BDMGenderEvidence {

  /**
   * Method to get an active gender evidence for the concern role and return the
   * gender evidence POJO with the evidence data.
   *
   * @param concernRoleID concern role identifier
   * @return return gender evidence POJO with the evidence data
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  public List<BDMGenderEvidenceVO> getGenderEvidenceValueObject(
    final long concernRoleID) throws AppException, InformationalException {

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new BDMEvidenceUtil().getEvdDtlsByConcernroleIDandEvidenceType(
        concernRoleID, PDCConst.PDCGENDER);

    final List<BDMGenderEvidenceVO> genderEvidenceVOList =
      new ArrayList<BDMGenderEvidenceVO>();

    for (final DynamicEvidenceDataDetails details : evidenceDataDetailsList) {

      final BDMGenderEvidenceVO genderEvidenceVO = new BDMGenderEvidenceVO();

      new BDMLifeEventUtil().setEvidenceData(genderEvidenceVO, details);

      genderEvidenceVOList.add(genderEvidenceVO);
    }

    return genderEvidenceVOList;
  }

  /**
   * Method to create or modify gender evidence with the evidence details in
   * POJO. This modifies the existing evidence if the evidence identifier is
   * present in the POJO otherwise creates a new evidence.
   *
   * @param concernRoleID concern role identifier
   * @param evidenceDetails gender evidence details
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  public void createGenderEvidence(final long concernRoleID,
    final BDMGenderEvidenceVO evidenceDetails,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    final long evidenceID = evidenceDetails.getEvidenceID();

    if (evidenceID == 0) {

      final HashMap<String, String> evidenceData =
        new BDMLifeEventUtil().getEvidenceData(evidenceDetails);

      BDMEvidenceUtil.createPDCDynamicEvidence(concernRoleID, evidenceData,
        PDCConst.PDCGENDER, evidenceChangeReason);
    } else {
      // modify the gender evidence
      BDMEvidenceUtil.modifyEvidence(evidenceID, PDCConst.PDCGENDER,
        evidenceDetails, evidenceChangeReason);
    }
  }

}
