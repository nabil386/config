package curam.ca.gc.bdmoas.deductions.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMDEDUCTIONTYPE;
import curam.ca.gc.bdm.entity.bdmcasedeductionitem.struct.BDMCaseDeductionItemKey;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityKey;
import curam.ca.gc.bdm.facade.integratedcase.fact.BDMIntegratedCaseFactory;
import curam.ca.gc.bdm.facade.integratedcase.struct.BDMCurrentDeductionsForProductDeliveryList;
import curam.ca.gc.bdm.facade.integratedcase.struct.BDMDeductionItemDetail;
import curam.ca.gc.bdm.sl.financial.maintainliabilitycase.struct.ConcernRoleIDKey;
import curam.ca.gc.bdm.sl.maintaincasedeductions.impl.MaintainCaseDeductions;
import curam.ca.gc.bdm.sl.maintainparticipantdeduction.impl.MaintainParticipantDeduction;
import curam.ca.gc.bdm.sl.struct.CreateCaseDeductionDetails;
import curam.ca.gc.bdm.vtw.deduction.entity.fact.BDMVTWDeductionFactory;
import curam.ca.gc.bdm.vtw.deduction.entity.struct.BDMVTWDeductionDtls;
import curam.ca.gc.bdm.vtw.deduction.entity.struct.BDMVTWDeductionKey;
import curam.ca.gc.bdmoas.entity.oasdeduction.fact.BDMOASDeductionFactory;
import curam.ca.gc.bdmoas.entity.oasdeduction.struct.BDMOASDeductionDetails;
import curam.ca.gc.bdmoas.entity.oasdeduction.struct.BDMOASDeductionDtls;
import curam.ca.gc.bdmoas.entity.oasdeduction.struct.BDMOASDeductionKey;
import curam.codetable.BUSINESSSTATUS;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.DEDUCTIONNAME;
import curam.codetable.PRODUCTCATEGORY;
import curam.codetable.RATECOLUMNTYPE;
import curam.codetable.RATEROWTYPE;
import curam.codetable.RATESTATUS;
import curam.codetable.RATETABLETYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.facade.fact.RateTableFactory;
import curam.core.facade.struct.ActiveCasesConcernRoleIDAndTypeKey;
import curam.core.facade.struct.ListICProductDeliveryDeductionKey;
import curam.core.facade.struct.ReadRateCellValue;
import curam.core.facade.struct.ReadRateRowKey;
import curam.core.facade.struct.ReadRateTableDetails;
import curam.core.facade.struct.ReadRateTableKey;
import curam.core.facade.struct.ReadRowDetails;
import curam.core.fact.CaseDeductionItemFCLinkFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.InstructionLineItemFactory;
import curam.core.impl.CuramConst;
import curam.core.sl.entity.fact.RateHeaderFactory;
import curam.core.sl.entity.fact.RateRowFactory;
import curam.core.sl.entity.intf.RateRow;
import curam.core.sl.entity.struct.RateHeaderByDateTypeIn;
import curam.core.sl.entity.struct.RateHeaderDtls;
import curam.core.sl.entity.struct.RateRowDtls;
import curam.core.sl.entity.struct.RateRowDtlsList;
import curam.core.sl.entity.struct.RateRowSearchByHeaderIDIn;
import curam.core.sl.struct.CaseHeaderDtlsList;
import curam.core.sl.struct.CaseIDKey;
import curam.core.struct.CaseDeductionItem;
import curam.core.struct.CaseDeductionItemDtls;
import curam.core.struct.CaseDeductionItemFCLinkDtls;
import curam.core.struct.CaseDeductionItemFCLinkDtlsList;
import curam.core.struct.CaseDeductionItemKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.Count;
import curam.core.struct.FinancialComponentID;
import curam.core.struct.IndicatorStruct;
import curam.dynamicevidence.util.impl.AdminCodetableUtil;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.type.Date;
import curam.util.type.Money;

