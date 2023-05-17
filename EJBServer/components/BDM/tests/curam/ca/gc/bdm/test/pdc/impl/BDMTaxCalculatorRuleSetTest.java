package curam.ca.gc.bdm.test.pdc.impl;

import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.PROVINCETYPE;
import curam.creole.execution.session.InterpretedRuleObjectFactory;
import curam.creole.execution.session.RecalculationsProhibited;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.ruleclass.BDMTaxCalculatorRuleSet.impl.BDMTaxCalculator;
import curam.creole.ruleclass.BDMTaxCalculatorRuleSet.impl.BDMTaxCalculator_Factory;
import curam.creole.storage.inmemory.InMemoryDataStorage;
import curam.creole.value.CREOLENumber;
import curam.creole.value.CodeTableItem;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.Date;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BDMTaxCalculatorRuleSetTest extends CuramServerTestJUnit4 {

  public BDMTaxCalculatorRuleSetTest() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Tests a simple scenario for the province of Ontario
   *
   * @throws AppException
   * @throws InformationalException
   */
  // @Test
  // public void simpleTest() throws AppException, InformationalException {
  //
  // final Session session =
  // Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
  // new InMemoryDataStorage(new InterpretedRuleObjectFactory()));
  //
  // final BDMTaxCalculator taxCalculator =
  // BDMTaxCalculator_Factory.getFactory().newInstance(session);
  //
  // // set up inputted parameters
  // taxCalculator.getAttributeValue("effectiveDate")
  // .specifyValue(Date.getDate("20220101"));
  // taxCalculator.getAttributeValue("province").specifyValue(
  // new CodeTableItem(PROVINCETYPE.TABLENAME, PROVINCETYPE.ONTARIO));
  // taxCalculator.getAttributeValue("age").specifyValue(new Integer(65));
  // taxCalculator.getAttributeValue("A_annualTaxableIncome")
  // .specifyValue(new Double(182000));
  // taxCalculator.getAttributeValue("clientStatusRegisteredAsIndian")
  // .specifyValue(Boolean.valueOf(false));
  // taxCalculator.getAttributeValue("anyEmployersDeductedIncomeFromPartOfPay")
  // .specifyValue(Boolean.valueOf(false));
  // taxCalculator.getAttributeValue("anyEmployersDeductedIncomeFromPay")
  // .specifyValue(Boolean.valueOf(false));
  // taxCalculator.getAttributeValue("dependentSpouseDeclaredInTaxReturn")
  // .specifyValue(Boolean.valueOf(true));
  // taxCalculator.getAttributeValue("dependentChildrenCount")
  // .specifyValue(new Integer(2));
  //
  // final CalculateFederalTax federalTaxCalculator =
  // CalculateFederalTax_Factory.getFactory().newInstance(session,
  // taxCalculator);
  // assertEquals(29.0,
  // federalTaxCalculator.getAttributeValue("R_federalTaxRate").getValue());
  // assertEquals(12951.0, federalTaxCalculator
  // .getAttributeValue("K_federalTaxConstant").getValue());
  //
  // assertEquals(15.0, federalTaxCalculator
  // .getAttributeValue("federalTaxRateLowestIncome").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(36694), federalTaxCalculator
  // .getAttributeValue("TC_totalClaimAmount").getValue());
  // assertEquals(CREOLENumber.toCREOLENumber(5504.1), federalTaxCalculator
  // .getAttributeValue("K1_nonRefundablePersonalTaxCredit").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(0), federalTaxCalculator
  // .getAttributeValue("K2_federalPensionEITaxCredit").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(0),
  // federalTaxCalculator
  // .getAttributeValue("K3_federalOtherNonRefundableTaxCredits")
  // .getValue());
  //
  // assertEquals(1287.0,
  // federalTaxCalculator.getAttributeValue("federalCEAValue").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(193.05), federalTaxCalculator
  // .getAttributeValue("K4_federalCEAFactor").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(34131.85), federalTaxCalculator
  // .getAttributeValue("T3_annualFederalTaxDeduction").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(34131.85), federalTaxCalculator
  // .getAttributeValue("T1_annualBasicFederalTaxDeduction").getValue());
  //
  // final CalculateProvincialTax provincialTaxCalculator =
  // CalculateProvincialTax_Factory.getFactory().newInstance(session,
  // taxCalculator);
  //
  // assertEquals(12.16, provincialTaxCalculator
  // .getAttributeValue("V_provincialTaxRate").getValue());
  //
  // assertEquals(5254.0, provincialTaxCalculator
  // .getAttributeValue("KP_provincialTaxConstant").getValue());
  //
  // assertEquals(5.05, provincialTaxCalculator
  // .getAttributeValue("provincialTaxRateLowestIncome").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(26041), provincialTaxCalculator
  // .getAttributeValue("TCP_totalClaimAmount").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(1315.0705),
  // provincialTaxCalculator
  // .getAttributeValue("K1P_nonRefundablePersonalTaxCredit").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(0), provincialTaxCalculator
  // .getAttributeValue("K2P_provincialPensionEITaxCredit").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(0),
  // provincialTaxCalculator
  // .getAttributeValue("K3P_provincialOtherNonRefundableTaxCredits")
  // .getValue());
  //
  // assertEquals(0.0, provincialTaxCalculator
  // .getAttributeValue("provincialCEAValue").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(0.0), provincialTaxCalculator
  // .getAttributeValue("K4P_provincialCEAFactor").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(15562.1295),
  // provincialTaxCalculator.getAttributeValue("T4_annualProvincialBasicTax")
  // .getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(5417.27252),
  // provincialTaxCalculator.getAttributeValue("V1_ProvincialSurtax")
  // .getValue());
  //
  // assertEquals(750.0, provincialTaxCalculator
  // .getAttributeValue("V2_ProvincialHealthPremium").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(0), provincialTaxCalculator
  // .getAttributeValue("S_provincialTaxReductionBasicAmount").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(21729.40202),
  // provincialTaxCalculator
  // .getAttributeValue("T2_annualProvincialTaxDeduction").getValue());
  // }

  /**
   * Tests the the health premium calculation can handle scenario where no
   * upper bound income exists
   *
   * @throws AppException
   * @throws InformationalException
   */
  // @Test
  // public void testUpperIncomeOntario()
  // throws AppException, InformationalException {
  //
  // final Session session =
  // Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
  // new InMemoryDataStorage(new InterpretedRuleObjectFactory()));
  //
  // final BDMTaxCalculator taxCalculator =
  // BDMTaxCalculator_Factory.getFactory().newInstance(session);
  //
  // // set up inputted parameters
  // taxCalculator.getAttributeValue("effectiveDate")
  // .specifyValue(Date.getDate("20220101"));
  // taxCalculator.getAttributeValue("province").specifyValue(
  // new CodeTableItem(PROVINCETYPE.TABLENAME, PROVINCETYPE.ONTARIO));
  // taxCalculator.getAttributeValue("age").specifyValue(new Integer(65));
  // taxCalculator.getAttributeValue("A_annualTaxableIncome")
  // .specifyValue(new Double(182000));
  // taxCalculator.getAttributeValue("clientStatusRegisteredAsIndian")
  // .specifyValue(Boolean.valueOf(false));
  // taxCalculator.getAttributeValue("anyEmployersDeductedIncomeFromPartOfPay")
  // .specifyValue(Boolean.valueOf(true));
  // taxCalculator.getAttributeValue("anyEmployersDeductedIncomeFromPay")
  // .specifyValue(Boolean.valueOf(false));
  // taxCalculator.getAttributeValue("dependentSpouseDeclaredInTaxReturn")
  // .specifyValue(Boolean.valueOf(true));
  // taxCalculator.getAttributeValue("dependentChildrenCount")
  // .specifyValue(new Integer(2));
  //
  // final CalculateFederalTax federalTaxCalculator =
  // CalculateFederalTax_Factory.getFactory().newInstance(session,
  // taxCalculator);
  // assertEquals(29.0,
  // federalTaxCalculator.getAttributeValue("R_federalTaxRate").getValue());
  // assertEquals(12951.0, federalTaxCalculator
  // .getAttributeValue("K_federalTaxConstant").getValue());
  //
  // assertEquals(15.0, federalTaxCalculator
  // .getAttributeValue("federalTaxRateLowestIncome").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(36694), federalTaxCalculator
  // .getAttributeValue("TC_totalClaimAmount").getValue());
  // assertEquals(CREOLENumber.toCREOLENumber(5504.1), federalTaxCalculator
  // .getAttributeValue("K1_nonRefundablePersonalTaxCredit").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(0), federalTaxCalculator
  // .getAttributeValue("K2_federalPensionEITaxCredit").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(0),
  // federalTaxCalculator
  // .getAttributeValue("K3_federalOtherNonRefundableTaxCredits")
  // .getValue());
  //
  // assertEquals(1287.0,
  // federalTaxCalculator.getAttributeValue("federalCEAValue").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(193.05), federalTaxCalculator
  // .getAttributeValue("K4_federalCEAFactor").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(34131.85), federalTaxCalculator
  // .getAttributeValue("T3_annualFederalTaxDeduction").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(34131.85), federalTaxCalculator
  // .getAttributeValue("T1_annualBasicFederalTaxDeduction").getValue());
  //
  // final CalculateProvincialTax provincialTaxCalculator =
  // CalculateProvincialTax_Factory.getFactory().newInstance(session,
  // taxCalculator);
  //
  // assertEquals(12.16, provincialTaxCalculator
  // .getAttributeValue("V_provincialTaxRate").getValue());
  //
  // assertEquals(5254.0, provincialTaxCalculator
  // .getAttributeValue("KP_provincialTaxConstant").getValue());
  //
  // assertEquals(5.05, provincialTaxCalculator
  // .getAttributeValue("provincialTaxRateLowestIncome").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(26041), provincialTaxCalculator
  // .getAttributeValue("TCP_totalClaimAmount").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(1315.0705),
  // provincialTaxCalculator
  // .getAttributeValue("K1P_nonRefundablePersonalTaxCredit").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(0), provincialTaxCalculator
  // .getAttributeValue("K2P_provincialPensionEITaxCredit").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(0),
  // provincialTaxCalculator
  // .getAttributeValue("K3P_provincialOtherNonRefundableTaxCredits")
  // .getValue());
  //
  // assertEquals(0.0, provincialTaxCalculator
  // .getAttributeValue("provincialCEAValue").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(0.0), provincialTaxCalculator
  // .getAttributeValue("K4P_provincialCEAFactor").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(15562.1295),
  // provincialTaxCalculator.getAttributeValue("T4_annualProvincialBasicTax")
  // .getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(5417.27252),
  // provincialTaxCalculator.getAttributeValue("V1_ProvincialSurtax")
  // .getValue());
  //
  // assertEquals(750.0, provincialTaxCalculator
  // .getAttributeValue("V2_ProvincialHealthPremium").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(0), provincialTaxCalculator
  // .getAttributeValue("S_provincialTaxReductionBasicAmount").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(21729.40202),
  // provincialTaxCalculator
  // .getAttributeValue("T2_annualProvincialTaxDeduction").getValue());
  // }

  /**
   * Tests a simple scenario for the province of BC
   *
   * @throws AppException
   * @throws InformationalException
   */
  // @Test
  // public void testNoConditions() throws AppException, InformationalException {
  //
  // final Session session =
  // Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
  // new InMemoryDataStorage(new InterpretedRuleObjectFactory()));
  //
  // final BDMTaxCalculator taxCalculator =
  // BDMTaxCalculator_Factory.getFactory().newInstance(session);
  //
  // // set up inputted parameters
  // taxCalculator.getAttributeValue("effectiveDate")
  // .specifyValue(Date.getDate("20221001"));
  // taxCalculator.getAttributeValue("province")
  // .specifyValue(new CodeTableItem(PROVINCETYPE.TABLENAME,
  // PROVINCETYPE.BRITISHCOLUMBIA));
  // taxCalculator.getAttributeValue("age").specifyValue(new Integer(20));
  // taxCalculator.getAttributeValue("A_annualTaxableIncome")
  // .specifyValue(new Double(42184));
  // taxCalculator.getAttributeValue("clientStatusRegisteredAsIndian")
  // .specifyValue(Boolean.valueOf(false));
  // taxCalculator.getAttributeValue("anyEmployersDeductedIncomeFromPartOfPay")
  // .specifyValue(Boolean.valueOf(false));
  // taxCalculator.getAttributeValue("anyEmployersDeductedIncomeFromPay")
  // .specifyValue(Boolean.valueOf(false));
  // taxCalculator.getAttributeValue("dependentSpouseDeclaredInTaxReturn")
  // .specifyValue(Boolean.valueOf(false));
  // taxCalculator.getAttributeValue("dependentChildrenCount")
  // .specifyValue(new Integer(0));
  //
  // final CalculateFederalTax federalTaxCalculator =
  // CalculateFederalTax_Factory.getFactory().newInstance(session,
  // taxCalculator);
  // assertEquals(15.0,
  // federalTaxCalculator.getAttributeValue("R_federalTaxRate").getValue());
  // assertEquals(0.0, federalTaxCalculator
  // .getAttributeValue("K_federalTaxConstant").getValue());
  //
  // assertEquals(15.0, federalTaxCalculator
  // .getAttributeValue("federalTaxRateLowestIncome").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(14398), federalTaxCalculator
  // .getAttributeValue("TC_totalClaimAmount").getValue());
  // assertEquals(CREOLENumber.toCREOLENumber(2159.7), federalTaxCalculator
  // .getAttributeValue("K1_nonRefundablePersonalTaxCredit").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(0), federalTaxCalculator
  // .getAttributeValue("K2_federalPensionEITaxCredit").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(0),
  // federalTaxCalculator
  // .getAttributeValue("K3_federalOtherNonRefundableTaxCredits")
  // .getValue());
  //
  // assertEquals(1287.0,
  // federalTaxCalculator.getAttributeValue("federalCEAValue").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(193.05), federalTaxCalculator
  // .getAttributeValue("K4_federalCEAFactor").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(3974.85), federalTaxCalculator
  // .getAttributeValue("T3_annualFederalTaxDeduction").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(3974.85), federalTaxCalculator
  // .getAttributeValue("T1_annualBasicFederalTaxDeduction").getValue());
  //
  // final CalculateProvincialTax provincialTaxCalculator =
  // CalculateProvincialTax_Factory.getFactory().newInstance(session,
  // taxCalculator);
  //
  // assertEquals(5.06, provincialTaxCalculator
  // .getAttributeValue("V_provincialTaxRate").getValue());
  //
  // assertEquals(0.0, provincialTaxCalculator
  // .getAttributeValue("KP_provincialTaxConstant").getValue());
  //
  // assertEquals(5.06, provincialTaxCalculator
  // .getAttributeValue("provincialTaxRateLowestIncome").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(11302), provincialTaxCalculator
  // .getAttributeValue("TCP_totalClaimAmount").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(571.8812),
  // provincialTaxCalculator
  // .getAttributeValue("K1P_nonRefundablePersonalTaxCredit").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(0), provincialTaxCalculator
  // .getAttributeValue("K2P_provincialPensionEITaxCredit").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(0),
  // provincialTaxCalculator
  // .getAttributeValue("K3P_provincialOtherNonRefundableTaxCredits")
  // .getValue());
  //
  // assertEquals(0.0, provincialTaxCalculator
  // .getAttributeValue("provincialCEAValue").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(0.0), provincialTaxCalculator
  // .getAttributeValue("K4P_provincialCEAFactor").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(1562.6292),
  // provincialTaxCalculator.getAttributeValue("T4_annualProvincialBasicTax")
  // .getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(0), provincialTaxCalculator
  // .getAttributeValue("V1_ProvincialSurtax").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(0), provincialTaxCalculator
  // .getAttributeValue("V2_ProvincialHealthPremium").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(0.0), provincialTaxCalculator
  // .getAttributeValue("S_provincialTaxReductionBasicAmount").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(1562.6292),
  // provincialTaxCalculator
  // .getAttributeValue("T2_annualProvincialTaxDeduction").getValue());
  // }

  /**
   * Tests a simple scenario for the Yukon territories
   *
   * @throws AppException
   * @throws InformationalException
   */
  // @Test
  // public void testYukon() throws AppException, InformationalException {
  //
  // final Session session =
  // Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
  // new InMemoryDataStorage(new InterpretedRuleObjectFactory()));
  //
  // final BDMTaxCalculator taxCalculator =
  // BDMTaxCalculator_Factory.getFactory().newInstance(session);
  //
  // // set up inputted parameters
  // taxCalculator.getAttributeValue("effectiveDate")
  // .specifyValue(Date.getDate("20221001"));
  // taxCalculator.getAttributeValue("province").specifyValue(
  // new CodeTableItem(PROVINCETYPE.TABLENAME, PROVINCETYPE.YUKON));
  // taxCalculator.getAttributeValue("age").specifyValue(new Integer(20));
  // taxCalculator.getAttributeValue("A_annualTaxableIncome")
  // .specifyValue(new Double(216512));
  // taxCalculator.getAttributeValue("clientStatusRegisteredAsIndian")
  // .specifyValue(Boolean.valueOf(true));
  // taxCalculator.getAttributeValue("anyEmployersDeductedIncomeFromPartOfPay")
  // .specifyValue(Boolean.valueOf(true));
  // taxCalculator.getAttributeValue("anyEmployersDeductedIncomeFromPay")
  // .specifyValue(Boolean.valueOf(false));
  // taxCalculator.getAttributeValue("dependentSpouseDeclaredInTaxReturn")
  // .specifyValue(Boolean.valueOf(false));
  // taxCalculator.getAttributeValue("dependentChildrenCount")
  // .specifyValue(new Integer(0));
  //
  // final CalculateFederalTax federalTaxCalculator =
  // CalculateFederalTax_Factory.getFactory().newInstance(session,
  // taxCalculator);
  //
  // assertEquals(29.0,
  // federalTaxCalculator.getAttributeValue("R_federalTaxRate").getValue());
  // assertEquals(12951.0, federalTaxCalculator
  // .getAttributeValue("K_federalTaxConstant").getValue());
  //
  // assertEquals(15.0, federalTaxCalculator
  // .getAttributeValue("federalTaxRateLowestIncome").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(14398), federalTaxCalculator
  // .getAttributeValue("TC_totalClaimAmount").getValue());
  // assertEquals(CREOLENumber.toCREOLENumber(2159.7), federalTaxCalculator
  // .getAttributeValue("K1_nonRefundablePersonalTaxCredit").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(0), federalTaxCalculator
  // .getAttributeValue("K2_federalPensionEITaxCredit").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(0),
  // federalTaxCalculator
  // .getAttributeValue("K3_federalOtherNonRefundableTaxCredits")
  // .getValue());
  //
  // assertEquals(1287.0,
  // federalTaxCalculator.getAttributeValue("federalCEAValue").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(193.05), federalTaxCalculator
  // .getAttributeValue("K4_federalCEAFactor").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(47484.73), federalTaxCalculator
  // .getAttributeValue("T3_annualFederalTaxDeduction").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(47484.73), federalTaxCalculator
  // .getAttributeValue("T1_annualBasicFederalTaxDeduction").getValue());
  //
  // final CalculateProvincialTax provincialTaxCalculator =
  // CalculateProvincialTax_Factory.getFactory().newInstance(session,
  // taxCalculator);
  //
  // assertEquals(12.8, provincialTaxCalculator
  // .getAttributeValue("V_provincialTaxRate").getValue());
  //
  // assertEquals(6169.0, provincialTaxCalculator
  // .getAttributeValue("KP_provincialTaxConstant").getValue());
  //
  // assertEquals(6.4, provincialTaxCalculator
  // .getAttributeValue("provincialTaxRateLowestIncome").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(14398), provincialTaxCalculator
  // .getAttributeValue("TCP_totalClaimAmount").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(921.472), provincialTaxCalculator
  // .getAttributeValue("K1P_nonRefundablePersonalTaxCredit").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(0), provincialTaxCalculator
  // .getAttributeValue("K2P_provincialPensionEITaxCredit").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(0),
  // provincialTaxCalculator
  // .getAttributeValue("K3P_provincialOtherNonRefundableTaxCredits")
  // .getValue());
  //
  // assertEquals(1287.0, provincialTaxCalculator
  // .getAttributeValue("provincialCEAValue").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(82.368), provincialTaxCalculator
  // .getAttributeValue("K4P_provincialCEAFactor").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(20540.696),
  // provincialTaxCalculator.getAttributeValue("T4_annualProvincialBasicTax")
  // .getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(0), provincialTaxCalculator
  // .getAttributeValue("V1_ProvincialSurtax").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(0.0), provincialTaxCalculator
  // .getAttributeValue("S_provincialTaxReductionBasicAmount").getValue());
  //
  // assertEquals(CREOLENumber.toCREOLENumber(20540.696),
  // provincialTaxCalculator
  // .getAttributeValue("T2_annualProvincialTaxDeduction").getValue());
  //
  // }

  /**
   * Tests a basic scenario for Quebec provincial tax
   */
  // @Test
  // public void testQuebecProvincialTaxCalculator() {
  //
  // final Session session =
  // Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
  // new InMemoryDataStorage(new InterpretedRuleObjectFactory()));
  //
  // final BDMTaxCalculator taxCalculator =
  // BDMTaxCalculator_Factory.getFactory().newInstance(session);
  //
  // taxCalculator.effectiveDate().specifyValue(Date.getDate("20220101"));
  // taxCalculator.A_annualTaxableIncome().specifyValue(new Double(60000));
  // taxCalculator.province().specifyValue(
  // new CodeTableItem(PROVINCETYPE.TABLENAME, PROVINCETYPE.QUEBEC));
  // taxCalculator.age().specifyValue(65);
  // taxCalculator.getAttributeValue("clientStatusRegisteredAsIndian")
  // .specifyValue(Boolean.valueOf(true));
  // taxCalculator.getAttributeValue("anyEmployersDeductedIncomeFromPartOfPay")
  // .specifyValue(Boolean.valueOf(false));
  // taxCalculator.getAttributeValue("anyEmployersDeductedIncomeFromPay")
  // .specifyValue(Boolean.valueOf(true));
  // taxCalculator.getAttributeValue("dependentSpouseDeclaredInTaxReturn")
  // .specifyValue(Boolean.valueOf(false));
  // taxCalculator.getAttributeValue("dependentChildrenCount")
  // .specifyValue(new Integer(0));
  //
  // final QuebecProvincialTaxCalculator qcTaxCalculator =
  // QuebecProvincialTaxCalculator_Factory.getFactory().newInstance(session,
  // taxCalculator);
  //
  // assertEquals(CREOLENumber.toCREOLENumber(0.2),
  // qcTaxCalculator.QT_taxRate().getValue());
  // assertEquals(2314.0, qcTaxCalculator.QK_taxConstant().getValue());
  // assertEquals(CREOLENumber.toCREOLENumber(0.15),
  // qcTaxCalculator.quebecTaxRateLowestIncome().getValue());
  // assertEquals(CREOLENumber.toCREOLENumber(19538),
  // qcTaxCalculator.QE_totalClaimAmount().getValue());
  // assertEquals(CREOLENumber.toCREOLENumber(2930.7),
  // qcTaxCalculator.nonRefundablePersonalTaxCredit().getValue());
  // assertEquals(CREOLENumber.toCREOLENumber(6755.3),
  // qcTaxCalculator.QY_annualIncomeTaxDeduction().getValue());
  // }

  /**
   * Tests that negative annual income results in a tax deduction of 0
   */
  @Test
  public void testQuebecProvincialTaxCalculator_NegativeIncome() {

    final Session session =
      Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
        new InMemoryDataStorage(new InterpretedRuleObjectFactory()));

    final BDMTaxCalculator taxCalculator =
      BDMTaxCalculator_Factory.getFactory().newInstance(session);

    taxCalculator.effectiveDate().specifyValue(Date.getDate("20220101"));
    taxCalculator.A_annualTaxableIncome().specifyValue(new Double(-60000));
    taxCalculator.province().specifyValue(
      new CodeTableItem(PROVINCETYPE.TABLENAME, PROVINCETYPE.QUEBEC));
    taxCalculator.age().specifyValue(65);
    taxCalculator.getAttributeValue("clientStatusRegisteredAsIndian")
      .specifyValue(Boolean.valueOf(false));
    taxCalculator.getAttributeValue("anyEmployersDeductedIncomeFromPartOfPay")
      .specifyValue(Boolean.valueOf(false));
    taxCalculator.getAttributeValue("anyEmployersDeductedIncomeFromPay")
      .specifyValue(Boolean.valueOf(false));
    taxCalculator.getAttributeValue("dependentSpouseDeclaredInTaxReturn")
      .specifyValue(Boolean.valueOf(false));
    taxCalculator.getAttributeValue("dependentChildrenCount")
      .specifyValue(new Integer(0));

    assertEquals(CREOLENumber.toCREOLENumber(0),
      taxCalculator.QY_annualIncomeTaxDeduction().getValue());

  }

  /**
   * Tests that when a client is registered as a Status Indian, the deduction is
   * 0
   */
  @Test
  public void testQuebecProvincialTaxCalculator_StatusIndian() {

    final Session session =
      Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
        new InMemoryDataStorage(new InterpretedRuleObjectFactory()));

    final BDMTaxCalculator taxCalculator =
      BDMTaxCalculator_Factory.getFactory().newInstance(session);

    taxCalculator.effectiveDate().specifyValue(Date.getDate("20220101"));
    taxCalculator.A_annualTaxableIncome().specifyValue(new Double(60000));
    taxCalculator.province().specifyValue(
      new CodeTableItem(PROVINCETYPE.TABLENAME, PROVINCETYPE.QUEBEC));
    taxCalculator.age().specifyValue(65);
    taxCalculator.getAttributeValue("clientStatusRegisteredAsIndian")
      .specifyValue(Boolean.valueOf(true));
    taxCalculator.getAttributeValue("anyEmployersDeductedIncomeFromPartOfPay")
      .specifyValue(Boolean.valueOf(false));
    taxCalculator.getAttributeValue("anyEmployersDeductedIncomeFromPay")
      .specifyValue(Boolean.valueOf(false));
    taxCalculator.getAttributeValue("dependentSpouseDeclaredInTaxReturn")
      .specifyValue(Boolean.valueOf(false));
    taxCalculator.getAttributeValue("dependentChildrenCount")
      .specifyValue(new Integer(0));

    assertEquals(CREOLENumber.toCREOLENumber(0),
      taxCalculator.QY_annualIncomeTaxDeduction().getValue());

  }

}
