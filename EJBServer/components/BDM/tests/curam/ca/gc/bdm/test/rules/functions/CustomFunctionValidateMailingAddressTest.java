package curam.ca.gc.bdm.test.rules.functions;

import curam.codetable.COUNTRY;
import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionValidateMailingAddress;
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
 * The Class tests {@link CustomFunctionvalidateStartedLivingDateBeforeDOB}
 * custom
 * function.
 */
@RunWith(JMockit.class)
public class CustomFunctionValidateMailingAddressTest
  extends CustomFunctionTestJunit4 {

  /** Test po box */
  private static final String PO_BOX = "PO 123";

  /** Test street number */
  private static final String STREET_NUMBER = "123";

  /** Test street name */
  private static final String STREET_NAME = "Street";

  /** Test suite number */
  private static final String SUITE_NUMBER = "2345";

  /** The input is valid */
  private static final boolean IS_VALID = true;

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The tested class. */
  @Tested
  CustomFunctionValidateMailingAddress customFunction;

  /**
   * Instantiates a new CustomFunctionconcatenateTest.
   */
  public CustomFunctionValidateMailingAddressTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    customFunction = new CustomFunctionValidateMailingAddress();
    ieg2Context = new IEG2Context();
  }

  /**
   * Test valid CA scenario.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_ca() throws AppException, InformationalException {

    final String country = COUNTRY.CA;
    final String pobox = "";
    final String streetNumber = STREET_NUMBER;
    final String streetName = STREET_NAME;
    final String suiteNum = SUITE_NUMBER;
    final String intlpobox = "";
    final String intlstreetNumber = "";
    final String intlstreetName = "";
    final String intlsuiteNum = "";

    // Expectations
    expectationsParameters(country, pobox, streetNumber, streetName, suiteNum,
      intlpobox, intlstreetNumber, intlstreetName, intlsuiteNum);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction.getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Valid Canadian address", IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test valid CA scenario.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_ca_nosuite()
    throws AppException, InformationalException {

    final String country = COUNTRY.CA;
    final String pobox = "";
    final String streetNumber = STREET_NUMBER;
    final String streetName = STREET_NAME;
    final String suiteNum = "";
    final String intlpobox = "";
    final String intlstreetNumber = "";
    final String intlstreetName = "";
    final String intlsuiteNum = "";

    // Expectations
    expectationsParameters(country, pobox, streetNumber, streetName, suiteNum,
      intlpobox, intlstreetNumber, intlstreetName, intlsuiteNum);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction.getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Valid Canadian address", IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test valid CA scenario.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_ca_pobox()
    throws AppException, InformationalException {

    final String country = COUNTRY.CA;
    final String pobox = PO_BOX;
    final String streetNumber = "";
    final String streetName = "";
    final String suiteNum = "";
    final String intlpobox = "";
    final String intlstreetNumber = "";
    final String intlstreetName = "";
    final String intlsuiteNum = "";

    // Expectations
    expectationsParameters(country, pobox, streetNumber, streetName, suiteNum,
      intlpobox, intlstreetNumber, intlstreetName, intlsuiteNum);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction.getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Valid Canadian address", IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test valid non-CA scenario.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_nonca() throws AppException, InformationalException {

    final String country = COUNTRY.US;
    final String pobox = "";
    final String streetNumber = "";
    final String streetName = "";
    final String suiteNum = "";
    final String intlpobox = "";
    final String intlstreetNumber = STREET_NUMBER;
    final String intlstreetName = STREET_NAME;
    final String intlsuiteNum = SUITE_NUMBER;

    // Expectations
    expectationsParameters(country, pobox, streetNumber, streetName, suiteNum,
      intlpobox, intlstreetNumber, intlstreetName, intlsuiteNum);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction.getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Valid Canadian address", IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test valid non-CA scenario.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_nonca_nosuite()
    throws AppException, InformationalException {

    final String country = COUNTRY.US;
    final String pobox = "";
    final String streetNumber = "";
    final String streetName = "";
    final String suiteNum = "";
    final String intlpobox = "";
    final String intlstreetNumber = STREET_NUMBER;
    final String intlstreetName = STREET_NAME;
    final String intlsuiteNum = "";

    // Expectations
    expectationsParameters(country, pobox, streetNumber, streetName, suiteNum,
      intlpobox, intlstreetNumber, intlstreetName, intlsuiteNum);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction.getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Valid Canadian address", IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test valid non-CA scenario.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_nonca_pobox()
    throws AppException, InformationalException {

    final String country = COUNTRY.US;
    final String pobox = "";
    final String streetNumber = "";
    final String streetName = "";
    final String suiteNum = "";
    final String intlpobox = PO_BOX;
    final String intlstreetNumber = "";
    final String intlstreetName = "";
    final String intlsuiteNum = "";

    // Expectations
    expectationsParameters(country, pobox, streetNumber, streetName, suiteNum,
      intlpobox, intlstreetNumber, intlstreetName, intlsuiteNum);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction.getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Valid Canadian address", IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test invalid CA scenario.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalid_ca_none()
    throws AppException, InformationalException {

    final String country = COUNTRY.CA;
    final String pobox = "";
    final String streetNumber = "";
    final String streetName = "";
    final String suiteNum = "";
    final String intlpobox = "";
    final String intlstreetNumber = "";
    final String intlstreetName = "";
    final String intlsuiteNum = "";

    // Expectations
    expectationsParameters(country, pobox, streetNumber, streetName, suiteNum,
      intlpobox, intlstreetNumber, intlstreetName, intlsuiteNum);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction.getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Valid Canadian address", !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test invalid CA scenario.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalid_ca_number()
    throws AppException, InformationalException {

    final String country = COUNTRY.CA;
    final String pobox = "";
    final String streetNumber = STREET_NUMBER;
    final String streetName = "";
    final String suiteNum = "";
    final String intlpobox = "";
    final String intlstreetNumber = "";
    final String intlstreetName = "";
    final String intlsuiteNum = "";

    // Expectations
    expectationsParameters(country, pobox, streetNumber, streetName, suiteNum,
      intlpobox, intlstreetNumber, intlstreetName, intlsuiteNum);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction.getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Valid Canadian address", !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test invalid CA scenario.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalid_ca_name()
    throws AppException, InformationalException {

    final String country = COUNTRY.CA;
    final String pobox = "";
    final String streetNumber = "";
    final String streetName = STREET_NAME;
    final String suiteNum = "";
    final String intlpobox = "";
    final String intlstreetNumber = "";
    final String intlstreetName = "";
    final String intlsuiteNum = "";

    // Expectations
    expectationsParameters(country, pobox, streetNumber, streetName, suiteNum,
      intlpobox, intlstreetNumber, intlstreetName, intlsuiteNum);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction.getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Valid Canadian address", !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test invalid CA scenario.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalid_ca_suite()
    throws AppException, InformationalException {

    final String country = COUNTRY.CA;
    final String pobox = "";
    final String streetNumber = "";
    final String streetName = "";
    final String suiteNum = SUITE_NUMBER;
    final String intlpobox = "";
    final String intlstreetNumber = "";
    final String intlstreetName = "";
    final String intlsuiteNum = "";

    // Expectations
    expectationsParameters(country, pobox, streetNumber, streetName, suiteNum,
      intlpobox, intlstreetNumber, intlstreetName, intlsuiteNum);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction.getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Valid Canadian address", !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test invalid non-CA scenario.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalid_nonca_none()
    throws AppException, InformationalException {

    final String country = COUNTRY.US;
    final String pobox = "";
    final String streetNumber = "";
    final String streetName = "";
    final String suiteNum = "";
    final String intlpobox = "";
    final String intlstreetNumber = "";
    final String intlstreetName = "";
    final String intlsuiteNum = "";

    // Expectations
    expectationsParameters(country, pobox, streetNumber, streetName, suiteNum,
      intlpobox, intlstreetNumber, intlstreetName, intlsuiteNum);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction.getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Valid Canadian address", !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test invalid non-CA scenario.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalid_nonca_number()
    throws AppException, InformationalException {

    final String country = COUNTRY.US;
    final String pobox = "";
    final String streetNumber = "";
    final String streetName = "";
    final String suiteNum = "";
    final String intlpobox = "";
    final String intlstreetNumber = STREET_NUMBER;
    final String intlstreetName = "";
    final String intlsuiteNum = "";

    // Expectations
    expectationsParameters(country, pobox, streetNumber, streetName, suiteNum,
      intlpobox, intlstreetNumber, intlstreetName, intlsuiteNum);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction.getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Valid Canadian address", !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test invalid non-CA scenario.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalid_nonca_name()
    throws AppException, InformationalException {

    final String country = COUNTRY.US;
    final String pobox = "";
    final String streetNumber = "";
    final String streetName = "";
    final String suiteNum = "";
    final String intlpobox = "";
    final String intlstreetNumber = "";
    final String intlstreetName = STREET_NAME;
    final String intlsuiteNum = "";

    // Expectations
    expectationsParameters(country, pobox, streetNumber, streetName, suiteNum,
      intlpobox, intlstreetNumber, intlstreetName, intlsuiteNum);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction.getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Valid Canadian address", !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test invalid non-CA scenario.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalid_nonca_suite()
    throws AppException, InformationalException {

    final String country = COUNTRY.US;
    final String pobox = "";
    final String streetNumber = "";
    final String streetName = "";
    final String suiteNum = "";
    final String intlpobox = "";
    final String intlstreetNumber = "";
    final String intlstreetName = "";
    final String intlsuiteNum = SUITE_NUMBER;

    // Expectations
    expectationsParameters(country, pobox, streetNumber, streetName, suiteNum,
      intlpobox, intlstreetNumber, intlstreetName, intlsuiteNum);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction.getAdaptorValue(ieg2Context);

    // Verifications
    assertEquals("Valid Canadian address", !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

}
