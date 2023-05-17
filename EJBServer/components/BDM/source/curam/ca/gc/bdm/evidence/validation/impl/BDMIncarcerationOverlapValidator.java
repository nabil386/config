package curam.ca.gc.bdm.evidence.validation.impl;

import curam.codetable.CASEEVIDENCE;
import curam.core.impl.CuramConst;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.ApplyChangesEvidenceLists;
import curam.core.struct.CaseKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.util.type.Date;

public class BDMIncarcerationOverlapValidator
  extends BDMActivationOverlapValidator {

  public BDMIncarcerationOverlapValidator(
    final EvidenceControllerInterface evidenceController,
    final CaseKey caseKey, final ApplyChangesEvidenceLists evidenceLists) {

    super(evidenceController, CASEEVIDENCE.BDMINCARCERATION, caseKey,
      evidenceLists);

  }

  @Override
  public Date getEndDate(final DynamicEvidenceDataDetails evidence) {

    final String endDateString =
      evidence.getAttribute(CuramConst.endDateAttribute).getValue();

    final Date endDate = endDateString.isEmpty() ? Date.kZeroDate
      : Date.fromISO8601(endDateString);

    return endDate;

  }

}
