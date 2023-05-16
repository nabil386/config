package curam.ca.gc.bdm.sl.financial.impl;

import curam.ca.gc.bdm.financial.entity.fact.BDMRateCellDAFactory;
import curam.ca.gc.bdm.financial.entity.struct.BDMRateCellByRowRangeDetails;
import curam.ca.gc.bdm.financial.entity.struct.BDMRatesInRowRangeDetails;
import curam.ca.gc.bdm.financial.entity.struct.BDMRatesInRowRangeDetailsList;
import curam.ca.gc.bdm.financial.entity.struct.BDMRatesInRowRangeKey;
import curam.codetable.PROVINCETYPE;
import curam.codetable.RATECOLUMNTYPE;
import curam.codetable.RATEROWTYPE;
import curam.codetable.RATESTATUS;
import curam.codetable.RATETABLETYPE;
import curam.core.sl.entity.fact.RateCellFactory;
import curam.core.sl.entity.intf.RateCell;
import curam.core.sl.entity.struct.RateCellDtls;
import curam.core.sl.entity.struct.RateCellKey;
import curam.core.sl.entity.struct.RateCellValue;
import curam.core.sl.entity.struct.ReadValueByColumnAndRowKey;
import curam.creole.execution.RuleObject;
import curam.creole.execution.session.Session;
import curam.creole.ruleitem.RuleClass;
import curam.creole.value.CodeTableItem;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.RecordNotFoundException;
import curam.util.type.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Statics class to retrieve specific values from tax rate tables
 *
 */
public class TaxCalculatorRates {

  public static Number getFederalTaxRate(final Session session,
    final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();

    final Number annualTaxableIncome = (Number) bdmTaxCalculator
      .getAttributeValue("A_annualTaxableIncome").getValue();

    final double cellValue = getTaxRatesByIncome(taxEffectiveDate,
      annualTaxableIncome.doubleValue(), RATEROWTYPE.BDM_TAXAREA_FEDERAL,
      RATECOLUMNTYPE.BDM_TAX_INCOMERANGE, RATECOLUMNTYPE.BDM_TAX_TAXRATE);

    return new Double(cellValue);
  }

  public static Number getProvincialTaxRate(final Session session,
    final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();
    final Number annualTaxableIncome = (Number) bdmTaxCalculator
      .getAttributeValue("A_annualTaxableIncome").getValue();
    final CodeTableItem province = (CodeTableItem) bdmTaxCalculator
      .getAttributeValue("province").getValue();
    final String taxAreaCode = getTaxAreaByProvince(province.code());

    final double cellValue = getTaxRatesByIncome(taxEffectiveDate,
      annualTaxableIncome.doubleValue(), taxAreaCode,
      RATECOLUMNTYPE.BDM_TAX_INCOMERANGE, RATECOLUMNTYPE.BDM_TAX_TAXRATE);

    return new Double(cellValue);
  }

  public static Number getFederalTaxConstant(final Session session,
    final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();
    final Number annualTaxableIncome = (Number) bdmTaxCalculator
      .getAttributeValue("A_annualTaxableIncome").getValue();

    final double cellValue = getTaxRatesByIncome(taxEffectiveDate,
      annualTaxableIncome.doubleValue(), RATEROWTYPE.BDM_TAXAREA_FEDERAL,
      RATECOLUMNTYPE.BDM_TAX_INCOMERANGE, RATECOLUMNTYPE.BDM_TAX_TAXCONSTANT);

    return new Double(cellValue);
  }

  public static Number getProvincialTaxConstant(final Session session,
    final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();
    final Number annualTaxableIncome = (Number) bdmTaxCalculator
      .getAttributeValue("A_annualTaxableIncome").getValue();
    final CodeTableItem province = (CodeTableItem) bdmTaxCalculator
      .getAttributeValue("province").getValue();
    final String taxAreaCode = getTaxAreaByProvince(province.code());

    final double cellValue = getTaxRatesByIncome(taxEffectiveDate,
      annualTaxableIncome.doubleValue(), taxAreaCode,
      RATECOLUMNTYPE.BDM_TAX_INCOMERANGE, RATECOLUMNTYPE.BDM_TAX_TAXCONSTANT);

    return new Double(cellValue);
  }

