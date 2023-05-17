package curam.ca.gc.bdm.test.lifeevent.impl;

import curam.ca.gc.bdm.codetable.BDMEMAILADDRESSCHANGETYPE;
import curam.ca.gc.bdm.codetable.BDMPHONENUMBERCHANGETYPE;
import curam.ca.gc.bdm.lifeevent.impl.BDMEmailEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMEmailEvidenceVO;
import curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest;
import curam.ca.gc.bdm.test.rules.functions.CustomFunctionTestJunit4;
import curam.codetable.EMAILTYPE;
import curam.codetable.EVIDENCECHANGEREASON;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/*
 * The Class tests {@link BDMEmailEvidence}
 * custom
 * function.
 */
@RunWith(JMockit.class)
public class BDMEmailEvidenceTest extends CustomFunctionTestJunit4 {

  BDMEvidenceUtilsTest bdmEvidenceUtilsTest = new BDMEvidenceUtilsTest();

  final String kEmailAddress = "testemail@gmail.com";

  /** The validate function. */
  @Tested
  BDMEmailEvidence bdmEmailEvidenceTest;

  public BDMEmailEvidenceTest() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() throws NoSuchSchemaException {

    bdmEmailEvidenceTest = new BDMEmailEvidence();
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
    final List<BDMEmailEvidenceVO> bdmEmailEvidenceVOList =
      bdmEmailEvidenceTest.getEvidenceValueObject(concernRoleID);
    assertEquals(1, bdmEmailEvidenceVOList.size());

  }

  /**
   * test method createEmailEvidence
   * for scenario of creating email evidence
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testCreateEmailEvidence()
    throws AppException, InformationalException {

    // Create Concern Role PDC
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();

    final List<BDMEmailEvidenceVO> evidenceDetailsList =
      new ArrayList<BDMEmailEvidenceVO>();
    final BDMEmailEvidenceVO bdmEmailEvidenceVO = new BDMEmailEvidenceVO();
    bdmEmailEvidenceVO.setEmailAddress(kEmailAddress);
    bdmEmailEvidenceVO.setFromDate(Date.getCurrentDate());
    bdmEmailEvidenceVO.setEmailAddressType(EMAILTYPE.PERSONAL);
    bdmEmailEvidenceVO.setFromDate(Date.getCurrentDate());
    bdmEmailEvidenceVO.setToDate(Date.kZeroDate);
    // Task 25720- Email address life event rework
    bdmEmailEvidenceVO.setEmailAddressType(BDMPHONENUMBERCHANGETYPE.ADD);
    evidenceDetailsList.add(bdmEmailEvidenceVO);
    // create email evidence
    bdmEmailEvidenceTest.createEmailEvidence(pdcPersonDetails.concernRoleID,
      evidenceDetailsList, EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);
    // retrieve the email evidence
    final List<BDMEmailEvidenceVO> returnEmailEvidenceList =
      bdmEmailEvidenceTest
        .getEvidenceValueObject(pdcPersonDetails.concernRoleID);
    // assert the result
    assertEquals(2, returnEmailEvidenceList.size());
    assertTrue(
      returnEmailEvidenceList.get(0).getEmailAddress().equals(kEmailAddress)
        || returnEmailEvidenceList.get(1).getEmailAddress()
          .equals(kEmailAddress));

  }

  /**
   * test method createEmailEvidence
   * for scenario of modifying email evidence
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testCreateEmailEvidence_modify()
    throws AppException, InformationalException {

    // Create Concern Role PDC
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();

    final long concernRoleID = pdcPersonDetails.concernRoleID;
    final List<BDMEmailEvidenceVO> bdmEmailEvidenceVOList =
      bdmEmailEvidenceTest.getEvidenceValueObject(concernRoleID);
    assertEquals(1, bdmEmailEvidenceVOList.size());
    assertFalse(
      bdmEmailEvidenceVOList.get(0).getEmailAddress().equals(kEmailAddress));

    final List<BDMEmailEvidenceVO> evidenceDetailsList =
      new ArrayList<BDMEmailEvidenceVO>();
    final BDMEmailEvidenceVO bdmEmailEvidenceVO = new BDMEmailEvidenceVO();
    bdmEmailEvidenceVO
      .setEvidenceID(bdmEmailEvidenceVOList.get(0).getEvidenceID());
    // Task 25720- Email address life event rework
    bdmEmailEvidenceVO
      .setEmailAddressChangeType(BDMEMAILADDRESSCHANGETYPE.UPDATE);
    bdmEmailEvidenceVO.setEmailAddress(kEmailAddress);
    bdmEmailEvidenceVO.setFromDate(Date.getCurrentDate());
    bdmEmailEvidenceVO.setEmailAddressType(EMAILTYPE.PERSONAL);
    bdmEmailEvidenceVO.setFromDate(Date.getCurrentDate());
    bdmEmailEvidenceVO.setToDate(Date.kZeroDate);
    evidenceDetailsList.add(bdmEmailEvidenceVO);
    // create email evidence
    bdmEmailEvidenceTest.createEmailEvidence(pdcPersonDetails.concernRoleID,
      evidenceDetailsList, EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);
    // retrieve the email evidence
    final List<BDMEmailEvidenceVO> returnEmailEvidenceList =
      bdmEmailEvidenceTest
        .getEvidenceValueObject(pdcPersonDetails.concernRoleID);
    // assert the result
    assertEquals(1, returnEmailEvidenceList.size());
    assertTrue(
      returnEmailEvidenceList.get(0).getEmailAddress().equals(kEmailAddress));

  }
}
