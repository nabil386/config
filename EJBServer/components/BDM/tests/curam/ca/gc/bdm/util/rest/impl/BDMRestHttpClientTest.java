package curam.ca.gc.bdm.util.rest.impl;

import curam.ca.gc.bdm.impl.BDMInterfaceConstants;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.util.rest.impl.stubs.HttpClientHttpExceptionStub;
import curam.ca.gc.bdm.util.rest.impl.stubs.HttpClientIOExceptionStub;
import curam.ca.gc.bdm.util.rest.impl.stubs.HttpClientStub;
import curam.core.impl.EnvVars;
import curam.util.resources.Configuration;
import java.io.IOException;
import org.apache.commons.httpclient.HttpMethod;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

/**
 * Suite of tests for the BDMRestHttpClient class.
 */
public class BDMRestHttpClientTest extends CuramServerTestJUnit4 {

  public static BDMRestHttpClient stubbedTestSubject;

  public BDMRestRequest testRequest;

  /**
   * Set up method for the BDMRestHttpClientTest class.
   */
  @BeforeClass
  public static void setUpClass() {

    stubbedTestSubject = new BDMRestHttpClient((short) 1);
    stubbedTestSubject = new BDMRestHttpClient(new HttpClientStub()) {

      @Override
      public BDMRestResponse makeRequest(final HttpMethod httpMethod,
        final BDMRestRequest request) throws IOException {

        return new BDMRestResponse();
      }
    };
    stubbedTestSubject.setTimeout(100);
    stubbedTestSubject.setCallAndSocketTimeouts(100, 100);
    stubbedTestSubject.noOfTimesToRetry((short) 15);
  }

  /**
   * Set up function for tests.
   */
  @Before
  public void setUp() {

    testRequest = new BDMRestRequest();
  }

  /**
   * Test for callAPI() which results in an IOException.
   *
   * @throws Exception
   */
  @Test
  public void testCallAPI_getMethodIOException() throws Exception {

    testRequest.setMethod(BDMRestMethod.GET);

    // Use a stubbed version of HttpClient that will throw an IOException at
    // executeMethod(), check that this is not intercepted and repackaged as
    // anything else.
    assertThrows(IOException.class,
      () -> new BDMRestHttpClient(new HttpClientIOExceptionStub())
        .callAPI(testRequest));
  }

  /**
   * Test for callAPI() which results in an HttpException from executeMethod.
   *
   * @throws Exception
   */
  @Test
  public void testCallAPI_getMethodHttpException() throws Exception {

    testRequest.setMethod(BDMRestMethod.GET);

    // Use a stubbed version of HttpClient that will throw an HttpException at
    // executeMethod() and check that this is returned as an IOException
    assertThrows(IOException.class,
      () -> new BDMRestHttpClient(new HttpClientHttpExceptionStub())
        .callAPI(testRequest));
  }

