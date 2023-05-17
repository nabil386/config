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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * BDMOAS FEATURE 92921:
 * Retirement Pension Reduction Evidence
 *
 * @author abid.a.khan
 *
 */
public class BDMOASRetirementPensionReductionEvidence {

  /**
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public List<BDMOASRetirementPensionReductionEvidenceVO>
    getRetirementPensionReductionEvidenceValueObject(final long concernRoleID)
      throws AppException, InformationalException {

    final List<BDMOASRetirementPensionReductionEvidenceVO> retirementPensionReductionEvidenceList =
      new ArrayList<>();
    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new BDMEvidenceUtil().getEvdDtlsByConcernroleIDandEvidenceTypeInACOrIC(
        concernRoleID, CASEEVIDENCE.OAS_RETIREMENT_PENSION_REDUCTION);
    final BDMLifeEventUtil bdmLifeEventUtil = new BDMLifeEventUtil();

    for (final DynamicEvidenceDataDetails details : evidenceDataDetailsList) {
      final BDMOASRetirementPensionReductionEvidenceVO retirementPensionReductionEvidenceVO =
        new BDMOASRetirementPensionReductionEvidenceVO();
      bdmLifeEventUtil.setEvidenceData(retirementPensionReductionEvidenceVO,
        details);
      retirementPensionReductionEvidenceList
        .add(retirementPensionReductionEvidenceVO);
    }

    // order the list by legal status start date
    Collections.sort(retirementPensionReductionEvidenceList,
      (c1, c2) -> c1.getEventDate().compareTo(c2.getEventDate()));

    return retirementPensionReductionEvidenceList;
  }

  /**
   *
   * @param concernRoleID
   * @param evidenceDetails
   * @throws AppException
   * @throws InformationalException
   */
  public void createRetirementPensionReductionEvidence(
    final long concernRoleID,
    final List<BDMOASRetirementPensionReductionEvidenceVO> evidenceDetailsList,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    HashMap<String, String> evidenceData;
    final BDMLifeEventUtil bdmLifeEventUtil = new BDMLifeEventUtil();

    for (final BDMOASRetirementPensionReductionEvidenceVO retirementPensionReductionEvidenceVO : evidenceDetailsList) {
      evidenceData = bdmLifeEventUtil
        .getEvidenceData(retirementPensionReductionEvidenceVO);

      final long evidenceID =
        retirementPensionReductionEvidenceVO.getEvidenceID();

      if (evidenceID != 0) {
        BDMEvidenceUtil.modifyEvidenceForCase(concernRoleID, evidenceID,
          CASEEVIDENCE.OAS_RETIREMENT_PENSION_REDUCTION, evidenceData,
          evidenceChangeReason);
      } else {

        BDMEvidenceUtil.createACOrICDynamicEvidence(concernRoleID,
          evidenceData, CASEEVIDENCE.OAS_RETIREMENT_PENSION_REDUCTION,
          evidenceChangeReason, 0);
      }
    }
  }

}