public class BDMOASMaintainDeduction {

  @Inject
  MaintainParticipantDeduction maintainParticipantDeductionObj;

  /**
   * Constructor
   */
  public BDMOASMaintainDeduction() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * This method will record the VTW deductions.This will call
   * the third party deduction method to insert the deduction
   * information into the OOTB tables.
   *
   * @param key
   * Contains the BDMVTWDeduction ID.
   * @throws AppException
   * Application Exception.
   * @throws InformationalException
   * Informational Exception.
   */
  public void recordVTWDeductionByIC(final BDMVTWDeductionKey key)
    throws AppException, InformationalException {

    if (key.vtwDeductionID != CuramConst.gkZero) {

      final BDMVTWDeductionDtls bdmvtwDeductionDtls =
        BDMVTWDeductionFactory.newInstance().read(key);
      final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
      caseHeaderKey.caseID = bdmvtwDeductionDtls.caseID;

      final CaseHeaderDtls caseHeaderDtls =
        CaseHeaderFactory.newInstance().read(caseHeaderKey);

      // Check if the IC Case is of type OAS IC Case.
      if (PRODUCTCATEGORY.OAS_OLD_AGE_SECURITY
        .equals(caseHeaderDtls.integratedCaseType)) {

        // Read the Active Product Delivery Case
        final ActiveCasesConcernRoleIDAndTypeKey activeCasesConcernRoleIDAndTypeKey =
          new ActiveCasesConcernRoleIDAndTypeKey();
        activeCasesConcernRoleIDAndTypeKey.dtls.caseTypeCode =
          CASETYPECODE.PRODUCTDELIVERY;
        activeCasesConcernRoleIDAndTypeKey.dtls.concernRoleID =
          bdmvtwDeductionDtls.concernRoleID;
        activeCasesConcernRoleIDAndTypeKey.dtls.statusCode =
          CASESTATUS.ACTIVE;

        final CaseHeaderDtlsList caseHeadDtlsList =
          curam.core.facade.fact.CaseHeaderFactory.newInstance()
            .searchActiveCasesByTypeConcernRoleID(
              activeCasesConcernRoleIDAndTypeKey);

        // There should be only one ACTIVE OAS Benefit Case can exists.
        if (caseHeadDtlsList.dtlsList.dtls.size() > CuramConst.gkZero) {

          // Read the OAS Product Delivery Case ID.

          final CaseIDKey caseIDKey = new CaseIDKey();
          caseIDKey.caseID = caseHeadDtlsList.dtlsList.dtls.item(0).caseID;

          // Call the Record VTW Deduction by for OAS PDC.

          final MaintainCaseDeductions maintainCaseDeductions =
            new MaintainCaseDeductions();

          maintainCaseDeductions.recordVTWDeductionByPDC(caseIDKey, key);
        }
      }
    }
  }

