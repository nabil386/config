package curam.ca.gc.bdmoas.test.lifeevent.impl;

import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdmoas.codetable.BDMOASOPTIONABLEEVENT;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASRetirementPensionReductionEvidence;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASRetirementPensionReductionEvidenceVO;
import curam.ca.gc.bdmoas.test.evidence.BDMEvidenceUtilsTest;
import curam.testframework.CuramServerTestJUnit4;
import curam.codetable.CASEEVIDENCE;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.pdc.struct.PDCPersonDetails;
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
public class BDMOASRetirementPensionReductionEvidenceTest
  extends CuramServerTestJUnit4 {

  @Mocked
  BDMEvidenceUtil BDMEvidenceUtil;

  @Tested
  BDMOASRetirementPensionReductionEvidence BDMOASRetirementPensionReductionEvidence;

  /** The testing functions. */
  BDMEvidenceUtilsTest bdmEvidenceUtils;

  @Before
  public void setUp() throws AppException, InformationalException {

    bdmEvidenceUtils = new BDMEvidenceUtilsTest();
    BDMEvidenceUtil = new BDMEvidenceUtil();

  }


  private List<BDMOASRetirementPensionReductionEvidenceVO> setUpVOList(
    final long evidenceID) throws AppException, InformationalException {

    final List<BDMOASRetirementPensionReductionEvidenceVO> retirementPensionReductionEvidenceList =
      new ArrayList<BDMOASRetirementPensionReductionEvidenceVO>();

    final BDMOASRetirementPensionReductionEvidenceVO retirementPensionReductionEvidence =
      new BDMOASRetirementPensionReductionEvidenceVO();

    retirementPensionReductionEvidence.setEvidenceID(evidenceID);
    retirementPensionReductionEvidence.setEventDate(Date.getCurrentDate());
    retirementPensionReductionEvidence
      .setEventType(BDMOASOPTIONABLEEVENT.RETIREMENT);
    retirementPensionReductionEvidence.setComments("Test here");
    retirementPensionReductionEvidenceList
      .add(retirementPensionReductionEvidence);

    return retirementPensionReductionEvidenceList;

  }

  private List<DynamicEvidenceDataDetails> setUpDataList(final long id)
    throws AppException {

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new ArrayList<DynamicEvidenceDataDetails>();

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(
        CASEEVIDENCE.OAS_RETIREMENT_PENSION_REDUCTION, Date.getCurrentDate());

    dynamicEvidenceDataDetails.setID(id);
    dynamicEvidenceDataDetails.getAttribute("eventType")
      .setValue(BDMOASOPTIONABLEEVENT.RETIREMENT);
    dynamicEvidenceDataDetails.getAttribute("comments").setValue("Test here");
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("eventDate"),
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
  public void testGetretirementPensionReductionEvidenceValueObject()
    throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASRetirementPensionReductionEvidenceVO> testretirementPensionReductionEvidenceList =
      setUpVOList(-584395839);

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      setUpDataList(-584395839);
    new Expectations() {

      {
        BDMEvidenceUtil.getEvdDtlsByConcernroleIDandEvidenceTypeInACOrIC(
          anyLong, CASEEVIDENCE.OAS_RETIREMENT_PENSION_REDUCTION);
        result = evidenceDataDetailsList;

      }
    };

    final List<BDMOASRetirementPensionReductionEvidenceVO> retirementPensionReductionEvidenceList =
      BDMOASRetirementPensionReductionEvidence
        .getRetirementPensionReductionEvidenceValueObject(
          pdcPersonDetails.concernRoleID);

    final BDMOASRetirementPensionReductionEvidenceVO expectedVO =
      retirementPensionReductionEvidenceList.get(0);
    final BDMOASRetirementPensionReductionEvidenceVO resultVO =
      testretirementPensionReductionEvidenceList.get(0);

    assertEquals("Created evidence list should match",
      expectedVO.getEventDate(), resultVO.getEventDate());
    assertEquals("Created evidence list should match",
      expectedVO.getEventType(), resultVO.getEventType());
    assertEquals("Created evidence list should match",
      expectedVO.getComments(), resultVO.getComments());
    assertEquals("Created evidence list should match",
      expectedVO.getEvidenceID(), resultVO.getEvidenceID());
  }

  /**
   * Verifies evidence is modified correctly if it exists
   *
   * @throws AppException
   * @throws InformationalException
   */

  @Test
  public void testcreateRetirementPensionReductionEvidence_Exists()
    throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASRetirementPensionReductionEvidenceVO> retirementPensionReductionEvidenceList =
      setUpVOList(-584395839);

    BDMOASRetirementPensionReductionEvidence
      .createRetirementPensionReductionEvidence(
        pdcPersonDetails.concernRoleID,
        retirementPensionReductionEvidenceList, "Test Reason Here");

    verificationModifyEvidence();
  }

  /**
   * Verifies evidence is created correctly if it doesn't exist
   *
   * @throws AppException
   * @throws InformationalException
   */

  @Test
  public void testcreateRetirementPensionReductionEvidence_NotExists()
    throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASRetirementPensionReductionEvidenceVO> retirementPensionReductionEvidenceList =
      setUpVOList(0);

    BDMOASRetirementPensionReductionEvidence
      .createRetirementPensionReductionEvidence(
        pdcPersonDetails.concernRoleID,
        retirementPensionReductionEvidenceList, "Test Reason Here");

    verificationCreateEvidence();
  }

  private void verificationModifyEvidence()
    throws AppException, InformationalException {

    try {

      new Verifications() {

        {
          curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil
            .modifyEvidenceForCase(anyLong, anyLong,
              CASEEVIDENCE.OAS_RETIREMENT_PENSION_REDUCTION,
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
              CASEEVIDENCE.OAS_RETIREMENT_PENSION_REDUCTION, anyString, 0);
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