  public static Number getFederalTaxRateLowestIncome(final Session session,
    final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();

    final double cellValue = getTaxRatesByIncome(taxEffectiveDate, 0,
      RATEROWTYPE.BDM_TAXAREA_FEDERAL, RATECOLUMNTYPE.BDM_TAX_INCOMERANGE,
      RATECOLUMNTYPE.BDM_TAX_TAXRATE);

    return new Double(cellValue);
  }

  public static Number getProvincialTaxRateLowestIncome(final Session session,
    final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();
    final CodeTableItem province = (CodeTableItem) bdmTaxCalculator
      .getAttributeValue("province").getValue();
    final String taxAreaCode = getTaxAreaByProvince(province.code());

    final double cellValue =
      getTaxRatesByIncome(taxEffectiveDate, 0, taxAreaCode,
        RATECOLUMNTYPE.BDM_TAX_INCOMERANGE, RATECOLUMNTYPE.BDM_TAX_TAXRATE);

    return new Double(cellValue);
  }

  protected static double getTaxRatesByIncome(final Date taxEffectiveDate,
    final double annualTaxableIncome, final String rateRowType,
    final String rangeColumnType, final String rateColumnType)
    throws AppException, InformationalException {

    double cellValue = 0;

    try {
      final BDMRatesInRowRangeKey cellByRangeKey =
        new BDMRatesInRowRangeKey();
      cellByRangeKey.effectiveDate = taxEffectiveDate;
      cellByRangeKey.rateStatus = RATESTATUS.ACTIVE;
      cellByRangeKey.rateTableType = RATETABLETYPE.BDM_TAXRATESBYINCOME;
      cellByRangeKey.rangeColumnType = rangeColumnType;
      cellByRangeKey.rangeValue = annualTaxableIncome;
      cellByRangeKey.rateCellColumnType = rateColumnType;
      cellByRangeKey.rateRowType = rateRowType;

      final BDMRateCellByRowRangeDetails cellByRangeDetails =
        BDMRateCellDAFactory.newInstance()
          .readRateCellByRowRange(cellByRangeKey);

      cellValue = cellByRangeDetails.rateCellValue;
    } catch (final RecordNotFoundException rfe) {
      rfe.printStackTrace();
    }

    return cellValue;
  }

  public static Number getTD1FederalBasicPersonal(final Session session,
    final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();

    final double cellValue =
      getTD1Value(taxEffectiveDate, RATEROWTYPE.BDM_TAXAREA_FEDERAL,
        RATECOLUMNTYPE.BDM_TD1_BASICPERSONALAMOUNT);

    return new Double(cellValue);
  }

  public static Number getTD1FederalAgeAmount(final Session session,
    final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();

    final double cellValue = getTD1Value(taxEffectiveDate,
      RATEROWTYPE.BDM_TAXAREA_FEDERAL, RATECOLUMNTYPE.BDM_TD1_AGEAMOUNT);

    return new Double(cellValue);
  }

  public static Number getTD1FederalSpouseInLawAmount(final Session session,
    final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();

    final double cellValue =
      getTD1Value(taxEffectiveDate, RATEROWTYPE.BDM_TAXAREA_FEDERAL,
        RATECOLUMNTYPE.BDM_TD1_SPOUSEINLAWAMOUNT);

    return new Double(cellValue);
  }

  public static Number getTD1ProvincialBasicPersonal(final Session session,
    final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();
    final CodeTableItem province = (CodeTableItem) bdmTaxCalculator
      .getAttributeValue("province").getValue();
    final String taxAreaCode = getTaxAreaByProvince(province.code());

    final double cellValue = getTD1Value(taxEffectiveDate, taxAreaCode,
      RATECOLUMNTYPE.BDM_TD1_BASICPERSONALAMOUNT);

    return new Double(cellValue);
  }

  public static Number getTD1ProvincialBasicPersonalBelowThreshold(
    final Session session, final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();
    final CodeTableItem province = (CodeTableItem) bdmTaxCalculator
      .getAttributeValue("province").getValue();
    final Number annualTaxableIncome = (Number) bdmTaxCalculator
      .getAttributeValue("A_annualTaxableIncome").getValue();
    final String taxAreaCode = getTaxAreaByProvince(province.code());

    final double cellValue = getTD1ValueByThreshold(taxEffectiveDate,
      taxAreaCode, RATECOLUMNTYPE.BDM_TD1_BASICPERSONALAMOUNT_SEC,
      annualTaxableIncome.doubleValue());

    return new Double(cellValue);
  }

