/**
 *
 */
package curam.ca.gc.bdm.test.evidence;

import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.creole.calculator.CREOLETestHelper;
import curam.creole.execution.session.RecalculationsProhibited;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.execution.session.StronglyTypedRuleObjectFactory;
import curam.creole.ruleclass.BDMIncomeValidationRuleSet.impl.ValidationResult;
import curam.creole.ruleclass.BDMIncomeValidationRuleSet.impl.ValidationResult_Factory;
import curam.creole.ruleclass.IncomeRuleSet.impl.Income;
import curam.creole.ruleclass.IncomeRuleSet.impl.Income_Factory;
import curam.creole.storage.inmemory.InMemoryDataStorage;
import org.junit.Test;

/**
 * @author
 *
 */
public class BDMIncomeValidationRuleSetTest extends CuramServerTestJUnit4 {

  Session session;

  private static String ERR_TAXYEAR_CAN_BE_PRIOR_YEAR =
    "Tax year must be year prior to current year.";

  private static String ERR_YEAR_NOT_NUMERIC =
    "Tax year must be numeric and completed.";

  private static String ERR_NETINCOME_MUST_BE_NUMERIC =
    "Net income must be numeric and completed.";

  @Override
  public void setUpCuramServerTest() {

    super.setUpCuramServerTest();
    session =
      Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
        new InMemoryDataStorage(new StronglyTypedRuleObjectFactory()));

  }

  /**
   * Asssert true when correct income year is specified (Previous year of
   * current year )
   */
  @Test
  public void testIncomeYearValid() {

    final ValidationResult validationResult =
      ValidationResult_Factory.getFactory().newInstance(session);
    final Income income = Income_Factory.getFactory().newInstance(session);

    income.year().specifyValue("2019");

    validationResult.evidence().specifyValue(income);

    CREOLETestHelper.assertEquals(true,
      validationResult.yearValidation().getValue());
  }

  /**
   * Asssert false when invalid income year is specified (Text format )
   */
  @Test
  public void testIncomeYearInValid() {

    final ValidationResult validationResult =
      ValidationResult_Factory.getFactory().newInstance(session);
    final Income income = Income_Factory.getFactory().newInstance(session);

    income.year().specifyValue("test");

    validationResult.evidence().specifyValue(income);

    CREOLETestHelper.assertEquals(false,
      validationResult.yearValidation().getValue());
  }

  /**
   * Asssert True when invalid income year is specified (Text format )
   */
  @Test
  public void testPreviousYearValidation() {

    final ValidationResult validationResult =
      ValidationResult_Factory.getFactory().newInstance(session);
    final Income income = Income_Factory.getFactory().newInstance(session);

    // current year
    income.year().specifyValue("2022");

    validationResult.evidence().specifyValue(income);

    CREOLETestHelper.assertEquals(true,
      validationResult.previousYearValidation().getValue());
  }

  /**
   * Asssert false when invalid income year is specified (Future year )
   */

  @Test
  public void testPreviousYearValidation_invalid() {

    final ValidationResult validationResult =
      ValidationResult_Factory.getFactory().newInstance(session);
    final Income income = Income_Factory.getFactory().newInstance(session);

    income.year().specifyValue("2022");

    validationResult.evidence().specifyValue(income);

    CREOLETestHelper.assertEquals(true,
      validationResult.previousYearValidation().getValue());
  }

  /**
   * Specify previous year validation
   */
  @Test
  public void testPreviousYearValidation_valid() {

    final ValidationResult validationResult =
      ValidationResult_Factory.getFactory().newInstance(session);
    final Income income = Income_Factory.getFactory().newInstance(session);

    income.year().specifyValue("2020");

    validationResult.evidence().specifyValue(income);

    CREOLETestHelper.assertEquals(false,
      validationResult.previousYearValidation().getValue());
  }

  /**
   * Validation must be thrown when year not previous year
   */
  @Test
  public void testPreviousYearValidation_validationMsg() {

    final ValidationResult validationResult =
      ValidationResult_Factory.getFactory().newInstance(session);
    final Income income = Income_Factory.getFactory().newInstance(session);

    // future year
    income.year().specifyValue("2022");

    validationResult.evidence().specifyValue(income);

    CREOLETestHelper.assertEquals(ERR_TAXYEAR_CAN_BE_PRIOR_YEAR,
      validationResult.validateYearIsPrevoiusYear().getValue()
        .failureMessage().getValue().toString());

  }

  @Test
  public void testPreviousYearValidation_validationMsg2() {

    final ValidationResult validationResult =
      ValidationResult_Factory.getFactory().newInstance(session);
    final Income income = Income_Factory.getFactory().newInstance(session);

    // current year
    income.year().specifyValue("2021");

    validationResult.evidence().specifyValue(income);

    CREOLETestHelper.assertEquals(ERR_TAXYEAR_CAN_BE_PRIOR_YEAR,
      validationResult.validateYearIsPrevoiusYear().getValue()
        .failureMessage().getValue().toString());
  }

  /**
   * validation message to check if income is above $0
   */
  @Test
  public void testValidateNetIncomeisNumeric_validationMsg2() {

    final ValidationResult validationResult =
      ValidationResult_Factory.getFactory().newInstance(session);
    final Income income = Income_Factory.getFactory().newInstance(session);

    // String value as income
    income.income().specifyValue(0);

    validationResult.evidence().specifyValue(income);

    // CREOLETestHelper.assertEquals(ERR_NETINCOME_MUST_BE_NUMERIC,
    // validationResult.validate().getValue()
    // .failureMessage().getValue().toString());
  }

  /**
   * validation message to check if Tax year is a number
   */
  @Test
  public void testValidateYearIsNumber_validationMsg2() {

    final ValidationResult validationResult =
      ValidationResult_Factory.getFactory().newInstance(session);
    final Income income = Income_Factory.getFactory().newInstance(session);

    // String value as year
    income.year().specifyValue("FAKEYEAR");

    validationResult.evidence().specifyValue(income);

    CREOLETestHelper.assertEquals(ERR_YEAR_NOT_NUMERIC,
      validationResult.validateYearIsNumber().getValue().failureMessage()
        .getValue().toString());
  }
}
