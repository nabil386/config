package curam.ca.gc.bdm.util.integrity.impl;

import curam.codetable.CASETYPECODE;
import curam.commonintake.entity.struct.ApplicationCaseDtls;
import curam.commonintake.entity.struct.ApplicationCaseDtlsList;
import curam.commonintake.entity.struct.SearchByParticipantRoleAndCaseTypeKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.ConcernRoleKey;
import curam.util.dataaccess.CuramValueList;
import curam.util.dataaccess.DynamicDataAccess;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;

public class BDMSINIntegrityCheckDetails {

  private long nameEvidenceDescriptorID = 0;

  private long dobEvidenceDescriptorID = 0;

  private long sinEvidenceDescriptorID = 0;

  private long addressEvidenceDescriptorID = 0;

  private String birthLastName = null;

  private Date applicationDate = null;

  private long concernRoleID = 0;

  private long applicationCaseID = 0;

  private long icCaseID = 0;

  public BDMSINIntegrityCheckDetails(final long concernRoleID)
    throws AppException, InformationalException {

    this.concernRoleID = concernRoleID;

    final ConcernRoleKey key = new ConcernRoleKey();
    key.concernRoleID = concernRoleID;

    final String sql =
      "SELECT max(a.APPLICATIONDATE) into :applicationDate FROM APPLICATIONCASE a "
        + " JOIN CASEPARTICIPANTROLE c ON a.APPLICATIONCASEID  = c.CASEID"
        + " WHERE c.PARTICIPANTROLEID = :concernRoleID ";

    try {

      final ApplicationCaseDtls applicationCaseDtls =
        (ApplicationCaseDtls) DynamicDataAccess
          .executeNs(ApplicationCaseDtls.class, key, false, sql);
      applicationDate = applicationCaseDtls.applicationDate;

    } catch (final Exception e) {

      e.printStackTrace();
      applicationDate = Date.kZeroDate;

    }
    // Setting Application ID
    setApplicationCaseID(concernRoleID);

    // Setting IC CaseID
    setICCaseID(concernRoleID);

  }

  public void setApplicationCaseID(final long concernRoleID)
    throws AppException, InformationalException {

    this.concernRoleID = concernRoleID;

    final ConcernRoleKey key = new ConcernRoleKey();
    key.concernRoleID = concernRoleID;

    final String sql =
      "SELECT applicationCaseID into :applicationCaseID FROM APPLICATIONCASE a "
        + " JOIN CASEPARTICIPANTROLE c ON a.APPLICATIONCASEID  = c.CASEID"
        + " WHERE c.PARTICIPANTROLEID = :concernRoleID and  APPLICATIONDATE= to_date('"
        + applicationDate + "', 'YYYY-MM-DD')";

    try {

      final CuramValueList<ApplicationCaseDtls> applicationCaseDtls =
        DynamicDataAccess.executeNsMulti(ApplicationCaseDtls.class, key,
          false, sql);

      applicationCaseID = applicationCaseDtls.isEmpty() ? 0
        : applicationCaseDtls.get(0).applicationCaseID;

    } catch (final Exception e) {

      final SearchByParticipantRoleAndCaseTypeKey caseTypeKey =
        new SearchByParticipantRoleAndCaseTypeKey();
      caseTypeKey.caseTypeCode = CASETYPECODE.APPLICATION_CASE;
      caseTypeKey.concernRoleID = concernRoleID;

      final ApplicationCaseDtlsList applicationCaseDtlsList =
        curam.commonintake.entity.fact.ApplicationCaseFactory.newInstance()
          .searchByParticipantRoleAndType(caseTypeKey);

      applicationCaseID = applicationCaseDtlsList.dtls.isEmpty() ? 0
        : applicationCaseDtlsList.dtls.get(0).applicationCaseID;

    }
  }

  public void setICCaseID(final long concernRoleID) {

    this.concernRoleID = concernRoleID;

    final ConcernRoleKey key = new ConcernRoleKey();
    key.concernRoleID = concernRoleID;

    final String sql = "SELECT " + "  CH.CASEID " + "FROM "
      + "  CASEHEADER CH, " + "  CASEPARTICIPANTROLE CPR " + "WHERE "
      + "  CPR.RECORDSTATUS = 'RST1' AND CPR.TYPECODE IN ('PRI', 'MEM')"
      + "  AND CH.CASEID = CPR.CASEID "
      + "  AND CH.INTEGRATEDCASETYPE = 'ICT80000'"
      + "  AND CPR.PARTICIPANTROLEID =:concernRoleID;";

    try {

      final CuramValueList<CaseHeaderDtls> caseHeaderDtls = DynamicDataAccess
        .executeNsMulti(CaseHeaderDtls.class, key, false, sql);

      icCaseID = caseHeaderDtls.isEmpty() ? 0 : caseHeaderDtls.get(0).caseID;

    } catch (final Exception e) {

      e.printStackTrace();
      icCaseID = 0;

    }

  }

  public long getApplicationCaseID() {

    return this.applicationCaseID;
  }

  public long getICCaseID() {

    return this.icCaseID;
  }

  public long getAddressEvidenceDecriptorID() {

    return this.addressEvidenceDescriptorID;
  }

  public long getNameEvidenceDescriptorID() {

    return this.nameEvidenceDescriptorID;
  }

  public long getDobEvidenceDescriptorID() {

    return this.dobEvidenceDescriptorID;
  }

  public long getSinEvidenceDescriptorID() {

    return this.sinEvidenceDescriptorID;
  }

  public String getBirthLastName() {

    return this.birthLastName;
  }

  public long getConcernRoleID() {

    return this.concernRoleID;
  }

  public Date getApplicationDate() {

    return applicationDate;
  }

  public void
    setNameEvidenceDescriptorID(final long nameEvidenceDescriptorID) {

    this.nameEvidenceDescriptorID = nameEvidenceDescriptorID;
  }

  public void setDobEvidenceDescriptorID(final long dobEvidenceDescriptorID) {

    this.dobEvidenceDescriptorID = dobEvidenceDescriptorID;
  }

  public void setSinEvidenceDescriptorID(final long sinEvidenceDescriptorID) {

    this.sinEvidenceDescriptorID = sinEvidenceDescriptorID;
  }

  public void
    setAddressEvidenceDesciptorID(final long addressEvidenceDescriptorID) {

    this.addressEvidenceDescriptorID = addressEvidenceDescriptorID;
  }

  public void setBirthLastName(final String birthLastName) {

    this.birthLastName = birthLastName;
  }

}
