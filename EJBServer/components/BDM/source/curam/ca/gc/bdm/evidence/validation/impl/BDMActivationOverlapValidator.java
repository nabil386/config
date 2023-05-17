package curam.ca.gc.bdm.evidence.validation.impl;

import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtlsList;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.core.sl.infrastructure.entity.struct.EvidenceTypeCaseIDStatusesKey;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.ApplyChangesEvidenceLists;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.infrastructure.struct.EvidenceKey;
import curam.core.struct.CaseKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import curam.util.type.DateRange;
import java.util.HashMap;
import java.util.Map;

/**
 * A common validator for overlapping evidences at apply changes.
 */
public class BDMActivationOverlapValidator
  extends BDMAbstractEvidenceValidator {

  private final EvidenceControllerInterface controller;

  private final String evidenceType;

  private final CaseKey caseKey;

  private final ApplyChangesEvidenceLists evidenceLists;

  final Map<Long, Map<Long, DateRange>> ranges = new HashMap<>();

  private static final String START_DATE = "startDate";

  private static final String END_DATE = "endDate";

  public BDMActivationOverlapValidator(
    final EvidenceControllerInterface evidenceController,
    final String evidenceType, final CaseKey caseKey,
    final ApplyChangesEvidenceLists evidenceLists) {

    this.controller = evidenceController;
    this.evidenceType = evidenceType;
    this.caseKey = caseKey;
    this.evidenceLists = evidenceLists;

  }

  @Override
  public boolean isValid() throws AppException, InformationalException {

    final EIEvidenceKey readEvidenceKey = new EIEvidenceKey();
    final EvidenceDescriptor descriptor =
      EvidenceDescriptorFactory.newInstance();
    final EvidenceDescriptorKey evidenceDescriptorKey =
      new EvidenceDescriptorKey();

    if (this.evidenceLists.newAndUpdateList.dtls.size() > 0) {

      for (final EvidenceKey evidenceKey : this.evidenceLists.newAndUpdateList.dtls) {

        readEvidenceKey.evidenceID = evidenceKey.evidenceID;
        readEvidenceKey.evidenceType = this.evidenceType;

        final EIEvidenceReadDtls evidenceReadDtls =
          this.controller.readEvidence(readEvidenceKey);

        final DynamicEvidenceDataDetails evidence =
          (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;

        final Date startDate = this.getStartDate(evidence);
        final Date endDate = this.getEndDate(evidence);

        evidenceDescriptorKey.evidenceDescriptorID =
          evidenceReadDtls.descriptor.evidenceDescriptorID;
        final EvidenceDescriptorDtls evidenceDescriptorDtls =
          descriptor.read(evidenceDescriptorKey);

        final Map<Long, DateRange> participantRanges =
          this.getParticipantRangeMap(evidenceDescriptorDtls.participantID);

        participantRanges.put(evidenceReadDtls.descriptor.successionID,
          new DateRange(startDate, endDate));

      }

    }

    final EvidenceTypeCaseIDStatusesKey activeKey =
      new EvidenceTypeCaseIDStatusesKey();
    activeKey.caseID = this.caseKey.caseID;
    activeKey.evidenceType = this.evidenceType;
    activeKey.statusCode1 = EVIDENCEDESCRIPTORSTATUS.ACTIVE;

    final EvidenceDescriptorDtlsList descriptorDtlsList =
      descriptor.searchByCaseIDEvidenceTypeAndStatus(activeKey);

    for (final EvidenceDescriptorDtls dtls : descriptorDtlsList.dtls) {

      readEvidenceKey.evidenceID = dtls.relatedID;
      readEvidenceKey.evidenceType = this.evidenceType;

      final EIEvidenceReadDtls evidenceReadDtls =
        controller.readEvidence(readEvidenceKey);

      final DynamicEvidenceDataDetails evidence =
        (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;

      final Map<Long, DateRange> participantRanges =
        this.getParticipantRangeMap(dtls.participantID);

      if (!participantRanges.containsKey(dtls.successionID)) {

        final Date startDate = this.getStartDate(evidence);
        final Date endDate = this.getEndDate(evidence);
        participantRanges.put(evidenceReadDtls.descriptor.successionID,
          new DateRange(startDate, endDate));

      }

    }

    for (final EvidenceKey evidenceKey : evidenceLists.removeList.dtls) {

      for (final Map<Long, DateRange> participantRanges : this.ranges
        .values()) {

        if (participantRanges.containsKey(evidenceKey.successionID)) {
          participantRanges.remove(evidenceKey.successionID);
        }

      }

    }

    for (final Map<Long, DateRange> participantRanges : this.ranges
      .values()) {

      for (final Map.Entry<Long, DateRange> left : participantRanges
        .entrySet()) {

        for (final Map.Entry<Long, DateRange> right : participantRanges
          .entrySet()) {
          if (!left.getKey().equals(right.getKey())
            && left.getValue().overlapsWith(right.getValue())) {
            return false;
          }
        }

      }

    }

    return true;

  }

  public String startDateName() {

    return START_DATE;

  }

  public String endDateName() {

    return END_DATE;

  }

  public Date getStartDate(final DynamicEvidenceDataDetails evidence) {

    final String startDateString =
      evidence.getAttribute(startDateName()).getValue();

    final Date startDate = startDateString.isEmpty() ? Date.kZeroDate
      : Date.fromISO8601(startDateString);

    return startDate;

  }

  public Date getEndDate(final DynamicEvidenceDataDetails evidence) {

    final String endDateString =
      evidence.getAttribute(endDateName()).getValue();

    final Date endDate = endDateString.isEmpty() ? Date.kZeroDate
      : Date.fromISO8601(endDateString);

    return endDate;

  }

  private Map<Long, DateRange>
    getParticipantRangeMap(final long concernRoleID) {

    if (!this.ranges.containsKey(concernRoleID)) {
      this.ranges.put(concernRoleID, new HashMap<Long, DateRange>());
    }

    return this.ranges.get(concernRoleID);

  }

}
