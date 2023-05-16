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
 * @author abid.a.khan
 *
 */
public class BDMOASManuallyEnteredIncomeEvidence {

  /**
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public List<BDMOASManuallyEnteredIncomeEvidenceVO>
    getManuallyEnteredIncomeEvidenceValueObject(final long concernRoleID)
      throws AppException, InformationalException {

    final List<BDMOASManuallyEnteredIncomeEvidenceVO> manuallyEnteredIncomeEvidenceList =
      new ArrayList<>();
    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new BDMEvidenceUtil().getEvdDtlsByConcernroleIDandEvidenceTypeInACOrIC(
        concernRoleID, CASEEVIDENCE.OAS_MANUALLY_ENTERED_INCOME);

    final BDMLifeEventUtil bdmLifeEventUtil = new BDMLifeEventUtil();

    for (final DynamicEvidenceDataDetails details : evidenceDataDetailsList) {
      final BDMOASManuallyEnteredIncomeEvidenceVO manuallyEnteredIncomeEvidenceVO =
        new BDMOASManuallyEnteredIncomeEvidenceVO();
      bdmLifeEventUtil.setEvidenceData(manuallyEnteredIncomeEvidenceVO,
        details);
      manuallyEnteredIncomeEvidenceList.add(manuallyEnteredIncomeEvidenceVO);
    }

    return manuallyEnteredIncomeEvidenceList;
  }

  /**
   *
   * @param concernRoleID
   * @param evidenceDetails
   * @throws AppException
   * @throws InformationalException
   */
  public void createManuallyEnteredIncomeEvidence(final long concernRoleID,
    final List<BDMOASManuallyEnteredIncomeEvidenceVO> evidenceDetailsList,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    HashMap<String, String> evidenceData;
    final BDMLifeEventUtil bdmLifeEventUtil = new BDMLifeEventUtil();

    for (final BDMOASManuallyEnteredIncomeEvidenceVO manuallyEnteredIncomeEvidenceVO : evidenceDetailsList) {
      evidenceData =
        bdmLifeEventUtil.getEvidenceData(manuallyEnteredIncomeEvidenceVO);

      final long evidenceID = manuallyEnteredIncomeEvidenceVO.getEvidenceID();

      if (evidenceID != 0) {
        BDMEvidenceUtil.modifyEvidenceForCase(concernRoleID, evidenceID,
          CASEEVIDENCE.OAS_MANUALLY_ENTERED_INCOME, evidenceData,
          evidenceChangeReason);
      } else {

        BDMEvidenceUtil.createACOrICDynamicEvidence(concernRoleID,
          evidenceData, CASEEVIDENCE.OAS_MANUALLY_ENTERED_INCOME,
          evidenceChangeReason, 0);
      }
    }
  }

}
