package curam.ca.gc.bdm.test.rules.functions;

import com.ibm.icu.util.Calendar;
import curam.datastore.impl.NoSuchSchemaException;
import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctiongetPreviousYear;
import curam.rules.functions.IEG2ExecutionContext;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.functor.Adaptor;
import curam.util.type.Date;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/**
 * The Class tests {@link CustomFunctiongetPreviousYear} custom function.
 */
@RunWith(JMockit.class)
public class CustomFunctiongetPreviousYearTest
  extends CustomFunctionTestJunit4 {

  /** A date in YYYYMMDD format */
  private final static String DATE = "20210405";

  /** The previous year for {@link #DATE} */
  private final static String PREVIOUS_YEAR = "2020";

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The rulesParameters. */
  @Mocked
  IEG2ExecutionContext ieg2ExecutionContext;

  /** The validate function. */
  @Tested
  CustomFunctiongetPreviousYear customFunction;

  public CustomFunctiongetPreviousYearTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() throws NoSuchSchemaException {

    customFunction = new CustomFunctiongetPreviousYear();
    ieg2Context = new IEG2Context();
  }

  /**
   * Test using logic
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_dynamic()
    throws AppException, InformationalException, NoSuchSchemaException {

    final String previousYear = Integer
      .toString(Date.getCurrentDate().getCalendar().get(Calendar.YEAR) - 1);

    // Test
    final Adaptor.StringAdaptor adaptorValue =
      (Adaptor.StringAdaptor) customFunction.getAdaptorValue(ieg2Context);

    assertEquals("Returns the previous year (calculated)", previousYear,
      adaptorValue.getStringValue(ieg2Context));
  }

  /**
   * Test using static values
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_static()
    throws AppException, InformationalException, NoSuchSchemaException {

    Date.overrideDate(DATE);

    // Test
    final Adaptor.StringAdaptor adaptorValue =
      (Adaptor.StringAdaptor) customFunction.getAdaptorValue(ieg2Context);

    assertEquals("Returns the previous year (static)", PREVIOUS_YEAR,
      adaptorValue.getStringValue(ieg2Context));
  }

}
