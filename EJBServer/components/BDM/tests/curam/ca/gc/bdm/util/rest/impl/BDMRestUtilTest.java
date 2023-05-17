package curam.ca.gc.bdm.util.rest.impl;

import com.google.gson.JsonSyntaxException;
import curam.ca.gc.bdm.codetable.BDMAUDITAPITYPE;
import curam.ca.gc.bdm.message.BDMBPOCCT;
import curam.ca.gc.bdm.message.BDMRESTAPIERRORMESSAGE;
import curam.ca.gc.bdm.rest.bdmcctapi.struct.BDMCCTWorkItemStatusDetails;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTGetWorkItemStatusResponse;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.util.rest.impl.BDMAPIAuditDetails.BDMAPIAuditDetailsBuilder;
import curam.ca.gc.bdm.util.rest.impl.BDMOutboundCloudEventDetails.BDMOutboundCloudEventDetailsBuilder;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.security.EncryptionUtil;
import curam.util.type.DateTime;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.apache.commons.httpclient.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)

public class BDMRestUtilTest extends CuramServerTestJUnit4 {

  @Tested
  BDMRestUtil bdmRestUtil;

  boolean auditErrorResponse = false;

  @Before
  public void setUp() throws AppException, InformationalException {

    new BDMRestUtil();
  }

  // BEGIN, Task 89081 - API Authentication Encryption for Outbound API
  // processing
  /**
   * Task#89081 API Authentication Encryption for Outbound API processing Junit
   *
   * @throws Exception
   */
  @Test
  public void testDecryptKeys() throws Exception {

    // Set a key value for encryption.
    final String originalValue = "12345abcd";
    final String encryptedValue =
      EncryptionUtil.encryptPassword(originalValue);
    final String decryptedValue = BDMRestUtil.decryptKeys(encryptedValue);
    assertEquals(originalValue, decryptedValue);
  }
  // END, Task 89081 - API Authentication Encryption for Outbound API processing

  /**
   * Test decryptKeys() for an input of less than 2 characters.
   *
   * @throws Exception
   */
  @Test
  public void testDecryptKeys_lessThanTwoCharacters() throws Exception {

    final AppException exception =
      assertThrows(AppException.class, () -> BDMRestUtil.decryptKeys("A"));
    assertEquals(exception.getCatEntry(),
      BDMRESTAPIERRORMESSAGE.BDM_ERR_AUTHENTICATION_KEY_DECRYPTION);
  }

  /**
   * Test for convertToJSON() method.
   *
   * @throws Exception
   */
  @Test
  public void testConvertToJSON() {

    final String result =
      BDMRestUtil.convertToJSON(new BDMCCTGetWorkItemStatusResponse());

    assertEquals(false, result.isEmpty());
    assertEquals(
      "{  \"WorkItemStatusId\": 0,  \"WorkItemStatusName\": null, "
        + " \"WorkItemStatusUrl\": null,  \"WorkItemData\": null}",
      result.replaceAll("\n", ""));
  }

  /**
   * Test for convertFromJSON() method.
   *
   * @throws Exception
   */
  @Test
  public void testConvertFromJSON() {

    final BDMCCTGetWorkItemStatusResponse result =
      BDMRestUtil.convertFromJSON(
        "{\"WorkItemStatusId\": 1, \"WorkItemStatusName\": \"TEST\", "
          + "\"WorkItemStatusUrl\": \"a.com\", \"WorkItemData\": \"test\"}",
        BDMCCTGetWorkItemStatusResponse.class);

    assertEquals("test", result.getWorkItemData());
    assertEquals(1, result.getWorkItemStatusID());
    assertEquals("TEST", result.getWorkItemStatusName());
    assertEquals("a.com", result.getWorkItemStatusURL());

  }

  /**
   * Test for convertFromJSON() method with an invalid JSON string.
   *
   * @throws Exception
   */
  @Test
  public void testConvertFromJSON_invalidString() {

    assertThrows(JsonSyntaxException.class, () -> BDMRestUtil
      .convertFromJSON("{", BDMCCTGetWorkItemStatusResponse.class));

  }

  /**
   * Test for decryptKeys() with an empty string.
   */
  @Test
  public void testDecryptKeys_emptyString() throws AppException {

    final String result = BDMRestUtil.decryptKeys("");

    assertEquals("", result);
  }

