package curam.ca.gc.bdmoas.test.rest.bdmoascraapi.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSINKey;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.impl.BDMInterfaceConstants;
import curam.ca.gc.bdm.message.BDMBPOCCT;
import curam.ca.gc.bdm.util.impl.BDMDateUtil;
import curam.ca.gc.bdm.util.rest.impl.BDMRestUtil;
import curam.ca.gc.bdmoas.entity.fact.BDMOASPersonFactory;
import curam.ca.gc.bdmoas.entity.intf.BDMOASPerson;
import curam.ca.gc.bdmoas.entity.struct.BDMOASPersonSummaryDetails;
import curam.ca.gc.bdmoas.entity.struct.BDMOASPersonSummaryDetailsList;
import curam.ca.gc.bdmoas.rest.bdmoascraapi.fact.BDMOASCRAAPIFactory;
import curam.ca.gc.bdmoas.rest.bdmoascraapi.impl.BDMOASCRAHelper;
import curam.ca.gc.bdmoas.rest.bdmoascraapi.struct.BDMOASCRARestRequest;
import curam.ca.gc.bdmoas.sl.interfaces.cra.impl.pojos.BDMOASDeathMatchInbound;
import curam.citizenaccount.facade.struct.ConcernRoleKey;
import curam.codetable.ADDRESSLAYOUTTYPE;
import curam.codetable.CONCERNROLETYPE;
import curam.codetable.COUNTRY;
import curam.codetable.DUPLICATEREASON;
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
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * @author Nadira.Banu
 *
 */

public class BDMOASCRADeathMatchTest extends CuramServerTestJUnit4 {

  private static curam.ca.gc.bdmoas.rest.bdmoascraapi.intf.BDMOASCRAAPI testSubject;

  private static BDMOASCRARestRequest testRequest;

  private BDMOASDeathMatchInbound testRequestData;

  public String craBirthDate, craSurName, sin, isoCRABirthDate, craDeathDate,
    isoCRADeathDate;

  @Inject
  BDMOASCRAHelper craHelper;

  private final String requestSampleData =
    "{\"id\":\"UniqueEventID\",\"specversion\":\"1.0\",\"type\":\"esdc.curam.client.deathmatch\","
      + "\"source\":\"file://CRADeathMatch20221105.csv\","
      + "\"data\":{\"Client\":{\"ClientIdentification\":[{\"IdentificationID\":\"177755477\",\"IdentificationCategoryText\":\"SAccountNumber\"}],"
      + "\"PersonBirthDate\":{\"date\":\"1949-12-12\"},"
      + "\"PersonDeathDate\":{\"date\":\"2022-12-12\"},\"PersonSINIdentification\":{\"IdentificationID\":\"177755477\"},"
      + "\"PersonName\":[{\"PersonGivenName\":[\"Nandita\"],\"PersonSurName\":\"Pat--él\","
      + "\"PersonNameInitialsText\":\"RB\"},{\"PersonGivenName\":[\"NadiraBéatrice\"],"
      + "\"PersonSurName\":\"StephanieMael\",\"PersonNameInitialsText\":\"NB\"}]}}}";

  private final String requestInvalidSampleData =
    "{\"id\":\"UniqueEventID\",\"specversion\":\"1.0\",\"type\":\"esdc.curam.client.deathmatch\","
      + "\"source\":\"file://CRADeathMatch20221105.csv\","
      + "\"data\":{\"Client\":{\"ClientIdentification\":[{\"IdentificationID\":\"177755477\",\"IdentificationCategoryText\":\"SAccountNumber\"}],"
      + "\"PersonBirthDate\":{\"date\":\"1949-12-12\"},"
      + "\"PersonDeathDate\":{\"date\":\"2022-12-12\"},\"PersonSINIdentification\":{\"IdentificationID\":\"177755477\"},"
      + "\"PersonName\":[{\"PersonGivenName\":[\"Nandita\"],\"PersonSurName\":\"Pat--él\","
      + "\"PersonNameInitialsText\":\"RB\"},{\"PersonGivenName\":[\"NadiraBéatrice\"],"
      + "\"PersonSurName\":\"StephanieMael\",\"PersonNameInitialsText\":\"NB\"}]}}}}}}";

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
    testRequestData = BDMRestUtil.convertFromJSON(requestSampleData,
      BDMOASDeathMatchInbound.class);

