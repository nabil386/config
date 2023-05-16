package curam.ca.gc.bdmoas.test.rules.functions;

import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionValidateMailingAddressFields;
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
 * The Class tests {@link CustomFunctionValidateMailingAddressFieldsTest} custom
 * function.
 */

@RunWith(JMockit.class)
public class CustomFunctionValidateMailingAddressFieldsTest
  extends CustomFunctionTestJunit4 {

  private static final boolean IS_VALID = true;

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The tested class. */
  @Tested
  CustomFunctionValidateMailingAddressFields validateMailingAddressFields;

  /**
   * Instantiates a new CustomFunctionValidateMailingAddressFieldsTest.
   */
  public CustomFunctionValidateMailingAddressFieldsTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    validateMailingAddressFields =
      new CustomFunctionValidateMailingAddressFields();
    ieg2Context = new IEG2Context();
  }

  /**
   * Test a valid Mailing address.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateMailingAddressFields_Success()
    throws AppException, InformationalException {

    final String pobox = GeneralConstants.kEmpty;
    final String streetNumber = "77";
    final String streetName = "Imagine Street";
    // Expectations
    expectationsParameters(pobox, streetNumber, streetName);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateMailingAddressFields
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The address should be valid", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test a valid Mailing address.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateMailingAddressFields_SuccessTwo()
    throws AppException, InformationalException {

    final String pobox = "22";
    final String streetNumber = GeneralConstants.kEmpty;
    final String streetName = GeneralConstants.kEmpty;
    // Expectations
    expectationsParameters(pobox, streetNumber, streetName);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateMailingAddressFields
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The address should be valid", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test a invalid Mailing address.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateMailingAddressFields_MissingNum()
    throws AppException, InformationalException {

    final String pobox = "23";
    final String streetNumber = GeneralConstants.kEmpty;
    final String streetName = "Imagine Street";
    // Expectations
    expectationsParameters(pobox, streetNumber, streetName);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateMailingAddressFields
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The address should not be valid", !IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test a invalid Mailing address.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateMailingAddressFields_MissingName()
    throws AppException, InformationalException {

    final String pobox = "23";
    final String streetNumber = "77";
    final String streetName = GeneralConstants.kEmpty;
    // Expectations
    expectationsParameters(pobox, streetNumber, streetName);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateMailingAddressFields
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The address should not be valid", !IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test a invalid Mailing address.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateMailingAddressFields_MissingAll()
    throws AppException, InformationalException {

    final String pobox = GeneralConstants.kEmpty;
    final String streetNumber = GeneralConstants.kEmpty;
    final String streetName = GeneralConstants.kEmpty;
    // Expectations
    expectationsParameters(pobox, streetNumber, streetName);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateMailingAddressFields
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The address should not be valid", !IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test a invalid Mailing address.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateMailingAddressFields_All()
    throws AppException, InformationalException {

    final String pobox = "33";
    final String streetNumber = "44";
    final String streetName = "Imagine Road";
    // Expectations
    expectationsParameters(pobox, streetNumber, streetName);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateMailingAddressFields
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The address should not be valid", !IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

}
