package curam.ca.gc.bdmoas.test.lifeevent.impl;

import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdmoas.codetable.BDMOASLEGALSTATUS;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASLegalStatusEvidence;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASLegalStatusEvidenceVO;
import curam.ca.gc.bdmoas.test.evidence.BDMEvidenceUtilsTest;
import curam.codetable.CASEEVIDENCE;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.pdc.struct.PDCPersonDetails;
import curam.testframework.CuramServerTestJUnit4;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import java.util.ArrayList;
import java.util.HashMap;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)
public class BDMOASLegalStatusEvidenceTest extends CuramServerTestJUnit4 {

  @Mocked
  BDMEvidenceUtil BDMEvidenceUtil;

  @Tested
  BDMOASLegalStatusEvidence BDMOASLegalStatusEvidence;

  /** The testing functions. */
  BDMEvidenceUtilsTest bdmEvidenceUtils;

  @Before
  public void setUp() throws AppException, InformationalException {

    bdmEvidenceUtils = new BDMEvidenceUtilsTest();
    BDMEvidenceUtil = new BDMEvidenceUtil();

  }

  private List<BDMOASLegalStatusEvidenceVO> setUpVOList(final long evidenceID)
    throws AppException, InformationalException {

    final List<BDMOASLegalStatusEvidenceVO> legalStatusEvidenceList =
      new ArrayList<BDMOASLegalStatusEvidenceVO>();

    final BDMOASLegalStatusEvidenceVO legalStatusEvidence =
      new BDMOASLegalStatusEvidenceVO();

    legalStatusEvidence.setEvidenceID(evidenceID);
    legalStatusEvidence.setEndDate(Date.getCurrentDate());
    legalStatusEvidence.setLegalStatus(BDMOASLEGALSTATUS.CANADIAN_CITIZEN);
    legalStatusEvidence
      .setOtherLegalStatusDetails("Test Other Legal Status Details");
    legalStatusEvidence.setStartDate(Date.getCurrentDate());
    legalStatusEvidence.setComments("Test here");

    legalStatusEvidenceList.add(legalStatusEvidence);

    return legalStatusEvidenceList;

  }

  private List<DynamicEvidenceDataDetails> setUpDataList(final long id)
    throws AppException {

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new ArrayList<DynamicEvidenceDataDetails>();

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory
        .newInstance(CASEEVIDENCE.OAS_LEGAL_STATUS, Date.getCurrentDate());

    dynamicEvidenceDataDetails.setID(id);
    dynamicEvidenceDataDetails.getAttribute("legalStatus")
      .setValue(BDMOASLEGALSTATUS.CANADIAN_CITIZEN);
    dynamicEvidenceDataDetails.getAttribute("otherLegalStatusDetails")
      .setValue("Test Other Legal Status Details");
    dynamicEvidenceDataDetails.getAttribute("comments").setValue("Test here");
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("endDate"),
      Date.getCurrentDate());
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("startDate"),
      Date.getCurrentDate());
    evidenceDataDetailsList.add(dynamicEvidenceDataDetails);

    return evidenceDataDetailsList;

  }

  /**
   * Verifies evidence is retrieved correctly.
   *
   * @throws AppException
   * @throws InformationalException
   */

  @Test
  public void testGetlegalStatusEvidenceValueObject() throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASLegalStatusEvidenceVO> testlegalStatusEvidenceList =
      setUpVOList(-584395839);

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      setUpDataList(-584395839);
    new Expectations() {

      {
        BDMEvidenceUtil.getEvdDtlsByConcernroleIDandEvidenceTypeInACOrIC(
          anyLong, CASEEVIDENCE.OAS_LEGAL_STATUS);
        result = evidenceDataDetailsList;

      }
    };

    final List<BDMOASLegalStatusEvidenceVO> legalStatusEvidenceList =
      BDMOASLegalStatusEvidence
        .getLegalStatusEvidenceValueObject(pdcPersonDetails.concernRoleID);

    final BDMOASLegalStatusEvidenceVO expectedVO =
      legalStatusEvidenceList.get(0);
    final BDMOASLegalStatusEvidenceVO resultVO =
      testlegalStatusEvidenceList.get(0);

    assertEquals("Created evidence list should match",
      expectedVO.getEndDate(), resultVO.getEndDate());
    assertEquals("Created evidence list should match",
      expectedVO.getLegalStatus(), resultVO.getLegalStatus());
    assertEquals("Created evidence list should match",
      expectedVO.getComments(), resultVO.getComments());
    assertEquals("Created evidence list should match",
      expectedVO.getEvidenceID(), resultVO.getEvidenceID());
    assertEquals("Created evidence list should match",
      expectedVO.getOtherLegalStatusDetails(),
      resultVO.getOtherLegalStatusDetails());
    assertEquals("Created evidence list should match",
      expectedVO.getStartDate(), resultVO.getStartDate());
  }

  /**
   * Verifies evidence is modified correctly if it exists
   *
   * @throws AppException
   * @throws InformationalException
   */

  @Test
  public void testCreateLegalStatusEvidence_Exists() throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASLegalStatusEvidenceVO> legalStatusEvidenceList =
      setUpVOList(-584395839);

    BDMOASLegalStatusEvidence.createLegalStatusEvidence(
      pdcPersonDetails.concernRoleID, legalStatusEvidenceList,
      "Test Reason Here");

    verificationModifyEvidence();
  }

  /**
   * Verifies evidence is created correctly if it doesn't exist
   *
   * @throws AppException
   * @throws InformationalException
   */

  @Test
  public void testcreatelegalStatusEvidence_NotExists() throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASLegalStatusEvidenceVO> legalStatusEvidenceList =
      setUpVOList(0);

    BDMOASLegalStatusEvidence.createLegalStatusEvidence(
      pdcPersonDetails.concernRoleID, legalStatusEvidenceList,
      "Test Reason Here");

    verificationCreateEvidence();
  }

  private void verificationModifyEvidence()
    throws AppException, InformationalException {

    try {

      new Verifications() {

        {
          curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil
            .modifyEvidenceForCase(anyLong, anyLong,
              CASEEVIDENCE.OAS_LEGAL_STATUS, (HashMap<String, String>) any,
              anyString);
          times = 1;
        }
      };
    } catch (final mockit.internal.MissingInvocation e) {
      assertTrue(
        "With given attribute information, evidence should have been modified.",
        false);
    }

  }

  private void verificationCreateEvidence()
    throws AppException, InformationalException {

    try {

      new Verifications() {

        {
          curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil
            .createACOrICDynamicEvidence(anyLong,
              (HashMap<String, String>) any, CASEEVIDENCE.OAS_LEGAL_STATUS,
              anyString, 0);
          times = 1;
        }
      };
    } catch (final mockit.internal.MissingInvocation e) {
      assertTrue(
        "With given attribute information, evidence should have been created.",
        false);
    }

  }
}
