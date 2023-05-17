package curam.ca.gc.bdmoas.evidence.facade.impl;

import curam.ca.gc.bdm.facade.evidence.struct.BDMCaseKey;
import curam.ca.gc.bdm.facade.evidence.struct.BDMEvdInstanceChangeDtlsList;
import curam.ca.gc.bdm.facade.evidence.struct.BDMListAllEvidenceDtls;
import curam.ca.gc.bdm.facade.evidence.struct.BDMSuccessionID;
import curam.ca.gc.bdm.facade.evidence.struct.EvdInstanceChangeDtls;
import curam.ca.gc.bdm.facade.evidence.struct.EvidenceParticipantDtls;
import curam.codetable.CASEEVIDENCE;
import curam.core.facade.infrastructure.fact.EvidenceFactory;
import curam.core.facade.infrastructure.struct.EvdInstanceChangeDtlsList;
import curam.core.sl.infrastructure.entity.struct.SuccessionID;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

/**
 * The Class BDMOASEvidence.
 * To override or extend the Facade API related to evidence.
 */
public class BDMOASEvidence
  extends curam.ca.gc.bdmoas.evidence.facade.base.BDMOASEvidence {

  /**
   * Override BDMEvidence listAllEvidence method to disable delete and modify
   * option on the
   * CRA Evidence list page.
   */
  @Override
  public BDMListAllEvidenceDtls listAllEvidence(final BDMCaseKey key)
    throws AppException, InformationalException {

    final BDMListAllEvidenceDtls bdmListAllEvidenceDtls =
      super.listAllEvidence(key);

    for (final EvidenceParticipantDtls evidenceParticipantDtls : bdmListAllEvidenceDtls.evidenceParticipantDtlsList.dtls) {
      if (evidenceParticipantDtls.evidenceType
        .equals(CASEEVIDENCE.OAS_CRA_INCOME)
        || evidenceParticipantDtls.evidenceType
          .equals(CASEEVIDENCE.OAS_CRA_BIRTH_DEATH)) {
        // disable delete and modify
        evidenceParticipantDtls.activeWithSingleInstanceInd = false;
        evidenceParticipantDtls.readOnlyInd = true;
      } else if (evidenceParticipantDtls.evidenceType
        .equals(CASEEVIDENCE.OAS_RI_NON_FILER)) {
        // disable delete
        evidenceParticipantDtls.activeWithSingleInstanceInd = false;
      }
    }

    return bdmListAllEvidenceDtls;
  }

  /**
   * Override BDMEvidence listEvdInstanceChanges method to disable delete and
   * modify on the
   * application case evidence list page for CRA Evidences.
   */
  @Override
  public BDMEvdInstanceChangeDtlsList listEvdInstanceChanges(
    final BDMSuccessionID key) throws AppException, InformationalException {

    final BDMEvdInstanceChangeDtlsList bdmEvdInstanceChangeDtlsList =
      super.listEvdInstanceChanges(key);

    for (final EvdInstanceChangeDtls evdInstanceChangeDtls : bdmEvdInstanceChangeDtlsList.dtlsList) {

      if (evdInstanceChangeDtls.evidenceType
        .equals(CASEEVIDENCE.OAS_CRA_INCOME)
        || evdInstanceChangeDtls.evidenceType
          .equals(CASEEVIDENCE.OAS_CRA_BIRTH_DEATH)) {
        // disable delete and modify
        evdInstanceChangeDtls.activeInd = false;
        evdInstanceChangeDtls.readOnlyInd = true;
      } else if (evdInstanceChangeDtls.evidenceType
        .equals(CASEEVIDENCE.OAS_RI_NON_FILER)) {
        // disable delete
        evdInstanceChangeDtls.activeInd = false;
      }
    }

    return bdmEvdInstanceChangeDtlsList;
  }

  /**
   * Modify OOTB Evidence return struct to disable delete and modify on the
   * active evidence
   * list page for CRA Evidences.
   */
  @Override
  public EvdInstanceChangeDtlsList listActiveEvdInstanceChanges(
    final SuccessionID key) throws AppException, InformationalException {

    final EvdInstanceChangeDtlsList instanceChangeDtlsList =
      EvidenceFactory.newInstance().listActiveEvdInstanceChanges(key);

    for (final curam.core.facade.infrastructure.struct.EvdInstanceChangeDtls instanceChangeDtls : instanceChangeDtlsList.dtlsList) {
      if (instanceChangeDtls.evidenceType.equals(CASEEVIDENCE.OAS_CRA_INCOME)
        || instanceChangeDtls.evidenceType
          .equals(CASEEVIDENCE.OAS_CRA_BIRTH_DEATH)) {
        // disable delete and modify
        instanceChangeDtls.activeInd = false;
        instanceChangeDtls.readOnlyInd = true;
      } else if (instanceChangeDtls.evidenceType
        .equals(CASEEVIDENCE.OAS_RI_NON_FILER)) {
        // disable delete
        instanceChangeDtls.activeInd = false;
      }
    }

    return instanceChangeDtlsList;
  }

}
