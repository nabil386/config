package curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl;

import com.google.gson.Gson;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMRESTAPIERRORMESSAGE;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRResponseError;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRRestResponse;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRSearchTable;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRValidation;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.WSSearchTableRequest;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.WSSinValidation;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.intf.BDMWSSINValidateInterfaceIntf;
import curam.ca.gc.bdm.util.rest.impl.BDMRestUtil;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
// import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Task WSSinValidation REST API implementation consumed in the Curam
 * REST API exposed by Interops teams is consumed in Curam
 *
 * @author : Chandan Kumar
 *
 */
public class BDMWSSINValidateInterfaceImpl
  implements BDMWSSINValidateInterfaceIntf {

  public static URL url = null;

  static HttpURLConnection connection = null;

  static String REST_API_SUBSCRIPTION_KEY =
    Configuration.getProperty(EnvVars.BDM_SIN_SIR_REST_API_SUBSCRIPTION_KEY);

  static String SIN_API_URL =
    Configuration.getProperty(EnvVars.BDM_SIN_VALIDATE_REST_API_URL);

  /*
   * User Story-890 SIN/SIR Validation Service
   *
   * Agent portal and Client portal invoke this service to
   * validate Person SIN/SIR number
   */

  @Override
  public WSSinValidation validatePersonBySIN(final WSSearchTableRequest req)
    throws AppException, InformationalException, IOException {

    try {

      final StringBuffer wsSinValidateURL = new StringBuffer();
      wsSinValidateURL.append(SIN_API_URL);
      wsSinValidateURL.append(CuramConst.kQuestion);

      wsSinValidateURL.append(BDMConstants.kSIN + BDMConstants.gkEquals);
      wsSinValidateURL.append(req.getPerPersonSINIdentification());
      wsSinValidateURL.append(BDMConstants.kAmbersand);

      wsSinValidateURL
        .append(BDMConstants.kGIVEN_NAME + BDMConstants.gkEquals);
      wsSinValidateURL.append(req.getNcPersonGivenName());
      wsSinValidateURL.append(BDMConstants.kAmbersand);

      wsSinValidateURL.append(BDMConstants.kSURNAME + BDMConstants.gkEquals);
      wsSinValidateURL.append(req.getNcPersonSurName());
      wsSinValidateURL.append(BDMConstants.kAmbersand);

      wsSinValidateURL.append(BDMConstants.kDOB + BDMConstants.gkEquals);
      wsSinValidateURL.append(req.getNcPersonBirthDate());
      wsSinValidateURL.append(BDMConstants.kAmbersand);

      wsSinValidateURL.append(BDMConstants.kPARENTS + BDMConstants.gkEquals);
      wsSinValidateURL.append(req.getSsParentMaidenName());

      url = new URL(wsSinValidateURL.toString());
      connection = (HttpURLConnection) url.openConnection();

    } catch (final MalformedURLException e) {

      final AppException urlExceptionErr = new AppException(
        BDMRESTAPIERRORMESSAGE.BDM_REST_API_ERR_MALFORMED_URL_EXCEPTION);
      urlExceptionErr.arg(BDMConstants.kREST_API_VALIDATE_SIN_SIR);
      throw urlExceptionErr;

    } catch (final IOException e) {
      final AppException ioExceptionErr = new AppException(
        BDMRESTAPIERRORMESSAGE.BDM_REST_API_ERR_IO_EXCEPTION);
      ioExceptionErr.arg(BDMConstants.kREST_API_VALIDATE_SIN_SIR);
      throw ioExceptionErr;
    } catch (final Exception e) {
      final AppException exceptionErr =
        new AppException(BDMRESTAPIERRORMESSAGE.BDM_REST_API_ERR_EXCEPTION);
      exceptionErr.arg(BDMConstants.kREST_API_VALIDATE_SIN_SIR);
      if (Trace.atLeast(Trace.kTraceVerbose)) {
        // If tracing is enabled, exception shown on log
        Trace.kTopLevelLogger.debug("SIN/SIR Validation REST API Failure ");
        Trace.kTopLevelLogger.debug(exceptionErr.getStackTrace());
      }
      throw exceptionErr;
    }

    // set the request headers
    final Map<String, String> headers = new HashMap<>();
    // START, TASK-89081 : API Authentication Encryption for Outbound API
    // processing
    String decryptedAuthenticationValue = CuramConst.gkEmpty;
    if (!StringUtil.isNullOrEmpty(REST_API_SUBSCRIPTION_KEY)) {
      decryptedAuthenticationValue =
        BDMRestUtil.decryptKeys(REST_API_SUBSCRIPTION_KEY);
    }
    headers.put(BDMConstants.kSubscription_Key, decryptedAuthenticationValue);
    // END, TASK-89081 : API Authentication Encryption for Outbound API
    // processing

    headers.put(BDMConstants.kAccept, BDMConstants.kApplication_json);
    headers.put(BDMConstants.kAccept_Language,
      BDMConstants.kAddress_Search_Locale_en_CA);
    for (final String headerKey : headers.keySet()) {
      connection.setRequestProperty(headerKey, headers.get(headerKey));
    }

    WSSinValidation results = new WSSinValidation();
    final Gson gson = new Gson();

    int responseCode = 0;
    try {
      responseCode = connection.getResponseCode();
      if (responseCode == BDMConstants.kHTTP_STATUS_SUCCESS_CODE_200) {

        final StringBuilder responseBuilder = new StringBuilder();
        final BufferedReader in = new BufferedReader(
          new InputStreamReader(connection.getInputStream()));

        while (in.ready()) {
          responseBuilder.append(in.readLine());
        }

        results =
          gson.fromJson(responseBuilder.toString(), WSSinValidation.class);

        Trace.kTopLevelLogger
          .info("getSsMatchFlag=" + results.getSsMatchFlag());
        Trace.kTopLevelLogger
          .info("getSsMatchFlag=" + results.getSsMatchFlag());
        Trace.kTopLevelLogger
          .info("getSsNonMatch=" + results.getSsNonMatch());
        Trace.kTopLevelLogger.info("getNcEffectiveDate="
          + results.getSsMiscellaneous().getNcEffectiveDate());
        Trace.kTopLevelLogger.info(
          "getSsSearchTable=" + results.getSsIndividual().getSsSearchTable());

        Trace.kTopLevelLogger.info("getNcPersonLivingIndicator="
          + results.getSsDeathIndividuals().getNcPersonLivingIndicator()
          + "\n");

      }

      else if (responseCode == BDMConstants.kHTTP_STATUS_BAD_REQUEST_CODE_400) {

        final AppException exceptionErr =
          new AppException(BDMRESTAPIERRORMESSAGE.BDM_REST_API_ERR_EXCEPTION);
        exceptionErr.arg(BDMConstants.kREST_API_VALIDATE_SIN_SIR);
        if (Trace.atLeast(Trace.kTraceVerbose)) {
          // If tracing is enabled, exception shown on log
          Trace.kTopLevelLogger.debug("SIN/SIR Validation REST API Failure ");
          Trace.kTopLevelLogger.debug(exceptionErr.getStackTrace());
        }
        throw exceptionErr;

      }
    } catch (final IOException e) {
      final AppException ioExceptionErr = new AppException(
        BDMRESTAPIERRORMESSAGE.BDM_REST_API_ERR_IO_EXCEPTION);
      ioExceptionErr.arg(BDMConstants.kREST_API_VALIDATE_SIN_SIR);
      ioExceptionErr.printStackTrace();
      throw ioExceptionErr;
    }

    return results;
  }

  /**
   * User Story-890 SIN/SIR Validation Service
   *
   * @param BDMSINSIRSearchTable input data for validation
   * @return BDMSINSIRRestResponse validation response
   * if BDMSINSIRRestResponse.isSINSIRValidatonSuccess =true ,
   * BDMSINSIRValidation is populated
   * else BDMSINSIRResponseError is populated
   *
   */
  @Override
  public BDMSINSIRRestResponse
    validatePersonBySIN(final BDMSINSIRSearchTable req)
      throws AppException, InformationalException, IOException {

    final BDMSINSIRRestResponse validateSINSIRRestResponse =
      new BDMSINSIRRestResponse();
    try {

      final StringBuffer wsSinValidateURL = new StringBuffer();
      wsSinValidateURL.append(SIN_API_URL);
      wsSinValidateURL.append(CuramConst.kQuestion);

      Trace.kTopLevelLogger.debug(
        "SIN/SIR Validate SIN REST API URL : " + wsSinValidateURL.toString());

      wsSinValidateURL.append(BDMConstants.kSIN + BDMConstants.gkEquals);
      wsSinValidateURL.append(req.getPerPersonSINIdentification());
      wsSinValidateURL.append(BDMConstants.kAmbersand);

      wsSinValidateURL
        .append(BDMConstants.kGIVEN_NAME + BDMConstants.gkEquals);
      wsSinValidateURL.append(req.getNcPersonGivenName());
      wsSinValidateURL.append(BDMConstants.kAmbersand);

      wsSinValidateURL.append(BDMConstants.kSURNAME + BDMConstants.gkEquals);
      wsSinValidateURL.append(req.getNcPersonSurName());
      wsSinValidateURL.append(BDMConstants.kAmbersand);

      wsSinValidateURL.append(BDMConstants.kDOB + BDMConstants.gkEquals);
      wsSinValidateURL.append(req.getNcPersonBirthDate());
      wsSinValidateURL.append(BDMConstants.kAmbersand);

      wsSinValidateURL.append(BDMConstants.kPARENTS + BDMConstants.gkEquals);
      wsSinValidateURL.append(req.getSsParentMaidenName());

      final String newUrlString =
        wsSinValidateURL.toString().replaceAll(CuramConst.gkSpace, "%20");

      url = new URL(newUrlString);
      connection = (HttpURLConnection) url.openConnection();

    } catch (final MalformedURLException e) {
      final AppException urlExceptionErr = new AppException(
        BDMRESTAPIERRORMESSAGE.BDM_REST_API_ERR_MALFORMED_URL_EXCEPTION);
      urlExceptionErr.arg(BDMConstants.kREST_API_VALIDATE_SIN_SIR);
      throw urlExceptionErr;

    } catch (final IOException e) {
      final AppException ioExceptionErr = new AppException(
        BDMRESTAPIERRORMESSAGE.BDM_REST_API_ERR_IO_EXCEPTION);
      ioExceptionErr.arg(BDMConstants.kREST_API_VALIDATE_SIN_SIR);
      throw ioExceptionErr;
    } catch (final Exception e) {
      final AppException exceptionErr =
        new AppException(BDMRESTAPIERRORMESSAGE.BDM_REST_API_ERR_EXCEPTION);
      exceptionErr.arg(BDMConstants.kREST_API_VALIDATE_SIN_SIR);
      if (Trace.atLeast(Trace.kTraceVerbose)) {
        // If tracing is enabled, exception shown on log
        Trace.kTopLevelLogger.debug("SIN/SIR Validation REST API Failure ");
        Trace.kTopLevelLogger.debug(exceptionErr.getStackTrace());
      }
      throw exceptionErr;
    }

    // set the request headers
    final Map<String, String> headers = new HashMap<>();

    // START, TASK-89081 : API Authentication Encryption for Outbound API
    // processing
    String decryptedAuthenticationValue = CuramConst.gkEmpty;
    if (!StringUtil.isNullOrEmpty(REST_API_SUBSCRIPTION_KEY)) {
      decryptedAuthenticationValue =
        BDMRestUtil.decryptKeys(REST_API_SUBSCRIPTION_KEY);
    }
    headers.put(BDMConstants.kSubscription_Key, decryptedAuthenticationValue);
    // END, TASK-89081 : API Authentication Encryption for Outbound API
    // processing

    headers.put(BDMConstants.kAccept,
      BDMConstants.kREST_Request_Header_Accpet_JSON_NIEM_Aware);
    headers.put(BDMConstants.kAccept_Language,
      BDMConstants.kAddress_Search_Locale_en_CA);
    for (final String headerKey : headers.keySet()) {
      connection.setRequestProperty(headerKey, headers.get(headerKey));
    }

    BDMSINSIRValidation validatedSINResults = new BDMSINSIRValidation();
    BDMSINSIRResponseError sinErrorResponseResults =
      new BDMSINSIRResponseError();
    final Gson gson = new Gson();

    int responseCode = 0;
    try {
      responseCode = connection.getResponseCode();
      Trace.kTopLevelLogger
        .debug("SIN/SIR Vaidate REST API ResponseCode :" + responseCode);

      if (responseCode == BDMConstants.kHTTP_STATUS_SUCCESS_CODE_200) {

        validatedSINResults = gson.fromJson(
          getSINSIRValidationRESTResponse(connection).toString(),
          BDMSINSIRValidation.class);

        validateSINSIRRestResponse
          .setValidatedSINResults(validatedSINResults);
        // set boolean to true
        validateSINSIRRestResponse.setIsSINSIRValidatonSuccess(true);

      } else if (responseCode == BDMConstants.kHTTP_STATUS_BAD_REQUEST_CODE_400
        || responseCode == BDMConstants.kREST_ERROR_CODE_500) {

        sinErrorResponseResults = gson.fromJson(
          getSINSIRValidationRESTResponse(connection).toString(),
          BDMSINSIRResponseError.class);

        validateSINSIRRestResponse
          .setSinErrorResponseResults(sinErrorResponseResults);
        validateSINSIRRestResponse.setIsSINSIRValidatonSuccess(false);
      } else if (responseCode == BDMConstants.kREST_ERROR_CODE_404) {

        Trace.kTopLevelLogger.debug("SIN/SIR Vaidate REST API Error :"
          + getSINSIRValidationRESTResponse(connection).toString());
        validateSINSIRRestResponse.setIsSINSIRValidatonSuccess(false);

      }
    } catch (final IOException e) {
      final AppException ioExceptionErr = new AppException(
        BDMRESTAPIERRORMESSAGE.BDM_REST_API_ERR_IO_EXCEPTION);
      ioExceptionErr.arg(BDMConstants.kREST_API_VALIDATE_SIN_SIR);
      throw ioExceptionErr;
    }

    return validateSINSIRRestResponse;

  }

  /**
   * Constructs the Response json for SIN/SIR validation
   *
   * @param connection
   * @return
   * @throws IOException
   */
  StringBuilder getSINSIRValidationRESTResponse(
    final HttpURLConnection httpConn) throws IOException {

    final StringBuilder responseBuilder = new StringBuilder();
    InputStream inputStreamObj;
    if (httpConn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
      Trace.kTopLevelLogger
        .info("SIN/SIR Passed ,  Response Code:" + httpConn.getResponseCode()
          + " Success message :" + httpConn.getResponseMessage());
      inputStreamObj = httpConn.getInputStream();
    } else {
      /* error from server */
      Trace.kTopLevelLogger
        .info("SIN/SIR  Failed, Response Code:" + httpConn.getResponseCode()
          + " Error message :" + httpConn.getResponseMessage());
      inputStreamObj = httpConn.getErrorStream();
    }

    final BufferedReader in =
      new BufferedReader(new InputStreamReader(inputStreamObj));

    while (in.ready()) {
      responseBuilder.append(in.readLine());
    }
    return responseBuilder;
  }

}
