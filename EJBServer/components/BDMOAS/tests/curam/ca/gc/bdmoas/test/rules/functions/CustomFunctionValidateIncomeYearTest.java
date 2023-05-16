package curam.ca.gc.bdmoas.test.rules.functions;

import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionValidateIncomeYear;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.CustomFunctor;
import java.util.ArrayList;
import java.util.List;
import mockit.Mock;
import mockit.MockUp;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/**
 * The Class tests {@link CustomFunctionValidateIncomeYearTest} custom
 * function.
 */

@RunWith(JMockit.class)
public class CustomFunctionValidateIncomeYearTest
  extends CustomFunctionTestJunit4 {

  private static final boolean IS_VALID = true;

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The tested class. */
  @Tested
  CustomFunctionValidateIncomeYear validateIncomeYear;

  /**
   * Instantiates a new CustomFunctionValidateIncomeYearTest.
   */
  public CustomFunctionValidateIncomeYearTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    validateIncomeYear = new CustomFunctionValidateIncomeYear();
    ieg2Context = new IEG2Context();
  }

  /**
   * Test a valid year.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateIncomeYear_Success()
    throws AppException, InformationalException {

    final String year = "2022";

    // Expectations
    expectationsParameters(year);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateIncomeYear
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The year should be valid. ", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test an invalid year
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateIncomeYear_Failure()
    throws AppException, InformationalException {

    final String year = "2023";

    // Expectations
    expectationsParameters(year);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateIncomeYear
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The year should not be valid. ", !IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test an invalid year
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateIncomeYear_FailureAlpha()
    throws AppException, InformationalException {

    final String year = "abc";

    // Expectations
    expectationsParameters(year);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateIncomeYear
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The year should not be valid. ", !IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test an null year
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateIncomeYear_Null()
    throws AppException, InformationalException {

    // to test null check
    new MockUp<CustomFunctor>() {

      @Mock
      public List<Adaptor> getParameters() {

        final List<Adaptor> parameters = new ArrayList<>();

        final Adaptor c = null;
        parameters.add(c);

        return parameters;
      }

    };

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateIncomeYear
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The year should be valid. ", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

}
