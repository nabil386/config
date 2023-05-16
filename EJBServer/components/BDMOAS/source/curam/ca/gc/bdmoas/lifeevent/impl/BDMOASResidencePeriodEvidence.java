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
 * Class for Residence Period evidence.
 *
 * @author SMisal
 *
 */
public class BDMOASResidencePeriodEvidence {

  /**
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public List<BDMOASResidencePeriodEvidenceVO>
    getResidencePeriodEvidenceValueObject(final long concernRoleID)
      throws AppException, InformationalException {

    final List<BDMOASResidencePeriodEvidenceVO> residencePeriodEvidenceList =
      new ArrayList<BDMOASResidencePeriodEvidenceVO>();
    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new BDMEvidenceUtil().getEvdDtlsByConcernroleIDandEvidenceTypeInACOrIC(
        concernRoleID, CASEEVIDENCE.OAS_RESIDENCE_PERIOD);

    final BDMLifeEventUtil bdmLifeEventUtil = new BDMLifeEventUtil();

    for (final DynamicEvidenceDataDetails details : evidenceDataDetailsList) {
      final BDMOASResidencePeriodEvidenceVO residencePeriodEvidenceVO =
        new BDMOASResidencePeriodEvidenceVO();
      bdmLifeEventUtil.setEvidenceData(residencePeriodEvidenceVO, details);
      residencePeriodEvidenceList.add(residencePeriodEvidenceVO);
    }

    return residencePeriodEvidenceList;
  }

  /**
   *
   * @param concernRoleID
   * @param evidenceDetails
   * @throws AppException
   * @throws InformationalException
   */
  public void createResidencePeriodEvidence(final long concernRoleID,
    final List<BDMOASResidencePeriodEvidenceVO> evidenceDetailsList,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    HashMap<String, String> evidenceData;
    final BDMLifeEventUtil bdmLifeEventUtil = new BDMLifeEventUtil();

    for (final BDMOASResidencePeriodEvidenceVO residencePeriodEvidenceVO : evidenceDetailsList) {
      evidenceData =
        bdmLifeEventUtil.getEvidenceData(residencePeriodEvidenceVO);

      final long evidenceID = residencePeriodEvidenceVO.getEvidenceID();

      if (evidenceID != 0) {
        BDMEvidenceUtil.modifyEvidenceForCase(concernRoleID, evidenceID,
          CASEEVIDENCE.OAS_RESIDENCE_PERIOD, evidenceData,
          evidenceChangeReason);
      } else {

        BDMEvidenceUtil.createACOrICDynamicEvidence(concernRoleID,
          evidenceData, CASEEVIDENCE.OAS_RESIDENCE_PERIOD,
          evidenceChangeReason, 0);
      }
    }
  }
}
