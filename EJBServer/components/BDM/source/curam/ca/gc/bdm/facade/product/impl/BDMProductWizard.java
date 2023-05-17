package curam.ca.gc.bdm.facade.product.impl;

import curam.ca.gc.bdm.entity.fact.BDMProductFactory;
import curam.ca.gc.bdm.entity.struct.BDMProductDtls;
import curam.ca.gc.bdm.facade.product.struct.BDMBenefitPageDetails;
import curam.ca.gc.bdm.facade.product.struct.BDMBenefitSummaryPageDetails;
import curam.ca.gc.bdm.wizard.datastore.impl.BDMProductWizardDataDetails;
import curam.ca.gc.bdm.wizard.datastore.impl.BDMProductWizardDatastoreFactory;
import curam.ca.gc.bdm.wizard.datastore.impl.BDMProductWizardDatastoreImpl;
import curam.core.impl.CuramConst;
import curam.core.sl.infrastructure.assessment.impl.RuleSetIdentifiers;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.wizard.datastore.impl.ProductWizardDataDetails;
import curam.wizard.datastore.impl.ProductWizardDataDetailsFactory;
import curam.wizard.datastore.impl.ProductWizardDatastore;
import curam.wizard.facade.struct.BenefitPageDetails;
import curam.wizard.facade.struct.WizardData;
import curam.wizard.sl.impl.AdminProductWizardFactory;

