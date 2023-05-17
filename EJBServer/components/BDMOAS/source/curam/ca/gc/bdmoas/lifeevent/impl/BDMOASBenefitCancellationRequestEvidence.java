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
 * Benefit Cancellation Request Evidence
 *
 * @author abid.a.khan
 *
 */
public class BDMOASBenefitCancellationRequestEvidence {

  /**
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public List<BDMOASBenefitCancellationRequestEvidenceVO>
    getRetirementPensionReductionEvidenceValueObject(final long concernRoleID)
      throws AppException, InformationalException {

    final List<BDMOASBenefitCancellationRequestEvidenceVO> benefitCancellationEvidenceList =
      new ArrayList<>();
    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new BDMEvidenceUtil().getEvdDtlsByConcernroleIDandEvidenceTypeInACOrIC(
        concernRoleID, CASEEVIDENCE.OAS_BENEFIT_CANCELLATION);

    final BDMLifeEventUtil bdmLifeEventUtil = new BDMLifeEventUtil();

    for (final DynamicEvidenceDataDetails details : evidenceDataDetailsList) {
      final BDMOASBenefitCancellationRequestEvidenceVO benefitCancellationEvidenceVO =
        new BDMOASBenefitCancellationRequestEvidenceVO();
      bdmLifeEventUtil.setEvidenceData(benefitCancellationEvidenceVO,
        details);
      benefitCancellationEvidenceList.add(benefitCancellationEvidenceVO);
    }

    // order the list by request date
    Collections.sort(benefitCancellationEvidenceList,
      (c1, c2) -> c1.getRequestDate().compareTo(c2.getRequestDate()));

    return benefitCancellationEvidenceList;
  }

  /**
   *
   * @param concernRoleID
   * @param evidenceDetails
   * @throws AppException
   * @throws InformationalException
   */
  public void createBenefitCancellationEvidence(final long concernRoleID,
    final List<BDMOASBenefitCancellationRequestEvidenceVO> evidenceDetailsList,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    HashMap<String, String> evidenceData;

    final BDMLifeEventUtil bdmLifeEventUtil = new BDMLifeEventUtil();

    for (final BDMOASBenefitCancellationRequestEvidenceVO benefitCancellationEvidenceVO : evidenceDetailsList) {
      evidenceData =
        bdmLifeEventUtil.getEvidenceData(benefitCancellationEvidenceVO);

      final long evidenceID = benefitCancellationEvidenceVO.getEvidenceID();

      if (evidenceID != 0) {
        BDMEvidenceUtil.modifyEvidenceForCase(concernRoleID, evidenceID,
          CASEEVIDENCE.OAS_BENEFIT_CANCELLATION, evidenceData,
          evidenceChangeReason);
      } else {

        BDMEvidenceUtil.createACOrICDynamicEvidence(concernRoleID,
          evidenceData, CASEEVIDENCE.OAS_BENEFIT_CANCELLATION,
          evidenceChangeReason, 0);
      }
    }
  }

}
