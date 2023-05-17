package curam.ca.gc.bdmoas.test.rules.functions;

import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionValidateDateFirstOfMonth;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.functor.Adaptor;
import curam.util.type.Date;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/**
 * The Class tests {@link CustomFunctionValidateDateFirstOfMonthTest} custom
 * function.
 */

@RunWith(JMockit.class)
public class CustomFunctionValidateDateFirstOfMonthTest
  extends CustomFunctionTestJunit4 {

  private static final boolean IS_VALID = true;

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The tested class. */
  @Tested
  CustomFunctionValidateDateFirstOfMonth validateDateFirstOfMonth;

  /**
   * Instantiates a new CustomFunctionValidateDateFirstOfMonthTest.
   */
  public CustomFunctionValidateDateFirstOfMonthTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    validateDateFirstOfMonth = new CustomFunctionValidateDateFirstOfMonth();
    ieg2Context = new IEG2Context();
  }

  /**
   * Test a valid date of first of month.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateDateFirstOfMonth_Success()
    throws AppException, InformationalException {

    final Date dateParameter = Date.getDate("20211201");

    // Expectations
    expectationsParameters(dateParameter);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateDateFirstOfMonth
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("This date should be the valid first of the month.",
      IS_VALID, adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test an invalid date not first of month.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateDateFirstOfMonth_Failure()
    throws AppException, InformationalException {

    final Date dateParameter = Date.getDate("20211225");

    // Expectations
    expectationsParameters(dateParameter);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateDateFirstOfMonth
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals(
      "This date should be invalid as it's not the first of the month.",
      !IS_VALID, adaptorValue.getValue(ieg2Context));
  }

}
