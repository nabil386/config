package curam.ca.gc.bdmoas.test.rules.functions;

import curam.codetable.IEGYESNO;
import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionValidateSpouseOrWitnessSigndDeclrn;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.functor.Adaptor;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/**
 * The Class tests {@link CustomFunctionValidateSpouseOrWitnessSigndDeclrnTest}
 * custom
 * function.
 */

@RunWith(JMockit.class)
public class CustomFunctionValidateSpouseOrWitnessSigndDeclrnTest
  extends CustomFunctionTestJunit4 {

  private static final boolean IS_VALID = true;

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The tested class. */
  @Tested
  CustomFunctionValidateSpouseOrWitnessSigndDeclrn validateSpouseOrWitnessSigndDeclrn;

  /**
   * Instantiates a new CustomFunctionValidateAplcntOrWitnessSigndDeclrnTest.
   */
  public CustomFunctionValidateSpouseOrWitnessSigndDeclrnTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    validateSpouseOrWitnessSigndDeclrn =
      new CustomFunctionValidateSpouseOrWitnessSigndDeclrn();
    ieg2Context = new IEG2Context();
  }

  /**
   * Test valid when only spouse signed.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateSpouseOrWitnessSigndDeclrn_SpouseSignd()
    throws AppException, InformationalException {

    final String didSpouseOrCLPSigned = IEGYESNO.YES;
    final String didTPSignedBehalfOfSpouse = IEGYESNO.NO;

    // Expectations
    expectationsParameters(didSpouseOrCLPSigned, didTPSignedBehalfOfSpouse);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateSpouseOrWitnessSigndDeclrn
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Since spouse only signed, it should be valid.", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test valid when only witness signed.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateSpouseOrWitnessSigndDeclrn_WitnessSignd()
    throws AppException, InformationalException {

    final String didSpouseOrCLPSigned = IEGYESNO.NO;
    final String didTPSignedBehalfOfSpouse = IEGYESNO.YES;

    // Expectations
    expectationsParameters(didSpouseOrCLPSigned, didTPSignedBehalfOfSpouse);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateSpouseOrWitnessSigndDeclrn
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Since witness only signed, it should be valid.", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test invalid when both spouse and witness signed.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateSpouseOrWitnessSigndDeclrn_BothSignd()
    throws AppException, InformationalException {

    final String didSpouseOrCLPSigned = IEGYESNO.YES;
    final String didTPSignedBehalfOfSpouse = IEGYESNO.YES;

    // Expectations
    expectationsParameters(didSpouseOrCLPSigned, didTPSignedBehalfOfSpouse);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateSpouseOrWitnessSigndDeclrn
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals(
      "Since both spouse and witness signed, it should be invalid.",
      !IS_VALID, adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test valid when both spouse and witness are null.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateSpouseOrWitnessSigndDeclrn_bothNull()
    throws AppException, InformationalException {

    final String didSpouseOrCLPSigned = null;
    final String didTPSignedBehalfOfSpouse = null;

    // Expectations
    expectationsParameters(didSpouseOrCLPSigned, didTPSignedBehalfOfSpouse);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateSpouseOrWitnessSigndDeclrn
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Since both are null, it should be valid.", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test valid when spouse is null.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateSpouseOrWitnessSigndDeclrn_AplcntNull()
    throws AppException, InformationalException {

    final String didSpouseOrCLPSigned = null;
    final String didTPSignedBehalfOfSpouse = IEGYESNO.YES;

    // Expectations
    expectationsParameters(didSpouseOrCLPSigned, didTPSignedBehalfOfSpouse);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateSpouseOrWitnessSigndDeclrn
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Since spouse is null, it should be valid.", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test valid when witness is null.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateSpouseOrWitnessSigndDeclrn_WitnessNull()
    throws AppException, InformationalException {

    final String didSpouseOrCLPSigned = IEGYESNO.YES;
    final String didTPSignedBehalfOfSpouse = null;

    // Expectations
    expectationsParameters(didSpouseOrCLPSigned, didTPSignedBehalfOfSpouse);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateSpouseOrWitnessSigndDeclrn
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Since witness null, it should be valid.", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test invalid when parameters are missing (testing try/catch block)
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateSpouseOrWitnessSigndDeclrn_Error()
    throws AppException, InformationalException {

    final String didSpouseOrCLPSigned = IEGYESNO.YES;

    // Expectations
    expectationsParameters(didSpouseOrCLPSigned);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateSpouseOrWitnessSigndDeclrn
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Since only one parameter is given, should be invalid.",
      !IS_VALID, adaptorValue.getValue(ieg2Context));

  }
}
