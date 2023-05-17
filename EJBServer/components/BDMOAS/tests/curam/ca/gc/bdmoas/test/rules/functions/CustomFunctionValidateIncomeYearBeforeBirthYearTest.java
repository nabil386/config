package curam.ca.gc.bdmoas.test.rules.functions;

import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionValidateIncomeYearBeforeBirthYear;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.CustomFunctor;
import curam.util.type.Date;
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
 * The Class tests {@link CustomFunctionValidateIncomeYearBeforeBirthYearTest}
 * custom
 * function.
 */

@RunWith(JMockit.class)
public class CustomFunctionValidateIncomeYearBeforeBirthYearTest
  extends CustomFunctionTestJunit4 {

  private static final boolean IS_VALID = true;

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The tested class. */
  @Tested
  CustomFunctionValidateIncomeYearBeforeBirthYear validateIncomeYearBeforeBirthYear;

  /**
   * Instantiates a new CustomFunctionValidateIncomeYearBeforeBirthYearTest.
   */
  public CustomFunctionValidateIncomeYearBeforeBirthYearTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    validateIncomeYearBeforeBirthYear =
      new CustomFunctionValidateIncomeYearBeforeBirthYear();
    ieg2Context = new IEG2Context();
  }

  /**
   * Test a valid year.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateIncomeYearBeforeBirthYear_Success()
    throws AppException, InformationalException {

    final String year = "2021";
    final Date death = Date.getDate("19801212");

    // Expectations
    expectationsParameters(year, death);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateIncomeYearBeforeBirthYear
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
  public void test_validateIncomeYearBeforeBirthYear_Failure()
    throws AppException, InformationalException {

    final String year = "1999";
    final Date death = Date.getDate("20001212");

    // Expectations
    expectationsParameters(year, death);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateIncomeYearBeforeBirthYear
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
  public void test_validateIncomeYearBeforeBirthYear_Null()
    throws AppException, InformationalException {

    // to test null check
    new MockUp<CustomFunctor>() {

      @Mock
      public List<Adaptor> getParameters() {

        final List<Adaptor> parameters = new ArrayList<>();

        final Adaptor c = null;
        final Adaptor d = null;
        parameters.add(c);
        parameters.add(d);

        return parameters;
      }

    };

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateIncomeYearBeforeBirthYear
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("The year should be valid. ", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

}
