package curam.ca.gc.bdmoas.sl.financial.bdmoasmaintaininstructionlineitem.impl;

import curam.ca.gc.bdm.codetable.BDMDEDUCTIONTYPE;
import curam.ca.gc.bdm.entity.deduction.fact.BDMDeductionFactory;
import curam.ca.gc.bdm.entity.deduction.intf.BDMDeduction;
import curam.ca.gc.bdm.entity.deduction.struct.BDMDeductionDetails;
import curam.ca.gc.bdm.entity.deduction.struct.BDMDeductionKey;
import curam.ca.gc.bdm.entity.financial.fact.BDMInstructionLineItemFactory;
import curam.ca.gc.bdm.entity.financial.intf.BDMInstructionLineItem;
import curam.ca.gc.bdm.entity.financial.struct.BDMInstructionLineItemDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMInstructionLineItemKey;
import curam.ca.gc.bdm.entity.subclass.instructionlineitem.fact.BDMInstructionLineItemDAFactory;
import curam.ca.gc.bdm.entity.subclass.instructionlineitem.intf.BDMInstructionLineItemDA;
import curam.ca.gc.bdm.sl.financial.struct.GenILIDOJDeductionDetails;
import curam.ca.gc.bdm.sl.financial.struct.GenILITaxDeductionDetails;
import curam.ca.gc.bdmoas.sl.bdmoasmaintainexternalliability.fact.BDMOASMaintainExternalLiabilityFactory;
import curam.ca.gc.bdmoas.sl.bdmoasmaintainexternalliability.intf.BDMOASMaintainExternalLiability;
import curam.core.fact.MaintainInstructionLineItemFactory;
import curam.core.struct.Amount;
import curam.core.struct.CaseDeductionItemDtls;
import curam.core.struct.CurrencyExchangeDtls;
import curam.core.struct.FCAmount;
import curam.core.struct.FinCompCoverPeriod;
import curam.core.struct.FinancialComponentDtls;
import curam.core.struct.InstructionLineItemDtls;
import curam.core.struct.InstructionLineItemID;
import curam.core.struct.InstructionLineItemKey;
import curam.core.struct.TotalDeductionAmount;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMOASMaintainInstructionLineItem extends
  curam.ca.gc.bdmoas.sl.financial.bdmoasmaintaininstructionlineitem.base.BDMOASMaintainInstructionLineItem {

  @Override
  public InstructionLineItemID createThirdPartyDeductionILIs(
    final CaseDeductionItemDtls caseDeductionItemDtls,
    final FinancialComponentDtls fcDtls,
    final FinCompCoverPeriod fcCoverPeriod, final FCAmount fcAmount,
    final CurrencyExchangeDtls currencyExchangeDtls,
    final TotalDeductionAmount totalDeductionAmount)
    throws AppException, InformationalException {

    InstructionLineItemID iliID = new InstructionLineItemID();

    final BDMDeduction bdmDeductionObj = BDMDeductionFactory.newInstance();
    final BDMDeductionKey bdmDeductionKey = new BDMDeductionKey();
    bdmDeductionKey.deductionID = caseDeductionItemDtls.deductionID;
    final BDMDeductionDetails bdmDeductionDetails =
      bdmDeductionObj.readByDeductionID(bdmDeductionKey);

    // instance of service layer
    final BDMOASMaintainExternalLiability bdmoasMaintainExternalLiability =
      BDMOASMaintainExternalLiabilityFactory.newInstance();

    // Added changes for DOJ Arrears

    GenILIDOJDeductionDetails genILIDOJDeductionDetails =
      new GenILIDOJDeductionDetails();
    if (BDMDEDUCTIONTYPE.DOJ_FOE_WITHHOLD_ARREARS
      .equals(bdmDeductionDetails.deductionType)
      || BDMDEDUCTIONTYPE.DOJ_FOE_WITHHOLD_FEES
        .equals(bdmDeductionDetails.deductionType)) {
      // Move this call into getDOJDeductionDetails.
      genILIDOJDeductionDetails = bdmoasMaintainExternalLiability
        .getDOJDeductionDetails(caseDeductionItemDtls, fcDtls, fcCoverPeriod,
          fcAmount, totalDeductionAmount);
      fcAmount.amount = genILIDOJDeductionDetails.fcAmount;
    } else if (BDMDEDUCTIONTYPE.CRA_SET_OFF
      .equals(bdmDeductionDetails.deductionType)
      || BDMDEDUCTIONTYPE.OAS_RCV_TAX
        .equals(bdmDeductionDetails.deductionType)
      || BDMDEDUCTIONTYPE.OAS_NRT.equals(bdmDeductionDetails.deductionType)
      || BDMDEDUCTIONTYPE.PENALTIES.equals(bdmDeductionDetails.deductionType)
      || BDMDEDUCTIONTYPE.CPP_OVERPAYMENTS
        .equals(bdmDeductionDetails.deductionType)
      || BDMDEDUCTIONTYPE.EMPLOYMENT_INSURANCE
        .equals(bdmDeductionDetails.deductionType)
      || BDMDEDUCTIONTYPE.VETERANS_AFFAIRS_CANADA
        .equals(bdmDeductionDetails.deductionType)) {
      bdmoasMaintainExternalLiability
        .determineDeductionILIAmount(caseDeductionItemDtls, fcAmount);
    } else if (BDMDEDUCTIONTYPE.BASIC_FEDERAL_TAX
      .equals(bdmDeductionDetails.deductionType)
      || BDMDEDUCTIONTYPE.BASIC_PROVINCIAL_TAX
        .equals(bdmDeductionDetails.deductionType)) {
      final GenILITaxDeductionDetails genILITaxDeductionDetails =
        bdmoasMaintainExternalLiability.getTaxDeductionDetails(
          caseDeductionItemDtls, fcDtls, fcCoverPeriod, fcAmount,
          totalDeductionAmount);
      fcAmount.amount = genILITaxDeductionDetails.fcAmount;
    }

    // BUG 63360 - Starts
    // check for FC amount if zero then will not call OOTB API to create third
    // party deduction
    if (!fcAmount.amount.isZero()) {
      // BUG 63360 - Ends
      final curam.core.intf.MaintainInstructionLineItem maintainInstructionLineItem =
        MaintainInstructionLineItemFactory.newInstance();
      // calling OOTB method to add deduction ILIs
      iliID = maintainInstructionLineItem.addDeductionItemILIs(fcDtls,
        fcCoverPeriod, fcAmount, currencyExchangeDtls, totalDeductionAmount);
    }

    if (iliID.instructionLineItemID != 0) {
      final InstructionLineItemKey instructionLineItemKey =
        new InstructionLineItemKey();
      instructionLineItemKey.instructLineItemID = iliID.instructionLineItemID;

      final BDMInstructionLineItemDA bdmInstructionLineItemDA =
        BDMInstructionLineItemDAFactory.newInstance();
      final Amount iliAmount =
        bdmInstructionLineItemDA.readILIAmountByILIID(instructionLineItemKey);
      bdmoasMaintainExternalLiability
        .postDeductionReduceOutstandingBalanceAmount(caseDeductionItemDtls,
          iliAmount);

      // DOJ Arrears related post process
      if (BDMDEDUCTIONTYPE.DOJ_FOE_WITHHOLD_ARREARS
        .equals(bdmDeductionDetails.deductionType)) {
        final BDMInstructionLineItem bdmInstructionLineItemObj =
          BDMInstructionLineItemFactory.newInstance();
        final BDMInstructionLineItemKey bdmInstructionLineItemKey =
          new BDMInstructionLineItemKey();
        bdmInstructionLineItemKey.instructLineItemID =
          iliID.instructionLineItemID;
        final BDMInstructionLineItemDtls bdmInstructionLineItemDtls =
          bdmInstructionLineItemObj.read(bdmInstructionLineItemKey);
        bdmInstructionLineItemDtls.dojDebtRatio =
          genILIDOJDeductionDetails.dojDebtRatio;
        bdmInstructionLineItemObj.modify(bdmInstructionLineItemKey,
          bdmInstructionLineItemDtls);
      }
    }
    return iliID;
  }

  @Override
  public void regenerateILI(final InstructionLineItemDtls iliDtls)
    throws AppException, InformationalException {

    // calling the super class method
    super.regenerateILI(iliDtls);
  }

}
