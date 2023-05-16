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
 * Class for sponsorship evidence.
 *
 * @author SMisal
 *
 */
public class BDMOASSponsorshipEvidence {

  /**
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public List<BDMOASSponsorshipEvidenceVO>
    getResidencePeriodEvidenceValueObject(final long concernRoleID)
      throws AppException, InformationalException {

    final List<BDMOASSponsorshipEvidenceVO> sponsorshipEvidenceList =
      new ArrayList<>();
    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new BDMEvidenceUtil().getEvdDtlsByConcernroleIDandEvidenceTypeInACOrIC(
        concernRoleID, CASEEVIDENCE.OAS_SPONSORSHIP);

    final BDMLifeEventUtil bdmLifeEventUtil = new BDMLifeEventUtil();

    for (final DynamicEvidenceDataDetails details : evidenceDataDetailsList) {
      final BDMOASSponsorshipEvidenceVO sponsorshipEvidenceVO =
        new BDMOASSponsorshipEvidenceVO();
      bdmLifeEventUtil.setEvidenceData(sponsorshipEvidenceVO, details);
      sponsorshipEvidenceList.add(sponsorshipEvidenceVO);
    }

    return sponsorshipEvidenceList;
  }

  /**
   *
   * @param concernRoleID
   * @param evidenceDetails
   * @throws AppException
   * @throws InformationalException
   */
  public void createResidencePeriodEvidence(final long concernRoleID,
    final List<BDMOASSponsorshipEvidenceVO> evidenceDetailsList,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    HashMap<String, String> evidenceData;
    final BDMLifeEventUtil bdmLifeEventUtil = new BDMLifeEventUtil();

    for (final BDMOASSponsorshipEvidenceVO sponsorshipEvidenceVO : evidenceDetailsList) {
      evidenceData = bdmLifeEventUtil.getEvidenceData(sponsorshipEvidenceVO);

      final long evidenceID = sponsorshipEvidenceVO.getEvidenceID();

      if (evidenceID != 0) {
        BDMEvidenceUtil.modifyEvidenceForCase(concernRoleID, evidenceID,
          CASEEVIDENCE.OAS_SPONSORSHIP, evidenceData, evidenceChangeReason);
      } else {

        BDMEvidenceUtil.createACOrICDynamicEvidence(concernRoleID,
          evidenceData, CASEEVIDENCE.OAS_SPONSORSHIP, evidenceChangeReason,
          0);
      }
    }
  }
}
