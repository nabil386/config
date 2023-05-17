package curam.ca.gc.bdm.util.rest.impl;

import curam.core.impl.CuramConst;
import curam.util.type.DateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Interface to HTTP request.
 */

public class BDMRestRequest {

  private BDMRestMethod method;

  private String endpointURI;

  private String body;

  private String contentType;

  private String charset;

  private Map<String, String> headers;

  private Map<String, String> queryParams;

  private transient String relatedID;

  private transient String invokingMethod;

  private transient String correlationID;

  private transient String source;

  private transient DateTime requestTransactionDateTime;

  public BDMRestRequest() {

    this.headers = new HashMap<>();
    this.queryParams = new HashMap<>();
    this.charset = "UTF-8";
    this.reset();
  }

  /**
   * Place the object into an empty state.
   */
  public void reset() {

    this.clearMethod();
    this.clearEndpointURI();
    this.clearBody();
    this.clearHeaders();
    this.clearQueryParams();
    this.clearRelatedID();
    this.clearInvokingMethod();
    this.clearCorrelationID();
    this.clearSource();
  }

  public void addQueryParam(final String key, final String value) {

    this.queryParams.put(key, value);
  }

  public void addHeader(final String key, final String value) {

    this.headers.put(key, value);
  }

  public String removeQueryParam(final String key) {

    return this.queryParams.remove(key);
  }

  public String removeHeader(final String key) {

    return this.headers.remove(key);
  }

  public void setMethod(final BDMRestMethod method) {

    this.method = method;
  }

  public void setEndpointURI(final String endpointURI) {

    this.endpointURI = endpointURI;
  }

  public void setBody(final String body) {

    this.body = body;
  }

  public void setContentType(final String contentType) {

    this.contentType = contentType;
  }

  public void setCharset(final String charset) {

    this.charset = charset;
  }

  public Map<String, String> getHeaders() {

    return this.headers;
  }

  public Map<String, String> getQueryParams() {

    return this.queryParams;
  }

  public BDMRestMethod getMethod() {

    return this.method;
  }

  public String getEndpointURI() {

    return this.endpointURI;
  }

  public String getBody() {

    return this.body;
  }

  public String getContentType() {

    return this.contentType;
  }

  public String getCharset() {

    return this.charset;
  }

  public void clearMethod() {

    this.method = null;
  }

  public void clearEndpointURI() {

    this.endpointURI = CuramConst.gkEmpty;
  }

  public void clearBody() {

    this.body = CuramConst.gkEmpty;
  }

  public void clearQueryParams() {

    this.queryParams.clear();
  }

  public void clearHeaders() {

    this.headers.clear();
  }

  public void setHeaders(final Map<String, String> headersInput) {

    this.headers = headersInput;
  }

  public void setQueryParams(final Map<String, String> queryParamsInput) {

    this.queryParams = queryParamsInput;
  }

  public void setRelatedID(final String relatedID) {

    this.relatedID = relatedID;
  }

  public void setRelatedID(final long relatedID) {

    this.relatedID = CuramConst.gkEmpty + relatedID;
  }

  public String getRelatedID() {

    return this.relatedID;
  }

  public void clearRelatedID() {

    this.relatedID = CuramConst.gkEmpty;
  }

  public void setInvokingMethod(final String invokingMethod) {

    this.invokingMethod = invokingMethod;
  }

  public String getInvokingMethod() {

    return this.invokingMethod;
  }

  public void clearInvokingMethod() {

    this.invokingMethod = CuramConst.gkEmpty;
  }

  public void setCorrelationID(final String correlationID) {

    this.correlationID = correlationID;
  }

  public String getCorrelationID() {

    return this.correlationID;
  }

  public void clearCorrelationID() {

    this.correlationID = CuramConst.gkEmpty;
  }

  public void setSource(final String source) {

    this.source = source;
  }

  public String getSource() {

    return this.source;
  }

  public void clearSource() {

    this.source = CuramConst.gkEmpty;
  }

  public void
    setRequestTransactionDateTime(final DateTime requestTransactionDateTime) {

    this.requestTransactionDateTime = requestTransactionDateTime;
  }

  public DateTime getRequestTransactionDateTime() {

    return this.requestTransactionDateTime;
  }

  public void clearRequestTransactionDateTime() {

    this.source = CuramConst.gkEmpty;
  }

}
