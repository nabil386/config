package curam.ca.gc.bdmoas.test.application.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventDatastoreConstants;
import curam.ca.gc.bdm.codetable.BDMMARITALSTATUS;
import curam.ca.gc.bdm.codetable.impl.BDMMARITALSTATUSEntry;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.facade.fec.struct.BDMFAConcernRoleKey;
import curam.ca.gc.bdm.facade.fec.struct.BDMForeignID;
import curam.ca.gc.bdm.facade.fec.struct.BDMListOfForeignIDs;
import curam.ca.gc.bdm.lifeevent.impl.BDMContactPreferenceEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMContactPreferenceEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMDateofBirthEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMDateofBirthEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMGenderEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMGenderEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMIdentificationEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMIdentificationEvidenceVO;
import curam.ca.gc.bdmoas.application.impl.BDMOASConcernRoleMappingStrategyImpl;
import curam.ca.gc.bdmoas.application.impl.BDMOASDatastoreConstants;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.ca.gc.bdmoas.evidencemapping.util.impl.BDMOASEvidenceMappingUtil;
import curam.ca.gc.bdmoas.test.evidence.BDMEvidenceUtilsTest;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.participant.impl.ConcernRole;
import curam.participant.impl.ConcernRoleDAO;
import curam.pdc.fact.PDCBankAccountFactory;
import curam.pdc.impl.PDCConst;
import curam.pdc.struct.PDCPersonDetails;
import curam.pdc.struct.ParticipantBankAccountDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)
public class BDMOASConcernRoleMappingStrategyImplTest extends BDMOASCaseTest {

  /** The validate function. */
  @Tested
  BDMOASConcernRoleMappingStrategyImpl bdmoasConcernRoleMappingStrategyImpl;

  @Inject
  private ConcernRoleDAO concernRoleDAO;

  Datastore datastore;

  /** The testing functions. */
  BDMEvidenceUtilsTest bdmEvidenceUtils;

  /** The entity functions. */
  @Mocked
  BDMEvidenceUtil bdmEvidenceUtil;

  @Mocked
  BDMOASEvidenceMappingUtil bdmoasEvidenceMappingUtil;

  @Mocked
  BDMApplicationEventsUtil bdmApplicationEventsUtil;

  @Mocked
  PDCBankAccountFactory pdcBankAccountFactory;

  @Injectable
  BDMUtil bdmUtil;

  @Injectable
  BDMIdentificationEvidence bdmIdentificationEvd;

  Entity applicationEntity;

  Entity primaryPersonEntity;

  /**
   * Before each test, init the tested class and other objects.
   */
  @Before
  public void before() throws NoSuchSchemaException {

    GuiceWrapper.getInjector().injectMembers(this);

    bdmEvidenceUtils = new BDMEvidenceUtilsTest();
    bdmoasConcernRoleMappingStrategyImpl =
      new BDMOASConcernRoleMappingStrategyImpl();
    bdmUtil = new BDMUtil();
    datastore = DatastoreFactory.newInstance()
      .openDatastore("BDMOASGISApplicationSchema");

  }



