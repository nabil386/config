package curam.ca.gc.bdmoas.lifeevent.impl;

import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.lifeevent.impl.BDMLifeEventUtil;
import curam.codetable.CASEEVIDENCE;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * BDMOAS FEATURE 92921: Class Added
 * Class for Marital Status evidence.
 *
 * @author SMisal
 *
 */
public class BDMOASMaritalStatusEvidence {

  /**
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public List<BDMOASMaritalStatusEvidenceVO>
    getMaritalStatusEvidenceValueObject(final long concernRoleID)
      throws AppException, InformationalException {

    final List<BDMOASMaritalStatusEvidenceVO> maritalStatusEvidenceList =
      new ArrayList<BDMOASMaritalStatusEvidenceVO>();
    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new BDMEvidenceUtil().getEvdDtlsByConcernroleIDandEvidenceTypeInACOrIC(
        concernRoleID, CASEEVIDENCE.BDM_MARITAL_STATUS);

    for (final DynamicEvidenceDataDetails details : evidenceDataDetailsList) {
      final BDMOASMaritalStatusEvidenceVO maritalStatusEvidenceVO =
        new BDMOASMaritalStatusEvidenceVO();
      new BDMLifeEventUtil().setEvidenceData(maritalStatusEvidenceVO,
        details);
      maritalStatusEvidenceList.add(maritalStatusEvidenceVO);
    }

    return maritalStatusEvidenceList;
  }

  /**
   *
   * @param concernRoleID
   * @param evidenceDetails
   * @throws AppException
   * @throws InformationalException
   */
  public void createMaritalStatusEvidence(final long concernRoleID,
    final List<BDMOASMaritalStatusEvidenceVO> evidenceDetailsList,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    HashMap<String, String> evidenceData;

    for (final BDMOASMaritalStatusEvidenceVO maritalStatusEvidenceVO : evidenceDetailsList) {
      evidenceData =
        new BDMLifeEventUtil().getEvidenceData(maritalStatusEvidenceVO);

      final long evidenceID = maritalStatusEvidenceVO.getEvidenceID();

      if (evidenceID != 0) {
        BDMEvidenceUtil.modifyEvidenceForCase(concernRoleID, evidenceID,
          CASEEVIDENCE.BDM_MARITAL_STATUS, evidenceData,
          evidenceChangeReason);
      } else {

        BDMEvidenceUtil.createACOrICDynamicEvidence(concernRoleID,
          evidenceData, CASEEVIDENCE.BDM_MARITAL_STATUS, evidenceChangeReason,
          0);
      }
    }
  }
}