public abstract class BDMProductWizard
  extends curam.ca.gc.bdm.facade.product.base.BDMProductWizard {

  @Override
  public WizardData storeBenefitSummary(final WizardData key)
    throws AppException, InformationalException {

    /* 473 */ final ProductWizardDataDetails dataDetails =
      ProductWizardDataDetailsFactory.newInstance();
    /*     */
    /* 475 */ dataDetails.setContextData(key);
    /*     */
    /* 477 */ if (key.wizardAction.equalsIgnoreCase(CuramConst.CANCEL_ACTION))
      /* 478 */ cancelWizard(key);
    /*     */
    /*     */
    /* 481 */ if (key.wizardAction.equalsIgnoreCase(CuramConst.FINISH_ACTION)
      && (key.productID != 0L || key.productCategoryID != 0L
        || key.deliveryPatternID != 0L || key.productProvisionID != 0L
        || key.provisionLocationID != 0L || key.creoleProductID != 0L)) {
      /*     */
      /*     */
      /*     */
      /*     */
      /*     */
      /*     */
      /* 488 */ final ProductWizardDatastore dataStore =
        BDMProductWizardDatastoreFactory.newDatastoreInstance();
      /*     */

      final BDMBenefitPageDetails pageDetails =
        new BDMProductWizardDatastoreImpl().readBDMBenefit(key.productID);

      /* 490 */ dataDetails.setBenefitDetails(pageDetails.dtls);
      /* 491 */ dataDetails.setCreoleDetails(
        dataStore/* 492 */ .getCreoleDetails(key.creoleProductID));
      /* 493 */ dataDetails.setProductCategoryTabList(
        dataStore/* 494 */ .getTabListFromDatastore(key.productCategoryID));
      /* 495 */ dataDetails.setDeliveryPatternDetails(dataStore
        /* 496 */ .readDeliveryPattern(key.productID, key.deliveryPatternID));
      /* 497 */ dataDetails.setProductProvisionDetails(
        dataStore/* 498 */ .readProductProvision(key.productID,
          key.productProvisionID));
      /* 499 */ dataDetails.setProvisionLocationDetails(
        dataStore/* 500 */ .readProvisionLocation(key.productID,
          key.provisionLocationID));
      /*     */
      /*     */
      /*     */
      /* 504 */ dataDetails
        .getBenefitDetails().wizardDataForBenefitDtls.eligibilityEntitlementRuleSetNameOpt =
          key.eligibilityEntitlementRuleSetNameOpt;
      /*     */
      /*     */
      /* 507 */ dataDetails
        .getBenefitDetails().wizardDataForBenefitDtls.eligibilityEntitlementRuleSetDescOpt =
          key.eligibilityEntitlementRuleSetDescOpt;
      /*     */
      /*     */
      /* 510 */ final long sucessID = AdminProductWizardFactory.newInstance()
        .createAdminProduct(dataDetails);
      /*     */
      /*     */

      if (dataDetails.isBenefitProduct()) {

        final BDMProductDtls productDtls = new BDMProductDtls();
        productDtls.productID = sucessID;
        productDtls.spsCdoCode = pageDetails.prodDetails.spsCdoCode;
        productDtls.spsProductCode = pageDetails.prodDetails.spsProductCode;

        BDMProductFactory.newInstance().insert(productDtls);
      }

      /* 513 */ if (sucessID != 0L)
        /* 514 */ dataStore.remove(key);
      /*     */
      /*     */
      /*     */ }
    /* 518 */ return dataDetails.getContextData();
    /*     */ }

  @Override
  public WizardData storeBDMBenefitProduct(final BDMBenefitPageDetails key)
    throws AppException, InformationalException {

    /* 255 */ long productID = 0L;
    /* 256 */ long creoleProductID = 0L;
    /*     */
    /* 258 */ if (key.dtls.wizardDataForBenefitDtls.wizardAction
      .equalsIgnoreCase(CuramConst.CANCEL_ACTION))
      /*     */
      /* 260 */ cancelWizard(key.dtls.wizardDataForBenefitDtls);
    /*     */
    /*     */
    /* 263 */ if (key.dtls.wizardDataForBenefitDtls.wizardAction
      .equalsIgnoreCase(CuramConst.NEXT_ACTION)) {
      /*     */
      /*     */
      /* 266 */ final BDMProductWizardDatastoreImpl dataStore =
        new BDMProductWizardDatastoreImpl();
      /*     */
      /* 268 */ productID = dataStore.store(key);
      /*     */
      /*     */
      /*     */
      /*     */
      /* 273 */ if (!key.dtls.creoleProductDtls.reassessmentStrategyType
        .isEmpty() ||
      /* 274 */ !key.dtls.creoleProductDtls.determinationCompStrategyType
        .isEmpty())
        /*     */
        /* 276 */ creoleProductID =
          dataStore.store(key.dtls.creoleProductDtls);
      /*     */
      /*     */
      /*     */
      /*     */
      /*     */
      /*     */
      /*     */
      /*     */ }
    /* 285 */ final WizardData wizardData = new WizardData();
    /*     */
    /* 287 */ wizardData.productID = productID;
    /* 288 */ wizardData.creoleProductID = creoleProductID;
    /* 289 */ wizardData.wizardMenu = CuramConst.kBenefitWizardMenuProperties;
    /* 290 */ return wizardData;
    /*     */ }

  @Override
  public BDMBenefitSummaryPageDetails readBDMBenefitSummary(
    final WizardData key) throws AppException, InformationalException {

    /* 437 */ final BDMBenefitSummaryPageDetails details =
      new BDMBenefitSummaryPageDetails();
    /*     */
    /*     */
    /* 440 */ final BDMProductWizardDatastoreImpl datastore =
      new BDMProductWizardDatastoreImpl();
    /*     */
    /* 442 */ final BDMProductWizardDataDetails dataDetails =
      datastore.readAllBenefitDetails(key);
    /*     */
    /*     */
    /* 445 */ final BenefitPageDetails benefitDetails =
      dataDetails.getBenefitDetails();
    /*     */
    /* 447 */ details.dtls.dynamicBenefitSummaryDtls =
      benefitDetails.dynamicBenefitDtls;
    /* 448 */ details.dtls.benefitSummaryDtls =
      benefitDetails.benefitProductDtls;
    /* 449 */ details.dtls.summaryDeliveryDtls =
      dataDetails.getDeliveryPatternDetails().deliveryPageDtls;
    /* 450 */ details.dtls.provisionSummaryDtls =
      dataDetails.getProductProvisionDtls();
    /* 451 */ details.dtls.locationSummaryDtls =
      dataDetails.getProvisionLocationDtls();
    /* 452 */ details.dtls.categorySummaryDtls =
      dataDetails.getCategoryDetails().categoryList;
    /* 453 */ details.dtls.creoleProductDtls = dataDetails.getCreoleDetails();
    /*     */
    /*     */details.prodDetails = dataDetails.getBdmspsCodes();
    /* 456 */ details.dtls.wizardDataForBenefitSummaryDtls.wizardMenu =
      datastore.getWizardMenu(key.productID);
    /*     */
    /*     */
    /* 459 */ return details;
    /*     */
  }

  @Override
  public BDMBenefitPageDetails readBDMBenefitProduct(final WizardData key)
    throws AppException, InformationalException {

    /* 75 */ BDMBenefitPageDetails benefitDetails =
      new BDMBenefitPageDetails();
    /*     */
    /* 77 */ final ProductWizardDatastore datastore =
      BDMProductWizardDatastoreFactory.newDatastoreInstance();
    /*     */
    /* 79 */ benefitDetails =
      new BDMProductWizardDatastoreImpl().readBDMBenefit(key.productID);
    /* 80 */ benefitDetails.dtls.creoleProductDtls =
      datastore.getCreoleDetails(key.creoleProductID);
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /* 94 */ if (key.eligibilityEntitlementRuleSetNameOpt != null &&
    /* 95 */ !key.eligibilityEntitlementRuleSetNameOpt.isEmpty()) {
      /*     */
      /*     */
      /*     */
      /*     */
      /* 100 */ benefitDetails.dtls.wizardDataForBenefitDtls.eligibilityEntitlementRuleSetNameOpt =
        key.eligibilityEntitlementRuleSetNameOpt;
      /*     */
      /* 102 */ benefitDetails.dtls.wizardDataForBenefitDtls.eligibilityEntitlementRuleSetDescOpt =
        key.eligibilityEntitlementRuleSetDescOpt;
      /*     */
      /*     */
      /*     */
      /*     */
      /*     */
      /*     */
      /*     */
      /*     */ } else {
      /* 111 */ String productName =
        benefitDetails.dtls.dynamicBenefitDtls.productName;
      /*     */
      /*     */
      /* 114 */ productName = productName.replaceAll("\\W", "");
      /*     */
      /*     */
      /*     */
      /*     */
      /* 119 */ final String ruleSetName = productName
        + RuleSetIdentifiers.kRuleSet_EligibilityEntitlementRuleSet;
      /*     */
      /*     */
      /* 122 */ benefitDetails.dtls.wizardDataForBenefitDtls.eligibilityEntitlementRuleSetNameOpt =
        ruleSetName;
      /*     */
      /*     */
      /*     */
      /* 126 */ final String ruleSetDescription =
        benefitDetails.dtls.dynamicBenefitDtls.productName + " "
          + RuleSetIdentifiers.kRuleSet_EligibilityEntitlementRuleSetDescription;
      /*     */
      /*     */
      /*     */
      /* 130 */ benefitDetails.dtls.wizardDataForBenefitDtls.eligibilityEntitlementRuleSetDescOpt =
        ruleSetDescription;
      /*     */
      /*     */
      /*     */
      /*     */ }
    /* 135 */ return benefitDetails;
    /*     */ }

}
