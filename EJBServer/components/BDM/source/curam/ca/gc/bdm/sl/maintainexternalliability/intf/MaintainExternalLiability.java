package curam.ca.gc.bdm.sl.maintainexternalliability.intf;

import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityKey;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMExternalLbyWizardIDKey;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMExternalLbyWizardSaveStep1Details;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMExternalLbyWizardSaveStep2Details;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMExternalLiabilityBenefitDetailsList;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMExternalLiabilityDetailsList;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMExternalLiabilityHistoryDetailsList;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMModifyLiabilityDetails;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMReadLbyDetailsForEdit;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMRegisterExternalLbyDetails;
import curam.ca.gc.bdm.sl.financial.struct.BDMCheckLiabilityExistDetails;
import curam.ca.gc.bdm.sl.financial.struct.BDMModifyDOJLbyDetailsKey;
import curam.ca.gc.bdm.sl.financial.struct.GenILIDOJDeductionDetails;
import curam.ca.gc.bdm.sl.financial.struct.GenILITaxDeductionDetails;
import curam.core.sl.struct.WizardStateID;
import curam.core.struct.Amount;
import curam.core.struct.CaseDeductionItemDtls;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.FCAmount;
import curam.core.struct.FinCompCoverPeriod;
import curam.core.struct.FinancialComponentDtls;
import curam.core.struct.TotalDeductionAmount;
import curam.core.struct.VersionNumberDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public interface MaintainExternalLiability {

  public BDMExternalLiabilityDetailsList listLiabilities(
    final ConcernRoleKey key) throws AppException, InformationalException;

  public BDMExternalLbyWizardIDKey
    createLbySaveStep1Data(final BDMExternalLbyWizardSaveStep1Details key)
      throws AppException, InformationalException;

  public BDMExternalLiabilityBenefitDetailsList listBenefitCases(
    WizardStateID wizardStateID) throws AppException, InformationalException;

  public BDMExternalLiabilityKey createLbySaveStep2Data(
    WizardStateID wizardStateID,
    BDMExternalLbyWizardSaveStep2Details step2Details)
    throws AppException, InformationalException;

  void activateLiability(BDMExternalLiabilityKey key,
    VersionNumberDetails versionNumberDetails)
    throws AppException, InformationalException;

  public void deactivateLiability(final BDMExternalLiabilityKey key,
    VersionNumberDetails versionNumberDetails)
    throws AppException, InformationalException;

  public void deleteLiability(final BDMExternalLiabilityKey key,
    VersionNumberDetails versionNumberDetails)
    throws AppException, InformationalException;

  public BDMReadLbyDetailsForEdit readLbyDetailsForEdit(
    BDMExternalLiabilityKey key) throws AppException, InformationalException;

  public void modifyLiabilityDetails(BDMModifyLiabilityDetails modifyDtls,
    boolean resetOutstandingAmountInd)
    throws AppException, InformationalException;

  public BDMExternalLiabilityHistoryDetailsList listLiabilityHistory(
    BDMExternalLiabilityKey key) throws AppException, InformationalException;

  public VersionNumberDetails
    readLiabilityVersion(final BDMExternalLiabilityKey key)
      throws AppException, InformationalException;

  public void determineDeductionILIAmount(
    CaseDeductionItemDtls caseDeductionItemDtls, FCAmount fcAmount)
    throws AppException, InformationalException;

  public void postDeductionReduceOutstandingBalanceAmount(
    CaseDeductionItemDtls caseDeductionItemDtls, Amount iliAmount)
    throws AppException, InformationalException;

  public BDMExternalLiabilityBenefitDetailsList
    listBenefitCasesForModify(final BDMExternalLiabilityKey key)
      throws AppException, InformationalException;

  public BDMExternalLiabilityKey
    registerLiability(final BDMRegisterExternalLbyDetails lbyDetails)
      throws AppException, InformationalException;

  public BDMExternalLiabilityKey
    getLiabilityByExtRefNumber(final BDMCheckLiabilityExistDetails lbyDetails)
      throws AppException, InformationalException;

  public void
    modifyDOJLiabilityDetails(BDMModifyDOJLbyDetailsKey modifyDetails)
      throws AppException, InformationalException;

  public GenILIDOJDeductionDetails getDOJDeductionDetails(
    CaseDeductionItemDtls caseDeductionItemDtls,
    FinancialComponentDtls fcDtls, FinCompCoverPeriod fcCoverPeriod,
    FCAmount fcAmount, TotalDeductionAmount totalDeductionAmount)
    throws AppException, InformationalException;

  public GenILITaxDeductionDetails getTaxDeductionDetails(
    CaseDeductionItemDtls caseDeductionItemDtls,
    FinancialComponentDtls fcDtls, FinCompCoverPeriod fcCoverPeriod,
    FCAmount fcAmount, TotalDeductionAmount totalDeductionAmount)
    throws AppException, InformationalException;
}
