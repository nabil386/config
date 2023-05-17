package curam.ca.gc.bdm.test.rules.functions;

import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionValidateInstitutionNameLength;
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
public class CustomFunctionValidateInstitutionNameLengthTest
  extends CustomFunctionTestJunit4 {

  /** The Constant IS_VALID. */
  private static final boolean IS_VALID = true;

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The validate length. */
  @Tested
  CustomFunctionValidateInstitutionNameLength validateInstitutionNameLength;

  /**
   * Instantiates a new CustomFunctionvalidateLengthTest.
   */
  public CustomFunctionValidateInstitutionNameLengthTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    validateInstitutionNameLength =
      new CustomFunctionValidateInstitutionNameLength();
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
    expectationsParameters(
      "This Institution Name has exactly sixty five characters in length", "",
      "65");

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateInstitutionNameLength
        .getAdaptorValue(ieg2Context);

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
    expectationsParameters("I", "1", "");

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateInstitutionNameLength
        .getAdaptorValue(ieg2Context);

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
    expectationsParameters("Instit", "6", "6");

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateInstitutionNameLength
        .getAdaptorValue(ieg2Context);

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
    expectationsParameters("In", "3", "6");

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateInstitutionNameLength
        .getAdaptorValue(ieg2Context);

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
    expectationsParameters("Institution", "1", "6");

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateInstitutionNameLength
        .getAdaptorValue(ieg2Context);

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
    expectationsParameters("Institution", "A", "B");

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateInstitutionNameLength
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("The length of the string is invalid", !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test no numerals allowed.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalidExceptionNumerals()
    throws AppException, InformationalException {

    // Expectations
    expectationsParameters("Institution est 1956", "1", "65");

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateInstitutionNameLength
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Numerals are invalid", !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test no special chars allowed.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalidExceptionSpecialChars()
    throws AppException, InformationalException {

    // Expectations
    expectationsParameters("Institution.", "1", "65");

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateInstitutionNameLength
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Special Chars are invalid", !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test only spaces chars not allowed.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalidExceptionOnlySpaces()
    throws AppException, InformationalException {

    // Expectations
    expectationsParameters("  ", "1", "65");

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateInstitutionNameLength
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Only Spaces is invalid", !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

}
