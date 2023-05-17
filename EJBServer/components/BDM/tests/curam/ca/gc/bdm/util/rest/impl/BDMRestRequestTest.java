package curam.ca.gc.bdm.util.rest.impl;

import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Suite of tests for BDMRestRequest class.
 */
public class BDMRestRequestTest extends CuramServerTestJUnit4 {

  /**
   * Test for the addQueryParam() method.
   *
   * @throws Exception
   */
  @Test
  public void testAddQueryParam() throws Exception {

    final BDMRestRequest testSubject = new BDMRestRequest();
    testSubject.addQueryParam("test-param", "test-value");

    final Map<String, String> queryParams = testSubject.getQueryParams();
    assertEquals(queryParams.size(), 1);
    assertEquals(queryParams.get("test-param"), "test-value");
  }

  /**
   * Test for the addHeader() method.
   *
   * @throws Exception
   */
  @Test
  public void testAddHeader() throws Exception {

    final BDMRestRequest testSubject = new BDMRestRequest();
    testSubject.addHeader("test-header", "test-value");

    final Map<String, String> headers = testSubject.getHeaders();
    assertEquals(headers.size(), 1);
    assertEquals(headers.get("test-header"), "test-value");
  }

  /**
   * Test for the setCharset() method.
   *
   * @throws Exception
   */
  @Test
  public void testSetCharset() throws Exception {

    final BDMRestRequest testSubject = new BDMRestRequest();
    testSubject.setCharset("UTF-8");

    assertEquals(testSubject.getCharset(), "UTF-8");
  }

  /**
   * Test for the setMethod() method.
   *
   * @throws Exception
   */
  @Test
  public void testSetMethod() throws Exception {

    final BDMRestRequest testSubject = new BDMRestRequest();
    testSubject.setMethod(BDMRestMethod.GET);

    assertEquals(testSubject.getMethod(), BDMRestMethod.GET);
  }

  /**
   * Test for the setEndpointURI() method.
   *
   * @throws Exception
   */
  @Test
  public void testSetEndpointURI() throws Exception {

    final BDMRestRequest testSubject = new BDMRestRequest();
    testSubject.setEndpointURI("http://localhost:8080");

    assertEquals(testSubject.getEndpointURI(), "http://localhost:8080");
  }

  /**
   * Test for the setBody() method.
   *
   * @throws Exception
   */
  @Test
  public void testSetBody() throws Exception {

    final BDMRestRequest testSubject = new BDMRestRequest();
    testSubject.setBody("<body></body>");

    assertEquals(testSubject.getBody(), "<body></body>");
  }

  /**
   * Test for the setContentType() method.
   *
   * @throws Exception
   */
  @Test
  public void testSetContentType() throws Exception {

    final BDMRestRequest testSubject = new BDMRestRequest();
    testSubject.setContentType("application/json");

    assertEquals(testSubject.getContentType(), "application/json");
  }

  /**
   * Test for the reset() method.
   *
   * @throws Exception
   */
  @Test
  public void testReset() throws Exception {

    final BDMRestRequest testSubject = new BDMRestRequest();
    testSubject.setMethod(BDMRestMethod.POST);
    testSubject.setEndpointURI("http://localhost:8080");
    testSubject.setBody("<body></body>");
    testSubject.addHeader("test-header", "test-value");
    testSubject.addQueryParam("test-param", "another-test-value");
    testSubject.setContentType("application/json");

    testSubject.reset();

    assertEquals(testSubject.getBody(), "");
    assertEquals(testSubject.getEndpointURI(), "");
    assertEquals(testSubject.getMethod(), null);
    assertEquals(testSubject.getQueryParams().size(), 0);
    assertEquals(testSubject.getHeaders().size(), 0);
  }

  /**
   * Test for the clearMethod() method.
   *
   * @throws Exception
   */
  @Test
  public void testClearMethod() throws Exception {

    final BDMRestRequest testSubject = new BDMRestRequest();
    testSubject.setMethod(BDMRestMethod.POST);

    testSubject.clearMethod();

    assertEquals(testSubject.getMethod(), null);
  }

  /**
   * Test for the clearEndpointURI() method.
   *
   * @throws Exception
   */
  @Test
  public void testClearEndpointURI() throws Exception {

    final BDMRestRequest testSubject = new BDMRestRequest();
    testSubject.setEndpointURI("http://localhost:8080");

    testSubject.clearEndpointURI();

    assertEquals(testSubject.getEndpointURI(), "");
  }

  /**
   * Test for the clearBody() method.
   *
   * @throws Exception
   */
  @Test
  public void testClearBody() throws Exception {

    final BDMRestRequest testSubject = new BDMRestRequest();
    testSubject.setBody("<body></body>");

    testSubject.clearBody();

    assertEquals(testSubject.getBody(), "");
  }

  /**
   * Test for the clearQueryParams() method.
   *
   * @throws Exception
   */
  @Test
  public void testClearQueryParams() throws Exception {

    final BDMRestRequest testSubject = new BDMRestRequest();
    testSubject.addQueryParam("test-param", "another-test-value");

    testSubject.clearQueryParams();

    assertEquals(testSubject.getQueryParams().size(), 0);
  }

  /**
   * Test for the clearHeaders() method.
   *
   * @throws Exception
   */
  @Test
  public void testClearHeaders() throws Exception {

    final BDMRestRequest testSubject = new BDMRestRequest();
    testSubject.addHeader("test-header", "test-value");

    testSubject.clearHeaders();

    assertEquals(testSubject.getHeaders().size(), 0);
  }

  /**
   * Test for the clearQueryParams() method.
   *
   * @throws Exception
   */
  @Test
  public void testRemoveQueryParam() throws Exception {

    final BDMRestRequest testSubject = new BDMRestRequest();
    testSubject.addQueryParam("test-param", "another-test-value");
    testSubject.addQueryParam("test-param2", "another-test-value2");

    testSubject.removeQueryParam("test-param2");

    assertEquals(1, testSubject.getQueryParams().size());
    assertEquals("Value does not exist", testSubject.getQueryParams()
      .getOrDefault("test-param2", "Value does not exist"));
  }

  /**
   * Test for the clearHeaders() method.
   *
   * @throws Exception
   */
  @Test
  public void testRemoveHeader() throws Exception {

    final BDMRestRequest testSubject = new BDMRestRequest();
    testSubject.addHeader("test-header", "test-value");
    testSubject.addHeader("test-header2", "test-value2");

    testSubject.removeHeader("test-header");

    assertEquals(1, testSubject.getHeaders().size());
    assertEquals("Value does not exist", testSubject.getHeaders()
      .getOrDefault("test-header", "Value does not exist"));
  }
}
