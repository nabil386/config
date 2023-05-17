package curam.ca.gc.bdm.sl.impl;

import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.struct.CaseKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Implementable;

/**
 * 95630 DEV: Implement Manage Override Evidence - R2
 * BDM component implementation for EvidenceControllerHook methods.
 * This class can be subclassed in other BenefitGroup (OAS, CPP) components
 * using Google guice binding. The existing implementation in this class, if any
 * should be called using super in the BenefitGroup component so that BDM
 * component as well as BenefitGroup specific logic will be executed.
 *
 * NOTE: This is an extension for the existing BDMEvidenceControllerHook class
 * to make the Hook available across components through guice binding.
 *
 * @author guruvamshikrishna.va
 *
 */
@Implementable
public interface BDMEvidenceControllerHookExtensionIntf {

  public void postInsertEvidence(final CaseKey caseKey,
    final EIEvidenceKey evidenceKey)
    throws AppException, InformationalException;

  public void postModifyEvidence(final CaseKey caseKey,
    final EIEvidenceKey evidenceKey)
    throws AppException, InformationalException;

  public void postRemoveEvidence(final CaseKey caseKey,
    final EIEvidenceKey evKey) throws AppException, InformationalException;

  public void preRemoveEvidence(CaseKey paramCaseKey,
    EIEvidenceKey paramEIEvidenceKey)
    throws AppException, InformationalException;

  public void
    postDiscardPendingUpdate(EvidenceDescriptorKey paramEvidenceDescriptorKey)
      throws AppException, InformationalException;
}
