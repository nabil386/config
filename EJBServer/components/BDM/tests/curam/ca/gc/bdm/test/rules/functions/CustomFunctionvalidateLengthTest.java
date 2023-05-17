package curam.ca.gc.bdm.test.rules.functions;

import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionvalidateLength;
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
 * The Class tests {@link CustomFunctionvalidateLength} custom
 * function.
 */
@RunWith(JMockit.class)
public class CustomFunctionvalidateLengthTest
  extends CustomFunctionTestJunit4 {

  /** The Constant IS_VALID. */
  private static final boolean IS_VALID = true;

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The validate length. */
  @Tested
  CustomFunctionvalidateLength validateLength;

  /**
   * Instantiates a new CustomFunctionvalidateLengthTest.
   */
  public CustomFunctionvalidateLengthTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    validateLength = new CustomFunctionvalidateLength();
    ieg2Context = new IEG2Context();
  }

  /**
   * Test valid maximum string length.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validMaximumStringLength()
    throws AppException, InformationalException {

    // Expectations
    expectationsParameters("123456", "", "10");

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateLength.getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("The length of the string is valid", IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test valid minimum string length.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validMinimumStringLength()
    throws AppException, InformationalException {

    // Expectations
    expectationsParameters("123456", "5", "");

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateLength.getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("The length of the string is valid", IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test valid minimum maximum string length.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validMinimumMaximumStringLength()
    throws AppException, InformationalException {

    // Expectations
    expectationsParameters("123456", "6", "6");

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateLength.getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("The length of the string is valid", IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test invalid minimum string length.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalidMinimumStringLength()
    throws AppException, InformationalException {

    // Expectations
    expectationsParameters("123", "4", "6");

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateLength.getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("The length of the string is invalid", !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test invalid maximum string length.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalidMaximumStringLength()
    throws AppException, InformationalException {

    // Expectations
    expectationsParameters("12345689", "1", "6");

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateLength.getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("The length of the string is invalid", !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test invalid exception string length.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalidExceptionStringLength()
    throws AppException, InformationalException {

    // Expectations
    expectationsParameters("12345689", "A", "B");

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateLength.getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("The length of the string is invalid", !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }
}
