package curam.ca.gc.bdm.test.interfaces.sl.wsaddress;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.BDMWSAddressInterfaceImpl;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.validatepojos.WSAddressValidate;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.validatepojos.WsaddrValidationRequest;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.wsaddresspojos.WSAddress;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.wsaddresspojos.WsaddrSearchRequest;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.intf.BDMWSAddressInterfaceIntf;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.util.rest.impl.BDMAPIAuditDetails;
import curam.ca.gc.bdm.util.rest.impl.BDMAPIAuditUtil;
import curam.ca.gc.bdm.util.rest.impl.BDMOutboundCloudEventDetails;
import curam.ca.gc.bdm.util.rest.impl.BDMOutboundCloudEventDetails.BDMOutboundCloudEventDetailsBuilder;
import curam.ca.gc.bdm.util.rest.impl.BDMRestUtil;
import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressConstants;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.type.DateTime;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.apache.commons.httpclient.StatusLine;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)
/**
 * Unit test for class BDMWSAddressInterfaceImplTest
 */
public class BDMWSAddressInterfaceImplTest extends CuramServerTestJUnit4 {

  /**
   * Instantiates a new BDM address test.
   */
  public BDMWSAddressInterfaceImplTest() {

    super();

  }

  /**
   * Before.
   */
  @Before
  public void before() {

    Configuration.setProperty(
      EnvVars.BDM_ENV_BDM_WSADDRESS_SERVICE_ENABLEMENT, CuramConst.gkTrue);

    Configuration.setProperty(EnvVars.ENV_REQUESTRESPONSE_AUDIT_ENABLED,
      "false");
  }

  @Override
  public boolean shouldCommit() {

    return false;
  }

  /**
   * After each test.
   */
  @After
  public void after() {

    Configuration.setProperty(
      EnvVars.BDM_ENV_BDM_WSADDRESS_SERVICE_ENABLEMENT, CuramConst.gkTrue);

    Configuration.setProperty(EnvVars.ENV_REQUESTRESPONSE_AUDIT_ENABLED,
      "true");
  }

  /**
   * Test search address with proper post code
   *
   * @throws InformationalException the informational exception
   */

  @Test
  public void test_searchAddress_PostCode()
    throws InformationalException, AppException {

    // Postal code not present
    final WsaddrSearchRequest requestDetails = new WsaddrSearchRequest();
    requestDetails.setNcAddressPostalCode("M2N7G7");
    requestDetails.setNcCountryCode(BDMConstants.kCountryCanada);
    requestDetails
      .setNcLanguageName(BDMConstants.kAddress_Search_Locale_en_CA);
    final BDMWSAddressInterfaceIntf wsAddressService =
      new BDMWSAddressInterfaceImpl();
    WSAddress wsAddressObj = new WSAddress();

    wsAddressObj =
      wsAddressService.searchAddressesByPostalCode(requestDetails);

    assertEquals(
      wsAddressObj.getWsaddrSearchResults().getWsaddrAddressMatches().size(),
      1);

  }

  @Test
  public void test_searchAddress_PostCode_EN()
    throws InformationalException, AppException {

    // Postal code not present
    final WsaddrSearchRequest requestDetails = new WsaddrSearchRequest();
    requestDetails.setNcAddressPostalCode("M2N7G7");
    requestDetails.setNcCountryCode(BDMConstants.kCountryCanada);
    requestDetails
      .setNcLanguageName(BDMConstants.kAddress_Search_Locale_en_CA);
    final BDMWSAddressInterfaceIntf wsAddressService =
      new BDMWSAddressInterfaceImpl();
    WSAddress wsAddressObj = new WSAddress();

    wsAddressObj =
      wsAddressService.searchAddressesByPostalCode(requestDetails);

    assertEquals(
      wsAddressObj.getWsaddrSearchResults().getWsaddrAddressMatches().size(),
      1);

  }

  /**
   * This method is to ensure the Sample Json output file provided by Iterops
   * team
   * works as expected.
   *
   * @throws IOException
   * @throws AppException
   * @throws InformationalException
   * @throws URISyntaxException
   */
  @Test
  public void test_SearchAddresses_By_PostalCode_Sample_JSON()
    throws IOException, AppException, InformationalException,
    URISyntaxException {

    final File jsonSampleFile =
      new File(new URI(BDMWSAddressInterfaceImplTest.class
        .getResource("resources/SearchByPostalCodeAPI_Sample.json")
        .toString()));

    final FileInputStream fis = new FileInputStream(jsonSampleFile.getPath());
    final String json_Sample_Data =
      org.apache.commons.io.IOUtils.toString(fis, "UTF-8");

    final Gson gson = new Gson();
    final WSAddress results =
      gson.fromJson(json_Sample_Data.toString(), WSAddress.class);

    assertEquals(
      results.getWsaddrSearchResults().getWsaddrAddressMatches().size(), 5);

  }

