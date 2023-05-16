package curam.ca.gc.bdm.lifeevent.impl;

import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.codetable.CASEEVIDENCE;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 *
 * The utility class for voluntary tax withhold evidence.
 */
public class BDMVoluntaryTaxWithholdEvidence {

  /**
   * Method to get an active evidence for the concern role and return the
   * evidence POJO with the evidence data.
   *
   * @param concernRoleID concern role identifier
   * @return return evidence POJO with the evidence data
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  public List<BDMVoluntaryTaxWithholdEvidenceVO>
    getVoluntaryTaxWithholdEvidenceValueObjectList(final long concernRoleID)
      throws AppException, InformationalException {

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new BDMEvidenceUtil().getEvdDtlsByConcernroleIDandEvidenceTypeInACOrIC(
        concernRoleID, CASEEVIDENCE.BDMVTW);

    final List<BDMVoluntaryTaxWithholdEvidenceVO> voluntaryTaxWithholdEvidenceVOList =
      new ArrayList<BDMVoluntaryTaxWithholdEvidenceVO>();

    for (final DynamicEvidenceDataDetails details : evidenceDataDetailsList) {

      final BDMVoluntaryTaxWithholdEvidenceVO voluntaryTaxWithholdEvidenceVO =
        new BDMVoluntaryTaxWithholdEvidenceVO();

      new BDMLifeEventUtil().setEvidenceData(voluntaryTaxWithholdEvidenceVO,
        details);

      voluntaryTaxWithholdEvidenceVOList.add(voluntaryTaxWithholdEvidenceVO);
    }

    // order the list by start date
    Collections.sort(voluntaryTaxWithholdEvidenceVOList,
      new Comparator<BDMVoluntaryTaxWithholdEvidenceVO>() {

        @Override
        public int compare(final BDMVoluntaryTaxWithholdEvidenceVO c1,
          final BDMVoluntaryTaxWithholdEvidenceVO c2) {

          return c1.getStartDate().compareTo(c2.getStartDate());
        }
      });
    return voluntaryTaxWithholdEvidenceVOList;
  }

  /**
   * Method to create or modify evidence with the evidence details in
   * POJO. This modifies the existing evidence if the evidence identifier is
   * present in the POJO otherwise creates a new evidence.
   *
   * @param concernRoleID concern role identifier
   * @param evidenceDetails evidence details
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  public void createOrUpdateVoluntaryTaxWithholdEvidence(
    final long concernRoleID,
    final BDMVoluntaryTaxWithholdEvidenceVO bdmVoluntaryTaxWithholdEvidenceVO,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    final long evidenceID = bdmVoluntaryTaxWithholdEvidenceVO.getEvidenceID();
    final HashMap<String, String> evidenceData = new BDMLifeEventUtil()
      .getEvidenceData(bdmVoluntaryTaxWithholdEvidenceVO);
    if (evidenceID == 0) {
      // Create new evidence
      BDMEvidenceUtil.createACOrICDynamicEvidence(concernRoleID, evidenceData,
        CASEEVIDENCE.BDMVTW, evidenceChangeReason, 0);
    } else {
      // modify the evidence
      BDMEvidenceUtil.modifyEvidenceForCase(concernRoleID, evidenceID,
        CASEEVIDENCE.BDMVTW, evidenceData, evidenceChangeReason);
    }

  }

}
