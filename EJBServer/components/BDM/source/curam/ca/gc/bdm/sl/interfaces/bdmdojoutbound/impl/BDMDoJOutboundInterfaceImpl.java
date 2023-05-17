package curam.ca.gc.bdm.sl.interfaces.bdmdojoutbound.impl;

import com.google.gson.Gson;
import curam.ca.gc.bdm.entity.struct.BDMDoJOutboundStageDtls;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMRESTAPIERRORMESSAGE;
import curam.ca.gc.bdm.sl.interfaces.bdmdojoutbound.intf.BDMDoJOutboundInterfaceIntf;
import curam.ca.gc.bdm.sl.interfaces.dojoutbound.impl.bdmdojoutboundpojos.BDMWSDoJOutbound;
import curam.ca.gc.bdm.sl.interfaces.dojoutbound.impl.bdmdojoutboundpojos.Metadata;
import curam.ca.gc.bdm.sl.interfaces.dojoutbound.impl.bdmdojoutboundpojos.Obligation;
import curam.ca.gc.bdm.sl.interfaces.dojoutbound.impl.bdmdojoutboundpojos.Person;
import curam.ca.gc.bdm.sl.interfaces.dojoutbound.impl.bdmsystemeventpojos.BDMDoJOutboundSystemEvent;
import curam.ca.gc.bdm.sl.interfaces.gcnotify.impl.gcnotifypojos.BDMGCNotifyResponse;
import curam.ca.gc.bdm.util.payment.impl.BDMPaymentUtil;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.Trace;
import curam.util.type.Date;
import curam.util.type.DateTime;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.net.ssl.HttpsURLConnection;

/*
 * Task 14251 DoJ Outbound REST API exposed by Interops teams and is its
 * consumed in Curam application and send the curam diverted amount data as
 * payload to interop.
 * The interop will received the response as input and they will update to the
 * SPS file.
 */

