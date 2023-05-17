package curam.ca.gc.bdm.test.rules.functions;

import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionRemoveSpecialCharacters;
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
 * The Class tests {@link CustomFunctionRemoveSpecialCharacters} custom
 * function.
 */
@RunWith(JMockit.class)
public class CustomFunctionRemoveSpecialCharactersTest
  extends CustomFunctionTestJunit4 {

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The tested class. */
  @Tested
  CustomFunctionRemoveSpecialCharacters removeSpecialCharacters;

  /**
   * Instantiates a new CustomFunctionRemoveSpecialCharactersTest.
   */
  public CustomFunctionRemoveSpecialCharactersTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    removeSpecialCharacters = new CustomFunctionRemoveSpecialCharacters();
    ieg2Context = new IEG2Context();
  }

  /**
   * Test remove special characters successfully.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_removeSpecialCharacters_Success()
    throws AppException, InformationalException {

    final String str = " )(*&^%$#@! Homer  )(*&^%$#@.,`";
    final String expectedStr = "Homer";

    // Expectations
    expectationsParameters(str);

    // Test
    final Adaptor.StringAdaptor adaptorValue =
      (Adaptor.StringAdaptor) removeSpecialCharacters
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Strings should match", expectedStr,
      adaptorValue.getStringValue(ieg2Context));
  }

  /**
   * Test remove special characters when no params are provided.
   * An empty string is expected as a result.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_removeSpecialCharacters_NoParams()
    throws AppException, InformationalException {

    final String expectedStr = GeneralConstants.kEmpty;

    // Expectations
    expectationsParameters();

    // Test
    final Adaptor.StringAdaptor adaptorValue =
      (Adaptor.StringAdaptor) removeSpecialCharacters
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Strings should match", expectedStr,
      adaptorValue.getStringValue(ieg2Context));
  }

  /**
   * Test remove special characters when the parameter contains a null object.
   * An empty string is expected as a result.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_removeSpecialCharacters_NullParams()
    throws AppException, InformationalException {

    final String str = null;
    final String expectedStr = GeneralConstants.kEmpty;

    // Expectations
    expectationsParameters(str);

    // Test
    final Adaptor.StringAdaptor adaptorValue =
      (Adaptor.StringAdaptor) removeSpecialCharacters
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Strings should match", expectedStr,
      adaptorValue.getStringValue(ieg2Context));
  }

  /**
   * Test remove special characters on an empty string.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_removeSpecialCharacters_EmptyString()
    throws AppException, InformationalException {

    final String str = GeneralConstants.kEmpty;
    final String expectedStr = GeneralConstants.kEmpty;

    // Expectations
    expectationsParameters(str);

    // Test
    final Adaptor.StringAdaptor adaptorValue =
      (Adaptor.StringAdaptor) removeSpecialCharacters
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Strings should match", expectedStr,
      adaptorValue.getStringValue(ieg2Context));
  }
}
