package curam.ca.gc.bdm.lifeevent.impl;

import curam.ca.gc.bdm.codetable.BDMEMAILADDRESSCHANGETYPE;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.core.sl.struct.ReturnEvidenceDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.pdc.impl.PDCConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BDMEmailEvidence {

  /**
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public List<BDMEmailEvidenceVO> getEvidenceValueObject(
    final long concernRoleID) throws AppException, InformationalException {

    final List<BDMEmailEvidenceVO> emailEvidenceList =
      new ArrayList<BDMEmailEvidenceVO>();

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new BDMEvidenceUtil().getEvdDtlsByConcernroleIDandEvidenceType(
        concernRoleID, PDCConst.PDCEMAILADDRESS);

    for (final DynamicEvidenceDataDetails details : evidenceDataDetailsList) {

      final BDMEmailEvidenceVO emailEvidenceVO = new BDMEmailEvidenceVO();

      new BDMLifeEventUtil().setEvidenceData(emailEvidenceVO, details);

      emailEvidenceList.add(emailEvidenceVO);
    }
    return emailEvidenceList;
  }

  /**
   *
   * @param concernRoleID
   * @param evidenceDetails
   * @throws AppException
   * @throws InformationalException
   */
  public void createEmailEvidence(final long concernRoleID,
    final List<BDMEmailEvidenceVO> evidenceDetailsList,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    for (final BDMEmailEvidenceVO emailEvidenceVO : evidenceDetailsList) {

      final long evidenceID = emailEvidenceVO.getEvidenceID();

      if (evidenceID != 0) {
        if (emailEvidenceVO.getEmailAddressChangeType()
          .equals(BDMEMAILADDRESSCHANGETYPE.UPDATE)) {
          BDMEvidenceUtil.modifyEvidence(evidenceID, PDCConst.PDCEMAILADDRESS,
            emailEvidenceVO, evidenceChangeReason, true);
        }
        if (emailEvidenceVO.getEmailAddressChangeType()
          .equals(BDMEMAILADDRESSCHANGETYPE.REMOVE)) {
          if (emailEvidenceVO.isUseForAlertsInd()) {
            // first modify the phone evidence to clear the alert indicator
            emailEvidenceVO.setUseForAlertsInd(false);
            final HashMap<String, String> evidenceData =
              new BDMLifeEventUtil().getEvidenceData(emailEvidenceVO);
            final ReturnEvidenceDetails returnEvidenceDetails =
              BDMEvidenceUtil.modifyPDCDynamicEvidence(concernRoleID,
                evidenceID, PDCConst.PDCEMAILADDRESS, evidenceData,
                evidenceChangeReason);
            // then remove the evidence
            BDMEvidenceUtil.removeEvidence(
              returnEvidenceDetails.evidenceKey.evidenceID,
              PDCConst.PDCEMAILADDRESS);
          } else {
            BDMEvidenceUtil.removeEvidence(evidenceID,
              PDCConst.PDCEMAILADDRESS);
          }
        }
      } else {

        final HashMap<String, String> evidenceData =
          new BDMLifeEventUtil().getEvidenceData(emailEvidenceVO);

        BDMEvidenceUtil.createPDCDynamicEvidence(concernRoleID, evidenceData,
          PDCConst.PDCEMAILADDRESS, evidenceChangeReason);
      }
    }
  }

}
