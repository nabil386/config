package curam.ca.gc.bdm.test.rules.functions;

import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionConstants;
import curam.rules.functions.CustomFunctionValidateNoChildren;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.Adaptor.BooleanAdaptor;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.tools.configuration.base.MethodRef;
import static org.junit.Assert.assertEquals;

@RunWith(JMockit.class)
public class CustomFunctionValidateNoChildrenTest
  extends CustomFunctionTestJunit4 {

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  private CustomFunctionValidateNoChildren createTestSubject() {

    return new CustomFunctionValidateNoChildren();
  }

  @MethodRef(name = "getAdaptorValue",
    signature = "(QRulesParameters;)QAdaptor;")
  @Test
  public void testGetAdaptorValue_TrueSK() throws Exception {

    CustomFunctionValidateNoChildren testSubject;
    final String province = CustomFunctionConstants.PROVINCE_SK;
    final Integer noOfChildern = 12;
    expectationsParameters(province, noOfChildern);
    final RulesParameters rulesParameters = null;

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
  public void testGetAdaptorValue_TrueNotSK() throws Exception {

    CustomFunctionValidateNoChildren testSubject;
    final String province = "Alberta";
    final Integer noOfChildern = 12;
    expectationsParameters(province, noOfChildern);
    final RulesParameters rulesParameters = null;

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

    CustomFunctionValidateNoChildren testSubject;
    final String province = CustomFunctionConstants.PROVINCE_SK;
    final Integer noOfChildern = 200;
    expectationsParameters(province, noOfChildern);
    final RulesParameters rulesParameters = null;

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
  public void testGetAdaptorValue_null() throws Exception {

    CustomFunctionValidateNoChildren testSubject;

    final RulesParameters rulesParameters = null;

    Adaptor result;

    // default test
    testSubject = createTestSubject();
    result = testSubject.getAdaptorValue(rulesParameters);

    final Adaptor.BooleanAdaptor booleanAdaptor = (BooleanAdaptor) result;

    assertEquals(booleanAdaptor.getValue(ieg2Context), true);
  }
}
