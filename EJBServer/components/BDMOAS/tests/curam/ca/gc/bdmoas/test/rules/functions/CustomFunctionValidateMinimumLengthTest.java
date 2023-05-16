package curam.ca.gc.bdmoas.test.rules.functions;

import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionValidateMinimumLength;
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
 * The Class tests {@link CustomFunctionValidateMinimumLengthTest} custom
 * function.
 */

@RunWith(JMockit.class)
public class CustomFunctionValidateMinimumLengthTest
  extends CustomFunctionTestJunit4 {

  private static final boolean IS_VALID = true;

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The tested class. */
  @Tested
  CustomFunctionValidateMinimumLength validateMinimumLength;

  /**
   * Instantiates a new CustomFunctionValidateMinimumLengthTest.
   */
  public CustomFunctionValidateMinimumLengthTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    validateMinimumLength = new CustomFunctionValidateMinimumLength();
    ieg2Context = new IEG2Context();
  }

  /**
   * Test a valid length.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateMinimumLength_Success()
    throws AppException, InformationalException {

    final String str = "dfkdsfksd";
    final String min = "2";

    // Expectations
    expectationsParameters(str, min);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateMinimumLength
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The year should be valid. ", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test an invalid length
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateMinimumLength_Failure()
    throws AppException, InformationalException {

    final String str = "dfk";
    final String min = "4";

    // Expectations
    expectationsParameters(str, min);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateMinimumLength
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The year should not be valid. ", !IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test an invalid length
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateMinimumLength_FailureNull()
    throws AppException, InformationalException {

    final String str = "dfk";
    final String min = null;

    // Expectations
    expectationsParameters(str, min);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateMinimumLength
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The year should not be valid. ", !IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }
  
  /**
   * Test an invalid length
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateMinimumLength_FailureAlpha()
    throws AppException, InformationalException {

    final String str = "dfk";
    final String min = "sdfk";

    // Expectations
    expectationsParameters(str, min);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateMinimumLength
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The year should not be valid. ", !IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }


}
