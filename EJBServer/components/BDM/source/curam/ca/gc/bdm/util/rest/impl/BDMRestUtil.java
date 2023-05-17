package curam.ca.gc.bdm.util.rest.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import curam.ca.gc.bdm.codetable.BDMAUDITAPITYPE;
import curam.ca.gc.bdm.message.BDMBPOCCT;
import curam.ca.gc.bdm.message.BDMRESTAPIERRORMESSAGE;
import curam.ca.gc.bdm.util.rest.impl.BDMOutboundCloudEventDetails.BDMOutboundCloudEventDetailsBuilder;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.security.EncryptionUtil;
import curam.util.type.DateTime;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.httpclient.HttpStatus;

/**
 * Utility class to handle common logic for Federal data hub services
 *
 */
public class BDMRestUtil {

  // BEGIN, ADO-117413 , CloudEvent, CorrelationID Implementation
  static String CLOUDEVENT_SPECVERSION =
    Configuration.getProperty(EnvVars.CLOUDEVENT_SPECVERSION);

  static String CLOUDEVENT_ID =
    Configuration.getProperty(EnvVars.CLOUDEVENT_ID);

  static String CLOUDEVENT_SOURCE =
    Configuration.getProperty(EnvVars.CLOUDEVENT_SOURCE);

  static String CLOUDEVENT_TYPE =
    Configuration.getProperty(EnvVars.CLOUDEVENT_TYPE);

  static String CLOUDEVENT_DATACONTENTTYPE =
    Configuration.getProperty(EnvVars.CLOUDEVENT_DATACONTENTTYPE);

  static String CLOUDEVENT_DATASCHEMA =
    Configuration.getProperty(EnvVars.CLOUDEVENT_DATASCHEMA);

  static String CLOUDEVENT_SUBJECT =
    Configuration.getProperty(EnvVars.CLOUDEVENT_SUBJECT);

  static String CLOUDEVENT_TIME =
    Configuration.getProperty(EnvVars.CLOUDEVENT_TIME);

  static String CLOUDEVENT_PARTITIONKEY =
    Configuration.getProperty(EnvVars.CLOUDEVENT_PARTITIONKEY);

  static String CLOUDEVENT_FILESEQUENCENUMBER =
    Configuration.getProperty(EnvVars.CLOUDEVENT_FILESEQUENCENUMBER);

  static String CLOUDEVENT_SEQUENCE =
    Configuration.getProperty(EnvVars.CLOUDEVENT_SEQUENCE);

  static String CLOUDEVENT_TOTALCOUNT =
    Configuration.getProperty(EnvVars.CLOUDEVENT_TOTALCOUNT);
  // END, ADO-117413 , CloudEvent, CorrelationID Implementation

  /**
   * Helper method that will take the JSON object and convert into input class.
   * JSON object will be mapped to the struct and will be returned.
   * Unknown fields will be ignored rather than failing the mapping.
   *
   * @param json the JSON text
   * @param classObj the struct class instance to convert to
   * @return a instance of classObj
   */
  public static <T> T convertFromJSON(final String json,
    final Class<T> classObj) {

    final Gson gson =
      new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    return gson.fromJson(json.toString(), classObj);
  }

  /**
   * Helper method that will take the key object and convert into
   * JSON object and will be returned.
   *
   * @param key
   * @return JSON Object
   */
  public static String convertToJSON(final Object key) {

    final Gson gson =
      new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    return gson.toJson(key);
  }

  @Deprecated
  /**
   * This method throws 404 - resource not found message.
   *
   * @param argumentMsg
   *
   * @throws AppException
   * @throws InformationalException
   */
  public static AppException throwHTTP404Status(final String argumentMsg)
    throws AppException, InformationalException {

    final AppException appException =
      new AppException(BDMBPOCCT.ERR_HTTP_404_RESOURCE_NOT_FOUND);
    appException.arg(argumentMsg);

    Trace.kTopLevelLogger.error(appException.getMessage());

    return appException;
  }

  /**
   * This method throws 404 - resource not found message.
   *
   * @param argumentMsg
   *
   * @throws AppException
   * @throws InformationalException
   */

  public static void throwHTTP404Status(final String argumentMsg,
    final BDMAPIAuditDetails bdmapiAuditDetails)
    throws AppException, InformationalException {

    final AppException appException =
    new AppException(BDMBPOCCT.ERR_HTTP_404_RESOURCE_NOT_FOUND);
    appException.arg(argumentMsg);

    // BEGIN, ADO-107574 , Audit enablement for interfaces
    auditErrorResponse(appException, HttpStatus.SC_BAD_REQUEST,
      bdmapiAuditDetails);
    // END, ADO-107574 , Audit enablement for interfaces

    Trace.kTopLevelLogger.error(appException.getMessage());

    throw appException;
  }

