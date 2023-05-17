package curam.ca.gc.bdm.util.rest.impl;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import curam.ca.gc.bdm.entity.fact.BDMRequestResponseFactory;
import curam.ca.gc.bdm.entity.intf.BDMRequestResponse;
import curam.ca.gc.bdm.entity.struct.BDMRequestResponseDtls;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.util.exception.AppException;
import curam.util.internal.BizTransaction;
import curam.util.resources.Configuration;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import curam.util.transaction.TransactionInfo.TransactionType;
import curam.util.type.UniqueID;
import java.io.IOException;
import java.io.StringWriter;

// BEGIN, ADO-107574 , Audit APIs
/**
 * This class contains operations to support audit for APIs.
 */
public final class BDMAPIAuditUtil {

  BDMRestUtil bdmRestUtil;

  // changes for Task#114750
  private final String auditEnableProperty =
    EnvVars.ENV_REQUESTRESPONSE_AUDIT_ENABLED;

  /**
   * Method to audit the API details. This method will be called from inbound &
   * outbound call by populating the input BDMAPIAuditDetails values. If the
   * global property for enabling audit and if the individual method's property
   * for enabling the audit is set to 'true', then further logic to insert the
   * audit details will be invoked.
   *
   * @param BDMAPIAuditDetails
   * @throws AppException, InformationalException
   */
  public void auditAPI(final BDMAPIAuditDetails bdmapiAuditDetails)
    throws AppException {

    final String invokingMethod = bdmapiAuditDetails.getInvokingMethod();
    if (Configuration
      .getBooleanProperty(EnvVars.ENV_REQUESTRESPONSE_AUDIT_ENABLED)
      && Configuration.getBooleanProperty(
        auditEnableProperty + CuramConst.gkDotChar + invokingMethod.trim())) {
      insertAuditDetailsToDB(bdmapiAuditDetails);
    }
  }

  /**
   * Method to insert the audit details into the audit table - BDMRequestResponse.
   *
   * @param BDMAPIAuditDetails
   *
   * @throws AppException, InformationalException
   */
  public void insertAuditDetailsToDB(
    final BDMAPIAuditDetails bdmapiAuditDetails) throws AppException {

    final BDMRequestResponse bdmRequestResponseObj =
      BDMRequestResponseFactory.newInstance();
    final BDMRequestResponseDtls requestResponseDtls =
      new BDMRequestResponseDtls();

    requestResponseDtls.auditID = UniqueID.nextUniqueID();
    requestResponseDtls.methodName = bdmapiAuditDetails.getInvokingMethod();
    if (null != bdmapiAuditDetails.getRelatedID()) {
      requestResponseDtls.relatedID =
        bdmapiAuditDetails.getRelatedID().toString();
    }
    requestResponseDtls.userName = TransactionInfo.getProgramUser();
    requestResponseDtls.apiType = bdmapiAuditDetails.getApiType();
    requestResponseDtls.correlationID = bdmapiAuditDetails.getCorrelationID();
    requestResponseDtls.source = bdmapiAuditDetails.getSource();

    final Object requestObject = bdmapiAuditDetails.getRequestObject();
    if (null != requestObject) {
      requestResponseDtls.requestDetails = convertToJSON(requestObject, true);
    }

    final Object responseObject = bdmapiAuditDetails.getResponseObject();
    if (null != responseObject) {
      requestResponseDtls.responseDetails =
        convertToJSON(responseObject, true);
    }

    requestResponseDtls.statusCode = "" + bdmapiAuditDetails.getStatusCode();
    requestResponseDtls.requestTime =
      bdmapiAuditDetails.getRequestTransactionDateTime();
    requestResponseDtls.responseTime =
      bdmapiAuditDetails.getResponseTransactionDateTime();

    performInsert(bdmRequestResponseObj, requestResponseDtls);

  }

