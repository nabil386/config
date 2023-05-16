package curam.ca.gc.bdm.lifeevent.impl;

import curam.ca.gc.bdm.codetable.BDMINCARCERATIONCHANGETYPE;
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

public class BDMIncarcerationEvidence {

  /**
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public List<BDMIncarcerationEvidenceVO> getEvidenceValueObject(
    final long concernRoleID) throws AppException, InformationalException {

    final List<BDMIncarcerationEvidenceVO> incarcerationEvidenceList =
      new ArrayList<BDMIncarcerationEvidenceVO>();
    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new BDMEvidenceUtil().getEvdDtlsByConcernroleIDandEvidenceTypeInACOrIC(
        concernRoleID, CASEEVIDENCE.BDMINCARCERATION);

    for (final DynamicEvidenceDataDetails details : evidenceDataDetailsList) {
      final BDMIncarcerationEvidenceVO incarcerationEvidenceVO =
        new BDMIncarcerationEvidenceVO();
      new BDMLifeEventUtil().setEvidenceData(incarcerationEvidenceVO,
        details);
      incarcerationEvidenceList.add(incarcerationEvidenceVO);
    }

    // order the list by incarceration start date
    Collections.sort(incarcerationEvidenceList,
      new Comparator<BDMIncarcerationEvidenceVO>() {

        @Override
        public int compare(final BDMIncarcerationEvidenceVO c1,
          final BDMIncarcerationEvidenceVO c2) {

          return c1.getStartDate().compareTo(c2.getStartDate());
        }
      });

    return incarcerationEvidenceList;
  }

  /**
   *
   * @param concernRoleID
   * @param evidenceDetails
   * @throws AppException
   * @throws InformationalException
   */
  public void createIncarcerationEvidence(final long concernRoleID,
    final List<BDMIncarcerationEvidenceVO> evidenceDetailsList,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    HashMap<String, String> evidenceData;

    for (final BDMIncarcerationEvidenceVO incarcerationEvidenceVO : evidenceDetailsList) {
      evidenceData =
        new BDMLifeEventUtil().getEvidenceData(incarcerationEvidenceVO);

      final long evidenceID = incarcerationEvidenceVO.getEvidenceID();

      if (evidenceID != 0) {
        if (incarcerationEvidenceVO.getIncarcerationChangeType()
          .equals(BDMINCARCERATIONCHANGETYPE.UPDATE)) {
          BDMEvidenceUtil.modifyEvidenceForCase(concernRoleID, evidenceID,
            CASEEVIDENCE.BDMINCARCERATION, evidenceData,
            evidenceChangeReason);
        }
        if (incarcerationEvidenceVO.getIncarcerationChangeType()
          .equals(BDMINCARCERATIONCHANGETYPE.REMOVE)) {
          BDMEvidenceUtil.removeEvidence(evidenceID,
            CASEEVIDENCE.BDMINCARCERATION);
        }
      } else {

        BDMEvidenceUtil.createACOrICDynamicEvidence(concernRoleID,
          evidenceData, CASEEVIDENCE.BDMINCARCERATION, evidenceChangeReason,
          0);
      }
    }
  }
}
