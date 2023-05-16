package curam.ca.gc.bdmoas.test.lifeevent.impl;

import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdmoas.codetable.BDMOASCANCELSTATUS;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASBenefitCancellationRequestEvidence;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASBenefitCancellationRequestEvidenceVO;
import curam.ca.gc.bdmoas.test.evidence.BDMEvidenceUtilsTest;
import curam.codetable.CASEEVIDENCE;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.pdc.struct.PDCPersonDetails;
import curam.testframework.CuramServerTestJUnit4;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)
public class BDMOASBenefitCancellationRequestEvidenceTest
  extends CuramServerTestJUnit4 {

  @Mocked
  BDMEvidenceUtil BDMEvidenceUtil;

  @Tested
  BDMOASBenefitCancellationRequestEvidence BDMOASBenefitCancellationRequestEvidence;

  /** The testing functions. */
  BDMEvidenceUtilsTest bdmEvidenceUtils;

  @Before
  public void setUp() throws AppException, InformationalException {

    bdmEvidenceUtils = new BDMEvidenceUtilsTest();
    BDMEvidenceUtil = new BDMEvidenceUtil();

  }

  private List<BDMOASBenefitCancellationRequestEvidenceVO> setUpVOList(
    final long evidenceID) throws AppException, InformationalException {

    final List<BDMOASBenefitCancellationRequestEvidenceVO> benefitCancellationRequestEvidenceList =
      new ArrayList<BDMOASBenefitCancellationRequestEvidenceVO>();

    final BDMOASBenefitCancellationRequestEvidenceVO benefitCancellationRequestEvidence =
      new BDMOASBenefitCancellationRequestEvidenceVO();

    benefitCancellationRequestEvidence.setEvidenceID(evidenceID);
    benefitCancellationRequestEvidence
      .setCancellationStatus(BDMOASCANCELSTATUS.REQUESTED);
    benefitCancellationRequestEvidence.setGrantDate(Date.getCurrentDate());
    benefitCancellationRequestEvidence
      .setRepaymentDueDateOverride(Date.getCurrentDate());
    benefitCancellationRequestEvidence.setRequestDate(Date.getCurrentDate());
    benefitCancellationRequestEvidence.setComments("Test here");

    benefitCancellationRequestEvidenceList
      .add(benefitCancellationRequestEvidence);

    return benefitCancellationRequestEvidenceList;

  }

  private List<DynamicEvidenceDataDetails> setUpDataList(final long id)
    throws AppException {

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new ArrayList<DynamicEvidenceDataDetails>();

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(
        CASEEVIDENCE.OAS_BENEFIT_CANCELLATION, Date.getCurrentDate());

    dynamicEvidenceDataDetails.setID(id);
    dynamicEvidenceDataDetails.getAttribute("cancellationStatus")
      .setValue(BDMOASCANCELSTATUS.REQUESTED);
    dynamicEvidenceDataDetails.getAttribute("comments").setValue("Test here");
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("grantDate"),
      Date.getCurrentDate());
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("repaymentDueDateOverride"),
      Date.getCurrentDate());
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("requestDate"),
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
  public void testGetBenefitCancellationRequestEvidenceValueObject()
    throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASBenefitCancellationRequestEvidenceVO> testbenefitCancellationRequestEvidenceList =
      setUpVOList(-584395839);

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      setUpDataList(-584395839);
    new Expectations() {

      {
        BDMEvidenceUtil.getEvdDtlsByConcernroleIDandEvidenceTypeInACOrIC(
          anyLong, CASEEVIDENCE.OAS_BENEFIT_CANCELLATION);
        result = evidenceDataDetailsList;

      }
    };

    final List<BDMOASBenefitCancellationRequestEvidenceVO> benefitCancellationRequestEvidenceList =
      BDMOASBenefitCancellationRequestEvidence
        .getRetirementPensionReductionEvidenceValueObject(
          pdcPersonDetails.concernRoleID);

    final BDMOASBenefitCancellationRequestEvidenceVO expectedVO =
      testbenefitCancellationRequestEvidenceList.get(0);
    final BDMOASBenefitCancellationRequestEvidenceVO resultVO =
      benefitCancellationRequestEvidenceList.get(0);

    assertEquals("Created evidence list should match",
      expectedVO.getCancellationStatus(), resultVO.getCancellationStatus());
    assertEquals("Created evidence list should match",
      expectedVO.getGrantDate(), resultVO.getGrantDate());
    assertEquals("Created evidence list should match",
      expectedVO.getComments(), resultVO.getComments());
    assertEquals("Created evidence list should match",
      expectedVO.getEvidenceID(), resultVO.getEvidenceID());
    assertEquals("Created evidence list should match",
      expectedVO.getGrantDate(), resultVO.getGrantDate());
    assertEquals("Created evidence list should match",
      expectedVO.getRepaymentDueDateOverride(),
      resultVO.getRepaymentDueDateOverride());
    assertEquals("Created evidence list should match",
      expectedVO.getRequestDate(), resultVO.getRequestDate());

  }

  /**
   * Verifies evidence is modified correctly if it exists
   *
   * @throws AppException
   * @throws InformationalException
   */

  @Test
  public void testCreateBenefitCancellationRequestEvidence_Exists()
    throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASBenefitCancellationRequestEvidenceVO> benefitCancellationRequestEvidenceList =
      setUpVOList(-584395839);

    BDMOASBenefitCancellationRequestEvidence
      .createBenefitCancellationEvidence(pdcPersonDetails.concernRoleID,
        benefitCancellationRequestEvidenceList, "Test Reason Here");

    verificationModifyEvidence();
  }

  /**
   * Verifies evidence is created correctly if it doesn't exist
   *
   * @throws AppException
   * @throws InformationalException
   */

  @Test
  public void testCreateBenefitCancellationRequestEvidence_NotExists()
    throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASBenefitCancellationRequestEvidenceVO> benefitCancellationRequestEvidenceList =
      setUpVOList(0);

    BDMOASBenefitCancellationRequestEvidence
      .createBenefitCancellationEvidence(pdcPersonDetails.concernRoleID,
        benefitCancellationRequestEvidenceList, "Test Reason Here");

    verificationCreateEvidence();
  }

  private void verificationModifyEvidence()
    throws AppException, InformationalException {

    try {

      new Verifications() {

        {
          curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil
            .modifyEvidenceForCase(anyLong, anyLong,
              CASEEVIDENCE.OAS_BENEFIT_CANCELLATION,
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
              CASEEVIDENCE.OAS_BENEFIT_CANCELLATION, anyString, 0);
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
