package curam.ca.gc.bdmoas.test.rules.functions;

import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionvalidateLength2;
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
 * The Class tests {@link CustomFunctionvalidateLength2Test} custom
 * function.
 */

@RunWith(JMockit.class)
public class CustomFunctionvalidateLength2Test
  extends CustomFunctionTestJunit4 {

  private static final boolean IS_VALID = true;

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The tested class. */
  @Tested
  CustomFunctionvalidateLength2 validateLength2;

  /**
   * Instantiates a new CustomFunctionvalidatePhoneNumberCountryTest.
   */
  public CustomFunctionvalidateLength2Test() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    validateLength2 = new CustomFunctionvalidateLength2();
    ieg2Context = new IEG2Context();
  }

  /**
   * Test a valid phone length
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateLength2_Success()
    throws AppException, InformationalException {

    final String testString = "9991234";
    final String min = "7";
    final String max = "7";
    // Expectations
    expectationsParameters(testString, min, max);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateLength2.getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The length should be valid", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test a valid phone length
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateLength2_Success_alphachars()
    throws AppException, InformationalException {

    final String testString = "999abcde";
    final String min = "8";
    final String max = "8";
    // Expectations
    expectationsParameters(testString, min, max);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateLength2.getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The length should be valid", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test a invalid phone length
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateLength2_FailureUnderMin()
    throws AppException, InformationalException {

    final String testString = "12";
    final String min = "3";
    final String max = "7";
    // Expectations
    expectationsParameters(testString, min, max);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateLength2.getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The length should be not valid", !IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test a invalid phone length
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateLength2_FailureUnderMax()
    throws AppException, InformationalException {

    final String testString = "12345678";
    final String min = "3";
    final String max = "7";
    // Expectations
    expectationsParameters(testString, min, max);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateLength2.getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The length should be not valid", !IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test a invalid phone length
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateLength2_Null()
    throws AppException, InformationalException {

    final String testString = "12";
    final String min = "";
    final String max = "";
    // Expectations
    expectationsParameters(testString, min, max);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateLength2.getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The length should be not valid", !IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

}
