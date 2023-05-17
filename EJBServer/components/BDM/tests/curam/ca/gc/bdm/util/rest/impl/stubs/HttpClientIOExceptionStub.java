package curam.ca.gc.bdm.util.rest.impl.stubs;

import java.io.IOException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;

/**
 * Stub for HttpClient which throws an IOException.
 */
public class HttpClientIOExceptionStub extends HttpClient {

  /**
   * Override executeMethod() and throw an IOException.
   *
   * method - HTTPMethod that is executed on
   */
  @Override
  public int executeMethod(final HttpMethod method) throws IOException {

    throw new IOException();
  }
}
