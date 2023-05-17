package curam.ca.gc.bdm.test.rules.functions;

import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.ieg.impl.IEG2Context;
import curam.ieg.impl.IEGScriptExecution;
import curam.ieg.impl.IEGScriptExecutionFactory;
import curam.rules.functions.CustomFunctionValidateSINLength;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.functor.Adaptor;
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
 * The Class tests {@link CustomFunctionValidateSINLength} custom
 * function.
 */
@RunWith(JMockit.class)
public class CustomFunctionValidateSINLengthTest
  extends CustomFunctionTestJunit4 {

  /** The Constant IS_VALID. */
  private static final boolean IS_VALID = true;

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

  /** The validate length. */
  @Tested
  CustomFunctionValidateSINLength validateSINLength;

  /**
   * Instantiates a new CustomFunctionValidateSINLengthTest.
   */
  public CustomFunctionValidateSINLengthTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    validateSINLength = new CustomFunctionValidateSINLength();
    ieg2Context = new IEG2Context();
  }

  /**
   * Test invalid SIN lenght.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalidSINLenght()
    throws AppException, InformationalException {

    // Expectations
    final String sin = " 04  44  28";
    expectationsParameters(sin);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateSINLength.getAdaptorValue(ieg2Context);

    // Verifications
    verifycontinueSINAttributeIsUpdated(!IS_VALID, 1);

    assertEquals("The lenght of the SIN is invalid", !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test valid SIN lenght.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validSINLenght()
    throws AppException, InformationalException {

    // Expectations
    final String sin = "046 454 286";
    expectationsParameters(sin);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateSINLength.getAdaptorValue(ieg2Context);

    // Verifications
    verifycontinueSINAttributeIsUpdated(IS_VALID, 1);

    assertEquals("The lenght of the SIN is valid", IS_VALID,
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
