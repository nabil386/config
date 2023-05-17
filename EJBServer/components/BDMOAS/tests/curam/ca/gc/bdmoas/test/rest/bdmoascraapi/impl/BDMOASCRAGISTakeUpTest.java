package curam.ca.gc.bdmoas.test.rest.bdmoascraapi.impl;

import curam.ca.gc.bdm.codetable.BDMInterfaceNameCode;
import curam.ca.gc.bdm.codetable.BDMMARITALSTATUS;
import curam.ca.gc.bdm.entity.bdmsummary.fact.BDMInterfaceSummaryFactory;
import curam.ca.gc.bdm.entity.bdmsummary.struct.BDMCRADeathMatchSearchKey;
import curam.ca.gc.bdm.entity.bdmsummary.struct.BDMInterfaceSummaryDtlsList;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.sl.interfaces.impl.commonpojos.BDMAmount;
import curam.ca.gc.bdm.sl.interfaces.impl.commonpojos.BDMClientData;
import curam.ca.gc.bdm.sl.interfaces.impl.commonpojos.BDMIncomeDetail;
import curam.ca.gc.bdm.util.rest.impl.BDMRestUtil;
import curam.ca.gc.bdmoas.codetable.BDMOASCRALINEITEMTYPE;
import curam.ca.gc.bdmoas.codetable.BDMOASCRAMARITALSTATUS;
import curam.ca.gc.bdmoas.entity.cra.transaction.fact.BDMOASCRADataFactory;
import curam.ca.gc.bdmoas.entity.cra.transaction.fact.BDMOASCRAIncomeDataFactory;
import curam.ca.gc.bdmoas.entity.cra.transaction.fact.BDMOASCRATransactionFactory;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRADataDtls;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRAIncomeDataCRADataKey;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRAIncomeDataDtls;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRAIncomeDataDtlsList;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRATransactionDtls;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRATransactionDtlsList;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRATransactionKey;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.ca.gc.bdmoas.impl.BDMOASConstants;
import curam.ca.gc.bdmoas.rest.bdmoascraapi.impl.BDMOASCRAAPI;
import curam.ca.gc.bdmoas.rest.bdmoascraapi.struct.BDMOASCRARestRequest;
import curam.ca.gc.bdmoas.sl.interfaces.cra.impl.pojos.BDMOASGISTakeUpInbound;
import curam.ca.gc.bdmoas.test.concern.person.RegisterPerson;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.DUPLICATEREASON;
import curam.codetable.DUPLICATESTATUS;
import curam.codetable.RECORDSTATUS;
import curam.commonintake.entity.struct.ApplicationCaseKey;
import curam.core.facade.struct.CreateIntegratedCaseResult;
import curam.core.fact.ConcernRoleAlternateIDFactory;
import curam.core.fact.PersonRegistrationFactory;
import curam.core.impl.EnvVars;
import curam.core.sl.entity.fact.ConcernRoleDuplicateFactory;
import curam.core.sl.entity.intf.ConcernRoleDuplicate;
import curam.core.sl.entity.struct.ConcernRoleDuplicateDtls;
import curam.core.struct.ConcernRoleAlternateIDDtls;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.PersonRegistrationDetails;
import curam.core.struct.RegistrationIDDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.testframework.CuramServerTestJUnit4;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.type.Date;
import curam.util.type.Money;
import curam.util.type.UniqueID;
import java.util.Calendar;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Suite of tests for the BDMCRAAPI class.
 */
public class BDMOASCRAGISTakeUpTest extends CuramServerTestJUnit4 {

  private BDMOASCRAAPI testSubject;

  private BDMOASCRARestRequest testRequest;

  private BDMOASGISTakeUpInbound testRequestData;

  private BDMCRADeathMatchSearchKey interfaceSummaryKey;

  /**
   * Set up function for unit tests
   */
  @Before
  public void setUp() {

    testSubject = new BDMOASCRAAPI();
    testRequest = new BDMOASCRARestRequest();
    final String payloadData = "{ \"specversion\": \"1.0\", \"type\": "
      + "\"cra.income.gistakeup1-2\", \"source\": \"file://landingpag/"
      + "cra/gis/gistakeup20221201.flatfile\", \"id\": \"A234-1234-12"
      + "34\", \"Data\": { \"IndividualIncome\": { \"IndividualIncome"
      + "TaxationYear\": { \"YearDate\": \"2021\" }, \"IndividualInco"
      + "meStatus\": { \"StatusCode\": { \"ReferenceDataID\": \"01\","
      + " \"ReferenceDataName\": \"income found for tax year\" } }, "
      + "\"Client\": { \"ClientStatus\": { \"StatusCode\": { \"Refer"
      + "enceDataName\": \"Active\" }, \"StatusReasonCode\": { \"Ref"
      + "erenceDataName\": \"Deceased\" } }, \"PersonBirthDate\": { "
      + "\"date\": \"1990-01-01\" }, \"PersonContactInformation\": [{"
      + " \"ContactMailingAddress\": { \"AddressProvince\": { \"Prov"
      + "inceCode\": { \"ReferenceDataID\": \"QC\" } } } }], \"Person"
      + "DeathDate\": { \"date\": \"2022-10-01\" }, \"PersonMaritalS"
      + "tatus\": { \"StatusCode\": { \"ReferenceDataID\": \"88\" } "
      + "}, \"PersonName\": [ { \"PersonGivenName\": [ \"Johnny\" ],"
      + " \"PersonSurName\": \"Football\" } ], \"PersonGenderCode\":"
      + " { \"ReferenceDataName\": \"Man\" }, \"PersonSINIdentificat"
      + "ion\": { \"IdentificationID\": \"878333111\" } }, \"Related"
      + "Person\": [ { \"PersonRelationshipCode\": { \"ReferenceData"
      + "Name\": \"Spouse\" }, \"PersonSINIdentification\": { \"Iden"
      + "tificationID\": \"123456789\" } } ], \"IndividualIncomeBank"
      + "ruptcyCategoryCode\": { \"ReferenceDataName\": \"Post-Bankr"
      + "uptcy\" }, \"IndividualIncomeDetail\": [ ] } } }";
    testRequestData =
      BDMRestUtil.convertFromJSON(payloadData, BDMOASGISTakeUpInbound.class);

    interfaceSummaryKey = new BDMCRADeathMatchSearchKey();
    interfaceSummaryKey.interfaceNameCode =
      BDMInterfaceNameCode.CRA_GIS_TAKE_UP;
    interfaceSummaryKey.year = Integer.parseInt(testRequestData.getData()
      .getIndividualIncome().getIncomeTaxationYear().getYearDate());
  }

