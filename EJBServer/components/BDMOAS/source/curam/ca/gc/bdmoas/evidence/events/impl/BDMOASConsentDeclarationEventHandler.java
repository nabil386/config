package curam.ca.gc.bdmoas.evidence.events.impl;

import curam.ca.gc.bdm.evidence.events.impl.BDMAbstractEvidenceEventHandler;
import curam.ca.gc.bdmoas.util.impl.BDMOASTaskUtil;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.facade.fact.ConcernRoleFactory;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.infrastructure.struct.EIEvidenceKeyList;
import curam.core.struct.CaseKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

/**
 * 104301: DEV: Implement Manage Consent and Declaration Evidence - R2
 * Evidence Events Handler class for ConsentDeclaration Evidence.
 *
 * @author guruvamshikrishna.va
 *
 */
public class BDMOASConsentDeclarationEventHandler
  extends BDMAbstractEvidenceEventHandler {

  @Override
  public CASEEVIDENCEEntry evidenceType() {

    return CASEEVIDENCEEntry.BDM_CONSENT_DECLARATION;

  }

  @Override
  public boolean subscribePostActivation() {

    return true;

  }

  /**
   * Creates TASK-50 when signing on behalf is true.
   */
  @Override
  public void postActivation(
    final EvidenceControllerInterface evidenceController, final CaseKey key,
    final EIEvidenceKeyList list)
    throws AppException, InformationalException {

    final EIEvidenceKeyList evidenceKeyList =
      this.filterEvidenceKeyList(list);

    for (final EIEvidenceKey evidenceKey : evidenceKeyList.dtls) {

      // Read evidence descriptor record
      final EIEvidenceReadDtls evidenceReadDtls =
        evidenceController.readEvidence(evidenceKey);
      final EvidenceDescriptor descriptor =
        EvidenceDescriptorFactory.newInstance();
      final EvidenceDescriptorKey descriptorKey = new EvidenceDescriptorKey();
      descriptorKey.evidenceDescriptorID =
        evidenceReadDtls.descriptor.evidenceDescriptorID;
      final EvidenceDescriptorDtls descriptorDtls =
        descriptor.read(descriptorKey);

      final curam.core.facade.struct.ConcernRoleIDKey concernRoleIDKey =
        new curam.core.facade.struct.ConcernRoleIDKey();

      concernRoleIDKey.concernRoleID = descriptorDtls.participantID;
      final String participantName = ConcernRoleFactory.newInstance()
        .readConcernRoleName(concernRoleIDKey).concernRoleName;

      final DynamicEvidenceDataDetails evidence =
        (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;

      final String signingOnBehalf =
        evidence.getAttribute("isSigningOnBehalf").getValue();
      if (Boolean.valueOf(signingOnBehalf))
        BDMOASTaskUtil.createVerifyAuthorizedPersonTask(key.caseID,
          participantName);

    }
  }

}