  public static Number getTD1ProvincialAgeAmount(final Session session,
    final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();
    final CodeTableItem province = (CodeTableItem) bdmTaxCalculator
      .getAttributeValue("province").getValue();
    final String taxAreaCode = getTaxAreaByProvince(province.code());

    final double cellValue = getTD1Value(taxEffectiveDate, taxAreaCode,
      RATECOLUMNTYPE.BDM_TD1_AGEAMOUNT);

    return new Double(cellValue);
  }

  public static Number getTD1ProvincialAgeAmountBelowThreshold(
    final Session session, final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();
    final CodeTableItem province = (CodeTableItem) bdmTaxCalculator
      .getAttributeValue("province").getValue();
    final Number annualTaxableIncome = (Number) bdmTaxCalculator
      .getAttributeValue("A_annualTaxableIncome").getValue();
    final String taxAreaCode = getTaxAreaByProvince(province.code());

    final double cellValue = getTD1ValueByThreshold(taxEffectiveDate,
      taxAreaCode, RATECOLUMNTYPE.BDM_TD1_AGEAMOUNT_SEC,
      annualTaxableIncome.doubleValue());

    return new Double(cellValue);
  }

  public static Number getTD1ProvincialSpouseInLawAmount(
    final Session session, final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();
    final CodeTableItem province = (CodeTableItem) bdmTaxCalculator
      .getAttributeValue("province").getValue();
    final String taxAreaCode = getTaxAreaByProvince(province.code());

    final double cellValue = getTD1Value(taxEffectiveDate, taxAreaCode,
      RATECOLUMNTYPE.BDM_TD1_SPOUSEINLAWAMOUNT);

    return new Double(cellValue);
  }

  public static Number getTD1ProvincialSpouseInLawAmountBelowThreshold(
    final Session session, final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();
    final CodeTableItem province = (CodeTableItem) bdmTaxCalculator
      .getAttributeValue("province").getValue();
    final Number annualTaxableIncome = (Number) bdmTaxCalculator
      .getAttributeValue("A_annualTaxableIncome").getValue();
    final String taxAreaCode = getTaxAreaByProvince(province.code());

    final double cellValue = getTD1ValueByThreshold(taxEffectiveDate,
      taxAreaCode, RATECOLUMNTYPE.BDM_TD1_SPOUSEINLAWAMOUNT_SEC,
      annualTaxableIncome.doubleValue());

    return new Double(cellValue);
  }

  public static Number getTD1ProvincialChildAmount(final Session session,
    final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();
    final CodeTableItem province = (CodeTableItem) bdmTaxCalculator
      .getAttributeValue("province").getValue();
    final String taxAreaCode = getTaxAreaByProvince(province.code());

    final double cellValue = getTD1Value(taxEffectiveDate, taxAreaCode,
      RATECOLUMNTYPE.BDM_TD1_CHILDAMOUNT);

    return new Double(cellValue);
  }

  public static Number getTD1ProvincialChildAmountBelowThreshold(
    final Session session, final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();
    final CodeTableItem province = (CodeTableItem) bdmTaxCalculator
      .getAttributeValue("province").getValue();
    final Number annualTaxableIncome = (Number) bdmTaxCalculator
      .getAttributeValue("A_annualTaxableIncome").getValue();
    final String taxAreaCode = getTaxAreaByProvince(province.code());

    final double cellValue = getTD1ValueByThreshold(taxEffectiveDate,
      taxAreaCode, RATECOLUMNTYPE.BDM_TD1_CHILDAMOUNT_SEC,
      annualTaxableIncome.doubleValue());

    return new Double(cellValue);
  }

  public static Number getOTFFederalBasicAmount(final Session session,
    final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();

    final double cellValue =
      getOTFValue(taxEffectiveDate, RATEROWTYPE.BDM_TAXAREA_FEDERAL,
        RATECOLUMNTYPE.BDM_OTHERTAX_BASICAMOUNT);

    return new Double(cellValue);
  }

  public static Number getOTFFederalIndexRate(final Session session,
    final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();

    final double cellValue = getOTFValue(taxEffectiveDate,
      RATEROWTYPE.BDM_TAXAREA_FEDERAL, RATECOLUMNTYPE.BDM_OTHERTAX_INDEXRATE);

    return new Double(cellValue);
  }

