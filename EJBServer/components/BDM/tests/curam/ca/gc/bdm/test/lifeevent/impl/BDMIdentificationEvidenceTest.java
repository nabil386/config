package curam.ca.gc.bdm.test.lifeevent.impl;

import curam.ca.gc.bdm.lifeevent.impl.BDMIdentificationEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMIdentificationEvidenceVO;
import curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest;
import curam.ca.gc.bdm.test.rules.functions.CustomFunctionTestJunit4;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.datastore.impl.NoSuchSchemaException;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.Date;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/*
 * The Class tests {@link BDMIdentificationEvidence}
 * custom
 * function.
 */
@RunWith(JMockit.class)
public class BDMIdentificationEvidenceTest extends CustomFunctionTestJunit4 {

  BDMEvidenceUtilsTest bdmEvidenceUtilsTest = new BDMEvidenceUtilsTest();

  /** The validate function. */
  @Tested
  BDMIdentificationEvidence bdmIdentificationEvidence;

  public BDMIdentificationEvidenceTest() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() throws NoSuchSchemaException {

    bdmIdentificationEvidence = new BDMIdentificationEvidence();
  }

  /**
   * test method getSINEvidenceValueObject
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testGetSINEvidenceValueObject()
    throws AppException, InformationalException {

    // Create Concern Role PDC
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();

    final long concernRoleID = pdcPersonDetails.concernRoleID;
    final BDMIdentificationEvidenceVO bdmIdentificationEvidenceVO =
      bdmIdentificationEvidence.getSINEvidenceValueObject(concernRoleID);
    assertEquals(0, bdmIdentificationEvidenceVO.getEvidenceID());

  }

  /**
   * test method createIdentificationEvidence
   * for scenario of creating name evidence
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testCreateIdentificationEvidence()
    throws AppException, InformationalException {

    final String kSin = "145818308";
    // Create Concern Role PDC
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();

    final BDMIdentificationEvidenceVO bdmIdentificationEvidenceVO =
      new BDMIdentificationEvidenceVO();
    bdmIdentificationEvidenceVO
      .setAltIDType(CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER);
    bdmIdentificationEvidenceVO.setAlternateID(kSin);
    bdmIdentificationEvidenceVO.setFromDate(Date.getCurrentDate());
    bdmIdentificationEvidenceVO.setToDate(Date.kZeroDate);
    // create identification evidence
    bdmIdentificationEvidence.createIdentificationEvidence(
      pdcPersonDetails.concernRoleID, bdmIdentificationEvidenceVO,
      EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);
    // retrieve the identification evidence
    final BDMIdentificationEvidenceVO returnIdentificationEvidence =
      bdmIdentificationEvidence
        .getSINEvidenceValueObject(pdcPersonDetails.concernRoleID);
    // assert the result
    assertEquals(CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER,
      returnIdentificationEvidence.getAltIDType());
    assertEquals(kSin, returnIdentificationEvidence.getAlternateID());

  }

  /**
   * test method createIdentificationEvidence
   * for scenario of modifying evidence
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Ignore
  @Test
  public void testCreateIdentificationEvidence_modifyEvidence()
    throws AppException, InformationalException {

    final String kSin = "145818308";
    // Create Concern Role PDC
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();

    final BDMIdentificationEvidenceVO bdmIdentificationEvidenceVO =
      new BDMIdentificationEvidenceVO();
    bdmIdentificationEvidenceVO
      .setAltIDType(CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER);
    bdmIdentificationEvidenceVO.setAlternateID(kSin);
    bdmIdentificationEvidenceVO.setFromDate(Date.getCurrentDate());
    bdmIdentificationEvidenceVO.setToDate(Date.kZeroDate);
    // create identification evidence
    bdmIdentificationEvidence.createIdentificationEvidence(
      pdcPersonDetails.concernRoleID, bdmIdentificationEvidenceVO,
      EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);
    // retrieve the identification evidence
    BDMIdentificationEvidenceVO returnIdentificationEvidence =
      bdmIdentificationEvidence
        .getSINEvidenceValueObject(pdcPersonDetails.concernRoleID);
    // assert the result
    assertEquals(CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER,
      returnIdentificationEvidence.getAltIDType());
    assertEquals(kSin, returnIdentificationEvidence.getAlternateID());
    assertEquals(Date.kZeroDate, returnIdentificationEvidence.getToDate());

    final BDMIdentificationEvidenceVO identificationEvidenceVOForModify =
      new BDMIdentificationEvidenceVO();
    identificationEvidenceVOForModify
      .setEvidenceID(returnIdentificationEvidence.getEvidenceID());
    identificationEvidenceVOForModify
      .setAltIDType(CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER);
    identificationEvidenceVOForModify.setAlternateID(kSin);
    identificationEvidenceVOForModify.setFromDate(Date.getCurrentDate());
    identificationEvidenceVOForModify.setToDate(Date.getCurrentDate());
    // create identification evidence
    try {
      bdmIdentificationEvidence.createIdentificationEvidence(
        pdcPersonDetails.concernRoleID, identificationEvidenceVOForModify,
        EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);

      // retrieve the identification evidence
      returnIdentificationEvidence = bdmIdentificationEvidence
        .getSINEvidenceValueObject(pdcPersonDetails.concernRoleID);
      // assert the result
      assertEquals(CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER,
        returnIdentificationEvidence.getAltIDType());
      assertEquals(kSin, returnIdentificationEvidence.getAlternateID());
      assertEquals(Date.getCurrentDate(),
        returnIdentificationEvidence.getToDate());
    } catch (final InformationalException e) {
      assertEquals(e.getMessage(),
        "An active SIN number is present in the system. Please end date the existing SIN number to add a new SIN number.");
    }

  }
}
