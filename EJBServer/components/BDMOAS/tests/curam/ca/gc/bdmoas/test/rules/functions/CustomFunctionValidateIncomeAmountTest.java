package curam.ca.gc.bdmoas.test.rules.functions;

import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionValidateIncomeAmount;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.CustomFunctor;
import curam.util.type.Money;
import java.util.ArrayList;
import java.util.List;
import mockit.Mock;
import mockit.MockUp;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/**
 * The Class tests {@link CustomFunctionValidateIncomeAmountTest} custom
 * function.
 */

@RunWith(JMockit.class)
public class CustomFunctionValidateIncomeAmountTest
  extends CustomFunctionTestJunit4 {

  private static final boolean IS_POSITIVE = true;

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The tested class. */
  @Tested
  CustomFunctionValidateIncomeAmount validateIncomeAmount;

  /**
   * Instantiates a new CustomFunctionValidateIncomeAmountTest.
   */
  public CustomFunctionValidateIncomeAmountTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    validateIncomeAmount = new CustomFunctionValidateIncomeAmount();
    ieg2Context = new IEG2Context();
  }

  /**
   * Test a valid money amount.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateIncomeAmount_Success()
    throws AppException, InformationalException {

    final Money sum = new Money(22.02);

    // Expectations
    expectationsParameters(sum);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateIncomeAmount
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The amount of money should be valid. ", IS_POSITIVE,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test an invalid money amount.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateIncomeAmount_Failure()
    throws AppException, InformationalException {

    final Money sum = new Money(-22.02);

    // Expectations
    expectationsParameters(sum);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateIncomeAmount
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The amount of money should not be valid. ", !IS_POSITIVE,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test an null money amount - valid because implies question was not asked
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateIncomeAmount_Null()
    throws AppException, InformationalException {

    // to test null check
    new MockUp<CustomFunctor>() {

      @Mock
      public List<Adaptor> getParameters() {

        final List<Adaptor> parameters = new ArrayList<>();

        final Adaptor c = null;
        parameters.add(c);

        return parameters;
      }

    };

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateIncomeAmount
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The amount of money should be valid. ", IS_POSITIVE,
      adaptorValue.getValue(ieg2Context));

  }

}
