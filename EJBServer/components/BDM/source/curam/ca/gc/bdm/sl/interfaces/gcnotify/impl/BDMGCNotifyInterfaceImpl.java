package curam.ca.gc.bdm.sl.interfaces.gcnotify.impl;

import com.google.gson.Gson;
import curam.ca.gc.bdm.batch.bdmgcnotify.impl.BDMGCNotifyWhereClauseKey2;
import curam.ca.gc.bdm.codetable.BDMGCNOTIFYALERTTYPE;
import curam.ca.gc.bdm.codetable.BDMGCNOTIFYINTERFACETYPE;
import curam.ca.gc.bdm.codetable.BDMGCNOTIFYNOTIFICATIONSTATUS;
import curam.ca.gc.bdm.codetable.BDMGCNotifyTemplateType;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.entity.bdmgcnotify.struct.BDMGcNotifyRequestDataDetails;
import curam.ca.gc.bdm.entity.bdmgcnotify.struct.BDMGcNotifyRequestDataDetailsList;
import curam.ca.gc.bdm.entity.bdmgcnotify.struct.BDMGcNotifyRequestDataDtls;
import curam.ca.gc.bdm.gcnotify.impl.BDMGCNotifyHelper;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMRESTAPIERRORMESSAGE;
import curam.ca.gc.bdm.notification.impl.BDMNotificationConstants;
import curam.ca.gc.bdm.sl.interfaces.gcnotify.impl.gcnotifypojos.BDMGCNotifyBulkRequest;
import curam.ca.gc.bdm.sl.interfaces.gcnotify.impl.gcnotifypojos.BDMGCNotifyRealTimeEmailRequest;
import curam.ca.gc.bdm.sl.interfaces.gcnotify.impl.gcnotifypojos.BDMGCNotifyRealTimeSMSRequest;
import curam.ca.gc.bdm.sl.interfaces.gcnotify.impl.gcnotifypojos.BDMGCNotifyResponse;
import curam.ca.gc.bdm.sl.interfaces.gcnotify.intf.BDMGCNotifyInterfaceIntf;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.util.dataaccess.DynamicDataAccess;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import curam.util.type.DateTime;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 * GC Notify Implementation
 *
 */
public class BDMGCNotifyInterfaceImpl implements BDMGCNotifyInterfaceIntf {

  public static URL url = null;

  static HttpURLConnection connection = null;

  static String ENV_GCNOTIFY_HOST =
    Configuration.getProperty(EnvVars.BDM_ENV_GCNOTIFY_HOST);

  static String GCNOTIFY_API_BULK_URL =
    Configuration.getProperty(EnvVars.BDM_ENV_GCNOTIFY_BULK_URL);

  static String GCNOTIFY_BULK_API_URL =
    ENV_GCNOTIFY_HOST + GCNOTIFY_API_BULK_URL;

  static String ENV_GCNOTIFY_EMAIL_URL =
    Configuration.getProperty(EnvVars.BDM_ENV_GCNOTIFY_EMAIL_URL);

  static String ENV_GCNOTIFY_SMS_URL =
    Configuration.getProperty(EnvVars.BDM_ENV_GCNOTIFY_SMS_URL);

  static String GCNOTIFY_EMAIL_API_URL =
    ENV_GCNOTIFY_HOST + ENV_GCNOTIFY_EMAIL_URL;

  static String GCNOTIFY_SMS_API_URL =
    ENV_GCNOTIFY_HOST + ENV_GCNOTIFY_SMS_URL;

  static String ENV_GCNOTIFY_API_KEY =
    Configuration.getProperty(EnvVars.BDM_ENV_GCNOTIFY_API_KEY);

