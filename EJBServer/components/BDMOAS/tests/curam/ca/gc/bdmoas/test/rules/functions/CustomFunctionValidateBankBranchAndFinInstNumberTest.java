package curam.ca.gc.bdmoas.test.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventDatastoreConstants;
import curam.ca.gc.bdm.codetable.BDMBANKACCOUNTCHOICE;
import curam.ca.gc.bdmoas.test.evidence.BDMEvidenceUtilsTest;
import curam.codetable.METHODOFDELIVERY;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.ieg.impl.IEG2Context;
import curam.ieg.impl.IEGScriptExecution;
import curam.ieg.impl.IEGScriptExecutionFactory;
import curam.pdc.struct.PDCPersonDetails;
import curam.rules.functions.CustomFunctionValidateBankBranchAndFinInstNumber;
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
 * The Class tests {@link CustomFunctionValidateBankBranchAndFinInstNumber}
 * custom
 * function.
 */

@RunWith(JMockit.class)
public class CustomFunctionValidateBankBranchAndFinInstNumberTest
  extends CustomFunctionTestJunit4 {

  private static final boolean IS_VALID = true;

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

  Datastore datastore;

  Entity applicationEntity;

  Entity personEntity;

  Entity paymentEntity;

  /** The testing functions. */
  BDMEvidenceUtilsTest bdmEvidenceUtils;

  /** The validate function. */
  @Tested
  CustomFunctionValidateBankBranchAndFinInstNumber customFunction;

  public CustomFunctionValidateBankBranchAndFinInstNumberTest() {

    super();
  }

  /**
   * Before each test, init the tested class and other objects.
   */
  @Before
  public void before() throws NoSuchSchemaException {

    customFunction = new CustomFunctionValidateBankBranchAndFinInstNumber();
    ieg2Context = new IEG2Context();
    bdmEvidenceUtils = new BDMEvidenceUtilsTest();
    datastore = DatastoreFactory.newInstance()
      .openDatastore("BDMOASGISApplicationSchema");

  }

  public void setUpEntities()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    // Set up entities

    applicationEntity =
      datastore.newEntity(BDMDatastoreConstants.APPLICATION);
    datastore.addRootEntity(applicationEntity);

    personEntity = datastore.newEntity(BDMDatastoreConstants.PERSON);
    personEntity.setAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT,
      "true");
    personEntity.setAttribute("localID", "" + pdcPersonDetails.concernRoleID);
    personEntity.setAttribute(BDMDatastoreConstants.PAYMENT_METHOD,
      METHODOFDELIVERY.EFT);
    applicationEntity.addChildEntity(personEntity);

    paymentEntity = datastore.newEntity(BDMDatastoreConstants.PAYMENT);
    paymentEntity.setAttribute(
      BDMLifeEventDatastoreConstants.PREFERRED_BANK_ACCOUNT,
      BDMBANKACCOUNTCHOICE.ADD_NEW);
    paymentEntity.setAttribute(BDMDatastoreConstants.BANK_ACCT_NUM,
      "1234567");
    paymentEntity.setAttribute(BDMDatastoreConstants.BANK_INSTITUTION_CODE,
      "200");
    paymentEntity.setAttribute(BDMDatastoreConstants.BANK_BRANCH_NUM,
      "04381");

    personEntity.addChildEntity(paymentEntity);

    paymentEntity.update();
    personEntity.update();
    applicationEntity.update();

  }

  /**
   * Test that function checks bank sort codes when length is correct.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_notValidNumbers()
    throws AppException, InformationalException, NoSuchSchemaException {

    setUpEntities();

    ieg2Context.setRootEntityID(applicationEntity.getUniqueID());

    final RulesParameters rulesParameters = ieg2Context;

    paymentEntity.setAttribute(BDMDatastoreConstants.BANK_ACCT_NUM,
      "1234567");
    paymentEntity.setAttribute(BDMDatastoreConstants.BANK_INSTITUTION_CODE,
      "123");
    paymentEntity.setAttribute(BDMDatastoreConstants.BANK_BRANCH_NUM,
      "12345");
    paymentEntity.update();
    personEntity.update();
    applicationEntity.update();

    // Expectations
    new Expectations(datastore) {

      {
        datastore.readEntity(anyLong);
        result = applicationEntity;

        curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil
          .retrieveChildEntity(personEntity, BDMDatastoreConstants.PAYMENT);
        result = paymentEntity;
      }

    };

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction
        .getAdaptorValue(rulesParameters);

    assertEquals("The bank codes should not be valid", !IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test that function checks bank sort codes when length is correct.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_notValidLength()
    throws AppException, InformationalException, NoSuchSchemaException {

    setUpEntities();

    ieg2Context.setRootEntityID(applicationEntity.getUniqueID());

    final RulesParameters rulesParameters = ieg2Context;

    paymentEntity.setAttribute(BDMDatastoreConstants.BANK_ACCT_NUM,
      "1234567");
    paymentEntity.setAttribute(BDMDatastoreConstants.BANK_INSTITUTION_CODE,
      "12");
    paymentEntity.setAttribute(BDMDatastoreConstants.BANK_BRANCH_NUM, "1234");
    paymentEntity.update();
    personEntity.update();
    applicationEntity.update();

    // Expectations
    new Expectations(datastore) {

      {
        datastore.readEntity(anyLong);
        result = applicationEntity;

        curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil
          .retrieveChildEntity(personEntity, BDMDatastoreConstants.PAYMENT);
        result = paymentEntity;
      }

    };

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction
        .getAdaptorValue(rulesParameters);

    assertEquals("The bank codes should not be valid", !IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }
}
