package curam.ca.gc.bdmoas.test.sl.cra.transaction.impl;

import curam.ca.gc.bdmoas.codetable.BDMOASCRAINCOMESTATUS;
import curam.ca.gc.bdmoas.codetable.BDMOASCRARESPONSESTATUS;
import curam.ca.gc.bdmoas.codetable.BDMOASCRATRANSACTIONSTATUS;
import curam.ca.gc.bdmoas.codetable.BDMOASCRATRANSSUBCODE;
import curam.ca.gc.bdmoas.entity.cra.transaction.fact.BDMOASCRADataFactory;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRADataDtls;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRATransactionConcernRoleStatusKey;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRATransactionDtls;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRATransactionDtlsList;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRATransactionKey;
import curam.ca.gc.bdmoas.facade.cra.transaction.struct.BDMOASCRARequestCreateDetails;
import curam.ca.gc.bdmoas.facade.cra.transaction.struct.BDMOASCRARequestCreateOptions;
import curam.ca.gc.bdmoas.facade.cra.transaction.struct.BDMOASCRARequestSummaryList;
import curam.ca.gc.bdmoas.facade.cra.transaction.struct.BDMOASCRATaxYearSummaryList;
import curam.ca.gc.bdmoas.message.BDMOASAPPLICATIONCASE;
import curam.ca.gc.bdmoas.sl.cra.transaction.fact.BDMOASCRATransactionFactory;
import curam.ca.gc.bdmoas.test.rules.functions.CustomFunctionTestJunit4;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.fact.PersonFactory;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.struct.AlternateNameDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseKey;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.ParticipantAndCaseTypeDetails;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonKey;
import curam.litereferral.impl.CuramConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import curam.util.workflow.fact.BizObjAssociationFactory;
import curam.util.workflow.fact.TaskAdminFactory;
import curam.util.workflow.impl.EnactmentService;
import curam.util.workflow.impl.ProcessInstanceAdmin;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/**
 * The Class tests {@link BDMOASCRATransactionTest} custom
 * function.
 */

@RunWith(JMockit.class)
public class BDMOASCRATransactionTest extends CustomFunctionTestJunit4 {

  private static final long CASEID = 101l;

  private static final long CONCERNROLEID = 102l;

  private static final long CASEPARTICIPANTTOLEID = 103l;

  private static final String CORRECTIONSETID = "114";

  private static final long SUCCESSIONID = 113l;

  private static final long EVIDENCEDESCRIPTORID = 112l;

  private static final long EVIDENCEID = 111l;

  private static final long TASKID = 115l;

  private static final String PARTICIPANTNAME = "Junit Lee";

  @Tested
  BDMOASCRATransactionFactory bdmOASCRATransactionFactory;

  @Mocked
  EvidenceDescriptorFactory evidenceDescriptorFactory;

  @Mocked
  BizObjAssociationFactory bizObjAssociationFactory;

  @Mocked
  TaskAdminFactory taskAdminFactory;

  @Mocked
  ProcessInstanceAdmin processInstanceAdmin;

  @Mocked
  curam.util.events.impl.EventService eventService;

  @Mocked
  EnactmentService enactmentService;

  @Mocked
  EvidenceControllerFactory evidenceControllerFactory;

  @Mocked
  CaseHeaderFactory caseHeaderFactory;

  @Mocked
  ConcernRoleFactory concernRoleFactory;

  @Mocked
  BDMOASCRADataFactory bdmOASCRADataFactory;

  @Mocked
  curam.core.fact.AlternateNameFactory alternateNameFactory;

  @Mocked
  PersonFactory personFactory;

  @Mocked
  curam.ca.gc.bdmoas.entity.cra.transaction.fact.BDMOASCRATransactionFactory bdmOASCRATransactionFactoryENT;

  public BDMOASCRATransactionTest() {

    super();
  }

  @Override
  public boolean shouldCommit() {

    return false;
  }

