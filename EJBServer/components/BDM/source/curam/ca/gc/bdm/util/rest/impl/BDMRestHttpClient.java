package curam.ca.gc.bdm.util.rest.impl;

import curam.ca.gc.bdm.impl.BDMInterfaceConstants;
import curam.core.impl.CuramConst;
import curam.ca.gc.bdm.codetable.BDMAUDITAPITYPE;
import curam.core.impl.CuramConst;
import curam.util.exception.AppException;
import curam.util.resources.Trace;
import curam.util.type.DateTime;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import static java.util.stream.Collectors.joining;

/**
 * HTTP client which is used to establish HTTP connections and make requests.
 */
public class BDMRestHttpClient {

  private final HttpClient httpClient;

  /**
   * BDMRestHttpClient constructor which creates a HttpClient.
   */

  public BDMRestHttpClient() {

    this.httpClient = new HttpClient();
    noOfTimesToRetry((short) 1);
  }

  /**
   * BDMRestHttpClient constructor which creates a HttpClient w/ specified number
   * of retries.
   */
  public BDMRestHttpClient(final short numberOfRetries) {

    this.httpClient = new HttpClient();
    noOfTimesToRetry(numberOfRetries);
  }

  /**
   * BDMRestHttpClient constructor with HttpClient dependency passed in.
   *
   * @param httpClient
   */

  public BDMRestHttpClient(final HttpClient httpClient) {

    this.httpClient = httpClient;
  }

  /**
   * Set parameter for number of retries for the HttpClient requests.
   *
   * @param number
   */

