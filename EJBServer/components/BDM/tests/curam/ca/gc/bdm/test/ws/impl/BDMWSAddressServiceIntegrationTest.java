package curam.ca.gc.bdm.test.ws.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressDetail;
import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressRequest;
import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressResponse;
import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressSearchResponse;
import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressService;
import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressValidateResponse;
import curam.codetable.impl.TARGETSYSTEMSTATUSEntry;
import curam.core.impl.EnvVars;
import curam.ctm.sl.entity.fact.TargetSystemServiceFactory;
import curam.ctm.sl.entity.struct.TargetSystemServiceDtls;
import curam.ctm.sl.entity.struct.TargetSystemServiceKey;
import curam.ctm.targetsystem.impl.TargetSystemDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test class for {@link BDMWSAddressService}
 */
public class BDMWSAddressServiceIntegrationTest
  extends CuramServerTestJUnit4 {

  private final String testOverrideEndpointRootURL = "";
  // private final String testOverrideEndpointRootURL = "http://localhost:4547";
  // private final String testOverrideEndpointRootURL =
  // "https://dsb-bsm-apps.service.gc.ca:7443";

  private final String testOverrideEndpointExtensionURL = "";
  // private final String testOverrideEndpointExtensionURL = "/WSAddress";
  // private final String testOverrideEndpointExtensionURL =
  // "/nonprd/perf/addressvalidation/2.0/?wsdl";

  @Inject
  private TargetSystemDAO targetSystemDAO;

  @Inject
  private BDMWSAddressService externalService;

  public BDMWSAddressServiceIntegrationTest() {

    super();
    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Override the endpoint url based on the test class field members (e.g. to
   * point towards a local mock server)
   */
  @Before
  public void setUp() {

    Configuration.setProperty("curam.trace", "trace_verbose");

    try {
      if (!StringUtils.isEmpty(testOverrideEndpointRootURL)) {
        targetSystemDAO
          .readByTargetSystemNameAndStatus(TARGETSYSTEMSTATUSEntry.ACTIVE,
            Configuration
              .getProperty(EnvVars.BDM_ENV_BDM_WSADDRESS_TARGET_SYSTEM_NAME))
          .setRootURL(testOverrideEndpointRootURL);

      }

      if (!StringUtils.isEmpty(testOverrideEndpointExtensionURL)) {
        final TargetSystemServiceKey key = new TargetSystemServiceKey();
        key.serviceID = 42201;
        final TargetSystemServiceDtls dtls =
          TargetSystemServiceFactory.newInstance().read(key);
        dtls.extensionURL = testOverrideEndpointExtensionURL;
        TargetSystemServiceFactory.newInstance().modify(key, dtls);
      }

    } catch (final AppException e) {

      e.printStackTrace();
      fail("Error in setup");
    } catch (final InformationalException e) {

      e.printStackTrace();
      fail("Error in setup");
    }

  }

  @After
  public void tearDown() {

    Configuration.setProperty("curam.trace", "");
  }

  /**
   * Given an address is available for a given PostalCode
   * When an BDMWSAddress.Search call is made with the request
   * Then it returns matching results
   */
  @Ignore
  @Test
  public void testSearch_WhenUrbanRouteAddressesFound_ThenReturnsResults()
    throws InformationalException, AppException {

    // ---- SETUP

    final BDMWSAddressRequest requestDetails = new BDMWSAddressRequest();
    requestDetails.setPostalCode("K0J 1V0");

    // ---- EXECUTE

    final BDMWSAddressResponse response =
      externalService.search(requestDetails);

    // ---- VERIFY

    assertTrue("Response should have been flagged as successful",
      response.isSuccessfulResponse());
    assertFalse("Response should not have a validate response",
      response.hasValidateResponse());
    assertTrue("Response should have a search response",
      response.hasSearchResponse());

    final BDMWSAddressSearchResponse wsAddressResponseList =
      response.getSearchResponse();

    assertNotNull("Search response should not be null",
      wsAddressResponseList);
    assertTrue("Mock search response should one result",
      wsAddressResponseList.getAddressMatchResults().size() == 25);
    final BDMWSAddressDetail addressMatch =
      wsAddressResponseList.getAddressMatchResults().get(0);
    assertFalse(
      "Mock search address detail should not have an empty AddressType",
      addressMatch.getAddressType().isEmpty());
    assertFalse(
      "Mock search address detail should not have an empty StreetName",
      addressMatch.getStreetName().isEmpty());
    assertFalse(
      "Mock search address detail should not have an empty StreetType",
      addressMatch.getStreetType().isEmpty());
    assertFalse(
      "Mock search address detail should not have an empty StreetNumberMinimum",
      addressMatch.getStreetNumberMinimum().isEmpty());
    assertFalse(
      "Mock search address detail should not have an empty StreetNumberMaximum",
      addressMatch.getStreetNumberMaximum().isEmpty());
    assertFalse(
      "Mock search address detail should not have an empty DirectoryAreaName",
      addressMatch.getDirectoryAreaName().isEmpty());
    assertFalse("Mock search address detail should not have an empty City",
      addressMatch.getCity().isEmpty());
    assertFalse(
      "Mock search address detail should not have an empty Province",
      addressMatch.getProvince().isEmpty());
    assertFalse(
      "Mock search address detail should not have an empty PostalCode",
      addressMatch.getPostalCode().isEmpty());
    assertFalse(
      "Mock search address detail should not have an empty PostalCode",
      addressMatch.getMiddleSpacedPostalCode().isEmpty());

  }

  /**
   * Given an address is available for a given PostalCode
   * When an BDMWSAddress.Search call is made with the request
   * Then it returns matching results
   */
  @Ignore
  @Test
  public void testSearch_WhenUrbanAddressFound_ThenReturnsResult()
    throws InformationalException, AppException {

    // ---- SETUP

    final BDMWSAddressRequest requestDetails = new BDMWSAddressRequest();
    requestDetails.setPostalCode("A1C5B9");

    // ---- EXECUTE

    final BDMWSAddressResponse response =
      externalService.search(requestDetails);

    // ---- VERIFY

    assertTrue("Response should have been flagged as successful",
      response.isSuccessfulResponse());
    assertFalse("Response should not have a validate response",
      response.hasValidateResponse());
    assertTrue("Response should have a search response",
      response.hasSearchResponse());

    final BDMWSAddressSearchResponse wsAddressResponseList =
      response.getSearchResponse();

    assertNotNull("Search response should not be null",
      wsAddressResponseList);
    assertTrue("Mock search response should one result",
      wsAddressResponseList.getAddressMatchResults().size() == 1);
    final BDMWSAddressDetail addressMatch =
      wsAddressResponseList.getAddressMatchResults().get(0);
    assertFalse(
      "Mock search address detail should not have an empty AddressType",
      addressMatch.getAddressType().isEmpty());
    assertFalse(
      "Mock search address detail should not have an empty StreetName",
      addressMatch.getStreetName().isEmpty());
    assertFalse(
      "Mock search address detail should not have an empty StreetType",
      addressMatch.getStreetType().isEmpty());
    assertFalse(
      "Mock search address detail should not have an empty StreetNumberMinimum",
      addressMatch.getStreetNumberMinimum().isEmpty());
    assertFalse(
      "Mock search address detail should not have an empty StreetNumberMaximum",
      addressMatch.getStreetNumberMaximum().isEmpty());
    assertFalse(
      "Mock search address detail should not have an empty DirectoryAreaName",
      addressMatch.getDirectoryAreaName().isEmpty());
    assertFalse("Mock search address detail should not have an empty City",
      addressMatch.getCity().isEmpty());
    assertFalse(
      "Mock search address detail should not have an empty Province",
      addressMatch.getProvince().isEmpty());
    assertFalse(
      "Mock search address detail should not have an empty PostalCode",
      addressMatch.getPostalCode().isEmpty());
    assertFalse(
      "Mock search address detail should not have an empty PostalCode",
      addressMatch.getMiddleSpacedPostalCode().isEmpty());

  }

  /**
   * Given an address is available for a given PostalCode
   * When an BDMWSAddress.Search call is made with the request
   * Then it returns matching results
   */
  @Ignore
  @Test
  public void testSearch_WhenRuralRouteAddressesFound_ThenReturnsResults()
    throws InformationalException, AppException {

    // ---- SETUP

    final BDMWSAddressRequest requestDetails = new BDMWSAddressRequest();
    requestDetails.setPostalCode("T0H1A0");

    // ---- EXECUTE

    final BDMWSAddressResponse response =
      externalService.search(requestDetails);

    // ---- VERIFY

    assertTrue("Response should have been flagged as successful",
      response.isSuccessfulResponse());
    assertFalse("Response should not have a validate response",
      response.hasValidateResponse());
    assertTrue("Response should have a search response",
      response.hasSearchResponse());

    final BDMWSAddressSearchResponse wsAddressResponseList =
      response.getSearchResponse();

    assertNotNull("Search response should not be null",
      wsAddressResponseList);
    assertTrue("Mock search response should one result",
      wsAddressResponseList.getAddressMatchResults().size() == 4);
    final BDMWSAddressDetail addressMatch =
      wsAddressResponseList.getAddressMatchResults().get(0);
    assertFalse(
      "Mock search address detail should not have an empty AddressType",
      addressMatch.getAddressType().isEmpty());
    // assertTrue("Mock search address detail should have an empty StreetName",
    // addressMatch.getStreetName().isEmpty());
    // assertFalse(
    // "Mock search address detail should not have an empty StreetType",
    // addressMatch.getStreetType().isEmpty());
    // assertFalse(
    // "Mock search address detail should not have an empty
    // StreetNumberMinimum",
    // addressMatch.getStreetNumberMinimum().isEmpty());
    // assertFalse(
    // "Mock search address detail should not have an empty
    // StreetNumberMaximum",
    // addressMatch.getStreetNumberMaximum().isEmpty());
    assertFalse(
      "Mock search address detail should not have an empty DirectoryAreaName",
      addressMatch.getDirectoryAreaName().isEmpty());
    assertFalse("Mock search address detail should not have an empty City",
      addressMatch.getCity().isEmpty());
    assertFalse(
      "Mock search address detail should not have an empty Province",
      addressMatch.getProvince().isEmpty());
    assertFalse(
      "Mock search address detail should not have an empty PostalCode",
      addressMatch.getPostalCode().isEmpty());
    assertFalse(
      "Mock search address detail should not have an empty PostalCode",
      addressMatch.getMiddleSpacedPostalCode().isEmpty());

  }

  /**
   * Given an address is valid for a given input
   * When an BDMWSAddress.Validate call is made with the request
   * Then it returns a is-valid result
   */
  @Ignore
  @Test
  public void testValidate_WhenAddressIsValid_ThenReturnValidAddressResponse()
    throws InformationalException, AppException {

    // ---- SETUP

    final BDMWSAddressRequest requestDetails = new BDMWSAddressRequest();
    requestDetails.setAddressLine("500 rue du Binóme");
    requestDetails.setCity("Québec");
    requestDetails.setProvince("QC");
    requestDetails.setCountry("CAN");
    requestDetails.setPostalCode("G1P 4P1");

    // ---- EXECUTE

    final BDMWSAddressResponse response =
      externalService.validate(requestDetails);

    // ---- VERIFY

    assertTrue("Response should have been flagged as successful",
      response.isSuccessfulResponse());
    assertTrue("Response should have a validate response",
      response.hasValidateResponse());
    assertFalse("Response should not have a search response",
      response.hasSearchResponse());

    final BDMWSAddressValidateResponse wsValidateResponse =
      response.getValidateResponse();

    assertNotNull("Validate response should not be null", wsValidateResponse);
    assertTrue("Mock is address valid result should be true",
      wsValidateResponse.isAddressValid());

    final List<String> errorMessages =
      wsValidateResponse.getValidationErrorMessages();
    assertNotNull("Validation error messages should not be null",
      errorMessages);
    assertEquals(
      "Mock validation error messages should be empty when result is valid",
      0, errorMessages.size());

  }

  /**
   * Given an address is invalid for a given input
   * When an BDMWSAddress.Validate call is made with the request
   * Then it returns a invalid result
   */
  @Ignore
  @Test
  public void
    testValidate_WhenAddressIsInvalid_ThenReturnInvalidAddressResponse()
      throws InformationalException, AppException {

    // ---- SETUP

    final BDMWSAddressRequest requestDetails = new BDMWSAddressRequest();
    requestDetails.setPostalCode("G1P 4P X");

    // ---- EXECUTE

    final BDMWSAddressResponse response =
      externalService.validate(requestDetails);

    // ---- VERIFY

    assertTrue("Response should have been flagged as successful",
      response.isSuccessfulResponse());
    assertTrue("Response should have a validate response",
      response.hasValidateResponse());
    assertFalse("Response should not have a search response",
      response.hasSearchResponse());

    final BDMWSAddressValidateResponse wsValidateResponse =
      response.getValidateResponse();

    assertNotNull("Validate response should not be null", wsValidateResponse);
    assertFalse("Mock is address valid result should be false",
      wsValidateResponse.isAddressValid());

    final List<String> errorMessages =
      wsValidateResponse.getValidationErrorMessages();
    assertNotNull("Validation error messages should not be null",
      errorMessages);
    assertEquals("Mock validation error messages should have a single result",
      1, errorMessages.size());
    final String errorMessage = errorMessages.get(0);
    assertEquals("Mock validation error message does not match",
      "No Match Found For This Address", errorMessage);

  }

}