  @Test
  public void testListInProgressForCase()
    throws AppException, InformationalException {

    new Expectations() {

      ParticipantAndCaseTypeDetails partCaseTypedtls =
        new ParticipantAndCaseTypeDetails();

      BDMOASCRATransactionDtlsList dtlsList =
        new BDMOASCRATransactionDtlsList();

      {
        partCaseTypedtls.concernRoleID = CONCERNROLEID;
        caseHeaderFactory
          .readParticipantAndCaseTypeDetails((CaseHeaderKey) any);
        result = partCaseTypedtls;

        final BDMOASCRATransactionDtls bdmOASCRATransactionDtls =
          new BDMOASCRATransactionDtls();
        bdmOASCRATransactionDtls.concernRoleID = CONCERNROLEID;
        bdmOASCRATransactionDtls.taxYear = "2022";
        bdmOASCRATransactionDtls.transactionSubCode =
          BDMOASCRATRANSSUBCODE.ADD;
        bdmOASCRATransactionDtls.status = BDMOASCRATRANSACTIONSTATUS.PENDING;
        bdmOASCRATransactionDtls.createdOn =
          Date.getCurrentDate().addDays(-CuramConst.gkTen).getDateTime();
        dtlsList.dtls.add(bdmOASCRATransactionDtls);
        bdmOASCRATransactionFactoryENT
          .searchByConcernRole((ConcernRoleKey) any);
        result = dtlsList;
      }
    };

    final CaseKey key = new CaseKey();
    key.caseID = CASEID;
    final BDMOASCRARequestSummaryList bdmOASCRARequestSummaryList =
      bdmOASCRATransactionFactory.listInProgressForCase(key);
    assertEquals(curam.core.impl.CuramConst.gkOne,
      bdmOASCRARequestSummaryList.dtls.size());
    assertEquals(CONCERNROLEID, bdmOASCRARequestSummaryList.dtls
      .get(curam.core.impl.CuramConst.gkZero).concernRoleID);
  }

  @Test
  public void testListTaxYearsForCase()
    throws AppException, InformationalException {

    new Expectations() {

      ParticipantAndCaseTypeDetails partCaseTypedtls =
        new ParticipantAndCaseTypeDetails();

      BDMOASCRATransactionDtlsList dtlsList =
        new BDMOASCRATransactionDtlsList();

      BDMOASCRADataDtls dataDtls = new BDMOASCRADataDtls();

      {
        partCaseTypedtls.concernRoleID = CONCERNROLEID;
        caseHeaderFactory
          .readParticipantAndCaseTypeDetails((CaseHeaderKey) any);
        result = partCaseTypedtls;

        final BDMOASCRATransactionDtls bdmOASCRATransactionDtls =
          new BDMOASCRATransactionDtls();
        bdmOASCRATransactionDtls.concernRoleID = CONCERNROLEID;
        bdmOASCRATransactionDtls.taxYear = "2022";
        bdmOASCRATransactionDtls.transactionSubCode =
          BDMOASCRATRANSSUBCODE.ADD;
        bdmOASCRATransactionDtls.status = BDMOASCRATRANSACTIONSTATUS.RECEIVED;
        bdmOASCRATransactionDtls.createdOn =
          Date.getCurrentDate().addDays(-CuramConst.gkTen).getDateTime();
        bdmOASCRATransactionDtls.transactionID = 131l;
        dtlsList.dtls.add(bdmOASCRATransactionDtls);
        bdmOASCRATransactionFactoryENT
          .searchByConcernRole((ConcernRoleKey) any);
        result = dtlsList;

        dataDtls.responseStatus = BDMOASCRARESPONSESTATUS.SUCCESSFUL;
        dataDtls.incomeStatus = BDMOASCRAINCOMESTATUS.FOUND;
        bdmOASCRADataFactory.readByTransaction((BDMOASCRATransactionKey) any);
        result = dataDtls;
      }
    };

    final CaseKey key = new CaseKey();
    key.caseID = CASEID;
    final BDMOASCRATaxYearSummaryList bdmOASCRATaxYearSummaryList =
      bdmOASCRATransactionFactory.listTaxYearsForCase(key);
    assertEquals(curam.core.impl.CuramConst.gkOne,
      bdmOASCRATaxYearSummaryList.dtls.size());
    assertEquals(CONCERNROLEID, bdmOASCRATaxYearSummaryList.dtls
      .get(curam.core.impl.CuramConst.gkZero).concernRoleID);
  }

  @Test
  public void testCreateRequestEXCEPTION()
    throws AppException, InformationalException {

    new Expectations() {

      BDMOASCRATransactionDtlsList tranList =
        new BDMOASCRATransactionDtlsList();

      {
        final BDMOASCRATransactionDtls bdmOASCRATransactionDtls =
          new BDMOASCRATransactionDtls();
        bdmOASCRATransactionDtls.concernRoleID = CONCERNROLEID;
        bdmOASCRATransactionDtls.taxYear = "2022";
        bdmOASCRATransactionDtls.transactionSubCode =
          BDMOASCRATRANSSUBCODE.ADD;
        bdmOASCRATransactionDtls.status = BDMOASCRATRANSACTIONSTATUS.PENDING;
        bdmOASCRATransactionDtls.createdOn =
          Date.getCurrentDate().addDays(-CuramConst.gkTen).getDateTime();
        tranList.dtls.add(bdmOASCRATransactionDtls);
        bdmOASCRATransactionFactoryENT.searchByConcernRoleAndStatus(
          (BDMOASCRATransactionConcernRoleStatusKey) any);
        result = tranList;
      }
    };

    final BDMOASCRARequestCreateDetails details =
      new BDMOASCRARequestCreateDetails();
    details.concernRoleID = CONCERNROLEID;
    details.transactionSubCode = BDMOASCRATRANSSUBCODE.ADD;
    details.taxYearList = "2022";
    try {
      bdmOASCRATransactionFactory.createRequest(details);
    } catch (AppException | InformationalException e) {
      BDMOASAPPLICATIONCASE.TRASANCTION_EXIST.equals(e.getLocalizedMessage());
    }

  }