  /**
   * This method throws 404 - resource not found message.
   *
   * @param AppException
   *
   * @throws AppException
   * @throws InformationalException
   */
  public static void throwHTTP404Status(final AppException appException,
    final BDMAPIAuditDetails bdmapiAuditDetails)
    throws AppException, InformationalException {

    auditErrorResponse(appException, HttpStatus.SC_NOT_FOUND,
      bdmapiAuditDetails);

    Trace.kTopLevelLogger.error(appException.getMessage());

    throw appException;
  }

  @Deprecated
  /**
   * This method throws 400 - bad request error.
   *
   * @param argumentMsg
   *
   * @throws AppException
   * @throws InformationalException
   */
  public static void throwHTTP400Status(final String argumentMsg)
    throws AppException, InformationalException {

    final AppException appException =
      new AppException(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST);
    appException.arg(argumentMsg);

    Trace.kTopLevelLogger.error(appException.getMessage());

    throw appException;
  }

  /**
   * This method throws 400 - bad request error.
   *
   * @param argumentMsg
   *
   * @throws AppException
   * @throws InformationalException
   */
  public static void throwHTTP400Status(final String argumentMsg,
    final BDMAPIAuditDetails bdmapiAuditDetails)
    throws AppException, InformationalException {

    final AppException appException =
      new AppException(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST);
    appException.arg(argumentMsg);

    // BEGIN, ADO-107574 , Audit enablement for interfaces
    auditErrorResponse(appException, HttpStatus.SC_BAD_REQUEST,
      bdmapiAuditDetails);
    // END, ADO-107574 , Audit enablement for interfaces

    Trace.kTopLevelLogger.error(appException.getMessage());

    throw appException;
  }

  @Deprecated
  /**
   * This method throws 403 - no permission message.
   *
   * @param argumentMsg
   *
   * @throws AppException
   * @throws InformationalException
   */
  public static AppException throwHTTP403Status()
    throws AppException, InformationalException {

    Trace.kTopLevelLogger
      .error(BDMBPOCCT.ERR_HTTP_403_PERMISSION_DENIED.getMessageText());

    return new AppException(BDMBPOCCT.ERR_HTTP_403_PERMISSION_DENIED);
  }

  /**
   * This method returns a 400 - bad request after logging the error message
   *
   * @param argumentMsg
   *
   * @throws AppException
   * @throws InformationalException
   */
  public static AppException getHTTP400Status(final AppException ae)
    throws AppException, InformationalException {

    Trace.kTopLevelLogger.error(ae.getMessage());

    return ae;
  }

  /**
   * This method throws 403 - no permission message.
   *
   * @param argumentMsg
   *
   * @throws AppException
   * @throws InformationalException
   */
  public static void
    throwHTTP403Status(final BDMAPIAuditDetails bdmapiAuditDetails)
      throws AppException, InformationalException {

    // BEGIN, ADO-107574 , Audit enablement for interfaces
    auditErrorResponse(
      new AppException(BDMBPOCCT.ERR_HTTP_403_PERMISSION_DENIED),
      HttpStatus.SC_FORBIDDEN, bdmapiAuditDetails);
    // END, ADO-107574 , Audit enablement for interfaces

    Trace.kTopLevelLogger
      .error(BDMBPOCCT.ERR_HTTP_403_PERMISSION_DENIED.getMessageText());

    throw new AppException(BDMBPOCCT.ERR_HTTP_403_PERMISSION_DENIED);
  }

  // BEGIN, Task 89081 - API Authentication Encryption for Outbound API processing
  /**
   * This method decrypts the value and return it for further processing.
   *
   * @param encryptedValue
   * @return String
   *
   * @throws AppException
   * @throws InformationalException
   */
  public static String decryptKeys(final String encryptedValue)
    throws AppException {

    String decryptedValue = curam.core.impl.CuramConst.gkEmpty;
    try {
      if (!StringUtil.isNullOrEmpty(encryptedValue)) {
        decryptedValue = EncryptionUtil.decryptPassword(encryptedValue);
      }
    } catch (final Exception e) {
      Trace.kTopLevelLogger
        .error("Error occurred during decryption" + e.getMessage(), e);
      throw new AppException(
        BDMRESTAPIERRORMESSAGE.BDM_ERR_AUTHENTICATION_KEY_DECRYPTION);
    }
    return decryptedValue;
  }

