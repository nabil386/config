package curam.ca.gc.bdmoas.productdelivery.impl;

import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.sl.financial.maintainliabilitycase.struct.ConcernRoleIDKey;
import curam.ca.gc.bdmoas.deductions.impl.BDMOASMaintainDeduction;
import curam.codetable.ADDRESSELEMENTTYPE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.COUNTRYCODE;
import curam.codetable.PRODUCTTYPE;
import curam.core.facade.fact.ProductDeliveryFactory;
import curam.core.fact.AddressFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.impl.ProductDeliveryLifeCycleEvents;
import curam.core.intf.Address;
import curam.core.struct.AddressDtls;
import curam.core.struct.AddressKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.OtherAddressData;
import curam.core.struct.ProductDeliveryKey;
import curam.core.struct.ProductDeliveryTypeDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.pdc.impl.PDCConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.type.Date;
import java.util.List;

public class BDMOASProductDeliveryLifeCycleEvents
  extends ProductDeliveryLifeCycleEvents {

  /**
   * Constructor
   */
  public BDMOASProductDeliveryLifeCycleEvents() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * This method is override of the OOTB postProductDeliveryActivation of
   * ProductDeliveryLifeCycleEvents class.
   * Method is override to check if the case type is OAS Product Delivery
   * case and is activated, then create the NRT Deduction if the
   * primary of the product delivery case has the address of outside Canada for
   * the residential address.
   *
   * @param caseID Refers to the caseID of the Product Delivery Case.
   */
  @Override
  public void postProductDeliveryActivation(final Long caseID)
    throws AppException, InformationalException {

    // call the super product delivery activation.
    super.postProductDeliveryActivation(caseID);

    // Create the NRT Deduction on the OAS Product Delivery Case
    // Check if the product is OAS Product Delivery Case
    // If the case is OAS Product Delivery Case then create the NRT Deduction if
    // residential address is Foreign on or after the case creation date.

    final ProductDeliveryKey productDeliveryKey = new ProductDeliveryKey();
    productDeliveryKey.caseID = caseID;

    final ProductDeliveryTypeDetails productDeliveryTypeDetails =
      ProductDeliveryFactory.newInstance()
        .readProductType(productDeliveryKey);

    if (PRODUCTTYPE.OAS_BENEFITS
      .equals(productDeliveryTypeDetails.productType)) {

      // Call method to create the NRT deduction if the
      // client has the foreign residential address after the
      // case start date.
      recordOASNRTDeduction(caseID);
    }
  }

  /**
   * Check if the Foreign Residential Address Exists for the participant
   * on the case, on or after the PDC start date.
   * If exists then create the NRT deduction.
   *
   *
   *
   */
  public void recordOASNRTDeduction(final Long caseID)
    throws AppException, InformationalException {

    // Read the primary participant
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = caseID;

    final CaseHeaderDtls caseHeaderDtls =
      CaseHeaderFactory.newInstance().read(caseHeaderKey);

    // Get the Primary Participant of the Case.
    final ConcernRoleIDKey concernRoleIDKey = new ConcernRoleIDKey();
    concernRoleIDKey.concernRoleID = caseHeaderDtls.concernRoleID;

    // PDC Start Date
    final Date pdcStartDate = caseHeaderDtls.startDate;

    final BDMEvidenceUtil bdmEvidenceUtil = new BDMEvidenceUtil();

    final List<DynamicEvidenceDataDetails> addressEvdDtls =
      bdmEvidenceUtil.getEvdDtlsByConcernroleIDandEvidenceType(
        concernRoleIDKey.concernRoleID, PDCConst.PDCADDRESS);

    final AddressKey addressKey = new AddressKey();
    final Address addressObj = AddressFactory.newInstance();

    for (final DynamicEvidenceDataDetails addressDynamicEvidenceDataDetails : addressEvdDtls) {

      if (addressDynamicEvidenceDataDetails.getAttribute("addressType")
        .getValue().equals(CONCERNROLEADDRESSTYPE.PRIVATE)) {

        addressKey.addressID =
          Long.parseLong(addressDynamicEvidenceDataDetails
            .getAttribute("address").getValue());

        final AddressDtls addressDtls = addressObj.read(addressKey);

        final OtherAddressData otherAddressData = new OtherAddressData();
        otherAddressData.addressData = addressDtls.addressData;

        // get Country from the address.
        final String country = bdmEvidenceUtil
          .getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.COUNTRY);

        if (!country.equals(COUNTRYCODE.CACODE)) {

          Date startDate = new Date();

          // Check the NRT start and end date with Address From and To
          // Dates.
          if (Date
            .getDate(addressDynamicEvidenceDataDetails
              .getAttribute("fromDate").getValue())
            .after(pdcStartDate)
            || Date.getDate(addressDynamicEvidenceDataDetails
              .getAttribute("fromDate").getValue()).equals(pdcStartDate)) {

            startDate = Date.getDate(addressDynamicEvidenceDataDetails
              .getAttribute("fromDate").getValue());

          } else {

            // If PDC Start Date is after the Address From Date then
            // use the PDC Start Date.
            startDate = pdcStartDate;
          }

          // create the NRT deduction
          final BDMOASMaintainDeduction caseDeductions =
            new BDMOASMaintainDeduction();

          // Read the dynamic evidence details start date and end date.
          final String endDateValue = addressDynamicEvidenceDataDetails
            .getAttribute("toDate").getValue();

          Date endDate = new Date();

          if (!StringUtil.isNullOrEmpty(endDateValue)) {

            endDate = Date.getDate(endDateValue);
          }

          final boolean isNRTCorrectionChangeInd = false;
          final double nrtCorrectionPercentage = 0;
          final long nrtCorrectionEvidenceID = 0;
          caseDeductions.recordOASNRTDeductions(startDate, endDate,
            caseHeaderKey, concernRoleIDKey, country,
            isNRTCorrectionChangeInd, nrtCorrectionPercentage,
            nrtCorrectionEvidenceID);
        }
      }
    }
  }

}
