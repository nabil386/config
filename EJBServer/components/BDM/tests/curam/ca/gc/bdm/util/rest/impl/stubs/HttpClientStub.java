package curam.ca.gc.bdm.util.rest.impl.stubs;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;

/**
 * Stub for HttpClient to prevent making actual HTTP call.
 */
public class HttpClientStub extends HttpClient {

  /**
   * Override executeMethod() and return value without making HTTP call.
   *
   * method - HTTPMethod that is executed on
   */
  @Override
  public int executeMethod(final HttpMethod method) {

    return 0;
  }
}