  // END, Task 89081 - API Authentication Encryption for Outbound API processing
  @Deprecated
  /**
   * This method throws 412 - precondition falied.
   *
   * @param argumentMsg
   *
   * @throws AppException
   * @throws InformationalException
   */
  public static void throwHTTP412Status(final String argumentMsg)
    throws AppException, InformationalException {

    final AppException appException =
      new AppException(BDMBPOCCT.ERR_HTTP_412_PRECONDITION_FAILED);
    appException.arg(argumentMsg);

    Trace.kTopLevelLogger.error(appException.getMessage());

    throw appException;
  }

  /**
   * This method throws 412 - precondition failed.
   *
   * @param argumentMsg
   *
   * @throws AppException
   * @throws InformationalException
   */
  public static void throwHTTP412Status(final String argumentMsg,
    final BDMAPIAuditDetails bdmapiAuditDetails)
    throws AppException, InformationalException {

    final AppException appException =
      new AppException(BDMBPOCCT.ERR_HTTP_412_PRECONDITION_FAILED);
    appException.arg(argumentMsg);

    // BEGIN, ADO-107574 , Audit enablement for interfaces
    auditErrorResponse(appException, HttpStatus.SC_PRECONDITION_FAILED,
      bdmapiAuditDetails);
    // END, ADO-107574 , Audit enablement for interfaces

    Trace.kTopLevelLogger.error(appException.getMessage());

    throw appException;
  }

  // BEGIN, ADO-107574 , Audit enablement for APIs

  /**
   * Helper method to audit the request/response details of the APIs when there
   * are
   * error scenarios. Based on the input details, first call will made to
   * 'ErrorMessage' class to construct the error response format and audit API
   * util class is invoked after constructing the BDMAPIAuditDetails for the error
   * scenario.
   *
   * @param AppException (that happened in the error scenario in the
   * inbound/outbound)
   * @param statusErrorCode
   * @param BDMAPIAuditDetails
   *
   * @throws AppException, InformationalException
   *
   */
  public static void auditErrorResponse(final AppException appException,
    final int statusErrorCode, BDMAPIAuditDetails bdmapiAuditDetails)
    throws AppException, InformationalException {

    final BDMAPIAuditUtil bdmapiAuditUtil = new BDMAPIAuditUtil();

    final BDMErrorMessage errorMessageRequestObject =
      new BDMErrorMessage(statusErrorCode, appException.getLocalizedMessage(),
        "error", appException.getCatEntry().getEntry());

    bdmapiAuditDetails = new BDMAPIAuditDetails.BDMAPIAuditDetailsBuilder()
      .setMethod(bdmapiAuditDetails.getInvokingMethod())
      .setRelatedID(bdmapiAuditDetails.getRelatedID())
      .setResponseObject(errorMessageRequestObject)
      .setApiType(BDMAUDITAPITYPE.BDMINBOUND)
      .setCorrelationID(bdmapiAuditDetails.getCorrelationID())
      .setSource(bdmapiAuditDetails.getSource())
      .setRequestObject(bdmapiAuditDetails.getRequestObject())
      .setRequestTransactionDateTime(
        bdmapiAuditDetails.getRequestTransactionDateTime())
      .setResponseTransactionDateTime(DateTime.getCurrentDateTime())
      .setStatusCode(statusErrorCode).build();

    bdmapiAuditUtil.auditAPI(bdmapiAuditDetails);
  }

  /**
   * Helper method to construct the Response JSON from HTTPURLconnection object
   * based on the response code, input stream/error stream is read to construct
   * the response
   *
   * @param HttpURLConnection
   * @return StringBuilder (Response)
   * @throws IOException
   */
  public StringBuilder getRESTResponseHTTPConnection(
    final HttpURLConnection httpConn) throws IOException {

    final StringBuilder responseBuilder = new StringBuilder();
    InputStream inputStreamObj;
    if (httpConn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
      inputStreamObj = httpConn.getInputStream();
    } else {
      inputStreamObj = httpConn.getErrorStream();
    }

    final BufferedReader in =
      new BufferedReader(new InputStreamReader(inputStreamObj));

    while (in.ready()) {
      responseBuilder.append(in.readLine());
    }
    return responseBuilder;
  }
  // END, ADO-107574 , Audit enablement for APIs

  // BEGIN, ADO-117413 , CloudEvent, CorrelationID Implementation

