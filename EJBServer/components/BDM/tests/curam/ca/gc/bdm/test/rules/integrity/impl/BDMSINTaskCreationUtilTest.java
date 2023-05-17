package curam.ca.gc.bdm.test.rules.integrity.impl;

import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.util.integrity.impl.BDMSINIntegrityCheckConstants;
import curam.ca.gc.bdm.util.integrity.impl.BDMSINIntegrityTaskCreationUtil;
import curam.core.fact.PersonRegistrationFactory;
import curam.core.struct.PersonRegistrationDetails;
import curam.core.struct.RegistrationIDDetails;
import curam.util.persistence.GuiceWrapper;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertTrue;

/**
 * JUnit for Bank Account Integrity Rules.
 *
 *
 */
@RunWith(JMockit.class)
public class BDMSINTaskCreationUtilTest extends CuramServerTestJUnit4 {

  public BDMSINTaskCreationUtilTest() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Test
  public void testSINIntegrityTaskCreation_01() throws Exception {

    try {
      // register person
      final RegisterPerson registerPersonObj = new RegisterPerson(getName());

      final PersonRegistrationDetails personRegistrationDetails =
        registerPersonObj.getPersonRegistrationDetails();
      final RegistrationIDDetails registrationIDDetails =
        PersonRegistrationFactory.newInstance()
          .registerPerson(personRegistrationDetails);
      BDMSINIntegrityTaskCreationUtil.createTaskForSINIntegrity(
        registrationIDDetails.concernRoleID,
        BDMSINIntegrityCheckConstants.SIN_TASKTYPE_INTEGRITY);
      assertTrue(true);
    } catch (final Exception e) {
      // fail test if exception occurred
      assertTrue(false);
    }
  }

  @Test
  public void testSINIntegrityTaskCreation_02() throws Exception {

    try {
      // register person
      final RegisterPerson registerPersonObj = new RegisterPerson(getName());

      final PersonRegistrationDetails personRegistrationDetails =
        registerPersonObj.getPersonRegistrationDetails();
      final RegistrationIDDetails registrationIDDetails =
        PersonRegistrationFactory.newInstance()
          .registerPerson(personRegistrationDetails);
      BDMSINIntegrityTaskCreationUtil.createTaskForSINIntegrity(
        registrationIDDetails.concernRoleID,
        BDMSINIntegrityCheckConstants.SIN_TASKTYPE_MISMATCH);
      assertTrue(true);
    } catch (final Exception e) {
      // fail test if exception occurred
      assertTrue(false);
    }
  }

  @Test
  public void testSINIntegrityTaskCreation_03() throws Exception {

    try {
      // register person
      final RegisterPerson registerPersonObj = new RegisterPerson(getName());

      final PersonRegistrationDetails personRegistrationDetails =
        registerPersonObj.getPersonRegistrationDetails();
      final RegistrationIDDetails registrationIDDetails =
        PersonRegistrationFactory.newInstance()
          .registerPerson(personRegistrationDetails);
      BDMSINIntegrityTaskCreationUtil.createTaskForSINIntegrity(
        registrationIDDetails.concernRoleID,
        BDMSINIntegrityCheckConstants.SIN_TASKTYPE_INVESTIGATION);
      assertTrue(true);
    } catch (final Exception e) {
      // fail test if exception occurred
      assertTrue(false);
    }
  }

  @Test
  public void testSINIntegrityTaskCreation_04() throws Exception {

    try {
      // register person
      final RegisterPerson registerPersonObj = new RegisterPerson(getName());
      final PersonRegistrationDetails personRegistrationDetails =
        registerPersonObj.getPersonRegistrationDetails();
      final RegistrationIDDetails registrationIDDetails =
        PersonRegistrationFactory.newInstance()
          .registerPerson(personRegistrationDetails);
      BDMSINIntegrityTaskCreationUtil.createTaskForSINIntegrity(
        registrationIDDetails.concernRoleID,
        BDMSINIntegrityCheckConstants.SIN_TASKTYPE_INVESTIGATION);
      assertTrue(true);
      BDMSINIntegrityTaskCreationUtil.createTaskForSINIntegrity(
        registrationIDDetails.concernRoleID,
        BDMSINIntegrityCheckConstants.SIN_TASKTYPE_MISMATCH);
      assertTrue(true);
      BDMSINIntegrityTaskCreationUtil.createTaskForSINIntegrity(
        registrationIDDetails.concernRoleID,
        BDMSINIntegrityCheckConstants.SIN_TASKTYPE_INTEGRITY);
      assertTrue(true);
    } catch (final Exception e) {
      // fail test if exception occurred
      assertTrue(false);
    }
  }

  @Test
  public void testSINIntegrityTaskCreation_05() throws Exception {

    try {
      // register person
      final RegisterPerson registerPersonObj = new RegisterPerson(getName());
      final PersonRegistrationDetails personRegistrationDetails =
        registerPersonObj.getPersonRegistrationDetails();
      final RegistrationIDDetails registrationIDDetails =
        PersonRegistrationFactory.newInstance()
          .registerPerson(personRegistrationDetails);
      BDMSINIntegrityTaskCreationUtil.createTaskForSINIntegrity(
        registrationIDDetails.concernRoleID,
        BDMSINIntegrityCheckConstants.SIN_TASKTYPE_INVESTIGATION);
      assertTrue(true);
      BDMSINIntegrityTaskCreationUtil.createTaskForSINIntegrity(
        registrationIDDetails.concernRoleID,
        BDMSINIntegrityCheckConstants.SIN_TASKTYPE_INVESTIGATION);
      assertTrue(true);
    } catch (final Exception e) {
      // fail test if exception occurred
      assertTrue(false);
    }
  }
}
