package curam.ca.gc.bdm.sl.maintaindojliability.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMDEDUCTIONTYPE;
import curam.ca.gc.bdm.codetable.BDMEXTERNALLIABILITYTYPE;
import curam.ca.gc.bdm.entity.bdmcasedeductionitem.fact.BDMCaseDeductionItemFactory;
import curam.ca.gc.bdm.entity.bdmcasedeductionitem.struct.BDMCaseDeductionExternalLiabilityKey;
import curam.ca.gc.bdm.entity.bdmcasedeductionitem.struct.BDMCaseDeductionItemDtls;
import curam.ca.gc.bdm.entity.bdmcasedeductionitem.struct.BDMCaseDeductionItemDtlsList;
import curam.ca.gc.bdm.entity.bdmexternalliability.fact.BDMExternalLiabilityFactory;
import curam.ca.gc.bdm.entity.bdmexternalliability.intf.BDMExternalLiability;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityDtls;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityKey;
import curam.ca.gc.bdm.entity.deduction.fact.BDMDeductionFactory;
import curam.ca.gc.bdm.entity.deduction.intf.BDMDeduction;
import curam.ca.gc.bdm.entity.deduction.struct.GetDeductionNameByCaseKey;
import curam.ca.gc.bdm.entity.financial.fact.BDMExternalLiabilityDOJDataFactory;
import curam.ca.gc.bdm.entity.financial.intf.BDMExternalLiabilityDOJData;
import curam.ca.gc.bdm.entity.financial.struct.BDMExternalLiabilityDOJDataDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMExternalLiabilityDOJDataKey;
import curam.ca.gc.bdm.entity.subclass.productdelivery.fact.BDMProductDeliveryDAFactory;
import curam.ca.gc.bdm.entity.subclass.productdelivery.struct.BDMGetDOJDedEligibleCaseKey;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMRegisterExternalLbyDetails;
import curam.ca.gc.bdm.sl.financial.struct.BDMCheckDOJLiabilityExistDetails;
import curam.ca.gc.bdm.sl.financial.struct.BDMCheckLiabilityExistDetails;
import curam.ca.gc.bdm.sl.financial.struct.BDMModifyDOJLbyDetails;
import curam.ca.gc.bdm.sl.financial.struct.BDMModifyDOJLbyDetailsKey;
import curam.ca.gc.bdm.sl.financial.struct.BDMRegisterDOJLbyDetails;
import curam.ca.gc.bdm.sl.maintaincasedeductions.impl.MaintainCaseDeductions;
import curam.ca.gc.bdm.sl.maintainexternalliability.impl.MaintainExternalLiability;
import curam.ca.gc.bdm.sl.maintainparticipantdeduction.impl.MaintainParticipantDeduction;
import curam.ca.gc.bdm.sl.productdelivery.impl.MaintainDeductionDetails;
import curam.ca.gc.bdm.sl.struct.CreateCaseDeductionDetails;
import curam.codetable.CASESTATUS;
import curam.codetable.RECORDSTATUS;
import curam.core.facade.fact.ProductDeliveryFactory;
import curam.core.facade.intf.ProductDelivery;
import curam.core.facade.struct.CancelDeductionKey;
import curam.core.facade.struct.DeductionItemKeyVersionNo;
import curam.core.fact.CaseDeductionItemFactory;
import curam.core.intf.CaseDeductionItem;
import curam.core.sl.fact.CaseStatusModeFactory;
import curam.core.sl.infrastructure.entity.struct.VersionNo;
import curam.core.sl.struct.CaseIDKey;
import curam.core.sl.struct.DeductionName;
import curam.core.struct.CaseDeductionItemDtls;
import curam.core.struct.CaseDeductionItemKey;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseHeaderKeyList;
import curam.core.struct.IndicatorStruct;
import curam.core.struct.VersionNumberDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;

