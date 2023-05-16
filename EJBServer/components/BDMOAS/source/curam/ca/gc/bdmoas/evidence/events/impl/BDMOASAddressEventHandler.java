package curam.ca.gc.bdmoas.evidence.events.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.evidence.events.impl.BDMAbstractEvidenceEventHandler;
import curam.ca.gc.bdm.facade.integratedcase.struct.BDMCurrentDeductionsForProductDeliveryList;
import curam.ca.gc.bdm.facade.integratedcase.struct.BDMDeductionItemDetail;
import curam.ca.gc.bdm.facade.pdcperson.fact.BDMPDCPersonFactory;
import curam.ca.gc.bdm.sl.financial.maintainliabilitycase.struct.ConcernRoleIDKey;
import curam.ca.gc.bdm.sl.maintainparticipantdeduction.impl.MaintainParticipantDeduction;
import curam.ca.gc.bdm.util.impl.BDMAddressUtil;
import curam.ca.gc.bdmoas.codetable.BDMOASNRTCORRECTIONREASON;
import curam.ca.gc.bdmoas.deductions.impl.BDMOASMaintainDeduction;
import curam.ca.gc.bdmoas.entity.paymentschedule.fact.BDMOASAnnualPaymentScheduleFactory;
import curam.ca.gc.bdmoas.entity.paymentschedule.struct.BDMOASAnnualPaymentScheduleDtls;
import curam.ca.gc.bdmoas.entity.paymentschedule.struct.BDMOASAnnualPaymentScheduleMonthAndYearDetails;
import curam.codetable.ADDRESSELEMENTTYPE;
import curam.codetable.BUSINESSSTATUS;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASETYPECODE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.COUNTRY;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.codetable.PRODUCTTYPE;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.facade.fact.ProductDeliveryFactory;
import curam.core.facade.struct.CaseHeaderDetails;
import curam.core.facade.struct.CaseReference;
import curam.core.fact.AddressElementFactory;
import curam.core.impl.CuramConst;
import curam.core.intf.AddressElement;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor;
import curam.core.sl.infrastructure.entity.struct.CaseIDStatusAndEvidenceTypeKey;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKeyList;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.infrastructure.struct.EIEvidenceKeyList;
import curam.core.struct.AddressElementDtls;
import curam.core.struct.AddressElementDtlsList;
import curam.core.struct.AddressKey;
import curam.core.struct.CaseDeductionItemDtls;
import curam.core.struct.CaseDeductionItemKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseKey;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.IndicatorStruct;
import curam.core.struct.OtherAddressData;
import curam.core.struct.ProductDeliveryKey;
import curam.core.struct.ProductDeliveryTypeDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.pdc.facade.struct.PDCCaseDetails;
import curam.pdc.facade.struct.PDCCaseDetailsList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.type.Date;
import curam.util.type.NotFoundIndicator;
import java.util.Calendar;

