package curam.ca.gc.bdm.test.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.ieg.impl.IEG2Context;
import curam.ieg.impl.IEGScriptExecution;
import curam.ieg.impl.IEGScriptExecutionFactory;
import curam.rules.functions.CustomFunctionValidateDatastoreSINValueChanged;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.type.Date;
import java.util.Objects;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/**
 * The Class tests {@link CustomFunctionValidateDatastoreSINValueChanged} custom
 * function.
 */
@RunWith(JMockit.class)
public class CustomFunctionValidateDatastoreSINValueChangedTest
  extends CustomFunctionTestJunit4 {

  /** The Constant IS_VALID. */
  private static final boolean IS_VALID = true;

  /** A valid SIN number. */
  private static final String VALID_SIN = "865000111";

  /** A valid sinRawDate. */
  private static final Date VALID_SIN_RAW_DATE = Date.getCurrentDate();

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The script execution. */
  @Mocked
  IEGScriptExecution scriptExecution;

  /** The script execution factory. */
  @Mocked
  IEGScriptExecutionFactory scriptExecutionFactory;

  /** The datastore. */
  @Mocked
  Datastore datastore;

  /** The rulesParameters. */
  @Mocked
  RulesParameters rulesParameters;

  /** The bdmApplicationEventsUtil. */
  @Mocked
  BDMApplicationEventsUtil bdmApplicationEventsUtil;

  /** The datastore factory. */
  @Mocked
  DatastoreFactory datastoreFactory;

  /** The root entity. */
  @Mocked
  Entity rootEntity;

  /** The person entity. */
  @Mocked
  Entity personEntity;

  /** The validate length. */
  @Tested
  CustomFunctionValidateDatastoreSINValueChanged validateDatastoreSIN;

  public CustomFunctionValidateDatastoreSINValueChangedTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() throws NoSuchSchemaException {

    validateDatastoreSIN =
      new CustomFunctionValidateDatastoreSINValueChanged();
    ieg2Context = new IEG2Context();
  }

  /**
   * Expectations for the SIN attribute.
   *
   * @param datastoreSinRaw the value of the attribute
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   * @throws NoSuchSchemaException the no such schema exception
   */
  private void expectationSinAttributes(final String datastoreSinRaw,
    final Date sinRawDate)
    throws AppException, InformationalException, NoSuchSchemaException {

    // If sinRawDate is null, only then will this expectation be required in the
    // customFunction
    if (Objects.isNull(sinRawDate)) {
      new Expectations() {

        {
          personEntity.getTypedAttribute(BDMDatastoreConstants.SIN);
          result = datastoreSinRaw;

        }
      };
    }
  }

  /**
   * SIN Changed, Date empty String, Negative Result
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalid_SINChange_NoDate()
    throws AppException, InformationalException, NoSuchSchemaException {

    // Test Data
    final String scriptSinRaw = "123456799";
    final Date sinRawDate = null;
    final String resultMessage =
      "Datastore SIN does not match Script SIN, Date required but not provided";

    // Expectations
    expectationsParameters(scriptSinRaw, sinRawDate);
    expectationSinAttributes(VALID_SIN, sinRawDate);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateDatastoreSIN
        .getAdaptorValue(ieg2Context);

    // Verifications,
    assertEquals(resultMessage, !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * SIN unchanged, Date empty String, Positive Result
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_SINSame_NoDate()
    throws AppException, InformationalException, NoSuchSchemaException {

    // Test Data
    final String scriptSinRaw = VALID_SIN;
    final Date sinRawDate = null;
    final String resultMessage =
      "Datastore SIN matches Script SIN, Date not required";

    // Expectations
    expectationsParameters(scriptSinRaw, sinRawDate);
    expectationSinAttributes(VALID_SIN, sinRawDate);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateDatastoreSIN
        .getAdaptorValue(ieg2Context);

    // Verifications,
    assertEquals(resultMessage, IS_VALID, adaptorValue.getValue(ieg2Context));
  }

  /**
   * SIN Changed, Date added, Positive Result
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_SINChange_DateAdded()
    throws AppException, InformationalException, NoSuchSchemaException {

    // Test Data
    final String scriptSinRaw = "123456799";
    final Date sinRawDate = VALID_SIN_RAW_DATE;
    final String resultMessage =
      "Datastore SIN does not match Script SIN, Date required and provided";

    // Expectations
    expectationsParameters(scriptSinRaw, sinRawDate);
    expectationSinAttributes(VALID_SIN, sinRawDate);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateDatastoreSIN
        .getAdaptorValue(ieg2Context);

    // Verifications,
    assertEquals(resultMessage, IS_VALID, adaptorValue.getValue(ieg2Context));
  }

  /**
   * SIN Unchanged, Date added, Positive Result
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_SINSame_DateAdded()
    throws AppException, InformationalException, NoSuchSchemaException {

    // Test Data
    final String scriptSinRaw = VALID_SIN;
    final Date sinRawDate = VALID_SIN_RAW_DATE;
    final String resultMessage =
      "Datastore SIN matches Script SIN, Date not required but provided";

    // Expectations
    expectationsParameters(scriptSinRaw, sinRawDate);
    expectationSinAttributes(VALID_SIN, sinRawDate);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateDatastoreSIN
        .getAdaptorValue(ieg2Context);

    // Verifications,
    assertEquals(resultMessage, IS_VALID, adaptorValue.getValue(ieg2Context));
  }

}
