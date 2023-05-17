package curam.ca.gc.bdm.lifeevent.impl;

import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.codetable.CASEEVIDENCE;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BDMIncomeEvidence {

  /**
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public List<BDMIncomeEvidenceVO> getEvidenceValueObject(
    final long concernRoleID) throws AppException, InformationalException {

    final List<BDMIncomeEvidenceVO> incomeEvidenceList =
      new ArrayList<BDMIncomeEvidenceVO>();
    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new BDMEvidenceUtil().getEvdDtlsByConcernroleIDandEvidenceTypeInACOrIC(
        concernRoleID, CASEEVIDENCE.BDMINCOME);

    for (final DynamicEvidenceDataDetails details : evidenceDataDetailsList) {
      final BDMIncomeEvidenceVO incomeEvidenceVO = new BDMIncomeEvidenceVO();
      new BDMLifeEventUtil().setEvidenceData(incomeEvidenceVO, details);
      incomeEvidenceList.add(incomeEvidenceVO);
    }
    return incomeEvidenceList;
  }

  /**
   *
   * @param concernRoleID
   * @param evidenceDetails
   * @throws AppException
   * @throws InformationalException
   */
  public void createIncomeEvidence(final long concernRoleID,
    final List<BDMIncomeEvidenceVO> evidenceDetailsList,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    for (final BDMIncomeEvidenceVO incomeEvidenceVO : evidenceDetailsList) {
      final long evidenceID = incomeEvidenceVO.getEvidenceID();

      final HashMap<String, String> evidenceData =
        new BDMLifeEventUtil().getEvidenceData(incomeEvidenceVO);
      if (evidenceID != 0) {
        BDMEvidenceUtil.modifyEvidenceForCase(concernRoleID, evidenceID,
          CASEEVIDENCE.BDMINCOME, evidenceData, evidenceChangeReason);
      } else {

        BDMEvidenceUtil.createACOrICDynamicEvidence(concernRoleID,
          evidenceData, CASEEVIDENCE.BDMINCOME, evidenceChangeReason, 0);
      }
    }
  }

}
