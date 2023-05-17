package curam.ca.gc.bdm.util.rest.impl.stubs;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;

/**
 * Stub for HttpClient which throws an HttpException.
 */
public class HttpClientHttpExceptionStub extends HttpClient {

  /**
   * Override executeMethod() and throw an HttpExecution.
   *
   * method - HTTPMethod that is executed on
   */
  @Override
  public int executeMethod(final HttpMethod method) throws HttpException {

    throw new HttpException();
  }

}
