package curam.ca.gc.bdm.sl.maintainparticipantdeduction.intf;

import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityKey;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMParticipantCaseDeductionItemLinkKey;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMParticipantDeductionCaseLinkKey;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMParticipantDeductionItemDtls;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMParticipantDeductionItemKey;
import curam.ca.gc.bdm.sl.financial.maintainparticipantdeduction.struct.BDMParticipantDeductionCaseLinkList;
import curam.ca.gc.bdm.sl.financial.maintainparticipantdeduction.struct.BDMParticipantDeductionCreateDetails;
import curam.ca.gc.bdm.sl.financial.maintainparticipantdeduction.struct.BDMParticipantDeductionLinkBenefitDetails;
import curam.ca.gc.bdm.sl.financial.maintainparticipantdeduction.struct.BDMParticipantDeductionModifyDetails;
import curam.core.struct.CaseDeductionItemKey;
import curam.core.struct.IndicatorStruct;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public interface MaintainParticipantDeduction {

  public BDMParticipantDeductionItemKey createParticipantDeduction(
    BDMParticipantDeductionCreateDetails createDetails)
    throws AppException, InformationalException;

  public BDMParticipantDeductionCaseLinkKey
    linkBenefitCase(BDMParticipantDeductionLinkBenefitDetails benefitDetails)
      throws AppException, InformationalException;

  public void createCaseDeductions(BDMParticipantDeductionCaseLinkKey key)
    throws AppException, InformationalException;

  public BDMParticipantDeductionCaseLinkList listLinkedBenefitsByExtLby(
    BDMExternalLiabilityKey key) throws AppException, InformationalException;

  public void unlinkBenefitCase(BDMParticipantDeductionCaseLinkKey key)
    throws AppException, InformationalException;

  public void modifyParticipantDeduction(
    BDMParticipantDeductionModifyDetails modifyDetails)
    throws AppException, InformationalException;

  public void modifyCaseDeduction(BDMParticipantDeductionCaseLinkKey key,
    BDMParticipantDeductionItemDtls originalParticipantDeductionItemDtls)
    throws AppException, InformationalException;

  public void activateParticipantDeduction(BDMParticipantDeductionItemKey key)
    throws AppException, InformationalException;

  public void activateCaseDeductions(
    final BDMParticipantDeductionCaseLinkKey caseLinkKey)
    throws AppException, InformationalException;

  public void
    deactivateParticipantDeduction(BDMParticipantDeductionItemKey key)
      throws AppException, InformationalException;

  public void deactivateCaseDeductions(
    final BDMParticipantDeductionCaseLinkKey caseLinkKey,
    final IndicatorStruct deletedUnusedInd)
    throws AppException, InformationalException;

  public void deactivateSingleCaseDeduction(
    final CaseDeductionItemKey caseDeductionItemKey,
    final IndicatorStruct deletedUnusedInd)
    throws AppException, InformationalException;

  public void
    closeCaseDeductions(final BDMParticipantDeductionCaseLinkKey key)
      throws AppException, InformationalException;

  public void closeSingleCaseDeduction(
    final BDMParticipantCaseDeductionItemLinkKey caseDeductionItemLinkKey)
    throws AppException, InformationalException;

  public IndicatorStruct
    isCaseDeductionEverProcessed(final CaseDeductionItemKey key)
      throws AppException, InformationalException;

}