  /**
   * Test for callAPI() for a GET method. This is a valid request to the CCT
   * endpoint /getfoldertree.
   *
   * @throws Exception
   */
  @Test
  public void testCallAPI_getMethod() throws Exception {

    // Create valid GET request for CCT /GetFolderTree
    testRequest.setMethod(BDMRestMethod.GET);
    testRequest.setEndpointURI(
      Configuration.getProperty(EnvVars.BDM_CCT_GET_FOLDER_TREE_URL));
    testRequest.addQueryParam(
      BDMInterfaceConstants.BDM_CCT_USERID_QUERY_PARAM,
      Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    testRequest.addQueryParam(
      BDMInterfaceConstants.BDM_CCT_COMMUNITY_QUERY_PARAM,
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    testRequest.addQueryParam("includetemplates", "true");
    testRequest.addQueryParam("includetemplatefields", "true");
    testRequest.addHeader(
      BDMInterfaceConstants.BDM_APIM_SUBSCRIPTION_HEADER_KEY,
      BDMRestUtil.decryptKeys(
        Configuration.getProperty(EnvVars.BDM_OCP_APIM_SUBSCRIPTION_KEY)));

    final BDMRestResponse result =
      new BDMRestHttpClient().callAPI(testRequest);
    assertNotNull(result);
  }

  /**
   * Test for callAPI() for a GET method that will return a response other than
   * HTTP 200.
   *
   * @throws Exception
   */
  @Test
  public void testCallAPI_getMethodFailure() throws Exception {

    // Create GET request for CCT GetFolderTree w/ incorrect community query
    // parameter
    testRequest.setMethod(BDMRestMethod.GET);
    testRequest.setBody("test");
    testRequest.setEndpointURI(
      Configuration.getProperty(EnvVars.BDM_CCT_GET_FOLDER_TREE_URL));
    testRequest.addQueryParam(
      BDMInterfaceConstants.BDM_CCT_USERID_QUERY_PARAM,
      Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    testRequest.addQueryParam(
      BDMInterfaceConstants.BDM_CCT_COMMUNITY_QUERY_PARAM, "test");
    testRequest.addQueryParam("includetemplates", "true");
    testRequest.addQueryParam("includetemplatefields", "true");
    testRequest.addHeader(
      BDMInterfaceConstants.BDM_APIM_SUBSCRIPTION_HEADER_KEY,
      BDMRestUtil.decryptKeys(
        Configuration.getProperty(EnvVars.BDM_OCP_APIM_SUBSCRIPTION_KEY)));

    final BDMRestResponse result =
      new BDMRestHttpClient().callAPI(testRequest);
    assertNotNull(result);
  }

  /**
   * Test for callAPI() for a POST method.
   *
   * @throws Exception
   */
  @Test
  public void testCallAPI_postMethod() throws Exception {

    testRequest.setMethod(BDMRestMethod.POST);
    testRequest.addHeader("test-header", "test-value");
    testRequest.addQueryParam("test-param", "test-value");
    testRequest.setBody("test");

    final BDMRestResponse result = stubbedTestSubject.callAPI(testRequest);

    assertNotNull(result);
  }

  /**
   * Test for callAPI() for a POST method.
   *
   * @throws Exception
   */
  @Test
  public void testCallAPI_postMethodNoHeadersOrBody() throws Exception {

    testRequest.setMethod(BDMRestMethod.POST);
    testRequest.setBody("");

    final BDMRestResponse result = stubbedTestSubject.callAPI(testRequest);

    assertNotNull(result);
  }

  /**
   * Test for callAPI() for a PUT method.
   *
   * @throws Exception
   */
  @Test
  public void testCallAPI_putMethod() throws Exception {

    testRequest.setMethod(BDMRestMethod.PUT);
    testRequest.addHeader("test-header", "test-value");
    testRequest.addQueryParam("test-param", "test-value");
    testRequest.setBody("test");

    final BDMRestResponse result = stubbedTestSubject.callAPI(testRequest);

    assertNotNull(result);
  }

  /**
   * Test for callAPI() for a PUT method.
   *
   * @throws Exception
   */
  @Test
  public void testCallAPI_putMethodNoHeadersOrBody() throws Exception {

    testRequest.setMethod(BDMRestMethod.PUT);

    final BDMRestResponse result = stubbedTestSubject.callAPI(testRequest);

    assertNotNull(result);
  }

  /**
   * Test for callAPI() for a DELETE method.
   *
   * @throws Exception
   */
  @Test
  public void testCallAPI_deleteMethod() throws Exception {

    testRequest.setMethod(BDMRestMethod.DELETE);
    testRequest.addHeader("test-header", "test-value");
    testRequest.addQueryParam("test-param", "test-value");
    testRequest.setBody("test");

    final BDMRestResponse result = stubbedTestSubject.callAPI(testRequest);

    assertNotNull(result);
  }

  /**
   * Test for callAPI() for a DELETE method.
   *
   * @throws Exception
   */
  @Test
  public void testCallAPI_deleteMethodNoHeadersAndBody() throws Exception {

    testRequest.setMethod(BDMRestMethod.DELETE);

    final BDMRestResponse result = stubbedTestSubject.callAPI(testRequest);

    assertNotNull(result);
  }

  /**
   * Test for callAPI() when the HttpMethod passed in is null.
   *
   * @throws Exception
   */
  @Test
  public void testCallAPI_nullMethod() throws Exception {

    testRequest.setMethod(null);
    assertThrows(IOException.class,
      () -> stubbedTestSubject.callAPI(testRequest));
  }

  /**
   * Test for callAPI() when the HttpMethod passed in is PATCH.
   *
   * @throws Exception
   */
  @Test
  public void testCallAPI_patchMethod() throws Exception {

    testRequest.setMethod(BDMRestMethod.PATCH);
    assertThrows(IOException.class,
      () -> stubbedTestSubject.callAPI(testRequest));
  }

}
