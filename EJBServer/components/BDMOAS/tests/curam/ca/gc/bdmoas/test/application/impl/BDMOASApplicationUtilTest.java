package curam.ca.gc.bdmoas.test.application.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.lifeevent.impl.BDMPhoneEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMPhoneEvidenceVO;
import curam.ca.gc.bdmoas.application.impl.BDMOASApplicationUtil;
import curam.ca.gc.bdmoas.application.impl.BDMOASDatastoreConstants;
import curam.ca.gc.bdmoas.test.evidence.BDMEvidenceUtilsTest;
import curam.ca.gc.bdmoas.test.rules.functions.CustomFunctionTestJunit4;
import curam.codetable.COUNTRY;
import curam.codetable.COUNTRYCODE;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.PHONETYPE;
import curam.creole.value.CodeTableItem;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.participant.impl.ConcernRole;
import curam.participant.impl.ConcernRoleDAO;
import curam.pdc.impl.PDCConst;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import java.util.ArrayList;
import java.util.List;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * The Class tests {@link BDMOASApplicationUtil} custom
 * function.
 */

@RunWith(JMockit.class)
public class BDMOASApplicationUtilTest extends CustomFunctionTestJunit4 {

  private static final String SCHEMA_NAME = "BDMOASGISApplicationSchema";

  /** The entity functions. */
  @Mocked
  BDMEvidenceUtil bdmEvidenceUtil;

  @Mocked
  BDMApplicationEventsUtil BDMApplicationEventsUtil;

  @Mocked
  BDMPhoneEvidence bdmPhoneEvidence;

  Datastore datastore;

  Entity applicationEntity;

  Entity personEntity;

  /** The testing functions. */

  BDMEvidenceUtilsTest bdmEvidenceUtils;

  @Inject
  private ConcernRoleDAO concernRoleDAO;

  /** The validated function. */
  @Tested
  BDMOASApplicationUtil BDMOASApplicationUtil;

  public BDMOASApplicationUtilTest() {

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

  /**
   * Test that the processPhoneEvd.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_processPhoneEvd()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final ConcernRole concernRole =
      this.concernRoleDAO.get(pdcPersonDetails.concernRoleID);

    applicationEntity =
      datastore.newEntity(BDMDatastoreConstants.APPLICATION);
    datastore.addRootEntity(applicationEntity);

    personEntity = datastore.newEntity(BDMDatastoreConstants.PERSON);
    personEntity.setAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT,
      "true");
    personEntity.setAttribute("localID", "" + pdcPersonDetails.concernRoleID);
    personEntity.setAttribute(BDMOASDatastoreConstants.COUNTRY_OF_BIRTH,
      COUNTRY.CA);
    applicationEntity.addChildEntity(personEntity);

    final Entity communicationsEntity1 =
      datastore.newEntity(BDMDatastoreConstants.COMMUNICATION_DETAILS);
    communicationsEntity1.setAttribute(BDMDatastoreConstants.PHONE_NUMBER,
      "1234567");
    communicationsEntity1.setAttribute(BDMDatastoreConstants.IS_PRIMARY_COMM,
      "true");
    communicationsEntity1.setAttribute(BDMDatastoreConstants.COUNTRY_CODE,
      COUNTRYCODE.CACODE);
    communicationsEntity1.setAttribute(BDMDatastoreConstants.PHONE_AREA_CODE,
      "919");
    communicationsEntity1.setAttribute(BDMDatastoreConstants.PHONE_TYPE,
      PHONETYPE.PERSONAL);
    communicationsEntity1.setAttribute(BDMDatastoreConstants.PHONE_EXT, "");
    communicationsEntity1.setAttribute(BDMDatastoreConstants.PHONE_IS_MOR,
      "true");
    communicationsEntity1.setAttribute(BDMDatastoreConstants.PHONE_IS_AFTNOON,
      "false");
    personEntity.addChildEntity(communicationsEntity1);
    communicationsEntity1.update();

    final Entity communicationsEntity2 =
      datastore.newEntity(BDMDatastoreConstants.COMMUNICATION_DETAILS);
    communicationsEntity2.setAttribute(BDMDatastoreConstants.PHONE_NUMBER,
      "7654321");
    communicationsEntity2.setAttribute(BDMDatastoreConstants.IS_PRIMARY_COMM,
      "false");
    communicationsEntity2.setAttribute(BDMDatastoreConstants.COUNTRY_CODE,
      COUNTRYCODE.CACODE);
    communicationsEntity2.setAttribute(BDMDatastoreConstants.PHONE_AREA_CODE,
      "818");
    communicationsEntity2.setAttribute(BDMDatastoreConstants.PHONE_TYPE,
      PHONETYPE.PERSONAL);
    communicationsEntity2.setAttribute(BDMDatastoreConstants.PHONE_EXT, "");
    communicationsEntity2.setAttribute(BDMDatastoreConstants.PHONE_IS_AFTNOON,
      "true");
    communicationsEntity2.setAttribute(BDMDatastoreConstants.PHONE_IS_EVE,
      "false");
    personEntity.addChildEntity(communicationsEntity2);
    communicationsEntity2.update();
    personEntity.update();
    applicationEntity.update();

    final Entity[] communicationEntities =
      {communicationsEntity1, communicationsEntity2 };

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new ArrayList<DynamicEvidenceDataDetails>();

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails1 =
      DynamicEvidenceDataDetailsFactory.newInstance(PDCConst.PDCPHONENUMBER,
        Date.getCurrentDate());
    dynamicEvidenceDataDetails1.setID(pdcPersonDetails.concernRoleID);
    dynamicEvidenceDataDetails1.getAttribute("phoneAreaCode").setValue("818");
    dynamicEvidenceDataDetails1.getAttribute("phoneExtension").setValue("");
    dynamicEvidenceDataDetails1.getAttribute("phoneNumber")
      .setValue("7654321");
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails1.getAttribute("phoneCountryCode"),
      new CodeTableItem(COUNTRYCODE.TABLENAME, COUNTRY.CA));
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails1.getAttribute("phoneType"),
      new CodeTableItem(PHONETYPE.TABLENAME, PHONETYPE.PERSONAL));
    evidenceDataDetailsList.add(dynamicEvidenceDataDetails1);

    new Expectations() {

      {
        bdmEvidenceUtil.getEvdDtlsByConcernroleIDandEvidenceType(
          concernRole.getID(), PDCConst.PDCPHONENUMBER);
        result = evidenceDataDetailsList;

        curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil
          .retrieveChildEntities(personEntity,
            BDMDatastoreConstants.COMMUNICATION_DETAILS);
        result = communicationEntities;

      }
    };

    BDMOASApplicationUtil.processPhoneEvd(personEntity, concernRole);

    new Verifications() {

      {
        bdmPhoneEvidence.createPhoneEvidence(anyLong,
          (List<BDMPhoneEvidenceVO>) any,
          EVIDENCECHANGEREASON.REPORTEDBYCLIENT);
      }
    };
  }

}
