package curam.ca.gc.bdmoas.test.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdmoas.application.impl.BDMOASDatastoreConstants;
import curam.codetable.COUNTRY;
import curam.codetable.IEGYESNO;
import curam.codetable.INCOMETYPECODE;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.ieg.impl.IEG2Context;
import curam.ieg.impl.IEGScriptExecution;
import curam.ieg.impl.IEGScriptExecutionFactory;
import curam.rules.functions.CustomFunctionCopyPartnerDetailsTemp;
import curam.rules.functions.IEG2ExecutionContext;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/**
 * The Class tests {@link CustomFunctionSetAddOrUpdateAddressIndicators} custom
 * function.
 */

@RunWith(JMockit.class)
public class CustomFunctionCopyPartnerDetailsTempTest
  extends CustomFunctionTestJunit4 {

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The rulesParameters. */
  @Mocked
  IEG2ExecutionContext ieg2ExecutionContext;

  /** The script execution. */
  @Mocked
  IEGScriptExecution scriptExecution;

  /** The script execution factory. */
  @Mocked
  IEGScriptExecutionFactory scriptExecutionFactory;

  /** The validate function. */
  @Tested
  CustomFunctionCopyPartnerDetailsTemp customFunction;

  Datastore datastore;

  Entity applicationEntity;

  Entity personEntity;

  Entity retirementIncomeEntity;

  Entity foreignIncomeEntity;

  Entity sponsorshipInfoEntity;

  public CustomFunctionCopyPartnerDetailsTempTest() {

    super();
  }

  /**
   * Before each test, init the tested class and other objects.
   */
  @Before
  public void before() throws NoSuchSchemaException {

    customFunction = new CustomFunctionCopyPartnerDetailsTemp();
    ieg2Context = new IEG2Context();

  }

  public void setUpEntities() throws NoSuchSchemaException {

    // Set up entities
    datastore = DatastoreFactory.newInstance()
      .openDatastore("BDMOASGISApplicationSchema");

    applicationEntity =
      datastore.newEntity(BDMDatastoreConstants.APPLICATION);
    datastore.addRootEntity(applicationEntity);

    personEntity = datastore.newEntity(BDMDatastoreConstants.PERSON);
    personEntity.setAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT,
      "true");
    personEntity.setAttribute("localID", "" + 3942034);
    applicationEntity.addChildEntity(personEntity);

    retirementIncomeEntity = datastore.newEntity(
      BDMOASDatastoreConstants.RETIREMENT_PENSION_REDUCTION_ENTITY);
    retirementIncomeEntity.setAttribute(
      BDMOASDatastoreConstants.RETIREMENT_PENSION_REDUCTION_RETIRED_ORRETIRING_ATTR,
      IEGYESNO.YES);
    retirementIncomeEntity.setAttribute(
      BDMOASDatastoreConstants.RETIREMENT_PENSION_REDUCTION_RETIREDATE_ATTR,
      "20210101");
    retirementIncomeEntity.setAttribute(
      BDMOASDatastoreConstants.RETIREMENT_PENSION_REDUCTION_REDUCED_ATTR,
      IEGYESNO.YES);
    retirementIncomeEntity.setAttribute(
      BDMOASDatastoreConstants.RETIREMENT_PENSION_REDUCTION_DATE_ATTR,
      "20210101");
    personEntity.addChildEntity(retirementIncomeEntity);
    retirementIncomeEntity.update();

    foreignIncomeEntity =
      datastore.newEntity(BDMOASDatastoreConstants.FOREIGN_INCOME_ENTITY);
    foreignIncomeEntity.setAttribute(
      BDMOASDatastoreConstants.FOREIGN_INCOME_ISRECEIVING_ATTR, IEGYESNO.YES);
    foreignIncomeEntity.setAttribute(
      BDMOASDatastoreConstants.FOREIGN_INCOME_TYPE_ATTR,
      INCOMETYPECODE.OTHER);
    foreignIncomeEntity.setAttribute(
      BDMOASDatastoreConstants.FOREIGN_INCOME_ANNUAL_ATTR, "1000");
    personEntity.addChildEntity(foreignIncomeEntity);
    foreignIncomeEntity.update();

    sponsorshipInfoEntity =
      datastore.newEntity(BDMOASDatastoreConstants.SPONSORSHIPINFO_ENTITY);
    sponsorshipInfoEntity.setAttribute(
      BDMOASDatastoreConstants.IS_SPOUSE_OR_CLP_SPONSORED_ATTR, IEGYESNO.YES);
    personEntity.addChildEntity(sponsorshipInfoEntity);
    sponsorshipInfoEntity.update();

    personEntity.update();
    applicationEntity.update();

  }

  /**
   * Test that the appropriate fields are set.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_setTempAttributes_CAAddress()
    throws AppException, InformationalException, NoSuchSchemaException {

    setUpEntities();

    final Entity residentialAddress =
      datastore.newEntity(BDMDatastoreConstants.RESIDENTIAL_ADDRESS);
    residentialAddress.setAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY,
      COUNTRY.CA);
    residentialAddress.setAttribute(BDMDatastoreConstants.POSTAL_CODE,
      "A1A1A1");
    residentialAddress.setAttribute(BDMDatastoreConstants.PROVINCE, "ON");
    residentialAddress.setAttribute(BDMDatastoreConstants.SUITE_NUM, "11");
    residentialAddress.setAttribute(BDMDatastoreConstants.ADDRESS_LINE1,
      "3-4");
    residentialAddress.setAttribute(BDMDatastoreConstants.ADDRESS_LINE2,
      "Broken Road");
    residentialAddress.setAttribute(BDMDatastoreConstants.CITY, "ST. John's");
    personEntity.addChildEntity(residentialAddress);
    residentialAddress.update();

    final String tempResidentialAddress = "11" + " " + "3-4" + " "
      + "Broken Road" + " " + " " + " " + "ST. John's" + " " + "ON" + " "
      + " " + COUNTRY.CA + " " + " " + "A1A1A1";

    final Entity mailingAddress =
      datastore.newEntity(BDMDatastoreConstants.MAILING_ADDRESS);
    mailingAddress.setAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY,
      COUNTRY.CA);
    mailingAddress.setAttribute(BDMDatastoreConstants.POSTAL_CODE, "B1B1B1");
    mailingAddress.setAttribute(BDMDatastoreConstants.PROVINCE, "NS");
    mailingAddress.setAttribute(BDMDatastoreConstants.SUITE_NUM, "22");
    mailingAddress.setAttribute(BDMDatastoreConstants.ADDRESS_LINE1, "4-5");
    mailingAddress.setAttribute(BDMDatastoreConstants.ADDRESS_LINE2,
      "Mailing Road");
    mailingAddress.setAttribute(BDMDatastoreConstants.CITY, "Halifax");
    personEntity.addChildEntity(mailingAddress);
    mailingAddress.update();

    final String tempMailingAddress =
      "22" + " " + "4-5" + " " + "Mailing Road" + " " + " " + " " + "Halifax"
        + " " + "NS" + " " + " " + COUNTRY.CA + " " + " " + "B1B1B1";

    personEntity.update();
    applicationEntity.update();

    new Expectations(datastore) {

      {
        datastore.readEntity(anyLong);
        result = applicationEntity;

      }
    };

    ieg2Context.setRootEntityID(applicationEntity.getUniqueID());
    final RulesParameters rulesParameters = ieg2Context;

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction
        .getAdaptorValue(rulesParameters);

    // Verify
    assertEquals(true, adaptorValue.getValue(ieg2Context));

    final Entity verifyPersonEntity = applicationEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.PERSON))[0];
    // address

    assertEquals(tempResidentialAddress, verifyPersonEntity
      .getAttribute(BDMOASDatastoreConstants.kTempPartResAddress));

    assertEquals(tempMailingAddress, verifyPersonEntity
      .getAttribute(BDMOASDatastoreConstants.kTempPartMailAddress));

    // retirement details
    assertEquals(retirementIncomeEntity.getAttribute(
      BDMOASDatastoreConstants.RETIREMENT_PENSION_REDUCTION_RETIRED_ORRETIRING_ATTR),
      verifyPersonEntity
        .getAttribute(BDMOASDatastoreConstants.kTempIsRetirdOrRetirng2Yrs));

    assertEquals(retirementIncomeEntity.getAttribute(
      BDMOASDatastoreConstants.RETIREMENT_PENSION_REDUCTION_RETIREDATE_ATTR),
      verifyPersonEntity
        .getAttribute(BDMOASDatastoreConstants.kTempRetirementDate));

    assertEquals(
      retirementIncomeEntity.getAttribute(
        BDMOASDatastoreConstants.RETIREMENT_PENSION_REDUCTION_REDUCED_ATTR),
      verifyPersonEntity
        .getAttribute(BDMOASDatastoreConstants.kTempIsPensionReduced2Yrs));

    assertEquals(
      retirementIncomeEntity.getAttribute(
        BDMOASDatastoreConstants.RETIREMENT_PENSION_REDUCTION_DATE_ATTR),
      verifyPersonEntity
        .getAttribute(BDMOASDatastoreConstants.kTempReductionDate));

    // foreign income
    assertEquals(
      foreignIncomeEntity.getAttribute(
        BDMOASDatastoreConstants.FOREIGN_INCOME_ISRECEIVING_ATTR),
      verifyPersonEntity.getAttribute(
        BDMOASDatastoreConstants.kTempIsReceivingForeignIncome));

    assertEquals(
      foreignIncomeEntity
        .getAttribute(BDMOASDatastoreConstants.FOREIGN_INCOME_TYPE_ATTR),
      verifyPersonEntity
        .getAttribute(BDMOASDatastoreConstants.kTempForeignIncomeType));

    assertEquals(
      foreignIncomeEntity
        .getAttribute(BDMOASDatastoreConstants.FOREIGN_INCOME_ANNUAL_ATTR),
      verifyPersonEntity
        .getAttribute(BDMOASDatastoreConstants.kTempAnnualForeignIncomeStr));

    // sponsorship
    assertEquals(
      sponsorshipInfoEntity.getAttribute(
        BDMOASDatastoreConstants.IS_SPOUSE_OR_CLP_SPONSORED_ATTR),
      verifyPersonEntity.getAttribute(
        BDMOASDatastoreConstants.kTempIsSpouseOrCLPartnrSponsored));

  }

  /**
   * Test that the appropriate fields are set.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_setTempAttributes_IntlAddress()
    throws AppException, InformationalException, NoSuchSchemaException {

    setUpEntities();

    final Entity residentialAddress =
      datastore.newEntity(BDMDatastoreConstants.INTL_RESIDENTIAL_ADDRESS);
    residentialAddress.setAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY,
      COUNTRY.IRELAND);
    residentialAddress.setAttribute(BDMDatastoreConstants.ZIP_CODE, "12345");
    residentialAddress.setAttribute(BDMDatastoreConstants.ADDRESS_LINE4,
      "dfsdj");
    residentialAddress.setAttribute(BDMDatastoreConstants.ADDRESS_LINE3,
      "dfjsd");
    residentialAddress.setAttribute(BDMDatastoreConstants.STATE, "Dublin");
    residentialAddress.setAttribute(BDMDatastoreConstants.SUITE_NUM, "11");
    residentialAddress.setAttribute(BDMDatastoreConstants.ADDRESS_LINE1,
      "3-4");
    residentialAddress.setAttribute(BDMDatastoreConstants.ADDRESS_LINE2,
      "Broken Road");
    residentialAddress.setAttribute(BDMDatastoreConstants.CITY, "ST. John's");
    personEntity.addChildEntity(residentialAddress);
    residentialAddress.update();

    final String tempResidentialAddress = "11" + " " + "3-4" + " "
      + "Broken Road" + " " + "dfjsd" + " " + "dfsdj" + " " + "ST. John's"
      + " " + " " + "Dublin" + " " + COUNTRY.IRELAND + " " + "12345" + " ";

    final Entity mailingAddress =
      datastore.newEntity(BDMDatastoreConstants.INTL_MAILING_ADDRESS);
    mailingAddress.setAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY,
      COUNTRY.GB);
    mailingAddress.setAttribute(BDMDatastoreConstants.ADDRESS_LINE4,
      "asdals");
    mailingAddress.setAttribute(BDMDatastoreConstants.ADDRESS_LINE3,
      "dfdsfsd");
    mailingAddress.setAttribute(BDMDatastoreConstants.ZIP_CODE, "54321");
    mailingAddress.setAttribute(BDMDatastoreConstants.STATE, "skfsd");
    mailingAddress.setAttribute(BDMDatastoreConstants.SUITE_NUM, "22");
    mailingAddress.setAttribute(BDMDatastoreConstants.ADDRESS_LINE1, "4-5");
    mailingAddress.setAttribute(BDMDatastoreConstants.ADDRESS_LINE2,
      "Mailing Road");
    mailingAddress.setAttribute(BDMDatastoreConstants.CITY, "Halifax");
    personEntity.addChildEntity(mailingAddress);
    mailingAddress.update();

    final String tempMailingAddress = "22" + " " + "4-5" + " "
      + "Mailing Road" + " " + "dfdsfsd" + " " + "asdals" + " " + "Halifax"
      + " " + " " + "skfsd" + " " + COUNTRY.GB + " " + "54321" + " ";

    personEntity.update();
    applicationEntity.update();

    new Expectations(datastore) {

      {
        datastore.readEntity(anyLong);
        result = applicationEntity;

      }
    };

    ieg2Context.setRootEntityID(applicationEntity.getUniqueID());
    final RulesParameters rulesParameters = ieg2Context;

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) customFunction
        .getAdaptorValue(rulesParameters);

    // Verify
    assertEquals(true, adaptorValue.getValue(ieg2Context));

    final Entity verifyPersonEntity = applicationEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.PERSON))[0];
    // address

    assertEquals(tempResidentialAddress, verifyPersonEntity
      .getAttribute(BDMOASDatastoreConstants.kTempPartResAddress));

    assertEquals(tempMailingAddress, verifyPersonEntity
      .getAttribute(BDMOASDatastoreConstants.kTempPartMailAddress));

    // retirement details
    assertEquals(retirementIncomeEntity.getAttribute(
      BDMOASDatastoreConstants.RETIREMENT_PENSION_REDUCTION_RETIRED_ORRETIRING_ATTR),
      verifyPersonEntity
        .getAttribute(BDMOASDatastoreConstants.kTempIsRetirdOrRetirng2Yrs));

    assertEquals(retirementIncomeEntity.getAttribute(
      BDMOASDatastoreConstants.RETIREMENT_PENSION_REDUCTION_RETIREDATE_ATTR),
      verifyPersonEntity
        .getAttribute(BDMOASDatastoreConstants.kTempRetirementDate));

    assertEquals(
      retirementIncomeEntity.getAttribute(
        BDMOASDatastoreConstants.RETIREMENT_PENSION_REDUCTION_REDUCED_ATTR),
      verifyPersonEntity
        .getAttribute(BDMOASDatastoreConstants.kTempIsPensionReduced2Yrs));

    assertEquals(
      retirementIncomeEntity.getAttribute(
        BDMOASDatastoreConstants.RETIREMENT_PENSION_REDUCTION_DATE_ATTR),
      verifyPersonEntity
        .getAttribute(BDMOASDatastoreConstants.kTempReductionDate));

    // foreign income
    assertEquals(
      foreignIncomeEntity.getAttribute(
        BDMOASDatastoreConstants.FOREIGN_INCOME_ISRECEIVING_ATTR),
      verifyPersonEntity.getAttribute(
        BDMOASDatastoreConstants.kTempIsReceivingForeignIncome));

    assertEquals(
      foreignIncomeEntity
        .getAttribute(BDMOASDatastoreConstants.FOREIGN_INCOME_TYPE_ATTR),
      verifyPersonEntity
        .getAttribute(BDMOASDatastoreConstants.kTempForeignIncomeType));

    assertEquals(
      foreignIncomeEntity
        .getAttribute(BDMOASDatastoreConstants.FOREIGN_INCOME_ANNUAL_ATTR),
      verifyPersonEntity
        .getAttribute(BDMOASDatastoreConstants.kTempAnnualForeignIncomeStr));

    // sponsorship
    assertEquals(
      sponsorshipInfoEntity.getAttribute(
        BDMOASDatastoreConstants.IS_SPOUSE_OR_CLP_SPONSORED_ATTR),
      verifyPersonEntity.getAttribute(
        BDMOASDatastoreConstants.kTempIsSpouseOrCLPartnrSponsored));

  }

}