  /**
   * Method to add cloud event attributes to headers. The input
   * 'BDMOutboundCloudEventDetails' has the details of the cloud events populated
   * and values are read and set in header. For the name of cloud event attributes
   * to set in header, values are read from application properties. If the value
   * of 'fileAPI' boolean is set to true, then additionally file API related cloud
   * event attributes will be set in the header.(Used for the outbound APIs which
   * has 'BDMRestRequest' for holding the request/connection details)
   *
   * @param BDMRestRequest
   * @param BDMOutboundCloudEventDetails
   * @param fileAPI
   */
  public BDMRestRequest addCloudEventHeaders(final BDMRestRequest request,
    final BDMOutboundCloudEventDetails cloudEventDetails,
    final boolean fileAPI) {

    // get the values from the CloudEventDetails and set in header.
    request.addHeader(CLOUDEVENT_SPECVERSION,
      cloudEventDetails.getSpecVersion());
    request.addHeader(CLOUDEVENT_ID, cloudEventDetails.getId());
    request.addHeader(CLOUDEVENT_SOURCE, cloudEventDetails.getSource());
    request.addHeader(CLOUDEVENT_TYPE, cloudEventDetails.getType());
    request.addHeader(CLOUDEVENT_DATACONTENTTYPE,
      cloudEventDetails.getDatacontenttype());
    request.addHeader(CLOUDEVENT_DATASCHEMA,
      cloudEventDetails.getDataschema());
    request.addHeader(CLOUDEVENT_SUBJECT, cloudEventDetails.getSubject());
    request.addHeader(CLOUDEVENT_TIME, cloudEventDetails.getTime());
    if (fileAPI) {
      request.addHeader(CLOUDEVENT_PARTITIONKEY,
        cloudEventDetails.getPartitionkey());
      request.addHeader(CLOUDEVENT_FILESEQUENCENUMBER,
        cloudEventDetails.getFilesequencenumber());
      request.addHeader(CLOUDEVENT_SEQUENCE, cloudEventDetails.getSequence());
      request.addHeader(CLOUDEVENT_TOTALCOUNT,
        cloudEventDetails.getTotalcount());
    }

    request.setCorrelationID(cloudEventDetails.getId());
    request.setSource(cloudEventDetails.getSource());
    return request;
  }

  /**
   * Method to add cloud event attributes to headers. The input
   * 'BDMOutboundCloudEventDetails' has the details of the cloud events populated
   * and values are read and set in header. For the name of cloud event attributes
   * to set in header, values are read from application properties. If the value
   * of 'fileAPI' boolean is set to true, then additionally file API related cloud
   * event attributes will be set in the header.(Used for the outbound APIs which
   * has 'HTTPURLConnection' approach where the headers are populated in
   * headerMap)
   *
   * @param BDMRestRequest
   * @param BDMOutboundCloudEventDetails
   * @param fileAPI
   */
  public Map<String, String> addCloudEventHeaders(
    final Map<String, String> headerMap,
    final BDMOutboundCloudEventDetails cloudEventDetails,
    final boolean fileAPI) {

    // get the values from the CloudEventDetails and set in header.
    headerMap.put(CLOUDEVENT_SPECVERSION, cloudEventDetails.getSpecVersion());
    headerMap.put(CLOUDEVENT_ID, cloudEventDetails.getId());
    headerMap.put(CLOUDEVENT_SOURCE, cloudEventDetails.getSource());
    headerMap.put(CLOUDEVENT_TYPE, cloudEventDetails.getType());
    headerMap.put(CLOUDEVENT_DATACONTENTTYPE,
      cloudEventDetails.getDatacontenttype());
    headerMap.put(CLOUDEVENT_DATASCHEMA, cloudEventDetails.getDataschema());
    headerMap.put(CLOUDEVENT_SUBJECT, cloudEventDetails.getSubject());
    headerMap.put(CLOUDEVENT_TIME, cloudEventDetails.getTime());
    if (fileAPI) {
      headerMap.put(CLOUDEVENT_PARTITIONKEY,
        cloudEventDetails.getPartitionkey());
      headerMap.put(CLOUDEVENT_FILESEQUENCENUMBER,
        cloudEventDetails.getFilesequencenumber());
      headerMap.put(CLOUDEVENT_SEQUENCE, cloudEventDetails.getSequence());
      headerMap.put(CLOUDEVENT_TOTALCOUNT, cloudEventDetails.getTotalcount());
    }
    return headerMap;
  }

