package curam.ca.gc.bdmoas.evidence.events.impl;

import curam.ca.gc.bdm.codetable.BDMMARITALSTATUS;
import curam.ca.gc.bdm.codetable.impl.BDMMARITALSTATUSEntry;
import curam.ca.gc.bdm.evidence.events.impl.BDMAbstractEvidenceEventHandler;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdmoas.codetable.BDMOASMARITALCHANGETYPE;
import curam.ca.gc.bdmoas.codetable.BDMOASMARITALRELATION;
import curam.ca.gc.bdmoas.evidence.constants.impl.BDMOASMaritalRelationshipConstants;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.sl.entity.fact.CaseParticipantRoleFactory;
import curam.core.sl.entity.intf.CaseParticipantRole;
import curam.core.sl.entity.struct.CaseParticipantRoleDtls;
import curam.core.sl.entity.struct.CaseParticipantRoleKey;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKeyList;
import curam.core.sl.infrastructure.impl.EIEvidenceModifyDtls;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.impl.EvidenceMap;
import curam.core.sl.infrastructure.impl.StandardEvidenceInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.infrastructure.struct.EIEvidenceKeyList;
import curam.core.sl.struct.EvidenceDescriptorDetails;
import curam.core.sl.struct.EvidenceTypeKey;
import curam.core.struct.CaseKey;
import curam.core.struct.ConcernRoleKey;
import curam.creole.value.CodeTableItem;
import curam.dynamicevidence.impl.DynamicEvidenceDataAttributeDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.dynamicevidence.sl.impl.CpDetailsAdaptor;
import curam.dynamicevidence.sl.impl.EvidenceGenericSLFactory;
import curam.dynamicevidence.sl.impl.EvidenceServiceInterface;
import curam.dynamicevidence.sl.struct.impl.GenericSLDataDetails;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.pdc.fact.PDCUtilFactory;
import curam.pdc.intf.PDCUtil;
import curam.pdc.struct.PDCCaseIDCaseParticipantRoleID;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.StringUtil;
import curam.util.type.Date;

