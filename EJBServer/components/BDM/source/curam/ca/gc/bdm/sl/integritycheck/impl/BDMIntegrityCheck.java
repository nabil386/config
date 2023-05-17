package curam.ca.gc.bdm.sl.integritycheck.impl;

import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.sl.integritycheck.struct.BDMIntegrityCheckKey;
import curam.ca.gc.bdm.util.integrity.impl.BDMIntegrityRulesUtil;
import curam.ca.gc.bdm.util.integrity.impl.BDMSINFlagAndStatusReviewUtil;
import curam.ca.gc.bdm.util.integrity.impl.BDMSINIntegrityCheckConstants;
import curam.ca.gc.bdm.util.integrity.impl.BDMSINIntegrityCheckResult;
import curam.ca.gc.bdm.util.integrity.impl.BDMSINIntegrityCheckUtil;
import curam.ca.gc.bdm.util.integrity.impl.BDMSINIntegrityDateCheck;
import curam.ca.gc.bdm.util.integrity.impl.BDMSINIntegrityTaskCreationUtil;
import curam.codetable.CASEPARTICIPANTROLETYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.impl.EnvVars;
import curam.core.sl.entity.fact.CaseParticipantRoleFactory;
import curam.core.sl.entity.struct.CaseParticipantRoleDtls;
import curam.core.sl.entity.struct.CaseParticipantRoleDtlsList;
import curam.core.sl.entity.struct.CaseParticipantRole_eoCaseIDKey;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.core.struct.ConcernRoleKey;
import curam.util.dataaccess.DynamicDataAccess;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.Trace;

public class BDMIntegrityCheck
  extends curam.ca.gc.bdm.sl.integritycheck.base.BDMIntegrityCheck {

  @Override
  public void sinIntegrityCheck(final BDMIntegrityCheckKey key)
    throws AppException, InformationalException {

    if (!Configuration
      .getBooleanProperty(EnvVars.BDM_SININTEGRITYCHECK_ENABLED)) {

      Trace.kTopLevelLogger
        .info(BDMConstants.BDM_LOGS_PREFIX + "SIN Integrity Check disabled");

      return;
    }

    final CaseParticipantRole_eoCaseIDKey caseIDKey =
      new CaseParticipantRole_eoCaseIDKey();
    caseIDKey.caseID = key.caseID;

    final CaseParticipantRoleDtlsList cprList =
      CaseParticipantRoleFactory.newInstance().searchByCaseID(caseIDKey);

    final BDMIntegrityCheckKey checkOnPersonkey = new BDMIntegrityCheckKey();

    for (final CaseParticipantRoleDtls cpr : cprList.dtls) {

      if (!cpr.recordStatus.equals(RECORDSTATUS.NORMAL)
        || !(cpr.typeCode.equals(CASEPARTICIPANTROLETYPE.PRIMARY)
          || cpr.typeCode.equals(CASEPARTICIPANTROLETYPE.MEMBER)))
        continue;

      checkOnPersonkey.concernRoleID = cpr.participantRoleID;
      sinIntegrityCheckOnPerson(checkOnPersonkey);
    }

  }

  private CaseParticipantRoleDtls getCaseParticipantRolePC(
    final long concernRoleID) throws AppException, InformationalException {

    final ConcernRoleKey key = new ConcernRoleKey();
    key.concernRoleID = concernRoleID;

    final String sql = "SELECT "
      + "  c.CASEPARTICIPANTROLEID ,c.CASEID into :CASEPARTICIPANTROLEID, :CASEID "
      + "FROM " + "  CASEPARTICIPANTROLE c " + "JOIN CASEHEADER c2 ON "
      + "  c.CASEID = c2.CASEID " + "  AND c2.CASETYPECODE = 'CT2001' "
      + "  WHERE c.PARTICIPANTROLEID  = :concernRoleID "
      + "  AND c.TYPECODE = 'PRI'";

    final CaseParticipantRoleDtls caseParticipantRoleDtls =
      (CaseParticipantRoleDtls) DynamicDataAccess
        .executeNs(CaseParticipantRoleDtls.class, key, false, sql);

    caseParticipantRoleDtls.participantRoleID = concernRoleID;

    return caseParticipantRoleDtls;

  }

  @Override
  public void sinIntegrityCheckOnPerson(final BDMIntegrityCheckKey key)
    throws AppException, InformationalException {

    final BDMSINIntegrityCheckUtil bdmSINIntegrityCheckUtil =
      new BDMSINIntegrityCheckUtil();
    // Step 1: delete the existing SIR Response evidence if any
    final CaseParticipantRoleDtls cprPC = bdmSINIntegrityCheckUtil
      .removeExistingSINSIRResponceEvidence(key.concernRoleID);

    final BDMSINIntegrityCheckResult checkResult =
      bdmSINIntegrityCheckUtil.sinIntegrityCheck(key.concernRoleID);

    if (checkResult == null) {
      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + "SIR service not called as required evidence not found");
      return;
    }

    final String misMatchResult = checkResult.getMismatchCheckResult();
    if (misMatchResult
      .equals(BDMSINIntegrityCheckConstants.CHECK_RESULT_UNAVAILABLE)) {
      Trace.kTopLevelLogger
        .info(BDMConstants.BDM_LOGS_PREFIX + "SIR service not available");
      return;
    }

    if (misMatchResult
      .equals(BDMSINIntegrityCheckConstants.CHECK_RESULT_ERROR)) {
      Trace.kTopLevelLogger
        .info(BDMConstants.BDM_LOGS_PREFIX + "SIR service response error");

    }

    // create the SIR Response evidence
    BDMIntegrityRulesUtil.createSINSIRResponseEvidence(cprPC, checkResult);

    if (!misMatchResult
      .equals(BDMSINIntegrityCheckConstants.CHECK_RESULT_PASS)) {

      final EvidenceDescriptorKey evidenceDescriptorKey =
        new EvidenceDescriptorKey();
      evidenceDescriptorKey.evidenceDescriptorID =
        checkResult.getDetails().getSinEvidenceDescriptorID();
      bdmSINIntegrityCheckUtil.triggerVerifiction(evidenceDescriptorKey);

      BDMSINIntegrityTaskCreationUtil.createTaskForSINIntegrity(
        key.concernRoleID,
        BDMSINIntegrityCheckConstants.SIN_TASKTYPE_MISMATCH);

      // Bug 57918 Fixed to continue checking flag and date
      // return;
    } else {
      // Step 2: delete the existing verification on SIN evidence if any
      bdmSINIntegrityCheckUtil.removeExistingSINVerifiction(
        checkResult.getDetails().getSinEvidenceDescriptorID());

    }

    // BEGIN TASK-22189 - Continue checking flag and dates if name matched.
    // Calling the SIN Flag and Status Review Process.
    final boolean isInvestigationTaskCreated =
      new BDMSINFlagAndStatusReviewUtil()
        .processSINFlagAndStatusReview(checkResult);
    // END TASK-22189

    // BEGIN Task - 22190 - SIN Date Verification
    if (!isInvestigationTaskCreated) {
      new BDMSINIntegrityDateCheck().getSINDateVerificationCheck(checkResult);
    }

    // END Task - 22190 - SIN Date Verification

  }

}
