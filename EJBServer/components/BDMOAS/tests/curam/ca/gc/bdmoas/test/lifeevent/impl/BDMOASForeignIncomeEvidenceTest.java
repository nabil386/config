package curam.ca.gc.bdmoas.test.lifeevent.impl;

import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASForeignIncomeEvidence;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASForeignIncomeEvidenceVO;
import curam.ca.gc.bdmoas.test.evidence.BDMEvidenceUtilsTest;
import curam.codetable.BDMCURRENCY;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.COUNTRY;
import curam.codetable.INCOMETYPECODE;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
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
public class BDMOASForeignIncomeEvidenceTest extends CuramServerTestJUnit4 {

  @Mocked
  BDMEvidenceUtil BDMEvidenceUtil;

  @Tested
  BDMOASForeignIncomeEvidence BDMOASForeignIncomeEvidence;

  /** The testing functions. */
  BDMEvidenceUtilsTest bdmEvidenceUtils;

  @Before
  public void setUp() throws AppException, InformationalException {

    bdmEvidenceUtils = new BDMEvidenceUtilsTest();
    BDMEvidenceUtil = new BDMEvidenceUtil();

  }


  private List<BDMOASForeignIncomeEvidenceVO> setUpVOList(
    final long evidenceID) throws AppException, InformationalException {

    final List<BDMOASForeignIncomeEvidenceVO> foreignIncomeEvidenceList =
      new ArrayList<BDMOASForeignIncomeEvidenceVO>();

    final BDMOASForeignIncomeEvidenceVO foreignIncomeEvidence =
      new BDMOASForeignIncomeEvidenceVO();

    foreignIncomeEvidence.setEvidenceID(evidenceID);
    foreignIncomeEvidence.setAmount("1000");
    foreignIncomeEvidence.setCanadianAmount("1200");
    foreignIncomeEvidence.setCountry(COUNTRY.GB);
    foreignIncomeEvidence.setCurrency(BDMCURRENCY.EURO);
    foreignIncomeEvidence.setIncomeType(INCOMETYPECODE.OTHER);
    foreignIncomeEvidence.setOtherDescription("other example desc");
    foreignIncomeEvidence.setYear("2022");
    foreignIncomeEvidence.setComments("Test here");

    foreignIncomeEvidenceList.add(foreignIncomeEvidence);

    return foreignIncomeEvidenceList;

  }

  private List<DynamicEvidenceDataDetails> setUpDataList(final long id)
    throws AppException {

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new ArrayList<DynamicEvidenceDataDetails>();

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory
        .newInstance(CASEEVIDENCE.OAS_FOREIGN_INCOME, Date.getCurrentDate());

    dynamicEvidenceDataDetails.setID(id);
    dynamicEvidenceDataDetails.getAttribute("amount").setValue("1000");
    dynamicEvidenceDataDetails.getAttribute("canadianAmount")
      .setValue("1200");
    dynamicEvidenceDataDetails.getAttribute("country").setValue(COUNTRY.GB);
    dynamicEvidenceDataDetails.getAttribute("currency")
      .setValue(BDMCURRENCY.EURO);
    dynamicEvidenceDataDetails.getAttribute("incomeType")
      .setValue(INCOMETYPECODE.OTHER);
    dynamicEvidenceDataDetails.getAttribute("comments").setValue("Test here");
    dynamicEvidenceDataDetails.getAttribute("otherDescription")
      .setValue("other example desc");
    dynamicEvidenceDataDetails.getAttribute("year").setValue("2022");

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
  public void testGetForeignIncomeEvidenceValueObject() throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASForeignIncomeEvidenceVO> testforeignIncomeEvidenceList =
      setUpVOList(-584395839);

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      setUpDataList(-584395839);
    new Expectations() {

      {
        BDMEvidenceUtil.getEvdDtlsByConcernroleIDandEvidenceTypeInACOrIC(
          anyLong, CASEEVIDENCE.OAS_FOREIGN_INCOME);
        result = evidenceDataDetailsList;

      }
    };

    final List<BDMOASForeignIncomeEvidenceVO> foreignIncomeEvidenceList =
      BDMOASForeignIncomeEvidence
        .getEvidenceValueObject(pdcPersonDetails.concernRoleID);

    final BDMOASForeignIncomeEvidenceVO expectedVO =
      testforeignIncomeEvidenceList.get(0);
    final BDMOASForeignIncomeEvidenceVO resultVO =
      foreignIncomeEvidenceList.get(0);

    assertEquals("Created evidence list should match", expectedVO.getAmount(),
      resultVO.getAmount());
    assertEquals("Created evidence list should match",
      expectedVO.getCanadianAmount(), resultVO.getCanadianAmount());
    assertEquals("Created evidence list should match",
      expectedVO.getCountry(), resultVO.getCountry());
    assertEquals("Created evidence list should match",
      expectedVO.getEvidenceID(), resultVO.getEvidenceID());
    assertEquals("Created evidence list should match",
      expectedVO.getCurrency(), resultVO.getCurrency());
    assertEquals("Created evidence list should match",
      expectedVO.getIncomeType(), resultVO.getIncomeType());
    assertEquals("Created evidence list should match",
      expectedVO.getOtherDescription(), resultVO.getOtherDescription());
    assertEquals("Created evidence list should match", expectedVO.getYear(),
      resultVO.getYear());

  }

  /**
   * Verifies evidence is modified correctly if it exists
   *
   * @throws AppException
   * @throws InformationalException
   */

  @Test
  public void testCreateForeignIncomeEvidence_Exists() throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASForeignIncomeEvidenceVO> foreignIncomeEvidenceList =
      setUpVOList(-584395839);

    BDMOASForeignIncomeEvidence.createIncomeEvidence(
      pdcPersonDetails.concernRoleID, foreignIncomeEvidenceList,
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
  public void testCreateForeignIncomeEvidence_NotExists() throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASForeignIncomeEvidenceVO> foreignIncomeEvidenceList =
      setUpVOList(0);

    BDMOASForeignIncomeEvidence.createIncomeEvidence(
      pdcPersonDetails.concernRoleID, foreignIncomeEvidenceList,
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
              CASEEVIDENCE.OAS_FOREIGN_INCOME, (HashMap<String, String>) any,
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
              (HashMap<String, String>) any, CASEEVIDENCE.OAS_FOREIGN_INCOME,
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
