package curam.ca.gc.bdmoas.test.rest.bdmoascraapi.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMInterfaceNameCode;
import curam.ca.gc.bdm.entity.bdmsummary.fact.BDMInterfaceSummaryFactory;
import curam.ca.gc.bdm.entity.bdmsummary.intf.BDMInterfaceSummary;
import curam.ca.gc.bdm.entity.bdmsummary.struct.BDMCRADeathMatchSearchKey;
import curam.ca.gc.bdm.entity.bdmsummary.struct.BDMInterfaceSummaryDtls;
import curam.ca.gc.bdm.entity.bdmsummary.struct.BDMInterfaceSummaryDtlsList;
import curam.ca.gc.bdm.entity.bdmsummary.struct.BDMInterfaceSummaryKey;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMBPOCCT;
import curam.ca.gc.bdm.util.impl.BDMDateUtil;
import curam.ca.gc.bdmoas.entity.struct.BDMOASPersonSummaryDetails;
import curam.ca.gc.bdmoas.entity.struct.BDMOASPersonSummaryDetailsList;
import curam.ca.gc.bdmoas.rest.bdmoascraapi.impl.BDMOASCRAHelper;
import curam.ca.gc.bdmoas.test.concern.person.RegisterPerson;
import curam.codetable.ADDRESSLAYOUTTYPE;
import curam.codetable.CONCERNROLETYPE;
import curam.codetable.COUNTRY;
import curam.codetable.DUPLICATESTATUS;
import curam.core.fact.AddressFactory;
import curam.core.fact.AlternateNameFactory;
import curam.core.fact.ConcernFactory;
import curam.core.fact.ConcernRoleAlternateIDFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.fact.PersonFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.impl.EnvVars;
import curam.core.intf.UniqueID;
import curam.core.struct.AddressDtls;
import curam.core.struct.AlternateNameDtls;
import curam.core.struct.ConcernDtls;
import curam.core.struct.ConcernRoleAlternateIDDtls;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.PersonDtls;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import java.util.Calendar;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * @author anthony.chraim
 *
 */