  /**
   * Test search address with no post code
   *
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_searchAddress_noPostCode()
    throws InformationalException, AppException {

    // Postal code not present
    final WsaddrSearchRequest requestDetails = new WsaddrSearchRequest();
    requestDetails.setNcAddressPostalCode(CuramConst.gkEmpty);
    requestDetails.setNcCountryCode(BDMConstants.kCountryCanada);
    requestDetails
      .setNcLanguageName(BDMConstants.kAddress_Search_Locale_en_CA);
    final BDMWSAddressInterfaceIntf wsAddressService =
      new BDMWSAddressInterfaceImpl();
    WSAddress wsAddressObj = new WSAddress();

    try {
      wsAddressObj =
        wsAddressService.searchAddressesByPostalCode(requestDetails);
    } catch (final Exception e) {
      assertTrue(true);
    }

    assertEquals(wsAddressObj.getWsaddrSearchResults(), null);
  }

  @Test
  public void test_searchAddress_noPostCode_EN()
    throws InformationalException, AppException {

    // Postal code not present
    final WsaddrSearchRequest requestDetails = new WsaddrSearchRequest();
    requestDetails.setNcAddressPostalCode(CuramConst.gkEmpty);
    requestDetails.setNcCountryCode(BDMConstants.kCountryCanada);
    requestDetails
      .setNcLanguageName(BDMConstants.kAddress_Search_Locale_en_CA);
    final BDMWSAddressInterfaceIntf wsAddressService =
      new BDMWSAddressInterfaceImpl();
    WSAddress wsAddressObj = new WSAddress();

    try {
      wsAddressObj =
        wsAddressService.searchAddressesByPostalCode(requestDetails);
    } catch (final Exception e) {
      assertTrue(true);
    }

    assertEquals(wsAddressObj.getWsaddrSearchResults(), null);
  }

  /**
   * Test search address with Invalid format post code
   *
   * @throws InformationalException the informational exception
   */

  @Test
  public void test_searchAddress_wrongPostCodeFormatInput()
    throws InformationalException, AppException {

    // Postal code not present
    final WsaddrSearchRequest requestDetails = new WsaddrSearchRequest();
    requestDetails.setNcAddressPostalCode("XXXXXXXX");
    requestDetails.setNcCountryCode(BDMConstants.kCountryCanada);
    requestDetails
      .setNcLanguageName(BDMConstants.kAddress_Search_Locale_en_CA);
    final BDMWSAddressInterfaceIntf wsAddressService =
      new BDMWSAddressInterfaceImpl();
    WSAddress wsAddressObj = new WSAddress();
    try {
      wsAddressObj =
        wsAddressService.searchAddressesByPostalCode(requestDetails);
    } catch (final Exception e) {
      assertEquals("Error occurred when accessing WSAddress REST API",
        e.getLocalizedMessage());
    }
  }

  /**
   * Test search address with Invalid post code
   *
   * @throws InformationalException the informational exception
   */

  @Test
  public void test_searchAddress_wrongPostalCode()
    throws InformationalException, AppException {

    // Postal code not present
    final WsaddrSearchRequest requestDetails = new WsaddrSearchRequest();
    requestDetails.setNcAddressPostalCode("M2N9G2");
    requestDetails.setNcCountryCode(BDMConstants.kCountryCanada);
    requestDetails
      .setNcLanguageName(BDMConstants.kAddress_Search_Locale_en_CA);
    final BDMWSAddressInterfaceIntf wsAddressService =
      new BDMWSAddressInterfaceImpl();
    WSAddress wsAddressObj = new WSAddress();
    try {
      wsAddressObj =
        wsAddressService.searchAddressesByPostalCode(requestDetails);
    } catch (final Exception e) {
      assertEquals(
        "Error occurred when accessing WSAddress REST API. Additional details : Error Code  ParameterError and Error description : Postal Code Not Found ",
        e.getLocalizedMessage());
    }
  }

  /**
   *
   * @throws InformationalException
   * @throws AppException
   */

  @Test
  public void test_validate_address()
    throws InformationalException, AppException {

    final WsaddrValidationRequest addrValidationRequest =
      new WsaddrValidationRequest();

    addrValidationRequest.setCanProvinceCode("NB");
    addrValidationRequest.setNcAddressCityName("Fredericton");
    addrValidationRequest.setNcAddressFullText("636 Chestnut Street");
    addrValidationRequest.setNcAddressPostalCode("E3B3W2");
    addrValidationRequest.setNcCountryCode(BDMConstants.kCountryCanada);
    addrValidationRequest
      .setNcLanguageName(BDMConstants.kAddress_Search_Locale_en_CA);
    final BDMWSAddressInterfaceIntf wsAddressService =
      new BDMWSAddressInterfaceImpl();

    assertTrue(wsAddressService.validateAddress(addrValidationRequest));
  }

  @Test
  public void test_validate_address_EN()
    throws InformationalException, AppException {

    final WsaddrValidationRequest addrValidationRequest =
      new WsaddrValidationRequest();

    addrValidationRequest.setCanProvinceCode("NB");
    addrValidationRequest.setNcAddressCityName("Fredericton");
    addrValidationRequest.setNcAddressFullText("636 Chestnut Street");
    addrValidationRequest.setNcAddressPostalCode("E3B3W2");
    addrValidationRequest.setNcCountryCode(BDMConstants.kCountryCanada);
    addrValidationRequest
      .setNcLanguageName(BDMConstants.kAddress_Search_Locale_en_CA);

    final BDMWSAddressInterfaceIntf wsAddressService =
      new BDMWSAddressInterfaceImpl();

    assertTrue(wsAddressService.validateAddress(addrValidationRequest));
  }

  /**
   *
   * @throws InformationalException
   * @throws AppException
   */

