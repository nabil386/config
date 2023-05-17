package curam.ca.gc.bdm.test.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil;
import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventDatastoreConstants;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.ieg.impl.IEG2Context;
import curam.ieg.impl.IEGScriptExecution;
import curam.ieg.impl.IEGScriptExecutionFactory;
import curam.rules.functions.CustomFunctionValidateDatastoreIncomeValueChanged;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.type.Date;
import curam.util.type.Money;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/*
 * The Class tests {@link CustomFunctionValidateDatastoreNameValueChanged}
 * custom
 * function.
 */
@RunWith(JMockit.class)
public class CustomFunctionValidateDatastoreIncomeValueChangedTest
  extends CustomFunctionTestJunit4 {

  /** The Constant IS_VALID. */
  private static final boolean IS_VALID = true;

  /** Unchanged income amount for tests */
  private static final Money INCOME_AMOUNT = new Money(100.0);

  /** Changed income amount for tests */
  private static final Money INCOME_AMOUNT_CHANGED = new Money(200.0);

  /** A valid date of change. */
  private static final Date VALID_CHANGE_DATE = Date.getCurrentDate();

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The script execution. */
  @Mocked
  IEGScriptExecution scriptExecution;

  /** The script execution factory. */
  @Mocked
  IEGScriptExecutionFactory scriptExecutionFactory;

  /** The datastore. */
  @Mocked
  Datastore datastore;

  /** The rulesParameters. */
  @Mocked
  RulesParameters rulesParameters;

  /** The bdmApplicationEventsUtil. */
  @Mocked
  BDMApplicationEventsUtil bdmApplicationEventsUtil;

  /** The datastore factory. */
  @Mocked
  DatastoreFactory datastoreFactory;

  /** The root entity. */
  @Mocked
  Entity rootEntity;

  /** The person entity. */
  @Mocked
  Entity personEntity;

  /** The income entity. */
  @Mocked
  Entity incomeEntity;

  /** The validate function. */
  @Tested
  CustomFunctionValidateDatastoreIncomeValueChanged validateDatastoreIncome;

  public CustomFunctionValidateDatastoreIncomeValueChangedTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() throws NoSuchSchemaException {

    validateDatastoreIncome =
      new CustomFunctionValidateDatastoreIncomeValueChanged();
    ieg2Context = new IEG2Context();
  }

  /**
   * Income is changed, positive result
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_changed()
    throws AppException, InformationalException, NoSuchSchemaException {

    // Test Data
    final Money incomeAmount = INCOME_AMOUNT_CHANGED;
    final String resultMessage = "Income is changed";

    // Expectations
    expectationsParameters(incomeAmount);
    new Expectations() {

      {
        incomeEntity
          .getTypedAttribute(BDMLifeEventDatastoreConstants.INCOME_AMOUNT);
        result = INCOME_AMOUNT;

      }
    };

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateDatastoreIncome
        .getAdaptorValue(ieg2Context);

    // Verifications,
    assertEquals(resultMessage, IS_VALID, adaptorValue.getValue(ieg2Context));
  }

  /**
   * Income is the same, negative result
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalid_same()
    throws AppException, InformationalException, NoSuchSchemaException {

    // Test Data
    final Money incomeAmount = INCOME_AMOUNT;
    final String resultMessage = "Income is unchanged";

    // Expectations
    expectationsParameters(incomeAmount);
    new Expectations() {

      {
        incomeEntity
          .getTypedAttribute(BDMLifeEventDatastoreConstants.INCOME_AMOUNT);
        result = INCOME_AMOUNT;

      }
    };

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateDatastoreIncome
        .getAdaptorValue(ieg2Context);

    // Verifications,
    assertEquals(resultMessage, !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

}
