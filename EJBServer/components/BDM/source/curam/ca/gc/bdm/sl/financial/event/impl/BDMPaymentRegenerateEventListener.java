package curam.ca.gc.bdm.sl.financial.event.impl;

import curam.ca.gc.bdm.entity.bdminstructionlineitemrelation.fact.BDMInstructionLineItemRelationFactory;
import curam.ca.gc.bdm.entity.bdminstructionlineitemrelation.intf.BDMInstructionLineItemRelation;
import curam.ca.gc.bdm.entity.bdminstructionlineitemrelation.struct.BDMRelatedIDTypeCodeKey;
import curam.ca.gc.bdm.entity.financial.fact.BDMInstructionLineItemFactory;
import curam.ca.gc.bdm.entity.financial.intf.BDMInstructionLineItem;
import curam.ca.gc.bdm.entity.financial.struct.BDMInstruLineItemKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMInstructionLineItemDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMInstructionLineItemKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxDetails;
import curam.ca.gc.bdm.entity.financial.struct.VersionNo;
import curam.codetable.ILIRELATIONSHIP;
import curam.core.fact.InstructionLineItemFactory;
import curam.core.sl.event.impl.PaymentRegenerateEventListener;
import curam.core.struct.FinancialInstructionKey;
import curam.core.struct.ILIFinInstructID;
import curam.core.struct.InstructionLineItemDtls;
import curam.core.struct.InstructionLineItemDtlsList;
import curam.core.struct.InstructionLineItemRelationDtls;
import curam.core.struct.InstructionLineItemRelationDtlsList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Trace;

public class BDMPaymentRegenerateEventListener
  extends PaymentRegenerateEventListener {

  /**
   * On a payment regeneration, find the related cancelled payment and set the
   * new ILIs tax indicators on BDMInstructionLineItem to the previous ILI tax
   * indicators
   */
  @Override
  public void
    paymentRegenerated(final FinancialInstructionKey financialInstructionKey)
      throws InformationalException {

    final ILIFinInstructID finInstructKey = new ILIFinInstructID();
    finInstructKey.finInstructionID =
      financialInstructionKey.finInstructionID;
    try {
      // find all ILIs linked to Fin Instruction
      final InstructionLineItemDtlsList iliDtlsList =
        InstructionLineItemFactory.newInstance()
          .searchByFinInstructID(finInstructKey);

      final BDMInstructionLineItemRelation iliRelatedObj =
        BDMInstructionLineItemRelationFactory.newInstance();
      final BDMRelatedIDTypeCodeKey relatedItemKey =
        new BDMRelatedIDTypeCodeKey();

      final BDMInstructionLineItem bdmILIObj =
        BDMInstructionLineItemFactory.newInstance();
      final BDMInstruLineItemKey bdmILIKey = new BDMInstruLineItemKey();
      final BDMTaxDetails bdmTaxDetails = new BDMTaxDetails();

      for (final InstructionLineItemDtls ili : iliDtlsList.dtls) {

        // find related ILIs that the new ILI was regenerated from
        relatedItemKey.relatedLineItemID = ili.instructLineItemID;
        relatedItemKey.typeCode = ILIRELATIONSHIP.REGENERATION;
        final InstructionLineItemRelationDtlsList relatedItemList =
          iliRelatedObj.searchByRelatedItemIDTypeCode(relatedItemKey);

        // modify their tax indicators - there should only be one related ILI
        for (final InstructionLineItemRelationDtls relatedILI : relatedItemList.dtls) {
          // find the previous ILIs BDMInstructionLineItem details
          bdmILIKey.instructLineItemID = relatedILI.instructLineItemID;
          final BDMInstructionLineItemDtls bdmILIDtls =
            bdmILIObj.readByInstructionLineItemID(bdmILIKey);

          final BDMInstructionLineItemKey iliToUpdateKey =
            new BDMInstructionLineItemKey();
          iliToUpdateKey.instructLineItemID = ili.instructLineItemID;
          final VersionNo iliUpdateVersionNo =
            bdmILIObj.readVersionNo(iliToUpdateKey);

          // modify the new ILI to have the same tax indicator details
          bdmILIKey.instructLineItemID = ili.instructLineItemID;
          bdmTaxDetails.preTaxDeductionInd = bdmILIDtls.preTaxDeductionInd;
          bdmTaxDetails.taxReportInd = bdmILIDtls.taxReportInd;
          bdmTaxDetails.versionNo = iliUpdateVersionNo.versionNo;
          bdmILIObj.modifyTaxDetails(bdmILIKey, bdmTaxDetails);

        }
      }
    } catch (final AppException e) {
      Trace.kTopLevelLogger.error(e.getMessage(), e);
      throw new InformationalException();
    }

    super.paymentRegenerated(financialInstructionKey);
  }

}