  /**
   * Test processCRAGISTakeUp() for scenario where the niemPayload provided is
   * an
   * incorrectly formed JSON.
   */
  @Test
  public void testProcessCRAGISTakeUp_invalidRequestBody()
    throws AppException, InformationalException {

    final BDMOASCRARestRequest request = new BDMOASCRARestRequest();
    request.niemPayload = "{test: }";
    assertThrows(AppException.class,
      () -> testSubject.processCRAGISTakeUp(request));

    // Test updates to summary report
    checkSummaryReport(false, false);
  }

  /**
   * Test processCRAGISTakeUp() for scenario where the niemPayload provided is a
   * null or empty string.
   */
  @Test
  public void testProcessCRAGISTakeUp_emptyOrNullNiemPayload()
    throws AppException, InformationalException {

    final BDMOASCRARestRequest request = new BDMOASCRARestRequest();

    request.niemPayload = "";
    assertThrows(AppException.class,
      () -> testSubject.processCRAGISTakeUp(request));

    request.niemPayload = null;
    assertThrows(AppException.class,
      () -> testSubject.processCRAGISTakeUp(request));

    // Test updates to summary report
    interfaceSummaryKey.year =
      Date.getCurrentDate().getCalendar().get(Calendar.YEAR);
    final BDMInterfaceSummaryDtlsList currentReport =
      BDMInterfaceSummaryFactory.newInstance()
        .searchByNameCodeAndYear(interfaceSummaryKey);
    assertEquals(2, currentReport.dtls.get(0).totalRecords);
    assertEquals(0, currentReport.dtls.get(0).totalSuccessRecords);
    assertEquals(0, currentReport.dtls.get(0).totalTasks);
  }

  /**
   * Test processCRAGISTakeUp() for scenario where the date of death is provided
   * in an incoming request record.
   */
  @Test
  public void testProcessCRAGISTakeUp_dateOfDeathProvided()
    throws AppException, InformationalException {

    final BDMOASCRARestRequest request = new BDMOASCRARestRequest();
    testRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    request.niemPayload = BDMRestUtil.convertToJSON(testRequestData);

    assertThrows(AppException.class,
      () -> testSubject.processCRAGISTakeUp(request));

    // Test updates to summary report
    checkSummaryReport(false, false);
  }

  /**
   * Test processCRAGISTakeUp() for the scenario where no date of birth info has
   * been provided in an incoming request record.
   */
  @Test
  public void testProcessCRAGISTakeUp_noDateOfBirthProvided()
    throws AppException, InformationalException {

    final BDMOASCRARestRequest request = new BDMOASCRARestRequest();
    testRequestData.getData().getIndividualIncome().getClient()
      .setDateOfDeath(null);
    testRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    testRequestData.getData().getIndividualIncome().getClient()
      .setDateOfBirth(null);

    request.niemPayload = BDMRestUtil.convertToJSON(testRequestData);

    assertThrows(AppException.class,
      () -> testSubject.processCRAGISTakeUp(request));

    // Test updates to summary report
    checkSummaryReport(false, false);
  }

  /**
   * Test processCRAGISTakeUp() for the scenario where the date of birth that
   * has
   * been provided is in an invalid format.
   */
  @Test
  public void testProcessCRAGISTakeUp_invalidDateOfBirthProvided()
    throws AppException, InformationalException {

    final BDMOASCRARestRequest request = new BDMOASCRARestRequest();
    testRequestData.getData().getIndividualIncome().getClient()
      .setDateOfDeath(null);
    testRequestData.getData().getIndividualIncome().getClient()
      .getDateOfBirth().setDate("02032023"); // MMDDYYYY
    testRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    request.niemPayload = BDMRestUtil.convertToJSON(testRequestData);

    assertThrows(AppException.class,
      () -> testSubject.processCRAGISTakeUp(request));

    // Test updates to summary report
    checkSummaryReport(false, false);
  }

  /**
   * Test processCRAGISTakeUp() for the scenario where no SIN info has
   * been provided in an incoming request record.
   */
  @Test
  public void testProcessCRAGISTakeUp_noSINProvided()
    throws AppException, InformationalException {

    final BDMOASCRARestRequest request = new BDMOASCRARestRequest();
    testRequestData.getData().getIndividualIncome().getClient()
      .setDateOfDeath(null);
    testRequestData.getData().getIndividualIncome().getClient()
      .setPersonSINIdentification(null);
    testRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    request.niemPayload = BDMRestUtil.convertToJSON(testRequestData);

    assertThrows(AppException.class,
      () -> testSubject.processCRAGISTakeUp(request));

    // Test updates to summary report
    checkSummaryReport(false, false);
  }

