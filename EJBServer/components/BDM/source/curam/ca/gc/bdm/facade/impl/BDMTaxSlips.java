package curam.ca.gc.bdm.facade.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.facade.struct.BDMEditTaxSlipDetails;
import curam.ca.gc.bdm.facade.struct.BDMEditTaxSlipDisplayDetails;
import curam.ca.gc.bdm.facade.struct.BDMReadVersionNo;
import curam.ca.gc.bdm.facade.struct.BDMTaxSlipDataIDVersionNoKey;
import curam.ca.gc.bdm.facade.struct.BDMTaxSlipDataKey;
import curam.ca.gc.bdm.facade.struct.BDMTaxSlipHistoryList;
import curam.ca.gc.bdm.facade.struct.BDMViewTaxSlipDetailsList;
import curam.ca.gc.bdm.facade.struct.BDMViewTaxSlipInlineDetails;
import curam.ca.gc.bdm.sl.financial.managetaxslips.intf.BDMManageTaxSlips;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.struct.ConcernRoleKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;

public class BDMTaxSlips extends curam.ca.gc.bdm.facade.base.BDMTaxSlips {

  @Inject
  BDMManageTaxSlips manageTaxSlipsObj;

  public BDMTaxSlips() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public BDMViewTaxSlipDetailsList listTaxSlips(final ConcernRoleKey key)
    throws AppException, InformationalException {

    return manageTaxSlipsObj.listTaxSlips(key);
  }

  @Override
  public BDMViewTaxSlipInlineDetails viewTaxSlipInline(
    final BDMTaxSlipDataKey key) throws AppException, InformationalException {

    // TODO Auto-generated method stub
    return manageTaxSlipsObj.viewTaxSlipInline(key);
  }

  @Override
  public BDMTaxSlipHistoryList listHistoryByTaxSlip(
    final BDMTaxSlipDataKey key) throws AppException, InformationalException {

    // TODO Auto-generated method stub
    return manageTaxSlipsObj.listHistoryByTaxSlip(key);
  }

  @Override
  public BDMEditTaxSlipDisplayDetails readEditTaxSlipDetails(
    final BDMTaxSlipDataKey key) throws AppException, InformationalException {

    // TODO Auto-generated method stub
    return manageTaxSlipsObj.readEditTaxSlipDetails(key);
  }

  @Override
  public void modifyTaxSlipDetails(final BDMEditTaxSlipDetails details,
    final ActionIDProperty actionIDProperty)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub
    manageTaxSlipsObj.modifyTaxSlipDetails(details, actionIDProperty);

  }

  @Override
  public BDMTaxSlipDataKey amendTaxSlip(final BDMTaxSlipDataKey key)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub
    return manageTaxSlipsObj.amendTaxSlip(key);
  }

  @Override
  public BDMTaxSlipDataKey createNewTaxSlipFromExisting(
    final BDMTaxSlipDataKey key) throws AppException, InformationalException {

    // TODO Auto-generated method stub
    return manageTaxSlipsObj.createNewTaxSlipFromExisting(key);
  }

  @Override
  public void createCancelTaxSlipFromExisting(final BDMTaxSlipDataKey key)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub
    manageTaxSlipsObj.createCancelTaxSlipFromExisting(key);

  }

  @Override
  public void deleteTaxSlip(final BDMTaxSlipDataIDVersionNoKey key)
    throws AppException, InformationalException {

    manageTaxSlipsObj.deleteTaxSlip(key);
  }

  @Override
  public void issueTaxSlip(final BDMTaxSlipDataIDVersionNoKey key)
    throws AppException, InformationalException {

    manageTaxSlipsObj.issueTaxSlip(key);

  }

  @Override
  public void createDuplicateTaxSlip(final BDMTaxSlipDataKey key)
    throws AppException, InformationalException {

    manageTaxSlipsObj.createDuplicateTaxSlip(key);
  }

  @Override
  public BDMReadVersionNo readVersionNo(final BDMTaxSlipDataKey key)
    throws AppException, InformationalException {

    return manageTaxSlipsObj.readVersionNo(key);
  }

}
