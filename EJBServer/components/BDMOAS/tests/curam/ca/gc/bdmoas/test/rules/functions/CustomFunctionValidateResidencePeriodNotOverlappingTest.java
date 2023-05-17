package curam.ca.gc.bdmoas.test.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdmoas.application.impl.BDMOASDatastoreConstants;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.ieg.impl.IEG2Context;
import curam.ieg.impl.IEGScriptExecution;
import curam.ieg.impl.IEGScriptExecutionFactory;
import curam.rules.functions.BDMFunctor;
import curam.rules.functions.CustomFunctionValidateResidencePeriodNotOverlapping;
import curam.rules.functions.IEG2ExecutionContext;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.type.Date;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/**
 * The Class tests {@link CustomFunctionValidateResidencePeriodNotOverlapping}
 * custom
 * function.
 */

@RunWith(JMockit.class)
public class CustomFunctionValidateResidencePeriodNotOverlappingTest
  extends CustomFunctionTestJunit4 {

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The rulesParameters. */
  @Mocked
  IEG2ExecutionContext ieg2ExecutionContext;

  /** The script execution. */
  @Mocked
  IEGScriptExecution scriptExecution;

  BDMFunctor BDMFunctor;

  /** The script execution factory. */
  @Mocked
  IEGScriptExecutionFactory scriptExecutionFactory;

  /** The validate function. */
  @Tested
  CustomFunctionValidateResidencePeriodNotOverlapping customFunction;

  Datastore datastore;

  Entity applicationEntity;

  Entity personEntity;

  public CustomFunctionValidateResidencePeriodNotOverlappingTest() {

    super();
  }

  /**
   * Before each test, init the tested class and other objects.
   */
  @Before
  public void before() throws NoSuchSchemaException {

    customFunction =
      new CustomFunctionValidateResidencePeriodNotOverlapping();
    ieg2Context = new IEG2Context();

  }

  public void setUpEntities() throws NoSuchSchemaException {

    // Set up entities
    datastore = DatastoreFactory.newInstance()
      .openDatastore("BDMOASGISApplicationSchema");

    applicationEntity =
      datastore.newEntity(BDMDatastoreConstants.APPLICATION);
    datastore.addRootEntity(applicationEntity);

    personEntity = datastore.newEntity(BDMDatastoreConstants.PERSON);
    personEntity.setAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT,
      "true");
    personEntity.setAttribute("localID", "" + 3942034);
    personEntity.setAttribute(BDMDatastoreConstants.RESIDENTIAL_COUNTRY,
      "CA");
    personEntity.setAttribute(BDMDatastoreConstants.MAILING_COUNTRY, "CA");
    applicationEntity.addChildEntity(personEntity);

    personEntity.update();
    applicationEntity.update();

  }

  /**
   * Test a valid period.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validPeriod()
    throws AppException, InformationalException, NoSuchSchemaException {

    setUpEntities();

    final Entity residencePeriodEntity =
      datastore.newEntity(BDMOASDatastoreConstants.RESIDENCE_HISTORY);
    residencePeriodEntity.setAttribute(
      BDMOASDatastoreConstants.RESIDENCE_HISTORY_START_ATTR, "20200101");
    residencePeriodEntity.setAttribute(
      BDMOASDatastoreConstants.RESIDENCE_HISTORY_END_ATTR, "20201212");
    personEntity.addChildEntity(residencePeriodEntity);
    personEntity.update();
    applicationEntity.update();

    new MockUp<BDMFunctor>() {

      @Mock
      Entity readRoot(final RulesParameters rulesParameters) {

        return applicationEntity;
      }
    };

    final Date fromDate = Date.getDate("20210101");
    final Date toDate = Date.getDate("20220101");
    final boolean primary = true;

    expectationsParameters(fromDate, toDate, primary);
    ieg2Context.setRootEntityID(applicationEntity.getUniqueID());
    final RulesParameters rulesParameters = ieg2Context;

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction
        .getAdaptorValue(rulesParameters);

    // Verify
    assertEquals(true, adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test a valid period.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalidPeriod()
    throws AppException, InformationalException, NoSuchSchemaException {

    setUpEntities();

    final Entity residencePeriodEntity =
      datastore.newEntity(BDMOASDatastoreConstants.RESIDENCE_HISTORY);
    residencePeriodEntity.setAttribute(
      BDMOASDatastoreConstants.RESIDENCE_HISTORY_START_ATTR, "20200101");
    residencePeriodEntity.setAttribute(
      BDMOASDatastoreConstants.RESIDENCE_HISTORY_END_ATTR, "20221212");
    personEntity.addChildEntity(residencePeriodEntity);
    personEntity.update();
    applicationEntity.update();

    new MockUp<BDMFunctor>() {

      @Mock
      Entity readRoot(final RulesParameters rulesParameters) {

        return applicationEntity;
      }
    };

    final Date fromDate = Date.getDate("20210101");
    final Date toDate = Date.getDate("20220101");
    final boolean primary = true;

    expectationsParameters(fromDate, toDate, primary);
    ieg2Context.setRootEntityID(applicationEntity.getUniqueID());
    final RulesParameters rulesParameters = ieg2Context;

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction
        .getAdaptorValue(rulesParameters);

    // Verify
    assertEquals(false, adaptorValue.getValue(ieg2Context));
  }

}
