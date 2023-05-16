package curam.ca.gc.bdmoas.test.rules.functions;

import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionValidateDateBetweenPeriod;
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
 * The Class tests {@link CustomFunctionValidateDateBetweenPeriodTest} custom
 * function.
 */

@RunWith(JMockit.class)
public class CustomFunctionValidateDateBetweenPeriodTest
  extends CustomFunctionTestJunit4 {

  private static final boolean IS_VALID = true;

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The tested class. */
  @Tested
  CustomFunctionValidateDateBetweenPeriod validateDateBetweenPeriod;

  /**
   * Instantiates a new CustomFunctionValidateDateBetweenPeriodTest.
   */
  public CustomFunctionValidateDateBetweenPeriodTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    validateDateBetweenPeriod = new CustomFunctionValidateDateBetweenPeriod();
    ieg2Context = new IEG2Context();
  }

  /**
   * Test a valid period.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateDateBetweenPeriod_Success()
    throws AppException, InformationalException {

    final Date checkedDate = Date.getDate("20221212");
    final String years = "2";

    // Expectations
    expectationsParameters(checkedDate, years);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateDateBetweenPeriod
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The year should be valid. ", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test an invalid period.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateDateBetweenPeriod_Failure()
    throws AppException, InformationalException {

    final Date checkedDate = Date.getDate("20201212");
    final String years = "2";

    // Expectations
    expectationsParameters(checkedDate, years);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateDateBetweenPeriod
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The year should not be valid. ", !IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

}
