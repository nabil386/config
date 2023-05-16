package curam.ca.gc.bdmoas.evidence.events.impl;

import curam.ca.gc.bdm.evidence.events.impl.BDMAbstractEvidenceEventHandler;
import curam.ca.gc.bdmoas.evidence.validation.impl.BDMOASSponsorshipOverlapValidator;
import curam.ca.gc.bdmoas.message.impl.BDMOASEVIDENCEMESSAGEExceptionCreator;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.ApplyChangesEvidenceLists;
import curam.core.struct.CaseKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

/**
 * An evidence event class for listening and handling OASLegalStatus evidence
 * evidence.
 */
public class BDMOASSponsorshipEventHandler
  extends BDMAbstractEvidenceEventHandler {

  @Override
  public CASEEVIDENCEEntry evidenceType() {

    return CASEEVIDENCEEntry.OAS_SPONSORSHIP_AGREEMENT;

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

      final BDMOASSponsorshipOverlapValidator validator =
        new BDMOASSponsorshipOverlapValidator(evidenceController, caseKey,
          filteredEvidenceLists);

      if (!validator.isValid()) {
        throw BDMOASEVIDENCEMESSAGEExceptionCreator
          .ERR_OVERLAPPING_SPONSORSHIP();
      }

    }

  }

}