  @Test
  public void testThrowHTTP404Status() throws Exception {

    new Expectations() {

      {
        new MockUp<BDMRestUtil>() {

          @Mock
          public void auditErrorResponse(final AppException appException,
            final int statusErrorCode,
            final BDMAPIAuditDetails bdmapiAuditDetails)
            throws AppException, InformationalException {

            assertEquals(statusErrorCode, HttpStatus.SC_BAD_REQUEST);
            assertEquals(appException.getCatEntry().getMessageText(),
              BDMBPOCCT.ERR_HTTP_404_RESOURCE_NOT_FOUND.getMessageText());
            auditErrorResponse = true;
          }

        };

      }

    };
    final String argumentMsg = "";
    final BDMAPIAuditDetails bdmapiAuditDetails =
      new BDMAPIAuditDetailsBuilder()
        .setMethod("BDMCCTAPI.updateWorkItemStatus").build();
    try {
      BDMRestUtil.throwHTTP404Status(argumentMsg, bdmapiAuditDetails);
    } catch (final AppException e) {
      assertTrue(true);
    }

    assertEquals(auditErrorResponse, true);
  }

  @Test
  public void testThrowHTTP400Status() throws Exception {

    new Expectations() {

      {
        new MockUp<BDMRestUtil>() {

          @Mock
          public void auditErrorResponse(final AppException appException,
            final int statusErrorCode,
            final BDMAPIAuditDetails bdmapiAuditDetails)
            throws AppException, InformationalException {

            assertEquals(statusErrorCode, HttpStatus.SC_BAD_REQUEST);
            assertEquals(appException.getCatEntry().getMessageText(),
              BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST.getMessageText());
            auditErrorResponse = true;
          }

        };

      }

    };
    final String argumentMsg = "";
    final BDMAPIAuditDetails bdmapiAuditDetails =
      new BDMAPIAuditDetailsBuilder()
        .setMethod("BDMCCTAPI.updateWorkItemStatus").build();
    try {
      BDMRestUtil.throwHTTP400Status(argumentMsg, bdmapiAuditDetails);
    } catch (final AppException e) {
      assertTrue(true);
    }

    assertEquals(auditErrorResponse, true);
  }

  @Test
  public void testThrowHTTP403Status() throws Exception {

    new Expectations() {

      {
        new MockUp<BDMRestUtil>() {

          @Mock
          public void auditErrorResponse(final AppException appException,
            final int statusErrorCode,
            final BDMAPIAuditDetails bdmapiAuditDetails)
            throws AppException, InformationalException {

            assertEquals(statusErrorCode, HttpStatus.SC_FORBIDDEN);
            assertEquals(appException.getCatEntry().getMessageText(),
              BDMBPOCCT.ERR_HTTP_403_PERMISSION_DENIED.getMessageText());
            auditErrorResponse = true;
          }

        };

      }

    };

    final BDMAPIAuditDetails bdmapiAuditDetails =
      new BDMAPIAuditDetailsBuilder()
        .setMethod("BDMCCTAPI.updateWorkItemStatus").build();
    try {
      BDMRestUtil.throwHTTP403Status(bdmapiAuditDetails);
    } catch (final AppException e) {
      assertTrue(true);
    }

    assertEquals(auditErrorResponse, true);
  }

  @Test
  public void testThrowHTTP412Status() throws Exception {

    new Expectations() {

      {
        new MockUp<BDMRestUtil>() {

          @Mock
          public void auditErrorResponse(final AppException appException,
            final int statusErrorCode,
            final BDMAPIAuditDetails bdmapiAuditDetails)
            throws AppException, InformationalException {

            assertEquals(statusErrorCode, HttpStatus.SC_PRECONDITION_FAILED);
            assertEquals(appException.getCatEntry().getMessageText(),
              BDMBPOCCT.ERR_HTTP_412_PRECONDITION_FAILED.getMessageText());
            auditErrorResponse = true;
          }

        };

      }

    };
    final String argumentMsg = "";
    final BDMAPIAuditDetails bdmapiAuditDetails =
      new BDMAPIAuditDetailsBuilder()
        .setMethod("BDMCCTAPI.updateWorkItemStatus").build();
    try {
      BDMRestUtil.throwHTTP412Status(argumentMsg, bdmapiAuditDetails);
    } catch (final AppException e) {
      assertTrue(true);
    }

    assertEquals(auditErrorResponse, true);
  }

  @Test
  public void testAuditErrorResponse(@Mocked
  final BDMAPIAuditUtil bdmapiAuditUtil) throws Exception {

    new Expectations() {

      {
        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);
      }
    };

