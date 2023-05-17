package curam.ca.gc.bdmoas.evidence.events.impl;

import curam.ca.gc.bdm.evidence.events.impl.BDMAbstractEvidenceEventHandler;
import curam.ca.gc.bdmoas.codetable.BDMOASAPPLICATIONSTATUS;
import curam.ca.gc.bdmoas.codetable.BDMOASCANCELSTATUS;
import curam.ca.gc.bdmoas.evidence.constants.impl.BDMOASApplicationDetailsConstants;
import curam.ca.gc.bdmoas.evidence.constants.impl.BDMOASBenefitCancellationRequestConstants;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor;
import curam.core.sl.infrastructure.entity.struct.EvdObjInstanceChangeDtls;
import curam.core.sl.infrastructure.entity.struct.ParentKey;
import curam.core.sl.infrastructure.entity.struct.SuccessionID;
import curam.core.sl.infrastructure.fact.EvidenceRelationshipFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceModifyDtls;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.intf.EvidenceRelationship;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.infrastructure.struct.EIEvidenceKeyList;
import curam.core.sl.infrastructure.struct.EvidenceKey;
import curam.core.sl.infrastructure.struct.ParentList;
import curam.core.sl.infrastructure.struct.SuccIDParIDEvdTypeCaseIDKey;
import curam.core.struct.CaseKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataAttributeDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMOASBenefitCancellationRequestEventHandler
  extends BDMAbstractEvidenceEventHandler {

  @Override
  public CASEEVIDENCEEntry evidenceType() {

    return CASEEVIDENCEEntry.OAS_BENEFIT_CANCELLATION;

  }

  @Override
  public boolean subscribePostActivation() {

    return true;

  }

  /**
   * Sets the Application Details status to Cancelled.
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

      final DynamicEvidenceDataDetails evidence =
        (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;

      final String cancellationStatus = evidence
        .getAttribute(
          BDMOASBenefitCancellationRequestConstants.CANCELLATION_STATUS)
        .getValue();

      if (BDMOASCANCELSTATUS.REPAID.equals(cancellationStatus)) {

        final EIEvidenceReadDtls applicationReadDtls =
          this.getApplicationDetails(evidenceKey, evidenceController);

        final DynamicEvidenceDataDetails applicationDtlsEvidence =
          (DynamicEvidenceDataDetails) applicationReadDtls.evidenceObject;

        final DynamicEvidenceDataAttributeDetails applicationStatus =
          applicationDtlsEvidence.getAttribute(
            BDMOASApplicationDetailsConstants.APPLICATION_STATUS);

        if (!BDMOASAPPLICATIONSTATUS.CANCELLED
          .equals(applicationStatus.getValue())) {

          applicationStatus.setValue(BDMOASAPPLICATIONSTATUS.CANCELLED);

          final EIEvidenceModifyDtls modifyDtls = new EIEvidenceModifyDtls();
          modifyDtls.descriptor.assign(applicationReadDtls.descriptor);
          modifyDtls.evidenceObject = applicationDtlsEvidence;

          final EIEvidenceKey applicationDtlsKey = new EIEvidenceKey();
          applicationDtlsKey.evidenceID = applicationDtlsEvidence.getID();
          applicationDtlsKey.evidenceType =
            CASEEVIDENCE.OAS_APPLICATION_DETAILS;

          evidenceController.modifyEvidence(applicationDtlsKey, modifyDtls);

          final SuccessionID successionID = new SuccessionID();
          successionID.successionID =
            applicationReadDtls.descriptor.successionID;

          final EvidenceDescriptor evidenceDescriptor =
            EvidenceDescriptorFactory.newInstance();
          final SuccIDParIDEvdTypeCaseIDKey inEditInstanceKey =
            new SuccIDParIDEvdTypeCaseIDKey();
          inEditInstanceKey.caseID = applicationReadDtls.descriptor.caseID;
          inEditInstanceKey.evidenceType =
            applicationReadDtls.descriptor.evidenceType;
          inEditInstanceKey.successionID =
            applicationReadDtls.descriptor.successionID;

          final EvdObjInstanceChangeDtls changeDtls = evidenceDescriptor
            .searchInEditEvdObjInstanceChanges(inEditInstanceKey).dtls.get(0);

          final EvidenceKey inEditAppDtlsKey = new EvidenceKey();
          inEditAppDtlsKey.evidenceID = changeDtls.relatedID;
          inEditAppDtlsKey.evidenceDescriptorID =
            changeDtls.evidenceDescriptorID;
          inEditAppDtlsKey.evidenceType = changeDtls.evidenceType;

          evidenceController.applyInEdit(inEditAppDtlsKey);

        }

      }

    }

  }

  private EIEvidenceReadDtls getApplicationDetails(
    final EIEvidenceKey evidenceKey,
    final EvidenceControllerInterface evidenceController)
    throws AppException, InformationalException {

    final EvidenceRelationship evidenceRelationship =
      EvidenceRelationshipFactory.newInstance();
    final ParentList parentList =
      evidenceRelationship.getParentKeyList(evidenceKey);

    final ParentKey parentKey = parentList.list.dtls.get(0);

    final EIEvidenceKey parentEvidenceKey = new EIEvidenceKey();
    parentEvidenceKey.evidenceID = parentKey.parentID;
    parentEvidenceKey.evidenceType = parentKey.parentType;

    final EIEvidenceReadDtls evidenceReadDtls =
      evidenceController.readEvidence(parentEvidenceKey);

    return evidenceReadDtls;

  }

}
