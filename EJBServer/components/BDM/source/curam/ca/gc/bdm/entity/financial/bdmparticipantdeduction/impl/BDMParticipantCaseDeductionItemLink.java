package curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.impl;

import curam.ca.gc.bdm.entity.bdmcasedeductionitem.fact.BDMCaseDeductionItemFactory;
import curam.ca.gc.bdm.entity.bdmcasedeductionitem.struct.BDMCaseDeductionItemDtls;
import curam.ca.gc.bdm.entity.bdmcasedeductionitem.struct.BDMCaseDeductionItemKey;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.fact.BDMParticipantDeductionCaseLinkFactory;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.fact.BDMParticipantDeductionItemFactory;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMParticipantCaseDeductionItemLinkDtls;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMParticipantDeductionCaseLinkDtls;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMParticipantDeductionCaseLinkKey;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMParticipantDeductionItemDtls;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMParticipantDeductionItemKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMParticipantCaseDeductionItemLink extends
  curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.base.BDMParticipantCaseDeductionItemLink {

  /**
   * If the participant deduction is linked to an external liability, add an
   * entry in the BDMCaseDeductionItem table
   */
  @Override
  protected void
    postinsert(final BDMParticipantCaseDeductionItemLinkDtls details)
      throws AppException, InformationalException {

    final BDMParticipantDeductionCaseLinkKey caseLinkKey =
      new BDMParticipantDeductionCaseLinkKey();
    caseLinkKey.bdmParticipantDeductionCaseLinkID =
      details.bdmParticipantDeductionCaseLinkID;
    final BDMParticipantDeductionCaseLinkDtls caseLinkDtls =
      BDMParticipantDeductionCaseLinkFactory.newInstance().read(caseLinkKey);

    final BDMParticipantDeductionItemKey pdiKey =
      new BDMParticipantDeductionItemKey();
    pdiKey.bdmParticipantDeductionItemID =
      caseLinkDtls.bdmParticipantDeductionItemID;
    final BDMParticipantDeductionItemDtls pdiDtls =
      BDMParticipantDeductionItemFactory.newInstance().read(pdiKey);

    final BDMCaseDeductionItemKey cdiKey = new BDMCaseDeductionItemKey();
    cdiKey.caseDeductionItemID = details.caseDeductionItemID;

    final BDMCaseDeductionItemDtls cdiDtls =
      BDMCaseDeductionItemFactory.newInstance().read(cdiKey);
    cdiDtls.externalLiabilityID = pdiDtls.externalLiabilityID;

    BDMCaseDeductionItemFactory.newInstance().modify(cdiKey, cdiDtls);

  }

}
