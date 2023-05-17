package curam.ca.gc.bdmoas.evidence.events.impl;

import curam.ca.gc.bdm.evidence.events.impl.BDMAbstractEvidenceEventHandler;
import curam.ca.gc.bdmoas.codetable.OASRESIDENCETYPE;
import curam.ca.gc.bdmoas.evidence.constants.impl.OASResidencePeriodConstants;
import curam.ca.gc.bdmoas.message.impl.BDMOASEVIDENCEMESSAGEExceptionCreator;
import curam.ca.gc.bdmoas.util.impl.BDMOASEvidenceUtil;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.COUNTRY;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.ApplyChangesEvidenceLists;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.infrastructure.struct.EvidenceKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.StringUtil;
import curam.util.type.Date;
import java.util.Arrays;
import java.util.List;

// 116400: DEV: Implement Manage Creditable Periods for OAS under IA - R2
/**
 * An evidence event class for listening and handling Foreign Residence Period
 * evidence events.
 */
public class BDMOASForeignResidencePeriodEventHandler
  extends BDMAbstractEvidenceEventHandler {

  @Override
  public CASEEVIDENCEEntry evidenceType() {

    return CASEEVIDENCEEntry.BDM_FOREIGN_RESIDENCE_PERIOD;

  }

  @Override
  public boolean subscribePreActivation() {

    return true;

  }

  /**
   * Checks for overlap exceptions
   */
  @Override
  public void preActivation(
    final EvidenceControllerInterface evidenceController,
    final CaseKey caseKey, final ApplyChangesEvidenceLists evidenceLists)
    throws AppException, InformationalException {

    final ApplyChangesEvidenceLists filteredEvidenceLists =
      this.filterEvidenceLists(evidenceLists);

    if (isOverlappingOASResidencePeriodExists(evidenceController, caseKey,
      filteredEvidenceLists)) {
      throw BDMOASEVIDENCEMESSAGEExceptionCreator
        .ERR_OVERLAPPING_OAS_CANADIAN_RESIDENCE_PERIOD();
    }

  }

  /**
   * Checks if there is an active overlapping OAS Residence period for the
   * participant.
   *
   * @param evidenceController
   * @param caseKey
   * @param filteredEvidenceLists
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private boolean isOverlappingOASResidencePeriodExists(
    final EvidenceControllerInterface evidenceController,
    final CaseKey caseKey,
    final ApplyChangesEvidenceLists filteredEvidenceLists)
    throws AppException, InformationalException {

    final EIEvidenceKey readEvidenceKey = new EIEvidenceKey();

    for (final EvidenceKey evidenceKey : filteredEvidenceLists.newAndUpdateList.dtls) {

      // Read Foreign Residence Period evidence data
      readEvidenceKey.evidenceID = evidenceKey.evidenceID;
      readEvidenceKey.evidenceType =
        CASEEVIDENCE.BDM_FOREIGN_RESIDENCE_PERIOD;
      final EIEvidenceReadDtls foreignResidenceEvidenceReadDtls =
        evidenceController.readEvidence(readEvidenceKey);
      final DynamicEvidenceDataDetails foreignResidenceEvidenceData =
        (DynamicEvidenceDataDetails) foreignResidenceEvidenceReadDtls.evidenceObject;

      final Long participantID =
        BDMOASEvidenceUtil.getParticipantIDForEvidence(readEvidenceKey);

      // Read open application and integrated cases for the participant.
      final List<CaseHeaderDtls> caseHeaderList = BDMOASEvidenceUtil
        .readOpenApplicationAndIntegratedCasesByConcernRoleID(participantID);

      for (final CaseHeaderDtls caseHeaderDtls : caseHeaderList) {
        final Long caseID = caseHeaderDtls.caseID;

        // Read OAS Residence Period records for the concern role in
        // open application and integrated cases.
        final List<EvidenceDescriptorDtls> oasResidencePeriodEDList =
          BDMOASEvidenceUtil
            .readActiveEvidenceDescriptorListByCaseIDParticipantIDEvidenceType(
              caseID, participantID, CASEEVIDENCE.OAS_RESIDENCE_PERIOD);

        for (final EvidenceDescriptorDtls evidenceDescriptorDtls : oasResidencePeriodEDList) {

          // Read OAS Residence Period evidence data
          final EIEvidenceKey oasResidenceEvidenceKey = new EIEvidenceKey();
          oasResidenceEvidenceKey.evidenceID =
            evidenceDescriptorDtls.relatedID;
          oasResidenceEvidenceKey.evidenceType =
            evidenceDescriptorDtls.evidenceType;
          final EIEvidenceReadDtls oasResidenceEvidenceReadDtls =
            evidenceController.readEvidence(oasResidenceEvidenceKey);
          final DynamicEvidenceDataDetails oasResidenceEvidenceData =
            (DynamicEvidenceDataDetails) oasResidenceEvidenceReadDtls.evidenceObject;

          if (isForeignResidenceAndOASCanadianResidencePeriodsOverlap(
            foreignResidenceEvidenceData, oasResidenceEvidenceData))
            return true;
        }
      }
    }
    return false;
  }

  /**
   * Checks if foreign residence and OAS canadian residence periods overlap.
   *
   * @param foreignResidenceEvidenceData the foreign residence evidence data
   * @param oasResidenceEvidenceData the oas residence evidence data
   * @return the boolean
   */
  private Boolean isForeignResidenceAndOASCanadianResidencePeriodsOverlap(
    final DynamicEvidenceDataDetails foreignResidenceEvidenceData,
    final DynamicEvidenceDataDetails oasResidenceEvidenceData) {

    final String[] concernedResidenceTypes =
      {OASRESIDENCETYPE.RESIDENCE, OASRESIDENCETYPE.DEEMEDRESIDENCE };

    final String oasResidenceCountry = oasResidenceEvidenceData
      .getAttribute(OASResidencePeriodConstants.COUNTRY).getValue();
    final String oasResidenceType = oasResidenceEvidenceData
      .getAttribute(OASResidencePeriodConstants.RESIDENCE_TYPE).getValue();

    if (COUNTRY.CA.equals(oasResidenceCountry)
      && Arrays.asList(concernedResidenceTypes).contains(oasResidenceType)) {

      final Date foreignResidenceStartDate = Date.fromISO8601(
        foreignResidenceEvidenceData.getAttribute("startDate").getValue());
      final Date foreignResidenceEndDate = Date.fromISO8601(
        foreignResidenceEvidenceData.getAttribute("endDate").getValue());
      final Date canadianResidenceStartDate = Date.fromISO8601(
        oasResidenceEvidenceData.getAttribute("startDate").getValue());
      final String canadianResidenceEndDateString =
        oasResidenceEvidenceData.getAttribute("endDate").getValue();
      final Date canadianResidenceEndDate =
        !StringUtil.isNullOrEmpty(canadianResidenceEndDateString)
          ? Date.fromISO8601(canadianResidenceEndDateString)
          : Date.fromISO8601("99991231");

      return BDMOASEvidenceUtil.isDateRangeOverlap(foreignResidenceStartDate,
        foreignResidenceEndDate, canadianResidenceStartDate,
        canadianResidenceEndDate);
    }

    return false;
  }

}