  /**
   * This method will record the OAS Recovery Tax Deductions by
   * inserting the deductions details into the OOTB tables and
   * activates the created deduction.
   *
   * Once the record is inserted into the OOTB CaseDeductionItem
   * table, the caseDeductionItemID is linked in the
   * BDMOASDeduction table againt the evidence record which
   * created the deduction.
   *
   * @param bdmOASDeductionKey
   * Contains OAS Deduction Key value
   * @param bdmOASDeductionDetails
   * Contains the deduction details like start date, end date
   * rate and amount.
   *
   * @exception AppException
   * Application Exception
   *
   * @exception InformationalException
   * Informational Exception.
   *
   */
  public void recordOASRecoveryTaxDeductions(
    final BDMOASDeductionKey bdmOASDeductionKey,
    final BDMOASDeductionDetails bdmOASDeductionDetails)
    throws AppException, InformationalException {

    if (bdmOASDeductionKey.bdmOASDeductionID != CuramConst.gkZero) {

      final BDMOASDeductionDtls bdmOASDeductionDtls =
        BDMOASDeductionFactory.newInstance().read(bdmOASDeductionKey);

      final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
      caseHeaderKey.caseID = bdmOASDeductionDtls.caseID;

      final CaseHeaderDtls caseHeaderDtls =
        CaseHeaderFactory.newInstance().read(caseHeaderKey);

      // Check if the IC Case is of type OAS IC Case.
      if (PRODUCTCATEGORY.OAS_OLD_AGE_SECURITY
        .equals(caseHeaderDtls.integratedCaseType)) {

        // Read the Active Product Delivery Case
        final ActiveCasesConcernRoleIDAndTypeKey activeCasesConcernRoleIDAndTypeKey =
          new ActiveCasesConcernRoleIDAndTypeKey();
        activeCasesConcernRoleIDAndTypeKey.dtls.caseTypeCode =
          CASETYPECODE.PRODUCTDELIVERY;
        activeCasesConcernRoleIDAndTypeKey.dtls.concernRoleID =
          bdmOASDeductionDtls.concernRoleID;
        activeCasesConcernRoleIDAndTypeKey.dtls.statusCode =
          CASESTATUS.ACTIVE;

        final CaseHeaderDtlsList caseHeadDtlsList =
          curam.core.facade.fact.CaseHeaderFactory.newInstance()
            .searchActiveCasesByTypeConcernRoleID(
              activeCasesConcernRoleIDAndTypeKey);

        // There should be only one ACTIVE OAS Benefit Case can exists.
        if (caseHeadDtlsList.dtlsList.dtls.size() > CuramConst.gkZero) {

          // Read the OAS Product Delivery Case ID.

          final CaseIDKey caseIDKey = new CaseIDKey();
          caseIDKey.caseID = caseHeadDtlsList.dtlsList.dtls.item(0).caseID;

          final CreateCaseDeductionDetails createCaseDeductionDetails =
            new CreateCaseDeductionDetails();
          createCaseDeductionDetails.caseID = caseIDKey.caseID;
          createCaseDeductionDetails.deductionName =
            DEDUCTIONNAME.OAS_RCV_TAX;
          createCaseDeductionDetails.startDate =
            bdmOASDeductionDetails.startDate;
          createCaseDeductionDetails.endDate = bdmOASDeductionDetails.endDate;
          createCaseDeductionDetails.amount = bdmOASDeductionDetails.amount;
          createCaseDeductionDetails.rate = bdmOASDeductionDetails.rate;
          createCaseDeductionDetails.doNotAutoActivateInd = true;

          // Create OAS Recovery Tax Deductions
          final MaintainCaseDeductions maintainCaseDeductions =
            new MaintainCaseDeductions();
          final BDMCaseDeductionItemKey bdmCaseDeductionItemKeyNew =
            maintainCaseDeductions.createThirdPartyCaseDeduction(
              createCaseDeductionDetails, new BDMExternalLiabilityKey());

          final CaseDeductionItemKey caseDeductionItemKey =
            new CaseDeductionItemKey();

          caseDeductionItemKey.caseDeductionItemID =
            bdmCaseDeductionItemKeyNew.caseDeductionItemID;

          maintainCaseDeductions.activateDeductionItem(caseDeductionItemKey);

          // Update the BDM OAS Deduction Entity with the caseDeductionID.

          bdmOASDeductionDtls.caseDeductionItemID =
            bdmCaseDeductionItemKeyNew.caseDeductionItemID;
          BDMOASDeductionFactory.newInstance().modify(bdmOASDeductionKey,
            bdmOASDeductionDtls);

        }
      }
    }
  }