  /**
   * Test processCRAGISTakeUp() for the scenario where an invalid SIN has
   * been provided in an incoming request record.
   */
  @Test
  public void testProcessCRAGISTakeUp_invalidSINProvided()
    throws AppException, InformationalException {

    final BDMOASCRARestRequest request = new BDMOASCRARestRequest();
    testRequestData.getData().getIndividualIncome().getClient()
      .setDateOfDeath(null);
    testRequestData.getData().getIndividualIncome().getClient()
      .getPersonSINIdentification().setSin("0922812"); // 7 digits
    testRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    request.niemPayload = BDMRestUtil.convertToJSON(testRequestData);

    assertThrows(AppException.class,
      () -> testSubject.processCRAGISTakeUp(request));

    // Test updates to summary report
    checkSummaryReport(false, false);
  }

  /**
   * Test processCRAGISTakeUp() for the scenario where no info regarding the
   * client's active status has
   * been provided in an incoming request record.
   */
  @Test
  public void testProcessCRAGISTakeUp_noActiveStatusProvided()
    throws AppException, InformationalException {

    // Change active status
    final BDMOASCRARestRequest request = new BDMOASCRARestRequest();
    testRequestData.getData().getIndividualIncome().getClient()
      .setDateOfDeath(null);
    testRequestData.getData().getIndividualIncome().getClient()
      .setPersonStatus(null);
    testRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    request.niemPayload = BDMRestUtil.convertToJSON(testRequestData);

    assertThrows(AppException.class,
      () -> testSubject.processCRAGISTakeUp(request));

    // Test updates to summary report
    checkSummaryReport(false, true);
  }

  /**
   * Test processCRAGISTakeUp() for the scenario where an inactive status has
   * been provided in an incoming request record.
   */
  @Test
  public void testProcessCRAGISTakeUp_inactiveStatusProvided() {

    final BDMOASCRARestRequest request = new BDMOASCRARestRequest();
    testRequestData.getData().getIndividualIncome().getClient()
      .setDateOfDeath(null);
    testRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    testRequestData.getData().getIndividualIncome().getClient()
      .getPersonStatus().getStatusCode().setReferenceDataName("Inactive");
    request.niemPayload = BDMRestUtil.convertToJSON(testRequestData);

    assertThrows(AppException.class,
      () -> testSubject.processCRAGISTakeUp(request));

    // Test updates to summary report
    checkSummaryReport(false, true);
  }

  /**
   * Test processCRAGISTakeUp() for the scenario where bankruptcy code info has
   * been provided in an incoming request record.
   */
  @Test
  public void testProcessCRAGISTakeUp_bankruptcyFlagProvided()
    throws AppException, InformationalException {

    //
    final BDMOASCRARestRequest request = new BDMOASCRARestRequest();
    testRequestData.getData().getIndividualIncome().getClient()
      .setDateOfDeath(null);

    request.niemPayload = BDMRestUtil.convertToJSON(testRequestData);
    assertThrows(AppException.class,
      () -> testSubject.processCRAGISTakeUp(request));

    // Test updates to summary report
    checkSummaryReport(false, true);
  }

  /**
   * Test processCRAGISTakeUp() for the scenario where no surname info has
   * been provided in an incoming request record.
   */
  @Test
  public void testProcessCRAGISTakeUp_noSurnameProvided()
    throws AppException, InformationalException {

    // Remove surname from request
    final BDMOASCRARestRequest request = new BDMOASCRARestRequest();
    testRequestData.getData().getIndividualIncome().getClient()
      .setDateOfDeath(null);
    testRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    testRequestData.getData().getIndividualIncome().getClient()
      .getPersonNameList().get(0).setPersonSurname(null);

    request.niemPayload = BDMRestUtil.convertToJSON(testRequestData);
    assertThrows(AppException.class,
      () -> testSubject.processCRAGISTakeUp(request));

    // Test updates to summary report
    checkSummaryReport(false, false);
  }

  /**
   * Test processCRAGISTakeUp() for the scenario where no records can be found
   * in
   * backend for the person detailed in the request.
   */
  @Test
  public void testProcessCRAGISTakeUp_noRecordsFoundUsingSIN()
    throws AppException, InformationalException {

    final BDMOASCRARestRequest request = new BDMOASCRARestRequest();
    testRequestData.getData().getIndividualIncome().getClient()
      .setDateOfDeath(null);
    testRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    testRequestData.getData().getIndividualIncome().getClient()
      .getMaritalStatus().getStatusCode()
      .setReferenceDataID(BDMOASConstants.BDM_OAS_CRA_MARITAL_STATUS_SINGLE);
    testRequestData.getData().getIndividualIncome().getRelatedPerson()
      .clear();

    request.niemPayload = BDMRestUtil.convertToJSON(testRequestData);
    assertThrows(AppException.class,
      () -> testSubject.processCRAGISTakeUp(request));

    // Test updates to summary report
    checkSummaryReport(false, false);
  }

  /**
   * Test processCRAGISTakeUp() for the scenario where only duplicate records
   * can
   * be found in backend for the person detailed in the request.
   */
  @Test
  public void testProcessCRAGISTakeUp_duplicateRecordFound()
    throws AppException, InformationalException {

    // create person for test
    createClient(testRequestData.getData().getIndividualIncome().getClient(),
      true, 1);

    // update request
    final BDMOASCRARestRequest request = new BDMOASCRARestRequest();
    testRequestData.getData().getIndividualIncome().getClient()
      .setDateOfDeath(null);
    testRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    testRequestData.getData().getIndividualIncome().getClient()
      .getMaritalStatus().getStatusCode()
      .setReferenceDataID(BDMOASConstants.BDM_OAS_CRA_MARITAL_STATUS_SINGLE);
    testRequestData.getData().getIndividualIncome().getRelatedPerson()
      .clear();

    request.niemPayload = BDMRestUtil.convertToJSON(testRequestData);
    assertThrows(AppException.class,
      () -> testSubject.processCRAGISTakeUp(request));

    // Test updates to summary report
    checkSummaryReport(false, true);
  }

