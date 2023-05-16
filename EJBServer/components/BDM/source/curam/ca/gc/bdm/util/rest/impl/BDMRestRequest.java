package curam.ca.gc.bdm.util.rest.impl;

import curam.core.impl.CuramConst;
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

  private final Map<String, String> headers;

  private final Map<String, String> queryParams;

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
}
