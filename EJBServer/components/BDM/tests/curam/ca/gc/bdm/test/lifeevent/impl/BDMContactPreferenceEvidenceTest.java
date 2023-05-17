package curam.ca.gc.bdm.test.lifeevent.impl;

import curam.ca.gc.bdm.lifeevent.impl.BDMContactPreferenceEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMContactPreferenceEvidenceVO;
import curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest;
import curam.ca.gc.bdm.test.rules.functions.CustomFunctionTestJunit4;
import curam.codetable.COMMUNICATIONMETHOD;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.LANGUAGE;
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

/*
 * The Class tests {@link BDMContactPreferenceEvidence}
 * custom
 * function.
 */
@RunWith(JMockit.class)
public class BDMContactPreferenceEvidenceTest
  extends CustomFunctionTestJunit4 {

  BDMEvidenceUtilsTest bdmEvidenceUtilsTest = new BDMEvidenceUtilsTest();

  /** The validate function. */
  @Tested
  BDMContactPreferenceEvidence bdmContactPreferenceEvidence;

  public BDMContactPreferenceEvidenceTest() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() throws NoSuchSchemaException {

    bdmContactPreferenceEvidence = new BDMContactPreferenceEvidence();
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
    final List<BDMContactPreferenceEvidenceVO> bdmContactPreferenceEvidenceVOList =
      bdmContactPreferenceEvidence.getEvidenceValueObject(concernRoleID);
    assertEquals(0, bdmContactPreferenceEvidenceVOList.size());

  }

  /**
   * test method createContactPreference
   * for scenario of creating Contact preference evidence
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testCreateContactPreference()
    throws AppException, InformationalException {

    // Create Concern Role PDC
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();

    final BDMContactPreferenceEvidenceVO bdmContactPreferenceEvidenceVO =
      new BDMContactPreferenceEvidenceVO();
    bdmContactPreferenceEvidenceVO.setPreferredLanguage(LANGUAGE.FRENCH);
    bdmContactPreferenceEvidenceVO
      .setPreferredCommunication(COMMUNICATIONMETHOD.PHONE);
    bdmContactPreferenceEvidenceVO.setPreferredOralLanguage(LANGUAGE.FRENCH);
    bdmContactPreferenceEvidenceVO
      .setPreferredWrittenLanguage(LANGUAGE.FRENCH);
    // create contact preference evidence
    bdmContactPreferenceEvidence.createContactPreference(
      pdcPersonDetails.concernRoleID, bdmContactPreferenceEvidenceVO,
      EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);
    // retrieve the contact preference evidence
    final List<BDMContactPreferenceEvidenceVO> bdmContactPreferenceEvidenceVOList =
      bdmContactPreferenceEvidence
        .getEvidenceValueObject(pdcPersonDetails.concernRoleID);
    // assert the result
    assertEquals(1, bdmContactPreferenceEvidenceVOList.size());
    assertEquals(LANGUAGE.FRENCH,
      bdmContactPreferenceEvidenceVOList.get(0).getPreferredLanguage());

  }

  /**
   * test method createDateOfBirthEvidence
   * for scenario of modifying Contact preference evidence
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testCreateContactPreference_modifyEvidence()
    throws AppException, InformationalException {

    // Create Concern Role PDC
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();

    final BDMContactPreferenceEvidenceVO bdmContactPreferenceEvidenceVO =
      new BDMContactPreferenceEvidenceVO();
    bdmContactPreferenceEvidenceVO.setPreferredLanguage(LANGUAGE.FRENCH);
    bdmContactPreferenceEvidenceVO
      .setPreferredCommunication(COMMUNICATIONMETHOD.PHONE);
    bdmContactPreferenceEvidenceVO.setPreferredOralLanguage(LANGUAGE.FRENCH);
    bdmContactPreferenceEvidenceVO
      .setPreferredWrittenLanguage(LANGUAGE.FRENCH);
    // create contact preference evidence
    bdmContactPreferenceEvidence.createContactPreference(
      pdcPersonDetails.concernRoleID, bdmContactPreferenceEvidenceVO,
      EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);
    // retrieve the contact preference evidence
    List<BDMContactPreferenceEvidenceVO> bdmContactPreferenceEvidenceVOList =
      bdmContactPreferenceEvidence
        .getEvidenceValueObject(pdcPersonDetails.concernRoleID);
    // assert the result
    assertEquals(1, bdmContactPreferenceEvidenceVOList.size());
    assertEquals(LANGUAGE.FRENCH,
      bdmContactPreferenceEvidenceVOList.get(0).getPreferredLanguage());

    final BDMContactPreferenceEvidenceVO contactPreferenceEvidenceVOFooModify =
      new BDMContactPreferenceEvidenceVO();
    contactPreferenceEvidenceVOFooModify.setEvidenceID(
      bdmContactPreferenceEvidenceVOList.get(0).getEvidenceID());
    contactPreferenceEvidenceVOFooModify
      .setPreferredLanguage(LANGUAGE.ENGLISH);
    contactPreferenceEvidenceVOFooModify
      .setPreferredCommunication(COMMUNICATIONMETHOD.PHONE);
    contactPreferenceEvidenceVOFooModify
      .setPreferredOralLanguage(LANGUAGE.ENGLISH);
    contactPreferenceEvidenceVOFooModify
      .setPreferredWrittenLanguage(LANGUAGE.ENGLISH);
    bdmContactPreferenceEvidenceVOList = bdmContactPreferenceEvidence
      .getEvidenceValueObject(pdcPersonDetails.concernRoleID);
    // assert the result
    assertEquals(1, bdmContactPreferenceEvidenceVOList.size());
    assertEquals(LANGUAGE.FRENCH,
      bdmContactPreferenceEvidenceVOList.get(0).getPreferredLanguage());

  }
}
