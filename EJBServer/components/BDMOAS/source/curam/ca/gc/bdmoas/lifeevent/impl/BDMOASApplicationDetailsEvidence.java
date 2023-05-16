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
 * Class for application details evidence.
 *
 * @author SMisal
 *
 */
public class BDMOASApplicationDetailsEvidence {

  /**
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public List<BDMOASApplicationDetailsEvidenceVO>
    getApplicationDetailsEvidenceValueObject(final long concernRoleID)
      throws AppException, InformationalException {

    final List<BDMOASApplicationDetailsEvidenceVO> applicationDetailsEvidenceList =
      new ArrayList<>();
    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new BDMEvidenceUtil().getEvdDtlsByConcernroleIDandEvidenceTypeInACOrIC(
        concernRoleID, CASEEVIDENCE.OAS_APPLICATION_DETAILS);

    final BDMLifeEventUtil bdmLifeEventUtil = new BDMLifeEventUtil();

    for (final DynamicEvidenceDataDetails details : evidenceDataDetailsList) {
      final BDMOASApplicationDetailsEvidenceVO applicationDetailsEvidenceVO =
        new BDMOASApplicationDetailsEvidenceVO();
      bdmLifeEventUtil.setEvidenceData(applicationDetailsEvidenceVO, details);
      applicationDetailsEvidenceList.add(applicationDetailsEvidenceVO);
    }

    return applicationDetailsEvidenceList;
  }

  /**
   *
   * @param concernRoleID
   * @param evidenceDetails
   * @throws AppException
   * @throws InformationalException
   */
  public void createApplicationDetailsEvidence(final long concernRoleID,
    final List<BDMOASApplicationDetailsEvidenceVO> evidenceDetailsList,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    HashMap<String, String> evidenceData;
    final BDMLifeEventUtil bdmLifeEventUtil = new BDMLifeEventUtil();

    for (final BDMOASApplicationDetailsEvidenceVO applicationDetailsEvidenceVO : evidenceDetailsList) {
      evidenceData =
        bdmLifeEventUtil.getEvidenceData(applicationDetailsEvidenceVO);

      final long evidenceID = applicationDetailsEvidenceVO.getEvidenceID();

      if (evidenceID != 0) {
        BDMEvidenceUtil.modifyEvidenceForCase(concernRoleID, evidenceID,
          CASEEVIDENCE.OAS_APPLICATION_DETAILS, evidenceData,
          evidenceChangeReason);
      } else {

        BDMEvidenceUtil.createACOrICDynamicEvidence(concernRoleID,
          evidenceData, CASEEVIDENCE.OAS_APPLICATION_DETAILS,
          evidenceChangeReason, 0);
      }
    }
  }
}
