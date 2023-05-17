package curam.ca.gc.bdm.test.creole.ruleclass.PDCBirthAndDeathValidationRuleSet.impl;

import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.creole.calculator.CREOLETestHelper;
import curam.creole.execution.session.RecalculationsProhibited;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.execution.session.StronglyTypedRuleObjectFactory;
import curam.creole.ruleclass.PDCBirthAndDeathRuleSet.impl.PDCBirthAndDeath;
import curam.creole.ruleclass.PDCBirthAndDeathRuleSet.impl.PDCBirthAndDeath_Factory;
import curam.creole.ruleclass.PDCBirthAndDeathValidationRuleSet.impl.ValidationResult;
import curam.creole.ruleclass.PDCBirthAndDeathValidationRuleSet.impl.ValidationResult_Factory;
import curam.creole.storage.inmemory.InMemoryDataStorage;
import curam.util.type.Date;
import javax.annotation.Generated;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Generated(value = "org.junit-tools-1.1.0")
public class PDCBirthAndDeathValidationRuleSetTest
  extends CuramServerTestJUnit4 {

  Session session;

  private final String ERR_BIRTH_AND_DEATH_PERSON_DOB_BEFORE_TODAY =
    "Date of birth must not be later than today.";

  private final String ERR_BIRTH_LASTNAME_MUST_BE_ENTERED =
    "Birth lastname must be entered";

  private final String ERR_PARENT_LASTNAME_MUST_BE_ENTERED =
    "Parent last name must be entered";

  @Override
  public void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    session =
      Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
        new InMemoryDataStorage(new StronglyTypedRuleObjectFactory()));
  }

  /**
   * Test PDCBirthAndDeath to see if the date of birth is before today.;
   */
  @Test
  public void testBirthAndDeath_DOBBeforeToday() throws Exception {

    final PDCBirthAndDeath birthAndDeath =
      PDCBirthAndDeath_Factory.getFactory().newInstance(session);
    final ValidationResult result =
      ValidationResult_Factory.getFactory().newInstance(session);

    // set date of birth
    birthAndDeath.dateOfBirth().specifyValue(Date.fromISO8601("20020101"));
    // set caseID
    birthAndDeath.caseID().specifyValue(123);

    result.evidence().specifyValue(birthAndDeath);

    CREOLETestHelper.assertEquals(ERR_BIRTH_AND_DEATH_PERSON_DOB_BEFORE_TODAY,
      result.isDateOfBirthBeforeToday().getValue().failureMessage().getValue()
        .toString());

  }

  /**
   * Test PDCBirthAndDeath to see if the date of birth is after today.;
   */
  @Test
  public void testBirthAndDeath_DOBAfterToday() throws Exception {

    final PDCBirthAndDeath birthAndDeath =
      PDCBirthAndDeath_Factory.getFactory().newInstance(session);
    final ValidationResult result =
      ValidationResult_Factory.getFactory().newInstance(session);

    // set date of birth
    birthAndDeath.dateOfBirth().specifyValue(Date.fromISO8601("20260101"));
    // set caseID
    birthAndDeath.caseID().specifyValue(123);

    result.evidence().specifyValue(birthAndDeath);

    CREOLETestHelper.assertEquals(ERR_BIRTH_AND_DEATH_PERSON_DOB_BEFORE_TODAY,
      result.isDateOfBirthBeforeToday().getValue().failureMessage().getValue()
        .toString());

  }

  @Test
  public void testBirthAndDeath_birthLastNameNotBlank() throws Exception {

    final PDCBirthAndDeath birthAndDeath =
      PDCBirthAndDeath_Factory.getFactory().newInstance(session);
    final ValidationResult result =
      ValidationResult_Factory.getFactory().newInstance(session);

    // set date of birth
    birthAndDeath.dateOfBirth().specifyValue(Date.fromISO8601("20020101"));
    // set birth last name
    birthAndDeath.birthLastName().specifyValue("Johnson");
    // set caseID
    birthAndDeath.caseID().specifyValue(123);

    result.evidence().specifyValue(birthAndDeath);

    assertFalse(ERR_BIRTH_LASTNAME_MUST_BE_ENTERED,
      result.isBirthLastNameBlank().getValue());

  }

  @Test
  public void testBirthAndDeath_birthLastNameBlank() throws Exception {

    final PDCBirthAndDeath birthAndDeath =
      PDCBirthAndDeath_Factory.getFactory().newInstance(session);
    final ValidationResult result =
      ValidationResult_Factory.getFactory().newInstance(session);

    // set date of birth
    birthAndDeath.dateOfBirth().specifyValue(Date.fromISO8601("20020101"));
    // set birth last name
    birthAndDeath.birthLastName().specifyValue("");
    // set caseID
    birthAndDeath.caseID().specifyValue(123);

    result.evidence().specifyValue(birthAndDeath);

    assertTrue(ERR_BIRTH_LASTNAME_MUST_BE_ENTERED,
      result.isBirthLastNameBlank().getValue());
  }

  @Test
  public void testBirthAndDeath_parentLastNameNotBlank() throws Exception {

    final PDCBirthAndDeath birthAndDeath =
      PDCBirthAndDeath_Factory.getFactory().newInstance(session);
    final ValidationResult result =
      ValidationResult_Factory.getFactory().newInstance(session);

    // set date of birth
    birthAndDeath.dateOfBirth().specifyValue(Date.fromISO8601("20020101"));
    // set birth last name
    birthAndDeath.mothersBirthLastName().specifyValue("Johnson");
    // set caseID
    birthAndDeath.caseID().specifyValue(123);

    result.evidence().specifyValue(birthAndDeath);

    assertFalse(ERR_PARENT_LASTNAME_MUST_BE_ENTERED,
      result.isParentLastNameBlank().getValue());

  }

  @Test
  public void testBirthAndDeath_parentLastNameBlank() throws Exception {

    final PDCBirthAndDeath birthAndDeath =
      PDCBirthAndDeath_Factory.getFactory().newInstance(session);
    final ValidationResult result =
      ValidationResult_Factory.getFactory().newInstance(session);

    // set date of birth
    birthAndDeath.dateOfBirth().specifyValue(Date.fromISO8601("20020101"));
    // set birth last name
    birthAndDeath.mothersBirthLastName().specifyValue("");
    // set caseID
    birthAndDeath.caseID().specifyValue(123);

    result.evidence().specifyValue(birthAndDeath);

    assertTrue(ERR_PARENT_LASTNAME_MUST_BE_ENTERED,
      result.isParentLastNameBlank().getValue());

  }
}
