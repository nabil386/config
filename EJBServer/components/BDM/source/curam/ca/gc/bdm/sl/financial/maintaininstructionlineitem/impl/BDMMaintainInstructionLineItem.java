package curam.ca.gc.bdm.sl.financial.maintaininstructionlineitem.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMDEDUCTIONTYPE;
import curam.ca.gc.bdm.entity.deduction.fact.BDMDeductionFactory;
import curam.ca.gc.bdm.entity.deduction.intf.BDMDeduction;
import curam.ca.gc.bdm.entity.deduction.struct.BDMDeductionDetails;
import curam.ca.gc.bdm.entity.deduction.struct.BDMDeductionKey;
import curam.ca.gc.bdm.entity.deduction.struct.GetDeductionNameByCaseKey;
import curam.ca.gc.bdm.entity.financial.fact.BDMInstructionLineItemFactory;
import curam.ca.gc.bdm.entity.financial.intf.BDMInstructionLineItem;
import curam.ca.gc.bdm.entity.financial.struct.BDMInstruLineItemKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMInstructionLineItemDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMInstructionLineItemKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMPreTaxDetails;
import curam.ca.gc.bdm.entity.financial.struct.VersionNo;
import curam.ca.gc.bdm.entity.subclass.instructionlineitem.fact.BDMInstructionLineItemDAFactory;
import curam.ca.gc.bdm.entity.subclass.instructionlineitem.intf.BDMInstructionLineItemDA;
import curam.ca.gc.bdm.sl.financial.struct.GenILIDOJDeductionDetails;
import curam.ca.gc.bdm.sl.financial.struct.GenILITaxDeductionDetails;
import curam.ca.gc.bdm.sl.maintainexternalliability.impl.MaintainExternalLiability;
import curam.ca.gc.bdm.sl.productdelivery.impl.MaintainDeductionDetails;
import curam.codetable.CREDITDEBIT;
import curam.codetable.DEDUCTIONACTIONTYPE;
import curam.codetable.DEDUCTIONCATEGORYCODE;
import curam.codetable.DEDUCTIONITEMTYPE;
import curam.codetable.FINCOMPONENTSTATUS;
import curam.codetable.ILICATEGORY;
import curam.codetable.ILISTATUS;
import curam.core.fact.CaseDeductionItemFCLinkFactory;
import curam.core.fact.CaseDeductionItemFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.FinancialComponentFactory;
import curam.core.fact.InstructionLineItemFactory;
import curam.core.fact.InstructionLineItemProcessingFactory;
import curam.core.fact.MaintainDeductionItemsFactory;
import curam.core.intf.CaseDeductionItem;
import curam.core.intf.FinancialComponent;
import curam.core.intf.InstructionLineItem;
import curam.core.intf.MaintainDeductionItems;
import curam.core.sl.struct.DeductionName;
import curam.core.struct.Amount;
import curam.core.struct.CaseDeductionItemDtls;
import curam.core.struct.CaseDeductionItemFCLinkDtls;
import curam.core.struct.CaseDeductionItemFinCompID;
import curam.core.struct.CaseDeductionItemKey;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CurrencyExchangeDtls;
import curam.core.struct.FCAmount;
import curam.core.struct.FinCompCoverPeriod;
import curam.core.struct.FinancialComponentDtls;
import curam.core.struct.FinancialComponentKey;
import curam.core.struct.ILIAmount;
import curam.core.struct.ILICaseIDCreditDebitType;
import curam.core.struct.ILICaseIDFinInstCatStatus;
import curam.core.struct.InstructionLineItemDetails;
import curam.core.struct.InstructionLineItemDtls;
import curam.core.struct.InstructionLineItemID;
import curam.core.struct.InstructionLineItemKey;
import curam.core.struct.MaintainDeductionItemDetails;
import curam.core.struct.ReadParticipantRoleIDDetails;
import curam.core.struct.SumUnallocatedAmt;
import curam.core.struct.TotalDeductionAmount;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.RecordNotFoundException;
import curam.util.type.Date;
import curam.util.type.Money;
import curam.util.type.UniqueID;

