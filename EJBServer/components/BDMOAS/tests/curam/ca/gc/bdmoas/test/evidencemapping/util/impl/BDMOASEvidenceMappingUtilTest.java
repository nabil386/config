package curam.ca.gc.bdmoas.test.evidencemapping.util.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.codetable.BDMMARITALSTATUS;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdmoas.application.impl.BDMOASDatastoreConstants;
import curam.ca.gc.bdmoas.codetable.BDMOASLEGALSTATUS;
import curam.ca.gc.bdmoas.evidencemapping.util.impl.BDMOASEvidenceMappingUtil;
import curam.ca.gc.bdmoas.test.evidence.BDMEvidenceUtilsTest;
import curam.ca.gc.bdmoas.test.rules.functions.CustomFunctionTestJunit4;
import curam.ca.gc.bdmoas.util.impl.BDMOASEvidenceUtil;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.COUNTRY;
import curam.codetable.DOCUMENTTYPE;
import curam.codetable.IEGYESNO;
import curam.codetable.INCOMETYPECODE;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.codetable.impl.CASEPARTICIPANTROLETYPEEntry;
import curam.core.impl.CuramConst;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.dynamicevidence.sl.impl.EvidenceGenericSLFactory;
import curam.dynamicevidence.sl.impl.EvidenceServiceInterface;
import curam.participant.impl.ConcernRoleDAO;
import curam.pdc.impl.PDCConst;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.Date;
import curam.workspaceservices.applicationprocessing.impl.DataStoreCEFMappings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertTrue;

/**
 * The Class tests {@link BDMOASEvidenceMappingUtil} custom
 * function.
 */

@RunWith(JMockit.class)
public class BDMOASEvidenceMappingUtilTest extends CustomFunctionTestJunit4 {

  private static final String SCHEMA_NAME = "BDMOASGISApplicationSchema";

  /** The entity functions. */
  @Mocked
  BDMEvidenceUtil bdmEvidenceUtil;

  @Mocked
  BDMOASEvidenceUtil bdmoasEvidenceUtil;

  Datastore datastore;

  Entity applicationEntity;

  Entity personEntity;

  @Inject
  private ConcernRoleDAO concernRoleDAO;

  @Mocked
  DataStoreCEFMappings dataStoreCEFMappings;

  /** The testing functions. */

  BDMEvidenceUtilsTest bdmEvidenceUtils;

  /** The validated function. */
  @Tested
  BDMOASEvidenceMappingUtil bdmoasEvidenceMappingUtil;

  public BDMOASEvidenceMappingUtilTest() {

    super();
  }

  /**
   * Before each test, init the tested class and other objects.
   */
  @Before
  public void before() throws NoSuchSchemaException {

    GuiceWrapper.getInjector().injectMembers(this);

    bdmEvidenceUtils = new BDMEvidenceUtilsTest();
    datastore = DatastoreFactory.newInstance().openDatastore(SCHEMA_NAME);

  }

  public void verifyCreated(final String evidenceType, final int time)
    throws AppException, InformationalException {

    try {

      new Verifications() {

        {
          BDMEvidenceUtil.createACOrICDynamicEvidence(anyLong,
            (HashMap<String, String>) any, evidenceType, anyString, anyLong);
          times = time;

        }

      };

    } catch (final mockit.internal.MissingInvocation e) {
      assertTrue(
        "With given attribute information, evidence should be created.",
        false);
    }

  }

  public void verifyCreatedOnlyAC(final String evidenceType, final int time)
    throws AppException, InformationalException {

    try {

      new Verifications() {

        {
          BDMOASEvidenceUtil.createACDynamicEvidence(anyLong,
            (HashMap<String, String>) any, evidenceType, anyString, anyLong);
          times = time;

        }

      };

    } catch (final mockit.internal.MissingInvocation e) {
      assertTrue(
        "With given attribute information, evidence should be created.",
        false);
    }

  }

  public void verifyCreatedBDMOAS(final CASEEVIDENCEEntry evidenceType,
    final int time) throws AppException, InformationalException {

    try {

      new Expectations(bdmoasEvidenceMappingUtil) {

        {
          BDMOASEvidenceMappingUtil.createACOrICDynamicEvidence(anyLong,
            (HashMap<String, String>) any, evidenceType, anyString, anyLong,
            (CASEPARTICIPANTROLETYPEEntry) any);
          times = time;

        }

      };

    } catch (final mockit.internal.MissingInvocation e) {
      assertTrue(
        "With given attribute information, evidence should be created.",
        false);
    }

  }

  public void expectationsConcernRole(final long concernRoleID) {

    new Expectations() {

      {

        dataStoreCEFMappings.getPrimaryClient().getConcernRoleID();
        result = concernRoleID;

      }
    };

  }

