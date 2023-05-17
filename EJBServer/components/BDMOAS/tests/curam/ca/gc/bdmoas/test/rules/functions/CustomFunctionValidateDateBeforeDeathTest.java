package curam.ca.gc.bdmoas.test.rules.functions;

import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionValidateDateBeforeDeath;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.functor.Adaptor;
import curam.util.type.Date;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/**
 * The Class tests {@link CustomFunctionValidateDateBeforeDeathTest} custom
 * function.
 */

@RunWith(JMockit.class)
public class CustomFunctionValidateDateBeforeDeathTest
  extends CustomFunctionTestJunit4 {

  private static final boolean IS_VALID = true;

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The tested class. */
  @Tested
  CustomFunctionValidateDateBeforeDeath validateDateBeforeDeath;

  /**
   * Instantiates a new CustomFunctionValidateDateBeforeDeathTest.
   */
  public CustomFunctionValidateDateBeforeDeathTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    validateDateBeforeDeath = new CustomFunctionValidateDateBeforeDeath();
    ieg2Context = new IEG2Context();
  }

  /**
   * Test a valid date.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateDateBeforeDeath_Success()
    throws AppException, InformationalException {

    final Date checkedDate = Date.getDate("20221212");
    final Date deathDate = Date.getDate("20231212");

    // Expectations
    expectationsParameters(checkedDate, deathDate);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateDateBeforeDeath
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The year should be valid. ", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test a valid date.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateDateBeforeDeath_Failure()
    throws AppException, InformationalException {

    final Date checkedDate = Date.getDate("20231212");
    final Date deathDate = Date.getDate("20221212");

    // Expectations
    expectationsParameters(checkedDate, deathDate);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateDateBeforeDeath
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The year should not be valid. ", !IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }
}
