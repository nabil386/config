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
 * Class for Marital Relationship evidence.
 *
 * @author SMisal
 *
 */
public class BDMOASMaritalRelationshipEvidence {

  /**
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public List<BDMOASMaritalRelationshipEvidenceVO>
    getMaritalStatusEvidenceValueObject(final long concernRoleID)
      throws AppException, InformationalException {

    final List<BDMOASMaritalRelationshipEvidenceVO> maritalRelationshipEvidenceList =
      new ArrayList<BDMOASMaritalRelationshipEvidenceVO>();
    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new BDMEvidenceUtil().getEvdDtlsByConcernroleIDandEvidenceTypeInACOrIC(
        concernRoleID, CASEEVIDENCE.OAS_MARITAL_RELATIONSHIP);

    final BDMLifeEventUtil bdmLifeEventUtil = new BDMLifeEventUtil();

    for (final DynamicEvidenceDataDetails details : evidenceDataDetailsList) {
      final BDMOASMaritalRelationshipEvidenceVO maritalRelationshipEvidenceVO =
        new BDMOASMaritalRelationshipEvidenceVO();
      bdmLifeEventUtil.setEvidenceData(maritalRelationshipEvidenceVO,
        details);
      maritalRelationshipEvidenceList.add(maritalRelationshipEvidenceVO);
    }

    return maritalRelationshipEvidenceList;
  }

  /**
   *
   * @param concernRoleID
   * @param evidenceDetails
   * @throws AppException
   * @throws InformationalException
   */
  public void createMaritalStatusEvidence(final long concernRoleID,
    final List<BDMOASMaritalRelationshipEvidenceVO> evidenceDetailsList,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    HashMap<String, String> evidenceData;
    final BDMLifeEventUtil bdmLifeEventUtil = new BDMLifeEventUtil();

    for (final BDMOASMaritalRelationshipEvidenceVO maritalRelationshipEvidenceVO : evidenceDetailsList) {
      evidenceData =
        bdmLifeEventUtil.getEvidenceData(maritalRelationshipEvidenceVO);

      final long evidenceID = maritalRelationshipEvidenceVO.getEvidenceID();

      if (evidenceID != 0) {
        BDMEvidenceUtil.modifyEvidenceForCase(concernRoleID, evidenceID,
          CASEEVIDENCE.OAS_MARITAL_RELATIONSHIP, evidenceData,
          evidenceChangeReason);
      } else {

        BDMEvidenceUtil.createACOrICDynamicEvidence(concernRoleID,
          evidenceData, CASEEVIDENCE.OAS_MARITAL_RELATIONSHIP,
          evidenceChangeReason, 0);
      }
    }
  }
}
