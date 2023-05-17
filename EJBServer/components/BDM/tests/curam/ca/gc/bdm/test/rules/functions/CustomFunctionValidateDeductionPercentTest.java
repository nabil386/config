package curam.ca.gc.bdm.test.rules.functions;

import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionConstants;
import curam.rules.functions.CustomFunctionValidateDeductionPercent;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.Adaptor.BooleanAdaptor;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.tools.configuration.base.MethodRef;
import static org.junit.Assert.assertEquals;

@RunWith(JMockit.class)
public class CustomFunctionValidateDeductionPercentTest
  extends CustomFunctionTestJunit4 {

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  private CustomFunctionValidateDeductionPercent createTestSubject() {

    return new CustomFunctionValidateDeductionPercent();
  }

  @MethodRef(name = "getAdaptorValue",
    signature = "(QRulesParameters;)QAdaptor;")
  @Test
  public void testGetAdaptorValue_True() throws Exception {

    CustomFunctionValidateDeductionPercent testSubject;
    final String deductionPArameter = "Percent";
    final Long percentParameter = (long) 20;
    expectationsParameters(deductionPArameter, percentParameter);
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
  public void testGetAdaptorValue_Null() throws Exception {

    CustomFunctionValidateDeductionPercent testSubject;
    final String deductionPArameter = null;
    final Long percentParameter = null;
    expectationsParameters(deductionPArameter, percentParameter);
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
  public void testGetAdaptorValue_InvalidPercent() throws Exception {

    CustomFunctionValidateDeductionPercent testSubject;
    final String deductionPArameter = "Percent";
    final Long percentParameter = (long) 123;
    expectationsParameters(deductionPArameter, percentParameter);
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
  public void testGetAdaptorValue_DollarAmount() throws Exception {

    CustomFunctionValidateDeductionPercent testSubject;
    final String deductionPArameter =
      CustomFunctionConstants.DEDUCTION_TYPE_METHOD_AMT;
    final Long percentParameter = (long) 123;
    expectationsParameters(deductionPArameter, percentParameter);
    final RulesParameters rulesParameters = null;
    Adaptor result;

    // default test
    testSubject = createTestSubject();
    result = testSubject.getAdaptorValue(rulesParameters);

    final Adaptor.BooleanAdaptor booleanAdaptor = (BooleanAdaptor) result;

    assertEquals(booleanAdaptor.getValue(ieg2Context), true);
  }

}
