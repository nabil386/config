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
 * Class for First Entry Into Canada Details evidence.
 *
 * @author SMisal
 *
 */
public class BDMOASFirstEntryIntoCanadaDetailsEvidence {

  /**
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public List<BDMOASFirstEntryIntoCanadaDetailsEvidenceVO>
    getFirstEntryIntoCanadaDetailsEvidenceValueObject(
      final long concernRoleID) throws AppException, InformationalException {

    final List<BDMOASFirstEntryIntoCanadaDetailsEvidenceVO> firstEntryIntoCanadaDetailsEvidenceList =
      new ArrayList<>();
    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new BDMEvidenceUtil().getEvdDtlsByConcernroleIDandEvidenceTypeInACOrIC(
        concernRoleID, CASEEVIDENCE.OAS_FIRST_ENTRY_INTO_CANADA);

    final BDMLifeEventUtil bdmLifeEventUtil = new BDMLifeEventUtil();

    for (final DynamicEvidenceDataDetails details : evidenceDataDetailsList) {
      final BDMOASFirstEntryIntoCanadaDetailsEvidenceVO firstEntryIntoCanadaDetailsEvidenceVO =
        new BDMOASFirstEntryIntoCanadaDetailsEvidenceVO();
      bdmLifeEventUtil.setEvidenceData(firstEntryIntoCanadaDetailsEvidenceVO,
        details);
      firstEntryIntoCanadaDetailsEvidenceList
        .add(firstEntryIntoCanadaDetailsEvidenceVO);
    }

    return firstEntryIntoCanadaDetailsEvidenceList;
  }

  /**
   *
   * @param concernRoleID
   * @param evidenceDetails
   * @throws AppException
   * @throws InformationalException
   */
  public void createFirstEntryIntoCanadaDetailsEvidence(
    final long concernRoleID,
    final List<BDMOASFirstEntryIntoCanadaDetailsEvidenceVO> evidenceDetailsList,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    HashMap<String, String> evidenceData;

    final BDMLifeEventUtil bdmLifeEventUtil = new BDMLifeEventUtil();

    for (final BDMOASFirstEntryIntoCanadaDetailsEvidenceVO firstEntryIntoCanadaDetailsEvidenceVO : evidenceDetailsList) {
      evidenceData = bdmLifeEventUtil
        .getEvidenceData(firstEntryIntoCanadaDetailsEvidenceVO);

      final long evidenceID =
        firstEntryIntoCanadaDetailsEvidenceVO.getEvidenceID();

      if (evidenceID != 0) {
        BDMEvidenceUtil.modifyEvidenceForCase(concernRoleID, evidenceID,
          CASEEVIDENCE.OAS_FIRST_ENTRY_INTO_CANADA, evidenceData,
          evidenceChangeReason);
      } else {

        BDMEvidenceUtil.createACOrICDynamicEvidence(concernRoleID,
          evidenceData, CASEEVIDENCE.OAS_FIRST_ENTRY_INTO_CANADA,
          evidenceChangeReason, 0);
      }
    }
  }
}
