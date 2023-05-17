package curam.ca.gc.bdm.facade.bdmmanualoverpayment.impl;

import curam.ca.gc.bdm.entity.subclass.productdelivery.fact.BDMProductDeliveryDAFactory;
import curam.ca.gc.bdm.entity.subclass.productdelivery.struct.EligBenefitsForDeductionDetailsList;
import curam.ca.gc.bdm.entity.subclass.productdelivery.struct.EligBenefitsForDeductionKey;
import curam.ca.gc.bdm.facade.bdmcommonintake.struct.BDMCaseID;
import curam.ca.gc.bdm.facade.bdmmanualoverpayment.struct.BDMLiabilityCaseKey;
import curam.ca.gc.bdm.facade.bdmmanualoverpayment.struct.BDMSearchManualOPByBenefitCaseIDDetailsList;
import curam.ca.gc.bdm.facade.bdmmanualoverpayment.struct.BDMWizardKey;
import curam.ca.gc.bdm.facade.bdmmanualoverpayment.struct.CreateBenefitOverpaymentDetails;
import curam.ca.gc.bdm.facade.bdmmanualoverpayment.struct.CreateManualOverpaymentDetails;
import curam.ca.gc.bdm.message.BDMFINANCIALS;
import curam.ca.gc.bdm.rest.bdmpaymentapi.impl.BDMPaymentAPI;
import curam.ca.gc.bdm.sl.financial.maintainliabilitycase.fact.MaintainLiabilityCaseFactory;
import curam.ca.gc.bdm.sl.financial.maintainliabilitycase.intf.MaintainLiabilityCase;
import curam.ca.gc.bdm.sl.financial.maintainliabilitycase.struct.BenefitsForDeductionDetails;
import curam.ca.gc.bdm.sl.financial.maintainliabilitycase.struct.BenefitsForDeductionList;
import curam.ca.gc.bdm.sl.financial.maintainliabilitycase.struct.ConcernRoleIDKey;
import curam.ca.gc.bdm.sl.financial.maintainliabilitycase.struct.ManualOverpaymentDeductionsKey;
import curam.codetable.CASESTATUS;
import curam.codetable.PRODUCTTYPE;
import curam.core.facade.struct.CaseIDKey;
import curam.core.impl.CuramConst;
import curam.core.struct.InformationalMsgDtlsList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement.InformationalType;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.Money;
import curam.wizardpersistence.impl.WizardPersistentState;