  public void noOfTimesToRetry(short number) {

    // Max allowed is 10 times retry
    if (number > BDMInterfaceConstants.BDM_INTERFACE_MAX_NUMBER_OF_RETRIES) {
      number = BDMInterfaceConstants.BDM_INTERFACE_MAX_NUMBER_OF_RETRIES;
    }
    final DefaultHttpMethodRetryHandler retryhandler =
      new DefaultHttpMethodRetryHandler(number, false);
    this.httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
      retryhandler);
  }

  /**
   * Set parameter for length of timeout for the HttpClient requests.
   *
   * @param timeout
   */

  public void setTimeout(final int timeout) {

    // TODO set connection timeout
    this.httpClient.getHttpConnectionManager().getParams()
      .setConnectionTimeout(timeout);
    this.httpClient.getHttpConnectionManager().getParams()
      .setSoTimeout(timeout);
  }

  /**
   * Sets the call and socket timeouts of http client to given values
   *
   * @param callTimeOut
   * @param socketTimeOut
   */
  public void setCallAndSocketTimeouts(final int callTimeOut,
    final int socketTimeOut) {

    this.httpClient.getHttpConnectionManager().getParams()
      .setConnectionTimeout(callTimeOut);
    this.httpClient.getHttpConnectionManager().getParams()
      .setSoTimeout(socketTimeOut);
  }

  /**
   * Take httpMethod and prepare a rest response object that represents the result
   * of the httpMethod execution.
   *
   * @param httpMethod - object representing the HTTP request & response
   * @return BDM Rest response which represents the HTTP response
   * @throws IOException
   */

  public BDMRestResponse getRestResponse(final HttpMethod httpMethod)
    throws IOException {

    final BDMRestResponse returnResponse = new BDMRestResponse();
    returnResponse.setBody(httpMethod.getResponseBodyAsString());
    returnResponse.setStatusLine(httpMethod.getStatusLine());
    returnResponse.setBodyAsInputStream(httpMethod.getResponseBodyAsStream());

    final Header[] headers = httpMethod.getResponseHeaders();
    final Map<String, String> responseHeaders = new HashMap<String, String>();
    for (final Header h : headers) {
      responseHeaders.put(h.getName(), h.getValue());
    }
    returnResponse.setHeaders(responseHeaders);

    if (!(returnResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK))
      Trace.kTopLevelLogger.info("Response for " + httpMethod.getPath()
        + " is " + returnResponse.getStatusLine().getStatusCode() + " : "
        + new String(returnResponse.getBody()));

    return returnResponse;
  }

  /**
   * Make a POST request and provide the status code, response body and
   * response headers.
   *
   * @param request - rest request object to prepare the POST call
   * @return rest response which represents the response of the HTTP request
   * @throws IOException
   */
  public BDMRestResponse post(final BDMRestRequest request)
    throws IOException, AppException {

    final PostMethod httpPost = new PostMethod(request.getEndpointURI());

    if (!request.getHeaders().isEmpty()) {
      for (final Map.Entry<String, String> entry : request.getHeaders()
        .entrySet()) {
        httpPost.addRequestHeader(entry.getKey(), entry.getValue());
      }
    }
    final RequestEntity entity = new StringRequestEntity(request.getBody(),
      request.getContentType(), request.getCharset());
    httpPost.setRequestEntity(entity);
    if (!CuramConst.gkEmpty.equals(request.getBody())) {
      httpPost.setRequestHeader("Content-Type", request.getContentType());
    }

    return makeRequest(httpPost, request);
  }

  /**
   * Make a GET request and provide the status code, response body and
   * response headers.
   *
   * @param request - rest request object to prepare the GET call
   * @return rest response which represents the response of the HTTP request
   * @throws IOException
   */
  public BDMRestResponse get(final BDMRestRequest request)
    throws IOException, AppException {

    final GetMethod httpGet = new GetMethod(request.getEndpointURI());

    if (!request.getHeaders().isEmpty()) {
      for (final Map.Entry<String, String> entry : request.getHeaders()
        .entrySet()) {
        httpGet.addRequestHeader(entry.getKey(), entry.getValue());
      }
    }

    if (!CuramConst.gkEmpty.equals(request.getBody())) {
      httpGet.setRequestHeader("Content-Type", request.getContentType());
    }
    if (!request.getQueryParams().isEmpty()) {

      String queryParameter = "";
      for (final Map.Entry<String, String> entry : request.getQueryParams()
        .entrySet()) {
        queryParameter =
          queryParameter + entry.getKey() + "=" + entry.getValue() + "&";
      }

      queryParameter =
        queryParameter.substring(0, queryParameter.length() - 1);
      httpGet.setQueryString(queryParameter);
    }

    return makeRequest(httpGet, request);
  }

  /**
   * Make a PUT request and provide the status code, response body and
   * response headers.
   *
   * @param request - rest request object to prepare the PUT call
   * @return rest response which represents the response of the HTTP request
   * @throws IOException
   */
  public BDMRestResponse put(final BDMRestRequest request)
    throws IOException, AppException {

    final PutMethod httpPut = new PutMethod(request.getEndpointURI());

    if (!request.getHeaders().isEmpty()) {
      for (final Map.Entry<String, String> entry : request.getHeaders()
        .entrySet()) {
        httpPut.addRequestHeader(entry.getKey(), entry.getValue());
      }
    }
    final RequestEntity entity = new StringRequestEntity(request.getBody(),
      request.getContentType(), request.getCharset());
    httpPut.setRequestEntity(entity);

    if (!CuramConst.gkEmpty.equals(request.getBody())) {
      httpPut.setRequestHeader("Content-Type", request.getContentType());
    }

    return makeRequest(httpPut, request);
  }

  /**
   * Make a DELETE request and provide the status code, response body and
   * response headers.
   *
   * @param request - rest request object to prepare the DELETE call
   * @return rest response which represents the response of the HTTP request
   * @throws IOException
   */
  public BDMRestResponse delete(final BDMRestRequest request)
    throws IOException, AppException {

    final DeleteMethod httpDelete =
      new DeleteMethod(request.getEndpointURI());

    if (!request.getHeaders().isEmpty()) {
      for (final Map.Entry<String, String> entry : request.getHeaders()
        .entrySet()) {
        httpDelete.addRequestHeader(entry.getKey(), entry.getValue());
      }
    }

    if (!CuramConst.gkEmpty.equals(request.getBody())) {
      httpDelete.setRequestHeader("Content-Type", request.getContentType());
    }

    if (!request.getQueryParams().isEmpty()) {
      // Use streams to convert map entries to strings consisting of key=value and
      // joining them together with &
      final String queryString = request.getQueryParams().entrySet().stream()
        .map(e -> e.getKey() + "=" + e.getValue()).collect(joining("&"));

      httpDelete.setQueryString(queryString);
    }

    return makeRequest(httpDelete, request);
  }

  /**
   * Make the HTTP call to the URL specified in httpMethod with the included
   * parameters, headers, etc.
   *
   * @param httpMethod - HTTP request object
   * @return Rest response object representing return of HTTP request
   * @throws IOException
   */

  BDMRestResponse makeRequest(final HttpMethod httpMethod,
    final BDMRestRequest request) throws IOException, AppException {

    final BDMAPIAuditUtil bdmapiAuditUtil = new BDMAPIAuditUtil();
    BDMRestResponse bdmRestResponse = new BDMRestResponse();
    final String requestObject =
      bdmapiAuditUtil.convertToJSON(httpMethod, true);
    try {
      httpClient.executeMethod(httpMethod);
      try {
        bdmRestResponse = getRestResponse(httpMethod);
        // BEGIN, ADO-107574 , Audit enablement for APIs
        // construct the BDMAPIAuditDetails and invoke the method 'auditAPI' to store
        // the request/response
        final BDMAPIAuditDetails bdmapiAuditDetails =
          new BDMAPIAuditDetails.BDMAPIAuditDetailsBuilder()
            .setMethod(request.getInvokingMethod())
            .setRelatedID(request.getRelatedID())
            .setRequestObject(requestObject).setResponseObject(httpMethod)
            .setRequestTransactionDateTime(
              request.getRequestTransactionDateTime())
            .setResponseTransactionDateTime(DateTime.getCurrentDateTime())
            .setApiType(BDMAUDITAPITYPE.BDMOUTBOUND)
            .setStatusCode(bdmRestResponse.getStatusLine().getStatusCode())
            .setSource(request.getSource())
            .setCorrelationID(request.getCorrelationID()).build();

        bdmapiAuditUtil.auditAPI(bdmapiAuditDetails);
        // END, ADO-107574 , Audit enablement for APIs
        return bdmRestResponse;
      } finally {
        httpMethod.releaseConnection();
      }
    } catch (final HttpException e) {
      Trace.error(httpMethod.getURI().toString(), e.getMessage(), e);
      throw new IOException(e.getMessage(), e);
    } catch (final IOException e) {
      Trace.error(httpMethod.getURI().toString(), e.getMessage(), e);
      throw e;
    }
  }

  /**
   *
   * @param request
   * @return
   * @throws IOException
   */

  public BDMRestResponse callAPI(final BDMRestRequest request)
    throws IOException, AppException {

    try {
      if (request.getMethod() == null) {
        throw new IOException("Not Supported");
      }
      switch (request.getMethod()) {
        case GET:
          return get(request);
        case POST:
          return post(request);
        case PUT:
          return put(request);
        case DELETE:
          return delete(request);

        default:
          throw new IOException(request.getMethod() + " is not supported.");
      }
    } catch (final IOException ioex) {
      Trace.kTopLevelLogger.error(ioex.getMessage(), ioex);
      final StringWriter errors = new StringWriter();
      ioex.printStackTrace(new PrintWriter(errors));
      throw ioex;
    }
  }

}
