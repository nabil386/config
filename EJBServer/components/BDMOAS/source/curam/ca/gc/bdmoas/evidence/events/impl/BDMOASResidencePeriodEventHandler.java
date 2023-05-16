package curam.ca.gc.bdmoas.evidence.events.impl;

import curam.ca.gc.bdm.evidence.events.impl.BDMAbstractEvidenceEventHandler;
import curam.ca.gc.bdm.evidence.validation.impl.BDMActivationOverlapValidator;
import curam.ca.gc.bdmoas.evidence.constants.impl.OASResidencePeriodConstants;
import curam.ca.gc.bdmoas.message.impl.BDMOASEVIDENCEMESSAGEExceptionCreator;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.ApplyChangesEvidenceLists;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.infrastructure.struct.EvidenceKey;
import curam.core.struct.CaseKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

// 64790: Residence Period
/**
 * An evidence event class for listening and handling OASResidencePeriod
 * evidence
 * events.
 */
public class BDMOASResidencePeriodEventHandler
  extends BDMAbstractEvidenceEventHandler {

  @Override
  public CASEEVIDENCEEntry evidenceType() {

    return CASEEVIDENCEEntry.OAS_RESIDENCE_PERIOD;

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

    if (filteredEvidenceLists.newAndUpdateList.dtls.size() > 0) {

      final BDMActivationOverlapValidator validator =
        new BDMActivationOverlapValidator(evidenceController,
          this.evidenceType().getCode(), caseKey, filteredEvidenceLists);

      if (!validator.isValid()) {
        throw BDMOASEVIDENCEMESSAGEExceptionCreator
          .ERR_OVERLAPPING_RESIDENCE_PERIOD();
      }

    }

    if (isResidenceTypeMissing(evidenceController, caseKey,
      filteredEvidenceLists)) {
      throw BDMOASEVIDENCEMESSAGEExceptionCreator
        .ERR_RESIDENCE_TYPE_REQUIRED();
    }

  }

  /**
   * Checks if Residence Type has been entered.
   *
   * @param evidenceController
   * @param caseKey
   * @param filteredEvidenceLists
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private boolean isResidenceTypeMissing(
    final EvidenceControllerInterface evidenceController,
    final CaseKey caseKey,
    final ApplyChangesEvidenceLists filteredEvidenceLists)
    throws AppException, InformationalException {

    boolean isResidenceTypeMissing = false;

    final EIEvidenceKey readEvidenceKey = new EIEvidenceKey();

    for (final EvidenceKey evidenceKey : filteredEvidenceLists.newAndUpdateList.dtls) {

      readEvidenceKey.evidenceID = evidenceKey.evidenceID;
      readEvidenceKey.evidenceType = CASEEVIDENCE.OAS_RESIDENCE_PERIOD;

      final EIEvidenceReadDtls evidenceReadDtls =
        evidenceController.readEvidence(readEvidenceKey);

      final DynamicEvidenceDataDetails evidence =
        (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;

      isResidenceTypeMissing =
        evidence.getAttribute(OASResidencePeriodConstants.RESIDENCE_TYPE)
          .getValue().isEmpty();

      if (isResidenceTypeMissing) {
        break;
      }

    }

    return isResidenceTypeMissing;

  }

}