  /**
   * Send bulk emails for the day
   *
   * @param BDMGcNotifyRequestDataDetailsList
   * @return String *
   */
  @Override
  public String sendGCNotifyBulkEmailRequest(
    final BDMGcNotifyRequestDataDetailsList rowsList)
    throws AppException, InformationalException, IOException {

    final BDMGcNotifyRequestDataDetails gcNotifyDetails =
      rowsList.dtls.get(0);

    final BDMGCNotifyBulkRequest requestObj = new BDMGCNotifyBulkRequest();

    requestObj.setName(getBulkRequestName(gcNotifyDetails));

    requestObj.setTemplateId(gcNotifyDetails.templateID);

    // get rows with row header
    final List<List<String>> rows =
      this.getRowDetails(BDMConstants.kEmailAtributeUpper, rowsList);

    // after the for loop set the rows finally back to request object
    requestObj.setRows(rows);

    final Gson gson = new Gson();
    final String gcNotifyJson = gson.toJson(requestObj);
    String gcNotifyResponseJson = CuramConst.gkEmpty;

    try {
      gcNotifyResponseJson = sendGCNotifyRESTRequest(GCNOTIFY_BULK_API_URL,
        ENV_GCNOTIFY_API_KEY, gcNotifyJson);
    } catch (final NumberFormatException ex) {
      ex.printStackTrace();
    }
    return gcNotifyResponseJson;
  }

  /**
   * Populate the Bulk name to be sent to input json, based on the input
   * criteria
   *
   * @param gcNotifyDetails
   * @return String
   */
  private String
    getBulkRequestName(final BDMGcNotifyRequestDataDetails gcNotifyDetails) {

    String bulkRequestName = CuramConst.gkEmpty;

    if (!gcNotifyDetails.alertType.isEmpty() && gcNotifyDetails.alertType
      .equals(BDMGCNOTIFYALERTTYPE.BDM_GCNOTIFY_EMAIL)) {
      if (!gcNotifyDetails.language.isEmpty()
        && gcNotifyDetails.language.equals(BDMLANGUAGE.ENGLISHL)) {

        if (!gcNotifyDetails.templateType.isEmpty()
          && gcNotifyDetails.templateType
            .equals(BDMGCNotifyTemplateType.BDM_GC_COR)) {
          bulkRequestName =
            BDMConstants.kGCNotifyBulk_Correspondence_Email_English;
        } else if (!gcNotifyDetails.templateType.isEmpty()
          && gcNotifyDetails.templateType
            .equals(BDMGCNotifyTemplateType.BDM_GC_NF)) {
          bulkRequestName =
            BDMConstants.kGCNotifyBulk_Notifications_Email_English;
        }

      } else if (!gcNotifyDetails.language.isEmpty()
        && gcNotifyDetails.language.equals(BDMLANGUAGE.FRENCHL)) {

        if (!gcNotifyDetails.templateType.isEmpty()
          && gcNotifyDetails.templateType
            .equals(BDMGCNotifyTemplateType.BDM_GC_COR)) {
          bulkRequestName =
            BDMConstants.kGCNotifyBulk_Correspondence_Email_French;
        } else if (!gcNotifyDetails.templateType.isEmpty()
          && gcNotifyDetails.templateType
            .equals(BDMGCNotifyTemplateType.BDM_GC_NF)) {
          bulkRequestName =
            BDMConstants.kGCNotifyBulk_Notifications_Email_French;
        }

      }
    } else if (!gcNotifyDetails.alertType.isEmpty()
      && gcNotifyDetails.alertType
        .equals(BDMGCNOTIFYALERTTYPE.BDM_GCNOTIFY_PHONE)) {

      if (!gcNotifyDetails.language.isEmpty()
        && gcNotifyDetails.language.equals(BDMLANGUAGE.ENGLISHL)) {

        if (!gcNotifyDetails.templateType.isEmpty()
          && gcNotifyDetails.templateType
            .equals(BDMGCNotifyTemplateType.BDM_GC_COR)) {
          bulkRequestName =
            BDMConstants.kGCNotifyBulk_Correspondence_SMS_English;
        } else if (!gcNotifyDetails.templateType.isEmpty()
          && gcNotifyDetails.templateType
            .equals(BDMGCNotifyTemplateType.BDM_GC_NF)) {
          bulkRequestName =
            BDMConstants.kGCNotifyBulk_Notifications_SMS_English;
        }

      } else if (!gcNotifyDetails.language.isEmpty()
        && gcNotifyDetails.language.equals(BDMLANGUAGE.FRENCHL)) {

        if (!gcNotifyDetails.templateType.isEmpty()
          && gcNotifyDetails.templateType
            .equals(BDMGCNotifyTemplateType.BDM_GC_COR)) {
          bulkRequestName =
            BDMConstants.kGCNotifyBulk_Correspondence_SMS_French;
        } else if (!gcNotifyDetails.templateType.isEmpty()
          && gcNotifyDetails.templateType
            .equals(BDMGCNotifyTemplateType.BDM_GC_NF)) {
          bulkRequestName =
            BDMConstants.kGCNotifyBulk_Notifications_SMS_French;
        }

      }

    }
    // If no matching , set to default one
    if (bulkRequestName.isEmpty()) {
      bulkRequestName = BDMConstants.kGCNotifyBulkSendSubjectAttribute;
    }
    return bulkRequestName;
  }

