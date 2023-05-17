package curam.ca.gc.bdmoas.test.lifeevent.impl;

import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdmoas.codetable.BDMOASTAXTHRESHOLD;
import curam.ca.gc.bdmoas.codetable.BDMOASYESNONA;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASWorldIncomeEvidence;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASWorldIncomeEvidenceVO;
import curam.ca.gc.bdmoas.test.evidence.BDMEvidenceUtilsTest;
import curam.testframework.CuramServerTestJUnit4;
import curam.codetable.CASEEVIDENCE;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.pdc.struct.PDCPersonDetails;
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
public class BDMOASWorldIncomeEvidenceTest extends CuramServerTestJUnit4 {

  @Mocked
  BDMEvidenceUtil BDMEvidenceUtil;

  @Tested
  BDMOASWorldIncomeEvidence BDMOASWorldIncomeEvidence;

  /** The testing functions. */
  BDMEvidenceUtilsTest bdmEvidenceUtils;

  @Before
  public void setUp() throws AppException, InformationalException {

    bdmEvidenceUtils = new BDMEvidenceUtilsTest();
    BDMEvidenceUtil = new BDMEvidenceUtil();

  }

  private List<BDMOASWorldIncomeEvidenceVO> setUpVOList(final long evidenceID)
    throws AppException, InformationalException {

    final List<BDMOASWorldIncomeEvidenceVO> worldIncomeEvidenceList =
      new ArrayList<BDMOASWorldIncomeEvidenceVO>();

    final BDMOASWorldIncomeEvidenceVO worldIncomeEvidence =
      new BDMOASWorldIncomeEvidenceVO();

    worldIncomeEvidence.setEvidenceID(evidenceID);
    worldIncomeEvidence.setOverThreshold(BDMOASYESNONA.YES);
    worldIncomeEvidence.setThreshold(BDMOASTAXTHRESHOLD.Y2021);
    worldIncomeEvidence.setComments("Test here");

    worldIncomeEvidenceList.add(worldIncomeEvidence);

    return worldIncomeEvidenceList;

  }

  private List<DynamicEvidenceDataDetails> setUpDataList(final long id)
    throws AppException {

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new ArrayList<DynamicEvidenceDataDetails>();

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory
        .newInstance(CASEEVIDENCE.OAS_WORLD_INCOME, Date.getCurrentDate());

    dynamicEvidenceDataDetails.setID(id);
    dynamicEvidenceDataDetails.getAttribute("overThreshold")
      .setValue(BDMOASYESNONA.YES);
    dynamicEvidenceDataDetails.getAttribute("threshold")
      .setValue(BDMOASTAXTHRESHOLD.Y2021);
    dynamicEvidenceDataDetails.getAttribute("comments").setValue("Test here");

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
  public void testGetworldIncomeEvidenceValueObject() throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASWorldIncomeEvidenceVO> testworldIncomeEvidenceList =
      setUpVOList(-584395839);

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      setUpDataList(-584395839);
    new Expectations() {

      {
        BDMEvidenceUtil.getEvdDtlsByConcernroleIDandEvidenceTypeInACOrIC(
          anyLong, CASEEVIDENCE.OAS_WORLD_INCOME);
        result = evidenceDataDetailsList;

      }
    };

    final List<BDMOASWorldIncomeEvidenceVO> worldIncomeEvidenceList =
      BDMOASWorldIncomeEvidence
        .getworldIncomeEvidenceValueObject(pdcPersonDetails.concernRoleID);

    final BDMOASWorldIncomeEvidenceVO expectedVO =
      worldIncomeEvidenceList.get(0);
    final BDMOASWorldIncomeEvidenceVO resultVO =
      testworldIncomeEvidenceList.get(0);

    assertEquals("Created evidence list should match",
      expectedVO.getOverThreshold(), resultVO.getOverThreshold());
    assertEquals("Created evidence list should match",
      expectedVO.getThreshold(), resultVO.getThreshold());
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
  public void testcreateWorldIncomeEvidence_Exists() throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASWorldIncomeEvidenceVO> worldIncomeEvidenceList =
      setUpVOList(-584395839);

    BDMOASWorldIncomeEvidence.createWorldIncomeEvidence(
      pdcPersonDetails.concernRoleID, worldIncomeEvidenceList,
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
  public void testcreateWorldIncomeEvidence_NotExists() throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASWorldIncomeEvidenceVO> worldIncomeEvidenceList =
      setUpVOList(0);

    BDMOASWorldIncomeEvidence.createWorldIncomeEvidence(
      pdcPersonDetails.concernRoleID, worldIncomeEvidenceList,
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
              CASEEVIDENCE.OAS_WORLD_INCOME, (HashMap<String, String>) any,
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
              (HashMap<String, String>) any, CASEEVIDENCE.OAS_WORLD_INCOME,
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
