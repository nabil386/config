package curam.ca.gc.bdm.util.rest.impl;

import curam.util.type.DateTime;

// BEGIN, ADO-107574
/**
 * Helper class to construct the audit details for APIs
 *
 */
public class BDMAPIAuditDetails {

  private final String invokingMethod;

  private final Object relatedID;

  private final Object requestObject;

  private final Object responseObject;

  private final String apiType;

  private final String correlationID;

  private final String auditType;

  private final String source;

  private final int statusCode;

  private final DateTime requestTransactionDateTime;

  private final DateTime responseTransactionDateTime;

  public String getInvokingMethod() {

    return invokingMethod;
  }

  public Object getRelatedID() {

    return relatedID;
  }

  public String getApiType() {

    return apiType;
  }

  public String getCorrelationID() {

    return correlationID;
  }

  public String getAuditType() {

    return auditType;
  }

  public int getStatusCode() {

    return statusCode;
  }

  public String getSource() {

    return source;
  }

  public Object getRequestObject() {

    return requestObject;
  }

  public Object getResponseObject() {

    return responseObject;
  }

  public DateTime getRequestTransactionDateTime() {

    return requestTransactionDateTime;
  }

  public DateTime getResponseTransactionDateTime() {

    return responseTransactionDateTime;
  }

  @SuppressWarnings("synthetic-access")
  private BDMAPIAuditDetails(
    final BDMAPIAuditDetailsBuilder bdmAPIAuditDetailsBuilder) {

    this.invokingMethod = bdmAPIAuditDetailsBuilder.method;
    this.relatedID = bdmAPIAuditDetailsBuilder.relatedID;
    this.apiType = bdmAPIAuditDetailsBuilder.apiType;
    this.correlationID = bdmAPIAuditDetailsBuilder.correlationID;
    this.auditType = bdmAPIAuditDetailsBuilder.auditType;
    this.statusCode = bdmAPIAuditDetailsBuilder.statusCode;
    this.source = bdmAPIAuditDetailsBuilder.source;
    this.requestObject = bdmAPIAuditDetailsBuilder.requestObject;
    this.responseObject = bdmAPIAuditDetailsBuilder.responseObject;
    this.requestTransactionDateTime =
      bdmAPIAuditDetailsBuilder.requestTransactionDateTime;
    this.responseTransactionDateTime =
      bdmAPIAuditDetailsBuilder.responseTransactionDateTime;
  }

  public static class BDMAPIAuditDetailsBuilder {

    private String method;

    private Object relatedID;

    private Object requestObject;

    private Object responseObject;

    private String apiType;

    private String correlationID;

    private String auditType;

    private int statusCode;

    private String source;

    private DateTime requestTransactionDateTime;

    private DateTime responseTransactionDateTime;

    public BDMAPIAuditDetailsBuilder setMethod(final String methodInput) {

      this.method = methodInput;
      return this;
    }

    public BDMAPIAuditDetailsBuilder setRelatedID(final Object relatedID) {

      this.relatedID = relatedID;
      return this;
    }

    public BDMAPIAuditDetailsBuilder setApiType(final String apiType) {

      this.apiType = apiType;
      return this;
    }

    public BDMAPIAuditDetailsBuilder
      setCorrelationID(final String correlationID) {

      this.correlationID = correlationID;
      return this;
    }

    public BDMAPIAuditDetailsBuilder setAuditType(final String auditType) {

      this.auditType = auditType;
      return this;
    }

    public BDMAPIAuditDetailsBuilder setStatusCode(final int statusCode) {

      this.statusCode = statusCode;
      return this;
    }

    public BDMAPIAuditDetailsBuilder setSource(final String source) {

      this.source = source;
      return this;
    }

    public BDMAPIAuditDetailsBuilder
      setRequestObject(final Object requestObject) {

      this.requestObject = requestObject;
      return this;
    }

    public BDMAPIAuditDetailsBuilder
      setResponseObject(final Object responseObject) {

      this.responseObject = responseObject;
      return this;
    }

    public BDMAPIAuditDetailsBuilder setRequestTransactionDateTime(
      final DateTime requestTransactionDateTime) {

      this.requestTransactionDateTime = requestTransactionDateTime;
      return this;
    }

    public BDMAPIAuditDetailsBuilder setResponseTransactionDateTime(
      final DateTime responseTransactionDateTime) {

      this.responseTransactionDateTime = responseTransactionDateTime;
      return this;
    }

    public BDMAPIAuditDetails build() {

      return new BDMAPIAuditDetails(this);
    }
  }

  // END, ADO-107574
}
