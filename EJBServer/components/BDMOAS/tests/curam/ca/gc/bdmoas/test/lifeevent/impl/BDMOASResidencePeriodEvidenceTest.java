package curam.ca.gc.bdmoas.test.lifeevent.impl;

import curam.ca.gc.bdm.codetable.BDMYESNO;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdmoas.codetable.OASABSENCEREASON;
import curam.ca.gc.bdmoas.codetable.OASRESIDENCETYPE;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASResidencePeriodEvidence;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASResidencePeriodEvidenceVO;
import curam.ca.gc.bdmoas.test.evidence.BDMEvidenceUtilsTest;
import curam.testframework.CuramServerTestJUnit4;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.COUNTRY;
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
public class BDMOASResidencePeriodEvidenceTest extends CuramServerTestJUnit4 {

  @Mocked
  BDMEvidenceUtil BDMEvidenceUtil;

  @Tested
  BDMOASResidencePeriodEvidence BDMOASResidencePeriodEvidence;

  /** The testing functions. */
  BDMEvidenceUtilsTest bdmEvidenceUtils;

  @Before
  public void setUp() throws AppException, InformationalException {

    bdmEvidenceUtils = new BDMEvidenceUtilsTest();
    BDMEvidenceUtil = new BDMEvidenceUtil();

  }


  private List<BDMOASResidencePeriodEvidenceVO> setUpVOList(
    final long evidenceID) throws AppException, InformationalException {

    final List<BDMOASResidencePeriodEvidenceVO> residencePeriodEvidenceList =
      new ArrayList<BDMOASResidencePeriodEvidenceVO>();

    final BDMOASResidencePeriodEvidenceVO residencePeriodEvidence =
      new BDMOASResidencePeriodEvidenceVO();

    residencePeriodEvidence.setEvidenceID(evidenceID);
    residencePeriodEvidence.setAbsenceReason(OASABSENCEREASON.EDUCATION);
    residencePeriodEvidence.setCountry(COUNTRY.CA);
    residencePeriodEvidence.setEndDate(Date.getCurrentDate());
    residencePeriodEvidence.setIntentToReturnYesNo(BDMYESNO.YES);
    residencePeriodEvidence.setResidenceType(OASRESIDENCETYPE.RESIDENCE);
    residencePeriodEvidence.setStartDate(Date.getCurrentDate());
    residencePeriodEvidence.setComments("Test here");

    residencePeriodEvidenceList.add(residencePeriodEvidence);

    return residencePeriodEvidenceList;

  }

  private List<DynamicEvidenceDataDetails> setUpDataList(final long id)
    throws AppException {

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new ArrayList<DynamicEvidenceDataDetails>();

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(
        CASEEVIDENCE.OAS_RESIDENCE_PERIOD, Date.getCurrentDate());

    dynamicEvidenceDataDetails.setID(id);
    dynamicEvidenceDataDetails.getAttribute("absenceReason")
      .setValue(OASABSENCEREASON.EDUCATION);
    dynamicEvidenceDataDetails.getAttribute("country").setValue(COUNTRY.CA);
    dynamicEvidenceDataDetails.getAttribute("intentToReturnYesNo")
      .setValue(BDMYESNO.YES);
    dynamicEvidenceDataDetails.getAttribute("residenceType")
      .setValue(OASRESIDENCETYPE.RESIDENCE);
    dynamicEvidenceDataDetails.getAttribute("comments").setValue("Test here");
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("startDate"),
      Date.getCurrentDate());
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("endDate"),
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
  public void testGetresidencePeriodEvidenceValueObject() throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASResidencePeriodEvidenceVO> testresidencePeriodEvidenceList =
      setUpVOList(-584395839);

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      setUpDataList(-584395839);
    new Expectations() {

      {
        BDMEvidenceUtil.getEvdDtlsByConcernroleIDandEvidenceTypeInACOrIC(
          anyLong, CASEEVIDENCE.OAS_RESIDENCE_PERIOD);
        result = evidenceDataDetailsList;

      }
    };

    final List<BDMOASResidencePeriodEvidenceVO> residencePeriodEvidenceList =
      BDMOASResidencePeriodEvidence.getResidencePeriodEvidenceValueObject(
        pdcPersonDetails.concernRoleID);

    final BDMOASResidencePeriodEvidenceVO expectedVO =
      residencePeriodEvidenceList.get(0);
    final BDMOASResidencePeriodEvidenceVO resultVO =
      testresidencePeriodEvidenceList.get(0);

    assertEquals("Created evidence list should match",
      expectedVO.getEndDate(), resultVO.getEndDate());
    assertEquals("Created evidence list should match",
      expectedVO.getStartDate(), resultVO.getStartDate());
    assertEquals("Created evidence list should match",
      expectedVO.getComments(), resultVO.getComments());
    assertEquals("Created evidence list should match",
      expectedVO.getEvidenceID(), resultVO.getEvidenceID());
    assertEquals("Created evidence list should match",
      expectedVO.getAbsenceReason(), resultVO.getAbsenceReason());
    assertEquals("Created evidence list should match",
      expectedVO.getCountry(), resultVO.getCountry());
    assertEquals("Created evidence list should match",
      expectedVO.getIntentToReturnYesNo(), resultVO.getIntentToReturnYesNo());
    assertEquals("Created evidence list should match",
      expectedVO.getResidenceType(), resultVO.getResidenceType());
    assertEquals("Created evidence list should match",
      expectedVO.getComments(), resultVO.getComments());

  }

  /**
   * Verifies evidence is modified correctly if it exists
   *
   * @throws AppException
   * @throws InformationalException
   */

  @Test
  public void testCreateResidencePeriodEvidence_Exists() throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASResidencePeriodEvidenceVO> residencePeriodEvidenceList =
      setUpVOList(-584395839);

    BDMOASResidencePeriodEvidence.createResidencePeriodEvidence(
      pdcPersonDetails.concernRoleID, residencePeriodEvidenceList,
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
  public void testCreateResidencePeriodEvidence_NotExists() throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASResidencePeriodEvidenceVO> residencePeriodEvidenceList =
      setUpVOList(0);

    BDMOASResidencePeriodEvidence.createResidencePeriodEvidence(
      pdcPersonDetails.concernRoleID, residencePeriodEvidenceList,
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
              CASEEVIDENCE.OAS_RESIDENCE_PERIOD,
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
              CASEEVIDENCE.OAS_RESIDENCE_PERIOD, anyString, 0);
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
