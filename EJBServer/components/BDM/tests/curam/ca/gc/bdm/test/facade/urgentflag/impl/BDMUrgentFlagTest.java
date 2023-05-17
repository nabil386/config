package curam.ca.gc.bdm.test.facade.urgentflag.impl;

import curam.ca.gc.bdm.codetable.BDMURGENTFLAGTYPE;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.fact.BDMCaseUrgentFlagFactory;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.struct.BDMCaseUrgentFlagDetails;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.struct.BDMCaseUrgentFlagDetailsList;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.struct.BDMCaseUrgentFlagIDKey;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.RECORDSTATUS;
import curam.core.facade.struct.CaseIDDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class BDMUrgentFlagTest extends CuramServerTestJUnit4 {

  @Before
  public void setUp() throws AppException, InformationalException {

    final BDMUrgentFlagHelper urgentFlagHelper = new BDMUrgentFlagHelper();

    urgentFlagHelper.createCase("1000", 1000L);
    urgentFlagHelper.createCase("1001", 1001L);
    urgentFlagHelper.setUpUrgentFlagData(1000l, BDMURGENTFLAGTYPE.MPENQUIRY,
      RECORDSTATUS.NORMAL, Date.getCurrentDate(), Date.kZeroDate);
    urgentFlagHelper.setUpUrgentFlagData(1001l, BDMURGENTFLAGTYPE.MPENQUIRY,
      RECORDSTATUS.NORMAL, Date.getCurrentDate().addDays(-5),
      Date.getCurrentDate());
  }

  @Test
  public void testCurrentUrgentFlagsByCaseID()
    throws AppException, InformationalException {

    final CaseIDDetails caseDtlskey = new CaseIDDetails();
    caseDtlskey.caseID = 1000;
    final BDMCaseUrgentFlagDetailsList urgentFlagDtlsList =
      BDMCaseUrgentFlagFactory.newInstance()
        .listCurrentUrgentFlags(caseDtlskey);

    assert urgentFlagDtlsList.dtls.size() == 1;
  }

  @Test
  public void testPreviosUrgentFlagsByCaseID()
    throws AppException, InformationalException {

    final CaseIDDetails caseDtlskey = new CaseIDDetails();
    caseDtlskey.caseID = 1001l;
    final BDMCaseUrgentFlagDetailsList urgentFlagDtlsList =
      BDMCaseUrgentFlagFactory.newInstance()
        .listPreviousUrgentFlags(caseDtlskey);

    assert urgentFlagDtlsList.dtls.size() == 1;
  }

  @Test
  public void testreadCaseUrgentFlag()
    throws AppException, InformationalException {

    final BDMCaseUrgentFlagDetails returnDetails =
      new BDMCaseUrgentFlagDetails();
    final CaseIDDetails caseDtlskey = new CaseIDDetails();
    final BDMCaseUrgentFlagDetailsList urgentFlagDtlsList =
      BDMCaseUrgentFlagFactory.newInstance()
        .listCurrentUrgentFlags(caseDtlskey);
    final BDMCaseUrgentFlagIDKey key = new BDMCaseUrgentFlagIDKey();

    returnDetails.caseID = 1001l;
    returnDetails.recordStatus = RECORDSTATUS.NORMAL;
    returnDetails.createdByFullName = "John Deo";
    returnDetails.startDate = Date.getCurrentDate();
    returnDetails.createdBy = "John";

    returnDetails.bdmCaseUrgentFlagID = 0;

    BDMCaseUrgentFlagFactory.newInstance().readCaseUrgentFlag(key);

    // assert urgentFlagDtlsList1.dtls.size() == 1;
  }

  @Test
  public void testmodifyCaseUrgentFlag()
    throws AppException, InformationalException {

    final BDMCaseUrgentFlagDetails returnDetails =
      new BDMCaseUrgentFlagDetails();
    final CaseIDDetails caseDtlskey = new CaseIDDetails();
    final BDMCaseUrgentFlagDetailsList urgentFlagDtlsList =
      BDMCaseUrgentFlagFactory.newInstance()
        .listCurrentUrgentFlags(caseDtlskey);
    final BDMCaseUrgentFlagIDKey key1 = new BDMCaseUrgentFlagIDKey();

    returnDetails.caseID = 1001l;
    returnDetails.recordStatus = RECORDSTATUS.NORMAL;
    returnDetails.createdByFullName = "John Deo";
    returnDetails.startDate = Date.getCurrentDate();
    returnDetails.createdBy = "John";
    returnDetails.bdmCaseUrgentFlagID = 0;

    BDMCaseUrgentFlagFactory.newInstance()
      .modifyCaseUrgentFlag(returnDetails);

    // assert urgentFlagDtlsList1.dtls.size() == 1;
  }

  @Test
  public void testDeleteUrgentFlagsByCaseID()
    throws AppException, InformationalException {

    final CaseIDDetails caseDtlskey = new CaseIDDetails();
    caseDtlskey.caseID = 1000l;
    final BDMCaseUrgentFlagDetailsList urgentFlagDtlsList =
      BDMCaseUrgentFlagFactory.newInstance()
        .listCurrentUrgentFlags(caseDtlskey);

    for (final BDMCaseUrgentFlagDetails urgentFlagDtls : urgentFlagDtlsList.dtls
      .items()) {
      final BDMCaseUrgentFlagIDKey deleteFlagKey =
        new BDMCaseUrgentFlagIDKey();
      deleteFlagKey.bdmCaseUrgentFlagID = urgentFlagDtls.bdmCaseUrgentFlagID;
      deleteFlagKey.caseID = urgentFlagDtls.caseID;
      BDMCaseUrgentFlagFactory.newInstance()
        .deleteCaseUrgentFlag(deleteFlagKey);
    }

    final BDMCaseUrgentFlagDetailsList urgentFlagDtlsListNew =
      BDMCaseUrgentFlagFactory.newInstance()
        .listCurrentUrgentFlags(caseDtlskey);

    assert urgentFlagDtlsListNew.dtls.isEmpty();
  }

}
