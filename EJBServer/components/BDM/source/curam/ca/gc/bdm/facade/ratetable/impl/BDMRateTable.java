package curam.ca.gc.bdm.facade.ratetable.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.facade.ratetable.struct.BDMRateHeaderDetailsList;
import curam.core.facade.fact.RateTableFactory;
import curam.core.facade.struct.CloneRateTableKey;
import curam.core.facade.struct.CreateRateTableHeaderDetails;
import curam.core.facade.struct.ListRateTableHistoryKey;
import curam.core.facade.struct.RateHeaderDetailsList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;

/**
 * @author CK
 *
 */
public class BDMRateTable
  extends curam.ca.gc.bdm.facade.ratetable.base.BDMRateTable {

  @Inject
  curam.ca.gc.bdm.sl.ratetable.impl.MaintainRateTable bdmRateTableObj;

  public BDMRateTable() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Add validation of effective date for tax rate tables
   */
  @Override
  public void createRateTableHeader(
    final CreateRateTableHeaderDetails createRateTableHeaderDetails)
    throws AppException, InformationalException {

    bdmRateTableObj.validateCreateNewRateTableHeader(
      createRateTableHeaderDetails.createRateTableHeaderDetails.effectiveDate,
      createRateTableHeaderDetails.createRateTableHeaderDetails.rateTableType);

    RateTableFactory.newInstance()
      .createRateTableHeader(createRateTableHeaderDetails);

  }

  /**
   * Add indicator to prevent modification of tax rate tables with invalid dates
   */
  @Override
  public BDMRateHeaderDetailsList listRateTableHistory(
    final ListRateTableHistoryKey listRateTableHistoryKey)
    throws AppException, InformationalException {

    final RateHeaderDetailsList detailsList = RateTableFactory.newInstance()
      .listRateTableHistory(listRateTableHistoryKey);

    final BDMRateHeaderDetailsList bdmRateHeaderDetailsList =
      bdmRateTableObj.addEditRestrictions(detailsList);
    return bdmRateHeaderDetailsList;
  }

  /**
   * Add validation of effective date for tax rate tables
   */
  @Override
  public void cloneRateTable(final CloneRateTableKey cloneRateTableKey)
    throws AppException, InformationalException {

    bdmRateTableObj.validateCloneNewRateTableHeader(
      cloneRateTableKey.cloneRateTableKey.effectiveDate,
      cloneRateTableKey.cloneRateTableKey.rateHeaderID);

    RateTableFactory.newInstance().cloneRateTable(cloneRateTableKey);

  }

}
