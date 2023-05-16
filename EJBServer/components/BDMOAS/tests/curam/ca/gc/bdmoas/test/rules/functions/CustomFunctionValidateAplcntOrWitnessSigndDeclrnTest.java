package curam.ca.gc.bdmoas.test.rules.functions;

import curam.codetable.IEGYESNO;
import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionValidateAplcntOrWitnessSigndDeclrn;
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
 * The Class tests {@link CustomFunctionValidateAplcntOrWitnessSigndDeclrnTest}
 * custom
 * function.
 */

@RunWith(JMockit.class)
public class CustomFunctionValidateAplcntOrWitnessSigndDeclrnTest
  extends CustomFunctionTestJunit4 {

  private static final boolean IS_VALID = true;

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The tested class. */
  @Tested
  CustomFunctionValidateAplcntOrWitnessSigndDeclrn validateAplcntOrWitnessSigndDeclrn;

  /**
   * Instantiates a new CustomFunctionValidateAplcntOrWitnessSigndDeclrnTest.
   */
  public CustomFunctionValidateAplcntOrWitnessSigndDeclrnTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    validateAplcntOrWitnessSigndDeclrn =
      new CustomFunctionValidateAplcntOrWitnessSigndDeclrn();
    ieg2Context = new IEG2Context();
  }

  /**
   * Test valid when only applicant signed.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateAplcntOrWitnessSigndDeclrn_AplcntSignd()
    throws AppException, InformationalException {

    final String didAplicntSigned = IEGYESNO.YES;
    final String didTPSignedBehalfOfAplcnt = IEGYESNO.NO;

    // Expectations
    expectationsParameters(didAplicntSigned, didTPSignedBehalfOfAplcnt);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateAplcntOrWitnessSigndDeclrn
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Since applicant only signed, it should be valid.", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test valid when only witness signed.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateAplcntOrWitnessSigndDeclrn_WitnessSignd()
    throws AppException, InformationalException {

    final String didAplicntSigned = IEGYESNO.NO;
    final String didTPSignedBehalfOfAplcnt = IEGYESNO.YES;

    // Expectations
    expectationsParameters(didAplicntSigned, didTPSignedBehalfOfAplcnt);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateAplcntOrWitnessSigndDeclrn
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Since witness only signed, it should be valid.", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test invalid when both applicant and witness signed.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateAplcntOrWitnessSigndDeclrn_BothSignd()
    throws AppException, InformationalException {

    final String didAplicntSigned = IEGYESNO.YES;
    final String didTPSignedBehalfOfAplcnt = IEGYESNO.YES;

    // Expectations
    expectationsParameters(didAplicntSigned, didTPSignedBehalfOfAplcnt);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateAplcntOrWitnessSigndDeclrn
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals(
      "Since both applicant and witness signed, it should be invalid.",
      !IS_VALID, adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test valid when both applicant and witness are null.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateAplcntOrWitnessSigndDeclrn_bothNull()
    throws AppException, InformationalException {

    final String didAplicntSigned = null;
    final String didTPSignedBehalfOfAplcnt = null;

    // Expectations
    expectationsParameters(didAplicntSigned, didTPSignedBehalfOfAplcnt);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateAplcntOrWitnessSigndDeclrn
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Since both are null, it should be valid.", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test valid when applicant is null.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateAplcntOrWitnessSigndDeclrn_AplcntNull()
    throws AppException, InformationalException {

    final String didAplicntSigned = null;
    final String didTPSignedBehalfOfAplcnt = IEGYESNO.YES;

    // Expectations
    expectationsParameters(didAplicntSigned, didTPSignedBehalfOfAplcnt);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateAplcntOrWitnessSigndDeclrn
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Since applicant is null, it should be valid.", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test valid when witness is null.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateAplcntOrWitnessSigndDeclrn_WitnessNull()
    throws AppException, InformationalException {

    final String didAplicntSigned = IEGYESNO.YES;
    final String didTPSignedBehalfOfAplcnt = null;

    // Expectations
    expectationsParameters(didAplicntSigned, didTPSignedBehalfOfAplcnt);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateAplcntOrWitnessSigndDeclrn
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
  public void test_validateAplcntOrWitnessSigndDeclrn_Error()
    throws AppException, InformationalException {

    final String didAplicntSigned = IEGYESNO.YES;

    // Expectations
    expectationsParameters(didAplicntSigned);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateAplcntOrWitnessSigndDeclrn
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Since only one parameter is given, should be invalid.",
      !IS_VALID, adaptorValue.getValue(ieg2Context));

  }
}
