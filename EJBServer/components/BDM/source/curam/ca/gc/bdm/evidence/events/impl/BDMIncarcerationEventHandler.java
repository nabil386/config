package curam.ca.gc.bdm.evidence.events.impl;

import curam.ca.gc.bdm.codetable.BDMOFFENDERSTATUS;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.evidence.validation.impl.BDMIncarcerationOverlapValidator;
import curam.ca.gc.bdm.message.impl.BDMEVIDENCEExceptionCreator;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKeyList;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.ApplyChangesEvidenceLists;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.infrastructure.struct.EvidenceKey;
import curam.core.struct.CaseKey;
import curam.core.struct.ConcernRoleKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.pdc.fact.PDCUtilFactory;
import curam.pdc.impl.PDCConst;
import curam.pdc.intf.PDCUtil;
import curam.pdc.struct.PDCCaseIDCaseParticipantRoleID;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.StringUtil;

public class BDMIncarcerationEventHandler
  extends BDMAbstractEvidenceEventHandler {

  private static final String EVD_ATTRIBUTE_OFFERNDER_STATUS =
    "offenderStatus";

  private static final String EVD_ATTRIBUTE_DATE_OF_DEATH = "dateOfDeath";

  @Override
  public CASEEVIDENCEEntry evidenceType() {

    return CASEEVIDENCEEntry.BDMINCARCERATION;
  }

  @Override
  public boolean subscribePreActivation() {

    return true;
  }

  @Override
  public void preActivation(
    final EvidenceControllerInterface evidenceController,
    final CaseKey caseKey, final ApplyChangesEvidenceLists evidenceLists)
    throws AppException, InformationalException {

    final ApplyChangesEvidenceLists filteredEvidenceLists =
      this.filterEvidenceLists(evidenceLists);

    if (filteredEvidenceLists.newAndUpdateList.dtls.size() > 0) {

      final BDMIncarcerationOverlapValidator validator =
        new BDMIncarcerationOverlapValidator(evidenceController, caseKey,
          filteredEvidenceLists);

      if (!validator.isValid()) {
        throw BDMEVIDENCEExceptionCreator
          .ERR_INCARCERATION_EVIDENCE_OVERLAP();
      }
    }

    for (final EvidenceKey evidenceKey : filteredEvidenceLists.newAndUpdateList.dtls
      .items()) {

      final EIEvidenceKey readEvidenceKey = new EIEvidenceKey();
      readEvidenceKey.evidenceID = evidenceKey.evidenceID;
      readEvidenceKey.evidenceType = evidenceKey.evidenceType;

      final EIEvidenceReadDtls evidenceReadDtls =
        evidenceController.readEvidence(readEvidenceKey);

      final DynamicEvidenceDataDetails evidence =
        (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;

      if (evidence.getAttribute(EVD_ATTRIBUTE_OFFERNDER_STATUS).getValue()
        .equals(BDMOFFENDERSTATUS.DECEASED)) {

        validateOffenderStatus(evidenceController, caseKey,
          filteredEvidenceLists);
      }
    }
  }

  /**
   * Throws the error message if the offender status is set as 'Deceased'
   * and date of death is not recorded during the evidence activation stage.
   *
   * @param evidenceController
   * @param caseKey
   * @param filteredEvidenceLists
   * @throws AppException
   * @throws InformationalException
   */
  private void validateOffenderStatus(
    final EvidenceControllerInterface evidenceController,
    final CaseKey caseKey,
    final ApplyChangesEvidenceLists filteredEvidenceLists)
    throws AppException, InformationalException {

    final EvidenceDescriptor descriptor =
      EvidenceDescriptorFactory.newInstance();
    final EvidenceDescriptorKey evidenceDescriptorKey =
      new EvidenceDescriptorKey();

    for (final EvidenceKey evidenceKey : filteredEvidenceLists.newAndUpdateList.dtls) {
      evidenceDescriptorKey.evidenceDescriptorID =
        evidenceKey.evidenceDescriptorID;
      final EvidenceDescriptorDtls evidenceDescriptorDtls =
        descriptor.read(evidenceDescriptorKey);

      if (isDateOfDeathEmpty(evidenceController,
        evidenceDescriptorDtls.participantID)) {
        throw BDMEVIDENCEExceptionCreator
          .ERR_DATE_OF_DEATH_REQUIRED_FOR_OFFENDER_STATUS_DECEASED();
      }
    }
  }

  /**
   * Check the offender status value in the incarceration evidence
   * if the offender status is 'Deceased', and the date of death
   * value for the participant is empty then return <code>true</code>
   * else <code>false</code>
   *
   * @param evidenceController
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private boolean isDateOfDeathEmpty(
    final EvidenceControllerInterface evidenceController,
    final Long concernRoleID) throws AppException, InformationalException {

    // Read participant data case details
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    concernRoleKey.concernRoleID = concernRoleID;
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = pdcCaseIDCaseParticipantRoleID.caseID;

    // Read PDC Birth and Death details evidences
    final BDMEvidenceUtil evdUtil = new BDMEvidenceUtil();
    final RelatedIDAndEvidenceTypeKeyList pdcBirthAndDeathEvidenceList =
      evdUtil.getActiveEvidenceIDByEvidenceTypeAndCase(
        PDCConst.PDCBIRTHANDDEATH, caseKey);

    // Pick one evidence details from evidence list
    final RelatedIDAndEvidenceTypeKey evidenceDetails =
      pdcBirthAndDeathEvidenceList.dtls.get(0);

    // read evidence data
    final EIEvidenceKey readEvidenceKey = new EIEvidenceKey();
    readEvidenceKey.evidenceID = evidenceDetails.relatedID;
    readEvidenceKey.evidenceType = PDCConst.PDCBIRTHANDDEATH;

    final EIEvidenceReadDtls evidenceReadDtls =
      evidenceController.readEvidence(readEvidenceKey);

    final DynamicEvidenceDataDetails evidence =
      (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;

    if (StringUtil.isNullOrEmpty(
      evidence.getAttribute(EVD_ATTRIBUTE_DATE_OF_DEATH).getValue())) {

      return true;
    }

    return false;
  }

}
