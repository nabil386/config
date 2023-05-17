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
import curam.rules.functions.CustomFunctionValidateDatastoreNameValueChanged;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.type.Date;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/*
 * The Class tests {@link CustomFunctionValidateDatastoreNameValueChanged}
 * custom
 * function.
 */
@RunWith(JMockit.class)
public class CustomFunctionValidateDatastoreNameValueChangedTest
  extends CustomFunctionTestJunit4 {

  /** The Constant IS_VALID. */
  private static final boolean IS_VALID = true;

  /** A valid Name. */
  private static final String VALID_NAME = "DatastoreName";

  /** A valid Name updated. */
  private static final String VALID_NAME_UPDATED = "UpdatedScriptName";

  /** A valid nameDate. */
  private static final Date VALID_NAME_DATE = Date.getCurrentDate();

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
  CustomFunctionValidateDatastoreNameValueChanged validateDatastoreName;

  public CustomFunctionValidateDatastoreNameValueChangedTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() throws NoSuchSchemaException {

    validateDatastoreName =
      new CustomFunctionValidateDatastoreNameValueChanged();
    ieg2Context = new IEG2Context();
  }

  /**
   * All Names Same, Date empty, positive Result
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_AllNameSame_NoDate()
    throws AppException, InformationalException, NoSuchSchemaException {

    // Test Data
    final String scriptFirstName = VALID_NAME;
    final String scriptMiddleName = VALID_NAME;
    final String scriptLastName = VALID_NAME;
    final String scriptPrefferredName = VALID_NAME;
    final Date nameDate = null;
    final String resultMessage =
      "Datastore Names match Script Names, Date not required and not provided";

    // Expectations
    expectationsParameters(scriptFirstName, scriptMiddleName, scriptLastName,
      scriptPrefferredName, nameDate);
    new Expectations() {

      {
        personEntity.getTypedAttribute(BDMDatastoreConstants.FIRST_NAME);
        result = VALID_NAME;

        personEntity.getTypedAttribute(BDMDatastoreConstants.MIDDLE_NAME);
        result = VALID_NAME;

        personEntity.getTypedAttribute(BDMDatastoreConstants.LAST_NAME);
        result = VALID_NAME;

        personEntity.getTypedAttribute(BDMDatastoreConstants.PREFERRED_NAME);
        result = VALID_NAME;
      }
    };

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateDatastoreName
        .getAdaptorValue(ieg2Context);

    // Verifications,
    assertEquals(resultMessage, IS_VALID, adaptorValue.getValue(ieg2Context));
  }

  /**
   * All Names Same, Date filled, positive Result
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_AllNameSame_DateAdded()
    throws AppException, InformationalException, NoSuchSchemaException {

    // Test Data
    final String scriptFirstName = VALID_NAME;
    final String scriptMiddleName = VALID_NAME;
    final String scriptLastName = VALID_NAME;
    final String scriptPrefferredName = VALID_NAME;
    final Date nameDate = VALID_NAME_DATE;
    final String resultMessage =
      "Datastore Names match Script Names, Date not required but provided";

    // Expectations
    expectationsParameters(scriptFirstName, scriptMiddleName, scriptLastName,
      scriptPrefferredName, nameDate);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateDatastoreName
        .getAdaptorValue(ieg2Context);

    // Verifications,
    assertEquals(resultMessage, IS_VALID, adaptorValue.getValue(ieg2Context));
  }

  /**
   * All Names Changed, Date empty, negative Result
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalid_AllNameChanged_NoDate()
    throws AppException, InformationalException, NoSuchSchemaException {

    // Test Data
    final String scriptFirstName = VALID_NAME_UPDATED;
    final String scriptMiddleName = VALID_NAME_UPDATED;
    final String scriptLastName = VALID_NAME_UPDATED;
    final String scriptPrefferredName = VALID_NAME_UPDATED;
    final Date nameDate = null;
    final String resultMessage =
      "Datastore Names dont match Script Names, Date required but not provided";

    // Expectations
    expectationsParameters(scriptFirstName, scriptMiddleName, scriptLastName,
      scriptPrefferredName, nameDate);
    new Expectations() {

      {
        personEntity.getTypedAttribute(BDMDatastoreConstants.FIRST_NAME);
        result = VALID_NAME;
      }
    };

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateDatastoreName
        .getAdaptorValue(ieg2Context);

    // Verifications,
    assertEquals(resultMessage, !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * All Names Changed, Date filled, positive Result
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_AllNameChanged_DateAdded()
    throws AppException, InformationalException, NoSuchSchemaException {

    // Test Data
    final String scriptFirstName = VALID_NAME_UPDATED;
    final String scriptMiddleName = VALID_NAME_UPDATED;
    final String scriptLastName = VALID_NAME_UPDATED;
    final String scriptPrefferredName = VALID_NAME_UPDATED;
    final Date nameDate = VALID_NAME_DATE;
    final String resultMessage =
      "Datastore Names dont match Script Names, Date required and provided";

    // Expectations
    expectationsParameters(scriptFirstName, scriptMiddleName, scriptLastName,
      scriptPrefferredName, nameDate);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateDatastoreName
        .getAdaptorValue(ieg2Context);

    // Verifications,
    assertEquals(resultMessage, IS_VALID, adaptorValue.getValue(ieg2Context));
  }

  /**
   * First Name Changed, Date empty, negative Result
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalid_FirstNameChanged_NoDate()
    throws AppException, InformationalException, NoSuchSchemaException {

    // Test Data
    final String scriptFirstName = VALID_NAME_UPDATED;
    final String scriptMiddleName = VALID_NAME;
    final String scriptLastName = VALID_NAME;
    final String scriptPrefferredName = VALID_NAME;
    final Date nameDate = null;
    final String resultMessage =
      "First Names dont match Script Names, Date required but not provided";

    // Expectations
    expectationsParameters(scriptFirstName, scriptMiddleName, scriptLastName,
      scriptPrefferredName, nameDate);
    new Expectations() {

      {
        personEntity.getTypedAttribute(BDMDatastoreConstants.FIRST_NAME);
        result = VALID_NAME;
      }
    };

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateDatastoreName
        .getAdaptorValue(ieg2Context);

    // Verifications,
    assertEquals(resultMessage, !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * First Name Changed, Date filled, positive Result
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalid_FirstNameChanged_DateAdded()
    throws AppException, InformationalException, NoSuchSchemaException {

    // Test Data
    final String scriptFirstName = VALID_NAME_UPDATED;
    final String scriptMiddleName = VALID_NAME;
    final String scriptLastName = VALID_NAME;
    final String scriptPrefferredName = VALID_NAME;
    final Date nameDate = VALID_NAME_DATE;
    final String resultMessage =
      "First Names dont match Script Names, Date required and provided";

    // Expectations
    expectationsParameters(scriptFirstName, scriptMiddleName, scriptLastName,
      scriptPrefferredName, nameDate);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateDatastoreName
        .getAdaptorValue(ieg2Context);

    // Verifications,
    assertEquals(resultMessage, IS_VALID, adaptorValue.getValue(ieg2Context));
  }

  /**
   * Middle name Changed, Date empty, negative Result
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalid_MiddleNameChanged_NoDate()
    throws AppException, InformationalException, NoSuchSchemaException {

    // Test Data
    final String scriptFirstName = VALID_NAME;
    final String scriptMiddleName = VALID_NAME_UPDATED;
    final String scriptLastName = VALID_NAME;
    final String scriptPrefferredName = VALID_NAME;
    final Date nameDate = null;
    final String resultMessage =
      "Datastore Names dont match Script Names, Date required but not provided";

    // Expectations
    expectationsParameters(scriptFirstName, scriptMiddleName, scriptLastName,
      scriptPrefferredName, nameDate);
    new Expectations() {

      {
        personEntity.getTypedAttribute(BDMDatastoreConstants.FIRST_NAME);
        result = VALID_NAME;

        personEntity.getTypedAttribute(BDMDatastoreConstants.MIDDLE_NAME);
        result = VALID_NAME;
      }
    };

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateDatastoreName
        .getAdaptorValue(ieg2Context);

    // Verifications,
    assertEquals(resultMessage, !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Middle Name Changed, Date filled, positive Result
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_MiddleNameChanged_DateAdded()
    throws AppException, InformationalException, NoSuchSchemaException {

    // Test Data
    final String scriptFirstName = VALID_NAME;
    final String scriptMiddleName = VALID_NAME_UPDATED;
    final String scriptLastName = VALID_NAME;
    final String scriptPrefferredName = VALID_NAME;
    final Date nameDate = VALID_NAME_DATE;
    final String resultMessage =
      "Middle Names dont match Script Names, Date required and provided";

    // Expectations
    expectationsParameters(scriptFirstName, scriptMiddleName, scriptLastName,
      scriptPrefferredName, nameDate);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateDatastoreName
        .getAdaptorValue(ieg2Context);

    // Verifications,
    assertEquals(resultMessage, IS_VALID, adaptorValue.getValue(ieg2Context));
  }

  /**
   * Last Names Changed, Date empty, negative Result
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalid_LastNameChanged_NoDate()
    throws AppException, InformationalException, NoSuchSchemaException {

    // Test Data
    final String scriptFirstName = VALID_NAME;
    final String scriptMiddleName = VALID_NAME;
    final String scriptLastName = VALID_NAME_UPDATED;
    final String scriptPrefferredName = VALID_NAME;
    final Date nameDate = null;
    final String resultMessage =
      "Last Names dont match Script Names, Date required but not provided";

    // Expectations
    expectationsParameters(scriptFirstName, scriptMiddleName, scriptLastName,
      scriptPrefferredName, nameDate);
    new Expectations() {

      {
        personEntity.getTypedAttribute(BDMDatastoreConstants.FIRST_NAME);
        result = VALID_NAME;

        personEntity.getTypedAttribute(BDMDatastoreConstants.MIDDLE_NAME);
        result = VALID_NAME;

        personEntity.getTypedAttribute(BDMDatastoreConstants.LAST_NAME);
        result = VALID_NAME;
      }
    };

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateDatastoreName
        .getAdaptorValue(ieg2Context);

    // Verifications,
    assertEquals(resultMessage, !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Last Names Changed, Date filled, positive Result
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_LastNameChanged_DateAdded()
    throws AppException, InformationalException, NoSuchSchemaException {

    // Test Data
    final String scriptFirstName = VALID_NAME;
    final String scriptMiddleName = VALID_NAME;
    final String scriptLastName = VALID_NAME_UPDATED;
    final String scriptPrefferredName = VALID_NAME;
    final Date nameDate = VALID_NAME_DATE;
    final String resultMessage =
      "Last Names dont match Script Names, Date required and provided";

    // Expectations
    expectationsParameters(scriptFirstName, scriptMiddleName, scriptLastName,
      scriptPrefferredName, nameDate);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateDatastoreName
        .getAdaptorValue(ieg2Context);

    // Verifications,
    assertEquals(resultMessage, IS_VALID, adaptorValue.getValue(ieg2Context));
  }

  /**
   * Preferred Names Changed, Date empty, negative Result
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalid_PreferredNameChanged_NoDate()
    throws AppException, InformationalException, NoSuchSchemaException {

    // Test Data
    final String scriptFirstName = VALID_NAME;
    final String scriptMiddleName = VALID_NAME;
    final String scriptLastName = VALID_NAME;
    final String scriptPrefferredName = VALID_NAME_UPDATED;
    final Date nameDate = null;
    final String resultMessage =
      "Preferred Names dont match Script Names, Date required but not provided";

    // Expectations
    expectationsParameters(scriptFirstName, scriptMiddleName, scriptLastName,
      scriptPrefferredName, nameDate);
    new Expectations() {

      {
        personEntity.getTypedAttribute(BDMDatastoreConstants.FIRST_NAME);
        result = VALID_NAME;

        personEntity.getTypedAttribute(BDMDatastoreConstants.MIDDLE_NAME);
        result = VALID_NAME;

        personEntity.getTypedAttribute(BDMDatastoreConstants.LAST_NAME);
        result = VALID_NAME;

        personEntity.getTypedAttribute(BDMDatastoreConstants.PREFERRED_NAME);
        result = VALID_NAME;
      }
    };

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateDatastoreName
        .getAdaptorValue(ieg2Context);

    // Verifications,
    assertEquals(resultMessage, !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Preferred Names Changed, Date filled, positive Result
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_PreferredNameChanged_DateAdded()
    throws AppException, InformationalException, NoSuchSchemaException {

    // Test Data
    final String scriptFirstName = VALID_NAME;
    final String scriptMiddleName = VALID_NAME;
    final String scriptLastName = VALID_NAME;
    final String scriptPrefferredName = VALID_NAME_UPDATED;
    final Date nameDate = VALID_NAME_DATE;
    final String resultMessage =
      "Preferred Names dont match Script Names, Date required and provided";

    // Expectations
    expectationsParameters(scriptFirstName, scriptMiddleName, scriptLastName,
      scriptPrefferredName, nameDate);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateDatastoreName
        .getAdaptorValue(ieg2Context);

    // Verifications,
    assertEquals(resultMessage, IS_VALID, adaptorValue.getValue(ieg2Context));
  }

}
