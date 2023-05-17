package curam.ca.gc.bdm.lifeevent.impl;

import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.pdc.impl.PDCConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import java.util.ArrayList;
import java.util.List;

/**
 * @since- ADO-19764
 *
 * util class for Names
 *
 */
public class BDMNamesEvidence {

  /**
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public List<BDMNamesEvidenceVO> getNamesEvidenceValueObject(
    final long concernRoleID) throws AppException, InformationalException {

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new BDMEvidenceUtil().getEvdDtlsByConcernroleIDandEvidenceType(
        concernRoleID, PDCConst.PDCNAME);

    final List<BDMNamesEvidenceVO> namesEvidenceVOList =
      new ArrayList<BDMNamesEvidenceVO>();

    for (final DynamicEvidenceDataDetails details : evidenceDataDetailsList) {

      final BDMNamesEvidenceVO namesEvidenceVO = new BDMNamesEvidenceVO();
      new BDMLifeEventUtil().setEvidenceData(namesEvidenceVO, details);

      namesEvidenceVOList.add(namesEvidenceVO);
    }

    return namesEvidenceVOList;
  }

  /**
   *
   * @param concernRoleID
   * @param evidenceDetails
   * @throws AppException
   * @throws InformationalException
   */
  public void createNamesEvidence(final long concernRoleID,
    final BDMNamesEvidenceVO evidenceDetails,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    BDMEvidenceUtil.modifyEvidence(evidenceDetails.getEvidenceID(),
      PDCConst.PDCNAME, evidenceDetails, evidenceChangeReason, true);
  }

}