  /**
   * Populate the Bulk SMS
   *
   * @param BDMGcNotifyRequestDataDetailsList
   * @return String
   */
  @Override
  public String sendGCNotifyBulkSMSRequest(
    final BDMGcNotifyRequestDataDetailsList rowsList)
    throws AppException, InformationalException, IOException {

    final BDMGCNotifyBulkRequest requestObj = new BDMGCNotifyBulkRequest();
    final BDMGcNotifyRequestDataDetails gcNotifyDetails =
      rowsList.dtls.get(0);
    requestObj.setName(getBulkRequestName(gcNotifyDetails));
    requestObj.setTemplateId(gcNotifyDetails.templateID);

    // get rows with row header
    final List<List<String>> rows =
      this.getRowDetails(BDMConstants.kGCNotifySmsAtributeUpper, rowsList);

    // after the for loop set the rows finally back to request object
    requestObj.setRows(rows);

    final Gson gson = new Gson();
    final String gcNotifyJson = gson.toJson(requestObj);
    String gcNotifyResponseJson = CuramConst.gkEmpty; // for return
    try {
      gcNotifyResponseJson = sendGCNotifyRESTRequest(GCNOTIFY_BULK_API_URL,
        ENV_GCNOTIFY_API_KEY, gcNotifyJson);
    } catch (final NumberFormatException ex) {
      ex.printStackTrace();
    }
    return gcNotifyResponseJson;

  }

  /**
   * Get rows for bulk notifications
   *
   * @param CommunicationMethod
   * @param rowsList
   * @return
   */
  private List<List<String>> getRowDetails(final String CommunicationMethod,
    final BDMGcNotifyRequestDataDetailsList rowsList) {

    final List<List<String>> updatedRows = new ArrayList<>();
    if (CommunicationMethod
      .equalsIgnoreCase(BDMConstants.kEmailAtributeUpper)) {
      updatedRows.add(getRowHeader(BDMConstants.kEmailAtributeLower));
    } else if (CommunicationMethod
      .equalsIgnoreCase(BDMConstants.kGCNotifySmsAtributeUpper)) {
      updatedRows.add(getRowHeader(BDMConstants.kGCNotifySmsAtributeLower));
    }
    for (final BDMGcNotifyRequestDataDetails dtls : rowsList.dtls) {
      final List<String> rowDetail1 = new ArrayList<>();
      if (CommunicationMethod
        .equalsIgnoreCase(BDMConstants.kEmailAtributeUpper)) {
        rowDetail1.add(dtls.emailAddress);
      } else if (CommunicationMethod
        .equalsIgnoreCase(BDMConstants.kGCNotifySmsAtributeUpper)) {
        rowDetail1.add(dtls.phoneNumber);
      }
      rowDetail1.add(dtls.fullName);
      updatedRows.add(rowDetail1);
    }
    return updatedRows;
  }

  /**
   * Get row header based on bulk email/SMS
   *
   * @param emailOrSMS
   * @return
   */
  private List<String> getRowHeader(final String emailOrSMS) {

    final List<String> rowHeader = new ArrayList<>();
    if (!StringUtil.isNullOrEmpty(emailOrSMS)) {
      if (emailOrSMS.equalsIgnoreCase(BDMConstants.kEmailAtributeLower)) {
        rowHeader.add(BDMConstants.kEmailAddressAtributeLower);
      } else if (emailOrSMS
        .equalsIgnoreCase(BDMConstants.kGCNotifySmsAtributeLower)) {
        rowHeader.add(BDMConstants.kPhone_numberAtributeLower);
      }
    }
    rowHeader.add(BDMConstants.kNameAtributeLower);
    return rowHeader;
  }

