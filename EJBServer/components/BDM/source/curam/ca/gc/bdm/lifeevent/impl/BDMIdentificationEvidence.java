package curam.ca.gc.bdm.lifeevent.impl;

import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.pdc.impl.PDCConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @since
 *
 */
public class BDMIdentificationEvidence {

  /**
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public BDMIdentificationEvidenceVO getSINEvidenceValueObject(
    final long concernRoleID) throws AppException, InformationalException {

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new BDMEvidenceUtil().getEvdDtlsByConcernroleIDandEvidenceType(
        concernRoleID, PDCConst.PDCIDENTIFICATION);

    final BDMIdentificationEvidenceVO identificationEvidenceVO =
      new BDMIdentificationEvidenceVO();

    for (final DynamicEvidenceDataDetails details : evidenceDataDetailsList) {

      final String alternateIDType =
        details.getAttribute("altIDType").getValue();

      if (!CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER
        .equals(alternateIDType)) {
        continue;
      }

      new BDMLifeEventUtil().setEvidenceData(identificationEvidenceVO,
        details);
      break;
    }

    return identificationEvidenceVO;
  }

  /**
   *
   * @param concernRoleID
   * @param evidenceDetails
   * @throws AppException
   * @throws InformationalException
   */
  public void createIdentificationEvidence(final long concernRoleID,
    final BDMIdentificationEvidenceVO evidenceDetails,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    final HashMap<String, String> evidenceData =
      new BDMLifeEventUtil().getEvidenceData(evidenceDetails);

    if (evidenceDetails.getEvidenceID() != 0) {
      // end date the current existing identification evidence
      final Date endDate = evidenceDetails.getFromDate();
      final long evidenceID = evidenceDetails.getEvidenceID();

      BDMEvidenceUtil.endDateEvidence(evidenceID, endDate,
        PDCConst.PDCIDENTIFICATION, evidenceChangeReason);
    }
    // create new identification evidence
    BDMEvidenceUtil.createPDCDynamicEvidence(concernRoleID, evidenceData,
      PDCConst.PDCIDENTIFICATION, evidenceChangeReason);
  }

  /**
   *
   * Create SIN Identification evidence for Guided Change
   * TASK 26338
   *
   * @param concernRoleID
   * @param evidenceDetails
   * @throws AppException
   * @throws InformationalException
   */
  public void createSINIdentificationEvidence(final long concernRoleID,
    final Date sinIdentificationExistingEndDate,
    final BDMIdentificationEvidenceVO evidenceDetails,
    final String evidenceChangeReason,
    final Date sinIdentificationExistingStartDate)
    throws AppException, InformationalException {

    final HashMap<String, String> evidenceData =
      new BDMLifeEventUtil().getEvidenceData(evidenceDetails);

    if (evidenceDetails.getEvidenceID() != 0) {
      // end date the current existing identification evidence
      final Date endDate = sinIdentificationExistingEndDate.isZero()
        ? evidenceDetails.getFromDate().addDays(-1)
        : sinIdentificationExistingEndDate;
      final long evidenceID = evidenceDetails.getEvidenceID();
      final Date startDate = evidenceDetails.getFromDate();
      if (startDate.equals(sinIdentificationExistingStartDate)) {
        BDMEvidenceUtil.modifyPDCDynamicEvidence(concernRoleID, evidenceID,
          PDCConst.PDCIDENTIFICATION, evidenceData, evidenceChangeReason);
      } else {
        BDMEvidenceUtil.endDateEvidence(evidenceID, endDate,
          PDCConst.PDCIDENTIFICATION, evidenceChangeReason);
        // create new identification evidence
        BDMEvidenceUtil.createPDCDynamicEvidence(concernRoleID, evidenceData,
          PDCConst.PDCIDENTIFICATION, evidenceChangeReason);
      }
    } else {
      // create new identification evidence
      BDMEvidenceUtil.createPDCDynamicEvidence(concernRoleID, evidenceData,
        PDCConst.PDCIDENTIFICATION, evidenceChangeReason);
    }
  }
}
