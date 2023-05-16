package curam.ca.gc.bdm.wizard.datastore.impl;

import curam.ca.gc.bdm.facade.product.struct.BDMSpsCodes;
import curam.core.sl.infrastructure.product.creole.struct.CREOLEProductDtls;
import curam.core.struct.GetProductProvisionDtls;
import curam.core.struct.GetProvisionLocationDtls;
import curam.core.struct.ProductCategoryTabList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.wizard.datastore.impl.ProductWizardDataDetails;
import curam.wizard.facade.struct.BenefitPageDetails;
import curam.wizard.facade.struct.DeliveryPatternPageDetails;
import curam.wizard.facade.struct.LiabilityPageDetails;
import curam.wizard.facade.struct.ProductCategoryPageDetails;
import curam.wizard.facade.struct.ProductProvisionPageDetails;
import curam.wizard.facade.struct.ProvisionLocationPageDetails;
import curam.wizard.facade.struct.WizardData;

public class BDMProductWizardDataDetails implements ProductWizardDataDetails {

  /*     */ long productId;

  /*     */
  /*     */ WizardData contextData;

  /*     */
  /*     */ boolean benefitInd;

  /*     */

  /*     */ boolean creoleProductInd;

  BDMSpsCodes bdmspsCodes;

  public BDMSpsCodes getBdmspsCodes() {

    return this.bdmspsCodes;
  }

  public void setBdmspsCodes(final BDMSpsCodes bdmspsCodes) {

    this.bdmspsCodes = bdmspsCodes;
  }

  /*     */
  /*     */ BenefitPageDetails benefitPageDetails;

  /*     */
  /*     */ CREOLEProductDtls creoleProductDtls;

  /*     */
  /*     */ LiabilityPageDetails liabilityPageDetails;

  /*     */
  /*     */ ProductCategoryPageDetails categoryDetails;

  /*     */
  /*     */ DeliveryPatternPageDetails deliveryPatternDetails;

  /*     */
  /*     */ ProductProvisionPageDetails provisionDetails;

  /*     */
  /*     */ ProvisionLocationPageDetails locationDetails;

  /*     */
  /*     */ GetProductProvisionDtls provisionDtls;

  /*     */
  /*     */ GetProvisionLocationDtls locationDtls;

  /*     */
  /*     */ ProductCategoryTabList productCategoryTabList;

  /*     */
  /*     */ BDMProductWizardDataDetails() {

  }

  /*     */
  /*     */ BDMProductWizardDataDetails(final WizardData wizardData) {

    /* 77 */ setContextData(wizardData);
    /*     */ }

  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */ @Override
  public long getProductID() {

    /* 87 */ return this.productId;
    /*     */ }

  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */ @Override
  public void setProductID(final long productId) {

    /* 97 */ this.productId = productId;
    /*     */ }

  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */ @Override
  public WizardData getContextData() {

    /* 107 */ return this.contextData;
    /*     */ }

  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */ @Override
  public void setContextData(final WizardData contextData) {

    /* 117 */ this.contextData = contextData;
    /*     */ }

  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */ @Override
  public void setBenefitDetails(final BenefitPageDetails details) {

    /* 127 */ this.benefitPageDetails = details;
    /* 128 */ setIsBenefitProduct(true);
    /*     */ }

  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */ @Override
  public void setCreoleDetails(final CREOLEProductDtls details) {

    /* 138 */ this.creoleProductDtls = details;
    /* 139 */ setIsCreoleProduct(true);
    /*     */ }

  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */ @Override
  public boolean isCreoleProduct() {

    /* 149 */ return this.creoleProductInd;
    /*     */ }

  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */ @Override
  public boolean isBenefitProduct() {

    /* 159 */ return this.benefitInd;
    /*     */ }

  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */ @Override
  public void setIsCreoleProduct(final boolean creoleProductInd) {

    /* 169 */ this.creoleProductInd = creoleProductInd;
    /*     */ }

  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */ @Override
  public void setIsBenefitProduct(final boolean benefitInd) {

    /* 179 */ this.benefitInd = benefitInd;
    /*     */ }

  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */ @Override
  public BenefitPageDetails getBenefitDetails() {

    /* 189 */ return this.benefitPageDetails;
    /*     */ }

  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */ @Override
  public CREOLEProductDtls getCreoleDetails() {

    /* 199 */ return this.creoleProductDtls;
    /*     */ }

  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */ @Override
  public LiabilityPageDetails getLiabilityDetails() {

    /* 209 */ return this.liabilityPageDetails;
    /*     */ }

  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */ @Override
  public ProductCategoryPageDetails getCategoryDetails() {

    /* 219 */ return this.categoryDetails;
    /*     */ }

  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */ @Override
  public DeliveryPatternPageDetails getDeliveryPatternDetails()
    throws AppException, InformationalException {

    /* 230 */ return this.deliveryPatternDetails;
    /*     */ }

  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */ @Override
  public ProductProvisionPageDetails getProductProvisionDetails()
    throws AppException, InformationalException {

    /* 241 */ return this.provisionDetails;
    /*     */ }

  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */ @Override
  public ProvisionLocationPageDetails getProvisionLocationDetails()
    throws AppException, InformationalException {

    /* 252 */ return this.locationDetails;
    /*     */ }

  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */ @Override
  public void
    setCategoryDetails(final ProductCategoryPageDetails categoryDetails) {

    /* 263 */ this.categoryDetails = categoryDetails;
    /*     */ }

  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */ @Override
  public void
    setDeliveryPatternDetails(final DeliveryPatternPageDetails details) {

    /* 274 */ this.deliveryPatternDetails = details;
    /*     */ }

  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */ @Override
  public void
    setLiabilityDetails(final LiabilityPageDetails liabilityPageDetails) {

    /* 285 */ this.liabilityPageDetails = liabilityPageDetails;
    /* 286 */ setIsBenefitProduct(false);
    /*     */ }

  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */ @Override
  public void setProductProvisionDetails(
    final ProductProvisionPageDetails provisionDetails) {

    /* 297 */ this.provisionDetails = provisionDetails;
    /*     */ }

  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */ @Override
  public void setProvisionLocationDetails(
    final ProvisionLocationPageDetails locationDetails) {

    /* 308 */ this.locationDetails = locationDetails;
    /*     */ }

  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */ @Override
  public GetProductProvisionDtls getProductProvisionDtls()
    throws AppException, InformationalException {

    /* 319 */ return this.provisionDtls;
    /*     */ }

  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */ @Override
  public GetProvisionLocationDtls getProvisionLocationDtls()
    throws AppException, InformationalException {

    /* 330 */ return this.locationDtls;
    /*     */ }

  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */ @Override
  public void setProductProvisionDtls(final GetProductProvisionDtls dtls) {

    /* 340 */ this.provisionDtls = dtls;
    /*     */ }

  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */ @Override
  public void setProvisionLocationDtls(final GetProvisionLocationDtls dtls) {

    /* 350 */ this.locationDtls = dtls;
    /*     */ }

  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */ @Override
  public ProductCategoryTabList getProductCategoryTabList() {

    /* 360 */ return this.productCategoryTabList;
    /*     */ }

  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */
  /*     */ @Override
  public void setProductCategoryTabList(
    final ProductCategoryTabList productCategoryTabList) {

    /* 371 */ this.productCategoryTabList = productCategoryTabList;
    /*     */ }
  /*     */

}
