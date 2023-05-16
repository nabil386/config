package curam.ca.gc.bdm.lifeevent.impl;

import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.codetable.CASEEVIDENCE;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BDMAttestation {

  /**
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public List<BDMAttestationVO> getEvidenceValueObject(
    final long concernRoleID) throws AppException, InformationalException {

    final List<BDMAttestationVO> attestationEvidenceList =
      new ArrayList<BDMAttestationVO>();

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new BDMEvidenceUtil().getEvdDtlsByConcernroleIDandEvidenceType(
        concernRoleID, CASEEVIDENCE.BDMATTS);

    for (final DynamicEvidenceDataDetails details : evidenceDataDetailsList) {

      final BDMAttestationVO attestationEvidenceVO = new BDMAttestationVO();

      new BDMLifeEventUtil().setEvidenceData(attestationEvidenceVO, details);

      attestationEvidenceList.add(attestationEvidenceVO);
    }

    return attestationEvidenceList;
  }

  /**
   *
   * @param concernRoleID
   * @param evidenceDetails
   * @throws AppException
   * @throws InformationalException
   */
  public void createAttestationEvidence(final long concernRoleID,
    final List<BDMAttestationVO> evidenceDetailsList,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    for (final BDMAttestationVO attestationEvidenceVO : evidenceDetailsList) {
      final HashMap<String, String> evidenceData =
        new BDMLifeEventUtil().getEvidenceData(attestationEvidenceVO);

      new BDMEvidenceUtil();
      BDMEvidenceUtil.createACOrICDynamicEvidence(concernRoleID, evidenceData,
        CASEEVIDENCE.BDMATTS, evidenceChangeReason, 0);
    }
  }

}
