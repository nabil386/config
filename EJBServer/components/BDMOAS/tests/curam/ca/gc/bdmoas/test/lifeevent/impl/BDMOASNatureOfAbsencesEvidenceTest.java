package curam.ca.gc.bdmoas.test.lifeevent.impl;

import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASNatureOfAbsencesEvidence;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASNatureOfAbsencesEvidenceVO;
import curam.ca.gc.bdmoas.test.evidence.BDMEvidenceUtilsTest;
import curam.codetable.CASEEVIDENCE;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
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
public class BDMOASNatureOfAbsencesEvidenceTest
  extends CuramServerTestJUnit4 {

  @Mocked
  BDMEvidenceUtil BDMEvidenceUtil;

  @Tested
  BDMOASNatureOfAbsencesEvidence BDMOASNatureOfAbsencesEvidence;

  /** The testing functions. */
  BDMEvidenceUtilsTest bdmEvidenceUtils;

  @Before
  public void setUp() throws AppException, InformationalException {

    bdmEvidenceUtils = new BDMEvidenceUtilsTest();
    BDMEvidenceUtil = new BDMEvidenceUtil();

  }

  private List<BDMOASNatureOfAbsencesEvidenceVO> setUpVOList(
    final long evidenceID) throws AppException, InformationalException {

    final List<BDMOASNatureOfAbsencesEvidenceVO> natureOfAbsencesEvidenceList =
      new ArrayList<BDMOASNatureOfAbsencesEvidenceVO>();

    final BDMOASNatureOfAbsencesEvidenceVO natureOfAbsencesEvidence =
      new BDMOASNatureOfAbsencesEvidenceVO();

    natureOfAbsencesEvidence.setEvidenceID(evidenceID);
    natureOfAbsencesEvidence
      .setAbsenceDetails("Example Absence Details Here");
    natureOfAbsencesEvidence.setComments("Test here");
    natureOfAbsencesEvidenceList.add(natureOfAbsencesEvidence);

    return natureOfAbsencesEvidenceList;

  }

  private List<DynamicEvidenceDataDetails> setUpDataList(final long id)
    throws AppException {

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new ArrayList<DynamicEvidenceDataDetails>();

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(
        CASEEVIDENCE.OAS_NATURE_OF_ABSENCES, Date.getCurrentDate());

    dynamicEvidenceDataDetails.setID(id);
    dynamicEvidenceDataDetails.getAttribute("absenceDetails")
      .setValue("Example Absence Details Here");
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
  public void testGetnatureOfAbsencesEvidenceValueObject() throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASNatureOfAbsencesEvidenceVO> testnatureOfAbsencesEvidenceList =
      setUpVOList(-584395839);

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      setUpDataList(-584395839);
    new Expectations() {

      {
        BDMEvidenceUtil.getEvdDtlsByConcernroleIDandEvidenceTypeInACOrIC(
          anyLong, CASEEVIDENCE.OAS_NATURE_OF_ABSENCES);
        result = evidenceDataDetailsList;

      }
    };

    final List<BDMOASNatureOfAbsencesEvidenceVO> natureOfAbsencesEvidenceList =
      BDMOASNatureOfAbsencesEvidence
        .getEvidenceValueObject(pdcPersonDetails.concernRoleID);

    final BDMOASNatureOfAbsencesEvidenceVO expectedVO =
      natureOfAbsencesEvidenceList.get(0);
    final BDMOASNatureOfAbsencesEvidenceVO resultVO =
      testnatureOfAbsencesEvidenceList.get(0);

    assertEquals("Created evidence list should match",
      expectedVO.getAbsenceDetails(), resultVO.getAbsenceDetails());
    assertEquals("Created evidence list should match",
      expectedVO.getComments(), resultVO.getComments());
    assertEquals("Created evidence list should match",
      expectedVO.getEvidenceID(), resultVO.getEvidenceID());
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
  public void testcreateNatureOfAbsencesEvidence_Exists() throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASNatureOfAbsencesEvidenceVO> natureOfAbsencesEvidenceList =
      setUpVOList(-584395839);

    BDMOASNatureOfAbsencesEvidence.createNatureOfAbsencesEvidence(
      pdcPersonDetails.concernRoleID, natureOfAbsencesEvidenceList,
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
  public void testcreateNatureOfAbsencesEvidence_NotExists()
    throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASNatureOfAbsencesEvidenceVO> natureOfAbsencesEvidenceList =
      setUpVOList(0);

    BDMOASNatureOfAbsencesEvidence.createNatureOfAbsencesEvidence(
      pdcPersonDetails.concernRoleID, natureOfAbsencesEvidenceList,
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
              CASEEVIDENCE.OAS_NATURE_OF_ABSENCES,
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
              CASEEVIDENCE.OAS_NATURE_OF_ABSENCES, anyString, 0);
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
