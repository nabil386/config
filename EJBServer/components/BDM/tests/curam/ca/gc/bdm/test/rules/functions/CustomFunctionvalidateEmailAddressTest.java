package curam.ca.gc.bdm.test.rules.functions;

import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionvalidateEmailAddress;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.Adaptor.BooleanAdaptor;
import javax.annotation.Generated;
import org.junit.Test;
import org.junit.tools.configuration.base.MethodRef;
import static org.junit.Assert.assertEquals;

@Generated(value = "org.junit-tools-1.1.0")

public class CustomFunctionvalidateEmailAddressTest
  extends CustomFunctionTestJunit4 {

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  private CustomFunctionvalidateEmailAddress createTestSubject() {

    return new CustomFunctionvalidateEmailAddress();
  }

  @MethodRef(name = "getAdaptorValue",
    signature = "(QRulesParameters;)QAdaptor;")
  @Test
  public void testGetAdaptorValue_True() throws Exception {

    CustomFunctionvalidateEmailAddress testSubject;
    final String emailAddress = "joe@joe.com";
    final String emailType = "gmail";
    expectationsParameters(emailAddress, emailType);

    final RulesParameters rulesParameters = null;
    Adaptor result;

    // default test
    testSubject = createTestSubject();
    result = testSubject.getAdaptorValue(ieg2Context);

    final Adaptor.BooleanAdaptor booleanAdaptor = (BooleanAdaptor) result;

    assertEquals(booleanAdaptor.getValue(ieg2Context), true);
  }

  @MethodRef(name = "getAdaptorValue",
    signature = "(QRulesParameters;)QAdaptor;")
  @Test
  public void testGetAdaptorValue_NotValid() throws Exception {

    CustomFunctionvalidateEmailAddress testSubject;

    final String emailAddress = "fjeijfeoi";
    final String emailType = "gmail";

    expectationsParameters(emailAddress, emailType);

    final RulesParameters rulesParameters = null;
    Adaptor result;

    // default test
    testSubject = createTestSubject();
    result = testSubject.getAdaptorValue(ieg2Context);

    final Adaptor.BooleanAdaptor booleanAdaptor = (BooleanAdaptor) result;

    assertEquals(booleanAdaptor.getValue(ieg2Context), false);
  }

  @MethodRef(name = "getAdaptorValue",
    signature = "(QRulesParameters;)QAdaptor;")
  @Test
  public void testGetAdaptorValue_TooLong() throws Exception {

    CustomFunctionvalidateEmailAddress testSubject;

    final String emailAddress =
      "fjeijfeoidsfaqwefawfawedfrawefdwaefdcawefdawefcawefcawefdcawefdcwefcawedfcawedfcawefcwfcdwefcwedfcaewfcewefdcwedfcawefcvawedfcvwaefcwenhfcaiujefhncvaiweuhfnciuawehnfvciuawhnefiuewhfciuvawehiufchawieudvfgiawuehdbvfiawedgfciawehdfcihelfvuahweubcvfiujvbhaiLUJWEhdncfvouawhednviuahwenduivchnsdkuhvnauidfasdiafeiujdhafkeh@gmail.com";
    final String emailType = "gmail";

    expectationsParameters(emailAddress, emailType);

    final RulesParameters rulesParameters = null;
    Adaptor result;

    // default test
    testSubject = createTestSubject();
    result = testSubject.getAdaptorValue(ieg2Context);

    final Adaptor.BooleanAdaptor booleanAdaptor = (BooleanAdaptor) result;

    assertEquals(booleanAdaptor.getValue(ieg2Context), false);
  }

  @MethodRef(name = "getAdaptorValue",
    signature = "(QRulesParameters;)QAdaptor;")
  @Test
  public void testGetAdaptorValue_Null() throws Exception {

    CustomFunctionvalidateEmailAddress testSubject;

    final RulesParameters rulesParameters = null;
    Adaptor result;

    // default test
    testSubject = createTestSubject();
    result = testSubject.getAdaptorValue(ieg2Context);

    final Adaptor.BooleanAdaptor booleanAdaptor = (BooleanAdaptor) result;

    assertEquals(booleanAdaptor.getValue(ieg2Context), true);
  }

  @MethodRef(name = "getAdaptorValue",
    signature = "(QRulesParameters;)QAdaptor;")
  @Test
  public void testGetAdaptorValue_Empty() throws Exception {

    CustomFunctionvalidateEmailAddress testSubject;
    final String emailAddress = "";
    final String emailType = "";
    expectationsParameters(emailAddress, emailType);

    final RulesParameters rulesParameters = null;
    Adaptor result;

    // default test
    testSubject = createTestSubject();
    result = testSubject.getAdaptorValue(ieg2Context);

    final Adaptor.BooleanAdaptor booleanAdaptor = (BooleanAdaptor) result;

    assertEquals(booleanAdaptor.getValue(ieg2Context), true);
  }

}
