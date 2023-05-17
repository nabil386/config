package curam.ca.gc.bdmoas.test.rules.functions;

import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionValidatePhoneNumberCountry;
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
 * The Class tests {@link CustomFunctionValidatePhoneNumberCountryTest} custom
 * function.
 */

@RunWith(JMockit.class)
public class CustomFunctionValidatePhoneNumberCountryTest
  extends CustomFunctionTestJunit4 {

  private static final boolean IS_VALID = true;

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The tested class. */
  @Tested
  CustomFunctionValidatePhoneNumberCountry validatePhoneNumberCountry;

  /**
   * Instantiates a new CustomFunctionvalidatePhoneNumberCountryTest.
   */
  public CustomFunctionValidatePhoneNumberCountryTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    validatePhoneNumberCountry =
      new CustomFunctionValidatePhoneNumberCountry();
    ieg2Context = new IEG2Context();
  }

  /**
   * Test a valid phone number and country type.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validatePhoneCountry_Success()
    throws AppException, InformationalException {

    final String phoneCountry = "BDMPC80035";
    final String phoneNumber = "9991234";
    // Expectations
    expectationsParameters(phoneCountry, phoneNumber);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validatePhoneNumberCountry
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The phone number and country should be valid", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test validate when the parameters contains a null object.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */

  @Test
  public void test_validatePhoneCountry_NullParams()
    throws AppException, InformationalException {

    final String phoneCountry = null;
    final String phoneNumber = null;
    // Expectations
    expectationsParameters(phoneCountry, phoneNumber);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validatePhoneNumberCountry
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("The phone number and country should be valid", IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test validate phone country and number on an empty string.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validatePhoneCountry_EmptyString()
    throws AppException, InformationalException {

    final String phoneCountry = GeneralConstants.kEmpty;
    final String phoneNumber = GeneralConstants.kEmpty;
    // Expectations
    expectationsParameters(phoneCountry, phoneNumber);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validatePhoneNumberCountry
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("The phone number and country should be valid", IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test valid phone number without a phone country.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validatePhoneNumberNoCountry()
    throws AppException, InformationalException {

    final String phoneCountry = GeneralConstants.kEmpty;
    final String phoneNumber = "9991234";
    // Expectations
    expectationsParameters(phoneCountry, phoneNumber);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validatePhoneNumberCountry
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("The phone country should be entered", !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

}
