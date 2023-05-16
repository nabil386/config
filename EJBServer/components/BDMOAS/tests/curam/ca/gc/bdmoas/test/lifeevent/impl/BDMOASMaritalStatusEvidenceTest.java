package curam.ca.gc.bdmoas.test.lifeevent.impl;

import curam.ca.gc.bdm.codetable.BDMMODEOFRECEIPT;
import curam.ca.gc.bdm.codetable.BDMRECEIVEDFROM;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASMaritalStatusEvidence;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASMaritalStatusEvidenceVO;
import curam.ca.gc.bdmoas.test.evidence.BDMEvidenceUtilsTest;
import curam.testframework.CuramServerTestJUnit4;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.COUNTRY;
import curam.codetable.MARITALSTATUS;
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
public class BDMOASMaritalStatusEvidenceTest extends CuramServerTestJUnit4 {

  @Mocked
  BDMEvidenceUtil BDMEvidenceUtil;

  @Tested
  BDMOASMaritalStatusEvidence BDMOASMaritalStatusEvidence;

  /** The testing functions. */
  BDMEvidenceUtilsTest bdmEvidenceUtils;

  @Before
  public void setUp() throws AppException, InformationalException {

    bdmEvidenceUtils = new BDMEvidenceUtilsTest();
    BDMEvidenceUtil = new BDMEvidenceUtil();

  }


  private List<BDMOASMaritalStatusEvidenceVO> setUpVOList(
    final long evidenceID) throws AppException, InformationalException {

    final List<BDMOASMaritalStatusEvidenceVO> maritalStatusEvidenceList =
      new ArrayList<BDMOASMaritalStatusEvidenceVO>();

    final BDMOASMaritalStatusEvidenceVO maritalStatusEvidence =
      new BDMOASMaritalStatusEvidenceVO();

    maritalStatusEvidence.setEvidenceID(evidenceID);
    maritalStatusEvidence.setEndDate(Date.getCurrentDate());
    maritalStatusEvidence.setBdmModeOfReceipt(BDMMODEOFRECEIPT.CERTIFIED_APP);
    maritalStatusEvidence
      .setBdmReceivedFrom(BDMRECEIVEDFROM.FOREIGN_GOVERNMENT);
    maritalStatusEvidence.setBdmReceivedFromCountry(COUNTRY.US);
    maritalStatusEvidence.setMaritalStatus(MARITALSTATUS.MARRIED);
    maritalStatusEvidence.setStartDate(Date.getCurrentDate());
    maritalStatusEvidence.setComments("Test here");

    maritalStatusEvidenceList.add(maritalStatusEvidence);

    return maritalStatusEvidenceList;

  }

  private List<DynamicEvidenceDataDetails> setUpDataList(final long id)
    throws AppException {

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new ArrayList<DynamicEvidenceDataDetails>();

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory
        .newInstance(CASEEVIDENCE.BDM_MARITAL_STATUS, Date.getCurrentDate());

    dynamicEvidenceDataDetails.setID(id);
    dynamicEvidenceDataDetails.getAttribute("bdmModeOfReceipt")
      .setValue(BDMMODEOFRECEIPT.CERTIFIED_APP);
    dynamicEvidenceDataDetails.getAttribute("bdmReceivedFrom")
      .setValue(BDMRECEIVEDFROM.FOREIGN_GOVERNMENT);
    dynamicEvidenceDataDetails.getAttribute("bdmReceivedFromCountry")
      .setValue(COUNTRY.US);
    dynamicEvidenceDataDetails.getAttribute("maritalStatus")
      .setValue(MARITALSTATUS.MARRIED);
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
  public void testGetmaritalStatusEvidenceValueObject() throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASMaritalStatusEvidenceVO> testmaritalStatusEvidenceList =
      setUpVOList(-584395839);

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      setUpDataList(-584395839);
    new Expectations() {

      {
        BDMEvidenceUtil.getEvdDtlsByConcernroleIDandEvidenceTypeInACOrIC(
          anyLong, CASEEVIDENCE.BDM_MARITAL_STATUS);
        result = evidenceDataDetailsList;

      }
    };

    final List<BDMOASMaritalStatusEvidenceVO> maritalStatusEvidenceList =
      BDMOASMaritalStatusEvidence
        .getMaritalStatusEvidenceValueObject(pdcPersonDetails.concernRoleID);

    final BDMOASMaritalStatusEvidenceVO expectedVO =
      maritalStatusEvidenceList.get(0);
    final BDMOASMaritalStatusEvidenceVO resultVO =
      testmaritalStatusEvidenceList.get(0);

    assertEquals("Created evidence list should match",
      expectedVO.getEndDate(), resultVO.getEndDate());
    assertEquals("Created evidence list should match",
      expectedVO.getStartDate(), resultVO.getStartDate());
    assertEquals("Created evidence list should match",
      expectedVO.getComments(), resultVO.getComments());
    assertEquals("Created evidence list should match",
      expectedVO.getEvidenceID(), resultVO.getEvidenceID());
    assertEquals("Created evidence list should match",
      expectedVO.getBdmModeOfReceipt(), resultVO.getBdmModeOfReceipt());
    assertEquals("Created evidence list should match",
      expectedVO.getBdmReceivedFrom(), resultVO.getBdmReceivedFrom());
    assertEquals("Created evidence list should match",
      expectedVO.getBdmReceivedFromCountry(),
      resultVO.getBdmReceivedFromCountry());
    assertEquals("Created evidence list should match",
      expectedVO.getMaritalStatus(), resultVO.getMaritalStatus());

  }

  /**
   * Verifies evidence is modified correctly if it exists
   *
   * @throws AppException
   * @throws InformationalException
   */

  @Test
  public void testcreateMaritalStatusEvidence_Exists() throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASMaritalStatusEvidenceVO> maritalStatusEvidenceList =
      setUpVOList(-584395839);

    BDMOASMaritalStatusEvidence.createMaritalStatusEvidence(
      pdcPersonDetails.concernRoleID, maritalStatusEvidenceList,
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
  public void testcreateMaritalStatusEvidence_NotExists() throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASMaritalStatusEvidenceVO> maritalStatusEvidenceList =
      setUpVOList(0);

    BDMOASMaritalStatusEvidence.createMaritalStatusEvidence(
      pdcPersonDetails.concernRoleID, maritalStatusEvidenceList,
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
              CASEEVIDENCE.BDM_MARITAL_STATUS, (HashMap<String, String>) any,
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
              (HashMap<String, String>) any, CASEEVIDENCE.BDM_MARITAL_STATUS,
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
