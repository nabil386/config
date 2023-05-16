package curam.ca.gc.bdm.facade.product.impl;

import curam.ca.gc.bdm.entity.fact.BDMProductFactory;
import curam.ca.gc.bdm.entity.struct.BDMProductDtls;
import curam.ca.gc.bdm.entity.struct.BDMProductKey;
import curam.ca.gc.bdm.facade.product.struct.BDMBenefitProductDetails;
import curam.ca.gc.bdm.facade.product.struct.BDMReadBenefitProductDetails;
import curam.core.facade.struct.ReadBenefitProductKey;
import curam.core.struct.ProductKeyStruct;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.NotFoundIndicator;

public class BDMProduct
  extends curam.ca.gc.bdm.facade.product.base.BDMProduct {

  @Override
  public void modifyBDMBenefitProduct1(final ProductKeyStruct key,
    final BDMBenefitProductDetails details)
    throws AppException, InformationalException {

    super.modifyBenefitProduct1(key, details.dtls);

    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();

    final BDMProductKey productKey = new BDMProductKey();
    productKey.productID = key.productID;

    BDMProductDtls productDtls = new BDMProductDtls();
    productDtls =
      BDMProductFactory.newInstance().read(notFoundIndicator, productKey);

    if (!notFoundIndicator.isNotFound()) {
      productDtls.spsCdoCode = details.prodDetail.spsCdoCode;
      productDtls.spsProductCode = details.prodDetail.spsProductCode;
      BDMProductFactory.newInstance().modify(productKey, productDtls);
    } else {
      productDtls = new BDMProductDtls();
      productDtls.productID = key.productID;
      productDtls.spsCdoCode = details.prodDetail.spsCdoCode;
      productDtls.spsProductCode = details.prodDetail.spsProductCode;
      BDMProductFactory.newInstance().insert(productDtls);
    }

  }

  @Override
  public BDMReadBenefitProductDetails
    readBDMBenefitProduct1(final ReadBenefitProductKey key)
      throws AppException, InformationalException {

    final BDMReadBenefitProductDetails readBenefitProductDetails =
      new BDMReadBenefitProductDetails();

    readBenefitProductDetails.dtls = super.readBenefitProduct1(key);

    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();

    final BDMProductKey productKey = new BDMProductKey();
    productKey.productID = key.productID;

    final BDMProductDtls productDtls =
      BDMProductFactory.newInstance().read(notFoundIndicator, productKey);

    if (!notFoundIndicator.isNotFound()) {
      readBenefitProductDetails.prodDetails.spsCdoCode =
        productDtls.spsCdoCode;
      readBenefitProductDetails.prodDetails.spsProductCode =
        productDtls.spsProductCode;
    }
    return readBenefitProductDetails;
  }

}
