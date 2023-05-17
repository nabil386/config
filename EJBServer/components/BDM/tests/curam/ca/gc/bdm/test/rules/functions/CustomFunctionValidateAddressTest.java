package curam.ca.gc.bdm.test.rules.functions;

import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressRequest;
import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressService;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.ieg.impl.IEG2Context;
import curam.rules.functions.BDMFunctor;
import curam.rules.functions.CustomFunctionValidateAddress;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.Adaptor.StringAdaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.rules.functor.CustomFunctor;
import java.util.ArrayList;
import java.util.List;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/**
 * The Class tests {@link CustomFunctionValidateAddress} custom
 * function.
 */
@RunWith(JMockit.class)
public class CustomFunctionValidateAddressTest
  extends CustomFunctionTestJunit4 {

  /** The Constant VALID_ADDRESS. */
  protected static final boolean VALID_ADDRESS = true;

  /** The validate address. */
  @Tested
  private CustomFunctionValidateAddress validateAddress;

  /** The custom functor. */
  @Mocked
  protected CustomFunctor customFunctor;

  /** The ws address service. */
  @Mocked
  protected BDMWSAddressService wsAddressService;

  /**
   * Instantiates a new custom functionvalidate address test.
   */
  public CustomFunctionValidateAddressTest() {

    super();
  }

  /**
   * Expectation WS validation enabled.
   *
   * @param isWSValidationEnabled the is WS validation enabled
   */
  protected void
    expectactionWSValidationEnabled(final boolean isWSValidationEnabled) {

    super.expectationsEnvVars(
      EnvVars.BDM_ENV_BDM_WSADDRESS_SERVICE_ENABLEMENT,
      isWSValidationEnabled);
  }

  /**
   * Expectations address parameters.
   *
   * @param params the address params
   * @throws InformationalException the informational exception
   * @throws AppException the app exception
   */
  protected void expectationsAddressParameters(final Object... params)
    throws InformationalException, AppException {

    final List<Adaptor> parameters = new ArrayList<>();
    for (final Object p : params) {
      if (p instanceof String) {
        parameters.add(AdaptorFactory.getStringAdaptor((String) p));
      } else if (p instanceof Integer) {
        parameters.add(AdaptorFactory.getIntegerAdaptor((Integer) p));
      } else if (null == p) {
        parameters.add(null);
      } else if (p instanceof Boolean) {
        parameters.add(AdaptorFactory.getBooleanAdaptor((Boolean) p));
      } else if (p instanceof Long) {
        parameters.add(AdaptorFactory.getLongAdaptor((Long) p));
      }

    }

    new MockUp<BDMFunctor>() {

      @Mock
      public String getOptionalStringParam(
        final RulesParameters rulesParameters, final int index)
        throws InformationalException, AppException {

        return ((StringAdaptor) parameters.get(index))
          .getStringValue(rulesParameters);
      }

    };
  }

  /**
   * Verifications Web Service is not called.
   *
   * @throws AppException the app exception
   */
  protected void verificationsWSNotCalled() throws AppException {

    new Verifications() {

      {
        wsAddressService.validate((BDMWSAddressRequest) any);

        times = 0;
      }
    };
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    validateAddress = new CustomFunctionValidateAddress();
  }

  /**
   * After each test.
   */
  @After
  public void after() {

    Configuration.setProperty(
      EnvVars.BDM_ENV_BDM_WSADDRESS_SERVICE_ENABLEMENT, CuramConst.gkTrue);
  }

  /**
   * Test validate address is WS validation enabled false.
   *
   * @throws Exception the exception
   */
  @Test
  public void test_validateAddress_isWSValidationEnabled_false()
    throws Exception {

    // Expectations
    final IEG2Context ieg2Context = new IEG2Context();

    final boolean isWSValidationEnabled = false;
    expectactionWSValidationEnabled(isWSValidationEnabled);
    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateAddress.getAdaptorValue(ieg2Context);

    // Verifications
    verificationsWSNotCalled();
    assertEquals(
      "The var ENV_BDM_WSADDRESS_SERVICE_ENABLEMENT should be disable", false,
      isWSValidationEnabled);
    assertEquals("Aaddress must be valid", VALID_ADDRESS,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test validate empty address.
   *
   * @throws Exception the exception
   */
  // @Test
  @Ignore // TODO - Enable WSValidation when implementing 1174
  public void test_validateEmptyAddress() throws Exception {

    // Expectations
    final IEG2Context ieg2Context = new IEG2Context();
    final boolean isWSValidationEnabled = true;
    expectactionWSValidationEnabled(isWSValidationEnabled);
    expectationsAddressParameters("", "", "", "", "", "", "", "");
    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateAddress.getAdaptorValue(ieg2Context);

    // Verifications
    verificationsWSNotCalled();
    assertEquals(
      "The var ENV_BDM_WSADDRESS_SERVICE_ENABLEMENT should be enabled", true,
      isWSValidationEnabled);
    assertEquals("Empty address is a valid address", VALID_ADDRESS,
      adaptorValue.getValue(ieg2Context));

  }

  /**
   * Test validate manual address.
   *
   * @throws Exception the exception
   */
  // @Test
  @Ignore // TODO - Enable WSValidation when implementing 1174
  public void test_validateManualAddress() throws Exception {

    // Expectations
    final IEG2Context ieg2Context = new IEG2Context();

    final String idAddManual = CustomFunctionValidateAddress.ID_ADD_MANUAL;
    final String streetName = "streetName";
    final boolean isWSValidationEnabled = true;
    expectactionWSValidationEnabled(isWSValidationEnabled);

    expectationsAddressParameters(idAddManual, "suiteNum", "streetNumber",
      streetName, "city", "province", "postalCode", "country");

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateAddress.getAdaptorValue(ieg2Context);

    // Verifications
    verificationsWSNotCalled();
    assertEquals("Manual address id is not correct ",
      CustomFunctionValidateAddress.ID_ADD_MANUAL, idAddManual);
    assertEquals("Manual address must be valid", VALID_ADDRESS,
      adaptorValue.getValue(ieg2Context));
    assertEquals(
      "The var ENV_BDM_WSADDRESS_SERVICE_ENABLEMENT should be enabled", true,
      isWSValidationEnabled);
  }
}
