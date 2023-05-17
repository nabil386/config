package curam.ca.gc.bdmoas.test.rules.functions;

import curam.ca.gc.bdm.codetable.BDMYESNO;
import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionValidatePartnerLastName;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.GeneralConstants;
import curam.util.rules.functor.Adaptor;
import curam.util.type.Date;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/**
 * The Class tests {@link CustomFunctionValidatePartnerLastNameTest} custom
 * function.
 */

@RunWith(JMockit.class)
public class CustomFunctionValidatePartnerLastNameTest
  extends CustomFunctionTestJunit4 {

  private static final boolean IS_VALID = true;

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The tested class. */
  @Tested
  CustomFunctionValidatePartnerLastName validatePartnerLastName;

  /**
   * Instantiates a new CustomFunctionValidatePartnerLastNameTest.
   */
  public CustomFunctionValidatePartnerLastNameTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    validatePartnerLastName = new CustomFunctionValidatePartnerLastName();
    ieg2Context = new IEG2Context();
  }

  /**
   * Test when all params are given and valid.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validatePartnerLastName_Success()
    throws AppException, InformationalException {

    final String firstName = "Jane";
    final String lastName = "Doe";
    final Date dob = Date.getDate("20211201");
    final String provideDiffAddr = BDMYESNO.YES;
    // Expectations
    expectationsParameters(firstName, lastName, dob, provideDiffAddr);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validatePartnerLastName
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("Last name should be valid", IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test when all last name is not given but other params are.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validatePartnerLastName_Null()
    throws AppException, InformationalException {

    final String firstName = null;
    final String lastName = null;
    final Date dob = Date.getDate("20211201");
    final String provideDiffAddr = BDMYESNO.YES;
    // Expectations
    expectationsParameters(firstName, lastName, dob, provideDiffAddr);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validatePartnerLastName
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("Empty last name must be non valid.", !IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test when all last name is not given but other params are.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validatePartnerLastName_Empty()
    throws AppException, InformationalException {

    final String firstName = null;
    final String lastName = GeneralConstants.kEmpty;
    final Date dob = null;
    final String provideDiffAddr = BDMYESNO.YES;
    // Expectations
    expectationsParameters(firstName, lastName, dob, provideDiffAddr);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validatePartnerLastName
        .getAdaptorValue(ieg2Context);
    // Verifications
    assertEquals("Empty last name must be non valid.", !IS_VALID,
      adaptorValue.getValue(ieg2Context));

  }

}