  public static Number getOTFFederalLCPRate(final Session session,
    final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();

    final double cellValue = getOTFValue(taxEffectiveDate,
      RATEROWTYPE.BDM_TAXAREA_FEDERAL, RATECOLUMNTYPE.BDM_OTHERTAX_LCPRATE);

    return new Double(cellValue);
  }

  public static Number getOTFFederalLCPAmount(final Session session,
    final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();

    final double cellValue = getOTFValue(taxEffectiveDate,
      RATEROWTYPE.BDM_TAXAREA_FEDERAL, RATECOLUMNTYPE.BDM_OTHERTAX_LCPAMOUNT);

    return new Double(cellValue);
  }

  public static Number getOTFFederalCEA(final Session session,
    final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();

    final double cellValue = getOTFValue(taxEffectiveDate,
      RATEROWTYPE.BDM_TAXAREA_FEDERAL, RATECOLUMNTYPE.BDM_OTHERTAX_CEA);

    return new Double(cellValue);
  }

  public static Number getOTFFederalS(final Session session,
    final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();

    final double cellValue = getOTFValue(taxEffectiveDate,
      RATEROWTYPE.BDM_TAXAREA_FEDERAL, RATECOLUMNTYPE.BDM_OTHERTAX_S);

    return new Double(cellValue);
  }

  public static Number getOTFFederalAbatement(final Session session,
    final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();

    final double cellValue = getOTFValue(taxEffectiveDate,
      RATEROWTYPE.BDM_TAXAREA_FEDERAL, RATECOLUMNTYPE.BDM_OTHERTAX_ABATEMENT);

    return new Double(cellValue);
  }

  public static Number getOTFFederalSurtax(final Session session,
    final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();

    final double cellValue = getOTFValue(taxEffectiveDate,
      RATEROWTYPE.BDM_TAXAREA_FEDERAL, RATECOLUMNTYPE.BDM_OTHERTAX_SURTAX);

    return new Double(cellValue);
  }

  public static Number getOTFProvincialBasicAmount(final Session session,
    final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();
    final CodeTableItem province = (CodeTableItem) bdmTaxCalculator
      .getAttributeValue("province").getValue();
    final String taxAreaCode = getTaxAreaByProvince(province.code());

    final double cellValue = getOTFValue(taxEffectiveDate, taxAreaCode,
      RATECOLUMNTYPE.BDM_OTHERTAX_BASICAMOUNT);

    return new Double(cellValue);
  }

  public static Number getOTFProvincialIndexRate(final Session session,
    final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();
    final CodeTableItem province = (CodeTableItem) bdmTaxCalculator
      .getAttributeValue("province").getValue();
    final String taxAreaCode = getTaxAreaByProvince(province.code());

    final double cellValue = getOTFValue(taxEffectiveDate, taxAreaCode,
      RATECOLUMNTYPE.BDM_OTHERTAX_INDEXRATE);

    return new Double(cellValue);
  }

  public static Number getOTFProvincialLCPRate(final Session session,
    final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();
    final CodeTableItem province = (CodeTableItem) bdmTaxCalculator
      .getAttributeValue("province").getValue();
    final String taxAreaCode = getTaxAreaByProvince(province.code());

    final double cellValue = getOTFValue(taxEffectiveDate, taxAreaCode,
      RATECOLUMNTYPE.BDM_OTHERTAX_LCPRATE);

    return new Double(cellValue);
  }

  public static Number getOTFProvincialLCPAmount(final Session session,
    final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();
    final CodeTableItem province = (CodeTableItem) bdmTaxCalculator
      .getAttributeValue("province").getValue();
    final String taxAreaCode = getTaxAreaByProvince(province.code());

    final double cellValue = getOTFValue(taxEffectiveDate, taxAreaCode,
      RATECOLUMNTYPE.BDM_OTHERTAX_LCPAMOUNT);

    return new Double(cellValue);
  }

  public static Number getOTFProvincialCEA(final Session session,
    final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();
    final CodeTableItem province = (CodeTableItem) bdmTaxCalculator
      .getAttributeValue("province").getValue();
    final String taxAreaCode = getTaxAreaByProvince(province.code());

    final double cellValue = getOTFValue(taxEffectiveDate, taxAreaCode,
      RATECOLUMNTYPE.BDM_OTHERTAX_CEA);

    return new Double(cellValue);
  }

