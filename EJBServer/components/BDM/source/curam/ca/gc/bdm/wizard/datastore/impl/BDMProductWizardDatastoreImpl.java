package curam.ca.gc.bdm.wizard.datastore.impl;

import curam.ca.gc.bdm.facade.product.struct.BDMBenefitPageDetails;
import curam.codetable.PRODUCTTYPE;
import curam.core.facade.struct.BenefitProductDetails1;
import curam.core.impl.CuramConst;
import curam.core.sl.infrastructure.product.creole.struct.CREOLEProductDtls;
import curam.core.struct.GetProductProvisionDtls;
import curam.core.struct.GetProvisionLocationDtls;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import curam.util.type.Money;
import curam.wizard.datastore.impl.ProductWizardDataDetails;
import curam.wizard.datastore.impl.ProductWizardDataDetailsFactory;
import curam.wizard.datastore.impl.ProductWizardDatastoreImpl;
import curam.wizard.facade.struct.BenefitPageDetails;
import curam.wizard.facade.struct.DeliveryPatternPageDetails;
import curam.wizard.facade.struct.ProductCategoryPageDetails;
import curam.wizard.facade.struct.WizardData;
import curam.wizard.util.impl.CodetableUtil;
import curam.wizard.util.impl.DynamicProductWizardUtil;
import curam.wizard.util.impl.ProductDatastoreUtil;
import curam.wizard.util.impl.ProductExceptionUtil;
import curam.wizard.validator.impl.ProductValidator;