  /**
   * This method will record the OAS NRT Deductions by
   * inserting the deductions details into the OOTB tables and
   * activates the created deduction.
   *
   *
   * @param bdmOASDeductionKey
   * Contains OAS Deduction Key value
   * @param bdmOASDeductionDetails
   * Contains the deduction details like start date, end date
   * rate and amount.
   *
   * @exception AppException
   * Application Exception
   *
   * @exception InformationalException
   * Informational Exception.
   *
   */
  public void recordOASNRTDeductions(final Date startDate, final Date endDate,
    final CaseHeaderKey caseHeaderKey,
    final ConcernRoleIDKey concernRoleIDKey, final String country,
    final boolean isNRTCorrectionChangeInd,
    final double nrtCorrectionPercentage, final long nrtCorrectionEvidenceID)
    throws AppException, InformationalException {

    double percentage;

    if (isNRTCorrectionChangeInd) {

      percentage = nrtCorrectionPercentage;

    } else {

      if (nrtCorrectionPercentage != CuramConst.gkDoubleZero
        && nrtCorrectionPercentage < getNRTRateForCountry(startDate,
          country).dtls.rateCellValue) {

        percentage = nrtCorrectionPercentage;
      } else {

        percentage =
          getNRTRateForCountry(startDate, country).dtls.rateCellValue;
      }
    }

    // NRT deduction is not created when the percentage is zero.
    if (percentage == CuramConst.gkDoubleZero) {
      return;
    }

    final CreateCaseDeductionDetails createCaseDeductionDetails =
      new CreateCaseDeductionDetails();
    createCaseDeductionDetails.caseID = caseHeaderKey.caseID;
    createCaseDeductionDetails.deductionName = DEDUCTIONNAME.OAS_NRT;
    createCaseDeductionDetails.startDate = startDate;
    createCaseDeductionDetails.endDate = endDate;
    createCaseDeductionDetails.amount = Money.kZeroMoney;
    createCaseDeductionDetails.rate = percentage;
    createCaseDeductionDetails.doNotAutoActivateInd = true;

    // Create OAS NRT Deductions
    final MaintainCaseDeductions maintainCaseDeductions =
      new MaintainCaseDeductions();
    final BDMCaseDeductionItemKey bdmCaseDeductionItemKeyNew =
      maintainCaseDeductions.createThirdPartyCaseDeduction(
        createCaseDeductionDetails, new BDMExternalLiabilityKey());

    final CaseDeductionItemKey caseDeductionItemKey =
      new CaseDeductionItemKey();

    caseDeductionItemKey.caseDeductionItemID =
      bdmCaseDeductionItemKeyNew.caseDeductionItemID;

    maintainCaseDeductions.activateDeductionItem(caseDeductionItemKey);

    // Update the BDM OAS Deduction Entity with the caseDeductionID.

    final BDMOASDeductionDtls bdmOASDeductionDtls = new BDMOASDeductionDtls();
    bdmOASDeductionDtls.caseDeductionItemID =
      bdmCaseDeductionItemKeyNew.caseDeductionItemID;
    bdmOASDeductionDtls.caseID = caseHeaderKey.caseID;
    bdmOASDeductionDtls.concernRoleID = concernRoleIDKey.concernRoleID;
    bdmOASDeductionDtls.deductionType = BDMDEDUCTIONTYPE.OAS_NRT;
    bdmOASDeductionDtls.evidenceID = nrtCorrectionEvidenceID;
    bdmOASDeductionDtls.recordStatusCode = RECORDSTATUS.NORMAL;
    if (!StringUtil.isNullOrEmpty(country)) {
      bdmOASDeductionDtls.nrtCountry = country;
    }
    bdmOASDeductionDtls.caseDeductionItemID =
      bdmCaseDeductionItemKeyNew.caseDeductionItemID;
    BDMOASDeductionFactory.newInstance().insert(bdmOASDeductionDtls);
  }

