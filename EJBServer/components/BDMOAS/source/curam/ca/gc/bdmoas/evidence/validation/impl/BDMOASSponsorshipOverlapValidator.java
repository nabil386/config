package curam.ca.gc.bdmoas.evidence.validation.impl;

import curam.ca.gc.bdm.evidence.validation.impl.BDMActivationOverlapValidator;
import curam.ca.gc.bdmoas.evidence.constants.impl.OASSponsorshipConstants;
import curam.codetable.CASEEVIDENCE;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.ApplyChangesEvidenceLists;
import curam.core.struct.CaseKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.util.type.Date;

public class BDMOASSponsorshipOverlapValidator
  extends BDMActivationOverlapValidator {

  public BDMOASSponsorshipOverlapValidator(
    final EvidenceControllerInterface evidenceController,
    final CaseKey caseKey, final ApplyChangesEvidenceLists evidenceLists) {

    super(evidenceController, CASEEVIDENCE.OAS_SPONSORSHIP_AGREEMENT, caseKey,
      evidenceLists);

  }

  @Override
  public Date getEndDate(final DynamicEvidenceDataDetails evidence) {

    String endDateString =
      evidence.getAttribute(OASSponsorshipConstants.END_DATE).getValue();

    final String breakdownDateString = evidence
      .getAttribute(OASSponsorshipConstants.BREAKDOWN_DATE).getValue();

    if (!breakdownDateString.isEmpty()) {
      endDateString = breakdownDateString;
    }

    final Date endDate = endDateString.isEmpty() ? Date.kZeroDate
      : Date.fromISO8601(endDateString);

    return endDate;

  }

}
