package curam.ca.gc.bdm.test.rules.functions;

import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionvalidateCAPostalCode;
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
 * The Class tests {@link CustomFunctionvalidateCAPostalCode} custom
 * function.
 */
@RunWith(JMockit.class)
public class CustomFunctionvalidateCAPostalCodeTest
  extends CustomFunctionTestJunit4 {

  /** The Constant VALID_POSTAL_CODE. */
  private static final boolean VALID_POSTAL_CODE = true;

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The validate CA postal code. */
  @Tested
  CustomFunctionvalidateCAPostalCode validateCAPostalCode;

  /**
   * Instantiates a new custom functionvalidate CA postal code test.
   */
  public CustomFunctionvalidateCAPostalCodeTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    validateCAPostalCode = new CustomFunctionvalidateCAPostalCode();
    ieg2Context = new IEG2Context();
  }

  /**
   * Test valid postal code.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validPostalCode()
    throws AppException, InformationalException {

    final String postalCode = "A1C 5B9";
    // Expectations
    expectationsParameters(postalCode);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateCAPostalCode
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Postal code should be valid", VALID_POSTAL_CODE,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test valid postal code lower case.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validPostalCodeLowerCase()
    throws AppException, InformationalException {

    final String postalCode = "a1c 5b9";
    // Expectations
    expectationsParameters(postalCode);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateCAPostalCode
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Postal code should be valid", VALID_POSTAL_CODE,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test invalid postal code pattern.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalidPostalCodePattern()
    throws AppException, InformationalException {

    final String postalCode = "ZZZZZZ";
    // Expectations
    expectationsParameters(postalCode);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateCAPostalCode
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Postal code should not be valid", !VALID_POSTAL_CODE,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test invalid postal code size.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalidPostalCodeSize()
    throws AppException, InformationalException {

    final String postalCode = "A1C 5B9 A1C 5B9";
    // Expectations
    expectationsParameters(postalCode);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateCAPostalCode
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Postal code should not be valid", VALID_POSTAL_CODE,
      adaptorValue.getValue(ieg2Context));
  }
}