public class BDMOASAddressEventHandler
  extends BDMAbstractEvidenceEventHandler {

  @Inject
  MaintainParticipantDeduction maintainParticipantDeductionObj;

  /**
   * Constructor
   */
  public BDMOASAddressEventHandler() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public CASEEVIDENCEEntry evidenceType() {

    return CASEEVIDENCEEntry.BDMADDRESS;
  }

  @Override
  public boolean subscribePostActivation() {

    return true;
  }

  /**
   *
   */
  @Override
  public void postActivation(
    final EvidenceControllerInterface evidenceControllerInterface,
    final CaseKey key, final EIEvidenceKeyList list)
    throws AppException, InformationalException {

    super.postActivation(evidenceControllerInterface, key, list);

    // create the NRT Deduction
    manageOASNRTDeductionDetails(evidenceControllerInterface, key, list);

  }

  /**
   * This method will manage the NRT deduction based on the address
   * evidence changes.
   *
   * @throws AppException
   * @throws InformationalException
   */
  public void manageOASNRTDeductionDetails(
    final EvidenceControllerInterface evidenceControllerInterface,
    final CaseKey key, final EIEvidenceKeyList list)
    throws AppException, InformationalException {

    // Get the OAS PDC Case

    final BDMOASMaintainDeduction bdmOASMaintainDeduction =
      new BDMOASMaintainDeduction();

    for (final EIEvidenceKey eiEvidenceKey : list.dtls) {

      // Check if the end date is added to the Residential Address Evidence
      final EvidenceControllerInterface evidenceControllerObj =
        (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

      final EIEvidenceReadDtls eiEvidenceReadDtls =
        evidenceControllerObj.readEvidence(eiEvidenceKey);

      final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
        (DynamicEvidenceDataDetails) eiEvidenceReadDtls.evidenceObject;

      // Get the concernRole ID
      final EvidenceDescriptorKey evidenceDescriptorKey =
        new EvidenceDescriptorKey();
      evidenceDescriptorKey.evidenceDescriptorID =
        eiEvidenceReadDtls.descriptor.evidenceDescriptorID;

      final EvidenceDescriptorDtls evidenceDiscriptorDtls =
        EvidenceDescriptorFactory.newInstance().read(evidenceDescriptorKey);

      // Check if the address type is residential

      final String addressType =
        dynamicEvidenceDataDetails.getAttribute("addressType").getValue();

      if (!CONCERNROLEADDRESSTYPE.PRIVATE.equals(addressType)) {

        return;
      }

      final String startDateValue =
        dynamicEvidenceDataDetails.getAttribute("fromDate").getValue();

      final String endDateValue =
        dynamicEvidenceDataDetails.getAttribute("toDate").getValue();

      final Date startDate = Date.getDate(startDateValue);

      final String addressData =
        dynamicEvidenceDataDetails.getAttribute("address").getValue();

      final AddressKey resAddressKey = new AddressKey();
      resAddressKey.addressID = Long.valueOf(addressData);

      final AddressKey addressKey = new AddressKey();
      addressKey.addressID = Long.valueOf(addressData);

      final AddressElement addressElementObj =
        AddressElementFactory.newInstance();

      final AddressElementDtlsList addressElementDtlsList =
        addressElementObj.readAddressElementDetails(addressKey);

      String addressCountry = new String();

      for (final AddressElementDtls addressElementDtls : addressElementDtlsList.dtls) {

        if (ADDRESSELEMENTTYPE.COUNTRY
          .equals(addressElementDtls.elementType)) {
          addressCountry = addressElementDtls.elementValue;
          break;
        }
      }

      final Date endDate = StringUtil.isNullOrEmpty(endDateValue)
        ? Date.kZeroDate : Date.getDate(endDateValue);

      final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
      concernRoleKey.concernRoleID = evidenceDiscriptorDtls.participantID;

      // Get the list of IC Cases

      final PDCCaseDetailsList pdcCaseDetailsList =
        BDMPDCPersonFactory.newInstance().listCurrentCases(concernRoleKey);

      final CaseKey caseKey = new CaseKey();

      for (final PDCCaseDetails pdcCaseDetails : pdcCaseDetailsList.dtlsList
        .items()) {

        final CaseReference caseReference = new CaseReference();
        caseReference.dtls.caseReference = pdcCaseDetails.reference;
        final CaseHeaderDetails caseHeaderDetails =
          curam.core.facade.fact.CaseHeaderFactory.newInstance()
            .readByCaseReference(caseReference);

        if (CASETYPECODE.PRODUCTDELIVERY
          .equals(caseHeaderDetails.dtls.caseTypeCode)) {

          caseKey.caseID = caseHeaderDetails.dtls.caseID;
          break;
        }
      }

      if (caseKey.caseID != CuramConst.gkZero) {

        final ProductDeliveryKey productDeliveryKey =
          new ProductDeliveryKey();
        productDeliveryKey.caseID = caseKey.caseID;

        final ProductDeliveryTypeDetails productDeliveryTypeDetails =
          ProductDeliveryFactory.newInstance()
            .readProductType(productDeliveryKey);

        if (PRODUCTTYPE.OAS_BENEFITS
          .equals(productDeliveryTypeDetails.productType)) {

          deleteOrEndDateNRTDeduction(bdmOASMaintainDeduction, caseKey,
            startDate, endDate, addressData);

          // Check if the NRT Deduction exists on the OAS PDC Case.
          // then check for the extending the end date of the OAS PDC Case.

          if (isOASNRTDeductionExtended(caseKey, evidenceDiscriptorDtls,
            startDate, addressCountry)) {

            extendNRTDeduction(bdmOASMaintainDeduction, caseKey, startDate,
              endDate, addressData, concernRoleKey);

          } else {

            endDateNRTDeduction(bdmOASMaintainDeduction, caseKey, startDate,
              endDate, addressData);

            // Create new NRT deduction;

            if (!isCountryCAOrUSOrGB(addressCountry)) {

              final int month =
                Date.getCurrentDate().getCalendar().get(Calendar.MONTH);
              final int year =
                Date.getCurrentDate().getCalendar().get(Calendar.YEAR);

              // Get last payment processing date

              final BDMOASAnnualPaymentScheduleMonthAndYearDetails annualPaymentScheduleMonthAndYearDetails =
                new BDMOASAnnualPaymentScheduleMonthAndYearDetails();
              annualPaymentScheduleMonthAndYearDetails.month =
                month + CuramConst.gkOne;
              annualPaymentScheduleMonthAndYearDetails.year = year;

              final NotFoundIndicator notFoundIndicator =
                new NotFoundIndicator();

              BDMOASAnnualPaymentScheduleDtls bdmOASAnnualPaymentScheduleDtls =
                BDMOASAnnualPaymentScheduleFactory.newInstance()
                  .readByMonthAndYear(notFoundIndicator,
                    annualPaymentScheduleMonthAndYearDetails);

              if (!notFoundIndicator.isNotFound()
                && bdmOASAnnualPaymentScheduleDtls.paymentIssueDate
                  .after(Date.getCurrentDate())) {

                if (month == 11) {
                  annualPaymentScheduleMonthAndYearDetails.month =
                    CuramConst.gkOne;
                  annualPaymentScheduleMonthAndYearDetails.year =
                    year - CuramConst.gkOne;

                } else {
                  annualPaymentScheduleMonthAndYearDetails.month = month;
                  annualPaymentScheduleMonthAndYearDetails.year = year;
                }

                bdmOASAnnualPaymentScheduleDtls =
                  BDMOASAnnualPaymentScheduleFactory.newInstance()
                    .readByMonthAndYear(notFoundIndicator,
                      annualPaymentScheduleMonthAndYearDetails);
              }

              if (bdmOASAnnualPaymentScheduleDtls != null
                && startDate
                  .before(bdmOASAnnualPaymentScheduleDtls.paymentIssueDate)
                && Date.kZeroDate != endDate && endDate
                  .before(bdmOASAnnualPaymentScheduleDtls.paymentIssueDate)) {

                return;
              }

              final Date deductionStartDate =
                Date.getCurrentDate().after(startDate) ? Date.getCurrentDate()
                  : startDate;

              // Read all the NRT Correction evidences.

              final ConcernRoleIDKey concernRoleIDKey =
                new ConcernRoleIDKey();
              concernRoleIDKey.concernRoleID =
                evidenceDiscriptorDtls.participantID;

              final Integer nrtPercentage = new Integer(0);

              final Date nrtCorrectionEvidenceEndDate =
                getNRTCorrectionEvidenceEndDate(productDeliveryKey,
                  concernRoleIDKey, deductionStartDate, nrtPercentage);

              Date deductionEndDate = new Date();

              if (!Date.kZeroDate.equals(endDate)
                && Date.kZeroDate.equals(nrtCorrectionEvidenceEndDate)) {

                deductionEndDate = endDate;

              } else if (Date.kZeroDate.equals(endDate)
                && Date.kZeroDate.equals(nrtCorrectionEvidenceEndDate)) {

                deductionEndDate = Date.kZeroDate;

              } else if (Date.kZeroDate.equals(endDate)
                && !Date.kZeroDate.equals(nrtCorrectionEvidenceEndDate)) {

                deductionEndDate = nrtCorrectionEvidenceEndDate;
              }

              final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
              caseHeaderKey.caseID = caseKey.caseID;

              bdmOASMaintainDeduction.recordOASNRTDeductions(
                deductionStartDate, deductionEndDate, caseHeaderKey,
                concernRoleIDKey, addressCountry, false, nrtPercentage, 0);
            }
          }
        }
      }
    }
  }

  /**
   *
   * @param caseKey
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public final boolean isOASNRTDeductionExtended(final CaseKey caseKey,
    final EvidenceDescriptorDtls evidenceDiscriptorDtls, final Date startDate,
    final String addressCountry) throws AppException, InformationalException {

    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = caseKey.caseID;
    final CaseHeaderDtls caseHeaderDtls =
      curam.core.fact.CaseHeaderFactory.newInstance().read(caseHeaderKey);

    // Get the Integrated Case ID.
    caseHeaderKey.caseID = caseHeaderDtls.integratedCaseID;

    // Read all the address evidences for the Person

    final CaseIDStatusAndEvidenceTypeKey caseIDStatusAndEvidenceTypeKey =
      new CaseIDStatusAndEvidenceTypeKey();

    caseIDStatusAndEvidenceTypeKey.caseID = evidenceDiscriptorDtls.caseID;
    caseIDStatusAndEvidenceTypeKey.evidenceType = CASEEVIDENCE.BDMADDRESS;
    caseIDStatusAndEvidenceTypeKey.statusCode =
      EVIDENCEDESCRIPTORSTATUS.ACTIVE;

    final EvidenceDescriptor evidenceDescriptorObj =
      EvidenceDescriptorFactory.newInstance();

    final RelatedIDAndEvidenceTypeKeyList relatedIDAndEvidenceTypeKeyList =
      evidenceDescriptorObj
        .searchByCaseIDTypeAndStatus(caseIDStatusAndEvidenceTypeKey);

    // Check if the end date is added to the Residential Address Evidence
    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
    eiEvidenceKey.evidenceType = CASEEVIDENCE.BDMADDRESS;

    for (final RelatedIDAndEvidenceTypeKey relatedIDAndEvidenceType : relatedIDAndEvidenceTypeKeyList.dtls
      .items()) {

      eiEvidenceKey.evidenceID = relatedIDAndEvidenceType.relatedID;

      if (evidenceDiscriptorDtls.relatedID == eiEvidenceKey.evidenceID) {
        continue;
      }

      final EIEvidenceReadDtls eiEvidenceReadDtls =
        evidenceControllerObj.readEvidence(eiEvidenceKey);

      final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
        (DynamicEvidenceDataDetails) eiEvidenceReadDtls.evidenceObject;

      // Get the concernRole ID from the evidence descriptor
      final EvidenceDescriptorKey evidenceDescriptorKey =
        new EvidenceDescriptorKey();

      evidenceDescriptorKey.evidenceDescriptorID =
        eiEvidenceReadDtls.descriptor.evidenceDescriptorID;

      final EvidenceDescriptorDtls currentEvidenceDiscriptorDtls =
        EvidenceDescriptorFactory.newInstance().read(evidenceDescriptorKey);

      final String addressType =
        dynamicEvidenceDataDetails.getAttribute("addressType").getValue();

      if (!CONCERNROLEADDRESSTYPE.PRIVATE.equals(addressType)
        || currentEvidenceDiscriptorDtls.participantID != evidenceDiscriptorDtls.participantID) {

        continue;
      }

      final String toDateValue =
        dynamicEvidenceDataDetails.getAttribute("toDate").getValue();

      final Date toDate = StringUtil.isNullOrEmpty(toDateValue)
        ? Date.kZeroDate : Date.getDate(toDateValue);

      final String addressData =
        dynamicEvidenceDataDetails.getAttribute("address").getValue();

      final AddressKey addressKey = new AddressKey();
      addressKey.addressID = Long.valueOf(addressData);

      final AddressElement addressElementObj =
        AddressElementFactory.newInstance();

      final AddressElementDtlsList addressElementDtlsList =
        addressElementObj.readAddressElementDetails(addressKey);

      String currentAddressCountry = new String();

      for (final AddressElementDtls addressElementDtls : addressElementDtlsList.dtls) {

        if (ADDRESSELEMENTTYPE.COUNTRY
          .equals(addressElementDtls.elementType)) {
          currentAddressCountry = addressElementDtls.elementValue;
          break;
        }
      }

      if (currentAddressCountry.equals(addressCountry)
        && startDate.addDays(-1).equals(toDate)) {

        return true;
      }
    }

    return false;

  }

  /**
   *
   * @throws AppException
   * @throws InformationalException
   */
  public void deleteOrEndDateNRTDeduction(
    final BDMOASMaintainDeduction bdmOASMaintainDeduction,
    final CaseKey caseKey, final Date startDate, final Date endDate,
    final String addressData) throws AppException, InformationalException {

    BDMCurrentDeductionsForProductDeliveryList bdmNRTDeductionsList =
      new BDMCurrentDeductionsForProductDeliveryList();

    bdmNRTDeductionsList = bdmOASMaintainDeduction
      .getNRTOverlappingDeductions(caseKey.caseID, startDate, endDate);

    // Check if the NRT Deduction to be Deleted or Modified.
    for (final BDMDeductionItemDetail bdmDeductionItemDetail : bdmNRTDeductionsList.dtls
      .items()) {

      final boolean isDeductionApplied =
        bdmOASMaintainDeduction.isNRTDeductionApplied(bdmDeductionItemDetail);

      final CaseDeductionItemKey caseDeductionItemKey =
        new CaseDeductionItemKey();

      caseDeductionItemKey.caseDeductionItemID =
        bdmDeductionItemDetail.caseDeductionItemID;

      // If the Deduction is not applied then delete the existing deduction.
      if (!isDeductionApplied) {

        final IndicatorStruct deletedUnusedInd = new IndicatorStruct();
        deletedUnusedInd.changeAllIndicator = true;
        this.maintainParticipantDeductionObj.deactivateSingleCaseDeduction(
          caseDeductionItemKey, deletedUnusedInd);

      }
    }
  }

  /**
   *
   * @param bdmOASMaintainDeduction
   * @param caseKey
   * @param startDate
   * @param endDate
   * @param addressData
   * @throws AppException
   * @throws InformationalException
   */
  public void endDateNRTDeduction(
    final BDMOASMaintainDeduction bdmOASMaintainDeduction,
    final CaseKey caseKey, final Date startDate, final Date endDate,
    final String addressData) throws AppException, InformationalException {

    BDMCurrentDeductionsForProductDeliveryList bdmNRTDeductionsList =
      new BDMCurrentDeductionsForProductDeliveryList();

    bdmNRTDeductionsList = bdmOASMaintainDeduction
      .getNRTOverlappingDeductions(caseKey.caseID, startDate, endDate);

    // Check if the NRT Deduction to be Deleted or Modified.
    for (final BDMDeductionItemDetail bdmDeductionItemDetail : bdmNRTDeductionsList.dtls
      .items()) {

      final boolean isDeductionApplied =
        bdmOASMaintainDeduction.isNRTDeductionApplied(bdmDeductionItemDetail);

      final CaseDeductionItemKey caseDeductionItemKey =
        new CaseDeductionItemKey();

      caseDeductionItemKey.caseDeductionItemID =
        bdmDeductionItemDetail.caseDeductionItemID;

      final curam.core.intf.CaseDeductionItem caseDeductionItemObj =
        curam.core.fact.CaseDeductionItemFactory.newInstance();
      // If the Deduction is applied then end date the existing deduction
      final CaseDeductionItemDtls caseDeductionItemDtls =
        caseDeductionItemObj.read(caseDeductionItemKey);

      final BDMAddressUtil addressUtil = new BDMAddressUtil();
      final OtherAddressData otherAddressData = new OtherAddressData();
      otherAddressData.addressData = addressData;

      final String countryCode = addressUtil
        .getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.COUNTRY);

      if (COUNTRY.CA.equals(countryCode)) {

        caseDeductionItemDtls.endDate = Date.getCurrentDate();

      } else {

        caseDeductionItemDtls.endDate = Date.kZeroDate != endDate
          && endDate.after(Date.getCurrentDate().addDays(-1)) ? endDate
            : Date.getCurrentDate().addDays(-1);

      }
      // check for the business status if active then will in-activate it
      if (!BUSINESSSTATUS.INACTIVE
        .equalsIgnoreCase(caseDeductionItemDtls.businessStatus)) {
        // calling the method to modify the case deduction item details
        caseDeductionItemObj.modify(caseDeductionItemKey,
          caseDeductionItemDtls);
      }
    }

  }

  /**
   *
   * @param bdmOASMaintainDeduction
   * @param caseKey
   * @param startDate
   * @param endDate
   * @param addressData
   * @throws AppException
   * @throws InformationalException
   */
  public void extendNRTDeduction(
    final BDMOASMaintainDeduction bdmOASMaintainDeduction,
    final CaseKey caseKey, final Date startDate, final Date endDate,
    final String addressData, final ConcernRoleKey concernRoleKey)
    throws AppException, InformationalException {

    BDMCurrentDeductionsForProductDeliveryList bdmNRTDeductionsList =
      new BDMCurrentDeductionsForProductDeliveryList();

    bdmNRTDeductionsList = bdmOASMaintainDeduction
      .getNRTOverlappingDeductions(caseKey.caseID, startDate, endDate);

    // Check if the NRT Deduction to be Deleted or Modified.
    for (final BDMDeductionItemDetail bdmDeductionItemDetail : bdmNRTDeductionsList.dtls
      .items()) {

      final CaseDeductionItemKey caseDeductionItemKey =
        new CaseDeductionItemKey();

      caseDeductionItemKey.caseDeductionItemID =
        bdmDeductionItemDetail.caseDeductionItemID;

      final curam.core.intf.CaseDeductionItem caseDeductionItemObj =
        curam.core.fact.CaseDeductionItemFactory.newInstance();
      // If the Deduction is applied then end date the existing deduction
      final CaseDeductionItemDtls caseDeductionItemDtls =
        caseDeductionItemObj.read(caseDeductionItemKey);

      final BDMAddressUtil addressUtil = new BDMAddressUtil();
      final OtherAddressData otherAddressData = new OtherAddressData();
      otherAddressData.addressData = addressData;

      final String countryCode = addressUtil
        .getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.COUNTRY);

      if (COUNTRY.CA.equals(countryCode)) {

        caseDeductionItemDtls.endDate = Date.getCurrentDate();

      } else {

        final ProductDeliveryKey productDeliveryKey =
          new ProductDeliveryKey();
        productDeliveryKey.caseID = caseKey.caseID;
        final ConcernRoleIDKey concernRoleIDKey = new ConcernRoleIDKey();
        concernRoleIDKey.concernRoleID = concernRoleKey.concernRoleID;

        caseDeductionItemDtls.endDate = getNRTCorrectionEvidenceEndDate(null,
          null, caseDeductionItemDtls.startDate, 0);

      }
      // check for the business status if active then will in-activate it
      if (!BUSINESSSTATUS.INACTIVE
        .equalsIgnoreCase(caseDeductionItemDtls.businessStatus)) {
        // calling the method to modify the case deduction item details
        caseDeductionItemObj.modify(caseDeductionItemKey,
          caseDeductionItemDtls);
      }
    }

  }

  /**
   * This method will check if the country matches with the
   * US, UK or GB.
   *
   * If matches then return true else return false.
   *
   * @param addressCountry
   * Contains the Address country code.
   * @return
   * <code>true</code> if the country is one of CA,US or GB else
   * <code>false</code>
   *
   */
  public boolean isCountryCAOrUSOrGB(final String addressCountry) {

    // Check if the country is Canada, United States, or Great Britain.
    if (!StringUtil.isNullOrEmpty(addressCountry)
      && (COUNTRY.CA.equals(addressCountry)
        || COUNTRY.US.equals(addressCountry)
        || COUNTRY.GB.equals(addressCountry))) {

      return true;
    }

    return false;
  }

  /**
   * This method will return the
   *
   * @param productDeliveryKey
   * Contains the Product Delivery Key.
   *
   * @param concernRoleIDKey
   * Contains the ConcernRoleID Key.
   *
   * @param deductionStartDate
   * Deduction Start Date.
   * @return
   *
   * @throws AppException
   * Application Exception.
   * @throws InformationalException
   * Informational Exception.
   */
  public Date getNRTCorrectionEvidenceEndDate(
    final ProductDeliveryKey productDeliveryKey,
    final ConcernRoleIDKey concernRoleIDKey, final Date deductionStartDate,
    Integer nrtPercentage) throws AppException, InformationalException {

    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = productDeliveryKey.caseID;
    final CaseHeaderDtls caseHeaderDtls =
      curam.core.fact.CaseHeaderFactory.newInstance().read(caseHeaderKey);

    // Get the Integrated Case ID.
    caseHeaderKey.caseID = caseHeaderDtls.integratedCaseID;

    final CaseIDStatusAndEvidenceTypeKey caseIDStatusAndEvidenceTypeKey =
      new CaseIDStatusAndEvidenceTypeKey();

    caseIDStatusAndEvidenceTypeKey.caseID = caseHeaderDtls.caseID;
    caseIDStatusAndEvidenceTypeKey.evidenceType =
      CASEEVIDENCE.OAS_NRT_CORRECTION;
    caseIDStatusAndEvidenceTypeKey.statusCode =
      EVIDENCEDESCRIPTORSTATUS.ACTIVE;

    final EvidenceDescriptor evidenceDescriptorObj =
      EvidenceDescriptorFactory.newInstance();

    final RelatedIDAndEvidenceTypeKeyList relatedIDAndEvidenceTypeKeyList =
      evidenceDescriptorObj
        .searchByCaseIDTypeAndStatus(caseIDStatusAndEvidenceTypeKey);

    // Check if the end date is added to the Residential Address Evidence
    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();

    Date endDate = new Date();

    for (final RelatedIDAndEvidenceTypeKey relatedIDAndEvidenceType : relatedIDAndEvidenceTypeKeyList.dtls
      .items()) {

      eiEvidenceKey.evidenceID = relatedIDAndEvidenceType.relatedID;

      final EIEvidenceReadDtls eiEvidenceReadDtls =
        evidenceControllerObj.readEvidence(eiEvidenceKey);

      final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
        (DynamicEvidenceDataDetails) eiEvidenceReadDtls.evidenceObject;

      // Get the concernRole ID from the evidence descriptor
      final EvidenceDescriptorKey evidenceDescriptorKey =
        new EvidenceDescriptorKey();
      evidenceDescriptorKey.evidenceDescriptorID =
        eiEvidenceReadDtls.descriptor.evidenceDescriptorID;

      final EvidenceDescriptorDtls evidenceDiscriptorDtls =
        EvidenceDescriptorFactory.newInstance().read(evidenceDescriptorKey);

      // Read the start date.
      final Date startDate = Date.getDate(
        dynamicEvidenceDataDetails.getAttribute("startDate").getValue());

      final String correctionReason = dynamicEvidenceDataDetails
        .getAttribute("correctionReason").getValue();

      if (startDate.before(deductionStartDate)
        || evidenceDiscriptorDtls.participantID != concernRoleIDKey.concernRoleID) {

        continue;
      }

      if (BDMOASNRTCORRECTIONREASON.OAS_NR5_TAX_REDUCTION
        .equals(correctionReason)
        || BDMOASNRTCORRECTIONREASON.OAS_RETROACTIVE_NRT_RECOVERY
          .equals(correctionReason)) {

        endDate = Date.getDate(
          dynamicEvidenceDataDetails.getAttribute("endDate").getValue());
        nrtPercentage = Integer.getInteger(
          dynamicEvidenceDataDetails.getAttribute("percentage").getValue());
      }
    }
    return endDate;
  }

}
