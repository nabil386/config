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
 * World Income Evidence
 *
 * @author abid.a.khan
 *
 */
public class BDMOASWorldIncomeEvidence {

  /**
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public List<BDMOASWorldIncomeEvidenceVO> getworldIncomeEvidenceValueObject(
    final long concernRoleID) throws AppException, InformationalException {

    final List<BDMOASWorldIncomeEvidenceVO> worldIncomeEvidenceList =
      new ArrayList<>();
    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new BDMEvidenceUtil().getEvdDtlsByConcernroleIDandEvidenceTypeInACOrIC(
        concernRoleID, CASEEVIDENCE.OAS_WORLD_INCOME);
    final BDMLifeEventUtil bdmLifeEventUtil = new BDMLifeEventUtil();

    for (final DynamicEvidenceDataDetails details : evidenceDataDetailsList) {
      final BDMOASWorldIncomeEvidenceVO worldIncomeEvidenceVO =
        new BDMOASWorldIncomeEvidenceVO();
      bdmLifeEventUtil.setEvidenceData(worldIncomeEvidenceVO, details);
      worldIncomeEvidenceList.add(worldIncomeEvidenceVO);
    }

    return worldIncomeEvidenceList;
  }

  /**
   *
   * @param concernRoleID
   * @param evidenceDetails
   * @throws AppException
   * @throws InformationalException
   */
  public void createWorldIncomeEvidence(final long concernRoleID,
    final List<BDMOASWorldIncomeEvidenceVO> evidenceDetailsList,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    HashMap<String, String> evidenceData;
    final BDMLifeEventUtil bdmLifeEventUtil = new BDMLifeEventUtil();

    for (final BDMOASWorldIncomeEvidenceVO worldIncomeEvidenceVO : evidenceDetailsList) {
      evidenceData = bdmLifeEventUtil.getEvidenceData(worldIncomeEvidenceVO);

      final long evidenceID = worldIncomeEvidenceVO.getEvidenceID();

      if (evidenceID != 0) {
        BDMEvidenceUtil.modifyEvidenceForCase(concernRoleID, evidenceID,
          CASEEVIDENCE.OAS_WORLD_INCOME, evidenceData, evidenceChangeReason);
      } else {

        BDMEvidenceUtil.createACOrICDynamicEvidence(concernRoleID,
          evidenceData, CASEEVIDENCE.OAS_WORLD_INCOME, evidenceChangeReason,
          0);
      }
    }
  }
}
