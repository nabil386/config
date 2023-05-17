
package curam.ca.gc.bdm.test.rest.bdmwsaddressapi.impl;

import curam.ca.gc.bdm.message.impl.BDMRESTAPIERRORMESSAGEExceptionCreator;
import curam.ca.gc.bdm.rest.bdmwsaddressapi.impl.BDMWSAddressAPI;
import curam.ca.gc.bdm.rest.bdmwsaddressapi.struct.BDMWSAddressList;
import curam.ca.gc.bdm.rest.bdmwsaddressapi.struct.BDMWSAddressSearchKey;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressDetail;
import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressRequest;
import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressResponse;
import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressSearchResponse;
import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressService.RequestResponseType;
import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressServiceImpl;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import java.util.ArrayList;
import java.util.List;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Unit test for class BDMWSAddressAPI.
 */
@RunWith(JMockit.class)
public class BDMWSAddressAPITest extends CuramServerTestJUnit4 {

  private static final boolean WS_SERVICE_ENABLED = true;

  /** The address API. */
  @Tested
  BDMWSAddressAPI addressAPI;

  /**
   * Instantiates a new BDM address API test.
   */
  public BDMWSAddressAPITest() {

    super();
  }

  /**
   * Before.
   */
  @Before
  public void before() {

    addressAPI = new BDMWSAddressAPI();
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
   * Test search address invalid query params.
   *
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_searchAddress_invalidQueryParams()
    throws InformationalException, AppException {

    new Expectations(Configuration.class) {

      {
        Configuration.getBooleanProperty(
          EnvVars.BDM_ENV_BDM_WSADDRESS_SERVICE_ENABLEMENT);
        result = WS_SERVICE_ENABLED;
      }
    };
    // Postal code not present
    BDMWSAddressSearchKey addressSearchKey = new BDMWSAddressSearchKey();
    try {
      addressAPI.searchAddress(addressSearchKey);
      Assert
        .fail("should have failed as query param postalCode is not present");
    } catch (final AppException appException) {
      Assert.assertEquals(BDMRESTAPIERRORMESSAGEExceptionCreator
        .HTTP_400_SEARCH_ADDRESS_QUERY_PARAMS_NOT_VALID(), appException);
    }

    // Postal code present , country not valid
    addressSearchKey = new BDMWSAddressSearchKey();
    addressSearchKey.postalCode = "AIX 123";
    addressSearchKey.country = "USA";
    try {
      addressAPI.searchAddress(addressSearchKey);
      Assert.fail("should have failed as query param country is not valid");
    } catch (final AppException appException) {
      Assert.assertEquals(BDMRESTAPIERRORMESSAGEExceptionCreator
        .HTTP_400_SEARCH_ADDRESS_QUERY_PARAMS_NOT_VALID(), appException);
    }

    // Postal code present , country valid, language not valid
    addressSearchKey = new BDMWSAddressSearchKey();
    addressSearchKey.postalCode = "AIX 123";
    addressSearchKey.country = "CAN";
    addressSearchKey.language = "Spanish";
    try {
      addressAPI.searchAddress(addressSearchKey);
      Assert.fail("should have failed as query param language is not valid");
    } catch (final AppException appException) {
      Assert.assertEquals(BDMRESTAPIERRORMESSAGEExceptionCreator
        .HTTP_400_SEARCH_ADDRESS_QUERY_PARAMS_NOT_VALID(), appException);
    }

    // Postal code present , country not valid, language not valid
    addressSearchKey = new BDMWSAddressSearchKey();
    addressSearchKey.postalCode = "AIX 123";
    addressSearchKey.country = "USA";
    addressSearchKey.language = "Spanish";
    try {
      addressAPI.searchAddress(addressSearchKey);
      Assert.fail(
        "should have failed as query param country and language are not valid");
    } catch (final AppException appException) {
      Assert.assertEquals(BDMRESTAPIERRORMESSAGEExceptionCreator
        .HTTP_400_SEARCH_ADDRESS_QUERY_PARAMS_NOT_VALID(), appException);
    }

  }

  /**
   * Test search address with valid query params.
   *
   * @throws InformationalException the informational exception
   */
  @Ignore
  @Test
  public void test_searchAddress_validQueryParams()
    throws InformationalException, AppException {

    defaultExpectactions(WS_SERVICE_ENABLED);
    final BDMWSAddressSearchKey addressSearchKey =
      new BDMWSAddressSearchKey();
    addressSearchKey.postalCode = "A1A1A1";
    addressSearchKey.country = "CAN";
    addressSearchKey.language = "fr-CA";

    final BDMWSAddressList addressList =
      addressAPI.searchAddress(addressSearchKey);

    Assert.assertNotNull("The list should not be null", addressList.data);
    Assert.assertTrue("The list should not be empty",
      !addressList.data.isEmpty());

  }

  /**
   * Test search address with valid default query params.
   *
   * @throws InformationalException the informational exception
   */
  @Ignore
  @Test
  public void test_searchAddress_defaultQueryParams()
    throws InformationalException, AppException {

    defaultExpectactions(WS_SERVICE_ENABLED);
    final BDMWSAddressSearchKey addressSearchKey =
      new BDMWSAddressSearchKey();
    addressSearchKey.postalCode = "A1A1A1";

    final BDMWSAddressList addressList =
      addressAPI.searchAddress(addressSearchKey);

    Assert.assertNotNull("The list should not be null", addressList.data);
    Assert.assertTrue("The list should not be empty",
      !addressList.data.isEmpty());

  }

  /**
   * Test search address with valid default query params.
   *
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_searchAddress_serviceDisabled()
    throws InformationalException, AppException {

    defaultExpectactions(!WS_SERVICE_ENABLED);

    final BDMWSAddressSearchKey addressSearchKey =
      new BDMWSAddressSearchKey();
    final BDMWSAddressList addressList =
      addressAPI.searchAddress(addressSearchKey);
    // The service is disable , not even the query params are validated.
    Assert.assertNotNull("The list should not be null", addressList.data);
    Assert.assertTrue("The list should be empty", addressList.data.isEmpty());
  }

  /**
   * Default expectations for this test.
   *
   * @param isServiceEnabled the is service enabled
   */
  private void defaultExpectactions(final boolean isServiceEnabled)
    throws AppException {

    new Expectations(Configuration.class) {

      {
        Configuration.getBooleanProperty(
          EnvVars.BDM_ENV_BDM_WSADDRESS_SERVICE_ENABLEMENT);
        result = isServiceEnabled;
      }
    };

    final BDMWSAddressSearchResponse wsAddressSearchResponse =
      new BDMWSAddressSearchResponse();
    final List<BDMWSAddressDetail> searchResults = new ArrayList<>();
    final BDMWSAddressDetail wsAddressDetail = new BDMWSAddressDetail();
    searchResults.add(wsAddressDetail);
    wsAddressSearchResponse.setAddressMatchResults(searchResults);
    final BDMWSAddressResponse addressResponse = new BDMWSAddressResponse();
    addressResponse.getIndividualRequestResponses()
      .put(RequestResponseType.SEARCH, wsAddressSearchResponse);

    if (isServiceEnabled) {
      new MockUp<BDMWSAddressServiceImpl>() {

        @Mock
        public BDMWSAddressResponse search(
          final BDMWSAddressRequest wsAddressRequest) throws AppException {

          return addressResponse;
        }
      };

    }
  }

}
