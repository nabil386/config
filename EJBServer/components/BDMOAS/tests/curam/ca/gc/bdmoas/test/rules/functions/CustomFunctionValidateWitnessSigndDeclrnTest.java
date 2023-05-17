package curam.ca.gc.bdmoas.test.rules.functions;

import curam.codetable.IEGYESNO;
import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionValidateWitnessSigndDeclrn;
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
 * The Class tests {@link CustomFunctionValidateWitnessSigndDeclrnTest}
 * custom
 * function.
 */

@RunWith(JMockit.class)
public class CustomFunctionValidateWitnessSigndDeclrnTest
  extends CustomFunctionTestJunit4 {

  private static final boolean IS_VALID = true;

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The tested class. */
  @Tested
  CustomFunctionValidateWitnessSigndDeclrn validateWitnessSigndDeclrn;

  /**
   * Instantiates a new CustomFunctionValidateWitnessSigndDeclrnTest.
   */
  public CustomFunctionValidateWitnessSigndDeclrnTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    validateWitnessSigndDeclrn =
      new CustomFunctionValidateWitnessSigndDeclrn();
    ieg2Context = new IEG2Context();
  }

  /**
   * Test valid when applicant and spouse signed.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateWitnessSigndDeclrn_AplcntSpouseSignd()
    throws AppException, InformationalException {

    final String didAplicntSignedWithMark = IEGYESNO.YES;
    final String didSpouseOrCLPSignedWithMark = IEGYESNO.YES;
    final String didTPSignedAsWitness = IEGYESNO.NO;
    // Expectations
    expectationsParameters(didAplicntSignedWithMark,
      didSpouseOrCLPSignedWithMark, didTPSignedAsWitness);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateWitnessSigndDeclrn
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Since applicant and spouse signed, it should be valid.",
      IS_VALID, adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test valid when both applicant and witness signed.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateWitnessSigndDeclrn_WitnessAplcntSignd()
    throws AppException, InformationalException {

    final String didAplicntSignedWithMark = IEGYESNO.YES;
    final String didSpouseOrCLPSignedWithMark = IEGYESNO.NO;
    final String didTPSignedAsWitness = IEGYESNO.YES;

    // Expectations
    expectationsParameters(didAplicntSignedWithMark,
      didSpouseOrCLPSignedWithMark, didTPSignedAsWitness);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateWitnessSigndDeclrn
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals(
      "Since both applicant and witness signed, it should be valid.",
      IS_VALID, adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test valid when both spouse and witness signed.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateWitnessSigndDeclrn_WitnessSpouseSignd()
    throws AppException, InformationalException {

    final String didAplicntSignedWithMark = IEGYESNO.NO;
    final String didSpouseOrCLPSignedWithMark = IEGYESNO.YES;
    final String didTPSignedAsWitness = IEGYESNO.YES;

    // Expectations
    expectationsParameters(didAplicntSignedWithMark,
      didSpouseOrCLPSignedWithMark, didTPSignedAsWitness);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateWitnessSigndDeclrn
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Since both spouse and witness signed, it should be valid.",
      IS_VALID, adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test valid when only applicant signed.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateWitnessSigndDeclrn_AplcntSignd()
    throws AppException, InformationalException {

    final String didAplicntSignedWithMark = IEGYESNO.YES;
    final String didSpouseOrCLPSignedWithMark = IEGYESNO.NO;
    final String didTPSignedAsWitness = IEGYESNO.NO;

    // Expectations
    expectationsParameters(didAplicntSignedWithMark,
      didSpouseOrCLPSignedWithMark, didTPSignedAsWitness);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateWitnessSigndDeclrn
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Since only applicant signed, it should be valid.", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test valid when only spouse signed.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateWitnessSigndDeclrn_SpouseSignd()
    throws AppException, InformationalException {

    final String didAplicntSignedWithMark = IEGYESNO.NO;
    final String didSpouseOrCLPSignedWithMark = IEGYESNO.YES;
    final String didTPSignedAsWitness = IEGYESNO.NO;

    // Expectations
    expectationsParameters(didAplicntSignedWithMark,
      didSpouseOrCLPSignedWithMark, didTPSignedAsWitness);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateWitnessSigndDeclrn
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Since only spouse signed, it should be valid.", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test invalid when only witness signed.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateWitnessSigndDeclrn_WitnessSignd()
    throws AppException, InformationalException {

    final String didAplicntSignedWithMark = IEGYESNO.NO;
    final String didSpouseOrCLPSignedWithMark = IEGYESNO.NO;
    final String didTPSignedAsWitness = IEGYESNO.YES;

    // Expectations
    expectationsParameters(didAplicntSignedWithMark,
      didSpouseOrCLPSignedWithMark, didTPSignedAsWitness);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateWitnessSigndDeclrn
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Since witness only signed, it should be invalid.",
      !IS_VALID, adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test valid when all signed.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateWitnessSigndDeclrn_AllSignd()
    throws AppException, InformationalException {

    final String didAplicntSignedWithMark = IEGYESNO.YES;
    final String didSpouseOrCLPSignedWithMark = IEGYESNO.YES;
    final String didTPSignedAsWitness = IEGYESNO.YES;

    // Expectations
    expectationsParameters(didAplicntSignedWithMark,
      didSpouseOrCLPSignedWithMark, didTPSignedAsWitness);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateWitnessSigndDeclrn
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Since all signed, it should be valid.", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test valid when all are null.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateWitnessSigndDeclrn_allNull()
    throws AppException, InformationalException {

    final String didAplicntSignedWithMark = null;
    final String didSpouseOrCLPSignedWithMark = null;
    final String didTPSignedAsWitness = null;

    // Expectations
    expectationsParameters(didAplicntSignedWithMark,
      didSpouseOrCLPSignedWithMark, didTPSignedAsWitness);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateWitnessSigndDeclrn
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Since all are null, it should be valid.", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test valid when both spouse and witness are null.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateWitnessSigndDeclrn_bothNull()
    throws AppException, InformationalException {

    final String didAplicntSignedWithMark = null;
    final String didSpouseOrCLPSignedWithMark = null;
    final String didTPSignedAsWitness = IEGYESNO.YES;

    // Expectations
    expectationsParameters(didAplicntSignedWithMark,
      didSpouseOrCLPSignedWithMark, didTPSignedAsWitness);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateWitnessSigndDeclrn
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Since both are null, it should not be valid.", !IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test valid when spouse is null.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validateWitnessSigndDeclrn_AplcntNull()
    throws AppException, InformationalException {

    final String didAplicntSignedWithMark = null;
    final String didSpouseOrCLPSignedWithMark = IEGYESNO.YES;
    final String didTPSignedAsWitness = IEGYESNO.YES;

    // Expectations
    expectationsParameters(didAplicntSignedWithMark,
      didSpouseOrCLPSignedWithMark, didTPSignedAsWitness);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateWitnessSigndDeclrn
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
  public void test_validateWitnessSigndDeclrn_WitnessNull()
    throws AppException, InformationalException {

    final String didAplicntSignedWithMark = IEGYESNO.YES;
    final String didSpouseOrCLPSignedWithMark = null;
    final String didTPSignedAsWitness = IEGYESNO.YES;

    // Expectations
    expectationsParameters(didAplicntSignedWithMark,
      didSpouseOrCLPSignedWithMark, didTPSignedAsWitness);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateWitnessSigndDeclrn
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
  public void test_validateWitnessSigndDeclrn_Error()
    throws AppException, InformationalException {

    final String didAplicntSignedWithMark = IEGYESNO.YES;

    // Expectations
    expectationsParameters(didAplicntSignedWithMark);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateWitnessSigndDeclrn
        .getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Since only one parameter is given, should be invalid.",
      !IS_VALID, adaptorValue.getValue(ieg2Context));

  }
}
