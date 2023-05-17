/**
 *
 */
package curam.ca.gc.bdm.sl.financial.maintainliabilitycase.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMDEDUCTIONTYPE;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMSearchManualOPByBenefitCaseIDKey;
import curam.ca.gc.bdm.entity.deduction.fact.BDMDeductionFactory;
import curam.ca.gc.bdm.entity.deduction.intf.BDMDeduction;
import curam.ca.gc.bdm.entity.deduction.struct.GetDeductionNameByCaseKey;
import curam.ca.gc.bdm.entity.fact.BDMCaseHeaderDAFactory;
import curam.ca.gc.bdm.entity.intf.BDMCaseHeaderDA;
import curam.ca.gc.bdm.entity.struct.BDMSearchManualOPByBenefitCaseIDDetails;
import curam.ca.gc.bdm.entity.subclass.instructionlineitem.fact.BDMInstructionLineItemDAFactory;
import curam.ca.gc.bdm.entity.subclass.instructionlineitem.intf.BDMInstructionLineItemDA;
import curam.ca.gc.bdm.entity.subclass.instructionlineitem.struct.CaseCreditDebitTypeKey;
import curam.ca.gc.bdm.entity.subclass.productdelivery.fact.BDMProductDeliveryDAFactory;
import curam.ca.gc.bdm.entity.subclass.productdelivery.struct.BDMSearchEligBenefitsForExternalLbyDtls;
import curam.ca.gc.bdm.entity.subclass.productdelivery.struct.BDMSearchEligBenefitsForExternalLbyDtlsList;
import curam.ca.gc.bdm.entity.subclass.productdelivery.struct.BDMSearchEligBenefitsForExternalLbyKey;
import curam.ca.gc.bdm.entity.subclass.productdelivery.struct.EligBenefitsForDeductionDetailsList;
import curam.ca.gc.bdm.entity.subclass.productdelivery.struct.EligBenefitsForDeductionKey;
import curam.ca.gc.bdm.facade.bdmcommonintake.struct.BDMCaseID;
import curam.ca.gc.bdm.facade.bdmmanualoverpayment.struct.BDMSearchManualOPByBenefitCaseIDDetailsList;
import curam.ca.gc.bdm.message.BDMEXTERNALLIABILITY;
import curam.ca.gc.bdm.sl.financial.maintainliabilitycase.struct.BenefitsForDeductionList;
import curam.ca.gc.bdm.sl.financial.maintainliabilitycase.struct.ConcernRoleIDKey;
import curam.ca.gc.bdm.sl.financial.maintainliabilitycase.struct.CreateBenefitOverpaymentDetails;
import curam.ca.gc.bdm.sl.financial.maintainliabilitycase.struct.CreateManualOverpaymentDetails;
import curam.ca.gc.bdm.sl.financial.maintainliabilitycase.struct.CreateNonReverseOverpaymentDetails;
import curam.ca.gc.bdm.sl.financial.maintainliabilitycase.struct.ManualOverpaymentDeductionsKey;
import curam.ca.gc.bdm.sl.maintaincasedeductions.impl.MaintainCaseDeductions;
import curam.ca.gc.bdm.sl.organization.fact.BDMCaseOwnershipStrategyFactory;
import curam.ca.gc.bdm.sl.struct.CreateCaseDeductionDetails;
import curam.codetable.CASECLOSEREASON;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASESTATUS;
import curam.codetable.CREDITDEBIT;
import curam.codetable.DEDUCTIONNAME;
import curam.codetable.EVIDENCEGROUPNAMECODE;
import curam.codetable.ILICATEGORY;
import curam.codetable.ILISTATUS;
import curam.codetable.METHODOFDELIVERY;
import curam.codetable.PRODUCTNAME;
import curam.codetable.RECORDSTATUS;
import curam.codetable.WRITEOFFREASON;
import curam.core.facade.fact.FinancialFactory;
import curam.core.facade.fact.ProductDeliveryFactory;
import curam.core.facade.intf.Financial;
import curam.core.facade.struct.CloseCaseDetails;
import curam.core.facade.struct.GenerateLiabilityKey;
import curam.core.facade.struct.WriteOffInstructionLineItemKey;
import curam.core.fact.CachedCaseHeaderFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.CaseStatusFactory;
import curam.core.fact.CreateProductDeliveryFactory;
import curam.core.fact.InstructionLineItemFactory;
import curam.core.fact.ProductDeliveryActivationEligibilityFactory;
import curam.core.fact.ProductFactory;
import curam.core.fact.ProductProvisionFactory;
import curam.core.fact.ProvisionLocationFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.intf.CachedCaseHeader;
import curam.core.intf.CaseHeader;
import curam.core.intf.CaseStatus;
import curam.core.intf.CreateProductDelivery;
import curam.core.intf.InstructionLineItem;
import curam.core.intf.Product;
import curam.core.intf.ProductDeliveryActivationEligibility;
import curam.core.intf.ProductProvision;
import curam.core.intf.ProvisionLocation;
import curam.core.sl.entity.fact.OverpaymentEvidenceFactory;
import curam.core.sl.entity.intf.OverpaymentEvidence;
import curam.core.sl.entity.struct.OverpaymentEvidenceDtls;
import curam.core.sl.fact.CaseEvidenceAPIFactory;
import curam.core.sl.intf.CaseEvidenceAPI;
import curam.core.sl.struct.ActivateEvidenceTreeDetails;
import curam.core.sl.struct.CaseIDKey;
import curam.core.sl.struct.CreateEvidenceGroupDetails;
import curam.core.sl.struct.CreateEvidenceGroupResult;
import curam.core.sl.struct.CreateLinkDetails;
import curam.core.sl.struct.CreateNewTreeDetails;
import curam.core.sl.struct.CreateNewTreeResult;
import curam.core.sl.struct.DeductionName;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseIDDetails;
import curam.core.struct.CaseStatusDtls;
import curam.core.struct.CaseStatusKey;
import curam.core.struct.CurrentCaseStatusKey;
import curam.core.struct.EffectiveDateKey;
import curam.core.struct.ILIAmount;
import curam.core.struct.ILICaseIDCreditDebitType;
import curam.core.struct.ILICaseIDFinInstCatStatus;
import curam.core.struct.InstructionLineItemDtls;
import curam.core.struct.InstructionLineItemDtlsList;
import curam.core.struct.ProductDtls;
import curam.core.struct.ProductKey;
import curam.core.struct.ProductNameStructRef;
import curam.core.struct.ProductProvisionDtlsList;
import curam.core.struct.ProductProvisionKey;
import curam.core.struct.ProvisionLocationDtlsList;
import curam.core.struct.RegisterProductDeliveryDetails;
import curam.core.struct.RegisterProductDeliveryKey;
import curam.core.struct.SumUnallocatedAmt;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.RecordNotFoundException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.UniqueID;
import java.util.List;

