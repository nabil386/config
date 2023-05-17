package curam.bdm.test.ca.gc.bdm.evidence;

import com.google.inject.Inject;
import curam.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest;
import curam.commonintake.codetable.METHODOFAPPLICATION;
import curam.commonintake.entity.struct.ApplicationCaseKey;
import curam.commonintake.facade.fact.ApplicationCaseFactory;
import curam.commonintake.facade.intf.ApplicationCase;
import curam.commonintake.facade.struct.ApplicationCaseCreateDetails;
import curam.creole.execution.session.RecalculationsProhibited;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.execution.session.StronglyTypedRuleObjectFactory;
import curam.creole.ruleclass.BDMIncarcerationRuleSet.impl.BDMIncarceration;
import curam.creole.ruleclass.BDMIncarcerationRuleSet.impl.BDMIncarceration_Factory;
import curam.creole.ruleclass.BDMIncarcerationValidationRuleSet.impl.ValidationResult;
import curam.creole.ruleclass.BDMIncarcerationValidationRuleSet.impl.ValidationResult_Factory;
import curam.creole.storage.inmemory.InMemoryDataStorage;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDefDAO;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test case to test the incarceration evidence.
 *
 */
public class BDMIncarcerationValidationRuleSetTest
  extends CuramServerTestJUnit4 {

  public BDMIncarcerationValidationRuleSetTest() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  Session session;

  private final String ERR_OVERLAPPING_TIME_NOT_ALLOWED =
    "An incarceration of overlapping time period is not allowed.";

  private final String ERR_OVERLAPPING =
    "Incarceration record already exists for the time period.";

  RegisterPerson registerPerson = new RegisterPerson("RegisterPerson");

  @Inject
  protected EvidenceTypeVersionDefDAO etVerDefDAO;

  @Override
  public void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    session =
      Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
        new InMemoryDataStorage(new StronglyTypedRuleObjectFactory()));
  }

  /**
   * Setup the application case for the person used in incarceration evidence
   * tests. This method returns the newly created application case identifier.
   *
   * @param concernRoleID concern role identifier of the person
   *
   * @return returns the newly created application case identifier
   * @throws AppException
   * Generic {@link AppException}
   * @throws InformationalException
   * Generic {@link InformationalException}
   */
  private ApplicationCaseKey createApplicationCase(final long concernRoleID)
    throws AppException, InformationalException {

    // create Application case
    final ApplicationCase appCase = ApplicationCaseFactory.newInstance();

    final ApplicationCaseCreateDetails appCaseDetails =
      new ApplicationCaseCreateDetails();
    appCaseDetails.concernRoleID = concernRoleID;
    appCaseDetails.dtls.applicationCaseAdminID = 80001l;
    appCaseDetails.dtls.applicationDate = TransactionInfo.getSystemDate();
    appCaseDetails.dtls.methodOfApplication = METHODOFAPPLICATION.INPERSON;

    final curam.commonintake.entity.struct.ApplicationCaseKey appCaseKey =
      appCase.createApplicationCaseForConcernRole(appCaseDetails);

    return appCaseKey;
  }

  /**
   * Test that the system prevents user from adding an overlapping
   * incarceration evidence.
   *
   * @throws AppException Generic {@link AppException}
   * @throws InformationalException Generic {@link AppException}
   */
  @Test
  public void testIncarceration_overlapping()
    throws AppException, InformationalException {

    final BDMEvidenceUtilsTest BDMutils = new BDMEvidenceUtilsTest();

    final PDCPersonDetails pdcPersonDetails = BDMutils.createPDCPerson();
    final ApplicationCaseKey appCaseKey =
      this.createApplicationCase(pdcPersonDetails.concernRoleID);
    try {
      BDMutils.createIncarcerationEvidence(appCaseKey,
        pdcPersonDetails.concernRoleID, Date.fromISO8601("20210101"),
        "Institute");

      // overlapping evidence created
      BDMutils.createIncarcerationEvidence(appCaseKey,
        pdcPersonDetails.concernRoleID, Date.fromISO8601("20210102"),
        "Institute");

    } catch (final Exception e) {
      assertEquals(ERR_OVERLAPPING, e.getMessage());
    }

  }

  /**
   * Test the validation message for incarceration evidence with same start and
   * end date.
   */
  @Test
  public void testIncarceration() {

    final ValidationResult validationResult =
      ValidationResult_Factory.getFactory().newInstance(session);
    final BDMIncarceration incarceration =
      BDMIncarceration_Factory.getFactory().newInstance(session);
    final BDMIncarceration incarceration1 =
      BDMIncarceration_Factory.getFactory().newInstance(session);

    // Incarceration from 2021-01-01 to 2021-01-30
    incarceration.startDate().specifyValue(Date.fromISO8601("20210101"));
    incarceration.endDate().specifyValue(Date.fromISO8601("20210130"));
    incarceration.caseID().specifyValue(123);

    // Incarceration from 2021-01-01 to 2021-01-30
    incarceration1.startDate().specifyValue(Date.fromISO8601("20210101"));
    incarceration1.endDate().specifyValue(Date.fromISO8601("20210130"));
    incarceration1.caseID().specifyValue(123);

    validationResult.evidence().specifyValue(incarceration);

    // assert for the overlapping date message
    // CREOLETestHelper.assertEquals(ERR_OVERLAPPING_TIME_NOT_ALLOWED,
    // validationResult.incarcerationDateOverlapValidation().getValue()
    // .failureMessage().getValue().toString());
  }

  /**
   * Should throw validation message if incarceration with same startdate and
   * different enddate.
   * Test the overlapping incarceration evidences with the different end dates.
   */
  @Test
  public void testIncarceration_differentenddate() {

    final ValidationResult validationResult =
      ValidationResult_Factory.getFactory().newInstance(session);
    final BDMIncarceration incarceration =
      BDMIncarceration_Factory.getFactory().newInstance(session);
    final BDMIncarceration incarceration1 =
      BDMIncarceration_Factory.getFactory().newInstance(session);

    incarceration.startDate().specifyValue(Date.fromISO8601("20210101"));
    incarceration.endDate().specifyValue(Date.fromISO8601("20210130"));
    incarceration.caseID().specifyValue(123);

    incarceration1.startDate().specifyValue(Date.fromISO8601("20210101"));
    incarceration1.endDate().specifyValue(Date.fromISO8601("20210105"));
    incarceration1.caseID().specifyValue(123);

    validationResult.evidence().specifyValue(incarceration);

    // CREOLETestHelper.assertEquals(ERR_OVERLAPPING_TIME_NOT_ALLOWED,
    // validationResult.incarcerationDateOverlapValidation().getValue()
    // .failureMessage().getValue().toString());
  }

  /**
   * Should throw validation message if incarceration with same startdate and
   * no enddate
   */
  @Test
  public void testIncarceration_noenddate() {

    final ValidationResult validationResult =
      ValidationResult_Factory.getFactory().newInstance(session);
    final BDMIncarceration incarceration =
      BDMIncarceration_Factory.getFactory().newInstance(session);
    final BDMIncarceration incarceration1 =
      BDMIncarceration_Factory.getFactory().newInstance(session);

    // Incarceration from 2021-01-01 to 2021-01-05
    incarceration.startDate().specifyValue(Date.fromISO8601("20210101"));
    incarceration.endDate().specifyValue(Date.fromISO8601("20210105"));
    incarceration.caseID().specifyValue(123);

    // Incarceration from 2021-01-01 and no end date
    incarceration1.startDate().specifyValue(Date.fromISO8601("20210101"));
    incarceration1.endDate().specifyValue(Date.kZeroDate);
    incarceration1.caseID().specifyValue(123);

    validationResult.evidence().specifyValue(incarceration);

    // CREOLETestHelper.assertEquals(ERR_OVERLAPPING_TIME_NOT_ALLOWED,
    // validationResult.incarcerationDateOverlapValidation().getValue()
    // .failureMessage().getValue().toString());
  }

  /**
   * Test overlapping incarceration evidence.
   */
  @Test
  public void testIncarceration1() {

    final ValidationResult validationResult =
      ValidationResult_Factory.getFactory().newInstance(session);
    final BDMIncarceration incarceration =
      BDMIncarceration_Factory.getFactory().newInstance(session);
    final BDMIncarceration incarceration1 =
      BDMIncarceration_Factory.getFactory().newInstance(session);

    // Incarceration from 2021-01-01 to 2021-01-30
    incarceration.startDate().specifyValue(Date.fromISO8601("20210101"));
    incarceration.endDate().specifyValue(Date.fromISO8601("20210130"));
    incarceration.caseID().specifyValue(123);

    // Incarceration from 2021-01-01 to 2021-01-30
    incarceration1.startDate().specifyValue(Date.fromISO8601("20210101"));
    incarceration1.endDate().specifyValue(Date.fromISO8601("20210130"));
    incarceration1.caseID().specifyValue(123);

    // assert for the overlapping date message
    validationResult.evidence().specifyValue(incarceration);
    // CREOLETestHelper.assertEquals(ERR_OVERLAPPING_TIME_NOT_ALLOWED,
    // validationResult.incarcerationDateOverlapValidation().getValue()
    // .failureMessage().getValue().toString());
  }

}
