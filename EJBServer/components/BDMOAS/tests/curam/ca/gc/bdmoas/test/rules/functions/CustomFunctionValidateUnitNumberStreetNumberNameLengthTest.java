package curam.ca.gc.bdmoas.test.rules.functions;

import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionValidateUnitNumberStreetNumberNameLength;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.GeneralConstants;
import curam.util.rules.functor.Adaptor;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/**
 * The Class tests
 * {@link CustomFunctionValidateUnitNumberStreetNumberNameLengthTest} custom
 * function.
 */

@RunWith(JMockit.class)
public class CustomFunctionValidateUnitNumberStreetNumberNameLengthTest
  extends CustomFunctionTestJunit4 {

  private static final boolean IS_VALID = true;

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The tested class. */
  @Tested
  CustomFunctionValidateUnitNumberStreetNumberNameLength validateUnitNumberStreetNumberNameLength;

  /**
   * Instantiates a new CustomFunctionvalidatePhoneNumberCountryTest.
   */
  public CustomFunctionValidateUnitNumberStreetNumberNameLengthTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    validateUnitNumberStreetNumberNameLength =
      new CustomFunctionValidateUnitNumberStreetNumberNameLength();
    ieg2Context = new IEG2Context();
  }

  /**
   * Test a valid address length
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateUnitNumberStreetNumberNameLength_Success()
    throws AppException, InformationalException {

    final String unit = "33";
    final String street1 = "22";
    final String street2 = "solder road";
    // Expectations
    expectationsParameters(unit, street1, street2);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateUnitNumberStreetNumberNameLength
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The length should be valid", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test a invalid address length
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateUnitNumberStreetNumberNameLength_Failure()
    throws AppException, InformationalException {

    final String unit = "123456789123456789123456789123456789123456789";
    final String street1 = "22";
    final String street2 = "solder road";
    // Expectations
    expectationsParameters(unit, street1, street2);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateUnitNumberStreetNumberNameLength
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The length should be valid", !IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test a valid address length
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateUnitNumberStreetNumberNameLength_Empty()
    throws AppException, InformationalException {

    final String unit = GeneralConstants.kEmpty;
    final String street1 = "22";
    final String street2 = "solder road";
    // Expectations
    expectationsParameters(unit, street1, street2);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateUnitNumberStreetNumberNameLength
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The length should be valid", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test a valid address length
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateUnitNumberStreetNumberNameLength_Null()
    throws AppException, InformationalException {

    final String unit = GeneralConstants.kEmpty;
    final String street1 = "22";
    final String street2 = null;
    // Expectations
    expectationsParameters(unit, street1, street2);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateUnitNumberStreetNumberNameLength
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The length should be valid", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

}
