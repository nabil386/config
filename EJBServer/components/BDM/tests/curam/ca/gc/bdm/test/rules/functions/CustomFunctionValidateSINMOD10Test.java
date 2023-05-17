package curam.ca.gc.bdm.test.rules.functions;

import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.ieg.impl.IEG2Context;
import curam.ieg.impl.IEGScriptExecution;
import curam.ieg.impl.IEGScriptExecutionFactory;
import curam.rules.functions.CustomFunctionValidateSINMOD10;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.functor.Adaptor;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static curam.rules.functions.CustomFunctionConstants.CONTINUE_ATTRIBUTE;
import static org.junit.Assert.assertEquals;

/**
 * The Class tests {@link CustomFunctionValidateSINMOD10} custom
 * function.
 */
@RunWith(JMockit.class)
public class CustomFunctionValidateSINMOD10Test
  extends CustomFunctionTestJunit4 {

  /** The Constant IS_VALID_SIN. */
  private static final boolean IS_VALID_SIN = true;

  /** A valid SIN number. */
  private static final String VALID_SIN = "046 454 286";

  /** The datastore. */
  @Mocked
  Datastore datastore;

  /** The datastore factory. */
  @Mocked
  DatastoreFactory datastoreFactory;

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The root entity. */
  @Mocked
  Entity rootEntity;

  /** The script execution. */
  @Mocked
  IEGScriptExecution scriptExecution;

  /** The script execution factory. */
  @Mocked
  IEGScriptExecutionFactory scriptExecutionFactory;

  /** The validate SINMOD 10. */
  @Tested
  CustomFunctionValidateSINMOD10 validateSINMOD10;

  /**
   * Instantiates a new CustomFunctionValidateSINMOD10Test.
   */
  public CustomFunctionValidateSINMOD10Test() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    validateSINMOD10 = new CustomFunctionValidateSINMOD10();
    ieg2Context = new IEG2Context();
  }

  /**
   * Expectations for the continueSIN attribute.
   *
   * @param continueSin the value of the attribute
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   * @throws NoSuchSchemaException the no such schema exception
   */
  private void expectationSINAttribute(final boolean continueSin)
    throws AppException, InformationalException, NoSuchSchemaException {

    new Expectations() {

      {
        scriptExecutionFactory.getScriptExecutionObject(anyLong);
        result = scriptExecution;

        datastoreFactory.openDatastore(scriptExecution.getSchemaName());
        result = datastore;

        rootEntity.getTypedAttribute(CONTINUE_ATTRIBUTE);
        result = continueSin;

      }
    };
  }

  /**
   * Test valid SIN due to a incorrect number, when the attribute continueSIN is
   * set to true.
   * As the continueSIN is set to true, it is expected that the root entity is
   * updated with the result of the evaluation
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   * @throws NoSuchSchemaException the no such schema exception
   */
  @Test
  public void test_invalidNumberSIN_ContinueSIN_true()
    throws AppException, InformationalException, NoSuchSchemaException {

    final String sin = "XXX XXX XXX";
    final boolean isContinueSIN = true;
    // Expectations
    expectationsParameters(sin);

    expectationSINAttribute(isContinueSIN);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateSINMOD10.getAdaptorValue(ieg2Context);

    // Verifications
    verifycontinueSINAttributeIsUpdated(!IS_VALID_SIN, 1);

    // Verifications
    assertEquals("SIN should be invalid", !IS_VALID_SIN,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test valid SIN when the attribute continueSIN is set to false.
   * As the continueSIN is set to false, the callout must short circuit
   * returning a true value without updating the root entity in the datastore.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   * @throws NoSuchSchemaException the no such schema exception
   */
  @Test
  public void test_invalidSIN_ContinueSIN_false()
    throws AppException, InformationalException, NoSuchSchemaException {

    final String sin = "123 123 123";
    final boolean isContinueSIN = false;
    // Expectations
    expectationsParameters(sin);

    expectationSINAttribute(isContinueSIN);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateSINMOD10.getAdaptorValue(ieg2Context);

    // Verifications
    verifycontinueSINAttributeIsUpdated(!IS_VALID_SIN, 0);

    // Verifications
    assertEquals("SIN should be valid", IS_VALID_SIN,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test invalid SIN when the attribute continueSIN is set to true.
   * As the continueSIN is set to true, it is expected that the root entity is
   * updated with the result of the evaluation
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   * @throws NoSuchSchemaException the no such schema exception
   */
  @Test
  public void test_invalidSIN_ContinueSIN_true()
    throws AppException, InformationalException, NoSuchSchemaException {

    final String sin = "123 123 123";
    final boolean isContinueSIN = true;
    // Expectations
    expectationsParameters(sin);

    expectationSINAttribute(isContinueSIN);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateSINMOD10.getAdaptorValue(ieg2Context);

    // Verifications
    verifycontinueSINAttributeIsUpdated(!IS_VALID_SIN, 1);

    // Verifications
    assertEquals("SIN should be invalid", !IS_VALID_SIN,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test valid SIN due to wrong size of the SIN number, when the attribute
   * continueSIN is set to true.
   * As the continueSIN is set to true, it is expected that the root entity is
   * updated with the result of the evaluation
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   * @throws NoSuchSchemaException the no such schema exception
   */
  @Test
  public void test_invalidSizeSIN_ContinueSIN_true()
    throws AppException, InformationalException, NoSuchSchemaException {

    final String sin = "123 123";
    final boolean isContinueSIN = true;
    // Expectations
    expectationsParameters(sin);

    expectationSINAttribute(isContinueSIN);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateSINMOD10.getAdaptorValue(ieg2Context);

    // Verifications
    verifycontinueSINAttributeIsUpdated(!IS_VALID_SIN, 1);

    // Verifications
    assertEquals("SIN should be invalid", !IS_VALID_SIN,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test valid SIN when the attribute continueSIN is set to false.
   * As the continueSIN is set to false, the callout must short circuit
   * returning a true value without updating the root entity in the datastore.
   *
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   * @throws NoSuchSchemaException the no such schema exception
   */
  @Test
  public void test_validSIN_ContinueSIN_false()
    throws AppException, InformationalException, NoSuchSchemaException {

    final boolean isContinueSIN = false;
    // Expectations
    expectationsParameters(VALID_SIN);

    expectationSINAttribute(isContinueSIN);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateSINMOD10.getAdaptorValue(ieg2Context);

    // Verifications
    verifycontinueSINAttributeIsUpdated(isContinueSIN, 0);

    // Verifications
    assertEquals("SIN should be valid", IS_VALID_SIN,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test valid SIN when the attribute continueSIN is set to true.
   * As the continueSIN is set to true, it is expected that the root entity is
   * updated with the result of the evaluation
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   * @throws NoSuchSchemaException the no such schema exception
   */
  @Test
  public void test_validSIN_ContinueSIN_true()
    throws AppException, InformationalException, NoSuchSchemaException {

    final boolean isContinueSIN = true;
    // Expectations
    expectationsParameters(VALID_SIN);

    expectationSINAttribute(isContinueSIN);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateSINMOD10.getAdaptorValue(ieg2Context);

    // Verifications
    verifycontinueSINAttributeIsUpdated(isContinueSIN, 1);

    // Verifications
    assertEquals("SIN should be valid", IS_VALID_SIN,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Verifications for continueSIN attribute is updated.
   *
   * @param isContinueSIN the attribute value.
   * @param continueSINAttributeUpdateTimes the number of times the root entity
   * must be updated.
   */
  private void verifycontinueSINAttributeIsUpdated(
    final boolean isContinueSIN, final int continueSINAttributeUpdateTimes) {

    new Verifications() {

      {
        rootEntity.setTypedAttribute(CONTINUE_ATTRIBUTE, isContinueSIN);
        times = continueSINAttributeUpdateTimes;
        rootEntity.update();
        times = continueSINAttributeUpdateTimes;
      }
    };
  }
}
