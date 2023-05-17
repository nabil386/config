package curam.ca.gc.bdm.test.interfaces.sl.wssinvalidate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.BDMWSSINValidateInterfaceImpl;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRRestResponse;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRSearchTable;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRValidation;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.WSSearchTableRequest;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.WSSinValidation;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.intf.BDMWSSINValidateInterfaceIntf;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.util.rest.impl.BDMAPIAuditDetails;
import curam.ca.gc.bdm.util.rest.impl.BDMAPIAuditUtil;
import curam.ca.gc.bdm.util.rest.impl.BDMOutboundCloudEventDetails;
import curam.ca.gc.bdm.util.rest.impl.BDMOutboundCloudEventDetails.BDMOutboundCloudEventDetailsBuilder;
import curam.ca.gc.bdm.util.rest.impl.BDMRestUtil;
import curam.core.impl.EnvVars;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.type.DateTime;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
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
 * Unit test for class BDMWSSINValidateInterfaceImpl
 * Success and Failure test
 *
 *
 * @author Raghu
 */
public class BDMWSSINValidateInterfaceImplTest extends CuramServerTestJUnit4 {

  /**
   * Instantiates a new BDM address test.
   */
  public BDMWSSINValidateInterfaceImplTest() {

    super();

  }

  /**
   * Before.
   */
  @Before
  public void before() {

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

    Configuration.setProperty(EnvVars.ENV_REQUESTRESPONSE_AUDIT_ENABLED,
      "true");
  }

  /**
   * Success Response test
   *
   * @throws IOException
   * @throws AppException
   * @throws InformationalException
   * @throws URISyntaxException
   */

  @Test
  public void test_Valid_SIN_Check_Success()
    throws InformationalException, AppException, IOException {

    final BDMSINSIRSearchTable req = new BDMSINSIRSearchTable();

    req.setNcPersonBirthDate("19810909");
    req.setNcPersonGivenName("CHRISTINA");
    req.setNcPersonSurName("JOHNSON");
    req.setPerPersonSINIdentification("991001587");
    req.setSsParentMaidenName("BROWN");

    final BDMWSSINValidateInterfaceIntf wsSinService =
      new BDMWSSINValidateInterfaceImpl();

    final BDMSINSIRRestResponse restResponse =
      wsSinService.validatePersonBySIN(req);

    final boolean isSINResponseValid =
      restResponse.isSINSIRValidatonSuccess();
    if (isSINResponseValid) {
      assertTrue(restResponse.getValidatedSINResults().getSsMatchFlag());
    }
  }

  /**
   * Agent portal and Client portal invoke this service to
   * validate Person SIN/SIR number
   *
   * @throws InformationalException
   * @throws AppException
   * @throws IOException
   */

  @Test
  public void test_Valid_SIN_Check_Failure()
    throws InformationalException, AppException, IOException {

    final WSSearchTableRequest req = new WSSearchTableRequest();

    req.setNcPersonBirthDate("19810909");
    req.setNcPersonGivenName("CHRISTINAl");
    req.setNcPersonSurName("JOHNSON");
    req.setPerPersonSINIdentification("991001587");
    req.setSsParentMaidenName("BROWN");

    final BDMWSSINValidateInterfaceIntf wsSinService =
      new BDMWSSINValidateInterfaceImpl();

    final WSSinValidation restResponse =
      wsSinService.validatePersonBySIN(req);
    assertEquals(null, restResponse.getSsMatchFlag());

  }

  /**
   * 400 Client Side
   * Description: A message to indicate that the server cannot understand the
   * request, because its either incorrect or corrupt.
   *
   * Example: A SIN is entered which fails the MOD-10 check: "123456789"
   *
   * This method is to check "Invalid SIN - failed mod10 check"
   *
   * @throws IOException
   * @throws AppException
   * @throws InformationalException
   * @throws URISyntaxException
   */

