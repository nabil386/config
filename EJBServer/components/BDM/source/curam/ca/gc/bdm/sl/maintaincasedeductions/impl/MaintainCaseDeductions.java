package curam.ca.gc.bdm.sl.maintaincasedeductions.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMDEDUCTIONTYPE;
import curam.ca.gc.bdm.codetable.BDMVTWTYPE;
import curam.ca.gc.bdm.entity.bdmcasedeductionitem.fact.BDMCaseDeductionItemFactory;
import curam.ca.gc.bdm.entity.bdmcasedeductionitem.intf.BDMCaseDeductionItem;
import curam.ca.gc.bdm.entity.bdmcasedeductionitem.struct.BDMCaseDeductionItemDtls;
import curam.ca.gc.bdm.entity.bdmcasedeductionitem.struct.BDMCaseDeductionItemKey;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityKey;
import curam.ca.gc.bdm.entity.deduction.fact.BDMDeductionFactory;
import curam.ca.gc.bdm.entity.deduction.intf.BDMDeduction;
import curam.ca.gc.bdm.entity.deduction.struct.BDMDeductionDetails;
import curam.ca.gc.bdm.entity.deduction.struct.BDMDeductionKey;
import curam.ca.gc.bdm.entity.deduction.struct.GetDeductionNameByCaseKey;
import curam.ca.gc.bdm.entity.fact.BDMCaseHeaderDAFactory;
import curam.ca.gc.bdm.entity.intf.BDMCaseHeaderDA;
import curam.ca.gc.bdm.entity.struct.ConcernRoleIDCaseTypeKey;
import curam.ca.gc.bdm.entity.subclass.casedeductionitem.fact.BDMCaseDeductionItemDAFactory;
import curam.ca.gc.bdm.entity.subclass.casedeductionitem.intf.BDMCaseDeductionItemDA;
import curam.ca.gc.bdm.entity.subclass.casedeductionitem.struct.ReadValidVTWDeductionKey;
import curam.ca.gc.bdm.entity.subclass.casedeductionitem.struct.VTWSpecificCaseDedDetails;
import curam.ca.gc.bdm.entity.subclass.casedeductionitem.struct.VTWSpecificCaseDedDetailsList;
import curam.ca.gc.bdm.entity.subclass.casedeductionitem.struct.VTWSpecificCaseDedKey;
import curam.ca.gc.bdm.entity.subclass.productdelivery.fact.BDMProductDeliveryDAFactory;
import curam.ca.gc.bdm.entity.subclass.productdelivery.intf.BDMProductDeliveryDA;
import curam.ca.gc.bdm.entity.subclass.productdelivery.struct.SearchEligBenefitsForVTWKey;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.sl.maintainparticipantdeduction.intf.MaintainParticipantDeduction;
import curam.ca.gc.bdm.sl.productdelivery.impl.MaintainDeductionDetails;
import curam.ca.gc.bdm.sl.struct.BDMPostAddressChangeKey;
import curam.ca.gc.bdm.sl.struct.CreateCaseDeductionDetails;
import curam.ca.gc.bdm.vtw.deduction.entity.fact.BDMVTWDeductionFactory;
import curam.ca.gc.bdm.vtw.deduction.entity.intf.BDMVTWDeduction;
import curam.ca.gc.bdm.vtw.deduction.entity.struct.BDMVTWDeductionDtls;
import curam.ca.gc.bdm.vtw.deduction.entity.struct.BDMVTWDeductionDtlsList;
import curam.ca.gc.bdm.vtw.deduction.entity.struct.BDMVTWDeductionKey;
import curam.codetable.ADDRESSELEMENTTYPE;
import curam.codetable.BUSINESSSTATUS;
import curam.codetable.CASENOMINEESTATUS;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.DEDUCTIONCATEGORYCODE;
import curam.codetable.DEDUCTIONITEMTYPE;
import curam.codetable.DEDUCTIONNAME;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.codetable.PROVINCETYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.facade.fact.CaseFactory;
import curam.core.facade.fact.ProductDeliveryFactory;
import curam.core.facade.intf.ProductDelivery;
import curam.core.facade.struct.CreateDeductionDetails1;
import curam.core.facade.struct.DeductionActivationDetails;
import curam.core.facade.struct.DeductionItemKeyVersionNo;
import curam.core.facade.struct.ThirdPartyFixedDeductionDetails1;
import curam.core.facade.struct.ThirdPartyVariableDeductionDetails1;
import curam.core.fact.AddressElementFactory;
import curam.core.fact.CaseDeductionItemFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.CaseStatusFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.fact.ModifyCaseStatusForDelayedProcessingFactory;
import curam.core.fact.WMInstanceDataFactory;
import curam.core.impl.DP_const;
import curam.core.intf.AddressElement;
import curam.core.intf.CaseDeductionItem;
import curam.core.intf.CaseHeader;
import curam.core.intf.CaseStatus;
import curam.core.intf.ConcernRole;
import curam.core.intf.ModifyCaseStatusForDelayedProcessing;
import curam.core.intf.WMInstanceData;
import curam.core.sl.entity.fact.DeductionProductLinkFactory;
import curam.core.sl.entity.struct.DeductionDtls;
import curam.core.sl.entity.struct.DeductionNameStatus;
import curam.core.sl.entity.struct.DeductionProductLinkDtls;
import curam.core.sl.entity.struct.DeductionProductLinkDtlsList;
import curam.core.sl.fact.CaseNomineeFactory;
import curam.core.sl.fact.CaseStatusModeFactory;
import curam.core.sl.fact.DeductionFactory;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor;
import curam.core.sl.infrastructure.entity.struct.CaseIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtlsList;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.core.sl.infrastructure.entity.struct.ReadRelatedIDParticipantIDAndEvidenceTypeDetails;
import curam.core.sl.infrastructure.entity.struct.VersionNo;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.ClientActionConst;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.intf.CaseNominee;
import curam.core.sl.struct.CaseIDKey;
import curam.core.sl.struct.CaseNomineeAndStatusDetails;
import curam.core.sl.struct.CaseNomineeAndStatusDetailsList;
import curam.core.sl.struct.CaseNomineeCaseIDAndStatusKey;
import curam.core.sl.struct.DeductionKey;
import curam.core.sl.struct.DeductionName;
import curam.core.sl.struct.ReadDeductionDetails;
import curam.core.struct.AddressElementDtls;
import curam.core.struct.AddressElementDtlsList;
import curam.core.struct.AddressKey;
import curam.core.struct.CaseByConcernRoleIDStatusAndTypeKey;
import curam.core.struct.CaseDeductionItemDtls;
import curam.core.struct.CaseDeductionItemDtlsList;
import curam.core.struct.CaseDeductionItemKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderDtlsList;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseKey;
import curam.core.struct.CaseKeyList;
import curam.core.struct.CaseStartDate;
import curam.core.struct.CaseStatusCode;
import curam.core.struct.CaseStatusDtls;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.CurrentCaseStatusKey;
import curam.core.struct.DeductionItemCaseID;
import curam.core.struct.IndicatorStruct;
import curam.core.struct.IntegratedCaseKey;
import curam.core.struct.ProductDeliveryDtls;
import curam.core.struct.ProductDeliveryKey;
import curam.core.struct.ProductKey;
import curam.core.struct.ReadParticipantRoleIDDetails;
import curam.core.struct.SetStatusForDelayedProcessingDtls;
import curam.core.struct.SetStatusForDelayedProcessingKey;
import curam.core.struct.WMInstanceDataDtls;
import curam.creole.value.CodeTableItem;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.type.impl.CodeTableItemConverter;
import curam.dynamicevidence.type.impl.DateConverter;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.RecordNotFoundException;
import curam.util.fact.DeferredProcessingFactory;
import curam.util.intf.DeferredProcessing;
import curam.util.persistence.GuiceWrapper;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.Money;
import curam.util.type.NotFoundIndicator;

/**
 * @author jigar.shah
 *
 */
