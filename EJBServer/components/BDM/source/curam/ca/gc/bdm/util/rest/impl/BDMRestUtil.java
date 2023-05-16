package curam.ca.gc.bdm.util.rest.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import curam.ca.gc.bdm.message.BDMBPOCCT;
import curam.ca.gc.bdm.message.BDMRESTAPIERRORMESSAGE;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.security.EncryptionUtil;

/**
 * Utility class to handle common logic for Federal data hub services
 *
 */
public class BDMRestUtil {

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

    Trace.kTopLevelLogger.error(argumentMsg);

    return appException;
  }

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
}
