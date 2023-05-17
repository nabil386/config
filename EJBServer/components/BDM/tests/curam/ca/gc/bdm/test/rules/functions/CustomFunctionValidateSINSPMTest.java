package curam.ca.gc.bdm.test.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.core.impl.EnvVars;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.ieg.impl.IEG2Context;
import curam.ieg.impl.IEGScriptExecution;
import curam.ieg.impl.IEGScriptExecutionFactory;
import curam.rules.functions.CustomFunctionValidateSINSPM;
import curam.rules.functions.SINValidator;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.Adaptor.BooleanAdaptor;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.tools.configuration.base.MethodRef;
import static org.junit.Assert.assertEquals;

@RunWith(JMockit.class)
public class CustomFunctionValidateSINSPMTest
  extends CustomFunctionTestJunit4 {

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The root entity. */
  @Mocked
  Entity rootEntity;

  @Mocked
  Entity personEntity;

  @Mocked
  Entity intakeApplicationEntity;

  /** The datastore. */
  @Mocked
  Datastore datastore;

  /** The datastore factory. */
  @Mocked
  DatastoreFactory datastoreFactory;

  /** The script execution. */
  @Mocked
  IEGScriptExecution scriptExecution;

  /** The script execution factory. */
  @Mocked
  IEGScriptExecutionFactory scriptExecutionFactory;

  curam.util.type.Date dateOfBirth;

  private CustomFunctionValidateSINSPM createTestSubject() {

    return new CustomFunctionValidateSINSPM();
  }

  @Before
  public void setUp() throws AppException, InformationalException {

    final java.util.Calendar cal = new java.util.GregorianCalendar(1970,
      java.util.Calendar.APRIL, 1, 0, 0, 0);
    dateOfBirth = new curam.util.type.Date(cal);

    try {
      datastore = DatastoreFactory.newInstance()
        .openDatastore(BDMDatastoreConstants.BDM_STIMULUS_SCHEMA);
    } catch (final NoSuchSchemaException e) {
      e.printStackTrace();
      return;
    }

    rootEntity = datastore.newEntity("Application");
    rootEntity.setAttribute("userName", "admin");

    datastore.addRootEntity(rootEntity);

    personEntity = datastore.newEntity("Person");
    personEntity.setAttribute(BDMDatastoreConstants.SOCIAL_INSURANCE_NUMBER,
      "712723857");
    personEntity.setAttribute(BDMDatastoreConstants.LAST_NAME_AT_BIRTH,
      "Test");
    personEntity.setAttribute(BDMDatastoreConstants.PARENT_LAST_NAME, "Test");
    personEntity.setAttribute(BDMDatastoreConstants.FIRST_NAME, "Test");
    personEntity.setAttribute(BDMDatastoreConstants.GENDER, "SX1");
    personEntity.setAttribute(BDMDatastoreConstants.LAST_NAME, "Test");
    personEntity.setAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT,
      "true");
    personEntity.setAttribute(
      BDMDatastoreConstants.IS_MAILING_ADDRESS_DIFFERENT, "false");
    personEntity.setAttribute(BDMDatastoreConstants.DATE_OF_BIRTH,
      "19700101");

    rootEntity.addChildEntity(personEntity);
    intakeApplicationEntity =
      datastore.newEntity(BDMDatastoreConstants.INTAKE_APPLICATION_TYPE);
    intakeApplicationEntity.setAttribute(
      BDMDatastoreConstants.INTAKE_APPLICATION_TYPE_ID, "80001");
    rootEntity.addChildEntity(intakeApplicationEntity);
    rootEntity.update();
  }

  @MethodRef(name = "getAdaptorValue",
    signature = "(QRulesParameters;)QAdaptor;")
  @Test
  public void testGetAdaptorValue_TrueNoConfigurationProperties()
    throws Exception {

    CustomFunctionValidateSINSPM testSubject;
    final boolean validSIN = true;
    final String sin = "865000541";
    expectationsParameters(validSIN, sin);
    final RulesParameters rulesParameters = ieg2Context;
    Adaptor result;

    // default test
    testSubject = createTestSubject();
    result = testSubject.getAdaptorValue(rulesParameters);
    final Adaptor.BooleanAdaptor booleanAdaptor = (BooleanAdaptor) result;

    assertEquals(booleanAdaptor.getValue(ieg2Context), true);
  }

  @MethodRef(name = "getAdaptorValue",
    signature = "(QRulesParameters;)QAdaptor;")
  @Test
  public void testGetAdaptorValue_True() throws Exception {

    CustomFunctionValidateSINSPM testSubject;
    final boolean validSIN = true;
    final String sin = "865000541";

    expectationsParameters(sin, validSIN);

    final String enVarKey = EnvVars.BDM_ENV_BDM_SIN_SPM_VALIDATION;
    final Boolean enVarValue = true;

    expectationsEnvVars(enVarKey, enVarValue);

    final IEG2Context ieg2Context = new IEG2Context();

    ieg2Context.setRootEntityID(rootEntity.getUniqueID());

    // expectationSINAttribute(true);
    // expectationsEntity();
    ieg2Context.setCurrentEntityID(personEntity.getUniqueID());
    ieg2Context.setExecutionID(123456789l);
    final Entity[] entities = {intakeApplicationEntity, personEntity };
    new Expectations() {

      {
        rootEntity.getTypedAttribute("continueSIN");
        result = true;

        rootEntity.getChildEntities(datastore
          .getEntityType(BDMDatastoreConstants.INTAKE_APPLICATION_TYPE));
        result = entities;
        intakeApplicationEntity
          .getAttribute(BDMDatastoreConstants.INTAKE_APPLICATION_TYPE_ID);
        result = 80001;
      }
    };

    final RulesParameters rulesParameters = ieg2Context;

    Adaptor result;

    // default test
    testSubject = createTestSubject();
    result = testSubject.getAdaptorValue(rulesParameters);
    final Adaptor.BooleanAdaptor booleanAdaptor = (BooleanAdaptor) result;

    assertEquals(booleanAdaptor.getValue(ieg2Context), true);
  }

  @MethodRef(name = "getAdaptorValue",
    signature = "(QRulesParameters;)QAdaptor;")
  @Test
  public void testGetAdaptorValue_InvalidSIN() throws Exception {

    CustomFunctionValidateSINSPM testSubject;
    final boolean validSIN = true;
    final String sin = "86500";

    expectationsParameters(sin, validSIN);

    final String enVarKey = EnvVars.BDM_ENV_BDM_SIN_SPM_VALIDATION;
    final Boolean enVarValue = true;

    expectationsEnvVars(enVarKey, enVarValue);

    final IEG2Context ieg2Context = new IEG2Context();

    ieg2Context.setRootEntityID(rootEntity.getUniqueID());

    // expectationSINAttribute(true);
    // expectationsEntity();
    ieg2Context.setCurrentEntityID(personEntity.getUniqueID());
    ieg2Context.setExecutionID(123456789l);
    final Entity[] entities = {intakeApplicationEntity, personEntity };
    new Expectations() {

      {
        rootEntity.getTypedAttribute("continueSIN");
        result = false;
      }
    };

    final RulesParameters rulesParameters = ieg2Context;

    Adaptor result;

    // default test
    testSubject = createTestSubject();
    result = testSubject.getAdaptorValue(rulesParameters);
    final Adaptor.BooleanAdaptor booleanAdaptor = (BooleanAdaptor) result;

    assertEquals(booleanAdaptor.getValue(ieg2Context), true);
  }

  @MethodRef(name = "getAdaptorValue",
    signature = "(QRulesParameters;)QAdaptor;")
  @Test
  public void testGetAdaptorValue_False() throws Exception {

    new MockUp<SINValidator>() {

      @Mock
      public boolean isSINRegisteredIntakeApplication(final String sin,
        final long intakeApplicationTypeId) {

        return true;
      }
    };

    CustomFunctionValidateSINSPM testSubject;
    final boolean validSIN = true;
    final String sin = "86500";

    expectationsParameters(sin, validSIN);

    final String enVarKey = EnvVars.BDM_ENV_BDM_SIN_SPM_VALIDATION;
    final Boolean enVarValue = true;

    expectationsEnvVars(enVarKey, enVarValue);

    final IEG2Context ieg2Context = new IEG2Context();

    ieg2Context.setRootEntityID(rootEntity.getUniqueID());
    ieg2Context.setCurrentEntityID(personEntity.getUniqueID());
    ieg2Context.setExecutionID(123456789l);

    final Entity[] entities = {intakeApplicationEntity, personEntity };

    new Expectations() {

      {
        rootEntity.getTypedAttribute("continueSIN");
        result = true;

        rootEntity.getChildEntities(datastore
          .getEntityType(BDMDatastoreConstants.INTAKE_APPLICATION_TYPE));
        result = entities;
        intakeApplicationEntity
          .getAttribute(BDMDatastoreConstants.INTAKE_APPLICATION_TYPE_ID);
        result = 80001;

      }
    };

    final RulesParameters rulesParameters = ieg2Context;

    Adaptor result;

    // default test
    testSubject = createTestSubject();
    result = testSubject.getAdaptorValue(rulesParameters);
    final Adaptor.BooleanAdaptor booleanAdaptor = (BooleanAdaptor) result;

    assertEquals(booleanAdaptor.getValue(ieg2Context), false);
  }
}
