package curam.ca.gc.bdmoas.test.lifeevent.impl;

import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdmoas.codetable.BDMOASBENEFITTYPE;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASApplicationDetailsEvidence;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASApplicationDetailsEvidenceVO;
import curam.ca.gc.bdmoas.test.evidence.BDMEvidenceUtilsTest;
import curam.codetable.CASEEVIDENCE;
import curam.commonintake.codetable.METHODOFAPPLICATION;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.pdc.struct.PDCPersonDetails;
import curam.testframework.CuramServerTestJUnit4;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.workspaceservices.codetable.IntakeProgramApplicationStatus;
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
public class BDMOASApplicationDetailsEvidenceTest
  extends CuramServerTestJUnit4 {

  @Mocked
  BDMEvidenceUtil BDMEvidenceUtil;

  @Tested
  BDMOASApplicationDetailsEvidence BDMOASApplicationDetailsEvidence;

  /** The testing functions. */
  BDMEvidenceUtilsTest bdmEvidenceUtils;

  @Before
  public void setUp() throws AppException, InformationalException {

    bdmEvidenceUtils = new BDMEvidenceUtilsTest();
    BDMEvidenceUtil = new BDMEvidenceUtil();

  }

  private List<BDMOASApplicationDetailsEvidenceVO> setUpVOList(
    final long evidenceID) throws AppException, InformationalException {

    final List<BDMOASApplicationDetailsEvidenceVO> applicationDetailsEvidenceList =
      new ArrayList<BDMOASApplicationDetailsEvidenceVO>();

    final BDMOASApplicationDetailsEvidenceVO applicationDetailsEvidence =
      new BDMOASApplicationDetailsEvidenceVO();

    applicationDetailsEvidence.setEvidenceID(evidenceID);
    applicationDetailsEvidence.setBenefitType(BDMOASBENEFITTYPE.OAS_PENSION);
    applicationDetailsEvidence
      .setMethodOfApplication(METHODOFAPPLICATION.PAPER);
    applicationDetailsEvidence.setReceiptDate(Date.getCurrentDate());
    applicationDetailsEvidence
      .setRequestedPensionStartDate(Date.getCurrentDate());
    applicationDetailsEvidence.setWithdrawalDate(Date.getCurrentDate());
    applicationDetailsEvidence
      .setApplicationStatus(IntakeProgramApplicationStatus.APPROVED);
    applicationDetailsEvidence.setComments("Test here");

    applicationDetailsEvidenceList.add(applicationDetailsEvidence);

    return applicationDetailsEvidenceList;

  }

  private List<DynamicEvidenceDataDetails> setUpDataList(final long id)
    throws AppException {

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new ArrayList<DynamicEvidenceDataDetails>();

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(
        CASEEVIDENCE.OAS_APPLICATION_DETAILS, Date.getCurrentDate());

    dynamicEvidenceDataDetails.setID(id);
    dynamicEvidenceDataDetails.getAttribute("applicationStatus")
      .setValue(IntakeProgramApplicationStatus.APPROVED);
    dynamicEvidenceDataDetails.getAttribute("benefitType")
      .setValue(BDMOASBENEFITTYPE.OAS_PENSION);
    dynamicEvidenceDataDetails.getAttribute("comments").setValue("Test here");
    dynamicEvidenceDataDetails.getAttribute("methodOfApplication")
      .setValue(METHODOFAPPLICATION.PAPER);
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("receiptDate"),
      Date.getCurrentDate());
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("requestedPensionStartDate"),
      Date.getCurrentDate());
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("withdrawalDate"),
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
  public void testGetApplicationDetailsEvidenceValueObject()
    throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASApplicationDetailsEvidenceVO> testApplicationDetailsEvidenceList =
      setUpVOList(-584395839);

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      setUpDataList(-584395839);
    new Expectations() {

      {
        BDMEvidenceUtil.getEvdDtlsByConcernroleIDandEvidenceTypeInACOrIC(
          anyLong, CASEEVIDENCE.OAS_APPLICATION_DETAILS);
        result = evidenceDataDetailsList;

      }
    };

    final List<BDMOASApplicationDetailsEvidenceVO> applicationDetailsEvidenceList =
      BDMOASApplicationDetailsEvidence
        .getApplicationDetailsEvidenceValueObject(
          pdcPersonDetails.concernRoleID);

    final BDMOASApplicationDetailsEvidenceVO expectedVO =
      applicationDetailsEvidenceList.get(0);
    final BDMOASApplicationDetailsEvidenceVO resultVO =
      testApplicationDetailsEvidenceList.get(0);

    assertEquals("Created evidence list should match",
      expectedVO.getApplicationStatus(), resultVO.getApplicationStatus());
    assertEquals("Created evidence list should match",
      expectedVO.getBenefitType(), resultVO.getBenefitType());
    assertEquals("Created evidence list should match",
      expectedVO.getComments(), resultVO.getComments());
    assertEquals("Created evidence list should match",
      expectedVO.getEvidenceID(), resultVO.getEvidenceID());
    assertEquals("Created evidence list should match",
      expectedVO.getMethodOfApplication(), resultVO.getMethodOfApplication());
    assertEquals("Created evidence list should match",
      expectedVO.getReceiptDate(), resultVO.getReceiptDate());
    assertEquals("Created evidence list should match",
      expectedVO.getRequestedPensionStartDate(),
      resultVO.getRequestedPensionStartDate());
    assertEquals("Created evidence list should match",
      expectedVO.getWithdrawalDate(), resultVO.getWithdrawalDate());

  }

  /**
   * Verifies evidence is modified correctly if it exists
   *
   * @throws AppException
   * @throws InformationalException
   */

  @Test
  public void testcreateApplicationDetailsEvidence_Exists() throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASApplicationDetailsEvidenceVO> applicationDetailsEvidenceList =
      setUpVOList(-584395839);

    BDMOASApplicationDetailsEvidence.createApplicationDetailsEvidence(
      pdcPersonDetails.concernRoleID, applicationDetailsEvidenceList,
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
  public void testcreateApplicationDetailsEvidence_NotExists()
    throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASApplicationDetailsEvidenceVO> applicationDetailsEvidenceList =
      setUpVOList(0);

    BDMOASApplicationDetailsEvidence.createApplicationDetailsEvidence(
      pdcPersonDetails.concernRoleID, applicationDetailsEvidenceList,
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
              CASEEVIDENCE.OAS_APPLICATION_DETAILS,
              (HashMap<String, String>) any, anyString);
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
              (HashMap<String, String>) any,
              CASEEVIDENCE.OAS_APPLICATION_DETAILS, anyString, 0);
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