  /**
   * Test processCRAGISTakeUp() for the scenario where multiple records can be
   * found in
   * backend for the person detailed in the request.
   */
  @Test
  public void testProcessCRAGISTakeUp_multipleRecordsFound()
    throws AppException, InformationalException {

    // create people for test
    createClient(testRequestData.getData().getIndividualIncome().getClient(),
      false, 0);
    createClient(testRequestData.getData().getIndividualIncome().getClient(),
      false, 0);

    // Update request for test
    final BDMOASCRARestRequest request = new BDMOASCRARestRequest();
    testRequestData.getData().getIndividualIncome().getClient()
      .setDateOfDeath(null);
    testRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    testRequestData.getData().getIndividualIncome().getClient()
      .getMaritalStatus().getStatusCode()
      .setReferenceDataID(BDMOASConstants.BDM_OAS_CRA_MARITAL_STATUS_SINGLE);
    testRequestData.getData().getIndividualIncome().getRelatedPerson()
      .clear();

    request.niemPayload = BDMRestUtil.convertToJSON(testRequestData);
    assertThrows(AppException.class,
      () -> testSubject.processCRAGISTakeUp(request));

    // Test updates to summary report
    checkSummaryReport(false, true);
  }

  /**
   * Test processCRAGISTakeUp() for the scenario where there is a match using
   * SIN,
   * Surname and the month of the DOB.
   */
  @Test
  public void testProcessCRAGISTakeUp_surnameAndDOBMonthMatched()
    throws AppException, InformationalException {

    // Create user to test
    testRequestData.getData().getIndividualIncome().getClient()
      .getDateOfBirth().setDate("1990-01-10");
    final long clientID = createClient(
      testRequestData.getData().getIndividualIncome().getClient(), false, 0);

    // Update DOB year to mismatch
    final BDMOASCRARestRequest request = new BDMOASCRARestRequest();
    testRequestData.getData().getIndividualIncome().getClient()
      .setDateOfDeath(null);
    testRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    testRequestData.getData().getIndividualIncome().getClient()
      .getDateOfBirth().setDate("1994-01-10");
    testRequestData.getData().getIndividualIncome().getClient()
      .getMaritalStatus().getStatusCode()
      .setReferenceDataID(BDMOASConstants.BDM_OAS_CRA_MARITAL_STATUS_SINGLE);
    testRequestData.getData().getIndividualIncome().getRelatedPerson()
      .clear();

    request.niemPayload = BDMRestUtil.convertToJSON(testRequestData);
    testSubject.processCRAGISTakeUp(request);

    // Test updates to summary report
    checkSummaryReport(true, false);
  }

  /**
   * Test processCRAGISTakeUp() for the scenario where there is a match using
   * SIN,
   * Surname and the year of the DOB.
   */
  @Test
  public void testProcessCRAGISTakeUp_surnameAndDOBYearMatched()
    throws AppException, InformationalException {

    // Create user to test
    testRequestData.getData().getIndividualIncome().getClient()
      .getDateOfBirth().setDate("1990-01-10");
    final long clientID = createClient(
      testRequestData.getData().getIndividualIncome().getClient(), false, 0);

    // Update DOB month to mismatch
    final BDMOASCRARestRequest request = new BDMOASCRARestRequest();
    testRequestData.getData().getIndividualIncome().getClient()
      .setDateOfDeath(null);
    testRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    testRequestData.getData().getIndividualIncome().getClient()
      .getDateOfBirth().setDate("1990-03-10");
    testRequestData.getData().getIndividualIncome().getClient()
      .getMaritalStatus().getStatusCode()
      .setReferenceDataID(BDMOASConstants.BDM_OAS_CRA_MARITAL_STATUS_SINGLE);
    testRequestData.getData().getIndividualIncome().getRelatedPerson()
      .clear();

    request.niemPayload = BDMRestUtil.convertToJSON(testRequestData);
    testSubject.processCRAGISTakeUp(request);

    // Test updates to summary report
    checkSummaryReport(true, false);
  }

  /**
   * Test processCRAGISTakeUp() for the scenario where there is a match using
   * SIN
   * and DOB elements.
   */
  @Test
  public void testProcessCRAGISTakeUp_DOBMatched()
    throws AppException, InformationalException {

    // Create client to test
    testRequestData.getData().getIndividualIncome().getClient()
      .getDateOfBirth().setDate("1990-01-10");
    final long clientID = createClient(
      testRequestData.getData().getIndividualIncome().getClient(), false, 0);

    // Change last name of request
    final BDMOASCRARestRequest request = new BDMOASCRARestRequest();
    testRequestData.getData().getIndividualIncome().getClient()
      .setDateOfDeath(null);
    testRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    testRequestData.getData().getIndividualIncome().getClient()
      .getPersonNameList().get(0).setPersonSurname("Jackson");
    testRequestData.getData().getIndividualIncome().getClient()
      .getMaritalStatus().getStatusCode()
      .setReferenceDataID(BDMOASConstants.BDM_OAS_CRA_MARITAL_STATUS_SINGLE);
    testRequestData.getData().getIndividualIncome().getRelatedPerson()
      .clear();

    request.niemPayload = BDMRestUtil.convertToJSON(testRequestData);
    testSubject.processCRAGISTakeUp(request);

    // Test updates to summary report
    checkSummaryReport(true, false);
  }

