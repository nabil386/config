package curam.ca.gc.bdmoas.test.rules.functions;

import curam.codetable.COUNTRY;
import curam.codetable.IEGYESNO;
import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionValidateForeignIdentifier;
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
 * The Class tests {@link CustomFunctionValidateForeignIdentifierTest} custom
 * function.
 */

@RunWith(JMockit.class)
public class CustomFunctionValidateForeignIdentifierTest
  extends CustomFunctionTestJunit4 {

  private static final boolean IS_VALID = true;

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The tested class. */
  @Tested
  CustomFunctionValidateForeignIdentifier validateForeignIdentifier;

  /**
   * Instantiates a new CustomFunctionValidateForeignIdentifierTest.
   */
  public CustomFunctionValidateForeignIdentifierTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    validateForeignIdentifier = new CustomFunctionValidateForeignIdentifier();
    ieg2Context = new IEG2Context();
  }

  /**
   * When foreign info entered, id number must be entered
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateForeignIdentifier_Success()
    throws AppException, InformationalException {

    final String country = COUNTRY.US;
    final String haveWorked = IEGYESNO.YES;
    final String id = "12345678912345678912";

    // Expectations
    expectationsParameters(country, haveWorked, id);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateForeignIdentifier
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The year should be valid. ", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * When canada info entered, not needed
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateForeignIdentifier_Canada()
    throws AppException, InformationalException {

    final String country = COUNTRY.CA;
    final String haveWorked = IEGYESNO.YES;

    // Expectations
    expectationsParameters(country, haveWorked);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateForeignIdentifier
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The year should be valid. ", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * When foreign info entered, id number missing
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateForeignIdentifier_Failure()
    throws AppException, InformationalException {

    final String country = COUNTRY.US;
    final String haveWorked = IEGYESNO.YES;
    final String id = "";

    // Expectations
    expectationsParameters(country, haveWorked, id);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateForeignIdentifier
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The year should not be valid. ", !IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * When foreign info entered, id number missing
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateForeignIdentifier_FailureNull()
    throws AppException, InformationalException {

    final String country = COUNTRY.US;
    final String haveWorked = IEGYESNO.YES;
    final String id = null;

    // Expectations
    expectationsParameters(country, haveWorked, id);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateForeignIdentifier
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The year should not be valid. ", !IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }
}
