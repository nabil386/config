package curam.ca.gc.bdm.test.lifeevent.impl;

import curam.ca.gc.bdm.codetable.BDMPHONECOUNTRY;
import curam.ca.gc.bdm.codetable.BDMPHONENUMBERCHANGETYPE;
import curam.ca.gc.bdm.lifeevent.impl.BDMPhoneEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMPhoneEvidenceVO;
import curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest;
import curam.ca.gc.bdm.test.rules.functions.CustomFunctionTestJunit4;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.PHONETYPE;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.datastore.impl.NoSuchSchemaException;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.Date;
import java.util.ArrayList;
import java.util.List;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/*
 * The Class tests {@link BDMPhoneEvidence}
 * custom
 * function.
 */
@RunWith(JMockit.class)
public class BDMPhoneEvidenceTest extends CustomFunctionTestJunit4 {

  final String kPhoneNumber = "1234567";

  final String kAreaCode = "604";

  final String kPhoneExtension = "";

  final boolean kPreferredInd = true;

  final boolean kReceiveAlertInd = true;

  BDMEvidenceUtilsTest bdmEvidenceUtilsTest = new BDMEvidenceUtilsTest();

  /** The validate function. */
  @Tested
  BDMPhoneEvidence bdmPhoneEvidence;

  public BDMPhoneEvidenceTest() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() throws NoSuchSchemaException {

    bdmPhoneEvidence = new BDMPhoneEvidence();

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

    final EIEvidenceKey phoneEvidkey =
      bdmEvidenceUtilsTest.createPhoneEvidence(pdcPersonDetails,
        BDMPHONECOUNTRY.CANADA, kPhoneNumber, kAreaCode, kPhoneExtension,
        PHONETYPE.PERSONAL_MOBILE, kPreferredInd, kReceiveAlertInd);

    final long concernRoleID = pdcPersonDetails.concernRoleID;
    final List<BDMPhoneEvidenceVO> bdmPhoneEvidenceList =
      bdmPhoneEvidence.getEvidenceValueObject(concernRoleID);
    assertEquals(1, bdmPhoneEvidenceList.size());
    assertEquals(PHONETYPE.PERSONAL_MOBILE,
      bdmPhoneEvidenceList.get(0).getPhoneType());
    assertEquals(kPhoneNumber, bdmPhoneEvidenceList.get(0).getPhoneNumber());
    assertEquals(kAreaCode, bdmPhoneEvidenceList.get(0).getPhoneAreaCode());

  }