public class MaintainDOJLiability implements
  curam.ca.gc.bdm.sl.maintaindojliability.intf.MaintainDOJLiability {

  @Inject
  MaintainExternalLiability maintainExternalLiabilityObj;

  @Inject
  MaintainParticipantDeduction maintainParticipantDeductionObj;

  @Inject
  MaintainDeductionDetails maintainDeductionDetailsObj;

  @Inject
  MaintainCaseDeductions maintainCaseDedObj;

  public MaintainDOJLiability() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Creates an active external liability.
   */
  @Override
  public BDMExternalLiabilityKey
    registerLiability(final BDMRegisterDOJLbyDetails details)
      throws AppException, InformationalException {

    final BDMRegisterExternalLbyDetails lbyDetails =
      new BDMRegisterExternalLbyDetails();

    lbyDetails.assign(details);
    lbyDetails.externalRefNumber =
      details.sinIdentification + "_" + details.obligationIDSuffix;

    if (details.isFeeTypeInd) {
      lbyDetails.liabilityTypeCode = BDMEXTERNALLIABILITYTYPE.DOJ_FEES;
    } else {
      lbyDetails.liabilityTypeCode = BDMEXTERNALLIABILITYTYPE.DOJ_ARREARS;
    }

    final BDMExternalLiabilityKey externalLiabilityKey =
      maintainExternalLiabilityObj.registerLiability(lbyDetails);

    final BDMExternalLiabilityDOJDataDtls dojDtls =
      new BDMExternalLiabilityDOJDataDtls();

    dojDtls.externalLiabilityID = externalLiabilityKey.externalLiabilityID;
    dojDtls.deductionRate = details.holdbackDeductionRate;
    dojDtls.deductionAmount = details.perPayDeductionAmount;
    dojDtls.deductionMinPayAmount = details.debtorFixedAmount;
    dojDtls.deductionFirstPayInd = details.fixedAmountInd;
    dojDtls.obligationID = details.obligationID;
    dojDtls.obligationIDSuffix = details.obligationIDSuffix;
    dojDtls.sinIdentification = details.sinIdentification;

    BDMExternalLiabilityDOJDataFactory.newInstance().insert(dojDtls);

    final VersionNumberDetails liabilityVersion =
      maintainExternalLiabilityObj.readLiabilityVersion(externalLiabilityKey);

    maintainExternalLiabilityObj.activateLiability(externalLiabilityKey,
      liabilityVersion);

    createDeduction(externalLiabilityKey);

    return externalLiabilityKey;
  }

  /**
   * Creates a DOJ deduction
   */
  @Override
  public void createDeduction(final BDMExternalLiabilityKey key)
    throws AppException, InformationalException {

    final BDMExternalLiabilityDtls liabilityDtls =
      BDMExternalLiabilityFactory.newInstance().read(key);

    String deductionType;
    // determine the deduction type based off the liability type
    if (liabilityDtls.liabilityTypeCode
      .equals(BDMEXTERNALLIABILITYTYPE.DOJ_ARREARS)) {

      deductionType = BDMDEDUCTIONTYPE.DOJ_FOE_WITHHOLD_ARREARS;

    } else {

      deductionType = BDMDEDUCTIONTYPE.DOJ_FOE_WITHHOLD_FEES;
    }

    // finds any cases eligible for a deduction to be added (should only return
    // 1)
    final BDMGetDOJDedEligibleCaseKey dojKey =
      new BDMGetDOJDedEligibleCaseKey();
    dojKey.externalLiabilityID = key.externalLiabilityID;
    dojKey.caseStatus = CASESTATUS.CLOSED;
    dojKey.deductionType = deductionType;
    final CaseHeaderKeyList caseList = BDMProductDeliveryDAFactory
      .newInstance().searchCaseEligibleForDOJDeduction(dojKey);

    final CreateCaseDeductionDetails dedDetails =
      new CreateCaseDeductionDetails();
    final BDMDeduction bdmDeductionObj = BDMDeductionFactory.newInstance();
    final ProductDelivery productDeliveryObj =
      ProductDeliveryFactory.newInstance();
    final CaseIDKey caseIDKey = new CaseIDKey();
    for (final CaseHeaderKey caseKey : caseList.dtls) {
      final GetDeductionNameByCaseKey dedNameCaseKey =
        new GetDeductionNameByCaseKey();
      dedNameCaseKey.caseID = caseKey.caseID;
      dedNameCaseKey.deductionType = deductionType;

      // find the deduction name
      final DeductionName readDeductionNameByCase =
        bdmDeductionObj.readDeductionNameByCase(dedNameCaseKey);

      dedDetails.deductionName = readDeductionNameByCase.deductionName;
      dedDetails.caseID = caseKey.caseID;
      dedDetails.startDate = liabilityDtls.creationDate;
      dedDetails.rate = 0.01;
      maintainCaseDedObj.createThirdPartyCaseDeduction(dedDetails, key);

      // OOTB regenrate financials doesn't happen for batch transaction types.
      caseIDKey.caseID = caseKey.caseID;
      maintainCaseDedObj.regenerateCaseFinancials(caseIDKey);
    }
  }

  /**
   * Modifies the DOJ liability
   */
  @Override
  public void modifyLiability(final BDMModifyDOJLbyDetails modifyDetails)
    throws AppException, InformationalException {

    // modify the liability details
    final BDMModifyDOJLbyDetailsKey modifyDetailsKey =
      new BDMModifyDOJLbyDetailsKey();
    modifyDetailsKey.assign(modifyDetails);
    maintainExternalLiabilityObj.modifyDOJLiabilityDetails(modifyDetailsKey);

    // modify the doj specific details
    final BDMExternalLiabilityDOJData dojDataObj =
      BDMExternalLiabilityDOJDataFactory.newInstance();
    final BDMExternalLiabilityDOJDataKey dojKey =
      new BDMExternalLiabilityDOJDataKey();
    dojKey.externalLiabilityID = modifyDetails.externalLiabilityID;

    final BDMExternalLiabilityDOJDataDtls dojDtls = dojDataObj.read(dojKey);

    dojDtls.deductionAmount = modifyDetails.perPayDeductionAmount;
    dojDtls.deductionRate = modifyDetails.holdbackDeductionRate;
    dojDtls.deductionMinPayAmount = modifyDetails.debtorFixedAmount;
    dojDtls.deductionFirstPayInd = modifyDetails.fixedAmountInd;
    dojDtls.obligationID = modifyDetails.obligationID;
    dojDtls.obligationIDSuffix = modifyDetails.obligationIDSuffix;

    dojDataObj.modify(dojKey, dojDtls);

  }

  /**
   * Performs a soft delete on the external liability and deactivates/cancels
   * the associated deduction item
   */
  @Override
  public void deleteLiability(final BDMExternalLiabilityKey key)
    throws AppException, InformationalException {

    // Disables FC regeneration from checking if a case is in DPP status
    CaseStatusModeFactory.newInstance()
      .setDisableRegenFCsCaseStatusDPPCheck(true);

    final BDMExternalLiability externalLiabilityObj =
      BDMExternalLiabilityFactory.newInstance();

    final BDMExternalLiabilityDtls liabilityDtls =
      externalLiabilityObj.read(key);

    // if it is already deleted, do not do anything
    if (!liabilityDtls.recordStatusCode.equals(RECORDSTATUS.NORMAL)) {
      return;
    }

    final BDMCaseDeductionExternalLiabilityKey bdmCaseDeductionKey =
      new BDMCaseDeductionExternalLiabilityKey();
    bdmCaseDeductionKey.externalLiabilityID = key.externalLiabilityID;
    final BDMCaseDeductionItemDtlsList caseDeductionList =
      BDMCaseDeductionItemFactory.newInstance()
        .readByExternalLiabilityID(bdmCaseDeductionKey);

    final CaseDeductionItem caseDeductionItemObj =
      CaseDeductionItemFactory.newInstance();
    final CaseIDKey caseIDKey = new CaseIDKey();

    // should only return 1 item at most - since DOJ liabilities are only
    // applied to one benefit and are always variable deductions
    for (final BDMCaseDeductionItemDtls deduction : caseDeductionList.dtls) {

      final CaseDeductionItemKey cdiKey = new CaseDeductionItemKey();
      cdiKey.caseDeductionItemID = deduction.caseDeductionItemID;

      // check if the deduction has been processed before
      final IndicatorStruct caseDeductionEverProcessed =
        maintainParticipantDeductionObj.isCaseDeductionEverProcessed(cdiKey);

      final VersionNo readVersionNo =
        caseDeductionItemObj.readVersionNo(cdiKey);

      // if it's been processed, deactivate it
      if (caseDeductionEverProcessed.changeAllIndicator) {
        final DeductionItemKeyVersionNo deductionKey =
          new DeductionItemKeyVersionNo();

        deductionKey.caseDeductionItemID = cdiKey.caseDeductionItemID;
        deductionKey.versionNo = readVersionNo.versionNo;
        ProductDeliveryFactory.newInstance()
          .deactivateDeduction(deductionKey);
      }
      // otherwise, cancel the deduction
      else {
        final CancelDeductionKey deductionKey = new CancelDeductionKey();

        deductionKey.caseDeductionItemID = cdiKey.caseDeductionItemID;
        deductionKey.versionNo = readVersionNo.versionNo;

        ProductDeliveryFactory.newInstance().cancelDeduction(deductionKey);
      }
      maintainDeductionDetailsObj
        .resequencePriorities(cdiKey.caseDeductionItemID);
      final CaseDeductionItemDtls caseDeductionItemDtls =
        caseDeductionItemObj.read(cdiKey);
      caseIDKey.caseID = caseDeductionItemDtls.caseID;
      maintainCaseDedObj.regenerateCaseFinancials(caseIDKey);
    }

    // delete the liability
    liabilityDtls.recordStatusCode = RECORDSTATUS.CANCELLED;

    externalLiabilityObj.modify(key, liabilityDtls);

  }

  @Override
  public BDMExternalLiabilityKey
    getLiabilityByExtRefNumber(final BDMCheckDOJLiabilityExistDetails details)
      throws AppException, InformationalException {

    final BDMCheckLiabilityExistDetails lbyDetails =
      new BDMCheckLiabilityExistDetails();

    if (details.isFeeTypeInd) {
      lbyDetails.liabilityTypeCode = BDMEXTERNALLIABILITYTYPE.DOJ_FEES;
    } else {
      lbyDetails.liabilityTypeCode = BDMEXTERNALLIABILITYTYPE.DOJ_ARREARS;
    }

    lbyDetails.concernRoleID = details.concernRoleID;
    lbyDetails.externalRefNumber =
      details.sinIdentification + "_" + details.obligationIDSuffix;

    return maintainExternalLiabilityObj
      .getLiabilityByExtRefNumber(lbyDetails);
  }

}
