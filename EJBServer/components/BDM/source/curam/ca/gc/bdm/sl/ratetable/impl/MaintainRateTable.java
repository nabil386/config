package curam.ca.gc.bdm.sl.ratetable.impl;

import curam.ca.gc.bdm.codetable.BDMRATETABLEGROUP;
import curam.ca.gc.bdm.entity.financial.fact.BDMCodeTableComboFactory;
import curam.ca.gc.bdm.entity.financial.intf.BDMCodeTableCombo;
import curam.ca.gc.bdm.entity.financial.struct.BDMReadGovernCodeBySubOrdTableDetails;
import curam.ca.gc.bdm.entity.financial.struct.BDMReadGovernCodeBySubOrdTableKey;
import curam.ca.gc.bdm.facade.ratetable.struct.BDMRateHeaderDetailsList;
import curam.ca.gc.bdm.message.BDMRATETABLE;
import curam.ca.gc.bdm.sl.ratetable.struct.BDMRateHeaderData;
import curam.codetable.RATETABLETYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.facade.struct.RateHeaderDetailsList;
import curam.core.sl.entity.fact.RateHeaderFactory;
import curam.core.sl.entity.struct.RateHeaderData;
import curam.core.sl.entity.struct.RateHeaderDtls;
import curam.core.sl.entity.struct.RateHeaderKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import curam.util.type.NotFoundIndicator;
import java.util.Calendar;

public class MaintainRateTable
  implements curam.ca.gc.bdm.sl.ratetable.intf.MaintainRateTable {

  /**
   * If it is a tax rate table, check to make sure that a new rate table is only
   * being created for the upcoming year.
   */
  @Override
  public void validateCreateNewRateTableHeader(final Date effectiveDate,
    final String rateTableType) throws AppException, InformationalException {

    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    final BDMReadGovernCodeBySubOrdTableKey codeTableComboKey =
      new BDMReadGovernCodeBySubOrdTableKey();

    codeTableComboKey.governTableName = BDMRATETABLEGROUP.TABLENAME;
    codeTableComboKey.recordStatusCode = RECORDSTATUS.NORMAL;
    codeTableComboKey.subOrdTableName = RATETABLETYPE.TABLENAME;
    codeTableComboKey.subOrdCode = rateTableType;

    // check if the rate table is part of any rate table groups
    final BDMReadGovernCodeBySubOrdTableDetails governCodeDetails =
      BDMCodeTableComboFactory.newInstance()
        .readGovernCodeBySubOrdTable(nfIndicator, codeTableComboKey);

    // if it is part of the tax rate table group, check that the new table being
    // created has an effective date within the upcoming year.
    if (!nfIndicator.isNotFound()
      && governCodeDetails.governCode.equals(BDMRATETABLEGROUP.TAX)) {
      final Calendar currentDateCal = Date.getCurrentDate().getCalendar();
      final int currentYear = currentDateCal.get(Calendar.YEAR);

      final Calendar effectiveDateCal = effectiveDate.getCalendar();
      final int effectiveYear = effectiveDateCal.get(Calendar.YEAR);

      if (currentYear != effectiveYear && currentYear + 1 != effectiveYear) {
        throw new AppException(BDMRATETABLE.ERR_ADD_TAX_RATE_INVALID_YEAR);
      }
    }

  }

  /**
   * Adds edit restrictions for rate table headers if they are in the tax rate
   * table group and the effective dates are within not the current year or the
   * upcoming one
   */
  @Override
  public BDMRateHeaderDetailsList
    addEditRestrictions(final RateHeaderDetailsList detailsList)
      throws AppException, InformationalException {

    final BDMRateHeaderDetailsList rateHeaderDetailsList =
      new BDMRateHeaderDetailsList();
    rateHeaderDetailsList.assign(detailsList);

    final curam.ca.gc.bdm.sl.ratetable.struct.BDMRateHeaderDetailsList rateHeaderSLDetailsList =
      new curam.ca.gc.bdm.sl.ratetable.struct.BDMRateHeaderDetailsList();

    rateHeaderDetailsList.dtls = rateHeaderSLDetailsList;

    final BDMCodeTableCombo codeTableComboObj =
      BDMCodeTableComboFactory.newInstance();

    for (final RateHeaderData details : detailsList.rateHeaderDetailsList.rateHeaderData) {
      final BDMRateHeaderData bdmRateHeaderData = new BDMRateHeaderData();
      bdmRateHeaderData.rateHeaderData = details;

      final NotFoundIndicator nfIndicator = new NotFoundIndicator();
      final BDMReadGovernCodeBySubOrdTableKey codeTableComboKey =
        new BDMReadGovernCodeBySubOrdTableKey();

      codeTableComboKey.governTableName = BDMRATETABLEGROUP.TABLENAME;
      codeTableComboKey.recordStatusCode = RECORDSTATUS.NORMAL;
      codeTableComboKey.subOrdTableName = RATETABLETYPE.TABLENAME;
      codeTableComboKey.subOrdCode = details.rateTableType;

      // check if the rate table is part of any rate table groups
      final BDMReadGovernCodeBySubOrdTableDetails governCodeDetails =
        codeTableComboObj.readGovernCodeBySubOrdTable(nfIndicator,
          codeTableComboKey);

      // if it is part of tax rate tables, restrict editing/deleting if the
      // effective date is not within the current or upcoming year
      if (!nfIndicator.isNotFound()
        && governCodeDetails.governCode.equals(BDMRATETABLEGROUP.TAX)) {

        final Calendar currentDateCal = Date.getCurrentDate().getCalendar();
        final int currentYear = currentDateCal.get(Calendar.YEAR);

        final Calendar effectiveDateCal = details.effectiveDate.getCalendar();
        final int effectiveYear = effectiveDateCal.get(Calendar.YEAR);

        if (effectiveYear != currentYear
          && effectiveYear != currentYear + 1) {
          bdmRateHeaderData.shouldBlockEditing = true;
        }

      }

      rateHeaderSLDetailsList.dtls.add(bdmRateHeaderData);

    }
    return rateHeaderDetailsList;
  }

  /**
   * Validates that if a tax rate table is being cloned, its effective date is
   * only for the upcoming year
   */
  @Override
  public void validateCloneNewRateTableHeader(final Date effectiveDate,
    final long rateHeaderID) throws AppException, InformationalException {

    final RateHeaderKey rateHeaderKey = new RateHeaderKey();
    rateHeaderKey.rateHeaderID = rateHeaderID;
    final RateHeaderDtls rateHeaderDtls =
      RateHeaderFactory.newInstance().read(rateHeaderKey);

    validateCreateNewRateTableHeader(effectiveDate,
      rateHeaderDtls.rateTableType);

  }

}