  public static Number getOTFProvincialS(final Session session,
    final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();
    final CodeTableItem province = (CodeTableItem) bdmTaxCalculator
      .getAttributeValue("province").getValue();
    final String taxAreaCode = getTaxAreaByProvince(province.code());

    final double cellValue = getOTFValue(taxEffectiveDate, taxAreaCode,
      RATECOLUMNTYPE.BDM_OTHERTAX_S);

    return new Double(cellValue);
  }

  public static Number getOTFProvincialAbatement(final Session session,
    final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();
    final CodeTableItem province = (CodeTableItem) bdmTaxCalculator
      .getAttributeValue("province").getValue();
    final String taxAreaCode = getTaxAreaByProvince(province.code());

    final double cellValue = getOTFValue(taxEffectiveDate, taxAreaCode,
      RATECOLUMNTYPE.BDM_OTHERTAX_ABATEMENT);

    return new Double(cellValue);
  }

  public static Number getOTFProvincialSurtax(final Session session,
    final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();
    final CodeTableItem province = (CodeTableItem) bdmTaxCalculator
      .getAttributeValue("province").getValue();
    final String taxAreaCode = getTaxAreaByProvince(province.code());

    final double cellValue = getOTFValue(taxEffectiveDate, taxAreaCode,
      RATECOLUMNTYPE.BDM_OTHERTAX_SURTAX);

    return new Double(cellValue);
  }

  public static List<RuleObject> getV1ProvincialSurTaxRatesInRange(
    final Session session, final RuleObject bdmTaxCalculator,
    final Number T4Value) throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();
    final CodeTableItem province = (CodeTableItem) bdmTaxCalculator
      .getAttributeValue("province").getValue();
    final String taxAreaCode = getTaxAreaByProvince(province.code());

    final BDMRatesInRowRangeDetailsList detailsList =
      getV1RatesInRange(taxEffectiveDate, T4Value.doubleValue(), taxAreaCode,
        RATECOLUMNTYPE.BDM_PROVSURTAX_INCOMERANGE,
        RATECOLUMNTYPE.BDM_PROVSURTAX_SURTAXRATE);

    final List<RuleObject> list = new ArrayList<RuleObject>();

    final RuleClass ruleClass = bdmTaxCalculator.getRuleClass()
      .owningRuleSet().findClass("ProvincialSurtaxInRange");
    RuleObject provincialSurtaxObj = null;
    for (final BDMRatesInRowRangeDetails rowRangeDetails : detailsList.dtls) {
      provincialSurtaxObj = session.createRuleObject(ruleClass);
      provincialSurtaxObj.getAttributeValue("lowerBound")
        .specifyValue(new Double(rowRangeDetails.rateCellMin));
      provincialSurtaxObj.getAttributeValue("upperBound")
        .specifyValue(new Double(rowRangeDetails.rateCellMax));
      provincialSurtaxObj.getAttributeValue("surtaxRate")
        .specifyValue(new Double(rowRangeDetails.rateCellValue));
      list.add(provincialSurtaxObj);
    }