  /**
   * Test processCRAGISTakeUp() for the scenario where only the SIN and surname
   * matched.
   */
  @Test
  public void testProcessCRAGISTakeUp_surnameMatchedOnly()
    throws AppException, InformationalException {

    // Create user for test
    testRequestData.getData().getIndividualIncome().getClient()
      .getDateOfBirth().setDate("1994-02-10");
    createClient(testRequestData.getData().getIndividualIncome().getClient(),
      false, 0);

    final BDMOASCRARestRequest request = new BDMOASCRARestRequest();
    testRequestData.getData().getIndividualIncome().getClient()
      .setDateOfDeath(null);
    testRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    testRequestData.getData().getIndividualIncome().getClient()
      .getDateOfBirth().setDate("1998-03-10");
    testRequestData.getData().getIndividualIncome().getClient()
      .getMaritalStatus().getStatusCode()
      .setReferenceDataID(BDMOASConstants.BDM_OAS_CRA_MARITAL_STATUS_SINGLE);
    testRequestData.getData().getIndividualIncome().getRelatedPerson()
      .clear();

    request.niemPayload = BDMRestUtil.convertToJSON(testRequestData);
    assertThrows(AppException.class,
      () -> testSubject.processCRAGISTakeUp(request));

    // Test updates to summary report
    checkSummaryReport(false, false);
  }

  /**
   * Test processCRAGISTakeUp() for the scenario where only the SIN and DOB
   * month
   * matched.
   */
  @Test
  public void testProcessCRAGISTakeUp_DOBMonthMatchedOnly()
    throws AppException, InformationalException {

    // Create user for test
    testRequestData.getData().getIndividualIncome().getClient()
      .getDateOfBirth().setDate("1994-02-10");
    createClient(testRequestData.getData().getIndividualIncome().getClient(),
      false, 0);

    // Change DOB year and surname
    final BDMOASCRARestRequest request = new BDMOASCRARestRequest();
    testRequestData.getData().getIndividualIncome().getClient()
      .setDateOfDeath(null);
    testRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    testRequestData.getData().getIndividualIncome().getClient()
      .getDateOfBirth().setDate("1998-02-10");
    testRequestData.getData().getIndividualIncome().getClient()
      .getMaritalStatus().getStatusCode()
      .setReferenceDataID(BDMOASConstants.BDM_OAS_CRA_MARITAL_STATUS_SINGLE);
    testRequestData.getData().getIndividualIncome().getRelatedPerson()
      .clear();
    testRequestData.getData().getIndividualIncome().getClient()
      .getPersonNameList().get(0).setPersonSurname("Williams");

    request.niemPayload = BDMRestUtil.convertToJSON(testRequestData);
    assertThrows(AppException.class,
      () -> testSubject.processCRAGISTakeUp(request));

    // Test updates to summary report
    checkSummaryReport(false, false);
  }

  /**
   * Test processCRAGISTakeUp() for the scenario where only the SIN and DOB year
   * matched.
   */
  @Test
  public void testProcessCRAGISTakeUp_DOBYearMatchedOnly()
    throws AppException, InformationalException {

    // Create user for test
    testRequestData.getData().getIndividualIncome().getClient()
      .getDateOfBirth().setDate("1998-09-10");
    createClient(testRequestData.getData().getIndividualIncome().getClient(),
      false, 0);

    // Update request for test
    final BDMOASCRARestRequest request = new BDMOASCRARestRequest();
    testRequestData.getData().getIndividualIncome().getClient()
      .setDateOfDeath(null);
    testRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    testRequestData.getData().getIndividualIncome().getClient()
      .getDateOfBirth().setDate("1998-02-10");
    testRequestData.getData().getIndividualIncome().getClient()
      .getMaritalStatus().getStatusCode()
      .setReferenceDataID(BDMOASConstants.BDM_OAS_CRA_MARITAL_STATUS_SINGLE);
    testRequestData.getData().getIndividualIncome().getRelatedPerson()
      .clear();
    testRequestData.getData().getIndividualIncome().getClient()
      .getPersonNameList().get(0).setPersonSurname("Williams"); // Change last
                                                                // name

    request.niemPayload = BDMRestUtil.convertToJSON(testRequestData);
    assertThrows(AppException.class,
      () -> testSubject.processCRAGISTakeUp(request));

    // Test updates to summary report
    checkSummaryReport(false, false);
  }

