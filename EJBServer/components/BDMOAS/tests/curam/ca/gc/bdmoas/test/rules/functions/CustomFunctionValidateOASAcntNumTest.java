package curam.ca.gc.bdmoas.test.rules.functions;

import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionValidateOASAcntNum;
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
 * The Class tests {@link CustomFunctionValidateOASAcntNumTest} custom
 * function.
 * failures - size over 9 length, non-digit chars, must start w 0
 */

@RunWith(JMockit.class)
public class CustomFunctionValidateOASAcntNumTest
  extends CustomFunctionTestJunit4 {

  private static final boolean IS_VALID = true;

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The tested class. */
  @Tested
  CustomFunctionValidateOASAcntNum validateOASAcntNum;

  /**
   * Instantiates a new CustomFunctionValidateOASAcntNumTest.
   */
  public CustomFunctionValidateOASAcntNumTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    validateOASAcntNum = new CustomFunctionValidateOASAcntNum();
    ieg2Context = new IEG2Context();
  }

  /**
   * Test a valid OAS number
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateOASAcntNum_Success()
    throws AppException, InformationalException {

    final String accountNumber = "012345678";

    // Expectations
    expectationsParameters(accountNumber);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateOASAcntNum
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("This account number should be valid.", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test validate when the account number is not 9 digits in length.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */

  @Test
  public void test_validateOASAcntNum_NotNineDigits()
    throws AppException, InformationalException {

    final String accountNumber = "0123456789";

    // Expectations
    expectationsParameters(accountNumber);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateOASAcntNum
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("This account number should be invalid.", !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test validate account number when not starting with 0.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validatePhoneCountry_EmptyString()
    throws AppException, InformationalException {

    final String accountNumber = "812345670";

    // Expectations
    expectationsParameters(accountNumber);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateOASAcntNum
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("This account number should be invalid.", !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test validate account number with containing alphabet char.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateOASAcntNum_AlphaChar()
    throws AppException, InformationalException {

    final String accountNumber = "A1B3C5678";

    // Expectations
    expectationsParameters(accountNumber);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateOASAcntNum
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("This account number should be invalid.", !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

}