/**
 * @author jigar.shah
 *
 */
public class MaintainLiabilityCase extends
  curam.ca.gc.bdm.sl.financial.maintainliabilitycase.base.MaintainLiabilityCase {

  private static String BASE_CURRENCY_USD = "USD";

  private static String BDM_ENV_BDMPAYMENT_MANUALOVERPAYMENT =
    "curam.ca.gc.bdm.payment.manualOverpayment.productid";

  private static String BDM_ENV_BDMPAYMENT_NONREVERSIBLEDEDOP =
    "curam.ca.gc.bdm.payment.nonreversibleDedOP.productid";

  private static int BDM_ENV_BDMPAYMENT_MANUALOVERPAYMENT_DEFAULT = 80011;

  private static int BDM_ENV_BDMPAYMENT_NONREVERSIBLEDEDOP_DEFAULT = 80010;

  @Inject
  MaintainCaseDeductions maintainCaseDedObj;

  public MaintainLiabilityCase() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * This method is used to return the list of manual overpayment for benefit
   * case. This method will fetch the list by using Case Relationship.
   */
  @Override
  public BDMSearchManualOPByBenefitCaseIDDetailsList
    searchManualOPByBenefitCaseID(final BDMCaseID arg1)
      throws AppException, InformationalException {

    final BDMSearchManualOPByBenefitCaseIDDetailsList list =
      new BDMSearchManualOPByBenefitCaseIDDetailsList();

    final BDMSearchManualOPByBenefitCaseIDKey key =
      new BDMSearchManualOPByBenefitCaseIDKey();
    key.caseID = arg1.caseID;
    key.closedCaseStatus = CASESTATUS.CLOSED;
    try {
      key.manOPProductID =
        Configuration.getIntProperty(BDM_ENV_BDMPAYMENT_MANUALOVERPAYMENT);
    } catch (final NumberFormatException nfe) {
      key.manOPProductID = BDM_ENV_BDMPAYMENT_MANUALOVERPAYMENT_DEFAULT;
    }
    try {
      key.nonRevOPProductID =
        Configuration.getIntProperty(BDM_ENV_BDMPAYMENT_NONREVERSIBLEDEDOP);
    } catch (final NumberFormatException nfe) {
      key.nonRevOPProductID = BDM_ENV_BDMPAYMENT_NONREVERSIBLEDEDOP_DEFAULT;
    }
    final BDMCaseHeaderDA caseHeaderObj =
      BDMCaseHeaderDAFactory.newInstance();
    final curam.ca.gc.bdm.entity.struct.BDMSearchManualOPByBenefitCaseIDDetailsList listEL =
      caseHeaderObj.searchManualOPByBenefitCaseID(key);
    BDMSearchManualOPByBenefitCaseIDDetails detailsEL;
    curam.ca.gc.bdm.facade.bdmmanualoverpayment.struct.BDMSearchManualOPByBenefitCaseIDDetails details;
    final ILICaseIDCreditDebitType iliCaseIDCreditDebitType =
      new ILICaseIDCreditDebitType();
    iliCaseIDCreditDebitType.creditDebitType = CREDITDEBIT.DEBIT;
    final InstructionLineItem instructionLineItemInst =
      InstructionLineItemFactory.newInstance();
    SumUnallocatedAmt sumUnallocatedAmt;
    for (int i = 0; i < listEL.dtls.size(); i++) {
      detailsEL = listEL.dtls.item(i);
      details =
        new curam.ca.gc.bdm.facade.bdmmanualoverpayment.struct.BDMSearchManualOPByBenefitCaseIDDetails();
      details.assign(detailsEL);
      iliCaseIDCreditDebitType.caseID = details.caseID;
      sumUnallocatedAmt =
        instructionLineItemInst.readSumUnprocessedAmtByCaseIDCreditDebitType(
          iliCaseIDCreditDebitType);
      details.outstandingAmount = sumUnallocatedAmt.amount;
      list.dtls.addRef(details);
    }
    return list;
  }

  /**
   * This method will create an overpayment case for NonReverse Deductions upon
   * cancellation of the payment.
   */
  @Override
  public CaseIDKey createNonReverseDedOverpayment(
    final CreateNonReverseOverpaymentDetails details)
    throws AppException, InformationalException {

    final CreateBenefitOverpaymentDetails createBenefitOverpaymentDetails =
      new CreateBenefitOverpaymentDetails();
    createBenefitOverpaymentDetails.caseStartDate =
      details.caseStartDate.after(TransactionInfo.getSystemDate())
        ? TransactionInfo.getSystemDate() : details.caseStartDate;
    createBenefitOverpaymentDetails.caseEndDate = details.caseEndDate;
    createBenefitOverpaymentDetails.productName =
      PRODUCTNAME.BDM_OVERPAYMENT_NONREVERSEDED;
    createBenefitOverpaymentDetails.concernRoleID = details.concernRoleID;
    createBenefitOverpaymentDetails.liabilityAmount = details.liabilityAmount;
    createBenefitOverpaymentDetails.autoActivateInd = true;
    final CaseIDKey lbyCaseIDKey =
      this.createBenefitOverpayment(createBenefitOverpaymentDetails);

    // Create deductions at 50%
    // look for any benefit cases that are not closed
    final BDMSearchEligBenefitsForExternalLbyKey pdKey =
      new BDMSearchEligBenefitsForExternalLbyKey();
    pdKey.caseStatus = CASESTATUS.CLOSED;
    pdKey.concernRoleID = details.concernRoleID;
    pdKey.deductionType = BDMDEDUCTIONTYPE.PROGRAM_SPECIFIC_OVERPAYMENT;
    final BDMSearchEligBenefitsForExternalLbyDtlsList eligBenefitsList =
      BDMProductDeliveryDAFactory.newInstance()
        .searchEligBenefitsForExternalLby(pdKey);
    final BDMDeduction bdmDeductionObj = BDMDeductionFactory.newInstance();
    CreateCaseDeductionDetails caseDeductionDetails = null;
    CaseIDKey caseIDKey = null;
    GetDeductionNameByCaseKey dedNameCaseKey = null;
    DeductionName readDeductionNameByCase = null;
    for (final BDMSearchEligBenefitsForExternalLbyDtls benefitCase : eligBenefitsList.dtls) {
      dedNameCaseKey = new GetDeductionNameByCaseKey();
      dedNameCaseKey.caseID = benefitCase.caseID;
      dedNameCaseKey.deductionType =
        BDMDEDUCTIONTYPE.PROGRAM_SPECIFIC_OVERPAYMENT;

      // find the deduction name
      readDeductionNameByCase =
        bdmDeductionObj.readDeductionNameByCase(dedNameCaseKey);

      caseDeductionDetails = new CreateCaseDeductionDetails();
      caseIDKey = new CaseIDKey();
      caseDeductionDetails.deductionName =
        readDeductionNameByCase.deductionName;
      caseDeductionDetails.caseID = benefitCase.caseID;
      caseDeductionDetails.startDate = Date.getCurrentDate();
      caseDeductionDetails.rate = 100;
      caseIDKey.caseID = lbyCaseIDKey.caseID;
      maintainCaseDedObj.createAppliedCaseDeduction(caseDeductionDetails,
        caseIDKey);

      // OOTB regenrate financials doesn't happen for batch transaction types.
      caseIDKey = new CaseIDKey();
      caseIDKey.caseID = benefitCase.caseID;
      maintainCaseDedObj.regenerateCaseFinancials(caseIDKey);
    }

    return lbyCaseIDKey;
  }

  /**
   * This method will create Benefit Overpayment Case.
   */
  @Override
  public CaseIDKey
    createBenefitOverpayment(final CreateBenefitOverpaymentDetails details)
      throws AppException, InformationalException {

    // Read product details
    final Product productObj = ProductFactory.newInstance();
    final ProductNameStructRef productNameStructRef =
      new ProductNameStructRef();
    productNameStructRef.name = details.productName;
    productNameStructRef.statusCode = RECORDSTATUS.NORMAL;

    final ProductDtls productDtls =
      productObj.readProductsByName(productNameStructRef);

    final ProductProvision productProvisionObj =
      ProductProvisionFactory.newInstance();
    final ProductKey productKey = new ProductKey();
    productKey.productID = productDtls.productID;
    final ProductProvisionDtlsList productProvisionDtlsList =
      productProvisionObj.searchByProductID(productKey);

    final ProvisionLocation provisionLocationObj =
      ProvisionLocationFactory.newInstance();
    final ProductProvisionKey productProvisionKey = new ProductProvisionKey();
    productProvisionKey.productProvisionID =
      productProvisionDtlsList.dtls.item(0).productProvisionID;

    final ProvisionLocationDtlsList provisionLocationList =
      provisionLocationObj.searchByProductProvisionID(productProvisionKey);

    // Fetch the base currency information from properties. If base currency is
    // not set in properties then set it to "USD"
    String baseCurrency = Configuration.getProperty(EnvVars.ENV_BASECURRENCY);
    if (StringUtil.isNullOrEmpty(baseCurrency)) {
      baseCurrency = BASE_CURRENCY_USD;
    }

    final RegisterProductDeliveryKey registerProductDeliveryKey =
      new RegisterProductDeliveryKey();
    registerProductDeliveryKey.expectedStartDate = details.caseStartDate;
    registerProductDeliveryKey.caseStartDate = details.caseStartDate;
    registerProductDeliveryKey.expectedEndDate = details.caseEndDate;
    registerProductDeliveryKey.receivedDate = Date.getCurrentDate();
    registerProductDeliveryKey.paymentMethodType =
      METHODOFDELIVERY.DEFAULTCODE;
    registerProductDeliveryKey.currencyType = baseCurrency;
    registerProductDeliveryKey.clientID = details.concernRoleID;
    registerProductDeliveryKey.productProviderID =
      productProvisionDtlsList.dtls.item(0).providerConcernRoleID;
    registerProductDeliveryKey.providerLocationID =
      provisionLocationList.dtls.item(0).providerLocationID;
    registerProductDeliveryKey.productID = productDtls.productID;
    registerProductDeliveryKey.productDeliveryPatternID =
      productDtls.defaultProductDelPatternID;

    final CreateProductDelivery createProductDeliveryObj =
      CreateProductDeliveryFactory.newInstance();
    final RegisterProductDeliveryDetails registerProductDeliveryDetails =
      createProductDeliveryObj.registerProductDelivery(
        registerProductDeliveryKey, new RegisterProductDeliveryDetails());
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = registerProductDeliveryDetails.caseID;
    CaseHeaderDtls caseHeaderDtls = caseHeaderObj.read(caseHeaderKey);
    final EffectiveDateKey effectiveDateKey = new EffectiveDateKey();
    effectiveDateKey.effectiveDate = registerProductDeliveryKey.caseStartDate;
    effectiveDateKey.versionNo = caseHeaderDtls.versionNo;
    caseHeaderObj.modifyEffectiveDate(caseHeaderKey, effectiveDateKey);
    final CachedCaseHeader cachedCaseHeader =
      CachedCaseHeaderFactory.newInstance();
    cachedCaseHeader.reloadCache(caseHeaderKey);

    final CaseIDKey caseIDKey = new CaseIDKey();
    caseIDKey.caseID = registerProductDeliveryDetails.caseID;

    BDMCaseOwnershipStrategyFactory.newInstance().assignCase(caseIDKey);

    final CreateNewTreeDetails createNewTreeDetails =
      new CreateNewTreeDetails();
    createNewTreeDetails.caseEvidenceTreeDtls.caseID = caseHeaderKey.caseID;
    final CaseEvidenceAPI caseEvidenceAPI =
      CaseEvidenceAPIFactory.newInstance();
    final CreateNewTreeResult createNewTreeResult =
      caseEvidenceAPI.createNewTree(createNewTreeDetails);

    final CreateEvidenceGroupDetails createEvidenceGroupDetails =
      new CreateEvidenceGroupDetails();
    createEvidenceGroupDetails.caseEvidenceTreeID =
      createNewTreeResult.caseEvidenceTreeID;
    createEvidenceGroupDetails.evidenceGroupNameCode =
      EVIDENCEGROUPNAMECODE.OVERPAYMENT;
    final CreateEvidenceGroupResult createEvidenceGroupResult =
      caseEvidenceAPI.createEvidenceGroup(createEvidenceGroupDetails);

    // Insert evidence
    final OverpaymentEvidenceDtls overpaymentEvidenceDtls =
      new OverpaymentEvidenceDtls();
    overpaymentEvidenceDtls.overpaymentEvidenceID = UniqueID.nextUniqueID();
    overpaymentEvidenceDtls.caseID = caseHeaderKey.caseID;
    overpaymentEvidenceDtls.fromDate = details.caseStartDate;
    overpaymentEvidenceDtls.toDate = details.caseStartDate;
    overpaymentEvidenceDtls.overpaymentAmount = details.liabilityAmount;
    final OverpaymentEvidence overpaymentEvidenceObj =
      OverpaymentEvidenceFactory.newInstance();
    overpaymentEvidenceObj.insert(overpaymentEvidenceDtls);

    // create Evidence Link
    final CreateLinkDetails createLinkDetails = new CreateLinkDetails();
    createLinkDetails.caseEvidenceGroupID =
      createEvidenceGroupResult.caseEvidenceGroupID;
    createLinkDetails.relatedID =
      overpaymentEvidenceDtls.overpaymentEvidenceID;
    createLinkDetails.evidenceType = CASEEVIDENCE.OVERPAYMENT;
    caseEvidenceAPI.createLink(createLinkDetails);

    // activate evidence
    final ActivateEvidenceTreeDetails activateEvidenceTreeDetails =
      new ActivateEvidenceTreeDetails();
    activateEvidenceTreeDetails.key.caseEvidenceTreeID =
      createNewTreeResult.caseEvidenceTreeID;
    activateEvidenceTreeDetails.details.versionNo =
      createNewTreeResult.versionNo;
    caseEvidenceAPI.activateTree(activateEvidenceTreeDetails);

    if (details.autoActivateInd) {
      final CurrentCaseStatusKey currentCaseStatusKey =
        new CurrentCaseStatusKey();
      currentCaseStatusKey.caseID = caseHeaderKey.caseID;
      final CaseStatus caseStatusObj = CaseStatusFactory.newInstance();
      final CaseStatusDtls caseStatusDtls =
        caseStatusObj.readCurrentStatusByCaseID1(currentCaseStatusKey);
      final CaseStatusKey caseStatusKey = new CaseStatusKey();
      caseStatusKey.caseStatusID = caseStatusDtls.caseStatusID;
      caseStatusDtls.endDate = TransactionInfo.getSystemDate();
      caseStatusDtls.endDateTime = TransactionInfo.getSystemDateTime();
      caseStatusObj.modify(caseStatusKey, caseStatusDtls);
      final CaseStatusDtls caseStatusDtls2 = new CaseStatusDtls();
      caseStatusDtls2.caseID = caseHeaderKey.caseID;
      caseStatusDtls2.statusCode = CASESTATUS.APPROVED;
      caseStatusDtls2.startDate = TransactionInfo.getSystemDate();
      caseStatusDtls2.endDate = Date.kZeroDate;
      caseStatusDtls2.startDateTime = TransactionInfo.getSystemDateTime();
      caseStatusDtls2.endDateTime = DateTime.kZeroDateTime;
      caseStatusDtls2.caseStatusID = UniqueID.nextUniqueID();
      caseStatusDtls2.userName = TransactionInfo.getProgramUser();
      caseStatusObj.insert(caseStatusDtls2);

      caseHeaderDtls = caseHeaderObj.read(caseHeaderKey);
      caseHeaderDtls.statusCode = CASESTATUS.APPROVED;
      caseHeaderDtls.effectiveDate = caseHeaderDtls.startDate;
      caseHeaderObj.modify(caseHeaderKey, caseHeaderDtls);
      cachedCaseHeader.reloadCache(caseHeaderKey);

      // Activate Case
      final ProductDeliveryActivationEligibility productDeliveryActivationEligibilityObj =
        ProductDeliveryActivationEligibilityFactory.newInstance();
      final CaseIDDetails caseIDDetails = new CaseIDDetails();
      caseIDDetails.caseID = caseHeaderKey.caseID;
      caseIDDetails.caseType = caseHeaderDtls.caseTypeCode;
      productDeliveryActivationEligibilityObj
        .assessEligibilityForCase(caseIDDetails);

      // Process Liability
      final Financial financialObj = FinancialFactory.newInstance();
      final GenerateLiabilityKey generateLiabilityKey =
        new GenerateLiabilityKey();
      generateLiabilityKey.caseID = caseHeaderKey.caseID;
      financialObj.generateLiability(generateLiabilityKey);
    }

    return caseIDKey;
  }

  /**
   * This method is used to delete manual overpayment for BDM.
   */
  @Override
  public void deleteManualOverpayment(final CaseIDKey key)
    throws AppException, InformationalException {

    final ILICaseIDFinInstCatStatus iliCaseIDFinInstCatStatus =
      new ILICaseIDFinInstCatStatus();
    iliCaseIDFinInstCatStatus.caseID = key.caseID;
    iliCaseIDFinInstCatStatus.instructLineItemCategory =
      ILICATEGORY.PAYMENTINSTRUCTIONFORDEDUCTION;
    iliCaseIDFinInstCatStatus.statusCode = ILISTATUS.UNPROCESSED;
    final BDMInstructionLineItemDA iliObj =
      BDMInstructionLineItemDAFactory.newInstance();
    ILIAmount unprocessedDeductionAmt;
    try {
      unprocessedDeductionAmt =
        iliObj.readSumAmtByCaseIDCatStatus(iliCaseIDFinInstCatStatus);
    } catch (final RecordNotFoundException rnfe) {
      unprocessedDeductionAmt = new ILIAmount();
    }
    if (unprocessedDeductionAmt.amount.isPositive()) {
      throw new AppException(
        BDMEXTERNALLIABILITY.ERR_UNALLOCATED_DEDUCTIONS_EXISTS);
    }
    final CaseCreditDebitTypeKey debitTypeKey = new CaseCreditDebitTypeKey();
    debitTypeKey.caseID = key.caseID;
    debitTypeKey.creditDebitType = CREDITDEBIT.DEBIT;
    final InstructionLineItemDtlsList iliList =
      iliObj.readBDMByCaseCreditDebitType(debitTypeKey);
    final Financial financialObj = FinancialFactory.newInstance();
    final WriteOffInstructionLineItemKey itemKey =
      new WriteOffInstructionLineItemKey();
    InstructionLineItemDtls iliDtls;
    for (int i = 0; i < iliList.dtls.size(); i++) {
      iliDtls = iliList.dtls.item(i);
      itemKey.amount = iliDtls.unprocessedAmount;
      itemKey.debitInstructLineItemID = iliDtls.instructLineItemID;
      itemKey.reasonCode = WRITEOFFREASON.BDMCREATEDINERROR;
      financialObj.writeOffInstructionLineItem(itemKey);
    }
    final ILICaseIDCreditDebitType creditDebitType =
      new ILICaseIDCreditDebitType();
    creditDebitType.caseID = key.caseID;
    creditDebitType.creditDebitType = CREDITDEBIT.DEBIT;
    SumUnallocatedAmt sumUnallocatedAmt;
    try {
      sumUnallocatedAmt =
        iliObj.readSumUnprocessedAmtByCaseIDCreditDebitType(creditDebitType);
    } catch (final RecordNotFoundException rnfe) {
      sumUnallocatedAmt = new SumUnallocatedAmt();
    }
    if (sumUnallocatedAmt.amount.isPositive()) {
      throw new AppException(
        BDMEXTERNALLIABILITY.ERR_UNABLE_TOWRITEOFF_OUTSTANDINGBAL);
    }
    final CloseCaseDetails closeCaseDetails = new CloseCaseDetails();
    closeCaseDetails.caseID = key.caseID;
    closeCaseDetails.reasonCode = CASECLOSEREASON.BDMCREATEDINERROR;
    closeCaseDetails.closureDate = Date.getCurrentDate();
    ProductDeliveryFactory.newInstance().close(closeCaseDetails);
  }

  /**
   * Create Manual Overpayment case
   */
  @Override
  public CaseIDKey
    createManualOverpayment(final CreateManualOverpaymentDetails details)
      throws AppException, InformationalException {

    final CreateBenefitOverpaymentDetails overPaymentDetails =
      new CreateBenefitOverpaymentDetails();
    overPaymentDetails.caseStartDate = details.caseStartDate;
    overPaymentDetails.caseEndDate = details.caseStartDate;
    overPaymentDetails.productName = PRODUCTNAME.BDM_OVERPAYMENT_MANUAL; // DPN80004
    overPaymentDetails.concernRoleID = details.concernRoleID;
    overPaymentDetails.liabilityAmount = details.liabilityAmount;
    overPaymentDetails.autoActivateInd = true;
    return createBenefitOverpayment(overPaymentDetails);
  }

  /**
   * returns Elig benefits for given concernrole
   */
  @Override
  public BenefitsForDeductionList listBenefitsForDeduction(
    final ConcernRoleIDKey arg1) throws AppException, InformationalException {

    final BenefitsForDeductionList benefitsForDeductionList =
      new BenefitsForDeductionList();
    final EligBenefitsForDeductionKey deductionDetailsKey =
      new EligBenefitsForDeductionKey();
    deductionDetailsKey.concernRoleID = arg1.concernRoleID;
    deductionDetailsKey.caseStatus = CASESTATUS.CLOSED;
    final EligBenefitsForDeductionDetailsList deductionDtlsList =
      BDMProductDeliveryDAFactory.newInstance()
        .searchEligBenefitsForDeduction(deductionDetailsKey);
    benefitsForDeductionList.assign(deductionDtlsList);
    return benefitsForDeductionList;
  }

  /**
   * Apply Deductions for Manual Overpayment case
   */
  @Override
  public void
    applyManualOverpaymentDeductions(final ManualOverpaymentDeductionsKey key)
      throws AppException, InformationalException {

    if (!StringUtil.isNullOrEmpty(key.selBenefitIDs)) {

      // seperate tab delimited list of selected cases into an array
      final List<String> selectedCases = StringUtil.delimitedText2StringList(
        key.selBenefitIDs, CuramConst.gkTabDelimiterChar);

      for (final String caseID : selectedCases) {

        final CreateCaseDeductionDetails deductionDtls =
          new CreateCaseDeductionDetails();
        deductionDtls.caseID = Long.valueOf(caseID);
        deductionDtls.amount = key.deductionAmount;
        deductionDtls.rate = key.deductionRate;
        deductionDtls.startDate = key.deductionStartDate;
        deductionDtls.deductionName = DEDUCTIONNAME.PROGRAM_OVERPAYMENT;
        final CaseIDKey caseIDKey = new CaseIDKey();
        caseIDKey.caseID = key.liabilityCaseID;
        final MaintainCaseDeductions caseDeductions =
          new MaintainCaseDeductions();
        caseDeductions.createAppliedCaseDeduction(deductionDtls, caseIDKey);
      }
    }

  }
}