public class BDMDoJOutboundInterfaceImpl
  implements BDMDoJOutboundInterfaceIntf {

  public static URL url = null;

  static HttpURLConnection connection = null;

  static String WS_DOJ_OUTBOUND_API_URL =
    Configuration.getProperty(EnvVars.BDM_WS_DOJ_OUTBOUND_API_URL);

  static String WS_DOJ_OUTBOUND_SYSTEM_EVENT_API_URL = Configuration
    .getProperty(EnvVars.BDM_WS_DOJ_OUTBOUND_SYSTEM_EVENT_API_URL);

  static String REST_API_SUBSCRIPTION_KEY = Configuration
    .getProperty(EnvVars.BDM_WS_INTEROP_REST_API_SUBSCRIPTION_KEY);

  @Override
  public String sendPayload(final String apiURL, final String apiKEY,
    final String requestObj)
    throws AppException, InformationalException, IOException {

    HttpsURLConnection connection = null;
    try {
      final URL url = new URL(apiURL);

      // Create connection
      connection = (HttpsURLConnection) url.openConnection();
      connection.setDoOutput(true);
      connection.setRequestMethod(BDMConstants.kPOSTRequestAttribute);

      connection.setRequestProperty(BDMConstants.kSubscription_Key,
        REST_API_SUBSCRIPTION_KEY);
      connection.setRequestProperty(BDMConstants.kContent_TypeAtribute,
        BDMConstants.kApplication_json);
      connection.setRequestProperty(BDMConstants.kAccept,
        BDMConstants.kApplication_json);
      connection.setConnectTimeout(BDMConstants.kGCNotifyConnectionTimeout);

    } catch (final MalformedURLException e) {

      final AppException urlExceptionErr = new AppException(
        BDMRESTAPIERRORMESSAGE.BDM_ERR_GCNOTIFY_MALFORMED_URL_EXCEPTION);
      Trace.kTopLevelLogger
        .debug(" Connection Error : " + urlExceptionErr.getMessage());
      throw urlExceptionErr;

    } catch (final IOException e) {

      final AppException ioExceptionErr = new AppException(
        BDMRESTAPIERRORMESSAGE.BDM_ERR_GCNOTIFY_IO_EXCEPTION);
      Trace.kTopLevelLogger
        .debug("Connection Error : " + ioExceptionErr.getMessage());
      throw ioExceptionErr;
    } catch (final Exception e) {
      final AppException exceptionErr =
        new AppException(BDMRESTAPIERRORMESSAGE.BDM_ERR_GCNOTIFY_EXCEPTION);
      Trace.kTopLevelLogger
        .debug("Connection Error : " + exceptionErr.getMessage());
      if (Trace.atLeast(Trace.kTraceVerbose)) {

        Trace.kTopLevelLogger.debug(BDMConstants.kGCNotifyAPIErrorMessage);
        Trace.kTopLevelLogger.debug(exceptionErr.getStackTrace());
      }
      throw exceptionErr;
    }

    Trace.kTopLevelLogger.info("\nPayload body :" + requestObj + "\n");

    // Prepare for response
    String responseString = CuramConst.gkEmpty;
    String dojOutboundResponseJson = CuramConst.gkEmpty; // for return

    int responseCode;
    // Send request
    try {
      final OutputStream os = connection.getOutputStream();
      os.write(requestObj.getBytes());
      connection.connect();
      responseCode = connection.getResponseCode();
      final String responseMessage = connection.getResponseMessage();
      Trace.kTopLevelLogger.info("ResponseCode :" + responseCode);
      Trace.kTopLevelLogger.info("ResponseMessage :" + responseMessage);
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
        Trace.kTopLevelLogger.info("Response:" + responseString);
        bufferedReader.close();
      }
      final BDMGCNotifyResponse dojOutboundResponse =
        new BDMGCNotifyResponse();
      dojOutboundResponse.setResponseCode(responseCode);
      dojOutboundResponse.setResponse(responseString);
      final Gson gson = new Gson();
      dojOutboundResponseJson = gson.toJson(dojOutboundResponse);

      if (dojOutboundResponse
        .getResponseCode() == BDMConstants.kHTTP_STATUS_SUCCESS_CODE_201) {
        Trace.kTopLevelLogger
          .info("Success Response:" + BDMConstants.kSuccessMessage);

      } else if (dojOutboundResponse
        .getResponseCode() == BDMConstants.kHTTP_STATUS_BAD_REQUEST_CODE_400) {

        Trace.kTopLevelLogger
          .info("Failure Response:" + BDMConstants.kBadRequestErrorMessage);
      } else if (dojOutboundResponse
        .getResponseCode() == BDMConstants.kHTTP_STATUS_BAD_REQUEST_CODE_403) {

        Trace.kTopLevelLogger
          .info("Failure Response:" + BDMConstants.kForbiddenErrorMessage);
      } else if (dojOutboundResponse
        .getResponseCode() == BDMConstants.kHTTP_STATUS_INTERNAL_SERVER_ERROR_500) {
        Trace.kTopLevelLogger.info("Failure Response:"
          + BDMConstants.kHTTP_STATUS_INTERNAL_SERVER_ERROR_500);
      }

    } catch (final Exception e) {
      // add error messages
      e.printStackTrace();
      Trace.kTopLevelLogger.debug("Error : " + e.getMessage());

    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
    return dojOutboundResponseJson;
  }

  // create doj outbound request payload
  @Override
  public void createObligationRequest(
    final BDMDoJOutboundStageDtls payloadDtls,
    final int processedInstrumentsCount)
    throws ParseException, AppException, InformationalException, IOException {

    final BDMWSDoJOutbound wsObj = new BDMWSDoJOutbound();
    final Metadata metadata = new Metadata();
    metadata.setDocumentFileControlID(payloadDtls.documentFileControlID);
    final String filDate = dateFormatter(Date.getCurrentDate().toString());

    metadata.setDocumentFileDate(filDate);
    metadata.setDocumentFileEnvironmentType(
      payloadDtls.documentFileEnvironmentType);
    metadata.setDocumentFileName(payloadDtls.documentFileName);
    metadata.setDocumentFileWeekCode(payloadDtls.documentFileWeekCode);
    metadata.setDocumentFileDayCode(payloadDtls.documentFileDayCode);
    metadata.setDocumentSource(payloadDtls.documentSource);
    metadata.setDocumentFileTime(
      timeFormatter(DateTime.getCurrentDateTime().toString()));
    metadata.setDocumentRecipient(BDMConstants.kDocumentRecipient);
    metadata
      .setMetadataIdentificationID(payloadDtls.metadataIdentificationID);
    metadata.setTransactionControlIdentificationID(
      payloadDtls.transactionControlIdentificationID);

    wsObj.setMetadata(metadata);

    final Obligation obligationObj = new Obligation();

    obligationObj
      .setMetadataIdentificationID(payloadDtls.metadataIdentificationID);
    obligationObj.setRecordID(processedInstrumentsCount);

    obligationObj.setObligationAmountOfFeeIncluded(
      payloadDtls.obligationAmtOfFeeIncluded.getValue());

    obligationObj.setObligationArrearsBalance(
      payloadDtls.obligationArrearsBalance.getValue());

    obligationObj.setObligationAmountPerDebt(
      payloadDtls.obligationAmtOfFeeIncluded.getValue());

    obligationObj.setObligationDebtPercentage(
      Integer.valueOf(payloadDtls.obligationDebtPercentage));

    obligationObj.setObligationAmountPerDebt(
      payloadDtls.obligationAmountPerDebt.getValue());

    obligationObj.setObligationDivertAmount(
      payloadDtls.obligationDivertAmount.getValue());
    obligationObj.setObligationDebtRatioPercentage(
      payloadDtls.obligationDivertAmount.getValue());

    obligationObj
      .setObligationFixedAmount(payloadDtls.obligationFixedAmount.getValue());
    obligationObj.setObligationFixedAmountIndicator(
      payloadDtls.obligationFixedAmountIND ? 1 : 0);
    obligationObj.setObligationFundsAvailable(
      payloadDtls.obligationFundsAvailable.getValue());

    obligationObj
      .setObligationNumberOfWeek(payloadDtls.obligationNumberOfWeek.isEmpty()
        ? 0 : Integer.valueOf(payloadDtls.obligationNumberOfWeek));

    obligationObj
      .setObligationPaidAmount(payloadDtls.obligationPaidAmount.getValue());
    obligationObj.setObligationOutstandingFeeBalance(
      payloadDtls.obligationOutstandingFeeBal.getValue());
    obligationObj.setObligationPaymentAmount(
      payloadDtls.obligationPaymentAmount.getValue());

    obligationObj.setObligationItcCode(payloadDtls.obligationITCCode);
    obligationObj
      .setObligationOCONRegionalCode(payloadDtls.obligationOCONRegionalCode);
    obligationObj.setObligationCanadianProvinceCode(
      payloadDtls.obligationCanadianProvinceCode);
    obligationObj
      .setObligationDebtSequenceNumber(payloadDtls.obligationDebtSeqNumber);
    obligationObj.setObligationPaymentDate(dateFormatterWithoutSlash(
      payloadDtls.obligationPaymentDate.toString()));

    obligationObj.setObligationVendorCode(payloadDtls.obligationVendorCode);
    obligationObj.setObligationException(payloadDtls.obligationExceptionCode);
    wsObj.setObligation(obligationObj);

    final Person personObj = new Person();
    personObj.setPersonObligationID(payloadDtls.personObligationID);
    personObj
      .setPersonObligationIDSuffix(payloadDtls.personObligationIDSuffix);
    personObj.setPersonSINIdentification(payloadDtls.personSINIdentification);
    obligationObj.setPerson(personObj);

    final Gson gson = new Gson();
    sendPayload(WS_DOJ_OUTBOUND_API_URL, REST_API_SUBSCRIPTION_KEY,
      gson.toJson(wsObj));

  }

  // create doj outbound system event request payload
  @Override
  public void createSystemEventRequest(final int transactionCount)
    throws AppException, InformationalException, IOException, ParseException {

    final BDMPaymentUtil pmtUtil = new BDMPaymentUtil();

    final BDMDoJOutboundSystemEvent dojOutboundReq =
      new BDMDoJOutboundSystemEvent();
    dojOutboundReq.setBatchId(pmtUtil.getGuidNumber());
    dojOutboundReq.setEventDestination(BDMConstants.kEventDestination);
    dojOutboundReq.setEventSource(BDMConstants.kEventSource);
    dojOutboundReq.setStatus(BDMConstants.kStatus);
    dojOutboundReq.setEventType(BDMConstants.kEventType);
    dojOutboundReq.setProgramId(BDMConstants.kProgramId);
    dojOutboundReq.setTimeStamp(
      dateTimeFormatter(DateTime.getCurrentDateTime().toString()));
    dojOutboundReq.setMessage(CuramConst.gkEmpty);
    dojOutboundReq.setTransactionCount(transactionCount);
    final Gson gson = new Gson();

    Trace.kTopLevelLogger.debug(
      "\nSend total transaction count to DoJ Outbound System Event:\n");

    sendPayload(WS_DOJ_OUTBOUND_SYSTEM_EVENT_API_URL,
      REST_API_SUBSCRIPTION_KEY, gson.toJson(dojOutboundReq));

  }

  private static String dateFormatter(final String inputDate)
    throws ParseException {

    final SimpleDateFormat inSDF = new SimpleDateFormat(
      BDMConstants.MM_DD_YYYY_DATE_FORMAT_SLASH_DELIMITER);
    final SimpleDateFormat outSDF = new SimpleDateFormat(
      BDMConstants.YYYY_MM_DD_DATE_FORMAT_DASH_DELIMITER);

    String outDate = "";
    if (inputDate != null) {
      try {
        final java.util.Date date = inSDF.parse(inputDate);
        outDate = outSDF.format(date);
      } catch (final ParseException ex) {
      }
    }

    return outDate;
  }

  private static String dateFormatterWithoutSlash(final String inputDate)
    throws ParseException {

    final SimpleDateFormat inSDF = new SimpleDateFormat(
      BDMConstants.MM_DD_YYYY_DATE_FORMAT_SLASH_DELIMITER);
    final SimpleDateFormat outSDF =
      new SimpleDateFormat(BDMConstants.DD_MM_YYYY);

    String outDate = "";
    if (inputDate != null) {
      try {
        final java.util.Date date = inSDF.parse(inputDate);
        outDate = outSDF.format(date);
      } catch (final ParseException ex) {
      }
    }

    return outDate;
  }

  private static String dateTimeFormatter(final String inputDate)
    throws ParseException {

    final SimpleDateFormat inSDF = new SimpleDateFormat(
      BDMConstants.MM_DD_YYYY_HH_MM_SS_DATE_FORMAT_SLASH_DELIMITER1);
    final SimpleDateFormat outSDF = new SimpleDateFormat(
      BDMConstants.YYYY_MM_DD_HH_MM_SS_SSS_DATE_FORMAT_DASH_DELIMITER);

    String outDateTime = "";
    if (inputDate != null) {
      try {
        final java.util.Date date = inSDF.parse(inputDate);
        outDateTime = outSDF.format(date);
      } catch (final ParseException ex) {
      }
    }

    return outDateTime;
  }

  private static String timeFormatter(final String inputDate)
    throws ParseException {

    final SimpleDateFormat inSDF = new SimpleDateFormat(
      BDMConstants.MM_DD_YYYY_HH_MM_SS_DATE_FORMAT_SLASH_DELIMITER1);
    final SimpleDateFormat outSDF =
      new SimpleDateFormat(BDMConstants.TWENTY_FOUR_HOUR_TIME_FORMAT);

    String outDateTime = "";
    if (inputDate != null) {
      try {
        final java.util.Date date = inSDF.parse(inputDate);
        outDateTime = outSDF.format(date);
      } catch (final ParseException ex) {
      }
    }

    return outDateTime;
  }
}