  @Test
  public void test_Invalid_SIN_Mod10_Check_Failure()
    throws InformationalException, AppException, IOException {

    final BDMSINSIRSearchTable req = new BDMSINSIRSearchTable();

    req.setNcPersonBirthDate("19901105");
    req.setNcPersonGivenName("Dan");
    req.setNcPersonSurName("Joannisse");
    req.setPerPersonSINIdentification("123456789");
    req.setSsParentMaidenName("Garand");

    final BDMWSSINValidateInterfaceIntf wsSinService =
      new BDMWSSINValidateInterfaceImpl();

    final BDMSINSIRRestResponse restResponse =
      wsSinService.validatePersonBySIN(req);

    final boolean isSINResponseValid =
      restResponse.isSINSIRValidatonSuccess();
    if (isSINResponseValid) {
      assertTrue(restResponse.getValidatedSINResults().getSsMatchFlag());
    } else {
      assertEquals("Invalid SIN - failed mod10 check",
        restResponse.getSinErrorResponseResults().getFaultDetails().get(0));
      assertEquals("SS-4000",
        restResponse.getSinErrorResponseResults().getFaultErrorCode());
    }

  }

  /**
   * 400 Client Side
   * Description: The SIN must be 9 digits in length
   *
   * @throws IOException
   * @throws AppException
   * @throws InformationalException
   * @throws URISyntaxException
   */

  @Test
  public void test_Invalid_SIN_Check_Failure()
    throws InformationalException, AppException, IOException {

    final BDMSINSIRSearchTable req = new BDMSINSIRSearchTable();

    req.setNcPersonBirthDate("19901105");
    req.setNcPersonGivenName("Dan");
    req.setNcPersonSurName("Joannisse");
    req.setPerPersonSINIdentification("NOTASIN");
    req.setSsParentMaidenName("Garand");

    final BDMWSSINValidateInterfaceIntf wsSinService =
      new BDMWSSINValidateInterfaceImpl();

    final BDMSINSIRRestResponse restResponse =
      wsSinService.validatePersonBySIN(req);

    final boolean isSINResponseValid =
      restResponse.isSINSIRValidatonSuccess();
    if (isSINResponseValid) {
      assertTrue(restResponse.getValidatedSINResults().getSsMatchFlag());
    } else {
      assertEquals("The SIN must be 9 digits in length",
        restResponse.getSinErrorResponseResults().getFaultDetails().get(0));
      assertEquals("SS-4000",
        restResponse.getSinErrorResponseResults().getFaultErrorCode());
    }

  }

  // BEGIN, ADO-107574 , Audit enablement for APIs
  // BEGIN, ADO-117413 , CloudEvent, CorrelationID Implementation
  @Test
  public void testValidatePersonBySIN_success(@Mocked
  final java.net.URL url, @Mocked
  final HttpURLConnection connection, @Mocked
  final StatusLine line, @Mocked
  final BDMRestUtil bdmRestUtil, @Mocked
  final BDMAPIAuditUtil bdmapiAuditUtil) throws Exception {

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

        bdmRestUtil.getRESTResponseHTTPConnection((HttpURLConnection) any);
        final BDMSINSIRValidation bdmsinsirValidation =
          new BDMSINSIRValidation();
        final Gson gson =
          new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        final StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append(gson.toJson(bdmsinsirValidation));
        result = responseBuilder;

        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);

        bdmRestUtil.isCloudEventEnabled(anyString);
        result = true;

        bdmRestUtil.getCloudEventDetailsForSINSIR();
        final BDMOutboundCloudEventDetails cloudEventDetails =
          new BDMOutboundCloudEventDetailsBuilder().setId(correlationUniqueId)
            .setSpecVersion("1.0").setSource("SINSIRSource")
            .setType("SINSIRType").setSubject("SINSIRSubject").setTime(time)
            .build();
        result = cloudEventDetails;

        final Map<String, String> headers = new HashMap<>();
        bdmRestUtil.addCloudEventHeaders((Map) any,
          (BDMOutboundCloudEventDetails) any, false);
        result = headers;