  /**
   * Test processCRAGISTakeUp() for scenario where an invalid marital status is
   * passed in from the request.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testProcessCRAGISTakeUp_storeMaritalInvalidStatus()
    throws AppException, InformationalException {

    // Create user for test
    final long concernRoleId = createClient(
      testRequestData.getData().getIndividualIncome().getClient(), false, 0);

    // Update request for test
    final BDMOASCRARestRequest request = new BDMOASCRARestRequest();
    testRequestData.getData().getIndividualIncome().getClient()
      .setDateOfDeath(null);
    testRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    testRequestData.getData().getIndividualIncome().getClient()
      .getMaritalStatus().getStatusCode().setReferenceDataID("A");

    request.niemPayload = BDMRestUtil.convertToJSON(testRequestData);
    assertThrows(AppException.class,
      () -> testSubject.processCRAGISTakeUp(request));

    // Test updates to summary report
    checkSummaryReport(false, false);

  }

  /**
   * Test processCRAGISTakeUp() for scenario where a single case's evidence is
   * updated.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testProcessCRAGISTakeUp_singleMaritalStatusUpdate()
    throws AppException, InformationalException {

    // Create user for test
    final long concernRoleId = createClient(
      testRequestData.getData().getIndividualIncome().getClient(), false, 0);
    addApplicationCase(concernRoleId);

    // Update request for test
    final BDMOASCRARestRequest request = new BDMOASCRARestRequest();
    testRequestData.getData().getIndividualIncome().getClient()
      .setDateOfDeath(null);
    testRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    testRequestData.getData().getIndividualIncome().getClient()
      .getMaritalStatus().getStatusCode()
      .setReferenceDataID(BDMOASConstants.BDM_OAS_CRA_MARITAL_STATUS_SINGLE);

    request.niemPayload = BDMRestUtil.convertToJSON(testRequestData);
    testSubject.processCRAGISTakeUp(request);

    // Test updates to summary report
    checkSummaryReport(true, false);

    // Single evidence is created
    final BDMEvidenceUtil evidenceUtil = new BDMEvidenceUtil();
    final List<DynamicEvidenceDataDetails> evidences =
      evidenceUtil.getEvdDtlsByConcernroleIDandEvidenceType(concernRoleId,
        CASEEVIDENCE.OAS_CRA_MARITAL_STATUS);
    assertEquals(1, evidences.size());
    assertEquals(BDMOASCRAMARITALSTATUS.SINGLE,
      evidences.get(0).getAttribute("maritalStatus").getValue());

  }

  /**
   * Test processCRAGISTakeUp() for scenario where multiple cases' evidences are
   * updated.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testProcessCRAGISTakeUp_updateMaritalMultipleCases()
    throws AppException, InformationalException {

    // Create user for test
    final long concernRoleId = createClient(
      testRequestData.getData().getIndividualIncome().getClient(), false, 0);
    addApplicationCase(concernRoleId);
    addIntegratedCase(concernRoleId);

    // Update request for test
    final BDMOASCRARestRequest request = new BDMOASCRARestRequest();
    testRequestData.getData().getIndividualIncome().getClient()
      .setDateOfDeath(null);
    testRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    testRequestData.getData().getIndividualIncome().getClient()
      .getMaritalStatus().getStatusCode()
      .setReferenceDataID(BDMOASConstants.BDM_OAS_CRA_MARITAL_STATUS_MARRIED);

    request.niemPayload = BDMRestUtil.convertToJSON(testRequestData);
    testSubject.processCRAGISTakeUp(request);

    // Test updates to summary report
    checkSummaryReport(true, false);

    // Single evidence for 2 cases
    final BDMEvidenceUtil evidenceUtil = new BDMEvidenceUtil();
    final List<DynamicEvidenceDataDetails> evidences =
      evidenceUtil.getEvdDtlsByConcernroleIDandEvidenceType(concernRoleId,
        CASEEVIDENCE.OAS_CRA_MARITAL_STATUS);
    assertEquals(2, evidences.size());
    assertEquals(BDMOASCRAMARITALSTATUS.MARRIED,
      evidences.get(0).getAttribute("maritalStatus").getValue());
  }

  /**
   * Test processCRAGISTakeUp() to attempt to store income items with line items
   * that cannot be mapped to.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testProcessCRAGISTakeUp_storeIncomeUnMappedLineItems()
    throws AppException, InformationalException {

    // Create user for test
    final long concernRoleId = createClient(
      testRequestData.getData().getIndividualIncome().getClient(), false, 0);

    // Update request for test
    final BDMOASCRARestRequest request = new BDMOASCRARestRequest();
    testRequestData.getData().getIndividualIncome().getClient()
      .setDateOfDeath(null);
    testRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    testRequestData.getData().getIndividualIncome().getClient()
      .getMaritalStatus().getStatusCode()
      .setReferenceDataID(BDMOASConstants.BDM_OAS_CRA_MARITAL_STATUS_SINGLE);
    testRequestData.getData().getIndividualIncome().getRelatedPerson()
      .clear();

    final BDMIncomeDetail incomeElement = new BDMIncomeDetail();
    final BDMAmount incomeAmount = new BDMAmount();
    incomeAmount.setAmount("500");
    incomeElement.setIncomeAmount(incomeAmount);
    incomeElement.setIncomeCalculatedIndicator(false);
    incomeElement.setIncomeLineNumber("99000");
    incomeElement.setIncomeName("New Income Value");

    testRequestData.getData().getIndividualIncome()
      .getIndividualIncomeDetail().add(incomeElement);

    request.niemPayload = BDMRestUtil.convertToJSON(testRequestData);
    testSubject.processCRAGISTakeUp(request);

    // Test updates to summary report
    checkSummaryReport(true, false);

    // Test BDMOASCRATransaction
    final ConcernRoleKey txnKey = new ConcernRoleKey();
    txnKey.concernRoleID = concernRoleId;
    final BDMOASCRATransactionDtlsList transactions =
      BDMOASCRATransactionFactory.newInstance().searchByConcernRole(txnKey);
    final BDMOASCRATransactionDtls transaction = transactions.dtls.stream()
      .filter(t -> t.taxYear.equals(testRequestData.getData()
        .getIndividualIncome().getIncomeTaxationYear().getYearDate()))
      .findAny().orElse(null);
    assertEquals(1, transactions.dtls.size());
    assertNotNull(transaction);
    assertEquals(concernRoleId, transaction.concernRoleID);

    // Test BDMOASCRAData
    final BDMOASCRATransactionKey dataKey = new BDMOASCRATransactionKey();
    dataKey.transactionID = transaction.transactionID;
    final BDMOASCRADataDtls dataDtls =
      BDMOASCRADataFactory.newInstance().readByTransaction(dataKey);
    assertNotNull(dataDtls);
    assertEquals("", dataDtls.spouseSIN);
    assertEquals(testRequestData.getData().getIndividualIncome().getClient()
      .getPersonSINIdentification().getSin(), dataDtls.sin);

    // Test BDMOASCRAIncomeData
    final BDMOASCRAIncomeDataCRADataKey incomeDataKey =
      new BDMOASCRAIncomeDataCRADataKey();
    incomeDataKey.dataID = dataDtls.dataID;
    final BDMOASCRAIncomeDataDtlsList incomeDataList =
      BDMOASCRAIncomeDataFactory.newInstance().searchByCRAData(incomeDataKey);

    // Assert empty because lineItem shouldn't be able to be mapped to
    assertEquals(0, incomeDataList.dtls.size());
  }

  /**
   * Test processCRAGISTakeUp() for scenario where incoming income details can
   * be
   * mapped to code table defining line items.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testProcessCRAGISTakeUp_storeIncomeMappedLineItems()
    throws AppException, InformationalException {

    // Create user for test
    final long concernRoleId = createClient(
      testRequestData.getData().getIndividualIncome().getClient(), false, 0);

    // Update request for test
    final BDMOASCRARestRequest request = new BDMOASCRARestRequest();
    testRequestData.getData().getIndividualIncome().getClient()
      .setDateOfDeath(null);
    testRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    testRequestData.getData().getIndividualIncome().getClient()
      .getMaritalStatus().getStatusCode()
      .setReferenceDataID(BDMOASConstants.BDM_OAS_CRA_MARITAL_STATUS_SINGLE);
    testRequestData.getData().getIndividualIncome().getRelatedPerson()
      .clear();

    final BDMIncomeDetail incomeElement = new BDMIncomeDetail();
    final BDMAmount incomeAmount = new BDMAmount();
    incomeAmount.setAmount("500");
    incomeElement.setIncomeAmount(incomeAmount);
    incomeElement.setIncomeCalculatedIndicator(false);
    incomeElement.setIncomeLineNumber("10400");
    incomeElement.setIncomeName("Other Employment Income");

    testRequestData.getData().getIndividualIncome()
      .getIndividualIncomeDetail().add(incomeElement);

    request.niemPayload = BDMRestUtil.convertToJSON(testRequestData);
    testSubject.processCRAGISTakeUp(request);

    // Test updates to summary report
    checkSummaryReport(true, false);

    // Test BDMOASCRATransaction
    final ConcernRoleKey txnKey = new ConcernRoleKey();
    txnKey.concernRoleID = concernRoleId;
    final BDMOASCRATransactionDtlsList transactions =
      BDMOASCRATransactionFactory.newInstance().searchByConcernRole(txnKey);
    final BDMOASCRATransactionDtls transaction = transactions.dtls.stream()
      .filter(t -> t.taxYear.equals("2021")).findAny().orElse(null);
    assertNotNull(transaction);
    assertEquals(1, transactions.dtls.size());

    // Test BDMOASCRAData
    final BDMOASCRATransactionKey dataKey = new BDMOASCRATransactionKey();
    dataKey.transactionID = transaction.transactionID;
    final BDMOASCRADataDtls dataDtls =
      BDMOASCRADataFactory.newInstance().readByTransaction(dataKey);
    assertNotNull(dataDtls);
    assertEquals("", dataDtls.spouseSIN);

    // Test BDMOASCRAIncomeData
    final BDMOASCRAIncomeDataCRADataKey incomeDataKey =
      new BDMOASCRAIncomeDataCRADataKey();
    incomeDataKey.dataID = dataDtls.dataID;
    final BDMOASCRAIncomeDataDtlsList incomeDataList =
      BDMOASCRAIncomeDataFactory.newInstance().searchByCRAData(incomeDataKey);
    final BDMOASCRAIncomeDataDtls incomeData = incomeDataList.dtls.stream()
      .filter(record -> record.lineItem
        .equals(BDMOASCRALINEITEMTYPE.OTHER_EMPT_INCOME))
      .findAny().orElse(null);

    assertNotNull(incomeData);
    assertEquals(1, incomeDataList.dtls.size());
    assertTrue(new Money("500").compareTo(incomeData.amount) == 0);
  }

  /**
   * Test processCRAGISTakeUp() for scenario where incoming income details can
   * be
   * mapped to code table defining line items. For this scenario, the spouse SIN
   * is also provided.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testProcessCRAGISTakeUp_storeIncomeSpouseProvided()
    throws AppException, InformationalException {

    // Create user for test
    final long concernRoleId = createClient(
      testRequestData.getData().getIndividualIncome().getClient(), false, 0);

    // Update request for test
    final BDMOASCRARestRequest request = new BDMOASCRARestRequest();
    testRequestData.getData().getIndividualIncome().getClient()
      .setDateOfDeath(null);
    testRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    testRequestData.getData().getIndividualIncome().getClient()
      .getMaritalStatus().getStatusCode()
      .setReferenceDataID(BDMOASConstants.BDM_OAS_CRA_MARITAL_STATUS_MARRIED);

    final BDMIncomeDetail incomeElement = new BDMIncomeDetail();
    final BDMAmount incomeAmount = new BDMAmount();
    incomeAmount.setAmount("500");
    incomeElement.setIncomeAmount(incomeAmount);
    incomeElement.setIncomeCalculatedIndicator(false);
    incomeElement.setIncomeLineNumber("10400");
    incomeElement.setIncomeName("Other Employment Income");

    testRequestData.getData().getIndividualIncome()
      .getIndividualIncomeDetail().add(incomeElement);

    request.niemPayload = BDMRestUtil.convertToJSON(testRequestData);
    testSubject.processCRAGISTakeUp(request);

    // Test updates to summary report
    checkSummaryReport(true, false);

    // Test BDMOASCRATransaction
    final ConcernRoleKey txnKey = new ConcernRoleKey();
    txnKey.concernRoleID = concernRoleId;
    final BDMOASCRATransactionDtlsList transactions =
      BDMOASCRATransactionFactory.newInstance().searchByConcernRole(txnKey);
    final BDMOASCRATransactionDtls transaction = transactions.dtls.stream()
      .filter(t -> t.taxYear.equals("2021")).findAny().orElse(null);
    assertNotNull(transaction);

    // Test BDMOASCRAData
    final BDMOASCRATransactionKey dataKey = new BDMOASCRATransactionKey();
    dataKey.transactionID = transaction.transactionID;
    final BDMOASCRADataDtls dataDtls =
      BDMOASCRADataFactory.newInstance().readByTransaction(dataKey);
    assertNotNull(dataDtls);
    assertTrue(dataDtls.spouseSIN.equals(testRequestData.getData()
      .getIndividualIncome().getRelatedPerson().get(0).getSin().getSin()));

    // Test BDMOASCRAIncomeData
    final BDMOASCRAIncomeDataCRADataKey incomeDataKey =
      new BDMOASCRAIncomeDataCRADataKey();
    incomeDataKey.dataID = dataDtls.dataID;
    final BDMOASCRAIncomeDataDtlsList incomeDataList =
      BDMOASCRAIncomeDataFactory.newInstance().searchByCRAData(incomeDataKey);
    final BDMOASCRAIncomeDataDtls incomeData = incomeDataList.dtls.stream()
      .filter(record -> record.lineItem
        .equals(BDMOASCRALINEITEMTYPE.OTHER_EMPT_INCOME))
      .findAny().orElse(null);

    assertNotNull(incomeData);
    assertEquals(1, incomeDataList.dtls.size());
    assertTrue(new Money("500").compareTo(incomeData.amount) == 0);
  }

  /**
   * Helper function to create client data for testing GIS Take Up requests
   *
   * @param client - client data
   * @param isDuplicate - indicator for if client is duplicate
   * @param originalConcernRoleId - original client's concernRoleId (if created
   * client will be duplicate)
   * @return ConcernRoleId for newly created client
   * @throws AppException
   * @throws InformationalException
   */
  public long createClient(final BDMClientData client,
    final boolean isDuplicate, final long originalConcernRoleId)
    throws AppException, InformationalException {

    final String pdcProperty =
      Configuration.getProperty(EnvVars.ENV_PDC_ENABLED);
    Configuration.setProperty(EnvVars.ENV_PDC_ENABLED, "false");

    final String date = client.getDateOfBirth().getDate().replaceAll("-", "");
    final PersonRegistrationDetails originalPerson =
      new RegisterPerson(this.getName()).getPersonRegistrationDetails(
        client.getPersonNameList().get(0).getPersonGivenName()[0],
        client.getPersonNameList().get(0).getPersonSurname(),
        Date.getDate(date));
    originalPerson.currentMaritalStatus = BDMMARITALSTATUS.SINGLE;
    final RegistrationIDDetails personDtls =
      PersonRegistrationFactory.newInstance().registerPerson(originalPerson);

    final curam.core.intf.ConcernRoleAlternateID craidObj =
      ConcernRoleAlternateIDFactory.newInstance();
    final ConcernRoleAlternateIDDtls alternateDtls =
      new ConcernRoleAlternateIDDtls();
    alternateDtls.concernRoleID = personDtls.concernRoleID;
    alternateDtls.alternateID = client.getPersonSINIdentification().getSin();
    alternateDtls.typeCode = CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER;
    alternateDtls.statusCode = RECORDSTATUS.NORMAL;
    alternateDtls.concernRoleAlternateID = UniqueID.nextUniqueID();
    craidObj.insert(alternateDtls);

    if (isDuplicate) {
      final ConcernRoleDuplicate crdObj =
        ConcernRoleDuplicateFactory.newInstance();
      final ConcernRoleDuplicateDtls duplicateDtls =
        new ConcernRoleDuplicateDtls();
      duplicateDtls.originalConcernRoleID = originalConcernRoleId;
      duplicateDtls.duplicateConcernRoleID = personDtls.concernRoleID;
      duplicateDtls.statusCode = DUPLICATESTATUS.MARKED;
      duplicateDtls.duplicateDate = Date.getCurrentDate();
      duplicateDtls.duplicateReason = DUPLICATEREASON.PERSONMATCH;
      crdObj.insert(duplicateDtls);
    }

    Configuration.setProperty(EnvVars.ENV_PDC_ENABLED, pdcProperty);

    return personDtls.concernRoleID;
  }

