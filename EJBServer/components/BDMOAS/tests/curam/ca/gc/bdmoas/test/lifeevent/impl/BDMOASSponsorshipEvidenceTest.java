package curam.ca.gc.bdmoas.test.lifeevent.impl;

import curam.ca.gc.bdm.codetable.BDMYESNO;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASSponsorshipEvidence;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASSponsorshipEvidenceVO;
import curam.ca.gc.bdmoas.test.evidence.BDMEvidenceUtilsTest;
import curam.testframework.CuramServerTestJUnit4;
import curam.codetable.CASEEVIDENCE;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
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
public class BDMOASSponsorshipEvidenceTest extends CuramServerTestJUnit4 {

  @Mocked
  BDMEvidenceUtil BDMEvidenceUtil;

  @Tested
  BDMOASSponsorshipEvidence BDMOASSponsorshipEvidence;

  /** The testing functions. */
  BDMEvidenceUtilsTest bdmEvidenceUtils;

  @Before
  public void setUp() throws AppException, InformationalException {

    bdmEvidenceUtils = new BDMEvidenceUtilsTest();
    BDMEvidenceUtil = new BDMEvidenceUtil();


  }

  private List<BDMOASSponsorshipEvidenceVO> setUpVOList(final long evidenceID)
    throws AppException, InformationalException {

    final List<BDMOASSponsorshipEvidenceVO> sponsorshipEvidenceList =
      new ArrayList<BDMOASSponsorshipEvidenceVO>();

    final BDMOASSponsorshipEvidenceVO sponsorshipEvidence =
      new BDMOASSponsorshipEvidenceVO();

    sponsorshipEvidence.setEvidenceID(evidenceID);
    sponsorshipEvidence.setIsSponsored(BDMYESNO.YES);
    sponsorshipEvidence.setComments("Test here");

    sponsorshipEvidenceList.add(sponsorshipEvidence);

    return sponsorshipEvidenceList;

  }

  private List<DynamicEvidenceDataDetails> setUpDataList(final long id)
    throws AppException {

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new ArrayList<DynamicEvidenceDataDetails>();

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory
        .newInstance(CASEEVIDENCE.OAS_SPONSORSHIP, Date.getCurrentDate());

    dynamicEvidenceDataDetails.setID(id);
    dynamicEvidenceDataDetails.getAttribute("isSponsored")
      .setValue(BDMYESNO.YES);
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
  public void testGetsponsorshipEvidenceValueObject() throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASSponsorshipEvidenceVO> testsponsorshipEvidenceList =
      setUpVOList(-584395839);

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      setUpDataList(-584395839);
    new Expectations() {

      {
        BDMEvidenceUtil.getEvdDtlsByConcernroleIDandEvidenceTypeInACOrIC(
          anyLong, CASEEVIDENCE.OAS_SPONSORSHIP);
        result = evidenceDataDetailsList;

      }
    };

    final List<BDMOASSponsorshipEvidenceVO> sponsorshipEvidenceList =
      BDMOASSponsorshipEvidence.getResidencePeriodEvidenceValueObject(
        pdcPersonDetails.concernRoleID);

    final BDMOASSponsorshipEvidenceVO expectedVO =
      sponsorshipEvidenceList.get(0);
    final BDMOASSponsorshipEvidenceVO resultVO =
      testsponsorshipEvidenceList.get(0);

    assertEquals("Created evidence list should match",
      expectedVO.getIsSponsored(), resultVO.getIsSponsored());
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
  public void testcreateResidencePeriodEvidence_Exists() throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASSponsorshipEvidenceVO> sponsorshipEvidenceList =
      setUpVOList(-584395839);

    BDMOASSponsorshipEvidence.createResidencePeriodEvidence(
      pdcPersonDetails.concernRoleID, sponsorshipEvidenceList,
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
  public void testcreateResidencePeriodEvidence_NotExists() throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASSponsorshipEvidenceVO> sponsorshipEvidenceList =
      setUpVOList(0);

    BDMOASSponsorshipEvidence.createResidencePeriodEvidence(
      pdcPersonDetails.concernRoleID, sponsorshipEvidenceList,
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
              CASEEVIDENCE.OAS_SPONSORSHIP, (HashMap<String, String>) any,
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
              (HashMap<String, String>) any, CASEEVIDENCE.OAS_SPONSORSHIP,
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
