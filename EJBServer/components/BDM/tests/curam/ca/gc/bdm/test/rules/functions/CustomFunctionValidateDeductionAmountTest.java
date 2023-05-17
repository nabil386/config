package curam.ca.gc.bdm.test.rules.functions;

import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionConstants;
import curam.rules.functions.CustomFunctionValidateDeductionAmount;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.Adaptor.BooleanAdaptor;
import curam.util.type.Money;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.tools.configuration.base.MethodRef;
import static org.junit.Assert.assertEquals;

@RunWith(JMockit.class)
public class CustomFunctionValidateDeductionAmountTest
  extends CustomFunctionTestJunit4 {

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  private CustomFunctionValidateDeductionAmount createTestSubject() {

    return new CustomFunctionValidateDeductionAmount();
  }

  @MethodRef(name = "getAdaptorValue",
    signature = "(QRulesParameters;)QAdaptor;")
  @Test
  public void testGetAdaptorValue_True() throws Exception {

    CustomFunctionValidateDeductionAmount testSubject;
    final String deductionPArameter =
      CustomFunctionConstants.DEDUCTION_TYPE_METHOD_AMT;
    final Money amountParameter = new Money(20.0);
    expectationsParameters(deductionPArameter, amountParameter);
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

    CustomFunctionValidateDeductionAmount testSubject;
    final String deductionPArameter = null;
    final Money amountParameter = null;
    expectationsParameters(deductionPArameter, amountParameter);
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
  public void testGetAdaptorValue_InvalidAmount() throws Exception {

    CustomFunctionValidateDeductionAmount testSubject;
    final String deductionPArameter =
      CustomFunctionConstants.DEDUCTION_TYPE_METHOD_AMT;
    final Money amountParameter = new Money(444.4);
    expectationsParameters(deductionPArameter, amountParameter);
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
  public void testGetAdaptorValue_NonDollarAmmount() throws Exception {

    CustomFunctionValidateDeductionAmount testSubject;
    final String deductionPArameter = "Pounds";
    final Money amountParameter = new Money(444.4);
    expectationsParameters(deductionPArameter, amountParameter);
    final RulesParameters rulesParameters = null;
    Adaptor result;

    // default test
    testSubject = createTestSubject();
    result = testSubject.getAdaptorValue(rulesParameters);

    final Adaptor.BooleanAdaptor booleanAdaptor = (BooleanAdaptor) result;

    assertEquals(booleanAdaptor.getValue(ieg2Context), true);
  }
}