public class BDMOASMaritalRelationshipEventHandler
  extends BDMAbstractEvidenceEventHandler {

  private static final String CASE_PARTICIPANT_ROLE = "caseParticipantRoleID";

  private static final String START_DATE = "startDate";

  private static final String MARITAL_STATUS = "maritalStatus";

  @Override
  public CASEEVIDENCEEntry evidenceType() {

    return CASEEVIDENCEEntry.OAS_MARITAL_RELATIONSHIP;

  }

  @Override
  public boolean subscribePostActivation() {

    return true;

  }

  /**
   * Updates the associated Participant and Related Participant Participant Data
   * Case Marital Status evidence.
   */
  @Override
  public void postActivation(
    final EvidenceControllerInterface evidenceController, final CaseKey key,
    final EIEvidenceKeyList list)
    throws AppException, InformationalException {

    final EIEvidenceKeyList evidenceKeyList =
      this.filterEvidenceKeyList(list);

    for (final EIEvidenceKey evidenceKey : evidenceKeyList.dtls) {

      final EIEvidenceReadDtls evidenceReadDtls =
        evidenceController.readEvidence(evidenceKey);

      // read participants
      final DynamicEvidenceDataDetails evidence =
        (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;

      final String cprIDString = evidence
        .getAttribute(
          BDMOASMaritalRelationshipConstants.CASE_PARTICIPANT_ROLE_ID)
        .getValue();
      final Long cprID = StringUtil.isNullOrEmpty(cprIDString) ? 0L
        : Long.parseLong(cprIDString);

      final String relatedCprIDString = evidence
        .getAttribute(
          BDMOASMaritalRelationshipConstants.RELATED_CASE_PARTICIPANT_ROLE_ID)
        .getValue();
      final Long relatedCprID = StringUtil.isNullOrEmpty(relatedCprIDString)
        ? 0L : Long.parseLong(relatedCprIDString);

      // determine the marital status for the marital status evidence
      final BDMMARITALSTATUSEntry maritalStatus =
        determineMaritalStatus(evidenceReadDtls);

      // determine the start date or effective date of the marital status
      // evidence
      final Date maritalStatusEffectiveDate =
        determineMaritalStatusEffectiveDate(evidenceReadDtls);

      // evidence change reason
      final String evidenceChangeReason =
        evidenceReadDtls.descriptor.changeReason;

      // update or create marital status evidence for primary
      updateOrCreateParticipantMaritalStatusEvidence(evidenceController,
        cprID, maritalStatusEffectiveDate, maritalStatus,
        evidenceChangeReason);

      // update or create marital status evidence for member
      if (relatedCprID != 0L) {
        updateOrCreateParticipantMaritalStatusEvidence(evidenceController,
          relatedCprID, maritalStatusEffectiveDate, maritalStatus,
          evidenceChangeReason);
      }
    }
  }

  private void updateOrCreateParticipantMaritalStatusEvidence(
    final EvidenceControllerInterface evidenceController, final Long cprID,
    final Date maritalStatusEffectiveDate,
    final BDMMARITALSTATUSEntry maritalStatus,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    // Participant Role ID
    final CaseParticipantRole cprObj =
      CaseParticipantRoleFactory.newInstance();
    final CaseParticipantRoleKey cprKey = new CaseParticipantRoleKey();
    cprKey.caseParticipantRoleID = cprID;
    final CaseParticipantRoleDtls cprDtls = cprObj.read(cprKey);

    // PDC case
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    concernRoleKey.concernRoleID = cprDtls.participantRoleID;
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = pdcCaseIDCaseParticipantRoleID.caseID;

    final BDMEvidenceUtil evdUtil = new BDMEvidenceUtil();

    // Read PDC Marital Status Evidence
    final RelatedIDAndEvidenceTypeKeyList maritalStatusEvidenceList =
      evdUtil.getActiveEvidenceIDByEvidenceTypeAndCase(
        CASEEVIDENCE.BDM_MARITAL_STATUS, caseKey);

    if (!maritalStatusEvidenceList.dtls.isEmpty()) {
      // Pick one evidence details from evidence list
      final RelatedIDAndEvidenceTypeKey evidenceDetails =
        maritalStatusEvidenceList.dtls.get(0);

      // update marital status evidence in participant data case
      updateMaritalStatusEvidence(evidenceController,
        maritalStatusEffectiveDate, maritalStatus, evidenceDetails,
        evidenceChangeReason);

    } else {
      // create marital status evidence in participant data case
      createMaritalStatusEvidence(pdcCaseIDCaseParticipantRoleID.caseID,
        pdcCaseIDCaseParticipantRoleID.caseParticipantRoleID,
        cprDtls.participantRoleID, maritalStatusEffectiveDate, maritalStatus,
        evidenceChangeReason);
    }

  }

  private BDMMARITALSTATUSEntry
    determineMaritalStatus(final EIEvidenceReadDtls evidenceReadDtls) {

    final DynamicEvidenceDataDetails evidence =
      (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;

    final String endDateString = evidence
      .getAttribute(BDMOASMaritalRelationshipConstants.END_DATE).getValue();

    final Date endDate = StringUtil.isNullOrEmpty(endDateString)
      ? Date.kZeroDate : Date.fromISO8601(endDateString);

    final Date effectiveFrom = evidenceReadDtls.descriptor.effectiveFrom;

    final String relationshipStatus = evidence
      .getAttribute(BDMOASMaritalRelationshipConstants.RELATIONSHIP_STATUS)
      .getValue();

    final String relationshipChangeType = evidence
      .getAttribute(
        BDMOASMaritalRelationshipConstants.RELATIONSHIP_CHANGE_TYPE)
      .getValue();

    // Default Marital status based on Relationship status
    BDMMARITALSTATUSEntry maritalStatus =
      BDMOASMARITALRELATION.OAS_MARITAL_RELATION_MARRIED
        .equals(relationshipStatus) ? BDMMARITALSTATUSEntry.MARRIED
          : BDMMARITALSTATUSEntry.COMMONLAW;

    // if end-date exists then marital status is determined from relationship
    // change type
    if (!Date.kZeroDate.equals(endDate)) {
      if (BDMOASMARITALCHANGETYPE.OAS_MARITAL_CHANGE_TYPE_ANNULMENT
        .equals(relationshipChangeType))
        maritalStatus = BDMMARITALSTATUSEntry.SINGLE;
      else if (BDMOASMARITALCHANGETYPE.OAS_MARITAL_CHANGE_TYPE_DEATH
        .equals(relationshipChangeType))
        maritalStatus = BDMMARITALSTATUSEntry.WIDOWED;
      else if (BDMOASMARITALCHANGETYPE.OAS_MARITAL_CHANGE_TYPE_DIVORCE
        .equals(relationshipChangeType))
        maritalStatus = BDMMARITALSTATUSEntry.DIVORCED;
      else
        maritalStatus = BDMMARITALSTATUSEntry.SEPARATED;
    } else {
      // if effective dated and change type is separation then marital status is
      // separated
      if (!Date.kZeroDate.equals(effectiveFrom)
        && BDMOASMARITALCHANGETYPE.OAS_MARITAL_CHANGE_TYPE_SEPARATION
          .equals(relationshipChangeType))
        maritalStatus = BDMMARITALSTATUSEntry.SEPARATED;
    }

    return maritalStatus;
  }

  private Date determineMaritalStatusEffectiveDate(
    final EIEvidenceReadDtls evidenceReadDtls) {

    final DynamicEvidenceDataDetails evidence =
      (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;

    final String startDateString = evidence
      .getAttribute(BDMOASMaritalRelationshipConstants.START_DATE).getValue();

    final Date startDate = StringUtil.isNullOrEmpty(startDateString)
      ? Date.kZeroDate : Date.fromISO8601(startDateString);

    final String endDateString = evidence
      .getAttribute(BDMOASMaritalRelationshipConstants.END_DATE).getValue();

    final Date endDate = StringUtil.isNullOrEmpty(endDateString)
      ? Date.kZeroDate : Date.fromISO8601(endDateString);

    final Date effectiveFrom = evidenceReadDtls.descriptor.effectiveFrom;

    Date effectiveDate = startDate;
    if (!Date.kZeroDate.equals(endDate)) {
      effectiveDate = endDate;
    } else if (!Date.kZeroDate.equals(effectiveFrom)) {
      effectiveDate = effectiveFrom;
    }

    return effectiveDate;
  }

  private void updateMaritalStatusEvidence(
    final EvidenceControllerInterface evidenceController,
    final Date maritalStatusEffectiveDate,
    final BDMMARITALSTATUSEntry maritalStatus,
    final RelatedIDAndEvidenceTypeKey evidenceDetails,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    // Read DynamicEvidenceDataDetails
    final EvidenceMap map =
      curam.core.sl.infrastructure.impl.EvidenceController.getEvidenceMap();
    final StandardEvidenceInterface standardEvidenceInterface =
      map.getEvidenceType(CASEEVIDENCE.BDM_MARITAL_STATUS);

    final EIEvidenceKey evidenceKey = new EIEvidenceKey();
    evidenceKey.evidenceID = evidenceDetails.relatedID;
    evidenceKey.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;

    final DynamicEvidenceDataDetails evidenceData =
      (DynamicEvidenceDataDetails) standardEvidenceInterface
        .readEvidence(evidenceKey);

    // update the Marital Status
    final DynamicEvidenceDataAttributeDetails attributeObj =
      evidenceData.getAttribute(MARITAL_STATUS);

    DynamicEvidenceTypeConverter.setAttribute(attributeObj,
      new CodeTableItem(BDMMARITALSTATUS.TABLENAME, maritalStatus.getCode()));

    // evidence descriptor details for this evidence
    final RelatedIDAndEvidenceTypeKey relatedIDAndTypeKey =
      new RelatedIDAndEvidenceTypeKey();
    relatedIDAndTypeKey.relatedID = evidenceDetails.relatedID;
    relatedIDAndTypeKey.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;

    final EvidenceDescriptorDtls evidenceDescriptorDtls =
      EvidenceDescriptorFactory.newInstance()
        .readByRelatedIDAndType(relatedIDAndTypeKey);

    // modify evidence details
    final EIEvidenceModifyDtls modifyEvidenceDetails =
      new EIEvidenceModifyDtls();

    modifyEvidenceDetails.evidenceObject = evidenceData;
    modifyEvidenceDetails.descriptor.assign(evidenceDescriptorDtls);
    modifyEvidenceDetails.descriptor.versionNo =
      evidenceDescriptorDtls.versionNo;
    modifyEvidenceDetails.descriptor.changeReason = evidenceChangeReason;
    modifyEvidenceDetails.descriptor.effectiveFrom =
      maritalStatusEffectiveDate;

    evidenceController.modifyEvidence(evidenceKey, modifyEvidenceDetails);
  }

  private curam.core.sl.struct.EvidenceKey createMaritalStatusEvidence(
    final Long caseID, final Long cprID, final long concernroleID,
    final Date startDate, final BDMMARITALSTATUSEntry maritalStatus,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;

    // get Latest Version of Evidence
    final GenericSLDataDetails genericDtls = new GenericSLDataDetails();
    final EvidenceServiceInterface evidenceServiceInterface =
      EvidenceGenericSLFactory.instance(eType, Date.getCurrentDate());

    final DynamicEvidenceDataDetails dynamicEvidencedataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(eType.evidenceType,
        Date.getCurrentDate());
    // assigning all the required attributes
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(CASE_PARTICIPANT_ROLE), cprID);
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(START_DATE), startDate);
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(MARITAL_STATUS),
      new CodeTableItem(BDMMARITALSTATUS.TABLENAME, maritalStatus.getCode()));

    // set descriptor attributes to call OOTB Evidence creation logic
    final EvidenceDescriptorDetails descriptor =
      new EvidenceDescriptorDetails();
    descriptor.evidenceType = eType.evidenceType;
    descriptor.caseID = caseID;
    descriptor.receivedDate = Date.getCurrentDate();
    descriptor.participantID = concernroleID;
    descriptor.changeReason = evidenceChangeReason;

    genericDtls.setData(dynamicEvidencedataDetails);
    genericDtls.setDescriptor(descriptor);
    genericDtls.setCaseIdKey(descriptor.caseID);

    final CpDetailsAdaptor cpAdaptor = new CpDetailsAdaptor();
    cpAdaptor.setCaseParticipantRoleID(cprID);
    genericDtls.addRelCp(CASE_PARTICIPANT_ROLE, cpAdaptor);

    return evidenceServiceInterface.createEvidence(genericDtls).evidenceKey;
  }

}