public class BDMMaintainInstructionLineItem extends
  curam.ca.gc.bdm.sl.financial.maintaininstructionlineitem.base.BDMMaintainInstructionLineItem {

  @Inject
  protected MaintainExternalLiability maintainExternalLiabilityObj;

  @Inject
  MaintainDeductionDetails maintainDeductionDetailsObj;

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

    // Added changes for DOJ Arrears
    GenILIDOJDeductionDetails genILIDOJDeductionDetails =
      new GenILIDOJDeductionDetails();
    if (BDMDEDUCTIONTYPE.DOJ_FOE_WITHHOLD_ARREARS
      .equals(bdmDeductionDetails.deductionType)
      || BDMDEDUCTIONTYPE.DOJ_FOE_WITHHOLD_FEES
        .equals(bdmDeductionDetails.deductionType)) {
      // Move this call into getDOJDeductionDetails.
      genILIDOJDeductionDetails = maintainExternalLiabilityObj
        .getDOJDeductionDetails(caseDeductionItemDtls, fcDtls, fcCoverPeriod,
          fcAmount, totalDeductionAmount);
      fcAmount.amount = genILIDOJDeductionDetails.fcAmount;
    } else if (BDMDEDUCTIONTYPE.CRA_SET_OFF
      .equals(bdmDeductionDetails.deductionType)) {
      maintainExternalLiabilityObj
        .determineDeductionILIAmount(caseDeductionItemDtls, fcAmount);
    } else if (BDMDEDUCTIONTYPE.BASIC_FEDERAL_TAX
      .equals(bdmDeductionDetails.deductionType)
      || BDMDEDUCTIONTYPE.BASIC_PROVINCIAL_TAX
        .equals(bdmDeductionDetails.deductionType)) {
      final GenILITaxDeductionDetails genILITaxDeductionDetails =
        maintainExternalLiabilityObj.getTaxDeductionDetails(
          caseDeductionItemDtls, fcDtls, fcCoverPeriod, fcAmount,
          totalDeductionAmount);
      fcAmount.amount = genILITaxDeductionDetails.fcAmount;
    }

    // BUG 63360 - Starts
    // check for FC amount if zero then will not call OOTB API to create third
    // party deduction
    if (!fcAmount.amount.isZero()) {
      // BUG 63360 - Ends
      // calling OOTB method to create third party deduction ILIs
      iliID = super.createThirdPartyDeductionILIs(caseDeductionItemDtls,
        fcDtls, fcCoverPeriod, fcAmount, currencyExchangeDtls,
        totalDeductionAmount);
    }

    if (iliID.instructionLineItemID != 0) {
      final InstructionLineItemKey instructionLineItemKey =
        new InstructionLineItemKey();
      instructionLineItemKey.instructLineItemID = iliID.instructionLineItemID;

      final BDMInstructionLineItemDA bdmInstructionLineItemDA =
        BDMInstructionLineItemDAFactory.newInstance();
      final Amount iliAmount =
        bdmInstructionLineItemDA.readILIAmountByILIID(instructionLineItemKey);
      maintainExternalLiabilityObj
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

  /**
   * For an ILI if its an non-reversible deduction type regenerate ILI for New
   * Liability case created earlier
   */
  @Override
  public void regenerateILI(final InstructionLineItemDtls iliDtls)
    throws AppException, InformationalException {

    final FinancialComponent financialComponentInst =
      FinancialComponentFactory.newInstance();
    final CaseDeductionItem CaseDeductionDtls =
      CaseDeductionItemFactory.newInstance();
    final BDMInstructionLineItem bdmInstructionLineItem =
      BDMInstructionLineItemFactory.newInstance();
    final InstructionLineItem instructionLineItemInst =
      InstructionLineItemFactory.newInstance();

    final BDMInstruLineItemKey instrumentIdkey = new BDMInstruLineItemKey();
    instrumentIdkey.instructLineItemID = iliDtls.instructLineItemID;
    final BDMInstructionLineItemDtls bdmInstructionLineItemDtls =
      bdmInstructionLineItem.readByInstructionLineItemID(instrumentIdkey);

    // Check if this ILI is of non-reversible deduction type and there exists a
    // liablity case created.
    if (bdmInstructionLineItemDtls.nonReverseRelatedCaseID != 0l) {
      final InstructionLineItemDtls iliDtls_can =
        new InstructionLineItemDtls();
      iliDtls_can.assign(iliDtls);

      CaseDeductionItemDtls caseDeductionItemDtls = null;
      CaseDeductionItemDtls caseDeductionItemDtls_old = null;
      final CaseDeductionItem caseDeductionItemObj =
        CaseDeductionItemFactory.newInstance();
      try {
        final CaseDeductionItemFinCompID caseDeductionItemFinCompID =
          new CaseDeductionItemFinCompID();
        caseDeductionItemFinCompID.financialCompID = iliDtls.financialCompID;
        caseDeductionItemDtls_old = caseDeductionItemObj
          .readByFinancialCompID(caseDeductionItemFinCompID);

      } catch (final RecordNotFoundException rfe) {
        caseDeductionItemDtls_old = new CaseDeductionItemDtls();
      }

      final ILICaseIDCreditDebitType iliCaseIDCreditDebitType =
        new ILICaseIDCreditDebitType();
      iliCaseIDCreditDebitType.caseID =
        bdmInstructionLineItemDtls.nonReverseRelatedCaseID;
      iliCaseIDCreditDebitType.creditDebitType = CREDITDEBIT.DEBIT;
      final SumUnallocatedAmt sumUnallocatedAmt =
        instructionLineItemInst.readSumUnprocessedAmtByCaseIDCreditDebitType(
          iliCaseIDCreditDebitType);
      final ILICaseIDFinInstCatStatus iliCaseIDFinInstCatStatus =
        new ILICaseIDFinInstCatStatus();
      iliCaseIDFinInstCatStatus.caseID =
        bdmInstructionLineItemDtls.nonReverseRelatedCaseID;
      iliCaseIDFinInstCatStatus.finInstructionID = iliDtls.finInstructionID;
      iliCaseIDFinInstCatStatus.instructLineItemCategory =
        ILICATEGORY.PAYMENTINSTRUCTIONFORDEDUCTION;
      iliCaseIDFinInstCatStatus.statusCode = ILISTATUS.UNPROCESSED;
      final ILIAmount unprocessedDeductionAmount = instructionLineItemInst
        .readSumAmtByCaseIDCatStatus(iliCaseIDFinInstCatStatus);

      final Money iliAmount = new Money(sumUnallocatedAmt.amount.getValue()
        - unprocessedDeductionAmount.amount.getValue());

      if (iliAmount.getValue() > 0) {
        final FinancialComponent financialComponentObj =
          FinancialComponentFactory.newInstance();
        FinancialComponentKey financialComponentKey =
          new FinancialComponentKey();
        financialComponentKey.financialCompID = iliDtls.financialCompID;

        FinancialComponentDtls regeneratedFCDtls =
          financialComponentObj.read(financialComponentKey);

        regeneratedFCDtls.financialCompID = UniqueID.nextUniqueID();
        regeneratedFCDtls.amount = iliAmount;
        regeneratedFCDtls.statusCode = FINCOMPONENTSTATUS.CLOSED_OUTOFDATE;
        financialComponentObj.insert(regeneratedFCDtls);

        final MaintainDeductionItems maintainDeductionItemsObj =
          MaintainDeductionItemsFactory.newInstance();
        final MaintainDeductionItemDetails maintainDeductionItemDetails =
          new MaintainDeductionItemDetails();

        final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();

        caseHeaderKey.caseID =
          bdmInstructionLineItemDtls.nonReverseRelatedCaseID;

        final ReadParticipantRoleIDDetails readParticipantRoleIDDetails =
          CaseHeaderFactory.newInstance()
            .readParticipantRoleID(caseHeaderKey);

        final BDMDeduction bdmDeductionObj =
          BDMDeductionFactory.newInstance();
        final GetDeductionNameByCaseKey getDeductionNameByCaseKey =
          new GetDeductionNameByCaseKey();
        getDeductionNameByCaseKey.caseID = caseDeductionItemDtls_old.caseID;
        getDeductionNameByCaseKey.deductionType =
          BDMDEDUCTIONTYPE.PROGRAM_SPECIFIC_OVERPAYMENT;
        DeductionName deductionName =
          bdmDeductionObj.readDeductionNameByCase(getDeductionNameByCaseKey);
        maintainDeductionItemDetails.deductionName =
          deductionName.deductionName;

        maintainDeductionItemDetails.deductionType = DEDUCTIONITEMTYPE.FIXED;
        maintainDeductionItemDetails.category =
          DEDUCTIONCATEGORYCODE.APPLIEDDEDUCTION;
        maintainDeductionItemDetails.actionType =
          DEDUCTIONACTIONTYPE.REMAINING;
        maintainDeductionItemDetails.startDate =
          caseDeductionItemDtls_old.startDate;
        maintainDeductionItemDetails.endDate =
          caseDeductionItemDtls_old.endDate;
        final int basePriority1 = maintainDeductionDetailsObj.getBasePriority(
          caseDeductionItemDtls_old.caseID,
          maintainDeductionItemDetails.deductionName);
        maintainDeductionItemDetails.priority = basePriority1;
        maintainDeductionItemDetails.amount = iliAmount;
        maintainDeductionItemDetails.caseID =
          caseDeductionItemDtls_old.caseID;
        maintainDeductionItemDetails.caseNomineeID =
          caseDeductionItemDtls_old.caseNomineeID;
        maintainDeductionItemDetails.concernRoleID =
          caseDeductionItemDtls_old.concernRoleID;
        maintainDeductionItemDetails.createdDate =
          caseDeductionItemDtls_old.createdDate;
        maintainDeductionItemDetails.relatedCaseID =
          bdmInstructionLineItemDtls.nonReverseRelatedCaseID;
        maintainDeductionItemDetails.liabilityConcernRoleID =
          readParticipantRoleIDDetails.concernRoleID;
        maintainDeductionItemsObj
          .createProcessedDeductionItem(maintainDeductionItemDetails);

        final CaseDeductionItemKey caseDeductionItemKey =
          new CaseDeductionItemKey();
        caseDeductionItemKey.caseDeductionItemID =
          maintainDeductionItemDetails.caseDeductionItemID;

        caseDeductionItemDtls =
          CaseDeductionItemFactory.newInstance().read(caseDeductionItemKey);

        final CaseDeductionItemFCLinkDtls caseDeductionItemFCLinkDtls =
          new CaseDeductionItemFCLinkDtls();
        caseDeductionItemFCLinkDtls.caseDeductionItemFCLinkID =
          UniqueID.nextUniqueID();
        caseDeductionItemFCLinkDtls.financialComponentID =
          regeneratedFCDtls.financialCompID;
        caseDeductionItemFCLinkDtls.caseDeductionItemID =
          maintainDeductionItemDetails.caseDeductionItemID;

        CaseDeductionItemFCLinkFactory.newInstance()
          .insert(caseDeductionItemFCLinkDtls);

        iliDtls.financialCompID = regeneratedFCDtls.financialCompID;

        final InstructionLineItemDetails regeneratedILIDetails =
          new InstructionLineItemDetails();

        regeneratedILIDetails.assign(iliDtls);

        regeneratedILIDetails.statusCode = ILISTATUS.UNPROCESSED;
        regeneratedILIDetails.finInstructionID = 0;
        regeneratedILIDetails.creditDebitType = iliDtls.creditDebitType;
        regeneratedILIDetails.effectiveDate = Date.getCurrentDate();
        regeneratedILIDetails.instrumentGenInd = true;

        regeneratedILIDetails.amount = iliAmount;
        regeneratedILIDetails.unprocessedAmount = iliAmount;

        final InstructionLineItemID regeneratedILIid =
          addInstructionLineItem(regeneratedILIDetails);

        // 2) Relate the original ILI to the regenerated ILI
        /*
         * createILIRelationship(iliDtls.instructLineItemID,
         * regeneratedILIid.instructionLineItemID,
         * ILIRELATIONSHIPEntry.get(ILIRELATIONSHIP.REGENERATION));
         */

        // create deduction payment ili on the liability side.
        financialComponentKey = new FinancialComponentKey();
        financialComponentKey.financialCompID = iliDtls.financialCompID;

        regeneratedFCDtls = financialComponentObj.read(financialComponentKey);
        final FinCompCoverPeriod finCompCoverPeriod =
          new FinCompCoverPeriod();
        finCompCoverPeriod.coverPeriodFrom = iliDtls.coverPeriodFrom;
        finCompCoverPeriod.coverPeriodTo = iliDtls.coverPeriodTo;
        final FCAmount fcAmount = new FCAmount();
        fcAmount.amount = iliAmount;
        final CurrencyExchangeDtls currencyExchangeDtls =
          new CurrencyExchangeDtls();
        currencyExchangeDtls.currencyExchangeID = iliDtls.currencyExchangeID;

        final InstructionLineItemDetails deductionPmtILIDetails =
          setAppliedDedPaymentILIDetails(caseDeductionItemDtls,
            regeneratedFCDtls, finCompCoverPeriod, fcAmount,
            currencyExchangeDtls);

        // Add Deduction Payment Instruction Line Item Details
        final InstructionLineItemID regeneratedDeductionPaymentILIid =
          addInstructionLineItem(deductionPmtILIDetails);

        // Get pre-tax deduction indicator.
        boolean preTaxDeductionInd = false;
        try {
          // Get priority for tax deductions.
          getDeductionNameByCaseKey.deductionType =
            BDMDEDUCTIONTYPE.BASIC_FEDERAL_TAX;
          deductionName = bdmDeductionObj
            .readDeductionNameByCase(getDeductionNameByCaseKey);
          final int basePriority2 =
            maintainDeductionDetailsObj.getBasePriority(
              caseDeductionItemDtls_old.caseID, deductionName.deductionName);
          if (basePriority2 > basePriority1) {
            preTaxDeductionInd = true;
          }
        } catch (final RecordNotFoundException rnfex) {
        }

        try {
          // Get priority for tax deductions.
          getDeductionNameByCaseKey.deductionType =
            BDMDEDUCTIONTYPE.BASIC_PROVINCIAL_TAX;
          deductionName = bdmDeductionObj
            .readDeductionNameByCase(getDeductionNameByCaseKey);
          final int basePriority3 =
            maintainDeductionDetailsObj.getBasePriority(
              caseDeductionItemDtls_old.caseID, deductionName.deductionName);
          if (basePriority3 > basePriority1) {
            preTaxDeductionInd = true;
          }
        } catch (final RecordNotFoundException rnfex) {
        }

        if (preTaxDeductionInd == true) {
          final BDMInstructionLineItem bdmInstructionItemObj =
            BDMInstructionLineItemFactory.newInstance();
          final BDMInstructionLineItemKey bdmInstructionLineItemKey =
            new BDMInstructionLineItemKey();
          bdmInstructionLineItemKey.instructLineItemID =
            regeneratedILIid.instructionLineItemID;
          final VersionNo versionNo =
            bdmInstructionItemObj.readVersionNo(bdmInstructionLineItemKey);
          final BDMInstruLineItemKey bdmInstruLineItemKey =
            new BDMInstruLineItemKey();
          bdmInstructionLineItemKey.instructLineItemID =
            regeneratedILIid.instructionLineItemID;
          final BDMPreTaxDetails bdmPreTaxDetails = new BDMPreTaxDetails();
          bdmPreTaxDetails.preTaxDeductionInd = true;
          bdmPreTaxDetails.versionNo = versionNo.versionNo;
          bdmInstruLineItemKey.instructLineItemID =
            regeneratedILIid.instructionLineItemID;
          bdmInstructionItemObj.modifyPreTaxDeductionInd(bdmInstruLineItemKey,
            bdmPreTaxDetails);
        }

        // Need to set up the relationship between the deduction item and
        // deduction payment ILIs
        final InstructionLineItemID deductionInstructionLineItemID =
          new InstructionLineItemID();
        deductionInstructionLineItemID.instructionLineItemID =
          regeneratedILIid.instructionLineItemID;
        createILIRelationship(deductionInstructionLineItemID,
          regeneratedDeductionPaymentILIid);

        // Now process the regenerated deduction payment ILI
        final InstructionLineItemKey iliKey = new InstructionLineItemKey();

        iliKey.instructLineItemID =
          regeneratedDeductionPaymentILIid.instructionLineItemID;
        final InstructionLineItemDtls regeneratedILIDtls =
          InstructionLineItemFactory.newInstance().read(iliKey);

        InstructionLineItemProcessingFactory.newInstance()
          .processDeductionLineGroup(regeneratedILIDtls);

        // Find ILI id of related deduction payment
        /*
         * final PrimaryILIidTypeCode primaryILIidTypeCode =
         * new PrimaryILIidTypeCode();
         *
         * primaryILIidTypeCode.instructLineItemID =
         * iliDtls.instructLineItemID;
         * primaryILIidTypeCode.typeCode =
         * ILIRELATIONSHIP.DEDUCTIONPAYMENT;
         *
         * final InstructionLineItemRelationDtls iliRelationDtls =
         * InstructionLineItemRelationFactory.newInstance()
         * .readByInstLineItemIDTypeCode(primaryILIidTypeCode);
         *
         * // 6) Relate this new regenerated ILI to the canceled ILI
         * createILIRelationship(iliRelationDtls.relatedLineItemID,
         * regeneratedDeductionPaymentILIid.instructionLineItemID,
         * ILIRELATIONSHIPEntry.get(ILIRELATIONSHIP.REGENERATION));
         */

        iliDtls.instructLineItemID = regeneratedILIid.instructionLineItemID;

      }
    } else {
      super.regenerateILI(iliDtls);
    }
  }
}
