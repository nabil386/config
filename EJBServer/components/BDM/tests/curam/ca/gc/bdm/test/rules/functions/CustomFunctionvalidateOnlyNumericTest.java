package curam.ca.gc.bdm.test.rules.functions;

import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionvalidateOnlyNumeric;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.functor.Adaptor;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/**
 * The Class tests {@link CustomFunctionvalidateOnlyNumeric} custom
 * function.
 */
@RunWith(JMockit.class)
public class CustomFunctionvalidateOnlyNumericTest
  extends CustomFunctionTestJunit4 {

  /** The input is valid */
  private static final boolean IS_VALID = true;

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The tested class. */
  @Tested
  CustomFunctionvalidateOnlyNumeric customFunction;

  /**
   * Instantiates a new CustomFunctionconcatenateTest.
   */
  public CustomFunctionvalidateOnlyNumericTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    customFunction = new CustomFunctionvalidateOnlyNumeric();
    ieg2Context = new IEG2Context();
  }

  /**
   * Test "0".
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_zero() throws AppException, InformationalException {

    final String input = "0";

    // Expectations
    expectationsParameters(input);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction.getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals(input + " is valid", IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test "42".
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_number()
    throws AppException, InformationalException {

    final String input = "42";

    // Expectations
    expectationsParameters(input);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction.getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals(input + " is valid", IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test "foobar".
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalid_text()
    throws AppException, InformationalException {

    final String input = "foobar";

    // Expectations
    expectationsParameters(input);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction.getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals(input + " is not valid", !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test "foo42".
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalid_alphanumeric()
    throws AppException, InformationalException {

    final String input = "foo42";

    // Expectations
    expectationsParameters(input);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction.getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals(input + " is not valid", !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test "!@#".
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalid_symbols()
    throws AppException, InformationalException {

    final String input = "!@#";

    // Expectations
    expectationsParameters(input);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction.getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals(input + " is not valid", !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

}
