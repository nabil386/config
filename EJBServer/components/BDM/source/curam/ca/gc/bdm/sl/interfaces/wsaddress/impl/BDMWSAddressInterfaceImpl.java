package curam.ca.gc.bdm.sl.interfaces.wsaddress.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMRESTAPIERRORMESSAGE;
import curam.ca.gc.bdm.message.impl.BDMRESTAPIERRORMESSAGEExceptionCreator;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.validatepojos.WSAddressValidate;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.validatepojos.WsaddrValidationRequest;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.wsaddresspojos.WSAddress;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.wsaddresspojos.WsaddrSearchRequest;
import curam.ca.gc.bdm.util.rest.impl.BDMRestUtil;
import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressConstants;
import curam.codetable.LOCALE;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Task 8264 WSAddress REST API outbound implementation
 * REST API exposed by Interops teams is consumed in Curam
 *
 *
 */
public class BDMWSAddressInterfaceImpl implements
  curam.ca.gc.bdm.sl.interfaces.wsaddress.intf.BDMWSAddressInterfaceIntf {

  public static URL url = null;

  static HttpURLConnection connection = null;

  static String WS_ADDRESS_REST_SEARCH_BY_POSTALCODE_API_URL = Configuration
    .getProperty(EnvVars.BDM_WS_ADDRESS_REST_SEARCH_BY_POSTALCODE_API_URL);

  static String WS_ADDRESS_REST_VALIDATE_ADDRESS_API_URL = Configuration
    .getProperty(EnvVars.BDM_WS_ADDRESS_REST_VALIDATE_ADDRESS_API_URL);

  static String WS_ADDRESS_REST_API_SUBSCRIPTION_KEY = Configuration
    .getProperty(EnvVars.BDM_WS_ADDRESS_REST_API_SUBSCRIPTION_KEY);

  /**
   * Search Addresses by Postal code
   * REST API exposed by Interops team is called
   *
   * @param addressSearchKey
   * @return WSAddress
   * @throws AppException
   */
  @Override
  public WSAddress
    searchAddressesByPostalCode(final WsaddrSearchRequest addrSearchRequest)
      throws AppException, InformationalException {

    // TODO Remove this later, after UI is fixed
    // addrSearchRequest.setNcAddressPostalCode("C0A1P0");

    // If no language populated, fetch it from current transaction
    if (StringUtil.isNullOrEmpty(addrSearchRequest.getNcLanguageName())) {
      addrSearchRequest.setNcLanguageName(getLanguageForAddresSearch());
    }

    // validation if Canada address verification
    validateQueryParamsForSearchAddress(
      addrSearchRequest.getNcAddressPostalCode(),
      addrSearchRequest.getNcCountryCode(),
      addrSearchRequest.getNcLanguageName());

    try {
      // Remove whitespace in Postal code
      String postalCode = addrSearchRequest.getNcAddressPostalCode().trim();
      if (!StringUtil.isNullOrEmpty(postalCode) && addrSearchRequest
        .getNcAddressPostalCode().contains(CuramConst.gkSpace)) {
        postalCode = postalCode.replaceAll(BDMConstants.kWhiteSpaceRegEx,
          CuramConst.gkEmpty);
      }

      final StringBuffer wsAddressURL = new StringBuffer();
      wsAddressURL.append(WS_ADDRESS_REST_SEARCH_BY_POSTALCODE_API_URL);
      wsAddressURL
        .append(CuramConst.kQuestion + BDMConstants.kAddressPostalCode
          + BDMConstants.gkEquals + postalCode);
      wsAddressURL
        .append(BDMConstants.kAmbersand + BDMConstants.kReturnAddressLine
          + BDMConstants.gkEquals + CuramConst.gkTrue);

      url = new URL(wsAddressURL.toString());
      /*
       * Trace.kTopLevelLogger
       * .debug("WSAddress REST URL = " + wsAddressURL.toString());
       */ connection = (HttpURLConnection) url.openConnection();
    } catch (final MalformedURLException e) {

      final AppException urlExceptionErr = new AppException(
        BDMRESTAPIERRORMESSAGE.BDM_REST_API_ERR_MALFORMED_URL_EXCEPTION);
      urlExceptionErr.arg(BDMConstants.kREST_API_WSADDRESS);
      throw urlExceptionErr;

    } catch (final IOException e) {
      Trace.kTopLevelLogger
        .info("WSAddress Exception  = " + e.getStackTrace());
      final AppException ioExceptionErr = new AppException(
        BDMRESTAPIERRORMESSAGE.BDM_REST_API_ERR_IO_EXCEPTION);
      ioExceptionErr.arg(BDMConstants.kREST_API_WSADDRESS);
      throw ioExceptionErr;

    } catch (final Exception e) {

      final AppException exceptionErr =
        new AppException(BDMRESTAPIERRORMESSAGE.BDM_REST_API_ERR_EXCEPTION);
      exceptionErr.arg(BDMConstants.kREST_API_WSADDRESS);
      if (Trace.atLeast(Trace.kTraceVerbose)) {
        // If tracing is enabled, exception shown on log
        Trace.kTopLevelLogger
          .debug("WEBSERVICE: WSAddress constructed XML payload");
        Trace.kTopLevelLogger.debug(exceptionErr.getStackTrace());
      }
      throw exceptionErr;
    }

    // set the request headers
    final Map<String, String> headers = new HashMap<>();
    // START, TASK-89081 : API Authentication Encryption for Outbound API
    // processing
    String decryptedAuthenticationValue = CuramConst.gkEmpty;
    if (!StringUtil.isNullOrEmpty(WS_ADDRESS_REST_API_SUBSCRIPTION_KEY)) {
      decryptedAuthenticationValue =
        BDMRestUtil.decryptKeys(WS_ADDRESS_REST_API_SUBSCRIPTION_KEY);
    }
    headers.put(BDMConstants.kSubscription_Key, decryptedAuthenticationValue);
    // END, TASK-89081 : API Authentication Encryption for Outbound API
    // processing
    headers.put(BDMConstants.kAccept, BDMConstants.kRestContentTypeAll);
    headers.put(BDMConstants.kAccept_Language,
      addrSearchRequest.getNcLanguageName());
    for (final String headerKey : headers.keySet()) {
      connection.setRequestProperty(headerKey, headers.get(headerKey));
    }

    // call the WSAddress API and populate return Address results
    WSAddress results = new WSAddress();
    int responseCode = 0;
    try {
      responseCode = connection.getResponseCode();
      Trace.kTopLevelLogger
        .debug("WSAddress REST Call ResponseCode = " + responseCode);
      if (responseCode == BDMConstants.kHTTP_STATUS_SUCCESS_CODE_200) {
        final StringBuilder responseBuilder = new StringBuilder();
        final BufferedReader in = new BufferedReader(
          new InputStreamReader(connection.getInputStream()));

        while (in.ready()) {
          responseBuilder.append(in.readLine());
        }

        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        results = gson.fromJson(responseBuilder.toString(), WSAddress.class);

        /*
         * Trace.kTopLevelLogger
         * .debug("WSADDRESS SEARCH REST Response = " + gson.toJson(results));
         */

        // If Status 200 , It doesn't guarantee the Address found , As per the
        // API spec check if there are any error
        if (results != null && !results.getWsaddrSearchResults()
          .getWsaddrInformation().getWsaddrStatusCode()
          .equalsIgnoreCase(BDMWSAddressConstants.kWSAddressRecordsFound)) {
          Trace.kTopLevelLogger.debug("WSAddress Search result StatusCode : "
            + results.getWsaddrSearchResults().getWsaddrInformation()
              .getWsaddrStatusCode());
          Trace.kTopLevelLogger.debug("WSAddress Search result MessageText : "
            + results.getWsaddrSearchResults().getWsaddrInformation()
              .getNcMessageText());

          final AppException exceptionErr = new AppException(
            BDMRESTAPIERRORMESSAGE.BDM_WSADDRESS_SEARCH_BY_POSTALCODE_REST_API_ERR_EXCEPTION);
          exceptionErr.arg(BDMConstants.kREST_API_WSADDRESS);
          exceptionErr.arg("" + results.getWsaddrSearchResults()
            .getWsaddrInformation().getWsaddrStatusCode() + "");
          exceptionErr.arg("" + results.getWsaddrSearchResults()
            .getWsaddrInformation().getNcMessageText() + "");

          throw exceptionErr;
        }

      } else if (responseCode == BDMConstants.kHTTP_STATUS_TIME_OUT_CODE_504) {
        Trace.kTopLevelLogger
          .info("WSADDRESS Search by postal code Time out error::"
            + connection.getErrorStream());
        final AppException exceptionErr = new AppException(
          BDMRESTAPIERRORMESSAGE.BDM_WSADDRESS_SEARCH_BY_POSTALCODE_ERR_504_GATEWAY_TIMEOUT_EXCEPTION);
        throw exceptionErr;
        // BEGIN TASK-28504 Map Correct Error Message for Response Code -400
      } else if (responseCode == BDMConstants.kHTTP_STATUS_TIME_OUT_CODE_400) {
        Trace.kTopLevelLogger
          .info("WSADDRESS Search by postal code Invalid Request Data::"
            + connection.getErrorStream());
        final AppException exceptionErr = new AppException(
          BDMRESTAPIERRORMESSAGE.BDM_WSADDRESS_SEARCH_BY_POSTALCODE_REST_API_ERR_EXCEPTION_INVALID_ADDRESS_SEARCH_DATA);
        exceptionErr.arg(BDMConstants.kREST_API_WSADDRESS);
        // throw exceptionErr; AS per discussion with BA and FDD we are not
        // suppose to display any error
      } else {
        Trace.kTopLevelLogger
          .info("WSADDRESS SEARCH BY POST CODE ERROR Status code :::"
            + responseCode);
        final AppException exceptionErr = new AppException(
          BDMRESTAPIERRORMESSAGE.BDM_WSADDRESS_SEARCH_BY_POSTALCODE_ERR_504_GATEWAY_TIMEOUT_EXCEPTION);
        exceptionErr.arg(BDMConstants.kREST_API_WSADDRESS);

        final StringBuilder responseBuilder = new StringBuilder();
        final InputStream inputStreamObj = connection.getErrorStream();
        final BufferedReader in =
          new BufferedReader(new InputStreamReader(inputStreamObj));
        while (in.ready()) {
          responseBuilder.append(in.readLine());
        }

        Trace.kTopLevelLogger
          .debug("WSADDRESS SEARCH BY POST CODE  Error  Response json :"
            + responseBuilder.toString());
        throw exceptionErr;
      }
    } catch (final IOException e) {
      Trace.kTopLevelLogger
        .info("WSAddress IO Exception  = " + e.getStackTrace());
      final AppException ioExceptionErr = new AppException(
        BDMRESTAPIERRORMESSAGE.BDM_REST_API_ERR_IO_EXCEPTION);
      ioExceptionErr.arg(BDMConstants.kREST_API_WSADDRESS);
      throw ioExceptionErr;

    }
    return results;
  }

  /**
   * Gets the language. If the language is not provided language of the
   * transaction is
   * return.
   *
   * @param addressSearchKey the address search key
   * @return the language
   */
  private String getLanguageForAddresSearch() {

    final String programLocale =
      TransactionInfo.getProgramLocale().toLowerCase();
    String searchAddressLanguageRequest = "";
    if (programLocale.equalsIgnoreCase(LOCALE.ENGLISH)
      || programLocale.equalsIgnoreCase(LOCALE.ENGLISH_CA)) {
      searchAddressLanguageRequest =
        BDMConstants.kAddress_Search_Locale_en_CA;
    } else if (programLocale.equalsIgnoreCase(LOCALE.FRENCH)) {
      searchAddressLanguageRequest =
        BDMConstants.kAddress_Search_Locale_fr_CA;
    } else {
      searchAddressLanguageRequest =
        BDMConstants.kAddress_Search_Locale_en_CA;
    }
    return searchAddressLanguageRequest;
  }

  /**
   * Validate query params for search address.
   *
   * @param postalCode the postal code
   * @param country the country
   * @param language the language
   * @throws AppException the app exception
   */
  private static void validateQueryParamsForSearchAddress(
    final String postalCode, final String country, final String language)
    throws AppException {

    if (StringUtil.isNullOrEmpty(postalCode)
      || StringUtil.isNullOrEmpty(country)
      || !country.toUpperCase().equals(BDMConstants.kCountryCanada)
      || !(language.equals(BDMConstants.kAddress_Search_Locale_en_CA)
        || language.equals(BDMConstants.kAddress_Search_Locale_fr_CA))) {
      BDMRESTAPIERRORMESSAGEExceptionCreator
        .HTTP_400_SEARCH_ADDRESS_QUERY_PARAMS_NOT_VALID().printStackTrace();
      throw BDMRESTAPIERRORMESSAGEExceptionCreator
        .HTTP_400_SEARCH_ADDRESS_QUERY_PARAMS_NOT_VALID();
    }

  }

  private static void validateQueryParamsForValidateAddress(
    final String provinceCode, final String cityName,
    final String fullAddressText) throws AppException {

    if (StringUtil.isNullOrEmpty(provinceCode)
      || StringUtil.isNullOrEmpty(cityName)
      || StringUtil.isNullOrEmpty(fullAddressText)) {
      throw BDMRESTAPIERRORMESSAGEExceptionCreator
        .HTTP_400_VALIDATE_ADDRESS_QUERY_PARAMS_NOT_VALID();
    }
  }

  @Override
  public boolean
    validateAddress(final WsaddrValidationRequest addrValidationRequest)
      throws AppException, InformationalException {

    boolean isValidAddress = false;

    // If no language populated, fetch it from current transaction
    if (StringUtil.isNullOrEmpty(addrValidationRequest.getNcLanguageName())) {
      addrValidationRequest.setNcLanguageName(getLanguageForAddresSearch());
    }

    // validation if Canada address verification
    validateQueryParamsForSearchAddress(
      addrValidationRequest.getNcAddressPostalCode(),
      addrValidationRequest.getNcCountryCode(),
      addrValidationRequest.getNcLanguageName());
    validateQueryParamsForValidateAddress(
      addrValidationRequest.getCanProvinceCode(),
      addrValidationRequest.getNcAddressCityName(),
      addrValidationRequest.getNcAddressFullText());

    try {

      final StringBuffer wsAddressURL = new StringBuffer();

      wsAddressURL.append(WS_ADDRESS_REST_VALIDATE_ADDRESS_API_URL);
      wsAddressURL.append(CuramConst.kQuestion);
      wsAddressURL
        .append(BDMConstants.kAddressCityName + BDMConstants.gkEquals);
      wsAddressURL.append(
        URLEncoder.encode(addrValidationRequest.getNcAddressCityName(),
          StandardCharsets.UTF_8.toString()));
      wsAddressURL.append(BDMConstants.kAmbersand);
      wsAddressURL
        .append(BDMConstants.kAddressFullText + BDMConstants.gkEquals);
      wsAddressURL.append(
        URLEncoder.encode(addrValidationRequest.getNcAddressFullText(),
          StandardCharsets.UTF_8.toString()));
      wsAddressURL.append(BDMConstants.kAmbersand);
      wsAddressURL
        .append(BDMConstants.kAddressPostalCode + BDMConstants.gkEquals);
      wsAddressURL.append(
        URLEncoder.encode(addrValidationRequest.getNcAddressPostalCode(),
          StandardCharsets.UTF_8.toString()));
      wsAddressURL.append(BDMConstants.kAmbersand);
      wsAddressURL.append(BDMConstants.kProvinceCode + BDMConstants.gkEquals);
      wsAddressURL.append(addrValidationRequest.getCanProvinceCode());

      /*
       * Trace.kTopLevelLogger
       * .debug("WSADDRESS Validate REST URL = " + wsAddressURL.toString());
       */
      url = new URL(wsAddressURL.toString());
      connection = (HttpURLConnection) url.openConnection();
    } catch (final MalformedURLException e) {

      final AppException urlExceptionErr = new AppException(
        BDMRESTAPIERRORMESSAGE.BDM_REST_API_ERR_MALFORMED_URL_EXCEPTION);
      urlExceptionErr.arg(BDMConstants.kREST_API_WSADDRESS);
      throw urlExceptionErr;

    } catch (final IOException e) {
      Trace.kTopLevelLogger
        .info("Validate WSAddress Exception  = " + e.getStackTrace());
      final AppException ioExceptionErr = new AppException(
        BDMRESTAPIERRORMESSAGE.BDM_REST_API_ERR_IO_EXCEPTION);
      ioExceptionErr.arg(BDMConstants.kREST_API_WSADDRESS);
      throw ioExceptionErr;

    } catch (final Exception e) {

      final AppException exceptionErr =
        new AppException(BDMRESTAPIERRORMESSAGE.BDM_REST_API_ERR_EXCEPTION);
      exceptionErr.arg(BDMConstants.kREST_API_WSADDRESS);
      if (Trace.atLeast(Trace.kTraceVerbose)) {
        // If tracing is enabled, exception shown on log
        Trace.kTopLevelLogger
          .debug("WEBSERVICE: validate Address REST API Failure");
        Trace.kTopLevelLogger.debug(exceptionErr.getStackTrace());
      }
      throw exceptionErr;
    }

    // set the request headers
    final Map<String, String> headers = new HashMap<>();

    // START, TASK-89081 : API Authentication Encryption for Outbound API
    // processing
    String decryptedAuthenticationValue = CuramConst.gkEmpty;
    if (!StringUtil.isNullOrEmpty(WS_ADDRESS_REST_API_SUBSCRIPTION_KEY)) {
      decryptedAuthenticationValue =
        BDMRestUtil.decryptKeys(WS_ADDRESS_REST_API_SUBSCRIPTION_KEY);
    }
    headers.put(BDMConstants.kSubscription_Key, decryptedAuthenticationValue);
    // END, TASK-89081 : API Authentication Encryption for Outbound API
    // processing

    // headers.put(BDMConstants.kAccept, BDMConstants.kApplication_json);
    headers.put(BDMConstants.kAccept, BDMConstants.kRestContentTypeAll);
    headers.put(BDMConstants.kAccept_Language,
      addrValidationRequest.getNcLanguageName());
    for (final String headerKey : headers.keySet()) {
      connection.setRequestProperty(headerKey, headers.get(headerKey));
    }

    // call the WSAddress API and populate return Addressresults
    WSAddressValidate results = new WSAddressValidate();
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

        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        results =
          gson.fromJson(responseBuilder.toString(), WSAddressValidate.class);
        /*
         * Trace.kTopLevelLogger.debug(
         * "WSADDRESS Validate REST Response = " + gson.toJson(results));
         */
      } else if (responseCode == BDMConstants.kHTTP_STATUS_TIME_OUT_CODE_504) {
        Trace.kTopLevelLogger.info("WSADDRESS Validate  Time out error::"
          + connection.getErrorStream());
        final AppException exceptionErr = new AppException(
          BDMRESTAPIERRORMESSAGE.BDM_WSADDRESS_VALIDATE_ERR_504_GATEWAY_TIMEOUT_EXCEPTION);
        throw exceptionErr;
      } else { // For any other error

        Trace.kTopLevelLogger
          .info("WSADDRESS VALIDATE ERROR Status code :::" + responseCode);
        final AppException exceptionErr = new AppException(
          BDMRESTAPIERRORMESSAGE.BDM_WSADDRESS_SEARCH_BY_POSTALCODE_REST_API_ERR_EXCEPTION_INVALID_ADDRESS);
        // exceptionErr.arg(BDMConstants.kREST_API_WSADDRESS);

        final StringBuilder responseBuilder = new StringBuilder();
        final InputStream inputStreamObj = connection.getErrorStream();
        final BufferedReader in =
          new BufferedReader(new InputStreamReader(inputStreamObj));
        while (in.ready()) {
          responseBuilder.append(in.readLine());
        }
        Trace.kTopLevelLogger
          .debug("WSADDRESS  VALIDATE ERROR   Response json :"
            + responseBuilder.toString());
        throw exceptionErr;
      }

      // Throw exception if the address is not valid
      if (results != null
        && !results.getWsaddrValidationResults().getWsaddrInformation()
          .getWsaddrStatusCode().equalsIgnoreCase(BDMConstants.kValid)) {
        // BEGIN TASK-9418 Input Application script, Changed Error message: for
        // correct error message for invalid address
        final AppException exceptionErr = new AppException(
          BDMRESTAPIERRORMESSAGE.BDM_WSADDRESS_SEARCH_BY_POSTALCODE_REST_API_ERR_EXCEPTION_INVALID_ADDRESS);
        // exceptionErr.arg(BDMConstants.kREST_API_WSADDRESS);
        // END TASK 9418
        if (Trace.atLeast(Trace.kTraceVerbose)) {
          // If tracing is enabled, exception shown on log
          Trace.kTopLevelLogger
            .debug("WSAddress Validate Address REST API Failure");
          Trace.kTopLevelLogger.debug(exceptionErr.getStackTrace());
        }
        throw exceptionErr;

      } else {
        isValidAddress = true;
      }
    } catch (final IOException e) {

      Trace.kTopLevelLogger
        .info("Validate WSAddress Exception  = " + e.getStackTrace());
      final AppException ioExceptionErr = new AppException(
        BDMRESTAPIERRORMESSAGE.BDM_REST_API_ERR_IO_EXCEPTION);
      ioExceptionErr.arg(BDMConstants.kREST_API_WSADDRESS);
      throw ioExceptionErr;

    }

    return isValidAddress;
  }

}
