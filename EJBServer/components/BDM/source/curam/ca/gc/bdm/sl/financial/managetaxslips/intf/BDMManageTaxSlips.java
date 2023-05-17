package curam.ca.gc.bdm.sl.financial.managetaxslips.intf;

import com.google.inject.ImplementedBy;
import curam.ca.gc.bdm.entity.financial.struct.BDMSpecificAttributeTextKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMSpecificAttributeValueKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipDataRL1Dtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipDataT4ADtls;
import curam.ca.gc.bdm.facade.struct.BDMEditTaxSlipDetails;
import curam.ca.gc.bdm.facade.struct.BDMEditTaxSlipDisplayDetails;
import curam.ca.gc.bdm.facade.struct.BDMReadVersionNo;
import curam.ca.gc.bdm.facade.struct.BDMTaxSlipDataIDVersionNoKey;
import curam.ca.gc.bdm.facade.struct.BDMTaxSlipDataKey;
import curam.ca.gc.bdm.facade.struct.BDMTaxSlipHistoryList;
import curam.ca.gc.bdm.facade.struct.BDMViewTaxSlipDetailsList;
import curam.ca.gc.bdm.facade.struct.BDMViewTaxSlipInlineDetails;
import curam.ca.gc.bdm.sl.financial.struct.BDMTaxSlipClientDetails;
import curam.ca.gc.bdm.sl.financial.struct.BDMTaxSlipDataForPrint;
import curam.ca.gc.bdm.sl.financial.struct.BDMTaxSlipDataForPrintKey;
import curam.ca.gc.bdm.sl.financial.struct.BDMTaxSlipStoreAttrDataKey;
import curam.ca.gc.bdm.sl.financial.struct.BDMUpdateNextTaxSlipKey;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.struct.ConcernRoleKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;

@ImplementedBy(curam.ca.gc.bdm.sl.financial.managetaxslips.impl.BDMManageTaxSlips.class)
public interface BDMManageTaxSlips {

  public BDMTaxSlipClientDetails
    populateClientDetails(final long concernRoleID, Date yearEndDate)
      throws AppException, InformationalException;

  public void formatT4AClientDetails(final BDMTaxSlipDataT4ADtls t4aDtls);

  public void formatRL1ClientDetails(final BDMTaxSlipDataRL1Dtls rl1Dtls,
    final long concernRoleID) throws AppException, InformationalException;

  public boolean checkQuebecAddressForEndOfTaxYear(final long concernRoleID,
    final Date yearEndDate) throws AppException, InformationalException;

  public BDMViewTaxSlipDetailsList listTaxSlips(ConcernRoleKey key)
    throws AppException, InformationalException;

  public BDMViewTaxSlipInlineDetails viewTaxSlipInline(BDMTaxSlipDataKey key)
    throws AppException, InformationalException;

  public BDMTaxSlipHistoryList listHistoryByTaxSlip(BDMTaxSlipDataKey key)
    throws AppException, InformationalException;

  public BDMEditTaxSlipDisplayDetails readEditTaxSlipDetails(
    BDMTaxSlipDataKey key) throws AppException, InformationalException;

  public void modifyTaxSlipDetails(BDMEditTaxSlipDetails details,
    ActionIDProperty actionIDProperty)
    throws AppException, InformationalException;

  public BDMTaxSlipDataKey amendTaxSlip(BDMTaxSlipDataKey key)
    throws AppException, InformationalException;

  public BDMTaxSlipDataKey createNewTaxSlipFromExisting(BDMTaxSlipDataKey key)
    throws AppException, InformationalException;

  public void createCancelTaxSlipFromExisting(BDMTaxSlipDataKey key)
    throws AppException, InformationalException;

  public void deleteTaxSlip(BDMTaxSlipDataIDVersionNoKey key)
    throws AppException, InformationalException;

  public void issueTaxSlip(BDMTaxSlipDataIDVersionNoKey key)
    throws AppException, InformationalException;

  public void createDuplicateTaxSlip(BDMTaxSlipDataKey key)
    throws AppException, InformationalException;

  public BDMSpecificAttributeValueKey
    readSpecificAttributeText(BDMSpecificAttributeTextKey key)
      throws AppException, InformationalException;

  public void storeAttributeData(BDMTaxSlipStoreAttrDataKey key)
    throws AppException, InformationalException;

  public String formatAttrValueForDisplay(String attrValue, String domainType)
    throws AppException, InformationalException;

  public BDMTaxSlipDataForPrint
    getTaxSlipDataForPrint(BDMTaxSlipDataForPrintKey key)
      throws AppException, InformationalException;

  public BDMReadVersionNo readVersionNo(BDMTaxSlipDataKey key)
    throws AppException, InformationalException;

  public void t4AUpdateNextTaxSlipID(BDMUpdateNextTaxSlipKey key)
    throws AppException, InformationalException;

  public void rl1UpdateNextTaxSlipID(BDMUpdateNextTaxSlipKey key)
    throws AppException, InformationalException;

}