  @Test
  public void testCreateRequest()
    throws AppException, InformationalException {

    new Expectations() {

      BDMOASCRATransactionDtlsList tranList =
        new BDMOASCRATransactionDtlsList();

      PersonDtls personDtls = new PersonDtls();

      AlternateNameDtls nameDtls = new AlternateNameDtls();

      {
        final BDMOASCRATransactionDtls bdmOASCRATransactionDtls =
          new BDMOASCRATransactionDtls();
        bdmOASCRATransactionDtls.concernRoleID = CONCERNROLEID;
        bdmOASCRATransactionDtls.taxYear = "2021";
        bdmOASCRATransactionDtls.transactionSubCode =
          BDMOASCRATRANSSUBCODE.ADD;
        bdmOASCRATransactionDtls.status = BDMOASCRATRANSACTIONSTATUS.PENDING;
        bdmOASCRATransactionDtls.createdOn =
          Date.getCurrentDate().addDays(-CuramConst.gkTen).getDateTime();
        tranList.dtls.add(bdmOASCRATransactionDtls);
        bdmOASCRATransactionFactoryENT.searchByConcernRoleAndStatus(
          (BDMOASCRATransactionConcernRoleStatusKey) any);
        result = tranList;

        bdmOASCRATransactionFactoryENT.insert((BDMOASCRATransactionDtls) any);

        personDtls.primaryAlternateNameID = 141l;
        personDtls.dateOfBirth = Date.getCurrentDate().addDays(-9000);
        personFactory.read((PersonKey) any);
        result = personDtls;

        nameDtls.firstForename = "Henry";
        nameDtls.surname = "Lee";
        alternateNameFactory.read((curam.core.struct.AlternateNameKey) any);
        result = nameDtls;

        bdmOASCRADataFactory.insert((BDMOASCRADataDtls) any);
      }
    };

    final BDMOASCRARequestCreateDetails details =
      new BDMOASCRARequestCreateDetails();
    details.concernRoleID = CONCERNROLEID;
    details.transactionSubCode = BDMOASCRATRANSSUBCODE.ADD;
    details.taxYearList = "2022";
    bdmOASCRATransactionFactory.createRequest(details);

  }
  @Test
  public void testGetRequestOptionsForCase()
    throws AppException, InformationalException {

    new Expectations() {

      ParticipantAndCaseTypeDetails partCaseTypedtls =
        new ParticipantAndCaseTypeDetails();

      BDMOASCRATransactionDtlsList dtlsList =
        new BDMOASCRATransactionDtlsList();

      {
        partCaseTypedtls.concernRoleID = CONCERNROLEID;
        caseHeaderFactory
          .readParticipantAndCaseTypeDetails((CaseHeaderKey) any);
        result = partCaseTypedtls;

        final BDMOASCRATransactionDtls bdmOASCRATransactionDtls =
          new BDMOASCRATransactionDtls();
        bdmOASCRATransactionDtls.concernRoleID = CONCERNROLEID;
        bdmOASCRATransactionDtls.taxYear = "2022";
        bdmOASCRATransactionDtls.transactionSubCode =
          BDMOASCRATRANSSUBCODE.ADD;
        bdmOASCRATransactionDtls.status = BDMOASCRATRANSACTIONSTATUS.PENDING;
        bdmOASCRATransactionDtls.createdOn =
          Date.getCurrentDate().addDays(-CuramConst.gkTen).getDateTime();
        dtlsList.dtls.add(bdmOASCRATransactionDtls);
        bdmOASCRATransactionFactoryENT
          .searchByConcernRole((ConcernRoleKey) any);
        result = dtlsList;
      }
    };

    final CaseKey key = new CaseKey();
    key.caseID = CASEID;
    final BDMOASCRARequestCreateOptions bdmOASCRARequestCreateOptions =
      bdmOASCRATransactionFactory.getRequestOptionsForCase(key);
  }
}
