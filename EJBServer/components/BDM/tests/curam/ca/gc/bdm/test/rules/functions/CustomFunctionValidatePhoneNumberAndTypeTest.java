package curam.ca.gc.bdm.test.rules.functions;

import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionValidatePhoneNumberAndType;
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
 * The Class tests {@link CustomFunctionValidatePhoneNumberAndTypeTest} custom
 * function.
 */
@RunWith(JMockit.class)
public class CustomFunctionValidatePhoneNumberAndTypeTest
  extends CustomFunctionTestJunit4 {

  private static final boolean IS_VALID = true;

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The tested class. */
  @Tested
  CustomFunctionValidatePhoneNumberAndType validatePhoneNumberAndType;

  /**
   * Instantiates a new CustomFunctionvalidatePhoneNumberTest.
   */
  public CustomFunctionValidatePhoneNumberAndTypeTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    validatePhoneNumberAndType =
      new CustomFunctionValidatePhoneNumberAndType();
    ieg2Context = new IEG2Context();
  }

  /**
   * Test an invalid phone number.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalidatePhoneNumber()
    throws AppException, InformationalException {

    final String phoneNumber = GeneralConstants.kEmpty;
    final String phoneType = "business";
    // Expectations
    expectationsParameters(phoneNumber, phoneType);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validatePhoneNumberAndType
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The phone number should be invalid", !IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test a valid phone number.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validatePhoneNumber_Success()
    throws AppException, InformationalException {

    final String phoneNumber = "9991234";
    final String phoneType = "business";
    // Expectations
    expectationsParameters(phoneNumber, phoneType);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validatePhoneNumberAndType
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The phone number and type should be valid", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test validate phone number when the parameter contains a null object.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validatePhoneNumber_NullParams()
    throws AppException, InformationalException {

    final String phoneNumber = null;
    final String phoneType = null;
    // Expectations
    expectationsParameters(phoneNumber, phoneType);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validatePhoneNumberAndType
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("The phone number and type should be valid", IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test validate phone number and type on an empty string.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validatePhoneNumber_EmptyString()
    throws AppException, InformationalException {

    final String phoneNumber = GeneralConstants.kEmpty;
    final String phoneType = GeneralConstants.kEmpty;
    // Expectations
    expectationsParameters(phoneNumber, phoneType);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validatePhoneNumberAndType
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("The phone number and type should be valid", IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test validate phone number without a phone Type.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validatePhoneNumberNoPhoneType()
    throws AppException, InformationalException {

    final String phoneNumber = "9991234";
    final String phoneType = GeneralConstants.kEmpty;
    // Expectations
    expectationsParameters(phoneNumber, phoneType);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validatePhoneNumberAndType
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("The phone type should be entered", !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

}
