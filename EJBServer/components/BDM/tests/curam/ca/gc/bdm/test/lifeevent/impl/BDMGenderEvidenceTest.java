package curam.ca.gc.bdm.test.lifeevent.impl;

import curam.ca.gc.bdm.lifeevent.impl.BDMGenderEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMGenderEvidenceVO;
import curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest;
import curam.ca.gc.bdm.test.rules.functions.CustomFunctionTestJunit4;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.GENDER;
import curam.datastore.impl.NoSuchSchemaException;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import java.util.List;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

@RunWith(JMockit.class)
public class BDMGenderEvidenceTest extends CustomFunctionTestJunit4 {

  BDMEvidenceUtilsTest bdmEvidenceUtilsTest = new BDMEvidenceUtilsTest();

  /** The validate function. */
  @Tested
  BDMGenderEvidence bdmGenderEvidence;

  public BDMGenderEvidenceTest() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() throws NoSuchSchemaException {

    bdmGenderEvidence = new BDMGenderEvidence();
  }

  /**
   * test method getEvidenceValueObject
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testGetEvidenceValueObject()
    throws AppException, InformationalException {

    // Create Concern Role PDC
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();

    final long concernRoleID = pdcPersonDetails.concernRoleID;
    final List<BDMGenderEvidenceVO> bdmGenderEvidenceVOList =
      bdmGenderEvidence.getGenderEvidenceValueObject(concernRoleID);
    assertEquals(1, bdmGenderEvidenceVOList.size());

  }

  /**
   * test method createGenderEvidence
   * for scenario of creating gender evidence
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testCreateGenderEvidence()
    throws AppException, InformationalException {

    // Create Concern Role PDC
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();

    final BDMGenderEvidenceVO bdmGenderEvidenceVO = new BDMGenderEvidenceVO();

    bdmGenderEvidenceVO.setGender(GENDER.MALE);

    bdmGenderEvidence.createGenderEvidence(pdcPersonDetails.concernRoleID,
      bdmGenderEvidenceVO, EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);
    final List<BDMGenderEvidenceVO> returnGenderList = bdmGenderEvidence
      .getGenderEvidenceValueObject(pdcPersonDetails.concernRoleID);
    assertEquals(2, returnGenderList.size());
    assertEquals(GENDER.MALE, returnGenderList.get(0).getGender());

  }

  /**
   * test method createDateOfBirthEvidence
   * for scenario of modifying Contact preference evidence
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testCreateGender_modifyEvidence()
    throws AppException, InformationalException {

    // Create Concern Role PDC
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();

    final BDMGenderEvidenceVO bdmGenderEvidenceVO = new BDMGenderEvidenceVO();

    bdmGenderEvidenceVO.setGender(GENDER.MALE);

    bdmGenderEvidence.createGenderEvidence(pdcPersonDetails.concernRoleID,
      bdmGenderEvidenceVO, EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);
    List<BDMGenderEvidenceVO> returnGenderList = bdmGenderEvidence
      .getGenderEvidenceValueObject(pdcPersonDetails.concernRoleID);
    assertEquals(2, returnGenderList.size());
    assertEquals(GENDER.MALE, returnGenderList.get(0).getGender());

    final BDMGenderEvidenceVO bdmGenderEvidenceVOModify =
      new BDMGenderEvidenceVO();

    bdmGenderEvidenceVOModify.setGender(GENDER.FEMALE);
    bdmGenderEvidenceVOModify
      .setEvidenceID(returnGenderList.get(0).getEvidenceID());

    bdmGenderEvidence.createGenderEvidence(pdcPersonDetails.concernRoleID,
      bdmGenderEvidenceVOModify,
      EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);

    returnGenderList = bdmGenderEvidence
      .getGenderEvidenceValueObject(pdcPersonDetails.concernRoleID);
    assertEquals(3, returnGenderList.size());
    assertEquals(GENDER.MALE, returnGenderList.get(0).getGender());

  }

}
