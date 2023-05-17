package curam.ca.gc.bdm.util.rest.impl;

import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.httpclient.StatusLine;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Suite of tests for BDMRestResponse class.
 */
public class BDMRestResponseTest extends CuramServerTestJUnit4 {

  /**
   * Test for setHeaders() method.
   *
   * @throws Exception
   */
  @Test
  public void testSetHeaders() throws Exception {

    final BDMRestResponse testSubject = new BDMRestResponse();
    final Map<String, String> headers = new HashMap<>();
    headers.put("header", "test-value");

    testSubject.setHeaders(headers);

    final Map<String, String> result = testSubject.getHeaders();
    assertEquals(1, result.size());
    assertEquals("test-value", result.get("header"));
  }

  /**
   * Test for setStatusLine() method.
   *
   * @throws Exception
   */
  @Test
  public void testSetStatusLine() throws Exception {

    final BDMRestResponse testSubject = new BDMRestResponse();
    final StatusLine sl = new StatusLine("HTTP/1.1 200 OK");

    testSubject.setStatusLine(sl);

    final StatusLine result = testSubject.getStatusLine();
    assertEquals("HTTP/1.1", result.getHttpVersion());
    assertEquals("OK", result.getReasonPhrase());
    assertEquals(200, result.getStatusCode());
  }

  /**
   * Test for the setBody() method.
   *
   * @throws Exception
   */
  @Test
  public void testSetBody() throws Exception {

    final BDMRestResponse testSubject = new BDMRestResponse();

    testSubject.setBody("{test: success}");

    assertEquals("{test: success}", testSubject.getBody());
  }

  /**
   * Test for the setBodyAsInputStream() method.
   *
   * @throws Exception
   */
  @Test
  public void testSetBodyAsInputStream() throws Exception {

    final BDMRestResponse testSubject = new BDMRestResponse();

    testSubject.setBodyAsInputStream(
      new ByteArrayInputStream("{test: success}".getBytes()));

    final InputStream inputStream = testSubject.getBodyAsInputStream();
    final String result =
      new BufferedReader(new InputStreamReader(inputStream)).lines()
        .collect(Collectors.joining(""));

    assertEquals("{test: success}", result);
  }

  /**
   * Test for the reset() method.
   *
   * @throws Exception
   */
  @Test
  public void testReset() throws Exception {

    final BDMRestResponse testSubject = new BDMRestResponse();
    final Map<String, String> headers = new HashMap<>();
    headers.put("header", "test-value");

    testSubject.setHeaders(headers);
    testSubject.setStatusLine(new StatusLine("HTTP/1.1 200 OK"));
    testSubject.setBody("{test: success}");
    testSubject.setBodyAsInputStream(
      new ByteArrayInputStream("{test: success}".getBytes()));

    testSubject.reset();

    assertEquals(null, testSubject.getStatusLine());
    assertEquals("", testSubject.getBody());
    assertEquals(null, testSubject.getHeaders());
    assertEquals(null, testSubject.getBodyAsInputStream());
  }
}
