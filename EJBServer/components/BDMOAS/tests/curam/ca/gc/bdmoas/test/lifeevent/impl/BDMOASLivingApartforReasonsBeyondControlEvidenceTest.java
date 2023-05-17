package curam.ca.gc.bdmoas.test.lifeevent.impl;

import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdmoas.codetable.BDMOASLIVINGAPARTREASON;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASLivingApartforReasonsBeyondControlEvidence;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASLivingApartforReasonsBeyondControlEvidenceVO;
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
public class BDMOASLivingApartforReasonsBeyondControlEvidenceTest
  extends CuramServerTestJUnit4 {

  @Mocked
  BDMEvidenceUtil BDMEvidenceUtil;

  @Tested
  BDMOASLivingApartforReasonsBeyondControlEvidence BDMOASLivingApartforReasonsBeyondControlEvidence;

  /** The testing functions. */
  BDMEvidenceUtilsTest bdmEvidenceUtils;

  @Before
  public void setUp() throws AppException, InformationalException {

    bdmEvidenceUtils = new BDMEvidenceUtilsTest();
    BDMEvidenceUtil = new BDMEvidenceUtil();

  }


  private List<BDMOASLivingApartforReasonsBeyondControlEvidenceVO>
    setUpVOList(final long evidenceID)
      throws AppException, InformationalException {

    final List<BDMOASLivingApartforReasonsBeyondControlEvidenceVO> livingApartforReasonsBeyondControlEvidenceList =
      new ArrayList<BDMOASLivingApartforReasonsBeyondControlEvidenceVO>();

    final BDMOASLivingApartforReasonsBeyondControlEvidenceVO livingApartforReasonsBeyondControlEvidence =
      new BDMOASLivingApartforReasonsBeyondControlEvidenceVO();

    livingApartforReasonsBeyondControlEvidence.setEvidenceID(evidenceID);
    livingApartforReasonsBeyondControlEvidence
      .setStartDate(Date.getCurrentDate());
    livingApartforReasonsBeyondControlEvidence
      .setEndDate(Date.getCurrentDate());
    livingApartforReasonsBeyondControlEvidence
      .setLivingApartReasonDescription("Test Description Here");
    livingApartforReasonsBeyondControlEvidence
      .setReasonForLivingApart(BDMOASLIVINGAPARTREASON.OAS_OTHER);
    livingApartforReasonsBeyondControlEvidenceList
      .add(livingApartforReasonsBeyondControlEvidence);

    return livingApartforReasonsBeyondControlEvidenceList;

  }

  private List<DynamicEvidenceDataDetails> setUpDataList(final long id)
    throws AppException {

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new ArrayList<DynamicEvidenceDataDetails>();

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(
        CASEEVIDENCE.OAS_LIVING_APART_FOR_REASONS_BEYOND_CONTROL,
        Date.getCurrentDate());

    dynamicEvidenceDataDetails.setID(id);
    dynamicEvidenceDataDetails.getAttribute("reasonForLivingApart")
      .setValue(BDMOASLIVINGAPARTREASON.OAS_OTHER);
    dynamicEvidenceDataDetails.getAttribute("livingApartReasonDescription")
      .setValue("Test Description Here");
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
  public void testGetLivingApartforReasonsBeyondControlEvidenceValueObject()
    throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASLivingApartforReasonsBeyondControlEvidenceVO> testlivingApartforReasonsBeyondControlEvidenceList =
      setUpVOList(-584395839);

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      setUpDataList(-584395839);
    new Expectations() {

      {
        BDMEvidenceUtil.getEvdDtlsByConcernroleIDandEvidenceTypeInACOrIC(
          anyLong, CASEEVIDENCE.OAS_LIVING_APART_FOR_REASONS_BEYOND_CONTROL);
        result = evidenceDataDetailsList;

      }
    };

    final List<BDMOASLivingApartforReasonsBeyondControlEvidenceVO> livingApartforReasonsBeyondControlEvidenceList =
      BDMOASLivingApartforReasonsBeyondControlEvidence
        .getLegalStatusEvidenceValueObject(pdcPersonDetails.concernRoleID);

    final BDMOASLivingApartforReasonsBeyondControlEvidenceVO expectedVO =
      livingApartforReasonsBeyondControlEvidenceList.get(0);
    final BDMOASLivingApartforReasonsBeyondControlEvidenceVO resultVO =
      testlivingApartforReasonsBeyondControlEvidenceList.get(0);

    assertEquals("Created evidence list should match",
      expectedVO.getEndDate(), resultVO.getEndDate());
    assertEquals("Created evidence list should match",
      expectedVO.getLivingApartReasonDescription(),
      resultVO.getLivingApartReasonDescription());
    assertEquals("Created evidence list should match",
      expectedVO.getReasonForLivingApart(),
      resultVO.getReasonForLivingApart());
    assertEquals("Created evidence list should match",
      expectedVO.getEvidenceID(), resultVO.getEvidenceID());
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
  public void testCreateLivingApartforReasonsBeyondControlEvidence_Exists()
    throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASLivingApartforReasonsBeyondControlEvidenceVO> livingApartforReasonsBeyondControlEvidenceList =
      setUpVOList(-584395839);

    BDMOASLivingApartforReasonsBeyondControlEvidence
      .createLegalStatusEvidence(pdcPersonDetails.concernRoleID,
        livingApartforReasonsBeyondControlEvidenceList, "Test Reason Here");

    verificationModifyEvidence();
  }

  /**
   * Verifies evidence is created correctly if it doesn't exist
   *
   * @throws AppException
   * @throws InformationalException
   */

  @Test
  public void testCreateLivingApartforReasonsBeyondControlEvidence_NotExists()
    throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASLivingApartforReasonsBeyondControlEvidenceVO> livingApartforReasonsBeyondControlEvidenceList =
      setUpVOList(0);

    BDMOASLivingApartforReasonsBeyondControlEvidence
      .createLegalStatusEvidence(pdcPersonDetails.concernRoleID,
        livingApartforReasonsBeyondControlEvidenceList, "Test Reason Here");

    verificationCreateEvidence();
  }

  private void verificationModifyEvidence()
    throws AppException, InformationalException {

    try {

      new Verifications() {

        {
          curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil
            .modifyEvidenceForCase(anyLong, anyLong,
              CASEEVIDENCE.OAS_LIVING_APART_FOR_REASONS_BEYOND_CONTROL,
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
              CASEEVIDENCE.OAS_LIVING_APART_FOR_REASONS_BEYOND_CONTROL,
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
