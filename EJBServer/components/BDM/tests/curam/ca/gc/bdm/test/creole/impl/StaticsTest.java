package curam.ca.gc.bdm.test.creole.impl;

import curam.ca.gc.bdm.codetable.BDMPHONECOUNTRY;
import curam.ca.gc.bdm.creole.impl.Statics;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.PHONETYPE;
import curam.creole.execution.session.RecalculationsProhibited;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.execution.session.StronglyTypedRuleObjectFactory;
import curam.creole.ruleclass.PDCPhoneNumberRuleSet.impl.PDCPhoneNumber;
import curam.creole.storage.inmemory.InMemoryDataStorage;
import curam.creole.value.CodeTableItem;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.tools.configuration.base.MethodRef;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/*
 * These are tests for the Static methods the rules sets use.
 */
@RunWith(JMockit.class)
public class StaticsTest extends CuramServerTestJUnit4 {

  @MethodRef(name = "validateEmailAddress",
    signature = "(QSession;QString;)QBoolean;")
  @Test
  public void testValidateEmailAddress() throws Exception {

    final Session session =
      Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
        new InMemoryDataStorage(new StronglyTypedRuleObjectFactory()));
    String emailAddress = "";
    Boolean result;

    // test 1 with correct email address
    emailAddress = "testing@testemail.com";
    result = Statics.validateEmailAddress(session, emailAddress);
    Assert.assertEquals(true, result);

