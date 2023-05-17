package curam.ca.gc.bdm.test.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionvalidateAlerts;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.Adaptor.BooleanAdaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.rules.functor.CustomFunctor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.tools.configuration.base.MethodRef;
import static org.junit.Assert.assertEquals;

@RunWith(JMockit.class)
public class CustomFunctionvalidateAlertsTest extends CuramServerTestJUnit4 {

  private CustomFunctionvalidateAlerts createTestSubject() {

    return new CustomFunctionvalidateAlerts();
  }

  private IEG2Context setCheckCommunicaionPrefTrue()
    throws AppException, InformationalException {

    final IEG2Context ieg2Context = new IEG2Context();
    final String idOrLinkEntity = "123";
    final String entity = BDMDatastoreConstants.COMMUNICATION_DETAILS;
    final Map<Long, String> map = new HashMap<>();
    map.put((long) 123, "true");
    final List<Map<Long, String>> list = new ArrayList<>();
    list.add(map);

    ieg2Context.addListQuestion(idOrLinkEntity, entity, list);
    return ieg2Context;
  }

  private IEG2Context setCheckCommunicaionPrefFalse()
    throws AppException, InformationalException {

    final IEG2Context ieg2Context = new IEG2Context();
    final String idOrLinkEntity = "123";
    final String entity = BDMDatastoreConstants.COMMUNICATION_DETAILS;
    final Map<Long, String> map = new HashMap<>();
    map.put((long) 123, "false");
    final List<Map<Long, String>> list = new ArrayList<>();
    list.add(map);

    ieg2Context.addListQuestion(idOrLinkEntity, entity, list);
    return ieg2Context;
  }

  private IEG2Context setUpGetAdaptorValueYN1False() {

    final IEG2Context ieg2Context = new IEG2Context();
    final String idOrLinkEntity = BDMDatastoreConstants.ALT_PREF_EMAIL;
    final String entity = BDMDatastoreConstants.COMMUNICATION_DETAILS;
    final Map<Long, String> map = new HashMap<>();
    map.put((long) 123, "false");

    final List<Map<Long, String>> list = new ArrayList<>();
    list.add(map);

    final String idOrLinkEntityPhone = BDMDatastoreConstants.PHONE_ALT_PREF;
    final String entityPhone = BDMDatastoreConstants.COMMUNICATION_DETAILS;
    final Map<Long, String> mapPhone = new HashMap<>();
    mapPhone.put((long) 123, "false");

    final List<Map<Long, String>> listPhone = new ArrayList<>();
    listPhone.add(map);

    ieg2Context.addListQuestion(idOrLinkEntity, entity, list);
    ieg2Context.addListQuestion(idOrLinkEntityPhone, entityPhone, listPhone);

    new MockUp<CustomFunctor>() {

      @Mock
      public List<Adaptor> getParameters()
        throws AppException, InformationalException {

        final List<Adaptor> list = new ArrayList<>();

        final Adaptor adaptor = AdaptorFactory.getStringAdaptor("YN1");

        list.add(adaptor);

        return list;
      }
    };
    new MockUp<Adaptor.StringAdaptor>() {

      @Mock
      public String getStringValue(final RulesParameters rulesParameter) {

        return "YN1";
      }
    };
    return ieg2Context;
  }

  private IEG2Context setUpGetAdaptorValueTrueNull() {

    final IEG2Context ieg2Context = new IEG2Context();
    new MockUp<CustomFunctor>() {

      @Mock
      public List<Adaptor> getParameters()
        throws AppException, InformationalException {

        final List<Adaptor> list = new ArrayList<>();
        ;
        final Adaptor adaptor = AdaptorFactory.getStringAdaptor(null);

        list.add(adaptor);

        return list;
      }
    };
    new MockUp<Adaptor.StringAdaptor>() {

      @Mock
      public String getStringValue(final RulesParameters rulesParameter) {

        return null;
      }
    };
    return ieg2Context;
  }

  private IEG2Context setUpGetAdaptorValueYN1True() {

    final IEG2Context ieg2Context = new IEG2Context();
    final String idOrLinkEntity = BDMDatastoreConstants.ALT_PREF_EMAIL;
    final String entity = BDMDatastoreConstants.COMMUNICATION_DETAILS;
    final Map<Long, String> map = new HashMap<>();
    map.put((long) 123, "true");
    final List<Map<Long, String>> list = new ArrayList<>();
    list.add(map);

    ieg2Context.addListQuestion(idOrLinkEntity, entity, list);

    new MockUp<CustomFunctor>() {

      @Mock
      public List<Adaptor> getParameters()
        throws AppException, InformationalException {

        final List<Adaptor> list = new ArrayList<>();
        ;
        final Adaptor adaptor = AdaptorFactory.getStringAdaptor("YN1");

        list.add(adaptor);

        return list;
      }
    };
    new MockUp<Adaptor.StringAdaptor>() {

      @Mock
      public String getStringValue(final RulesParameters rulesParameter) {

        return "YN1";
      }
    };
    return ieg2Context;
  }

  @MethodRef(name = "checkCommunicationPref",
    signature = "(QString;QIEG2Context;)Z")
  @Test
  public void testCheckCommunicationPref() throws Exception {

    CustomFunctionvalidateAlerts testSubject;
    final String prefType = "123";
    final IEG2Context ieg2ContextTrue = setCheckCommunicaionPrefTrue();
    final IEG2Context ieg2ContextFalse = setCheckCommunicaionPrefFalse();
    boolean resultTrue;
    boolean resultFalse;

    // default test
    testSubject = createTestSubject();
    resultTrue =
      testSubject.checkCommunicationPref(prefType, ieg2ContextTrue);
    resultFalse =
      testSubject.checkCommunicationPref(prefType, ieg2ContextFalse);
    assertEquals(resultTrue, true);
    assertEquals(resultFalse, false);
  }

  @MethodRef(name = "getAdaptorValue",
    signature = "(QRulesParameters;)QAdaptor;")
  @Test
  public void testGetAdaptorValueNull() throws Exception {

    CustomFunctionvalidateAlerts testSubject;

    final IEG2Context ieg2ContextNull = setUpGetAdaptorValueTrueNull();

    final RulesParameters rulesParametersNull = ieg2ContextNull;

    final Adaptor resultTrue;

    // default test
    testSubject = createTestSubject();

    resultTrue = testSubject.getAdaptorValue(rulesParametersNull);

    final Adaptor.BooleanAdaptor booleanAdaptorTrue =
      (BooleanAdaptor) resultTrue;

    assertEquals(booleanAdaptorTrue.getValue(rulesParametersNull), true);
  }
}