    return list;
  }

  /*
   * fallback option.
   * public static Number getV1ProvincialSurTaxRateInRange(Session session,
   * RuleObject bdmTaxCalculator, Number T4Value)
   * throws AppException, InformationalException {
   *
   * Date taxEffectiveDate =
   * (Date)bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();
   * CodeTableItem province =
   * (CodeTableItem)bdmTaxCalculator.getAttributeValue("province").getValue();
   * String taxAreaCode = getTaxAreaByProvince(province.code());
   * double T4_annualProvincialBasicTax = T4Value.doubleValue();
   *
   * RatesInRowRangeDetailsList detailsList =
   * getV1RatesInRange(taxEffectiveDate, T4_annualProvincialBasicTax,
   * taxAreaCode,
   * RATECOLUMNTYPE.BDM_PROVSURTAX_INCOMERANGE,
   * RATECOLUMNTYPE.BDM_PROVSURTAX_SURTAXRATE);
   *
   * double V1_ProvincialSurtax = 0;
   * for (RatesInRowRangeDetails rowRangeDetails : detailsList.dtls) {
   * V1_ProvincialSurtax +=
   * (T4_annualProvincialBasicTax-rowRangeDetails.rateCellMin)*rowRangeDetails.
   * rateCellValue/100;
   * }
   *
   * return new Double(V1_ProvincialSurtax);
   * }
   */
  protected static BDMRatesInRowRangeDetailsList getV1RatesInRange(
    final Date taxEffectiveDate, final double rangeValue,
    final String rateRowType, final String rangeColumnType,
    final String rateColumnType) throws AppException, InformationalException {

    final BDMRatesInRowRangeKey ratesInRowRangeKey =
      new BDMRatesInRowRangeKey();
    ratesInRowRangeKey.effectiveDate = taxEffectiveDate;
    ratesInRowRangeKey.rateStatus = RATESTATUS.ACTIVE;
    ratesInRowRangeKey.rateTableType = RATETABLETYPE.BDM_PROVINCIALSURTAX;
    ratesInRowRangeKey.rangeColumnType = rangeColumnType;
    ratesInRowRangeKey.rangeValue = rangeValue;
    ratesInRowRangeKey.rateCellColumnType = rateColumnType;
    ratesInRowRangeKey.rateRowType = rateRowType;

    final BDMRatesInRowRangeDetailsList detailsList = BDMRateCellDAFactory
      .newInstance().searchRatesInRowRange(ratesInRowRangeKey);

    return detailsList;
  }

  protected static double getTD1Value(final Date taxEffectiveDate,
    final String rateRowType, final String rateColumnType)
    throws AppException, InformationalException {

    double cellValue = 0;

    try {
      final ReadValueByColumnAndRowKey readValueByColumnAndRowKey =
        new ReadValueByColumnAndRowKey();
      readValueByColumnAndRowKey.effectiveDate = taxEffectiveDate;
      readValueByColumnAndRowKey.rateStatus = RATESTATUS.ACTIVE;
      readValueByColumnAndRowKey.rateTableType = RATETABLETYPE.BDM_TD1RATES;
      readValueByColumnAndRowKey.rateRowType = rateRowType;
      readValueByColumnAndRowKey.rateColumnType = rateColumnType;

      final RateCellValue rateCellValue = BDMRateCellDAFactory.newInstance()
        .readCustomValueByColumnAndRow(readValueByColumnAndRowKey);

      cellValue = rateCellValue.rateCellValue;
    } catch (final RecordNotFoundException rfe) {
      rfe.printStackTrace();
    }

    return cellValue;
  }

  protected static double getTD1ValueByThreshold(final Date taxEffectiveDate,
    final String rateRowType, final String rateColumnType,
    final double thresholdAmount)
    throws AppException, InformationalException {

    double cellValue = 0;

    try {
      final RateCell rateCellObj = RateCellFactory.newInstance();
      final ReadValueByColumnAndRowKey readValueByColumnAndRowKey =
        new ReadValueByColumnAndRowKey();
      readValueByColumnAndRowKey.effectiveDate = taxEffectiveDate;
      readValueByColumnAndRowKey.rateStatus = RATESTATUS.ACTIVE;
      readValueByColumnAndRowKey.rateTableType = RATETABLETYPE.BDM_TD1RATES;
      readValueByColumnAndRowKey.rateRowType = rateRowType;
      readValueByColumnAndRowKey.rateColumnType = rateColumnType;

      final RateCellValue rateCellValue = BDMRateCellDAFactory.newInstance()
        .readCustomValueByColumnAndRow(readValueByColumnAndRowKey);
      // TODO: convert into one sql
      final RateCellKey rateCellKey = new RateCellKey();
      rateCellKey.rateCellID = rateCellValue.rateCellID;
      final RateCellDtls rateCellDtls = rateCellObj.read(rateCellKey);

      if (rateCellDtls.anyMaximumInd
        && thresholdAmount < rateCellDtls.rateCellMax) {
        cellValue = rateCellDtls.rateCellValue;
      }
    } catch (final RecordNotFoundException rfe) {
      rfe.printStackTrace();
    }

    return cellValue;
  }

  private static String getTaxAreaByProvince(final String province)
    throws AppException, InformationalException {

    String taxAreaCode = "";
    if (PROVINCETYPE.BRITISHCOLUMBIA.equals(province)) {
      taxAreaCode = RATEROWTYPE.BDM_TAXAREA_BRITISHCOLUMBIA;
    } else if (PROVINCETYPE.NOVASCOTIA.equals(province)) {
      taxAreaCode = RATEROWTYPE.BDM_TAXAREA_NOVASCOTIA;
    } else if (PROVINCETYPE.SASKATCHEWAN.equals(province)) {
      taxAreaCode = RATEROWTYPE.BDM_TAXAREA_SASKATCHEWAN;
    } else if (PROVINCETYPE.ALBERTA.equals(province)) {
      taxAreaCode = RATEROWTYPE.BDM_TAXAREA_ALBERTA;
    } else if (PROVINCETYPE.MANITOBA.equals(province)) {
      taxAreaCode = RATEROWTYPE.BDM_TAXAREA_MANITOBA;
    } else if (PROVINCETYPE.NEWBRUNSWICK.equals(province)) {
      taxAreaCode = RATEROWTYPE.BDM_TAXAREA_NEWBRUNSWICK;
    } else if (PROVINCETYPE.NEWFOUNDLANDANDLABRADOR.equals(province)) {
      taxAreaCode = RATEROWTYPE.BDM_TAXAREA_NEWFOUNDLAND;
    } else if (PROVINCETYPE.NORTHWESTTERRITORIES.equals(province)) {
      taxAreaCode = RATEROWTYPE.BDM_TAXAREA_NWT;
    } else if (PROVINCETYPE.NUNAVUT.equals(province)) {
      taxAreaCode = RATEROWTYPE.BDM_TAXAREA_NUNAVUT;
    } else if (PROVINCETYPE.ONTARIO.equals(province)) {
      taxAreaCode = RATEROWTYPE.BDM_TAXAREA_ONTARIO;
    } else if (PROVINCETYPE.PRINCEEDWARDISLAND.equals(province)) {
      taxAreaCode = RATEROWTYPE.BDM_TAXAREA_PEI;
    } else if (PROVINCETYPE.YUKON.equals(province)) {
      taxAreaCode = RATEROWTYPE.BDM_TAXAREA_YUKON;
    } else if (PROVINCETYPE.QUEBEC.equals(province)) {
      taxAreaCode = RATEROWTYPE.BDM_TAXAREA_QUEBEC;
    } else {
      taxAreaCode = RATEROWTYPE.BDM_TAXAREA_OUTSIDECANADA;
    }

    return taxAreaCode;
  }

  protected static double getOTFValue(final Date taxEffectiveDate,
    final String rateRowType, final String rateColumnType)
    throws AppException, InformationalException {

    double cellValue = 0;

    try {
      final ReadValueByColumnAndRowKey readValueByColumnAndRowKey =
        new ReadValueByColumnAndRowKey();
      readValueByColumnAndRowKey.effectiveDate = taxEffectiveDate;
      readValueByColumnAndRowKey.rateStatus = RATESTATUS.ACTIVE;
      readValueByColumnAndRowKey.rateTableType =
        RATETABLETYPE.BDM_OTHERTAXFACTORS;
      readValueByColumnAndRowKey.rateRowType = rateRowType;
      readValueByColumnAndRowKey.rateColumnType = rateColumnType;

      final RateCellValue rateCellValue = RateCellFactory.newInstance()
        .readValueByColumnAndRow(readValueByColumnAndRowKey);

      cellValue = rateCellValue.rateCellValue;
    } catch (final RecordNotFoundException rfe) {
      rfe.printStackTrace();
    }

    return cellValue;
  }

  public static Number getV2ProvincialHealthPremiumV2Min(
    final Session session, final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();
    final Number annualTaxableIncome = (Number) bdmTaxCalculator
      .getAttributeValue("A_annualTaxableIncome").getValue();
    final CodeTableItem province = (CodeTableItem) bdmTaxCalculator
      .getAttributeValue("province").getValue();
    final String taxAreaCode = getTaxAreaByProvince(province.code());

    final double cellValue =
      getV2ByIncome(taxEffectiveDate, annualTaxableIncome.doubleValue(),
        taxAreaCode, RATECOLUMNTYPE.BDM_HEALTHPREMIUM_INCOMERANGE,
        RATECOLUMNTYPE.BDM_HEALTHPREMIUM_V2MIN);

    return new Double(cellValue);
  }

  public static Number getV2ProvincialHealthPremiumV2Base(
    final Session session, final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();
    final Number annualTaxableIncome = (Number) bdmTaxCalculator
      .getAttributeValue("A_annualTaxableIncome").getValue();
    final CodeTableItem province = (CodeTableItem) bdmTaxCalculator
      .getAttributeValue("province").getValue();
    final String taxAreaCode = getTaxAreaByProvince(province.code());

    final double cellValue =
      getV2ByIncome(taxEffectiveDate, annualTaxableIncome.doubleValue(),
        taxAreaCode, RATECOLUMNTYPE.BDM_HEALTHPREMIUM_INCOMERANGE,
        RATECOLUMNTYPE.BDM_HEALTHPREMIUM_V2BASE);

    return new Double(cellValue);
  }

  public static Number getV2ProvincialHealthPremiumV2Rate(
    final Session session, final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();
    final Number annualTaxableIncome = (Number) bdmTaxCalculator
      .getAttributeValue("A_annualTaxableIncome").getValue();
    final CodeTableItem province = (CodeTableItem) bdmTaxCalculator
      .getAttributeValue("province").getValue();
    final String taxAreaCode = getTaxAreaByProvince(province.code());

    final double cellValue =
      getV2ByIncome(taxEffectiveDate, annualTaxableIncome.doubleValue(),
        taxAreaCode, RATECOLUMNTYPE.BDM_HEALTHPREMIUM_INCOMERANGE,
        RATECOLUMNTYPE.BDM_HEALTHPREMIUM_V2RATE);

    return new Double(cellValue);
  }

  protected static double getV2ByIncome(final Date taxEffectiveDate,
    final double annualTaxableIncome, final String rateRowType,
    final String rangeColumnType, final String rateColumnType)
    throws AppException, InformationalException {

    double cellValue = 0;

    try {
      final BDMRatesInRowRangeKey cellByRangeKey =
        new BDMRatesInRowRangeKey();
      cellByRangeKey.effectiveDate = taxEffectiveDate;
      cellByRangeKey.rateStatus = RATESTATUS.ACTIVE;
      cellByRangeKey.rateTableType = RATETABLETYPE.BDM_HEALTHPREMIUM;
      cellByRangeKey.rangeColumnType = rangeColumnType;
      cellByRangeKey.rangeValue = annualTaxableIncome;
      cellByRangeKey.rateCellColumnType = rateColumnType;
      cellByRangeKey.rateRowType = rateRowType;

      final BDMRateCellByRowRangeDetails cellByRangeDetails =
        BDMRateCellDAFactory.newInstance()
          .readRateCellByRowRange(cellByRangeKey);

      cellValue = cellByRangeDetails.rateCellValue;
    } catch (final RecordNotFoundException rfe) {
      rfe.printStackTrace();
    }

    return cellValue;
  }

  public static Number getV2ProvincialHealthPremiumLowerBound(
    final Session session, final RuleObject bdmTaxCalculator)
    throws AppException, InformationalException {

    final Date taxEffectiveDate =
      (Date) bdmTaxCalculator.getAttributeValue("effectiveDate").getValue();
    final Number annualTaxableIncome = (Number) bdmTaxCalculator
      .getAttributeValue("A_annualTaxableIncome").getValue();
    final CodeTableItem province = (CodeTableItem) bdmTaxCalculator
      .getAttributeValue("province").getValue();
    final String taxAreaCode = getTaxAreaByProvince(province.code());

    final double cellValue = getV2LowerBoundByIncome(taxEffectiveDate,
      annualTaxableIncome.doubleValue(), taxAreaCode,
      RATECOLUMNTYPE.BDM_HEALTHPREMIUM_INCOMERANGE);

    return new Double(cellValue);
  }

  protected static double getV2LowerBoundByIncome(final Date taxEffectiveDate,
    final double annualTaxableIncome, final String rateRowType,
    final String rangeColumnType)
    throws AppException, InformationalException {

    double cellValue = 0;

    try {
      final BDMRatesInRowRangeKey cellByRangeKey =
        new BDMRatesInRowRangeKey();
      cellByRangeKey.effectiveDate = taxEffectiveDate;
      cellByRangeKey.rateStatus = RATESTATUS.ACTIVE;
      cellByRangeKey.rateTableType = RATETABLETYPE.BDM_HEALTHPREMIUM;
      cellByRangeKey.rangeColumnType = rangeColumnType;
      cellByRangeKey.rangeValue = annualTaxableIncome;
      cellByRangeKey.rateRowType = rateRowType;

      final BDMRateCellByRowRangeDetails cellByRangeDetails =
        BDMRateCellDAFactory.newInstance()
          .readRateCellLowerBoundByRowRange(cellByRangeKey);

      cellValue = cellByRangeDetails.rateCellValue;
    } catch (final RecordNotFoundException rfe) {
      rfe.printStackTrace();
    }

    return cellValue;
  }
}
