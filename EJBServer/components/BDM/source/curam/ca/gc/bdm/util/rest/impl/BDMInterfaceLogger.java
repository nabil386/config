package curam.ca.gc.bdm.util.rest.impl;

import curam.core.impl.EnvVars;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;

/**
 * This class contains operation to audit logs for interfaces.
 */
public final class BDMInterfaceLogger {

  BDMRestUtil bdmRestUtil;

  /**
   * Private constructor for not allowing object to be created for this class.
   */
  public BDMInterfaceLogger() {

    super();
    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * This method is to log the request.
   *
   * @param method
   * @param requestID
   * @param obj - the request object
   *
   * @throws AppException
   * @throws InformationalException
   */
  public void logRequest(final String method, final String requestID,
    final Object obj) throws AppException, InformationalException {

    if (Configuration.getBooleanProperty(EnvVars.ENV_BDMREST_LOG_ENABLED)) {

      if (obj.getClass().equals(String.class)) { // string passed in
        Trace.kTopLevelLogger.info(String.format(
          "%1$s, user: %2$s, requestID: %3$s, requestObject: %4$s", method,
          TransactionInfo.getProgramUser(), requestID, obj));
      } else { // json passed in
        Trace.kTopLevelLogger.info(String.format(
          "%1$s, user: %2$s, requestID: %3$s, requestObject: %4$s", method,
          TransactionInfo.getProgramUser(), requestID,
          BDMRestUtil.convertToJSON(obj)));
      }
    }
  }

  /**
   * This method is to log the response.
   *
   * @param method
   * @param requestID
   * @param obj - the response object
   *
   * @throws AppException
   * @throws InformationalException
   */
  public void logResponse(final String method, final String requestID,
    final Object obj) throws AppException, InformationalException {

    if (Configuration.getBooleanProperty(EnvVars.ENV_BDMREST_LOG_ENABLED)) {

      if (!(obj == null) && obj.getClass().equals(String.class)) { // string passed in
        Trace.kTopLevelLogger.info(String.format(
          "%1$s, user: %2$s, requestID: %3$s, responseObject: %4$s", method,
          TransactionInfo.getProgramUser(), requestID, obj));
      } else { // json passed in
        Trace.kTopLevelLogger.info(String.format(
          "%1$s, user: %2$s, requestID: %3$s, responseObject: %4$s", method,
          TransactionInfo.getProgramUser(), requestID,
          BDMRestUtil.convertToJSON(obj)));
      }
    }
  }

  /**
   * This method is to log performance of the API calls.
   *
   * @param method
   * @param startTime
   * @param requestID
   *
   * @throws AppException
   * @throws InformationalException
   */
  public void logRestAPIPerf(final String method, final long startTime,
    final String requestID) throws AppException, InformationalException {

    if (Configuration.getBooleanProperty(EnvVars.ENV_BDMREST_LOG_ENABLED)) {
      Trace.kTopLevelLogger.info(
        String.format("PERF: REST %1$s %2$sms user: %3$s requestID=%4$s",
          method, System.currentTimeMillis() - startTime,
          TransactionInfo.getProgramUser(), requestID));
    }
  }

}