  // utility function to set up entities
  public void setUpEntities(final long concernRoleID)
    throws NoSuchSchemaException {

    applicationEntity =
      datastore.newEntity(BDMDatastoreConstants.APPLICATION);
    applicationEntity.setAttribute(
      BDMOASDatastoreConstants.APPLICATION_RECEIPTDATE_ATTR, "20220606");
    datastore.addRootEntity(applicationEntity);

    personEntity = datastore.newEntity(BDMDatastoreConstants.PERSON);
    personEntity.setAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT,
      "true");
    personEntity.setAttribute("localID", "" + concernRoleID);
    personEntity.setAttribute(BDMOASDatastoreConstants.COUNTRY_OF_BIRTH,
      COUNTRY.CA);
    personEntity.setAttribute(BDMDatastoreConstants.DATE_OF_BIRTH,
      "19500606");

    applicationEntity.addChildEntity(personEntity);

  }

  /**
   * Test that the mapApplicationDetailsEvidence function (which calls
   * createApplicationDetailsEvidence).
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_MapAndCreateApplicationDetailsEvidence()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    setUpEntities(pdcPersonDetails.concernRoleID);
    final Entity oasEntity =
      datastore.newEntity(BDMOASDatastoreConstants.OASPENSION_ENTITY);

    oasEntity.setAttribute(BDMOASDatastoreConstants.OASPENSION_STARTDATE_ATTR,
      "20210606");
    personEntity.addChildEntity(oasEntity);
    oasEntity.update();

    final Entity gisEntity =
      datastore.newEntity(BDMOASDatastoreConstants.GIS);

    gisEntity.setAttribute(BDMOASDatastoreConstants.GIS_APPLYGISIND_ATTR,
      IEGYESNO.YES);
    personEntity.addChildEntity(gisEntity);
    gisEntity.update();

    personEntity.update();
    applicationEntity.update();

    expectationsConcernRole(pdcPersonDetails.concernRoleID);

    // check if OAS and GIS Evidence was created
    verifyCreatedBDMOAS(CASEEVIDENCEEntry.OAS_APPLICATION_DETAILS, 1);

    bdmoasEvidenceMappingUtil.intakeScriptID =
      BDMOASDatastoreConstants.BDMOASGIS_COMBINED_APPLICATION_AP_SCRIPT;

    bdmoasEvidenceMappingUtil.mapApplicationDetailsEvidence(applicationEntity,
      pdcPersonDetails.caseID, dataStoreCEFMappings, SCHEMA_NAME, true);

    verifyCreated(CASEEVIDENCE.OAS_APPLICATION_DETAILS, 1);

  }

  /**
   * Test that the mapLegalStatusEvidence function (which calls
   * createLegalStatusEvidence).
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_MapAndCreateLegalStatusEvidence()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    setUpEntities(pdcPersonDetails.concernRoleID);
    final Entity legalStatusEntity =
      datastore.newEntity(BDMOASDatastoreConstants.LEGAL_STATUS_ENTITY);

    legalStatusEntity.setAttribute(
      BDMOASDatastoreConstants.LEGAL_STATUS_INCALEGSTATUS_ATTR,
      BDMOASLEGALSTATUS.CANADIAN_CITIZEN);
    legalStatusEntity.setAttribute(
      BDMOASDatastoreConstants.LEGAL_STATUS_INCALEGSTATUSOTHR_ATTR, "test");
    personEntity.addChildEntity(legalStatusEntity);

    legalStatusEntity.update();
    personEntity.update();
    applicationEntity.update();

    expectationsConcernRole(pdcPersonDetails.concernRoleID);
    bdmoasEvidenceMappingUtil.mapLegalStatusEvidence(applicationEntity,
      pdcPersonDetails.caseID, dataStoreCEFMappings, SCHEMA_NAME, true);

    verifyCreated(CASEEVIDENCE.OAS_LEGAL_STATUS, 1);

  }

  /**
   * Test that the mapFirstEntryIntoCanadaEvidence function (which calls
   * createFirstEntryIntoCanadaEvidence).
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_MapAndCreateFirstEntryIntoCanadaEvidence()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    setUpEntities(pdcPersonDetails.concernRoleID);
    final Entity legalStatusEntity =
      datastore.newEntity(BDMOASDatastoreConstants.LEGAL_STATUS_ENTITY);
    legalStatusEntity.setAttribute(
      BDMOASDatastoreConstants.OUT_OF_CA_BORNED_ATTRIBUTE, IEGYESNO.YES);
    legalStatusEntity.setAttribute(
      BDMOASDatastoreConstants.LEGAL_STATUS_CA_FIRSTENTRYDATE_ATTR,
      "19990505");
    legalStatusEntity.setAttribute(
      BDMOASDatastoreConstants.LEGAL_STATUS_IMMIGRNDOCTYPE_ATTR,
      DOCUMENTTYPE.LETTER);
    legalStatusEntity.setAttribute(
      BDMOASDatastoreConstants.LEGAL_STATUS_IMMIGRNDOCTYPEOTHR_ATTR,
      "test details");
    personEntity.addChildEntity(legalStatusEntity);

    legalStatusEntity.update();
    personEntity.update();
    applicationEntity.update();

    expectationsConcernRole(pdcPersonDetails.concernRoleID);
    bdmoasEvidenceMappingUtil.mapFirstEntryIntoCanadaEvidence(
      applicationEntity, pdcPersonDetails.caseID, dataStoreCEFMappings,
      SCHEMA_NAME, true);

    verifyCreated(CASEEVIDENCE.OAS_FIRST_ENTRY_INTO_CANADA, 1);

  }

  /**
   * Test that the mapSponsorshipEvidence function (which calls
   * createSponsorshipEvidence).
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_MapAndCreateSponsorshipEvidence()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    setUpEntities(pdcPersonDetails.concernRoleID);
    final Entity legalStatusEntity =
      datastore.newEntity(BDMOASDatastoreConstants.LEGAL_STATUS_ENTITY);

    legalStatusEntity.setAttribute(
      BDMOASDatastoreConstants.LEGAL_STATUS_CASPONSORED_ATTR, "test");
    personEntity.addChildEntity(legalStatusEntity);

    legalStatusEntity.update();
    personEntity.update();
    applicationEntity.update();

    expectationsConcernRole(pdcPersonDetails.concernRoleID);

    verifyCreatedBDMOAS(CASEEVIDENCEEntry.OAS_SPONSORSHIP, 1);

    bdmoasEvidenceMappingUtil.intakeScriptID =
      BDMOASDatastoreConstants.BDMOASGIS_COMBINED_APPLICATION_AP_SCRIPT;

    bdmoasEvidenceMappingUtil.mapSponsorshipEvidence(applicationEntity,
      pdcPersonDetails.caseID, dataStoreCEFMappings, SCHEMA_NAME, true);

  }

  /**
   * Test that the mapForeignIncomeEvidence function (which calls
   * createForeignIncomeEvidence).
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_MapAndCreateForeignIncomeEvidence()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    setUpEntities(pdcPersonDetails.concernRoleID);
    final Entity foreignIncomeEntity =
      datastore.newEntity(BDMOASDatastoreConstants.FOREIGN_INCOME_ENTITY);
    foreignIncomeEntity.setAttribute(
      BDMOASDatastoreConstants.FOREIGN_INCOME_TYPE_ATTR,
      INCOMETYPECODE.OTHER);
    foreignIncomeEntity.setAttribute(
      BDMOASDatastoreConstants.FOREIGN_INCOME_ISRECEIVING_ATTR, IEGYESNO.YES);
    foreignIncomeEntity.setAttribute(
      BDMOASDatastoreConstants.FOREIGN_INCOME_ANNUAL_ATTR, "1000");

    personEntity.addChildEntity(foreignIncomeEntity);

    foreignIncomeEntity.update();
    personEntity.update();
    applicationEntity.update();

    expectationsConcernRole(pdcPersonDetails.concernRoleID);

    bdmoasEvidenceMappingUtil.intakeScriptID =
      BDMOASDatastoreConstants.BDMOASGIS_COMBINED_APPLICATION_AP_SCRIPT;

    verifyCreatedBDMOAS(CASEEVIDENCEEntry.OAS_FOREIGN_INCOME, 1);

    bdmoasEvidenceMappingUtil.mapForeignIncomeEvidence(applicationEntity,
      pdcPersonDetails.caseID, dataStoreCEFMappings, SCHEMA_NAME, true);

  }

  /**
   * Test that the mapResidencePeriodEvidence function (which calls
   * createResidencePeriodEvidence).
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_MapAndCreateResidencePeriodEvidence_since18()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    setUpEntities(pdcPersonDetails.concernRoleID);
    final Entity legalStatusEntity =
      datastore.newEntity(BDMOASDatastoreConstants.LEGAL_STATUS_ENTITY);
    legalStatusEntity.setAttribute(
      BDMOASDatastoreConstants.LEGAL_STATUS_LIVEDINCASINCE18_ATTR,
      IEGYESNO.YES);

    personEntity.addChildEntity(legalStatusEntity);

    legalStatusEntity.update();
    personEntity.update();
    applicationEntity.update();

    expectationsConcernRole(pdcPersonDetails.concernRoleID);
    bdmoasEvidenceMappingUtil.intakeScriptID =
      BDMOASDatastoreConstants.BDMOASGIS_COMBINED_APPLICATION_AP_SCRIPT;

    bdmoasEvidenceMappingUtil.mapResidencePeriodEvidence(applicationEntity,
      pdcPersonDetails.caseID, dataStoreCEFMappings, SCHEMA_NAME, true);

    verifyCreated(CASEEVIDENCE.OAS_RESIDENCE_PERIOD, 1);

  }

  /**
   * Test that the mapResidencePeriodEvidence function (which calls
   * createResidencePeriodEvidence).
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_MapAndCreateResidencePeriodEvidence_notSince18()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    setUpEntities(pdcPersonDetails.concernRoleID);
    final Entity legalStatusEntity =
      datastore.newEntity(BDMOASDatastoreConstants.LEGAL_STATUS_ENTITY);
    legalStatusEntity.setAttribute(
      BDMOASDatastoreConstants.LEGAL_STATUS_LIVEDINCASINCE18_ATTR,
      IEGYESNO.NO);
    personEntity.addChildEntity(legalStatusEntity);
    legalStatusEntity.update();

    final Entity residencePeriodEntity =
      datastore.newEntity(BDMOASDatastoreConstants.RESIDENCE_HISTORY);
    residencePeriodEntity.setAttribute(
      BDMOASDatastoreConstants.RESIDENCE_HISTORY_COUNTRY_ATTR, COUNTRY.US);
    residencePeriodEntity.setAttribute(
      BDMOASDatastoreConstants.RESIDENCE_HISTORY_START_ATTR, "19700606");
    residencePeriodEntity.setAttribute(
      BDMOASDatastoreConstants.RESIDENCE_HISTORY_END_ATTR, "20100606");
    personEntity.addChildEntity(residencePeriodEntity);
    residencePeriodEntity.update();

    personEntity.update();
    applicationEntity.update();

    expectationsConcernRole(pdcPersonDetails.concernRoleID);

    bdmoasEvidenceMappingUtil.intakeScriptID =
      BDMOASDatastoreConstants.BDMOASGIS_COMBINED_APPLICATION_AP_SCRIPT;
    bdmoasEvidenceMappingUtil.mapResidencePeriodEvidence(applicationEntity,
      pdcPersonDetails.caseID, dataStoreCEFMappings, SCHEMA_NAME, true);

    verifyCreated(CASEEVIDENCE.OAS_RESIDENCE_PERIOD, 1);

  }

  /**
   * Test that the mapMaritalRelationshipEvidencefunction (which calls
   * createMaritalRelationshipEvidence).
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_MapAndCreateMaritalRelationshipEvidence_Married()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final PDCPersonDetails pdcPartnerPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    setUpEntities(pdcPersonDetails.concernRoleID);
    personEntity.setAttribute(BDMOASDatastoreConstants.SPOUSE_OR_CLP_IND,
      "true");
    final Entity secondPersonEntity =
      datastore.newEntity(BDMDatastoreConstants.PERSON);
    secondPersonEntity
      .setAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT, "false");
    secondPersonEntity.setAttribute("localID",
      "" + pdcPartnerPersonDetails.concernRoleID);
    applicationEntity.addChildEntity(secondPersonEntity);
    secondPersonEntity.update();

    final Entity maritalStatusEntity = datastore
      .newEntity(BDMOASDatastoreConstants.MARITAL_STATUS_INFO_ENTITY);
    maritalStatusEntity.setAttribute(
      BDMOASDatastoreConstants.MARITAL_STATUS_INFO_CURNTSTATUS_ATTR,
      BDMMARITALSTATUS.MARRIED);
    maritalStatusEntity.setAttribute(
      BDMOASDatastoreConstants.MARITAL_STATUS_INFO_MARITALDATE_ATTR,
      "20000606");

    personEntity.addChildEntity(maritalStatusEntity);

    maritalStatusEntity.update();
    personEntity.update();
    applicationEntity.update();

    expectationsConcernRole(pdcPersonDetails.concernRoleID);

    new Expectations() {

      {
        BDMOASEvidenceUtil.getCaseParticipantRoleIDByType(anyLong, anyString);
        result = pdcPartnerPersonDetails.concernRoleID;

      }
    };

    bdmoasEvidenceMappingUtil.mapMaritalRelationshipEvidence(
      applicationEntity, pdcPersonDetails.caseID, dataStoreCEFMappings,
      SCHEMA_NAME);

    verifyCreatedOnlyAC(CASEEVIDENCE.OAS_MARITAL_RELATIONSHIP, 1);

  }

  /**
   * Test that the mapMaritalRelationshipEvidencefunction (which calls
   * createMaritalRelationshipEvidence).
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_MapAndCreateMaritalRelationshipEvidence_Divorced()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final PDCPersonDetails pdcPartnerPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    setUpEntities(pdcPersonDetails.concernRoleID);
    personEntity.setAttribute(BDMOASDatastoreConstants.SPOUSE_OR_CLP_IND,
      "true");
    final Entity secondPersonEntity =
      datastore.newEntity(BDMDatastoreConstants.PERSON);
    secondPersonEntity
      .setAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT, "false");
    secondPersonEntity.setAttribute("localID",
      "" + pdcPartnerPersonDetails.concernRoleID);
    applicationEntity.addChildEntity(secondPersonEntity);
    secondPersonEntity.update();

    final Entity maritalStatusEntity = datastore
      .newEntity(BDMOASDatastoreConstants.MARITAL_STATUS_INFO_ENTITY);
    maritalStatusEntity.setAttribute(
      BDMOASDatastoreConstants.MARITAL_STATUS_INFO_CURNTSTATUS_ATTR,
      BDMMARITALSTATUS.DIVORCED);
    maritalStatusEntity.setAttribute(
      BDMOASDatastoreConstants.MARITAL_STATUS_INFO_MARITALDATE_ATTR,
      "20000606");

    personEntity.addChildEntity(maritalStatusEntity);

    maritalStatusEntity.update();
    personEntity.update();
    applicationEntity.update();

    expectationsConcernRole(pdcPersonDetails.concernRoleID);

    new Expectations() {

      {
        BDMOASEvidenceUtil.getCaseParticipantRoleIDByType(anyLong, anyString);
        result = pdcPartnerPersonDetails.concernRoleID;
      }
    };

    bdmoasEvidenceMappingUtil.mapMaritalRelationshipEvidence(
      applicationEntity, pdcPersonDetails.caseID, dataStoreCEFMappings,
      SCHEMA_NAME);

    verifyCreatedOnlyAC(CASEEVIDENCE.OAS_MARITAL_RELATIONSHIP, 1);

  }

  /**
   * Test that the mapMaritalRelationshipEvidencefunction (which calls
   * createMaritalRelationshipEvidence).
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_MapAndCreateMaritalRelationshipEvidence_Widowed()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final PDCPersonDetails pdcPartnerPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    setUpEntities(pdcPersonDetails.concernRoleID);
    personEntity.setAttribute(BDMOASDatastoreConstants.SPOUSE_OR_CLP_IND,
      "true");
    final Entity secondPersonEntity =
      datastore.newEntity(BDMDatastoreConstants.PERSON);
    secondPersonEntity
      .setAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT, "false");
    secondPersonEntity.setAttribute("localID",
      "" + pdcPartnerPersonDetails.concernRoleID);
    applicationEntity.addChildEntity(secondPersonEntity);
    secondPersonEntity.update();

    final Entity maritalStatusEntity = datastore
      .newEntity(BDMOASDatastoreConstants.MARITAL_STATUS_INFO_ENTITY);
    maritalStatusEntity.setAttribute(
      BDMOASDatastoreConstants.MARITAL_STATUS_INFO_CURNTSTATUS_ATTR,
      BDMMARITALSTATUS.WIDOWED);
    maritalStatusEntity.setAttribute(
      BDMOASDatastoreConstants.MARITAL_STATUS_INFO_MARITALDATE_ATTR,
      "20000606");

    personEntity.addChildEntity(maritalStatusEntity);

    maritalStatusEntity.update();
    personEntity.update();
    applicationEntity.update();

    expectationsConcernRole(pdcPersonDetails.concernRoleID);

    new Expectations() {

      {
        BDMOASEvidenceUtil.getCaseParticipantRoleIDByType(anyLong, anyString);
        result = pdcPartnerPersonDetails.concernRoleID;
      }
    };

    bdmoasEvidenceMappingUtil.mapMaritalRelationshipEvidence(
      applicationEntity, pdcPersonDetails.caseID, dataStoreCEFMappings,
      SCHEMA_NAME);

    verifyCreatedOnlyAC(CASEEVIDENCE.OAS_MARITAL_RELATIONSHIP, 1);

  }

  /**
   * Test that the mapMaritalRelationshipEvidencefunction (which calls
   * createMaritalRelationshipEvidence).
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_MapAndCreateMaritalRelationshipEvidence_Separated()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final PDCPersonDetails pdcPartnerPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    setUpEntities(pdcPersonDetails.concernRoleID);
    personEntity.setAttribute(BDMOASDatastoreConstants.SPOUSE_OR_CLP_IND,
      "true");
    final Entity secondPersonEntity =
      datastore.newEntity(BDMDatastoreConstants.PERSON);
    secondPersonEntity
      .setAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT, "false");
    secondPersonEntity.setAttribute("localID",
      "" + pdcPartnerPersonDetails.concernRoleID);
    applicationEntity.addChildEntity(secondPersonEntity);
    secondPersonEntity.update();

    final Entity maritalStatusEntity = datastore
      .newEntity(BDMOASDatastoreConstants.MARITAL_STATUS_INFO_ENTITY);
    maritalStatusEntity.setAttribute(
      BDMOASDatastoreConstants.MARITAL_STATUS_INFO_CURNTSTATUS_ATTR,
      BDMMARITALSTATUS.SEPARATED);
    maritalStatusEntity.setAttribute(
      BDMOASDatastoreConstants.MARITAL_STATUS_INFO_MARITALDATE_ATTR,
      "20000606");

    personEntity.addChildEntity(maritalStatusEntity);

    maritalStatusEntity.update();
    personEntity.update();
    applicationEntity.update();

    expectationsConcernRole(pdcPersonDetails.concernRoleID);

    new Expectations() {

      {
        BDMOASEvidenceUtil.getCaseParticipantRoleIDByType(anyLong, anyString);
        result = pdcPartnerPersonDetails.concernRoleID;
      }
    };

    bdmoasEvidenceMappingUtil.mapMaritalRelationshipEvidence(
      applicationEntity, pdcPersonDetails.caseID, dataStoreCEFMappings,
      SCHEMA_NAME);

    verifyCreatedOnlyAC(CASEEVIDENCE.OAS_MARITAL_RELATIONSHIP, 1);

  }

  /**
   * Test that the mapNatureOfAbsencesEvidence function (which calls
   * createNatureOfAbsencesEvidence).
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_MapAndCreateNatureOfAbsencesEvidence()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    setUpEntities(pdcPersonDetails.concernRoleID);
    final Entity absencesEntity =
      datastore.newEntity(BDMOASDatastoreConstants.NATURE_OF_ABSENCES_ENTITY);
    absencesEntity
      .setAttribute(BDMOASDatastoreConstants.NATURE_OF_ABSENCES_ATTR, "test");

    personEntity.addChildEntity(absencesEntity);

    absencesEntity.update();
    personEntity.update();
    applicationEntity.update();

    expectationsConcernRole(pdcPersonDetails.concernRoleID);
    bdmoasEvidenceMappingUtil.mapNatureOfAbsencesEvidence(applicationEntity,
      pdcPersonDetails.caseID, dataStoreCEFMappings, SCHEMA_NAME);

    verifyCreated(CASEEVIDENCE.OAS_NATURE_OF_ABSENCES, 1);

  }

  /**
   * Test that the mapWorldIncomeEvidence function (which calls
   * createWorldIncomeEvidence).
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_MapAndCreateWorldIncomeEvidence()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    setUpEntities(pdcPersonDetails.concernRoleID);
    final Entity worldIncomeEntity =
      datastore.newEntity(BDMOASDatastoreConstants.WORLD_INCOME_ENTITY);
    worldIncomeEntity.setAttribute(BDMOASDatastoreConstants.WORLD_INCOME_ATTR,
      "test");

    personEntity.addChildEntity(worldIncomeEntity);

    worldIncomeEntity.update();
    personEntity.update();
    applicationEntity.update();

    expectationsConcernRole(pdcPersonDetails.concernRoleID);
    bdmoasEvidenceMappingUtil.mapWorldIncomeEvidence(applicationEntity,
      pdcPersonDetails.caseID, dataStoreCEFMappings, SCHEMA_NAME);

    verifyCreated(CASEEVIDENCE.OAS_WORLD_INCOME, 1);

  }

  /**
   * Test that the mapRetirementOrPensionReductionEvidence function (which calls
   * createRetirementPensionReductionEvidence).
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_MapAndCreateRetirementOrPensionReductionEvidence()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    setUpEntities(pdcPersonDetails.concernRoleID);
    final Entity retirementIncomeEntity = datastore.newEntity(
      BDMOASDatastoreConstants.RETIREMENT_PENSION_REDUCTION_ENTITY);
    retirementIncomeEntity.setAttribute(
      BDMOASDatastoreConstants.RETIREMENT_PENSION_REDUCTION_RETIRED_ORRETIRING_ATTR,
      IEGYESNO.YES);
    retirementIncomeEntity.setAttribute(
      BDMOASDatastoreConstants.RETIREMENT_PENSION_REDUCTION_REDUCED_ATTR,
      IEGYESNO.YES);
    retirementIncomeEntity.setAttribute(
      BDMOASDatastoreConstants.RETIREMENT_PENSION_REDUCTION_RETIREDATE_ATTR,
      "20230202");
    retirementIncomeEntity.setAttribute(
      BDMOASDatastoreConstants.RETIREMENT_PENSION_REDUCTION_DATE_ATTR,
      "20230202");

    personEntity.addChildEntity(retirementIncomeEntity);

    retirementIncomeEntity.update();
    personEntity.update();
    applicationEntity.update();

    expectationsConcernRole(pdcPersonDetails.concernRoleID);

    verifyCreatedBDMOAS(CASEEVIDENCEEntry.OAS_RETIREMENT_PENSION_REDUCTION,
      2);

    bdmoasEvidenceMappingUtil.mapRetirementOrPensionReductionEvidence(
      applicationEntity, pdcPersonDetails.caseID, dataStoreCEFMappings,
      SCHEMA_NAME, true);

  }

  /**
   * Test that the mapManuallyEnteredIncomeEvidence function (which calls
   * createManuallyEnteredIncomeEvidence).
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_MapAndCreateManuallyEnteredIncomeEvidence()
    throws AppException, InformationalException, NoSuchSchemaException {
    // TODO: Add other fields once file is updated

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    setUpEntities(pdcPersonDetails.concernRoleID);

    final Entity gisEntity =
      datastore.newEntity(BDMOASDatastoreConstants.GIS);

    gisEntity.setAttribute(BDMOASDatastoreConstants.GIS_APPLYGISIND_ATTR,
      IEGYESNO.YES);
    personEntity.addChildEntity(gisEntity);
    gisEntity.update();

    final Entity incomeEntity =
      datastore.newEntity(BDMOASDatastoreConstants.INCOME);

    incomeEntity.setAttribute(BDMOASDatastoreConstants.INCOME_YEAR_ATTR,
      "2021");
    personEntity.addChildEntity(incomeEntity);
    incomeEntity.update();
    personEntity.update();
    applicationEntity.update();

    expectationsConcernRole(pdcPersonDetails.concernRoleID);

    verifyCreatedBDMOAS(CASEEVIDENCEEntry.OAS_MANUALLY_ENTERED_INCOME, 1);

    bdmoasEvidenceMappingUtil.mapManuallyEnteredIncomeEvidence(
      applicationEntity, pdcPersonDetails.caseID, dataStoreCEFMappings,
      SCHEMA_NAME, true);

  }

  /**
   * Test that the mapMaritalRelationshipEvidencefunction (which calls
   * createMaritalRelationshipEvidence).
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_createLivingApartforReasonsBeyondControlEvidence(@Mocked
  final EvidenceServiceInterface evidenceServiceInterface, @Mocked
  final EvidenceGenericSLFactory EvidenceGenericSLFactory)
    throws AppException, InformationalException, NoSuchSchemaException {

    setUpEntities(394392);
    personEntity.setAttribute(BDMOASDatastoreConstants.SPOUSE_OR_CLP_IND,
      "true");

    final Entity maritalStatusEntity = datastore
      .newEntity(BDMOASDatastoreConstants.MARITAL_STATUS_INFO_ENTITY);
    maritalStatusEntity.setAttribute(
      BDMOASDatastoreConstants.MARITAL_STATUS_INFO_CURNTSTATUS_ATTR,
      BDMMARITALSTATUS.SEPARATED);
    maritalStatusEntity.setAttribute(
      BDMOASDatastoreConstants.MARITAL_STATUS_IS_SEPERATED_ATTR,
      IEGYESNO.YES);
    maritalStatusEntity.setAttribute(
      BDMOASDatastoreConstants.MARITAL_STATUS_INFO_MARITALDATE_ATTR,
      "20000606");
    maritalStatusEntity.setAttribute(
      BDMOASDatastoreConstants.SEPERATION_DATE_ATTR, "20200606");

    personEntity.addChildEntity(maritalStatusEntity);

    maritalStatusEntity.update();
    personEntity.update();
    applicationEntity.update();

    expectationsConcernRole(394392);

    new Expectations() {

      {
        curam.dynamicevidence.sl.impl.EvidenceGenericSLFactory
          .instance((curam.core.sl.struct.EvidenceTypeKey) any, (Date) any);
        result = evidenceServiceInterface;
      }
    };

    bdmoasEvidenceMappingUtil.mapLivingApartforReasonsBeyondControlEvidence(
      applicationEntity, 3294392, dataStoreCEFMappings, SCHEMA_NAME, 23930);

    try {

      new Verifications() {

        {
          evidenceServiceInterface.createEvidence(
            (curam.dynamicevidence.sl.struct.impl.GenericSLDataDetails) any);
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
   * Test that the mapConsentDeclarationEvidence function (which calls
   * createConsentDeclarationEvidence).
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_MapAndCreateConsentDeclarationEvidence()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    setUpEntities(pdcPersonDetails.concernRoleID);

    final Entity declarationEntity =
      datastore.newEntity(BDMOASDatastoreConstants.DECLARATION_ENTITY);
    declarationEntity.setAttribute(BDMOASDatastoreConstants.kDidAplicntSigned,
      IEGYESNO.YES);
    declarationEntity.setAttribute(
      BDMOASDatastoreConstants.kDidAplicntSignedWithMark, IEGYESNO.YES);
    declarationEntity
      .setAttribute(BDMOASDatastoreConstants.kAplicntSignedDate, "20201212");
    declarationEntity.setAttribute(
      BDMOASDatastoreConstants.kDidSpouseOrCLPSigned, IEGYESNO.YES);
    declarationEntity.setAttribute(
      BDMOASDatastoreConstants.kDidSpouseOrCLPSignedWithMark, IEGYESNO.YES);
    declarationEntity.setAttribute(
      BDMOASDatastoreConstants.kSpouseOrCLPSignedDate, "20201212");
    declarationEntity.setAttribute(
      BDMOASDatastoreConstants.kDidTPSignedAsWitness, IEGYESNO.YES);
    declarationEntity.setAttribute(
      BDMOASDatastoreConstants.kThirdPartySignedDate, "20201212");
    declarationEntity.setAttribute(
      BDMOASDatastoreConstants.kDidTPSignedBehalfOfAplcnt, IEGYESNO.YES);
    declarationEntity.setAttribute(
      BDMOASDatastoreConstants.kThirdPartySignedDate, "20201212");
    declarationEntity.setAttribute(
      BDMOASDatastoreConstants.kDidTPSignedBehalfOfSpouse, IEGYESNO.YES);
    declarationEntity.setAttribute(
      BDMOASDatastoreConstants.kTpSignedDateBehalfOfSpouse, "20201212");
    applicationEntity.addChildEntity(declarationEntity);

    declarationEntity.update();
    applicationEntity.update();

    expectationsConcernRole(pdcPersonDetails.concernRoleID);
    bdmoasEvidenceMappingUtil.mapConsentDeclarationEvidence(applicationEntity,
      pdcPersonDetails.caseID, dataStoreCEFMappings, SCHEMA_NAME);

    verifyCreated(CASEEVIDENCE.BDM_CONSENT_DECLARATION, 1);

  }

  /**
   * Test that the mapConsentDeclarationEvidence function (which calls
   * createConsentDeclarationEvidence).
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_MapAndCreateConsentDeclarationEvidence_Partner()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final PDCPersonDetails pdcPartnerPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    setUpEntities(pdcPersonDetails.concernRoleID);
    personEntity.setAttribute(BDMOASDatastoreConstants.SPOUSE_OR_CLP_IND,
      "true");
    final Entity secondPersonEntity =
      datastore.newEntity(BDMDatastoreConstants.PERSON);
    secondPersonEntity
      .setAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT, "false");
    secondPersonEntity.setAttribute("localID",
      "" + pdcPartnerPersonDetails.concernRoleID);
    applicationEntity.addChildEntity(secondPersonEntity);
    secondPersonEntity.update();

    final Entity declarationEntity =
      datastore.newEntity(BDMOASDatastoreConstants.DECLARATION_ENTITY);
    declarationEntity.setAttribute(BDMOASDatastoreConstants.kDidAplicntSigned,
      IEGYESNO.YES);

    declarationEntity.setAttribute(
      BDMOASDatastoreConstants.kDidSpouseOrCLPSigned, IEGYESNO.YES);

    declarationEntity.setAttribute(
      BDMOASDatastoreConstants.kSpouseOrCLPSignedDate,
      Date.getCurrentDate().toString());
    applicationEntity.addChildEntity(declarationEntity);

    declarationEntity.update();
    applicationEntity.update();

    expectationsConcernRole(pdcPartnerPersonDetails.concernRoleID);
    bdmoasEvidenceMappingUtil.mapConsentDeclarationEvidence(applicationEntity,
      pdcPersonDetails.caseID, dataStoreCEFMappings, SCHEMA_NAME);

    verifyCreated(CASEEVIDENCE.BDM_CONSENT_DECLARATION, 1);

  }

  /**
   * Test that the processAddressEvd function .
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_processAddressEvd()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final curam.participant.impl.ConcernRole concernRole =
      this.concernRoleDAO.get(pdcPersonDetails.concernRoleID);

    final String addressData = "1\r\n" + "0\r\n" + "BDMINTL\r\n" + "CA\r\n"
      + "1\r\n" + "2\r\n" + "POBOXNO=\r\n" + "APT=4324\r\n" + "ADD1=434\r\n"
      + "ADD2=NLfante Plaza\r\n" + "CITY=Montreal\r\n" + "PROV=QC\r\n"
      + "STATEPROV=\r\n" + "BDMSTPROVX=\r\n" + "COUNTRY=CA\r\n"
      + "POSTCODE=B1B1B1\r\n" + "ZIP=\r\n" + "BDMZIPX=\r\n" + "";

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new ArrayList<DynamicEvidenceDataDetails>();

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(PDCConst.PDCADDRESS,
        Date.getCurrentDate());

    dynamicEvidenceDataDetails.getAttribute("addressType")
      .setValue(CONCERNROLEADDRESSTYPE.PRIVATE);
    dynamicEvidenceDataDetails.getAttribute("address").setValue(addressData);

    evidenceDataDetailsList.add(dynamicEvidenceDataDetails);

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails2 =
      DynamicEvidenceDataDetailsFactory.newInstance(PDCConst.PDCADDRESS,
        Date.getCurrentDate());

    dynamicEvidenceDataDetails2.getAttribute("addressType")
      .setValue(CONCERNROLEADDRESSTYPE.MAILING);
    dynamicEvidenceDataDetails2.getAttribute("address").setValue(addressData);

    evidenceDataDetailsList.add(dynamicEvidenceDataDetails2);

    setUpEntities(pdcPersonDetails.concernRoleID);
    personEntity.setTypedAttribute(
      BDMDatastoreConstants.IS_MAILING_ADDRESS_DIFFERENT, IEGYESNO.YES);
    personEntity.setTypedAttribute(
      BDMDatastoreConstants.IS_MAILING_ADDRESS_UPDATE, IEGYESNO.YES);
    final Entity addressEntity =
      datastore.newEntity(BDMOASDatastoreConstants.kResidentialAddress);
    personEntity.addChildEntity(addressEntity);

    final Entity mailingEntity =
      datastore.newEntity(BDMOASDatastoreConstants.kMailingAddress);
    personEntity.addChildEntity(mailingEntity);

    mailingEntity.update();
    addressEntity.update();
    personEntity.update();
    applicationEntity.update();

    new Expectations() {

      {
        bdmEvidenceUtil.getEvdDtlsByConcernroleIDandEvidenceType(
          concernRole.getID(), PDCConst.PDCADDRESS);
        result = evidenceDataDetailsList;

        new BDMEvidenceUtil();
        result = bdmEvidenceUtil;

      }
    };

    bdmoasEvidenceMappingUtil.processAddressEvd(personEntity, concernRole);

    try {

      new Verifications() {

        {
          bdmEvidenceUtil.modifyAddressEvidence(anyLong, anyString,
            (Date) any, CONCERNROLEADDRESSTYPE.PRIVATE, Date.kZeroDate,
            anyBoolean, "", anyLong, null, CuramConst.gkEmpty,
            CuramConst.gkEmpty, CuramConst.gkEmpty, anyString);
          times = 1;

          bdmEvidenceUtil.createAddressEvidence(anyLong, anyString,
            (Date) any, CONCERNROLEADDRESSTYPE.MAILING, Date.kZeroDate, false,
            "");
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
