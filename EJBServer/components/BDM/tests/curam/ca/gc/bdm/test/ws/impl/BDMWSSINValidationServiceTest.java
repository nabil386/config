package curam.ca.gc.bdm.test.ws.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.ws.sinvalidation.impl.WSSINValidationRequestDetails;
import curam.ca.gc.bdm.ws.sinvalidation.impl.WSSINValidationService;
import curam.codetable.impl.GENDEREntry;
import curam.codetable.impl.TARGETSYSTEMSTATUSEntry;
import curam.core.impl.EnvVars;
import curam.ctm.targetsystem.impl.TargetSystemDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.type.Date;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the external validate SIN service.
 *
 * The tests do not run against the actual BDM SIR validation service. To run
 * these tests, one needs a hosted (either deployed or locally) mock SOAP server
 * configured with the correct responses.
 *
 * These tests are ignored so they are not included in the automated build
 * process. Any remote deployment cannot be guaranteed availability during
 * automated integration builds.
 *
 */
public class BDMWSSINValidationServiceTest extends CuramServerTestJUnit4 {

  // private final String testEndpointRootURL = "http://localhost:4546";

  private final String testEndpointRootURL = "";

  @Inject
  private TargetSystemDAO targetSystemDAO;

  public BDMWSSINValidationServiceTest() {

    super();
    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Override the endpoint domain if defined as a field value in
   * testEndpointRootURL (it will still be appended with /wsdl)
   */
  @Before
  public void setUp() {

    if (!StringUtils.isEmpty(testEndpointRootURL)) {
      targetSystemDAO
        .readByTargetSystemNameAndStatus(TARGETSYSTEMSTATUSEntry.ACTIVE,
          Configuration.getProperty(
            EnvVars.BDM_ENV_BDM_WSSINVALIDATION_TARGET_SYSTEM_NAME))
        .setRootURL(testEndpointRootURL);

    }

  }

  /**
   * This test asserts on the result of the ExternalValidateSINService that
   * connects to a mock server representing the BDM SIR validation service.
   *
   * Example Payload 1:
   * {ItemName: "SIN", Value: "045013364"},
   * {ItemName: "SIN_RESTRICTIONS", Value: "045013364"},
   * {ItemName: "SURNAME", Value: "ZIMMERMANN"},
   * {ItemName: "GIVEN_NAME", Value: "VERONICA"},
   * {ItemName: "DATE_OF_BIRTH", Value: "19620927"}
   *
   * @throws InformationalException
   * @throws AppException
   */
  @Ignore
  @Test
  public void testValidate_Match_Payload1()
    throws InformationalException, AppException {

    // ---- SETUP

    final WSSINValidationRequestDetails requestDetails =
      new WSSINValidationRequestDetails();
    requestDetails.setSin("045013364");
    requestDetails.setGivenName("Veronica");
    requestDetails.setSurname("Zimmermann");
    requestDetails.setGender(GENDEREntry.FEMALE);
    requestDetails.setDateOfBirth(Date.fromISO8601("19620927"));

    // ---- EXECUTE

    final WSSINValidationService extService = new WSSINValidationService();
    final boolean isValid = extService.validate(requestDetails);

    // ---- VERIFY

    assertTrue(
      "This scenario should produce a valid result from the mock server",
      isValid);

  }

  /**
   * This test asserts on the result of the ExternalValidateSINService that
   * connects to a mock server representing the BDM SIR validation service.
   *
   * Example Payload 2:
   * {ItemName: "SIN", Value: "146454285"},
   * {ItemName: "SIN_RESTRICTIONS", Value: false},
   * {ItemName: "SURNAME", Value: "SMITH"},
   * {ItemName: "DATE_OF_BIRTH", Value: "19921029"}
   *
   * @throws InformationalException
   * @throws AppException
   */
  @Ignore
  @Test
  public void testValidate_NoMatch_Payload2()
    throws InformationalException, AppException {

    // ---- SETUP

    final WSSINValidationRequestDetails requestDetails =
      new WSSINValidationRequestDetails();
    requestDetails.setSin("146454285");
    requestDetails.setSurname("SMITH");
    requestDetails.setDateOfBirth(Date.fromISO8601("19921029"));

    // ---- EXECUTE

    final WSSINValidationService extService = new WSSINValidationService();
    final boolean isValid = extService.validate(requestDetails);

    // ---- VERIFY

    assertFalse(
      "This scenario should produce an invalid result from the mock server",
      isValid);
  }

  /**
   * This test asserts on the result of the ExternalValidateSINService that
   * connects to a mock server representing the BDM SIR validation service.
   *
   * This sends an empty payload which should produce an error response from the
   * service. This error response should be rethrown as a Curam exception.
   *
   * @throws InformationalException
   * @throws AppException
   */
  @Ignore
  @Test
  public void testValidate_AppException_EmptyPayload()
    throws InformationalException, AppException {

    // ---- SETUP
    boolean hasErred = false;

    final WSSINValidationRequestDetails requestDetails =
      new WSSINValidationRequestDetails();

    // ---- EXECUTE

    try {
      final WSSINValidationService extService = new WSSINValidationService();
      extService.validate(requestDetails);
    }
    // ---- VERIFY
    catch (final AppException e) {
      hasErred = true;

    }
    assertTrue(
      "The external validation service has swallowed a payload error",
      hasErred);

  }

}