public class BDMProductWizardDatastoreImpl
  extends ProductWizardDatastoreImpl {

  public long store(final BDMBenefitPageDetails details)
    throws InformationalException, AppException {

    /* 96 */ details.dtls.benefitProductDtls.benefitInd = true;
    /* 97 */ details.dtls.benefitProductDtls.caseHomePageName =
      CuramConst.kProductCaseHomePage;
    /*      */
    /*      */
    /* 100 */ final ProductValidator productAssistant =
      new ProductValidator();
    /*      */
    /* 102 */ productAssistant.validate(details.dtls.benefitProductDtls,
      details.dtls.dynamicBenefitDtls.productName,
      details.dtls.dynamicBenefitDtls.productType);
    /*      */
    /*      */
    /* 107 */ ProductExceptionUtil.checkForInformationals();
    /* 108 */ if (!ProductExceptionUtil.hasError())
      /* 109 */ return storeBDMBenefitInDatastore(details);
    /*      */
    /* 111 */ return 0L;
    /*      */ }

  protected long storeBDMBenefitInDatastore(
    final BDMBenefitPageDetails benefitPageDetails) {

    final BenefitProductDetails1 details =
      benefitPageDetails.dtls.benefitProductDtls;
    /* 1340 */ final String productName =
      benefitPageDetails.dtls.dynamicBenefitDtls.productName;
    /* 1341 */ final String productType =
      benefitPageDetails.dtls.dynamicBenefitDtls.productType;
    /* 1342 */ final long benefitDatastoreID =
      benefitPageDetails.dtls.wizardDataForBenefitDtls.productID;
    /*      */
    /* 1344 */ final Datastore datastoreObj =
      ProductDatastoreUtil.openDynamicProduct();
    /* 1345 */ Entity productEntity = null;
    /*      */
    /* 1347 */ if (0L == benefitDatastoreID) {
      /* 1348 */ productEntity =
        datastoreObj.newEntity(CuramConst.kProductEntity);
      /*      */ } else {
      /* 1350 */ productEntity = datastoreObj.readEntity(benefitDatastoreID);
      /*      */
      /*      */ }
    /* 1353 */ if (productEntity != null) {
      /* 1354 */ productEntity.setAttribute("productName", productName);
      /* 1355 */ productEntity.setAttribute("startDate",
        /* 1356 */ DynamicProductWizardUtil.date2String(details.startDate));
      /* 1357 */ productEntity.setAttribute("languageCode",
        details.languageCode);
      /* 1358 */ productEntity.setAttribute("caseHomePageName",
        details.caseHomePageName);
      /* 1359 */ productEntity.setAttribute("myCasesFilterInd",
        /* 1360 */ DynamicProductWizardUtil
          .boolean2string(details.myCasesFilterInd));
      /* 1361 */ productEntity.setAttribute("caseSearchFilterInd",
        /* 1362 */ DynamicProductWizardUtil
          .boolean2string(details.caseSearchFilterInd));
      /* 1363 */ productEntity.setAttribute("endDate",
        /* 1364 */ DynamicProductWizardUtil.date2String(details.endDate));
      /* 1365 */ productEntity.setAttribute("reviewFrequency",
        details.reviewFrequency);
      /* 1366 */ productEntity.setAttribute("orderProductInd",
        /* 1367 */ DynamicProductWizardUtil
          .boolean2string(details.orderProductInd));
      /* 1368 */ productEntity.setAttribute("typeCode", details.typeCode);
      /* 1369 */ productEntity.setAttribute("description",
        details.description);
      /* 1370 */ productEntity.setAttribute("minDeductionAmount",
        /* 1371 */ DynamicProductWizardUtil
          .money2string(details.minDeductionAmount));
      /* 1372 */ productEntity.setAttribute("autoUnderpaymentCaseInd",
        /* 1373 */ DynamicProductWizardUtil
          .boolean2string(details.autoUnderpaymentCaseInd));
      /*      */
      /* 1375 */ productEntity.setAttribute("reassessmentActionType",
        details.reassessmentActionType);
      /*      */
      /*      */
      /* 1378 */ productEntity.setAttribute("adjustmentInd",
        /* 1379 */ DynamicProductWizardUtil
          .boolean2string(details.adjustmentInd));
      /* 1380 */ productEntity.setAttribute("maxDeductionRate",
        /* 1381 */ String.valueOf(details.maxDeductionRate));
      /* 1382 */ productEntity.setAttribute("minimumPmtAmt",
        /* 1383 */ DynamicProductWizardUtil
          .money2string(details.minimumPmtAmt));
      /* 1384 */ productEntity.setAttribute("calculateCostInd",
        /* 1385 */ DynamicProductWizardUtil
          .boolean2string(details.calculateCostInd));
      /* 1386 */ productEntity.setAttribute("certificationFrequency",
        details.certificationFrequency);
      /*      */
      /* 1388 */ productEntity.setAttribute("certGracePeriod",
        /* 1389 */ DynamicProductWizardUtil
          .short2string(details.certGracePeriod));
      /*      */
      /* 1391 */ productEntity.setAttribute("createSecurity",
        details.createSecurity);
      /* 1392 */ productEntity.setAttribute("readSecurity",
        details.readSecurity);
      /* 1393 */ productEntity.setAttribute("maintainSecurity",
        details.maintainSecurity);
      /* 1394 */ productEntity.setAttribute("approveSecurity",
        details.approveSecurity);
      /*      */
      /* 1396 */ productEntity.setAttribute("adjustmentFrquency",
        details.adjustmentFrequency);
      /*      */
      /* 1398 */ productEntity.setAttribute("productProviderInd",
        /* 1399 */ DynamicProductWizardUtil
          .boolean2string(details.productProviderInd));
      /* 1400 */ productEntity.setAttribute("serviceSupplierInd",
        /* 1401 */ DynamicProductWizardUtil
          .boolean2string(details.serviceSupplierInd));
      /* 1402 */ productEntity.setAttribute("informationProviderInd",
        /* 1403 */ DynamicProductWizardUtil
          .boolean2string(details.informationProviderInd));
      /* 1404 */ productEntity.setAttribute("utilityInd",
        /* 1405 */ DynamicProductWizardUtil
          .boolean2string(details.utilityInd));
      /* 1406 */ productEntity.setAttribute("employerInd",
        /* 1407 */ DynamicProductWizardUtil
          .boolean2string(details.employerInd));
      /* 1408 */ productEntity.setAttribute("personInd",
        /* 1409 */ DynamicProductWizardUtil
          .boolean2string(details.personInd));
      /* 1410 */ productEntity.setAttribute("certificationRequiredInd",
        /* 1411 */ DynamicProductWizardUtil
          .boolean2string(details.certificationRequiredInd));
      /*      */
      /* 1413 */ productEntity.setAttribute("creationDate",
        /* 1414 */ DynamicProductWizardUtil
          .date2String(Date.getCurrentDate()));
      /* 1415 */ productEntity.setAttribute("benefitInd",
        /* 1416 */ DynamicProductWizardUtil.boolean2string(true));
      /* 1417 */ productEntity.setAttribute("myCasesFilterInd",
        /* 1418 */ DynamicProductWizardUtil.boolean2string(true));
      /* 1419 */ productEntity.setAttribute("caseSearchFilterInd",
        /* 1420 */ DynamicProductWizardUtil.boolean2string(true));
      /* 1421 */ productEntity.setAttribute("deliveryMaxPeriod",
        /* 1422 */ DynamicProductWizardUtil
          .short2string(details.deliveryMaxPeriod));
      /* 1423 */ productEntity.setAttribute("unitCost",
        /* 1424 */ DynamicProductWizardUtil.money2string(details.unitCost));
      /* 1425 */ productEntity.setAttribute("newTypeCode", productType);
      /*      */
      /* 1427 */ productEntity.setAttribute("programListInd",
        /* 1428 */ DynamicProductWizardUtil
          .boolean2string(details.programListInd));
      /* 1429 */ productEntity.setAttribute("citizenWorkspaceInd",
        /* 1430 */ DynamicProductWizardUtil
          .boolean2string(details.citizenWorkspaceInd));
      /* 1431 */ productEntity.setAttribute("adminTranslationRequiredInd",
        /* 1432 */ DynamicProductWizardUtil.boolean2string(true));
      /* 1433 */ productEntity.setAttribute("ownershipStrategyName",
        details.ownershipStrategyName);
      productEntity.setAttribute("spsProductCode",
        benefitPageDetails.prodDetails.spsProductCode);
      productEntity.setAttribute("spsCdoCode",
        benefitPageDetails.prodDetails.spsCdoCode);

      /*      */
      /*      */
      /* 1436 */ if (0L == benefitDatastoreID) {
        /* 1437 */ productEntity = datastoreObj.addRootEntity(productEntity);
        /*      */ } else {
        /* 1439 */ productEntity.update();
        /*      */ }
      /* 1441 */ return productEntity.getUniqueID();
      /*      */ }
    /* 1443 */ return 0L;
    /*      */ }

  public BDMBenefitPageDetails readBDMBenefit(final long productDatastoreID) {

    final BDMBenefitPageDetails benefitPageDetails =
      new BDMBenefitPageDetails();
    /* 564 */ final BenefitPageDetails details = new BenefitPageDetails();
    /*      */
    /* 566 */ details.benefitProductDtls.startDate = Date.getCurrentDate();
    /* 567 */ details.benefitProductDtls.maxDeductionRate =
      CuramConst.kMaxDeductRate;
    /* 568 */ final Money minDeduction =
      new Money(CuramConst.kMinDeductionAmount);
    /*      */
    /* 570 */ details.benefitProductDtls.minDeductionAmount = minDeduction;
    /*      */
    /* 572 */ if (productDatastoreID != 0L) {
      /*      */
      /* 574 */ final Datastore datastoreObj =
        ProductDatastoreUtil.openDynamicProduct();
      /* 575 */ final Entity productEntity =
        datastoreObj.readEntity(productDatastoreID);
      /*      */
      /* 577 */ if (productEntity != null) {
        /* 578 */ details.dynamicBenefitDtls.productName =
          productEntity.getAttribute("productName");
        /*      */
        /* 580 */ details.benefitProductDtls.languageCode =
          productEntity.getAttribute("languageCode");
        /*      */
        /* 582 */ details.benefitProductDtls.caseHomePageName =
          productEntity.getAttribute("caseHomePageName");
        /*      */
        /* 584 */ details.benefitProductDtls.myCasesFilterInd =
          DynamicProductWizardUtil.booleanValue(
            productEntity/* 585 */ .getAttribute("myCasesFilterInd"));
        /* 586 */ details.benefitProductDtls.caseSearchFilterInd =
          DynamicProductWizardUtil.booleanValue(
            productEntity/* 587 */ .getAttribute("caseSearchFilterInd"));
        /* 588 */ details.benefitProductDtls.startDate =
          DynamicProductWizardUtil
            .dateValue(productEntity/* 589 */ .getAttribute("startDate"));
        /* 590 */ details.benefitProductDtls.endDate =
          DynamicProductWizardUtil
            .dateValue(productEntity/* 591 */ .getAttribute("endDate"));
        /* 592 */ details.benefitProductDtls.reviewFrequency =
          productEntity.getAttribute("reviewFrequency");
        /*      */
        /* 594 */ details.benefitProductDtls.orderProductInd =
          DynamicProductWizardUtil.booleanValue(
            productEntity/* 595 */ .getAttribute("orderProductInd"));
        /*      */
        /* 597 */ details.benefitProductDtls.typeCode =
          productEntity.getAttribute("typeCode");
        /*      */
        /* 599 */ details.benefitProductDtls.description =
          productEntity.getAttribute("description");
        /*      */
        /* 601 */ details.benefitProductDtls.minDeductionAmount =
          DynamicProductWizardUtil.string2money(
            productEntity/* 602 */ .getAttribute("minDeductionAmount"));
        /* 603 */ details.benefitProductDtls.minDeductionAmount =
          DynamicProductWizardUtil.string2money(
            productEntity/* 604 */ .getAttribute("minDeductionAmount"));
        /*      */
        /* 606 */ details.benefitProductDtls.autoUnderpaymentCaseInd =
          DynamicProductWizardUtil.booleanValue(
            productEntity/* 607 */ .getAttribute("autoUnderpaymentCaseInd"));
        /*      */
        /* 609 */ details.benefitProductDtls.reassessmentActionType =
          productEntity.getAttribute("reassessmentActionType");
        /*      */
        /*      */
        /* 612 */ details.benefitProductDtls.adjustmentInd =
          DynamicProductWizardUtil.booleanValue(
            productEntity/* 613 */ .getAttribute("adjustmentInd"));
        /* 614 */ details.benefitProductDtls.maxDeductionRate =
          Double.parseDouble(
            productEntity/* 615 */ .getAttribute("maxDeductionRate"));
        /* 616 */ details.benefitProductDtls.minimumPmtAmt =
          DynamicProductWizardUtil.string2money(
            productEntity/* 617 */ .getAttribute("minimumPmtAmt"));
        /* 618 */ details.benefitProductDtls.calculateCostInd =
          DynamicProductWizardUtil.booleanValue(
            productEntity/* 619 */ .getAttribute("calculateCostInd"));
        /* 620 */ details.benefitProductDtls.certificationFrequency =
          productEntity.getAttribute("certificationFrequency");
        /*      */
        /* 622 */ details.benefitProductDtls.certGracePeriod =
          DynamicProductWizardUtil.shortValue(
            productEntity/* 623 */ .getAttribute("certGracePeriod"));
        /*      */
        /* 625 */ details.benefitProductDtls.createSecurity =
          productEntity.getAttribute("createSecurity");
        /*      */
        /* 627 */ details.benefitProductDtls.readSecurity =
          productEntity.getAttribute("readSecurity");
        /*      */
        /* 629 */ details.benefitProductDtls.maintainSecurity =
          productEntity.getAttribute("maintainSecurity");
        /*      */
        /* 631 */ details.benefitProductDtls.approveSecurity =
          productEntity.getAttribute("approveSecurity");
        /*      */
        /*      */
        /* 634 */ details.benefitProductDtls.adjustmentFrequency =
          productEntity.getAttribute("adjustmentFrquency");
        /*      */
        /* 636 */ details.benefitProductDtls.productProviderInd =
          DynamicProductWizardUtil.booleanValue(
            productEntity/* 637 */ .getAttribute("productProviderInd"));
        /* 638 */ details.benefitProductDtls.serviceSupplierInd =
          DynamicProductWizardUtil.booleanValue(
            productEntity/* 639 */ .getAttribute("serviceSupplierInd"));
        /* 640 */ details.benefitProductDtls.informationProviderInd =
          DynamicProductWizardUtil.booleanValue(
            productEntity/* 641 */ .getAttribute("informationProviderInd"));
        /* 642 */ details.benefitProductDtls.utilityInd =
          DynamicProductWizardUtil
            .booleanValue(productEntity/* 643 */ .getAttribute("utilityInd"));
        /* 644 */ details.benefitProductDtls.employerInd =
          DynamicProductWizardUtil.booleanValue(
            productEntity/* 645 */ .getAttribute("employerInd"));
        /* 646 */ details.benefitProductDtls.personInd =
          DynamicProductWizardUtil
            .booleanValue(productEntity/* 647 */ .getAttribute("personInd"));
        /* 648 */ details.benefitProductDtls.certificationRequiredInd =
          DynamicProductWizardUtil.booleanValue(
            productEntity/* 649 */ .getAttribute("certificationRequiredInd"));
        /* 650 */ details.benefitProductDtls.unitCost =
          DynamicProductWizardUtil
            .string2money(productEntity/* 651 */ .getAttribute("unitCost"));
        /* 652 */ details.benefitProductDtls.deliveryMaxPeriod =
          DynamicProductWizardUtil.shortValue(
            productEntity/* 653 */ .getAttribute("deliveryMaxPeriod"));
        /* 654 */ details.benefitProductDtls.creationDate =
          DynamicProductWizardUtil
            .dateValue(productEntity/* 655 */ .getAttribute("creationDate"));
        /* 656 */ details.benefitProductDtls.benefitInd =
          DynamicProductWizardUtil
            .booleanValue(productEntity/* 657 */ .getAttribute("benefitInd"));
        /* 658 */ details.benefitProductDtls.myCasesFilterInd =
          DynamicProductWizardUtil.booleanValue(
            productEntity/* 659 */ .getAttribute("myCasesFilterInd"));
        /* 660 */ details.benefitProductDtls.caseSearchFilterInd =
          DynamicProductWizardUtil.booleanValue(
            productEntity/* 661 */ .getAttribute("caseSearchFilterInd"));
        /* 662 */ details.benefitProductDtls.productID = productDatastoreID;
        /* 663 */ details.dynamicBenefitDtls.productType =
          productEntity.getAttribute("newTypeCode");
        /*      */
        /*      */
        /* 666 */ details.benefitProductDtls.programListInd =
          DynamicProductWizardUtil.booleanValue(
            productEntity/* 667 */ .getAttribute("programListInd"));
        /* 668 */ details.benefitProductDtls.citizenWorkspaceInd =
          DynamicProductWizardUtil.booleanValue(
            productEntity/* 669 */ .getAttribute("citizenWorkspaceInd"));
        /* 670 */ details.benefitProductDtls.adminTranslationRequiredInd =
          DynamicProductWizardUtil.booleanValue(productEntity
            /* 671 */ .getAttribute("adminTranslationRequiredInd"));
        /* 672 */ details.benefitProductDtls.ownershipStrategyName =
          productEntity.getAttribute("ownershipStrategyName");
        benefitPageDetails.prodDetails.spsCdoCode =
          productEntity.getAttribute("spsCdoCode");
        benefitPageDetails.prodDetails.spsProductCode =
          productEntity.getAttribute("spsProductCode");
        /*      */
        /*      */
        /*      */ }
      /*      */ }
    /* 677 */ details.wizardDataForBenefitDtls.productID = productDatastoreID;
    /* 678 */ details.wizardDataForBenefitDtls.wizardMenu =
      CuramConst.kBenefitWizardMenuProperties;
    benefitPageDetails.dtls = details;

    /* 679 */ return benefitPageDetails;
    /*      */ }
  /*      */

  public ProductWizardDataDetails readAllBDMBenefitDetails(
    final WizardData contextID) throws AppException, InformationalException {

    /* 466 */ final ProductWizardDataDetails dataDetails =
      ProductWizardDataDetailsFactory.newInstance(contextID);
    /*      */
    /*      */
    /* 469 */ final BDMBenefitPageDetails benefitDetails =
      readBDMBenefit(contextID.productID);
    /*      */
    /*      */
    /* 472 */ if (!benefitDetails.dtls.benefitProductDtls.typeCode
      .equalsIgnoreCase(PRODUCTTYPE.OTHER))
      /*      */
      /* 474 */ benefitDetails.dtls.dynamicBenefitDtls.productType =
        CodetableUtil.getCodetableDescription(PRODUCTTYPE.TABLENAME,
          benefitDetails.dtls.benefitProductDtls.typeCode);
    /*      */
    /*      */
    /* 477 */ dataDetails.setBenefitDetails(benefitDetails.dtls);
    /*      */
    /* 479 */ final ProductCategoryPageDetails categoryDetails =
      readCategories(contextID.productID, contextID.productCategoryID);
    /*      */
    /*      */
    /* 482 */ dataDetails.setCategoryDetails(categoryDetails);
    /*      */
    /* 484 */ final DeliveryPatternPageDetails deliveryDetails =
      getDeliveryPatternDetails(contextID.deliveryPatternID,
        contextID.productID);
    /*      */
    /*      */
    /* 487 */ dataDetails.setDeliveryPatternDetails(deliveryDetails);
    /*      */
    /* 489 */ final GetProductProvisionDtls provisionDtls =
      getProductProvisionDetails(contextID.productProvisionID);
    /*      */
    /*      */
    /* 492 */ dataDetails.setProductProvisionDtls(provisionDtls);
    /*      */
    /* 494 */ final GetProvisionLocationDtls locationDtls =
      getProvisionLocation(contextID.provisionLocationID);
    /*      */
    /*      */
    /* 497 */ dataDetails.setProvisionLocationDtls(locationDtls);
    /*      */
    /* 499 */ final CREOLEProductDtls creoleDtls =
      getCreoleDetails(contextID.creoleProductID);
    /*      */
    /*      */
    /* 502 */ dataDetails.setCreoleDetails(creoleDtls);
    /*      */
    /* 504 */ return dataDetails;
    /*      */ }
  /*      */
  /*      */

  @Override
  public BDMProductWizardDataDetails readAllBenefitDetails(
    final WizardData contextID) throws AppException, InformationalException {

    /* 466 */ final BDMProductWizardDataDetails dataDetails =
      new BDMProductWizardDataDetails(contextID);
    /*      */
    /*      */
    /* 469 */ final BDMBenefitPageDetails benefitDetails =
      readBDMBenefit(contextID.productID);
    /*      */
    /*      */
    /* 472 */ if (!benefitDetails.dtls.benefitProductDtls.typeCode
      .equalsIgnoreCase(PRODUCTTYPE.OTHER))
      /*      */
      /* 474 */ benefitDetails.dtls.dynamicBenefitDtls.productType =
        CodetableUtil.getCodetableDescription(PRODUCTTYPE.TABLENAME,
          benefitDetails.dtls.benefitProductDtls.typeCode);
    /*      */
    /*      */
    /* 477 */ dataDetails.setBenefitDetails(benefitDetails.dtls);
    /*      */
    /* 479 */ final ProductCategoryPageDetails categoryDetails =
      readCategories(contextID.productID, contextID.productCategoryID);
    /*      */
    /*      */
    /* 482 */ dataDetails.setCategoryDetails(categoryDetails);
    /*      */
    /* 484 */ final DeliveryPatternPageDetails deliveryDetails =
      getDeliveryPatternDetails(contextID.deliveryPatternID,
        contextID.productID);
    /*      */
    /*      */
    /* 487 */ dataDetails.setDeliveryPatternDetails(deliveryDetails);
    /*      */
    /* 489 */ final GetProductProvisionDtls provisionDtls =
      getProductProvisionDetails(contextID.productProvisionID);
    /*      */
    /*      */
    /* 492 */ dataDetails.setProductProvisionDtls(provisionDtls);
    /*      */
    /* 494 */ final GetProvisionLocationDtls locationDtls =
      getProvisionLocation(contextID.provisionLocationID);
    /*      */
    /*      */
    /* 497 */ dataDetails.setProvisionLocationDtls(locationDtls);
    /*      */
    /* 499 */ final CREOLEProductDtls creoleDtls =
      getCreoleDetails(contextID.creoleProductID);
    /*      */
    /*      */
    /* 502 */ dataDetails.setCreoleDetails(creoleDtls);

    dataDetails.setBdmspsCodes(benefitDetails.prodDetails);

    /*      */
    /* 504 */ return dataDetails;
    /*      */ }

}