  public void setUpEntities(final long concernRoleID)
    throws NoSuchSchemaException {

    applicationEntity =
      datastore.newEntity(BDMDatastoreConstants.APPLICATION);
    datastore.addRootEntity(applicationEntity);

    primaryPersonEntity = datastore.newEntity(BDMDatastoreConstants.PERSON);
    primaryPersonEntity
      .setAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT, "true");
    primaryPersonEntity.setAttribute("localID", "" + concernRoleID);
    applicationEntity.addChildEntity(primaryPersonEntity);

  }

  public void expectationsGetChildEntity(final Entity[] entities) {

    new Expectations(primaryPersonEntity) {

      {
        primaryPersonEntity.getChildEntities(
          primaryPersonEntity.getDatastore().getEntityType(anyString));
        result = entities;

      }

    };
  }

  public void expectationsCreateForiegnKeySet(final String altID)
    throws InformationalException, AppException {

    final BDMListOfForeignIDs bdmListOfForeignIDs = new BDMListOfForeignIDs();
    final BDMForeignID bdmForeignID = new BDMForeignID();
    bdmForeignID.alternateID = altID;
    bdmListOfForeignIDs.bdmFId.addRef(bdmForeignID);
    new Expectations(bdmUtil) {

      {
        bdmUtil.getListOfForeignIdentifiers((BDMFAConcernRoleKey) any);
        result = bdmListOfForeignIDs;

      }
    };

  }

  public void verifyCreated() throws AppException, InformationalException {

    try {

      new Verifications() {

        {
          BDMEvidenceUtil.createPDCDynamicEvidence(anyLong,
            (HashMap<String, String>) any, anyString, anyString);
          times = 1;

        }

      };

    } catch (final mockit.internal.MissingInvocation e) {
      assertTrue(
        "With given attribute information, evidence should be created.",
        false);
    }

  }

  public void verifyNotCreated() throws AppException, InformationalException {

    try {

      new Verifications() {

        {
          BDMEvidenceUtil.createPDCDynamicEvidence(anyLong,
            (HashMap<String, String>) any, anyString, anyString);
          times = 0;

        }

      };
    } catch (final mockit.internal.UnexpectedInvocation e) {
      assertTrue(
        "With given attribute information, evidence should not be created.",
        false);
    }
  }

  /**
   * test method createBankAccount
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testCreateBankAccount_Match()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final ConcernRole concernRole =
      this.concernRoleDAO.get(pdcPersonDetails.concernRoleID);

    setUpEntities(pdcPersonDetails.concernRoleID);

    final Entity paymentEntity =
      datastore.newEntity(BDMDatastoreConstants.PAYMENT);
    paymentEntity.setAttribute(BDMDatastoreConstants.BANK_ACCT_NUM,
      "12345678");
    paymentEntity.setAttribute(BDMDatastoreConstants.BANK_INSTITUTION_CODE,
      "002");
    paymentEntity.setAttribute(BDMDatastoreConstants.BANK_BRANCH_NUM,
      "12345");
    primaryPersonEntity.addChildEntity(paymentEntity);
    paymentEntity.update();

    final Entity paymentBanksEntity =
      datastore.newEntity(BDMLifeEventDatastoreConstants.PAYMENT_BANKS);
    paymentBanksEntity
      .setAttribute(BDMLifeEventDatastoreConstants.BANK_ACCT_NUM, "12345678");
    paymentBanksEntity.setAttribute(
      BDMLifeEventDatastoreConstants.BANK_SORT_CODE, "00212345");
    primaryPersonEntity.addChildEntity(paymentBanksEntity);
    paymentBanksEntity.update();

    primaryPersonEntity.update();
    applicationEntity.update();

    final Entity[] entities = {paymentBanksEntity };

    new Expectations() {

      {
        BDMApplicationEventsUtil.retrieveChildEntity(primaryPersonEntity,
          BDMDatastoreConstants.PAYMENT);
        result = paymentEntity;

        BDMApplicationEventsUtil.retrieveChildEntities(primaryPersonEntity,
          BDMLifeEventDatastoreConstants.PAYMENT_BANKS);
        result = entities;
      }
    };

    Deencapsulation.invoke(bdmoasConcernRoleMappingStrategyImpl,
      "createBankAccount", primaryPersonEntity, concernRole);

  }

  /**
   * test method createBankAccount
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testCreateBankAccount_NotMatch()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final ConcernRole concernRole =
      this.concernRoleDAO.get(pdcPersonDetails.concernRoleID);

    setUpEntities(pdcPersonDetails.concernRoleID);

    final Entity paymentEntity =
      datastore.newEntity(BDMDatastoreConstants.PAYMENT);
    paymentEntity.setAttribute(BDMDatastoreConstants.BANK_ACCT_NUM,
      "12345678");
    paymentEntity.setAttribute(BDMDatastoreConstants.BANK_INSTITUTION_CODE,
      "002");
    paymentEntity.setAttribute(BDMDatastoreConstants.BANK_BRANCH_NUM,
      "12345");
    paymentEntity.setAttribute(BDMDatastoreConstants.BANK_ACCOUNT_NAME,
      "Jane X");
    primaryPersonEntity.addChildEntity(paymentEntity);
    paymentEntity.update();

    final Entity paymentBanksEntity =
      datastore.newEntity(BDMLifeEventDatastoreConstants.PAYMENT_BANKS);
    paymentBanksEntity
      .setAttribute(BDMLifeEventDatastoreConstants.BANK_ACCT_NUM, "87654321");
    paymentBanksEntity.setAttribute(
      BDMLifeEventDatastoreConstants.BANK_SORT_CODE, "00154321");
    primaryPersonEntity.addChildEntity(paymentBanksEntity);
    paymentBanksEntity.update();

    primaryPersonEntity.update();
    applicationEntity.update();

    final Entity[] entities = {paymentBanksEntity };

    new Expectations() {

      {
        BDMApplicationEventsUtil.retrieveChildEntity(primaryPersonEntity,
          BDMDatastoreConstants.PAYMENT);
        result = paymentEntity;

        BDMApplicationEventsUtil.retrieveChildEntities(primaryPersonEntity,
          BDMLifeEventDatastoreConstants.PAYMENT_BANKS);
        result = entities;

        PDCBankAccountFactory.newInstance();
        result = pdcBankAccountFactory;

        pdcBankAccountFactory.insert((ParticipantBankAccountDetails) any);
        times = 1;

      }
    };

    Deencapsulation.invoke(bdmoasConcernRoleMappingStrategyImpl,
      "createBankAccount", primaryPersonEntity, concernRole);

  }

  /**
   * test method updateDateOfBirthEvidence
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testUpdateDateOfBirthEvidence_Created(@Mocked
  final BDMDateofBirthEvidence bdmDateofBirthEvidence)
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final ConcernRole concernRole =
      this.concernRoleDAO.get(pdcPersonDetails.concernRoleID);

    setUpEntities(pdcPersonDetails.concernRoleID);
    primaryPersonEntity
      .setAttribute(BDMLifeEventDatastoreConstants.LAST_NAME_AT_BIRTH, "Doe");
    primaryPersonEntity
      .setAttribute(BDMLifeEventDatastoreConstants.DATE_OF_BIRTH, "19601221");
    primaryPersonEntity.update();
    applicationEntity.update();

    final List<BDMDateofBirthEvidenceVO> getDOBEvidenceValueObjectList =
      new ArrayList<BDMDateofBirthEvidenceVO>();

    new Expectations() {

      {
        new BDMDateofBirthEvidence();
        result = bdmDateofBirthEvidence;

        bdmDateofBirthEvidence.getDOBEvidenceValueObject(concernRole.getID());
        result = getDOBEvidenceValueObjectList;
      }
    };

    Deencapsulation.invoke(bdmoasConcernRoleMappingStrategyImpl,
      "updateDateOfBirthEvidence", primaryPersonEntity, concernRole);
    verifyCreated();
  }

  /**
   * test method updateDateOfBirthEvidence
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testUpdateDateOfBirthEvidence_NotCreated(@Mocked
  final BDMDateofBirthEvidence bdmDateofBirthEvidence)
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final ConcernRole concernRole =
      this.concernRoleDAO.get(pdcPersonDetails.concernRoleID);

    setUpEntities(pdcPersonDetails.concernRoleID);
    primaryPersonEntity
      .setAttribute(BDMLifeEventDatastoreConstants.LAST_NAME_AT_BIRTH, "Doe");
    primaryPersonEntity
      .setAttribute(BDMLifeEventDatastoreConstants.DATE_OF_BIRTH, "19601221");
    primaryPersonEntity
      .setAttribute(BDMOASDatastoreConstants.COUNTRY_OF_BIRTH, "CA");
    primaryPersonEntity.update();
    applicationEntity.update();

    final List<BDMDateofBirthEvidenceVO> getDOBEvidenceValueObjectList =
      new ArrayList<BDMDateofBirthEvidenceVO>();

    final BDMDateofBirthEvidenceVO bdmDateofBirthEvidenceVO =
      new BDMDateofBirthEvidenceVO();
    bdmDateofBirthEvidenceVO.setDateOfDeath(Date.getCurrentDate());
    bdmDateofBirthEvidenceVO.setPerson(39423);

    getDOBEvidenceValueObjectList.add(bdmDateofBirthEvidenceVO);
    new Expectations() {

      {
        new BDMDateofBirthEvidence();
        result = bdmDateofBirthEvidence;

        bdmDateofBirthEvidence.getDOBEvidenceValueObject(concernRole.getID());
        result = getDOBEvidenceValueObjectList;
      }
    };

    Deencapsulation.invoke(bdmoasConcernRoleMappingStrategyImpl,
      "updateDateOfBirthEvidence", primaryPersonEntity, concernRole);
    verifyNotCreated();
  }

  /**
   * test method createMaritalStatusEvidence
   * for scenario of creating Marital Status evidence because current status is
   * single
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testCreateMaritalStatusEvidence_Created()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final ConcernRole concernRole =
      this.concernRoleDAO.get(pdcPersonDetails.concernRoleID);

    setUpEntities(pdcPersonDetails.concernRoleID);
    final Entity maritalStatusEntity = datastore
      .newEntity(BDMOASDatastoreConstants.MARITAL_STATUS_INFO_ENTITY);
    maritalStatusEntity.setAttribute(
      BDMOASDatastoreConstants.MARITAL_STATUS_INFO_CURNTSTATUS_ATTR,
      BDMMARITALSTATUS.SINGLE);
    primaryPersonEntity.addChildEntity(maritalStatusEntity);
    maritalStatusEntity.update();
    primaryPersonEntity.update();
    applicationEntity.update();

    final Entity[] entities = {maritalStatusEntity };
    expectationsGetChildEntity(entities);

    Deencapsulation.invoke(bdmoasConcernRoleMappingStrategyImpl,
      "createMaritalStatusEvidence", primaryPersonEntity, concernRole);

    try {

      new Verifications() {

        {
          BDMOASEvidenceMappingUtil.mapMaritalStatusEvidence(anyLong,
            BDMMARITALSTATUSEntry.SINGLE);
          times = 1;

        }

      };

    } catch (final mockit.internal.MissingInvocation e) {
      assertTrue(
        "With given attribute information, evidence should be created.",
        false);
    }

  }

  /**
   * test method createMaritalStatusEvidence
   * for scenario of Marital Status evidence already exists because current
   * status is
   * married
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testCreateMaritalStatusEvidence_NotCreated()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final ConcernRole concernRole =
      this.concernRoleDAO.get(pdcPersonDetails.concernRoleID);

    setUpEntities(pdcPersonDetails.concernRoleID);
    final Entity maritalStatusEntity = datastore
      .newEntity(BDMOASDatastoreConstants.MARITAL_STATUS_INFO_ENTITY);
    maritalStatusEntity.setAttribute(
      BDMOASDatastoreConstants.MARITAL_STATUS_INFO_CURNTSTATUS_ATTR,
      BDMMARITALSTATUS.MARRIED);
    primaryPersonEntity.addChildEntity(maritalStatusEntity);
    maritalStatusEntity.update();
    primaryPersonEntity.update();
    applicationEntity.update();

    final Entity[] entities = {maritalStatusEntity };
    expectationsGetChildEntity(entities);

    Deencapsulation.invoke(bdmoasConcernRoleMappingStrategyImpl,
      "createMaritalStatusEvidence", primaryPersonEntity, concernRole);
    verifyNotCreated();

  }

  /**
   * test method createPDCForeignIDEvidence
   * for scenario of creating Foreign ID evidence
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testCreatePDCForeignIDEvidence_Created()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final ConcernRole concernRole =
      this.concernRoleDAO.get(pdcPersonDetails.concernRoleID);

    setUpEntities(pdcPersonDetails.concernRoleID);
    final Entity residencePeriodEntity =
      datastore.newEntity(BDMOASDatastoreConstants.RESIDENCE_HISTORY);
    residencePeriodEntity.setAttribute(
      BDMOASDatastoreConstants.RESIDENCE_HISTORY_COUNTRYID_ATTR, "CA");
    primaryPersonEntity.addChildEntity(residencePeriodEntity);
    residencePeriodEntity.update();
    primaryPersonEntity.update();
    applicationEntity.update();

    final Entity[] entities = {residencePeriodEntity };
    expectationsGetChildEntity(entities);
    expectationsCreateForiegnKeySet("US");

    Deencapsulation.invoke(bdmoasConcernRoleMappingStrategyImpl,
      "createPDCForeignIDEvidence", primaryPersonEntity, concernRole);
    verifyCreated();
  }

  /**
   * test method createPDCForeignIDEvidence
   * for scenario of not creating Foreign ID evidence
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testCreatePDCForeignIDEvidence_NotCreated()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final ConcernRole concernRole =
      this.concernRoleDAO.get(pdcPersonDetails.concernRoleID);

    setUpEntities(pdcPersonDetails.concernRoleID);

    final Entity residencePeriodEntity =
      datastore.newEntity(BDMOASDatastoreConstants.RESIDENCE_HISTORY);
    residencePeriodEntity.setAttribute(
      BDMOASDatastoreConstants.RESIDENCE_HISTORY_COUNTRYID_ATTR, "CA");
    primaryPersonEntity.addChildEntity(residencePeriodEntity);
    residencePeriodEntity.update();
    primaryPersonEntity.update();
    applicationEntity.update();

    final Entity[] entities = {residencePeriodEntity };
    expectationsGetChildEntity(entities);
    expectationsCreateForiegnKeySet("CA");

    Deencapsulation.invoke(bdmoasConcernRoleMappingStrategyImpl,
      "createPDCForeignIDEvidence", primaryPersonEntity, concernRole);
    verifyNotCreated();
  }

  /**
   * test method UpdateGenderEvidence
   * for scenario of creating gender evidence
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testUpdateGenderEvidence_Created(@Mocked
  final BDMGenderEvidence BDMGenderEvidence)
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final ConcernRole concernRole =
      this.concernRoleDAO.get(pdcPersonDetails.concernRoleID);

    setUpEntities(pdcPersonDetails.concernRoleID);
    primaryPersonEntity.setAttribute(BDMLifeEventDatastoreConstants.GENDER,
      "F");
    primaryPersonEntity.update();
    applicationEntity.update();

    final List<BDMGenderEvidenceVO> genderEvidenceValueObjectList =
      new ArrayList<BDMGenderEvidenceVO>();

    new Expectations() {

      {
        BDMGenderEvidence.getGenderEvidenceValueObject(anyLong);
        result = genderEvidenceValueObjectList;
      }
    };

    Deencapsulation.invoke(bdmoasConcernRoleMappingStrategyImpl,
      "updateGenderEvidence", primaryPersonEntity, concernRole);
    verifyCreated();
  }

  /**
   * test method UpdateGenderEvidence
   * for scenario of not creating gender evidence
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testUpdateGenderEvidence_NotCreated(@Mocked
  final BDMGenderEvidence BDMGenderEvidence)
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final ConcernRole concernRole =
      this.concernRoleDAO.get(pdcPersonDetails.concernRoleID);

    setUpEntities(pdcPersonDetails.concernRoleID);
    primaryPersonEntity.setAttribute(BDMLifeEventDatastoreConstants.GENDER,
      "F");
    primaryPersonEntity.update();
    applicationEntity.update();

    final List<BDMGenderEvidenceVO> genderEvidenceValueObjectList =
      new ArrayList<BDMGenderEvidenceVO>();
    final BDMGenderEvidenceVO genderEvidenceVO = new BDMGenderEvidenceVO();
    genderEvidenceVO.setGender("F");
    genderEvidenceValueObjectList.add(genderEvidenceVO);

    new Expectations() {

      {
        BDMGenderEvidence.getGenderEvidenceValueObject(anyLong);
        result = genderEvidenceValueObjectList;
      }
    };
    Deencapsulation.invoke(bdmoasConcernRoleMappingStrategyImpl,
      "updateGenderEvidence", primaryPersonEntity, concernRole);
    verifyNotCreated();
  }

  /**
   * test method UpdateSINEvidence
   * for scenario of creating SIN Evidence
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testUpdateSINEvidence_Created()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final ConcernRole concernRole =
      this.concernRoleDAO.get(pdcPersonDetails.concernRoleID);

    setUpEntities(pdcPersonDetails.concernRoleID);
    primaryPersonEntity.setAttribute(
      BDMLifeEventDatastoreConstants.SOCIAL_INSURANCE_NUMBER, "111111111");
    primaryPersonEntity.setAttribute(BDMLifeEventDatastoreConstants.GENDER,
      "F");
    primaryPersonEntity.update();
    applicationEntity.update();

    Deencapsulation.invoke(bdmoasConcernRoleMappingStrategyImpl,
      "updateSINEvidence", primaryPersonEntity, concernRole);
    verifyCreated();
  }

  /**
   * test method UpdateSINEvidence
   * for scenario of not creating SIN evidence
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testUpdateSINEvidence_NotCreated()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final ConcernRole concernRole =
      this.concernRoleDAO.get(pdcPersonDetails.concernRoleID);

    setUpEntities(pdcPersonDetails.concernRoleID);
    primaryPersonEntity.setAttribute(
      BDMLifeEventDatastoreConstants.SOCIAL_INSURANCE_NUMBER, null);
    primaryPersonEntity.setAttribute(BDMLifeEventDatastoreConstants.GENDER,
      "F");
    primaryPersonEntity.update();
    applicationEntity.update();

    Deencapsulation.invoke(bdmoasConcernRoleMappingStrategyImpl,
      "updateSINEvidence", primaryPersonEntity, concernRole);
    verifyNotCreated();
  }

  /**
   * test method UpdateSINEvidence
   * for scenario of not creating SIN evidence
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testUpdateSINEvidence_Mismatch(@Mocked
  final BDMIdentificationEvidence BDMIdentificationEvidence)
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final ConcernRole concernRole =
      this.concernRoleDAO.get(pdcPersonDetails.concernRoleID);

    setUpEntities(pdcPersonDetails.concernRoleID);
    primaryPersonEntity.setAttribute(
      BDMLifeEventDatastoreConstants.SOCIAL_INSURANCE_NUMBER, "987654321");
    primaryPersonEntity.setAttribute(BDMLifeEventDatastoreConstants.GENDER,
      "F");
    primaryPersonEntity.update();
    applicationEntity.update();

    final BDMIdentificationEvidenceVO sinEvidenceValueObject =
      new BDMIdentificationEvidenceVO();
    sinEvidenceValueObject.setAlternateID("123456789");
    sinEvidenceValueObject
      .setAltIDType(CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER);
    sinEvidenceValueObject.setFromDate(Date.getCurrentDate());

    new Expectations() {

      {

        new BDMIdentificationEvidence();
        result = BDMIdentificationEvidence;

        BDMIdentificationEvidence.getSINEvidenceValueObject(anyLong);
        result = sinEvidenceValueObject;
      }
    };

    Deencapsulation.invoke(bdmoasConcernRoleMappingStrategyImpl,
      "updateSINEvidence", primaryPersonEntity, concernRole);

    try {

      new Verifications() {

        {

          bdmIdentificationEvd.createSINIdentificationEvidence(anyLong,
            (Date) any, (BDMIdentificationEvidenceVO) any,
            EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT,
            Date.kZeroDate);

          times = 1;

        }

      };

    } catch (final mockit.internal.MissingInvocation e) {
      assertTrue(
        "With given attribute information, evidence should be created.",
        false);
    }
  }

  /**
   * test method updateContactPrefEvidence
   * for scenario of creating communication evidence
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testupdateContactPrefEvidence_Created()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final ConcernRole concernRole =
      this.concernRoleDAO.get(pdcPersonDetails.concernRoleID);

    setUpEntities(pdcPersonDetails.concernRoleID);

    final Entity communicationPrefEntity =
      datastore.newEntity(BDMDatastoreConstants.COMMUNICATION_PREF);
    communicationPrefEntity.setAttribute(
      BDMLifeEventDatastoreConstants.PREFERRED_SPOKEN_LANGUAGE, "EN");
    communicationPrefEntity.setAttribute(
      BDMLifeEventDatastoreConstants.PREFERRED_WRITTEN_LANGUAGE, "EN");
    primaryPersonEntity.addChildEntity(communicationPrefEntity);
    communicationPrefEntity.update();
    primaryPersonEntity.update();
    applicationEntity.update();

    Deencapsulation.invoke(bdmoasConcernRoleMappingStrategyImpl,
      "updateContactPrefEvidence", primaryPersonEntity, concernRole);
    verifyCreated();
  }

  /**
   * test method updateContactPrefEvidence
   * for scenario of creating communication evidence
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testupdateContactPrefEvidence_Mismatch(@Mocked
  final BDMContactPreferenceEvidence BDMContactPreferenceEvidence)
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final ConcernRole concernRole =
      this.concernRoleDAO.get(pdcPersonDetails.concernRoleID);

    setUpEntities(pdcPersonDetails.concernRoleID);

    final Entity communicationPrefEntity =
      datastore.newEntity(BDMDatastoreConstants.COMMUNICATION_PREF);
    communicationPrefEntity.setAttribute(
      BDMLifeEventDatastoreConstants.PREFERRED_SPOKEN_LANGUAGE, "EN");
    communicationPrefEntity.setAttribute(
      BDMLifeEventDatastoreConstants.PREFERRED_WRITTEN_LANGUAGE, "EN");
    primaryPersonEntity.addChildEntity(communicationPrefEntity);
    communicationPrefEntity.update();
    primaryPersonEntity.update();
    applicationEntity.update();

    final List<BDMContactPreferenceEvidenceVO> contactPrefEvidenceValueObjectList =
      new ArrayList<BDMContactPreferenceEvidenceVO>();
    final BDMContactPreferenceEvidenceVO contactPrefEvidenceVOObject =
      new BDMContactPreferenceEvidenceVO();
    contactPrefEvidenceVOObject
      .setPreferredOralLanguage(communicationPrefEntity.getAttribute(
        BDMLifeEventDatastoreConstants.PREFERRED_SPOKEN_LANGUAGE));
    contactPrefEvidenceVOObject
      .setPreferredWrittenLanguage(communicationPrefEntity.getAttribute(
        BDMLifeEventDatastoreConstants.PREFERRED_WRITTEN_LANGUAGE));
    contactPrefEvidenceValueObjectList.add(contactPrefEvidenceVOObject);

    new Expectations() {

      {
        new BDMContactPreferenceEvidence();
        result = BDMContactPreferenceEvidence;

        BDMContactPreferenceEvidence
          .getEvidenceValueObject(concernRole.getID());
        result = contactPrefEvidenceValueObjectList;
      }
    };

    Deencapsulation.invoke(bdmoasConcernRoleMappingStrategyImpl,
      "updateContactPrefEvidence", primaryPersonEntity, concernRole);

    try {

      new Verifications() {

        {
          BDMEvidenceUtil.modifyEvidence(anyLong,
            PDCConst.PDCCONTACTPREFERENCES,
            (BDMContactPreferenceEvidenceVO) any,
            EVIDENCECHANGEREASON.REPORTEDBYCLIENT, true);
          times = 1;

        }

      };

    } catch (final mockit.internal.MissingInvocation e) {
      assertTrue(
        "With given attribute information, evidence should be created.",
        false);
    }

  }

}
