package curam.ca.gc.bdmoas.test.rules.functions;

import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionValidateFirstEntryDate;
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
 * The Class tests {@link CustomFunctionValidateFirstEntryDateTest} custom
 * function.
 */

@RunWith(JMockit.class)
public class CustomFunctionValidateFirstEntryDateTest
  extends CustomFunctionTestJunit4 {

  private static final boolean IS_VALID = true;

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The tested class. */
  @Tested
  CustomFunctionValidateFirstEntryDate validateFirstEntryDate;

  /**
   * Instantiates a new CustomFunctionValidateFirstEntryDateTest.
   */
  public CustomFunctionValidateFirstEntryDateTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    validateFirstEntryDate = new CustomFunctionValidateFirstEntryDate();
    ieg2Context = new IEG2Context();
  }

  /**
   * Test a valid date combination.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateFirstEntryDate_Success()
    throws AppException, InformationalException {

    final Date firstEntryDate = Date.getDate("20211201");
    final Date dateOfDeath = Date.getDate("20221101");
    // Expectations
    expectationsParameters(firstEntryDate, dateOfDeath);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateFirstEntryDate
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("This date combination should be invalid.", IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test a invalid date combination.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateFirstEntryDate_Failure()
    throws AppException, InformationalException {

    final Date firstEntryDate = Date.getDate("20221101");
    final Date dateOfDeath = Date.getDate("20211201");
    // Expectations
    expectationsParameters(firstEntryDate, dateOfDeath);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateFirstEntryDate
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("This date combination should be invalid.", !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

}
