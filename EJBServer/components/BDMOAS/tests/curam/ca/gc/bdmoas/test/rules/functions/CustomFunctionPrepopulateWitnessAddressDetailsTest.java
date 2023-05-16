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
import curam.rules.functions.CustomFunctionPrepopulateWitnessAddressDetails;
import curam.rules.functions.IEG2ExecutionContext;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertTrue;

/**
 * The Class tests {@link CustomFunctionPrepopulateWitnessAddressDetails} custom
 * function.
 */

@RunWith(JMockit.class)
public class CustomFunctionPrepopulateWitnessAddressDetailsTest
  extends CustomFunctionTestJunit4 {

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The rulesParameters. */
  @Mocked
  IEG2ExecutionContext ieg2ExecutionContext;

  /** The script execution. */
  @Mocked
  IEGScriptExecution scriptExecution;

  /** The script execution factory. */
  @Mocked
  IEGScriptExecutionFactory scriptExecutionFactory;

  /** The validate function. */
  @Tested
  CustomFunctionPrepopulateWitnessAddressDetails customFunction;

  public CustomFunctionPrepopulateWitnessAddressDetailsTest() {

    super();
  }

  /**
   * Before each test, init the tested class and other objects.
   */
  @Before
  public void before() throws NoSuchSchemaException {

    customFunction = new CustomFunctionPrepopulateWitnessAddressDetails();
    ieg2Context = new IEG2Context();

  }

  /**
   * Test that the appropriate fields are set.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_mailing()
    throws AppException, InformationalException, NoSuchSchemaException {

    // Set up entities
    final Datastore datastore = DatastoreFactory.newInstance()
      .openDatastore("BDMOASGISApplicationSchema");

    final Entity applicationEntity =
      datastore.newEntity(BDMDatastoreConstants.APPLICATION);
    datastore.addRootEntity(applicationEntity);

    final Entity witnessEntity =
      datastore.newEntity(BDMOASDatastoreConstants.DECLARATION_ENTITY);
    witnessEntity.setAttribute(BDMDatastoreConstants.MAILING_COUNTRY, "CA");
    applicationEntity.addChildEntity(witnessEntity);

    witnessEntity.update();
    applicationEntity.update();

    new Expectations(datastore) {

      {
        datastore.readEntity(anyLong);
        result = applicationEntity;

      }
    };

    ieg2Context.setRootEntityID(applicationEntity.getUniqueID());
    final RulesParameters rulesParameters = ieg2Context;

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction
        .getAdaptorValue(rulesParameters);

    // Verify
    final Entity verifyWitnessEntity =
      applicationEntity.getChildEntities(datastore
        .getEntityType(BDMOASDatastoreConstants.DECLARATION_ENTITY))[0];

    final Entity[] mailingAddress = verifyWitnessEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.MAILING_ADDRESS));

    assertTrue("The mailing address entity should be added.",
      mailingAddress.length > 0);

  }
}
