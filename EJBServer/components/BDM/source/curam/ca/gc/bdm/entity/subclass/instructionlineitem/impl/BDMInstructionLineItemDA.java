package curam.ca.gc.bdm.entity.subclass.instructionlineitem.impl;

import curam.ca.gc.bdm.entity.financial.fact.BDMInstructionLineItemFactory;
import curam.ca.gc.bdm.entity.financial.struct.BDMInstructionLineItemDtls;
import curam.ca.gc.bdm.entity.subclass.instructionlineitem.struct.InstructionLineItemKey;
import curam.codetable.ILICATEGORY;
import curam.codetable.ILITYPE;
import curam.core.struct.IndicatorStruct;
import curam.core.struct.InstructionLineItemDtls;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMInstructionLineItemDA extends
  curam.ca.gc.bdm.entity.subclass.instructionlineitem.base.BDMInstructionLineItemDA {

  @Override
  protected void postinsert(final InstructionLineItemDtls details)
    throws AppException, InformationalException {

    final BDMInstructionLineItemDtls bdmInstructionItemDtls =
      new BDMInstructionLineItemDtls();
    bdmInstructionItemDtls.instructLineItemID = details.instructLineItemID;
    // START - Added changes for Task 9810
    if (ILICATEGORY.PAYMENTINSTRUCTION
      .equals(details.instructLineItemCategory)
      && !(ILITYPE.DEDUCTIONITEM.equals(details.instructionLineItemType)
        || ILITYPE.TAXPAYMENT.equals(details.instructionLineItemType))) {
      final InstructionLineItemKey instructionLineItemKey =
        new InstructionLineItemKey();
      instructionLineItemKey.instructionLineItemID =
        details.instructLineItemID;
      final IndicatorStruct indicator =
        this.readILITaxReportDetails(instructionLineItemKey);
      bdmInstructionItemDtls.taxReportInd = indicator.changeAllIndicator;
    }
    // END - Task 9810 - JSHAH
    BDMInstructionLineItemFactory.newInstance()
      .insert(bdmInstructionItemDtls);
  }

}
