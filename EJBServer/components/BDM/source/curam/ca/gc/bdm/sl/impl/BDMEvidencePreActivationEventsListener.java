package curam.ca.gc.bdm.sl.impl;

import com.google.inject.Inject;
import curam.advisor.entity.fact.AdviceCaseIssueFactory;
import curam.advisor.entity.intf.AdviceCaseIssue;
import curam.advisor.entity.struct.AdviceCaseIssueDtlsList;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMEVIDENCE;
import curam.ca.gc.bdm.message.impl.BDMEVIDENCEExceptionCreator;
import curam.ca.gc.bdm.vtw.deduction.entity.fact.BDMVTWDeductionFactory;
import curam.ca.gc.bdm.vtw.deduction.entity.struct.BDMVTWDeductionCountDetails;
import curam.ca.gc.bdm.vtw.deduction.entity.struct.BDMVTWDeductionCountKey;
import curam.ca.gc.bdm.vtw.deduction.entity.struct.BDMVTWDeductionDtls;
import curam.ca.gc.bdm.vtw.deduction.entity.struct.BDMVTWDeductionKey;
import curam.ca.gc.bdm.vtw.deduction.entity.struct.BDMVTWDeductionKeyStruct1;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASETYPECODE;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.codetable.RECORDSTATUS;
import curam.core.facade.infrastructure.struct.CaseIDAndEvidenceTypeKey;
import curam.core.fact.CaseHeaderFactory;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor;
import curam.core.sl.infrastructure.entity.struct.CaseIDStatusAndEvidenceTypeKey;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKeyList;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface.EvidencePreActivationEvents;
import curam.core.sl.infrastructure.impl.ValidationManagerFactory;
import curam.core.sl.infrastructure.struct.ApplyChangesEvidenceLists;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.infrastructure.struct.EvidenceKey;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement.InformationalType;
import curam.util.exception.InformationalException;
import curam.util.exception.RecordNotFoundException;
import curam.util.persistence.GuiceWrapper;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;