  /**
   * Call the GCNotify interface
   *
   * @param apiURL
   * @param apiKEY
   * @param requestObj
   * @return responseMessage
   * @throws AppException
   * @throws InformationalException
   * @throws IOException
   */
  private String sendGCNotifyRESTRequest(final String apiURL,
    final String apiKEY, final String requestObj)
    throws AppException, InformationalException, IOException {

    // Setting proxy. This is essential to establish the connection from code
    // for GCNotify to work.
    // If system to system connection works , the configuration can be set to
    // false to skip this code
    final boolean isProxyRequired = Configuration
      .getBooleanProperty(EnvVars.BDM_ENABLE_PROXY_SETTINGS_FOR_INTERFACES);
    if (isProxyRequired) {
      System.setProperty(BDMConstants.kHttpProxyHostAttribute,
        BDMConstants.kHTTP_PROXY_HOST);
      System.setProperty(BDMConstants.kHttpProxyPortAttribute,
        BDMConstants.kHTTP_PROXY_PORT);
      System.setProperty(BDMConstants.kHttpsProxyHostAttribute,
        BDMConstants.kHTTPS_PROXY_HOST);
      System.setProperty(BDMConstants.kHttpsProxyPortAttribute,
        BDMConstants.kHTTPS_PROXY_PORT);
    }
    HttpsURLConnection connection = null;
    try {
      final URL url = new URL(apiURL);
      Trace.kTopLevelLogger.info("GCNotify URL=" + apiURL);
      Trace.kTopLevelLogger.info("GCNotify apiKEY=" + apiKEY);
      // Create connection
      connection = (HttpsURLConnection) url.openConnection();
      connection.setDoOutput(true);
      connection.setRequestMethod(BDMConstants.kPOSTRequestAttribute);
      // request header
      connection.setRequestProperty(BDMConstants.kAuthorizationAttribute,
        apiKEY);
      // BEGIN : Fix for Task-4253-AlignGCNotify-with-Interop, pass
      // Ocp-Apim-Subscription-Key as well for interop
      connection.setRequestProperty(BDMConstants.kSubscription_Key,
        Configuration
          .getProperty(EnvVars.BDM_GCNOTIFY_REST_API_SUBSCRIPTION_KEY));
      // END : Fix for Task-4253-AlignGCNotify-with-Interop, pass
      // Ocp-Apim-Subscription-Key as well for interop
      connection.setRequestProperty(BDMConstants.kContent_TypeAtribute,
        BDMConstants.kApplication_json);
      connection.setRequestProperty(BDMConstants.kAccept,
        BDMConstants.kApplication_json);
      connection.setConnectTimeout(BDMConstants.kGCNotifyConnectionTimeout);

    } catch (final MalformedURLException e) {

      final AppException urlExceptionErr = new AppException(
        BDMRESTAPIERRORMESSAGE.BDM_ERR_GCNOTIFY_MALFORMED_URL_EXCEPTION);
      Trace.kTopLevelLogger
        .debug("GCNotify Connection Error : " + urlExceptionErr.getMessage());
      throw urlExceptionErr;

    } catch (final IOException e) {

      final AppException ioExceptionErr = new AppException(
        BDMRESTAPIERRORMESSAGE.BDM_ERR_GCNOTIFY_IO_EXCEPTION);
      Trace.kTopLevelLogger
        .debug("GCNotify Connection Error : " + ioExceptionErr.getMessage());
      throw ioExceptionErr;
    } catch (final Exception e) {
      final AppException exceptionErr =
        new AppException(BDMRESTAPIERRORMESSAGE.BDM_ERR_GCNOTIFY_EXCEPTION);
      Trace.kTopLevelLogger
        .debug("GCNotify Connection Error : " + exceptionErr.getMessage());
      if (Trace.atLeast(Trace.kTraceVerbose)) {
        // If tracing is enabled, exception shown on log
        Trace.kTopLevelLogger.debug(BDMConstants.kGCNotifyAPIErrorMessage);
        Trace.kTopLevelLogger.debug(exceptionErr.getStackTrace());
      }
      throw exceptionErr;
    }

    // Log request json body
    Trace.kTopLevelLogger.info("GCNotify request :" + requestObj);

    // Prepare for response
    String responseString = CuramConst.gkEmpty;
    String gcNotifyResponseJson = CuramConst.gkEmpty; // for return
    // ResponseCode
    int responseCode;
    // Send request
    try {
      final OutputStream os = connection.getOutputStream();
      os.write(requestObj.getBytes());
      connection.connect();
      responseCode = connection.getResponseCode();
      final String responseMessage = connection.getResponseMessage();

      Trace.kTopLevelLogger.info("GCNotify ResponseCode :" + responseCode);
      Trace.kTopLevelLogger
        .info("GCNotify ResponseMessage :" + responseMessage);
      os.flush();
      if (responseCode != BDMConstants.kHTTP_STATUS_SUCCESS_CODE_201) {
        responseString = responseMessage;
      } else {
        final StringBuffer jsonResponseData = new StringBuffer();
        String readLine = null;
        final BufferedReader bufferedReader = new BufferedReader(
          new InputStreamReader(connection.getInputStream()));
        while ((readLine = bufferedReader.readLine()) != null) {
          jsonResponseData.append(readLine + "\n");
          responseString = jsonResponseData.toString();
        }
        Trace.kTopLevelLogger.info("GCNotify  Response:" + responseString);
        bufferedReader.close();
      }
      final BDMGCNotifyResponse gcnotifyResponse = new BDMGCNotifyResponse();
      gcnotifyResponse.setResponseCode(responseCode);
      gcnotifyResponse.setResponse(responseString);
      final Gson gson = new Gson();
      gcNotifyResponseJson = gson.toJson(gcnotifyResponse);

      if (gcnotifyResponse
        .getResponseCode() == BDMConstants.kHTTP_STATUS_SUCCESS_CODE_201) {
        Trace.kTopLevelLogger
          .info("GCNotify Success Response:" + BDMConstants.kSuccessMessage);

      } else if (gcnotifyResponse
        .getResponseCode() == BDMConstants.kHTTP_STATUS_BAD_REQUEST_CODE_400) {

        Trace.kTopLevelLogger.info("GCNotify Failure Response:"
          + BDMConstants.kBadRequestErrorMessage);
        final InputStream responseStream = connection.getErrorStream();
        final byte[] byteArray = new byte[responseStream.available()];
        responseStream.read(byteArray);
        Trace.kTopLevelLogger.info(new String(byteArray));

      } else if (gcnotifyResponse
        .getResponseCode() == BDMConstants.kHTTP_STATUS_BAD_REQUEST_CODE_403) {

        Trace.kTopLevelLogger.info(
          "GCNotify Failure Response:" + BDMConstants.kForbiddenErrorMessage);
      } else if (gcnotifyResponse
        .getResponseCode() == BDMConstants.kHTTP_STATUS_INTERNAL_SERVER_ERROR_500) {
        Trace.kTopLevelLogger.info("GCNotify Failure Response:"
          + BDMConstants.kRemoteGCNotifyAPIErrorMessage);
      }

    } catch (final Exception e) {
      // add error messages
      e.printStackTrace();
      Trace.kTopLevelLogger.debug("GCNotify  Error : " + e.getMessage());

    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
    return gcNotifyResponseJson;
  }

  /**
   * GCNotify API to send real time emails
   *
   * @param String , input json
   * @return String, response
   */
  @Override
  public String sendGCNotifyEmailRequest(final String jsonObj)
    throws AppException, InformationalException, IOException {

    String gcNotifyResponseJson = CuramConst.gkEmpty;

    try {
      gcNotifyResponseJson = sendGCNotifyRESTRequest(GCNOTIFY_EMAIL_API_URL,
        ENV_GCNOTIFY_API_KEY, jsonObj);

    } catch (final AppException e) {
      e.printStackTrace();
    }

    return gcNotifyResponseJson;

  }

  /**
   * GCNotify API to send real time SMS
   *
   * @param String , input json
   * @return String, response
   */
  @Override
  public String sendGCNotifySMSRequest(final String jsonObj)
    throws AppException, InformationalException, IOException {

    String gcNotifyResponseJson = CuramConst.gkEmpty;

    try {
      gcNotifyResponseJson = sendGCNotifyRESTRequest(GCNOTIFY_SMS_API_URL,
        ENV_GCNOTIFY_API_KEY, jsonObj);

    } catch (final AppException e) {
      e.printStackTrace();
    }

    return gcNotifyResponseJson;
  }

  /**
   * Update GCNotifyrequestData table based on response from GCNotify API
   *
   * @param metaDataID
   * @param Response
   * @throws AppException
   * @throws InformationalException
   */
  public void updateGCNotifyRecords(final long metaDataID,
    final String response) throws AppException, InformationalException {

    final BDMGCNotifyWhereClauseKey2 gcNotifyWhereClauseKey2 =
      new BDMGCNotifyWhereClauseKey2();
    gcNotifyWhereClauseKey2.processingDateTime =
      TransactionInfo.getSystemDateTime();

    final Gson gson = new Gson();
    final BDMGCNotifyResponse responseObj =
      gson.fromJson(response, BDMGCNotifyResponse.class);
    final int responseCode = responseObj.getResponseCode();

    String status = CuramConst.gkEmpty;
    if (responseCode == BDMConstants.kHTTP_STATUS_SUCCESS_CODE_201) {
      status = BDMGCNOTIFYNOTIFICATIONSTATUS.TRNSFD;
    } else {
      status = BDMGCNOTIFYNOTIFICATIONSTATUS.UN_PROC;
    }

    final StringBuilder sqlUpdateBuilder = new StringBuilder();

    sqlUpdateBuilder.append("UPDATE BDMGcNotifyRequestData gc ");
    sqlUpdateBuilder.append("SET gc.STATUS= '" + status + "',");
    sqlUpdateBuilder
      .append("gc.ERRORRESPONSEDESCRIPTION='" + response + "',");
    sqlUpdateBuilder
      .append("gc.ERRORRESPONSEDATETIME = :processingDateTime ");
    sqlUpdateBuilder.append(",gc.PROCESSINGDATETIME = :processingDateTime");
    sqlUpdateBuilder.append(" WHERE gc.METADATAID = " + metaDataID);

    Trace.kTopLevelLogger
      .info("BDMGCNotifyDataBatch SQL for updating records with response:"
        + sqlUpdateBuilder.toString());

    DynamicDataAccess.executeNs(null, gcNotifyWhereClauseKey2, false,
      sqlUpdateBuilder.toString());

  }

  /**
   * Populate the Real Time SMS
   *
   * @param BDMGcNotifyRequestDataDetailsList
   * @return String
   */
  @Override
  public String sendGCNotifyRTEmailRequest(
    final BDMGcNotifyRequestDataDtls requestDataDtls)
    throws AppException, InformationalException, IOException {

    final BDMGCNotifyRealTimeEmailRequest requestObj =
      new BDMGCNotifyRealTimeEmailRequest();

    // set request essential data email address and template ID
    requestObj.setEmailAddress(requestDataDtls.emailAddress);

    requestObj.setTemplateId(requestDataDtls.templateID);

    // get personalisation data
    final BDMGCNotifyHelper gcNotifyHelper = new BDMGCNotifyHelper();

    // after the for loop set the rows finally back to request object
    // requestObj.setPersonalisation(
    // gcNotifyHelper.getPersonaliseDataForTemplate(requestDataDtls));

    final Gson gson = new Gson();
    final String gcNotifyJson = gson.toJson(requestObj);
    String gcNotifyResponseJson = CuramConst.gkEmpty;

    try {
      gcNotifyResponseJson = sendGCNotifyRESTRequest(GCNOTIFY_EMAIL_API_URL,
        ENV_GCNOTIFY_API_KEY, gcNotifyJson);

      BDMGCNotifyResponse gcnotifyResponse = new BDMGCNotifyResponse();
      gcnotifyResponse =
        gson.fromJson(gcNotifyResponseJson, gcnotifyResponse.getClass());

      if (gcnotifyResponse
        .getResponseCode() != BDMConstants.kHTTP_STATUS_SUCCESS_CODE_201) {
        requestDataDtls.errorResponseDescription =
          BDMNotificationConstants.kGCNotifyRealTimeTag
            + CuramConst.gkTabDelimiter + gcNotifyResponseJson;
        requestDataDtls.errorResponseDateTime = DateTime.getCurrentDateTime();
        requestDataDtls.interfaceType =
          BDMGCNOTIFYINTERFACETYPE.BDM_GCNOTIFY_BATCH;
        requestDataDtls.status = BDMGCNOTIFYNOTIFICATIONSTATUS.UN_PROC;
        gcNotifyHelper.insertGCNotifyRequestData(requestDataDtls);
      }
    } catch (final Exception ex) {
      requestDataDtls.errorResponseDescription =
        BDMNotificationConstants.kGCNotifyRealTimeTag
          + CuramConst.gkTabDelimiter + ex.getLocalizedMessage();
      requestDataDtls.errorResponseDateTime = DateTime.getCurrentDateTime();
      requestDataDtls.interfaceType =
        BDMGCNOTIFYINTERFACETYPE.BDM_GCNOTIFY_BATCH;
      requestDataDtls.status = BDMGCNOTIFYNOTIFICATIONSTATUS.UN_PROC;
      gcNotifyHelper.insertGCNotifyRequestData(requestDataDtls);
    }
    return gcNotifyResponseJson;
  }

  /**
   * Populate the Real Time Email
   *
   * @param BDMGcNotifyRequestDataDetailsList
   * @return String
   */
  @Override
  public String
    sendGCNotifyRTSMSRequest(final BDMGcNotifyRequestDataDtls requestDataDtls)
      throws AppException, InformationalException, IOException {

    final BDMGCNotifyRealTimeSMSRequest requestObj =
      new BDMGCNotifyRealTimeSMSRequest();

    // set request essential data email address and template ID
    requestObj.setPhoneNumber(requestDataDtls.phoneNumber);

    requestObj.setTemplateId(requestDataDtls.templateID);

    // get personalisation data
    final BDMGCNotifyHelper gcNotifyHelper = new BDMGCNotifyHelper();

    // after the for loop set the rows finally back to request object
    // requestObj.setPersonalisation(
    // gcNotifyHelper.getPersonaliseDataForTemplate(requestDataDtls));

    final Gson gson = new Gson();
    final String gcNotifyJson = gson.toJson(requestObj);
    String gcNotifyResponseJson = CuramConst.gkEmpty; // for return
    try {
      gcNotifyResponseJson = sendGCNotifyRESTRequest(GCNOTIFY_SMS_API_URL,
        ENV_GCNOTIFY_API_KEY, gcNotifyJson);

      BDMGCNotifyResponse gcnotifyResponse = new BDMGCNotifyResponse();
      gcnotifyResponse =
        gson.fromJson(gcNotifyResponseJson, gcnotifyResponse.getClass());

      if (gcnotifyResponse
        .getResponseCode() != BDMConstants.kHTTP_STATUS_SUCCESS_CODE_201) {
        requestDataDtls.errorResponseDescription =
          BDMNotificationConstants.kGCNotifyRealTimeTag
            + CuramConst.gkTabDelimiter + gcNotifyResponseJson;
        requestDataDtls.errorResponseDateTime = DateTime.getCurrentDateTime();
        requestDataDtls.interfaceType =
          BDMGCNOTIFYINTERFACETYPE.BDM_GCNOTIFY_BATCH;
        requestDataDtls.status = BDMGCNOTIFYNOTIFICATIONSTATUS.UN_PROC;
        gcNotifyHelper.insertGCNotifyRequestData(requestDataDtls);
      }
    } catch (final Exception ex) {
      requestDataDtls.errorResponseDescription =
        BDMNotificationConstants.kGCNotifyRealTimeTag
          + CuramConst.gkTabDelimiter + ex.getLocalizedMessage();
      requestDataDtls.errorResponseDateTime = DateTime.getCurrentDateTime();
      requestDataDtls.interfaceType =
        BDMGCNOTIFYINTERFACETYPE.BDM_GCNOTIFY_BATCH;
      requestDataDtls.status = BDMGCNOTIFYNOTIFICATIONSTATUS.UN_PROC;
      gcNotifyHelper.insertGCNotifyRequestData(requestDataDtls);
    }
    return gcNotifyResponseJson;
  }

}
