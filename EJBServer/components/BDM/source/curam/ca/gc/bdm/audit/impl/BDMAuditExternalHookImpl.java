package curam.ca.gc.bdm.audit.impl;

import curam.ca.gc.bdm.entity.fact.BDMOpAuditTrailFactory;
import curam.ca.gc.bdm.entity.struct.BDMOpAuditTrailDtls;
import curam.util.audit.Audit;
import curam.util.audit.ExternalOperationHook;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import curam.util.transaction.TransactionInfo.TransactionType;
import curam.util.type.DateTime;

public class BDMAuditExternalHookImpl implements ExternalOperationHook {

  @Override
  public void preOperation(final Object[] parameters)
    throws AppException, InformationalException {

    // Implement Audit Logic here
    if (Trace.atLeast(Trace.kTraceVerbose)) {
      Trace.kTopLevelLogger
        .debug("--> BDMAuditExternalHookImpl Call START <--");
    }
    // Get the user name.
    final String customUserID = TransactionInfo.getCustomUserID();
    String username;
    if (!StringUtil.isNullOrEmpty(StringUtil.rtrimSpacesOnly(customUserID))) {
      username = customUserID;
    } else {
      username = TransactionInfo.getProgramUser();
    }
    // program name
    final String programName = TransactionInfo.getProgramName();
    // time entered
    final DateTime timeStamp =
      new DateTime(TransactionInfo.getProgramTimeStamp());
    // transaction type
    final TransactionType tranSactionTypeObj =
      TransactionInfo.getTransactionType();
    final String tranType = Character
      .valueOf(tranSactionTypeObj.toCompactRepresentation()).toString();
    // parameter in xml representation
    String paramxml = "";
    if (parameters.length > 0) {
      paramxml =
        Audit.compactXML(Audit.getXmlStringRepresentation(parameters[0]));
    }
    // Trace.kTopLevelLogger.info("paramxml = " + paramxml);
    insertOpAuditEntry(paramxml, programName, timeStamp, username, tranType);
    if (Trace.atLeast(Trace.kTraceVerbose)) {
      Trace.kTopLevelLogger
        .debug("--> BDMAuditExternalHookImpl Call END <--");
    }
  }

  /**
   * Insert entry into the BDMOpsAuditTrail table
   *
   * @param paramKeyInfo
   * @param programName
   * @param timeEntered
   * @param userId
   * @param tranType
   * @throws AppException
   * @throws InformationalException
   */
  private void insertOpAuditEntry(final String paramKeyInfo,
    final String programName, final DateTime timeEntered, final String userId,
    final String tranType) throws AppException, InformationalException {

    // call insert on entity after setting all the values
    final BDMOpAuditTrailDtls auditDtls = new BDMOpAuditTrailDtls();
    auditDtls.lastWritten = TransactionInfo.getSystemDateTime();
    auditDtls.paramKeyInfo = paramKeyInfo.length() < 3000 ? paramKeyInfo
      : paramKeyInfo.substring(0, 3000);
    auditDtls.programName = programName;
    auditDtls.timeEntered = timeEntered;
    auditDtls.userId = userId;
    auditDtls.tranType = tranType;
    BDMOpAuditTrailFactory.newInstance().insert(auditDtls);
    if (Trace.atLeast(Trace.kTraceVerbose)) {
      Trace.kTopLevelLogger
        .debug("--> Entry Inserted into the table BDMOpAuditTrail <--");
    }
  }

}