public class BDMEvidencePreActivationEventsListener
  implements EvidencePreActivationEvents {

  @Inject
  curam.ca.gc.bdm.sl.maintaincasedeductions.intf.MaintainCaseDeductions caseDeductions;

  public BDMEvidencePreActivationEventsListener() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public void preActivation(final EvidenceControllerInterface arg0,
    final CaseKey caseKey, final ApplyChangesEvidenceLists evList)
    throws AppException, InformationalException {

    for (final EvidenceKey key : evList.removeList.dtls) {
      operateOnVTWEvidence(caseKey.caseID, key.evidenceType, key.evidenceID);
    }
    // have to check that modifications do not work either
    for (final EvidenceKey key : evList.newAndUpdateList.dtls) {
      validateModifyVTWEvidence(caseKey.caseID, key.evidenceType,
        key.evidenceID, key.correctionSetID);

      // BEGIN 90018 DEV: Evidence- creditable residency period in Canada
      if (key.evidenceType
        .equals(CASEEVIDENCE.BDM_CANADIAN_RESIDENCE_PERIOD)) {
        validateModifyCanadianResidencePeriodEvidence(caseKey.caseID,
          key.evidenceType, key.evidenceID, key.correctionSetID);
      }
      // END 90018 DEV: Evidence- creditable residency period in Canada
    }
  }

  // 90018 DEV: Evidence- creditable residency period in Canada
  private void validateModifyCanadianResidencePeriodEvidence(
    final long caseID, final String evidenceType, final long evidenceID,
    final String correctionSetID)
    throws AppException, InformationalException {

    final AdviceCaseIssue adviceCaseIssueObj =
      AdviceCaseIssueFactory.newInstance();

    final CaseIDAndEvidenceTypeKey adviceCaseIssueKey =
      new CaseIDAndEvidenceTypeKey();
    adviceCaseIssueKey.caseID = caseID;
    adviceCaseIssueKey.evidenceType = evidenceType;
    final AdviceCaseIssueDtlsList adviceCaseIssueDtls =
      adviceCaseIssueObj.searchByCaseIDAndEvidenceType(adviceCaseIssueKey);

    final Boolean issueExistsForEvidenceID = adviceCaseIssueDtls.dtls.stream()
      .anyMatch(x -> x.evidenceID == evidenceID);

    if (issueExistsForEvidenceID) {
      throw BDMEVIDENCEExceptionCreator.ERR_RESOLVE_ASSOCIATED_ISSUE();
    }

  }

  /**
   * Validates that start date is not changed for a vtw evidence once a
   * deduction has been processed
   *
   * @param caseID
   * @param evidenceType
   * @param evidenceID
   */
  private void validateModifyVTWEvidence(final long caseID,
    final String evidenceType, final long evidenceID,
    final String correctionSetID)
    throws AppException, InformationalException {

    if (evidenceType.equals(CASEEVIDENCE.BDMVTW) && isICCase(caseID)) {

      // get the current active VTW evidence
      final CaseIDStatusAndEvidenceTypeKey caseIDStatusAndEvidenceTypeKey =
        new CaseIDStatusAndEvidenceTypeKey();

      caseIDStatusAndEvidenceTypeKey.caseID = caseID;
      caseIDStatusAndEvidenceTypeKey.evidenceType = evidenceType;
      caseIDStatusAndEvidenceTypeKey.statusCode =
        EVIDENCEDESCRIPTORSTATUS.ACTIVE;

      final EvidenceDescriptor evidenceDescriptorObj =
        EvidenceDescriptorFactory.newInstance();

      final RelatedIDAndEvidenceTypeKeyList currentActiveEvidenceList =
        evidenceDescriptorObj
          .searchByCaseIDTypeAndStatus(caseIDStatusAndEvidenceTypeKey);

      // if this is an add activation, there will be no current active
      // evidence that matches the correction set ID

      final EvidenceControllerInterface evidenceController =
        (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

      EIEvidenceReadDtls currentEvidence = null;
      long currentEvidenceID = 0;

      final EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
      // iterate through all active evidences
      for (final RelatedIDAndEvidenceTypeKey activeEvidence : currentActiveEvidenceList.dtls) {
        // get the current evidence details
        eiEvidenceKey.evidenceID = activeEvidence.relatedID;

        eiEvidenceKey.evidenceType = evidenceType;
        final EIEvidenceReadDtls activeEvidenceDtls =
          evidenceController.readEvidence(eiEvidenceKey);

        // check correction set ID matches
        if (activeEvidenceDtls.descriptor.correctionSetID
          .equals(correctionSetID)) {
          currentEvidence = activeEvidenceDtls;
          currentEvidenceID = activeEvidence.relatedID;
          break;
        }
      }

      if (currentEvidence != null) {

        final DynamicEvidenceDataDetails currentDtls =
          (DynamicEvidenceDataDetails) currentEvidence.evidenceObject;
        final String currentStartDate = currentDtls
          .getAttribute(BDMConstants.kEvidenceAttrVTWStartDate).getValue();

        // count ILIs related to the original active evidence
        final BDMVTWDeductionCountKey countKey =
          new BDMVTWDeductionCountKey();
        countKey.evidenceID = currentEvidenceID;
        final BDMVTWDeductionCountDetails countDetails =
          BDMVTWDeductionFactory.newInstance()
            .countVTWILIByEvidenceID(countKey);

        if (countDetails.numberOfRecords > 0) {

          // get the new updated evidence details
          eiEvidenceKey.evidenceID = evidenceID;
          eiEvidenceKey.evidenceType = evidenceType;
          final EIEvidenceReadDtls updatedEvidence =
            evidenceController.readEvidence(eiEvidenceKey);
          final DynamicEvidenceDataDetails updatedDtls =
            (DynamicEvidenceDataDetails) updatedEvidence.evidenceObject;
          final String updatedStartDate = updatedDtls
            .getAttribute(BDMConstants.kEvidenceAttrVTWStartDate).getValue();

          // if the start date has been changed, fail activation
          if (!Date.getDate(currentStartDate)
            .equals(Date.getDate(updatedStartDate))) {

            ValidationManagerFactory.getManager()
              .addInfoMgrExceptionWithLookup(
                new AppException(BDMEVIDENCE.ERR_CANNOT_MODIFY_VTW_EVIDENCE),
                null, InformationalType.kError,
                curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine,
                0);
            TransactionInfo.getInformationalManager().failOperation();
          }
        }

      }
    }

  }

  /**
   * This method operates on VTW evidence deduction record.
   *
   * @param caseID
   * @param evidenceType
   * @param evidenceID
   * @throws AppException
   * @throws InformationalException
   */
  private void operateOnVTWEvidence(final long caseID,
    final String evidenceType, final long evidenceID)
    throws AppException, InformationalException {

    if (evidenceType.equals(CASEEVIDENCE.BDMVTW) && isICCase(caseID)) {

      final BDMVTWDeductionCountKey countKey = new BDMVTWDeductionCountKey();
      countKey.evidenceID = evidenceID;
      final BDMVTWDeductionCountDetails countDetails = BDMVTWDeductionFactory
        .newInstance().countVTWILIByEvidenceID(countKey);

      // If deduction are applied the payments restrict the user from deleting
      // the VTW evidence
      if (countDetails.numberOfRecords > 0) {
        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          new AppException(BDMEVIDENCE.ERR_CANNOT_DELETE_VTW_EVIDENCE), null,
          InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine,
          0);

        TransactionInfo.getInformationalManager().failOperation();
      }

      try {
        final BDMVTWDeductionKeyStruct1 bdmVTWDeductionKeyStruct1 =
          new BDMVTWDeductionKeyStruct1();
        bdmVTWDeductionKeyStruct1.evidenceID = evidenceID;
        bdmVTWDeductionKeyStruct1.recordStatusCode = RECORDSTATUS.NORMAL;

        final curam.ca.gc.bdm.vtw.deduction.entity.struct.BDMVTWDeductionDetails bddmVTWdedDetails =
          BDMVTWDeductionFactory.newInstance()
            .readByEvidenceID(bdmVTWDeductionKeyStruct1);

        bddmVTWdedDetails.recordStatusCode = RECORDSTATUS.CANCELLED;
        final BDMVTWDeductionKey bdmVTWDeductionKey =
          new BDMVTWDeductionKey();
        bdmVTWDeductionKey.vtwDeductionID = bddmVTWdedDetails.vtwDeductionID;
        final BDMVTWDeductionDtls modifyBDMDeductionDtls =
          new BDMVTWDeductionDtls();
        modifyBDMDeductionDtls.assign(bddmVTWdedDetails);

        BDMVTWDeductionFactory.newInstance().modify(bdmVTWDeductionKey,
          modifyBDMDeductionDtls);

        final BDMVTWDeductionKey vtwDeductionKey = new BDMVTWDeductionKey();
        vtwDeductionKey.vtwDeductionID = bddmVTWdedDetails.vtwDeductionID;
        caseDeductions.recordVTWDeductionByIC(vtwDeductionKey);
      } catch (final RecordNotFoundException rnfe) {
        rnfe.printStackTrace();
      }
    }
  }

  /**
   * This method verifies whether it is IC case or not.
   *
   * @param caseID
   * @return boolean
   * @throws AppException
   * @throws InformationalException
   */
  private boolean isICCase(final long caseID)
    throws AppException, InformationalException {

    // Instantiate objects and assign values.
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    boolean result = false;
    caseHeaderKey.caseID = caseID;

    // Read Case Header table and try to read the case type code.
    try {
      final String caseTypeCode =
        CaseHeaderFactory.newInstance().read(caseHeaderKey).caseTypeCode;

      // Verifies case type code.
      if (CASETYPECODE.INTEGRATEDCASE.equals(caseTypeCode)) {
        result = true;
      }
    } catch (final RecordNotFoundException rnfe) {
      result = false;
    }

    // Return boolean result.
    return result;
  }

}