    final BDMCCTWorkItemStatusDetails req = new BDMCCTWorkItemStatusDetails();
    final BDMAPIAuditDetails bdmapiAuditDetails =
      new BDMAPIAuditDetailsBuilder()
        .setMethod("BDMCCTAPI.updateWorkItemStatus").setRelatedID("100")
        .setRequestObject(req).setApiType(BDMAUDITAPITYPE.BDMINBOUND)
        .setRequestTransactionDateTime(DateTime.getCurrentDateTime())
        .setSource("SOURCE").setCorrelationID("100").build();

    final AppException appException =
      new AppException(BDMBPOCCT.ERR_HTTP_404_RESOURCE_NOT_FOUND);
    appException.arg("workItemId");

    BDMRestUtil.auditErrorResponse(appException, HttpStatus.SC_BAD_REQUEST,
      bdmapiAuditDetails);

    new Verifications() {

      {
        BDMAPIAuditDetails bdmapiAuditDetails =
          new BDMAPIAuditDetailsBuilder().build();
        bdmapiAuditUtil.auditAPI(bdmapiAuditDetails = withCapture());
        times = 1;

        assertEquals(bdmapiAuditDetails.getInvokingMethod(),
          "BDMCCTAPI.updateWorkItemStatus");
        assertEquals(bdmapiAuditDetails.getRelatedID(), "100");
        assertEquals(bdmapiAuditDetails.getCorrelationID(), "100");
        assertEquals(bdmapiAuditDetails.getApiType(),
          BDMAUDITAPITYPE.BDMINBOUND);
      }
    };
  }

  @Test
  public void testGetRESTResponseHTTPConnection_successResponseCode(@Mocked
  final HttpURLConnection httpURLConnection) throws Exception {

    new Expectations() {

      {
        httpURLConnection.getResponseCode();
        result = HttpStatus.SC_OK;

        httpURLConnection.getInputStream();
        httpURLConnection.getErrorStream();
        minTimes = 0;
      }
    };

    bdmRestUtil.getRESTResponseHTTPConnection(httpURLConnection);

    new Verifications() {

      {
        httpURLConnection.getResponseCode();
        times = 1;

        httpURLConnection.getInputStream();
        times = 1;
        httpURLConnection.getErrorStream();
        times = 0;
      }
    };
  }

  @Test
  public void testGetRESTResponseHTTPConnection_errorResponseCode(@Mocked
  final HttpURLConnection httpURLConnection) throws Exception {

    new Expectations() {

      {
        httpURLConnection.getResponseCode();
        result = HttpStatus.SC_BAD_REQUEST;

        httpURLConnection.getInputStream();
        minTimes = 0;

        httpURLConnection.getErrorStream();

      }
    };

    bdmRestUtil.getRESTResponseHTTPConnection(httpURLConnection);

    new Verifications() {

      {
        httpURLConnection.getResponseCode();
        times = 1;

        httpURLConnection.getInputStream();
        times = 0;
        httpURLConnection.getErrorStream();
        times = 1;
      }
    };
  }

  // BEGIN, ADO-117413 , CloudEvent, CorrelationID Implementation
  @Test
  public void testAddCloudEventHeaders_bdmRequest_API(@Mocked
  final BDMRestRequest bdmRestRequest) {

    new Expectations() {

      {
        bdmRestRequest.addHeader(anyString, anyString);
        bdmRestRequest.setCorrelationID(anyString);
        bdmRestRequest.setSource(anyString);
      }
    };

    final BDMOutboundCloudEventDetails cloudEventDetails =
      new BDMOutboundCloudEventDetailsBuilder().setId("1234-123-123")
        .setSpecVersion("1.0").setSource("source").setType("type")
        .setSubject("subject")
        .setTime(DateTime.getCurrentDateTime().toString()).build();
    bdmRestUtil.addCloudEventHeaders(new BDMRestRequest(), cloudEventDetails,
      false);

    new Verifications() {

      {
        bdmRestRequest.addHeader(anyString, anyString);
        times = 8;
        bdmRestRequest.setCorrelationID(anyString);
        times = 1;
        bdmRestRequest.setSource(anyString);
        times = 1;
      }
    };
  }

  @Test
  public void testAddCloudEventHeaders_bdmRequest_fileAPI(@Mocked
  final BDMRestRequest bdmRestRequest) {

    new Expectations() {

      {
        bdmRestRequest.addHeader(anyString, anyString);
        bdmRestRequest.setCorrelationID(anyString);
        bdmRestRequest.setSource(anyString);
      }
    };

    final BDMOutboundCloudEventDetails cloudEventDetails =
      new BDMOutboundCloudEventDetailsBuilder().setId("1234-123-123")
        .setSpecVersion("1.0").setSource("source").setType("type")
        .setSubject("subject")
        .setTime(DateTime.getCurrentDateTime().toString()).build();
    bdmRestUtil.addCloudEventHeaders(new BDMRestRequest(), cloudEventDetails,
      true);

    new Verifications() {

      {
        bdmRestRequest.addHeader(anyString, anyString);
        times = 12;
        bdmRestRequest.setCorrelationID(anyString);
        times = 1;
        bdmRestRequest.setSource(anyString);
        times = 1;
      }
    };
  }

  @Test
  public void testAddCloudEventHeaders_headerMap_API() {

    final BDMOutboundCloudEventDetails cloudEventDetails =
      new BDMOutboundCloudEventDetailsBuilder().setId("1234-123-123")
        .setSpecVersion("1.0").setSource("source").setType("type")
        .setSubject("subject")
        .setTime(DateTime.getCurrentDateTime().toString()).build();
    final Map<String, String> headerMap = bdmRestUtil.addCloudEventHeaders(
      new HashMap<String, String>(), cloudEventDetails, false);

    assertEquals(headerMap.get("ce-specversion"), "1.0");
    assertEquals(headerMap.get("ce-source"), "source");
    assertEquals(headerMap.get("ce-type"), "type");
    assertEquals(headerMap.get("ce-subject"), "subject");
    assertEquals(headerMap.get("ce-id"), "1234-123-123");

  }

  @Test
  public void testAddCloudEventHeaders_headerMap_fileAPI() {

    final BDMOutboundCloudEventDetails cloudEventDetails =
      new BDMOutboundCloudEventDetailsBuilder().setId("1234-123-123")
        .setSpecVersion("1.0").setSource("source").setType("type")
        .setSubject("subject").setPartitionkey("2.0")
        .setFilesequencenumber("1").setTotalcount("10")
        .setTime(DateTime.getCurrentDateTime().toString()).build();
    final Map<String, String> headerMap = bdmRestUtil.addCloudEventHeaders(
      new HashMap<String, String>(), cloudEventDetails, true);

    assertEquals(headerMap.get("ce-id"), "1234-123-123");
    assertEquals(headerMap.get("ce-specversion"), "1.0");
    assertEquals(headerMap.get("ce-source"), "source");
    assertEquals(headerMap.get("ce-type"), "type");
    assertEquals(headerMap.get("ce-subject"), "subject");
    assertEquals(headerMap.get("ce-partitionkey"), "2.0");
    assertEquals(headerMap.get("ce-filesequencenumber"), "1");
    assertEquals(headerMap.get("ce-totalcount"), "10");

  }

  @Test
  public void testGetCloudEventDetailsForCCT(@Mocked
  final Configuration configuration) {

    new Expectations() {

      {
        Configuration.getProperty(EnvVars.CLOUDEVENT_SPECVERSION_VALUE);
        result = "1.0";
        Configuration.getProperty(EnvVars.CLOUDEVENT_SOURCE_VALUE);
        result = "Test Source CCT";
        Configuration.getProperty(EnvVars.BDM_CCT_CLOUDEVENT_TYPE);
        result = "Test Type CCT";
        Configuration.getProperty(EnvVars.BDM_CCT_CLOUDEVENT_SUBJECT);
        result = "Test Subject CCT";
      }
    };

    final BDMRestRequest request = new BDMRestRequest();

    final BDMOutboundCloudEventDetails cloudEventDetails =
      bdmRestUtil.getCloudEventDetailsForCCT(request);

    assertEquals(cloudEventDetails.getSpecVersion(), "1.0");
    assertEquals(cloudEventDetails.getSource(), "Test Source CCT");
    assertEquals(cloudEventDetails.getType(), "Test Type CCT");
    assertEquals(cloudEventDetails.getSubject(), "Test Subject CCT");
  }

  @Test
  public void testGetCloudEventDetailsForWSAddress(@Mocked
  final Configuration configuration) {

    new Expectations() {

      {
        Configuration.getProperty(EnvVars.CLOUDEVENT_SPECVERSION_VALUE);
        result = "1.0";
        Configuration.getProperty(EnvVars.CLOUDEVENT_SOURCE_VALUE);
        result = "Test Source WSADDRESS";
        Configuration.getProperty(EnvVars.BDM_WSADDRESS_CLOUDEVENT_TYPE);
        result = "Test Type WSADDRESS";
        Configuration.getProperty(EnvVars.BDM_WSADDRESS_CLOUDEVENT_SUBJECT);
        result = "Test Subject WSADDRESS";
      }
    };

    final BDMOutboundCloudEventDetails cloudEventDetails =
      bdmRestUtil.getCloudEventDetailsForWSAddress();

    assertEquals(cloudEventDetails.getSpecVersion(), "1.0");
    assertEquals(cloudEventDetails.getSource(), "Test Source WSADDRESS");
    assertEquals(cloudEventDetails.getType(), "Test Type WSADDRESS");
    assertEquals(cloudEventDetails.getSubject(), "Test Subject WSADDRESS");
  }

  @Test
  public void testGetCloudEventDetailsForSINSIR(@Mocked
  final Configuration configuration) {

    new Expectations() {

      {
        Configuration.getProperty(EnvVars.CLOUDEVENT_SPECVERSION_VALUE);
        result = "1.0";
        Configuration.getProperty(EnvVars.CLOUDEVENT_SOURCE_VALUE);
        result = "Test Source SINSIR";
        Configuration.getProperty(EnvVars.BDM_SINSIR_CLOUDEVENT_TYPE);
        result = "Test Type SINSIR";
        Configuration.getProperty(EnvVars.BDM_SINSIR_CLOUDEVENT_SUBJECT);
        result = "Test Subject SINSIR";
      }
    };

    final BDMOutboundCloudEventDetails cloudEventDetails =
      bdmRestUtil.getCloudEventDetailsForSINSIR();

    assertEquals(cloudEventDetails.getSpecVersion(), "1.0");
    assertEquals(cloudEventDetails.getSource(), "Test Source SINSIR");
    assertEquals(cloudEventDetails.getType(), "Test Type SINSIR");
    assertEquals(cloudEventDetails.getSubject(), "Test Subject SINSIR");
  }

  @Test
  public void testIsCloudEventEnabled_globalPropertyDisabled(@Mocked
  final Configuration configuration) {

    new Expectations() {

      {
        Configuration.getBooleanProperty(EnvVars.ENV_CLOUDEVENT_ENABLED);
        result = false;
      }
    };
    final boolean isCloudEventEnabled =
      bdmRestUtil.isCloudEventEnabled("TEST METHODNAME");

    assertEquals(isCloudEventEnabled, false);
  }

  @Test
  public void
    testIsCloudEventEnabled_globalPropertyEnabled_methodLevelDisabled(@Mocked
  final Configuration configuration) {

    new Expectations() {

      {
        Configuration.getBooleanProperty(EnvVars.ENV_CLOUDEVENT_ENABLED);
        result = true;

        Configuration.getBooleanProperty(EnvVars.ENV_CLOUDEVENT_ENABLED
          + CuramConst.gkDotChar + "TEST METHODNAME");
        result = false;
      }
    };
    final boolean isCloudEventEnabled =
      bdmRestUtil.isCloudEventEnabled("TEST METHODNAME");

    assertEquals(isCloudEventEnabled, false);
  }

  @Test
  public void
    testIsCloudEventEnabled_globalPropertyEnabled_methodLevelEnabled(@Mocked
  final Configuration configuration) {

    new Expectations() {

      {
        Configuration.getBooleanProperty(EnvVars.ENV_CLOUDEVENT_ENABLED);
        result = true;

        Configuration.getBooleanProperty(EnvVars.ENV_CLOUDEVENT_ENABLED
          + CuramConst.gkDotChar + "TEST METHODNAME");
        result = true;
      }
    };
    final boolean isCloudEventEnabled =
      bdmRestUtil.isCloudEventEnabled("TEST METHODNAME");

    assertEquals(isCloudEventEnabled, true);
  }

  @Test
  public void
    testIsCloudEventEnabled_globalPropertyEnabled_methodLevelPropertyNotFound_Disabled(
      @Mocked
      final Configuration configuration) {

    new Expectations() {

      {
        Configuration.getBooleanProperty(EnvVars.ENV_CLOUDEVENT_ENABLED);
        result = true;

        Configuration.getBooleanProperty(EnvVars.ENV_CLOUDEVENT_ENABLED
          + CuramConst.gkDotChar + "TEST METHODNAME1");
        minTimes = 0;
        result = true;
      }
    };
    final boolean isCloudEventEnabled =
      bdmRestUtil.isCloudEventEnabled("TEST METHODNAME");

    assertEquals(isCloudEventEnabled, false);
  }

  // END, ADO-117413 , CloudEvent, CorrelationID Implementation
}