public class MaintainCaseDeductions implements
  curam.ca.gc.bdm.sl.maintaincasedeductions.intf.MaintainCaseDeductions {

  private static String ADDRESS_EVIDENCE_TYPE = "PDC0000261";

  @Inject
  MaintainDeductionDetails maintainDeductionDetailsObj;

  @Inject
  MaintainParticipantDeduction maintainParticipantDeductionObj;

  public MaintainCaseDeductions() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * This method will be used to record VTW Deduction By an Integrated Case
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void recordVTWDeductionByIC(final BDMVTWDeductionKey key)
    throws AppException, InformationalException {

    // Record VTW deductions when VTW evidence is activated.
    // This will ensure deductions are always created either when benefit case
    // is created after IC
    // evidence or IC evidence is recorded after benefit case.
    final BDMVTWDeduction vtwDeductionObj =
      BDMVTWDeductionFactory.newInstance();
    final BDMVTWDeductionDtls vtwDeductionDtls = vtwDeductionObj.read(key);

    final BDMProductDeliveryDA bdmProductDeliveryDA =
      BDMProductDeliveryDAFactory.newInstance();
    final SearchEligBenefitsForVTWKey searchEligBenefitsForVTWKey =
      new SearchEligBenefitsForVTWKey();
    searchEligBenefitsForVTWKey.caseID = vtwDeductionDtls.caseID;
    searchEligBenefitsForVTWKey.caseStatusClosed = CASESTATUS.CLOSED;
    // uncomment below code after testing.
    final CaseKeyList caseKeyList = bdmProductDeliveryDA
      .searchEligBenefitsForVTWByIC(searchEligBenefitsForVTWKey);
    final CaseIDKey keyPDC = new CaseIDKey();
    CaseKey caseKey = null;
    for (int i = 0; i < caseKeyList.dtls.size(); i++) {
      caseKey = caseKeyList.dtls.item(i);
      keyPDC.caseID = caseKey.caseID;
      this.recordVTWDeductionByPDC(keyPDC, key);
    }
  }

  /**
   * This method will be used to record VTW Deduction By Product Delivery Case
   *
   * @throws AppException
   * @throws InformationalException
   */
  public void recordVTWDeductionByPDC(final CaseIDKey caseIDKey,
    final BDMVTWDeductionKey key)
    throws AppException, InformationalException {

    final BDMCaseDeductionItemDA bdmCaseDeductionItemDA =
      BDMCaseDeductionItemDAFactory.newInstance();
    final VTWSpecificCaseDedKey vtwSpecificCaseDedKey =
      new VTWSpecificCaseDedKey();
    vtwSpecificCaseDedKey.caseID = caseIDKey.caseID;
    vtwSpecificCaseDedKey.vtwDeductionID = key.vtwDeductionID;
    final VTWSpecificCaseDedDetailsList vtwSpecificCaseDedDetailsList =
      bdmCaseDeductionItemDA
        .searchVTWSpecificCaseDeductions(vtwSpecificCaseDedKey);
    final IndicatorStruct deleteUnused = new IndicatorStruct();
    deleteUnused.changeAllIndicator = true;
    final CaseDeductionItemKey caseDeductionItemKey =
      new CaseDeductionItemKey();
    final BDMCaseDeductionItem bdmCaseDeductionItemObj =
      BDMCaseDeductionItemFactory.newInstance();
    final BDMCaseDeductionItemKey bdmCaseDeductionItemKey =
      new BDMCaseDeductionItemKey();
    for (final VTWSpecificCaseDedDetails details : vtwSpecificCaseDedDetailsList.dtls) {
      // Find active or future active case deduction item for this vtw and
      // mostly there should only be one record like this.
      // Look for active record or an in-active record with status recheck date
      // as not null.
      if (!details.statusRecheckDate.isZero()
        || BUSINESSSTATUS.ACTIVE.equals(details.businessStatus)) {
        // since it is complicated to adjust existing case deduction items with
        // many OOTB validations, lets deactivate or delete exisitng records.
        caseDeductionItemKey.caseDeductionItemID =
          details.caseDeductionItemID;
        maintainParticipantDeductionObj
          .deactivateSingleCaseDeduction(caseDeductionItemKey, deleteUnused);

        if (!details.statusRecheckDate.isZero()) {
          bdmCaseDeductionItemKey.caseDeductionItemID =
            details.caseDeductionItemID;
          final BDMCaseDeductionItemDtls bdmCaseDeductionItemDtls =
            bdmCaseDeductionItemObj.read(bdmCaseDeductionItemKey);
          bdmCaseDeductionItemDtls.statusRecheckDate = Date.kZeroDate;
          bdmCaseDeductionItemObj.modify(bdmCaseDeductionItemKey,
            bdmCaseDeductionItemDtls);
        }
      }
    }

    // Create new case deduction item for this VTW.
    final BDMVTWDeduction bdmVTWDeductionObj =
      BDMVTWDeductionFactory.newInstance();
    final BDMVTWDeductionDtls vtwDeductionDtls = bdmVTWDeductionObj.read(key);

    // Create deduction only if product supports this deduction type
    final BDMDeduction bdmDeductionObj = BDMDeductionFactory.newInstance();
    final GetDeductionNameByCaseKey getDeductionNameByCaseKey =
      new GetDeductionNameByCaseKey();
    getDeductionNameByCaseKey.caseID = caseIDKey.caseID;
    if (BDMVTWTYPE.FEDERAL.equals(vtwDeductionDtls.vtwType)) {
      getDeductionNameByCaseKey.deductionType =
        BDMDEDUCTIONTYPE.VOLUNTARY_TAX_WITHHOLD_FED;
    } else {
      getDeductionNameByCaseKey.deductionType =
        BDMDEDUCTIONTYPE.VOLUNTARY_TAX_WITHHOLD_Prov;
    }

    DeductionName deductionName = null;
    try {
      deductionName =
        bdmDeductionObj.readDeductionNameByCase(getDeductionNameByCaseKey);
    } catch (final RecordNotFoundException rnfex) {
      // If the product is not configured for this deduction type, skip the
      // processing.
    }

    if (deductionName != null
      && vtwDeductionDtls.recordStatusCode.equals(RECORDSTATUS.NORMAL)) {
      final CreateCaseDeductionDetails createCaseDeductionDetails =
        new CreateCaseDeductionDetails();
      createCaseDeductionDetails.caseID = caseIDKey.caseID;
      createCaseDeductionDetails.deductionName = deductionName.deductionName;
      // if (vtwDeductionDtls.startDate.before(Date.getCurrentDate())) {
      // createCaseDeductionDetails.startDate = Date.getCurrentDate();
      // } else {
      createCaseDeductionDetails.startDate = vtwDeductionDtls.startDate;
      // }
      createCaseDeductionDetails.endDate = vtwDeductionDtls.endDate;
      createCaseDeductionDetails.amount = vtwDeductionDtls.amount;
      createCaseDeductionDetails.rate = vtwDeductionDtls.rate;
      createCaseDeductionDetails.doNotAutoActivateInd = false;

      final BDMCaseDeductionItemKey bdmCaseDeductionItemKeyNew =
        this.createThirdPartyCaseDeduction(createCaseDeductionDetails,
          new BDMExternalLiabilityKey());
      caseDeductionItemKey.caseDeductionItemID =
        bdmCaseDeductionItemKeyNew.caseDeductionItemID;

      final CaseDeductionItemDtls caseDeductionItemDtls =
        CaseDeductionItemFactory.newInstance().read(caseDeductionItemKey);

      // Calculate next recheck date.
      Date statusRecheckDate = Date.kZeroDate;
      if (caseDeductionItemDtls.startDate.after(Date.getCurrentDate())) {
        statusRecheckDate = caseDeductionItemDtls.startDate;
      } else if (!vtwDeductionDtls.endDate.isZero()
        && !vtwDeductionDtls.endDate.before(Date.getCurrentDate())) {
        statusRecheckDate = vtwDeductionDtls.endDate.addDays(1);
      }

      final BDMCaseDeductionItemDtls bdmCaseDeductionItemDtls =
        bdmCaseDeductionItemObj.read(bdmCaseDeductionItemKeyNew);

      bdmCaseDeductionItemDtls.vtwDeductionID = key.vtwDeductionID;
      bdmCaseDeductionItemDtls.statusRecheckDate = statusRecheckDate;
      bdmCaseDeductionItemObj.modify(bdmCaseDeductionItemKeyNew,
        bdmCaseDeductionItemDtls);
    }
  }

  /**
   * This method will be used to create Third Party Case Deduction. If there is
   * an external liability linked to the third party deduction, update
   * bdmcasedeductionitem
   *
   * @param CreateCaseDeductionDetails
   * @throws AppException
   * @throws InformationalException
   * @author jigar.shah
   */
  public BDMCaseDeductionItemKey createThirdPartyCaseDeduction(
    final CreateCaseDeductionDetails details,
    final BDMExternalLiabilityKey externalLiabilityKey)
    throws AppException, InformationalException {

    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = details.caseID;
    final CaseHeaderDtls caseHeaderDtls = caseHeaderObj.read(caseHeaderKey);
    final BDMCaseDeductionItemKey returnBDMCDIKey =
      new BDMCaseDeductionItemKey();
    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    final curam.core.sl.entity.intf.Deduction deductionDA =
      curam.core.sl.entity.fact.DeductionFactory.newInstance();
    final DeductionNameStatus deductionNameStatus = new DeductionNameStatus();
    deductionNameStatus.deductionName = details.deductionName;
    deductionNameStatus.recordStatus = RECORDSTATUS.NORMAL;
    final DeductionDtls deductionDtls =
      deductionDA.readActiveDeductionByName(nfIndicator, deductionNameStatus);

    final CaseStartDate caseStartDate =
      caseHeaderObj.readStartDate(caseHeaderKey);
    Date startDate = details.startDate;
    if (startDate.before(caseStartDate.startDate)) {
      startDate = caseStartDate.startDate;
    }

    Date endDate = details.endDate;
    final BDMDeduction bdmDeductionObj = BDMDeductionFactory.newInstance();
    final BDMDeductionKey bdmDeductionKey = new BDMDeductionKey();
    bdmDeductionKey.deductionID = deductionDtls.deductionID;
    final BDMDeductionDetails bdmDeductionDetails =
      bdmDeductionObj.readByDeductionID(bdmDeductionKey);

    final ConcernRole concernRoleObj = ConcernRoleFactory.newInstance();
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID =
      bdmDeductionDetails.thirdPartyConcernRoleID;
    final ConcernRoleDtls concernRoleDtls =
      concernRoleObj.read(concernRoleKey);
    final String customerAccName = concernRoleDtls.concernRoleName;
    final String customerAccNumber = concernRoleDtls.primaryAlternateID;

    if (!Date.kZeroDate.equals(concernRoleDtls.endDate)
      && concernRoleDtls.endDate.before(endDate)) {
      endDate = concernRoleDtls.endDate;
    }

    final String category = DEDUCTIONCATEGORYCODE.THIRDPARTYDEDUCTION;
    final double rate = details.rate;
    final Money amount = details.amount;
    final String actionType = deductionDtls.actionType;
    String deductionType = "";
    if (!amount.isZero()) {
      deductionType = DEDUCTIONITEMTYPE.FIXED;
    } else {
      deductionType = DEDUCTIONITEMTYPE.VARIABLE;
    }
    final ProductDelivery productDeliveryObj =
      ProductDeliveryFactory.newInstance();

    final CaseDeductionItemDtls cdiDtls = new CaseDeductionItemDtls();

    final curam.core.facade.struct.CaseNomineeCaseIDAndStatusKey caseNomineeCaseIDAndStatusKey1 =
      new curam.core.facade.struct.CaseNomineeCaseIDAndStatusKey();
    caseNomineeCaseIDAndStatusKey1.caseNomineeCaseIDAndStatusKey.caseNomineeCaseIDAndStatusKey.caseID =
      details.caseID;
    caseNomineeCaseIDAndStatusKey1.caseNomineeCaseIDAndStatusKey.caseNomineeCaseIDAndStatusKey.nomineeStatus =
      CASENOMINEESTATUS.OPERATIONAL;

    final curam.core.facade.struct.CaseNomineeAndStatusDetails caseNomineeAndStatusDetails1 =
      CaseFactory.newInstance()
        .listOperationalNominee1(caseNomineeCaseIDAndStatusKey1);

    CaseNomineeAndStatusDetails caseNomineeAndStatusDetails = null;

    final BDMCaseDeductionItem bdmCDIObj =
      BDMCaseDeductionItemFactory.newInstance();

    for (int i =
      0; i < caseNomineeAndStatusDetails1.caseNomineeAndStatusDetailsList.caseNomineeAndStatusDetailsList
        .size(); i++) {
      caseNomineeAndStatusDetails =
        caseNomineeAndStatusDetails1.caseNomineeAndStatusDetailsList.caseNomineeAndStatusDetailsList
          .item(i);

      if (DEDUCTIONITEMTYPE.FIXED.equals(deductionType)) {

        if (caseNomineeAndStatusDetails.defaultNomInd) {
          final ThirdPartyFixedDeductionDetails1 fixedDeductionDetails1 =
            new ThirdPartyFixedDeductionDetails1();
          fixedDeductionDetails1.deductionDtls.startDate = startDate;
          fixedDeductionDetails1.deductionDtls.endDate = endDate;
          fixedDeductionDetails1.deductionDtls.rate = rate;
          fixedDeductionDetails1.deductionDtls.caseID = details.caseID;
          fixedDeductionDetails1.deductionDtls.amount = amount;
          fixedDeductionDetails1.deductionDtls.deductionName =
            details.deductionName;
          fixedDeductionDetails1.deductionDtls.actionType = actionType;
          fixedDeductionDetails1.deductionDtls.category = category;
          fixedDeductionDetails1.deductionDtls.deductionType = deductionType;
          fixedDeductionDetails1.nomineeDtls.caseNomineeID =
            caseNomineeAndStatusDetails.caseNomineeID;
          fixedDeductionDetails1.deductionDtls.nomineeID =
            caseNomineeAndStatusDetails.caseNomineeID;
          fixedDeductionDetails1.thirdPartyDtls.thirdPartyConcernRoleID =
            bdmDeductionDetails.thirdPartyConcernRoleID;
          fixedDeductionDetails1.thirdPartyDtls.customerAccName =
            customerAccName;
          fixedDeductionDetails1.thirdPartyDtls.customerAccNumber =
            customerAccNumber;
          cdiDtls.assign(fixedDeductionDetails1.deductionDtls);
          fixedDeductionDetails1.deductionDtls.priority =
            maintainDeductionDetailsObj.calculatePriority(cdiDtls,
              externalLiabilityKey);
          productDeliveryObj
            .createThirdPartyFixedDeduction1(fixedDeductionDetails1);

          if (!details.doNotAutoActivateInd) {
            final CaseDeductionItemKey caseDeductionItemKey =
              new CaseDeductionItemKey();
            caseDeductionItemKey.caseDeductionItemID =
              fixedDeductionDetails1.deductionDtls.caseDeductionItemIDOpt;

            final CaseDeductionItemDtls caseDeductionItemDtls =
              CaseDeductionItemFactory.newInstance()
                .read(caseDeductionItemKey);
            final DeductionItemKeyVersionNo deductionItemKeyVersionNo =
              new DeductionItemKeyVersionNo();
            deductionItemKeyVersionNo.caseDeductionItemID =
              caseDeductionItemDtls.caseDeductionItemID;
            deductionItemKeyVersionNo.versionNo =
              caseDeductionItemDtls.versionNo;
            allowMultipleCaseDeductionUpdates(true);
            productDeliveryObj.activateDeduction(deductionItemKeyVersionNo);
            allowMultipleCaseDeductionUpdates(false);
          }

          // if there is an external liability, link BDMCaseDeductionItem to it
          if (externalLiabilityKey.externalLiabilityID != 0) {
            final BDMCaseDeductionItemKey bdmCDIKey =
              new BDMCaseDeductionItemKey();
            bdmCDIKey.caseDeductionItemID =
              fixedDeductionDetails1.deductionDtls.caseDeductionItemIDOpt;
            final BDMCaseDeductionItemDtls bdmCDIDtls =
              bdmCDIObj.read(bdmCDIKey);

            bdmCDIDtls.externalLiabilityID =
              externalLiabilityKey.externalLiabilityID;
            bdmCDIObj.modify(bdmCDIKey, bdmCDIDtls);
          }
          returnBDMCDIKey.caseDeductionItemID =
            fixedDeductionDetails1.deductionDtls.caseDeductionItemIDOpt;
        }
      } else

      {
        final ThirdPartyVariableDeductionDetails1 createDeductionDetails1 =
          new ThirdPartyVariableDeductionDetails1();

        createDeductionDetails1.deductionDtls.startDate = startDate;
        createDeductionDetails1.deductionDtls.endDate = endDate;
        createDeductionDetails1.deductionDtls.rate = rate;
        createDeductionDetails1.deductionDtls.caseID = details.caseID;
        createDeductionDetails1.deductionDtls.amount = amount;
        createDeductionDetails1.deductionDtls.deductionName =
          details.deductionName;
        createDeductionDetails1.deductionDtls.actionType = actionType;
        createDeductionDetails1.deductionDtls.category = category;
        createDeductionDetails1.deductionDtls.deductionType = deductionType;
        createDeductionDetails1.deductionDtls.nomineeID =
          caseNomineeAndStatusDetails.caseNomineeID;
        createDeductionDetails1.thirdPartyDtls.thirdPartyConcernRoleID =
          bdmDeductionDetails.thirdPartyConcernRoleID;
        createDeductionDetails1.thirdPartyDtls.customerAccName =
          customerAccName;
        createDeductionDetails1.thirdPartyDtls.customerAccNumber =
          customerAccNumber;
        cdiDtls.assign(createDeductionDetails1.deductionDtls);
        createDeductionDetails1.deductionDtls.priority =
          maintainDeductionDetailsObj.calculatePriority(cdiDtls,
            externalLiabilityKey);
        productDeliveryObj
          .createThirdPartyVariableDeduction1(createDeductionDetails1);
        if (!details.doNotAutoActivateInd) {
          final CaseDeductionItemKey caseDeductionItemKey =
            new CaseDeductionItemKey();
          caseDeductionItemKey.caseDeductionItemID =
            createDeductionDetails1.deductionDtls.caseDeductionItemIDOpt;

          final CaseDeductionItemDtls caseDeductionItemDtls =
            CaseDeductionItemFactory.newInstance().read(caseDeductionItemKey);
          final DeductionItemKeyVersionNo deductionItemKeyVersionNo =
            new DeductionItemKeyVersionNo();
          deductionItemKeyVersionNo.caseDeductionItemID =
            caseDeductionItemDtls.caseDeductionItemID;
          deductionItemKeyVersionNo.versionNo =
            caseDeductionItemDtls.versionNo;
          allowMultipleCaseDeductionUpdates(true);
          productDeliveryObj.activateDeduction(deductionItemKeyVersionNo);
          allowMultipleCaseDeductionUpdates(false);
        }

        // if there is an external liability, link BDMCaseDeductionItem to it
        if (externalLiabilityKey.externalLiabilityID != 0) {

          final BDMCaseDeductionItemKey bdmCDIKey =
            new BDMCaseDeductionItemKey();
          bdmCDIKey.caseDeductionItemID =
            createDeductionDetails1.deductionDtls.caseDeductionItemIDOpt;
          final BDMCaseDeductionItemDtls bdmCDIDtls =
            bdmCDIObj.read(bdmCDIKey);

          bdmCDIDtls.externalLiabilityID =
            externalLiabilityKey.externalLiabilityID;
          bdmCDIObj.modify(bdmCDIKey, bdmCDIDtls);
        }
        returnBDMCDIKey.caseDeductionItemID =
          createDeductionDetails1.deductionDtls.caseDeductionItemIDOpt;
      }
    }

    return returnBDMCDIKey;
  }

  /**
   * This method will be used to sync the VTW Deduction for Benefit Case.
   *
   * @param CaseHeaderKey
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void syncBenefitCaseVTWDeductions(final CaseHeaderKey key)
    throws AppException, InformationalException {

    // Clear existing VTW case deduction items that were left out when case is
    // closed.
    final BDMCaseDeductionItemDA bdmCaseDeductionItemDA =
      BDMCaseDeductionItemDAFactory.newInstance();
    final BDMCaseDeductionItem bdmCaseDeductionItemObj =
      BDMCaseDeductionItemFactory.newInstance();
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = key.caseID;
    final BDMCaseDeductionItemKey bdmCaseDeductionItemKey =
      new BDMCaseDeductionItemKey();
    final VTWSpecificCaseDedDetailsList vtwSpecificCaseDedDetailsList =
      bdmCaseDeductionItemDA.searchVTWTypeCaseDeductions(caseKey);
    final IndicatorStruct deleteUnused = new IndicatorStruct();
    deleteUnused.changeAllIndicator = true;
    final CaseDeductionItemKey caseDeductionItemKey =
      new CaseDeductionItemKey();
    BDMCaseDeductionItemDtls bdmCaseDeductionItemDtls = null;
    for (final VTWSpecificCaseDedDetails details : vtwSpecificCaseDedDetailsList.dtls) {
      if (!details.statusRecheckDate.isZero()
        || BUSINESSSTATUS.ACTIVE.equals(details.businessStatus)) {
        caseDeductionItemKey.caseDeductionItemID =
          details.caseDeductionItemID;
        maintainParticipantDeductionObj
          .deactivateSingleCaseDeduction(caseDeductionItemKey, deleteUnused);
        if (!details.statusRecheckDate.isZero()) {
          // Remove next recheck date.
          bdmCaseDeductionItemKey.caseDeductionItemID =
            details.caseDeductionItemID;
          bdmCaseDeductionItemDtls =
            bdmCaseDeductionItemObj.read(bdmCaseDeductionItemKey);
          bdmCaseDeductionItemDtls.statusRecheckDate = Date.kZeroDate;
          bdmCaseDeductionItemObj.modify(bdmCaseDeductionItemKey,
            bdmCaseDeductionItemDtls);
        }
      }
    }

    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final IntegratedCaseKey integratedCaseKey =
      caseHeaderObj.readIntegratedCaseIDByCaseID(caseKey);
    final BDMVTWDeduction bdmVTWDeductionObj =
      BDMVTWDeductionFactory.newInstance();
    caseKey.caseID = integratedCaseKey.integratedCaseID;
    final BDMVTWDeductionDtlsList bdmVTWDeductionDtlsList =
      bdmVTWDeductionObj.readActiveVTWRecordsByIC(caseKey);
    final CaseIDKey caseIDKey = new CaseIDKey();
    final BDMVTWDeductionKey bdmVTWDeductionKey = new BDMVTWDeductionKey();
    for (final BDMVTWDeductionDtls vtwDeductionDtls : bdmVTWDeductionDtlsList.dtls) {
      caseIDKey.caseID = key.caseID;
      bdmVTWDeductionKey.vtwDeductionID = vtwDeductionDtls.vtwDeductionID;
      this.recordVTWDeductionByPDC(caseIDKey, bdmVTWDeductionKey);
    }
  }

  private DynamicEvidenceDataDetails
    readDynamicEvidenceDataDetails(final EIEvidenceKey evidenceKey)
      throws AppException, InformationalException {

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final EIEvidenceReadDtls eiEvidenceReadDtls =
      evidenceControllerObj.readEvidence(evidenceKey);
    return (DynamicEvidenceDataDetails) eiEvidenceReadDtls.evidenceObject;
  }

  /**
   * This method will create Tax Deductions.
   */
  @Override
  public void createTaxDeductions(final CaseIDKey key)
    throws AppException, InformationalException {

    // read product delivery to get the product ID
    final ProductDeliveryKey productDeliveryKey = new ProductDeliveryKey();
    // populate the PDC case ID
    productDeliveryKey.caseID = key.caseID;
    // calling the method to read the Product delivery record
    final ProductDeliveryDtls productDeliveryDtls =
      curam.core.fact.ProductDeliveryFactory.newInstance()
        .read(productDeliveryKey);

    // check deduction product link record
    // new instance of product key
    final ProductKey productKey = new ProductKey();
    // populate the product ID
    productKey.productID = productDeliveryDtls.productID;
    // calling the method to search deductions by product ID
    final DeductionProductLinkDtlsList deductionProductLinkDtlsList =
      DeductionProductLinkFactory.newInstance().searchByProductID(productKey);

    // entity instance
    final BDMDeduction bdmDeductionObj = BDMDeductionFactory.newInstance();
    // new reference
    BDMDeductionKey bdmDeductionKey;
    // boolean indicators to check if deductions configured
    boolean basicFederalTaxDeductionConfiguredForProduct = Boolean.FALSE;
    boolean basicProvincialTaxDeductionConfiguredForProduct = Boolean.FALSE;
    // iterate the loop
    for (final DeductionProductLinkDtls deductionProductLinkDtls : deductionProductLinkDtlsList.dtls) {
      // new instance of input key
      bdmDeductionKey = new BDMDeductionKey();
      // populate the deduction ID
      bdmDeductionKey.deductionID = deductionProductLinkDtls.deductionID;
      // calling the method to read the BDM Deduction record
      final BDMDeductionDetails bdmDeductionDetails =
        bdmDeductionObj.readByDeductionID(bdmDeductionKey);

      // check for deduction type
      if (!basicFederalTaxDeductionConfiguredForProduct
        && BDMDEDUCTIONTYPE.BASIC_FEDERAL_TAX
          .equalsIgnoreCase(bdmDeductionDetails.deductionType)) {
        // set the boolean indicator to true
        basicFederalTaxDeductionConfiguredForProduct = Boolean.TRUE;
      }

      if (!basicProvincialTaxDeductionConfiguredForProduct
        && BDMDEDUCTIONTYPE.BASIC_PROVINCIAL_TAX
          .equalsIgnoreCase(bdmDeductionDetails.deductionType)) {
        // set the boolean indicator to true
        basicProvincialTaxDeductionConfiguredForProduct = Boolean.TRUE;
      }
    }

    // read the case start date
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    // new instance of key
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    // populate the case ID
    caseHeaderKey.caseID = key.caseID;
    // calling the method to read start date
    final CaseStartDate caseStartDate =
      caseHeaderObj.readStartDate(caseHeaderKey);

    // boolean indicator to indicate if third party tax deduction is created
    boolean thirdPartyTaxDeductionCreated = Boolean.FALSE;

    // check for the boolean indicator for federal and provincial tax
    if (basicFederalTaxDeductionConfiguredForProduct
      || basicProvincialTaxDeductionConfiguredForProduct) {
      // check if any active record available in CaseDeductionItem table
      final DeductionItemCaseID deductionItemCaseID =
        new DeductionItemCaseID();
      // populate the case ID
      deductionItemCaseID.caseID = key.caseID;
      // calling the method to read the case deduction items
      final CaseDeductionItemDtlsList caseDeductionItemDtlsList =
        CaseDeductionItemFactory.newInstance()
          .searchByCaseID(deductionItemCaseID);

      // boolean indicator to indicate if active record exists
      boolean activeBFTCaseDeductionRecordExists = Boolean.FALSE;
      boolean activeBPTCaseDeductionRecordExists = Boolean.FALSE;
      // iterate the list
      for (final CaseDeductionItemDtls caseDeductionItemDtls : caseDeductionItemDtlsList.dtls) {
        // check for active record
        if (RECORDSTATUS.NORMAL
          .equalsIgnoreCase(caseDeductionItemDtls.statusCode)) {
          // check if record exists for federal tax
          if (!activeBFTCaseDeductionRecordExists
            && BDMDEDUCTIONTYPE.BASIC_FEDERAL_TAX
              .equalsIgnoreCase(caseDeductionItemDtls.deductionType)) {
            // set the boolean indicator to true
            activeBFTCaseDeductionRecordExists = Boolean.TRUE;
          }

          if (!activeBPTCaseDeductionRecordExists
            && BDMDEDUCTIONTYPE.BASIC_PROVINCIAL_TAX
              .equalsIgnoreCase(caseDeductionItemDtls.deductionType)) {
            // set the boolean indicator to true
            activeBPTCaseDeductionRecordExists = Boolean.TRUE;
            // break the loop
            break;
          }
        }
      }

      // check for the boolean indicator to create TPD for Federal Tax
      if (basicFederalTaxDeductionConfiguredForProduct
        && !activeBFTCaseDeductionRecordExists) {
        // create new record
        final CreateCaseDeductionDetails createCaseDeductionDetails =
          new CreateCaseDeductionDetails();
        // populate the details
        createCaseDeductionDetails.deductionName = DEDUCTIONNAME.FEDERAL_TAX;
        createCaseDeductionDetails.caseID = key.caseID;
        createCaseDeductionDetails.startDate = caseStartDate.startDate;
        createCaseDeductionDetails.rate = BDMConstants.kDummyDeductionRate;
        // calling the method to create third party case deduction
        createThirdPartyCaseDeduction(createCaseDeductionDetails,
          new BDMExternalLiabilityKey());

        // set the boolean indicator to true
        thirdPartyTaxDeductionCreated = Boolean.TRUE;
      }

      // check for the boolean indicator to create TPD for Provincial Tax
      if (basicProvincialTaxDeductionConfiguredForProduct
        && !activeBPTCaseDeductionRecordExists) {
        // create new record
        final CreateCaseDeductionDetails createCaseDeductionDetails =
          new CreateCaseDeductionDetails();
        // populate the details
        createCaseDeductionDetails.deductionName =
          DEDUCTIONNAME.PROVINCIAL_TAX;
        createCaseDeductionDetails.caseID = key.caseID;
        createCaseDeductionDetails.startDate = caseStartDate.startDate;
        createCaseDeductionDetails.rate = BDMConstants.kDummyDeductionRate;
        // calling the method to create third party case deduction
        createThirdPartyCaseDeduction(createCaseDeductionDetails,
          new BDMExternalLiabilityKey());

        // set the boolean indicator to true
        thirdPartyTaxDeductionCreated = Boolean.TRUE;
      }
    }

    // check for the boolean indicator if deduction is created
    if (thirdPartyTaxDeductionCreated) {
      // calling the method to update tax deduction status
      updateTaxDeductionStatus(key);
    }
  }

  @Override
  public void
    syncCaseDeductionsOnAddressChange(final BDMPostAddressChangeKey key)
      throws AppException, InformationalException {

    final EvidenceDescriptor evidenceDescriptorObj =
      EvidenceDescriptorFactory.newInstance();
    final EvidenceDescriptorKey evidenceDescriptorKey =
      new EvidenceDescriptorKey();
    evidenceDescriptorKey.evidenceDescriptorID = key.evidenceDescriptorID;
    final ReadRelatedIDParticipantIDAndEvidenceTypeDetails readRelatedIDParticipantIDAndEvidenceTypeDetails =
      evidenceDescriptorObj
        .readRelatedIDParticipantIDAndEvidenceType(evidenceDescriptorKey);

    final EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
    eiEvidenceKey.evidenceID =
      readRelatedIDParticipantIDAndEvidenceTypeDetails.relatedID;
    eiEvidenceKey.evidenceType =
      readRelatedIDParticipantIDAndEvidenceTypeDetails.evidenceType;
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      readDynamicEvidenceDataDetails(eiEvidenceKey);

    final String addressIDStr =
      dynamicEvidenceDataDetails.getAttribute("address").getValue();
    final String addressType =
      dynamicEvidenceDataDetails.getAttribute("addressType").getValue();

    final long addressID = Long.parseLong(addressIDStr);

    if (CONCERNROLEADDRESSTYPE.PRIVATE.equals(addressType)) {
      final boolean isQuebecAddress = isQuebecAddress(addressID);

      if (isQuebecAddress) {
        final BDMCaseHeaderDA bdmCaseHeaderDA =
          BDMCaseHeaderDAFactory.newInstance();
        final ConcernRoleIDCaseTypeKey concernRoleIDCaseTypeKey =
          new ConcernRoleIDCaseTypeKey();
        concernRoleIDCaseTypeKey.concernRoleID = key.concernRoleID;
        concernRoleIDCaseTypeKey.caseTypeCode = CASETYPECODE.PRODUCTDELIVERY;
        final CaseHeaderDtlsList caseHeaderDtlsList = bdmCaseHeaderDA
          .searchByBDMConcernRoleIDType(concernRoleIDCaseTypeKey);
        final CaseIDKey caseIDKey = new CaseIDKey();
        for (final CaseHeaderDtls caseHeaderDtls : caseHeaderDtlsList.dtls) {
          caseIDKey.caseID = caseHeaderDtls.caseID;
          updateTaxDeductionStatus(caseIDKey);
        }
      }
    }
  }

  @Override
  public void updateTaxDeductionStatus(final CaseIDKey key)
    throws AppException, InformationalException {

    // Activate/Deactivate the provincial tax deduction and also set the
    // next status recheck date.
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = key.caseID;
    final CaseHeaderDtls caseHeaderDtls =
      CaseHeaderFactory.newInstance().read(caseHeaderKey);
    Date closestRecheckDate = Date.kZeroDate;
    if (!CASESTATUS.CLOSED.equals(caseHeaderDtls.statusCode)) {
      final long participantDataCaseID =
        this.getParticipantDataCaseID(caseHeaderDtls.concernRoleID);
      final String addressEvidenceType = ADDRESS_EVIDENCE_TYPE;

      final EvidenceDescriptor evidenceDescriptorObj =
        EvidenceDescriptorFactory.newInstance();
      final CaseIDAndEvidenceTypeKey caseIDAndEvidenceTypeKey =
        new CaseIDAndEvidenceTypeKey();
      caseIDAndEvidenceTypeKey.caseID = participantDataCaseID;
      caseIDAndEvidenceTypeKey.evidenceType = addressEvidenceType;

      final EvidenceDescriptorDtlsList evidenceDescriptorDtlsList =
        evidenceDescriptorObj
          .readByCaseIDAndEvidenceType(caseIDAndEvidenceTypeKey);

      boolean currentAddressQuebecInd = false;
      final EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
      DynamicEvidenceDataDetails dynamicEvidenceDataDetails = null;
      for (final EvidenceDescriptorDtls evidenceDescriptorDtls : evidenceDescriptorDtlsList.dtls) {
        if (EVIDENCEDESCRIPTORSTATUS.ACTIVE
          .equals(evidenceDescriptorDtls.statusCode)) {
          eiEvidenceKey.evidenceID = evidenceDescriptorDtls.relatedID;
          eiEvidenceKey.evidenceType = addressEvidenceType;
          dynamicEvidenceDataDetails =
            readDynamicEvidenceDataDetails(eiEvidenceKey);

          final String addressIDStr =
            dynamicEvidenceDataDetails.getAttribute("address").getValue();
          final String addressType =
            ((CodeTableItem) new CodeTableItemConverter().convert(
              dynamicEvidenceDataDetails.getAttribute("addressType"))).code();
          final Date fromDate = (Date) new DateConverter()
            .convert(dynamicEvidenceDataDetails.getAttribute("fromDate"));
          final Date toDate = (Date) new DateConverter()
            .convert(dynamicEvidenceDataDetails.getAttribute("toDate"));

          final long addressID = Long.parseLong(addressIDStr);

          if (CONCERNROLEADDRESSTYPE.PRIVATE.equals(addressType)) {
            final boolean isQuebecAddress = isQuebecAddress(addressID);

            if (isQuebecAddress
              && (toDate.isZero() || !toDate.before(Date.getCurrentDate()))) {
              if (!fromDate.after(Date.getCurrentDate())) {
                currentAddressQuebecInd = true;
              } else if (fromDate.after(Date.getCurrentDate())) {
                if (closestRecheckDate.isZero()
                  || closestRecheckDate.after(fromDate)) {
                  closestRecheckDate = fromDate;
                }
              }

              if (!toDate.before(Date.getCurrentDate())
                && !toDate.equals(Date.fromISO8601("99991231"))) {
                if (closestRecheckDate.isZero()
                  || closestRecheckDate.after(toDate.addDays(1))) {
                  closestRecheckDate = toDate.addDays(1);
                }
              }
            }

          }
        }
      }

      if (currentAddressQuebecInd) {
        this.activateProvTaxDeduction(key);
      } else {
        this.deactivateProvTaxDeduction(key);
      }
    }

    final BDMCaseDeductionItemDA bdmCaseDeductionItemDA =
      BDMCaseDeductionItemDAFactory.newInstance();
    final ReadValidVTWDeductionKey readValidVTWDeductionKey =
      new ReadValidVTWDeductionKey();
    readValidVTWDeductionKey.caseID = key.caseID;
    readValidVTWDeductionKey.deductionType =
      BDMDEDUCTIONTYPE.BASIC_PROVINCIAL_TAX;
    final CaseDeductionItemKey caseDeductionItemKey = bdmCaseDeductionItemDA
      .readValidDeductionByName(readValidVTWDeductionKey);

    final BDMCaseDeductionItem bdmCaseDeductionItemObj =
      BDMCaseDeductionItemFactory.newInstance();
    final BDMCaseDeductionItemKey bdmCaseDeductionItemKey =
      new BDMCaseDeductionItemKey();
    bdmCaseDeductionItemKey.caseDeductionItemID =
      caseDeductionItemKey.caseDeductionItemID;
    final BDMCaseDeductionItemDtls bdmCaseDeductionItemDtls =
      bdmCaseDeductionItemObj.read(bdmCaseDeductionItemKey);
    bdmCaseDeductionItemDtls.statusRecheckDate = closestRecheckDate;
    bdmCaseDeductionItemObj.modify(bdmCaseDeductionItemKey,
      bdmCaseDeductionItemDtls);
  }

  private boolean isQuebecAddress(final long addressID)
    throws AppException, InformationalException {

    final AddressElement addressElementObj =
      AddressElementFactory.newInstance();
    final AddressKey addressKey = new AddressKey();
    addressKey.addressID = addressID;
    final AddressElementDtlsList addressElementDtlsList =
      addressElementObj.readAddressElementDetails(addressKey);
    boolean isQuebecAddress = false;
    for (final AddressElementDtls addressElementDtls : addressElementDtlsList.dtls) {
      if (ADDRESSELEMENTTYPE.PROVINCE.equals(addressElementDtls.elementType)
        && PROVINCETYPE.QUEBEC.equals(addressElementDtls.elementValue)) {
        isQuebecAddress = true;
        break;
      }
    }
    return isQuebecAddress;
  }

  @Override
  public void activateProvTaxDeduction(final CaseIDKey key)
    throws AppException, InformationalException {

    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = key.caseID;
    final CaseStatusCode caseStatusCode =
      caseHeaderObj.readStatus(caseHeaderKey);

    final BDMCaseDeductionItemDA bdmCaseDeductionItemDA =
      BDMCaseDeductionItemDAFactory.newInstance();
    final ReadValidVTWDeductionKey readValidVTWDeductionKey =
      new ReadValidVTWDeductionKey();
    readValidVTWDeductionKey.caseID = key.caseID;
    readValidVTWDeductionKey.deductionType =
      BDMDEDUCTIONTYPE.BASIC_PROVINCIAL_TAX;
    final CaseDeductionItemKey caseDeductionItemKey = bdmCaseDeductionItemDA
      .readValidDeductionByName(readValidVTWDeductionKey);
    final CaseDeductionItemDtls caseDeductionItemDtls =
      CaseDeductionItemFactory.newInstance().read(caseDeductionItemKey);
    if (!BUSINESSSTATUS.ACTIVE.equals(caseDeductionItemDtls.statusCode)) {
      final DeductionItemKeyVersionNo deductionItemKeyVersionNo =
        new DeductionItemKeyVersionNo();
      deductionItemKeyVersionNo.caseDeductionItemID =
        caseDeductionItemDtls.caseDeductionItemID;
      deductionItemKeyVersionNo.versionNo = caseDeductionItemDtls.versionNo;
      allowMultipleCaseDeductionUpdates(true);
      ProductDeliveryFactory.newInstance()
        .activateDeduction(deductionItemKeyVersionNo);
      allowMultipleCaseDeductionUpdates(false);
    }

  }

  @Override
  public void deactivateProvTaxDeduction(final CaseIDKey key)
    throws AppException, InformationalException {

    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = key.caseID;
    final CaseStatusCode caseStatusCode =
      caseHeaderObj.readStatus(caseHeaderKey);

    final BDMCaseDeductionItemDA bdmCaseDeductionItemDA =
      BDMCaseDeductionItemDAFactory.newInstance();
    final ReadValidVTWDeductionKey readValidVTWDeductionKey =
      new ReadValidVTWDeductionKey();
    readValidVTWDeductionKey.caseID = key.caseID;
    readValidVTWDeductionKey.deductionType =
      BDMDEDUCTIONTYPE.BASIC_PROVINCIAL_TAX;
    final CaseDeductionItemKey caseDeductionItemKey = bdmCaseDeductionItemDA
      .readValidDeductionByName(readValidVTWDeductionKey);
    final CaseDeductionItemDtls caseDeductionItemDtls =
      CaseDeductionItemFactory.newInstance().read(caseDeductionItemKey);
    if (!BUSINESSSTATUS.INACTIVE
      .equals(caseDeductionItemDtls.businessStatus)) {
      final DeductionItemKeyVersionNo deductionItemKeyVersionNo =
        new DeductionItemKeyVersionNo();
      deductionItemKeyVersionNo.caseDeductionItemID =
        caseDeductionItemDtls.caseDeductionItemID;
      deductionItemKeyVersionNo.versionNo = caseDeductionItemDtls.versionNo;
      allowMultipleCaseDeductionUpdates(true);
      ProductDeliveryFactory.newInstance()
        .deactivateDeduction(deductionItemKeyVersionNo);
      allowMultipleCaseDeductionUpdates(false);
    }

  }

  /**
   * This method will return the PARTICIPANTDATACASE caseID based on
   * participantRoleID and case type.
   *
   * @param participantRoleID
   * @return caseID
   *
   * @throws AppException
   * - Generic Curam Exception
   * @throws InformationalException
   * - Generic Curam Exception
   */
  public long getParticipantDataCaseID(final long participantRoleID)
    throws AppException, InformationalException {

    final CaseByConcernRoleIDStatusAndTypeKey caseByConcernRoleIDStatusAndTypeKey =
      new CaseByConcernRoleIDStatusAndTypeKey();
    caseByConcernRoleIDStatusAndTypeKey.caseTypeCode =
      CASETYPECODE.PARTICIPANTDATACASE;
    caseByConcernRoleIDStatusAndTypeKey.concernRoleID = participantRoleID;
    caseByConcernRoleIDStatusAndTypeKey.statusCode = CASESTATUS.OPEN;

    final CaseHeaderDtlsList caseHeaderDtlsList =
      CaseHeaderFactory.newInstance().searchByConcernRoleIDStatusAndType(
        caseByConcernRoleIDStatusAndTypeKey);
    for (final CaseHeaderDtls caseHeaderDtls : caseHeaderDtlsList.dtls) {

      return caseHeaderDtls.caseID;
    }

    return 0l;
  }

  /**
   * Disables FC regeneration from checking if a case is in DPP status
   *
   * @throws AppException
   * @throws InformationalException
   */
  private void allowMultipleCaseDeductionUpdates(final boolean isEnable)
    throws AppException, InformationalException {

    CaseStatusModeFactory.newInstance()
      .setDisableRegenFCsCaseStatusDPPCheck(isEnable);

  }

  /**
   * This method will be used to create Applied Deduction for Benefit Case.
   *
   * @param CaseHeaderKey
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void createAppliedCaseDeduction(
    final CreateCaseDeductionDetails caseDeductionDtls,
    final CaseIDKey caseIDKey) throws AppException, InformationalException {

    final ProductDelivery productDeliveryObj =
      ProductDeliveryFactory.newInstance();
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = caseDeductionDtls.caseID;
    final CaseHeaderDtls caseHeaderDtls = caseHeaderObj.read(caseHeaderKey);
    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    final curam.core.sl.entity.intf.Deduction deductionDA =
      curam.core.sl.entity.fact.DeductionFactory.newInstance();
    final DeductionNameStatus deductionNameStatus = new DeductionNameStatus();
    deductionNameStatus.deductionName = caseDeductionDtls.deductionName;
    deductionNameStatus.recordStatus = RECORDSTATUS.NORMAL;
    final DeductionDtls deductionDtls =
      deductionDA.readActiveDeductionByName(nfIndicator, deductionNameStatus);

    final CaseStartDate caseStartDate =
      caseHeaderObj.readStartDate(caseHeaderKey);
    Date startDate = caseDeductionDtls.startDate;
    if (startDate.before(caseStartDate.startDate)) {
      startDate = caseStartDate.startDate;
    }

    final Date endDate = caseDeductionDtls.endDate;
    final long relatedCaseID = caseIDKey.caseID;
    final double rate = caseDeductionDtls.rate;
    final long caseID = caseDeductionDtls.caseID;
    final Money amount = caseDeductionDtls.amount;

    final BDMDeduction bdmDeductionObj = BDMDeductionFactory.newInstance();
    final BDMDeductionKey bdmDeductionKey = new BDMDeductionKey();
    bdmDeductionKey.deductionID = deductionDtls.deductionID;
    final BDMDeductionDetails bdmDedDtls =
      bdmDeductionObj.readByDeductionID(bdmDeductionKey);

    final DeductionKey adminDeductionKey = new DeductionKey();
    adminDeductionKey.key.deductionID = deductionDtls.deductionID;
    final ReadDeductionDetails dedDtls =
      DeductionFactory.newInstance().read(adminDeductionKey);

    final String deductionName = caseDeductionDtls.deductionName;
    final String actionType = dedDtls.dtls.actionType;
    final String category = DEDUCTIONCATEGORYCODE.APPLIEDDEDUCTION;
    String deductionType = "";
    if (!amount.isZero()) {
      deductionType = DEDUCTIONITEMTYPE.FIXED;
    } else {
      deductionType = DEDUCTIONITEMTYPE.VARIABLE;
    }

    if (DEDUCTIONITEMTYPE.FIXED.equals(deductionType)) {
      final CaseNominee caseNomineeObj = CaseNomineeFactory.newInstance();
      final CaseNomineeCaseIDAndStatusKey caseNomineeCaseIDAndStatusKey =
        new CaseNomineeCaseIDAndStatusKey();
      caseNomineeCaseIDAndStatusKey.caseNomineeCaseIDAndStatusKey.caseID =
        caseDeductionDtls.caseID;
      caseNomineeCaseIDAndStatusKey.caseNomineeCaseIDAndStatusKey.nomineeStatus =
        CASENOMINEESTATUS.OPERATIONAL;
      final CaseNomineeAndStatusDetailsList caseNomineeAndStatusDetailsList =
        caseNomineeObj.listOperationalNominee(caseNomineeCaseIDAndStatusKey);
      CaseNomineeAndStatusDetails caseNomineeAndStatusDetails = null;

      for (int i =
        0; i < caseNomineeAndStatusDetailsList.caseNomineeAndStatusDetailsList
          .size(); i++) {
        caseNomineeAndStatusDetails =
          caseNomineeAndStatusDetailsList.caseNomineeAndStatusDetailsList
            .item(i);

        if (caseNomineeAndStatusDetails.defaultNomInd) {
          final DeductionActivationDetails deductionKey =
            new DeductionActivationDetails();
          final CaseDeductionItemDtls cdiDtls = new CaseDeductionItemDtls();
          deductionKey.actionIDProperty =
            ClientActionConst.kActivate_Now_Fixed;
          deductionKey.deductionDtls.startDate = startDate;
          deductionKey.deductionDtls.endDate = endDate;
          deductionKey.deductionDtls.rate = rate;
          deductionKey.deductionDtls.relatedCaseID = relatedCaseID;
          deductionKey.deductionDtls.caseID = caseID;
          deductionKey.deductionDtls.amount = amount;
          deductionKey.deductionDtls.deductionName = deductionName;
          deductionKey.deductionDtls.actionType = actionType;
          deductionKey.deductionDtls.category = category;
          deductionKey.deductionDtls.deductionType = deductionType;
          deductionKey.nomineeDtls.caseNomineeID =
            caseNomineeAndStatusDetails.caseNomineeID;
          deductionKey.deductionDtls.nomineeID =
            caseNomineeAndStatusDetails.caseNomineeID;
          cdiDtls.assign(deductionKey.deductionDtls);
          deductionKey.deductionDtls.priority =
            maintainDeductionDetailsObj.calculatePriority(cdiDtls);
          allowMultipleCaseDeductionUpdates(true);
          productDeliveryObj.userActionFixedDeduction(deductionKey);
          allowMultipleCaseDeductionUpdates(false);
        }
      }
    } else {
      final CreateDeductionDetails1 activeVariableDeductionKey =
        new CreateDeductionDetails1();
      final CaseDeductionItemDtls cdiDtls = new CaseDeductionItemDtls();
      activeVariableDeductionKey.startDate = startDate;
      activeVariableDeductionKey.endDate = endDate;
      activeVariableDeductionKey.rate = rate;
      activeVariableDeductionKey.caseID = caseDeductionDtls.caseID;
      activeVariableDeductionKey.relatedCaseID = relatedCaseID;
      activeVariableDeductionKey.amount = amount;
      activeVariableDeductionKey.deductionName =
        caseDeductionDtls.deductionName;
      activeVariableDeductionKey.actionType = actionType;
      activeVariableDeductionKey.category = category;
      activeVariableDeductionKey.deductionType = deductionType;
      cdiDtls.assign(activeVariableDeductionKey);

      caseHeaderKey.caseID = relatedCaseID;

      // Get the liability concern role id
      final ReadParticipantRoleIDDetails readParticipantRoleIDDetails =
        CaseHeaderFactory.newInstance().readParticipantRoleID(caseHeaderKey);

      activeVariableDeductionKey.liabilityConcernRoleID =
        readParticipantRoleIDDetails.concernRoleID;

      activeVariableDeductionKey.priority =
        maintainDeductionDetailsObj.calculatePriority(cdiDtls);

      allowMultipleCaseDeductionUpdates(true);
      productDeliveryObj
        .createActiveVariableDeduction(activeVariableDeductionKey);
      allowMultipleCaseDeductionUpdates(false);
    }

  }

  @Override
  public void regenerateCaseFinancials(final CaseIDKey caseIDKey)
    throws AppException, InformationalException {

    // This method submits the case for regeneration only if OOTB
    // RegenerateCaseFinancials doesn't do because the transaction type is not
    // online.
    final CaseStatus caseStatusObj = CaseStatusFactory.newInstance();
    final CurrentCaseStatusKey currentCaseStatusKey =
      new CurrentCaseStatusKey();
    currentCaseStatusKey.caseID = caseIDKey.caseID;
    final CaseStatusDtls caseStatusDtls =
      caseStatusObj.readCurrentStatusByCaseID1(currentCaseStatusKey);

    if (!CASESTATUS.DELAYEDPROC.equals(caseStatusDtls.statusCode)) {
      if ((CASESTATUS.ACTIVE.equals(caseStatusDtls.statusCode)
        || CASESTATUS.SUSPENDED.equals(caseStatusDtls.statusCode)
        || CASESTATUS.CLOSED.equals(caseStatusDtls.statusCode)
        || CASESTATUS.PENDINGCLOSURE.equals(caseStatusDtls.statusCode))
        && !TransactionInfo.TransactionType.kOnline
          .equals(TransactionInfo.getTransactionType())) {
        final ModifyCaseStatusForDelayedProcessing modifyCaseStatusForDelayedProcessing =
          ModifyCaseStatusForDelayedProcessingFactory.newInstance();
        final SetStatusForDelayedProcessingKey setStatusForDelayedProcessingKey =
          new SetStatusForDelayedProcessingKey();
        setStatusForDelayedProcessingKey.caseID = caseIDKey.caseID;
        final SetStatusForDelayedProcessingDtls setStatusForDelayedProcessingDtls =
          modifyCaseStatusForDelayedProcessing
            .setStatus(setStatusForDelayedProcessingKey);
        final WMInstanceDataDtls wmInstanceDataDtls =
          new WMInstanceDataDtls();
        final WMInstanceData wmInstanceDataObj =
          WMInstanceDataFactory.newInstance();
        wmInstanceDataDtls.wm_instDataID =
          curam.util.type.UniqueID.nextUniqueID("DPTICKET");
        wmInstanceDataDtls.enteredByID = TransactionInfo.getProgramUser();
        wmInstanceDataDtls.caseID = caseIDKey.caseID;
        wmInstanceDataDtls.caseStatus =
          setStatusForDelayedProcessingDtls.caseStatus;
        wmInstanceDataObj.insert(wmInstanceDataDtls);

        final DeferredProcessing dpEnactmentProcessingObj =
          DeferredProcessingFactory.newInstance();
        dpEnactmentProcessingObj.startProcess(
          DP_const.DP_REGENERATE_CASE_FINANCIAL_COMPONENTS,
          wmInstanceDataDtls.wm_instDataID);

      }
    }

  }

  /**
   * This method will process status due vtw deduction.
   *
   * @param key - CaseDeductionItemKey
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void processStatusDueVTWDeduction(final CaseDeductionItemKey key)
    throws AppException, InformationalException {

    final BDMCaseDeductionItem bdmCaseDeductionItemObj =
      BDMCaseDeductionItemFactory.newInstance();
    final BDMCaseDeductionItemKey bdmCaseDeductionItemKey =
      new BDMCaseDeductionItemKey();
    bdmCaseDeductionItemKey.caseDeductionItemID = key.caseDeductionItemID;
    final BDMCaseDeductionItemDtls bdmCaseDeductionItemDtls =
      bdmCaseDeductionItemObj.read(bdmCaseDeductionItemKey);

    if (bdmCaseDeductionItemDtls.statusRecheckDate
      .after(Date.getCurrentDate())) {
      return;
    }

    final CaseDeductionItem caseDeductionItemObj =
      CaseDeductionItemFactory.newInstance();
    final CaseDeductionItemDtls caseDeductionItemDtls =
      caseDeductionItemObj.read(key);

    final BDMVTWDeduction bdmVTWDeductionObj =
      BDMVTWDeductionFactory.newInstance();
    final BDMVTWDeductionKey bdmvtwDeductionKey = new BDMVTWDeductionKey();
    bdmvtwDeductionKey.vtwDeductionID =
      bdmCaseDeductionItemDtls.vtwDeductionID;
    final BDMVTWDeductionDtls bdmvtwDeductionDtls =
      bdmVTWDeductionObj.read(bdmvtwDeductionKey);
    final IndicatorStruct deleteUnused = new IndicatorStruct();

    Date statusRecheckDate = Date.kZeroDate;

    if (!bdmvtwDeductionDtls.endDate.isZero()
      && bdmvtwDeductionDtls.endDate.before(Date.getCurrentDate())) {
      if (BUSINESSSTATUS.ACTIVE
        .equals(caseDeductionItemDtls.businessStatus)) {
        deleteUnused.changeAllIndicator = true;
        this.maintainParticipantDeductionObj
          .deactivateSingleCaseDeduction(key, deleteUnused);
      }
    } else if (!bdmvtwDeductionDtls.startDate.after(Date.getCurrentDate())) {
      if (BUSINESSSTATUS.INACTIVE
        .equals(caseDeductionItemDtls.businessStatus)) {
        this.activateDeductionItem(key);
      }
      if (!bdmvtwDeductionDtls.endDate.isZero()
        && !bdmvtwDeductionDtls.endDate.before(Date.getCurrentDate())) {
        statusRecheckDate = bdmvtwDeductionDtls.endDate.addDays(1);
      }

    } else if (bdmvtwDeductionDtls.startDate.after(Date.getCurrentDate())) {
      // Unlikely scenarios.
      if (BUSINESSSTATUS.ACTIVE
        .equals(caseDeductionItemDtls.businessStatus)) {
        deleteUnused.changeAllIndicator = false;
        this.maintainParticipantDeductionObj
          .deactivateSingleCaseDeduction(key, deleteUnused);
      }
      statusRecheckDate = bdmvtwDeductionDtls.startDate;
    }
    bdmCaseDeductionItemDtls.statusRecheckDate = statusRecheckDate;
    bdmCaseDeductionItemObj.modify(bdmCaseDeductionItemKey,
      bdmCaseDeductionItemDtls);
  }

  /**
   * This method will activate the deduction item.
   *
   * @param key
   * @throws AppException
   * @throws InformationalException
   */
  public void activateDeductionItem(final CaseDeductionItemKey key)
    throws AppException, InformationalException {

    final CaseDeductionItem caseDeductionItemObj =
      CaseDeductionItemFactory.newInstance();
    final VersionNo versionNo = caseDeductionItemObj.readVersionNo(key);
    this.allowMultipleCaseDeductionUpdates(true);
    final DeductionItemKeyVersionNo deductionItemKeyVersionNo =
      new DeductionItemKeyVersionNo();
    deductionItemKeyVersionNo.caseDeductionItemID = key.caseDeductionItemID;
    deductionItemKeyVersionNo.versionNo = versionNo.versionNo;
    ProductDeliveryFactory.newInstance()
      .activateDeduction(deductionItemKeyVersionNo);
    this.allowMultipleCaseDeductionUpdates(false);
  }
}