public abstract class BDMManualOverPayment extends
  curam.ca.gc.bdm.facade.bdmmanualoverpayment.base.BDMManualOverPayment {

  final String kProperties = "BDMAddManualOverPayment.properties";

  @Override
  public BDMSearchManualOPByBenefitCaseIDDetailsList
    searchManualOPByBenefitCaseID(final BDMCaseID arg1)
      throws AppException, InformationalException {

    final MaintainLiabilityCase caseObj =
      MaintainLiabilityCaseFactory.newInstance();
    final BDMSearchManualOPByBenefitCaseIDDetailsList list =
      caseObj.searchManualOPByBenefitCaseID(arg1);
    return list;
  }

  @Override
  public BDMLiabilityCaseKey
    createBenefitOverpayment(final CreateBenefitOverpaymentDetails key)
      throws AppException, InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    final InformationalMsgDtlsList informationalMsgDtlsList =
      new InformationalMsgDtlsList();

    if (key.selCaseIDStr.trim().isEmpty()) {

      informationalManager.addInformationalMsg(
        new AppException(BDMFINANCIALS.ERR_PRODUCT_DELIVERY_CASE_MANDATORY),
        "", InformationalType.kError);
    }

    if (key.recoveryRateFixed == 0 && key.recoveryRateVariable == 0) {

      informationalManager.addInformationalMsg(
        new AppException(BDMFINANCIALS.ERR_RECOVERY_RATE_MANDATORY), "",
        InformationalType.kError);

    }

    if (key.deductionStartDate.before(Date.getCurrentDate())
      || key.deductionStartDate.isZero()) {

      informationalManager.addInformationalMsg(
        new AppException(BDMFINANCIALS.ERR_DEDUCTION_START_DATE), "",
        InformationalType.kError);

    }

    informationalManager.failOperation();

    final BDMWizardKey wizardKey = new BDMWizardKey();
    wizardKey.wizardStateID = key.wizardStateID;

    final CreateManualOverpaymentDetails createManualOverpaymentDetails =
      this.readWizardDetails(wizardKey);

    final MaintainLiabilityCase maintainLiabilityCaseObj =
      MaintainLiabilityCaseFactory.newInstance();
    final curam.ca.gc.bdm.sl.financial.maintainliabilitycase.struct.CreateManualOverpaymentDetails overPaymentDtls =
      new curam.ca.gc.bdm.sl.financial.maintainliabilitycase.struct.CreateManualOverpaymentDetails();
    // need to confirm
    overPaymentDtls.caseStartDate =
      createManualOverpaymentDetails.caseStartDate;
    overPaymentDtls.concernRoleID =
      createManualOverpaymentDetails.concernRoleID;
    overPaymentDtls.liabilityAmount =
      createManualOverpaymentDetails.liabilityAmount;

    // Create Overpayment case
    final curam.core.sl.struct.CaseIDKey liabilityCaseIDKey =
      maintainLiabilityCaseObj.createManualOverpayment(overPaymentDtls);

    // Create deductions for each one of the case

    final ManualOverpaymentDeductionsKey manualOverpaymentDeductionsKey =
      new ManualOverpaymentDeductionsKey();
    manualOverpaymentDeductionsKey.concernRoleID =
      createManualOverpaymentDetails.concernRoleID;

    manualOverpaymentDeductionsKey.deductionStartDate =
      key.deductionStartDate;
    manualOverpaymentDeductionsKey.liabilityCaseID =
      liabilityCaseIDKey.caseID;
    manualOverpaymentDeductionsKey.selBenefitIDs = key.selCaseIDStr;

    if (key.recoveryRateFixed != 0) {
      manualOverpaymentDeductionsKey.deductionAmount =
        new Money(key.recoveryRateFixed);
    } else {
      manualOverpaymentDeductionsKey.deductionRate = key.recoveryRateVariable;
    }

    maintainLiabilityCaseObj
      .applyManualOverpaymentDeductions(manualOverpaymentDeductionsKey);

    final BDMLiabilityCaseKey bdmLiabilityCaseKey = new BDMLiabilityCaseKey();
    bdmLiabilityCaseKey.caseID = liabilityCaseIDKey.caseID;
    bdmLiabilityCaseKey.informationalMsgDtlsList = informationalMsgDtlsList;

    return bdmLiabilityCaseKey;
  }

  /**
   * This method is used to delete manual overpayment for BDM.
   */
  @Override
  public void deleteManualOverpayment(final CaseIDKey key)
    throws AppException, InformationalException {

    final curam.core.sl.struct.CaseIDKey keySL =
      new curam.core.sl.struct.CaseIDKey();
    keySL.caseID = key.dtls.dtls.caseID;

    MaintainLiabilityCaseFactory.newInstance().deleteManualOverpayment(keySL);

  }

  @Override
  public CreateManualOverpaymentDetails
    storeWizardDetails(final CreateManualOverpaymentDetails key)
      throws AppException, InformationalException {

    if (key.liabilityAmount.isZero()) {
      throw new AppException(BDMFINANCIALS.ERR_LIABILITY_AMOUNT_MANDATORY);
    }

    key.wizardMenu = kProperties;

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    key.wizardStateID = wizardPersistentState.create(key);

    return key;

  }

  @Override
  public CreateManualOverpaymentDetails readWizardDetails(
    final BDMWizardKey key) throws AppException, InformationalException {

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();
    CreateManualOverpaymentDetails createManualOverpaymentDetails =
      new CreateManualOverpaymentDetails();

    if (key.wizardStateID != 0) {
      createManualOverpaymentDetails =
        (CreateManualOverpaymentDetails) wizardPersistentState
          .read(key.wizardStateID);
    }

    createManualOverpaymentDetails.wizardMenu = kProperties;

    return createManualOverpaymentDetails;

  }

  @Override
  public BenefitsForDeductionList listBenefitsForDeduction(
    final ConcernRoleIDKey key) throws AppException, InformationalException {

    final BenefitsForDeductionList benefitsForDeductionList =
      new BenefitsForDeductionList();

    final EligBenefitsForDeductionKey eligBenefitsForDeductionKey =
      new EligBenefitsForDeductionKey();

    eligBenefitsForDeductionKey.caseStatus = CASESTATUS.CLOSED;
    eligBenefitsForDeductionKey.concernRoleID = key.concernRoleID;

    final EligBenefitsForDeductionDetailsList eligBenefitsForDeductionDetailsList =
      BDMProductDeliveryDAFactory.newInstance()
        .searchEligBenefitsForDeduction(eligBenefitsForDeductionKey);

    eligBenefitsForDeductionDetailsList.dtls
      .forEach((eligBenefitsForDeductionDetails) -> {

        final BenefitsForDeductionDetails benefitsForDeduction =
          new BenefitsForDeductionDetails();

        benefitsForDeduction.caseDescription =
          eligBenefitsForDeductionDetails.caseReference + CuramConst.gkSpace
            + "-" + CuramConst.gkSpace
            + BDMPaymentAPI.getCodeTableDescriptionForUserLocale(
              PRODUCTTYPE.TABLENAME,
              eligBenefitsForDeductionDetails.productType);

        benefitsForDeduction.caseID = eligBenefitsForDeductionDetails.caseID;
        benefitsForDeduction.productType =
          eligBenefitsForDeductionDetails.productType;
        benefitsForDeduction.caseReference =
          eligBenefitsForDeductionDetails.caseReference;

        benefitsForDeductionList.dtls.add(benefitsForDeduction);

      });

    return benefitsForDeductionList;
  }

}
