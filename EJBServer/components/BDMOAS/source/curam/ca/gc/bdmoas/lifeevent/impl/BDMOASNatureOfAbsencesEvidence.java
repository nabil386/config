/**
 *
 */
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
 * BDMOAS FEATURE 92921:
 * Nature Of Absences evidence
 *
 * @author abid.a.khan
 *
 */
public class BDMOASNatureOfAbsencesEvidence {

  /**
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public List<BDMOASNatureOfAbsencesEvidenceVO> getEvidenceValueObject(
    final long concernRoleID) throws AppException, InformationalException {

    final List<BDMOASNatureOfAbsencesEvidenceVO> natureOfAbsencesEvidenceList =
      new ArrayList<>();

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new BDMEvidenceUtil().getEvdDtlsByConcernroleIDandEvidenceTypeInACOrIC(
        concernRoleID, CASEEVIDENCE.OAS_NATURE_OF_ABSENCES);

    final BDMLifeEventUtil bdmLifeEventUtil = new BDMLifeEventUtil();

    for (final DynamicEvidenceDataDetails details : evidenceDataDetailsList) {
      final BDMOASNatureOfAbsencesEvidenceVO natureOfAbsencesEvidenceVO =
        new BDMOASNatureOfAbsencesEvidenceVO();
      bdmLifeEventUtil.setEvidenceData(natureOfAbsencesEvidenceVO, details);
      natureOfAbsencesEvidenceList.add(natureOfAbsencesEvidenceVO);
    }
    return natureOfAbsencesEvidenceList;
  }

  /**
   *
   * @param concernRoleID
   * @param evidenceDetails
   * @throws AppException
   * @throws InformationalException
   */
  public void createNatureOfAbsencesEvidence(final long concernRoleID,
    final List<BDMOASNatureOfAbsencesEvidenceVO> evidenceDetailsList,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    HashMap<String, String> evidenceData;
    final BDMLifeEventUtil bdmLifeEventUtil = new BDMLifeEventUtil();

    for (final BDMOASNatureOfAbsencesEvidenceVO natureOfAbsencesEvidenceVO : evidenceDetailsList) {
      evidenceData =
        bdmLifeEventUtil.getEvidenceData(natureOfAbsencesEvidenceVO);

      final long evidenceID = natureOfAbsencesEvidenceVO.getEvidenceID();

      if (evidenceID != 0) {
        BDMEvidenceUtil.modifyEvidenceForCase(concernRoleID, evidenceID,
          CASEEVIDENCE.OAS_NATURE_OF_ABSENCES, evidenceData,
          evidenceChangeReason);
      } else {

        BDMEvidenceUtil.createACOrICDynamicEvidence(concernRoleID,
          evidenceData, CASEEVIDENCE.OAS_NATURE_OF_ABSENCES,
          evidenceChangeReason, 0);
      }
    }
  }

}
