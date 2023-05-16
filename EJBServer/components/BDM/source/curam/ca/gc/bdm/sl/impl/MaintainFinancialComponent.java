package curam.ca.gc.bdm.sl.impl;

import curam.ca.gc.bdm.entity.struct.BDMProductDtls;
import curam.ca.gc.bdm.entity.struct.BDMProductKey;
import curam.codetable.RULESCOMPONENTTYPE;
import curam.core.facade.fact.ProductDeliveryFactory;
import curam.core.sl.struct.CaseIDKey;
import curam.core.struct.CaseID;
import curam.core.struct.DeductibleInd;
import curam.core.struct.ProductIDDetails;
import curam.core.struct.RulesObjectiveID;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public abstract class MaintainFinancialComponent
  extends curam.ca.gc.bdm.sl.base.MaintainFinancialComponent {

  @Override
  public DeductibleInd isComponentDeductible(final CaseID caseID,
    final RulesObjectiveID rulesObjectiveID)
    throws AppException, InformationalException {

    // Create return object
    DeductibleInd deductibleInd = new DeductibleInd();

    deductibleInd = super.isComponentDeductible(caseID, rulesObjectiveID);

    if (!deductibleInd.deductibleInd && rulesObjectiveID.rulesObjectiveID
      .equalsIgnoreCase(RULESCOMPONENTTYPE.BENEFITUNDERPAYMENT)) {
      final CaseIDKey caseIDKey = new CaseIDKey();
      caseIDKey.caseID = caseID.caseID;
      final ProductIDDetails productIDDetails =
        ProductDeliveryFactory.newInstance().readProductID(caseIDKey);
      final BDMProductKey productKey = new BDMProductKey();
      productKey.productID = productIDDetails.productID;
      final BDMProductDtls bdmProductDtls =
        curam.ca.gc.bdm.entity.fact.BDMProductFactory.newInstance()
          .read(productKey);

      if (bdmProductDtls.underpaymentDeductibleInd) {
        deductibleInd.deductibleInd =
          bdmProductDtls.underpaymentDeductibleInd;
      }
    }
    return deductibleInd;
  }
}
