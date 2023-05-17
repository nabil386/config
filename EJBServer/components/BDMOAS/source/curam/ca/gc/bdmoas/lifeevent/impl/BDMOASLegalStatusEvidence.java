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
 * BDMOAS FEATURE 92921: Class Added
 * Class for Legal Status evidence.
 *
 * @author SMisal
 *
 */
public class BDMOASLegalStatusEvidence {

  /**
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public List<BDMOASLegalStatusEvidenceVO> getLegalStatusEvidenceValueObject(
    final long concernRoleID) throws AppException, InformationalException {

    final List<BDMOASLegalStatusEvidenceVO> legalStatusEvidenceList =
      new ArrayList<BDMOASLegalStatusEvidenceVO>();
    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new BDMEvidenceUtil().getEvdDtlsByConcernroleIDandEvidenceTypeInACOrIC(
        concernRoleID, CASEEVIDENCE.OAS_LEGAL_STATUS);
    final BDMLifeEventUtil bdmLifeEventUtil = new BDMLifeEventUtil();

    for (final DynamicEvidenceDataDetails details : evidenceDataDetailsList) {
      final BDMOASLegalStatusEvidenceVO legalStatusEvidenceVO =
        new BDMOASLegalStatusEvidenceVO();
      bdmLifeEventUtil.setEvidenceData(legalStatusEvidenceVO, details);
      legalStatusEvidenceList.add(legalStatusEvidenceVO);
    }

    // order the list by legal status start date
    Collections.sort(legalStatusEvidenceList,
      (c1, c2) -> c1.getStartDate().compareTo(c2.getStartDate()));

    return legalStatusEvidenceList;
  }

  /**
   *
   * @param concernRoleID
   * @param evidenceDetails
   * @throws AppException
   * @throws InformationalException
   */
  public void createLegalStatusEvidence(final long concernRoleID,
    final List<BDMOASLegalStatusEvidenceVO> evidenceDetailsList,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    HashMap<String, String> evidenceData;
    final BDMLifeEventUtil bdmLifeEventUtil = new BDMLifeEventUtil();

    for (final BDMOASLegalStatusEvidenceVO legalStatusEvidenceVO : evidenceDetailsList) {
      evidenceData = bdmLifeEventUtil.getEvidenceData(legalStatusEvidenceVO);

      final long evidenceID = legalStatusEvidenceVO.getEvidenceID();

      if (evidenceID != 0) {
        BDMEvidenceUtil.modifyEvidenceForCase(concernRoleID, evidenceID,
          CASEEVIDENCE.OAS_LEGAL_STATUS, evidenceData, evidenceChangeReason);
      } else {

        BDMEvidenceUtil.createACOrICDynamicEvidence(concernRoleID,
          evidenceData, CASEEVIDENCE.OAS_LEGAL_STATUS, evidenceChangeReason,
          0);
      }
    }
  }
}