  /**
   * Helper function to test updates to the BDMINTERFACESUMMARY table.
   *
   * @param isSuccessful - Indicator for if operation was successful
   * @param hasTask - Indicator for if operation created a task
   */
  public void checkSummaryReport(final boolean isSuccessful,
    final boolean hasTask) {

    // Test updates to summary report
    BDMInterfaceSummaryDtlsList currentReport;
    try {
      currentReport = BDMInterfaceSummaryFactory.newInstance()
        .searchByNameCodeAndYear(interfaceSummaryKey);
      assertEquals(1, currentReport.dtls.get(0).totalRecords);
      assertEquals(isSuccessful ? 1 : 0,
        currentReport.dtls.get(0).totalSuccessRecords);
      assertEquals(hasTask ? 1 : 0, currentReport.dtls.get(0).totalTasks);
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Helper to create application case.
   *
   * @param concernRoleId - Client concernRoleId
   * @return case id
   * @throws AppException
   * @throws InformationalException
   */
  public long addApplicationCase(final long concernRoleId)
    throws AppException, InformationalException {

    final ApplicationCaseKey applicationCaseKey =
      new BDMOASCaseTest().createApplicationCase(concernRoleId, 920000);
    return applicationCaseKey.applicationCaseID;
  }

  /**
   * Helper to create integrated case.
   *
   * @param concernRoleId - Client concernRoleId
   * @return case id
   * @throws AppException
   * @throws InformationalException
   */
  public long addIntegratedCase(final long concernRoleId)
    throws AppException, InformationalException {

    final CreateIntegratedCaseResult ic =
      new BDMOASCaseTest().createIntegratedCase(concernRoleId);
    return ic.integratedCaseID;
  }

  @Override
  public boolean shouldCommit() {

    return false;
  }
}