public class BDMOASCRAHelperTest
  extends curam.testframework.CuramServerTestJUnit4 {

  @Inject
  BDMOASCRAHelper craHelper;

  public BDMOASCRAHelperTest() {

    super();

  }

  @Before
  /**
   * Set up function and curam server test for CRA Death match tests
   */
  public void setUpCRADeathMatchTests() {

    super.setUpCuramServerTest();
    craHelper = new BDMOASCRAHelper();

    Configuration.setProperty(EnvVars.ENV_PDC_ENABLED, "NO");
  }

  RegisterPerson registerPerson = new RegisterPerson("RegisterPerson");

  /**
   * Test validateSINFormat() for a scenario where the input is valid and the
   * operation should succeed
   *
   * @throws Exception
   */
  @Test
  public void validateSINFormat_success() throws Exception {

    final String validSIN = "123456789 ";
    final boolean successInd = craHelper.validateSINFormat(validSIN);
    assertTrue(successInd);
  }

  /**
   * Test validateSINFormat() for a scenario where the input is empty causing the
   * test to fail
   *
   * @throws Exception
   */
  @Test
  public void validateSINFormat_SINEmpty() throws Exception {

    final String invalidSIN = "";
    assertFalse(craHelper.validateSINFormat(invalidSIN));

  }

  /**
   * Test validateSINFormat() for a scenario where the input is missing causing
   * the test to fail
   *
   * @throws Exception
   */
  @Test
  public void validateSINFormat_SINMissing() throws Exception {

    final String invalidSIN = null;

    assertFalse(craHelper.validateSINFormat(invalidSIN));
  }

  /**
   * Test validateSINFormat() for a scenario where the input is too long (more
   * than 9 digits) causing the test to fail
   *
   * @throws Exception
   */
  @Test
  public void validateSINFormat_SINTooLong() throws Exception {

    final String invalidSIN = "123456789101112";

    assertFalse(craHelper.validateSINFormat(invalidSIN));
  }

  /**
   * Test validateSINFormat() for a scenario where the input is too short (less
   * than 9 digits) causing the test to fail
   *
   * @throws Exception
   */
  @Test
  public void validateSINFormat_SINTooShort() throws Exception {

    final String invalidSIN = "12345";

    assertFalse(craHelper.validateSINFormat(invalidSIN));
  }

  /**
   * Test validateSINFormat() for a scenario where the input is in an incorrect
   * format (not numeric) causing the test to fail
   *
   * @throws Exception
   */
  @Test
  public void validateSINFormat_SINIncorrectFormat() throws Exception {

    final String invalidSIN = "abcdefg";

    assertFalse(craHelper.validateSINFormat(invalidSIN));

  }

  /**
   * Test validateDateFormat() for a DOB scenario where the input is valid and the
   * operation should succeed
   *
   * @throws Exception
   */
  @Test
  public void validateDateFormatDOB_success() throws Exception {

    final String validDate = "1990-01-01";
    final boolean successInd =
      craHelper.validateDateFormat(validDate, BDMConstants.kDOB);
    assertTrue(successInd);
  }

  /**
   * Test validateDateFormat() for a DOD scenario where the input is valid and the
   * operation should succeed
   *
   * @throws Exception
   */
  @Test
  public void validateDateFormatDOD_success() throws Exception {

    final String validDate = "1990-01-01";
    final boolean successInd =
      craHelper.validateDateFormat(validDate, BDMConstants.kDOD);
    assertTrue(successInd);
  }

  /**
   * Test validateDateFormat() for a scenario where the input is in the wrong
   * format causing the test to fail. Right format is yyyy-mm-dd
   *
   * @throws Exception
   */
  @Test
  public void validateDateFormat_wrongFromat() throws Exception {

    final String invalidDate = "01-01-1990";

    assertFalse(craHelper.validateDateFormat(invalidDate, BDMConstants.kDOB));
  }

  /**
   * Test validateDateFormat() for a DOB scenario where the input is in the
   * missing format causing the test to fail
   *
   * @throws Exception
   */
  @Test
  public void validateDateFormatDOB_dateMissing() throws Exception {

    final String invalidDate = null;
    assertFalse(craHelper.validateDateFormat(invalidDate, BDMConstants.kDOB));

  }

  /**
   * Test validateDateFormat() for a DOD scenario where the input is in the
   * missing format causing the test to fail
   *
   * @throws Exception
   */
  @Test
  public void validateDateFormatDOD_dateMissing() throws Exception {

    final String invalidDate = null;
    assertFalse(craHelper.validateDateFormat(invalidDate, BDMConstants.kDOD));

  }

  /**
   * Test validateDateFormat() for a DOD scenario where the input is in the empty
   * format causing the test to fail
   *
   * @throws Exception
   */
  @Test
  public void validateDateFormatDOD_dateEmpty() throws Exception {

    final String invalidDate = "";

    assertFalse(craHelper.validateDateFormat(invalidDate, BDMConstants.kDOD));

  }

  /**
   * Test validateDateFormat() for a DOB scenario where the input is in the
   * missing format causing the test to fail
   *
   * @throws Exception
   */
  @Test
  public void validateDateFormatDOB_dateEmpty() throws Exception {

    final String invalidDate = "";

    assertFalse(craHelper.validateDateFormat(invalidDate, BDMConstants.kDOB));

  }

  /**
   * Test isDODMatched() for a scenario where the input is valid and the operation
   * should succeed
   *
   * @throws Exception
   */
  @Test
  public void isDODMatched_success() throws Exception {

    final String dateOfDeath = "1990-01-01";
    final String craDeathDate = "1990-01-01";

    final String isoDateOfDeath = BDMDateUtil.dateFormatter(dateOfDeath);

    final String isoCraDeathDate = BDMDateUtil.dateFormatter(craDeathDate);

    final Date curamDateOfDeath = Date.getDate(isoDateOfDeath);
    final Date curamCraDeathDate = Date.getDate(isoCraDeathDate);

    assertTrue(craHelper.isDODMatched(curamDateOfDeath, curamCraDeathDate));
  }

  /**
   * Test validateDateFormat() for a scenario where the input months are not the
   * same causing the test to fail
   *
   * @throws Exception
   */
  @Test
  public void isDODMatched_mismatchedMonths() throws Exception {

    final String dateOfDeath = "1990-03-01";
    final String craDeathDate = "1990-01-01";

    final String isoDateOfDeath = BDMDateUtil.dateFormatter(dateOfDeath);

    final String isoCraDeathDate = BDMDateUtil.dateFormatter(craDeathDate);

    final Date curamDateOfDeath = Date.getDate(isoDateOfDeath);
    final Date curamCraDeathDate = Date.getDate(isoCraDeathDate);

    assertFalse(craHelper.isDODMatched(curamDateOfDeath, curamCraDeathDate));
  }

  /**
   * Test validateDateFormat() for a scenario where the input years are not the
   * same causing the test to fail
   *
   * @throws Exception
   */
  @Test
  public void isDODMatched_mismatchedYears() throws Exception {

    final String dateOfDeath = "1991-01-01";
    final String craDeathDate = "1990-01-01";

    final String isoDateOfDeath = BDMDateUtil.dateFormatter(dateOfDeath);

    final String isoCraDeathDate = BDMDateUtil.dateFormatter(craDeathDate);

    final Date curamDateOfDeath = Date.getDate(isoDateOfDeath);
    final Date curamCraDeathDate = Date.getDate(isoCraDeathDate);

    assertFalse(craHelper.isDODMatched(curamDateOfDeath, curamCraDeathDate));
  }

  /**
   * Test validateDateFormat() for a scenario where the input months and years are
   * not the
   * same causing the test to fail
   *
   * @throws Exception
   */
  @Test
  public void isDODMatched_mismatchedMonthsAndYears() throws Exception {

    final String dateOfDeath = "1991-02-01";
    final String craDeathDate = "1990-01-01";

    final String isoDateOfDeath = BDMDateUtil.dateFormatter(dateOfDeath);

    final String isoCraDeathDate = BDMDateUtil.dateFormatter(craDeathDate);

    final Date curamDateOfDeath = Date.getDate(isoDateOfDeath);
    final Date curamCraDeathDate = Date.getDate(isoCraDeathDate);

    assertFalse(craHelper.isDODMatched(curamDateOfDeath, curamCraDeathDate));
  }

  /**
   * Test firstFiveLettersSurnameMatch() for a scenario where the input valid and
   * the operation should succeed
   *
   * @throws Exception
   */
  @Test
  public void firstFiveLettersSurnameMatch_success() throws Exception {

    final String curamSurname = "Bond";
    final String craSurname = "Bond";

    assertTrue(
      craHelper.firstFiveLettersSurnameMatch(curamSurname, craSurname));
  }

  /**
   * Test firstFiveLettersSurnameMatch() for a scenario where the input valid and
   * the operation should succeed even with long surnames (longer than 5
   * characters)
   *
   * @throws Exception
   */
  @Test
  public void firstFiveLettersSurnameMatch_successWithLongSurname()
    throws Exception {

    final String curamSurname = "Longerbond";
    final String craSurname = "longerbond";

    assertTrue(
      craHelper.firstFiveLettersSurnameMatch(curamSurname, craSurname));
  }

  /**
   * Test firstFiveLettersSurnameMatch() for a scenario where the input surnames
   * are not the same causing the test to fail
   *
   * @throws Exception
   */
  @Test
  public void firstFiveLettersSurnameMatch_mismatchedSurnames()
    throws Exception {

    final String curamSurname = "James";
    final String craSurname = "Bond";

    assertFalse(
      craHelper.firstFiveLettersSurnameMatch(curamSurname, craSurname));
  }

  /**
   * Test firstFiveLettersSurnameMatch() for a scenario where the input valid and
   * the operation should succeed even with special characters
   *
   * @throws Exception
   */
  @Test
  public void
    firstFiveLettersSurnameMatch_successWithSpecialCharacterRemoval()
      throws Exception {

    final String curamSurname = "Cl@ment";
    final String craSurname = "Clment";

    assertTrue(
      craHelper.firstFiveLettersSurnameMatch(curamSurname, craSurname));
  }

  /**
   * Test firstFiveLettersSurnameMatch() for a scenario where the input surnames
   * are not the same causing the test to fail even with special characters
   *
   * @throws Exception
   */
  @Test
  public void
    firstFiveLettersSurnameMatch_mismatchSurnamesWithSpecialCharacterRemoval()
      throws Exception {

    final String curamSurname = "Cl@ment";
    final String craSurname = "Clement";

    assertFalse(
      craHelper.firstFiveLettersSurnameMatch(curamSurname, craSurname));
  }

  /**
   * Test firstFiveLettersSurnameMatch() for a scenario where the input valid and
   * the operation should succeed even with accented characters
   *
   * @throws Exception
   */
  @Test
  public void
    firstFiveLettersSurnameMatch_matchSurnamesWithAccentCharacterNormalize()
      throws Exception {

    final String curamSurname = "Clément";
    final String craSurname = "Clement";

    assertTrue(
      craHelper.firstFiveLettersSurnameMatch(curamSurname, craSurname));
  }

  /**
   * Test firstFiveLettersSurnameMatch() for a scenario where the input surnames
   * are not the same causing the test to fail even with accented characters
   *
   * @throws Exception
   */
  @Test
  public void
    firstFiveLettersSurnameMatch_mismatchSurnamesWithAccentCharacterNormalize()
      throws Exception {

    final String curamSurname = "Clément";
    final String craSurname = "Clemint";

    assertFalse(
      craHelper.firstFiveLettersSurnameMatch(curamSurname, craSurname));
  }

  /**
   * Test matchMonth() for a scenario where the input dates are valid and the
   * operation should succeed
   *
   * @throws Exception
   */
  @Test
  public void matchMonth_success() throws Exception {

    final String curamPersonDateString = "1990-01-02";
    final String craPersonDateString = "1990-01-02";

    final String isoCuramPersonDate =
      BDMDateUtil.dateFormatter(curamPersonDateString);
    final String isoCraPersonDate =
      BDMDateUtil.dateFormatter(craPersonDateString);

    final Date curamPersonDate = Date.getDate(isoCuramPersonDate);
    final Date craPersonDate = Date.getDate(isoCraPersonDate);

    assertTrue(craHelper.matchMonth(curamPersonDate, craPersonDate));
  }

  /**
   * Test matchMonth() for a scenario where the input dates are not the same
   * causing the test to fail
   *
   * @throws Exception
   */
  @Test
  public void matchMonth_mismatchedMonths() throws Exception {

    final String curamPersonDateString = "1990-02-02";
    final String craPersonDateString = "1990-01-02";

    final String isoCuramPersonDate =
      BDMDateUtil.dateFormatter(curamPersonDateString);
    final String isoCraPersonDate =
      BDMDateUtil.dateFormatter(craPersonDateString);

    final Date curamPersonDate = Date.getDate(isoCuramPersonDate);
    final Date craPersonDate = Date.getDate(isoCraPersonDate);

    assertFalse(craHelper.matchMonth(curamPersonDate, craPersonDate));
  }

  /**
   * Test matchYear() for a scenario where the input dates are valid and the
   * operation should succeed
   *
   * @throws Exception
   */
  @Test
  public void matchYear_success() throws Exception {

    final String curamPersonDateString = "1990-01-02";
    final String craPersonDateString = "1990-01-02";

    final String isoCuramPersonDate =
      BDMDateUtil.dateFormatter(curamPersonDateString);
    final String isoCraPersonDate =
      BDMDateUtil.dateFormatter(craPersonDateString);

    final Date curamPersonDate = Date.getDate(isoCuramPersonDate);
    final Date craPersonDate = Date.getDate(isoCraPersonDate);

    assertTrue(craHelper.matchYear(curamPersonDate, craPersonDate));
  }

  /**
   * Test matchYear() for a scenario where the input dates are not the same
   * causing the test to fail
   *
   * @throws Exception
   */
  @Test
  public void matchYear_mismatchedYears() throws Exception {

    final String curamPersonDateString = "1991-01-02";
    final String craPersonDateString = "1990-01-02";

    final String isoCuramPersonDate =
      BDMDateUtil.dateFormatter(curamPersonDateString);
    final String isoCraPersonDate =
      BDMDateUtil.dateFormatter(craPersonDateString);

    final Date curamPersonDate = Date.getDate(isoCuramPersonDate);
    final Date craPersonDate = Date.getDate(isoCraPersonDate);

    assertFalse(craHelper.matchYear(curamPersonDate, craPersonDate));
  }

  /**
   * Test isSuccessfulPersonMatch() for a scenario where the input person are the
   * same and the operation should succeed
   *
   * @throws Exception
   */
  @Test
  public void isSuccessfulPersonMatch_perfectMatch() throws Exception {

    final BDMOASPersonSummaryDetails curamPersonDtls =
      new BDMOASPersonSummaryDetails();
    final BDMOASPersonSummaryDetails craPersonDtls =
      new BDMOASPersonSummaryDetails();

    curamPersonDtls.concernRoleID = 123456789;
    curamPersonDtls.lastName = "Bond";
    curamPersonDtls.dateOfBirth =
      Date.getDate(BDMDateUtil.dateFormatter("1990-01-02"));

    craPersonDtls.concernRoleID = 123456789;
    craPersonDtls.lastName = "Bond";
    craPersonDtls.dateOfBirth =
      Date.getDate(BDMDateUtil.dateFormatter("1990-01-02"));

    assertTrue(
      craHelper.isSuccessfulPersonMatch(curamPersonDtls, craPersonDtls));

  }

  /**
   * Test isSuccessfulPersonMatch() for a scenario where the input person have
   * different SINs causing the test to fail
   *
   * @throws Exception
   */
  @Test
  public void isSuccessfulPersonMatch_successAndMismatchedSIN()
    throws Exception {

    final BDMOASPersonSummaryDetails curamPersonDtls =
      new BDMOASPersonSummaryDetails();
    final BDMOASPersonSummaryDetails craPersonDtls =
      new BDMOASPersonSummaryDetails();

    curamPersonDtls.concernRoleID = 123456788;
    curamPersonDtls.lastName = "Bond";
    curamPersonDtls.dateOfBirth =
      Date.getDate(BDMDateUtil.dateFormatter("1990-01-02"));

    craPersonDtls.concernRoleID = 123456789;
    craPersonDtls.lastName = "Bond";
    craPersonDtls.dateOfBirth =
      Date.getDate(BDMDateUtil.dateFormatter("1990-01-02"));

    assertTrue(
      craHelper.isSuccessfulPersonMatch(curamPersonDtls, craPersonDtls));

  }

  /**
   * Test isSuccessfulPersonMatch() for a scenario where the input person have
   * mismatched last names but the SIN, DOB month and DOB year are the same so the
   * operation should still succeed
   *
   * @throws Exception
   */
  @Test
  public void isSuccessfulPersonMatch_successAndMismatchedLastName()
    throws Exception {

    final BDMOASPersonSummaryDetails curamPersonDtls =
      new BDMOASPersonSummaryDetails();
    final BDMOASPersonSummaryDetails craPersonDtls =
      new BDMOASPersonSummaryDetails();

    curamPersonDtls.concernRoleID = 123456789;
    curamPersonDtls.lastName = "incorrect";
    curamPersonDtls.dateOfBirth =
      Date.getDate(BDMDateUtil.dateFormatter("1990-01-02"));

    craPersonDtls.concernRoleID = 123456789;
    craPersonDtls.lastName = "Bond";
    craPersonDtls.dateOfBirth =
      Date.getDate(BDMDateUtil.dateFormatter("1990-01-02"));

    assertTrue(
      craHelper.isSuccessfulPersonMatch(curamPersonDtls, craPersonDtls));

  }

  /**
   * Test isSuccessfulPersonMatch() for a scenario where the input person have
   * mismatched DOB months but the SIN, last name and DOB year are the same so the
   * operation should still succeed
   *
   * @throws Exception
   */
  @Test
  public void isSuccessfulPersonMatch_successAndMismatchedDOBMonth()
    throws Exception {

    final BDMOASPersonSummaryDetails curamPersonDtls =
      new BDMOASPersonSummaryDetails();
    final BDMOASPersonSummaryDetails craPersonDtls =
      new BDMOASPersonSummaryDetails();

    curamPersonDtls.concernRoleID = 123456789;
    curamPersonDtls.lastName = "Bond";
    curamPersonDtls.dateOfBirth =
      Date.getDate(BDMDateUtil.dateFormatter("1990-02-02"));

    craPersonDtls.concernRoleID = 123456789;
    craPersonDtls.lastName = "Bond";
    craPersonDtls.dateOfBirth =
      Date.getDate(BDMDateUtil.dateFormatter("1990-01-02"));

    assertTrue(
      craHelper.isSuccessfulPersonMatch(curamPersonDtls, craPersonDtls));

  }

  /**
   * Test isSuccessfulPersonMatch() for a scenario where the input person have
   * mismatched DOB months but the SIN, last name and DOB year are the same so the
   * operation should still succeed
   *
   * @throws Exception
   */
  @Test
  public void isSuccessfulPersonMatch_successAndMismatchedDOBYear()
    throws Exception {

    final BDMOASPersonSummaryDetails curamPersonDtls =
      new BDMOASPersonSummaryDetails();
    final BDMOASPersonSummaryDetails craPersonDtls =
      new BDMOASPersonSummaryDetails();

    curamPersonDtls.concernRoleID = 123456789;
    curamPersonDtls.lastName = "Bond";
    curamPersonDtls.dateOfBirth =
      Date.getDate(BDMDateUtil.dateFormatter("1991-01-02"));

    craPersonDtls.concernRoleID = 123456789;
    craPersonDtls.lastName = "Bond";
    craPersonDtls.dateOfBirth =
      Date.getDate(BDMDateUtil.dateFormatter("1990-01-02"));

    assertTrue(
      craHelper.isSuccessfulPersonMatch(curamPersonDtls, craPersonDtls));

  }

  /**
   * Test isSuccessfulPersonMatch() for a scenario where the input person have
   * mismatched DOB months but the SIN, last name and DOB year are the same so the
   * operation should still succeed
   *
   * @throws Exception
   */
  @Test
  public void isSuccessfulPersonMatch_partialMismatch() throws Exception {

    final BDMOASPersonSummaryDetails curamPersonDtls =
      new BDMOASPersonSummaryDetails();
    final BDMOASPersonSummaryDetails craPersonDtls =
      new BDMOASPersonSummaryDetails();

    curamPersonDtls.concernRoleID = 123456789;
    curamPersonDtls.lastName = "Bond";
    curamPersonDtls.dateOfBirth =
      Date.getDate(BDMDateUtil.dateFormatter("1991-02-02"));

    craPersonDtls.concernRoleID = 123456789;
    craPersonDtls.lastName = "Bond";
    craPersonDtls.dateOfBirth =
      Date.getDate(BDMDateUtil.dateFormatter("1991-01-02"));

    assertTrue(
      craHelper.isSuccessfulPersonMatch(curamPersonDtls, craPersonDtls));

  }

  /**
   * Test isSuccessfulPersonMatch() for a scenario where the input person have
   * the same SIN, but the DOB month, last name and DOB year are mismatched
   * causing the test to fail
   *
   * @throws Exception
   */
  @Test
  public void isSuccessfulPersonMatch_completeMismatch() throws Exception {

    final BDMOASPersonSummaryDetails curamPersonDtls =
      new BDMOASPersonSummaryDetails();
    final BDMOASPersonSummaryDetails craPersonDtls =
      new BDMOASPersonSummaryDetails();

    curamPersonDtls.concernRoleID = 123456789;
    curamPersonDtls.lastName = "incorrect";

    curamPersonDtls.dateOfBirth =
      Date.getDate(BDMDateUtil.dateFormatter("1990-02-02"));

    craPersonDtls.concernRoleID = 123456789;
    craPersonDtls.lastName = "Bond";
    craPersonDtls.dateOfBirth =
      Date.getDate(BDMDateUtil.dateFormatter("1991-01-02"));

    assertFalse(
      craHelper.isSuccessfulPersonMatch(curamPersonDtls, craPersonDtls));

  }

  /**
   * Create a Person, Concern, Address, ConcernRole, AlternateName in order to
   * insert a them in the local curam server and search them using the SIN created
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void searchPersonBySIN_success()
    throws AppException, InformationalException {

    final Date craBirthDate =
      Date.getDate(BDMDateUtil.dateFormatter("1991-02-02"));
    final String craSurname = "Bond";
    final String sin = "123456789";
    final UniqueID uniqueID = UniqueIDFactory.newInstance();

    final PersonDtls personDtls = new PersonDtls();
    personDtls.concernRoleID = uniqueID.getNextID();
    personDtls.dateOfBirth = craBirthDate;
    personDtls.personBirthName = craSurname;

    // creates person record on curam local database
    createPersonRecordWithSin(personDtls, sin, DUPLICATESTATUS.UNMARKED);

    // createBDMSummaryDeatailsList with that person
    final BDMOASPersonSummaryDetailsList pdcPersonSummaryDetailsList =
      new BDMOASPersonSummaryDetailsList();
    final BDMOASPersonSummaryDetails pdcPersonSummaryDetails =
      new BDMOASPersonSummaryDetails();

    pdcPersonSummaryDetails.concernRoleID = personDtls.concernRoleID;
    pdcPersonSummaryDetails.dateOfBirth = personDtls.dateOfBirth;
    pdcPersonSummaryDetails.dateOfDeath = personDtls.dateOfDeath;
    pdcPersonSummaryDetails.maritalStatus = personDtls.maritalStatusCode;

    pdcPersonSummaryDetailsList.dtls.add(pdcPersonSummaryDetails);

    // create personSummaryDtls with same concerRoleID
    final BDMInterfaceSummaryDtls interfaceSummaryDtls =
      new BDMInterfaceSummaryDtls();
    interfaceSummaryDtls.interfaceSummaryID = personDtls.concernRoleID;

    // search person create with sin using seachPersonBySIN
    final BDMOASPersonSummaryDetailsList BDMOASPersonSummaryDetailsList =
      craHelper.searchPersonBySIN(sin);

    // compare BDMOASPersonSummaryDetailsList
    assertEquals(BDMOASPersonSummaryDetailsList.dtls.get(0).concernRoleID,
      personDtls.concernRoleID);

  }

  /**
   * Test searchPersonBySIN with a specific sin without adding it to the local
   * server causing the test to fail
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void searchPersonBySIN_noMatch()
    throws AppException, InformationalException {

    final String sin = "123456789";

    final BDMOASPersonSummaryDetailsList personSummaryDetailsList =
      craHelper.searchPersonBySIN(sin);
    assertEquals(personSummaryDetailsList.dtls.size(), 0);

  }

  /**
   * Test createSummaryData with no existing records and the record status input
   * being false. Read the local server after to see to see the if it got updated
   * properly
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void createSummaryData_noRecords_falseRecordStatus()
    throws AppException, InformationalException {

    // generate unique ID
    final UniqueID uniqueID = UniqueIDFactory.newInstance();

    final String interfaceName = BDMInterfaceNameCode.CRA_DEATH_MATCH;
    final Date date = Date.getDate(BDMDateUtil.dateFormatter("1991-02-02"));
    final boolean recordStatus = false;
    final long interfaceID = uniqueID.getNextID();

    // initialize keys in order to find the interfaceSummaryDtlsList from the DB
    final BDMCRADeathMatchSearchKey craDeathMatchSearchKey =
      new BDMCRADeathMatchSearchKey();

    craDeathMatchSearchKey.interfaceNameCode = interfaceName;
    craDeathMatchSearchKey.year = date.getCalendar().get(Calendar.YEAR);

    // cant read from BDMInterfaceSummary since there are no records so we
    // instantiate instead
    final BDMInterfaceSummaryDtls interfaceSummaryObjdtls =
      new BDMInterfaceSummaryDtls();

    final BDMInterfaceSummary interfaceSummaryObj =
      BDMInterfaceSummaryFactory.newInstance();

    // read from table
    final BDMInterfaceSummaryDtlsList interfaceSummaryDtlsList =
      interfaceSummaryObj.searchByNameCodeAndYear(craDeathMatchSearchKey);

    final BDMInterfaceSummaryKey interfaceSummaryObjKey =
      new BDMInterfaceSummaryKey();

    interfaceSummaryObjKey.interfaceSummaryID = interfaceID;

    // call function that we are testing
    craHelper.createSummaryData(interfaceName, recordStatus, date,
      interfaceID);

    final BDMInterfaceSummaryDtls interfaceSummaryDtls =
      BDMInterfaceSummaryFactory.newInstance().read(interfaceSummaryObjKey);

    assertEquals(interfaceSummaryDtls.interfaceNameCode, interfaceName);
    assertEquals(interfaceSummaryDtls.totalRecords,
      interfaceSummaryDtlsList.dtls.size() + 1);
    assertEquals(interfaceSummaryDtls.year,
      date.getCalendar().get(Calendar.YEAR));
    assertEquals(interfaceSummaryDtls.totalTasks,
      interfaceSummaryObjdtls.totalTasks + 1);
    assertEquals(interfaceSummaryDtls.totalSuccessRecords,
      interfaceSummaryObjdtls.totalSuccessRecords);

  }

  /**
   * Test createSummaryData with no existing records and the record status input
   * being true. Read the local server after to see to see the if it got updated
   * properly
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void createSummaryData_noRecords_trueRecordStatus()
    throws AppException, InformationalException {

    // generate unique ID
    final UniqueID uniqueID = UniqueIDFactory.newInstance();

    final String interfaceName = BDMInterfaceNameCode.CRA_DEATH_MATCH;
    final Date date = Date.getDate(BDMDateUtil.dateFormatter("1991-02-02"));
    final boolean recordStatus = true;
    final long interfaceID = uniqueID.getNextID();

    // initialize keys in order to find the interfaceSummaryDtlsList from the DB
    final BDMCRADeathMatchSearchKey craDeathMatchSearchKey =
      new BDMCRADeathMatchSearchKey();

    craDeathMatchSearchKey.interfaceNameCode = interfaceName;
    craDeathMatchSearchKey.year = date.getCalendar().get(Calendar.YEAR);

    // cant read from BDMInterfaceSummary since there are no records so we
    // instantiate instead
    final BDMInterfaceSummaryDtls interfaceSummaryObjdtls =
      new BDMInterfaceSummaryDtls();

    final BDMInterfaceSummary interfaceSummaryObj =
      BDMInterfaceSummaryFactory.newInstance();

    // read from table
    final BDMInterfaceSummaryDtlsList interfaceSummaryDtlsList =
      interfaceSummaryObj.searchByNameCodeAndYear(craDeathMatchSearchKey);

    final BDMInterfaceSummaryKey interfaceSummaryObjKey =
      new BDMInterfaceSummaryKey();

    interfaceSummaryObjKey.interfaceSummaryID = interfaceID;

    // call function that we are testing
    craHelper.createSummaryData(interfaceName, recordStatus, date,
      interfaceID);

    final BDMInterfaceSummaryDtls interfaceSummaryDtls =
      BDMInterfaceSummaryFactory.newInstance().read(interfaceSummaryObjKey);

    assertEquals(interfaceSummaryDtls.interfaceNameCode, interfaceName);
    assertEquals(interfaceSummaryDtls.totalRecords,
      interfaceSummaryDtlsList.dtls.size() + 1);
    assertEquals(interfaceSummaryDtls.year,
      date.getCalendar().get(Calendar.YEAR));
    assertEquals(interfaceSummaryDtls.totalTasks,
      interfaceSummaryDtlsList.dtls.size());
    assertEquals(interfaceSummaryDtls.totalSuccessRecords,
      interfaceSummaryObjdtls.totalSuccessRecords + 1);

  }

  /**
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void createSummaryData_withRecords_falseRecordStatus()
    throws AppException, InformationalException {

    // generate unique ID
    final UniqueID uniqueID = UniqueIDFactory.newInstance();

    final String interfaceName = BDMInterfaceNameCode.CRA_DEATH_MATCH;
    final Date date = Date.getDate(BDMDateUtil.dateFormatter("1991-02-02"));
    final boolean recordStatus = false;
    final long interfaceID = uniqueID.getNextID();

    // create record
    final BDMInterfaceSummaryDtls details = new BDMInterfaceSummaryDtls();
    details.interfaceSummaryID = interfaceID;
    details.totalRecords = 1;
    details.interfaceNameCode = BDMInterfaceNameCode.CRA_DEATH_MATCH;
    details.year = date.getCalendar().get(Calendar.YEAR);
    details.totalSuccessRecords = 2;
    details.totalTasks = 3;

    // insert record in curam test server
    BDMInterfaceSummaryFactory.newInstance().insert(details);

    // initialize keys in order to find the interfaceSummaryDtlsList from the DB
    final BDMCRADeathMatchSearchKey craDeathMatchSearchKey =
      new BDMCRADeathMatchSearchKey();

    craDeathMatchSearchKey.interfaceNameCode = interfaceName;
    craDeathMatchSearchKey.year = date.getCalendar().get(Calendar.YEAR);

    final BDMInterfaceSummaryKey interfaceSummaryObjKey =
      new BDMInterfaceSummaryKey();

    interfaceSummaryObjKey.interfaceSummaryID = interfaceID;

    // read from table before updating
    final BDMInterfaceSummaryDtls interfaceSummaryObjdtls =
      BDMInterfaceSummaryFactory.newInstance().read(interfaceSummaryObjKey);

    final BDMInterfaceSummary interfaceSummaryObj =
      BDMInterfaceSummaryFactory.newInstance();

    final BDMInterfaceSummaryDtlsList interfaceSummaryDtlsList =
      interfaceSummaryObj.searchByNameCodeAndYear(craDeathMatchSearchKey);

    craHelper.createSummaryData(interfaceName, recordStatus, date,
      interfaceID);

    // read from table after updating
    final BDMInterfaceSummaryDtls interfaceSummaryDtls =
      BDMInterfaceSummaryFactory.newInstance().read(interfaceSummaryObjKey);

    // change numbers for variables
    assertEquals(interfaceSummaryDtls.interfaceNameCode, interfaceName);
    assertEquals(interfaceSummaryDtls.totalRecords,
      interfaceSummaryDtlsList.dtls.size() + 1);
    assertEquals(interfaceSummaryDtls.year,
      date.getCalendar().get(Calendar.YEAR));
    assertEquals(interfaceSummaryDtls.totalTasks,
      interfaceSummaryObjdtls.totalTasks + 1);
    assertEquals(interfaceSummaryDtls.totalSuccessRecords,
      interfaceSummaryObjdtls.totalSuccessRecords);

  }

  /**
   * Test createSummaryData with existing records and the record status input
   * being false. Read the local server after to see to see the if it got updated
   * properly
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void createSummaryData_withRecords_trueRecordStatus()
    throws AppException, InformationalException {

    // generate unique ID
    final UniqueID uniqueID = UniqueIDFactory.newInstance();

    final String interfaceName = BDMInterfaceNameCode.CRA_DEATH_MATCH;
    final Date date = Date.getDate(BDMDateUtil.dateFormatter("1991-02-02"));
    final boolean recordStatus = true;
    final long interfaceID = uniqueID.getNextID();

    // create record
    final BDMInterfaceSummaryDtls details = new BDMInterfaceSummaryDtls();
    details.interfaceSummaryID = interfaceID;
    details.totalRecords = 1;
    details.interfaceNameCode = BDMInterfaceNameCode.CRA_DEATH_MATCH;
    details.year = date.getCalendar().get(Calendar.YEAR);
    details.totalSuccessRecords = 2;
    details.totalTasks = 3;

    // insert record in curam test server
    BDMInterfaceSummaryFactory.newInstance().insert(details);

    // initialize keys in order to find the interfaceSummaryDtlsList from the DB
    final BDMCRADeathMatchSearchKey craDeathMatchSearchKey =
      new BDMCRADeathMatchSearchKey();

    craDeathMatchSearchKey.interfaceNameCode = interfaceName;
    craDeathMatchSearchKey.year = date.getCalendar().get(Calendar.YEAR);

    final BDMInterfaceSummaryKey interfaceSummaryObjKey =
      new BDMInterfaceSummaryKey();

    interfaceSummaryObjKey.interfaceSummaryID = interfaceID;

    // read from table before updating
    final BDMInterfaceSummaryDtls interfaceSummaryObjdtls =
      BDMInterfaceSummaryFactory.newInstance().read(interfaceSummaryObjKey);

    final BDMInterfaceSummary interfaceSummaryObj =
      BDMInterfaceSummaryFactory.newInstance();

    final BDMInterfaceSummaryDtlsList interfaceSummaryDtlsList =
      interfaceSummaryObj.searchByNameCodeAndYear(craDeathMatchSearchKey);

    craHelper.createSummaryData(interfaceName, recordStatus, date,
      interfaceID);

    // read from table after updating
    final BDMInterfaceSummaryDtls interfaceSummaryDtls =
      BDMInterfaceSummaryFactory.newInstance().read(interfaceSummaryObjKey);

    // change numbers for variables
    assertEquals(interfaceSummaryDtls.interfaceNameCode, interfaceName);
    assertEquals(interfaceSummaryDtls.totalRecords,
      interfaceSummaryDtlsList.dtls.size() + 1);
    assertEquals(interfaceSummaryDtls.year,
      date.getCalendar().get(Calendar.YEAR));
    assertEquals(interfaceSummaryDtls.totalTasks,
      interfaceSummaryObjdtls.totalTasks);
    assertEquals(interfaceSummaryDtls.totalSuccessRecords,
      interfaceSummaryObjdtls.totalSuccessRecords + 1);

  }

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

  @Test
  public void errorTypeHandler_success() throws Exception {

    final String constantName = BDMConstants.kSIN;

    final AppException exception = assertThrows(AppException.class,
      () -> craHelper.errorTypeHandler(constantName));

    final AppException appException =
      new AppException(BDMBPOCCT.ERR_HTTP_404_RESOURCE_NOT_FOUND);
    appException.arg(BDMConstants.kSIN);

    assertEquals(exception.getLocalizedMessage(), appException.getMessage());
  }

}