  /**
   *
   * @return
   */
  public ReadRateCellValue getNRTRateForCountry(final Date startDate,
    final String country) throws AppException, InformationalException {

    // Read the Rate for the country by County Code
    final RateHeaderByDateTypeIn rateHeaderByDateTypeIn =
      new RateHeaderByDateTypeIn();
    rateHeaderByDateTypeIn.effectiveDate = startDate;
    rateHeaderByDateTypeIn.rateStatus = RATESTATUS.ACTIVE;
    rateHeaderByDateTypeIn.rateTableType = RATETABLETYPE.OAS_NRT_PERCENT;

    final RateHeaderDtls rateHeaderDtls = RateHeaderFactory.newInstance()
      .readNearestByEffectiveDateType(rateHeaderByDateTypeIn);

    final ReadRateTableKey readRateTableKey = new ReadRateTableKey();
    readRateTableKey.readRateTableKey.rateHeaderID =
      rateHeaderDtls.rateHeaderID;
    final ReadRateTableDetails readRateTableDetails =
      RateTableFactory.newInstance().readRateTable(readRateTableKey);

    final String rateTableRowValue =
      AdminCodetableUtil.getCodetableCode(RATEROWTYPE.TABLENAME, country);

    final ReadRateRowKey readRateRowKey = new ReadRateRowKey();

    final RateRow rateRowObj = RateRowFactory.newInstance();
    long rateRowID = 0;

    final RateRowSearchByHeaderIDIn headerID =
      new RateRowSearchByHeaderIDIn();

    headerID.rateHeaderID = rateHeaderDtls.rateHeaderID;
    final RateRowDtlsList rowDtlsList = rateRowObj.searchByHeaderID(headerID);

    for (final RateRowDtls rowDtls : rowDtlsList.dtls) {
      if (rowDtls.rateRowType.equals(rateTableRowValue)) {
        rateRowID = rowDtls.rateRowID;
        break;
      }
    }

    readRateRowKey.readRateRowKey.rateRowID = rateRowID;

    final ReadRowDetails readRowDetails =
      RateTableFactory.newInstance().readRateRow(readRateRowKey);

    // ReadValueByColumnAndRowIndexKey
    final curam.core.facade.struct.ReadValueByColumnAndRowIndexKey readValueByColumnAndRowIndexKey =
      new curam.core.facade.struct.ReadValueByColumnAndRowIndexKey();

    readValueByColumnAndRowIndexKey.dtls.dtls.effectiveDate =
      readRateTableDetails.readRateTableDetails.effectiveDate;
    readValueByColumnAndRowIndexKey.dtls.dtls.rateColumnType =
      RATECOLUMNTYPE.OAS_NRT_PERCENT;
    readValueByColumnAndRowIndexKey.dtls.dtls.rateRowIndex =
      readRowDetails.readRowDetails.rateRowIndex;
    readValueByColumnAndRowIndexKey.dtls.dtls.rateStatus = RATESTATUS.ACTIVE;
    readValueByColumnAndRowIndexKey.dtls.dtls.rateTableType =
      RATETABLETYPE.OAS_NRT_PERCENT;

    return RateTableFactory.newInstance()
      .readRateCellValue(readValueByColumnAndRowIndexKey);

  }

