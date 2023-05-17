package curam.ca.gc.bdm.test.lifeevent.impl;

import curam.ca.gc.bdm.codetable.BDMAGREEATTESTATION;
import curam.ca.gc.bdm.lifeevent.impl.BDMDateofBirthEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMDateofBirthEvidenceVO;
import curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest;
import curam.ca.gc.bdm.test.rules.functions.CustomFunctionTestJunit4;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.datastore.impl.NoSuchSchemaException;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.Date;
import java.util.List;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/*
 * The Class tests {@link BDMDateofBirthEvidence}
 * custom
 * function.
 */
@RunWith(JMockit.class)
public class BDMDateofBirthEvidenceTest extends CustomFunctionTestJunit4 {

  BDMEvidenceUtilsTest bdmEvidenceUtilsTest = new BDMEvidenceUtilsTest();

  /** The validate function. */
  @Tested
  BDMDateofBirthEvidence bdmDateofBirthEvidence;

  public BDMDateofBirthEvidenceTest() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() throws NoSuchSchemaException {

    bdmDateofBirthEvidence = new BDMDateofBirthEvidence();
  }

  /**
   * test method getDOBEvidenceValueObject
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testGetDOBEvidenceValueObject()
    throws AppException, InformationalException {

    // Create Concern Role PDC
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();

    final long concernRoleID = pdcPersonDetails.concernRoleID;
    final List<BDMDateofBirthEvidenceVO> bdmIdentificationEvidenceVOList =
      bdmDateofBirthEvidence.getDOBEvidenceValueObject(concernRoleID);
    assertEquals(1, bdmIdentificationEvidenceVOList.size());
    assertEquals(pdcPersonDetails.dateOfBirth,
      bdmIdentificationEvidenceVOList.get(0).getDateOfBirth());

  }

  /**
   * test method createDateOfBirthEvidence
   * for scenario of creating Date of Birth evidence
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testCreateDateOfBirthEvidence()
    throws AppException, InformationalException {

    // Create Concern Role PDC
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();

    final BDMDateofBirthEvidenceVO bdmDateofBirthEvidenceVO =
      new BDMDateofBirthEvidenceVO();
    bdmDateofBirthEvidenceVO.setDateOfBirth(Date.getCurrentDate());
    bdmDateofBirthEvidenceVO.setDateOfDeath(Date.kZeroDate);
    List<BDMDateofBirthEvidenceVO> returnDateofBirthEvidenceList =
      bdmDateofBirthEvidence
        .getDOBEvidenceValueObject(pdcPersonDetails.concernRoleID);
    assertEquals(1, returnDateofBirthEvidenceList.size());
    assertTrue(
      returnDateofBirthEvidenceList.get(0).getBdmAgreeAttestation() == null);
    // BEGIN - User Story 21834 - Added Attestation for DOB evidence
    bdmDateofBirthEvidenceVO.setBdmAgreeAttestation(BDMAGREEATTESTATION.YES);
    bdmDateofBirthEvidenceVO.setAttestationDate(Date.getCurrentDate());
    // END - User Story 21834 - Added Attestation for DOB evidence
    // create Date of Birth evidence
    try {
      bdmDateofBirthEvidence.createDateOfBirthEvidence(
        pdcPersonDetails.concernRoleID, bdmDateofBirthEvidenceVO,
        EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);
      // retrieve the Date of Birth evidence

      returnDateofBirthEvidenceList = bdmDateofBirthEvidence
        .getDOBEvidenceValueObject(pdcPersonDetails.concernRoleID);
      assertEquals(2, returnDateofBirthEvidenceList.size());
      // BEGIN - User Story 21834 - Added Attestation for DOB evidence
      assertEquals(BDMAGREEATTESTATION.YES,
        returnDateofBirthEvidenceList.get(0).getBdmAgreeAttestation());
      assertEquals(BDMAGREEATTESTATION.YES,
        returnDateofBirthEvidenceList.get(1).getBdmAgreeAttestation());
      // END - User Story 21834 - Added Attestation for DOB evidence
    } catch (final Exception e) {
      assertFalse(false);
    }

  }

  /**
   * test method createDateOfBirthEvidence
   * for scenario of modifying Date of Birth evidence
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testCreateDateOfBirthEvidence_modifyEvidence()
    throws AppException, InformationalException {

    // Create Concern Role PDC
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();

    // retrieve the Date of Birth evidence
    final List<BDMDateofBirthEvidenceVO> dateofBirthEvidenceList =
      bdmDateofBirthEvidence
        .getDOBEvidenceValueObject(pdcPersonDetails.concernRoleID);
    assertEquals(1, dateofBirthEvidenceList.size());
    assertNotEquals(Date.getCurrentDate(),
      dateofBirthEvidenceList.get(0).getDateOfBirth());
    assertTrue(
      dateofBirthEvidenceList.get(0).getBdmAgreeAttestation() == null);

    final BDMDateofBirthEvidenceVO bdmDateofBirthEvidenceVO =
      new BDMDateofBirthEvidenceVO();
    bdmDateofBirthEvidenceVO
      .setEvidenceID(dateofBirthEvidenceList.get(0).getEvidenceID());
    bdmDateofBirthEvidenceVO.setDateOfBirth(Date.getCurrentDate());
    bdmDateofBirthEvidenceVO.setDateOfDeath(Date.kZeroDate);
    // BEGIN - User Story 21834 - Added Attestation for DOB evidence
    bdmDateofBirthEvidenceVO
      .setPerson(dateofBirthEvidenceList.get(0).getPerson());
    bdmDateofBirthEvidenceVO
      .setAttesteeCaseParticipant(dateofBirthEvidenceList.get(0).getPerson());
    bdmDateofBirthEvidenceVO.setBdmAgreeAttestation(BDMAGREEATTESTATION.YES);
    bdmDateofBirthEvidenceVO.setAttestationDate(Date.getCurrentDate());
    // END - User Story 21834 - Added Attestation for DOB evidence
    // modify Date of Birth evidence
    bdmDateofBirthEvidence.createDateOfBirthEvidence(
      pdcPersonDetails.concernRoleID, bdmDateofBirthEvidenceVO,
      EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);
    // retrieve the Date of Birth evidence
    final List<BDMDateofBirthEvidenceVO> returnDateofBirthEvidenceList =
      bdmDateofBirthEvidence
        .getDOBEvidenceValueObject(pdcPersonDetails.concernRoleID);
    assertEquals(1, returnDateofBirthEvidenceList.size());
    assertEquals(Date.getCurrentDate(),
      returnDateofBirthEvidenceList.get(0).getDateOfBirth());
    assertEquals(BDMAGREEATTESTATION.YES,
      returnDateofBirthEvidenceList.get(0).getBdmAgreeAttestation());
    assertTrue(returnDateofBirthEvidenceList.get(0).getAttestationDate()
      .equals(Date.getCurrentDate()));
  }
}
