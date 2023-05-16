package curam.ca.gc.bdmoas.test.rules.functions;

import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionValidatePhoneType;
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
 * The Class tests {@link CustomFunctionValidatePhoneTypeTest} custom
 * function.
 */

@RunWith(JMockit.class)
public class CustomFunctionValidatePhoneTypeTest
  extends CustomFunctionTestJunit4 {

  private static final boolean IS_VALID = true;

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The tested class. */
  @Tested
  CustomFunctionValidatePhoneType validatePhoneType;

  /**
   * Instantiates a new CustomFunctionvalidatePhoneNumberCountryTest.
   */
  public CustomFunctionValidatePhoneTypeTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    validatePhoneType = new CustomFunctionValidatePhoneType();
    ieg2Context = new IEG2Context();
  }

  /**
   * Test a valid phone type.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validatePhoneType_Success()
    throws AppException, InformationalException {

    final String phoneNumber = "9991234";
    final String phoneType = "business";
    // Expectations
    expectationsParameters(phoneType, phoneNumber);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validatePhoneType.getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The phone type should be valid", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test when phone type is null.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validatePhoneType_Null()
    throws AppException, InformationalException {

    final String phoneNumber = "9991234";
    final String phoneType = null;
    // Expectations
    expectationsParameters(phoneType, phoneNumber);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validatePhoneType.getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The phone type is not valid.", !IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test when phone type is empty.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validatePhoneType_Empty()
    throws AppException, InformationalException {

    final String phoneNumber = "9991234";
    final String phoneType = GeneralConstants.kEmpty;
    // Expectations
    expectationsParameters(phoneType, phoneNumber);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validatePhoneType.getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The phone type is not valid.", !IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

}