    // test 2 with invalid email address
    emailAddress = "thisIsNotAnEmailAddress";
    result = Statics.validateEmailAddress(session, emailAddress);
    Assert.assertEquals(false, result);
  }

  @Test
  public void testvalidateAreaCode_CA_US_validAreaCode() throws Exception {

    final Session session =
      Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
        new InMemoryDataStorage(new StronglyTypedRuleObjectFactory()));

    final PDCPhoneNumber activeEvidenceRecord =
      curam.creole.ruleclass.PDCPhoneNumberRuleSet.impl.PDCPhoneNumber_Factory
        .getFactory().newInstance(session);
    activeEvidenceRecord.phoneType().specifyValue(
      new CodeTableItem(PHONETYPE.TABLENAME, PHONETYPE.PERSONAL_LANDLINE));
    activeEvidenceRecord.phoneCountryCode().specifyValue(
      new CodeTableItem(BDMPHONECOUNTRY.TABLENAME, BDMPHONECOUNTRY.CANADA));
    activeEvidenceRecord.phoneAreaCode().specifyValue("333");

    final boolean restrictOperation =
      Statics.validateAreaCode_CA_US(session, activeEvidenceRecord);

    assertTrue(restrictOperation == true);
  }

  @Test
  public void testValidateAreaCode_CA_US1_invalidAreaCode() throws Exception {

    final Session session =
      Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
        new InMemoryDataStorage(new StronglyTypedRuleObjectFactory()));
    final PDCPhoneNumber activeEvidenceRecord =
      curam.creole.ruleclass.PDCPhoneNumberRuleSet.impl.PDCPhoneNumber_Factory
        .getFactory().newInstance(session);
    activeEvidenceRecord.phoneAreaCode().specifyValue("Alpha123");

    // returns true if the area code is invalid.
    final boolean isInvalid =
      Statics.validateAreaCode_CA_US1(session, activeEvidenceRecord);

    assertEquals(true, isInvalid);

  }

  @Test
  public void testValidateAreaCode_CA_US1_validAreaCode() throws Exception {

    final Session session =
      Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
        new InMemoryDataStorage(new StronglyTypedRuleObjectFactory()));
    final PDCPhoneNumber activeEvidenceRecord =
      curam.creole.ruleclass.PDCPhoneNumberRuleSet.impl.PDCPhoneNumber_Factory
        .getFactory().newInstance(session);
    activeEvidenceRecord.phoneAreaCode().specifyValue("819");

    // returns false if the area code is valid.
    final boolean isValid =
      Statics.validateAreaCode_CA_US1(session, activeEvidenceRecord);

    assertEquals(false, isValid);

  }

  @Test
  public void testValidatePhoneNumber_CA_US1_validPhoneNumber()
    throws Exception {

    final Session session =
      Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
        new InMemoryDataStorage(new StronglyTypedRuleObjectFactory()));
    final PDCPhoneNumber activeEvidenceRecord =
      curam.creole.ruleclass.PDCPhoneNumberRuleSet.impl.PDCPhoneNumber_Factory
        .getFactory().newInstance(session);
    activeEvidenceRecord.phoneNumber().specifyValue("3339898");

    // returns false if the phone number is valid.
    final boolean flag =
      Statics.validatePhoneNumber_CA_US1(session, activeEvidenceRecord);

    assertEquals(false, flag);

  }

  @Test
  public void testValidatePhoneNumber_CA_US1_InvalidPhoneNumber()
    throws Exception {

    final Session session =
      Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
        new InMemoryDataStorage(new StronglyTypedRuleObjectFactory()));
    final PDCPhoneNumber activeEvidenceRecord =
      curam.creole.ruleclass.PDCPhoneNumberRuleSet.impl.PDCPhoneNumber_Factory
        .getFactory().newInstance(session);
    activeEvidenceRecord.phoneNumber().specifyValue("33392898");

    // returns true if the phone number is invalid.
    final boolean flag =
      Statics.validatePhoneNumber_CA_US1(session, activeEvidenceRecord);

    assertEquals(true, flag);

  }

  @Test
  public void testvalidatePhoneNumberOther1_validPhoneNumber() {

    // tests for valid phone number and area code
    final Session session =
      Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
        new InMemoryDataStorage(new StronglyTypedRuleObjectFactory()));
    final PDCPhoneNumber activeEvidenceRecord =
      curam.creole.ruleclass.PDCPhoneNumberRuleSet.impl.PDCPhoneNumber_Factory
        .getFactory().newInstance(session);

    activeEvidenceRecord.phoneNumber().specifyValue("1234567");
    activeEvidenceRecord.phoneAreaCode().specifyValue("998");
    activeEvidenceRecord.phoneCountryCode().specifyValue(
      new CodeTableItem(BDMPHONECOUNTRY.TABLENAME, BDMPHONECOUNTRY.BELARUS));

    final boolean flag =
      Statics.validatePhoneNumberOther1(session, activeEvidenceRecord);

    assertEquals(false, flag);
  }

  @Test
  public void testvalidatePhoneNumberOther1_invalidPhoneNumber() {

    final Session session =
      Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
        new InMemoryDataStorage(new StronglyTypedRuleObjectFactory()));
    final PDCPhoneNumber activeEvidenceRecord =
      curam.creole.ruleclass.PDCPhoneNumberRuleSet.impl.PDCPhoneNumber_Factory
        .getFactory().newInstance(session);
    // invalid phone number
    activeEvidenceRecord.phoneNumber().specifyValue("123d4567a");
    activeEvidenceRecord.phoneAreaCode().specifyValue("9981");
    activeEvidenceRecord.phoneCountryCode().specifyValue(
      new CodeTableItem(BDMPHONECOUNTRY.TABLENAME, BDMPHONECOUNTRY.BELARUS));

    final boolean flag =
      Statics.validatePhoneNumberOther1(session, activeEvidenceRecord);

    assertEquals(true, flag);
  }

  @Test
  public void testvalidatePhoneNumberOther1_invalidAreaCode() {

    final Session session =
      Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
        new InMemoryDataStorage(new StronglyTypedRuleObjectFactory()));
    final PDCPhoneNumber activeEvidenceRecord =
      curam.creole.ruleclass.PDCPhoneNumberRuleSet.impl.PDCPhoneNumber_Factory
        .getFactory().newInstance(session);
    // invalid area code
    activeEvidenceRecord.phoneNumber().specifyValue("123434");
    activeEvidenceRecord.phoneAreaCode().specifyValue("9asd981");
    activeEvidenceRecord.phoneCountryCode().specifyValue(
      new CodeTableItem(BDMPHONECOUNTRY.TABLENAME, BDMPHONECOUNTRY.BELARUS));

    final boolean flag =
      Statics.validatePhoneNumberOther1(session, activeEvidenceRecord);

    assertEquals(true, flag);
  }

  @Test
  public void testvalidateAreaCode_CA_US_invalidAreaCode() throws Exception {

    final Session session =
      Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
        new InMemoryDataStorage(new StronglyTypedRuleObjectFactory()));

    final PDCPhoneNumber activeEvidenceRecord =
      curam.creole.ruleclass.PDCPhoneNumberRuleSet.impl.PDCPhoneNumber_Factory
        .getFactory().newInstance(session);
    activeEvidenceRecord.phoneType().specifyValue(
      new CodeTableItem(PHONETYPE.TABLENAME, PHONETYPE.PERSONAL_LANDLINE));
    activeEvidenceRecord.phoneCountryCode().specifyValue(
      new CodeTableItem(BDMPHONECOUNTRY.TABLENAME, BDMPHONECOUNTRY.CANADA));
    activeEvidenceRecord.phoneAreaCode().specifyValue("4444");

    final boolean restrictOperation =
      Statics.validateAreaCode_CA_US(session, activeEvidenceRecord);

    assertTrue(restrictOperation == false);
  }

  @Test
  public void testValidatePhoneNumber_CA_US_validPhoneNumber()
    throws Exception {

    final Session session =
      Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
        new InMemoryDataStorage(new StronglyTypedRuleObjectFactory()));

    final PDCPhoneNumber activeEvidenceRecord =
      curam.creole.ruleclass.PDCPhoneNumberRuleSet.impl.PDCPhoneNumber_Factory
        .getFactory().newInstance(session);
    activeEvidenceRecord.phoneType().specifyValue(
      new CodeTableItem(PHONETYPE.TABLENAME, PHONETYPE.PERSONAL_LANDLINE));
    activeEvidenceRecord.phoneCountryCode().specifyValue(
      new CodeTableItem(BDMPHONECOUNTRY.TABLENAME, BDMPHONECOUNTRY.USA));
    activeEvidenceRecord.phoneNumber().specifyValue("3333337");

    final boolean restrictOperation =
      Statics.validatePhoneNumber_CA_US(session, activeEvidenceRecord);

    assertTrue(restrictOperation == true);
  }

  @Test
  public void testValidatePhoneNumber_CA_US_invalidPhoneNumber()
    throws Exception {

    final Session session =
      Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
        new InMemoryDataStorage(new StronglyTypedRuleObjectFactory()));

    final PDCPhoneNumber activeEvidenceRecord =
      curam.creole.ruleclass.PDCPhoneNumberRuleSet.impl.PDCPhoneNumber_Factory
        .getFactory().newInstance(session);
    activeEvidenceRecord.phoneType().specifyValue(
      new CodeTableItem(PHONETYPE.TABLENAME, PHONETYPE.PERSONAL_LANDLINE));
    activeEvidenceRecord.phoneCountryCode().specifyValue(
      new CodeTableItem(BDMPHONECOUNTRY.TABLENAME, BDMPHONECOUNTRY.USA));
    activeEvidenceRecord.phoneNumber().specifyValue("33333378");

    final boolean restrictOperation =
      Statics.validatePhoneNumber_CA_US(session, activeEvidenceRecord);
    assertTrue(restrictOperation == false);
  }

  @Test
  public void testWhenMatchesThreeDigitsNumber_valid() {

    assertTrue(Statics.whenMatchesThreeDigitNumber("333"));

  }

  @Test
  public void testWhenMatchesThreeDigitsNumber_invalid() {

    // length 4 and aplha numeric
    assertFalse(Statics.whenMatchesThreeDigitNumber("a4aa"));

    // length=3 and alpha numeric
    assertFalse(Statics.whenMatchesThreeDigitNumber("a4a"));

    // length=3 and alpha
    assertFalse(Statics.whenMatchesThreeDigitNumber("aaa"));
    // length=4 and alpha
    assertFalse(Statics.whenMatchesThreeDigitNumber("4444"));

    // length=4 and alpha
    assertFalse(Statics.whenMatchesThreeDigitNumber("aaa444aa"));

    assertFalse(Statics.whenMatchesThreeDigitNumber("aaa 444 aa"));

  }

  @Test
  public void testWhenMatchesSevenDigitNumber_invalid() {

    // length 7 and aplha
    assertFalse(Statics.whenMatchesSevenDigitNumber("abcdefg"));

    // length=7 and alpha numeric
    assertFalse(Statics.whenMatchesSevenDigitNumber("123defg"));

    // length > 7 and alpha
    assertFalse(Statics.whenMatchesSevenDigitNumber("abcdefg1111111"));
    // length>7 and alpha
    assertFalse(Statics.whenMatchesSevenDigitNumber("1111111abcdefg"));

    // alphanumeric
    assertFalse(Statics.whenMatchesSevenDigitNumber("aaa7777777aa"));

  }

  @Test
  public void testisValidNumber_valid() throws Exception {

    final Session session =
      Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
        new InMemoryDataStorage(new StronglyTypedRuleObjectFactory()));

    // ALL NUMBERS
    assertTrue(!Statics.isValidNumber(session, "123456789"));

  }

  @Test
  public void testisValidNumber_invalid() throws Exception {

    final Session session =
      Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
        new InMemoryDataStorage(new StronglyTypedRuleObjectFactory()));

    final String phoneNumber = "123a456789";
    final boolean restrictOperation =
      Statics.isValidNumber(session, phoneNumber);
    assertTrue(restrictOperation == true);
  }

  @Test
  public void testIsValidEmail_Invalid() throws Exception {

    final Session session =
      Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
        new InMemoryDataStorage(new StronglyTypedRuleObjectFactory()));

    final List<String> invalidEmails = new ArrayList<String>();
    // empty string
    invalidEmails.add("");
    // no values before or after @
    invalidEmails.add("@");
    // no values before @
    invalidEmails.add("@gmail.com");
    // personal_info cannot begin or end with period
    invalidEmails.add(".@gmail.com");
    invalidEmails.add("test.234.@gmail.com");
    invalidEmails.add(".test@gmail.com");
    // email must have domain
    invalidEmails.add("test@");
    // domain mist have at least 1 period
    invalidEmails.add("test@gmail");
    // can't have 2 periods in a row
    invalidEmails.add("test..234@gmail.com");
    // domain cannot start wth period
    invalidEmails.add("test@.gmail.com");
    // top level domain must be between 2-6 characters
    invalidEmails.add("test@gmail.c");
    invalidEmails.add("test@gmail.comcomc");
    // cannot have invalid characters
    invalidEmails.add("test[]123@gmail.com");
    invalidEmails.add("test@gmail[].com");

    for (final String email : invalidEmails) {
      final boolean isValid = Statics.validateEmailAddress(session, email);
      final String errorMessage = "Email " + email + " is valid.";
      assertFalse(errorMessage, isValid);
    }

  }

  @Test
  public void testIsValidEmail_Valid() throws Exception {

    final Session session =
      Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
        new InMemoryDataStorage(new StronglyTypedRuleObjectFactory()));

    final List<String> validEmails = new ArrayList<String>();
    validEmails.add("test@gmail.com");
    // valid characters
    validEmails.add("!#$%^&*{}|`~/@gmail.c--.123.com");
    validEmails.add("ABCabc1234567890@mysite.site.ca");
    validEmails.add("test.123.abc@gmail.com");

    for (final String email : validEmails) {
      final boolean isValid = Statics.validateEmailAddress(session, email);
      final String errorMessage = "Email " + email + " is invalid.";
      assertTrue(errorMessage, isValid);
    }
  }

  /**
   * This is a test for the getIncomeStartDay method which makes sure the
   * correct start date is calculated from the income evidence.
   *
   * @throws Exception
   */
  @Test
  public void testGetIncomeStartDate() throws Exception {

    final Session session =
      Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
        new InMemoryDataStorage(new StronglyTypedRuleObjectFactory()));

    final List<String> validStartDates = new ArrayList<String>();
    final List<String> validExpectedStartDates = new ArrayList<String>();

    // valid income start dates
    for (int year = 2025; year < 2030; year++) {
      validStartDates.add(Integer.toString(year));
    }

    // expected start dates (the next year of the inputted year)
    for (int expectedyear = 2026; expectedyear < 2031; expectedyear++) {
      validExpectedStartDates.add(Integer.toString(expectedyear) + "0101");
    }

    // Assert
    for (int i = 0; i < validStartDates.size(); i++) {
      final Date isValid =
        Statics.getIncomeStartDate(session, validStartDates.get(i));
      final Date expectedDate =
        Date.fromISO8601(validExpectedStartDates.get(i));
      assertEquals(expectedDate, isValid);
    }
  }

  /**
   * This is a test for the getIncomeEndDate method which makes sure the
   * correct end date is calculated from the income evidence.
   *
   * @throws Exception
   */
  @Test
  public void testGetIncomeEndDate() throws Exception {

    final Session session =
      Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
        new InMemoryDataStorage(new StronglyTypedRuleObjectFactory()));

    final List<String> validEndDates = new ArrayList<String>();
    final List<String> validExpectedEndDates = new ArrayList<String>();

    // valid income start dates
    for (int year = 2025; year < 2030; year++) {
      validEndDates.add(Integer.toString(year));
    }

    // expected start dates (the next year of the inputted year)
    for (int expectedyear = 2026; expectedyear < 2031; expectedyear++) {
      validExpectedEndDates.add(Integer.toString(expectedyear) + "1231");
    }

    // Assert
    for (int i = 0; i < validEndDates.size(); i++) {
      final Date isValid =
        Statics.getIncomeEndDate(session, validEndDates.get(i));
      final Date expectedDate =
        Date.fromISO8601(validExpectedEndDates.get(i));
      assertEquals(expectedDate, isValid);
    }
  }

  /**
   * This is a test for the isSINNumberStartsWith9Series method which checks if
   * SIN given is starting with the number 9 or not
   */
  @Test
  public void testIsSINNumberStartsWith9Series() {

    final Session session =
      Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
        new InMemoryDataStorage(new StronglyTypedRuleObjectFactory()));

    // Set up
    final String validSIN = "123456789";
    final String invalidSin = "987654321";
    final String noSIN = "";
    final String nullSIN = null;

    // Assert
    assertFalse(Statics.isSINNumberStartsWith9Series(session, validSIN));
    assertTrue(Statics.isSINNumberStartsWith9Series(session, invalidSin));
    assertFalse(Statics.isSINNumberStartsWith9Series(session, noSIN));
    assertFalse(Statics.isSINNumberStartsWith9Series(session, nullSIN));

  }

  /**
   * This is a test for the getPersonAgeAsOnGivenDate method which caluculates
   * the age of a person on a given date.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testGetPersonAgeAsOnGivenDate()
    throws AppException, InformationalException {

    final Session session =
      Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
        new InMemoryDataStorage(new StronglyTypedRuleObjectFactory()));

    // Set up
    final Number concernRole = 1234568789;
    final Date dateOfBirth = Date.fromISO8601("20220101");
    final Date givenDate = Date.getCurrentDate();

    final Number expectedAge = givenDate.getCalendar().get(Calendar.YEAR)
      - dateOfBirth.getCalendar().get(Calendar.YEAR);

    // Add 1 extra year for invalid age
    final Number notExpecetedAge = givenDate.getCalendar().get(Calendar.YEAR)
      - dateOfBirth.getCalendar().get(Calendar.YEAR) + 1;

    // Assert
    assertEquals(expectedAge, Statics.getPersonAgeAsOnGivenDate(session,
      concernRole, dateOfBirth, givenDate));

    assertNotEquals(notExpecetedAge, Statics.getPersonAgeAsOnGivenDate(
      session, concernRole, dateOfBirth, givenDate));

  }

}
