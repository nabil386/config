package curam.ca.gc.bdmoas.test.rules.functions;

import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionValidateCityProvincePostalCodeLength;
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
 * {@link CustomFunctionValidateCityProvincePostalCodeLengthTest} custom
 * function.
 */

@RunWith(JMockit.class)
public class CustomFunctionValidateCityProvincePostalCodeLengthTest
  extends CustomFunctionTestJunit4 {

  private static final boolean IS_VALID = true;

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The tested class. */
  @Tested
  CustomFunctionValidateCityProvincePostalCodeLength validateCityProvincePostalCodeLength;

  /**
   * Instantiates a new CustomFunctionvalidatePhoneNumberCountryTest.
   */
  public CustomFunctionValidateCityProvincePostalCodeLengthTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    validateCityProvincePostalCodeLength =
      new CustomFunctionValidateCityProvincePostalCodeLength();
    ieg2Context = new IEG2Context();
  }

  /**
   * Test a valid address length
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateCityProvincePostalCodeLength_Success()
    throws AppException, InformationalException {

    final String city = "ST.JOHN'S";
    final String province = "NL";
    final String postalCode = "A1A 1A1";
    // Expectations
    expectationsParameters(city, province, postalCode);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateCityProvincePostalCodeLength
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
  public void test_validateCityProvincePostalCodeLength_Failure()
    throws AppException, InformationalException {

    final String city = "12345678901234567890123345768";
    final String province = "NL";
    final String postalCode = "A1A 1A1";
    // Expectations
    expectationsParameters(city, province, postalCode);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateCityProvincePostalCodeLength
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
  public void test_validateCityProvincePostalCodeLength_Empty()
    throws AppException, InformationalException {

    final String city = GeneralConstants.kEmpty;
    final String province = "NL";
    final String postalCode = "A1A 1A1";
    // Expectations
    expectationsParameters(city, province, postalCode);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateCityProvincePostalCodeLength
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
  public void test_validateCityProvincePostalCodeLength_Null()
    throws AppException, InformationalException {

    final String city = null;
    final String province = "NL";
    final String postalCode = "A1A 1A1";
    // Expectations
    expectationsParameters(city, province, postalCode);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateCityProvincePostalCodeLength
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The length should be valid", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

}
