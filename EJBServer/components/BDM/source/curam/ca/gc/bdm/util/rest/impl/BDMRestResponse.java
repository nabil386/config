package curam.ca.gc.bdm.util.rest.impl;

import curam.core.impl.CuramConst;
import java.io.InputStream;
import java.util.Map;
import org.apache.commons.httpclient.StatusLine;

/**
 * Interface to an HTTP response.
 */
public class BDMRestResponse {

  private StatusLine statusLine;

  private String body;

  private Map<String, String> headers;

  private InputStream bodyAsInputStream;

  public BDMRestResponse() {

    this.reset();
  }

  /**
   * Place the object into an empty state.
   */
  public void reset() {

    this.statusLine = null;
    this.body = CuramConst.gkEmpty;
    this.headers = null;
    this.bodyAsInputStream = null;
  }

  public StatusLine getStatusLine() {

    return this.statusLine;
  }

  public String getBody() {

    return this.body;
  }

  public Map<String, String> getHeaders() {

    return this.headers;
  }

  public void setStatusLine(final StatusLine statusLine) {

    this.statusLine = statusLine;
  }

  public void setBody(final String body) {

    this.body = body;
  }

  public void setHeaders(final Map<String, String> headers) {

    this.headers = headers;
  }

  public InputStream getBodyAsInputStream() {

    return this.bodyAsInputStream;
  }

  public void setBodyAsInputStream(final InputStream bodyAsInputStream) {

    this.bodyAsInputStream = bodyAsInputStream;
  }
}
