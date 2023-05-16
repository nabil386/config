package curam.ca.gc.bdmoas.facade.productdelivery.deduction.impl;

import curam.ca.gc.bdmoas.entity.oasdeduction.fact.BDMOASDeductionFactory;
import curam.ca.gc.bdmoas.entity.oasdeduction.struct.BDMOASDeductionDtls;
import curam.ca.gc.bdmoas.entity.oasdeduction.struct.BDMOASNRTCaseDeductionItemID;
import curam.ca.gc.bdmoas.facade.productdelivery.deduction.struct.BDMOASNRTCountryDetails;
import curam.codetable.DEDUCTIONNAME;
import curam.core.facade.fact.ProductDeliveryFactory;
import curam.core.facade.struct.ReadDeductionAndHeaderDetails;
import curam.core.facade.struct.ReadDeductionKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.NotFoundIndicator;

public class BDMOASProductDeliveryDeduction implements
  curam.ca.gc.bdmoas.facade.productdelivery.deduction.intf.BDMOASProductDeliveryDeduction {

  /**
   * Method will read the Country details if the deduction is of type
   * NRT.
   *
   * @param key
   * Contains the Case Deduction Item ID.
   *
   * @return Country details
   *
   */
  @Override
  public BDMOASNRTCountryDetails readNRTDeductionCountryDetails(
    final ReadDeductionKey key) throws AppException, InformationalException {

    // BEGIN Feature 106244: Manage NRT
    // Check if the Deduction is of Type NRT Deduction, if yes populate the
    // country field.

    final ReadDeductionAndHeaderDetails readDeductionAndHeaderDetails =
      ProductDeliveryFactory.newInstance().readDeduction1(key);
    final BDMOASNRTCountryDetails bdmOASNRTCountryDetails =
      new BDMOASNRTCountryDetails();

    if (readDeductionAndHeaderDetails.readDeductionDetails.dtls.deductionName
      .equals(DEDUCTIONNAME.OAS_NRT)) {

      final BDMOASNRTCaseDeductionItemID bdmOASNRTCaseDeductionItemID =
        new BDMOASNRTCaseDeductionItemID();
      bdmOASNRTCaseDeductionItemID.caseDeductionItemID =
        readDeductionAndHeaderDetails.readDeductionDetails.caseDeductionItemID;

      final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
      final BDMOASDeductionDtls bdmOASCaseDeductionDtls =
        BDMOASDeductionFactory.newInstance().readCountryByCaseDeductionItemID(
          notFoundIndicator, bdmOASNRTCaseDeductionItemID);
      if (!notFoundIndicator.isNotFound()) {
        bdmOASNRTCountryDetails.bdmOASNRTCountry =
          bdmOASCaseDeductionDtls.nrtCountry;
        bdmOASNRTCountryDetails.bdmOASNRTCountryInd = true;
      }
    }

    // END Feature 106244: Manage NRT

    return bdmOASNRTCountryDetails;
  }

}