  /**
   * Method to invoke the respective methods to create a separate transaction for
   * handling the insertion of audit request/response.
   *
   * @param BDMRequestResponse Object
   * @param BDMRequestResponseDtls
   *
   **/
  public void performInsert(final BDMRequestResponse bdmRequestResponse,
    final BDMRequestResponseDtls auditDtls) {

    TransactionInfo localTransaction = null;
    try {
      localTransaction = beginLocalTransaction();
      // insert record into the DB
      bdmRequestResponse.insert(auditDtls);
      commitLocalTransaction(localTransaction);
    } catch (final Exception t) {
      Trace.kTopLevelLogger.info(t.getStackTrace());
      rollbackLocalTransaction(localTransaction);
    }
  }

  /**
   * Commit local transaction and restore original transaction
   *
   * @param localTransaction
   * @throws Exception
   */
  private void commitLocalTransaction(final TransactionInfo localTransaction)
    throws Exception {

    if (Configuration.runningInAppServer()) {
      TransactionInfo.resumeTransaction(false);
    } else {
      try {
        if (localTransaction != null) {
          localTransaction.commit();
          localTransaction.closeConnection();
        }
      } catch (final Exception e) {
        Trace.kTopLevelLogger.info(e.getStackTrace());
      }
      TransactionInfo.restoreTransactionInfo();
    }
  }

  /**
   * Rollback local transaction and restore original transaction
   *
   * @param localTransaction
   */
  private void
    rollbackLocalTransaction(final TransactionInfo localTransaction) {

    if (Configuration.runningInAppServer()) {
      try {
        TransactionInfo.resumeTransaction(true);
      } catch (final Exception e) {
        Trace.kTopLevelLogger.info(e.getStackTrace());
      }
    } else {
      try {
        if (localTransaction != null) {
          localTransaction.rollback();
          localTransaction.closeConnection();
        }
      } catch (final Exception e) {
        Trace.kTopLevelLogger.info(e.getStackTrace());
      }
      TransactionInfo.restoreTransactionInfo();
    }
  }

  /**
   * Method for transaction handling.
   *
   */
  private TransactionInfo beginLocalTransaction() throws Exception {

    TransactionInfo localTransaction = null;
    if (Configuration.runningInAppServer()) {
      TransactionInfo.suspendTransaction();
    } else {
      // Back up current transaction info
      TransactionInfo.backUpTransactionInfo();
      final String currentLocale = TransactionInfo.getProgramLocale();
      final TransactionType currentTransactionType =
        TransactionInfo.getTransactionType();

      // create a new local transaction
      localTransaction =
        TransactionInfo.setTransactionInfo(currentTransactionType,
          new BDMAPIAuditUtilLocalTransaction(), null, currentLocale);
      localTransaction.begin();
    }
    return localTransaction;
  }

  // Transaction handling inner class
  public static class BDMAPIAuditUtilLocalTransaction
    implements BizTransaction {

    @Override
    public String getName() {

      return "BDMAPIAuditUtilLocalTransaction";
    }

    @Override
    public boolean transactional() {

      return true;
    }
  }

  /**
   * Method invoked to convert the incoming object to JSON for audit storing
   * purpose.
   *
   * @param Object, onlyNullInd (boolean)
   *
   * @return JSON string
   */
  public String convertToJSON(final Object obj, final boolean onlyNonNull) {

    if (obj.getClass().isInstance(String.class)) {
      return obj.toString();
    } else {
      final ObjectMapper jsonMapper = new ObjectMapper();
      jsonMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
      if (onlyNonNull) {
        jsonMapper.setSerializationInclusion(Include.NON_NULL);
      }
      final StringWriter sw = new StringWriter();
      try {
        jsonMapper.writeValue(sw, obj);
      } catch (final IOException e) {
        // no action as audit handling shouldnot impact the main transaction
      }
      return sw.toString();
    }
  }

  // END, ADO-107574, Audit APIs
}