        new MockUp<BDMWSSINValidateInterfaceImpl>() {

          @Mock
          public String constructRelatedIdSINSIR(
            final BDMSINSIRSearchTable bdmsinsirSearchTable) {

            return "relatedID";
          }

        };
      }
    };

    final BDMWSSINValidateInterfaceIntf wsSinService =
      new BDMWSSINValidateInterfaceImpl();

    final BDMSINSIRSearchTable req = new BDMSINSIRSearchTable();

    req.setNcPersonBirthDate("19810909");
    req.setNcPersonGivenName("RANDOMG");
    req.setNcPersonSurName("RANDOMS");
    req.setPerPersonSINIdentification("999111555");
    req.setSsParentMaidenName("RANDOMM");

    final BDMSINSIRRestResponse validateSINSIRRestResponse =
      wsSinService.validatePersonBySIN(req);

    final boolean isSINResponseValid =
      validateSINSIRRestResponse.isSINSIRValidatonSuccess();
    assertTrue(isSINResponseValid);

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
          "BDMWSSINValidateInterfaceImpl.validatePersonBySIN");
        assertEquals(auditDetails.getRelatedID(), "relatedID");
        assertEquals(auditDetails.getStatusCode(), 200);
        assert auditDetails
          .getRequestTransactionDateTime() != DateTime.kZeroDateTime;
        assert auditDetails
          .getResponseTransactionDateTime() != DateTime.kZeroDateTime;

        // CloudEvent/CorrelationId details
        bdmRestUtil.getCloudEventDetailsForSINSIR();
        times = 1;
        final BDMOutboundCloudEventDetails bdmOutboundCloudEventDetails;
        bdmRestUtil.addCloudEventHeaders((Map<String, String>) any,
          bdmOutboundCloudEventDetails = withCapture(), (boolean) any);
        times = 1;

        assertEquals(bdmOutboundCloudEventDetails.getSpecVersion(), "1.0");
        assertEquals(bdmOutboundCloudEventDetails.getSource(),
          "SINSIRSource");
        assertEquals(bdmOutboundCloudEventDetails.getType(), "SINSIRType");
        assertEquals(bdmOutboundCloudEventDetails.getSubject(),
          "SINSIRSubject");
        assertEquals(bdmOutboundCloudEventDetails.getId(),
          correlationUniqueId);
        assertEquals(bdmOutboundCloudEventDetails.getTime(), time);
      }
    };

  }

  @Test
  public void testValidatePersonBySIN_error400(@Mocked
  final java.net.URL url, @Mocked
  final HttpURLConnection connection, @Mocked
  final StatusLine line, @Mocked
  final BDMRestUtil bdmRestUtil, @Mocked
  final BDMAPIAuditUtil bdmapiAuditUtil) throws Exception {

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
        final BDMSINSIRValidation bdmsinsirValidation =
          new BDMSINSIRValidation();
        final Gson gson =
          new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        final StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append(gson.toJson(bdmsinsirValidation));
        result = responseBuilder;

        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);

        bdmRestUtil.isCloudEventEnabled(anyString);
        result = true;

        bdmRestUtil.getCloudEventDetailsForSINSIR();
        final BDMOutboundCloudEventDetails cloudEventDetails =
          new BDMOutboundCloudEventDetailsBuilder().setId(correlationUniqueId)
            .setSpecVersion("1.0").setSource("SINSIRSource")
            .setType("SINSIRType").setSubject("SINSIRSubject").setTime(time)
            .build();
        result = cloudEventDetails;

        final Map<String, String> headers = new HashMap<>();
        bdmRestUtil.addCloudEventHeaders((Map) any,
          (BDMOutboundCloudEventDetails) any, false);
        result = headers;

        new MockUp<BDMWSSINValidateInterfaceImpl>() {

          @Mock
          public String constructRelatedIdSINSIR(
            final BDMSINSIRSearchTable bdmsinsirSearchTable) {

            return "relatedID";
          }

        };
      }
    };

    final BDMWSSINValidateInterfaceIntf wsSinService =
      new BDMWSSINValidateInterfaceImpl();

    final BDMSINSIRSearchTable req = new BDMSINSIRSearchTable();

    req.setNcPersonBirthDate("19810909");
    req.setNcPersonGivenName("RANDOMG");
    req.setNcPersonSurName("RANDOMS");
    req.setPerPersonSINIdentification("999111555");
    req.setSsParentMaidenName("RANDOMM");

    final BDMSINSIRRestResponse validateSINSIRRestResponse =
      wsSinService.validatePersonBySIN(req);

    final boolean isSINResponseValid =
      validateSINSIRRestResponse.isSINSIRValidatonSuccess();
    assertEquals(isSINResponseValid, false);

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
          "BDMWSSINValidateInterfaceImpl.validatePersonBySIN");
        assertEquals(auditDetails.getRelatedID(), "relatedID");

        assertEquals(auditDetails.getStatusCode(), 400);
        assert auditDetails
          .getRequestTransactionDateTime() != DateTime.kZeroDateTime;
        assert auditDetails
          .getResponseTransactionDateTime() != DateTime.kZeroDateTime;

        // CloudEvent/CorrelationId details
        bdmRestUtil.getCloudEventDetailsForSINSIR();
        times = 1;
        final BDMOutboundCloudEventDetails bdmOutboundCloudEventDetails;
        bdmRestUtil.addCloudEventHeaders((Map<String, String>) any,
          bdmOutboundCloudEventDetails = withCapture(), (boolean) any);
        times = 1;

        assertEquals(bdmOutboundCloudEventDetails.getSpecVersion(), "1.0");
        assertEquals(bdmOutboundCloudEventDetails.getSource(),
          "SINSIRSource");
        assertEquals(bdmOutboundCloudEventDetails.getType(), "SINSIRType");
        assertEquals(bdmOutboundCloudEventDetails.getSubject(),
          "SINSIRSubject");
        assertEquals(bdmOutboundCloudEventDetails.getId(),
          correlationUniqueId);
        assertEquals(bdmOutboundCloudEventDetails.getTime(), time);
      }
    };

  }

  @Test
  public void testValidatePersonBySIN_error404(@Mocked
  final java.net.URL url, @Mocked
  final HttpURLConnection connection, @Mocked
  final StatusLine line, @Mocked
  final BDMRestUtil bdmRestUtil, @Mocked
  final BDMAPIAuditUtil bdmapiAuditUtil) throws Exception {

    final String correlationUniqueId = UUID.randomUUID().toString();
    final String time = DateTime.getCurrentDateTime().toString();

    new Expectations() {

      {
        new URL(anyString);
        result = url;

        url.openConnection();
        result = connection;

        connection.getResponseCode();
        result = 404;

        bdmRestUtil.getRESTResponseHTTPConnection((HttpURLConnection) any);
        final BDMSINSIRValidation bdmsinsirValidation =
          new BDMSINSIRValidation();
        final Gson gson =
          new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        final StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append(gson.toJson(bdmsinsirValidation));
        result = responseBuilder;

        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);

        bdmRestUtil.isCloudEventEnabled(anyString);
        result = true;

        bdmRestUtil.getCloudEventDetailsForSINSIR();
        final BDMOutboundCloudEventDetails cloudEventDetails =
          new BDMOutboundCloudEventDetailsBuilder().setId(correlationUniqueId)
            .setSpecVersion("1.0").setSource("SINSIRSource")
            .setType("SINSIRType").setSubject("SINSIRSubject").setTime(time)
            .build();
        result = cloudEventDetails;

        final Map<String, String> headers = new HashMap<>();
        bdmRestUtil.addCloudEventHeaders((Map) any,
          (BDMOutboundCloudEventDetails) any, false);
        result = headers;

        new MockUp<BDMWSSINValidateInterfaceImpl>() {

          @Mock
          public String constructRelatedIdSINSIR(
            final BDMSINSIRSearchTable bdmsinsirSearchTable) {

            return "relatedID";
          }

        };
      }
    };

    final BDMWSSINValidateInterfaceIntf wsSinService =
      new BDMWSSINValidateInterfaceImpl();

    final BDMSINSIRSearchTable req = new BDMSINSIRSearchTable();

    req.setNcPersonBirthDate("19810909");
    req.setNcPersonGivenName("RANDOMG");
    req.setNcPersonSurName("RANDOMS");
    req.setPerPersonSINIdentification("999111555");
    req.setSsParentMaidenName("RANDOMM");

    final BDMSINSIRRestResponse validateSINSIRRestResponse =
      wsSinService.validatePersonBySIN(req);

    final boolean isSINResponseValid =
      validateSINSIRRestResponse.isSINSIRValidatonSuccess();
    assertEquals(isSINResponseValid, false);

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
          "BDMWSSINValidateInterfaceImpl.validatePersonBySIN");
        assertEquals(auditDetails.getRelatedID(), "relatedID");
        assertEquals(auditDetails.getStatusCode(), 404);
        assert auditDetails
          .getRequestTransactionDateTime() != DateTime.kZeroDateTime;
        assert auditDetails
          .getResponseTransactionDateTime() != DateTime.kZeroDateTime;

        // CloudEvent/CorrelationId details
        bdmRestUtil.getCloudEventDetailsForSINSIR();
        times = 1;
        final BDMOutboundCloudEventDetails bdmOutboundCloudEventDetails;
        bdmRestUtil.addCloudEventHeaders((Map<String, String>) any,
          bdmOutboundCloudEventDetails = withCapture(), (boolean) any);
        times = 1;

        assertEquals(bdmOutboundCloudEventDetails.getSpecVersion(), "1.0");
        assertEquals(bdmOutboundCloudEventDetails.getSource(),
          "SINSIRSource");
        assertEquals(bdmOutboundCloudEventDetails.getType(), "SINSIRType");
        assertEquals(bdmOutboundCloudEventDetails.getSubject(),
          "SINSIRSubject");
        assertEquals(bdmOutboundCloudEventDetails.getId(),
          correlationUniqueId);
        assertEquals(bdmOutboundCloudEventDetails.getTime(), time);
      }
    };

  }

  @Test
  public void testValidatePersonBySIN_error500(@Mocked
  final java.net.URL url, @Mocked
  final HttpURLConnection connection, @Mocked
  final StatusLine line, @Mocked
  final BDMRestUtil bdmRestUtil, @Mocked
  final BDMAPIAuditUtil bdmapiAuditUtil) throws Exception {

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
        final BDMSINSIRValidation bdmsinsirValidation =
          new BDMSINSIRValidation();
        final Gson gson =
          new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        final StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append(gson.toJson(bdmsinsirValidation));
        result = responseBuilder;

        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);

        bdmRestUtil.isCloudEventEnabled(anyString);
        result = true;

        bdmRestUtil.getCloudEventDetailsForSINSIR();
        final BDMOutboundCloudEventDetails cloudEventDetails =
          new BDMOutboundCloudEventDetailsBuilder().setId(correlationUniqueId)
            .setSpecVersion("1.0").setSource("SINSIRSource")
            .setType("SINSIRType").setSubject("SINSIRSubject").setTime(time)
            .build();
        result = cloudEventDetails;

        final Map<String, String> headers = new HashMap<>();
        bdmRestUtil.addCloudEventHeaders((Map) any,
          (BDMOutboundCloudEventDetails) any, false);
        result = headers;

        new MockUp<BDMWSSINValidateInterfaceImpl>() {

          @Mock
          public String constructRelatedIdSINSIR(
            final BDMSINSIRSearchTable bdmsinsirSearchTable) {

            return "relatedID";
          }

        };
      }
    };

    final BDMWSSINValidateInterfaceIntf wsSinService =
      new BDMWSSINValidateInterfaceImpl();

    final BDMSINSIRSearchTable req = new BDMSINSIRSearchTable();

    req.setNcPersonBirthDate("19810909");
    req.setNcPersonGivenName("RANDOMG");
    req.setNcPersonSurName("RANDOMS");
    req.setPerPersonSINIdentification("999111555");
    req.setSsParentMaidenName("RANDOMM");

    final BDMSINSIRRestResponse validateSINSIRRestResponse =
      wsSinService.validatePersonBySIN(req);

    final boolean isSINResponseValid =
      validateSINSIRRestResponse.isSINSIRValidatonSuccess();
    assertEquals(isSINResponseValid, false);

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
          "BDMWSSINValidateInterfaceImpl.validatePersonBySIN");
        assertEquals(auditDetails.getRelatedID(), "relatedID");
        assertEquals(auditDetails.getStatusCode(), 500);
        assert auditDetails
          .getRequestTransactionDateTime() != DateTime.kZeroDateTime;
        assert auditDetails
          .getResponseTransactionDateTime() != DateTime.kZeroDateTime;

        // CloudEvent/CorrelationId details
        bdmRestUtil.getCloudEventDetailsForSINSIR();
        times = 1;
        final BDMOutboundCloudEventDetails bdmOutboundCloudEventDetails;
        bdmRestUtil.addCloudEventHeaders((Map<String, String>) any,
          bdmOutboundCloudEventDetails = withCapture(), (boolean) any);
        times = 1;

        assertEquals(bdmOutboundCloudEventDetails.getSpecVersion(), "1.0");
        assertEquals(bdmOutboundCloudEventDetails.getSource(),
          "SINSIRSource");
        assertEquals(bdmOutboundCloudEventDetails.getType(), "SINSIRType");
        assertEquals(bdmOutboundCloudEventDetails.getSubject(),
          "SINSIRSubject");
        assertEquals(bdmOutboundCloudEventDetails.getId(),
          correlationUniqueId);
        assertEquals(bdmOutboundCloudEventDetails.getTime(), time);
      }
    };

  }
  // END, ADO-107574 , Audit enablement for APIs
  // END, ADO-117413 , CloudEvent, CorrelationID Implementation
}