  /**
   * test method createPhoneEvidence
   * for scenario of creating phone evidence
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testCreatePhoneEvidence()
    throws AppException, InformationalException {

    // Create Concern Role PDC
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();

    final List<BDMPhoneEvidenceVO> bdmPhoneEvidenceList =
      new ArrayList<BDMPhoneEvidenceVO>();
    final BDMPhoneEvidenceVO bdmPhoneEvidenceVO = new BDMPhoneEvidenceVO();
    bdmPhoneEvidenceVO.setPhoneCountryCode(BDMPHONECOUNTRY.CANADA);
    bdmPhoneEvidenceVO.setPhoneType(PHONETYPE.PERSONAL_MOBILE);
    bdmPhoneEvidenceVO.setPhoneAreaCode(kAreaCode);
    bdmPhoneEvidenceVO.setPhoneNumber(kPhoneNumber);
    bdmPhoneEvidenceVO.setFromDate(Date.getCurrentDate());
    bdmPhoneEvidenceList.add(bdmPhoneEvidenceVO);
    bdmPhoneEvidence.createPhoneEvidence(pdcPersonDetails.concernRoleID,
      bdmPhoneEvidenceList,
      EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);
    final List<BDMPhoneEvidenceVO> returnPhoneList =
      bdmPhoneEvidence.getEvidenceValueObject(pdcPersonDetails.concernRoleID);
    assertEquals(1, returnPhoneList.size());
    assertEquals(PHONETYPE.PERSONAL_MOBILE,
      returnPhoneList.get(0).getPhoneType());

  }

  /**
   * test method createPhoneEvidence
   * for scenario of modifying phone evidence
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testCreatePhoneEvidence_modifyEvidence()
    throws AppException, InformationalException {

    // Create Concern Role PDC
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();

    final List<BDMPhoneEvidenceVO> bdmPhoneEvidenceList =
      new ArrayList<BDMPhoneEvidenceVO>();
    final BDMPhoneEvidenceVO bdmPhoneEvidenceVO = new BDMPhoneEvidenceVO();
    bdmPhoneEvidenceVO.setPhoneCountryCode(BDMPHONECOUNTRY.CANADA);
    bdmPhoneEvidenceVO.setPhoneType(PHONETYPE.PERSONAL_MOBILE);
    bdmPhoneEvidenceVO.setPhoneAreaCode(kAreaCode);
    bdmPhoneEvidenceVO.setPhoneNumber(kPhoneNumber);
    bdmPhoneEvidenceVO.setFromDate(Date.getCurrentDate());
    bdmPhoneEvidenceList.add(bdmPhoneEvidenceVO);
    bdmPhoneEvidence.createPhoneEvidence(pdcPersonDetails.concernRoleID,
      bdmPhoneEvidenceList,
      EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);
    List<BDMPhoneEvidenceVO> returnPhoneList =
      bdmPhoneEvidence.getEvidenceValueObject(pdcPersonDetails.concernRoleID);
    assertEquals(1, returnPhoneList.size());
    assertEquals(PHONETYPE.PERSONAL_MOBILE,
      returnPhoneList.get(0).getPhoneType());

    final List<BDMPhoneEvidenceVO> bdmPhoneEvidenceListForModify =
      new ArrayList<BDMPhoneEvidenceVO>();
    final BDMPhoneEvidenceVO bdmPhoneEvidenceVOForModify =
      new BDMPhoneEvidenceVO();
    bdmPhoneEvidenceVOForModify
      .setEvidenceID(returnPhoneList.get(0).getEvidenceID());
    // Task 21654 - Phone number life event rework
    bdmPhoneEvidenceVOForModify
      .setPhoneNumberChangeType(BDMPHONENUMBERCHANGETYPE.UPDATE);
    bdmPhoneEvidenceVOForModify.setPhoneCountryCode(BDMPHONECOUNTRY.CANADA);
    bdmPhoneEvidenceVOForModify.setPhoneType(PHONETYPE.BUSINESS_MOBILE);
    bdmPhoneEvidenceVOForModify.setPhoneAreaCode(kAreaCode);
    bdmPhoneEvidenceVOForModify.setPhoneNumber(kPhoneNumber);
    bdmPhoneEvidenceVOForModify.setFromDate(Date.getCurrentDate());
    bdmPhoneEvidenceListForModify.add(bdmPhoneEvidenceVOForModify);
    bdmPhoneEvidence.createPhoneEvidence(pdcPersonDetails.concernRoleID,
      bdmPhoneEvidenceListForModify,
      EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);
    returnPhoneList =
      bdmPhoneEvidence.getEvidenceValueObject(pdcPersonDetails.concernRoleID);
    assertEquals(1, returnPhoneList.size());
    assertEquals(PHONETYPE.BUSINESS_MOBILE,
      returnPhoneList.get(0).getPhoneType());
  }

  /**
   * test method createPhoneEvidence
   * for scenario of removing phone evidence
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testCreatePhoneEvidence_removeEvidence()
    throws AppException, InformationalException {

    // Create Concern Role PDC
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();

    final List<BDMPhoneEvidenceVO> bdmPhoneEvidenceList =
      new ArrayList<BDMPhoneEvidenceVO>();
    final BDMPhoneEvidenceVO bdmPhoneEvidenceVO = new BDMPhoneEvidenceVO();
    bdmPhoneEvidenceVO.setPhoneCountryCode(BDMPHONECOUNTRY.CANADA);
    bdmPhoneEvidenceVO.setPhoneType(PHONETYPE.PERSONAL_MOBILE);
    bdmPhoneEvidenceVO.setPhoneAreaCode(kAreaCode);
    bdmPhoneEvidenceVO.setPhoneNumber(kPhoneNumber);
    bdmPhoneEvidenceVO.setFromDate(Date.getCurrentDate());
    bdmPhoneEvidenceList.add(bdmPhoneEvidenceVO);
    bdmPhoneEvidence.createPhoneEvidence(pdcPersonDetails.concernRoleID,
      bdmPhoneEvidenceList,
      EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);
    List<BDMPhoneEvidenceVO> returnPhoneList =
      bdmPhoneEvidence.getEvidenceValueObject(pdcPersonDetails.concernRoleID);
    assertEquals(1, returnPhoneList.size());
    assertEquals(PHONETYPE.PERSONAL_MOBILE,
      returnPhoneList.get(0).getPhoneType());

    final List<BDMPhoneEvidenceVO> bdmPhoneEvidenceListForDelete =
      new ArrayList<BDMPhoneEvidenceVO>();

    bdmPhoneEvidenceVO.setEvidenceID(returnPhoneList.get(0).getEvidenceID());
    // Task 21654 - Phone number life event rework
    bdmPhoneEvidenceVO
      .setPhoneNumberChangeType(BDMPHONENUMBERCHANGETYPE.REMOVE);
    bdmPhoneEvidenceListForDelete.add(bdmPhoneEvidenceVO);
    bdmPhoneEvidence.createPhoneEvidence(pdcPersonDetails.concernRoleID,
      bdmPhoneEvidenceListForDelete,
      EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);
    returnPhoneList =
      bdmPhoneEvidence.getEvidenceValueObject(pdcPersonDetails.concernRoleID);
    assertEquals(0, returnPhoneList.size());
  }
}
