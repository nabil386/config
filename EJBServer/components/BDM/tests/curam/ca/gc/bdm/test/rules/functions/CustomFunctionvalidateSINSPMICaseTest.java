package curam.ca.gc.bdm.test.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.ieg.impl.IEG2Context;
import curam.ieg.impl.IEGScriptExecution;
import curam.ieg.impl.IEGScriptExecutionFactory;
import curam.rules.functions.CustomFunctionvalidateSINSPMICase;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.ItemGroup;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.Adaptor.BooleanAdaptor;
import curam.util.type.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.tools.configuration.base.MethodRef;
import static org.junit.Assert.assertEquals;

@RunWith(JMockit.class)
public class CustomFunctionvalidateSINSPMICaseTest
  extends CustomFunctionTestJunit4 {

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The root entity. */
  @Mocked
  Entity rootEntity;

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

  private CustomFunctionvalidateSINSPMICase createTestSubject() {

    return new CustomFunctionvalidateSINSPMICase();
  }

  private void expectationSINAttribute(final boolean continueSin)
    throws AppException, InformationalException, NoSuchSchemaException {

    new Expectations() {

      {
        scriptExecutionFactory.getScriptExecutionObject(anyLong);
        result = scriptExecution;

        datastoreFactory.openDatastore(scriptExecution.getSchemaName());
        result = datastore;

        rootEntity.getTypedAttribute("continueSIN");

        result = continueSin;

      }
    };
  }

  @MethodRef(name = "getAdaptorValue",
    signature = "(QRulesParameters;)QAdaptor;")
  @Test
  public void testGetAdaptorValue_False() throws Exception {

    CustomFunctionvalidateSINSPMICase testSubject;

    final String sin = "865000541";
    final String firstName = "Paul";
    final String lastName = "Clarkson";
    final Date dob = Date.getCurrentDate();
    expectationsParameters(sin, firstName, lastName, dob);

    final IEG2Context ieg2Context = new IEG2Context();
    final String idOrLinkEntity = "123";
    final String entity = BDMDatastoreConstants.COMMUNICATION_DETAILS;
    final Map<Long, String> map = new HashMap<>();
    map.put((long) 123, "true");
    final List<Map<Long, String>> list = new ArrayList<>();
    list.add(map);

    ieg2Context.addListQuestion(idOrLinkEntity, entity, list);
    final ItemGroup itemGroup = new ItemGroup("SIN", "SIN");
    // itemGroup.insert("865000541");
    ieg2Context.addRDO(itemGroup, true);
    expectationSINAttribute(true);
    final RulesParameters rulesParameters = ieg2Context;
    Adaptor result;

    // default test
    testSubject = createTestSubject();
    result = testSubject.getAdaptorValue(rulesParameters);

    final Adaptor.BooleanAdaptor booleanAdaptor = (BooleanAdaptor) result;

    assertEquals(booleanAdaptor.getValue(ieg2Context), false);
  }

  @MethodRef(name = "getAdaptorValue",
    signature = "(QRulesParameters;)QAdaptor;")
  @Test
  public void testGetAdaptorValue_True() throws Exception {

    CustomFunctionvalidateSINSPMICase testSubject;

    final String sin = "86500";
    final String firstName = "Paul";
    final String lastName = "Clarkson";
    final Date dob = Date.getCurrentDate();
    expectationsParameters(sin, firstName, lastName, dob);

    final IEG2Context ieg2Context = new IEG2Context();
    final String idOrLinkEntity = "123";
    final String entity = BDMDatastoreConstants.COMMUNICATION_DETAILS;
    final Map<Long, String> map = new HashMap<>();
    map.put((long) 123, "true");
    final List<Map<Long, String>> list = new ArrayList<>();
    list.add(map);

    ieg2Context.addListQuestion(idOrLinkEntity, entity, list);
    final ItemGroup itemGroup = new ItemGroup("SIN", "SIN");
    // itemGroup.insert("865000541");
    ieg2Context.addRDO(itemGroup, true);
    expectationSINAttribute(false);

    final RulesParameters rulesParameters = ieg2Context;
    Adaptor result;

    // default test
    testSubject = createTestSubject();
    result = testSubject.getAdaptorValue(rulesParameters);

    final Adaptor.BooleanAdaptor booleanAdaptor = (BooleanAdaptor) result;

    assertEquals(booleanAdaptor.getValue(ieg2Context), true);
  }
}
