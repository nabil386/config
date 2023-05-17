package curam.ca.gc.bdm.test.rules.functions;

import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionvalidateStartedLivingDateBeforeDOB;
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
 * The Class tests {@link CustomFunctionvalidateStartedLivingDateBeforeDOB}
 * custom
 * function.
 */
@RunWith(JMockit.class)
public class CustomFunctionvalidateStartedLivingDateBeforeDOBTest
  extends CustomFunctionTestJunit4 {

  /** The input is valid */
  private static final boolean IS_VALID = true;

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The tested class. */
  @Tested
  CustomFunctionvalidateStartedLivingDateBeforeDOB customFunction;

  /**
   * Instantiates a new CustomFunctionconcatenateTest.
   */
  public CustomFunctionvalidateStartedLivingDateBeforeDOBTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    customFunction = new CustomFunctionvalidateStartedLivingDateBeforeDOB();
    ieg2Context = new IEG2Context();
  }

  /**
   * Test valid scenario.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid() throws AppException, InformationalException {

    final Date startDate = Date.fromISO8601("20220401");
    final Date birthDate = Date.fromISO8601("19900101");

    // Expectations
    expectationsParameters(startDate, birthDate);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction.getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Start date is after birthdate, should be valid", IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test invalid scenario.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalid() throws AppException, InformationalException {

    final Date startDate = Date.fromISO8601("19881010");
    final Date birthDate = Date.fromISO8601("19900101");

    // Expectations
    expectationsParameters(startDate, birthDate);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction.getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Start date is before birthdate, should be invalid",
      !IS_VALID, adaptorValue.getValue(ieg2Context));
  }

}
