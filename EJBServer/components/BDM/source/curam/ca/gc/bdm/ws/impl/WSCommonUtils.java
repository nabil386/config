package curam.ca.gc.bdm.ws.impl;

import curam.util.exception.AppException;
import curam.util.exception.AppRuntimeException;
import curam.util.resources.GeneralConstants;
import curam.util.resources.Trace;
import curam.util.security.EncryptionUtil;
import org.apache.commons.lang3.StringUtils;

public class WSCommonUtils {

  /**
   * See
   * {@linkplain curam.custom.ca.gc.bdm.axis.impl.ExternalValidateSINService.logError(Exception,
   * String)}
   *
   * @param e Exception
   */
  public static final void logError(final Exception e) {

    logError(e, "");

  }

  /**
   * Log an error.
   * If trace is on, we'll log the entire stack trace, otherwise just the
   * message. The message will be the exception message or the
   * overrideErrorMessage parameter if not empty.
   *
   * @param e Exception
   * @param overrideErrorMessage String
   */
  public static final void logError(final Exception e,
    final String overrideErrorMessage) {

    String exceptionMessage = e.getMessage();
    if (StringUtils.isEmpty(overrideErrorMessage)) {
      exceptionMessage = overrideErrorMessage;
    }
    if (Trace.atLeast(Trace.kTraceOn)) {
      // If tracing is enabled, we include the stack trace.
      Trace.kTopLevelLogger.error(exceptionMessage, e);
    } else {
      // Otherwise we just include the basic exception name.
      Trace.kTopLevelLogger.error(exceptionMessage);
    }

  }

  /**
   * Throw an AppException from an Exception
   *
   * @param e Exception
   * @throws AppException
   */
  public static final void throwAppRuntimeException(final Exception e)
    throws AppException {

    final AppException appException =
      new AppRuntimeException(e).getAppException();
    throw appException;
  }

  /**
   * Gets the plain text version of a password.
   *
   * @return The plain text version of the password, or an empty string
   * if it cannot be decrypted.
   */
  public static final String
    getPlainTextPassword(final String encryptedPassword) {

    String result = GeneralConstants.kEmpty;

    if (encryptedPassword != null && encryptedPassword.length() > 0) {
      try {
        // TODO store/retrieve decrypted password in Cache
        result = EncryptionUtil.decryptPassword(encryptedPassword);

      } catch (final Exception e) {

        // NB We must not let an exception be thrown from here, because the
        // caller of this class interprets that as meaning that no
        // implementation is available and will not attempt to call it.
        // This means the end user would see a misleading message like
        // 'There are no potential duplicate identities' .
        // Instead we return a blank password which will allow the
        // authentication mechanism to handle the condition.

        // Write the underlying exception to the server log.
        logError(e);

      }
    }
    return result;
  }

}
