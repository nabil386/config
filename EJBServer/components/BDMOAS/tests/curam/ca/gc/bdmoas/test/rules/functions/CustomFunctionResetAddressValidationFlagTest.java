package curam.ca.gc.bdmoas.test.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdmoas.test.evidence.BDMEvidenceUtilsTest;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.ieg.impl.IEG2Context;
import curam.ieg.impl.IEGScriptExecution;
import curam.ieg.impl.IEGScriptExecutionFactory;
import curam.pdc.struct.PDCPersonDetails;
import curam.rules.functions.BDMFunctor;
import curam.rules.functions.CustomFunctionResetAddressValidationFlag;
import curam.rules.functions.IEG2ExecutionContext;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.transaction.TransactionInfo;
import mockit.Expectations;
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
 * The Class tests {@link CustomFunctionResetAddressValidationFlag} custom
 * function.
 */

@RunWith(JMockit.class)
public class CustomFunctionResetAddressValidationFlagTest
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

  /** The entity functions. */
  @Mocked
  BDMApplicationEventsUtil BDMApplicationEventsUtil;

  /** The testing functions. */
  BDMEvidenceUtilsTest bdmEvidenceUtils;

  /** The validate function. */
  @Tested
  CustomFunctionResetAddressValidationFlag customFunction;

  public CustomFunctionResetAddressValidationFlagTest() {

    super();
  }

  /**
   * Before each test, init the tested class and other objects.
   */
  @Before
  public void before() throws NoSuchSchemaException {

    customFunction = new CustomFunctionResetAddressValidationFlag();
    ieg2Context = new IEG2Context();
    bdmEvidenceUtils = new BDMEvidenceUtilsTest();

  }

  /**
   * Test that the appropriate flags are being reset.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    // Set up entities
    final Datastore datastore = DatastoreFactory.newInstance()
      .openDatastore("BDMOASGISApplicationSchema");

    final Entity applicationEntity =
      datastore.newEntity(BDMDatastoreConstants.APPLICATION);
    datastore.addRootEntity(applicationEntity);

    final Entity primaryPersonEntity =
      datastore.newEntity(BDMDatastoreConstants.PERSON);
    primaryPersonEntity
      .setAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT, "true");
    primaryPersonEntity.setAttribute("localID",
      "" + pdcPersonDetails.concernRoleID);
    applicationEntity.addChildEntity(primaryPersonEntity);

    primaryPersonEntity.update();
    applicationEntity.update();

    ieg2Context.setRootEntityID(applicationEntity.getUniqueID());

    final RulesParameters rulesParameters = ieg2Context;

    // Mocked abstract function
    new MockUp<BDMFunctor>() {

      @Mock
      protected Entity readRoot(final RulesParameters rulesParameters) {

        return applicationEntity;
      }
    };

    // Expectations
    new Expectations() {

      {
        curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil
          .retrieveChildEntity(applicationEntity,
            BDMDatastoreConstants.PERSON);
        result = primaryPersonEntity;

      }

    };

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction
        .getAdaptorValue(rulesParameters);

    // Check if function executed fully
    assertEquals("A successful function invocation should return true.",
      Boolean.TRUE, adaptorValue.getValue(ieg2Context));

    final Entity[] primaryPersons = applicationEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.PERSON),
      "isPrimaryParticipant==true");

    // Check if flags have been reset to -1
    final String WSResFlag = (String) primaryPersons[0]
      .getTypedAttribute(BDMDatastoreConstants.WS_RES_CONTINUE);

    assertEquals("WS_RES_CONTINUE flag should be reset to -1.", WSResFlag,
      "-1");

    final String WSMailFlag = (String) primaryPersons[0]
      .getTypedAttribute(BDMDatastoreConstants.WS_MAIL_CONTINUE);

    assertEquals("WS_MAIL_CONTINUE flag should be reset to -1.", WSMailFlag,
      "-1");

  }
}