  /**
   * This method will return the NRT Deductions
   *
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public BDMCurrentDeductionsForProductDeliveryList
    getNRTOverlappingDeductions(final long caseID,
      final Date nrtCorrectionStartDate, final Date nrtCorrectionEndDate)
      throws AppException, InformationalException {

    // Read all the NRT Deductions for the case.

    final ListICProductDeliveryDeductionKey listICProductDeliveryDeductionKey =
      new ListICProductDeliveryDeductionKey();
    listICProductDeliveryDeductionKey.caseID = caseID;

    final BDMCurrentDeductionsForProductDeliveryList bdmCurrentDeductionsForProductDeliveryList =
      BDMIntegratedCaseFactory.newInstance()
        .listCurrentProductDeliveryDeductions(
          listICProductDeliveryDeductionKey);

    final BDMCurrentDeductionsForProductDeliveryList bdmNRTDeductionsForProductDeliveryList =
      new BDMCurrentDeductionsForProductDeliveryList();

    for (final BDMDeductionItemDetail bdmDeductionItemDetail : bdmCurrentDeductionsForProductDeliveryList.dtls
      .items()) {

      boolean overlapExists = false;

      if (DEDUCTIONNAME.OAS_NRT.equals(bdmDeductionItemDetail.deductionName)
        && bdmDeductionItemDetail.businessStatus
          .equals(BUSINESSSTATUS.ACTIVE)) {

        if (Date.kZeroDate.equals(bdmDeductionItemDetail.endDate)) {

          if (bdmDeductionItemDetail.startDate
            .compareTo(nrtCorrectionStartDate) <= 0) {

            overlapExists = true;
          }

        } else if (bdmDeductionItemDetail.startDate
          .compareTo(nrtCorrectionStartDate) <= 0
          && nrtCorrectionStartDate
            .compareTo(bdmDeductionItemDetail.endDate) <= 0) {

          overlapExists = true;

        }

        if (overlapExists) {

          bdmNRTDeductionsForProductDeliveryList.dtls
            .addRef(bdmDeductionItemDetail);
        }
      }
    }

    return bdmNRTDeductionsForProductDeliveryList;
  }

  /**
   *
   * @param bdmDeductionItemDetails
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public boolean isNRTDeductionApplied(
    final BDMDeductionItemDetail bdmDeductionItemDetails)
    throws AppException, InformationalException {

    final CaseDeductionItem caseDeductionItemID = new CaseDeductionItem();
    caseDeductionItemID.caseDeductionItemID =
      bdmDeductionItemDetails.caseDeductionItemID;
    final CaseDeductionItemFCLinkDtlsList caseDeductionItemFCLinkDtlsList =
      CaseDeductionItemFCLinkFactory.newInstance()
        .searchByCaseDeductionItem(caseDeductionItemID);

    for (final CaseDeductionItemFCLinkDtls caseDeductionItemFCLinkDtls : caseDeductionItemFCLinkDtlsList.dtls
      .items()) {

      final FinancialComponentID financialComponentID =
        new FinancialComponentID();
      financialComponentID.financialCompID =
        caseDeductionItemFCLinkDtls.financialComponentID;
      final Count count = InstructionLineItemFactory.newInstance()
        .countByFinancialCompID(financialComponentID);

      if (count.numberOfRecords > CuramConst.gkZero) {
        return true;
      }
    }

    return false;

  }

  /**
   *
   * @param isDeductionApplied
   * @param bdmDeductionItemDetails
   * @param startDate
   * @param endDate
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public void processExistingNRTDeduction(final boolean isDeductionApplied,
    final BDMDeductionItemDetail bdmDeductionItemDetail, final Date startDate,
    final Date endDate) throws AppException, InformationalException {

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

    } else {

      // new instance
      final curam.core.intf.CaseDeductionItem caseDeductionItemObj =
        curam.core.fact.CaseDeductionItemFactory.newInstance();
      // If the Deduction is applied then end date the existing deduction
      final CaseDeductionItemDtls caseDeductionItemDtls =
        caseDeductionItemObj.read(caseDeductionItemKey);
      // check for the business status if active then will in-activate it
      if (!BUSINESSSTATUS.INACTIVE
        .equalsIgnoreCase(caseDeductionItemDtls.businessStatus)) {
        // update End date
        caseDeductionItemDtls.endDate = startDate.addDays(-1)
          .compareTo(Date.getCurrentDate().addDays(-1)) > CuramConst.gkZero
            ? startDate.addDays(-1) : Date.getCurrentDate().addDays(-1);
        // calling the method to modify the case deduction item details
        caseDeductionItemObj.modify(caseDeductionItemKey,
          caseDeductionItemDtls);
      }
    }
  }
}
