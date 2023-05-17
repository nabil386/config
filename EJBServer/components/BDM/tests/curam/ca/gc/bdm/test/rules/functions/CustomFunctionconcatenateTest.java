package curam.ca.gc.bdm.test.rules.functions;

import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionconcatenate;
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
 * The Class tests {@link CustomFunctionconcatenate} custom
 * function.
 */
@RunWith(JMockit.class)
public class CustomFunctionconcatenateTest extends CustomFunctionTestJunit4 {

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The tested class. */
  @Tested
  CustomFunctionconcatenate concatenate;

  /**
   * Instantiates a new CustomFunctionconcatenateTest.
   */
  public CustomFunctionconcatenateTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    concatenate = new CustomFunctionconcatenate();
    ieg2Context = new IEG2Context();
  }

  /**
   * Test concatenate strings success.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_concatenateStrings_Success()
    throws AppException, InformationalException {

    final String firstName = "James ";
    final String lastName = "Smith";
    final String fullName = "James Smith";

    // Expectations
    expectationsParameters(firstName, lastName);

    // Test
    final Adaptor.StringAdaptor adaptorValue =
      (Adaptor.StringAdaptor) concatenate.getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Strings should match", fullName,
      adaptorValue.getStringValue(ieg2Context));
  }

  /**
   * Test concatenate strings failure, if one of the parameters is not provided
   * , returns an empty string.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_concatenateStrings_Failure()
    throws AppException, InformationalException {

    final String firstName = "James ";
    final String lastName = null;

    // Expectations
    expectationsParameters(firstName, lastName);

    // Test
    final Adaptor.StringAdaptor adaptorValue =
      (Adaptor.StringAdaptor) concatenate.getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Strings should match", GeneralConstants.kEmpty,
      adaptorValue.getStringValue(ieg2Context));
  }

}
