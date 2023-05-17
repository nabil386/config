package curam.ca.gc.bdmoas.lifeevent.impl;

import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.lifeevent.impl.BDMLifeEventUtil;
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
 * BDMOAS FEATURE 92921: Class Added
 * Class for Living Apart for Reasons Beyond Control evidence.
 *
 * @author SMisal
 *
 */
public class BDMOASLivingApartforReasonsBeyondControlEvidence {

  /**
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public List<BDMOASLivingApartforReasonsBeyondControlEvidenceVO>
    getLegalStatusEvidenceValueObject(final long concernRoleID)
      throws AppException, InformationalException {

    final List<BDMOASLivingApartforReasonsBeyondControlEvidenceVO> livingApartEvidenceList =
      new ArrayList<BDMOASLivingApartforReasonsBeyondControlEvidenceVO>();
    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new BDMEvidenceUtil().getEvdDtlsByConcernroleIDandEvidenceTypeInACOrIC(
        concernRoleID,
        CASEEVIDENCE.OAS_LIVING_APART_FOR_REASONS_BEYOND_CONTROL);

    final BDMLifeEventUtil bdmLifeEventUtil = new BDMLifeEventUtil();

    for (final DynamicEvidenceDataDetails details : evidenceDataDetailsList) {
      final BDMOASLivingApartforReasonsBeyondControlEvidenceVO livingApartEvidenceVO =
        new BDMOASLivingApartforReasonsBeyondControlEvidenceVO();
      bdmLifeEventUtil.setEvidenceData(livingApartEvidenceVO, details);
      livingApartEvidenceList.add(livingApartEvidenceVO);
    }

    // order the list by legal status start date
    Collections.sort(livingApartEvidenceList,
      new Comparator<BDMOASLivingApartforReasonsBeyondControlEvidenceVO>() {

        @Override
        public int compare(
          final BDMOASLivingApartforReasonsBeyondControlEvidenceVO c1,
          final BDMOASLivingApartforReasonsBeyondControlEvidenceVO c2) {

          return c1.getStartDate().compareTo(c2.getStartDate());
        }
      });

    return livingApartEvidenceList;
  }

  /**
   *
   * @param concernRoleID
   * @param evidenceDetails
   * @throws AppException
   * @throws InformationalException
   */
  public void createLegalStatusEvidence(final long concernRoleID,
    final List<BDMOASLivingApartforReasonsBeyondControlEvidenceVO> evidenceDetailsList,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    HashMap<String, String> evidenceData;
    final BDMLifeEventUtil bdmLifeEventUtil = new BDMLifeEventUtil();

    for (final BDMOASLivingApartforReasonsBeyondControlEvidenceVO livingApartEvidenceVO : evidenceDetailsList) {
      evidenceData = bdmLifeEventUtil.getEvidenceData(livingApartEvidenceVO);

      final long evidenceID = livingApartEvidenceVO.getEvidenceID();

      if (evidenceID != 0) {
        BDMEvidenceUtil.modifyEvidenceForCase(concernRoleID, evidenceID,
          CASEEVIDENCE.OAS_LIVING_APART_FOR_REASONS_BEYOND_CONTROL,
          evidenceData, evidenceChangeReason);
      } else {

        BDMEvidenceUtil.createACOrICDynamicEvidence(concernRoleID,
          evidenceData,
          CASEEVIDENCE.OAS_LIVING_APART_FOR_REASONS_BEYOND_CONTROL,
          evidenceChangeReason, 0);
      }
    }
  }
}