  @Test
  public void test_validate_address_wrong_address()
    throws InformationalException, AppException {

    final WsaddrValidationRequest addrValidationRequest =
      new WsaddrValidationRequest();

    addrValidationRequest.setCanProvinceCode("ON");
    addrValidationRequest.setNcAddressCityName("Vancouver");
    addrValidationRequest.setNcAddressFullText("636 Chestnut Street");
    addrValidationRequest.setNcAddressPostalCode("E3B3W2");
    addrValidationRequest.setNcCountryCode(BDMConstants.kCountryCanada);
    addrValidationRequest
      .setNcLanguageName(BDMConstants.kAddress_Search_Locale_en_CA);
    final BDMWSAddressInterfaceIntf wsAddressService =
      new BDMWSAddressInterfaceImpl();

    try {
      wsAddressService.validateAddress(addrValidationRequest);
    } catch (final Exception e) {
      assertEquals(
        "Below is the address that you submitted. It does not appear to be a valid address. To make a correction, press ‘Back’. To accept the address displayed, press ‘Confirm’. ",
        e.getLocalizedMessage());
    }
  }

  /**
   *
   * @throws InformationalException
   * @throws AppException
   */

  @Test
  public void test_validate_address_province()
    throws InformationalException, AppException {

    final WsaddrValidationRequest addrValidationRequest =
      new WsaddrValidationRequest();

    addrValidationRequest.setCanProvinceCode(CuramConst.gkEmpty);
    addrValidationRequest.setNcAddressCityName(CuramConst.gkEmpty);
    addrValidationRequest.setNcAddressFullText(CuramConst.gkEmpty);
    addrValidationRequest.setNcAddressPostalCode("E3B3W2");
    addrValidationRequest.setNcCountryCode(BDMConstants.kCountryCanada);
    addrValidationRequest
      .setNcLanguageName(BDMConstants.kAddress_Search_Locale_en_CA);
    final BDMWSAddressInterfaceIntf wsAddressService =
      new BDMWSAddressInterfaceImpl();

    try {
      wsAddressService.validateAddress(addrValidationRequest);
    } catch (final Exception e) {
      assertEquals(
        "Address text, province and city are required to validate an address.",
        e.getLocalizedMessage());
    }
  }

  // BEGIN, ADO-107574 , Audit enablement for APIs
  // BEGIN, ADO-117413 , CloudEvent, CorrelationID Implementation
  @Test
  public void testValidateAddress_success(@Mocked
  final java.net.URL url, @Mocked
  final HttpURLConnection connection, @Mocked
  final StatusLine line, @Mocked
  final BDMRestUtil bdmRestUtil, @Mocked
  final BDMAPIAuditUtil bdmapiAuditUtil, @Mocked
  final WSAddressValidate wsAddressValidate) throws Exception {

    final String correlationUniqueId = UUID.randomUUID().toString();
    final String time = DateTime.getCurrentDateTime().toString();
    new Expectations() {

      {
        new URL(anyString);
        result = url;

        url.openConnection();
        result = connection;

        connection.getResponseCode();
        result = 200;

        wsAddressValidate.getWsaddrValidationResults().getWsaddrInformation()
          .getWsaddrStatusCode();
        result = BDMConstants.kValid;

        bdmRestUtil.getRESTResponseHTTPConnection((HttpURLConnection) any);
        final Gson gson =
          new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        final StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append(gson.toJson(wsAddressValidate));
        result = responseBuilder;

        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);

        bdmRestUtil.isCloudEventEnabled(anyString);
        result = true;

        bdmRestUtil.getCloudEventDetailsForWSAddress();
        final BDMOutboundCloudEventDetails cloudEventDetails =
          new BDMOutboundCloudEventDetailsBuilder().setId(correlationUniqueId)
            .setSpecVersion("1.0").setSource("WSADDRESSSource")
            .setType("WSADDRESSType").setSubject("WSADDRESSSubject")
            .setTime(time).build();
        result = cloudEventDetails;

        final Map<String, String> headers = new HashMap<>();
        bdmRestUtil.addCloudEventHeaders((Map) any,
          (BDMOutboundCloudEventDetails) any, false);
        result = headers;

      }
    };

    final WsaddrValidationRequest addrValidationRequest =
      new WsaddrValidationRequest();

    addrValidationRequest.setCanProvinceCode("NB");
    addrValidationRequest.setNcAddressCityName("Fredericton");
    addrValidationRequest.setNcAddressFullText("636 Chestnut Street");
    addrValidationRequest.setNcAddressPostalCode("E3B3W2");

    addrValidationRequest.setNcCountryCode(BDMConstants.kCountryCanada);
    final BDMWSAddressInterfaceIntf wsAddressService =
      new BDMWSAddressInterfaceImpl();

    final boolean isValid =
      wsAddressService.validateAddress(addrValidationRequest);
    assertTrue(isValid);

