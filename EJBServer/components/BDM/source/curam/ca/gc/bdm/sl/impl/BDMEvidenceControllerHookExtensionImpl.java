package curam.ca.gc.bdm.sl.impl;

import curam.ca.gc.bdm.message.BDMEVIDENCE;
import curam.ca.gc.bdm.vtw.deduction.entity.fact.BDMVTWDeductionFactory;
import curam.ca.gc.bdm.vtw.deduction.entity.struct.BDMVTWDeductionCountDetails;
import curam.ca.gc.bdm.vtw.deduction.entity.struct.BDMVTWDeductionCountKey;
import curam.codetable.CASEEVIDENCE;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.core.sl.infrastructure.impl.ValidationManagerFactory;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.struct.CaseKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement.InformationalType;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;

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
public class BDMEvidenceControllerHookExtensionImpl
  implements BDMEvidenceControllerHookExtensionIntf {

  /**
   * BDM component postInsertEvidence implementation
   */
  @Override
  public void postInsertEvidence(final CaseKey caseKey,
    final EIEvidenceKey evidenceKey)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub
  }

  /**
   * BDM component postModifyEvidence implementation
   */
  @Override
  public void postModifyEvidence(final CaseKey caseKey,
    final EIEvidenceKey evidenceKey)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub
  }

  /**
   * BDM component postRemoveEvidence implementation
   */
  @Override
  public void postRemoveEvidence(final CaseKey caseKey,
    final EIEvidenceKey evKey) throws AppException, InformationalException {

    // TODO Auto-generated method stub

  }

  @Override
  public void preRemoveEvidence(final CaseKey paramCaseKey,
    final EIEvidenceKey paramEIEvidenceKey)
    throws AppException, InformationalException {

    // If the Evidence Type is OAS Recovery Tax
    if (paramEIEvidenceKey.evidenceType.equals(CASEEVIDENCE.BDMVTW)) {
      checkVTWDeductionProcessed(paramEIEvidenceKey);
    }

  }

  /**
   * This Event will be called when discard an InEdit Evidence.
   *
   * @param paramEvidenceDescriptorKey the param evidence descriptor key
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Override
  public void postDiscardPendingUpdate(
    final EvidenceDescriptorKey paramEvidenceDescriptorKey)
    throws AppException, InformationalException {

  }

  /**
   * Check if the OAS Recovery Tax is already used in the Payment
   * Processing.
   * If used then throw the exception message.
   *
   * @param paramEIEvidenceKey
   * Contains the evidence key value
   */
  private void
    checkVTWDeductionProcessed(final EIEvidenceKey paramEIEvidenceKey)
      throws AppException, InformationalException {

    // Check if any deduction associated with this evidence is
    // included in the gross to net calculation.

    final BDMVTWDeductionCountKey countKey = new BDMVTWDeductionCountKey();
    countKey.evidenceID = paramEIEvidenceKey.evidenceID;
    final BDMVTWDeductionCountDetails countDetails =
      BDMVTWDeductionFactory.newInstance().countVTWILIByEvidenceID(countKey);

    // If deduction are applied the payments restrict the user from deleting
    // the VTW evidence
    if (countDetails.numberOfRecords > 0) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMEVIDENCE.ERR_CANNOT_DELETE_VTW_EVIDENCE), null,
        InformationalType.kError,
        curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine, 0);

      TransactionInfo.getInformationalManager().failOperation();
    }
  }

}