  /**
   * Method to get the cloud event related headers for CCT related outbounds. The
   * values for attributes are read from application properties, so it can be
   * configured and changed later as per the interaction/need with interops.
   *
   * @param BDMRestRequest
   * @return BDMOutboundCloudEventDetails
   */
  public BDMOutboundCloudEventDetails
    getCloudEventDetailsForCCT(final BDMRestRequest req) {

    final String correlationUniqueId = UUID.randomUUID().toString();
    final String time = DateTime.getCurrentDateTime().toString();
    final BDMOutboundCloudEventDetails cloudEventDetails =
      new BDMOutboundCloudEventDetailsBuilder().setId(correlationUniqueId)
        .setSpecVersion(
          Configuration.getProperty(EnvVars.CLOUDEVENT_SPECVERSION_VALUE))
        .setSource(Configuration.getProperty(EnvVars.CLOUDEVENT_SOURCE_VALUE))
        .setType(Configuration.getProperty(EnvVars.BDM_CCT_CLOUDEVENT_TYPE))
        .setSubject(
          Configuration.getProperty(EnvVars.BDM_CCT_CLOUDEVENT_SUBJECT))
        .setTime(time).setDatacontenttype(req.getContentType()).build();

    return cloudEventDetails;
  }

  /**
   * Method to get the cloud event related headers for WSAddress related
   * outbounds. The
   * values for attributes are read from application properties, so it can be
   * configured and changed later as per the interaction/need with interops.
   *
   * @return BDMOutboundCloudEventDetails
   */
  public BDMOutboundCloudEventDetails getCloudEventDetailsForWSAddress() {

    final String correlationUniqueId = UUID.randomUUID().toString();
    final String time = DateTime.getCurrentDateTime().toString();
    final BDMOutboundCloudEventDetails cloudEventDetails =
      new BDMOutboundCloudEventDetailsBuilder().setId(correlationUniqueId)
        .setSpecVersion(
          Configuration.getProperty(EnvVars.CLOUDEVENT_SPECVERSION_VALUE))
        .setSource(Configuration.getProperty(EnvVars.CLOUDEVENT_SOURCE_VALUE))
        .setType(
          Configuration.getProperty(EnvVars.BDM_WSADDRESS_CLOUDEVENT_TYPE))
        .setSubject(
          Configuration.getProperty(EnvVars.BDM_WSADDRESS_CLOUDEVENT_SUBJECT))
        .setTime(time).build();

    return cloudEventDetails;
  }

  /**
   * Method to get the cloud event related headers for SIN/SIR related
   * outbounds. The
   * values for attributes are read from application properties, so it can be
   * configured and changed later as per the interaction/need with interops.
   *
   * @return BDMOutboundCloudEventDetails
   */
  public BDMOutboundCloudEventDetails getCloudEventDetailsForSINSIR() {

    final String correlationUniqueId = UUID.randomUUID().toString();
    final String time = DateTime.getCurrentDateTime().toString();
    final BDMOutboundCloudEventDetails cloudEventDetails =
      new BDMOutboundCloudEventDetailsBuilder().setId(correlationUniqueId)
        .setSpecVersion(
          Configuration.getProperty(EnvVars.CLOUDEVENT_SPECVERSION_VALUE))
        .setSource(Configuration.getProperty(EnvVars.CLOUDEVENT_SOURCE_VALUE))
        .setType(
          Configuration.getProperty(EnvVars.BDM_SINSIR_CLOUDEVENT_TYPE))
        .setSubject(
          Configuration.getProperty(EnvVars.BDM_SINSIR_CLOUDEVENT_SUBJECT))
        .setTime(time).build();

    return cloudEventDetails;
  }
  // END, ADO-117413 , CloudEvent, CorrelationID Implementation

  // BEGIN, ADO-117413 , CloudEvent, CorrelationID Implementation
  /**
   * Method to check if cloud event properties are enabled. Checks for overall
   * property for cloud enablement and additionally individual property set for
   * the method.
   *
   * @param methodName
   * @return boolean
   */
  public boolean isCloudEventEnabled(final String methodName) {

    boolean isCloudEventEnabled = false;
    if (Configuration.getBooleanProperty(EnvVars.ENV_CLOUDEVENT_ENABLED)
      && Configuration.getBooleanProperty(EnvVars.ENV_CLOUDEVENT_ENABLED
        + CuramConst.gkDotChar + methodName.trim())) {
      isCloudEventEnabled = true;
    }
    return isCloudEventEnabled;
  }
  // END, ADO-117413 , CloudEvent, CorrelationID Implementation
}