    craBirthDate =
      testRequestData.getData().getClientData().getDateOfBirth().getDate();
    craDeathDate =
      testRequestData.getData().getClientData().getDateOfDeath().getDate();

    craSurName = testRequestData.getData().getClientData().getPersonNameList()
      .get(0).getPersonSurname();
    sin = testRequestData.getData().getClientData()
      .getPersonSINIdentification().getSin();

    isoCRABirthDate = BDMDateUtil.dateFormatter(craBirthDate);
    isoCRADeathDate = BDMDateUtil.dateFormatter(craDeathDate);

  }

  /**
   * Tests ProcessCRADeathmatch() with an invalid NiemPayload causing the test
   * to
   * fail
   *
   * @throws Exception
   */
  @Test
  public void testProcessCRADeathMatch_invalidNeimPayload() throws Exception {

    testRequest.niemPayload =
      BDMRestUtil.convertToJSON(requestInvalidSampleData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRADeathMatch(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals(BDMInterfaceConstants.BDM_INVALID_REQUEST,
      exception.getMessage());

  }

  /**
   * Tests ProcessCRADeathmatch() with an empty NiemPayload causing the test to
   * fail
   *
   * @throws Exception
   */
  @Test
  public void testProcessCRADeathMatch_emptyNeimPayload() throws Exception {

    testRequest.niemPayload = "";
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRADeathMatch(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals(BDMInterfaceConstants.BDM_INVALID_REQUEST,
      exception.getMessage());

  }

  /**
   * Tests ProcessCRADeathmatch() with a null NiemPayload causing the test to
   * fail
   *
   * @throws Exception
   */
  @Test
  public void testProcessCRADeathMatch_nullNeimPayload() throws Exception {

    testRequest.niemPayload = null;
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRADeathMatch(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals(BDMInterfaceConstants.BDM_INVALID_REQUEST,
      exception.getMessage());

  }

  /**
   * Tests ProcessCRADeathmatch() with an invalid DOD format causing the test to
   * fail
   *
   * @throws Exception
   */
  @Test
  public void testProcessCRADeathMatch_invalidDODFormat() throws Exception {

    testRequestData.getData().getClientData().getDateOfDeath()
      .setDate("01-01-1990");

    testRequest.niemPayload = BDMRestUtil.convertToJSON(testRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRADeathMatch(testRequest));

    final AppException appException =
      new AppException(BDMBPOCCT.ERR_HTTP_404_RESOURCE_NOT_FOUND);
    appException.arg(BDMConstants.kDOD);

    assertEquals(exception.getLocalizedMessage(), appException.getMessage());
  }

  /**
   * Tests ProcessCRADeathmatch() with an invalid DOD format causing the test to
   * fail
   *
   * @throws Exception
   */
  @Test
  public void testProcessCRADeathMatch_validDODFormat() throws Exception {

    testRequestData.getData().getClientData().getDateOfDeath()
      .setDate("1990-01-01");

    testRequest.niemPayload = BDMRestUtil.convertToJSON(testRequestData);
    testSubject.processCRADeathMatch(testRequest);

    assertEquals(true,
      craHelper.validateDateFormat(craDeathDate, BDMConstants.kDOD));
  }

  /**
   * Tests ProcessCRADeathmatch() with an invalid DOB format causing the test to
   * fail
   *
   * @throws Exception
   */
  @Test
  public void testProcessCRADeathMatch_invalidDOBFormat() throws Exception {

    testRequestData.getData().getClientData().getDateOfBirth()
      .setDate("01-01-1990");

    testRequest.niemPayload = BDMRestUtil.convertToJSON(testRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRADeathMatch(testRequest));

    final AppException appException =
      new AppException(BDMBPOCCT.ERR_HTTP_404_RESOURCE_NOT_FOUND);
    appException.arg(BDMConstants.kDOB);

    assertEquals(exception.getLocalizedMessage(), appException.getMessage());

  }

  /**
   * Tests ProcessCRADeathmatch() with an invalid SIN format causing the test to
   * fail
   *
   * @throws Exception
   */
  @Test
  public void testProcessCRADeathMatch_invalidSINFormat() throws Exception {

    testRequestData.getData().getClientData().getPersonSINIdentification()
      .setSin(" ");

    testRequest.niemPayload = BDMRestUtil.convertToJSON(testRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRADeathMatch(testRequest));

    final AppException appException =
      new AppException(BDMBPOCCT.ERR_HTTP_404_RESOURCE_NOT_FOUND);
    appException.arg(BDMConstants.kSIN);

    assertEquals(exception.getLocalizedMessage(), appException.getMessage());

  }

  /**
   * Tests ProcessCRADeathmatch() with an invalid PersonNameList causing the
   * test
   * to fail
   *
   * @throws Exception
   */
  @Test
  public void testProcessCRADeathMatch_invalidPersonNameList()
    throws Exception {

    testRequestData.getData().getClientData().setPersonNameList(null);

    testRequest.niemPayload = BDMRestUtil.convertToJSON(testRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRADeathMatch(testRequest));
    assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST,
      exception.getCatEntry());
    assertEquals(BDMInterfaceConstants.BDM_INVALID_REQUEST,
      exception.getMessage());
  }

  /**
   * Tests ProcessCRADeathmatch() with an invalid Surname causing the test to
   * fail
   *
   * @throws Exception
   */
  @Test
  public void testProcessCRADeathMatch_invalidSurname() throws Exception {

    testRequestData.getData().getClientData().getPersonNameList().get(0)
      .setPersonSurname(null);

    testRequest.niemPayload = BDMRestUtil.convertToJSON(testRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRADeathMatch(testRequest));

    final AppException appException =
      new AppException(BDMBPOCCT.ERR_HTTP_404_RESOURCE_NOT_FOUND);
    appException.arg(BDMConstants.kSURNAME);

    assertEquals(exception.getLocalizedMessage(), appException.getMessage());

  }

  /**
   * Tests ProcessCRADeathmatch() where the reqParamValid will be false causing
   * the test to fail
   *
   * @throws Exception
   */
  @Test
  public void testProcessCRADeathMatch_isReqParamValidFalse()
    throws Exception {

    testRequestData.getData().getClientData().getPersonSINIdentification()
      .setSin("1234567");
    testRequest.niemPayload = BDMRestUtil.convertToJSON(testRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRADeathMatch(testRequest));

    final AppException appException =
      new AppException(BDMBPOCCT.ERR_HTTP_404_RESOURCE_NOT_FOUND);
    appException.arg(BDMConstants.kSIN);

    assertEquals(exception.getLocalizedMessage(), appException.getMessage());

  }

  /**
   * Tests ProcessCRADeathmatch() where the reqParamNullOrEmpty will be false
   * causing the test to fail
   *
   * @throws Exception
   */
  @Test
  public void testProcessCRADeathMatch_isReqParamNullOrEmpty()
    throws Exception {

    testRequestData.getData().getClientData().getPersonSINIdentification()
      .setSin(" ");
    testRequestData.getData().getClientData().getDateOfBirth().setDate(" ");

    testRequestData.getData().getClientData().getPersonNameList().get(0)
      .setPersonSurname("");

    testRequest.niemPayload = BDMRestUtil.convertToJSON(testRequestData);
    final AppException exception = assertThrows(AppException.class,
      () -> testSubject.processCRADeathMatch(testRequest));

    final AppException appException =
      new AppException(BDMBPOCCT.ERR_HTTP_404_RESOURCE_NOT_FOUND);
    appException.arg(BDMConstants.kDOB);

    assertEquals(exception.getLocalizedMessage(), appException.getMessage());

  }

  /**
   * Tests ProcessCRADeathmatch() where there will be a match with the first
   * five
   * letters of the surname and the operation will be successful
   *
   * @throws Exception
   */
  @Test
  public void
    testProcessCRADeathMatch_matchingRecordFoundfirstFiveLettersSurnameMatch()
      throws Exception {

    final UniqueID uniqueID = UniqueIDFactory.newInstance();
    final PersonDtls personDtls = new PersonDtls();
    personDtls.concernRoleID = uniqueID.getNextID();
    personDtls.dateOfBirth = Date.getDate(isoCRABirthDate);
    personDtls.personBirthName = craSurName;
    createPersonRecordWithSin(personDtls, sin, DUPLICATESTATUS.UNMARKED);// creates
                                                                         // person
                                                                         // record
                                                                         // on
                                                                         // curam
                                                                         // local
                                                                         // database

    final BDMOASPersonSummaryDetailsList curamdata = searchPersonBySin(sin);

    testRequest.niemPayload = BDMRestUtil.convertToJSON(testRequestData);

    testSubject.processCRADeathMatch(testRequest);

    assertTrue(craHelper.firstFiveLettersSurnameMatch(
      curamdata.dtls.get(0).lastName, craSurName));

  }

  /**
   * Tests ProcessCRADeathmatch() where there will be a match with the month and
   * year and the operation will be successful
   *
   * @throws Exception
   */
  @Test
  public void
    testProcessCRADeathMatch_matchingRecordFoundMatchMonthMatchYear()
      throws Exception {

    final UniqueID uniqueID = UniqueIDFactory.newInstance();
    final PersonDtls personDtls = new PersonDtls();
    personDtls.concernRoleID = uniqueID.getNextID();
    personDtls.dateOfBirth = Date.getDate(isoCRABirthDate);
    personDtls.personBirthName = craSurName;
    createPersonRecordWithSin(personDtls, sin, DUPLICATESTATUS.UNMARKED);// creates
                                                                         // person
                                                                         // record
                                                                         // on
                                                                         // curam
                                                                         // local
                                                                         // database

    final BDMOASPersonSummaryDetailsList curamdata = searchPersonBySin(sin);

    testRequest.niemPayload = BDMRestUtil.convertToJSON(testRequestData);

    testSubject.processCRADeathMatch(testRequest);

    assertTrue(craHelper.matchMonth(curamdata.dtls.get(0).dateOfBirth,
      Date.getDate(isoCRABirthDate)));

  }

  /**
   * Tests ProcessCRADeathmatch() where there will be a successful person match
   * and the operation will be successful
   *
   * @throws Exception
   */
  @Test
  public void testProcessCRADeathMatch_isSuccessfullPersonMatch()
    throws Exception {

    final UniqueID uniqueID = UniqueIDFactory.newInstance();
    final PersonDtls personDtls = new PersonDtls();
    personDtls.concernRoleID = uniqueID.getNextID();
    personDtls.dateOfBirth = Date.getDate(isoCRABirthDate);
    personDtls.personBirthName = craSurName;
    createPersonRecordWithSin(personDtls, sin, DUPLICATESTATUS.UNMARKED);// creates
                                                                         // person
                                                                         // record
                                                                         // on
                                                                         // curam
                                                                         // local
                                                                         // database

    final BDMOASPersonSummaryDetailsList curamdata = searchPersonBySin(sin);

    testRequest.niemPayload = BDMRestUtil.convertToJSON(testRequestData);

    testSubject.processCRADeathMatch(testRequest);
    // Count the number of matching fields
    int matchingFields = 0;

    // compare 5 first letters of surname
    if (craHelper.firstFiveLettersSurnameMatch(curamdata.dtls.get(0).lastName,
      craSurName)) {
      matchingFields++;
    }

    // compare DOB month

    if (craHelper.matchMonth(curamdata.dtls.get(0).dateOfBirth,
      Date.getDate(isoCRABirthDate))) {
      matchingFields++;
    }
    if (craHelper.matchYear(curamdata.dtls.get(0).dateOfBirth,
      Date.getDate(isoCRABirthDate))) {
      matchingFields++;
    }

    assertTrue(matchingFields >= 2);

  }

  /**
   * Tests ProcessCRADeathmatch() where there will not be a successful person
   * match
   * and the operation will be unsuccessful
   *
   * @throws Exception
   */
  @Test
  public void testProcessCRADeathMatch_isSuccessfullPersonMatchFailure()
    throws Exception {

    final UniqueID uniqueID = UniqueIDFactory.newInstance();
    final PersonDtls personDtls = new PersonDtls();
    personDtls.concernRoleID = uniqueID.getNextID();
    personDtls.dateOfBirth = Date.getCurrentDate();
    personDtls.personBirthName = "Smith";
    createPersonRecordWithSin(personDtls, sin, DUPLICATESTATUS.UNMARKED);// creates
                                                                         // person
                                                                         // record
                                                                         // on
                                                                         // curam
                                                                         // local
                                                                         // database

    final BDMOASPersonSummaryDetailsList curamdata = searchPersonBySin(sin);

    testRequest.niemPayload = BDMRestUtil.convertToJSON(testRequestData);

    testSubject.processCRADeathMatch(testRequest);
    // Count the number of matching fields
    int matchingFields = 0;

    // compare 5 first letters of surname
    if (craHelper.firstFiveLettersSurnameMatch(curamdata.dtls.get(0).lastName,
      craSurName)) {
      matchingFields++;
    }

    // compare DOB month
    if (craHelper.matchMonth(curamdata.dtls.get(0).dateOfBirth,
      Date.getDate(isoCRABirthDate))) {
      matchingFields++;
    }
    if (craHelper.matchYear(curamdata.dtls.get(0).dateOfBirth,
      Date.getDate(isoCRABirthDate))) {
      matchingFields++;
    }

    assertFalse(matchingFields >= 2);

  }

  /**
   * Tests ProcessCRADeathmatch() where the person criteria match list is
   * obtained
   * and the operation will be successful
   *
   * @throws Exception
   */
  @Test
  public void testProcessCRADeathMatch_personAllCriteriaMatchListObtained()
    throws Exception {

    final BDMOASPersonSummaryDetails craPersonDtls =
      new BDMOASPersonSummaryDetails();

    craPersonDtls.dateOfBirth = Date.getDate(isoCRABirthDate);
    craPersonDtls.lastName = craSurName;
    final UniqueID uniqueID = UniqueIDFactory.newInstance();
    final BDMOASPersonSummaryDetails curamPersonDtls =
      new BDMOASPersonSummaryDetails();
    final BDMOASPersonSummaryDetailsList personAllCriteriaMatchDtlsList =
      new BDMOASPersonSummaryDetailsList();

    final PersonDtls personDtls = new PersonDtls();
    personDtls.concernRoleID = uniqueID.getNextID();
    personDtls.dateOfBirth = Date.getDate(isoCRABirthDate);
    personDtls.personBirthName = craSurName;
    createPersonRecordWithSin(personDtls, sin, DUPLICATESTATUS.UNMARKED);// creates
                                                                         // person
                                                                         // record
                                                                         // on
                                                                         // curam
                                                                         // local
                                                                         // database

    final BDMOASPersonSummaryDetailsList curamdata = searchPersonBySin(sin);

    testRequest.niemPayload = BDMRestUtil.convertToJSON(testRequestData);

    testSubject.processCRADeathMatch(testRequest);

    if (curamdata.dtls.size() > 0) {

      for (int i = 0; i < curamdata.dtls.size(); i++) {
        curamPersonDtls.dateOfBirth = curamdata.dtls.get(i).dateOfBirth;
        curamPersonDtls.lastName = curamdata.dtls.get(i).lastName;
        if (craHelper.isSuccessfulPersonMatch(curamPersonDtls,
          craPersonDtls)) {
          personAllCriteriaMatchDtlsList.dtls.add(curamPersonDtls);
        }

      }

    }

    assertTrue(personAllCriteriaMatchDtlsList.dtls.size() > 0);

  }

  /**
   * Tests ProcessCRADeathmatch() where there will be one person match found and
   * the operation will be successful
   *
   * @throws Exception
   */
  @Test
  public void testProcessCRADeathMatch_onePersonMatchFound()
    throws Exception {

    final BDMOASPersonSummaryDetails craPersonDtls =
      new BDMOASPersonSummaryDetails();

    craPersonDtls.dateOfBirth = Date.getDate(isoCRABirthDate);
    craPersonDtls.lastName = craSurName;
    final UniqueID uniqueID = UniqueIDFactory.newInstance();
    final BDMOASPersonSummaryDetails curamPersonDtls =
      new BDMOASPersonSummaryDetails();
    final BDMOASPersonSummaryDetailsList personAllCriteriaMatchDtlsList =
      new BDMOASPersonSummaryDetailsList();

    final PersonDtls personDtls = new PersonDtls();
    personDtls.concernRoleID = uniqueID.getNextID();
    personDtls.dateOfBirth = Date.getDate(isoCRABirthDate);
    personDtls.personBirthName = craSurName;
    createPersonRecordWithSin(personDtls, sin, DUPLICATESTATUS.UNMARKED);// creates
                                                                         // person
                                                                         // record
                                                                         // on
                                                                         // curam
                                                                         // local
                                                                         // database

    final BDMOASPersonSummaryDetailsList curamdata = searchPersonBySin(sin);

    testRequest.niemPayload = BDMRestUtil.convertToJSON(testRequestData);

    testSubject.processCRADeathMatch(testRequest);

    if (curamdata.dtls.size() > 0) {

      for (int i = 0; i < curamdata.dtls.size(); i++) {
        curamPersonDtls.dateOfBirth = curamdata.dtls.get(i).dateOfBirth;
        curamPersonDtls.lastName = curamdata.dtls.get(i).lastName;
        if (craHelper.isSuccessfulPersonMatch(curamPersonDtls,
          craPersonDtls)) {
          personAllCriteriaMatchDtlsList.dtls.add(curamPersonDtls);
        }

      }

    }

    assertEquals(1, personAllCriteriaMatchDtlsList.dtls.size());

  }

  /**
   * Tests ProcessCRADeathmatch() where there will be no person match found and
   * the operation will be successful
   *
   * @throws Exception
   */
  @Test
  public void testProcessCRADeathMatch_noMatchFoundWithAllCriteria()
    throws Exception {

    final BDMOASPersonSummaryDetails craPersonDtls =
      new BDMOASPersonSummaryDetails();

    craPersonDtls.dateOfBirth = Date.getDate(isoCRABirthDate);
    craPersonDtls.dateOfDeath = Date.getDate(isoCRADeathDate);
    craPersonDtls.lastName = craSurName;
    final UniqueID uniqueID = UniqueIDFactory.newInstance();
    final BDMOASPersonSummaryDetails curamPersonDtls =
      new BDMOASPersonSummaryDetails();
    final BDMOASPersonSummaryDetailsList personAllCriteriaMatchDtlsList =
      new BDMOASPersonSummaryDetailsList();

    final PersonDtls personDtls = new PersonDtls();
    personDtls.concernRoleID = uniqueID.getNextID();
    personDtls.dateOfBirth = Date.getCurrentDate();
    personDtls.personBirthName = craSurName;
    createPersonRecordWithSin(personDtls, sin, DUPLICATESTATUS.UNMARKED);

    final BDMOASPersonSummaryDetailsList curamdata = searchPersonBySin(sin);

    testRequest.niemPayload = BDMRestUtil.convertToJSON(testRequestData);

    testSubject.processCRADeathMatch(testRequest);

    for (int i = 0; i < curamdata.dtls.size(); i++) {
      curamPersonDtls.dateOfBirth = curamdata.dtls.get(i).dateOfBirth;
      curamPersonDtls.lastName = curamdata.dtls.get(i).lastName;
      if (craHelper.isSuccessfulPersonMatch(curamPersonDtls, craPersonDtls)) {
        personAllCriteriaMatchDtlsList.dtls.add(curamPersonDtls);
      }

    }
    assertTrue(personAllCriteriaMatchDtlsList.dtls.size() == 0);
  }

  /**
   * Tests ProcessCRADeathmatch() where all the person criteria match list is
   * obtained and the operation will be successful
   *
   * @throws Exception
   */
  @Test
  public void
    testProcessCRADeathMatch_personAllCriteriaMatchListObtainedDODMatched()
      throws Exception {

    Boolean isDODMatch = false;
    Boolean IsSuccessfullMatch = false;

    final BDMOASPersonSummaryDetails craPersonDtls =
      new BDMOASPersonSummaryDetails();

    craPersonDtls.dateOfBirth = Date.getDate(isoCRABirthDate);
    craPersonDtls.dateOfDeath = Date.getDate(isoCRADeathDate);
    craPersonDtls.lastName = craSurName;
    final UniqueID uniqueID = UniqueIDFactory.newInstance();
    final BDMOASPersonSummaryDetails curamPersonDtls =
      new BDMOASPersonSummaryDetails();
    final BDMOASPersonSummaryDetailsList personAllCriteriaMatchDtlsList =
      new BDMOASPersonSummaryDetailsList();

    final PersonDtls personDtls = new PersonDtls();
    personDtls.concernRoleID = uniqueID.getNextID();
    personDtls.dateOfBirth = Date.getDate(isoCRABirthDate);
    personDtls.dateOfDeath = Date.getDate(isoCRADeathDate);
    personDtls.personBirthName = craSurName;
    createPersonRecordWithSin(personDtls, sin, DUPLICATESTATUS.UNMARKED);

    final BDMOASPersonSummaryDetailsList curamdata = searchPersonBySin(sin);

    testRequest.niemPayload = BDMRestUtil.convertToJSON(testRequestData);

    testSubject.processCRADeathMatch(testRequest);

    if (curamdata.dtls.size() > 0) {

      for (int i = 0; i < curamdata.dtls.size(); i++) {
        curamPersonDtls.dateOfBirth = curamdata.dtls.get(i).dateOfBirth;
        curamPersonDtls.dateOfDeath = curamdata.dtls.get(i).dateOfDeath;
        curamPersonDtls.lastName = curamdata.dtls.get(i).lastName;
        if (craHelper.isSuccessfulPersonMatch(curamPersonDtls,
          craPersonDtls)) {
          personAllCriteriaMatchDtlsList.dtls.add(curamPersonDtls);
        }

      }

      if (personAllCriteriaMatchDtlsList.dtls
        .get(0).duplicateStatus != DUPLICATESTATUS.MARKED) {
        isDODMatch = craHelper.isDODMatched(
          personAllCriteriaMatchDtlsList.dtls.get(0).dateOfDeath,
          Date.getDate(isoCRADeathDate));
        if (!isDODMatch) {
          craHelper.createCRADeathMismatch(true, false, curamPersonDtls);
          Trace.kTopLevelLogger.debug(BDMConstants.BDM_LOGS_PREFIX
            + "person match found but dod did not match");
          IsSuccessfullMatch = false;
        } else {
          // If person matched and dod matches the matching process must end
          Trace.kTopLevelLogger.debug(BDMConstants.BDM_LOGS_PREFIX
            + "person match found and dod matched");
          IsSuccessfullMatch = true;
        }
      }

      assertTrue(IsSuccessfullMatch);
    }

  }

  /**
   * Tests ProcessCRADeathmatch() where the matching record found is original
   * and
   * the operation will be successful
   *
   * @throws Exception
   */
  @Test
  public void testProcessCRADeathMatch_matchingRecordFoundIsOriginal()
    throws Exception {

    final UniqueID uniqueID = UniqueIDFactory.newInstance();

    final PersonDtls originalPersonDtls = new PersonDtls();
    originalPersonDtls.concernRoleID = uniqueID.getNextID();
    originalPersonDtls.dateOfBirth = Date.getDate(isoCRABirthDate);
    originalPersonDtls.personBirthName = craSurName;
    createPersonRecordWithSin(originalPersonDtls, sin,
      DUPLICATESTATUS.UNMARKED);// creates person record on curam local database

    final PersonDtls duplicatePersonDtls = new PersonDtls();
    duplicatePersonDtls.concernRoleID = uniqueID.getNextID();
    duplicatePersonDtls.dateOfBirth = Date.getDate(isoCRABirthDate);
    duplicatePersonDtls.personBirthName = craSurName;
    createPersonRecordWithSin(duplicatePersonDtls, sin,
      DUPLICATESTATUS.MARKED);// creates person record on curam local database

    final BDMOASPersonSummaryDetailsList curamdata = searchPersonBySin(sin);
    final ConcernRoleKey key = new ConcernRoleKey();
    key.concernRoleID = originalPersonDtls.concernRoleID;

    final MarkDuplicateCreateDetails markDuplicateDetails =
      new MarkDuplicateCreateDetails();
    markDuplicateDetails.markDuplicateCreateDetails.originalConcernRoleID =
      originalPersonDtls.concernRoleID;
    markDuplicateDetails.markDuplicateCreateDetails.duplicateConcernRoleID =
      duplicatePersonDtls.concernRoleID;
    markDuplicateDetails.markDuplicateCreateDetails.duplicateReason =
      DUPLICATEREASON.PERSONMATCH;

    markDuplicate(markDuplicateDetails);

    testRequest.niemPayload = BDMRestUtil.convertToJSON(testRequestData);

    testSubject.processCRADeathMatch(testRequest);

    assertTrue(
      curamdata.dtls.get(0).duplicateStatus != DUPLICATESTATUS.MARKED);

  }

  /**
   * Tests ProcessCRADeathmatch() where the matching record found is a duplicate
   * and the operation will be successful
   *
   * @throws Exception
   */
  @Test
  public void testProcessCRADeathMatch_matchingRecordFoundIsDuplicate()
    throws Exception {

    final UniqueID uniqueID = UniqueIDFactory.newInstance();
    final BDMOASPersonSummaryDetails craPersonDtls =
      new BDMOASPersonSummaryDetails();

    craPersonDtls.dateOfBirth = Date.getDate(isoCRABirthDate);
    craPersonDtls.dateOfDeath = Date.getDate(isoCRADeathDate);
    craPersonDtls.lastName = craSurName;
    final PersonDtls duplicatePersonDtls = new PersonDtls();
    duplicatePersonDtls.concernRoleID = uniqueID.getNextID();
    duplicatePersonDtls.dateOfBirth = Date.getDate(isoCRABirthDate);
    duplicatePersonDtls.personBirthName = craSurName;
    createPersonRecordWithSin(duplicatePersonDtls, sin,
      DUPLICATESTATUS.MARKED);

    final PersonDtls originalPersonDtls = new PersonDtls();
    originalPersonDtls.concernRoleID = uniqueID.getNextID();
    originalPersonDtls.dateOfBirth = Date.getDate(isoCRABirthDate);
    originalPersonDtls.personBirthName = craSurName;
    createPersonRecordWithSin(originalPersonDtls, sin,
      DUPLICATESTATUS.UNMARKED);// creates person record on curam local database

    final MarkDuplicateCreateDetails markDuplicateDetails =
      new MarkDuplicateCreateDetails();
    markDuplicateDetails.markDuplicateCreateDetails.originalConcernRoleID =
      originalPersonDtls.concernRoleID;
    markDuplicateDetails.markDuplicateCreateDetails.duplicateConcernRoleID =
      duplicatePersonDtls.concernRoleID;
    markDuplicateDetails.markDuplicateCreateDetails.duplicateReason =
      DUPLICATEREASON.PERSONMATCH;

    final MarkDuplicateDetails mrkDuplDtls =
      markDuplicate(markDuplicateDetails);

    final PersonKey personKey = new PersonKey();
    final Person personObj = PersonFactory.newInstance();

    personKey.concernRoleID =
      mrkDuplDtls.markDuplicateDetails.duplicateConcernRoleID;

    final BDMOASPersonSummaryDetailsList curamdata = searchPersonBySin(sin);

    testRequest.niemPayload = BDMRestUtil.convertToJSON(testRequestData);

    testSubject.processCRADeathMatch(testRequest);

    assertEquals(curamdata.dtls.get(0).duplicateStatus,
      DUPLICATESTATUS.MARKED);

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