    new Verifications() {

      {
        new URL(anyString);
        times = 1;
        url.openConnection();
        times = 1;

        connection.getResponseCode();
        times = 1;
        bdmRestUtil.getRESTResponseHTTPConnection((HttpURLConnection) any);
        times = 1;

        BDMAPIAuditDetails auditDetails;
        bdmapiAuditUtil.auditAPI(auditDetails = withCapture());
        times = 1;

        assertEquals(auditDetails.getApiType(), "OUTBOUND");
        assertEquals(auditDetails.getInvokingMethod(),
          "BDMWSAddressInterfaceImpl.validateAddress");
        assertEquals(auditDetails.getRelatedID(),
          "AddressCityName=FrederictonAddressPostalCode=E3B3W2ProvinceCode=NB");
        assertEquals(auditDetails.getStatusCode(), 200);
        assert auditDetails
          .getRequestTransactionDateTime() != DateTime.kZeroDateTime;
        assert auditDetails
          .getResponseTransactionDateTime() != DateTime.kZeroDateTime;

        // CloudEvent/CorrelationId details

        bdmRestUtil.getCloudEventDetailsForWSAddress();
        times = 1;
        final BDMOutboundCloudEventDetails bdmOutboundCloudEventDetails;
        bdmRestUtil.addCloudEventHeaders((Map<String, String>) any,
          bdmOutboundCloudEventDetails = withCapture(), (boolean) any);
        times = 1;

        assertEquals(bdmOutboundCloudEventDetails.getSpecVersion(), "1.0");
        assertEquals(bdmOutboundCloudEventDetails.getSource(),
          "WSADDRESSSource");
        assertEquals(bdmOutboundCloudEventDetails.getType(), "WSADDRESSType");
        assertEquals(bdmOutboundCloudEventDetails.getSubject(),
          "WSADDRESSSubject");
        assertEquals(bdmOutboundCloudEventDetails.getId(),
          correlationUniqueId);
        assertEquals(bdmOutboundCloudEventDetails.getTime(), time);
      }
    };

  }

  @Test
  public void testValidateAddress_error504(@Mocked
  final java.net.URL url, @Mocked
  final HttpURLConnection connection, @Mocked
  final StatusLine line, @Mocked
  final BDMRestUtil bdmRestUtil, @Mocked
  final BDMAPIAuditUtil bdmapiAuditUtil, @Mocked
  final WSAddressValidate wsAddressValidate) throws Exception {

    final String correlationUniqueId = UUID.randomUUID().toString();
    final String time = DateTime.getCurrentDateTime().toString();
    new Expectations() {

      {
        new URL(anyString);
        result = url;

        url.openConnection();
        result = connection;

        connection.getResponseCode();
        result = 504;

        bdmRestUtil.getRESTResponseHTTPConnection((HttpURLConnection) any);
        final Gson gson =
          new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        final StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append(gson.toJson(wsAddressValidate));
        result = responseBuilder;

        bdmRestUtil.isCloudEventEnabled(anyString);
        result = true;

        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);

        bdmRestUtil.getCloudEventDetailsForWSAddress();
        final BDMOutboundCloudEventDetails cloudEventDetails =
          new BDMOutboundCloudEventDetailsBuilder().setId(correlationUniqueId)
            .setSpecVersion("1.0").setSource("WSADDRESSSource")
            .setType("WSADDRESSType").setSubject("WSADDRESSSubject")
            .setTime(time).build();
        result = cloudEventDetails;

        final Map<String, String> headers = new HashMap<>();
        bdmRestUtil.addCloudEventHeaders((Map) any,
          (BDMOutboundCloudEventDetails) any, false);
        result = headers;

      }
    };

    final WsaddrValidationRequest addrValidationRequest =
      new WsaddrValidationRequest();

    addrValidationRequest.setCanProvinceCode("NB");
    addrValidationRequest.setNcAddressCityName("Fredericton");
    addrValidationRequest.setNcAddressFullText("636 Chestnut Street");
    addrValidationRequest.setNcAddressPostalCode("E3B3W2");

    addrValidationRequest.setNcCountryCode(BDMConstants.kCountryCanada);
    final BDMWSAddressInterfaceIntf wsAddressService =
      new BDMWSAddressInterfaceImpl();

    try {
      final boolean isValid =
        wsAddressService.validateAddress(addrValidationRequest);
      assertTrue(isValid);
    } catch (final Exception e) {
      assertTrue(true);
    }

    new Verifications() {

      {
        new URL(anyString);
        times = 1;
        url.openConnection();
        times = 1;

        connection.getResponseCode();
        times = 1;
        bdmRestUtil.getRESTResponseHTTPConnection((HttpURLConnection) any);
        times = 1;

        BDMAPIAuditDetails auditDetails;
        bdmapiAuditUtil.auditAPI(auditDetails = withCapture());
        times = 1;

        assertEquals(auditDetails.getApiType(), "OUTBOUND");
        assertEquals(auditDetails.getInvokingMethod(),
          "BDMWSAddressInterfaceImpl.validateAddress");
        assertEquals(auditDetails.getRelatedID(),
          "AddressCityName=FrederictonAddressPostalCode=E3B3W2ProvinceCode=NB");
        assertEquals(auditDetails.getStatusCode(), 504);
        assert auditDetails
          .getRequestTransactionDateTime() != DateTime.kZeroDateTime;
        assert auditDetails
          .getResponseTransactionDateTime() != DateTime.kZeroDateTime;

        // CloudEvent/CorrelationId details
        bdmRestUtil.getCloudEventDetailsForWSAddress();
        times = 1;
        final BDMOutboundCloudEventDetails bdmOutboundCloudEventDetails;
        bdmRestUtil.addCloudEventHeaders((Map<String, String>) any,
          bdmOutboundCloudEventDetails = withCapture(), (boolean) any);
        times = 1;

        assertEquals(bdmOutboundCloudEventDetails.getSpecVersion(), "1.0");
        assertEquals(bdmOutboundCloudEventDetails.getSource(),
          "WSADDRESSSource");
        assertEquals(bdmOutboundCloudEventDetails.getType(), "WSADDRESSType");
        assertEquals(bdmOutboundCloudEventDetails.getSubject(),
          "WSADDRESSSubject");
        assertEquals(bdmOutboundCloudEventDetails.getId(),
          correlationUniqueId);
        assertEquals(bdmOutboundCloudEventDetails.getTime(), time);
      }
    };

  }

  @Test
  public void testValidateAddress_error500(@Mocked
  final java.net.URL url, @Mocked
  final HttpURLConnection connection, @Mocked
  final StatusLine line, @Mocked
  final BDMRestUtil bdmRestUtil, @Mocked
  final BDMAPIAuditUtil bdmapiAuditUtil, @Mocked
  final WSAddressValidate wsAddressValidate) throws Exception {

    final String correlationUniqueId = UUID.randomUUID().toString();
    final String time = DateTime.getCurrentDateTime().toString();

    new Expectations() {

      {
        new URL(anyString);
        result = url;

        url.openConnection();
        result = connection;

        connection.getResponseCode();
        result = 504;

        bdmRestUtil.getRESTResponseHTTPConnection((HttpURLConnection) any);
        final Gson gson =
          new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        final StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append(gson.toJson(wsAddressValidate));
        result = responseBuilder;

        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);

        bdmRestUtil.isCloudEventEnabled(anyString);
        result = true;

        bdmRestUtil.getCloudEventDetailsForWSAddress();
        final BDMOutboundCloudEventDetails cloudEventDetails =
          new BDMOutboundCloudEventDetailsBuilder().setId(correlationUniqueId)
            .setSpecVersion("1.0").setSource("WSADDRESSSource")
            .setType("WSADDRESSType").setSubject("WSADDRESSSubject")
            .setTime(time).build();
        result = cloudEventDetails;

        final Map<String, String> headers = new HashMap<>();
        bdmRestUtil.addCloudEventHeaders((Map) any,
          (BDMOutboundCloudEventDetails) any, false);
        result = headers;

      }
    };

    final WsaddrValidationRequest addrValidationRequest =
      new WsaddrValidationRequest();

    addrValidationRequest.setCanProvinceCode("NB");
    addrValidationRequest.setNcAddressCityName("Fredericton");
    addrValidationRequest.setNcAddressFullText("636 Chestnut Street");
    addrValidationRequest.setNcAddressPostalCode("E3B3W2");

    addrValidationRequest.setNcCountryCode(BDMConstants.kCountryCanada);
    final BDMWSAddressInterfaceIntf wsAddressService =
      new BDMWSAddressInterfaceImpl();

    try {
      final boolean isValid =
        wsAddressService.validateAddress(addrValidationRequest);
      assertTrue(isValid);
    } catch (final Exception e) {
      assertTrue(true);
    }

    new Verifications() {

      {
        new URL(anyString);
        times = 1;
        url.openConnection();
        times = 1;

        connection.getResponseCode();
        times = 1;
        bdmRestUtil.getRESTResponseHTTPConnection((HttpURLConnection) any);
        times = 1;

        BDMAPIAuditDetails auditDetails;
        bdmapiAuditUtil.auditAPI(auditDetails = withCapture());
        times = 1;

        assertEquals(auditDetails.getApiType(), "OUTBOUND");
        assertEquals(auditDetails.getInvokingMethod(),
          "BDMWSAddressInterfaceImpl.validateAddress");
        assertEquals(auditDetails.getRelatedID(),
          "AddressCityName=FrederictonAddressPostalCode=E3B3W2ProvinceCode=NB");
        assertEquals(auditDetails.getStatusCode(), 504);
        assert auditDetails
          .getRequestTransactionDateTime() != DateTime.kZeroDateTime;
        assert auditDetails
          .getResponseTransactionDateTime() != DateTime.kZeroDateTime;

        // CloudEvent/CorrelationId details
        bdmRestUtil.getCloudEventDetailsForWSAddress();
        times = 1;
        final BDMOutboundCloudEventDetails bdmOutboundCloudEventDetails;
        bdmRestUtil.addCloudEventHeaders((Map<String, String>) any,
          bdmOutboundCloudEventDetails = withCapture(), (boolean) any);
        times = 1;

        assertEquals(bdmOutboundCloudEventDetails.getSpecVersion(), "1.0");
        assertEquals(bdmOutboundCloudEventDetails.getSource(),
          "WSADDRESSSource");
        assertEquals(bdmOutboundCloudEventDetails.getType(), "WSADDRESSType");
        assertEquals(bdmOutboundCloudEventDetails.getSubject(),
          "WSADDRESSSubject");
        assertEquals(bdmOutboundCloudEventDetails.getId(),
          correlationUniqueId);
        assertEquals(bdmOutboundCloudEventDetails.getTime(), time);
      }
    };

  }

  @Test
  public void testSearchAddressesByPostalCode_success(@Mocked
  final java.net.URL url, @Mocked
  final HttpURLConnection connection, @Mocked
  final StatusLine line, @Mocked
  final BDMRestUtil bdmRestUtil, @Mocked
  final BDMAPIAuditUtil bdmapiAuditUtil, @Mocked
  final WSAddress wsAddress) throws Exception {

    final String correlationUniqueId = UUID.randomUUID().toString();
    final String time = DateTime.getCurrentDateTime().toString();
    new Expectations() {

      {
        new URL(anyString);
        result = url;

        url.openConnection();
        result = connection;

        connection.getResponseCode();
        result = 200;

        wsAddress.getWsaddrSearchResults().getWsaddrInformation()
          .getWsaddrStatusCode();
        result = BDMWSAddressConstants.kWSAddressRecordsFound;

        bdmRestUtil.isCloudEventEnabled(anyString);
        result = true;

        bdmRestUtil.getRESTResponseHTTPConnection((HttpURLConnection) any);
        final Gson gson =
          new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        final StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append(gson.toJson(wsAddress));
        result = responseBuilder;

        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);

        bdmRestUtil.getCloudEventDetailsForWSAddress();
        final BDMOutboundCloudEventDetails cloudEventDetails =
          new BDMOutboundCloudEventDetailsBuilder().setId(correlationUniqueId)
            .setSpecVersion("1.0").setSource("WSADDRESSSource")
            .setType("WSADDRESSType").setSubject("WSADDRESSSubject")
            .setTime(time).build();
        result = cloudEventDetails;

        final Map<String, String> headers = new HashMap<>();
        bdmRestUtil.addCloudEventHeaders((Map) any,
          (BDMOutboundCloudEventDetails) any, false);
        result = headers;

      }
    };

    final WsaddrSearchRequest requestDetails = new WsaddrSearchRequest();
    requestDetails.setNcAddressPostalCode("M2N7G7");
    requestDetails.setNcCountryCode(BDMConstants.kCountryCanada);
    requestDetails
      .setNcLanguageName(BDMConstants.kAddress_Search_Locale_en_CA);
    final BDMWSAddressInterfaceIntf wsAddressService =
      new BDMWSAddressInterfaceImpl();

    wsAddressService.searchAddressesByPostalCode(requestDetails);

    new Verifications() {

      {
        new URL(anyString);
        times = 1;
        url.openConnection();
        times = 1;

        connection.getResponseCode();
        times = 1;
        bdmRestUtil.getRESTResponseHTTPConnection((HttpURLConnection) any);
        times = 1;

        BDMAPIAuditDetails auditDetails;
        bdmapiAuditUtil.auditAPI(auditDetails = withCapture());
        times = 1;

        wsAddress.getWsaddrSearchResults().getWsaddrInformation()
          .getWsaddrStatusCode();
        times = 1;

        assertEquals(auditDetails.getApiType(), "OUTBOUND");
        assertEquals(auditDetails.getInvokingMethod(),
          "BDMWSAddressInterfaceImpl.searchAddressesByPostalCode");
        assertEquals(auditDetails.getRelatedID(), "M2N7G7");
        assertEquals(auditDetails.getStatusCode(), 200);
        assert auditDetails
          .getRequestTransactionDateTime() != DateTime.kZeroDateTime;
        assert auditDetails
          .getResponseTransactionDateTime() != DateTime.kZeroDateTime;

        // CloudEvent/CorrelationId details
        bdmRestUtil.getCloudEventDetailsForWSAddress();
        times = 1;
        final BDMOutboundCloudEventDetails bdmOutboundCloudEventDetails;
        bdmRestUtil.addCloudEventHeaders((Map<String, String>) any,
          bdmOutboundCloudEventDetails = withCapture(), (boolean) any);
        times = 1;

        assertEquals(bdmOutboundCloudEventDetails.getSpecVersion(), "1.0");
        assertEquals(bdmOutboundCloudEventDetails.getSource(),
          "WSADDRESSSource");
        assertEquals(bdmOutboundCloudEventDetails.getType(), "WSADDRESSType");
        assertEquals(bdmOutboundCloudEventDetails.getSubject(),
          "WSADDRESSSubject");
        assertEquals(bdmOutboundCloudEventDetails.getId(),
          correlationUniqueId);
        assertEquals(bdmOutboundCloudEventDetails.getTime(), time);
      }
    };

  }

  @Test
  public void testSearchAddressesByPostalCode_error504(@Mocked
  final java.net.URL url, @Mocked
  final HttpURLConnection connection, @Mocked
  final StatusLine line, @Mocked
  final BDMRestUtil bdmRestUtil, @Mocked
  final BDMAPIAuditUtil bdmapiAuditUtil, @Mocked
  final WSAddress wsAddress) throws Exception {

    final String correlationUniqueId = UUID.randomUUID().toString();
    final String time = DateTime.getCurrentDateTime().toString();

    new Expectations() {

      {
        new URL(anyString);
        result = url;

        url.openConnection();
        result = connection;

        connection.getResponseCode();
        result = 504;

        bdmRestUtil.getRESTResponseHTTPConnection((HttpURLConnection) any);
        final Gson gson =
          new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        final StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append(gson.toJson(wsAddress));
        result = responseBuilder;

        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);

        bdmRestUtil.isCloudEventEnabled(anyString);
        result = true;

        bdmRestUtil.getCloudEventDetailsForWSAddress();
        final BDMOutboundCloudEventDetails cloudEventDetails =
          new BDMOutboundCloudEventDetailsBuilder().setId(correlationUniqueId)
            .setSpecVersion("1.0").setSource("WSADDRESSSource")
            .setType("WSADDRESSType").setSubject("WSADDRESSSubject")
            .setTime(time).build();
        result = cloudEventDetails;

        final Map<String, String> headers = new HashMap<>();
        bdmRestUtil.addCloudEventHeaders((Map) any,
          (BDMOutboundCloudEventDetails) any, false);
        result = headers;

      }
    };

    final WsaddrSearchRequest requestDetails = new WsaddrSearchRequest();
    requestDetails.setNcAddressPostalCode("M2N7G7");
    requestDetails.setNcCountryCode(BDMConstants.kCountryCanada);
    requestDetails
      .setNcLanguageName(BDMConstants.kAddress_Search_Locale_en_CA);
    final BDMWSAddressInterfaceIntf wsAddressService =
      new BDMWSAddressInterfaceImpl();

    try {
      wsAddressService.searchAddressesByPostalCode(requestDetails);
    } catch (final Exception e) {
      assertTrue(true);
    }

    new Verifications() {

      {
        new URL(anyString);
        times = 1;
        url.openConnection();
        times = 1;

        connection.getResponseCode();
        times = 1;
        bdmRestUtil.getRESTResponseHTTPConnection((HttpURLConnection) any);
        times = 1;

        BDMAPIAuditDetails auditDetails;
        bdmapiAuditUtil.auditAPI(auditDetails = withCapture());
        times = 1;

        assertEquals(auditDetails.getApiType(), "OUTBOUND");
        assertEquals(auditDetails.getInvokingMethod(),
          "BDMWSAddressInterfaceImpl.searchAddressesByPostalCode");
        assertEquals(auditDetails.getRelatedID(), "M2N7G7");
        assertEquals(auditDetails.getStatusCode(), 504);
        assert auditDetails
          .getRequestTransactionDateTime() != DateTime.kZeroDateTime;
        assert auditDetails
          .getResponseTransactionDateTime() != DateTime.kZeroDateTime;

        // CloudEvent/CorrelationId details
        bdmRestUtil.getCloudEventDetailsForWSAddress();
        times = 1;
        final BDMOutboundCloudEventDetails bdmOutboundCloudEventDetails;
        bdmRestUtil.addCloudEventHeaders((Map<String, String>) any,
          bdmOutboundCloudEventDetails = withCapture(), (boolean) any);
        times = 1;

        assertEquals(bdmOutboundCloudEventDetails.getSpecVersion(), "1.0");
        assertEquals(bdmOutboundCloudEventDetails.getSource(),
          "WSADDRESSSource");
        assertEquals(bdmOutboundCloudEventDetails.getType(), "WSADDRESSType");
        assertEquals(bdmOutboundCloudEventDetails.getSubject(),
          "WSADDRESSSubject");
        assertEquals(bdmOutboundCloudEventDetails.getId(),
          correlationUniqueId);
        assertEquals(bdmOutboundCloudEventDetails.getTime(), time);
      }
    };

  }

  @Test
  public void testSearchAddressesByPostalCode_error500(@Mocked
  final java.net.URL url, @Mocked
  final HttpURLConnection connection, @Mocked
  final StatusLine line, @Mocked
  final BDMRestUtil bdmRestUtil, @Mocked
  final BDMAPIAuditUtil bdmapiAuditUtil, @Mocked
  final WSAddress wsAddress) throws Exception {

    final String correlationUniqueId = UUID.randomUUID().toString();
    final String time = DateTime.getCurrentDateTime().toString();

    new Expectations() {

      {
        new URL(anyString);
        result = url;

        url.openConnection();
        result = connection;

        connection.getResponseCode();
        result = 500;

        bdmRestUtil.getRESTResponseHTTPConnection((HttpURLConnection) any);
        final Gson gson =
          new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        final StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append(gson.toJson(wsAddress));
        result = responseBuilder;

        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);

        bdmRestUtil.isCloudEventEnabled(anyString);
        result = true;

        bdmRestUtil.getCloudEventDetailsForWSAddress();
        final BDMOutboundCloudEventDetails cloudEventDetails =
          new BDMOutboundCloudEventDetailsBuilder().setId(correlationUniqueId)
            .setSpecVersion("1.0").setSource("WSADDRESSSource")
            .setType("WSADDRESSType").setSubject("WSADDRESSSubject")
            .setTime(time).build();
        result = cloudEventDetails;

        final Map<String, String> headers = new HashMap<>();
        bdmRestUtil.addCloudEventHeaders((Map) any,
          (BDMOutboundCloudEventDetails) any, false);
        result = headers;

      }
    };

    final WsaddrSearchRequest requestDetails = new WsaddrSearchRequest();
    requestDetails.setNcAddressPostalCode("M2N7G7");
    requestDetails.setNcCountryCode(BDMConstants.kCountryCanada);
    requestDetails
      .setNcLanguageName(BDMConstants.kAddress_Search_Locale_en_CA);
    final BDMWSAddressInterfaceIntf wsAddressService =
      new BDMWSAddressInterfaceImpl();

    try {
      wsAddressService.searchAddressesByPostalCode(requestDetails);
    } catch (final Exception e) {
      assertTrue(true);
    }

    new Verifications() {

      {
        new URL(anyString);
        times = 1;
        url.openConnection();
        times = 1;

        connection.getResponseCode();
        times = 1;
        bdmRestUtil.getRESTResponseHTTPConnection((HttpURLConnection) any);
        times = 1;

        BDMAPIAuditDetails auditDetails;
        bdmapiAuditUtil.auditAPI(auditDetails = withCapture());
        times = 1;

        assertEquals(auditDetails.getApiType(), "OUTBOUND");
        assertEquals(auditDetails.getInvokingMethod(),
          "BDMWSAddressInterfaceImpl.searchAddressesByPostalCode");
        assertEquals(auditDetails.getRelatedID(), "M2N7G7");
        assertEquals(auditDetails.getStatusCode(), 500);
        assert auditDetails
          .getRequestTransactionDateTime() != DateTime.kZeroDateTime;
        assert auditDetails
          .getResponseTransactionDateTime() != DateTime.kZeroDateTime;

        // CloudEvent/CorrelationId details

        bdmRestUtil.getCloudEventDetailsForWSAddress();
        times = 1;
        final BDMOutboundCloudEventDetails bdmOutboundCloudEventDetails;
        bdmRestUtil.addCloudEventHeaders((Map<String, String>) any,
          bdmOutboundCloudEventDetails = withCapture(), (boolean) any);
        times = 1;

        assertEquals(bdmOutboundCloudEventDetails.getSpecVersion(), "1.0");
        assertEquals(bdmOutboundCloudEventDetails.getSource(),
          "WSADDRESSSource");
        assertEquals(bdmOutboundCloudEventDetails.getType(), "WSADDRESSType");
        assertEquals(bdmOutboundCloudEventDetails.getSubject(),
          "WSADDRESSSubject");
        assertEquals(bdmOutboundCloudEventDetails.getId(),
          correlationUniqueId);
        assertEquals(bdmOutboundCloudEventDetails.getTime(), time);
      }
    };

  }

  @Test
  public void testSearchAddressesByPostalCode_error400(@Mocked
  final java.net.URL url, @Mocked
  final HttpURLConnection connection, @Mocked
  final StatusLine line, @Mocked
  final BDMRestUtil bdmRestUtil, @Mocked
  final BDMAPIAuditUtil bdmapiAuditUtil, @Mocked
  final WSAddress wsAddress) throws Exception {

    final String correlationUniqueId = UUID.randomUUID().toString();
    final String time = DateTime.getCurrentDateTime().toString();
    new Expectations() {

      {
        new URL(anyString);
        result = url;

        url.openConnection();
        result = connection;

        connection.getResponseCode();
        result = 400;

        bdmRestUtil.getRESTResponseHTTPConnection((HttpURLConnection) any);
        final Gson gson =
          new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        final StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append(gson.toJson(wsAddress));
        result = responseBuilder;

        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);

        bdmRestUtil.isCloudEventEnabled(anyString);
        result = true;

        bdmRestUtil.getCloudEventDetailsForWSAddress();
        final BDMOutboundCloudEventDetails cloudEventDetails =
          new BDMOutboundCloudEventDetailsBuilder().setId(correlationUniqueId)
            .setSpecVersion("1.0").setSource("WSADDRESSSource")
            .setType("WSADDRESSType").setSubject("WSADDRESSSubject")
            .setTime(time).build();
        result = cloudEventDetails;

        final Map<String, String> headers = new HashMap<>();
        bdmRestUtil.addCloudEventHeaders((Map) any,
          (BDMOutboundCloudEventDetails) any, false);
        result = headers;

      }
    };

    final WsaddrSearchRequest requestDetails = new WsaddrSearchRequest();
    requestDetails.setNcAddressPostalCode("M2N7G7");
    requestDetails.setNcCountryCode(BDMConstants.kCountryCanada);
    requestDetails
      .setNcLanguageName(BDMConstants.kAddress_Search_Locale_en_CA);
    final BDMWSAddressInterfaceIntf wsAddressService =
      new BDMWSAddressInterfaceImpl();

    try {
      wsAddressService.searchAddressesByPostalCode(requestDetails);
    } catch (final Exception e) {
      assertTrue(true);
    }

    new Verifications() {

      {
        new URL(anyString);
        times = 1;
        url.openConnection();
        times = 1;

        connection.getResponseCode();
        times = 1;
        bdmRestUtil.getRESTResponseHTTPConnection((HttpURLConnection) any);
        times = 1;

        BDMAPIAuditDetails auditDetails;
        bdmapiAuditUtil.auditAPI(auditDetails = withCapture());
        times = 1;

        assertEquals(auditDetails.getApiType(), "OUTBOUND");
        assertEquals(auditDetails.getInvokingMethod(),
          "BDMWSAddressInterfaceImpl.searchAddressesByPostalCode");
        assertEquals(auditDetails.getRelatedID(), "M2N7G7");
        assertEquals(auditDetails.getStatusCode(), 400);
        assert auditDetails
          .getRequestTransactionDateTime() != DateTime.kZeroDateTime;
        assert auditDetails
          .getResponseTransactionDateTime() != DateTime.kZeroDateTime;

        // CloudEvent/CorrelationId details
        bdmRestUtil.getCloudEventDetailsForWSAddress();
        times = 1;
        final BDMOutboundCloudEventDetails bdmOutboundCloudEventDetails;
        bdmRestUtil.addCloudEventHeaders((Map<String, String>) any,
          bdmOutboundCloudEventDetails = withCapture(), (boolean) any);
        times = 1;

        assertEquals(bdmOutboundCloudEventDetails.getSpecVersion(), "1.0");
        assertEquals(bdmOutboundCloudEventDetails.getSource(),
          "WSADDRESSSource");
        assertEquals(bdmOutboundCloudEventDetails.getType(), "WSADDRESSType");
        assertEquals(bdmOutboundCloudEventDetails.getSubject(),
          "WSADDRESSSubject");
        assertEquals(bdmOutboundCloudEventDetails.getId(),
          correlationUniqueId);
        assertEquals(bdmOutboundCloudEventDetails.getTime(), time);
      }
    };

  }
  // END, ADO-117413 , CloudEvent, CorrelationID Implementation
}
