package curam.ca.gc.bdm.lifeevent.impl;

import curam.ca.gc.bdm.codetable.BDMPHONENUMBERCHANGETYPE;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.core.sl.struct.ReturnEvidenceDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.pdc.impl.PDCConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BDMPhoneEvidence {

  /**
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public List<BDMPhoneEvidenceVO> getEvidenceValueObject(
    final long concernRoleID) throws AppException, InformationalException {

    final List<BDMPhoneEvidenceVO> phoneEvidenceList =
      new ArrayList<BDMPhoneEvidenceVO>();

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new BDMEvidenceUtil().getEvdDtlsByConcernroleIDandEvidenceType(
        concernRoleID, PDCConst.PDCPHONENUMBER);

    for (final DynamicEvidenceDataDetails details : evidenceDataDetailsList) {

      final BDMPhoneEvidenceVO phoneEvidenceVO = new BDMPhoneEvidenceVO();

      new BDMLifeEventUtil().setEvidenceData(phoneEvidenceVO, details);

      phoneEvidenceList.add(phoneEvidenceVO);
    }

    return phoneEvidenceList;
  }

  /**
   *
   * @param concernRoleID
   * @param evidenceDetails
   * @throws AppException
   * @throws InformationalException
   */
  public void createPhoneEvidence(final long concernRoleID,
    final List<BDMPhoneEvidenceVO> evidenceDetailsList,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    for (final BDMPhoneEvidenceVO phoneEvidenceVO : evidenceDetailsList) {

      final long evidenceID = phoneEvidenceVO.getEvidenceID();

      if (evidenceID != 0) {
        if (phoneEvidenceVO.getPhoneNumberChangeType()
          .equals(BDMPHONENUMBERCHANGETYPE.UPDATE)) {
          BDMEvidenceUtil.modifyEvidence(evidenceID, PDCConst.PDCPHONENUMBER,
            phoneEvidenceVO, evidenceChangeReason, true);
        }
        if (phoneEvidenceVO.getPhoneNumberChangeType()
          .equals(BDMPHONENUMBERCHANGETYPE.REMOVE)) {
          if (phoneEvidenceVO.isUseForAlertsInd()) {
            // first modify the phone evidence to clear the alert indicator
            phoneEvidenceVO.setUseForAlertsInd(false);
            final HashMap<String, String> evidenceData =
              new BDMLifeEventUtil().getEvidenceData(phoneEvidenceVO);
            final ReturnEvidenceDetails returnEvidenceDetails =
              BDMEvidenceUtil.modifyPDCDynamicEvidence(concernRoleID,
                evidenceID, PDCConst.PDCPHONENUMBER, evidenceData,
                evidenceChangeReason);
            // then remove the evidence
            BDMEvidenceUtil.removeEvidence(
              returnEvidenceDetails.evidenceKey.evidenceID,
              PDCConst.PDCPHONENUMBER);
          } else {
            BDMEvidenceUtil.removeEvidence(evidenceID,
              PDCConst.PDCPHONENUMBER);
          }
        }

      } else {
        final HashMap<String, String> evidenceData =
          new BDMLifeEventUtil().getEvidenceData(phoneEvidenceVO);
        BDMEvidenceUtil.createPDCDynamicEvidence(concernRoleID, evidenceData,
          PDCConst.PDCPHONENUMBER, evidenceChangeReason);
      }
    }
  }
}
