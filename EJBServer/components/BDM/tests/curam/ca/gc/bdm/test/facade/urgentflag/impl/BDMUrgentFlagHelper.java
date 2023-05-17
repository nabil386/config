package curam.ca.gc.bdm.test.facade.urgentflag.impl;

import curam.ca.gc.bdm.facade.bdmcaseurgentflag.fact.BDMCaseUrgentFlagFactory;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.struct.BDMCaseUrgentFlagDetails;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.codetable.impl.METHODOFDELIVERYEntry;
import curam.core.fact.CaseHeaderFactory;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.PersonDtls;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;

public class BDMUrgentFlagHelper {

  private final long concernRoleID;

  public BDMUrgentFlagHelper() throws AppException, InformationalException {

    final RegisterPerson registerPersonObj = new RegisterPerson("");
    final PersonDtls personDtls = registerPersonObj
      .registerDefault("Joe Test", METHODOFDELIVERYEntry.CHEQUE);
    this.concernRoleID = personDtls.concernRoleID;
  }

  public void createCase(final String caseReference, final long caseID)
    throws AppException, InformationalException {

    final CaseHeaderDtls chDtls = new CaseHeaderDtls();
    chDtls.appealIndicator = false;
    chDtls.caseReference = caseReference;
    chDtls.versionNo = 1;
    chDtls.concernRoleID = this.concernRoleID;
    chDtls.caseID = caseID;
    CaseHeaderFactory.newInstance().insert(chDtls);
  }

  public void setUpUrgentFlagData(final long caseID,
    final String urgentFlagType, final String recordStatus,
    final Date startDate, final Date endDate)
    throws AppException, InformationalException {

    final BDMCaseUrgentFlagDetails flagDtls = new BDMCaseUrgentFlagDetails();
    flagDtls.caseID = caseID;
    TransactionInfo.getInfo();
    flagDtls.createdBy = TransactionInfo.getProgramUser();
    TransactionInfo.getInfo();
    flagDtls.createdByFullName = TransactionInfo.getProgramUser();
    flagDtls.recordStatus = recordStatus;
    flagDtls.startDate = startDate;
    flagDtls.endDate = endDate;
    flagDtls.type = urgentFlagType;
    BDMCaseUrgentFlagFactory.newInstance().createCaseUrgentFlag(flagDtls);

  }

}
