package curam.ca.gc.bdmoas.test.rest.bdmoascraapi.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMInterfaceNameCode;
import curam.ca.gc.bdm.codetable.BDMMARITALSTATUS;
import curam.ca.gc.bdm.entity.bdmsummary.fact.BDMInterfaceSummaryFactory;
import curam.ca.gc.bdm.entity.bdmsummary.intf.BDMInterfaceSummary;
import curam.ca.gc.bdm.entity.bdmsummary.struct.BDMCRADeathMatchSearchKey;
import curam.ca.gc.bdm.entity.bdmsummary.struct.BDMInterfaceSummaryDtlsList;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSINKey;
import curam.ca.gc.bdm.message.BDMBPOCCT;
import curam.ca.gc.bdm.sl.interfaces.impl.commonpojos.BDMPersonName;
import curam.ca.gc.bdm.util.impl.BDMDateUtil;
import curam.ca.gc.bdm.util.rest.impl.BDMRestUtil;
import curam.ca.gc.bdmoas.entity.fact.BDMOASPersonFactory;
import curam.ca.gc.bdmoas.entity.intf.BDMOASPerson;
import curam.ca.gc.bdmoas.entity.struct.BDMOASPersonSummaryDetails;
import curam.ca.gc.bdmoas.entity.struct.BDMOASPersonSummaryDetailsList;
import curam.ca.gc.bdmoas.impl.BDMOASConstants;
import curam.ca.gc.bdmoas.rest.bdmoascraapi.fact.BDMOASCRAAPIFactory;
import curam.ca.gc.bdmoas.rest.bdmoascraapi.impl.BDMOASCRAHelper;
import curam.ca.gc.bdmoas.rest.bdmoascraapi.struct.BDMOASCRARestRequest;
import curam.ca.gc.bdmoas.sl.interfaces.cra.impl.pojos.BDMOASGISTakeUpInbound;
import curam.codetable.ADDRESSLAYOUTTYPE;
import curam.codetable.CONCERNROLETYPE;
import curam.codetable.COUNTRY;
import curam.codetable.DUPLICATESTATUS;
import curam.core.facade.fact.ClientMergeFactory;
import curam.core.facade.intf.ClientMerge;
import curam.core.facade.struct.MarkDuplicateCreateDetails;
import curam.core.facade.struct.MarkDuplicateDetails;
import curam.core.fact.AddressFactory;
import curam.core.fact.AlternateNameFactory;
import curam.core.fact.ConcernFactory;
import curam.core.fact.ConcernRoleAlternateIDFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.fact.PersonFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.impl.EnvVars;
import curam.core.intf.Person;
import curam.core.intf.UniqueID;
import curam.core.struct.AddressDtls;
import curam.core.struct.AlternateNameDtls;
import curam.core.struct.ConcernDtls;
import curam.core.struct.ConcernRoleAlternateIDDtls;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonKey;
import curam.testframework.CuramServerTestJUnit4;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BDMOASGISCRAAssessmentTest extends CuramServerTestJUnit4 {

  private static curam.ca.gc.bdmoas.rest.bdmoascraapi.intf.BDMOASCRAAPI testSubject;

  private static BDMOASCRARestRequest testRequest;

  private BDMOASGISTakeUpInbound testGISCRAAssesssmentRequestData;

  @Inject
  BDMOASCRAHelper craHelper;

  private final String requestGISCRAssessmentSampleData =
    "{ \"specversion\": \"1.0\", \"type\": "
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
      + "tatus\": { \"StatusCode\": { \"ReferenceDataID\": \"1\" } "
      + "}, \"PersonName\": [ { \"PersonGivenName\": [ \"Johnny\" ],"
      + " \"PersonSurName\": \"Football\" } ], \"PersonGenderCode\":"
      + " { \"ReferenceDataName\": \"Man\" }, \"PersonSINIdentificat"
      + "ion\": { \"IdentificationID\": \"878333111\" } }, \"Related"
      + "Person\": [ { \"PersonRelationshipCode\": { \"ReferenceData"
      + "Name\": \"Spouse\" }, \"PersonSINIdentification\": { \"Iden"
      + "tificationID\": \"123456789\" } } ], \"IndividualIncomeBank"
      + "ruptcyCategoryCode\": { \"ReferenceDataName\": \"Post-Bankr"
      + "uptcy\" }, \"IndividualIncomeDetail\": [ ] } } }";

  @BeforeClass
  public static void classSetup() {

    testSubject = BDMOASCRAAPIFactory.newInstance();
    testRequest = new BDMOASCRARestRequest();

  }

  @Before
  /**
   * Set up function for CRA Death match tests
   */
  public void setUpCRADeathMatchTests() {

    super.setUpCuramServerTest();
    craHelper = new BDMOASCRAHelper();

    Configuration.setProperty(EnvVars.ENV_PDC_ENABLED, "NO");

    testGISCRAAssesssmentRequestData = BDMRestUtil.convertFromJSON(
      requestGISCRAssessmentSampleData, BDMOASGISTakeUpInbound.class);

  }

  @Test
  public void testProcessCRAGISAssessment_invalidNeimPayload()
    throws Exception {

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(new BDMOASGISTakeUpInbound());
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Invalid request body", exception.getMessage());

  }

  @Test
  public void testProcessCRAGISAssessment_nullNeimPayload() throws Exception {

    testRequest.niemPayload = null;
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Invalid request body", exception.getMessage());

  }

  @Test
  public void testProcessCRAGISAssessment_blankNeimPayload()
    throws Exception {

    testRequest.niemPayload = "   ";
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Invalid request body", exception.getMessage());

  }

  @Test
  public void testProcessCRAGISAssessment_emptyNeimPayload()
    throws Exception {

    testRequest.niemPayload = "";
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Invalid request body", exception.getMessage());

  }

  @Test
  public void testProcessCRAGISAssessment_emptyDOB() throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().getDateOfBirth().setDate("");
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfDeath(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Missing or invalid format for Date of Birth",
      exception.getMessage());
  }

  @Test
  public void
    testProcessCRAGISAssessment_differentIncomeTaxationYearProvided()
      throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getIncomeTaxationYear().setYearDate("2019");

    final String personFirstName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonGivenName()[0];
    final String personSurName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonSurname();
    final String personBirthName = personFirstName + " " + personSurName;
    final String dateOfBirth = testGISCRAAssesssmentRequestData.getData()
      .getIndividualIncome().getClient().getDateOfBirth().getDate();
    final String isoDOB = BDMDateUtil.dateFormatter(dateOfBirth);
    final String sin =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonSINIdentification().getSin();

    final UniqueID uniqueID = UniqueIDFactory.newInstance();

    final PersonDtls originalPersonDtls = new PersonDtls();
    originalPersonDtls.concernRoleID = uniqueID.getNextID();
    originalPersonDtls.personBirthName = personBirthName;
    originalPersonDtls.dateOfBirth = Date.getDate(isoDOB);
    originalPersonDtls.maritalStatusCode = BDMMARITALSTATUS.MARRIED;
    createPersonRecordWithSin(originalPersonDtls, sin,
      DUPLICATESTATUS.UNMARKED);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);

    final BDMInterfaceSummary interfaceSummaryObj =
      BDMInterfaceSummaryFactory.newInstance();

    final BDMCRADeathMatchSearchKey searchKey =
      new BDMCRADeathMatchSearchKey();
    searchKey.interfaceNameCode = BDMInterfaceNameCode.CRA_GIS_REASSESSMENT;
    searchKey.year =
      Date.getFromJavaUtilDate(new SimpleDateFormat("yyyy").parse("2019"))
        .getCalendar().get(Calendar.YEAR);

    final BDMInterfaceSummaryDtlsList records =
      interfaceSummaryObj.searchByNameCodeAndYear(searchKey);

    assertEquals(0, records.dtls.size());

    testSubject.processCRAGISAssessment(testRequest);

    assertEquals(1,
      interfaceSummaryObj.searchByNameCodeAndYear(searchKey).dtls.size());
  }

  @Test
  public void testProcessCRAGISAssessment_noIncomeTaxationYearProvided()
    throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getIncomeTaxationYear().setYearDate(null);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Income Taxation Year is missing for request",
      exception.getMessage());
  }

  @Test
  public void testProcessCRAGISAssessment_blankDOB() throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().getDateOfBirth().setDate("   ");
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfDeath(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Missing or invalid format for Date of Birth",
      exception.getMessage());
  }

  @Test
  public void testProcessCRAGISAssessment_invalidDOBFormat()
    throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().getDateOfBirth().setDate("01-01-2001");
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfDeath(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Missing or invalid format for Date of Birth",
      exception.getMessage());
  }

  @Test
  public void testProcessCRAGISAssessment_noDOBProvided() throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfBirth(null);

    final String personFirstName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonGivenName()[0];
    final String personSurName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonSurname();
    final String personBirthName = personFirstName + " " + personSurName;
    final String sin =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonSINIdentification().getSin();

    final UniqueID uniqueID = UniqueIDFactory.newInstance();

    final PersonDtls personDtls = new PersonDtls();
    personDtls.concernRoleID = uniqueID.getNextID();
    personDtls.personBirthName = personBirthName;
    personDtls.dateOfBirth = Date.getDate("19910101");
    personDtls.maritalStatusCode = BDMMARITALSTATUS.SINGLE;
    createPersonRecordWithSin(personDtls, sin, DUPLICATESTATUS.UNMARKED);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Invalid request body", exception.getMessage());

  }

  @Test
  public void testProcessCRAGISAssessment_nullDOBProvided() throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().getDateOfBirth().setDate(null);

    final String personFirstName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonGivenName()[0];
    final String personSurName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonSurname();
    final String personBirthName = personFirstName + " " + personSurName;
    final String sin =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonSINIdentification().getSin();

    final UniqueID uniqueID = UniqueIDFactory.newInstance();

    final PersonDtls personDtls = new PersonDtls();
    personDtls.concernRoleID = uniqueID.getNextID();
    personDtls.personBirthName = personBirthName;
    personDtls.dateOfBirth = Date.getDate("19910101");
    personDtls.maritalStatusCode = BDMMARITALSTATUS.SINGLE;
    createPersonRecordWithSin(personDtls, sin, DUPLICATESTATUS.UNMARKED);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Missing or invalid format for Date of Birth",
      exception.getMessage());

  }

  @Test
  public void testProcessCRAGISAssessment_blankDODProvided()
    throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().getDateOfDeath().setDate(" ");

    final String personFirstName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonGivenName()[0];
    final String personSurName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonSurname();
    final String personBirthName = personFirstName + " " + personSurName;
    final String dateOfBirth = testGISCRAAssesssmentRequestData.getData()
      .getIndividualIncome().getClient().getDateOfBirth().getDate();
    final String isoDOB = BDMDateUtil.dateFormatter(dateOfBirth);
    final String sin =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonSINIdentification().getSin();

    final UniqueID uniqueID = UniqueIDFactory.newInstance();

    final PersonDtls personDtls = new PersonDtls();
    personDtls.concernRoleID = uniqueID.getNextID();
    personDtls.personBirthName = personBirthName;
    personDtls.dateOfBirth = Date.getDate(isoDOB);
    personDtls.maritalStatusCode = BDMMARITALSTATUS.SINGLE;
    createPersonRecordWithSin(personDtls, sin, DUPLICATESTATUS.UNMARKED);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Invalid Death Of Death", exception.getMessage());
  }

  @Test
  public void testProcessCRAGISAssessment_noDODProvided() throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfDeath(null);

    final String personFirstName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonGivenName()[0];
    final String personSurName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonSurname();
    final String personBirthName = personFirstName + " " + personSurName;
    final String dateOfBirth = testGISCRAAssesssmentRequestData.getData()
      .getIndividualIncome().getClient().getDateOfBirth().getDate();
    final String isoDOB = BDMDateUtil.dateFormatter(dateOfBirth);
    final String sin =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonSINIdentification().getSin();

    final UniqueID uniqueID = UniqueIDFactory.newInstance();

    final PersonDtls personDtls = new PersonDtls();
    personDtls.concernRoleID = uniqueID.getNextID();
    personDtls.personBirthName = personBirthName;
    personDtls.dateOfBirth = Date.getDate(isoDOB);
    personDtls.maritalStatusCode = BDMMARITALSTATUS.SINGLE;
    createPersonRecordWithSin(personDtls, sin, DUPLICATESTATUS.UNMARKED);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    testSubject.processCRAGISAssessment(testRequest);

    // TODO: check that no task has been created
  }

  @Test
  public void testProcessCRAGISAssessment_emptyDODProvided()
    throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().getDateOfDeath().setDate("");

    final String personFirstName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonGivenName()[0];
    final String personSurName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonSurname();
    final String personBirthName = personFirstName + " " + personSurName;
    final String dateOfBirth = testGISCRAAssesssmentRequestData.getData()
      .getIndividualIncome().getClient().getDateOfBirth().getDate();
    final String isoDOB = BDMDateUtil.dateFormatter(dateOfBirth);
    final String sin =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonSINIdentification().getSin();

    final UniqueID uniqueID = UniqueIDFactory.newInstance();

    final PersonDtls personDtls = new PersonDtls();
    personDtls.concernRoleID = uniqueID.getNextID();
    personDtls.personBirthName = personBirthName;
    personDtls.dateOfBirth = Date.getDate(isoDOB);
    personDtls.maritalStatusCode = BDMMARITALSTATUS.SINGLE;
    createPersonRecordWithSin(personDtls, sin, DUPLICATESTATUS.UNMARKED);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Invalid Death Of Death", exception.getMessage());
  }

  @Test
  public void testProcessCRAGISAssessment_DODProvidedMatchingYearButNotMonth()
    throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().getDateOfDeath().setDate("1991-03-17");

    final String personFirstName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonGivenName()[0];
    final String personSurName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonSurname();
    final String personBirthName = personFirstName + " " + personSurName;
    final String personDOB = testGISCRAAssesssmentRequestData.getData()
      .getIndividualIncome().getClient().getDateOfBirth().getDate();

    final String isoDOB = BDMDateUtil.dateFormatter(personDOB);
    final String isoDOD = BDMDateUtil.dateFormatter("1991-04-17");
    final String sin =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonSINIdentification().getSin();

    final UniqueID uniqueID = UniqueIDFactory.newInstance();

    final PersonDtls testPersonDtls = new PersonDtls();
    testPersonDtls.concernRoleID = uniqueID.getNextID();
    testPersonDtls.personBirthName = personBirthName;
    testPersonDtls.dateOfBirth = Date.getDate(isoDOB);
    testPersonDtls.dateOfDeath = Date.getDate(isoDOD);
    testPersonDtls.maritalStatusCode = BDMMARITALSTATUS.SINGLE;
    createPersonRecordWithSin(testPersonDtls, sin, DUPLICATESTATUS.UNMARKED);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    testSubject.processCRAGISAssessment(testRequest);

    final PersonKey key = new PersonKey();
    key.concernRoleID = testPersonDtls.concernRoleID;
    final Person personObj = PersonFactory.newInstance();
    final PersonDtls actualPersonDtls = personObj.read(key);

    final Date expectedDOD =
      Date.getDate(BDMDateUtil.dateFormatter("1991-03-17"));
    final Date actualDOD = actualPersonDtls.dateOfDeath;

    assertEquals(expectedDOD, actualDOD);

    // TODO: check if a task has been created
  }

  @Test
  public void testProcessCRAGISAssessment_DODProvidedMatchingMonthButNotYear()
    throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().getDateOfDeath().setDate("1991-03-17");

    final String personFirstName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonGivenName()[0];
    final String personSurName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonSurname();
    final String personBirthName = personFirstName + " " + personSurName;
    final String personDOB = testGISCRAAssesssmentRequestData.getData()
      .getIndividualIncome().getClient().getDateOfBirth().getDate();

    final String isoDOB = BDMDateUtil.dateFormatter(personDOB);
    final String isoDOD = BDMDateUtil.dateFormatter("1992-03-17");
    final String sin =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonSINIdentification().getSin();

    final UniqueID uniqueID = UniqueIDFactory.newInstance();

    final PersonDtls testPersonDtls = new PersonDtls();
    testPersonDtls.concernRoleID = uniqueID.getNextID();
    testPersonDtls.personBirthName = personBirthName;
    testPersonDtls.dateOfBirth = Date.getDate(isoDOB);
    testPersonDtls.dateOfDeath = Date.getDate(isoDOD);
    testPersonDtls.maritalStatusCode = BDMMARITALSTATUS.SINGLE;
    createPersonRecordWithSin(testPersonDtls, sin, DUPLICATESTATUS.UNMARKED);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    testSubject.processCRAGISAssessment(testRequest);

    final PersonKey key = new PersonKey();
    key.concernRoleID = testPersonDtls.concernRoleID;
    final Person personObj = PersonFactory.newInstance();
    final PersonDtls actualPersonDtls = personObj.read(key);

    final Date expectedDOD =
      Date.getDate(BDMDateUtil.dateFormatter("1991-03-17"));
    final Date actualDOD = actualPersonDtls.dateOfDeath;

    assertEquals(expectedDOD, actualDOD);

    // TODO: check if a task has been created

  }

  @Test
  public void testProcessCRAGISAssessment_DODProvidedMatchingYearAndMonth()
    throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().getDateOfDeath().setDate("1991-03-17");

    final String personFirstName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonGivenName()[0];
    final String personSurName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonSurname();
    final String personBirthName = personFirstName + " " + personSurName;
    final String personDOB = testGISCRAAssesssmentRequestData.getData()
      .getIndividualIncome().getClient().getDateOfBirth().getDate();

    final String isoDOB = BDMDateUtil.dateFormatter(personDOB);
    final String isoDOD = BDMDateUtil.dateFormatter("1991-03-17");
    final String sin =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonSINIdentification().getSin();

    final UniqueID uniqueID = UniqueIDFactory.newInstance();

    final PersonDtls testPersonDtls = new PersonDtls();
    testPersonDtls.concernRoleID = uniqueID.getNextID();
    testPersonDtls.personBirthName = personBirthName;
    testPersonDtls.dateOfBirth = Date.getDate(isoDOB);
    testPersonDtls.dateOfDeath = Date.getDate(isoDOD);
    testPersonDtls.maritalStatusCode = BDMMARITALSTATUS.SINGLE;
    createPersonRecordWithSin(testPersonDtls, sin, DUPLICATESTATUS.UNMARKED);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    testSubject.processCRAGISAssessment(testRequest);

    final PersonKey key = new PersonKey();
    key.concernRoleID = testPersonDtls.concernRoleID;
    final Person personObj = PersonFactory.newInstance();
    final PersonDtls actualPersonDtls = personObj.read(key);

    final Date expectedDOD =
      Date.getDate(BDMDateUtil.dateFormatter("1991-03-17"));
    final Date actualDOD = actualPersonDtls.dateOfDeath;

    assertEquals(expectedDOD, actualDOD);

    // TODO: check that no task has been created
  }

  @Test
  public void testProcessCRAGISAssessment_DODProvidedNotMatchingYearOrMonth()
    throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().getDateOfDeath().setDate("1991-03-17");

    final String personFirstName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonGivenName()[0];
    final String personSurName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonSurname();
    final String personBirthName = personFirstName + " " + personSurName;
    final String personDOB = testGISCRAAssesssmentRequestData.getData()
      .getIndividualIncome().getClient().getDateOfBirth().getDate();

    final String isoDOB = BDMDateUtil.dateFormatter(personDOB);
    final String isoDOD = BDMDateUtil.dateFormatter("1992-04-17");
    final String sin =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonSINIdentification().getSin();

    final UniqueID uniqueID = UniqueIDFactory.newInstance();

    final PersonDtls testPersonDtls = new PersonDtls();
    testPersonDtls.concernRoleID = uniqueID.getNextID();
    testPersonDtls.personBirthName = personBirthName;
    testPersonDtls.dateOfBirth = Date.getDate(isoDOB);
    testPersonDtls.dateOfDeath = Date.getDate(isoDOD);
    testPersonDtls.maritalStatusCode = BDMMARITALSTATUS.SINGLE;
    createPersonRecordWithSin(testPersonDtls, sin, DUPLICATESTATUS.UNMARKED);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    testSubject.processCRAGISAssessment(testRequest);

    final PersonKey key = new PersonKey();
    key.concernRoleID = testPersonDtls.concernRoleID;
    final Person personObj = PersonFactory.newInstance();
    final PersonDtls actualPersonDtls = personObj.read(key);

    final Date expectedDOD =
      Date.getDate(BDMDateUtil.dateFormatter("1991-03-17"));
    final Date actualDOD = actualPersonDtls.dateOfDeath;

    assertEquals(expectedDOD, actualDOD);

    // TODO: check if a task has been created
  }

  @Test
  public void testProcessCRAGISAssessment_invalidSinFormat()
    throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().getPersonSINIdentification().setSin("test-sin");
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfDeath(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Missing or invalid format for SIN", exception.getMessage());

  }

  @Test
  public void testProcessCRAGISAssessment_noSinProvided() throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setPersonSINIdentification(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfDeath(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Invalid request body", exception.getMessage());

  }

  @Test
  public void testProcessCRAGISAssessment_nullSinProvided() throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().getPersonSINIdentification().setSin(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfDeath(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Missing or invalid format for SIN", exception.getMessage());

  }

  @Test
  public void testProcessCRAGISAssessment_emptySinProvided()
    throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().getPersonSINIdentification().setSin("");
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfDeath(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Missing or invalid format for SIN", exception.getMessage());

  }

  @Test
  public void testProcessCRAGISAssessment_blankSinProvided()
    throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().getPersonSINIdentification().setSin("  ");
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfDeath(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Missing or invalid format for SIN", exception.getMessage());

  }

  @Test
  public void testProcessCRAGISAssessment_noPersonNameListProvided()
    throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setPersonNameList(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfDeath(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Missing or invalid Person Surname", exception.getMessage());

  }

  @Test
  public void testProcessCRAGISAssessment_emptyPersonNameListProvided()
    throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setPersonNameList(new ArrayList<>());
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfDeath(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Missing or invalid Person Surname", exception.getMessage());

  }

  @Test
  public void testProcessCRAGISAssessment_emptyPersonNameProvided()
    throws Exception {

    final List<BDMPersonName> personNameList = new ArrayList<>();
    personNameList.add(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setPersonNameList(personNameList);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfDeath(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Missing or invalid Person Surname", exception.getMessage());

  }

  @Test
  public void testProcessCRAGISAssessment_emptyPersonSurNameProvided()
    throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().getPersonNameList().get(0).setPersonSurname("");
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfDeath(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Missing or invalid Person Surname", exception.getMessage());

  }

  @Test
  public void testProcessCRAGISAssessment_blankPersonSurNameProvided()
    throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().getPersonNameList().get(0).setPersonSurname("  ");
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfDeath(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Missing or invalid Person Surname", exception.getMessage());

  }

  @Test
  public void testProcessCRAGISAssessment_noPersonSurNameProvided()
    throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().getPersonNameList().get(0).setPersonSurname(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfDeath(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Missing or invalid Person Surname", exception.getMessage());

  }

  @Test
  public void testProcessCRAGISAssessment_emptyPersonNamelistProvided()
    throws Exception {

    final List<BDMPersonName> emptyPersonNameList = Collections.emptyList();
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setPersonNameList(emptyPersonNameList);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfDeath(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Missing or invalid Person Surname", exception.getMessage());

  }

  @Test
  public void testProcessCRAGISAssessment_emptyMaritalStatusProvided()
    throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().getMaritalStatus().getStatusCode().setReferenceDataID("");
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfDeath(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);

    final String personFirstName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonGivenName()[0];
    final String personSurName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonSurname();
    final String personBirthName = personFirstName + " " + personSurName;
    final String dateOfBirth = testGISCRAAssesssmentRequestData.getData()
      .getIndividualIncome().getClient().getDateOfBirth().getDate();
    final String isoDOB = BDMDateUtil.dateFormatter(dateOfBirth);
    final String sin =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonSINIdentification().getSin();

    final UniqueID uniqueID = UniqueIDFactory.newInstance();

    final PersonDtls personDtls = new PersonDtls();
    personDtls.concernRoleID = uniqueID.getNextID();
    personDtls.personBirthName = personBirthName;
    personDtls.dateOfBirth = Date.getDate(isoDOB);
    personDtls.maritalStatusCode = BDMMARITALSTATUS.SINGLE;
    createPersonRecordWithSin(personDtls, sin, DUPLICATESTATUS.UNMARKED);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Missing or invalid Marital Status", exception.getMessage());

  }

  @Test
  public void testProcessCRAGISAssessment_blankMaritalStatusProvided()
    throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().getMaritalStatus().getStatusCode()
      .setReferenceDataID("  ");
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfDeath(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfDeath(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);

    final String personFirstName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonGivenName()[0];
    final String personSurName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonSurname();
    final String personBirthName = personFirstName + " " + personSurName;
    final String dateOfBirth = testGISCRAAssesssmentRequestData.getData()
      .getIndividualIncome().getClient().getDateOfBirth().getDate();
    final String isoDOB = BDMDateUtil.dateFormatter(dateOfBirth);
    final String sin =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonSINIdentification().getSin();

    final UniqueID uniqueID = UniqueIDFactory.newInstance();

    final PersonDtls personDtls = new PersonDtls();
    personDtls.concernRoleID = uniqueID.getNextID();
    personDtls.personBirthName = personBirthName;
    personDtls.dateOfBirth = Date.getDate(isoDOB);
    personDtls.maritalStatusCode = BDMMARITALSTATUS.SINGLE;
    createPersonRecordWithSin(personDtls, sin, DUPLICATESTATUS.UNMARKED);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Missing or invalid Marital Status", exception.getMessage());

  }

  @Test
  public void testProcessCRAGISAssessment_noMaritalStatusProvided()
    throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setMaritalStatus(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfDeath(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);

    final String personFirstName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonGivenName()[0];
    final String personSurName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonSurname();
    final String personBirthName = personFirstName + " " + personSurName;
    final String dateOfBirth = testGISCRAAssesssmentRequestData.getData()
      .getIndividualIncome().getClient().getDateOfBirth().getDate();
    final String isoDOB = BDMDateUtil.dateFormatter(dateOfBirth);
    final String sin =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonSINIdentification().getSin();

    final UniqueID uniqueID = UniqueIDFactory.newInstance();

    final PersonDtls personDtls = new PersonDtls();
    personDtls.concernRoleID = uniqueID.getNextID();
    personDtls.personBirthName = personBirthName;
    personDtls.dateOfBirth = Date.getDate(isoDOB);
    personDtls.maritalStatusCode = BDMMARITALSTATUS.SINGLE;
    createPersonRecordWithSin(personDtls, sin, DUPLICATESTATUS.UNMARKED);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Invalid request body", exception.getMessage());
  }

  @Test
  public void testProcessCRAGISAssessment_maritalStatusSameAsInCuram()
    throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfDeath(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().getMaritalStatus().getStatusCode().setReferenceDataID("6");

    final String personFirstName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonGivenName()[0];
    final String personSurName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonSurname();
    final String personBirthName = personFirstName + " " + personSurName;
    final String dateOfBirth = testGISCRAAssesssmentRequestData.getData()
      .getIndividualIncome().getClient().getDateOfBirth().getDate();
    final String isoDOB = BDMDateUtil.dateFormatter(dateOfBirth);
    final String sin =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonSINIdentification().getSin();

    final UniqueID uniqueID = UniqueIDFactory.newInstance();

    final PersonDtls personDtls = new PersonDtls();
    personDtls.concernRoleID = uniqueID.getNextID();
    personDtls.personBirthName = personBirthName;
    personDtls.dateOfBirth = Date.getDate(isoDOB);
    personDtls.maritalStatusCode = BDMMARITALSTATUS.SINGLE;
    createPersonRecordWithSin(personDtls, sin, DUPLICATESTATUS.UNMARKED);

    final BDMOASPersonSummaryDetails expected = searchPersonBySin(
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonSINIdentification().getSin()).dtls.get(0);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    testSubject.processCRAGISAssessment(testRequest);

    final BDMOASPersonSummaryDetails actual = searchPersonBySin(
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonSINIdentification().getSin()).dtls.get(0);

    assertEquals(expected.maritalStatus, actual.maritalStatus);
  }

  @Test
  public void testProcessCRAGISAssessment_unknownMaritalStatus()
    throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfDeath(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().getMaritalStatus().getStatusCode().setReferenceDataID("0");

    final String personFirstName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonGivenName()[0];
    final String personSurName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonSurname();
    final String personBirthName = personFirstName + " " + personSurName;
    final String dateOfBirth = testGISCRAAssesssmentRequestData.getData()
      .getIndividualIncome().getClient().getDateOfBirth().getDate();
    final String isoDOB = BDMDateUtil.dateFormatter(dateOfBirth);
    final String sin =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonSINIdentification().getSin();

    final UniqueID uniqueID = UniqueIDFactory.newInstance();

    final PersonDtls personDtls = new PersonDtls();
    personDtls.concernRoleID = uniqueID.getNextID();
    personDtls.personBirthName = personBirthName;
    personDtls.dateOfBirth = Date.getDate(isoDOB);
    personDtls.maritalStatusCode = BDMMARITALSTATUS.MARRIED;
    createPersonRecordWithSin(personDtls, sin, DUPLICATESTATUS.UNMARKED);

    final BDMOASPersonSummaryDetails expected = searchPersonBySin(
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonSINIdentification().getSin()).dtls.get(0);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    testSubject.processCRAGISAssessment(testRequest);

    final BDMOASPersonSummaryDetails actual = searchPersonBySin(
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonSINIdentification().getSin()).dtls.get(0);

    assertEquals(expected.maritalStatus, actual.maritalStatus);
  }

  @Test
  public void testProcessCRAGISAssessment_invalidMaritalStatus()
    throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfDeath(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().getMaritalStatus().getStatusCode().setReferenceDataID("*");

    final String personFirstName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonGivenName()[0];
    final String personSurName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonSurname();
    final String personBirthName = personFirstName + " " + personSurName;
    final String dateOfBirth = testGISCRAAssesssmentRequestData.getData()
      .getIndividualIncome().getClient().getDateOfBirth().getDate();
    final String isoDOB = BDMDateUtil.dateFormatter(dateOfBirth);
    final String sin =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonSINIdentification().getSin();

    final UniqueID uniqueID = UniqueIDFactory.newInstance();

    final PersonDtls personDtls = new PersonDtls();
    personDtls.concernRoleID = uniqueID.getNextID();
    personDtls.personBirthName = personBirthName;
    personDtls.dateOfBirth = Date.getDate(isoDOB);
    personDtls.maritalStatusCode = BDMMARITALSTATUS.MARRIED;
    createPersonRecordWithSin(personDtls, sin, DUPLICATESTATUS.UNMARKED);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Missing or invalid Marital Status", exception.getMessage());

  }

  @Test
  public void testProcessCRAGISAssessment_blankMaritalStatus()
    throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfDeath(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().getMaritalStatus().getStatusCode().setReferenceDataID(" ");

    final String personFirstName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonGivenName()[0];
    final String personSurName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonSurname();
    final String personBirthName = personFirstName + " " + personSurName;
    final String dateOfBirth = testGISCRAAssesssmentRequestData.getData()
      .getIndividualIncome().getClient().getDateOfBirth().getDate();
    final String isoDOB = BDMDateUtil.dateFormatter(dateOfBirth);
    final String sin =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonSINIdentification().getSin();

    final UniqueID uniqueID = UniqueIDFactory.newInstance();

    final PersonDtls personDtls = new PersonDtls();
    personDtls.concernRoleID = uniqueID.getNextID();
    personDtls.personBirthName = personBirthName;
    personDtls.dateOfBirth = Date.getDate(isoDOB);
    personDtls.maritalStatusCode = BDMMARITALSTATUS.MARRIED;
    createPersonRecordWithSin(personDtls, sin, DUPLICATESTATUS.UNMARKED);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Missing or invalid Marital Status", exception.getMessage());

  }

  @Test
  public void testProcessCRAGISAssessment_bankruptcyCodeProvided()
    throws Exception {

    final String personFirstName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonGivenName()[0];
    final String personSurName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonSurname();
    final String personBirthName = personFirstName + " " + personSurName;
    final String dateOfBirth = testGISCRAAssesssmentRequestData.getData()
      .getIndividualIncome().getClient().getDateOfBirth().getDate();
    final String isoDOB = BDMDateUtil.dateFormatter(dateOfBirth);
    final String sin =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonSINIdentification().getSin();

    final UniqueID uniqueID = UniqueIDFactory.newInstance();

    final PersonDtls personDtls = new PersonDtls();
    personDtls.concernRoleID = uniqueID.getNextID();
    personDtls.personBirthName = personBirthName;
    personDtls.dateOfBirth = Date.getDate(isoDOB);
    personDtls.maritalStatusCode = BDMMARITALSTATUS.MARRIED;
    createPersonRecordWithSin(personDtls, sin, DUPLICATESTATUS.UNMARKED);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    testSubject.processCRAGISAssessment(testRequest);

    // TODO: check that a task has been created

    // final AppException exception = assertThrows(AppException.class,
    // () -> testSubject.processCRAGISAssessment(testRequest));
    // assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
    // exception.getCatEntry());
    // assertEquals("Client CRA Account Indicates Pre/post-bankruptcy",
    // exception.getMessage());

  }

  @Test
  public void testProcessCRAGISAssessment_noBankruptcyCodeProvided()
    throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);

    final String personFirstName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonGivenName()[0];
    final String personSurName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonSurname();
    final String personBirthName = personFirstName + " " + personSurName;
    final String dateOfBirth = testGISCRAAssesssmentRequestData.getData()
      .getIndividualIncome().getClient().getDateOfBirth().getDate();
    final String isoDOB = BDMDateUtil.dateFormatter(dateOfBirth);
    final String sin =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonSINIdentification().getSin();

    final UniqueID uniqueID = UniqueIDFactory.newInstance();

    final PersonDtls personDtls = new PersonDtls();
    personDtls.concernRoleID = uniqueID.getNextID();
    personDtls.personBirthName = personBirthName;
    personDtls.dateOfBirth = Date.getDate(isoDOB);
    personDtls.maritalStatusCode = BDMMARITALSTATUS.MARRIED;
    createPersonRecordWithSin(personDtls, sin, DUPLICATESTATUS.UNMARKED);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    testSubject.processCRAGISAssessment(testRequest);

    // TODO: check that no task has been created

    // final AppException exception = assertThrows(AppException.class,
    // () -> testSubject.processCRAGISAssessment(testRequest));
    // assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
    // exception.getCatEntry());
    // assertEquals("Client CRA Account Indicates Pre/post-bankruptcy",
    // exception.getMessage());

  }

  @Test
  public void testProcessCRAGISAssessment_blankBankruptcyCodeProvided()
    throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getIncomeBankruptcyCode().setReferenceDataID("  ");

    final String personFirstName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonGivenName()[0];
    final String personSurName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonSurname();
    final String personBirthName = personFirstName + " " + personSurName;
    final String dateOfBirth = testGISCRAAssesssmentRequestData.getData()
      .getIndividualIncome().getClient().getDateOfBirth().getDate();
    final String isoDOB = BDMDateUtil.dateFormatter(dateOfBirth);
    final String sin =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonSINIdentification().getSin();

    final UniqueID uniqueID = UniqueIDFactory.newInstance();

    final PersonDtls personDtls = new PersonDtls();
    personDtls.concernRoleID = uniqueID.getNextID();
    personDtls.personBirthName = personBirthName;
    personDtls.dateOfBirth = Date.getDate(isoDOB);
    personDtls.maritalStatusCode = BDMMARITALSTATUS.MARRIED;
    createPersonRecordWithSin(personDtls, sin, DUPLICATESTATUS.UNMARKED);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    testSubject.processCRAGISAssessment(testRequest);

    // TODO: check if a task has been created

    // final AppException exception = assertThrows(AppException.class,
    // () -> testSubject.processCRAGISAssessment(testRequest));
    // assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
    // exception.getCatEntry());
    // assertEquals("Client CRA Account Indicates Pre/post-bankruptcy",
    // exception.getMessage());

  }

  @Test
  public void testProcessCRAGISAssessment_emptyBankruptcyCodeProvided()
    throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getIncomeBankruptcyCode().setReferenceDataName("");

    final String personFirstName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonGivenName()[0];
    final String personSurName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonSurname();
    final String personBirthName = personFirstName + " " + personSurName;
    final String dateOfBirth = testGISCRAAssesssmentRequestData.getData()
      .getIndividualIncome().getClient().getDateOfBirth().getDate();
    final String isoDOB = BDMDateUtil.dateFormatter(dateOfBirth);
    final String sin =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonSINIdentification().getSin();

    final UniqueID uniqueID = UniqueIDFactory.newInstance();

    final PersonDtls personDtls = new PersonDtls();
    personDtls.concernRoleID = uniqueID.getNextID();
    personDtls.personBirthName = personBirthName;
    personDtls.dateOfBirth = Date.getDate(isoDOB);
    personDtls.maritalStatusCode = BDMMARITALSTATUS.MARRIED;
    createPersonRecordWithSin(personDtls, sin, DUPLICATESTATUS.UNMARKED);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);

    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Invalid request body", exception.getMessage());

  }

  @Test
  public void testProcessCRAGISAssessment_emptyCRAAccount() throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().getPersonStatus().getStatusCode().setReferenceDataName("");
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfDeath(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Missing or invalid CRA Account Status",
      exception.getMessage());
  }

  @Test
  public void testProcessCRAGISAssessment_blankCRAAccount() throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().getPersonStatus().getStatusCode()
      .setReferenceDataName("  ");
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfDeath(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Missing or invalid CRA Account Status",
      exception.getMessage());
  }

  @Test
  public void testProcessCRAGISAssessment_inactiveCRAAccount()
    throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().getPersonStatus().getStatusCode()
      .setReferenceDataName("Inactive");
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfDeath(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);

    final String personFirstName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonGivenName()[0];
    final String personSurName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonSurname();
    final String personBirthName = personFirstName + " " + personSurName;
    final String dateOfBirth = testGISCRAAssesssmentRequestData.getData()
      .getIndividualIncome().getClient().getDateOfBirth().getDate();
    final String isoDOB = BDMDateUtil.dateFormatter(dateOfBirth);
    final String sin =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonSINIdentification().getSin();

    final UniqueID uniqueID = UniqueIDFactory.newInstance();

    final PersonDtls originalPersonDtls = new PersonDtls();
    originalPersonDtls.concernRoleID = uniqueID.getNextID();
    originalPersonDtls.personBirthName = personBirthName;
    originalPersonDtls.dateOfBirth = Date.getDate(isoDOB);
    originalPersonDtls.maritalStatusCode = BDMMARITALSTATUS.MARRIED;
    createPersonRecordWithSin(originalPersonDtls, sin,
      DUPLICATESTATUS.UNMARKED);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    testSubject.processCRAGISAssessment(testRequest);

    // TODO: check if a task has been created

    // final AppException exception = assertThrows(AppException.class,
    // () -> testSubject.processCRAGISAssessment(testRequest));
    // assertEquals(BDMBPOCCT.ERR_HTTP_404_RESOURCE_NOT_FOUND,
    // exception.getCatEntry());
    // assertEquals(
    // "The requested data could not be found. Specify a valid SIN, DOB,
    // Surname.",
    // exception.getMessage());
  }

  @Test
  public void testProcessCRAGISAssessment_activeCRAAccount()
    throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().getPersonStatus().getStatusCode()
      .setReferenceDataName("Active");
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfDeath(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);

    final String personFirstName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonGivenName()[0];
    final String personSurName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonSurname();
    final String personBirthName = personFirstName + " " + personSurName;
    final String dateOfBirth = testGISCRAAssesssmentRequestData.getData()
      .getIndividualIncome().getClient().getDateOfBirth().getDate();
    final String isoDOB = BDMDateUtil.dateFormatter(dateOfBirth);
    final String sin =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonSINIdentification().getSin();

    final UniqueID uniqueID = UniqueIDFactory.newInstance();

    final PersonDtls originalPersonDtls = new PersonDtls();
    originalPersonDtls.concernRoleID = uniqueID.getNextID();
    originalPersonDtls.personBirthName = personBirthName;
    originalPersonDtls.dateOfBirth = Date.getDate(isoDOB);
    originalPersonDtls.maritalStatusCode = BDMMARITALSTATUS.MARRIED;
    createPersonRecordWithSin(originalPersonDtls, sin,
      DUPLICATESTATUS.UNMARKED);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    testSubject.processCRAGISAssessment(testRequest);

    // TODO: check that a task has not been created

    // final AppException exception = assertThrows(AppException.class,
    // () -> testSubject.processCRAGISAssessment(testRequest));
    // assertEquals(BDMBPOCCT.ERR_HTTP_404_RESOURCE_NOT_FOUND,
    // exception.getCatEntry());
    // assertEquals(
    // "The requested data could not be found. Specify a valid SIN, DOB,
    // Surname.",
    // exception.getMessage());
  }

  @Test
  public void testProcessCRAGISAssessment_duplicateRecordFoundButNotMarked()
    throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfDeath(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().getMaritalStatus().getStatusCode().setReferenceDataID("1");

    final String personFirstName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonGivenName()[0];
    final String personSurName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonSurname();
    final String personBirthName = personFirstName + " " + personSurName;
    final String dateOfBirth = testGISCRAAssesssmentRequestData.getData()
      .getIndividualIncome().getClient().getDateOfBirth().getDate();
    final String isoDOB = BDMDateUtil.dateFormatter(dateOfBirth);
    final String sin =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonSINIdentification().getSin();

    final UniqueID uniqueID = UniqueIDFactory.newInstance();

    final PersonDtls originalPersonDtls = new PersonDtls();
    originalPersonDtls.concernRoleID = uniqueID.getNextID();
    originalPersonDtls.personBirthName = personBirthName;
    originalPersonDtls.dateOfBirth = Date.getDate(isoDOB);
    createPersonRecordWithSin(originalPersonDtls, sin,
      DUPLICATESTATUS.UNMARKED);

    final PersonDtls duplicatePersonDtls = new PersonDtls();
    duplicatePersonDtls.concernRoleID = uniqueID.getNextID();
    duplicatePersonDtls.personBirthName = personBirthName;
    duplicatePersonDtls.dateOfBirth = Date.getDate(isoDOB);
    createPersonRecordWithSin(duplicatePersonDtls, sin,
      DUPLICATESTATUS.UNMARKED);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals("Duplicate records found. Specify a valid SIN, DOB, Surname",
      exception.getMessage());

  }

  // @Test
  // public void
  // testProcessCRAGISAssessment_duplicateRecordFoundButMarkedOriginalFirst()
  // throws Exception {
  //
  // testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
  // .getClient().setDateOfDeath(null);
  // testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
  // .setIncomeBankruptcyCode(null);
  // testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
  // .getClient().getMaritalStatus().getStatusCode()
  // .setReferenceDataID(BDMOASConstants.BDM_OAS_CRA_MARITAL_STATUS_SINGLE);
  //
  // final String personFirstName =
  // testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
  // .getClient().getPersonNameList().get(0).getPersonGivenName()[0];
  // final String personSurName =
  // testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
  // .getClient().getPersonNameList().get(0).getPersonSurname();
  // final String personBirthName = personFirstName + " " + personSurName;
  // final String dateOfBirth = testGISCRAAssesssmentRequestData.getData()
  // .getIndividualIncome().getClient().getDateOfBirth().getDate();
  // final String isoDOB = BDMDateUtil.dateFormatter(dateOfBirth);
  // final String sin =
  // testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
  // .getClient().getPersonSINIdentification().getSin();
  //
  // final UniqueID uniqueID = UniqueIDFactory.newInstance();
  //
  // final PersonDtls originalPersonDtls = new PersonDtls();
  // originalPersonDtls.concernRoleID = uniqueID.getNextID();
  // originalPersonDtls.personBirthName = personBirthName;
  // originalPersonDtls.dateOfBirth = Date.getDate(isoDOB);
  // originalPersonDtls.maritalStatusCode = BDMMARITALSTATUS.MARRIED;
  // createPersonRecordWithSin(originalPersonDtls, sin,
  // DUPLICATESTATUS.UNMARKED);
  //
  // final PersonDtls duplicatePersonDtls = new PersonDtls();
  // duplicatePersonDtls.concernRoleID = uniqueID.getNextID();
  // duplicatePersonDtls.personBirthName = personBirthName;
  // duplicatePersonDtls.dateOfBirth = Date.getDate(isoDOB);
  // duplicatePersonDtls.maritalStatusCode = BDMMARITALSTATUS.MARRIED;
  // createPersonRecordWithSin(duplicatePersonDtls, sin,
  // DUPLICATESTATUS.UNMARKED);
  //
  // final ConcernRoleKey key = new ConcernRoleKey();
  // key.concernRoleID = originalPersonDtls.concernRoleID;
  //
  // final MarkDuplicateCreateDetails markDuplicateDetails =
  // new MarkDuplicateCreateDetails();
  // markDuplicateDetails.markDuplicateCreateDetails.originalConcernRoleID =
  // originalPersonDtls.concernRoleID;
  // markDuplicateDetails.markDuplicateCreateDetails.duplicateConcernRoleID =
  // duplicatePersonDtls.concernRoleID;
  // markDuplicateDetails.markDuplicateCreateDetails.duplicateReason =
  // DUPLICATEREASON.PERSONMATCH;
  //
  // final MarkDuplicateDetails mdd = markDuplicate(markDuplicateDetails);
  //
  // testRequest.niemPayload =
  // BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
  // testSubject.processCRAGISAssessment(testRequest);
  //
  // final PersonKey personKey = new PersonKey();
  // final Person personObj = PersonFactory.newInstance();
  //
  // personKey.concernRoleID = originalPersonDtls.concernRoleID;
  // final PersonDtls actualOriginalPersonDtls = personObj.read(personKey);
  // assertEquals(BDMMARITALSTATUS.SINGLE,
  // actualOriginalPersonDtls.maritalStatusCode);
  //
  // personKey.concernRoleID = duplicatePersonDtls.concernRoleID;
  // final PersonDtls actualDuplicatePersonDtls = personObj.read(personKey);
  // assertEquals(BDMMARITALSTATUS.MARRIED,
  // actualDuplicatePersonDtls.maritalStatusCode);
  //
  // }

  // @Test
  // public void
  // testProcessCRAGISAssessment_duplicateRecordFoundButMarkedDuplicateFirst()
  // throws Exception {
  //
  // testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
  // .getClient().setDateOfDeath(null);
  // testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
  // .setIncomeBankruptcyCode(null);
  // testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
  // .getClient().getMaritalStatus().getStatusCode()
  // .setReferenceDataID(BDMOASConstants.BDM_OAS_CRA_MARITAL_STATUS_SINGLE);
  //
  // final String personFirstName =
  // testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
  // .getClient().getPersonNameList().get(0).getPersonGivenName()[0];
  // final String personSurName =
  // testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
  // .getClient().getPersonNameList().get(0).getPersonSurname();
  // final String personBirthName = personFirstName + " " + personSurName;
  // final String dateOfBirth = testGISCRAAssesssmentRequestData.getData()
  // .getIndividualIncome().getClient().getDateOfBirth().getDate();
  // final String isoDOB = BDMDateUtil.dateFormatter(dateOfBirth);
  // final String sin =
  // testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
  // .getClient().getPersonSINIdentification().getSin();
  //
  // final UniqueID uniqueID = UniqueIDFactory.newInstance();
  //
  // final PersonDtls duplicatePersonDtls = new PersonDtls();
  // duplicatePersonDtls.concernRoleID = uniqueID.getNextID();
  // duplicatePersonDtls.personBirthName = personBirthName;
  // duplicatePersonDtls.dateOfBirth = Date.getDate(isoDOB);
  // duplicatePersonDtls.maritalStatusCode = BDMMARITALSTATUS.MARRIED;
  // createPersonRecordWithSin(duplicatePersonDtls, sin,
  // DUPLICATESTATUS.UNMARKED);
  //
  // final PersonDtls originalPersonDtls = new PersonDtls();
  // originalPersonDtls.concernRoleID = uniqueID.getNextID();
  // originalPersonDtls.personBirthName = personBirthName;
  // originalPersonDtls.dateOfBirth = Date.getDate(isoDOB);
  // originalPersonDtls.maritalStatusCode = BDMMARITALSTATUS.MARRIED;
  // createPersonRecordWithSin(originalPersonDtls, sin,
  // DUPLICATESTATUS.UNMARKED);
  //
  // final ConcernRoleKey key = new ConcernRoleKey();
  // key.concernRoleID = originalPersonDtls.concernRoleID;
  //
  // final MarkDuplicateCreateDetails markDuplicateDetails =
  // new MarkDuplicateCreateDetails();
  // markDuplicateDetails.markDuplicateCreateDetails.originalConcernRoleID =
  // originalPersonDtls.concernRoleID;
  // markDuplicateDetails.markDuplicateCreateDetails.duplicateConcernRoleID =
  // duplicatePersonDtls.concernRoleID;
  // markDuplicateDetails.markDuplicateCreateDetails.duplicateReason =
  // DUPLICATEREASON.PERSONMATCH;
  //
  // final MarkDuplicateDetails mdd = markDuplicate(markDuplicateDetails);
  //
  // testRequest.niemPayload =
  // BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
  // testSubject.processCRAGISAssessment(testRequest);
  //
  // final PersonKey personKey = new PersonKey();
  // final Person personObj = PersonFactory.newInstance();
  //
  // personKey.concernRoleID = originalPersonDtls.concernRoleID;
  // final PersonDtls actualOriginalPersonDtls = personObj.read(personKey);
  // assertEquals(BDMMARITALSTATUS.SINGLE,
  // actualOriginalPersonDtls.maritalStatusCode);
  //
  // personKey.concernRoleID = duplicatePersonDtls.concernRoleID;
  // final PersonDtls actualDuplicatePersonDtls = personObj.read(personKey);
  // assertEquals(BDMMARITALSTATUS.MARRIED,
  // actualDuplicatePersonDtls.maritalStatusCode);
  //
  // }

  @Test
  public void testProcessCRAGISAssessment_noMatchingNameAndDOBRecordFound()
    throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfDeath(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().getMaritalStatus().getStatusCode()
      .setReferenceDataID(BDMOASConstants.BDM_OAS_CRA_MARITAL_STATUS_SINGLE);

    final String personFirstName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonGivenName()[0];
    final String personSurName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonSurname();
    final String personBirthName = personFirstName + " " + personSurName;
    final String dateOfBirth = testGISCRAAssesssmentRequestData.getData()
      .getIndividualIncome().getClient().getDateOfBirth().getDate();
    final String isoDOB = BDMDateUtil.dateFormatter(dateOfBirth);
    final String sin =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonSINIdentification().getSin();

    final UniqueID uniqueID = UniqueIDFactory.newInstance();

    final PersonDtls firstPersonDtls = new PersonDtls();
    firstPersonDtls.concernRoleID = uniqueID.getNextID();
    firstPersonDtls.personBirthName = personBirthName;
    firstPersonDtls.dateOfBirth = Date.getDate("18000101");
    firstPersonDtls.maritalStatusCode = BDMMARITALSTATUS.MARRIED;
    createPersonRecordWithSin(firstPersonDtls, sin, DUPLICATESTATUS.UNMARKED);

    final PersonDtls secondPersonDtls = new PersonDtls();
    secondPersonDtls.concernRoleID = uniqueID.getNextID();
    secondPersonDtls.personBirthName = personBirthName;
    secondPersonDtls.dateOfBirth = Date.getDate("18990101");
    secondPersonDtls.maritalStatusCode = BDMMARITALSTATUS.MARRIED;
    createPersonRecordWithSin(secondPersonDtls, sin,
      DUPLICATESTATUS.UNMARKED);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_HTTP_404_RESOURCE_NOT_FOUND,
      exception.getCatEntry());
    assertEquals(
      "The requested data could not be found. Specify a valid SIN, DOB, Surname.",
      exception.getMessage());

  }

  @Test
  public void testProcessCRAGISAssessment_noMatchingSinRecordFound()
    throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().getPersonSINIdentification().setSin("000000000");
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfDeath(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRAGISAssessment(testRequest));
    assertEquals(BDMBPOCCT.ERR_HTTP_404_RESOURCE_NOT_FOUND,
      exception.getCatEntry());
    assertEquals(
      "The requested data could not be found. Specify a valid SIN, DOB, Surname.",
      exception.getMessage());

  }

  @Test
  public void testProcessCRAGISAssessment_success() throws Exception {

    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().setDateOfDeath(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .setIncomeBankruptcyCode(null);
    testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
      .getClient().getMaritalStatus().getStatusCode()
      .setReferenceDataID(BDMOASConstants.BDM_OAS_CRA_MARITAL_STATUS_SINGLE);

    final String personFirstName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonGivenName()[0];
    final String personSurName =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonNameList().get(0).getPersonSurname();
    final String personBirthName = personFirstName + " " + personSurName;
    final String dateOfBirth = testGISCRAAssesssmentRequestData.getData()
      .getIndividualIncome().getClient().getDateOfBirth().getDate();
    final String isoDOB = BDMDateUtil.dateFormatter(dateOfBirth);
    final String sin =
      testGISCRAAssesssmentRequestData.getData().getIndividualIncome()
        .getClient().getPersonSINIdentification().getSin();

    final UniqueID uniqueID = UniqueIDFactory.newInstance();

    final PersonDtls expectedPersonDtls = new PersonDtls();
    expectedPersonDtls.concernRoleID = uniqueID.getNextID();
    expectedPersonDtls.personBirthName = personBirthName;
    expectedPersonDtls.dateOfBirth = Date.getDate(isoDOB);
    expectedPersonDtls.maritalStatusCode = BDMMARITALSTATUS.MARRIED;
    createPersonRecordWithSin(expectedPersonDtls, sin,
      DUPLICATESTATUS.UNMARKED);

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(testGISCRAAssesssmentRequestData);
    testSubject.processCRAGISAssessment(testRequest);

    final PersonKey personKey = new PersonKey();
    final Person personObj = PersonFactory.newInstance();
    personKey.concernRoleID = expectedPersonDtls.concernRoleID;
    final PersonDtls actualPersonDtls = personObj.read(personKey);

    assertEquals(expectedPersonDtls.maritalStatusCode,
      actualPersonDtls.maritalStatusCode);

  }

  /**
   * Searches a person by SIN
   *
   * @param String sin
   * @return
   * @throws AppException
   * @throws InformationalException
   */

  private BDMOASPersonSummaryDetailsList searchPersonBySin(final String sin)
    throws AppException, InformationalException {

    final BDMOASPerson personObj = BDMOASPersonFactory.newInstance();
    final BDMPersonSINKey sinKey = new BDMPersonSINKey();
    sinKey.sin = sin;
    return personObj.searchPersonBySIN(sinKey);

  }

  /**
   * Creates a concern role with bare bones details
   *
   * @param PersonDtls personDtls
   * @param String sin
   * @return
   * @throws AppException
   * @throws InformationalException
   */

  public void createPersonRecordWithSin(final PersonDtls personDtls,
    final String sin, final String duplicateStatus)
    throws AppException, InformationalException {

    final ConcernDtls concernDtls = new ConcernDtls();
    concernDtls.concernID = personDtls.concernRoleID;
    concernDtls.creationDate = Date.getCurrentDate();
    concernDtls.name = "CRA";
    ConcernFactory.newInstance().insert(concernDtls);

    final AddressDtls addressDtls = new AddressDtls();
    addressDtls.addressID = personDtls.concernRoleID;
    addressDtls.addressData =
      "1\n" + "100\n" + "BDMINTL\n" + "CA\n" + "1\n" + "1\n" + "COUNTRY=CA\n"
        + "POSTCODE=T3T 3T3\n" + "ZIP=\n" + "BDMZIPX=\n" + "APT=\n"
        + "POBOXNO=\n" + "ADD1=123\n" + "ADD2=Street\n" + "CITY=Edmonton\n"
        + "PROV=AB\n" + "STATEPROV=\n" + "BDMSTPROVX=\n" + "BDMUNPARSE=\n";
    addressDtls.addressLayoutType = ADDRESSLAYOUTTYPE.BDMINTL;
    addressDtls.countryCode = COUNTRY.CA;
    AddressFactory.newInstance().insert(addressDtls);

    final ConcernRoleDtls crDtls = new ConcernRoleDtls();
    crDtls.concernID = concernDtls.concernID;
    crDtls.concernRoleID = personDtls.concernRoleID;
    crDtls.concernRoleName = personDtls.personBirthName;
    crDtls.primaryAddressID = addressDtls.addressID;
    crDtls.concernRoleType = CONCERNROLETYPE.PERSON;
    crDtls.creationDate = Date.getCurrentDate();
    crDtls.registrationDate = Date.getCurrentDate();
    crDtls.startDate = Date.getCurrentDate();
    crDtls.sensitivity = "1";
    crDtls.regUserName = TransactionInfo.getProgramUser();

    ConcernRoleFactory.newInstance().insert(crDtls);

    final AlternateNameDtls alternateNameDtls = new AlternateNameDtls();
    alternateNameDtls.alternateNameID = personDtls.concernRoleID;
    alternateNameDtls.concernRoleID = personDtls.concernRoleID;
    alternateNameDtls.firstForename = personDtls.personBirthName;
    alternateNameDtls.surname = personDtls.personBirthName;
    alternateNameDtls.nameType = "AT1";
    alternateNameDtls.nameStatus = "RST1";
    AlternateNameFactory.newInstance().insert(alternateNameDtls);

    personDtls.primaryAlternateNameID = alternateNameDtls.alternateNameID;

    final ConcernRoleAlternateIDDtls sinDtls =
      new ConcernRoleAlternateIDDtls();
    sinDtls.concernRoleAlternateID = personDtls.concernRoleID;
    sinDtls.concernRoleID = crDtls.concernRoleID;
    sinDtls.alternateID = sin;
    sinDtls.typeCode = "BDMCA80002";
    sinDtls.statusCode = "RST1";
    ConcernRoleAlternateIDFactory.newInstance().insert(sinDtls);

    PersonFactory.newInstance().insert(personDtls);

  }

  /**
   * @param details
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public MarkDuplicateDetails
    markDuplicate(final MarkDuplicateCreateDetails details)
      throws AppException, InformationalException {

    final ClientMerge clientMerge = ClientMergeFactory.newInstance();

    final MarkDuplicateDetails markDuplicateDetails =
      clientMerge.markDuplicate(details);

    return markDuplicateDetails;
  }

  @Override
  public boolean shouldCommit() {

    return false;
  }

}
