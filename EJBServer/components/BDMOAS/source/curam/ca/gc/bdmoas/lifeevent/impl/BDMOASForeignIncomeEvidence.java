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
 * Class for Foreign Income evidence.
 *
 * @author SMisal
 *
 */
public class BDMOASForeignIncomeEvidence {

  /**
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public List<BDMOASForeignIncomeEvidenceVO> getEvidenceValueObject(
    final long concernRoleID) throws AppException, InformationalException {

    final List<BDMOASForeignIncomeEvidenceVO> foreignIncomeEvidenceList =
      new ArrayList<BDMOASForeignIncomeEvidenceVO>();

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new BDMEvidenceUtil().getEvdDtlsByConcernroleIDandEvidenceTypeInACOrIC(
        concernRoleID, CASEEVIDENCE.OAS_FOREIGN_INCOME);

    final BDMLifeEventUtil bdmLifeEventUtil = new BDMLifeEventUtil();

    for (final DynamicEvidenceDataDetails details : evidenceDataDetailsList) {
      final BDMOASForeignIncomeEvidenceVO foreignIncomeEvidenceVO =
        new BDMOASForeignIncomeEvidenceVO();
      bdmLifeEventUtil.setEvidenceData(foreignIncomeEvidenceVO, details);
      foreignIncomeEvidenceList.add(foreignIncomeEvidenceVO);
    }
    return foreignIncomeEvidenceList;
  }

  /**
   *
   * @param concernRoleID
   * @param evidenceDetails
   * @throws AppException
   * @throws InformationalException
   */
  public void createIncomeEvidence(final long concernRoleID,
    final List<BDMOASForeignIncomeEvidenceVO> evidenceDetailsList,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    final BDMLifeEventUtil bdmLifeEventUtil = new BDMLifeEventUtil();

    for (final BDMOASForeignIncomeEvidenceVO incomeEvidenceVO : evidenceDetailsList) {
      final long evidenceID = incomeEvidenceVO.getEvidenceID();

      final HashMap<String, String> evidenceData =
        bdmLifeEventUtil.getEvidenceData(incomeEvidenceVO);
      if (evidenceID != 0) {

        BDMEvidenceUtil.modifyEvidenceForCase(concernRoleID, evidenceID,
          CASEEVIDENCE.OAS_FOREIGN_INCOME, evidenceData,
          evidenceChangeReason);
      } else {

        BDMEvidenceUtil.createACOrICDynamicEvidence(concernRoleID,
          evidenceData, CASEEVIDENCE.OAS_FOREIGN_INCOME, evidenceChangeReason,
          0);
      }
    }
  }

}
